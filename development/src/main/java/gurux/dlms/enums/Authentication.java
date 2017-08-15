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
 * Authentication enumerates the authentication levels.
 */
public enum Authentication {
    /**
     * No authentication is used.
     */
    NONE,

    /**
     * Low authentication is used.
     */
    LOW,

    /**
     * High authentication is used. Because DLMS/COSEM specification does not
     * specify details of the HLS mechanism we have implemented Indian standard.
     * Texas Instruments also uses this.
     */
    HIGH,

    /**
     * High authentication is used. Password is hashed with MD5.
     */
    HIGH_MD5,

    /**
     * High authentication is used. Password is hashed with SHA1.
     */
    HIGH_SHA1,

    /**
     * High authentication is used. Password is hashed with GMAC.
     */
    HIGH_GMAC,

    /**
     * High authentication is used. Password is hashed with SHA-256.
     */
    HIGH_SHA256,

    /**
     * High authentication is used. Password is hashed with ECDSA.
     */
    HIGH_ECDSA;

    /**
     * @return Get integer value for enumeration.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Convert integer for enumeration value.
     * 
     * @param value
     *            Integer value of enumeration.
     * @return Enumerated value from integer.
     */
    public static Authentication forValue(final int value) {
        return values()[value];
    }

    @Override
    public String toString() {
        String str;
        Authentication value = Authentication.forValue(getValue());
        switch (value) {
        case HIGH:
            str = "High";
            break;
        case HIGH_GMAC:
            str = "HighGMac";
            break;
        case HIGH_MD5:
            str = "HighMd5";
            break;
        case HIGH_SHA1:
            str = "HighSha1";
            break;
        case HIGH_SHA256:
            str = "HighSha256";
            break;
        case LOW:
            str = "Low";
            break;
        case NONE:
            str = "None";
            break;
        default:
            str = "";
            break;
        }
        return str;
    }

    public static Authentication valueOfString(final String value) {
        Authentication v = Authentication.NONE;
        if ("None".equalsIgnoreCase(value)) {
            v = Authentication.NONE;
        } else if ("Low".equalsIgnoreCase(value)) {
            v = Authentication.LOW;
        } else if ("High".equalsIgnoreCase(value)) {
            v = Authentication.HIGH;
        } else if ("HighMd5".equalsIgnoreCase(value)) {
            v = Authentication.HIGH_MD5;
        } else if ("HighSha1".equalsIgnoreCase(value)) {
            v = Authentication.HIGH_SHA1;
        } else if ("HighSha256".equalsIgnoreCase(value)) {
            v = Authentication.HIGH_SHA256;
        } else if ("HighGMac".equalsIgnoreCase(value)) {
            v = Authentication.HIGH_GMAC;
        } else {
            throw new IllegalArgumentException(value);
        }
        return v;
    }
}