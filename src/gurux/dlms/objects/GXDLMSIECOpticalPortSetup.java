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
import gurux.dlms.enums.BaudRate;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.LocalPortResponseTime;
import gurux.dlms.enums.ObjectType;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSIECOpticalPortSetup extends GXDLMSObject implements IGXDLMSBase
{
    private String m_Password2;
    private String m_Password5;
    private OpticalProtocolMode m_DefaultMode;
    private BaudRate m_DefaultBaudrate;
    private BaudRate m_ProposedBaudrate;
    private LocalPortResponseTime m_ResponseTime;
    private String m_DeviceAddress;
    private String m_Password1;

    /**  
     Constructor.
    */
    public GXDLMSIECOpticalPortSetup()
    {
        this("0.0.20.0.0.255");
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSIECOpticalPortSetup(String ln)
    {
        this(ln, 0);
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSIECOpticalPortSetup(String ln, int sn)
    {
        super(ObjectType.IEC_LOCAL_PORT_SETUP, ln, sn);
    }

    public final OpticalProtocolMode getDefaultMode()
    {
        return m_DefaultMode;
    }
    public final void setDefaultMode(OpticalProtocolMode value)
    {
        m_DefaultMode = value;
    }

    public final BaudRate getDefaultBaudrate()
    {
        return m_DefaultBaudrate;
    }
    public final void setDefaultBaudrate(BaudRate value)
    {
        m_DefaultBaudrate = value;
    }

    public final BaudRate getProposedBaudrate()
    {
        return m_ProposedBaudrate;
    }
    public final void setProposedBaudrate(BaudRate value)
    {
        m_ProposedBaudrate = value;
    }

    public final LocalPortResponseTime getResponseTime()
    {
        return m_ResponseTime;
    }
    public final void setResponseTime(LocalPortResponseTime value)
    {
        m_ResponseTime = value;
    }

    public final String getDeviceAddress()
    {
        return m_DeviceAddress;
    }
    public final void setDeviceAddress(String value)
    {
        m_DeviceAddress = value;
    }

    public final String getPassword1()
    {
        return m_Password1;
    }
    public final void setPassword1(String value)
    {
        m_Password1 = value;
    }

    public final String getPassword2()
    {
        return m_Password2;
    }
    public final void setPassword2(String value)
    {
        m_Password2 = value;
    }

    public final String getPassword5()
    {
        return m_Password5;
    }
    public final void setPassword5(String value)
    {
        m_Password5 = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getDefaultMode(), 
            getDefaultBaudrate(), getProposedBaudrate(), getResponseTime(), 
            getDeviceAddress(), getPassword1(), getPassword2(), getPassword5()};
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
        //DefaultMode
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //DefaultBaudrate
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //ProposedBaudrate
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //ResponseTime
        if (!isRead(5))
        {
            attributes.add(5);
        }
        //DeviceAddress
        if (!isRead(6))
        {
            attributes.add(6);
        }
        //Password1
        if (!isRead(7))
        {
            attributes.add(7);
        }
        //Password2
        if (!isRead(8))
        {
            attributes.add(8);
        }
        //Password5
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
            return DataType.ENUM;
        }
        if (index == 4)
        {
            return DataType.ENUM;
        }
        if (index == 5)
        {
            return DataType.ENUM;
        }
        if (index == 6)
        {
            return DataType.OCTET_STRING;
        }
        if (index == 7)
        {
            return DataType.OCTET_STRING;
        }
        if (index == 8)
        {
            return DataType.OCTET_STRING;
        }
        if (index == 9)
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
            return this.getDefaultMode().getValue();
        }
        if (index == 3)
        {
            return getDefaultBaudrate().ordinal();
        }
        if (index == 4)
        {
            return getProposedBaudrate().ordinal();
        }
        if (index == 5)
        {
            return getResponseTime().ordinal();
        }
        if (index == 6)
        {
            try 
            {
                if (getDeviceAddress() == null)
                {
                    return null;
                }
                return getDeviceAddress().getBytes("ASCII");
            }
            catch (UnsupportedEncodingException ex) 
            {
                Logger.getLogger(GXDLMSIECOpticalPortSetup.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        if (index == 7)
        {
            try 
            {
                if (getPassword1() == null)
                {
                    return null;
                }
                return getPassword1().getBytes("ASCII");
            }
            catch (UnsupportedEncodingException ex) 
            {
                Logger.getLogger(GXDLMSIECOpticalPortSetup.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }            
        }
        if (index == 8)
        {
            try 
            {               
                if (getPassword2() == null)
                {
                    return null;
                }
                return getPassword2().getBytes("ASCII");
            }
            catch (UnsupportedEncodingException ex) 
            {
                Logger.getLogger(GXDLMSIECOpticalPortSetup.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }            
        }
        if (index == 9)
        {
            try 
            {            
                if (getPassword5() == null)
                {
                    return null;
                }
                return getPassword5().getBytes("ASCII");
            }
            catch (UnsupportedEncodingException ex) 
            {
                Logger.getLogger(GXDLMSIECOpticalPortSetup.class.getName()).log(Level.SEVERE, null, ex);
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
            setDefaultMode(OpticalProtocolMode.forValue(((Number)value).intValue()));
        }
        else if (index == 3)
        {
            setDefaultBaudrate(BaudRate.values()[((Number)value).intValue()]);
        }
        else if (index == 4)
        {
            setProposedBaudrate(BaudRate.values()[((Number)value).intValue()]);
        }
        else if (index == 5)
        {
            setResponseTime(LocalPortResponseTime.values()[((Number)value).intValue()]);
        }
        else if (index == 6)
        {
            setDeviceAddress(GXDLMSClient.changeType((byte[])value, DataType.STRING).toString());
        }
        else if (index == 7)
        {
            setPassword1(GXDLMSClient.changeType((byte[])value, DataType.STRING).toString());
        }
        else if (index == 8)
        {
            setPassword2(GXDLMSClient.changeType((byte[])value, DataType.STRING).toString());
        }
        else if (index == 9)
        {
            setPassword5(GXDLMSClient.changeType((byte[])value, DataType.STRING).toString());
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}