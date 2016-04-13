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

/**
 * Used priority.
 */
public enum Priority {
    /**
     * Normal priority.
     */
    NORMAL(0),

    /**
     * High priority.
     */
    HIGH(1);

    private int intValue;
    private static java.util.HashMap<Integer, Priority> mappings;

    private static java.util.HashMap<Integer, Priority> getMappings() {
        if (mappings == null) {
            synchronized (Priority.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, Priority>();
                }
            }
        }
        return mappings;
    }

    Priority(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
    }

    public int getValue() {
        return intValue;
    }

    public static Priority forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}