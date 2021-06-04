//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSCertificateException;
import gurux.dlms.asn.enums.ExtendedKeyUsage;
import gurux.dlms.asn.enums.HashAlgorithm;
import gurux.dlms.asn.enums.KeyUsage;
import gurux.dlms.asn.enums.PkcsType;
import gurux.dlms.asn.enums.X509Certificate;
import gurux.dlms.asn.enums.X509Name;
import gurux.dlms.enums.BerType;
import gurux.dlms.internal.GXCommon;

/**
 * x509 Certificate. https://tools.ietf.org/html/rfc5280
 */
public class GXx509Certificate {
	/**
	 * Loaded x509 Certificate certificate as a raw data.
	 */
	private byte[] rawData;

	/**
	 * This extension identifies the public key being certified.
	 */
	private byte[] subjectKeyIdentifier;
	/**
	 * May be used either as a certificate or CRL extension. It identifies the
	 * public key to be used to verify the signature on this certificate or CRL. It
	 * enables distinct keys used by the same CA to be distinguished.
	 */
	private byte[] authorityKeyIdentifier;

	/**
	 * Authority certification serial number.
	 */
	private byte[] authorityCertificationSerialNumber;

	/**
	 * Indicates if the subject may act as a CA.
	 */
	private boolean basicConstraints;

	/**
	 * Signature algorithm.
	 */
	private HashAlgorithm signatureAlgorithm;
	/**
	 * Signature parameters.
	 */
	private Object signatureParameters;

	/**
	 * Public key.
	 */
	private PublicKey publicKey;

	/**
	 * Algorithm.
	 */
	private HashAlgorithm publicKeySignature;

	/**
	 * Parameters.
	 */
	private Object parameters;

	/**
	 * Signature.
	 */
	private byte[] signature;

	/**
	 * Subject. Example: "CN=4142434445464748, O=Gurux, L=Tampere, C=FI".
	 */
	private String subject;

	/**
	 * Issuer. Example: "CN=Gurux O=Gurux, L=Tampere, C=FI".
	 */
	private String issuer;

	/**
	 * Authority Cert Issuer. Example: "CN=Test O=Gurux, L=Tampere, C=FI".
	 */
	private String authorityCertIssuer;

	/**
	 * Serial number.
	 */
	private BigInteger serialNumber;

	/**
	 * Version.
	 */
	private CertificateVersion version;
	/**
	 * Validity from.
	 */
	private Date validFrom;
	/**
	 * Validity to.
	 */
	private Date validTo;

	/**
	 * Indicates the purpose for which the certified public key is used.
	 */
	private Set<KeyUsage> keyUsage = new HashSet<KeyUsage>();

	/**
	 * Indicates that a certificate can be used as an TLS server or client
	 * certificate.
	 */
	private Set<ExtendedKeyUsage> extendedKeyUsage = new HashSet<ExtendedKeyUsage>();

	/**
	 * Constructor.
	 */
	public GXx509Certificate() {
		version = CertificateVersion.V3;
	}

	/**
	 * 
	 * @param cert Certificate.
	 * @return Return default file path.
	 */
	public static Path getFilePath(GXx509Certificate cert) {
		String path;
		if (cert.getKeyUsage().contains(KeyUsage.DIGITAL_SIGNATURE)
				&& cert.getKeyUsage().contains(KeyUsage.KEY_AGREEMENT)) {
			path = "T";
		} else if (cert.getKeyUsage().contains(KeyUsage.DIGITAL_SIGNATURE)) {
			path = "D";
		} else if (cert.getKeyUsage().contains(KeyUsage.KEY_AGREEMENT)) {
			path = "A";
		} else {
			throw new IllegalArgumentException("Unknown certificate type.");
		}
		path += GXAsn1Converter.hexSystemTitleFromSubject(cert.getSubject()).trim() + ".pem";
		if (cert.getPublicKey().getEncoded().length < 100) {
			path = Paths.get("Certificates", path).toString();
		} else {
			path = Paths.get("Certificates384", path).toString();
		}
		return Paths.get(path);
	}

