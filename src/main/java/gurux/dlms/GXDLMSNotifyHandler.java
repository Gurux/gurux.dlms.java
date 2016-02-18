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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;

/**
 * This class is used to send data notify and push messages to the clients.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSNotifyHandler {

    /**
     * DLMS settings.
     */
    private final GXDLMSSettings settings = new GXDLMSSettings(false);

    /**
     * Cipher interface that is used to cipher PDU.
     */
    private GXICipher cipher;

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
    public GXDLMSNotifyHandler(final boolean useLogicalNameReferencing,
            final int clientAddress, final int serverAddress,
            final InterfaceType interfaceType) {
        setUseLogicalNameReferencing(useLogicalNameReferencing);
        settings.setClientAddress(clientAddress);
        settings.setServerAddress(serverAddress);
        settings.setInterfaceType(interfaceType);
    }

    /**
     * @return Get settings.
     */
    protected final GXDLMSSettings getSettings() {
        return settings;
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
     * @see GXDLMSClient#getDLMSVersion
     * @see GXDLMSClient#getUseLogicalNameReferencing
     * @return Maximum size of received PDU.
     */
    public final int getMaxReceivePDUSize() {
        return settings.getMaxReceivePDUSize();
    }

    /**
     * @param value
     *            Maximum size of received PDU.
     */
    public final void setMaxReceivePDUSize(final int value) {
        settings.setMaxReceivePDUSize(value);
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
    public final void setInvokeID(final int value) {
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
    public final boolean getData(final byte[] reply, final GXReplyData data) {
        return GXDLMS.getData(settings, new GXByteBuffer(reply), data, cipher);
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
        Object value = obj.getValue(settings, index, 0, null);
        dt = obj.getDataType(index);
        if (dt == DataType.NONE && value != null) {
            dt = GXCommon.getValueType(value);
        }
        GXCommon.setData(buff, dt, value);
    }

    /**
     * Add value of COSEM object to byte buffer. AddData method can be used with
     * GetDataNotificationMessage -method. DLMS spesification do not specify the
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
     * @param date
     *            Date time. Set Date(0) if not added.
     * @param data
     *            Notification body.
     * @return Generated data notification message(s).
     */
    public final byte[][] getDataNotificationMessage(final Date date,
            final byte[] data) {
        GXByteBuffer buff = new GXByteBuffer();
        if (date == null || date == new Date(0)) {
            buff.setUInt8((byte) DataType.NONE.getValue());
        } else {
            GXCommon.setData(buff, DataType.DATETIME, date);
        }
        buff.set(data);
        List<byte[][]> list = GXDLMS.splitPdu(settings,
                Command.DATA_NOTIFICATION, 0, buff, ErrorCode.OK, null, cipher);
        List<byte[]> arr = new ArrayList<byte[]>();
        for (byte[][] it : list) {
            arr.addAll(Arrays.asList(it));
        }
        return arr.toArray(new byte[arr.size()][0]);
    }

    /**
     * Generates data notification message.
     * 
     * @param date
     *            Date time. Set Date(0) if not added.
     * @param data
     *            Notification body.
     * @return Generated data notification message(s).
     */
    public final byte[][] getDataNotificationMessage(final Date date,
            final GXByteBuffer data) {
        return getDataNotificationMessage(date, data.array());
    }

    /**
     * Generates data notification message.
     * 
     * @param date
     *            Date time. Set To Min or Max if not added.
     * @param objects
     *            List of objects and attribute indexes to notify.
     * @return Generated data notification message(s).
     */
    public final byte[][] generateDataNotificationMessage(final Date date,
            final List<SimpleEntry<GXDLMSObject, Integer>> objects) {
        if (objects == null) {
            throw new IllegalArgumentException("objects");
        }
        GXByteBuffer buff = new GXByteBuffer();
        buff.setUInt8((byte) DataType.ARRAY.getValue());
        GXCommon.setObjectCount(objects.size(), buff);
        for (SimpleEntry<GXDLMSObject, Integer> it : objects) {
            addData(it.getKey(), it.getValue(), buff);
        }
        return getDataNotificationMessage(date, buff);
    }

    /**
     * Returns collection of push objects.
     * 
     * @param data
     *            Received data.
     * @return Array of objects and called indexes.
     */
    public final List<SimpleEntry<GXDLMSObject, Integer>>
            parsePushObjects(final GXByteBuffer data) {
        GXDLMSObject obj;
        int index;
        DataType dt;
        Object value;
        GXReplyData reply = new GXReplyData();
        reply.setData(data);
        List<SimpleEntry<GXDLMSObject, Integer>> items =
                new ArrayList<SimpleEntry<GXDLMSObject, Integer>>();
        GXDLMS.getValueFromData(settings, reply);
        Object[] list = (Object[]) reply.getValue();
        for (Object it : (Object[]) list[0]) {
            Object[] tmp = (Object[]) it;
            int classID = ((Number) (tmp[0])).intValue() & 0xFFFF;
            if (classID > 0) {
                GXDLMSObject comp;
                comp = getObjects().findByLN(ObjectType.forValue(classID),
                        GXDLMSObject.toLogicalName((byte[]) tmp[1]));
                if (comp == null) {
                    comp = GXDLMSClient.createDLMSObject(classID, 0, 0, tmp[1],
                            null);
                    settings.getObjects().add(comp);
                }
                if (comp.getClass() != GXDLMSObject.class) {
                    items.add(new SimpleEntry<GXDLMSObject, Integer>(comp,
                            ((Number) tmp[2]).intValue()));
                } else {
                    System.out.println(String.format("Unknown object : %d %s",
                            classID,
                            GXDLMSObject.toLogicalName((byte[]) tmp[1])));
                }
            }
        }
        GXDLMSClient.updateOBISCodes(settings.getObjects());
        for (int pos = 0; pos < list.length; ++pos) {
            obj = (GXDLMSObject) items.get(pos).getKey();
            value = list[pos];
            index = items.get(pos).getValue();
            if (value instanceof byte[]) {
                dt = obj.getUIDataType(index);
                if (dt != DataType.NONE) {
                    value = GXDLMSClient.changeType((byte[]) value, dt);
                }
            }
            obj.setValue(settings, index, value);

            items.get(pos).getKey().setValue(settings,
                    items.get(pos).getValue(), list[pos]);
        }
        return items;
    }

    /**
     * Generates Push message.
     * 
     * @param date
     *            Date time. Set To Min or Max if not added.
     * @param objects
     *            List of objects and attribute indexes to push.
     * @return Generated push message(s).
     */
    public final byte[][] generatePushMessage(final Date date,
            final List<SimpleEntry<GXDLMSObject, Integer>> objects) {
        DataType dt;
        Object value;
        if (objects == null) {
            throw new IllegalArgumentException("objects");
        }
        GXByteBuffer buff = new GXByteBuffer();
        // Add data
        buff.setUInt8(DataType.ARRAY.getValue());
        GXCommon.setObjectCount(objects.size(), buff);
        for (SimpleEntry<GXDLMSObject, Integer> it : objects) {
            dt = it.getKey().getDataType(it.getValue());
            value = it.getKey().getValue(settings, it.getValue(), 0, null);
            if (dt == DataType.NONE && value != null) {
                dt = GXCommon.getValueType(value);
            }
            GXCommon.setData(buff, dt, value);
        }
        List<byte[][]> list = GXDLMS.splitPdu(settings, Command.PUSH, 0, buff,
                ErrorCode.OK, null, cipher);
        List<byte[]> arr = new ArrayList<byte[]>();

        for (byte[][] it : list) {
            arr.addAll(Arrays.asList(it));
        }
        return arr.toArray(new byte[arr.size()][0]);
    }
}
