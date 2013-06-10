package gurux.dlms.enums;

public enum ClockStatus
{    
    OK(0),
    INVALID_VALUE(0x1),
    DOUBTFUL_VALUE(0x2),
    DIFFERENT_CLOCK_BASE(0X4),
    RESERVED1(0X8),
    RESERVED2(0x10),
    RESERVED3(0x20),
    RESERVED4(0x40),
    DAYLIGHT_SAVE_ACTIVE(0x40);

    private int value;
    private static java.util.HashMap<Integer, ClockStatus> mappings;
    private static java.util.HashMap<Integer, ClockStatus> getMappings()
    {
        synchronized (ClockStatus.class)
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, ClockStatus>();
            }
        }
        return mappings;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private ClockStatus(int value)
    {
        this.value = value;
        getMappings().put(value, this);
    }

    /*
     * Get integer value for enum.
     */
    public int getValue()
    {
        return value;
    }
    
    /*
     * Convert integer for enum value.
     */
    public static ClockStatus forValue(int value)
    {
        return getMappings().get(value);
    }
};