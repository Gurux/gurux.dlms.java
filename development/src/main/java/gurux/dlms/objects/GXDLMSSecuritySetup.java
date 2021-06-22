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

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.asn.GXAsn1Converter;
import gurux.dlms.asn.GXPkcs10;
import gurux.dlms.asn.GXPkcs8;
import gurux.dlms.asn.GXx509Certificate;
import gurux.dlms.asn.GXx509CertificateCollection;
import gurux.dlms.asn.enums.Ecc;
import gurux.dlms.asn.enums.KeyUsage;
import gurux.dlms.ecdsa.GXEcdsa;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.KeyAgreementScheme;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.enums.CertificateEntity;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.GlobalKeyType;
import gurux.dlms.objects.enums.SecurityPolicy;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.dlms.secure.GXDLMSSecureClient;
import gurux.dlms.secure.GXSecure;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSSecuritySetup
 */
public class GXDLMSSecuritySetup extends GXDLMSObject implements IGXDLMSBase, IGXDLMSSettings {

	/**
	 * Block cipher key.
	 */
	private byte[] guek;
	/**
	 * Broadcast block cipher key.
	 */
	private byte[] gbek;

	/*
	 * Authentication key.
	 */
	private byte[] gak;

	/**
	 * Master key.
	 */
	private byte[] kek;

	GXDLMSSettings serverSettings;
	/**
	 * Signing key of the server.
	 */
	public KeyPair signingKey;

	/**
	 * Key agreement key pair of the server.
	 */
	public KeyPair keyAgreement;

	/**
	 * TLS pair of the server.
	 */
	public KeyPair tls;

	/**
	 * Available certificates of the server.
	 */
	public GXx509CertificateCollection serverCertificates = new GXx509CertificateCollection();

	/**
	 * Security policy.
	 */
	private Set<SecurityPolicy> securityPolicy = new HashSet<SecurityPolicy>();

	/**
	 * Security suite.
	 */
	private SecuritySuite securitySuite;

	/**
	 * Server system title.
	 */
	private byte[] serverSystemTitle;

	/**
	 * Client system title.
	 */
	private byte[] clientSystemTitle;

	/**
	 * Available certificates.
	 */
	private GXDLMSCertificateCollection certificates;

