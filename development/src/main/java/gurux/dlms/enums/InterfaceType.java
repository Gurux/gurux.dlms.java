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

import java.util.HashSet;
import java.util.Set;

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
    WIRED_MBUS,
    /**
     * SMS short wrapper scheme is used.
     */
    SMS,
    /**
     * PRIME data concentrator wrapper.
     */
    PRIME_DC_WRAPPER,
    /**
     * Constrained Application Protocol (CoAP).
     */
    COAP;

    /**
     * @return Get integer value for enumeration.
     */
    public int getValue() {
        return this.ordinal();
    }

    static InterfaceType[] getEnumConstants() {
        return new InterfaceType[] { HDLC, WRAPPER, PDU, WIRELESS_MBUS, HDLC_WITH_MODE_E, PLC, PLC_HDLC, LPWAN, WI_SUN,
                PLC_PRIME, WIRED_MBUS, SMS, PRIME_DC_WRAPPER, COAP };
    }

    /*
     * Convert integer for enumeration value.
     */
    public static InterfaceType forValue(final int value) {
        return values()[value];
    }

    /*
     * Convert integer for enumeration values.
     */
    public static Set<InterfaceType> getInterfaceTypes(final int value) {
        Set<InterfaceType> types = new HashSet<InterfaceType>();
        InterfaceType[] enums = getEnumConstants();
        for (int pos = 0; pos != enums.length; ++pos) {
            if ((enums[pos].getValue() & value) == enums[pos].getValue()) {
                types.add(enums[pos]);
            }
        }
        return types;
    }

    @Override
    public String toString() {
        String str;
        InterfaceType value = InterfaceType.forValue(getValue());
        switch (value) {
        case HDLC:
            str = "HDLC";
            break;
        case WRAPPER:
            str = "WRAPPER";
            break;
        case PDU:
            str = "PDU";
            break;
        case WIRELESS_MBUS:
            str = "Wireless MBUS";
            break;
        case HDLC_WITH_MODE_E:
            str = "HDLC with mode E";
            break;
        case PLC:
            str = "PLC";
            break;
        case PLC_HDLC:
            str = "PLC HDLC";
            break;
        case LPWAN:
            str = "LPWAN";
            break;
        case WI_SUN:
            str = "WI-Sun";
            break;
        case PLC_PRIME:
            str = "PLC PRIME";
            break;
        case WIRED_MBUS:
            str = "Wired MBUS";
            break;
        case SMS:
            str = "SMS";
            break;
        case PRIME_DC_WRAPPER:
            str = "DC WRAPPER";
            break;
        case COAP:
            str = "CoAP";
            break;
        default:
            str = "";
            break;
        }
        return str;
    }

    public static InterfaceType valueOfString(final String value) {
        InterfaceType v;
        if ("HDLC".equalsIgnoreCase(value)) {
            v = InterfaceType.HDLC;
        } else if ("WRAPPER".equalsIgnoreCase(value)) {
            v = InterfaceType.WRAPPER;
        } else if ("PDU".equalsIgnoreCase(value)) {
            v = InterfaceType.PDU;
        } else if ("Wireless MBUS".equalsIgnoreCase(value)) {
            v = InterfaceType.WIRELESS_MBUS;
        } else if ("HDLC with mode E".equalsIgnoreCase(value)) {
            v = InterfaceType.HDLC_WITH_MODE_E;
        } else if ("PLC".equalsIgnoreCase(value)) {
            v = InterfaceType.PLC;
        } else if ("PLC HDLC".equalsIgnoreCase(value)) {
            v = InterfaceType.PLC_HDLC;
        } else if ("LPWAN".equalsIgnoreCase(value)) {
            v = InterfaceType.LPWAN;
        } else if ("WI-Sun".equalsIgnoreCase(value)) {
            v = InterfaceType.WI_SUN;
        } else if ("PLC PRIME".equalsIgnoreCase(value)) {
            v = InterfaceType.PLC_PRIME;
        } else if ("Wired MBUS".equalsIgnoreCase(value)) {
            v = InterfaceType.WIRED_MBUS;
        } else if ("SMS".equalsIgnoreCase(value)) {
            v = InterfaceType.SMS;
        } else if ("DC WRAPPER".equalsIgnoreCase(value)) {
            v = InterfaceType.PRIME_DC_WRAPPER;
        } else if ("CoAP".equalsIgnoreCase(value)) {
            v = InterfaceType.COAP;
        } else {
            throw new IllegalArgumentException(value);
        }
        return v;
    }
}