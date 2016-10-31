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

package gurux.dlms.secure;

import java.security.PrivateKey;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXICipher;
import gurux.dlms.enums.Security;

/**
 * Gurux DLMS/COSEM Transport security (Ciphering) settings.
 */
public class GXCiphering implements GXICipher {
    private Security security = Security.NONE;
    private byte[] authenticationKey;
    /**
     * System title.
     */
    private byte[] systemTitle;

    private byte[] blockCipherKey;
    private long frameCounter = 0;

    /**
     * Private key.
     */
    private PrivateKey privateKey;

    /**
     * Constructor. Default values are from the Green Book.
     * 
     * @param title
     *            Used system title.
     */
    public GXCiphering(final byte[] title) {
        setSecurity(Security.NONE);
        setSystemTitle(title);
        setBlockCipherKey(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F });
        setAuthenticationKey(new byte[] { (byte) 0xD0, (byte) 0xD1, (byte) 0xD2,
                (byte) 0xD3, (byte) 0xD4, (byte) 0xD5, (byte) 0xD6, (byte) 0xD7,
                (byte) 0xD8, (byte) 0xD9, (byte) 0xDA, (byte) 0xDB, (byte) 0xDC,
                (byte) 0xDD, (byte) 0xDE, (byte) 0xDF });
    }

    /**
     * Cipher PDU.
     * 
     * @param tag
     *            Tag.
     * @param title
     *            System title.
     * @param data
     *            Plain text.
     * @return Secured data.
     */
    @Override
    public final byte[] encrypt(final int tag, final byte[] title,
            final byte[] data) {
        if (getSecurity() != Security.NONE) {
            AesGcmParameter p =
                    new AesGcmParameter(tag, getSecurity(), getFrameCounter(),
                            title, getBlockCipherKey(), getAuthenticationKey());
            byte[] tmp = GXDLMSChippering.encryptAesGcm(p, data);
            setFrameCounter(getFrameCounter() + 1);
            return tmp;
        }
        return data;
    }

    @Override
    public final Security decrypt(final byte[] title, final GXByteBuffer data) {
        AesGcmParameter p =
                new AesGcmParameter(title, blockCipherKey, authenticationKey);
        byte[] tmp = GXDLMSChippering.decryptAesGcm(p, data);
        data.clear();
        data.set(tmp);
        return p.getSecurity();
    }

    @Override
    public final void reset() {
        setSecurity(Security.NONE);
        setFrameCounter(0);
    }

    @Override
    public final boolean isCiphered() {
        return security != Security.NONE;
    }

    /**
     * @return Used security.
     */
    public final long getFrameCounter() {
        return frameCounter;
    }

    public final void setFrameCounter(final long value) {
        frameCounter = value;
    }

    /**
     * @return Used security.
     */
    public final Security getSecurity() {
        return security;
    }

    /**
     * @param value
     *            Used security.
     */
    public final void setSecurity(final Security value) {
        security = value;
    }

    /**
     * The SystemTitle is a 8 bytes (64 bit) value that identifies a partner of
     * the communication. First 3 bytes contains the three letters manufacturer
     * ID. The remainder of the system title holds for example a serial number.
     * 
     * @see <a href="http://www.dlms.com/organization/flagmanufacturesids">List
     *      of manufacturer IDs.</a>
     * @return System title.
     */
    public final byte[] getSystemTitle() {
        return systemTitle;
    }

    /**
     * @param value
     *            System title.
     */
    public final void setSystemTitle(final byte[] value) {
        systemTitle = value;
    }

    /**
     * Each block is ciphered with this key.
     * 
     * @return Block cipher key.
     */
    public final byte[] getBlockCipherKey() {
        return blockCipherKey;
    }

    public final void setBlockCipherKey(final byte[] value) {
        if (value != null && value.length != 16) {
            throw new IllegalArgumentException("Invalid Block Cipher Key.");
        }
        blockCipherKey = value;
    }

    /**
     * @return Authentication Key is 16 bytes value.
     */
    public final byte[] getAuthenticationKey() {
        return authenticationKey;
    }

    public final void setAuthenticationKey(final byte[] value) {
        if (value != null && value.length != 16) {
            throw new IllegalArgumentException("Invalid Authentication Key.");
        }
        authenticationKey = value;
    }

    /**
     * @return Private key.
     */
    public final PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * @param value
     *            Private key.
     */
    public final void setPrivateKey(final PrivateKey value) {
        this.privateKey = value;
    }
}