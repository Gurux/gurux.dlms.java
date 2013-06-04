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

import gurux.dlms.enums.Authentication;

/** 
 Authentication class is used to give authentication iformation to the server.
*/
public class GXAuthentication
{
    private boolean privateSelected;
    private Object privateClientID;
    private Authentication privateType = Authentication.NONE;
    private String privatePassword;

    public GXAuthentication()
    {
    }

    @Override
    public String toString()
    {
        return getType().toString();
    }

    /** 
     Constructor.

     @param type Authentication type
     @param clientID Client Id.
    */
    public GXAuthentication(Authentication type, Object clientID)
    {
        this(type, null, clientID);
    }

    /** 
     Constructor.

     @param type Authentication type
     @param pw Used password.
     @param clientID Client Id.
    */
    public GXAuthentication(Authentication type, String pw, Object clientID)
    {
        setType(type);
        setPassword(pw);
        setClientID(clientID);
    }

    /** 
     Is authentication selected.
    */

    public final boolean getSelected()
    {
        return privateSelected;
    }
    public final void setSelected(boolean value)
    {
        privateSelected = value;
    }

    /** 
     Authentication type
    */
    public final Authentication getType()
    {
        return privateType;
    }
    public final void setType(Authentication value)
    {
        privateType = value;
    }

    /** 
     Client address.
    */
    public final Object getClientID()
    {
        return privateClientID;
    }
    public final void setClientID(Object value)
    {
        privateClientID = value;
    }

    /** 
     Used password.
    */
    public final String getPassword()
    {
        return privatePassword;
    }
    public final void setPassword(String value)
    {
        privatePassword = value;
    }
}