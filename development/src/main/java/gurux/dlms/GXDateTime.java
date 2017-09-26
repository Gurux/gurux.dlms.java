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

import java.text.DecimalFormat;
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
    private Calendar meterCalendar;
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
     * Constructor.
     */
    public GXDateTime() {
        skip = new HashSet<DateTimeSkips>();
        meterCalendar = Calendar.getInstance();
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Date value.
     */
    public GXDateTime(final Date value) {
        skip = new HashSet<DateTimeSkips>();
        meterCalendar = Calendar.getInstance();
        meterCalendar.setTime(value);
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Date value.
     */
    public GXDateTime(final Calendar value) {
        skip = new HashSet<DateTimeSkips>();
        meterCalendar = value;
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
        meterCalendar = Calendar.getInstance();
        init(year, month, day, hour, minute, second, millisecond);
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
     * @param timeZone
     *            Used time Zone.
     * @deprecated use {@link #GXDateTime} instead.
     */
    public GXDateTime(final int year, final int month, final int day,
            final int hour, final int minute, final int second,
            final int millisecond, final int timeZone) {
        meterCalendar = Calendar.getInstance(getTimeZone(timeZone, true));
        init(year, month, day, hour, minute, second, millisecond);
    }

    /**
     * Initialize settings.
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
    protected void init(final int year, final int month, final int day,
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
            Calendar tm = Calendar.getInstance();
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
        meterCalendar.set(y, m, d, h, min, s);
        if (ms != 0) {
            meterCalendar.set(Calendar.MILLISECOND, ms);
        }
    }

    /**
     * Constructor
     * 
     * @param value
     *            Date time value as a string.
     */
    public GXDateTime(final String value) {
        if (value != null) {
            int year = 2000, month = 1, day = 1, hour = 0, min = 0, sec = 0;
            SimpleDateFormat sd = new SimpleDateFormat();
            // Separate date and time parts.
            List<String> tmp = GXCommon.split(sd.toPattern(), " ");
            List<String> shortDatePattern = new ArrayList<String>();
            List<String> shortTimePattern = new ArrayList<String>();
            // Find date time separator.
            char separator = 0;
            for (char it : tmp.get(0).toCharArray()) {
                if (!Character.isLetter(it)) {
                    separator = it;
                    break;
                }
            }
            String sep = String.valueOf(separator);
            shortDatePattern.addAll(GXCommon.split(tmp.get(0), sep));
            shortTimePattern.addAll(GXCommon.split(tmp.get(1), ":"));
            // Add seconds if not used.
            if (!shortTimePattern.contains("ss")) {
                shortTimePattern.add("ss");
            }
            List<String> values = GXCommon.split(value.trim(),
                    new char[] { separator, ':', ' ' });
            if (shortDatePattern.size() != values.size()
                    && shortDatePattern.size()
                            + shortTimePattern.size() != values.size()) {
                throw new IllegalArgumentException("Invalid DateTime");
            }
            for (int pos = 0; pos != shortDatePattern.size(); ++pos) {
                boolean ignore = false;
                if ("*".equals(values.get(pos))) {
                    ignore = true;
                }
                String val = shortDatePattern.get(pos);
                if ("yyyy".compareToIgnoreCase(val) == 0) {
                    if (ignore) {
                        year = -1;
                    } else {
                        year = Integer.parseInt(values.get(pos));
                    }
                } else if ("M".compareToIgnoreCase(val) == 0) {
                    if (ignore) {
                        month = -1;
                    } else {
                        month = Integer.parseInt(values.get(pos));
                    }
                } else if ("d".compareToIgnoreCase(val) == 0) {
                    if (ignore) {
                        day = -1;
                    } else {
                        day = Integer.parseInt(values.get(pos));
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Invalid Date time pattern.");
                }
            }
            if (values.size() > 3) {
                for (int pos = 0; pos != shortTimePattern.size(); ++pos) {
                    boolean ignore = false;
                    if ("*".equals(values.get(3 + pos))) {
                        ignore = true;
                    }
                    String val = shortTimePattern.get(pos);
                    if ("h".compareToIgnoreCase(val) == 0) {
                        if (ignore) {
                            hour = -1;
                        } else {
                            hour = Integer.parseInt(values.get(3 + pos));
                        }
                    } else if ("mm".compareToIgnoreCase(val) == 0) {
                        if (ignore) {
                            min = -1;
                        } else {
                            min = Integer.parseInt(values.get(3 + pos));
                        }
                    } else if ("ss".compareToIgnoreCase(val) == 0) {
                        if (ignore) {
                            sec = -1;
                        } else {
                            sec = Integer.parseInt(values.get(3 + pos));
                        }
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid Date time pattern.");
                    }
                }
            }
            meterCalendar = Calendar.getInstance();
            init(year, month, day, hour, min, sec, 0);
        }
    }

    /**
     * @return Used calendar.
     * @deprecated use {@link #getMeterCalendar} instead.
     */
    public final Calendar getCalendar() {
        return meterCalendar;
    }

    /**
     * @return Used local calendar.
     */
    public final Calendar getLocalCalendar() {
        long meterTime = meterCalendar.getTime().getTime();
        Calendar local = Calendar.getInstance();
        int diff = meterCalendar.getTimeZone().getRawOffset()
                - local.getTimeZone().getRawOffset();
        long localtime = meterTime + diff;
        local.setTimeInMillis(localtime);
        // If meter is not use daylight saving time.
        if (!meterCalendar.getTimeZone().useDaylightTime()) {
            local.add(Calendar.HOUR_OF_DAY, -1);
        }
        return local;
    }

    /**
     * @return Used meter calendar.
     */
    public final Calendar getMeterCalendar() {
        return meterCalendar;
    }

    /**
     * @param value
     *            Used meter calendar.
     */
    public final void setMeterCalendar(final Calendar value) {
        meterCalendar = value;
    }

    /**
     * @return Used date time value.
     * @deprecated use {@link #getLocalCalendar} instead.
     */
    @Deprecated
    public final java.util.Date getValue() {
        return meterCalendar.getTime();
    }

    /**
     * Set date time value.
     * 
     * @param forvalue
     *            Used date time value.
     * @deprecated use {@link #getLocalCalendar} instead.
     */
    @Deprecated
    public final void setValue(final java.util.Date forvalue) {
        meterCalendar.setTime(forvalue);
    }

    /**
     * Set date time value.
     * 
     * @param forvalue
     *            Used date time value.
     * @param forDeviation
     *            Used deviation.
     * @deprecated use {@link #setMeterCalendar} instead.
     */
    @Deprecated
    public final void setValue(final java.util.Date forvalue,
            final int forDeviation) {
        meterCalendar = Calendar.getInstance(getTimeZone(forDeviation, true));
        meterCalendar.setTime(forvalue);
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
     * @return Deviation is time from current time zone to UTC time.
     */
    public final int getDeviation() {
        return -meterCalendar.getTimeZone().getRawOffset() / 60000;
    }

    /**
     * @param forValue
     *            Deviation is time from current time zone to UTC time.
     * @deprecated use {@link #setMeterCalendar} instead.
     */
    @Deprecated
    public final void setDeviation(final int forValue) {
        meterCalendar = Calendar.getInstance(getTimeZone(forValue, true));
    }

    /**
     * @return Clock status.
     */
    public final java.util.Set<ClockStatus> getStatus() {
        return status;
    }

    /**
     * @param forValue
     *            Clock status.
     */
    public final void setStatus(final java.util.Set<ClockStatus> forValue) {
        status = forValue;
    }

    public String toFormatString() {
        SimpleDateFormat sd = new SimpleDateFormat();
        int pos;
        if (skip.size() != 0) {
            String dateSeparator = null;
            String timeSeparator = ":";
            // Separate date and time parts.
            List<String> tmp = GXCommon.split(sd.toPattern(), " ");
            List<String> shortDatePattern = new ArrayList<String>();
            List<String> shortTimePattern = new ArrayList<String>();
            // Find date time separator.
            char separator = 0;
            for (char it : tmp.get(0).toCharArray()) {
                if (dateSeparator == null && !Character.isDigit(it)) {
                    dateSeparator = String.valueOf(it);
                } else if (!Character.isLetter(it)) {
                    separator = it;
                    break;
                }
            }
            String sep = String.valueOf(separator);
            shortDatePattern.addAll(GXCommon.split(tmp.get(0), sep));
            shortTimePattern.addAll(GXCommon.split(tmp.get(1), ":"));
            if (!shortTimePattern.contains("ss")) {
                shortTimePattern.add("ss");
            }
            if (this instanceof GXTime) {
                shortDatePattern.clear();
            } else {
                if (skip.contains(DateTimeSkips.YEAR)) {
                    pos = shortDatePattern.indexOf("yyyy");
                    shortDatePattern.set(pos, "*");
                }
                if (skip.contains(DateTimeSkips.MONTH)) {
                    pos = shortDatePattern.indexOf("M");
                    shortDatePattern.set(pos, "*");
                }
                if (skip.contains(DateTimeSkips.DAY)) {
                    pos = shortDatePattern.indexOf("d");
                    shortDatePattern.set(pos, "*");
                }
            }
            if (this instanceof GXDate) {
                shortTimePattern.clear();
            } else {
                if (skip.contains(DateTimeSkips.HOUR)) {
                    pos = shortTimePattern.indexOf("h");
                    if (pos == -1) {
                        pos = shortTimePattern.indexOf("H");
                    }
                    shortTimePattern.set(pos, "*");
                }
                if (skip.contains(DateTimeSkips.MINUTE)) {
                    pos = shortTimePattern.indexOf("mm");
                    shortTimePattern.set(pos, "*");
                }
                if (skip.contains(DateTimeSkips.SECOND)
                        || (shortTimePattern.size() == 1 && getLocalCalendar()
                                .get(Calendar.SECOND) == 0)) {
                    pos = shortTimePattern.indexOf("ss");
                    shortTimePattern.set(pos, "*");
                }
            }
            String format = null;
            if (!shortDatePattern.isEmpty()) {
                format = String.join(dateSeparator,
                        shortDatePattern.toArray(new String[0]));
            }
            if (!shortTimePattern.isEmpty()) {
                if (format != null) {
                    format += " ";
                } else {
                    format = "";
                }
                format += String.join(timeSeparator,
                        shortTimePattern.toArray(new String[0]));
            }
            if (format == "H") {
                return String
                        .valueOf(getLocalCalendar().get(Calendar.HOUR_OF_DAY));
            }
            if (format == null) {
                return "";
            }
            sd = new SimpleDateFormat(format);
            return sd.format(getLocalCalendar().getTime());
        }
        return sd.format(getLocalCalendar().getTime());
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
                String sep = String.valueOf(separator);
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
                return sd.format(getLocalCalendar().getTime());
            }
        }
        return sd.format(getLocalCalendar().getTime());
    }

    /**
     * Get difference between given time and run time in ms.
     * 
     * @param start
     *            Start date time.
     * @param to
     *            Compared time.
     * @return Difference in milliseconds.
     */
    public static long getDifference(final Calendar start,
            final GXDateTime to) {
        long diff = 0;
        Calendar cal = to.getLocalCalendar();
        // Compare seconds.
        if (!to.getSkip().contains(DateTimeSkips.SECOND)) {
            if (start.get(Calendar.SECOND) < cal.get(Calendar.SECOND)) {
                diff += (cal.get(Calendar.SECOND) - start.get(Calendar.SECOND))
                        * 1000L;
            } else {
                diff -= (start.get(Calendar.SECOND) - cal.get(Calendar.SECOND))
                        * 1000L;
            }
        } else if (diff < 0) {
            diff = 60000 + diff;
        }
        // Compare minutes.
        if (!to.getSkip().contains(DateTimeSkips.MINUTE)) {
            if (start.get(Calendar.MINUTE) < cal.get(Calendar.MINUTE)) {
                diff += (cal.get(Calendar.MINUTE) - start.get(Calendar.MINUTE))
                        * 60000L;
            } else {
                diff -= (start.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE))
                        * 60000L;
            }
        } else if (diff < 0) {
            diff = 60 * 60000 + diff;
        }
        // Compare hours.
        if (!to.getSkip().contains(DateTimeSkips.HOUR)) {
            if (start.get(Calendar.HOUR_OF_DAY) < cal
                    .get(Calendar.HOUR_OF_DAY)) {
                diff += (cal.get(Calendar.HOUR_OF_DAY)
                        - start.get(Calendar.HOUR_OF_DAY)) * 60 * 60000L;
            } else {
                diff -= (start.get(Calendar.HOUR_OF_DAY)
                        - cal.get(Calendar.HOUR_OF_DAY)) * 60 * 60000L;
            }
        } else if (diff < 0) {
            diff = 60 * 60000 + diff;
        }
        // Compare days.
        if (!to.getSkip().contains(DateTimeSkips.DAY)) {
            if (start.get(Calendar.DAY_OF_MONTH) < cal
                    .get(Calendar.DAY_OF_MONTH)) {
                diff += (cal.get(Calendar.DAY_OF_MONTH)
                        - start.get(Calendar.DAY_OF_MONTH)) * 24 * 60 * 60000;
            } else if (start.get(Calendar.DAY_OF_MONTH) != cal
                    .get(Calendar.DAY_OF_MONTH)) {
                if (!to.getSkip().contains(DateTimeSkips.DAY)) {
                    diff += (cal.get(Calendar.DAY_OF_MONTH)
                            - start.get(Calendar.DAY_OF_MONTH)) * 24 * 60
                            * 60000L;
                } else {
                    diff = ((GXCommon.daysInMonth(start.get(Calendar.YEAR),
                            start.get(Calendar.MONTH))
                            - start.get(Calendar.DAY_OF_MONTH)
                            + cal.get(Calendar.DAY_OF_MONTH)) * 24 * 60
                            * 60000L) + diff;
                }
            }
        } else if (diff < 0) {
            diff = 24 * 60 * 60000 + diff;
        }
        // Compare months.
        if (!to.getSkip().contains(DateTimeSkips.MONTH)) {
            if (start.get(Calendar.MONTH) < cal.get(Calendar.MONTH)) {
                for (int m = start.get(Calendar.MONTH); m != cal
                        .get(Calendar.MONTH); ++m) {
                    diff += GXCommon.daysInMonth(start.get(Calendar.YEAR), m)
                            * 24 * 60 * 60000L;
                }
            } else {
                for (int m = cal.get(Calendar.MONTH); m != start
                        .get(Calendar.MONTH); ++m) {
                    diff += -GXCommon.daysInMonth(start.get(Calendar.YEAR), m)
                            * 24 * 60 * 60000L;
                }
            }
        } else if (diff < 0) {
            diff = GXCommon.daysInMonth(start.get(Calendar.YEAR),
                    start.get(Calendar.MONTH)) * 24 * 60 * 60000L + diff;
        }
        return diff;
    }

    /**
     * Convert deviation to time zone.
     * 
     * @param deviation
     *            Used deviation.
     * @param dst
     *            Is daylight saving time used.
     * @return Time zone.
     */
    public static TimeZone getTimeZone(final int deviation, final boolean dst) {
        // Return current time zone if time zone is not used.
        if (deviation == 0x8000 || deviation == -32768) {
            return Calendar.getInstance().getTimeZone();
        }
        if (dst) {
            TimeZone tz = Calendar.getInstance().getTimeZone();
            String[] ids = TimeZone.getAvailableIDs(deviation * 60000);
            tz = null;
            for (int pos = 0; pos != ids.length; ++pos) {
                tz = TimeZone.getTimeZone(ids[pos]);
                if (tz.observesDaylightTime()
                        && tz.getRawOffset() / 60000 == deviation) {
                    break;
                }
            }
            return tz;
        }
        String str;
        DecimalFormat df = new DecimalFormat("00");
        String tmp =
                df.format(deviation / 60) + ":" + df.format(deviation % 60);
        if (deviation == 0) {
            str = "GMT";
        } else if (deviation > 0) {
            str = "GMT+" + tmp;
        } else {
            str = "GMT" + tmp;
        }
        return TimeZone.getTimeZone(str);
    }
}