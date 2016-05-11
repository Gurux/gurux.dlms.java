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
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSRegisterMonitor extends GXDLMSObject implements IGXDLMSBase {
    private GXDLMSActionSet[] actions;
    private GXDLMSMonitoredValue monitoredValue;
    private Object[] thresholds;

    /**
     * Constructor.
     */
    public GXDLMSRegisterMonitor() {
        super(ObjectType.REGISTER_MONITOR);
        this.setThresholds(new Object[0]);
        this.setMonitoredValue(new GXDLMSMonitoredValue());
        this.setActions(new GXDLMSActionSet[0]);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSRegisterMonitor(final String ln) {
        super(ObjectType.REGISTER_MONITOR, ln, 0);
        this.setThresholds(new Object[0]);
        this.setMonitoredValue(new GXDLMSMonitoredValue());
        this.setActions(new GXDLMSActionSet[0]);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSRegisterMonitor(final String ln, final int sn) {
        super(ObjectType.REGISTER_MONITOR, ln, sn);
        this.setThresholds(new Object[0]);
        this.setMonitoredValue(new GXDLMSMonitoredValue());
        this.setActions(new GXDLMSActionSet[0]);
    }

    public final Object[] getThresholds() {
        return thresholds;
    }

    public final void setThresholds(final Object[] value) {
        thresholds = value;
    }

    public final GXDLMSMonitoredValue getMonitoredValue() {
        return monitoredValue;
    }

    final void setMonitoredValue(final GXDLMSMonitoredValue value) {
        monitoredValue = value;
    }

    public final GXDLMSActionSet[] getActions() {
        return actions;
    }

    public final void setActions(final GXDLMSActionSet[] value) {
        actions = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getThresholds(),
                getMonitoredValue(), getActions() };
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
        // Thresholds
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // MonitoredValue
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // Actions
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
            return super.getDataType(index);
        }
        if (index == 3) {
            return DataType.ARRAY;
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
            return getThresholds();
        }
        if (e.getIndex() == 3) {
            GXByteBuffer stream = new GXByteBuffer();
            stream.setUInt8(DataType.STRUCTURE.getValue());
            stream.setUInt8(3);
            // ClassID
            GXCommon.setData(stream, DataType.UINT16,
                    new Integer(monitoredValue.getObjectType().getValue()));
            // LN.
            GXCommon.setData(stream, DataType.OCTET_STRING,
                    monitoredValue.getLogicalName());
            // Attribute index.
            GXCommon.setData(stream, DataType.INT8,
                    new Integer(monitoredValue.getAttributeIndex()));
            return stream.array();
        }
        if (e.getIndex() == 4) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.STRUCTURE.getValue());
            if (actions == null) {
                bb.setUInt8(0);
            } else {
                bb.setUInt8(actions.length);
                for (GXDLMSActionSet it : actions) {
                    bb.setUInt8((byte) DataType.STRUCTURE.getValue());
                    bb.setUInt8(2);
                    bb.setUInt8((byte) DataType.STRUCTURE.getValue());
                    bb.setUInt8(2);
                    // LN
                    GXCommon.setData(bb, DataType.OCTET_STRING,
                            it.getActionUp().getLogicalName());
                    // ScriptSelector
                    GXCommon.setData(bb, DataType.UINT16,
                            new Integer(it.getActionUp().getScriptSelector()));
                    bb.setUInt8((byte) DataType.STRUCTURE.getValue());
                    bb.setUInt8(2);
                    // LN
                    GXCommon.setData(bb, DataType.OCTET_STRING,
                            it.getActionDown().getLogicalName());
                    // ScriptSelector
                    GXCommon.setData(bb, DataType.UINT16, new Integer(
                            it.getActionDown().getScriptSelector()));
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
            setThresholds((Object[]) e.getValue());
        } else if (e.getIndex() == 3) {
            if (getMonitoredValue() == null) {
                setMonitoredValue(new GXDLMSMonitoredValue());
            }
            getMonitoredValue().setObjectType(ObjectType.forValue(
                    ((Number) ((Object[]) e.getValue())[0]).intValue()));
            getMonitoredValue()
                    .setLogicalName(
                            GXDLMSClient
                                    .changeType(
                                            (byte[]) ((Object[]) e
                                                    .getValue())[1],
                                            DataType.OCTET_STRING)
                                    .toString());
            getMonitoredValue().setAttributeIndex(
                    ((Number) ((Object[]) e.getValue())[2]).intValue());
        } else if (e.getIndex() == 4) {
            setActions(new GXDLMSActionSet[0]);
            if (e.getValue() != null) {
                List<GXDLMSActionSet> items = new ArrayList<GXDLMSActionSet>();
                for (Object as : (Object[]) e.getValue()) {
                    GXDLMSActionSet set = new GXDLMSActionSet();
                    Object[] target = (Object[]) ((Object[]) as)[0];
                    set.getActionUp()
                            .setLogicalName(
                                    GXDLMSClient
                                            .changeType((byte[]) target[0],
                                                    DataType.OCTET_STRING)
                                            .toString());
                    set.getActionUp()
                            .setScriptSelector(((Number) target[1]).intValue());
                    target = (Object[]) ((Object[]) as)[1];
                    set.getActionDown()
                            .setLogicalName(
                                    GXDLMSClient
                                            .changeType((byte[]) target[0],
                                                    DataType.OCTET_STRING)
                                            .toString());
                    set.getActionDown()
                            .setScriptSelector(((Number) target[1]).intValue());
                    items.add(set);
                }
                setActions(items.toArray(new GXDLMSActionSet[items.size()]));
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }
}