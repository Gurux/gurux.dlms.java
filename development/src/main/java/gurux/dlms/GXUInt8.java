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

import java.lang.annotation.Native;

public class GXUInt8 extends Number implements Comparable<GXUInt8> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private short _value;

    /**
     * A constant holding the minimum value an {@code byte} can have.
     */
    @Native
    public static final short MIN_VALUE = 0x0000000;

    /**
     * A constant holding the maximum value an {@code byte} can have,
     * 2<sup>8</sup>.
     */
    @Native
    public static final short MAX_VALUE = 0xff;

    /**
     * Constructor.
     */
    public GXUInt8() {
    }

    /**
     * Constructor.
     * 
     * @param val
     *            Initial value.
     */
    public GXUInt8(final short val) {
        _value = val;
    }

    @Override
    public final String toString() {
        return String.valueOf(_value);
    }

    @Override
    public int intValue() {
        return (int) _value;
    }

    @Override
    public long longValue() {
        return _value;
    }

    @Override
    public float floatValue() {
        return _value;
    }

    @Override
    public double doubleValue() {
        return _value;
    }

    @Override
    public int compareTo(GXUInt8 o) {
        return compare(this._value, o._value);
    }

    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    @Override
    public boolean equals(Object x) {
        if (x instanceof GXUInt8) {
            return ((Short) _value).equals(((GXUInt8) x)._value);
        }
        return ((Short) _value).equals(x);
    }

    @Override
    public int hashCode() {
        return ((Short) _value).hashCode();
    }
}