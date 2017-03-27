//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.asn.enums.HashAlgorithm;
import gurux.dlms.asn.enums.KeyUsage;
import gurux.dlms.enums.BerType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.GXDLMSSecuritySetup;

/**
 * x509 Certificate.
 */
public class GXx509Certificate {

    // private static final String OLD_AUTHORITY_KEY_IDENTIFIER = "2.5.29.1";
    // private static final String OLD_PRIMARY_KEY_ATTRIBUTES = "2.5.29.2";
    // private static final String CERTIFICATE_POLICIES = "2.5.29.3";
    // private static final String PRIMARY_KEY_USAGE_RESTRICTION = "2.5.29.4";
    // private static final String SUBJECT_DIRECTORY_ATTRIBUTES = "2.5.29.9";
    private static final String SUBJECT_KEY_IDENTIFIER = "2.5.29.14";
    private static final String KEY_USAGE = "2.5.29.15";
    // private static final String PRIVATE_KEY_USAGE_PERIOD = "2.5.29.16";
    // private static final String SUBJECT_ALTERNATIVE_NAME = "2.5.29.17";
    // private static final String ISSUER_ALTERNATIVE_NAME = "2.5.29.18";
    private static final String BASIC_CONSTRAINTS = "2.5.29.19";
    // private static final String CRL_NUMBER = "2.5.29.20";
    // private static final String REASON_CODE = "2.5.29.21";
    // private static final String HOLD_INSTRUCTION_CODE = "2.5.29.23";
    // private static final String INVALIDITY_DATE = "2.5.29.24";
    // private static final String DELTA_CRL_INDICATOR = "2.5.29.27";
    // private static final String ISSUING_DISTRIBUTION_POINT = "2.5.29.28";
    // private static final String CERTIFICATE_ISSUER = "2.5.29.29";
    // private static final String NAME_CONSTRAINTS = "2.5.29.30";
    // private static final String CRL_DISTRIBUTION_POINTS = "2.5.29.31";
    // private static final String CERTIFICATE_POLICIES_2 = "2.5.29.32";
    // private static final String POLICY_MAPPINGS = "2.5.29.33";
    private static final String AUTHORITY_KEY_IDENTIFIER = "2.5.29.35";
    // private static final String POLICY_CONSTRAINTS = "2.5.29.36";
    // private static final String EXTENDED_KEY_USAGE = "2.5.29.37";
    // private static final String FRESHEST_CRL = "2.5.29.46";

    /**
     * This extension identifies the public key being certified.
     */
    private byte[] subjectKeyIdentifier;
    /**
     * May be used either as a certificate or CRL extension. It identifies the
     * public key to be used to verify the signature on this certificate or CRL.
     * It enables distinct keys used by the same CA to be distinguished.
     */
    private byte[] authorityKeyIdentifier;

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
    private HashAlgorithm algorithm;

    /**
     * Parameters.
     */
    private Object parameters;

    /**
     * Signature.
     */
    private byte[] signature;

    /**
     * Subject. Example: "CN=Test O=Gurux, L=Tampere, C=FI".
     */
    private String subject;

    /**
     * Issuer. Example: "CN=Test O=Gurux, L=Tampere, C=FI".
     */
    private String issuer;

    /**
     * Serial number.
     */
    private GXAsn1Integer serialNumber;

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
     * Constructor.
     */
    public GXx509Certificate() {
        version = CertificateVersion.V3;
    }

    /**
     * Constructor.
     * 
     * @param data
     *            Base64 string.
     */
    public GXx509Certificate(final String data) {
        String tmp = data.replace("-----BEGIN CERTIFICATE-----", "");
        tmp = tmp.replace("-----END CERTIFICATE-----", "");
        init(GXCommon.fromBase64(tmp.trim()));
    }

    static String getAlgorithm(final String algorithm) {
        if (algorithm.toString().endsWith("RSA")) {
            return "RSA";
        } else if (algorithm.toString().endsWith("ECDSA")) {
            return "EC";
        } else {
            throw new IllegalStateException("Unknown algorithm:" + algorithm);
        }
    }

