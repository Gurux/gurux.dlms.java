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

import gurux.dlms.enums.AddressState;
import gurux.dlms.enums.BaudRate;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;

/**
 * This IC allows modelling and configuring communication channels 
 * according to EN 13757-2.Several communication channels can be configured.
 */
public class GXDLMSMBusSlavePortSetup extends GXDLMSObject implements IGXDLMSBase
{
    BaudRate m_DefaultBaud;
    BaudRate m_AvailableBaud;
    AddressState m_AddressState;
    int m_BusAddress;      

    /**  
     Constructor.
     @param ln Logical Name of the object.
    */
    public GXDLMSMBusSlavePortSetup()
    {
        super(ObjectType.MBUS_SLAVE_PORT_SETUP);        
    }
    
    /**  
     Constructor.
     @param ln Logical Name of the object.
    */
    public GXDLMSMBusSlavePortSetup(String ln)
    {
        super(ObjectType.MBUS_SLAVE_PORT_SETUP, ln, 0);        
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSMBusSlavePortSetup(String ln, int sn)
    {
        super(ObjectType.MBUS_SLAVE_PORT_SETUP, ln, sn);        
    }   
          
    /** 
     Defines the baud rate for the opening sequence.
    */
    public final BaudRate getDefaultBaud()
    {
        return m_DefaultBaud;
    }
    public final void setDefaultBaud(BaudRate value)
    {
    	m_DefaultBaud = value;
    }    
    /** 
     Defines the baud rate for the opening sequence.
    */
    public final BaudRate getAvailableBaud()
    {
        return m_AvailableBaud;
    }
    public final void setAvailableBaud(BaudRate value)
    {
    	m_AvailableBaud = value;
    }
        
    /** 
     Defines whether or not the device has been assigned an address
     * since last power up of the device.
    */
    public final AddressState getAddressState()
    {
        return m_AddressState;
    }
    public final void setAddressState(AddressState value)
    {
        m_AddressState = value;
    }
    
    
    /** 
     Defines the baud rate for the opening sequence.
    */
    public final int getBusAddress()
    {
        return m_BusAddress;
    }
    public final void setBusAddress(int value)
    {
    	m_BusAddress = value;
    }
    
    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getDefaultBaud(), 
            getAvailableBaud(), getAddressState(), getBusAddress()
        };
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
        //DefaultBaud
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //AvailableBaud
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //AddressState
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //BusAddress
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
            return DataType.ENUM;
        }
        if (index == 4)
        {
            return DataType.ENUM;
        }
        if (index == 5)
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
            return getDefaultBaud().ordinal();
        }
        if (index == 3)
        {
            return getAvailableBaud().ordinal();
        }
        if (index == 4)
        {
            return getAddressState().ordinal();
        }
        if (index == 5)
        {
            return getBusAddress();
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
                setDefaultBaud(BaudRate.BAUDRATE_300);                
            }
            else
            {
                setDefaultBaud(BaudRate.values()[((Number) value).intValue()]);
            }
        }
        else if (index == 3)
        {
            if (value == null)
            {
                setAvailableBaud(BaudRate.BAUDRATE_300);                
            }
            else
            {
                setAvailableBaud(BaudRate.values()[((Number) value).intValue()]);
            }           
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setAddressState(AddressState.NONE);                
            }
            else
            {
                setAddressState(AddressState.values()[((Number) value).intValue()]);
            }          
        }
        else if (index == 5)
        {
            if (value == null)
            {
                setBusAddress(0);
            }
            else
            {
                setBusAddress(((Number) value).intValue());
            }
        }              
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }    
}
