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

public interface GXICipher {

    /**
     * Encrypt PDU.
     * 
     * @param tag
     *            Tag.
     * @param systemTitle
     *            System Title.
     * @param data
     *            Data to encrypt.
     * @return Encrypted data.
     */
    byte[] encrypt(int tag, final byte[] systemTitle, byte[] data);

    /**
     * Decrypt data.
     * 
     * @param systemTitle
     *            System Title.
     * @param data
     *            Decrypted data.
     */
    Security decrypt(final byte[] systemTitle, GXByteBuffer data);

    /**
     * Reset encrypt settings.
     */
    void reset();

    /**
     * @return Is ciphering used.
     */
    boolean isCiphered();

    /**
     * @return Used security.
     */
    Security getSecurity();

    /**
     * @param value
     *            Used security.
     */
    void setSecurity(Security value);

    /**
     * @return system title.
     */
    byte[] getSystemTitle();

    /**
     * @return Block cipher key.
     */
    byte[] getBlockCipherKey();

    /**
     * @return Authentication key.
     */
    byte[] getAuthenticationKey();

    /**
     * @return Frame counter. Invocation counter.
     */
    long getFrameCounter();
}
