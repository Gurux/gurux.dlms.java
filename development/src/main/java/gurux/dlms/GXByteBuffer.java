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
        this.position = value;
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
        return this.size;
    }

    /**
     * @param value
     *            Buffer size.
     */
    public final void size(final int value) {
        this.size = value;
    }

    /**
     * @return Get buffer data as byte array.
     */
    public final byte[] array() {
        return subArray(0, size);
    }

    /**
     * Returns data as byte array.
     * 
     * @return Byte buffer as a byte array.
     */
    public final byte[] subArray(final int index, final int count) {
        byte[] tmp = new byte[count];
        System.arraycopy(data, index, tmp, 0, count);
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
            size = (short) (destPos + count);
            position = (short) destPos;
        } else {
            size = 0;
        }
    }

    /**
     * Remove handled bytes. This can be used in debugging to remove handled
     * bytes.
     */
    public final void trim() {
        move(position, 0, size - position);
        if (position > size) {
            position = size;
        }
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
        int tmp = Float.floatToIntBits(value);
        setUInt32(size, tmp);
        size += 4;
    }

    public final void setFloat(final int index, final float value) {
        int tmp = Float.floatToIntBits(value);
        setUInt32(index, tmp);
    }

    public final void setDouble(final double value) {
        long tmp = Double.doubleToLongBits(value);
        setUInt64(size, tmp);
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
            throw new OutOfMemoryError();
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
            throw new OutOfMemoryError();
        }
        return ((data[index] & 0xFF) << 8) | (data[index + 1] & 0xFF);
    }

    public final long getUInt32() {
        long value = getUInt32(position());
        position += 4;
        return value;
    }

    public final int getInt32() {
        return (int) getUInt32();
    }

    public final long getUInt32(final int index) {

        if (index + 4 > size) {
            throw new OutOfMemoryError();
        }
        return (data[index] & 0xFF) << 24 | (data[index + 1] & 0xFF) << 16
                | (data[index + 2] & 0xFF) << 8 | (data[index + 3] & 0xFF);
    }

    public final float getFloat() {
        float value = java.nio.ByteBuffer.wrap(getData()).getFloat(position);
        position += 4;
        return value;
    }

    public final double getDouble() {
        double value = java.nio.ByteBuffer.wrap(getData()).getDouble(position);
        position += 8;
        return value;
    }

    public final long getInt64() {
        long value = java.nio.ByteBuffer.wrap(getData()).getLong(position);
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
        long value = java.nio.ByteBuffer.wrap(getData()).getLong(position);
        position += 8;
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
            throw new OutOfMemoryError();
        }
        try {
            return new String(getData(), index, count, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

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
        set(value, value.size() - value.position());
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
                if (value instanceof Integer) {
                    setUInt64(((Integer) value).longValue());
                }
            } else if (value instanceof String) {
                try {
                    set(((String) value).getBytes("ASCII"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("Invalid object type.");
            }
        }
    }

    public final void get(final byte[] target) {

        if (size - position < target.length) {
            throw new OutOfMemoryError();
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

    @Override
    public final String toString() {
        return GXCommon.toHex(data, 0, size);
    }
}
