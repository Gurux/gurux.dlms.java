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
import gurux.dlms.enums.AutoConnectMode;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSAutoConnect extends GXDLMSObject implements IGXDLMSBase
{
    private AutoConnectMode Mode;
    private List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> m_CallingWindow;
    private String[] m_Destinations;   
    private int m_RepetitionDelay;
    private int m_Repetitions;

    /**  
     Constructor.
    */
    public GXDLMSAutoConnect()
    {
        this("0.0.2.1.0.255");
    }
    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSAutoConnect(String ln)
    {
        this(ln, (short) 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSAutoConnect(String ln, int sn)
    {
        super(ObjectType.AUTO_CONNECT, ln, sn);
        m_CallingWindow = new java.util.ArrayList<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>>();
    }

    public final AutoConnectMode getMode()
    {
        return Mode;
    }
    public final void setMode(AutoConnectMode value)
    {
        Mode = value;
    }

    public final int getRepetitions()
    {
        return m_Repetitions;
    }
    public final void setRepetitions(int value)
    {
        m_Repetitions = value;
    }

    public final int getRepetitionDelay()
    {
        return m_RepetitionDelay;
    }
    public final void setRepetitionDelay(int value)
    {
        m_RepetitionDelay = value;
    }
   
    public final List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> getCallingWindow()
    {
        return m_CallingWindow;
    }
    public final void setCallingWindow(List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> value)
    {
        m_CallingWindow = value;
    }

    public final String[] getDestinations()
    {
        return m_Destinations;
    }
    public final void setDestinations(String[] value)
    {
        m_Destinations = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getMode(), getRepetitions(), getRepetitionDelay(), getCallingWindow(), getDestinations()};
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
        //Mode
        if (canRead(2))
        {
            attributes.add(2);
        }
        //Repetitions
        if (canRead(3))
        {
            attributes.add(3);
        }
        //RepetitionDelay
        if (canRead(4))
        {
            attributes.add(4);
        }
        //CallingWindow
        if (canRead(5))
        {
            attributes.add(5);
        }
        //Destinations
        if (canRead(6))
        {
            attributes.add(6);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 6;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 0;
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
            return DataType.ENUM;
        }
        if (index == 3)
        {
            return DataType.UINT8;
        }
        if (index == 4)
        {
            return DataType.UINT16;
        }
        if (index == 5)
        {
            return DataType.ARRAY;
        }
        if (index == 6)
        {
            return DataType.ARRAY;             
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
            return (byte) getMode().getValue();
        }
        if (index == 3)
        {
            return getRepetitions();
        }
        if (index == 4)
        {
            return getRepetitionDelay();
        }
        if (index == 5)
        {
            int cnt = getCallingWindow().size();
            ByteArrayOutputStream data = new ByteArrayOutputStream();            
            data.write((byte)DataType.ARRAY.getValue());
            //Add count            
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0)
            {                
                for (AbstractMap.SimpleEntry<GXDateTime, GXDateTime> it : getCallingWindow())
                {
                    data.write((byte)DataType.STRUCTURE.getValue());
                    data.write((byte)2); //Count
                    try 
                    {
                        GXCommon.setData(data, DataType.OCTET_STRING, it.getKey()); //start_time
                        GXCommon.setData(data, DataType.OCTET_STRING, it.getValue()); //end_time
                    }
                    catch (Exception ex) 
                    {
                        Logger.getLogger(GXDLMSAutoConnect.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException(ex.getMessage());
                    }                    
                }
            }
            return data.toByteArray();
        }
        if (index == 6)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.ARRAY.getValue());
            if (getDestinations() == null)
            {
                //Add count            
                GXCommon.setObjectCount(0, data);
            }
            else
            {
                int cnt = getDestinations().length;                                        
                //Add count            
                GXCommon.setObjectCount(cnt, data);
                for (String it : getDestinations())
                {
                    try 
                    {
                        GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(it)); //destination
                    }
                    catch (Exception ex) 
                    {
                        Logger.getLogger(GXDLMSAutoConnect.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException(ex.getMessage());
                    }
                }
            }
            return data.toByteArray();
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
            setMode(AutoConnectMode.forValue(((Number) value).intValue()));
        }
        else if (index == 3)
        {
            setRepetitions(((Number) value).intValue());
        }
        else if (index == 4)
        {
            setRepetitionDelay(((Number) value).intValue());
        }
        else if (index == 5)
        {
            getCallingWindow().clear();
            if (value != null)
            {
                for (Object item : (Object[])value)
                {
                    GXDateTime start = (GXDateTime)GXDLMSClient.changeType((byte[])Array.get(item, 0), DataType.DATETIME);
                    GXDateTime end = (GXDateTime)GXDLMSClient.changeType((byte[])Array.get(item, 1), DataType.DATETIME);                    
                    getCallingWindow().add(new AbstractMap.SimpleEntry(start, end));
                }
            }
        }
        else if (index == 6)
        {
            setDestinations(null);
            if (value != null)
            {
                List<String> items = new ArrayList<String>();
                for (Object item : (Object[])value)
                {
                    String it = GXDLMSClient.changeType((byte[]) item, DataType.STRING).toString();
                    items.add(it);
                }
                setDestinations(items.toArray(new String[items.size()]));
            }
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}