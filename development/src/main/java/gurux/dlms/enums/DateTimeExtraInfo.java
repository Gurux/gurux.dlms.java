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

package gurux.dlms.enums;

import java.util.HashSet;
import java.util.Set;

/*
 *  Date time extra info.
 */
public enum DateTimeExtraInfo {
    // Daylight savings begin.
    DST_BEGIN(0x1),
    // Daylight savings end.
    DST_END(0x2),
    // Last day of month.
    LAST_DAY(0x4),
    // 2nd last day of month
    LAST_DAY2(0x8);

    private int value;

    /*
     * Constructor.
     */
    DateTimeExtraInfo(final int forValue) {
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
    private static DateTimeExtraInfo[] getEnumConstants() {
        return new DateTimeExtraInfo[] { DST_BEGIN, DST_END, LAST_DAY,
                LAST_DAY2 };

    }

    /**
     * Converts the integer value to enumerated value.
     * 
     * @param value
     *            The integer value, which is read from the device.
     * @return The enumerated value, which represents the integer.
     */
    public static java.util.Set<DateTimeExtraInfo> forValue(final int value) {
        Set<DateTimeExtraInfo> types = new HashSet<DateTimeExtraInfo>();
        DateTimeExtraInfo[] enums = getEnumConstants();
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
    public static int toInteger(final Set<DateTimeExtraInfo> value) {
        int tmp = 0;
        for (DateTimeExtraInfo it : value) {
            tmp |= it.getValue();
        }
        return tmp;
    }
}
