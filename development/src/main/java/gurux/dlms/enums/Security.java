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
 * Used security model.
 */
public enum Security {
    /**
     * Transport security is not used.
     */
    NONE(0),

    /**
     * Authentication security is used.
     */
    AUTHENTICATION(0x10),

    /**
     * Encryption security is used.
     */
    ENCRYPTION(0x20),

    /**
     * Authentication and Encryption security are used.
     */
    AUTHENTICATION_ENCRYPTION(0x30);

    private int intValue;
    private static java.util.HashMap<Integer, Security> mappings;

    private static java.util.HashMap<Integer, Security> getMappings() {
        if (mappings == null) {
            synchronized (Security.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, Security>();
                }
            }
        }
        return mappings;
    }

    Security(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
    }

    public int getValue() {
        return intValue;
    }

    public static Security forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}