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

import gurux.dlms.GXDateTime;
import gurux.dlms.enums.ClockStatus;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <b>
 * This class is for internal use only and is subject to changes 
 * or removal in future versions of the API. Don't use it.
 * </b>
 */
public class GXCommon
{       
    public static final byte HDLCFrameStartEnd = 0x7E;
    public static final byte InitialRequest = 0x1;
    public static final byte InitialResponce = 0x8;
    public static final byte AARQTag = 0x60;
    public static final byte AARETag = 0x61;
    public static final byte[] LogicalNameObjectID = {0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x01};
    public static final byte[] ShortNameObjectID = {0x60, (byte) 0x85, 0x74, 0x05, 0x08, 0x01, 0x02};
    public static final byte[] LLCSendBytes = {(byte) 0xE6, (byte) 0xE6, 0x00};
    public static final byte[] LLCReplyBytes = {(byte)0xE6, (byte) 0xE7, 0x00};

    /**
     * Reserved for internal use.
     * @param value
     * @param BitMask
     * @param val 
     */
    public static byte setBits(byte value, int BitMask, boolean val)
    {
        byte Mask = (byte)(0xFF ^ BitMask);
        value &= Mask;
        if (val)
        {
            value |= BitMask;
        }
        return value;
    }
    
    /*
     * Convert string to byte array.
     */
    public static byte[] getBytes(String str)
    {
        try 
        {
            if (str == null)
            {
                return new byte[0];
            }
            return str.getBytes("ASCII");
        } 
        catch (UnsupportedEncodingException ex) 
        {
            Logger.getLogger(GXCommon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }
    
    /*
    /// Convert string to byte array.
    */
    public static byte[] hexToBytes(String str, boolean includeSpace)
    {
        if (includeSpace && !str.isEmpty() && str.charAt(str.length() -1) != ' ')
        {
            str += " ";
        }
        int cnt = includeSpace ? 3 : 2;
        if (str.length() == 0 || str.length() % cnt != 0)
        {
            throw new RuntimeException("Not hex string");
        }
        byte[] buffer = new byte[str.length() / cnt];
        char c;
        for (int bx = 0, sx = 0; bx < buffer.length; ++bx, ++sx)
        {
            c = str.charAt(sx);
            buffer[bx] = (byte)((c > '9' ? (c > 'Z' ? (c - 'a' + 10) : (c - 'A' + 10)) : (c - '0')) << 4);
            c = str.charAt(++sx);
            buffer[bx] |= (byte)(c > '9' ? (c > 'Z' ? (c - 'a' + 10) : (c - 'A' + 10)) : (c - '0'));
            if (includeSpace)
            {
                ++sx;
            }
        }
        return buffer;
    }
    /*
     * Convert byte array to hex string.
     */
    public static String toHex(byte[] bytes) 
    {   
        return toHex(bytes, 0, bytes.length);        
    } 
    
    /*
     * Convert byte array to hex string.
     */
    public static String toHex(byte[] bytes, int index, int count) 
    {         
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[count * 3];
        int tmp;
        for (int pos = index; pos != count; ++pos) 
        {
            tmp = bytes[pos] & 0xFF;
            hexChars[pos * 3] = hexArray[tmp >>> 4];
            hexChars[pos * 3 + 1] = hexArray[tmp & 0x0F];
            hexChars[pos * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }   

    /**
     * Reserved for internal use.
     * @param value
     * @param BitMask
     * @return 
     */
    public static boolean getBits(byte value, int BitMask)
    {
        return (value & BitMask) != 0;
    }

    public static byte[] rawData(byte[] data, int[] index, int count)
    {
        byte[] buff = new byte[count];
        System.arraycopy(data, index[0], buff, 0, count);
        index[0] += count;
        return buff;
    }
        
    public static byte[] getAsByteArray(Object value) 
            throws UnsupportedEncodingException
    {
        if (value instanceof byte[])
        {
            return (byte[]) value;
        }
        if (value instanceof Byte)
        {
            return new byte[]{(byte) (((Byte)value).intValue() & 0xFF)};
        }
        if (value instanceof Short)
        {
            java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(2);
            buff.putShort((short) (((Short)value).intValue() & 0xFFFF));
            return buff.array();
        }
        if (value instanceof Integer)
        {
            java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(4);
            buff.putInt((int) ((Integer)value).intValue());
            return buff.array();
        }
        if (value instanceof Long)
        {
            java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(8);
            buff.putLong((long) (((Long)value).longValue()));
            return buff.array();
        }
        if (value instanceof String)
        {
            return ((String)value).getBytes("ASCII");        
        }
        throw new RuntimeException("Invalid object type.");
    }

    public static int intValue(Object value)
    {
        if (value instanceof Byte)
        {
            return ((Byte)value).intValue() & 0xFF;        
        }
        if (value instanceof Short)
        {
            return ((Short)value).intValue() & 0xFFFF;        
        }
        return ((Number)value).intValue();
    }
    
    public static short getInt16(byte[] data, int[] index)
    {
        return getInt16(java.nio.ByteBuffer.wrap(data), index);
    }

    public static int getInt32(byte[] data, int[] index)
    {
        return getInt32(java.nio.ByteBuffer.wrap(data), index);
    }
    
    public static long getInt64(byte[] data, int[] index)
    {
        long value = java.nio.ByteBuffer.wrap(data).getLong(index[0]);
        index[0] += 8;
        return value;
    }

    public static float toFloat(byte[] data, int[] index)
    {         
        float value = java.nio.ByteBuffer.wrap(data).getFloat(index[0]);
        index[0] += 4;
        return value;
    }

    public static double toDouble(byte[] data, int[] index)
    {
        double value = java.nio.ByteBuffer.wrap(data).getDouble(index[0]);
        index[0] += 8;
        return value;
    }

    public static int getUInt16(byte[] data, int[] index)
    {
        return getUInt16(java.nio.ByteBuffer.wrap(data), index);
    }

    public static long getUInt32(byte[] data, int[] index)
    {
        return getUInt32(java.nio.ByteBuffer.wrap(data), index);
    }

    public static BigInteger getUInt64(byte[] data, int[] index)
    { 
        long value = java.nio.ByteBuffer.wrap(data).getLong(index[0]);
        index[0] += 8;
        BigInteger b = BigInteger.valueOf(value);
        if (b.compareTo(BigInteger.ZERO) < 0)
        {
            b = b.add(BigInteger.ONE.shiftLeft(64));        
        }
        return b;
    }

    public static short getInt16(java.nio.ByteBuffer data, int[] index)
    {
        short value = data.getShort(index[0]);
        index[0] += 2;
        return value;
    }

    public static int getInt32(java.nio.ByteBuffer data, int[] index)
    {
        int value = data.getInt(index[0]);
        index[0] += 4;
        return value;
    }

    public static int getUInt16(java.nio.ByteBuffer data, int[] index)
    {
        short value = data.getShort(index[0]);
        index[0] += 2;
        return value & 0xFFFF;
    }

    public static long getUInt32(java.nio.ByteBuffer data, int[] index)
    {
        long value = data.getInt(index[0]);
        index[0] += 4;
        return value & 0xFFFFFFFFL;
    }

    public static void setInt16(short value, java.nio.ByteBuffer data)
    {
        data.putShort(value);
    }

    public static void setInt32(int value, java.nio.ByteBuffer data)
    {
        data.putInt(value);
    }

    public static void setUInt16(short value, java.nio.ByteBuffer data)
    {
        data.putShort(value);
    }

    public static void setUInt32(int value, java.nio.ByteBuffer data)
    {
        data.putInt(value);
    }
    
    public static void setUInt64(long value, java.nio.ByteBuffer data)
    {
        data.putLong(value);
    }

    /** 
     Get object count.


     If first byte > 0x80 it will tell bytes count.

     @param buff
     @param index
     @return 
    */
    public static int getObjectCount(byte[] buff, int[] index)
    {
        int cnt = buff[index[0]++] & 0xFF;
        if (cnt > 0x80)
        {
            int tmp = cnt;
            cnt = 0;
            for (int pos = tmp - 0x81; pos > -1; --pos)
            {
                cnt += (buff[index[0]++] & 0xFF) << (8 * pos);
            }
        }
        return cnt;
    }    

    /** 
     Set item count.

     @param count
     @param buff
    */
    public static void setObjectCount(int count, ByteArrayOutputStream buff)            
    {
        if (count > 0x7F)
        {
            int cnt = 0;
            while (count >> (7 * ++cnt) > 0)
            {
                
            }
            buff.write((byte)(0x80 + cnt));
            java.nio.ByteBuffer tmp = null;
            if(cnt == 2)
            {
                tmp = java.nio.ByteBuffer.allocate(2);
                tmp.putShort((short)count);
            }
            else if(cnt == 3)
            {
                tmp = java.nio.ByteBuffer.allocate(3);
                tmp.put((byte)(count >> 16));
                tmp.put((byte)((count >> 8) & 0xFF));
                tmp.put((byte) (count & 0xFF));
            }
            else if(cnt == 4)
            {
                tmp = java.nio.ByteBuffer.allocate(4);
                tmp.putInt((int)count);
            }
            else
            {
                throw new RuntimeException("Invalid object count size.");
            }
            try
            {
                buff.write(tmp.array());
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
        }
        else
        {
            buff.write(count);
        }
    }
            
    /** 
     Set item count.

     @param count
     @param buff
    */
    public static void setObjectCount(int count, java.nio.ByteBuffer buff)
    {
        if (count > 0x7F)
        {
            int cnt = 0;
            while (count >> (7 * ++cnt) > 0)
            {
            }
            buff.put((byte)(0x80 + cnt));
            java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(1);
            if(cnt == 2)
            {
                tmp.putShort((short)count);
            }
            else if(cnt == 4)
            {
                tmp.putInt((int)count);
            }
            buff.put(tmp);
        }
        else
        {
            buff.put((byte)count);
        }
    }

    /** 
     Get object count. If first byte > 0x80 it will tell bytes count.

     @param buff
     @param index
     @return 
    */
    public static int getObjectCount(java.nio.ByteBuffer buff, int[] index)
    {
        int cnt = buff.get(index[0]++) & 0xFF;
        if (cnt > 0x80)
        {
            int tmp = cnt;
            cnt = 0;
            for (int pos = tmp - 0x81; pos > -1; --pos)
            {
                cnt += (buff.get(index[0]++) & 0xFF) << (8 * pos);
            }
        }
        return cnt;
    }       
            
    /** 
     Compares, whether two given arrays are similar.

     @param arr1 First array to compare.
     @param index Starting index of table, for first array.
     @param arr2 Second array to compare.
     @return True, if arrays are similar. False, if the arrays differ.
    */
    public static boolean compare(byte[] arr1, int[] index, byte[] arr2)
    {
        if (arr1.length - index[0] < arr2.length)
        {
            return false;
        }
        int pos;
        for (pos = 0; pos != arr2.length; ++pos)
        {
            if (arr1[pos + index[0]] != arr2[pos])
            {
                return false;
            }
        }
        index[0] += pos;
        return true;
    }
    
      /**
     * Compares, whether two given arrays are similar.
     * @param arr1 First array to compare.
     * @param Starting index of table, for first array.
     * @param arr2 Second array to compare.
     * @return True, if arrays are similar. False, if the arrays differ.
     */
    public static boolean compare(java.nio.ByteBuffer arr1, byte[] arr2)
    {
        if (arr1.limit() - arr1.position() < arr2.length)
        {
            return false;
        }
        byte[] bytes = new byte[arr2.length];
        int originalPos = arr1.position();
        arr1.get(bytes);
        boolean ret = java.util.Arrays.equals(bytes, arr2);
        //If compare failed return original position.
        if (!ret)
        {
            arr1.position(originalPos);
        }
        return ret;
    }
    
    /**
     * Converts a value of type Unsigned Byte to Integer.
     * @param b The Unsigned Byte to convert.
     * @return The Integer value of the given Unsigned Byte.
     */
    public static int unsignedByteToInt(byte b)
    {
        return (int) b & 0xFF;
    }

    /**
     * Converts a value of type Word to Integer.
     * @param b The Word to convert. 
     * @return The Integer value of the given Word.
     */
    public static int wordToInt(int b)
    {
        return (int) b & 0xFFFF;
    }

    /**
     * Converts a value of type DWord to Long.
     * @param b The DWord to convert.
     * @return The Long value of the given DWord.
     */
    public static long dWordTolong(long b)
    {
        return (long) b & 0xFFFFFFFF;
    }

    /**
     * Converts a value of type UInt64 to Integer.
     * @param v The UInt64 to convert.
     * @return The Integer value of the given UInt64.
     */
    public static java.math.BigInteger uint64ToInt(long v)
    {
        java.math.BigInteger vds = java.math.BigInteger.valueOf(v);
        return vds;
    }

    /** 
     Reserved for internal use.
    */
    static void ToBitString(StringBuilder sb, byte value, int count)
    {
        if (count > 8)
        {
            count = 8;
        }
        char[] data = new char[count];
        for (int pos = 0; pos != count; ++pos)
        {
            if ((value & (1 << pos)) != 0)
            {
                data[count - pos - 1] = '1';
            }
            else
            {
                data[count - pos - 1] = '0';
            }
        }
        sb.append(data);
    }
    /** 
     Reserved for internal use.
    */
    public static Object getData(byte[] buff, int[] pos, int action, int[] count, int[] index, DataType[] type, int[] cachePosition)
    {
        count[0] = 0;
        index[0] = 0;
        Object value = null;
        if (pos[0] == buff.length)
        {
            pos[0] = -1;
            return null;
        }
        boolean knownType = type[0] != DataType.NONE;
        if (!knownType)
        {
            type[0] = DataType.forValue(buff[pos[0]++]);
        }
        if (type[0] == DataType.NONE)
        {
            return value;
        }
        if (pos[0] == buff.length)
        {
            pos[0] = -1;
            return null;
        }
        int size = buff.length - pos[0];
        if (type[0] == DataType.ARRAY || type[0] == DataType.STRUCTURE)
        {
            count[0] = GXCommon.getObjectCount(buff, pos);
            if (action == 2)//ActionType.COUNT)
            {
                return value; //Don't go further. Only object's count is resolved.
            }
            if (cachePosition[0] > pos[0])
            {
                pos[0] = cachePosition[0];
            }
            size = buff.length - pos[0];
            if (count[0] != 0 && size < 1)
            {
                pos[0] = -1;
                return null;
            }
            java.util.ArrayList<Object> arr = new java.util.ArrayList<Object>(count[0]);
            for (index[0] = 0; index[0] != count[0]; ++index[0])
            {
                DataType[] itemType = new DataType[]{DataType.NONE};
                int[] colCount = new int[1], colIndex = new int[1];
                int[] tmpPos = new int[1];
                Object tmp = getData(buff, pos, 0, 
                        colCount, colIndex, itemType, tmpPos);
                if (colCount[0] == colIndex[0] && pos[0] != -1)
                {
                    arr.add(tmp);
                }
                if (pos[0] == -1)
                {
                    break;
                }
                else
                {
                    cachePosition[0] = pos[0];
                }
            }
            if (index[0] == count[0] && pos[0] != -1)
            {
                cachePosition[0] = buff.length;
            }
            value = arr.toArray();
        }
        else if (type[0] == DataType.BOOLEAN)
        {
            value = buff[pos[0]++] != 0;
        }
        else if (type[0] == DataType.BITSTRING)
        {
            int oldPos = pos[0];
            int cnt = getObjectCount(buff, pos);
            size -= pos[0] - oldPos;            
            double t = cnt;
            t /= 8;
            if (cnt % 8 != 0)
            {
                ++t;
            }
            int byteCnt = (int)Math.floor(t);
            if (size < byteCnt) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            StringBuilder sb = new StringBuilder();
            while (cnt > 0)
            {
                ToBitString(sb, buff[pos[0]++], cnt);
                cnt -= 8;
            }
            value = sb.toString();
        }
        else if (type[0] == DataType.INT32)
        {
            if (size < 4) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.getInt32(buff, pos);
        }
        else if (type[0] == DataType.UINT32)
        {
            if (size < 4) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.getUInt32(buff, pos);
        }
        else if (type[0] == DataType.STRING)
        {
            int len;
            if (knownType)
            {
                len = buff.length;
            }
            else
            {
                len = GXCommon.getObjectCount(buff, pos);
                if (buff.length - pos[0] < len) //If there is not enought data available.
                {
                    pos[0] = -1;
                    return null;
                }
            }
            if (len > 0)
            {                
                try 
                {
                    value = new String(GXCommon.rawData(buff, pos, len), "ASCII");
                }
                catch (UnsupportedEncodingException ex) 
                {
                    throw new RuntimeException(ex.getMessage());
                }            
            }
        }
        else if (type[0] == DataType.STRING_UTF8)
        {
            int len;
            if (knownType)
            {
                len = buff.length;
            }
            else
            {
                len = GXCommon.getObjectCount(buff, pos);
                if (buff.length - pos[0] < len) //If there is not enought data available.
                {
                    pos[0] = -1;
                    return null;
                }
            }
            if (len > 0)
            {
                try 
                {
                    value = new String(GXCommon.rawData(buff, pos, len), "UTF-8");
                }
                catch (UnsupportedEncodingException ex) 
                {
                    throw new RuntimeException(ex.getMessage());
                }                
            }
        }
        //Example Logical name is octet string, so do not change to string...
        else if (type[0] == DataType.OCTET_STRING)
        {
            int len;
            if (knownType)
            {
                len = buff.length;
            }
            else
            {
                len = GXCommon.getObjectCount(buff, pos);
                if (buff.length - pos[0] < len) //If there is not enought data available.
                {
                    pos[0] = -1;
                    return null;
                }
            }
            value = GXCommon.rawData(buff, pos, len);
        }
        else if (type[0] == DataType.BCD)
        {
            int len;
            if (knownType)
            {
                len = buff.length;
            }
            else
            {
                len = GXCommon.getObjectCount(buff, pos);
            }
            StringBuilder bcd = new StringBuilder(len * 2);
            for (int a = 0; a != len; ++a)
            {
                int idHigh = buff[pos[0]] >>> 4;
                int idLow = buff[pos[0]] & 0x0F;
                ++pos[0];
                bcd.append(String.format("%1X%1X", idHigh, idLow));
            }
            value = bcd.toString();
        }
        else if (type[0] == DataType.INT8)
        {
            value = (byte)buff[pos[0]++];
        }
        else if (type[0] == DataType.INT16)
        {
            if (size < 2) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.getInt16(buff, pos);
        }
        else if (type[0] == DataType.UINT8)
        {
            value = buff[pos[0]++] & 0xFF;
        }
        else if (type[0] == DataType.UINT16)
        {
            if (size < 2) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.getUInt16(buff, pos);
        }
        else if (type[0] == DataType.COMPACTARRAY)
        {
            throw new RuntimeException("Invalid data type.");
        }
        else if (type[0] == DataType.INT64)
        {
            if (size < 8) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.getInt64(buff, pos);
        }
        else if (type[0] == DataType.UINT64)
        {
            if (size < 8) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.getUInt64(buff, pos);
        }
        else if (type[0] == DataType.ENUM)
        {
            if (size < 1) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = buff[pos[0]++];
        }
        else if (type[0] == DataType.FLOAT32)
        {
            if (size < 4) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.toFloat(buff, pos);
        }
        else if (type[0] == DataType.FLOAT64)
        {
            if (size < 8) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            value = GXCommon.toDouble(buff, pos);
        }
        else if (type[0] == DataType.DATETIME)
        {      
            //If there is not enought data available.
            if (size - pos[0] < 12)
            {
                pos[0] = -1;
                return null;
            }            
            //Get year.
            int year = GXCommon.getUInt16(buff, pos);
            //Get month
            int month = buff[pos[0]++];
            //Get day
            int day = buff[pos[0]++];
            //Skip week day
            pos[0]++;
            //Get time.
            int hour = buff[pos[0]++];
            int minute = buff[pos[0]++];
            int second = buff[pos[0]++];
            int ms = buff[pos[0]++] & 0xFF;
            if (ms != 0xFF)
            {
                ms *= 10;
            }
            else
            {
                ms = 0;
            }
            int deviation = GXCommon.getInt16(buff, pos);  
            ClockStatus status = ClockStatus.forValue(buff[pos[0]++] & 0xFF);                                                            
            GXDateTime dt = new GXDateTime();            
            dt.setStatus(status);
            java.util.Set<DateTimeSkips> Skip = EnumSet.noneOf(DateTimeSkips.class);
            if (year < 1 || year == 0xFFFF)
            {
                Skip.add(DateTimeSkips.YEAR);
                java.util.Calendar tm = java.util.Calendar.getInstance();
                year = tm.get(Calendar.YEAR);            
            }
            dt.setDaylightSavingsBegin(month == 0xFE);
            dt.setDaylightSavingsEnd(month == 0xFD);
            if (month < 1 || month > 12)
            {
                Skip.add(DateTimeSkips.MONTH);
                month = 0;
            }
            else
            {
                month -= 1;        
            }
            if (day == -1 || day == 0 || day > 31)
            {
                Skip.add(DateTimeSkips.DAY);
                day = 1;
            }
            else if (day < 0)
            {            
                Calendar cal = Calendar.getInstance();
                day = cal.getActualMaximum(Calendar.DATE) + day + 3;
            }
            if (hour < 0 || hour > 24)
            {
                Skip.add(DateTimeSkips.HOUR);
                hour = 0;
            }
            if (minute < 0 || minute > 60 )
            {
                Skip.add(DateTimeSkips.MINUTE);
                minute = 0;
            }        
            if (second < 0 || second > 60)
            {
                Skip.add(DateTimeSkips.SECOND);
                second = 0;
            }
            //If ms is Zero it's skipped.
            if (ms < 1 || ms > 1000)
            {
                Skip.add(DateTimeSkips.MILLISECOND);
                ms = 0;
            }          
            java.util.Calendar tm;
            if ((deviation & 0xFFFF) != 0x8000)
            {
                tm = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));            
                tm.add(java.util.Calendar.MINUTE, deviation);                                            
            }
            else
            {
                tm = java.util.Calendar.getInstance();
            }            
            tm.set(year, month, day, hour, minute, second);       
            if (ms != 0)
            {
                tm.set(Calendar.MILLISECOND, ms);
            }        
            dt.setValue(tm.getTime());
            dt.setSkip(Skip);
            value = dt;
        }
        else if (type[0] == DataType.DATE)
        {
            if (knownType)
            {
                if (size < 5) //If there is not enought data available.
                {
                    pos[0] = -1;
                    return null;
                }                
            }
            else if (size < 6) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            //Get year.
            int year = GXCommon.getUInt16(buff, pos);
            //Get month
            int month = buff[pos[0]++];
            //Get day
            int day = buff[pos[0]++];
            //Skip week day
            pos[0]++;
            GXDateTime dt = new GXDateTime(year, month, day, -1, -1, -1, -1);
            value = dt;                            
        }
        else if (type[0] == DataType.TIME)
        {
            if (knownType)
            {
                if (size < 4) //If there is not enought data available.
                {
                    pos[0] = -1;
                    return null;
                }
            }
            else if (size < 5) //If there is not enought data available.
            {
                pos[0] = -1;
                return null;
            }
            //Get time.
            int hour = buff[pos[0]++];
            int minute = buff[pos[0]++];
            int second = buff[pos[0]++];            
            int ms = buff[pos[0]++];
            GXDateTime dt = new GXDateTime(-1, -1, -1, hour, minute, second, ms);
            value = dt;
        }
        else
        {
            throw new RuntimeException("Invalid data type.");
        }
        return value;
    }    
            
    /** 
     Reserved for internal use.

     @param buff
     @param type
     @param value
    */
    public static void setData(ByteArrayOutputStream buff, DataType type, Object value)            
    {
        try
        {
            //If value is enum get integer value.
            if (value instanceof Enum)
            {
                throw new RuntimeException("Value can't be enum. Give integer value.");
            }
            if (type == DataType.OCTET_STRING && (value instanceof GXDateTime 
                    || value instanceof Date))
            {
                type = DataType.DATETIME;
            }
            if (type == DataType.DATETIME ||
                    type == DataType.DATE ||
                    type == DataType.TIME)
            {
                buff.write(DataType.OCTET_STRING.getValue());
            }
             //If byte array is added do not add type.
            else if ((type == DataType.ARRAY || type == DataType.STRUCTURE) && value instanceof byte[])
            {
                buff.write((byte[]) value);
                return;
            }
            else
            {
                buff.write(type.getValue());
            }
            if (type == DataType.NONE)
            {
                return;
            }
            if (type == DataType.BOOLEAN)
            {
                if (Boolean.parseBoolean(value.toString()))
                {
                    buff.write(1);
                }
                else
                {
                    buff.write(0);
                }
            }
            else if (type == DataType.INT8 || type == DataType.UINT8 ||
                type == DataType.ENUM)
            {
                buff.write(((Number)value).byteValue());
            }
            else if (type == DataType.INT16 || type == DataType.UINT16)
            {
                java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(2);
                tmp.putShort(((Number)value).shortValue());
                buff.write(tmp.array());
            }
            else if (type == DataType.INT32 || type == DataType.UINT32)
            {
                java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(4);
                tmp.putInt(((Number)value).intValue());
                buff.write(tmp.array());
            }
            else if (type == DataType.INT64 || type == DataType.UINT64)
            {
                java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(8);
                tmp.putLong(((Number)value).longValue());
                buff.write(tmp.array());            
            }
            else if (type == DataType.FLOAT32)
            {
                java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(4);
                tmp.putFloat(((Number)value).floatValue());
                buff.write(tmp.array());
            }
            else if (type == DataType.FLOAT64)
            {
                java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(8);
                tmp.putDouble(((Number)value).doubleValue());
                buff.write(tmp.array());
            }
            else if (type == DataType.BITSTRING)
            {
                if (value instanceof String)
                {
                    byte val = 0;
                    int index = 0;
                    String str = new StringBuilder((String) value).reverse().toString();
                    setObjectCount(str.length(), buff);
                    for (char it : str.toCharArray())
                    {
                        if (it == '1')
                        {
                            val |= (byte)(1 << index++);
                        }
                        else if (it == '0')
                        {
                            index++;
                        }
                        else 
                        {
                            throw new RuntimeException("Not a bit string.");
                        }
                        if (index == 8)
                        {
                            index = 0;
                            buff.write(val);
                            val = 0;
                        }
                    }
                    if (index != 0)
                    {
                        buff.write(val);
                    }
                }
                else if (value instanceof byte[])
                {
                    byte[] arr = (byte[]) value;
                    setObjectCount(arr.length, buff);
                    buff.write(arr);
                }
                else if (value == null)
                {
                    buff.write(0);
                }
                else
                {
                    throw new RuntimeException("BitString must give as string.");
                }
            }
            else if (type == DataType.STRING)
            {
                if (value != null)
                {
                    String str = value.toString();
                    setObjectCount(str.length(), buff);
                    buff.write(str.getBytes("ASCII"));                 
                }
                else
                {
                    setObjectCount(0, buff);
                }
            }
            else if (type == DataType.STRING_UTF8)
            {
                if (value != null)
                {
                    String str = value.toString();
                    setObjectCount(str.length(), buff);
                    buff.write(str.getBytes("UTF-8"));                 
                }
                else
                {
                    setObjectCount(0, buff);
                }
            }

            //Excample Logical name is octet string, so do not change to string...
            else if (type == DataType.OCTET_STRING)
            {
                if (value instanceof String)
                {
                    String[] items = ((String)value).split("[.]", -1);
                    setObjectCount(items.length, buff);
                    for (String it : items)
                    {
                        buff.write(Integer.parseInt(it));
                    }
                }
                else if (value instanceof byte[])
                {
                    setObjectCount(((byte[])value).length, buff);
                    buff.write((byte[])value);
                }
                else if (value == null)
                {
                    setObjectCount(0, buff);
                }
                else
                {
                     throw new RuntimeException("Invalid data type.");
                }
            }
            else if (type == DataType.ARRAY || type == DataType.STRUCTURE)
            {
                if (value != null)
                {
                    int len = Array.getLength(value);
                    setObjectCount(len, buff);                
                    for (int pos = 0; pos != len; ++pos)
                    {
                        Object it = Array.get(value, pos);
                        setData(buff, getValueType(it), it);
                    }
                }
                else
                {
                    setObjectCount(0, buff);
                }
            }        
            else if (type == DataType.BCD)
            {
                if (!(value instanceof String))
                {
                    throw new RuntimeException("BCD value must give as string.");
                }
                String str = value.toString().trim();
                int len = str.length();
                if (len % 2 != 0)
                {
                    str = "0" + str;
                    ++len;
                }
                len /= 2;
                buff.write(len);
                for (int pos = 0; pos != len; ++pos)
                {
                    int ch1 = Integer.parseInt(str.substring(2 * pos, 2 * pos + 1));
                    int ch2 = Integer.parseInt(str.substring(2 * pos + 1, 2 * pos + 1 + 1));
                    buff.write((byte)(ch1 << 4 | ch2));
                }
            }
            else if (type == DataType.COMPACTARRAY)
            {
                throw new RuntimeException("Invalid data type.");
            }
            else if (type == DataType.DATETIME)
            {
                GXDateTime dt;
                if (value instanceof GXDateTime)
                {
                    dt = (GXDateTime) value;
                }
                else if (value instanceof java.util.Date)
                {
                    dt = new GXDateTime((java.util.Date) value);
                }
                else if(value instanceof java.util.Calendar)
                {
                    dt = new GXDateTime(((java.util.Calendar) value).getTime());
                }
                else if (value instanceof String)
                {
                    DateFormat f = new SimpleDateFormat();
                    try
                    {                        
                        dt = new GXDateTime(f.parse(value.toString()));
                    }
                    catch(ParseException ex)
                    {
                        throw new RuntimeException("Invalid date time value." + ex.getMessage());
                    }
                }
                else
                {
                    throw new RuntimeException("Invalid date format.");
                }
                //Add size
                buff.write(12);
                java.util.Calendar tm = java.util.Calendar.getInstance();
                tm.setTime(dt.getValue());
 
                //Add year.
                if (dt.getSkip().contains(DateTimeSkips.YEAR))
                {
                    buff.write(0xFF);
                    buff.write(0xFF);
                }
                else
                {
                    java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(2);
                    tmp.putShort((short) tm.get(java.util.Calendar.YEAR));
                    buff.write(tmp.array());                        
                }
                //Add month
                if (dt.getDaylightSavingsEnd())
                {
                    buff.write(0xFD);
                }
                else if (dt.getDaylightSavingsBegin())
                {
                    buff.write(0xFE);
                }
                else if (dt.getSkip().contains(DateTimeSkips.MONTH))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write((tm.get(java.util.Calendar.MONTH) + 1));                
                }
                //Add day
                if (dt.getSkip().contains(DateTimeSkips.DAY))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.DATE));
                }
                //Week day is not spesified.
                buff.write(0xFF);
                //Add time.
                if (dt.getSkip().contains(DateTimeSkips.HOUR))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.HOUR_OF_DAY));                
                }
                if (dt.getSkip().contains(DateTimeSkips.MINUTE))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.MINUTE));            
                }
                if (dt.getSkip().contains(DateTimeSkips.SECOND))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.SECOND));                
                }
                if (dt.getSkip().contains(DateTimeSkips.MILLISECOND))
                {
                    //Hundredths of second is not used.
                    buff.write(0xFF);
                }
                else
                {
                    int ms = tm.get(java.util.Calendar.MILLISECOND);
                    if (ms != 0)
                    {
                        ms /= 10;
                    }
                    buff.write(ms);                
                }                
                //devitation not used.
                if (dt.getSkip().contains(DateTimeSkips.DEVITATION))
                {
                    buff.write(0x80);
                    buff.write(0x00);
                }
                else //Add devitation.
                {
                    short tmp = (short) -(tm.getTimeZone().getOffset(tm.getTime().getTime()) / 60000);
                    buff.write(tmp >> 8);
                    buff.write(tmp & 0xFF);                    
                }
                //Add clock_status
                buff.write(dt.getStatus().getValue());   
            }
            else if (type == DataType.DATE)
            {
                GXDateTime dt;
                if (value instanceof GXDateTime)
                {
                    dt = (GXDateTime) value;
                }
                else if (value instanceof java.util.Date)
                {
                    dt = new GXDateTime((java.util.Date) value);
                }
                else if(value instanceof java.util.Calendar)
                {
                    dt = new GXDateTime(((java.util.Calendar) value).getTime());
                }
                else if (value instanceof String)
                {
                    DateFormat f = new SimpleDateFormat();
                    dt = new GXDateTime(f.parse(value.toString()));
                }
                else
                {
                    throw new RuntimeException("Invalid date format.");
                }
                java.util.Calendar tm = java.util.Calendar.getInstance();
                tm.setTime(dt.getValue());                
                //Add size
                buff.write(5);
                //Add year.
                if (dt.getSkip().contains(DateTimeSkips.YEAR))
                {
                    buff.write(0xFF);
                    buff.write(0xFF);
                }
                else
                {
                    java.nio.ByteBuffer tmp = java.nio.ByteBuffer.allocate(2);
                    tmp.putShort((short) tm.get(java.util.Calendar.YEAR));
                    buff.write(tmp.array());
                }
                //Add month
                if (dt.getDaylightSavingsBegin())
                {
                    buff.write(0xFE);
                }
                else if (dt.getDaylightSavingsEnd())
                {
                    buff.write(0xFD);
                }
                else if (dt.getSkip().contains(DateTimeSkips.MONTH))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write((tm.get(java.util.Calendar.MONTH) + 1));               
                }
                //Add day
                if (dt.getSkip().contains(DateTimeSkips.DAY))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.DATE));                
                }
                //Week day is not spesified.
                buff.write(0xFF);
            }
            else if (type == DataType.TIME)
            {
                java.util.Set<DateTimeSkips> skip = EnumSet.noneOf(DateTimeSkips.class);
                java.util.Calendar tm = java.util.Calendar.getInstance();
                if (value instanceof GXDateTime)
                {
                    GXDateTime tmp = (GXDateTime) value;
                    tm.setTime(tmp.getValue());
                    skip = tmp.getSkip();
                }
                else if (value instanceof java.util.Date)
                {
                    tm.setTime((java.util.Date) value);
                }
                else if(value instanceof java.util.Calendar)
                {
                    tm.setTime(((java.util.Calendar) value).getTime());
                }
                else if (value instanceof String)
                {
                    DateFormat f = new SimpleDateFormat();
                    try
                    {                        
                        tm.setTime(f.parse(value.toString()));
                    }
                    catch(ParseException ex)
                    {
                        throw new RuntimeException("Invalid date time value.\r\n" + 
                                ex.getMessage());
                    }
                }
                else
                {
                    throw new RuntimeException("Invalid date format.");
                }
                //Add size
                buff.write(4);
                //Add time.
                if (skip.contains(DateTimeSkips.HOUR))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.HOUR_OF_DAY));
                }
                if (skip.contains(DateTimeSkips.MINUTE))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.MINUTE));
                }
                if (skip.contains(DateTimeSkips.SECOND))
                {
                    buff.write(0xFF);
                }
                else
                {
                    buff.write(tm.get(java.util.Calendar.SECOND));
                }
                buff.write(0xFF); //Hundredths of second is not used.               
            }
            else
            {
                throw new RuntimeException("Invalid data type.");
            }
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }
            
    /** 
     Reserved for internal use.

     @param buff
     @param type
     @param value
    */
    public static void setData(java.nio.ByteBuffer buff, DataType type, Object value)    
    {
        if (value != null && type == DataType.NONE)
        {
            type = getValueType(value);
        }
        //If value is enum get integer value.
        if (value instanceof Enum)
        {
            throw new RuntimeException("Value can't be enum. Give integer value.");
        }
        if (type == DataType.DATETIME ||
                type == DataType.DATE ||
                type == DataType.TIME)
        {
            buff.put((byte) DataType.OCTET_STRING.getValue());
        }
        //If byte array is added do not add type.
        else if ((type == DataType.ARRAY && value instanceof byte[]))
        {
            buff.put((byte[]) value);
            return;
        }
        else 
        {                        
            buff.put((byte) type.getValue());         
        }
        
        if (type == DataType.NONE)
        {
            return;
        }
        if (type == DataType.BOOLEAN)
        {
            if (Boolean.parseBoolean(value.toString()))
            {
                buff.put((byte) 1);
            }
            else
            {
                buff.put((byte) 0);
            }
        }
        else if (type == DataType.INT8 || type == DataType.UINT8 ||
            type == DataType.ENUM)
        {
            buff.put(((Number)value).byteValue());
        }
        else if (type == DataType.INT16 || type == DataType.UINT16)
        {
            buff.putShort(((Number)value).shortValue());
        }
        else if (type == DataType.INT32 || type == DataType.UINT32)
        {
            buff.putInt(((Number)value).intValue());
        }
        else if (type == DataType.INT64 || type == DataType.UINT64)
        {
            buff.putLong(((Number)value).longValue());
        }
        else if (type == DataType.FLOAT32)
        {
            buff.putFloat(((Number)value).floatValue());
        }
        else if (type == DataType.FLOAT64)
        {
            buff.putDouble(((Number)value).doubleValue());
        }
        else if (type == DataType.BITSTRING)
        {
            throw new RuntimeException("Invalid data type.");
        }
        else if (type == DataType.STRING)
        {
            if (value != null)
            {
                String str = value.toString();
                setObjectCount(str.length(), buff);
                try
                {
                    buff.put(str.getBytes("ASCII"));                 
                }
                catch(Exception ex)
                {
                    throw new RuntimeException(ex.getLocalizedMessage());
                }
            }
            else
            {
                setObjectCount(0, buff);
            }
        }
        //Excample Logical name is octet string, so do not change to string...
        else if (type == DataType.OCTET_STRING)
        {
            if (value instanceof String)
            {
                String[] items = ((String)value).split("[.]", -1);
                setObjectCount(items.length, buff);
                for (String it : items)
                {
                    buff.put((byte) Integer.parseInt(it));
                }
            }
            else if (value instanceof byte[])
            {
                setObjectCount(((byte[])value).length, buff);
                buff.put((byte[])value);
            }
            else
            {
                 throw new RuntimeException("Invalid data type.");
            }
        }
        else if (type == DataType.ARRAY || type == DataType.STRUCTURE)
        {
            if (value != null)
            {
                int len = Array.getLength(value);
                setObjectCount(len, buff);                
                for (int pos = 0; pos != len; ++pos)
                {
                    Object it = Array.get(value, pos);
                    setData(buff, getValueType(it), it);
                }
            }
            else
            {
                setObjectCount(0, buff);
            }
        }        
        else if (type == DataType.BCD)
        {
            if (!(value instanceof String))
            {
                throw new RuntimeException("BCD value must give as string.");
            }
            String str = value.toString().trim();
            int len = str.length();
            if (len % 2 != 0)
            {
                str = "0" + str;
                ++len;
            }
            len /= 2;
            buff.put((byte)(len));
            for (int pos = 0; pos != len; ++pos)
            {
                int ch1 = Integer.parseInt(str.substring(2 * pos, 2 * pos + 1));
                int ch2 = Integer.parseInt(str.substring(2 * pos + 1, 2 * pos + 1 + 1));
                buff.put((byte)(ch1 << 4 | ch2));
            }
        }
        else if (type == DataType.COMPACTARRAY)
        {
            throw new RuntimeException("Invalid data type.");
        }
        else if (type == DataType.DATETIME)
        {
            GXDateTime dt;            
            if (value instanceof GXDateTime)
            {
                dt = (GXDateTime) value;
            }
            else if (value instanceof java.util.Date)
            {
                dt = new GXDateTime((java.util.Date) value);                
            }
            else if(value instanceof java.util.Calendar)
            {
                dt = new GXDateTime(((java.util.Calendar) value).getTime());
            }
            else if (value instanceof String)
            {
                DateFormat f = new SimpleDateFormat();
                try
                {                        
                    dt = new GXDateTime(f.parse(value.toString()));
                }
                catch(ParseException ex)
                {
                    throw new RuntimeException("Invalid date time value." + ex.getMessage());
                }
            }
            else
            {
                throw new RuntimeException("Invalid date format.");
            }
            java.util.Calendar tm = java.util.Calendar.getInstance();
            tm.setTime(dt.getValue());
            //Add size
            buff.put((byte) 12);
            //Add year.
            buff.putShort((short) tm.get(java.util.Calendar.YEAR));
            //Add month
            buff.put((byte) (tm.get(java.util.Calendar.MONTH) + 1));
            //Add day
            buff.put((byte) tm.get(java.util.Calendar.DATE));
            //Week day is not spesified.
            buff.put((byte) 0xFF);
            //Add time.
            buff.put((byte) tm.get(java.util.Calendar.HOUR_OF_DAY));
            buff.put((byte) tm.get(java.util.Calendar.MINUTE));
            buff.put((byte) tm.get(java.util.Calendar.SECOND));
            buff.put((byte) 0xFF); //Hundredths of second is not used.
            //If devitation is not used.
            if (dt.getSkip().contains(DateTimeSkips.DEVITATION))
            {                
                buff.put((byte) 0x80);
                buff.put((byte) 0x00);
            }
            else
            {
                short tmp = (short) -(tm.getTimeZone().getOffset(tm.getTime().getTime()) / 60000);
                buff.put((byte) (tmp >> 8));
                buff.put((byte) (tmp & 0xFF));
            }
            //Add clock_status
            buff.put((byte) dt.getStatus().getValue());   
        }
        else if (type == DataType.DATE)
        {
            java.util.Calendar tm = java.util.Calendar.getInstance();
            if (value instanceof java.util.Date)
            {
                tm.setTime((java.util.Date) value);
            }
            else if(value instanceof java.util.Calendar)
            {
                tm.setTime(((java.util.Calendar) value).getTime());
            }
            else if (value instanceof String)
            {
                DateFormat f = new SimpleDateFormat();
                try
                {
                    tm.setTime(f.parse(value.toString()));
                }
                catch(Exception ex)
                {
                    throw new RuntimeException(ex.getLocalizedMessage());
                }
            }
            else
            {
                throw new RuntimeException("Invalid date format.");
            }
            //Add size
            buff.put((byte) 5);
            //Add year.
            buff.putShort((short) tm.get(java.util.Calendar.YEAR));
            //Add month
            buff.put((byte) (tm.get(java.util.Calendar.MONTH) + 1));
            //Add day
            buff.put((byte) tm.get(java.util.Calendar.DATE));
            //Week day is not spesified.
            buff.put((byte) 0xFF);
        }
        else if (type == DataType.TIME)
        {
            java.util.Calendar tm = java.util.Calendar.getInstance();
            if (value instanceof java.util.Date)
            {
                tm.setTime((java.util.Date) value);
            }
            else if(value instanceof java.util.Calendar)
            {
                tm.setTime(((java.util.Calendar) value).getTime());
            }
            else if (value instanceof String)
            {
                DateFormat f = new SimpleDateFormat();
                try
                {                        
                    tm.setTime(f.parse(value.toString()));
                }
                catch(ParseException ex)
                {
                    throw new RuntimeException("Invalid date time value.\r\n" + 
                            ex.getMessage());
                }
            }
            else
            {
                throw new RuntimeException("Invalid date format.");
            }
            //Add size
            buff.put((byte) 7);
            //Add time.
            buff.put((byte) tm.get(java.util.Calendar.HOUR_OF_DAY));
            buff.put((byte) tm.get(java.util.Calendar.MINUTE));
            buff.put((byte) tm.get(java.util.Calendar.SECOND));
            buff.put((byte) 0xFF); //Hundredths of second is not used.
            //Add deviation (Not used).
            buff.put((byte) 0x80);
            buff.put((byte) 0x00);
            //Add clock_status
            buff.put((byte) 0xFF);
        }
        else
        {
            throw new RuntimeException("Invalid data type.");
        }
    }
    
    public static DataType getValueType(Object value)
    {
        if (value == null)
        {
            return DataType.NONE;
        }
        if (value instanceof byte[])
        {
            return DataType.OCTET_STRING;
        }
        if (value instanceof Enum)
        {
            return DataType.ENUM;
        }
        if (value instanceof Byte)
        {
            return DataType.INT8;
        }
        if (value instanceof Short)
        {
            return DataType.INT16;
        }
        if (value instanceof Integer)
        {
            return DataType.INT32;
        }
        if (value instanceof Long)
        {
            return DataType.INT64;
        }
        if (value instanceof java.util.Date ||
            value instanceof GXDateTime)
        {
            return DataType.DATETIME;
        }
        if (value.getClass().isArray())
        {
            return DataType.ARRAY;
        }
        if (value instanceof String)
        {
            return DataType.STRING;
        }
        if (value instanceof Boolean)
        {
            return DataType.BOOLEAN;
        }
        if (value instanceof Float)
        {
            return DataType.FLOAT32;
        }
        if (value instanceof Double)
        {
            return DataType.FLOAT64;
        }
        throw new RuntimeException("Invalid value.");
    }
}