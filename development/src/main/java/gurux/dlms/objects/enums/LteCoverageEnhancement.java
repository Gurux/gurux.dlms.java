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

/**
 * Lte coverage enhancement.
 */
public enum LteCoverageEnhancement {
    /**
     * CE Mode A in LTE Cat M1 and CE Level 0 in NB-Iot.
     */
    LEVEL0,
    /**
     * CE Mode B in LTE Cat M1 and CE Level 1 in NB-Iot.
     */
    LEVEL1,
    /**
     * CE Level 2 in NB-Iot.
     */
    LEVEL2;

    /**
     * Get integer value for enumerator.
     * 
     * @return Enumerator integer value.
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static LteCoverageEnhancement forValue(final int value) {
        LteCoverageEnhancement ret = LteCoverageEnhancement.values()[value];
        if (ret == null) {
            throw new IllegalArgumentException("Invalid LTE coverage enhancement enum value.");
        }
        return ret;
    }
}