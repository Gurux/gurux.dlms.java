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

//
// --------------------------------------------------------------------------
package gurux.dlms;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import gurux.dlms.internal.GXCommon;

/**
 * Byte array class is used to save received bytes.
 * 
 * @author Gurux Ltd.
 */
public class GXByteBuffer {
    static final int ARRAY_CAPACITY = 10;
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
        position = 0;
        size = 0;
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
        return this.position;
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
    public final void move(final int srcPos, final int destPos,
            final int count) {
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

    public final void setUInt8(final int index, final int item) {

        if (index >= capacity()) {
            capacity(index + ARRAY_CAPACITY);
        }
        data[index] = (byte) item;
    }

    public final void setUInt16(final int item) {
        setUInt16(size, item);
        size += 2;
    }

    public final void setUInt16(final int index, final int item) {

        if (index + 2 >= capacity()) {
            capacity(index + ARRAY_CAPACITY);
        }
        data[index] = (byte) ((item >> 8) & 0xFF);
        data[index + 1] = (byte) (item & 0xFF);
    }

    public final void setUInt32(final long item) {

        setUInt32(size, item);
        size += 4;
    }

    public final void setUInt32(final int index, final long item) {

        if (index + 4 >= capacity()) {
            capacity(index + ARRAY_CAPACITY);
        }
        data[index] = (byte) ((item >> 24) & 0xFF);
        data[index + 1] = (byte) ((item >> 16) & 0xFF);
        data[index + 2] = (byte) ((item >> 8) & 0xFF);
        data[index + 3] = (byte) (item & 0xFF);
    }

    public final void setUInt64(final long item) {

        setUInt64(size, item);
        size += 8;
    }

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

    public final void setFloat(final float value) {
        setFloat(size, value);
        size += 4;
    }

    public final void setFloat(final int index, final float value) {
        int tmp = Float.floatToIntBits(value);
        setUInt32(index, tmp);
    }

    public final void setDouble(final double value) {
        setDouble(size, value);
        size += 8;
    }

    public final void setDouble(final int index, final double value) {
        long tmp = Double.doubleToLongBits(value);
        setUInt64(index, tmp);
    }

    public final short getUInt8() {
        short value = getUInt8(position());
        ++position;
        return value;
    }

    public final byte getInt8() {
        return (byte) getUInt8();
    }

    public final short getUInt8(final int index) {
        if (index >= size) {
            throw new IllegalArgumentException("getUInt8");
        }
        return (short) (data[index] & 0xFF);
    }

    public final int getUInt16() {
        int value = getUInt16(position());
        position += 2;
        return value;
    }

    public final short getInt16() {
        return (short) getUInt16();
    }

    public final int getUInt16(final int index) {
        if (index + 2 > size) {
            throw new IllegalArgumentException("getUInt16");
        }
        return ((data[index] & 0xFF) << 8) | (data[index + 1] & 0xFF);
    }

    public final long getUInt32() {
        long value = getUInt32(position());
        position += 4;
        return value;
    }

    public final int getInt32() {
        int value = getInt32(position());
        position += 4;
        return value;
    }

    public final int getInt32(final int index) {
        if (index + 4 > size) {
            throw new IllegalArgumentException("getUInt32");
        }
        return (data[index] & 0xFF) << 24 | (data[index + 1] & 0xFF) << 16
                | (data[index + 2] & 0xFF) << 8 | (data[index + 3] & 0xFF);
    }

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

    public final float getFloat() {
        return Float.intBitsToFloat(getInt32());
    }

    public final double getDouble() {
        return Double.longBitsToDouble(getInt64());
    }

    public final long getInt64() {
        long value = (long) (data[position] & 0xFF) << 56;
        value |= (long) (data[position + 1] & 0xFF) << 48;
        value |= (long) (data[position + 2] & 0xFF) << 40;
        value |= (long) (data[position + 3] & 0xFF) << 32;
        value |= (long) (data[position + 4] & 0xFF) << 24;
        value |= (data[position + 5] & 0xFF) << 16;
        value |= (data[position + 6] & 0xFF) << 8;
        value |= (data[position + 7] & 0xFF);
        position += 8;
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

    public final BigInteger getUInt64() {
        long value = getInt64();
        BigInteger b = BigInteger.valueOf(value);
        if (b.compareTo(BigInteger.ZERO) < 0) {
            b = b.add(BigInteger.ONE.shiftLeft(64));
        }
        return b;
    }

    public final String getString(final int count) {
        String str = getString(position, count, "ASCII");
        position += count;
        return str;
    }

    public final String getString(final int index, final int count) {
        return getString(index, count, "ASCII");
    }

    public final String getString(final int index, final int count,
            final String charsetName) {
        if (index + count > size) {
            throw new IllegalArgumentException("getString");
        }
        try {
            return new String(getData(), index, count, charsetName);
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
    public final void set(final byte[] value, final int index,
            final int count) {
        if (value != null && count != 0) {
            if (size + count > capacity()) {
                capacity(size + count + ARRAY_CAPACITY);
            }
            System.arraycopy(value, index, getData(), size, count);
            size += count;
        }
    }

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
            } else {
                throw new RuntimeException("Invalid object type.");
            }
        }
    }

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
    public final void setHexString(final String value, final int index,
            final int count) {
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
    public final String toHex(final boolean addSpace, final int index,
            final int count) {
        return GXCommon.toHex(data, addSpace, index, count);
    }

}
