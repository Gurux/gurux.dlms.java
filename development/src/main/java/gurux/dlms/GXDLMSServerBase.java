package gurux.dlms;

import java.util.logging.Level;
import java.util.logging.Logger;

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
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXProfileGenericUpdater;
import gurux.dlms.objects.IGXDLMSBase;
import gurux.dlms.secure.GXSecure;

public class GXDLMSServerBase {
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSServer.class.getName());

    private final Object owner;
    private GXReplyData info = new GXReplyData();
    /*
     * Received data.
     */
    private GXByteBuffer receivedData = new GXByteBuffer();

    /*
     * Reply data.
     */
    private GXByteBuffer replyData = new GXByteBuffer();

    /*
     * Long get or read transaction information.
     */
    private GXDLMSLongTransaction transaction;

    /*
     * Server settings.
     */
    private final GXDLMSSettings settings = new GXDLMSSettings(true);

    /*
     * Is server initialized.
     */
    private boolean initialized = false;

    /*
     * @param value Cipher interface that is used to cipher PDU.
     */
    protected final void setCipher(final GXICipher value) {
        settings.setCipher(value);
    }

    /*
     * This is reserved for internal use only.
     * @param value Transaction.
     */
    final void setTransaction(final GXDLMSLongTransaction value) {
        transaction = value;
    }

    /*
     * This is reserved for internal use only.
     * @return Transaction.
     */
    final GXDLMSLongTransaction getTransaction() {
        return transaction;
    }

    /*
     * @return Client to Server challenge.
     */
    public final byte[] getCtoSChallenge() {
        return settings.getCtoSChallenge();
    }

    /*
     * @return Server to Client challenge.
     */
    public final byte[] getStoCChallenge() {
        return settings.getStoCChallenge();
    }

    /*
     * @return Interface type.
     */
    public final InterfaceType getInterfaceType() {
        return settings.getInterfaceType();
    }

    /*
     * Server to Client custom challenge. This is for debugging purposes. Reset
     * custom challenge settings StoCChallenge to null.
     * @param value Server to Client challenge.
     */
    public final void setStoCChallenge(final byte[] value) {
        settings.setUseCustomChallenge(value != null);
        settings.setStoCChallenge(value);
    }

    /*
     * Set starting packet index. Default is One based, but some meters use Zero
     * based value. Usually this is not used.
     * @param value Zero based starting index.
     */
    public final void setStartingPacketIndex(final int value) {
        settings.setBlockIndex(value);
    }

    /*
     * @return Invoke ID.
     */
    public final int getInvokeID() {
        return settings.getInvokeID();
    }

    /*
     * @param value Invoke ID.
     */
    public final void setInvokeID(final int value) {
        settings.setInvokeID(value);
    }

    /*
     * @return Used service class.
     */
    public final ServiceClass getServiceClass() {
        return settings.getServiceClass();
    }

    /*
     * @param value Used service class.
     */
    public final void setServiceClass(final ServiceClass value) {
        settings.setServiceClass(value);
    }

    /*
     * @return Used priority.
     */
    public final Priority getPriority() {
        return settings.getPriority();
    }

    /*
     * @param value Used priority.
     */
    public final void setPriority(final Priority value) {
        settings.setPriority(value);
    }

    /*
     * Constructor.
     * @param forOwner Owner.
     * @param logicalNameReferencing Is logical name referencing used.
     * @param type Interface type.
     */
    public GXDLMSServerBase(final Object forOwner,
            final boolean logicalNameReferencing, final InterfaceType type) {
        owner = forOwner;
        settings.setUseLogicalNameReferencing(logicalNameReferencing);
        settings.setInterfaceType(type);
        reset();
    }

    /*
     * @return List of objects that meter supports.
     */
    public final GXDLMSObjectCollection getItems() {
        return settings.getObjects();
    }

    /*
     * @return Information from the connection size that server can handle.
     */
    public final GXDLMSLimits getLimits() {
        return settings.getLimits();
    }

    /*
     * Retrieves the maximum size of received PDU. PDU size tells maximum size
     * of PDU packet. Value can be from 0 to 0xFFFF. By default the value is
     * 0xFFFF.
     * @return Maximum size of received PDU.
     */
    public final int getMaxReceivePDUSize() {
        return settings.getMaxServerPDUSize();
    }

    /*
     * @param value Maximum size of received PDU.
     */
    public final void setMaxReceivePDUSize(final int value) {
        settings.setMaxServerPDUSize(value);
    }

    /*
     * Determines, whether Logical, or Short name, referencing is used.
     * Referencing depends on the device to communicate with. Normally, a device
     * supports only either Logical or Short name referencing. The referencing
     * is defined by the device manufacturer. If the referencing is wrong, the
     * SNMR message will fail.
     * @see #getMaxReceivePDUSize
     * @return Is logical name referencing used.
     */
    public final boolean getUseLogicalNameReferencing() {
        return settings.getUseLogicalNameReferencing();
    }

    /*
     * @param value Is Logical Name referencing used.
     */
    public final void setUseLogicalNameReferencing(final boolean value) {
        settings.setUseLogicalNameReferencing(value);
    }

    /*
     * @return Get settings.
     */
    public final GXDLMSSettings getSettings() {
        return settings;
    }

    /*
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
                LOGGER.log(Level.INFO, it.getLogicalName() + " not supported.");
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

    /*
     * Parse AARQ request that client send and returns AARE request.
     * @return Reply to the client.
     */
    private void handleAarqRequest(final GXByteBuffer data,
            final GXDLMSConnectionEventArgs connectionInfo) {
        AssociationResult result = AssociationResult.ACCEPTED;
        // Reset settings for wrapper.
        if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            reset(true);
        }
        SourceDiagnostic diagnostic =
                GXAPDU.parsePDU(settings, settings.getCipher(), data, null);

        if (diagnostic != SourceDiagnostic.NONE) {
            result = AssociationResult.PERMANENT_REJECTED;
            diagnostic = SourceDiagnostic.NOT_SUPPORTED;
            notifyInvalidConnection(connectionInfo);
        } else {
            if (owner instanceof GXDLMSServer) {
                GXDLMSServer b = (GXDLMSServer) owner;
                diagnostic = b.validateAuthentication(
                        settings.getAuthentication(), settings.getPassword());
            } else {
                GXDLMSServer2 b = (GXDLMSServer2) owner;
                diagnostic = b.onValidateAuthentication(
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
            } else {
                settings.setConnected(true);
            }
        }
        if (settings.getInterfaceType() == InterfaceType.HDLC) {
            replyData.set(GXCommon.LLC_REPLY_BYTES);
        }
        // Generate AARE packet.
        GXAPDU.generateAARE(settings, replyData, result, diagnostic,
                settings.getCipher(), null);
    }

    /*
     * Parse SNRM Request. If server do not accept client empty byte array is
     * returned.
     * @return Returns returned UA packet.
     */
    private void handleSnrmRequest() {
        reset(true);
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

    /*
     * Generates disconnect request.
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

    /*
     * Reset after connection is closed.
     * @param connect Is new connection.
     */
    final void reset(final boolean connect) {
        if (!connect) {
            info.clear();
            settings.setServerAddress(0);
            settings.setClientAddress(0);
        }
        settings.setCtoSChallenge(null);
        settings.setStoCChallenge(null);
        receivedData.clear();
        transaction = null;
        settings.setCount(0);
        settings.setIndex(0);
        settings.setConnected(false);
        replyData.clear();
        settings.setAuthentication(Authentication.NONE);
        if (settings.getCipher() != null) {
            settings.getCipher().reset();
        }
    }

    /*
     * Reset after connection is closed.
     */
    public final void reset() {
        reset(false);
    }

    /*
     * Handles client request.
     * @param buff Received data from the client.
     * @return Response to the request. Response is null if request packet is
     * not complete.
     */
    public final byte[] handleRequest(final byte[] buff) {
        return handleRequest(buff, new GXDLMSConnectionEventArgs());
    }

    /*
     * Handles client request.
     * @param buff Received data from the client.
     * @param connectionInfo Connection info.
     * @return Response to the request. Response is null if request packet is
     * not complete.
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
                if (owner instanceof GXDLMSServer) {
                    GXDLMSServer b = (GXDLMSServer) owner;
                    // Check is data send to this server.
                    if (!b.isTarget(settings.getServerAddress(),
                            settings.getClientAddress())) {
                        info.clear();
                        return null;
                    }
                } else {
                    GXDLMSServer2 b = (GXDLMSServer2) owner;
                    // Check is data send to this server.
                    if (!b.isTarget(settings.getServerAddress(),
                            settings.getClientAddress())) {
                        info.clear();
                        return null;
                    }
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
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString());
            if (info.getCommand() != Command.NONE) {
                return reportError(info.getCommand(), ErrorCode.HARDWARE_FAULT);
            } else {
                reset();
                if (settings.isConnected()) {
                    settings.setConnected(false);
                    if (owner instanceof GXDLMSServer) {
                        GXDLMSServer b = (GXDLMSServer) owner;
                        b.disconnected(connectionInfo);

                    } else {
                        GXDLMSServer2 b = (GXDLMSServer2) owner;
                        b.onDisconnected(connectionInfo);
                    }
                }
                return null;
            }
        }
    }

    private byte[] reportError(final int command, final ErrorCode error) {
        short cmd;
        switch (command) {
        case Command.READ_REQUEST:
            cmd = Command.READ_RESPONSE;
            break;
        case Command.WRITE_REQUEST:
            cmd = Command.WRITE_RESPONSE;
            break;
        case Command.GET_REQUEST:
            cmd = Command.GET_RESPONSE;
            break;
        case Command.SET_REQUEST:
            cmd = Command.SET_RESPONSE;
            break;
        case Command.METHOD_REQUEST:
            cmd = Command.METHOD_RESPONSE;
            break;
        default:
            // Return HW error and close connection.
            cmd = Command.NONE;
            break;
        }
        if (settings.getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, cmd, 1,
                    null, null, error.getValue());
            GXDLMS.getLNPdu(p, replyData);
        } else {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(error.getValue());
            GXDLMSSNParameters p =
                    new GXDLMSSNParameters(settings, cmd, 1, 1, null, bb);
            GXDLMS.getSNPdu(p, replyData);
        }
        if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            return GXDLMS.getWrapperFrame(settings, replyData);
        } else {
            return GXDLMS.getHdlcFrame(settings, (byte) 0, replyData);
        }
    }

    /*
     * Handle received command.
     * @param cmd Executed command.
     * @param data Received data from the client.
     * @param connectionInfo Connection info.
     * @return Response for the client.
     */
    private byte[] handleCommand(final int cmd, final GXByteBuffer data,
            final GXDLMSConnectionEventArgs connectionInfo) {
        byte frame = 0;
        switch (cmd) {
        case Command.ACCESS_REQUEST:
            GXDLMSLNCommandHandler.handleAccessRequest(settings, this, data,
                    replyData, null);
            break;
        case Command.SET_REQUEST:
            GXDLMSLNCommandHandler.handleSetRequest(settings, this, data,
                    replyData, null);
            break;
        case Command.WRITE_REQUEST:
            GXDLMSSNCommandHandler.handleWriteRequest(settings, this, data,
                    replyData, null);
            break;
        case Command.GET_REQUEST:
            if (data.size() != 0) {
                GXDLMSLNCommandHandler.handleGetRequest(settings, this, data,
                        replyData, null);
            }
            break;
        case Command.READ_REQUEST:
            GXDLMSSNCommandHandler.handleReadRequest(settings, this, data,
                    replyData, null);
            break;
        case Command.METHOD_REQUEST:
            GXDLMSLNCommandHandler.handleMethodRequest(settings, this, data,
                    connectionInfo, replyData, null);
            break;
        case Command.SNRM:
            handleSnrmRequest();
            frame = (byte) Command.UA;
            break;
        case Command.AARQ:
            handleAarqRequest(data, connectionInfo);
            if (owner instanceof GXDLMSServer) {
                ((GXDLMSServer) owner).connected(connectionInfo);
            } else {
                ((GXDLMSServer2) owner).onConnected(connectionInfo);
            }
            break;
        case Command.RELEASE_REQUEST:
        case Command.DISCONNECT_REQUEST:
            generateDisconnectRequest();
            settings.setConnected(false);
            if (owner instanceof GXDLMSServer) {
                ((GXDLMSServer) owner).disconnected(connectionInfo);
            } else {
                ((GXDLMSServer2) owner).onDisconnected(connectionInfo);
            }
            frame = Command.UA;
            break;
        case Command.NONE:
            // Client wants to get next block.
            break;
        default:
            LOGGER.log(Level.SEVERE, "Invalid command: " + cmd);
        }
        byte[] reply;
        if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            reply = GXDLMS.getWrapperFrame(settings, replyData);
        } else {
            reply = GXDLMS.getHdlcFrame(settings, frame, replyData);
        }
        return reply;
    }

    /*
     * Generate confirmed service error.
     * @param service Confirmed service error.
     * @param type Service error.
     * @param code code
     * @return
     */
    static byte[] generateConfirmedServiceError(
            final ConfirmedServiceError service, final ServiceError type,
            final int code) {
        return new byte[] { (byte) Command.CONFIRMED_SERVICE_ERROR,
                (byte) service.getValue(), (byte) type.getValue(),
                (byte) code };
    }

    final GXDLMSObject notifyFindObject(final ObjectType objectType,
            final int sn, final String ln) {
        if (owner instanceof GXDLMSServer) {
            return ((GXDLMSServer) owner).onFindObject(objectType, sn, ln);
        }
        return ((GXDLMSServer2) owner).onFindObject(objectType, sn, ln);
    }

    /*
     * Read selected item(s).
     * @param args Handled read requests.
     */
    final void notifyRead(final ValueEventArgs[] args) {
        if (owner instanceof GXDLMSServer) {
            ((GXDLMSServer) owner).read(args);
        } else {
            ((GXDLMSServer2) owner).onPreRead(args);
        }
    }

    /*
     * Write selected item(s).
     * @param args Handled write requests.
     */
    final void notifyWrite(final ValueEventArgs[] args) {
        if (owner instanceof GXDLMSServer) {
            ((GXDLMSServer) owner).write(args);
        } else {
            ((GXDLMSServer2) owner).onPreWrite(args);
        }
    }

    /*
     * Action is occurred.
     * @param args Handled action requests.
     */
    final void notifyAction(final ValueEventArgs[] args) {
        if (owner instanceof GXDLMSServer) {
            ((GXDLMSServer) owner).action(args);
        } else {
            ((GXDLMSServer2) owner).onPreAction(args);
        }
    }

    /*
     * Client has try to made invalid connection. Password is incorrect.
     * @param connectionInfo Connection info.
     */
    final void notifyInvalidConnection(
            final GXDLMSConnectionEventArgs connectionInfo) {
        if (owner instanceof GXDLMSServer) {
            ((GXDLMSServer) owner).invalidConnection(connectionInfo);
        } else {
            ((GXDLMSServer2) owner).onInvalidConnection(connectionInfo);
        }
    }

    /*
     * Read selected item(s).
     * @param args Handled read requests.
     */
    final void notifyPostRead(final ValueEventArgs[] args) {
        if (owner instanceof GXDLMSServer2) {
            ((GXDLMSServer2) owner).onPostRead(args);
        }
    }

    /*
     * Write selected item(s).
     * @param args Handled write requests.
     */
    final void notifyPostWrite(final ValueEventArgs[] args) {
        if (owner instanceof GXDLMSServer2) {
            ((GXDLMSServer2) owner).onPostWrite(args);
        }
    }

    /*
     * Action is occurred.
     * @param args Handled action requests.
     */
    final void notifyPostAction(final ValueEventArgs[] args) {
        if (owner instanceof GXDLMSServer2) {
            ((GXDLMSServer2) owner).onPostAction(args);
        }
    }
}
