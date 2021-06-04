//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSCertificateException;
import gurux.dlms.asn.enums.GXOid;
import gurux.dlms.asn.enums.PkcsObjectIdentifier;
import gurux.dlms.asn.enums.PkcsType;
import gurux.dlms.asn.enums.X9ObjectIdentifier;
import gurux.dlms.internal.GXCommon;

/**
 * Pkcs8 certification request. Private key is saved using this format.
 * https://tools.ietf.org/html/rfc5208
 */
public class GXPkcs8 {

	/**
	 * Loaded PKCS #8 certificate request as a raw data.
	 */
	private byte[] rawData;

	/**
	 * Private key version.
	 */
	private CertificateVersion version;

	/**
	 * Algorithm.
	 */
	private GXOid algorithm;

	/**
	 * Private key.
	 */
	private PrivateKey privateKey;

	/**
	 * Public key.
	 */
	private PublicKey publicKey;

	/**
	 * Constructor.
	 */
	public GXPkcs8() {
		version = CertificateVersion.V1;
		algorithm = X9ObjectIdentifier.IdECPublicKey;
	}

	/**
	 * Constructor.
	 */
	public GXPkcs8(final KeyPair keys) {
		this();
		privateKey = keys.getPrivate();
		publicKey = keys.getPublic();
	}

	/**
	 * Constructor.
	 * 
	 * @param data Encoded bytes.
	 */
	public GXPkcs8(final byte[] data) {
		init(data);
	}

