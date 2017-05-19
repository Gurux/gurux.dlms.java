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
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXDate;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXTime;
import gurux.dlms.enums.DataType;

/**
 * Read serialized COSEM object from the file.
 */
public class GXXmlReader implements AutoCloseable {

    /**
     * Element name.
     */
    private String elementName;

    private XMLStreamReader reader = null;

    private FileInputStream stream;
    /**
     * Collection of read objects.
     */
    private GXDLMSObjectCollection privateObjects;

    public final GXDLMSObjectCollection getObjects() {
        return privateObjects;
    }

    private void setObjects(final GXDLMSObjectCollection value) {
        privateObjects = value;
    }

    @Override
    public final void close() throws Exception {
        if (reader != null) {
            reader.close();
            reader = null;
        }
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    /**
     * Constructor.
     * 
     * @param s
     *            Input stream.
     * @throws XMLStreamException
     *             Invalid XML stream.
     */
    public GXXmlReader(final InputStream s) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        reader = inputFactory.createXMLStreamReader(s);
        setObjects(new GXDLMSObjectCollection());
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
    public GXXmlReader(final String filename)
            throws XMLStreamException, FileNotFoundException {
        stream = new java.io.FileInputStream(filename);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        reader = inputFactory.createXMLStreamReader(stream);
        setObjects(new GXDLMSObjectCollection());
    }

    /**
     * @return Name of current tag.
     */
    public String getName() {
        if (elementName == null
                || reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
            elementName = String.valueOf(reader.getName());
        }
        return elementName;
    }

    private void getNext() throws XMLStreamException {
        int type = reader.getEventType();
        while (type == XMLStreamConstants.COMMENT
                || type == XMLStreamConstants.SPACE
                || type == XMLStreamConstants.CHARACTERS) {
            read();
            type = reader.getEventType();
        }
    }

    public final boolean isEOF() throws XMLStreamException {
        return !reader.hasNext();
    }

    public final boolean read() throws XMLStreamException {
        reader.next();
        return reader.hasNext();
    }

    public final void readEndElement(final String name)
            throws XMLStreamException {
        getNext();
        if (reader.getEventType() == XMLStreamConstants.END_ELEMENT
                && name.equalsIgnoreCase(getName())) {
            read();
            getNext();
        }
    }

    public final boolean isStartElement(final String name,
            final boolean getNext) throws XMLStreamException {
        getNext();
        boolean ret = reader.getEventType() == XMLStreamConstants.START_ELEMENT
                && name.equalsIgnoreCase(getName());

        if (getNext && (ret
                || (reader.getEventType() == XMLStreamConstants.END_ELEMENT))) {
            if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                elementName = null;
            }
            if (ret || name.equalsIgnoreCase(getName())) {
                read();
                if (!ret) {
                    ret = isStartElement(name, getNext);
                }
            }
        }
        getNext();
        return ret;
    }

    public final boolean isStartElement() {
        return reader.isStartElement();
    }

    public final String getAttribute(final int index) {
        return reader.getAttributeValue(index);
    }

    public final int readElementContentAsInt(final String name)
            throws XMLStreamException {
        return readElementContentAsInt(name, 0);
    }

    public final int readElementContentAsInt(final String name,
            final int defaultValue) throws XMLStreamException {
        getNext();
        if (name.compareToIgnoreCase(getName()) == 0) {
            String str = getText();
            int ret = Integer.parseInt(str);
            return ret;
        }
        return defaultValue;
    }

    public final long readElementContentAsLong(final String name)
            throws XMLStreamException {
        return readElementContentAsLong(name, 0);
    }

    public final long readElementContentAsLong(final String name,
            final long defaultValue) throws XMLStreamException {
        getNext();
        if (name.compareToIgnoreCase(getName()) == 0) {
            String str = getText();
            long ret = Long.parseLong(str);
            return ret;
        }
        return defaultValue;
    }

    public final long readElementContentAsULong(final String name)
            throws XMLStreamException {
        return readElementContentAsULong(name, 0);
    }

    public final long readElementContentAsULong(final String name,
            final long defaultValue) throws XMLStreamException {
        getNext();
        if (name.compareToIgnoreCase(getName()) == 0) {
            String str = getText();
            long ret = Long.parseLong(str);
            return ret;
        }
        return defaultValue;
    }

    public final double readElementContentAsDouble(final String name,
            final double defaultValue) throws XMLStreamException {
        getNext();
        if (name.compareToIgnoreCase(getName()) == 0) {
            String str = getText();
            double ret = Double.parseDouble(str);
            return ret;
        }
        return defaultValue;
    }

    private Object[] readArray() throws XMLStreamException {
        java.util.ArrayList<Object> list = new java.util.ArrayList<Object>();
        while (isStartElement("Item", false)) {
            list.add(readElementContentAsObject("Item", null));
        }
        return list.toArray(new Object[0]);
    }

    public final Object readElementContentAsObject(final String name,
            final Object defaultValue) throws XMLStreamException {
        getNext();
        if (name.compareToIgnoreCase(getName()) == 0) {
            Object ret = null;
            String str = getAttribute(0);
            DataType tp = DataType.forValue(Integer.parseInt(str));
            if (tp == DataType.ARRAY) {
                read();
                getNext();
                ret = readArray();
                readEndElement(name);
                return ret;
            } else {
                str = getText();
                if (tp == DataType.OCTET_STRING) {
                    ret = GXDLMSTranslator.hexToBytes(str);
                } else if (tp == DataType.DATETIME) {
                    ret = new GXDateTime(str);
                } else if (tp == DataType.DATE) {
                    ret = new GXDate(str);
                } else if (tp == DataType.TIME) {
                    ret = new GXTime(str);
                } else {
                    ret = GXDLMSConverter.changeType(str, tp);
                }
            }
            int type = reader.getEventType();
            while (!(type == XMLStreamConstants.START_ELEMENT
                    || type == XMLStreamConstants.END_ELEMENT)) {
                read();
                type = reader.getEventType();
            }
            return ret;
        }
        return defaultValue;
    }

    public final String readElementContentAsString(final String name)
            throws XMLStreamException {
        return readElementContentAsString(name, null);
    }

    private String getText() throws XMLStreamException {
        String ret = reader.getElementText();
        read();
        getNext();
        elementName = null;
        return ret;
    }

    public final String readElementContentAsString(final String name,
            final String defaultValue) throws XMLStreamException {
        getNext();
        if (name.compareToIgnoreCase(getName()) == 0) {
            return getText();
        }
        return defaultValue;
    }

    @Override
    public final String toString() {
        if (reader != null) {
            return reader.getEventType() + ", Name=\"" + elementName + "\"";
        }
        return super.toString();
    }

}