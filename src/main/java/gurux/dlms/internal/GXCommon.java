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
import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.TimeZone;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;

/*
 * <b> This class is for internal use only and is subject to changes or removal
 * in future versions of the API. Don't use it. </b>
 */
public final class GXCommon {

    /*
     * Constructor.
     */
    private GXCommon() {

    }

    /*
     * HDLC frame start and end charachter.
     */
    public static final byte HDLC_FRAME_START_END = 0x7E;
    public static final byte AARQ_TAG = 0x60;
    public static final byte AARE_TAG = 0x61;
    public static final byte INITIAL_REQUEST = 0x1;
    public static final byte INITIAL_RESPONSE = 0x8;
    public static final byte[] LOGICAL_NAME_OBJECT_ID =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x01 };
    public static final byte[] SHORT_NAME_OBJECT_ID =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x02 };
    public static final byte[] LOGICAL_NAME_OBJECT_ID_WITH_CIPHERING =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x01 };
    public static final byte[] SHORT_NAME_OBJECT_ID_WITH_CIPHERING =
            { 0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x02 };

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
        byte value;
        if (c > '9') {
            if (c > 'Z') {
                value = (byte) (c - 'a' + 10);
            } else {
                value = (byte) (c - 'A' + 10);
            }
        } else {
            value = (byte) (c - '0');
        }
        return value;
    }

    /*
     * /// Convert string to byte array.
     */
    public static byte[] hexToBytes(final String value) {
        byte[] buffer = new byte[value.length() / 2];
        int lastValue = -1;
        int index = 0;
        try {
            for (byte ch : value.getBytes("ASCII")) {
                if (ch >= '0' && ch < 'g') {
                    if (lastValue == -1) {
                        lastValue = getValue(ch);
                    } else if (lastValue != -1) {
                        buffer[index] = (byte) (lastValue << 4 | getValue(ch));
                        lastValue = -1;
                        ++index;
                    }
                } else if (lastValue != -1) {
                    buffer[index] = getValue(ch);
                    lastValue = -1;
                    ++index;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        byte[] tmp = new byte[index];
        System.arraycopy(buffer, 0, tmp, 0, index);
        return tmp;

    }

    /*
     * Convert byte array to hex string.
     */
    public static String toHex(final byte[] bytes) {
        return toHex(bytes, 0, bytes.length);
    }

    /*
     * Convert byte array to hex string.
     */
    public static String toHex(final byte[] bytes, final int index,
            final int count) {
        final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] hexChars = new char[count * 3];
        int tmp;
        for (int pos = index; pos != count; ++pos) {
            tmp = bytes[pos] & 0xFF;
            hexChars[pos * 3] = hexArray[tmp >>> 4];
            hexChars[pos * 3 + 1] = hexArray[tmp & 0x0F];
            hexChars[pos * 3 + 2] = ' ';
        }
        return new String(hexChars, 0, count * 3 - 1);
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

    public static byte[] rawData(final byte[] data, final int[] index,
            final int count) {
        byte[] buff = new byte[count];
        System.arraycopy(data, index[0], buff, 0, count);
        index[0] += count;
        return buff;
    }

    public static int getSize(final Object value) {
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

    public static byte[] getAsByteArray(final Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        if (value instanceof Byte) {
            return new byte[] { (byte) (((Byte) value).intValue() & 0xFF) };
        }
        if (value instanceof Short) {
            java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(2);
            buff.putShort((short) (((Short) value).intValue() & 0xFFFF));
            return buff.array();
        }
        if (value instanceof Integer) {
            java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(4);
            buff.putInt((int) ((Integer) value).intValue());
            return buff.array();
        }
        if (value instanceof Long) {
            java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(8);
            buff.putLong((long) (((Long) value).longValue()));
            return buff.array();
        }
        if (value instanceof String) {
            try {
                return ((String) value).getBytes("ASCII");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
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

    /*
     * Set item count.
     * @param count
     * @param buff
     */
    public static void setObjectCount(final int count,
            final GXByteBuffer buff) {
        if (count < 0x80) {
            buff.setUInt8(count);
        } else if (count < 0x100) {
            buff.setUInt8(0x81);
            buff.setUInt8(count);
        } else if (count < 0x10000) {
            buff.setUInt8(0x82);
            buff.setUInt16(count);
        } else {
            buff.setUInt8(0x84);
            buff.setUInt32(count);
        }
    }

    /*
     * Compares, whether two given arrays are similar.
     * @param arr1 First array to compare.
     * @param index Starting index of table, for first array.
     * @param arr2 Second array to compare.
     * @return True, if arrays are similar. False, if the arrays differ.
     */
    public static boolean compare(final byte[] arr1, final int[] index,
            final byte[] arr2) {
        if (arr1.length - index[0] < arr2.length) {
            return false;
        }
        int pos;
        for (pos = 0; pos != arr2.length; ++pos) {
            if (arr1[pos + index[0]] != arr2[pos]) {
                return false;
            }
        }
        index[0] += pos;
        return true;
    }

    /*
     * Reserved for internal use.
     */
    static void toBitString(final StringBuilder sb, final byte value,
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

    /*
     * Reserved for internal use.
     */
    public static Object getData(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value = null;
        int startIndex = buff.position();
        if (buff.position() == buff.size()) {
            info.setCompleate(false);
            return null;
        }
        info.setCompleate(true);
        boolean knownType = info.getType() != DataType.NONE;
        if (!knownType) {
            info.setType(DataType.forValue(buff.getUInt8()));
        }
        if (info.getType() == DataType.NONE) {
            return value;
        }
        if (buff.position() == buff.size()) {
            info.setCompleate(false);
            return null;
        }
        switch (info.getType()) {
        case ARRAY:
        case STRUCTURE:
            value = getArray(buff, info, startIndex);
            break;
        case BOOLEAN:
            value = getBoolean(buff, info);
            break;
        case BITSTRING:
            value = getBitString(buff, info);
            break;
        case INT32:
            value = getInt32(buff, info);
            break;
        case UINT32:
            value = getUInt32(buff, info);
            break;
        case STRING:
            value = getString(buff, info, knownType);
            break;
        case STRING_UTF8:
            value = getUtfString(buff, info, knownType);
            break;
        case OCTET_STRING:
            value = getOctetString(buff, info, knownType);
            break;
        case BCD:
            value = getBcd(buff, info, knownType);
            break;
        case INT8:
            value = getInt8(buff, info);
            break;
        case INT16:
            value = getInt16(buff, info);
            break;
        case UINT8:
            value = getUInt8(buff, info);
            break;
        case UINT16:
            value = getUInt16(buff, info);
            break;
        case COMPACTARRAY:
            throw new RuntimeException("Invalid data type.");
        case INT64:
            value = getInt64(buff, info);
            break;
        case UINT64:
            value = getUInt64(buff, info);
            break;
        case ENUM:
            value = getEnum(buff, info);
            break;
        case FLOAT32:
            value = getfloat(buff, info);
            break;
        case FLOAT64:
            value = getDouble(buff, info);
            break;
        case DATETIME:
            value = getDateTime(buff, info);
            break;
        case DATE:
            value = getDate(buff, info, knownType);
            break;
        case TIME:
            value = getTime(buff, info, knownType);
            break;
        default:
            throw new RuntimeException("Invalid data type.");
        }
        return value;
    }

    private static Object getArray(final GXByteBuffer buff,
            final GXDataInfo info, final int index) {
        Object value;
        if (info.getCount() == 0) {
            info.setCount(GXCommon.getObjectCount(buff));
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
            Object tmp = getData(buff, info2);
            if (!info2.isCompleate()) {
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
        info.setIndex(pos);
        value = arr.toArray();
        return value;
    }

    private static Object getTime(final GXByteBuffer buff,
            final GXDataInfo info, final boolean knownType) {
        Object value;
        if (knownType) {
            if (buff.size() - buff.position() < 4) {
                // If there is not enough data available.
                info.setCompleate(false);
                return null;
            }
        } else if (buff.size() - buff.position() < 5) {
            // If there is not enough data available.
            info.setCompleate(false);
            return null;
        }
        // Get time.
        int hour = buff.getUInt8();
        int minute = buff.getUInt8();
        int second = buff.getUInt8();
        int ms = buff.getUInt8();
        GXDateTime dt = new GXDateTime(-1, -1, -1, hour, minute, second, ms);
        value = dt;
        return value;
    }

    private static Object getDate(final GXByteBuffer buff,
            final GXDataInfo info, final boolean knownType) {
        Object value;
        if (knownType) {
            if (buff.size() - buff.position() < 5) {
                // If there is not enough data available.
                info.setCompleate(false);
                return null;
            }
        } else if (buff.size() - buff.position() < 6) {
            // If there is not enough data available.
            info.setCompleate(false);
            return null;
        }
        // Get year.
        int year = buff.getUInt16();
        // Get month
        int month = buff.getUInt8();
        // Get day
        int day = buff.getUInt8();
        // Skip week day
        buff.getUInt8();
        GXDateTime dt = new GXDateTime(year, month, day, -1, -1, -1, -1);
        value = dt;
        return value;
    }

    private static Object getDateTime(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 12) {
            info.setCompleate(false);
            return null;
        }
        // Get year.
        int year = buff.getUInt16();
        // Get month
        int month = buff.getUInt8();
        // Get day
        int day = buff.getUInt8();
        // Skip week day
        buff.getUInt8();
        // Get time.
        int hour = buff.getUInt8();
        int minute = buff.getUInt8();
        int second = buff.getUInt8();
        int ms = buff.getUInt8() & 0xFF;
        if (ms != 0xFF) {
            ms *= 10;
        } else {
            ms = 0;
        }
        int deviation = buff.getInt16();
        ClockStatus status = ClockStatus.forValue(buff.getUInt8());
        GXDateTime dt = new GXDateTime();
        dt.setStatus(status);
        java.util.Set<DateTimeSkips> skip = EnumSet.noneOf(DateTimeSkips.class);
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
        if (ms < 1 || ms > 1000) {
            skip.add(DateTimeSkips.MILLISECOND);
            ms = 0;
        }
        java.util.Calendar tm = java.util.Calendar.getInstance();
        tm.clear();
        tm.set(year, month, day, hour, minute, second);
        // If deviation is used. -32768 == 0x8000
        if (deviation != -32768) {
            short tmp =
                    (short) (tm.getTimeZone().getOffset(tm.getTime().getTime())
                            / 60000);
            deviation = tmp - deviation;
            // Add deviation.
            if (deviation != 0) {
                tm.add(Calendar.MINUTE, deviation);
            }
        }
        if (ms != 0) {
            tm.set(Calendar.MILLISECOND, ms);
        }
        if ((status.getValue()
                & ClockStatus.DAYLIGHT_SAVE_ACTIVE.getValue()) != 0
                && !TimeZone.getDefault().inDaylightTime(dt.getValue())) {
            // If meter is in summer time and PC is not.
            tm.add(Calendar.HOUR, 1);
        } else if ((status.getValue()
                & ClockStatus.DAYLIGHT_SAVE_ACTIVE.getValue()) == 0
                && TimeZone.getDefault().inDaylightTime(dt.getValue())) {
            // If meter is not summer time but PC is.
            tm.add(Calendar.HOUR, -1);
        }
        dt.setValue(tm.getTime());
        dt.setSkip(skip);
        value = dt;
        return value;
    }

    private static Object getDouble(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getDouble();
        return value;
    }

    private static Object getfloat(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getFloat();
        return value;
    }

    private static Object getEnum(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getUInt8();
        return value;
    }

    private static Object getUInt64(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getUInt64();
        return value;
    }

    private static Object getInt64(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 8) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getInt64();
        return value;
    }

    private static Object getUInt16(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 2) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getUInt16();
        return value;
    }

    private static Object getUInt8(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getUInt8() & 0xFF;
        return value;
    }

    private static Object getInt16(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 2) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getInt16();
        return value;
    }

    private static Object getInt8(final GXByteBuffer buff,
            final GXDataInfo info) {
        Object value;
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        value = buff.getInt8();
        return value;
    }

    private static Object getBcd(final GXByteBuffer buff, final GXDataInfo info,
            final boolean knownType) {
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
        StringBuilder bcd = new StringBuilder(len * 2);
        for (int a = 0; a != len; ++a) {
            byte ch = buff.getInt8();
            int idHigh = ch >>> 4;
            int idLow = ch & 0x0F;
            bcd.append(String.valueOf(idHigh) + String.valueOf(idLow));
        }
        value = bcd.toString();
        return value;
    }

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
        return value;
    }

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
        return value;
    }

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
            value = buff.getString(buff.position(), len);
        } else {
            value = "";
        }
        return value;
    }

    private static Object getUInt32(final GXByteBuffer buff,
            final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setCompleate(false);
            return null;
        }
        return buff.getUInt32();
    }

    private static Object getInt32(final GXByteBuffer buff,
            final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 4) {
            info.setCompleate(false);
            return null;
        }
        return buff.getInt32();
    }

    private static Object getBitString(final GXByteBuffer buff,
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
        return sb.toString();
    }

    private static Object getBoolean(final GXByteBuffer buff,
            final GXDataInfo info) {
        // If there is not enough data available.
        if (buff.size() - buff.position() < 1) {
            info.setCompleate(false);
            return null;
        }
        return buff.getUInt8() != 0;
    }

    /*
     * Reserved for internal use.
     */
    public static int getAdd(final GXByteBuffer buff) {
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
            size = ((size & 0xFE) >>> 1) | ((size & 0xFE00) >>> 1);
            return size;
        } else if (size == 4) {
            int tmp = buff.getInt32();
            tmp = ((tmp & 0xFE) >>> 1) | ((tmp & 0xFE00) >>> 1)
                    | ((tmp & 0xFE0000) >>> 1) | ((tmp & 0xFE0000) >>> 1);
            return tmp;
        }
        throw new InvalidParameterException("Wrong size.");
    }

    /*
     * Reserved for internal use.
     * @param buff
     * @param dataType
     * @param value
     */
    public static void setData(final GXByteBuffer buff, final DataType dataType,
            final Object value) {
        DataType type = dataType;
        try {
            // If value is enum get integer value.
            if (value instanceof Enum) {
                throw new RuntimeException(
                        "Value can't be enum. Give integer value.");
            }
            if (type == DataType.OCTET_STRING
                    && (value instanceof GXDateTime || value instanceof Date)) {
                type = DataType.DATETIME;
            }
            if (type == DataType.DATETIME || type == DataType.DATE
                    || type == DataType.TIME) {
                buff.setUInt8(DataType.OCTET_STRING.getValue());
            } else if ((type == DataType.ARRAY || type == DataType.STRUCTURE)
                    && value instanceof byte[]) {
                // If byte array is added do not add type.
                buff.set((byte[]) value);
                return;
            } else {
                buff.setUInt8(type.getValue());
            }
            if (type == DataType.NONE) {
                return;
            }
            if (type == DataType.BOOLEAN) {
                if (Boolean.parseBoolean(value.toString())) {
                    buff.setUInt8(1);
                } else {
                    buff.setUInt8(0);
                }
            } else if (type == DataType.INT8 || type == DataType.UINT8
                    || type == DataType.ENUM) {
                buff.setUInt8(((Number) value).byteValue());
            } else if (type == DataType.INT16 || type == DataType.UINT16) {
                buff.setUInt16(((Number) value).shortValue());
            } else if (type == DataType.INT32 || type == DataType.UINT32) {
                buff.setUInt32(((Number) value).intValue());
            } else if (type == DataType.INT64 || type == DataType.UINT64) {
                buff.setUInt64(((Number) value).longValue());
            } else if (type == DataType.FLOAT32) {
                java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(4);
                tmp.putFloat(((Number) value).floatValue());
                buff.set(tmp.array());
            } else if (type == DataType.FLOAT64) {
                java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(8);
                tmp.putDouble(((Number) value).doubleValue());
                buff.set(tmp.array());
            } else if (type == DataType.BITSTRING) {
                setBitString(buff, value);
            } else if (type == DataType.STRING) {
                setString(buff, value);
            } else if (type == DataType.STRING_UTF8) {
                setUtcString(buff, value);
            } else if (type == DataType.OCTET_STRING) {
                setOctetString(buff, value);
            } else if (type == DataType.ARRAY || type == DataType.STRUCTURE) {
                setArray(buff, value);
            } else if (type == DataType.BCD) {
                setBcd(buff, value);
            } else if (type == DataType.COMPACTARRAY) {
                throw new RuntimeException("Invalid data type.");
            } else if (type == DataType.DATETIME) {
                setDateTime(buff, value);
            } else if (type == DataType.DATE) {
                setDate(buff, value);
            } else if (type == DataType.TIME) {
                setTime(buff, value);
            } else {
                throw new RuntimeException("Invalid data type.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void setTime(final GXByteBuffer buff, final Object value) {
        java.util.Set<DateTimeSkips> skip = EnumSet.noneOf(DateTimeSkips.class);
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
        // Add size
        buff.setUInt8(4);
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
        buff.setUInt8(0xFF); // Hundredths of second is not used.
    }

    private static void setDate(final GXByteBuffer buff, final Object value)
            throws ParseException {
        GXDateTime dt;
        if (value instanceof GXDateTime) {
            dt = (GXDateTime) value;
        } else if (value instanceof java.util.Date) {
            dt = new GXDateTime((java.util.Date) value);
        } else if (value instanceof java.util.Calendar) {
            dt = new GXDateTime(((java.util.Calendar) value).getTime());
        } else if (value instanceof String) {
            DateFormat f = new SimpleDateFormat();
            dt = new GXDateTime(f.parse(value.toString()));
        } else {
            throw new RuntimeException("Invalid date format.");
        }
        java.util.Calendar tm = java.util.Calendar.getInstance();
        tm.setTime(dt.getValue());
        // Add size
        buff.setUInt8(5);
        // Add year.
        if (dt.getSkip().contains(DateTimeSkips.YEAR)) {
            buff.setUInt8(0xFF);
            buff.setUInt8(0xFF);
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
        // Week day is not spesified.
        buff.setUInt8(0xFF);
    }

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
        // Add size
        buff.setUInt8(12);
        tm.setTime(dt.getValue());

        // Add year.
        if (dt.getSkip().contains(DateTimeSkips.YEAR)) {
            buff.setUInt8(0xFF);
            buff.setUInt8(0xFF);
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
        // Week day is not spesified.
        buff.setUInt8(0xFF);
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
            // Hundredths of second is not used.
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
            short tmp =
                    (short) (tm.getTimeZone().getOffset(tm.getTime().getTime())
                            / 60000);
            buff.setUInt16(tmp);
        }
        // Add clock_status
        if (TimeZone.getDefault().inDaylightTime(dt.getValue())) {
            buff.setUInt8(dt.getStatus().getValue()
                    | ClockStatus.DAYLIGHT_SAVE_ACTIVE.getValue());
        } else {
            buff.setUInt8(dt.getStatus().getValue());
        }
    }

    private static void setBcd(final GXByteBuffer buff, final Object value) {
        if (!(value instanceof String)) {
            throw new RuntimeException("BCD value must give as string.");
        }
        String str = value.toString().trim();
        int len = str.length();
        if (len % 2 != 0) {
            str = "0" + str;
            ++len;
        }
        len /= 2;
        buff.setUInt8(len);
        for (int pos = 0; pos != len; ++pos) {
            int ch1 = Integer.parseInt(str.substring(2 * pos, 2 * pos + 1));
            int ch2 = Integer
                    .parseInt(str.substring(2 * pos + 1, 2 * pos + 1 + 1));
            buff.setUInt8((byte) (ch1 << 4 | ch2));
        }
    }

    private static void setArray(final GXByteBuffer buff, final Object value) {
        if (value != null) {
            int len = Array.getLength(value);
            setObjectCount(len, buff);
            for (int pos = 0; pos != len; ++pos) {
                Object it = Array.get(value, pos);
                setData(buff, getValueType(it), it);
            }
        } else {
            setObjectCount(0, buff);
        }
    }

    private static void setOctetString(final GXByteBuffer buff,
            final Object value) {
        // Example Logical name is octet string, so do not change to
        // string...
        if (value instanceof String) {
            String[] items = ((String) value).split("[.]", -1);
            // If data is string.
            if (items.length == 1) {
                byte[] tmp = ((String) value).getBytes();
                setObjectCount(tmp.length, buff);
                buff.set(tmp);
            } else {
                setObjectCount(items.length, buff);
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

    private static void setUtcString(final GXByteBuffer buff,
            final Object value) throws UnsupportedEncodingException {
        if (value != null) {
            String str = value.toString();
            setObjectCount(str.length(), buff);
            buff.set(str.getBytes("UTF-8"));
        } else {
            buff.setUInt8(0);
        }
    }

    private static void setString(final GXByteBuffer buff, final Object value)
            throws UnsupportedEncodingException {
        if (value != null) {
            String str = value.toString();
            setObjectCount(str.length(), buff);
            buff.set(str.getBytes("ASCII"));
        } else {
            buff.setUInt8(0);
        }
    }

    private static void setBitString(final GXByteBuffer buff,
            final Object value) {
        if (value instanceof String) {
            byte val = 0;
            int index = 0;
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
                    buff.setUInt8(val);
                    val = 0;
                }
            }
            if (index != 0) {
                buff.setUInt8(val);
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