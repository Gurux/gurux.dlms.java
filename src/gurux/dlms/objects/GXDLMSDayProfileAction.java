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
 Activity Calendar's Day Profile Action is defined on the standard.
*/
public class GXDLMSDayProfileAction
{
    private GXDateTime StartTime;
    private String ScriptLogicalName;
    private int ScriptSelector;    
    
    /** 
     Constructor.
    */
    public GXDLMSDayProfileAction()
    {

    }

    /** 
     Constructor.
    */
    public GXDLMSDayProfileAction(GXDateTime startTime, String scriptLogicalName, int scriptSelector)
    {
        setStartTime(startTime);
        setScriptLogicalName(scriptLogicalName);
        setScriptSelector(scriptSelector);
    }

    /** 
     Defines the time when the script is to be executed.
    */   
    public final GXDateTime getStartTime()
    {
        return StartTime;
    }
    public final void setStartTime(GXDateTime value)
    {
        StartTime = value;
    }

    /** 
     Defines the logical name of the "Script table" object;
    */
    public final String getScriptLogicalName()
    {
        return ScriptLogicalName;
    }
    public final void setScriptLogicalName(String value)
    {
        ScriptLogicalName = value;
    }

    /** 
     Defines the script_identifier of the script to be executed.
    */    
    public final int getScriptSelector()
    {
        return ScriptSelector;
    }
    public final void setScriptSelector(int value)
    {
        ScriptSelector = value;
    }
    
    @Override
    public String toString()
    {
        return StartTime.toString() + " " + ScriptLogicalName;
    }
}