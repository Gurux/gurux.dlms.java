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
 * Access describes access errors.
 */
public enum Access {
    /**
     * Other error.
     */
    OTHER(0),
    /**
     * Scope of access violated.
     */
    SCOPE_OF_ACCESS_VIOLATED(1),
    /**
     * Object access is invalid.
     */
    OBJECT_ACCESS_INVALID(2),
    /**
     * Hardware fault.
     */
    HARDWARE_FAULT(3),
    /**
     * Object is unavailable.
     */
    OBJECT_UNAVAILABLE(4);

    private int value;
    private static java.util.HashMap<Integer, Access> mappings;

    private static java.util.HashMap<Integer, Access> getMappings() {
        synchronized (Access.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, Access>();
            }
        }
        return mappings;
    }

    Access(final int mode) {
        this.value = mode;
        getMappings().put(new Integer(mode), this);
    }

    /*
     * Get integer value for enum.
     */
    public final int getValue() {
        return value;
    }

    /*
     * Convert integer for enum value.
     */
    public static Access forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}