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

import java.util.Calendar;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDate;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.ErrorCode;
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
            attributes.add(new Integer(1));
        }
        // ExecutedScriptLogicalName is static and read only once.
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // Type is static and read only once.
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // ExecutionTime is static and read only once.
        if (!isRead(4)) {
            attributes.add(new Integer(4));
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
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return getLogicalName();
        }
        if (e.getIndex() == 2) {
            GXByteBuffer stream = new GXByteBuffer();
            stream.setUInt8(DataType.STRUCTURE.getValue());
            stream.setUInt8(2);
            GXCommon.setData(stream, DataType.OCTET_STRING,
                    executedScriptLogicalName);
            GXCommon.setData(stream, DataType.UINT16,
                    new Integer(executedScriptSelector));
            return stream.array();
        }
        if (e.getIndex() == 3) {
            return new Integer(this.getType().getValue());
        }
        if (e.getIndex() == 4) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (getExecutionTime() == null) {
                GXCommon.setObjectCount(0, bb);
            } else {
                GXCommon.setObjectCount(getExecutionTime().length, bb);
                for (GXDateTime it : getExecutionTime()) {
                    bb.setUInt8(DataType.STRUCTURE.getValue());
                    bb.setUInt8(2); // Count
                    GXCommon.setData(bb, DataType.OCTET_STRING, new GXTime(it)); // Time
                    GXCommon.setData(bb, DataType.OCTET_STRING, new GXDate(it)); // Date
                }
            }
            return bb.array();
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
            super.setValue(settings, e);
        } else if (e.getIndex() == 2) {
            setExecutedScriptLogicalName(GXDLMSClient
                    .changeType((byte[]) ((Object[]) e.getValue())[0],
                            DataType.OCTET_STRING)
                    .toString());
            setExecutedScriptSelector(
                    ((Number) ((Object[]) e.getValue())[1]).intValue());
        } else if (e.getIndex() == 3) {
            setType(SingleActionScheduleType
                    .forValue(((Number) e.getValue()).intValue()));
        } else if (e.getIndex() == 4) {
            setExecutionTime(null);
            if (e.getValue() != null) {
                java.util.ArrayList<GXDateTime> items =
                        new java.util.ArrayList<GXDateTime>();
                for (Object it : (Object[]) e.getValue()) {
                    GXDateTime dt = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) ((Object[]) it)[0], DataType.TIME);
                    GXDateTime dt2 = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) ((Object[]) it)[1], DataType.DATE);
                    java.util.Calendar tm = Calendar.getInstance();
                    tm.setTime(dt.getCalendar().getTime());
                    java.util.Calendar date = Calendar.getInstance();
                    date.setTime(dt2.getCalendar().getTime());
                    tm.set(java.util.Calendar.YEAR,
                            date.get(java.util.Calendar.YEAR) - 1);
                    tm.set(java.util.Calendar.MONTH,
                            date.get(java.util.Calendar.MONTH));
                    tm.set(java.util.Calendar.DAY_OF_MONTH,
                            date.get(java.util.Calendar.DAY_OF_MONTH) - 1);
                    java.util.Set<DateTimeSkips> skip = dt.getSkip();
                    skip.addAll(dt2.getSkip());
                    dt.setSkip(skip);
                    dt.setCalendar(tm);
                    items.add(dt);
                }
                setExecutionTime(items.toArray(new GXDateTime[items.size()]));
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }
}