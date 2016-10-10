//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
package gurux.dlms.secure;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSException;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;

/**
 * Implements GMAC. This class is based to this doc:
 * http://csrc.nist.gov/publications/nistpubs/800-38D/SP-800-38D.pdf
 */
class GXDLMSChipperingStream {
    private GXByteBuffer output = new GXByteBuffer();
    // Consts.
    private static final int BLOCK_SIZE = 16;
    private static final int TAG_SIZE = 0x10;
    private static final byte[] IV = { (byte) 0xA6, (byte) 0xA6, (byte) 0xA6,
            (byte) 0xA6, (byte) 0xA6, (byte) 0xA6, (byte) 0xA6, (byte) 0xA6 };

    private Security security;
    // Properties.
    private final int[][][] mArray = new int[32][][];
    private long totalLength;
    private final byte[] zeroes = new byte[BLOCK_SIZE];
    private byte[] s;
    private byte[] counter;
    private byte[] aad;
    private byte[] j0;
    // How many bytes are not crypted/encrypted.
    private int bytesRemaining;
    private byte[] h;
    private byte[] bufBlock;
    private byte[] tag;
    private int rounds;
    private int[][] workingKey;
    private int c0, c1, c2, c3;
    private boolean encrypt;

    /**
     * Constructor.
     * 
     * @param encrypt
     *            Is data encrypt or decrypt.
     * @param kek
     *            Key Encrypting Key, also known as Master key.
     */
    GXDLMSChipperingStream(final boolean forEncrypt, final byte[] kek) {
        encrypt = forEncrypt;
        workingKey = generateKey(encrypt, kek);
    }

    /**
     * Clone byte array.
     * 
     * @param value
     *            Source array.
     * @return cloned array.
     */
    private byte[] clone(final byte[] value) {
        byte[] tmp = new byte[value.length];
        System.arraycopy(value, 0, tmp, 0, value.length);
        return tmp;
    }

    /**
     * Clone int array.
     * 
     * @param value
     *            Source array.
     * @return cloned array.
     */
    private int[] clone(final int[] value) {
        int[] tmp = new int[value.length];
        System.arraycopy(value, 0, tmp, 0, value.length);
        return tmp;
    }

    /**
     * Constructor.
     * 
     * @param forSecurity
     *            Used security level.
     * @param forEncrypt
     * @param blockCipherKey
     * @param forAad
     * @param iv
     * @param forTag
     */
    GXDLMSChipperingStream(final Security forSecurity, final boolean forEncrypt,
            final byte[] blockCipherKey, final byte[] forAad, final byte[] iv,
            final byte[] forTag) {
        security = forSecurity;
        this.tag = forTag;
        if (this.tag == null) {
            // Tag size is 12 bytes.
            this.tag = new byte[12];
        } else if (this.tag.length != 12) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        encrypt = forEncrypt;
        workingKey = generateKey(true, blockCipherKey);
        int bufLength;
        if (encrypt) {
            bufLength = BLOCK_SIZE;
        } else {
            bufLength = (BLOCK_SIZE + TAG_SIZE);
        }
        this.bufBlock = new byte[bufLength];
        aad = forAad;
        this.h = new byte[BLOCK_SIZE];
        processBlock(h, 0, h, 0);
        init(h);
        this.j0 = new byte[16];
        System.arraycopy(iv, 0, j0, 0, iv.length);
        this.j0[15] = 0x01;
        this.s = getGHash(aad);
        this.counter = clone(j0);
        this.bytesRemaining = 0;
        this.totalLength = 0;
    }

    /**
     * Get tag from crypted data.
     * 
     * @return Tag from crypted data.
     */
    public byte[] getTag() {
        return tag;
    }

    /**
     * Convert byte array to Little Endian.
     * 
     * @param data
     * @param offset
     * @return
     */
    private static int toUInt32(final byte[] value, final int offset) {
        int tmp = value[offset] & 0xFF;
        tmp |= (value[offset + 1] << 8) & 0xFF00;
        tmp |= (value[offset + 2] << 16) & 0xFF0000;
        tmp |= (value[offset + 3] << 24) & 0xFF000000;
        return tmp;
    }

    private static int subWord(final int value) {
        int tmp = S_BOX[value & 0xFF] & 0xFF;
        tmp |= (((S_BOX[(value >>> 8) & 0xFF]) & 0xFF) << 8) & 0xFF00;
        tmp |= (((S_BOX[(value >>> 16) & 0xFF]) & 0xFF) << 16) & 0xFF0000;
        tmp |= (((S_BOX[(value >>> 24) & 0xFF]) & 0xFF) << 24) & 0xFF000000;
        return tmp;
    }

    /**
     * Shift value.
     * 
     * @param r
     * @param shift
     * @return
     */
    private int shift(final int value, final int shift) {
        return (value >>> shift) | (value << (32 - shift)) & 0xFFFFFFFF;
    }

    /**
     * Key schedule Vector (powers of x).
     */
    private static final byte[] R_CON = { (byte) 0x01, (byte) 0x02, (byte) 0x04,
            (byte) 0x08, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80,
            (byte) 0x1b, (byte) 0x36, (byte) 0x6c, (byte) 0xd8, (byte) 0xab,
            (byte) 0x4d, (byte) 0x9a, (byte) 0x2f, (byte) 0x5e, (byte) 0xbc,
            (byte) 0x63, (byte) 0xc6, (byte) 0x97, (byte) 0x35, (byte) 0x6a,
            (byte) 0xd4, (byte) 0xb3, (byte) 0x7d, (byte) 0xfa, (byte) 0xef,
            (byte) 0xc5, (byte) 0x91 };

