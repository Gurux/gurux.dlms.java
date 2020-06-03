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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXBitString;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDate;
import gurux.dlms.GXTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.Weekdays;

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
        this("0.0.12.0.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSchedule(final String ln) {
        this(ln, 0);
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
        entries = new ArrayList<GXDLMSScheduleEntry>();
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

    /**
     * Add entry to entries list.
     * 
     * @param client
     *            DLMS client.
     * @param entry
     *            Schedule entry.
     * @return Action bytes.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     */
    public final byte[][] insert(final GXDLMSClient client,
            final GXDLMSScheduleEntry entry)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer data = new GXByteBuffer();
        addEntry(null, entry, data);
        return client.method(this, 2, data.array(), DataType.STRUCTURE);
    }

    /**
     * Remove entry from entries list.
     * 
     * @param client
     *            DLMS client.
     * @param entry
     *            Schedule entry.
     * @return Action bytes.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     */
    public final byte[][] delete(GXDLMSClient client, GXDLMSScheduleEntry entry)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.STRUCTURE.getValue());
        // Add structure size.
        data.setUInt8(2);
        // firstIndex
        GXCommon.setData(null, data, DataType.UINT16, entry.getIndex());
        // lastIndex
        GXCommon.setData(null, data, DataType.UINT16, entry.getIndex());
        return client.method(this, 3, data.array(), DataType.STRUCTURE);
    }

    /**
     * Enable entry from entries list.
     * 
     * @param client
     *            DLMS client.
     * @param entry
     *            Schedule entries.
     * @return Action bytes.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     */
    public final byte[][] enable(GXDLMSClient client, GXDLMSScheduleEntry entry)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.STRUCTURE.getValue());
        // Add structure size.
        data.setUInt8(4);
        // firstIndex
        GXCommon.setData(null, data, DataType.UINT16, entry.getIndex());
        // lastIndex
        GXCommon.setData(null, data, DataType.UINT16, entry.getIndex());
        GXCommon.setData(null, data, DataType.UINT16, 0);
        GXCommon.setData(null, data, DataType.UINT16, 0);
        return client.method(this, 1, data.array(), DataType.STRUCTURE);
    }

    /**
     * Disable entry from entries list.
     * 
     * @param client
     *            DLMS client.
     * @param entry
     *            Schedule entries.
     * @return Action bytes.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     */
    public final byte[][] disable(GXDLMSClient client,
            GXDLMSScheduleEntry entry)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.STRUCTURE.getValue());
        // Add structure size.
        data.setUInt8(4);
        // firstIndex
        GXCommon.setData(null, data, DataType.UINT16, 0);
        GXCommon.setData(null, data, DataType.UINT16, 0);
        GXCommon.setData(null, data, DataType.UINT16, entry.getIndex());
        // lastIndex
        GXCommon.setData(null, data, DataType.UINT16, entry.getIndex());
        return client.method(this, 1, data.array(), DataType.STRUCTURE);
    }

    private void removeEntry(final int index) {
        for (GXDLMSScheduleEntry it : entries) {
            if (it.getIndex() == index) {
                entries.remove(it);
                break;
            }
        }
    }

    private void enableDisable(java.util.ArrayList<?> tmp) {
        // Enable
        for (int index =
                ((Number) tmp.get(0)).intValue(); index <= ((Number) tmp.get(1))
                        .intValue(); ++index) {
            if (index != 0) {
                for (GXDLMSScheduleEntry it : entries) {
                    if (it.getIndex() == index) {
                        it.setEnable(true);
                        break;
                    }
                }
            }
        }
        // Disable
        for (int index =
                ((Number) tmp.get(2)).intValue(); index <= ((Number) tmp.get(3))
                        .intValue(); ++index) {
            if (index != 0) {
                for (GXDLMSScheduleEntry it : entries) {
                    if (it.getIndex() == index) {
                        it.setEnable(false);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        // Enable/disable entry
        case 1: {
            enableDisable((java.util.ArrayList<?>) e.getParameters());
        }
            break;
        // Insert entry
        case 2:
            boolean useUtc;
            if (e.getSettings() != null) {
                useUtc = e.getSettings().getUseUtc2NormalTime();
            } else {
                useUtc = false;
            }
            GXDLMSScheduleEntry entry = createEntry(settings,
                    (java.util.ArrayList<?>) e.getParameters(), useUtc);
            removeEntry(entry.getIndex());
            entries.add(entry);
            break;
        // Delete entry
        case 3: {
            java.util.ArrayList<?> tmp =
                    (java.util.ArrayList<?>) e.getParameters();
            for (int index = ((Number) tmp.get(0))
                    .intValue(); index <= ((Number) tmp.get(1))
                            .intValue(); ++index) {
                removeEntry(index);
            }
        }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
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

    private void addEntry(final GXDLMSSettings settings,
            final GXDLMSScheduleEntry it, GXByteBuffer data) {
        data.setUInt8(DataType.STRUCTURE.getValue());
        data.setUInt8(10);
        // Add index.
        data.setUInt8(DataType.UINT16.getValue());
        data.setUInt16(it.getIndex());
        // Add enable.
        data.setUInt8(DataType.BOOLEAN.getValue());
        data.setUInt8((it.getEnable() ? 1 : 0));
        // Add logical Name.
        data.setUInt8(DataType.OCTET_STRING.getValue());
        data.setUInt8(6);
        if (it.getLogicalName() == null) {
            data.set(new byte[] { 0, 0, 0, 0, 0, 0 });
        } else {
            data.set(GXCommon.logicalNameToBytes(it.getLogicalName()));
        }
        // Add script selector.
        data.setUInt8(DataType.UINT16.getValue());
        data.setUInt16(it.getScriptSelector());
        // Add switch time.
        GXCommon.setData(settings, data, DataType.OCTET_STRING,
                it.getSwitchTime());
        // Add validity window.
        data.setUInt8(DataType.UINT16.getValue());
        data.setUInt16(it.getValidityWindow());
        // Add exec week days.
        GXCommon.setData(settings, data, DataType.BITSTRING, GXBitString
                .toBitString(Weekdays.toInteger(it.getExecWeekdays()), 7));
        // Add exec spec days.
        GXCommon.setData(settings, data, DataType.BITSTRING,
                it.getExecSpecDays());
        // Add begin date.
        GXCommon.setData(settings, data, DataType.OCTET_STRING,
                it.getBeginDate());
        // Add end date.
        GXCommon.setData(settings, data, DataType.OCTET_STRING,
                it.getEndDate());
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
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(entries.size(), data);
            for (GXDLMSScheduleEntry it : entries) {
                addEntry(settings, it, data);
            }
            return data.array();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    /*
     * Create a new entry.
     */
    private GXDLMSScheduleEntry createEntry(final GXDLMSSettings settings,
            final List<?> tmp, final boolean useUtc) {
        GXDLMSScheduleEntry item = new GXDLMSScheduleEntry();
        item.setIndex(((Number) tmp.get(0)).intValue());
        item.setEnable(((Boolean) tmp.get(1)).booleanValue());
        item.setLogicalName(GXCommon.toLogicalName(tmp.get(2)));
        item.setScriptSelector(((Number) tmp.get(3)).intValue());
        item.setSwitchTime((GXTime) GXDLMSClient.changeType((byte[]) tmp.get(4),
                DataType.TIME, useUtc));
        item.setValidityWindow(((Number) tmp.get(5)).intValue());
        item.setExecWeekdays(
                Weekdays.forValue(((GXBitString) tmp.get(6)).toInteger()));
        item.setExecSpecDays(tmp.get(7).toString());
        item.setBeginDate((GXDate) GXDLMSClient.changeType((byte[]) tmp.get(8),
                DataType.DATE, useUtc));
        item.setEndDate((GXDate) GXDLMSClient.changeType((byte[]) tmp.get(9),
                DataType.DATE, useUtc));
        return item;
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
            boolean useUtc;
            if (e.getSettings() != null) {
                useUtc = e.getSettings().getUseUtc2NormalTime();
            } else {
                useUtc = false;
            }
            for (Object it : arr) {
                List<?> tmp = (List<?>) it;
                entries.add(createEntry(settings, tmp, useUtc));
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
                it.setSwitchTime(reader.readElementContentAsTime("SwitchTime"));
                it.setValidityWindow((byte) reader
                        .readElementContentAsInt("ValidityWindow"));
                it.setExecWeekdays(Weekdays.forValue(
                        reader.readElementContentAsInt("ExecWeekdays")));
                it.setExecSpecDays(
                        reader.readElementContentAsString("ExecSpecDays"));
                it.setBeginDate(reader.readElementContentAsDate("BeginDate"));
                it.setEndDate(reader.readElementContentAsDate("EndDate"));
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
                writer.writeElementString("SwitchTime", it.getSwitchTime());
                writer.writeElementString("ValidityWindow",
                        it.getValidityWindow());
                writer.writeElementString("ExecWeekdays",
                        Weekdays.toInteger(it.getExecWeekdays()));
                writer.writeElementString("ExecSpecDays", it.getExecSpecDays());
                writer.writeElementString("BeginDate", it.getBeginDate());
                writer.writeElementString("EndDate", it.getEndDate());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}