	/**
	 * Constructor.
	 */
	public GXDLMSSecuritySetup() {
		this("0.0.43.0.0.255");
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSSecuritySetup(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSSecuritySetup(final String ln, final int sn) {
		super(ObjectType.SECURITY_SETUP, ln, sn);
		securitySuite = SecuritySuite.SUITE_0;
		certificates = new GXDLMSCertificateCollection();
		setVersion(1);
		// If Broadcast block cipher key is not defined GUEK is used.
		gbek = null;
		guek = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
				0x0F };
		gak = new byte[] { (byte) 0xD0, (byte) 0xD1, (byte) 0xD2, (byte) 0xD3, (byte) 0xD4, (byte) 0xD5, (byte) 0xD6,
				(byte) 0xD7, (byte) 0xD8, (byte) 0xD9, (byte) 0xDA, (byte) 0xDB, (byte) 0xDC, (byte) 0xDD, (byte) 0xDE,
				(byte) 0xDF };
		setKek("1111111111111111".getBytes());
	}

	/**
	 * 
	 * @return Block cipher key.
	 */
	public byte[] getGuek() {
		return guek;
	}

	/**
	 * 
	 * @param value Block cipher key.
	 */
	public void setGuek(final byte[] value) {
		guek = value;
	}

	/**
	 * 
	 * @return Broadcast block cipher key.
	 */
	public byte[] getGbek() {
		return gbek;
	}

	/**
	 * 
	 * @param value Broadcast block cipher key.
	 */
	public void setGbek(final byte[] value) {
		gbek = value;
	}

	/**
	 * 
	 * @return Authentication key.
	 */
	public byte[] getGak() {
		return gak;
	}

	/**
	 * 
	 * @param value Authentication key.
	 */
	public void setGak(final byte[] value) {
		gak = value;
	}

	/**
	 * 
	 * @return Master key.
	 */
	public byte[] getKek() {
		return kek;
	}

	/**
	 * 
	 * @param value Master key.
	 */
	public void setKek(final byte[] value) {
		kek = value;
	}

	/**
	 * @return Security policy for version 1.
	 */
	public final Set<SecurityPolicy> getSecurityPolicy() {
		return securityPolicy;
	}

	/**
	 * @param value Security policy for version 1.
	 */
	public final void setSecurityPolicy(final Set<SecurityPolicy> value) {
		securityPolicy = value;
	}

	/**
	 * @return Security suite.
	 */
	public final SecuritySuite getSecuritySuite() {
		return securitySuite;
	}

	/**
	 * @param value Security suite.
	 */
	public final void setSecuritySuite(final SecuritySuite value) {
		securitySuite = value;
	}

	/**
	 * @return Client system title.
	 */
	public final byte[] getClientSystemTitle() {
		return clientSystemTitle;
	}

	/**
	 * @param value Client system title.
	 */
	public final void setClientSystemTitle(final byte[] value) {
		clientSystemTitle = value;
	}

	/**
	 * @return Server system title.
	 */
	public final byte[] getServerSystemTitle() {
		return serverSystemTitle;
	}

	/**
	 * @param value Server system title.
	 */
	public final void setServerSystemTitle(final byte[] value) {
		serverSystemTitle = value;
	}

	/**
	 * @return Available certificates.
	 */
	public final GXDLMSCertificateCollection getCertificates() {
		return certificates;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), securityPolicy, securitySuite, clientSystemTitle, serverSystemTitle,
				certificates };
	}

	/**
	 * Activates and strengthens the security policy for version 0 Security Setup
	 * Object.
	 * 
	 * @param client   DLMS client that is used to generate action.
	 * @param security New security level.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 */
	public final byte[][] activate(final GXDLMSClient client, final SecurityPolicy security)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 1, security.getValue(), DataType.ENUM);
	}

	/**
	 * Activates and strengthens the security policy for version 1 Security Setup
	 * Object.
	 * 
	 * @param client   DLMS client that is used to generate action.
	 * @param security New security level.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 */
	public final byte[][] activate(final GXDLMSClient client, final Set<SecurityPolicy> security)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 1, SecurityPolicy.toInteger(security), DataType.ENUM);
	}

	/**
	 * Parse GXx509Certificate from data that meter sends.
	 * 
	 * @param data Meter data.
	 * @return Generated GXx509Certificate.
	 */
	public static GXPkcs10 parseCertificate(final GXByteBuffer data) {
		GXDataInfo info = new GXDataInfo();
		byte[] value = (byte[]) GXCommon.getData(null, data, info);
		return new GXPkcs10(value);
	}

	/**
	 * Updates one or more global keys.
	 * 
	 * @param client DLMS client that is used to generate action.
	 * @param kek    Master key, also known as Key Encrypting Key.
	 * @param list   List of Global key types and keys.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 */
	public final byte[][] globalKeyTransfer(final GXDLMSClient client, final byte[] kek,
			final List<GXSimpleEntry<GlobalKeyType, byte[]>> list) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Invalid list. It is empty.");
		}
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(DataType.ARRAY.getValue());
		bb.setUInt8((byte) list.size());
		byte[] tmp;
		for (GXSimpleEntry<GlobalKeyType, byte[]> it : list) {
			bb.setUInt8(DataType.STRUCTURE.getValue());
			bb.setUInt8(2);
			GXCommon.setData(null, bb, DataType.ENUM, it.getKey().ordinal());
			tmp = GXDLMSSecureClient.encrypt(kek, it.getValue());
			GXCommon.setData(null, bb, DataType.OCTET_STRING, tmp);
		}
		return client.method(this, 2, bb.array(), DataType.ARRAY);
	}

	/**
	 * Agree on one or more symmetric keys using the key agreement algorithm.
	 * 
	 * @param client DLMS client that is used to generate action.
	 * @param list   List of keys.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 */
	public final byte[][] keyAgreement(final GXDLMSClient client, final List<GXSimpleEntry<GlobalKeyType, byte[]>> list)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Invalid list. It is empty.");
		}

		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(DataType.ARRAY.getValue());
		bb.setUInt8((byte) list.size());
		for (GXSimpleEntry<GlobalKeyType, byte[]> it : list) {
			bb.setUInt8(DataType.STRUCTURE.getValue());
			bb.setUInt8(2);
			GXCommon.setData(null, bb, DataType.ENUM, it.getKey().ordinal());
			GXCommon.setData(null, bb, DataType.OCTET_STRING, it.getValue());
		}
		return client.method(this, 3, bb.array(), DataType.ARRAY);
	}

	/**
	 * Agree on one or more symmetric keys using the key agreement algorithm.
	 * 
	 * @param client DLMS client that is used to generate action.
	 * @param type   Global key type.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws SignatureException                 Signature exception.
	 */
	public final byte[][] keyAgreement(final GXDLMSSecureClient client, final GlobalKeyType type)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		GXByteBuffer bb = new GXByteBuffer();
		byte[] data = GXSecure.getEphemeralPublicKeyData(type.ordinal(),
				client.getCiphering().getEphemeralKeyPair().getPublic());
		bb.set(data, 1, 64);
		Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.INFO, "Signin public key: {0}",
				client.getCiphering().getSigningKeyPair().getPublic());

		// Add signature.
		byte[] sign = GXSecure.getEphemeralPublicKeySignature(type.ordinal(),
				client.getCiphering().getEphemeralKeyPair().getPublic(),
				client.getCiphering().getSigningKeyPair().getPrivate());
		bb.set(sign);
		Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.FINEST, "Data: {0}", GXCommon.toHex(data));
		Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.FINEST, "Sign: {0}", GXCommon.toHex(sign));

		List<GXSimpleEntry<GlobalKeyType, byte[]>> list = new ArrayList<GXSimpleEntry<GlobalKeyType, byte[]>>();
		list.add(new GXSimpleEntry<GlobalKeyType, byte[]>(type, bb.array()));
		return keyAgreement(client, list);
	}

	/**
	 * Generates an asymmetric key pair as required by the security suite.
	 * 
	 * @param client DLMS client that is used to generate action.
	 * @param type   New certificate type.
	 * @return Generated action. * @throws BadPaddingException. Bad padding
	 *         exception.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 */
	public final byte[][] generateKeyPair(final GXDLMSClient client, final CertificateType type)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 4, type.getValue(), DataType.ENUM);
	}

	/**
	 * Ask Server sends the Certificate Signing Request (CSR) data.
	 * 
	 * @param client DLMS client that is used to generate action.
	 * @param type   identifies the key pair for which the certificate will be
	 *               requested.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 */
	public final byte[][] generateCertificate(final GXDLMSClient client, final CertificateType type)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 5, type.getValue(), DataType.ENUM);
	}

	/**
	 * Imports an X.509 v3 certificate of a public key.
	 * 
	 * @param client      DLMS client that is used to generate action.
	 * @param certificate X.509 v3 certificate.
	 * @return Generated action. * @throws BadPaddingException. Bad padding
	 *         exception.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 */
	public final byte[][] importCertificate(final GXDLMSClient client, final GXx509Certificate certificate)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return importCertificate(client, certificate.getEncoded());
	}

	/**
	 * Imports an X.509 v3 certificate of a public key.
	 * 
	 * @param client DLMS client that is used to generate action.
	 * @param key    Public key.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 */
	public final byte[][] importCertificate(final GXDLMSClient client, final byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 6, key, DataType.OCTET_STRING);
	}

	/**
	 * Exports an X.509 v3 certificate from the server using entity information.
	 * 
	 * @param client      DLMS client that is used to generate action.
	 * @param entity      Certificate entity.
	 * @param type        Certificate type.
	 * @param systemTitle System title.
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 */
	public final byte[][] exportCertificateByEntity(final GXDLMSSecureClient client, final CertificateEntity entity,
			final CertificateType type, final byte[] systemTitle) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (systemTitle == null || systemTitle.length == 0) {
			throw new IllegalArgumentException("Invalid system title.");
		}
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(2);
		// Add enum
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(0);
		// Add certificate_identification_by_entity
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(3);
		// Add certificate_entity
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(entity.getValue());
		// Add certificate_type
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(type.getValue());
		// system_title
		GXCommon.setData(null, bb, DataType.OCTET_STRING, systemTitle);
		return client.method(this, 7, bb.array(), DataType.STRUCTURE);
	}

	/**
	 * Exports an X.509 v3 certificate from the server using serial information.
	 * 
	 * @param client       DLMS client that is used to generate action.
	 * @param serialNumber Serial number.
	 * @param issuer       Issuer
	 * @return Generated action.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 */
	public final byte[][] exportCertificateBySerial(final GXDLMSSecureClient client, final BigInteger serialNumber,
			final String issuer) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(2);
		// Add enum
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(1);
		// Add certificate_identification_by_entity
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(2);
		// serialNumber
		GXCommon.setData(null, bb, DataType.OCTET_STRING, serialNumber.toByteArray());
		// issuer
		GXCommon.setData(null, bb, DataType.OCTET_STRING, issuer.getBytes());
		return client.method(this, 7, bb.array(), DataType.STRUCTURE);
	}

	/**
	 * Removes X.509 v3 certificate from the server using entity.
	 * 
	 * @param client      DLMS client that is used to generate action.
	 * @param entity      Certificate entity type.
	 * @param type        Certificate type.
	 * @param systemTitle System title.
	 * @return Generated action. * @throws BadPaddingException. Bad padding
	 *         exception.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 */
	public final byte[][] removeCertificateByEntity(final GXDLMSSecureClient client, final CertificateEntity entity,
			final CertificateType type, final byte[] systemTitle) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(2);
		// Add enum
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(0);
		// Add certificate_identification_by_entity
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(3);
		// Add certificate_entity
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(entity.getValue());
		// Add certificate_type
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(type.getValue());
		// system_title
		GXCommon.setData(null, bb, DataType.OCTET_STRING, systemTitle);
		return client.method(this, 8, bb.array(), DataType.STRUCTURE);
	}

	/**
	 * Removes X.509 v3 certificate from the server using serial number.
	 * 
	 * @param client       DLMS client that is used to generate action.
	 * @param serialNumber Serial number.
	 * @param issuer       Issuer.
	 * @return Generated action. * @throws BadPaddingException. Bad padding
	 *         exception.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 */
	public final byte[][] removeCertificateBySerial(final GXDLMSSecureClient client, final String serialNumber,
			final String issuer) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(2);
		// Add enum
		bb.setUInt8(DataType.ENUM.getValue());
		bb.setUInt8(1);
		// Add certificate_identification_by_entity
		bb.setUInt8(DataType.STRUCTURE.getValue());
		bb.setUInt8(2);
		// serialNumber
		GXCommon.setData(null, bb, DataType.OCTET_STRING, serialNumber.getBytes());
		// issuer
		GXCommon.setData(null, bb, DataType.OCTET_STRING, issuer.getBytes());
		return client.method(this, 8, bb.array(), DataType.STRUCTURE);
	}

	@Override
	public final byte[] invoke(final GXDLMSSettings settings, final ValueEventArgs e) {
		switch (e.getIndex()) {
		case 1:
			securityActivate(settings, e);
			break;
		case 2:
			keyTransfer(settings, e);
			break;
		case 3:
			return invokeKeyAgreement(settings, e);
		case 4:
			generateKeyPair(settings, e);
			break;
		case 5:
			return generateCertificateRequest(settings, e);
		case 6:
			importCertificate(settings, e);
			break;
		case 7:
			return exportCertificate(e);
		case 8:
			removeCertificate(e);
			break;
		default:
			// Invalid type
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
		// Return standard reply.
		return null;
	}

	private void removeCertificate(final ValueEventArgs e) {
		List<?> tmp = (List<?>) ((List<?>) e.getParameters());
		short type = ((Number) tmp.get(0)).shortValue();
		tmp = (List<?>) tmp.get(1);
		GXx509Certificate cert = null;
		if (type == 0) {
			cert = findCertificateByEntity(serverCertificates,
					CertificateEntity.forValue(((Number) tmp.get(0)).intValue()),
					CertificateType.forValue(((Number) tmp.get(1)).intValue()), (byte[]) tmp.get(2));
		} else if (type == 1) {
			cert = serverCertificates.findBySerial(new BigInteger((byte[]) tmp.get(0)),
					new String((byte[]) tmp.get(1)));
		}
		if (cert == null) {
			e.setError(ErrorCode.INCONSISTENT_CLASS);
		} else {
			serverCertificates.remove(cert);
		}
	}

	private byte[] exportCertificate(final ValueEventArgs e) {
		List<?> tmp = (List<?>) e.getParameters();
		short type = ((Number) tmp.get(0)).shortValue();
		GXx509Certificate cert = null;
		synchronized (serverCertificates) {
			if (type == 0) {
				tmp = (List<?>) tmp.get(1);
				cert = findCertificateByEntity(serverCertificates,
						CertificateEntity.forValue(((Number) tmp.get(0)).shortValue()),
						CertificateType.forValue(((Number) tmp.get(1)).shortValue()), (byte[]) tmp.get(2));
			} else if (type == 1) {
				tmp = (List<?>) tmp.get(1);
				cert = serverCertificates.findBySerial(new BigInteger((byte[]) tmp.get(0)),
						new String((byte[]) tmp.get(1)));
			}
			if (cert == null) {
				e.setError(ErrorCode.INCONSISTENT_CLASS);
			} else {
				Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.INFO, "Export certificate: {0}",
						cert.getSerialNumber());
				return cert.getEncoded();
			}
		}
		return null;
	}

	private void importCertificate(final GXDLMSSettings settings, final ValueEventArgs e) {
		GXx509Certificate cert = new GXx509Certificate((byte[]) e.getParameters());
		byte[] st = getServerSystemTitle();
		if (st == null) {
			st = settings.getCipher().getSystemTitle();
		}
		String serverSubject = GXAsn1Converter.systemTitleToSubject(st);
		// If server certification is added.
		boolean isServerCert = cert.getSubject().contains(serverSubject);

		int usage = KeyUsage.toInteger(cert.getKeyUsage());
		if (usage != KeyUsage.KEY_AGREEMENT.getValue() && usage != KeyUsage.DIGITAL_SIGNATURE.getValue()
				&& usage != (KeyUsage.KEY_AGREEMENT.getValue() | KeyUsage.DIGITAL_SIGNATURE.getValue())) {
			// At least one bit must be used.
			e.setError(ErrorCode.INCONSISTENT_CLASS);
		} else {
			// Remove old certificate if it exists.
			List<GXx509Certificate> list = serverCertificates.getCertificates(cert.getKeyUsage());
			for (GXx509Certificate it : list) {
				boolean isServer = it.getSubject().contains(serverSubject);
				if (isServer == isServerCert) {
					serverCertificates.remove(it);
				}
			}
		}
		serverCertificates.add(cert);
		Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.INFO, "New certificate imported: {0}",
				cert.getSerialNumber());
	}

	private byte[] generateCertificateRequest(final GXDLMSSettings settings, final ValueEventArgs e) {
		CertificateType key = CertificateType.forValue(((Number) e.getParameters()).intValue());
		byte[] st = getServerSystemTitle();
		if (st == null) {
			st = settings.getCipher().getSystemTitle();
		}
		try {
			KeyPair kp = null;
			switch (key) {
			case DIGITAL_SIGNATURE:
				kp = signingKey;
				break;
			case KEY_AGREEMENT:
				kp = keyAgreement;
				break;
			case TLS:
				kp = tls;
				break;
			default:
				break;
			}
			if (kp == null) {
				e.setError(ErrorCode.INCONSISTENT_CLASS);
			} else {
				GXPkcs10 pkc10 = GXPkcs10.createCertificateSigningRequest(kp, GXAsn1Converter.systemTitleToSubject(st));
				return GXCommon.fromBase64(pkc10.toDer());
			}
		} catch (Exception e1) {
			e.setError(ErrorCode.INCONSISTENT_CLASS);
		}
		return null;
	}

	private void generateKeyPair(final GXDLMSSettings settings, final ValueEventArgs e) {
		CertificateType key = CertificateType.forValue(((Number) e.getParameters()).intValue());
		try {
			KeyPair value = GXEcdsa.generateKeyPair(Ecc.P256);
			switch (key) {
			case DIGITAL_SIGNATURE:
				signingKey = value;
				// If all associations are using same certifications.
				if (settings.getAssociationsShareCertificates()) {
					settings.getCipher().setSigningKeyPair(value);
				}
				break;
			case KEY_AGREEMENT:
				keyAgreement = value;
				// If all associations are using same certifications.
				if (settings.getAssociationsShareCertificates()) {
					settings.getCipher().setKeyAgreementKeyPair(value);
				}
			case TLS:
				tls = value;
				// If all associations are using same certifications.
				if (settings.getAssociationsShareCertificates()) {
					settings.getCipher().setKeyAgreementKeyPair(value);
				}
				break;
			default:
				e.setError(ErrorCode.INCONSISTENT_CLASS);
			}
		} catch (Exception e1) {
			e.setError(ErrorCode.INCONSISTENT_CLASS);
		}
	}

	private byte[] invokeKeyAgreement(final GXDLMSSettings settings, final ValueEventArgs e) {
		try {
			List<?> tmp = (List<?>) ((List<?>) e.getParameters()).get(0);
			short keyId = ((Number) tmp.get(0)).shortValue();
			if (keyId != 0) {
				e.setError(ErrorCode.READ_WRITE_DENIED);
			} else {
				byte[] data = (byte[]) tmp.get(1);
				// ephemeral public key
				GXByteBuffer data2 = new GXByteBuffer(65);
				data2.setUInt8(keyId);
				data2.set(data, 0, 64);
				GXByteBuffer sign = new GXByteBuffer();
				sign.set(data, 64, 64);
				PublicKey pk = settings.getCipher().getKeyAgreementKeyPair().getPublic();
				if (pk == null || !GXSecure.validateEphemeralPublicKeySignature(data2.array(), sign.array(), pk)) {
					e.setError(ErrorCode.READ_WRITE_DENIED);
					settings.setTargetEphemeralKey(null);
				} else {
					e.setByteArray(true);
					settings.setTargetEphemeralKey(GXAsn1Converter.getPublicKey(data2.subArray(1, 64)));
					// Generate ephemeral keys.
					KeyPair eKpS = settings.getCipher().getEphemeralKeyPair();
					eKpS = GXEcdsa.generateKeyPair(Ecc.P256);
					settings.getCipher().setEphemeralKeyPair(eKpS);
					// Generate shared secret.
					KeyAgreement ka = KeyAgreement.getInstance("ECDH");
					ka.init(eKpS.getPrivate());
					ka.doPhase(settings.getTargetEphemeralKey(), true);
					byte[] sharedSecret = ka.generateSecret();
					// settings.getCipher().setSharedSecret(sharedSecret);
					Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.FINEST, "Server shared secret: {0}",
							GXCommon.toHex(sharedSecret));
					GXByteBuffer bb = new GXByteBuffer();
					bb.setUInt8(DataType.ARRAY);
					bb.setUInt8(1);
					bb.setUInt8(DataType.STRUCTURE);
					bb.setUInt8(2);
					// Add key ID.
					bb.setUInt8(0x16);
					bb.setUInt8(0);
					bb.setUInt8(DataType.OCTET_STRING);
					GXCommon.setObjectCount(128, bb);
					data = GXSecure.getEphemeralPublicKeyData(keyId, eKpS.getPublic());
					bb.set(data, 1, 64);
					// Add signature.
					byte[] tmp2 = GXSecure.getEphemeralPublicKeySignature(keyId, eKpS.getPublic(),
							settings.getCipher().getSigningKeyPair().getPrivate());
					bb.set(tmp2);
					Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.FINEST, "Data: {0}",
							GXCommon.toHex(data));
					Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.FINEST, "Sign: {0}",
							GXCommon.toHex(tmp2));
					byte[] algID = GXCommon.hexToBytes("60857405080300"); // AES-GCM-128
					GXByteBuffer kdf = new GXByteBuffer();
					kdf.set(GXSecure.generateKDF("SHA-256", sharedSecret, 256, algID, settings.getSourceSystemTitle(),
							settings.getCipher().getSystemTitle(), null, null), 0, 16);
					Logger.getLogger(GXDLMSSecuritySetup.class.getName()).log(Level.INFO, "GUEK: {0}", kdf);
					settings.getCipher().setKeyAgreementScheme(KeyAgreementScheme.EPHEMERAL_UNIFIED_MODEL);

					switch (GlobalKeyType.values()[keyId]) {
					case BROADCAST_ENCRYPTION:
						gbek = kdf.array();
						break;
					case UNICAST_ENCRYPTION:
						guek = kdf.array();
						break;
					case AUTHENTICATION:
						gak = kdf.array();
						break;
					case KEK:
						kek = kdf.array();
						break;
					default:
						e.setError(ErrorCode.INCONSISTENT_CLASS);
						break;
					}
					return bb.array();
				}
			}
		} catch (Exception ex) {
			e.setError(ErrorCode.INCONSISTENT_CLASS);
		}
		return null;
	}

	private void keyTransfer(final GXDLMSSettings settings, final ValueEventArgs e) {
		// if settings.Cipher is null non secure server is used.
		// Keys are take in action after reply is generated.
		try {
			for (Object tmp : (List<?>) e.getParameters()) {
				List<?> item = (List<?>) tmp;
				GlobalKeyType type = GlobalKeyType.values()[((Number) item.get(0)).intValue()];
				byte[] data = (byte[]) item.get(1);
				switch (type) {
				case BROADCAST_ENCRYPTION:
					gbek = GXDLMSSecureClient.decrypt(settings.getKek(), data);
					break;
				case UNICAST_ENCRYPTION:
					guek = GXDLMSSecureClient.decrypt(settings.getKek(), data);
					break;
				case AUTHENTICATION:
					gak = GXDLMSSecureClient.decrypt(settings.getKek(), data);
					break;
				case KEK:
					kek = GXDLMSSecureClient.decrypt(settings.getKek(), data);
					break;
				default:
					e.setError(ErrorCode.INCONSISTENT_CLASS);
				}
			}
		} catch (Exception ex) {
			e.setError(ErrorCode.INCONSISTENT_CLASS);
		}
	}

	private boolean isAssigned(final GXDLMSSettings settings) {
		return settings.getAssignedAssociation() == null
				|| getLogicalName().compareTo(settings.getAssignedAssociation().getSecuritySetupReference()) == 0;
	}

	private void securityActivate(final GXDLMSSettings settings, final ValueEventArgs e) {
		Security security = settings.getCipher().getSecurity();
		if (getVersion() == 0) {
			Set<SecurityPolicy> policy = SecurityPolicy.forValue(((Number) e.getParameters()).byteValue());
			setSecurityPolicy(policy);
			if (isAssigned(settings)) {
				int val = SecurityPolicy.toInteger(policy);
				if (val == SecurityPolicy.AUTHENTICATED.getValue()) {
					settings.getCipher().setSecurity(Security.AUTHENTICATION);
				} else if (val == SecurityPolicy.ENCRYPTED.getValue()) {
					settings.getCipher().setSecurity(Security.ENCRYPTION);
				} else if (val == (SecurityPolicy.AUTHENTICATED.getValue() | SecurityPolicy.ENCRYPTED.getValue())) {
					settings.getCipher().setSecurity(Security.AUTHENTICATION_ENCRYPTION);
				}
			}
		} else if (getVersion() == 1) {
			java.util.Set<SecurityPolicy> policy = SecurityPolicy.forValue(((Number) e.getParameters()).byteValue());
			setSecurityPolicy(policy);
			if (isAssigned(settings)) {
				if (policy.contains(SecurityPolicy.AUTHENTICATED_RESPONSE)) {
					security = Security.forValue(security.getValue() | Security.AUTHENTICATION.getValue());
					settings.getCipher().setSecurity(security);
				}
				if (policy.contains(SecurityPolicy.ENCRYPTED_RESPONSE)) {
					security = Security.forValue(security.getValue() | Security.ENCRYPTION.getValue());
					settings.getCipher().setSecurity(security);
				}
			}
		}
	}

	/*
	 * Server uses this method to apply new keys.
	 */
	public final void applyKeys(final GXDLMSSettings settings, final ValueEventArgs e) {
		try {
			if (isAssigned(settings)) {
				for (Object tmp : (List<?>) e.getParameters()) {
					List<?> item = (List<?>) tmp;
					GlobalKeyType type = GlobalKeyType.values()[((Number) item.get(0)).intValue()];
					switch (type) {
					case UNICAST_ENCRYPTION:
						if (e.getIndex() == 2) {
							settings.getCipher().setBlockCipherKey(guek);

						} else {
							settings.setEphemeralBlockCipherKey(guek);
						}
						break;
					case BROADCAST_ENCRYPTION:
						if (e.getIndex() == 2) {
							settings.getCipher().setBroadcastBlockCipherKey(gbek);
						} else {
							settings.setEphemeralBroadcastBlockCipherKey(gbek);
						}
						break;
					case AUTHENTICATION:
						// if settings.Cipher is null non secure server is used.
						if (e.getIndex() == 2) {
							settings.getCipher().setAuthenticationKey(gak);
						} else {
							settings.setEphemeralAuthenticationKey(gak);
						}
						break;
					case KEK:
						settings.setKek(kek);
						break;
					default:
						e.setError(ErrorCode.INCONSISTENT_CLASS);
					}
				}
			}
		} catch (Exception ex) {
			e.setError(ErrorCode.INCONSISTENT_CLASS);
		}
	}

	/**
	 * Find certificate using entity information.
	 * 
	 * @param settings    DLMS Settings.
	 * @param entity      Certificate entity type.
	 * @param type        Certificate type.
	 * @param systemtitle System title.
	 * @return
	 */
	private static GXx509Certificate findCertificateByEntity(final GXx509CertificateCollection certificates,
			final CertificateEntity entity, final CertificateType type, final byte[] systemtitle) {
		String subject = GXAsn1Converter.systemTitleToSubject(systemtitle);
		int k = KeyUsage.toInteger(GXAsn1Converter.certificateTypeToKeyUsage(type));
		for (GXx509Certificate it : certificates) {
			if (KeyUsage.toInteger(it.getKeyUsage()) == k && it.getSubject().contains(subject)) {
				return it;
			}
		}
		return null;
	}

	@Override
	public final int[] getAttributeIndexToRead(final boolean all) {
		java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
		// LN is static and read only once.
		if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
			attributes.add(1);
		}
		// SecurityPolicy
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// SecuritySuite
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// ClientSystemTitle
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// ServerSystemTitle
		if (all || canRead(5)) {
			attributes.add(5);
		}
		if (getVersion() != 0) {
			// Certificates
			if (all || canRead(6)) {
				attributes.add(6);
			}
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		if (getVersion() == 0) {
			return 5;
		}
		return 6;
	}

	/*
	 * Returns amount of methods.
	 */
	@Override
	public final int getMethodCount() {
		if (getVersion() == 0) {
			return 2;
		}
		return 8;
	}

	@Override
	public final DataType getDataType(final int index) {
		if (index == 1) {
			return DataType.OCTET_STRING;
		}
		if (index == 2) {
			return DataType.ENUM;
		}
		if (index == 3) {
			return DataType.ENUM;
		}
		if (index == 4) {
			return DataType.OCTET_STRING;
		}
		if (index == 5) {
			return DataType.OCTET_STRING;
		}
		if (getVersion() > 0) {
			if (index == 6) {
				return DataType.ARRAY;
			}
			throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
		} else {
			throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
		}
	}

	/*
	 * Get certificates as byte buffer.
	 */
	private byte[] getCertificatesByteArray(final GXDLMSSettings settings) {
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8((byte) DataType.ARRAY.getValue());
		GXCommon.setObjectCount(serverCertificates.size(), bb);
		for (GXx509Certificate it : serverCertificates) {
			bb.setUInt8((byte) DataType.STRUCTURE.getValue());
			GXCommon.setObjectCount(6, bb);
			bb.setUInt8((byte) DataType.ENUM.getValue());
			if (it.isBasicConstraints()) {
				bb.setUInt8((byte) CertificateEntity.CERTIFICATION_AUTHORITY.getValue());
			} else if (it.getSubject()
					.contains(GXAsn1Converter.systemTitleToSubject(settings.getCipher().getSystemTitle()))) {
				bb.setUInt8((byte) CertificateEntity.SERVER.getValue());
			} else {
				bb.setUInt8((byte) CertificateEntity.CLIENT.getValue());
			}
			bb.setUInt8((byte) DataType.ENUM.getValue());
			if (it.getKeyUsage().contains(KeyUsage.DIGITAL_SIGNATURE)
					&& it.getKeyUsage().contains(KeyUsage.KEY_AGREEMENT)) {
				bb.setUInt8((byte) CertificateType.TLS.getValue());
			} else if (it.getKeyUsage().contains(KeyUsage.DIGITAL_SIGNATURE)) {
				bb.setUInt8((byte) CertificateType.DIGITAL_SIGNATURE.getValue());
			} else if (it.getKeyUsage().contains(KeyUsage.KEY_AGREEMENT)) {
				bb.setUInt8((byte) CertificateType.KEY_AGREEMENT.getValue());
			} else {
				bb.setUInt8((byte) CertificateType.OTHER.getValue());
			}
			bb.setUInt8(DataType.OCTET_STRING.getValue());
			byte[] tmp = it.getSerialNumber().toByteArray();
			bb.setUInt8(tmp.length);
			bb.set(tmp);
			GXCommon.addString(it.getIssuer(), bb);
			GXCommon.addString(it.getSubject(), bb);
			GXCommon.addString("", bb);
		}
		return bb.array();
	}

	/*
	 * Returns value of given attribute.
	 */
	@Override
	public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			return GXCommon.logicalNameToBytes(getLogicalName());
		}
		if (e.getIndex() == 2) {
			return SecurityPolicy.toInteger(securityPolicy);
		}
		if (e.getIndex() == 3) {
			return getSecuritySuite().getValue();
		}
		if (e.getIndex() == 4) {
			return getClientSystemTitle();
		}
		if (e.getIndex() == 5) {
			return getServerSystemTitle();
		}
		if (e.getIndex() == 6) {
			return getCertificatesByteArray(settings);
		}
		e.setError(ErrorCode.READ_WRITE_DENIED);
		return null;
	}

	private void updateSertificates(final List<?> list) {
		certificates.clear();
		if (list != null) {
			for (Object tmp : list) {
				List<?> it = (List<?>) tmp;
				GXDLMSCertificateInfo info = new GXDLMSCertificateInfo();
				info.setEntity(CertificateEntity.forValue(((Number) it.get(0)).intValue()));
				info.setType(CertificateType.forValue(((Number) it.get(1)).intValue()));
				info.setSerialNumber(new BigInteger((byte[]) it.get(2)));
				info.setIssuer(new String((byte[]) it.get(3)));
				info.setSubject(new String((byte[]) it.get(4)));
				info.setSubjectAltName(new String((byte[]) it.get(5)));
				certificates.add(info);
			}
		}
	}

	/*
	 * Set value of given attribute.
	 */
	@Override
	public final void setValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			setLogicalName(GXCommon.toLogicalName(e.getValue()));
		} else if (e.getIndex() == 2) {
			// Security level is set with action.
			if (settings.isServer()) {
				e.setError(ErrorCode.INCONSISTENT_CLASS);
			} else {
				securityPolicy = SecurityPolicy.forValue(((Number) e.getValue()).intValue());
			}
		} else if (e.getIndex() == 3) {
			setSecuritySuite(SecuritySuite.forValue(((Number) e.getValue()).byteValue()));
		} else if (e.getIndex() == 4) {
			if (e.getValue() != null && ((byte[]) e.getValue()).length != 8 && ((byte[]) e.getValue()).length != 0) {
				e.setError(ErrorCode.INCONSISTENT_CLASS);
			} else {
				setClientSystemTitle((byte[]) e.getValue());
			}
		} else if (e.getIndex() == 5) {
			if (((byte[]) e.getValue()).length != 8) {
				e.setError(ErrorCode.INCONSISTENT_CLASS);
			} else {
				setServerSystemTitle((byte[]) e.getValue());
			}
		} else if (e.getIndex() == 6) {
			updateSertificates((List<?>) e.getValue());
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		securityPolicy = SecurityPolicy.forValue(reader.readElementContentAsInt("SecurityPolicy"));
		// Handle old way.
		int value = reader.readElementContentAsInt("SecurityPolicy0");
		if (value != 0) {
			securityPolicy = SecurityPolicy.forValue(value);
		}
		securitySuite = SecuritySuite.values()[reader.readElementContentAsInt("SecuritySuite")];
		String str = reader.readElementContentAsString("ClientSystemTitle");
		if (str == null) {
			clientSystemTitle = null;
		} else {
			clientSystemTitle = GXDLMSTranslator.hexToBytes(str);
		}
		str = reader.readElementContentAsString("ServerSystemTitle");
		if (str == null) {
			serverSystemTitle = null;
		} else {
			serverSystemTitle = GXDLMSTranslator.hexToBytes(str);
		}
		certificates.clear();
		if (reader.isStartElement("Certificates", true)) {
			while (reader.isStartElement("Item", true)) {
				GXDLMSCertificateInfo it = new GXDLMSCertificateInfo();
				certificates.add(it);
				it.setEntity(CertificateEntity.forValue(reader.readElementContentAsInt("Entity")));
				it.setType(CertificateType.forValue(reader.readElementContentAsInt("Type")));
				it.setSerialNumber(new BigInteger(reader.readElementContentAsString("SerialNumber")));
				it.setIssuer(reader.readElementContentAsString("Issuer"));
				it.setSubject(reader.readElementContentAsString("Subject"));
				it.setSubjectAltName(reader.readElementContentAsString("SubjectAltName"));
			}
			reader.readEndElement("Certificates");
		}

		str = reader.readElementContentAsString("SigningKey");
		if (str == null) {
			signingKey = null;
		} else {
			GXPkcs8 pk = GXPkcs8.fromDer(str);
			signingKey = new KeyPair(pk.getPublicKey(), pk.getPrivateKey());
		}
		str = reader.readElementContentAsString("KeyAgreement");
		if (str == null) {
			keyAgreement = null;
		} else {
			GXPkcs8 pk = GXPkcs8.fromDer(str);
			keyAgreement = new KeyPair(pk.getPublicKey(), pk.getPrivateKey());
		}
		str = reader.readElementContentAsString("TLS");
		if (str == null) {
			tls = null;
		} else {
			GXPkcs8 pk = GXPkcs8.fromDer(str);
			tls = new KeyPair(pk.getPublicKey(), pk.getPrivateKey());
		}
		serverCertificates.clear();
		if (reader.isStartElement("ServerCertificates", true)) {
			while (reader.isStartElement("Cert", false)) {
				GXx509Certificate cert = GXx509Certificate.fromDer(reader.readElementContentAsString("Cert"));
				if (serverCertificates.find(cert) == null) {
					serverCertificates.add(cert);
				}
			}
			reader.readEndElement("ServerCertificates");
		}
		str = reader.readElementContentAsString("Guek");
		if (str != null) {
			guek = GXCommon.hexToBytes(str);
		}
		str = reader.readElementContentAsString("Gbek");
		if (str != null) {
			gbek = GXCommon.hexToBytes(str);
		}
		str = reader.readElementContentAsString("Gak");
		if (str != null) {
			gak = GXCommon.hexToBytes(str);
		}
		str = reader.readElementContentAsString("Kek");
		if (str != null) {
			kek = GXCommon.hexToBytes(str);
		}
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("SecurityPolicy", SecurityPolicy.toInteger(securityPolicy));
		writer.writeElementString("SecuritySuite", securitySuite.ordinal());
		writer.writeElementString("ClientSystemTitle", GXDLMSTranslator.toHex(clientSystemTitle));
		writer.writeElementString("ServerSystemTitle", GXDLMSTranslator.toHex(serverSystemTitle));
		if (certificates != null && certificates.size() != 0) {
			writer.writeStartElement("Certificates");
			for (GXDLMSCertificateInfo it : certificates) {
				writer.writeStartElement("Item");
				writer.writeElementString("Entity", it.getEntity().getValue());
				writer.writeElementString("Type", it.getType().getValue());
				writer.writeElementString("SerialNumber", it.getSerialNumber().toString());
				writer.writeElementString("Issuer", it.getIssuer());
				writer.writeElementString("Subject", it.getSubject());
				writer.writeElementString("SubjectAltName", it.getSubjectAltName());
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
		// If all associations are using the same certificates.
		if (serverSettings != null && serverSettings.getAssociationsShareCertificates()) {
			signingKey = serverSettings.getCipher().getSigningKeyPair();
			keyAgreement = serverSettings.getCipher().getKeyAgreementKeyPair();
		}

		if (signingKey != null) {
			GXPkcs8 kp = new GXPkcs8(signingKey);
			writer.writeElementString("SigningKey", kp.toDer());
		}
		if (keyAgreement != null) {
			GXPkcs8 kp = new GXPkcs8(keyAgreement);
			writer.writeElementString("KeyAgreement", kp.toDer());
		}
		if (tls != null) {
			GXPkcs8 kp = new GXPkcs8(tls);
			writer.writeElementString("TLS", kp.toDer());
		}
		if (serverCertificates.size() != 0) {
			writer.writeStartElement("ServerCertificates");
			for (GXx509Certificate it : serverCertificates) {
				writer.writeElementString("Cert", it.toDer());
			}
			writer.writeEndElement();
		}
		if (guek != null) {
			writer.writeElementString("Guek", GXCommon.toHex(guek));
		}
		if (gbek != null) {
			writer.writeElementString("Gbek", GXCommon.toHex(gbek));
		}
		if (gak != null) {
			writer.writeElementString("Gak", GXCommon.toHex(gak));
		}
		if (kek != null) {
			writer.writeElementString("Kek", GXCommon.toHex(kek));
		}
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public void setSettings(GXDLMSSettings settings) {
		serverSettings = settings;
		// If all associations are using same certifications.
		if (settings.getAssociationsShareCertificates() && settings.getCipher().getCertificates().size() == 0) {
			GXx509CertificateCollection tmp = serverCertificates;
			serverCertificates = settings.getCipher().getCertificates();
			serverCertificates.addAll(tmp);
			serverSettings.getCipher().setSigningKeyPair(signingKey);
			serverSettings.getCipher().setKeyAgreementKeyPair(keyAgreement);
		}
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "Security Policy", "Security Suite", "Client System Title",
				"Server System Title" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Security activate", "Key transfer" };
	}
}