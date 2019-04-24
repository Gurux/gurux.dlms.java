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

public class GXDLMSTarget {
    /**
     * Target COSEM object.
     */
    private GXDLMSObject target;

    /**
     * Attribute Index of COSEM object.
     */
    private int attributeIndex;

    /**
     * Data index of COSEM object. All targets don't use this.
     */
    private int dataIndex;

    /**
     * Data value of COSEM object. All targets don't use this.
     */
    private Object _value;

    public final GXDLMSObject getTarget() {
        return target;
    }

    public final void setTarget(final GXDLMSObject value) {
        target = value;
    }

    /**
     * @return Attribute Index of COSEM object.
     */
    public final int getAttributeIndex() {
        return attributeIndex;
    }

    /**
     * @param value
     *            Attribute Index of COSEM object.
     */
    public final void setAttributeIndex(final int value) {
        attributeIndex = value;
    }

    /**
     * @return Data index of COSEM object. All targets don't use this.
     */
    public final int getDataIndex() {
        return dataIndex;
    }

    /**
     * @param value
     *            Data index of COSEM object. All targets don't use this.
     */
    public final void setDataIndex(final int value) {
        dataIndex = value;
    }

    /**
     * @return Data value of COSEM object. All targets don't use this.
     */
    public final Object getValue() {
        return _value;
    }

    /**
     * @param value
     *            Data value of COSEM object. All targets don't use this.
     */
    public final void setValue(final Object value) {
        _value = value;
    }

    /**
     * Constructor
     */
    public GXDLMSTarget() {

    }

    /**
     * Constructor.
     * 
     * @param target
     *            Target object.
     * @param attributeIndex
     *            Attribute index.
     */
    public GXDLMSTarget(final GXDLMSObject target, final int attributeIndex) {
        setTarget(target);
        setAttributeIndex(attributeIndex);
    }

    /**
     * Constructor.
     * 
     * @param target
     *            Target object.
     * @param attributeIndex
     *            Attribute index.
     * @param dataIndex
     *            Data Index.
     */
    public GXDLMSTarget(final GXDLMSObject target, final int attributeIndex,
            final int dataIndex) {
        setTarget(target);
        setAttributeIndex(attributeIndex);
        setDataIndex(dataIndex);
    }
}