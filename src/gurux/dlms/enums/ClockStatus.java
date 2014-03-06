package gurux.dlms.enums;

/*
 *  Defines Clock status.
 */
public enum ClockStatus
{   
    //OK.
    OK(0),
    //Invalid value.
    INVALID_VALUE(0x1),
    //Doubtful b value.
    DOUBTFUL_VALUE(0x2),
    //Different clock base c.
    DIFFERENT_CLOCK_BASE(0X4),
    //Invalid clock status d.
    INVALID_CLOCK_STATUS(0x8),
    //Reserved.
    RESERVED2(0x10),
    //Reserved.
    RESERVED3(0x20),
    //Reserved.
    RESERVED4(0x40),
    //Daylight saving active.
    DAYLIGHT_SAVE_ACTIVE(0x80);

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