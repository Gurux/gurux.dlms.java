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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

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
            if ((type == ObjectType.ALL || it.getObjectType() == type)
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
        GXDLMSObjectCollection objects = new GXDLMSObjectCollection();
        FileInputStream tmp = null;
        try {
            tmp = new FileInputStream(path);
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader xmlStreamReader =
                    inputFactory.createXMLStreamReader(tmp);
            GXDLMSObject obj = null;
            String target;
            ObjectType type;
            String data = null;
            while (xmlStreamReader.hasNext()) {
                int event = xmlStreamReader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    data = null;
                    target = xmlStreamReader.getLocalName();
                    if ("object".compareToIgnoreCase(target) == 0) {
                        type = ObjectType
                                .valueOf(xmlStreamReader.getAttributeValue(0));
                        obj = GXDLMSClient.createObject(type);
                        objects.add(obj);
                    }
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    data = xmlStreamReader.getText();
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    target = xmlStreamReader.getLocalName();
                    if ("SN".compareToIgnoreCase(target) == 0) {
                        obj.setShortName(Integer.parseInt(data));
                    } else if ("LN".compareToIgnoreCase(target) == 0) {
                        obj.setLogicalName(data);
                    } else if ("Description".compareToIgnoreCase(target) == 0) {
                        obj.setDescription(data);
                    }
                }
            }
        } finally {
            if (tmp != null) {
                tmp.close();
            }
        }
        return objects;
    }

    /*
     * Save COSEM objects to the file.
     * @param path File path.
     */
    public final void save(final String path)
            throws IOException, XMLStreamException {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(path, "utf-8");
            String newline = System.getProperty("line.separator");
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = output.createXMLStreamWriter(pw);
            writer.writeStartDocument("utf-8", "1.0");
            writer.setPrefix("gurux", "http://www.gurux.org");
            writer.setDefaultNamespace("http://www.gurux.org");
            writer.writeCharacters(newline);
            writer.writeStartElement("objects");
            writer.writeCharacters(newline);
            for (GXDLMSObject it : this) {
                writer.writeStartElement("object");
                writer.writeAttribute("Type",
                        String.valueOf(it.getObjectType()));
                writer.writeCharacters(newline);
                // Add SN
                if (it.getShortName() != 0) {
                    writer.writeStartElement("SN");
                    writer.writeCharacters(String.valueOf(it.getShortName()));
                    writer.writeEndElement();
                    writer.writeCharacters(newline);
                }
                // Add LN
                writer.writeStartElement("LN");
                writer.writeCharacters(it.getLogicalName());
                writer.writeEndElement();
                writer.writeCharacters(newline);
                // Add description if given.
                if (it.getDescription() != null
                        && !it.getDescription().isEmpty()) {
                    writer.writeStartElement("Description");
                    writer.writeCharacters(it.getDescription());
                    writer.writeEndElement();
                    writer.writeCharacters(newline);
                }
                // Close object.
                writer.writeEndElement();
                writer.writeCharacters(newline);
            }
            // Close objects
            writer.writeEndElement();
            writer.writeEndDocument();
            pw.flush();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}