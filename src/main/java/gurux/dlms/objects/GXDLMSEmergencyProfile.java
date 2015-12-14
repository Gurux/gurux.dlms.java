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

import gurux.dlms.GXDateTime;

public class GXDLMSEmergencyProfile {
    private int id;
    private GXDateTime activationTime;
    private long duration;

    public final int getID() {
        return id;
    }

    public final void setID(final int value) {
        id = value;
    }

    public final GXDateTime getActivationTime() {
        return activationTime;
    }

    public final void setActivationTime(final GXDateTime value) {
        activationTime = value;
    }

    public final long getDuration() {
        return duration;
    }

    public final void setDuration(final long value) {
        duration = value;
    }

    @Override
    public final String toString() {
        return String.valueOf(id) + " " + activationTime.toString() + " "
                + String.valueOf(duration);
    }
}