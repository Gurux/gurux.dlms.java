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

import gurux.dlms.GXTime;

/**
 * Activity Calendar's Day Profile Action is defined on the standard.
 */
public class GXDLMSDayProfileAction {
    private GXTime time;
    private String logicalName;
    private int selector;

    /**
     * Constructor.
     */
    public GXDLMSDayProfileAction() {

    }

    /**
     * Constructor.
     * 
     * @param startTime
     *            Start time.
     * @param scriptLogicalName
     *            Logical name.
     * @param scriptSelector
     *            Script selector.
     */
    public GXDLMSDayProfileAction(final GXTime startTime,
            final String scriptLogicalName, final int scriptSelector) {
        setStartTime(startTime);
        setScriptLogicalName(scriptLogicalName);
        setScriptSelector(scriptSelector);
    }

    /**
     * @return Defines the time when the script is to be executed.
     */
    public final GXTime getStartTime() {
        return time;
    }

    /**
     * @param value
     *            Defines the time when the script is to be executed.
     */
    public final void setStartTime(final GXTime value) {
        time = value;
    }

    /**
     * @return Defines the logical name of the "Script table" object.
     */
    public final String getScriptLogicalName() {
        return logicalName;
    }

    /**
     * @param value
     *            Defines the logical name of the "Script table" object.
     */
    public final void setScriptLogicalName(final String value) {
        logicalName = value;
    }

    /**
     * @return Defines the script_identifier of the script to be executed.
     */
    public final int getScriptSelector() {
        return selector;
    }

    /**
     * @param value
     *            Defines the script_identifier of the script to be executed.
     */
    public final void setScriptSelector(final int value) {
        selector = value;
    }

    @Override
    public final String toString() {
        return time.toString() + " " + logicalName;
    }
}