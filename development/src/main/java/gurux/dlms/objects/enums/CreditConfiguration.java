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
import java.util.HashSet;

/**
 * Enumerated Credit configuration values.<br>
 * Online help:<br>
 * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
 */
public enum CreditConfiguration {
    /**
     * Requires visual indication,
     */
    VISUAL(0x10),
    /**
     * Requires confirmation before it can be selected/invoked
     */
    CONFIRMATION(0x8),
    /**
     * Requires the credit amount to be paid back.
     */
    PAID_BACK(0x4),
    /**
     * Resettable.
     */

    RESETTABLE(0x2),
    /**
     * Able to receive credit amounts from tokens.
     */

    TOKENS(0x1);

    /**
     * Integer value of enumerator.
     */
    private int intValue;

    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, CreditConfiguration> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, CreditConfiguration> getMappings() {
        synchronized (CreditConfiguration.class) {
            if (mappings == null) {
                mappings = new HashMap<Integer, CreditConfiguration>();
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
    CreditConfiguration(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
    }

    /*
     * Get integer value for enumeration.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * @return Get enumeration constant values.
     */
    private static CreditConfiguration[] getEnumConstants() {
        return new CreditConfiguration[] { VISUAL, CONFIRMATION, PAID_BACK,
                RESETTABLE, TOKENS };

    }

    /**
     * Converts the integer value to enumerated value.
     * 
     * @param value
     *            The integer value, which is read from the device.
     * @return The enumerated value, which represents the integer.
     */
    public static java.util.Set<CreditConfiguration> forValue(final int value) {
        java.util.Set<CreditConfiguration> types;
        if (value == 0) {
            types = new HashSet<CreditConfiguration>();
        } else {
            types = new HashSet<CreditConfiguration>();
            CreditConfiguration[] enums = getEnumConstants();
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
            toInteger(final java.util.Set<CreditConfiguration> value) {
        int tmp = 0;
        for (CreditConfiguration it : value) {
            tmp |= it.getValue();
        }
        return tmp;
    }
}