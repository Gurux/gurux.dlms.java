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
 * InterfaceType enumerates the usable types of connection in GuruxDLMS.
 */
public enum InterfaceType {
    /**
     * By default, the interface type is HDLC.
     */
    HDLC,
    /**
     * The interface type is TCP/IP or UDP wrapper, can be used with devices
     * that support IEC 62056-47.
     */
    WRAPPER,
    /**
     * Plain PDU is returned.
     */
    PDU,
    /**
     * Wireless M-Bus frame.
     */
    WIRELESS_MBUS,
    /**
     * IEC 62056-21 E-Mode is used to initialize communication before moving to
     * HDLC protocol.
     */
    HDLC_WITH_MODE_E,
    /**
     * PLC Logical link control (LLC) profile is used with IEC 61334-4-32
     * connectionless LLC sublayer.
     * <p>
     * Blue Book: 10.4.4.3.3 The connectionless LLC sublayer.
     * </p>
     */
    PLC,
    /**
     * PLC Logical link control (LLC) profile is used with HDLC.
     * <p>
     * Blue Book: 10.4.4.3.4 The HDLC based LLC sublayer.
     * </p>
     */
    PLC_HDLC,
    /**
     * LowPower Wide Area Networks (LPWAN) profile is used.
     */
    LPWAN,
    /**
     * Wi-SUN FAN mesh network is used.
     */
    WI_SUN,
    /**
     * OFDM PLC PRIME is defined in IEC 62056-8-4.
     */
    PLC_PRIME,
    /**
     * EN 13757-2 wired (twisted pair based) M-Bus scheme is used.
     */
    WIRED_MBUS;

    /**
     * @return Get integer value for enumeration.
     */
    public int getValue() {
        return this.ordinal();
    }

    /*
     * Convert integer for enumeration value.
     */
    public static InterfaceType forValue(final int value) {
        return values()[value];
    }
}