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

package gurux.dlms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSPushSetup;

/**
 * This class is used to send data notify and push messages to the clients.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSNotify {

    /**
     * DLMS settings.
     */
    private final GXDLMSSettings settings = new GXDLMSSettings(true);

    /**
     * Constructor.
     * 
     * @param useLogicalNameReferencing
     *            Is Logical Name referencing used.
     * @param clientAddress
     *            Server address.
     * @param serverAddress
     *            Client address.
     * @param interfaceType
     *            Object type.
     */
    public GXDLMSNotify(final boolean useLogicalNameReferencing,
            final int clientAddress, final int serverAddress,
            final InterfaceType interfaceType) {
        setUseLogicalNameReferencing(useLogicalNameReferencing);
        settings.setClientAddress(clientAddress);
        settings.setServerAddress(serverAddress);
        settings.setInterfaceType(interfaceType);
    }

    /**
     * What kind of services are used.
     * 
     * @return Functionality.
     */
    public final java.util.Set<Conformance> getConformance() {
        return settings.getNegotiatedConformance();
    }

    /**
     * @param value
     *            What kind of services are used.
     */
    public final void setConformance(final java.util.Set<Conformance> value) {
        settings.setNegotiatedConformance(value);
    }

    /**
     * @param value
     *            Cipher interface that is used to cipher PDU.
     */
    protected final void setCipher(final GXICipher value) {
        settings.setCipher(value);
    }

    /**
     * @return Get list of meter's objects.
     */
    public final GXDLMSObjectCollection getObjects() {
        return settings.getObjects();
    }

    /**
     * @return Information from the connection size that server can handle.
     */
    public final GXDLMSLimits getLimits() {
        return settings.getLimits();
    }

    /**
     * Retrieves the maximum size of received PDU. PDU size tells maximum size
     * of PDU packet. Value can be from 0 to 0xFFFF. By default the value is
     * 0xFFFF.
     * 
     * @see GXDLMSClient#getClientAddress
     * @see GXDLMSClient#getServerAddress
     * @see GXDLMSClient#getUseLogicalNameReferencing
     * @return Maximum size of received PDU.
     */
    public final int getMaxReceivePDUSize() {
        return settings.getMaxPduSize();
    }

    /**
     * @param value
     *            Maximum size of received PDU.
     */
    public final void setMaxReceivePDUSize(final int value) {
        settings.setMaxPduSize(value);
    }

    /**
     * Determines, whether Logical, or Short name, referencing is used.
     * Referencing depends on the device to communicate with. Normally, a device
     * supports only either Logical or Short name referencing. The referencing
     * is defined by the device manufacturer. If the referencing is wrong, the
     * SNMR message will fail.
     * 
     * @return Is Logical Name referencing used.
     */
    public final boolean getUseLogicalNameReferencing() {
        return settings.getUseLogicalNameReferencing();
    }

    /**
     * @param value
     *            Is Logical Name referencing used.
     */
    public final void setUseLogicalNameReferencing(final boolean value) {
        settings.setUseLogicalNameReferencing(value);
    }

    /**
     * @return Used Priority.
     */
    public final Priority getPriority() {
        return settings.getPriority();
    }

    /**
     * @param value
     *            Used Priority.
     */
    public final void setPriority(final Priority value) {
        settings.setPriority(value);
    }

    /**
     * @return Used service class.
     */
    public final ServiceClass getServiceClass() {
        return settings.getServiceClass();
    }

    /**
     * @param value
     *            Used service class.
     */
    public final void setServiceClass(final ServiceClass value) {
        settings.setServiceClass(value);
    }

    /**
     * @return Invoke ID.
     */
    public final int getInvokeID() {
        return settings.getInvokeID();
    }

    /**
     * @param value
     *            Invoke ID.
     */
    public final void setInvokeID(final byte value) {
        settings.setInvokeID(value);
    }

    /**
     * Removes the HDLC frame from the packet, and returns COSEM data only.
     * 
     * @param reply
     *            The received data from the device.
     * @param data
     *            Information from the received data.
     * @return Is frame complete.
     */
    public final boolean getData(final GXByteBuffer reply,
            final GXReplyData data) {
        return GXDLMS.getData(settings, reply, data);
    }

    /**
     * Add value of COSEM object to byte buffer. AddData method can be used with
     * GetDataNotificationMessage -method. DLMS specification do not specify the
     * structure of Data-Notification body. So each manufacture can sent
     * different data.
     * 
     * @param obj
     *            COSEM object.
     * @param index
     *            Attribute index.
     * @param buff
     *            Byte buffer.
     */
    public final void addData(final GXDLMSObject obj, final int index,
            final GXByteBuffer buff) {
        DataType dt;
        ValueEventArgs e = new ValueEventArgs(settings, obj, index, 0, null);
        Object value = obj.getValue(settings, e);
        dt = obj.getDataType(index);
        if (dt == DataType.NONE && value != null) {
            dt = GXDLMSConverter.getDLMSDataType(value);
        }
        GXCommon.setData(buff, dt, value);
    }

    /**
     * Add value of COSEM object to byte buffer. AddData method can be used with
     * GetDataNotificationMessage -method. DLMS specification do not specify the
     * structure of Data-Notification body. So each manufacture can sent
     * different data.
     * 
     * @param value
     *            Added value.
     * @param type
     *            Value data type.
     * @param buff
     *            Byte buffer.
     */
    public final void addData(final Object value, final DataType type,
            final GXByteBuffer buff) {
        GXCommon.setData(buff, type, value);
    }

    /**
     * Generates data notification message.
     * 
     * @param time
     *            Date time. Set to null or Date(0) if not used
     * @param data
     *            Notification body.
     * @return Generated data notification message(s).
     */
    public final byte[][] generateDataNotificationMessages(final Date time,
            final byte[] data) {
        return generateDataNotificationMessages(time, new GXByteBuffer(data));
    }

    /**
     * Generates data notification message.
     * 
     * @param time
     *            Date time. Set Date(0) if not added.
     * @param data
     *            Notification body.
     * @return Generated data notification message(s).
     */
    public final byte[][] generateDataNotificationMessages(final Date time,
            final GXByteBuffer data) {
        List<byte[]> reply;
        if (getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.DATA_NOTIFICATION, 0, null, data, 0xff);
            if (time == null) {
                p.setTime(null);
            } else {
                p.setTime(new GXDateTime(time));
            }
            reply = GXDLMS.getLnMessages(p);
        } else {
            GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                    Command.DATA_NOTIFICATION, 1, 0, data, null);
            reply = GXDLMS.getSnMessages(p);
        }
        if (!settings.getNegotiatedConformance()
                .contains(Conformance.GENERAL_BLOCK_TRANSFER)
                && reply.size() != 1) {
            throw new IllegalArgumentException(
                    "Data is not fit to one PDU. Use general block transfer.");
        }
        return reply.toArray(new byte[0][0]);
    }

    /**
     * Generates data notification message.
     * 
     * @param date
     *            Date time. Set To null if not added.
     * @param objects
     *            List of objects and attribute indexes to notify.
     * @return Generated data notification message(s).
     */
    public final byte[][] generateDataNotificationMessages(final Date date,
            final List<Entry<GXDLMSObject, Integer>> objects) {
        if (objects == null) {
            throw new IllegalArgumentException("objects");
        }
        GXByteBuffer buff = new GXByteBuffer();
        buff.setUInt8((byte) DataType.STRUCTURE.getValue());
        GXCommon.setObjectCount(objects.size(), buff);
        for (Entry<GXDLMSObject, Integer> it : objects) {
            addData(it.getKey(), it.getValue(), buff);
        }
        return generateDataNotificationMessages(date, buff);
    }

    /**
     * Generates push setup message.
     * 
     * @param date
     *            Date time. Set to null or Date(0) if not used.
     * @param push
     *            Target Push object.
     * @return Generated data notification message(s).
     */
    public final byte[][] generatePushSetupMessages(final Date date,
            final GXDLMSPushSetup push) {
        if (push == null) {
            throw new IllegalArgumentException("push");
        }
        GXByteBuffer buff = new GXByteBuffer();
        buff.setUInt8((byte) DataType.STRUCTURE.getValue());
        GXCommon.setObjectCount(push.getPushObjectList().size(), buff);
        for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : push
                .getPushObjectList()) {
            addData(it.getKey(), it.getValue().getAttributeIndex(), buff);
        }
        return generateDataNotificationMessages(date, buff);
    }

    /**
     * Returns collection of push objects. If this method is used Push object
     * must be set for first object on push object list.
     * 
     * @param data
     *            Received value.
     * @return Array of objects and called indexes.
     */
    public final List<Entry<GXDLMSObject, Integer>>
            parsePush(final Object[] data) {
        GXDLMSObject obj;
        int index;
        DataType dt;
        Object value;
        List<Entry<GXDLMSObject, Integer>> items =
                new ArrayList<Entry<GXDLMSObject, Integer>>();
        if (data != null) {
            GXDLMSConverter c = new GXDLMSConverter();
            for (Object it : (Object[]) data[0]) {
                Object[] tmp = (Object[]) it;
                int classID = ((Number) (tmp[0])).intValue() & 0xFFFF;
                if (classID > 0) {
                    GXDLMSObject comp;
                    comp = getObjects().findByLN(ObjectType.forValue(classID),
                            GXCommon.toLogicalName((byte[]) tmp[1]));
                    if (comp == null) {
                        comp = GXDLMSClient.createDLMSObject(classID, 0, 0,
                                tmp[1], null);
                        settings.getObjects().add(comp);
                        c.updateOBISCodeInformation(comp);
                    }
                    if (comp.getClass() != GXDLMSObject.class) {
                        items.add(new GXSimpleEntry<GXDLMSObject, Integer>(comp,
                                ((Number) tmp[2]).intValue()));
                    } else {
                        System.out.println("Unknown object: "
                                + String.valueOf(classID) + " "
                                + GXCommon.toLogicalName((byte[]) tmp[1]));
                    }
                }
            }
            for (int pos = 0; pos < data.length; ++pos) {
                obj = (GXDLMSObject) items.get(pos).getKey();
                value = data[pos];
                index = items.get(pos).getValue();
                if (value instanceof byte[]) {
                    dt = obj.getUIDataType(index);
                    if (dt != DataType.NONE) {
                        value = GXDLMSClient.changeType((byte[]) value, dt);
                    }
                }
                ValueEventArgs e =
                        new ValueEventArgs(settings, obj, index, 0, null);
                e.setValue(value);
                obj.setValue(settings, e);
                e.setValue(value);

                e = new ValueEventArgs(settings, items.get(pos).getKey(),
                        items.get(pos).getValue(), 0, null);
                e.setValue(data[pos]);
                items.get(pos).getKey().setValue(settings, e);
            }
        }
        return items;
    }

    /**
     * Returns collection of push objects.
     * 
     * @param objects
     *            Array of objects and called indexes.
     * @param data
     *            Received data.
     */
    public final void parsePush(
            final List<Entry<GXDLMSObject, Integer>> objects,
            final Object[] data) {
        GXDLMSObject obj;
        int index;
        DataType dt;
        Object value;
        if (data == null) {
            throw new IllegalArgumentException("Invalid push message.");
        }
        if (data.length != objects.size()) {
            throw new IllegalArgumentException("Push arguments do not match.");
        }
        for (int pos = 0; pos < data.length; ++pos) {
            obj = (GXDLMSObject) objects.get(pos).getKey();
            value = data[pos];
            index = objects.get(pos).getValue();
            if (value instanceof byte[]) {
                dt = obj.getUIDataType(index);
                if (dt != DataType.NONE) {
                    value = GXDLMSClient.changeType((byte[]) value, dt);
                }
            }
            ValueEventArgs e =
                    new ValueEventArgs(settings, obj, index, 0, null);
            e.setValue(value);
            obj.setValue(settings, e);
            e.setValue(value);
        }
    }

    /**
     * Sends Event Notification or Information Report Request.
     * 
     * @param time
     *            Send time.
     * @param list
     *            List of COSEM object and attribute index to report.
     * @return Report request as byte array.
     */
    public byte[][] generateReport(final GXDateTime time,
            final List<Entry<GXDLMSObject, Integer>> list) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("list");
        }
        if (getUseLogicalNameReferencing() && list.size() != 1) {
            throw new IllegalArgumentException(
                    "Only one object can send with Event Notification request.");
        }

        GXByteBuffer buff = new GXByteBuffer();
        List<byte[]> reply;
        if (getUseLogicalNameReferencing()) {
            for (Entry<GXDLMSObject, Integer> it : list) {
                buff.setUInt16(it.getKey().getObjectType().getValue());
                buff.set(GXCommon
                        .logicalNameToBytes(it.getKey().getLogicalName()));
                buff.setUInt8(it.getValue());
                addData(it.getKey(), it.getValue(), buff);
            }
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.EVENT_NOTIFICATION, 0, null, buff, 0xff);
            p.setTime(time);
            reply = GXDLMS.getLnMessages(p);
        } else {
            GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                    Command.INFORMATION_REPORT, list.size(), 0xFF, null, buff);
            for (Entry<GXDLMSObject, Integer> it : list) {
                // Add variable type.
                buff.setUInt8(VariableAccessSpecification.VARIABLE_NAME);
                int sn = it.getKey().getShortName();
                sn += (it.getValue() - 1) * 8;
                buff.setUInt16(sn);
            }
            GXCommon.setObjectCount(list.size(), buff);
            for (Entry<GXDLMSObject, Integer> it : list) {
                addData(it.getKey(), it.getValue(), buff);
            }
            reply = GXDLMS.getSnMessages(p);
        }
        return reply.toArray(new byte[0][0]);
    }
}
