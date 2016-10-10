//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * Enumerates Set response types.
 */
final class SetResponseType {
    /*
     * Constructor.
     */
    private SetResponseType() {

    }

    /**
     * Normal set response.
     */
    static final byte NORMAL = 1;
    /**
     * Set response in data blocks.
     */
    static final byte DATA_BLOCK = 2;
    /**
     * Set response in last data block.
     */
    static final byte LAST_DATA_BLOCK = 3;
    /**
     * Set response in last data block with list.
     */
    static final byte LAST_DATA_BLOCK_WITH_LIST = 4;
    /**
     * Set with list response.
     */
    static final byte WITH_LIST = 5;

}
