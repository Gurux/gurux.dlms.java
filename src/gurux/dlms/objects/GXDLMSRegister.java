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
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Unit;
import gurux.dlms.enums.DataType;
import java.lang.reflect.Array;

public class GXDLMSRegister extends GXDLMSObject implements IGXDLMSBase
{
    protected int[] ScalerUnit;
    private Object privateValue;

    /**  
     Constructor.
    */
    public GXDLMSRegister()
    {
        super(ObjectType.REGISTER);
        setScaler(1);
        setUnit(Unit.NO_UNIT);
    }

public GXDLMSRegister(ObjectType type, String ln, int sn)
{
    super(type, ln, sn);
    setScaler(1);
    setUnit(Unit.NO_UNIT);
}

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSRegister(String ln)
    {
        this(ObjectType.REGISTER, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSRegister(String ln, int sn)
    {
        this(ObjectType.REGISTER, ln, 0);
    }

    /** 
     Scaler of COSEM Register object.
    */
    public final double getScaler()
    {
        if (ScalerUnit == null)
        {
            return 1;
        }
        return Math.pow(10, (int)(ScalerUnit[0]));
    }
    public final void setScaler(double value)
    {
        if (ScalerUnit == null)
        {
            ScalerUnit = new int[2];
            ScalerUnit[1] = 0;
        }
        ScalerUnit[0] = (int)Math.log10(value);
    }

    /** 
     Unit of COSEM Register object.
    */
    public final Unit getUnit()
    {
        if (ScalerUnit == null)
        {
            return Unit.NO_UNIT;
        }
        return Unit.forValue((int)(ScalerUnit[1]));
    }
    public final void setUnit(Unit value)
    {
        if (ScalerUnit == null)
        {
            ScalerUnit = new int[2];
            ScalerUnit[0] = 0;
        }
        ScalerUnit[1] = value.getValue();
    }

    /** 
     Value of COSEM Register object.
     Register value is not serialized because XML serializer can't handle all cases.
    */
    public final Object getValue()
    {
        return privateValue;
    }
    public final void setValue(Object value)
    {
        privateValue = value;
    }   
    
    /*
     * Reset value.
     */
    public byte[][] reset(GXDLMSClient client)
    {
        byte[] ret = client.method(getName(), getObjectType(), 1, (int) 0);
        return new byte[][]{ret};    
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getValue(), ScalerUnit};
    }
   
    @Override
    public void invoke(int index, Object parameters)
    {
        // Resets the value to the default value. 
        // The default value is an instance specific constant.
        if (index == 1)
        {
            setValue(null);
        }
        else
        {
            throw new IllegalArgumentException("Invoke failed. Invalid attribute index.");
        }
    }

    /*
     * Returns amount of attributes.
     */    
    @Override
    public int getAttributeCount()
    {
        return 3;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 1;
    }
    
    /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, DataType[] type, byte[] parameters)
    {
        if (index == 1)
        {
            type[0] = DataType.OCTET_STRING;
            return getLogicalName();
        }
        if (index == 2)
        {
            return getValue();
        }
        if (index == 3)
        {
            type[0] = DataType.STRUCTURE;
            return ScalerUnit;
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
            setLogicalName(GXDLMSObject.toLogicalName((byte[]) value));            
        }
        else if (index == 2)
        {
            setValue(value);
        }
        else if (index == 3)
        {
            if (ScalerUnit == null)
            {
                ScalerUnit = new int[2];                
            }
            //Set default values.
            if (value == null)
            {
                ScalerUnit[0] = ScalerUnit[1] = 0;
            }
            else
            {
                if (Array.getLength(value) != 2)
                {
                    throw new IllegalArgumentException("setValue failed. Invalid scaler unit value.");
                }
                ScalerUnit[0] = ((Number)Array.get(value, 0)).intValue();
                ScalerUnit[1] = ((Number)Array.get(value, 1)).intValue();
            }
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }       
}