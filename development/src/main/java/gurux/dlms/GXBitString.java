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

package gurux.dlms;

import gurux.dlms.internal.GXCommon;

/**
 * Bit string class is used with Bit strings.
 */
public class GXBitString {

    /**
     * Number of extra bits at the end of the string.
     */
    private int padBits;

    /**
     * Bit string.
     */
    private byte[] _value;

    /**
     * @return Number of extra bits at the end of the string.
     */
    public final int getPadBits() {
        return padBits;
    }

    /**
     * @return Bit string.
     */
    public final byte[] getValue() {
        return _value;
    }

    /**
     * @return Number of extra bits at the end of the string.
     */
    public final int length() {
        if (_value == null) {
            return 0;
        }
        return (8 * _value.length) - padBits;
    }

    /**
     * Constructor.
     */
    public GXBitString() {

    }

    /**
     * Constructor
     * 
     * @param bitString
     *            Bit string.
     */
    public GXBitString(final String bitString) {
        padBits = 8 - (bitString.length() % 8);
        if (padBits == 8) {
            padBits = 0;
        }
        StringBuilder sb = new StringBuilder(bitString);
        appendZeros(sb, padBits);
        _value = new byte[sb.length() / 8];
        for (int pos = 0; pos != _value.length; ++pos) {
            _value[pos] = (byte) Integer.parseInt(sb.substring(8 * pos, 8 * (pos + 1)), 2);
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
    public GXBitString(final byte[] str, final int padCount) {
        if (str == null) {
            throw new IllegalArgumentException("data");
        }
        if (padBits < 0 || padBits > 7) {
            throw new IllegalArgumentException("PadCount must be in the range 0 to 7");
        }
        _value = str;
        padBits = padCount;
    }

    /**
     * Constructor
     * 
     * @param str
     *            Bit string.
     */
    public GXBitString(final byte[] str) {
        if (str == null) {
            throw new IllegalArgumentException("data");
        }
        padBits = str[0];
        if (padBits < 0 || padBits > 7) {
            throw new IllegalArgumentException("PadCount must be in the range 0 to 7");
        }
        _value = new byte[str.length - 1];
        System.arraycopy(str, 1, _value, 0, str.length - 1);
    }

    /**
     * Constructor
     * 
     * @param value
     *            Interer value.
     * @param count
     *            Amount of the bits.
     */
    public GXBitString(final long value, final int count) {
        long tmp = value;
        padBits = count % 8;
        _value = new byte[count / 8 + padBits];
        for (byte pos = 0; pos != _value.length; ++pos) {
            _value[pos] = (byte) (GXCommon.swapBits((byte) tmp));
            tmp >>= 8;
        }
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

    @Override
    public final String toString() {
        return toString(false);
    }

    /**
     * Convert bit string to string.
     * 
     * @param showBits
     *            Is the number of the bits shown.
     * @return Bit string as an string.
     */
    public final String toString(final boolean showBits) {
        if (_value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(8 * _value.length);
        for (byte it : _value) {
            GXCommon.toBitString(sb, it, 8);
        }
        sb.setLength(sb.length() - padBits);
        if (showBits) {
            sb.insert(0, String.valueOf((8 * _value.length) - padBits) + " bit ");
        }
        return sb.toString();
    }

    /**
     * @return Bit-string value as integer.
     */
    public int toInteger() {
        int ret = 0;
        if (_value != null) {
            int bytePos = 0;
            for (byte it : _value) {
                ret |= (int) (GXCommon.swapBits(it) << bytePos);
                bytePos += 8;
            }
        }
        return ret;
    }
}
