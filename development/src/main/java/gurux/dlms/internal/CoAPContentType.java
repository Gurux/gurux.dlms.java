package gurux.dlms.internal;

import java.util.HashMap;

/**
 * Enumerated CoAP content types.
 */
public enum CoAPContentType {
    /**
     * None.
     */
    NONE(0),
    /**
     * Application/oscore.
     */
    APPLICATION_OSCORE(10001);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, CoAPContentType> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, CoAPContentType> getMappings() {
        if (mappings == null) {
            synchronized (CoAPContentType.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, CoAPContentType>();
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
    CoAPContentType(final int value) {
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
    public static CoAPContentType forValue(final int value) {
        CoAPContentType ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid CoAP content type enum value.");
        }
        return ret;
    }
}