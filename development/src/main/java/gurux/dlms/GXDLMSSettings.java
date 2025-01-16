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

package gurux.dlms;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import gurux.dlms.asn.GXPkcs8;
import gurux.dlms.asn.GXx509Certificate;
import gurux.dlms.asn.enums.KeyUsage;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.ConnectionState;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.Signing;
import gurux.dlms.enums.Standard;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSSecuritySetup;
import gurux.dlms.objects.GXDLMSTcpUdpSetup;
import gurux.dlms.objects.enums.ApplicationContextName;
import gurux.dlms.objects.enums.AssociationStatus;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecuritySuite;

/**
 * This class includes DLMS communication settings.
 * 
 * @author Gurux Ltd.
 */
public class GXDLMSSettings {

    /**
     * The version can be used for backward compatibility.
     */
    private int version = 4;

    /**
     * HDLC settings.
     */
    private GXDLMSHdlcSetup hdlc;

    /**
     * Wrapper settings.
     */
    private GXDLMSTcpUdpSetup wrapper;

    /**
     * Frame check is skipped for some unit tests. This is internal use only.
     */
    private boolean skipFrameCheck = false;
    /**
     * Server sender frame sequence starting number.
     */
    static final short SERVER_START_SENDER_FRAME_SEQUENCE = 0x1E;

    /**
     * Server receiver frame sequence starting number.
     */
    static final short SERVER_START_RECEIVER_FRAME_SEQUENCE = 0xFE;

    /**
     * Client sender frame sequence starting number.
     */
    static final short CLIENT_START_SENDER_FRAME_SEQUENCE = 0xFE;

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
     * Pre-established system title.
     */
    private byte[] preEstablishedSystemTitle;

    /**
     * Invoke ID.
     */
    private byte invokeID = 0x1;
    /**
     * Long Invoke ID.
     */
    private long longInvokeID = 0x1;

    /**
     * Priority.
     */
    private Priority priority = Priority.HIGH;
    /**
     * Service class.
     */
    private ServiceClass serviceClass = ServiceClass.CONFIRMED;

    /**
     * Assigned association for the server.
     */
    GXDLMSAssociationLogicalName assignedAssociation;

    /**
     * Client address.
     */
    private int clientAddress;
    /**
     * Server address.
     */
    private int serverAddress;

    /**
     * Server is using push client address when sending push messages. Client
     * address is used if PushAddress is zero.
     */
    private int pushClientAddress;

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
    private long count;

    /**
     * Long data index.
     */
    private long index;

    /**
     * Target ephemeral public key.
     */
    private PublicKey targetEphemeralKey;

    /**
     * DLMS version number.
     */
    private int dlmsVersionNumber = DLMS_VERSION;

    private byte connected = ConnectionState.NONE;

    /**
     * Maximum receivers PDU size.
     */
    private int maxPduSize = MAX_RECEIVE_PDU_SIZE;

    /**
     * Server maximum PDU size.
     */
    private int maxServerPDUSize = MAX_RECEIVE_PDU_SIZE;

    /**
     * When connection is made client tells what kind of services it want's to
     * use.
     */
    private Set<Conformance> proposedConformance = new HashSet<Conformance>();

    /**
     * Server tells what functionality is available and client will know it.
     */
    private Set<Conformance> negotiatedConformance = new HashSet<Conformance>();

    /**
     * HDLC sender frame sequence number.
     */
    short senderFrame;

    /**
     * HDLC receiver frame sequence number.
     */
    short receiverFrame;

    /**
     * Is this server or client.
     */
    private boolean server;

    /**
     * HDLC framing settings.
     */
    private GXHdlcSettings hdlcSettings;

    /**
     * Gateway settings.
     */
    private GXDLMSGateway gateway;

    /**
     * PLC settings.
     */
    private GXPlcSettings plc;

    /**
     * M-Bus settings.
     */
    private GXMBusSettings mBus;

    /**
     * CoAP settings.
     */
    private GXCoAPSettings coap;

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
     * Crypto notifier.
     */
    private final IGXCryptoNotifier cryptoNotifier;

