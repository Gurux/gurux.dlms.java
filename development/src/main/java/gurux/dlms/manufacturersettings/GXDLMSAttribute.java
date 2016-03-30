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

package gurux.dlms.manufacturersettings;

import gurux.dlms.enums.DataType;

public class GXDLMSAttribute extends GXDLMSAttributeSettings {
    /**
     * Constructor.
     * 
     * @param index
     *            Attribute index.
     */
    public GXDLMSAttribute(final int index) {
        this(index, DataType.NONE, DataType.NONE, 0);
    }

    /**
     * Constructor.
     */
    public GXDLMSAttribute() {
        this(0, DataType.NONE, DataType.NONE, 0);
    }

    /**
     * Constructor.
     * 
     * @param index
     *            Attribute index.
     * @param uiType
     *            UI Data type.
     */
    public GXDLMSAttribute(final int index, final DataType uiType) {
        this(index, DataType.NONE, uiType, 0);
    }

    /**
     * Constructor.
     * 
     * @param index
     *            Attribute index.
     * @param type
     *            Data type.
     * @param uiType
     *            UI data type.
     */
    public GXDLMSAttribute(final int index, final DataType type,
            final DataType uiType) {
        this(index, type, uiType, 0);
    }

    /**
     * Constructor.
     * 
     * @param index
     *            Attribute index.
     * @param type
     *            Data type.
     * @param uiType
     *            UI data type.
     * @param order
     *            Order.
     */
    public GXDLMSAttribute(final int index, final DataType type,
            final DataType uiType, final int order) {
        setIndex(index);
        setType(type);
        setUIType(uiType);
        setOrder(order);
    }
}