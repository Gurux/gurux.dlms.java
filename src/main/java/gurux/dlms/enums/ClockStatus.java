package gurux.dlms.enums;

import java.util.EnumSet;

/**
 * Defines Clock status.
 */
public enum ClockStatus {
    /**
     * OK.
     */
    OK(0),

    /**
     * Invalid value.
     */
    INVALID_VALUE(0x1),

    /**
     * Doubtful b value.
     */
    DOUBTFUL_VALUE(0x2),

    /**
     * Different clock base c.
     */
    DIFFERENT_CLOCK_BASE(0X4),

    /**
     * Invalid clock status d.
     */
    INVALID_CLOCK_STATUS(0x8),

    /**
     * Reserved.
     */
    RESERVED2(0x10),

    /**
     * Reserved.
     */
    RESERVED3(0x20),

    /**
     * Reserved.
     */
    RESERVED4(0x40),

    /**
     * Daylight saving active.
     */
    DAYLIGHT_SAVE_ACTIVE(0x80),

    /**
     * Clock status is skipped.
     */
    SKIPPED(0xFF);

    private int value;
    private static java.util.HashMap<Integer, ClockStatus> mappings;

    private static java.util.HashMap<Integer, ClockStatus> getMappings() {
        synchronized (ClockStatus.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, ClockStatus>();
            }
        }
        return mappings;
    }

    ClockStatus(final int forValue) {
        this.value = forValue;
        getMappings().put(forValue, this);
    }

    /*
     * Get integer value for enumeration.
     */
    public int getValue() {
        return value;
    }

    /**
     * Converts the integer value to enumerated value.
     * 
     * @param value
     *            The integer value, which is read from the device.
     * @return The enumerated value, which represents the integer.
     */
    public static java.util.Set<ClockStatus> forValue(final int value) {
        EnumSet<ClockStatus> types;
        if (value == 0) {
            types = EnumSet.of(ClockStatus.OK);
        } else {
            types = EnumSet.noneOf(ClockStatus.class);
            ClockStatus[] enums = ClockStatus.class.getEnumConstants();
            for (int pos = 0; pos != enums.length; ++pos) {
                if (enums[pos] != ClockStatus.OK
                        && (enums[pos].value & value) == enums[pos].value) {
                    types.add(enums[pos]);
                }
            }
        }
        return types;
    }

    /**
     * Converts the enumerated value to integer value.
     * 
     * @param value
     *            The enumerated value.
     * @return The integer value.
     */
    public static int toInteger(final java.util.Set<ClockStatus> value) {
        int tmp = 0;
        for (ClockStatus it : value) {
            tmp |= it.getValue();
        }
        return tmp;
    }
};