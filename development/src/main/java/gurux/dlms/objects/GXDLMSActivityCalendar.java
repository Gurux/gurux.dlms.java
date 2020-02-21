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

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSActivityCalendar
 */
public class GXDLMSActivityCalendar extends GXDLMSObject
        implements IGXDLMSBase {
    private String calendarNameActive;
    private String calendarNamePassive;
    private GXDLMSSeasonProfile[] seasonProfileActive;
    private GXDLMSWeekProfile[] weekProfileTableActive;
    private GXDLMSDayProfile[] dayProfileTableActive;
    private GXDLMSSeasonProfile[] seasonProfilePassive;
    private GXDLMSDayProfile[] dayProfileTablePassive;
    private GXDLMSWeekProfile[] weekProfileTablePassive;
    private GXDateTime time;
    private boolean isSec = false;

    /**
     * Constructor.
     */
    public GXDLMSActivityCalendar() {
        this("0.0.13.0.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSActivityCalendar(final String ln) {
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
    public GXDLMSActivityCalendar(final String ln, final int sn) {
        super(ObjectType.ACTIVITY_CALENDAR, ln, sn);
    }

    public final String getCalendarNameActive() {
        return calendarNameActive;
    }

    public final void setCalendarNameActive(final String value) {
        calendarNameActive = value;
    }

    public final GXDLMSSeasonProfile[] getSeasonProfileActive() {
        return seasonProfileActive;
    }

    public final void
            setSeasonProfileActive(final GXDLMSSeasonProfile[] value) {
        seasonProfileActive = value;
    }

    public final GXDLMSWeekProfile[] getWeekProfileTableActive() {
        return weekProfileTableActive;
    }

    public final void
            setWeekProfileTableActive(final GXDLMSWeekProfile[] value) {
        weekProfileTableActive = value;
    }

    public final GXDLMSDayProfile[] getDayProfileTableActive() {
        return dayProfileTableActive;
    }

    public final void setDayProfileTableActive(final GXDLMSDayProfile[] value) {
        dayProfileTableActive = value;
    }

    public final String getCalendarNamePassive() {
        return calendarNamePassive;
    }

    public final void setCalendarNamePassive(final String value) {
        calendarNamePassive = value;
    }

    public final GXDLMSSeasonProfile[] getSeasonProfilePassive() {
        return seasonProfilePassive;
    }

    public final void
            setSeasonProfilePassive(final GXDLMSSeasonProfile[] value) {
        seasonProfilePassive = value;
    }

    public final GXDLMSWeekProfile[] getWeekProfileTablePassive() {
        return weekProfileTablePassive;
    }

    public final void
            setWeekProfileTablePassive(final GXDLMSWeekProfile[] value) {
        weekProfileTablePassive = value;
    }

    public final GXDLMSDayProfile[] getDayProfileTablePassive() {
        return dayProfileTablePassive;
    }

    public final void
            setDayProfileTablePassive(final GXDLMSDayProfile[] value) {
        dayProfileTablePassive = value;
    }

    public final GXDateTime getTime() {
        return time;
    }

    public final void setTime(final GXDateTime value) {
        time = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getCalendarNameActive(),
                getSeasonProfileActive(), getWeekProfileTableActive(),
                getDayProfileTableActive(), getCalendarNamePassive(),
                getSeasonProfilePassive(), getWeekProfileTablePassive(),
                getDayProfileTablePassive(), getTime() };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead(final boolean all) {
        final java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null
                || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // CalendarNameActive
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // SeasonProfileActive
        if (all || canRead(3)) {
            attributes.add(3);
        }

        // WeekProfileTableActive
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // DayProfileTableActive
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // CalendarNamePassive
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // SeasonProfilePassive
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // WeekProfileTablePassive
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // DayProfileTablePassive
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // Time.
        if (all || canRead(10)) {
            attributes.add(10);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 10;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.OCTET_STRING;
        }
        if (index == 3) {
            return DataType.ARRAY;
        }
        if (index == 4) {
            return DataType.ARRAY;
        }
        if (index == 5) {
            return DataType.ARRAY;
        }
        if (index == 6) {
            return DataType.OCTET_STRING;
        }
        if (index == 7) {
            return DataType.ARRAY;
        }
        if (index == 8) {
            return DataType.ARRAY;
        }
        if (index == 9) {
            return DataType.ARRAY;
        }
        if (index == 10) {
            if (isSec) {
                return DataType.DATETIME;
            }
            return DataType.OCTET_STRING;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    static Object getSeasonProfile(final GXDLMSSettings settings,
            final GXDLMSSeasonProfile[] target) {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.ARRAY.getValue());
        if (target == null) {
            // Add count
            GXCommon.setObjectCount(0, data);
        } else {
            int cnt = target.length;
            // Add count
            GXCommon.setObjectCount(cnt, data);
            for (final GXDLMSSeasonProfile it : target) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(3);
                GXCommon.setData(settings, data, DataType.OCTET_STRING,
                        it.getName());
                GXCommon.setData(settings, data, DataType.OCTET_STRING,
                        it.getStart());
                GXCommon.setData(settings, data, DataType.OCTET_STRING,
                        it.getWeekName());
            }
        }
        return data.array();
    }

    static Object getWeekProfileTable(final GXDLMSSettings settings,
            final GXDLMSWeekProfile[] target) {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.ARRAY.getValue());
        if (target == null) {
            // Add count
            GXCommon.setObjectCount(0, data);
        } else {
            // Add count
            GXCommon.setObjectCount(target.length, data);
            for (final GXDLMSWeekProfile it : target) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(8);
                GXCommon.setData(settings, data, DataType.OCTET_STRING,
                        it.getName());
                GXCommon.setData(settings, data, DataType.UINT8,
                        it.getMonday());
                GXCommon.setData(settings, data, DataType.UINT8,
                        it.getTuesday());
                GXCommon.setData(settings, data, DataType.UINT8,
                        it.getWednesday());
                GXCommon.setData(settings, data, DataType.UINT8,
                        it.getThursday());
                GXCommon.setData(settings, data, DataType.UINT8,
                        it.getFriday());
                GXCommon.setData(settings, data, DataType.UINT8,
                        it.getSaturday());
                GXCommon.setData(settings, data, DataType.UINT8,
                        it.getSunday());
            }
        }
        return data.array();
    }

    static Object getDayProfileTable(final GXDLMSSettings settings,
            final GXDLMSDayProfile[] target) {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.ARRAY.getValue());
        if (target == null) {
            // Add count
            GXCommon.setObjectCount(0, data);
        } else {
            // Add count
            GXCommon.setObjectCount(target.length, data);
            for (final GXDLMSDayProfile it : target) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(2);
                GXCommon.setData(settings, data, DataType.UINT8, it.getDayId());
                data.setUInt8(DataType.ARRAY.getValue());
                // Add count
                GXCommon.setObjectCount(it.getDaySchedules().length, data);
                for (final GXDLMSDayProfileAction action : it
                        .getDaySchedules()) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    data.setUInt8(3);
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            action.getStartTime());
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(
                                    action.getScriptLogicalName()));
                    GXCommon.setData(settings, data, DataType.UINT16,
                            action.getScriptSelector());
                }
            }
        }
        return data.array();
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            if (calendarNameActive == null) {
                return null;
            }
            if (isSec) {
                return GXCommon.hexToBytes(calendarNameActive);
            }
            return calendarNameActive.getBytes();
        case 3:
            return getSeasonProfile(settings, seasonProfileActive);
        case 4:
            return getWeekProfileTable(settings, weekProfileTableActive);
        case 5:
            return getDayProfileTable(settings, dayProfileTableActive);
        case 6:
            if (calendarNamePassive == null) {
                return null;
            }
            if (isSec) {
                return GXCommon.hexToBytes(calendarNamePassive);
            }
            return calendarNamePassive.getBytes();
        case 7:
            return getSeasonProfile(settings, seasonProfilePassive);
        case 8:
            return getWeekProfileTable(settings, weekProfileTablePassive);
        case 9:
            return getDayProfileTable(settings, dayProfileTablePassive);
        case 10:
            return getTime();
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            return null;
        }
    }

    static GXDLMSSeasonProfile[] getSeasonProfile(final Object value) {
        final List<GXDLMSSeasonProfile> items =
                new ArrayList<GXDLMSSeasonProfile>();
        if (value != null) {
            for (final Object item : (List<?>) value) {
                List<?> arr = (List<?>) item;
                final GXDLMSSeasonProfile it = new GXDLMSSeasonProfile();
                it.setName((byte[]) arr.get(0));
                it.setStart((GXDateTime) GXDLMSClient
                        .changeType((byte[]) arr.get(1), DataType.DATETIME));
                byte[] weekName = (byte[]) arr.get(2);
                // If week name is ignored.
                if (weekName != null && weekName.length == 1
                        && weekName[0] == 0xFF) {
                    it.setWeekName("");
                } else {
                    it.setWeekName(weekName);
                }

                items.add(it);
            }
        }
        return items.toArray(new GXDLMSSeasonProfile[items.size()]);
    }

    static GXDLMSWeekProfile[] getWeekProfileTable(final Object value) {
        final List<GXDLMSWeekProfile> items =
                new ArrayList<GXDLMSWeekProfile>();
        if (value != null) {
            for (final Object item : (List<?>) value) {
                List<?> arr = (List<?>) item;
                final GXDLMSWeekProfile it = new GXDLMSWeekProfile();
                it.setName((byte[]) arr.get(0));
                it.setMonday(((Number) arr.get(1)).intValue());
                it.setTuesday(((Number) arr.get(2)).intValue());
                it.setWednesday(((Number) arr.get(3)).intValue());
                it.setThursday(((Number) arr.get(4)).intValue());
                it.setFriday(((Number) arr.get(5)).intValue());
                it.setSaturday(((Number) arr.get(6)).intValue());
                it.setSunday(((Number) arr.get(7)).intValue());
                items.add(it);
            }
        }
        return items.toArray(new GXDLMSWeekProfile[items.size()]);
    }

    static GXDLMSDayProfile[] getDayProfileTable(final Object value) {
        final List<GXDLMSDayProfile> items = new ArrayList<GXDLMSDayProfile>();
        if (value != null) {
            for (final Object item : (List<?>) value) {
                List<?> arr = (List<?>) item;
                final GXDLMSDayProfile it = new GXDLMSDayProfile();
                it.setDayId(((Number) arr.get(0)).intValue());
                final List<GXDLMSDayProfileAction> actions =
                        new ArrayList<GXDLMSDayProfileAction>();
                for (final Object it2 : (List<?>) arr.get(1)) {
                    List<?> arr1 = (List<?>) it2;
                    final GXDLMSDayProfileAction ac =
                            new GXDLMSDayProfileAction();
                    ac.setStartTime((GXTime) GXDLMSClient
                            .changeType((byte[]) arr1.get(0), DataType.TIME));
                    ac.setScriptLogicalName(
                            GXCommon.toLogicalName(arr1.get(1)));
                    ac.setScriptSelector(((Number) arr1.get(2)).intValue());
                    actions.add(ac);
                }
                it.setDaySchedules(actions
                        .toArray(new GXDLMSDayProfileAction[actions.size()]));
                items.add(it);
            }
        }
        return items.toArray(new GXDLMSDayProfile[items.size()]);
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        String tmp;
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            if (e.getValue() == null) {
                setCalendarNameActive(null);
            } else {
                tmp = new String((byte[]) e.getValue()).trim();
                if (isSec || !GXCommon.isAscii(tmp)) {
                    setCalendarNameActive(
                            GXCommon.toHex((byte[]) e.getValue()));
                } else {
                    setCalendarNameActive(tmp);
                }
            }
            break;
        case 3:
            setSeasonProfileActive(getSeasonProfile(e.getValue()));
            break;
        case 4:
            setWeekProfileTableActive(getWeekProfileTable(e.getValue()));
            break;
        case 5:
            setDayProfileTableActive(getDayProfileTable(e.getValue()));
            break;
        case 6:
            if (e.getValue() == null) {
                setCalendarNamePassive(null);
            } else {
                tmp = new String((byte[]) e.getValue()).trim();
                if (isSec || !GXCommon.isAscii(tmp)) {
                    setCalendarNamePassive(
                            GXCommon.toHex((byte[]) e.getValue()));
                } else {
                    setCalendarNamePassive(new String((byte[]) e.getValue()));
                }
            }
            break;
        case 7:
            setSeasonProfilePassive(getSeasonProfile(e.getValue()));
            break;
        case 8:
            setWeekProfileTablePassive(getWeekProfileTable(e.getValue()));
            break;
        case 9:
            setDayProfileTablePassive(getDayProfileTable(e.getValue()));
            break;
        case 10:
            if (e.getValue() instanceof GXDateTime) {
                setTime((GXDateTime) e.getValue());
            } else {
                setTime((GXDateTime) GXDLMSClient
                        .changeType((byte[]) e.getValue(), DataType.DATETIME));
            }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    private static GXDLMSSeasonProfile[]
            loadSeasonProfile(final GXXmlReader reader, final String name)
                    throws XMLStreamException {
        List<GXDLMSSeasonProfile> list = new ArrayList<GXDLMSSeasonProfile>();
        if (reader.isStartElement(name, true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSSeasonProfile it = new GXDLMSSeasonProfile();
                it.setName(GXDLMSTranslator
                        .hexToBytes(reader.readElementContentAsString("Name")));
                it.setStart(reader.readElementContentAsDateTime("Start"));
                it.setWeekName(GXDLMSTranslator.hexToBytes(
                        reader.readElementContentAsString("WeekName")));
                list.add(it);
            }
            reader.readEndElement(name);
        }
        return list.toArray(new GXDLMSSeasonProfile[list.size()]);
    }

    private static GXDLMSWeekProfile[]
            loadWeekProfileTable(final GXXmlReader reader, final String name)
                    throws XMLStreamException {
        List<GXDLMSWeekProfile> list = new ArrayList<GXDLMSWeekProfile>();
        if (reader.isStartElement(name, true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSWeekProfile it = new GXDLMSWeekProfile();
                it.setName(GXDLMSTranslator
                        .hexToBytes(reader.readElementContentAsString("Name")));
                it.setMonday(reader.readElementContentAsInt("Monday"));
                it.setTuesday(reader.readElementContentAsInt("Tuesday"));
                it.setWednesday(reader.readElementContentAsInt("Wednesday"));
                it.setThursday(reader.readElementContentAsInt("Thursday"));
                it.setFriday(reader.readElementContentAsInt("Friday"));
                it.setSaturday(reader.readElementContentAsInt("Saturday"));
                it.setSunday(reader.readElementContentAsInt("Sunday"));
                list.add(it);
            }
            reader.readEndElement(name);
        }
        return list.toArray(new GXDLMSWeekProfile[list.size()]);
    }

    private static GXDLMSDayProfile[]
            loadDayProfileTable(final GXXmlReader reader, final String name)
                    throws XMLStreamException {
        List<GXDLMSDayProfile> list = new ArrayList<GXDLMSDayProfile>();
        if (reader.isStartElement(name, true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSDayProfile it = new GXDLMSDayProfile();
                it.setDayId(reader.readElementContentAsInt("DayId"));
                list.add(it);
                List<GXDLMSDayProfileAction> actions =
                        new ArrayList<GXDLMSDayProfileAction>();
                if (reader.isStartElement("Actions", true)) {
                    while (reader.isStartElement("Action", true)) {
                        GXDLMSDayProfileAction d = new GXDLMSDayProfileAction();
                        actions.add(d);
                        d.setStartTime(
                                reader.readElementContentAsTime("Start"));
                        d.setScriptLogicalName(
                                reader.readElementContentAsString("LN"));
                        d.setScriptSelector(
                                reader.readElementContentAsInt("Selector"));
                    }
                    reader.readEndElement("Actions");
                }
                it.setDaySchedules(actions
                        .toArray(new GXDLMSDayProfileAction[actions.size()]));
            }
            reader.readEndElement(name);
        }
        return list.toArray(new GXDLMSDayProfile[list.size()]);
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        calendarNameActive =
                reader.readElementContentAsString("CalendarNameActive");
        seasonProfileActive = loadSeasonProfile(reader, "SeasonProfileActive");
        weekProfileTableActive =
                loadWeekProfileTable(reader, "WeekProfileTableActive");
        dayProfileTableActive =
                loadDayProfileTable(reader, "DayProfileTableActive");
        calendarNamePassive =
                reader.readElementContentAsString("CalendarNamePassive");
        seasonProfilePassive =
                loadSeasonProfile(reader, "SeasonProfilePassive");
        weekProfileTablePassive =
                loadWeekProfileTable(reader, "WeekProfileTablePassive");
        dayProfileTablePassive =
                loadDayProfileTable(reader, "DayProfileTablePassive");
        time = reader.readElementContentAsDateTime("Time");
    }

    private void saveSeasonProfile(final GXXmlWriter writer,
            final GXDLMSSeasonProfile[] list, final String name)
            throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement(name);
            for (GXDLMSSeasonProfile it : list) {
                writer.writeStartElement("Item");
                writer.writeElementString("Name",
                        GXDLMSTranslator.toHex(it.getName()));
                writer.writeElementString("Start", it.getStart());
                writer.writeElementString("WeekName",
                        GXDLMSTranslator.toHex(it.getWeekName()));
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveWeekProfileTable(final GXXmlWriter writer,
            final GXDLMSWeekProfile[] list, final String name)
            throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement(name);
            for (GXDLMSWeekProfile it : list) {
                writer.writeStartElement("Item");
                writer.writeElementString("Name",
                        GXDLMSTranslator.toHex(it.getName()));
                writer.writeElementString("Monday", it.getMonday());
                writer.writeElementString("Tuesday", it.getTuesday());
                writer.writeElementString("Wednesday", it.getWednesday());
                writer.writeElementString("Thursday", it.getThursday());
                writer.writeElementString("Friday", it.getFriday());
                writer.writeElementString("Saturday", it.getSaturday());
                writer.writeElementString("Sunday", it.getSunday());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveDayProfileTable(final GXXmlWriter writer,
            final GXDLMSDayProfile[] list, final String name)
            throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement(name);
            for (GXDLMSDayProfile it : list) {
                writer.writeStartElement("Item");
                writer.writeElementString("DayId",
                        String.valueOf(it.getDayId()));
                writer.writeStartElement("Actions");
                for (GXDLMSDayProfileAction d : it.getDaySchedules()) {
                    writer.writeStartElement("Action");
                    writer.writeElementString("Start", d.getStartTime());
                    writer.writeElementString("LN", d.getScriptLogicalName());
                    writer.writeElementString("Selector",
                            d.getScriptSelector());
                    writer.writeEndElement();
                }
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("CalendarNameActive", calendarNameActive);
        saveSeasonProfile(writer, seasonProfileActive, "SeasonProfileActive");
        saveWeekProfileTable(writer, weekProfileTableActive,
                "WeekProfileTableActive");
        saveDayProfileTable(writer, dayProfileTableActive,
                "DayProfileTableActive");
        writer.writeElementString("CalendarNamePassive", calendarNamePassive);
        saveSeasonProfile(writer, seasonProfilePassive, "SeasonProfilePassive");
        saveWeekProfileTable(writer, weekProfileTablePassive,
                "WeekProfileTablePassive");
        saveDayProfileTable(writer, dayProfileTablePassive,
                "DayProfileTablePassive");
        writer.writeElementString("Time", time);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
        // Not needed for this object.
    }
}