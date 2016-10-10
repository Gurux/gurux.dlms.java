//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * Enumerates Action request types.
 */
final class ActionRequestType {
    /*
     * Constructor.
     */
    private ActionRequestType() {

    }

    /**
     * Normal action.
     */
    static final byte NORMAL = 1;
    /**
     * Next block.
     */
    static final byte NEXT_BLOCK = 2;
    /**
     * Action with list.
     */
    static final byte WITH_LIST = 3;
    /**
     * Action with first block.
     */
    static final byte WITH_FIRST_BLOCK = 4;
    /**
     * Action with list and first block.
     */
    static final byte WITH_LIST_AND_FIRST_BLOCK = 5;
    /**
     * Action with block.
     */
    static final byte WITH_BLOCK = 6;
}
