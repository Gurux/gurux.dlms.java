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

package gurux.dlms.enums;

import java.util.HashSet;

/**
 * Defines Clock status.
 */
public enum ClockStatus {
    /**
     * OK.
     */
    OK(0),

    /**
     * Invalid value.
     */
    INVALID_VALUE(0x1),

    /**
     * Doubtful b value.
     */
    DOUBTFUL_VALUE(0x2),

    /**
     * Different clock base c.
     */
    DIFFERENT_CLOCK_BASE(0X4),

    /**
     * Invalid clock status d.
     */
    INVALID_CLOCK_STATUS(0x8),

    /**
     * Reserved.
     */
    RESERVED2(0x10),

    /**
     * Reserved.
     */
    RESERVED3(0x20),

    /**
     * Reserved.
     */
    RESERVED4(0x40),

    /**
     * Daylight saving active.
     */
    DAYLIGHT_SAVE_ACTIVE(0x80),

    /**
     * Clock status is skipped.
     */
    SKIPPED(0xFF);

    private int value;
    private static java.util.HashMap<Integer, ClockStatus> mappings;

    private static java.util.HashMap<Integer, ClockStatus> getMappings() {
        synchronized (ClockStatus.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, ClockStatus>();
            }
        }
        return mappings;
    }

    ClockStatus(final int forValue) {
        this.value = forValue;
        getMappings().put(new Integer(forValue), this);
    }

    /*
     * Get integer value for enumeration.
     */
    public int getValue() {
        return value;
    }

    /**
     * @return Get enumeration constant values.
     */
    private static ClockStatus[] getEnumConstants() {
        return new ClockStatus[] { OK, INVALID_VALUE, DOUBTFUL_VALUE,
                DIFFERENT_CLOCK_BASE, INVALID_CLOCK_STATUS, RESERVED2,
                RESERVED3, RESERVED4, DAYLIGHT_SAVE_ACTIVE, SKIPPED };

    }

    /**
     * Converts the integer value to enumerated value.
     * 
     * @param value
     *            The integer value, which is read from the device.
     * @return The enumerated value, which represents the integer.
     */
    public static java.util.Set<ClockStatus> forValue(final int value) {
        java.util.Set<ClockStatus> types;
        if (value == 0) {
            types = new HashSet<ClockStatus>();
            types.add(ClockStatus.OK);
        } else {
            types = new HashSet<ClockStatus>();
            ClockStatus[] enums = getEnumConstants();
            for (int pos = 0; pos != enums.length; ++pos) {
                if (enums[pos] != ClockStatus.OK
                        && (enums[pos].value & value) == enums[pos].value) {
                    types.add(enums[pos]);
                }
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
    public static int toInteger(final java.util.Set<ClockStatus> value) {
        int tmp = 0;
        for (ClockStatus it : value) {
            tmp |= it.getValue();
        }
        return tmp;
    }
};