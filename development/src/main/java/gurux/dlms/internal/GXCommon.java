//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.internal;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import gurux.dlms.GXArray;
import gurux.dlms.GXBitString;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXCryptoKeyParameter;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslatorStructure;
import gurux.dlms.GXDate;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXDeltaInt16;
import gurux.dlms.GXDeltaInt32;
import gurux.dlms.GXDeltaInt8;
import gurux.dlms.GXDeltaUInt16;
import gurux.dlms.GXDeltaUInt32;
import gurux.dlms.GXDeltaUInt8;
import gurux.dlms.GXEnum;
import gurux.dlms.GXStructure;
import gurux.dlms.GXTime;
import gurux.dlms.GXUInt16;
import gurux.dlms.GXUInt32;
import gurux.dlms.GXUInt64;
import gurux.dlms.GXUInt8;
import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.ConnectionState;
import gurux.dlms.enums.CryptoKeyType;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeExtraInfo;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.Standard;
import gurux.dlms.enums.TranslatorOutputType;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecuritySuite;

/*
 * <b> This class is for internal use only and is subject to changes or removal
 * in future versions of the API. Don't use it. </b>
 */
public final class GXCommon {
    private static String zeroes = "00000000000000000000000000000000";
    private static char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static final char[] BASE_64_ARRAY = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/', '=' };

    /*
     * Constructor.
     */
    private GXCommon() {

    }

    @SuppressWarnings("squid:S2386")
    public static final byte[] LLC_SEND_BYTES = { (byte) 0xE6, (byte) 0xE6, 0x00 };
    @SuppressWarnings("squid:S2386")
    public static final byte[] LLC_REPLY_BYTES = { (byte) 0xE6, (byte) 0xE7, 0x00 };

    public static String toCamelCase(final String value) {
        String name = value.toLowerCase();
        StringBuilder camelCaseName = new StringBuilder();
        boolean nextUpper = true;
        for (char c : name.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else if (nextUpper) {
                camelCaseName.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                camelCaseName.append(c);
            }
        }
        return camelCaseName.toString();
    }

    /**
     * Check is string null or empty.
     * 
     * @param value
     *            String value.
     * @return True, if string is null or empty.
     */
    public static boolean isNullOrEmpty(final String value) {
        return value == null || value.isEmpty();
    }

    /*
     * Convert string to byte array.
     * @param value String value.
     * @return String as bytes.
     */
    public static byte[] getBytes(final String value) {
        if (value == null) {
            return new byte[0];
        }
        return value.getBytes(StandardCharsets.US_ASCII);
    }

    /*
     * Convert char hex value to byte value.
     * @param c Character to convert hex.
     * @return Byte value of hex char value.
     */
    private static byte getValue(final byte c) {
        byte value = -1;
        // If number
        if (c > 0x2F && c < 0x3A) {
            value = (byte) (c - '0');
        } else if (c > 0x40 && c < 'G') {
            // If upper case.
            value = (byte) (c - 'A' + 10);
        } else if (c > 0x60 && c < 'g') {
            // If lower case.
            value = (byte) (c - 'a' + 10);
        }
        return value;
    }

    private static boolean isHex(final byte c) {
        return getValue(c) != -1;
    }

    /**
     * Convert string to byte array.
     * 
     * @param value
     *            Hex string.
     * @return byte array.
     */
    @SuppressWarnings("squid:S3034")
    public static byte[] hexToBytes(final String value) {
        if (value == null || value.length() == 0) {
            return new byte[0];
        }
        int len = value.length() / 2;
        if (value.length() % 2 != 0) {
            ++len;
        }

        byte[] buffer = new byte[len];
        int lastValue = -1;
        int index = 0;
        for (byte ch : value.getBytes()) {
            if (isHex(ch)) {
                if (lastValue == -1) {
                    lastValue = getValue(ch);
                } else if (lastValue != -1) {
                    buffer[index] = (byte) (lastValue << 4 | getValue(ch));
                    lastValue = -1;
                    ++index;
                }
            } else if (lastValue != -1 && ch == ' ') {
                buffer[index] = getValue(ch);
                lastValue = -1;
                ++index;
            } else {
                lastValue = -1;
            }
        }
        if (lastValue != -1) {
            buffer[index] = (byte) lastValue;
            ++index;
        }
        // If there are no spaces in the hex string.
        if (buffer.length == index) {
            return buffer;
        }
        byte[] tmp = new byte[index];
        System.arraycopy(buffer, 0, tmp, 0, index);
        return tmp;
    }

    /*
     * Convert byte array to hex string.
     */
    public static String toHex(final byte[] bytes) {
        return toHex(bytes, true);
    }

    /*
     * Convert byte array to hex string.
     */
    public static String toHex(final byte[] bytes, final boolean addSpace) {
        if (bytes == null) {
            return "";
        }
        return toHex(bytes, addSpace, 0, bytes.length);
    }

    /*
     * Convert byte array to hex string.
     */
    public static String toHex(final byte[] bytes, final boolean addSpace, final int index, final int count) {
        if (bytes == null || bytes.length == 0 || count == 0) {
            return "";
        }
        char[] str;
        if (addSpace) {
            str = new char[count * 3];
        } else {
            str = new char[count * 2];
        }
        int tmp;
        int len = 0;
        for (int pos = 0; pos != count; ++pos) {
            tmp = bytes[index + pos] & 0xFF;
            str[len] = hexArray[tmp >>> 4];
            ++len;
            str[len] = hexArray[tmp & 0x0F];
            ++len;
            if (addSpace) {
                str[len] = ' ';
                ++len;
            }
        }
        if (addSpace) {
            --len;
        }
        return new String(str, 0, len);
    }

