//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms.asn;

import java.io.StringReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.asn.enums.X509Name;
import gurux.dlms.enums.BerType;
import gurux.dlms.internal.GXCommon;

/*
 * ASN1 converter. This class is used to convert 
 * public and private keys to byte array and vice verse.
 */
public final class GXAsn1Converter {

    /*
     * Constructor.
     */
    private GXAsn1Converter() {

    }

    /**
     * Get private key from bytes.
     * 
     * @param value
     *            Private key bytes.
     * @return Private key.
     * @throws InvalidKeySpecException
     *             Invalid key spec.
     * @throws NoSuchAlgorithmException
     *             No such Algorithm.
     */
    public static PrivateKey getPrivateKey(final byte[] value)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (value == null || value.length != 32) {
            throw new IllegalArgumentException("Invalid private key.");
        }
        byte[] privKeyBytes =
                GXCommon.hexToBytes("3041020100301306072A8648CE3D0201"
                        + "06082A8648CE3D030107042730250201010420");
        byte[] key = new byte[privKeyBytes.length + value.length];
        System.arraycopy(privKeyBytes, 0, key, 0, privKeyBytes.length);
        System.arraycopy(value, 0, key, privKeyBytes.length, value.length);
        PKCS8EncodedKeySpec priv = new PKCS8EncodedKeySpec(key);
        KeyFactory kf = KeyFactory.getInstance("EC");
        System.out.println(GXCommon.toHex(key));
        return kf.generatePrivate(priv);
    }

    private static byte[] p256Head =
            GXCommon.fromBase64("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE");

    /*
     * Get public key from bytes (P256Key).
     * @param value Public key bytes.
     * @return Public key.
     */
    public static PublicKey getPublicKey(final byte[] value) {
        if (value == null || value.length != 64) {
            throw new IllegalArgumentException("Invalid public key.");
        }
        byte[] encodedKey = new byte[p256Head.length + value.length];
        System.arraycopy(p256Head, 0, encodedKey, 0, p256Head.length);
        System.arraycopy(value, 0, encodedKey, p256Head.length, value.length);
        KeyFactory eckf;
        try {
            eckf = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "EC key factory not present in runtime");
        }
        try {
            X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
            return eckf.generatePublic(ecpks);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Convert public key to UInt 64.
     * 
     * @param key
     *            Public key.
     * @return Public key in byte array.
     */
    public static byte[] toUIn64(final PublicKey key) {
        if (key == null) {
            throw new IllegalArgumentException("Invalid public key.");
        }
        GXAsn1BitString tmp =
                (GXAsn1BitString) ((GXAsn1Sequence) GXAsn1Converter
                        .fromByteArray(key.getEncoded())).get(1);
        GXByteBuffer bb = new GXByteBuffer();
        bb.set(tmp.getValue(), 1, 64);
        return bb.array();
    }

    /**
     * Generates new key pair.
     * 
     * @return New Key pair.
     * @throws NoSuchAlgorithmException
     *             No such algorithm.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter.
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        // Generate keys.
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec kpgparams = new ECGenParameterSpec("secp256r1");
        g.initialize(kpgparams);
        return g.generateKeyPair();
    }

    static List<GXSimpleEntry<Object, Object>>
            encodeSubject(final String value) {
        X509Name name;
        Object val;
        List<GXSimpleEntry<Object, Object>> list =
                new ArrayList<GXSimpleEntry<Object, Object>>();
        for (String tmp : value.split("[,]")) {
            String[] it = tmp.split("[=]");
            if (it.length != 2) {
                throw new IllegalArgumentException("Invalid subject.");
            }
            name = X509Name.valueOf(it[0].trim());
            switch (name) {
            case C:
                // Country code is printable string
                val = it[1].trim();
                break;
            case E:
                // email address in Verisign certificates
                val = new GXAsn1Ia5String(it[1].trim());
                break;
            default:
                val = new GXAsn1Utf8String(it[1].trim());
            }
            String oid = name.getValue();
            list.add(new GXSimpleEntry<Object, Object>(
                    new GXAsn1ObjectIdentifier(oid), val));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    static String getSubject(final GXAsn1Sequence values) {
        Object value;
        StringBuilder sb = new StringBuilder();
        for (Object tmp : values) {
            Map.Entry<Object, Object> it = (Map.Entry<Object, Object>) tmp;
            sb.append(X509Name.forValue(it.getKey().toString()));
            sb.append('=');
            value = it.getValue();
            sb.append(value);
            sb.append(", ");
        }
        // Remove last comma.
        if (sb.length() != 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private static void getValue(final GXByteBuffer bb,
            final List<Object> objects, final GXAsn1Settings s) {
        int len;
        short type;
        List<Object> tmp = null;
        byte[] tmp2;
        type = bb.getUInt8();
        len = GXCommon.getObjectCount(bb);
        if (len > bb.size() - bb.position()) {
            throw new IllegalArgumentException("Not enought memory.");
        }
        int start = bb.position();
        String tagString = null;
        if (s != null) {
            s.appendSpaces();
            if (type == BerType.INTEGER) {
                if (len == 1 || len == 2 || len == 4 || len == 8) {
                    tagString = s.getTag((short) -len);
                } else {
                    tagString = s.getTag(BerType.INTEGER);
                }
            } else {
                tagString = s.getTag(type);
            }
            s.append("<" + tagString + ">");
        }

        switch (type) {
        case BerType.CONSTRUCTED | BerType.CONTEXT:
        case BerType.CONSTRUCTED | BerType.CONTEXT | 1:
        case BerType.CONSTRUCTED | BerType.CONTEXT | 2:
        case BerType.CONSTRUCTED | BerType.CONTEXT | 3:
            if (s != null) {
                s.increase();
            }
            tmp = new GXAsn1Context();
            ((GXAsn1Context) tmp).setIndex(type & 0xF);
            objects.add(tmp);
            while (bb.position() < start + len) {
                getValue(bb, tmp, s);
            }
            if (s != null) {
                s.decrease();
            }
            break;
        case BerType.CONSTRUCTED | BerType.SEQUENCE:
            if (s != null) {
                s.increase();
            }
            tmp = new GXAsn1Sequence();
            objects.add(tmp);
            while (bb.position() < start + len) {
                getValue(bb, tmp, s);
            }
            if (s != null) {
                s.decrease();
            }
            break;
        case BerType.CONSTRUCTED | BerType.SET:
            if (s != null) {
                s.increase();
            }
            tmp = new ArrayList<Object>();
            getValue(bb, tmp, s);
            if (tmp.get(0) instanceof GXAsn1Sequence) {
                tmp = (GXAsn1Sequence) tmp.get(0);
                objects.add(new GXSimpleEntry<Object, Object>(tmp.get(0),
                        tmp.get(1)));
            } else {
                GXSimpleEntry<Object, Object> e =
                        new GXSimpleEntry<Object, Object>(tmp, null);
                objects.add(e);
            }
            if (s != null) {
                s.decrease();
            }
            break;
        case BerType.OBJECT_IDENTIFIER:
            objects.add(new GXAsn1ObjectIdentifier(bb, len));
            if (s != null) {
                s.append(String.valueOf(objects.get(objects.size() - 1)));
            }

            break;
        case BerType.PRINTABLE_STRING:
            objects.add(bb.getString(len));
            if (s != null) {
                s.append(String.valueOf(objects.get(objects.size() - 1)));
            }

            break;
        case BerType.UTF8STRING:
            objects.add(new GXAsn1Utf8String(
                    bb.getString(bb.position(), len, "UTF-8")));
            bb.position(bb.position() + len);
            if (s != null) {
                s.append(String.valueOf(objects.get(objects.size() - 1)));
            }

            break;
        case BerType.IA5_STRING:
            objects.add(new GXAsn1Ia5String(bb.getString(len)));
            if (s != null) {
                s.append(String.valueOf(objects.get(objects.size() - 1)));
            }
            break;
        case BerType.INTEGER:
            if (len == 1) {
                objects.add(bb.getInt8());
            } else if (len == 2) {
                objects.add(bb.getInt16());
            } else if (len == 4) {
                objects.add(bb.getInt32());
            } else {
                tmp2 = new byte[len];
                bb.get(tmp2);
                objects.add(new GXAsn1Integer(tmp2));
            }
            if (s != null) {
                s.append(String.valueOf(objects.get(objects.size() - 1)));
            }
            break;
        case BerType.NULL:
            objects.add(null);
            break;
        case BerType.BIT_STRING:
            objects.add(new GXAsn1BitString(bb.subArray(bb.position(), len)));
            bb.position(bb.position() + len);
            if (s != null) {
                GXAsn1BitString tmp3 =
                        (GXAsn1BitString) objects.get(objects.size() - 1);
                s.append(tmp3.asString());
            }
            break;
        case BerType.UTC_TIME:
            tmp2 = new byte[len];
            bb.get(tmp2);
            objects.add(getUtcTime(new String(tmp2)));
            if (s != null) {
                DateFormat f = new SimpleDateFormat();
                s.append(f.format(objects.get(objects.size() - 1)));
            }
            break;
        case BerType.GENERALIZED_TIME:
            tmp2 = new byte[len];
            bb.get(tmp2);
            objects.add(GXCommon.getGeneralizedTime(new String(tmp2)));
            if (s != null) {
                s.append(String.valueOf(objects.get(objects.size() - 1)));
            }
            break;
        case BerType.CONTEXT:
        case BerType.OCTET_STRING:
            tmp2 = new byte[len];
            bb.get(tmp2);
            objects.add(tmp2);
            if (s != null) {
                s.append(GXCommon.toHex(tmp2));
            }
            break;
        case BerType.BOOLEAN:
            objects.add(bb.getUInt8() != 0);
            break;
        default:
            throw new RuntimeException("Invalid type: " + type);
        }
        if (s != null) {
            s.append("</" + tagString + ">\r\n");
        }
    }

    private static Date getUtcTime(final String dateString) {
        int year, month, day, hour, minute, second = 0;
        Calendar calendar;
        year = 2000 + Integer.parseInt(dateString.substring(0, 2));
        month = Integer.parseInt(dateString.substring(2, 4)) - 1;
        day = Integer.parseInt(dateString.substring(4, 6));
        hour = Integer.parseInt(dateString.substring(6, 8));
        minute = Integer.parseInt(dateString.substring(8, 10));
        // If UTC time.
        if (dateString.endsWith("Z")) {
            if (dateString.length() > 11) {
                second = Integer.parseInt(dateString.substring(10, 12));
            }
            calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        } else {
            if (dateString.length() > 15) {
                second = Integer.parseInt(dateString.substring(10, 12));
            }
            calendar = Calendar.getInstance(TimeZone.getTimeZone(
                    "GMT" + dateString.substring(dateString.length() - 6,
                            dateString.length() - 1)));
        }
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTime();
    }

    private static String dateToString(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long v = calendar.getTimeInMillis();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(v);
        StringBuffer sb = new StringBuffer();
        sb.append(
                GXCommon.integerString(calendar.get(Calendar.YEAR) - 2000, 2));
        sb.append(GXCommon.integerString(1 + calendar.get(Calendar.MONTH), 2));
        sb.append(
                GXCommon.integerString(calendar.get(Calendar.DAY_OF_MONTH), 2));
        sb.append(
                GXCommon.integerString(calendar.get(Calendar.HOUR_OF_DAY), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.MINUTE), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.SECOND), 2));
        sb.append("Z");
        return sb.toString();
    }

    /**
     * Convert byte array to ASN1 objects.
     * 
     * @param data
     *            ASN-1 bytes.
     * @return Parsed objects.
     */
    public static Object fromByteArray(final byte[] data) {
        GXByteBuffer bb = new GXByteBuffer(data);
        List<Object> objects = new ArrayList<Object>();
        while (bb.position() != bb.size()) {
            getValue(bb, objects, null);
        }
        return objects.get(0);
    }

    /**
     * Add ASN1 object to byte buffer.
     * 
     * @param bb
     *            Byte buffer where ANS1 object is serialized.
     * @param target
     *            ANS1 object
     * @return Size of object.
     */
    @SuppressWarnings("unchecked")
    private static int getBytes(final GXByteBuffer bb, final Object target) {
        GXByteBuffer tmp;
        String str;
        int start = bb.size();
        int cnt = 0;
        if (target instanceof GXAsn1Context) {
            tmp = new GXByteBuffer();
            for (Object it : ((GXAsn1Context) target)) {
                cnt += getBytes(tmp, it);
            }
            start = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.CONTEXT);
            GXCommon.setObjectCount(cnt, bb);
            cnt += bb.size() - start;
            bb.set(tmp);
            return cnt;
        } else if (target instanceof Object[]) {
            tmp = new GXByteBuffer();
            for (Object it : (Object[]) target) {
                cnt += getBytes(tmp, it);
            }
            start = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.SEQUENCE);
            GXCommon.setObjectCount(cnt, bb);
            cnt += bb.size() - start;
            bb.set(tmp);
            return cnt;
        } else if (target instanceof GXAsn1Sequence) {
            tmp = new GXByteBuffer();
            for (Object it : (List<Object>) target) {
                cnt += getBytes(tmp, it);
            }
            start = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.SEQUENCE);
            GXCommon.setObjectCount(cnt, bb);
            cnt += bb.size() - start;
            bb.set(tmp);
            return cnt;
        } else if (target instanceof List) {
            tmp = new GXByteBuffer();
            for (Object it : (List<Object>) target) {
                cnt += getBytes(tmp, it);
            }
            start = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.SEQUENCE);
            GXCommon.setObjectCount(cnt, bb);
            cnt += bb.size() - start;
            bb.set(tmp);
            return cnt;
        } else if (target instanceof String) {
            bb.setUInt8(BerType.PRINTABLE_STRING);
            GXCommon.setObjectCount(((String) target).length(), bb);
            bb.add(target);
        } else if (target instanceof Byte) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(1, bb);
            bb.add(target);
        } else if (target instanceof Short) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(2, bb);
            bb.add(target);
        } else if (target instanceof Integer) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(4, bb);
            bb.add(target);
        } else if (target instanceof GXAsn1Integer) {
            bb.setUInt8(BerType.INTEGER);
            byte[] b = ((GXAsn1Integer) target).getByteArray();
            GXCommon.setObjectCount(b.length, bb);
            bb.set(b);
        } else if (target instanceof Long) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(8, bb);
            bb.add(target);
        } else if (target instanceof byte[]) {
            bb.setUInt8(BerType.OCTET_STRING);
            GXCommon.setObjectCount(((byte[]) target).length, bb);
            bb.add(target);
        } else if (target == null) {
            bb.setUInt8(BerType.NULL);
            GXCommon.setObjectCount(0, bb);
        } else if (target instanceof Boolean) {
            bb.setUInt8(BerType.BOOLEAN);
            bb.setUInt8(1);
            if ((Boolean) target) {
                bb.setUInt8(255);
            } else {
                bb.setUInt8(0);
            }
        } else if (target instanceof GXAsn1ObjectIdentifier) {
            bb.setUInt8(BerType.OBJECT_IDENTIFIER);
            byte[] t = ((GXAsn1ObjectIdentifier) target).getEncoded();
            GXCommon.setObjectCount(t.length, bb);
            bb.add(t);
        } else if (target instanceof Entry) {
            Entry<Object, Object> e = (Entry<Object, Object>) target;
            GXByteBuffer tmp2 = new GXByteBuffer();
            if (e.getValue() != null) {
                tmp = new GXByteBuffer();
                cnt += getBytes(tmp2, e.getKey());
                cnt += getBytes(tmp2, e.getValue());
                tmp.setUInt8(BerType.CONSTRUCTED | BerType.SEQUENCE);
                GXCommon.setObjectCount(cnt, tmp);
                tmp.set(tmp2);
            } else {
                cnt += getBytes(tmp2, ((List<Object>) e.getKey()).get(0));
                tmp = tmp2;
            }
            // Update len.
            cnt = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.SET);
            GXCommon.setObjectCount(tmp.size(), bb);
            bb.set(tmp);
            return bb.size() - cnt;
        } else if (target instanceof GXAsn1Utf8String) {
            bb.setUInt8(BerType.UTF8STRING);
            str = target.toString();
            GXCommon.setObjectCount(str.length(), bb);
            bb.add(str);
        } else if (target instanceof GXAsn1Ia5String) {
            bb.setUInt8(BerType.IA5_STRING);
            str = target.toString();
            GXCommon.setObjectCount(str.length(), bb);
            bb.add(str);
        } else if (target instanceof GXAsn1BitString) {
            GXAsn1BitString bs = (GXAsn1BitString) target;
            bb.setUInt8(BerType.BIT_STRING);
            GXCommon.setObjectCount(1 + bs.getValue().length, bb);
            bb.setUInt8(bs.getPadBits());
            bb.add(bs.getValue());
        } else if (target instanceof GXAsn1PublicKey) {
            GXAsn1PublicKey bs = (GXAsn1PublicKey) target;
            bb.setUInt8(BerType.BIT_STRING);
            // Size is 64 bytes + padding and uncompressed point indicator.
            GXCommon.setObjectCount(66, bb);
            // Add padding.
            bb.setUInt8(0);
            // prefixed with the uncompressed point indicator 04
            bb.setUInt8(4);
            bb.add(bs.getValue());
            // Count is type + size + 64 bytes + padding + uncompressed point
            // indicator.
            return 68;
        } else if (target instanceof Date) {
            // Save date time in UTC.
            bb.setUInt8(BerType.UTC_TIME);
            str = dateToString((Date) target);
            bb.setUInt8(str.length());
            bb.add(str);
        } else {
            throw new RuntimeException(
                    "Invalid type: " + target.getClass().toString());
        }
        return bb.size() - start;
    }

    /**
     * Convert ASN1 objects to byte array.
     * 
     * @param objects
     *            ASN.1 objects.
     * @return ASN.1 objects as byte array.
     */
    public static byte[] toByteArray(final Object objects) {
        GXByteBuffer bb = new GXByteBuffer();
        getBytes(bb, objects);
        return bb.array();
    }

    /**
     * Convert ASN1 PDU bytes to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted XML.
     */
    public static String pduToXml(final String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        if (!GXCommon.isHexString(value)) {
            return pduToXml(GXCommon.fromBase64(value));
        }
        return pduToXml(new GXByteBuffer(GXCommon.hexToBytes(value)));
    }

    /**
     * Convert ASN1 PDU bytes to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted XML.
     */
    public static String pduToXml(final byte[] value) {
        return pduToXml(new GXByteBuffer(value));
    }

    /**
     * Convert ASN.1 PDU bytes to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted XML.
     */
    public static String pduToXml(final GXByteBuffer value) {
        GXAsn1Settings s = new GXAsn1Settings();
        List<Object> objects = new ArrayList<Object>();
        while (value.position() != value.size()) {
            getValue(value, objects, s);
        }
        return s.toString();
    }

    @SuppressWarnings("rawtypes")
    private static int readNode(final Node node, final GXAsn1Settings s,
            final List<Object> list) {
        List<Object> tmp;
        String str = node.getNodeName().toLowerCase();
        int tag = s.getTag(str);
        switch (tag) {
        case BerType.APPLICATION:
            tmp = new ArrayList<Object>();
            for (int pos = 0; pos != node.getChildNodes().getLength(); ++pos) {
                Node node2 = node.getChildNodes().item(pos);
                if (node2.getNodeType() == Node.ELEMENT_NODE) {
                    readNode(node2, s, tmp);
                }
            }
            list.add(tmp);
            break;
        case BerType.CONSTRUCTED | BerType.CONTEXT:
            tmp = new GXAsn1Context();
            for (int pos = 0; pos != node.getChildNodes().getLength(); ++pos) {
                Node node2 = node.getChildNodes().item(pos);
                if (node2.getNodeType() == Node.ELEMENT_NODE) {
                    readNode(node2, s, tmp);
                }
            }
            list.add(tmp);
            break;
        case BerType.CONSTRUCTED | BerType.SEQUENCE:
            tmp = new GXAsn1Sequence();
            for (int pos = 0; pos != node.getChildNodes().getLength(); ++pos) {
                Node node2 = node.getChildNodes().item(pos);
                if (node2.getNodeType() == Node.ELEMENT_NODE) {
                    readNode(node2, s, tmp);
                }
            }
            list.add(tmp);
            break;
        case BerType.CONSTRUCTED | BerType.SET:
            tmp = new ArrayList<Object>();
            for (int pos = 0; pos != node.getChildNodes().getLength(); ++pos) {
                Node node2 = node.getChildNodes().item(pos);
                if (node2.getNodeType() == Node.ELEMENT_NODE) {
                    readNode(node2, s, tmp);
                }
            }
            for (Object val : tmp) {
                GXSimpleEntry<Object, Object> e;
                if (val instanceof List) {
                    List t = (List) val;
                    e = new GXSimpleEntry<Object, Object>(t.get(0), t.get(1));
                } else {
                    e = new GXSimpleEntry<Object, Object>(tmp, null);
                }
                list.add(e);
            }
            break;
        case BerType.OBJECT_IDENTIFIER:
            list.add(new GXAsn1ObjectIdentifier(
                    node.getChildNodes().item(0).getNodeValue()));
            break;
        case BerType.PRINTABLE_STRING:
            list.add(node.getChildNodes().item(0).getNodeValue());
            break;
        case BerType.UTF8STRING:
            list.add(new GXAsn1Utf8String(
                    node.getChildNodes().item(0).getNodeValue()));
            break;
        case BerType.IA5_STRING:
            list.add(new GXAsn1Ia5String(
                    node.getChildNodes().item(0).getNodeValue()));
            break;
        case BerType.INTEGER:
            list.add(new GXAsn1Integer(
                    node.getChildNodes().item(0).getNodeValue()));
            break;
        case BerType.NULL:
            list.add(null);
            break;
        case BerType.BIT_STRING:
            list.add(new GXAsn1BitString(
                    node.getChildNodes().item(0).getNodeValue()));
            break;
        case BerType.UTC_TIME:
            try {
                DateFormat f = new SimpleDateFormat();
                Date d = f.parse(node.getChildNodes().item(0).getNodeValue());
                list.add(d);
            } catch (DOMException | ParseException e) {
                throw new RuntimeException(e.getMessage());
            }

            break;
        case BerType.GENERALIZED_TIME:
            break;
        case BerType.OCTET_STRING:
            break;
        case -1:
            list.add(Byte
                    .parseByte(node.getChildNodes().item(0).getNodeValue()));
            break;
        case -2:
            list.add(Short
                    .parseShort(node.getChildNodes().item(0).getNodeValue()));
            break;
        case -4:
            list.add(Integer
                    .parseInt(node.getChildNodes().item(0).getNodeValue()));
            break;
        case -8:
            list.add(Long
                    .parseLong(node.getChildNodes().item(0).getNodeValue()));
            break;
        default:
            throw new IllegalArgumentException(
                    "Invalid node: " + node.getNodeName());
        }
        return 0;
    }

    /**
     * Convert XML to ASN.1 PDU bytes.
     * 
     * @param xml
     *            XML.
     * @return ASN.1 PDU.
     */
    public static byte[] xmlToPdu(final String xml) {
        DocumentBuilder docBuilder;
        Document doc;
        DocumentBuilderFactory docBuilderFactory =
                DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        List<Object> list = new ArrayList<Object>();
        GXAsn1Settings s = new GXAsn1Settings();
        readNode(doc.getDocumentElement(), s, list);
        return toByteArray(list.get(0));
    }
}