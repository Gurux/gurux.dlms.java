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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.ClockBase;

public class GXDLMSClock extends GXDLMSObject implements IGXDLMSBase {
    private ClockBase clockBase;
    private int deviation;
    private boolean enabled;
    private GXDateTime end = new GXDateTime();
    private java.util.Set<ClockStatus> status;
    private GXDateTime begin = new GXDateTime();
    private int timeZone;
    private GXDateTime time = new GXDateTime();

    /**
     * Constructor.
     */
    public GXDLMSClock() {
        super(ObjectType.CLOCK, "0.0.1.0.0.255", 0);
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
        deviation = 0;
        java.util.Set<DateTimeSkips> value = new HashSet<DateTimeSkips>();
        value.add(DateTimeSkips.MONTH);
        value.add(DateTimeSkips.DAY);
        begin.setSkip(value);
        end.setSkip(begin.getSkip());
        clockBase = ClockBase.NONE;
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSClock(final String ln) {
        super(ObjectType.CLOCK, ln, 0);
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
        deviation = 0;
        java.util.Set<DateTimeSkips> value = new HashSet<DateTimeSkips>();
        value.add(DateTimeSkips.MONTH);
        value.add(DateTimeSkips.DAY);
        begin.setSkip(value);
        end.setSkip(begin.getSkip());
        clockBase = ClockBase.NONE;
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSClock(final String ln, final int sn) {
        super(ObjectType.CLOCK, ln, sn);
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
        deviation = 0;
        java.util.Set<DateTimeSkips> value = new HashSet<DateTimeSkips>();
        value.add(DateTimeSkips.MONTH);
        value.add(DateTimeSkips.DAY);
        begin.setSkip(value);
        end.setSkip(begin.getSkip());
        clockBase = ClockBase.NONE;
    }

    @Override
    public final DataType getUIDataType(final int index) {
        if (index == 2 || index == 5 || index == 6) {
            return DataType.DATETIME;
        }
        return super.getDataType(index);
    }

    /**
     * @return Time of COSEM Clock object.
     */
    public final GXDateTime getTime() {
        return time;
    }

    /**
     * @param value
     *            Time of COSEM Clock object.
     */
    public final void setTime(final GXDateTime value) {
        time = value;
    }

    /**
     * @param value
     *            Time of COSEM Clock object.
     */
    public final void setTime(final Date value) {
        time = new GXDateTime(value);
    }

    /**
     * @param value
     *            Time of COSEM Clock object.
     */
    public final void setTime(final java.util.Calendar value) {
        time = new GXDateTime(value.getTime());
    }

    /**
     * @return TimeZone of COSEM Clock object.
     */
    public final int getTimeZone() {
        return timeZone;
    }

    /**
     * @param value
     *            TimeZone of COSEM Clock object.
     */
    public final void setTimeZone(final int value) {
        timeZone = value;
    }

    /**
     * @return Status of COSEM Clock object.
     */
    public final java.util.Set<ClockStatus> getStatus() {
        return status;
    }

    /**
     * @param value
     *            Status of COSEM Clock object.
     */
    public final void setStatus(final java.util.Set<ClockStatus> value) {
        status = value;
    }

    public final GXDateTime getBegin() {
        return begin;
    }

    public final void setBegin(final GXDateTime value) {
        begin = value;
    }

    public final GXDateTime getEnd() {
        return end;
    }

    public final void setEnd(final GXDateTime value) {
        end = value;
    }

    public final int getDeviation() {
        return deviation;
    }

    public final void setDeviation(final int value) {
        deviation = value;
    }

    public final boolean getEnabled() {
        return enabled;
    }

    public final void setEnabled(final boolean value) {
        enabled = value;
    }

    /**
     * @return Clock base of COSEM Clock object.
     */
    public final ClockBase getClockBase() {
        return clockBase;
    }

    /**
     * @param value
     *            Clock base of COSEM Clock object.
     */
    public final void setClockBase(final ClockBase value) {
        clockBase = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getTime(),
                new Integer(getTimeZone()), getStatus(), getBegin(), getEnd(),
                new Integer(getDeviation()), new Boolean(getEnabled()),
                getClockBase() };
    }

    /**
     * @return Returns current time.
     */
    public GXDateTime now() {
        Calendar now = Calendar.getInstance();
        GXDateTime tm = new GXDateTime(now);
        // -32768 == 0x8000
        int tzOffset = -deviation;
        if (timeZone == -1 || timeZone == -32768 || timeZone == 0x8000) {
            tm.getSkip().add(DateTimeSkips.DEVITATION);
        } else {
            // If clock's time zone is different what user want's to use.
            int offset =
                    timeZone + (int) now.getTimeZone().getRawOffset() / 60000;
            if (offset != 0) {
                TimeZone tz = GXDateTime.getTimeZone(timeZone, enabled);
                if (tz != null) {
                    now = Calendar.getInstance(tz);
                } else {
                    // Use current time zone if time zone is not found.
                    now = Calendar.getInstance();
                    tzOffset = -(timeZone
                            + (now.getTimeZone().getRawOffset() / 60000))
                            + -deviation;
                }

                tm.setMeterCalendar(now);
            }
        }
        // If clock's daylight saving is active but user do not want to use it.
        if (!enabled && now.getTimeZone().observesDaylightTime()) {
            tm.getStatus().remove(ClockStatus.DAYLIGHT_SAVE_ACTIVE);
            tm.getMeterCalendar().add(Calendar.MINUTE, -deviation);
        }
        return tm;
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // Resets the value to the default value.
        // The default value is an instance specific constant.
        if (e.getIndex() == 1) {
            GXDateTime dt = getTime();
            java.util.Calendar tm = Calendar.getInstance();
            tm.setTime(dt.getMeterCalendar().getTime());
            int minutes = tm.get(java.util.Calendar.MINUTE);
            if (minutes < 8) {
                minutes = 0;
            } else if (minutes < 23) {
                minutes = 15;
            } else if (minutes < 38) {
                minutes = 30;
            } else if (minutes < 53) {
                minutes = 45;
            } else {
                minutes = 0;
                tm.add(java.util.Calendar.HOUR_OF_DAY, 1);
            }
            tm.set(java.util.Calendar.MINUTE, minutes);
            tm.set(java.util.Calendar.SECOND, 0);
            tm.set(java.util.Calendar.MILLISECOND, 0);
            dt.setMeterCalendar(tm);
            setTime(dt);
        } else if (e.getIndex() == 3) {
            // Sets the meter's time to the nearest minute.
            GXDateTime dt = getTime();
            java.util.Calendar tm = Calendar.getInstance();
            tm.setTime(dt.getMeterCalendar().getTime());
            int s = tm.get(java.util.Calendar.SECOND);
            if (s > 30) {
                tm.add(java.util.Calendar.MINUTE, 1);
            }
            tm.set(java.util.Calendar.SECOND, 0);
            tm.set(java.util.Calendar.MILLISECOND, 0);
            dt.setMeterCalendar(tm);
            setTime(dt);

        } else if (e.getIndex() == 5) {
            // Presets the time to a new value (preset_time) and defines
            // avalidity_interval within which the new time can be activated.
            GXDateTime presetTime = (GXDateTime) GXDLMSClient.changeType(
                    (byte[]) ((Object[]) e.getParameters())[0],
                    DataType.DATETIME);
            // TODO:
            /*
             * GXDateTime validityIntervalStart = (GXDateTime) GXDLMSClient
             * .changeType((byte[]) Array.get(parameters, 1),
             * DataType.DATETIME); GXDateTime validityIntervalEnd = (GXDateTime)
             * GXDLMSClient .changeType((byte[]) Array.get(parameters, 2),
             * DataType.DATETIME);
             */
            setTime(presetTime);
        } else if (e.getIndex() == 6) {
            // Shifts the time.
            int shift = ((Number) e.getParameters()).intValue();
            GXDateTime dt = getTime();
            java.util.Calendar tm = Calendar.getInstance();
            tm.setTime(dt.getMeterCalendar().getTime());
            tm.add(java.util.Calendar.SECOND, shift);
            dt.setMeterCalendar(tm);
            setTime(dt);
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
    }

    /*
     * Sets the meter's time to the nearest (+/-) quarter of an hour value
     * (*:00, *:15, *:30, *:45).
     */
    public final byte[][] adjustToQuarter(final GXDLMSClient client) {
        return client.method(this, 1, new Integer(0), DataType.INT8);
    }

    /*
     * Sets the meter's time to the nearest (+/-) starting point of a measuring
     * period.
     */
    public final byte[][] adjustToMeasuringPeriod(final GXDLMSClient client) {
        return client.method(this, 2, new Integer(0), DataType.INT8);
    }

    /*
     * Sets the meter's time to the nearest minute. If second_counter < 30 s, so
     * second_counter is set to 0. If second_counter ³ 30 s, so second_counter
     * is set to 0, and minute_counter and all depending clock values are
     * incremented if necessary.
     */
    public final byte[][] adjustToMinute(final GXDLMSClient client) {
        return client.method(this, 3, new Integer(0), DataType.INT8);
    }

    /*
     * This method is used in conjunction with the preset_adjusting_time method.
     * If the meter's time lies between validity_interval_start and
     * validity_interval_end, then time is set to preset_time.
     */
    public final byte[][] adjustToPresetTime(final GXDLMSClient client) {
        return client.method(this, 4, new Integer(0), DataType.INT8);
    }

    /*
     * Presets the time to a new value (preset_time) and defines a
     * validity_interval within which the new time can be activated.
     */
    public final byte[][] presetAdjustingTime(final GXDLMSClient client,
            final Date presetTime, final Date validityIntervalStart,
            final Date validityIntervalEnd) {
        GXByteBuffer buff = new GXByteBuffer(44);
        buff.setUInt8(DataType.STRUCTURE.getValue());
        buff.setUInt8(3);
        GXCommon.setData(buff, DataType.OCTET_STRING, presetTime);
        GXCommon.setData(buff, DataType.OCTET_STRING, validityIntervalStart);
        GXCommon.setData(buff, DataType.OCTET_STRING, validityIntervalEnd);
        return client.method(this, 5, buff.array(), DataType.OCTET_STRING);
    }

    /*
     * Shifts the time by n (-900 <= n <= 900) s.
     */
    public final byte[][] shiftTime(final GXDLMSClient client,
            final int forTime) {
        if (forTime < -900 || forTime > 900) {
            throw new IllegalArgumentException("Invalid shift time.");
        }
        return client.method(this, 6, new Integer(forTime), DataType.INT16);
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
        // Time
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        // TimeZone
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // Status
        if (canRead(4)) {
            attributes.add(new Integer(4));
        }
        // Begin
        if (!isRead(5)) {
            attributes.add(new Integer(5));
        }
        // End
        if (!isRead(6)) {
            attributes.add(new Integer(6));
        }
        // Deviation
        if (!isRead(7)) {
            attributes.add(new Integer(7));
        }
        // Enabled
        if (!isRead(8)) {
            attributes.add(new Integer(8));
        }
        // ClockBase
        if (!isRead(9)) {
            attributes.add(new Integer(9));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 9;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 6;
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
            return DataType.INT16;
        }
        if (index == 4) {
            return DataType.UINT8;
        }
        if (index == 5) {
            return DataType.OCTET_STRING;
        }
        if (index == 6) {
            return DataType.OCTET_STRING;
        }
        if (index == 7) {
            return DataType.INT8;
        }
        if (index == 8) {
            return DataType.BOOLEAN;
        }
        if (index == 9) {
            return DataType.ENUM;
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
            return getTime();
        }
        if (e.getIndex() == 3) {
            return new Integer(getTimeZone());
        }
        if (e.getIndex() == 4) {
            return new Integer(ClockStatus.toInteger(status));
        }
        if (e.getIndex() == 5) {
            return getBegin();
        }
        if (e.getIndex() == 6) {
            return getEnd();
        }
        if (e.getIndex() == 7) {
            return new Integer(getDeviation());
        }
        if (e.getIndex() == 8) {
            return new Boolean(getEnabled());
        }
        if (e.getIndex() == 9) {
            return new Integer(getClockBase().ordinal());
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
            if (e.getValue() == null) {
                setTime(new GXDateTime());
            } else {
                GXDateTime tmp;
                if (e.getValue() instanceof byte[]) {
                    tmp = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) e.getValue(), DataType.DATETIME);
                } else {
                    tmp = (GXDateTime) e.getValue();
                }
                setTime(tmp);
            }
        } else if (e.getIndex() == 3) {
            if (e.getValue() == null) {
                setTimeZone(0);
            } else {
                setTimeZone(((Number) e.getValue()).intValue());
            }
        } else if (e.getIndex() == 4) {
            if (e.getValue() == null) {
                Set<ClockStatus> val = new HashSet<ClockStatus>();
                val.add(ClockStatus.OK);
                setStatus(val);
            } else {
                setStatus(ClockStatus
                        .forValue(((Number) e.getValue()).intValue()));
            }
        } else if (e.getIndex() == 5) {
            if (e.getValue() == null) {
                setBegin(new GXDateTime());
            } else if (e.getValue() instanceof byte[]) {
                GXDateTime tmp;
                tmp = (GXDateTime) GXDLMSClient
                        .changeType((byte[]) e.getValue(), DataType.DATETIME);
                setBegin(tmp);
            } else {
                setBegin((GXDateTime) e.getValue());
            }
        } else if (e.getIndex() == 6) {
            if (e.getValue() == null) {
                setEnd(new GXDateTime());
            } else if (e.getValue() instanceof byte[]) {
                GXDateTime tmp;
                tmp = (GXDateTime) GXDLMSClient
                        .changeType((byte[]) e.getValue(), DataType.DATETIME);
                setEnd(tmp);
            } else {
                setEnd((GXDateTime) e.getValue());
            }
        } else if (e.getIndex() == 7) {
            if (e.getValue() == null) {
                setDeviation(0);
            } else {
                setDeviation(((Number) e.getValue()).intValue());
            }
        } else if (e.getIndex() == 8) {
            if (e.getValue() == null) {
                setEnabled(false);
            } else {
                setEnabled(((Boolean) e.getValue()).booleanValue());
            }
        } else if (e.getIndex() == 9) {
            if (e.getValue() == null) {
                setClockBase(ClockBase.NONE);
            } else {
                setClockBase(
                        ClockBase.values()[((Number) e.getValue()).intValue()]);
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        if ("Time".compareToIgnoreCase(reader.getName()) == 0) {
            time = new GXDateTime(reader.readElementContentAsString("Time"));
        }
        timeZone = reader.readElementContentAsInt("TimeZone");
        status = ClockStatus.forValue(reader.readElementContentAsInt("Status"));
        String str = reader.readElementContentAsString("Begin");
        if (str != null) {
            begin = new GXDateTime(str);
        }
        str = reader.readElementContentAsString("End");
        if (str != null) {
            end = new GXDateTime(str);
        }
        deviation = reader.readElementContentAsInt("Deviation");
        enabled = reader.readElementContentAsInt("Enabled") != 0;
        clockBase =
                ClockBase.values()[reader.readElementContentAsInt("ClockBase")];
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (time != null) {
            writer.writeElementString("Time", time.toFormatString());
        }
        if (timeZone != 0) {
            writer.writeElementString("TimeZone", timeZone);
        }
        writer.writeElementString("Status", ClockStatus.toInteger(status));
        if (begin != null) {
            writer.writeElementString("Begin", begin.toFormatString());
        }
        if (end != null) {
            writer.writeElementString("End", end.toFormatString());
        }
        writer.writeElementString("Deviation", deviation);
        writer.writeElementString("Enabled", enabled);
        writer.writeElementString("ClockBase", clockBase.ordinal());
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}