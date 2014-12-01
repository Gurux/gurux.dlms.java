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
import java.text.NumberFormat;

public class GXDLMSExtendedRegister extends GXDLMSRegister
{
    private GXDateTime CaptureTime = new GXDateTime();
    private Object Status;

    /**  
     Constructor.
    */
    public GXDLMSExtendedRegister()
    {
        super(ObjectType.EXTENDED_REGISTER, null, 0);        
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSExtendedRegister(String ln)
    {
        super(ObjectType.EXTENDED_REGISTER, ln, 0);
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSExtendedRegister(String ln, int sn)
    {
        super(ObjectType.EXTENDED_REGISTER, ln, sn);
    }
   
    /** 
     Status of COSEM Extended Register object.
    */
    public final Object getStatus()
    {
        return Status;
    }
    public final void setStatus(Object value)
    {
        Status = value;
    }
    
    /** 
     Capture time of COSEM Extended Register object.
    */
    public final GXDateTime getCaptureTime()
    {
        return CaptureTime;
    }
    public final void setCaptureTime(GXDateTime value)
    {
        CaptureTime = value;
    }
    
    @Override   
    public DataType getUIDataType(int index)
    {
        if (index == 5)
        {
            return DataType.DATETIME;
        }        
        return super.getUIDataType(index);
    }
    
    @Override
    public Object[] getValues()
    {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        String str = "Scaler: " + formatter.format(getScaler());
        str += " Unit: ";
        str += getUnit().toString();
        return new Object[] {getLogicalName(), getValue(), str, getStatus(), getCaptureTime()};
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
            return DataType.ARRAY;
        }
        if (index == 4)
        {
            return super.getDataType(index);
        }
        if (index == 5)
        {
            return DataType.DATETIME;                
        }
        throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
    }
     
    /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, int selector, Object parameters)
    {        
        if (index == 4)
        {
            return getStatus();
        }
        if (index == 5)
        {
            return getCaptureTime();
        }
        return super.getValue(index, selector, parameters);
    }
    
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value)
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
            super.setValue(index, value);
        }
    }
}