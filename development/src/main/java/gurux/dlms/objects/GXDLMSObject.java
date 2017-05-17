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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.manufacturersettings.GXAttributeCollection;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;

/**
 * GXDLMSObject provides an interface to DLMS registers.
 */
public class GXDLMSObject {
    private HashMap<Integer, java.util.Date> readTimes =
            new HashMap<Integer, java.util.Date>();
    private int version;
    private ObjectType objectType = ObjectType.NONE;
    private GXAttributeCollection attributes = null;
    private GXAttributeCollection methodAttributes = null;
    private int shortName;
    private String logicalName;
    private String description;

    /**
     * Constructor.
     */
    public GXDLMSObject() {
        this(ObjectType.NONE, null, 0);
    }

    /*
     * Constructor,
     */
    protected GXDLMSObject(final ObjectType type) {
        this(type, null, 0);
    }

    /*
     * Constructor,
     */
    protected GXDLMSObject(final ObjectType type, final String ln,
            final int sn) {
        attributes = new GXAttributeCollection();
        methodAttributes = new GXAttributeCollection();
        setObjectType(type);
        this.setShortName(sn);
        if (ln != null) {
            List<String> items = GXCommon.split(ln, '.');
            if (items.size() != 6) {
                throw new GXDLMSException("Invalid Logical Name.");
            }
        }
        logicalName = ln;
    }

    protected static byte[] toByteArray(final List<Byte> list) {
        byte[] ret = new byte[list.size()];
        int i = -1;
        for (Byte e : list) {
            ret[++i] = e.byteValue();
        }
        return ret;
    }

    /*
     * Is attribute read. This can be used with static attributes to make meter
     * reading faster.
     */
    // CHECKSTYLE:OFF
    protected boolean isRead(final int index) {
        if (!canRead(index)) {
            return true;
        }
        return !getLastReadTime(index).equals(new java.util.Date(0));
    }
    // CHECKSTYLE:ON

    protected final boolean canRead(final int index) {
        return getAccess(index) != AccessMode.NO_ACCESS;
    }

    /**
     * Returns time when attribute was last time read. -
     * 
     * @param attributeIndex
     *            Attribute index.
     * @return Is attribute read only.
     */
    protected final java.util.Date getLastReadTime(final int attributeIndex) {
        for (Map.Entry<Integer, java.util.Date> it : readTimes.entrySet()) {
            if (it.getKey() == attributeIndex) {
                return it.getValue();
            }
        }
        return new java.util.Date(0);
    }

    /*
     * Set time when attribute was last time read.
     * @param attributeIndex Attribute index.
     * @param tm Read time.
     */
    protected final void setLastReadTime(final int attributeIndex,
            final java.util.Date tm) {
        readTimes.put(attributeIndex, tm);
    }

    /**
     * Logical or Short Name of DLMS object.
     * 
     * @return Logical or Short Name of DLMS object.
     */
    @Override
    public final String toString() {
        String str;
        if (getShortName() != 0) {
            str = String.valueOf(getShortName());
        } else {
            str = getLogicalName();
        }
        if (description != null) {
            str += " " + description;
        }
        return str;
    }

    /**
     * @return Interface type of the COSEM object.
     */
    public final ObjectType getObjectType() {
        return objectType;
    }

    /**
     * @param value
     *            Interface type of the COSEM object.
     */
    public final void setObjectType(final ObjectType value) {
        objectType = value;
    }

    /**
     * @return DLMS version number.
     */
    public final int getVersion() {
        return version;
    }

    /**
     * @param value
     *            DLMS version number.
     */
    public final void setVersion(final int value) {
        version = value;
    }

    /**
     * The base name of the object, if using SN. When using SN referencing,
     * retrieves the base name of the DLMS object. When using LN referencing,
     * the value is 0.
     * 
     * @return The base name of the object.
     */
    public final int getShortName() {
        return shortName;
    }

    /**
     * The base name of the object, if using SN. When using SN referencing,
     * retrieves the base name of the DLMS object. When using LN referencing,
     * the value is 0.
     * 
     * @param value
     *            The base name of the object.
     */
    public final void setShortName(final int value) {
        shortName = value;
    }

    /**
     * Logical or Short Name of DLMS object.
     * 
     * @return Logical or Short Name of DLMS object
     */
    public final Object getName() {
        if (getShortName() != 0) {
            return getShortName();
        }
        return getLogicalName();
    }

    /**
     * @return Logical Name of COSEM object.
     */
    public final String getLogicalName() {
        return logicalName;
    }

    /**
     * @param value
     *            Logical Name of COSEM object.
     */
    public final void setLogicalName(final String value) {
        logicalName = value;
    }

