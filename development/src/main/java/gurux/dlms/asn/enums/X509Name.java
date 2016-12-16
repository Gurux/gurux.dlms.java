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
 * X509 names.
 */
public enum X509Name {

    /**
     * country code - StringType(SIZE(2))
     */
    C("2.5.4.6"),

    /**
     * organization - StringType(SIZE(1..64))
     */
    O("2.5.4.10"),

    /**
     * organizational unit name - StringType(SIZE(1..64))
     */
    OU("2.5.4.11"),

    /**
     * Title
     */
    T("2.5.4.12"),

    /**
     * common name - StringType(SIZE(1..64))
     */
    CN("2.5.4.3"),

    /**
     * street - StringType(SIZE(1..64))
     */
    STREET("2.5.4.9"),

    /**
     * device serial number name - StringType(SIZE(1..64))
     */
    SERIAL_NUMBER("2.5.4.5"),

    /**
     * locality name - StringType(SIZE(1..64))
     */
    L("2.5.4.7"),

    /**
     * state, or province name - StringType(SIZE(1..64))
     */
    ST("2.5.4.8"),

    /**
     * Naming attributes of type X520name
     */
    SUR_NAME("2.5.4.4"),
    /**
     * Given name.
     */
    GIVEN_NAME("2.5.4.42"),

    /**
     * Initials.
     */
    INITIALS("2.5.4.43"),

    /**
     * Generation.
     */
    GENERATION("2.5.4.44"),

    /**
     * Unique identifier.
     */
    UNIQUE_IDENTIFIER("2.5.4.45"),

    /**
     * businessCategory - DirectoryString(SIZE(1..128))
     */
    BUSINESS_CATEGORY("2.5.4.15"),

    /**
     * postalCode - DirectoryString(SIZE(1..40))
     */
    POSTAL_CODE("2.5.4.17"),

    /**
     * dnQualifier - DirectoryString(SIZE(1..64))
     */
    DN_QUALIFIER("2.5.4.46"),

    /**
     * RFC 3039 Pseudonym - DirectoryString(SIZE(1..64))
     */
    PSEUDONYM("2.5.4.65"),

    /**
     * RFC 3039 DateOfBirth - GeneralizedTime - YYYYMMDD000000Z
     */
    DATE_OF_BIRTH("1.3.6.1.5.5.7.9.1"),

    /**
     * RFC 3039 PlaceOfBirth - DirectoryString(SIZE(1..128))
     */
    PLACE_OF_BIRTH("1.3.6.1.5.5.7.9.2"),

    /**
     * RFC 3039 DateOfBirth - PrintableString (SIZE(1 -- "M", "F", "m" or "f")
     */
    GENDER("1.3.6.1.5.5.7.9.3"),

    /**
     * RFC 3039 CountryOfCitizenship - PrintableString (SIZE (2 -- ISO 3166))
     * codes only
     */
    COUNTRY_OF_CITIZENSHIP("1.3.6.1.5.5.7.9.4"),

    /**
     * RFC 3039 CountryOfCitizenship - PrintableString (SIZE (2 -- ISO 3166))
     * codes only
     */
    COUNTRY_OF_RESIDENCE("1.3.6.1.5.5.7.9.5"),

    /**
     * ISIS-MTT NameAtBirth - DirectoryString(SIZE(1..64))
     */
    NAME_AT_BIRTH("1.3.36.8.3.14"),

    /**
     * RFC 3039 PostalAddress - SEQUENCE SIZE (1..6 OF
     * DirectoryString(SIZE(1..30)))
     */
    POSTAL_ADDRESS("2.5.4.16"),

    /**
     * RFC 2256 dmdName
     */
    DMD_NAME("2.5.4.54"),

    /**
     * id-at-telephoneNumber
     */
    TelephoneNumber("2.5.4.20"),

    /**
     * id-at-name
     */
    Name("2.5.4.41"),

    /**
     * email address in Verisign certificates
     */
    E("1.2.840.113549.1.9.1"),

    DC("0.9.2342.19200300.100.1.25"),

    /**
     * LDAP User id.
     */
    UID("0.9.2342.19200300.100.1.1");

    private String value;
    private static java.util.HashMap<String, X509Name> mappings;

    private static java.util.HashMap<String, X509Name> getMappings() {
        synchronized (X509Name.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<String, X509Name>();
            }
        }
        return mappings;
    }

    X509Name(final String mode) {
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
    public static X509Name forValue(final String value) {
        return getMappings().get(value);
    }
}
