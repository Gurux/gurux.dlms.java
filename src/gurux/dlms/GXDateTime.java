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
import gurux.dlms.enums.DateTimeSkips;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;

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

    public GXDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        init(year, month, day, hour, minute, second, millisecond, 0);
    }

    /** 
     Constructor.
    */
    public GXDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, int deviation)
    {
        init(year, month, day, hour, minute, second, millisecond, deviation);
    }
    
    private final void init(int year, int month, int day, int hour, int minute, int second, int millisecond, int deviation)
    {
        Status = ClockStatus.OK;
        Skip = EnumSet.noneOf(DateTimeSkips.class);
        if (year == -1 || year == 0xFFFF)
        {
            Skip.add(DateTimeSkips.YEAR);
            year = 1;
        }
        DaylightSavingsBegin = month == 0xFE;
        DaylightSavingsEnd = month == 0xFD;
        if (month < 0 || month > 11)
        {
            Skip.add(DateTimeSkips.MONTH);
            month = 0;
        }
        else
        {
            month -= 1;        
        }
        
        if (day < 0 || day > 31)
        {
            Skip.add(DateTimeSkips.DAY);
            day = 1;
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
        if (millisecond < 0 || millisecond > 1000)
        {
            Skip.add(DateTimeSkips.MILLISECOND);
            millisecond = 0;
        }
        java.util.Calendar tm = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        tm.set(year, month, day, hour, minute, second);
        if (deviation != 0 && deviation != 0x8000)
        {
            tm.add(java.util.Calendar.MINUTE, deviation);
        }        
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
        if (!getSkip().isEmpty())
        {            
            SimpleDateFormat sd = new SimpleDateFormat();
            String format = sd.toPattern();            
            if (getSkip().contains(DateTimeSkips.YEAR))
            {                
                format = format.replace("yyyy", "");
            }
            if (getSkip().contains(DateTimeSkips.MONTH))
            {
                format = format.replace("M", "");
            }
            if (getSkip().contains(DateTimeSkips.DAY))
            {
                format = format.replace("d", "");                
            }
            if (getSkip().contains(DateTimeSkips.HOUR))
            {
                format = format.replace("HH", "");
            }
            if (getSkip().contains(DateTimeSkips.MINUTE))
            {
                format = format.replace("m", "");
            }
            if (getSkip().contains(DateTimeSkips.SECOND))
            {
                format = format.replace("ss", "");
            }
            else
            {
                format += ":ss";
            }
            DateFormat df = new SimpleDateFormat(format);
            return df.format(getValue());
        }
        return getValue().toString();
    }
}