    /**
     * Custom object notifier.
     */
    private final IGXCustomObjectNotifier customObjectNotifier;

    /**
     * Block number acknowledged in GBT.
     */
    private int blockNumberAck;

    /**
     * GBT window size.
     */
    private byte gbtWindowSize;

    /*
     * User id is the identifier of the user. This value is used if user list on
     * Association LN is used.
     */
    private int userId;

    /*
     * Quality of service.
     */
    private byte qualityOfService;

    /**
     * Standard says that Time zone is from normal time to UTC in minutes. If
     * meter is configured to use UTC time (UTC to normal time) set this to
     * true.
     */
    private boolean useUtc2NormalTime;

    /**
     * Some meters expect that Invocation Counter is increased for
     * Authentication when connection is established.
     */
    private boolean increaseInvocationCounterForGMacAuthentication;

    /**
     * Skipped fields.
     */
    private java.util.Set<DateTimeSkips> dateTimeSkips = new HashSet<DateTimeSkips>();

    /**
     * Skipped fields.
     */
    private java.util.Set<DateTimeSkips> readDateTimeSkips = new HashSet<DateTimeSkips>();

    private Standard standard;

    /**
     * Last executed command.
     */
    private int command;

    /**
     * Last executed command type
     */
    private byte commandType;

    /**
     * Protocol version.
     */
    private String protocolVersion = null;

    /**
     * Auto increase Invoke ID.
     */
    private boolean autoIncreaseInvokeID = false;

    GXDLMSData invocationCounter;

    /**
     * Ephemeral Block cipher key.
     */
    private byte[] ephemeralBlockCipherKey;

    /**
     * Ephemeral broadcast block cipher key.
     */
    private byte[] ephemeralBroadcastBlockCipherKey;
    /**
     * Ephemeral authentication key.
     */
    private byte[] ephemeralAuthenticationKey;

    /**
     * Data is only decrypted and PDU is not parsed.
     */
    private boolean decryptOnly;

    /**
     * XML needs list of certificates to decrypt the data.
     */
    private List<Map.Entry<GXPkcs8, GXx509Certificate>> keys;

    /**
     * Size of the challenge.
     */
    private byte challengeSize = 16;

    private boolean overwriteAttributeAccessRights;

    /**
     * Optional ECDSA public key certificate that is send in part of AARQ.
     */
    private GXx509Certificate clientPublicKeyCertificate;

    /**
     * Optional ECDSA public key certificate that is send in part of AARE.
     */
    private GXx509Certificate serverPublicKeyCertificate;

    /**
     * @return Ephemeral Block cipher key.
     */
    public final byte[] getEphemeralBlockCipherKey() {
        return ephemeralBlockCipherKey;
    }

    /**
     * @param value
     *            Ephemeral Block cipher key.
     */
    public final void setEphemeralBlockCipherKey(final byte[] value) {
        ephemeralBlockCipherKey = value;
    }

    /**
     * @return Ephemeral broadcast block cipher key.
     */
    public final byte[] getEphemeralBroadcastBlockCipherKey() {
        return ephemeralBroadcastBlockCipherKey;
    }

    /**
     * @param value
     *            Ephemeral broadcast block cipher key.
     */
    public final void setEphemeralBroadcastBlockCipherKey(final byte[] value) {
        ephemeralBroadcastBlockCipherKey = value;
    }

    /**
     * @return Ephemeral authentication key.
     */
    public final byte[] getEphemeralAuthenticationKey() {
        return ephemeralAuthenticationKey;
    }

    /**
     * @param value
     *            Ephemeral authentication key.
     */
    public final void setEphemeralAuthenticationKey(final byte[] value) {
        ephemeralAuthenticationKey = value;
    }

