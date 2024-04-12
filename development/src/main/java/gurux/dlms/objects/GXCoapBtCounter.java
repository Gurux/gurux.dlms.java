package gurux.dlms.objects;

/**
 * Bt counter.
 */
public class GXCoapBtCounter {
    /**
     * CoAP Block-Wise Transfers started.
     */
    private long blockWiseTransferStarted;

    /**
     * CoAP Block-Wise Transfers completed.
     */
    private long blockWiseTransferCompleted;

    /**
     * CoAP Block-Wise Transfers timeouts.
     */
    private long blockWiseTransferTimeout;

    /**
     * @return CoAP Block-Wise Transfers started.
     */
    public final long getBlockWiseTransferStarted() {
        return blockWiseTransferStarted;
    }

    /**
     * @param value
     *            CoAP Block-Wise Transfers started.
     */
    public final void setBlockWiseTransferStarted(final long value) {
        blockWiseTransferStarted = value;
    }

    /**
     * @return CoAP Block-Wise Transfers completed.
     */
    public final long getBlockWiseTransferCompleted() {
        return blockWiseTransferCompleted;
    }

    /**
     * @param value
     *            CoAP Block-Wise Transfers completed.
     */
    public final void setBlockWiseTransferCompleted(final long value) {
        blockWiseTransferCompleted = value;
    }

    /**
     * @return CoAP Block-Wise Transfers timeouts.
     */
    public final long getBlockWiseTransferTimeout() {
        return blockWiseTransferTimeout;
    }

    /**
     * @param value
     *            CoAP Block-Wise Transfers timeouts.
     */
    public final void setBlockWiseTransferTimeout(final long value) {
        blockWiseTransferTimeout = value;
    }
}