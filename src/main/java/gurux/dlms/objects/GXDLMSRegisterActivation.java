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

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSRegisterActivation extends GXDLMSObject
        implements IGXDLMSBase {
    private List<GXDLMSObjectDefinition> registerAssignment =
            new ArrayList<GXDLMSObjectDefinition>();
    private List<AbstractMap.SimpleEntry<byte[], byte[]>> maskList =
            new ArrayList<AbstractMap.SimpleEntry<byte[], byte[]>>();
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
    public final List<AbstractMap.SimpleEntry<byte[], byte[]>> getMaskList() {
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
            attributes.add(1);
        }
        // RegisterAssignment
        if (!isRead(2)) {
            attributes.add(2);
        }
        // MaskList
        if (!isRead(3)) {
            attributes.add(3);
        }
        // ActiveMask
        if (!isRead(4)) {
            attributes.add(4);
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
    public final Object getValue(final GXDLMSSettings settings, final int index,
            final int selector, final Object parameters) {
        if (index == 1) {
            return getLogicalName();
        }
        if (index == 2) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            data.setUInt8(getRegisterAssignment().size());
            for (GXDLMSObjectDefinition it : getRegisterAssignment()) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(2);
                GXCommon.setData(data, DataType.UINT16,
                        it.getClassId().getValue());
                GXCommon.setData(data, DataType.OCTET_STRING,
                        it.getLogicalName());
            }
            return data.array();
        }
        if (index == 3) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            data.setUInt8(maskList.size());
            for (AbstractMap.SimpleEntry<byte[], byte[]> it : maskList) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(2);
                GXCommon.setData(data, DataType.OCTET_STRING, it.getKey());
                data.setUInt8(DataType.ARRAY.getValue());
                data.setUInt8(it.getValue().length);
                for (byte b : it.getValue()) {
                    GXCommon.setData(data, DataType.UINT8, b);
                }
            }
            return data.array();
        }
        if (index == 4) {
            return getActiveMask();
        }
        throw new IllegalArgumentException(
                "GetValue failed. Invalid attribute index.");
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final int index,
            final Object value) {
        if (index == 1) {
            super.setValue(settings, index, value);
        } else if (index == 2) {
            registerAssignment.clear();
            if (value != null) {
                for (Object it : (Object[]) value) {
                    GXDLMSObjectDefinition item = new GXDLMSObjectDefinition();
                    item.setClassId(ObjectType
                            .forValue(((Number) Array.get(it, 0)).intValue()));
                    item.setLogicalName(GXDLMSObject
                            .toLogicalName((byte[]) Array.get(it, 1)));
                    registerAssignment.add(item);
                }
            }
        } else if (index == 3) {
            maskList.clear();
            if (value != null) {
                for (Object it : (Object[]) value) {
                    byte[] key = (byte[]) Array.get(it, 0);
                    Object arr = Array.get(it, 1);
                    byte[] tmp = new byte[Array.getLength(arr)];
                    for (int pos = 0; pos != tmp.length; ++pos) {
                        tmp[pos] = ((Number) Array.get(arr, pos)).byteValue();
                    }
                    maskList.add(new AbstractMap.SimpleEntry<byte[], byte[]>(
                            key, tmp));
                }
            }
        } else if (index == 4) {
            if (value == null) {
                setActiveMask(null);
            } else {
                setActiveMask((byte[]) value);
            }
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}