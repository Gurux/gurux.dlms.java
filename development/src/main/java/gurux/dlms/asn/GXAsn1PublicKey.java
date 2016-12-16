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

import java.security.PublicKey;

import gurux.dlms.internal.GXCommon;

/**
 * ASN1 Public key.
 */
public class GXAsn1PublicKey {
    /**
     * Public key.
     */
    private byte[] value;

    private void init(final byte[] key) {
        if (key == null || key.length != 270) {
            throw new IllegalArgumentException("data");
        }
        value = new byte[key.length];
        System.arraycopy(key, 0, value, 0, key.length);
    }

    /**
     * Constructor.
     */
    public GXAsn1PublicKey() {

    }

    /**
     * Constructor
     * 
     * @param key
     *            Public key.
     */
    public GXAsn1PublicKey(final PublicKey key) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }
        Object[] data =
                (Object[]) GXAsn1Converter.fromByteArray(key.getEncoded());
        /*
         * GXByteBuffer bb = new GXByteBuffer(); bb.set(((GXAsn1BitString)
         * GXAsn1Converter.getDerValue(data[1])) .getValue());
         * init((GXAsn1BitString) GXAsn1Converter.getDerValue(data[1])); //
         * init(GXAsn1Converter.toByteArray( // new Object[] {
         * GXAsn1Converter.getDerValue(data[0]), //
         * GXAsn1Converter.getDerValue(data[1]) }));
         */

    }

    /**
     * Constructor for RSA Public Key (PKCS#1). This is read from PEM file.
     * 
     * @param data
     *            (PKCS#1) Public key.
     */
    public GXAsn1PublicKey(final GXAsn1BitString data) {
        if (data == null) {
            throw new IllegalArgumentException("key");
        }
        GXAsn1Sequence seq =
                (GXAsn1Sequence) GXAsn1Converter.fromByteArray(data.getValue());
        init(GXAsn1Converter
                .toByteArray(new Object[] { seq.get(0), seq.get(1) }));
    }

    /**
     * Constructor
     * 
     * @param key
     *            Public key.
     */
    public GXAsn1PublicKey(final byte[] key) {
        init(key);
    }

    /**
     * @return Public key as byte array.
     */
    public final byte[] getValue() {
        return value;
    }

    @Override
    public final String toString() {
        if (value == null) {
            return "";
        }
        return GXCommon.toHex(value);
    }
}
