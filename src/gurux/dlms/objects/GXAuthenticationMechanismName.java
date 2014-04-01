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

public class GXAuthenticationMechanismName
{
    private int JointIsoCtt;
    private int Country;
    private int CountryName;
    private int IdentifiedOrganization;
    private int DlmsUA;
    private int AuthenticationMechanismName;
    private Authentication MechanismId;

    /*
     * Constructor.
     */
    public GXAuthenticationMechanismName()
    {
        MechanismId = Authentication.NONE;
    }
    
    public final int getJointIsoCtt()
    {
        return JointIsoCtt;
    }
    public final void setJointIsoCtt(int value)
    {
        JointIsoCtt = value;
    }

    public final int getCountry()
    {
        return Country;
    }
    public final void setCountry(int value)
    {
        Country = value;
    }

    public final int getCountryName()
    {
        return CountryName;
    }
    public final void setCountryName(int value)
    {
        CountryName = value;
    }
    public final int getIdentifiedOrganization()
    {
        return IdentifiedOrganization;
    }
    public final void setIdentifiedOrganization(int value)
    {
        IdentifiedOrganization = value;
    }

    public final int getDlmsUA()
    {
        return DlmsUA;
    }
    public final void setDlmsUA(int value)
    {
        DlmsUA = value;
    }
    public final int getAuthenticationMechanismName()
    {
        return AuthenticationMechanismName;
    }
    public final void setAuthenticationMechanismName(int value)
    {
        AuthenticationMechanismName = value;
    }

    public final Authentication getMechanismId()
    {
        return MechanismId;
    }
    public final void setMechanismId(Authentication value)
    {
        MechanismId = value;
    }
    
    @Override
    public String toString()
    {        
        return String.valueOf(JointIsoCtt) + " " + String.valueOf(Country) + " " + 
                String.valueOf(CountryName) + " " + String.valueOf(IdentifiedOrganization) + " " + 
                String.valueOf(DlmsUA) + " " + String.valueOf(AuthenticationMechanismName) + " " + 
                String.valueOf(MechanismId);
    }
}