	/**
	 * Constructor.
	 * 
	 * @param data Base64 string.
	 * @deprecated use {@link fromPem} instead.
	 */
	public GXx509Certificate(final String data) {
		final String START = "BEGIN CERTIFICATE-----\n";
		final String END = "-----END";
		final String tmp = data.replace("\r\n", "\n");
		int start = tmp.indexOf(START);
		if (start == -1) {
			throw new IllegalArgumentException("Invalid PEM file.");
		}
		int end = tmp.indexOf(END);
		if (end == -1) {
			throw new IllegalArgumentException("Invalid PEM file.");
		}
		init(GXCommon.fromBase64(tmp.substring(start + START.length(), end)));
	}

	/**
	 * Create x509Certificate from PEM string.
	 * 
	 * @param data PEM string.
	 * @return x509 certificate.
	 */
	public static GXx509Certificate fromPem(final String data) {
		final String START = "BEGIN CERTIFICATE-----\n";
		final String END = "-----END";
		final String tmp = data.replace("\r\n", "\n");
		int start = tmp.indexOf(START);
		if (start == -1) {
			throw new IllegalArgumentException("Invalid PEM file.");
		}
		int end = tmp.indexOf(END);
		if (end == -1) {
			throw new IllegalArgumentException("Invalid PEM file.");
		}
		return fromDer(tmp.substring(start + START.length(), end));
	}

	/**
	 * Create x509Certificate from DER Base64 encoded string.
	 * 
	 * @param data Base64 DER string.
	 * @return x509 certificate.
	 */
	public static GXx509Certificate fromDer(final String data) {
		GXx509Certificate cert = new GXx509Certificate();
		cert.init(GXCommon.fromBase64(data));
		return cert;
	}

	static String getAlgorithm(final String algorithm) {
		if (algorithm.endsWith("RSA")) {
			return "RSA";
		} else if (algorithm.endsWith("ECDSA")) {
			return "EC";
		} else {
			throw new IllegalStateException("Unknown algorithm:" + algorithm);
		}
	}

