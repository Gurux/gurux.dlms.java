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
final class GXApplicationContextName {

    /**
     * Constructor.
     */
    private GXApplicationContextName() {

    }

    /**
     * Code application context name.
     * 
     * @param data
     */
    static void codeData(final GXDLMSSettings settings, final GXByteBuffer data,
            final boolean ciphered) {
        // Application context name tag
        data.setUInt8(0xA1);
        // Length of Application context name.
        data.setUInt8(0x09);
        data.setUInt8(0x06);
        data.setUInt8(0x07);
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
    }

    /**
     * Encode application context name.
     * 
     * @param settings
     *            DMLS settings.
     * @param buff
     *            Byte buffer where data is encoded.
     */
    static boolean encodeData(final GXDLMSSettings settings,
            final GXByteBuffer buff) {
        int tag = buff.getUInt8();
        if (tag != 0xA1) {
            throw new GXDLMSException("Invalid tag.");
        }
        // Get length.
        int len = buff.getUInt8();
        if (buff.size() - buff.position() < len) {
            throw new GXDLMSException("Encoding failed. Not enought data.");
        }
        buff.getUInt8();
        // Get length.
        buff.getUInt8();
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
};