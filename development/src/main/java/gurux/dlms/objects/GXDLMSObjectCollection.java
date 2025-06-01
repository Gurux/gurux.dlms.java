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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AccessMode3;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.MethodAccessMode3;
import gurux.dlms.enums.ObjectType;

/**
 * Collection of DLMS objects.
 */
public class GXDLMSObjectCollection extends ArrayList<GXDLMSObject> implements java.util.List<GXDLMSObject> {
    private static final long serialVersionUID = 1L;
    private Object parent;

    /**
     * Constructor.
     */
    public GXDLMSObjectCollection() {
    }

    /**
     * Constructor.
     * 
     * @param forParent
     *            Parent object.
     */
    public GXDLMSObjectCollection(final Object forParent) {
        parent = forParent;
    }

    public final Object getParent() {
        return parent;
    }

    final void setParent(final Object value) {
        parent = value;
    }

    public final GXDLMSObjectCollection getObjects(final ObjectType type) {
        GXDLMSObjectCollection items = new GXDLMSObjectCollection();
        for (GXDLMSObject it : this) {
            if (it.getObjectType() == type) {
                items.add(it);
            }
        }
        return items;
    }

    public final GXDLMSObjectCollection getObjects(final ObjectType[] types) {
        GXDLMSObjectCollection items = new GXDLMSObjectCollection();
        for (GXDLMSObject it : this) {
            for (ObjectType type : types) {
                if (type == it.getObjectType()) {
                    items.add(it);
                    break;
                }
            }
        }
        return items;
    }

    /**
     * Find COSEM object.
     * 
     * @param type
     *            Object type.
     * @param ln
     *            Logical name.
     * @return Found object or null if object is not found.
     */
    public final GXDLMSObject findByLN(final ObjectType type, final String ln) {
        for (GXDLMSObject it : this) {
            if ((type == ObjectType.NONE || it.getObjectType() == type) && it.getLogicalName().trim().equals(ln)) {
                return it;
            }
        }
        return null;
    }

    /**
     * Find custom COSEM object.
     * 
     * @param type
     *            Object type.
     * @param ln
     *            Logical name.
     * @return Found object or null if object is not found.
     */
    public final GXDLMSObject findByLN(final int type, final String ln) {
        for (GXDLMSObject it : this) {
            if ((type == 0 || it.getCustomObjectType() == type) && it.getLogicalName().trim().equals(ln)) {
                return it;
            }
        }
        return null;
    }

    public final GXDLMSObject findBySN(final int sn) {
        for (GXDLMSObject it : this) {
            if (it.getShortName() == sn) {
                return it;
            }
        }
        return null;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (GXDLMSObject it : this) {
            if (sb.length() != 1) {
                sb.append(", ");
            }
            sb.append(it.getName().toString());
        }
        sb.append(']');
        return sb.toString();
    }

    /*
     * Load COSEM objects from the file.
     * @param path File path.
     * @return Collection of serialized COSEM objects.
     */
    public static GXDLMSObjectCollection load(final String path) throws XMLStreamException, IOException {
        FileInputStream stream = new FileInputStream(path);
        try {
            return load(stream);
        } finally {
            stream.close();
            stream = null;
        }
    }

    /**
     * Load COSEM objects from the stream.
     * 
     * @param stream
     *            XML stream.
     * @return Collection of serialized COSEM objects.
     * @throws XMLStreamException
     *             Stream exception.
     */
    public static GXDLMSObjectCollection load(final InputStream stream) throws XMLStreamException {
        return load(null, stream);
    }

