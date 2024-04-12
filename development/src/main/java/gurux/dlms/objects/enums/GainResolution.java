package gurux.dlms.objects.enums;

/**
 * Enumerates gain resolution steps.
 */
public enum GainResolution {
    /**
     * Step is 6 dB.
     */
    DB6,
    /**
     * Step is 3 dB.
     */
    DB3;

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
    public static GainResolution forValue(int value) {
        return values()[value];
    }
}