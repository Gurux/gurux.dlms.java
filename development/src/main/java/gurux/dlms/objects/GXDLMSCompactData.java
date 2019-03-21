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

import java.util.List;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.CaptureMethod;

/**
 * Online help:<br>
 * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCompactData
 */
public class GXDLMSCompactData extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Compact buffer
     */
    private byte[] buffer;

    /**
     * Capture objects.
     */
    private List<Entry<GXDLMSObject, GXDLMSCaptureObject>> captureObjects;

    /**
     * Template ID.
     */
    private short templateId;

    /**
     * Template description.
     */
    private byte[] templateDescription;

    /**
     * Capture method.
     */
    private CaptureMethod captureMethod;

    /**
     * Constructor.
     */
    public GXDLMSCompactData() {
        this(null, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSCompactData(final String ln) {
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
    public GXDLMSCompactData(final String ln, final int sn) {
        super(ObjectType.COMPACT_DATA, ln, sn);
        captureMethod = CaptureMethod.INVOKE;
    }

    /**
     * @return Compact buffer
     */
    public final byte[] getBuffer() {
        return buffer;
    }

    /**
     * @param value
     *            Compact buffer
     */
    public final void setBuffer(final byte[] value) {
        buffer = value;
    }

    /**
     * @return Capture objects.
     */
    public final List<Entry<GXDLMSObject, GXDLMSCaptureObject>>
            getCaptureObjects() {
        return captureObjects;
    }

    /**
     * @return Template ID.
     */
    public final short getTemplateId() {
        return templateId;
    }

    /**
     * @param value
     *            Template ID.
     */
    public final void setTemplateId(final short value) {
        templateId = value;
    }

    /**
     * @return Template description.
     */
    public final byte[] getTemplateDescription() {
        return templateDescription;
    }

    /**
     * @param value
     *            Template description.
     */
    public final void setTemplateDescription(final byte[] value) {
        templateDescription = value;
    }

    /**
     * @return Capture method.
     */
    public final CaptureMethod getCaptureMethod() {
        return captureMethod;
    }

    /**
     * @param value
     *            Capture method.
     */
    public final void setCaptureMethod(final CaptureMethod value) {
        captureMethod = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), buffer, captureObjects,
                templateId, templateDescription, captureMethod };
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
            attributes.add(new Integer(1));
        }
        // Buffer
        if (all || canRead(2)) {
            attributes.add(new Integer(2));
        }
        // CaptureObjects
        if (all || canRead(3)) {
            attributes.add(new Integer(3));
        }
        // TemplateId
        if (all || canRead(4)) {
            attributes.add(new Integer(4));
        }
        // TemplateDescription
        if (all || canRead(5)) {
            attributes.add(new Integer(5));
        }
        // CaptureMethod
        if (all || canRead(6)) {
            attributes.add(new Integer(6));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 6;
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
            return DataType.OCTET_STRING;
        case 3:
            return DataType.ARRAY;
        case 4:
            return DataType.UINT8;
        case 5:
            return DataType.OCTET_STRING;
        case 6:
            return DataType.ENUM;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /**
     * Returns captured objects.
     * 
     * @param settings
     *            DLMS settings.
     * @return
     */
    private byte[] getCaptureObjects(GXDLMSSettings settings) {
        GXByteBuffer data = new GXByteBuffer();
        data.setUInt8(DataType.ARRAY.getValue());
        // Add count
        GXCommon.setObjectCount(captureObjects.size(), data);
        for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
            data.setUInt8(DataType.STRUCTURE.getValue());
            // Count
            data.setUInt8(4);
            // ClassID
            GXCommon.setData(data, DataType.UINT16,
                    it.getKey().getObjectType().getValue());
            // LN
            GXCommon.setData(data, DataType.OCTET_STRING,
                    GXCommon.logicalNameToBytes(it.getKey().getLogicalName()));
            // Selected Attribute Index
            GXCommon.setData(data, DataType.INT8,
                    it.getValue().getAttributeIndex());
            // Selected Data Index
            GXCommon.setData(data, DataType.UINT16,
                    it.getValue().getDataIndex());
        }
        return data.array();
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
        case 2:
            return buffer;
        case 3:
            return getCaptureObjects(settings);
        case 4:
            return templateId;
        case 5:
            return templateDescription;
        case 6:
            return captureMethod;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    private static void setCaptureObjects(GXDLMSSettings settings,
            List<Entry<GXDLMSObject, GXDLMSCaptureObject>> list,
            Object[] array) {
        GXDLMSConverter c = null;
        try {
            if (array != null) {
                for (Object it : array) {
                    Object[] tmp = (Object[]) it;
                    if (tmp.length != 4) {
                        throw new GXDLMSException("Invalid structure format.");
                    }
                    int v = (short) (tmp[0]);
                    ObjectType type = ObjectType.forValue(v);
                    String ln = GXCommon.toLogicalName((byte[]) tmp[1]);
                    int attributeIndex = (short) (tmp[2]);
                    int dataIndex = (short) (tmp[3]);
                    GXDLMSObject obj = null;
                    if (settings != null && settings.getObjects() != null) {
                        obj = settings.getObjects().findByLN(type, ln);
                    }
                    if (obj == null) {
                        obj = gurux.dlms.GXDLMSClient.createObject(type);
                        if (c == null) {
                            c = new GXDLMSConverter();
                        }
                        c.updateOBISCodeInformation(obj);
                    }
                    list.add(
                            new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(
                                    obj, new GXDLMSCaptureObject(attributeIndex,
                                            dataIndex)));
                }
            }
        } catch (RuntimeException e) {
            list.clear();
        }
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
        case 2:
            if (e.getValue() instanceof byte[]) {
                buffer = (byte[]) e.getValue();
            } else if (e.getValue() instanceof String) {
                buffer = GXCommon.hexToBytes((String) e.getValue());
            }
            break;
        case 3:
            setCaptureObjects(settings, captureObjects,
                    (Object[]) e.getValue());
            break;
        case 4:
            templateId = (short) e.getValue();
            break;
        case 5:
            templateDescription = (byte[]) e.getValue();
            break;
        case 6:
            captureMethod = CaptureMethod.values()[(short) e.getValue()];
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }

    /*
     * Copies the values of the objects to capture into the buffer by reading
     * capture objects.
     */
    public final void capture(final Object server) throws Exception {
        synchronized (this) {
            GXDLMSServerBase srv = (GXDLMSServerBase) server;
            GXByteBuffer bb = new GXByteBuffer();
            ValueEventArgs[] args = new ValueEventArgs[] {
                    new ValueEventArgs(srv, this, 2, 0, null) };
            buffer = null;
            srv.notifyPreGet(args);
            if (!args[0].getHandled()) {
                for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
                    ValueEventArgs e = new ValueEventArgs(srv, it.getKey(),
                            it.getValue().getAttributeIndex(), 0, null);
                    Object value = it.getKey().getValue(srv.getSettings(), e);
                    if (value instanceof byte[]) {
                        bb.set((byte[]) value);
                    } else {
                        GXByteBuffer tmp = new GXByteBuffer();
                        GXCommon.setData(tmp,
                                it.getKey().getDataType(
                                        it.getValue().getAttributeIndex()),
                                value);
                        // If data is empty.
                        if (tmp.size() == 1) {
                            bb.setUInt8(0);
                        } else {
                            tmp.position(1);
                            bb.set(tmp);
                        }
                    }
                }
                buffer = bb.array();
            }
            srv.notifyPostGet(args);
            srv.notifyAction(args);
            srv.notifyPostAction(args);
        }
    }

    /**
     * Update template description.
     */
    public final void updateTemplateDescription() {
        synchronized (this) {
            GXByteBuffer bb = new GXByteBuffer();
            buffer = null;
            for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
                bb.setUInt8(it.getKey()
                        .getDataType(it.getValue().getAttributeIndex())
                        .getValue());
            }
            templateDescription = bb.array();
        }
    }
}