    /**
     * S box
     */
    private static final byte[] S_BOX = { (byte) 0x63, (byte) 0x7C, (byte) 0x77,
            (byte) 0x7B, (byte) 0xF2, (byte) 0x6B, (byte) 0x6F, (byte) 0xC5,
            (byte) 0x30, (byte) 0x01, (byte) 0x67, (byte) 0x2B, (byte) 0xFE,
            (byte) 0xD7, (byte) 0xAB, (byte) 0x76, (byte) 0xCA, (byte) 0x82,
            (byte) 0xC9, (byte) 0x7D, (byte) 0xFA, (byte) 0x59, (byte) 0x47,
            (byte) 0xF0, (byte) 0xAD, (byte) 0xD4, (byte) 0xA2, (byte) 0xAF,
            (byte) 0x9C, (byte) 0xA4, (byte) 0x72, (byte) 0xC0, (byte) 0xB7,
            (byte) 0xFD, (byte) 0x93, (byte) 0x26, (byte) 0x36, (byte) 0x3F,
            (byte) 0xF7, (byte) 0xCC, (byte) 0x34, (byte) 0xA5, (byte) 0xE5,
            (byte) 0xF1, (byte) 0x71, (byte) 0xD8, (byte) 0x31, (byte) 0x15,
            (byte) 0x04, (byte) 0xC7, (byte) 0x23, (byte) 0xC3, (byte) 0x18,
            (byte) 0x96, (byte) 0x05, (byte) 0x9A, (byte) 0x07, (byte) 0x12,
            (byte) 0x80, (byte) 0xE2, (byte) 0xEB, (byte) 0x27, (byte) 0xB2,
            (byte) 0x75, (byte) 0x09, (byte) 0x83, (byte) 0x2C, (byte) 0x1A,
            (byte) 0x1B, (byte) 0x6E, (byte) 0x5A, (byte) 0xA0, (byte) 0x52,
            (byte) 0x3B, (byte) 0xD6, (byte) 0xB3, (byte) 0x29, (byte) 0xE3,
            (byte) 0x2F, (byte) 0x84, (byte) 0x53, (byte) 0xD1, (byte) 0x00,
            (byte) 0xED, (byte) 0x20, (byte) 0xFC, (byte) 0xB1, (byte) 0x5B,
            (byte) 0x6A, (byte) 0xCB, (byte) 0xBE, (byte) 0x39, (byte) 0x4A,
            (byte) 0x4C, (byte) 0x58, (byte) 0xCF, (byte) 0xD0, (byte) 0xEF,
            (byte) 0xAA, (byte) 0xFB, (byte) 0x43, (byte) 0x4D, (byte) 0x33,
            (byte) 0x85, (byte) 0x45, (byte) 0xF9, (byte) 0x02, (byte) 0x7F,
            (byte) 0x50, (byte) 0x3C, (byte) 0x9F, (byte) 0xA8, (byte) 0x51,
            (byte) 0xA3, (byte) 0x40, (byte) 0x8F, (byte) 0x92, (byte) 0x9D,
            (byte) 0x38, (byte) 0xF5, (byte) 0xBC, (byte) 0xB6, (byte) 0xDA,
            (byte) 0x21, (byte) 0x10, (byte) 0xFF, (byte) 0xF3, (byte) 0xD2,
            (byte) 0xCD, (byte) 0x0C, (byte) 0x13, (byte) 0xEC, (byte) 0x5F,
            (byte) 0x97, (byte) 0x44, (byte) 0x17, (byte) 0xC4, (byte) 0xA7,
            (byte) 0x7E, (byte) 0x3D, (byte) 0x64, (byte) 0x5D, (byte) 0x19,
            (byte) 0x73, (byte) 0x60, (byte) 0x81, (byte) 0x4F, (byte) 0xDC,
            (byte) 0x22, (byte) 0x2A, (byte) 0x90, (byte) 0x88, (byte) 0x46,
            (byte) 0xEE, (byte) 0xB8, (byte) 0x14, (byte) 0xDE, (byte) 0x5E,
            (byte) 0x0B, (byte) 0xDB, (byte) 0xE0, (byte) 0x32, (byte) 0x3A,
            (byte) 0x0A, (byte) 0x49, (byte) 0x06, (byte) 0x24, (byte) 0x5C,
            (byte) 0xC2, (byte) 0xD3, (byte) 0xAC, (byte) 0x62, (byte) 0x91,
            (byte) 0x95, (byte) 0xE4, (byte) 0x79, (byte) 0xE7, (byte) 0xC8,
            (byte) 0x37, (byte) 0x6D, (byte) 0x8D, (byte) 0xD5, (byte) 0x4E,
            (byte) 0xA9, (byte) 0x6C, (byte) 0x56, (byte) 0xF4, (byte) 0xEA,
            (byte) 0x65, (byte) 0x7A, (byte) 0xAE, (byte) 0x08, (byte) 0xBA,
            (byte) 0x78, (byte) 0x25, (byte) 0x2E, (byte) 0x1C, (byte) 0xA6,
            (byte) 0xB4, (byte) 0xC6, (byte) 0xE8, (byte) 0xDD, (byte) 0x74,
            (byte) 0x1F, (byte) 0x4B, (byte) 0xBD, (byte) 0x8B, (byte) 0x8A,
            (byte) 0x70, (byte) 0x3E, (byte) 0xB5, (byte) 0x66, (byte) 0x48,
            (byte) 0x03, (byte) 0xF6, (byte) 0x0E, (byte) 0x61, (byte) 0x35,
            (byte) 0x57, (byte) 0xB9, (byte) 0x86, (byte) 0xC1, (byte) 0x1D,
            (byte) 0x9E, (byte) 0xE1, (byte) 0xF8, (byte) 0x98, (byte) 0x11,
            (byte) 0x69, (byte) 0xD9, (byte) 0x8E, (byte) 0x94, (byte) 0x9B,
            (byte) 0x1E, (byte) 0x87, (byte) 0xE9, (byte) 0xCE, (byte) 0x55,
            (byte) 0x28, (byte) 0xDF, (byte) 0x8C, (byte) 0xA1, (byte) 0x89,
            (byte) 0x0D, (byte) 0xBF, (byte) 0xE6, (byte) 0x42, (byte) 0x68,
            (byte) 0x41, (byte) 0x99, (byte) 0x2D, (byte) 0x0F, (byte) 0xB0,
            (byte) 0x54, (byte) 0xBB, (byte) 0x16 };

    /**
     * Inverse sbox
     */
    private static final byte[] S_BOX_REVERSED = { 0x52, (byte) 0x09,
            (byte) 0x6a, (byte) 0xd5, (byte) 0x30, (byte) 0x36, (byte) 0xa5,
            (byte) 0x38, (byte) 0xbf, (byte) 0x40, (byte) 0xa3, (byte) 0x9e,
            (byte) 0x81, (byte) 0xf3, (byte) 0xd7, (byte) 0xfb, (byte) 0x7c,
            (byte) 0xe3, (byte) 0x39, (byte) 0x82, (byte) 0x9b, (byte) 0x2f,
            (byte) 0xff, (byte) 0x87, (byte) 0x34, (byte) 0x8e, (byte) 0x43,
            (byte) 0x44, (byte) 0xc4, (byte) 0xde, (byte) 0xe9, (byte) 0xcb,
            (byte) 0x54, (byte) 0x7b, (byte) 0x94, (byte) 0x32, (byte) 0xa6,
            (byte) 0xc2, (byte) 0x23, (byte) 0x3d, (byte) 0xee, (byte) 0x4c,
            (byte) 0x95, (byte) 0x0b, (byte) 0x42, (byte) 0xfa, (byte) 0xc3,
            (byte) 0x4e, (byte) 0x08, (byte) 0x2e, (byte) 0xa1, (byte) 0x66,
            (byte) 0x28, (byte) 0xd9, (byte) 0x24, (byte) 0xb2, (byte) 0x76,
            (byte) 0x5b, (byte) 0xa2, (byte) 0x49, (byte) 0x6d, (byte) 0x8b,
            (byte) 0xd1, (byte) 0x25, (byte) 0x72, (byte) 0xf8, (byte) 0xf6,
            (byte) 0x64, (byte) 0x86, (byte) 0x68, (byte) 0x98, (byte) 0x16,
            (byte) 0xd4, (byte) 0xa4, (byte) 0x5c, (byte) 0xcc, (byte) 0x5d,
            (byte) 0x65, (byte) 0xb6, (byte) 0x92, (byte) 0x6c, (byte) 0x70,
            (byte) 0x48, (byte) 0x50, (byte) 0xfd, (byte) 0xed, (byte) 0xb9,
            (byte) 0xda, (byte) 0x5e, (byte) 0x15, (byte) 0x46, (byte) 0x57,
            (byte) 0xa7, (byte) 0x8d, (byte) 0x9d, (byte) 0x84, (byte) 0x90,
            (byte) 0xd8, (byte) 0xab, (byte) 0x00, (byte) 0x8c, (byte) 0xbc,
            (byte) 0xd3, (byte) 0x0a, (byte) 0xf7, (byte) 0xe4, (byte) 0x58,
            (byte) 0x05, (byte) 0xb8, (byte) 0xb3, (byte) 0x45, (byte) 0x06,
            (byte) 0xd0, (byte) 0x2c, (byte) 0x1e, (byte) 0x8f, (byte) 0xca,
            (byte) 0x3f, (byte) 0x0f, (byte) 0x02, (byte) 0xc1, (byte) 0xaf,
            (byte) 0xbd, (byte) 0x03, (byte) 0x01, (byte) 0x13, (byte) 0x8a,
            (byte) 0x6b, (byte) 0x3a, (byte) 0x91, (byte) 0x11, (byte) 0x41,
            (byte) 0x4f, (byte) 0x67, (byte) 0xdc, (byte) 0xea, (byte) 0x97,
            (byte) 0xf2, (byte) 0xcf, (byte) 0xce, (byte) 0xf0, (byte) 0xb4,
            (byte) 0xe6, (byte) 0x73, (byte) 0x96, (byte) 0xac, (byte) 0x74,
            (byte) 0x22, (byte) 0xe7, (byte) 0xad, (byte) 0x35, (byte) 0x85,
            (byte) 0xe2, (byte) 0xf9, (byte) 0x37, (byte) 0xe8, (byte) 0x1c,
            (byte) 0x75, (byte) 0xdf, (byte) 0x6e, (byte) 0x47, (byte) 0xf1,
            (byte) 0x1a, (byte) 0x71, (byte) 0x1d, (byte) 0x29, (byte) 0xc5,
            (byte) 0x89, (byte) 0x6f, (byte) 0xb7, (byte) 0x62, (byte) 0x0e,
            (byte) 0xaa, (byte) 0x18, (byte) 0xbe, (byte) 0x1b, (byte) 0xfc,
            (byte) 0x56, (byte) 0x3e, (byte) 0x4b, (byte) 0xc6, (byte) 0xd2,
            (byte) 0x79, (byte) 0x20, (byte) 0x9a, (byte) 0xdb, (byte) 0xc0,
            (byte) 0xfe, (byte) 0x78, (byte) 0xcd, (byte) 0x5a, (byte) 0xf4,
            (byte) 0x1f, (byte) 0xdd, (byte) 0xa8, (byte) 0x33, (byte) 0x88,
            (byte) 0x07, (byte) 0xc7, (byte) 0x31, (byte) 0xb1, (byte) 0x12,
            (byte) 0x10, (byte) 0x59, (byte) 0x27, (byte) 0x80, (byte) 0xec,
            (byte) 0x5f, (byte) 0x60, (byte) 0x51, (byte) 0x7f, (byte) 0xa9,
            (byte) 0x19, (byte) 0xb5, (byte) 0x4a, (byte) 0x0d, (byte) 0x2d,
            (byte) 0xe5, (byte) 0x7a, (byte) 0x9f, (byte) 0x93, (byte) 0xc9,
            (byte) 0x9c, (byte) 0xef, (byte) 0xa0, (byte) 0xe0, (byte) 0x3b,
            (byte) 0x4d, (byte) 0xae, (byte) 0x2a, (byte) 0xf5, (byte) 0xb0,
            (byte) 0xc8, (byte) 0xeb, (byte) 0xbb, (byte) 0x3c, (byte) 0x83,
            (byte) 0x53, (byte) 0x99, (byte) 0x61, (byte) 0x17, (byte) 0x2b,
            (byte) 0x04, (byte) 0x7e, (byte) 0xba, (byte) 0x77, (byte) 0xd6,
            (byte) 0x26, (byte) 0xe1, (byte) 0x69, (byte) 0x14, (byte) 0x63,
            (byte) 0x55, (byte) 0x21, (byte) 0x0c, (byte) 0x7d };

