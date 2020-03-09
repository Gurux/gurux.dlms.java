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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import gurux.dlms.GXArray;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.GXStructure;
import gurux.dlms.enums.DataType;
import gurux.dlms.internal.GXCommon;

/**
 * Save COSEM object to the file.
 */
public class GXXmlWriter implements AutoCloseable {
    private XMLStreamWriter writer = null;
    private String newline = System.getProperty("line.separator");
    private int indenting = 0;
    char[] spaces = null;

    /**
     * Close writer.
     */
    public final void close() throws XMLStreamException {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

    /**
     * Constructor.
     * 
     * @param filename
     *            File name.
     * @throws XMLStreamException
     *             Invalid XML stream.
     * @throws FileNotFoundException
     *             File not found.
     */
    public GXXmlWriter(final String filename)
            throws FileNotFoundException, XMLStreamException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        writer = outputFactory
                .createXMLStreamWriter(new FileOutputStream(filename));
    }

    /**
     * Constructor.
     * 
     * @param s
     *            Stream.
     * @throws XMLStreamException
     *             Invalid XML stream.
     */
    public GXXmlWriter(final OutputStream s) throws XMLStreamException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        writer = outputFactory.createXMLStreamWriter(s);
    }

    /**
     * Append spaces to the buffer.
     * 
     * @param count
     *            Amount of spaces.
     * @throws XMLStreamException
     */
    private void appendSpaces() throws XMLStreamException {
        if (indenting > spaces.length / 2) {
            writer.writeCharacters(spaces, 0, spaces.length);
        } else {
            writer.writeCharacters(spaces, 0, 2 * indenting);
        }
    }

    public final void writeStartDocument() throws XMLStreamException {
        if (spaces == null) {
            spaces = new char[100];
            for (int pos = 0; pos != spaces.length; ++pos) {
                spaces[pos] = ' ';
            }
        }
        writer.writeStartDocument();
        writer.writeCharacters(newline);
        indenting = 0;
    }

    public final void writeStartElement(final String name)
            throws XMLStreamException {
        appendSpaces();
        writer.writeStartElement(name);
        writer.writeCharacters(newline);
        ++indenting;
    }

    public final void writeStartElement(final String elementName,
            final String attributeName, final String value,
            final boolean newLine) throws XMLStreamException {
        appendSpaces();
        writer.writeStartElement(elementName);
        writer.writeAttribute(attributeName, value);
        if (newLine) {
            writer.writeCharacters(newline);
        }
        ++indenting;
    }

    public final void writeStartElement(final String elementName,
            final List<Map.Entry<String, String>> attributes,
            final boolean newLine) throws XMLStreamException {
        appendSpaces();
        writer.writeStartElement(elementName);
        for (Map.Entry<String, String> it : attributes) {
            writer.writeAttribute(it.getKey(), it.getValue());
        }
        if (newLine) {
            writer.writeCharacters(newline);
        }
        ++indenting;
    }

    public final void writeElementString(final String name, final Date value)
            throws XMLStreamException {
        if (value != null && value.compareTo(new Date(0)) != 0) {
            SimpleDateFormat sd = new SimpleDateFormat("", Locale.US);
            writeElementString(name, sd.format(value));
        }
    }

    public final void writeElementString(final String name, final long value)
            throws XMLStreamException {
        if (value != 0) {
            writeElementString(name, String.valueOf(value));
        }
    }

    public final void writeElementString(final String name, final double value)
            throws XMLStreamException {
        writeElementString(name, value, 0);
    }

    public final void writeElementString(final String name, final double value,
            final double defaultValue) throws XMLStreamException {
        if (value != defaultValue) {
            writeElementString(name, String.valueOf(value));
        }
    }

    public final void writeElementString(final String name, final int value)
            throws XMLStreamException {
        if (value != 0) {
            writeElementString(name, String.valueOf(value));
        }
    }

    public final void writeElementString(final String name, final String value)
            throws XMLStreamException {
        if (value != null && value.length() != 0) {
            appendSpaces();
            writer.writeStartElement(name);
            writer.writeCharacters(value);
            writer.writeEndElement();
            writer.writeCharacters(newline);
        }
    }

    public final void writeElementString(final String name, final boolean value)
            throws XMLStreamException {
        if (value) {
            writeElementString(name, "1");
        }
    }

    public final void writeElementString(final String name,
            final GXDateTime value) throws XMLStreamException {
        if (value != null && value.getMeterCalendar()
                .getTime() != new java.util.Date(0)) {
            writeElementString(name, value.toFormatString(Locale.ROOT));
        }
    }

    private void writeArray(final Object data) throws XMLStreamException {
        if (data instanceof List<?>) {
            writer.writeCharacters(System.getProperty("line.separator"));
            List<?> arr = (List<?>) data;
            for (Object tmp : arr) {
                if (tmp instanceof byte[]) {
                    writeElementObject("Item", tmp, false);
                } else if (tmp instanceof GXArray) {
                    writeStartElement("Item", "Type",
                            String.valueOf(DataType.ARRAY.getValue()), true);
                    writeArray(tmp);
                    writeEndElement();
                } else if (tmp instanceof GXStructure) {
                    writeStartElement("Item", "Type",
                            String.valueOf(DataType.STRUCTURE.getValue()),
                            true);
                    writeArray(tmp);
                    writeEndElement();
                } else {
                    writeElementObject("Item", tmp);
                }
            }
        }
    }

    public final void writeElementObject(final String name, final Object value)
            throws XMLStreamException {
        writeElementObject(name, value, true);
    }

    public final void writeElementObject(final String name, final Object value,
            final DataType type, final DataType uiType)
            throws XMLStreamException {
        if (type != DataType.NONE && value instanceof String) {
            if (type == DataType.OCTET_STRING) {
                if (uiType == DataType.STRING) {
                    writeElementObject(name, ((String) value).getBytes(), true);
                    return;
                } else if (uiType == DataType.OCTET_STRING) {
                    writeElementObject(name,
                            GXDLMSTranslator.hexToBytes((String) value), true);
                    return;
                }
            } else if (!(value instanceof GXDateTime)) {
                writeElementObject(name,
                        GXDLMSConverter.changeType(value, type), true);
                return;
            }
        }
        if (type != DataType.NONE) {
            if (uiType == DataType.NONE
                    && (value instanceof Float || value instanceof Double)) {
                writeElementObject(name, value, true, type, DataType.FLOAT64);
            } else {
                writeElementObject(name, value, true, type, uiType);
            }
        } else {
            writeElementObject(name, value, true);
        }
    }

    /**
     * Write object value to file.
     * 
     * @param name
     *            Object name.
     * @param value
     *            Object value.
     * @param skipDefaultValue
     *            Is default value serialized.
     * @param dt
     *            Data type of serialized value.
     * @throws XMLStreamException
     *             Invalid XML stream.
     */
    public final void writeElementObject(final String name, final Object value,
            final boolean skipDefaultValue, final DataType dt,
            final DataType uiType) throws XMLStreamException {
        if (value != null) {
            if (skipDefaultValue && value instanceof java.util.Date
                    && (((java.util.Date) value)
                            .compareTo(new java.util.Date(0))) == 0) {
                return;
            }
            if (uiType != DataType.NONE && uiType != DataType.STRING) {
                List<Map.Entry<String, String>> list =
                        new ArrayList<Map.Entry<String, String>>();
                list.add(new GXSimpleEntry<String, String>("Type",
                        String.valueOf(dt.getValue())));
                list.add(new GXSimpleEntry<String, String>("UIType",
                        String.valueOf(uiType.getValue())));
                writeStartElement(name, list, false);
            } else {
                writeStartElement(name, "Type", String.valueOf(dt.getValue()),
                        false);
            }
            if (dt == DataType.ARRAY || dt == DataType.STRUCTURE) {
                writeArray(value);
            } else {
                if (value instanceof GXDateTime) {
                    writer.writeCharacters(
                            ((GXDateTime) value).toFormatString(Locale.ROOT));
                } else if (value instanceof byte[]) {
                    writer.writeCharacters(GXCommon.toHex((byte[]) value));
                } else {
                    writer.writeCharacters(String.valueOf(value));
                }
            }
            writeEndElement(false);
        }
    }

    /**
     * Write object value to file.
     * 
     * @param name
     *            Object name.
     * @param value
     *            Object value.
     * @param skipDefaultValue
     *            Is default value serialized.
     * @throws XMLStreamException
     *             Invalid XML stream.
     */
    public final void writeElementObject(final String name, final Object value,
            final boolean skipDefaultValue) throws XMLStreamException {
        if (value != null) {
            if (skipDefaultValue && value instanceof java.util.Date
                    && (((java.util.Date) value)
                            .compareTo(new java.util.Date(0))) == 0) {
                return;
            }
            DataType dt = GXDLMSConverter.getDLMSDataType(value);
            writeElementObject(name, value, skipDefaultValue, dt,
                    DataType.NONE);
        }
    }

    private void writeEndElement(final boolean addSpaces)
            throws XMLStreamException {
        --indenting;
        if (addSpaces) {
            appendSpaces();
        }
        writer.writeEndElement();
        writer.writeCharacters(newline);
    }

    /**
     * Write End Element tag.
     * 
     * @throws XMLStreamException
     *             Invalid XML stream.
     */
    public final void writeEndElement() throws XMLStreamException {
        writeEndElement(true);
    }

    /**
     * Write End document tag.
     * 
     * @throws XMLStreamException
     *             Invalid XML stream.
     */
    public final void writeEndDocument() throws XMLStreamException {
        writer.writeEndDocument();
    }

    /**
     * Write any cached data to the stream.
     * 
     * @throws XMLStreamException
     *             Invalid XML stream.
     */
    public final void flush() throws XMLStreamException {
        writer.flush();
    }
}
