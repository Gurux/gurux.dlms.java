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
package gurux.dlms;

import gurux.dlms.enums.Security;
import java.io.ByteArrayOutputStream;

/** 
    Implements GMAC.
    This class is based to this doc:
    http://csrc.nist.gov/publications/nistpubs/800-38D/SP-800-38D.pdf
*/
    class GXDLMSChipperingStream
    {
        ByteArrayOutputStream Output = new ByteArrayOutputStream();                
        //Consts.        
        private static final int BlockSize = 16;
        private Security Security;
        //Properties.
        private final int[][][] M = new int[32][][];
        private long totalLength;
        private static final byte[] Zeroes = new byte[BlockSize];
	private byte[] S;
	private byte[] counter;
	private byte[] Aad;
	private byte[] J0;
        //How many butes are not crypted/encrypted.
        private int BytesRemaining;
        private byte[] H;
	private byte[] bufBlock;
	private byte[] Tag;
	private int Rounds;
	private int[][] WorkingKey;
	private int C0, C1, C2, C3;
	private boolean Encrypt;

        /** 
         Constructor.

         @param security Used security level.
         @param encrypt
         @param blockCipherKey
         @param aad
         @param iv
         @param tag
        */
        public GXDLMSChipperingStream(Security security, boolean encrypt, byte[] blockCipherKey, byte[] aad, byte[] iv, byte[] tag)
        {
            this.Security = security;
            final int TagSize = 0x10;
            this.Tag = tag;
            if (this.Tag == null) //Tag size is 12 bytes.
            {
                this.Tag = new byte[12];
            }
            else if (this.Tag.length != 12)
            {
                throw new IllegalArgumentException("Invalid tag.");
            }
            Encrypt = encrypt;
            WorkingKey = GenerateKey(true, blockCipherKey);
            int bufLength = Encrypt ? BlockSize : (BlockSize + TagSize);
            this.bufBlock = new byte[bufLength];
            Aad = aad;
            this.H = new byte[BlockSize];
            ProcessBlock(H, 0, H, 0);
            Init(H);
            this.J0 = new byte[16];
            System.arraycopy(iv, 0, J0, 0, iv.length);
            this.J0[15] = 0x01;
            this.S = GetGHash(Aad);
            this.counter = (byte[]) J0.clone();
            this.BytesRemaining = 0;
            this.totalLength = 0;
        }

        /** 
         Get tag from crypted data.

         @return Tag from crypted data.
        */
        public byte[] GetTag()
        {
            return Tag;
        }

        /** 
         Convert byte array to Little Endian.

         @param data
         @param offset
         @return 
        */
        private static int ToUInt32(byte[] value, int offset)
        {
            int tmp = value[offset];
            tmp |= (value[++offset] << 8) & 0xFF00;
            tmp |= (value[++offset] << 16) & 0xFF0000;
            tmp |= (value[++offset] << 24) & 0xFF000000;
            return tmp;
        }

        private static int SubWord(int value)
        {            
            int tmp = SBox[value & 0xFF] & 0xFF;
            tmp |= (((SBox[(value >>> 8) & 0xFF]) & 0xFF) << 8) & 0xFF00;
            tmp |= (((SBox[(value >>> 16) & 0xFF]) & 0xFF) << 16) & 0xFF0000;
            tmp |= (((SBox[(value >>> 24) & 0xFF]) & 0xFF) << 24) & 0xFF000000;
            return tmp;
        }

        /** 
         Shift value.

         @param r
         @param shift
         @return 
        */
        private int Shift(int value, int shift)
        {
            return (value >>> shift) | (value << (32 - shift));
        }

        /** 
         Key schedule Vector (powers of x).
        */
        private static final byte[] rcon = {(byte)0x01, (byte)0x02, (byte)0x04,
            (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80, (byte)0x1b, 
            (byte)0x36, (byte)0x6c, (byte)0xd8, (byte)0xab, (byte)0x4d, (byte)0x9a, 
            (byte)0x2f, (byte)0x5e, (byte)0xbc, (byte)0x63, (byte)0xc6, (byte)0x97, 
            (byte)0x35, (byte)0x6a, (byte)0xd4, (byte)0xb3, (byte)0x7d, (byte)0xfa, 
            (byte)0xef, (byte)0xc5, (byte)0x91};


        /** 
         S box
        */
        private static final byte[] SBox = {(byte)0x63, (byte)0x7C, (byte)0x77, 
            (byte)0x7B, (byte)0xF2, (byte)0x6B, (byte)0x6F, (byte)0xC5, (byte)0x30, 
            (byte)0x01, (byte)0x67, (byte)0x2B, (byte)0xFE, (byte)0xD7, (byte)0xAB,
            (byte)0x76, (byte)0xCA, (byte)0x82, (byte)0xC9, (byte)0x7D, (byte)0xFA, 
            (byte)0x59, (byte)0x47, (byte)0xF0, (byte)0xAD, (byte)0xD4, (byte)0xA2,
            (byte)0xAF, (byte)0x9C, (byte)0xA4, (byte)0x72, (byte)0xC0, (byte)0xB7, 
            (byte)0xFD, (byte)0x93, (byte)0x26, (byte)0x36, (byte)0x3F, (byte)0xF7, 
            (byte)0xCC, (byte)0x34, (byte)0xA5, (byte)0xE5, (byte)0xF1, (byte)0x71, 
            (byte)0xD8, (byte)0x31, (byte)0x15, (byte)0x04, (byte)0xC7, (byte)0x23, 
            (byte)0xC3, (byte)0x18, (byte)0x96, (byte)0x05, (byte)0x9A, (byte)0x07, 
            (byte)0x12, (byte)0x80, (byte)0xE2, (byte)0xEB, (byte)0x27, (byte)0xB2, 
            (byte)0x75, (byte)0x09, (byte)0x83, (byte)0x2C, (byte)0x1A, (byte)0x1B, 
            (byte)0x6E, (byte)0x5A, (byte)0xA0, (byte)0x52, (byte)0x3B, (byte)0xD6, 
            (byte)0xB3, (byte)0x29, (byte)0xE3, (byte)0x2F, (byte)0x84, (byte)0x53, 
            (byte)0xD1, (byte)0x00, (byte)0xED, (byte)0x20, (byte)0xFC, (byte)0xB1, 
            (byte)0x5B, (byte)0x6A, (byte)0xCB, (byte)0xBE, (byte)0x39, (byte)0x4A, 
            (byte)0x4C, (byte)0x58, (byte)0xCF, (byte)0xD0, (byte)0xEF, (byte)0xAA, 
            (byte)0xFB, (byte)0x43, (byte)0x4D, (byte)0x33, (byte)0x85, (byte)0x45, 
            (byte)0xF9, (byte)0x02, (byte)0x7F, (byte)0x50, (byte)0x3C, (byte)0x9F, 
            (byte)0xA8, (byte)0x51, (byte)0xA3, (byte)0x40, (byte)0x8F, (byte)0x92, 
            (byte)0x9D, (byte)0x38, (byte)0xF5, (byte)0xBC, (byte)0xB6, (byte)0xDA, 
            (byte)0x21, (byte)0x10, (byte)0xFF, (byte)0xF3, (byte)0xD2, (byte)0xCD, 
            (byte)0x0C, (byte)0x13, (byte)0xEC, (byte)0x5F, (byte)0x97, (byte)0x44, 
            (byte)0x17, (byte)0xC4, (byte)0xA7, (byte)0x7E, (byte)0x3D, (byte)0x64, 
            (byte)0x5D, (byte)0x19, (byte)0x73, (byte)0x60, (byte)0x81, (byte)0x4F, 
            (byte)0xDC, (byte)0x22, (byte)0x2A, (byte)0x90, (byte)0x88, (byte)0x46, 
            (byte)0xEE, (byte)0xB8, (byte)0x14, (byte)0xDE, (byte)0x5E, (byte)0x0B, 
            (byte)0xDB, (byte)0xE0, (byte)0x32, (byte)0x3A, (byte)0x0A, (byte)0x49, 
            (byte)0x06, (byte)0x24, (byte)0x5C, (byte)0xC2, (byte)0xD3, (byte)0xAC, 
            (byte)0x62, (byte)0x91, (byte)0x95, (byte)0xE4, (byte)0x79, (byte)0xE7, 
            (byte)0xC8, (byte)0x37, (byte)0x6D, (byte)0x8D, (byte)0xD5, (byte)0x4E, 
            (byte)0xA9, (byte)0x6C, (byte)0x56, (byte)0xF4, (byte)0xEA, (byte)0x65, 
            (byte)0x7A, (byte)0xAE, (byte)0x08, (byte)0xBA, (byte)0x78, (byte)0x25, 
            (byte)0x2E, (byte)0x1C, (byte)0xA6, (byte)0xB4, (byte)0xC6, (byte)0xE8, 
            (byte)0xDD, (byte)0x74, (byte)0x1F, (byte)0x4B, (byte)0xBD, (byte)0x8B, 
            (byte)0x8A, (byte)0x70, (byte)0x3E, (byte)0xB5, (byte)0x66, (byte)0x48, 
            (byte)0x03, (byte)0xF6, (byte)0x0E, (byte)0x61, (byte)0x35, (byte)0x57, 
            (byte)0xB9, (byte)0x86, (byte)0xC1, (byte)0x1D, (byte)0x9E, (byte)0xE1, 
            (byte)0xF8, (byte)0x98, (byte)0x11, (byte)0x69, (byte)0xD9, (byte)0x8E, 
            (byte)0x94, (byte)0x9B, (byte)0x1E, (byte)0x87, (byte)0xE9, (byte)0xCE, 
            (byte)0x55, (byte)0x28, (byte)0xDF, (byte)0x8C, (byte)0xA1, (byte)0x89, 
            (byte)0x0D, (byte)0xBF, (byte)0xE6, (byte)0x42, (byte)0x68, (byte)0x41, 
            (byte)0x99, (byte)0x2D, (byte)0x0F, (byte)0xB0, (byte)0x54, (byte)0xBB, 
            (byte)0x16};

        /** 
         The inverse S-box.
        */
        private static final byte[] SBoxInverse = {(byte)0x52, (byte) 0x09, 
            (byte) 0x6A, (byte) 0xD5, (byte) 0x30, (byte) 0x36, (byte) 0xA5,
                (byte) 0x38, (byte) 0xBF, (byte) 0x40, (byte) 0xA3, (byte) 0x9E, 
                (byte) 0x81, (byte) 0xF3, (byte) 0xD7, (byte) 0xFB, (byte) 0x7C, 
                (byte) 0xE3, (byte) 0x39, (byte) 0x82, (byte) 0x9B, (byte) 0x2F, 
                (byte) 0xFF, (byte) 0x87, (byte) 0x34, (byte) 0x8E, (byte) 0x43, 
                (byte) 0x44, (byte) 0xC4, (byte) 0xDE, (byte) 0xE9, (byte) 0xCB, 
                (byte) 0x54, (byte) 0x7B, (byte) 0x94, (byte) 0x32, (byte) 0xA6,
                (byte) 0xC2, (byte) 0x23, (byte) 0x3D, (byte) 0xEE, (byte) 0x4C,
                (byte) 0x95, (byte) 0x0B, (byte) 0x42, (byte) 0xFA, (byte) 0xC3,
                (byte) 0x4E, (byte) 0x08, (byte) 0x2E, (byte) 0xA1, (byte) 0x66, 
                (byte) 0x28, (byte) 0xD9, (byte) 0x24, (byte) 0xB2, (byte) 0x76, 
                (byte) 0x5B, (byte) 0xA2, (byte) 0x49, (byte) 0x6D, (byte) 0x8B, 
                (byte) 0xD1, (byte) 0x25, (byte) 0x72, (byte) 0xF8, (byte) 0xF6, 
                (byte) 0x64, (byte) 0x86, (byte) 0x68, (byte) 0x98, (byte) 0x16, 
                (byte) 0xD4, (byte) 0xA4, (byte) 0x5C, (byte) 0xCC, (byte) 0x5D, 
                (byte) 0x65, (byte) 0xB6, (byte) 0x92, (byte) 0x6C, (byte) 0x70, 
                (byte) 0x48, (byte) 0x50, (byte) 0xFD, (byte) 0xED, (byte) 0xB9, 
                (byte) 0xDA, (byte) 0x5E, (byte) 0x15, (byte) 0x46, (byte) 0x57, 
                (byte) 0xA7, (byte) 0x8D, (byte) 0x9D, (byte) 0x84, (byte) 0x90, 
                (byte) 0xD8, (byte) 0xAB, (byte) 0x00, (byte) 0x8C, (byte) 0xBC, 
                (byte) 0xD3, (byte) 0x0A, (byte) 0xF7, (byte) 0xE4, (byte) 0x58, 
                (byte) 0x05, (byte) 0xB8, (byte) 0xB3, (byte) 0x45, (byte) 0x06, 
                (byte) 0xD0, (byte) 0x2C, (byte) 0x1E, (byte) 0x8F, (byte) 0xCA,
                (byte) 0x3F, (byte) 0x0F, (byte) 0x02, (byte) 0xC1, (byte) 0xAF,
                (byte) 0xBD, (byte) 0x03, (byte) 0x01, (byte) 0x13, (byte) 0x8A, 
                (byte) 0x6B, (byte) 0x3A, (byte) 0x91, (byte) 0x11, (byte) 0x41, 
                (byte) 0x4F, (byte) 0x67, (byte) 0xDC, (byte) 0xEA, (byte) 0x97,
                (byte) 0xF2, (byte) 0xCF, (byte) 0xCE, (byte) 0xF0, (byte) 0xB4, 
                (byte) 0xE6, (byte) 0x73, (byte) 0x96, (byte) 0xAC, (byte) 0x74, 
                (byte) 0x22, (byte) 0xE7, (byte) 0xAD, (byte) 0x35, (byte) 0x85, 
                (byte) 0xE2, (byte) 0xF9, (byte) 0x37, (byte) 0xE8, (byte) 0x1C,
                (byte) 0x75, (byte) 0xDF, (byte) 0x6E, (byte) 0x47, (byte) 0xF1,
                (byte) 0x1A, (byte) 0x71, (byte) 0x1D, (byte) 0x29, (byte) 0xC5, 
                (byte) 0x89, (byte) 0x6F, (byte) 0xB7, (byte) 0x62, (byte) 0x0E, 
                (byte) 0xAA, (byte) 0x18, (byte) 0xBE, (byte) 0x1B, (byte) 0xFC, 
                (byte) 0x56, (byte) 0x3E, (byte) 0x4B, (byte) 0xC6, (byte) 0xD2, 
                (byte) 0x79, (byte) 0x20, (byte) 0x9A, (byte) 0xDB, (byte) 0xC0, 
                (byte) 0xFE, (byte) 0x78, (byte) 0xCD, (byte) 0x5A, (byte) 0xF4, 
                (byte) 0x1F, (byte) 0xDD, (byte) 0xA8, (byte) 0x33, (byte) 0x88, 
                (byte) 0x07, (byte) 0xC7, (byte) 0x31, (byte) 0xB1, (byte) 0x12, 
                (byte) 0x10, (byte) 0x59, (byte) 0x27, (byte) 0x80, (byte) 0xEC, 
                (byte) 0x5F, (byte) 0x60, (byte) 0x51, (byte) 0x7F, (byte) 0xA9, 
                (byte) 0x19, (byte) 0xB5, (byte) 0x4A, (byte) 0x0D, (byte) 0x2D, 
                (byte) 0xE5, (byte) 0x7A, (byte) 0x9F, (byte) 0x93, (byte) 0xC9, 
                (byte) 0x9C, (byte) 0xEF, (byte) 0xA0, (byte) 0xE0, (byte) 0x3B, 
                (byte) 0x4D, (byte) 0xAE, (byte) 0x2A, (byte) 0xF5, (byte) 0xB0, 
                (byte) 0xC8, (byte) 0xEB, (byte) 0xBB, (byte) 0x3C, (byte) 0x83, 
                (byte) 0x53, (byte) 0x99, (byte) 0x61, (byte) 0x17, (byte) 0x2B, 
                (byte) 0x04, (byte) 0x7E, (byte) 0xBA, (byte) 0x77, (byte) 0xD6, 
                (byte) 0x26, (byte) 0xE1, (byte) 0x69, (byte) 0x14, (byte) 0x63, 
                (byte) 0x55, (byte) 0x21, (byte) 0x0C, (byte) 0x7D};

    /** 
     Rijndael (AES) Encryption fast table.
    */
    private static final int[] AES1 =
    {
        0xa56363c6, 0x847c7cf8, 0x997777ee, 0x8d7b7bf6, 0x0df2f2ff,
        0xbd6b6bd6, 0xb16f6fde, 0x54c5c591, 0x50303060, 0x03010102,
        0xa96767ce, 0x7d2b2b56, 0x19fefee7, 0x62d7d7b5, 0xe6abab4d,
        0x9a7676ec, 0x45caca8f, 0x9d82821f, 0x40c9c989, 0x877d7dfa,
        0x15fafaef, 0xeb5959b2, 0xc947478e, 0x0bf0f0fb, 0xecadad41,
        0x67d4d4b3, 0xfda2a25f, 0xeaafaf45, 0xbf9c9c23, 0xf7a4a453,
        0x967272e4, 0x5bc0c09b, 0xc2b7b775, 0x1cfdfde1, 0xae93933d,
        0x6a26264c, 0x5a36366c, 0x413f3f7e, 0x02f7f7f5, 0x4fcccc83,
        0x5c343468, 0xf4a5a551, 0x34e5e5d1, 0x08f1f1f9, 0x937171e2,
        0x73d8d8ab, 0x53313162, 0x3f15152a, 0x0c040408, 0x52c7c795,
        0x65232346, 0x5ec3c39d, 0x28181830, 0xa1969637, 0x0f05050a,
        0xb59a9a2f, 0x0907070e, 0x36121224, 0x9b80801b, 0x3de2e2df,
        0x26ebebcd, 0x6927274e, 0xcdb2b27f, 0x9f7575ea, 0x1b090912,
        0x9e83831d, 0x742c2c58, 0x2e1a1a34, 0x2d1b1b36, 0xb26e6edc,
        0xee5a5ab4, 0xfba0a05b, 0xf65252a4, 0x4d3b3b76, 0x61d6d6b7,
        0xceb3b37d, 0x7b292952, 0x3ee3e3dd, 0x712f2f5e, 0x97848413,
        0xf55353a6, 0x68d1d1b9, 0x00000000, 0x2cededc1, 0x60202040,
        0x1ffcfce3, 0xc8b1b179, 0xed5b5bb6, 0xbe6a6ad4, 0x46cbcb8d,
        0xd9bebe67, 0x4b393972, 0xde4a4a94, 0xd44c4c98, 0xe85858b0,
        0x4acfcf85, 0x6bd0d0bb, 0x2aefefc5, 0xe5aaaa4f, 0x16fbfbed,
        0xc5434386, 0xd74d4d9a, 0x55333366, 0x94858511, 0xcf45458a,
        0x10f9f9e9, 0x06020204, 0x817f7ffe, 0xf05050a0, 0x443c3c78,
        0xba9f9f25, 0xe3a8a84b, 0xf35151a2, 0xfea3a35d, 0xc0404080,
        0x8a8f8f05, 0xad92923f, 0xbc9d9d21, 0x48383870, 0x04f5f5f1,
        0xdfbcbc63, 0xc1b6b677, 0x75dadaaf, 0x63212142, 0x30101020,
        0x1affffe5, 0x0ef3f3fd, 0x6dd2d2bf, 0x4ccdcd81, 0x140c0c18,
        0x35131326, 0x2fececc3, 0xe15f5fbe, 0xa2979735, 0xcc444488,
        0x3917172e, 0x57c4c493, 0xf2a7a755, 0x827e7efc, 0x473d3d7a,
        0xac6464c8, 0xe75d5dba, 0x2b191932, 0x957373e6, 0xa06060c0,
        0x98818119, 0xd14f4f9e, 0x7fdcdca3, 0x66222244, 0x7e2a2a54,
        0xab90903b, 0x8388880b, 0xca46468c, 0x29eeeec7, 0xd3b8b86b,
        0x3c141428, 0x79dedea7, 0xe25e5ebc, 0x1d0b0b16, 0x76dbdbad,
        0x3be0e0db, 0x56323264, 0x4e3a3a74, 0x1e0a0a14, 0xdb494992,
        0x0a06060c, 0x6c242448, 0xe45c5cb8, 0x5dc2c29f, 0x6ed3d3bd,
        0xefacac43, 0xa66262c4, 0xa8919139, 0xa4959531, 0x37e4e4d3,
        0x8b7979f2, 0x32e7e7d5, 0x43c8c88b, 0x5937376e, 0xb76d6dda,
        0x8c8d8d01, 0x64d5d5b1, 0xd24e4e9c, 0xe0a9a949, 0xb46c6cd8,
        0xfa5656ac, 0x07f4f4f3, 0x25eaeacf, 0xaf6565ca, 0x8e7a7af4,
        0xe9aeae47, 0x18080810, 0xd5baba6f, 0x887878f0, 0x6f25254a,
        0x722e2e5c, 0x241c1c38, 0xf1a6a657, 0xc7b4b473, 0x51c6c697,
        0x23e8e8cb, 0x7cdddda1, 0x9c7474e8, 0x211f1f3e, 0xdd4b4b96,
        0xdcbdbd61, 0x868b8b0d, 0x858a8a0f, 0x907070e0, 0x423e3e7c,
        0xc4b5b571, 0xaa6666cc, 0xd8484890, 0x05030306, 0x01f6f6f7,
        0x120e0e1c, 0xa36161c2, 0x5f35356a, 0xf95757ae, 0xd0b9b969,
        0x91868617, 0x58c1c199, 0x271d1d3a, 0xb99e9e27, 0x38e1e1d9,
        0x13f8f8eb, 0xb398982b, 0x33111122, 0xbb6969d2, 0x70d9d9a9,
        0x898e8e07, 0xa7949433, 0xb69b9b2d, 0x221e1e3c, 0x92878715,
        0x20e9e9c9, 0x49cece87, 0xff5555aa, 0x78282850, 0x7adfdfa5,
        0x8f8c8c03, 0xf8a1a159, 0x80898909, 0x170d0d1a, 0xdabfbf65,
        0x31e6e6d7, 0xc6424284, 0xb86868d0, 0xc3414182, 0xb0999929,
        0x772d2d5a, 0x110f0f1e, 0xcbb0b07b, 0xfc5454a8, 0xd6bbbb6d,
        0x3a16162c
    };
    /** 
     Rijndael (AES) Encryption fast table 2.
    */
    private static final int[] AES2 = {0x6363c6a5, 0x7c7cf884, 
        0x7777ee99, 0x7b7bf68d, 0xf2f2ff0d, 0x6b6bd6bd, 0x6f6fdeb1, 
        0xc5c59154, 0x30306050, 0x01010203, 0x6767cea9, 0x2b2b567d,
        0xfefee719, 0xd7d7b562, 0xabab4de6, 0x7676ec9a, 0xcaca8f45,
        0x82821f9d, 0xc9c98940, 0x7d7dfa87, 0xfafaef15, 0x5959b2eb, 
        0x47478ec9, 0xf0f0fb0b, 0xadad41ec, 0xd4d4b367, 0xa2a25ffd, 
        0xafaf45ea, 0x9c9c23bf, 0xa4a453f7, 0x7272e496, 0xc0c09b5b, 
        0xb7b775c2, 0xfdfde11c, 0x93933dae, 0x26264c6a, 0x36366c5a,
        0x3f3f7e41, 0xf7f7f502, 0xcccc834f, 0x3434685c, 0xa5a551f4, 
        0xe5e5d134, 0xf1f1f908, 0x7171e293, 0xd8d8ab73, 0x31316253,
        0x15152a3f, 0x0404080c, 0xc7c79552, 0x23234665, 0xc3c39d5e,
        0x18183028, 0x969637a1, 0x05050a0f, 0x9a9a2fb5, 0x07070e09,
        0x12122436, 0x80801b9b, 0xe2e2df3d, 0xebebcd26, 0x27274e69,
        0xb2b27fcd, 0x7575ea9f, 0x0909121b, 0x83831d9e, 0x2c2c5874,
        0x1a1a342e, 0x1b1b362d, 0x6e6edcb2, 0x5a5ab4ee, 0xa0a05bfb, 
        0x5252a4f6, 0x3b3b764d, 0xd6d6b761, 0xb3b37dce, 0x2929527b,
        0xe3e3dd3e, 0x2f2f5e71, 0x84841397, 0x5353a6f5, 0xd1d1b968,
        0x00000000, 0xededc12c, 0x20204060, 0xfcfce31f, 0xb1b179c8,
        0x5b5bb6ed, 0x6a6ad4be, 0xcbcb8d46, 0xbebe67d9, 0x3939724b,
        0x4a4a94de, 0x4c4c98d4, 0x5858b0e8, 0xcfcf854a, 0xd0d0bb6b,
        0xefefc52a, 0xaaaa4fe5, 0xfbfbed16, 0x434386c5, 0x4d4d9ad7,
        0x33336655, 0x85851194, 0x45458acf, 0xf9f9e910, 0x02020406,
        0x7f7ffe81, 0x5050a0f0, 0x3c3c7844, 0x9f9f25ba, 0xa8a84be3, 
        0x5151a2f3, 0xa3a35dfe, 0x404080c0, 0x8f8f058a, 0x92923fad,
        0x9d9d21bc, 0x38387048, 0xf5f5f104, 0xbcbc63df, 0xb6b677c1, 
        0xdadaaf75, 0x21214263, 0x10102030, 0xffffe51a, 0xf3f3fd0e, 
        0xd2d2bf6d, 0xcdcd814c, 0x0c0c1814, 0x13132635, 0xececc32f,
        0x5f5fbee1, 0x979735a2, 0x444488cc, 0x17172e39, 0xc4c49357,
        0xa7a755f2, 0x7e7efc82, 0x3d3d7a47, 0x6464c8ac, 0x5d5dbae7, 
        0x1919322b, 0x7373e695, 0x6060c0a0, 0x81811998, 0x4f4f9ed1, 
        0xdcdca37f, 0x22224466, 0x2a2a547e, 0x90903bab, 0x88880b83,
        0x46468cca, 0xeeeec729, 0xb8b86bd3, 0x1414283c, 0xdedea779,
        0x5e5ebce2, 0x0b0b161d, 0xdbdbad76, 0xe0e0db3b, 0x32326456, 
        0x3a3a744e, 0x0a0a141e, 0x494992db, 0x06060c0a, 0x2424486c,
        0x5c5cb8e4, 0xc2c29f5d, 0xd3d3bd6e, 0xacac43ef, 0x6262c4a6, 
        0x919139a8, 0x959531a4, 0xe4e4d337, 0x7979f28b, 0xe7e7d532,
        0xc8c88b43, 0x37376e59, 0x6d6ddab7, 0x8d8d018c, 0xd5d5b164,
        0x4e4e9cd2, 0xa9a949e0, 0x6c6cd8b4, 0x5656acfa, 0xf4f4f307,
        0xeaeacf25, 0x6565caaf, 0x7a7af48e, 0xaeae47e9, 0x08081018,
        0xbaba6fd5, 0x7878f088, 0x25254a6f, 0x2e2e5c72, 0x1c1c3824, 
        0xa6a657f1, 0xb4b473c7, 0xc6c69751, 0xe8e8cb23, 0xdddda17c,
        0x7474e89c, 0x1f1f3e21, 0x4b4b96dd, 0xbdbd61dc, 0x8b8b0d86,
        0x8a8a0f85, 0x7070e090, 0x3e3e7c42, 0xb5b571c4, 0x6666ccaa, 
        0x484890d8, 0x03030605, 0xf6f6f701, 0x0e0e1c12, 0x6161c2a3,
        0x35356a5f, 0x5757aef9, 0xb9b969d0, 0x86861791, 0xc1c19958,
        0x1d1d3a27, 0x9e9e27b9, 0xe1e1d938, 0xf8f8eb13, 0x98982bb3, 
        0x11112233, 0x6969d2bb, 0xd9d9a970, 0x8e8e0789, 0x949433a7,
        0x9b9b2db6, 0x1e1e3c22, 0x87871592, 0xe9e9c920, 0xcece8749, 
        0x5555aaff, 0x28285078, 0xdfdfa57a, 0x8c8c038f, 0xa1a159f8, 
        0x89890980, 0x0d0d1a17, 0xbfbf65da, 0xe6e6d731, 0x424284c6,
        0x6868d0b8, 0x414182c3, 0x999929b0, 0x2d2d5a77, 0x0f0f1e11,
        0xb0b07bcb, 0x5454a8fc, 0xbbbb6dd6, 0x16162c3a};

    /** 
     Rijndael (AES) Encryption fast table 3.
    */
    private static final long[] AES3 = {0x63c6a563, 0x7cf8847c, 0x77ee9977, 
        0x7bf68d7b, 0xf2ff0df2, 0x6bd6bd6b, 0x6fdeb16f, 0xc59154c5, 0x30605030, 
        0x01020301, 0x67cea967, 0x2b567d2b, 0xfee719fe, 0xd7b562d7, 0xab4de6ab,
        0x76ec9a76, 0xca8f45ca, 0x821f9d82, 0xc98940c9, 0x7dfa877d, 0xfaef15fa, 
        0x59b2eb59, 0x478ec947, 0xf0fb0bf0, 0xad41ecad, 0xd4b367d4, 0xa25ffda2, 
        0xaf45eaaf, 0x9c23bf9c, 0xa453f7a4, 0x72e49672, 0xc09b5bc0, 0xb775c2b7, 
        0xfde11cfd, 0x933dae93, 0x264c6a26, 0x366c5a36, 0x3f7e413f, 0xf7f502f7,
        0xcc834fcc, 0x34685c34, 0xa551f4a5, 0xe5d134e5, 0xf1f908f1, 0x71e29371,
        0xd8ab73d8, 0x31625331, 0x152a3f15, 0x04080c04, 0xc79552c7, 0x23466523,
        0xc39d5ec3, 0x18302818, 0x9637a196, 0x050a0f05, 0x9a2fb59a, 0x070e0907, 
        0x12243612, 0x801b9b80, 0xe2df3de2, 0xebcd26eb, 0x274e6927, 0xb27fcdb2, 
        0x75ea9f75, 0x09121b09, 0x831d9e83, 0x2c58742c, 0x1a342e1a, 0x1b362d1b,
        0x6edcb26e, 0x5ab4ee5a, 0xa05bfba0, 0x52a4f652, 0x3b764d3b, 0xd6b761d6,
        0xb37dceb3, 0x29527b29, 0xe3dd3ee3, 0x2f5e712f, 0x84139784, 0x53a6f553, 
        0xd1b968d1, 0x00000000, 0xedc12ced, 0x20406020, 0xfce31ffc, 0xb179c8b1,
        0x5bb6ed5b, 0x6ad4be6a, 0xcb8d46cb, 0xbe67d9be, 0x39724b39, 0x4a94de4a,
        0x4c98d44c, 0x58b0e858, 0xcf854acf, 0xd0bb6bd0, 0xefc52aef, 0xaa4fe5aa,
        0xfbed16fb, 0x4386c543, 0x4d9ad74d, 0x33665533, 0x85119485, 0x458acf45, 
        0xf9e910f9, 0x02040602, 0x7ffe817f, 0x50a0f050, 0x3c78443c, 0x9f25ba9f, 
        0xa84be3a8, 0x51a2f351, 0xa35dfea3, 0x4080c040, 0x8f058a8f, 0x923fad92,
        0x9d21bc9d, 0x38704838, 0xf5f104f5, 0xbc63dfbc, 0xb677c1b6, 0xdaaf75da,
        0x21426321, 0x10203010, 0xffe51aff, 0xf3fd0ef3, 0xd2bf6dd2, 0xcd814ccd,
        0x0c18140c, 0x13263513, 0xecc32fec, 0x5fbee15f, 0x9735a297, 0x4488cc44,
        0x172e3917, 0xc49357c4, 0xa755f2a7, 0x7efc827e, 0x3d7a473d, 0x64c8ac64,
        0x5dbae75d, 0x19322b19, 0x73e69573, 0x60c0a060, 0x81199881, 0x4f9ed14f,
        0xdca37fdc, 0x22446622, 0x2a547e2a, 0x903bab90, 0x880b8388, 0x468cca46, 
        0xeec729ee, 0xb86bd3b8, 0x14283c14, 0xdea779de, 0x5ebce25e, 0x0b161d0b,
        0xdbad76db, 0xe0db3be0, 0x32645632, 0x3a744e3a, 0x0a141e0a, 0x4992db49,
        0x060c0a06, 0x24486c24, 0x5cb8e45c, 0xc29f5dc2, 0xd3bd6ed3, 0xac43efac,
        0x62c4a662, 0x9139a891, 0x9531a495, 0xe4d337e4, 0x79f28b79, 0xe7d532e7,
        0xc88b43c8, 0x376e5937, 0x6ddab76d, 0x8d018c8d, 0xd5b164d5, 0x4e9cd24e,
        0xa949e0a9, 0x6cd8b46c, 0x56acfa56, 0xf4f307f4, 0xeacf25ea, 0x65caaf65,
        0x7af48e7a, 0xae47e9ae, 0x08101808, 0xba6fd5ba, 0x78f08878, 0x254a6f25, 
        0x2e5c722e, 0x1c38241c, 0xa657f1a6, 0xb473c7b4, 0xc69751c6, 0xe8cb23e8, 
        0xdda17cdd, 0x74e89c74, 0x1f3e211f, 0x4b96dd4b, 0xbd61dcbd, 0x8b0d868b,
        0x8a0f858a, 0x70e09070, 0x3e7c423e, 0xb571c4b5, 0x66ccaa66, 0x4890d848,
        0x03060503, 0xf6f701f6, 0x0e1c120e, 0x61c2a361, 0x356a5f35, 0x57aef957,
        0xb969d0b9, 0x86179186, 0xc19958c1, 0x1d3a271d, 0x9e27b99e, 0xe1d938e1,
        0xf8eb13f8, 0x982bb398, 0x11223311, 0x69d2bb69, 0xd9a970d9, 0x8e07898e, 
        0x9433a794, 0x9b2db69b, 0x1e3c221e, 0x87159287, 0xe9c920e9, 0xce8749ce, 
        0x55aaff55, 0x28507828, 0xdfa57adf, 0x8c038f8c, 0xa159f8a1, 0x89098089,
        0x0d1a170d, 0xbf65dabf, 0xe6d731e6, 0x4284c642, 0x68d0b868, 0x4182c341, 
        0x9929b099, 0x2d5a772d, 0x0f1e110f, 0xb07bcbb0, 0x54a8fc54, 0xbb6dd6bb,
        0x162c3a16};

    /** 
     Rijndael (AES) Encryption fast table 4.
    */
    private static final long[] AES4 = {0xc6a56363, 0xf8847c7c, 0xee997777, 
        0xf68d7b7b, 0xff0df2f2, 0xd6bd6b6b, 0xdeb16f6f, 0x9154c5c5, 0x60503030,
        0x02030101, 0xcea96767, 0x567d2b2b, 0xe719fefe, 0xb562d7d7, 0x4de6abab,
        0xec9a7676, 0x8f45caca, 0x1f9d8282, 0x8940c9c9, 0xfa877d7d, 0xef15fafa,
        0xb2eb5959, 0x8ec94747, 0xfb0bf0f0, 0x41ecadad, 0xb367d4d4, 0x5ffda2a2, 
        0x45eaafaf, 0x23bf9c9c, 0x53f7a4a4, 0xe4967272, 0x9b5bc0c0, 0x75c2b7b7,
        0xe11cfdfd, 0x3dae9393, 0x4c6a2626, 0x6c5a3636, 0x7e413f3f, 0xf502f7f7,
        0x834fcccc, 0x685c3434, 0x51f4a5a5, 0xd134e5e5, 0xf908f1f1, 0xe2937171,
        0xab73d8d8, 0x62533131, 0x2a3f1515, 0x080c0404, 0x9552c7c7, 0x46652323,
        0x9d5ec3c3, 0x30281818, 0x37a19696, 0x0a0f0505, 0x2fb59a9a, 0x0e090707,
        0x24361212, 0x1b9b8080, 0xdf3de2e2, 0xcd26ebeb, 0x4e692727, 0x7fcdb2b2, 
        0xea9f7575, 0x121b0909, 0x1d9e8383, 0x58742c2c, 0x342e1a1a, 0x362d1b1b,
        0xdcb26e6e, 0xb4ee5a5a, 0x5bfba0a0, 0xa4f65252, 0x764d3b3b, 0xb761d6d6,
        0x7dceb3b3, 0x527b2929, 0xdd3ee3e3, 0x5e712f2f, 0x13978484, 0xa6f55353,
        0xb968d1d1, 0x00000000, 0xc12ceded, 0x40602020, 0xe31ffcfc, 0x79c8b1b1,
        0xb6ed5b5b, 0xd4be6a6a, 0x8d46cbcb, 0x67d9bebe, 0x724b3939, 0x94de4a4a, 
        0x98d44c4c, 0xb0e85858, 0x854acfcf, 0xbb6bd0d0, 0xc52aefef, 0x4fe5aaaa,
        0xed16fbfb, 0x86c54343, 0x9ad74d4d, 0x66553333, 0x11948585, 0x8acf4545,
        0xe910f9f9, 0x04060202, 0xfe817f7f, 0xa0f05050, 0x78443c3c, 0x25ba9f9f,
        0x4be3a8a8, 0xa2f35151, 0x5dfea3a3, 0x80c04040, 0x058a8f8f, 0x3fad9292,
        0x21bc9d9d, 0x70483838, 0xf104f5f5, 0x63dfbcbc, 0x77c1b6b6, 0xaf75dada,
        0x42632121, 0x20301010, 0xe51affff, 0xfd0ef3f3, 0xbf6dd2d2, 0x814ccdcd,
        0x18140c0c, 0x26351313, 0xc32fecec, 0xbee15f5f, 0x35a29797, 0x88cc4444, 
        0x2e391717, 0x9357c4c4, 0x55f2a7a7, 0xfc827e7e, 0x7a473d3d, 0xc8ac6464,
        0xbae75d5d, 0x322b1919, 0xe6957373, 0xc0a06060, 0x19988181, 0x9ed14f4f,
        0xa37fdcdc, 0x44662222, 0x547e2a2a, 0x3bab9090, 0x0b838888, 0x8cca4646,
        0xc729eeee, 0x6bd3b8b8, 0x283c1414, 0xa779dede, 0xbce25e5e, 0x161d0b0b,
        0xad76dbdb, 0xdb3be0e0, 0x64563232, 0x744e3a3a, 0x141e0a0a, 0x92db4949, 
        0x0c0a0606, 0x486c2424, 0xb8e45c5c, 0x9f5dc2c2, 0xbd6ed3d3, 0x43efacac, 
        0xc4a66262, 0x39a89191, 0x31a49595, 0xd337e4e4, 0xf28b7979, 0xd532e7e7, 
        0x8b43c8c8, 0x6e593737, 0xdab76d6d, 0x018c8d8d, 0xb164d5d5, 0x9cd24e4e, 
        0x49e0a9a9, 0xd8b46c6c, 0xacfa5656, 0xf307f4f4, 0xcf25eaea, 0xcaaf6565, 
        0xf48e7a7a, 0x47e9aeae, 0x10180808, 0x6fd5baba, 0xf0887878, 0x4a6f2525,
        0x5c722e2e, 0x38241c1c, 0x57f1a6a6, 0x73c7b4b4, 0x9751c6c6, 0xcb23e8e8, 
        0xa17cdddd, 0xe89c7474, 0x3e211f1f, 0x96dd4b4b, 0x61dcbdbd, 0x0d868b8b, 
        0x0f858a8a, 0xe0907070, 0x7c423e3e, 0x71c4b5b5, 0xccaa6666, 0x90d84848, 
        0x06050303, 0xf701f6f6, 0x1c120e0e, 0xc2a36161, 0x6a5f3535, 0xaef95757, 
        0x69d0b9b9, 0x17918686, 0x9958c1c1, 0x3a271d1d, 0x27b99e9e, 0xd938e1e1, 
        0xeb13f8f8, 0x2bb39898, 0x22331111, 0xd2bb6969, 0xa970d9d9, 0x07898e8e,
        0x33a79494, 0x2db69b9b, 0x3c221e1e, 0x15928787, 0xc920e9e9, 0x8749cece, 
        0xaaff5555, 0x50782828, 0xa57adfdf, 0x038f8c8c, 0x59f8a1a1, 0x09808989, 
        0x1a170d0d, 0x65dabfbf, 0xd731e6e6, 0x84c64242, 0xd0b86868, 0x82c34141, 
        0x29b09999, 0x5a772d2d, 0x1e110f0f, 0x7bcbb0b0, 0xa8fc5454, 0x6dd6bbbb, 
        0x2c3a1616};

    /** 
     Initialise the key schedule from the user supplied key.

     @return 
    */
    private int StarX(int value)
    {
        final int m1 = (int)0x80808080;
        final int m2 = 0x7f7f7f7f;
        final int m3 = 0x0000001b;
        return ((value & m2) << 1) ^ (((value & m1) >>> 7) * m3);
    }

    private int ImixCol(int x)
    {
        int f2 = StarX(x);
        int f4 = StarX(f2);
        int f8 = StarX(f4);
        int f9 = x ^ f8;
        return f2 ^ f4 ^ f8 ^ Shift(f2 ^ f9, 8) ^ Shift(f4 ^ f9, 16) ^ Shift(f9, 24);
    }

    /** 
     Get bytes from UIn32.

     @param value
     @param data
     @param offset
    */
    public static void GetUInt32(long value, byte[] data, int offset)
    {
        data[offset] = (byte)(value);
        data[++offset] = (byte)(value >>> 8);
        data[++offset] = (byte)(value >>> 16);
        data[++offset] = (byte)(value >>> 24);
    }

    private void UnPackBlock(byte[] bytes, int offset)
    {
        C0 = ToUInt32(bytes, offset);
        C1 = ToUInt32(bytes, offset + 4);
        C2 = ToUInt32(bytes, offset + 8);
        C3 = ToUInt32(bytes, offset + 12);
    }

    private void PackBlock(byte[] bytes, int offset)
    {
        GetUInt32(C0, bytes, offset);
        GetUInt32(C1, bytes, offset + 4);
        GetUInt32(C2, bytes, offset + 8);
        GetUInt32(C3, bytes, offset + 12);
    }

    /** 
     Encrypt data block.

     @param KW
    */
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: void EncryptBlock(uint[,] key)
    private void EncryptBlock(int[][] key)
    {
        int r;
        int r0, r1, r2, r3;
        C0 ^= key[0][0];
        C1 ^= key[0][1];
        C2 ^= key[0][2];
        C3 ^= key[0][3];
        for (r = 1; r < Rounds - 1;)
        {
            r0 = (AES1[C0 & 0xFF] & 0xFFFFFFFF);
            r0 ^= (AES2[(C1 >> 8) & 0xFF] & 0xFFFFFFFF);
            r0 ^= (AES3[(C2 >> 16) & 0xFF]  & 0xFFFFFFFF);
            r0 ^= (AES4[(C3 >> 24) & 0xFF] & 0xFFFFFFFF);
            r0 ^= (key[r][0] & 0xFFFFFFFF);
            
            r1 = (AES1[C1 & 0xFF] & 0xFFFFFFFF);
            r1 ^= AES2[(C2 >> 8) & 0xFF] & 0xFFFFFFFF;
            r1 ^= AES3[(C3 >> 16) & 0xFF] & 0xFFFFFFFF;
            r1 ^= AES4[(C0 >> 24) & 0xFF] & 0xFFFFFFFF;
            r1 ^= key[r][1] & 0xFFFFFFFF;
            
            r2 = AES1[C2 & 0xFF] & 0xFFFFFFFF;
            r2 ^= AES2[(C3 >> 8) & 0xFF] & 0xFFFFFFFF;
            r2 ^= AES3[(C0 >> 16) & 0xFF] & 0xFFFFFFFF;
            r2 ^= AES4[(C1 >> 24) & 0xFF] & 0xFFFFFFFF;
            r2 ^= key[r][2] & 0xFFFFFFFF;
            
            r3 = AES1[C3 & 0xFF] & 0xFFFFFFFF;
            r3 ^= AES2[(C0 >> 8) & 0xFF]& 0xFFFFFFFF;
            r3 ^= AES3[(C1 >> 16) & 0xFF]& 0xFFFFFFFF;
            r3 ^= AES4[(C2 >> 24) & 0xFF] & 0xFFFFFFFF;
            r3 ^= key[r++][3] & 0xFFFFFFFF;
            
            C0 = AES1[r0 & 0xFF] & 0xFFFFFFFF;
            C0 ^= AES2[(r1 >> 8) & 0xFF] & 0xFFFFFFFF;
            C0 ^= AES3[(r2 >> 16) & 0xFF] & 0xFFFFFFFF;
            C0 ^= AES4[(r3 >> 24) & 0xFF] & 0xFFFFFFFF;
            C0 ^= key[r][0] & 0xFFFFFFFF;
            
            C1 = AES1[r1 & 0xFF] & 0xFFFFFFFF;
            C1 ^= AES2[(r2 >>> 8) & 0xFF]& 0xFFFFFFFF;
            C1 ^= AES3[(r3 >>> 16) & 0xFF]& 0xFFFFFFFF;
            C1 ^= AES4[(r0 >>> 24) & 0xFF] & 0xFFFFFFFF;
            C1 ^= key[r][1] & 0xFFFFFFFF;
            
            C2 = AES1[r2 & 0xFF] & 0xFFFFFFFF;
            C2 ^= AES2[(r3 >>> 8) & 0xFF] & 0xFFFFFFFF;
            C2 ^= AES3[(r0 >>> 16) & 0xFF] & 0xFFFFFFFF;
            C2 ^= AES4[(r1 >>> 24) & 0xFF] & 0xFFFFFFFF;
            C2 ^= key[r][2] & 0xFFFFFFFF;
            
            C3 = AES1[r3 & 0xFF] & 0xFFFFFFFF;
            C3 ^= AES2[(r0 >>> 8) & 0xFF] & 0xFFFFFFFF;
            C3 ^= AES3[(r1 >>> 16) & 0xFF] & 0xFFFFFFFF;
            C3 ^= AES4[(r2 >>> 24) & 0xFF] & 0xFFFFFFFF;
            C3 ^= key[r++][3] & 0xFFFFFFFF;
        }
        r0 = AES1[C0 & 0xFF] & 0xFFFFFFFF;
        r0 ^= AES2[(C1 >>> 8) & 0xFF] & 0xFFFFFFFF;
        r0 ^= AES3[(C2 >>> 16) & 0xFF] & 0xFFFFFFFF;
        r0 ^= AES4[C3 >>> 24] & 0xFFFFFFFF;
        r0 ^= key[r][0] & 0xFFFFFFFF;
        r1 = AES1[C1 & 0xFF] & 0xFFFFFFFF;
        r1 ^= AES2[(C2 >>> 8) & 0xFF] & 0xFFFFFFFF;
        r1 ^= AES3[(C3 >>> 16) & 0xFF] & 0xFFFFFFFF;
        r1 ^= AES4[C0 >>> 24] & 0xFFFFFFFF;
        r1 ^= key[r][1] & 0xFFFFFFFF;
        r2 = AES1[C2 & 0xFF] & 0xFFFFFFFF;
        r2 ^= AES2[(C3 >>> 8) & 0xFF] & 0xFFFFFFFF;
        r2 ^= AES3[(C0 >>> 16) & 0xFF] & 0xFFFFFFFF;
        r2 ^= AES4[C1 >>> 24] & 0xFFFFFFFF;
        r2 ^= key[r][2] & 0xFFFFFFFF;
        r3 = AES1[C3 & 0xFF] & 0xFFFFFFFF;
        r3 ^= AES2[(C0 >>> 8) & 0xFF] & 0xFFFFFFFF;
        r3 ^= AES3[(C1 >>> 16) & 0xFF] & 0xFFFFFFFF;
        r3 ^= AES4[C2 >>> 24] & 0xFFFFFFFF;
        r3 ^= key[r++][3] & 0xFFFFFFFF;
        C0 = (SBox[r0 & 0xFF] & 0xFF) & 0xFFFFFFFF;
        C0 ^= ((SBox[(r1 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        C0 ^= ((SBox[(r2 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        C0 ^= ((SBox[r3 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        C0 ^= key[r][0] & 0xFFFFFFFF;
        C1 = (SBox[r1 & 0xFF]  & 0xFF) & 0xFFFFFFFF;
        C1 ^= ((SBox[(r2 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        C1 ^= ((SBox[(r3 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        C1 ^= ((SBox[r0 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        C1 ^= key[r][1] & 0xFFFFFFFF;
        C2 = (SBox[r2 & 0xFF] & 0xFF) & 0xFFFFFFFF;
        C2 ^= ((SBox[(r3 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        C2 ^= ((SBox[(r0 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        C2 ^= ((SBox[r1 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        C2 ^= key[r][2] & 0xFFFFFFFF;
        C3 = (SBox[r3 & 0xFF] & 0xFF) & 0xFFFFFFFF;
        C3 ^= ((SBox[(r0 >>> 8) & 0xFF] & 0xFF) << 8) & 0xFFFFFFFF;
        C3 ^= ((SBox[(r1 >>> 16) & 0xFF] & 0xFF) << 16) & 0xFFFFFFFF;
        C3 ^= ((SBox[r2 >>> 24] & 0xFF) << 24) & 0xFFFFFFFF;
        C3 ^= key[r][3] & 0xFFFFFFFF;
    }

    private int ProcessBlock(byte[] input, int inOffset, byte[] output, int outOffset)
    {
        if ((inOffset + (32 / 2)) > input.length)
        {
            throw new RuntimeException("input buffer too short");
        }

        if ((outOffset + (32 / 2)) > output.length)
        {
            throw new RuntimeException("output buffer too short");
        }

        UnPackBlock(input, inOffset);
        EncryptBlock(WorkingKey);
        PackBlock(output, outOffset);
        return BlockSize;
    }

    /** 
     Convert Big Endian byte array to Little Endian UInt 32.

     @param bs
     @param offset
     @return 
    */
    public static int bEToUInt32(byte[] buff, int offset)
    {
        int value = (buff[offset] << 24);
        value |= (buff[++offset] << 16) & 0xFF0000;
        value |= (buff[++offset] << 8) & 0xFF00;
        value |= buff[++offset] & 0xFF;
        return value;        
    }

    /** 
     Shift block to right.

     @param block
     @param n
    */
    public static void shiftRight(int[] block, int count)
    {
        int bit = 0;
        for (int i = 0; i < 4; ++i)
        {
            int b = block[i];
            block[i] = (b >>> count) | bit;
            bit = b << (32 - count);
        }
    }

    public static void multiplyP(int[] x)
    {
        boolean lsb = (x[3] & 1) != 0;
        shiftRight(x, 1);
        if (lsb)
        {
            x[0] ^= 0xe1000000l;
        }
    }

    /** 
     Get Uint 128 as array of UInt32.

     @param buff
     @return 
    */
    public static int[] getUint128(byte[] buff)
    {
        int[] us = new int[4];
        us[0] = bEToUInt32(buff, 0);
        us[1] = bEToUInt32(buff, 4);
        us[2] = bEToUInt32(buff, 8);
        us[3] = bEToUInt32(buff, 12);
        return us;
    }

    /** 
     Make Xor for 128 bits.

     @param block block.
     @param val
    */
    private static void Xor(byte[] block, byte[] value)
    {
        for (int pos = 0; pos != 16; ++pos)
        {
            block[pos] ^= value[pos];
        }
    }

    /** 
     Make Xor for 128 bits.

     @param block block.
     @param val
    */
    private static void Xor(int[] block, int[] value)
    {
        for (int pos = 0; pos != 4; ++pos)
        {
            block[pos] ^= value[pos];
        }
    }

    private static void MultiplyP8(int[] x)
    {
        long lsw = x[3];
        shiftRight(x, 8);
        for (int pos = 0; pos != 8; ++pos)
        {
            if ((lsw & (1 << pos)) != 0)
            {                
                x[0] ^= ((0xe1000000l >> (7 - pos)) & 0xFFFFFFFF);
            }
        }
    }

    /** 
     The GF(2128) field used is defined by the polynomial x^{128}+x^7+x^2+x+1.
     The authentication tag is constructed by feeding blocks of data into the GHASH function, 
     and encrypting the result.

     @param b
     @return 
    */
    private byte[] GetGHash(byte[] b)
    {
        byte[] Y = new byte[16];
        for (int pos = 0; pos < b.length; pos += 16)
        {
            byte[] X = new byte[16];
            int cnt = Math.min(b.length - pos, 16);
            System.arraycopy(b, pos, X, 0, cnt);
            Xor(Y, X);
            MultiplyH(Y);
        }
        return Y;
    }

    /** 
     Convert uint32 to Big Endian byte array.

     @param value
     @param buff
     @param offset
    */
    private static void UInt32_To_BE(int value, byte[] buff, int offset)
    {
        buff[offset] = (byte)(value >>> 24);
        buff[++offset] = (byte)(value >>> 16);
        buff[++offset] = (byte)(value >>> 8);
        buff[++offset] = (byte)(value);
    }

    private void MultiplyH(byte[] value)
    {
        int[] tmp = new int[4];
        for (int pos = 0; pos != 16; ++pos)
        {
            int[] m = M[pos + pos][value[pos] & 0x0f];
            tmp[0] ^= m[0];
            tmp[1] ^= m[1];
            tmp[2] ^= m[2];
            tmp[3] ^= m[3];
            m = M[pos + pos + 1][(value[pos] & 0xf0) >> 4];
            tmp[0] ^= m[0];
            tmp[1] ^= m[1];
            tmp[2] ^= m[2];
            tmp[3] ^= m[3];
        }

        UInt32_To_BE(tmp[0], value, 0);
        UInt32_To_BE(tmp[1], value, 4);
        UInt32_To_BE(tmp[2], value, 8);
        UInt32_To_BE(tmp[3], value, 12);
    }

    private void Init(byte[] H)
    {
        M[0] = new int[16][];
        M[1] = new int[16][];
        M[0][0] = new int[4];
        M[1][0] = new int[4];
        M[1][8] = getUint128(H);
        int[] tmp;
        for (int pos = 4; pos >= 1; pos >>= 1)
        {
            tmp = (int[])M[1][pos + pos].clone();
            multiplyP(tmp);
            M[1][pos] = tmp;
        }
        tmp = (int[])M[1][1].clone();
        multiplyP(tmp);
        M[0][8] = tmp;

        for (int pos = 4; pos >= 1; pos >>= 1)
        {
            tmp = (int[])M[0][pos + pos].clone();
            multiplyP(tmp);
            M[0][pos] = tmp;
        }

        for (int pos1 = 0; ;)
        {
            for (int pos2 = 2; pos2 < 16; pos2 += pos2)
            {
                for (int k = 1; k < pos2; ++k)
                {
                    tmp = (int[])M[pos1][pos2].clone();
                    Xor(tmp, M[pos1][k]);
                    M[pos1][pos2 + k] = tmp;
                }
            }

            if (++pos1 == 32)
            {
                return;
            }

            if (pos1 > 1)
            {
                M[pos1] = new int[16][];
                M[pos1][0] = new int[4];
                for (int pos = 8; pos > 0; pos >>= 1)
                {
                    tmp = (int[])M[pos1 - 2][pos].clone();
                    MultiplyP8(tmp);
                    M[pos1][pos] = tmp;
                }
            }
        }
    }

    private void gCTRBlock(byte[] buf, int bufCount)
    {
        for (int i = 15; i >= 12; --i)
        {
            if (++counter[i] != 0)
            {
                break;
            }
        }

        byte[] tmp = new byte[BlockSize];
        ProcessBlock(counter, 0, tmp, 0);
        byte[] hashBytes;
        if (Encrypt)
        {
            System.arraycopy(Zeroes, bufCount, tmp, bufCount, BlockSize - bufCount);
            hashBytes = tmp;
        }
        else
        {
            hashBytes = buf;
        }
        for (int pos = 0; pos != bufCount; ++pos)
        {
            tmp[pos] ^= buf[pos];
            Output.write(tmp[pos]);
        }
        Xor(S, hashBytes);
        MultiplyH(S);
        totalLength += (long)bufCount;
    }

    /** 
     Set packet length to byte array.

     @param length
     @param buff
     @param offset
    */
    private static void SetPackLength(long length, byte[] buff, int offset)
    {
        UInt32_To_BE((int)(length >> 32), buff, offset);
        UInt32_To_BE((int)length, buff, offset + 4);
    }

    /** 
     Reset 
    */
    private void Reset()
    {
        S = GetGHash(Aad);
        counter = (byte[]) J0.clone();
        BytesRemaining = 0;
        totalLength = 0;        
    }

    /** 
     Are tags equals.

     @param tag1
     @param tag2
     @return 
    */
    public static boolean TagsEquals(byte[] tag1, byte[] tag2)
    {
        for (int pos = 0; pos != 12; ++pos)
        {
            if (tag1[pos] != tag2[pos])
            {
                return false;
            }
        }
        return true;
    }

    /** 
     Write bytes to decrypt/encrypt.

     @param input
    */
    public void Write(byte[] input)
    {
        for (byte it : input)
        {
            bufBlock[BytesRemaining++] = it;
            if (BytesRemaining == BlockSize)
            {
                gCTRBlock(bufBlock, BlockSize);
                if (!Encrypt)
                {
                    System.arraycopy(bufBlock, BlockSize, bufBlock, 0, Tag.length);
                }
                BytesRemaining = 0;
            }
        }
    }

    /** 
     Process encrypting/decrypting.

     @return 
    */
    public final byte[] FlushFinalBlock()
    {
        //Crypt/Uncrypt remaining bytes.
        if (BytesRemaining > 0)
        {
            byte[] tmp = new byte[BlockSize];
            System.arraycopy(bufBlock, 0, tmp, 0, BytesRemaining);
            gCTRBlock(tmp, BytesRemaining);
        }
        //If tag is not needed.
        if (Security == Security.ENCRYPTION)
        {
            Reset();
            return this.Output.toByteArray();
        }
        //Count HASH.
        byte[] X = new byte[16];
        SetPackLength((long)Aad.length * 8, X, 0);
        SetPackLength(totalLength * 8, X, 8);
        Xor(S, X);
        MultiplyH(S);
        byte[] tag = new byte[BlockSize];
        ProcessBlock(J0, 0, tag, 0);
        Xor(tag, S);
        if (!Encrypt)
        {
            if (!TagsEquals(this.Tag, tag))
            {
                throw new GXDLMSException("Decrypt failed. Invalid tag.");
            }
        }
        else
        {
            //Tag size is 12 bytes.
            System.arraycopy(tag, 0, Tag, 0, 12);
        }
        Reset();
        return this.Output.toByteArray();
    }

    /** 
     Generate AES keys.

     @param key
     @param forEncryption
     @return 
    */
    private int[][] GenerateKey(boolean encrypt, byte[] key)
    {
        // Key length in words.
        int keyLen = key.length / 4;
        Rounds = keyLen + 6;
        // 4 words make one block.
        int[][] W = new int[Rounds + 1][4];
        // Copy the key into the round key array.
        int t = 0;
        for (int i = 0; i < key.length; t++)
        {
            W[t >> 2][t & 3] = ToUInt32(key, i);
            i += 4;
        }
        // while not enough round key material calculated calculate new values.
        int k = (Rounds + 1) << 2;
        for (int i = keyLen; (i < k); i++)
        {
            int temp = W[(i - 1) >> 2][(i - 1) & 3];            
            if ((i % keyLen) == 0)
            {
                temp = SubWord(Shift(temp, 8)) ^ (rcon[(i / keyLen) - 1] & 0xFF);
            }
            else if ((keyLen > 6) && ((i % keyLen) == 4))
            {
                temp = SubWord(temp);
            }
            W[i >> 2][i & 3] = W[(i - keyLen) >> 2][(i - keyLen) & 3] ^ temp;
        }

        if (!encrypt)
        {
            for (int j = 1; j < Rounds; j++)
            {
                for (int i = 0; i < 4; i++)
                {
                    W[j][i] = ImixCol(W[j][i]);
                }
            }
        }
        return W;
    }
}