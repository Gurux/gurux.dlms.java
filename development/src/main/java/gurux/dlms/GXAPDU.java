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

import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.internal.GXCommon;

/**
 * The services to access the attributes and methods of COSEM objects are
 * determined on DLMS/COSEM Application layer. The services are carried by
 * Application Protocol Data Units (APDUs).
 * <p />
 * In DLMS/COSEM the meter is primarily a server, and the controlling system is
 * a client. Also unsolicited (received without a request) messages are
 * available.
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
    private static void getAuthenticationString(final GXDLMSSettings settings,
            final GXByteBuffer data) {
        // If authentication is used.
        if (settings.getAuthentication() != Authentication.NONE) {
            // Add sender ACSE-requirements field component.
            data.setUInt8(BerType.CONTEXT.getValue()
                    | PduType.SenderAcseRequirements.getValue());
            data.setUInt8(2);
            data.setUInt8(BerType.BIT_STRING.getValue()
                    | BerType.OCTET_STRING.getValue());
            data.setUInt8(0x80);

            data.setUInt8(BerType.CONTEXT.getValue()
                    | PduType.MECHANISMNAME.getValue());
            // Len
            data.setUInt8(7);
            // OBJECT IDENTIFIER
            byte[] p = { (byte) 0x60, (byte) 0x85, (byte) 0x74, 0x05, 0x08,
                    0x02, (byte) settings.getAuthentication().getValue() };
            data.set(p);
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
            data.setUInt8(
                    BerType.CONTEXT.getValue() | BerType.CONSTRUCTED.getValue()
                            | PduType.CallingAuthenticationValue.getValue());
            // Len
            data.setUInt8((2 + len));
            // Add authentication information.
            data.setUInt8(BerType.CONTEXT.getValue());
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
    private static void generateApplicationContextName(
            final GXDLMSSettings settings, final GXByteBuffer data,
            final GXICipher cipher) {
        // Application context name tag
        data.setUInt8(
                (BerType.CONTEXT.getValue() | BerType.CONSTRUCTED.getValue()
                        | PduType.ApplicationContextName.getValue()));
        // Len
        data.setUInt8(0x09);
        data.setUInt8(BerType.OBJECT_IDENTIFIER.getValue());
        // Len
        data.setUInt8(0x07);
        boolean ciphered = cipher != null && cipher.isCiphered();
        if (settings.getUseLogicalNameReferencing()) {
            if (ciphered) {
                data.set(GXCommon.LOGICAL_NAME_OBJECT_ID_WITH_CIPHERING);
            } else {
                data.set(GXCommon.LOGICAL_NAME_OBJECT_ID);
            }
        } else {
            if (ciphered) {
                data.set(GXCommon.SHORT_NAME_OBJECT_ID_WITH_CIPHERING);
            } else {
                data.set(GXCommon.SHORT_NAME_OBJECT_ID);
            }
        }
        // Add system title.
        if (!settings.isServer() && (ciphered
                || settings.getAuthentication() == Authentication.HIGH_GMAC)) {
            if (cipher.getSystemTitle() == null
                    || cipher.getSystemTitle().length == 0) {
                throw new IllegalArgumentException("SystemTitle");
            }
            // Add calling-AP-title
            data.setUInt8((BerType.CONTEXT.getValue()
                    | BerType.CONSTRUCTED.getValue() | 6));
            // LEN
            data.setUInt8((2 + cipher.getSystemTitle().length));
            data.setUInt8(BerType.OCTET_STRING.getValue());
            // LEN
            data.setUInt8(cipher.getSystemTitle().length);
            data.set(cipher.getSystemTitle());
        }
    }

    /**
     * Generate User information initiate request.
     * 
     * @param settings
     *            DLMS settings.
     * @param cipher
     * @param data
     */
    private static void getInitiateRequest(final GXDLMSSettings settings,
            final GXICipher cipher, final GXByteBuffer data) {
        // Tag for xDLMS-Initiate request
        data.setUInt8(GXCommon.INITIAL_REQUEST);
        // Usage field for the response allowed component.

        // Usage field for dedicated-key component. Not used
        data.setUInt8(0x00);

        // encoding of the response-allowed component (BOOLEAN DEFAULT TRUE)
        // usage flag (FALSE, default value TRUE conveyed)
        data.setUInt8(0);

        // Usage field of the proposed-quality-of-service component. Not used
        data.setUInt8(0x00);
        data.setUInt8(settings.getDLMSVersion());
        // Tag for conformance block
        data.setUInt8(0x5F);
        data.setUInt8(0x1F);
        // length of the conformance block
        data.setUInt8(0x04);
        // encoding the number of unused bits in the bit string
        data.setUInt8(0x00);
        if (settings.getUseLogicalNameReferencing()) {
            data.set(settings.getLnSettings().getConformanceBlock());
        } else {
            data.set(settings.getSnSettings().getConformanceBlock());
        }
        data.setUInt16(settings.getMaxReceivePDUSize());
    }

    /**
     * Generate user information.
     * 
     * @param settings
     *            DLMS settings.
     * @param cipher
     * @param data
     *            Generated user information.
     */
    private static void generateUserInformation(final GXDLMSSettings settings,
            final GXICipher cipher, final GXByteBuffer data) {
        data.setUInt8(
                BerType.CONTEXT.getValue() | BerType.CONSTRUCTED.getValue()
                        | PduType.UserInformation.getValue());
        if (cipher == null || !cipher.isCiphered()) {
            // Length for AARQ user field
            data.setUInt8(0x10);
            // Coding the choice for user-information (Octet STRING, universal)
            data.setUInt8(BerType.OCTET_STRING.getValue());
            // Length
            data.setUInt8(0x0E);
            getInitiateRequest(settings, cipher, data);
        } else {
            GXByteBuffer tmp = new GXByteBuffer();
            getInitiateRequest(settings, cipher, tmp);
            byte[] crypted = cipher.encrypt((byte) 0x21,
                    cipher.getSystemTitle(), tmp.array());
            // Length for AARQ user field
            data.setUInt8((2 + crypted.length));
            // Coding the choice for user-information (Octet string, universal)
            data.setUInt8(BerType.OCTET_STRING.getValue());
            data.setUInt8(crypted.length);
            data.set(crypted);
        }
    }

    /**
     * Generates Aarq.
     */
    public static void generateAarq(final GXDLMSSettings settings,
            final GXICipher cipher, final GXByteBuffer data) {
        // AARQ APDU Tag
        data.setUInt8(BerType.APPLICATION.getValue()
                | BerType.CONSTRUCTED.getValue());
        // Length is updated later.
        int offset = data.size();
        data.setUInt8(0);
        ///////////////////////////////////////////
        // Add Application context name.
        generateApplicationContextName(settings, data, cipher);
        getAuthenticationString(settings, data);
        generateUserInformation(settings, cipher, data);
        data.setUInt8(offset, (data.size() - offset - 1));
    }

    /**
     * Parse User Information from PDU.
     */
    private static void parseUserInformation(final GXDLMSSettings settings,
            final GXICipher cipher, final GXByteBuffer data) {
        int len = data.getUInt8();
        if (data.size() - data.position() < len) {
            throw new RuntimeException("Not enough data.");
        }
        // Encoding the choice for user information
        int tag = data.getUInt8();
        if (tag != 0x4) {
            throw new RuntimeException("Invalid tag.");
        }
        len = data.getUInt8();
        // Tag for xDLMS-Initate.response
        tag = data.getUInt8();
        if (tag == GXCommon.INITIAL_RESPONSE_GLO) {
            data.position(data.position() - 1);
            cipher.setSecurity(
                    cipher.decrypt(settings.getSourceSystemTitle(), data));
            tag = data.getUInt8();
        } else if (tag == GXCommon.INITIAL_REQUEST_GLO) {
            data.position(data.position() - 1);
            // InitiateRequest
            cipher.setSecurity(
                    cipher.decrypt(settings.getSourceSystemTitle(), data));
            tag = data.getUInt8();
        }
        boolean response = tag == GXCommon.INITIAL_RESPONSE;
        if (response) {
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            if (tag != 0) {
                len = data.getUInt8();
                data.position(data.position() + len);
            }
        } else if (tag == GXCommon.INITIAL_REQUEST) {
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            // CtoS.
            if (tag != 0) {
                len = data.getUInt8();
                byte[] tmp = new byte[len];
                data.get(tmp);
                settings.setCtoSChallenge(tmp);
            }
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            // Skip if used.
            if (tag != 0) {
                len = data.getUInt8();
                data.position(data.position() + len);
            }
            // Optional usage field of the proposed quality of service component
            tag = data.getUInt8();
            // Skip if used.
            if (tag != 0) {
                len = data.getUInt8();
                data.position(data.position() + len);
            }
        } else {
            throw new RuntimeException("Invalid tag.");
        }
        // Get DLMS version number.
        if (settings.isServer()) {
            settings.setDLMSVersion((byte) data.getUInt8());
        } else {
            if (data.getUInt8() != 6) {
                throw new RuntimeException("Invalid DLMS version number.");
            }
        }

        // Tag for conformance block
        tag = data.getUInt8();
        if (tag != 0x5F) {
            throw new RuntimeException("Invalid tag.");
        }
        // Old Way...
        if (data.getUInt8(data.position()) == 0x1F) {
            data.getUInt8();
        }
        len = data.getUInt8();
        // The number of unused bits in the bit string.
        tag = data.getUInt8();
        if (settings.getUseLogicalNameReferencing()) {
            if (settings.isServer()) {
                // Skip settings what client asks.
                // All server settings are always returned.
                byte[] tmp = new byte[3];
                data.get(tmp);
            } else {
                data.get(settings.getLnSettings().getConformanceBlock());
            }
        } else {
            if (settings.isServer()) {
                // Skip settings what client asks.
                // All server settings are always returned.
                byte[] tmp = new byte[3];
                data.get(tmp);
            } else {
                data.get(settings.getSnSettings().getConformanceBlock());
            }
        }
        if (settings.isServer()) {
            data.getUInt16();
        } else {
            settings.setMaxReceivePDUSize(data.getUInt16());
        }
        if (response) {
            // VAA Name
            tag = data.getUInt16();
            if (tag == 0x0007) {
                // If LN
                if (!settings.getUseLogicalNameReferencing()) {
                    throw new IllegalArgumentException("Invalid VAA.");
                }
            } else if (tag == 0xFA00) {
                // If SN
                if (settings.getUseLogicalNameReferencing()) {
                    throw new IllegalArgumentException("Invalid VAA.");
                }
            } else {
                // Unknown VAA.
                throw new IllegalArgumentException("Invalid VAA.");
            }
        }
    }

    /**
     * Parse application context name.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     */
    private static boolean parseApplicationContextName(
            final GXDLMSSettings settings, final GXByteBuffer buff) {
        // Get length.
        int len = buff.getUInt8();
        if (buff.size() - buff.position() < len) {
            throw new RuntimeException("Encoding failed. Not enough data.");
        }
        if (buff.getUInt8() != 0x6) {
            throw new RuntimeException("Encoding failed. Not an Object ID.");
        }
        if (settings.isServer() && settings.getCipher() != null) {
            settings.getCipher().setSecurity(Security.NONE);
        }
        // Object ID length.
        len = buff.getUInt8();
        if (settings.getUseLogicalNameReferencing()) {
            if (buff.compare(GXCommon.LOGICAL_NAME_OBJECT_ID)) {
                return true;
            }
            // If ciphering is used.
            return buff.compare(GXCommon.LOGICAL_NAME_OBJECT_ID_WITH_CIPHERING);
        }
        if (buff.compare(GXCommon.SHORT_NAME_OBJECT_ID)) {
            return true;
        }
        // If ciphering is used.
        return buff.compare(GXCommon.SHORT_NAME_OBJECT_ID_WITH_CIPHERING);
    }

    private static void validateAare(final GXDLMSSettings settings,
            final GXByteBuffer buff) {
        int tag = buff.getUInt8();
        if (settings.isServer()) {
            if (tag != (BerType.APPLICATION.getValue()
                    | BerType.CONSTRUCTED.getValue()
                    | PduType.ProtocolVersion.getValue())) {
                throw new RuntimeException("Invalid tag.");
            }
        } else {
            if (tag != (BerType.APPLICATION.getValue()
                    | BerType.CONSTRUCTED.getValue()
                    | PduType.ApplicationContextName.getValue())) {
                throw new RuntimeException("Invalid tag.");
            }
        }
    }

    /**
     * Parse APDU.
     */
    public static SourceDiagnostic parsePDU(final GXDLMSSettings settings,
            final GXICipher cipher, final GXByteBuffer buff) {
        // Get AARE tag and length
        validateAare(settings, buff);
        byte[] tmp;
        int tag;
        int len = buff.getUInt8();
        int size = buff.size() - buff.position();
        if (len > size) {
            throw new RuntimeException("Not enough data.");
        }
        AssociationResult resultComponent = AssociationResult.ACCEPTED;
        SourceDiagnostic resultDiagnosticValue = SourceDiagnostic.NONE;
        while (buff.position() < buff.size()) {
            tag = buff.getUInt8();
            switch (tag) {
            // BerType.CONTEXT | BerType.CONSTRUCTED |
            // PduType.APPLICATIONCONTEXTNAME
            case 0xA1:
                if (!parseApplicationContextName(settings, buff)) {
                    throw new GXDLMSException(
                            AssociationResult.PERMANENT_REJECTED,
                            SourceDiagnostic.NOT_SUPPORTED);
                }
                break;
            // Result BerType.CONTEXT | BerType.CONSTRUCTED |
            // PduType.CALLEDAPTITLE
            case 0xA2:
                // Get len.
                if (buff.getUInt8() != 3) {
                    throw new RuntimeException("Invalid tag.");
                }
                // Choice for result (INTEGER, universal)
                if (buff.getUInt8() != BerType.INTEGER.getValue()) {
                    throw new RuntimeException("Invalid tag.");
                }
                // Get len.
                if (buff.getUInt8() != 1) {
                    throw new RuntimeException("Invalid tag.");
                }
                resultComponent = AssociationResult.forValue(buff.getUInt8());
                break;
            // SourceDiagnostic BerType.CONTEXT | BerType.CONSTRUCTED |
            // PduType.CALLEDAEQUALIFIER
            case 0xA3:
                len = buff.getUInt8();
                // ACSE service user tag.
                tag = buff.getUInt8();
                len = buff.getUInt8();
                // Result source diagnostic component.
                if (buff.getUInt8() != BerType.INTEGER.getValue()) {
                    throw new RuntimeException("Invalid tag.");
                }
                if (buff.getUInt8() != 1) {
                    throw new RuntimeException("Invalid tag.");
                }
                resultDiagnosticValue =
                        SourceDiagnostic.forValue(buff.getUInt8());
                break;
            // Result BerType.CONTEXT | BerType.CONSTRUCTED |
            // PduType.CalledApInvocationId
            case 0xA4:
                // Get len.
                if (buff.getUInt8() != 0xA) {
                    throw new RuntimeException("Invalid tag.");
                }
                // Choice for result (Universal, Octet string type)
                if (buff.getUInt8() != BerType.OCTET_STRING.getValue()) {
                    throw new RuntimeException("Invalid tag.");
                }
                // responding-AP-title-field
                // Get len.
                len = buff.getUInt8();
                tmp = new byte[len];
                buff.get(tmp);
                settings.setSourceSystemTitle(tmp);
                break;
            // Client system title. BerType.CONTEXT | BerType.CONSTRUCTED |
            // PduType.CALLINGAPTITLE
            case 0xA6:
                len = buff.getUInt8();
                tag = buff.getUInt8();
                len = buff.getUInt8();
                tmp = new byte[len];
                buff.get(tmp);
                settings.setSourceSystemTitle(tmp);
                break;
            // Server system title. BerType.CONTEXT| BerType.CONSTRUCTED |
            // PduType.SENDERACSEREQUIREMENTS
            case 0xAA:
                len = buff.getUInt8();
                tag = buff.getUInt8();
                len = buff.getUInt8();
                tmp = new byte[len];
                buff.get(tmp);
                settings.setStoCChallenge(tmp);
                break;
            // BerType.CONTEXT | PduType.SENDERACSEREQUIREMENTS or
            // BerType.CONTEXT | PduType.CALLINGAPINVOCATIONID
            case 0x8A:
            case 0x88:
                // Get sender ACSE-requirements field component.
                if (buff.getUInt8() != 2) {
                    throw new RuntimeException("Invalid tag.");
                }
                if (buff.getUInt8() != BerType.OBJECT_DESCRIPTOR.getValue()) {
                    throw new RuntimeException("Invalid tag.");
                }
                //Get only value because client app is sending system title with LOW authentication.
                buff.getUInt8();
                break;
            // BerType.CONTEXT | PduType.MECHANISMNAME or
            case 0x8B:
                // BerType.CONTEXT | PduType.CALLINGAEINVOCATIONID
            case 0x89:
                updateAuthentication(settings, buff);
                break;
            // BerType.CONTEXT | BerType.CONSTRUCTED |
            // PduType.CALLINGAUTHENTICATIONVALUE
            case 0xAC:
                updatePassword(settings, buff);
                break;
            // BerType.CONTEXT | BerType.CONSTRUCTED | PduType.USERINFORMATION
            case 0xBE:
                if (resultComponent != AssociationResult.ACCEPTED
                        && resultDiagnosticValue != SourceDiagnostic.NONE) {
                    throw new GXDLMSException(resultComponent,
                            resultDiagnosticValue);
                }
                parseUserInformation(settings, cipher, buff);
                break;
            default:
                // Unknown tags.
                System.out.println("Unknown tag: " + tag + ".");
                len = buff.getUInt8();
                buff.position(buff.position() + len);
                break;
            }
        }
        return resultDiagnosticValue;
    }

    private static void updatePassword(final GXDLMSSettings settings,
            final GXByteBuffer buff) {
        byte[] tmp;
        int len;
        len = buff.getUInt8();
        // Get authentication information.
        if (buff.getUInt8() != 0x80) {
            throw new RuntimeException("Invalid tag.");
        }
        len = buff.getUInt8();
        tmp = new byte[len];
        buff.get(tmp);
        if (settings.getAuthentication() == Authentication.LOW) {
            settings.setPassword(tmp);
        } else {
            settings.setCtoSChallenge(tmp);
        }
    }

    private static void updateAuthentication(final GXDLMSSettings settings,
            final GXByteBuffer buff) {
        int ch = buff.getUInt8();
        if (buff.getUInt8() != 0x60) {
            throw new RuntimeException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x85) {
            throw new RuntimeException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x74) {
            throw new RuntimeException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x05) {
            throw new RuntimeException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x08) {
            throw new RuntimeException("Invalid tag.");
        }
        if (buff.getUInt8() != 0x02) {
            throw new RuntimeException("Invalid tag.");
        }
        ch = buff.getUInt8();
        if (ch < 0 || ch > 5) {
            throw new RuntimeException("Invalid tag.");
        }
        settings.setAuthentication(Authentication.forValue(ch));
    }

    private static byte[] getUserInformation(final GXDLMSSettings settings,
            final GXICipher cipher) {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(GXCommon.INITIAL_RESPONSE); // Tag for xDLMS-Initiate
                                                  // response
        data.setUInt8(0x01);
        data.setUInt8(0x00); // Usage field for the response allowed component
                             // (not used)
        // DLMS Version Number
        data.setUInt8(06);
        data.setUInt8(0x5F);
        data.setUInt8(0x1F);
        data.setUInt8(0x04); // length of the conformance block
        data.setUInt8(0x00); // encoding the number of unused bits in the bit
                             // string
        if (settings.getUseLogicalNameReferencing()) {
            data.set(settings.getLnSettings().getConformanceBlock());
        } else {
            data.set(settings.getSnSettings().getConformanceBlock());

        }
        data.setUInt16(settings.getMaxReceivePDUSize());
        // VAA Name VAA name (0x0007 for LN referencing and 0xFA00 for SN)
        if (settings.getUseLogicalNameReferencing()) {
            data.setUInt16(0x0007);
        } else {
            data.setUInt16(0xFA00);
        }
        if (cipher != null && cipher.isCiphered()) {
            return cipher.encrypt(0x28, cipher.getSystemTitle(), data.array());
        }
        return data.array();
    }

    /**
     * Server generates AARE message.
     */
    public static void generateAARE(final GXDLMSSettings settings,
            final GXByteBuffer data, final AssociationResult result,
            final SourceDiagnostic diagnostic, final GXICipher cipher) {
        int offset = data.position();
        // Set AARE tag and length 0x61
        data.setUInt8(
                BerType.APPLICATION.getValue() | BerType.CONSTRUCTED.getValue()
                        | PduType.ApplicationContextName.getValue());
        // Length is updated later.
        data.setUInt8(0);
        generateApplicationContextName(settings, data, cipher);
        // Result 0xA2
        data.setUInt8(BerType.CONTEXT.getValue()
                | BerType.CONSTRUCTED.getValue() | BerType.INTEGER.getValue());
        data.setUInt8(3); // len
        data.setUInt8(BerType.INTEGER.getValue()); // Tag
        // Choice for result (INTEGER, universal)
        data.setUInt8(1); // Len
        data.setUInt8(result.getValue()); // ResultValue
        // SourceDiagnostic
        data.setUInt8(0xA3);
        data.setUInt8(5); // len
        data.setUInt8(0xA1); // Tag
        data.setUInt8(3); // len
        data.setUInt8(2); // Tag
        // Choice for result (INTEGER, universal)
        data.setUInt8(1); // Len
        // diagnostic
        data.setUInt8(diagnostic.getValue());
        // SystemTitle
        if (cipher != null
                && (settings.getAuthentication() == Authentication.HIGH_GMAC
                        || cipher.isCiphered())) {
            data.setUInt8(
                    BerType.CONTEXT.getValue() | BerType.CONSTRUCTED.getValue()
                            | PduType.CalledApInvocationId.getValue());
            data.setUInt8((2 + cipher.getSystemTitle().length));
            data.setUInt8(BerType.OCTET_STRING.getValue());
            data.setUInt8(cipher.getSystemTitle().length);
            data.set(cipher.getSystemTitle());
        }

        if (result != AssociationResult.PERMANENT_REJECTED
                && diagnostic == SourceDiagnostic.AUTHENTICATION_REQUIRED) {
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
            data.setUInt8(BerType.CONTEXT.getValue());
            data.setUInt8(settings.getStoCChallenge().length);
            data.set(settings.getStoCChallenge());
        }
        // Add User Information
        // Tag 0xBE
        data.setUInt8(
                BerType.CONTEXT.getValue() | BerType.CONSTRUCTED.getValue()
                        | PduType.UserInformation.getValue());
        byte[] tmp = getUserInformation(settings, cipher);
        data.setUInt8((2 + tmp.length));
        // Coding the choice for user-information (Octet STRING, universal)
        data.setUInt8(BerType.OCTET_STRING.getValue());
        // Length
        data.setUInt8(tmp.length);
        data.set(tmp);
        data.setUInt8((short) (offset + 1), (data.size() - offset - 2));
    }
}