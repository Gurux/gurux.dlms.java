package gurux.dlms.secure;

import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.enums.InterfaceType;

/**
 * Implements secured DLMS server.
 * 
 * @author Gurux Ltd.
 */
public abstract class GXDLMSSecureServerBase extends GXDLMSServerBase {
    /**
     * Ciphering settings.
     */
    private GXCiphering ciphering;

    /**
     * Constructor.
     * 
     * @param logicalNameReferencing
     *            Is logical name referencing used.
     * @param type
     *            Interface type.
     */
    public GXDLMSSecureServerBase(final boolean logicalNameReferencing,
            final InterfaceType type) {
        super(logicalNameReferencing, type);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    public final GXCiphering getCiphering() {
        return ciphering;
    }
}
