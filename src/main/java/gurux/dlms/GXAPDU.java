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
class GXAPDU {
    private int resultValue;
    private SourceDiagnostic resultDiagnostic;

    /**
     * AssociationResult
     * 
     * @return
     */
    AssociationResult getResultComponent() {
        return AssociationResult.forValue(resultValue);
    }

    /**
     * SourceDiagnostic
     * 
     * @return
     * @throws Exception
     */
    SourceDiagnostic getResultDiagnostic() {
        return resultDiagnostic;
    }

    void setResultDiagnosticValue(final SourceDiagnostic value) {
        resultDiagnostic = value;
    }

    /**
     * Retrieves the string that indicates the level of authentication, if any.
     * 
     * @param data
     */
    static void getAuthenticationString(final GXDLMSSettings settings,
            final GXByteBuffer data, final byte[] challenge) {
        // If authentication is used.
        if (settings.getAuthentication() != Authentication.NONE) {
            // Add sender ACSE-requirements field component.
            data.setUInt8(0x8A);
            data.setUInt8(2);
            data.setUInt16(0x0780);
            data.setUInt8(0x8B);
            data.setUInt8(7);
            byte[] p = { (byte) 0x60, (byte) 0x85, (byte) 0x74, (byte) 0x05,
                    (byte) 0x08, (byte) 0x02,
                    (byte) settings.getAuthentication().ordinal() };
            data.set(p);
            // Add Calling authentication information.
            int len = 0;
            if (settings.getPassword() != null) {
                len = settings.getPassword().length;
            }
            if (settings.getAuthentication() == Authentication.LOW) {
                if (settings.getPassword() != null) {
                    len = settings.getPassword().length;
                }
            } else {
                len = challenge.length;
            }
            data.setUInt8(0xAC);
            data.setUInt8((2 + len));
            // Add authentication information.
            data.setUInt8(0x80);
            data.setUInt8(len);
            if (challenge != null) {
                data.set(challenge);
            } else if (settings.getPassword() != null) {
                data.set(settings.getPassword());
            }
        }
    }

    /**
     * codeData
     * 
     * @param data
     * @param interfaceType
     */
    void codeData(final GXDLMSSettings settings, final boolean ciphering,
            final GXByteBuffer data) {
        // AARQ APDU Tag
        data.setUInt8(GXCommon.AARQ_TAG);
        // Length
        int lenPos = data.size();
        data.setUInt8(0);
        ///////////////////////////////////////////
        // Add Application context name.
        GXApplicationContextName.codeData(settings, data, ciphering);
        getAuthenticationString(settings, data, settings.getCtoSChallenge());
        GXUserInformation.codeData(settings, data);
        data.setUInt8(lenPos, data.size() - lenPos - 1);
    }

