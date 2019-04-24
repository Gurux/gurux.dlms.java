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
 * GXDLMSGateway contains information that is needed if gateway is used between
 * the client and the meter.
 */
public class GXDLMSGateway {
    /**
     * Gateway network ID.
     */
    private short networkId;

    /**
     * Physical device address.
     */
    private byte[] physicalDeviceAddress;

    /**
     * Constructor.
     */
    public GXDLMSGateway() {
    }

    /**
     * @return Gateway network ID.
     */
    public final short getNetworkId() {
        return networkId;
    }

    /**
     * @param value
     *            Gateway network ID.
     */
    public final void setNetworkId(final short value) {
        networkId = value;
    }

    /**
     * @return Physical device address.
     */
    public final byte[] getPhysicalDeviceAddress() {
        return physicalDeviceAddress;
    }

    /**
     * @param value
     *            Physical device address.
     */
    public final void setPhysicalDeviceAddress(final byte[] value) {
        physicalDeviceAddress = value;
    }
}