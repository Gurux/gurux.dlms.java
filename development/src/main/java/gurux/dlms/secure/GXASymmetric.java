package gurux.dlms.secure;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.asn.GXAsn1BitString;
import gurux.dlms.asn.GXAsn1Converter;
import gurux.dlms.asn.GXAsn1Integer;
import gurux.dlms.asn.GXAsn1Sequence;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.SecuritySuite;

/*
 * This class is used to handle asymmetric communication. KDF generating is in
 * Blue book and
 * http://csrc.nist.gov/publications/nistpubs/800-56A/SP800-56A_Revision1_Mar08-2007.pdf
 */
public final class GXASymmetric {

    /*
     * Constructor.
     */
    private GXASymmetric() {

    }

    /*
     * Generate KDF.
     * @param hashAlg Hash Algorithm. (SHA-256 or SHA-384 )
     * @param z Shared Secret.
     * @param keyDataLen Key data length in bits.
     * @param algorithmID Algorithm ID.
     * @param partyUInfo Sender system title.
     * @param partyVInfo Receiver system title.
     * @param suppPubInfo Not used in DLMS.
     * @param suppPrivInfo Not used in DLMS.
     * @param securitySuite Used security suite.
     * @return Generated KDF.
     */
    public static byte[] generateKDF(final String hashAlg, final byte[] z,
            final int keyDataLen, final byte[] algorithmID,
            final byte[] partyUInfo, final byte[] partyVInfo,
            final byte[] suppPubInfo, final byte[] suppPrivInfo,
            final SecuritySuite securitySuite) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.set(algorithmID);
        bb.set(partyUInfo);
        bb.set(partyVInfo);
        if (suppPubInfo != null) {
            bb.set(suppPubInfo);
        }
        if (suppPrivInfo != null) {
            bb.set(suppPrivInfo);
        }
        return generateKDF(hashAlg, z, keyDataLen, bb.array(), securitySuite);
    }

    /*
     * Generate KDF.
     * @param hashAlg Hash Algorithm. (SHA-256 or SHA-384 )
     * @param z Shared Secret.
     * @param keyDataLen Key data length in bits.
     * @param otherInfo OtherInfo
     * @param securitySuite Used security suite.
     * @return Generated KDF.
     */
    public static byte[] generateKDF(final String hashAlg, final byte[] z,
            final int keyDataLen, final byte[] otherInfo,
            final SecuritySuite securitySuite) {
        byte[] key = new byte[keyDataLen / 8];
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlg);
            int hashLen = md.getDigestLength();
            int cnt = key.length / hashLen;
            byte[] v = new byte[4];
            for (int pos = 1; pos <= cnt; pos++) {
                md.reset();
                getBytes(v, pos);
                md.update(v);
                md.update(z);
                md.update(otherInfo);
                byte[] hash = md.digest();
                if (pos < cnt) {
                    System.arraycopy(hash, 0, key, hashLen * (pos - 1), hashLen);
                } else {
                    if (key.length % hashLen == 0) {
                        System.arraycopy(hash, 0, key, hashLen * (pos - 1),
                                hashLen);
                    } else {
                        System.arraycopy(hash, 0, key, hashLen * (pos - 1),
                                key.length % hashLen);
                    }
                }
            }
            if (securitySuite == SecuritySuite.ECDH_ECDSA_AES_GCM_128_SHA_256) {
                byte[] tmp = new byte[key.length / 2];
                System.arraycopy(key, 0, tmp, 0, tmp.length);
                return tmp;
            }
            return key;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /*
     * Convert int to byte array.
     * @param buff Buffer where data is saved.
     * @param value Integer value.
     */
    private static void getBytes(final byte[] buff, final int value) {
        buff[0] = (byte) (value >>> 24);
        buff[1] = (byte) ((value >>> 16) & 0xFF);
        buff[2] = (byte) ((value >>> 8) & 0xFF);
        buff[3] = (byte) (value & 0xFF);
    }

    /*
     * Get Ephemeral Public Key Signature.
     * @param keyId Key ID.
     * @param ephemeralKey Ephemeral key.
     * @param signKey Private Key.
     * @return Ephemeral Public Key Signature.
     */
    public static byte[] getEphemeralPublicKeySignature(final int keyId,
            final PublicKey ephemeralKey, final PrivateKey signKey)
            throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException {
        GXAsn1BitString tmp =
                (GXAsn1BitString) ((GXAsn1Sequence) GXAsn1Converter
                        .fromByteArray(ephemeralKey.getEncoded())).get(1);

        // Ephemeral public key client
        GXByteBuffer epk = new GXByteBuffer(tmp.getValue());
        // First byte is 4 in Java and that is not used. We can override it.
        epk.getData()[0] = (byte) keyId;
        // Add ephemeral public key signature.
        Signature instance = Signature.getInstance("SHA256withECDSA");
        instance.initSign(signKey);
        instance.update(epk.array());
        byte[] sign = instance.sign();
        GXAsn1Sequence tmp2 =
                (GXAsn1Sequence) GXAsn1Converter.fromByteArray(sign);
        System.out.println(GXCommon.toHex(sign));
        GXByteBuffer data = new GXByteBuffer(64);
        // Truncate to 64 bytes. Remove zeros from the begin.
        byte[] arr = ((GXAsn1Integer) tmp2.get(0)).getByteArray();
        data.set(arr, arr.length - 32, 32);
        arr = ((GXAsn1Integer) tmp2.get(1)).getByteArray();
        data.set(arr, arr.length - 32, 32);
        return data.array();
    }

    /*
     * Validate ephemeral public key signature.
     * @param data Data to validate.
     * @param sign Sign.
     * @param publicSigningKey Public Signing key from other party.
     * @return Is verify succeeded.
     */
    public static boolean validateEphemeralPublicKeySignature(final byte[] data,
            final byte[] sign, final PublicKey publicSigningKey)
            throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException {

        GXAsn1Integer a = new GXAsn1Integer(sign, 0, 32);
        GXAsn1Integer b = new GXAsn1Integer(sign, 32, 32);
        GXAsn1Sequence s = new GXAsn1Sequence();
        s.add(a);
        s.add(b);
        byte[] tmp = GXAsn1Converter.toByteArray(s);
        Signature instance = Signature.getInstance("SHA256withECDSA");
        instance.initVerify(publicSigningKey);
        instance.update(data);
        boolean v = instance.verify(tmp);
        if (!v) {
            System.out.println("Data: " + GXCommon.toHex(data));
            System.out.println("Sign: " + GXCommon.toHex(sign));
        }
        return v;
    }
}