	private void init(final byte[] data) {
		rawData = data;
		GXAsn1Sequence seq = (GXAsn1Sequence) GXAsn1Converter.fromByteArray(data);
		if (seq.size() != 3) {
			throw new IllegalArgumentException("Wrong number of elements in sequence.");
		}
		if (!(seq.get(0) instanceof GXAsn1Sequence)) {
			PkcsType type = GXAsn1Converter.getCertificateType(data, seq);
			switch (type) {
			case PKCS_8:
				throw new GXDLMSCertificateException(
						"Invalid Certificate. This is PKCS 8 private key, not x509 certificate.");
			case PKCS_10:
				throw new GXDLMSCertificateException(
						"Invalid Certificate. This is PKCS 10 certification requests, not x509 certificate.");
			default:
				throw new GXDLMSCertificateException("Invalid Certificate Version.");
			}
		}
		GXAsn1Sequence reqInfo = (GXAsn1Sequence) seq.get(0);
		if (!(reqInfo.get(0) instanceof GXAsn1Context)) {
			throw new GXDLMSCertificateException("Invalid Certificate Version.");
		}

		version = CertificateVersion.forValue(((Number) ((GXAsn1Context) reqInfo.get(0)).get(0)).byteValue());
		if (reqInfo.get(1) instanceof GXAsn1Integer) {
			serialNumber = new BigInteger(((GXAsn1Integer) reqInfo.get(1)).getByteArray()).abs();
		} else {
			serialNumber = BigInteger.valueOf(((Number) reqInfo.get(1)).longValue());
		}
		String tmp = ((GXAsn1Sequence) reqInfo.get(2)).get(0).toString();
		// Signature Algorithm
		signatureAlgorithm = HashAlgorithm.forValue(tmp);
		if (signatureAlgorithm != HashAlgorithm.SHA256withECDSA) {
			throw new IllegalArgumentException("DLMS certificate must be signed with ecdsa-with-SHA256.");
		}
		// Optional.
		if (((GXAsn1Sequence) reqInfo.get(2)).size() > 1) {
			parameters = ((GXAsn1Sequence) reqInfo.get(2)).get(1);
		}
		// Issuer
		issuer = GXAsn1Converter.getSubject((GXAsn1Sequence) reqInfo.get(3));
		boolean basicConstraintsExists = false;
		// Validity
		validFrom = (Date) ((GXAsn1Sequence) reqInfo.get(4)).get(0);
		validTo = (Date) ((GXAsn1Sequence) reqInfo.get(4)).get(1);
		subject = GXAsn1Converter.getSubject((GXAsn1Sequence) reqInfo.get(5));
		// subject public key Info
		GXAsn1Sequence subjectPKInfo = (GXAsn1Sequence) reqInfo.get(6);
		// Get Standard Extensions.
		if (reqInfo.size() > 7) {
			for (Object it : (GXAsn1Sequence) ((GXAsn1Context) reqInfo.get(7)).get(0)) {
				GXAsn1Sequence s = (GXAsn1Sequence) it;
				GXAsn1ObjectIdentifier id = (GXAsn1ObjectIdentifier) s.get(0);
				Object value = s.get(1);
				X509Certificate t = X509Certificate.forValue(id.toString());
				switch (t) {
				case SUBJECT_KEY_IDENTIFIER:
					subjectKeyIdentifier = (byte[]) GXAsn1Converter.fromByteArray((byte[]) value);
					break;
				case AUTHORITY_KEY_IDENTIFIER:
					for (Object i : (GXAsn1Sequence) value) {
						GXAsn1Context a = (GXAsn1Context) i;
						switch (a.getIndex()) {
						case 0:
							// Authority Key Identifier.
							authorityKeyIdentifier = (byte[]) a.get(0);
							break;
						case 1: {
							StringBuilder sb = new StringBuilder();
							// authorityCertIssuer
							for (Object kp : ((GXAsn1Sequence) ((GXAsn1Context) a.get(0)).get(0))) {
								Map.Entry<?, ?> it2 = (Map.Entry<?, ?>) kp;
								if (sb.length() != 0) {
									sb.append(", ");
								}
								sb.append(X509Name.forValue(String.valueOf(it2.getKey())));
								sb.append("=");
								sb.append(String.valueOf(it2.getValue()));
							}
							authorityCertIssuer = sb.toString();
						}
							break;
						case 2:
							// Authority cert serial number.
							authorityCertificationSerialNumber = (byte[]) a.get(0);
							break;
						default:
							throw new IllegalArgumentException("Invalid context." + a.getIndex());
						}
					}
					break;
				case KEY_USAGE:
					if (value instanceof GXAsn1BitString) {
						// critical is optional. BOOLEAN DEFAULT FALSE,
						keyUsage = KeyUsage.forValue(((GXAsn1BitString) value).toInteger());
					} else if (value instanceof Boolean) {
						value = s.get(2);
						keyUsage = KeyUsage.forValue(((GXAsn1BitString) value).toInteger());
					} else {
						throw new IllegalStateException("Invalid key usage.");
					}
					break;
				case EXTENDED_KEY_USAGE:
					if (value instanceof GXAsn1Sequence) {
						for (Object tmp2 : (GXAsn1Sequence) value) {
							GXAsn1ObjectIdentifier eku = (GXAsn1ObjectIdentifier) tmp2;
							if ("1.3.6.1.5.5.7.3.1".compareTo(eku.getObjectIdentifier()) == 0) {
								extendedKeyUsage.add(ExtendedKeyUsage.SERVER_AUTH);
							} else if ("1.3.6.1.5.5.7.3.2".compareTo(eku.getObjectIdentifier()) == 0) {
								extendedKeyUsage.add(ExtendedKeyUsage.CLIENT_AUTH);
							} else {
								throw new IllegalStateException("Invalid extended key usage.");
							}
						}
					} else {
						throw new IllegalStateException("Invalid extended key usage.");
					}
					break;
				case BASIC_CONSTRAINTS:
					basicConstraintsExists = true;
					if (value instanceof GXAsn1Sequence) {
						if (((GXAsn1Sequence) value).size() != 0) {
							basicConstraints = (boolean) ((GXAsn1Sequence) value).get(0);
						}
					} else if (value instanceof Boolean) {
						basicConstraints = (Boolean) value;
					} else {
						throw new IllegalStateException("Invalid key usage.");
					}
					break;
				default:
					Logger.getLogger(GXx509Certificate.class.getName()).log(Level.SEVERE,
							"Unknown extensions: " + t.toString());
				}
			}
		}

		if (!basicConstraintsExists) {
			// Verify that Common Name is included to system title.
			boolean commonNameFound = false;
			for (Object tmp2 : (GXAsn1Sequence) reqInfo.get(5)) {
				Map.Entry<?, ?> it = (Map.Entry<?, ?>) tmp2;
				if (X509Name.CN.getValue().equals((String.valueOf(it.getKey())))) {
					if (it.getValue().toString().length() != 16) {
						throw new GXDLMSCertificateException("System title is not included in Common Name.");
					}
					commonNameFound = true;
					break;
				}
			}
			if (!commonNameFound) {
				throw new GXDLMSCertificateException("common name doesn't exist.");
			}

		}
		if (keyUsage == null || keyUsage.isEmpty()) {
			throw new IllegalArgumentException("Key usage not present. It's mandotory.");
		}
		if ((keyUsage.contains(KeyUsage.KEY_CERT_SIGN) || keyUsage.contains(KeyUsage.CRL_SIGN))
				&& !basicConstraintsExists) {
			throw new IllegalArgumentException("Basic Constraints value not present. It's mandotory.");
		}
		if (keyUsage.contains(KeyUsage.DIGITAL_SIGNATURE) && keyUsage.contains(KeyUsage.KEY_AGREEMENT)
				&& extendedKeyUsage.isEmpty()) {
			throw new IllegalArgumentException("Extended key usage not present. It's mandotory for TLS.");
		}
		if (extendedKeyUsage.isEmpty() && keyUsage.contains(KeyUsage.DIGITAL_SIGNATURE)
				&& keyUsage.contains(KeyUsage.KEY_AGREEMENT)) {
			throw new IllegalArgumentException("Extended key usage present. It's used only for TLS.");
		}

		// Make public key.
		KeyFactory eckf;
		try {
			eckf = KeyFactory.getInstance(getAlgorithm(signatureAlgorithm.toString()));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					signatureAlgorithm.name().substring(0, 2) + "key factory not present in runtime");
		}
		try {
			byte[] encodedKey = GXAsn1Converter.toByteArray(subjectPKInfo);
			X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
			publicKey = eckf.generatePublic(ecpks);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		publicKeySignature = HashAlgorithm.forValue(((GXAsn1Sequence) seq.get(1)).get(0).toString());
		// Optional.
		if (((GXAsn1Sequence) seq.get(1)).size() > 1) {
			signatureParameters = ((GXAsn1Sequence) seq.get(1)).get(1);
		}
		// signature
		signature = ((GXAsn1BitString) seq.get(2)).getValue();
	}

