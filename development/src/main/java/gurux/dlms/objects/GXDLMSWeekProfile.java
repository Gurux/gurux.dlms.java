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

import gurux.dlms.internal.GXCommon;

public class GXDLMSWeekProfile {
    private byte[] name;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;

    /**
     * Constructor.
     */
    public GXDLMSWeekProfile() {
    }

    public final byte[] getName() {
        return name;
    }

    public final void setName(final String value) {
        if (value == null) {
            name = null;
        } else {
            name = value.getBytes();
        }
    }

    public final void setName(final byte[] value) {
        name = value;
    }

    public final int getMonday() {
        return monday;
    }

    public final void setMonday(final int value) {
        monday = value;
    }

    public final int getTuesday() {
        return tuesday;
    }

    public final void setTuesday(final int value) {
        tuesday = value;
    }

    public final int getWednesday() {
        return wednesday;
    }

    public final void setWednesday(final int value) {
        wednesday = value;
    }

    public final int getThursday() {
        return thursday;
    }

    public final void setThursday(final int value) {
        thursday = value;
    }

    public final int getFriday() {
        return friday;
    }

    public final void setFriday(final int value) {
        friday = value;
    }

    public final int getSaturday() {
        return saturday;
    }

    public final void setSaturday(final int value) {
        saturday = value;
    }

    public final int getSunday() {
        return sunday;
    }

    public final void setSunday(final int value) {
        sunday = value;
    }

    @Override
    public final String toString() {
        if (name == null) {
            return null;
        }
        return GXCommon.toHex(name);
    }
}
