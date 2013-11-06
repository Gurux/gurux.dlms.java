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
import gurux.dlms.enums.GXDLMSIp4SetupIpOptionType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSIp4Setup extends GXDLMSObject implements IGXDLMSBase
{
    private String privateDataLinkLayerReference;
    private long privateIPAddress;
    private long[] privateMulticastIPAddress;
    private GXDLMSIp4SetupIpOption[] privateIPOptions;
    private long privateSubnetMask;
    private long privateGatewayIPAddress;
    private boolean privateUseDHCP;
    private long privatePrimaryDNSAddress;
    private long privateSecondaryDNSAddress;

    /**  
     Constructor.
    */
    public GXDLMSIp4Setup()
    {
        super(ObjectType.IP4_SETUP);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSIp4Setup(String ln)
    {
        super(ObjectType.IP4_SETUP, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSIp4Setup(String ln, int sn)
    {
        super(ObjectType.IP4_SETUP, ln, sn);
    }

    public final String getDataLinkLayerReference()
    {
        return privateDataLinkLayerReference;
    }
    public final void setDataLinkLayerReference(String value)
    {
        privateDataLinkLayerReference = value;
    }

    public final long getIPAddress()
    {
        return privateIPAddress;
    }

    public final void setIPAddress(long value)
    {
        privateIPAddress = value;
    }

    public final long[] getMulticastIPAddress()
    {
        return privateMulticastIPAddress;
    }
    public final void setMulticastIPAddress(long[] value)
    {
        privateMulticastIPAddress = value;
    }

    public final GXDLMSIp4SetupIpOption[] getIPOptions()
    {
        return privateIPOptions;
    }
    public final void setIPOptions(GXDLMSIp4SetupIpOption[] value)
    {
        privateIPOptions = value;
    }

    public final long getSubnetMask()
    {
        return privateSubnetMask;
    }

    public final void setSubnetMask(long value)
    {
        privateSubnetMask = value;
    }

    public final long getGatewayIPAddress()
    {
        return privateGatewayIPAddress;
    }
    public final void setGatewayIPAddress(long value)
    {
        privateGatewayIPAddress = value;
    }

    public final boolean getUseDHCP()
    {
        return privateUseDHCP;
    }
    public final void setUseDHCP(boolean value)
    {
        privateUseDHCP = value;
    }

    public final long getPrimaryDNSAddress()
    {
        return privatePrimaryDNSAddress;
    }
    public final void setPrimaryDNSAddress(long value)
    {
        privatePrimaryDNSAddress = value;
    }

    public final long getSecondaryDNSAddress()
    {
        return privateSecondaryDNSAddress;
    }

    public final void setSecondaryDNSAddress(long value)
    {
        privateSecondaryDNSAddress = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getDataLinkLayerReference(), getIPAddress(), getMulticastIPAddress(), getIPOptions(), getSubnetMask(), getGatewayIPAddress(), getUseDHCP(), getPrimaryDNSAddress(), getSecondaryDNSAddress()};
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
        //DataLinkLayerReference
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //IPAddress
        if (canRead(3))
        {
            attributes.add(3);
        }
        //MulticastIPAddress
        if (canRead(4))
        {
            attributes.add(4);
        }
        //IPOptions
        if (canRead(5))
        {
            attributes.add(5);
        }
        //SubnetMask
        if (canRead(6))
        {
            attributes.add(6);
        }
        //GatewayIPAddress
        if (canRead(7))
        {
            attributes.add(7);
        }
        //UseDHCP
        if (!isRead(8))
        {
            attributes.add(8);
        }
        //PrimaryDNSAddress
        if (canRead(9))
        {
            attributes.add(9);
        }
        //SecondaryDNSAddress
        if (canRead(10))
        {
            attributes.add(10);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 10;
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
            type[0] = DataType.OCTET_STRING;
            return this.getDataLinkLayerReference();
        }
        if (index == 3)
        {
            type[0] = DataType.UINT16;
            return this.getIPAddress();
        }
        if (index == 4)
        {
            type[0] = DataType.ARRAY;
            return this.getMulticastIPAddress();
        }
        if (index == 5)
        {
            type[0] = DataType.ARRAY;
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.ARRAY.getValue());
            if (privateIPOptions == null)
            {
                data.write(1);
            }
            else
            {
                GXCommon.setObjectCount(privateIPOptions.length, data);
                for (GXDLMSIp4SetupIpOption it : privateIPOptions)
                {
                    data.write((byte)DataType.STRUCTURE.getValue());
                    data.write(3);
                    try 
                    {
                        GXCommon.setData(data, DataType.UINT8, it.getType());
                        GXCommon.setData(data, DataType.UINT8, it.getLength());
                        GXCommon.setData(data, DataType.OCTET_STRING, it.getData());                        
                    } 
                    catch (Exception ex)
                    {
                        Logger.getLogger(GXDLMSIp4Setup.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return data.toByteArray();
        }
        if (index == 6)
        {
            type[0] = DataType.UINT32;
            return this.getSubnetMask();
        }
        if (index == 7)
        {
            type[0] = DataType.UINT32;
            return this.getGatewayIPAddress();
        }
        if (index == 8)
        {
            type[0] = DataType.BOOLEAN;
            return this.getUseDHCP();
        }
        if (index == 9)
        {
            type[0] = DataType.UINT32;
            return this.getPrimaryDNSAddress();
        }
        if (index == 10)
        {
            type[0] = DataType.UINT32;
            return this.getSecondaryDNSAddress();
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
                this.setDataLinkLayerReference(value.toString());
            }
            else
            {
                this.setDataLinkLayerReference(GXDLMSClient.changeType((byte[])value, DataType.OCTET_STRING).toString());
            }
    }
    else if (index == 3)
    {
        setIPAddress(((Number)value).intValue());
    }
    else if (index == 4)
    {
        java.util.ArrayList<Long> data = new java.util.ArrayList<Long>();
        if (value != null)
        {
            for (Object it : (Object[])value)
            {
                data.add(((Number)it).longValue());
            }
        }
        setMulticastIPAddress(toLongArray(data));
    }
    else if (index == 5)
    {
        java.util.ArrayList<GXDLMSIp4SetupIpOption> data = new java.util.ArrayList<GXDLMSIp4SetupIpOption>();
        if (value != null)
        {
            for (Object it : (Object[])value)
            {
                GXDLMSIp4SetupIpOption item = new GXDLMSIp4SetupIpOption();
                item.setType(GXDLMSIp4SetupIpOptionType.forValue(((Number)(Array.get(it, 1))).intValue()));
                item.setLength(((Number)(Array.get(it, 1))).shortValue());
                item.setData((byte[]) Array.get(it, 2));
                data.add(item);
            }
        }
        setIPOptions(data.toArray(new GXDLMSIp4SetupIpOption[data.size()]));
    }
    else if (index == 6)
    {
        setSubnetMask(((Number)value).intValue());
    }
    else if (index == 7)
    {
        setGatewayIPAddress(((Number)value).intValue());
    }
    else if (index == 8)
    {
        setUseDHCP((Boolean)value);
    }
    else if (index == 9)
    {
        setPrimaryDNSAddress(((Number)value).intValue());
    }
    else if (index == 10)
    {
        setSecondaryDNSAddress(((Number)value).intValue());
    }
    else
    {
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
    }
}