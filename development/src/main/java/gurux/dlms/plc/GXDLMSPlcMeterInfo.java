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

package gurux.dlms.plc;

/**
 * Information from the discovered PLC meter(s).
 */
public class GXDLMSPlcMeterInfo {
    /**
     * Source Address.
     */
    private int sourceAddress;

    /**
     * Destination Address.
     */
    private int destinationAddress;
    /**
     * System title.
     */
    private byte[] systemTitle;
    /**
     * Alarm descriptor.
     */
    private short alarmDescriptor;

    public final int getSourceAddress() {
        return sourceAddress;
    }

    public final void setSourceAddress(final int value) {
        sourceAddress = value;
    }

    /**
     * @return Destination Address.
     */
    public final int getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param value
     *            Destination Address.
     */
    public final void setDestinationAddress(final int value) {
        destinationAddress = value;
    }

    /**
     * @return System title.
     */
    public final byte[] getSystemTitle() {
        return systemTitle;
    }

    /**
     * @param value
     *            System title.
     */
    public final void setSystemTitle(byte[] value) {
        systemTitle = value;
    }

    /**
     * @return Alarm descriptor.
     */
    public final short getAlarmDescriptor() {
        return alarmDescriptor;
    }

    /**
     * @param value
     *            Alarm descriptor.
     */
    public final void setAlarmDescriptor(final short value) {
        alarmDescriptor = value;
    }
}