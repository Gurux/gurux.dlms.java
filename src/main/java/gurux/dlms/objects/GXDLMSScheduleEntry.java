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

/**
 * Executed scripts.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSScheduleEntry {

    /**
     * Schedule entry index.
     */
    private byte index;

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
    private byte scriptSelector;

    /**
    *
    */
    private GXDateTime switchTime;

    /**
     * Defines a period in minutes, in which an entry shall be processed after
     * power fail.
     */
    private byte validityWindow;

    /**
     * Days of the week on which the entry is valid.
     */
    private String execWeekdays;

    /**
     * Perform the link to the IC Special days table, day_id.
     */
    private String execSpecDays;

    /**
     * Date starting period in which the entry is valid.
     */
    private GXDateTime beginDate;

    /**
     * Date starting period in which the entry is valid.
     */
    private GXDateTime endDate;

    /**
     * Get schedule entry index.
     * 
     * @return Entry index.
     */
    public final byte getIndex() {
        return index;
    }

    /**
     * Set schedule entry index.
     * 
     * @param value
     *            Entry index.
     */
    public final void setIndex(final byte value) {
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
    public final byte getScriptSelector() {
        return scriptSelector;
    }

    /**
     * Set script identifier of the script to be executed.
     * 
     * @param value
     *            Script identifier.
     */
    public final void setScriptSelector(final byte value) {
        scriptSelector = value;
    }

    /**
     * @return Switch time.
     */
    public final GXDateTime getSwitchTime() {
        return switchTime;
    }

    /**
     * @param value
     *            Switch time.
     */
    public final void setSwitchTime(final GXDateTime value) {
        switchTime = value;
    }

    /**
     * Defines a period in minutes, in which an entry shall be processed after
     * power fail.
     * 
     * @return Validity period in minutes.
     */
    public final byte getValidityWindow() {
        return validityWindow;
    }

    /**
     * Defines a period in minutes, in which an entry shall be processed after
     * power fail.
     * 
     * @param value
     *            Validity period in minutes.
     */
    public final void setValidityWindow(final byte value) {
        validityWindow = value;
    }

    /**
     * Get days of the week on which the entry is valid.
     * 
     * @return Bit array of valid week days.
     */
    public final String getExecWeekdays() {
        return execWeekdays;
    }

    /**
     * Set days of the week on which the entry is valid.
     * 
     * @param value
     *            Bit array of valid week days.
     */
    public final void setExecWeekdays(final String value) {
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
    public final GXDateTime getBeginDate() {
        return beginDate;
    }

    /**
     * Date starting period in which the entry is valid.
     * 
     * @param value
     *            Begin date.
     */
    public final void setBeginDate(final GXDateTime value) {
        beginDate = value;
    }

    /**
     * Get date starting period in which the entry is valid.
     * 
     * @return End date.
     */
    public final GXDateTime getEndDate() {
        return endDate;
    }

    /**
     * Set date starting period in which the entry is valid.
     * 
     * @param value
     *            End date.
     */
    public final void setEndDate(final GXDateTime value) {
        endDate = value;
    }
}
