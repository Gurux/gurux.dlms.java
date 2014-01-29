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
import java.lang.reflect.Array;

public class GXDLMSPppSetup extends GXDLMSObject implements IGXDLMSBase
{
    private GXDLMSPppSetupIPCPOption[] IPCPOptions;
    private String PHYReference;
    private GXDLMSPppSetupLcpOption[] LCPOptions;
    private byte[] UserName;
    private byte[] Password;

    private PppAuthenticationType m_Authentication;
    
    public final PppAuthenticationType getAuthentication()
    {
        return m_Authentication;
    }
    public final void setAuthentication(PppAuthenticationType value)
    {
        m_Authentication = value;
    }    
    
    /** 
    PPP authentication procedure user name.
   */    
    public final byte[] getUserName()
    {
        return UserName;
    }

    public final void setUserName(byte[] value)
    {
        UserName = value;
    }

    /** 
     PPP authentication procedure password.
    */

    public final byte[] getPassword()
    {
        return Password;
    }
    public final void setPassword(byte[] value)
    {
        Password = value;
    }
    
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
        return PHYReference;
    }
    public final void setPHYReference(String value)
    {
        PHYReference = value;
    }

    public final GXDLMSPppSetupLcpOption[] getLCPOptions()
    {
        return LCPOptions;
    }
    public final void setLCPOptions(GXDLMSPppSetupLcpOption[] value)
    {
        LCPOptions = value;
    }

    public final GXDLMSPppSetupIPCPOption[] getIPCPOptions()
    {
        return IPCPOptions;
    }
    public final void setIPCPOptions(GXDLMSPppSetupIPCPOption[] value)
    {
        IPCPOptions = value;
    }   

    @Override
    public Object[] getValues()
    {
        String str = "";
        if (UserName != null)
        {
            str = new String(UserName);
        }
        if (Password != null)
        {
            str += " " + new String(Password);
        }
        return new Object[] {getLogicalName(), getPHYReference(), getLCPOptions(), getIPCPOptions(), str};
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
        else if (index == 2)
        {
            if (value instanceof String)
            {
                PHYReference = value.toString();
            }
            else
            {
                PHYReference = GXDLMSClient.changeType((byte[])value, DataType.OCTET_STRING).toString();
            }
        }
        else if (index == 3)
        {
            java.util.ArrayList<GXDLMSPppSetupLcpOption> items = new java.util.ArrayList<GXDLMSPppSetupLcpOption>();
            if (value != null)
            {
                for (Object item : (Object[])value)
                {
                    GXDLMSPppSetupLcpOption it = new GXDLMSPppSetupLcpOption();
                    it.setType(GXDLMSPppSetupLcpOptionType.forValue((int) Array.get(item, 0)));
                    it.setLength((int)Array.get(item, 1));
                    it.setData(Array.get(item, 2));
                    items.add(it);
                }
            }
            LCPOptions = items.toArray(new GXDLMSPppSetupLcpOption[0]);
        }
        else if (index == 4)
        {
            java.util.ArrayList<GXDLMSPppSetupIPCPOption> items = new java.util.ArrayList<GXDLMSPppSetupIPCPOption>();
            if (value != null)
            {
                for (Object item : (Object[])value)
                {
                    GXDLMSPppSetupIPCPOption it = new GXDLMSPppSetupIPCPOption();
                    it.setType(GXDLMSPppSetupIPCPOptionType.forValue((int)Array.get(item, 0)));
                    it.setLength((int)Array.get(item, 1));
                    it.setData(Array.get(item, 2));
                    items.add(it);
                }
            }
            IPCPOptions = items.toArray(new GXDLMSPppSetupIPCPOption[0]);
        }
        else if (index == 5)
        {
            UserName = (byte[])((Object[])value)[0];
            Password = (byte[])((Object[])value)[1];
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}