    /**
     * Rijndael (AES) Encryption fast table.
     */
    // CHECKSTYLE:OFF
    private static final int[] AES = { 0xa56363c6, 0x847c7cf8, 0x997777ee,
            0x8d7b7bf6, 0x0df2f2ff, 0xbd6b6bd6, 0xb16f6fde, 0x54c5c591,
            0x50303060, 0x03010102, 0xa96767ce, 0x7d2b2b56, 0x19fefee7,
            0x62d7d7b5, 0xe6abab4d, 0x9a7676ec, 0x45caca8f, 0x9d82821f,
            0x40c9c989, 0x877d7dfa, 0x15fafaef, 0xeb5959b2, 0xc947478e,
            0x0bf0f0fb, 0xecadad41, 0x67d4d4b3, 0xfda2a25f, 0xeaafaf45,
            0xbf9c9c23, 0xf7a4a453, 0x967272e4, 0x5bc0c09b, 0xc2b7b775,
            0x1cfdfde1, 0xae93933d, 0x6a26264c, 0x5a36366c, 0x413f3f7e,
            0x02f7f7f5, 0x4fcccc83, 0x5c343468, 0xf4a5a551, 0x34e5e5d1,
            0x08f1f1f9, 0x937171e2, 0x73d8d8ab, 0x53313162, 0x3f15152a,
            0x0c040408, 0x52c7c795, 0x65232346, 0x5ec3c39d, 0x28181830,
            0xa1969637, 0x0f05050a, 0xb59a9a2f, 0x0907070e, 0x36121224,
            0x9b80801b, 0x3de2e2df, 0x26ebebcd, 0x6927274e, 0xcdb2b27f,
            0x9f7575ea, 0x1b090912, 0x9e83831d, 0x742c2c58, 0x2e1a1a34,
            0x2d1b1b36, 0xb26e6edc, 0xee5a5ab4, 0xfba0a05b, 0xf65252a4,
            0x4d3b3b76, 0x61d6d6b7, 0xceb3b37d, 0x7b292952, 0x3ee3e3dd,
            0x712f2f5e, 0x97848413, 0xf55353a6, 0x68d1d1b9, 0x00000000,
            0x2cededc1, 0x60202040, 0x1ffcfce3, 0xc8b1b179, 0xed5b5bb6,
            0xbe6a6ad4, 0x46cbcb8d, 0xd9bebe67, 0x4b393972, 0xde4a4a94,
            0xd44c4c98, 0xe85858b0, 0x4acfcf85, 0x6bd0d0bb, 0x2aefefc5,
            0xe5aaaa4f, 0x16fbfbed, 0xc5434386, 0xd74d4d9a, 0x55333366,
            0x94858511, 0xcf45458a, 0x10f9f9e9, 0x06020204, 0x817f7ffe,
            0xf05050a0, 0x443c3c78, 0xba9f9f25, 0xe3a8a84b, 0xf35151a2,
            0xfea3a35d, 0xc0404080, 0x8a8f8f05, 0xad92923f, 0xbc9d9d21,
            0x48383870, 0x04f5f5f1, 0xdfbcbc63, 0xc1b6b677, 0x75dadaaf,
            0x63212142, 0x30101020, 0x1affffe5, 0x0ef3f3fd, 0x6dd2d2bf,
            0x4ccdcd81, 0x140c0c18, 0x35131326, 0x2fececc3, 0xe15f5fbe,
            0xa2979735, 0xcc444488, 0x3917172e, 0x57c4c493, 0xf2a7a755,
            0x827e7efc, 0x473d3d7a, 0xac6464c8, 0xe75d5dba, 0x2b191932,
            0x957373e6, 0xa06060c0, 0x98818119, 0xd14f4f9e, 0x7fdcdca3,
            0x66222244, 0x7e2a2a54, 0xab90903b, 0x8388880b, 0xca46468c,
            0x29eeeec7, 0xd3b8b86b, 0x3c141428, 0x79dedea7, 0xe25e5ebc,
            0x1d0b0b16, 0x76dbdbad, 0x3be0e0db, 0x56323264, 0x4e3a3a74,
            0x1e0a0a14, 0xdb494992, 0x0a06060c, 0x6c242448, 0xe45c5cb8,
            0x5dc2c29f, 0x6ed3d3bd, 0xefacac43, 0xa66262c4, 0xa8919139,
            0xa4959531, 0x37e4e4d3, 0x8b7979f2, 0x32e7e7d5, 0x43c8c88b,
            0x5937376e, 0xb76d6dda, 0x8c8d8d01, 0x64d5d5b1, 0xd24e4e9c,
            0xe0a9a949, 0xb46c6cd8, 0xfa5656ac, 0x07f4f4f3, 0x25eaeacf,
            0xaf6565ca, 0x8e7a7af4, 0xe9aeae47, 0x18080810, 0xd5baba6f,
            0x887878f0, 0x6f25254a, 0x722e2e5c, 0x241c1c38, 0xf1a6a657,
            0xc7b4b473, 0x51c6c697, 0x23e8e8cb, 0x7cdddda1, 0x9c7474e8,
            0x211f1f3e, 0xdd4b4b96, 0xdcbdbd61, 0x868b8b0d, 0x858a8a0f,
            0x907070e0, 0x423e3e7c, 0xc4b5b571, 0xaa6666cc, 0xd8484890,
            0x05030306, 0x01f6f6f7, 0x120e0e1c, 0xa36161c2, 0x5f35356a,
            0xf95757ae, 0xd0b9b969, 0x91868617, 0x58c1c199, 0x271d1d3a,
            0xb99e9e27, 0x38e1e1d9, 0x13f8f8eb, 0xb398982b, 0x33111122,
            0xbb6969d2, 0x70d9d9a9, 0x898e8e07, 0xa7949433, 0xb69b9b2d,
            0x221e1e3c, 0x92878715, 0x20e9e9c9, 0x49cece87, 0xff5555aa,
            0x78282850, 0x7adfdfa5, 0x8f8c8c03, 0xf8a1a159, 0x80898909,
            0x170d0d1a, 0xdabfbf65, 0x31e6e6d7, 0xc6424284, 0xb86868d0,
            0xc3414182, 0xb0999929, 0x772d2d5a, 0x110f0f1e, 0xcbb0b07b,
            0xfc5454a8, 0xd6bbbb6d, 0x3a16162c };

