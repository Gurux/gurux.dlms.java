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
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSPushSetup;
import gurux.dlms.objects.GXDLMSTcpUdpSetup;

/**
 * GXDLMSServer implements methods to implement DLMS/COSEM meter/proxy.
 */
public abstract class GXDLMSServer2 {

    private final GXDLMSServerBase base;

    /**
     * Constructor for logical name referencing.
     * 
     * @param ln
     *            Association logical name.
     * @param type
     *            Interface type.
     */
    public GXDLMSServer2(final GXDLMSAssociationLogicalName ln,
            final InterfaceType type) {
        base = new GXDLMSServerBase(this, true, type);
        ln.getXDLMSContextInfo().setSettings(getSettings());
        base.getItems().add(ln);
    }

    /**
     * Constructor for short name referencing.
     * 
     * @param sn
     *            Association short name.
     * @param type
     *            Interface type.
     */
    public GXDLMSServer2(final GXDLMSAssociationShortName sn,
            final InterfaceType type) {
        base = new GXDLMSServerBase(this, false, type);
        base.getItems().add(sn);
    }

    /**
     * Constructor for logical name referencing.
     * 
     * @param ln
     *            Association logical name.
     * @param hdlc
     *            HDLC settings.
     */
    public GXDLMSServer2(final GXDLMSAssociationLogicalName ln,
            final GXDLMSHdlcSetup hdlc) {
        base = new GXDLMSServerBase(this, true, InterfaceType.HDLC);
        base.setHdlc(hdlc);
        ln.getXDLMSContextInfo().setSettings(getSettings());
        base.getItems().add(ln);
        base.getItems().add(hdlc);
    }

    /**
     * Constructor for short name referencing.
     * 
     * @param sn
     *            Association short name.
     * @param hdlc
     *            HDLC settings.
     */
    public GXDLMSServer2(final GXDLMSAssociationShortName sn,
            final GXDLMSHdlcSetup hdlc) {
        base = new GXDLMSServerBase(this, false, InterfaceType.HDLC);
        getSettings().setHdlc(hdlc);
        base.getItems().add(sn);
        base.getItems().add(hdlc);
    }

    /**
     * Constructor for logical name referencing.
     * 
     * @param ln
     *            Association logical name.
     * @param wrapper
     *            WRAPPER settings.
     */
    public GXDLMSServer2(final GXDLMSAssociationLogicalName ln,
            final GXDLMSTcpUdpSetup wrapper) {
        base = new GXDLMSServerBase(this, true, InterfaceType.WRAPPER);
        ln.getXDLMSContextInfo().setSettings(getSettings());
        getSettings().setWrapper(wrapper);
        base.getItems().add(ln);
        base.getItems().add(wrapper);
    }

    /**
     * Constructor for short name referencing.
     * 
     * @param sn
     *            Association short name.
     * @param wrapper
     *            WRAPPER settings.
     */
    public GXDLMSServer2(final GXDLMSAssociationShortName sn,
            final GXDLMSTcpUdpSetup wrapper) {
        base = new GXDLMSServerBase(this, false, InterfaceType.WRAPPER);
        getSettings().setWrapper(wrapper);
        base.getItems().add(sn);
        base.getItems().add(wrapper);
    }

    /**
     * @return Server is using push client address when sending push messages.
     *         Client address is used if PushAddress is zero.
     */
    public int getPushClientAddress() {
        return getSettings().getPushClientAddress();
    }

    /**
     * @param value
     *            Server is using push client address when sending push
     *            messages. Client address is used if PushAddress is zero.
     */
    public void setPushClientAddress(final int value) {
        getSettings().setPushClientAddress(value);
    }

    /**
     * @return List of objects that meter supports.
     */
    public final GXDLMSObjectCollection getItems() {
        return base.getItems();
    }

    /**
     * @return HDLC settings.
     */
    public final GXDLMSHdlcSetup getHdlc() {
        return getSettings().getHdlc();
    }

