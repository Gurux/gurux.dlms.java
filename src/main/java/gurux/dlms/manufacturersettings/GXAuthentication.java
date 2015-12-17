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

import java.io.UnsupportedEncodingException;

import gurux.dlms.enums.Authentication;

/**
 * Authentication class is used to give authentication information to the
 * server.
 */
public class GXAuthentication {
    private int clientAddress;
    private Authentication type = Authentication.NONE;
    private byte[] password;

    public GXAuthentication() {
    }

    @Override
    public final String toString() {
        return getType().toString();
    }

    /**
     * Constructor.
     * 
     * @param forType
     *            Authentication type
     * @param forClientAddress
     *            Client Id.
     */
    public GXAuthentication(final Authentication forType,
            final int forClientAddress) {
        this(forType, (byte[]) null, forClientAddress);
    }

    /**
     * Constructor.
     * 
     * @param forType
     *            Authentication type
     * @param pw
     *            Used password.
     * @param forClientAddress
     *            Client Id.
     */
    public GXAuthentication(final Authentication forType, final byte[] pw,
            final int forClientAddress) {
        setType(forType);
        setPassword(pw);
        setClientAddress(forClientAddress);
    }

    /**
     * Constructor.
     * 
     * @param forType
     *            Authentication type
     * @param pw
     *            Used password.
     * @param forClientAddress
     *            Client Id.
     */
    public GXAuthentication(final Authentication forType, final String pw,
            final int forClientAddress) {
        setType(forType);
        try {
            setPassword(pw.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        setClientAddress(forClientAddress);
    }

    /**
     * @return Authentication type.
     */
    public final Authentication getType() {
        return type;
    }

    /**
     * @param value
     *            Authentication type.
     */
    public final void setType(final Authentication value) {
        type = value;
    }

    /**
     * @return Client address.
     */
    public final int getClientAddress() {
        return clientAddress;
    }

    /**
     * @param value
     *            Client address.
     */
    public final void setClientAddress(final int value) {
        clientAddress = value;
    }

    /**
     * @return Used password.
     */
    public final byte[] getPassword() {
        return password;
    }

    /**
     * @param value
     *            Used password.
     */
    public final void setPassword(final byte[] value) {
        password = value;
    }
}