    private static final int[] AES1_REVERSED = { 0x50a7f451, 0x5365417e,
            0xc3a4171a, 0x965e273a, 0xcb6bab3b, 0xf1459d1f, 0xab58faac,
            0x9303e34b, 0x55fa3020, 0xf66d76ad, 0x9176cc88, 0x254c02f5,
            0xfcd7e54f, 0xd7cb2ac5, 0x80443526, 0x8fa362b5, 0x495ab1de,
            0x671bba25, 0x980eea45, 0xe1c0fe5d, 0x02752fc3, 0x12f04c81,
            0xa397468d, 0xc6f9d36b, 0xe75f8f03, 0x959c9215, 0xeb7a6dbf,
            0xda595295, 0x2d83bed4, 0xd3217458, 0x2969e049, 0x44c8c98e,
            0x6a89c275, 0x78798ef4, 0x6b3e5899, 0xdd71b927, 0xb64fe1be,
            0x17ad88f0, 0x66ac20c9, 0xb43ace7d, 0x184adf63, 0x82311ae5,
            0x60335197, 0x457f5362, 0xe07764b1, 0x84ae6bbb, 0x1ca081fe,
            0x942b08f9, 0x58684870, 0x19fd458f, 0x876cde94, 0xb7f87b52,
            0x23d373ab, 0xe2024b72, 0x578f1fe3, 0x2aab5566, 0x0728ebb2,
            0x03c2b52f, 0x9a7bc586, 0xa50837d3, 0xf2872830, 0xb2a5bf23,
            0xba6a0302, 0x5c8216ed, 0x2b1ccf8a, 0x92b479a7, 0xf0f207f3,
            0xa1e2694e, 0xcdf4da65, 0xd5be0506, 0x1f6234d1, 0x8afea6c4,
            0x9d532e34, 0xa055f3a2, 0x32e18a05, 0x75ebf6a4, 0x39ec830b,
            0xaaef6040, 0x069f715e, 0x51106ebd, 0xf98a213e, 0x3d06dd96,
            0xae053edd, 0x46bde64d, 0xb58d5491, 0x055dc471, 0x6fd40604,
            0xff155060, 0x24fb9819, 0x97e9bdd6, 0xcc434089, 0x779ed967,
            0xbd42e8b0, 0x888b8907, 0x385b19e7, 0xdbeec879, 0x470a7ca1,
            0xe90f427c, 0xc91e84f8, 0x00000000, 0x83868009, 0x48ed2b32,
            0xac70111e, 0x4e725a6c, 0xfbff0efd, 0x5638850f, 0x1ed5ae3d,
            0x27392d36, 0x64d90f0a, 0x21a65c68, 0xd1545b9b, 0x3a2e3624,
            0xb1670a0c, 0x0fe75793, 0xd296eeb4, 0x9e919b1b, 0x4fc5c080,
            0xa220dc61, 0x694b775a, 0x161a121c, 0x0aba93e2, 0xe52aa0c0,
            0x43e0223c, 0x1d171b12, 0x0b0d090e, 0xadc78bf2, 0xb9a8b62d,
            0xc8a91e14, 0x8519f157, 0x4c0775af, 0xbbdd99ee, 0xfd607fa3,
            0x9f2601f7, 0xbcf5725c, 0xc53b6644, 0x347efb5b, 0x7629438b,
            0xdcc623cb, 0x68fcedb6, 0x63f1e4b8, 0xcadc31d7, 0x10856342,
            0x40229713, 0x2011c684, 0x7d244a85, 0xf83dbbd2, 0x1132f9ae,
            0x6da129c7, 0x4b2f9e1d, 0xf330b2dc, 0xec52860d, 0xd0e3c177,
            0x6c16b32b, 0x99b970a9, 0xfa489411, 0x2264e947, 0xc48cfca8,
            0x1a3ff0a0, 0xd82c7d56, 0xef903322, 0xc74e4987, 0xc1d138d9,
            0xfea2ca8c, 0x360bd498, 0xcf81f5a6, 0x28de7aa5, 0x268eb7da,
            0xa4bfad3f, 0xe49d3a2c, 0x0d927850, 0x9bcc5f6a, 0x62467e54,
            0xc2138df6, 0xe8b8d890, 0x5ef7392e, 0xf5afc382, 0xbe805d9f,
            0x7c93d069, 0xa92dd56f, 0xb31225cf, 0x3b99acc8, 0xa77d1810,
            0x6e639ce8, 0x7bbb3bdb, 0x097826cd, 0xf418596e, 0x01b79aec,
            0xa89a4f83, 0x656e95e6, 0x7ee6ffaa, 0x08cfbc21, 0xe6e815ef,
            0xd99be7ba, 0xce366f4a, 0xd4099fea, 0xd67cb029, 0xafb2a431,
            0x31233f2a, 0x3094a5c6, 0xc066a235, 0x37bc4e74, 0xa6ca82fc,
            0xb0d090e0, 0x15d8a733, 0x4a9804f1, 0xf7daec41, 0x0e50cd7f,
            0x2ff69117, 0x8dd64d76, 0x4db0ef43, 0x544daacc, 0xdf0496e4,
            0xe3b5d19e, 0x1b886a4c, 0xb81f2cc1, 0x7f516546, 0x04ea5e9d,
            0x5d358c01, 0x737487fa, 0x2e410bfb, 0x5a1d67b3, 0x52d2db92,
            0x335610e9, 0x1347d66d, 0x8c61d79a, 0x7a0ca137, 0x8e14f859,
            0x893c13eb, 0xee27a9ce, 0x35c961b7, 0xede51ce1, 0x3cb1477a,
            0x59dfd29c, 0x3f73f255, 0x79ce1418, 0xbf37c773, 0xeacdf753,
            0x5baafd5f, 0x146f3ddf, 0x86db4478, 0x81f3afca, 0x3ec468b9,
            0x2c342438, 0x5f40a3c2, 0x72c31d16, 0x0c25e2bc, 0x8b493c28,
            0x41950dff, 0x7101a839, 0xdeb30c08, 0x9ce4b4d8, 0x90c15664,
            0x6184cb7b, 0x70b632d5, 0x745c6c48, 0x4257b8d0 };
    // CHECKSTYLE:ON

    /**
     * Initialise the key schedule from the user supplied key.
     * 
     * @return
     */
    private int starX(final int value) {
        final int m1 = (int) 0x80808080;
        final int m2 = 0x7f7f7f7f;
        final int m3 = 0x0000001b;
        return ((value & m2) << 1) ^ (((value & m1) >>> 7) * m3);
    }

    private int imixCol(final int x) {
        int f2 = starX(x);
        int f4 = starX(f2);
        int f8 = starX(f4);
        int f9 = x ^ f8;
        return f2 ^ f4 ^ f8 ^ shift(f2 ^ f9, 8) ^ shift(f4 ^ f9, 16)
                ^ shift(f9, 24);
    }

    /**
     * Get bytes from UIn32.
     * 
     * @param value
     * @param data
     * @param offset
     */
    public static void getUInt32(final long value, final byte[] data,
            final int offset) {
        data[offset] = (byte) (value);
        data[offset + 1] = (byte) (value >>> 8);
        data[offset + 2] = (byte) (value >>> 16);
        data[offset + 3] = (byte) (value >>> 24);
    }

    private void unPackBlock(final byte[] bytes, final int offset) {
        c0 = toUInt32(bytes, offset);
        c1 = toUInt32(bytes, offset + 4);
        c2 = toUInt32(bytes, offset + 8);
        c3 = toUInt32(bytes, offset + 12);
    }

    private void packBlock(final byte[] bytes, final int offset) {
        getUInt32(c0, bytes, offset);
        getUInt32(c1, bytes, offset + 4);
        getUInt32(c2, bytes, offset + 8);
        getUInt32(c3, bytes, offset + 12);
    }

