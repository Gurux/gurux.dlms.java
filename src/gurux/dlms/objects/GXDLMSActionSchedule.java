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
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.Calendar;

public class GXDLMSActionSchedule extends GXDLMSObject implements IGXDLMSBase
{
    private String m_ExecutedScriptLogicalName;
    private int m_ExecutedScriptSelector;
    private SingleActionScheduleType m_Type;
    private GXDateTime[] m_ExecutionTime;
    
    /**  
     Constructor.
    */
    public GXDLMSActionSchedule()
    {
        super(ObjectType.ACTION_SCHEDULE);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSActionSchedule(String ln)
    {
        super(ObjectType.ACTION_SCHEDULE, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSActionSchedule(String ln, int sn)
    {
        super(ObjectType.ACTION_SCHEDULE, ln, sn);
    }
        
    public final String getExecutedScriptLogicalName()
    {
        return m_ExecutedScriptLogicalName;
    }
    public final void setExecutedScriptLogicalName(String value)
    {
        m_ExecutedScriptLogicalName = value;
    }

    public final int getExecutedScriptSelector()
    {
        return m_ExecutedScriptSelector;
    }
    public final void setExecutedScriptSelector(int value)
    {
        m_ExecutedScriptSelector = value;
    }

    public final SingleActionScheduleType getType()
    {
        return m_Type;
    }
    public final void setType(SingleActionScheduleType value)
    {
        m_Type = value;
    }

    public final GXDateTime[] getExecutionTime()
    {
        return m_ExecutionTime;
    }
    public final void setExecutionTime(GXDateTime[] value)
    {
        m_ExecutionTime = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getExecutedScriptLogicalName(), getType(), getExecutionTime()};
    }
    
    /*
     * Returns collection of attributes to read.
     * 
     * If attribute is static and already read or device is returned HW error it is not returned.
     */
    @Override
    public int[] GetAttributeIndexToRead()
    {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        //LN is static and read only once.
        if (LogicalName == null || LogicalName.compareTo("") == 0)
        {
            attributes.add(1);
        }
        //ExecutedScriptLogicalName is static and read only once.
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //Type is static and read only once.
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //ExecutionTime is static and read only once.
        if (!isRead(4))
        {
            attributes.add(4);
        }                       
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 4;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 0;
    }    
    
    /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, DataType[] type, byte[] parameters, boolean raw)
    {
        if (index == 1)
        {
            type[0] = DataType.OCTET_STRING;
            return getLogicalName();
        }
        if (index == 2)
        {
            type[0] = DataType.ARRAY;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();   
            stream.write((byte)DataType.STRUCTURE.getValue());
            stream.write(2);
            try
            {
                GXCommon.setData(stream, DataType.OCTET_STRING, getExecutedScriptLogicalName().getBytes("ASCII"));
                GXCommon.setData(stream, DataType.UINT16, getExecutedScriptSelector());
            }            
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            } 
            return stream.toByteArray();
        }
        if (index == 3)
        {
            type[0] = DataType.ENUM;
            return this.getType().getValue();
        }
        if (index == 4)
        {
            type[0] = DataType.ARRAY;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();   
            stream.write((byte)DataType.ARRAY.getValue());
            if (getExecutionTime() == null)
            {
                GXCommon.setObjectCount(0, stream);
            }
            else
            {
                GXCommon.setObjectCount(getExecutionTime().length, stream);
                try
                {
                    for (GXDateTime it : getExecutionTime())
                    {
                        stream.write((byte)DataType.STRUCTURE.getValue());
                        stream.write((byte)2); //Count
                        GXCommon.setData(stream, DataType.TIME, it.getValue()); //Time
                        GXCommon.setData(stream, DataType.DATE, it.getValue()); //Date
                    }
                }
                catch(Exception ex)
                {
                    throw new RuntimeException(ex.getMessage());
                } 
            }                
            return stream.toByteArray();
        }  
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
    
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value, boolean raw)
    {
        if (index == 1)
        {
            setLogicalName(GXDLMSObject.toLogicalName((byte[]) value));            
        }
        else if (index == 2)
        {                
            setExecutedScriptLogicalName(GXDLMSClient.changeType((byte[])Array.get(value, 0), DataType.OCTET_STRING).toString());
            setExecutedScriptSelector(((Number) Array.get(value, 1)).intValue());
        }
        else if (index == 3)
        {
            setType(SingleActionScheduleType.forValue(((Number)value).intValue()));
        }        
        else if (index == 4)
        {
            setExecutionTime(null);
            if (value != null)
            {
                java.util.ArrayList<GXDateTime> items = new java.util.ArrayList<GXDateTime>();                
                for (Object it : (Object[])value)
                {
                    GXDateTime dt = (GXDateTime) GXDLMSClient.changeType((byte[])Array.get(it, 0), DataType.TIME);
                    GXDateTime dt2 = (GXDateTime) GXDLMSClient.changeType((byte[])Array.get(it, 1), DataType.DATE);                    
                    java.util.Calendar tm = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
                    tm.setTime(dt.getValue());
                    java.util.Calendar date = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
                    date.setTime(dt2.getValue());                    
                    tm.set(java.util.Calendar.YEAR, date.get(java.util.Calendar.YEAR));
                    tm.set(java.util.Calendar.MONTH, date.get(java.util.Calendar.MONTH));
                    tm.set(java.util.Calendar.DAY_OF_MONTH, date.get(java.util.Calendar.DAY_OF_MONTH));
                    java.util.Set<DateTimeSkips> skip = dt.getSkip();
                    skip.addAll(dt2.getSkip());
                    dt.setSkip(skip);
                    dt.setValue(tm.getTime());
                    items.add(dt);
                }
                setExecutionTime(items.toArray(new GXDateTime[items.size()]));
            }
        }                       
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}