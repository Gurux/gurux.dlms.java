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

public enum AutoAnswerStatus {
    INACTIVE(0), ACTIVE(1), LOCKED(2);

    private int intValue;
    private static java.util.HashMap<Integer, AutoAnswerStatus> mappings;

    private static java.util.HashMap<Integer, AutoAnswerStatus> getMappings() {
        synchronized (AutoAnswerStatus.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, AutoAnswerStatus>();
            }
        }
        return mappings;
    }

    AutoAnswerStatus(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /*
     * Get integer value for enum.
     */
    public int getValue() {
        return intValue;
    }

    /*
     * Convert integer for enum value.
     */
    public static AutoAnswerStatus forValue(final int value) {
        AutoAnswerStatus ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid auto answer status enum value.");
        }
        return ret;
    }
}