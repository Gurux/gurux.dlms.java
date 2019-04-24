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

import gurux.dlms.objects.enums.CreditCollectionConfiguration;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
 */
public class GXCreditChargeConfiguration {

    /**
     * Credit reference.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private String creditReference;

    /**
     * Charge reference.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private String chargeReference;

    /**
     * Collection configuration.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private CreditCollectionConfiguration collectionConfiguration;

    /**
     * Constructor.
     */
    public GXCreditChargeConfiguration() {
        collectionConfiguration = CreditCollectionConfiguration.NONE;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Credit reference.
     */
    public final String getCreditReference() {
        return creditReference;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Credit reference.
     */
    public final void setCreditReference(final String value) {
        creditReference = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Charge reference.
     */
    public final String getChargeReference() {
        return chargeReference;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Charge reference.
     */
    public final void setChargeReference(final String value) {
        chargeReference = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Collection configuration.
     */
    public final CreditCollectionConfiguration getCollectionConfiguration() {
        return collectionConfiguration;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Collection configuration.
     */
    public final void setCollectionConfiguration(
            final CreditCollectionConfiguration value) {
        collectionConfiguration = value;
    }
}