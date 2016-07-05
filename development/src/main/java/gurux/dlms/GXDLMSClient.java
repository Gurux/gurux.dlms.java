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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
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
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
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
        this.setUseLogicalNameReferencing(useLogicalNameReferencing);
        this.setClientAddress(clientAddress);
        this.setServerAddress(serverAddress);
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
     * DLMS version number. Gurux DLMS component supports DLMS version number 6.
     * 
     * @return DLMS version number.
     */
    public final byte getDLMSVersion() {
        return settings.getDLMSVersion();
    }

    /**
     * @param value
     *            DLMS version number.
     */
    public final void setDLMSVersion(final byte value) {
        settings.setDLMSVersion(value);
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
     * @return Logical Name settings.
     */
    public final GXDLMSLNSettings getLNSettings() {
        return settings.getLnSettings();
    }

    /**
     * @return Short Name settings.
     */
    public final GXDLMSSNSettings getSNSettings() {
        return settings.getSnSettings();
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
        settings.setMaxReceivePDUSize(0xFFFF);
        // SNRM request is not used in network connections.
        if (this.getInterfaceType() == InterfaceType.WRAPPER) {
            return new byte[0];
        }
        GXByteBuffer data = new GXByteBuffer(25);
        data.setUInt8(0x81); // FromatID
        data.setUInt8(0x80); // GroupID
        data.setUInt8(0); // Length.

        // If custom HDLC parameters are used.
        if (!GXDLMSLimits.DEFAULT_MAX_INFO_TX
                .equals(this.getLimits().getMaxInfoTX())) {
            data.setUInt8(HDLCInfo.MAX_INFO_TX);
            data.setUInt8(GXCommon.getSize(getLimits().getMaxInfoTX()));
            data.add(getLimits().getMaxInfoTX());
        }
        if (!GXDLMSLimits.DEFAULT_MAX_INFO_RX
                .equals(this.getLimits().getMaxInfoRX())) {
            data.setUInt8(HDLCInfo.MAX_INFO_RX);
            data.setUInt8(GXCommon.getSize(getLimits().getMaxInfoRX()));
            data.add(getLimits().getMaxInfoRX());
        }
        if (!GXDLMSLimits.DEFAULT_WINDOWS_SIZE_TX
                .equals(this.getLimits().getWindowSizeTX())) {
            data.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
            data.setUInt8(GXCommon.getSize(getLimits().getWindowSizeTX()));
            data.add(getLimits().getWindowSizeTX());
        }
        if (!GXDLMSLimits.DEFAULT_WINDOWS_SIZE_TX
                .equals(this.getLimits().getWindowSizeRX())) {
            data.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
            data.setUInt8(GXCommon.getSize(getLimits().getWindowSizeRX()));
            data.add(getLimits().getWindowSizeRX());
        }
        // If default HDLC parameters are not used.
        if (data.size() != 3) {
            data.setUInt8(2, data.position() - 3); // Length.
        } else {
            data = null;
        }
        return GXDLMS.getHdlcFrame(settings, (byte) Command.SNRM.getValue(),
                null);
    }

    /**
     * Parses UAResponse from byte array.
     * 
     * @param data
     *            Received message from the server.
     * @see GXDLMSClient#snrmRequest
     */
    public final void parseUAResponse(final byte[] data) {
        parseUAResponse(new GXByteBuffer(data));
    }

    /**
     * Parses UAResponse from byte array.
     * 
     * @param data
     *            Received message from the server.
     * @see GXDLMSClient#snrmRequest
     */
    public final void parseUAResponse(final GXByteBuffer data) {
        data.getUInt8(); // Skip FromatID
        data.getUInt8(); // Skip Group ID.
        data.getUInt8(); // Skip Group len
        Object val;
        while (data.position() < data.size()) {
            int id = data.getUInt8();
            short len = data.getUInt8();
            switch (len) {
            case 1:
                val = data.getUInt8();
                break;
            case 2:
                val = data.getUInt16();
                break;
            case 4:
                val = data.getUInt32();
                break;
            default:
                throw new GXDLMSException("Invalid Exception.");
            }
            switch (id) { // RX / TX are delivered from the partner's point of view => reversed to ours
            case HDLCInfo.MAX_INFO_RX:
                getLimits().setMaxInfoTX(val);
                break;
            case HDLCInfo.MAX_INFO_TX:
                getLimits().setMaxInfoRX(val);
                break;
            case HDLCInfo.WINDOW_SIZE_RX:
                getLimits().setWindowSizeTX(val);
                break;
            case HDLCInfo.WINDOW_SIZE_TX:
                getLimits().setWindowSizeRX(val);
                break;
            default:
                throw new GXDLMSException("Invalid UA response.");
            }
        }
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
        GXAPDU.generateAarq(settings, settings.getCipher(), buff);
        return GXDLMS.getMessages(settings, Command.AARQ, 0, buff, null);
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
     * @see GXDLMSClient#getLNSettings
     * @see GXDLMSClient#getSNSettings
     */
    public final void parseAareResponse(final GXByteBuffer reply) {
        settings.setConnected(true);
        isAuthenticationRequired =
                GXAPDU.parsePDU(settings, settings.getCipher(),
                        reply) == SourceDiagnostic.AUTHENTICATION_REQUIRED;
        if (getDLMSVersion() != 6) {
            throw new GXDLMSException("Invalid DLMS version number.");
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
        if (settings.getPassword() == null
                || settings.getPassword().length == 0) {
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
            ic = settings.getCipher().getFrameCounter();
        }
        byte[] challenge = GXSecure.secure(settings, settings.getCipher(), ic,
                settings.getStoCChallenge(), pw);
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.OCTET_STRING.getValue());
        GXCommon.setObjectCount(challenge.length, bb);
        bb.set(challenge);
        if (getUseLogicalNameReferencing()) {
            return method("0.0.40.0.0.255", ObjectType.ASSOCIATION_LOGICAL_NAME,
                    1, bb.array(), DataType.OCTET_STRING);
        }
        return method(0xFA00, ObjectType.ASSOCIATION_SHORT_NAME, 8, bb.array(),
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
                LOGGER.info("Invalid StoC:" + GXCommon.toHex(value) + "-"
                        + GXCommon.toHex(tmp));
            }
        } else {
            LOGGER.info("Server did not accept CtoS.");
        }

        if (!equals) {
            throw new GXDLMSException(
                    "parseApplicationAssociationResponse failed. "
                            + " Server to Client do not match.");
        }
    }

    /**
     * Generates a disconnect request.
     * 
     * @return Disconnected request, as byte array.
     */
    public final byte[] disconnectRequest() {
        // If connection is not established, there is no need to send
        // DisconnectRequest.
        if (settings.getSnSettings() == null
                && settings.getLnSettings() == null) {
            return new byte[0];
        }
        if (this.getInterfaceType() == InterfaceType.HDLC) {
            return GXDLMS.getHdlcFrame(settings, (byte) Command.DISC.getValue(),
                    null);
        }
        GXByteBuffer bb = new GXByteBuffer(2);
        bb.setUInt8(Command.DISCONNECT_REQUEST.getValue());
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
                    System.out.println(String.format("Unknown object : %d %d",
                            classID, baseName));
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
        obj.setLogicalName(GXDLMSObject.toLogicalName(logicalName));
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
                    System.out.println(String.format("Unknown object : %d %s",
                            classID,
                            GXDLMSObject.toLogicalName((byte[]) objects[2])));
                }
            }
        }
        return items;
    }

    /*
     * Get Value from byte array received from the meter.
     */
    public final Object updateValue(final GXDLMSObject target,
            final int attributeIndex, final Object value) {
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
        ValueEventArgs e =
                new ValueEventArgs(settings, target, attributeIndex, 0, null);
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
    public final Object getValue(final GXByteBuffer data) {
        GXDataInfo info = new GXDataInfo();
        return GXCommon.getData(data, info);
    }

    /**
     * Update list of values.
     * 
     * @param list
     *            read objects.
     * @param data
     *            Received reply from the meter.
     */
    public final void updateValues(
            final List<Entry<GXDLMSObject, Integer>> list,
            final GXByteBuffer data) {
        Object value;
        GXDataInfo info = new GXDataInfo();
        for (Entry<GXDLMSObject, Integer> it : list) {
            int ret = data.getUInt8();
            if (ret == 0) {
                value = GXCommon.getData(data, info);
                ValueEventArgs e = new ValueEventArgs(settings, it.getKey(),
                        it.getValue(), 0, null);
                e.setValue(value);
                it.getKey().setValue(settings, e);
                info.clear();
            } else {
                throw new GXDLMSException(ret);
            }
        }
    }

    /**
     * Update list of values.
     * 
     * @param list
     *            read objects.
     * @param reply
     *            Received reply from the meter.
     */
    public final void updateValues(
            final List<Entry<GXDLMSObject, Integer>> list, final byte[] reply) {
        updateValues(list, new GXByteBuffer(reply));
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
            return GXCommon.toHex(value);
        }
        if (value.length == 0
                && (type == DataType.STRING || type == DataType.OCTET_STRING)) {
            return "";
        }
        GXDataInfo info = new GXDataInfo();
        info.setType(type);
        Object ret = GXCommon.getData(new GXByteBuffer(value), info);
        if (!info.isCompleate()) {
            throw new OutOfMemoryError();
        }
        if (type == DataType.OCTET_STRING && ret instanceof byte[]) {
            String str;
            byte[] arr = (byte[]) ret;
            if (arr.length == 0) {
                str = "";
            } else {
                StringBuilder bcd = new StringBuilder(arr.length * 4);
                for (int it : arr) {
                    if (bcd.length() != 0) {
                        bcd.append(".");
                    }
                    bcd.append(String.format("%d", it & 0xFF));
                }
                str = bcd.toString();
            }
            return str;
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
            type = GXCommon.getValueType(value);
            if (type == DataType.NONE) {
                throw new GXDLMSException(
                        "Invalid parameter. In java value type must give.");
            }
        }
        GXByteBuffer bb = new GXByteBuffer();
        Command cmd;
        if (getUseLogicalNameReferencing()) {
            cmd = Command.METHOD_REQUEST;
            // CI
            bb.setUInt16(objectType.getValue());
            // Add LN
            List<String> items = GXCommon.split((String) name, '.');
            if (items.size() != 6) {
                throw new IllegalArgumentException("Invalid Logical Name.");
            }
            for (String it2 : items) {
                bb.setUInt8(Integer.valueOf(it2).byteValue());
            }
            // Attribute ID.
            bb.setUInt8(index);
            // Method Invocation Parameters is not used.
            if (type == DataType.NONE) {
                bb.setUInt8(0);
            } else {
                bb.setUInt8(1);
            }
        } else {
            int[] data = new int[1], count = new int[1];
            GXDLMS.getActionInfo(objectType, data, count);
            if (index > count[0]) {
                throw new IllegalArgumentException("methodIndex");
            }
            int sn = GXCommon.intValue(name);
            index = (data[0] + (index - 1) * 0x8);
            sn += index;
            cmd = Command.READ_REQUEST;
            // Add SN count.
            bb.setUInt8(1);
            // Add name length.
            bb.setUInt8(4);
            // Add name.
            bb.setUInt16(sn);
            // Method Invocation Parameters is not used.
            if (type == DataType.NONE) {
                bb.setUInt8(0);
            } else {
                bb.setUInt8(1);
            }
        }
        if ((value instanceof byte[])) {
            bb.set((byte[]) value);
        } else if (type != DataType.NONE) {
            GXCommon.setData(bb, type, value);
        }
        return GXDLMS.getMessages(settings, cmd, 1, bb, null);
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
            type = GXCommon.getValueType(value);
            if (type == DataType.NONE) {
                throw new GXDLMSException(
                        "Invalid parameter. In java value type must give.");
            }
        }
        GXByteBuffer bb = new GXByteBuffer();
        Command cmd;
        if (getUseLogicalNameReferencing()) {
            cmd = Command.SET_REQUEST;
            // Add CI.
            bb.setUInt16(objectType.getValue());
            // Add LN.
            List<String> items = GXCommon.split((String) name, '.');
            if (items.size() != 6) {
                throw new IllegalArgumentException("Invalid Logical Name.");
            }
            for (String it2 : items) {
                bb.setUInt8(Integer.valueOf(it2).byteValue());
            }
            // Attribute ID.
            bb.setUInt8(index);
            // Access selection is not used.
            bb.setUInt8(0);
        } else {
            cmd = Command.WRITE_REQUEST;
            // Add SN count.
            bb.setUInt8(1);
            // Add name length.
            bb.setUInt8(2);
            // Add name.
            int sn = GXCommon.intValue(name);
            sn += (index - 1) * 8;
            bb.setUInt16(sn);
            // Add data count.
            bb.setUInt8(1);
        }
        GXCommon.setData(bb, type, value);
        return GXDLMS.getMessages(settings, cmd, 1, bb, null);
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
        Command cmd;
        settings.resetBlockIndex();
        GXByteBuffer bb = new GXByteBuffer();
        if (this.getUseLogicalNameReferencing()) {
            cmd = Command.SET_REQUEST;
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
            cmd = Command.WRITE_REQUEST;
            // Add length.
            bb.setUInt8(list.size());
            for (GXWriteItem it : list) {
                // Add variable type.
                bb.setUInt8(2);
                int sn = GXCommon.intValue(it.getTarget().getShortName());
                sn += (it.getIndex() - 1) * 8;
                bb.setUInt16(sn);
            }
        }
        // Write values.
        bb.setUInt8(list.size());
        for (GXWriteItem it : list) {
            ValueEventArgs e = new ValueEventArgs(settings, it.getTarget(),
                    it.getIndex(), it.getSelector(), it.getParameters());
            value = it.getTarget().getValue(settings, e);
            if ((value instanceof byte[])) {
                bb.set((byte[]) value);
            } else {
                DataType type = it.getDataType();
                if (type == DataType.NONE && value != null) {
                    type = GXCommon.getValueType(value);
                    if (type == DataType.NONE) {
                        throw new GXDLMSException("Invalid parameter. "
                                + " In java value type must give.");
                    }
                }
                GXCommon.setData(bb, type, value);
            }
        }
        return GXDLMS.getMessages(settings, cmd, 4, bb, null);
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
        Command cmd;
        GXByteBuffer bb = new GXByteBuffer();
        settings.resetBlockIndex();
        if (this.getUseLogicalNameReferencing()) {
            cmd = Command.GET_REQUEST;
            // CI
            bb.setUInt16(objectType.getValue());
            // Add LN
            List<String> items = GXCommon.split((String) name, '.');
            if (items.size() != 6) {
                throw new IllegalArgumentException("Invalid Logical Name.");
            }
            for (String it2 : items) {
                bb.setUInt8(Integer.valueOf(it2).byteValue());
            }
            // Attribute ID.
            bb.setUInt8(attributeOrdinal);
            if (data == null || data.size() == 0) {
                // Access selection is not used.
                bb.setUInt8(0);
            } else {
                // Access selection is used.
                bb.setUInt8(1);
                // Add data.
                bb.set(data.getData(), 0, data.size());
            }
        } else {
            cmd = Command.READ_REQUEST;
            // Add length.
            bb.setUInt8(1);
            // Add Selector.
            if (data != null && data.size() != 0) {
                bb.setUInt8(4);
            } else {
                bb.setUInt8(2);
            }
            int sn = GXCommon.intValue(name);
            sn += (attributeOrdinal - 1) * 8;
            bb.setUInt16(sn);
            // Add data.
            if (data != null && data.size() != 0) {
                bb.set(data.getData(), 0, data.size());
            }
        }
        return GXDLMS.getMessages(settings, cmd, 1, bb, null);
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
        Command cmd;
        List<byte[]> messages = new ArrayList<byte[]>();
        GXByteBuffer bb = new GXByteBuffer();
        settings.resetBlockIndex();
        if (this.getUseLogicalNameReferencing()) {
            cmd = Command.GET_REQUEST;

            // Request service primitive shall always fit in a single APDU.
            int pos = 0, count = (settings.getMaxReceivePDUSize() - 12) / 10;
            if (list.size() < count) {
                count = list.size();
            }
            // All meters can handle 10 items.
            if (count > 10) {
                count = 10;
            }
            // Add length.
            GXCommon.setObjectCount(count, bb);
            for (Entry<GXDLMSObject, Integer> it : list) {
                // CI.
                bb.setUInt16(it.getKey().getObjectType().getValue());
                List<String> items =
                        GXCommon.split(it.getKey().getLogicalName(), '.');
                if (items.size() != 6) {
                    throw new IllegalArgumentException("Invalid Logical Name.");
                }
                for (String it2 : items) {
                    bb.setUInt8(Integer.valueOf(it2).byteValue());
                }
                // Attribute ID.
                bb.setUInt8(it.getValue());
                // Attribute selector is not used.
                bb.setUInt8(0);

                ++pos;
                if (pos % count == 0 && list.size() != pos) {
                    messages.addAll(Arrays.asList(
                            GXDLMS.getMessages(settings, cmd, 3, bb, null)));
                    bb.clear();
                    if (list.size() - pos < count) {
                        GXCommon.setObjectCount(list.size() - pos, bb);
                    } else {
                        GXCommon.setObjectCount(count, bb);
                    }
                }
            }
        } else {
            cmd = Command.READ_REQUEST;
            // Add length.
            bb.setUInt8(list.size());
            for (Entry<GXDLMSObject, Integer> it : list) {
                // Add variable type.
                bb.setUInt8(2);
                int sn = GXCommon.intValue(it.getKey().getShortName());
                sn += (it.getValue() - 1) * 8;
                bb.setUInt16(sn);
            }
        }

        messages.addAll(
                Arrays.asList(GXDLMS.getMessages(settings, cmd, 3, bb, null)));
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
        // Read all columns.
        if (this.getUseLogicalNameReferencing()) {
            GXCommon.setData(buff, DataType.UINT16, 1);
        } else {
            GXCommon.setData(buff, DataType.UINT16, 0);
        }
        GXCommon.setData(buff, DataType.UINT16, 0);
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
            final java.util.Date start, final java.util.Date end) {
        return readByRange(pg, start, end);
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
        return readByRange(pg, start, end);
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
            final Object start, final Object end) {
        settings.resetBlockIndex();
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
        GXCommon.setData(buff, DataType.OCTET_STRING, sort.getLogicalName());
        // Add attribute index.
        GXCommon.setData(buff, DataType.INT8, 2);
        // Add version
        GXCommon.setData(buff, DataType.UINT16, sort.getVersion());
        GXCommon.setData(buff, DataType.OCTET_STRING, start); // Add start time
        GXCommon.setData(buff, DataType.OCTET_STRING, end); // Add start time
        // Add array of read columns. Read All...
        buff.setUInt8(0x01);
        // Add item count
        buff.setUInt8(0x00);
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
}