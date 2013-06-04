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

import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.*;
import gurux.dlms.manufacturersettings.GXAttributeCollection;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;

/** 
 GXDLMSObject provides an interface to DLMS registers. 
*/
public class GXDLMSObject implements IGXDLMSColumnObject
{
    private int privateVersion;
    private ObjectType privateObjectType = ObjectType.NONE;
    private GXAttributeCollection privateAttributes = null;
    private GXDLMSObjectCollection privateParent = null;
    private GXAttributeCollection privateMethodAttributes = null;
    int privateShortName;
    private int privateSelectedAttributeIndex;
    private ObjectType privateSourceObjectType = ObjectType.NONE;
    private String privateLogicalName;
    private String privateSourceLogicalName;
    private String privateDescription;
    private int privateSelectedDataIndex;

    /** 
     Constructor.
    */
    public GXDLMSObject()
    {
        this(ObjectType.NONE, null, 0);
    }

    /**  
     Constructor,
    */
    protected GXDLMSObject(gurux.dlms.enums.ObjectType objectType)
    {
        this(objectType, null, 0);
    }

    /**  
     Constructor,
    */
    protected GXDLMSObject(gurux.dlms.enums.ObjectType objectType, String ln, int sn)
    {
        privateAttributes = new GXAttributeCollection();
        privateMethodAttributes = new GXAttributeCollection();        
        setObjectType(objectType);
        this.setShortName(sn);
        if (ln != null)
        {
            String[] items = ln.split("[.]", -1);
            if (items.length != 6)
            {
                throw new GXDLMSException("Invalid Logical Name.");
            }
        }
        this.setLogicalName(ln);
    }
    
    public final gurux.dlms.objects.GXDLMSObjectCollection getParent()
    {
        return privateParent;
    }
    
    final void setParent(gurux.dlms.objects.GXDLMSObjectCollection value)
    {
        privateParent = value;
    }

    /** 
     Logical or Short Name of DLMS object.
     @return Logical or Short Name of DLMS object.
    */
    @Override
    public String toString()
    {
        if (getShortName() != 0)
        {
            return (new Integer(getShortName())).toString();
        }
        return getLogicalName();
    }

    /** 
     Converts Logical Name to string.
    */
    public static String toLogicalName(byte[] buff)
    {
        if (buff != null && buff.length == 6)
        {
            return (buff[0] & 0xFF) + "." + (buff[1] & 0xFF) + "." + (buff[2] & 0xFF) + "." + (buff[3] & 0xFF) + "." + (buff[4] & 0xFF) + "." + (buff[5] & 0xFF);
        }
        return "";
    }

    /** 
     Interface type of the DLMS object.
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
     DLMS version number.
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
     The base name of the object, if using SN.
     When using SN referencing, retrieves the base name of the DLMS object.
     When using LN referencing, the value is 0.	 
    */
    public final int getShortName()
    {
        return privateShortName;
    }
    public final void setShortName(int value)
    {
        privateShortName = value;
    }

    /** 
     Logical or Short Name of DLMS object.	 
     @return Logical or Short Name of DLMS object
    */
    public final Object getName()
    {
        if (getShortName() != 0)
        {
            return getShortName();
        }
        return getLogicalName();
    }

    /** 
     Index of the object, if it is in a profile generic table. 
     Retrieves the index of the DLMS object, if the object is a part of a  
     profile generic table. If the object is not a part of a profile generic 
     table, the value is 0.
    */
    public final int getSelectedAttributeIndex()
    {
        return privateSelectedAttributeIndex;
    }
    public final void setSelectedAttributeIndex(int value)
    {
        privateSelectedAttributeIndex = value;
    }

    /** 
     Data index of DLMS object, if it is in a profile generic table. 
    */
    @Override
    public final int getSelectedDataIndex()
    {
        return privateSelectedDataIndex;
    }
    @Override
    public final void setSelectedDataIndex(int value)
    {
        privateSelectedDataIndex = value;
    }

    /** 
     Logical Name of parent object.
    */
    @Override
    public final String getSourceLogicalName()
    {
        return privateSourceLogicalName;
    }
    @Override
    public final void setSourceLogicalName(String value)
    {
        privateSourceLogicalName = value;
    }

