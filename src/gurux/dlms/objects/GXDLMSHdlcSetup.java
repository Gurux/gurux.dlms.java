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

import gurux.dlms.enums.BaudRate;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;

public class GXDLMSHdlcSetup extends GXDLMSObject implements IGXDLMSBase
{
    private int InactivityTimeout;
    private int DeviceAddress;
    private int MaximumInfoLengthTransmit;
    private BaudRate CommunicationSpeed;
    private int WindowSizeTransmit;
    private int WindowSizeReceive;
    private int InterCharachterTimeout;
    private int MaximumInfoLengthReceive;

    /**  
     Constructor.
    */
    public GXDLMSHdlcSetup()
    {
        super(ObjectType.IEC_HDLC_SETUP);
        setCommunicationSpeed(BaudRate.BAUDRATE_9600);
        setWindowSizeTransmit(1);
        setWindowSizeReceive(getWindowSizeTransmit());
        setMaximumInfoLengthReceive(128);
        setMaximumInfoLengthTransmit(getMaximumInfoLengthReceive());
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
    */
    public GXDLMSHdlcSetup(String ln)
    {
        super(ObjectType.IEC_HDLC_SETUP, ln, 0);
        setCommunicationSpeed(BaudRate.BAUDRATE_9600);
        setWindowSizeTransmit(1);
        setWindowSizeReceive(getWindowSizeTransmit());
        setMaximumInfoLengthReceive(128);
        setMaximumInfoLengthTransmit(getMaximumInfoLengthReceive());
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSHdlcSetup(String ln, int sn)
    {
        super(ObjectType.IEC_HDLC_SETUP, ln, 0);
        setCommunicationSpeed(BaudRate.BAUDRATE_9600);
        setWindowSizeTransmit(1);
        setWindowSizeReceive(getWindowSizeTransmit());
        setMaximumInfoLengthReceive(128);
        setMaximumInfoLengthTransmit(getMaximumInfoLengthReceive());
    }
   
    public final BaudRate getCommunicationSpeed()
    {
        return CommunicationSpeed;
    }
    public final void setCommunicationSpeed(BaudRate value)
    {
        CommunicationSpeed = value;
    }

    public final int getWindowSizeTransmit()
    {
        return WindowSizeTransmit;
    }
    public final void setWindowSizeTransmit(int value)
    {
        WindowSizeTransmit = value;
    }

    public final int getWindowSizeReceive()
    {
        return WindowSizeReceive;
    }
    public final void setWindowSizeReceive(int value)
    {
        WindowSizeReceive = value;
    }

    public final int getMaximumInfoLengthTransmit()
    {
        return MaximumInfoLengthTransmit;
    }
    public final void setMaximumInfoLengthTransmit(int value)
    {
        MaximumInfoLengthTransmit = value;
    }

    public final int getMaximumInfoLengthReceive()
    {
        return MaximumInfoLengthReceive;
    }
    public final void setMaximumInfoLengthReceive(int value)
    {
        MaximumInfoLengthReceive = value;
    }

    public final int getInterCharachterTimeout()
    {
        return InterCharachterTimeout;
    }
    public final void setInterCharachterTimeout(int value)
    {
        InterCharachterTimeout = value;
    }

    public final int getInactivityTimeout()
    {
        return InactivityTimeout;
    }
    public final void setInactivityTimeout(int value)
    {
        InactivityTimeout = value;
    }

    public final int getDeviceAddress()
    {
        return DeviceAddress;
    }
    public final void setDeviceAddress(int value)
    {
        DeviceAddress = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getCommunicationSpeed(), getWindowSizeTransmit(), getWindowSizeReceive(), getMaximumInfoLengthTransmit(), getMaximumInfoLengthReceive(), getInterCharachterTimeout(), getInactivityTimeout(), getDeviceAddress()};
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
        //CommunicationSpeed
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //WindowSizeTransmit
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //WindowSizeReceive
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //MaximumInfoLengthTransmit
        if (!isRead(5))
        {
            attributes.add(5);
        }
        //MaximumInfoLengthReceive
        if (!isRead(6))
        {
            attributes.add(6);
        }
        //InterCharachterTimeout
        if (!isRead(7))
        {
            attributes.add(7);
        }
        //InactivityTimeout
        if (!isRead(8))
        {
            attributes.add(8);
        }
        //DeviceAddress
        if (!isRead(9))
        {
            attributes.add(9);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 9;
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
            return DataType.ENUM;
        }
        if (index == 3)
        {
            return DataType.UINT8;
        }
        if (index == 4)
        {
            return DataType.UINT8;
        }
        if (index == 5)
        {
            return DataType.UINT16;
        }
        if (index == 6)
        {
            return DataType.UINT16;
        }
        if (index == 7)
        {
            return DataType.UINT16;
        }
        if (index == 8)
        {
            return DataType.UINT16;
        }
        if (index == 9)
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
            return this.CommunicationSpeed.ordinal();
        }
        if (index == 3)
        {
            return this.WindowSizeTransmit;
        }
        if (index == 4)
        {
            return this.WindowSizeReceive;
        }
        if (index == 5)
        {
            return this.MaximumInfoLengthTransmit;
        }
        if (index == 6)
        {
            return this.MaximumInfoLengthReceive;
        }
        if (index == 7)
        {
            return InterCharachterTimeout;
        }
        if (index == 8)
        {
            return InactivityTimeout;
        }
        if (index == 9)
        {
            return DeviceAddress;
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
            CommunicationSpeed = BaudRate.values()[((Number)value).intValue()];
        }
        else if (index == 3)
        {
            WindowSizeTransmit = ((Number)value).intValue();
        }
        else if (index == 4)
        {
            WindowSizeReceive = ((Number)value).intValue();
        }
        else if (index == 5)
        {
            MaximumInfoLengthTransmit = ((Number)value).intValue();
        }
        else if (index == 6)
        {
            MaximumInfoLengthReceive = ((Number)value).intValue();
        }
        else if (index == 7)
        {
            InterCharachterTimeout = ((Number)value).intValue();
        }
        else if (index == 8)
        {
            InactivityTimeout = ((Number)value).intValue();
        }
        else if (index == 9)
        {
            DeviceAddress = ((Number)value).intValue();
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}