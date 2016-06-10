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

import java.util.ArrayList;
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.GXDLMSScriptActionType;

public class GXDLMSScriptTable extends GXDLMSObject implements IGXDLMSBase {
    private List<GXDLMSScript> scripts;

    /**
     * Constructor.
     */
    public GXDLMSScriptTable() {
        this(null);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSScriptTable(final String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSScriptTable(final String ln, final int sn) {
        super(ObjectType.SCRIPT_TABLE, ln, sn);
        scripts = new ArrayList<GXDLMSScript>();
    }

    public final List<GXDLMSScript> getScripts() {
        return scripts;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getScripts() };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // Scripts
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 2;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ARRAY;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return getLogicalName();
        }
        if (e.getIndex() == 2) {
            int cnt = scripts.size();
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(cnt, data);
            for (GXDLMSScript it : scripts) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                // Count
                data.setUInt8(2);
                // Script_identifier:
                GXCommon.setData(data, DataType.UINT16, it.getId());
                data.setUInt8(DataType.ARRAY.getValue());
                // Count
                data.setUInt8(it.getActions().size());
                for (GXDLMSScriptAction a : it.getActions()) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    data.setUInt8(5);
                    // service_id
                    GXCommon.setData(data, DataType.ENUM,
                            new Integer(a.getType().ordinal() + 1));
                    // class_id
                    GXCommon.setData(data, DataType.UINT16,
                            new Integer(a.getObjectType().getValue()));
                    // logical_name
                    GXCommon.setData(data, DataType.OCTET_STRING,
                            a.getLogicalName());
                    // index
                    GXCommon.setData(data, DataType.INT8,
                            new Integer(a.getIndex()));
                    // parameter
                    GXCommon.setData(data, a.getParameterType(),
                            a.getParameter());
                }
            }
            return data.array();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;

    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            super.setValue(settings, e);
        } else if (e.getIndex() == 2) {
            scripts.clear();
            // Fix Xemex bug here.
            // Xemex meters do not return array as they shoul be according
            // standard.
            if (e.getValue() instanceof Object[]
                    && ((Object[]) e.getValue()).length != 0) {
                if (((Object[]) e.getValue())[0] instanceof Object[]) {
                    for (Object item : (Object[]) e.getValue()) {
                        GXDLMSScript script = new GXDLMSScript();
                        script.setId(
                                ((Number) ((Object[]) item)[0]).intValue());
                        scripts.add(script);
                        for (Object arr : (Object[]) ((Object[]) item)[1]) {
                            GXDLMSScriptAction it = new GXDLMSScriptAction();
                            int val = ((Number) ((Object[]) arr)[0]).intValue();
                            GXDLMSScriptActionType type =
                                    GXDLMSScriptActionType.NONE;
                            // Some Iskra meters return -1 here.
                            // It is not standard value.
                            if (val > 0) {
                                type = GXDLMSScriptActionType.values()[val];
                            }
                            it.setType(type);
                            ObjectType ot = ObjectType.forValue(
                                    ((Number) ((Object[]) arr)[1]).intValue());
                            it.setObjectType(ot);
                            String ln = GXDLMSObject.toLogicalName(
                                    (byte[]) ((Object[]) arr)[2]);
                            it.setLogicalName(ln);
                            it.setIndex(
                                    ((Number) ((Object[]) arr)[3]).intValue());
                            it.setParameter(((Object[]) arr)[4], DataType.NONE);
                            script.getActions().add(it);
                        }
                    }
                } else {
                    // Read Xemex meter here.
                    GXDLMSScript script = new GXDLMSScript();
                    script.setId(
                            ((Number) ((Object[]) e.getValue())[0]).intValue());
                    Object[] arr = (Object[]) ((Object[]) e.getValue())[1];
                    GXDLMSScriptAction it = new GXDLMSScriptAction();
                    GXDLMSScriptActionType type = GXDLMSScriptActionType
                            .values()[((Number) ((Object[]) arr)[0]).intValue()
                                    - 1];
                    it.setType(type);
                    ObjectType ot = ObjectType.forValue(
                            ((Number) ((Object[]) arr)[1]).intValue());
                    it.setObjectType(ot);
                    String ln = GXDLMSObject
                            .toLogicalName((byte[]) ((Object[]) arr)[2]);
                    it.setLogicalName(ln);
                    it.setIndex(((Number) ((Object[]) arr)[3]).intValue());
                    it.setParameter(((Object[]) arr)[4], DataType.NONE);
                    script.getActions().add(it);
                }
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    /*
     * Executes the script specified in parameter data.
     */
    public final byte[][] execute(final GXDLMSClient client, final Object data,
            final DataType type) {
        return client.method(this, 1, data, type);
    }
}