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

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSSpecialDaysTable extends GXDLMSObject
        implements IGXDLMSBase {
    private GXDLMSSpecialDay[] entries;

    /**
     * Constructor.
     */
    public GXDLMSSpecialDaysTable() {
        super(ObjectType.SPECIAL_DAYS_TABLE);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSpecialDaysTable(final String ln) {
        super(ObjectType.SPECIAL_DAYS_TABLE, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSSpecialDaysTable(final String ln, final int sn) {
        super(ObjectType.SPECIAL_DAYS_TABLE, ln, sn);
    }

    /**
     * @return Special day entries.
     */
    public final GXDLMSSpecialDay[] getEntries() {
        return entries;
    }

    /**
     * @param value
     *            Special day entries.
     */
    public final void setEntries(final GXDLMSSpecialDay[] value) {
        entries = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getEntries() };
    }

    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // Entries
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
        return 2;
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
            return getLogicalName();
        }
        if (e.getIndex() == 2) {
            int cnt = entries.length;
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(cnt, data);
            for (GXDLMSSpecialDay it : entries) {
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(3); // Count
                GXCommon.setData(data, DataType.UINT16,
                        new Integer(it.getIndex()));
                GXCommon.setData(data, DataType.OCTET_STRING, it.getDate());
                GXCommon.setData(data, DataType.UINT8,
                        new Integer(it.getDayId()));
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
            super.setValue(settings, e);
        } else if (e.getIndex() == 2) {
            entries = null;
            if (e.getValue() != null) {
                java.util.ArrayList<GXDLMSSpecialDay> items =
                        new java.util.ArrayList<GXDLMSSpecialDay>();
                for (Object item : (Object[]) e.getValue()) {
                    GXDLMSSpecialDay it = new GXDLMSSpecialDay();

                    it.setIndex(((Number) ((Object[]) item)[0]).intValue());
                    it.setDate((GXDateTime) GXDLMSClient.changeType(
                            (byte[]) ((Object[]) item)[1], DataType.DATE));
                    it.setDayId(((Number) ((Object[]) item)[2]).intValue());
                    items.add(it);
                }
                entries = items.toArray(new GXDLMSSpecialDay[0]);
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }
}