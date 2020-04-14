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

//
// --------------------------------------------------------------------------
package gurux.dlms;

import gurux.dlms.internal.GXCommon;

/**
 * BitString class is used with Bit strings.
 * 
 * @author Gurux Ltd.
 */
public class GXBitString {

    /**
     * Bit string value.
     */
    private String value;

    /**
     * Constructor.
     */
    public GXBitString() {

    }

    /**
     * Constructor.
     * 
     * @param val
     *            Bit string value.
     */
    public GXBitString(final String val) {
        value = val;
    }

    /**
     * Constructor.
     * 
     * @param val
     *            Byte value.
     * @param count
     *            Bit count.
     */
    public GXBitString(final byte val, final int count) {
        StringBuilder sb = new StringBuilder();
        GXCommon.toBitString(sb, val, 8);
        if (count != 8) {
            value = sb.toString().substring(0, count);
        } else {
            value = sb.toString();
        }
    }

    /**
     * @return Bit string value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param val
     *            Bit string value.
     */
    public void setValue(final String val) {
        value = val;
    }

    @Override
    public final String toString() {
        return value;
    }

    /**
     * @return Bit string value.
     */
    public short toByte() {
        short val = 0;
        if (value != null) {
            int index = 7;
            for (int pos = 0; pos != value.length(); ++pos) {
                if (value.charAt(pos) == '1') {
                    val |= (1 << index);
                } else if (value.charAt(pos) != '0') {
                    throw new IllegalArgumentException("Invalid parameter");
                }
                --index;
            }
        }
        return val;
    }
}
