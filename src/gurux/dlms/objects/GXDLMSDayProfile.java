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
 Activity Calendar's Day profile is defined on the standard.
 */
public class GXDLMSDayProfile
{
    private int DayId;
    private GXDLMSDayProfileAction[] DaySchedules;    
    
    /** 
     Constructor.
    */
    public GXDLMSDayProfile()
    {
    }

    /** 
     Constructor.
    */
    public GXDLMSDayProfile(int dayId, GXDLMSDayProfileAction[] schedules)
    {
        setDayId(dayId);
        setDaySchedules(schedules);
    }

    /** 
     User defined identifier, identifying the currentday_profile.
    */
    public final int getDayId()
    {
        return DayId;
    }
    public final void setDayId(int value)
    {
        DayId = value;
    }
    
    public final GXDLMSDayProfileAction[] getDaySchedules()
    {
        return DaySchedules;
    }
    public final void setDaySchedules(GXDLMSDayProfileAction[] value)
    {
        DaySchedules = value;
    }
    
    @Override
    public String toString()
    {
        String str = String.format("%d", DayId);
        for (GXDLMSDayProfileAction it : DaySchedules)
        {
            str += " " + it.toString();
        }
        return str;
    }
}