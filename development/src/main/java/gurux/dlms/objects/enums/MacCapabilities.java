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

import java.util.HashSet;
import java.util.Set;

/**
 * Present functional state of the node.
 */
public enum MacCapabilities {
    /**
     * Switch capable.
     */
    SWITCH_CAPABLE(1),
    /**
     * Packet aggregation.
     */
    PACKET_AGGREGATION(2),
    /**
     * Contention free period.
     */
    CONTENTION_FREE_PERIOD(4),
    /**
     * Direct connection.
     */
    DIRECT_CONNECTION(8),
    /**
     * Multicast.
     */
    MULTICAST(0x10),
    /**
     * PHY Robustness Management.
     */
    PHY_ROBUSTNESS_MANAGEMENT(0x20),
    /**
     * ARQ.
     */
    ARQ(0x40),
    /**
     * Reserved for future use.
     */
    RESERVED_FOR_FUTURE_USE(0x80),
    /**
     * Direct Connection Switching.
     */
    DIRECT_CONNECTION_SWITCHING(0x100),
    /**
     * Multicast Switching Capability.
     */
    MULTICAST_SWITCHING_CAPABILITY(0x200),
    /**
     * PHY Robustness Management Switching Capability.
     */
    PHY_ROBUSTNESS_MANAGEMENT_SWITCHING_CAPABILITY(0x400),
    /**
     * ARQ Buffering Switching Capability.
     */
    ARQ_BUFFERING_SWITCHING_CAPABILITY(0x800);

    private int value;

    /*
     * Constructor.
     */
    MacCapabilities(final int forValue) {
        value = forValue;
    }

    /*
     * Get integer value for enumerator.
     */
    public int getValue() {
        return value;
    }

    /**
     * @return Get enumeration constant values.
     */
    private static MacCapabilities[] getEnumConstants() {
        return new MacCapabilities[] { SWITCH_CAPABLE, PACKET_AGGREGATION,
                CONTENTION_FREE_PERIOD, DIRECT_CONNECTION, MULTICAST,
                PHY_ROBUSTNESS_MANAGEMENT, ARQ, RESERVED_FOR_FUTURE_USE,
                DIRECT_CONNECTION_SWITCHING, MULTICAST_SWITCHING_CAPABILITY,
                PHY_ROBUSTNESS_MANAGEMENT_SWITCHING_CAPABILITY,
                ARQ_BUFFERING_SWITCHING_CAPABILITY };

    }

    /**
     * Converts the integer value to enumerated value.
     * 
     * @param value
     *            The integer value, which is read from the device.
     * @return The enumerated value, which represents the integer.
     */
    public static java.util.Set<MacCapabilities> forValue(final int value) {
        Set<MacCapabilities> types = new HashSet<MacCapabilities>();
        MacCapabilities[] enums = getEnumConstants();
        for (int pos = 0; pos != enums.length; ++pos) {
            if ((enums[pos].value & value) == enums[pos].value) {
                types.add(enums[pos]);
            }
        }
        return types;
    }

    /**
     * Converts the enumerated value to integer value.
     * 
     * @param value
     *            The enumerated value.
     * @return The integer value.
     */
    public static int toInteger(final Set<MacCapabilities> value) {
        int tmp = 0;
        for (MacCapabilities it : value) {
            tmp |= it.getValue();
        }
        return tmp;
    }
}