    /**
     * @return Wrapper settings.
     */
    public final GXDLMSTcpUdpSetup getWrapper() {
        return getSettings().getWrapper();
    }

    /**
     * @return GBT window size.
     */
    public final int getWindowSize() {
        return getSettings().getWindowSize();
    }

    /**
     * @param value
     *            GBT window size.
     */
    public final void setWindowSize(final int value) {
        getSettings().setWindowSize((byte) value);
    }

    /**
     * @return Information from the connection size that server can handle.
     */
    public final GXDLMSLimits getLimits() {
        return base.getLimits();
    }

    /**
     * Retrieves the maximum size of received PDU. PDU size tells maximum size
     * of PDU packet. Value can be from 0 to 0xFFFF. By default the value is
     * 0xFFFF.
     * 
     * @return Maximum size of received PDU.
     */
    public final int getMaxReceivePDUSize() {
        return base.getMaxReceivePDUSize();
    }

    /**
     * @param value
     *            Maximum size of received PDU.
     */
    public final void setMaxReceivePDUSize(final int value) {
        base.setMaxReceivePDUSize(value);
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
        return base.getUseLogicalNameReferencing();
    }

    /**
     * @param value
     *            Is Logical Name referencing used.
     */
    public final void setUseLogicalNameReferencing(final boolean value) {
        base.setUseLogicalNameReferencing(value);
    }

    /**
     * @return Get settings.
     */
    public final GXDLMSSettings getSettings() {
        return base.getSettings();
    }

    /**
     * @param value
     *            Cipher interface that is used to cipher PDU.
     */
    protected final void setCipher(final GXICipher value) {
        base.setCipher(value);
    }

    /**
     * Initialize server. This must call after server objects are set.
     */
    public final void initialize() {
        base.initialize();
    }

    /**
     * Update short names.
     */
    public void updateShortNames() {
        base.updateShortNames(true);
    }

    /**
     * Close server.
     * 
     * @throws Exception
     *             Occurred exception.
     */
    public void close() throws Exception {
        base.close();
    }

    /**
     * Reset after connection is closed.
     */
    public final void reset() {
        base.reset(false);
    }

    /**
     * @return Client to Server challenge.
     */
    public final byte[] getCtoSChallenge() {
        return base.getCtoSChallenge();
    }

    /**
     * @return Server to Client challenge.
     */
    public final byte[] getStoCChallenge() {
        return base.getStoCChallenge();
    }

    /**
     * @return Interface type.
     */
    public final InterfaceType getInterfaceType() {
        return base.getInterfaceType();
    }

    /**
     * What kind of services server is offering.
     * 
     * @return Functionality.
     */
    public final java.util.Set<Conformance> getConformance() {
        return base.getSettings().getProposedConformance();
    }

    /**
     * @param value
     *            What kind of services server is offering.
     */
    public final void setConformance(final java.util.Set<Conformance> value) {
        base.getSettings().setProposedConformance(value);
    }

    /**
     * Server to Client custom challenge. This is for debugging purposes. Reset
     * custom challenge settings StoCChallenge to null.
     * 
     * @param value
     *            Server to Client challenge.
     */
    public final void setStoCChallenge(final byte[] value) {
        base.setStoCChallenge(value);
    }

    /**
     * Set starting packet index. Default is One based, but some meters use Zero
     * based value. Usually this is not used.
     * 
     * @param value
     *            Zero based starting index.
     */
    public final void setStartingPacketIndex(final int value) {
        base.setStartingPacketIndex(value);
    }

    /**
     * @return Invoke ID.
     */
    public final int getInvokeID() {
        return base.getInvokeID();
    }

    /**
     * @param value
     *            Invoke ID.
     */
    public final void setInvokeID(final int value) {
        base.setInvokeID(value);
    }

