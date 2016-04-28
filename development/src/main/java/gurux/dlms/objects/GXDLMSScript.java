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

import java.util.List;

/**
 * Script of script table.
 */
public class GXDLMSScript {
    /**
     * Script identifier.
     */
    private int id;

    /**
     * Script actions.
     */
    private List<GXDLMSScriptAction> actions;

    /**
     * Constructor.
     */
    public GXDLMSScript() {
        actions = new java.util.ArrayList<GXDLMSScriptAction>();
    }

    /**
     * @return Script identifier.
     */
    public final int getId() {
        return id;
    }

    /**
     * @param value
     *            Script identifier.
     */
    public final void setId(final int value) {
        id = value;
    }

    /**
     * @return Script actions.
     */
    public final List<GXDLMSScriptAction> getActions() {
        return actions;
    }
}
