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
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSSapAssignment extends GXDLMSObject implements IGXDLMSBase {
    private List<Entry<Integer, String>> sapAssignmentList;

    /**
     * Constructor.
     */
    public GXDLMSSapAssignment() {
        super(ObjectType.SAP_ASSIGNMENT, "0.0.41.0.0.255", 0);
        sapAssignmentList = new ArrayList<Entry<Integer, String>>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSapAssignment(final String ln) {
        super(ObjectType.SAP_ASSIGNMENT, ln, 0);
        sapAssignmentList = new ArrayList<Entry<Integer, String>>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSSapAssignment(final String ln, final int sn) {
        super(ObjectType.SAP_ASSIGNMENT, ln, sn);
        sapAssignmentList = new ArrayList<Entry<Integer, String>>();
    }

    public final List<Entry<Integer, String>> getSapAssignmentList() {
        return sapAssignmentList;
    }

    public final void
            setSapAssignmentList(final List<Entry<Integer, String>> value) {
        sapAssignmentList = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getSapAssignmentList() };
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
        // SapAssignmentList
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 2;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ARRAY;
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
            int cnt = 0;
            if (sapAssignmentList != null) {
                cnt = sapAssignmentList.size();
            }
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0) {
                for (Entry<Integer, String> it : sapAssignmentList) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    data.setUInt8(2); // Count
                    GXCommon.setData(data, DataType.UINT16, it.getKey());
                    GXCommon.setData(data, DataType.OCTET_STRING,
                            GXCommon.getBytes(it.getValue()));
                }
            }
            return data.array();
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
            sapAssignmentList.clear();
            if (e.getValue() != null) {
                for (Object item : (Object[]) e.getValue()) {
                    String str;
                    Object tmp = Array.get(item, 1);
                    if (tmp instanceof byte[]) {
                        str = GXDLMSClient
                                .changeType((byte[]) tmp, DataType.STRING)
                                .toString();
                    } else {
                        str = tmp.toString();
                    }
                    sapAssignmentList.add(new GXSimpleEntry<Integer, String>(
                            ((Number) Array.get(item, 0)).intValue(), str));
                }
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        sapAssignmentList.clear();
        if (reader.isStartElement("SapAssignmentList", true)) {
            while (reader.isStartElement("Item", true)) {
                int sap = reader.readElementContentAsInt("SAP");
                String ldn = reader.readElementContentAsString("LDN");
                sapAssignmentList
                        .add(new GXSimpleEntry<Integer, String>(sap, ldn));
            }
            reader.readEndElement("SapAssignmentList");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (sapAssignmentList != null) {
            writer.writeStartElement("SapAssignmentList");
            for (Entry<Integer, String> it : sapAssignmentList) {
                writer.writeStartElement("Item");
                writer.writeElementString("SAP", it.getKey());
                writer.writeElementString("LDN", it.getValue());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }

    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}