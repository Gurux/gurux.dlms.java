package gurux.dlms.internal;

import java.util.HashMap;

/**
 * CoAP client error codes.
 */
public enum CoAPClientError {
    /**
     * Bad Request.
     */
    BAD_REQUEST(0),
    /**
     * Unauthorized.
     */
    UNAUTHORIZED(1),
    /**
     * Bad Option.
     */
    BAD_OPTION(2),
    /**
     * Forbidden.
     */
    FORBIDDEN(3),
    /**
     * Not Found.
     */
    NOT_FOUND(4),
    /**
     * Method Not Allowed.
     */
    METHOD_NOT_ALLOWED(5),
    /**
     * Not Acceptable.
     */
    NOT_ACCEPTABLE(6),
    /**
     * Request Entity Incomplete.
     */
    REQUEST_ENTITY_INCOMPLETE(8),
    /**
     * Conflict.
     */
    CONFLICT(9),
    /**
     * Precondition Failed.
     */
    PRECONDITION_FAILED(12),
    /**
     * Request Entity Too Large.
     */
    REQUEST_ENTITY_TOO_LARGE(13),
    /**
     * Unsupported Content-Format.
     */
    UNSUPPORTED_CONTENT_FORMAT(15);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, CoAPClientError> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static HashMap<Integer, CoAPClientError> getMappings() {
        if (mappings == null) {
            synchronized (CoAPClientError.class) {
                if (mappings == null) {
                    mappings = new HashMap<Integer, CoAPClientError>();
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
    CoAPClientError(final int value) {
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
    public static CoAPClientError forValue(final int value) {
        CoAPClientError ret = getMappings().get(value);
        if (ret == null) {
            throw new IllegalArgumentException(
                    "Invalid CoAP client error enum value.");
        }
        return ret;
    }
}