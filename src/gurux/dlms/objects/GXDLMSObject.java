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
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/** 
 GXDLMSObject provides an interface to DLMS registers. 
*/
public class GXDLMSObject
{
    Dictionary<Integer, java.util.Date> ReadTimes = new Hashtable<Integer, java.util.Date>();
    private int Version;
    private ObjectType m_ObjectType = ObjectType.NONE;
    private GXAttributeCollection Attributes = null;
    private GXDLMSObjectCollection Parent = null;
    private GXAttributeCollection MethodAttributes = null;
    int ShortName;
    protected String LogicalName;
    private String Description;

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
        Attributes = new GXAttributeCollection();
        MethodAttributes = new GXAttributeCollection();        
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
        LogicalName = ln;
    }
    
    protected static int[] toIntArray(List<Integer> list)  
    {
        int[] ret = new int[list.size()];
        int i = -1;
        for (Integer e : list)  
        {
            ret[++i] = e.intValue();
        }
        return ret;
    }
    
    protected static long[] toLongArray(List<Long> list)  
    {
        long[] ret = new long[list.size()];
        int i = -1;
        for (Long e : list)  
        {
            ret[++i] = e.longValue();
        }
        return ret;
    }
    
    /*
     * Is attribute read. This can be used with static attributes to make 
     * meter reading faster.
     */    
    protected boolean isRead(int index)
    {            
        if (!canRead(index))
        {
            return true;
        }        
        return !getLastReadTime(index).equals(new java.util.Date(0));
    }        
    protected boolean canRead(int index)
    {
        return getAccess(index) != AccessMode.NO_ACCESS;
    }
    
    /** 
    Returns time when attribute was last time read.
    -
    @param attributeIndex Attribute index.
    @return Is attribute read only.
*/
    protected final java.util.Date getLastReadTime(int attributeIndex)
    {
        Enumeration<Integer> key = ReadTimes.keys();
        int value;
        while(key.hasMoreElements())
        {            
            if ((value = key.nextElement()) == attributeIndex)
            {
                return ReadTimes.get(value);
            }
        }
        return new java.util.Date(0);        
    }

    /** 
     Set time when attribute was last time read.
    */
    protected final void setLastReadTime(int attributeIndex, java.util.Date tm)
    {
        ReadTimes.put(attributeIndex, tm);
    }

    public final gurux.dlms.objects.GXDLMSObjectCollection getParent()
    {
        return Parent;
    }
    
    final void setParent(gurux.dlms.objects.GXDLMSObjectCollection value)
    {
        Parent = value;
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
        return m_ObjectType;
    }
    public final void setObjectType(ObjectType value)
    {
        m_ObjectType = value;
    }

    /** 
     DLMS version number.
    */
    public final int getVersion()
    {
        return Version;
    }
    public final void setVersion(int value)
    {
        Version = value;
    }

    /** 
     The base name of the object, if using SN.
     When using SN referencing, retrieves the base name of the DLMS object.
     When using LN referencing, the value is 0.	 
    */
    public final int getShortName()
    {
        return ShortName;
    }
    public final void setShortName(int value)
    {
        ShortName = value;
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
     Logical Name of DLMS object.
    */
    public String getLogicalName()
    {
        return LogicalName;
    }
    public void setLogicalName(String value)
    {
        LogicalName = value;
    }

    /** 
     Description of DLMS object.
    */
    public final String getDescription()
    {
        return Description;
    }

    public final void setDescription(String value)
    {
        Description = value;
    }

    /** 
     object attribute collection.
    */
    public final GXAttributeCollection getAttributes()
    {
        return Attributes;
    }

    /** 
     object attribute collection.
    */
    public final GXAttributeCollection getMethodAttributes()
    {
        return MethodAttributes;
    }

    /** 
     Returns is attribute read only.
     -
     @param index Attribute index.
     @return Is attribute read only.
    */
    public final AccessMode getAccess(int index)
    {   
        GXDLMSAttributeSettings att = Attributes.find(index);
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
        GXDLMSAttributeSettings att = Attributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            Attributes.add(att);
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
        GXDLMSAttributeSettings att = Attributes.find(index);
        if (att == null)
        {
            return DataType.NONE;
        }
        return att.getType();
    }

    public DataType getUIDataType(int index)
    {
        GXDLMSAttributeSettings att = Attributes.find(index);
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
    public Object getValue(int index, DataType[] type, byte[] parameters, boolean raw)
    {
        assert(false);
        throw new UnsupportedOperationException("getValue");
    }
    
    /*
     * Set value of given attribute.
     */
    public void setValue(int index, Object value, boolean raw)
    {
        assert(false);
        throw new UnsupportedOperationException("setValue");
    }
    
    /*
    * Invokes method.
    * 
     @param index Method index.
    */
    public byte[] invoke(Object sender, int index, Object parameters)
    {
        assert(false);
        throw new UnsupportedOperationException("invoke");
    }

    public final void setDataType(int index, DataType type)
    {
        GXDLMSAttributeSettings att = Attributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            Attributes.add(att);
        }
        att.setType(type);
    }

    public final void setUIDataType(int index, DataType type)
    {
        GXDLMSAttributeSettings att = Attributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            Attributes.add(att);
        }
        att.setUIType(type);
    }

    public final void setStatic(int index, boolean isStatic)
    {
        GXDLMSAttributeSettings att = Attributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            Attributes.add(att);
        }
        att.setStatic(isStatic);
    }

    public final boolean getStatic(int index)
    {
        GXDLMSAttributeSettings att = Attributes.find(index);
        if (att == null)
        {
            att = new GXDLMSAttributeSettings(index);
            Attributes.add(att);
        }
        return att.getStatic();
    }
}