    /**
     * EncodeData
     * 
     * @param buff
     * @throws Exception
     */
    boolean encodeData(final GXDLMSSettings settings, final GXByteBuffer buff) {
        // Get AARE tag and length
        int tag = buff.getUInt8();
        if (tag != GXCommon.AARE_TAG && tag != GXCommon.AARQ_TAG && tag != 0x81
                && tag != 0x80) {
            throw new GXDLMSException("Invalid tag.");
        }
        int len = buff.getUInt8();
        int size = buff.size() - buff.position();
        if (len > size) {
            throw new GXDLMSException("Not enought data.");
        }
        while (buff.position() < buff.size()) {
            tag = buff.getUInt8(buff.position());
            if (tag == 0xA1) {
                if (!handleApplicationContextName(settings, buff)) {
                    return false;
                }
            } else if (tag == 0xBE) {
                GXUserInformation.encodeData(settings, buff);
            } else if (tag == 0xA2) {
                // Result
                // Get tag
                buff.getUInt8();
                // Get length.
                buff.getUInt8();
                // Choice for result (INTEGER, universal)
                // Get tag
                buff.getUInt8();
                // Get length.
                buff.getUInt8();
                resultValue = buff.getUInt8();
            } else if (tag == 0xA3) {
                // SourceDiagnostic
                // Get tag
                buff.getUInt8();
                // Get length.
                buff.getUInt8();
                // ACSE service user tag.
                // Get tag
                buff.getUInt8();
                // Get length.
                buff.getUInt8();
                // Result source diagnostic component.
                // Get tag
                buff.getUInt8();
                // Get length.
                buff.getUInt8();
                resultDiagnostic = SourceDiagnostic.forValue(buff.getUInt8());
            } else if (tag == 0x8A || tag == 0x88) {
                // Authentication.
                tag = buff.getUInt8();
                // Get sender ACSE-requirenents field component.
                if (buff.getUInt8() != 2) {
                    throw new RuntimeException("Invalid tag.");
                }
                int val = (buff.getUInt16() & 0xFFFF);
                if (val != 0x0780 && val != 0x0680) {
                    throw new RuntimeException("Invalid tag.");
                }
            } else if (tag == 0xAA) {
                // Server Challenge.
                tag = buff.getUInt8();
                len = buff.getUInt8();
                buff.getUInt8();
                len = buff.getUInt8();
                byte[] challenge = new byte[len];
                buff.get(challenge);
                settings.setCtoSChallenge(challenge);
            } else if (tag == 0x8B || tag == 0x89) {
                // Authentication.
                tag = buff.getUInt8();
                len = buff.getUInt8();
                if (buff.getUInt8() != 0x60) {
                    throw new RuntimeException("Invalid tag.");
                }
                if ((buff.getUInt8() & 0xFF) != 0x85) {
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
                int tmp = buff.getUInt8();
                if (tmp < 0 || tmp > 5) {
                    throw new RuntimeException("Invalid tag.");
                }
                settings.setAuthentication(Authentication.forValue(tmp));
                if (tmp != 0) {
                    int tag2 = (buff.getUInt8() & 0xFF);
                    if (tag2 != 0xAC && tag2 != 0xAA) {
                        throw new RuntimeException("Invalid tag.");
                    }
                    len = buff.getUInt8();
                    // Get authentication information.
                    if ((buff.getUInt8() & 0xFF) != 0x80) {
                        throw new RuntimeException("Invalid tag.");
                    }
                    len = buff.getUInt8() & 0xFF;
                    byte[] tmp2 = new byte[len];
                    buff.get(tmp2);
                    if (tmp < 2) {
                        settings.setPassword(tmp2);
                    } else {
                        if (settings.isServer()) {
                            settings.setCtoSChallenge(tmp2);
                        } else {
                            settings.setStoCChallenge(tmp2);
                        }
                    }
                }
            } else {
                // Unknown tags.
                tag = buff.getUInt8();
                len = buff.getUInt8();
                buff.position(buff.position() + len);
            }
        }
        return true;
    }

    private boolean handleApplicationContextName(final GXDLMSSettings settings,
            final GXByteBuffer buff) {
        if (!GXApplicationContextName.encodeData(settings, buff)) {
            resultValue = AssociationResult.PERMANENT_REJECTED.getValue();
            resultDiagnostic = SourceDiagnostic.NOT_SUPPORTED;
            return false;
        }
        return true;
    }

    /**
     * Server generates AARE message.
     */
    public final void generateAare(final GXDLMSSettings settings,
            final GXByteBuffer data, final AssociationResult result,
            final SourceDiagnostic diagnostic, final boolean ciphering) {
        int offset = data.position();
        // Set AARE tag and length
        data.setUInt8(GXCommon.AARE_TAG);
        data.setUInt8(0); // Length is updated later.
        GXApplicationContextName.codeData(settings, data, ciphering);
        // Result
        data.setUInt8(0xA2);
        data.setUInt8(3); // len
        data.setUInt8(2); // Tag
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
        data.setUInt8(diagnostic.getValue()); // diagnostic
        if (result != AssociationResult.PERMANENT_REJECTED
                && diagnostic == SourceDiagnostic.AUTHENTICATION_REQUIRED) {
            // Add server ACSE-requirenents field component.
            data.setUInt8(0x88);
            // Length.
            data.setUInt8(0x02);
            data.setUInt16(0x0780);
            // Add tag.
            data.setUInt8(0x89);
            // Length.
            data.setUInt8(0x07);
            data.setUInt8(0x60);
            data.setUInt8(0x85);
            data.setUInt8(0x74);
            data.setUInt8(0x05);
            data.setUInt8(0x08);
            data.setUInt8(0x02);
            data.setUInt8(settings.getAuthentication().getValue());
            // Add tag.
            data.setUInt8(0xAA);
            // Tag length.
            data.setUInt8((2 + settings.getStoCChallenge().length));
            data.setUInt8(0x80);
            // Challenge Length.
            data.setUInt8(settings.getStoCChallenge().length);
            data.set(settings.getStoCChallenge());
        }
        // Add User Information Tag
        data.setUInt8(0xBE);
        // Length for AARQ user field
        data.setUInt8(0x10);
        // Coding the choice for user-information (Octet STRING, universal)
        data.setUInt8(0x04);
        // Length
        data.setUInt8(0xE);
        // Tag for xDLMS-Initiate response
        data.setUInt8(GXCommon.INITIAL_RESPONSE);
        // Usage field for the response allowed component not used
        data.setUInt8(0x00);
        data.setUInt8(6); // DLMSVersioNumber
        data.setUInt8(0x5F);
        data.setUInt8(0x1F);
        // Length of the conformance block.
        data.setUInt8(0x04);
        // encoding the number of unused bits in the bit string
        data.setUInt8(0x00);
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
        data.setUInt8(offset + 1, data.size() - offset - 2);
    }
}