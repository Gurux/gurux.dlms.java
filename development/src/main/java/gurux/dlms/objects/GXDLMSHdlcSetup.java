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
import gurux.dlms.objects.enums.BaudRate;

public class GXDLMSHdlcSetup extends GXDLMSObject implements IGXDLMSBase {
    private int inactivityTimeout;
    private int deviceAddress;
    private int maximumInfoLengthTransmit;
    private BaudRate communicationSpeed;
    private int windowSizeTransmit;
    private int windowSizeReceive;
    private int interCharachterTimeout;
    private int maximumInfoLengthReceive;

    /**
     * Constructor.
     */
    public GXDLMSHdlcSetup() {
        this("0.0.22.0.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSHdlcSetup(final String ln) {
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
    public GXDLMSHdlcSetup(final String ln, final int sn) {
        super(ObjectType.IEC_HDLC_SETUP, ln, 0);
        setCommunicationSpeed(BaudRate.BAUDRATE_9600);
        windowSizeTransmit = 1;
        windowSizeReceive = 1;
        maximumInfoLengthReceive = 128;
        maximumInfoLengthTransmit = 128;
        inactivityTimeout = 120;
    }

    public final BaudRate getCommunicationSpeed() {
        return communicationSpeed;
    }

    public final void setCommunicationSpeed(final BaudRate value) {
        communicationSpeed = value;
    }

    public final int getWindowSizeTransmit() {
        return windowSizeTransmit;
    }

    public final void setWindowSizeTransmit(final int value) {
        windowSizeTransmit = value;
    }

    public final int getWindowSizeReceive() {
        return windowSizeReceive;
    }

    public final void setWindowSizeReceive(final int value) {
        windowSizeReceive = value;
    }

    public final int getMaximumInfoLengthTransmit() {
        return maximumInfoLengthTransmit;
    }

    public final void setMaximumInfoLengthTransmit(final int value) {
        maximumInfoLengthTransmit = value;
    }

    public final int getMaximumInfoLengthReceive() {
        return maximumInfoLengthReceive;
    }

    public final void setMaximumInfoLengthReceive(final int value) {
        maximumInfoLengthReceive = value;
    }

    public final int getInterCharachterTimeout() {
        return interCharachterTimeout;
    }

    public final void setInterCharachterTimeout(final int value) {
        interCharachterTimeout = value;
    }

    public final int getInactivityTimeout() {
        return inactivityTimeout;
    }

    public final void setInactivityTimeout(final int value) {
        inactivityTimeout = value;
    }

    public final int getDeviceAddress() {
        return deviceAddress;
    }

    public final void setDeviceAddress(final int value) {
        deviceAddress = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getCommunicationSpeed(),
                new Integer(getWindowSizeTransmit()),
                new Integer(getWindowSizeReceive()),
                new Integer(getMaximumInfoLengthTransmit()),
                new Integer(getMaximumInfoLengthReceive()),
                new Integer(getInterCharachterTimeout()),
                new Integer(getInactivityTimeout()),
                new Integer(getDeviceAddress()) };
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
        // CommunicationSpeed
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // WindowSizeTransmit
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // WindowSizeReceive
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        // MaximumInfoLengthTransmit
        if (!isRead(5)) {
            attributes.add(new Integer(5));
        }
        // MaximumInfoLengthReceive
        if (!isRead(6)) {
            attributes.add(new Integer(6));
        }
        // InterCharachterTimeout
        if (!isRead(7)) {
            attributes.add(new Integer(7));
        }
        // InactivityTimeout
        if (!isRead(8)) {
            attributes.add(new Integer(8));
        }
        // DeviceAddress
        if (!isRead(9)) {
            attributes.add(new Integer(9));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 9;
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
            return DataType.UINT8;
        }
        if (index == 4) {
            return DataType.UINT8;
        }
        if (index == 5) {
            return DataType.UINT16;
        }
        if (index == 6) {
            return DataType.UINT16;
        }
        if (index == 7) {
            return DataType.UINT16;
        }
        if (index == 8) {
            return DataType.UINT16;
        }
        if (index == 9) {
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
            return new Integer(communicationSpeed.ordinal());
        }
        if (e.getIndex() == 3) {
            return new Integer(windowSizeTransmit);
        }
        if (e.getIndex() == 4) {
            return new Integer(windowSizeReceive);
        }
        if (e.getIndex() == 5) {
            return new Integer(maximumInfoLengthTransmit);
        }
        if (e.getIndex() == 6) {
            return new Integer(maximumInfoLengthReceive);
        }
        if (e.getIndex() == 7) {
            return new Integer(interCharachterTimeout);
        }
        if (e.getIndex() == 8) {
            return new Integer(inactivityTimeout);
        }
        if (e.getIndex() == 9) {
            return new Integer(deviceAddress);
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
            communicationSpeed =
                    BaudRate.values()[((Number) e.getValue()).intValue()];
        } else if (e.getIndex() == 3) {
            windowSizeTransmit = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 4) {
            windowSizeReceive = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 5) {
            maximumInfoLengthTransmit = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 6) {
            maximumInfoLengthReceive = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 7) {
            interCharachterTimeout = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 8) {
            inactivityTimeout = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 9) {
            deviceAddress = ((Number) e.getValue()).intValue();
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        communicationSpeed =
                BaudRate.values()[reader.readElementContentAsInt("Speed")];
        windowSizeTransmit = reader.readElementContentAsInt("WindowSizeTx");
        windowSizeReceive = reader.readElementContentAsInt("WindowSizeRx");
        maximumInfoLengthTransmit =
                reader.readElementContentAsInt("MaximumInfoLengthTx");
        maximumInfoLengthReceive =
                reader.readElementContentAsInt("MaximumInfoLengthRx");
        interCharachterTimeout =
                reader.readElementContentAsInt("InterCharachterTimeout");
        inactivityTimeout = reader.readElementContentAsInt("InactivityTimeout");
        deviceAddress = reader.readElementContentAsInt("DeviceAddress");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Speed", communicationSpeed.ordinal());
        writer.writeElementString("WindowSizeTx", windowSizeTransmit);
        writer.writeElementString("WindowSizeRx", windowSizeReceive);
        writer.writeElementString("MaximumInfoLengthTx",
                maximumInfoLengthTransmit);
        writer.writeElementString("MaximumInfoLengthRx",
                maximumInfoLengthReceive);
        writer.writeElementString("InterCharachterTimeout",
                interCharachterTimeout);
        writer.writeElementString("InactivityTimeout", inactivityTimeout);
        writer.writeElementString("DeviceAddress", deviceAddress);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}