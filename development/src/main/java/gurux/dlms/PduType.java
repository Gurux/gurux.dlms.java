//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * APDU types.
 */
final class PduType {
    /**
     * Constructor.
     */
    private PduType() {
    }

    /**
     * IMPLICIT BIT STRING {version1 = 0)} DEFAULT {version1}
     */
    static final byte PROTOCOL_VERSION = 0;

    /**
     * Application-context-name
     */
    static final byte APPLICATION_CONTEXT_NAME = 1;

    /**
     * AP-title OPTIONAL
     */
    static final byte CALLED_AP_TITLE = 2;

    /**
     * AE-qualifier OPTIONAL.
     */
    static final byte CALLED_AE_QUALIFIER = 3;

    /**
     * AP-invocation-identifier OPTIONAL.
     */
    static final byte CALLED_AP_INVOCATION_ID = 4;

    /**
     * AE-invocation-identifier OPTIONAL
     */
    static final byte CALLED_AE_INVOCATION_ID = 5;

    /**
     * AP-title OPTIONAL
     */
    static final byte CALLING_AP_TITLE = 6;

    /**
     * AE-qualifier OPTIONAL
     */
    static final byte CALLING_AE_QUALIFIER = 7;

    /**
     * AP-invocation-identifier OPTIONAL
     */
    static final byte CALLING_AP_INVOCATION_ID = 8;

    /**
     * AE-invocation-identifier OPTIONAL
     */
    static final byte CALLING_AE_INVOCATION_ID = 9;

    /**
     * The following field shall not be present if only the kernel is used.
     */
    static final byte SENDER_ACSE_REQUIREMENTS = 10;

    /**
     * The following field shall only be present if the authentication
     * functional unit is selected.
     */
    static final byte MECHANISM_NAME = 11;

    /**
     * The following field shall only be present if the authentication
     * functional unit is selected.
     */
    static final byte CALLING_AUTHENTICATION_VALUE = 12;

    /**
     * Implementation-data.
     */
    static final byte IMPLEMENTATION_INFORMATION = 29;

    /**
     * Association-information OPTIONAL
     */
    static final byte USER_INFORMATION = 30;
}
