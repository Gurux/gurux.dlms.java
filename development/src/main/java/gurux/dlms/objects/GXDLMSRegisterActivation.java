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

package gurux.dlms.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSRegisterActivation extends GXDLMSObject
        implements IGXDLMSBase {
    private List<GXDLMSObjectDefinition> registerAssignment =
            new ArrayList<GXDLMSObjectDefinition>();
    private List<Entry<byte[], byte[]>> maskList =
            new ArrayList<Entry<byte[], byte[]>>();
    private byte[] activeMask;

    /**
     * Constructor.
     */
    public GXDLMSRegisterActivation() {
        super(ObjectType.REGISTER_ACTIVATION);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSRegisterActivation(final String ln) {
        super(ObjectType.REGISTER_ACTIVATION, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSRegisterActivation(final String ln, final int sn) {
        super(ObjectType.REGISTER_ACTIVATION, ln, sn);
    }

    /**
     * @return Register assignment list.
     */
    public final List<GXDLMSObjectDefinition> getRegisterAssignment() {
        return registerAssignment;
    }

    /**
     * @return Mask list.
     */
    public final List<Entry<byte[], byte[]>> getMaskList() {
        return maskList;
    }

    /**
     * @return Active mask.
     */
    public final byte[] getActiveMask() {
        return activeMask;
    }

    /**
     * @param value
     *            Active mask.
     */
    public final void setActiveMask(final byte[] value) {
        activeMask = value;
    }

    /*
     * Add register.
     */
    final void addRegister(final GXDLMSObjectDefinition item) {
        registerAssignment.add(item);
    }

    /*
     * Add mask.
     */
    void addMask() {

    }

    /*
     * delete mask.
     */
    void deleteMask() {

    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getRegisterAssignment(),
                getMaskList(), getActiveMask() };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // RegisterAssignment
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // MaskList
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // ActiveMask
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 4;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 3;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ARRAY;
        }
        if (index == 3) {
            return DataType.ARRAY;
        }
        if (index == 4) {
            return DataType.OCTET_STRING;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            data.setUInt8(getRegisterAssignment().size());
            for (GXDLMSObjectDefinition it : getRegisterAssignment()) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(2);
                GXCommon.setData(data, DataType.UINT16,
                        new Integer(it.getObjectType().getValue()));
                GXCommon.setData(data, DataType.OCTET_STRING,
                        GXCommon.logicalNameToBytes(it.getLogicalName()));
            }
            return data.array();
        }
        if (e.getIndex() == 3) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            data.setUInt8(maskList.size());
            for (Entry<byte[], byte[]> it : maskList) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(2);
                GXCommon.setData(data, DataType.OCTET_STRING, it.getKey());
                data.setUInt8(DataType.ARRAY.getValue());
                data.setUInt8(it.getValue().length);
                for (byte b : it.getValue()) {
                    GXCommon.setData(data, DataType.UINT8, new Byte(b));
                }
            }
            return data.array();
        }
        if (e.getIndex() == 4) {
            return getActiveMask();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            registerAssignment.clear();
            if (e.getValue() != null) {
                for (Object it : (Object[]) e.getValue()) {
                    GXDLMSObjectDefinition item = new GXDLMSObjectDefinition();
                    item.setObjectType(ObjectType.forValue(
                            ((Number) ((Object[]) it)[0]).intValue()));
                    item.setLogicalName(GXCommon
                            .toLogicalName((byte[]) ((Object[]) it)[1]));
                    registerAssignment.add(item);
                }
            }
        } else if (e.getIndex() == 3) {
            maskList.clear();
            if (e.getValue() != null) {
                for (Object it : (Object[]) e.getValue()) {
                    byte[] key = (byte[]) ((Object[]) it)[0];
                    Object arr = ((Object[]) it)[1];
                    byte[] tmp = new byte[((Object[]) arr).length];
                    for (int pos = 0; pos != tmp.length; ++pos) {
                        tmp[pos] = ((Number) ((Object[]) arr)[pos]).byteValue();
                    }
                    maskList.add(new GXSimpleEntry<byte[], byte[]>(key, tmp));
                }
            }
        } else if (e.getIndex() == 4) {
            if (e.getValue() == null) {
                setActiveMask(null);
            } else {
                setActiveMask((byte[]) e.getValue());
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        registerAssignment.clear();
        if (reader.isStartElement("RegisterAssignment", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSObjectDefinition it = new GXDLMSObjectDefinition();
                it.setObjectType(ObjectType.forValue(
                        reader.readElementContentAsInt("ObjectType")));
                it.setLogicalName(reader.readElementContentAsString("LN"));
                registerAssignment.add(it);
            }
            reader.readEndElement("RegisterAssignment");
        }
        maskList.clear();
        if (reader.isStartElement("MaskList", true)) {
            while (reader.isStartElement("Item", true)) {
                byte[] mask = GXDLMSTranslator
                        .hexToBytes(reader.readElementContentAsString("Mask"));
                byte[] i = GXDLMSTranslator
                        .hexToBytes(reader.readElementContentAsString("Index"));
                maskList.add(new GXSimpleEntry<byte[], byte[]>(mask, i));
            }
            reader.readEndElement("MaskList");
        }
        activeMask = GXDLMSTranslator
                .hexToBytes(reader.readElementContentAsString("ActiveMask"));
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (registerAssignment != null) {
            writer.writeStartElement("RegisterAssignment");
            for (GXDLMSObjectDefinition it : registerAssignment) {
                writer.writeStartElement("Item");
                writer.writeElementString("ObjectType",
                        it.getObjectType().getValue());
                writer.writeElementString("LN", it.getLogicalName());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }

        if (maskList != null) {
            writer.writeStartElement("MaskList");
            for (Entry<byte[], byte[]> it : maskList) {
                writer.writeStartElement("Item");
                writer.writeElementString("Mask",
                        GXDLMSTranslator.toHex(it.getKey()));
                writer.writeElementString("Index", GXDLMSTranslator
                        .toHex(it.getValue()).replace(" ", ";"));
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        if (activeMask != null) {
            writer.writeElementString("ActiveMask",
                    GXDLMSTranslator.toHex(activeMask));
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}