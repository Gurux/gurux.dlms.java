//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * Enumerates Set request types.
 */
final class SetRequestType {
    /*
     * Constructor.
     */
    private SetRequestType() {

    }

    /**
     * Normal Set.
     */
    static final byte NORMAL = 1;
    /**
     * Set with first data block.
     */
    static final byte FIRST_DATA_BLOCK = 2;
    /**
     * Set with data block.
     */
    static final byte WITH_DATA_BLOCK = 3;
    /**
     * Set with list .
     */
    static final byte WITH_LIST = 4;
    /**
     * Set with list and first data block.
     */
    static final byte WITH_LIST_AND_WITH_FIRST_DATABLOCK = 5;
}
