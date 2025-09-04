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

package gurux.dlms;

/**
 * Get crypto keys.
 */
public interface IGXCryptoNotifier {
    /**
     * Notifies Un-ciphered PDU.
     * 
     * @param sender
     *            The source of the event.
     * @param data
     *            Un-ciphered PDU.
     */
    void onPdu(Object sender, byte[] data);

    /**
     * Called when the public or private key is needed and it's unknown.
     * 
     * @param sender
     *            Sender
     * @param args
     *            Arguments
     */
    void onKey(Object sender, GXCryptoKeyParameter args);

    /**
     * Called to encrypt or decrypt the data using external Hardware Security
     * Module.
     * 
     * @param sender
     *            Sender
     * @param args
     *            Arguments
     */
    void onCrypto(Object sender, GXCryptoKeyParameter args);
}
