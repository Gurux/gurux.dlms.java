//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: http://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------
package gurux.dlms.objects;

import gurux.dlms.enums.ObjectType;

public class GXDLMSMonitoredValue
{
    private ObjectType ObjectType;
    private String LogicalName;
    private int AttributeIndex;
  
    public final void update(GXDLMSObject value, int attributeIndex)
    {
        ObjectType = value.getObjectType();
        LogicalName = value.getLogicalName();
        AttributeIndex = attributeIndex;
    }
    
    public final ObjectType getObjectType()
    {
        return ObjectType;
    }
    public final void setObjectType(ObjectType value)
    {
        ObjectType = value;
    }

    public final String getLogicalName()
    {
        return LogicalName;
    }
    public final void setLogicalName(String value)
    {
        LogicalName = value;
    }

    public final int getAttributeIndex()
    {
        return AttributeIndex;
    }
    public final void setAttributeIndex(int value)
    {
        AttributeIndex = value;
    }
    
    @Override
    public String toString()
    {
        return String.valueOf(ObjectType) + " " + LogicalName + " " + String.valueOf(AttributeIndex);
    }
}