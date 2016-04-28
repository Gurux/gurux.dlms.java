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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXICipher;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Security;

public final class GXSecure {
    /**
     * Constructor.
     */
    private GXSecure() {

    }

    /**
     * Chipher text.
     * 
     * @param auth
     *            Authentication level.
     * @param data
     *            Text to chipher.
     * @param secret
     *            Secret.
     * @return Chiphered text.
     */
    public static byte[] secure(final GXDLMSSettings settings,
            final GXICipher cipher, final long ic, final byte[] data,
            final byte[] secret) {

        try {

            byte[] tmp;
            if (settings.getAuthentication() == Authentication.HIGH) {
                tmp = new byte[data.length + (16 - (data.length % 16))];
                System.arraycopy(data, 0, tmp, 0, data.length);
                for (int pos = 0; pos < tmp.length / 16; ++pos) {
                    GXDLMSChipperingStream.aes1Encrypt(tmp, pos * 16, secret);
                }
                return tmp;
            }
            // Get server Challenge.
            GXByteBuffer challenge = new GXByteBuffer();
            // Get shared secret
            if (settings.getAuthentication() == Authentication.HIGH_GMAC) {
                challenge.set(data);
            } else {
                challenge.set(data);
                challenge.set(secret);
            }
            tmp = challenge.array();
            if (settings.getAuthentication() == Authentication.HIGH_MD5) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return md.digest(tmp);
            } else if (settings
                    .getAuthentication() == Authentication.HIGH_SHA1) {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                return md.digest(tmp);
            } else if (settings
                    .getAuthentication() == Authentication.HIGH_GMAC) {
                // SC is always Security.Authentication.
                AesGcmParameter p =
                        new AesGcmParameter(0, Security.AUTHENTICATION, ic,
                                secret, cipher.getBlockCipherKey(),
                                cipher.getAuthenticationKey());
                p.setType(CountType.TAG);
                challenge.clear();
                challenge.setUInt8((byte) Security.AUTHENTICATION.getValue());
                challenge.setUInt32(p.getFrameCounter());
                challenge.set(GXDLMSChippering.encryptAesGcm(p, tmp));
                tmp = challenge.array();
                return tmp;
            }
            return data;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Generates challenge.
     * 
     * @param authentication
     *            Used authentication.
     * @return Generated challenge.
     */
    public static byte[]
            generateChallenge(final Authentication authentication) {
        Random r = new Random();
        // Random challenge is 8 to 64 bytes.
        // Texas Instruments accepts only 16 byte long challenge.
        // For this reason challenge size is 16 bytes at the moment.
        int len = 16;
        // int len = r.nextInt(57) + 8;
        byte[] result = new byte[len];
        for (int pos = 0; pos != len; ++pos) {
            // Allow printable characters only.
            do {
                result[pos] = (byte) r.nextInt(0x7A);
            } while (result[pos] < 0x21);
        }
        return result;
    }
}
