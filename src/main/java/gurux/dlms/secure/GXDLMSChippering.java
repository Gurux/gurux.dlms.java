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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSException;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;

final class GXDLMSChippering {

    /**
     * Constructor.
     */
    private GXDLMSChippering() {

    }

    /**
     * Get Nonse from frame counter and system title.
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

    static byte[] encryptAesGcm(final Command command, final Security security,
            final long frameCounter, final byte[] systemTitle,
            final byte[] blockCipherKey, final byte[] authenticationKey,
            final byte[] plainText) {
        Command cmd;
        switch (command) {
        case READ_REQUEST:
        case GET_REQUEST:
            cmd = Command.GLO_GET_REQUEST;
            break;
        case WRITE_REQUEST:
        case SET_REQUEST:
            cmd = Command.GLO_SET_REQUEST;
            break;
        case METHOD_REQUEST:
            cmd = Command.GLO_METHOD_REQUEST;
            break;
        case READ_RESPONSE:
        case GET_RESPONSE:
            cmd = Command.GLO_GET_RESPONSE;
            break;
        case WRITE_RESPONSE:
        case SET_RESPONSE:
            cmd = Command.GLO_SET_RESPONSE;
            break;
        case METHOD_RESPONSE:
            cmd = Command.GLO_METHOD_RESPONSE;
            break;
        default:
            throw new GXDLMSException("Invalid GLO command.");
        }
        AesGcmParameter p = new AesGcmParameter(cmd, security, frameCounter,
                systemTitle, blockCipherKey, authenticationKey, plainText);
        return encryptAesGcm(p);
    }

    private static byte[] encryptAesGcm(final AesGcmParameter param) {
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
                param.getAuthenticationKey(), param.getPlainText());
        GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(
                param.getSecurity(), true, param.getBlockCipherKey(), aad,
                getNonse(frameCounter, param.getSystemTitle()), null);
        // Encrypt the secret message
        if (param.getSecurity() != Security.AUTHENTICATION) {
            gcm.write(param.getPlainText());
        }
        byte[] ciphertext = gcm.flushFinalBlock();
        if (param.getSecurity() == Security.AUTHENTICATION) {
            if (param.getType() == CountType.PACKET) {
                data.set(tmp);
            }
            if ((param.getType().getValue() & CountType.DATA.getValue()) != 0) {
                data.set(param.getPlainText());
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
            tmp2.setUInt8(param.getCommand().getValue());
            gurux.dlms.internal.GXCommon.setObjectCount(data.size(), tmp2);
            tmp2.set(data.getData(), 0, data.size());
            return tmp2.array();
        }
        return data.array();
    }

    private static byte[] getAuthenticatedData(final Security security,
            final byte[] authenticationKey, final byte[] plainText) {
        try {
            if (security == Security.AUTHENTICATION) {
                ByteArrayOutputStream tmp2 = new ByteArrayOutputStream();
                tmp2.write((byte) security.getValue());
                tmp2.write(authenticationKey);
                tmp2.write(plainText);
                return tmp2.toByteArray();
            } else if (security == Security.ENCRYPTION) {
                return authenticationKey;
            } else if (security == Security.AUTHENTICATION_ENCRYPTION) {
                ByteArrayOutputStream tmp2 = new ByteArrayOutputStream();
                tmp2.write((byte) security.getValue());
                tmp2.write(authenticationKey);
                return tmp2.toByteArray();
            }
            return null;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Decrypt data.
     * 
     * @param cryptedText
     *            Crypted data.
     * @param systemTitle
     * @param BlockCipherKey
     * @param AuthenticationKey
     * @return
     */
    static byte[] decryptAesGcm(final GXByteBuffer cryptedText,
            final byte[] systemTitle, final byte[] blockCipherKey,
            final byte[] authenticationKey) {
        if (cryptedText == null
                || cryptedText.size() - cryptedText.position() < 2) {
            throw new IllegalArgumentException("cryptedData");
        }
        Command cmd = Command.forValue(cryptedText.getUInt8());
        if (!(cmd == Command.GLO_GET_REQUEST || cmd == Command.GLO_GET_RESPONSE
                || cmd == Command.GLO_SET_REQUEST
                || cmd == Command.GLO_SET_RESPONSE
                || cmd == Command.GLO_METHOD_REQUEST
                || cmd == Command.GLO_METHOD_RESPONSE)) {
            throw new IllegalArgumentException("cryptedData");
        }
        GXCommon.getObjectCount(cryptedText);
        Security security = Security.forValue(cryptedText.getUInt8());
        long frameCounter = cryptedText.getUInt32();
        byte[] tag = new byte[12];
        byte[] encryptedData;
        int length;
        if (security == Security.AUTHENTICATION) {
            length = cryptedText.size() - cryptedText.position() - 12;
            encryptedData = new byte[length];
            cryptedText.get(encryptedData);
            cryptedText.get(tag);
            // Check tag.
            AesGcmParameter p = new AesGcmParameter(Command.NONE, security,
                    frameCounter, systemTitle, blockCipherKey,
                    authenticationKey, encryptedData);
            encryptAesGcm(p);
            if (!GXDLMSChipperingStream.tagsEquals(tag, p.getCountTag())) {
                throw new GXDLMSException("Decrypt failed. Invalid tag.");
            }
            return encryptedData;
        }
        byte[] ciphertext = null;
        if (security == Security.ENCRYPTION) {
            length = cryptedText.size() - cryptedText.position();
            ciphertext = new byte[length];
            cryptedText.get(ciphertext);
        } else if (security == Security.AUTHENTICATION_ENCRYPTION) {
            length = cryptedText.size() - cryptedText.position() - 12;
            ciphertext = new byte[length];
            cryptedText.get(ciphertext);
            cryptedText.get(tag);
        }
        byte[] aad = getAuthenticatedData(security, authenticationKey,
                cryptedText.array());
        GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(security, false,
                blockCipherKey, aad, getNonse(frameCounter, systemTitle), tag);
        gcm.write(ciphertext);
        return gcm.flushFinalBlock();
    }
}