	/**
	 * Constructor.
	 * 
	 * @param data Encoded bytes.
	 */
	public GXx509Certificate(final byte[] data) {
		init(data);
	}

	/**
	 * @return Subject.
	 */
	public final String getSubject() {
		return subject;
	}

	/**
	 * @param value Subject.
	 */
	public final void setSubject(final String value) {
		subject = value;
	}

	/**
	 * @return Issuer.
	 */
	public final String getIssuer() {
		return issuer;
	}

	/**
	 * @param value Issuer.
	 */
	public final void setIssuer(final String value) {
		issuer = value;
	}

	/**
	 * @return Serial number.
	 */
	public final BigInteger getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param value Serial number.
	 */
	public final void setSerialNumber(final BigInteger value) {
		serialNumber = value;
	}

	/**
	 * @return Version number.
	 */
	public final CertificateVersion getVersion() {
		return version;
	}

	/**
	 * @param value Version number.
	 */
	public final void setVersion(final CertificateVersion value) {
		version = value;
	}

	/**
	 * @return Validity from.
	 */
	public final Date getValidFrom() {
		return validFrom;
	}

	/**
	 * @param value Validity from.
	 */
	public final void setValidFrom(final Date value) {
		validFrom = value;
	}

	/**
	 * @return Validity to.
	 */
	public final Date getValidTo() {
		return validTo;
	}

