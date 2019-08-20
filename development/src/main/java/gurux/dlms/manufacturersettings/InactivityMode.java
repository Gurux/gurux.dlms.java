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

package gurux.dlms.manufacturersettings;

/**
 * Enumerates inactivity modes that are used, when communicating with IEC using
 * serial port connection.
 */
public enum InactivityMode {
    NONE(0),
    /*
     * Keep alive message is sent, only if there is no traffic on the active
     * connection.
     */
    KEEPALIVE(1),
    /*
     * Connection is reopened, if there is no traffic on the active connection.
     */
    REOPEN(2),
    /*
     * Connection is reopened, even if there is traffic on the active
     * connection.
     */
    REOPENACTIVE(3),
    /*
     * Closes connection, if there is no traffic on the active connection.
     */
    DISCONNECT(4);

    private int intValue;
    private static java.util.HashMap<Integer, InactivityMode> mappings;

    private static java.util.HashMap<Integer, InactivityMode> getMappings() {
        synchronized (InactivityMode.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, InactivityMode>();
            }
        }
        return mappings;
    }

    InactivityMode(final int value) {
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
    public static InactivityMode forValue(final int value) {
        return getMappings().get(value);
    }
}