package gurux.dlms.secure;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import gurux.dlms.GXByteBuffer;

/**
 * This class is used to handle symmetric communication.
 */
public final class GXSymmetric {

    static SecretKeySpec getKey(byte[] pass) throws NoSuchAlgorithmException {
        final MessageDigest sha = MessageDigest.getInstance("SHA-256");

        byte[] key = sha.digest(pass);
        // use only first 128 bit (16 bytes). By default Java only supports AES
        // 128 bit key sizes for encryption.
        // Updated jvm policies are required for 256 bit.
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }

    public static Cipher getCipher(final AesGcmParameter p,
            final boolean encrypt)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        GXByteBuffer iv = new GXByteBuffer();
        iv.set(p.getSystemTitle());
        iv.setUInt32(p.getInvocationCounter());
        SecretKeySpec eks = new SecretKeySpec(p.getBlockCipherKey(), "AES");
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        int mode;
        if (encrypt) {
            mode = Cipher.ENCRYPT_MODE;
        } else {
            mode = Cipher.DECRYPT_MODE;
        }
        c.init(mode, eks, new GCMParameterSpec(12 * 8, iv.array()));
        return c;
    }

    public static void countTag(final Cipher c, final AesGcmParameter p,
            final byte[] data)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer data2 = new GXByteBuffer();
        data2.setUInt8(p.getSecurity().getValue());
        data2.set(p.getAuthenticationKey());
        if (data != null) {
            data2.set(data);
        }
        c.updateAAD(data2.array());
        byte[] tmp = c.doFinal();
        // DLMS Tag is only 12 bytes.
        byte[] tag = new byte[12];
        System.arraycopy(tmp, 0, tag, 0, 12);
        p.setCountTag(tag);
    }

    public static byte[] encrypt(final Cipher c, final byte[] data)
            throws Exception {
        byte[] es = c.doFinal(data);
        // Remove tag.
        byte[] tmp = new byte[es.length - 12];
        System.arraycopy(es, 0, tmp, 0, tmp.length);
        return tmp;
    }
}
