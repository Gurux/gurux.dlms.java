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

public class GXDLMSTcpUdpSetup extends GXDLMSObject implements IGXDLMSBase
{
    private int Port;
    private String IPReference;
    private int MaximumSimultaneousConnections;
    private int InactivityTimeout;
    private int MaximumSegmentSize;
    
    /**  
     Constructor.
    */
    public GXDLMSTcpUdpSetup()
    {
        this("0.0.25.0.0.255", (short) 0);        
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSTcpUdpSetup(String ln)
    {
        this(ln, (short) 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSTcpUdpSetup(String ln, int sn)
    {
        super(ObjectType.TCP_UDP_SETUP, ln, sn);
        setPort(4059);
        setInactivityTimeout(180);
        setMaximumSegmentSize(576);
    }

    public final int getPort()
    {
        return Port;
    }
    public final void setPort(int value)
    {
        Port = value;
    }

    public final String getIPReference()
    {
        return IPReference;
    }
    public final void setIPReference(String value)
    {
        IPReference = value;
    }

    public final int getMaximumSegmentSize()
    {
        return MaximumSegmentSize;
    }
    public final void setMaximumSegmentSize(int value)
    {
        MaximumSegmentSize = value;
    }
   
    public final int getMaximumSimultaneousConnections()
    {
        return MaximumSimultaneousConnections;
    }
    public final void setMaximumSimultaneousConnections(int value)
    {
        if (value < 0)
        {
            throw new IllegalArgumentException("Invalid value");
        }
        MaximumSimultaneousConnections = value;
    }

    public final int getInactivityTimeout()
    {
        return InactivityTimeout;
    }
    public final void setInactivityTimeout(int value)
    {
        InactivityTimeout = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getPort(), getIPReference(), 
            getMaximumSegmentSize(), getMaximumSimultaneousConnections(), 
            getInactivityTimeout()};
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
        //Port
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //IPReference
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //MaximumSegmentSize
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //MaximumSimultaneousConnections
        if (!isRead(5))
        {
            attributes.add(5);
        }
        //InactivityTimeout
        if (!isRead(6))
        {
            attributes.add(6);
        }
        return toIntArray(attributes);
    }
    
    @Override
    public int getAttributeCount()
    {
        return 6;
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
            return DataType.UINT16;
        } 
        if (index == 3)
        {
            return DataType.OCTET_STRING;
        }
        if (index == 4)
        {
            return DataType.UINT16;
        }
        if (index == 5)
        {
            return DataType.UINT8;
        }
        if (index == 6)
        {
            return DataType.UINT16;
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
            return getPort();
        } 
        if (index == 3)
        {
            return getIPReference();
        }
        if (index == 4)
        {
            return getMaximumSegmentSize();
        }
        if (index == 5)
        {
            return getMaximumSimultaneousConnections();
        }
        if (index == 6)
        {
            return getInactivityTimeout();
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
            if (value == null)
            {
                setPort(4059);
            }
            else
            {
                setPort(((Number) value).intValue());
            }
        } 
        else if (index == 3)
        {
            if (value == null)
            {
                setIPReference(null);
            }
            else
            {
                if (value instanceof byte[])
                {                    
                    setIPReference(GXDLMSClient.changeType((byte[]) value, DataType.OCTET_STRING).toString());
                }
                else
                {
                    setIPReference(String.valueOf(value));
                }
            }
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setMaximumSegmentSize(576);
            }
            else
            {
                setMaximumSegmentSize(((Number) value).intValue());
            }
        }
        else if (index == 5)
        {
            if (value == null)
            {
                setMaximumSimultaneousConnections(1);
            }
            else
            {
                setMaximumSimultaneousConnections(((Number) value).intValue());
            }
        }
        else if (index == 6)
        {
            if (value == null)
            {
                setInactivityTimeout(180);
            }
            else
            {
                setInactivityTimeout(((Number) value).intValue());
            }
        }
        else
        {
            throw new IllegalArgumentException("SetValue failed. Invalid attribute index.");
        }
    }
}