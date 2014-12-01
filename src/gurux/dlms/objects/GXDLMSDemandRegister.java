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
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Unit;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSDemandRegister extends GXDLMSObject implements IGXDLMSBase
{
    protected int m_Scaler;
    protected int m_Unit;
    private Object m_CurrentAvarageValue;    
    private Object m_LastAvarageValue;    
    private Object m_Status;
    private GXDateTime m_CaptureTime = new GXDateTime();
    private GXDateTime m_StartTimeCurrent = new GXDateTime();
    private int m_NumberOfPeriods;
    private BigInteger m_Period;
    /**  
     Constructor.
    */
    public GXDLMSDemandRegister()
    {
        this(null);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSDemandRegister(String ln)
    {
        this(ln, 0);
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSDemandRegister(String ln, int sn)
    {
        super(ObjectType.DEMAND_REGISTER, ln, sn);
        setScaler(1);
    }
    
    /** 
     Current avarage value of COSEM Data object.
    */
    public final Object getCurrentAvarageValue()
    {
        return m_CurrentAvarageValue;
    }
    public final void setCurrentAvarageValue(Object value)
    {
        m_CurrentAvarageValue = value;
    }

    /** 
     Last avarage value of COSEM Data object.
    */
    public final Object getLastAvarageValue()
    {
        return m_LastAvarageValue;
    }
    public final void setLastAvarageValue(Object value)
    {
        m_LastAvarageValue = value;
    }

    /** 
     Scaler of COSEM Register object.
    */
    public final double getScaler()
    {        
        return Math.pow(10, m_Scaler);
    }
    public final void setScaler(double value)
    {
        m_Scaler = (int)Math.log10(value);
    }

    /** 
     Unit of COSEM Register object.
    */
    public final Unit getUnit()
    {
        return Unit.forValue(m_Unit);
    }
    public final void setUnit(Unit value)
    {
        m_Unit = value.getValue();
    }

    /** 
     Scaler of COSEM Register object.
    */
    public final Object getStatus()
    {
        return m_Status;
    }
    public final void setStatus(Object value)
    {
        m_Status = value;
    }

    /** 
     Capture time of COSEM Register object.
    */
    public final GXDateTime getCaptureTime()
    {
        return m_CaptureTime;
    }
    public final void setCaptureTime(GXDateTime value)
    {
        m_CaptureTime = value;
    }

    /** 
     Current start time of COSEM Register object.
    */
    public final GXDateTime getStartTimeCurrent()
    {
        return m_StartTimeCurrent;
    }
    public final void setStartTimeCurrent(GXDateTime value)
    {
        m_StartTimeCurrent = value;
    }

    public final BigInteger getPeriod()
    {
        return m_Period;
    }

    public final void setPeriod(BigInteger value)
    {
        m_Period = value;
    }
    
    public final int getNumberOfPeriods()
    {
        return m_NumberOfPeriods;
    }

    public final void setNumberOfPeriods(int value)
    {
        m_NumberOfPeriods = value;
    }

    /*
     * Reset value.
     */
    void reset()
    {

    }
    
    /*
     * Next period.
     */
    void nextPeriod()
    {

    }
    
    
    
    @Override
    public Object[] getValues()
    {
        String str = String.format("Scaler: %1$,.2f Unit: ", getScaler());
        str += getUnit().toString();
        return new Object[] {getLogicalName(), getCurrentAvarageValue(), 
            getLastAvarageValue(), str, getStatus(), getCaptureTime(), 
            getStartTimeCurrent(), getPeriod(), getNumberOfPeriods()};
    }
    
    @Override
    public boolean isRead(int index)
    {
        if (index == 4)
        {
            return m_Unit != 0;
        }
        return super.isRead(index);
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
        //ScalerUnit
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //CurrentAvarageValue
        if (canRead(2))
        {
            attributes.add(2);
        }
        //LastAvarageValue            
        if (canRead(3))
        {
            attributes.add(3);
        }        
        //Status
        if (canRead(5))
        {
            attributes.add(5);
        }
        //CaptureTime
        if (canRead(6))
        {
            attributes.add(6);
        }
        //StartTimeCurrent
        if (canRead(7))
        {
            attributes.add(7);
        }
        //Period
        if (canRead(8))
        {
            attributes.add(8);
        }
        //NumberOfPeriods
        if (canRead(9))
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
        return 2;
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
            return super.getDataType(index);
        }
        if (index == 3)
        {
            return super.getDataType(index);
        }      
        if (index == 4)
        {
            return DataType.ARRAY;
        }
        if (index == 5)
        {
            return super.getDataType(index);
        }
        if (index == 6)
        {
            return DataType.DATETIME;
        }
        if (index == 7)
        {
            return DataType.DATETIME;
        }
        if (index == 8)
        {
            return DataType.UINT32;
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
            return getCurrentAvarageValue();
        }
        if (index == 3)
        {
            return getLastAvarageValue();
        }      
        if (index == 4)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write(DataType.STRUCTURE.getValue());
            data.write(2);            
            try 
            {                
                GXCommon.setData(data, DataType.INT8, m_Scaler);            
                GXCommon.setData(data, DataType.ENUM, m_Unit);
                return data.toByteArray();
            }
            catch (Exception ex) 
            {
                Logger.getLogger(GXDLMSRegister.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }
        if (index == 5)
        {
            return getStatus();
        }
        if (index == 6)
        {
            return getCaptureTime();
        }
        if (index == 7)
        {
            return getStartTimeCurrent();
        }
        if (index == 8)
        {
            return getPeriod();
        } 
        if (index == 9)
        {
            return getNumberOfPeriods();
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
            setCurrentAvarageValue(value);
        }
        else if (index == 3)
        {
            setLastAvarageValue(value);
        }
        else if (index == 4)
        {            
            //Set default values.
            if (value == null)
            {
                m_Scaler = m_Unit = 0;
            }
            else
            {
                if (Array.getLength(value) != 2)
                {
                    throw new IllegalArgumentException("setValue failed. Invalid scaler unit value.");
                }
                m_Scaler = ((Number)Array.get(value, 0)).intValue();
                m_Unit = (((Number)Array.get(value, 1)).intValue() & 0xFF);
            }
        }
        else if (index == 5)
        {
            if (value == null)
            {
                setStatus(null);
            }
            else
            {
                setStatus(value);
            }
        }
        else if (index == 6)
        {
            if (value == null)
            {
                setCaptureTime(new GXDateTime());                
            }
            else
            {
                if (value instanceof byte[])
                {
                    value = GXDLMSClient.changeType((byte[]) value, DataType.DATETIME);
                }
                setCaptureTime((GXDateTime) value);
            }
        }
        else if (index == 7)
        {
            if (value == null)
            {
                setStartTimeCurrent(new GXDateTime());                
            }
            else
            {
                if (value instanceof byte[])
                {
                    value = GXDLMSClient.changeType((byte[]) value, DataType.DATETIME);
                }
                setStartTimeCurrent((GXDateTime) value);
            }
        }
        else if (index == 8)
        {
            if (value == null)
            {
                setPeriod(BigInteger.valueOf(0));
            }
            else
            {                
                setPeriod(BigInteger.valueOf(((Number) value).longValue()));
            }
        }   
        else if (index == 9)
        {
            if (value == null)
            {
                setNumberOfPeriods(1);
            }
            else
            {
                setNumberOfPeriods(GXCommon.intValue(value));
            }
        }   
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}