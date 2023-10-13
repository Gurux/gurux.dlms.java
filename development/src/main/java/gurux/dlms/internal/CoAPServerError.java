package gurux.dlms.internal;

/**
 * CoAP server error codes.
 */
public enum CoAPServerError {
    /**
     * Internal Server error.
     */
    INTERNAL,
    /**
     * Not implemented.
     */
    NOT_IMPLEMENTED,
    /**
     * Bad gateway.
     */
    BAD_GATEWAY,
    /**
     * Service unavailable.
     */
    SERVICE_UNAVAILABLE,
    /**
     * Gateway timeout.
     */
    GATEWAY_TIMEOUT,
    /**
     * Proxying not supported.
     */
    PROXYING_NOT_SUPPORTED;

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
    public static CoAPServerError forValue(int value) {
        return values()[value];
    }
}