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

import java.util.logging.Level;
import java.util.logging.Logger;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXICipher;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecuritySuite;

final class GXDLMSChippering {
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSChippering.class.getName());

    /*
     * Constructor.
     */
    private GXDLMSChippering() {

    }

    /*
     * Get nonse from frame counter and system title.
     * @param invocationCounter Invocation counter.
     * @param systemTitle System title.
     * @return Generated nonse.
     */
    private static byte[] getNonse(final long invocationCounter,
            final byte[] systemTitle) {
        byte[] nonce = new byte[12];
        System.arraycopy(systemTitle, 0, nonce, 0, systemTitle.length);
        nonce[8] = (byte) ((invocationCounter >> 24) & 0xFF);
        nonce[9] = (byte) ((invocationCounter >> 16) & 0xFF);
        nonce[10] = (byte) ((invocationCounter >> 8) & 0xFF);
        nonce[11] = (byte) (invocationCounter & 0xFF);
        return nonce;
    }

    static byte[] encryptAesGcm(final AesGcmParameter p,
            final byte[] plainText) {
        LOGGER.log(Level.INFO, "Encrypt settings: " + p.toString());
        p.setCountTag(null);
        GXByteBuffer data = new GXByteBuffer();
        if (p.getType() == CountType.PACKET) {
            data.setUInt8(p.getSecurity().getValue());
        }
        byte[] tmp = new byte[4];
        long invocationCounter = 0;
        if (p.getSecuritySuite() == SecuritySuite.AES_GCM_128) {
            invocationCounter = p.getInvocationCounter();
        }
        tmp[0] = (byte) ((invocationCounter >> 24) & 0xFF);
        tmp[1] = (byte) ((invocationCounter >> 16) & 0xFF);
        tmp[2] = (byte) ((invocationCounter >> 8) & 0xFF);
        tmp[3] = (byte) (invocationCounter & 0xFF);
        byte[] aad = getAuthenticatedData(p, plainText);
        byte[] iv = getNonse(invocationCounter, p.getSystemTitle());
        GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(p.getSecurity(),
                true, p.getBlockCipherKey(), aad, iv, null);
        // Encrypt the secret message
        if (p.getSecurity() != Security.AUTHENTICATION) {
            gcm.write(plainText);
        }
        byte[] ciphertext = gcm.flushFinalBlock();
        if (p.getSecurity() == Security.AUTHENTICATION) {
            if (p.getType() == CountType.PACKET) {
                data.set(tmp);
            }
            if ((p.getType() & CountType.DATA) != 0) {
                data.set(plainText);
            }
            if ((p.getType() & CountType.TAG) != 0) {
                p.setCountTag(gcm.getTag());
                data.set(p.getCountTag());
            }
        } else if (p.getSecurity() == Security.ENCRYPTION) {
            if (p.getType() == CountType.PACKET) {
                data.set(tmp);
            }
            data.set(ciphertext);
        } else if (p.getSecurity() == Security.AUTHENTICATION_ENCRYPTION) {
            if (p.getType() == CountType.PACKET) {
                data.set(tmp);
            }
            if ((p.getType() & CountType.DATA) != 0) {
                data.set(ciphertext);
            }
            if ((p.getType() & CountType.TAG) != 0) {
                p.setCountTag(gcm.getTag());
                data.set(p.getCountTag());
            }
        } else {
            throw new IllegalArgumentException("security");
        }
        if (p.getType() == CountType.PACKET) {
            GXByteBuffer tmp2 = new GXByteBuffer(10 + data.size());
            tmp2.setUInt8(p.getTag());
            gurux.dlms.internal.GXCommon.setObjectCount(data.size(), tmp2);
            tmp2.set(data.getData(), 0, data.size());
            data = tmp2;
        }
        byte[] crypted = data.array();
        LOGGER.log(Level.INFO, "Crypted: " + GXCommon.toHex(crypted, false));
        return crypted;
    }

    private static byte[] getAuthenticatedData(final AesGcmParameter p,
            final byte[] plainText) {
        GXByteBuffer data = new GXByteBuffer();
        int sc = p.getSecurity().getValue() | p.getSecuritySuite().getValue();

        switch (p.getSecurity()) {
        case AUTHENTICATION:
            data.setUInt8(sc);
            data.set(p.getAuthenticationKey());
            data.set(plainText);
            break;
        case AUTHENTICATION_ENCRYPTION:
            data.setUInt8(sc);
            data.set(p.getAuthenticationKey());
            if (p.getSecuritySuite() != SecuritySuite.AES_GCM_128) {
                // transaction-id
                GXByteBuffer transactionId = new GXByteBuffer();
                transactionId.setUInt64(p.getInvocationCounter());
                data.setUInt8(8);
                data.set(transactionId);
                // originator-system-title
                GXCommon.setObjectCount(p.getSystemTitle().length, data);
                data.set(p.getSystemTitle());
                // recipient-system-title
                GXCommon.setObjectCount(p.getRecipientSystemTitle().length,
                        data);
                data.set(p.getRecipientSystemTitle());
                // date-time not present
                data.setUInt8(0);
                // other-information not present
                data.setUInt8(0);
            }
            break;
        case ENCRYPTION:
            data.set(p.getAuthenticationKey());
            break;
        default:
            break;
        }
        return data.array();
    }

    /**
     * Decrypt data.
     * 
     * @param c
     *            Cipher settings.
     * @param p
     *            GMAC Parameter.
     * @return Encrypted data.
     */
    static byte[] decryptAesGcm(final GXICipher c, final AesGcmParameter p,
            final GXByteBuffer data) {
        if (data == null || data.size() - data.position() < 2) {
            throw new IllegalArgumentException("cryptedData");
        }
        byte[] tmp;
        int len, cmd = data.getUInt8();
        switch (cmd) {
        case Command.GENERAL_GLO_CIPHERING:
            len = GXCommon.getObjectCount(data);
            byte[] title = new byte[len];
            data.get(title);
            p.setSystemTitle(title);
            break;
        case Command.GENERAL_CIPHERING:
        case Command.GLO_INITIATE_REQUEST:
        case Command.GLO_INITIATE_RESPONSE:
        case Command.GLO_READ_REQUEST:
        case Command.GLO_READ_RESPONSE:
        case Command.GLO_WRITE_REQUEST:
        case Command.GLO_WRITE_RESPONSE:
        case Command.GLO_GET_REQUEST:
        case Command.GLO_GET_RESPONSE:
        case Command.GLO_SET_REQUEST:
        case Command.GLO_SET_RESPONSE:
        case Command.GLO_METHOD_REQUEST:
        case Command.GLO_METHOD_RESPONSE:
        case Command.GLO_EVENT_NOTIFICATION_REQUEST:
            break;
        default:
            throw new IllegalArgumentException("cryptedData");
        }
        int value = 0;
        long transactionId = 0;
        if (cmd == Command.GENERAL_CIPHERING) {
            len = GXCommon.getObjectCount(data);
            tmp = new byte[len];
            data.get(tmp);
            GXByteBuffer t = new GXByteBuffer(tmp);
            transactionId = t.getInt64();
            len = GXCommon.getObjectCount(data);
            tmp = new byte[len];
            data.get(tmp);
            p.setSystemTitle(tmp);
            len = GXCommon.getObjectCount(data);
            tmp = new byte[len];
            data.get(tmp);
            p.setRecipientSystemTitle(tmp);
            // Get date time.
            len = GXCommon.getObjectCount(data);
            if (len != 0) {
                tmp = new byte[len];
                data.get(tmp);
                p.setDateTime(tmp);
            }
            // other-information
            len = data.getUInt8();
            if (len != 0) {
                tmp = new byte[len];
                data.get(tmp);
                p.setOtherInformation(tmp);
            }
            // KeyInfo OPTIONAL
            len = data.getUInt8();
            // AgreedKey CHOICE tag.
            data.getUInt8();
            // key-parameters
            len = data.getUInt8();
            value = data.getUInt8();
            p.setKeyParameters(value);
            if (value == 1) {
                // KeyAgreement.ONE_PASS_DIFFIE_HELLMAN
                // key-ciphered-data
                len = GXCommon.getObjectCount(data);
                tmp = new byte[len];
                data.get(tmp);
                p.setKeyCipheredData(tmp);
            } else if (value == 2) {
                // KeyAgreement.STATIC_UNIFIED_MODEL
                len = GXCommon.getObjectCount(data);
                if (len != 0) {
                    throw new IllegalArgumentException(
                            "Invalid key parameters");
                }
            } else {
                throw new IllegalArgumentException("key-parameters");
            }
        }
        len = GXCommon.getObjectCount(data);
        p.setCipheredContent(data.remaining());
        byte sc = (byte) data.getUInt8();
        Security security = Security.forValue(sc & 0x30);
        SecuritySuite ss = SecuritySuite.forValue(sc & 0x3);
        if (ss != SecuritySuite.AES_GCM_128) {
            byte[] algID = GXCommon.hexToBytes("60857405080300");
            if (value == 1) {
                if (p.getSharedSecret() != null) {
                    GXByteBuffer kdf =
                            new GXByteBuffer(GXASymmetric.generateKDF("SHA-256",
                                    p.getSharedSecret(), 256, algID,
                                    p.getSystemTitle(),
                                    p.getRecipientSystemTitle(), null, null,
                                    SecuritySuite.ECDH_ECDSA_AES_GCM_128_SHA_256));
                    p.setBlockCipherKey(kdf.array());
                }
            } else if (value == 2) {
                GXByteBuffer tmp2 = new GXByteBuffer();
                if (p.getSharedSecret() == null) {
                    byte[] z = GXCommon.getSharedSecret(c,
                            CertificateType.KEY_AGREEMENT);
                    p.setSharedSecret(z);
                }
                tmp2.setUInt8(0x8);
                tmp2.setUInt64(transactionId);
                tmp2.set(p.getRecipientSystemTitle());
                GXByteBuffer kdf =
                        new GXByteBuffer(GXASymmetric.generateKDF("SHA-256",
                                p.getSharedSecret(), 256, algID,
                                p.getSystemTitle(), tmp2.array(), null, null,
                                SecuritySuite.ECDH_ECDSA_AES_GCM_128_SHA_256));
                p.setBlockCipherKey(kdf.subArray(0, 16));
            }
        }
        p.setSecurity(security);
        long invocationCounter = data.getUInt32();
        p.setInvocationCounter(invocationCounter);
        LOGGER.log(Level.INFO, "Decrypt settings: " + p.toString());
        LOGGER.log(Level.INFO, "Encrypted: " + GXCommon.toHex(data.getData(),
                false, data.position(), data.size() - data.position()));
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
                if (transactionId != 0) {
                    p.setInvocationCounter(transactionId);
                }
                if (p.getXml() == null) {
                    throw new GXDLMSException("Decrypt failed. Invalid tag.");
                } else {
                    p.getXml().appendComment("Decrypt failed. Invalid tag.");
                }
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
        byte[] aad = getAuthenticatedData(p, ciphertext),
                iv = getNonse(invocationCounter, p.getSystemTitle());
        GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(security, true,
                p.getBlockCipherKey(), aad, iv, tag);
        gcm.write(ciphertext);
        if (transactionId != 0) {
            p.setInvocationCounter(transactionId);
        }
        return gcm.flushFinalBlock();
    }
}
