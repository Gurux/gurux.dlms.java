//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms.ecdsa;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;

import gurux.dlms.asn.enums.Ecc;

/**
 * ECDSA asynchronous ciphering.
 */
final public class GXEcdsa {

	/**
	 * Generates new key pair.
	 * 
	 * @return New Key pair.
	 * @throws NoSuchAlgorithmException           No such algorithm.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter.
	 */
	public static KeyPair generateKeyPair(final Ecc ecc)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		// Generate keys.
		if (ecc == Ecc.P256) {
			KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
			ECGenParameterSpec kpgparams = new ECGenParameterSpec("secp256r1");
			g.initialize(kpgparams);
			return g.generateKeyPair();
		}
		throw new IllegalArgumentException("P384 is not supported.");
	}

}
