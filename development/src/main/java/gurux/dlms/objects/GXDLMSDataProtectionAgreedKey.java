package gurux.dlms.objects;

/**
 * Data protection agreed key.
 */
public class GXDLMSDataProtectionAgreedKey {
    /**
     * Key parameters.
     */
    private byte[] parameters;

    /**
     * Key ciphered data.
     */
    private byte[] data;

    public final byte[] getParameters() {
        return parameters;
    }

    public final void setParameters(final byte[] value) {
        parameters = value;
    }

    /**
     * @return Key ciphered data.
     */
    public final byte[] getData() {
        return data;
    }

    /**
     * @param value
     *            Key ciphered data.
     */
    public final void setData(final byte[] value) {
        data = value;
    }
}