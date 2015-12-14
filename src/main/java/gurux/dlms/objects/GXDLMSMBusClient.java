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

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.List;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;

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

    /**
     * Constructor.
     */
    public GXDLMSMBusClient() {
        super(ObjectType.MBUS_CLIENT);
        captureDefinition =
                new java.util.ArrayList<java.util.Map.Entry<String, String>>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSMBusClient(final String ln) {
        super(ObjectType.MBUS_CLIENT, ln, 0);
        captureDefinition =
                new java.util.ArrayList<java.util.Map.Entry<String, String>>();
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

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), mBusPortReference,
                captureDefinition, capturePeriod, primaryAddress,
                identificationNumber, manufacturerID, dataHeaderVersion,
                deviceType, accessNumber, status, alarm };
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
            attributes.add(1);
        }
        // MBusPortReference
        if (canRead(2)) {
            attributes.add(2);
        }
        // CaptureDefinition
        if (canRead(3)) {
            attributes.add(3);
        }
        // CapturePeriod
        if (canRead(4)) {
            attributes.add(4);
        }
        // PrimaryAddress
        if (canRead(5)) {
            attributes.add(5);
        }
        // IdentificationNumber
        if (canRead(6)) {
            attributes.add(6);
        }
        // ManufacturerID
        if (canRead(7)) {
            attributes.add(7);
        }
        // Version
        if (canRead(8)) {
            attributes.add(8);
        }
        // DeviceType
        if (canRead(9)) {
            attributes.add(9);
        }
        // AccessNumber
        if (canRead(10)) {
            attributes.add(10);
        }
        // Status
        if (canRead(11)) {
            attributes.add(11);
        }
        // Alarm
        if (canRead(12)) {
            attributes.add(12);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 12;
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
            return mBusPortReference;
        }
        if (index == 3) {
            // TODO;
            return captureDefinition;
        }
        if (index == 4) {
            return capturePeriod;
        }
        if (index == 5) {
            return primaryAddress;
        }
        if (index == 6) {
            return identificationNumber;
        }
        if (index == 7) {
            return manufacturerID;
        }
        if (index == 8) {
            return dataHeaderVersion;
        }
        if (index == 9) {
            return deviceType;
        }
        if (index == 10) {
            return accessNumber;
        }
        if (index == 11) {
            return status;
        }
        if (index == 12) {
            return alarm;
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
            mBusPortReference = GXDLMSClient
                    .changeType((byte[]) value, DataType.OCTET_STRING)
                    .toString();
        } else if (index == 3) {
            captureDefinition.clear();
            for (Object it : (Object[]) value) {
                captureDefinition
                        .add(new AbstractMap.SimpleEntry<String, String>(
                                GXDLMSClient
                                        .changeType((byte[]) Array.get(it, 0),
                                                DataType.OCTET_STRING)
                                        .toString(),
                                GXDLMSClient
                                        .changeType((byte[]) Array.get(it, 1),
                                                DataType.OCTET_STRING)
                                        .toString()));
            }
        } else if (index == 4) {
            capturePeriod = ((Number) value).longValue();
        } else if (index == 5) {
            primaryAddress = ((Number) value).intValue();
        } else if (index == 6) {
            identificationNumber = ((Number) value).longValue();
        } else if (index == 7) {
            manufacturerID = ((Number) value).intValue();
        } else if (index == 8) {
            dataHeaderVersion = ((Number) value).intValue();
        } else if (index == 9) {
            deviceType = ((Number) value).intValue();
        } else if (index == 10) {
            accessNumber = ((Number) value).intValue();
        } else if (index == 11) {
            status = ((Number) value).intValue();
        } else if (index == 12) {
            alarm = ((Number) value).intValue();
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}