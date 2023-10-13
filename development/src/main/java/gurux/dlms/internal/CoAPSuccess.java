package gurux.dlms.internal;

import java.util.HashMap;

/**
 * CoAP success enumerates.
 */
public enum CoAPSuccess {
    /**
     * Empty success code.
     */
    NONE(0),
    /**
     * Created.
     */
    CREATED(1),
    /**
     * Deleted.
     */
    DELETED(2),
    /**
     * Valid.
     */
    VALID(3),
    /**
     * Changed.
     */
    CHANGED(4),
    /**
     * Content.
     */
    CONTENT(5),
    /**
     * Continue.
     */
    CONTINUE(31);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, CoAPSuccess> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, CoAPSuccess> getMappings() {
        if (mappings == null) {
            synchronized (CoAPSuccess.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, CoAPSuccess>();
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
    CoAPSuccess(final int value) {
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
    public static CoAPSuccess forValue(final int value) {
        CoAPSuccess ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid CoAP Success enum value.");
        }
        return ret;
    }
}
