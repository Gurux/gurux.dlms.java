//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * Enumerates single write response types.
 */
final class SingleWriteResponse {
    /*
     * Constructor.
     */
    private SingleWriteResponse() {

    }

    /**
     * Write succeeded.
     */
    static final byte SUCCESS = 0;
    /**
     * Write error has occurred.
     */
    static final byte DATA_ACCESS_ERROR = 1;
    /**
     * Get next block.
     */
    static final byte BLOCK_NUMBER = 2;

}
