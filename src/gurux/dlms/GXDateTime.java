/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms;

import gurux.dlms.enums.DateTimeSkips;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;

public class GXDateTime
{
    private java.util.Date Value = new java.util.Date(0);
    private java.util.Set<DateTimeSkips> Skip;

    /** 
     Constructor.
    */
    public GXDateTime()
    {
        Skip = EnumSet.noneOf(DateTimeSkips.class);
    }
    
    /** 
     Constructor.
    */
    public GXDateTime(java.util.Date value)
    {
        Skip = EnumSet.noneOf(DateTimeSkips.class);
        setValue(value);
    }

    /** 
     Constructor.
    */
    public GXDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        Skip = EnumSet.noneOf(DateTimeSkips.class);
        if (year == -1)
        {
            Skip.add(DateTimeSkips.YEAR);
            year = 1;
        }
        if (month == -1)
        {
            Skip.add(DateTimeSkips.MONTH);
            month = 0;
        }
        else
        {
            month -= 1;        
        }
        if (day == -1)
        {
            Skip.add(DateTimeSkips.DAY);
            day = 1;
        }
        if (hour == -1)
        {
            Skip.add(DateTimeSkips.HOUR);
            hour = 0;
        }
        if (minute == -1)
        {
            Skip.add(DateTimeSkips.MINUTE);
            minute = 0;
        }
        if (second == -1)
        {
            Skip.add(DateTimeSkips.SECOND);
            second = 0;
        }
        if (millisecond == -1)
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

    @Override
    public String toString()
    {
        if (!getSkip().isEmpty())
        {
            String format = null;
            if (getSkip().contains(DateTimeSkips.YEAR))
            {                
                format += "yyyy";
            }
            if (getSkip().contains(DateTimeSkips.MONTH))
            {
                if (format != null)
                {
                    format += "-";
                }
                format += "MM";
            }
            if (getSkip().contains(DateTimeSkips.DAY))
            {
                if (format != null)
                {
                    format += "-";
                }
                format += "dd";
            }
            if (getSkip().contains(DateTimeSkips.HOUR))
            {
                if (format != null)
                {
                    format += "-";
                }
                format += "HH";
            }
            if (getSkip().contains(DateTimeSkips.MINUTE))
            {
                if (format != null)
                {
                    format += "-";
                }
                format += "mm";
            }
            if (getSkip().contains(DateTimeSkips.SECOND))
            {
                if (format != null)
                {
                    format += "-";
                }
                format += "ss";
            }
            DateFormat df = new SimpleDateFormat(format);
            return df.format(getValue());
        }
        return getValue().toString();
    }
}