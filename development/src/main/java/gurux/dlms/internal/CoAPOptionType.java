package gurux.dlms.internal;

import java.util.HashMap;

/**
 * CoAP option types.
 */
public enum CoAPOptionType {
    /**
     * If-Match.
     */
    IF_MATCH(1),
    /**
     * Uri-Host.
     */
    URI_HOST(3),
    /**
     * ETag.
     */
    E_TAG(4),
    /**
     * If-None-Match.
     */
    IF_NONE_MATCH(5),
    /**
     * Uri-Port.
     */
    URI_PORT(7),
    /**
     * Location-Path.
     */
    LOCATION_PATH(8),
    /**
     * Uri-Path.
     */
    URI_PATH(11),
    /**
     * Content-Format.
     */
    CONTENT_FORMAT(12),
    /**
     * Max-Age.
     */
    MAX_AGE(14),
    /**
     * Uri-Query.
     */
    URI_QUERY(15),
    /**
     * Accept.
     */
    ACCEPT(17),
    /**
     * Location-Query.
     */
    LOCATION_QUERY(20),
    /**
     * Block2.
     */
    BLOCK2(23),
    /**
     * Block1.
     */
    BLOCK1(27),
    /**
     * Proxy-Uri.
     */
    PROXY_URI(35),
    /**
     * Proxy-Scheme.
     */
    PROXY_SCHEME(39),
    /**
     * Size1.
     */
    SIZE1(60);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, CoAPOptionType> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, CoAPOptionType> getMappings() {
        if (mappings == null) {
            synchronized (CoAPOptionType.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, CoAPOptionType>();
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
    CoAPOptionType(final int value) {
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
    public static CoAPOptionType forValue(final int value) {
        CoAPOptionType ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid CoAP option type enum value.");
        }
        return ret;
    }
}