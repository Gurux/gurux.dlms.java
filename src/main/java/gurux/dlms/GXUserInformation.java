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

import gurux.dlms.internal.GXCommon;

/**
 * Reserved for internal use.
 */
final class GXUserInformation {

    /**
     * Constructor.
     */
    private GXUserInformation() {
    }

    /**
     * Reserved for internal use.
     * 
     * @param data
     */
    static void codeData(final GXDLMSSettings settings,
            final GXByteBuffer data) {
        // Tag
        data.setUInt8(0xBE);
        // Length for AARQ user field
        data.setUInt8(0x10);
        // Coding the choice for user-information (Octet-string, universal)
        data.setUInt8(0x04);
        // Length
        data.setUInt8(0x0E);
        // Tag for xDLMS-Initiate request
        data.setUInt8(GXCommon.INITIAL_REQUEST);
        // Usage field for dedicated-key component. Not used.
        data.setUInt8(0x00);
        // Usage field for the response allowed component. Not used.
        data.setUInt8(0x00);
        // Usage field of the proposed-quality-of-service component. Not used
        data.setUInt8(0x00);
        data.setUInt8(settings.getDlmsVersionNumber());
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
     * Reserved for internal use.
     * 
     * @param data
     * @throws Exception
     */
    static void encodeData(final GXDLMSSettings settings,
            final GXByteBuffer data) {
        int tag = data.getUInt8();
        if (tag != 0xBE) {
            throw new GXDLMSException("Invalid tag.");
        }
        int len = data.getUInt8();
        if (data.size() - data.position() < len) {
            throw new GXDLMSException("Not enought data.");
        }
        // Encoding the choice for user information
        tag = data.getUInt8();
        if (tag != 0x4) {
            throw new GXDLMSException("Invalid tag.");
        }
        // Skip
        data.getUInt8();
        // Tag for xDLMS-Initate.response
        tag = data.getUInt8();
        boolean response = tag == GXCommon.INITIAL_RESPONSE;
        if (response) {
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            // Skip if used.
            if (tag != 0) {
                len = data.getUInt8();
                data.position(data.position() + len);
            }
        } else if (tag == GXCommon.INITIAL_REQUEST) {
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            // Skip if used.
            if (tag != 0) {
                len = data.getUInt8();
                data.position(data.position() + len);
            }
            // Optional usage field of the negotiated quality of service
            // component
            tag = data.getUInt8();
            // Skip if used.
            if (tag != 0) {
                len = data.getUInt8();
                data.position(data.position() + len);
            }
            // Optional usage field of the negotiated quality of service
            // component
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
            if (data.getUInt8() != 6) {
                throw new RuntimeException("Invalid DLMS version number.");
            }
        } else {
            settings.setDlmsVersionNumber((byte) data.getUInt8());
        }
        // Tag for conformance block
        tag = data.getUInt8();
        if (tag != 0x5F) {
            throw new GXDLMSException("Invalid tag.");
        }
        // Old Way...
        if (data.getUInt8(data.position()) == 0x1F) {
            data.getUInt8();
        }
        // Get length.
        data.getUInt8();
        // The number of unused bits in the bit string.
        // Get tag
        data.getUInt8();
        if (settings.getUseLogicalNameReferencing()) {
            if (settings.getLnSettings() == null) {
                settings.setLnSettings(new GXDLMSLNSettings());
            }
            if (settings.isServer()) {
                // Skip settings what client asks.
                // All server settings are always returned.
                byte[] tmp = new byte[3];
                data.get(tmp);
            } else {
                data.get(settings.getLnSettings().getConformanceBlock());
            }
        } else {
            if (settings.getSnSettings() == null) {
                settings.setSnSettings(new GXDLMSSNSettings());
            }
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
        // get VAA Name
        if (response) {
            int vaa = data.getUInt16();
            if (vaa == 0x0007) {
                // If LN
                if (!settings.getUseLogicalNameReferencing()) {
                    throw new RuntimeException("Invalid VAA.");
                }
            } else if (vaa == 0xFA00) {
                // If SN
                if (settings.getUseLogicalNameReferencing()) {
                    throw new RuntimeException("Invalid VAA.");
                }
            } else {
                // Unknown VAA.
                throw new RuntimeException("Invalid VAA.");
            }
        }
    }
}
