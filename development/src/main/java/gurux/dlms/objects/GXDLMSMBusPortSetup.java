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

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXEnum;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.GXStructure;
import gurux.dlms.GXUInt16;
import gurux.dlms.GXUInt32;
import gurux.dlms.GXUInt8;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.MBusDataHeaderType;
import gurux.dlms.objects.enums.MBusDeviceType;
import gurux.dlms.objects.enums.MBusPortCommunicationState;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSMBusPortSetup
 */
public class GXDLMSMBusPortSetup extends GXDLMSObject implements IGXDLMSBase {

    /**
     * References an M-Bus communication port setup object describing the
     * physical capabilities for wired or wireless communication.
     */
    private String profileSelection;
    /**
     * Communication status of the M-Bus node.
     */
    private MBusPortCommunicationState portCommunicationStatus = MBusPortCommunicationState.NO_ACCESS;
    /**
     * M-Bus data header type.
     */
    private MBusDataHeaderType dataHeaderType = MBusDataHeaderType.NONE;
    /**
     * The primary address of the M-Bus slave device.
     */
    private short primaryAddress;

    /**
     * Identification Number element of the data header.
     */
    private long identificationNumber;

    /**
     * Manufacturer Identification element.
     */
    private int manufacturerId;
    /**
     * M-Bus version.
     */
    private short mBusVersion;

    /**
     * Device type.
     */
    private MBusDeviceType deviceType = MBusDeviceType.OTHER;
    /**
     * Max PDU size.
     */
    private int maxPduSize;
    /**
     * Listening windows.
     */
    private ArrayList<Entry<GXDateTime, GXDateTime>> listeningWindow;

