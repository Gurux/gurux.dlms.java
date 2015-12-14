package gurux.dlms;

import gurux.dlms.enums.Command;

public interface GXICipher {

    /**
     * Encrypt PDU.
     * 
     * @param command
     *            Command.
     * @param data
     *            Data to encrypt.
     * @return Encrypted data.
     */
    byte[] encrypt(Command command, byte[] data);

    /**
     * Decrypt data.
     * 
     * @param data
     *            Decrypted data.
     */
    void decrypt(GXByteBuffer data);

    /**
     * Reset encrypt settings.
     */
    void reset();

    /**
     * @return Is ciphering used.
     */
    boolean isCiphered();
}
