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

package gurux.dlms.secure;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;

/**
 * GXDLMSSecureClient implements secure client where all messages are secured
 * using transport security.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSSecureClient extends GXDLMSClient {
	/**
	 * Ciphering settings.
	 */
	private GXCiphering ciphering;

	/**
	 * Constructor.
	 */
	public GXDLMSSecureClient() {
		this(false);
	}

	/**
	 * Constructor.
	 * 
	 * @param useLogicalNameReferencing Is Logical Name referencing used.
	 */
	public GXDLMSSecureClient(final boolean useLogicalNameReferencing) {
		this(useLogicalNameReferencing, 16, 1, Authentication.NONE, null, InterfaceType.HDLC);
	}

	/**
	 * Constructor.
	 * 
	 * @param useLogicalNameReferencing Is Logical Name referencing used.
	 * @param clientAddress             Server address.
	 * @param serverAddress             Client address.
	 * @param forAuthentication         Authentication type.
	 * @param password                  Password if authentication is used.
	 * @param interfaceType             Object type.
	 */
	public GXDLMSSecureClient(final boolean useLogicalNameReferencing, final int clientAddress, final int serverAddress,
			final Authentication forAuthentication, final String password, final InterfaceType interfaceType) {
		super(useLogicalNameReferencing, clientAddress, serverAddress, forAuthentication, password, interfaceType);
		ciphering = new GXCiphering("ABCDEFGH".getBytes());
		setCipher(ciphering);
	}

	/**
	 * @return Ciphering settings.
	 */
	public final GXCiphering getCiphering() {
		return ciphering;
	}

	/**
	 * Encrypt data using Key Encrypting Key.
	 * 
	 * @param kek  Key Encrypting Key, also known as Master key.
	 * @param data Data to encrypt.
	 * @return Encrypted data.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 */
	public static byte[] encrypt(final byte[] kek, final byte[] data)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return GXSecure.encryptAesKeyWrapping(data, kek);
	}

	/**
	 * Decrypt data using Key Encrypting Key.
	 * 
	 * @param kek  Key Encrypting Key, also known as Master key.
	 * @param data Data to decrypt.
	 * @return Decrypted data.
	 */
	public static byte[] decrypt(final byte[] kek, final byte[] data) {
		if (kek == null) {
			throw new NullPointerException("Key Encrypting Key");
		}
		if (kek.length < 16) {
			throw new IllegalArgumentException("Key Encrypting Key");
		}
		if (kek.length % 8 != 0) {
			throw new IllegalArgumentException("Key Encrypting Key");
		}
		if (data == null) {
			throw new NullPointerException("data");
		}
		if (data.length < 16) {
			throw new IllegalArgumentException("data");
		}
		if (data.length % 8 != 0) {
			throw new IllegalArgumentException("data");
		}
		try {
			return GXSecure.decryptAesKeyWrapping(data, kek);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * @return Server system title.
	 */
	public byte[] getServerSystemTitle() {
		return getSettings().getSourceSystemTitle();
	}

	/**
	 * @param value Server system title.
	 */
	public void setServerSystemTitle(final byte[] value) {
		getSettings().setSourceSystemTitle(value);
	}
}
