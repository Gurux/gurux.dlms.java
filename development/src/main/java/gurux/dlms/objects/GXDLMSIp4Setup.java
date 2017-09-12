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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
import gurux.dlms.objects.enums.Ip4SetupIpOptionType;

public class GXDLMSIp4Setup extends GXDLMSObject implements IGXDLMSBase {
    private String dataLinkLayerReference;
    private String ipAddress;
    private int[] multicastIPAddress;
    private GXDLMSIp4SetupIpOption[] ipOptions;
    private long subnetMask;
    private long gatewayIPAddress;
    private boolean useDHCP;
    private long primaryDNSAddress;
    private long secondaryDNSAddress;

    /**
     * Constructor.
     */
    public GXDLMSIp4Setup() {
        this("0.0.25.1.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSIp4Setup(final String ln) {
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
    public GXDLMSIp4Setup(final String ln, final int sn) {
        super(ObjectType.IP4_SETUP, ln, sn);
    }

    public final String getDataLinkLayerReference() {
        return dataLinkLayerReference;
    }

    public final void setDataLinkLayerReference(final String value) {
        dataLinkLayerReference = value;
    }

    public final String getIPAddress() {
        return ipAddress;
    }

    public final void setIPAddress(final String value) {
        ipAddress = value;
    }

    public final int[] getMulticastIPAddress() {
        return multicastIPAddress;
    }

    public final void setMulticastIPAddress(final int[] value) {
        multicastIPAddress = value;
    }

    public final GXDLMSIp4SetupIpOption[] getIPOptions() {
        return ipOptions;
    }

    public final void setIPOptions(final GXDLMSIp4SetupIpOption[] value) {
        ipOptions = value;
    }

    public final long getSubnetMask() {
        return subnetMask;
    }

    public final void setSubnetMask(final long value) {
        subnetMask = value;
    }

    public final long getGatewayIPAddress() {
        return gatewayIPAddress;
    }

    public final void setGatewayIPAddress(final long value) {
        gatewayIPAddress = value;
    }

    public final boolean getUseDHCP() {
        return useDHCP;
    }

    public final void setUseDHCP(final boolean value) {
        useDHCP = value;
    }

    public final long getPrimaryDNSAddress() {
        return primaryDNSAddress;
    }

    public final void setPrimaryDNSAddress(final long value) {
        primaryDNSAddress = value;
    }

    public final long getSecondaryDNSAddress() {
        return secondaryDNSAddress;
    }

    public final void setSecondaryDNSAddress(final long value) {
        secondaryDNSAddress = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getDataLinkLayerReference(),
                getIPAddress(), getMulticastIPAddress(), getIPOptions(),
                new Long(getSubnetMask()), new Long(getGatewayIPAddress()),
                new Boolean(getUseDHCP()), new Long(getPrimaryDNSAddress()),
                new Long(getSecondaryDNSAddress()) };
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
        // DataLinkLayerReference
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // IPAddress
        if (canRead(3)) {
            attributes.add(new Integer(3));
        }
        // MulticastIPAddress
        if (canRead(4)) {
            attributes.add(new Integer(4));
        }
        // IPOptions
        if (canRead(5)) {
            attributes.add(new Integer(5));
        }
        // SubnetMask
        if (canRead(6)) {
            attributes.add(new Integer(6));
        }
        // GatewayIPAddress
        if (canRead(7)) {
            attributes.add(new Integer(7));
        }
        // UseDHCP
        if (!isRead(8)) {
            attributes.add(new Integer(8));
        }
        // PrimaryDNSAddress
        if (canRead(9)) {
            attributes.add(new Integer(9));
        }
        // SecondaryDNSAddress
        if (canRead(10)) {
            attributes.add(new Integer(10));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 10;
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
            return DataType.OCTET_STRING;
        }
        if (index == 3) {
            return DataType.UINT32;
        }
        if (index == 4) {
            return DataType.ARRAY;
        }
        if (index == 5) {
            return DataType.ARRAY;
        }
        if (index == 6) {
            return DataType.UINT32;
        }
        if (index == 7) {
            return DataType.UINT32;
        }
        if (index == 8) {
            return DataType.BOOLEAN;
        }
        if (index == 9) {
            return DataType.UINT32;
        }
        if (index == 10) {
            return DataType.UINT32;
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
            return GXCommon.logicalNameToBytes(getDataLinkLayerReference());
        }
        if (e.getIndex() == 3) {
            if (getIPAddress() == null || getIPAddress().isEmpty()) {
                return 0;
            }
            GXByteBuffer bb = new GXByteBuffer();
            try {
                bb.set(InetAddress.getByName(getIPAddress()).getAddress());
                bb.reverse();
                return new Long(bb.getUInt32());
            } catch (UnknownHostException e1) {
                throw new RuntimeException("Invalid IP address.");
            }
        }
        if (e.getIndex() == 4) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (multicastIPAddress == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(getMulticastIPAddress().length, data);
                for (long it : getMulticastIPAddress()) {
                    GXCommon.setData(data, DataType.UINT16, it);
                }
            }
            return data.array();
        }
        if (e.getIndex() == 5) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (ipOptions == null) {
                data.setUInt8(0);
            } else {
                GXCommon.setObjectCount(ipOptions.length, data);
                for (GXDLMSIp4SetupIpOption it : ipOptions) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    data.setUInt8(3);
                    GXCommon.setData(data, DataType.UINT8, it.getType());
                    GXCommon.setData(data, DataType.UINT8,
                            new Integer(it.getLength()));
                    GXCommon.setData(data, DataType.OCTET_STRING, it.getData());
                }
            }
            return data.array();
        }
        if (e.getIndex() == 6) {
            return new Long(this.getSubnetMask());
        }
        if (e.getIndex() == 7) {
            return new Long(this.getGatewayIPAddress());
        }
        if (e.getIndex() == 8) {
            return new Boolean(this.getUseDHCP());
        }
        if (e.getIndex() == 9) {
            return new Long(this.getPrimaryDNSAddress());
        }
        if (e.getIndex() == 10) {
            return new Long(this.getSecondaryDNSAddress());
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
            if (e.getValue() instanceof String) {
                setDataLinkLayerReference(e.getValue().toString());
            } else {
                setDataLinkLayerReference(GXCommon.toLogicalName(e.getValue()));
            }
        } else if (e.getIndex() == 3) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt32(((Number) e.getValue()).intValue());
            bb.reverse();
            try {
                setIPAddress(InetAddress.getByAddress(bb.array())
                        .getCanonicalHostName());
            } catch (UnknownHostException e1) {
                throw new RuntimeException("Invalid IP address.");
            }
        } else if (e.getIndex() == 4) {
            java.util.ArrayList<Integer> data =
                    new java.util.ArrayList<Integer>();
            if (e.getValue() != null) {
                for (Object it : (Object[]) e.getValue()) {
                    data.add(new Integer(((Number) it).intValue()));
                }
            }
            setMulticastIPAddress(GXDLMSObjectHelpers.toIntArray(data));
        } else if (e.getIndex() == 5) {
            java.util.ArrayList<GXDLMSIp4SetupIpOption> data =
                    new java.util.ArrayList<GXDLMSIp4SetupIpOption>();
            if (e.getValue() != null) {
                for (Object it : (Object[]) e.getValue()) {
                    GXDLMSIp4SetupIpOption item = new GXDLMSIp4SetupIpOption();
                    item.setType(Ip4SetupIpOptionType.forValue(
                            ((Number) ((Object[]) it)[0]).intValue()));
                    item.setLength(
                            ((Number) (((Object[]) it)[1])).shortValue());
                    item.setData((byte[]) ((Object[]) it)[2]);
                    data.add(item);
                }
            }
            setIPOptions(data.toArray(new GXDLMSIp4SetupIpOption[data.size()]));
        } else if (e.getIndex() == 6) {
            setSubnetMask(((Number) e.getValue()).intValue());
        } else if (e.getIndex() == 7) {
            setGatewayIPAddress(((Number) e.getValue()).intValue());
        } else if (e.getIndex() == 8) {
            setUseDHCP(((Boolean) e.getValue()).booleanValue());
        } else if (e.getIndex() == 9) {
            setPrimaryDNSAddress(((Number) e.getValue()).intValue());
        } else if (e.getIndex() == 10) {
            setSecondaryDNSAddress(((Number) e.getValue()).intValue());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        dataLinkLayerReference =
                reader.readElementContentAsString("DataLinkLayerReference");
        ipAddress = reader.readElementContentAsString("IPAddress");
        List<Integer> list = new ArrayList<Integer>();
        if (reader.isStartElement("MulticastIPAddress", true)) {
            while (reader.isStartElement("Value", false)) {
                list.add(reader.readElementContentAsInt("Value"));
            }
            reader.readEndElement("MulticastIPAddress");
        }
        multicastIPAddress = GXCommon.toIntArray(list);

        List<GXDLMSIp4SetupIpOption> tmp =
                new ArrayList<GXDLMSIp4SetupIpOption>();
        if (reader.isStartElement("IPOptions", true)) {
            while (reader.isStartElement("IPOptions", true)) {
                GXDLMSIp4SetupIpOption it = new GXDLMSIp4SetupIpOption();
                it.setType(Ip4SetupIpOptionType
                        .forValue(reader.readElementContentAsInt("Type")));
                it.setLength((short) reader.readElementContentAsInt("Length"));
                it.setData(GXDLMSTranslator
                        .hexToBytes(reader.readElementContentAsString("Data")));
                tmp.add(it);
            }
            reader.readEndElement("IPOptions");
        }
        ipOptions = tmp
                .toArray(list.toArray(new GXDLMSIp4SetupIpOption[list.size()]));
        subnetMask = reader.readElementContentAsULong("SubnetMask");
        gatewayIPAddress = reader.readElementContentAsULong("GatewayIPAddress");
        useDHCP = reader.readElementContentAsInt("UseDHCP") != 0;
        primaryDNSAddress =
                reader.readElementContentAsULong("PrimaryDNSAddress");
        secondaryDNSAddress =
                reader.readElementContentAsULong("SecondaryDNSAddress");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("DataLinkLayerReference",
                dataLinkLayerReference);
        writer.writeElementString("IPAddress", ipAddress);
        if (multicastIPAddress != null) {
            writer.writeStartElement("MulticastIPAddress");
            for (int it : multicastIPAddress) {
                writer.writeElementString("Value", it);
            }
            writer.writeEndElement();
        }
        if (ipOptions != null) {
            writer.writeStartElement("IPOptions");
            for (GXDLMSIp4SetupIpOption it : ipOptions) {
                writer.writeStartElement("IPOptions");
                writer.writeElementString("Type", it.getType().ordinal());
                writer.writeElementString("Length", it.getLength());
                writer.writeElementString("Data",
                        GXDLMSTranslator.toHex(it.getData()));
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeElementString("SubnetMask", subnetMask);
        writer.writeElementString("GatewayIPAddress", gatewayIPAddress);
        writer.writeElementString("UseDHCP", useDHCP);
        writer.writeElementString("PrimaryDNSAddress", primaryDNSAddress);
        writer.writeElementString("SecondaryDNSAddress", secondaryDNSAddress);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}