    /**
     * @return Description of COSEM object.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param value
     *            Description of COSEM object.
     */
    public final void setDescription(final String value) {
        description = value;
    }

    /**
     * @return Object attribute collection.
     */
    public final GXAttributeCollection getAttributes() {
        return attributes;
    }

    /**
     * @return Object method attribute collection.
     */
    public final GXAttributeCollection getMethodAttributes() {
        return methodAttributes;
    }

    /**
     * Returns is attribute read only. -
     * 
     * @param index
     *            Attribute index.
     * @return Is attribute read only.
     */
    public final AccessMode getAccess(final int index) {
        if (index == 1) {
            return AccessMode.READ;
        }
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            return AccessMode.READ_WRITE;
        }
        return att.getAccess();
    }

    /**
     * Set attribute access.
     * 
     * @param index
     *            Attribute index.
     * @param access
     *            Attribute access.
     */
    public final void setAccess(final int index, final AccessMode access) {
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            att = new GXDLMSAttributeSettings(index);
            attributes.add(att);
        }
        att.setAccess(access);
    }

    /*
     * Returns amount of methods.
     */
    // CHECKSTYLE:OFF
    public int getMethodCount() {
        throw new UnsupportedOperationException("getMethodCount");
    }
    // CHECKSTYLE:ON

    /**
     * Returns is Method attribute read only. -
     * 
     * @param index
     *            Method Attribute index.
     * @return Is attribute read only.
     */
    public final MethodAccessMode getMethodAccess(final int index) {
        GXDLMSAttributeSettings att = getMethodAttributes().find(index);
        if (att != null) {
            return att.getMethodAccess();
        }
        return MethodAccessMode.ACCESS;
    }

    /**
     * Set Method attribute access.
     * 
     * @param index
     *            Method index.
     * @param access
     *            Method access mode.
     */
    public final void setMethodAccess(final int index,
            final MethodAccessMode access) {
        GXDLMSAttributeSettings att = getMethodAttributes().find(index);
        if (att == null) {
            att = new GXDLMSAttributeSettings(index);
            getMethodAttributes().add(att);
        }
        att.setMethodAccess(access);
    }

    // CHECKSTYLE:OFF
    public DataType getDataType(final int index) {
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            return DataType.NONE;
        }
        return att.getType();
    }
    // CHECKSTYLE:ON

    // CHECKSTYLE:OFF
    public DataType getUIDataType(final int index) {
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            return DataType.NONE;
        }
        return att.getUIType();
    }
    // CHECKSTYLE:ON

    /**
     * @return Amount of attributes.
     */
    // CHECKSTYLE:OFF
    public int getAttributeCount() {
        throw new UnsupportedOperationException("getAttributeCount");
    }
    // CHECKSTYLE:ON

    /**
     * @return Object values as an array.
     */
    public Object[] getValues() {
        throw new UnsupportedOperationException("getValues");
    }

    /*
     * Get value.
     * @param settings DLMS settings.
     * @param e Value event parameters.
     * @return Value of given attribute.
     */
    public Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        throw new UnsupportedOperationException("getValue");
    }

    /**
     * Set value of given attribute.
     * 
     * @param settings
     *            DLMS settings.
     * @param e
     *            Value event parameters.
     */
    public void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        throw new UnsupportedOperationException("setValue");
    }

    /*
     * Server calls this invokes method.
     * @param settings DLMS settings.
     * @param e Value event parameters.
     */
    // CHECKSTYLE:OFF
    public byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }
    // CHECKSTYLE:ON

    public final void setDataType(final int index, final DataType type) {
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            att = new GXDLMSAttributeSettings(index);
            attributes.add(att);
        }
        att.setType(type);
    }

    public final void setUIDataType(final int index, final DataType type) {
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            att = new GXDLMSAttributeSettings(index);
            attributes.add(att);
        }
        att.setUIType(type);
    }

    public final void setStatic(final int index, final boolean isStatic) {
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            att = new GXDLMSAttributeSettings(index);
            attributes.add(att);
        }
        att.setStatic(isStatic);
    }

    public final boolean getStatic(final int index) {
        GXDLMSAttributeSettings att = attributes.find(index);
        if (att == null) {
            att = new GXDLMSAttributeSettings(index);
            attributes.add(att);
        }
        return att.getStatic();
    }

    /**
     * Server calls this when it's started.<br>
     * This is reserved for internal use. Do not use.
     * 
     * @param server
     *            Server.
     */
    public void start(final GXDLMSServerBase server) {

    }

    /**
     * Server calls this when it's closed.<br>
     * This is reserved for internal use. Do not use.
     * 
     * @param server
     *            Server.
     * @throws Exception
     *             Occurred exception.
     */
    public void stop(final GXDLMSServerBase server) throws Exception {

    }
}