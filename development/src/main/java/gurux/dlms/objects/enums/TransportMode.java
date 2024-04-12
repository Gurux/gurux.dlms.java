package gurux.dlms.objects.enums;

import java.util.HashMap;

/**
 * CoAP transport modes
 */
public enum TransportMode {
    /**
     * Reliable operation supported only.
     */
    RELIABLE(1),
    /**
     * Unreliable operation supported only,
     */
    UNRELIABLE(2),
    /**
     * Reliable and Unreliable operation supported.
     */
    RELIABLE_UNRELIABLE(3);

    /**
     * Integer value of enumerator.
     */
    private int intValue;

    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, TransportMode> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, TransportMode> getMappings() {
        synchronized (TransportMode.class) {
            if (mappings == null) {
                mappings = new HashMap<Integer, TransportMode>();
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
    TransportMode(final int value) {
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
    public static TransportMode forValue(final int value) {
        TransportMode ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException("Invalid transport mode enum value.");
        }
        return ret;
    }
}