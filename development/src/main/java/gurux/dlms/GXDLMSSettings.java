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
     * Server sender frame sequence starting number.
     */
    static final short SERVER_START_SENDER_FRAME_SEQUENCE = 0x1E;

    /**
     * Server receiver frame sequence starting number.
     */
    static final short SERVER_START_RECEIVER_FRAME_SEQUENCE = 0xEE;

    /**
     * Client sender frame sequence starting number.
     */
    static final short CLIENT_START_SENDER_FRAME_SEQUENCE = 0x10;

    /**
     * Client receiver frame sequence starting number.
     */
    static final short CLIENT_START_RCEIVER_FRAME_SEQUENCE = 0xE;

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
     * Source challenge.
     */
    private byte[] ctoSChallenge;
    /**
     * Server to Client challenge.
     */
    private byte[] stoCChallenge;

    /**
     * Source system title.
     */
    private byte[] sourceSystemTitle;

    /**
     * Invoke ID.
     */
    private byte invokeID = 0x1;
    /**
     * Long Invoke ID.
     */
    private int longInvokeID = 0x1;

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
     * Server address side.
     */
    private int serverAddressSize = 0;

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
     * Key Encrypting Key, also known as Master key.
     */
    private byte[] kek;

    /**
     * Long data count.
     */
    private int count;

    /**
     * Long data index.
     */
    private int index;

    /**
     * DLMS version number.
     */
    private byte dlmsVersionNumber = DLMS_VERSION;

    private boolean connected = false;

    /**
     * Maximum receivers PDU size.
     */
    private int maxReceivePDUSize = MAX_RECEIVE_PDU_SIZE;

    /**
     * Server maximum PDU size.
     */
    public int maxServerPDUSize = MAX_RECEIVE_PDU_SIZE;

    /**
     * HDLC sender frame sequence number.
     */
    private short senderFrame;

    /**
     * HDLC receiver block sequence number.
     */
    private short receiverFrame;

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

    private int startingPacketIndex = 1;

    /**
     * Block packet index.
     */
    private int blockIndex = 1;
    /**
     * List of server or client objects.
     */
    private final GXDLMSObjectCollection objects;

    /**
     * Cipher interface that is used to cipher PDU.
     */
    private GXICipher cipher;

    /**
     * Constructor.
     */
    GXDLMSSettings(final boolean isServer) {
        server = isServer;
        objects = new GXDLMSObjectCollection();
        limits = new GXDLMSLimits();
        if (isServer) {
            lnSettings = new GXDLMSLNSettings(new byte[] { 0x00, 0x7C, 0x1F });
        } else {
            lnSettings = new GXDLMSLNSettings(new byte[] { 0x00, 0x7E, 0x1F });
        }
        snSettings = new GXDLMSSNSettings(new byte[] { 0x1C, 0x03, 0x20 });
        resetFrameSequence();
    }

    /**
     * @param value
     *            Cipher interface that is used to cipher PDU.
     */
    final void setCipher(final GXICipher value) {
        cipher = value;
    }

    /**
     * @return Cipher interface that is used to cipher PDU.
     */
    public final GXICipher getCipher() {
        return cipher;
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
     * @return Is connected to the meter.
     */
    public final boolean getConnected() {
        return connected;
    }

    /**
     * @param value
     *            Is connected to the meter.
     */
    public final void setConnected(final boolean value) {
        connected = value;
    }

    /**
     * Reset frame sequence.
     */
    public final void resetFrameSequence() {
        if (server) {
            senderFrame = SERVER_START_SENDER_FRAME_SEQUENCE;
            receiverFrame = SERVER_START_RECEIVER_FRAME_SEQUENCE;
        } else {
            senderFrame = CLIENT_START_SENDER_FRAME_SEQUENCE;
            receiverFrame = CLIENT_START_RCEIVER_FRAME_SEQUENCE;
        }
    }

    final boolean checkFrame(final short frame) {
        // If U frame.
        if ((frame & HdlcFrameType.U_FRAME.getValue()) == HdlcFrameType.U_FRAME
                .getValue()) {
            if (frame == 0x73 || frame == 0x93) {
                resetFrameSequence();
                return true;
            }
        }
        // If S -frame.
        if ((frame & HdlcFrameType.S_FRAME.getValue()) == HdlcFrameType.S_FRAME
                .getValue()) {
            receiverFrame = increaseReceiverSequence(receiverFrame);
            return true;
        }
        // Handle I-frame.
        if (frame == increaseReceiverSequence(
                increaseSendSequence(receiverFrame))) {
            receiverFrame = frame;
            return true;
        }
        // If answer for RR.
        if (frame == increaseSendSequence(receiverFrame)) {
            receiverFrame = frame;
            return true;
        }
        System.out.println("Frame ID do not match.");
        return true;
        // TODO: unit test must fix first. return false;
    }

    /**
     * Increase receiver sequence.
     * 
     * @param value
     *            Frame value.
     * @return Increased receiver frame sequence.
     */
    static byte increaseReceiverSequence(final short value) {
        return (byte) (value + 0x20 | 0x10 | value & 0xE);
    }

    /**
     * Increase sender sequence.
     * 
     * @param value
     *            Frame value.
     * @return Increased sender frame sequence.
     */
    static byte increaseSendSequence(final short value) {
        return (byte) (value & 0xF0 | (value + 0x2) & 0xE);
    }

    /**
     * @return Generates I-frame.
     */
    final byte getNextSend() {
        senderFrame = increaseReceiverSequence(
                increaseSendSequence((byte) senderFrame));
        return (byte) senderFrame;
    }

    /**
     * @return Generates Receiver Ready S-frame.
     */
    final byte getReceiverReady() {
        senderFrame = increaseReceiverSequence((byte) (senderFrame | 1));
        return (byte) (senderFrame & 0xF1);
    }

    /**
     * @return Generates Keep Alive S-frame.
     */
    final byte getKeepAlive() {
        senderFrame = (byte) (senderFrame | 1);
        return (byte) (senderFrame & 0xF1);
    }

    /**
     * @return Gets Logical Name settings.
     */
    public final GXDLMSLNSettings getLnSettings() {
        return lnSettings;
    }

    /**
     * @return Short name settings.
     */
    public final GXDLMSSNSettings getSnSettings() {
        return snSettings;
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
     * Gets starting block index in HDLC framing. Default is One based, but some
     * meters use Zero based value. Usually this is not used.
     * 
     * @return Current block index.
     */
    final int getStartingPacketIndex() {
        return startingPacketIndex;
    }

    /**
     * Set starting block index in HDLC framing. Default is One based, but some
     * meters use Zero based value. Usually this is not used.
     * 
     * @param value
     *            Zero based starting index.
     */
    public final void setStartingPacketIndex(final int value) {
        startingPacketIndex = value;
        resetBlockIndex();
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
        blockIndex = startingPacketIndex;
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
     * @return Server address size in bytes. If it is Zero it is counted
     *         automatically.
     */
    public final int getServerAddressSize() {
        return serverAddressSize;
    }

    /**
     * @param value
     *            Server address size in bytes. If it is Zero it is counted
     *            automatically.
     */
    public final void setServerAddressSize(final int value) {
        serverAddressSize = value;
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
     * @return Server maximum PDU size.
     */
    public final int getMaxServerPDUSize() {
        return maxServerPDUSize;
    }

    /**
     * @param value
     *            Server maximum PDU size.
     */
    public final void setMaxServerPDUSize(final int value) {
        maxServerPDUSize = value;
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
        invokeID = (byte) value;
    }

    /**
     * @return Invoke ID.
     */
    public final int getLongInvokeID() {
        return longInvokeID;
    }

    /**
     * @param value
     *            Invoke ID.
     */
    public final void setLongInvokeID(final int value) {
        if (value > 0xFFFFFF) {
            throw new IllegalArgumentException("Invalid InvokeID");
        }
        longInvokeID = value;
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
    public final void setUseCustomChallenge(final boolean value) {
        customChallenges = value;
    }

    /**
     * @return Source system title.
     */
    public final byte[] getSourceSystemTitle() {
        return sourceSystemTitle;
    }

    /**
     * @param value
     *            Source system title.
     */
    final void setSourceSystemTitle(final byte[] value) {
        if (value != null && value.length != 8) {
            throw new IllegalArgumentException("Invalid client system title.");
        }
        sourceSystemTitle = value;
    }

    /**
     * @return Key Encrypting Key, also known as Master key.
     */
    public final byte[] getKek() {
        return kek;
    }

    /**
     * @param value
     *            Key Encrypting Key, also known as Master key.
     */
    public final void setKek(final byte[] value) {
        kek = value;
    }

    /**
     * @return Long data count.
     */
    public final int getCount() {
        return count;
    }

    /**
     * @param value
     *            Data count.
     */
    public final void setCount(final int value) {
        count = value;
    }

    /**
     * @return Long data index.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            Long data index
     */
    public final void setIndex(final int value) {
        index = value;
    }
}
