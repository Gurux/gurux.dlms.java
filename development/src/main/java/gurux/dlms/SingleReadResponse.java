//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * Enumerates single read response types.
 */
final class SingleReadResponse {
    /*
     * Constructor.
     */
    private SingleReadResponse() {

    }

    /**
     * Normal data.
     */
    static final byte DATA = 0;
    /**
     * Error has occurred on read.
     */
    static final byte DATA_ACCESS_ERROR = 1;
    /**
     * Return data as blocks.
     */
    static final byte DATA_BLOCK_RESULT = 2;
    /**
     * Return block number.
     */
    static final byte BLOCK_NUMBER = 3;
}
