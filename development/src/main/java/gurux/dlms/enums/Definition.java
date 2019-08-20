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

/**
 * Definition describes definition errors.
 */
public enum Definition {
    /**
     * Other error.
     */
    OTHER(0),
    /**
     * Object is Undefined.
     */
    OBJECT_UNDEFINED(1),
    /**
     * Object class inconsistent.
     */
    OBJECT_CLASS_INCONSISTENT(2),
    /**
     * Object attribute inconsistent.
     */
    OBJECT_ATTRIBUTE_INCONSISTENT(3);

    private int value;
    private static java.util.HashMap<Integer, Definition> mappings;

    private static java.util.HashMap<Integer, Definition> getMappings() {
        synchronized (Definition.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, Definition>();
            }
        }
        return mappings;
    }

    Definition(final int mode) {
        this.value = mode;
        getMappings().put(mode, this);
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
    public static Definition forValue(final int value) {
        return getMappings().get(value);
    }
}