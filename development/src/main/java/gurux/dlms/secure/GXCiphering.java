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
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXICipher;
import gurux.dlms.asn.GXx509CertificateCollection;
import gurux.dlms.enums.KeyAgreementScheme;
import gurux.dlms.enums.Security;
import gurux.dlms.objects.enums.SecurityPolicy;
import gurux.dlms.objects.enums.SecuritySuite;

/**
 * Gurux DLMS/COSEM Transport security (Ciphering) settings.
 */
public class GXCiphering implements GXICipher {
	private Security security = Security.NONE;

	Set<SecurityPolicy> securityPolicy;

	private byte[] authenticationKey;
	/**
	 * Dedicated key.
	 */
	private byte[] dedicatedKey;

	/**
	 * Certificates.
	 */
	private GXx509CertificateCollection certificates;

	/**
	 * Ephemeral key pair.
	 */
	private KeyPair ephemeralKeyPair;

	/**
	 * System title.
	 */
	private byte[] systemTitle;

	/**
	 * recipient system title.
	 */
	private byte[] recipientSystemTitle;

	/**
	 * Block cipher key.
	 */
	private byte[] blockCipherKey;

	/**
	 * Broadcast block cipher key.
	 */
	private byte[] broadcastBlockCipherKey;

	/**
	 * Invocation Counter.
	 */
	private long invocationCounter = 0;

	/**
	 * Used security suite.
	 */
	private SecuritySuite securitySuite = SecuritySuite.SUITE_0;

	/**
	 * Signing key pair.
	 */
	private KeyPair signingKeyPair;

	/**
	 * Client key agreement key pair.
	 */
	private KeyPair keyAgreementKeyPair;

	/**
	 * Used key agreement scheme.
	 */
	private KeyAgreementScheme keyAgreementScheme;

