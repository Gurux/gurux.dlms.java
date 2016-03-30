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

package gurux.dlms;

import gurux.dlms.objects.enums.BaudRate;

/**
 * BER encoding enumeration values.
 */
public enum BerType {
    /**
     * End of Content.
     */
    EOC(0x00),

    /**
     * Boolean.
     */
    BOOLEAN(0x1),

    /**
     * Integer.
     */
    INTEGER(0x2),

    /**
     * Bit String.
     */
    BIT_STRING(0x3),

    /**
     * Octet string.
     */
    OCTET_STRING(0x4),

    /**
     * Null value.
     */
    NULL(0x5),

    /**
     * Object identifier.
     */
    OBJECT_IDENTIFIER(0x6),

    /**
     * Object Descriptor.
     */
    OBJECT_DESCRIPTOR(7),

    /**
     * External
     */
    EXTERNAL(8),

    /**
     * Real (float).
     */
    REAL(9),

    /**
     * Enumerated.
     */
    ENUMERATED(10),

    /**
     * Utf8 String.
     */
    UTF8STRING(12),

    /**
     * Numeric string.
     */
    NUMERIC_STRING(18),

    /**
     * Printable string.
     */
    PRINTABLE_STRING(19),

    /**
     * Teletex string.
     */
    TELETEX_STRING(20),

    /**
     * Videotex string.
     */
    VIDEOTEX_STRING(21),

    /**
     * Ia5 string
     */
    IA5_STRING(22),

    /**
     * Utc time.
     */
    UTC_TIME(23),

    /**
     * Generalized time.
     */
    GENERALIZED_TIME(24),

    /**
     * Graphic string.
     */
    GRAPHIC_STRING(25),

    /**
     * Visible string.
     */
    VISIBLE_STRING(26),

    /**
     * General string.
     */
    GENERAL_STRING(27),

    /**
     * Universal string.
     */
    UNIVERSAL_STRING(28),

    /**
     * Bmp string.
     */
    BMP_STRING(30),

    /**
     * Application class.
     */
    APPLICATION(0x40),

    /**
     * Context class.
     */
    CONTEXT(0x80),

    /**
     * Private class.
     */
    PRIVATE(0xc0),

    /**
     * Constructed.
     */
    CONSTRUCTED(0x20);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, BerType> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static java.util.HashMap<Integer, BerType> getMappings() {
        if (mappings == null) {
            synchronized (BaudRate.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, BerType>();
                }
            }
        }
        return mappings;
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Integer value of enumerator.
     */
    BerType(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /**
     * Get integer value for enumerator.
     * 
     * @return Enumerator integer value.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static BerType forValue(final int value) {
        return getMappings().get(value);
    }
}
