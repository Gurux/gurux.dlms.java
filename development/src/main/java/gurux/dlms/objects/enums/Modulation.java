package gurux.dlms.objects.enums;

/**
 * Enumerates modulation types.
 */
public enum Modulation {
    /**
     * Robust Mode.
     */
    ROBUST_MODE,
    /**
     * DBPSK.
     */
    DBPSK,
    /**
     * DQPSK.
     */
    DQPSK,
    /**
     * D8PSK.
     */
    D8PSK,
    /**
     * 16-QAM.
     */
    QAM_16;

    /**
     * Get integer value for enumerator.
     * 
     * @return Enumerator integer value.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static Modulation forValue(int value) {
        return values()[value];
    }
}