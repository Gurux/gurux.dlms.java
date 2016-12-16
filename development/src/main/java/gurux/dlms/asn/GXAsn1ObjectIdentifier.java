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

import gurux.dlms.GXByteBuffer;

public class GXAsn1ObjectIdentifier {

    private String objectIdentifier;

    /**
     * Constructor.
     */
    public GXAsn1ObjectIdentifier() {

    }

    /**
     * Constructor.
     * 
     * @param oid
     *            Object identifier in dotted format.
     */
    public GXAsn1ObjectIdentifier(final String oid) {
        objectIdentifier = oid;
    }

    /*
     * Constructor.
     */
    public GXAsn1ObjectIdentifier(final GXByteBuffer bb, final int count) {
        objectIdentifier = oidStringFromBytes(bb, count);
    }

    /**
     * Get OID string from bytes.
     * 
     * @param bb
     *            converted bytes.
     * @param len
     *            byte count.
     * @return OID string.
     */
    private static String oidStringFromBytes(final GXByteBuffer bb,
            final int len) {
        long value = 0;
        StringBuilder sb = new StringBuilder();
        if (len != 0) {
            // Get first byte.
            int tmp = bb.getUInt8();
            sb.append(tmp / 40);
            sb.append('.');
            sb.append(tmp % 40);
            for (int pos = 1; pos != len; ++pos) {
                tmp = bb.getUInt8();
                if ((tmp & 0x80) != 0) {
                    value += (tmp & 0x7F);
                    value <<= 7;
                } else {
                    value += tmp;
                    sb.append('.');
                    sb.append(value);
                    value = 0;
                }
            }
        }
        return sb.toString();
    }

    /**
     * Convert OID string to bytes.
     * 
     * @param oid
     *            OID string.
     */
    static byte[] oidStringtoBytes(final String oid) {
        int value;
        String[] arr = oid.trim().split("[.]");
        // Make first byte.
        GXByteBuffer tmp = new GXByteBuffer();
        value = Integer.parseInt(arr[0]) * 40;
        value += Integer.parseInt(arr[1]);
        tmp.setUInt8(value);
        for (int pos = 2; pos != arr.length; ++pos) {
            value = Integer.parseInt(arr[pos]);
            if (value < 0x80) {
                tmp.setUInt8(value);
            } else if (value < 0x4000) {
                tmp.setUInt8(0x80 | (value >> 7));
                tmp.setUInt8(value & 0x7F);
            } else if (value < 0x200000) {
                tmp.setUInt8(0x80 | (value >> 14));
                tmp.setUInt8(0x80 | (value >> 7));
                tmp.setUInt8(value & 0x7F);
            } else if (value < 0x10000000) {
                tmp.setUInt8(0x80 | (value >> 21));
                tmp.setUInt8(0x80 | (value >> 14));
                tmp.setUInt8(0x80 | (value >> 7));
                tmp.setUInt8(value & 0x7F);
            } else if (value < 0x800000000L) {
                tmp.setUInt8(0x80 | (value >> 49));
                tmp.setUInt8(0x80 | (value >> 42));
                tmp.setUInt8(0x80 | (value >> 35));
                tmp.setUInt8(0x80 | (value >> 28));
                tmp.setUInt8(0x80 | (value >> 21));
                tmp.setUInt8(0x80 | (value >> 14));
                tmp.setUInt8(0x80 | (value >> 7));
                tmp.setUInt8(value & 0x7F);
            } else {
                throw new IllegalArgumentException("Invalid OID.");
            }
        }
        return tmp.array();
    }

    public final String getObjectIdentifier() {
        return objectIdentifier;
    }

    public final void setObjectIdentifier(final String value) {
        objectIdentifier = value;
    }

    @Override
    public final String toString() {
        return objectIdentifier;
    }

    /**
     * @return Object identifier as byte array.
     */
    public final byte[] getEncoded() {
        return oidStringtoBytes(objectIdentifier);
    }
}
