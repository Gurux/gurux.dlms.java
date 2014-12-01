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

import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class GXDLMSRegisterActivation extends GXDLMSObject implements IGXDLMSBase
{
    List<GXDLMSObjectDefinition> RegisterAssignment = new ArrayList<GXDLMSObjectDefinition>();           
    List<AbstractMap.SimpleEntry<byte[], byte[]>> MaskList = new ArrayList<AbstractMap.SimpleEntry<byte[], byte[]>>();        
    private byte[] ActiveMask;
    /**  
     Constructor.
    */
    public GXDLMSRegisterActivation()
    {
        super(ObjectType.REGISTER_ACTIVATION);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSRegisterActivation(String ln)
    {
        super(ObjectType.REGISTER_ACTIVATION, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSRegisterActivation(String ln, int sn)
    {
        super(ObjectType.REGISTER_ACTIVATION, ln, sn);
    }

    /** 

    */
    public final List<GXDLMSObjectDefinition> getRegisterAssignment()
    {
        return RegisterAssignment;
    }

    /** 

    */
    public final List<AbstractMap.SimpleEntry<byte[], byte[]>> getMaskList()
    {
        return MaskList;
    }

    public final byte[] getActiveMask()
    {
        return ActiveMask;
    }
    public final void setActiveMask(byte[] value)
    {
        ActiveMask = value;
    }

    /*
     * Add register.
     */
    void addRegister(GXDLMSObjectDefinition item)
    {
        RegisterAssignment.add(item);
    }
    
    /*
     * Add mask.
     */
    void addMask()
    {

    }
    
    /*
     * delete mask.
     */
    void deleteMask()
    {

    }
        
    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getRegisterAssignment(), getMaskList(), getActiveMask()};
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
        //RegisterAssignment
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //MaskList
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //ActiveMask
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
        return 3;
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
        if (index == 3)
        {
            return DataType.ARRAY;
        }
        if (index == 4)
        {
            return DataType.OCTET_STRING;
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
            ByteArrayOutputStream data = new ByteArrayOutputStream();            
            data.write((byte)DataType.ARRAY.getValue());
            data.write((byte)getRegisterAssignment().size());
            try
            {
                for(GXDLMSObjectDefinition it : getRegisterAssignment())
                {            
                    data.write((byte)DataType.STRUCTURE.getValue());
                    data.write(2);                
                    GXCommon.setData(data, DataType.UINT16, it.getClassId().getValue());
                    GXCommon.setData(data, DataType.OCTET_STRING, it.getLogicalName());
                }
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
            return data.toByteArray();
        }
        if (index == 3)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();            
            data.write((byte)DataType.ARRAY.getValue());
            data.write((byte)MaskList.size());
            for(AbstractMap.SimpleEntry<byte[], byte[]> it : MaskList)
            {
                data.write((byte)DataType.STRUCTURE.getValue());
                data.write(2);
                GXCommon.setData(data, DataType.OCTET_STRING, it.getKey());
                data.write((byte)DataType.ARRAY.getValue());
                data.write((byte)it.getValue().length);
                for (byte b : it.getValue())
                {
                    GXCommon.setData(data, DataType.UINT8, b);
                }
            }
            return data.toByteArray();
        }
        if (index == 4)
        {
            try
            {
                return getActiveMask();
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
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
            RegisterAssignment.clear();
            if (value != null)
            {
                for(Object it : (Object[]) value)
                {
                    GXDLMSObjectDefinition item = new GXDLMSObjectDefinition();
                    item.setClassId(ObjectType.forValue(((Number)Array.get(it, 0)).intValue()));
                    item.setLogicalName(GXDLMSObject.toLogicalName((byte[]) Array.get(it, 1)));
                    RegisterAssignment.add(item);
                }
            }            
        }
        else if (index == 3)
        {
            MaskList.clear();
            if (value != null)
            {
                for(Object it : (Object[]) value)
                {                    
                    byte[] key = (byte[]) Array.get(it, 0);
                    Object arr = Array.get(it, 1);
                    byte[] tmp = new byte[Array.getLength(arr)];
                    for(int pos = 0; pos != tmp.length; ++pos)
                    {
                        tmp[pos] = ((Number)Array.get(arr, pos)).byteValue();
                    }
                    MaskList.add(new AbstractMap.SimpleEntry<byte[], byte[]>(key, tmp));        
                }
            }             
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setActiveMask(null);
            }
            else
            {
                setActiveMask((byte[]) value);
            }
        }        
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}