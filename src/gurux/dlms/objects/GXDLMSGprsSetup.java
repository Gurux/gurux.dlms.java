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
import java.lang.reflect.Array;

public class GXDLMSGprsSetup extends GXDLMSObject implements IGXDLMSBase
{
    private String privateAPN;
    private long privatePINCode;
    GXDLMSQualityOfService m_DefaultQualityOfService;
    GXDLMSQualityOfService m_RequestedQualityOfService;   
            
    /**  
     Constructor.
    */
    public GXDLMSGprsSetup()
    {
        super(ObjectType.GPRS_SETUP);
        m_DefaultQualityOfService = new GXDLMSQualityOfService();
        m_RequestedQualityOfService = new GXDLMSQualityOfService();
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSGprsSetup(String ln)
    {
        super(ObjectType.GPRS_SETUP, ln, 0);
        m_DefaultQualityOfService = new GXDLMSQualityOfService();
        m_RequestedQualityOfService = new GXDLMSQualityOfService();
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSGprsSetup(String ln, int sn)
    {
        super(ObjectType.GPRS_SETUP, ln, sn);
    }

    public final String getAPN()
    {
        return privateAPN;
    }
    public final void setAPN(String value)
    {
        privateAPN = value;
    }

    public final long getPINCode()
    {
        return privatePINCode;
    }
    public final void setPINCode(long value)
    {
        privatePINCode = value;
    }

    public final GXDLMSQualityOfService getDefaultQualityOfService()
    {
        return m_DefaultQualityOfService;
    }

    public final GXDLMSQualityOfService getRequestedQualityOfService()
    {
        return m_RequestedQualityOfService;
    }

    
    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getAPN(), getPINCode(), getDefaultQualityOfService(), getRequestedQualityOfService()};
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
        //APN
        if (!isRead(2))
        {
            attributes.add(2);
        }            
        //PINCode
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //DefaultQualityOfService + RequestedQualityOfService
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
        if (index == 2)
        {
            type[0] = DataType.OCTET_STRING;
            return getAPN();
        }  
        if (index == 3)
        {
            type[0] = DataType.UINT16;
            return getPINCode();
        }
        if (index == 4)
        {
            type[0] = DataType.ARRAY;
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.STRUCTURE.getValue());
            data.write((byte) 2);
            data.write((byte)DataType.STRUCTURE.getValue());            
            try
            {
                data.write((byte) 5);
                GXCommon.setData(data, DataType.UINT8, m_DefaultQualityOfService.getPrecedence());
                GXCommon.setData(data, DataType.UINT8, m_DefaultQualityOfService.getDelay());
                GXCommon.setData(data, DataType.UINT8, m_DefaultQualityOfService.getReliability());
                GXCommon.setData(data, DataType.UINT8, m_DefaultQualityOfService.getPeakThroughput());
                GXCommon.setData(data, DataType.UINT8, m_DefaultQualityOfService.getMeanThroughput());
                data.write((byte) 5);
                GXCommon.setData(data, DataType.UINT8, m_RequestedQualityOfService.getPrecedence());
                GXCommon.setData(data, DataType.UINT8, m_RequestedQualityOfService.getDelay());
                GXCommon.setData(data, DataType.UINT8, m_RequestedQualityOfService.getReliability());
                GXCommon.setData(data, DataType.UINT8, m_RequestedQualityOfService.getPeakThroughput());
                GXCommon.setData(data, DataType.UINT8, m_RequestedQualityOfService.getMeanThroughput());
                return data.toByteArray();
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
            setAPN(GXDLMSClient.changeType((byte[]) value, DataType.STRING).toString());
        }
        else if (index == 3)
        {            
            setPINCode(((Number)value).intValue());
        }
        else if (index == 4)
        {            
            Object[] arr = (Object[]) Array.get(value, 0);
            m_DefaultQualityOfService.setPrecedence(((Number) Array.get(arr, 0)).intValue());
            m_DefaultQualityOfService.setDelay(((Number) Array.get(arr, 1)).intValue());
            m_DefaultQualityOfService.setReliability(((Number) Array.get(arr, 2)).intValue());
            m_DefaultQualityOfService.setPeakThroughput(((Number) Array.get(arr, 3)).intValue());
            m_DefaultQualityOfService.setMeanThroughput(((Number) Array.get(arr, 4)).intValue());
            arr = (Object[]) Array.get(value, 1);
            m_RequestedQualityOfService.setPrecedence(((Number) Array.get(arr, 0)).intValue());
            m_RequestedQualityOfService.setDelay(((Number) Array.get(arr, 1)).intValue());
            m_RequestedQualityOfService.setReliability(((Number) Array.get(arr, 2)).intValue());
            m_RequestedQualityOfService.setPeakThroughput(((Number) Array.get(arr, 3)).intValue());
            m_RequestedQualityOfService.setMeanThroughput(((Number) Array.get(arr, 4)).intValue());
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}