	/**
	 * @param value Validity to.
	 */
	public final void setValidTo(final Date value) {
		validTo = value;
	}

	/**
	 * @return Signature algorithm
	 */
	public final HashAlgorithm getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	/**
	 * @param value Signature algorithm.
	 */
	public final void setSignatureAlgorithm(final HashAlgorithm value) {
		signatureAlgorithm = value;
	}

	/**
	 * @return Parameters.
	 */
	public final Object getParameters() {
		return parameters;
	}

	/**
	 * @param value Parameters.
	 */
	public final void setParameters(final Object value) {
		parameters = value;
	}

	/**
	 * @return Public key.
	 */
	public final PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * @param value Public key.
	 */
	public final void setPublicKey(final PublicKey value) {
		publicKey = value;
	}

	/**
	 * @return Signature.
	 */
	public final byte[] getSignature() {
		return signature;
	}

	/**
	 * @param value Signature.
	 */
	public final void setSignature(final byte[] value) {
		signature = value;
	}

	private Object[] getdata() {
		GXAsn1ObjectIdentifier a = new GXAsn1ObjectIdentifier(signatureAlgorithm.getValue());
		GXAsn1Context p = new GXAsn1Context();
		p.add(version.getValue());
		GXAsn1Sequence s = new GXAsn1Sequence();
		GXAsn1Sequence s1;
		if (subjectKeyIdentifier != null) {
			s1 = new GXAsn1Sequence();
			s1.add(new GXAsn1ObjectIdentifier(X509Certificate.SUBJECT_KEY_IDENTIFIER.getValue()));
			GXByteBuffer bb = new GXByteBuffer();
			bb.setUInt8(BerType.OCTET_STRING);
			GXCommon.setObjectCount(subjectKeyIdentifier.length, bb);
			bb.set(subjectKeyIdentifier);
			s1.add(bb.array());
			s.add(s1);
		}
		if (authorityKeyIdentifier != null || authorityCertIssuer != null
				|| authorityCertificationSerialNumber != null) {
			s1 = new GXAsn1Sequence();
			s1.add(new GXAsn1ObjectIdentifier(X509Certificate.AUTHORITY_KEY_IDENTIFIER.getValue()));
			s.add(s1);
			GXAsn1Context s2 = new GXAsn1Context();
			s2.setIndex(3);
			GXAsn1Sequence c1 = new GXAsn1Sequence();
			if (authorityKeyIdentifier != null) {
				GXAsn1Context c4 = new GXAsn1Context();
				c4.setConstructed(false);
				c4.setIndex(0);
				c4.add(authorityKeyIdentifier);
				c1.add(c4);
				s1.add(GXAsn1Converter.toByteArray(c1));
			}
			if (authorityCertIssuer != null) {
				GXAsn1Context c2 = new GXAsn1Context();
				c2.setIndex(1);
				c1.add(c2);
				GXAsn1Context c3 = new GXAsn1Context();
				c3.setIndex(4);
				c2.add(c3);
				c3.add(GXAsn1Converter.encodeSubject(authorityCertIssuer));
				s2.add(c1);
			}
			if (authorityCertificationSerialNumber != null) {
				GXAsn1Context c4 = new GXAsn1Context();
				c4.setConstructed(false);
				c4.setIndex(2);
				c4.add(authorityCertificationSerialNumber);
				c1.add(c4);
				s1.add(GXAsn1Converter.toByteArray(c1));
			}
		}
		// BasicConstraints
		s1 = new GXAsn1Sequence();
		s1.add(new GXAsn1ObjectIdentifier(X509Certificate.BASIC_CONSTRAINTS.getValue()));
		GXAsn1Sequence seq = new GXAsn1Sequence();
		if (basicConstraints) {
			// BasicConstraints is critical if it exists.
			s1.add(basicConstraints);
		}
		s1.add(GXAsn1Converter.toByteArray(seq));
		s.add(s1);
		if (keyUsage == null || keyUsage.isEmpty()) {
			throw new IllegalArgumentException("Key usage not present.");
		}
		s1 = new GXAsn1Sequence();
		s1.add(new GXAsn1ObjectIdentifier(X509Certificate.KEY_USAGE.getValue()));
		int value = 0;
		int min = 255;
		for (KeyUsage it : keyUsage) {
			short val = GXCommon.swapBits((byte) it.getValue());
			value |= val;
			if (val < min) {
				min = val;
			}
		}
		int ignore = 0;
		while ((min >>= 1) != 0) {
			++ignore;
		}
		byte[] tmp = GXAsn1Converter.toByteArray(new GXAsn1BitString(new byte[] { (byte) (ignore % 8), (byte) value }));
		s1.add(tmp);
		s.add(s1);

		GXAsn1Sequence valid = new GXAsn1Sequence();
		valid.add(validFrom);
		valid.add(validTo);
		Object[] list;
		Object[] tmp3 = new Object[] { new GXAsn1ObjectIdentifier("1.2.840.10045.2.1"),
				new GXAsn1ObjectIdentifier("1.2.840.10045.3.1.7") };
		GXAsn1Context tmp4 = new GXAsn1Context();
		tmp4.setIndex(3);
		tmp4.add(s);
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(4);
		bb.set(GXAsn1Converter.rawValue(publicKey));
		Object[] tmp2 = new Object[] { tmp3, new GXAsn1BitString(bb.array(), 0) };
		Object[] p2;
		if (signatureParameters == null) {
			p2 = new Object[] { a };
		} else {
			p2 = new Object[] { a, signatureParameters };
		}
		list = new Object[] { p, new GXAsn1Integer(serialNumber.toByteArray()), p2,
				GXAsn1Converter.encodeSubject(issuer), valid, GXAsn1Converter.encodeSubject(subject), tmp2, tmp4 };
		return list;
	}

