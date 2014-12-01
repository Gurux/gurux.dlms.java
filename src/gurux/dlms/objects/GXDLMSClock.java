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
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class GXDLMSClock extends GXDLMSObject implements IGXDLMSBase
{
    ClockBase m_ClockBase;
    int Deviation;
    boolean Enabled;
    GXDateTime End = new GXDateTime();
    ClockStatus Status;
    GXDateTime Begin = new GXDateTime();
    int TimeZone;
    GXDateTime Time = new GXDateTime();
    /**  
     Constructor.
    */
    public GXDLMSClock()
    {
        super(ObjectType.CLOCK, "0.0.1.0.0.255", 0);
        Status = ClockStatus.OK;
        Deviation = 0;
        java.util.Set<DateTimeSkips> value = EnumSet.of(DateTimeSkips.MONTH);
        value.add(DateTimeSkips.DAY);
        Begin.setSkip(value);
        End.setSkip(Begin.getSkip());
        m_ClockBase = ClockBase.None;
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
    */
    public GXDLMSClock(String ln)
    {
        super(ObjectType.CLOCK, ln, 0);        
        Status = ClockStatus.OK;
        Deviation = 0;
        java.util.Set<DateTimeSkips> value = EnumSet.of(DateTimeSkips.MONTH);
        value.add(DateTimeSkips.DAY);
        Begin.setSkip(value);
        End.setSkip(Begin.getSkip());
        m_ClockBase = ClockBase.None;
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSClock(String ln, int sn)
    {
        super(ObjectType.CLOCK, ln, sn);        
        Status = ClockStatus.OK;
        Deviation = 0;
        java.util.Set<DateTimeSkips> value = EnumSet.of(DateTimeSkips.MONTH);
        value.add(DateTimeSkips.DAY);
        Begin.setSkip(value);
        End.setSkip(Begin.getSkip());
        m_ClockBase = ClockBase.None;
    }   
            
    @Override
    public DataType getUIDataType(int index)
    {
        if (index == 2 || index == 5 || index == 6)
        {
            return DataType.DATETIME;
        }
        return super.getDataType(index);
    }

    /** 
     Time of COSEM Clock object.
    */
    public final GXDateTime getTime()
    {
        return Time;
    }
    public final void setTime(GXDateTime value)
    {
        Time = value;
    }

    /** 
     TimeZone of COSEM Clock object.
    */
    public final int getTimeZone()
    {
        return TimeZone;
    }
    public final void setTimeZone(int value)
    {
        TimeZone = value;
    }

    /** 
     Status of COSEM Clock object.
    */
    public final ClockStatus getStatus()
    {
        return Status;
    }
    public final void setStatus(ClockStatus value)
    {
        Status = value;
    }

    public final GXDateTime getBegin()
    {
        return Begin;
    }
    public final void setBegin(GXDateTime value)
    {
        Begin = value;
    }

    public final GXDateTime getEnd()
    {
        return End;
    }
    public final void setEnd(GXDateTime value)
    {
        End = value;
    }

    public final int getDeviation()
    {
        return Deviation;
    }
    public final void setDeviation(int value)
    {
        Deviation = value;
    }

    public final boolean getEnabled()
    {
        return Enabled;
    }
    public final void setEnabled(boolean value)
    {
        Enabled = value;
    }

    /** 
     Clock base of COSEM Clock object.
    */
    public final ClockBase getClockBase()
    {
        return m_ClockBase;
    }
    public final void setClockBase(ClockBase value)
    {
        m_ClockBase = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getTime(), getTimeZone(), 
            getStatus(), getBegin(), getEnd(), getDeviation(), 
            getEnabled(), getClockBase()};
    }
    
    @Override
    @SuppressWarnings("unused")
    public byte[][] invoke(Object sender, int index, Object parameters)
    {
        // Resets the value to the default value. 
        // The default value is an instance specific constant.
        if (index == 1)
        {
            GXDateTime dt = getTime();
            java.util.Calendar tm = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
            tm.setTime(dt.getValue());
            int minutes = tm.get(java.util.Calendar.MINUTE);
            if (minutes < 8)
            {
                minutes = 0;
            }
            else if (minutes < 23)
            {
                minutes = 15;
            }
            else if (minutes < 38)
            {
                minutes = 30;
            }
            else if (minutes < 53)
            {
                minutes = 45;
            }
            else
            {
                minutes = 0;
                tm.add(java.util.Calendar.HOUR, 1);
            }
            tm.set(java.util.Calendar.MINUTE, minutes);
            tm.set(java.util.Calendar.SECOND, 0);
            tm.set(java.util.Calendar.MILLISECOND, 0);            
            dt.setValue(tm.getTime());
            setTime(dt);
        }
        // Sets the meter’s time to the nearest minute.
        else if (index == 3)
        {
            GXDateTime dt = getTime();
            java.util.Calendar tm = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
            tm.setTime(dt.getValue());
            int s = tm.get(java.util.Calendar.SECOND);
            if (s > 30)
            {
                tm.add(java.util.Calendar.MINUTE, 1);
            }
            tm.set(java.util.Calendar.SECOND, 0);
            tm.set(java.util.Calendar.MILLISECOND, 0);
            dt.setValue(tm.getTime());
            setTime(dt);

        }
        // Presets the time to a new value (preset_time) and defines 
        // avalidity_interval within which the new time can be activated.
        else if (index == 5)
        {           
            GXDateTime presetTime = (GXDateTime) GXDLMSClient.changeType((byte[]) Array.get(parameters, 0), 
                    DataType.DATETIME);
            GXDateTime validityIntervalStart  = (GXDateTime) GXDLMSClient.changeType((byte[]) Array.get(parameters, 1), 
                    DataType.DATETIME);
            GXDateTime validityIntervalEnd = (GXDateTime) GXDLMSClient.changeType((byte[]) Array.get(parameters, 2), 
                    DataType.DATETIME);
            setTime(presetTime);
        }
        // Shifts the time.
        else if (index == 6)
        {   
            int shift = ((Number)parameters).intValue();
            GXDateTime dt = getTime();
            java.util.Calendar tm = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
            tm.setTime(dt.getValue());
            tm.add(java.util.Calendar.SECOND, shift);   
            dt.setValue(tm.getTime());
            setTime(dt);
        }
        else
        {
            throw new IllegalArgumentException("Invoke failed. Invalid attribute index.");
        }
        return null;
    }
    
    /*
     * Sets the meter’s time to the nearest (+/-) quarter of an hour value
     * (*:00, *:15, *:30, *:45).
     */
    public byte[][] adjustToQuarter(GXDLMSClient client)
    {
        return client.method(this, 1, 0, DataType.INT8);
    }

    
    /*
     * Sets the meter’s time to the nearest (+/-) starting point 
     * of a measuring period.
     */
    public byte[][] adjustToMeasuringPeriod(GXDLMSClient client)
    {
        return client.method(this, 2, 0, DataType.INT8);        
    }

    /*
     * Sets the meter’s time to the nearest minute.
     * If second_counter < 30 s, so second_counter is set to 0.
     * If second_counter ³ 30 s, so second_counter is set to 0, and
     * minute_counter and all depending clock values are incremented if necessary.
     */
    public byte[][] adjustToMinute(GXDLMSClient client)
    {
        return client.method(this, 3, 0, DataType.INT8);
    }
    /*
     * This method is used in conjunction with the preset_adjusting_time
     * method. If the meter’s time lies between validity_interval_start and
     * validity_interval_end, then time is set to preset_time.
     */
    public byte[][] adjustToPresetTime(GXDLMSClient client)
    {
        return client.method(this, 4, 0, DataType.INT8);
    }
    /*
     * Presets the time to a new value (preset_time) and defines a
     * validity_interval within which the new time can be activated.
     */
    public byte[][] presetAdjustingTime(GXDLMSClient client, Date presetTime, Date validityIntervalStart, Date validityIntervalEnd)
    {        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write((byte) DataType.STRUCTURE.getValue());
        stream.write((byte) 3);
        try 
        {
            GXCommon.setData(stream, DataType.DATETIME, presetTime);
            GXCommon.setData(stream, DataType.DATETIME, validityIntervalStart);
            GXCommon.setData(stream, DataType.DATETIME, validityIntervalEnd);
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException(ex.getMessage());
        }
        return client.method(this, 5, stream.toByteArray(), DataType.ARRAY);    
    }
    /*
     * Shifts the time by n (-900 <= n <= 900) s.
     */
    public byte[][] shiftTime(GXDLMSClient client, int time)
    {
        if (time < -900 || time > 900)
        {
            throw new IllegalArgumentException("Invalid shift time.");
        }
        return client.method(this, 6, time, DataType.INT16);
    }

    /*
     * Returns collection of attributes to read.
     * 
     * If attribute is static and already read or device is returned HW error it is not returned.
     */
    @Override
    public int[] getAttributeIndexToRead()
    {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        //LN is static and read only once.
        if (LogicalName == null || LogicalName.compareTo("") == 0)
        {
            attributes.add(1);
        }
        //Time
        if (canRead(2))
        {
            attributes.add(2);
        }
        //TimeZone
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //Status
        if (canRead(4))
        {
            attributes.add(4);
        }
        //Begin
        if (!isRead(5))
        {
            attributes.add(5);
        }
        //End
        if (!isRead(6))
        {
            attributes.add(6);
        }
        //Deviation
        if (!isRead(7))
        {
            attributes.add(7);
        }
        //Enabled
        if (!isRead(8))
        {
            attributes.add(8);
        }
        //ClockBase
        if (!isRead(9))
        {
            attributes.add(9);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 9;
    }
    
     /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 6;
    }
    
    @Override
    public DataType getDataType(int index)
    {
        if (index == 1)
        {
            return DataType.OCTET_STRING;
        }
        if (index == 2)
        {
            return DataType.DATETIME;
        }
        if (index == 3)
        {
            return DataType.INT16;
        }
        if (index == 4)
        {
            return DataType.UINT8;
        }
        if (index == 5)
        {
            return DataType.DATETIME;            
        }
        if (index == 6)
        {
            return DataType.DATETIME;            
        }
        if (index == 7)
        {
            return DataType.INT8;
        }
        if (index == 8)
        {
            return DataType.BOOLEAN;
        }
        if (index == 9)
        {
            return DataType.ENUM;
        }  
        throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
    }
    
    /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, int selector, Object parameters)
    {
        if (index == 1)
        {
            return getLogicalName();
        }
        if (index == 2)
        {
            return getTime();
        }
        if (index == 3)
        {
            return getTimeZone();
        }
        if (index == 4)
        {
            return getStatus().getValue();
        }
        if (index == 5)
        {
            return getBegin();
        }
        if (index == 6)
        {
            return getEnd();
        }
        if (index == 7)
        {
            return getDeviation();
        }
        if (index == 8)
        {
            return getEnabled();
        }
        if (index == 9)
        {
            return getClockBase().ordinal();
        }
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
    
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value)
    {
        if (index == 1)
        {
            super.setValue(index, value);            
        }        
        else if (index == 2)
        {
            if (value == null)
            {
                setTime(new GXDateTime());                
            }
            else
            {
                if (value instanceof byte[])
                {
                    value = GXDLMSClient.changeType((byte[]) value, DataType.DATETIME);
                }                
                setTime((GXDateTime) value);
            }
        }
        else if (index == 3)
        {
            if (value == null)
            {
                setTimeZone(0);
            }
            else
            {
                setTimeZone(((Number) value).intValue());
            }            
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setStatus(ClockStatus.OK);
            }
            else
            {
                setStatus(ClockStatus.forValue(((Number) value).intValue()));
            }            
        }
        else if (index == 5)
        {
            if (value == null)
            {
                setBegin(new GXDateTime());                
            }
            else if (value instanceof byte[])
            {
                value = GXDLMSClient.changeType((byte[]) value, DataType.DATETIME);
                setBegin((GXDateTime) value);
            }
            else
            {
                setBegin((GXDateTime) value);
            }                    
        }
        else if (index == 6)
        {
            if (value == null)
            {
                setEnd(new GXDateTime());                
            }
            else  if (value instanceof byte[])
            {                
                value = GXDLMSClient.changeType((byte[]) value, DataType.DATETIME);
                setEnd((GXDateTime) value);
            }
            else
            {
                setEnd((GXDateTime) value);
            }
        }
        else if (index == 7)
        {
            if (value == null)
            {
                setDeviation(0);
            }
            else
            {
                setDeviation(((Number) value).intValue());
            }            
        }
        else if (index == 8)
        {
            if (value == null)
            {
                setEnabled(false);
            }
            else
            {
                setEnabled((Boolean) value);
            }
        }
        else if (index == 9)
        {
            if (value == null)
            {
                setClockBase(m_ClockBase.None);
            }
            else
            {
                setClockBase(ClockBase.values()[((Number) value).intValue()]);
            }            
        }        
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}