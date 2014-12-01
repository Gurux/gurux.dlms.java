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
import gurux.dlms.enums.BaudRate;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSModemConfiguration extends GXDLMSObject implements IGXDLMSBase
{
    private GXDLMSModemInitialisation[] m_InitialisationStrings;
    private String[] m_ModemProfile;
    private BaudRate m_CommunicationSpeed;

    static String[] DefaultProfiles()
    {
        return new String[]{"OK", "CONNECT", "RING", "NO CARRIER", "ERROR", "CONNECT 1200", "NO DIAL TONE",
            "BUSY", "NO ANSWER", "CONNECT 600", "CONNECT 2400", "CONNECT 4800", "CONNECT 9600", 
            "CONNECT 14 400", "CONNECT 28 800", "CONNECT 33 600", "CONNECT 56 000"};
    }
    /**  
     Constructor.
    */
    public GXDLMSModemConfiguration()
    {
        super(ObjectType.MODEM_CONFIGURATION, "0.0.2.0.0.255", 0);
        m_InitialisationStrings = new GXDLMSModemInitialisation[0];
        m_CommunicationSpeed = BaudRate.BAUDRATE_300;
        m_ModemProfile = DefaultProfiles();
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
    */
    public GXDLMSModemConfiguration(String ln)
    {
        super(ObjectType.MODEM_CONFIGURATION, ln, 0);
        m_InitialisationStrings = new GXDLMSModemInitialisation[0];
        m_CommunicationSpeed = BaudRate.BAUDRATE_300;
        m_ModemProfile = DefaultProfiles();
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSModemConfiguration(String ln, int sn)
    {
        super(ObjectType.MODEM_CONFIGURATION, ln, 0);
        m_InitialisationStrings = new GXDLMSModemInitialisation[0];
        m_CommunicationSpeed = BaudRate.BAUDRATE_300;
        m_ModemProfile = DefaultProfiles();
    }   

    public final BaudRate getCommunicationSpeed()
    {
        return m_CommunicationSpeed;
    }
    public final void setCommunicationSpeed(BaudRate value)
    {
        m_CommunicationSpeed = value;
    }

    public final GXDLMSModemInitialisation[] getInitialisationStrings()
    {
        return m_InitialisationStrings;
    }
    public final void setInitialisationStrings(GXDLMSModemInitialisation[] value)
    {
        m_InitialisationStrings = value;
    }

    public final String[] getModemProfile()
    {
        return m_ModemProfile;
    }

    public final void setModemProfile(String[] value)
    {
        m_ModemProfile = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getCommunicationSpeed(), getInitialisationStrings(), getModemProfile()};
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
        //CommunicationSpeed
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //InitialisationStrings
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //ModemProfile
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
            return m_CommunicationSpeed.ordinal();
        }
        if (index == 3)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write(DataType.ARRAY.getValue());
            //Add count
            int cnt = 0;
            if (m_InitialisationStrings != null)
            {
                cnt = m_InitialisationStrings.length;
            }
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0)
            {
                try 
                {                
                    for (GXDLMSModemInitialisation it : m_InitialisationStrings)
                    {
                        data.write(DataType.STRUCTURE.getValue());
                        data.write(3); //Count                        
                        GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(it.getRequest()));
                        GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(it.getResponse()));
                        GXCommon.setData(data, DataType.UINT16, it.getDelay());                    
                    }
                } 
                catch (Exception ex) 
                {                    
                    Logger.getLogger(GXDLMSModemConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }                                    
            }
            return data.toByteArray();
        }
        if (index == 4)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write(DataType.ARRAY.getValue());
            //Add count
            int cnt = 0;
            if (m_ModemProfile != null)
            {
                cnt = m_ModemProfile.length;
            }
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0)
            {
                try 
                {                
                    for(String it : m_ModemProfile)
                    {
                        GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(it));
                    }
                }
                catch (Exception ex) 
                {
                    Logger.getLogger(GXDLMSModemConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
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
            m_CommunicationSpeed = BaudRate.values()[((Number)value).intValue()];
        }
        else if (index == 3)
        {
            m_InitialisationStrings = null;
            if (value != null)
            {                
                List<GXDLMSModemInitialisation> items = new ArrayList<GXDLMSModemInitialisation>();
                for(Object it : (Object[])value)
                {
                    GXDLMSModemInitialisation item = new GXDLMSModemInitialisation();                                        
                    item.setRequest(GXDLMSClient.changeType((byte[])Array.get(it, 0), DataType.STRING).toString());
                    item.setResponse(GXDLMSClient.changeType((byte[])Array.get(it, 1), DataType.STRING).toString());
                    if (Array.getLength(it) > 2)
                    {
                        item.setDelay(((Number)Array.get(it, 2)).intValue());
                    }
                    items.add(item);
                }
                m_InitialisationStrings = items.toArray(new GXDLMSModemInitialisation[items.size()]);
            }                                 
        }
        else if (index == 4)
        {
            m_ModemProfile = null;
            if (value != null)
            {
                List<String> items = new ArrayList<String>();
                for (Object it : (Object[])value)
                {
                    items.add(GXDLMSClient.changeType((byte[])it, DataType.STRING).toString());
                }
                m_ModemProfile = items.toArray(new String[items.size()]);
            }                   
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}