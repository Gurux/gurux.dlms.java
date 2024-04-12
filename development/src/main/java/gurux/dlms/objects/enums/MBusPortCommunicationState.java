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
 * Defines the M-Bus port communication status.
 */
public enum MBusPortCommunicationState {
    /**
     * No access.
     */
    NO_ACCESS,
    /**
     * Temporary no access
     */
    TEMPORARY_NO_ACCESS,
    /**
     * Limited access
     */
    LIMITED_ACCESS,
    /**
     * Unlimited access.
     */
    UNLIMITED_ACCESS,
    /**
     * wM-Bus.
     */
    WM_BUS;

    /**
     * Get enemerator's integer value.
     * 
     * @return Integer value of enumerator.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Get enumerator from integer value.
     * 
     * @param value
     *            integer value.
     * @return Enumerator value.
     */
    public static MBusPortCommunicationState forValue(int value) {
        return MBusPortCommunicationState.values()[value];
    }
}