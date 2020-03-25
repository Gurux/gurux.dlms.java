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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXStructure;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.AddressConfigMode;
import gurux.dlms.objects.enums.IPv6AddressType;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSIp6Setup
 */
public class GXDLMSIp6Setup extends GXDLMSObject implements IGXDLMSBase {

    private String dataLinkLayerReference;
    private AddressConfigMode addressConfigMode;
    private InetAddress[] unicastIPAddress;
    private InetAddress[] multicastIPAddress;
    private InetAddress[] gatewayIPAddress;
    private InetAddress primaryDNSAddress;
    private InetAddress secondaryDNSAddress;
    private byte trafficClass;
    private GXNeighborDiscoverySetup[] neighborDiscoverySetup;

    /**
     * Constructor.
     */
    public GXDLMSIp6Setup() {
        this("0.0.25.7.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSIp6Setup(final String ln) {
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
    public GXDLMSIp6Setup(final String ln, final int sn) {
        super(ObjectType.IP6_SETUP, ln, sn);
        addressConfigMode = AddressConfigMode.AUTO;
    }

    public final String getDataLinkLayerReference() {
        return dataLinkLayerReference;
    }

    public final void setDataLinkLayerReference(final String value) {
        dataLinkLayerReference = value;
    }

    public final AddressConfigMode getAddressConfigMode() {
        return addressConfigMode;
    }

    public final void setAddressConfigMode(final AddressConfigMode value) {
        addressConfigMode = value;
    }

    public final InetAddress[] getUnicastIPAddress() {
        return unicastIPAddress;
    }

    public final void setUnicastIPAddress(final InetAddress[] value) {
        unicastIPAddress = value;
    }

    public final InetAddress[] getMulticastIPAddress() {
        return multicastIPAddress;
    }

    public final void setMulticastIPAddress(final InetAddress[] value) {
        multicastIPAddress = value;
    }

    public final InetAddress[] getGatewayIPAddress() {
        return gatewayIPAddress;
    }

    public final void setGatewayIPAddress(final InetAddress[] value) {
        gatewayIPAddress = value;
    }

    public final InetAddress getPrimaryDNSAddress() {
        return primaryDNSAddress;
    }

    public final void setPrimaryDNSAddress(final InetAddress value) {
        primaryDNSAddress = value;
    }

    public final InetAddress getSecondaryDNSAddress() {
        return secondaryDNSAddress;
    }

    public final void setSecondaryDNSAddress(final InetAddress value) {
        secondaryDNSAddress = value;
    }

    public final byte getTrafficClass() {
        return trafficClass;
    }

    public final void setTrafficClass(final byte value) {
        trafficClass = value;
    }

    public final GXNeighborDiscoverySetup[] getNeighborDiscoverySetup() {
        return neighborDiscoverySetup;
    }

    public final void
            setNeighborDiscoverySetup(final GXNeighborDiscoverySetup[] value) {
        neighborDiscoverySetup = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), dataLinkLayerReference,
                addressConfigMode, unicastIPAddress, multicastIPAddress,
                gatewayIPAddress, primaryDNSAddress, secondaryDNSAddress,
                trafficClass, neighborDiscoverySetup };
    }

