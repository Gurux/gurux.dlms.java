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

/**
 * GXDeltaInt16 class presents delta Int16 value.
 */
public class GXDeltaInt16 extends Number implements Comparable<GXDeltaInt16> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Delta value.
     */
    private short _value;

    /**
     * Constructor.
     */
    public GXDeltaInt16() {
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Date value.
     */
    public GXDeltaInt16(final short value) {
        _value = value;
    }

    /**
     * @return Delta value.
     */
    public short get_value() {
        return _value;
    }

    /**
     * @param value
     *            Delta value.
     */
    public void set_value(final short value) {
        _value = value;
    }

    @Override
    public int intValue() {
        return _value;
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
    public int compareTo(GXDeltaInt16 o) {
        return compare(this._value, o._value);
    }

    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    @Override
    public boolean equals(Object x) {
        if (x instanceof GXDeltaInt16) {
            return ((Short) _value).equals(((GXDeltaInt16) x)._value);
        }
        return ((Short) _value).equals(x);
    }

    @Override
    public int hashCode() {
        return ((Short) _value).hashCode();
    }
}