package gurux.dlms.internal;

/**
 * CoAP type.
 */
public enum CoAPType {
    /**
     * Request confirmable.
     */
    CONFIRMABLE,
    /**
     * Request non-confirmable.
     */
    NON_CONFIRMABLE,
    /**
     * Response acknowledgement.
     */
    ACKNOWLEDGEMENT,
    /**
     * Meter receives a message, but can't process it.
     */
    RESET;

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
    public static CoAPType forValue(int value) {
        return values()[value];
    }
}