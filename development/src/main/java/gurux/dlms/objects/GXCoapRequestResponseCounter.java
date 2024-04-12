package gurux.dlms.objects;

/**
 * CoAP request response counter.
 */
public class GXCoapRequestResponseCounter {
    /**
     * CoAP requests received.
     */
    private long rxRequests;

    /**
     * CoAP requests sent.
     */
    private long txRequests;

    /**
     * CoAP responses received.
     */
    private long rxResponse;
    /**
     * CoAP responses sent.
     */
    private long txResponse;

    /**
     * CoAP client errors sent.
     */
    private long txClientError;
    /**
     * CoAP client errors received.
     */
    private long rxClientError;
    /**
     * CoAP server errors sent.
     */
    private long txServerError;
    /**
     * CoAP server errors received.
     */
    private long rxServerError;

    /**
     * @return CoAP requests received.
     */
    public final long getRxRequests() {
        return rxRequests;
    }

    /**
     * @param value
     *            CoAP requests received.
     */
    public final void setRxRequests(final long value) {
        rxRequests = value;
    }

    /**
     * @return CoAP requests sent.
     */
    public final long getTxRequests() {
        return txRequests;
    }

    /**
     * @param value
     *            CoAP requests sent.
     */
    public final void setTxRequests(final long value) {
        txRequests = value;
    }

    /**
     * @return CoAP responses received.
     */
    public final long getRxResponse() {
        return rxResponse;
    }

    /**
     * @param value
     *            CoAP responses received.
     */
    public final void setRxResponse(final long value) {
        rxResponse = value;
    }

    /**
     * @return CoAP responses sent.
     */
    public final long getTxResponse() {
        return txResponse;
    }

    /**
     * @param value
     *            CoAP responses sent.
     */
    public final void setTxResponse(final long value) {
        txResponse = value;
    }

    /**
     * @return CoAP client errors sent.
     */
    public final long getTxClientError() {
        return txClientError;
    }

    /**
     * @param value
     *            CoAP client errors sent.
     */
    public final void setTxClientError(final long value) {
        txClientError = value;
    }

    /**
     * @return CoAP client errors received.
     */
    public final long getRxClientError() {
        return rxClientError;
    }

    /**
     * @param value
     *            CoAP client errors received.
     */
    public final void setRxClientError(final long value) {
        rxClientError = value;
    }

    /**
     * @return CoAP server errors sent.
     */
    public final long getTxServerError() {
        return txServerError;
    }

    /**
     * @param value
     *            CoAP server errors sent.
     */
    public final void setTxServerError(final long value) {
        txServerError = value;
    }

    /**
     * @return CoAP server errors received.
     */
    public final long getRxServerError() {
        return rxServerError;
    }

    /**
     * @param value
     *            CoAP server errors received.
     */
    public final void setRxServerError(final long value) {
        rxServerError = value;
    }
}