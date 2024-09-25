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

import gurux.dlms.objects.enums.RestrictionType;

/**
 * Compact data and push object restriction values.
 */
public class GXDLMSRestriction {
    /**
     * Restriction type.
     */
    private RestrictionType type;

    /**
     * From date or entry.
     */
    private Object from;

    /**
     * To date or entry.
     */
    private Object to;

    public final RestrictionType getType() {
        return type;
    }

    public final void setType(final RestrictionType value) {
        type = value;
    }

    public final Object getFrom() {
        return from;
    }

    public final void setFrom(final Object value) {
        from = value;
    }

    public final Object getTo() {
        return to;
    }

    public final void setTo(final Object value) {
        to = value;
    }
}