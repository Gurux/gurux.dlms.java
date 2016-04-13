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

package gurux.dlms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.internal.GXCommon;

public class GXDateTime {
    /**
     * Clock status.
     */
    private java.util.Set<ClockStatus> status;
    /**
     * Clock time.
     */
    private java.util.Date time = new java.util.Date(0);
    /**
     * Skipped fields.
     */
    private java.util.Set<DateTimeSkips> skip;
    /**
     * Daylight savings begin.
     */
    private boolean daylightSavingsBegin;
    /**
     * Daylight savings end.
     */
    private boolean daylightSavingsEnd;
    /**
     * Deviation.
     */
    private int deviation;

    /**
     * Constructor.
     */
    public GXDateTime() {
        skip = new HashSet<DateTimeSkips>();
        deviation = 0x8000;
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
    }

    /**
     * Constructor.
     * 
     * @param forvalue
     *            Date value.
     */
    public GXDateTime(final Date forvalue) {
        skip = new HashSet<DateTimeSkips>();
        setValue(forvalue);
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
    }

    /**
     * Constructor.
     * 
     * @param year
     *            Used year.
     * @param month
     *            Used month.
     * @param day
     *            Used day.
     * @param hour
     *            Used hour.
     * @param minute
     *            Used minute.
     * @param second
     *            Used second.
     * @param millisecond
     *            Used millisecond.
     */
    public GXDateTime(final int year, final int month, final int day,
            final int hour, final int minute, final int second,
            final int millisecond) {
        int y = year;
        int m = month;
        int d = day;
        int h = hour;
        int min = minute;
        int s = second;
        int ms = millisecond;
        skip = new HashSet<DateTimeSkips>();
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
        if (y < 1 || y == 0xFFFF) {
            skip.add(DateTimeSkips.YEAR);
            java.util.Calendar tm = java.util.Calendar.getInstance();
            y = tm.get(Calendar.YEAR);
        }
        daylightSavingsBegin = m == 0xFE;
        daylightSavingsEnd = m == 0xFD;
        if (m < 1 || m > 12) {
            skip.add(DateTimeSkips.MONTH);
            m = 0;
        } else {
            m -= 1;
        }

        if (d == -1 || d == 0 || d > 31) {
            skip.add(DateTimeSkips.DAY);
            d = 1;
        } else if (d < 0) {
            Calendar cal = Calendar.getInstance();
            d = cal.getActualMaximum(Calendar.DATE) + d + 3;
        }
        if (h < 0 || h > 24) {
            skip.add(DateTimeSkips.HOUR);
            h = 0;
        }
        if (min < 0 || min > 60) {
            skip.add(DateTimeSkips.MINUTE);
            min = 0;
        }
        if (s < 0 || s > 60) {
            skip.add(DateTimeSkips.SECOND);
            s = 0;
        }
        // If ms is Zero it's skipped.
        if (ms < 1 || ms > 1000) {
            skip.add(DateTimeSkips.MILLISECOND);
            ms = 0;
        }
        java.util.Calendar tm = java.util.Calendar.getInstance();
        tm.set(y, m, d, h, min, s);
        if (ms != 0) {
            tm.set(Calendar.MILLISECOND, ms);
        }
        setValue(tm.getTime());
    }

    /**
     * @return Used date time value.
     */
    public final java.util.Date getValue() {
        return time;
    }

    /**
     * Set date time value.
     * 
     * @param forvalue
     *            Used date time value.
     */
    public final void setValue(final java.util.Date forvalue) {
        setValue(forvalue, java.util.Calendar.getInstance().getTimeZone()
                .getOffset(time.getTime()) / 60000);
    }

    /**
     * Set date time value.
     * 
     * @param forvalue
     *            Used date time value.
     * @param forDeviation
     *            Used deviation.
     */
    public final void setValue(final java.util.Date forvalue,
            final int forDeviation) {
        time = forvalue;
        deviation = forDeviation;
    }

    /**
     * @return Skipped date time fields.
     */
    public final java.util.Set<DateTimeSkips> getSkip() {
        return skip;
    }

    /**
     * @param forValue
     *            Skipped date time fields.
     */
    public final void setSkip(final java.util.Set<DateTimeSkips> forValue) {
        skip = forValue;
    }

    public final void setUsed(final java.util.Set<DateTimeSkips> forValue) {
        int val = 0;
        for (DateTimeSkips it : forValue) {
            val |= it.getValue();
        }
        int tmp = (-1 & ~val);
        skip = DateTimeSkips.forValue(tmp);
    }

    /**
     * @return Daylight savings begin.
     */
    public final boolean getDaylightSavingsBegin() {
        return daylightSavingsBegin;
    }

    /**
     * @param forValue
     *            Daylight savings begin.
     */
    public final void setDaylightSavingsBegin(final boolean forValue) {
        daylightSavingsBegin = forValue;
    }

