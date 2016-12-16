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

import gurux.dlms.internal.GXCommon;

/**
 * ASN1 bit string
 */
public class GXAsn1BitString {

    /**
     * Number of extra bits at the end of the string.
     */
    private int padBits;

    /**
     * Bit string.
     */
    private byte[] value;

    /**
     * Constructor.
     */
    public GXAsn1BitString() {

    }

    /**
     * Append zeroes to the buffer.
     * 
     * @param count
     *            Amount of zero.
     */
    private static void appendZeros(final StringBuilder sb, final int count) {
        for (int pos = 0; pos != count; ++pos) {
            sb.append('0');
        }
    }

    /**
     * Constructor
     * 
     * @param bitString
     *            Bit string.
     */
    public GXAsn1BitString(final String bitString) {
        padBits = 8 - (bitString.length() % 8);
        if (padBits == 8) {
            padBits = 0;
        }
        StringBuilder sb = new StringBuilder(bitString);
        appendZeros(sb, padBits);
        value = new byte[sb.length() / 8];
        for (int pos = 0; pos != value.length; ++pos) {
            value[pos] = (byte) Integer
                    .parseInt(sb.substring(8 * pos, 8 * (pos + 1)), 2);
        }
    }

    /**
     * Constructor
     * 
     * @param str
     *            Bit string.
     * @param padCount
     *            Number of extra bits at the end of the string.
     */
    public GXAsn1BitString(final byte[] str, final int padCount) {
        if (str == null) {
            throw new IllegalArgumentException("data");
        }
        if (padBits < 0 || padBits > 7) {
            throw new IllegalArgumentException(
                    "PadCount must be in the range 0 to 7");
        }
        value = str;
        padBits = padCount;
    }

    /**
     * Constructor
     * 
     * @param str
     *            Bit string.
     */
    public GXAsn1BitString(final byte[] str) {
        if (str == null) {
            throw new IllegalArgumentException("data");
        }
        padBits = str[0];
        if (padBits < 0 || padBits > 7) {
            throw new IllegalArgumentException(
                    "PadCount must be in the range 0 to 7");
        }
        value = new byte[str.length - 1];
        System.arraycopy(str, 1, value, 0, str.length - 1);
    }

    /**
     * @return Bit string.
     */
    public final byte[] getValue() {
        return value;
    }

    /**
     * @return Number of extra bits at the end of the string.
     */
    public final int getPadBits() {
        return padBits;
    }

    @Override
    public final String toString() {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(8 * value.length);
        for (byte it : value) {
            GXCommon.toBitString(sb, it, 8);
        }
        sb.setLength(sb.length() - padBits);
        return String.valueOf((8 * value.length) - padBits) + " bit "
                + sb.toString();
    }

    public final String asString() {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(8 * value.length);
        for (byte it : value) {
            GXCommon.toBitString(sb, it, 8);
        }
        sb.setLength(sb.length() - padBits);
        return sb.toString();
    }

}
