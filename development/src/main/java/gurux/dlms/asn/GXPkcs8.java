//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import gurux.dlms.asn.enums.GXOid;
import gurux.dlms.asn.enums.PkcsObjectIdentifier;
import gurux.dlms.asn.enums.X9ObjectIdentifier;
import gurux.dlms.internal.GXCommon;

/**
 * Pkcs8 certification request. Private key is saved using this format.
 * https://tools.ietf.org/html/rfc5208
 */
public class GXPkcs8 {

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
    }

    /**
     * Constructor.
     * 
     * @param data
     *            Base64 string.
     */
    public GXPkcs8(final String data) {
        String tmp = data.replace("-----BEGIN PUBLIC KEY-----", "");
        tmp = tmp.replace("-----END PUBLIC KEY-----", "");
        tmp = tmp.replace("-----BEGIN RSA PRIVATE KEY-----", "");
        tmp = tmp.replace("-----END RSA PRIVATE KEY-----", "");
        tmp = tmp.replace("-----BEGIN EC PRIVATE KEY-----", "");
        tmp = tmp.replace("-----END EC PRIVATE KEY-----", "");
        tmp = tmp.replace("-----BEGIN PRIVATE KEY-----", "");
        tmp = tmp.replace("-----END PRIVATE KEY-----", "");
        init(GXCommon.fromBase64(tmp));
    }

    /**
     * Constructor.
     * 
     * @param data
     *            Encoded bytes.
     */
    public GXPkcs8(final byte[] data) {
        init(data);
    }

    // version Version,
    // privateKeyAlgorithm PrivateKeyAlgorithmIdentifier,
    // privateKey PrivateKey,
    // attributes [0]
    private void init(final byte[] data) {
        GXAsn1Sequence seq =
                (GXAsn1Sequence) GXAsn1Converter.fromByteArray(data);
        // If public key.
        if (seq.size() == 2) {
            GXAsn1Sequence tmp = (GXAsn1Sequence) seq.get(0);
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
                throw new IllegalStateException(
                        algorithm.toString().substring(0, 2)
                                + "key factory not present in runtime");
            }
            try {
                byte[] encodedKey = GXAsn1Converter.toByteArray(seq);
                X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
                setPublicKey(eckf.generatePublic(ecpks));
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e.getMessage());
            }

            // PublicKeyInfo ::= SEQUENCE {
            // algorithm AlgorithmIdentifier,
            // PublicKey BIT STRING
            // }
            // AlgorithmIdentifier ::= SEQUENCE {
            // algorithm OBJECT IDENTIFIER,
            // parameters ANY DEFINED BY algorithm OPTIONAL
            // }

        } else {
            if (seq.size() < 3) {
                throw new IllegalArgumentException(
                        "Wrong number of elements in sequence.");
            }
            version = CertificateVersion
                    .forValue(((Number) seq.get(0)).intValue());
            GXAsn1Sequence tmp = (GXAsn1Sequence) seq.get(1);
            algorithm = X9ObjectIdentifier.forValue(tmp.get(0).toString());
            if (algorithm == null) {
                algorithm =
                        PkcsObjectIdentifier.forValue(tmp.get(0).toString());
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
                throw new IllegalStateException(
                        algorithm.toString().substring(0, 2)
                                + "key factory not present in runtime");
            }
            try {
                PKCS8EncodedKeySpec ecpks = new PKCS8EncodedKeySpec(data);
                privateKey = (eckf.generatePrivate(ecpks));
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e.getMessage());
            }
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
     * @param value
     *            Private key.
     */
    public void setPrivateKey(final PrivateKey value) {
        privateKey = value;
    }

    /**
     * @return Public key.
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @param value
     *            Public key.
     */
    public void setPublicKey(final PublicKey value) {
        publicKey = value;
    }
}
