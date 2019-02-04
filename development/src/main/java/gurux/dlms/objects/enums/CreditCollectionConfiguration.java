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
 * Defines behavior under specific conditions.
 */
public enum CreditCollectionConfiguration {
    /**
     * None.
     */
    NONE(0),
    /**
     * Collect when supply disconnected.
     */
    DISCONNECTED(0x80),
    /**
     * Collect in load limiting periods.
     */
    LOAD_LIMITING(0x40),
    /**
     * Collect in friendly credit periods.
     */
    FRIENDLY_CREDIT(0x20);

    /**
     * Integer value of enumerator.
     */
    private int intValue;

    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, CreditCollectionConfiguration> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, CreditCollectionConfiguration>
            getMappings() {
        synchronized (CreditCollectionConfiguration.class) {
            if (mappings == null) {
                mappings =
                        new HashMap<Integer, CreditCollectionConfiguration>();
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
    CreditCollectionConfiguration(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
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
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static CreditCollectionConfiguration forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}