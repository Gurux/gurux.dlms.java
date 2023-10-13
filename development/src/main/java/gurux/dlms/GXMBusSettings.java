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

import java.math.BigInteger;

/**
 * M-Bus settings contains communication information when DLMS PDU is
 * transported using M-Bus frames.
 */
public class GXMBusSettings {
    /**
     * Device identification number.
     */
    private BigInteger id;

    /**
     * Manufacturer Id.
     */
    private String manufacturerId;

    /**
     * Version.
     */
    private byte version;

    /**
     * Device type.
     */
    private MBusMeterType meterType;

    /**
     * @return Device identification number.
     */
    public final BigInteger getId() {
        return id;
    }

    /**
     * @param value
     *            Device identification number.
     */
    public final void setId(final BigInteger value) {
        id = value;
    }

    public final String getManufacturerId() {
        return manufacturerId;
    }

    public final void setManufacturerId(String value) {
        manufacturerId = value;
    }

    /**
     * @return Version.
     */
    public final byte getVersion() {
        return version;
    }

    /**
     * @param value
     *            Version.
     */
    public final void setVersion(final byte value) {
        version = value;
    }

    /**
     * @return Device type.
     */
    public final MBusMeterType getMeterType() {
        return meterType;
    }

    /**
     * @param value
     *            Device type.
     */
    public final void setMeterType(final MBusMeterType value) {
        meterType = value;
    }
}