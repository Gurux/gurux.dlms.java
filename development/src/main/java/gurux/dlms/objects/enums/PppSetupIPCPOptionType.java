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

package gurux.dlms.objects.enums;

import java.util.HashMap;

public enum PppSetupIPCPOptionType {
    /*
     * 
     */
    IP_COMPRESSION_PROTOCOL(2),
    /*
     * 
     */
    PREF_LOCAL_IP(3),
    /*
     * 
     */
    PREF_PEER_IP(20),
    /*
     * 
     */
    GAO(21),
    /*
     * 
     */
    USIP(22);

    private int intValue;
    private static HashMap<Integer, PppSetupIPCPOptionType> mappings;

    private static HashMap<Integer, PppSetupIPCPOptionType>
            getMappings() {
        if (mappings == null) {
            synchronized (PppSetupIPCPOptionType.class) {
                mappings = new HashMap<Integer, PppSetupIPCPOptionType>();
            }
        }
        return mappings;
    }

    PppSetupIPCPOptionType(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
    }

    public int getValue() {
        return intValue;
    }

    public static PppSetupIPCPOptionType forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}