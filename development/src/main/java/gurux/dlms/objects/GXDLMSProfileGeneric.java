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

import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSServer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.enums.SortMethod;

public class GXDLMSProfileGeneric extends GXDLMSObject implements IGXDLMSBase {
    private ArrayList<Object[]> buffer = new ArrayList<Object[]>();
    private List<SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>> captureObjects;
    private int capturePeriod;
    private SortMethod sortMethod;
    private GXDLMSObject sortObject;
    private int sortObjectAttributeIndex;
    private int sortObjectDataIndex;
    private int entriesInUse;
    private int profileEntries;

    /**
     * Constructor.
     */
    public GXDLMSProfileGeneric() {
        this(null, (short) 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSProfileGeneric(final String ln) {
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
    public GXDLMSProfileGeneric(final String ln, final int sn) {
        super(ObjectType.PROFILE_GENERIC, ln, sn);
        captureObjects =
                new ArrayList<SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>>();
    }

    /**
     * @return Data of profile generic.
     */
    public final Object[] getBuffer() {
        return buffer.toArray();
    }

    /**
     * @param value
     *            Data of profile generic.
     */
    public final void setBuffer(final Object[][] value) {
        buffer.clear();
        buffer.addAll(Arrays.asList(value));
        entriesInUse = buffer.size();
    }

    /**
     * @param value
     *            Data of profile generic.
     */
    public final void addBuffer(final Object[][] value) {
        buffer.addAll(Arrays.asList(value));
        entriesInUse = buffer.size();
    }

    /**
     * @param value
     *            Data of profile generic.
     */
    public final void addBuffer(final List<Object[]> value) {
        buffer.addAll(value);
        entriesInUse = buffer.size();
    }

    /**
     * Clear profile generic buffer.
     */
    public final void clearBuffer() {
        buffer.clear();
        entriesInUse = 0;
    }

    /*
     * Add new capture object (column) to the profile generic.
     */
    public final void addCaptureObject(final GXDLMSObject item,
            final int attributeIndex, final int dataIndex) {
        if (item == null) {
            throw new RuntimeException("Invalid Object");
        }
        if (attributeIndex < 1) {
            throw new RuntimeException("Invalid attribute index");
        }
        if (dataIndex < 0) {
            throw new RuntimeException("Invalid data index");
        }
        GXDLMSCaptureObject co =
                new GXDLMSCaptureObject(attributeIndex, dataIndex);
        captureObjects.add(
                new SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(item, co));
    }

    /**
     * @return Captured Objects.
     */
    public final List<SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>>
            getCaptureObjects() {
        return captureObjects;
    }

    /**
     * @return How often values are captured.
     */
    public final int getCapturePeriod() {
        return capturePeriod;
    }

    /**
     * @param value
     *            How often values are captured.
     */
    public final void setCapturePeriod(final int value) {
        capturePeriod = value;
    }

    /**
     * @return How columns are sorted.
     */
    public final SortMethod getSortMethod() {
        return sortMethod;
    }

    /**
     * @param value
     *            How columns are sorted.
     */
    public final void setSortMethod(final SortMethod value) {
        sortMethod = value;
    }

    /**
     * @return Column that is used for sorting.
     */
    public final GXDLMSObject getSortObject() {
        return sortObject;
    }

    /**
     * @param value
     *            Column that is used for sorting.
     */
    public final void setSortObject(final GXDLMSObject value) {
        sortObject = value;
    }

    /**
     * @return Attribute index of sort object.
     */
    public final int getSortObjectAttributeIndex() {
        return sortObjectAttributeIndex;
    }

    /**
     * @param value
     *            Attribute index of sort object.
     */
    public final void setSortObjectAttributeIndex(final int value) {
        sortObjectAttributeIndex = value;
    }

    /**
     * @return Data index of sort object.
     */
    public final int getSortObjectDataIndex() {
        return sortObjectDataIndex;
    }

    /**
     * @param value
     *            Data index of sort object.
     */
    public final void setSortObject(final int value) {
        sortObjectDataIndex = value;
    }

    /**
     * @return Entries (rows) in Use.
     */
    public final int getEntriesInUse() {
        return entriesInUse;
    }

    /**
     * @return Entries (rows) in Use.
     */
    public final int setEntriesInUse() {
        return entriesInUse;
    }

    /**
     * @return Maximum Entries (rows) count.
     */
    public final int getProfileEntries() {
        return profileEntries;
    }

    /**
     * @param value
     *            Maximum Entries (rows) count.
     */
    public final void setProfileEntries(final int value) {
        profileEntries = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getBuffer(),
                getCaptureObjects(), getCapturePeriod(), getSortMethod(),
                getSortObject(), getEntriesInUse(), getProfileEntries() };
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
            attributes.add(1);
        }
        // Buffer
        if (!isRead(2)) {
            attributes.add(2);
        }
        // CaptureObjects
        if (!isRead(3)) {
            attributes.add(3);
        }
        // CapturePeriod
        if (!isRead(4)) {
            attributes.add(4);
        }
        // SortMethod
        if (!isRead(5)) {
            attributes.add(5);
        }
        // SortObject
        if (!isRead(6)) {
            attributes.add(6);
        }
        // EntriesInUse
        if (!isRead(7)) {
            attributes.add(7);
        }
        // ProfileEntries
        if (!isRead(8)) {
            attributes.add(8);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 8;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 2;
    }

    /**
     * @return Returns captured columns.
     */
    private byte[] getColumns() {
        int cnt = captureObjects.size();
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.ARRAY.getValue());
        // Add count
        GXCommon.setObjectCount(cnt, data);
        // CHECKSTYLE:OFF
        for (SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
            // CHECKSTYLE:ON
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(4); // Count
            // ClassID
            GXCommon.setData(data, DataType.UINT16,
                    it.getKey().getObjectType().getValue());
            // LN
            GXCommon.setData(data, DataType.OCTET_STRING,
                    it.getKey().getLogicalName());
            // Attribute Index
            GXCommon.setData(data, DataType.INT8,
                    it.getValue().getAttributeIndex());
            // Data Index
            GXCommon.setData(data, DataType.UINT16,
                    it.getValue().getDataIndex());
        }
        return data.array();
    }

    /**
     * @param table
     *            Table where rows are get.
     * @return Buffer rows as byte array.
     */
    private byte[] getData(final Object[] table) {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8((byte) DataType.ARRAY.getValue());
        GXCommon.setObjectCount(Array.getLength(table), data);
        DataType[] types = new DataType[captureObjects.size()];
        int pos = -1;
        // CHECKSTYLE:OFF
        for (SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
            // CHECKSTYLE:ON
            types[++pos] =
                    it.getKey().getDataType(it.getValue().getAttributeIndex());
        }
        for (Object row : table) {
            Object[] items = (Object[]) row;
            data.setUInt8(DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(Array.getLength(items), data);
            pos = -1;
            for (Object value : items) {
                DataType tp = types[++pos];
                if (tp == DataType.NONE) {
                    tp = GXCommon.getValueType(value);
                    types[pos] = tp;
                }
                GXCommon.setData(data, tp, value);
            }
        }
        return data.array();

    }

    final byte[] getProfileGenericData(final int selector,
            final Object parameters) {
        // If all data is read.
        if (selector == 0 || parameters == null) {
            return getData(getBuffer());
        }

        Object[] arr = (Object[]) parameters;
        ArrayList<Object[]> table = new ArrayList<Object[]>();
        // Read by range
        if (selector == 1) {
            GXDataInfo info = new GXDataInfo();
            info.setType(DataType.DATETIME);
            java.util.Date start = ((GXDateTime) GXCommon
                    .getData(new GXByteBuffer((byte[]) arr[1]), info))
                            .getValue();
            info.clear();
            info.setType(DataType.DATETIME);
            java.util.Date end = ((GXDateTime) GXCommon
                    .getData(new GXByteBuffer((byte[]) arr[2]), info))
                            .getValue();
            for (Object row : getBuffer()) {
                java.util.Date tm;
                Object tmp = ((Object[]) row)[0];
                if (tmp instanceof GXDateTime) {
                    tm = ((GXDateTime) tmp).getValue();
                } else {
                    tm = (java.util.Date) tmp;
                }
                if (tm.compareTo(start) >= 0 && tm.compareTo(end) <= 0) {
                    table.add((Object[]) row);
                }
            }
        } else if (selector == 2) {
            // Read by entry.
            int start = ((Number) arr[0]).intValue();
            int count = ((Number) arr[1]).intValue();
            for (int pos = 0; pos < count; ++pos) {
                if (pos + start == getBuffer().length) {
                    break;
                }
                table.add((Object[]) getBuffer()[start + pos]);
            }
        } else {
            throw new IllegalArgumentException("Invalid selector.");
        }
        return getData(table.toArray());
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ARRAY;
        }
        if (index == 3) {
            return DataType.ARRAY;
        }
        if (index == 4) {
            return DataType.INT8;
        }
        if (index == 5) {
            return DataType.INT8;
        }
        if (index == 6) {
            return DataType.ARRAY;
        }
        if (index == 7) {
            return DataType.UINT32;
        }
        if (index == 8) {
            return DataType.UINT32;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings, final int index,
            final int selector, final Object parameters) {
        if (index == 1) {
            return getLogicalName();
        }
        if (index == 2) {
            return getProfileGenericData(selector, parameters);
        }
        if (index == 3) {
            return getColumns();
        }
        if (index == 4) {
            return getCapturePeriod();
        }
        if (index == 5) {
            return getSortMethod().getValue();
        }
        if (index == 6) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8((byte) DataType.STRUCTURE.getValue());
            data.setUInt8((byte) 4); // Count
            if (sortObject == null) {
                // ClassID
                GXCommon.setData(data, DataType.UINT16, 0);
                // LN
                GXCommon.setData(data, DataType.OCTET_STRING, new byte[6]);
                // Selected Attribute Index
                GXCommon.setData(data, DataType.INT8, 0);
                // Selected Data Index
                GXCommon.setData(data, DataType.UINT16, 0);
            } else {
                // ClassID
                GXCommon.setData(data, DataType.UINT16,
                        sortObject.getObjectType().getValue());
                // LN
                GXCommon.setData(data, DataType.OCTET_STRING,
                        sortObject.getLogicalName());
                // Attribute Index
                GXCommon.setData(data, DataType.INT8, sortObjectAttributeIndex);
                // Data Index
                GXCommon.setData(data, DataType.UINT16, sortObjectDataIndex);
            }
            return data.array();
        }
        if (index == 7) {
            return getEntriesInUse();
        }
        if (index == 8) {
            return getProfileEntries();
        }
        throw new IllegalArgumentException(
                "GetValue failed. Invalid attribute index.");
    }

    /**
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final int index,
            final Object value) {
        if (index == 1) {
            super.setValue(settings, index, value);
        } else if (index == 2) {
            setBuffer(value);
        } else if (index == 3) {
            captureObjects.clear();
            buffer.clear();
            entriesInUse = buffer.size();
            if (value != null) {
                for (Object it : (Object[]) value) {
                    Object[] tmp = (Object[]) it;
                    if (tmp.length != 4) {
                        throw new GXDLMSException("Invalid structure format.");
                    }
                    ObjectType type =
                            ObjectType.forValue(((Number) tmp[0]).intValue());
                    String ln = GXDLMSObject.toLogicalName((byte[]) tmp[1]);
                    GXDLMSObject obj = null;
                    if (settings != null && settings.getObjects() != null) {
                        obj = settings.getObjects().findByLN(type, ln);
                    }
                    if (obj == null) {
                        obj = gurux.dlms.GXDLMSClient.createObject(type);
                        obj.setLogicalName(ln);
                    }
                    addCaptureObject(obj, ((Number) tmp[2]).intValue(),
                            ((Number) tmp[3]).intValue());
                }
            }
        } else if (index == 4) {
            if (value == null) {
                capturePeriod = 0;
            } else {
                capturePeriod = ((Number) value).intValue();
            }

        } else if (index == 5) {
            if (value == null) {
                sortMethod = SortMethod.FIFO;
            } else {
                sortMethod = SortMethod.forValue(((Number) value).intValue());
            }

        } else if (index == 6) {
            if (value == null) {
                sortObject = null;
            } else {
                Object[] tmp = (Object[]) value;
                if (tmp.length != 4) {
                    throw new IllegalArgumentException(
                            "Invalid structure format.");
                }
                ObjectType type =
                        ObjectType.forValue(((Number) tmp[0]).intValue());
                String ln = GXDLMSObject.toLogicalName((byte[]) tmp[1]);
                int attributeIndex = ((Number) tmp[2]).intValue();
                int dataIndex = ((Number) tmp[3]).intValue();
                sortObject = settings.getObjects().findByLN(type, ln);
                if (sortObject == null) {
                    sortObject = gurux.dlms.GXDLMSClient.createObject(type);
                    sortObject.setLogicalName(ln);
                }
                sortObjectAttributeIndex = attributeIndex;
                sortObjectDataIndex = dataIndex;
            }
        } else if (index == 7) {
            if (value == null) {
                entriesInUse = 0;
            } else {
                entriesInUse = ((Number) value).intValue();
            }
        } else if (index == 8) {
            if (value == null) {
                profileEntries = 0;
            } else {
                profileEntries = ((Number) value).intValue();
            }
        } else {
            throw new IllegalArgumentException(
                    "SetValue failed. Invalid attribute index.");
        }
    }

    /**
     * Update buffer.
     * 
     * @param value
     *            Received data.
     */
    private void setBuffer(final Object value) {
        if (captureObjects == null || captureObjects.size() == 0) {
            throw new RuntimeException("Read capture objects first.");
        }
        buffer.clear();
        if (value != null) {
            java.util.Calendar lastDate = java.util.Calendar.getInstance();
            // java.util.Date lastDate = null;
            DataType[] types = new DataType[captureObjects.size()];
            int pos = -1;
            // CHECKSTYLE:OFF
            for (SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
                // CHECKSTYLE:ON
                types[++pos] = it.getKey()
                        .getUIDataType(it.getValue().getAttributeIndex());
            }
            for (Object row : (Object[]) value) {
                if (Array.getLength(row) != captureObjects.size()) {
                    throw new RuntimeException(
                            "Number of columns do not match.");
                }
                for (int a = 0; a < Array.getLength(row); ++a) {
                    Object data = Array.get(row, a);
                    DataType type = types[a];
                    if (type != DataType.NONE && type != null
                            && data instanceof byte[]) {
                        data = GXDLMSClient.changeType((byte[]) data, type);
                        if (data instanceof GXDateTime) {
                            GXDateTime dt = (GXDateTime) data;
                            lastDate.setTime(dt.getValue());
                        }
                        Array.set(row, a, data);
                    } else if (type == DataType.DATETIME && data == null
                            && capturePeriod != 0) {
                        if (lastDate.getTimeInMillis() == 0
                                && !buffer.isEmpty()) {
                            lastDate.setTime(((GXDateTime) buffer
                                    .get(buffer.size() - 1)[pos]).getValue());
                        }
                        if (lastDate.getTimeInMillis() != 0) {
                            lastDate.add(java.util.Calendar.SECOND,
                                    capturePeriod);
                            Array.set(row, a,
                                    new GXDateTime(lastDate.getTime()));
                        }
                    }
                    SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> item =
                            captureObjects.get(pos);
                    if (item.getKey() instanceof GXDLMSRegister
                            && item.getValue().getAttributeIndex() == 2) {
                        double scaler =
                                ((GXDLMSRegister) item.getKey()).getScaler();
                        if (scaler != 1) {
                            try {
                                data = ((Number) data).doubleValue() * scaler;
                                Array.set(row, a, data);
                            } catch (Exception ex) {
                                System.out.println("Scalar failed for: "
                                        + item.getKey().getLogicalName());
                                // Skip error
                            }
                        }
                    }
                }
                buffer.add((Object[]) row);
            }
            entriesInUse = buffer.size();
        }
    }

    /**
     * Clears the buffer.
     */
    public final void reset() {
        synchronized (this) {
            buffer.clear();
            entriesInUse = 0;
        }
    }

    /*
     * Copies the values of the objects to capture into the buffer by reading
     * capture objects.
     */
    public final void capture(final GXDLMSServer server) {
        synchronized (this) {
            Object[] values = new Object[captureObjects.size()];
            int pos = -1;
            // CHECKSTYLE:OFF
            for (SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> obj : captureObjects) {
                // CHECKSTYLE:ON
                ValueEventArgs e = new ValueEventArgs(obj.getKey(),
                        obj.getValue().getAttributeIndex(), 0, null);
                server.read(e);
                if (e.getHandled()) {
                    values[++pos] = e.getValue();
                } else {
                    values[++pos] = obj.getKey().getValue(null,
                            obj.getValue().getAttributeIndex() - 1, 0, null);
                }
            }
            synchronized (this) {
                // Remove first items if buffer is full.
                if (getProfileEntries() == Array.getLength(getBuffer())) {
                    buffer.remove(0);
                }
                buffer.add(values);
                entriesInUse = buffer.size();
            }
        }
    }
}