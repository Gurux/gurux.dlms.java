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

package gurux.dlms.asn;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Enumerate values that are add to counted GMAC.
 */
public final class GXx509CertificateCollection
        extends ArrayList<GXx509Certificate>
        implements java.util.List<GXx509Certificate> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Find certificate using serial information.
     * 
     * @param serialNumber
     *            Serial number.
     * @param issuer
     *            Issuer.
     * @return x509Certificate
     */
    public GXx509Certificate findCertificateBySerial(final byte[] serialNumber,
            final String issuer) {
        for (GXx509Certificate it : this) {
            if (Arrays.equals(it.getSerialNumber().getByteArray(), serialNumber)
                    && it.getIssuer().equalsIgnoreCase(issuer)) {
                return it;
            }
        }
        return null;
    }
}