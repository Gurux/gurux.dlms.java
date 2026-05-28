package gurux.dlms.compression.enums;

/**
 * Specifies predefined maximum string lengths that can be used to enforce
 * constraints on string values.
 */
public enum MaximumStringLength {
    /**
     * The maximum string length is 46 characters.
     */
    VALUE_46(46),

    /**
     * The maximum string length is 78 characters.
     */
    VALUE_78(78),

    /**
     * The maximum string length is 142 characters.
     */
    VALUE_142(142),

    /**
     * The maximum string length is 255 characters.
     */
    VALUE_255(255);

    /**
     * Maximum string length.
     */
    private final int value;

    /**
     * Constructor.
     *
     * @param forValue
     *            Maximum string length.
     */
    MaximumStringLength(final int forValue) {
        value = forValue;
    }

    /**
     * @return Get integer value for enumeration.
     */
    public int getValue() {
        return value;
    }

    /**
     * Convert integer for enumeration value.
     *
     * @param value
     *            Integer value of enumeration.
     * @return Enumerated value from integer.
     */
    public static MaximumStringLength forValue(final int value) {
        for (MaximumStringLength it : values()) {
            if (it.value == value) {
                return it;
            }
        }
        throw new IllegalArgumentException(String.valueOf(value));
    }
}
