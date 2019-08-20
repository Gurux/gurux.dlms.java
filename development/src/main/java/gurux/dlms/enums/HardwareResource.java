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
 * Hardware resource describes hardware errors.
 */
public enum HardwareResource {
    /**
     * Other hardware resource error.
     */
    OTHER(0),

    /**
     * Memory is unavailable.
     */
    MEMORY_UNAVAILABLE(1),

    /**
     * Processor resource is unavailable.
     */
    PROCESSOR_RESOURCE_UNAVAILABLE(2),

    /**
     * Mass storage is unavailable.
     */
    MASS_STORAGE_UNAVAILABLE(3),

    /**
     * Other resource is unavailable.
     */
    OTHER_RESOURCE_UNAVAILABLE(4);

    private int value;
    private static HashMap<Integer, HardwareResource> mappings;

    private static HashMap<Integer, HardwareResource> getMappings() {
        synchronized (HardwareResource.class) {
            if (mappings == null) {
                mappings = new HashMap<Integer, HardwareResource>();
            }
        }
        return mappings;
    }

    HardwareResource(final int mode) {
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
    public static HardwareResource forValue(final int value) {
        return getMappings().get(value);
    }
}