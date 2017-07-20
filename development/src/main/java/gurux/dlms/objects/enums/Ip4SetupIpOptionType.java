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

public enum Ip4SetupIpOptionType {
    /**
     * If this option is present, the device shall be allowed to send security,
     * compartmentation, handling restrictions and TCC (closed user group)
     * parameters within its IP Datagrams. The value of the IP-Option- Length
     * Field must be 11, and the IP-Option-Data shall contain the value of the
     * Security, Compartments, Handling Restrictions and Transmission Control
     * code values, as specified in STD0005 / RFC791.
     */
    SECURITY(0x82),
    /*
     * If this option is present, the device shall supply routing information to
     * be used by the gateways in forwarding the datagram to the destination,
     * and to record the route information. The IP-Option-length and
     * IP-Option-Data values are specified in STD0005 / RFC 791.
     */
    LOOSE_SOURCE_AND_RECORD_ROUTE(0x83),
    /*
     * If this option is present, the device shall supply routing information to
     * be used by the gateways in forwarding the datagram to the destination,
     * and to record the route information. The IP-Option-length and
     * IP-Option-Data values are specified in STD0005 / RFC 791.
     */
    STRICT_SOURCE_AND_RECORD_ROUTE(0x89),
    /*
     * If this option is present, the device shall as well: send originated IP
     * Datagrams with that option, providing means to record the route of these
     * Datagrams; as a router, send routed IP Datagrams with the route option
     * adjusted according to this option. The IP-Option-length and
     * IP-Option-Data values are specified in STD0005 / RFC 791.
     */
    RECORD_ROUTE(0x07),
    /*
     * If this option is present, the device shall as well: send originated IP
     * Datagrams with that option, providing means to time-stamp the datagram in
     * the route to its destination; as a router, send routed IP Datagrams with
     * the time-stamp option adjusted according to this option. The
     * IP-Option-length and IP-Option-Data values are specified in STD0005 / RFC
     * 791.
     */
    INTERNET_TIMESTAMP(0x44);

    private int intValue;
    private static HashMap<Integer, Ip4SetupIpOptionType> mappings;

    private static java.util.HashMap<Integer, Ip4SetupIpOptionType>
            getMappings() {
        if (mappings == null) {
            synchronized (Ip4SetupIpOptionType.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, Ip4SetupIpOptionType>();
                }
            }
        }
        return mappings;
    }

    Ip4SetupIpOptionType(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
    }

    public int getValue() {
        return intValue;
    }

    public static Ip4SetupIpOptionType forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}