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

import gurux.dlms.enums.Authentication;

public class GXAuthenticationMechanismName {
    private int jointIsoCtt;
    private int country;
    private int countryName;
    private int identifiedOrganization;
    private int dlmsUA;
    private int authenticationMechanismName;
    private Authentication mechanismId;

    /*
     * Constructor.
     */
    public GXAuthenticationMechanismName() {
        mechanismId = Authentication.NONE;
    }

    public final int getJointIsoCtt() {
        return jointIsoCtt;
    }

    public final void setJointIsoCtt(final int value) {
        jointIsoCtt = value;
    }

    public final int getCountry() {
        return country;
    }

    public final void setCountry(final int value) {
        country = value;
    }

    public final int getCountryName() {
        return countryName;
    }

    public final void setCountryName(final int value) {
        countryName = value;
    }

    public final int getIdentifiedOrganization() {
        return identifiedOrganization;
    }

    public final void setIdentifiedOrganization(final int value) {
        identifiedOrganization = value;
    }

    public final int getDlmsUA() {
        return dlmsUA;
    }

    public final void setDlmsUA(final int value) {
        dlmsUA = value;
    }

    public final int getAuthenticationMechanismName() {
        return authenticationMechanismName;
    }

    public final void setAuthenticationMechanismName(final int value) {
        authenticationMechanismName = value;
    }

    public final Authentication getMechanismId() {
        return mechanismId;
    }

    public final void setMechanismId(final Authentication value) {
        mechanismId = value;
    }

    @Override
    public final String toString() {
        return String.valueOf(jointIsoCtt) + " " + String.valueOf(country) + " "
                + String.valueOf(countryName) + " "
                + String.valueOf(identifiedOrganization) + " "
                + String.valueOf(dlmsUA) + " "
                + String.valueOf(authenticationMechanismName) + " "
                + String.valueOf(mechanismId);
    }
}