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

package gurux.dlms.objects.enums;

import java.util.HashMap;

/**
 * Defines the protocol used by the meter on the port.
 */
public enum OpticalProtocolMode {
    /**
     * Protocol according to IEC 62056-21 (modes A…E),
     */
    DEFAULT(0),
    /**
     * Protocol according to IEC 62056-46. Using this enumeration value all
     * other attributes of this IC are not applicable.
     */
    NET(1),
    /**
     * Protocol not specified. Using this enumeration value, ProposedBaudrate is
     * used for setting the communication speed on the port. All other
     * attributes are not applicable.
     */
    UNKNOWN(2);

    private int intValue;
    private static java.util.HashMap<Integer, OpticalProtocolMode> mappings;

    private static java.util.HashMap<Integer, OpticalProtocolMode>
            getMappings() {
        if (mappings == null) {
            synchronized (OpticalProtocolMode.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, OpticalProtocolMode>();
                }
            }
        }
        return mappings;
    }

    OpticalProtocolMode(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
    }

    public int getValue() {
        return intValue;
    }

    public static OpticalProtocolMode forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}