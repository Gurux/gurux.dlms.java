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

public class GXDLMSPppSetup extends GXDLMSObject implements IGXDLMSBase
{
    private Object privatePPPAuthentication;
    private Object privateIPCPOptions;
    private String privatePHYReference;
    private Object privateLCPOptions;

    /**  
     Constructor.
    */
    public GXDLMSPppSetup()
    {
        super(ObjectType.PPP_SETUP);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSPppSetup(String ln)
    {
        super(ObjectType.PPP_SETUP, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSPppSetup(String ln, int sn)
    {
        super(ObjectType.PPP_SETUP, ln, sn);
    }

    public final String getPHYReference()
    {
        return privatePHYReference;
    }
    public final void setPHYReference(String value)
    {
        privatePHYReference = value;
    }

    public final Object getLCPOptions()
    {
        return privateLCPOptions;
    }
    public final void setLCPOptions(Object value)
    {
        privateLCPOptions = value;
    }

    public final Object getIPCPOptions()
    {
        return privateIPCPOptions;
    }
    public final void setIPCPOptions(Object value)
    {
        privateIPCPOptions = value;
    }

    public final Object getPPPAuthentication()
    {
        return privatePPPAuthentication;
    }
    public final void setPPPAuthentication(Object value)
    {
        privatePPPAuthentication = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getPHYReference(), getLCPOptions(), getIPCPOptions(), getPPPAuthentication()};
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
        //PHYReference
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //LCPOptions
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //IPCPOptions
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //PPPAuthentication
        if (!isRead(5))
        {
            attributes.add(5);
        }   
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 5;
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
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}