    /**
     * Is string hex string.
     * 
     * @param value
     *            String value.
     * @return Return true, if string is hex string.
     */
    public static boolean isHexString(final String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        char ch;
        for (int pos = 0; pos != value.length(); ++pos) {
            ch = value.charAt(pos);
            if (ch != ' ' && !((ch > 0x40 && ch < 'G') || (ch > 0x60 && ch < 'g') || (ch > '/' && ch < ':'))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert byte array to Base64 string.
     * 
     * @param value
     *            Byte array to convert.
     * @return Base64 string.
     */
    public static String toBase64(final byte[] value) {
        StringBuilder str = new StringBuilder((value.length * 4) / 3);
        int b;
        for (int pos = 0; pos < value.length; pos += 3) {
            b = (value[pos] & 0xFC) >> 2;
            str.append(BASE_64_ARRAY[b]);
            b = (value[pos] & 0x03) << 4;
            if (pos + 1 < value.length) {
                b |= (value[pos + 1] & 0xF0) >> 4;
                str.append(BASE_64_ARRAY[b]);
                b = (value[pos + 1] & 0x0F) << 2;
                if (pos + 2 < value.length) {
                    b |= (value[pos + 2] & 0xC0) >> 6;
                    str.append(BASE_64_ARRAY[b]);
                    b = value[pos + 2] & 0x3F;
                    str.append(BASE_64_ARRAY[b]);
                } else {
                    str.append(BASE_64_ARRAY[b]);
                    str.append('=');
                }
            } else {
                str.append(BASE_64_ARRAY[b]);
                str.append("==");
            }
        }

        return str.toString();
    }

    /**
     * Get index of given char.
     * 
     * @param ch
     * @return
     */
    private static int getIndex(final char ch) {
        if (ch == '+') {
            return 62;
        }
        if (ch == '/') {
            return 63;
        }
        if (ch == '=') {
            return 64;
        }
        if (ch < ':') {
            return (52 + (ch - '0'));
        }
        if (ch < '[') {
            return (ch - 'A');
        }
        if (ch < '{') {
            return (26 + (ch - 'a'));
        }
        throw new IllegalArgumentException("fromBase64");
    }

    /**
     * Convert Base64 string to byte array.
     * 
     * @param input
     *            Base64 string.
     * @return Converted byte array.
     */
    public static byte[] fromBase64(final String input) {
        String tmp = input.replace("\r\n", "").replace("\n", "");
        if (tmp.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        int len = (tmp.length() * 3) / 4;
        int pos = tmp.indexOf('=');
        if (pos > 0) {
            len -= tmp.length() - pos;
        }
        byte[] decoded = new byte[len];
        char[] inChars = new char[4];
        int j = 0;
        int[] b = new int[4];
        for (pos = 0; pos != tmp.length(); pos += 4) {
            tmp.getChars(pos, pos + 4, inChars, 0);
            b[0] = getIndex(inChars[0]);
            b[1] = getIndex(inChars[1]);
            b[2] = getIndex(inChars[2]);
            b[3] = getIndex(inChars[3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }
        return decoded;
    }

    public static int intValue(final Object value) {
        if (value instanceof Byte) {
            return ((Byte) value).intValue() & 0xFF;
        }
        if (value instanceof Short) {
            return ((Short) value).intValue() & 0xFFFF;
        }
        return ((Number) value).intValue();
    }

    /**
     * Insert item count to the begin of the buffer.
     * 
     * @param count
     *            Count.
     * @param buff
     *            Buffer.
     * @param index
     *            byte index.
     */
    public static void insertObjectCount(int count, GXByteBuffer buff, int index) {
        if (count < 0x80) {
            buff.move(index, index + 1, buff.size());
            buff.size(buff.size() - index);
            buff.setUInt8(index, (byte) count);
        } else if (count < 0x100) {
            buff.move(index, index + 2, buff.size());
            buff.size(buff.size() - index);
            buff.setUInt8(index, 0x81);
            buff.setUInt8(index + 1, (byte) count);
        } else if (count < 0x10000) {
            buff.move(index, index + 4, buff.size());
            buff.size(buff.size() - index);
            buff.setUInt8(index, 0x82);
            buff.setUInt16(index + 1, count);
        } else {
            buff.move(index, index + 5, buff.size());
            buff.size(buff.size() - index);
            buff.setUInt8(index, 0x84);
            buff.setUInt32(index + 1, count);
        }
    }

    /*
     * Get object count. If first byte is 0x80 or higger it will tell bytes
     * count.
     * @param data received data.
     * @return Object count.
     */
    public static int getObjectCount(final GXByteBuffer data) {
        int cnt = data.getUInt8();
        if (cnt > 0x80) {
            if (cnt == 0x81) {
                return data.getUInt8();
            } else if (cnt == 0x82) {
                return data.getUInt16();
            } else if (cnt == 0x83) {
                return data.getUInt24();
            } else if (cnt == 0x84) {
                return (int) data.getUInt32();
            } else {
                throw new IllegalArgumentException("Invalid count.");
            }
        }
        return cnt;
    }

    /**
     * Return how many bytes object count takes.
     * 
     * @param count
     *            Value
     * @return Value size in bytes.
     */
    public static int getObjectCountSizeInBytes(final int count) {
        if (count < 0x80) {
            return 1;
        } else if (count < 0x100) {
            return 2;
        } else if (count < 0x10000) {
            return 3;
        } else {
            return 5;
        }
    }

    /**
     * Add string to byte buffer.
     * 
     * @param value
     *            String to add.
     * @param bb
     *            Byte buffer where string is added.
     */
    public static void addString(final String value, final GXByteBuffer bb) {
        bb.setUInt8((byte) DataType.OCTET_STRING.getValue());
        if (value == null) {
            setObjectCount(0, bb);
        } else {
            setObjectCount(value.length(), bb);
            bb.set(value.getBytes());
        }
    }

    /*
     * Set item count.
     * @param count
     * @param buff
     */
    public static byte setObjectCount(final int count, final GXByteBuffer buff) {
        if (count < 0x80) {
            buff.setUInt8(count);
            return 1;
        } else if (count < 0x100) {
            buff.setUInt8(0x81);
            buff.setUInt8(count);
            return 2;
        } else if (count < 0x10000) {
            buff.setUInt8(0x82);
            buff.setUInt16(count);
            return 3;
        } else {
            buff.setUInt8(0x84);
            buff.setUInt32(count);
            return 5;
        }
    }

    /*
     * Compares, whether two given arrays are similar.
     * @param arr1 First array to compare.
     * @param index Starting index of table, for first array.
     * @param arr2 Second array to compare.
     * @return True, if arrays are similar. False, if the arrays differ.
     */
    public static boolean compare(final byte[] arr1, final byte[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }
        int pos;
        for (pos = 0; pos != arr2.length; ++pos) {
            if (arr1[pos] != arr2[pos]) {
                return false;
            }
        }
        return true;
    }

    /*
     * Reserved for internal use.
     */
    public static void toBitString(final StringBuilder sb, final byte value, final int count) {
        int count2 = count;
        if (count2 > 0) {
            if (count2 > 8) {
                count2 = 8;
            }
            for (int pos = 7; pos != 8 - count2 - 1; --pos) {
                if ((value & (1 << pos)) != 0) {
                    sb.append('1');
                } else {
                    sb.append('0');
                }
            }
        }
    }

    /**
     * Get data from DLMS frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param data
     *            received data.
     * @param info
     *            Data info.
     * @return Received data.
     */
    public static Object getData(final GXDLMSSettings settings, final GXByteBuffer data, final GXDataInfo info) {
        Object value = null;
        int startIndex = data.position();
        if (data.position() == data.size()) {
            info.setComplete(false);
            return null;
        }
        info.setComplete(true);
        boolean knownType = info.getType() != DataType.NONE;
        // Get data type if it is unknown.
        if (!knownType) {
            info.setType(DataType.forValue(data.getUInt8()));
        }
        if (info.getType() == DataType.NONE) {
            if (info.getXml() != null) {
                info.getXml().appendLine("<" + info.getXml().getDataType(info.getType()) + " />");
            }
            return value;
        }
        if (data.position() == data.size()) {
            info.setComplete(false);
            return null;
        }
        switch (info.getType()) {
        case ARRAY:
        case STRUCTURE:
            value = getArray(settings, data, info, startIndex);
            break;
        case BOOLEAN:
            value = getBoolean(data, info);
            break;
        case BITSTRING:
            value = getBitString(data, info);
            break;
        case INT32:
            value = getInt32(data, info);
            break;
        case UINT32:
            value = new GXUInt32(getUInt32(data, info));
            break;
        case STRING:
            value = getString(data, info, knownType);
            break;
        case STRING_UTF8:
            value = getUtfString(data, info, knownType);
            break;
        case OCTET_STRING:
            value = getOctetString(settings, data, info, knownType);
            break;
        case BCD:
            value = getBcd(data, info);
            break;
        case INT8:
            value = getInt8(data, info);
            break;
        case INT16:
            value = getInt16(data, info);
            break;
        case UINT8:
            value = new GXUInt8(getUInt8(data, info));
            break;
        case UINT16:
            value = new GXUInt16(getUInt16(data, info));
            break;
        case COMPACT_ARRAY:
            value = getCompactArray(settings, data, info, false);
            break;
        case INT64:
            value = getInt64(data, info);
            break;
        case UINT64:
            value = new GXUInt64(getUInt64(settings, data, info));
            break;
        case ENUM:
            value = getEnum(data, info);
            break;
        case FLOAT32:
            value = getFloat(data, info);
            break;
        case FLOAT64:
            value = getDouble(data, info);
            break;
        case DATETIME:
            value = getDateTime(settings, data, info);
            break;
        case DATE:
            value = getDate(data, info);
            break;
        case TIME:
            value = getTime(data, info);
            break;
        case DELTA_INT8:
            value = new GXDeltaInt8(getInt8(data, info));
            break;
        case DELTA_INT16:
            value = new GXDeltaInt16(getInt16(data, info));
            break;
        case DELTA_INT32:
            value = new GXDeltaInt32(getInt32(data, info));
            break;
        case DELTA_UINT8:
            value = new GXDeltaUInt8(getUInt8(data, info));
            break;
        case DELTA_UINT16:
            value = new GXDeltaUInt16(getUInt16(data, info));
            break;
        case DELTA_UINT32:
            value = new GXDeltaUInt32(getUInt32(data, info));
            break;

        default:
            throw new IllegalArgumentException("Invalid data type.");
        }
        return value;
    }

    /*
     * Convert value to hex string.
     * @param value value to convert.
     * @param desimals Amount of decimals.
     * @return
     */
    public static String integerToHex(final Object value, final int desimals) {
        long tmp;
        if (value instanceof Byte) {
            tmp = ((Byte) value).byteValue() & 0xFF;
        } else if (value instanceof Short) {
            tmp = ((Short) value).shortValue() & 0xFFFF;
        } else if (value instanceof Long) {
            tmp = ((Long) value).longValue() & 0xFFFFFFFF;
        } else if (value instanceof GXUInt8) {
            tmp = ((GXUInt8) value).byteValue() & 0xFF;
        } else if (value instanceof GXUInt16) {
            tmp = ((GXUInt16) value).shortValue() & 0xFFFF;
        } else if (value instanceof GXUInt32) {
            tmp = ((GXUInt32) value).longValue() & 0xFFFFFFFF;
        } else {
            tmp = ((Number) value).longValue();
        }
        String str = Long.toString(tmp, 16).toUpperCase();
        if (desimals == 0 || str.length() >= desimals || str.length() == zeroes.length()) {
            return str;
        }
        return zeroes.substring(0, desimals - str.length()) + str;
    }

    /*
     * Convert value to hex string.
     * @param value value to convert.
     * @param desimals Amount of decimals.
     * @return
     */
    public static String integerString(final Object value, final int desimals) {
        String str = Long.toString(((Number) value).longValue());
        if (desimals == 0 || str.length() == zeroes.length()) {
            return str;
        }
        return zeroes.substring(0, desimals - str.length()) + str;
    }

    /**
     * Get array from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @param index
     *            starting index.
     * @return Object array.
     */
    private static Object getArray(final GXDLMSSettings settings, final GXByteBuffer buff, final GXDataInfo info,
            final int index) {
        if (info.getCount() == 0) {
            info.setCount(GXCommon.getObjectCount(buff));
        }
        if (info.getXml() != null) {
            info.getXml().appendStartTag(info.getXml().getDataType(info.getType()), "Qty",
                    info.getXml().integerToHex(info.getCount(), 2));
        }
        int size = buff.size() - buff.position();
        if (info.getCount() != 0 && size < 1) {
            info.setComplete(false);
            return null;
        }
        int startIndex = index;
        java.util.ArrayList<Object> arr;
        if (info.getType() == DataType.ARRAY) {
            arr = new GXArray();
        } else {
            arr = new GXStructure();
        }
        // Position where last row was found. Cache uses this info.
        int pos = info.getIndex();
        for (; pos != info.getCount(); ++pos) {
            GXDataInfo info2 = new GXDataInfo();
            info2.setXml(info.getXml());
            Object tmp = getData(settings, buff, info2);
            if (!info2.isComplete()) {
                buff.position(startIndex);
                info.setComplete(false);
                break;
            } else {
                if (info2.getCount() == info2.getIndex()) {
                    startIndex = buff.position();
                    arr.add(tmp);
                }
            }
        }
        if (info.getXml() != null) {
            info.getXml().appendEndTag(info.getXml().getDataType(info.getType()));
        }
        info.setIndex(pos);
        return arr;
    }

    /**
     * Get time from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return Parsed time.
     */
    private static Object getTime(final GXByteBuffer buff, final GXDataInfo info) {
        Object value = null;
        if (buff.size() - buff.position() < 4) {
            // If there is not enough data available.
            info.setComplete(false);
            return null;
        }
        String str = null;
        if (info.getXml() != null) {
            str = GXCommon.toHex(buff.getData(), false, buff.position(), 4);
        }
        try {
            // Get time.
            int hour = buff.getUInt8();
            int minute = buff.getUInt8();
            int second = buff.getUInt8();
            int ms = buff.getUInt8();
            if (ms != 0xFF) {
                ms *= 10;
            } else {
                ms = -1;
            }
            GXTime dt = new GXTime(hour, minute, second, ms);
            value = dt;
        } catch (Exception ex) {
            if (info.getXml() == null) {
                throw ex;
            }
        }
        if (info.getXml() != null) {
            if (value != null) {
                info.getXml().appendComment(String.valueOf(value));
            }
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, str);
        }
        return value;
    }

    /**
     * Get date from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return Parsed date.
     */
    private static Object getDate(final GXByteBuffer buff, final GXDataInfo info) {
        Object value = null;
        if (buff.size() - buff.position() < 5) {
            // If there is not enough data available.
            info.setComplete(false);
            return null;
        }
        String str = null;
        if (info.getXml() != null) {
            str = GXCommon.toHex(buff.getData(), false, buff.position(), 5);
        }
        try {
            // Get year.
            int year = buff.getUInt16();
            // Get month
            int month = buff.getUInt8();

            java.util.Set<DateTimeExtraInfo> extra = new HashSet<DateTimeExtraInfo>();
            java.util.Set<DateTimeSkips> skip = new HashSet<DateTimeSkips>();
            if (month == 0 || month == 0xFF) {
                month = 1;
                skip.add(DateTimeSkips.MONTH);
            } else if (month == 0xFE) {
                // Daylight savings begin.
                month = 1;
                extra.add(DateTimeExtraInfo.DST_BEGIN);
            } else if (month == 0xFD) {
                // Daylight savings end.
                month = 1;
                extra.add(DateTimeExtraInfo.DST_END);
            }
            // Get day
            int day = buff.getUInt8();
            if (day == 0xFD) {
                // 2nd last day of month.
                day = 1;
                extra.add(DateTimeExtraInfo.LAST_DAY2);
            } else if (day == 0xFE) {
                // Last day of month
                day = 1;
                extra.add(DateTimeExtraInfo.LAST_DAY);
            } else if (day < 1 || day == 0xFF) {
                day = 1;
                skip.add(DateTimeSkips.DAY);
            }

            GXDate dt = new GXDate(year, month, day);
            value = dt;
            dt.setExtra(extra);
            dt.getSkip().addAll(skip);
            // Skip week day
            if (buff.getUInt8() == 0xFF) {
                dt.getSkip().add(DateTimeSkips.DAY_OF_WEEK);
            }
        } catch (Exception ex) {
            if (info.getXml() == null) {
                throw ex;
            }
        }
        if (info.getXml() != null) {
            if (value != null) {
                info.getXml().appendComment(String.valueOf(value));
            }
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, str);
        }
        return value;
    }

    /**
     * Get the number of days in that month.
     * 
     * @param year
     *            Year.
     * @param month
     *            Month.
     * @return Number of days in month.
     */
    public static int daysInMonth(final int year, final int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get date and time from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return Parsed date and time.
     */
    private static Object getDateTime(final GXDLMSSettings settings, final GXByteBuffer buff, final GXDataInfo info) {
        Object value = null;
        java.util.Set<DateTimeSkips> skip = new HashSet<DateTimeSkips>();

        // If there is not enough data available.
        if (buff.size() - buff.position() < 12) {
            info.setComplete(false);
            return null;
        }
        String str = null;
        if (info.getXml() != null) {
            str = GXCommon.toHex(buff.getData(), false, buff.position(), 12);
        }
        GXDateTime dt = new GXDateTime();
        try {
            // Get year.
            int year = buff.getUInt16();
            // Get month
            int month = buff.getUInt8();
            if (month == 0 || month == 0xFF) {
                month = 1;
                skip.add(DateTimeSkips.MONTH);
            } else if (month == 0xFE) {
                // Daylight savings begin.
                month = 1;
                dt.getExtra().add(DateTimeExtraInfo.DST_BEGIN);
            } else if (month == 0xFD) {
                // Daylight savings end.
                month = 1;
                dt.getExtra().add(DateTimeExtraInfo.DST_END);
            }
            // Get day
            int day = buff.getUInt8();
            if (day == 0xFD) {
                // 2nd last day of month.
                day = 1;
                dt.getExtra().add(DateTimeExtraInfo.LAST_DAY2);
            } else if (day == 0xFE) {
                // Last day of month
                day = 1;
                dt.getExtra().add(DateTimeExtraInfo.LAST_DAY);
            } else if (day < 1 || day == 0xFF) {
                day = 1;
                skip.add(DateTimeSkips.DAY);
            }
            // Skip week day
            int dayOfWeek = buff.getUInt8();
            if (dayOfWeek == 0xFF) {
                skip.add(DateTimeSkips.DAY_OF_WEEK);
            } else {
                dt.setDayOfWeek(dayOfWeek);
            }
            // Get time.
            int hour = buff.getUInt8();
            int minute = buff.getUInt8();
            int second = buff.getUInt8();
            int ms = buff.getUInt8() & 0xFF;
            if (ms != 0xFF) {
                ms *= 10;
            } else {
                ms = -1;
            }
            int deviation = buff.getInt16();
            if (deviation == -32768) {
                skip.add(DateTimeSkips.DEVIATION);
            }
            int status = buff.getUInt8();
            dt.setStatus(ClockStatus.forValue(status));
            if (year < 1 || year == 0xFFFF) {
                skip.add(DateTimeSkips.YEAR);
                java.util.Calendar tm = java.util.Calendar.getInstance();
                year = tm.get(Calendar.YEAR);
            }
            if (month != 0xFE && month != 0xFD) {
                if (month < 1 || month > 12) {
                    skip.add(DateTimeSkips.MONTH);
                    month = 0;
                } else {
                    month -= 1;
                }
            }
            if (day != 0xFE && day != 0xFD) {
                if (day == -1 || day == 0 || day > 31) {
                    skip.add(DateTimeSkips.DAY);
                    day = 1;
                }
            }
            if (hour < 0 || hour > 24) {
                skip.add(DateTimeSkips.HOUR);
                hour = 0;
            }
            if (minute < 0 || minute > 60) {
                skip.add(DateTimeSkips.MINUTE);
                minute = 0;
            }
            if (second < 0 || second > 60) {
                skip.add(DateTimeSkips.SECOND);
                second = 0;
            }
            // If ms is Zero it's skipped.
            if (ms < 0 || ms > 1000) {
                skip.add(DateTimeSkips.MILLISECOND);
                ms = 0;
            }
            if (settings != null && settings.getUseUtc2NormalTime() && deviation != -32768) {
                deviation = -deviation;
            }
            java.util.Calendar tm;
            boolean ignoreDeviation = deviation == -32768;
            if (!ignoreDeviation && settings != null
                    && settings.getDateTimeSkipsOnRead().contains(DateTimeSkips.DEVIATION)) {
                ignoreDeviation = true;
            }
            if (ignoreDeviation) {
                tm = Calendar.getInstance();
            } else {
                TimeZone tz =
                        GXDateTime.getTimeZone(-deviation, dt.getStatus().contains(ClockStatus.DAYLIGHT_SAVE_ACTIVE));
                tm = Calendar.getInstance(tz);
            }
            tm.set(year, month, day, hour, minute, second);
            if (skip.contains(DateTimeSkips.MILLISECOND)) {
                tm.set(Calendar.MILLISECOND, 0);
            } else {
                tm.set(Calendar.MILLISECOND, ms);
            }
            dt.setMeterCalendar(tm);
            dt.getSkip().addAll(skip);
            value = dt;
        } catch (Exception ex) {
            if (info.getXml() == null) {
                throw ex;
            }
        }
        if (info.getXml() != null) {
            if (!dt.getSkip().contains(DateTimeSkips.YEAR) && value != null) {
                info.getXml().appendComment(String.valueOf(value));
            }
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, str);
        }
        return value;
    }

    /**
     * Get double value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return Parsed double value.
     */
    private static Object getDouble(final GXByteBuffer buff, final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setComplete(false);
            return null;
        }
        value = buff.getDouble();
        if (info.getXml() != null) {
            if (info.getXml().isComments()) {
                info.getXml().appendComment(String.format("%.2f", value));
            }

            GXByteBuffer tmp = new GXByteBuffer();
            setData(null, tmp, DataType.FLOAT64, value);
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    GXCommon.toHex(tmp.getData(), false, 1, tmp.size() - 1));
        }
        return value;
    }

    /**
     * Get float value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return Parsed float value.
     */
    private static Object getFloat(final GXByteBuffer buff, final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setComplete(false);
            return null;
        }
        value = buff.getFloat();
        if (info.getXml() != null) {

            if (info.getXml().isComments()) {
                info.getXml().appendComment(String.format("%.2f", value));
            }

            GXByteBuffer tmp = new GXByteBuffer();
            setData(null, tmp, DataType.FLOAT32, value);
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    GXCommon.toHex(tmp.getData(), false, 1, tmp.size() - 1));
        }
        return value;
    }