    /**
     * Constructor.
     */
    public GXDLMSMBusPortSetup() {
        this("0.0.24.8.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSMBusPortSetup(String ln) {
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
    public GXDLMSMBusPortSetup(String ln, int sn) {
        super(ObjectType.MBUS_PORT_SETUP, ln, sn);
        setListeningWindow(new java.util.ArrayList<Entry<GXDateTime, GXDateTime>>());
    }

    /**
     * @return References an M-Bus communication port setup object describing
     *         the physical capabilities for wired or wireless communication.
     */
    public final String getProfileSelection() {
        return profileSelection;
    }

    /**
     * @param value
     *            References an M-Bus communication port setup object describing
     *            the physical capabilities for wired or wireless communication.
     */
    public final void setProfileSelection(final String value) {
        profileSelection = value;
    }

    /**
     * @return Communication status of the M-Bus node.
     */
    public final MBusPortCommunicationState getPortCommunicationStatus() {
        return portCommunicationStatus;
    }

    /**
     * @param value
     *            Communication status of the M-Bus node.
     */
    public final void setPortCommunicationStatus(final MBusPortCommunicationState value) {
        portCommunicationStatus = value;
    }

    /**
     * @return M-Bus data header type.
     */
    public final MBusDataHeaderType getDataHeaderType() {
        return dataHeaderType;
    }

    /**
     * @param value
     *            M-Bus data header type.
     */
    public final void setDataHeaderType(MBusDataHeaderType value) {
        dataHeaderType = value;
    }

    /**
     * @return The primary address of the M-Bus slave device.
     */
    public final short getPrimaryAddress() {
        return primaryAddress;
    }

    /**
     * @param value
     *            The primary address of the M-Bus slave device.
     */
    public final void setPrimaryAddress(final short value) {
        primaryAddress = value;
    }

    /**
     * @return Identification Number element of the data header.
     */
    public final long getIdentificationNumber() {
        return identificationNumber;
    }

    /**
     * @param value
     *            Identification Number element of the data header.
     */
    public final void setIdentificationNumber(final long value) {
        identificationNumber = value;
    }

    /**
     * @return Manufacturer Identification element.
     */
    public final int getManufacturerId() {
        return manufacturerId;
    }

    /**
     * @param value
     *            Manufacturer Identification element.
     */
    public final void setManufacturerId(final int value) {
        manufacturerId = value;
    }

    /**
     * @return M-Bus version.
     */
    public final short getMBusVersion() {
        return mBusVersion;
    }

    /**
     * @param value
     *            M-Bus version.
     */
    public final void setMBusVersion(final short value) {
        mBusVersion = value;
    }

    /**
     * @return Device type.
     */
    public final MBusDeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * @param value
     *            Device type.
     */
    public final void setDeviceType(final MBusDeviceType value) {
        deviceType = value;
    }

    /**
     * @return Max PSU size.
     */
    public final int getMaxPduSize() {
        return maxPduSize;
    }

    /**
     * @param value
     *            Max PSU size.
     */
    public final void setMaxPduSize(final int value) {
        maxPduSize = value;
    }

    public final java.util.ArrayList<Entry<GXDateTime, GXDateTime>> getListeningWindow() {
        return listeningWindow;
    }

    public final void setListeningWindow(java.util.ArrayList<Entry<GXDateTime, GXDateTime>> value) {
        listeningWindow = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getProfileSelection(), getPortCommunicationStatus(),
                getDataHeaderType(), getPrimaryAddress(), getIdentificationNumber(), getManufacturerId(), mBusVersion,
                getDeviceType(), getMaxPduSize(), getListeningWindow() };
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // ProfileSelection
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // PortCommunicationStatus
        if (all || !super.isRead(3)) {
            attributes.add(3);
        }
        // DataHeaderType
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // PrimaryAddress
        if (all || !super.isRead(5)) {
            attributes.add(5);
        }
        // IdentificationNumber
        if (all || !super.isRead(6)) {
            attributes.add(6);
        }
        // ManufacturerId
        if (all || !super.isRead(7)) {
            attributes.add(7);
        }
        // M-Bus version.
        if (all || !super.isRead(8)) {
            attributes.add(8);
        }
        // DeviceType
        if (all || !super.isRead(9)) {
            attributes.add(9);
        }
        // MaxPduSize,
        if (all || !super.isRead(10)) {
            attributes.add(10);
        }
        // ListeningWindow
        if (all || !super.isRead(11)) {
            attributes.add(11);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "Profile selection", "Port communication status", "Data header type",
                "Primary address", "Identification number", "Manufacturer Id", "MBus version.", "Device type",
                "Max PDU size", "Listening window" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[0];
    }

    @Override
    public final int getAttributeCount() {
        return 11;
    }

    @Override
    public final int getMethodCount() {
        return 0;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.OCTET_STRING;
        case 3:
            return DataType.ENUM;
        case 4:
            return DataType.ENUM;
        case 5:
            return DataType.UINT8;
        case 6:
            return DataType.UINT32;
        case 7:
            return DataType.UINT16;
        case 8:
            return DataType.UINT8;
        case 9:
            return DataType.UINT8;
        case 10:
            return DataType.UINT16;
        case 11:
            return DataType.ARRAY;
        default:
            throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
        }
    }

    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            return GXCommon.logicalNameToBytes(getProfileSelection());
        case 3:
            return portCommunicationStatus.getValue();
        case 4:
            return dataHeaderType.getValue();
        case 5:
            return getPrimaryAddress();
        case 6:
            return getIdentificationNumber();
        case 7:
            return getManufacturerId();
        case 8:
            return mBusVersion;
        case 9:
            return deviceType.getValue();
        case 10:
            return getMaxPduSize();
        case 11:
            int cnt = getListeningWindow().size();
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            // Add count
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0) {
                for (Entry<GXDateTime, GXDateTime> it : listeningWindow) {
                    data.setUInt8(DataType.STRUCTURE);
                    data.setUInt8(2); // Count
                    // start_time
                    GXCommon.setData(settings, data, DataType.OCTET_STRING, it.getKey());
                    // end_time
                    GXCommon.setData(settings, data, DataType.OCTET_STRING, it.getValue());
                }
            }
            return data.array();
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    @Override
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            setProfileSelection(GXCommon.toLogicalName(e.getValue()));
            break;
        case 3:
            setPortCommunicationStatus(MBusPortCommunicationState.forValue(((GXEnum) e.getValue()).shortValue()));
            break;
        case 4:
            setDataHeaderType(MBusDataHeaderType.forValue(((GXEnum) e.getValue()).shortValue()));
            break;
        case 5:
            setPrimaryAddress(((GXUInt8) e.getValue()).shortValue());
            break;
        case 6:
            setIdentificationNumber(((GXUInt32) e.getValue()).longValue());
            break;
        case 7:
            setManufacturerId(((GXUInt16) e.getValue()).intValue());
            break;
        case 8:
            mBusVersion = ((GXUInt8) e.getValue()).shortValue();
            break;
        case 9:
            setDeviceType(MBusDeviceType.forValue(((GXUInt8) e.getValue()).shortValue()));
            break;
        case 10:
            setMaxPduSize(((GXUInt16) e.getValue()).intValue());
            break;
        case 11:
            getListeningWindow().clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure item = (GXStructure) tmp;
                    GXDateTime start = (GXDateTime) GXDLMSClient.changeType((byte[]) item.get(0), DataType.DATETIME,
                            settings.getUseUtc2NormalTime());
                    GXDateTime end = (GXDateTime) GXDLMSClient.changeType((byte[]) item.get(1), DataType.DATETIME,
                            settings.getUseUtc2NormalTime());
                    getListeningWindow().add(new GXSimpleEntry<GXDateTime, GXDateTime>(start, end));
                }
            }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(GXXmlReader reader) throws XMLStreamException {
        setProfileSelection(reader.readElementContentAsString("ProfileSelection"));
        setPortCommunicationStatus(MBusPortCommunicationState.forValue(reader.readElementContentAsInt("Status")));
        setDataHeaderType(MBusDataHeaderType.forValue(reader.readElementContentAsInt("DataHeaderType")));
        setPrimaryAddress((short) reader.readElementContentAsInt("PrimaryAddress"));
        setIdentificationNumber(reader.readElementContentAsLong("IdentificationNumber"));
        setManufacturerId(reader.readElementContentAsInt("ManufacturerId"));
        mBusVersion = (short) reader.readElementContentAsInt("Version");
        deviceType = MBusDeviceType.forValue(reader.readElementContentAsInt("DeviceType"));
        setMaxPduSize(reader.readElementContentAsInt("MaxPduSize"));
        listeningWindow.clear();
        if (reader.isStartElement("ListeningWindow", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDateTime start = new GXDateTime(reader.readElementContentAsString("Start"));
                GXDateTime end = new GXDateTime(reader.readElementContentAsString("End"));
                listeningWindow.add(new GXSimpleEntry<GXDateTime, GXDateTime>(start, end));
            }
            reader.readEndElement("ListeningWindow");
        }
    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("ProfileSelection", getProfileSelection());
        writer.writeElementString("Status", getPortCommunicationStatus().getValue());
        writer.writeElementString("DataHeaderType", getDataHeaderType().getValue());
        writer.writeElementString("PrimaryAddress", getPrimaryAddress());
        writer.writeElementString("IdentificationNumber", getIdentificationNumber());
        writer.writeElementString("ManufacturerId", getManufacturerId());
        writer.writeElementString("Version", mBusVersion);
        writer.writeElementString("DeviceType", deviceType.getValue());
        writer.writeElementString("MaxPduSize", maxPduSize);
        writer.writeStartElement("ListeningWindow");
        if (listeningWindow != null) {
            for (Entry<GXDateTime, GXDateTime> it : listeningWindow) {
                writer.writeStartElement("Item");
                // Some meters are returning time here, not date-time.
                writer.writeElementString("Start", new GXDateTime(it.getKey()));
                writer.writeElementString("End", new GXDateTime(it.getValue()));
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();

    }

    @Override
    public final void postLoad(GXXmlReader reader) {
    }
}
