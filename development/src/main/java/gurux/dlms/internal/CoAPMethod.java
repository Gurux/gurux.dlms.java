package gurux.dlms.internal;

/**
 * CoAP methods.
 */
public enum CoAPMethod {
    /**
     * Empty command.
     */
    NONE,
    /**
     * Get command.
     */
    GET,
    /**
     * Post command.
     */
    POST,
    /**
     * Put command.
     */
    PUT,
    /**
     * Delete command.
     */
    DELETE,
    /**
     * Fetch command.
     */
    FETCH,
    /**
     * Patch command.
     */
    PATCH,
    /**
     * IPatch command.
     */
    IPATCH;

    /**
     * @return Enumerated value as integer.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Convert integer value to enumerated value.
     * 
     * @param value
     *            Integer value.
     * @return Enumerated value.
     */
    public static CoAPMethod forValue(int value) {
        return values()[value];
    }
}
