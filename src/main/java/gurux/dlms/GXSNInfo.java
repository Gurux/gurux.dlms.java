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

import gurux.dlms.objects.GXDLMSObject;

/**
 * Server uses this class to find Short Name object and attribute index. This
 * class is reserved for internal use.
 * 
 * @author Gurux Ltd.
 */
class GXSNInfo {

    /**
     * Is attribute index or action index
     */
    private boolean action = false;

    /**
     * Attribute index.
     */
    private int index = 0;

    /**
     * COSEM object.
     */
    private GXDLMSObject item = null;

    /**
     * @return The index
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            The index to set
     */
    public final void setIndex(final int value) {
        index = value;
    }

    /**
     * @return Is action.
     */
    public final boolean isAction() {
        return action;
    }

    /**
     * @param value
     *            Is action.
     */
    public final void setAction(final boolean value) {
        action = value;
    }

    /**
     * @return The item
     */
    public final GXDLMSObject getItem() {
        return item;
    }

    /**
     * @param value
     *            The item to set
     */
    public final void setItem(final GXDLMSObject value) {
        item = value;
    }
}