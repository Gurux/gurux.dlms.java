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

import gurux.dlms.GXDateTime;

public class GXDLMSSeasonProfile {
    private byte[] name;
    private GXDateTime start;
    private byte[] weekName;

    /**
     * Constructor.
     */
    public GXDLMSSeasonProfile() {

    }

    /**
     * Constructor.
     * 
     * @param forName
     *            name of season profile.
     * @param forStart
     *            Start time.
     * @param forWeekName
     *            Week name.
     */
    public GXDLMSSeasonProfile(final String forName, final GXDateTime forStart,
            final String forWeekName) {
        setName(forName);
        setStart(forStart);
        setWeekName(forWeekName);
    }

    /**
     * Constructor.
     * 
     * @param forName
     *            name of season profile.
     * @param forStart
     *            Start time.
     * @param forWeekName
     *            Week name.
     */
    public GXDLMSSeasonProfile(final String forName, final GXDateTime forStart,
            final byte[] forWeekName) {
        setName(forName);
        setStart(forStart);
        setWeekName(forWeekName);
    }

    /**
     * @return Name of season profile.
     */
    public final byte[] getName() {
        return name;
    }

    /**
     * @param value
     *            Name of season profile.
     */
    public final void setName(final String value) {
        if (value == null) {
            name = null;
        } else {
            name = value.getBytes();
        }
    }

    /**
     * @param value
     *            Name of season profile.
     */
    public final void setName(final byte[] value) {
        name = value;
    }

    /**
     * @return Season Profile start time.
     */
    public final GXDateTime getStart() {
        return start;
    }

    /**
     * @param value
     *            Season Profile start time.
     */
    public final void setStart(final GXDateTime value) {
        start = value;
    }

    /**
     * @param value
     *            Week name of season profile.
     */
    public final void setWeekName(final String value) {
        if (value == null) {
            weekName = null;
        } else {
            weekName = value.getBytes();
        }
    }

    /**
     * @return Week name of season profile.
     */
    public final byte[] getWeekName() {
        return weekName;
    }

    /**
     * @param value
     *            Week name of season profile.
     */
    public final void setWeekName(final byte[] value) {
        weekName = value;
    }

    @Override
    public final String toString() {
        return name + " " + start.toString();
    }
}