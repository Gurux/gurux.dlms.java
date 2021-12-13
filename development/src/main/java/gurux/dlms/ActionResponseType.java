//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * Enumerates Action response types.
 */
final class ActionResponseType {
    /*
     * Constructor.
     */
    private ActionResponseType() {

    }

    /**
     * Normal action.
     */
    static final byte NORMAL = 1;
    /**
     * Action with block.
     */
    static final byte WITH_BLOCK = 2;
    /**
     * Action with list.
     */
    static final byte WITH_LIST = 3;
    /**
     * Server asks the next block.
     */
    static final byte NEXT_BLOCK = 4;
}
