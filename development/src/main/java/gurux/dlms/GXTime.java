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
import java.util.Date;
import java.util.List;

import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.internal.GXCommon;

public class GXTime extends GXDateTime {
    /**
     * Constructor.
     */
    public GXTime() {
        super();
        getSkip().add(DateTimeSkips.YEAR);
        getSkip().add(DateTimeSkips.MONTH);
        getSkip().add(DateTimeSkips.DAY);
        getSkip().add(DateTimeSkips.DAY_OF_WEEK);
    }

    /**
     * Constructor.
     * 
     * @param forvalue
     *            Date value.
     */
    public GXTime(final Date forvalue) {
        super(forvalue);
        getSkip().add(DateTimeSkips.YEAR);
        getSkip().add(DateTimeSkips.MONTH);
        getSkip().add(DateTimeSkips.DAY);
        getSkip().add(DateTimeSkips.DAY_OF_WEEK);
    }

    /**
     * Constructor.
     * 
     * @param forvalue
     *            Date value.
     */
    public GXTime(final GXDateTime forvalue) {
        super(forvalue.getLocalCalendar());
        getSkip().addAll(forvalue.getSkip());
        getSkip().add(DateTimeSkips.YEAR);
        getSkip().add(DateTimeSkips.MONTH);
        getSkip().add(DateTimeSkips.DAY);
        getSkip().add(DateTimeSkips.DAY_OF_WEEK);
    }

    /**
     * Constructor.
     * 
     * @param hour
     *            Used hour.
     * @param minute
     *            Used minute.
     * @param second
     *            Used second.
     * @param millisecond
     *            Used millisecond.
     */
    public GXTime(final int hour, final int minute, final int second,
            final int millisecond) {
        super(-1, -1, -1, hour, minute, second, millisecond);
    }

    /**
     * Constructor
     * 
     * @param value
     *            Date time value as a string.
     */
    public GXTime(final String value) {
        if (value != null) {
            int hour = 0, min = 0, sec = 0;
            SimpleDateFormat sd = new SimpleDateFormat();
            // Separate date and time parts.
            List<String> tmp = GXCommon.split(sd.toPattern(), " ");
            List<String> shortTimePattern = new ArrayList<String>();
            // Find date time separator.
            char separator = 0;
            for (char it : tmp.get(0).toCharArray()) {
                if (!Character.isLetter(it)) {
                    separator = it;
                    break;
                }
            }
            shortTimePattern.addAll(GXCommon.split(tmp.get(1), ":"));
            if (!shortTimePattern.contains("ss")) {
                shortTimePattern.add("ss");
            }
            List<String> values = GXCommon.split(value.trim(),
                    new char[] { separator, ':', ' ' });
            if (shortTimePattern.size() != values.size()) {
                throw new IllegalArgumentException("Invalid Time");
            }

            for (int pos = 0; pos != shortTimePattern.size(); ++pos) {
                boolean ignore = false;
                if ("*".compareTo(values.get(pos)) == 0) {
                    ignore = true;
                }
                String val = shortTimePattern.get(pos);
                if ("h".compareToIgnoreCase(val) == 0) {
                    if (ignore) {
                        hour = -1;
                    } else {
                        hour = Integer.parseInt(values.get(pos));
                    }
                } else if ("mm".compareToIgnoreCase(val) == 0) {
                    if (ignore) {
                        min = -1;
                    } else {
                        min = Integer.parseInt(values.get(pos));
                    }
                } else if ("ss".compareToIgnoreCase(val) == 0) {
                    if (ignore) {
                        sec = -1;
                    } else {
                        sec = Integer.parseInt(values.get(pos));
                    }
                } else {
                    throw new IllegalArgumentException("Invalid time pattern.");
                }
            }
            init(-1, -1, -1, hour, min, sec, -1);
        }
    }
}