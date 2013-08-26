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

public class GXDLMSScriptAction 
{
    GXDLMSScriptActionType m_Type;
    ObjectType m_ObjectType;
    String m_LogicalName;
    int m_Index;
    Object m_Parameter;
    DataType m_ParameterType;
    
    /** 
     Defines which action to be applied to the referenced object.
    */
    public final GXDLMSScriptActionType getType()
    {
        return m_Type;
    }
    public final void setType(GXDLMSScriptActionType value)
    {
        m_Type = value;
    }     
    
    /** 
     Executed object type.
    */
    public final ObjectType getObjectType()
    {
        return m_ObjectType;
    }
    public final void setObjectType(ObjectType value)
    {
        m_ObjectType = value;
    }   
    
    /** 
     Logical name of executed object.
    */
    public final String getLogicalName()
    {
        return m_LogicalName;
    }
    public final void setLogicalName(String value)
    {
        m_LogicalName = value;
    }
    
    /** 
     defines which attribute of the selected object is affected; or 
     * which specific method is to be executed.
    */
    public final int getIndex()
    {
        return m_Index;
    }
    public final void setIndex(int value)
    {
        m_Index = value;
    }   
    
    /** 
     Parameter is service spesific.
    */
    public final Object getParameter()
    {
        return m_Parameter;
    }
    public final void setParameter(Object value, DataType type)
    {
        m_Parameter = value;
        m_ParameterType = type;
    }   
    
    /** 
     Return parameter type..
    */
    public final DataType getParameterType()
    {
        return m_ParameterType;
    }
}
