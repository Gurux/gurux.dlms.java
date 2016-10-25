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
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSServer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.enums.SortMethod;

public class GXDLMSProfileGeneric extends GXDLMSObject implements IGXDLMSBase {
    private ArrayList<Object[]> buffer = new ArrayList<Object[]>();
    private List<Entry<GXDLMSObject, GXDLMSCaptureObject>> captureObjects;
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
                new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
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
     *            Add new row to Profile Generic data buffer.
     */
    public final void addRow(final Object[] value) {
        buffer.add(value);
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
                new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(item, co));
    }

    /**
     * @return Captured Objects.
     */
    public final List<Entry<GXDLMSObject, GXDLMSCaptureObject>>
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
            attributes.add(new Integer(1));
        }
        // Buffer
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // CaptureObjects
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // CapturePeriod
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        // SortMethod
        if (!isRead(5)) {
            attributes.add(new Integer(5));
        }
        // SortObject
        if (!isRead(6)) {
            attributes.add(new Integer(6));
        }
        // EntriesInUse
        if (!isRead(7)) {
            attributes.add(new Integer(7));
        }
        // ProfileEntries
        if (!isRead(8)) {
            attributes.add(new Integer(8));
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
        for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
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
     * @param columns
     *            Columns to get. Null if not used.
     * @return Buffer rows as byte array.
     */
    private byte[] getData(final Object[] table,
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns) {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8((byte) DataType.ARRAY.getValue());
        GXCommon.setObjectCount(table.length, data);
        DataType[] types = new DataType[captureObjects.size()];
        int pos = -1;
        // CHECKSTYLE:OFF
        for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
            // CHECKSTYLE:ON
            types[++pos] =
                    it.getKey().getDataType(it.getValue().getAttributeIndex());
        }
        DataType tp;
        for (Object row : table) {
            Object[] items = (Object[]) row;
            data.setUInt8(DataType.STRUCTURE.getValue());
            if (columns == null || columns.size() == 0) {
                GXCommon.setObjectCount(items.length, data);
            } else {
                GXCommon.setObjectCount(columns.size(), data);
            }
            pos = -1;
            for (Object value : items) {
                tp = DataType.NONE;
                if (types.length == items.length) {
                    tp = types[++pos];
                } else {
                    ++pos;
                }
                if (columns == null
                        || columns.contains(captureObjects.get(pos))) {
                    if (tp == DataType.NONE) {
                        tp = GXCommon.getValueType(value);
                        if (types.length == items.length) {
                            types[pos] = tp;
                        }
                    }
                    GXCommon.setData(data, tp, value);
                }
            }
        }
        return data.array();

    }

    /**
     * Get selected columns.
     * 
     * @param cols
     *            selected columns.
     * @return Selected columns.
     */
    private List<Entry<GXDLMSObject, GXDLMSCaptureObject>>
            getColumns(final Object[] cols) {
        List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns = null;
        if (cols != null && cols.length != 0) {
            columns = new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
            for (Object it : cols) {
                Object[] tmp = (Object[]) it;
                ObjectType ot =
                        ObjectType.forValue(((Number) tmp[0]).intValue());
                String ln = GXDLMSObject.toLogicalName((byte[]) tmp[1]);
                byte attributeIndex = ((Number) tmp[2]).byteValue();
                int dataIndex = ((Number) tmp[3]).intValue();
                // Find columns and update only them.
                // CHECKSTYLE:OFF
                for (Entry<GXDLMSObject, GXDLMSCaptureObject> c : captureObjects) {
                    // CHECKSTYLE:ON
                    if (c.getKey().getObjectType() == ot
                            && c.getValue()
                                    .getAttributeIndex() == attributeIndex
                            && c.getValue().getDataIndex() == dataIndex
                            && c.getKey().getLogicalName().compareTo(ln) == 0) {
                        columns.add(c);
                        break;
                    }
                }
            }
        } else {
            // Return all rows.
            List<Entry<GXDLMSObject, GXDLMSCaptureObject>> colums =
                    new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
            colums.addAll(captureObjects);
            return colums;
        }
        return columns;
    }

    /**
     * Get selected columns from parameters.
     * 
     * @param selector
     *            Is read by entry or range.
     * @param parameters
     *            Received parameters where columns information is found.
     * @return Selected columns.
     */
    public final List<Entry<GXDLMSObject, GXDLMSCaptureObject>>

            getSelectedColumns(final int selector, final Object parameters) {
        if (selector == 0) {
            // Return all rows.
            List<Entry<GXDLMSObject, GXDLMSCaptureObject>> colums =
                    new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
            colums.addAll(captureObjects);
            return colums;
        } else if (selector == 1) {
            return getColumns((Object[]) ((Object[]) parameters)[3]);
        } else if (selector == 2) {
            Object[] arr = (Object[]) parameters;
            int colStart = 1;
            int colCount = 0;
            if (arr.length > 2) {
                colStart = ((Number) arr[2]).intValue();
            }
            if (arr.length > 3) {
                colCount = ((Number) arr[3]).intValue();
            } else if (colStart != 1) {
                colCount = captureObjects.size();
            }
            if (colStart != 1 || colCount != 0) {
                return captureObjects.subList(colStart - 1,
                        colStart + colCount - 1);
            }
            // Return all rows.
            List<Entry<GXDLMSObject, GXDLMSCaptureObject>> colums =
                    new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
            colums.addAll(captureObjects);
            return colums;
        } else {
            throw new IllegalArgumentException("Invalid selector.");
        }
    }

    final byte[] getProfileGenericData(final int selector,
            final Object parameters) {
        List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns = null;
        // If all data is read.
        if (selector == 0 || parameters == null) {
            return getData(getBuffer(), columns);
        }
        Object[] arr = (Object[]) parameters;
        columns = getSelectedColumns(selector, arr);
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
                Object tmp = ((Object[]) row)[this.getSortObjectDataIndex()];
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
            if (start == 0) {
                start = 1;
            }
            int count = ((Number) arr[1]).intValue();
            if (count == 0) {
                count = getBuffer().length;
            }
            if (start + count > getBuffer().length + 1) {
                count = getBuffer().length;
            }
            // Starting index is 1.
            for (int pos = 0; pos < count; ++pos) {
                if (pos + start - 1 == getBuffer().length) {
                    break;
                }
                table.add((Object[]) getBuffer()[start + pos - 1]);
            }
        } else {
            throw new IllegalArgumentException("Invalid selector.");
        }
        return getData(table.toArray(), columns);
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
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return getLogicalName();
        }
        if (e.getIndex() == 2) {
            return getProfileGenericData(e.getSelector(), e.getParameters());
        }
        if (e.getIndex() == 3) {
            return getColumns();
        }
        if (e.getIndex() == 4) {
            return getCapturePeriod();
        }
        if (e.getIndex() == 5) {
            return getSortMethod().getValue();
        }
        if (e.getIndex() == 6) {
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
        if (e.getIndex() == 7) {
            return getEntriesInUse();
        }
        if (e.getIndex() == 8) {
            return getProfileEntries();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    /**
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            super.setValue(settings, e);
        } else if (e.getIndex() == 2) {
            setBuffer(e);
        } else if (e.getIndex() == 3) {
            captureObjects.clear();
            buffer.clear();
            entriesInUse = buffer.size();
            if (e.getValue() != null) {
                for (Object it : (Object[]) e.getValue()) {
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
        } else if (e.getIndex() == 4) {
            if (e.getValue() == null) {
                capturePeriod = 0;
            } else {
                capturePeriod = ((Number) e.getValue()).intValue();
            }

        } else if (e.getIndex() == 5) {
            if (e.getValue() == null) {
                sortMethod = SortMethod.FIFO;
            } else {
                sortMethod =
                        SortMethod.forValue(((Number) e.getValue()).intValue());
            }

        } else if (e.getIndex() == 6) {
            if (e.getValue() == null) {
                sortObject = null;
            } else {
                Object[] tmp = (Object[]) e.getValue();
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
        } else if (e.getIndex() == 7) {
            if (e.getValue() == null) {
                entriesInUse = 0;
            } else {
                entriesInUse = ((Number) e.getValue()).intValue();
            }
        } else if (e.getIndex() == 8) {
            if (e.getValue() == null) {
                profileEntries = 0;
            } else {
                profileEntries = ((Number) e.getValue()).intValue();
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    /**
     * Update buffer.
     * 
     * @param value
     *            Received data.
     */
    @SuppressWarnings("unchecked")
    private void setBuffer(final ValueEventArgs e) {

        List<Entry<GXDLMSObject, GXDLMSCaptureObject>> cols = null;
        if (e.getParameters() instanceof List) {
            cols = (List<Entry<GXDLMSObject, GXDLMSCaptureObject>>) e
                    .getParameters();
        }

        if (cols == null) {
            cols = captureObjects;
        }
        if (cols == null || cols.size() == 0) {
            throw new RuntimeException("Read capture objects first.");
        }
        buffer.clear();
        if (e.getValue() != null) {
            java.util.Calendar lastDate = java.util.Calendar.getInstance();
            DataType[] types = new DataType[cols.size()];
            int colIndex = -1;
            // CHECKSTYLE:OFF
            for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : cols) {
                // CHECKSTYLE:ON
                types[++colIndex] = it.getKey()
                        .getUIDataType(it.getValue().getAttributeIndex());
            }
            for (Object it : (Object[]) e.getValue()) {
                Object[] row = (Object[]) it;
                if (row.length != cols.size()) {
                    throw new RuntimeException(
                            "Number of columns do not match.");
                }
                for (colIndex = 0; colIndex < row.length; ++colIndex) {
                    Object data = row[colIndex];
                    DataType type = types[colIndex];
                    if (type != DataType.NONE && type != null
                            && data instanceof byte[]) {
                        data = GXDLMSClient.changeType((byte[]) data, type);
                        if (data instanceof GXDateTime) {
                            GXDateTime dt = (GXDateTime) data;
                            lastDate.setTime(dt.getValue());
                        }
                        row[colIndex] = data;
                    } else if (type == DataType.DATETIME && data == null
                            && capturePeriod != 0) {
                        if (lastDate.getTimeInMillis() == 0
                                && !buffer.isEmpty()) {
                            lastDate.setTime(((GXDateTime) buffer
                                    .get(buffer.size() - 1)[colIndex])
                                            .getValue());
                        }
                        if (lastDate.getTimeInMillis() != 0) {
                            lastDate.add(java.util.Calendar.SECOND,
                                    capturePeriod);
                            row[colIndex] = new GXDateTime(lastDate.getTime());
                        }
                    }
                    Entry<GXDLMSObject, GXDLMSCaptureObject> item =
                            cols.get(colIndex);
                    if (item.getKey() instanceof GXDLMSRegister
                            && item.getValue().getAttributeIndex() == 2) {
                        double scaler =
                                ((GXDLMSRegister) item.getKey()).getScaler();
                        if (scaler != 1 && data != null) {
                            try {
                                data = ((Number) data).doubleValue() * scaler;
                                row[colIndex] = data;
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
            for (Entry<GXDLMSObject, GXDLMSCaptureObject> obj : captureObjects) {
                // CHECKSTYLE:ON
                ValueEventArgs e =
                        new ValueEventArgs(server.getSettings(), obj.getKey(),
                                obj.getValue().getAttributeIndex(), 0, null);
                server.read(new ValueEventArgs[] { e });
                if (e.getHandled()) {
                    values[++pos] = e.getValue();
                } else {
                    values[++pos] = obj.getKey().getValue(null, e);
                }
            }
            synchronized (this) {
                // Remove first items if buffer is full.
                if (getProfileEntries() == getBuffer().length) {
                    buffer.remove(0);
                }
                buffer.add(values);
                entriesInUse = buffer.size();
            }
        }
    }
}