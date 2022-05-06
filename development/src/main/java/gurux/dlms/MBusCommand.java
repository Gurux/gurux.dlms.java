//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

import java.util.HashMap;

/**
 * M-Bus command.
 */
public enum MBusCommand {
    /**
     * Send unsolicited/periodical application data without request (Send/No
     * Reply)
     */
    SND_NR(0x4),
    /**
     * Send a command (Send User Data).
     */
    SND_UD(0x3),

    /**
     * Access demand from Meter to Other Device. This message requests an access
     * to the Meter (contains no application data).
     */
    RSP_UD(0x08);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, MBusCommand> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, MBusCommand> getMappings() {
        if (mappings == null) {
            synchronized (MBusCommand.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, MBusCommand>();
                }
            }
        }
        return mappings;
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Integer value of enumerator.
     */
    MBusCommand(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /**
     * Get integer value for enumerator.
     * 
     * @return Enumerator integer value.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static MBusCommand forValue(final int value) {
        MBusCommand ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException("Invalid MBus command enum value.");
        }
        return ret;
    }
}
