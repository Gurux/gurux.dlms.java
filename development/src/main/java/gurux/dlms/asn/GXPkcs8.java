//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn;

import java.security.PrivateKey;
import java.util.Base64;

import gurux.dlms.asn.enums.GXOid;
import gurux.dlms.asn.enums.X9ObjectIdentifier;

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
        init(Base64.getDecoder().decode(data));
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
        if (seq.size() != 3) {
            throw new IllegalArgumentException(
                    "Wrong number of elements in sequence.");
        }
        version = CertificateVersion.forValue(((Number) seq.get(0)).intValue());
        GXAsn1Sequence tmp = (GXAsn1Sequence) seq.get(1);
        algorithm = X9ObjectIdentifier.forValue(tmp.get(0).toString());

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
}
