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

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSLimiter extends GXDLMSObject implements IGXDLMSBase {
    private GXDLMSObject monitoredValue;
    private int monitoredAttributeIndex;
    private Object thresholdActive;
    private Object thresholdNormal;
    private Object thresholdEmergency;
    private long minOverThresholdDuration;
    private long minUnderThresholdDuration;
    private GXDLMSEmergencyProfile emergencyProfile;
    private int[] emergencyProfileGroupIDs;
    private boolean emergencyProfileActive;
    private GXDLMSActionItem actionOverThreshold;
    private GXDLMSActionItem actionUnderThreshold;

    /**
     * Constructor.
     */
    public GXDLMSLimiter() {
        super(ObjectType.LIMITER);
        emergencyProfile = new GXDLMSEmergencyProfile();
        actionOverThreshold = new GXDLMSActionItem();
        actionUnderThreshold = new GXDLMSActionItem();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSLimiter(final String ln) {
        super(ObjectType.LIMITER, ln, 0);
        emergencyProfile = new GXDLMSEmergencyProfile();
        actionOverThreshold = new GXDLMSActionItem();
        actionUnderThreshold = new GXDLMSActionItem();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSLimiter(final String ln, final int sn) {
        super(ObjectType.LIMITER, ln, sn);
        emergencyProfile = new GXDLMSEmergencyProfile();
        actionOverThreshold = new GXDLMSActionItem();
        actionUnderThreshold = new GXDLMSActionItem();
    }

    /**
     * @return Defines an attribute of an object to be monitored.
     */
    public final GXDLMSObject getMonitoredValue() {
        return monitoredValue;
    }

    /**
     * @param value
     *            Defines an attribute of an object to be monitored.
     */
    public final void setMonitoredValue(final GXDLMSObject value) {
        monitoredValue = value;
    }

    public final int getmonitoredAttributeIndex() {
        return monitoredAttributeIndex;
    }

    public final void setmonitoredAttributeIndex(final int value) {
        monitoredAttributeIndex = value;
    }

    /**
     * @return Provides the active threshold value to which the attribute
     *         monitored is compared.
     */
    public final Object getThresholdActive() {
        return thresholdActive;
    }

    /**
     * @param value
     *            Provides the active threshold value to which the attribute
     *            monitored is compared.
     */
    public final void setThresholdActive(final Object value) {
        thresholdActive = value;
    }

    /**
     * @return Provides the threshold value to which the attribute monitored is
     *         compared when in normal operation.
     */
    public final Object getThresholdNormal() {
        return thresholdNormal;
    }

    /**
     * @param value
     *            Provides the threshold value to which the attribute monitored
     *            is compared when in normal operation.
     */
    public final void setThresholdNormal(final Object value) {
        thresholdNormal = value;
    }

    /**
     * @return Provides the threshold value to which the attribute monitored is
     *         compared when an emergency profile is active.
     */
    public final Object getThresholdEmergency() {
        return thresholdEmergency;
    }

    /**
     * @param value
     *            Provides the threshold value to which the attribute monitored
     *            is compared when an emergency profile is active.
     */
    public final void setThresholdEmergency(final Object value) {
        thresholdEmergency = value;
    }

    /**
     * @return Defines minimal over threshold duration in seconds required to
     *         execute the over threshold action.
     */
    public final long getMinOverThresholdDuration() {
        return minOverThresholdDuration;
    }

    /**
     * @param value
     *            Defines minimal over threshold duration in seconds required to
     *            execute the over threshold action.
     */
    public final void setMinOverThresholdDuration(final long value) {
        minOverThresholdDuration = value;
    }

    /**
     * @return Defines minimal under threshold duration in seconds required to
     *         execute the under threshold action.
     */
    public final long getMinUnderThresholdDuration() {
        return minUnderThresholdDuration;
    }

    /**
     * @param value
     *            Defines minimal under threshold duration in seconds required
     *            to execute the under threshold action.
     */
    public final void setMinUnderThresholdDuration(final long value) {
        minUnderThresholdDuration = value;
    }

    public final GXDLMSEmergencyProfile getEmergencyProfile() {
        return emergencyProfile;
    }

    public final int[] getEmergencyProfileGroupIDs() {
        return emergencyProfileGroupIDs;
    }

    public final void setEmergencyProfileGroupIDs(final int[] value) {
        emergencyProfileGroupIDs = value;
    }

    /**
     * @return Is Emergency Profile active.
     */
    public final boolean getEmergencyProfileActive() {
        return emergencyProfileActive;
    }

    /**
     * @param value
     *            Is Emergency Profile active.
     */
    public final void setEmergencyProfileActive(final boolean value) {
        emergencyProfileActive = value;
    }

    /**
     * @return Defines the scripts to be executed when the monitored value
     *         crosses the threshold for minimal duration time.
     */
    public final GXDLMSActionItem getActionOverThreshold() {
        return actionOverThreshold;
    }

    /**
     * @param value
     *            Defines the scripts to be executed when the monitored value
     *            crosses the threshold for minimal duration time.
     */
    public final void setActionOverThreshold(final GXDLMSActionItem value) {
        actionOverThreshold = value;
    }

    /**
     * @return Defines the scripts to be executed when the monitored value
     *         crosses the threshold for minimal duration time.
     */
    public final GXDLMSActionItem getActionUnderThreshold() {
        return actionUnderThreshold;
    }

    /**
     * @param value
     *            Defines the scripts to be executed when the monitored value
     *            crosses the threshold for minimal duration time.
     */
    public final void setActionUnderThreshold(final GXDLMSActionItem value) {
        actionUnderThreshold = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), monitoredValue, thresholdActive,
                thresholdNormal, thresholdEmergency,
                new Long(minOverThresholdDuration),
                new Long(minUnderThresholdDuration), emergencyProfile,
                emergencyProfileGroupIDs, new Boolean(emergencyProfileActive),
                new Object[] { actionOverThreshold, actionUnderThreshold } };
    }

    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // MonitoredValue
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }

        // ThresholdActive
        if (canRead(3)) {
            attributes.add(new Integer(3));
        }

        // ThresholdNormal
        if (canRead(4)) {
            attributes.add(new Integer(4));
        }

        // ThresholdEmergency
        if (canRead(5)) {
            attributes.add(new Integer(5));
        }

        // MinOverThresholdDuration
        if (canRead(6)) {
            attributes.add(new Integer(6));
        }

        // MinUnderThresholdDuration
        if (canRead(7)) {
            attributes.add(new Integer(7));
        }

        // EmergencyProfile
        if (canRead(8)) {
            attributes.add(new Integer(8));
        }
        // EmergencyProfileGroup
        if (canRead(9)) {
            attributes.add(new Integer(9));
        }

        // EmergencyProfileActive
        if (canRead(10)) {
            attributes.add(new Integer(10));
        }
        // Actions
        if (canRead(11)) {
            attributes.add(new Integer(11));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 11;
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
            return DataType.STRUCTURE;
        }
        if (index == 3) {
            return super.getDataType(index);
        }
        if (index == 4) {
            return super.getDataType(index);
        }
        if (index == 5) {
            return super.getDataType(index);
        }
        if (index == 6) {
            return DataType.UINT32;
        }
        if (index == 7) {
            return DataType.UINT32;
        }
        if (index == 8) {
            return DataType.STRUCTURE;
        }
        if (index == 9) {
            return DataType.ARRAY;
        }
        if (index == 10) {
            return DataType.BOOLEAN;
        }
        if (index == 11) {
            return DataType.STRUCTURE;
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
        } else if (e.getIndex() == 2) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(3);
            if (monitoredValue == null) {
                GXCommon.setData(data, DataType.INT16, new Integer(0));
                GXCommon.setData(data, DataType.OCTET_STRING,
                        GXCommon.logicalNameToBytes(null));
                GXCommon.setData(data, DataType.UINT8, 0);
            } else {
                GXCommon.setData(data, DataType.INT16,
                        new Integer(monitoredValue.getObjectType().getValue()));
                GXCommon.setData(data, DataType.OCTET_STRING, GXCommon
                        .logicalNameToBytes(monitoredValue.getLogicalName()));
                GXCommon.setData(data, DataType.UINT8, monitoredAttributeIndex);
            }
            return data.array();
        } else if (e.getIndex() == 3) {
            return thresholdActive;
        } else if (e.getIndex() == 4) {
            return thresholdNormal;
        } else if (e.getIndex() == 5) {
            return thresholdEmergency;
        } else if (e.getIndex() == 6) {
            return new Long(minOverThresholdDuration);
        } else if (e.getIndex() == 7) {
            return new Long(minUnderThresholdDuration);
        } else if (e.getIndex() == 8) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(3);
            GXCommon.setData(data, DataType.UINT16,
                    new Integer(emergencyProfile.getID()));
            GXCommon.setData(data, DataType.OCTET_STRING,
                    emergencyProfile.getActivationTime());
            GXCommon.setData(data, DataType.UINT32,
                    new Long(emergencyProfile.getDuration()));
            return data.array();
        } else if (e.getIndex() == 9) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8((byte) DataType.ARRAY.getValue());
            data.setUInt8((byte) emergencyProfileGroupIDs.length);
            for (Object it : emergencyProfileGroupIDs) {
                GXCommon.setData(data, DataType.UINT16, it);
            }
            return data.array();
        } else if (e.getIndex() == 10) {
            return new Boolean(emergencyProfileActive);
        } else if (e.getIndex() == 11) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            GXCommon.setData(data, DataType.OCTET_STRING, GXCommon
                    .logicalNameToBytes(actionOverThreshold.getLogicalName()));
            GXCommon.setData(data, DataType.UINT16,
                    new Integer(actionOverThreshold.getScriptSelector()));
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            GXCommon.setData(data, DataType.OCTET_STRING, GXCommon
                    .logicalNameToBytes(actionUnderThreshold.getLogicalName()));
            GXCommon.setData(data, DataType.UINT16,
                    new Integer(actionUnderThreshold.getScriptSelector()));
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
            ObjectType ot = ObjectType.forValue(
                    ((Number) (((Object[]) e.getValue())[0])).intValue());
            String ln = GXCommon.toLogicalName(((Object[]) e.getValue())[1]);
            monitoredAttributeIndex =
                    ((Number) (((Object[]) e.getValue())[2])).intValue();
            monitoredValue = settings.getObjects().findByLN(ot, ln);
        } else if (e.getIndex() == 3) {
            thresholdActive = e.getValue();
        } else if (e.getIndex() == 4) {
            thresholdNormal = e.getValue();
        } else if (e.getIndex() == 5) {
            thresholdEmergency = e.getValue();
        } else if (e.getIndex() == 6) {
            minOverThresholdDuration = ((Number) e.getValue()).longValue();
        } else if (e.getIndex() == 7) {
            minUnderThresholdDuration = ((Number) e.getValue()).longValue();
        } else if (e.getIndex() == 8) {
            Object[] tmp = (Object[]) e.getValue();
            emergencyProfile.setID(((Number) tmp[0]).intValue());
            emergencyProfile.setActivationTime((GXDateTime) GXDLMSClient
                    .changeType((byte[]) tmp[1], DataType.DATETIME));
            emergencyProfile.setDuration(((Long) tmp[2]).longValue());
        } else if (e.getIndex() == 9) {
            java.util.ArrayList<Integer> list =
                    new java.util.ArrayList<Integer>();
            if (e.getValue() != null) {
                for (Object it : (Object[]) e.getValue()) {
                    list.add(new Integer(((Number) it).intValue()));
                }
            }
            emergencyProfileGroupIDs = GXDLMSObjectHelpers.toIntArray(list);
        } else if (e.getIndex() == 10) {
            emergencyProfileActive = ((Boolean) e.getValue()).booleanValue();
        } else if (e.getIndex() == 11) {
            Object[] tmp = (Object[]) e.getValue();
            Object[] tmp1 = (Object[]) tmp[0];
            Object[] tmp2 = (Object[]) tmp[1];
            actionOverThreshold.setLogicalName(GXCommon.toLogicalName(tmp1[0]));
            actionOverThreshold
                    .setScriptSelector(((Number) (tmp1[1])).intValue());
            actionUnderThreshold
                    .setLogicalName(GXCommon.toLogicalName(tmp2[0]));
            actionUnderThreshold
                    .setScriptSelector(((Number) (tmp2[1])).intValue());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        if (reader.isStartElement("MonitoredValue", true)) {
            ObjectType ot = ObjectType
                    .forValue(reader.readElementContentAsInt("ObjectType"));
            String ln = reader.readElementContentAsString("LN");
            if (ot != ObjectType.NONE && ln != null) {
                monitoredValue = reader.getObjects().findByLN(ot, ln);
                // If item is not serialized yet.
                if (monitoredValue == null) {
                    monitoredValue = GXDLMSClient.createObject(ot);
                    monitoredValue.setLogicalName(ln);
                }
            }
            reader.readEndElement("MonitoredValue");
        }
        thresholdActive =
                reader.readElementContentAsObject("ThresholdActive", null);
        thresholdNormal =
                reader.readElementContentAsObject("ThresholdNormal", null);
        thresholdEmergency =
                reader.readElementContentAsObject("ThresholdEmergency", null);
        minOverThresholdDuration =
                reader.readElementContentAsInt("MinOverThresholdDuration");
        minUnderThresholdDuration =
                reader.readElementContentAsInt("MinUnderThresholdDuration");
        if (reader.isStartElement("EmergencyProfile", true)) {
            emergencyProfile.setID(reader.readElementContentAsInt("ID"));
            emergencyProfile.setActivationTime((GXDateTime) reader
                    .readElementContentAsObject("Time", new GXDateTime()));
            emergencyProfile
                    .setDuration(reader.readElementContentAsInt("Duration"));
            reader.readEndElement("EmergencyProfile");
        }
        List<Integer> list = new ArrayList<Integer>();
        if (reader.isStartElement("EmergencyProfileGroupIDs", true)) {
            while (reader.isStartElement("Value", false)) {
                list.add(reader.readElementContentAsInt("Value"));
            }
            reader.readEndElement("EmergencyProfileGroupIDs");
        }
        emergencyProfileGroupIDs = GXCommon.toIntArray(list);
        emergencyProfileActive = reader.readElementContentAsInt("Active") != 0;
        if (reader.isStartElement("ActionOverThreshold", true)) {
            actionOverThreshold
                    .setLogicalName(reader.readElementContentAsString("LN"));
            actionOverThreshold.setScriptSelector(
                    reader.readElementContentAsInt("ScriptSelector"));
            reader.readEndElement("ActionOverThreshold");
        }
        if (reader.isStartElement("ActionUnderThreshold", true)) {
            actionUnderThreshold
                    .setLogicalName(reader.readElementContentAsString("LN"));
            actionUnderThreshold.setScriptSelector(
                    reader.readElementContentAsInt("ScriptSelector"));
            reader.readEndElement("ActionUnderThreshold");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (monitoredValue != null) {
            writer.writeStartElement("MonitoredValue");
            writer.writeElementString("ObjectType",
                    monitoredValue.getObjectType().ordinal());
            writer.writeElementString("LN", monitoredValue.getLogicalName());
            writer.writeEndElement();
        }
        writer.writeElementObject("ThresholdActive", thresholdActive);
        writer.writeElementObject("ThresholdNormal", thresholdNormal);
        writer.writeElementObject("ThresholdEmergency", thresholdEmergency);
        writer.writeElementString("MinOverThresholdDuration",
                minOverThresholdDuration);
        writer.writeElementString("MinUnderThresholdDuration",
                minUnderThresholdDuration);
        if (emergencyProfile != null) {
            writer.writeStartElement("EmergencyProfile");
            writer.writeElementString("ID", emergencyProfile.getID());
            writer.writeElementObject("Time",
                    emergencyProfile.getActivationTime());
            writer.writeElementString("Duration",
                    emergencyProfile.getDuration());
            writer.writeEndElement();
        }
        if (emergencyProfileGroupIDs != null) {
            writer.writeStartElement("EmergencyProfileGroupIDs");
            for (int it : emergencyProfileGroupIDs) {
                writer.writeElementString("Value", it);
            }
            writer.writeEndElement();
        }
        writer.writeElementString("Active", emergencyProfileActive);

        if (actionOverThreshold != null) {
            writer.writeStartElement("ActionOverThreshold");
            writer.writeElementString("LN",
                    actionOverThreshold.getLogicalName());
            writer.writeElementString("ScriptSelector",
                    actionOverThreshold.getScriptSelector());
            writer.writeEndElement();
        }

        if (actionUnderThreshold != null) {
            writer.writeStartElement("ActionUnderThreshold");
            writer.writeElementString("LN",
                    actionUnderThreshold.getLogicalName());
            writer.writeElementString("ScriptSelector",
                    actionUnderThreshold.getScriptSelector());
            writer.writeEndElement();
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
        // Upload Monitored Value after load.
        if (monitoredValue != null) {
            GXDLMSObject target =
                    reader.getObjects().findByLN(monitoredValue.getObjectType(),
                            monitoredValue.getLogicalName());
            if (target != monitoredValue) {
                monitoredValue = target;
            }
        }
    }
}