    /**
     * @return Used service class.
     */
    public final ServiceClass getServiceClass() {
        return base.getServiceClass();
    }

    /**
     * @param value
     *            Used service class.
     */
    public final void setServiceClass(final ServiceClass value) {
        base.setServiceClass(value);
    }

    /**
     * @return Used priority.
     */
    public final Priority getPriority() {
        return base.getPriority();
    }

    /**
     * @param value
     *            Used priority.
     */
    public final void setPriority(final Priority value) {
        base.setPriority(value);
    }

    /**
     * Handles client request.
     * 
     * @param buff
     *            Received data from the client.
     * @return Response to the request. Response is null if request packet is
     *         not complete.
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
     */
    public final byte[] handleRequest(final byte[] buff)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
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
     */
    public final byte[] handleRequest(final byte[] buff,
            final GXDLMSConnectionEventArgs connectionInfo)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXServerReply sr = new GXServerReply(buff);
        sr.setConnectionInfo(connectionInfo);
        base.handleRequest(sr);
        return sr.getReply();
    }

    /**
     * Handles client request.
     * 
     * @param sr
     *            Server reply.
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
     */
    public final void handleRequest(GXServerReply sr)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        base.handleRequest(sr);
    }

    /**
     * Check is data sent to this server.
     * 
     * @param serverAddress
     *            Server address.
     * @param clientAddress
     *            Client address.
     * @return True, if data is sent to this server.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract boolean isTarget(int serverAddress, int clientAddress)
            throws Exception;

    /**
     * Check whether the authentication and password are correct.
     * 
     * @param authentication
     *            Authentication level.
     * @param password
     *            Password.
     * @return Source diagnostic.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract SourceDiagnostic onValidateAuthentication(
            Authentication authentication, byte[] password) throws Exception;

    /**
     * Get selected value(s). This is called when example profile generic
     * request current value.
     * 
     * @param args
     *            Value event arguments.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    public abstract void onPreGet(ValueEventArgs[] args) throws Exception;

    /**
     * Get selected value(s). This is called when example profile generic
     * request current value.
     * 
     * @param args
     *            Value event arguments.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    public abstract void onPostGet(ValueEventArgs[] args) throws Exception;

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
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract GXDLMSObject onFindObject(ObjectType objectType, int sn,
            String ln) throws Exception;

    /**
     * Called before read is executed.
     * 
     * @param args
     *            Handled read requests.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    public abstract void onPreRead(ValueEventArgs[] args) throws Exception;

    /**
     * Called after read is executed.
     * 
     * @param args
     *            Handled read requests.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    public abstract void onPostRead(ValueEventArgs[] args) throws Exception;

    /**
     * Called before write is executed..
     * 
     * @param args
     *            Handled write requests.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract void onPreWrite(ValueEventArgs[] args) throws Exception;

    /**
     * Called after write is executed.
     * 
     * @param args
     *            Handled write requests.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract void onPostWrite(ValueEventArgs[] args) throws Exception;

    /**
     * Accepted connection is made for the server. All initialization is done
     * here.
     * 
     * @param connectionInfo
     *            Connection info.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract void onConnected(
            GXDLMSConnectionEventArgs connectionInfo) throws Exception;

    /**
     * Client has try to made invalid connection. Password is incorrect.
     * 
     * @param connectionInfo
     *            Connection info.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract void onInvalidConnection(
            GXDLMSConnectionEventArgs connectionInfo) throws Exception;

    /**
     * Server has close the connection. All clean up is made here.
     * 
     * @param connectionInfo
     *            Connection info.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract void onDisconnected(
            GXDLMSConnectionEventArgs connectionInfo) throws Exception;

    /**
     * Get attribute access mode.
     * 
     * @param arg
     *            Value event argument.
     * @return Access mode.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract AccessMode onGetAttributeAccess(ValueEventArgs arg)
            throws Exception;

    /**
     * Get method access mode.
     * 
     * @param arg
     *            Value event argument.
     * @return Method access mode.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract MethodAccessMode onGetMethodAccess(ValueEventArgs arg)
            throws Exception;

    /**
     * Called before action is executed.
     * 
     * @param args
     *            Handled action requests.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract void onPreAction(ValueEventArgs[] args) throws Exception;

    /**
     * Called after action is executed.
     * 
     * @param args
     *            Handled action requests.
     * @throws Exception
     *             Server handler occurred exceptions.
     */
    protected abstract void onPostAction(ValueEventArgs[] args)
            throws Exception;

    /**
     * Add value of COSEM object to byte buffer. AddData method can be used with
     * GetDataNotificationMessage -method. DLMS specification do not specify the
     * structure of Data-Notification body. So each manufacture can sent
     * different data.
     * 
     * @param obj
     *            COSEM object.
     * @param index
     *            Attribute index.
     * @param buff
     *            Byte buffer.
     */
    public final void addData(final GXDLMSObject obj, final int index,
            final GXByteBuffer buff) {
        DataType dt;
        ValueEventArgs e = new ValueEventArgs(obj, index, 0, null);
        Object value = obj.getValue(getSettings(), e);
        dt = obj.getDataType(index);
        if (dt == DataType.NONE && value != null) {
            dt = GXDLMSConverter.getDLMSDataType(value);
        }
        GXCommon.setData(base.getSettings(), buff, dt, value);
    }

    /**
     * Generates data notification message.
     * 
     * @param time
     *            Date time. Set Date(0) if not added.
     * @param data
     *            Notification body.
     * @return Generated data notification message(s).
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
     */
    public final byte[][] generateDataNotificationMessages(final Date time,
            final GXByteBuffer data)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        List<byte[]> reply;
        if (getUseLogicalNameReferencing()) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(getSettings(), 0,
                    Command.DATA_NOTIFICATION, 0, null, data, 0xff,
                    Command.NONE);
            if (time == null) {
                p.setTime(null);
            } else {
                p.setTime(new GXDateTime(time));
            }
            reply = GXDLMS.getLnMessages(p);
        } else {
            GXDLMSSNParameters p = new GXDLMSSNParameters(getSettings(),
                    Command.DATA_NOTIFICATION, 1, 0, data, null);
            reply = GXDLMS.getSnMessages(p);
        }
        if (!getSettings().getNegotiatedConformance()
                .contains(Conformance.GENERAL_BLOCK_TRANSFER)
                && reply.size() != 1) {
            throw new IllegalArgumentException(
                    "Data is not fit to one PDU. Use general block transfer.");
        }
        return reply.toArray(new byte[0][0]);
    }

    /**
     * Generates push setup message.
     * 
     * @param date
     *            Date time. Set to null or Date(0) if not used.
     * @param push
     *            Target Push object.
     * @return Generated data notification message(s).
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
     */
    public final byte[][] generatePushSetupMessages(final Date date,
            final GXDLMSPushSetup push)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        if (push == null) {
            throw new IllegalArgumentException("push");
        }
        GXByteBuffer buff = new GXByteBuffer();
        buff.setUInt8((byte) DataType.STRUCTURE.getValue());
        GXCommon.setObjectCount(push.getPushObjectList().size(), buff);
        for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : push
                .getPushObjectList()) {
            addData(it.getKey(), it.getValue().getAttributeIndex(), buff);
        }
        return generateDataNotificationMessages(date, buff);
    }

    /**
     * @return Client system title.
     *         <p>
     *         Client system title is optional and it's used when
     *         Pre-established Application Associations is used.
     */
    public byte[] getClientSystemTitle() {
        return getSettings().getPreEstablishedSystemTitle();
    }

    /**
     * @param value
     *            Client system title.
     */
    public void setClientSystemTitle(final byte[] value) {
        getSettings().setPreEstablishedSystemTitle(value);
    }
}
