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
package gurux.dlms.plc.enums;

/**
 * PLC Destination address enumerations.
 */
public enum PlcDestinationAddress {
    /**
     * All physical devices.
     */
    ALL_PHYSICAL(0xFFF);

    private int value;
    private static java.util.HashMap<Integer, PlcDestinationAddress> mappings;

    private static java.util.HashMap<Integer, PlcDestinationAddress>
            getMappings() {
        synchronized (PlcDestinationAddress.class) {
            if (mappings == null) {
                mappings =
                        new java.util.HashMap<Integer, PlcDestinationAddress>();
            }
        }
        return mappings;
    }

    PlcDestinationAddress(final int mode) {
        this.value = mode;
        getMappings().put(mode, this);
    }

    /*
     * Get integer value for enum.
     */
    public final int getValue() {
        return value;
    }

    /*
     * Convert integer for enum value.
     */
    public static PlcDestinationAddress forValue(final int value) {
        return getMappings().get(value);
    }
}