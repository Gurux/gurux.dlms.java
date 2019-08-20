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

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSUtilityTables
 */
public class GXDLMSUtilityTables extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Table Id.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSUtilityTables
     */
    private int tableId;

    /**
     * Contents of the table.
     */
    private byte[] buffer;

    /**
     * Constructor.
     */
    public GXDLMSUtilityTables() {
        this(null, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSUtilityTables(final String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSUtilityTables(final String ln, final int sn) {
        super(ObjectType.UTILITY_TABLES, ln, sn);
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSUtilityTables
     * 
     * @return Table Id.
     */
    public final int getTableId() {
        return tableId;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSUtilityTables
     * 
     * @param value
     *            Table Id.
     */
    public final void setTableId(final int value) {
        tableId = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSUtilityTables
     * 
     * @return Contents of the table.
     */
    public final byte[] getBuffer() {
        return buffer;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSUtilityTables
     * 
     * @param value
     *            Contents of the table.
     */
    public final void setBuffer(final byte[] value) {
        buffer = value;
    }

    @Override
    public final Object[] getValues() {
        int len = 0;
        if (buffer != null) {
            len = buffer.length;
        }
        return new Object[] { getLogicalName(), len, buffer };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead(final boolean all) {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null
                || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // TableId
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // Length
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // Buffer
        if (all || canRead(4)) {
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
        return 0;
    }

    @Override
    public final DataType getDataType(final int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.UINT16;
        case 3:
            return DataType.UINT32;
        case 4:
            return DataType.OCTET_STRING;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            return tableId;
        case 3:
            return buffer == null ? 0 : buffer.length;
        case 4:
            return buffer;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            tableId = ((Number) e.getValue()).intValue();
            break;
        case 3:
            // Skip len.
            break;
        case 4:
            if (e.getValue() instanceof String) {
                buffer = GXCommon.hexToBytes((String) e.getValue());
            } else {
                buffer = (byte[]) e.getValue();
            }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        tableId = reader.readElementContentAsInt("Value", 0);
        buffer = GXCommon
                .hexToBytes(reader.readElementContentAsString("Value", null));
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Id", tableId);
        writer.writeElementString("Buffer", GXCommon.toHex(buffer, true));
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}