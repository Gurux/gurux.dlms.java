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

import gurux.dlms.objects.enums.Currency;

/**
 * Used currency.<br>
 * Online help:<br>
 * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
 */
public class GXCurrency {
    /**
     * Currency name.
     */
    private String name;
    /**
     * Currency scale.
     */
    private short scale;
    /**
     * Currency unit.
     */
    private Currency unit;

    /**
     * Constructor.
     */
    public GXCurrency() {
        unit = Currency.TIME;
    }

    /**
     * @return Currency name.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param value
     *            Currency name.
     */
    public final void setName(final String value) {
        name = value;
    }

    /**
     * @return Currency scale.
     */
    public final short getScale() {
        return scale;
    }

    /**
     * @param value
     *            Currency scale.
     */
    public final void setScale(final short value) {
        scale = value;
    }

    /**
     * @return Currency unit.
     */
    public final Currency getUnit() {
        return unit;
    }

    /**
     * @param value
     *            Currency unit.
     */
    public final void setUnit(final Currency value) {
        unit = value;
    }
}