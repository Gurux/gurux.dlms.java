//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms.enums;

/**
 * Enumerates Access Service types.
 */
public final class AccessServiceCommandType {
    /**
     * Constructor.
     */
    private AccessServiceCommandType() {

    }

    /**
     * Get request or response.
     */
    public static final int GET = 1;
    /**
     * Set request or response.
     */
    public static final int SET = 2;
    /**
     * Action request or response.
     */
    public static final int ACTION = 3;
    /**
     * Get with selection request or response.
     */
    public static final int GET_WITH_SELECTION = 4;
    /**
     * Set with selection request or response.
     */
    public static final int SET_WITH_SELECTION = 5;
}
