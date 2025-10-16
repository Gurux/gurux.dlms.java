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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXArray;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.GXStructure;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.DataProtectionIdentifiedKeyType;
import gurux.dlms.objects.enums.DataProtectionKeyType;
import gurux.dlms.objects.enums.DataProtectionWrappedKeyType;
import gurux.dlms.objects.enums.MessageType;
import gurux.dlms.objects.enums.ProtectionType;
import gurux.dlms.objects.enums.PushOperationMethod;
import gurux.dlms.objects.enums.RestrictionType;
import gurux.dlms.objects.enums.ServiceType;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPushSetup
 */
public class GXDLMSPushSetup extends GXDLMSObject implements IGXDLMSBase {
    private ServiceType service;
    private String destination;
    private MessageType message;

    private List<Entry<GXDLMSObject, GXDLMSCaptureObject>> pushObjectList;
    private List<Map.Entry<GXDateTime, GXDateTime>> communicationWindow;
    private int randomisationStartInterval;
    private int numberOfRetries;
    /**
     * Repetition delay.
     */
    private int repetitionDelay;

    /**
     * Repetition delay for Version2.
     */
    private GXRepetitionDelay repetitionDelay2;

    /**
     * The logical name of a communication port setup object.
     */
    private GXDLMSObject portReference;

    /**
     * Push client SAP.
     */
    private byte pushClientSAP;

    /**
     * Push protection parameters.
     */
    private GXPushProtectionParameters[] pushProtectionParameters;

    /**
     * Push operation method.
     */
    private PushOperationMethod pushOperationMethod;

    /**
     * Push confirmation parameter.
     */
    private GXPushConfirmationParameter confirmationParameters;

    /**
     * Last confirmation date time.
     */
    private GXDateTime lastConfirmationDateTime;

