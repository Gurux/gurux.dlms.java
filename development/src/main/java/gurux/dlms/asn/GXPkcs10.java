//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

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

    /**
     * Algorithm.
     */
    private GXOid algorithm;

    /**
     * Parameters.
     */
    private Object parameters;

    /**
     * public key info.
     */
    private GXAsn1BitString publicKeyInfo;

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
        init(GXCommon.fromBase64(data));
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
        GXAsn1Sequence tmp = (GXAsn1Sequence) subjectPKInfo.get(0);
        algorithm = PkcsObjectIdentifier.forValue(tmp.get(0).toString());
        if (algorithm == null) {
            algorithm = X9ObjectIdentifier.forValue(tmp.get(0).toString());
        }
        parameters = tmp.get(1);
        publicKeyInfo = (GXAsn1BitString) subjectPKInfo.get(1);
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
        // signatureAlgorithm =
        // PkcsObjectIdentifier.forValue(sign.get(0).toString());
        // if (signatureAlgorithm == null) {
        // signatureAlgorithm =
        // X9ObjectIdentifier.forValue(tmp.get(0).toString());
        // }
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

    public final byte[] getEncoded() {
        if (signature == null) {
            throw new RuntimeException("Sign first.");
        }
        // Certification request info.
        // subject Public key info.
        GXAsn1ObjectIdentifier sa =
                new GXAsn1ObjectIdentifier(signatureAlgorithm.getValue());
        GXAsn1ObjectIdentifier a =
                new GXAsn1ObjectIdentifier(algorithm.getValue());
        Object[] list = new Object[] {
                new Object[] { version.getValue(),
                        GXAsn1Converter.encodeSubject(subject),
                        new Object[] { new Object[] { a, parameters, },
                                publicKeyInfo },
                        new GXAsn1Context() },
                new Object[] { sa, signatureParameters },
                new GXAsn1BitString(signature, 0) };
        return GXAsn1Converter.toByteArray(list);
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

        bb.append("Parameters: ");
        if (parameters != null) {
            bb.append(parameters.toString());
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

    private boolean verify(final byte[] data, final byte[] signature) {
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
            return instance.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
        GXAsn1Sequence seq = (GXAsn1Sequence) GXAsn1Converter
                .fromByteArray(kp.getPrivate().getEncoded());
        if (seq.size() != 3) {
            throw new IllegalArgumentException(
                    "Wrong number of elements in sequence.");
        }
        algorithm = hashAlgorithm;
        signatureAlgorithm = X9ObjectIdentifier
                .forValue(((GXAsn1Sequence) seq.get(1)).get(0).toString());
        if (signatureAlgorithm == null) {
            signatureAlgorithm = PkcsObjectIdentifier
                    .forValue(((GXAsn1Sequence) seq.get(1)).get(0).toString());
        }

        // Certification request info.
        // subject Public key info.
        publicKey = kp.getPublic();

        GXAsn1ObjectIdentifier sa =
                new GXAsn1ObjectIdentifier(signatureAlgorithm.getValue());
        GXAsn1ObjectIdentifier a =
                new GXAsn1ObjectIdentifier(algorithm.getValue());
        Object[] list = new Object[] { version.getValue(),
                GXAsn1Converter.encodeSubject(subject),
                new Object[] { new Object[] { a, parameters, }, publicKeyInfo },
                new GXAsn1Context() };
        byte[] data = GXAsn1Converter.toByteArray(list);

        // privateKey.getAlgorithm();
        // this.signatureAlgorithm =
        // Compute signature
        try {
            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initSign(kp.getPrivate());
            instance.update(data);
            signature = instance.sign();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
