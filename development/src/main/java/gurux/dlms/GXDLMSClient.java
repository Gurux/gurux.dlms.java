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

import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AccessServiceCommandType;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.DateTimeSkips;
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
import gurux.dlms.manufacturersettings.GXObisCodeCollection;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.IGXDLMSBase;
import gurux.dlms.secure.GXSecure;

/**
 * GXDLMS implements methods to communicate with DLMS/COSEM metering devices.
 */
public class GXDLMSClient {
    /**
     * DLMS settings.
     */
    private final GXDLMSSettings settings = new GXDLMSSettings(false);
    private GXObisCodeCollection obisCodes;
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSClient.class.getName());

    /**
     * Is authentication required.
     */
    private boolean isAuthenticationRequired = false;

    /**
     * Constructor.
     */
    public GXDLMSClient() {
        this(false);
    }

    /**
     * Constructor.
     * 
     * @param useLogicalNameReferencing
     *            Is Logical Name referencing used.
     */
    public GXDLMSClient(final boolean useLogicalNameReferencing) {
        this(useLogicalNameReferencing, 16, 1, Authentication.NONE, null,
                InterfaceType.HDLC);
    }

    /**
     * Constructor.
     * 
     * @param useLogicalNameReferencing
     *            Is Logical Name referencing used.
     * @param clientAddress
     *            Server address.
     * @param serverAddress
     *            Client address.
     * @param forAuthentication
     *            Authentication type.
     * @param password
     *            Password if authentication is used.
     * @param interfaceType
     *            Object type.
     */
    public GXDLMSClient(final boolean useLogicalNameReferencing,
            final int clientAddress, final int serverAddress,
            final Authentication forAuthentication, final String password,
            final InterfaceType interfaceType) {
        setUseLogicalNameReferencing(useLogicalNameReferencing);
        setClientAddress(clientAddress);
        setServerAddress(serverAddress);
        setAuthentication(forAuthentication);
        setPassword(GXCommon.getBytes(password));
        setInterfaceType(interfaceType);
    }

    /**
     * @param value
     *            Cipher interface that is used to cipher PDU.
     */
    protected final void setCipher(final GXICipher value) {
        settings.setCipher(value);
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
     * This list is used when Association view is read from the meter and
     * description of the object is needed. If collection is not set description
     * of object is empty.
     * 
     * @return List of available OBIS codes.
     */
    public final GXObisCodeCollection getObisCodes() {
        return obisCodes;
    }

    public final void setObisCodes(final GXObisCodeCollection value) {
        obisCodes = value;
    }

    /**
     * Set starting packet index. Default is One based, but some meters use Zero
     * based value. Usually this is not used.
     * 
     * @param value
     *            Zero based starting index.
     */
    public final void setStartingPacketIndex(final int value) {
        settings.setStartingPacketIndex(value);
    }

    /**
     * @return Client address.
     */
    public final int getClientAddress() {
        return settings.getClientAddress();
    }

    /**
     * @param value
     *            Client address
     */
    public final void setClientAddress(final int value) {
        settings.setClientAddress(value);
    }

    /**
     * @return Server Address.
     */
    public final int getServerAddress() {
        return settings.getServerAddress();
    }

    /**
     * @param value
     *            Server address.
     */
    public final void setServerAddress(final int value) {
        settings.setServerAddress(value);
    }

    /**
     * @return Server address size in bytes. If it is Zero it is counted
     *         automatically.
     */
    public final int getServerAddressSize() {
        return settings.getServerAddressSize();
    }

    /**
     * @param value
     *            Server address size in bytes. If it is Zero it is counted
     *            automatically.
     */
    public final void setServerAddressSize(final int value) {
        settings.setServerAddressSize(value);
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

    /// <summary>
    /// Client to Server custom challenge.
    /// </summary>
    /// <remarks>
    /// This is for debugging purposes. Reset custom challenge settings
    /// CtoSChallenge to null.
    /// </remarks>

    /**
     * Client to Server custom challenge.
     * 
     * @return Client to Server custom challenge.
     */
    public final byte[] getCtoSChallenge() {
        return settings.getCtoSChallenge();
    }

    /**
     * Client to Server custom challenge. This is for debugging purposes. Reset
     * custom challenge settings CtoSChallenge to null.
     * 
     * @param value
     *            Client to Server challenge.
     */
    public final void setCtoSChallenge(final byte[] value) {
        settings.setUseCustomChallenge(value != null);
        settings.setCtoSChallenge(value);
    }

    /**
     * Retrieves the password that is used in communication. If authentication
     * is set to none, password is not used.
     * 
     * @see GXDLMSClient#getAuthentication
     * @return Used password.
     */
    public final byte[] getPassword() {
        return settings.getPassword();
    }

    /**
     * @param value
     *            Used password as byte array.
     */
    public final void setPassword(final byte[] value) {
        settings.setPassword(value);
    }

    /**
     * @param value
     *            Used password as string value.
     */
    public final void setPassword(final String value) {
        settings.setPassword(value.getBytes());
    }

    /**
     * @return Functionality what server offers.
     */
    public final java.util.Set<Conformance> getNegotiatedConformance() {
        return settings.getNegotiatedConformance();
    }

    /**
     * @return When connection is made client tells what kind of services it
     *         want's to use.
     */
    public final java.util.Set<Conformance> getProposedConformance() {
        return settings.getProposedConformance();
    }

    /**
     * @param value
     *            When connection is made client tells what kind of services it
     *            want's to use.
     */
    public final void
            setProposedConformance(final java.util.Set<Conformance> value) {
        settings.setProposedConformance(value);
    }

    /**
     * Retrieves the authentication used in communicating with the device. By
     * default authentication is not used. If authentication is used, set the
     * password with the Password property.
     * 
     * @see GXDLMSClient#getPassword
     * @see GXDLMSClient#getClientAddress
     * @return Used authentication.
     */
    public final Authentication getAuthentication() {
        return settings.getAuthentication();
    }

    /**
     * @param value
     *            Used authentication.
     */
    public final void setAuthentication(final Authentication value) {
        settings.setAuthentication(value);
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
     * @return Interface type.
     */
    public final InterfaceType getInterfaceType() {
        return settings.getInterfaceType();
    }

    /**
     * @param value
     *            Interface type.
     */
    public final void setInterfaceType(final InterfaceType value) {
        settings.setInterfaceType(value);
    }

    /**
     * @return Information from the connection size that server can handle.
     */
    public final GXDLMSLimits getLimits() {
        return settings.getLimits();
    }

    /**
     * Generates SNRM request. his method is used to generate send SNRMRequest.
     * Before the SNRM request can be generated, at least the following
     * properties must be set:
     * <ul>
     * <li>ClientAddress</li>
     * <li>ServerAddress</li>
     * </ul>
     * <b>Note! </b>According to IEC 62056-47: when communicating using TCP/IP,
     * the SNRM request is not send.
     * 
     * @see GXDLMSClient#getClientAddress
     * @see GXDLMSClient#getServerAddress
     * @see GXDLMSClient#parseUAResponse
     * @return SNRM request as byte array.
     */
    public final byte[] snrmRequest() {
        settings.setConnected(false);

        isAuthenticationRequired = false;
        settings.setMaxPduSize(0xFFFF);
        // SNRM request is not used in network connections.
        if (this.getInterfaceType() == InterfaceType.WRAPPER) {
            return new byte[0];
        }
        GXByteBuffer data = new GXByteBuffer(25);
        data.setUInt8(0x81); // FromatID
        data.setUInt8(0x80); // GroupID
        data.setUInt8(0); // Length.

        // If custom HDLC parameters are used.
        if (GXDLMSLimits.DEFAULT_MAX_INFO_TX != getLimits().getMaxInfoTX()) {
            data.setUInt8(HDLCInfo.MAX_INFO_TX);
            data.setUInt8(1);
            data.setUInt8(getLimits().getMaxInfoTX());
        }
        if (GXDLMSLimits.DEFAULT_MAX_INFO_RX != getLimits().getMaxInfoRX()) {
            data.setUInt8(HDLCInfo.MAX_INFO_RX);
            data.setUInt8(1);
            data.setUInt8(getLimits().getMaxInfoRX());
        }
        if (GXDLMSLimits.DEFAULT_WINDOWS_SIZE_TX != getLimits()
                .getWindowSizeTX()) {
            data.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
            data.setUInt8(4);
            data.setUInt32(getLimits().getWindowSizeTX());
        }
        if (GXDLMSLimits.DEFAULT_WINDOWS_SIZE_TX != getLimits()
                .getWindowSizeRX()) {
            data.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
            data.setUInt8(4);
            data.setUInt32(getLimits().getWindowSizeRX());
        }
        // If default HDLC parameters are not used.
        if (data.size() != 3) {
            data.setUInt8(2, data.size() - 3); // Length.
        } else {
            data = null;
        }
        return GXDLMS.getHdlcFrame(settings, (byte) Command.SNRM, data);
    }

    /**
     * Parses UAResponse from byte array.
     * 
     * @param data
     *            Received message from the server.
     * @see GXDLMSClient#snrmRequest
     */
    public final void parseUAResponse(final byte[] data) {
        GXDLMS.parseSnrmUaResponse(new GXByteBuffer(data),
                settings.getLimits());
    }

    /**
     * Parses UAResponse from byte array.
     * 
     * @param data
     *            Received message from the server.
     * @see GXDLMSClient#snrmRequest
     */
    public final void parseUAResponse(final GXByteBuffer data) {
        GXDLMS.parseSnrmUaResponse(data, settings.getLimits());
    }

    /**
     * Generate AARQ request. Because all meters can't read all data in one
     * packet, the packet must be split first, by using SplitDataToPackets
     * method.
     * 
     * @return AARQ request as byte array.
     * @see GXDLMSClient#parseAareResponse
     */
    public final byte[][] aarqRequest() {
        GXByteBuffer buff = new GXByteBuffer(20);
        settings.resetBlockIndex();
        GXDLMS.checkInit(settings);
        settings.setStoCChallenge(null);
        // If authentication or ciphering is used.
        if (getAuthentication().ordinal() > Authentication.LOW.ordinal()) {
            settings.setCtoSChallenge(
                    GXSecure.generateChallenge(settings.getAuthentication()));
        } else {
            settings.setCtoSChallenge(null);
        }
        GXAPDU.generateAarq(settings, settings.getCipher(), null, buff);
        List<byte[]> reply;
        if (settings.getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.AARQ, 0, buff, null, 0xff);
            reply = GXDLMS.getLnMessages(p);
        } else {
            GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                    Command.AARQ, 0, 0, null, buff);
            reply = GXDLMS.getSnMessages(p);
        }
        return reply.toArray(new byte[][] {});
    }

    /**
     * Parses the AARE response. Parse method will update the following data:
     * <ul>
     * <li>DLMSVersion</li>
     * <li>MaxReceivePDUSize</li>
     * <li>UseLogicalNameReferencing</li>
     * <li>LNSettings or SNSettings</li>
     * </ul>
     * LNSettings or SNSettings will be updated, depending on the referencing,
     * Logical name or Short name.
     * 
     * @param reply
     *            Received data.
     * @see GXDLMSClient#aarqRequest
     * @see GXDLMSClient#getUseLogicalNameReferencing
     * @see GXDLMSClient#getNegotiatedConformance
     * @see GXDLMSClient#getProposedConformance
     */
    public final void parseAareResponse(final GXByteBuffer reply) {
        settings.setConnected(true);
        isAuthenticationRequired =
                GXAPDU.parsePDU(settings, settings.getCipher(), reply,
                        null) == SourceDiagnostic.AUTHENTICATION_REQUIRED;
        if (settings.getDLMSVersion() != 6) {
            throw new IllegalArgumentException("Invalid DLMS version number.");
        }
    }

    /**
     * @return Is authentication Required.
     */
    public final boolean getIsAuthenticationRequired() {
        return isAuthenticationRequired;
    }

    /**
     * @return Get challenge request if HLS authentication is used.
     */
    public final byte[][] getApplicationAssociationRequest() {
        if (settings.getAuthentication() != Authentication.HIGH_ECDSA
                && settings.getAuthentication() != Authentication.HIGH_GMAC
                && (settings.getPassword() == null
                        || settings.getPassword().length == 0)) {
            throw new IllegalArgumentException("Password is invalid.");
        }
        settings.resetBlockIndex();
        byte[] pw;
        if (settings.getAuthentication() == Authentication.HIGH_GMAC) {
            pw = settings.getCipher().getSystemTitle();
        } else {
            pw = settings.getPassword();
        }
        long ic = 0;
        if (settings.getCipher() != null) {
            ic = settings.getCipher().getInvocationCounter();
        }
        byte[] challenge = GXSecure.secure(settings, settings.getCipher(), ic,
                settings.getStoCChallenge(), pw);
        if (getUseLogicalNameReferencing()) {
            return method("0.0.40.0.0.255", ObjectType.ASSOCIATION_LOGICAL_NAME,
                    1, challenge, DataType.OCTET_STRING);
        }
        return method(0xFA00, ObjectType.ASSOCIATION_SHORT_NAME, 8, challenge,
                DataType.OCTET_STRING);
    }

    /**
     * Parse server's challenge if HLS authentication is used.
     * 
     * @param reply
     *            Received reply from the server.
     */
    public final void
            parseApplicationAssociationResponse(final GXByteBuffer reply) {
        GXDataInfo info = new GXDataInfo();
        boolean equals = false;
        byte[] secret;
        long ic = 0;
        byte[] value = (byte[]) GXCommon.getData(reply, info);
        if (value != null) {
            if (settings.getAuthentication() == Authentication.HIGH_ECDSA) {
                try {
                    Signature ver = Signature.getInstance("SHA256withECDSA");
                    ver.initVerify(settings.getCipher().getCertificates().get(0)
                            .getPublicKey());
                    GXByteBuffer bb = new GXByteBuffer();
                    bb.set(settings.getSourceSystemTitle());
                    bb.set(settings.getCipher().getSystemTitle());
                    bb.set(settings.getCtoSChallenge());
                    bb.set(settings.getStoCChallenge());
                    ver.update(bb.array());
                    equals = ver.verify(value);

                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            } else {
                if (settings.getAuthentication() == Authentication.HIGH_GMAC) {
                    secret = settings.getSourceSystemTitle();
                    GXByteBuffer bb = new GXByteBuffer(value);
                    bb.getUInt8();
                    ic = bb.getUInt32();
                } else {
                    secret = settings.getPassword();
                }
                byte[] tmp = GXSecure.secure(settings, settings.getCipher(), ic,
                        settings.getCtoSChallenge(), secret);
                GXByteBuffer challenge = new GXByteBuffer(tmp);
                equals = challenge.compare(value);
                if (!equals) {
                    LOGGER.log(Level.INFO,
                            "Invalid StoC:" + GXCommon.toHex(value, true) + "-"
                                    + GXCommon.toHex(tmp, true));
                }
            }
        } else {
            LOGGER.log(Level.INFO, "Server did not accept CtoS.");
        }

        if (!equals) {
            throw new GXDLMSException(
                    "parseApplicationAssociationResponse failed. "
                            + " Server to Client do not match.");
        }
    }

    /**
     * Generates a release request.
     * 
     * @return Release request, as byte array.
     */
    public byte[][] releaseRequest() {
        // If connection is not established, there is no need to send
        // DisconnectRequest.
        if (!settings.isConnected()) {
            return null;
        }
        GXByteBuffer buff = new GXByteBuffer();
        // Length.
        buff.setUInt8(0);
        buff.setUInt8(0x80);
        buff.setUInt8(01);
        buff.setUInt8(00);
        GXAPDU.generateUserInformation(settings, settings.getCipher(), null,
                buff);
        buff.setUInt8(0, (byte) (buff.size() - 1));
        List<byte[]> reply;
        if (getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.RELEASE_REQUEST, 0, buff, null, 0xff);
            reply = GXDLMS.getLnMessages(p);
        } else {
            reply = GXDLMS.getSnMessages(new GXDLMSSNParameters(settings,
                    Command.RELEASE_REQUEST, 0xFF, 0xFF, null, buff));
        }
        if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            settings.setConnected(false);
        }
        return reply.toArray(new byte[][] {});
    }

    /**
     * Generates a disconnect request.
     * 
     * @return Disconnected request, as byte array.
     */
    public final byte[] disconnectRequest() {
        settings.setMaxPduSize(0xFFFF);
        // If connection is not established, there is no need to send
        // DisconnectRequest.
        if (!settings.isConnected()) {
            return new byte[0];
        }
        if (this.getInterfaceType() == InterfaceType.HDLC) {
            return GXDLMS.getHdlcFrame(settings, Command.DISCONNECT_REQUEST,
                    null);
        }
        GXByteBuffer bb = new GXByteBuffer(2);
        bb.setUInt8(Command.RELEASE_REQUEST);
        bb.setUInt8(0x0);
        return GXDLMS.getWrapperFrame(settings, bb);
    }

    /**
     * Reserved for internal use.
     * 
     * @param classID
     *            Class ID.
     * @param version
     *            Version number.
     * @param baseName
     *            Short name.
     * @param ln
     *            Logical name.
     * @param accessRights
     *            Array of access rights.
     * @return Created COSEM object.
     */
    static GXDLMSObject createDLMSObject(final int classID,
            final Object version, final int baseName, final Object ln,
            final Object accessRights) {
        ObjectType type = ObjectType.forValue(classID);
        GXDLMSObject obj = createObject(type);
        updateObjectData(obj, type, version, baseName, (byte[]) ln,
                accessRights);
        return obj;
    }

    /**
     * Parse SN objects.
     * 
     * @param buff
     *            Byte stream where objects are parsed.
     * @param onlyKnownObjects
     *            Only known objects are parsed.
     * @return Collection of COSEM objects.
     */
    private GXDLMSObjectCollection parseSNObjects(final GXByteBuffer buff,
            final boolean onlyKnownObjects) {
        // Get array tag.
        buff.position(0);
        short size = buff.getUInt8();
        // Check that data is in the array
        if (size != 0x01) {
            throw new GXDLMSException("Invalid response.");
        }
        GXDLMSObjectCollection items = new GXDLMSObjectCollection(this);
        long cnt = GXCommon.getObjectCount(buff);
        GXDataInfo info = new GXDataInfo();
        for (long objPos = 0; objPos != cnt; ++objPos) {
            // Some meters give wrong item count.
            if (buff.position() == buff.size()) {
                break;
            }
            info.setCount(0);
            info.setIndex(0);
            info.setType(DataType.NONE);
            Object[] objects = (Object[]) GXCommon.getData(buff, info);
            if (objects.length != 4) {
                throw new GXDLMSException("Invalid structure format.");
            }
            int classID = ((Number) (objects[1])).intValue() & 0xFFFF;
            int baseName = ((Number) (objects[0])).intValue() & 0xFFFF;
            if (baseName > 0) {
                GXDLMSObject comp = createDLMSObject(classID, objects[2],
                        baseName, objects[3], null);
                if (!onlyKnownObjects
                        || comp.getClass() != GXDLMSObject.class) {
                    items.add(comp);
                } else {
                    System.out.println(
                            "Unknown object : " + String.valueOf(classID) + " "
                                    + String.valueOf(baseName));
                }
            }
        }
        return items;
    }

    /**
     * Reserved for internal use.
     * 
     * @param objectType
     * @param version
     * @param baseName
     * @param logicalName
     * @param accessRights
     */
    static void updateObjectData(final GXDLMSObject obj,
            final ObjectType objectType, final Object version,
            final Object baseName, final byte[] logicalName,
            final Object accessRights) {
        obj.setObjectType(objectType);
        // Check access rights.
        if (accessRights instanceof Object[]
                && ((Object[]) accessRights).length == 2) {
            // access_rights: access_right
            Object[] access = (Object[]) accessRights;
            for (Object attributeAccess : (Object[]) access[0]) {
                int id = ((Number) ((Object[]) attributeAccess)[0]).intValue();
                // Kamstrup is returning -1 here.
                if (id > 0) {
                    int tmp = ((Number) ((Object[]) attributeAccess)[1])
                            .intValue();
                    AccessMode mode = AccessMode.forValue(tmp);
                    obj.setAccess(id, mode);
                }
            }
            for (Object methodAccess : (Object[]) access[1]) {
                int id = ((Number) ((Object[]) methodAccess)[0]).intValue();
                int tmp;
                // If version is 0
                if (((Object[]) methodAccess)[1] instanceof Boolean) {
                    if ((Boolean) ((Object[]) methodAccess)[1]) {
                        tmp = 1;
                    } else {
                        tmp = 0;
                    }
                } else {
                    // If version is 1.
                    tmp = ((Number) ((Object[]) methodAccess)[1]).intValue();
                }
                MethodAccessMode mode = MethodAccessMode.forValue(tmp);
                obj.setMethodAccess(id, mode);
            }
        }
        if (baseName != null) {
            obj.setShortName(((Number) baseName).intValue());
        }
        if (version != null) {
            obj.setVersion(((Number) version).intValue());
        }
        obj.setLogicalName(GXCommon.toLogicalName(logicalName));
    }

    /**
     * Parses the COSEM objects of the received data.
     * 
     * @param data
     *            Received data, from the device, as byte array.
     * @param onlyKnownObjects
     *            Only known objects are parsed.
     * @return Collection of COSEM objects.
     */
    public final GXDLMSObjectCollection parseObjects(final GXByteBuffer data,
            final boolean onlyKnownObjects) {
        if (data == null) {
            throw new GXDLMSException("Invalid parameter.");
        }
        GXDLMSObjectCollection objects;
        if (getUseLogicalNameReferencing()) {
            objects = parseLNObjects(data, onlyKnownObjects);
        } else {
            objects = parseSNObjects(data, onlyKnownObjects);
        }
        settings.getObjects().addAll(objects);
        return objects;
    }

    /**
     * Parse LN objects.
     * 
     * @param buff
     *            Byte stream where objects are parsed.
     * @param onlyKnownObjects
     *            Only known objects are parsed.
     * @return Collection of COSEM objects.
     */
    private GXDLMSObjectCollection parseLNObjects(final GXByteBuffer buff,
            final boolean onlyKnownObjects) {
        // Get array tag.
        byte size = buff.getInt8();
        // Check that data is in the array
        if (size != 0x01) {
            throw new GXDLMSException("Invalid response.");
        }
        GXDLMSObjectCollection items = new GXDLMSObjectCollection(this);
        GXDataInfo info = new GXDataInfo();
        long cnt = GXCommon.getObjectCount(buff);
        for (long objPos = 0; objPos != cnt; ++objPos) {
            // Some meters give wrong item count.
            // This fix Iskraemeco (MT-880) bug.
            if (buff.position() == buff.size()) {
                break;
            }
            info.setType(DataType.NONE);
            info.setIndex(0);
            info.setCount(0);
            Object[] objects = (Object[]) GXCommon.getData(buff, info);
            if (objects.length != 4) {
                throw new GXDLMSException("Invalid structure format.");
            }
            int classID = ((Number) (objects[0])).intValue() & 0xFFFF;
            if (classID > 0) {
                GXDLMSObject comp = createDLMSObject(classID, objects[1], 0,
                        objects[2], objects[3]);
                if (!onlyKnownObjects
                        || comp.getClass() != GXDLMSObject.class) {
                    items.add(comp);
                } else {
                    System.out.println("Unknown object : "
                            + String.valueOf(classID) + " "
                            + GXCommon.toLogicalName((byte[]) objects[2]));
                }
            }
        }
        return items;
    }

    /**
     * Update value from byte array received from the meter.
     * 
     * @param target
     *            COSEM object.
     * @param attributeIndex
     *            Attribute index.
     * @param value
     *            Value to update.
     * @return Updated value.
     */
    public final Object updateValue(final GXDLMSObject target,
            final int attributeIndex, final Object value) {
        return updateValue(target, attributeIndex, value, null);
    }

    /**
     * Update value from byte array received from the meter.
     * 
     * @param target
     *            COSEM object.
     * @param attributeIndex
     *            Attribute index.
     * @param value
     *            Value to update.
     * @param parameters
     *            Optional parameters.
     * @return Updated value.
     */
    public final Object updateValue(final GXDLMSObject target,
            final int attributeIndex, final Object value,
            final Object parameters) {
        Object val = value;
        if (val instanceof byte[]) {
            DataType type = target.getUIDataType(attributeIndex);
            if (type == DataType.DATETIME && ((byte[]) val).length == 5) {
                type = DataType.DATE;
                target.setUIDataType(attributeIndex, type);
            }
            if (type != DataType.NONE) {
                val = changeType((byte[]) value, type);
            }
        }
        ValueEventArgs e = new ValueEventArgs(settings, target, attributeIndex,
                0, parameters);
        e.setValue(val);
        target.setValue(settings, e);
        return target.getValues()[attributeIndex - 1];
    }

    /**
     * Get Value from byte array received from the meter.
     * 
     * @param data
     *            Byte array received from the meter.
     * @return Received data.
     */
    public static Object getValue(final GXByteBuffer data) {
        GXDataInfo info = new GXDataInfo();
        return GXCommon.getData(data, info);
    }

    /**
     * Update list of values.
     * 
     * @param list
     *            read objects.
     * @param values
     *            Received values.
     */
    public final void updateValues(
            final List<Entry<GXDLMSObject, Integer>> list,
            final Object[] values) {
        int pos = 0;
        for (Entry<GXDLMSObject, Integer> it : list) {
            ValueEventArgs e = new ValueEventArgs(settings, it.getKey(),
                    it.getValue(), 0, null);
            e.setValue(values[pos]);
            it.getKey().setValue(settings, e);
            ++pos;
        }
    }

    /**
     * Update list of values.
     * 
     * @param list
     *            read objects.
     * @param values
     *            Received values.
     */
    public final void updateValues(
            final List<Entry<GXDLMSObject, Integer>> list,
            final List<Object> values) {
        int pos = 0;
        for (Entry<GXDLMSObject, Integer> it : list) {
            ValueEventArgs e = new ValueEventArgs(settings, it.getKey(),
                    it.getValue(), 0, null);
            e.setValue(values.get(pos));
            it.getKey().setValue(settings, e);
            ++pos;
        }
    }

    /**
     * Changes byte array received from the meter to given type.
     * 
     * @param value
     *            Byte array received from the meter.
     * @param type
     *            Wanted type.
     * @return Value changed by type.
     */
    public static Object changeType(final byte[] value, final DataType type) {
        if (value == null) {
            return null;
        }
        if (type == DataType.NONE) {
            return GXCommon.toHex(value, true);
        }
        if (value.length == 0
                && (type == DataType.STRING || type == DataType.OCTET_STRING)) {
            return "";
        }
        if (value.length == 0 && type == DataType.DATETIME) {
            return new GXDateTime(new Date(0));
        }
        if (value.length == 0 && type == DataType.DATE) {
            return new GXDate(new Date(0));
        }
        if (value.length == 0 && type == DataType.TIME) {
            return new GXTime(new Date(0));
        }

        GXDataInfo info = new GXDataInfo();
        info.setType(type);
        Object ret = GXCommon.getData(new GXByteBuffer(value), info);
        if (!info.isComplete()) {
            throw new IllegalArgumentException(
                    "Change type failed. Not enought data.");
        }
        if (type == DataType.OCTET_STRING && ret instanceof byte[]) {
            return GXCommon.toHex((byte[]) ret);
        }
        return ret;
    }

    /**
     * Reads the Association view from the device. This method is used to get
     * all objects in the device.
     * 
     * @return Read request, as byte array.
     */
    public final byte[] getObjectsRequest() {
        Object name;
        settings.resetBlockIndex();
        if (getUseLogicalNameReferencing()) {
            name = "0.0.40.0.0.255";
        } else {
            name = (short) 0xFA00;
        }
        return read(name, ObjectType.ASSOCIATION_LOGICAL_NAME, 2)[0];
    }

    /**
     * Generate Method (Action) request.
     * 
     * @param item
     *            Method object short name or Logical Name.
     * @param index
     *            Method index.
     * @param data
     *            Method data.
     * @param type
     *            Data type.
     * @return DLMS action message.
     */
    public final byte[][] method(final GXDLMSObject item, final int index,
            final Object data, final DataType type) {
        return method(item.getName(), item.getObjectType(), index, data, type);
    }

    /**
     * Generate Method (Action) request..
     * 
     * @param name
     *            Method object short name or Logical Name.
     * @param objectType
     *            Object type.
     * @param methodIndex
     *            Method index.
     * @param value
     *            Method data.
     * @param dataType
     *            Data type.
     * @return DLMS action message.
     */
    public final byte[][] method(final Object name, final ObjectType objectType,
            final int methodIndex, final Object value,
            final DataType dataType) {
        if (name == null || methodIndex < 1) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        settings.resetBlockIndex();
        int index = methodIndex;
        DataType type = dataType;
        if (type == DataType.NONE && value != null) {
            type = GXDLMSConverter.getDLMSDataType(value);
            if (type == DataType.NONE) {
                throw new GXDLMSException(
                        "Invalid parameter. In java value type must give.");
            }
        }
        List<byte[]> reply;
        GXByteBuffer data = new GXByteBuffer();
        GXByteBuffer attributeDescriptor = new GXByteBuffer();
        GXCommon.setData(data, type, value);

        if (getUseLogicalNameReferencing()) {
            // CI
            attributeDescriptor.setUInt16(objectType.getValue());
            // Add LN
            attributeDescriptor.set(GXCommon.logicalNameToBytes((String) name));
            // Attribute ID.
            attributeDescriptor.setUInt8((byte) methodIndex);
            // Method Invocation Parameters is not used.
            if (type == DataType.NONE) {
                attributeDescriptor.setUInt8(0);
            } else {
                attributeDescriptor.setUInt8(1);
            }
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.METHOD_REQUEST, ActionRequestType.NORMAL,
                    attributeDescriptor, data, 0xff);
            reply = GXDLMS.getLnMessages(p);
        } else {
            int requestType;
            if (type == DataType.NONE) {
                requestType = VariableAccessSpecification.VARIABLE_NAME;
            } else {
                requestType = VariableAccessSpecification.PARAMETERISED_ACCESS;
            }
            int[] ind = new int[1], count = new int[1];
            GXDLMS.getActionInfo(objectType, ind, count);
            if (index > count[0]) {
                throw new IllegalArgumentException("methodIndex");
            }

            int sn = GXCommon.intValue(name);
            index = (ind[0] + (index - 1) * 0x8);
            sn += index;
            // Add name.
            attributeDescriptor.setUInt16(sn);
            // Add selector.
            if (type != DataType.NONE) {
                attributeDescriptor.setUInt8(1);
            } else {
                attributeDescriptor.setUInt8(0);
            }
            GXDLMSSNParameters p =
                    new GXDLMSSNParameters(settings, Command.READ_REQUEST, 1,
                            requestType, attributeDescriptor, data);
            reply = GXDLMS.getSnMessages(p);
        }
        return reply.toArray(new byte[][] {});
    }

    /**
     * Generates a write message.
     * 
     * @param item
     *            COSEM object to read.
     * @param index
     *            Attribute index.
     * @return Generated write message(s).
     */
    public final byte[][] write(final GXDLMSObject item, final int index) {
        ValueEventArgs e = new ValueEventArgs(settings, item, index, 0, null);
        Object value = item.getValue(settings, e);
        DataType type = item.getDataType(index);
        if (type == DataType.OCTET_STRING && value instanceof String) {
            DataType ui = item.getUIDataType(index);
            if (ui == DataType.STRING) {
                return write(item.getName(), ((String) value).getBytes(), type,
                        item.getObjectType(), index);
            }
        }
        return write(item.getName(), value, type, item.getObjectType(), index);
    }

    /**
     * Generates a write message.
     * 
     * @param name
     *            Short or Logical Name.
     * @param value
     *            Data to Write.
     * @param dataType
     *            Data type of write object.
     * @param objectType
     *            Object type.
     * @param index
     *            Attribute index where data is write.
     * @return Generated write message(s).
     */
    public final byte[][] write(final Object name, final Object value,
            final DataType dataType, final ObjectType objectType,
            final int index) {
        if (index < 1) {
            throw new GXDLMSException("Invalid parameter");
        }
        settings.resetBlockIndex();
        DataType type = dataType;
        if (type == DataType.NONE && value != null) {
            type = GXDLMSConverter.getDLMSDataType(value);
            if (type == DataType.NONE) {
                throw new GXDLMSException(
                        "Invalid parameter. In java value type must give.");
            }
        }
        List<byte[]> reply;
        GXByteBuffer data = new GXByteBuffer();
        GXByteBuffer attributeDescriptor = new GXByteBuffer();
        GXCommon.setData(data, type, value);
        if (getUseLogicalNameReferencing()) {
            // Add CI.
            attributeDescriptor.setUInt16(objectType.getValue());
            // Add LN.
            attributeDescriptor.set(GXCommon.logicalNameToBytes((String) name));
            // Attribute ID.
            attributeDescriptor.setUInt8(index);
            // Access selection is not used.
            attributeDescriptor.setUInt8(0);
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.SET_REQUEST, SetRequestType.NORMAL,
                    attributeDescriptor, data, 0xff);
            reply = GXDLMS.getLnMessages(p);
        } else {
            // Add name.
            int sn = GXCommon.intValue(name);
            sn += (index - 1) * 8;
            attributeDescriptor.setUInt16(sn);
            // Add data count.
            attributeDescriptor.setUInt8(1);
            GXDLMSSNParameters p =
                    new GXDLMSSNParameters(settings, Command.WRITE_REQUEST, 1,
                            VariableAccessSpecification.VARIABLE_NAME,
                            attributeDescriptor, data);
            reply = GXDLMS.getSnMessages(p);
        }
        return reply.toArray(new byte[0][0]);
    }

    /**
     * Write list of COSEM objects.
     * 
     * @param list
     *            DLMS objects to write.
     * @return Write request as byte array.
     */
    public final byte[][] writeList(final List<GXWriteItem> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        Object value;
        List<byte[]> reply;
        settings.resetBlockIndex();
        GXByteBuffer data = new GXByteBuffer();
        GXByteBuffer bb = new GXByteBuffer();
        if (this.getUseLogicalNameReferencing()) {
            // Add length.
            bb.setUInt8(list.size());
            for (GXWriteItem it : list) {
                // CI.
                bb.setUInt16(it.getTarget().getObjectType().getValue());
                List<String> items =
                        GXCommon.split(it.getTarget().getLogicalName(), '.');
                if (items.size() != 6) {
                    throw new IllegalArgumentException("Invalid Logical Name.");
                }
                for (String it2 : items) {
                    bb.setUInt8(Integer.valueOf(it2).byteValue());
                }
                // Attribute ID.
                bb.setUInt8(it.getIndex());
                // Attribute selector is not used.
                bb.setUInt8(0);
            }
        } else {
            for (GXWriteItem it : list) {
                // Add variable type.
                bb.setUInt8(2);
                int sn = GXCommon.intValue(it.getTarget().getShortName());
                sn += (it.getIndex() - 1) * 8;
                bb.setUInt16(sn);
            }
        }
        // Write values.
        for (GXWriteItem it : list) {
            ValueEventArgs e = new ValueEventArgs(settings, it.getTarget(),
                    it.getIndex(), it.getSelector(), it.getParameters());
            value = it.getTarget().getValue(settings, e);
            if ((value instanceof byte[])) {
                data.set((byte[]) value);
            } else {
                DataType type = it.getDataType();
                if (type == DataType.NONE && value != null) {
                    type = GXDLMSConverter.getDLMSDataType(value);
                    if (type == DataType.NONE) {
                        throw new GXDLMSException("Invalid parameter. "
                                + " In java value type must give.");
                    }
                }
                GXCommon.setData(data, type, value);
            }
        }
        if (this.getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p =
                    new GXDLMSLNParameters(settings, Command.SET_REQUEST, 0,
                            SetRequestType.WITH_LIST, bb, data, 0xff);
            reply = GXDLMS.getLnMessages(p);
        } else {
            GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                    Command.WRITE_REQUEST, list.size(), 4, bb, data);
            reply = GXDLMS.getSnMessages(p);
        }
        return reply.toArray(new byte[0][0]);
    }

    /**
     * Generates a read message.
     * 
     * @param name
     *            Short or Logical Name.
     * @param objectType
     *            COSEM object type.
     * @param attributeOrdinal
     *            Attribute index of the object.
     * @return Generated read message(s).
     */
    public final byte[][] read(final Object name, final ObjectType objectType,
            final int attributeOrdinal) {
        return read(name, objectType, attributeOrdinal, null);
    }

    /**
     * Generates a read message.
     * 
     * @param name
     *            Short or Logical Name.
     * @param objectType
     *            COSEM object type.
     * @param attributeOrdinal
     *            Attribute index of the object.
     * @param data
     *            Read data parameter.
     * @return Generated read message(s).
     */
    private byte[][] read(final Object name, final ObjectType objectType,
            final int attributeOrdinal, final GXByteBuffer data) {
        if ((attributeOrdinal < 1)) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        GXByteBuffer attributeDescriptor = new GXByteBuffer();
        List<byte[]> reply;
        settings.resetBlockIndex();
        if (this.getUseLogicalNameReferencing()) {
            // CI
            attributeDescriptor.setUInt16(objectType.getValue());
            // Add LN
            attributeDescriptor.set(GXCommon.logicalNameToBytes((String) name));
            // Attribute ID.
            attributeDescriptor.setUInt8(attributeOrdinal);
            if (data == null || data.size() == 0) {
                // Access selection is not used.
                attributeDescriptor.setUInt8(0);
            } else {
                // Access selection is used.
                attributeDescriptor.setUInt8(1);
            }
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.GET_REQUEST, GetCommandType.NORMAL,
                    attributeDescriptor, data, 0xFF);
            reply = GXDLMS.getLnMessages(p);
        } else {
            int requestType;
            int sn = GXCommon.intValue(name);
            sn += (attributeOrdinal - 1) * 8;
            attributeDescriptor.setUInt16(sn);
            // parameterized-access
            if (data != null && data.size() != 0) {
                requestType = VariableAccessSpecification.PARAMETERISED_ACCESS;
            } else {
                // variable-name
                requestType = VariableAccessSpecification.VARIABLE_NAME;
            }
            GXDLMSSNParameters p =
                    new GXDLMSSNParameters(settings, Command.READ_REQUEST, 1,
                            requestType, attributeDescriptor, data);
            reply = GXDLMS.getSnMessages(p);
        }
        return reply.toArray(new byte[0][0]);
    }

    /**
     * Generates a read message.
     * 
     * @param item
     *            DLMS object to read.
     * @param attributeOrdinal
     *            Read attribute index.
     * @return Read request as byte array.
     */
    public final byte[][] read(final GXDLMSObject item,
            final int attributeOrdinal) {
        return read(item.getName(), item.getObjectType(), attributeOrdinal);
    }

    /**
     * Read list of COSEM objects.
     * 
     * @param list
     *            DLMS objects to read.
     * @return Read request as byte array.
     */
    public final byte[][]
            readList(final List<Entry<GXDLMSObject, Integer>> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        List<byte[]> messages = new ArrayList<byte[]>();
        GXByteBuffer data = new GXByteBuffer();
        settings.resetBlockIndex();
        if (this.getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p =
                    new GXDLMSLNParameters(settings, 0, Command.GET_REQUEST,
                            GetCommandType.WITH_LIST, data, null, 0xff);
            // Request service primitive shall always fit in a single APDU.
            int pos = 0, count = (settings.getMaxPduSize() - 12) / 10;
            if (list.size() < count) {
                count = list.size();
            }
            // All meters can handle 10 items.
            if (count > 10) {
                count = 10;
            }
            // Add length.
            GXCommon.setObjectCount(count, data);
            for (Entry<GXDLMSObject, Integer> it : list) {
                // CI.
                data.setUInt16(it.getKey().getObjectType().getValue());
                List<String> items =
                        GXCommon.split(it.getKey().getLogicalName(), '.');
                if (items.size() != 6) {
                    throw new IllegalArgumentException("Invalid Logical Name.");
                }
                for (String it2 : items) {
                    data.setUInt8(Integer.valueOf(it2).byteValue());
                }
                // Attribute ID.
                data.setUInt8(it.getValue());
                // Attribute selector is not used.
                data.setUInt8(0);

                ++pos;
                if (pos % count == 0 && list.size() != pos) {
                    messages.addAll(GXDLMS.getLnMessages(p));
                    data.clear();
                    if (list.size() - pos < count) {
                        GXCommon.setObjectCount(list.size() - pos, data);
                    } else {
                        GXCommon.setObjectCount(count, data);
                    }
                }
            }
            messages.addAll(GXDLMS.getLnMessages(p));
        } else {
            GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                    Command.READ_REQUEST, list.size(), 0xFF, data, null);
            for (Entry<GXDLMSObject, Integer> it : list) {
                // Add variable type.
                data.setUInt8(VariableAccessSpecification.VARIABLE_NAME);
                int sn = GXCommon.intValue(it.getKey().getShortName());
                sn += (it.getValue() - 1) * 8;
                data.setUInt16(sn);
            }
            messages.addAll(GXDLMS.getSnMessages(p));
        }
        return messages.toArray(new byte[0][0]);
    }

    /**
     * Generates the keep alive message. Keep alive message is sent to keep the
     * connection to the device alive.
     * 
     * @return Returns Keep alive message, as byte array.
     */
    public final byte[] keepAlive() {
        // There is no need for keep alive in IEC 62056-47.
        if (this.getInterfaceType() == InterfaceType.WRAPPER) {
            return new byte[0];
        }
        return GXDLMS.getHdlcFrame(settings, settings.getReceiverReady(), null);
    }

    /**
     * Read rows by entry.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param index
     *            Zero bases start index.
     * @param count
     *            Rows count to read.
     * @return Read message as byte array.
     */
    public final byte[][] readRowsByEntry(final GXDLMSProfileGeneric pg,
            final int index, final int count) {
        return readRowsByEntry(pg, index, count, null);
    }

    /**
     * Read rows by entry.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param index
     *            One based start index.
     * @param count
     *            Rows count to read.
     * @param columns
     *            Columns to read.
     * @return Read message as byte array.
     */
    public final byte[][] readRowsByEntry(final GXDLMSProfileGeneric pg,
            final int index, final int count,
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns) {
        GXByteBuffer buff = new GXByteBuffer(19);
        // Add AccessSelector value
        buff.setUInt8(0x02);
        // Add enum tag.
        buff.setUInt8(DataType.STRUCTURE.getValue());
        // Add item count
        buff.setUInt8(0x04);
        // Add start index
        GXCommon.setData(buff, DataType.UINT32, index);
        // Add Count
        GXCommon.setData(buff, DataType.UINT32, count);

        int columnIndex = 1;
        int columnCount = 0;
        int pos = 0;
        // If columns are given find indexes.
        if (columns != null && columns.size() != 0) {
            if (pg.getCaptureObjects() == null
                    || pg.getCaptureObjects().size() == 0) {
                throw new RuntimeException("Read capture objects first.");
            }
            columnIndex = pg.getCaptureObjects().size();
            columnCount = 1;
            for (Entry<GXDLMSObject, GXDLMSCaptureObject> c : columns) {
                pos = 0;
                boolean found = false;
                for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pg
                        .getCaptureObjects()) {
                    ++pos;
                    if (it.getKey().getObjectType() == c.getKey()
                            .getObjectType()
                            && it.getKey().getLogicalName()
                                    .compareTo(c.getKey().getLogicalName()) == 0
                            && it.getValue().getAttributeIndex() == c.getValue()
                                    .getAttributeIndex()
                            && it.getValue().getDataIndex() == c.getValue()
                                    .getDataIndex()) {
                        found = true;
                        if (pos < columnIndex) {
                            columnIndex = pos;
                        }
                        columnCount = pos - columnIndex + 1;
                        break;
                    }
                }
                if (!found) {
                    throw new RuntimeException(
                            "Invalid column: " + c.getKey().getLogicalName());
                }
            }
        }

        // Select columns to read.
        GXCommon.setData(buff, DataType.UINT16, columnIndex);
        GXCommon.setData(buff, DataType.UINT16, columnCount);
        return read(pg.getName(), ObjectType.PROFILE_GENERIC, 2, buff);
    }

    /**
     * Read rows by range. Use this method to read Profile Generic table between
     * dates.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param start
     *            Start time.
     * @param end
     *            End time.
     * @return Generated read message.
     */
    public final byte[][] readRowsByRange(final GXDLMSProfileGeneric pg,
            final GXDateTime start, final GXDateTime end) {
        return readByRange(pg, start, end, null);
    }

    /**
     * Read rows by range. Use this method to read Profile Generic table between
     * dates.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param start
     *            Start time.
     * @param end
     *            End time.
     * @return Generated read message.
     */
    public final byte[][] readRowsByRange(final GXDLMSProfileGeneric pg,
            final java.util.Date start, final java.util.Date end) {
        return readByRange(pg, start, end, null);
    }

    /**
     * Read rows by range. Use this method to read Profile Generic table between
     * dates.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param start
     *            Start time.
     * @param end
     *            End time.
     * @param columns
     *            Columns to read.
     * @return Generated read message.
     */
    public final byte[][] readRowsByRange(final GXDLMSProfileGeneric pg,
            final java.util.Date start, final java.util.Date end,
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns) {
        return readByRange(pg, start, end, columns);
    }

    /**
     * Read rows by range. Use this method to read Profile Generic table between
     * dates.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param start
     *            Start time.
     * @param end
     *            End time.
     * @return Generated read message.
     */
    public final byte[][] readRowsByRange(final GXDLMSProfileGeneric pg,
            final Calendar start, final Calendar end) {
        return readByRange(pg, start, end, null);
    }

    /**
     * Read rows by range. Use this method to read Profile Generic table between
     * dates.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param start
     *            Start time.
     * @param end
     *            End time.
     * @param columns
     *            Columns to read.
     * @return Generated read message.
     */
    public final byte[][] readRowsByRange(final GXDLMSProfileGeneric pg,
            final Calendar start, final Calendar end,
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns) {
        return readByRange(pg, start, end, columns);
    }

    /**
     * Read rows by range. Use this method to read Profile Generic table between
     * dates.
     * 
     * @param pg
     *            Profile generic object to read.
     * @param start
     *            Start time.
     * @param end
     *            End time.
     * @return Generated read message.
     */
    private byte[][] readByRange(final GXDLMSProfileGeneric pg,
            final Object start, final Object end,
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns) {
        settings.resetBlockIndex();
        GXDateTime s = GXCommon.getDateTime(start);
        GXDateTime e = GXCommon.getDateTime(end);
        s.getSkip().add(DateTimeSkips.DAY_OF_WEEK);
        e.getSkip().add(DateTimeSkips.DAY_OF_WEEK);
        GXDLMSObject sort = pg.getSortObject();
        if (sort == null && pg.getCaptureObjects().size() != 0) {
            sort = pg.getCaptureObjects().get(0).getKey();
        }
        // If sort object is not found or it is not clock object read all.
        if (sort == null || sort.getObjectType() != ObjectType.CLOCK) {
            return read(pg, 2);
        }
        GXByteBuffer buff = new GXByteBuffer(51);
        // Add AccessSelector value.
        buff.setUInt8(0x01);
        // Add enum tag.
        buff.setUInt8(DataType.STRUCTURE.getValue());
        // Add item count
        buff.setUInt8(0x04);
        // Add enum tag.
        buff.setUInt8(DataType.STRUCTURE.getValue());
        // Add item count
        buff.setUInt8(0x04);
        // CI
        GXCommon.setData(buff, DataType.UINT16,
                sort.getObjectType().getValue());
        // LN
        GXCommon.setData(buff, DataType.OCTET_STRING,
                GXCommon.logicalNameToBytes(sort.getLogicalName()));
        // Add attribute index.
        GXCommon.setData(buff, DataType.INT8, 2);
        // Add version
        GXCommon.setData(buff, DataType.UINT16, sort.getVersion());
        // Add start time
        GXCommon.setData(buff, DataType.OCTET_STRING, s);
        // Add end time
        GXCommon.setData(buff, DataType.OCTET_STRING, e);
        // Add array of read columns.
        buff.setUInt8(DataType.ARRAY.getValue());
        if (columns == null) {
            // Add item count
            buff.setUInt8(0x00);
        } else {
            GXCommon.setObjectCount(columns.size(), buff);
            for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : columns) {
                buff.setUInt8(DataType.STRUCTURE.getValue());
                // Add items count.
                buff.setUInt8(4);
                // CI
                GXCommon.setData(buff, DataType.UINT16,
                        it.getKey().getObjectType().getValue());
                // LN
                GXCommon.setData(buff, DataType.OCTET_STRING, GXCommon
                        .logicalNameToBytes(it.getKey().getLogicalName()));
                // Add attribute index.
                GXCommon.setData(buff, DataType.INT8,
                        it.getValue().getAttributeIndex());
                // Add data index.
                GXCommon.setData(buff, DataType.INT16,
                        it.getValue().getDataIndex());
            }
        }
        return read(pg.getName(), ObjectType.PROFILE_GENERIC, 2, buff);
    }

    /**
     * Create object by object type.
     * 
     * @param type
     *            Object type.
     * @return Created object.
     */
    public static GXDLMSObject createObject(final ObjectType type) {
        return GXDLMS.createObject(type);
    }

    /**
     * Generates an acknowledgment message, with which the server is informed to
     * send next packets.
     * 
     * @param type
     *            Frame type
     * @return Acknowledgment message as byte array.
     */
    public final byte[] receiverReady(final RequestTypes type) {
        return GXDLMS.receiverReady(settings, type);
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
        return GXDLMS.getData(settings, new GXByteBuffer(reply), data);
    }

    /**
     * Removes the HDLC frame from the packet, and returns COSEM data only.
     * 
     * @param reply
     *            The received data from the device.
     * @param data
     *            The exported reply information.
     */
    public final void getData(final GXByteBuffer reply,
            final GXReplyData data) {
        GXDLMS.getData(settings, reply, data);
    }

    /**
     * Converts meter serial number to server address. Default formula is used.
     * All meters do not use standard formula or support serial number
     * addressing at all.
     * 
     * @param serialNumber
     *            Meter serial number
     * @return Server address.
     */
    public static int getServerAddress(final int serialNumber) {
        return getServerAddress(serialNumber, null);
    }

    /**
     * Converts meter serial number to server address. Default formula is used.
     * All meters do not use standard formula or support serial number
     * addressing at all.
     * 
     * @param serialNumber
     *            Meter serial number
     * @param formula
     *            Formula used to convert serial number to server address.
     * @return Server address.
     */

    public static int getServerAddress(final int serialNumber,
            final String formula) {
        // If formula is not given use default formula.
        // This formula is defined in DLMS specification.
        if (formula == null || formula.length() == 0) {
            return 0x4000 | SerialNumberCounter.count(serialNumber,
                    "SN % 10000 + 1000");
        }
        return 0x4000 | SerialNumberCounter.count(serialNumber, formula);
    }

    /**
     * Convert physical address and logical address to server address.
     * 
     * @param logicalAddress
     *            Server logical address.
     * @param physicalAddress
     *            Server physical address.
     * @return Server address.
     */
    public static int getServerAddress(final int logicalAddress,
            final int physicalAddress) {
        return getServerAddress(logicalAddress, physicalAddress, 0);
    }

    /**
     * Convert physical address and logical address to server address.
     * 
     * @param logicalAddress
     *            Server logical address.
     * @param physicalAddress
     *            Server physical address.
     * @param addressSize
     *            Address size in bytes.
     * @return Server address.
     */
    public static int getServerAddress(final int logicalAddress,
            final int physicalAddress, final int addressSize) {
        if (addressSize < 4 && physicalAddress < 0x80
                && logicalAddress < 0x80) {
            return logicalAddress << 7 | physicalAddress;
        }
        if (physicalAddress < 0x4000 && logicalAddress < 0x4000) {
            return logicalAddress << 14 | physicalAddress;
        }
        throw new IllegalArgumentException(
                "Invalid logical or physical address.");
    }

    /**
     * Generates a access service message.
     * 
     * @param time
     *            Send time. Set to DateTime.MinValue is not used.
     * @param list
     *            List of access items.
     * @return Read request as byte array. {@link parseAccessResponse}
     */
    public final byte[][] accessRequest(final Date time,
            final ArrayList<GXDLMSAccessItem> list) {
        GXByteBuffer bb = new GXByteBuffer();
        GXCommon.setObjectCount(list.size(), bb);
        for (GXDLMSAccessItem it : list) {
            bb.setUInt8(it.getCommand());
            bb.setUInt16(it.getTarget().getObjectType().getValue());
            // LN
            String[] items = it.getTarget().getLogicalName().split("[.]");
            if (items.length != 6) {
                throw new IllegalArgumentException("Invalid Logical Name.");
            }
            for (String it2 : items) {
                bb.setUInt8(Integer.valueOf(it2).byteValue());
            }
            // Attribute ID.
            bb.setUInt8(it.getIndex());
        }
        // Data
        GXCommon.setObjectCount(list.size(), bb);
        for (GXDLMSAccessItem it : list) {
            if (it.getCommand() == AccessServiceCommandType.GET) {
                bb.setUInt8(0);
            } else {
                Object value = ((IGXDLMSBase) it.getTarget()).getValue(settings,
                        new ValueEventArgs(it.getTarget(), it.getIndex(), 0,
                                null));
                DataType type = it.getTarget().getDataType(it.getIndex());
                if (type == DataType.NONE) {
                    type = GXDLMSConverter.getDLMSDataType(value);
                }
                GXCommon.setData(bb, type, value);
            }
        }
        GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                Command.ACCESS_REQUEST, 0xFF, null, bb, 0xff);
        if (time != null && time != new Date(0)) {
            p.setTime(new GXDateTime(time));
        }
        return GXDLMS.getLnMessages(p).toArray(new byte[0][0]);
    }

    /**
     * Parse access response.
     * 
     * @param list
     *            Collection of access items.
     * @param data
     *            Received data from the meter.
     * @return Collection of received data and status codes.
     *         {@link accessRequest}
     */
    public final List<Map.Entry<Object, ErrorCode>> parseAccessResponse(
            final List<GXDLMSAccessItem> list, final GXByteBuffer data) {
        int pos;
        // Get count
        GXDataInfo info = new GXDataInfo();
        int cnt = GXCommon.getObjectCount(data);
        if (list.size() != cnt) {
            throw new IllegalArgumentException(
                    "List size and values size do not match.");
        }
        List<Object> values = new ArrayList<Object>(cnt);
        List<Map.Entry<Object, ErrorCode>> reply =
                new ArrayList<Map.Entry<Object, ErrorCode>>(cnt);
        for (pos = 0; pos != cnt; ++pos) {
            info.clear();
            Object value = GXCommon.getData(data, info);
            values.add(value);
        }
        // Get status codes.
        cnt = GXCommon.getObjectCount(data);
        if (values.size() != cnt) {
            throw new IllegalArgumentException(
                    "List size and values size do not match.");
        }
        for (Object it : values) {
            // Get access type.
            data.getUInt8();
            // Get status.
            reply.add(new GXSimpleEntry<Object, ErrorCode>(it,
                    ErrorCode.forValue(data.getUInt8())));
        }
        pos = 0;
        for (GXDLMSAccessItem it : list) {
            if (it.getCommand() == AccessServiceCommandType.GET
                    && reply.get(pos).getValue() == ErrorCode.OK) {
                ValueEventArgs ve = new ValueEventArgs(settings, it.getTarget(),
                        it.getIndex(), 0, null);
                ve.setValue(values.get(pos));
                ((IGXDLMSBase) it.getTarget()).setValue(settings, ve);
            }
            ++pos;
        }
        return reply;
    }

    /**
     * Get initial Conformance.
     * 
     * @param useLogicalNameReferencing
     *            Is logical name referencing used.
     * @return Initial Conformance.
     */
    public static Set<Conformance>
            getInitialConformance(final boolean useLogicalNameReferencing) {
        Set<Conformance> list = new HashSet<Conformance>();
        if (useLogicalNameReferencing) {
            list.addAll(Arrays.asList(
                    new Conformance[] { Conformance.BLOCK_TRANSFER_WITH_ACTION,
                            Conformance.BLOCK_TRANSFER_WITH_SET_OR_WRITE,
                            Conformance.BLOCK_TRANSFER_WITH_GET_OR_READ,
                            Conformance.SET, Conformance.SELECTIVE_ACCESS,
                            Conformance.ACTION, Conformance.MULTIPLE_REFERENCES,
                            Conformance.GET }));
        } else {
            list.addAll(Arrays
                    .asList(new Conformance[] { Conformance.INFORMATION_REPORT,
                            Conformance.READ, Conformance.UN_CONFIRMED_WRITE,
                            Conformance.WRITE, Conformance.PARAMETERIZED_ACCESS,
                            Conformance.MULTIPLE_REFERENCES }));
        }
        return list;
    }

    /**
     * Returns collection of report objects.
     * 
     * @param reply
     *            Reply.
     * @param list
     *            Array of objects and called indexes.
     * @return Data notification data.
     * @throws Exception
     *             Occurred exception
     */
    public Object parseReport(final GXReplyData reply,
            final List<Entry<GXDLMSObject, Integer>> list) throws Exception {

        if (reply.getCommand() == Command.EVENT_NOTIFICATION) {
            GXDLMSLNCommandHandler.handleEventNotification(settings, reply,
                    list);
            return null;
        } else if (reply.getCommand() == Command.INFORMATION_REPORT) {
            GXDLMSSNCommandHandler.handleInformationReport(settings, reply,
                    list);
            return null;
        } else if (reply.getCommand() == Command.DATA_NOTIFICATION) {
            return reply.getValue();
        } else {
            throw new IllegalArgumentException(
                    "Invalid command. " + reply.getCommand());
        }
    }
}