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
 * X9 object identifiers.
 */
public enum X9ObjectIdentifier implements GXOid {

    //
    // X9.62
    //
    // ansi-X9-62 OBJECT IDENTIFIER ::= { iso(1) member-body(2)
    // us(840) ansi-x962(10045) }
    //

    /**
     * IdFieldType.
     */
    IdFieldType("1.2.840.10045.1"),
    /**
     * PrimeField
     */
    PrimeField("1.2.840.10045.1"),
    /**
     * 
     */
    CharacteristicTwoField("1.2.840.10045.1.2"),
    /**
     * 
     */
    GNBasis("1.2.840.10045.1.2.3.1"),
    /**
     * 
     */
    TPBasis("1.2.840.10045.1.2.3.2"),
    /**
     * 
     */
    PPBasis("1.2.840.10045.1.2.3.3"),

    /**
     * 
     */
    ECDsaWithSha1("1.2.840.10045.4.1"),

    /**
     * 
     */
    IdECPublicKey("1.2.840.10045.2.1"),

    /**
     * 
     */
    ECDsaWithSha2("1.2.840.10045.4.3"),

    /**
     * 
     */
    ECDsaWithSha224("1.2.840.10045.4.31"),
    /**
    * 
    */
    ECDsaWithSha256("1.2.840.10045.4.32"),
    /**
    * 
    */
    ECDsaWithSha384("1.2.840.10045.4.33"),
    /**
    * 
    */
    ECDsaWithSha512("1.2.840.10045.4.34"),

    /**
     * 
    */
    EllipticCurve("1.2.840.10045.3"),

    /**
     * Two Curves
     */
    CTwoCurve("1.2.840.10045.3.0"),

    /**
     * 
     */
    C2Pnb163v1("1.2.840.10045.3.0.1"),
    /**
    * 
    */
    C2Pnb163v2("1.2.840.10045.3.0.2"),
    /**
     * 
     */
    C2Pnb163v3("1.2.840.10045.3.0.3"),
    /**
    * 
    */
    C2Pnb176w1("1.2.840.10045.3.0.4"),
    /**
    * 
    */
    C2Tnb191v1("1.2.840.10045.3.0.5"),
    /**
    * 
    */
    C2Tnb191v2("1.2.840.10045.3.0.6"),
    /**
    * 
    */
    C2Tnb191v3("1.2.840.10045.3.0.7"),
    /**
    * 
    */
    C2Onb191v4("1.2.840.10045.3.0.8"),
    /**
    * 
    */
    C2Onb191v5("1.2.840.10045.3.0.9"),
    /**
    * 
    */
    C2Pnb208w1("1.2.840.10045.3.0.10"),
    /**
    * 
    */
    C2Tnb239v1("1.2.840.10045.3.0.11"),
    /**
    * 
    */
    C2Tnb239v2("1.2.840.10045.3.0.12"),
    /**
    * 
    */
    C2Tnb239v3("1.2.840.10045.3.0.13"),
    /**
    * 
    */
    C2Onb239v4("1.2.840.10045.3.0.14"),
    /**
    * 
    */
    C2Onb239v5("1.2.840.10045.3.0.15"),
    /**
    * 
    */
    C2Pnb272w1("1.2.840.10045.3.0.16"),
    /**
    * 
    */
    C2Pnb304w1("1.2.840.10045.3.0.17"),
    /**
    * 
    */
    C2Tnb359v1("1.2.840.10045.3.0.18"),
    /**
    * 
    */
    C2Pnb368w1("1.2.840.10045.3.0.19"),
    /**
    * 
    */
    C2Tnb431r1("1.2.840.10045.3.0.20"),

    /**
     * Prime
     */
    PrimeCurve("1.2.840.10045.3.1"),
    /**
     * 
     */
    Prime192v1("1.2.840.10045.3.1.1"),
    /**
    * 
    */
    Prime192v2("1.2.840.10045.3.1.2"), Prime192v3("1.2.840.10045.3.1.3"),
    /**
    * 
    */
    Prime239v1("1.2.840.10045.3.1.4"), Prime239v2("1.2.840.10045.3.1.5"),
    /**
    * 
    */
    Prime239v3("1.2.840.10045.3.1.6"), Prime256v1("1.2.840.10045.3.1.7"),

    //
    // DSA
    //
    // dsapublicnumber OBJECT IDENTIFIER ::= { iso(1) member-body(2)
    // us(840) ansi-x957(10040) number-type(4) 1 }
    /**
     * 
     */
    IdDsa("1.2.840.10040.4.1"),

    /**
     * id-dsa-with-sha1 OBJECT IDENTIFIER ::= { iso(1) member-body(2) us(840)
     * x9-57 (10040) x9cm(4) 3 }
     */
    /**
     * 
     */
    IdDsaWithSha1("1.2.840.10040.4.3"),

    /**
     * X9.63
     */
    /**
     * 
     */
    X9x63Scheme("1.3.133.16.840.63.0"),

    /**
     * 
     */
    DHSinglePassStdDHSha1KdfScheme("1.3.133.16.840.63.0.2"),
    /**
     * 
     */
    DHSinglePassCofactorDHSha1KdfScheme("1.3.133.16.840.63.0.3"),
    /**
     * 
    */
    MqvSinglePassSha1KdfScheme("1.3.133.16.840.63.0.16"),

    /**
     * X9.42
     */
    ansi_x9_42("1.2.840.10046"),

    //
    // Diffie-Hellman
    //
    // dhpublicnumber OBJECT IDENTIFIER ::= { iso(1) member-body(2)
    // us(840) ansi-x942(10046) number-type(2) 1 }
    //
    /**
     * 
    */
    DHPublicNumber("1.2.840.10046.2.1"),

    /**
     * 
    */
    X9x42Schemes("1.2.840.10046.2.3"),

    /**
     * 
    */
    DHStatic("1.2.840.10046.2.3.1"),
    /**
    * 
    */
    DHEphem("1.2.840.10046.2.3.2"),
    /**
     * 
    */
    DHOneFlow("1.2.840.10046.2.3.3"),
    /**
    * 
    */
    DHHybrid1("1.2.840.10046.2.3.4"), DHHybrid2("1.2.840.10046.2.3.5"),
    /**
    * 
    */
    DHHybridOneFlow("1.2.840.10046.2.3.6"),
    /**
     * 
    */
    Mqv2("1.2.840.10046.2.3.7"),
    /**
    * 
    */
    Mqv1("1.2.840.10046.2.3.8");

    private String value;
    private static java.util.HashMap<String, X9ObjectIdentifier> mappings;

    private static java.util.HashMap<String, X9ObjectIdentifier> getMappings() {
        synchronized (X9ObjectIdentifier.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<String, X9ObjectIdentifier>();
            }
        }
        return mappings;
    }

    X9ObjectIdentifier(final String mode) {
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
    public static X9ObjectIdentifier forValue(final String value) {
        return getMappings().get(value);
    }
}
