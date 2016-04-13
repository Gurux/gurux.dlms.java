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

/**
 * Activity Calendar's Day profile is defined on the standard.
 */
public class GXDLMSDayProfile {
    private int dayId;
    private GXDLMSDayProfileAction[] daySchedules;

    /**
     * Constructor.
     */
    public GXDLMSDayProfile() {
    }

    /**
     * Constructor.
     * 
     * @param day
     *            Integer value of the day.
     * @param schedules
     *            Collection of schedules.
     */
    public GXDLMSDayProfile(final int day,
            final GXDLMSDayProfileAction[] schedules) {
        setDayId(day);
        setDaySchedules(schedules);
    }

    /**
     * @return User defined identifier, identifying the current day_profile.
     */
    public final int getDayId() {
        return dayId;
    }

    /**
     * @param value
     *            User defined identifier, identifying the current day_profile.
     */
    public final void setDayId(final int value) {
        dayId = value;
    }

    public final GXDLMSDayProfileAction[] getDaySchedules() {
        return daySchedules;
    }

    public final void setDaySchedules(final GXDLMSDayProfileAction[] value) {
        daySchedules = value;
    }

    @Override
    public final String toString() {
        String str = String.valueOf(dayId);
        for (GXDLMSDayProfileAction it : daySchedules) {
            str += " " + it.toString();
        }
        return str;
    }
}