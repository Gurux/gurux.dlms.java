package gurux.dlms.secure;

import gurux.dlms.GXDLMSClient;

/**
 * GXDLMSSecureClient implements secure client where all messages are secured
 * using transport security.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSSecureClient extends GXDLMSClient {

    /**
     * Ciphering settings.
     */
    private GXCiphering ciphering;

    /**
     * Constructor.
     */
    public GXDLMSSecureClient() {
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    public final GXCiphering getCiphering() {
        return ciphering;
    }
}
