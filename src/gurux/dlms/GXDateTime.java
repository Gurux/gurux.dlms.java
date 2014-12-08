//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: http://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms;

import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

public class GXDateTime
{
    private ClockStatus Status;
    private java.util.Date Value = new java.util.Date(0);
    private java.util.Set<DateTimeSkips> Skip;
    private boolean DaylightSavingsBegin;
    private boolean DaylightSavingsEnd;

    /** 
     Constructor.
    */
    public GXDateTime()
    {
        Skip = EnumSet.noneOf(DateTimeSkips.class);
        Status = ClockStatus.OK;
    }
    
    /** 
     Constructor.
    */
    public GXDateTime(java.util.Date value)
    {
        Skip = EnumSet.noneOf(DateTimeSkips.class);
        setValue(value);
        Status = ClockStatus.OK;
    }    

    /** 
     Constructor.
    */        
    public GXDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        Status = ClockStatus.OK;
        Skip = EnumSet.noneOf(DateTimeSkips.class);
        if (year < 1 || year == 0xFFFF)
        {
            Skip.add(DateTimeSkips.YEAR);
            java.util.Calendar tm = java.util.Calendar.getInstance();
            year = tm.get(Calendar.YEAR);            
        }
        DaylightSavingsBegin = month == 0xFE;
        DaylightSavingsEnd = month == 0xFD;
        if (month < 1 || month > 12)
        {
            Skip.add(DateTimeSkips.MONTH);
            month = 0;
        }
        else
        {
            month -= 1;        
        }
        
        if (day == -1 || day == 0 || day > 31)
        {
            Skip.add(DateTimeSkips.DAY);
            day = 1;
        }
        else if (day < 0)
        {            
            Calendar cal = Calendar.getInstance();
            day = cal.getActualMaximum(Calendar.DATE) + day + 3;
        }
        if (hour < 0 || hour > 24)
        {
            Skip.add(DateTimeSkips.HOUR);
            hour = 0;
        }
        if (minute < 0 || minute > 60 )
        {
            Skip.add(DateTimeSkips.MINUTE);
            minute = 0;
        }        
        if (second < 0 || second > 60)
        {
            Skip.add(DateTimeSkips.SECOND);
            second = 0;
        }
        //If ms is Zero it's skipped.
        if (millisecond < 1 || millisecond > 1000)
        {
            Skip.add(DateTimeSkips.MILLISECOND);
            millisecond = 0;
        }        
        java.util.Calendar tm = java.util.Calendar.getInstance();
        tm.set(year, month, day, hour, minute, second);       
        if (millisecond != 0)
        {
            tm.set(Calendar.MILLISECOND, millisecond);
        }        
        setValue(tm.getTime());
    }

    /**
     * Used date time value.
     */
    public final java.util.Date getValue()
    {
        return Value;
    }
    public final void setValue(java.util.Date value)
    {
        Value = value;
    }

    /**
     * Skip selected date time fields.
     */
    public final java.util.Set<DateTimeSkips> getSkip()
    {
        return Skip;
    }
    public final void setSkip(java.util.Set<DateTimeSkips> value)
    {
        Skip = value;
    }
    
    public final void setUsed(java.util.Set<DateTimeSkips> value)
    {
        int val = 0;
        for(DateTimeSkips it : value)
        {
            val |= it.getValue();
        }
        int tmp = (-1 & ~val);
        Skip = DateTimeSkips.forValue(tmp);
    }

    /** 
     Daylight savings begin.
    */    
    public final boolean getDaylightSavingsBegin()
    {
        return DaylightSavingsBegin;
    }
    public final void setDaylightSavingsBegin(boolean value)
    {
        DaylightSavingsBegin = value;
    }

    /** 
     Daylight savings end.
    */
    public final boolean getDaylightSavingsEnd()
    {
        return DaylightSavingsEnd;
    }
    public final void setDaylightSavingsEnd(boolean value)
    {
        DaylightSavingsEnd = value;
    }
    
    /*
        Status of the clock.
    */    
    public final ClockStatus getStatus()
    {
        return Status;
    }
    public final void setStatus(ClockStatus value)
    {
        Status = value;
    }
    
    @Override
    public String toString()
    {
        SimpleDateFormat sd = new SimpleDateFormat();            
        if (!getSkip().isEmpty())
        {               
            //Separate date and time parts.
            String[] tmp = sd.toPattern().split(" ");
            List<String> date = new ArrayList<String>();
            List<String> time = new ArrayList<String>();            
            //Find date time separator.
            char separator = 0;
            for(char it : tmp[0].toCharArray())
            {
                if (!Character.isLetter(it))
                {
                    separator = it;
                    break;
                }
            }
            if (separator != 0)
            {
                String sep = "\\" + String.valueOf(separator);
                date.addAll(Arrays.asList(tmp[0].split(sep)));
                time.addAll(Arrays.asList(tmp[1].split(":")));
                if (getSkip().contains(DateTimeSkips.YEAR))
                {                
                    date.remove("yyyy");
                }
                if (getSkip().contains(DateTimeSkips.MONTH))
                {
                    date.remove("M");
                }
                if (getSkip().contains(DateTimeSkips.DAY))
                {
                    date.remove("d");                
                }
                if (getSkip().contains(DateTimeSkips.HOUR))
                {
                    time.remove("H");
                    time.remove("HH");
                }
                if (getSkip().contains(DateTimeSkips.MINUTE))
                {
                    time.remove("m");
                    time.remove("mm");
                }
                if (getSkip().contains(DateTimeSkips.SECOND))
                {
                    time.remove("ss");
                }
                else
                {
                    time.add("ss");
                }
                if (getSkip().contains(DateTimeSkips.MILLISECOND))
                {
                    time.remove("SSS");
                }
                else
                {
                    time.add("SSS");
                }    

                String format = "";
                StringBuilder sb = new StringBuilder();
                if (!date.isEmpty())
                {
                    for(String it : date)
                    {
                        if (sb.length() != 0)
                        {
                            sb.append(separator);
                        }
                        sb.append(it);
                    }  
                    format = sb.toString();
                }
                if (!time.isEmpty())
                {
                    sb.setLength(0);
                    for(String it : time)
                    {
                        if (sb.length() != 0)
                        {
                            sb.append(':');
                        }
                        sb.append(it);
                    }  
                    if (format.length() != 0)
                    {
                        format += " ";                    
                    }
                    format += sb.toString();
                }
                sd = new SimpleDateFormat(format);
                return sd.format(getValue());
            }
        }
        return sd.format(getValue());        
    }
}