    /*
     * Constructor.
     */
    GXDLMSSettings(final boolean isServer, final IGXCryptoNotifier notifier, final IGXCustomObjectNotifier notifier2) {
        server = isServer;
        cryptoNotifier = notifier;
        customObjectNotifier = notifier2;
        objects = new GXDLMSObjectCollection();
        hdlcSettings = new GXDLMSLimits(this);
        plc = new GXPlcSettings(this);
        mBus = new GXMBusSettings();
        coap = new GXCoAPSettings();
        gateway = null;
        proposedConformance.addAll(GXDLMSClient.getInitialConformance(false));
        if (isServer) {
            proposedConformance.add(Conformance.GENERAL_PROTECTION);
        }
        resetFrameSequence();
        gbtWindowSize = 1;
        userId = -1;
        standard = Standard.DLMS;
        keys = new ArrayList<Map.Entry<GXPkcs8, GXx509Certificate>>();
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
     * @return Used authentication.
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
    public final int getDlmsVersionNumber() {
        return dlmsVersionNumber;
    }

    /**
     * @param value
     *            Used DLMS version number.
     */
    public final void setDlmsVersionNumber(final int value) {
        dlmsVersionNumber = value;
    }

    /**
     * @return Is connected to the meter.
     */
    public final byte getConnected() {
        return connected;
    }

    /**
     * @param value
     *            Is connected to the meter.
     */
    public final void setConnected(final int value) {
        connected = (byte) value;
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
        // If notify
        if (frame == 0x13) {
            return true;
        }
        // If U frame.
        if ((frame & HdlcFrameType.U_FRAME.getValue()) == HdlcFrameType.U_FRAME.getValue()) {
            if (frame == 0x73 || frame == 0x93) {
                resetFrameSequence();
                return true;
            }
        }
        // If S -frame.
        if ((frame & HdlcFrameType.S_FRAME.getValue()) == HdlcFrameType.S_FRAME.getValue()) {
            receiverFrame = increaseReceiverSequence(receiverFrame);
            return true;
        }
        // Handle I-frame.
        short expected;
        if ((senderFrame & 0x1) == 0) {
            expected = (short) (increaseReceiverSequence(increaseSendSequence(receiverFrame)) & 0xFF);
            if (frame == expected) {
                receiverFrame = frame;
                return true;
            }
        } else {
            expected = (short) (increaseSendSequence(receiverFrame) & 0xFF);
            // If answer for RR.
            if (frame == expected) {
                receiverFrame = frame;
                return true;
            }
        }
        // This is for unit tests.
        if (skipFrameCheck) {
            receiverFrame = frame;
            return true;
        }
        Logger.getLogger(GXDLMS.class.getName()).log(Level.INFO,
                "Invalid HDLC Frame: " + Long.toString(frame, 16) + " Expected: " + Long.toString(expected, 16));
        return false;
    }

    /**
     * Increase receiver sequence.
     * 
     * @param value
     *            Frame value.
     * @return Increased receiver frame sequence.
     */
    static byte increaseReceiverSequence(final short value) {
        return (byte) ((value & 0xFF) + 0x20 | 0x10 | value & 0xE);
    }

    /**
     * Increase sender sequence.
     * 
     * @param value
     *            Frame value.
     * @return Increased sender frame sequence.
     */
    static short increaseSendSequence(final short value) {
        return (short) ((value & 0xF0 | (value + 0x2) & 0xE) & 0xFF);
    }

    /**
     * @param first
     *            Is this first packet.
     * @return Generated I-frame
     */
    final byte getNextSend(final boolean first) {
        if (first) {
            senderFrame = increaseReceiverSequence(increaseSendSequence((byte) senderFrame));
        } else {
            senderFrame = increaseSendSequence((byte) senderFrame);
        }
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
     * @return Block number acknowledged in GBT.
     */
    public final int getBlockNumberAck() {
        return blockNumberAck;
    }

    /**
     * @param value
     *            Block number acknowledged in GBT.
     */
    public final void setBlockNumberAck(final int value) {
        blockNumberAck = value;
    }

    /**
     * Resets block index to default value.
     */
    final void resetBlockIndex() {
        blockIndex = startingPacketIndex;
        blockNumberAck = 0;
    }

    /**
     * Increases block index.
     */
    final void increaseBlockIndex() {
        blockIndex += 1;
    }

    /**
     * @return Is server or client.
     */
    public final boolean isServer() {
        return server;
    }

    /**
     * @param value
     *            Is server or client.
     */
    public final void setServer(final boolean value) {
        server = value;
    }

    /**
     * @return Information from the frame size that server can handle.
     */
    public final GXHdlcSettings getHdlcSettings() {
        return hdlcSettings;
    }

    /**
     * @return Gateway settings.
     */
    public final GXDLMSGateway getGateway() {
        return gateway;
    }

    /**
     * @param value
     *            Gateway settings.
     */
    public final void setGateway(final GXDLMSGateway value) {
        gateway = value;
    }

    /**
     * @return PLC settings.
     */
    public final GXPlcSettings getPlc() {
        return plc;
    }

    /**
     * @param value
     *            PLC settings.
     */
    public final void setPlc(GXPlcSettings value) {
        plc = value;
    }

    /**
     * @param value
     *            HDLC connection settings.
     */
    public final void setLimits(final GXHdlcSettings value) {
        hdlcSettings = value;
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

    /*
     * General Block Transfer count in server.
     */
    private int gbtCount;

    /**
     * @param value
     *            General Block Transfer count in server.
     */
    public final void setGbtCount(final int value) {
        gbtCount = value;
    }

    /**
     * @return General Block Transfer count in server.
     */
    public final int getGbtCount() {
        return gbtCount;
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
    public final int getDLMSVersion() {
        return dlmsVersionNumber;
    }

    /**
     * @param value
     *            DLMS version number.
     */
    public final void setDLMSVersion(final int value) {
        dlmsVersionNumber = value;
    }

    /**
     * @return Maximum PDU size.
     */
    public final int getMaxPduSize() {
        return maxPduSize;
    }

    /**
     * @param value
     *            Maximum PDU size.
     */
    public final void setMaxPduSize(final int value) {
        maxPduSize = value;
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
        if (useLogicalNameReferencing != value) {
            useLogicalNameReferencing = value;
            proposedConformance.clear();
            proposedConformance.addAll(GXDLMSClient.getInitialConformance(getUseLogicalNameReferencing()));
            if (isServer()) {
                proposedConformance.add(Conformance.GENERAL_PROTECTION);
            }
        }
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

    private void updateSecurity(byte[] systemTitle, GXDLMSSecuritySetup ss) {
        if (ss != null) {
            getCipher().setSecuritySuite(ss.getSecuritySuite());
            getCipher().setSecurityPolicy(ss.getSecurityPolicy());
            getCipher().setBlockCipherKey(ss.getGuek());
            getCipher().setBroadcastBlockCipherKey(ss.getGbek());
            getCipher().setAuthenticationKey(ss.getGak());
            setEphemeralBlockCipherKey(ss.getGuek());
            setEphemeralBroadcastBlockCipherKey(ss.getGbek());
            setEphemeralAuthenticationKey(ss.getGak());
            setKek(ss.getKek());
            // Update certificates for pre-established connections.
            byte[] st;
            if (systemTitle == null) {
                st = ss.getClientSystemTitle();
            } else {
                st = systemTitle;
            }
            if (st != null) {
                GXx509Certificate cert = ss.serverCertificates.findBySystemTitle(st, KeyUsage.DIGITAL_SIGNATURE);
                if (cert != null) {
                    getCipher().setSigningKeyPair(new KeyPair(cert.getPublicKey(), ss.signingKey.getPrivate()));
                }
                cert = ss.serverCertificates.findBySystemTitle(st, KeyUsage.KEY_AGREEMENT);
                if (cert != null) {
                    getCipher().setKeyAgreementKeyPair(new KeyPair(cert.getPublicKey(), ss.keyAgreement.getPrivate()));
                }
                setSourceSystemTitle(st);
            }
            getCipher().setSystemTitle(ss.getServerSystemTitle());
            // Find Invocation counter and use it if it exists.
            String ln = "0.0.43.1." + ss.getLogicalName().split("[.]")[4] + ".255";
            invocationCounter = (GXDLMSData) getObjects().findByLN(ObjectType.DATA, ln);
            if (invocationCounter != null && invocationCounter.getValue() == null) {
                if (invocationCounter.getDataType(2) == DataType.NONE) {
                    invocationCounter.setDataType(2, DataType.UINT32);
                }
                invocationCounter.setValue(0);
            }
        } else {
            assignedAssociation.getApplicationContextName().setContextId(ApplicationContextName.LOGICAL_NAME);
        }
    }

    void updateSecuritySettings(final byte[] systemTitle) {
        if (assignedAssociation != null) {
            // Update security settings.
            if (assignedAssociation.getSecuritySetupReference() != null && (assignedAssociation
                    .getApplicationContextName().getContextId() == ApplicationContextName.LOGICAL_NAME_WITH_CIPHERING
                    || assignedAssociation.getAuthenticationMechanismName().getMechanismId() == Authentication.HIGH_GMAC
                    || assignedAssociation.getAuthenticationMechanismName()
                            .getMechanismId() == Authentication.HIGH_ECDSA)) {
                GXDLMSSecuritySetup ss = (GXDLMSSecuritySetup) assignedAssociation.getObjectList()
                        .findByLN(ObjectType.SECURITY_SETUP, assignedAssociation.getSecuritySetupReference());
                updateSecurity(systemTitle, ss);
            } else {
                GXDLMSSecuritySetup ss = (GXDLMSSecuritySetup) assignedAssociation.getObjectList()
                        .findByLN(ObjectType.SECURITY_SETUP, assignedAssociation.getSecuritySetupReference());
                updateSecurity(systemTitle, ss);
            }
        }
    }

    /**
     * @param value
     *            Current association of the server.
     */
    public final void setAssignedAssociation(final GXDLMSAssociationLogicalName value) {
        if (assignedAssociation != null) {
            assignedAssociation.setAssociationStatus(AssociationStatus.NON_ASSOCIATED);
            assignedAssociation.getXDLMSContextInfo().setCypheringInfo(null);
            invocationCounter = null;
            getCipher().getSecurityPolicy().clear();
            setEphemeralBlockCipherKey(null);
            setEphemeralBroadcastBlockCipherKey(null);
            setEphemeralAuthenticationKey(null);
            getCipher().setSecuritySuite(SecuritySuite.SUITE_0);
        }

        assignedAssociation = value;
        if (assignedAssociation != null) {
            proposedConformance = assignedAssociation.getXDLMSContextInfo().getConformance();
            maxServerPDUSize = assignedAssociation.getXDLMSContextInfo().getMaxReceivePduSize();
            authentication = assignedAssociation.getAuthenticationMechanismName().getMechanismId();
            updateSecuritySettings(null);
        }
    }

    /**
     * @return Current server association.
     */
    public final GXDLMSAssociationLogicalName getAssignedAssociation() {
        return assignedAssociation;
    }

    /**
     * @return Invoke ID.
     */
    public final int getInvokeID() {
        return invokeID;
    }

    /**
     * @param value
     *            update invoke ID.
     */
    final void updateInvokeId(final short value) {
        if ((value & 0x80) != 0) {
            setPriority(Priority.HIGH);
        } else {
            setPriority(Priority.NORMAL);
        }
        if ((value & 0x40) != 0) {
            setServiceClass(ServiceClass.CONFIRMED);
        } else {
            setServiceClass(ServiceClass.UN_CONFIRMED);
        }
        invokeID = (byte) (value & 0xF);
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
    public final long getLongInvokeID() {
        return longInvokeID;
    }

    /**
     * @param value
     *            Invoke ID.
     */
    public final void setLongInvokeID(final long value) {
        if (value < 0) {
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

    public boolean isCiphered(boolean checkGeneralSigning) {
        if (cipher == null) {
            return false;
        }
        return cipher.getSecurity() != Security.NONE
                || (checkGeneralSigning && cipher.getSigning() == Signing.GENERAL_SIGNING);
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
    public final void setSourceSystemTitle(final byte[] value) {
        if (value != null && value.length != 0 && value.length != 8) {
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
    public final long getCount() {
        return count;
    }

    /**
     * @param value
     *            Data count.
     */
    public final void setCount(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid count.");
        }
        count = value;
    }

    /**
     * @return Long data index.
     */
    public final long getIndex() {
        return index;
    }

    /**
     * @param value
     *            Long data index
     */
    public final void setIndex(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid Index.");
        }
        index = value;
    }

    /**
     * @return Target ephemeral public key.
     */
    public PublicKey getTargetEphemeralKey() {
        return targetEphemeralKey;
    }

    /**
     * @param value
     *            Target ephemeral public key .
     */
    public void setTargetEphemeralKey(final PublicKey value) {
        targetEphemeralKey = value;
    }

    /**
     * @return Proposed conformance block.
     */
    public Set<Conformance> getProposedConformance() {
        return proposedConformance;
    }

    /**
     * @param value
     *            Proposed conformance block.
     */
    public void setProposedConformance(final Set<Conformance> value) {
        proposedConformance = value;
    }

    /**
     * @return Server tells what functionality is available and client will know
     *         it.
     */
    public Set<Conformance> getNegotiatedConformance() {
        return negotiatedConformance;
    }

    /**
     * @param value
     *            Server tells what functionality is available and client will
     *            know it.
     */
    public void setNegotiatedConformance(final Set<Conformance> value) {
        negotiatedConformance = value;
    }

    /*
     * @param value Frame check is skipped for some unit tests.
     */
    final void setSkipFrameCheck(final boolean value) {
        skipFrameCheck = value;
    }

    /**
     * @return HDLC settings.
     */
    public GXDLMSHdlcSetup getHdlc() {
        return hdlc;
    }

    /**
     * @param value
     *            HDLC settings.
     */
    public void setHdlc(final GXDLMSHdlcSetup value) {
        hdlc = value;
    }

    /**
     * @return Wrapper settings.
     */
    public GXDLMSTcpUdpSetup getWrapper() {
        return wrapper;
    }

    /**
     * @param value
     *            Wrapper settings.
     */
    public void setWrapper(final GXDLMSTcpUdpSetup value) {
        wrapper = value;
    }

    /**
     * @return GBT window size.
     */
    public final byte getGbtWindowSize() {
        return gbtWindowSize;
    }

    /**
     * @param value
     *            GBT window size.
     */
    public final void setGbtWindowSize(final byte value) {
        gbtWindowSize = value;
    }

    /**
     * @return User id is the identifier of the user.
     */
    public final int getUserId() {
        return userId;
    }

    /**
     * @param value
     *            User id is the identifier of the user.
     */
    public final void setUserId(final int value) {
        userId = value;
    }

    /**
     * Standard says that Time zone is from normal time to UTC in minutes. If
     * meter is configured to use UTC time (UTC to normal time) set this to
     * true.
     * 
     * @return True, if UTC time is used.
     */
    public boolean getUseUtc2NormalTime() {
        return useUtc2NormalTime;
    }

    /**
     * * Standard says that Time zone is from normal time to UTC in minutes. If
     * meter is configured to use UTC time (UTC to normal time) set this to
     * true.
     * 
     * @param value
     *            True, if UTC time is used.
     */
    public void setUseUtc2NormalTime(final boolean value) {
        useUtc2NormalTime = value;
    }

    /**
     * @return Some meters expect that Invocation Counter is increased for
     *         Authentication when connection is established.
     */
    public boolean getIncreaseInvocationCounterForGMacAuthentication() {
        return increaseInvocationCounterForGMacAuthentication;
    }

    /**
     * @param value
     *            Some meters expect that Invocation Counter is increased for
     *            Authentication when connection is established.
     */
    public void setIncreaseInvocationCounterForGMacAuthentication(final boolean value) {
        increaseInvocationCounterForGMacAuthentication = value;
    }

    /**
     * @return Skipped date time fields. This value can be used if meter can't
     *         handle deviation or status.
     */
    public java.util.Set<DateTimeSkips> getDateTimeSkips() {
        return dateTimeSkips;
    }

    /**
     * @param value
     *            Skipped date time fields. This value can be used if meter
     *            can't handle deviation or status.
     */
    public void setDateTimeSkips(final java.util.Set<DateTimeSkips> value) {
        dateTimeSkips = value;
    }

    /**
     * @return Skipped date time fields on read. This value can be used if meter
     *         returns invalid deviation on read.
     */
    public java.util.Set<DateTimeSkips> getDateTimeSkipsOnRead() {
        return readDateTimeSkips;
    }

    /**
     * @param value
     *            Skipped date time fields on read. This value can be used if
     *            meter returns invalid deviation on read.
     */
    public void setDateTimeSkipsOnRead(final java.util.Set<DateTimeSkips> value) {
        readDateTimeSkips = value;
    }

    /**
     * @return Used standard.
     */
    public Standard getStandard() {
        return standard;
    }

    /**
     * @param value
     *            Used standard.
     */
    public void setStandard(final Standard value) {
        standard = value;
    }

    /**
     * @return Protocol version.
     */
    public String getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * @param value
     *            Protocol version.
     */
    public void setProtocolVersion(final String value) {
        protocolVersion = value;
    }

    /**
     * @return Pre-established system title.
     */
    public byte[] getPreEstablishedSystemTitle() {
        return preEstablishedSystemTitle;
    }

    /**
     * @param value
     *            Pre-established system title.
     */
    public void setPreEstablishedSystemTitle(final byte[] value) {
        if (value != null && value.length != 8) {
            throw new IllegalArgumentException("Invalid system title.");
        }
        preEstablishedSystemTitle = value;
    }

    /**
     * @return the command
     */
    public int getCommand() {
        return command;
    }

    /**
     * @param value
     *            the command to set
     */
    public void setCommand(final int value) {
        command = value;
    }

    /**
     * @return the commandType
     */
    public byte getCommandType() {
        return commandType;
    }

    /**
     * @param value
     *            the commandType to set
     */
    public void setCommandType(final byte value) {
        commandType = value;
    }

    /**
     * @return Quality of service.
     */
    public byte getQualityOfService() {
        return qualityOfService;
    }

    /**
     * @param value
     *            Quality of service.
     */
    public void setQualityOfService(final byte value) {
        qualityOfService = value;
    }

    /**
     * @return Auto increase Invoke ID.
     */
    public final boolean getAutoIncreaseInvokeID() {
        return autoIncreaseInvokeID;
    }

    /**
     * @param value
     *            Auto increase Invoke ID.
     */
    public final void setAutoIncreaseInvokeID(final boolean value) {
        autoIncreaseInvokeID = value;
    }

    /**
     * @return The version can be used for backward compatibility.
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param value
     *            The version can be used for backward compatibility.
     */
    public void setVersion(final int value) {
        if (value != 3 && value != 4) {
            throw new IllegalArgumentException("Invalid version.");
        }
        version = value;
    }

    /**
     * @return Server is using push client address when sending push messages.
     *         Client address is used if PushAddress is zero.
     */
    public int getPushClientAddress() {
        return pushClientAddress;
    }

    /**
     * @param value
     *            Server is using push client address when sending push
     *            messages. Client address is used if PushAddress is zero.
     */
    public void setPushClientAddress(final int value) {
        pushClientAddress = value;
    }

    /**
     * @return XML needs list of certificates to decrypt the data.
     */
    public List<Map.Entry<GXPkcs8, GXx509Certificate>> getKeys() {
        return keys;
    }

    /**
     * @param value
     *            XML needs list of certificates to decrypt the data.
     */
    public void setKeys(final List<Map.Entry<GXPkcs8, GXx509Certificate>> value) {
        keys = value;
    }

    public GXDLMSData getInvocationCounter() {
        return invocationCounter;
    }

    public void setInvocationCounter(GXDLMSData invocationCounter) {
        this.invocationCounter = invocationCounter;
    }

    /**
     * @return Crypto notifier.
     */
    public IGXCryptoNotifier getCryptoNotifier() {
        return cryptoNotifier;
    }

    public Object getKey(CertificateType certificateType, byte[] systemTitle, boolean encrypt) {
        if (cryptoNotifier == null) {
            throw new RuntimeException("Failed to get the certificate.");
        }
        if (certificateType == CertificateType.DIGITAL_SIGNATURE && cipher.getSigningKeyPair() != null) {
            if (encrypt) {
                if (cipher.getSigningKeyPair().getPrivate() != null) {
                    return cipher.getSigningKeyPair().getPrivate();
                }
            } else if (cipher.getSigningKeyPair().getPublic() != null) {
                return cipher.getSigningKeyPair().getPublic();
            }
        } else if (certificateType == CertificateType.KEY_AGREEMENT && cipher.getKeyAgreementKeyPair() != null) {
            if (encrypt) {
                if (cipher.getKeyAgreementKeyPair().getPrivate() != null) {
                    return cipher.getKeyAgreementKeyPair().getPrivate();
                }
            } else if (cipher.getKeyAgreementKeyPair().getPublic() != null) {
                return cipher.getKeyAgreementKeyPair().getPublic();
            }
        }
        GXCryptoKeyParameter args = new GXCryptoKeyParameter();
        args.setEncrypt(encrypt);
        args.setSecuritySuite(cipher.getSecuritySuite());
        args.setCertificateType(certificateType);
        args.setSystemTitle(systemTitle);
        cryptoNotifier.onKey(cryptoNotifier, args);
        if (encrypt) {
            return args.getPrivateKey();
        } else {
            return args.getPublicKey();
        }
    }

    /**
     * @return Size of the challenge.
     */
    public byte getChallengeSize() {
        return challengeSize;
    }

    /**
     * @param value
     *            Size of the challenge.
     */
    public void setChallengeSize(byte value) {
        if (authentication == Authentication.HIGH_ECDSA && value < 32) {
            throw new IllegalArgumentException(
                    "Invalid challenge size. ECDSA challenge must be between 32 to 64 bytes.");
        } else if (value < 8 || value > 64) {
            throw new IllegalArgumentException("Invalid challenge size. Challenge must be between 8 to 64 bytes.");
        }
        challengeSize = value;
    }

    /**
     * @return Overwrite attribute access rights if association view tells wrong
     *         access rights and they need to be overwritten.
     */
    public final boolean getOverwriteAttributeAccessRights() {
        return overwriteAttributeAccessRights;
    }

    /**
     * @param value
     *            Overwrite attribute access rights if association view tells
     *            wrong access rights and they need to be overwritten.
     */
    public final void setOverwriteAttributeAccessRights(boolean value) {
        overwriteAttributeAccessRights = value;
    }

    /**
     * @return M-Bus settings.
     */
    public GXMBusSettings getMbus() {
        return mBus;
    }

    /**
     * @return CoAP settings.
     */
    public GXCoAPSettings getCoap() {
        return coap;
    }

    /**
     * @return Optional ECDSA public key certificate that is send in part of
     *         AARQ.
     */
    public GXx509Certificate getClientPublicKeyCertificate() {
        return clientPublicKeyCertificate;
    }

    /**
     * @param value
     *            Optional ECDSA public key certificate that is send in part of
     *            AARQ.
     */
    public void setClientPublicKeyCertificate(final GXx509Certificate value) {
        clientPublicKeyCertificate = value;
    }

    /**
     * @return Optional ECDSA public key certificate that is send in part of
     *         AARE.
     */
    public GXx509Certificate getServerPublicKeyCertificate() {
        return serverPublicKeyCertificate;
    }

    /**
     * @param value
     *            Optional ECDSA public key certificate that is send in part of
     *            AARE.
     */
    public void setServerPublicKeyCertificate(final GXx509Certificate value) {
        serverPublicKeyCertificate = value;
    }

    /**
     * @return Data is only decrypted and PDU is not parsed.
     */
    public boolean isDecryptOnly() {
        return decryptOnly;
    }

    /**
     * @param value
     *            Data is only decrypted and PDU is not parsed.
     */
    public void setDecryptOnly(final boolean value) {
        decryptOnly = value;
    }

    /**
     * @return Custom object notifier.
     */
    public IGXCustomObjectNotifier getCustomObjectNotifier() {
        return customObjectNotifier;
    }
}
