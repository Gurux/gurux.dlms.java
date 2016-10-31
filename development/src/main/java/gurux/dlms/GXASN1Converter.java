//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

/*
 * ASN1 converter. This class is used to convert 
 * public and private keys to byte array and vice verse.
 */
public final class GXASN1Converter {
    /*
     * Constructor.
     */
    private GXASN1Converter() {

    }

    /**
     * Convert bytes to ASN.1 DER.
     * 
     * @param data
     *            bytes to convert.
     * @return ASN.1 DER
     */
    public static byte[] encode(final byte[] data) {
        return data;
    }

    static int length(final byte[] der, final int pos) {
        if (der[pos] + pos > der.length) {
            throw new RuntimeException("Not enought memory.");
        }
        return der[pos];
    }

    /**
     * Convert ASN.1 DER to bytes.
     * 
     * @param der
     *            ASN.1 DER bytes.
     * @return bytes from DER structure.
     */
    public static byte[] getBytes(final byte[] der) {
        return der;
        // GXByteBuffer bb = new GXByteBuffer(der.length);
        // int len;
        // for (int pos = 0; pos != der.length; ++pos) {
        // byte it = der[pos];
        // // Get length.
        // len = length(der, ++pos);
        // switch (it) {
        // case BerType.CONSTRUCTED | BerType.SEQUENCE:
        // break;
        // case BerType.INTEGER:
        // bb.set(der, pos + 1, len);
        // pos += len;
        // break;
        // default:
        // throw new RuntimeException("Invalid data.");
        // }
        // }
        // return bb.array();
    }
}
