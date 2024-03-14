package gurux.dlms.objects;

import gurux.dlms.objects.enums.DataProtectionWrappedKeyType;

/**
 * Data protection wrapped key.
 */
public class GXDLMSDataProtectionWrappeddKey {
    /**
     * Data protectionKey type.
     */
    private DataProtectionWrappedKeyType keyType = DataProtectionWrappedKeyType.MASTER_KEY;

    /**
     * Key ciphered data.
     */
    private byte[] key;

    /**
     * @return Data protectionKey type.
     */
    public final DataProtectionWrappedKeyType getKeyType() {
        return keyType;
    }

    /**
     * @param value
     *            Data protectionKey type.
     */
    public final void setKeyType(final DataProtectionWrappedKeyType value) {
        keyType = value;
    }

    /**
     * @return Key ciphered data.
     */
    public final byte[] getKey() {
        return key;
    }

    /**
     * @param value
     *            Key ciphered data.
     */
    public final void setKey(final byte[] value) {
        key = value;
    }
}