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

import gurux.dlms.enums.DataType;
import gurux.dlms.objects.GXDLMSObject;

public class GXWriteItem {

    /**
     * Written object.
     */
    private GXDLMSObject target;

    /**
     * Attribute index to write.
     */
    private int index;

    /**
     * Data type to write.
     */
    private DataType dataType;

    /**
     * Parameter selector.
     */
    private int selector;

    /**
     * @return the selector
     */
    public final int getSelector() {
        return selector;
    }

    /**
     * @param value
     *            the selector to set
     */
    public final void setSelector(final int value) {
        selector = value;
    }

    /**
     * Optional parameters.
     */
    private Object parameters;

    /**
     * @return the data type
     */
    public final DataType getDataType() {
        return dataType;
    }

    /**
     * @param value
     *            the objectType to set
     */
    public final void setDataType(final DataType value) {
        dataType = value;
    }

    /**
     * @return the parameters
     */
    public final Object getParameters() {
        return parameters;
    }

    /**
     * @param value
     *            the parameters to set
     */
    public final void setParameters(final Object value) {
        parameters = value;
    }

    /**
     * @return the target.
     */
    public final GXDLMSObject getTarget() {
        return target;
    }

    /**
     * @param value
     *            the target to set.
     */
    public final void setTarget(final GXDLMSObject value) {
        target = value;
    }

    /**
     * @return the index.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            the index to set.
     */
    public final void setIndex(final int value) {
        index = value;
    }
}
