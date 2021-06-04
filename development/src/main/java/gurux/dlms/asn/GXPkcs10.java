//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSCertificateException;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.asn.enums.ExtendedKeyUsage;
import gurux.dlms.asn.enums.GXOid;
import gurux.dlms.asn.enums.HashAlgorithm;
import gurux.dlms.asn.enums.KeyUsage;
import gurux.dlms.asn.enums.PkcsObjectIdentifier;
import gurux.dlms.asn.enums.PkcsType;
import gurux.dlms.asn.enums.X9ObjectIdentifier;
import gurux.dlms.internal.GXCommon;

/**
 * Pkcs10 certification request. https://tools.ietf.org/html/rfc2986
 */
public class GXPkcs10 {

	/**
	 * Loaded PKCS #10 certificate as a raw data.
	 */
	private byte[] rawData;
	/**
	 * Certificate version.
	 */
	private CertificateVersion version;

	/**
	 * Subject.
	 */
	private String subject;

	/**
	 * Collection of attributes providing additional information about the subject
	 * of the certificate.
	 */
	private List<Map.Entry<PkcsObjectIdentifier, Object[]>> attributes;
	/**
	 * Algorithm.
	 */
	private GXOid algorithm = X9ObjectIdentifier.IdECPublicKey;

	/**
	 * Subject public key.
	 */
	private PublicKey publicKey;

	/**
	 * Signature algorithm.
	 */
	private GXOid signatureAlgorithm;

	/**
	 * Signature parameters.
	 */
	private Object signatureParameters;

	/**
	 * Signature.
	 */
	private byte[] signature;

	/**
	 * Constructor.
	 */
	public GXPkcs10() {
		algorithm = X9ObjectIdentifier.IdECPublicKey;
		version = CertificateVersion.V1;
		attributes = new ArrayList<Map.Entry<PkcsObjectIdentifier, Object[]>>();
	}

