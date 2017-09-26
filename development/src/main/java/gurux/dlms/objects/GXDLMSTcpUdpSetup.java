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

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSTcpUdpSetup extends GXDLMSObject implements IGXDLMSBase {
    private int port;
    private String ipReference;
    private int maximumSimultaneousConnections;
    private int inactivityTimeout;
    private int maximumSegmentSize;

    /**
     * Constructor.
     */
    public GXDLMSTcpUdpSetup() {
        this("0.0.25.0.0.255", (short) 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSTcpUdpSetup(final String ln) {
        this(ln, (short) 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSTcpUdpSetup(final String ln, final int sn) {
        super(ObjectType.TCP_UDP_SETUP, ln, sn);
        port = 4059;
        inactivityTimeout = 180;
        maximumSegmentSize = 576;
    }

    public final int getPort() {
        return port;
    }

    public final void setPort(final int value) {
        port = value;
    }

    public final String getIPReference() {
        return ipReference;
    }

    public final void setIPReference(final String value) {
        // Check that IP reference is OBIS code.
        if (value != null && value.length() != 0) {
            GXCommon.logicalNameToBytes(value);
        }
        ipReference = value;
    }

    public final int getMaximumSegmentSize() {
        return maximumSegmentSize;
    }

    public final void setMaximumSegmentSize(final int value) {
        maximumSegmentSize = value;
    }

    public final int getMaximumSimultaneousConnections() {
        return maximumSimultaneousConnections;
    }

    public final void setMaximumSimultaneousConnections(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid value");
        }
        maximumSimultaneousConnections = value;
    }

    public final int getInactivityTimeout() {
        return inactivityTimeout;
    }

    public final void setInactivityTimeout(final int value) {
        inactivityTimeout = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), new Integer(getPort()),
                getIPReference(), new Integer(getMaximumSegmentSize()),
                new Integer(getMaximumSimultaneousConnections()),
                new Integer(getInactivityTimeout()) };
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
        // Port
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // IPReference
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // MaximumSegmentSize
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        // MaximumSimultaneousConnections
        if (!isRead(5)) {
            attributes.add(new Integer(5));
        }
        // InactivityTimeout
        if (!isRead(6)) {
            attributes.add(new Integer(6));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final int getAttributeCount() {
        return 6;
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
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.UINT16;
        }
        if (index == 3) {
            return DataType.OCTET_STRING;
        }
        if (index == 4) {
            return DataType.UINT16;
        }
        if (index == 5) {
            return DataType.UINT8;
        }
        if (index == 6) {
            return DataType.UINT16;
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
            return new Integer(getPort());
        }
        if (e.getIndex() == 3) {
            return GXCommon.logicalNameToBytes(getIPReference());
        }
        if (e.getIndex() == 4) {
            return new Integer(getMaximumSegmentSize());
        }
        if (e.getIndex() == 5) {
            return new Integer(getMaximumSimultaneousConnections());
        }
        if (e.getIndex() == 6) {
            return new Integer(getInactivityTimeout());
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
            if (e.getValue() == null) {
                setPort(4059);
            } else {
                setPort(((Number) e.getValue()).intValue());
            }
        } else if (e.getIndex() == 3) {
            if (e.getValue() == null) {
                setIPReference(null);
            } else {
                if (e.getValue() instanceof byte[]) {
                    setIPReference(GXCommon.toLogicalName(e.getValue()));
                } else {
                    setIPReference(String.valueOf(e.getValue()));
                }
            }
        } else if (e.getIndex() == 4) {
            if (e.getValue() == null) {
                setMaximumSegmentSize(576);
            } else {
                setMaximumSegmentSize(((Number) e.getValue()).intValue());
            }
        } else if (e.getIndex() == 5) {
            if (e.getValue() == null) {
                setMaximumSimultaneousConnections(1);
            } else {
                setMaximumSimultaneousConnections(
                        ((Number) e.getValue()).intValue());
            }
        } else if (e.getIndex() == 6) {
            if (e.getValue() == null) {
                setInactivityTimeout(180);
            } else {
                setInactivityTimeout(((Number) e.getValue()).intValue());
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        port = reader.readElementContentAsInt("Port");
        ipReference = reader.readElementContentAsString("IPReference");
        maximumSegmentSize =
                reader.readElementContentAsInt("MaximumSegmentSize");
        maximumSimultaneousConnections = reader
                .readElementContentAsInt("MaximumSimultaneousConnections");
        inactivityTimeout = reader.readElementContentAsInt("InactivityTimeout");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Port", port);
        writer.writeElementString("IPReference", ipReference);
        writer.writeElementString("MaximumSegmentSize", maximumSegmentSize);
        writer.writeElementString("MaximumSimultaneousConnections",
                maximumSimultaneousConnections);
        writer.writeElementString("InactivityTimeout", inactivityTimeout);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}