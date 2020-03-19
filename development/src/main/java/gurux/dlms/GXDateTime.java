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

package gurux.dlms;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DateTimeExtraInfo;
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
     * Date time extra information.
     */
    private java.util.Set<DateTimeExtraInfo> extra;

    /**
     * Day of week.
     */
    private int dayOfWeek = 0;

    /**
     * Constructor.
     */
    public GXDateTime() {
        skip = new HashSet<DateTimeSkips>();
        meterCalendar = Calendar.getInstance();
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
        extra = new HashSet<DateTimeExtraInfo>();
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
        extra = new HashSet<DateTimeExtraInfo>();
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
        extra = new HashSet<DateTimeExtraInfo>();
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Date value.
     */
    public GXDateTime(final GXDateTime value) {
        skip = new HashSet<DateTimeSkips>();
        meterCalendar = Calendar.getInstance();
        meterCalendar = value.getMeterCalendar();
        status = new HashSet<ClockStatus>();
        extra = new HashSet<DateTimeExtraInfo>();
        if (value != null) {
            skip.addAll(value.getSkip());
            status.addAll(value.getStatus());
            extra.addAll(value.getExtra());
        }
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
    @Deprecated
    @SuppressWarnings("squid:S00107")
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
        extra = new HashSet<DateTimeExtraInfo>();
        status = new HashSet<ClockStatus>();
        status.add(ClockStatus.OK);
        if (y < 1 || y == 0xFFFF) {
            skip.add(DateTimeSkips.YEAR);
            Calendar tm = Calendar.getInstance();
            y = tm.get(Calendar.YEAR);
        }
        if (m == 0xFE) {
            m = 0;
            extra.add(DateTimeExtraInfo.DST_BEGIN);
        } else if (m == 0xFD) {
            m = 0;
            extra.add(DateTimeExtraInfo.DST_END);
        } else if (m < 1 || m > 12) {
            skip.add(DateTimeSkips.MONTH);
            m = 0;
        } else {
            m -= 1;
        }

        if (d == 0xFE) {
            d = 1;
            extra.add(DateTimeExtraInfo.LAST_DAY);
        } else if (d == 0xFD) {
            d = 1;
            extra.add(DateTimeExtraInfo.LAST_DAY2);
        } else if (d == -1 || d == 0 || d > 31) {
            skip.add(DateTimeSkips.DAY);
            d = 1;
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

    private static boolean isNumeric(char value) {
        return value >= '0' && value <= '9';
    }

    /**
     * Constructor
     * 
     * @param value
     *            Date time value as a string.
     */
    public GXDateTime(final String value) {
        this(value, null);
    }

    /**
     * Constructor
     * 
     * @param value
     *            Date time value as a string.
     * @param locale
     *            Used locale.
     */
    public GXDateTime(final String value, final Locale locale) {
        if (skip == null) {
            skip = new HashSet<DateTimeSkips>();
        }
        if (extra == null) {
            extra = new HashSet<DateTimeExtraInfo>();
        }
        if (status == null) {
            status = new HashSet<ClockStatus>();
            status.add(ClockStatus.OK);
        }

        if (value != null) {
            SimpleDateFormat sd;
            if (locale != null) {
                if (locale == Locale.ROOT) {
                    sd = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                } else {
                    sd = (SimpleDateFormat) DateFormat.getDateTimeInstance(
                            DateFormat.SHORT, DateFormat.SHORT, locale);
                }
            } else {
                sd = new SimpleDateFormat();
            }
            StringBuilder format = new StringBuilder();
            format.append(sd.toPattern());
            remove(format);
            String v = value;
            if (value.indexOf("BEGIN") != -1) {
                extra.add(DateTimeExtraInfo.DST_BEGIN);
                v = v.replace("BEGIN", "01");
            }
            if (value.indexOf("END") != -1) {
                extra.add(DateTimeExtraInfo.DST_END);
                v = v.replace("END", "01");
            }
            if (value.indexOf("LASTDAY2") != -1) {
                extra.add(DateTimeExtraInfo.LAST_DAY2);
                v = v.replace("LASTDAY2", "01");
            }
            if (value.indexOf("LASTDAY") != -1) {
                extra.add(DateTimeExtraInfo.LAST_DAY);
                v = v.replace("LASTDAY", "01");
            }
            if (value.indexOf('*') != -1) {
                int lastFormatIndex = -1;
                for (int pos = 0; pos < v.length(); ++pos) {
                    char c = v.charAt(pos);
                    if (!isNumeric(c)) {
                        if (c == '*') {
                            int end = lastFormatIndex + 1;
                            c = format.charAt(end);
                            while (end + 1 < format.length()
                                    && format.charAt(end) == c) {
                                ++end;
                            }
                            v = v.substring(0, pos) + "1"
                                    + v.substring(pos + 1);
                            String tmp = format
                                    .substring(lastFormatIndex + 1, end).trim();
                            if (tmp.startsWith("y")) {
                                skip.add(DateTimeSkips.YEAR);
                            } else if (tmp.equals("M") || tmp.equals("MM")) {
                                skip.add(DateTimeSkips.MONTH);
                            } else if (tmp.equals("dd") || tmp.equals("d")) {
                                skip.add(DateTimeSkips.DAY);
                            } else if (tmp.equals("h") || tmp.equals("hh")
                                    || tmp.equals("HH") || tmp.equals("H")
                                    || tmp.equals("a")) {
                                skip.add(DateTimeSkips.HOUR);
                                int pos2 = format.indexOf("a");
                                if (pos2 != -1) {
                                    format.replace(pos2, pos2 + 1, "");
                                }
                            } else if (tmp.equals("mm") || tmp.equals("m")) {
                                skip.add(DateTimeSkips.MINUTE);
                            } else if (tmp.equals("ss") || tmp.equals("s")) {
                                skip.add(DateTimeSkips.SECOND);
                            } else if (!tmp.isEmpty() && !tmp.equals("G")) {
                                throw new IllegalArgumentException(
                                        "Invalid date time format.");
                            }
                        } else {
                            lastFormatIndex = format.indexOf(String.valueOf(c),
                                    lastFormatIndex + 1);
                        }
                    }
                }
            }
            meterCalendar = Calendar.getInstance();
            try {
                if (locale != null && locale != Locale.ROOT) {
                    sd = new SimpleDateFormat(format.toString().trim(), locale);
                } else {
                    sd = new SimpleDateFormat(format.toString().trim());
                }
                meterCalendar.setTime(sd.parse(v));
                getSkip().add(DateTimeSkips.MILLISECOND);
            } catch (java.text.ParseException e) {
                try {
                    if (!getSkip().contains(DateTimeSkips.SECOND)) {
                        int index = format.indexOf("mm");
                        if (index != -1) {
                            String sep = format.substring(index - 1, index);
                            format.replace(index, index + 2, "mm" + sep + "ss");
                        }
                    }
                    sd = new SimpleDateFormat(format.toString().trim());
                    meterCalendar.setTime(sd.parse(v));
                    getSkip().add(DateTimeSkips.MILLISECOND);
                } catch (java.text.ParseException e1) {
                    try {
                        if (!getSkip().contains(DateTimeSkips.MILLISECOND)) {
                            int index = format.indexOf("ss");
                            if (index != -1) {
                                String sep = format.substring(index - 1, index);
                                format.replace(index, index + 2,
                                        "ss" + sep + "SSS");
                            }
                        }
                        sd = new SimpleDateFormat(format.toString().trim());
                        meterCalendar.setTime(sd.parse(v));
                    } catch (java.text.ParseException e2) {
                        throw new IllegalArgumentException(e2);
                    } catch (Exception e2) {
                        throw new IllegalArgumentException(e);
                    }
                } catch (Exception e1) {
                    throw new IllegalArgumentException(e1);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

    }

    /**
     * @return Used calendar.
     * @deprecated use {@link #getMeterCalendar} instead.
     */
    @Deprecated
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
        // If meter is not use daylight saving time and client is.
        if (!meterCalendar.getTimeZone()
                .inDaylightTime(meterCalendar.getTime())) {
            if (local.getTimeZone().inDaylightTime(local.getTime())) {
                local.add(Calendar.HOUR_OF_DAY, -1);
            }
        } else {
            if (!local.getTimeZone().inDaylightTime(local.getTime())) {
                local.add(Calendar.HOUR_OF_DAY, 1);
            }
        }
        return local;
    }

    /**
     * @return Used meter calendar.
     */
    @SuppressWarnings("squid:S4144")
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
     * @return Day of week.
     */
    public final int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param forValue
     *            Day of week.
     */
    public final void setDayOfWeek(final int forValue) {
        dayOfWeek = forValue;
    }

    /**
     * @return Deviation is time from current time zone to UTC time.
     */
    public final int getDeviation() {
        return -((meterCalendar.get(Calendar.ZONE_OFFSET)
                + meterCalendar.get(Calendar.DST_OFFSET)) / 60000);
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

    private void remove(final StringBuilder format) {
        if (this instanceof GXDate) {
            remove(format, "HH", true);
            remove(format, "H", true);
            remove(format, "hh", true);
            remove(format, "h", true);
            remove(format, "mm", true);
            remove(format, "ss", true);
            remove(format, "m", true);
            remove(format, "a", true);
        } else if (this instanceof GXTime) {
            remove(format, "yyyy", true);
            remove(format, "yy", true);
            remove(format, "MM", true);
            remove(format, "M", true);
            remove(format, "dd", true);
            remove(format, "d", true);
        }
        // Trim
        String tmp = format.toString();
        format.setLength(0);
        format.append(tmp.trim());
    }

    public String toFormatString() {
        return toFormatString(null);
    }

    public String toFormatString(final Locale locale) {
        StringBuilder format = new StringBuilder();
        SimpleDateFormat sd;
        if (locale != null) {
            if (locale == Locale.ROOT) {
                // Hour is save in 24 format.
                sd = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            } else {
                sd = (SimpleDateFormat) DateFormat.getDateTimeInstance(
                        DateFormat.SHORT, DateFormat.SHORT, locale);
            }
        } else {
            sd = new SimpleDateFormat();
        }
        // GyMdkHmsSEDFwWahKzZYuXL
        if (!getSkip().isEmpty()) {
            // Separate date and time parts.
            format.append(sd.toPattern());
            remove(format);
            if (extra.contains(DateTimeExtraInfo.DST_BEGIN)) {
                replace(format, "MM", "!");
                replace(format, "M", "!");
            } else if (extra.contains(DateTimeExtraInfo.DST_END)) {
                replace(format, "MM", "#");
                replace(format, "M", "#");
            } else if (extra.contains(DateTimeExtraInfo.LAST_DAY)) {
                replace(format, "dd", "%");
                replace(format, "d", "%");
            } else if (extra.contains(DateTimeExtraInfo.LAST_DAY2)) {
                replace(format, "dd", "?");
                replace(format, "d", "?");
            }
            if (getSkip().contains(DateTimeSkips.YEAR)) {
                replace(format, "yyyy");
                replace(format, "yy");
                replace(format, "y");
            }
            if (getSkip().contains(DateTimeSkips.MONTH)) {
                replace(format, "MM");
                replace(format, "M");
            }
            if (getSkip().contains(DateTimeSkips.DAY)) {
                replace(format, "dd");
                replace(format, "d");
            }
            if (getSkip().contains(DateTimeSkips.HOUR)) {
                replace(format, "HH");
                replace(format, "H");
                replace(format, "hh");
                replace(format, "h");
                remove(format, "a", false);
            }
            if (getSkip().contains(DateTimeSkips.MILLISECOND)) {
                replace(format, "SSS");
            } else {
                int index = format.indexOf("ss");
                if (index != -1) {
                    String sep = format.substring(index - 1, index);
                    format.replace(index, index + 2, "ss" + sep + "SSS");
                }
            }
            if (getSkip().contains(DateTimeSkips.SECOND)) {
                replace(format, "ss");
            } else if (format.indexOf("ss") == -1) {
                int index = format.indexOf("mm");
                if (index != -1) {
                    String sep = format.substring(index - 1, index);
                    format.replace(index, index + 2, "mm" + sep + "ss");
                }
            }
            if (getSkip().contains(DateTimeSkips.MINUTE)) {
                replace(format, "mm");
                replace(format, "m");
            }
            if (locale != null && locale != Locale.ROOT) {
                sd = new SimpleDateFormat(format.toString().trim(), locale);
            } else {
                sd = new SimpleDateFormat(format.toString().trim());
            }
            return sd.format(getLocalCalendar().getTime()).replace("!", "BEGIN")
                    .replace("#", "END").replace("%", "LASTDAY")
                    .replace("?", "LASTDAY2");
        }
        return sd.format(getLocalCalendar().getTime());
    }

    private void remove(final StringBuilder value, final String tag,
            final boolean removeSeparator) {
        int pos = value.indexOf(tag);
        if (pos != -1) {
            int len = pos + tag.length();
            if (pos != 0 && removeSeparator) {
                --pos;
            }
            value.replace(pos, len, "");
        }
    }

    private void replace(final StringBuilder value, final String tag) {
        replace(value, tag, "*");
    }

    private void replace(final StringBuilder value, final String tag,
            final String newValue) {
        int pos = value.indexOf(tag);
        if (pos != -1) {
            int len = pos + tag.length();
            value.replace(pos, len, newValue);
        }
    }

    @Override
    public final String toString() {
        StringBuilder format = new StringBuilder();
        SimpleDateFormat sd = new SimpleDateFormat();
        if (!getSkip().isEmpty()) {
            // Separate date and time parts.
            format.append(sd.toPattern());
            remove(format);
            if (getSkip().contains(DateTimeSkips.YEAR)) {
                remove(format, "yyyy", true);
                remove(format, "yy", true);
                remove(format, "y", true);
            }
            if (getSkip().contains(DateTimeSkips.MONTH)) {
                remove(format, "M", true);
            }
            if (getSkip().contains(DateTimeSkips.DAY)) {
                remove(format, "d", true);
            }
            if (getSkip().contains(DateTimeSkips.HOUR)) {
                remove(format, "HH", true);
                remove(format, "H", true);
                remove(format, "h", true);
                remove(format, "a", true);
            }
            if (getSkip().contains(DateTimeSkips.MILLISECOND)) {
                remove(format, "SSS", true);
            } else {
                int index = format.indexOf("ss");
                if (index != -1) {
                    String sep = format.substring(index - 1, index);
                    format.replace(index, index + 2, "ss" + sep + "SSS");
                }
            }
            if (getSkip().contains(DateTimeSkips.SECOND)) {
                remove(format, "ss", true);
            } else {
                int index = format.indexOf("mm");
                if (index != -1) {
                    String sep = format.substring(index - 1, index);
                    format.replace(index, index + 2, "mm" + sep + "ss");
                }
            }
            if (getSkip().contains(DateTimeSkips.MINUTE)) {
                remove(format, "mm", true);
                remove(format, "m", true);
            }
            sd = new SimpleDateFormat(format.toString().trim());
            return sd.format(getLocalCalendar().getTime());
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
        TimeZone tz = Calendar.getInstance().getTimeZone();
        if (dst) {
            // If meter is in same time zone than meter reading application.
            if (tz.observesDaylightTime()
                    && tz.getRawOffset() / 60000 == deviation - 60) {
                return tz;
            }
            String[] ids = TimeZone.getAvailableIDs((deviation - 60) * 60000);
            tz = null;
            for (int pos = 0; pos != ids.length; ++pos) {
                tz = TimeZone.getTimeZone(ids[pos]);
                if (tz.observesDaylightTime()
                        && tz.getRawOffset() / 60000 == deviation - 60) {
                    break;
                }
                tz = null;
            }
            if (tz != null) {
                return tz;
            }
        }
        if (tz != null && !tz.observesDaylightTime()
                && tz.getRawOffset() / 60000 == deviation) {
            return tz;
        }
        String str;
        DecimalFormat df = new DecimalFormat("00");
        String tmp = df.format(deviation / 60) + ":"
                + df.format(Math.abs(deviation) % 60);
        if (deviation == 0) {
            str = "GMT";
        } else if (deviation > 0) {
            str = "GMT+" + tmp;
        } else {
            str = "GMT" + tmp;
        }
        return TimeZone.getTimeZone(str);
    }

    /**
     * Get date time from Epoch time.
     * 
     * @param unixTime
     *            Unix time.
     * @return Date and time.
     */
    public static GXDateTime fromUnixTime(final long unixTime) {
        return new GXDateTime(new Date(unixTime * 1000));
    }

    /**
     * Convert date time to Epoch time.
     * 
     * @param date
     *            Date and time.
     * @return Unix time.
     */
    public static long toUnixTime(final java.util.Date date) {
        return date.getTime() / 1000;
    }

    /**
     * Convert date time to Epoch time.
     * 
     * @param date
     *            Date and time.
     * @return Unix time.
     */
    public static long toUnixTime(final GXDateTime date) {
        return date.getLocalCalendar().getTime().getTime() / 1000;
    }

    /**
     * @return Date time extra information.
     */
    public java.util.Set<DateTimeExtraInfo> getExtra() {
        return extra;
    }

    /**
     * @param value
     *            Date time extra information.
     */
    public void setExtra(final java.util.Set<DateTimeExtraInfo> value) {
        extra = value;
    }
}