    /**
     * @return Daylight savings end.
     */
    public final boolean getDaylightSavingsEnd() {
        return daylightSavingsEnd;
    }

    /**
     * @param forValue
     *            Daylight savings end.
     */
    public final void setDaylightSavingsEnd(final boolean forValue) {
        daylightSavingsEnd = forValue;
    }

    /**
     * @return Deviation.
     */
    public final int getDeviation() {
        return deviation;
    }

    /**
     * @param forValue
     *            Deviation.
     */
    public final void setDeviation(final int forValue) {
        deviation = forValue;
    }

    /*
     * Status of the clock.
     */
    public final java.util.Set<ClockStatus> getStatus() {
        return status;
    }

    public final void setStatus(final java.util.Set<ClockStatus> forValue) {
        status = forValue;
    }

    @Override
    public final String toString() {
        SimpleDateFormat sd = new SimpleDateFormat();
        if (!getSkip().isEmpty()) {
            // Separate date and time parts.
            List<String> tmp = GXCommon.split(sd.toPattern(), " ");
            List<String> date = new ArrayList<String>();
            List<String> tm = new ArrayList<String>();
            // Find date time separator.
            char separator = 0;
            for (char it : tmp.get(0).toCharArray()) {
                if (!Character.isLetter(it)) {
                    separator = it;
                    break;
                }
            }
            if (separator != 0) {
                String sep = "\\" + String.valueOf(separator);
                date.addAll(GXCommon.split(tmp.get(0), sep));
                tm.addAll(GXCommon.split(tmp.get(1), ":"));
                if (getSkip().contains(DateTimeSkips.YEAR)) {
                    date.remove("yyyy");
                }
                if (getSkip().contains(DateTimeSkips.MONTH)) {
                    date.remove("M");
                }
                if (getSkip().contains(DateTimeSkips.DAY)) {
                    date.remove("d");
                }
                if (getSkip().contains(DateTimeSkips.HOUR)) {
                    tm.remove("H");
                    tm.remove("HH");
                }
                if (getSkip().contains(DateTimeSkips.MINUTE)) {
                    tm.remove("m");
                    tm.remove("mm");
                }
                if (getSkip().contains(DateTimeSkips.SECOND)) {
                    tm.remove("ss");
                } else {
                    tm.add("ss");
                }
                if (getSkip().contains(DateTimeSkips.MILLISECOND)) {
                    tm.remove("SSS");
                } else {
                    tm.add("SSS");
                }

                String format = "";
                StringBuilder sb = new StringBuilder();
                if (!date.isEmpty()) {
                    for (String it : date) {
                        if (sb.length() != 0) {
                            sb.append(separator);
                        }
                        sb.append(it);
                    }
                    format = sb.toString();
                }
                if (!tm.isEmpty()) {
                    sb.setLength(0);
                    for (String it : tm) {
                        if (sb.length() != 0) {
                            sb.append(':');
                        }
                        sb.append(it);
                    }
                    if (format.length() != 0) {
                        format += " ";
                    }
                    format += sb.toString();
                }
                sd = new SimpleDateFormat(format);
                return sd.format(meterTimeToLocalTime(getMeterCalendar()));
            }
        }
        return sd.format(meterTimeToLocalTime(getMeterCalendar()));
    }

    public static Date meterTimeToLocalTime(final Calendar calendar) {
        Calendar tm = Calendar.getInstance();
        int offset = tm.getTimeZone().getRawOffset();
        offset -= calendar.getTimeZone().getRawOffset();
        offset /= 60000;
        tm.setTime(calendar.getTime());
        tm.add(Calendar.MINUTE, offset);
        return tm.getTime();
    }

    private static String getTimeZone(final int forDeviation) {
        String tmp = String.format("%02d:%02d", new Integer(forDeviation / 60),
                new Integer(forDeviation % 60));
        if (forDeviation == 0) {
            return "GMT";
        } else if (forDeviation > 0) {
            return "GMT+" + tmp;
        }
        return "GMT" + tmp;
    }

    /**
     * Gets a calendar using the meter's time zone.
     * 
     * @return Meter's calendar.
     */
    public final Calendar getMeterCalendar() {
        // Add deviation.
        TimeZone tz;
        tz = TimeZone.getTimeZone(getTimeZone(deviation));
        java.util.Calendar tm = java.util.Calendar.getInstance(tz);
        tm.setTime(time);
        // Convert to GMT if time zone not found.
        if (tz.getRawOffset() / 60000 != deviation) {
            tm.add(Calendar.MINUTE, -deviation);
        }
        return tm;
    }

    /**
     * @return Date time as local time.
     */
    public final Date toLocalTime() {
        return meterTimeToLocalTime(getMeterCalendar());
    }

    /**
     * @return Date time as local time.
     */
    public final Date toMeterTime() {
        return getMeterCalendar().getTime();
    }
}