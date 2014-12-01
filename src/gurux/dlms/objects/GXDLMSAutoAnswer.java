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
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSAutoAnswer extends GXDLMSObject implements IGXDLMSBase
{
    private AutoConnectMode m_Mode;
    private List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> m_ListeningWindow;
    private AutoAnswerStatus m_Status = AutoAnswerStatus.INACTIVE;
    private int m_NumberOfCalls;
    private int m_NumberOfRingsInListeningWindow, m_NumberOfRingsOutListeningWindow;
    
    /**  
     Constructor.
    */
    public GXDLMSAutoAnswer()
    {
        super(ObjectType.AUTO_ANSWER, "0.0.2.2.0.255", 0);
        m_ListeningWindow = new ArrayList<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSAutoAnswer(String ln)
    {
        super(ObjectType.AUTO_ANSWER, ln, 0);
        m_ListeningWindow = new ArrayList<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSAutoAnswer(String ln, int sn)
    {
        super(ObjectType.AUTO_ANSWER, ln, sn);
        m_ListeningWindow = new ArrayList<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>>();
    }

    public final AutoConnectMode getMode()
    {
        return m_Mode;
    }
    public final void setMode(AutoConnectMode value)
    {
        m_Mode = value;
    }

    public final List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> getListeningWindow()
    {
        return m_ListeningWindow;
    }
    public final void setListeningWindow(List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> value)
    {
        m_ListeningWindow = value;
    }

    public final AutoAnswerStatus getStatus()
    {
        return m_Status;
    }
    public final void setStatus(AutoAnswerStatus value)
    {
        m_Status = value;
    }

    public final int getNumberOfCalls()
    {
        return m_NumberOfCalls;
    }
    public final void setNumberOfCalls(int value)
    {
        m_NumberOfCalls = value;
    }    
            
    /*
     * Number of rings within the window defined by ListeningWindow.
     */
    public final int getNumberOfRingsInListeningWindow()
    {
        return m_NumberOfRingsInListeningWindow;
    }
    public final void setNumberOfRingsInListeningWindow(int value)
    {
        m_NumberOfRingsInListeningWindow = value;
    }

    /*
     * Number of rings outside the window defined by ListeningWindow.        
     */
    public final int getNumberOfRingsOutListeningWindow()
    {
        return m_NumberOfRingsOutListeningWindow;
    }
    public final void setNumberOfRingsOutListeningWindow(int value)
    {
        m_NumberOfRingsOutListeningWindow = value;
    }
     
    @Override
    public Object[] getValues()
    {
        String str = String.format("%d/%d", m_NumberOfRingsInListeningWindow, 
                m_NumberOfRingsOutListeningWindow);
        return new Object[] {getLogicalName(), getMode(), getListeningWindow(), 
            getStatus(), getNumberOfCalls(), str};
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
        //Mode is static and read only once.
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //ListeningWindow is static and read only once.
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //Status is not static.
        if (canRead(4))
        {
            attributes.add(4);
        }

        //NumberOfCalls is static and read only once.
        if (!isRead(5))
        {
            attributes.add(5);
        }
        //NumberOfRingsInListeningWindow is static and read only once.
        if (!isRead(6))
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
            return DataType.ARRAY;
        }
        if (index == 4)
        {
            return DataType.ENUM;
        }
        if (index == 5)
        {
            return DataType.UINT8;
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
            return getMode().ordinal();
        }    
        if (index == 3)
        {
            int cnt = getListeningWindow().size();
            ByteArrayOutputStream data = new ByteArrayOutputStream();   
            data.write(DataType.ARRAY.getValue());
            //Add count            
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0)
            {
                try
                {                    
                    for(AbstractMap.SimpleEntry<GXDateTime, GXDateTime> it : getListeningWindow())
                    {
                        data.write(DataType.STRUCTURE.getValue());
                        data.write(2); //Count
                        GXCommon.setData(data, DataType.OCTET_STRING, it.getKey()); //start_time
                        GXCommon.setData(data, DataType.OCTET_STRING, it.getValue()); //end_time
                    }
                }                
                catch (Exception ex) 
                {
                    Logger.getLogger(GXDLMSClock.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
            return data.toByteArray();                
        }
        if (index == 4)
        {
            return getStatus().getValue();
        }
        if (index == 5)
        {
            return getNumberOfCalls();
        }
        if (index == 6)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();   
            data.write(DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(2, data);
            try
            {
                GXCommon.setData(data, DataType.UINT8, m_NumberOfRingsInListeningWindow);
                GXCommon.setData(data, DataType.UINT8, m_NumberOfRingsOutListeningWindow);
            }                
            catch (Exception ex) 
            {
                Logger.getLogger(GXDLMSClock.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
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
            setMode(AutoConnectMode.forValue(((Number)value).byteValue()& 0xFF));
        }
        else if (index == 3)
        {
            getListeningWindow().clear();
            if (value != null)
            {
                for (Object item : (Object[])value)
                {
                    GXDateTime start = (GXDateTime)GXDLMSClient.changeType((byte[])Array.get(item, 0), DataType.DATETIME);
                    GXDateTime end = (GXDateTime)GXDLMSClient.changeType((byte[])Array.get(item, 1), DataType.DATETIME);                    
                    getListeningWindow().add(new SimpleEntry<GXDateTime, GXDateTime>(start, end));
                }
            }
        }
        else if (index == 4)
        {
            setStatus(AutoAnswerStatus.forValue(((Number)value).intValue()));
        }
        else if (index == 5)
        {
            setNumberOfCalls(((Number)value).intValue());
        }
        else if (index == 6)
        {          
            m_NumberOfRingsInListeningWindow = m_NumberOfRingsOutListeningWindow = 0;
            if (value != null)
            {
                m_NumberOfRingsInListeningWindow = ((Number)Array.get(value, 0)).intValue();
                m_NumberOfRingsOutListeningWindow = ((Number)Array.get(value, 1)).intValue();
            }
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}