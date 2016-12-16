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

package gurux.dlms.asn;

import java.util.HashMap;

import gurux.dlms.enums.BerType;

final class GXAsn1Settings {
    private int count;
    private final StringBuffer sb = new StringBuffer();
    private HashMap<Short, String> tags = new HashMap<Short, String>();
    private HashMap<String, Short> tagbyName = new HashMap<String, Short>();

    private void addTag(final int key, final String value) {
        tags.put(new Short((short) key), value);
        tagbyName.put(value.toLowerCase(), new Short((short) key));
    }

    /**
     * Constructor.
     */
    GXAsn1Settings() {
        addTag(BerType.APPLICATION, "Application");
        addTag(BerType.CONSTRUCTED | BerType.CONTEXT, "Context");
        addTag(BerType.CONSTRUCTED | BerType.SEQUENCE, "Sequence");
        addTag(BerType.CONSTRUCTED | BerType.SET, "Set");
        addTag(BerType.OBJECT_IDENTIFIER, "ObjectIdentifier");
        addTag(BerType.PRINTABLE_STRING, "String");
        addTag(BerType.UTF8STRING, "UTF8");
        addTag(BerType.IA5_STRING, "IA5");
        addTag(BerType.INTEGER, "Integer");
        addTag(BerType.NULL, "Null");
        addTag(BerType.BIT_STRING, "BitString");
        addTag(BerType.UTC_TIME, "UtcTime");
        addTag(BerType.GENERALIZED_TIME, "GeneralizedTime");
        addTag(BerType.OCTET_STRING, "OctetString");
        addTag(-1, "Byte");
        addTag(-2, "Short");
        addTag(-4, "Int");
        addTag(-8, "Long");
    }

    public String getTag(final short value) {
        return tags.get(value);
    }

    public short getTag(final String value) {
        return tagbyName.get(value);
    }

    /**
     * Append spaces to the buffer.
     * 
     * @param count
     *            Amount of spaces.
     */
    public void appendSpaces() {
        for (int pos = 0; pos != count; ++pos) {
            sb.append(' ');
        }
    }

    public void append(final String value) {
        sb.append(value);
    }

    public void increase() {
        ++count;
        append("\r\n");
    }

    public void decrease() {
        --count;
        appendSpaces();
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
