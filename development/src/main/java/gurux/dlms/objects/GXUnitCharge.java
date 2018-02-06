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
public class GXUnitCharge {
    /**
     * Charge per unit scaling. <br>
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private GXChargePerUnitScaling chargePerUnitScaling;

    /**
     * Commodity.<br>
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private GXCommodity commodity;

    /**
     * Charge tables.<br>
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private GXChargeTable[] chargeTables;

    /**
     * Constructor.
     */
    public GXUnitCharge() {
        chargePerUnitScaling = new GXChargePerUnitScaling();
        commodity = new GXCommodity();
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Charge per unit scaling.
     */
    public final GXChargePerUnitScaling getChargePerUnitScaling() {
        return chargePerUnitScaling;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Charge per unit scaling.
     */
    public final void
            setChargePerUnitScaling(final GXChargePerUnitScaling value) {
        chargePerUnitScaling = value;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Commodity
     */
    public final GXCommodity getCommodity() {
        return commodity;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Commodity
     */
    public final void setCommodity(final GXCommodity value) {
        commodity = value;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Charge tables.
     */
    public final GXChargeTable[] getChargeTables() {
        return chargeTables;
    }

    /**
     * Online help:<br>
     * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Charge tables.
     */
    public final void setChargeTables(final GXChargeTable[] value) {
        chargeTables = value;
    }

}