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

package gurux.dlms.objects;

public class GXDLMSGSMCellInfo {
    /**
     * Four-byte cell ID.
     */
    private long cellId;

    /**
     * Bit Error Rate.
     */
    private int ber;

    /**
     * Two byte location area code (LAC).
     */
    private int locationId;
    /**
     * Signal quality.
     */
    private int signalQuality;

    /**
     * @return Four-byte cell ID.
     */
    public final long getCellId() {
        return cellId;
    }

    /**
     * @param value
     *            Four-byte cell ID.
     */
    public final void setCellId(final long value) {
        cellId = value;
    }

    /**
     * @return Two byte location area code (LAC).
     */
    public final int getLocationId() {
        return locationId;
    }

    /**
     * @param value
     *            Two byte location area code (LAC).
     */
    public final void setLocationId(final int value) {
        locationId = value;
    }

    /**
     * @return Signal quality.
     */
    public final int getSignalQuality() {
        return signalQuality;
    }

    /**
     * @param value
     *            Signal quality.
     */
    public final void setSignalQuality(final int value) {
        signalQuality = value;
    }

    /**
     * @return Bit Error Rate.
     */
    public final int getBer() {
        return ber;
    }

    /**
     * @param value
     *            Bit Error Rate.
     */
    public final void setBer(final int value) {
        ber = value;
    }

}