	/**
	 * Constructor.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public GXPkcs8(final PrivateKey priv) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this();
		privateKey = priv;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(priv.getEncoded());
		KeyFactory kf = KeyFactory.getInstance("EC");
		publicKey = kf.generatePublic(keySpec);
	}

	/**
	 * Constructor.
	 * 
	 * @param data Base64 string.
	 * @deprecated use {@link fromPem} instead.
	 */
	public GXPkcs8(final String data) {
		final String START = "PRIVATE KEY-----\n";
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
	 * Create PKCS #8 from PEM string.
	 * 
	 * @param data PEM string.
	 * @return PKCS #8 certificate.
	 */
	public static GXPkcs8 fromPem(final String data) {
		final String START = "PRIVATE KEY-----\n";
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
	 * Create PKCS #8 from DER Base64 encoded string.
	 * 
	 * @param data Base64 DER string.
	 * @return PKCS #8 certificate.
	 */
	public static GXPkcs8 fromDer(final String data) {
		GXPkcs8 cert = new GXPkcs8();
		cert.init(GXCommon.fromBase64(data));
		return cert;
	}

	private void init(final byte[] data) {
		rawData = data;
		GXAsn1Sequence seq = (GXAsn1Sequence) GXAsn1Converter.fromByteArray(data);
		if (seq.size() < 3) {
			throw new IllegalArgumentException("Wrong number of elements in sequence.");
		}
		if (!(seq.get(0) instanceof Byte)) {
			PkcsType type = GXAsn1Converter.getCertificateType(data, seq);
			switch (type) {
			case PKCS_10:
				throw new GXDLMSCertificateException(
						"Invalid Certificate. This is PKCS 10 certification requests, not PKCS 8.");
			case x509_CERTIFICATE:
				throw new GXDLMSCertificateException("Invalid Certificate. This is PKCS x509 certificate, not PKCS 8.");
			default:
				throw new GXDLMSCertificateException("Invalid Certificate Version.");
			}
		}
		version = CertificateVersion.forValue(((Number) seq.get(0)).intValue());
		List<?> tmp = (List<?>) seq.get(1);
		algorithm = X9ObjectIdentifier.forValue(tmp.get(0).toString());
		if (algorithm == null) {
			algorithm = PkcsObjectIdentifier.forValue(tmp.get(0).toString());
		}
		// Create private and public keys.
		KeyFactory eckf;
		try {
			String name = algorithm.toString().toLowerCase();
			if (name.contains("rsa")) {
				eckf = KeyFactory.getInstance("RSA");
			} else if (name.endsWith("ecdsa")) {
				eckf = KeyFactory.getInstance("EC");
			} else if (name.contains("ec")) {
				eckf = KeyFactory.getInstance("EC");
			} else {
				throw new IllegalStateException("Unknown algorithm:" + algorithm.toString());
			}
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					algorithm.toString().substring(0, 2) + "key factory not present in runtime");
		}
		try {
			PKCS8EncodedKeySpec ecpks = new PKCS8EncodedKeySpec(data, tmp.get(0).toString());
			privateKey = eckf.generatePrivate(ecpks);
			tmp = (List<?>) seq.get(2);
			privateKey = GXAsn1Converter.getPrivateKey((byte[]) tmp.get(1));
			tmp = (List<?>) tmp.get(2);
			GXByteBuffer tmp2 = new GXByteBuffer();
			tmp2.set(((GXAsn1BitString) tmp.get(0)).getValue());
			publicKey = GXAsn1Converter.getPublicKey(tmp2.subArray(1, tmp2.size() - 1));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @return Certificate version.
	 */
	public final CertificateVersion getVersion() {
		return version;
	}

	/**
	 * @param value Certificate version.
	 */
	public final void setVersion(final CertificateVersion value) {
		version = value;
	}

	/**
	 * @return Algorithm
	 */
	public final GXOid getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param value Algorithm
	 */
	public final void setAlgorithm(final GXOid value) {
		algorithm = value;
	}

	@Override
	public final String toString() {
		StringBuilder bb = new StringBuilder();
		bb.append("PKCS #8:");
		bb.append("\r\n");
		bb.append("Version: ");
		bb.append(version.toString());
		bb.append("\r\n");
		bb.append("Algorithm: ");
		if (algorithm != null) {
			bb.append(algorithm.toString());
		}
		bb.append("\r\n");
		return bb.toString();
	}

	/**
	 * @return Private key.
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * @return Public key.
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * Load private key from the PEM file.
	 * 
	 * @param path File path.
	 * @return Created GXPkcs8 object.
	 * @throws IOException IO exception.
	 */
	public static GXPkcs8 load(final Path path) throws IOException {
		return GXPkcs8.fromPem(Files.readString(path));
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
	 * @return Private key in PEM format.
	 */
	public String toPem() {
		StringBuilder sb = new StringBuilder();
		if (privateKey == null) {
			throw new IllegalArgumentException("Private key is not set.");
		}
		sb.append("-----BEGIN PRIVATE KEY-----");
		sb.append(System.lineSeparator());
		sb.append(toDer());
		sb.append(System.lineSeparator());
		sb.append("-----END PRIVATE KEY-----");
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	/**
	 * @return Private key in DER format.
	 */
	public String toDer() {
		if (rawData != null) {
			return GXCommon.toBase64(rawData);
		}
		GXAsn1Sequence d = new GXAsn1Sequence();
		d.add((byte) version.getValue());
		GXAsn1Sequence d1 = new GXAsn1Sequence();
		d1.add(new GXAsn1ObjectIdentifier(algorithm.getValue()));
		d1.add(new GXAsn1ObjectIdentifier("1.2.840.10045.3.1.7"));
		d.add(d1);
		GXAsn1Sequence d2 = new GXAsn1Sequence();
		d2.add((byte) 1);
		d2.add(GXAsn1Converter.rawValue(privateKey));
		GXAsn1Context d3 = new GXAsn1Context();
		d3.setIndex(1);
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(4);
		bb.set(GXAsn1Converter.rawValue(publicKey));
		d3.add(new GXAsn1BitString(bb.array(), 0));
		d2.add(d3);
		d.add(GXAsn1Converter.toByteArray(d2));
		return GXCommon.toBase64(GXAsn1Converter.toByteArray(d));

		// return GXCommon.toBase64(privateKey.getEncoded());
	}
}