	/**
	 * 
	 * @return Encoded x509 certificate.
	 */
	public final byte[] getEncoded() {
		if (rawData != null) {
			return rawData;
		}
		Object tmp = new Object[] { new GXAsn1ObjectIdentifier(signatureAlgorithm.getValue()) };
		Object[] list = new Object[] { getdata(), tmp, new GXAsn1BitString(signature, 0) };
		return GXAsn1Converter.toByteArray(list);
	}

	/**
	 * Sign certificate.
	 * 
	 * @param kp            Public and Private key.
	 * @param hashAlgorithm Used signature algorithm.
	 */
	public void sign(final KeyPair kp, final HashAlgorithm hashAlgorithm) {
		byte[] data = GXAsn1Converter.toByteArray(getdata());
		try {
			Signature instance = Signature.getInstance(hashAlgorithm.toString());
			instance.initSign(kp.getPrivate());
			instance.update(data);
			signatureAlgorithm = hashAlgorithm;
			signature = instance.sign();
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public final String toString() {
		StringBuilder bb = new StringBuilder();
		bb.append("Version: ");
		bb.append(version.toString());
		bb.append("\n");
		bb.append("Serial Number: ");
		bb.append(serialNumber.toString());
		bb.append("\n");
		bb.append("Signature Algorithm: ");
		if (signatureAlgorithm != null) {
			bb.append(signatureAlgorithm.toString());
			bb.append(", OID = ");
			bb.append(signatureAlgorithm.getValue());
		}
		bb.append("\n");

		bb.append("Issuer: ");
		bb.append(issuer);
		bb.append("\n");
		bb.append("Validity: [From: ");
		bb.append(validFrom.toString());
		bb.append(", \n");
		bb.append("To: ");
		bb.append(validTo.toString());
		bb.append("]\n");
		bb.append("Subject Public Key Info:\n");
		bb.append("Public Key Algorythm: ");
		bb.append(publicKey.getAlgorithm());
		bb.append("\n");
		bb.append(publicKey.toString());
		bb.append("\n");
		if (subjectKeyIdentifier != null) {
			bb.append("X509v3 Subject Key Identifier:\n");
			bb.append(GXCommon.toHex(subjectKeyIdentifier));
			bb.append("\n");
		}
		if (authorityKeyIdentifier != null) {
			bb.append("X509v3 Authority Key Identifier:\n");
			bb.append(GXCommon.toHex(authorityKeyIdentifier));
			bb.append("\n");
		}
		bb.append("Signature Algorithm: ");
		if (signatureAlgorithm != null) {
			bb.append(signatureAlgorithm.toString());
		}
		bb.append("\n");
		bb.append(GXCommon.toHex(signature));
		bb.append("\n");
		return bb.toString();
	}

	/**
	 * @return Key usage.
	 */
	public Set<KeyUsage> getKeyUsage() {
		return keyUsage;
	}

	/**
	 * @param value Key usage.
	 */
	public void setKeyUsage(final Set<KeyUsage> value) {
		keyUsage = value;
	}

	/**
	 * 
	 * @return Indicates that a certificate can be used as an TLS server or client
	 *         certificate.
	 */
	public Set<ExtendedKeyUsage> getExtendedKeyUsage() {
		return extendedKeyUsage;
	}

	/**
	 * 
	 * @param value Indicates that a certificate can be used as an TLS server or
	 *              client certificate.
	 */
	public void setExtendedKeyUsage(final Set<ExtendedKeyUsage> value) {
		extendedKeyUsage = value;
	}

	/**
	 * @return Identifies the public key being certified.
	 */
	public byte[] getSubjectKeyIdentifier() {
		return subjectKeyIdentifier;
	}

	/**
	 * @param value Identifies the public key being certified.
	 */
	public void setSubjectKeyIdentifier(final byte[] value) {
		subjectKeyIdentifier = value;
	}

	/**
	 * @return May be used either as a certificate or CRL extension.
	 */
	public byte[] getAuthorityKeyIdentifier() {
		return authorityKeyIdentifier;
	}

	/**
	 * @param value May be used either as a certificate or CRL extension.
	 */
	public void setAuthorityKeyIdentifier(final byte[] value) {
		this.authorityKeyIdentifier = value;
	}

	/**
	 * @return Indicates if the subject may act as a CA.
	 */
	public boolean isBasicConstraints() {
		return basicConstraints;
	}

	/**
	 * @param value Indicates if the subject may act as a CA.
	 */
	public void setBasicConstraints(final boolean value) {
		basicConstraints = value;
	}

	/**
	 * Load private key from the PEM file.
	 * 
	 * @param path File path.
	 * @return Created GXPkcs8 object.
	 * @throws IOException IO exception.
	 */
	public static GXx509Certificate load(final Path path) throws IOException {
		return GXx509Certificate.fromPem(Files.readString(path));
	}

	/**
	 * Save private key to PEM file.
	 * 
	 * @param path File path.
	 * @throws IOException IO exception.
	 */
	public void save(final Path path) throws IOException {
		Files.write(path, toPem().getBytes(), StandardOpenOption.CREATE);
	}

	/**
	 * @return Public key in PEM format.
	 */
	public String toPem() {
		StringBuilder sb = new StringBuilder();
		if (publicKey == null) {
			throw new IllegalArgumentException("Public or private key is not set.");
		}
		sb.append("-----BEGIN CERTIFICATE-----" + System.lineSeparator());
		sb.append(toDer());
		sb.append(System.lineSeparator() + "-----END CERTIFICATE-----" + System.lineSeparator());
		return sb.toString();
	}

	/**
	 * @return Public key in DER format.
	 */
	public String toDer() {
		return GXCommon.toBase64(getEncoded());
	}
}