	/**
	 * Constructor. Default values are from the Green Book.
	 * 
	 * @param title Used system title.
	 */
	public GXCiphering(final byte[] title) {
		securityPolicy = new HashSet<SecurityPolicy>();
		certificates = new GXx509CertificateCollection();
		setSecurity(Security.NONE);
		setSystemTitle(title);
		setBlockCipherKey(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C,
				0x0D, 0x0E, 0x0F });
		setAuthenticationKey(new byte[] { (byte) 0xD0, (byte) 0xD1, (byte) 0xD2, (byte) 0xD3, (byte) 0xD4, (byte) 0xD5,
				(byte) 0xD6, (byte) 0xD7, (byte) 0xD8, (byte) 0xD9, (byte) 0xDA, (byte) 0xDB, (byte) 0xDC, (byte) 0xDD,
				(byte) 0xDE, (byte) 0xDF });
		keyAgreementScheme = KeyAgreementScheme.EPHEMERAL_UNIFIED_MODEL;
	}

	public static byte[] decrypt(final GXICipher c, final AesGcmParameter p, final GXByteBuffer data)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] tmp;
		tmp = GXSecure.decryptAesGcm(c, p, data);
		return tmp;
	}

	/**
	 * Cipher PDU.
	 * 
	 * @param p    Aes GCM Parameter.
	 * @param data Plain text.
	 * @return Secured data.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 */
	public static byte[] encrypt(final AesGcmParameter p, final byte[] data)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (p.getSecurity() != Security.NONE) {
			return GXSecure.encryptAesGcm(true, p, data);
		}
		return data;
	}

	@Override
	public final void reset() {
		setSecurity(Security.NONE);
		setInvocationCounter(0);
	}

	@Override
	public final boolean isCiphered() {
		return security != Security.NONE;
	}

	/**
	 * @param value Invocation Counter.
	 */
	public final void setInvocationCounter(final long value) {
		invocationCounter = value;
	}

	/**
	 * @return Invocation Counter.
	 */
	public final long getInvocationCounter() {
		return invocationCounter;
	}

	/**
	 * @return Used security.
	 */
	public final Security getSecurity() {
		return security;
	}

	/**
	 * @param value Used security.
	 */
	public final void setSecurity(final Security value) {
		security = value;
	}

	/**
	 * @param value Used security policy.
	 */
	public void setSecurityPolicy(java.util.Set<SecurityPolicy> value) {
		securityPolicy.addAll(value);
	}

	/**
	 * @return Used security policy.
	 */
	public java.util.Set<SecurityPolicy> getSecurityPolicy() {
		return securityPolicy;
	}

	/**
	 * The SystemTitle is a 8 bytes (64 bit) value that identifies a partner of the
	 * communication. First 3 bytes contains the three letters manufacturer ID. The
	 * remainder of the system title holds for example a serial number.
	 * 
	 * @see <a href="http://www.dlms.com/organization/flagmanufacturesids">List of
	 *      manufacturer IDs.</a>
	 * @return System title.
	 */
	public final byte[] getSystemTitle() {
		return systemTitle;
	}

	/**
	 * @param value System title.
	 */
	public final void setSystemTitle(final byte[] value) {
		if (value != null && value.length != 8) {
			throw new IllegalArgumentException("Invalid system title.");
		}
		systemTitle = value;
	}

	/**
	 * @return Recipient system title.
	 */
	public final byte[] getRecipientSystemTitle() {
		return recipientSystemTitle;
	}

	/**
	 * @param value Recipient system title.
	 */
	public final void setRecipientSystemTitle(final byte[] value) {
		if (value != null && value.length != 8) {
			throw new IllegalArgumentException("Invalid recipient system title.");
		}
		recipientSystemTitle = value;
	}

	/**
	 * Each block is ciphered with this key.
	 * 
	 * @return Block cipher key.
	 */
	public final byte[] getBlockCipherKey() {
		return blockCipherKey;
	}

	public final void setBlockCipherKey(final byte[] value) {
		if (value != null && value.length != 16) {
			throw new IllegalArgumentException("Invalid Block Cipher Key.");
		}
		blockCipherKey = value;
	}

	/**
	 * @return Authentication Key is 16 bytes value.
	 */
	public final byte[] getAuthenticationKey() {
		return authenticationKey;
	}

	public final void setAuthenticationKey(final byte[] value) {
		if (value != null && value.length != 16) {
			throw new IllegalArgumentException("Invalid Authentication Key.");
		}
		authenticationKey = value;
	}

	/**
	 * @param value Broadcast block cipher key.
	 */
	public final void setBroadcastBlockCipherKey(byte[] value) {
		if (value != null && value.length != 16) {
			throw new IllegalArgumentException("Invalid Block Cipher Key.");
		}
		broadcastBlockCipherKey = value;
	}

	/**
	 * @return Broadcast block cipher key.
	 */
	public final byte[] getBroadcastBlockCipherKey() {
		return broadcastBlockCipherKey;
	}

	/**
	 * @return Available certificates.
	 */
	public final GXx509CertificateCollection getCertificates() {
		return certificates;
	}

	/**
	 * @return Ephemeral key pair.
	 */
	public KeyPair getEphemeralKeyPair() {
		return ephemeralKeyPair;
	}

	/**
	 * @param value Ephemeral key pair.
	 */
	public void setEphemeralKeyPair(final KeyPair value) {
		ephemeralKeyPair = value;
	}

	/**
	 * @return Signing key pair.
	 */
	public final KeyPair getSigningKeyPair() {
		return signingKeyPair;
	}

	/**
	 * @param value Signing key pair.
	 */
	public final void setSigningKeyPair(final KeyPair value) {
		signingKeyPair = value;
	}

	/**
	 * @return Client's key agreement key pair.
	 */
	public final KeyPair getKeyAgreementKeyPair() {
		return keyAgreementKeyPair;
	}

	/**
	 * @param value Client's key agreement key pair.
	 */
	public final void setKeyAgreementKeyPair(final KeyPair value) {
		keyAgreementKeyPair = value;
	}

	/**
	 * @return Used security suite.
	 */
	public SecuritySuite getSecuritySuite() {
		return securitySuite;
	}

	/**
	 * @param value Used security suite.
	 */
	public void setSecuritySuite(final SecuritySuite value) {
		securitySuite = value;
	}

	/**
	 * Generate GMAC password from given challenge.
	 * 
	 * @param challenge Client to Server or Server to Client challenge.
	 * @return Generated challenge.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 */
	public byte[] generateGmacPassword(final byte[] challenge) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
		AesGcmParameter p = new AesGcmParameter(0x10, Security.AUTHENTICATION, SecuritySuite.SUITE_0, invocationCounter,
				systemTitle, blockCipherKey, authenticationKey);
		p.setType(CountType.TAG);
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(0x10);
		bb.setUInt32(invocationCounter);
		bb.set(GXSecure.encryptAesGcm(true, p, challenge));
		return bb.array();
	}

	@Override
	public byte[] getDedicatedKey() {
		return dedicatedKey;
	}

	@Override
	public void setDedicatedKey(final byte[] value) {
		dedicatedKey = value;
	}

	/**
	 * @return Used key agreement scheme.
	 */
	@Override
	public KeyAgreementScheme getKeyAgreementScheme() {
		return keyAgreementScheme;
	}

	/**
	 * @param value Used key agreement scheme.
	 */
	@Override
	public void setKeyAgreementScheme(final KeyAgreementScheme value) {
		keyAgreementScheme = value;
	}
}