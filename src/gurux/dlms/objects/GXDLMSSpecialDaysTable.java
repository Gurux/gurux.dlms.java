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
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSSpecialDaysTable extends GXDLMSObject implements IGXDLMSBase
{
    private GXDLMSSpecialDay[] Entries;
    /**  
     Constructor.
    */
    public GXDLMSSpecialDaysTable()
    {
        super(ObjectType.SPECIAL_DAYS_TABLE);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSSpecialDaysTable(String ln)
    {
        super(ObjectType.SPECIAL_DAYS_TABLE, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSSpecialDaysTable(String ln, int sn)
    {
        super(ObjectType.SPECIAL_DAYS_TABLE, ln, sn);
    }

    /** 
     Value of COSEM Data object.
    */
    public final GXDLMSSpecialDay[] getEntries()
    {
        return Entries;
    }
    public final void setEntries(GXDLMSSpecialDay[] value)
    {
        Entries = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getEntries()};
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
        //Entries
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
        return 2;
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
            int cnt = getEntries().length;
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.ARRAY.getValue());
            //Add count            
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0)
            {
                try
                {
                    for (GXDLMSSpecialDay it : Entries)
                    {
                        data.write((byte)DataType.STRUCTURE.getValue());
                        data.write((byte)3); //Count
                        GXCommon.setData(data, DataType.UINT16, it.getIndex());
                        GXCommon.setData(data, DataType.DATETIME, it.getDate());
                        GXCommon.setData(data, DataType.UINT8, it.getDayId());
                    }
                }
                catch(Exception ex)
                {
                    Logger.getLogger(GXDLMSAssociationShortName.class.getName()).log(Level.SEVERE, null, ex);
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
            Entries = null;
            if (value != null)
            {
                java.util.ArrayList<GXDLMSSpecialDay> items = new java.util.ArrayList<GXDLMSSpecialDay>();
                for (Object item : (Object[])value)
                {
                    GXDLMSSpecialDay it = new GXDLMSSpecialDay();
                    
                    it.setIndex(((Number)Array.get(item, 0)).intValue());
                    it.setDate((GXDateTime)GXDLMSClient.changeType((byte[])Array.get(item, 1), DataType.DATE));
                    it.setDayId(((Number)Array.get(item, 2)).intValue());
                    items.add(it);
                }
                Entries = items.toArray(new GXDLMSSpecialDay[0]);
            }
        }       
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}