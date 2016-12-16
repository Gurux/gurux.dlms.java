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

package gurux.dlms.asn.enums;

/**
 * Hash algorithms.
 */
public enum HashAlgorithm implements GXOid {

    /*
     * SHA1 RSA.
     */
    SHA1_RSA("1.2.840.113549.1.1.5"),
    /*
     * 
     */
    MD5_RSA("1.2.840.113549.1.1.4"),
    /*
     * 
     */
    SHA1_DSA("1.2.840.10040.4.3"),
    /*
     * 
     */
    SHA1_RSA_1("1.3.14.3.2.29"),
    /*
     * 
     */
    SHA_RSA("1.3.14.3.2.15"),
    /*
     * 
     */
    MD5_RSA_1("1.3.14.3.2.3"),
    /*
     * 
     */
    MD2_RSA_1("1.2.840.113549.1.1.2"),

    /*
     * 
     */
    MD4_RSA("1.2.840.113549.1.1.3"),

    /*
     * 
     */
    MD4_RSA_1("1.3.14.3.2.2"),

    /*
     * 
     */
    MD4_RSA_2("1.3.14.3.2.4"),

    /*
     * 
     */
    MD2_RSA("1.3.14.7.2.3.1"),

    /*
     * 
     */
    SHA1_DSA_1("1.3.14.3.2.13"),
    /*
     * 
     */
    DSA_SHA1("1.3.14.3.2.27"),

    /*
     * 
     */
    MOSAIC_UPDATED_SIG("2.16.840.1.101.2.1.1.19"),
    /*
     * 
     */
    SHA1_NO_SIGN("1.3.14.3.2.26"),
    /*
     * 
     */
    MD5_NO_SIGN("1.2.840.113549.2.5"),
    /*
     * 
     */
    SHA_256_NO_SIGN("2.16.840.1.101.3.4.2.1"),
    /*
     * 
     */
    SHA_384_NO_SIGN("2.16.840.1.101.3.4.2.2"),
    /*
     * 
     */
    SHA_512_NO_SIGN("2.16.840.1.101.3.4.2.3"),
    /*
     *
     */
    SHA_256_RSA("1.2.840.113549.1.1.11"),
    /*
     * 
     */
    SHA_384_RSA("1.2.840.113549.1.1.12"),
    /*
     * 
     */
    SHA_512_RSA("1.2.840.113549.1.1.13"),
    /*
     * 
     */
    RSA_SSA_PSS("1.2.840.113549.1.1.10"),

    /*
     * 
     */
    SHA1withECDSA("1.2.840.10045.4.1"),

    /*
     * 
     */
    SHA256withECDSA("1.2.840.10045.4.3.2"),
    /*
     * 
     */
    SHA384withECDSA("1.2.840.10045.4.3.3"),
    /*
     * 
     */
    SHA512withECDSA("1.2.840.10045.4.3.4"),
    /*
     * 
     */
    SPECIFIED_ECDSA("1.2.840.10045.4.3");

    private String value;
    private static java.util.HashMap<String, HashAlgorithm> mappings;

    private static java.util.HashMap<String, HashAlgorithm> getMappings() {
        synchronized (HashAlgorithm.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<String, HashAlgorithm>();
            }
        }
        return mappings;
    }

    HashAlgorithm(final String mode) {
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
    public static HashAlgorithm forValue(final String value) {
        return getMappings().get(value);
    }
}
