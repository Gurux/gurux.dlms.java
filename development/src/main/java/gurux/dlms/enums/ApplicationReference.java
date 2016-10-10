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

import java.util.HashMap;

/**
 * Application reference describes application errors.
 */
public enum ApplicationReference {
    /**
     * Other error is occurred.
     */
    OTHER(0),
    /**
     * Time elapsed.
     */
    TIME_ELAPSED(1),
    /**
     * Application unreachable.
     */
    APPLICATION_UNREACHABLE(2),
    /**
     * Application reference is invalid.
     */
    APPLICATION_REFERENCE_INVALID(3),
    /**
     * Application context unsupported.
     */
    APPLICATION_CONTEXT_UNSUPPORTED(4),
    /**
     * Provider communication error.
     */
    PROVIDER_COMMUNICATION_ERROR(5),
    /**
     * Deciphering error.
     */
    DECIPHERING_ERROR(6);

    private int value;
    private static HashMap<Integer, ApplicationReference> mappings;

    private static HashMap<Integer, ApplicationReference> getMappings() {
        synchronized (ApplicationReference.class) {
            if (mappings == null) {
                mappings = new HashMap<Integer, ApplicationReference>();
            }
        }
        return mappings;
    }

    ApplicationReference(final int mode) {
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
    public static ApplicationReference forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}