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
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.invoke.MethodHandles.Lookup;
import java.math.BigInteger;
import java.util.Optional;

public class GXUInt64 extends Number
        implements Comparable<GXUInt64>, Constable, ConstantDesc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigInteger value;

    /**
     * A constant holding the minimum value an {@code unsigned short} can have.
     */
    @Native
    public static final long MIN_VALUE = 0x0000000;

    /**
     * A constant holding the maximum value an {@code unsigned short} can have,
     * 2<sup>32</sup>.
     */
    @Native
    public static final long MAX_VALUE = 0xffffffffffffffffL;

    /**
     * Constructor.
     */
    public GXUInt64() {
    }

    /**
     * Constructor.
     * 
     * @param val
     *            Initial value.
     */
    public GXUInt64(final long val) {
        value = BigInteger.valueOf(val);
    }

    /**
     * Constructor.
     */
    public GXUInt64(final BigInteger val) {
        value = val;
    }

    @Override
    public final String toString() {
        return String.valueOf(value);
    }

    @Override
    public int intValue() {
        return value.intValueExact();
    }

    @Override
    public long longValue() {
        return value.longValueExact();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public Object resolveConstantDesc(Lookup lookup)
            throws ReflectiveOperationException {
        return this;
    }

    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.of(this);
    }

    @Override
    public int compareTo(GXUInt64 o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object x) {
        if (x instanceof GXUInt64) {
            return this.value.equals(((GXUInt64) x).value);
        }
        return this.value.equals(x);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}