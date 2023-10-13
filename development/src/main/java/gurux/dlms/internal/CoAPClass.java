package gurux.dlms.internal;

import java.util.HashMap;

/**
 * CoAP class.
 */
public enum CoAPClass {
    /**
     * Request method.
     */
    METHOD(0),
    /**
     * Success response.
     */
    SUCCESS(2),
    /**
     * Client error response.
     */
    CLIENT_ERROR(4),
    /**
     * Server error response.
     */
    SERVER_ERROR(5),
    /**
     * Signaling.
     */
    SIGNALING(7);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, CoAPClass> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, CoAPClass> getMappings() {
        if (mappings == null) {
            synchronized (CoAPClass.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, CoAPClass>();
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
    CoAPClass(final int value) {
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
    public static CoAPClass forValue(final int value) {
        CoAPClass ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid CoAP class enum value.");
        }
        return ret;
    }
}