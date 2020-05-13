//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

import java.util.HashMap;

/**
 * Encryption modes.
 */
public enum MBusEncryptionMode {
    /**
     * Encryption is not used.
     */
    NONE(0),
    /**
     * AES with Counter Mode (CTR) noPadding and IV.
     */
    AES_128(1),
    /**
     * DES with Cipher Block Chaining Mode (CBC).
     */
    DES_CBC(2),
    /**
     * DES with Cipher Block Chaining Mode (CBC) and Initial Vector.
     */
    DES_CBC_IV(3),
    /**
     * AES with Cipher Block Chaining Mode (CBC) and Initial Vector.
     */
    AES_CBC_IV(5),
    /**
     * AES 128 with Cipher Block Chaining Mode (CBC) and dynamic key and Initial
     * Vector with 0.
     */
    AES_CBC_IV0(7),
    /**
     * TLS
     */
    Tls(13);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, MBusEncryptionMode> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, MBusEncryptionMode> getMappings() {
        if (mappings == null) {
            synchronized (MBusEncryptionMode.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, MBusEncryptionMode>();
                }
            }
        }
        return mappings;
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Integer value of enumerator.
     */
    MBusEncryptionMode(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /**
     * Get integer value for enumerator.
     * 
     * @return Enumerator integer value.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static MBusEncryptionMode forValue(final int value) {
        MBusEncryptionMode ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid MBus encryption mode enum value.");
        }
        return ret;
    }
}