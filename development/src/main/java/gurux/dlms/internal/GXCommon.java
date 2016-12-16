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
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.internal;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDate;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXTime;
import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;

/*
 * <b> This class is for internal use only and is subject to changes or removal
 * in future versions of the API. Don't use it. </b>
 */
public final class GXCommon {
    private static String zeroes = "00000000000000000000000000000000";
    private static char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static final char[] base64Array = { 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '+', '/', '=' };

    /*
     * Constructor.
     */
    private GXCommon() {

    }

    /*
     * HDLC frame start and end character.
     */
    public static final byte HDLC_FRAME_START_END = 0x7E;
    public static final byte[] LOGICAL_NAME_OBJECT_ID =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x01 };
    public static final byte[] SHORT_NAME_OBJECT_ID =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x02 };
    public static final byte[] LOGICAL_NAME_OBJECT_ID_WITH_CIPHERING =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x03 };
    public static final byte[] SHORT_NAME_OBJECT_ID_WITH_CIPHERING =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x04 };

    public static final byte[] LLC_SEND_BYTES =
            { (byte) 0xE6, (byte) 0xE6, 0x00 };
    public static final byte[] LLC_REPLY_BYTES =
            { (byte) 0xE6, (byte) 0xE7, 0x00 };

    /*
     * Reserved for internal use.
     * @param value bit value.
     * @param bitMask Bit mask.
     * @param val Final OR mask.
     */
    public static byte setBits(final byte value, final int bitMask,
            final boolean val) {
        byte mask = (byte) (0xFF ^ bitMask);
        byte tmp = (byte) (value & mask);
        if (val) {
            tmp |= bitMask;
        }
        return tmp;
    }

    /*
     * Convert array to list.
     * @param value Converted array.
     * @return Converted list.
     */
    public static List<Object> asList(final Object[] value) {
        List<Object> list = new ArrayList<Object>(value.length);
        for (Object it : value) {
            list.add(it);
        }
        return list;
    }

    /*
     * Add all items to the list.
     * @param target
     * @param list
     */
    public static void addAll(final List<Object> target, final Object[] list) {
        for (Object it : list) {
            target.add(it);
        }
    }

    /*
     * Convert string to byte array.
     * @param value String value.
     * @return String as bytes.
     */
    public static byte[] getBytes(final String value) {
        try {
            if (value == null) {
                return new byte[0];
            }
            return value.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
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
    public static String toHex(final byte[] bytes, final boolean addSpace,
            final int index, final int count) {
        if (bytes == null || bytes.length == 0 || count == 0) {
            return "";
        }
        char[] str = new char[count * 3];
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
            if (ch != ' ') {
                if (!((ch > 0x40 && ch < 'G') || (ch > 0x60 && ch < 'g')
                        || (ch > '/' && ch < ':'))) {
                    return false;
                }
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
            str.append(base64Array[b]);
            b = (value[pos] & 0x03) << 4;
            if (pos + 1 < value.length) {
                b |= (value[pos + 1] & 0xF0) >> 4;
                str.append(base64Array[b]);
                b = (value[pos + 1] & 0x0F) << 2;
                if (pos + 2 < value.length) {
                    b |= (value[pos + 2] & 0xC0) >> 6;
                    str.append(base64Array[b]);
                    b = value[pos + 2] & 0x3F;
                    str.append(base64Array[b]);
                } else {
                    str.append(base64Array[b]);
                    str.append('=');
                }
            } else {
                str.append(base64Array[b]);
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
     * @return
     */
    public static byte[] fromBase64(final String input) {
        if (input.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        int len = (input.length() * 3) / 4;
        int pos = input.indexOf('=');
        if (pos > 0) {
            len -= input.length() - pos;
        }
        byte[] decoded = new byte[len];
        char[] inChars = new char[4];
        int j = 0;
        int[] b = new int[4];
        for (pos = 0; pos != input.length(); pos += 4) {
            input.getChars(pos, pos + 4, inChars, 0);
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

    /*
     * Reserved for internal use.
     * @param value Byte value.
     * @param bitMask Bit mask.
     * @return Get bits.
     */
    public static boolean getBits(final byte value, final int bitMask) {
        return (value & bitMask) != 0;
    }

    public static byte getSize(final Object value) {
        if (value instanceof Byte) {
            return 1;
        }
        if (value instanceof Short) {
            return 2;
        }
        if (value instanceof Integer) {
            return 4;
        }
        if (value instanceof Long) {
            return 8;
        }
        throw new RuntimeException("Invalid object type.");
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
    public static byte setObjectCount(final int count,
            final GXByteBuffer buff) {
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
    public static void toBitString(final StringBuilder sb, final byte value,
            final int count2) {
        int count = count2;
        if (count > 8) {
            count = 8;
        }
        char[] data = new char[count];
        for (int pos = 0; pos != count; ++pos) {
            if ((value & (1 << pos)) != 0) {
                data[count - pos - 1] = '1';
            } else {
                data[count - pos - 1] = '0';
            }
        }
        sb.append(data);
    }

    /**
     * Get data from DLMS frame.
     * 
     * @param data
     *            received data.
     * @param info
     *            Data info.
     * @return Received data.
     */
    public static Object getData(final GXByteBuffer data,
            final GXDataInfo info) {
        Object value = null;
        int startIndex = data.position();
        if (data.position() == data.size()) {
            info.setCompleate(false);
            return null;
        }
        info.setCompleate(true);
        boolean knownType = info.getType() != DataType.NONE;
        // Get data type if it is unknown.
        if (!knownType) {
            info.setType(DataType.forValue(data.getUInt8()));
        }
        if (info.getType() == DataType.NONE) {
            if (info.getXml() != null) {
                info.getXml().appendLine("<"
                        + info.getXml().getDataType(info.getType()) + " />");
            }
            return value;
        }
        if (data.position() == data.size()) {
            info.setCompleate(false);
            return null;
        }
        switch (info.getType()) {
        case ARRAY:
        case STRUCTURE:
            value = getArray(data, info, startIndex);
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
            value = getUInt32(data, info);
            break;
        case STRING:
            value = getString(data, info, knownType);
            break;
        case STRING_UTF8:
            value = getUtfString(data, info, knownType);
            break;
        case OCTET_STRING:
            value = getOctetString(data, info, knownType);
            break;
        case BCD:
            value = getBcd(data, info, knownType);
            break;
        case INT8:
            value = getInt8(data, info);
            break;
        case INT16:
            value = getInt16(data, info);
            break;
        case UINT8:
            value = getUInt8(data, info);
            break;
        case UINT16:
            value = getUInt16(data, info);
            break;
        case COMPACT_ARRAY:
            throw new RuntimeException("Invalid data type.");
        case INT64:
            value = getInt64(data, info);
            break;
        case UINT64:
            value = getUInt64(data, info);
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
            value = getDateTime(data, info);
            break;
        case DATE:
            value = getDate(data, info);
            break;
        case TIME:
            value = getTime(data, info);
            break;
        default:
            throw new RuntimeException("Invalid data type.");
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
        String str =
                Long.toString(((Number) value).longValue(), 16).toUpperCase();
        if (desimals == 0 || str.length() == zeroes.length()) {
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
    private static Object getArray(final GXByteBuffer buff,
            final GXDataInfo info, final int index) {
        Object value;
        if (info.getCount() == 0) {
            info.setCount(GXCommon.getObjectCount(buff));
        }
        if (info.getXml() != null) {
            info.getXml().appendStartTag(
                    info.getXml().getDataType(info.getType()), "Qty",
                    info.getXml().integerToHex(info.getCount(), 2));
        }
        int size = buff.size() - buff.position();
        if (info.getCount() != 0 && size < 1) {
            info.setCompleate(false);
            return null;
        }
        int startIndex = index;
        java.util.ArrayList<Object> arr = new java.util.ArrayList<Object>(
                info.getCount() - info.getIndex());
        // Position where last row was found. Cache uses this info.
        int pos = info.getIndex();
        for (; pos != info.getCount(); ++pos) {
            GXDataInfo info2 = new GXDataInfo();
            info2.setXml(info.getXml());
            Object tmp = getData(buff, info2);
            if (!info2.isComplete()) {
                buff.position(startIndex);
                info.setCompleate(false);
                break;
            } else {
                if (info2.getCount() == info2.getIndex()) {
                    startIndex = buff.position();
                    arr.add(tmp);
                }
            }
        }
        if (info.getXml() != null) {
            info.getXml()
                    .appendEndTag(info.getXml().getDataType(info.getType()));
        }
        info.setIndex(pos);
        value = arr.toArray();
        return value;
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
    private static Object getTime(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        if (buff.size() - buff.position() < 4) {
            // If there is not enough data available.
            info.setCompleate(false);
            return null;
        }
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null,
                    GXCommon.toHex(buff.getData(), false, buff.position(), 4));
        }
        // Get time.
        int hour = buff.getUInt8();
        int minute = buff.getUInt8();
        int second = buff.getUInt8();
        int ms = buff.getUInt8();
        if (ms != 0xFF) {
            ms *= 10;
        }
        GXTime dt = new GXTime(hour, minute, second, ms);
        value = dt;
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
    private static Object getDate(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        if (buff.size() - buff.position() < 5) {
            // If there is not enough data available.
            info.setCompleate(false);
            return null;
        }
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null,
                    GXCommon.toHex(buff.getData(), false, buff.position(), 5));
        }
        // Get year.
        int year = buff.getUInt16();
        // Get month
        int month = buff.getUInt8();
        // Get day
        int day = buff.getUInt8();
        GXDate dt = new GXDate(year, month, day);
        value = dt;
        // Skip week day
        if (buff.getUInt8() == 0xFF) {
            dt.getSkip().add(DateTimeSkips.DAY_OF_WEEK);
        }
        return value;
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
    private static Object getDateTime(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        java.util.Set<DateTimeSkips> skip = new HashSet<DateTimeSkips>();

        // If there is not enough data available.
        if (buff.size() - buff.position() < 12) {
            info.setCompleate(false);
            return null;
        }
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null,
                    GXCommon.toHex(buff.getData(), false, buff.position(), 12));
        }
        // Get year.
        int year = buff.getUInt16();
        // Get month
        int month = buff.getUInt8();
        // Get day
        int day = buff.getUInt8();
        // Skip week day
        if (buff.getUInt8() == 0xFF) {
            skip.add(DateTimeSkips.DAY_OF_WEEK);
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
        int status = buff.getUInt8();
        GXDateTime dt = new GXDateTime();
        dt.setStatus(ClockStatus.forValue(status));
        if (year < 1 || year == 0xFFFF) {
            skip.add(DateTimeSkips.YEAR);
            java.util.Calendar tm = java.util.Calendar.getInstance();
            year = tm.get(Calendar.YEAR);
        }
        dt.setDaylightSavingsBegin(month == 0xFE);
        dt.setDaylightSavingsEnd(month == 0xFD);
        if (month < 1 || month > 12) {
            skip.add(DateTimeSkips.MONTH);
            month = 0;
        } else {
            month -= 1;
        }
        if (day == -1 || day == 0 || day > 31) {
            skip.add(DateTimeSkips.DAY);
            day = 1;
        } else if (day < 0) {
            Calendar cal = Calendar.getInstance();
            day = cal.getActualMaximum(Calendar.DATE) + day + 3;
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
        java.util.Calendar tm = java.util.Calendar.getInstance();
        tm.clear();
        tm.set(year, month, day, hour, minute, second);
        if (ms != 0) {
            tm.set(Calendar.MILLISECOND, ms);
        }
        // If summer time.
        if ((status & ClockStatus.DAYLIGHT_SAVE_ACTIVE.getValue()) != 0) {
            tm.add(Calendar.HOUR, 1);
        }
        dt.setValue(tm.getTime(), deviation);
        dt.setSkip(skip);
        value = dt;
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
    private static Object getDouble(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setCompleate(false);
            return null;
        }
        value = new Double(buff.getDouble());
        if (info.getXml() != null) {
            GXByteBuffer tmp = new GXByteBuffer();
            setData(tmp, DataType.FLOAT64, value);
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null,
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
    private static Object getFloat(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setCompleate(false);
            return null;
        }
        value = new Float(buff.getFloat());
        if (info.getXml() != null) {
            GXByteBuffer tmp = new GXByteBuffer();
            setData(tmp, DataType.FLOAT32, value);
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null,
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
    private static Object getEnum(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        value = new Short(buff.getUInt8());
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 2));
        }
        return value;
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
    private static Object getUInt64(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getUInt64();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 16));
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
    private static Object getInt64(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setCompleate(false);
            return null;
        }
        value = new Long(buff.getInt64());
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 16));
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
    private static Object getUInt16(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 2) {
            info.setCompleate(false);
            return null;
        }
        value = new Integer(buff.getUInt16());
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 4));

        }
        return value;
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
    private static Object getUInt8(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        value = new Integer(buff.getUInt8() & 0xFF);
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 2));

        }
        return value;
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
    private static Object getInt16(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 2) {
            info.setCompleate(false);
            return null;
        }
        value = new Short(buff.getInt16());
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 4));
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
    private static Object getInt8(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        value = new Byte(buff.getInt8());
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 2));
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
    private static Object getBcd(final GXByteBuffer buff, final GXDataInfo info,
            final boolean knownType) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        short value = buff.getUInt8();
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 2));
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
    private static Object getUtfString(final GXByteBuffer buff,
            final GXDataInfo info, final boolean knownType) {
        Object value;
        int len;
        if (knownType) {
            len = buff.size();
        } else {
            len = GXCommon.getObjectCount(buff);
            // If there is not enough data available.
            if (buff.size() - buff.position() < len) {
                info.setCompleate(false);
                return null;
            }
        }
        if (len > 0) {
            value = buff.getString(buff.position(), len, "UTF-8");
        } else {
            value = "";
        }
        if (info.getXml() != null) {
            if (info.getXml().getShowStringAsHex()) {
                info.getXml().appendLine(
                        info.getXml().getDataType(info.getType()), null,
                        GXCommon.toHex(buff.getData(), false,
                                buff.position() - len, len));
            } else {
                info.getXml().appendLine(
                        info.getXml().getDataType(info.getType()), null,
                        value.toString());
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
    private static Object getOctetString(final GXByteBuffer buff,
            final GXDataInfo info, final boolean knownType) {
        Object value;
        int len;
        if (knownType) {
            len = buff.size();
        } else {
            len = GXCommon.getObjectCount(buff);
            // If there is not enough data available.
            if (buff.size() - buff.position() < len) {
                info.setCompleate(false);
                return null;
            }
        }
        byte[] tmp = new byte[len];
        buff.get(tmp);
        value = tmp;
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, GXCommon.toHex(tmp, false));
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
    private static Object getString(final GXByteBuffer buff,
            final GXDataInfo info, final boolean knownType) {
        Object value;
        int len;
        if (knownType) {
            len = buff.size();
        } else {
            len = GXCommon.getObjectCount(buff);
            // If there is not enough data available.
            if (buff.size() - buff.position() < len) {
                info.setCompleate(false);
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
                info.getXml().appendLine(
                        info.getXml().getDataType(info.getType()), null,
                        GXCommon.toHex(buff.getData(), false,
                                buff.position() - len, len));
            } else {
                info.getXml().appendLine(
                        info.getXml().getDataType(info.getType()), null,
                        value.toString());
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
    private static Object getUInt32(final GXByteBuffer buff,
            final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setCompleate(false);
            return null;
        }
        Long value = new Long(buff.getUInt32());
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 8));
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
    private static Object getInt32(final GXByteBuffer buff,
            final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setCompleate(false);
            return null;
        }
        Integer value = new Integer(buff.getInt32());
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, info.getXml().integerToHex(value, 8));
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
    private static String getBitString(final GXByteBuffer buff,
            final GXDataInfo info) {
        int cnt = getObjectCount(buff);
        double t = cnt;
        t /= 8;
        if (cnt % 8 != 0) {
            ++t;
        }
        int byteCnt = (int) Math.floor(t);
        // If there is not enough data available.
        if (buff.size() - buff.position() < byteCnt) {
            info.setCompleate(false);
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (cnt > 0) {
            toBitString(sb, buff.getInt8(), cnt);
            cnt -= 8;
        }
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, sb.toString());
        }
        return sb.toString();
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
    private static Object getBoolean(final GXByteBuffer buff,
            final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        Boolean value = new Boolean(buff.getUInt8() != 0);
        if (info.getXml() != null) {
            info.getXml().appendLine(info.getXml().getDataType(info.getType()),
                    null, value.toString());
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
            tmp = ((tmp & 0xFE) >> 1) | ((tmp & 0xFE00) >> 2)
                    | ((tmp & 0xFE0000) >> 3) | ((tmp & 0xFE000000) >> 4);
            return (int) tmp;
        }
        throw new IllegalArgumentException("Wrong size.");
    }

    /**
     * Convert object to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param dataType
     *            Data type.
     * @param value
     *            Added Value.
     */
    public static void setData(final GXByteBuffer buff, final DataType dataType,
            final Object value) {
        DataType type = dataType;
        // If value is enum get integer value.
        if (value instanceof Enum) {
            throw new RuntimeException(
                    "Value can't be enum. Give integer value.");
        }
        if ((type == DataType.ARRAY || type == DataType.STRUCTURE)
                && value instanceof byte[]) {
            // If byte array is added do not add type.
            buff.set((byte[]) value);
            return;
        } else {
            buff.setUInt8(type.getValue());
        }
        switch (type) {
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
            setBitString(buff, value);
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
                setDate(buff, value);
            } else if (value instanceof GXTime) {
                // Add size
                buff.setUInt8(4);
                setTime(buff, value);
            } else if (value instanceof GXDateTime
                    || value instanceof java.util.Date
                    || value instanceof java.util.Calendar) {
                // Date an calendar are always written as date time.
                buff.setUInt8(12);
                setDateTime(buff, value);
            } else {
                setOctetString(buff, value);
            }
            break;
        case ARRAY:
        case STRUCTURE:
            setArray(buff, value);
            break;
        case BCD:
            setBcd(buff, value);
            break;
        case COMPACT_ARRAY:
            throw new RuntimeException("Invalid data type.");
        case DATETIME:
            setDateTime(buff, value);
            break;
        case DATE:
            setDate(buff, value);
            break;
        case TIME:
            setTime(buff, value);
            break;
        default:
            throw new RuntimeException("Invalid data type.");
        }
    }

    /**
     * Convert time to DLMS bytes.
     * 
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setTime(final GXByteBuffer buff, final Object value) {
        java.util.Set<DateTimeSkips> skip = new HashSet<DateTimeSkips>();
        java.util.Calendar tm = java.util.Calendar.getInstance();
        if (value instanceof GXDateTime) {
            GXDateTime tmp = (GXDateTime) value;
            tm.setTime(tmp.getValue());
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
                throw new RuntimeException(
                        "Invalid date time value.\r\n" + e.getMessage());
            }
        } else {
            throw new RuntimeException("Invalid date format.");
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
     * @param buff
     *            Byte buffer where data is write.
     * @param value
     *            Added value.
     */
    private static void setDate(final GXByteBuffer buff, final Object value) {
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
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Invalid date format.");
        }
        java.util.Calendar tm = java.util.Calendar.getInstance();
        tm.setTime(dt.getValue());
        // Add year.
        if (dt.getSkip().contains(DateTimeSkips.YEAR)) {
            buff.setUInt16(0xFFFF);
        } else {
            buff.setUInt16(tm.get(java.util.Calendar.YEAR));
        }
        // Add month
        if (dt.getDaylightSavingsBegin()) {
            buff.setUInt8(0xFE);
        } else if (dt.getDaylightSavingsEnd()) {
            buff.setUInt8(0xFD);
        } else if (dt.getSkip().contains(DateTimeSkips.MONTH)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8((tm.get(java.util.Calendar.MONTH) + 1));
        }
        // Add day
        if (dt.getSkip().contains(DateTimeSkips.DAY)) {
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
            java.util.Calendar tm = (java.util.Calendar) value;
            dt = new GXDateTime(tm.getTime());
            dt.setDeviation(tm.getTimeZone().getRawOffset() / 60000);
            dt.getSkip().add(DateTimeSkips.MILLISECOND);
        } else if (value instanceof String) {
            DateFormat f = new SimpleDateFormat();
            try {
                dt = new GXDateTime(f.parse(value.toString()));
                dt.getSkip().add(DateTimeSkips.MILLISECOND);
            } catch (ParseException e) {
                throw new RuntimeException(
                        "Invalid date time value." + e.getMessage());
            }
        } else {
            throw new RuntimeException("Invalid date format.");
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
    private static void setDateTime(final GXByteBuffer buff,
            final Object value) {
        GXDateTime dt;
        java.util.Calendar tm = null;
        if (value instanceof GXDateTime) {
            dt = (GXDateTime) value;
        } else if (value instanceof java.util.Date) {
            dt = new GXDateTime((java.util.Date) value);
            dt.getSkip().add(DateTimeSkips.MILLISECOND);
        } else if (value instanceof java.util.Calendar) {
            tm = (java.util.Calendar) value;
            dt = new GXDateTime(tm.getTime());
            dt.setDeviation(tm.getTimeZone().getRawOffset() / 60000);
            dt.getSkip().add(DateTimeSkips.MILLISECOND);
        } else if (value instanceof String) {
            DateFormat f = new SimpleDateFormat();
            try {
                dt = new GXDateTime(f.parse(value.toString()));
                dt.getSkip().add(DateTimeSkips.MILLISECOND);
            } catch (ParseException e) {
                throw new RuntimeException(
                        "Invalid date time value." + e.getMessage());
            }
        } else {
            throw new RuntimeException("Invalid date format.");
        }
        if (tm == null) {
            tm = java.util.Calendar.getInstance();
        }
        Date date = dt.toMeterTime();
        boolean summertime = tm.getTimeZone().inDaylightTime(date);
        tm.setTime(dt.getValue());
        // If summer time.
        if (summertime
                || dt.getStatus().contains(ClockStatus.DAYLIGHT_SAVE_ACTIVE)) {
            tm.add(Calendar.HOUR, -1);
        }

        // Add year.
        if (dt.getSkip().contains(DateTimeSkips.YEAR)) {
            buff.setUInt16(0xFFFF);
        } else {
            buff.setUInt16(tm.get(java.util.Calendar.YEAR));
        }
        // Add month
        if (dt.getDaylightSavingsEnd()) {
            buff.setUInt8(0xFD);
        } else if (dt.getDaylightSavingsBegin()) {
            buff.setUInt8(0xFE);
        } else if (dt.getSkip().contains(DateTimeSkips.MONTH)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8((tm.get(java.util.Calendar.MONTH) + 1));
        }
        // Add day
        if (dt.getSkip().contains(DateTimeSkips.DAY)) {
            buff.setUInt8(0xFF);
        } else {
            buff.setUInt8(tm.get(java.util.Calendar.DATE));
        }
        // Week day.
        if (dt.getSkip().contains(DateTimeSkips.DAY_OF_WEEK)) {
            buff.setUInt8(0xFF);
        } else {
            int val = tm.get(java.util.Calendar.DAY_OF_WEEK);
            if (val == java.util.Calendar.SUNDAY) {
                val = 8;
            }
            buff.setUInt8(val - 1);
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
        if (dt.getSkip().contains(DateTimeSkips.DEVITATION)) {
            buff.setUInt16(0x8000);
        } else {
            // Add devitation.
            buff.setUInt16(dt.getDeviation());
        }
        // Add clock_status
        if (summertime) {
            buff.setUInt8(ClockStatus.toInteger(dt.getStatus())
                    | ClockStatus.DAYLIGHT_SAVE_ACTIVE.getValue());
        } else {
            buff.setUInt8(ClockStatus.toInteger(dt.getStatus()));
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
    private static void setArray(final GXByteBuffer buff, final Object value) {
        if (value != null) {
            Object[] arr = ((Object[]) value);
            int len = arr.length;
            setObjectCount(len, buff);
            for (int pos = 0; pos != len; ++pos) {
                Object it = arr[pos];
                setData(buff, getValueType(it), it);
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
    private static void setOctetString(final GXByteBuffer buff,
            final Object value) {
        // Example Logical name is octet string, so do not change to
        // string...
        if (value instanceof String) {
            List<String> items = split((String) value, '.');
            // If data is string.
            if (items.size() == 1) {
                byte[] tmp = ((String) value).getBytes();
                setObjectCount(tmp.length, buff);
                buff.set(tmp);
            } else {
                setObjectCount(items.size(), buff);
                for (String it : items) {
                    buff.setUInt8(Integer.parseInt(it));
                }
            }
        } else if (value instanceof byte[]) {
            setObjectCount(((byte[]) value).length, buff);
            buff.set((byte[]) value);
        } else if (value == null) {
            setObjectCount(0, buff);
        } else {
            throw new RuntimeException("Invalid data type.");
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
    private static void setUtfString(final GXByteBuffer buff,
            final Object value) {
        if (value != null) {
            try {
                byte[] tmp = String.valueOf(value).getBytes("UTF-8");
                setObjectCount(tmp.length, buff);
                buff.set(tmp);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
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
     * @param value
     *            Added value.
     */
    private static void setBitString(final GXByteBuffer buff,
            final Object value) {
        if (value instanceof String) {
            byte val = 0;
            int index = 0;
            GXByteBuffer tmp = new GXByteBuffer();
            String str = new StringBuilder((String) value).reverse().toString();
            setObjectCount(str.length(), buff);
            for (char it : str.toCharArray()) {
                if (it == '1') {
                    val |= (byte) (1 << index++);
                } else if (it == '0') {
                    index++;
                } else {
                    throw new RuntimeException("Not a bit string.");
                }
                if (index == 8) {
                    index = 0;
                    tmp.setUInt8(val);
                    val = 0;
                }
            }
            if (index != 0) {
                tmp.setUInt8(val);
            }
            for (int pos = tmp.size() - 1; pos != -1; --pos) {
                buff.setUInt8(tmp.getUInt8(pos));
            }
        } else if (value instanceof byte[]) {
            byte[] arr = (byte[]) value;
            setObjectCount(arr.length, buff);
            buff.set(arr);
        } else if (value == null) {
            buff.setUInt8(0);
        } else {
            throw new RuntimeException("BitString must give as string.");
        }
    }

    public static DataType getValueType(final Object value) {
        if (value == null) {
            return DataType.NONE;
        }
        if (value instanceof byte[]) {
            return DataType.OCTET_STRING;
        }
        if (value instanceof Enum) {
            return DataType.ENUM;
        }
        if (value instanceof Byte) {
            return DataType.INT8;
        }
        if (value instanceof Short) {
            return DataType.INT16;
        }
        if (value instanceof Integer) {
            return DataType.INT32;
        }
        if (value instanceof Long) {
            return DataType.INT64;
        }
        if (value instanceof GXTime) {
            return DataType.TIME;
        }
        if (value instanceof GXDate) {
            return DataType.DATE;
        }

        if (value instanceof java.util.Date || value instanceof GXDateTime) {
            return DataType.DATETIME;
        }
        if (value.getClass().isArray()) {
            return DataType.ARRAY;
        }
        if (value instanceof String) {
            return DataType.STRING;
        }
        if (value instanceof Boolean) {
            return DataType.BOOLEAN;
        }
        if (value instanceof Float) {
            return DataType.FLOAT32;
        }
        if (value instanceof Double) {
            return DataType.FLOAT64;
        }
        throw new RuntimeException("Invalid value.");
    }
}