    /**
     * Load COSEM objects from the stream.
     * 
     * @param settings
     *            DLMS settings is need when custom objects are serialized.
     * @param stream
     *            XML stream.
     * @return Collection of serialized COSEM objects.
     * @throws XMLStreamException
     *             Stream exception.
     */
    public static GXDLMSObjectCollection load(final GXDLMSSettings settings, final InputStream stream)
            throws XMLStreamException {
        GXDLMSObject obj = null;
        String target;
        ObjectType type = null;
        GXXmlReader reader = new GXXmlReader(stream);
        while (!reader.isEOF()) {
            if (reader.isStartElement()) {
                target = reader.getName();
                if ("Objects".equalsIgnoreCase(target)) {
                    // Skip.
                    reader.read();
                } else if (target.startsWith("GXDLMSCustom")) {
                    int ot = Integer.parseInt(target.substring(12));
                    obj = GXDLMSClient.createCustomObject(settings, ot);
                } else if (target.startsWith("GXDLMS")) {
                    type = ObjectType.getEnum(target.substring(6));
                    if (type == null) {
                        throw new RuntimeException("Invalid object type: " + target + ".");
                    }
                    reader.read();
                    obj = GXDLMSClient.createObject(type);
                    obj.setVersion(0);
                } else if ("Object".equalsIgnoreCase(target)) {
                    // Old format.
                    type = ObjectType.forValue(Integer.parseInt(reader.getAttribute(0)));
                    reader.read();
                    obj = GXDLMSClient.createObject(type);
                    obj.setVersion(0);
                } else if ("SN".equalsIgnoreCase(target)) {
                    if (obj != null) {
                        obj.setShortName(reader.readElementContentAsInt("SN"));
                        GXDLMSObject tmp = reader.getObjects().findBySN(obj.getShortName());
                        if (tmp == null) {
                            reader.getObjects().add(obj);
                        } else {
                            obj = tmp;
                        }
                    } else {
                        // Skip SN if obj is null
                        reader.readElementContentAsInt("SN");
                    }
                } else if ("LN".equalsIgnoreCase(target)) {
                    if (obj != null) {
                        obj.setLogicalName(reader.readElementContentAsString("LN"));
                        GXDLMSObject tmp = reader.getObjects().findByLN(obj.getObjectType(), obj.getLogicalName());
                        if (tmp == null) {
                            reader.getObjects().add(obj);
                        } else {
                            // Version must be update because component might be
                            // added to the association
                            // view.
                            tmp.setVersion(obj.getVersion());
                            obj = tmp;
                        }
                    } else {
                        // Skip LN if obj is null
                        reader.readElementContentAsString("LN");
                    }
                } else if ("Description".equalsIgnoreCase(target)) {
                    if (obj != null) {
                        obj.setDescription(reader.readElementContentAsString("Description"));
                    } else {
                        // Skip Description if obj is null
                        reader.readElementContentAsString("Description");
                    }
                } else if ("Version".equalsIgnoreCase(target)) {
                    if (obj != null) {
                        obj.setVersion(reader.readElementContentAsInt("Version"));
                    } else {
                        // Skip Version if obj is null
                        reader.readElementContentAsInt("Version");
                    }
                } else if ("Access".equalsIgnoreCase(target)) {
                    String accessStr = reader.readElementContentAsString("Access");
                    if (obj != null) {
                        int pos = 0;
                        for (byte it : accessStr.getBytes()) {
                            ++pos;
                            if (obj.getVersion() < 3) {
                                obj.setAccess(pos, AccessMode.forValue(it - 0x30));
                            } else {
                                // Handle old cases.
                                int tmp = it - 0x30;
                                if (tmp == 1) {
                                    obj.getAccess3(pos).add(AccessMode3.READ);
                                } else if (tmp == 2) {
                                    obj.getAccess3(pos).add(AccessMode3.WRITE);
                                } else if (tmp == 3) {
                                    obj.getAccess3(pos).add(AccessMode3.READ);
                                    obj.getAccess3(pos).add(AccessMode3.WRITE);
                                }
                            }
                        }
                    }
                } else if ("Access3".equalsIgnoreCase(target)) {
                    String tmp = reader.readElementContentAsString("Access3");
                    if (tmp != null && obj != null) {
                        for (int pos = 0; pos != tmp.length() / 4; ++pos) {
                            obj.getAccess3(pos).addAll(
                                    AccessMode3.forValue(Integer.parseInt(tmp.substring(4 * pos, 4 * pos + 4), 16)));
                        }
                    }
                } else if ("MethodAccess".equalsIgnoreCase(target)) {
                    String methodAccessStr = reader.readElementContentAsString("MethodAccess");
                    if (obj != null) {
                        int pos = 0;
                        for (byte it : methodAccessStr.getBytes()) {
                            ++pos;
                            if (obj.getVersion() < 3) {
                                obj.setMethodAccess(pos, MethodAccessMode.forValue(it - 0x30));
                            } else {
                                // Handle old cases.
                                obj.getMethodAccess3(pos).addAll(MethodAccessMode3.forValue(it - 0x30));
                            }
                        }
                    }
                } else if ("MethodAccess3".equalsIgnoreCase(target)) {
                    String tmp = reader.readElementContentAsString("MethodAccess3");
                    if (tmp != null && obj != null) {
                        for (int pos = 0; pos != tmp.length() / 4; ++pos) {
                            obj.getMethodAccess3(pos).addAll(MethodAccessMode3
                                    .forValue(Integer.parseInt(tmp.substring(4 * pos, 4 * pos + 4), 16)));
                        }
                    }
                } else {
                    // Check if obj is null before calling load
                    if (obj != null) {
                        try {
                            ((IGXDLMSBase) obj).load(reader);
                        } catch (Exception e) {
                            System.out.println("Error loading object: " + e.getMessage());
                        }
                        obj = null;
                    } else {
                        // Skip unknown element with context information
                        String parentInfo = "";
                        if (type != null) {
                            parentInfo = " in " + type.toString();
                        }
                        
                        System.out.println("Skipping unknown element: '" + target + "'" + parentInfo);
                        reader.read();
                    }
                }
            } else {
                reader.read();
            }
        }
        for (GXDLMSObject it : reader.getObjects()) {
            ((IGXDLMSBase) it).postLoad(reader);
        }
        return reader.getObjects();
    }

