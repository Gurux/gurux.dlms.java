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

import gurux.dlms.enums.ObjectType;

public class GXDLMSObjectDefinition {
    private ObjectType objectType;
    private String logicalName;

    /**
     * @return Object type.
     * @deprecated use {@link #getObjectType} instead.
     */
    public final ObjectType getClassId() {
        return objectType;
    }

    /**
     * @param value
     *            Object type.
     * @deprecated use {@link #setObjectType} instead.
     */
    public final void setClassId(final ObjectType value) {
        objectType = value;
    }

    /**
     * @return Object type.
     */
    public final ObjectType getObjectType() {
        return objectType;
    }

    /**
     * @param value
     *            Object type.
     */
    public final void setObjectType(final ObjectType value) {
        objectType = value;
    }

    /**
     * @return Logical Name.
     */
    public final String getLogicalName() {
        return logicalName;
    }

    /**
     * @param value
     *            Logical Name.
     */
    public final void setLogicalName(final String value) {
        logicalName = value;
    }

    /*
     * Constructor
     */
    public GXDLMSObjectDefinition() {

    }

    /*
     * Constructor
     */
    public GXDLMSObjectDefinition(final ObjectType type, final String ln) {
        objectType = type;
        logicalName = ln;
    }

    @Override
    public final String toString() {
        return objectType.toString() + " " + logicalName;
    }
}
