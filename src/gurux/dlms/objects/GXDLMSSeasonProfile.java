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

public class GXDLMSSeasonProfile
{
    private String Name;
    private GXDateTime Start;  
    private String WeekName;
    
    /** 
     Constructor.
    */
    public GXDLMSSeasonProfile()
    {

    }

    /** 
     Constructor.
    */
    public GXDLMSSeasonProfile(String name, GXDateTime start, String weekName)
    {
        setName(name);
        setStart(start);
        setWeekName(weekName);
    }

    /** 
     Name of season profile.
    */
    public final String getName()
    {
        return Name;
    }
    public final void setName(String value)
    {
        Name = value;
    }

    /** 
     Season Profile start time.
    */    
    public final GXDateTime getStart()
    {
        return Start;
    }
    public final void setStart(GXDateTime value)
    {
        Start = value;
    }

    /** 
     Week name of season profile.
    */    
    public final String getWeekName()
    {
        return WeekName;
    }
    public final void setWeekName(String value)
    {
        WeekName = value;
    }
    
    @Override
    public String toString()
    {
        return Name + " " + Start.toString();
    }
}