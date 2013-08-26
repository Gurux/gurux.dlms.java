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
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GXDLMSRegisterActivation extends GXDLMSObject implements IGXDLMSBase
{
    List<GXDLMSObjectDefinition> privateRegisterAssignment = new ArrayList<GXDLMSObjectDefinition>();           
    private Object privateMaskList;
    private String privateActiveMask;
    /**  
     Constructor.
    */
    public GXDLMSRegisterActivation()
    {
        super(ObjectType.REGISTER_ACTIVATION);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSRegisterActivation(String ln)
    {
        super(ObjectType.REGISTER_ACTIVATION, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
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
        return privateRegisterAssignment;
    }

    /** 

    */
    public final Object getMaskList()
    {
        return privateMaskList;
    }
    public final void setMaskList(Object value)
    {
        privateMaskList = value;
    }

    public final String getActiveMask()
    {
        return privateActiveMask;
    }
    public final void setActiveMask(String value)
    {
        privateActiveMask = value;
    }

    /*
     * Add register.
     */
    void addRegister(GXDLMSObjectDefinition item)
    {
        privateRegisterAssignment.add(item);
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
    
    @Override
    public void invoke(int index, Object parameters)
    {
        throw new IllegalArgumentException("Invoke failed. Invalid attribute index.");
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
            return getMaskList();
        }
        if (index == 4)
        {
            type[0] = DataType.OCTET_STRING;
            try
            {
                return getActiveMask().getBytes("ASCII");
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
    public void setValue(int index, Object value, boolean raw)
    {
        if (index == 1)
        {
            setLogicalName(GXDLMSObject.toLogicalName((byte[]) value));            
        }
        else if (index == 2)
        {
            privateRegisterAssignment.clear();
            if (value != null)
            {
                for(Object it : (Object[]) value)
                {
                    GXDLMSObjectDefinition item = new GXDLMSObjectDefinition();
                    item.setClassId(ObjectType.forValue(((Number)Array.get(it, 0)).intValue()));
                    item.setLogicalName(GXDLMSObject.toLogicalName((byte[]) Array.get(it, 1)));
                    privateRegisterAssignment.add(item);
                }
            }            
        }
        else if (index == 3)
        {
            setMaskList(value);
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setActiveMask(null);
            }
            else
            {
                setActiveMask(GXDLMSClient.changeType((byte[]) value, DataType.STRING).toString());
            }
        }        
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}