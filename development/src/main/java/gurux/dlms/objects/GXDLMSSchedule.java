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

import java.util.List;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSSchedule
 */
public class GXDLMSSchedule extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Specifies the scripts to be executed at given times.
     */
    private List<GXDLMSScheduleEntry> entries;

    /**
     * Constructor.
     */
    public GXDLMSSchedule() {
        super(ObjectType.SCHEDULE);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSchedule(final String ln) {
        super(ObjectType.SCHEDULE, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSSchedule(final String ln, final int sn) {
        super(ObjectType.SCHEDULE, ln, sn);
    }

    /**
     * Specifies the scripts to be executed at given times.
     * 
     * @return List of executed schedule entries.
     */
    public final List<GXDLMSScheduleEntry> getEntries() {
        return entries;
    }

    /**
     * Specifies the scripts to be executed at given times.
     * 
     * @param value
     *            List of executed schedule entries.
     */
    public final void setEntries(final List<GXDLMSScheduleEntry> value) {
        entries = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), entries };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead(final boolean all) {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null
                || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // Entries
        if (all || !isRead(2)) {
            attributes.add(2);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 2;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 3;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
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
        // TODO:
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
            entries.clear();
            List<?> arr = (List<?>) e.getValue();
            for (Object it : arr) {
                GXDLMSScheduleEntry item = new GXDLMSScheduleEntry();
                List<?> tmp = (List<?>) it;
                item.setIndex(((Number) tmp.get(0)).intValue());
                item.setEnable(((Boolean) tmp.get(1)).booleanValue());
                item.setLogicalName(GXDLMSClient
                        .changeType((byte[]) tmp.get(2), DataType.OCTET_STRING)
                        .toString());
                item.setScriptSelector(((Number) tmp.get(3)).intValue());
                item.setSwitchTime((GXDateTime) GXDLMSClient
                        .changeType((byte[]) tmp.get(4), DataType.DATETIME));
                item.setValidityWindow(((Number) tmp.get(5)).intValue());
                item.setExecWeekdays((String) tmp.get(6));
                item.setExecSpecDays((String) tmp.get(7));
                item.setBeginDate((GXDateTime) GXDLMSClient
                        .changeType((byte[]) tmp.get(8), DataType.DATETIME));
                item.setEndDate((GXDateTime) GXDLMSClient
                        .changeType((byte[]) tmp.get(9), DataType.DATETIME));
                entries.add(item);
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        entries.clear();
        if (reader.isStartElement("Entries", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSScheduleEntry it = new GXDLMSScheduleEntry();
                it.setIndex((byte) reader.readElementContentAsInt("Index"));
                it.setEnable(reader.readElementContentAsInt("Enable") != 0);
                it.setLogicalName(
                        reader.readElementContentAsString("LogicalName"));
                it.setScriptSelector((byte) reader
                        .readElementContentAsInt("ScriptSelector"));
                it.setSwitchTime(
                        reader.readElementContentAsDateTime("SwitchTime"));
                it.setValidityWindow((byte) reader
                        .readElementContentAsInt("ValidityWindow"));
                it.setExecWeekdays(
                        reader.readElementContentAsString("ExecWeekdays"));
                it.setExecSpecDays(
                        reader.readElementContentAsString("ExecSpecDays"));
                it.setBeginDate(
                        reader.readElementContentAsDateTime("BeginDate"));
                it.setEndDate(reader.readElementContentAsDateTime("EndDate"));
                entries.add(it);
            }
            reader.readEndElement("Entries");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (entries != null) {
            writer.writeStartElement("Entries");
            for (GXDLMSScheduleEntry it : entries) {
                writer.writeStartElement("Item");
                writer.writeElementString("Index", it.getIndex());
                writer.writeElementString("Enable", it.getEnable());
                writer.writeElementString("LogicalName", it.getLogicalName());
                writer.writeElementString("ScriptSelector",
                        it.getScriptSelector());
                writer.writeElementObject("SwitchTime", it.getSwitchTime());
                writer.writeElementString("ValidityWindow",
                        it.getValidityWindow());
                writer.writeElementString("ExecWeekdays", it.getExecWeekdays());
                writer.writeElementString("ExecSpecDays", it.getExecSpecDays());
                writer.writeElementObject("BeginDate", it.getBeginDate());
                writer.writeElementObject("EndDate", it.getEndDate());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}