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

public class GXDLMSExtendedRegister extends GXDLMSRegister
{
    private GXDateTime privateCaptureTime = new GXDateTime();
    private Object privateStatus;

    /**  
     Constructor.
    */
    public GXDLMSExtendedRegister()
    {
        super(ObjectType.EXTENDED_REGISTER, null, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSExtendedRegister(String ln)
    {
        super(ObjectType.EXTENDED_REGISTER, ln, 0);
    }

    /**  
     Constructor.
     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSExtendedRegister(String ln, int sn)
    {
        super(ObjectType.EXTENDED_REGISTER, ln, sn);
    }
   
    /** 
     Scaler of COSEM Register object.
    */
    public final Object getStatus()
    {
        return privateStatus;
    }
    public final void setStatus(Object value)
    {
        privateStatus = value;
    }
    
    @Override
    public DataType getDataType(int index)
    {
        if (index == 5)
        {
            return DataType.DATETIME;
        }
        return super.getDataType(index);
    }

    /** 
     Scaler of COSEM Register object.
    */
    public final GXDateTime getCaptureTime()
    {
        return privateCaptureTime;
    }
    public final void setCaptureTime(GXDateTime value)
    {
        privateCaptureTime = value;
    }
    
    @Override
    public Object[] getValues()
    {
        String str = String.format("Scaler: %1$,.2f Unit: ", getScaler());
        str += getUnit().toString();
        return new Object[] {getLogicalName(), getValue(), str, getStatus(), getCaptureTime()};
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
        //ScalerUnit
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //Value
        if (canRead(2))
        {
            attributes.add(2);
        }        
        //Status
        if (canRead(4))
        {
            attributes.add(4);
        }
        //CaptureTime
        if (canRead(5))
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
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, DataType[] type, byte[] parameters, boolean raw)
    {        
        if (index == 4)
        {
            return getStatus();
        }
        if (index == 5)
        {
            type[0] = DataType.DATETIME;
            return getCaptureTime();
        }
        return super.getValue(index, type, parameters, raw);
    }
    
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value, boolean raw)
    {
        if (index == 4)
        {
            setStatus(value);
        }
        else if (index == 5)
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
        else
        {
            super.setValue(index, value, raw);
        }
    }
}