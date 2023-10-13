package gurux.dlms;

import gurux.dlms.internal.CoAPClientError;
import gurux.dlms.internal.CoAPServerError;

/**
 * CoAP exception.
 */
public class GXDLMSCoAPException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * CoAP client error.
     */
    private CoAPClientError clientError;

    /**
     * CoAP server error.
     */
    private CoAPServerError serverError;

    /**
     * Constructor for the client error.
     * 
     * @param error
     *            Client error code.
     */
    public GXDLMSCoAPException(CoAPClientError error) {
        super(error.toString());
        clientError = error;
    }

    /**
     * Constructor for the server error.
     * 
     * @param error
     *            Server error code.
     */
    public GXDLMSCoAPException(CoAPServerError error) {
        super(error.toString());
        serverError = error;
    }

    /**
     * @return CoAP client error.
     */
    public final CoAPClientError getClientError() {
        return clientError;
    }

    /**
     * @return CoAP server error.
     */
    public final CoAPServerError getServerError() {
        return serverError;
    }
}
