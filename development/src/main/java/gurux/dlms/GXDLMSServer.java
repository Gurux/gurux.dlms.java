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
import java.util.logging.Logger;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode;
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

    private GXReplyData info = new GXReplyData();
    /**
     * Received data.
     */
    private GXByteBuffer receivedData = new GXByteBuffer();

    /**
     * Reply data.
     */
    private GXByteBuffer replyData = new GXByteBuffer();

    /**
     * Long get or read transaction information.
     */
    private GXDLMSLongTransaction transaction;

    /**
     * Server settings.
     */
    private final GXDLMSSettings settings = new GXDLMSSettings(true);

    /**
     * Is server initialized.
     */
    private boolean initialized = false;

    /**
     * @param value
     *            Cipher interface that is used to cipher PDU.
     */
    protected final void setCipher(final GXICipher value) {
        settings.setCipher(value);
    }

    /**
     * @return Client to Server challenge.
     */
    public final byte[] getCtoSChallenge() {
        return settings.getCtoSChallenge();
    }

    /**
     * @return Server to Client challenge.
     */
    public final byte[] getStoCChallenge() {
        return settings.getStoCChallenge();
    }

    /**
     * @return Interface type.
     */
    public final InterfaceType getInterfaceType() {
        return settings.getInterfaceType();
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
    protected abstract boolean isTarget(final int serverAddress,
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
    protected abstract SourceDiagnostic validateAuthentication(
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
    protected abstract GXDLMSObject onFindObject(ObjectType objectType, int sn,
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
    protected abstract void write(ValueEventArgs[] args);

    /**
     * Accepted connection is made for the server. All initialization is done
     * here.
     * 
     * @param connectionInfo
     *            Connection info.
     */
    protected abstract void connected(GXDLMSConnectionEventArgs connectionInfo);

    /**
     * Client has try to made invalid connection. Password is incorrect.
     * 
     * @param connectionInfo
     *            Connection info.
     */
    protected abstract void
            invalidConnection(GXDLMSConnectionEventArgs connectionInfo);

    /**
     * Server has close the connection. All clean up is made here.
     * 
     * @param connectionInfo
     *            Connection info.
     */
    protected abstract void
            disconnected(GXDLMSConnectionEventArgs connectionInfo);

    /**
     * Action is occurred.
     * 
     * @param args
     *            Handled action requests.
     */
    protected abstract void action(ValueEventArgs[] args);

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
        settings.setInterfaceType(type);
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
                 * TODO: Fix unit cases. if (pg.getProfileEntries() < 1) { throw
                 * new RuntimeException("Invalid Profile Entries. " +
                 * "Profile entries tells amount of rows " + " in the table.");
                 * }
                 */
                if (pg.getCapturePeriod() > 0) {
                    new GXProfileGenericUpdater(this, pg).start();
                }
            } else if (it instanceof GXDLMSAssociationShortName
                    && !this.getUseLogicalNameReferencing()) {
                if (((GXDLMSAssociationShortName) it).getObjectList()
                        .size() == 0) {
                    ((GXDLMSAssociationShortName) it).getObjectList()
                            .addAll(getItems());
                }
                associationObject = it;
            } else if (it instanceof GXDLMSAssociationLogicalName
                    && this.getUseLogicalNameReferencing()) {
                if (((GXDLMSAssociationLogicalName) it).getObjectList()
                        .size() == 0) {
                    ((GXDLMSAssociationLogicalName) it).getObjectList()
                            .addAll(getItems());
                }
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
                GXDLMSAssociationLogicalName it =
                        new GXDLMSAssociationLogicalName();
                getItems().add(it);
                it.getObjectList().addAll(getItems());
            } else {
                GXDLMSAssociationShortName it =
                        new GXDLMSAssociationShortName();
                getItems().add(it);
                it.getObjectList().addAll(getItems());
            }
        }
        // Arrange items by Short Name.
        short sn = 0xA0;
        if (!this.getUseLogicalNameReferencing()) {
            int[] offset = new int[1];
            int[] count = new int[1];

            for (GXDLMSObject it : settings.getObjects()) {
                // Generate Short Name if not given.
                if (it.getShortName() == 0) {
                    it.setShortName(sn);
                    // Add method index addresses.
                    GXDLMS.getActionInfo(it.getObjectType(), offset, count);
                    if (count[0] != 0) {
                        sn += offset[0] + (8 * count[0]);
                    } else {
                        // If there are no methods.
                        // Add attribute index addresses.
                        sn += 8 * it.getAttributeCount();
                    }
                }
            }
        }
    }

    /**
     * Parse AARQ request that client send and returns AARE request.
     * 
     * @return Reply to the client.
     */
    private void handleAarqRequest(final GXByteBuffer data,
            final GXDLMSConnectionEventArgs connectionInfo) {
        AssociationResult result = AssociationResult.ACCEPTED;
        settings.setCtoSChallenge(null);
        settings.setStoCChallenge(null);
        SourceDiagnostic diagnostic =
                GXAPDU.parsePDU(settings, settings.getCipher(), data);
        if (diagnostic != SourceDiagnostic.NONE) {
            result = AssociationResult.PERMANENT_REJECTED;
            diagnostic = SourceDiagnostic.NOT_SUPPORTED;
            invalidConnection(connectionInfo);
        } else {
            diagnostic = validateAuthentication(settings.getAuthentication(),
                    settings.getPassword());
            if (diagnostic != SourceDiagnostic.NONE) {
                result = AssociationResult.PERMANENT_REJECTED;
            } else if (settings.getAuthentication()
                    .getValue() > Authentication.LOW.getValue()) {
                // If High authentication is used.
                settings.setStoCChallenge(GXSecure
                        .generateChallenge(settings.getAuthentication()));
                result = AssociationResult.ACCEPTED;
                diagnostic = SourceDiagnostic.AUTHENTICATION_REQUIRED;
            } else {
                settings.setConnected(true);
            }
        }

        // Generate AARE packet.
        GXAPDU.generateAARE(settings, replyData, result, diagnostic,
                settings.getCipher());
    }

    /**
     * Parse SNRM Request. If server do not accept client empty byte array is
     * returned.
     * 
     * @return Returns returned UA packet.
     */
    private void handleSnrmRequest() {
        replyData.setUInt8(0x81); // FromatID
        replyData.setUInt8(0x80); // GroupID
        replyData.setUInt8(0); // Length
        replyData.setUInt8(HDLCInfo.MAX_INFO_TX);
        replyData.setUInt8(GXCommon.getSize(getLimits().getMaxInfoTX()));
        replyData.add(getLimits().getMaxInfoTX());
        replyData.setUInt8(HDLCInfo.MAX_INFO_RX);
        replyData.setUInt8(GXCommon.getSize(getLimits().getMaxInfoRX()));
        replyData.add(getLimits().getMaxInfoRX());
        replyData.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
        replyData.setUInt8(GXCommon.getSize(getLimits().getWindowSizeTX()));
        replyData.add(getLimits().getWindowSizeTX());
        replyData.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
        replyData.setUInt8(GXCommon.getSize(getLimits().getWindowSizeRX()));
        replyData.add(getLimits().getWindowSizeRX());
        int len = replyData.size() - 3;
        replyData.setUInt8(2, len); // Length
    }

    /**
     * Generates disconnect request.
     * 
     * @return Disconnect request.
     */
    private void generateDisconnectRequest() {
        if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            replyData.setUInt8(0x63);
            replyData.setUInt8(0x0);
        } else {
            replyData.setUInt8(0x81); // FromatID
            replyData.setUInt8(0x80); // GroupID
            replyData.setUInt8(0); // Length

            replyData.setUInt8(HDLCInfo.MAX_INFO_TX);
            replyData.setUInt8(GXCommon.getSize(getLimits().getMaxInfoTX()));
            replyData.add(getLimits().getMaxInfoTX());

            replyData.setUInt8(HDLCInfo.MAX_INFO_RX);
            replyData.setUInt8(GXCommon.getSize(getLimits().getMaxInfoRX()));
            replyData.add(getLimits().getMaxInfoRX());

            replyData.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
            replyData.setUInt8(GXCommon.getSize(getLimits().getWindowSizeTX()));
            replyData.add(getLimits().getWindowSizeTX());

            replyData.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
            replyData.setUInt8(GXCommon.getSize(getLimits().getWindowSizeRX()));
            replyData.add(getLimits().getWindowSizeRX());

            int len = replyData.size() - 3;
            replyData.setUInt8(2, len); // Length.
        }
    }

    /**
     * Reset after connection is closed.
     */
    public final void reset() {
        transaction = null;
        settings.setCount(0);
        settings.setIndex(0);
        info.clear();
        settings.setConnected(false);
        receivedData.clear();
        replyData.clear();
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
        return handleRequest(buff, new GXDLMSConnectionEventArgs());
    }

    /**
     * Handles client request.
     * 
     * @param buff
     *            Received data from the client.
     * @param connectionInfo
     *            Connection info.
     * @return Response to the request. Response is null if request packet is
     *         not complete.
     */
    public final byte[] handleRequest(final byte[] buff,
            final GXDLMSConnectionEventArgs connectionInfo) {

        if (buff == null || buff.length == 0) {
            return null;
        }
        if (!initialized) {
            throw new RuntimeException("Server not Initialized.");
        }
        try {
            receivedData.set(buff);
            boolean first = settings.getServerAddress() == 0
                    && settings.getClientAddress() == 0;
            GXDLMS.getData(settings, receivedData, info);
            // If all data is not received yet.
            if (!info.isComplete()) {
                return null;
            }
            receivedData.clear();

            if (first) {
                // Check is data send to this server.
                if (!isTarget(settings.getServerAddress(),
                        settings.getClientAddress())) {
                    info.clear();
                    return null;
                }
            }

            // If client want next frame.
            if ((info.getMoreData().getValue()
                    & RequestTypes.FRAME.getValue()) == RequestTypes.FRAME
                            .getValue()) {
                return GXDLMS.getHdlcFrame(settings,
                        settings.getReceiverReady(), replyData);
            }
            // Update command if transaction and next frame is asked.
            if (info.getCommand() == Command.NONE) {
                if (transaction != null) {
                    info.setCommand(transaction.getCommand());
                }
            }
            byte[] reply = handleCommand(info.getCommand(), info.getData(),
                    connectionInfo);
            info.clear();
            return reply;
        } catch (

        Exception e) {
            LOGGER.severe(e.toString());
            if (info.getCommand() != Command.NONE) {
                return reportError(info.getCommand(), ErrorCode.HARDWARE_FAULT);
            } else {
                reset();
                if (settings.getConnected()) {
                    settings.setConnected(false);
                    disconnected(connectionInfo);
                }
                return null;
            }
        }
    }

    private byte[] reportError(final Command command, final ErrorCode error) {
        Command cmd;
        switch (command) {
        case READ_REQUEST:
            cmd = Command.READ_RESPONSE;
            break;
        case WRITE_REQUEST:
            cmd = Command.WRITE_RESPONSE;
            break;
        case GET_REQUEST:
            cmd = Command.GET_RESPONSE;
            break;
        case SET_REQUEST:
            cmd = Command.SET_RESPONSE;
            break;
        case METHOD_REQUEST:
            cmd = Command.METHOD_RESPONSE;
            break;
        default:
            // Return HW error and close connection.
            cmd = Command.NONE;
            break;
        }
        if (settings.getUseLogicalNameReferencing()) {
            GXDLMS.getLNPdu(settings, cmd, 1, null, replyData, error.getValue(),
                    false, true, null);
        } else {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(error.getValue());
            GXDLMS.getSNPdu(settings, cmd, bb, replyData);
        }
        if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            return GXDLMS.getWrapperFrame(settings, replyData);
        } else {
            return GXDLMS.getHdlcFrame(settings, (byte) 0, replyData);
        }
    }

    /**
     * Handle received command.
     * 
     * @param cmd
     *            Executed command.
     * @param data
     *            Received data from the client.
     * @param connectionInfo
     *            Connection info.
     * @return Response for the client.
     */
    private byte[] handleCommand(final Command cmd, final GXByteBuffer data,
            final GXDLMSConnectionEventArgs connectionInfo) {
        byte frame = 0;
        switch (cmd) {
        case SET_REQUEST:
            handleSetRequest(data);
            break;
        case WRITE_REQUEST:
            handleWriteRequest(data);
            break;
        case GET_REQUEST:
            if (data.size() != 0) {
                handleGetRequest(data);
            }
            break;
        case READ_REQUEST:
            handleReadRequest(data);
            break;
        case METHOD_REQUEST:
            handleMethodRequest(data, connectionInfo);
            break;
        case SNRM:
            handleSnrmRequest();
            frame = (byte) Command.UA.getValue();
            break;
        case AARQ:
            handleAarqRequest(data, connectionInfo);
            connected(connectionInfo);
            break;
        case DISCONNECT_REQUEST:
        case DISC:
            generateDisconnectRequest();
            settings.setConnected(false);
            disconnected(connectionInfo);
            frame = (byte) Command.UA.getValue();
            break;
        case NONE:
            // Client wants to get next block.
            break;
        default:
            LOGGER.severe("Invalid command: " + cmd.toString());
        }
        if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            return GXDLMS.getWrapperFrame(settings, replyData);
        } else {
            return GXDLMS.getHdlcFrame(settings, frame, replyData);
        }
    }

    /**
     * Handle action request.
     * 
     * @param reply
     *            Received data from the client.
     * @return Reply.
     */
    private void handleMethodRequest(final GXByteBuffer data,
            final GXDLMSConnectionEventArgs connectionInfo) {
        ErrorCode error = ErrorCode.OK;
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
            GXDataInfo i = new GXDataInfo();
            parameters = GXCommon.getData(data, i);
        }
        GXDLMSObject obj = settings.getObjects().findByLN(ci,
                GXDLMSObject.toLogicalName(ln));
        if (obj == null) {
            obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
        }
        if (obj == null) {
            // Device reports a undefined object.
            error = ErrorCode.UNDEFINED_OBJECT;
        } else {
            ValueEventArgs e = new ValueEventArgs(obj, id, 0, parameters);
            if (obj.getMethodAccess(id) == MethodAccessMode.NO_ACCESS) {
                error = ErrorCode.READ_WRITE_DENIED;
            } else {
                action(new ValueEventArgs[] { e });
                byte[] actionReply;
                if (e.getHandled()) {
                    actionReply = (byte[]) e.getValue();
                } else {
                    actionReply = obj.invoke(settings, e);
                }
                // Set default action reply if not given.
                if (actionReply != null && e.getError() == ErrorCode.OK) {
                    // Add return parameters
                    bb.setUInt8(1);
                    // Add parameters error code.
                    bb.setUInt8(0);
                    GXCommon.setData(bb, GXCommon.getValueType(actionReply),
                            actionReply);
                } else {
                    // Add parameters error code.
                    error = e.getError();
                    // Add return parameters
                    bb.setUInt8(0);
                }
            }
        }
        GXDLMS.getLNPdu(settings, Command.METHOD_RESPONSE, 1, bb, replyData,
                error.getValue(), false, true, null);
        // If High level authentication fails.
        if (!settings.getConnected()
                && obj instanceof GXDLMSAssociationLogicalName && id == 1) {
            invalidConnection(connectionInfo);
        }
    }

    /**
     * Handle set request.
     * 
     * @return Reply to the client.
     */
    private void handleSetRequest(final GXByteBuffer data) {
        GXDataInfo reply = new GXDataInfo();
        GXByteBuffer bb = new GXByteBuffer();
        // Get type.
        short type = data.getUInt8();
        // Get invoke ID and priority.
        data.getUInt8();
        ErrorCode status = ErrorCode.OK;
        // SetRequest normal
        if (type == 1) {
            settings.resetBlockIndex();
            // CI
            ObjectType ci = ObjectType.forValue(data.getUInt16());
            byte[] ln = new byte[6];
            data.get(ln);
            // Attribute index.
            int index = data.getUInt8();
            // Get Access Selection.
            data.getUInt8();
            Object value = GXCommon.getData(data, reply);
            GXDLMSObject obj = settings.getObjects().findByLN(ci,
                    GXDLMSObject.toLogicalName(ln));
            if (obj == null) {
                obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
            }
            // If target is unknown.
            if (obj == null) {
                // Device reports a undefined object.
                status = ErrorCode.UNAVAILABLE_OBJECT;
            } else {
                AccessMode am = obj.getAccess(index);
                // If write is denied.
                if (am != AccessMode.WRITE && am != AccessMode.READ_WRITE) {
                    // Read Write denied.
                    status = ErrorCode.READ_WRITE_DENIED;
                } else {
                    try {
                        if (value instanceof byte[]) {
                            DataType dt = (obj).getDataType(index);
                            if (dt != DataType.NONE) {
                                value = GXDLMSClient.changeType((byte[]) value,
                                        dt);
                            }
                        }
                        ValueEventArgs e =
                                new ValueEventArgs(obj, index, 0, null);
                        e.setValue(value);
                        write(new ValueEventArgs[] { e });
                        if (!e.getHandled()) {
                            obj.setValue(settings, e);
                        }
                    } catch (Exception e) {
                        status = ErrorCode.HARDWARE_FAULT;
                    }
                }
            }
        } else {
            LOGGER.info("HandleSetRequest failed. Unknown command.");
            settings.resetBlockIndex();
            status = ErrorCode.HARDWARE_FAULT;
        }
        GXDLMS.getLNPdu(settings, Command.SET_RESPONSE, 1, bb, replyData,
                status.getValue(), false, true, null);
    }

    private void handleGetRequest(final GXByteBuffer data) {
        ErrorCode error = ErrorCode.OK;
        ValueEventArgs e = null;
        GXByteBuffer bb = new GXByteBuffer();
        int index = 0;
        // Get type.
        int type = data.getUInt8();
        // Get invoke ID and priority.
        data.getUInt8();
        // GetRequest normal
        if (type == 1) {
            settings.setCount(0);
            settings.setIndex(0);
            settings.resetBlockIndex();
            // CI
            ObjectType ci = ObjectType.forValue(data.getUInt16());
            byte[] ln = new byte[6];
            data.get(ln);
            // Attribute Id
            byte attributeIndex = (byte) data.getUInt8();
            GXDLMSObject obj = settings.getObjects().findByLN(ci,
                    GXDLMSObject.toLogicalName(ln));
            if (obj == null) {
                obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
            }
            if (obj == null) {
                // "Access Error : Device reports a undefined object."
                error = ErrorCode.UNDEFINED_OBJECT;
            } else {
                if (obj.getAccess(attributeIndex) == AccessMode.NO_ACCESS) {
                    error = ErrorCode.READ_WRITE_DENIED;
                } else {
                    // AccessSelection
                    int selection = data.getUInt8();
                    int selector = 0;
                    Object parameters = null;
                    if (selection != 0) {
                        selector = data.getUInt8();
                        GXDataInfo i = new GXDataInfo();
                        parameters = GXCommon.getData(data, i);
                    }

                    e = new ValueEventArgs(obj, attributeIndex, selector,
                            parameters);
                    read(new ValueEventArgs[] { e });
                    Object value;
                    if (e.getHandled()) {
                        value = e.getValue();
                    } else {
                        value = obj.getValue(settings, e);
                    }
                    GXDLMS.appendData(obj, attributeIndex, bb, value);
                    error = e.getError();
                }
            }
            if (settings.getCount() != settings.getIndex()
                    || GXDLMS.multipleBlocks(settings, bb)) {
                GXDLMS.getLNPdu(settings, Command.GET_RESPONSE, 2, bb,
                        replyData, error.getValue(), true, false, null);
                transaction = new GXDLMSLongTransaction(
                        new ValueEventArgs[] { e }, Command.GET_REQUEST, bb);
            } else {
                GXDLMS.getLNPdu(settings, Command.GET_RESPONSE, 1, bb,
                        replyData, error.getValue(), false, false, null);
            }
        } else if (type == 2) {
            // Get request for next data block
            // Get block index.
            index = (int) data.getUInt32();
            if (index != settings.getBlockIndex()) {
                LOGGER.info("handleGetRequest failed. Invalid block number. "
                        + settings.getBlockIndex() + "/" + index);
                GXDLMS.getLNPdu(settings, Command.GET_RESPONSE, 2, bb,
                        replyData,
                        ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue(), true,
                        true, null);
            } else {
                settings.increaseBlockIndex();
                // If transaction is not in progress.
                if (transaction == null) {
                    GXDLMS.getLNPdu(settings, Command.GET_RESPONSE, 2, bb,
                            replyData, ErrorCode.NO_LONG_GET_OR_READ_IN_PROGRESS
                                    .getValue(),
                            true, true, null);
                } else {
                    bb.set(transaction.getData());
                    // replyData.Clear();
                    boolean moreData = false;
                    if (settings.getIndex() != settings.getCount()) {
                        // If there is multiple blocks on the buffer.
                        // This might happen when Max PDU size is very small.
                        if (GXDLMS.multipleBlocks(settings, bb)) {
                            moreData = true;
                        } else {
                            for (ValueEventArgs arg : transaction
                                    .getTargets()) {
                                Object value;
                                if (arg.getHandled()) {
                                    value = arg.getValue();
                                } else {
                                    value = arg.getTarget().getValue(settings,
                                            arg);
                                }
                                // Add data.
                                GXDLMS.appendData(arg.getTarget(),
                                        arg.getIndex(), bb, value);
                                moreData = settings.getIndex() != settings
                                        .getCount();
                            }
                        }
                    } else {
                        moreData = GXDLMS.multipleBlocks(settings, bb);
                    }
                    GXDLMS.getLNPdu(settings, Command.GET_RESPONSE, 2, bb,
                            replyData, ErrorCode.OK.getValue(), true, !moreData,
                            null);
                    if (moreData || bb.size() != 0) {
                        transaction.setData(bb);
                    } else {
                        transaction = null;
                    }
                }
            }
        } else if (type == 3) {
            // Get request with a list.
            int pos;
            int cnt = GXCommon.getObjectCount(data);
            GXCommon.setObjectCount(cnt, bb);
            List<ValueEventArgs> list = new ArrayList<ValueEventArgs>();
            for (pos = 0; pos != cnt; ++pos) {
                ObjectType ci = ObjectType.forValue(data.getUInt16());
                byte[] ln = new byte[6];
                data.get(ln);
                short attributeIndex = data.getUInt8();
                GXDLMSObject obj = settings.getObjects().findByLN(ci,
                        GXDLMSObject.toLogicalName(ln));
                if (obj == null) {
                    obj = onFindObject(ci, 0, GXDLMSObject.toLogicalName(ln));
                }
                if (obj == null) {
                    // "Access Error : Device reports a undefined object."
                    e = new ValueEventArgs(obj, attributeIndex, 0, 0);
                    e.setError(ErrorCode.UNDEFINED_OBJECT);
                    list.add(e);
                } else {
                    if (obj.getAccess(attributeIndex) == AccessMode.NO_ACCESS) {
                        e = new ValueEventArgs(obj, attributeIndex, 0, 0);
                        e.setError(ErrorCode.READ_WRITE_DENIED);
                        list.add(e);
                    } else {
                        // AccessSelection
                        int selection = data.getUInt8();
                        int selector = 0;
                        Object parameters = null;
                        if (selection != 0) {
                            selector = data.getUInt8();
                            GXDataInfo i = new GXDataInfo();
                            parameters = GXCommon.getData(data, i);
                        }
                        ValueEventArgs arg = new ValueEventArgs(obj,
                                attributeIndex, selector, parameters);
                        list.add(arg);
                    }
                }
            }
            read(list.toArray(new ValueEventArgs[list.size()]));
            Object value;
            pos = 0;
            for (ValueEventArgs it : list) {
                try {
                    if (it.getHandled()) {
                        value = it.getValue();
                    } else {
                        value = it.getTarget().getValue(settings, it);
                    }
                    bb.setUInt8(it.getError().getValue());
                    GXDLMS.appendData(it.getTarget(), it.getIndex(), bb, value);
                } catch (Exception e1) {
                    bb.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
                }
                if (settings.getIndex() != settings.getCount()) {
                    transaction = new GXDLMSLongTransaction(
                            list.toArray(new ValueEventArgs[list.size()]),
                            Command.GET_REQUEST, null);
                }
                pos++;
            }
            GXDLMS.getLNPdu(settings, Command.GET_RESPONSE, 3, bb, replyData,
                    0xFF, GXDLMS.multipleBlocks(settings, bb), true, null);
        } else {
            LOGGER.info("HandleGetRequest failed. Invalid command type.");
            settings.resetBlockIndex();
            // Access Error : Device reports a hardware fault.
            bb.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
            GXDLMS.getLNPdu(settings, Command.GET_RESPONSE, type, bb, replyData,
                    ErrorCode.OK.getValue(), false, false, null);
        }
    }

    /**
     * Find Short Name object.
     * 
     * @param sn
     */
    private GXSNInfo findSNObject(final int sn) {
        GXSNInfo i = new GXSNInfo();
        int[] offset = new int[1], count = new int[1];
        for (GXDLMSObject it : getItems()) {
            if (sn >= it.getShortName()) {
                // If attribute is accessed.
                if (sn < it.getShortName() + it.getAttributeCount() * 8) {
                    i.setAction(false);
                    i.setItem(it);
                    i.setIndex(((sn - it.getShortName()) / 8) + 1);
                    break;
                } else {
                    // If method is accessed.
                    GXDLMS.getActionInfo(it.getObjectType(), offset, count);
                    if (sn < it.getShortName() + offset[0] + (8 * count[0])) {
                        i.setItem(it);
                        i.setAction(true);
                        i.setIndex(
                                (sn - it.getShortName() - offset[0]) / 8 + 1);
                        break;
                    }
                }
            }
        }
        if (i.getItem() == null) {
            i.setItem(onFindObject(ObjectType.NONE, sn, null));
        }
        return i;
    }

    /**
     * Handle read request.
     */
    private void handleReadRequest(final GXByteBuffer data) {
        int type;
        Object value = null;
        List<GXSimpleEntry<ValueEventArgs, Boolean>> list =
                new ArrayList<GXSimpleEntry<ValueEventArgs, Boolean>>();
        GXByteBuffer bb = new GXByteBuffer();
        // If get next frame.
        if (data.size() == 0) {
            bb.set(replyData);
            replyData.clear();
            for (ValueEventArgs it : transaction.getTargets()) {
                list.add(new GXSimpleEntry<ValueEventArgs, Boolean>(it,
                        it.isAction()));
            }
        } else {
            transaction = null;
            int cnt = GXCommon.getObjectCount(data);
            GXCommon.setObjectCount(cnt, bb);
            GXSNInfo i;
            List<ValueEventArgs> reads = new ArrayList<ValueEventArgs>();
            List<ValueEventArgs> actions = new ArrayList<ValueEventArgs>();
            for (int pos = 0; pos != cnt; ++pos) {
                type = data.getUInt8();
                if (type == 2) {
                    // GetRequest normal
                    int sn = data.getUInt16();
                    i = findSNObject(sn);
                    ValueEventArgs e = new ValueEventArgs(i.getItem(),
                            i.getIndex(), 0, null);
                    e.setAction(i.isAction());
                    list.add(new GXSimpleEntry<ValueEventArgs, Boolean>(e,
                            e.isAction()));
                    if ((!e.isAction() && i.getItem()
                            .getAccess(i.getIndex()) == AccessMode.NO_ACCESS)
                            || (e.isAction() && i.getItem().getMethodAccess(i
                                    .getIndex()) == MethodAccessMode.NO_ACCESS)) {
                        e.setError(ErrorCode.READ_WRITE_DENIED);
                    } else {
                        if (e.isAction()) {
                            actions.add(e);
                        } else {
                            reads.add(e);
                        }
                    }
                } else if (type == 4) {
                    // Parameterised access.
                    int sn = data.getUInt16();
                    int selector = data.getUInt8();
                    GXDataInfo di = new GXDataInfo();
                    Object parameters = GXCommon.getData(data, di);
                    i = findSNObject(sn);
                    ValueEventArgs e = new ValueEventArgs(i.getItem(),
                            i.getIndex(), selector, parameters);
                    e.setAction(i.isAction());
                    list.add(new GXSimpleEntry<ValueEventArgs, Boolean>(e,
                            e.isAction()));
                    if ((!e.isAction() && i.getItem()
                            .getAccess(i.getIndex()) == AccessMode.NO_ACCESS)
                            || (e.isAction() && i.getItem().getMethodAccess(i
                                    .getIndex()) == MethodAccessMode.NO_ACCESS)) {
                        e.setError(ErrorCode.READ_WRITE_DENIED);
                    } else {
                        if (e.isAction()) {
                            actions.add(e);
                        } else {
                            reads.add(e);
                        }
                    }
                }
            }
            if (reads.size() != 0) {
                read(reads.toArray(new ValueEventArgs[reads.size()]));
            }
            if (actions.size() != 0) {
                action(actions.toArray(new ValueEventArgs[actions.size()]));
            }
        }
        for (GXSimpleEntry<ValueEventArgs, Boolean> e1 : list) {
            if (e1.getKey().getHandled()) {
                value = e1.getKey().getValue();
            } else {
                // If action.
                if (e1.getValue()) {
                    value = e1.getKey().getTarget().invoke(settings,
                            e1.getKey());
                } else {
                    value = e1.getKey().getTarget().getValue(settings,
                            e1.getKey());
                }
            }
            if (e1.getKey().getError() == ErrorCode.OK) {
                // Set status.
                if (transaction == null) {
                    bb.setUInt8(e1.getKey().getError().getValue());
                }
                // If action.
                if (e1.getValue()) {
                    GXCommon.setData(bb, GXCommon.getValueType(value), value);
                } else {
                    GXDLMS.appendData(e1.getKey().getTarget(),
                            e1.getKey().getIndex(), bb, value);
                }
            } else {
                bb.setUInt8(1);
                bb.setUInt8(e1.getKey().getError().getValue());
            }
            if (transaction == null
                    && settings.getCount() != settings.getIndex()) {
                List<ValueEventArgs> reads = new ArrayList<ValueEventArgs>();
                for (GXSimpleEntry<ValueEventArgs, Boolean> it : list) {
                    reads.add(it.getKey());
                }
                transaction = new GXDLMSLongTransaction(
                        reads.toArray(new ValueEventArgs[reads.size()]),
                        Command.READ_REQUEST, null);
            } else if (transaction != null) {
                replyData.set(bb);
                return;
            }
        }
        GXDLMS.getSNPdu(settings, Command.READ_RESPONSE, bb, replyData);
    }

    /**
     * Handle write request.
     * 
     * @param reply
     *            Received data from the client.
     * @return Reply.
     */
    private void handleWriteRequest(final GXByteBuffer data) {
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
                GXSNInfo i = findSNObject(sn);
                targets.add(i);
                // If target is unknown.
                if (i == null) {
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
        GXDataInfo di = new GXDataInfo();
        for (int pos = 0; pos != cnt; ++pos) {
            if (results.getUInt8(pos) == 0) {
                // If object has found.
                GXSNInfo target = targets.get(pos);
                value = GXCommon.getData(data, di);
                if (value instanceof byte[]) {
                    DataType dt =
                            target.getItem().getDataType(target.getIndex());
                    if (dt != DataType.NONE && dt != DataType.OCTET_STRING) {
                        value = GXDLMSClient.changeType((byte[]) value, dt);
                    }
                }
                di.clear();
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
                        target.getItem().setValue(settings, e);
                    }
                }
            }
        }
        GXByteBuffer bb = new GXByteBuffer((2 * cnt + 2));
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
        GXDLMS.getSNPdu(settings, Command.WRITE_RESPONSE, bb, replyData);
    }
}