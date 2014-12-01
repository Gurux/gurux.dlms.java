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
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSSapAssignment extends GXDLMSObject implements IGXDLMSBase
{
    private List<AbstractMap.SimpleEntry<Integer, String>> m_SapAssignmentList;
    
    /**  
     Constructor.
    */
    public GXDLMSSapAssignment()
    {
        super(ObjectType.SAP_ASSIGNMENT, "0.0.41.0.0.255", 0);
        m_SapAssignmentList = new java.util.ArrayList<AbstractMap.SimpleEntry<Integer, String>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSSapAssignment(String ln)
    {
        super(ObjectType.SAP_ASSIGNMENT, ln, 0);
        m_SapAssignmentList = new java.util.ArrayList<AbstractMap.SimpleEntry<Integer, String>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSSapAssignment(String ln, int sn)
    {
        super(ObjectType.SAP_ASSIGNMENT, ln, sn);
        m_SapAssignmentList = new java.util.ArrayList<AbstractMap.SimpleEntry<Integer, String>>();
    }

    public final List<AbstractMap.SimpleEntry<Integer, String>> getSapAssignmentList()
    {
        return m_SapAssignmentList;
    }
    public final void setSapAssignmentList(List<AbstractMap.SimpleEntry<Integer, String>> value)
    {
        m_SapAssignmentList = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getSapAssignmentList()};
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
        //SapAssignmentList
        if (!isRead(2))
        {
            attributes.add(2);
        }
        return toIntArray(attributes);
    }
    
     /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 2;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 1;
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
            int cnt = 0;
            if (m_SapAssignmentList != null)
            {
                cnt = m_SapAssignmentList.size();
            }
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write(DataType.ARRAY.getValue());
            //Add count            
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0)
            {
                try
                {                    
                    for (AbstractMap.SimpleEntry<Integer, String> it : m_SapAssignmentList)
                    {
                        data.write(DataType.STRUCTURE.getValue());
                        data.write((byte)2); //Count
                        GXCommon.setData(data, DataType.UINT16, it.getKey());
                        GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(it.getValue()));
                    }
                }
                catch(Exception ex)
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
            m_SapAssignmentList.clear();
            if (value != null)
            {                    
                for (Object item : (Object[])value)
                {
                    String str;
                    Object tmp = Array.get(item, 1);
                    if (tmp instanceof byte[])
                    {
                        str = GXDLMSClient.changeType((byte[])tmp, DataType.STRING).toString();
                    }
                    else
                    {
                        str = tmp.toString();
                    }            
                    m_SapAssignmentList.add(new AbstractMap.SimpleEntry<Integer, String>(((Number) Array.get(item, 0)).intValue(), str));
                }                    
            }
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}