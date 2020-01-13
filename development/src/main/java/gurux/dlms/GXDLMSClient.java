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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AccessServiceCommandType;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.enums.Standard;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.manufacturersettings.GXObisCodeCollection;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.IGXDLMSBase;
import gurux.dlms.secure.GXSecure;

/**
 * GXDLMS implements methods to communicate with DLMS/COSEM metering devices.
 */
public class GXDLMSClient {

    protected GXDLMSTranslator translator;

    /**
     * XML client don't throw exceptions. It serializes them as a default. Set
     * value to true, if exceptions are thrown.
     */
    private boolean throwExceptions;

    /**
     * DLMS settings.
     */
    protected final GXDLMSSettings settings = new GXDLMSSettings(false);
    private GXObisCodeCollection obisCodes;
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSClient.class.getName());

    /**
     * Is authentication required.
     */
    private boolean isAuthenticationRequired = false;

    /**
     * Auto increase Invoke ID.
     */
    private boolean autoIncreaseInvokeID = false;

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
     * User id is the identifier of the user. This value is used if user list on
     * Association LN is used.
     * 
     * @return User id.
     */
    public final int getUserId() {
        return settings.getUserId();
    }

    /**
     * User id is the identifier of the user. This value is used if user list on
     * Association LN is used.
     * 
     * @param value
     *            User id.
     */
    public final void setUserId(final int value) {
        if (value < -1 || value > 255) {
            throw new IllegalArgumentException("Invalid user Id.");
        }
        settings.setUserId(value);
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
     * Meter returns system title when ciphered connection is made or GMAC
     * authentication is used.
     * 
     * @return Source system title.
     */
    public byte[] getSourceSystemTitle() {
        return settings.getSourceSystemTitle();
    }

    /**
     * @return GBT window size.
     */
    public final int getWindowSize() {
        return settings.getWindowSize();
    }

    /**
     * @param value
     *            GBT window size.
     */
    public final void setWindowSize(final int value) {
        settings.setWindowSize((byte) value);
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
     * Standard says that Time zone is from normal time to UTC in minutes. If
     * meter is configured to use UTC time (UTC to normal time) set this to
     * true.
     * 
     * @return True, if UTC time is used.
     */
    public boolean getUseUtc2NormalTime() {
        return settings.getUseUtc2NormalTime();
    }

    /**
     * Standard says that Time zone is from normal time to UTC in minutes. If
     * meter is configured to use UTC time (UTC to normal time) set this to
     * true.
     * 
     * @param value
     *            True, if UTC time is used.
     */
    public void setUseUtc2NormalTime(final boolean value) {
        settings.setUseUtc2NormalTime(value);
    }

    /**
     * Used standard.
     * 
     * @return True, if UTC time is used.
     */
    public Standard getStandard() {
        return settings.getStandard();
    }

    /**
     * Used standard.
     * 
     * @param value
     *            True, if UTC time is used.
     */
    public void setStandard(final Standard value) {
        settings.setStandard(value);
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
     * @return Gateway settings.
     */
    public final GXDLMSGateway getGateway() {
        return settings.getGateway();
    }

    /**
     * @param value
     *            Gateway settings.
     */
    public final void setGateway(final GXDLMSGateway value) {
        settings.setGateway(value);
    }

    /**
     * @return Protocol version.
     */
    public final String getProtocolVersion() {
        return settings.getProtocolVersion();
    }

    /**
     * @param value
     *            Protocol version.
     */
    public final void setProtocolVersion(final String value) {
        settings.setProtocolVersion(value);
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
        settings.setConnected(ConnectionState.NONE);
        isAuthenticationRequired = false;
        // SNRM request is not used in network connections.
        if (this.getInterfaceType() == InterfaceType.WRAPPER) {
            return new byte[0];
        }
        GXByteBuffer data = new GXByteBuffer(25);
        data.setUInt8(0x81); // FromatID
        data.setUInt8(0x80); // GroupID
        data.setUInt8(0); // Length.
        int maxInfoTX = getLimits().getMaxInfoTX(),
                maxInfoRX = getLimits().getMaxInfoRX();
        if (getLimits().isUseFrameSize()) {
            byte[] primaryAddress, secondaryAddress;
            primaryAddress =
                    GXDLMS.getHdlcAddressBytes(settings.getServerAddress(),
                            settings.getServerAddressSize());
            secondaryAddress =
                    GXDLMS.getHdlcAddressBytes(settings.getClientAddress(), 0);
            maxInfoTX -= (10 + secondaryAddress.length);
            maxInfoRX -= (10 + primaryAddress.length);
        }

        // If custom HDLC parameters are used.
        if (GXDLMSLimits.DEFAULT_MAX_INFO_TX != getLimits().getMaxInfoTX()
                || GXDLMSLimits.DEFAULT_MAX_INFO_RX != getLimits()
                        .getMaxInfoRX()
                || GXDLMSLimits.DEFAULT_WINDOWS_SIZE_TX != getLimits()
                        .getWindowSizeTX()
                || GXDLMSLimits.DEFAULT_WINDOWS_SIZE_RX != getLimits()
                        .getWindowSizeRX()) {
            data.setUInt8(HDLCInfo.MAX_INFO_TX);
            GXDLMS.appendHdlcParameter(data, maxInfoTX);
            data.setUInt8(HDLCInfo.MAX_INFO_RX);
            GXDLMS.appendHdlcParameter(data, maxInfoRX);
            data.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
            data.setUInt8(4);
            data.setUInt32(getLimits().getWindowSizeTX());
            data.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
            data.setUInt8(4);
            data.setUInt32(getLimits().getWindowSizeRX());
        }
        // If default HDLC parameters are not used.
        if (data.size() != 3) {
            // Length.
            data.setUInt8(2, data.size() - 3);
        } else {
            data = null;
        }
        settings.resetFrameSequence();
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
        GXDLMS.parseSnrmUaResponse(new GXByteBuffer(data), settings);
        settings.setConnected(ConnectionState.HDLC);
    }

    /**
     * Parses UAResponse from byte array.
     * 
     * @param data
     *            Received message from the server.
     * @see GXDLMSClient#snrmRequest
     */
    public final void parseUAResponse(final GXByteBuffer data) {
        GXDLMS.parseSnrmUaResponse(data, settings);
    }

    /**
     * Generate AARQ request. Because all meters can't read all data in one
     * packet, the packet must be split first, by using SplitDataToPackets
     * method.
     * 
     * @return AARQ request as byte array.
     * @see GXDLMSClient#parseAareResponse
     */
    public final byte[][] aarqRequest()
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        settings.setConnected(settings.getConnected() & ~ConnectionState.DLMS);
        GXByteBuffer buff = new GXByteBuffer(20);
        settings.resetBlockIndex();
        GXDLMS.checkInit(settings);
        settings.setStoCChallenge(null);
        if (autoIncreaseInvokeID) {
            settings.setInvokeID(0);
        } else {
            settings.setInvokeID(1);
        }
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
                    Command.AARQ, 0, buff, null, 0xff, Command.NONE);
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
        isAuthenticationRequired =
                GXAPDU.parsePDU(settings, settings.getCipher(), reply,
                        null) == SourceDiagnostic.AUTHENTICATION_REQUIRED;
        if (settings.getDLMSVersion() != 6) {
            throw new IllegalArgumentException("Invalid DLMS version number.");
        }
        if (!isAuthenticationRequired) {
            settings.setConnected(
                    settings.getConnected() | ConnectionState.DLMS);
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
    public final byte[][] getApplicationAssociationRequest()
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, SignatureException {
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
    @SuppressWarnings("squid:S00112")
    public final void parseApplicationAssociationResponse(
            final GXByteBuffer reply) throws InvalidKeyException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, SignatureException {
        GXDataInfo info = new GXDataInfo();
        boolean equals = false;
        byte[] secret;
        long ic = 0;
        byte[] value = (byte[]) GXCommon.getData(settings, reply, info);
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
                    String str = "Invalid StoC:" + GXCommon.toHex(value, true)
                            + "-" + GXCommon.toHex(tmp, true);
                    LOGGER.log(Level.INFO, str);
                }
            }
        } else {
            LOGGER.log(Level.INFO, "Server did not accept CtoS.");
        }

        if (!equals) {
            throw new GXDLMSException(
                    "parseApplicationAssociationResponse failed. "
                            + " Server to Client do not match.");
        } else {
            settings.setConnected(
                    settings.getConnected() | ConnectionState.DLMS);
        }
    }

    /**
     * @return Release request, as byte array.
     */
    public byte[][] releaseRequest()
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        // If connection is not established, there is no need to send
        // release request.
        if ((settings.getConnected() & ConnectionState.DLMS) == 0) {
            return null;
        }
        GXByteBuffer buff = new GXByteBuffer();
        // Length.
        buff.setUInt8(0);
        buff.setUInt8(0x80);
        buff.setUInt8(01);
        buff.setUInt8(00);
        if (settings.getCipher() != null && settings.getCipher().isCiphered()) {
            settings.getCipher().setInvocationCounter(
                    settings.getCipher().getInvocationCounter() + 1);
        }
        GXAPDU.generateUserInformation(settings, settings.getCipher(), null,
                buff);
        buff.setUInt8(0, (byte) (buff.size() - 1));
        List<byte[]> reply;
        if (getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.RELEASE_REQUEST, 0, buff, null, 0xff, Command.NONE);
            reply = GXDLMS.getLnMessages(p);
        } else {
            reply = GXDLMS.getSnMessages(new GXDLMSSNParameters(settings,
                    Command.RELEASE_REQUEST, 0xFF, 0xFF, null, buff));
        }
        settings.setConnected(settings.getConnected() & ~ConnectionState.DLMS);
        return reply.toArray(new byte[][] {});
    }

    /**
     * Generates a disconnect request.
     * 
     * @return Disconnected request, as byte array.
     */
    public final byte[] disconnectRequest()
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return disconnectRequest(false);
    }

    /**
     * Generates a disconnect request.
     * 
     * @param force
     *            Is disconnect method called always.
     * @return Disconnected request, as byte array.
     */
    public final byte[] disconnectRequest(final boolean force)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        // If connection is not established, there is no need to send
        // DisconnectRequest.
        byte[] reply = null;
        if (force || settings.getConnected() != ConnectionState.NONE) {
            if (this.getInterfaceType() == InterfaceType.HDLC) {
                settings.setConnected(ConnectionState.NONE);
                reply = GXDLMS.getHdlcFrame(settings,
                        Command.DISCONNECT_REQUEST, null);
            } else if ((settings.getConnected() & ConnectionState.DLMS) != 0) {
                reply = releaseRequest()[0];
            }
        }
        settings.setMaxPduSize(0xFFFF);
        settings.setConnected(ConnectionState.NONE);
        settings.resetFrameSequence();
        return reply;
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
            List<?> objects = (List<?>) GXCommon.getData(settings, buff, info);
            if (objects.size() != 4) {
                throw new GXDLMSException("Invalid structure format.");
            }
            int classID = ((Number) (objects.get(1))).intValue() & 0xFFFF;
            int baseName = ((Number) (objects.get(0))).intValue() & 0xFFFF;
            if (baseName > 0) {
                GXDLMSObject comp = createDLMSObject(classID, objects.get(2),
                        baseName, objects.get(3), null);
                if (!onlyKnownObjects
                        || comp.getClass() != GXDLMSObject.class) {
                    items.add(comp);
                } else {
                    String str = "Unknown object : " + String.valueOf(classID)
                            + " " + String.valueOf(baseName);
                    LOGGER.log(Level.INFO, str);
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
        if (accessRights instanceof List<?>
                && ((List<?>) accessRights).size() == 2) {
            // access_rights: access_right
            List<?> access = (List<?>) accessRights;
            for (Object attributeAccess : (List<?>) access.get(0)) {
                int id = ((Number) ((List<?>) attributeAccess).get(0))
                        .intValue();
                // Kamstrup is returning -1 here.
                if (id > 0) {
                    int tmp = ((Number) ((List<?>) attributeAccess).get(1))
                            .intValue();
                    AccessMode mode = AccessMode.forValue(tmp);
                    obj.setAccess(id, mode);
                }
            }
            for (Object methodAccess : (List<?>) access.get(1)) {
                int id = ((Number) ((List<?>) methodAccess).get(0)).intValue();
                int tmp;
                // If version is 0
                if (((List<?>) methodAccess).get(1) instanceof Boolean) {
                    if ((Boolean) ((List<?>) methodAccess).get(1)) {
                        tmp = 1;
                    } else {
                        tmp = 0;
                    }
                } else {
                    // If version is 1.
                    tmp = ((Number) ((List<?>) methodAccess).get(1)).intValue();
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
            List<?> objects = (List<?>) GXCommon.getData(settings, buff, info);
            if (objects.size() != 4) {
                throw new GXDLMSException("Invalid structure format.");
            }
            int classID = ((Number) (objects.get(0))).intValue() & 0xFFFF;
            if (classID > 0) {
                GXDLMSObject comp = createDLMSObject(classID, objects.get(1), 0,
                        objects.get(2), objects.get(3));
                if (!onlyKnownObjects
                        || comp.getClass() != GXDLMSObject.class) {
                    items.add(comp);
                } else {
                    String str = "Unknown object : " + String.valueOf(classID)
                            + " "
                            + GXCommon.toLogicalName((byte[]) objects.get(2));
                    LOGGER.log(Level.INFO, str);
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
                val = changeType((byte[]) value, type,
                        settings.getUseUtc2NormalTime());
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
        return GXCommon.getData(null, data, info);
    }

    /**
     * Get Value from byte array received from the meter.
     * 
     * @param data
     *            Byte array received from the meter.
     * @param useUtc
     *            Standard says that Time zone is from normal time to UTC in
     *            minutes. If meter is configured to use UTC time (UTC to normal
     *            time) set this to true.
     * @return Received data.
     */
    public static Object getValue(final GXByteBuffer data,
            final boolean useUtc) {
        GXDataInfo info = new GXDataInfo();
        GXDLMSSettings settings = new GXDLMSSettings(false);
        settings.setUseUtc2NormalTime(useUtc);
        return GXCommon.getData(settings, data, info);
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
            final List<?> values) {
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
        return changeType(value, type, false);
    }

    /**
     * Changes byte array received from the meter to given type.
     * 
     * @param value
     *            Byte array received from the meter.
     * @param type
     *            Wanted type.
     * @param useUtc
     *            Standard says that Time zone is from normal time to UTC in
     *            minutes. If meter is configured to use UTC time (UTC to normal
     *            time) set this to true.
     * @return Value changed by type.
     */
    public static Object changeType(final byte[] value, final DataType type,
            final boolean useUtc) {
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

        GXDLMSSettings settings = new GXDLMSSettings(false);
        settings.setUseUtc2NormalTime(useUtc);
        GXDataInfo info = new GXDataInfo();
        info.setType(type);
        Object ret = GXCommon.getData(settings, new GXByteBuffer(value), info);
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
    public final byte[] getObjectsRequest()
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return getObjectsRequest(null);
    }

    /**
     * Reads the Association view from the device. This method is used to get
     * all objects in the device.
     * 
     * @param ln
     *            Logical name of Association view.
     * @return Read request, as byte array.
     */
    public final byte[] getObjectsRequest(final String ln)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        Object name;
        settings.resetBlockIndex();
        if (getUseLogicalNameReferencing()) {
            if (ln == null || ln.isEmpty()) {
                name = "0.0.40.0.0.255";
            } else {
                name = ln;
            }
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
            final Object data, final DataType type)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final int methodIndex, final Object value, final DataType dataType)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        if (name == null || methodIndex < 1) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        settings.resetBlockIndex();
        if (autoIncreaseInvokeID) {
            settings.setInvokeID((byte) ((settings.getInvokeID() + 1) & 0xF));
        }
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
        GXCommon.setData(settings, data, type, value);

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
                    attributeDescriptor, data, 0xff, Command.NONE);
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
    public final byte[][] write(final GXDLMSObject item, final int index)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final int index)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        if (index < 1) {
            throw new GXDLMSException("Invalid parameter");
        }
        settings.resetBlockIndex();
        if (autoIncreaseInvokeID) {
            settings.setInvokeID((byte) ((settings.getInvokeID() + 1) & 0xF));
        }
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
        GXCommon.setData(settings, data, type, value);
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
                    attributeDescriptor, data, 0xff, Command.NONE);
            p.blockIndex = settings.getBlockIndex();
            p.blockNumberAck = settings.getBlockNumberAck();
            p.streaming = false;
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
    public final byte[][] writeList(final List<GXWriteItem> list)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
                bb.set(GXCommon
                        .logicalNameToBytes(it.getTarget().getLogicalName()));
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
        GXCommon.setObjectCount(list.size(), bb);
        for (GXWriteItem it : list) {
            ValueEventArgs e = new ValueEventArgs(settings, it.getTarget(),
                    it.getIndex(), it.getSelector(), it.getParameters());
            value = it.getTarget().getValue(settings, e);
            DataType type = it.getDataType();
            if ((type == null || type == DataType.NONE) && value != null) {
                type = it.getTarget().getDataType(it.getIndex());
                if (type == DataType.NONE) {
                    type = GXDLMSConverter.getDLMSDataType(value);
                    if (type == DataType.NONE) {
                        throw new GXDLMSException("Invalid parameter. "
                                + " In java value type must give.");
                    }
                }
            }
            GXCommon.setData(settings, data, type, value);
        }
        if (this.getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.SET_REQUEST, SetRequestType.WITH_LIST, bb, data,
                    0xff, Command.NONE);
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
            final int attributeOrdinal)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final int attributeOrdinal, final GXByteBuffer data)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        if ((attributeOrdinal < 1)) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        GXByteBuffer attributeDescriptor = new GXByteBuffer();
        List<byte[]> reply;
        settings.resetBlockIndex();
        if (autoIncreaseInvokeID) {
            settings.setInvokeID((byte) ((settings.getInvokeID() + 1) & 0xF));
        }
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
                    attributeDescriptor, data, 0xFF, Command.NONE);
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
            final int attributeOrdinal)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            readList(final List<Entry<GXDLMSObject, Integer>> list)
                    throws InvalidKeyException, NoSuchAlgorithmException,
                    NoSuchPaddingException, InvalidAlgorithmParameterException,
                    IllegalBlockSizeException, BadPaddingException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (!getNegotiatedConformance()
                .contains(Conformance.MULTIPLE_REFERENCES)) {
            throw new IllegalArgumentException(
                    "Meter doesn't support multiple objects reading with one request.");
        }
        List<byte[]> messages = new ArrayList<byte[]>();
        GXByteBuffer data = new GXByteBuffer();
        settings.resetBlockIndex();
        if (this.getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                    Command.GET_REQUEST, GetCommandType.WITH_LIST, data, null,
                    0xff, Command.NONE);
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
            final int index, final int count)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        int pos = 0;
        int columnStart = 1, columnEnd = 0;
        // If columns are given find indexes.
        if (columns != null && !columns.isEmpty()) {
            if (pg.getCaptureObjects() == null
                    || pg.getCaptureObjects().isEmpty()) {
                throw new IllegalArgumentException(
                        "Read capture objects first.");
            }
            columnStart = pg.getCaptureObjects().size();
            columnEnd = 1;
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
                        if (pos < columnStart) {
                            columnStart = pos;
                        }
                        columnEnd = pos;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalArgumentException(
                            "Invalid column: " + c.getKey().getLogicalName());
                }
            }
        }
        return readRowsByEntry(pg, index, count, columnStart, columnEnd);
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
     * @param columnStart
     *            One based column start index.
     * @param columnEnd
     *            Column end index.
     * @return Read message as byte array.
     */
    public final byte[][] readRowsByEntry(final GXDLMSProfileGeneric pg,
            final int index, final int count, final int columnStart,
            final int columnEnd)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        if (index < 0) {
            throw new IllegalArgumentException("index");
        }
        if (count < 0) {
            throw new IllegalArgumentException("count");
        }
        if (columnStart < 1) {
            throw new IllegalArgumentException("columnStart");
        }
        if (columnEnd < 0) {
            throw new IllegalArgumentException("columnEnd");
        }
        GXByteBuffer buff = new GXByteBuffer(19);
        // Add AccessSelector value
        buff.setUInt8(0x02);
        // Add enum tag.
        buff.setUInt8(DataType.STRUCTURE.getValue());
        // Add item count
        buff.setUInt8(0x04);
        // Add start index
        GXCommon.setData(settings, buff, DataType.UINT32, index);
        // Add Count
        if (count == 0) {
            GXCommon.setData(settings, buff, DataType.UINT32, count);
        } else {
            GXCommon.setData(settings, buff, DataType.UINT32,
                    index + count - 1);
        }
        // Select columns to read.
        GXCommon.setData(settings, buff, DataType.UINT16, columnStart);
        GXCommon.setData(settings, buff, DataType.UINT16, columnEnd);
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
            final GXDateTime start, final GXDateTime end)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final java.util.Date start, final java.util.Date end)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final Calendar start, final Calendar end)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
            final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> columns)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        settings.resetBlockIndex();
        GXDateTime s = GXCommon.getDateTime(start);
        GXDateTime e = GXCommon.getDateTime(end);
        GXDLMSObject sort = pg.getSortObject();
        if (sort == null && !pg.getCaptureObjects().isEmpty()) {
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
        GXCommon.setData(settings, buff, DataType.UINT16,
                sort.getObjectType().getValue());
        // LN
        GXCommon.setData(settings, buff, DataType.OCTET_STRING,
                GXCommon.logicalNameToBytes(sort.getLogicalName()));
        // Add attribute index.
        GXCommon.setData(settings, buff, DataType.INT8, 2);
        // Add version
        GXCommon.setData(settings, buff, DataType.UINT16, sort.getVersion());
        // If Unix time is used.
        if (!pg.getCaptureObjects().isEmpty()
                && pg.getCaptureObjects().get(0).getKey() instanceof GXDLMSData
                && pg.getCaptureObjects().get(0).getKey().getLogicalName()
                        .equals("0.0.1.1.0.255")) {
            // Add start time
            GXCommon.setData(settings, buff, DataType.UINT32,
                    GXDateTime.toUnixTime(s));
            // Add end time
            GXCommon.setData(settings, buff, DataType.UINT32,
                    GXDateTime.toUnixTime(e));
        } else {
            // Add start time
            GXCommon.setData(settings, buff, DataType.OCTET_STRING, s);
            // Add end time
            GXCommon.setData(settings, buff, DataType.OCTET_STRING, e);
        }

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
                GXCommon.setData(settings, buff, DataType.UINT16,
                        it.getKey().getObjectType().getValue());
                // LN
                GXCommon.setData(settings, buff, DataType.OCTET_STRING, GXCommon
                        .logicalNameToBytes(it.getKey().getLogicalName()));
                // Add attribute index.
                GXCommon.setData(settings, buff, DataType.INT8,
                        it.getValue().getAttributeIndex());
                // Add data index.
                GXCommon.setData(settings, buff, DataType.INT16,
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
     * Generates an acknowledgment message, with which the server is informed to
     * send next packets.
     * 
     * @param type
     *            Frame type
     * @return Acknowledgment message as byte array.
     */
    public final byte[] receiverReady(final java.util.Set<RequestTypes> type) {
        return GXDLMS.receiverReady(settings, type);
    }

    /**
     * Generates an acknowledgment message, with which the server is informed to
     * send next packets.
     * 
     * @param reply
     *            Received data.
     * @return Acknowledgment message as byte array.
     */
    public final byte[] receiverReady(final GXReplyData reply) {
        try {
            return GXDLMS.receiverReady(settings, reply);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
    public final boolean getData(final byte[] reply, final GXReplyData data)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return getData(new GXByteBuffer(reply), data, null);
    }

    /**
     * Removes the HDLC frame from the packet, and returns COSEM data only.
     * 
     * @param reply
     *            The received data from the device.
     * @param data
     *            Information from the received data.
     * @param notify
     *            Information from the notify message.
     * @return Is frame complete.
     */
    public final boolean getData(final byte[] reply, final GXReplyData data,
            final GXReplyData notify)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return getData(new GXByteBuffer(reply), data, notify);
    }

    /**
     * Removes the HDLC frame from the packet, and returns COSEM data only.
     * 
     * @param reply
     *            The received data from the device.
     * @param data
     *            The exported reply information.
     * @return Is frame complete.
     */
    public final boolean getData(final GXByteBuffer reply,
            final GXReplyData data)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return getData(reply, data, null);
    }

    /**
     * Removes the HDLC frame from the packet, and returns COSEM data only.
     * 
     * @param reply
     *            The received data from the device.
     * @param data
     *            The exported reply information.
     * @param notify
     *            Information from the notify message.
     * @return Is frame complete.
     */
    public final boolean getData(final GXByteBuffer reply,
            final GXReplyData data, final GXReplyData notify)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        data.setXml(null);
        boolean ret = false;
        try {
            ret = GXDLMS.getData(settings, reply, data, notify);
        } catch (Exception ex) {
            if (translator == null || throwExceptions) {
                throw ex;
            }
            ret = true;
        }
        if (ret && translator != null && data.getMoreData().isEmpty()) {
            if (data.getXml() == null) {
                data.setXml(new GXDLMSTranslatorStructure(
                        translator.getOutputType(),
                        translator.isOmitXmlNameSpace(), translator.isHex(),
                        translator.getShowStringAsHex(),
                        translator.isComments(), translator.tags));
            }
            int pos = data.getData().position();
            try {
                GXByteBuffer data2 = data.getData();
                if (data.getCommand() == Command.GET_RESPONSE) {
                    GXByteBuffer tmp =
                            new GXByteBuffer((4 + data.getData().size()));
                    tmp.setUInt8(data.getCommand());
                    tmp.setUInt8(GetCommandType.NORMAL);
                    tmp.setUInt8((byte) data.getInvokeId());
                    tmp.setUInt8(0);
                    tmp.set(data.getData());
                    data.setData(tmp);
                } else if (data.getCommand() == Command.METHOD_RESPONSE) {
                    GXByteBuffer tmp =
                            new GXByteBuffer((6 + data.getData().size()));
                    tmp.setUInt8(data.getCommand());
                    tmp.setUInt8(GetCommandType.NORMAL);
                    tmp.setUInt8((byte) data.getInvokeId());
                    tmp.setUInt8(0);
                    tmp.setUInt8(1);
                    tmp.setUInt8(0);
                    tmp.set(data.getData());
                    data.setData(tmp);
                } else if (data.getCommand() == Command.READ_RESPONSE) {
                    GXByteBuffer tmp =
                            new GXByteBuffer(3 + data.getData().size());
                    tmp.setUInt8(data.getCommand());
                    tmp.setUInt8(VariableAccessSpecification.VARIABLE_NAME);
                    tmp.setUInt8((byte) data.getInvokeId());
                    tmp.setUInt8(0);
                    tmp.set(data.getData());
                    data.setData(tmp);
                }
                data.getData().position(0);
                if (data.getCommand() == Command.SNRM
                        || data.getCommand() == Command.UA) {
                    data.getXml().appendStartTag(data.getCommand());
                    if (data.getData().size() != 0) {
                        translator.pduToXml(data.getXml(), data.getData(),
                                translator.isOmitXmlDeclaration(),
                                translator.isOmitXmlNameSpace(), true);
                    }
                    data.getXml().appendEndTag(data.getCommand());
                } else {
                    if (data.getData().size() != 0) {
                        translator.pduToXml(data.getXml(), data.getData(),
                                translator.isOmitXmlDeclaration(),
                                translator.isOmitXmlNameSpace(), true);
                    }
                    data.setData(data2);
                }
            } finally {
                data.getData().position(pos);
            }
        }
        return ret;
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
            final List<GXDLMSAccessItem> list)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
                GXCommon.setData(settings, bb, type, value);
            }
        }
        GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                Command.ACCESS_REQUEST, 0xFF, null, bb, 0xff, Command.NONE);
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
            Object value = GXCommon.getData(settings, data, info);
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
            list.addAll(Arrays.asList(Conformance.BLOCK_TRANSFER_WITH_ACTION,
                    Conformance.BLOCK_TRANSFER_WITH_SET_OR_WRITE,
                    Conformance.BLOCK_TRANSFER_WITH_GET_OR_READ,
                    Conformance.SET, Conformance.SELECTIVE_ACCESS,
                    Conformance.ACTION, Conformance.MULTIPLE_REFERENCES,
                    Conformance.GET));
        } else {
            list.addAll(Arrays.asList(Conformance.INFORMATION_REPORT,
                    Conformance.READ, Conformance.UN_CONFIRMED_WRITE,
                    Conformance.WRITE, Conformance.PARAMETERIZED_ACCESS,
                    Conformance.MULTIPLE_REFERENCES));
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

    /**
     * Returns collection of push objects.
     * 
     * @param data
     *            Received value.
     * @return Array of objects and called indexes.
     */
    public final List<Entry<GXDLMSObject, Integer>>
            parsePushObjects(final List<?> data) {
        List<Entry<GXDLMSObject, Integer>> objects =
                new ArrayList<Entry<GXDLMSObject, Integer>>();
        if (data != null) {
            GXDLMSConverter c = new GXDLMSConverter(getStandard());
            for (Object it : data) {
                List<?> tmp = (List<?>) it;
                int classID = ((Number) (tmp.get(0))).intValue() & 0xFFFF;
                if (classID > 0) {
                    GXDLMSObject comp;
                    comp = getObjects().findByLN(ObjectType.forValue(classID),
                            GXCommon.toLogicalName((byte[]) tmp.get(1)));
                    if (comp == null) {
                        comp = GXDLMSClient.createDLMSObject(classID, 0, 0,
                                tmp.get(1), null);
                        settings.getObjects().add(comp);
                        c.updateOBISCodeInformation(comp);
                    }
                    if (comp.getClass() != GXDLMSObject.class) {
                        objects.add(new GXSimpleEntry<GXDLMSObject, Integer>(
                                comp, ((Number) tmp.get(2)).intValue()));
                    } else {
                        String str = "Unknown object: "
                                + String.valueOf(classID) + " "
                                + GXCommon.toLogicalName((byte[]) tmp.get(1));
                        LOGGER.log(Level.INFO, str);
                    }
                }
            }
        }
        return objects;
    }

    /**
     * Get size of the frame.
     * <p>
     * When WRAPPER is used this method can be used to check how many bytes we
     * need to read.
     * 
     * @param data
     *            Received data.
     * @return Size of received bytes on the frame.
     */
    public final int getFrameSize(final GXByteBuffer data) {
        if (getInterfaceType() == InterfaceType.WRAPPER) {
            if (data.available() < 8 || data.getUInt16(data.position()) != 1) {
                return 8 - data.available();
            }
            return data.getUInt16(data.position() + 6);
        }
        return 1;
    }

    /**
     * @return XML client don't throw exceptions. It serializes them as a
     *         default. Set value to true, if exceptions are thrown.
     */
    boolean isThrowExceptions() {
        return throwExceptions;
    }

    /**
     * @param value
     *            XML client don't throw exceptions. It serializes them as a
     *            default. Set value to true, if exceptions are thrown.
     */
    void setThrowExceptions(final boolean value) {
        throwExceptions = value;
    }

    /**
     * Get HDLC sender and receiver address information.
     * 
     * @param reply
     *            Received data.
     * @param target
     *            target (primary) address
     * @param source
     *            Source (secondary) address.
     * @param type
     *            DLMS frame type.
     */
    public static void getHdlcAddressInfo(final GXByteBuffer reply,
            final int[] target, final int[] source, final short[] type) {
        GXDLMS.getHdlcAddressInfo(reply, target, source, type);
    }
}