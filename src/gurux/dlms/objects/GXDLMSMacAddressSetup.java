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

public class GXDLMSMacAddressSetup extends GXDLMSObject implements IGXDLMSBase
{
    private String m_MacAddress;
    /**  
     Constructor.
    */
    public GXDLMSMacAddressSetup()
    {
        super(ObjectType.MAC_ADDRESS_SETUP, "0.0.25.2.0.255", 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSMacAddressSetup(String ln)
    {
        super(ObjectType.MAC_ADDRESS_SETUP, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSMacAddressSetup(String ln, int sn)
    {
        super(ObjectType.MAC_ADDRESS_SETUP, ln, sn);
    }

    /** 
     Value of COSEM Data object.
    */
    public final String getMacAddress()
    {
        return m_MacAddress;
    }
    public final void setMacAddress(String value)
    {
        m_MacAddress = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getMacAddress()};
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
        //MacAddress
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
            if (m_MacAddress == null)
            {
                return m_MacAddress;
            }
            return getMacAddress().replaceAll(":", ".");
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
            String add = GXDLMSClient.changeType((byte[]) value, DataType.OCTET_STRING).toString();
            add = add.replaceAll(".", ":");
            setMacAddress(add);
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}