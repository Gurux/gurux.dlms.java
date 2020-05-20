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
import gurux.dlms.enums.ObjectType;

/**
 * Collection of DLMS objects.
 */
public class GXDLMSObjectCollection extends ArrayList<GXDLMSObject>
        implements java.util.List<GXDLMSObject> {
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

    public final GXDLMSObject findByLN(final ObjectType type, final String ln) {
        for (GXDLMSObject it : this) {
            if ((type == ObjectType.NONE || it.getObjectType() == type)
                    && it.getLogicalName().trim().equals(ln)) {
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
    public static GXDLMSObjectCollection load(final String path)
            throws XMLStreamException, IOException {
        FileInputStream stream = new FileInputStream(path);
        try {
            return load(stream);
        } finally {
            if (stream != null) {
                stream.close();
                stream = null;
            }
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
    public static GXDLMSObjectCollection load(final InputStream stream)
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
                } else if (target.startsWith("GXDLMS")) {
                    type = ObjectType.getEnum(target.substring(6));
                    if (type == null) {
                        throw new RuntimeException(
                                "Invalid object type: " + target + ".");
                    }
                    reader.read();
                    obj = GXDLMSClient.createObject(type);
                    obj.setVersion(0);
                    reader.getObjects().add(obj);
                } else if ("Object".equalsIgnoreCase(target)) {
                    // Old format.
                    type = ObjectType
                            .forValue(Integer.parseInt(reader.getAttribute(0)));
                    reader.read();
                    obj = GXDLMSClient.createObject(type);
                    obj.setVersion(0);
                    reader.getObjects().add(obj);
                } else if ("SN".equalsIgnoreCase(target)) {
                    obj.setShortName(reader.readElementContentAsInt("SN"));
                } else if ("LN".equalsIgnoreCase(target)) {
                    obj.setLogicalName(reader.readElementContentAsString("LN"));
                } else if ("Description".equalsIgnoreCase(target)) {
                    obj.setDescription(
                            reader.readElementContentAsString("Description"));
                } else if ("Version".equalsIgnoreCase(target)) {
                    obj.setVersion(reader.readElementContentAsInt("Version"));
                } else {
                    ((IGXDLMSBase) obj).load(reader);
                    obj = null;
                }
            } else {
                reader.read();
            }
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
    public final void save(final String filename,
            final GXXmlWriterSettings settings)
            throws XMLStreamException, IOException {
        FileOutputStream stream = new FileOutputStream(filename);
        try {
            save(stream, settings);
        } finally {
            if (stream != null) {
                stream.close();
                stream = null;
            }
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
    public final void save(final OutputStream stream,
            final GXXmlWriterSettings settings) throws XMLStreamException {
        GXXmlWriter writer = new GXXmlWriter(stream, settings);
        try {
            writer.writeStartDocument();
            writer.writeStartElement("Objects");
            for (GXDLMSObject it : this) {
                if (settings == null || !settings.getOld()) {
                    writer.writeStartElement(
                            "GXDLMS" + getObjectName(it.getObjectType()));
                } else {
                    writer.writeStartElement("Object", "Type",
                            String.valueOf(it.getObjectType().getValue()),
                            true);
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