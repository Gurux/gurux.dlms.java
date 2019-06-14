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

import java.util.HashMap;

/**
 * ACSE service provider.
 */
public enum AcseServiceProvider {
    /**
     * There is no error.
     */
    NONE(0),
    /**
     * Reason is not given.
     */
    NO_REASON_GIVEN(1),
    /**
     * Invalid ACSE version.
     */
    NO_COMMON_ACSE_VERSION(2);

    private int intValue;
    private static HashMap<Integer, AcseServiceProvider> mappings;

    private static HashMap<Integer, AcseServiceProvider> getMappings() {
        synchronized (AcseServiceProvider.class) {
            if (mappings == null) {
                mappings = new HashMap<Integer, AcseServiceProvider>();
            }
        }
        return mappings;
    }

    AcseServiceProvider(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /*
     * Get integer value for enumeration.
     */
    public int getValue() {
        return intValue;
    }

    /*
     * Convert integer for enumeration value.
     */
    public static AcseServiceProvider forValue(final int value) {
        return getMappings().get(value);
    }
}