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

import java.util.HashMap;
import java.util.HashSet;

/**
 * Charge configuration enumeration types.
 */
public enum ChargeConfiguration {

    /**
     * Percentage based collection.
     */
    PERCENTAGE_BASED_COLLECTION(0x1),
    /**
     * Continuous collection.
     */
    CONTINUOUS_COLLECTION(0x2);

    /**
     * Integer value of enumerator.
     */
    private int intValue;

    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, ChargeConfiguration> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, ChargeConfiguration> getMappings() {
        synchronized (ChargeConfiguration.class) {
            if (mappings == null) {
                mappings = new HashMap<Integer, ChargeConfiguration>();
            }
        }
        return mappings;
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Integer value of enumerator.
     */
    ChargeConfiguration(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /**
     * Get integer value for enumerator.
     * 
     * @return Enumerator integer value.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * @return Get enumeration constant values.
     */
    private static ChargeConfiguration[] getEnumConstants() {
        return new ChargeConfiguration[] { PERCENTAGE_BASED_COLLECTION,
                CONTINUOUS_COLLECTION };

    }

    /**
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static java.util.Set<ChargeConfiguration> forValue(final int value) {
        java.util.Set<ChargeConfiguration> types =
                new HashSet<ChargeConfiguration>();
        if (value != 0) {
            types = new HashSet<ChargeConfiguration>();
            ChargeConfiguration[] enums = getEnumConstants();
            for (int pos = 0; pos != enums.length; ++pos) {
                if ((enums[pos].intValue & value) == enums[pos].intValue) {
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
    public static int
            toInteger(final java.util.Set<ChargeConfiguration> value) {
        int tmp = 0;
        for (ChargeConfiguration it : value) {
            tmp |= it.getValue();
        }
        return tmp;
    }
}