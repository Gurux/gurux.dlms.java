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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.AutoConnectMode;

public class GXDLMSAutoConnect extends GXDLMSObject implements IGXDLMSBase {
    private AutoConnectMode mode;
    private List<Entry<GXDateTime, GXDateTime>> callingWindow;
    private String[] destinations;
    private int repetitionDelay;
    private int repetitions;

    /**
     * Constructor.
     */
    public GXDLMSAutoConnect() {
        this("0.0.2.1.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSAutoConnect(final String ln) {
        this(ln, (short) 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSAutoConnect(final String ln, final int sn) {
        super(ObjectType.AUTO_CONNECT, ln, sn);
        callingWindow = new ArrayList<Entry<GXDateTime, GXDateTime>>();
        mode = AutoConnectMode.NO_AUTO_DIALLING;
    }

    public final AutoConnectMode getMode() {
        return mode;
    }

    public final void setMode(final AutoConnectMode value) {
        mode = value;
    }

    public final int getRepetitions() {
        return repetitions;
    }

    public final void setRepetitions(final int value) {
        repetitions = value;
    }

    public final int getRepetitionDelay() {
        return repetitionDelay;
    }

    public final void setRepetitionDelay(final int value) {
        repetitionDelay = value;
    }

    public final List<Entry<GXDateTime, GXDateTime>> getCallingWindow() {
        return callingWindow;
    }

    public final void
            setCallingWindow(final List<Entry<GXDateTime, GXDateTime>> value) {
        callingWindow = value;
    }

    public final String[] getDestinations() {
        return destinations;
    }

    public final void setDestinations(final String[] value) {
        destinations = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getMode(),
                new Integer(getRepetitions()),
                new Integer(getRepetitionDelay()), getCallingWindow(),
                getDestinations() };
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
        // Mode
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        // Repetitions
        if (canRead(3)) {
            attributes.add(new Integer(3));
        }
        // RepetitionDelay
        if (canRead(4)) {
            attributes.add(new Integer(4));
        }
        // CallingWindow
        if (canRead(5)) {
            attributes.add(new Integer(5));
        }
        // Destinations
        if (canRead(6)) {
            attributes.add(new Integer(6));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 6;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 0;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ENUM;
        }
        if (index == 3) {
            return DataType.UINT8;
        }
        if (index == 4) {
            return DataType.UINT16;
        }
        if (index == 5) {
            return DataType.ARRAY;
        }
        if (index == 6) {
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
            return new Byte((byte) mode.getValue());
        }
        if (e.getIndex() == 3) {
            return new Integer(getRepetitions());
        }
        if (e.getIndex() == 4) {
            return new Integer(getRepetitionDelay());
        }
        if (e.getIndex() == 5) {
            int cnt = getCallingWindow().size();
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8((byte) DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0) {
                for (Entry<GXDateTime, GXDateTime> it : callingWindow) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    // Count
                    data.setUInt8(2);
                    // Start time
                    GXCommon.setData(data, DataType.OCTET_STRING, it.getKey());
                    // End time
                    GXCommon.setData(data, DataType.OCTET_STRING,
                            it.getValue());
                }
            }
            return data.array();
        }
        if (e.getIndex() == 6) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (getDestinations() == null) {
                // Add count
                GXCommon.setObjectCount(0, data);
            } else {
                int cnt = getDestinations().length;
                // Add count
                GXCommon.setObjectCount(cnt, data);
                for (String it : getDestinations()) {
                    GXCommon.setData(data, DataType.OCTET_STRING,
                            GXCommon.getBytes(it)); // destination
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
            setMode(AutoConnectMode
                    .forValue(((Number) e.getValue()).intValue()));
        } else if (e.getIndex() == 3) {
            setRepetitions(((Number) e.getValue()).intValue());
        } else if (e.getIndex() == 4) {
            setRepetitionDelay(((Number) e.getValue()).intValue());
        } else if (e.getIndex() == 5) {
            getCallingWindow().clear();
            if (e.getValue() != null) {
                for (Object item : (Object[]) e.getValue()) {
                    GXDateTime start = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) ((Object[]) item)[0], DataType.DATETIME);
                    GXDateTime end = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) ((Object[]) item)[1], DataType.DATETIME);
                    getCallingWindow().add(
                            new GXSimpleEntry<GXDateTime, GXDateTime>(start,
                                    end));
                }
            }
        } else if (e.getIndex() == 6) {
            setDestinations(null);
            if (e.getValue() != null) {
                List<String> items = new ArrayList<String>();
                for (Object item : (Object[]) e.getValue()) {
                    String it = GXDLMSClient
                            .changeType((byte[]) item, DataType.STRING)
                            .toString();
                    items.add(it);
                }
                setDestinations(items.toArray(new String[items.size()]));
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        mode = AutoConnectMode.forValue(reader.readElementContentAsInt("Mode"));
        repetitions = reader.readElementContentAsInt("Repetitions");
        repetitionDelay = reader.readElementContentAsInt("RepetitionDelay");
        callingWindow.clear();
        if (reader.isStartElement("CallingWindow", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDateTime start = new GXDateTime(
                        reader.readElementContentAsString("Start"));
                GXDateTime end = new GXDateTime(
                        reader.readElementContentAsString("End"));
                callingWindow.add(
                        new SimpleEntry<GXDateTime, GXDateTime>(start, end));
            }
            reader.readEndElement("CallingWindow");
        }
        destinations = GXCommon
                .split(reader.readElementContentAsString("Destinations", ""),
                        ';')
                .toArray(new String[0]);
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Mode", mode.ordinal());
        writer.writeElementString("Repetitions", repetitions);
        writer.writeElementString("RepetitionDelay", repetitionDelay);
        if (callingWindow != null) {
            writer.writeStartElement("CallingWindow");
            for (Entry<GXDateTime, GXDateTime> it : callingWindow) {
                writer.writeStartElement("Item");
                writer.writeElementString("Start",
                        it.getKey().toFormatString());
                writer.writeElementString("End",
                        it.getValue().toFormatString());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        if (destinations != null) {
            writer.writeElementString("Destinations",
                    String.join(";", destinations));
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}