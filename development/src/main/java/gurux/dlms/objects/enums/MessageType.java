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

package gurux.dlms.objects.enums;

public enum MessageType {
    COSEM_APDU(0), COSEM_APDU_XML(1), MANUFACTURER_SPESIFIC(128);
    // (128...255) manufacturer specific

    private int intValue;
    private static java.util.HashMap<Integer, MessageType> mappings;

    private static java.util.HashMap<Integer, MessageType> getMappings() {
        if (mappings == null) {
            synchronized (MessageType.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, MessageType>();
                }
            }
        }
        return mappings;
    }

    MessageType(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue() {
        return intValue;
    }

    public static MessageType forValue(final int value) {
        MessageType ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid message type enum value.");
        }
        return ret;
    }
}