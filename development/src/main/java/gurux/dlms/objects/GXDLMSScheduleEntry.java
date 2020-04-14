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

import java.util.HashSet;
import java.util.Set;

import gurux.dlms.GXDate;
import gurux.dlms.GXTime;
import gurux.dlms.objects.enums.Weekdays;

/**
 * Executed scripts.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSScheduleEntry {

    public GXDLMSScheduleEntry() {
        execWeekdays = new HashSet<Weekdays>();
    }

    /**
     * Schedule entry index.
     */
    private int index;

    /**
     * Is Schedule entry enabled.
     */
    private boolean enable;

    /**
     * Logical name of the Script table object.
     */
    private String logicalName;

    /**
     * Script identifier of the script to be executed.
     */
    private int scriptSelector;

    /**
    *
    */
    private GXTime switchTime;

    /**
     * Defines a period in minutes, in which an entry shall be processed after
     * power fail.
     */
    private int validityWindow;

    /**
     * Days of the week on which the entry is valid.
     */
    private Set<Weekdays> execWeekdays;

    /**
     * Perform the link to the IC Special days table, day_id.
     */
    private String execSpecDays;

    /**
     * Date starting period in which the entry is valid.
     */
    private GXDate beginDate;

    /**
     * Date starting period in which the entry is valid.
     */
    private GXDate endDate;

    /**
     * Get schedule entry index.
     * 
     * @return Entry index.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Set schedule entry index.
     * 
     * @param value
     *            Entry index.
     */
    public final void setIndex(final int value) {
        index = value;
    }

    /**
     * Is Schedule entry enabled.
     * 
     * @return True, if schedule entry is enabled.
     */
    public final boolean getEnable() {
        return enable;
    }

    /**
     * Enable schedule entry.
     * 
     * @param value
     *            Is Schedule entry enabled.
     */
    public final void setEnable(final boolean value) {
        enable = value;
    }

    /**
     * Returns logical name of the Script table object.
     * 
     * @return Logical name of the Script table object.
     */
    public final String getLogicalName() {
        return logicalName;
    }

    public final void setLogicalName(final String value) {
        logicalName = value;
    }

    /**
     * Get script identifier of the script to be executed.
     * 
     * @return Script identifier.
     */
    public final int getScriptSelector() {
        return scriptSelector;
    }

    /**
     * Set script identifier of the script to be executed.
     * 
     * @param value
     *            Script identifier.
     */
    public final void setScriptSelector(final int value) {
        scriptSelector = value;
    }

    /**
     * @return Switch time.
     */
    public final GXTime getSwitchTime() {
        return switchTime;
    }

    /**
     * @param value
     *            Switch time.
     */
    public final void setSwitchTime(final GXTime value) {
        switchTime = value;
    }

    /**
     * Defines a period in minutes, in which an entry shall be processed after
     * power fail.
     * 
     * @return Validity period in minutes.
     */
    public final int getValidityWindow() {
        return validityWindow;
    }

    /**
     * Defines a period in minutes, in which an entry shall be processed after
     * power fail.
     * 
     * @param value
     *            Validity period in minutes.
     */
    public final void setValidityWindow(final int value) {
        validityWindow = value;
    }

    /**
     * Get days of the week on which the entry is valid.
     * 
     * @return Days of the week on which the entry is valid.
     */
    public final Set<Weekdays> getExecWeekdays() {
        return execWeekdays;
    }

    /**
     * Set days of the week on which the entry is valid.
     * 
     * @param value
     *            Days of the week on which the entry is valid.
     */
    public final void setExecWeekdays(final Set<Weekdays> value) {
        execWeekdays = value;
    }

    /**
     * Perform the link to the IC Special days table..
     * 
     * @return day_id.
     */
    public final String getExecSpecDays() {
        return execSpecDays;
    }

    /**
     * Perform the link to the IC Special days table.
     * 
     * @param value
     *            day_id
     */
    public final void setExecSpecDays(final String value) {
        execSpecDays = value;
    }

    /**
     * Date starting period in which the entry is valid.
     * 
     * @return Begin date.
     */
    public final GXDate getBeginDate() {
        return beginDate;
    }

    /**
     * Date starting period in which the entry is valid.
     * 
     * @param value
     *            Begin date.
     */
    public final void setBeginDate(final GXDate value) {
        beginDate = value;
    }

    /**
     * Get date starting period in which the entry is valid.
     * 
     * @return End date.
     */
    public final GXDate getEndDate() {
        return endDate;
    }

    /**
     * Set date starting period in which the entry is valid.
     * 
     * @param value
     *            End date.
     */
    public final void setEndDate(final GXDate value) {
        endDate = value;
    }
}
