package gurux.dlms;

import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.objects.GXDLMSObjectCollection;

/**
 * This class includes DLMS communication settings.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSSettings {
    /**
     * Server frame sequence starting number.
     */
    static final byte SERVER_START_FRAME_SEQUENCE = 0x0F;
    /**
     * Client frame sequence starting number.
     */
    static final byte CLIENT_START_FRAME_SEQUENCE = (byte) 0xEE;

    /**
     * DLMS version number.
     */
    static final byte DLMS_VERSION = 6;

    static final int MAX_RECEIVE_PDU_SIZE = 0xFFFF;

    /**
     * Is custom challenges used. If custom challenge is used new challenge is
     * not generated if it is set. This is for debugging purposes.
     */
    private boolean customChallenges = false;

    /**
     * Client to server challenge.
     */
    private byte[] ctoSChallenge;
    /**
     * Server to Client challenge.
     */
    private byte[] stoCChallenge;
    /**
     * Invoke ID.
     */
    private int invokeID = 0x1;
    /**
     * Priority.
     */
    private Priority priority = Priority.HIGH;
    /**
     * Service class.
     */
    private ServiceClass serviceClass = ServiceClass.UN_CONFIRMED;
    /**
     * Client address.
     */
    private int clientAddress;
    /**
     * Server address.
     */
    private int serverAddress;
    /**
     * Is Logical Name referencing used.
     */
    private boolean useLogicalNameReferencing;
    /**
     * Interface type.
     */
    private InterfaceType interfaceType = InterfaceType.HDLC;
    /**
     * User authentication.
     */
    private Authentication authentication = Authentication.NONE;
    /**
     * User password.
     */
    private byte[] password;
    /**
     * DLMS version number.
     */
    private byte dlmsVersionNumber = DLMS_VERSION;
    /**
     * Maximum receivers PDU size.
     */
    private int maxReceivePDUSize = MAX_RECEIVE_PDU_SIZE;
    /**
     * Frame sequence number.
     */
    private byte frame = 0x10;

    /**
     * Is this server or client.
     */
    private final boolean server;

    /**
     * Information from the connection size that server can handle.
     */
    private GXDLMSLimits limits;
    /**
     * Logical name settings.
     */
    private GXDLMSLNSettings lnSettings;
    /**
     * Short name settings.
     */
    private GXDLMSSNSettings snSettings;

    /**
     * Block packet index.
     */
    private int blockIndex = 1;
    /**
     * List of server or client objects.
     */
    private final GXDLMSObjectCollection objects;

    /**
     * Constructor.
     */
    GXDLMSSettings(final boolean isServer) {
        server = isServer;
        objects = new GXDLMSObjectCollection();
        limits = new GXDLMSLimits();
    }

    /**
     * @return Client to Server challenge.
     */
    public final byte[] getCtoSChallenge() {
        return ctoSChallenge;
    }

    /**
     * @param value
     *            Client to Server challenge.
     */
    public final void setCtoSChallenge(final byte[] value) {
        if (!customChallenges || ctoSChallenge == null) {
            ctoSChallenge = value;
        }
    }

    /**
     * @return Server to Client challenge.
     */
    public final byte[] getStoCChallenge() {
        return stoCChallenge;
    }

    /**
     * @param value
     *            Server to Client challenge.
     */
    public final void setStoCChallenge(final byte[] value) {
        if (!customChallenges || stoCChallenge == null) {
            stoCChallenge = value;
        }
    }

    /**
     * @return Gets used authentication.
     */
    public final Authentication getAuthentication() {
        return authentication;
    }

    /**
     * @param value
     *            Used authentication.
     */
    public final void setAuthentication(final Authentication value) {
        authentication = value;
    }

    /**
     * @return Gets password.
     */
    public final byte[] getPassword() {
        return password;
    }

    /**
     * @param value
     *            Sets password.
     */
    public final void setPassword(final byte[] value) {
        password = value;
    }

    /**
     * @return Used DLMS version number.
     */
    public final byte getDlmsVersionNumber() {
        return dlmsVersionNumber;
    }

    /**
     * @param value
     *            Used DLMS version number.
     */
    public final void setDlmsVersionNumber(final byte value) {
        dlmsVersionNumber = value;
    }

    /**
     * Reset frame sequence.
     */
    public final void resetFrameSequence() {
        if (server) {
            frame = SERVER_START_FRAME_SEQUENCE;
        } else {
            frame = CLIENT_START_FRAME_SEQUENCE;
        }
    }

    /**
     * @return Is frame generated. This is used to tell is any packet send to
     *         the server.
     */
    public final boolean isGenerated() {
        if (server) {
            return frame != SERVER_START_FRAME_SEQUENCE;
        } else {
            return frame != CLIENT_START_FRAME_SEQUENCE;
        }
    }

    /**
     * Increase receiver sequence.
     * 
     * @param value
     *            Frame value.
     * @return Increased receiver frame sequence.
     */
    static byte increaseReceiverSequence(final byte value) {
        return (byte) (value + 0x20 | 0x10 | value & 0xE);
    }

    /**
     * Increase sender sequence.
     * 
     * @param value
     *            Frame value.
     * @return Increased sender frame sequence.
     */
    static byte increaseSendSequence(final byte value) {
        return (byte) (value & 0xF0 | (value + 0x2) & 0xE);
    }

    /**
     * @return Generates next frame.
     */
    final byte getNextFrame() {
        frame = increaseReceiverSequence(increaseSendSequence(frame));
        return frame;
    }

    /**
     * @return Generates send frame.
     */
    final byte getNextSend() {
        frame = increaseSendSequence(frame);
        return frame;
    }

    /**
     * @return Generates receive frame.
     */
    final byte getNextReceive() {
        frame = increaseReceiverSequence(frame);
        return frame;
    }

    /**
     * @return Generates receiver ready frame.
     */
    final byte getReceiverReady() {
        frame = (byte) (increaseReceiverSequence(frame) & 0xF0 | 1);
        return frame;
    }

    /**
     * @return Gets Logical Name settings.
     */
    public final GXDLMSLNSettings getLnSettings() {
        return lnSettings;
    }

    /**
     * @param value
     *            Logical Name settings.
     */
    public final void setLnSettings(final GXDLMSLNSettings value) {
        lnSettings = value;
    }

    /**
     * @return Short name settings.
     */
    public final GXDLMSSNSettings getSnSettings() {
        return snSettings;
    }

    /**
     * @param value
     *            Short Name settings.
     */
    public final void setSnSettings(final GXDLMSSNSettings value) {
        snSettings = value;
    }

    /**
     * Gets current block index.
     * 
     * @return Current block index.
     */
    final int getBlockIndex() {
        return blockIndex;
    }

    /**
     * Sets current block index.
     * 
     * @param value
     *            Block index.
     */
    final void setBlockIndex(final int value) {
        blockIndex = value;
    }

    /**
     * Resets block index to default value.
     */
    final void resetBlockIndex() {
        blockIndex = 1;
    }

    /**
     * Increases block index.
     */
    final void increaseBlockIndex() {
        blockIndex += 1;
    }

    public final boolean isServer() {
        return server;
    }

    /**
     * @return Information from the frame size that server can handle.
     */
    public final GXDLMSLimits getLimits() {
        return limits;
    }

    /**
     * @param value
     *            Information from the frame size that server can handle.
     */
    public final void setLimits(final GXDLMSLimits value) {
        limits = value;
    }

    /**
     * @return Used interface.
     */
    public final InterfaceType getInterfaceType() {
        return interfaceType;
    }

    /**
     * @param value
     *            Used interface.
     */
    public final void setInterfaceType(final InterfaceType value) {
        interfaceType = value;
    }

    /**
     * @return Client address.
     */
    public final int getClientAddress() {
        return clientAddress;
    }

    /**
     * @param value
     *            Client address.
     */
    public final void setClientAddress(final int value) {
        clientAddress = value;
    }

    /**
     * @return Server address.
     */
    public final int getServerAddress() {
        return serverAddress;
    }

    /**
     * @param value
     *            Server address.
     */
    public final void setServerAddress(final int value) {
        serverAddress = value;
    }

    /**
     * @return DLMS version number.
     */
    public final byte getDLMSVersion() {
        return dlmsVersionNumber;
    }

    /**
     * @param value
     *            DLMS version number.
     */
    public final void setDLMSVersion(final byte value) {
        dlmsVersionNumber = value;
    }

    /**
     * @return Maximum PDU size.
     */
    public final int getMaxReceivePDUSize() {
        return maxReceivePDUSize;
    }

    /**
     * @param value
     *            Maximum PDU size.
     */
    public final void setMaxReceivePDUSize(final int value) {
        maxReceivePDUSize = value;
    }

    /**
     * @return Is Logical Name Referencing used.
     */
    public final boolean getUseLogicalNameReferencing() {
        return useLogicalNameReferencing;
    }

    /**
     * @param value
     *            Is Logical Name Referencing used.
     */
    public final void setUseLogicalNameReferencing(final boolean value) {
        useLogicalNameReferencing = value;
    }

    /**
     * @return Used priority.
     */
    public final Priority getPriority() {
        return priority;
    }

    /**
     * @param value
     *            Used priority.
     */
    public final void setPriority(final Priority value) {
        priority = value;
    }

    /**
     * @return Used service class.
     */
    public final ServiceClass getServiceClass() {
        return serviceClass;
    }

    /**
     * @param value
     *            Used service class.
     */
    public final void setServiceClass(final ServiceClass value) {
        serviceClass = value;
    }

    /**
     * @return Invoke ID.
     */
    public final int getInvokeID() {
        return invokeID;
    }

    /**
     * @param value
     *            Invoke ID.
     */
    public final void setInvokeID(final int value) {
        if (value > 0xF) {
            throw new IllegalArgumentException("Invalid InvokeID");
        }
        invokeID = value;
    }

    /**
     * @return Collection of the objects.
     */
    public final GXDLMSObjectCollection getObjects() {
        return objects;
    }

    /**
     * @return Is custom challenges used.
     */
    public final boolean isCustomChallenges() {
        return customChallenges;
    }

    /**
     * @param value
     *            Is custom challenges used.
     */
    public final void setCustomChallenges(final boolean value) {
        customChallenges = value;
    }
}
