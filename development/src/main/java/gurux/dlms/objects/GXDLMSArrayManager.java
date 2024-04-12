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

package gurux.dlms.objects;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXArray;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXStructure;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSArrayManager
 */
public class GXDLMSArrayManager extends GXDLMSObject implements IGXDLMSBase {

    /**
     * Elements.
     */
    private java.util.ArrayList<GXDLMSArrayManagerItem> elements;

    /**
     * Constructor.
     */
    public GXDLMSArrayManager() {
        this("0.0.18.0.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSArrayManager(String ln) {
        this(ln, 0);
        elements = new java.util.ArrayList<GXDLMSArrayManagerItem>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSArrayManager(String ln, int sn) {
        super(ObjectType.ARRAY_MANAGER, ln, sn);
        elements = new java.util.ArrayList<GXDLMSArrayManagerItem>();
    }

    /**
     * @return Elements.
     */
    public final java.util.ArrayList<GXDLMSArrayManagerItem> getElements() {
        return elements;
    }

    /**
     * @param value
     *            Elements.
     */
    public final void setElements(final java.util.ArrayList<GXDLMSArrayManagerItem> value) {
        elements = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), elements };
    }

    /**
     * Returns the number of entries in the array identified.
     * 
     * @param client
     *            DLMS client.
     * @param id
     *            Array identified.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] numberOfEntries(GXDLMSClient client, byte id)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 1, id, DataType.INT8);
    }

    /**
     * Parse the number of entries from the meter reply.
     * 
     * @param reply
     *            Meter reply.
     * @return Number of entries.
     */
    public final int parseNumberOfEntries(byte[] reply) {
        return parseNumberOfEntries(new GXByteBuffer(reply));
    }

    /**
     * Parse the number of entries from the meter reply.
     * 
     * @param reply
     *            Meter reply.
     * @return Number of entries.
     */
    public final int parseNumberOfEntries(GXByteBuffer reply) {
        GXDataInfo info = new GXDataInfo();
        return (int) GXCommon.getData(null, reply, info);
    }

    /**
     * Returns entries from the given range.
     * 
     * @param client
     *            DLMS client.
     * @param id
     *            Array identified.
     * @param from
     *            From index.
     * @param to
     *            To index.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] retrieveEntries(GXDLMSClient client, short id, int from, int to)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        GXByteBuffer reply = new GXByteBuffer();
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT8, id);
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT16, from);
        GXCommon.setData(null, reply, DataType.UINT16, to);
        return client.method(this, 2, reply.array(), DataType.STRUCTURE);
    }

    /**
     * Parse the number of entries from the meter reply.
     * 
     * @param reply
     *            Meter reply.
     * @return Number of entries.
     */
    public final java.util.ArrayList<?> parseEntries(GXByteBuffer reply) {
        GXDataInfo info = new GXDataInfo();
        return (java.util.ArrayList<?>) GXCommon.getData(null, reply, info);
    }

