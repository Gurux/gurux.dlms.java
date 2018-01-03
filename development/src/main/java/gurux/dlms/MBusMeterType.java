//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

import java.util.HashMap;

/**
 * M-Bus meter type.
 */
public enum MBusMeterType {
    /**
     * Oil meter.
     */
    OIL(1),
    /**
     * Energy meter.
     */
    ENERGY(2),
    /**
     * Gas meter.
     */
    GAS(3),
    /**
     * Water meter.
     */
    WATER(7),
    /**
     * Unknown meter type.
     */
    UNKNOWN(0x0F);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, MBusMeterType> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, MBusMeterType> getMappings() {
        if (mappings == null) {
            synchronized (MBusMeterType.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, MBusMeterType>();
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
    MBusMeterType(final int value) {
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
    public static MBusMeterType forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}