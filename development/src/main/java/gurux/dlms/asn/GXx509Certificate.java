//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import gurux.dlms.asn.enums.GXOid;
import gurux.dlms.asn.enums.HashAlgorithm;
import gurux.dlms.asn.enums.PkcsObjectIdentifier;
import gurux.dlms.asn.enums.X9ObjectIdentifier;
import gurux.dlms.internal.GXCommon;

/**
 * x509 Certificate.
 */
public class GXx509Certificate {
    private GXAsn1Sequence subjectPKInfo;
    /**
     * Subject algorithm.
     */
    private GXOid subjectAlgorithm;
    private Object subjectParameters;

    private Object pk;

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
        init(GXCommon.fromBase64(data));
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
        subjectPKInfo = (GXAsn1Sequence) reqInfo.get(6);
        tmp = ((GXAsn1Sequence) subjectPKInfo.get(0)).get(0).toString();
        subjectAlgorithm = PkcsObjectIdentifier.forValue(tmp);
        if (subjectAlgorithm == null) {
            subjectAlgorithm = X9ObjectIdentifier.forValue(tmp);
        }
        subjectParameters = ((GXAsn1Sequence) subjectPKInfo.get(0)).get(1);

        // Make public key.
        KeyFactory eckf;
        try {
            if (algorithm.toString().endsWith("RSA")) {
                eckf = KeyFactory.getInstance("RSA");
            } else if (algorithm.toString().endsWith("ECDSA")) {
                eckf = KeyFactory.getInstance("EC");
            } else {
                throw new IllegalStateException(
                        "Unknown algorithm:" + algorithm.name());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(algorithm.name().substring(0, 2)
                    + "key factory not present in runtime");
        }
        try {
            byte[] encodedKey = GXAsn1Converter.toByteArray(subjectPKInfo);
            System.out.println(GXCommon.toHex(encodedKey));
            X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
            publicKey = eckf.generatePublic(ecpks);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }

        // publicKey = GXAsn1Converter.getPublicKey2(
        // X9ObjectIdentifier.IdECPublicKey, X9ObjectIdentifier.Prime256v1,
        // new GXAsn1PublicKey((GXAsn1BitString) subjectPKInfo.get(1)));
        pk = seq.get(1);
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
     * @return Algorithm
     */
    public final GXOid getSubjectAlgorithm() {
        return subjectAlgorithm;
    }

    /**
     * @param value
     *            Algorithm
     */
    public final void setSubjectAlgorithm(final GXOid value) {
        subjectAlgorithm = value;
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
     *            value
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

    public final byte[] getEncoded() {
        GXAsn1ObjectIdentifier a =
                new GXAsn1ObjectIdentifier(algorithm.getValue());
        GXAsn1Context p = new GXAsn1Context();
        p.add((byte) version.getValue());
        Object[] list = new Object[] {
                new Object[] { p, serialNumber, new Object[] { a, parameters },
                        GXAsn1Converter.encodeSubject(issuer),
                        new Object[] { validFrom, validTo },
                        GXAsn1Converter.encodeSubject(subject), subjectPKInfo },
                pk, new GXAsn1BitString(signature, 0) };
        return GXAsn1Converter.toByteArray(list);
    }

    /**
     * Sign certificate.
     * 
     * @param key
     *            Private key.
     */
    public final void sign(final PrivateKey key) {

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
        if (subjectAlgorithm != null) {
            bb.append(subjectAlgorithm.toString());
        }
        bb.append("\r\n");

        bb.append("Signature: ");
        bb.append(GXCommon.toHex(signature));
        bb.append("\r\n");
        return bb.toString();
    }
}