    /**
     * Save COSEM objects to the file.
     * 
     * @param filename
     *            File path.
     * @param settings
     *            XML write settings.
     * @throws XMLStreamException
     *             XML exception.
     * @throws IOException
     *             IO exception.
     */
    public final void save(final String filename, final GXXmlWriterSettings settings)
            throws XMLStreamException, IOException {
        FileOutputStream stream = new FileOutputStream(filename);
        try {
            save(stream, settings);
        } finally {
            stream.close();
            stream = null;
        }
    }

    private String getObjectName(final ObjectType ot) {
        String name = String.valueOf(ot).toLowerCase();
        String tmp[] = name.split("_");
        for (int pos = 0; pos != tmp.length; ++pos) {
            char[] array = tmp[pos].toCharArray();
            array[0] = Character.toUpperCase(array[0]);
            tmp[pos] = new String(array);
        }
        name = String.join("", tmp);
        return name;
    }

    /**
     * Save COSEM objects to the file.
     * 
     * @param stream
     *            Stream.
     * @param settings
     *            XML write settings.
     * @throws XMLStreamException
     *             XML exception.
     */
    public final void save(final OutputStream stream, final GXXmlWriterSettings settings) throws XMLStreamException {
        GXXmlWriter writer = new GXXmlWriter(stream, settings);
        try {
            writer.writeStartDocument();
            writer.writeStartElement("Objects");
            int lnVersion = 2;
            for (GXDLMSObject it : this) {
                if (settings == null || !settings.getOld()) {
                    ObjectType ot = it.getObjectType();
                    if (ot != null) {
                        writer.writeStartElement("GXDLMS" + getObjectName(ot));
                    } else {
                        writer.writeStartElement("GXDLMSCustom" + it.getCustomObjectType());
                    }
                } else {
                    writer.writeStartElement("Object", "Type", String.valueOf(it.getObjectType().getValue()), true);
                }
                // Add SN
                if (it.getShortName() != 0) {
                    writer.writeElementString("SN", it.getShortName());
                }
                // Add LN
                writer.writeElementString("LN", it.getLogicalName());
                // Add Version
                if (it.getVersion() != 0) {
                    writer.writeElementString("Version", it.getVersion());
                }
                // Add description if given.
                String d = it.getDescription();
                if (d != null && d.length() != 0) {
                    writer.writeElementString("Description", d);
                }
                // Add access rights.
                if (lnVersion < 3) {
                    StringBuilder sb = new StringBuilder();
                    for (int pos = 1; pos != it.getAttributeCount() + 1; ++pos) {
                        sb.append(String.valueOf(it.getAccess(pos).getValue()));
                    }
                    writer.writeElementString("Access", sb.toString());
                    sb.setLength(0);
                    for (int pos = 1; pos != it.getMethodCount() + 1; ++pos) {
                        sb.append(String.valueOf(it.getMethodAccess(pos).getValue()));
                    }
                    writer.writeElementString("MethodAccess", sb.toString());
                } else {
                    StringBuilder sb = new StringBuilder();
                    int value;
                    for (int pos = 1; pos != it.getAttributeCount() + 1; ++pos) {
                        // Set highest bit to save integer with two chars.
                        value = 0x8000;
                        for (AccessMode3 it2 : it.getAccess3(pos)) {
                            value |= it2.getValue();
                        }
                        sb.append(Integer.toHexString(value));
                    }
                    writer.writeElementString("Access3", sb.toString());
                    sb.setLength(0);
                    for (int pos = 1; pos != it.getMethodCount() + 1; ++pos) {
                        // Set highest bit to save integer with two chars.
                        value = 0x8000;
                        for (MethodAccessMode3 it2 : it.getMethodAccess3(pos)) {
                            value |= it2.getValue();
                        }
                        sb.append(Integer.toHexString(value));
                    }
                    writer.writeElementString("MethodAccess3", sb.toString());
                }
                if (settings == null || settings.getValues()) {
                    ((IGXDLMSBase) it).save(writer);
                }
                // Close object.
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndDocument();
        } finally {
            writer.close();
        }
    }
}
