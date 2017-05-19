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
import gurux.dlms.objects.enums.AddressState;
import gurux.dlms.objects.enums.BaudRate;

/**
 * This IC allows modeling and configuring communication channels according to
 * EN 13757-2.Several communication channels can be configured.
 */
public class GXDLMSMBusSlavePortSetup extends GXDLMSObject
        implements IGXDLMSBase {
    private BaudRate defaultBaud;
    private BaudRate availableBaud;
    private AddressState addressState;
    private int busAddress;

    /**
     * Constructor.
     */
    public GXDLMSMBusSlavePortSetup() {
        super(ObjectType.MBUS_SLAVE_PORT_SETUP);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSMBusSlavePortSetup(final String ln) {
        super(ObjectType.MBUS_SLAVE_PORT_SETUP, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSMBusSlavePortSetup(final String ln, final int sn) {
        super(ObjectType.MBUS_SLAVE_PORT_SETUP, ln, sn);
    }

    /**
     * @return Defines the baud rate for the opening sequence.
     */
    public final BaudRate getDefaultBaud() {
        return defaultBaud;
    }

    /**
     * @param value
     *            Defines the baud rate for the opening sequence.
     */
    public final void setDefaultBaud(final BaudRate value) {
        defaultBaud = value;
    }

    /**
     * @return Defines the baud rate for the opening sequence.
     */
    public final BaudRate getAvailableBaud() {
        return availableBaud;
    }

    /**
     * @param value
     *            Defines the baud rate for the opening sequence.
     */
    public final void setAvailableBaud(final BaudRate value) {
        availableBaud = value;
    }

    /**
     * @return Defines whether or not the device has been assigned an address
     *         since last power up of the device.
     */
    public final AddressState getAddressState() {
        return addressState;
    }

    /**
     * @param value
     *            Defines whether or not the device has been assigned an address
     *            since last power up of the device.
     */
    public final void setAddressState(final AddressState value) {
        addressState = value;
    }

    /**
     * @return Defines the baud rate for the opening sequence.
     */
    public final int getBusAddress() {
        return busAddress;
    }

    /**
     * @param value
     *            Defines the baud rate for the opening sequence.
     */
    public final void setBusAddress(final int value) {
        busAddress = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getDefaultBaud(),
                getAvailableBaud(), getAddressState(),
                new Integer(getBusAddress()) };
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
        // DefaultBaud
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // AvailableBaud
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // AddressState
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        // BusAddress
        if (!isRead(5)) {
            attributes.add(new Integer(5));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final int getAttributeCount() {
        return 5;
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
            return DataType.ENUM;
        }
        if (index == 3) {
            return DataType.ENUM;
        }
        if (index == 4) {
            return DataType.ENUM;
        }
        if (index == 5) {
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
            return new Integer(getDefaultBaud().ordinal());
        }
        if (e.getIndex() == 3) {
            return new Integer(getAvailableBaud().ordinal());
        }
        if (e.getIndex() == 4) {
            return new Integer(getAddressState().ordinal());
        }
        if (e.getIndex() == 5) {
            return new Integer(getBusAddress());
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
                setDefaultBaud(BaudRate.BAUDRATE_300);
            } else {
                setDefaultBaud(
                        BaudRate.values()[((Number) e.getValue()).intValue()]);
            }
        } else if (e.getIndex() == 3) {
            if (e.getValue() == null) {
                setAvailableBaud(BaudRate.BAUDRATE_300);
            } else {
                setAvailableBaud(
                        BaudRate.values()[((Number) e.getValue()).intValue()]);
            }
        } else if (e.getIndex() == 4) {
            if (e.getValue() == null) {
                setAddressState(AddressState.NONE);
            } else {
                setAddressState(AddressState.values()[((Number) e.getValue())
                        .intValue()]);
            }
        } else if (e.getIndex() == 5) {
            if (e.getValue() == null) {
                setBusAddress(0);
            } else {
                setBusAddress(((Number) e.getValue()).intValue());
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        defaultBaud = BaudRate.values()[reader
                .readElementContentAsInt("DefaultBaud")];
        availableBaud = BaudRate.values()[reader
                .readElementContentAsInt("AvailableBaud")];
        addressState = AddressState.values()[reader
                .readElementContentAsInt("AddressState")];
        busAddress = reader.readElementContentAsInt("BusAddress");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("DefaultBaud", defaultBaud.ordinal());
        writer.writeElementString("AvailableBaud", availableBaud.ordinal());
        writer.writeElementString("AddressState", addressState.ordinal());
        writer.writeElementString("BusAddress", busAddress);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}
