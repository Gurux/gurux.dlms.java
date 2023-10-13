package gurux.dlms.internal;

/**
 * CoAP Signaling .
 */
public enum CoAPSignaling {
    /**
     * Unassigned.
     */
    UNASSIGNED,
    /**
     * CSM.
     */
    CSM,
    /**
     * Ping.
     */
    PING,
    /**
     * Pong.
     */
    PONG,
    /**
     * Release.
     */
    RELEASE,
    /**
     * Forbidden.
     */
    ABORT;

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
    public static CoAPSignaling forValue(int value) {
        return values()[value];
    }
}