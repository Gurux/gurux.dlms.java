//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

import java.util.HashMap;

/**
 * M-Bus control info.
 */
public enum MBusControlInfo {
    /**
     * Long M-Bus data header present, direction master to slave
     */
    LONG_HEADER_MASTER(0x60),
    /**
     * Short M-Bus data header present, direction master to slave
     */
    SHORT_HEADER_MASTER(0x61),
    /**
     * Long M-Bus data header present, direction slave to master
     */
    LONG_HEADER_SLAVE(0x7C),
    /**
     * Short M-Bus data header present, direction slave to master
     */
    SHORT_HEADER_SLAVE(0x7D),
    /**
     * M-Bus short Header.
     */
    SHORT_HEADER(0x7A),
    /**
     * M-Bus long Header.
     */
    LONG_HEADER(0x72);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, MBusControlInfo> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, MBusControlInfo> getMappings() {
        if (mappings == null) {
            synchronized (MBusControlInfo.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, MBusControlInfo>();
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
    MBusControlInfo(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
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
    public static MBusControlInfo forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}