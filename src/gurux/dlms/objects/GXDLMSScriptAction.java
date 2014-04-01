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

import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSScriptAction 
{
    GXDLMSScriptActionType Type;
    ObjectType ObjectType;
    String LogicalName;
    int Index;
    Object Parameter;
    DataType ParameterType;
    
    /** 
     Defines which action to be applied to the referenced object.
    */
    public final GXDLMSScriptActionType getType()
    {
        return Type;
    }
    public final void setType(GXDLMSScriptActionType value)
    {
        Type = value;
    }     
    
    /** 
     Executed object type.
    */
    public final ObjectType getObjectType()
    {
        return ObjectType;
    }
    public final void setObjectType(ObjectType value)
    {
        ObjectType = value;
    }   
    
    /** 
     Logical name of executed object.
    */
    public final String getLogicalName()
    {
        return LogicalName;
    }
    public final void setLogicalName(String value)
    {
        LogicalName = value;
    }
    
    /** 
     defines which attribute of the selected object is affected; or 
     * which specific method is to be executed.
    */
    public final int getIndex()
    {
        return Index;
    }
    public final void setIndex(int value)
    {
        Index = value;
    }   
    
    /** 
     Parameter is service spesific.
    */
    public final Object getParameter()
    {
        return Parameter;
    }
    public final void setParameter(Object value, DataType type)
    {
        Parameter = value;
        ParameterType = type;
    }   
    
    /** 
     Return parameter type..
    */
    public final DataType getParameterType()
    {
        return ParameterType;
    }
    
    @Override
    public String toString()
    {
        String tmp;
        if (Parameter instanceof byte[])
        {
            tmp = GXCommon.toHex((byte[]) Parameter);
        }
        else
        {
            tmp = String.valueOf(Parameter);
        }
        return Type.toString() + " " + String.valueOf(ObjectType)  + " " + 
                LogicalName  + " " +  String.valueOf(Index)  + " " + tmp;
    }

}