    /**
     * Encrypt data block.
     * 
     * @param key
     */
    private void encryptBlock(final int[][] key) {
        int r;
        int r0, r1, r2, r3;
        c0 ^= key[0][0];
        c1 ^= key[0][1];
        c2 ^= key[0][2];
        c3 ^= key[0][3];
        for (r = 1; r < rounds - 1;) {
            r0 = (AES[c0 & 0xFF] & 0xFFFFFFFF);
            r0 ^= (shift(AES[(c1 >> 8) & 0xFF], 24) & 0xFFFFFFFF);
            r0 ^= (shift(AES[(c2 >> 16) & 0xFF], 16) & 0xFFFFFFFF);
            r0 ^= (shift(AES[(c3 >> 24) & 0xFF], 8) & 0xFFFFFFFF);
            r0 ^= (key[r][0] & 0xFFFFFFFF);

            r1 = (AES[c1 & 0xFF] & 0xFFFFFFFF);
            r1 ^= shift(AES[(c2 >> 8) & 0xFF], 24) & 0xFFFFFFFF;
            r1 ^= shift(AES[(c3 >> 16) & 0xFF], 16) & 0xFFFFFFFF;
            r1 ^= shift(AES[(c0 >> 24) & 0xFF], 8) & 0xFFFFFFFF;
            r1 ^= key[r][1] & 0xFFFFFFFF;

            r2 = AES[c2 & 0xFF] & 0xFFFFFFFF;
            r2 ^= shift(AES[(c3 >> 8) & 0xFF], 24) & 0xFFFFFFFF;
            r2 ^= shift(AES[(c0 >> 16) & 0xFF], 16) & 0xFFFFFFFF;
            r2 ^= shift(AES[(c1 >> 24) & 0xFF], 8) & 0xFFFFFFFF;
            r2 ^= key[r][2] & 0xFFFFFFFF;

            r3 = AES[c3 & 0xFF] & 0xFFFFFFFF;
            r3 ^= shift(AES[(c0 >> 8) & 0xFF], 24) & 0xFFFFFFFF;
            r3 ^= shift(AES[(c1 >> 16) & 0xFF], 16) & 0xFFFFFFFF;
            r3 ^= shift(AES[(c2 >> 24) & 0xFF], 8) & 0xFFFFFFFF;
            r3 ^= key[r++][3] & 0xFFFFFFFF;

            c0 = AES[r0 & 0xFF] & 0xFFFFFFFF;
            c0 ^= shift(AES[(r1 >> 8) & 0xFF], 24) & 0xFFFFFFFF;
            c0 ^= shift(AES[(r2 >> 16) & 0xFF], 16) & 0xFFFFFFFF;
            c0 ^= shift(AES[(r3 >> 24) & 0xFF], 8) & 0xFFFFFFFF;
            c0 ^= key[r][0] & 0xFFFFFFFF;

            c1 = AES[r1 & 0xFF] & 0xFFFFFFFF;
            c1 ^= shift(AES[(r2 >>> 8) & 0xFF], 24) & 0xFFFFFFFF;
            c1 ^= shift(AES[(r3 >>> 16) & 0xFF], 16) & 0xFFFFFFFF;
            c1 ^= shift(AES[(r0 >>> 24) & 0xFF], 8) & 0xFFFFFFFF;
            c1 ^= key[r][1] & 0xFFFFFFFF;

            c2 = AES[r2 & 0xFF] & 0xFFFFFFFF;
            c2 ^= shift(AES[(r3 >>> 8) & 0xFF], 24) & 0xFFFFFFFF;
            c2 ^= shift(AES[(r0 >>> 16) & 0xFF], 16) & 0xFFFFFFFF;
            c2 ^= shift(AES[(r1 >>> 24) & 0xFF], 8) & 0xFFFFFFFF;
            c2 ^= key[r][2] & 0xFFFFFFFF;

            c3 = AES[r3 & 0xFF] & 0xFFFFFFFF;
            c3 ^= shift(AES[(r0 >>> 8) & 0xFF], 24) & 0xFFFFFFFF;
            c3 ^= shift(AES[(r1 >>> 16) & 0xFF], 16) & 0xFFFFFFFF;
            c3 ^= shift(AES[(r2 >>> 24) & 0xFF], 8) & 0xFFFFFFFF;
            c3 ^= key[r++][3] & 0xFFFFFFFF;
        }
        r0 = AES[c0 & 0xFF] & 0xFFFFFFFF;
        r0 ^= shift(AES[(c1 >>> 8) & 0xFF], 24) & 0xFFFFFFFF;
        r0 ^= shift(AES[(c2 >>> 16) & 0xFF], 16) & 0xFFFFFFFF;
        r0 ^= shift(AES[c3 >>> 24], 8) & 0xFFFFFFFF;
        r0 ^= key[r][0] & 0xFFFFFFFF;
        r1 = AES[c1 & 0xFF] & 0xFFFFFFFF;
        r1 ^= shift(AES[(c2 >>> 8) & 0xFF], 24) & 0xFFFFFFFF;
        r1 ^= shift(AES[(c3 >>> 16) & 0xFF], 16) & 0xFFFFFFFF;
        r1 ^= shift(AES[c0 >>> 24], 8) & 0xFFFFFFFF;
        r1 ^= key[r][1] & 0xFFFFFFFF;
        r2 = AES[c2 & 0xFF] & 0xFFFFFFFF;
        r2 ^= shift(AES[(c3 >>> 8) & 0xFF], 24) & 0xFFFFFFFF;
        r2 ^= shift(AES[(c0 >>> 16) & 0xFF], 16) & 0xFFFFFFFF;
        r2 ^= shift(AES[c1 >>> 24], 8) & 0xFFFFFFFF;
        r2 ^= key[r][2] & 0xFFFFFFFF;
        r3 = AES[c3 & 0xFF] & 0xFFFFFFFF;
        r3 ^= shift(AES[(c0 >>> 8) & 0xFF], 24) & 0xFFFFFFFF;
        r3 ^= shift(AES[(c1 >>> 16) & 0xFF], 16) & 0xFFFFFFFF;
        r3 ^= shift(AES[c2 >>> 24], 8) & 0xFFFFFFFF;
        r3 ^= key[r++][3] & 0xFFFFFFFF;
        c0 = (S_BOX[r0 & 0xFF] & 0xFF) & 0xFFFFFFFF;
        c0 ^= ((S_BOX[(r1 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        c0 ^= ((S_BOX[(r2 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        c0 ^= ((S_BOX[r3 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        c0 ^= key[r][0] & 0xFFFFFFFF;
        c1 = (S_BOX[r1 & 0xFF] & 0xFF) & 0xFFFFFFFF;
        c1 ^= ((S_BOX[(r2 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        c1 ^= ((S_BOX[(r3 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        c1 ^= ((S_BOX[r0 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        c1 ^= key[r][1] & 0xFFFFFFFF;
        c2 = (S_BOX[r2 & 0xFF] & 0xFF) & 0xFFFFFFFF;
        c2 ^= ((S_BOX[(r3 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        c2 ^= ((S_BOX[(r0 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        c2 ^= ((S_BOX[r1 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        c2 ^= key[r][2] & 0xFFFFFFFF;
        c3 = (S_BOX[r3 & 0xFF] & 0xFF) & 0xFFFFFFFF;
        c3 ^= ((S_BOX[(r0 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        c3 ^= ((S_BOX[(r1 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        c3 ^= ((S_BOX[r2 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        c3 ^= key[r][3] & 0xFFFFFFFF;
    }

    private void decryptBlock(final int[][] key) {
        int t0 = this.c0 ^ key[rounds][0];
        int t1 = this.c1 ^ key[rounds][1];
        int t2 = this.c2 ^ key[rounds][2];

        int r0, r1, r2, r3 = this.c3 ^ key[rounds][3];
        int r = rounds - 1;
        while (r > 1) {
            r0 = (AES1_REVERSED[t0 & 255] & 0xFFFFFFFF)
                    ^ shift(AES1_REVERSED[(r3 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(t2 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(t1 >> 24) & 255], 8) ^ key[r][0];
            r1 = AES1_REVERSED[t1 & 255]
                    ^ shift(AES1_REVERSED[(t0 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(r3 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(t2 >> 24) & 255], 8) ^ key[r][1];
            r2 = AES1_REVERSED[t2 & 255]
                    ^ shift(AES1_REVERSED[(t1 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(t0 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(r3 >> 24) & 255], 8) ^ key[r][2];
            r3 = AES1_REVERSED[r3 & 255]
                    ^ shift(AES1_REVERSED[(t2 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(t1 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(t0 >> 24) & 255], 8) ^ key[r][3];
            --r;
            t0 = AES1_REVERSED[r0 & 255]
                    ^ shift(AES1_REVERSED[(r3 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(r2 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(r1 >> 24) & 255], 8) ^ key[r][0];
            t1 = AES1_REVERSED[r1 & 255]
                    ^ shift(AES1_REVERSED[(r0 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(r3 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(r2 >> 24) & 255], 8) ^ key[r][1];
            t2 = AES1_REVERSED[r2 & 255]
                    ^ shift(AES1_REVERSED[(r1 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(r0 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(r3 >> 24) & 255], 8) ^ key[r][2];
            r3 = AES1_REVERSED[r3 & 255]
                    ^ shift(AES1_REVERSED[(r2 >> 8) & 255], 24)
                    ^ shift(AES1_REVERSED[(r1 >> 16) & 255], 16)
                    ^ shift(AES1_REVERSED[(r0 >> 24) & 255], 8) ^ key[r][3];
            --r;
        }

        r = 1;
        r0 = AES1_REVERSED[t0 & 255] ^ shift(AES1_REVERSED[(r3 >> 8) & 255], 24)
                ^ shift(AES1_REVERSED[(t2 >> 16) & 255], 16)
                ^ shift(AES1_REVERSED[(t1 >> 24) & 255], 8) ^ key[r][0];
        r1 = AES1_REVERSED[t1 & 255] ^ shift(AES1_REVERSED[(t0 >> 8) & 255], 24)
                ^ shift(AES1_REVERSED[(r3 >> 16) & 255], 16)
                ^ shift(AES1_REVERSED[(t2 >> 24) & 255], 8) ^ key[r][1];
        r2 = AES1_REVERSED[t2 & 255] ^ shift(AES1_REVERSED[(t1 >> 8) & 255], 24)
                ^ shift(AES1_REVERSED[(t0 >> 16) & 255], 16)
                ^ shift(AES1_REVERSED[(r3 >> 24) & 255], 8) ^ key[r][2];
        r3 = AES1_REVERSED[r3 & 255] ^ shift(AES1_REVERSED[(t2 >> 8) & 255], 24)
                ^ shift(AES1_REVERSED[(t1 >> 16) & 255], 16)
                ^ shift(AES1_REVERSED[(t0 >> 24) & 255], 8) ^ key[r][3];

        r = 0;
        this.c0 = (S_BOX_REVERSED[r0 & 255] & 0xFF)
                ^ ((((S_BOX_REVERSED[(r3 >> 8) & 255]) & 0xFF) << 8))
                ^ ((((S_BOX_REVERSED[(r2 >> 16) & 255]) & 0xFF) << 16))
                ^ ((((S_BOX_REVERSED[(r1 >> 24) & 255]) & 0xFF) << 24))
                ^ key[r][0];
        this.c1 = (S_BOX_REVERSED[r1 & 255] & 0xFF)
                ^ (((S_BOX_REVERSED[(r0 >> 8) & 255]) & 0xFF) << 8)
                ^ (((S_BOX_REVERSED[(r3 >> 16) & 255]) & 0xFF) << 16)
                ^ (((S_BOX_REVERSED[(r2 >> 24) & 255]) & 0xFF) << 24)
                ^ key[r][1];
        this.c2 = (S_BOX_REVERSED[r2 & 255] & 0xFF)
                ^ (((S_BOX_REVERSED[(r1 >> 8) & 255]) & 0xFF) << 8)
                ^ (((S_BOX_REVERSED[(r0 >> 16) & 255]) & 0xFF) << 16)
                ^ (((S_BOX_REVERSED[(r3 >> 24) & 255]) & 0xFF) << 24)
                ^ key[r][2];
        this.c3 = (S_BOX_REVERSED[r3 & 255] & 0xFF)
                ^ (((S_BOX_REVERSED[(r2 >> 8) & 255]) & 0xFF) << 8)
                ^ (((S_BOX_REVERSED[(r1 >> 16) & 255]) & 0xFF) << 16)
                ^ (((S_BOX_REVERSED[(r0 >> 24) & 255]) & 0xFF) << 24)
                ^ key[r][3];
    }

    private int processBlock(final byte[] input, final int inOffset,
            final byte[] forOutput, final int outOffset) {
        if ((inOffset + (32 / 2)) > input.length) {
            throw new RuntimeException("input buffer too short");
        }

        if ((outOffset + (32 / 2)) > forOutput.length) {
            throw new RuntimeException("output buffer too short");
        }

        unPackBlock(input, inOffset);
        if (encrypt) {
            encryptBlock(workingKey);
        } else {
            decryptBlock(workingKey);
        }
        packBlock(forOutput, outOffset);
        return BLOCK_SIZE;
    }

    /**
     * Convert Big Endian byte array to Little Endian UInt 32.
     * 
     * @param bs
     * @param offset
     * @return
     */
    public static int bEToUInt32(final byte[] buff, final int offset) {
        int value = (buff[offset] << 24);
        value |= (buff[offset + 1] << 16) & 0xFF0000;
        value |= (buff[offset + 2] << 8) & 0xFF00;
        value |= buff[offset + 3] & 0xFF;
        return value;
    }

    /**
     * Shift block to right.
     * 
     * @param block
     *            Block list.
     * @param count
     *            Blocks to shift.
     */
    public static void shiftRight(final int[] block, final int count) {
        int bit = 0;
        for (int i = 0; i < 4; ++i) {
            int b = block[i];
            block[i] = (b >>> count) | bit;
            bit = b << (32 - count);
        }
    }

    public static void multiplyP(final int[] x) {
        boolean lsb = (x[3] & 1) != 0;
        shiftRight(x, 1);
        if (lsb) {
            x[0] ^= 0xe1000000L;
        }
    }

    /**
     * Get Uint 128 as array of UInt32.
     * 
     * @param buff
     * @return
     */
    public static int[] getUint128(final byte[] buff) {
        int[] us = new int[4];
        us[0] = bEToUInt32(buff, 0);
        us[1] = bEToUInt32(buff, 4);
        us[2] = bEToUInt32(buff, 8);
        us[3] = bEToUInt32(buff, 12);
        return us;
    }

    /**
     * Make Xor for 128 bits.
     * 
     * @param block
     *            block.
     * @param val
     */
    private static void xor(final byte[] block, final byte[] value) {
        for (int pos = 0; pos != 16; ++pos) {
            block[pos] ^= value[pos];
        }
    }

    /**
     * Make Xor for 128 bits.
     * 
     * @param block
     *            block.
     * @param val
     */
    private static void xor128(final int[] block, final int[] value) {
        for (int pos = 0; pos != 4; ++pos) {
            block[pos] ^= value[pos];
        }
    }

    private static void multiplyP8(final int[] x) {
        long lsw = x[3];
        shiftRight(x, 8);
        for (int pos = 0; pos != 8; ++pos) {
            if ((lsw & (1 << pos)) != 0) {
                x[0] ^= ((0xe1000000L >> (7 - pos)) & 0xFFFFFFFF);
            }
        }
    }

    /**
     * The GF(2128) field used is defined by the polynomial x^{128}+x^7+x^2+x+1.
     * The authentication tag is constructed by feeding blocks of data into the
     * GHASH function, and encrypting the result.
     * 
     * @param b
     * @return
     */
    private byte[] getGHash(final byte[] b) {
        byte[] y = new byte[16];
        for (int pos = 0; pos < b.length; pos += 16) {
            byte[] x = new byte[16];
            int cnt = Math.min(b.length - pos, 16);
            System.arraycopy(b, pos, x, 0, cnt);
            xor(y, x);
            multiplyH(y);
        }
        return y;
    }

    /**
     * Convert uint32 to Big Endian byte array.
     * 
     * @param value
     * @param buff
     * @param offset
     */
    private static void uInt32ToBE(final int value, final byte[] buff,
            final int offset) {
        buff[offset] = (byte) (value >>> 24);
        buff[offset + 1] = (byte) (value >>> 16);
        buff[offset + 2] = (byte) (value >>> 8);
        buff[offset + 3] = (byte) (value);
    }

    private void multiplyH(final byte[] value) {
        int[] tmp = new int[4];
        for (int pos = 0; pos != 16; ++pos) {
            int[] m = mArray[pos + pos][value[pos] & 0x0f];
            tmp[0] ^= m[0];
            tmp[1] ^= m[1];
            tmp[2] ^= m[2];
            tmp[3] ^= m[3];
            m = mArray[pos + pos + 1][(value[pos] & 0xf0) >> 4];
            tmp[0] ^= m[0];
            tmp[1] ^= m[1];
            tmp[2] ^= m[2];
            tmp[3] ^= m[3];
        }

        uInt32ToBE(tmp[0], value, 0);
        uInt32ToBE(tmp[1], value, 4);
        uInt32ToBE(tmp[2], value, 8);
        uInt32ToBE(tmp[3], value, 12);
    }

    private void init(final byte[] value) {
        mArray[0] = new int[16][];
        mArray[1] = new int[16][];
        mArray[0][0] = new int[4];
        mArray[1][0] = new int[4];
        mArray[1][8] = getUint128(value);
        int[] tmp;
        for (int pos = 4; pos >= 1; pos >>= 1) {
            tmp = clone(mArray[1][pos + pos]);
            multiplyP(tmp);
            mArray[1][pos] = tmp;
        }
        tmp = clone(mArray[1][1]);
        multiplyP(tmp);
        mArray[0][8] = tmp;

        for (int pos = 4; pos >= 1; pos >>= 1) {
            tmp = clone(mArray[0][pos + pos]);
            multiplyP(tmp);
            mArray[0][pos] = tmp;
        }

        for (int pos1 = 0;;) {
            for (int pos2 = 2; pos2 < 16; pos2 += pos2) {
                for (int k = 1; k < pos2; ++k) {
                    tmp = clone(mArray[pos1][pos2]);
                    xor128(tmp, mArray[pos1][k]);
                    mArray[pos1][pos2 + k] = tmp;
                }
            }

            if (++pos1 == 32) {
                return;
            }

            if (pos1 > 1) {
                mArray[pos1] = new int[16][];
                mArray[pos1][0] = new int[4];
                for (int pos = 8; pos > 0; pos >>= 1) {
                    tmp = clone(mArray[pos1 - 2][pos]);
                    multiplyP8(tmp);
                    mArray[pos1][pos] = tmp;
                }
            }
        }
    }

    private void gCTRBlock(final byte[] buf, final int bufCount) {
        for (int i = 15; i >= 12; --i) {
            if (++counter[i] != 0) {
                break;
            }
        }

        byte[] tmp = new byte[BLOCK_SIZE];
        processBlock(counter, 0, tmp, 0);
        byte[] hashBytes;
        if (encrypt) {
            System.arraycopy(zeroes, bufCount, tmp, bufCount,
                    BLOCK_SIZE - bufCount);
            hashBytes = tmp;
        } else {
            hashBytes = buf;
        }
        for (int pos = 0; pos != bufCount; ++pos) {
            tmp[pos] ^= buf[pos];
            output.setUInt8(tmp[pos]);
        }
        xor(s, hashBytes);
        multiplyH(s);
        totalLength += (long) bufCount;
    }

    /**
     * Set packet length to byte array.
     * 
     * @param length
     * @param buff
     * @param offset
     */
    private static void setPackLength(final long length, final byte[] buff,
            final int offset) {
        uInt32ToBE((int) (length >> 32), buff, offset);
        uInt32ToBE((int) length, buff, offset + 4);
    }

    /**
     * Reset
     */
    private void reset() {
        s = getGHash(aad);
        counter = clone(j0);
        bytesRemaining = 0;
        totalLength = 0;
    }

    /**
     * Are tags equals.
     * 
     * @param tag1
     *            Tag 1.
     * @param tag2
     *            Tag 2.
     * @return Returns true if tags content is the same.
     */
    public static boolean tagsEquals(final byte[] tag1, final byte[] tag2) {
        for (int pos = 0; pos != 12; ++pos) {
            if (tag1[pos] != tag2[pos]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Write bytes to decrypt/encrypt.
     * 
     * @param input
     */
    public void write(final byte[] input) {
        for (byte it : input) {
            bufBlock[bytesRemaining++] = it;
            if (bytesRemaining == BLOCK_SIZE) {
                gCTRBlock(bufBlock, BLOCK_SIZE);
                if (!encrypt) {
                    System.arraycopy(bufBlock, BLOCK_SIZE, bufBlock, 0,
                            tag.length);
                }
                bytesRemaining = 0;
            }
        }
    }

    /**
     * Process encrypting/decrypting.
     * 
     * @return
     */
    public final byte[] flushFinalBlock() {
        // Crypt/Uncrypt remaining bytes.
        if (bytesRemaining > 0) {
            byte[] tmp = new byte[BLOCK_SIZE];
            System.arraycopy(bufBlock, 0, tmp, 0, bytesRemaining);
            gCTRBlock(tmp, bytesRemaining);
        }
        // If tag is not needed.
        if (security == Security.ENCRYPTION) {
            reset();
            return output.array();
        }
        // Count HASH.
        byte[] x = new byte[16];
        setPackLength((long) aad.length * 8, x, 0);
        setPackLength(totalLength * 8, x, 8);
        xor(s, x);
        multiplyH(s);
        byte[] generatedTag = new byte[BLOCK_SIZE];
        processBlock(j0, 0, generatedTag, 0);
        xor(generatedTag, s);
        if (!encrypt) {
            if (!tagsEquals(this.tag, generatedTag)) {
                System.out.println(GXCommon.toHex(tag, false) + "-"
                        + GXCommon.toHex(generatedTag, false));
                throw new GXDLMSException("Decrypt failed. Invalid tag.");
            }
        } else {
            // Tag size is 12 bytes.
            System.arraycopy(generatedTag, 0, tag, 0, 12);
        }
        reset();
        return output.array();
    }

    /**
     * Generate AES keys.
     * 
     * @param key
     * @param forEncryption
     * @return
     */
    private int[][] generateKey(final boolean isEncrypt, final byte[] key) {
        // Key length in words.
        int keyLen = key.length / 4;
        rounds = keyLen + 6;
        // 4 words make one block.
        int[][] w = new int[rounds + 1][4];
        // Copy the key into the round key array.
        int t = 0;
        for (int i = 0; i < key.length; t++) {
            w[t >> 2][t & 3] = toUInt32(key, i);
            i += 4;
        }
        // while not enough round key material calculated calculate new values.
        int k = (rounds + 1) << 2;
        for (int i = keyLen; (i < k); i++) {
            int temp = w[(i - 1) >> 2][(i - 1) & 3];
            if ((i % keyLen) == 0) {
                temp = subWord(shift(temp, 8))
                        ^ (R_CON[(i / keyLen) - 1] & 0xFF);
            } else if ((keyLen > 6) && ((i % keyLen) == 4)) {
                temp = subWord(temp);
            }
            w[i >> 2][i & 3] = w[(i - keyLen) >> 2][(i - keyLen) & 3] ^ temp;
        }

        if (!isEncrypt) {
            for (int j = 1; j < rounds; j++) {
                for (int i = 0; i < 4; i++) {
                    w[j][i] = imixCol(w[j][i]);
                }
            }
        }
        return w;
    }

    /**
     * multiply by 2 in the Galois field
     * 
     * @param value
     *            value to multiply.
     * @return Value multiply by 2.
     */
    private static byte galoisMultiply(final int value) {
        // if MSB is 1, then this will signed extend and fill the temp variable
        // with 1's
        byte temp = (byte) (value >> 7);
        // AND with the reduction variable
        temp = (byte) (temp & 0x1b);
        // finally shift and reduce the value
        return (byte) ((value << 1) ^ temp);
    }

    /**
     * Encrypt data using AES.
     * 
     * @param data
     *            Encrypted data.
     * @param offset
     *            Data offset.
     * @param secret
     *            Secret.
     */
    static void aes1Encrypt(final byte[] data, final int offset,
            final byte[] secret) {
        byte buf1, buf2, buf3, buf4, round, i;
        final byte[] key = new byte[secret.length];
        System.arraycopy(secret, 0, key, 0, secret.length);

        for (round = 0; round < 10; ++round) {
            for (i = 0; i < 16; ++i) {
                data[i + offset] = S_BOX[(data[i + offset] ^ key[i]) & 0xFF];
            }
            // shift rows
            buf1 = data[1 + offset];
            data[1 + offset] = data[5 + offset];
            data[5 + offset] = data[9 + offset];
            data[9 + offset] = data[13 + offset];
            data[13 + offset] = buf1;

            buf1 = data[2 + offset];
            buf2 = data[6 + offset];
            data[2 + offset] = data[10 + offset];
            data[6 + offset] = data[14 + offset];
            data[10 + offset] = buf1;
            data[14 + offset] = buf2;

            buf1 = data[15 + offset];
            data[15 + offset] = data[11 + offset];
            data[11 + offset] = data[7 + offset];
            data[7 + offset] = data[3 + offset];
            data[3 + offset] = buf1;

            if (round < 9) {
                for (i = 0; i < 4; i++) {
                    buf4 = (byte) (i << 2);
                    buf1 = (byte) (data[buf4 + offset] ^ data[buf4 + 1 + offset]
                            ^ data[buf4 + 2 + offset]
                            ^ data[buf4 + 3 + offset]);
                    buf2 = data[buf4 + offset];
                    buf3 = (byte) (data[buf4 + offset]
                            ^ data[buf4 + 1 + offset]);
                    buf3 = galoisMultiply(buf3);
                    data[buf4 + offset] =
                            (byte) (data[buf4 + offset] ^ buf3 ^ buf1);
                    buf3 = (byte) (data[buf4 + 1 + offset]
                            ^ data[buf4 + 2 + offset]);
                    buf3 = galoisMultiply(buf3);
                    data[buf4 + 1 + offset] =
                            (byte) (data[buf4 + 1 + offset] ^ buf3 ^ buf1);
                    buf3 = (byte) (data[buf4 + 2 + offset]
                            ^ data[buf4 + 3 + offset]);
                    buf3 = galoisMultiply(buf3);
                    data[buf4 + 2 + offset] =
                            (byte) (data[buf4 + 2 + offset] ^ buf3 ^ buf1);
                    buf3 = (byte) (data[buf4 + 3 + offset] ^ buf2);
                    buf3 = galoisMultiply(buf3);
                    data[buf4 + 3 + offset] =
                            (byte) (data[buf4 + 3 + offset] ^ buf3 ^ buf1);
                }
            }

            key[0] = (byte) (S_BOX[key[13] & 0xFF] ^ key[0] ^ R_CON[round]);
            key[1] = (byte) (S_BOX[key[14] & 0xFF] ^ key[1]);
            key[2] = (byte) (S_BOX[key[15] & 0xFF] ^ key[2]);
            key[3] = (byte) (S_BOX[key[12] & 0xFF] ^ key[3]);

            for (i = 4; i < 16; i++) {
                key[i] = (byte) (key[i] ^ key[i - 4]);
            }
        }
        for (i = 0; i < 16; i++) {
            data[i + offset] = (byte) (data[i + offset] ^ key[i]);
        }
    }

    /**
     * Encrypt data using AES RFC3394.
     * 
     * @param data
     *            Encrypted data.
     */
    final byte[] encryptAes(final byte[] data) {
        int n = data.length / 8;

        if ((n * 8) != data.length) {
            throw new IllegalArgumentException("Invalid data.");
        }
        byte[] block = new byte[data.length + IV.length];
        byte[] buf = new byte[8 + IV.length];

        System.arraycopy(IV, 0, block, 0, IV.length);
        System.arraycopy(data, 0, block, IV.length, data.length);

        for (int j = 0; j != 6; j++) {
            for (int i = 1; i <= n; i++) {
                System.arraycopy(block, 0, buf, 0, IV.length);
                System.arraycopy(block, 8 * i, buf, IV.length, 8);
                processBlock(buf, 0, buf, 0);

                int t = n * j + i;
                for (int k = 1; t != 0; k++) {
                    byte v = (byte) t;

                    buf[IV.length - k] ^= v;
                    t = (int) ((int) t >> 8);
                }

                System.arraycopy(buf, 0, block, 0, 8);
                System.arraycopy(buf, 8, block, 8 * i, 8);
            }
        }
        return block;
    }

    /**
     * Decrypt data using AES RFC3394.
     * 
     * @param data
     *            Decrypted data.
     */
    final byte[] decryptAes(final byte[] input) {
        int n = input.length / 8;

        if ((n * 8) != input.length) {
            throw new IllegalArgumentException("Invalid data.");
        }

        byte[] block;
        if (input.length > IV.length) {
            block = new byte[input.length - IV.length];
        } else {
            block = new byte[IV.length];
        }
        byte[] a = new byte[IV.length];
        byte[] buf = new byte[8 + IV.length];

        System.arraycopy(input, 0, a, 0, IV.length);
        System.arraycopy(input, 0 + IV.length, block, 0,
                input.length - IV.length);

        n = n - 1;
        if (n == 0) {
            n = 1;
        }

        for (int j = 5; j >= 0; j--) {
            for (int i = n; i >= 1; i--) {
                System.arraycopy(a, 0, buf, 0, IV.length);
                System.arraycopy(block, 8 * (i - 1), buf, IV.length, 8);

                int t = n * j + i;
                for (int k = 1; t != 0; k++) {
                    byte v = (byte) t;

                    buf[IV.length - k] ^= v;
                    t = (int) ((int) t >> 8);
                }

                processBlock(buf, 0, buf, 0);
                System.arraycopy(buf, 0, a, 0, 8);
                System.arraycopy(buf, 8, block, 8 * (i - 1), 8);
            }
        }
        if (!GXCommon.compare(a, IV)) {
            throw new ArithmeticException("Invalid CRC");
        }
        return block;
    }

    /**
     * Decrypt data using AES1.
     * 
     * @param data
     *            Crypted data.
     * @param secret
     *            Secret.
     */
    static void aes1Decrypt(final byte[] data, final byte[] secret) {
        byte buf1, buf2, buf3, round, i;
        int buf4;
        final byte[] key = new byte[secret.length];
        System.arraycopy(secret, 0, key, 0, secret.length);

        for (round = 0; round < 10; round++) {
            key[0] = (byte) (S_BOX[key[13] & 0xFF] ^ key[0] ^ R_CON[round]);
            key[1] = (byte) (S_BOX[key[14] & 0xFF] ^ key[1]);
            key[2] = (byte) (S_BOX[key[15] & 0xFF] ^ key[2]);
            key[3] = (byte) (S_BOX[key[12] & 0xFF] ^ key[3]);
            for (i = 4; i < 16; i++) {
                key[i] = (byte) (key[i] ^ key[i - 4]);
            }
        }

        for (i = 0; i < 16; i++) {
            data[i] = (byte) (data[i] ^ key[i]);
        }

        for (round = 0; round < 10; ++round) {
            for (i = 15; i > 3; --i) {
                key[i] = (byte) (key[i] ^ key[i - 4]);
            }
            key[0] = (byte) (S_BOX[key[13] & 0xFF] ^ key[0] ^ R_CON[9 - round]);
            key[1] = (byte) (S_BOX[key[14] & 0xFF] ^ key[1]);
            key[2] = (byte) (S_BOX[key[15] & 0xFF] ^ key[2]);
            key[3] = (byte) (S_BOX[key[12] & 0xFF] ^ key[3]);

            if (round > 0) {
                for (i = 0; i < 4; i++) {
                    buf4 = (i << 2) & 0xFF;

                    buf1 = galoisMultiply(
                            galoisMultiply(data[buf4] ^ data[buf4 + 2]));
                    buf2 = galoisMultiply(
                            galoisMultiply(data[buf4 + 1] ^ data[buf4 + 3]));
                    data[buf4] ^= buf1;
                    data[buf4 + 1] ^= buf2;
                    data[buf4 + 2] ^= buf1;
                    data[buf4 + 3] ^= buf2;

                    buf1 = (byte) (data[buf4] ^ data[buf4 + 1] ^ data[buf4 + 2]
                            ^ data[buf4 + 3]);
                    buf2 = data[buf4];
                    buf3 = (byte) (data[buf4] ^ data[buf4 + 1]);
                    buf3 = galoisMultiply(buf3);
                    data[buf4] = (byte) (data[buf4] ^ buf3 ^ buf1);
                    buf3 = (byte) (data[buf4 + 1] ^ data[buf4 + 2]);
                    buf3 = galoisMultiply(buf3);
                    data[buf4 + 1] = (byte) (data[buf4 + 1] ^ buf3 ^ buf1);
                    buf3 = (byte) (data[buf4 + 2] ^ data[buf4 + 3]);
                    buf3 = galoisMultiply(buf3);
                    data[buf4 + 2] = (byte) (data[buf4 + 2] ^ buf3 ^ buf1);
                    buf3 = (byte) (data[buf4 + 3] ^ buf2);
                    buf3 = galoisMultiply(buf3);
                    data[buf4 + 3] = (byte) (data[buf4 + 3] ^ buf3 ^ buf1);
                }
            }
            // Row 1
            buf1 = data[13];
            data[13] = data[9];
            data[9] = data[5];
            data[5] = data[1];
            data[1] = buf1;
            // Row 2
            buf1 = data[10];
            buf2 = data[14];
            data[10] = data[2];
            data[14] = data[6];
            data[2] = buf1;
            data[6] = buf2;
            // Row 3
            buf1 = data[3];
            data[3] = data[7];
            data[7] = data[11];
            data[11] = data[15];
            data[15] = buf1;

            for (i = 0; i < 16; i++) {
                data[i] = (byte) (S_BOX_REVERSED[data[i] & 0xFF] ^ key[i]);
            }
        }
    }
}