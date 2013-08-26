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

public class GXDLMSWeekProfile
{
    private String Name;
    private int Monday;
    private int Tuesday;
    private int Wednesday;
    private int Thursday;
    private int Friday;
    private int Saturday;
    private int Sunday;    

    /** 
     Constructor.
    */
    public GXDLMSWeekProfile()
    {
    }

    /** 
     Constructor.
    */
    public GXDLMSWeekProfile(String name, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday)
    {
        setName(name);
        setMonday(monday);
        setTuesday(tuesday);
        setWednesday(wednesday);
        setThursday(thursday);
        setFriday(friday);
        setSaturday(saturday);
        setSunday(sunday);
    }

    public final String getName()
    {
        return Name;
    }
    public final void setName(String value)
    {
        Name = value;
    }
    public final int getMonday()
    {
        return Monday;
    }
    public final void setMonday(int value)
    {
        Monday = value;
    }

    public final int getTuesday()
    {
        return Tuesday;
    }
    public final void setTuesday(int value)
    {
        Tuesday = value;
    }

    public final int getWednesday()
    {
        return Wednesday;
    }
    public final void setWednesday(int value)
    {
        Wednesday = value;
    }

    public final int getThursday()
    {
        return Thursday;
    }
    public final void setThursday(int value)
    {
        Thursday = value;
    }

    public final int getFriday()
    {
        return Friday;
    }
    public final void setFriday(int value)
    {
        Friday = value;
    }

    public final int getSaturday()
    {
        return Saturday;
    }
    public final void setSaturday(int value)
    {
        Saturday = value;
    }

    public final int getSunday()
    {
        return Sunday;
    }
    public final void setSunday(int value)
    {
        Sunday = value;
    }
    
    @Override
    public String toString()
    {
        return Name;
    }
}
