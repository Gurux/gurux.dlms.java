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

import java.util.logging.Logger;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSException;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;

final class GXDLMSChippering {
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSChippering.class.getName());

    /**
     * Constructor.
     */
    private GXDLMSChippering() {

    }

    /**
     * Get nonse from frame counter and system title.
     * 
     * @param frameCounter
     *            Frame counter.
     * @param systemTitle
     *            System title.
     * @return
     */
    private static byte[] getNonse(final long frameCounter,
            final byte[] systemTitle) {
        byte[] nonce = new byte[12];
        System.arraycopy(systemTitle, 0, nonce, 0, systemTitle.length);
        nonce[8] = (byte) ((frameCounter >> 24) & 0xFF);
        nonce[9] = (byte) ((frameCounter >> 16) & 0xFF);
        nonce[10] = (byte) ((frameCounter >> 8) & 0xFF);
        nonce[11] = (byte) (frameCounter & 0xFF);
        return nonce;
    }

    static byte[] encryptAesGcm(final AesGcmParameter param,
            final byte[] plainText) {
        LOGGER.info("Encrypt settings: " + param.toString());
        param.setCountTag(null);
        GXByteBuffer data = new GXByteBuffer();
        if (param.getType() == CountType.PACKET) {
            data.setUInt8(param.getSecurity().getValue());
        }
        byte[] tmp = new byte[4];
        long frameCounter = param.getFrameCounter();
        tmp[0] = (byte) ((frameCounter >> 24) & 0xFF);
        tmp[1] = (byte) ((frameCounter >> 16) & 0xFF);
        tmp[2] = (byte) ((frameCounter >> 8) & 0xFF);
        tmp[3] = (byte) (frameCounter & 0xFF);
        byte[] aad = getAuthenticatedData(param.getSecurity(),
                param.getAuthenticationKey(), plainText);
        GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(
                param.getSecurity(), true, param.getBlockCipherKey(), aad,
                getNonse(frameCounter, param.getSystemTitle()), null);
        // Encrypt the secret message
        if (param.getSecurity() != Security.AUTHENTICATION) {
            gcm.write(plainText);
        }
        byte[] ciphertext = gcm.flushFinalBlock();
        if (param.getSecurity() == Security.AUTHENTICATION) {
            if (param.getType() == CountType.PACKET) {
                data.set(tmp);
            }
            if ((param.getType().getValue() & CountType.DATA.getValue()) != 0) {
                data.set(plainText);
            }
            if ((param.getType().getValue() & CountType.TAG.getValue()) != 0) {
                param.setCountTag(gcm.getTag());
                data.set(param.getCountTag());
            }
        } else if (param.getSecurity() == Security.ENCRYPTION) {
            data.set(tmp);
            data.set(ciphertext);
        } else if (param.getSecurity() == Security.AUTHENTICATION_ENCRYPTION) {
            if (param.getType() == CountType.PACKET) {
                data.set(tmp);
            }
            if ((param.getType().getValue() & CountType.DATA.getValue()) != 0) {
                data.set(ciphertext);
            }
            if ((param.getType().getValue() & CountType.TAG.getValue()) != 0) {
                param.setCountTag(gcm.getTag());
                data.set(param.getCountTag());
            }
        } else {
            throw new IllegalArgumentException("security");
        }
        if (param.getType() == CountType.PACKET) {
            GXByteBuffer tmp2 = new GXByteBuffer(10 + data.size());
            tmp2.setUInt8(param.getTag());
            gurux.dlms.internal.GXCommon.setObjectCount(data.size(), tmp2);
            tmp2.set(data.getData(), 0, data.size());
            data = tmp2;
        }
        byte[] crypted = data.array();
        LOGGER.info("Crypted: " + GXCommon.toHex(crypted));
        return crypted;
    }

    private static byte[] getAuthenticatedData(final Security security,
            final byte[] authenticationKey, final byte[] plainText) {
        if (security == Security.AUTHENTICATION) {
            GXByteBuffer tmp2 = new GXByteBuffer();
            tmp2.setUInt8((byte) security.getValue());
            tmp2.set(authenticationKey);
            tmp2.set(plainText);
            return tmp2.array();
        } else if (security == Security.ENCRYPTION) {
            return authenticationKey;
        } else if (security == Security.AUTHENTICATION_ENCRYPTION) {
            GXByteBuffer tmp2 = new GXByteBuffer();
            tmp2.setUInt8((byte) security.getValue());
            tmp2.set(authenticationKey);
            return tmp2.array();
        }
        return null;
    }

    /**
     * Decrypt data.
     * 
     * @param p
     *            Aes GcmParameter.
     * @return Crypted data.
     */
    static byte[] decryptAesGcm(final AesGcmParameter p,
            final GXByteBuffer data) {
        if (data == null || data.size() - data.position() < 2) {
            throw new IllegalArgumentException("cryptedData");
        }
        int ch = data.getUInt8();
        if (!(ch == 0x21 || ch == 0x28)) {
            Command cmd = Command.forValue(ch);
            if (!(cmd == Command.GLO_GET_REQUEST
                    || cmd == Command.GLO_GET_RESPONSE
                    || cmd == Command.GLO_SET_REQUEST
                    || cmd == Command.GLO_SET_RESPONSE
                    || cmd == Command.GLO_METHOD_REQUEST
                    || cmd == Command.GLO_METHOD_RESPONSE
                    || cmd == Command.GLO_EVENT_NOTIFICATION_REQUEST)) {
                throw new IllegalArgumentException("cryptedData");
            }
        }
        GXCommon.getObjectCount(data);
        Security security = Security.forValue(data.getUInt8());
        p.setSecurity(security);
        long frameCounter = data.getUInt32();
        p.setFrameCounter(frameCounter);
        LOGGER.info("Decrypt settings: " + p.toString());
        LOGGER.info("Encrypted: " + GXCommon.toHex(data.array()));
        byte[] tag = new byte[12];
        byte[] encryptedData;
        int length;
        if (security == Security.AUTHENTICATION) {
            length = data.size() - data.position() - 12;
            encryptedData = new byte[length];
            data.get(encryptedData);
            data.get(tag);
            // Check tag.
            encryptAesGcm(p, encryptedData);
            if (!GXDLMSChipperingStream.tagsEquals(tag, p.getCountTag())) {
                throw new GXDLMSException("Decrypt failed. Invalid tag.");
            }
            return encryptedData;
        }
        byte[] ciphertext = null;
        if (security == Security.ENCRYPTION) {
            length = data.size() - data.position();
            ciphertext = new byte[length];
            data.get(ciphertext);
        } else if (security == Security.AUTHENTICATION_ENCRYPTION) {
            length = data.size() - data.position() - 12;
            ciphertext = new byte[length];
            data.get(ciphertext);
            data.get(tag);
        }
        byte[] aad = getAuthenticatedData(security, p.getAuthenticationKey(),
                ciphertext);
        GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(security, true,
                p.getBlockCipherKey(), aad,
                getNonse(frameCounter, p.getSystemTitle()), tag);
        gcm.write(ciphertext);
        return gcm.flushFinalBlock();
    }
}
