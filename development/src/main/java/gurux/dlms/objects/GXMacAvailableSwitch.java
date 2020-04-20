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

package gurux.dlms.objects;

/**
 * MAC available switch.
 */
public class GXMacAvailableSwitch {
    /**
     * EUI-48 of the subnetwork.
     */
    private byte[] sna;

    /**
     * SID of this switch.
     */
    private short lsId;

    /**
     * Level of this switch in subnetwork hierarchy.
     */
    private byte level;

    /**
     * The received signal level for this switch.
     */
    private byte rxLevel;
    /**
     * The signal to noise ratio for this switch.
     */
    private byte rxSnr;

    /**
     * @return EUI-48 of the subnetwork.
     */
    public final byte[] getSna() {
        return sna;
    }

    /**
     * @param value
     *            EUI-48 of the subnetwork.
     */
    public final void setSna(final byte[] value) {
        sna = value;
    }

    /**
     * @return SID of this switch.
     */
    public final short getLsId() {
        return lsId;
    }

    /**
     * @param value
     *            SID of this switch.
     */
    public final void setLsId(short value) {
        lsId = value;
    }

    /**
     * @return Level of this switch in subnetwork hierarchy.
     */
    public final byte getLevel() {
        return level;
    }

    /**
     * @param value
     *            Level of this switch in subnetwork hierarchy.
     */
    public final void setLevel(final byte value) {
        level = value;
    }

    /**
     * @return The received signal level for this switch;
     */
    public final byte getRxLevel() {
        return rxLevel;
    }

    /**
     * @param value
     *            The received signal level for this switch;
     */
    public final void setRxLevel(final byte value) {
        rxLevel = value;
    }

    /**
     * @return The signal to noise ratio for this switch.
     */
    public final byte getRxSnr() {
        return rxSnr;
    }

    /**
     * @param value
     *            The signal to noise ratio for this switch.
     */
    public final void setRxSnr(final byte value) {
        rxSnr = value;
    }

}