	/**
	 * Constructor.
	 * 
	 * @param data Base64 string.
	 * @deprecated use {@link fromPem} instead.
	 */
	public GXPkcs10(final String data) {
		final String START = "CERTIFICATE REQUEST-----\n";
		final String END = "-----END CERTIFICATE REQUEST";
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
	 * Constructor.
	 * 
	 * @param data Encoded bytes.
	 */
	public GXPkcs10(final byte[] data) {
		init(data);
	}

	/**
	 * Create x509Certificate from PEM string.
	 * 
	 * @param data PEM string.
	 * @return x509 certificate.
	 */
	public static GXPkcs10 fromPem(final String data) {
		final String START = "CERTIFICATE REQUEST-----\n";
		final String END = "-----END CERTIFICATE REQUEST";
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
	public static GXPkcs10 fromDer(final String data) {
		GXPkcs10 cert = new GXPkcs10();
		cert.init(GXCommon.fromBase64(data));
		return cert;
	}

	private void init(final byte[] data) {
		rawData = data;
		attributes = new ArrayList<Map.Entry<PkcsObjectIdentifier, Object[]>>();
		GXAsn1Sequence seq = (GXAsn1Sequence) GXAsn1Converter.fromByteArray(data);
		if (seq.size() < 3) {
			throw new IllegalArgumentException("Wrong number of elements in sequence.");
		}
		if (!(seq.get(0) instanceof GXAsn1Sequence)) {
			PkcsType type = GXAsn1Converter.getCertificateType(data, seq);
			switch (type) {
			case PKCS_8:
				throw new GXDLMSCertificateException("Invalid Certificate. This is PKCS 8, not PKCS 10.");
			case x509_CERTIFICATE:
				throw new GXDLMSCertificateException(
						"Invalid Certificate. This is PKCS x509 certificate, not PKCS 10.");
			default:
				throw new GXDLMSCertificateException("Invalid Certificate Version.");
			}
		}
		GXAsn1Sequence reqInfo = (GXAsn1Sequence) seq.get(0);
		version = CertificateVersion.forValue(((Number) reqInfo.get(0)).intValue());
		subject = GXAsn1Converter.getSubject((GXAsn1Sequence) reqInfo.get(1));
		// subject Public key info.
		GXAsn1Sequence subjectPKInfo = (GXAsn1Sequence) reqInfo.get(2);
		if (reqInfo.size() > 3) {
			// PkcsObjectIdentifier
			for (Object t : (GXAsn1Context) reqInfo.get(3)) {
				GXAsn1Sequence it = (GXAsn1Sequence) t;
				List<Object> values = new ArrayList<Object>();
				for (Object v : (List<?>) ((Map.Entry<?, ?>) it.get(1)).getKey()) {
					values.add(v);
				}
				attributes.add(new GXSimpleEntry<PkcsObjectIdentifier, Object[]>(
						PkcsObjectIdentifier.forValue(it.get(0).toString()), values.toArray()));
			}
		}
		GXAsn1Sequence tmp = (GXAsn1Sequence) subjectPKInfo.get(0);
		algorithm = X9ObjectIdentifier.forValue(tmp.get(0).toString());
		if (algorithm != X9ObjectIdentifier.IdECPublicKey) {
			Object a = algorithm;
			if (a == null) {
				a = PkcsObjectIdentifier.forValue(tmp.get(0).toString());
				if (a == null) {
					a = tmp.get(0).toString();
				}
			}
			throw new IllegalArgumentException("Invalid PKCS #10 certificate algorithm. " + a);
		}
		// Make public key.
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
			byte[] encodedKey = GXAsn1Converter.toByteArray(subjectPKInfo);
			X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
			publicKey = eckf.generatePublic(ecpks);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		/////////////////////////////
		// signatureAlgorithm
		GXAsn1Sequence sign = (GXAsn1Sequence) seq.get(1);
		signatureAlgorithm = HashAlgorithm.forValue(sign.get(0).toString());
		if (sign.size() != 1) {
			signatureParameters = sign.get(1);
		}
		/////////////////////////////
		// signature
		signature = ((GXAsn1BitString) seq.get(2)).getValue();

		GXByteBuffer tmp2 = new GXByteBuffer();
		tmp2.set(data);
		GXAsn1Converter.getNext(tmp2);
		tmp2.size(tmp2.position());
		tmp2.position(1);
		GXCommon.getObjectCount(tmp2);
		if (!verify(tmp2.subArray(tmp2.position(), tmp2.available()), signature)) {
			throw new IllegalArgumentException("Invalid Signature.");
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
	 * @return Subject public key info.
	 */
	public final PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * @param value Subject public key info.
	 */
	public final void setPublicKey(final PublicKey value) {
		publicKey = value;
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

	/**
	 * @return Signature algorithm.
	 */
	public final GXOid getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	/**
	 * @param value Signature algorithm.
	 */
	public final void setSignatureAlgorithm(final GXOid value) {
		signatureAlgorithm = value;
	}

	/**
	 * @return Signature parameters.
	 */
	public final Object getSignatureParameters() {
		return signatureParameters;
	}

	/**
	 * @param value Signature parameters.
	 */
	public final void setSignatureParameters(final Object value) {
		signatureParameters = value;
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

	@Override
	public final String toString() {
		StringBuilder bb = new StringBuilder();
		bb.append("PKCS #10 certificate request:");
		bb.append("\r\n");
		bb.append("Version: ");
		bb.append(version.toString());
		bb.append("\r\n");

		bb.append("Subject: ");
		bb.append(subject);
		bb.append("\r\n");

		bb.append("Algorithm: ");
		if (algorithm != null) {
			bb.append(algorithm.toString());
		}
		bb.append("\r\n");
		bb.append("Public Key: ");
		if (publicKey != null) {
			bb.append(publicKey.toString());
		}
		bb.append("\r\n");
		bb.append("Signature algorithm: ");
		if (signatureAlgorithm != null) {
			bb.append(signatureAlgorithm.toString());
		}
		bb.append("\r\n");
		bb.append("Signature parameters: ");
		if (signatureParameters != null) {
			bb.append(signatureParameters.toString());
		}
		bb.append("\r\n");
		bb.append("Signature: ");
		bb.append(GXCommon.toHex(signature));
		bb.append("\r\n");
		return bb.toString();
	}

	private boolean verify(final byte[] data, final byte[] sign) {
		try {
			Signature instance;
			if (signatureAlgorithm == HashAlgorithm.SHA256withECDSA) {
				instance = Signature.getInstance("SHA256withECDSA");
			} else if (signatureAlgorithm == HashAlgorithm.SHA_256_RSA) {
				instance = Signature.getInstance("SHA256withRSA");
			} else {
				throw new IllegalArgumentException("Invalid Signature: " + signatureAlgorithm.toString());
			}
			instance.initVerify(publicKey);
			instance.update(data);
			return instance.verify(sign);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private Object[] getData() {
		GXByteBuffer bb = new GXByteBuffer();
		bb.setUInt8(4);
		bb.set(GXAsn1Converter.rawValue(publicKey));
		Object subjectPKInfo = new GXAsn1BitString(bb.array(), 0);
		Object[] tmp = new Object[] { new GXAsn1ObjectIdentifier("1.2.840.10045.2.1"),
				new GXAsn1ObjectIdentifier("1.2.840.10045.3.1.7") };
		GXAsn1Context list = new GXAsn1Context();
		for (Map.Entry<PkcsObjectIdentifier, Object[]> it : attributes) {
			GXAsn1Sequence s = new GXAsn1Sequence();
			s.add(new GXAsn1ObjectIdentifier(it.getKey().getValue()));
			// Convert object array to list.
			List<Object> values = new ArrayList<Object>();
			for (Object v : it.getValue()) {
				values.add(v);
			}
			s.add(new GXSimpleEntry<Object, Object>(values, null));
			list.add(s);
		}
		return new Object[] { version.getValue(), GXAsn1Converter.encodeSubject(subject),
				new Object[] { tmp, subjectPKInfo }, list };
	}

	/**
	 * @return PKCS #10 certificate as a byte array.
	 */
	public final byte[] getEncoded() {
		if (rawData != null) {
			return rawData;
		}
		if (signature == null) {
			throw new IllegalArgumentException("Sign first.");
		}
		// Certification request info.
		// subject Public key info.
		GXAsn1ObjectIdentifier sa = new GXAsn1ObjectIdentifier(signatureAlgorithm.getValue());
		Object[] list = new Object[] { getData(), new Object[] { sa }, new GXAsn1BitString(signature, 0) };
		return GXAsn1Converter.toByteArray(list);
	}

	/**
	 * Sign
	 * 
	 * @param kp            Public and Private key.
	 * @param hashAlgorithm Used algorithm for signing.
	 */
	@SuppressWarnings("squid:S00112")
	public void sign(final KeyPair kp, final HashAlgorithm hashAlgorithm) {
		byte[] data = GXAsn1Converter.toByteArray(getData());
		try {
			Signature instance = Signature.getInstance(hashAlgorithm.toString());
			instance.initSign(kp.getPrivate());
			instance.update(data);
			signatureAlgorithm = hashAlgorithm;
			signature = instance.sign();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Create Certificate Signing Request.
	 * 
	 * @param kp      KeyPair
	 * @param subject Subject.
	 * @return Created GXPkcs10.
	 */
	public static GXPkcs10 createCertificateSigningRequest(final KeyPair kp, final String subject) {
		GXPkcs10 pkc10 = new GXPkcs10();
		pkc10.setAlgorithm(X9ObjectIdentifier.IdECPublicKey);
		pkc10.setPublicKey(kp.getPublic());
		pkc10.setSubject(subject);
		pkc10.sign(kp, HashAlgorithm.SHA256withECDSA);
		return pkc10;
	}

	/**
	 * Ask Gurux certificate server to generate the new certificate.
	 * 
	 * @param address Certificate server address.
	 * @param cert    PKCS #10 certificate.
	 * @param usage   Certificate usage.
	 * @return Generated certificate.
	 * @throws IOException
	 */
	public static GXx509Certificate getCertificate(final String address, final GXPkcs10 cert, final KeyUsage usage)
			throws IOException {
		String der = "{\"KeyUsage\":" + usage.getValue() + ",\"CSR\":[\"" + cert.toDer() + "\"]}";
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			try {
				os.write(der.getBytes());
				os.flush();
			} finally {
				os.close();
			}
			int ret = connection.getResponseCode();
			if (ret == HttpURLConnection.HTTP_CREATED || ret == HttpURLConnection.HTTP_OK) {
				String str;
				StringBuilder data = new StringBuilder();
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				try {
					while ((str = in.readLine()) != null) {
						data.append(str);
					}
				} finally {
					in.close();
				}
				str = data.toString();
				int pos = str.indexOf("[");
				if (pos == -1) {
					throw new IllegalArgumentException("Certificates are missing.");
				}
				str = str.substring(pos + 2);
				pos = str.indexOf("]");
				if (pos == -1) {
					throw new IllegalArgumentException("Certificates are missing.");
				}
				str = str.substring(0, pos - 1);
				GXx509Certificate x509 = GXx509Certificate.fromDer(str);
				if (!cert.getPublicKey().equals(x509.getPublicKey())) {
					throw new IllegalArgumentException("Create certificate signingRequest generated wrong public key.");
				}
				return x509;
			}
			return null;
		} finally {
			connection.disconnect();
		}
	}

	/**
	 * Ask Gurux certificate server to generate the new certificate.
	 * 
	 * @param address        Certificate server address.
	 * @param certifications List of certification requests.
	 * @return Generated certificate(s).
	 * @throws IOException
	 */
	public static GXx509Certificate[] getCertificate(final String address,
			final List<GXCertificateRequest> certifications) throws IOException {
		StringBuilder usage = new StringBuilder();
		for (GXCertificateRequest it : certifications) {
			if (usage.length() != 0) {
				usage.append(", ");
			}
			usage.append("{\"KeyUsage\":");
			switch (it.getCertificateType()) {
			case DIGITAL_SIGNATURE:
				usage.append(String.valueOf(KeyUsage.DIGITAL_SIGNATURE.getValue()));
				break;
			case KEY_AGREEMENT:
				usage.append(String.valueOf(KeyUsage.KEY_AGREEMENT.getValue()));
				break;
			case TLS:
				usage.append(String.valueOf(KeyUsage.DIGITAL_SIGNATURE.getValue() | KeyUsage.KEY_AGREEMENT.getValue()));
				break;
			default:
				throw new RuntimeException("Invalid type.");
			}
			if (!it.getExtendedKeyUsage().isEmpty()) {
				usage.append(", \"ExtendedKeyUsage\":");
				usage.append(String.valueOf(ExtendedKeyUsage.toInteger(it.getExtendedKeyUsage())));
			}
			usage.append(", \"CSR\":\"");
			usage.append(it.getCertificate().toDer());
			usage.append("\"}");
		}
		String input = "{\"Certificates\":[" + usage.toString() + "]}";
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		try {
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED
					&& conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			StringBuilder sb = new StringBuilder();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			String str = sb.toString();
			int pos = str.indexOf("[");
			if (pos == -1) {
				throw new RuntimeException("Certificates are missing.");
			}
			str = str.substring(pos + 2);
			pos = str.indexOf("]");
			if (pos == -1) {
				throw new RuntimeException("Certificates are missing.");
			}
			str = str.substring(0, pos - 1);
			java.util.ArrayList<GXx509Certificate> list = new java.util.ArrayList<GXx509Certificate>();
			String[] tmp = str.split("['\"]");
			for (String it : tmp) {
				if (it.compareTo(",") != 0) {
					GXx509Certificate x509 = GXx509Certificate.fromDer(it);
					list.add(x509);
				}
			}
			return list.toArray(new GXx509Certificate[0]);
		} finally {
			conn.disconnect();
		}
	}

	/**
	 * Load Certificate Signing Request from the PEM file.
	 * 
	 * @param path File path.
	 * @return Created GXPkcs10 object.
	 * @throws IOException IO exception.
	 */
	public static GXPkcs10 load(final Path path) throws IOException {
		return GXPkcs10.fromPem(Files.readString(path));
	}

	/**
	 * Save public key to PEM file.
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
		sb.append("-----BEGIN CERTIFICATE REQUEST-----");
		sb.append(System.lineSeparator());
		sb.append(toDer());
		sb.append(System.lineSeparator());
		sb.append("-----END CERTIFICATE REQUEST-----");
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	/**
	 * @return Public key in DER format.
	 */
	public String toDer() {
		return GXCommon.toBase64(getEncoded());
	}

}
