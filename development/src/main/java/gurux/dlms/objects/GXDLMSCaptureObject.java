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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/*
 * Capture Object.
 */
public class GXDLMSCaptureObject {
    /**
     * Index of DLMS object in the profile generic table.
     */
    private int attributeIndex;
    /**
     * Data index of DLMS object in the profile generic table.
     */
    private int dataIndex;
    /**
     * Restriction element for compact or push data.
     */
    private GXDLMSRestriction restriction;
    /**
     * Push data columns.
     */
    private List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns;

    /*
     * Constructor.
     */
    public GXDLMSCaptureObject() {
        restriction = new GXDLMSRestriction();
        columns = new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
    }

    /**
     * Constructor.
     * 
     * @param aIndex
     *            Attribute index.
     * @param dIndex
     *            Data index.
     */
    public GXDLMSCaptureObject(final int aIndex, final int dIndex) {
        this.attributeIndex = aIndex;
        this.dataIndex = dIndex;
        restriction = new GXDLMSRestriction();
        columns = new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
    }

    /**
     * @return Index of DLMS object in the profile generic table.
     */
    public final int getAttributeIndex() {
        return attributeIndex;
    }

    /**
     * @param value
     *            Index of DLMS object in the profile generic table.
     */
    public final void setAttributeIndex(final int value) {
        attributeIndex = value;
    }

    /**
     * @return Data index of DLMS object in the profile generic table.
     */
    public final int getDataIndex() {
        return dataIndex;
    }

    /**
     * @param value
     *            Data index of DLMS object in the profile generic table.
     */
    public final void setDataIndex(final int value) {
        dataIndex = value;
    }

    /**
     * @return Restriction element for compact or push data.
     */
    public final GXDLMSRestriction getRestriction() {
        return restriction;
    }

    /**
     * @param value
     *            Restriction element for compact or push data.
     */
    public final void setRestriction(final GXDLMSRestriction value) {
        restriction = value;
    }

    /**
     * @return Push data columns.
     */
    public final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> getColumns() {
        return columns;
    }

    /**
     * @param value
     *            Push data columns.
     */
    public final void setColumns(final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> value) {
        columns = value;
    }

}
