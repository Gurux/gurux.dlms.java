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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXProfileGenericUpdater;
import gurux.dlms.objects.IGXDLMSBase;
import gurux.dlms.secure.GXSecure;

/**
 * GXDLMSServer implements methods to implement DLMS/COSEM meter/proxy.
 */
public abstract class GXDLMSServer {
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSServer.class.getName());
    private GXServerReply serverReply = new GXServerReply();

    /**
     * Received and parsed data from the client.
     */
    private GXReplyData reply = new GXReplyData();

    /**
     * Server settings.
     */
    private final GXDLMSSettings settings = new GXDLMSSettings(true);
    /**
     * Frames to send.
     */
    private byte[][] frames = null;
    /**
     * Frame index.
     */
    private int frameIndex = 0;
    /**
     * Is server initialized.
     */
    private boolean initialized = false;
    /**
     * Objects sorted by SN.
     */
    private TreeMap<Integer, GXDLMSObject> sortedItems =
            new TreeMap<Integer, GXDLMSObject>();

    /**
     * Connected server address.
     */
    private int connectedServerAddress = 0;
    /**
     * Connected client address.
     */
    private int connectedClientAddress = 0;

    /**
     * @param value
     *            Cipher interface that is used to cipher PDU.
     */
    protected final void setCipher(final GXICipher value) {
        settings.setCipher(value);
    }

    /*
     * Client to Server challenge. Reserved internal use. Do not use.
     */
    public final byte[] getCtoSChallenge() {
        return settings.getCtoSChallenge();
    }

    /*
     * Server to Client challenge. Reserved internal use. Do not use.
     */
    public final byte[] getStoCChallenge() {
        return settings.getStoCChallenge();
    }

    /**
     * Server to Client custom challenge. This is for debugging purposes. Reset
     * custom challenge settings StoCChallenge to null.
     * 
     * @param value
     *            Server to Client challenge.
     */
    public final void setStoCChallenge(final byte[] value) {
        settings.setUseCustomChallenge(value != null);
        settings.setStoCChallenge(value);
    }

    /**
     * Set starting packet index. Default is One based, but some meters use Zero
     * based value. Usually this is not used.
     * 
     * @param value
     *            Zero based starting index.
     */
    public final void setStartingPacketIndex(final int value) {
        settings.setBlockIndex(value);
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
     * @return Used priority.
     */
    public final Priority getPriority() {
        return settings.getPriority();
    }

    /**
     * @param value
     *            Used priority.
     */
    public final void setPriority(final Priority value) {
        settings.setPriority(value);
    }

    /**
     * Check is data sent to this server.
     * 
     * @param serverAddress
     *            Server address.
     * @param clientAddress
     *            Client address.
     * @return True, if data is sent to this server.
     */
    public abstract boolean isTarget(final int serverAddress,
            final int clientAddress);

    /**
     * Check whether the authentication and password are correct.
     * 
     * @param authentication
     *            Authentication level.
     * @param password
     *            Password.
     * @return Source diagnostic.
     */
    public abstract SourceDiagnostic validateAuthentication(
            final Authentication authentication, final byte[] password);

    /**
     * Find object.
     * 
     * @param objectType
     *            Object type.
     * @param sn
     *            Short Name. In Logical name referencing this is not used.
     * @param ln
     *            Logical Name. In Short Name referencing this is not used.
     * @return Found object or null if object is not found.
     */
    public abstract GXDLMSObject onFindObject(ObjectType objectType, int sn,
            String ln);

    /**
     * Read selected item(s).
     * 
     * @param args
     *            Handled read requests.
     */
    public abstract void read(ValueEventArgs[] args);

    /**
     * Write selected item(s).
     * 
     * @param args
     *            Handled write requests.
     */
    public abstract void write(ValueEventArgs[] args);

    /**
     * Accepted connection is made for the server. All initialization is done
     * here.
     */
    public abstract void connected();

    /**
     * Server has close the connection. All clean up is made here.
     */
    public abstract void disconnected();

    /**
     * Client attempts to connect with the wrong server or client address.
     * 
     * @param e
     *            Connection parameters.
     */
    public abstract void invalidConnection(ConnectionEventArgs e);

    /**
     * Action is occurred.
     * 
     * @param args
     *            Handled action requests.
     */
    public abstract void action(ValueEventArgs[] args);

    /**
     * Constructor.
     * 
     * @param logicalNameReferencing
     *            Is logical name referencing used.
     * @param type
     *            Interface type.
     */
    public GXDLMSServer(final boolean logicalNameReferencing,
            final InterfaceType type) {
        settings.setUseLogicalNameReferencing(logicalNameReferencing);
        this.setInterfaceType(type);
        reset();
    }

    /**
     * @return List of objects that meter supports.
     */
    public final GXDLMSObjectCollection getItems() {
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
     * @see #getMaxReceivePDUSize
     * @return Is logical name referencing used.
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
     * @return Get settings.
     */
    protected final GXDLMSSettings getSettings() {
        return settings;
    }

    /**
     * @return Interface type.
     */
    public final InterfaceType getInterfaceType() {
        return settings.getInterfaceType();
    }

    /**
     * @param value
     *            Interface type.
     */
    private void setInterfaceType(final InterfaceType value) {
        settings.setInterfaceType(value);
    }

    /**
     * Gets Logical Name settings.
     * 
     * @return Logical Name settings.
     */
    public final GXDLMSLNSettings getLNSettings() {
        return settings.getLnSettings();
    }

    /**
     * Gets Short Name settings.
     * 
     * @return Short Name settings.
     */
    public final GXDLMSSNSettings getSNSettings() {
        return settings.getSnSettings();
    }

    /**
     * Initialize server. This must call after server objects are set.
     */
    public final void initialize() {
        GXDLMSObject associationObject = null;
        initialized = true;
        if (sortedItems.size() != getItems().size()) {
            for (int pos = 0; pos != settings.getObjects().size(); ++pos) {
                GXDLMSObject it = settings.getObjects().get(pos);
                if (it.getLogicalName() == null) {
                    throw new RuntimeException("Invalid Logical Name.");
                }
                if (it instanceof GXDLMSProfileGeneric) {
                    GXDLMSProfileGeneric pg = null;
                    if (it instanceof GXDLMSProfileGeneric) {
                        pg = (GXDLMSProfileGeneric) it;
                    }
                    /*
                     * TODO: Fix unit cases. if (pg.getProfileEntries() < 1) {
                     * throw new RuntimeException("Invalid Profile Entries. " +
                     * "Profile entries tells amount of rows " +
                     * " in the table."); }
                     */
                    if (pg.getCapturePeriod() > 0) {
                        new GXProfileGenericUpdater(this, pg).start();
                    }
                } else if ((it instanceof GXDLMSAssociationShortName
                        && !this.getUseLogicalNameReferencing())
                        || (it instanceof GXDLMSAssociationLogicalName
                                && this.getUseLogicalNameReferencing())) {
                    associationObject = it;
                } else if (!(it instanceof IGXDLMSBase)) {
                    // Remove unsupported items.
                    LOGGER.info(it.getLogicalName() + " not supported.");
                    settings.getObjects().remove(pos);
                    --pos;
                }
            }
            if (associationObject == null) {
                if (getUseLogicalNameReferencing()) {
                    GXDLMSAssociationLogicalName ln =
                            new GXDLMSAssociationLogicalName();
                    for (GXDLMSObject it : settings.getObjects()) {
                        ln.getObjectList().add(it);
                    }
                    settings.getObjects().add(ln);
                } else {
                    GXDLMSAssociationShortName sn =
                            new GXDLMSAssociationShortName();
                    for (GXDLMSObject it : settings.getObjects()) {
                        sn.getObjectList().add(it);
                    }
                    settings.getObjects().add(sn);
                }
            } else {
                // If association objects list is not set yet update it.
                GXDLMSObjectCollection objects;
                if (getUseLogicalNameReferencing()) {
                    objects = ((GXDLMSAssociationLogicalName) associationObject)
                            .getObjectList();
                } else {
                    objects = ((GXDLMSAssociationShortName) associationObject)
                            .getObjectList();
                }
                if (objects.size() == 0) {
                    objects.addAll(getItems());
                }
            }
            // Arrange items by Short Name.
            short sn = 0xA0;
            if (!this.getUseLogicalNameReferencing()) {
                sortedItems.clear();
                for (GXDLMSObject it : settings.getObjects()) {
                    // Generate Short Name if not given.
                    if (it.getShortName() == 0) {
                        do {
                            it.setShortName(sn);
                            sn += 0xA0;
                        } while (sortedItems
                                .containsKey(it.getShortName() & 0xFFFF));
                    }
                    sortedItems.put(it.getShortName() & 0xFFFF, it);
                }
            }
        }
    }

    /**
     * Parse AARQ request that client send and returns AARE request.
     * 
     * @return Reply to the client.
     */
    private byte[][] handleAarqRequest() {
        AssociationResult result = AssociationResult.ACCEPTED;
        settings.setCtoSChallenge(null);
        settings.setStoCChallenge(null);
        SourceDiagnostic diagnostic = GXAPDU.parsePDU(settings,
                settings.getCipher(), reply.getData());
        if (diagnostic != SourceDiagnostic.NONE) {
            result = AssociationResult.PERMANENT_REJECTED;
            diagnostic = SourceDiagnostic.NOT_SUPPORTED;
        } else {
            if (settings.getAuthentication() == Authentication.LOW) {
                diagnostic = validateAuthentication(
                        settings.getAuthentication(), settings.getPassword());
            }
            if (diagnostic != SourceDiagnostic.NONE) {
                result = AssociationResult.PERMANENT_REJECTED;
            } else if (settings.getAuthentication()
                    .getValue() > Authentication.LOW.getValue()) {
                // If High authentication is used.
                settings.setStoCChallenge(GXSecure
                        .generateChallenge(settings.getAuthentication()));
                result = AssociationResult.ACCEPTED;
                diagnostic = SourceDiagnostic.AUTHENTICATION_REQUIRED;
            }
        }

        // Generate AARE packet.
        GXByteBuffer buff = new GXByteBuffer(150);
        GXAPDU.generateAARE(settings, buff, result, diagnostic,
                settings.getCipher());
        settings.resetFrameSequence();
        return GXDLMS.splitPdu(settings, Command.AARE, 0, buff, null).get(0);
    }

    /**
     * Parse SNRM Request. If server do not accept client empty byte array is
     * returned.
     * 
     * @return Returns returned UA packet.
     */
    private byte[][] handleSnrmRequest() {
        GXByteBuffer bb = new GXByteBuffer(25);
        bb.setUInt8(0x81); // FromatID
        bb.setUInt8(0x80); // GroupID
        bb.setUInt8(0); // Length
        bb.setUInt8(HDLCInfo.MAX_INFO_TX);
        bb.setUInt8(GXCommon.getSize(getLimits().getMaxInfoTX()));
        bb.add(getLimits().getMaxInfoTX());
        bb.setUInt8(HDLCInfo.MAX_INFO_RX);
        bb.setUInt8(GXCommon.getSize(getLimits().getMaxInfoRX()));
        bb.add(getLimits().getMaxInfoRX());
        bb.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
        bb.setUInt8(GXCommon.getSize(getLimits().getWindowSizeTX()));
        bb.add(getLimits().getWindowSizeTX());
        bb.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
        bb.setUInt8(GXCommon.getSize(getLimits().getWindowSizeRX()));
        bb.add(getLimits().getWindowSizeRX());
        int len = bb.size() - 3;
        bb.setUInt8(2, len); // Length
        return GXDLMS.splitToHdlcFrames(settings, Command.UA.getValue(), bb);
    }

    /**
     * Generates disconnect request.
     * 
     * @return Disconnect request.
     */
    private byte[][] generateDisconnectRequest() {
        GXByteBuffer buff;
        if (this.getInterfaceType() == InterfaceType.WRAPPER) {
            buff = new GXByteBuffer(2);
            buff.setUInt8(0x63);
            buff.setUInt8(0x0);
            return GXDLMS.splitToWrapperFrames(settings, buff);
        } else {
            buff = new GXByteBuffer(22);
            buff.setUInt8(0x81); // FromatID
            buff.setUInt8(0x80); // GroupID
            buff.setUInt8(0); // Length

            buff.setUInt8(HDLCInfo.MAX_INFO_TX);
            buff.setUInt8(GXCommon.getSize(getLimits().getMaxInfoTX()));
            buff.add(getLimits().getMaxInfoTX());

            buff.setUInt8(HDLCInfo.MAX_INFO_RX);
            buff.setUInt8(GXCommon.getSize(getLimits().getMaxInfoRX()));
            buff.add(getLimits().getMaxInfoRX());

            buff.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
            buff.setUInt8(GXCommon.getSize(getLimits().getWindowSizeTX()));
            buff.add(getLimits().getWindowSizeTX());

            buff.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
            buff.setUInt8(GXCommon.getSize(getLimits().getWindowSizeRX()));
            buff.add(getLimits().getWindowSizeRX());

            int len = buff.position() - 3;
            buff.setUInt8(2, len); // Length.
        }
        return GXDLMS.splitToHdlcFrames(settings, Command.UA.getValue(), buff);
    }

    /**
     * Reset after connection is closed.
     */
    public final void reset() {
        settings.setConnected(false);
        connectedServerAddress = 0;
        reply.clear();
        settings.setServerAddress(0);
        settings.setClientAddress(0);
        settings.setAuthentication(Authentication.NONE);
        if (settings.getCipher() != null) {
            settings.getCipher().reset();
        }
    }

    /**
     * Handles client request.
     * 
     * @param buff
     *            Received data from the client.
     * @return Response to the request. Response is null if request packet is
     *         not complete.
     */
    public final byte[] handleRequest(final byte[] buff) {
        if (buff == null || buff.length == 0) {
            return null;
        }
        if (!initialized) {
            throw new RuntimeException("Server not Initialized.");
        }
        try {
            byte[] data = getPacket(buff);
            // If all data is not received yet or message is not accepted.
            if (!reply.isComplete()) {
                return null;
            }
            if (data != null) {
                return data;
            }
            handleCommand();
            if (!reply.isMoreData()) {
                reply.clear();
            }
            data = frames[frameIndex++];
            LOGGER.info(GXCommon.toHex(data));
            return data;
        } catch (Exception e) {
            // Disconnect.
            LOGGER.severe(e.toString());
            reset();
            return GXDLMS.splitToHdlcFrames(settings,
                    FrameType.REJECTED.getValue(), null)[0];
        }
    }

    /**
     * Get packet from received data.
     * 
     * @param buff
     *            Received data.
     * @return Reply if any.
     */
    private byte[] getPacket(final byte[] buff) {
        LOGGER.info(GXCommon.toHex(buff));
        final GXByteBuffer receivedFrame = new GXByteBuffer(buff);
        // Save position where data was. This is used if data is not sent to
        // this server.
        int offset = reply.getData().size();
        GXDLMS.getData(settings, receivedFrame, reply);
        // If all data is not received yet.
        if (!reply.isComplete()) {
            return null;
        }
        // If client sends keepalive or get next frame request.
        if ((reply.getMoreData().getValue()
                & RequestTypes.FRAME.getValue()) != 0) {
            if (frames != null && frames.length > frameIndex) {
                byte[] data = frames[frameIndex++];
                LOGGER.info(GXCommon.toHex(data));
                return data;
            }
            frameIndex = 0;
            byte[] data = GXDLMS.receiverReady(settings, RequestTypes.FRAME);
            LOGGER.info(GXCommon.toHex(data));
            return data;
        }
        // Check is data sent to this server.
        if (connectedServerAddress == 0 || connectedClientAddress == 0) {
            // Check is data send to this server.
            if (!isTarget(settings.getServerAddress(),
                    settings.getClientAddress())) {
                reply.getData().size(offset);
                reply.setComplete(false);
            }
        } else if (settings.getServerAddress() != connectedServerAddress
                || settings.getClientAddress() != connectedClientAddress) {
            reply.getData().size(offset);
            reply.setComplete(false);
        }

        // Clear received data.
        receivedFrame.clear();
        serverReply.setData(reply.getData());
        frameIndex = 0;
        return null;
    }

    /**
     * Handle received command.
     */
    private void handleCommand() {
        switch (reply.getCommand()) {
        case SET_REQUEST:
            frames = handleSetRequest();
            break;
        case WRITE_REQUEST:
            frames = handleWriteRequest();
            break;
        case GET_REQUEST:
            frames = handleGetRequest();
            break;
        case READ_REQUEST:
            frames = handleReadRequest();
            break;
        case METHOD_REQUEST:
            frames = handleMethodRequest();
            break;
        case SNRM:
            frames = handleSnrmRequest();
            break;
        case AARQ:
            frames = handleAarqRequest();
            settings.setConnected(true);
            connected();
            break;
        case DISCONNECT_REQUEST:
            frames = generateDisconnectRequest();
            settings.setConnected(false);
            disconnected();
            break;
        default:
            LOGGER.severe("Invalid command: " + reply.getCommand().toString());
            frames = GXDLMS.splitToHdlcFrames(settings,
                    FrameType.REJECTED.getValue(), null);
        }
    }

    /**
     * Handle action request.
     * 
     * @param reply
     *            Received data from the client.
     * @return Reply.
     */
    private byte[][] handleMethodRequest() {
        GXByteBuffer data = reply.getData();
        GXByteBuffer bb = new GXByteBuffer();
        // Get type.
        data.getUInt8();
        // Get invoke ID and priority.
        data.getUInt8();
        // CI
        ObjectType ci = ObjectType.forValue(data.getUInt16());
        byte[] ln = new byte[6];
        data.get(ln);
        // Attribute Id
        int id = data.getUInt8();
        // Get parameters.
        Object parameters = null;
        if (data.getUInt8() != 0) {
            GXDataInfo info = new GXDataInfo();
            parameters = GXCommon.getData(data, info);
        }
        GXDLMSObject obj = settings.getObjects().findByLN(ci,
                GXDLMSObject.toLogicalName(ln));
        // If object is not default object.
        if (obj == null) {
            obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
        }

        if (obj == null) {
            // Device reports a undefined object.
            addError(ErrorCode.UNDEFINED_OBJECT, bb);
            LOGGER.severe("Undefined object.");
        } else {
            ValueEventArgs e = new ValueEventArgs(obj, id, 0, parameters);
            action(new ValueEventArgs[] { e });
            byte[] actionReply;
            if (e.getHandled()) {
                actionReply = (byte[]) e.getValue();
            } else {
                actionReply = obj.invoke(settings, id, parameters);
            }
            // Set default action reply if not given.
            if (actionReply == null) {
                actionReply = new byte[] { (byte) ErrorCode.OK.getValue() };
            }
            bb.add(actionReply);
        }
        return GXDLMS.splitPdu(settings, Command.METHOD_RESPONSE, 1, bb, null)
                .get(0);
    }

    /**
     * Server reports error.
     * 
     * @param error
     *            Error code.
     * @param bb
     *            Byte buffer where error info is saved.
     */
    private static void addError(final ErrorCode error, final GXByteBuffer bb) {
        if (error == ErrorCode.OK) {
            bb.setUInt8(0);
        } else {
            bb.setUInt8(1);
            bb.setUInt8(error.getValue());
        }
    }

    /**
     * Handle set request.
     * 
     * @return Reply to the client.
     */
    private byte[][] handleSetRequest() {
        GXByteBuffer data = reply.getData();
        GXDataInfo info = new GXDataInfo();
        GXByteBuffer bb = new GXByteBuffer();
        // Get type.
        short type = data.getUInt8();
        // Get invoke ID and priority.
        data.getUInt8();
        // GetRequest normal
        if (type == 1) {
            settings.resetBlockIndex();
            serverReply.setIndex(0);
            // CI
            ObjectType ci = ObjectType.forValue(data.getUInt16());
            byte[] ln = new byte[6];
            data.get(ln);
            // Attribute index.
            int index = data.getUInt8();
            // Get Access Selection.
            data.getUInt8();
            Object value = GXCommon.getData(data, info);
            GXDLMSObject obj = settings.getObjects().findByLN(ci,
                    GXDLMSObject.toLogicalName(ln));
            // If object is not default object.
            if (obj == null) {
                obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
            }

            // If target is unknown.
            if (obj == null) {
                LOGGER.severe("Undefined object.");
                // Device reports a undefined object.
                bb.setUInt8(ErrorCode.UNDEFINED_OBJECT.getValue());
            } else {
                AccessMode am = obj.getAccess(index);
                // If write is denied.
                if (am != AccessMode.WRITE && am != AccessMode.READ_WRITE) {
                    LOGGER.severe("Read Write denied.");
                    bb.setUInt8(ErrorCode.READ_WRITE_DENIED.getValue());
                } else {
                    try {
                        ValueEventArgs e =
                                new ValueEventArgs(obj, index, 0, null);
                        e.setValue(value);
                        write(new ValueEventArgs[] { e });
                        if (!e.getHandled()) {
                            obj.setValue(settings, index, value);
                        }
                    } catch (Exception e) {
                        LOGGER.severe(e.getMessage());
                        bb.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
                    }
                }
            }
        } else {
            LOGGER.severe("handleSetRequest failed. Unknown command.");
            settings.resetBlockIndex();
            bb.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
        }
        return GXDLMS.splitPdu(settings, Command.SET_RESPONSE, 1, bb, null)
                .get(0);
    }

    private byte[][] handleGetRequest() {
        GXByteBuffer data = reply.getData();
        GXByteBuffer bb = new GXByteBuffer();
        short type;
        int index = 0;
        Object parameters = null;
        // Get type.
        type = data.getUInt8();
        // Get invoke ID and priority.
        data.getUInt8();
        // GetRequest normal
        if (type == 1) {
            settings.resetBlockIndex();
            serverReply.setIndex(0);
            parameters = null;
            // CI
            ObjectType ci = ObjectType.forValue(data.getUInt16());
            byte[] ln = new byte[6];
            data.get(ln);
            // Attribute Id
            int attributeIndex = data.getUInt8();
            GXDLMSObject obj = settings.getObjects().findByLN(ci,
                    GXDLMSObject.toLogicalName(ln));

            // If object is not default object.
            if (obj == null) {
                obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
            }
            if (obj == null) {
                // "Access Error : Device reports a undefined object."
                bb.setUInt8(ErrorCode.UNDEFINED_OBJECT.getValue());
            } else {
                // AccessSelection
                int selection = data.getUInt8();
                int selector = 0;
                if (selection != 0) {
                    selector = data.getUInt8();
                    GXDataInfo info = new GXDataInfo();
                    parameters = GXCommon.getData(data, info);
                }
                ValueEventArgs e = new ValueEventArgs(obj, attributeIndex,
                        selector, parameters);
                read(new ValueEventArgs[] { e });
                Object value;
                if (!e.getHandled()) {
                    value = obj.getValue(settings, attributeIndex, selector,
                            parameters);
                } else {
                    value = e.getValue();
                }
                GXDLMS.appedData(obj, attributeIndex, bb, value);
                // Add status if not multiple blocks.
                boolean multibleBlocks =
                        bb.size() > settings.getMaxReceivePDUSize();
                if (!multibleBlocks) {
                    GXByteBuffer tmp = new GXByteBuffer();
                    tmp.setUInt8(ErrorCode.OK.getValue());
                    tmp.set(bb.array());
                    bb = tmp;
                }
            }
            serverReply.setReplyMessages(GXDLMS.splitPdu(settings,
                    Command.GET_RESPONSE, 1, bb, null));

        } else if (type == 2) {
            // Get request for next data block
            // Get block index.
            index = (int) data.getUInt32();
            if (index != settings.getBlockIndex()) {
                LOGGER.severe("handleGetRequest failed. Invalid block number. "
                        + settings.getBlockIndex() + "/" + index);
                bb.setUInt8(ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue());

                serverReply.setReplyMessages(GXDLMS.splitPdu(settings,
                        Command.GET_RESPONSE, 1, bb, null));
                index = 0;
                serverReply.setIndex(index);
            } else {
                settings.increaseBlockIndex();
                index = serverReply.getIndex() + 1;
                serverReply.setIndex(index);
            }
        } else if (type == 3) {
            // Get request with a list.
            int cnt = GXCommon.getObjectCount(data);
            GXCommon.setObjectCount(cnt, bb);
            List<ValueEventArgs> list = new ArrayList<ValueEventArgs>();
            for (int pos = 0; pos != cnt; ++pos) {
                ObjectType ci = ObjectType.forValue(data.getUInt16());
                byte[] ln = new byte[6];
                data.get(ln);
                short attributeIndex = data.getUInt8();

                GXDLMSObject obj = settings.getObjects().findByLN(ci,
                        GXDLMSObject.toLogicalName(ln));
                // If object is not default object.
                if (obj == null) {
                    obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
                }
                if (obj == null) {
                    // "Access Error : Device reports a undefined object."
                    bb.setUInt8(1);
                    bb.setUInt8(ErrorCode.UNDEFINED_OBJECT.getValue());
                } else {
                    // AccessSelection
                    int selection = data.getUInt8();
                    int selector = 0;
                    if (selection != 0) {
                        selector = data.getUInt8();
                        GXDataInfo info = new GXDataInfo();
                        parameters = GXCommon.getData(data, info);
                    }
                    ValueEventArgs e = new ValueEventArgs(obj, attributeIndex,
                            selector, parameters);
                    list.add(e);
                }
            }
            try {
                read(list.toArray(new ValueEventArgs[list.size()]));
                Object value;
                for (ValueEventArgs e : list) {
                    if (!e.getHandled()) {
                        value = e.getTarget().getValue(settings, e.getIndex(),
                                e.getSelector(), e.getParameters());
                    } else {
                        value = e.getValue();
                    }
                    bb.setUInt8(ErrorCode.OK.getValue());
                    GXDLMS.appedData(e.getTarget(), e.getIndex(), bb, value);
                }
            } catch (Exception e) {
                bb.setUInt8(1);
                bb.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
            }
            serverReply.setReplyMessages(GXDLMS.splitPdu(settings,
                    Command.GET_RESPONSE, 3, bb, null));
        } else {
            LOGGER.severe("handleGetRequest failed. Invalid command type.");
            settings.resetBlockIndex();
            // Access Error : Device reports a hardware fault.
            bb.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
            serverReply.setReplyMessages(GXDLMS.splitPdu(settings,
                    Command.GET_RESPONSE, 1, bb, null));
        }
        serverReply.setIndex(index);
        return serverReply.getReplyMessages().get((int) index);
    }

    /**
     * Find Short Name object.
     * 
     * @param sn
     */
    private GXSNInfo findSNObject(final int sn) {
        GXSNInfo info = new GXSNInfo();
        for (Map.Entry<Integer, GXDLMSObject> it : sortedItems.entrySet()) {
            int aCnt = ((IGXDLMSBase) it.getValue()).getAttributeCount();
            if (sn >= it.getKey().intValue()
                    && sn <= (it.getKey().intValue() + (8 * aCnt))) {
                info.setAction(false);
                info.setItem(it.getValue());
                info.setIndex(((sn - info.getItem().getShortName()) / 8) + 1);
                LOGGER.info(String.format("Reading %d, attribute index %d",
                        info.getItem().getName(),
                        new Integer(info.getIndex())));
                break;
            } else if (sn >= it.getKey().intValue() + aCnt
                    && ((IGXDLMSBase) it.getValue()).getMethodCount() != 0) {
                // Check if action.

                // Convert DLMS data to object type.
                int[] value2 = new int[1], count = new int[1];
                GXDLMS.getActionInfo(it.getValue().getObjectType(), value2,
                        count);
                if (sn <= it.getKey().intValue() + value2[0] + (8 * count[0])) {
                    info.setItem(it.getValue());
                    info.setAction(true);
                    info.setIndex(
                            (sn - it.getValue().getShortName() - value2[0]) / 8
                                    + 1);
                    break;
                }
            }
        }
        // If object is not default object.
        if (info.getItem() == null) {
            info.setItem(onFindObject(ObjectType.NONE, sn, null));
        }
        if (info.getItem() == null) {
            throw new IllegalArgumentException("Invalid SN Command.");
        }
        return info;
    }

    /**
     * Handle read request.
     * 
     * @return Reply to the client.
     */
    private byte[][] handleReadRequest() {
        GXByteBuffer data = reply.getData();
        short type;
        Object value = null;
        GXByteBuffer bb = new GXByteBuffer();
        int cnt = GXCommon.getObjectCount(data);
        GXCommon.setObjectCount(cnt, bb);
        GXSNInfo info;
        for (int pos = 0; pos != cnt; ++pos) {
            type = data.getUInt8();
            // GetRequest normal
            if (type == 2) {
                int sn = data.getUInt16();
                info = findSNObject(sn);
                if (!info.isAction()) {
                    ValueEventArgs e = new ValueEventArgs(info.getItem(),
                            info.getIndex(), 0, null);
                    read(new ValueEventArgs[] { e });
                    if (e.getHandled()) {
                        value = e.getValue();
                    } else {
                        value = info.getItem().getValue(settings,
                                info.getIndex(), 0, null);
                    }
                    // Set status.
                    bb.setUInt8(0);
                    GXDLMS.appedData(info.getItem(), info.getIndex(), bb,
                            value);
                } else {
                    ValueEventArgs e = new ValueEventArgs(info.getItem(),
                            info.getIndex(), 0, null);
                    action(new ValueEventArgs[] { e });
                    if (e.getHandled()) {
                        value = e.getValue();
                    } else {
                        value = ((IGXDLMSBase) info.getItem()).invoke(settings,
                                info.getIndex(), null);
                    }
                    // Set status.
                    bb.setUInt8(0);
                    // Add value
                    bb.setUInt8(GXCommon.getValueType(value).getValue());
                    bb.add(value);
                }
            } else if (type == 2) {
                // Get request for next data block
                throw new IllegalArgumentException("TODO: Invalid Command.");
            } else if (type == 4) {
                // Parameterised access.
                int sn = data.getUInt16();
                int selector = data.getUInt8();
                GXDataInfo di = new GXDataInfo();
                Object parameters = GXCommon.getData(data, di);
                info = findSNObject(sn);
                if (!info.isAction()) {
                    ValueEventArgs e = new ValueEventArgs(info.getItem(),
                            info.getIndex(), 0, null);
                    read(new ValueEventArgs[] { e });
                    if (e.getHandled()) {
                        value = e.getValue();
                    } else {
                        value = info.getItem().getValue(settings,
                                info.getIndex(), selector, parameters);
                    }
                    // Set status.
                    bb.setUInt8(0);
                    GXDLMS.appedData(info.getItem(), info.getIndex(), bb,
                            value);
                } else {
                    ValueEventArgs e = new ValueEventArgs(info.getItem(),
                            info.getIndex(), 0, null);
                    e.setValue(parameters);
                    action(new ValueEventArgs[] { e });
                    if (e.getHandled()) {
                        value = e.getValue();
                    } else {
                        value = ((IGXDLMSBase) info.getItem()).invoke(settings,
                                info.getIndex(), parameters);
                    }
                    // Set status.
                    bb.setUInt8(0);
                    // Add value
                    bb.add(value);
                }
            } else {
                bb.setUInt8(ErrorCode.READ_WRITE_DENIED.getValue());
            }
        }
        return GXDLMS.splitPdu(settings, Command.READ_RESPONSE, 1, bb, null)
                .get(0);
    }

    /**
     * Handle write request.
     * 
     * @param reply
     *            Received data from the client.
     * @return Reply.
     */
    private byte[][] handleWriteRequest() {
        GXByteBuffer data = reply.getData();
        short type;
        Object value;
        // Get object count.
        List<GXSNInfo> targets = new ArrayList<GXSNInfo>();
        int cnt = GXCommon.getObjectCount(data);
        GXByteBuffer results = new GXByteBuffer(cnt);
        for (int pos = 0; pos != cnt; ++pos) {
            type = data.getUInt8();
            if (type == 2) {
                int sn = data.getUInt16();
                GXSNInfo info = findSNObject(sn);
                targets.add(info);
                // If target is unknown.
                if (info == null) {
                    // Device reports a undefined object.
                    results.setUInt8(ErrorCode.UNDEFINED_OBJECT.getValue());
                } else {
                    results.setUInt8(ErrorCode.OK.getValue());
                }
            } else {
                // Device reports a HW error.
                results.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
            }
        }
        // Get data count.
        cnt = GXCommon.getObjectCount(data);
        GXDataInfo info = new GXDataInfo();
        for (int pos = 0; pos != cnt; ++pos) {
            if (results.getUInt8(pos) == 0) {
                // If object has found.
                GXSNInfo target = targets.get(pos);
                value = GXCommon.getData(data, info);
                info.clear();
                AccessMode am = target.getItem().getAccess(target.getIndex());
                // If write is denied.
                if (am != AccessMode.WRITE && am != AccessMode.READ_WRITE) {
                    results.setUInt8(pos,
                            ErrorCode.READ_WRITE_DENIED.getValue());
                } else {
                    ValueEventArgs e = new ValueEventArgs(target.getItem(),
                            target.getIndex(), 0, null);
                    e.setValue(value);
                    write(new ValueEventArgs[] { e });
                    if (!e.getHandled()) {
                        target.getItem().setValue(settings, target.getIndex(),
                                value);
                    }
                }
            }
        }
        GXByteBuffer bb = new GXByteBuffer(2 * cnt + 2);
        GXCommon.setObjectCount(cnt, bb);
        int ret;
        for (int pos = 0; pos != cnt; ++pos) {
            ret = results.getUInt8(pos);
            // If meter returns error.
            if (ret != 0) {
                bb.setUInt8(1);
            }
            bb.setUInt8(ret);
        }
        return GXDLMS.splitPdu(settings, Command.WRITE_RESPONSE, 1, bb, null)
                .get(0);
    }
}