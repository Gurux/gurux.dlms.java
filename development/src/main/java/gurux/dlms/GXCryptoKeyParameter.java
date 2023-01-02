package gurux.dlms;

import java.security.PrivateKey;
import java.security.PublicKey;

import gurux.dlms.enums.CryptoKeyType;
import gurux.dlms.enums.Security;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecurityPolicy;
import gurux.dlms.objects.enums.SecuritySuite;

/**
 * Crypto key parameter is used to get public or private key.
 */
public class GXCryptoKeyParameter {

    private int command;
    /**
     * Crypto key type.
     */
    private CryptoKeyType keyType;

    /**
     * Is data encrypted or decrypted.
     */
    private boolean encrypt;

    /**
     * Encrypted data.
     */
    private byte[] encrypted;

    /**
     * Decrypted data.
     */
    private byte[] plainText;

    /**
     * Used security.
     */
    private Security security;

    /**
     * Used security suite.
     */
    private SecuritySuite securitySuite;

    /**
     * Used security policy.
     */
    private java.util.Set<SecurityPolicy> securityPolicy;

    /**
     * Used certificate type.
     */
    private CertificateType certificateType;

    /**
     * System title
     */
    private byte[] systemTitle;

    /**
     * Recipient system title.
     */
    private byte[] recipientSystemTitle;

    /**
     * Block cipher key.
     */
    private byte[] blockCipherKey;

    /**
     * Authentication key.
     */
    private byte[] authenticationKey;

    /**
     * Frame counter. Invocation counter.
     */
    private long invocationCounter;

    /**
     * Transaction Id.
     */
    private byte[] transactionId;

    /**
     * Private key to used to encrypt the data.
     */
    private PrivateKey privateKey;

    /**
     * Public key to used to decrypt the data.
     */
    private PublicKey publicKey;

    /**
     * @return Is data encrypted or decrypted.
     */
    public final boolean getEncrypt() {
        return encrypt;
    }

    /**
     * @param value
     *            Is data encrypted or decrypted.
     */
    public final void setEncrypt(final boolean value) {
        encrypt = value;
    }

    /**
     * @return Used security suite.
     */
    public final SecuritySuite getSecuritySuite() {
        return securitySuite;
    }

    /**
     * @param value
     *            Used security suite.
     */
    public final void setSecuritySuite(final SecuritySuite value) {
        securitySuite = value;
    }

    /**
     * @return Used certificate type.
     */
    public final CertificateType getCertificateType() {
        return certificateType;
    }

    /**
     * @param value
     *            Used certificate type.
     */
    public final void setCertificateType(final CertificateType value) {
        certificateType = value;
    }

    /**
     * @return System title
     */
    public final byte[] getSystemTitle() {
        return systemTitle;
    }

    /**
     * @param value
     *            System title
     */
    public final void setSystemTitle(final byte[] value) {
        systemTitle = value;
    }

    /**
     * @return Private key to used to encrypt the data.
     */
    public final PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * @param value
     *            Private key to used to encrypt the data.
     */
    public final void setPrivateKey(final PrivateKey value) {
        privateKey = value;
    }

    /**
     * @return Public key to used to decrypt the data.
     */
    public final PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @param value
     *            Public key to used to decrypt the data.
     */
    public final void setPublicKey(final PublicKey value) {
        publicKey = value;
    }

    /**
     * @return Decrypted data.
     */
    public byte[] getPlainText() {
        return plainText;
    }

    /**
     * @param value
     *            Decrypted data.
     */
    public void setPlainText(final byte[] value) {
        plainText = value;
    }

    /**
     * @return Encrypted data.
     */
    public byte[] getEncrypted() {
        return encrypted;
    }

    /**
     * @param value
     *            Encrypted data.
     */
    public void setEncrypted(final byte[] value) {
        encrypted = value;
    }

    /**
     * @return Used security policy.
     */
    public java.util.Set<SecurityPolicy> getSecurityPolicy() {
        return securityPolicy;
    }

    /**
     * @param value
     *            Used security policy.
     */
    public void setSecurityPolicy(final java.util.Set<SecurityPolicy> value) {
        securityPolicy = value;
    }

    /**
     * @return Recipient system title.
     */
    public byte[] getRecipientSystemTitle() {
        return recipientSystemTitle;
    }

    /**
     * @param value
     *            Recipient system title.
     */
    public void setRecipientSystemTitle(final byte[] value) {
        recipientSystemTitle = value;
    }

    /**
     * @return Block cipher key.
     */
    public byte[] getBlockCipherKey() {
        return blockCipherKey;
    }

    /**
     * @param value
     *            Block cipher key.
     */
    public void setBlockCipherKey(final byte[] value) {
        blockCipherKey = value;
    }

    /**
     * @return Authentication key.
     */
    public byte[] getAuthenticationKey() {
        return authenticationKey;
    }

    /**
     * @param value
     *            Authentication key.
     */
    public void setAuthenticationKey(final byte[] value) {
        authenticationKey = value;
    }

    /**
     * @return Frame counter. Invocation counter.
     */
    public long getInvocationCounter() {
        return invocationCounter;
    }

    /**
     * @param value
     *            Frame counter. Invocation counter.
     */
    public void setInvocationCounter(final long value) {
        invocationCounter = value;
    }

    /**
     * @return Transaction Id.
     */
    public byte[] getTransactionId() {
        return transactionId;
    }

    /**
     * @param value
     *            Transaction Id.
     */
    public void setTransactionId(byte[] value) {
        transactionId = value;
    }

    /**
     * @return Crypto key type.
     */
    public CryptoKeyType getKeyType() {
        return keyType;
    }

    /**
     * @param value
     *            Crypto key type.
     */
    public void setKeyType(final CryptoKeyType value) {
        keyType = value;
    }

    /**
     * @return Command.
     */
    public int getCommand() {
        return command;
    }

    /**
     * @param value
     *            Command.
     */
    public void setCommand(final int value) {
        command = value;
    }

    /**
     * @return Used security
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * @param value
     *            Used security
     */
    public void setSecurity(final Security value) {
        security = value;
    }
}