    /**
     * Constructor.
     */
    public GXDLMSPushSetup() {
        this("0.7.25.9.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSPushSetup(final String ln) {
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
    public GXDLMSPushSetup(final String ln, final int sn) {
        super(ObjectType.PUSH_SETUP, ln, sn);
        pushObjectList = new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
        communicationWindow = new ArrayList<Map.Entry<GXDateTime, GXDateTime>>();
        repetitionDelay2 = new GXRepetitionDelay();
        confirmationParameters = new GXPushConfirmationParameter();
        service = ServiceType.TCP;
        message = MessageType.COSEM_APDU;
        pushOperationMethod = PushOperationMethod.UNCONFIRMED_FAILURE;
    }

    public final ServiceType getService() {
        return service;
    }

    public final void setService(final ServiceType value) {
        service = value;
    }

    public final String getDestination() {
        return destination;
    }

    public final void setDestination(final String value) {
        destination = value;
    }

    public final MessageType getMessage() {
        return message;
    }

    public final void setMessage(final MessageType value) {
        message = value;
    }

    /**
     * @return Defines the list of attributes or objects to be pushed. Upon a
     *         call of the push (data) method the selected attributes are sent
     *         to the destination defined in getSendDestinationAndMethod.
     */
    public final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> getPushObjectList() {
        return pushObjectList;
    }

    /**
     * @return Contains the start and end date/time stamp when the communication
     *         window(s) for the push become active (for the start instant), or
     *         inactive (for the end instant).
     */
    public final List<Map.Entry<GXDateTime, GXDateTime>> getCommunicationWindow() {
        return communicationWindow;
    }

    /**
     * @return To avoid simultaneous network connections of a lot of devices at
     *         ex-actly the same point in time, a randomisation interval in
     *         seconds can be defined. This means that the push operation is not
     *         started imme-diately at the beginning of the first communication
     *         window but started randomly delayed.
     */
    public final int getRandomisationStartInterval() {
        return randomisationStartInterval;
    }

    public final void setRandomisationStartInterval(final int value) {
        randomisationStartInterval = value;
    }

    /**
     * @return The maximum number of re-trials in case of unsuccessful push
     *         attempts. After a successful push no further push attempts are
     *         made until the push setup is triggered again. A value of 0 means
     *         no repetitions, i.e. only the initial connection attempt is made.
     */
    public final int getNumberOfRetries() {
        return numberOfRetries;
    }

    /**
     * @param value
     *            The maximum number of re-trials in case of unsuccessful push
     *            attempts. After a successful push no further push attempts are
     *            made until the push setup is triggered again. A value of 0
     *            means no repetitions, i.e. only the initial connection attempt
     *            is made.
     */
    public final void setNumberOfRetries(final byte value) {
        numberOfRetries = value;
    }

    /**
     * @return Repetition delay is used for versions zero and one.
     */
    public final int getRepetitionDelay() {
        return repetitionDelay;
    }

    /**
     * @param value
     *            Repetition delay is used for versions zero and one.
     */
    public final void setRepetitionDelay(final int value) {
        repetitionDelay = value;
    }

    @Override
    public final Object[] getValues() {
        if (version < 2) {
            return new Object[] { getLogicalName(), pushObjectList, service + " " + destination + " " + message,
                    communicationWindow, randomisationStartInterval, numberOfRetries, repetitionDelay, portReference,
                    pushClientSAP, pushProtectionParameters, pushOperationMethod, confirmationParameters,
                    lastConfirmationDateTime };
        }
        return new Object[] { getLogicalName(), pushObjectList, service + " " + destination + " " + message,
                communicationWindow, randomisationStartInterval, numberOfRetries, repetitionDelay2, portReference,
                pushClientSAP, pushProtectionParameters, pushOperationMethod, confirmationParameters,
                lastConfirmationDateTime };
    }

    /**
     * Get received objects from push message.
     * 
     * @param client
     *            GXDLMSClient used to update the values.
     * @param values
     *            Received values.
     */
    public void getPushValues(final GXDLMSClient client, final List<?> values) {
        if (values.size() != pushObjectList.size()) {
            throw new IllegalArgumentException("Size of the push object list is different than values.");
        }
        int pos = 0;
        List<Entry<GXDLMSObject, GXDLMSCaptureObject>> objects =
                new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
        for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pushObjectList) {
            GXDLMSObject obj = (GXDLMSObject) it.getKey();
            objects.add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj,
                    new GXDLMSCaptureObject(it.getValue().getAttributeIndex(), it.getValue().getDataIndex())));

            if (it.getValue().getAttributeIndex() == 0) {
                List<?> tmp = (List<?>) values.get(pos);
                for (int index = 1; index <= it.getKey().getAttributeCount(); ++index) {
                    client.updateValue(it.getKey(), index, tmp.get(index - 1));
                }
            } else {
                client.updateValue(obj, it.getValue().getAttributeIndex(), values.get(pos));
            }
            ++pos;
        }
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings, final ValueEventArgs e) {
        if (e.getIndex() != 1) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
    }

    /*
     * Activates the push process.
     */
    public final byte[][] activate(final GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(getName(), getObjectType(), 1, 0, DataType.INT8);
    }

    /*
     * Reset the push process.
     */
    public final byte[][] reset(final GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(getName(), getObjectType(), 2, 0, DataType.INT8);
    }

    @Override
    public final int[] getAttributeIndexToRead(final boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // PushObjectList
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // SendDestinationAndMethod
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // CommunicationWindow
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // RandomisationStartInterval
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // NumberOfRetries
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // RepetitionDelay
        if (all || canRead(7)) {
            attributes.add(7);
        }
        if (version > 0) {
            // PortReference
            if (all || canRead(8)) {
                attributes.add(8);
            }
            // PushClientSAP
            if (all || canRead(9)) {
                attributes.add(9);
            }
            // PushProtectionParameters
            if (all || canRead(10)) {
                attributes.add(10);
            }
            if (version < 1) {
                // PushOperationMethod
                if (all || canRead(11)) {
                    attributes.add(11);
                }
                // ConfirmationParameters
                if (all || canRead(12)) {
                    attributes.add(12);
                }
                // LastConfirmationDateTime
                if (all || canRead(13)) {
                    attributes.add(13);
                }
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        if (version == 0) {
            return 7;
        }
        if (version == 1) {
            return 10;
        }
        return 13;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        if (version < 2) {
            return 1;
        }
        return 2;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ARRAY;
        }
        if (index == 3) {
            return DataType.STRUCTURE;
        }
        if (index == 4) {
            return DataType.ARRAY;
        }
        if (index == 5) {
            return DataType.UINT16;
        }
        if (index == 6) {
            return DataType.UINT8;
        }
        if (index == 7) {
            if (version < 2) {
                return DataType.UINT16;
            }
            return DataType.STRUCTURE;
        }
        if (version > 0) {
            // PortReference
            if (index == 8) {
                return DataType.OCTET_STRING;
            }
            // PushClientSAP
            if (index == 9) {
                return DataType.INT8;
            }
            // PushProtectionParameters
            if (index == 10) {
                return DataType.ARRAY;
            }
            if (version > 1) {
                // PushOperationMethod
                if (index == 11) {
                    return DataType.ENUM;
                }
                // ConfirmationParameters
                if (index == 12) {
                    return DataType.STRUCTURE;
                }
                // LastConfirmationDateTime
                if (index == 13) {
                    return DataType.DATETIME;
                }
            }
        }
        throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        GXByteBuffer buff = new GXByteBuffer();
        Object ret;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            buff.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(pushObjectList.size(), buff);
            for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pushObjectList) {
                buff.setUInt8(DataType.STRUCTURE.getValue());
                if (version < 1) {
                    buff.setUInt8(4);
                    GXCommon.setData(settings, buff, DataType.UINT16, it.getKey().getObjectType().getValue());
                    GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(it.getKey().getLogicalName()));
                    GXCommon.setData(settings, buff, DataType.INT8, it.getValue().getAttributeIndex());
                    GXCommon.setData(settings, buff, DataType.UINT16, it.getValue().getDataIndex());
                } else {
                    buff.setUInt8(version == 1 ? 5 : 6);
                    GXCommon.setData(settings, buff, DataType.UINT16, it.getKey().getObjectType().getValue());
                    GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(it.getKey().getLogicalName()));
                    GXCommon.setData(settings, buff, DataType.INT8, it.getValue().getAttributeIndex());
                    GXCommon.setData(settings, buff, DataType.UINT16, it.getValue().getDataIndex());
                    GXCommon.setData(settings, buff, DataType.ENUM,
                            it.getValue().getRestriction().getType().getValue());
                    switch (it.getValue().getRestriction().getType()) {
                    case NONE:
                        GXCommon.setData(settings, buff, DataType.NONE, null);
                        break;
                    case DATE:
                        buff.setUInt8(DataType.STRUCTURE);
                        buff.setUInt8(2);
                        GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                                it.getValue().getRestriction().getFrom());
                        GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getValue().getRestriction().getTo());
                        break;
                    case ENTRY:
                        buff.setUInt8(DataType.STRUCTURE);
                        buff.setUInt8(2);
                        GXCommon.setData(settings, buff, DataType.UINT16, it.getValue().getRestriction().getFrom());
                        GXCommon.setData(settings, buff, DataType.UINT16, it.getValue().getRestriction().getTo());
                        break;
                    }
                    if (version > 1) {
                        if (it.getValue().getColumns() != null) {
                            buff.setUInt8(DataType.ARRAY);
                            GXCommon.setObjectCount(it.getValue().getColumns().size(), buff);
                            for (Entry<GXDLMSObject, GXDLMSCaptureObject> it2 : it.getValue().getColumns()) {
                                buff.setUInt8(DataType.STRUCTURE);
                                buff.setUInt8(4);
                                GXCommon.setData(settings, buff, DataType.UINT16, it2.getKey().getObjectType());
                                GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                                        GXCommon.logicalNameToBytes(it2.getKey().getLogicalName()));
                                GXCommon.setData(settings, buff, DataType.INT8, it2.getValue().getAttributeIndex());
                                GXCommon.setData(settings, buff, DataType.UINT16, it2.getValue().getDataIndex());
                            }
                        } else {
                            buff.setUInt8(DataType.ARRAY);
                            buff.setUInt8(0);
                        }
                    }
                }
            }
            ret = buff.array();
            break;
        case 3:
            buff.setUInt8(DataType.STRUCTURE.getValue());
            buff.setUInt8(3);
            GXCommon.setData(settings, buff, DataType.ENUM, getService().getValue());
            byte[] tmp = null;
            if (GXCommon.isHexString(destination)) {
                tmp = GXCommon.hexToBytes(destination);
            } else if (destination != null) {
                tmp = getDestination().getBytes();
            }
            GXCommon.setData(settings, buff, DataType.OCTET_STRING, tmp);
            GXCommon.setData(settings, buff, DataType.ENUM, getMessage().getValue());
            ret = buff.array();
            break;
        case 4:
            buff.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(communicationWindow.size(), buff);
            for (Entry<GXDateTime, GXDateTime> it : communicationWindow) {
                buff.setUInt8(DataType.STRUCTURE.getValue());
                buff.setUInt8(2);
                GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getKey());
                GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getValue());
            }
            ret = buff.array();
            break;
        case 5:
            ret = randomisationStartInterval;
            break;
        case 6:
            ret = numberOfRetries;
            break;
        case 7:
            if (version < 2) {
                ret = repetitionDelay;
            } else {
                buff.setUInt8(DataType.STRUCTURE);
                GXCommon.setObjectCount(3, buff);
                GXCommon.setData(settings, buff, DataType.UINT16, repetitionDelay2.getMin());
                GXCommon.setData(settings, buff, DataType.UINT16, repetitionDelay2.getExponent());
                GXCommon.setData(settings, buff, DataType.UINT16, repetitionDelay2.getMax());
                ret = buff.array();
            }
            break;
        case 8:
            if (portReference != null) {
                ret = GXCommon.logicalNameToBytes(portReference.getLogicalName());
            } else {
                ret = null;
            }
            break;
        case 9:
            ret = pushClientSAP;
            break;
        case 10:
            buff.setUInt8(DataType.ARRAY);
            GXCommon.setObjectCount(pushProtectionParameters.length, buff);
            for (GXPushProtectionParameters it : pushProtectionParameters) {
                buff.setUInt8(DataType.STRUCTURE);
                buff.setUInt8(2);
                GXCommon.setData(settings, buff, DataType.ENUM, it.getProtectionType().getValue());
                buff.setUInt8(DataType.STRUCTURE);
                buff.setUInt8(5);
                GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getTransactionId());
                GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getOriginatorSystemTitle());
                GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getRecipientSystemTitle());
                GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getOtherInformation());
                buff.setUInt8(DataType.STRUCTURE);
                buff.setUInt8(2);
                GXCommon.setData(settings, buff, DataType.ENUM, it.getKeyInfo().getDataProtectionKeyType().getValue());
                buff.setUInt8(DataType.STRUCTURE);
                if (it.getKeyInfo().getDataProtectionKeyType() == DataProtectionKeyType.IDENTIFIED) {
                    buff.setUInt8(1);
                    GXCommon.setData(settings, buff, DataType.ENUM,
                            it.getKeyInfo().getIdentifiedKey().getKeyType().getValue());
                } else if (it.getKeyInfo().getDataProtectionKeyType() == DataProtectionKeyType.WRAPPED) {
                    buff.setUInt8(2);
                    GXCommon.setData(settings, buff, DataType.ENUM,
                            it.getKeyInfo().getWrappedKey().getKeyType().getValue());
                    GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getKeyInfo().getWrappedKey().getKey());
                } else if (it.getKeyInfo().getDataProtectionKeyType() == DataProtectionKeyType.AGREED) {
                    buff.setUInt8(2);
                    GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                            it.getKeyInfo().getAgreedKey().getParameters());
                    GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getKeyInfo().getAgreedKey().getData());
                }
            }
            ret = buff.array();
            break;
        case 11:
            ret = pushOperationMethod.getValue();
            break;
        case 12:
            buff.setUInt8(DataType.STRUCTURE);
            GXCommon.setObjectCount(2, buff);
            GXCommon.setData(settings, buff, DataType.DATETIME, confirmationParameters.getStartDate());
            GXCommon.setData(settings, buff, DataType.UINT32, confirmationParameters.getInterval());
            ret = buff.array();
            break;
        case 13:
            ret = lastConfirmationDateTime;
            break;
        default:
            ret = null;
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return ret;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            pushObjectList.clear();
            Entry<GXDLMSObject, GXDLMSCaptureObject> ent;
            if (e.getValue() instanceof List<?>) {
                for (Object it : (List<?>) e.getValue()) {
                    List<?> tmp = (List<?>) it;
                    ObjectType type = ObjectType.forValue(((Number) tmp.get(0)).intValue());
                    String ln = GXCommon.toLogicalName(tmp.get(1));
                    GXDLMSObject obj = settings.getObjects().findByLN(type, ln);
                    if (obj == null) {
                        // Try to find from custom objects.
                        obj = settings.getObjects().findByLN(type, ln);
                    }
                    if (obj == null) {
                        obj = gurux.dlms.GXDLMSClient.createObject(type);
                        obj.setLogicalName(ln);
                    }
                    GXDLMSCaptureObject co = new GXDLMSCaptureObject();
                    co.setAttributeIndex(((Number) tmp.get(2)).intValue());
                    co.setDataIndex(((Number) tmp.get(3)).intValue());
                    ent = new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj, co);
                    pushObjectList.add(ent);
                    if (version > 1) {
                        GXStructure restriction = (GXStructure) tmp.get(4);
                        co.getRestriction().setType(RestrictionType.forValue(((Number) restriction.get(0)).intValue()));
                        switch (co.getRestriction().getType()) {
                        case NONE:
                            break;
                        case DATE:
                        case ENTRY:
                            co.getRestriction().setFrom(restriction.get(1));
                            co.getRestriction().setTo(restriction.get(2));
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid restriction type.");
                        }
                        if (tmp.get(5) != null) {
                            for (Object tmp2 : (GXArray) tmp.get(5)) {
                                GXStructure c = (GXStructure) tmp2;
                                type = ObjectType.forValue(((Number) c.get(0)).intValue());
                                ln = GXCommon.toLogicalName(c.get(1));
                                obj = settings.getObjects().findByLN(type, ln);
                                if (obj == null) {
                                    // Try to find from custom objects.
                                    obj = settings.getObjects().findByLN(type, ln);
                                }
                                if (obj == null) {
                                    obj = gurux.dlms.GXDLMSClient.createObject(type);
                                    obj.setLogicalName(ln);
                                }
                                co = new GXDLMSCaptureObject();
                                co.setAttributeIndex(((Number) c.get(2)).intValue());
                                co.setDataIndex(((Number) c.get(3)).intValue());
                                co.getColumns().add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj, co));
                            }
                        }
                    }
                }
            }
        } else if (e.getIndex() == 3) {
            List<?> tmp = (List<?>) e.getValue();
            if (tmp != null) {
                setService(ServiceType.forValue(((Number) tmp.get(0)).intValue()));
                byte[] tmp2 = (byte[]) tmp.get(1);
                if (GXByteBuffer.isAsciiString(tmp2)) {
                    setDestination(new String(tmp2));
                } else {
                    setDestination(GXCommon.toHex(tmp2, false));
                }
                setMessage(MessageType.forValue(((Number) tmp.get(2)).intValue()));
            }
        } else if (e.getIndex() == 4) {
            communicationWindow.clear();
            if (e.getValue() instanceof List<?>) {
                for (Object it : (List<?>) e.getValue()) {
                    List<?> tmp = (List<?>) it;
                    GXDateTime start = (GXDateTime) GXDLMSClient.changeType((byte[]) tmp.get(0), DataType.DATETIME,
                            e.getSettings());
                    GXDateTime end = (GXDateTime) GXDLMSClient.changeType((byte[]) tmp.get(1), DataType.DATETIME,
                            e.getSettings());
                    communicationWindow.add(new GXSimpleEntry<GXDateTime, GXDateTime>(start, end));
                }
            }
        } else if (e.getIndex() == 5) {
            randomisationStartInterval = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 6) {
            numberOfRetries = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 7) {
            if (version < 2) {
                repetitionDelay = ((Number) e.getValue()).intValue();
            } else {
                GXStructure tmp = (GXStructure) e.getValue();
                if (tmp != null) {
                    repetitionDelay2.setMin(((Number) tmp.get(0)).intValue());
                    repetitionDelay2.setExponent(((Number) tmp.get(1)).intValue());
                    repetitionDelay2.setMax(((Number) tmp.get(2)).intValue());
                }
            }
        } else if (version > 0 && e.getIndex() == 8) {
            portReference = null;
            if (e.getValue() instanceof byte[]) {
                byte[] bv = (byte[]) e.getValue();
                String ln = GXCommon.toLogicalName(bv);
                portReference = settings.getObjects().findByLN(ObjectType.NONE, ln);
            }
        } else if (version > 0 && e.getIndex() == 9) {
            pushClientSAP = ((Number) e.getValue()).byteValue();
        } else if (version > 0 && e.getIndex() == 10) {
            GXArray tmp = (GXArray) e.getValue();
            List<GXPushProtectionParameters> list = new ArrayList<GXPushProtectionParameters>();
            if (tmp != null) {
                for (Object t : tmp) {
                    GXStructure it = (GXStructure) t;
                    GXPushProtectionParameters p = new GXPushProtectionParameters();
                    p.setProtectionType(ProtectionType.forValue(((Number) it.get(0)).intValue()));
                    GXStructure options = (GXStructure) it.get(1);
                    p.setTransactionId((byte[]) options.get(0));
                    p.setOriginatorSystemTitle((byte[]) options.get(1));
                    p.setRecipientSystemTitle((byte[]) options.get(2));
                    p.setOtherInformation((byte[]) options.get(3));
                    GXStructure keyInfo = (GXStructure) options.get(4);
                    DataProtectionKeyType type = DataProtectionKeyType.forValue(((Number) keyInfo.get(0)).intValue());
                    p.getKeyInfo().setDataProtectionKeyType(type);
                    GXStructure data = (GXStructure) keyInfo.get(1);
                    if (type == DataProtectionKeyType.IDENTIFIED) {
                        p.getKeyInfo().getIdentifiedKey().setKeyType(
                                DataProtectionIdentifiedKeyType.forValue(((Number) data.get(0)).intValue()));
                    } else if (type == DataProtectionKeyType.WRAPPED) {
                        p.getKeyInfo().getWrappedKey()
                                .setKeyType(DataProtectionWrappedKeyType.forValue(((Number) data.get(0)).intValue()));
                        p.getKeyInfo().getWrappedKey().setKey((byte[]) data.get(1));
                    } else if (type == DataProtectionKeyType.AGREED) {
                        p.getKeyInfo().getAgreedKey().setParameters((byte[]) data.get(0));
                        p.getKeyInfo().getAgreedKey().setData((byte[]) data.get(1));
                    }
                    list.add(p);
                }
            }
            pushProtectionParameters = list.toArray(new GXPushProtectionParameters[list.size()]);
        } else if (version > 1 && e.getIndex() == 11) {
            pushOperationMethod = PushOperationMethod.forValue(((Number) e.getValue()).intValue());
        } else if (version > 1 && e.getIndex() == 12) {
            GXStructure tmp = (GXStructure) e.getValue();
            if (tmp != null) {
                confirmationParameters.setStartDate((GXDateTime) tmp.get(0));
                confirmationParameters.setInterval(((Number) tmp.get(0)).longValue());
            }
        } else if (version > 1 && e.getIndex() == 13) {
            lastConfirmationDateTime = (GXDateTime) e.getValue();
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        pushObjectList.clear();
        if (reader.isStartElement("ObjectList", true)) {
            while (reader.isStartElement("Item", true)) {
                ObjectType ot = ObjectType.forValue(reader.readElementContentAsInt("ObjectType"));
                String ln = reader.readElementContentAsString("LN");
                int ai = reader.readElementContentAsInt("AI");
                int di = reader.readElementContentAsInt("DI");
                reader.readEndElement("ObjectList");
                GXDLMSCaptureObject co = new GXDLMSCaptureObject(ai, di);
                GXDLMSObject obj = reader.getObjects().findByLN(ot, ln);
                if (obj == null) {
                    obj = GXDLMSClient.createObject(ot);
                    obj.setLogicalName(ln);
                }
                pushObjectList.add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj, co));
            }
            reader.readEndElement("ObjectList");
        }

        service = ServiceType.forValue(reader.readElementContentAsInt("Service"));
        destination = reader.readElementContentAsString("Destination");
        message = MessageType.forValue(reader.readElementContentAsInt("Message"));
        communicationWindow.clear();
        if (reader.isStartElement("CommunicationWindow", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDateTime start = reader.readElementContentAsDateTime("Start");
                GXDateTime end = reader.readElementContentAsDateTime("End");
                communicationWindow.add(new GXSimpleEntry<GXDateTime, GXDateTime>(start, end));
            }
            reader.readEndElement("CommunicationWindow");
        }
        randomisationStartInterval = reader.readElementContentAsInt("RandomisationStartInterval");
        numberOfRetries = reader.readElementContentAsInt("NumberOfRetries");
        if (version < 2) {
            repetitionDelay = reader.readElementContentAsInt("RepetitionDelay");
        } else {
            if (reader.isStartElement("RepetitionDelay", true)) {
                repetitionDelay2.setMin(reader.readElementContentAsInt("Min"));
                repetitionDelay2.setExponent(reader.readElementContentAsInt("Exponent"));
                repetitionDelay2.setMax(reader.readElementContentAsInt("Max"));
            }
        }
        if (version > 0) {
            portReference = null;
            String ln = reader.readElementContentAsString("LN");
            portReference = reader.getObjects().findByLN(ObjectType.NONE, ln);
            if (portReference == null) {
                portReference = GXDLMSClient.createObject(ObjectType.IEC_HDLC_SETUP);
                portReference.setLogicalName(ln);
            }
            pushClientSAP = (byte) reader.readElementContentAsInt("PushClientSAP");
            if (reader.isStartElement("PushProtectionParameters", true)) {
                List<GXPushProtectionParameters> list = new ArrayList<GXPushProtectionParameters>();
                while (reader.isStartElement("Item", true)) {
                    GXPushProtectionParameters it = new GXPushProtectionParameters();
                    it.setProtectionType(ProtectionType.forValue(reader.readElementContentAsInt("ProtectionType")));
                    it.setTransactionId(GXCommon.hexToBytes(reader.readElementContentAsString("TransactionId")));
                    it.setOriginatorSystemTitle(
                            GXCommon.hexToBytes(reader.readElementContentAsString("OriginatorSystemTitle")));
                    it.setRecipientSystemTitle(
                            GXCommon.hexToBytes(reader.readElementContentAsString("RecipientSystemTitle")));
                    it.setOtherInformation(GXCommon.hexToBytes(reader.readElementContentAsString("OtherInformation")));
                    it.getKeyInfo().setDataProtectionKeyType(
                            DataProtectionKeyType.forValue(reader.readElementContentAsInt("DataProtectionKeyType")));
                    it.getKeyInfo().getIdentifiedKey().setKeyType(
                            DataProtectionIdentifiedKeyType.forValue(reader.readElementContentAsInt("IdentifiedKey")));
                    it.getKeyInfo().getWrappedKey().setKeyType(
                            DataProtectionWrappedKeyType.forValue(reader.readElementContentAsInt("WrappedKeyType")));
                    it.getKeyInfo().getWrappedKey()
                            .setKey(GXCommon.hexToBytes(reader.readElementContentAsString("WrappedKey")));
                    it.getKeyInfo().getAgreedKey().setParameters(
                            GXCommon.hexToBytes(reader.readElementContentAsString("WrappedKeyParameters")));
                    it.getKeyInfo().getAgreedKey()
                            .setData(GXCommon.hexToBytes(reader.readElementContentAsString("AgreedKeyData")));
                    list.add(it);
                }
                reader.readEndElement("PushProtectionParameters");
                pushProtectionParameters = list.toArray(new GXPushProtectionParameters[list.size()]);
            }
            if (version > 1) {
                pushOperationMethod =
                        PushOperationMethod.forValue(reader.readElementContentAsInt("PushOperationMethod"));
                confirmationParameters
                        .setStartDate(reader.readElementContentAsDateTime("ConfirmationParametersStartDate"));
                confirmationParameters.setInterval(reader.readElementContentAsLong("ConfirmationParametersInterval"));
                lastConfirmationDateTime = reader.readElementContentAsDateTime("LastConfirmationDateTime");
            }
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (pushObjectList != null) {
            writer.writeStartElement("ObjectList");
            for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pushObjectList) {
                writer.writeStartElement("Item");
                writer.writeElementString("ObjectType", it.getKey().getObjectType().getValue());
                writer.writeElementString("LN", it.getKey().getLogicalName());
                writer.writeElementString("AI", it.getValue().getAttributeIndex());
                writer.writeElementString("DI", it.getValue().getDataIndex());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        if (service != null) {
            writer.writeElementString("Service", service.getValue());
        }
        writer.writeElementString("Destination", destination);
        if (message != null) {
            writer.writeElementString("Message", message.getValue());
        }
        if (communicationWindow != null) {
            writer.writeStartElement("CommunicationWindow");
            for (Entry<GXDateTime, GXDateTime> it : communicationWindow) {
                writer.writeStartElement("Item");
                writer.writeElementString("Start", it.getKey());
                writer.writeElementString("End", it.getValue());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeElementString("RandomisationStartInterval", randomisationStartInterval);
        writer.writeElementString("NumberOfRetries", numberOfRetries);
        if (version < 2) {
            writer.writeElementString("RepetitionDelay", repetitionDelay);
        } else {
            writer.writeStartElement("RepetitionDelay");
            writer.writeElementString("Min", repetitionDelay2.getMin(), 0);
            writer.writeElementString("Exponent", repetitionDelay2.getExponent(), 0);
            writer.writeElementString("Max", repetitionDelay2.getMax(), 0);
            writer.writeEndElement();
        }
        if (version > 0) {
            if (portReference != null) {
                writer.writeElementString("PortReference", portReference.getLogicalName());
            }
            writer.writeElementString("PushClientSAP", pushClientSAP);
            if (pushProtectionParameters != null) {
                writer.writeStartElement("PushProtectionParameters");
                for (GXPushProtectionParameters it : pushProtectionParameters) {
                    writer.writeStartElement("Item");
                    writer.writeElementString("ProtectionType", it.getProtectionType().getValue());
                    writer.writeElementString("TransactionId", GXCommon.toHex(it.getTransactionId(), false));
                    writer.writeElementString("OriginatorSystemTitle",
                            GXCommon.toHex(it.getOriginatorSystemTitle(), false));
                    writer.writeElementString("RecipientSystemTitle",
                            GXCommon.toHex(it.getRecipientSystemTitle(), false));
                    writer.writeElementString("OtherInformation", GXCommon.toHex(it.getOtherInformation(), false));
                    writer.writeElementString("DataProtectionKeyType",
                            it.getKeyInfo().getDataProtectionKeyType().getValue());
                    writer.writeElementString("IdentifiedKey",
                            it.getKeyInfo().getIdentifiedKey().getKeyType().getValue());
                    writer.writeElementString("WrappedKeyType",
                            it.getKeyInfo().getWrappedKey().getKeyType().getValue());
                    writer.writeElementString("WrappedKey",
                            GXCommon.toHex(it.getKeyInfo().getWrappedKey().getKey(), false));
                    writer.writeElementString("WrappedKeyParameters",
                            GXCommon.toHex(it.getKeyInfo().getAgreedKey().getParameters(), false));
                    writer.writeElementString("AgreedKeyData",
                            GXCommon.toHex(it.getKeyInfo().getAgreedKey().getData(), false));
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
            if (version > 1) {
                writer.writeElementString("PushOperationMethod", pushOperationMethod.getValue());
                writer.writeElementString("ConfirmationParametersStartDate", confirmationParameters.getStartDate());
                writer.writeElementString("ConfirmationParametersInterval", confirmationParameters.getInterval());
                writer.writeElementString("LastConfirmationDateTime", lastConfirmationDateTime);
            }
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
        // Update port reference.
        if (portReference != null) {
            GXDLMSObject target =
                    (GXDLMSObject) reader.getObjects().findByLN(ObjectType.NONE, portReference.getLogicalName());
            if (target != null && target != portReference) {
                portReference = target;
            }
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "Logical Name", "Object List", "Send Destination And Method", "Communication Window",
                "Randomisation Start Interval", "Number Of Retries", "Repetition Delay", "Port reference",
                "Push client SAP", "Push protection parameters", "Push operation method", "Confirmation parameters",
                "Last confirmation date time" };

    }

    @Override
    public String[] getMethodNames() {
        if (version < 2) {
            return new String[] { "Push" };
        }
        return new String[] { "Push", "Reset" };
    }

    public GXRepetitionDelay getRepetitionDelay2() {
        return repetitionDelay2;
    }

    public void setRepetitionDelay2(final GXRepetitionDelay value) {
        repetitionDelay2 = value;
    }

    public GXDLMSObject getPortReference() {
        return portReference;
    }

    public void setPortReference(final GXDLMSObject value) {
        portReference = value;
    };

    public byte getPushClientSAP() {
        return pushClientSAP;
    }

    public void setPushClientSAP(final byte value) {
        pushClientSAP = value;
    }

    public GXPushProtectionParameters[] getPushProtectionParameters() {
        return pushProtectionParameters;
    }

    public void setPushProtectionParameters(final GXPushProtectionParameters[] value) {
        pushProtectionParameters = value;
    }

    public PushOperationMethod getPushOperationMethod() {
        return pushOperationMethod;
    }

    public void setPushOperationMethod(final PushOperationMethod value) {
        pushOperationMethod = value;
    }

    public GXPushConfirmationParameter getConfirmationParameters() {
        return confirmationParameters;
    }

    public void setConfirmationParameters(final GXPushConfirmationParameter value) {
        confirmationParameters = value;
    }

    public GXDateTime getLastConfirmationDateTime() {
        return lastConfirmationDateTime;
    }

    public void setLastConfirmationDateTime(final GXDateTime value) {
        lastConfirmationDateTime = value;
    }
}