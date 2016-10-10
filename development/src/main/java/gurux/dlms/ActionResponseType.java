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
     * Action with first block.
     */
    static final byte WITH_FIRST_BLOCK = 2;
    /**
     * Action with list.
     */
    static final byte WITH_LIST = 3;
    /**
     * Action with next block.
     */
    static final byte WITH_BLOCK = 4;
}