    /**
     * Adds IP v6 address to the meter.
     * 
     * @param client
     *            DLMS client.
     * @param type
     *            Address type.
     * @param address
     *            IP v6 Address to add.
     * @return Generated bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public byte[][] addAddress(final GXDLMSClient client,
            final IPv6AddressType type, final InetAddress address)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.ordinal());
        bb.setUInt8(2);
        GXCommon.setData(null, bb, DataType.ENUM, type.ordinal());
        GXCommon.setData(null, bb, DataType.OCTET_STRING, address.getAddress());
        return client.method(this, 1, bb.array(), DataType.STRUCTURE);
    }

    /**
     * Removes IP v6 address to the meter.
     * 
     * @param client
     *            DLMS client.
     * @param type
     *            Address type.
     * @param address
     *            IP v6 Address to remove.
     * @return Generated bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     */
    public byte[][] removeAddress(final GXDLMSClient client,
            final IPv6AddressType type, final InetAddress address)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.ordinal());
        bb.setUInt8(2);
        GXCommon.setData(null, bb, DataType.ENUM, type.ordinal());
        GXCommon.setData(null, bb, DataType.OCTET_STRING, address.getAddress());
        return client.method(this, 2, bb.array(), DataType.STRUCTURE);
    }

    // Remove address from the list.
    private void remove(final List<InetAddress> list,
            final InetAddress address) {
        for (InetAddress it : list) {
            if (it.toString().compareTo(address.toString()) == 0) {
                list.remove(it);
                return;
            }
        }
        throw new IllegalArgumentException("IP address not found.");
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        GXStructure val = (GXStructure) e.getParameters();
        IPv6AddressType type =
                IPv6AddressType.values()[((Number) val.get(0)).shortValue()];
        InetAddress address;
        try {
            address = InetAddress.getByAddress((byte[]) val.get(1));
        } catch (UnknownHostException e1) {
            throw new IllegalArgumentException(e1);
        }
        List<InetAddress> list;
        if (e.getIndex() == 1) {
            switch (type) {
            case UNICAST:
                list = new ArrayList<InetAddress>();
                if (unicastIPAddress != null) {
                    list.addAll(Arrays.asList(unicastIPAddress));
                }
                list.add(address);
                unicastIPAddress = list.toArray(new InetAddress[0]);
                break;
            case MULTICAST:
                list = new ArrayList<InetAddress>();
                if (multicastIPAddress != null) {
                    list.addAll(Arrays.asList(multicastIPAddress));
                }
                list.add(address);
                multicastIPAddress = list.toArray(new InetAddress[0]);
                break;
            case GATEWAY:
                list = new ArrayList<InetAddress>();
                if (gatewayIPAddress != null) {
                    list.addAll(Arrays.asList(gatewayIPAddress));
                }
                list.add(address);
                gatewayIPAddress = list.toArray(new InetAddress[0]);
                break;
            default:
                e.setError(ErrorCode.READ_WRITE_DENIED);
                break;
            }
        } else if (e.getIndex() == 2) {
            switch (type) {
            case UNICAST:
                list = new ArrayList<InetAddress>(
                        Arrays.asList(multicastIPAddress));
                remove(list, address);
                unicastIPAddress = list.toArray(new InetAddress[0]);
                break;
            case MULTICAST:
                list = new ArrayList<InetAddress>(
                        Arrays.asList(multicastIPAddress));
                remove(list, address);
                multicastIPAddress = list.toArray(new InetAddress[0]);
                break;
            case GATEWAY:
                list = new ArrayList<InetAddress>(
                        Arrays.asList(gatewayIPAddress));
                remove(list, address);
                gatewayIPAddress = list.toArray(new InetAddress[0]);
                break;
            default:
                e.setError(ErrorCode.READ_WRITE_DENIED);
                break;
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
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
        // DataLinkLayerReference
        if (all || !isRead(2)) {
            attributes.add(2);
        }
        // AddressConfigMode
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // UnicastIPAddress
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // MulticastIPAddress
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // GatewayIPAddress
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // PrimaryDNSAddress
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // SecondaryDNSAddress
        if (all || !isRead(8)) {
            attributes.add(8);
        }
        // TrafficClass
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // NeighborDiscoverySetup
        if (all || canRead(10)) {
            attributes.add(10);
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
        return 2;
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
            return DataType.ENUM;
        }
        if (index == 4) {
            return DataType.ARRAY;
        }
        if (index == 5) {
            return DataType.ARRAY;
        }
        if (index == 6) {
            return DataType.ARRAY;
        }
        if (index == 7) {
            return DataType.OCTET_STRING;
        }
        if (index == 8) {
            return DataType.OCTET_STRING;
        }
        if (index == 9) {
            return DataType.UINT8;
        }
        if (index == 10) {
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
            return GXCommon.logicalNameToBytes(dataLinkLayerReference);
        }
        if (e.getIndex() == 3) {
            return addressConfigMode.ordinal();
        }
        if (e.getIndex() == 4) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (unicastIPAddress == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(unicastIPAddress.length, data);
                for (InetAddress it : unicastIPAddress) {
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            it.getAddress());
                }
            }
            return data.array();
        }
        if (e.getIndex() == 5) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (multicastIPAddress == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(multicastIPAddress.length, data);
                for (InetAddress it : multicastIPAddress) {
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            it.getAddress());
                }
            }
            return data.array();
        }
        if (e.getIndex() == 6) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (gatewayIPAddress == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(gatewayIPAddress.length, data);
                for (InetAddress it : gatewayIPAddress) {
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            it.getAddress());
                }
            }
            return data.array();
        }
        if (e.getIndex() == 7) {
            if (primaryDNSAddress == null) {
                return null;
            }
            return primaryDNSAddress.getAddress();
        }
        if (e.getIndex() == 8) {
            if (secondaryDNSAddress == null) {
                return null;
            }
            return secondaryDNSAddress.getAddress();
        }
        if (e.getIndex() == 9) {
            return trafficClass;
        }
        if (e.getIndex() == 10) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (neighborDiscoverySetup == null) {
                // Object count is zero.
                data.setUInt8(0);
            } else {
                GXCommon.setObjectCount(neighborDiscoverySetup.length, data);
                for (GXNeighborDiscoverySetup it : neighborDiscoverySetup) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    data.setUInt8(3);
                    GXCommon.setData(settings, data, DataType.UINT8,
                            it.getMaxRetry());
                    GXCommon.setData(settings, data, DataType.UINT16,
                            it.getRetryWaitTime());
                    GXCommon.setData(settings, data, DataType.UINT32,
                            it.getSendPeriod());
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
        try {
            if (e.getIndex() == 1) {
                setLogicalName(GXCommon.toLogicalName(e.getValue()));
            } else if (e.getIndex() == 2) {
                if (e.getValue() instanceof String) {
                    dataLinkLayerReference = e.getValue().toString();
                } else {
                    dataLinkLayerReference =
                            GXCommon.toLogicalName(e.getValue());
                }
            } else if (e.getIndex() == 3) {
                addressConfigMode = AddressConfigMode
                        .values()[((Number) e.getValue()).intValue()];
            } else if (e.getIndex() == 4) {
                List<InetAddress> data = new ArrayList<InetAddress>();
                if (e.getValue() != null) {
                    for (Object it : (List<?>) e.getValue()) {
                        data.add(InetAddress.getByAddress((byte[]) it));
                    }
                }
                unicastIPAddress = data.toArray(new InetAddress[0]);
            } else if (e.getIndex() == 5) {
                List<InetAddress> data = new ArrayList<InetAddress>();
                if (e.getValue() != null) {
                    for (Object it : (List<?>) e.getValue()) {
                        data.add(InetAddress.getByAddress((byte[]) it));
                    }
                }
                multicastIPAddress = data.toArray(new InetAddress[0]);
            } else if (e.getIndex() == 6) {
                List<InetAddress> data = new ArrayList<InetAddress>();
                if (e.getValue() != null) {
                    for (Object it : (List<?>) e.getValue()) {
                        data.add(InetAddress.getByAddress((byte[]) it));
                    }
                }
                gatewayIPAddress = data.toArray(new InetAddress[0]);
            } else if (e.getIndex() == 7) {
                if (e.getValue() == null
                        || ((byte[]) e.getValue()).length == 0) {
                    primaryDNSAddress = null;
                } else {
                    primaryDNSAddress =
                            InetAddress.getByAddress((byte[]) e.getValue());
                }
            } else if (e.getIndex() == 8) {
                if (e.getValue() == null
                        || ((byte[]) e.getValue()).length == 0) {
                    secondaryDNSAddress = null;
                } else {
                    secondaryDNSAddress =
                            InetAddress.getByAddress((byte[]) e.getValue());
                }
            } else if (e.getIndex() == 9) {
                trafficClass = ((Number) e.getValue()).byteValue();
            } else if (e.getIndex() == 10) {
                List<GXNeighborDiscoverySetup> data =
                        new ArrayList<GXNeighborDiscoverySetup>();
                if (e.getValue() != null) {
                    for (Object it : (List<?>) e.getValue()) {
                        List<?> tmp = (List<?>) it;
                        GXNeighborDiscoverySetup v =
                                new GXNeighborDiscoverySetup();
                        v.setMaxRetry(((Number) tmp.get(0)).byteValue());
                        v.setRetryWaitTime(((Number) tmp.get(1)).shortValue());
                        v.setSendPeriod(((Number) tmp.get(2)).longValue());
                        data.add(v);
                    }
                }
                neighborDiscoverySetup =
                        data.toArray(new GXNeighborDiscoverySetup[0]);
            } else {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private InetAddress[] loadIPAddress(final GXXmlReader reader,
            final String name) throws UnknownHostException, XMLStreamException {
        List<InetAddress> list = new ArrayList<InetAddress>();
        if (reader.isStartElement(name, true)) {
            while (reader.isStartElement("Value", false)) {
                list.add(InetAddress
                        .getByName(reader.readElementContentAsString("Value")));
            }
            reader.readEndElement(name);
        }
        return list.toArray(new InetAddress[0]);
    }

    private GXNeighborDiscoverySetup[] loadNeighborDiscoverySetup(
            final GXXmlReader reader, final String name)
            throws XMLStreamException {
        List<GXNeighborDiscoverySetup> list =
                new ArrayList<GXNeighborDiscoverySetup>();
        if (reader.isStartElement(name, true)) {
            while (reader.isStartElement("Item", true)) {
                GXNeighborDiscoverySetup it = new GXNeighborDiscoverySetup();
                list.add(it);
                it.setMaxRetry(reader.readElementContentAsInt("MaxRetry"));
                it.setRetryWaitTime(
                        reader.readElementContentAsInt("RetryWaitTime"));
                it.setSendPeriod(reader.readElementContentAsInt("SendPeriod"));
            }
            reader.readEndElement(name);
        }
        return list.toArray(new GXNeighborDiscoverySetup[0]);
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        try {
            dataLinkLayerReference =
                    reader.readElementContentAsString("DataLinkLayerReference");
            addressConfigMode = AddressConfigMode.values()[reader
                    .readElementContentAsInt("AddressConfigMode")];
            unicastIPAddress = loadIPAddress(reader, "UnicastIPAddress");
            multicastIPAddress = loadIPAddress(reader, "MulticastIPAddress");
            gatewayIPAddress = loadIPAddress(reader, "GatewayIPAddress");
            String str = reader.readElementContentAsString("PrimaryDNSAddress");
            if (str != null) {
                primaryDNSAddress = InetAddress.getByName(str);
            } else {
                primaryDNSAddress = null;
            }
            str = reader.readElementContentAsString("SecondaryDNSAddress");
            if (str != null) {
                secondaryDNSAddress = InetAddress.getByName(str);
            } else {
                secondaryDNSAddress = null;
            }
            trafficClass =
                    (byte) reader.readElementContentAsInt("TrafficClass");
            neighborDiscoverySetup = loadNeighborDiscoverySetup(reader,
                    "NeighborDiscoverySetup");
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void saveIPAddress(final GXXmlWriter writer,
            final InetAddress[] list, final String name)
            throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement(name);
            for (InetAddress it : list) {
                writer.writeElementString("Value", it.getHostAddress());
            }
            writer.writeEndElement();
        }
    }

    private void saveNeighborDiscoverySetup(final GXXmlWriter writer,
            final GXNeighborDiscoverySetup[] list, final String name)
            throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement(name);
            for (GXNeighborDiscoverySetup it : list) {
                writer.writeStartElement("Item");
                writer.writeElementString("MaxRetry", it.getMaxRetry());
                writer.writeElementString("RetryWaitTime",
                        it.getRetryWaitTime());
                writer.writeElementString("SendPeriod", it.getSendPeriod());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("DataLinkLayerReference",
                dataLinkLayerReference);
        writer.writeElementString("AddressConfigMode",
                addressConfigMode.ordinal());
        saveIPAddress(writer, unicastIPAddress, "UnicastIPAddress");
        saveIPAddress(writer, multicastIPAddress, "MulticastIPAddress");
        saveIPAddress(writer, gatewayIPAddress, "GatewayIPAddress");
        if (primaryDNSAddress != null) {
            writer.writeElementString("PrimaryDNSAddress",
                    primaryDNSAddress.getHostAddress());
        }
        if (secondaryDNSAddress != null) {
            writer.writeElementString("SecondaryDNSAddress",
                    secondaryDNSAddress.getHostAddress());
        }
        writer.writeElementString("TrafficClass", trafficClass);
        saveNeighborDiscoverySetup(writer, neighborDiscoverySetup,
                "NeighborDiscoverySetup");
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}