    /**
     * Get enumeration value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed enumeration value.
     */
    private static Object getEnum(final GXByteBuffer buff, final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setComplete(false);
            return null;
        }
        value = buff.getUInt8();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 2));
        }
        return new GXEnum((short) value);
    }

    /**
     * Get UInt64 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed UInt64 value.
     */
    private static BigInteger getUInt64(final GXDLMSSettings settings, final GXByteBuffer buff, final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setComplete(false);
            return null;
        }
        BigInteger value = buff.getUInt64();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 16));
        }
        return value;
    }

    /**
     * Get Int64 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed Int64 value.
     */
    private static Object getInt64(final GXByteBuffer buff, final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setComplete(false);
            return null;
        }
        value = buff.getInt64();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 16));
        }
        return value;
    }

    /**
     * Get UInt16 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed UInt16 value.
     */
    private static int getUInt16(final GXByteBuffer buff, final GXDataInfo info) {
        int value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 2) {
            info.setComplete(false);
            return 0;
        }
        value = buff.getUInt16();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 4));
        }
        return value;
    }

    private static void getCompactArrayItem(final GXDLMSSettings settings, final GXByteBuffer buff, final List<?> dt,
            final List<Object> list, final int len, final boolean array) {
        List<Object> tmp;
        if (array) {
            tmp = new GXArray();
        } else {
            tmp = new GXStructure();
        }
        for (Object it : dt) {
            if (it instanceof DataType) {
                getCompactArrayItem(settings, buff, (DataType) it, tmp, 1);
            } else {
                getCompactArrayItem(settings, buff, (List<?>) it, tmp, 1, it instanceof GXArray);
            }
        }
        list.add(tmp);
    }

    private static void getCompactArrayItem(final GXDLMSSettings settings, final GXByteBuffer buff, final DataType dt,
            final List<Object> list, final int len) {
        GXDataInfo tmp = new GXDataInfo();
        tmp.setType(dt);
        int start = buff.position();
        if (dt == DataType.STRING) {
            while (buff.position() - start < len) {
                tmp.clear();
                tmp.setType(dt);
                list.add(getString(buff, tmp, false));
                if (!tmp.isComplete()) {
                    break;
                }
            }
        } else if (dt == DataType.OCTET_STRING) {
            while (buff.position() - start < len) {
                tmp.clear();
                tmp.setType(dt);
                list.add(getOctetString(settings, buff, tmp, false));
                if (!tmp.isComplete()) {
                    break;
                }
            }
        } else {
            while (buff.position() - start < len) {
                tmp.clear();
                tmp.setType(dt);
                list.add(getData(settings, buff, tmp));
                if (!tmp.isComplete()) {
                    break;
                }
            }
        }

    }

    //
    static final int CONTENTS_DESCRIPTION = 0xFF68;
    static final int ARRAY_CONTENTS = 0xFF69;
    static final int DATA_TYPE_OFFSET = 0xFF0000;

    private static void getDataTypes(GXByteBuffer buff, List<Object> cols, int len) {
        DataType dt;
        for (int pos = 0; pos != len; ++pos) {
            dt = DataType.forValue(buff.getUInt8());
            if (dt == DataType.ARRAY) {
                int cnt = buff.getUInt16();
                List<Object> tmp = new ArrayList<Object>();
                GXArray tmp2 = new GXArray();
                if (cnt != 0) {
                    getDataTypes(buff, tmp, 1);
                    for (int i = 0; i != cnt; ++i) {
                        tmp2.add(tmp.get(0));
                    }
                }
                cols.add(tmp2);
            } else if (dt == DataType.STRUCTURE) {
                GXStructure tmp = new GXStructure();
                getDataTypes(buff, tmp, buff.getUInt8());
                cols.add(tmp);
            } else {
                cols.add(dt);
            }
        }
    }

    private static void appendDataTypeAsXml(List<?> cols, GXDataInfo info) {
        for (Object it : cols) {
            if (it instanceof DataType) {
                info.getXml().appendEmptyTag(info.getXml().getDataType((DataType) it));
            } else if (it instanceof GXStructure) {
                info.getXml().appendStartTag(DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue(), null, null);
                appendDataTypeAsXml((List<?>) it, info);
                info.getXml().appendEndTag(DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue());
            } else if (it instanceof GXArray) {
                info.getXml().appendStartTag(DATA_TYPE_OFFSET + DataType.ARRAY.getValue(), null, null);
                appendDataTypeAsXml(((List<?>) it), info);
                info.getXml().appendEndTag(DATA_TYPE_OFFSET + DataType.ARRAY.getValue());
            }
        }

    }

    /**
     * Get compact array value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed UInt16 value.
     */
    public static Object getCompactArray(final GXDLMSSettings settings, final GXByteBuffer buff, final GXDataInfo info,
            boolean onlyDataTypes) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 2) {
            info.setComplete(false);
            return null;
        }
        DataType dt = DataType.forValue(buff.getUInt8());
        if (dt == DataType.ARRAY) {
            throw new IllegalArgumentException("Invalid compact array data.");
        }
        int len = GXCommon.getObjectCount(buff);
        List<Object> list;
        if (dt == DataType.STRUCTURE) {
            list = new GXStructure();
        } else {
            list = new GXArray();
        }
        if (dt == DataType.STRUCTURE) {
            // Get data types.
            GXStructure cols = new GXStructure();
            getDataTypes(buff, cols, len);
            if (onlyDataTypes) {
                return cols;
            }
            len = GXCommon.getObjectCount(buff);
            if (info.getXml() != null) {
                info.getXml().appendStartTag(info.getXml().getDataType(DataType.COMPACT_ARRAY), null, null);
                info.getXml().appendStartTag(CONTENTS_DESCRIPTION);
                appendDataTypeAsXml(cols, info);
                info.getXml().appendEndTag(CONTENTS_DESCRIPTION);
                if (info.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    info.getXml().appendStartTag(ARRAY_CONTENTS, true);
                    info.getXml().append(buff.remainingHexString(true));
                    info.getXml().appendEndTag(ARRAY_CONTENTS, true);
                    info.getXml().appendEndTag(info.getXml().getDataType(DataType.COMPACT_ARRAY));
                } else {
                    info.getXml().appendStartTag(ARRAY_CONTENTS);
                }
            }
            int start = buff.position();
            while (buff.position() - start < len) {
                GXStructure row = new GXStructure();
                for (int pos = 0; pos != cols.size(); ++pos) {
                    if (cols.get(pos) instanceof GXStructure) {
                        getCompactArrayItem(settings, buff, (List<?>) cols.get(pos), row, 1, false);
                    } else if (cols.get(pos) instanceof GXArray) {
                        // For some reason there is count here in Italy
                        // standard. Remove it.
                        if (info.isAppendAA()) {
                            GXCommon.getObjectCount(buff);
                        }
                        getCompactArrayItem(settings, buff, ((List<?>) cols.get(pos)), row, 1, true);
                    } else {
                        getCompactArrayItem(settings, buff, (DataType) cols.get(pos), row, 1);
                    }
                    if (buff.position() == buff.size()) {
                        break;
                    }
                }
                // If all columns are read.
                if (row.size() >= cols.size()) {
                    list.add(row);
                } else {
                    break;
                }
            }
            if (info.getXml() != null && info.getXml().getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                StringBuilder sb = new StringBuilder();
                for (Object row : list) {
                    for (Object it : (List<?>) row) {
                        if (it instanceof byte[]) {
                            sb.append(GXCommon.toHex((byte[]) it));
                        } else if (it instanceof List<?>) {
                            for (Object it2 : (List<?>) it) {
                                if (it2 instanceof byte[]) {
                                    sb.append(GXCommon.toHex((byte[]) it2));
                                } else {
                                    sb.append(String.valueOf(it2));
                                }
                                sb.append(";");
                            }
                            if (!((List<?>) it).isEmpty()) {
                                sb.setLength(sb.length() - 1);
                            }
                        } else {
                            sb.append(String.valueOf(it));
                        }
                        sb.append(";");
                    }
                    if (sb.length() != 0) {
                        sb.setLength(sb.length() - 1);
                    }
                    info.getXml().appendLine(sb.toString());
                    sb.setLength(0);
                }
            }
            if (info.getXml() != null && info.getXml().getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                info.getXml().appendEndTag(ARRAY_CONTENTS);
                info.getXml().appendEndTag(info.getXml().getDataType(DataType.COMPACT_ARRAY));
            }
            return list;
        } else {
            if (info.getXml() != null) {
                info.getXml().appendStartTag(info.getXml().getDataType(DataType.COMPACT_ARRAY), null, null);
                info.getXml().appendStartTag(CONTENTS_DESCRIPTION);
                info.getXml().appendEmptyTag(info.getXml().getDataType(dt));
                info.getXml().appendEndTag(CONTENTS_DESCRIPTION);
                info.getXml().appendStartTag(ARRAY_CONTENTS, true);
                if (info.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    info.getXml().append(buff.remainingHexString(false));
                    info.getXml().appendEndTag(ARRAY_CONTENTS, true);
                    info.getXml().appendEndTag(info.getXml().getDataType(DataType.COMPACT_ARRAY));
                }
            }
            getCompactArrayItem(settings, buff, dt, list, len);
            if (info.getXml() != null && info.getXml().getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                for (Object it : list) {
                    if (it instanceof byte[]) {
                        info.getXml().append(GXCommon.toHex((byte[]) it));
                    } else {
                        info.getXml().append(String.valueOf(it));
                    }
                    info.getXml().append(";");
                }
                if (!list.isEmpty()) {
                    info.getXml().setXmlLength(info.getXml().getXmlLength() - 1);
                }
                info.getXml().appendEndTag(ARRAY_CONTENTS, true);
                info.getXml().appendEndTag(info.getXml().getDataType(DataType.COMPACT_ARRAY));
            }
        }
        return list;
    }

    /**
     * Get UInt8 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed UInt8 value.
     */
    private static short getUInt8(final GXByteBuffer buff, final GXDataInfo info) {
        int value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setComplete(false);
            return 0;
        }
        value = buff.getUInt8() & 0xFF;
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 2));
        }
        return (short) value;
    }

    /**
     * Get Int16 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed Int16 value.
     */
    private static short getInt16(final GXByteBuffer buff, final GXDataInfo info) {
        short value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 2) {
            info.setComplete(false);
            return 0;
        }
        value = buff.getInt16();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 4));
        }
        return value;
    }

    /**
     * Get Int8 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed Int8 value.
     */
    private static byte getInt8(final GXByteBuffer buff, final GXDataInfo info) {
        byte value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setComplete(false);
            return 0;
        }
        value = buff.getInt8();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 2));
        }
        return value;
    }

    /**
     * Get BCD value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed BCD value.
     */
    private static Object getBcd(final GXByteBuffer buff, final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setComplete(false);
            return null;
        }
        short value = buff.getUInt8();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 2));
        }
        return value;
    }

    /**
     * Get UTF string value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed UTF string value.
     */
    private static Object getUtfString(final GXByteBuffer buff, final GXDataInfo info, final boolean knownType) {
        Object value;
        int len;
        if (knownType) {
            len = buff.size();
        } else {
            len = GXCommon.getObjectCount(buff);
            // If there is not enough data available.
            if (buff.size() - buff.position() < len) {
                info.setComplete(false);
                return null;
            }
        }
        if (len > 0) {
            value = buff.getString(buff.position(), len, "UTF-8");
            buff.position(buff.position() + len);
        } else {
            value = "";
        }
        if (info.getXml() != null) {
            if (info.getXml().getShowStringAsHex()) {
                info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                        GXCommon.toHex(buff.getData(), false, buff.position() - len, len));
            } else {
                info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, value.toString());
            }
        }
        return value;
    }

    /**
     * Get octet string value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed octet string value.
     */
    private static Object getOctetString(final GXDLMSSettings settings, final GXByteBuffer buff, final GXDataInfo info,
            final boolean knownType) {
        Object value;
        int len;
        if (knownType) {
            len = buff.size();
        } else {
            len = GXCommon.getObjectCount(buff);
            // If there is not enough data available.
            if (buff.size() - buff.position() < len) {
                info.setComplete(false);
                return null;
            }
        }
        byte[] tmp = new byte[len];
        buff.get(tmp);
        value = tmp;
        if (info.getXml() != null) {
            if (info.getXml().isComments() && tmp.length != 0) {
                // This might be logical name.
                if (tmp.length == 6 && tmp[5] == -1) {
                    info.getXml().appendComment(toLogicalName(tmp));
                } else {
                    boolean isString = true;
                    // Try to move octet string to DateTime, Date or time.
                    if (tmp.length == 12 || tmp.length == 5 || tmp.length == 4) {
                        try {
                            DataType type;
                            if (tmp.length == 12) {
                                type = DataType.DATETIME;
                            } else if (tmp.length == 5) {
                                type = DataType.DATE;
                            } else {
                                type = DataType.TIME;
                            }
                            boolean useUtc;
                            if (settings != null) {
                                useUtc = settings.getUseUtc2NormalTime();
                            } else {
                                useUtc = false;
                            }
                            GXDateTime dt = (GXDateTime) GXDLMSClient.changeType(tmp, type, useUtc);
                            int year = dt.getMeterCalendar().get(Calendar.YEAR);
                            if (year > 1970 && year < 2100) {
                                info.getXml().appendComment(dt.toString());
                                isString = false;
                            }
                        } catch (Exception e) {
                            isString = true;
                        }
                    }
                    if (isString) {
                        for (byte it : tmp) {
                            if (it < 32 || it > 126) {
                                isString = false;
                                break;
                            }
                        }
                        if (isString) {
                            info.getXml().appendComment(new String(tmp));
                        }
                    }
                }
            }
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, GXCommon.toHex(tmp, false));
        }
        return value;
    }

    /**
     * Get string value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed string value.
     */
    private static String getString(final GXByteBuffer buff, final GXDataInfo info, final boolean knownType) {
        String value;
        int len;
        if (knownType) {
            len = buff.size();
        } else {
            len = GXCommon.getObjectCount(buff);
            // If there is not enough data available.
            if (buff.size() - buff.position() < len) {
                info.setComplete(false);
                return null;
            }
        }
        if (len > 0) {
            value = buff.getString(len);
        } else {
            value = "";
        }
        if (info.getXml() != null) {
            if (info.getXml().getShowStringAsHex()) {
                info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                        GXCommon.toHex(buff.getData(), false, buff.position() - len, len));
            } else {
                info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, value);
            }
        }
        return value;
    }

    /**
     * Get UInt32 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed UInt32 value.
     */
    private static long getUInt32(final GXByteBuffer buff, final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setComplete(false);
            return 0;
        }
        long value = buff.getUInt32();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 8));
        }
        return value;
    }

    /**
     * Get Int32 value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed Int32 value.
     */
    private static int getInt32(final GXByteBuffer buff, final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setComplete(false);
            return 0;
        }
        int value = buff.getInt32();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null,
                    info.getXml().integerToHex(value, 8));
        }
        return value;
    }

    /**
     * Get bit string value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed bit string value.
     */
    private static GXBitString getBitString(final GXByteBuffer buff, final GXDataInfo info) {
        int cnt = getObjectCount(buff);
        double t = cnt;
        t /= 8;
        if (cnt % 8 != 0) {
            ++t;
        }
        int byteCnt = (int) Math.floor(t);
        // If there is not enough data available.
        if (buff.size() - buff.position() < byteCnt) {
            info.setComplete(false);
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (cnt > 0) {
            toBitString(sb, buff.getInt8(), cnt);
            cnt -= 8;
        }
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, sb.toString());
        }
        return new GXBitString(sb.toString());
    }

    /**
     * Get boolean value from DLMS data.
     * 
     * @param buff
     *            Received DLMS data.
     * @param info
     *            Data info.
     * @return parsed boolean value.
     */
    private static Object getBoolean(final GXByteBuffer buff, final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setComplete(false);
            return null;
        }
        boolean value = buff.getUInt8() != 0;
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()), null, String.valueOf(value));
        }
        return value;
    }

    /**
     * Get HDLC address from byte array.
     * 
     * @param buff
     *            byte array.
     * @return HDLC address.
     */
    public static int getHDLCAddress(final GXByteBuffer buff) {
        int size = 0;
        for (int pos = buff.position(); pos != buff.size(); ++pos) {
            ++size;
            if ((buff.getUInt8(pos) & 0x1) == 1) {
                break;
            }
        }
        if (size == 1) {
            return (byte) ((buff.getUInt8() & 0xFE) >>> 1);
        } else if (size == 2) {
            size = buff.getUInt16();
            size = ((size & 0xFE) >>> 1) | ((size & 0xFE00) >>> 2);
            return size;
        } else if (size == 4) {
            long tmp = buff.getUInt32();
            tmp = ((tmp & 0xFE) >> 1) | ((tmp & 0xFE00) >> 2) | ((tmp & 0xFE0000) >> 3) | ((tmp & 0xFE000000) >> 4);
            return (int) tmp;
        }
        throw new IllegalArgumentException("Wrong size.");
    }

    /*
     * Convert object to DLMS bytes.
     * @param settings DLMS settings.
     * @param buff Byte buffer where data is write.
     * @param dataType Data type.
     * @param value Added Value.
     */
    public static void setData(final GXDLMSSettings settings, final GXByteBuffer buff, final DataType dataType,
            final Object value) {
        // If value is enum get integer value.
        if (value instanceof Enum) {
            throw new IllegalArgumentException("Value can't be enum. Give integer value.");
        }
        if ((dataType == DataType.ARRAY || dataType == DataType.STRUCTURE) && value instanceof byte[]) {
            // If byte array is added do not add type.
            buff.set((byte[]) value);
            return;
        } else {
            buff.setUInt8(dataType.getValue());
        }
        switch (dataType) {
        case NONE:
            break;
        case BOOLEAN:
            if (Boolean.parseBoolean(value.toString())) {
                buff.setUInt8(1);
            } else {
                buff.setUInt8(0);
            }
            break;
        case INT8:
        case UINT8:
        case ENUM:
            buff.setUInt8(((Number) value).byteValue());
            break;
        case INT16:
        case UINT16:
            buff.setUInt16(((Number) value).shortValue());
            break;
        case INT32:
        case UINT32:
            buff.setUInt32(((Number) value).intValue());
            break;
        case INT64:
        case UINT64:
            buff.setUInt64(((Number) value).longValue());
            break;
        case FLOAT32:
            buff.setFloat(((Number) value).floatValue());
            break;
        case FLOAT64:
            buff.setDouble(((Number) value).doubleValue());
            break;
        case BITSTRING:
            setBitString(buff, value, true);
            break;
        case STRING:
            setString(buff, value);
            break;
        case STRING_UTF8:
            setUtfString(buff, value);
            break;
        case OCTET_STRING:
            if (value instanceof GXDate) {
                // Add size
                buff.setUInt8(5);
                setDate(settings, buff, value);
            } else if (value instanceof GXTime) {
                // Add size
                buff.setUInt8(4);
                setTime(settings, buff, value);
            } else if (value instanceof GXDateTime || value instanceof java.util.Date
                    || value instanceof java.util.Calendar) {
                // Date an calendar are always written as date time.
                buff.setUInt8(12);
                setDateTime(settings, buff, value);
            } else {
                setOctetString(buff, value);
            }
            break;
        case ARRAY:
        case STRUCTURE:
            setArray(settings, buff, value);
            break;
        case BCD:
            setBcd(buff, value);
            break;
        case COMPACT_ARRAY:
            // Compact array is not work with java because we don't know data
            // types of each element. Java is not support unsigned values.
            throw new IllegalArgumentException("Invalid data type.");
        case DATETIME:
            setDateTime(settings, buff, value);
            break;
        case DATE:
            setDate(settings, buff, value);
            break;
        case TIME:
            setTime(settings, buff, value);
            break;
        default:
            throw new IllegalArgumentException("Invalid data type.");
        }
    }

    /**
     * Convert time to DLMS bytes.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setTime(final GXDLMSSettings settings, final GXByteBuffer buff, final Object value) {
        java.util.Set<DateTimeSkips> skip = new HashSet<DateTimeSkips>();
        java.util.Calendar tm = java.util.Calendar.getInstance();
        if (value instanceof GXDateTime) {
            GXDateTime tmp = (GXDateTime) value;
            tm.setTime(tmp.getMeterCalendar().getTime());
            skip = tmp.getSkip();
        } else if (value instanceof java.util.Date) {
            tm.setTime((java.util.Date) value);
        } else if (value instanceof java.util.Calendar) {
            tm.setTime(((java.util.Calendar) value).getTime());
        } else if (value instanceof String) {
            DateFormat f = new SimpleDateFormat();
            try {
                tm.setTime(f.parse(value.toString()));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date time value.\r\n" + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Invalid date format.");
        }
        // Add additional date time skips.
        if (settings != null && !settings.getDateTimeSkips().isEmpty()) {
            skip.addAll(settings.getDateTimeSkips());
        }
        // Add time.
        if (skip.contains(DateTimeSkips.HOUR)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.HOUR_OF_DAY));
        }
        if (skip.contains(DateTimeSkips.MINUTE)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.MINUTE));
        }
        if (skip.contains(DateTimeSkips.SECOND)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.SECOND));
        }
        if (skip.contains(DateTimeSkips.MILLISECOND)) {
            // Hundredths of second is not used.
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.MILLISECOND) / 10);
        }
    }

    /**
     * Convert date to DLMS bytes.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setDate(final GXDLMSSettings settings, final GXByteBuffer buff, final Object value) {
        GXDateTime dt;
        if (value instanceof GXDateTime) {
            dt = (GXDateTime) value;
        } else if (value instanceof java.util.Date) {
            dt = new GXDateTime((java.util.Date) value);
        } else if (value instanceof java.util.Calendar) {
            dt = new GXDateTime(((java.util.Calendar) value).getTime());
        } else if (value instanceof String) {
            DateFormat f = new SimpleDateFormat();
            try {
                dt = new GXDateTime(f.parse(String.valueOf(value)));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Invalid date format.");
        }
        java.util.Calendar tm = java.util.Calendar.getInstance();
        tm.setTime(dt.getMeterCalendar().getTime());
        // Add additional date time skips.
        if (settings != null && !settings.getDateTimeSkips().isEmpty()) {
            dt.getSkip().addAll(settings.getDateTimeSkips());
        }
        // Add year.
        if (dt.getSkip().contains(DateTimeSkips.YEAR)) {
            buff.setUInt16(0xFFFF);
        } else {
            buff.setUInt16(tm.get(java.util.Calendar.YEAR));
        }
        // Add month
        if (dt.getExtra().contains(DateTimeExtraInfo.DST_BEGIN)) {
            buff.setUInt8(0xFE);
        } else if (dt.getExtra().contains(DateTimeExtraInfo.DST_END)) {
            buff.setUInt8(0xFD);
        } else if (dt.getSkip().contains(DateTimeSkips.MONTH)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8((tm.get(java.util.Calendar.MONTH) + 1));
        }
        // Add day
        if (dt.getExtra().contains(DateTimeExtraInfo.LAST_DAY)) {
            buff.setUInt8(0xFE);
        } else if (dt.getExtra().contains(DateTimeExtraInfo.LAST_DAY2)) {
            buff.setUInt8(0xFD);
        } else if (dt.getSkip().contains(DateTimeSkips.DAY)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.DATE));
        }
        if (dt.getSkip().contains(DateTimeSkips.DAY_OF_WEEK)) {
            // Week day is not specified.
            buff.setUInt8(0xFF);
        } else {
            int val = tm.get(java.util.Calendar.DAY_OF_WEEK);
            if (val == java.util.Calendar.SUNDAY) {
                val = 8;
            }
            buff.setUInt8(val - 1);
        }
    }

    public static GXDateTime getDateTime(final Object value) {
        GXDateTime dt;
        if (value instanceof GXDateTime) {
            dt = (GXDateTime) value;
        } else if (value instanceof java.util.Date) {
            dt = new GXDateTime((java.util.Date) value);
            dt.getSkip().add(DateTimeSkips.MILLISECOND);
        } else if (value instanceof java.util.Calendar) {
            dt = new GXDateTime((java.util.Calendar) value);
            dt.getSkip().add(DateTimeSkips.MILLISECOND);
        } else if (value instanceof String) {
            DateFormat f = new SimpleDateFormat();
            try {
                dt = new GXDateTime(f.parse(value.toString()));
                dt.getSkip().add(DateTimeSkips.MILLISECOND);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date time value." + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Invalid date format.");
        }
        return dt;
    }

    /**
     * Convert date time to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setDateTime(final GXDLMSSettings settings, final GXByteBuffer buff, final Object value) {
        GXDateTime dt = getDateTime(value);
        java.util.Calendar tm = dt.getMeterCalendar();
        // Add additional date time skips.
        if (settings != null && !settings.getDateTimeSkips().isEmpty()) {
            dt.getSkip().addAll(settings.getDateTimeSkips());
        }
        // Add year.
        if (dt.getSkip().contains(DateTimeSkips.YEAR)) {
            buff.setUInt16(0xFFFF);
        } else {
            buff.setUInt16(tm.get(java.util.Calendar.YEAR));
        }
        // Add month
        if (dt.getExtra().contains(DateTimeExtraInfo.DST_BEGIN)) {
            buff.setUInt8(0xFE);
        } else if (dt.getExtra().contains(DateTimeExtraInfo.DST_END)) {
            buff.setUInt8(0xFD);
        } else if (dt.getSkip().contains(DateTimeSkips.MONTH)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8((tm.get(java.util.Calendar.MONTH) + 1));
        }
        // Add day
        if (dt.getExtra().contains(DateTimeExtraInfo.LAST_DAY)) {
            buff.setUInt8(0xFE);
        } else if (dt.getExtra().contains(DateTimeExtraInfo.LAST_DAY2)) {
            buff.setUInt8(0xFD);
        } else if (dt.getSkip().contains(DateTimeSkips.DAY)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.DATE));
        }
        // Day of week.
        if (dt.getSkip().contains(DateTimeSkips.DAY_OF_WEEK)) {
            buff.setUInt8(0xFF);
        } else {
            if (dt.getDayOfWeek() == 0) {
                int val = tm.get(java.util.Calendar.DAY_OF_WEEK);
                if (val == java.util.Calendar.SUNDAY) {
                    val = 8;
                }
                buff.setUInt8(val - 1);
            } else {
                buff.setUInt8(dt.getDayOfWeek());
            }
        }

        // Add time.
        if (dt.getSkip().contains(DateTimeSkips.HOUR)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.HOUR_OF_DAY));
        }
        if (dt.getSkip().contains(DateTimeSkips.MINUTE)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.MINUTE));
        }
        if (dt.getSkip().contains(DateTimeSkips.SECOND)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.SECOND));
        }
        if (dt.getSkip().contains(DateTimeSkips.MILLISECOND)) {
            // Hundredth of seconds is not used.
            buff.setUInt8(0xFF);
        } else {
            int ms = tm.get(java.util.Calendar.MILLISECOND);
            if (ms != 0) {
                ms /= 10;
            }
            buff.setUInt8(ms);
        }
        // devitation not used.
        if (dt.getSkip().contains(DateTimeSkips.DEVIATION)) {
            buff.setUInt16(0x8000);
        } else {
            int deviation =
                    (dt.getMeterCalendar().get(Calendar.ZONE_OFFSET) + dt.getMeterCalendar().get(Calendar.DST_OFFSET))
                            / 60000;
            if (settings != null && settings.getUseUtc2NormalTime()) {
                buff.setUInt16(deviation);
            } else {
                buff.setUInt16(-deviation);
            }
        }
        // Add clock_status
        if (!dt.getSkip().contains(DateTimeSkips.STATUS)) {
            if ((dt.getMeterCalendar().getTimeZone().inDaylightTime(dt.getMeterCalendar().getTime())
                    || dt.getStatus().contains(ClockStatus.DAYLIGHT_SAVE_ACTIVE))) {
                buff.setUInt8(ClockStatus.toInteger(dt.getStatus()) | ClockStatus.DAYLIGHT_SAVE_ACTIVE.getValue());
            } else {
                buff.setUInt8(ClockStatus.toInteger(dt.getStatus()));
            }
        } else {
            // Status is not used.
            buff.setUInt8(0xFF);
        }
    }

    /**
     * Convert BCD to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setBcd(final GXByteBuffer buff, final Object value) {
        // Standard supports only size of byte.
        buff.setUInt8(((Number) value).byteValue());
    }

    /**
     * Convert Array to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setArray(final GXDLMSSettings settings, final GXByteBuffer buff, final Object value) {
        if (value != null) {
            List<?> arr;
            if (value instanceof List<?>) {
                arr = ((List<?>) value);
            } else {
                List<Object> tmp = new ArrayList<Object>();
                tmp.addAll(Arrays.asList((Object[]) value));
                arr = tmp;
            }
            int len = arr.size();
            setObjectCount(len, buff);
            for (Object it : arr) {
                setData(settings, buff, GXDLMSConverter.getDLMSDataType(it), it);
            }
        } else {
            setObjectCount(0, buff);
        }
    }

    /**
     * Split string to array.
     * 
     * @param str
     *            String to split.
     * @param separators
     *            Separators.
     * @return Split values.
     */
    public static List<String> split(final String str, final char[] separators) {
        int lastPos = 0;
        List<String> arr = new ArrayList<String>();
        for (int pos = 0; pos != str.length(); ++pos) {
            char ch = str.charAt(pos);
            for (char sep : separators) {
                if (ch == sep) {
                    if (lastPos != pos) {
                        arr.add(str.substring(lastPos, pos));
                    }
                    lastPos = pos + 1;
                    break;
                }
            }
        }
        if (lastPos != str.length()) {
            arr.add(str.substring(lastPos, str.length()));
        }
        return arr;
    }

    /**
     * Split string to array.
     * 
     * @param str
     *            String to split.
     * @param separator
     *            Separator.
     * @return Split values.
     */
    public static List<String> split(final String str, final char separator) {
        List<String> arr = new ArrayList<String>();
        int pos = 0, lastPos = 0;
        while ((pos = str.indexOf(separator, lastPos)) != -1) {
            arr.add(str.substring(lastPos, pos));
            lastPos = pos + 1;
        }
        if (str.length() > lastPos) {
            arr.add(str.substring(lastPos));
        } else {
            arr.add("");
        }
        return arr;
    }

    /**
     * Split string to array.
     * 
     * @param str
     *            String to split.
     * @param separator
     *            Separator.
     * @return Split values.
     */
    public static List<String> split(final String str, final String separator) {
        List<String> arr = new ArrayList<String>();
        int pos = 0, lastPos = 0;
        while ((pos = str.indexOf(separator, lastPos)) != -1) {
            arr.add(str.substring(lastPos, pos));
            lastPos = pos + separator.length();
        }
        if (str.length() > lastPos) {
            arr.add(str.substring(lastPos));
        } else {
            arr.add("");
        }
        return arr;
    }

    /**
     * Convert Octet string to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setOctetString(final GXByteBuffer buff, final Object value) {
        if (value instanceof String) {
            byte[] tmp = GXCommon.hexToBytes((String) value);
            setObjectCount(tmp.length, buff);
            buff.set(tmp);
        } else if (value instanceof byte[]) {
            setObjectCount(((byte[]) value).length, buff);
            buff.set((byte[]) value);
        } else if (value == null) {
            setObjectCount(0, buff);
        } else {
            throw new IllegalArgumentException("Invalid data type.");
        }
    }

    /**
     * Convert UTF string to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setUtfString(final GXByteBuffer buff, final Object value) {
        if (value != null) {
            byte[] tmp = String.valueOf(value).getBytes(StandardCharsets.UTF_8);
            setObjectCount(tmp.length, buff);
            buff.set(tmp);
        } else {
            buff.setUInt8(0);
        }
    }

    /**
     * Convert ASCII string to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setString(final GXByteBuffer buff, final Object value) {
        if (value != null) {
            String str = String.valueOf(value);
            setObjectCount(str.length(), buff);
            buff.set(GXCommon.getBytes(str));
        } else {
            buff.setUInt8(0);
        }
    }

    /**
     * Convert Bit string to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param val1
     *            Added value.
     * @param addCount
     *            Is bit count added.
     */
    public static void setBitString(final GXByteBuffer buff, final Object val1, final boolean addCount) {
        Object value = val1;
        if (value instanceof GXBitString) {
            value = ((GXBitString) value).getValue();
        }

        if (value instanceof String) {
            byte val = 0;
            String str = (String) value;
            if (addCount) {
                setObjectCount(str.length(), buff);
            }
            int index = 7;
            for (int pos = 0; pos != str.length(); ++pos) {
                char it = str.charAt(pos);
                if (it == '1') {
                    val |= (byte) (1 << index);
                } else if (it != '0') {
                    throw new IllegalArgumentException("Not a bit string.");
                }
                --index;
                if (index == -1) {
                    index = 7;
                    buff.setUInt8(val);
                    val = 0;
                }
            }
            if (index != 7) {
                buff.setUInt8(val);
            }
        } else if (value instanceof byte[]) {
            byte[] arr = (byte[]) value;
            setObjectCount(8 * arr.length, buff);
            buff.set(arr);
        } else if (value instanceof Byte) {
            setObjectCount(8, buff);
            buff.setUInt8(((Byte) value).byteValue());
        } else if (value == null) {
            buff.setUInt8(0);
        } else {
            throw new IllegalArgumentException("BitString must give as string.");
        }
    }

    /**
     * Get data type in bytes.
     * 
     * @param type
     *            Data type.
     * @return Size of data type in bytes.
     */
    public static int getDataTypeSize(final DataType type) {
        int size = -1;
        switch (type) {
        case BCD:
            size = 1;
            break;
        case BOOLEAN:
            size = 1;
            break;
        case DATE:
            size = 5;
            break;
        case DATETIME:
            size = 12;
            break;
        case ENUM:
            size = 1;
            break;
        case FLOAT32:
            size = 4;
            break;
        case FLOAT64:
            size = 8;
            break;
        case INT16:
            size = 2;
            break;
        case INT32:
            size = 4;
            break;
        case INT64:
            size = 8;
            break;
        case INT8:
            size = 1;
            break;
        case NONE:
            size = 0;
            break;
        case TIME:
            size = 4;
            break;
        case UINT16:
            size = 2;
            break;
        case UINT32:
            size = 4;
            break;
        case UINT64:
            size = 8;
            break;
        case UINT8:
            size = 1;
            break;
        default:
            break;
        }
        return size;
    }

    /**
     * Convert logical name byte array to string.
     * 
     * @param value
     *            Logical name as a byte array.
     * @return Logical name as a string.
     */
    public static String toLogicalName(final Object value) {
        if (value instanceof byte[]) {
            byte[] buff = (byte[]) value;
            if (buff.length == 0) {
                buff = new byte[6];
            }
            if (buff.length == 6) {
                return (buff[0] & 0xFF) + "." + (buff[1] & 0xFF) + "." + (buff[2] & 0xFF) + "." + (buff[3] & 0xFF) + "."
                        + (buff[4] & 0xFF) + "." + (buff[5] & 0xFF);
            }
            throw new IllegalArgumentException("Invalid Logical name.");
        }
        return (String) value;
    }

    /**
     * Convert logical name to byte array.
     * 
     * @param value
     *            Logical name.
     * @return Logical name as byte array.
     */
    public static byte[] logicalNameToBytes(final String value) {
        if (value == null || value.length() == 0) {
            return new byte[6];
        }
        List<String> items = GXCommon.split(value, '.');
        // If data is string.
        if (items.size() != 6) {
            throw new IllegalArgumentException("Invalid Logical name.");
        }
        byte[] buff = new byte[6];
        byte pos = 0;
        for (String it : items) {
            int v = Integer.parseInt(it);
            if (v < 0 || v > 255) {
                throw new IllegalArgumentException("Invalid Logical name.");
            }
            buff[pos] = (byte) v;
            ++pos;
        }
        return buff;
    }

    /**
     * Convert integer list to array.
     * 
     * @param list
     *            Integer list to convert.
     * @return Integer array.
     */
    public static int[] toIntArray(final List<Integer> list) {
        int[] ret = new int[list.size()];
        int pos = 0;
        for (Integer it : list) {
            ret[pos] = it.intValue();
            ++pos;
        }
        return ret;
    }

    /**
     * Convert short list to array.
     * 
     * @param list
     *            Integer list to convert.
     * @return Integer array.
     */
    public static short[] toShortArray(final List<Short> list) {
        short[] ret = new short[list.size()];
        int pos = 0;
        for (Short it : list) {
            ret[pos] = it.shortValue();
            ++pos;
        }
        return ret;
    }

    public static Date getGeneralizedTime(final String dateString) {
        int year, month, day, hour, minute, second = 0;
        Calendar calendar;
        year = Integer.parseInt(dateString.substring(0, 4));
        month = Integer.parseInt(dateString.substring(4, 6)) - 1;
        day = Integer.parseInt(dateString.substring(6, 8));
        hour = Integer.parseInt(dateString.substring(8, 10));
        minute = Integer.parseInt(dateString.substring(10, 12));
        // If UTC time.
        if (dateString.endsWith("Z")) {
            if (dateString.length() > 13) {
                second = Integer.parseInt(dateString.substring(12, 14));
            }
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.set(year, month, day, hour, minute, second);
            long v = calendar.getTimeInMillis();
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(v);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } else {
            if (dateString.length() > 17) {
                second = Integer.parseInt(dateString.substring(12, 14));
            }
            calendar = Calendar.getInstance(
                    TimeZone.getTimeZone("GMT" + dateString.substring(dateString.length() - 4, dateString.length())));
        }
        calendar.set(year, month, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String generalizedTime(final GXDateTime date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(date.getMeterCalendar().getTimeInMillis());
        StringBuilder sb = new StringBuilder();
        sb.append(GXCommon.integerString(calendar.get(Calendar.YEAR), 4));
        sb.append(GXCommon.integerString(1 + calendar.get(Calendar.MONTH), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.DAY_OF_MONTH), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.HOUR_OF_DAY), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.MINUTE), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.SECOND), 2));
        // UTC time.
        sb.append("Z");
        return sb.toString();
    }

    /**
     * Check is string ASCII string.
     * 
     * @param value
     *            ASCII string.
     * @return Is ASCII string
     */
    public static boolean isAscii(final String value) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(value);
    }

    // Reserved for internal use.
    public static short swapBits(short value) {
        short ret = 0;
        for (int pos = 0; pos != 8; ++pos) {
            ret = (short) ((ret << 1) | (value & 0x01));
            value = (short) (value >> 1);
        }
        return ret;
    }

    public static void datatoXml(Object value, GXDLMSTranslatorStructure xml) {
        if (value == null) {
            xml.appendEmptyTag(xml.getDataType(DataType.NONE));
        } else if (value instanceof GXStructure) {
            xml.appendStartTag(DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue(), null, null);
            for (Object it : (List<?>) value) {
                datatoXml(it, xml);
            }
            xml.appendEndTag(DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue());
        } else if (value instanceof GXArray) {
            xml.appendStartTag(DATA_TYPE_OFFSET + DataType.ARRAY.getValue(), null, null);
            for (Object it : (List<?>) value) {
                datatoXml(it, xml);
            }
            xml.appendEndTag(DATA_TYPE_OFFSET + DataType.ARRAY.getValue());
        } else if (value instanceof GXDLMSObjectCollection) {
            xml.appendStartTag(DATA_TYPE_OFFSET + DataType.ARRAY.getValue(), null, null);
            for (GXDLMSObject it : (GXDLMSObjectCollection) value) {
                datatoXml(it, xml);
            }
            xml.appendEndTag(DATA_TYPE_OFFSET + DataType.ARRAY.getValue());
        } else if (value instanceof GXDLMSObject) {
            xml.appendStartTag(DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue(), null, null);
            GXDLMSObject obj = (GXDLMSObject) value;
            xml.appendLine(DATA_TYPE_OFFSET + DataType.UINT16.getValue(), null, obj.getObjectType());
            xml.appendLine(DATA_TYPE_OFFSET + DataType.UINT8.getValue(), null, obj.getVersion());
            xml.appendLine(DATA_TYPE_OFFSET + DataType.OCTET_STRING.getValue(), null, obj.getLogicalName());
            xml.appendEndTag(DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue());
        } else {
            DataType dt = GXDLMSConverter.getDLMSDataType(value);
            xml.appendLine(DATA_TYPE_OFFSET + dt.getValue(), "", value);
        }
    }

    /**
     * Encrypt Flag name to two bytes.
     * 
     * @param flagName
     *            3 letter Flag name.
     * @return Encrypted Flag name.
     */
    public static int encryptManufacturer(final String flagName) {
        if (flagName.length() != 3) {
            throw new IllegalArgumentException("Invalid Flag name.");
        }
        int value = ((flagName.charAt(0) - 0x40) & 0x1f);
        value <<= 5;
        value += ((flagName.charAt(0) - 0x40) & 0x1f);
        value <<= 5;
        value += ((flagName.charAt(0) - 0x40) & 0x1f);
        return value;
    }

    /**
     * Decrypt two bytes to Flag name.
     * 
     * @param value
     *            Encrypted Flag value.
     * @return Flag name.
     */
    public static String decryptManufacturer(final int value) {
        int tmp = (value >> 8 | value << 8);
        char c = (char) ((tmp & 0x1f) + 0x40);
        tmp = (tmp >> 5);
        char c1 = (char) ((tmp & 0x1f) + 0x40);
        tmp = (tmp >> 5);
        char c2 = (char) ((tmp & 0x1f) + 0x40);
        return new String(new char[] { c2, c1, c });
    }

    /**
     * Get serial number from the system title.
     * 
     * @param st
     *            system title.
     * @return serial number.
     */
    private static long getSerialNumber(byte[] st, boolean isIdis) {
        long sn = 0;
        if (!isIdis) {
            sn = (st[3] << 8);
        }
        sn |= st[4];
        sn <<= 8;
        sn |= st[5];
        sn <<= 8;
        sn |= st[6];
        sn <<= 8;
        sn |= st[7];
        return sn;
    }

    static String idisSystemTitleToString(final byte[] st, final boolean addComments) {
        StringBuilder sb = new StringBuilder();
        if (addComments) {
            String newline = System.getProperty("line.separator");
            sb.append("IDIS system title:");
            sb.append(newline);
            sb.append("Manufacturer Code: ");
            sb.append(new String(new char[] { (char) st[0], (char) st[1], (char) st[2] }));
            sb.append(newline);
            switch (st[3]) {
            case 99:
                sb.append("DC");
                break;
            case 100:
                sb.append("IDIS package1 PLC single phase meter");
                break;
            case 101:
                sb.append("IDIS package1 PLC polyphase meter");
                break;
            case 102:
                sb.append("IDIS package2 IP single phase meter");
                break;
            case 103:
                sb.append("IDIS package2 IP polyphase meter");
                break;
            }
            sb.append(newline);
            sb.append("Function type: ");
            int ft = st[4] >> 4;
            boolean add = false;
            if ((ft & 0x1) != 0) {
                sb.append("Disconnector");
                add = true;
            }
            if ((ft & 0x2) != 0) {
                if (add) {
                    sb.append(", ");
                }
                add = true;
                sb.append("Load Management");
            }
            if ((ft & 0x4) != 0) {
                if (add) {
                    sb.append(", ");
                }
                sb.append("Multi Utility");
            }
            sb.append(newline);
            sb.append("Serial number: ");
            sb.append(String.valueOf(getSerialNumber(st, true)));
            sb.append(newline);
        } else {
            sb.append(new String(new char[] { (char) st[0], (char) st[1], (char) st[2] }));
            sb.append(" ");
            sb.append(String.valueOf(getSerialNumber(st, false)));
        }
        return sb.toString();
    }

    static String dlmsSystemTitleToString(final byte[] st, final boolean addComments) {
        String newline = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append(newline);
        sb.append("DLMS system title:");
        sb.append(newline);
        sb.append("Manufacturer Code: ");
        sb.append(new String(new char[] { (char) st[0], (char) st[1], (char) st[2] }));
        sb.append(newline);
        sb.append("Serial number: ");
        sb.append(new String(new char[] { (char) st[3], (char) st[4], (char) st[5], (char) st[6], (char) st[7] }));
        sb.append(newline);
        return sb.toString();
    }

    static String uniSystemTitleToString(final byte[] st, final boolean addComments) {
        String newline = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        if (addComments) {
            sb.append(newline);
            sb.append("UNI/TS system title:");
            sb.append(newline);
            sb.append("Manufacturer: ");
            int m = (st[0] << 8 | st[1]);
            sb.append(decryptManufacturer(m));
            sb.append(newline);
            sb.append("Serial number: ");
            sb.append(toHex(new byte[] { st[7], st[6], st[5], st[4], st[3], st[2] }, false));
            sb.append(newline);
        } else {
            int m = (st[0] << 8 | st[1]);
            sb.append(decryptManufacturer(m));
            sb.append(toHex(new byte[] { st[7], st[6], st[5], st[4], st[3], st[2] }, false));
        }
        return sb.toString();
    }

    private static boolean IsT1(final byte value) {
        return value > 98 && value < 104;
    }

    private static boolean IsT2(final byte value) {
        return (value & 0xf0) != 0;
    }

    /**
     * Convert system title to string.
     * 
     * @param standard
     *            Used standard.
     * @param st
     *            System title.
     * @param addComments
     *            Are comments added.
     * @return System title in string format.
     */
    public static String systemTitleToString(final Standard standard, final byte[] st, final boolean addComments) {
        if (st == null || st.length != 8) {
            return "";
        }
        if (standard == Standard.ITALY || !Character.isLetter(st[0]) || !Character.isLetter(st[1])
                || !Character.isLetter(st[2])) {
            return uniSystemTitleToString(st, addComments);
        }
        if (standard == Standard.IDIS || (IsT1(st[3]) && IsT2(st[4]))) {
            return idisSystemTitleToString(st, addComments);
        }
        return dlmsSystemTitleToString(st, addComments);
    }

    public static boolean isCiphered(final short cmd) {
        switch (cmd) {
        case Command.GLO_READ_REQUEST:
        case Command.GLO_WRITE_REQUEST:
        case Command.GLO_GET_REQUEST:
        case Command.GLO_SET_REQUEST:
        case Command.GLO_READ_RESPONSE:
        case Command.GLO_WRITE_RESPONSE:
        case Command.GLO_GET_RESPONSE:
        case Command.GLO_SET_RESPONSE:
        case Command.GLO_METHOD_REQUEST:
        case Command.GLO_METHOD_RESPONSE:
        case Command.DED_GET_REQUEST:
        case Command.DED_SET_REQUEST:
        case Command.DED_READ_RESPONSE:
        case Command.DED_GET_RESPONSE:
        case Command.DED_SET_RESPONSE:
        case Command.DED_METHOD_REQUEST:
        case Command.DED_METHOD_RESPONSE:
        case Command.GENERAL_GLO_CIPHERING:
        case Command.GENERAL_DED_CIPHERING:
        case Command.AARE:
        case Command.AARQ:
        case Command.GLO_CONFIRMED_SERVICE_ERROR:
        case Command.DED_CONFIRMED_SERVICE_ERROR:
        case Command.GENERAL_CIPHERING:
        case Command.RELEASE_REQUEST:
        case Command.GENERAL_SIGNING:
            return true;
        default:
            return false;
        }
    }

    /**
     * Encrypt or decrypt the data using external Hardware Security Module.
     * 
     * @param certificateType
     *            Certificate type.
     * @param Data
     *            Data.
     * @param encrypt
     *            Is data encrypted or decrypted.
     * @param keyType
     *            Key type.
     * @return
     */
    public static byte[] crypt(final GXDLMSSettings settings, final CertificateType certificateType, final byte[] Data,
            final boolean encrypt, final CryptoKeyType keyType, final int command, final Security security,
            final SecuritySuite securitySuite, final long invocationCounter) {
        if (settings.getCryptoNotifier() != null) {
            GXCryptoKeyParameter args = new GXCryptoKeyParameter();
            args.setEncrypt(encrypt);
            args.setKeyType(keyType);
            args.setCommand(command);
            args.setSystemTitle(settings.getCipher().getSystemTitle());
            args.setRecipientSystemTitle(settings.getSourceSystemTitle());
            args.setCertificateType(certificateType);
            args.setInvocationCounter(invocationCounter);
            args.setSecurity(security);
            args.setSecuritySuite(securitySuite);
            args.setSecurityPolicy(settings.getCipher().getSecurityPolicy());
            if (encrypt) {
                args.setPlainText(Data);
            } else {
                args.setEncrypted(Data);
            }
            args.setAuthenticationKey(settings.getCipher().getAuthenticationKey());
            if (settings.getCipher().getDedicatedKey() != null && settings.getCipher().getDedicatedKey().length == 16
                    && (settings.getConnected() & ConnectionState.DLMS) != 0) {
                args.setBlockCipherKey(settings.getCipher().getDedicatedKey());
            } else {
                args.setBlockCipherKey(settings.getCipher().getBlockCipherKey());
            }
            settings.getCryptoNotifier().onCrypto(settings.getCryptoNotifier(), args);
            if (encrypt) {
                return args.getEncrypted();
            } else {
                return args.getPlainText();
            }
        }
        return null;
    }

}