    /** 
     object type of parent object.
    */
    public final ObjectType getSourceObjectType()
    {
        return privateSourceObjectType;
    }
    public final void setSourceObjectType(ObjectType value)
    {
        privateSourceObjectType = value;
    }

    /** 
     Logical Name of DLMS object.
    */
    public String getLogicalName()
    {
        return privateLogicalName;
    }
    public void setLogicalName(String value)
    {
        privateLogicalName = value;
    }

    /** 
     Description of DLMS object.
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
     object attribute collection.
    */
    public final GXAttributeCollection getAttributes()
    {
        return privateAttributes;
    }

    /** 
     object attribute collection.
    */
    public final GXAttributeCollection getMethodAttributes()
    {
        return privateMethodAttributes;
    }

    /** 
     Returns is attribute read only.
     -
     @param index Attribute index.
     @return Is attribute read only.
    */
    public final AccessMode getAccess(int index)
    {   
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            return AccessMode.READ_WRITE;
        }
        return att.getAccess();
    }

    /** 
     Set attribute access.
    */
    public final void setAccess(int index, AccessMode access)
    {
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            privateAttributes.add(att);
        }
        att.setAccess(access);
    }

    /*
    * Returns amount of methods.
    */
    public int getMethodCount()
    {
        assert(false);
        throw new UnsupportedOperationException("getMethodCount");
    }
    
    /** 
     Returns is Method attribute read only.
     -
     @param index Method Attribute index.
     @return Is attribute read only.
    */
    public final MethodAccessMode getMethodAccess(int index)
    {
        GXDLMSAttributeSettings att = getMethodAttributes().find(index);
        if (att != null)
        {
            return att.getMethodAccess();
        }
        return MethodAccessMode.NO_ACCESS;
    }

    /** 
     Set Method attribute access.

     @param index
     @param access
    */
    public final void setMethodAccess(int index, MethodAccessMode access)
    {
        GXDLMSAttributeSettings att = getMethodAttributes().find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            getMethodAttributes().add(att);
        }
        att.setMethodAccess(access);
    }

    /** 
     Override this function to update default values for OBIS attributes.
     This functionality is added because several manufacturers has different device and UI values.
    */
    public void updateDefaultValueItems()
    {
    }

    public DataType getDataType(int index)
    {
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            return DataType.NONE;
        }
        return att.getType();
    }

    public DataType getUIDataType(int index)
    {
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            return DataType.NONE;
        }
        return att.getUIType();
    }

     /*
    * Returns amount of attributes.
    */
    public int getAttributeCount()
    {
        assert(false);
        throw new UnsupportedOperationException("getAttributeCount");
    }
    
    /** 
     Returns object values as an array.
    */
    public Object[] getValues()
    {
        assert(false);
        throw new UnsupportedOperationException("getValues");
    }
    
    /*
     * Returns value of given attribute.
     */    
    public Object getValue(int index, DataType[] type, byte[] parameters)
    {
        assert(false);
        throw new UnsupportedOperationException("getValue");
    }
    
    /*
     * Set value of given attribute.
     */
    public void setValue(int index, Object value)
    {
        assert(false);
        throw new UnsupportedOperationException("setValue");
    }
    
    /*
    * Invokes method.
    * 
     @param index Method index.
    */
    public void invoke(int index, Object parameters)
    {
        assert(false);
        throw new UnsupportedOperationException("invoke");
    }

    public final void setDataType(int index, DataType type)
    {
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            privateAttributes.add(att);
        }
        att.setType(type);
    }

    public final void setUIDataType(int index, DataType type)
    {
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            privateAttributes.add(att);
        }
        att.setUIType(type);
    }

    public final void setStatic(int index, boolean isStatic)
    {
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            privateAttributes.add(att);
        }
        att.setStatic(isStatic);
    }

    public final boolean getStatic(int index)
    {
        GXDLMSAttributeSettings att = privateAttributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            privateAttributes.add(att);
        }
        return att.getStatic();
    }
}