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
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.MBusEncryptionKeyStatus;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSMBusClient
 */
public class GXDLMSMBusClient extends GXDLMSObject implements IGXDLMSBase {
    private long capturePeriod;
    private int primaryAddress;
    private String mBusPortReference;
    private List<java.util.Map.Entry<String, String>> captureDefinition;
    private long identificationNumber;
    private int manufacturerID;
    private int dataHeaderVersion;
    private int deviceType;
    private int accessNumber;
    private int status;
    private int alarm;
    private int configuration;
    private MBusEncryptionKeyStatus encryptionKeyStatus;

    /**
     * Constructor.
     */
    public GXDLMSMBusClient() {
        this(null, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSMBusClient(final String ln) {
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
    public GXDLMSMBusClient(final String ln, final int sn) {
        super(ObjectType.MBUS_CLIENT, ln, sn);
        captureDefinition =
                new java.util.ArrayList<java.util.Map.Entry<String, String>>();
    }

    /**
     * @return Provides reference to an "M-Bus master port setup" object, used
     *         to configure an M-Bus port, each interface allowing to exchange
     *         data with one or more M-Bus slave devices
     */
    public final String getMBusPortReference() {
        return mBusPortReference;
    }

    /**
     * @param value
     *            Provides reference to an "M-Bus master port setup" object,
     *            used to configure an M-Bus port, each interface allowing to
     *            exchange data with one or more M-Bus slave devices
     */
    public final void setMBusPortReference(final String value) {
        mBusPortReference = value;
    }

    public final List<java.util.Map.Entry<String, String>>
            getCaptureDefinition() {
        return captureDefinition;
    }

    public final long getCapturePeriod() {
        return capturePeriod;
    }

    public final void setCapturePeriod(final long value) {
        capturePeriod = value;
    }

    public final int getPrimaryAddress() {
        return primaryAddress;
    }

    public final void setPrimaryAddress(final int value) {
        primaryAddress = value;
    }

    public final long getIdentificationNumber() {
        return identificationNumber;
    }

    public final void setIdentificationNumber(final long value) {
        identificationNumber = value;
    }

    public final int getManufacturerID() {
        return manufacturerID;
    }

    public final void setManufacturerID(final int value) {
        manufacturerID = value;
    }

    /*
     * Carries the Version element of the data header as specified in EN 13757-3
     * sub-clause 5.6.
     */
    public final int getDataHeaderVersion() {
        return dataHeaderVersion;
    }

    public final void setDataHeaderVersion(final int value) {
        dataHeaderVersion = value;
    }

    public final int getDeviceType() {
        return deviceType;
    }

    public final void setDeviceType(final int value) {
        deviceType = value;
    }

    public final int getAccessNumber() {
        return accessNumber;
    }

    public final void setAccessNumber(final int value) {
        accessNumber = value;
    }

    public final int getStatus() {
        return status;
    }

    public final void setStatus(final int value) {
        status = value;
    }

    public final int getAlarm() {
        return alarm;
    }

    public final void setAlarm(final int value) {
        alarm = value;
    }

    public int getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final int value) {
        configuration = value;
    }

    public MBusEncryptionKeyStatus getEncryptionKeyStatus() {
        return encryptionKeyStatus;
    }

    public void setEncryptionKeyStatus(final MBusEncryptionKeyStatus value) {
        encryptionKeyStatus = value;
    }

    @Override
    public final Object[] getValues() {
        if (getVersion() == 0) {
            return new Object[] { getLogicalName(), mBusPortReference,
                    captureDefinition, capturePeriod, primaryAddress,
                    identificationNumber, manufacturerID, dataHeaderVersion,
                    deviceType, accessNumber, status, alarm };
        }
        return new Object[] { getLogicalName(), mBusPortReference,
                captureDefinition, capturePeriod, primaryAddress,
                identificationNumber, manufacturerID, dataHeaderVersion,
                deviceType, accessNumber, status, alarm, configuration,
                encryptionKeyStatus };
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
        // MBusPortReference
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // CaptureDefinition
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // CapturePeriod
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // PrimaryAddress
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // IdentificationNumber
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // ManufacturerID
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // Version
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // DeviceType
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // AccessNumber
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // Status
        if (all || canRead(11)) {
            attributes.add(11);
        }
        // Alarm
        if (all || canRead(12)) {
            attributes.add(12);
        }
        if (getVersion() > 0) {
            // Configuration
            if (all || canRead(13)) {
                attributes.add(13);
            }
            // EncryptionKeyStatus
            if (all || canRead(14)) {
                attributes.add(14);
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        if (getVersion() == 0) {
            return 12;
        }
        return 14;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 8;
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
            return DataType.ARRAY;
        }
        if (index == 4) {
            return DataType.UINT32;
        }
        if (index == 5) {
            return DataType.UINT8;
        }
        if (index == 6) {
            return DataType.UINT32;
        }
        if (index == 7) {
            return DataType.UINT16;
        }
        if (index == 8) {
            return DataType.UINT8;
        }
        if (index == 9) {
            return DataType.UINT8;
        }
        if (index == 10) {
            return DataType.UINT8;
        }
        if (index == 11) {
            return DataType.UINT8;
        }
        if (index == 12) {
            return DataType.UINT8;
        }
        if (getVersion() > 0) {
            if (index == 13) {
                return DataType.UINT16;
            }
            if (index == 14) {
                return DataType.ENUM;
            }
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
            return GXCommon.logicalNameToBytes(mBusPortReference);
        }
        if (e.getIndex() == 3) {
            GXByteBuffer buff = new GXByteBuffer();
            buff.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(captureDefinition.size(), buff);
            for (Map.Entry<String, String> it : captureDefinition) {
                buff.setUInt8(DataType.STRUCTURE.getValue());
                buff.setUInt8(2);
                GXCommon.setData(settings, buff, DataType.UINT8, it.getKey());
                if (it.getValue() == null) {
                    GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                            null);
                } else {
                    GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                            it.getValue().getBytes());
                }
            }
            return buff.array();
        }
        if (e.getIndex() == 4) {
            return capturePeriod;
        }
        if (e.getIndex() == 5) {
            return primaryAddress;
        }
        if (e.getIndex() == 6) {
            return identificationNumber;
        }
        if (e.getIndex() == 7) {
            return manufacturerID;
        }
        if (e.getIndex() == 8) {
            return dataHeaderVersion;
        }
        if (e.getIndex() == 9) {
            return deviceType;
        }
        if (e.getIndex() == 10) {
            return accessNumber;
        }
        if (e.getIndex() == 11) {
            return status;
        }
        if (e.getIndex() == 12) {
            return alarm;
        }
        if (getVersion() > 0) {
            if (e.getIndex() == 13) {
                return configuration;
            }
            if (e.getIndex() == 14) {
                return encryptionKeyStatus.ordinal();
            }
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
            mBusPortReference = GXCommon.toLogicalName(e.getValue());
        } else if (e.getIndex() == 3) {
            captureDefinition.clear();
            if (e.getValue() != null) {
                for (Object it : (List<?>) e.getValue()) {
                    captureDefinition
                            .add(new GXSimpleEntry<String, String>(
                                    GXDLMSClient
                                            .changeType(
                                                    (byte[]) ((List<?>) it)
                                                            .get(0),
                                                    DataType.OCTET_STRING)
                                            .toString(),
                                    GXDLMSClient
                                            .changeType(
                                                    (byte[]) ((List<?>) it)
                                                            .get(1),
                                                    DataType.OCTET_STRING)
                                            .toString()));
                }
            }
        } else if (e.getIndex() == 4) {
            capturePeriod = ((Number) e.getValue()).longValue();
        } else if (e.getIndex() == 5) {
            primaryAddress = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 6) {
            identificationNumber = ((Number) e.getValue()).longValue();
        } else if (e.getIndex() == 7) {
            manufacturerID = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 8) {
            dataHeaderVersion = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 9) {
            deviceType = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 10) {
            accessNumber = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 11) {
            status = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 12) {
            alarm = ((Number) e.getValue()).intValue();
        } else if (getVersion() > 0) {
            if (e.getIndex() == 13) {
                configuration = ((Number) e.getValue()).intValue();
            } else if (e.getIndex() == 14) {
                encryptionKeyStatus = MBusEncryptionKeyStatus
                        .values()[((Number) e.getValue()).intValue()];
            } else {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        mBusPortReference =
                reader.readElementContentAsString("MBusPortReference");
        captureDefinition.clear();
        if (reader.isStartElement("CaptureDefinition", true)) {
            while (reader.isStartElement("Item", true)) {
                String d = reader.readElementContentAsString("Data");
                String v = reader.readElementContentAsString("Value");
                captureDefinition.add(new GXSimpleEntry<String, String>(d, v));
            }
            reader.readEndElement("CaptureDefinition");
        }
        capturePeriod = reader.readElementContentAsInt("CapturePeriod");
        primaryAddress = reader.readElementContentAsInt("PrimaryAddress");
        identificationNumber =
                reader.readElementContentAsInt("IdentificationNumber");
        manufacturerID = reader.readElementContentAsInt("ManufacturerID");
        dataHeaderVersion = reader.readElementContentAsInt("DataHeaderVersion");
        deviceType = reader.readElementContentAsInt("DeviceType");
        accessNumber = reader.readElementContentAsInt("AccessNumber");

        status = reader.readElementContentAsInt("Status");
        alarm = reader.readElementContentAsInt("Alarm");
        if (getVersion() > 0) {
            configuration = reader.readElementContentAsInt("Configuration");
            encryptionKeyStatus = MBusEncryptionKeyStatus.values()[reader
                    .readElementContentAsInt("EncryptionKeyStatus")];
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("MBusPortReference", mBusPortReference);
        if (captureDefinition != null) {
            writer.writeStartElement("CaptureDefinition");
            for (Entry<String, String> it : captureDefinition) {
                writer.writeStartElement("Item");
                writer.writeElementString("Data", it.getKey());
                writer.writeElementString("Value", it.getValue());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeElementString("CapturePeriod", capturePeriod);
        writer.writeElementString("PrimaryAddress", primaryAddress);
        writer.writeElementString("IdentificationNumber", identificationNumber);
        writer.writeElementString("ManufacturerID", manufacturerID);
        writer.writeElementString("DataHeaderVersion", dataHeaderVersion);
        writer.writeElementString("DeviceType", deviceType);
        writer.writeElementString("AccessNumber", accessNumber);
        writer.writeElementString("Status", status);
        writer.writeElementString("Alarm", alarm);
        if (getVersion() > 0) {
            writer.writeElementString("Configuration", configuration);
            writer.writeElementString("EncryptionKeyStatus",
                    encryptionKeyStatus.ordinal());
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
        // Not needed for this object.
    }
}