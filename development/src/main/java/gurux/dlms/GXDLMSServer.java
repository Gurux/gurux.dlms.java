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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;

/**
 * GXDLMSServer implements methods to implement DLMS/COSEM meter/proxy.
 */
public abstract class GXDLMSServer {

    private final GXDLMSServerBase base;

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
        base = new GXDLMSServerBase(this, logicalNameReferencing, type);
    }

    /**
     * @return List of objects that meter supports.
     */
    public final GXDLMSObjectCollection getItems() {
        return base.getItems();
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
        return base.getSettings().getNegotiatedConformance();
    }

    /**
     * @param value
     *            What kind of services server is offering.
     */
    public final void setConformance(final java.util.Set<Conformance> value) {
        base.getSettings().setNegotiatedConformance(value);
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
     * Check is data sent to this server.
     * 
     * @param serverAddress
     *            Server address.
     * @param clientAddress
     *            Client address.
     * @return True, if data is sent to this server.
     */
    protected abstract boolean isTarget(int serverAddress, int clientAddress);

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
            Authentication authentication, byte[] password);

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

}