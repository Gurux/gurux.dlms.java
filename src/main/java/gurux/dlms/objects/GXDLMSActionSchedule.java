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
import java.util.Calendar;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.SingleActionScheduleType;

public class GXDLMSActionSchedule extends GXDLMSObject implements IGXDLMSBase {
    private String executedScriptLogicalName;
    private int executedScriptSelector;
    private SingleActionScheduleType type;
    private GXDateTime[] executionTime;

    /**
     * Constructor.
     */
    public GXDLMSActionSchedule() {
        super(ObjectType.ACTION_SCHEDULE);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSActionSchedule(final String ln) {
        super(ObjectType.ACTION_SCHEDULE, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSActionSchedule(final String ln, final int sn) {
        super(ObjectType.ACTION_SCHEDULE, ln, sn);
    }

    public final String getExecutedScriptLogicalName() {
        return executedScriptLogicalName;
    }

    public final void setExecutedScriptLogicalName(final String value) {
        executedScriptLogicalName = value;
    }

    public final int getExecutedScriptSelector() {
        return executedScriptSelector;
    }

    public final void setExecutedScriptSelector(final int value) {
        executedScriptSelector = value;
    }

    public final SingleActionScheduleType getType() {
        return type;
    }

    public final void setType(final SingleActionScheduleType value) {
        type = value;
    }

    public final GXDateTime[] getExecutionTime() {
        return executionTime;
    }

    public final void setExecutionTime(final GXDateTime[] value) {
        executionTime = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(),
                executedScriptLogicalName + " " + executedScriptSelector,
                getType(), getExecutionTime() };
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
        // ExecutedScriptLogicalName is static and read only once.
        if (!isRead(2)) {
            attributes.add(2);
        }
        // Type is static and read only once.
        if (!isRead(3)) {
            attributes.add(3);
        }
        // ExecutionTime is static and read only once.
        if (!isRead(4)) {
            attributes.add(4);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 4;
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
            return DataType.ARRAY;
        }
        if (index == 3) {
            return DataType.ENUM;
        }
        if (index == 4) {
            return DataType.ARRAY;
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
            GXByteBuffer stream = new GXByteBuffer();
            stream.setUInt8(DataType.STRUCTURE.getValue());
            stream.setUInt8(2);
            GXCommon.setData(stream, DataType.OCTET_STRING,
                    GXCommon.getBytes(executedScriptLogicalName));
            GXCommon.setData(stream, DataType.UINT16, executedScriptSelector);
            return stream.array();
        }
        if (index == 3) {
            return this.getType().getValue();
        }
        if (index == 4) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (getExecutionTime() == null) {
                GXCommon.setObjectCount(0, bb);
            } else {
                GXCommon.setObjectCount(getExecutionTime().length, bb);
                for (GXDateTime it : getExecutionTime()) {
                    bb.setUInt8(DataType.STRUCTURE.getValue());
                    bb.setUInt8(2); // Count
                    GXCommon.setData(bb, DataType.TIME, it.getValue()); // Time
                    GXCommon.setData(bb, DataType.DATE, it.getValue()); // Date
                }
            }
            return bb.array();
        }
        throw new IllegalArgumentException(
                "GetValue failed. Invalid attribute index.");
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final int index,
            final Object value) {
        if (index == 1) {
            super.setValue(settings, index, value);
        } else if (index == 2) {
            setExecutedScriptLogicalName(
                    GXDLMSClient.changeType((byte[]) Array.get(value, 0),
                            DataType.OCTET_STRING).toString());
            setExecutedScriptSelector(
                    ((Number) Array.get(value, 1)).intValue());
        } else if (index == 3) {
            setType(SingleActionScheduleType
                    .forValue(((Number) value).intValue()));
        } else if (index == 4) {
            setExecutionTime(null);
            if (value != null) {
                java.util.ArrayList<GXDateTime> items =
                        new java.util.ArrayList<GXDateTime>();
                for (Object it : (Object[]) value) {
                    GXDateTime dt = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) Array.get(it, 0), DataType.TIME);
                    GXDateTime dt2 = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) Array.get(it, 1), DataType.DATE);
                    java.util.Calendar tm = Calendar.getInstance();
                    tm.setTime(dt.getValue());
                    java.util.Calendar date = Calendar.getInstance();
                    date.setTime(dt2.getValue());
                    tm.set(java.util.Calendar.YEAR,
                            date.get(java.util.Calendar.YEAR) - 1);
                    tm.set(java.util.Calendar.MONTH,
                            date.get(java.util.Calendar.MONTH));
                    tm.set(java.util.Calendar.DAY_OF_MONTH,
                            date.get(java.util.Calendar.DAY_OF_MONTH) - 1);
                    java.util.Set<DateTimeSkips> skip = dt.getSkip();
                    skip.addAll(dt2.getSkip());
                    dt.setSkip(skip);
                    dt.setValue(tm.getTime());
                    items.add(dt);
                }
                setExecutionTime(items.toArray(new GXDateTime[items.size()]));
            }
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}