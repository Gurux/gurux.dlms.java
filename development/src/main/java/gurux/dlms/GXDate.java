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

public class GXDate extends GXDateTime {
    /**
     * Constructor.
     */
    public GXDate() {
        super();
        getSkip().add(DateTimeSkips.HOUR);
        getSkip().add(DateTimeSkips.MINUTE);
        getSkip().add(DateTimeSkips.SECOND);
        getSkip().add(DateTimeSkips.MILLISECOND);
    }

    /**
     * Constructor.
     * 
     * @param forvalue
     *            Date value.
     */
    public GXDate(final Date forvalue) {
        super(forvalue);
        getSkip().add(DateTimeSkips.HOUR);
        getSkip().add(DateTimeSkips.MINUTE);
        getSkip().add(DateTimeSkips.SECOND);
        getSkip().add(DateTimeSkips.MILLISECOND);
    }

    /**
     * Constructor.
     * 
     * @param forvalue
     *            Date value.
     */
    public GXDate(final GXDateTime forvalue) {
        super(forvalue.getLocalCalendar());
        getSkip().addAll(forvalue.getSkip());
        getSkip().add(DateTimeSkips.HOUR);
        getSkip().add(DateTimeSkips.MINUTE);
        getSkip().add(DateTimeSkips.SECOND);
        getSkip().add(DateTimeSkips.MILLISECOND);
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
     */
    public GXDate(final int year, final int month, final int day) {
        super(year, month, day, -1, -1, -1, -1);
    }

    /**
     * Constructor
     * 
     * @param value
     *            Date time value as a string.
     */
    public GXDate(final String value) {
        if (value != null) {
            int year = 2000, month = 1, day = 1;
            SimpleDateFormat sd = new SimpleDateFormat();
            // Separate date and time parts.
            List<String> tmp = GXCommon.split(sd.toPattern(), " ");
            List<String> shortDatePattern = new ArrayList<String>();
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

            List<String> values = GXCommon.split(value.trim(),
                    new char[] { separator, ':', ' ' });
            if (shortDatePattern.size() != values.size()
                    && shortDatePattern.size() != values.size()) {
                throw new IllegalArgumentException("Invalid Date.");
            }
            for (int pos = 0; pos != shortDatePattern.size(); ++pos) {
                boolean ignore = false;
                if ("*".compareTo(values.get(pos)) == 0) {
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
                    throw new IllegalArgumentException("Invalid Date pattern.");
                }
            }
            init(year, month, day, -1, -1, -1, -1);
        }
    }

}