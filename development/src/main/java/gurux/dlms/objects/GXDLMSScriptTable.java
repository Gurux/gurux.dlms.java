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

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.ScriptActionType;

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
            return GXCommon.logicalNameToBytes(getLogicalName());
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
                            new Integer(a.getType().ordinal()));
                    if (a.getTarget() == null) {
                        // class_id
                        GXCommon.setData(data, DataType.UINT16,
                                new Integer(a.getObjectType().getValue()));
                        // logical_name
                        GXCommon.setData(data, DataType.OCTET_STRING, GXCommon
                                .logicalNameToBytes(a.getLogicalName()));
                    } else {
                        // class_id
                        GXCommon.setData(data, DataType.UINT16, new Integer(
                                a.getTarget().getObjectType().getValue()));
                        // logical_name
                        GXCommon.setData(data, DataType.OCTET_STRING,
                                GXCommon.logicalNameToBytes(
                                        a.getTarget().getLogicalName()));
                    }
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
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
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
                            ScriptActionType type = ScriptActionType.NONE;
                            // Some Iskra meters return -1 here.
                            // It is not standard value.
                            if (val > 0) {
                                type = ScriptActionType.values()[val];
                            }
                            it.setType(type);
                            ObjectType ot = ObjectType.forValue(
                                    ((Number) ((Object[]) arr)[1]).intValue());
                            String ln = GXCommon.toLogicalName(
                                    (byte[]) ((Object[]) arr)[2]);
                            GXDLMSObject t =
                                    settings.getObjects().findByLN(ot, ln);
                            if (t == null) {
                                t = GXDLMSClient.createObject(ot);
                                t.setLogicalName(ln);
                            }
                            it.setTarget(t);
                            it.setIndex(
                                    ((Number) ((Object[]) arr)[3]).intValue());
                            Object param = ((Object[]) arr)[4];
                            it.setParameter(param,
                                    GXDLMSConverter.getDLMSDataType(param));
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
                    ScriptActionType type = ScriptActionType
                            .values()[((Number) ((Object[]) arr)[0]).intValue()
                                    - 1];
                    it.setType(type);
                    ObjectType ot = ObjectType.forValue(
                            ((Number) ((Object[]) arr)[1]).intValue());
                    String ln = GXCommon
                            .toLogicalName((byte[]) ((Object[]) arr)[2]);
                    GXDLMSObject t = settings.getObjects().findByLN(ot, ln);
                    if (t == null) {
                        t = GXDLMSClient.createObject(ot);
                        t.setLogicalName(ln);
                    }
                    it.setTarget(t);
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

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        scripts.clear();
        if (reader.isStartElement("Scripts", true)) {
            while (reader.isStartElement("Script", true)) {
                GXDLMSScript it = new GXDLMSScript();
                scripts.add(it);
                it.setId(reader.readElementContentAsInt("ID"));
                if (reader.isStartElement("Actions", true)) {
                    while (reader.isStartElement("Action", true)) {
                        GXDLMSScriptAction a = new GXDLMSScriptAction();
                        a.setType(ScriptActionType.values()[reader
                                .readElementContentAsInt("Type")]);
                        ObjectType ot = ObjectType.forValue(
                                reader.readElementContentAsInt("ObjectType"));
                        String ln = reader.readElementContentAsString("LN");
                        GXDLMSObject t = reader.getObjects().findByLN(ot, ln);
                        if (t == null) {
                            t = GXDLMSClient.createObject(ot);
                            t.setLogicalName(ln);
                        }
                        a.setTarget(t);
                        a.setIndex(reader.readElementContentAsInt("Index"));
                        DataType dt = DataType.forValue(reader
                                .readElementContentAsInt("ParameterDataType"));
                        a.setParameter(reader.readElementContentAsObject(
                                "Parameter", null), dt);
                        it.getActions().add(a);
                    }
                    reader.readEndElement("Actions");
                }
            }
            reader.readEndElement("Scripts");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (scripts != null) {
            writer.writeStartElement("Scripts");
            for (GXDLMSScript it : scripts) {
                writer.writeStartElement("Script");
                writer.writeElementString("ID", it.getId());
                writer.writeStartElement("Actions");
                for (GXDLMSScriptAction a : it.getActions()) {
                    writer.writeStartElement("Action");
                    writer.writeElementString("Type", a.getType().ordinal());
                    if (a.getTarget() == null) {
                        writer.writeElementString("ObjectType",
                                ObjectType.NONE.getValue());
                        writer.writeElementString("LN", "0.0.0.0.0.0");
                        writer.writeElementString("Index", "0");
                        writer.writeElementString("ParameterDataType",
                                DataType.NONE.getValue());
                        writer.writeElementObject("Parameter", "");
                    } else {
                        writer.writeElementString("ObjectType",
                                a.getTarget().getObjectType().getValue());
                        writer.writeElementString("LN",
                                a.getTarget().getLogicalName());
                        writer.writeElementString("Index", a.getIndex());
                        writer.writeElementObject("Parameter",
                                a.getParameter());
                    }
                    writer.writeEndElement();
                }
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }

}