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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.asn.GXx509Certificate;
import gurux.dlms.asn.enums.KeyUsage;
import gurux.dlms.enums.AcseServiceProvider;
import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.BerType;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.ConfirmedServiceError;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ExceptionServiceError;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.Service;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.enums.TranslatorOutputType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.ApplicationContextName;
import gurux.dlms.secure.AesGcmParameter;
import gurux.dlms.secure.GXCiphering;

/**
 * The services to access the attributes and methods of COSEM objects are
 * determined on DLMS/COSEM Application layer. The services are carried by
 * Application Protocol Data Units (APDUs).
 * <p>
 * In DLMS/COSEM the meter is primarily a server, and the controlling system is
 * a client. Also unsolicited (received without a request) messages are
 * available.
 * </p>
 */
final class GXAPDU {

    /**
     * Constructor.
     */
    private GXAPDU() {

    }

    /**
     * Retrieves the string that indicates the level of authentication, if any.
     */
    private static void getAuthenticationString(final GXDLMSSettings settings, final GXByteBuffer data,
            final boolean ignoreAcse) {
        if (settings.getAuthentication() != Authentication.NONE || (!ignoreAcse && settings.getCipher() != null
                && settings.getCipher().getSecurity() != Security.NONE)) {
            // Add sender ACSE-requirements field component.
            data.setUInt8(BerType.CONTEXT | PduType.SENDER_ACSE_REQUIREMENTS);
            data.setUInt8(2);
            data.setUInt8(BerType.BIT_STRING | BerType.OCTET_STRING);
            data.setUInt8(0x80);

            data.setUInt8(BerType.CONTEXT | PduType.MECHANISM_NAME);
            // Len
            data.setUInt8(7);
            // OBJECT IDENTIFIER
            byte[] p = { (byte) 0x60, (byte) 0x85, (byte) 0x74, 0x05, 0x08, 0x02,
                    (byte) settings.getAuthentication().getValue() };
            data.set(p);
        }
        // If authentication is used.
        if (settings.getAuthentication() != Authentication.NONE) {
            // Add Calling authentication information.
            int len = 0;
            byte[] callingAuthenticationValue = null;
            if (settings.getAuthentication() == Authentication.LOW) {
                if (settings.getPassword() != null) {
                    callingAuthenticationValue = settings.getPassword();
                    len = callingAuthenticationValue.length;
                }
            } else {
                callingAuthenticationValue = settings.getCtoSChallenge();
                len = callingAuthenticationValue.length;
            }
            // 0xAC
            data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AUTHENTICATION_VALUE);
            // Len
            data.setUInt8((2 + len));
            // Add authentication information.
            data.setUInt8(BerType.CONTEXT);
            // Len.
            data.setUInt8(len);
            if (len != 0) {
                data.set(callingAuthenticationValue);
            }
        }
    }

    /**
     * Code application context name.
     * 
     * @param settings
     *            DLMS settings.
     * @param data
     *            Byte buffer where data is saved.
     * @param cipher
     *            Is ciphering settings.
     */
    @SuppressWarnings("squid:S2259")
    private static void generateApplicationContextName(final int name, final GXDLMSSettings settings,
            final GXByteBuffer data, final GXICipher cipher) {
        // ProtocolVersion
        if (settings.getProtocolVersion() != null) {
            data.setUInt8(BerType.CONTEXT | PduType.PROTOCOL_VERSION);
            data.setUInt8(2);
            data.setUInt8((byte) (8 - settings.getProtocolVersion().length()));
            GXCommon.setBitString(data, settings.getProtocolVersion(), false);
        }
        // Application context name tag
        data.setUInt8((BerType.CONTEXT | BerType.CONSTRUCTED | PduType.APPLICATION_CONTEXT_NAME));
        // Len
        data.setUInt8(0x09);
        data.setUInt8(BerType.OBJECT_IDENTIFIER);
        // Len
        data.setUInt8(0x07);
        boolean ciphered = settings.isCiphered(true);
        data.setUInt8(0x60);
        data.setUInt8(0x85);
        data.setUInt8(0x74);
        data.setUInt8(0x5);
        data.setUInt8(0x8);
        data.setUInt8(0x1);
        if (name != 0) {
            data.setUInt8(name);
        } else {
            if (settings.getUseLogicalNameReferencing()) {
                if (ciphered) {
                    data.setUInt8(3);
                } else {
                    data.setUInt8(1);
                }
            } else {
                if (ciphered) {
                    data.setUInt8(4);
                } else {
                    data.setUInt8(2);
                }
            }
        }
        // Add system title.
        if (!settings.isServer() && (ciphered ||
                (settings.getAuthentication() == Authentication.HIGH_GMAC
                || settings.getAuthentication() == Authentication.HIGH_SHA256
                || settings.getAuthentication() == Authentication.HIGH_ECDSA))) {
            if (cipher.getSystemTitle() == null || cipher.getSystemTitle().length == 0) {
                throw new IllegalArgumentException("SystemTitle");
            }
            // Add calling-AP-title
            data.setUInt8((BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AP_TITLE));
            // LEN
            data.setUInt8((2 + cipher.getSystemTitle().length));
            data.setUInt8(BerType.OCTET_STRING);
            // LEN
            data.setUInt8(cipher.getSystemTitle().length);
            data.set(cipher.getSystemTitle());
            if (settings.getClientPublicKeyCertificate() != null) {
                // Add calling-AE-qualifier.
                byte[] raw = settings.getClientPublicKeyCertificate().getEncoded();
                data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AE_QUALIFIER);
                // LEN
                GXCommon.setObjectCount(4 + raw.length, data);
                data.setUInt8(BerType.OCTET_STRING);
                // LEN
                GXCommon.setObjectCount(raw.length, data);
                data.set(raw);
            }
        }
        // Add CallingAEInvocationId.
        if (!settings.isServer() && settings.getUserId() != -1) {
            data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AE_INVOCATION_ID);
            // LEN
            data.setUInt8(3);
            data.setUInt8(BerType.INTEGER);
            // LEN
            data.setUInt8(1);
            data.setUInt8(settings.getUserId());
        }
    }

    /**
     * Generate User information initiate request.
     * 
     * @param settings
     *            DLMS settings.
     * @param data
     *            Received data.
     * @param xml
     *            Is XML used.
     */
    static void getInitiateRequest(final GXDLMSSettings settings, final GXByteBuffer data, final boolean xml) {
        // Tag for xDLMS-Initiate request
        data.setUInt8(Command.INITIATE_REQUEST);
        // Usage field for the response allowed component.

        // Usage field for dedicated-key component.
        if ((settings.getCipher() == null || settings.getCipher().getDedicatedKey() == null
                || settings.getCipher().getSecurity() == Security.NONE) && !xml) {
            // Not used
            data.setUInt8(0x00);
        } else {
            data.setUInt8(1);
            GXCommon.setObjectCount(settings.getCipher().getDedicatedKey().length, data);
            data.set(settings.getCipher().getDedicatedKey());
        }

        // encoding of the response-allowed component (BOOLEAN DEFAULT TRUE)
        // usage flag (FALSE, default value TRUE conveyed)
        data.setUInt8(0);

        // Usage field of the proposed-quality-of-service component. Not used
        data.setUInt8(0x00);
        data.setUInt8(settings.getDLMSVersion());
        // Tag for conformance block
        data.setUInt8(0x5F);
        data.setUInt8(0x1F);
        data.setUInt8(DataType.BITSTRING);
        // Pad bits.
        data.setUInt8(0);
        GXBitString bs = new GXBitString(Conformance.toInteger(settings.getProposedConformance()), 24);
        data.set(bs.getValue());
        data.setUInt16(settings.getMaxPduSize());
    }

    /*
     * Generate user information.
     * @param settings DLMS settings.
     * @param cipher Ciphering interface.
     * @param data Generated user information.
     */
    static void generateUserInformation(final GXDLMSSettings settings, final GXICipher cipher,
            final GXByteBuffer encryptedData, final GXByteBuffer data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.USER_INFORMATION);
        if (!settings.isCiphered(true)) {
            // Length for AARQ user field
            data.setUInt8(0x10);
            // Coding the choice for user-information (Octet STRING, universal)
            data.setUInt8(BerType.OCTET_STRING);
            // Length
            data.setUInt8(0);
            int offset = data.size();
            getInitiateRequest(settings, data, false);
            data.setUInt8(offset - 1, data.size() - offset);
        } else {
            if (encryptedData != null && encryptedData.size() != 0) {
                // Length for AARQ user field
                data.setUInt8((byte) (4 + encryptedData.size()));
                // Tag
                data.setUInt8(BerType.OCTET_STRING);
                data.setUInt8((byte) (2 + encryptedData.size()));
                data.setUInt8((byte) Command.GLO_INITIATE_REQUEST);
                data.setUInt8((byte) encryptedData.size());
                data.set(encryptedData);
            } else {
                GXByteBuffer tmp = new GXByteBuffer();
                getInitiateRequest(settings, tmp, false);
                AesGcmParameter p = new AesGcmParameter(settings, Command.GLO_INITIATE_REQUEST, cipher.getSecurity(),
                        cipher.getSecuritySuite(), cipher.getInvocationCounter(), cipher.getSystemTitle(),
                        cipher.getBlockCipherKey(), cipher.getAuthenticationKey());
                byte[] crypted = GXCiphering.encrypt(p, tmp.array());
                cipher.setInvocationCounter(1 + cipher.getInvocationCounter());
                // Length for AARQ user field
                data.setUInt8((2 + crypted.length));
                data.setUInt8(BerType.OCTET_STRING);
                data.setUInt8(crypted.length);
                data.set(crypted);
            }
        }
    }

    /*
     * Generates Aarq.
     */
    public static void generateAarq(final GXDLMSSettings settings, final GXICipher cipher,
            final GXByteBuffer encryptedData, final GXByteBuffer data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer tmp = new GXByteBuffer();
        ///////////////////////////////////////////
        // Add Application context name.
        generateApplicationContextName(0, settings, tmp, cipher);
        getAuthenticationString(settings, tmp, (encryptedData != null && encryptedData.size() != 0));
        generateUserInformation(settings, cipher, encryptedData, tmp);
        // AARQ APDU Tag
        data.setUInt8(BerType.APPLICATION | BerType.CONSTRUCTED);
        GXCommon.setObjectCount(tmp.size(), data);
        data.set(tmp);
    }

    private static void getConformance(final long value, final GXDLMSTranslatorStructure xml) {
        if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
            for (Conformance it : Conformance.getEnumConstants()) {
                if ((it.getValue() & value) != 0) {
                    xml.appendLine(TranslatorGeneralTags.CONFORMANCE_BIT, "Name",
                            TranslatorSimpleTags.conformancetoString(it));
                }
            }
        } else {
            for (Conformance it : Conformance.getEnumConstants()) {
                if ((it.getValue() & value) != 0) {
                    xml.append(TranslatorStandardTags.conformancetoString(it) + " ");
                }
            }
        }
    }

    /*
     * Parse User Information from PDU.
     */
    static SourceDiagnostic parseUserInformation(final GXDLMSSettings settings, final GXICipher cipher,
            final GXByteBuffer data, final GXDLMSTranslatorStructure xml) throws Exception {
        short len = data.getUInt8();
        if (data.size() - data.position() < len) {
            if (xml == null) {
                throw new IllegalArgumentException("Not enough data.");
            }
            xml.appendComment("Error: Invalid data size.");
        }
        // Encoding the choice for user information
        short tag = data.getUInt8();
        if (tag != 0x4) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        len = data.getUInt8();
        if (data.size() - data.position() < len) {
            if (xml == null) {
                throw new IllegalArgumentException("Not enough data.");
            }
            xml.appendComment("Error: Invalid data size.");
        }
        if (xml != null && xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
            xml.appendLine(TranslatorGeneralTags.USER_INFORMATION, null,
                    GXCommon.toHex(data.getData(), false, data.position(), len));
            data.position(data.position() + len);
            return SourceDiagnostic.NONE;
        }
        return parseInitiate(false, settings, cipher, data, xml);
    }

    @SuppressWarnings("squid:S1066")
    static SourceDiagnostic parse(final boolean initiateRequest, final GXDLMSSettings settings, final GXICipher cipher,
            final GXByteBuffer data, final GXDLMSTranslatorStructure xml, final int tag2) {
        int len;
        int tag;
        GXByteBuffer tmp2 = new GXByteBuffer();
        tmp2.setUInt8(0);
        boolean response = tag2 == Command.INITIATE_RESPONSE;
        if (response) {
            if (xml != null) {
                // <InitiateResponse>
                xml.appendStartTag(Command.INITIATE_RESPONSE);
            }
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            if (tag != 0) {
                settings.setQualityOfService((byte) data.getUInt8());
                if (xml != null && xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    // NegotiatedQualityOfService
                    xml.appendLine(TranslatorGeneralTags.NEGOTIATED_QUALITY_OF_SERVICE, "Value",
                            String.format("%02d", settings.getQualityOfService()));
                }
            }
        } else if (tag2 == Command.INITIATE_REQUEST) {
            if (xml != null) {
                xml.appendStartTag(Command.INITIATE_REQUEST);
            }
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            if (tag != 0) {
                // Return error if ciphering is not used.
                if (xml == null && (cipher == null || cipher.getSecurity() != Security.AUTHENTICATION_ENCRYPTION)) {
                    return SourceDiagnostic.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED;
                }
                len = data.getUInt8();
                byte[] tmp = new byte[len];
                data.get(tmp);
                if (settings.getCipher() != null) {
                    settings.getCipher().setDedicatedKey(tmp);
                }
                if (settings.getAssignedAssociation() != null) {
                    settings.getAssignedAssociation().getXDLMSContextInfo().setCypheringInfo(tmp);
                }
                if (xml != null) {
                    xml.appendLine(TranslatorGeneralTags.DEDICATED_KEY, null, GXCommon.toHex(tmp, false));
                }
            } else if (settings.getCipher() != null) {
                settings.getCipher().setDedicatedKey(null);
            }
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            if (tag != 0) {
                len = data.getUInt8();
                if (xml != null && (initiateRequest || xml.getOutputType() == TranslatorOutputType.SIMPLE_XML)) {
                    xml.appendLine(TranslatorGeneralTags.PROPOSED_QUALITY_OF_SERVICE, null, String.valueOf(len));
                }
            } else {
                if (xml != null && xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    xml.appendLine(TranslatorTags.RESPONSE_ALLOWED, null, "true");
                }
            }
            // Optional usage field of the proposed quality of service component
            tag = data.getUInt8();
            if (tag != 0) {
                settings.setQualityOfService((byte) data.getUInt8());
            }
        } else if (tag2 == Command.CONFIRMED_SERVICE_ERROR) {
            if (xml != null) {
                xml.appendStartTag(Command.CONFIRMED_SERVICE_ERROR);
                if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    data.getUInt8();
                    xml.appendStartTag(TranslatorTags.INITIATE_ERROR);
                    ServiceError type = ServiceError.forValue(data.getUInt8());

                    String str = TranslatorStandardTags.serviceErrorToString(type);
                    String value = TranslatorStandardTags.getServiceErrorValue(type, (byte) data.getUInt8());
                    xml.appendLine("x:" + str, null, value);
                    xml.appendEndTag(TranslatorTags.INITIATE_ERROR);
                } else {
                    xml.appendLine(TranslatorTags.SERVICE, "Value", xml.integerToHex(data.getUInt8(), 2));
                    ServiceError type = ServiceError.forValue(data.getUInt8());
                    xml.appendStartTag(TranslatorTags.SERVICE_ERROR);
                    xml.appendLine(TranslatorSimpleTags.serviceErrorToString(type), "Value",
                            TranslatorSimpleTags.getServiceErrorValue(type, (byte) data.getUInt8()));
                    xml.appendEndTag(TranslatorTags.SERVICE_ERROR);
                }
                xml.appendEndTag(Command.CONFIRMED_SERVICE_ERROR);
                return SourceDiagnostic.NONE;
            }
            throw new GXDLMSConfirmedServiceError(ConfirmedServiceError.forValue(data.getUInt8()),
                    ServiceError.forValue(data.getUInt8()), data.getUInt8());
        } else {
            if (xml != null) {
                xml.appendComment("Error: Failed to decrypt data.");
                data.position(data.size());
                return SourceDiagnostic.NONE;
            }
            throw new IllegalArgumentException("Invalid tag.");
        }
        // Get DLMS version number.
        if (!response) {
            int ver = data.getUInt8();
            settings.setDLMSVersion(ver);
            if (ver != 6 && !settings.isServer()) {
                throw new IllegalArgumentException("Invalid DLMS version number.");
            }
            // ProposedDlmsVersionNumber
            if (xml != null && (initiateRequest || xml.getOutputType() == TranslatorOutputType.SIMPLE_XML)) {
                xml.appendLine(TranslatorGeneralTags.PROPOSED_DLMS_VERSION_NUMBER, "Value",
                        xml.integerToHex(settings.getDLMSVersion(), 2));
            }
        } else {
            if (data.getUInt8() != 6) {
                throw new IllegalArgumentException("Invalid DLMS version number.");
            }
            if (xml != null && (initiateRequest || xml.getOutputType() == TranslatorOutputType.SIMPLE_XML)) {
                xml.appendLine(TranslatorGeneralTags.NEGOTIATED_DLMS_VERSION_NUMBER, "Value",
                        xml.integerToHex(settings.getDLMSVersion(), 2));
            }
        }

        // Tag for conformance block
        tag = data.getUInt8();
        if (tag != 0x5F) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        // Old Way...
        if (data.getUInt8(data.position()) == 0x1F) {
            data.getUInt8();
        }
        // len =
        data.getUInt8();
        byte[] conformance = new byte[4];
        data.get(conformance);
        GXBitString bs = new GXBitString(conformance);
        int v = bs.toInteger();
        if (settings.isServer()) {
            settings.setNegotiatedConformance(
                    Conformance.forValue(v & Conformance.toInteger(settings.getProposedConformance())));
            if (xml != null) {
                xml.appendStartTag(TranslatorGeneralTags.PROPOSED_CONFORMANCE);
                getConformance(v, xml);
            }
        } else {
            if (xml != null) {
                xml.appendStartTag(TranslatorGeneralTags.NEGOTIATED_CONFORMANCE);
                getConformance(v, xml);
            }
            Set<Conformance> c = Conformance.forValue(v);
            settings.setNegotiatedConformance(c);
        }

        if (!response) {
            // Proposed max PDU size.
            int pdu = data.getUInt16();
            settings.setMaxPduSize(pdu);
            if (xml != null) {
                // ProposedConformance closing
                if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendEndTag(TranslatorGeneralTags.PROPOSED_CONFORMANCE);
                } else if (initiateRequest) {
                    xml.append(TranslatorGeneralTags.PROPOSED_CONFORMANCE, false);
                }
                // ProposedMaxPduSize
                xml.appendLine(TranslatorGeneralTags.PROPOSED_MAX_PDU_SIZE, "Value", xml.integerToHex(pdu, 4));
            }
            // If client asks too high PDU.
            if (pdu > settings.getMaxServerPDUSize()) {
                settings.setMaxPduSize(settings.getMaxServerPDUSize());
            }
        } else {
            int pduSize = data.getUInt16();
            if (xml == null && pduSize < 64) {
                throw new GXDLMSConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR, ServiceError.SERVICE,
                        Service.PDU_SIZE.getValue());
            }
            if (xml != null) {
                // NegotiatedConformance closing
                if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendEndTag(TranslatorGeneralTags.NEGOTIATED_CONFORMANCE);
                } else if (initiateRequest) {
                    xml.append(TranslatorGeneralTags.NEGOTIATED_CONFORMANCE, false);
                }
                // NegotiatedMaxPduSize
                xml.appendLine(TranslatorGeneralTags.NEGOTIATED_MAX_PDU_SIZE, "Value", xml.integerToHex(pduSize, 4));
            }
            // If client asks too high PDU.
            if (pduSize > settings.getMaxServerPDUSize()) {
                pduSize = settings.getMaxServerPDUSize();
            }
            // Max PDU size.
            settings.setMaxPduSize(pduSize);
        }
        if (response) {
            // VAA Name
            tag = data.getUInt16();
            if (xml != null) {
                if (initiateRequest || xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendLine(TranslatorGeneralTags.VAA_NAME, "Value", xml.integerToHex(tag, 4));
                }
            }
            if (tag == 0x0007) {
                if (initiateRequest) {
                    settings.setUseLogicalNameReferencing(true);
                } else {
                    // If LN
                    if (!settings.getUseLogicalNameReferencing() && xml == null) {
                        throw new IllegalArgumentException("Invalid VAA.");
                    }
                }
            } else if (tag == 0xFA00) {
                // If SN
                if (initiateRequest) {
                    settings.setUseLogicalNameReferencing(false);
                } else {
                    if (settings.getUseLogicalNameReferencing()) {
                        throw new IllegalArgumentException("Invalid VAA.");
                    }
                }
            } else {
                // Unknown VAA.
                throw new IllegalArgumentException("Invalid VAA.");
            }
            if (xml != null) {
                // <InitiateResponse>
                xml.appendEndTag(Command.INITIATE_RESPONSE);
            }
        } else if (xml != null) {
            xml.appendEndTag(Command.INITIATE_REQUEST);
        }
        return SourceDiagnostic.NONE;
    }

    static SourceDiagnostic parseInitiate(final boolean initiateRequest, final GXDLMSSettings settings,
            final GXICipher cipher, final GXByteBuffer data, final GXDLMSTranslatorStructure xml)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // Tag for xDLMS-Initate.response
        int tag = data.getUInt8();
        int originalPos;
        byte[] tmp, encrypted;
        AesGcmParameter p;
        if (tag == Command.GLO_INITIATE_RESPONSE || tag == Command.GLO_INITIATE_REQUEST
                || tag == Command.DED_INITIATE_RESPONSE || tag == Command.DED_INITIATE_REQUEST
                || tag == Command.GENERAL_GLO_CIPHERING || tag == Command.GENERAL_DED_CIPHERING) {
            if (xml != null) {
                originalPos = data.position();
                byte[] st;
                int cnt;
                if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    xml.appendStartTag(tag);
                }
                if (tag == Command.GENERAL_GLO_CIPHERING || tag == Command.GENERAL_DED_CIPHERING) {
                    cnt = GXCommon.getObjectCount(data);
                    st = new byte[cnt];
                    data.get(st);
                    xml.appendLine(TranslatorTags.SYSTEM_TITLE, null, GXCommon.toHex(st, false));
                } else {
                    st = settings.getSourceSystemTitle();
                }
                cnt = GXCommon.getObjectCount(data);
                encrypted = new byte[cnt];
                data.get(encrypted);
                if (cipher != null && xml.isComments()) {
                    int pos = xml.getXmlLength();
                    int pos2 = data.position();
                    try {
                        data.position(originalPos - 1);
                        p = new AesGcmParameter(settings, st, settings.getCipher().getBlockCipherKey(),
                                settings.getCipher().getAuthenticationKey());
                        p.setXml(xml);
                        tmp = GXCiphering.decrypt(settings.getCipher(), p, data);
                        data.clear();
                        data.set(tmp);
                        cipher.setSecurity(p.getSecurity());
                        short tag1 = data.getUInt8();
                        xml.startComment("Decrypted data:");
                        xml.appendLine("Security: " + p.getSecurity());
                        xml.appendLine("Invocation Counter: " + p.getInvocationCounter());
                        parse(initiateRequest, settings, cipher, data, xml, tag1);
                        xml.endComment();
                    } catch (Exception ex) {
                        // It's OK if this fails.
                        xml.setXmlLength(pos);
                        data.position(pos2);
                    }
                }
                if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendLine(tag, "Value", GXCommon.toHex(encrypted, false));
                } else {
                    xml.appendLine(TranslatorTags.CIPHERED_SERVICE, null, GXCommon.toHex(encrypted, false));
                    xml.appendEndTag(tag);
                }
                return SourceDiagnostic.NONE;
            }
            data.position(data.position() - 1);
            p = new AesGcmParameter(settings, settings.getSourceSystemTitle(), settings.getCipher().getBlockCipherKey(),
                    settings.getCipher().getAuthenticationKey());
            tmp = GXCiphering.decrypt(settings.getCipher(), p, data);
            data.size(0);
            data.set(tmp);
            // Update used security to server.
            if (settings.isServer()) {
                cipher.setSecurity(p.getSecurity());
            }
            tag = data.getUInt8();
        }
        return parse(initiateRequest, settings, cipher, data, xml, tag);
    }

    /**
     * Parse application context name.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     * @param xml
     *            XML.
     * @return null if succeeded.
     */
    private static ApplicationContextName parseApplicationContextName(final GXDLMSSettings settings,
            final GXByteBuffer buff, final GXDLMSTranslatorStructure xml) {
        // Get length.
        int len = buff.getUInt8();
        if (buff.size() - buff.position() < len) {
            throw new IllegalArgumentException("Encoding failed. Not enough data.");
        }
        if (buff.getUInt8() != 0x6) {
            throw new IllegalArgumentException("Encoding failed. Not an Object ID.");
        }
        if (settings.isServer() && settings.getCipher() != null) {
            settings.getCipher().setSecurity(Security.NONE);
        }
        // Object ID length.
        len = buff.getUInt8();
        byte[] tmp = new byte[len];
        buff.get(tmp);
        if (tmp[0] != 0x60 || tmp[1] != -123 || tmp[2] != 0x74 || tmp[3] != 0x5 || tmp[4] != 0x8 || tmp[5] != 0x1) {
            if (xml != null) {
                xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, "Value", "UNKNOWN");
                return null;
            }
            throw new IllegalArgumentException("Encoding failed. Invalid Application context name.");
        }
        byte name = tmp[6];
        if (xml != null) {
            if (name == 1) {
                if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, "Value", "LN");
                } else {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, null, "1");
                }
                settings.setUseLogicalNameReferencing(true);
            } else if (name == 3) {
                if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, "Value", "LN_WITH_CIPHERING");
                } else {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, null, "3");
                }
                settings.setUseLogicalNameReferencing(true);
            } else if (name == 2) {
                if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, "Value", "SN");
                } else {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, null, "2");
                }
                settings.setUseLogicalNameReferencing(false);
            } else if (name == 4) {
                if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, "Value", "SN_WITH_CIPHERING");
                } else {
                    xml.appendLine(TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, null, "4");
                }
                settings.setUseLogicalNameReferencing(false);
            }
            return null;
        }
        if (settings.assignedAssociation != null) {
            if (settings.assignedAssociation.getApplicationContextName().getContextId().getValue() == name) {
                return null;
            }
            return settings.assignedAssociation.getApplicationContextName().getContextId();
        } else {
            if (settings.getUseLogicalNameReferencing()) {
                if (name == 1 && (settings.isServer()
                        || (settings.getCipher() == null || settings.getCipher().getSecurity() == Security.NONE))) {
                    return null;
                }
                // If ciphering is used.
                if (name == 3 && (settings.isServer()
                        || (settings.getCipher() != null && settings.getCipher().getSecurity() != Security.NONE))) {
                    return null;
                }
            } else {
                if (name == 2 && ((settings.isServer() || settings.getCipher() == null
                        || settings.getCipher().getSecurity() == Security.NONE))) {
                    return null;
                }
                // If ciphering is used.
                if (name == 4 && (settings.isServer()
                        || (settings.getCipher() != null && settings.getCipher().getSecurity() != Security.NONE))) {
                    return null;
                }
            }
        }
        return ApplicationContextName.values()[name];
    }

    private static void validateAare(final GXDLMSSettings settings, final GXByteBuffer buff) {
        int tag = buff.getUInt8();
        if (settings.isServer()) {
            if (tag != (BerType.APPLICATION | BerType.CONSTRUCTED | PduType.PROTOCOL_VERSION)) {
                throw new IllegalArgumentException("Invalid tag.");
            }
        } else {
            if (tag != (BerType.APPLICATION | BerType.CONSTRUCTED | PduType.APPLICATION_CONTEXT_NAME)) {
                throw new IllegalArgumentException("Invalid tag.");
            }
        }
    }

    /*
     * Parse APDU.
     */
    public static Object parsePDU(final GXDLMSSettings settings, final GXICipher cipher, final GXByteBuffer buff,
            final GXDLMSTranslatorStructure xml) {
        // Get AARE tag and length
        validateAare(settings, buff);
        int len = GXCommon.getObjectCount(buff);
        int size = buff.size() - buff.position();
        if (len > size) {
            if (xml == null) {
                throw new IllegalArgumentException("Not enough data.");
            }
            xml.appendComment("Error: Invalid data size.");
        }
        // Opening tags
        if (xml != null) {
            if (settings.isServer()) {
                xml.appendStartTag(Command.AARQ);
            } else {
                xml.appendStartTag(Command.AARE);
            }
        }
        Object ret = parsePDU2(settings, cipher, buff, xml);
        // Closing tags
        if (xml != null) {
            if (settings.isServer()) {
                xml.appendEndTag(Command.AARQ);
            } else {
                xml.appendEndTag(Command.AARE);
            }
        }
        return ret;
    }

    private static AcseServiceProvider parseProtocolVersion(GXDLMSSettings settings, GXByteBuffer buff,
            GXDLMSTranslatorStructure xml) {
        // Get count.
        buff.getUInt8();
        byte unusedBits = (byte) buff.getUInt8();
        if (unusedBits > 8) {
            throw new IllegalArgumentException("unusedBits");
        }
        byte value = (byte) buff.getUInt8();
        StringBuilder sb = new StringBuilder();
        GXCommon.toBitString(sb, value, 8 - unusedBits);
        settings.setProtocolVersion(sb.toString());
        if (xml != null) {
            xml.appendLine(TranslatorTags.PROTOCOL_VERSION, "Value", settings.getProtocolVersion());
        } else {
            if (!settings.getProtocolVersion().equals("100001")) {
                return AcseServiceProvider.NO_COMMON_ACSE_VERSION;
            }
        }
        if (xml != null) {
            xml.appendLine(TranslatorTags.PROTOCOL_VERSION, "Value", settings.getProtocolVersion());
        }
        return AcseServiceProvider.NONE;
    }

    /*
     * Parse APDU.
     */
    @SuppressWarnings("squid:S106")
    public static Object parsePDU2(final GXDLMSSettings settings, final GXICipher cipher, final GXByteBuffer buff,
            final GXDLMSTranslatorStructure xml) {
        AssociationResult resultComponent = AssociationResult.ACCEPTED;
        String msg = null;
        Object ret = 0;
        int len, tag;
        ApplicationContextName name = null;
        byte[] tmp;
        while (buff.position() < buff.size()) {
            tag = buff.getUInt8();
            switch (tag) {
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.APPLICATION_CONTEXT_NAME:
                if ((name = parseApplicationContextName(settings, buff, xml)) != null) {
                    if (!settings.isServer()) {
                        switch (name) {
                        case LOGICAL_NAME:
                            msg = " Meter expects Logical Name referencing.";
                            break;
                        case SHORT_NAME:
                            msg = " Meter expects Short Name referencing.";
                            break;
                        case LOGICAL_NAME_WITH_CIPHERING:
                            msg = " Meter expects Logical Name referencing with secured connection.";
                            break;
                        case SHORT_NAME_WITH_CIPHERING:
                            msg = " Meter expects Short Name referencing with secured connection.";
                            break;
                        default:
                            break;
                        }
                        throw new GXDLMSException(AssociationResult.PERMANENT_REJECTED,
                                SourceDiagnostic.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED, msg);
                    }
                    ret = name;
                }
                break;
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLED_AP_TITLE: // 0xA2
                // Get length.
                if (buff.getUInt8() != 3) {
                    throw new IllegalArgumentException("Invalid tag.");
                }
                if (settings.isServer()) {
                    // Choice for result (INTEGER, universal)
                    if (buff.getUInt8() != BerType.OCTET_STRING) {
                        throw new IllegalArgumentException("Invalid tag.");
                    }
                    len = buff.getUInt8();
                    tmp = new byte[len];
                    buff.get(tmp);
                    if (xml != null) {
                        // Called AP Title
                        xml.appendLine(TranslatorTags.CALLED_AP_TITLE, "Value", GXCommon.toHex(tmp, false));
                    }
                } else {
                    // Choice for result (INTEGER, universal)
                    if (buff.getUInt8() != BerType.INTEGER) {
                        throw new IllegalArgumentException("Invalid tag.");
                    }
                    // Get length.
                    if (buff.getUInt8() != 1) {
                        throw new IllegalArgumentException("Invalid tag.");
                    }
                    resultComponent = AssociationResult.forValue(buff.getUInt8());
                    if (xml != null) {
                        if (resultComponent != AssociationResult.ACCEPTED) {
                            xml.appendComment(resultComponent.toString());
                        }
                        xml.appendLine(TranslatorGeneralTags.ASSOCIATION_RESULT, "Value",
                                xml.integerToHex(resultComponent.getValue(), 2));
                        xml.appendStartTag(TranslatorGeneralTags.RESULT_SOURCE_DIAGNOSTIC);
                    }
                }
                break;
            // SourceDiagnostic
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLED_AE_QUALIFIER: // 0xA3
                ret = parseSourceDiagnostic(settings, buff, xml);
                break;
            // Result
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLED_AP_INVOCATION_ID: // 0xA4
                parseResult(settings, buff, xml);
                break;
            // Client system title.
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AP_TITLE: // 0xA6
                // len =
                buff.getUInt8();
                // tag =
                buff.getUInt8();
                len = buff.getUInt8();
                tmp = new byte[len];
                buff.get(tmp);
                try {
                    if (tmp.length == 8) {
                        settings.setSourceSystemTitle(tmp);
                    } else {
                        settings.setSourceSystemTitle(null);
                    }
                } catch (Exception ex) {
                    if (xml == null) {
                        throw ex;
                    }
                }
                appendClientSystemTitleToXml(settings, xml);
                break;
            // Server system title.
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.SENDER_ACSE_REQUIREMENTS: // 0xAA
                // len =
                buff.getUInt8();
                tag = buff.getUInt8();
                len = buff.getUInt8();
                tmp = new byte[len];
                buff.get(tmp);
                settings.setStoCChallenge(tmp);
                appendServerSystemTitleToXml(settings, xml, tag);
                break;
            // Client AEInvocationId.
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AE_INVOCATION_ID:// 0xA9
                // len =
                buff.getUInt8();
                // tag =
                buff.getUInt8();
                // len =
                buff.getUInt8();
                settings.setUserId(buff.getUInt8());
                if (xml != null) {
                    // CallingAPTitle
                    xml.appendLine(TranslatorGeneralTags.CALLING_AE_INVOCATION_ID, "Value",
                            xml.integerToHex(settings.getUserId(), 2));
                }
                break;
            // Client CalledAeInvocationId.
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLED_AE_INVOCATION_ID:// 0xA5
                len = GXCommon.getObjectCount(buff);
                tag = buff.getUInt8();
                len = GXCommon.getObjectCount(buff);
                if (tag == BerType.OCTET_STRING) {
                    byte[] tmp2 = new byte[len];
                    buff.get(tmp2);
                    if (xml != null) {
                        xml.appendLine(TranslatorGeneralTags.CALLING_AE_QUALIFIER, "Value",
                                GXCommon.toHex(tmp2, false));
                    }
                    try {
                        // If public key certificate is coming part of AARE.
                        GXx509Certificate cert = new GXx509Certificate(tmp2);
                        settings.setServerPublicKeyCertificate(cert);
                        PrivateKey pk = null;
                        if (cert.getKeyUsage().contains(KeyUsage.KEY_CERT_SIGN)) {
                            if (settings.getCipher().getKeyAgreementKeyPair() != null) {
                                pk = settings.getCipher().getKeyAgreementKeyPair().getPrivate();
                            }
                            settings.getCipher().setKeyAgreementKeyPair(new KeyPair(cert.getPublicKey(), pk));
                        }
                        if (cert.getKeyUsage().contains(KeyUsage.DIGITAL_SIGNATURE)) {
                            if (settings.getCipher().getSigningKeyPair() != null) {
                                pk = settings.getCipher().getSigningKeyPair().getPrivate();
                            }
                            settings.getCipher().setSigningKeyPair(new KeyPair(cert.getPublicKey(), pk));
                        }
                        if (xml != null && xml.isComments()) {
                            xml.appendComment(cert.toString());
                        }
                    } catch (Exception ex) {
                        if (xml == null) {
                            throw ex;
                        }
                        xml.appendLine("Invalid certificate.");
                    }
                } else {
                    settings.setUserId(buff.getUInt8());
                    if (xml != null) {
                        // CallingAPTitle
                        xml.appendLine(TranslatorGeneralTags.CALLED_AE_INVOCATION_ID, "Value",
                                xml.integerToHex(settings.getUserId(), 2));
                    }
                }
                break;
            // Server RespondingAEInvocationId.
            case BerType.CONTEXT | BerType.CONSTRUCTED | 7:// 0xA7
                // len =
                GXCommon.getObjectCount(buff);
                tag = buff.getUInt8();
                len = GXCommon.getObjectCount(buff);
                if (tag == (byte) BerType.OCTET_STRING) {
                    // If public key certificate is coming part of AARQ.
                    byte[] tmp2 = new byte[len];
                    buff.get(tmp2);
                    if (xml != null) {
                        xml.appendLine(TranslatorGeneralTags.CALLING_AE_QUALIFIER, "Value",
                                GXCommon.toHex(tmp2, false));
                    }
                    try {
                        GXx509Certificate cert = new GXx509Certificate(tmp2);
                        settings.setClientPublicKeyCertificate(cert);
                        PrivateKey pk = null;
                        if (cert.getKeyUsage().contains(KeyUsage.KEY_CERT_SIGN)) {
                            if (settings.getCipher().getKeyAgreementKeyPair() != null) {
                                pk = settings.getCipher().getKeyAgreementKeyPair().getPrivate();
                            }
                            settings.getCipher().setKeyAgreementKeyPair(new KeyPair(cert.getPublicKey(), pk));
                        }
                        if (cert.getKeyUsage().contains(KeyUsage.DIGITAL_SIGNATURE)) {
                            if (settings.getCipher().getSigningKeyPair() != null) {
                                pk = settings.getCipher().getSigningKeyPair().getPrivate();
                            }
                            settings.getCipher().setSigningKeyPair(new KeyPair(cert.getPublicKey(), pk));
                        }
                        if (xml != null && xml.isComments()) {
                            xml.appendComment(cert.toString());
                        }
                    } catch (Exception ex) {
                        if (xml == null) {
                            throw ex;
                        }
                        xml.appendLine("Invalid certificate.");
                    }
                } else {
                    settings.setUserId(buff.getUInt8());
                    if (xml != null) {
                        if (settings.isServer()) {
                            xml.appendLine(TranslatorGeneralTags.CALLING_AE_QUALIFIER, "Value",
                                    xml.integerToHex(settings.getUserId(), 2));
                        } else {
                            xml.appendLine(TranslatorGeneralTags.RESPONDING_AE_INVOCATION_ID, "Value",
                                    xml.integerToHex(settings.getUserId(), 2));
                        }
                    }
                }
                break;
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AP_INVOCATION_ID:// 0xA8
                if (buff.getUInt8() != 3) {
                    throw new IllegalArgumentException("Invalid tag.");
                }
                if (buff.getUInt8() != 2) {
                    throw new IllegalArgumentException("Invalid length.");
                }
                if (buff.getUInt8() != 1) {
                    throw new IllegalArgumentException("Invalid tag length.");
                }
                // Get value.
                len = buff.getUInt8();
                if (xml != null) {
                    // CallingApInvocationId
                    xml.appendLine(TranslatorTags.CALLING_AP_INVOCATION_ID, "Value", xml.integerToHex(len, 2));
                }
                break;
            case BerType.CONTEXT | PduType.SENDER_ACSE_REQUIREMENTS: // 0x8A
            case BerType.CONTEXT | PduType.CALLING_AP_INVOCATION_ID: // 0x88
                // Get sender ACSE-requirements field component.
                if (buff.getUInt8() != 2) {
                    throw new IllegalArgumentException("Invalid tag.");
                }
                if (buff.getUInt8() != BerType.OBJECT_DESCRIPTOR) {
                    throw new IllegalArgumentException("Invalid tag.");
                }
                // Get only value because client application is
                // sending system title with LOW authentication.
                buff.getUInt8();
                if (xml != null) {
                    xml.appendLine(tag, "Value", "1");
                }
                break;
            case BerType.CONTEXT | PduType.MECHANISM_NAME: // 0x8B
            case BerType.CONTEXT | PduType.CALLING_AE_INVOCATION_ID: // 0x89
                updateAuthentication(settings, buff);
                if (xml != null) {
                    // CHECKSTYLE:OFF
                    if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                        // CHECKSTYLE:ON
                        xml.appendLine(tag, "Value", settings.getAuthentication().toString());
                    } else {
                        xml.appendLine(tag, "Value", String.valueOf(settings.getAuthentication().ordinal()));
                    }
                }
                break;
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AUTHENTICATION_VALUE: // 0xAC
                updatePassword(settings, buff, xml);
                break;
            case BerType.CONTEXT | BerType.CONSTRUCTED | PduType.USER_INFORMATION:// 0xBE
                try {
                    SourceDiagnostic ret2 = parseUserInformation(settings, cipher, buff, xml);
                    if (ret2 != SourceDiagnostic.NONE) {
                        return ret2.getValue();
                    }
                } catch (GXDLMSExceptionResponse e) {
                    return e.getExceptionServiceError();
                } catch (AEADBadTagException e) {
                    return ExceptionServiceError.DECIPHERING_ERROR;
                } catch (Exception e) {
                    if (xml == null) {
                        // Check result component. Some meters are returning
                        // invalid user-information if connection failed.
                        if (resultComponent != AssociationResult.ACCEPTED && ret instanceof SourceDiagnostic
                                && (SourceDiagnostic) ret != SourceDiagnostic.NONE) {
                            throw new GXDLMSException(resultComponent, (SourceDiagnostic) ret);
                        }
                        if (resultComponent != AssociationResult.ACCEPTED && ret instanceof AcseServiceProvider
                                && (AcseServiceProvider) ret != AcseServiceProvider.NONE) {
                            throw new GXDLMSException(resultComponent, (AcseServiceProvider) ret);
                        }
                        throw new GXDLMSException(AssociationResult.PERMANENT_REJECTED,
                                SourceDiagnostic.NO_REASON_GIVEN);
                    }
                }
                break;
            case BerType.CONTEXT: // 0x80
                AcseServiceProvider tmp2 = parseProtocolVersion(settings, buff, xml);
                if (tmp2 != AcseServiceProvider.NONE) {
                    resultComponent = AssociationResult.PERMANENT_REJECTED;
                }
                ret = tmp2;
                break;
            default:
                // Unknown tags.
                Logger.getLogger(GXAPDU.class.getName()).log(Level.WARNING, "Unknown tag: " + tag + ".");
                if (buff.position() < buff.size()) {
                    len = buff.getUInt8();
                    buff.position(buff.position() + len);
                }
                break;
            }
        }
        // All meters don't send user-information if connection is failed.
        // For this reason result component is check again.
        if (!settings.isServer() && xml == null && resultComponent != AssociationResult.ACCEPTED
                && !(ret instanceof Integer)) {
            if (ret instanceof SourceDiagnostic) {
                throw new GXDLMSException(resultComponent, (SourceDiagnostic) ret);
            } else {
                throw new GXDLMSException(resultComponent, (AcseServiceProvider) ret);
            }
        }
        if (name != null) {
            return name;
        }
        return ret;
    }

    private static void parseResult(final GXDLMSSettings settings, final GXByteBuffer buff,
            final GXDLMSTranslatorStructure xml) {
        byte[] tmp;
        int len;
        if (settings.isServer()) {
            // Get len.
            if (buff.getUInt8() != 3) {
                throw new IllegalArgumentException("Invalid tag.");
            }
            // Choice for result (Universal, Octetstring type)
            if (buff.getUInt8() != BerType.INTEGER) {
                throw new IllegalArgumentException("Invalid tag.");
            }
            if (buff.getUInt8() != 1) {
                throw new IllegalArgumentException("Invalid tag length.");
            }
            // Get value.
            len = buff.getUInt8();
            if (xml != null) {
                // RespondingAPTitle
                xml.appendLine(TranslatorTags.CALLED_AP_INVOCATION_ID, "Value", xml.integerToHex(len, 2));
            }
        } else {
            // Get length.
            if (buff.getUInt8() != 0xA) {
                throw new IllegalArgumentException("Invalid tag.");
            }
            // Choice for result (Universal, Octet string type)
            if (buff.getUInt8() != BerType.OCTET_STRING) {
                throw new IllegalArgumentException("Invalid tag.");
            }
            // responding-AP-title-field
            // Get length.
            len = buff.getUInt8();
            tmp = new byte[len];
            buff.get(tmp);
            settings.setSourceSystemTitle(tmp);
            appendResultToXml(settings, xml);
        }
    }

    private static Object parseSourceDiagnostic(final GXDLMSSettings settings, final GXByteBuffer buff,
            final GXDLMSTranslatorStructure xml) {
        int tag, tag2;
        int len;
        Object ret = 0;
        // len =
        buff.getUInt8();
        // ACSE service user tag.
        tag = buff.getUInt8();
        len = buff.getUInt8();
        tag2 = buff.getUInt8();
        if (tag2 == BerType.OCTET_STRING) {
            byte[] calledAEQualifier = new byte[len];
            buff.get(calledAEQualifier);
            if (xml != null) {
                xml.appendLine(TranslatorTags.CALLED_AE_QUALIFIER, "Value", GXCommon.toHex(calledAEQualifier, false));
            }
        } else {
            // Result source diagnostic component.
            if (tag2 != BerType.INTEGER) {
                throw new IllegalArgumentException("Invalid tag.");
            }
            if (buff.getUInt8() != 1) {
                throw new IllegalArgumentException("Invalid tag.");
            }
            if (tag == 0xA1) {
                ret = SourceDiagnostic.forValue(buff.getUInt8());
                if (xml != null) {
                    if ((SourceDiagnostic) ret != SourceDiagnostic.NONE) {
                        xml.appendComment(ret.toString());
                    }
                    xml.appendLine(TranslatorGeneralTags.ACSE_SERVICE_USER, "Value",
                            xml.integerToHex(((SourceDiagnostic) ret).getValue(), 2));
                }
            } else {
                // ACSEServiceProvicer
                ret = AcseServiceProvider.forValue(buff.getUInt8());
                if (xml != null) {
                    if ((AcseServiceProvider) ret != AcseServiceProvider.NONE) {
                        xml.appendComment(ret.toString());
                    }
                    xml.appendLine(TranslatorGeneralTags.ACSE_SERVICE_PROVIDER, "Value",
                            xml.integerToHex(((AcseServiceProvider) ret).getValue(), 2));
                }
            }
            if (xml != null) {
                xml.appendEndTag(TranslatorGeneralTags.RESULT_SOURCE_DIAGNOSTIC);
            }
        }
        return ret;
    }

    private static void appendServerSystemTitleToXml(final GXDLMSSettings settings, final GXDLMSTranslatorStructure xml,
            final int tag) {
        if (xml != null) {
            // RespondingAuthentication
            if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                xml.appendLine(tag, null, GXCommon.toHex(settings.getStoCChallenge(), false));
            } else {
                xml.append(tag, true);
                xml.append(TranslatorGeneralTags.CHAR_STRING, true);
                xml.append(GXCommon.toHex(settings.getStoCChallenge(), false));
                xml.append(TranslatorGeneralTags.CHAR_STRING, false);
                xml.append(tag, false);
                xml.append("\r\n");
            }
        }
    }

    private static void appendClientSystemTitleToXml(final GXDLMSSettings settings,
            final GXDLMSTranslatorStructure xml) {
        if (xml != null) {
            // CallingAPTitle
            xml.appendLine(TranslatorGeneralTags.CALLING_AP_TITLE, "Value",
                    GXCommon.toHex(settings.getSourceSystemTitle(), false));
        }
    }

    private static void appendResultToXml(final GXDLMSSettings settings, final GXDLMSTranslatorStructure xml) {
        if (xml != null) {
            // RespondingAPTitle
            xml.appendLine(TranslatorGeneralTags.RESPONDING_AP_TITLE, "Value",
                    GXCommon.toHex(settings.getSourceSystemTitle(), false));
        }
    }

    private static void updatePassword(final GXDLMSSettings settings, final GXByteBuffer buff,
            final GXDLMSTranslatorStructure xml) {
        byte[] tmp;
        int len;
        // len =
        buff.getUInt8();
        // Get authentication information.
        if (buff.getUInt8() != 0x80) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        len = buff.getUInt8();
        tmp = new byte[len];
        buff.get(tmp);
        if (settings.getAuthentication() == Authentication.LOW) {
            settings.setPassword(tmp);
        } else {
            settings.setCtoSChallenge(tmp);
        }
        if (xml != null) {
            if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                if (settings.getAuthentication() == Authentication.LOW) {
                    if (GXByteBuffer.isAsciiString(settings.getPassword())) {
                        xml.appendComment(new String(settings.getPassword()));
                    }
                    xml.appendLine(TranslatorGeneralTags.CALLING_AUTHENTICATION, "Value",
                            GXCommon.toHex(settings.getPassword(), false));
                } else {
                    xml.appendLine(TranslatorGeneralTags.CALLING_AUTHENTICATION, "Value",
                            GXCommon.toHex(settings.getCtoSChallenge(), false));
                }
            } else {
                xml.appendStartTag(TranslatorGeneralTags.CALLING_AUTHENTICATION);
                xml.appendStartTag(TranslatorGeneralTags.CHAR_STRING);
                if (settings.getAuthentication() == Authentication.LOW) {
                    xml.append(GXCommon.toHex(settings.getPassword(), false));
                } else {
                    xml.append(GXCommon.toHex(settings.getCtoSChallenge(), false));
                }
                xml.appendEndTag(TranslatorGeneralTags.CHAR_STRING);
                xml.appendEndTag(TranslatorGeneralTags.CALLING_AUTHENTICATION);
            }
        }
    }

    private static void updateAuthentication(final GXDLMSSettings settings, final GXByteBuffer buff) {
        int ch;
        buff.getUInt8();
        if (buff.getUInt8() != 0x60) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x85) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x74) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x05) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x08) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x02) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        ch = buff.getUInt8();
        if (ch < 0 || ch > 7) {
            throw new IllegalArgumentException("Invalid tag.");
        }
        settings.setAuthentication(Authentication.forValue(ch));
    }

    static byte[] getUserInformation(final GXDLMSSettings settings, final GXICipher cipher)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer data = new GXByteBuffer();
        // Tag for xDLMS-Initiate response
        data.setUInt8(Command.INITIATE_RESPONSE);
        // NegotiatedQualityOfService
        if (settings.getQualityOfService() == 0) {
            // Usage field for the response allowed component (not used)
            data.setUInt8(0x00);
        } else {
            data.setUInt8(1);
            data.setUInt8(settings.getQualityOfService());
        }
        // DLMS Version Number
        data.setUInt8(06);
        data.setUInt8(0x5F);
        data.setUInt8(0x1F);
        data.setUInt8(DataType.BITSTRING);
        // encoding the number of unused bits in the bit string
        data.setUInt8(0);
        GXBitString bs = new GXBitString(Conformance.toInteger(settings.getNegotiatedConformance()), 24);
        data.set(bs.getValue());
        data.setUInt16(settings.getMaxPduSize());
        // VAA Name VAA name (0x0007 for LN referencing and 0xFA00 for SN)
        if (settings.getUseLogicalNameReferencing()) {
            data.setUInt16(0x0007);
        } else {
            data.setUInt16(0xFA00);
        }
        if (settings.isCiphered(false)) {
            AesGcmParameter p = new AesGcmParameter(settings, Command.GLO_INITIATE_RESPONSE, cipher.getSecurity(),
                    cipher.getSecuritySuite(), cipher.getInvocationCounter(), cipher.getSystemTitle(),
                    cipher.getBlockCipherKey(), cipher.getAuthenticationKey());
            byte[] tmp = GXCiphering.encrypt(p, data.array());
            cipher.setInvocationCounter(1 + cipher.getInvocationCounter());
            return tmp;
        }
        return data.array();
    }

    /*
     * Server generates AARE message.
     */
    public static void generateAARE(final int name, final GXDLMSSettings settings, final GXByteBuffer data,
            final AssociationResult result, final Object diagnostic, final GXICipher cipher,
            final GXByteBuffer errorData, final GXByteBuffer encryptedData)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        int offset = data.size();
        // Set AARE tag and length 0x61
        data.setUInt8(BerType.APPLICATION | BerType.CONSTRUCTED | PduType.APPLICATION_CONTEXT_NAME);
        generateApplicationContextName(name, settings, data, cipher);
        // Result 0xA2
        data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | BerType.INTEGER);
        data.setUInt8(3); // len
        data.setUInt8(BerType.INTEGER); // Tag
        // Choice for result (INTEGER, universal)
        data.setUInt8(1); // Len
        data.setUInt8(result.getValue()); // ResultValue
        // SourceDiagnostic
        data.setUInt8(0xA3);
        data.setUInt8(5); // len
        // Tag
        if (diagnostic instanceof SourceDiagnostic) {
            data.setUInt8(0xA1);
        } else {
            data.setUInt8(0xA2);
        }
        data.setUInt8(3); // len
        data.setUInt8(2); // Tag
        // Choice for result (INTEGER, universal)
        data.setUInt8(1); // Len
        // diagnostic
        if (diagnostic instanceof SourceDiagnostic) {
            data.setUInt8(((SourceDiagnostic) diagnostic).getValue());
        } else if (diagnostic instanceof AcseServiceProvider) {
            data.setUInt8(((AcseServiceProvider) diagnostic).getValue());
        } else {
            data.setUInt8(((int) diagnostic));
        }
        // SystemTitle
        if (settings.isCiphered(false) || settings.getAuthentication() == Authentication.HIGH_GMAC
                || settings.getAuthentication() == Authentication.HIGH_SHA256
                || settings.getAuthentication() == Authentication.HIGH_ECDSA) {
            data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLED_AP_INVOCATION_ID);
            data.setUInt8((2 + cipher.getSystemTitle().length));
            data.setUInt8(BerType.OCTET_STRING);
            data.setUInt8(cipher.getSystemTitle().length);
            data.set(cipher.getSystemTitle());
        }
        // Add CalledAEInvocationId.
        if (settings.getAuthentication() == Authentication.HIGH_ECDSA
                && settings.getServerPublicKeyCertificate() != null) {
            byte[] raw = settings.getServerPublicKeyCertificate().getEncoded();
            data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLING_AE_QUALIFIER);
            GXCommon.setObjectCount(4 + raw.length, data);
            data.setUInt8(BerType.OCTET_STRING);
            GXCommon.setObjectCount(raw.length, data);
            data.set(raw);
        } else if (settings.getUserId() != -1) {
            data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.CALLED_AE_INVOCATION_ID);
            // LEN
            data.setUInt8(3);
            data.setUInt8(BerType.INTEGER);
            // LEN
            data.setUInt8(1);
            data.setUInt8(settings.getUserId());
        }

        if (settings.getAuthentication().getValue() > Authentication.LOW.getValue()) {
            // Add server ACSE-requirenents field component.
            data.setUInt8(0x88);
            data.setUInt8(0x02); // Len.
            data.setUInt16(0x0780);
            // Add tag.
            data.setUInt8(0x89);
            data.setUInt8(0x07); // Len
            data.setUInt8(0x60);
            data.setUInt8(0x85);
            data.setUInt8(0x74);
            data.setUInt8(0x05);
            data.setUInt8(0x08);
            data.setUInt8(0x02);
            data.setUInt8(settings.getAuthentication().getValue());
            // Add tag.
            data.setUInt8(0xAA);
            data.setUInt8((2 + settings.getStoCChallenge().length)); // Len
            data.setUInt8(BerType.CONTEXT);
            data.setUInt8(settings.getStoCChallenge().length);
            data.set(settings.getStoCChallenge());
        }
        if (result == AssociationResult.ACCEPTED || cipher == null || cipher.getSecurity() == Security.NONE) {
            byte[] tmp;
            // Add User Information
            // Tag 0xBE
            data.setUInt8(BerType.CONTEXT | BerType.CONSTRUCTED | PduType.USER_INFORMATION);
            if (encryptedData != null && encryptedData.size() != 0) {
                GXByteBuffer tmp2 = new GXByteBuffer(2 + encryptedData.size());
                tmp2.setUInt8(Command.GLO_INITIATE_RESPONSE);
                GXCommon.setObjectCount(encryptedData.size(), tmp2);
                tmp2.set(encryptedData);
                tmp = tmp2.array();
            } else {
                if (errorData != null && errorData.size() != 0) {
                    tmp = errorData.array();
                } else {
                    tmp = getUserInformation(settings, cipher);
                }
            }
            data.setUInt8((2 + tmp.length));
            // Coding the choice for user-information (Octet STRING, universal)
            data.setUInt8(BerType.OCTET_STRING);
            // Length
            data.setUInt8(tmp.length);
            data.set(tmp);
        }
        // Set AARE length
        GXCommon.insertObjectCount(data.size() - offset - 1, data, offset + 1);
    }
}