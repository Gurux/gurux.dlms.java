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

package gurux.dlms.asn.enums;

/**
 * x509 Certificate.
 */
public enum X509Certificate {
    UNKNOWN(""), OLD_AUTHORITY_KEY_IDENTIFIER("2.5.29.1"),
    OLD_PRIMARY_KEY_ATTRIBUTES("2.5.29.2"), CERTIFICATE_POLICIES("2.5.29.3"),
    PRIMARY_KEY_USAGE_RESTRICTION("2.5.29.4"),
    SUBJECT_DIRECTORY_ATTRIBUTES("2.5.29.9"),
    SUBJECT_KEY_IDENTIFIER("2.5.29.14"), KEY_USAGE("2.5.29.15"),
    PRIVATE_KEY_USAGE_PERIOD("2.5.29.16"),
    SUBJECT_ALTERNATIVE_NAME("2.5.29.17"), ISSUER_ALTERNATIVE_NAME("2.5.29.18"),
    BASIC_CONSTRAINTS("2.5.29.19"), CRL_NUMBER("2.5.29.20"),
    REASON_CODE("2.5.29.21"), HOLD_INSTRUCTION_CODE("2.5.29.23"),
    INVALIDITY_DATE("2.5.29.24"), DELTA_CRL_INDICATOR("2.5.29.27"),
    ISSUING_DISTRIBUTION_POINT("2.5.29.28"), CERTIFICATE_ISSUER("2.5.29.29"),
    NAME_CONSTRAINTS("2.5.29.30"), CRL_DISTRIBUTION_POINTS("2.5.29.31"),
    CERTIFICATE_POLICIES_2("2.5.29.32"), POLICY_MAPPINGS("2.5.29.33"),
    AUTHORITY_KEY_IDENTIFIER("2.5.29.35"), POLICY_CONSTRAINTS("2.5.29.36"),
    EXTENDED_KEY_USAGE("2.5.29.37"), FRESHEST_CRL("2.5.29.46");

    private String value;
    private static java.util.HashMap<String, X509Certificate> mappings;

    private static java.util.HashMap<String, X509Certificate> getMappings() {
        synchronized (X509Certificate.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<String, X509Certificate>();
            }
        }
        return mappings;
    }

    X509Certificate(final String mode) {
        value = mode;
        getMappings().put(new String(mode), this);
    }

    /*
     * Get string value for enum.
     */
    public final String getValue() {
        return value;
    }

    /*
     * Convert string for enum value.
     */
    public static X509Certificate forValue(final String value) {
        X509Certificate ret = getMappings().get(value);
        if (ret == null) {
            ret = X509Certificate.UNKNOWN;
        }
        return ret;
    }
}