    /*
     * https://tools.ietf.org/html/rfc5280#section-4.1
     */
    private void init(final byte[] data) {
        GXAsn1Sequence seq =
                (GXAsn1Sequence) GXAsn1Converter.fromByteArray(data);
        if (seq.size() != 3) {
            throw new IllegalArgumentException(
                    "Wrong number of elements in sequence.");
        }
        GXAsn1Sequence reqInfo = (GXAsn1Sequence) seq.get(0);
        version = CertificateVersion.forValue(
                ((Number) ((GXAsn1Context) reqInfo.get(0)).get(0)).byteValue());
        serialNumber = (GXAsn1Integer) reqInfo.get(1);
        String tmp = ((GXAsn1Sequence) reqInfo.get(2)).get(0).toString();
        // Signature Algorithm
        algorithm = HashAlgorithm.forValue(tmp);
        parameters = ((GXAsn1Sequence) reqInfo.get(2)).get(1);
        // Issuer
        issuer = GXAsn1Converter.getSubject((GXAsn1Sequence) reqInfo.get(3));
        // Validity
        validFrom = (Date) ((GXAsn1Sequence) reqInfo.get(4)).get(0);
        validTo = (Date) ((GXAsn1Sequence) reqInfo.get(4)).get(1);
        subject = GXAsn1Converter.getSubject((GXAsn1Sequence) reqInfo.get(5));
        // subject public key Info
        GXAsn1Sequence subjectPKInfo = (GXAsn1Sequence) reqInfo.get(6);
        // Get Standard Extensions.
        if (reqInfo.size() > 7) {
            for (Object it : (GXAsn1Sequence) ((GXAsn1Context) reqInfo.get(7))
                    .get(0)) {
                GXAsn1Sequence s = (GXAsn1Sequence) ((GXAsn1Sequence) it);
                GXAsn1ObjectIdentifier id = (GXAsn1ObjectIdentifier) s.get(0);
                Object value = GXAsn1Converter.fromByteArray((byte[]) s.get(1));
                if (SUBJECT_KEY_IDENTIFIER.equals(id.toString())) {
                    subjectKeyIdentifier = (byte[]) value;
                } else if (AUTHORITY_KEY_IDENTIFIER.equals(id.toString())) {
                    authorityKeyIdentifier =
                            (byte[]) ((GXAsn1Sequence) value).get(0);
                } else if (BASIC_CONSTRAINTS.equals(id.toString())) {
                    if (((GXAsn1Sequence) value).size() != 0) {
                        basicConstraints =
                                (boolean) ((GXAsn1Sequence) value).get(0);
                    }
                } else if (KEY_USAGE.equals(id.toString())) {
                    keyUsage = KeyUsage.forValue(
                            ((GXAsn1BitString) value).getValue()[0] & 0xFF);
                }
            }
        }

        // Make public key.
        KeyFactory eckf;
        try {
            eckf = KeyFactory.getInstance(getAlgorithm(algorithm.toString()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(algorithm.name().substring(0, 2)
                    + "key factory not present in runtime");
        }
        try {
            byte[] encodedKey = GXAsn1Converter.toByteArray(subjectPKInfo);
            X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
            publicKey = eckf.generatePublic(ecpks);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }
        signatureAlgorithm = HashAlgorithm
                .forValue(((GXAsn1Sequence) seq.get(1)).get(0).toString());
        signatureParameters = ((GXAsn1Sequence) seq.get(1)).get(1);
        // signature
        signature = ((GXAsn1BitString) seq.get(2)).getValue();
    }

    /**
     * Constructor.
     * 
     * @param data
     *            Encoded bytes.
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
     * @param value
     *            Subject.
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
     * @param value
     *            Issuer.
     */
    public final void setIssuer(final String value) {
        issuer = value;
    }

    /**
     * @return Serial number.
     */
    public final GXAsn1Integer getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param value
     *            Serial number.
     */
    public final void setSerialNumber(final GXAsn1Integer value) {
        serialNumber = value;
    }

    /**
     * @return Version number.
     */
    public final CertificateVersion getVersion() {
        return version;
    }

    /**
     * @param value
     *            Version number.
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
     * @param value
     *            Validity from.
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
     * @param value
     *            Validity to.
     */
    public final void setValidTo(final Date value) {
        validTo = value;
    }

    /**
     * @return Algorithm
     */
    public final HashAlgorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * @param value
     *            Algorithm.
     */
    public final void setAlgorithm(final HashAlgorithm value) {
        algorithm = value;
    }

    /**
     * @return Parameters.
     */
    public final Object getParameters() {
        return parameters;
    }

    /**
     * @param value
     *            Parameters.
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
     * @param value
     *            Public key.
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
     * @param value
     *            Signature.
     */
    public final void setSignature(final byte[] value) {
        signature = value;
    }

    private Object[] getdata() {
        GXAsn1ObjectIdentifier a =
                new GXAsn1ObjectIdentifier(algorithm.getValue());
        GXAsn1Context p = new GXAsn1Context();
        p.add((byte) version.getValue());
        Object subjectPKInfo =
                GXAsn1Converter.fromByteArray(publicKey.getEncoded());
        GXAsn1Sequence s = new GXAsn1Sequence();
        if (subjectKeyIdentifier != null) {
            GXAsn1Sequence s1 = new GXAsn1Sequence();
            s1.add(new GXAsn1ObjectIdentifier(SUBJECT_KEY_IDENTIFIER));
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(BerType.OCTET_STRING);
            GXCommon.setObjectCount(subjectKeyIdentifier.length, bb);
            bb.set(subjectKeyIdentifier);
            s1.add(bb.array());
            s.add(s1);
        }
        if (authorityKeyIdentifier != null) {
            GXAsn1Sequence s1 = new GXAsn1Sequence();
            s1.add(new GXAsn1ObjectIdentifier(AUTHORITY_KEY_IDENTIFIER));
            GXAsn1Sequence seq = new GXAsn1Sequence();
            seq.add(authorityKeyIdentifier);
            s1.add(GXAsn1Converter.toByteArray(seq));
            s.add(s1);
        }
        if (basicConstraints) {
            GXAsn1Sequence s1 = new GXAsn1Sequence();
            s1.add(new GXAsn1ObjectIdentifier(BASIC_CONSTRAINTS));
            GXAsn1Sequence seq = new GXAsn1Sequence();
            seq.add(basicConstraints);
            s1.add(GXAsn1Converter.toByteArray(seq));
            s.add(s1);
        }
        if (keyUsage != null && !keyUsage.isEmpty()) {
            GXAsn1Sequence s1 = new GXAsn1Sequence();
            s1.add(new GXAsn1ObjectIdentifier(KEY_USAGE));
            int value = 0;
            int min = 128;
            for (KeyUsage it : keyUsage) {
                int val = it.getValue();
                value |= val;
                if (val < min) {
                    min = val;
                }
            }
            int offset = 7;
            while ((min >>= 2) != 0) {
                --offset;
            }
            byte[] tmp = GXAsn1Converter.toByteArray(new GXAsn1BitString(
                    new byte[] { (byte) offset, (byte) value }));
            s1.add(tmp);
            s.add(s1);
        }
        GXAsn1Sequence valid = new GXAsn1Sequence();
        valid.add(validFrom);
        valid.add(validTo);
        Object[] list;
        if (s.isEmpty()) {
            list = new Object[] { p, serialNumber,
                    new Object[] { a, parameters },
                    GXAsn1Converter.encodeSubject(issuer), valid,
                    GXAsn1Converter.encodeSubject(subject), subjectPKInfo };
        } else {
            GXAsn1Context tmp = new GXAsn1Context();
            tmp.setIndex(3);
            tmp.add(s);
            list = new Object[] { p, serialNumber,
                    new Object[] { a, parameters },
                    GXAsn1Converter.encodeSubject(issuer), valid,
                    GXAsn1Converter.encodeSubject(subject), subjectPKInfo,
                    tmp };
        }
        return list;
    }

    public final byte[] getEncoded() {
        Object tmp = new Object[] {
                new GXAsn1ObjectIdentifier(signatureAlgorithm.getValue()),
                signatureParameters };
        Object[] list = new Object[] { getdata(), tmp,
                new GXAsn1BitString(signature, 0) };
        return GXAsn1Converter.toByteArray(list);
    }

    /**
     * Sign certificate.
     * 
     * @param kp
     *            Public and Private key.
     * @param hashAlgorithm
     *            Used signature algorithm.
     */
    public void sign(final KeyPair kp, final HashAlgorithm hashAlgorithm) {
        byte[] data = GXAsn1Converter.toByteArray(getdata());
        try {
            Signature instance =
                    Signature.getInstance(hashAlgorithm.toString());
            instance.initSign(kp.getPrivate());
            instance.update(data);
            signatureAlgorithm = hashAlgorithm;
            signature = instance.sign();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public final String toString() {
        StringBuilder bb = new StringBuilder();
        bb.append("Version: ");
        bb.append(version.toString());
        bb.append("\r\n");
        bb.append("Subject: ");
        bb.append(subject);
        bb.append("\r\n");

        bb.append("Signature Algorithm: ");
        if (algorithm != null) {
            bb.append(algorithm.toString());
            bb.append(", OID = ");
            bb.append(algorithm.getValue());
        }
        bb.append("\r\n");

        bb.append("Key: ");
        if (publicKey != null) {
            bb.append(publicKey.toString());
        }
        bb.append("\r\n");
        bb.append("Validity: [From: ");
        bb.append(validFrom.toString());
        bb.append(", \r\n");
        bb.append("To: ");
        bb.append(validTo.toString());
        bb.append("]\r\n");

        bb.append("Issuer: ");
        bb.append(issuer);
        bb.append("\r\n");
        bb.append("SerialNumber: ");
        bb.append(serialNumber);
        bb.append("\r\n");
        bb.append("Algorithm: ");
        if (signatureAlgorithm != null) {
            bb.append(signatureAlgorithm.toString());
        }
        bb.append("\r\n");

        bb.append("Signature: ");
        bb.append(GXCommon.toHex(signature));
        bb.append("\r\n");
        return bb.toString();
    }

    /**
     * Create self signed x509Certificate.
     * 
     * @param keyPair
     *            KeyPair
     * @param from
     *            Valid from.
     * @param to
     *            Valid to.
     * @param subject
     *            Subject.
     * @param issuer
     *            Issuer
     * @param keyUsage
     *            Key usage.
     * @return Created x509Certificate.
     */
    public static GXx509Certificate createSelfSignedCertificate(
            final KeyPair keyPair, final Date from, final Date to,
            final String subject, final String issuer,
            final Set<KeyUsage> keyUsage) {
        GXx509Certificate cert = new GXx509Certificate();
        // Set Serial number and version
        cert.setSerialNumber(new GXAsn1Integer(
                new BigInteger(64, new SecureRandom()).toByteArray()));
        cert.setVersion(gurux.dlms.asn.CertificateVersion.V3);
        cert.setAlgorithm(HashAlgorithm.SHA256withECDSA);
        // Set Validity
        cert.setValidFrom(from);
        cert.setKeyUsage(keyUsage);
        cert.setValidTo(to);
        cert.setSubject(subject);
        cert.setIssuer(issuer);
        // Set public key and algorithm.
        cert.setPublicKey(keyPair.getPublic());
        // Create a new certificate and sign it.
        cert.sign(keyPair, HashAlgorithm.SHA256withECDSA);
        return cert;
    }

    /**
     * Create self signed x509Certificate.
     * 
     * @param keyPair
     *            KeyPair
     * @param from
     *            Valid from.
     * @param to
     *            Valid to.
     * @param systemTitle
     *            System title.
     * @param issuer
     *            Issuer
     * @param keyUsage
     *            Key usage.
     * @return Created x509Certificate.
     */
    public static GXx509Certificate createSelfSignedCertificate(
            final KeyPair keyPair, final Date from, final Date to,
            final byte[] systemTitle, final String issuer,
            final Set<KeyUsage> keyUsage) {

        return createSelfSignedCertificate(keyPair, from, to,
                GXDLMSSecuritySetup.systemTitleToSubject(systemTitle), issuer,
                keyUsage);
    }

    /**
     * @return Key usage.
     */
    public Set<KeyUsage> getKeyUsage() {
        return keyUsage;
    }

    /**
     * @param value
     *            Key usage.
     */
    public void setKeyUsage(final Set<KeyUsage> value) {
        keyUsage = value;
    }

    /**
     * @return Identifies the public key being certified.
     */
    public byte[] getSubjectKeyIdentifier() {
        return subjectKeyIdentifier;
    }

    /**
     * @param value
     *            Identifies the public key being certified.
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
     * @param value
     *            May be used either as a certificate or CRL extension.
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
     * @param value
     *            Indicates if the subject may act as a CA.
     */
    public void setBasicConstraints(final boolean value) {
        basicConstraints = value;
    }

    /**
     * Load private key from the PEM file.
     * 
     * @param path
     *            File path.
     * @return Created GXPkcs8 object.
     * @throws IOException
     *             IO exception.
     */
    public static GXx509Certificate load(final Path path) throws IOException {
        String tmp = new String(Files.readAllBytes(path));
        return new GXx509Certificate(tmp);
    }

    /**
     * Save private key to PEM file.
     * 
     * @param path
     *            File path.
     * @throws IOException
     *             IO exception.
     */
    public void save(final Path path) throws IOException {
        StringBuffer sb = new StringBuffer();
        if (publicKey != null) {
            sb.append("-----BEGIN CERTIFICATE-----" + System.lineSeparator());
            sb.append(GXCommon.toBase64(getEncoded()));
            sb.append(System.lineSeparator() + "-----END CERTIFICATE-----");
        } else {
            throw new IllegalArgumentException(
                    "Public or private key is not set.");
        }
        Files.write(path, sb.toString().getBytes(), StandardOpenOption.CREATE);
    }
}
