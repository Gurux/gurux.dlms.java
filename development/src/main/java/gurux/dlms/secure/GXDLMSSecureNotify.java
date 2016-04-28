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

package gurux.dlms.secure;

import gurux.dlms.GXDLMSNotify;
import gurux.dlms.enums.InterfaceType;

/**
 * This class is used to send data notify and push messages to the clients.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSSecureNotify extends GXDLMSNotify {

    /**
     * Ciphering settings.
     */
    private GXCiphering ciphering;

    /**
     * Constructor.
     * 
     * @param useLogicalNameReferencing
     *            Is Logical Name referencing used.
     * @param clientAddress
     *            Server address.
     * @param serverAddress
     *            Client address.
     * @param interfaceType
     *            Object type.
     */
    public GXDLMSSecureNotify(final boolean useLogicalNameReferencing,
            final int clientAddress, final int serverAddress,
            final InterfaceType interfaceType) {
        super(useLogicalNameReferencing, clientAddress, serverAddress,
                interfaceType);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    /**
     * @return Ciphering settings.
     */
    public final GXCiphering getCiphering() {
        return ciphering;
    }
}
