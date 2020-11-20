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

import java.util.List;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSSFSKActiveInitiator
 */
public class GXDLMSSFSKActiveInitiator extends GXDLMSObject
        implements IGXDLMSBase {

    /**
     * System title of active initiator.
     */
    private byte[] systemTitle;
    /**
     * MAC address of active initiator.
     */
    private int macAddress;
    /**
     * L SAP selector of active initiator.
     */
    private byte lSapSelector;

    /**
     * Constructor.
     */
    public GXDLMSSFSKActiveInitiator() {
        this("0.0.26.1.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSFSKActiveInitiator(final String ln) {
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
    public GXDLMSSFSKActiveInitiator(final String ln, final int sn) {
        super(ObjectType.SFSK_ACTIVE_INITIATOR, ln, sn);
    }

    /**
     * @return System title of active initiator.
     */
    public final byte[] getSystemTitle() {
        return systemTitle;
    }

    /**
     * @param value
     *            System title of active initiator.
     */
    public final void setSystemTitle(final byte[] value) {
        systemTitle = value;
    }

    /**
     * @return MAC address of active initiator.
     */
    public final int getMacAddress() {
        return macAddress;
    }

    /**
     * @param value
     *            MAC address of active initiator.
     */
    public final void setMacAddress(final int value) {
        macAddress = value;
    }

    /**
     * @return L SAP selector of active initiator.
     */
    public final byte getLSapSelector() {
        return lSapSelector;
    }

    /**
     * @param value
     *            L SAP selector of active initiator.
     */
    public final void setLSapSelector(final byte value) {
        lSapSelector = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(),
                new Object[] { systemTitle, macAddress, lSapSelector } };
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
        // Active Initiator
        if (all || canRead(2)) {
            attributes.add(2);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 1;
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
            return DataType.STRUCTURE;
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
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.STRUCTURE);
            bb.setUInt8(3);
            GXCommon.setData(settings, bb, DataType.OCTET_STRING, systemTitle);
            GXCommon.setData(settings, bb, DataType.UINT16, macAddress);
            GXCommon.setData(settings, bb, DataType.UINT8, lSapSelector);
            return bb.array();
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
            if (e.getValue() != null) {
                List<?> arr = (List<?>) e.getValue();
                systemTitle = (byte[]) arr.get(0);
                macAddress = ((Number) arr.get(1)).intValue();
                lSapSelector = ((Number) arr.get(2)).byteValue();
            } else {
                systemTitle = null;
                macAddress = 0;
                lSapSelector = 0;
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        systemTitle = GXDLMSTranslator
                .hexToBytes(reader.readElementContentAsString("SystemTitle"));
        macAddress = reader.readElementContentAsInt("MacAddress");
        lSapSelector = (byte) reader.readElementContentAsInt("LSapSelector");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("SystemTitle",
                GXDLMSTranslator.toHex(systemTitle));
        writer.writeElementString("MacAddress", macAddress);
        writer.writeElementString("LSapSelector", lSapSelector);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}