    /**
     * Insert a new entry.
     * 
     * @param client
     *            DLMS client.
     * @param id
     *            Array identified.
     * @param index
     *            One based entry index number.
     * @param entry
     *            Inserted entry.
     * @param dataType
     *            The data type of the inserted entry.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] insertEntry(GXDLMSClient client, short id, int index, Object entry, DataType dataType)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        GXByteBuffer reply = new GXByteBuffer();
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT8, id);
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT16, index);
        GXCommon.setData(null, reply, dataType, entry);
        return client.method(this, 3, reply.array(), DataType.STRUCTURE);
    }

    /**
     * Update entry.
     * 
     * @param client
     *            DLMS client.
     * @param id
     *            Array identified.
     * @param index
     *            One based entry index number.
     * @param entry
     *            Inserted entry.
     * @param dataType
     *            The data type of the inserted entry.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] updateEntry(GXDLMSClient client, short id, int index, Object entry, DataType dataType)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        GXByteBuffer reply = new GXByteBuffer();
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT8, id);
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT16, index);
        GXCommon.setData(null, reply, dataType, entry);
        return client.method(this, 4, reply.array(), DataType.STRUCTURE);
    }

    /**
     * Remove entries from the given range.
     * 
     * @param client
     *            DLMS client.
     * @param id
     *            Array identified.
     * @param from
     *            From index.
     * @param to
     *            To index.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] RemoveEntries(GXDLMSClient client, short id, int from, int to)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        GXByteBuffer reply = new GXByteBuffer();
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT8, id);
        reply.setUInt8(DataType.STRUCTURE);
        reply.setUInt8(2);
        GXCommon.setData(null, reply, DataType.UINT16, from);
        GXCommon.setData(null, reply, DataType.UINT16, to);
        return client.method(this, 5, reply.array(), DataType.STRUCTURE);
    }

    private GXByteBuffer getData(GXDLMSSettings settings, GXDLMSObject target, int attributeIndex) {
        return getData(settings, target, attributeIndex, 0, null);
    }

    private GXByteBuffer getData(GXDLMSSettings settings, GXDLMSObject target, int attributeIndex, int selector,
            Object parameters) {
        // Check that data type is array.
        if (target.getDataType(attributeIndex) == DataType.ARRAY) {
            ValueEventArgs arg = new ValueEventArgs(target, attributeIndex, selector, parameters);
            Object tmp = ((IGXDLMSBase) target).getValue(settings, arg);
            if (tmp instanceof byte[]) {
                GXByteBuffer bb = new GXByteBuffer((byte[]) tmp);
                if (bb.getUInt8() == DataType.ARRAY.getValue()) {
                    return bb;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
        e.setByteArray(true);
        byte[] ret = null;
        GXByteBuffer reply = new GXByteBuffer();
        switch (e.getIndex()) {
        case 1:
            numberOfEntries(settings, e, (byte) e.getParameters(), reply);
            break;
        case 2: {
            GXStructure args = (GXStructure) e.getParameters();
            retrieveEntries(settings, e, args, reply);
        }
            break;
        case 3: {
            GXStructure args = (GXStructure) e.getParameters();
            insertEntry(settings, e, args, reply);
        }
            break;
        case 4: {
            GXStructure args = (GXStructure) e.getParameters();
            updateEntry(settings, e, args, reply);
        }
            break;
        case 5: {
            GXStructure args = (GXStructure) e.getParameters();
            removeEntries(settings, e, args, reply);
        }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        if (reply.size() != 0) {
            ret = reply.array();
        }
        return ret;
    }

    private void numberOfEntries(GXDLMSSettings settings, ValueEventArgs e, short id, GXByteBuffer reply) {
        boolean found = false;
        for (GXDLMSArrayManagerItem it : elements) {
            if (it.getId() == id) {
                if (it.getElement().getTarget() instanceof GXDLMSProfileGeneric) {
                    if (it.getElement().getAttributeIndex() == 2) {
                        GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it.getElement().getTarget();
                        // Entries in use is returned when buffer size is asked.
                        GXCommon.setData(settings, reply, DataType.UINT32, pg.getEntriesInUse());
                        found = true;
                    }
                }
                if (!found) {
                    GXByteBuffer value =
                            getData(settings, it.getElement().getTarget(), it.getElement().getAttributeIndex());
                    int count = GXCommon.getObjectCount(value);
                    DataType dt;
                    if (count <= Byte.MAX_VALUE) {
                        dt = DataType.UINT8;
                    } else if (count <= Short.MAX_VALUE) {
                        dt = DataType.UINT16;
                    } else {
                        dt = DataType.UINT32;
                    }
                    GXCommon.setData(settings, reply, dt, count);
                    found = true;
                }
                break;
            }
        }
        if (!found) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    private void retrieveEntries(GXDLMSSettings settings, ValueEventArgs e, GXStructure args, GXByteBuffer reply) {
        boolean found = false;
        short id = (short) args.get(0);
        int from = (int) ((GXStructure) args.get(1)).get(0);
        int to = (int) ((GXStructure) args.get(1)).get(1);
        for (GXDLMSArrayManagerItem it : elements) {
            if (it.getId() == id && from <= to && from > 0) {
                Object parameters = null;
                if (it.getElement().getTarget() instanceof GXDLMSProfileGeneric) {
                    parameters = new Object[] { null, null, from, to };
                }
                ValueEventArgs arg = new ValueEventArgs(it.getElement().getTarget(),
                        it.getElement().getAttributeIndex(), 2, parameters);
                GXByteBuffer bb =
                        new GXByteBuffer((byte[]) ((IGXDLMSBase) it.getElement().getTarget()).getValue(settings, arg));
                GXDataInfo info = new GXDataInfo();
                GXArray arr = (GXArray) GXCommon.getData(settings, bb, info);
                // Change from to zero-based.
                --from;
                if (to < arr.size()) {
                    arr.removeRange(to, arr.size());
                }
                if (from != 0) {
                    arr.removeRange(0, from);
                }
                GXCommon.setData(settings, reply, DataType.ARRAY, arr);
                found = true;
                break;
            }
        }
        if (!found) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    private void insertEntry(GXDLMSSettings settings, ValueEventArgs e, GXStructure args, GXByteBuffer reply) {
        boolean found = false;
        short id = (short) args.get(0);
        for (GXDLMSArrayManagerItem it : elements) {
            if (it.getId() == id) {
                int index = (int) ((GXStructure) args.get(1)).get(0);
                Object data = ((GXStructure) args.get(1)).get(1);
                ValueEventArgs arg =
                        new ValueEventArgs(it.getElement().getTarget(), it.getElement().getAttributeIndex(), 0, null);
                GXByteBuffer bb =
                        new GXByteBuffer((byte[]) ((IGXDLMSBase) it.getElement().getTarget()).getValue(settings, arg));
                GXDataInfo info = new GXDataInfo();
                GXArray arr = (GXArray) GXCommon.getData(settings, bb, info);
                if (index == 0) {
                    arr.add(0, data);
                } else if (index > arr.size()) {
                    arr.add(data);
                } else {
                    // Add item after existing entry.
                    arr.add(index, data);
                }
                arg.setValue(arr);
                ((IGXDLMSBase) it.getElement().getTarget()).setValue(settings, arg);
                found = true;
                break;
            }
        }
        if (!found) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    private void updateEntry(GXDLMSSettings settings, ValueEventArgs e, GXStructure args, GXByteBuffer reply) {
        boolean found = false;
        short id = (short) args.get(0);
        for (GXDLMSArrayManagerItem it : elements) {
            if (it.getId() == id) {
                int index = (int) ((GXStructure) args.get(1)).get(0);
                Object data = ((GXStructure) args.get(1)).get(1);
                if (index > 0) {
                    // Change index to zero-based.
                    --index;
                    ValueEventArgs arg = new ValueEventArgs(it.getElement().getTarget(),
                            it.getElement().getAttributeIndex(), 0, null);
                    GXByteBuffer bb = new GXByteBuffer(
                            (byte[]) ((IGXDLMSBase) it.getElement().getTarget()).getValue(settings, arg));
                    GXDataInfo info = new GXDataInfo();
                    GXArray arr = (GXArray) GXCommon.getData(settings, bb, info);
                    if (index < arr.size()) {
                        arr.set(index, data);
                        arg.setValue(arr);
                        ((IGXDLMSBase) it.getElement().getTarget()).setValue(settings, arg);
                        found = true;
                    }
                }
                break;
            }
        }
        if (!found) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    private void removeEntries(GXDLMSSettings settings, ValueEventArgs e, GXStructure args, GXByteBuffer reply) {
        boolean found = false;
        short id = (short) args.get(0);
        int from = (int) ((GXStructure) args.get(1)).get(0);
        int to = (int) ((GXStructure) args.get(1)).get(1);
        if (from != 0 && from <= to) {
            for (GXDLMSArrayManagerItem it : elements) {
                if (it.getId() == id) {
                    int index = (int) ((GXStructure) args.get(1)).get(0);
                    Object data = ((GXStructure) args.get(1)).get(1);
                    ValueEventArgs arg = new ValueEventArgs(it.getElement().getTarget(),
                            it.getElement().getAttributeIndex(), 0, null);
                    GXByteBuffer bb = new GXByteBuffer(
                            (byte[]) ((IGXDLMSBase) it.getElement().getTarget()).getValue(settings, arg));
                    GXDataInfo info = new GXDataInfo();
                    GXArray arr = (GXArray) GXCommon.getData(settings, bb, info);
                    // Change from to zero-based.
                    --from;
                    arr.removeRange(from, to);
                    arg.setValue(arr);
                    ((IGXDLMSBase) it.getElement().getTarget()).setValue(settings, arg);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }

        // Array object list
        if (all || canRead(2)) {
            attributes.add(2);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "Objects" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[] { "Amount", "Retrieve", "Insert", "Update", "Remove" };
    }

    @Override
    public final int getAttributeCount() {
        return 2;
    }

    @Override
    public final int getMethodCount() {
        return 5;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.ARRAY;
        default:
            throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
        }
    }

    @Override
    public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2: {
            int cnt = getElements() == null ? 0 : getElements().size();
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            // Add count
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0) {
                for (GXDLMSArrayManagerItem it : elements) {
                    data.setUInt8(DataType.STRUCTURE);
                    data.setUInt8(2); // Count
                    GXCommon.setData(settings, data, DataType.UINT8, it.getId());
                    data.setUInt8(DataType.STRUCTURE);
                    data.setUInt8(3); // Count
                    GXCommon.setData(settings, data, DataType.UINT16,
                            it.getElement().getTarget().getObjectType().getValue());
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(it.getElement().getTarget().getLogicalName()));
                    GXCommon.setData(settings, data, DataType.INT8, it.getElement().getAttributeIndex());
                }
            }
            return data.array();
        }
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    @Override
    public final void setValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            getElements().clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure item = (GXStructure) tmp;
                    GXDLMSArrayManagerItem tempVar = new GXDLMSArrayManagerItem();
                    tempVar.setId(((Number) item.get(0)).shortValue());
                    GXDLMSArrayManagerItem tmp2 = tempVar;
                    GXStructure a = (GXStructure) item.get(1);
                    ObjectType ot = ObjectType.forValue(((Number) a.get(0)).byteValue());
                    GXDLMSObject obj = GXDLMSClient.createObject(ot);
                    obj.setLogicalName(GXCommon.toLogicalName(a.get(1)));
                    byte attributeIndex = (byte) a.get(2);
                    tmp2.setElement(new GXDLMSTargetObject(obj, attributeIndex));
                    elements.add(tmp2);
                }
            }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        getElements().clear();
        if (reader.isStartElement("Elements", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSArrayManagerItem tempVar = new GXDLMSArrayManagerItem();
                tempVar.setId((short) reader.readElementContentAsInt("Id"));
                GXDLMSArrayManagerItem item = tempVar;
                if (reader.isStartElement("Target", true)) {
                    ObjectType ot = ObjectType.forValue(reader.readElementContentAsInt("Type"));
                    GXDLMSObject obj = GXDLMSClient.createObject(ot);
                    obj.setLogicalName(reader.readElementContentAsString("LN"));
                    int index = reader.readElementContentAsInt("Index");
                    item.setElement(new GXDLMSTargetObject(obj, index));
                    reader.readEndElement("Target");
                }
                reader.readEndElement("Item");
                getElements().add(item);
            }
            reader.readEndElement("Elements");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeStartElement("Elements");
        if (getElements() != null) {
            for (GXDLMSArrayManagerItem it : elements) {
                writer.writeStartElement("Item");
                writer.writeElementString("Id", it.getId());
                writer.writeStartElement("Target");
                writer.writeElementString("Type", it.getElement().getTarget().getObjectType().getValue());
                writer.writeElementString("LN", it.getElement().getTarget().getLogicalName());
                writer.writeElementString("Index", it.getElement().getAttributeIndex());
                writer.writeEndElement();
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    @Override
    public final void postLoad(GXXmlReader reader) {
        // Update element objects after read.
        for (GXDLMSArrayManagerItem it : elements) {
            if (it.getElement() != null && it.getElement().getTarget() != null) {
                GXDLMSObject obj = reader.getObjects().findByLN(it.getElement().getTarget().getObjectType(),
                        it.getElement().getTarget().getLogicalName());
                if (obj != null) {
                    it.getElement().setTarget(obj);
                }
            }
        }
    }
}
