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
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import gurux.dlms.asn.enums.GXOid;
import gurux.dlms.asn.enums.HashAlgorithm;
import gurux.dlms.asn.enums.PkcsObjectIdentifier;
import gurux.dlms.asn.enums.X9ObjectIdentifier;
import gurux.dlms.internal.GXCommon;

/**
 * Pkcs10 certification request. https://tools.ietf.org/html/rfc2986
 */
public class GXPkcs10 {

    /**
     * Certificate version.
     */
    private CertificateVersion version;

    /**
     * Subject.
     */
    private String subject;

    private Object attributes;
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
        version = CertificateVersion.V1;
    }

    /**
     * Constructor.
     * 
     * @param data
     *            Base64 string.
     */
    public GXPkcs10(final String data) {
        String tmp = data.replace("-----BEGIN CERTIFICATE REQUEST-----", "");
        tmp = tmp.replace("-----END CERTIFICATE REQUEST-----", "");
        tmp = tmp.replace("-----BEGIN NEW CERTIFICATE REQUEST-----", "");
        tmp = tmp.replace("-----END NEW CERTIFICATE REQUEST-----", "");
        init(GXCommon.fromBase64(tmp.trim()));
    }

    /**
     * Constructor.
     * 
     * @param data
     *            Encoded bytes.
     */
    public GXPkcs10(final byte[] data) {
        init(data);
    }

    private void init(final byte[] data) {
        GXAsn1Sequence seq =
                (GXAsn1Sequence) GXAsn1Converter.fromByteArray(data);
        if (seq.size() < 3) {
            throw new IllegalArgumentException(
                    "Wrong number of elements in sequence.");
        }
        /////////////////////////////
        // CertificationRequestInfo ::= SEQUENCE {
        // version INTEGER { v1(0) } (v1,...),
        // subject Name,
        // subjectPKInfo SubjectPublicKeyInfo{{ PKInfoAlgorithms }},
        // attributes [0] Attributes{{ CRIAttributes }}
        // }

        GXAsn1Sequence reqInfo = (GXAsn1Sequence) seq.get(0);
        version = CertificateVersion
                .forValue(((Number) reqInfo.get(0)).intValue());
        subject = GXAsn1Converter.getSubject((GXAsn1Sequence) reqInfo.get(1));
        // subject Public key info.
        GXAsn1Sequence subjectPKInfo = (GXAsn1Sequence) reqInfo.get(2);
        if (reqInfo.size() > 3) {
            attributes = reqInfo.get(3);
        }
        GXAsn1Sequence tmp = (GXAsn1Sequence) subjectPKInfo.get(0);
        algorithm = PkcsObjectIdentifier.forValue(tmp.get(0).toString());
        if (algorithm == null) {
            algorithm = X9ObjectIdentifier.forValue(tmp.get(0).toString());
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
                throw new IllegalStateException(
                        "Unknown algorithm:" + algorithm.toString());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(algorithm.toString().substring(0, 2)
                    + "key factory not present in runtime");
        }
        try {
            byte[] encodedKey = GXAsn1Converter.toByteArray(subjectPKInfo);
            X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
            publicKey = eckf.generatePublic(ecpks);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }

        /////////////////////////////
        // signatureAlgorithm
        GXAsn1Sequence sign = (GXAsn1Sequence) seq.get(1);
        signatureAlgorithm = HashAlgorithm.forValue(sign.get(0).toString());
        if (sign.size() != 1) {
            signatureParameters = (String) sign.get(1);
        }
        /////////////////////////////
        // signature
        signature = ((GXAsn1BitString) seq.get(2)).getValue();
        if (!verify(GXAsn1Converter.toByteArray(reqInfo), signature)) {
            throw new RuntimeException("Invalid Signature.");
        }
    }

    /**
     * @return Certificate version.
     */
    public final CertificateVersion getVersion() {
        return version;
    }

    /**
     * @param value
     *            Certificate version.
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
     * @param value
     *            Subject.
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
     * @param value
     *            Subject public key info.
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
     * @param value
     *            Algorithm
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
     * @param value
     *            Signature algorithm.
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
     * @param value
     *            Signature parameters.
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
     * @param value
     *            Signature.
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
        bb.append(subject.toString());
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
                throw new RuntimeException(
                        "Invalid Signature: " + signatureAlgorithm.toString());
            }
            // instance = Signature.getInstance(signatureAlgorithm.toString());
            instance.initVerify(publicKey);
            instance.update(data);
            return instance.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Object[] getdata() {
        Object subjectPKInfo =
                GXAsn1Converter.fromByteArray(publicKey.getEncoded());
        Object[] list;
        if (attributes != null) {
            list = new Object[] { version.getValue(),
                    GXAsn1Converter.encodeSubject(subject), subjectPKInfo,
                    attributes };
        } else {
            list = new Object[] { version.getValue(),
                    GXAsn1Converter.encodeSubject(subject), subjectPKInfo,
                    new GXAsn1Context() };
        }
        return list;
    }

    public final byte[] getEncoded() {
        if (signature == null) {
            throw new RuntimeException("Sign first.");
        }
        // Certification request info.
        // subject Public key info.
        GXAsn1ObjectIdentifier sa =
                new GXAsn1ObjectIdentifier(signatureAlgorithm.getValue());
        Object[] list = new Object[] { getdata(), new Object[] { sa },
                new GXAsn1BitString(signature, 0) };
        return GXAsn1Converter.toByteArray(list);
    }

    /**
     * Sign
     * 
     * @param kp
     *            Public and Private key.
     * @param hashAlgorithm
     *            Used algorithm for signing.
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

    /**
     * Create Certificate Signing Request.
     * 
     * @param kp
     *            KeyPair
     * @param subject
     *            Subject.
     * @return Created GXPkcs10.
     */
    public static GXPkcs10 createCertificateSigningRequest(final KeyPair kp,
            final String subject) {
        GXPkcs10 pkc10 = new GXPkcs10();
        pkc10.setAlgorithm(X9ObjectIdentifier.IdECPublicKey);
        pkc10.setPublicKey(kp.getPublic());
        pkc10.setSubject(subject);
        pkc10.sign(kp, HashAlgorithm.SHA256withECDSA);
        return pkc10;
    }

    /**
     * Load public key from the PEM file.
     * 
     * @param path
     *            File path.
     * @return Created GXPkcs10 object.
     * @throws IOException
     *             IO exception.
     */
    public static GXPkcs10 load(final Path path) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new GXPkcs10(new String(encoded));
    }

    /**
     * Save public key to PEM file.
     * 
     * @param path
     *            File path.
     * @throws IOException
     *             IO exception.
     */
    public void save(final Path path) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(
                "-----BEGIN CERTIFICATE REQUEST-----" + System.lineSeparator());
        sb.append(GXCommon.toBase64(getEncoded()));
        sb.append(System.lineSeparator() + "-----END CERTIFICATE REQUEST-----");
        Files.write(path, sb.toString().getBytes(), StandardOpenOption.CREATE);
    }
}
