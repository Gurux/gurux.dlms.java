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

/**
 * Online help:<br>
 * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
 */
public class GXChargePerUnitScaling {
    /**
     * Commodity scale.<br>
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private short commodityScale;
    /**
     * Price scale.<br>
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private short priceScale;

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Commodity scale.
     */
    public final short getCommodityScale() {
        return commodityScale;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Commodity scale.
     */
    public final void setCommodityScale(final short value) {
        commodityScale = value;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Price scale.
     */
    public final short getPriceScale() {
        return priceScale;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Price scale.
     */
    public final void setPriceScale(final short value) {
        priceScale = value;
    }
}