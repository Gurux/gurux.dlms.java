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

//
// --------------------------------------------------------------------------
package gurux.dlms;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import gurux.dlms.enums.DataType;
import gurux.dlms.internal.GXCommon;

/**
 * Byte array class is used to save received bytes.
 * 
 * @author Gurux Ltd.
 */
public class GXByteBuffer {
    private static final int ARRAY_CAPACITY = 10;

    /**
     * Byte array data.
     */
    private byte[] data;
    /**
     * Size of received bytes.
     */
    private int size;
    /**
     * Position of byte array.
     */
    private int position;

    /**
     * Constructor.
     */
    public GXByteBuffer() {

    }

    /**
     * Constructor.
     * 
     * @param capacity
     *            Buffer capacity.
     */
    public GXByteBuffer(final int capacity) {
        capacity(capacity);
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Byte array to attach.
     */
    public GXByteBuffer(final byte[] value) {
        capacity(value.length);
        set(value);
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Byte array to attach.
     */
    public GXByteBuffer(final GXByteBuffer value) {
        capacity(value.size - value.position);
        set(value);
    }

    /**
     * Clear buffer but do not release memory.
     */
    public final void clear() {
        size(0);
    }

    /**
     * Allocate new size for the array in bytes.
     * 
     * @param capacity
     *            Buffer capacity.
     */
    public final void capacity(final int capacity) {
        if (capacity == 0) {
            setData(null);
            size = 0;
            position = 0;
        } else {
            if (getData() == null) {
                setData(new byte[capacity]);
            } else {
                byte[] tmp = getData();
                setData(new byte[capacity]);
                if (size < capacity) {
                    System.arraycopy(tmp, 0, getData(), 0, size);
                } else {
                    System.arraycopy(tmp, 0, getData(), 0, capacity);
                    size(capacity);
                }
            }
        }
    }

    /**
     * Buffer capacity.
     * 
     * @return Buffer capacity.
     */
    public final int capacity() {
        if (getData() == null) {
            return 0;
        }
        return getData().length;
    }

    /**
     * @param value
     *            Buffer position.
     */
    public final void position(final int value) {
        if (value < 0 || value > size()) {
            throw new IllegalArgumentException("position");
        }
        position = value;
    }

    /**
     * @return Buffer position.
     */
    public final int position() {
        return position;
    }

    /**
     * @return Buffer size.
     */
    public final int size() {
        return size;
    }

    /**
     * @param value
     *            Buffer size.
     */
    public final void size(final int value) {
        if (value < 0 || value > capacity()) {
            throw new IllegalArgumentException("size");
        }
        size = value;
        if (position > size) {
            position = size;
        }
    }

    /**
     * @return Amount of non read bytes in the buffer.
     */
    public int available() {
        return size - position;
    }

    /**
     * @return Get buffer data as byte array.
     */
    public final byte[] array() {
        return subArray(0, size);
    }

    /**
     * Returns sub array from byte buffer.
     * 
     * @param index
     *            Start index.
     * @param count
     *            Byte count.
     * @return Sub array.
     */
    public final byte[] subArray(final int index, final int count) {
        byte[] tmp = new byte[count];
        if (count != 0) {
            System.arraycopy(data, index, tmp, 0, count);
        }
        return tmp;
    }

    /**
     * Move content from source to destination.
     * 
     * @param srcPos
     *            Source position.
     * @param destPos
     *            Destination position.
     * @param count
     *            Item count.
     */
    public final void move(final int srcPos, final int destPos, final int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count");
        }
        if (count != 0) {
            System.arraycopy(data, srcPos, data, destPos, count);
            size((destPos + count));
            if (position > size) {
                position(size);
            }
        }
    }

    /**
     * Remove handled bytes. This can be used in debugging to remove handled
     * bytes.
     */
    public final void trim() {
        if (size == position) {
            size(0);
        } else {
            move(position, 0, size - position);
        }
        position(0);
    }

    /**
     * Push the given byte into this buffer at the current position, and then
     * increments the position.
     * 
     * @param item
     *            The byte to be added.
     */
    public final void setUInt8(final int item) {
        setUInt8(size, item);
        ++size;
    }

    /**
     * Push the given data type into this buffer at the current position, and
     * then increments the position.
     * 
     * @param item
     *            The UInt8 value to be added.
     */
    public final void setUInt8(final DataType item) {
        setUInt8(item.getValue());
    }

    /**
     * Set the UInt8 value into this buffer for given position, and then
     * increments the position.
     * 
     * @param index
     *            Buffer index where value is set.
     * @param item
     *            The UInt8 value to be added.
     */
    public final void setUInt8(final int index, final int item) {
        if (index >= capacity()) {
            capacity(index + ARRAY_CAPACITY);
        }
        data[index] = (byte) item;
    }

    /**
     * Push the UInt16 value into this buffer at the current position, and then
     * increments the position.
     * 
     * @param item
     *            The UInt16 value to be added.
     */
    public final void setUInt16(final int item) {
        setUInt16(size, item);
        size += 2;
    }

    /**
     * Set the UInt16 value into this buffer for the given position, and then
     * increments the position.
     * 
     * @param index
     *            Buffer index where value is set.
     * @param item
     *            The UInt16 value to be added.
     */
    public final void setUInt16(final int index, final int item) {
        if (index + 2 >= capacity()) {
            capacity(index + ARRAY_CAPACITY);
        }
        data[index] = (byte) ((item >> 8) & 0xFF);
        data[index + 1] = (byte) (item & 0xFF);
    }

    /**
     * Push the UInt32 value into this buffer at the current position, and then
     * increments the position.
     * 
     * @param item
     *            The UInt32 value to be added.
     */
    public final void setUInt32(final long item) {
        setUInt32(size, item);
        size += 4;
    }

    /**
     * Set the UInt32 value into this buffer for the given position, and then
     * increments the position.
     * 
     * @param index
     *            Buffer index where value is set.
     * @param item
     *            The UInt32 value to be added.
     */
    public final void setUInt32(final int index, final long item) {
        if (index + 4 >= capacity()) {
            capacity(index + ARRAY_CAPACITY);
        }
        data[index] = (byte) ((item >> 24) & 0xFF);
        data[index + 1] = (byte) ((item >> 16) & 0xFF);
        data[index + 2] = (byte) ((item >> 8) & 0xFF);
        data[index + 3] = (byte) (item & 0xFF);
    }

    /**
     * Push the UInt64 value into this buffer at the current position, and then
     * increments the position.
     * 
     * @param item
     *            The UInt64 value to be added.
     */
    public final void setUInt64(final long item) {
        setUInt64(size, item);
        size += 8;
    }

    /**
     * Set the UInt64 value into this buffer for the given position, and then
     * increments the position.
     * 
     * @param index
     *            Buffer index where value is set.
     * @param item
     *            The UInt64 value to be added.
     */
    public final void setUInt64(final int index, final long item) {
        if (index + 8 >= capacity()) {
            capacity(index + ARRAY_CAPACITY);
        }
        data[size] = (byte) ((item >> 56) & 0xFF);
        data[size + 1] = (byte) ((item >> 48) & 0xFF);
        data[size + 2] = (byte) ((item >> 40) & 0xFF);
        data[size + 3] = (byte) ((item >> 32) & 0xFF);
        data[size + 4] = (byte) ((item >> 24) & 0xFF);
        data[size + 5] = (byte) ((item >> 16) & 0xFF);
        data[size + 6] = (byte) ((item >> 8) & 0xFF);
        data[size + 7] = (byte) (item & 0xFF);
    }

    /**
     * Push the float value into this buffer at the current position, and then
     * increments the position.
     * 
     * @param value
     *            Float value to be added.
     */
    public final void setFloat(final float value) {
        setFloat(size, value);
        size += 4;
    }

    /**
     * Set the float value into this buffer for the given position, and then
     * increments the position.
     * 
     * @param index
     *            Buffer index where value is set.
     * @param value
     *            The float value to be added.
     */

    public final void setFloat(final int index, final float value) {
        int tmp = Float.floatToIntBits(value);
        setUInt32(index, tmp);
    }

    /**
     * Push the double value into this buffer at the current position, and then
     * increments the position.
     * 
     * @param value
     *            Double value to be added.
     */
    public final void setDouble(final double value) {
        setDouble(size, value);
        size += 8;
    }

    /**
     * Set the double value into this buffer for the given position, and then
     * increments the position.
     * 
     * @param index
     *            Buffer index where value is set.
     * @param value
     *            The double value to be added.
     */
    public final void setDouble(final int index, final double value) {
        long tmp = Double.doubleToLongBits(value);
        setUInt64(index, tmp);
    }

    /**
     * @return UInt8 value from byte buffer.
     */
    public final short getUInt8() {
        short value = getUInt8(position());
        ++position;
        return value;
    }

    /**
     * @return Int8 value from byte buffer.
     */
    public final byte getInt8() {
        return (byte) getUInt8();
    }

    /**
     * Get UInt8 value from byte buffer from the given index..
     * 
     * @param index
     *            Buffer index.
     * @return UInt8 value.
     */
    public final short getUInt8(final int index) {
        if (index >= size) {
            throw new IllegalArgumentException("getUInt8");
        }
        return (short) (data[index] & 0xFF);
    }

    /**
     * @return UInt16 value from byte buffer.
     */
    public final int getUInt16() {
        int value = getUInt16(position());
        position += 2;
        return value;
    }

    /**
     * @return Int16 value from byte buffer.
     */
    public final short getInt16() {
        return (short) getUInt16();
    }

    /**
     * Get UInt16 value from byte buffer from the given index..
     * 
     * @param index
     *            Buffer index.
     * @return UInt16 value.
     */
    public final int getUInt16(final int index) {
        if (index + 2 > size) {
            throw new IllegalArgumentException("getUInt16");
        }
        return ((data[index] & 0xFF) << 8) | (data[index + 1] & 0xFF);
    }

    /**
     * Get Int32 value from the current position.
     * 
     * @return Int32 value.
     */
    public final long getUInt32() {
        long value = getUInt32(position());
        position += 4;
        return value;
    }

    /**
     * Get UInt24 value from the current position.
     * 
     * @return Int32 value.
     */
    public final int getUInt24() {
        int value = getUInt24(position());
        position += 3;
        return value;
    }

    /**
     * Get Int32 value from the current position.
     * 
     * @return Int32 value.
     */
    public final int getInt32() {
        int value = getInt32(position());
        position += 4;
        return value;
    }

    /**
     * Get UInt32 value from byte buffer from the given index..
     * 
     * @param index
     *            Buffer index.
     * @return UInt32 value.
     */

    public final int getInt32(final int index) {
        if (index + 4 > size) {
            throw new IllegalArgumentException("getUInt32");
        }
        return (data[index] & 0xFF) << 24 | (data[index + 1] & 0xFF) << 16 | (data[index + 2] & 0xFF) << 8
                | (data[index + 3] & 0xFF);
    }

    /**
     * Get UInt24 value from byte buffer from the given index..
     * 
     * @param index
     *            Buffer index.
     * @return UInt24 value.
     */
    public final int getUInt24(final int index) {
        if (index + 3 > size) {
            throw new IllegalArgumentException("getUInt24");
        }
        return (data[index] & 0xFF) << 16 | (data[index + 1] & 0xFF) << 8 | (data[index + 2] & 0xFF);
    }

    /**
     * Get UInt32 value from byte buffer from the given index..
     * 
     * @param index
     *            Buffer index.
     * @return UInt32 value.
     */
    public final long getUInt32(final int index) {
        if (index + 4 > size) {
            throw new IllegalArgumentException("getUInt32");
        }
        long value = data[index] & 0xFF;
        value = value << 24;
        value |= (data[index + 1] & 0xFF) << 16;
        value |= (data[index + 2] & 0xFF) << 8;
        value |= (data[index + 3] & 0xFF);
        return value;
    }

    /**
     * @return Get float value from byte buffer.
     */
    public final float getFloat() {
        return Float.intBitsToFloat(getInt32());
    }

    /**
     * @return Get double value from byte buffer.
     */
    public final double getDouble() {
        return Double.longBitsToDouble(getInt64());
    }

    /**
     * @return Get Int64 value from byte buffer.
     */
    public final long getInt64() {
        long value = getInt64(position);
        position += 8;
        return value;
    }

    /**
     * Get Int64 value from byte buffer using the index.
     * 
     * @param index
     *            Byte index.
     * @return Int64 value.
     */
    public final long getInt64(final int index) {
        long value = (long) (data[index] & 0xFF) << 56;
        value |= (long) (data[index + 1] & 0xFF) << 48;
        value |= (long) (data[index + 2] & 0xFF) << 40;
        value |= (long) (data[index + 3] & 0xFF) << 32;
        value |= (long) (data[index + 4] & 0xFF) << 24;
        value |= (data[index + 5] & 0xFF) << 16;
        value |= (data[index + 6] & 0xFF) << 8;
        value |= (data[index + 7] & 0xFF);
        return value;
    }

    /**
     * @return the data
     */
    public final byte[] getData() {
        return data;
    }

    /**
     * @param value
     *            The data to set
     */
    public final void setData(final byte[] value) {
        data = value;
    }

    /**
     * @return Get UInt64 value from byte buffer.
     */
    public final BigInteger getUInt64() {
        BigInteger value = getUInt64(position);
        position += 8;
        return value;
    }

    /**
     * Get UInt64 value from byte buffer using index.
     * 
     * @param index
     *            Byte index.
     * @return UInt64 value.
     */
    public final BigInteger getUInt64(final int index) {
        long value = getInt64(index);
        BigInteger b = BigInteger.valueOf(value);
        if (b.compareTo(BigInteger.ZERO) < 0) {
            b = b.add(BigInteger.ONE.shiftLeft(64));
        }
        return b;
    }

    /**
     * Check is string ASCII string.
     * 
     * @param value
     *            String value.
     * @return Is ASCII string.
     */
    public static boolean isAsciiString(final String value) {
        if (value != null) {
            return StandardCharsets.US_ASCII.newEncoder().canEncode(value);
        }
        return true;
    }

    /**
     * Check is byte buffer ASCII string.
     * 
     * @param value
     *            Byte array.
     * @return Is ASCII string.
     */
    public static boolean isAsciiString(byte[] value) {
        if (value != null) {
            for (byte it : value) {
                if ((it < 32 || it > 127) && it != '\r' && it != '\n' && it != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get string value from byte buffer.
     * 
     * @param count
     *            Amount of the chars to get.
     * @return String value.
     */
    public final String getString(final int count) {
        String str = getString(position, count, "ASCII");
        position += count;
        return str;
    }

    /**
     * Get string value from byte buffer.
     * 
     * @param index
     *            Byte index.
     * @param count
     *            Amount of the chars to get.
     * @return String value.
     */
    public final String getString(final int index, final int count) {
        return getString(index, count, "ASCII");
    }

    /**
     * Get string value from byte buffer.
     * 
     * @param index
     *            Byte index.
     * @param count
     *            Amount of the chars to get.
     * @param charsetName
     *            charset name.
     * @return String value.
     */
    public final String getString(final int index, final int count, final String charsetName) {
        if (index + count > size) {
            throw new IllegalArgumentException("getString");
        }
        try {
            byte[] tmp = subArray(index, count);
            if (charsetName.equalsIgnoreCase("ASCII")) {
                if (isAsciiString(tmp)) {
                    return new String(getData(), index, count, charsetName).replaceAll("[\\x00]", "");
                } else {
                    return GXCommon.toHex(tmp, true);
                }
            } else {
                return new String(getData(), index, count, charsetName).replaceAll("[\\x00]", "");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Append the given byte array into this buffer.
     * 
     * @param value
     *            Data to append.
     */
    public final void set(final byte[] value) {
        if (value != null) {
            set(value, 0, value.length);
        }
    }

    /**
     * Push the given byte array into this buffer at the current position, and
     * then increments the position.
     * 
     * @param index
     *            Byte index.
     * @param value
     *            The value to be added.
     */
    public final void set(final int index, final byte[] value) {
        if (value != null) {
            move(index, value.length, size - index);
            set(value, index, value.length);
        }
    }

    /**
     * Set new value to byte array.
     * 
     * @param value
     *            Byte array to add.
     * @param index
     *            Byte index.
     * @param count
     *            Byte count.
     */
    public final void set(final byte[] value, final int index, final int count) {
        if (value != null && count != 0) {
            if (size + count > capacity()) {
                capacity(size + count + ARRAY_CAPACITY);
            }
            System.arraycopy(value, index, data, size, count);
            size += count;
        }
    }

    /**
     * @param value
     *            Set new value to byte array.
     */
    public final void set(final GXByteBuffer value) {
        if (value != null) {
            set(value, value.size() - value.position());
        }
    }

    /**
     * Set new value to byte array.
     * 
     * @param value
     *            Byte array to add.
     * @param count
     *            Byte count.
     */
    public final void set(final GXByteBuffer value, final int count) {
        if (size + count > capacity()) {
            capacity(size + count + ARRAY_CAPACITY);
        }
        if (count != 0) {
            System.arraycopy(value.data, value.position, data, size, count);
            size += count;
            value.position += count;
        }
    }

    /**
     * Add new object to the byte buffer.
     * 
     * @param value
     *            Value to add.
     */
    public final void add(final Object value) {
        if (value != null) {
            if (value instanceof byte[]) {
                set((byte[]) value);
            } else if (value instanceof Byte) {
                setUInt8(((Byte) value).intValue());
            } else if (value instanceof Short) {
                setUInt16(((Short) value).shortValue());
            } else if (value instanceof Integer) {
                setUInt32(((Integer) value).intValue());
            } else if (value instanceof Long) {
                setUInt64(((Long) value).longValue());
            } else if (value instanceof String) {
                try {
                    set(((String) value).getBytes("ASCII"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (value instanceof BigInteger) {
                set(((BigInteger) value).toByteArray());
            } else if (value instanceof Boolean) {
                if ((Boolean) value) {
                    setUInt8(1);
                } else {
                    setUInt8(0);
                }
            } else if (value instanceof GXUInt8) {
                setUInt16(((GXUInt8) value).shortValue());
            } else if (value instanceof GXUInt16) {
                setUInt16(((GXUInt16) value).shortValue());
            } else if (value instanceof GXUInt32) {
                setUInt32(((GXUInt32) value).longValue());
            } else {
                throw new RuntimeException("Invalid object type.");
            }
        }
    }

    /**
     * @param target
     *            get bytes from byte buffer.
     */
    public final void get(final byte[] target) {
        if (size - position < target.length) {
            throw new IllegalArgumentException("get");
        }
        System.arraycopy(getData(), position, target, 0, target.length);
        position += target.length;
    }

    /**
     * Compares, whether two given arrays are similar starting from current
     * position.
     * 
     * @param arr
     *            Array to compare.
     * @return True, if arrays are similar. False, if the arrays differ.
     */
    public final boolean compare(final byte[] arr) {
        if (size - position < arr.length) {
            return false;
        }
        byte[] bytes = new byte[arr.length];
        get(bytes);
        boolean ret = java.util.Arrays.equals(bytes, arr);
        if (!ret) {
            this.position -= arr.length;
        }
        return ret;
    }

    /**
     * Reverses the order of the given array.
     */
    public final void reverse() {
        if (size() == 0) {
            return;
        }
        int first = position;
        int last = size - 1;
        byte tmp;
        while (last > first) {
            tmp = data[last];
            data[last] = data[first];
            data[first] = tmp;
            --last;
            ++first;
        }
    }

    /**
     * Push the given hex string as byte array into this buffer at the current
     * position, and then increments the position.
     * 
     * @param value
     *            The hex string to be added.
     */
    public final void setHexString(final String value) {
        set(GXCommon.hexToBytes(value));
    }

    /**
     * Push the given hex string as byte array into this buffer at the current
     * position, and then increments the position.
     * 
     * @param index
     *            Byte index.
     * @param value
     *            The hex string to be added.
     */
    public final void setHexString(final int index, final String value) {
        set(index, GXCommon.hexToBytes(value));
    }

    /**
     * Push the given hex string as byte array into this buffer at the current
     * position, and then increments the position.
     * 
     * @param value
     *            Byte array to add.
     * @param index
     *            Byte index.
     * @param count
     *            Byte count.
     */
    public final void setHexString(final String value, final int index, final int count) {
        set(GXCommon.hexToBytes(value), index, count);
    }

    @Override
    public final String toString() {
        return GXCommon.toHex(data, true, 0, size);
    }

    /**
     * Get remaining data.
     * 
     * @return Remaining data as byte array.
     */
    public byte[] remaining() {
        return subArray(position, size - position);
    }

    /**
     * Get remaining data as a hex string.
     * 
     * @param addSpace
     *            Add space between bytes.
     * @return Remaining data as a hex string.
     */
    public String remainingHexString(final boolean addSpace) {
        return GXCommon.toHex(data, addSpace, position, size - position);
    }

    /**
     * Get data as hex string.
     * 
     * @param addSpace
     *            Add space between bytes.
     * @param index
     *            Byte index.
     * @return Data as hex string.
     */
    public final String toHex(final boolean addSpace, final int index) {
        return GXCommon.toHex(data, addSpace, index, size - index);
    }

    /**
     * Get data as hex string.
     * 
     * @param addSpace
     *            Add space between bytes.
     * @param index
     *            Byte index.
     * @param count
     *            Byte count.
     * @return Data as hex string.
     */
    public final String toHex(final boolean addSpace, final int index, final int count) {
        return GXCommon.toHex(data, addSpace, index, count);
    }

}
