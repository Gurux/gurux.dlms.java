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
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.GXDLMSIp4SetupIpOptionType;

public class GXDLMSIp4Setup extends GXDLMSObject implements IGXDLMSBase {
    private String dataLinkLayerReference;
    private long ipAddress;
    private long[] multicastIPAddress;
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
        super(ObjectType.IP4_SETUP);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSIp4Setup(final String ln) {
        super(ObjectType.IP4_SETUP, ln, 0);
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

    public final long getIPAddress() {
        return ipAddress;
    }

    public final void setIPAddress(final long value) {
        ipAddress = value;
    }

    public final long[] getMulticastIPAddress() {
        return multicastIPAddress;
    }

    public final void setMulticastIPAddress(final long[] value) {
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
                new Long(getIPAddress()), getMulticastIPAddress(),
                getIPOptions(), new Long(getSubnetMask()),
                new Long(getGatewayIPAddress()), new Boolean(getUseDHCP()),
                new Long(getPrimaryDNSAddress()),
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
    public final Object getValue(final GXDLMSSettings settings, final int index,
            final int selector, final Object parameters) {
        if (index == 1) {
            return getLogicalName();
        }
        if (index == 2) {
            return this.getDataLinkLayerReference();
        }
        if (index == 3) {
            return new Long(this.getIPAddress());
        }
        if (index == 4) {
            return this.getMulticastIPAddress();
        }
        if (index == 5) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (ipOptions == null) {
                data.setUInt8(1);
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
        if (index == 6) {
            return new Long(this.getSubnetMask());
        }
        if (index == 7) {
            return new Long(this.getGatewayIPAddress());
        }
        if (index == 8) {
            return new Boolean(this.getUseDHCP());
        }
        if (index == 9) {
            return new Long(this.getPrimaryDNSAddress());
        }
        if (index == 10) {
            return new Long(this.getSecondaryDNSAddress());
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
            if (value instanceof String) {
                this.setDataLinkLayerReference(value.toString());
            } else {
                this.setDataLinkLayerReference(GXDLMSClient
                        .changeType((byte[]) value, DataType.OCTET_STRING)
                        .toString());
            }
        } else if (index == 3) {
            setIPAddress(((Number) value).intValue());
        } else if (index == 4) {
            java.util.ArrayList<Long> data = new java.util.ArrayList<Long>();
            if (value != null) {
                for (Object it : (Object[]) value) {
                    data.add(new Long(((Number) it).longValue()));
                }
            }
            setMulticastIPAddress(GXDLMSObjectHelpers.toLongArray(data));
        } else if (index == 5) {
            java.util.ArrayList<GXDLMSIp4SetupIpOption> data =
                    new java.util.ArrayList<GXDLMSIp4SetupIpOption>();
            if (value != null) {
                for (Object it : (Object[]) value) {
                    GXDLMSIp4SetupIpOption item = new GXDLMSIp4SetupIpOption();
                    item.setType(GXDLMSIp4SetupIpOptionType.forValue(
                            ((Number) ((Object[]) it)[0]).intValue()));
                    item.setLength(
                            ((Number) (((Object[]) it)[1])).shortValue());
                    item.setData((byte[]) ((Object[]) it)[2]);
                    data.add(item);
                }
            }
            setIPOptions(data.toArray(new GXDLMSIp4SetupIpOption[data.size()]));
        } else if (index == 6) {
            setSubnetMask(((Number) value).intValue());
        } else if (index == 7) {
            setGatewayIPAddress(((Number) value).intValue());
        } else if (index == 8) {
            setUseDHCP(((Boolean) value).booleanValue());
        } else if (index == 9) {
            setPrimaryDNSAddress(((Number) value).intValue());
        } else if (index == 10) {
            setSecondaryDNSAddress(((Number) value).intValue());
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}