//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn.enums;

/**
 * Certificate version.
 */
public final class CertificateVersion {

    public static final CertificateVersion V1 = new CertificateVersion(0);
    public static final CertificateVersion V2 = new CertificateVersion(1);
    public static final CertificateVersion V3 = new CertificateVersion(2);

    private byte value;
    private static java.util.HashMap<Byte, CertificateVersion> mappings;

    private static java.util.HashMap<Byte, CertificateVersion> getMappings() {
        synchronized (CertificateVersion.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Byte, CertificateVersion>();
            }
        }
        return mappings;
    }

    CertificateVersion(final int mode) {
        value = (byte) mode;
        getMappings().put(value, this);
    }

    /*
     * Get integer value for enumeration.
     */
    public byte getValue() {
        return value;
    }

    /*
     * Convert string for enumeration value.
     */
    public static CertificateVersion forValue(final int value) {
        return getMappings().get((byte) value);
    }

    @Override
    public String toString() {
        String str = "";
        if (value == 0) {
            str = "V1";
        } else if (value == 1) {
            str = "V2";
        } else if (value == 2) {
            str = "V3";
        }
        return str;
    }
}
