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

import java.security.KeyPair;

import gurux.dlms.asn.GXx509CertificateCollection;
import gurux.dlms.enums.KeyAgreementScheme;
import gurux.dlms.enums.Security;
import gurux.dlms.objects.enums.SecurityPolicy;
import gurux.dlms.objects.enums.SecuritySuite;

public interface GXICipher {

	/**
	 * Reset encrypt settings.
	 */
	void reset();

	/**
	 * @return Is ciphering used.
	 */
	boolean isCiphered();

	/**
	 * @return Used security.
	 */
	Security getSecurity();

	/**
	 * @param value Used security policy.
	 */
	void setSecurityPolicy(java.util.Set<SecurityPolicy> value);

	/**
	 * @return Used security policy.
	 */
	java.util.Set<SecurityPolicy> getSecurityPolicy();

	/**
	 * @param value Used security.
	 */
	void setSecurity(Security value);

	/**
	 * @return Used security suite.
	 */
	SecuritySuite getSecuritySuite();

	/**
	 * @param value Used security suite.
	 */
	void setSecuritySuite(final SecuritySuite value);

	/**
	 * @return System title.
	 */
	byte[] getSystemTitle();

	/**
	 * @param value System title.
	 */
	void setSystemTitle(byte[] value);

	/**
	 * @return Recipient system Title.
	 */
	byte[] getRecipientSystemTitle();

	/**
	 * @param value Block cipher key.
	 */
	void setBlockCipherKey(byte[] value);

	/**
	 * @return Block cipher key.
	 */
	byte[] getBlockCipherKey();

	/**
	 * @return Authentication key.
	 */
	byte[] getAuthenticationKey();

	/**
	 * @param value Authentication key.
	 */
	void setAuthenticationKey(byte[] value);

	/**
	 * @param value Broadcast block cipher key.
	 */
	void setBroadcastBlockCipherKey(byte[] value);

	/**
	 * @return Broadcast block cipher key.
	 */
	byte[] getBroadcastBlockCipherKey();

	/**
	 * @return Invocation counter.
	 */
	long getInvocationCounter();

	/**
	 * @param value Invocation counter.
	 */
	void setInvocationCounter(long value);

	/**
	 * @return Ephemeral key pair.
	 */
	KeyPair getEphemeralKeyPair();

	/**
	 * @param value Ephemeral key pair.
	 */
	void setEphemeralKeyPair(KeyPair value);

	/**
	 * @return Client's key agreement key pair.
	 */
	KeyPair getKeyAgreementKeyPair();

	/**
	 * @param value Client's key agreement key pair.
	 */
	void setKeyAgreementKeyPair(KeyPair value);

	/**
	 * @return Available certificates.
	 */
	GXx509CertificateCollection getCertificates();

	/**
	 * @return Signing key pair.
	 */
	KeyPair getSigningKeyPair();

	/**
	 * @param value Signing key pair.
	 */
	void setSigningKeyPair(KeyPair value);

	/**
	 * @return Dedicated key.
	 */
	byte[] getDedicatedKey();

	/**
	 * @param value Dedicated key.
	 */
	void setDedicatedKey(byte[] value);

	/**
	 * @return Used key agreement scheme.
	 */
	public KeyAgreementScheme getKeyAgreementScheme();

	/**
	 * @param value Used key agreement scheme.
	 */
	public void setKeyAgreementScheme(final KeyAgreementScheme value);
}
