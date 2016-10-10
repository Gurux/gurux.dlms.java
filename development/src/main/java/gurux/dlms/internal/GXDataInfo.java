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

package gurux.dlms.internal;

import gurux.dlms.GXDLMSTranslatorStructure;
import gurux.dlms.enums.DataType;

/**
 * This class is used in DLMS data parsing.
 * 
 * @author Gurux Ltd.
 */
public class GXDataInfo {
    /**
     * Last array index.
     */
    private int index;
    /**
     * Items count in array.
     */
    private int count;
    /**
     * Object data type.
     */
    private DataType type = DataType.NONE;
    /**
     * Is data parsed to the end.
     */
    private boolean compleate = true;

    private GXDLMSTranslatorStructure xml;

    /**
     * @return Last array index.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            Last array index.
     */
    public final void setIndex(final int value) {
        this.index = value;
    }

    /**
     * @return Items count in array.
     */
    public final int getCount() {
        return count;
    }

    /**
     * @param value
     *            Items count in array.
     */
    public final void setCount(final int value) {
        count = value;
    }

    /**
     * @return Object data type.
     */
    public final DataType getType() {
        return type;
    }

    /**
     * @param value
     *            Object data type.
     */
    public final void setType(final DataType value) {
        type = value;
    }

    /**
     * @return Is data parsed to the end.
     */
    public final boolean isComplete() {
        return compleate;
    }

    /**
     * @param value
     *            Is data parsed to the end.
     */
    public final void setCompleate(final boolean value) {
        compleate = value;
    }

    public final void setXml(final GXDLMSTranslatorStructure value) {
        xml = value;
    }

    public final GXDLMSTranslatorStructure getXml() {
        return xml;
    }

    public final void clear() {
        index = 0;
        count = 0;
        type = DataType.NONE;
        compleate = true;
    }
}
