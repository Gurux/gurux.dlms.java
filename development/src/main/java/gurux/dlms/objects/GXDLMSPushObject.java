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

public class GXDLMSPushObject {
    /**
     * Object type of push object.
     */
    private ObjectType type;
    /**
     * Logical name of push object.
     */
    private String logicalName;
    /**
     * Attribute index of push object.
     */
    private int attributeIndex;
    /**
     * Data Index of of push object.
     */
    private int dataIndex;

    /**
     * @return Object type of push object.
     */
    public final ObjectType getType() {
        return type;
    }

    /**
     * @param value
     *            Object type of push object.
     */
    public final void setType(final ObjectType value) {
        type = value;
    }

    /**
     * @return Logical name of push object.
     */
    public final String getLogicalName() {
        return logicalName;
    }

    /**
     * @param value
     *            Logical name of push object.
     */
    public final void setLogicalName(final String value) {
        logicalName = value;
    }

    /**
     * @return Attribute index of push object.
     */
    public final int getAttributeIndex() {
        return attributeIndex;
    }

    /**
     * @param value
     *            Attribute index of push object.
     */
    public final void setAttributeIndex(final int value) {
        attributeIndex = value;
    }

    /**
     * @return Data Index of of push object.
     */
    public final int getDataIndex() {
        return dataIndex;
    }

    /**
     * @param value
     *            Data Index of of push object.
     */
    public final void setDataIndex(final int value) {
        dataIndex = value;
    }

    /**
     * Constructor.
     */
    public GXDLMSPushObject() {
        type = ObjectType.NONE;
    }
}