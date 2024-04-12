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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXEnum;
import gurux.dlms.GXStructure;
import gurux.dlms.GXUInt32;
import gurux.dlms.GXUInt8;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.MBusLinkStatus;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSMBusDiagnostic
 */
public class GXDLMSMBusDiagnostic extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Received signal strength in dBm.
     */
    private short receivedSignalStrength;

    /**
     * Currently used channel ID.
     */
    private short channelId;

    /**
     * Link status.
     */
    private MBusLinkStatus linkStatus = MBusLinkStatus.NONE;

    /**
     * Broadcast frame counters.
     */
    private java.util.ArrayList<GXBroadcastFrameCounter> broadcastFrames;

    /**
     * Transmitted frames.
     */
    private long transmissions;

    /**
     * Received frames with a correct checksum.
     */
    private long receivedFrames;

    /**
     * Received frames with a incorrect checksum.
     */
    private long failedReceivedFrames;

    /**
     * Constructor.
     */
    public GXDLMSMBusDiagnostic() {
        this("0.0.24.9.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSMBusDiagnostic(String ln) {
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
    public GXDLMSMBusDiagnostic(String ln, int sn) {
        super(ObjectType.MBUS_DIAGNOSTIC, ln, sn);
        setBroadcastFrames(new java.util.ArrayList<GXBroadcastFrameCounter>());
        setCaptureTime(new GXCaptureTime());
    }

    /**
     * @return Received signal strength in dBm.
     */
    public final short getReceivedSignalStrength() {
        return receivedSignalStrength;
    }

    /**
     * @param value
     *            Received signal strength in dBm.
     */
    public final void setReceivedSignalStrength(final short value) {
        receivedSignalStrength = value;
    }

    /**
     * @return Currently used channel ID.
     */
    public final short getChannelId() {
        return channelId;
    }

    /**
     * @param value
     *            Currently used channel ID.
     */
    public final void setChannelId(final short value) {
        channelId = value;
    }

    /**
     * @return Link status.
     */
    public final MBusLinkStatus getLinkStatus() {
        return linkStatus;
    }

    /**
     * @param value
     *            Link status.
     */
    public final void setLinkStatus(final MBusLinkStatus value) {
        linkStatus = value;
    }

    /**
     * @return Broadcast frame counters.
     */
    public final java.util.ArrayList<GXBroadcastFrameCounter> getBroadcastFrames() {
        return broadcastFrames;
    }

    /**
     * @param value
     *            Broadcast frame counters.
     */
    public final void setBroadcastFrames(java.util.ArrayList<GXBroadcastFrameCounter> value) {
        broadcastFrames = value;
    }

    /**
     * @return Transmitted frames.
     */
    public final long getTransmissions() {
        return transmissions;
    }

    /**
     * @param value
     *            Transmitted frames.
     */
    public final void setTransmissions(final long value) {
        transmissions = value;
    }

    /**
     * @return Received frames with a correct checksum.
     */
    public final long getReceivedFrames() {
        return receivedFrames;
    }

    /**
     * @param value
     *            Received frames with a correct checksum.
     */
    public final void setReceivedFrames(final long value) {
        receivedFrames = value;
    }

    /**
     * @return Received frames with a incorrect checksum.
     */
    public final long getFailedReceivedFrames() {
        return failedReceivedFrames;
    }

    /**
     * @param value
     *            Received frames with a incorrect checksum.
     */
    public final void setFailedReceivedFrames(final long value) {
        failedReceivedFrames = value;
    }

    /**
     * Last time when ReceivedSignalStrength, LinkStatus, Transmissions,
     * ReceivedFrames or FailedReceivedFrames was changed.
     * {@link ReceivedSignalStrength} {@link LinkStatus} {@link Transmissions}
     * {@link ReceivedFrames} {@link FailedReceivedFrames}
     */
    private GXCaptureTime captureTime;

    /**
     * {@link receivedSignalStrength} {@link linkStatus} {@link transmissions}
     * {@link receivedFrames} {@link failedReceivedFrames}
     * 
     * @return Last time when ReceivedSignalStrength, LinkStatus, Transmissions,
     *         ReceivedFrames or FailedReceivedFrames was changed.
     */
    public final GXCaptureTime getCaptureTime() {
        return captureTime;
    }

    /**
     * {@link receivedSignalStrength} {@link linkStatus} {@link transmissions}
     * {@link receivedFrames} {@link failedReceivedFrames}
     * 
     * @param value
     *            Last time when ReceivedSignalStrength, LinkStatus,
     *            Transmissions, ReceivedFrames or FailedReceivedFrames was
     *            changed.
     */
    public final void setCaptureTime(GXCaptureTime value) {
        captureTime = value;
    }

    /**
     * Reset value.
     * 
     * @param client
     *            DLMS client.
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
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] reset(GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 1, 0, DataType.INT8);
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getReceivedSignalStrength(), getChannelId(), getLinkStatus(),
                getBroadcastFrames(), getTransmissions(), getReceivedFrames(), getFailedReceivedFrames(),
                getCaptureTime() };
    }

    @Override
    public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            receivedSignalStrength = 0;
            setTransmissions(0);
            setReceivedFrames(0);
            setFailedReceivedFrames(0);
            setCaptureTime(null);
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // ReceivedSignalStrength
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // ChannelId
        if (all || !super.isRead(3)) {
            attributes.add(3);
        }
        // LinkStatus
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // BroadcastFrames
        if (all || !super.isRead(5)) {
            attributes.add(5);
        }
        // Transmissions
        if (all || !super.isRead(6)) {
            attributes.add(6);
        }
        // ReceivedFrames
        if (all || !super.isRead(7)) {
            attributes.add(7);
        }
        // FailedReceivedFrames
        if (all || !super.isRead(8)) {
            attributes.add(8);
        }
        // CaptureTime
        if (all || !super.isRead(9)) {
            attributes.add(9);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "Received signal strength ", "Channel Id", "Link status",
                "Broadcast frames", "Transmissions", "Received frames", "Failed received frames", "Capture time" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[] { "Reset" };
    }

    @Override
    public final int getAttributeCount() {
        return 9;
    }

    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.UINT8;
        case 3:
            return DataType.UINT8;
        case 4:
            return DataType.ENUM;
        case 5:
            return DataType.ARRAY;
        case 6:
            return DataType.UINT32;
        case 7:
            return DataType.UINT32;
        case 8:
            return DataType.UINT32;
        case 9:
            return DataType.STRUCTURE;
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
            return getReceivedSignalStrength();
        case 3:
            return getChannelId();
        case 4:
            return linkStatus.getValue();
        case 5: {
            int cnt = getBroadcastFrames().size();
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            // Add count
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0) {
                for (GXBroadcastFrameCounter it : getBroadcastFrames()) {
                    data.setUInt8(DataType.STRUCTURE);
                    data.setUInt8(3); // Count
                    GXCommon.setData(settings, data, DataType.UINT8, it.getClientId());
                    GXCommon.setData(settings, data, DataType.UINT32, it.getCounter());
                    GXCommon.setData(settings, data, DataType.DATETIME, it.getTimeStamp());
                }
            }
            return data.array();
        }
        case 6:
            return getTransmissions();
        case 7:
            return getReceivedFrames();
        case 8:
            return getFailedReceivedFrames();
        case 9: {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE);
            // Add count
            GXCommon.setObjectCount(2, data);
            GXCommon.setData(settings, data, DataType.UINT8, getCaptureTime().getAttributeId());
            GXCommon.setData(settings, data, DataType.DATETIME, getCaptureTime().getTimeStamp());
            return data.array();
        }
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
            setReceivedSignalStrength(((GXUInt8) e.getValue()).shortValue());
            break;
        case 3:
            setChannelId(((GXUInt8) e.getValue()).shortValue());
            break;
        case 4:
            setLinkStatus(MBusLinkStatus.forValue(((GXEnum) e.getValue()).shortValue()));
            break;
        case 5:
            getBroadcastFrames().clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure item = (GXStructure) tmp;
                    // Time stamp should be date-time.
                    GXDateTime timeStamp;
                    if (item.get(2) instanceof GXDateTime) {
                        timeStamp = (GXDateTime) item.get(2);
                    } else if (item.get(2) instanceof byte[]) {
                        byte[] ba = (byte[]) item.get(2);
                        timeStamp = (GXDateTime) GXDLMSClient.changeType(ba, DataType.DATETIME,
                                settings.getUseUtc2NormalTime());
                    } else {
                        timeStamp = null;
                    }
                    GXBroadcastFrameCounter tempVar = new GXBroadcastFrameCounter();
                    tempVar.setClientId(((GXUInt8) item.get(0)).shortValue());
                    tempVar.setCounter(((GXUInt32) item.get(1)).longValue());
                    tempVar.setTimeStamp(timeStamp);
                    getBroadcastFrames().add(tempVar);
                }
            }
            break;
        case 6:
            setTransmissions(((GXUInt32) e.getValue()).longValue());
            break;
        case 7:
            setReceivedFrames(((GXUInt32) e.getValue()).longValue());
            break;
        case 8:
            setFailedReceivedFrames(((GXUInt32) e.getValue()).longValue());
            break;
        case 9:
            if (e.getValue() != null) {
                GXStructure item = (GXStructure) e.getValue();
                getCaptureTime().setAttributeId(((GXUInt8) item.get(0)).shortValue());
                // TimeStamp should be date time.
                if (item.get(1) instanceof GXDateTime) {
                    getCaptureTime().setTimeStamp((GXDateTime) item.get(1));
                } else if (item.get(1) instanceof byte[]) {
                    byte[] ba = (byte[]) item.get(2);
                    getCaptureTime().setTimeStamp((GXDateTime) GXDLMSClient.changeType(ba, DataType.DATETIME,
                            settings.getUseUtc2NormalTime()));
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
        String str;
        setReceivedSignalStrength((short) reader.readElementContentAsInt("ReceivedSignalStrength"));
        setChannelId((short) reader.readElementContentAsInt("ChannelId"));
        setLinkStatus(MBusLinkStatus.forValue(reader.readElementContentAsInt("LinkStatus")));
        getBroadcastFrames().clear();
        if (reader.isStartElement("BroadcastFrames", true)) {
            while (reader.isStartElement("Item", true)) {
                GXBroadcastFrameCounter tempVar = new GXBroadcastFrameCounter();
                tempVar.setClientId((short) reader.readElementContentAsInt("ClientId"));
                tempVar.setCounter((short) reader.readElementContentAsInt("Counter"));
                GXBroadcastFrameCounter item = tempVar;
                str = reader.readElementContentAsString("TimeStamp");
                if (str == null) {
                    item.setTimeStamp(null);
                } else {
                    item.setTimeStamp(new GXDateTime(str));
                }
                getBroadcastFrames().add(item);
            }
            reader.readEndElement("BroadcastFrames");
        }
        setTransmissions((long) reader.readElementContentAsInt("Transmissions"));
        setReceivedFrames((long) reader.readElementContentAsInt("ReceivedFrames"));
        setFailedReceivedFrames((long) reader.readElementContentAsInt("FailedReceivedFrames"));
        if (reader.isStartElement("CaptureTime", true)) {
            getCaptureTime().setAttributeId((short) reader.readElementContentAsInt("AttributeId"));
            str = reader.readElementContentAsString("TimeStamp");
            if (str == null) {
                getCaptureTime().setTimeStamp(null);
            } else {
                getCaptureTime().setTimeStamp(new GXDateTime(str));
            }
            reader.readEndElement("CaptureTime");
        }
    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("ReceivedSignalStrength", getReceivedSignalStrength());
        writer.writeElementString("ChannelId", getChannelId());
        writer.writeElementString("LinkStatus", getLinkStatus().getValue());
        writer.writeStartElement("BroadcastFrames");
        if (getBroadcastFrames() != null) {
            for (GXBroadcastFrameCounter it : getBroadcastFrames()) {
                writer.writeStartElement("Item");
                // Some meters are returning time here, not date-time.
                writer.writeElementString("ClientId", it.getClientId());
                writer.writeElementString("Counter", it.getCounter());
                writer.writeElementString("TimeStamp", it.getTimeStamp());
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
        writer.writeElementString("Transmissions", getTransmissions());
        writer.writeElementString("ReceivedFrames", getReceivedFrames());
        writer.writeElementString("FailedReceivedFrames", getFailedReceivedFrames());
        writer.writeStartElement("CaptureTime");
        writer.writeElementString("AttributeId", getCaptureTime().getAttributeId());
        writer.writeElementString("TimeStamp", getCaptureTime().getTimeStamp());
        writer.writeEndElement();
    }

    @Override
    public final void postLoad(GXXmlReader reader) {
    }
}
