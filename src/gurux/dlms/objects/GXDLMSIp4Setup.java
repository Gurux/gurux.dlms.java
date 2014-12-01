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
    private String DataLinkLayerReference;
    private long IPAddress;
    private long[] MulticastIPAddress;
    private GXDLMSIp4SetupIpOption[] IPOptions;
    private long SubnetMask;
    private long GatewayIPAddress;
    private boolean UseDHCP;
    private long PrimaryDNSAddress;
    private long SecondaryDNSAddress;

    /**  
     Constructor.
    */
    public GXDLMSIp4Setup()
    {
        super(ObjectType.IP4_SETUP);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSIp4Setup(String ln)
    {
        super(ObjectType.IP4_SETUP, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSIp4Setup(String ln, int sn)
    {
        super(ObjectType.IP4_SETUP, ln, sn);
    }

    public final String getDataLinkLayerReference()
    {
        return DataLinkLayerReference;
    }
    public final void setDataLinkLayerReference(String value)
    {
        DataLinkLayerReference = value;
    }

    public final long getIPAddress()
    {
        return IPAddress;
    }

    public final void setIPAddress(long value)
    {
        IPAddress = value;
    }

    public final long[] getMulticastIPAddress()
    {
        return MulticastIPAddress;
    }
    public final void setMulticastIPAddress(long[] value)
    {
        MulticastIPAddress = value;
    }

    public final GXDLMSIp4SetupIpOption[] getIPOptions()
    {
        return IPOptions;
    }
    public final void setIPOptions(GXDLMSIp4SetupIpOption[] value)
    {
        IPOptions = value;
    }

    public final long getSubnetMask()
    {
        return SubnetMask;
    }

    public final void setSubnetMask(long value)
    {
        SubnetMask = value;
    }

    public final long getGatewayIPAddress()
    {
        return GatewayIPAddress;
    }
    public final void setGatewayIPAddress(long value)
    {
        GatewayIPAddress = value;
    }

    public final boolean getUseDHCP()
    {
        return UseDHCP;
    }
    public final void setUseDHCP(boolean value)
    {
        UseDHCP = value;
    }

    public final long getPrimaryDNSAddress()
    {
        return PrimaryDNSAddress;
    }
    public final void setPrimaryDNSAddress(long value)
    {
        PrimaryDNSAddress = value;
    }

    public final long getSecondaryDNSAddress()
    {
        return SecondaryDNSAddress;
    }

    public final void setSecondaryDNSAddress(long value)
    {
        SecondaryDNSAddress = value;
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
    public int[] getAttributeIndexToRead()
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
        if (index == 3)
        {
            return DataType.UINT32;
        }
        if (index == 4)
        {
            return DataType.ARRAY;
        }
        if (index == 5)
        {
            return DataType.ARRAY;
        }
        if (index == 6)
        {
            return DataType.UINT32;
        }
        if (index == 7)
        {
            return DataType.UINT32;
        }
        if (index == 8)
        {
            return DataType.BOOLEAN;
        }
        if (index == 9)
        {
            return DataType.UINT32;
        }
        if (index == 10)
        {
            return DataType.UINT32;
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
            return this.getDataLinkLayerReference();
        }
        if (index == 3)
        {
            return this.getIPAddress();
        }
        if (index == 4)
        {
            return this.getMulticastIPAddress();
        }
        if (index == 5)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.ARRAY.getValue());
            if (IPOptions == null)
            {
                data.write(1);
            }
            else
            {
                GXCommon.setObjectCount(IPOptions.length, data);
                for (GXDLMSIp4SetupIpOption it : IPOptions)
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
            return this.getSubnetMask();
        }
        if (index == 7)
        {
            return this.getGatewayIPAddress();
        }
        if (index == 8)
        {
            return this.getUseDHCP();
        }
        if (index == 9)
        {
            return this.getPrimaryDNSAddress();
        }
        if (index == 10)
        {
            return this.getSecondaryDNSAddress();
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