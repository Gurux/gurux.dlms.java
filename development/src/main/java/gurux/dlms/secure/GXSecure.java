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
import java.security.Signature;
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
     * @param settings
     *            DLMS settings.
     * @param cipher
     *            Chipher settings.
     * @param ic
     *            IC
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

            byte[] d, s;
            if (settings.getAuthentication() == Authentication.HIGH) {
                int len = data.length;
                if (len % 16 != 0) {
                    len += (16 - (data.length % 16));
                }
                if (secret.length > data.length) {
                    len = secret.length;
                    if (len % 16 != 0) {
                        len += (16 - (secret.length % 16));
                    }
                }
                s = new byte[len];
                d = new byte[len];
                System.arraycopy(data, 0, d, 0, data.length);
                System.arraycopy(secret, 0, s, 0, secret.length);
                for (int pos = 0; pos < d.length / 16; ++pos) {
                    GXDLMSChipperingStream.aes1Encrypt(d, pos * 16, s);
                }
                return d;
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
            d = challenge.array();
            MessageDigest md;
            switch (settings.getAuthentication()) {
            case HIGH_MD5:
                md = MessageDigest.getInstance("MD5");
                d = md.digest(d);
                break;
            case HIGH_SHA1:
                md = MessageDigest.getInstance("SHA-1");
                d = md.digest(d);
                break;
            case HIGH_SHA256:
                md = MessageDigest.getInstance("SHA-256");
                d = md.digest(d);
                break;
            case HIGH_GMAC:
                // SC is always Security.Authentication.
                AesGcmParameter p =
                        new AesGcmParameter(0, Security.AUTHENTICATION, ic,
                                secret, cipher.getBlockCipherKey(),
                                cipher.getAuthenticationKey());
                p.setType(CountType.TAG);
                challenge.clear();
                challenge.setUInt8((byte) Security.AUTHENTICATION.getValue());
                challenge.setUInt32(p.getInvocationCounter());
                challenge.set(GXDLMSChippering.encryptAesGcm(p, d));
                d = challenge.array();
                break;
            case HIGH_ECDSA:
                Signature sig = Signature.getInstance("SHA256withECDSA");
                try {
                    sig.initSign(cipher.getKeyAgreementKeyPair().getPrivate());
                    GXByteBuffer bb = new GXByteBuffer();
                    bb.set(settings.getCipher().getSystemTitle());
                    bb.set(settings.getSourceSystemTitle());
                    if (settings.isServer()) {
                        bb.set(settings.getCtoSChallenge());
                        bb.set(settings.getStoCChallenge());
                    } else {
                        bb.set(settings.getStoCChallenge());
                        bb.set(settings.getCtoSChallenge());
                    }
                    sig.update(bb.array());
                    d = sig.sign();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                break;
            default:
            }
            return d;
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
            result[pos] = (byte) r.nextInt(0x7A);
        }
        return result;
    }
}
