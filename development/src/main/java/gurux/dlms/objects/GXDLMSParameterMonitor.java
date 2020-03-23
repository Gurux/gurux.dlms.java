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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSParameterMonitor
 */
public class GXDLMSParameterMonitor extends GXDLMSObject
        implements IGXDLMSBase {
    /**
     * Changed parameter.
     */
    private GXDLMSTarget changedParameter;
    /**
     * Capture time.
     */
    private Date captureTime = new Date(0);
    /**
     * Changed Parameter
     */
    private List<GXDLMSTarget> parameters;

    /**
     * Constructor.
     */
    public GXDLMSParameterMonitor() {
        this("0.0.16.2.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSParameterMonitor(final String ln) {
        this(ln, 0);
    }

    /**
     * @return Changed parameter.
     */
    public final GXDLMSTarget getChangedParameter() {
        return changedParameter;
    }

    /**
     * @param value
     *            Changed parameter.
     */
    public final void setChangedParameter(final GXDLMSTarget value) {
        changedParameter = value;
    }

    /**
     * @return Capture time.
     */
    public final Date getCaptureTime() {
        return captureTime;
    }

    /**
     * @param value
     *            Capture time.
     */
    public final void setCaptureTime(final Date value) {
        captureTime = value;
    }

    /**
     * @return Changed Parameter
     */
    public final List<GXDLMSTarget> getParameters() {
        return parameters;
    }

    /**
     * @param value
     *            Changed Parameter
     */
    public final void setParameters(final List<GXDLMSTarget> value) {
        parameters = value;
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSParameterMonitor(final String ln, final int sn) {
        super(ObjectType.PARAMETER_MONITOR, ln, sn);
        parameters = new ArrayList<GXDLMSTarget>();
        changedParameter = new GXDLMSTarget();
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), changedParameter, captureTime,
                parameters };
    }

    /**
     * Inserts a new entry in the table.
     * 
     * @param client
     *            DLMS Client.
     * @param entry
     *            Removed entry.
     * @return If a special day with the same index or with the same date as an
     *         already defined day is inserted, the old entry will be
     *         overwritten.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public final byte[][] insert(GXDLMSClient client, GXDLMSTarget entry)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(3);
        GXCommon.setData(null, bb, DataType.UINT16,
                entry.getTarget().getObjectType().getValue());
        GXCommon.setData(null, bb, DataType.OCTET_STRING, GXCommon
                .logicalNameToBytes(entry.getTarget().getLogicalName()));
        GXCommon.setData(null, bb, DataType.INT8, entry.getAttributeIndex());
        return client.method(this, 1, bb.array(), DataType.ARRAY);
    }

    /**
     * Deletes an entry from the table.
     * 
     * @param client
     *            DLMS Client.
     * @param entry
     *            Removed entry.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public final byte[][] delete(GXDLMSClient client, GXDLMSTarget entry)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(3);
        GXCommon.setData(null, bb, DataType.UINT16,
                entry.getTarget().getObjectType());
        GXCommon.setData(null, bb, DataType.OCTET_STRING, GXCommon
                .logicalNameToBytes(entry.getTarget().getLogicalName()));
        GXCommon.setData(null, bb, DataType.INT8, entry.getAttributeIndex());
        return client.method(this, 2, bb.array(), DataType.ARRAY);
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() != 1 && e.getIndex() != 2) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        } else {
            if (e.getIndex() == 1) {
                List<?> tmp = (List<?>) e.getParameters();
                ObjectType type =
                        ObjectType.forValue(((Number) tmp.get(0)).intValue());
                String ln = GXCommon.toLogicalName((byte[]) tmp.get(1));
                int index = ((Number) tmp.get(2)).intValue();
                for (GXDLMSTarget item : parameters) {
                    if (item.getTarget().getObjectType() == type
                            && item.getTarget().getLogicalName().equals(ln)
                            && item.getAttributeIndex() == index) {
                        parameters.remove(item);
                        break;
                    }
                }
                GXDLMSTarget it = new GXDLMSTarget();
                it.setTarget(settings.getObjects().findByLN(type, ln));
                if (it.getTarget() == null) {
                    it.setTarget(GXDLMSClient.createObject(type));
                    it.getTarget().setLogicalName(ln);
                }
                it.setAttributeIndex(index);
                parameters.add(it);
            } else if (e.getIndex() == 2) {
                List<?> tmp = (List<?>) e.getParameters();
                ObjectType type =
                        ObjectType.forValue(((Number) tmp.get(0)).intValue());
                String ln = GXCommon.toLogicalName((byte[]) tmp.get(1));
                int index = ((Number) tmp.get(2)).intValue();
                for (GXDLMSTarget item : parameters) {
                    if (item.getTarget().getObjectType() == type
                            && item.getTarget().getLogicalName().equals(ln)
                            && item.getAttributeIndex() == index) {
                        parameters.remove(item);
                        break;
                    }
                }
            }
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
        // ChangedParameter
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // CaptureTime
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // Parameters
        if (all || canRead(4)) {
            attributes.add(4);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 4;
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
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.STRUCTURE;
        case 3:
            return DataType.OCTET_STRING;
        case 4:
            return DataType.ARRAY;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2: {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(4);
            if (changedParameter == null) {
                GXCommon.setData(settings, data, DataType.UINT16, 0);
                GXCommon.setData(settings, data, DataType.OCTET_STRING,
                        new byte[] { 0, 0, 0, 0, 0, 0 });
                GXCommon.setData(settings, data, DataType.INT8, 1);
                GXCommon.setData(settings, data, DataType.NONE, null);
            } else {
                GXCommon.setData(settings, data, DataType.UINT16,
                        changedParameter.getTarget().getObjectType()
                                .getValue());
                GXCommon.setData(settings, data, DataType.OCTET_STRING,
                        GXCommon.logicalNameToBytes(
                                changedParameter.getTarget().getLogicalName()));
                GXCommon.setData(settings, data, DataType.INT8,
                        changedParameter.getAttributeIndex());
                GXCommon.setData(settings, data,
                        GXDLMSConverter
                                .getDLMSDataType(changedParameter.getValue()),
                        changedParameter.getValue());
            }
            return data.array();
        }
        case 3:
            return captureTime;
        case 4: {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (parameters == null) {
                data.setUInt8(0);
            } else {
                data.setUInt8(parameters.size());
                for (GXDLMSTarget it : parameters) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    data.setUInt8(3);
                    GXCommon.setData(settings, data, DataType.UINT16,
                            it.getTarget().getObjectType().getValue());
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(
                                    it.getTarget().getLogicalName()));
                    GXCommon.setData(settings, data, DataType.INT8,
                            it.getAttributeIndex());
                }
            }
            return data.array();
        }
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2: {
            changedParameter = new GXDLMSTarget();
            if (e.getValue() instanceof List<?>) {
                List<?> tmp = (List<?>) e.getValue();
                if (tmp.size() != 4) {
                    throw new GXDLMSException("Invalid structure format.");
                }
                ObjectType type =
                        ObjectType.forValue(((Number) tmp.get(0)).intValue());
                String ln = GXCommon.toLogicalName((byte[]) tmp.get(1));
                changedParameter
                        .setTarget(settings.getObjects().findByLN(type, ln));
                if (changedParameter.getTarget() == null) {
                    changedParameter.setTarget(GXDLMSClient.createObject(type));
                    changedParameter.getTarget().setLogicalName(ln);
                }
                changedParameter
                        .setAttributeIndex(((Number) tmp.get(2)).intValue());
                changedParameter.setValue(tmp.get(3));
            }
            break;
        }

        case 3:
            if (e.getValue() == null) {
                captureTime = new Date(0);
            } else {
                GXDateTime tmp;
                if (e.getValue() instanceof byte[]) {
                    boolean useUtc;
                    if (e.getSettings() != null) {
                        useUtc = e.getSettings().getUseUtc2NormalTime();
                    } else {
                        useUtc = false;
                    }
                    tmp = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) e.getValue(), DataType.DATETIME, useUtc);
                } else {
                    tmp = (GXDateTime) e.getValue();
                }
                captureTime = tmp.getLocalCalendar().getTime();
            }
            break;
        case 4: {
            parameters.clear();
            if (e.getValue() != null) {
                for (Object i : (List<?>) e.getValue()) {
                    List<?> tmp = (List<?>) i;
                    if (tmp.size() != 3) {
                        throw new GXDLMSException("Invalid structure format.");
                    }
                    GXDLMSTarget obj = new GXDLMSTarget();
                    ObjectType type = ObjectType
                            .forValue(((Number) tmp.get(0)).intValue());
                    String ln = GXCommon.toLogicalName((byte[]) tmp.get(1));
                    obj.setTarget(settings.getObjects().findByLN(type, ln));
                    if (obj.getTarget() == null) {
                        obj.setTarget(GXDLMSClient.createObject(type));
                        obj.getTarget().setLogicalName(
                                GXCommon.toLogicalName((byte[]) tmp.get(1)));
                    }
                    obj.setAttributeIndex(((Number) tmp.get(2)).intValue());
                    parameters.add(obj);
                }
            }
            break;
        }
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        changedParameter = new GXDLMSTarget();
        if (reader.isStartElement("ChangedParameter", true)) {
            ObjectType ot =
                    ObjectType.forValue(reader.readElementContentAsInt("Type"));
            String ln = reader.readElementContentAsString("LN");
            changedParameter.setTarget(reader.getObjects().findByLN(ot, ln));
            if (changedParameter.getTarget() == null) {
                changedParameter.setTarget(GXDLMSClient.createObject(ot));
                changedParameter.getTarget().setLogicalName(ln);
            }
            changedParameter
                    .setAttributeIndex(reader.readElementContentAsInt("Index"));
            changedParameter.setValue(
                    reader.readElementContentAsObject("Value", null, null, 0));
            reader.readEndElement("ChangedParameter");
        }
        GXDateTime tmp = reader.readElementContentAsDateTime("Time");
        captureTime = null;
        if (tmp != null) {
            captureTime = tmp.getMeterCalendar().getTime();
        }
        parameters.clear();
        if (reader.isStartElement("Parameters", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSTarget obj = new GXDLMSTarget();
                ObjectType ot = ObjectType
                        .forValue(reader.readElementContentAsInt("Type"));
                String ln = reader.readElementContentAsString("LN");
                obj.setTarget(reader.getObjects().findByLN(ot, ln));
                if (obj.getTarget() == null) {
                    obj.setTarget(GXDLMSClient.createObject(ot));
                    obj.getTarget().setLogicalName(ln);
                }
                obj.setAttributeIndex(reader.readElementContentAsInt("Index"));
                parameters.add(obj);
            }
            reader.readEndElement("Parameters");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (changedParameter != null && changedParameter.getTarget() != null) {
            writer.writeStartElement("ChangedParameter");
            writer.writeElementString("Type",
                    changedParameter.getTarget().getObjectType().getValue());
            writer.writeElementString("LN",
                    changedParameter.getTarget().getLogicalName());
            writer.writeElementString("Index",
                    changedParameter.getAttributeIndex());
            writer.writeElementObject("Value", changedParameter.getValue());
            writer.writeEndElement();
        }
        if (captureTime != null && captureTime.compareTo(new Date(0)) != 0) {
            writer.writeElementString("Time", captureTime);
        }
        if (parameters != null && parameters.size() != 0) {
            writer.writeStartElement("Parameters");
            for (GXDLMSTarget it : parameters) {
                writer.writeStartElement("Item");
                writer.writeElementString("Type",
                        it.getTarget().getObjectType().getValue());
                writer.writeElementString("LN",
                        it.getTarget().getLogicalName());
                writer.writeElementString("Index", it.getAttributeIndex());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}