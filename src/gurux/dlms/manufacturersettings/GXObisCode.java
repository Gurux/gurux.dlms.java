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

package gurux.dlms.manufacturersettings;

import gurux.dlms.enums.ObjectType;

public class GXObisCode
{
    private int privateVersion;
    private int privateAttributeIndex;
    private String privateLogicalName;
    private String privateDescription;
    private ObjectType privateObjectType = ObjectType.NONE;
    private GXAttributeCollection privateAttributes;

    /** 
     Constructor.
    */

    @SuppressWarnings("LeakingThisInConstructor")
    public GXObisCode()
    {
        privateAttributes = new GXAttributeCollection();
        privateAttributes.setParent(this);
    }

    @Override
    public String toString()
    {
        return getObjectType().toString();
    }

    /** 
     Constructor.
    */
    public GXObisCode(String ln, ObjectType objectType, int index)
    {
        setLogicalName(ln);
        setObjectType(objectType);
        setAttributeIndex(index);
    }

    /** 
     Constructor.
    */
    public GXObisCode(String ln, ObjectType objectType, String description)
    {
        setLogicalName(ln);
        setObjectType(objectType);
        setDescription(description);
    }

    /** 
     Attribute index.
    */
    public final int getAttributeIndex()
    {
        return privateAttributeIndex;
    }
    public final void setAttributeIndex(int value)
    {
        privateAttributeIndex = value;
    }

    /** 
     Logical name of the OBIS item.
    */
    public final String getLogicalName()
    {
        return privateLogicalName;
    }
    public final void setLogicalName(String value)
    {
        privateLogicalName = value;
    }

    /** 
     Description of the OBIS item.
    */
    public final String getDescription()
    {
        return privateDescription;
    }
    public final void setDescription(String value)
    {
        privateDescription = value;
    }

    /** 
     object type.
    */
    public final ObjectType getObjectType()
    {
        return privateObjectType;
    }
    public final void setObjectType(ObjectType value)
    {
        privateObjectType = value;
    }

    /** 
     Interface type.
    */
    public final int getVersion()
    {
        return privateVersion;
    }
    
    public final void setVersion(int value)
    {
        privateVersion = value;
    }

    /** 
     object attribute collection.
    */
    public final GXAttributeCollection getAttributes()
    {
        return privateAttributes;
    }
    public final void setAttributes(GXAttributeCollection value)
    {
        privateAttributes = value;
    }

    public static java.lang.Class getDataType(gurux.dlms.enums.DataType type)
    {
        switch (type)
        {
        case ARRAY:
            return byte[].class;
        case BCD:
        case BITSTRING:
            return String.class;
        case BOOLEAN:
            return Boolean.class;
        case DATE:
            return java.util.Date.class;
        case DATETIME:
            return java.util.Date.class;
        case FLOAT32:
            return Float.class;
        case FLOAT64:
            return Double.class;
        case INT16:
            return Short.class;
        case INT32:
            return Integer.class;
        case INT64:
            return Long.class;
        case INT8:
            return Byte.class;
        case NONE:
            return null;
        case OCTET_STRING:
            return String.class;
        case STRING:
            return String.class;
        case TIME:
            return java.util.Date.class;
        case UINT16:
            return Integer.class;
        case UINT32:
            return Long.class;
        case UINT64:
            return Long.class;
        case UINT8:
            return Short.class;
        /*
        case COMPACTARRAY:
        case ENUM:
        case STRUCTURE:
        */
        default:
            throw new RuntimeException("Invalid DLMS data type.");
        }
    }
}