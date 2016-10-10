//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/**
 * Enumerates how data is access on read or write.
 */
final class VariableAccessSpecification {
    /*
     * Constructor.
     */
    private VariableAccessSpecification() {

    }

    /**
     * Read data using SN.
     */
    static final byte VARIABLE_NAME = 2;
    /**
     * Get data using parameterized access.
     */
    static final byte PARAMETERISED_ACCESS = 4;
    /**
     * Get next block.
     */
    static final byte BLOCK_NUMBER_ACCESS = 5;
    /**
     * Read data as blocks.
     */
    static final byte READ_DATA_BLOCK_ACCESS = 6;
    /**
     * Write data as blocks.
     */
    static final byte WRITE_DATA_BLOCK_ACCESS = 7;
}
