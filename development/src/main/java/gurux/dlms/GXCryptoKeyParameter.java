package gurux.dlms;

import java.security.PrivateKey;
import java.security.PublicKey;

import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecuritySuite;

/**
 * Crypto key parameter is used to get public or private key.
 */
public class GXCryptoKeyParameter {
	/**
	 * Is data encrypted or decrypted.
	 */
	private boolean encrypt;

	/**
	 * Used security suite.
	 */
	private SecuritySuite securitySuite;

	/**
	 * Used certificate type.
	 */
	private CertificateType certificateType;

	/**
	 * System title
	 */
	private byte[] systemTitle;
	/**
	 * Private key to used to encrypt the data.
	 */
	private PrivateKey privateKey;

	/**
	 * Public key to used to decrypt the data.
	 */
	private PublicKey publicKey;

	/**
	 * 
	 * @return Is data encrypted or decrypted.
	 */
	public final boolean getEncrypt() {
		return encrypt;
	}

	/**
	 * 
	 * @param value Is data encrypted or decrypted.
	 */
	public final void setEncrypt(final boolean value) {
		encrypt = value;
	}

	/**
	 * 
	 * @return Used security suite.
	 */
	public final SecuritySuite getSecuritySuite() {
		return securitySuite;
	}

	/**
	 * 
	 * @param value Used security suite.
	 */
	public final void setSecuritySuite(final SecuritySuite value) {
		securitySuite = value;
	}

	/**
	 * 
	 * @return Used certificate type.
	 */
	public final CertificateType getCertificateType() {
		return certificateType;
	}

	/**
	 * 
	 * @param value Used certificate type.
	 */
	public final void setCertificateType(final CertificateType value) {
		certificateType = value;
	}

	/**
	 * 
	 * @return System title
	 */
	public final byte[] getSystemTitle() {
		return systemTitle;
	}

	/**
	 * 
	 * @param value System title
	 */
	public final void setSystemTitle(final byte[] value) {
		systemTitle = value;
	}

	/**
	 * 
	 * @return Private key to used to encrypt the data.
	 */
	public final PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 
	 * @param value Private key to used to encrypt the data.
	 */
	public final void setPrivateKey(final PrivateKey value) {
		privateKey = value;
	}

	/**
	 * 
	 * @return Public key to used to decrypt the data.
	 */
	public final PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * 
	 * @param value Public key to used to decrypt the data.
	 */
	public final void setPublicKey(final PublicKey value) {
		publicKey = value;
	}
}
