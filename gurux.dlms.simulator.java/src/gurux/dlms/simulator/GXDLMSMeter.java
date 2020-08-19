//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: https://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.simulator;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.common.IGXMedia;
import gurux.common.IGXMediaListener;
import gurux.common.MediaStateEventArgs;
import gurux.common.PropertyChangedEventArgs;
import gurux.common.ReceiveEventArgs;
import gurux.common.TraceEventArgs;
import gurux.common.enums.TraceLevel;
import gurux.dlms.ConnectionState;
import gurux.dlms.GXDLMSConnectionEventArgs;
import gurux.dlms.GXServerReply;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSSapAssignment;
import gurux.dlms.secure.GXDLMSSecureServer2;

/**
 * All example servers are using same objects.
 */
public class GXDLMSMeter extends GXDLMSSecureServer2
        implements IGXMediaListener, gurux.net.IGXNetListener {

    // Are all meters using the same port.
    boolean Exclusive = false;
    String objectsFile;
    TraceLevel Trace = TraceLevel.INFO;
    private IGXMedia Media;
    // Serial number of the meter.
    int serialNumber = 0;

    static final Object fileLock = new Object();

    /**
     * Constructor
     * 
     * @param logicalNameReferencing
     *            Is logical name referencing used.
     * @param type
     *            Interface type.
     */
    public GXDLMSMeter(boolean logicalNameReferencing, InterfaceType type) {
        super(logicalNameReferencing, type);
    }

    public void initialize(IGXMedia media, TraceLevel trace, String path,
            int sn, boolean exclusive) throws Exception {
        // If pre-established connections are used.
        setClientSystemTitle("ABCDEFGH".getBytes());
        getCiphering().setSecurity(Security.AUTHENTICATION_ENCRYPTION);

        serialNumber = sn;
        objectsFile = path;
        Exclusive = exclusive;
        Media = media;
        Trace = trace;
        media.addListener(this);
        init();
    }

    /**
     * Update simulated values for the meter instance.
     * 
     * @param items
     *            Simulated COSEM objects. //String.format("onPostAction
     *            %1$s:%2$s",
     */
    void updateValues(GXDLMSObjectCollection items) {
        // Update COSEM Logical Device Name
        GXDLMSData d =
                (GXDLMSData) items.findByLN(ObjectType.DATA, "0.0.42.0.0.255");
        if (d != null && d.getValue() instanceof String) {
            String v = (String) d.getValue();
            d.setValue(
                    v.substring(0, 3) + String.format("%013d", serialNumber));
        }

        // Update Meter serial number.
        d = (GXDLMSData) items.findByLN(ObjectType.DATA, "0.0.96.1.0.255");
        if (d != null && d.getValue() instanceof String) {
            String v = (String) d.getValue();
            StringBuilder sb = new StringBuilder();
            for (byte it : v.getBytes()) {
                // Append chars.
                if (it < 0x30 || it > 0x39) {
                    sb.append((char) it);
                } else {
                    break;
                }
            }
            String format =
                    "%0" + String.valueOf(v.length() - sb.length()) + "d";
            d.setValue(sb.toString() + String.format(format, serialNumber));
        }
    }

    /**
     * Load saved COSEM objects from XML.
     * 
     * @param path
     *            File path.
     * @param items
     * @return
     * @throws IOException
     * @throws XMLStreamException
     */
    boolean loadObjects(String path, GXDLMSObjectCollection items)
            throws XMLStreamException, IOException {
        synchronized (fileLock) {
            File f = new File(path);
            if (f.exists()) {
                GXDLMSObjectCollection objects =
                        GXDLMSObjectCollection.load(path);
                items.clear();
                items.addAll(objects);
                updateValues(items);
                return true;
            }
        }
        return false;
    }

    boolean init() throws Exception {
        // Load added objects.
        if (!loadObjects(objectsFile, getItems())) {
            throw new RuntimeException(String
                    .format("Invalid device template file {0}", objectsFile));
        }
        if (!Media.isOpen()) {
            Media.open();
        }
        ///////////////////////////////////////////////////////////////////////
        // Server must initialize after all objects are added.
        initialize();
        return true;
    }

    @Override
    public void close() throws Exception {
        super.close();
        Media.close();
    }

    @Override
    public void onPreRead(ValueEventArgs[] args) {
        for (ValueEventArgs e : args) {
            if (Trace.ordinal() > TraceLevel.WARNING.ordinal()) {
                System.out.println(String.format("PreRead %1$s:%2$s",
                        e.getTarget().getLogicalName(), e.getIndex()));
            }
        }
    }

    @Override
    public void onPostRead(ValueEventArgs[] args) {

    }

    @Override
    public void onPreWrite(ValueEventArgs[] args) {
        for (ValueEventArgs e : args) {
            System.out.println(String.format("PreWrite %1$s:%2$s",
                    e.getTarget().getLogicalName(), e.getIndex()));
        }
    }

    @Override
    public void onPostWrite(ValueEventArgs[] args) {
    }

    @Override
    public void onPreAction(ValueEventArgs[] args) throws Exception {
        for (ValueEventArgs e : args) {
            System.out.println(String.format("onPreAction %1$s:%2$s",
                    e.getTarget().getLogicalName(), e.getIndex()));
        }
    }

    @Override
    public void onPostAction(ValueEventArgs[] args) throws Exception {
        for (ValueEventArgs it : args) {
            System.out.println(String.format("onPostAction %1$s:%2$s",
                    it.getTarget().getLogicalName(), it.getIndex()));
        }
    }

    @Override
    public void onError(Object sender, Exception ex) {
        if (Trace.ordinal() > TraceLevel.OFF.ordinal()) {
            System.out.println("Error has occurred:" + ex.getMessage());
        }
    }

    /*
     * Client has send data.
     */
    @Override
    public void onReceived(Object sender, ReceiveEventArgs e) {
        try {
            synchronized (this) {
                if (Trace == TraceLevel.VERBOSE
                        && this.getConnectionState() != ConnectionState.NONE) {
                    System.out.println("RX:\t" + gurux.common.GXCommon
                            .bytesToHex((byte[]) e.getData()));
                }
                GXServerReply sr = new GXServerReply((byte[]) e.getData());
                do {
                    handleRequest(sr);
                    // Reply is null if we do not want to send any data to the
                    // client.
                    // This is done if client try to make connection with wrong
                    // server or client address.
                    if (sr.getReply() != null) {
                        if (Trace == TraceLevel.VERBOSE) {
                            System.out.println("TX:\t" + gurux.common.GXCommon
                                    .bytesToHex(sr.getReply()));
                        }
                        Media.send(sr.getReply(), e.getSenderInfo());
                        sr.setData(null);
                    }
                } while (sr.isStreaming());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void onMediaStateChange(Object sender, MediaStateEventArgs e) {

    }

    /*
     * Client has made connection.
     */
    @Override
    public void onClientConnected(Object sender,
            gurux.net.ConnectionEventArgs e) {
        if (Trace.ordinal() > TraceLevel.OFF.ordinal()
                && (!Exclusive || serialNumber == 1)) {
            System.out.println("TCP/IP connection established.");
        }
    }

    /*
     * Client has close connection.
     */
    @Override
    public void onClientDisconnected(Object sender,
            gurux.net.ConnectionEventArgs e) {
        if (Trace.ordinal() > TraceLevel.OFF.ordinal()
                && (!Exclusive || serialNumber == 1)) {
            System.out.println("TCP/IP connection closed.");
        }
    }

    @Override
    public void onTrace(Object sender, TraceEventArgs e) {
    }

    @Override
    public void onPropertyChanged(Object sender, PropertyChangedEventArgs e) {

    }

    @Override
    public GXDLMSObject onFindObject(ObjectType objectType, int sn, String ln) {
        if (objectType == ObjectType.ASSOCIATION_LOGICAL_NAME) {
            for (GXDLMSObject tmp : getItems()
                    .getObjects(ObjectType.ASSOCIATION_LOGICAL_NAME)) {
                GXDLMSAssociationLogicalName it =
                        (GXDLMSAssociationLogicalName) tmp;
                if (it.getAuthenticationMechanismName()
                        .getMechanismId() == getAuthentication()) {
                    return it;
                }
            }
        }
        return null;
    }

    /**
     * Example server accepts all connections.
     * 
     * @param serverAddress
     *            Server address.
     * @param clientAddress
     *            Client address.
     * @return True.
     */
    @Override
    public final boolean isTarget(final int serverAddress,
            final int clientAddress) {
        boolean ret = false;
        for (GXDLMSObject tmp : getItems()
                .getObjects(ObjectType.ASSOCIATION_LOGICAL_NAME)) {
            GXDLMSAssociationLogicalName it =
                    (GXDLMSAssociationLogicalName) tmp;
            if (it.getClientSAP() == clientAddress) {
                setMaxReceivePDUSize(
                        it.getXDLMSContextInfo().getMaxSendPduSize());
                setConformance(it.getXDLMSContextInfo().getConformance());
                ret = true;
                break;
            }
        }
        if (ret) {
            // Check server address using serial number.
            if (!(serverAddress == 0x3FFF || serverAddress == 0x7F
                    || (serverAddress & 0x3FFF) == serialNumber % 10000
                            + 1000)) {
                // Find address from the SAP table.
                GXDLMSObjectCollection saps =
                        getItems().getObjects(ObjectType.SAP_ASSIGNMENT);
                if (!saps.isEmpty()) {
                    ret = false;
                    for (GXDLMSObject it : saps) {
                        GXDLMSSapAssignment sap = (GXDLMSSapAssignment) it;
                        for (Entry<Integer, String> e : sap
                                .getSapAssignmentList()) {
                            // Check server address with two bytes.
                            if ((serverAddress & 0xFFFF0000) == 0
                                    && (serverAddress & 0x7FFF) == e.getKey()) {
                                ret = true;
                                break;
                            }
                            // Check server address with one byte.
                            if ((serverAddress & 0xFFFFFF00) == 0
                                    && (serverAddress & 0x7F) == e.getKey()) {
                                ret = true;
                                break;
                            }
                        }
                    }
                } else {
                    ret = serverAddress == 1;
                }
            }
        }
        return ret;
    }

    @Override
    public final SourceDiagnostic onValidateAuthentication(
            final Authentication authentication, final byte[] password) {
        if (getUseLogicalNameReferencing()) {
            GXDLMSAssociationLogicalName target = null;
            for (GXDLMSObject tmp : getItems()
                    .getObjects(ObjectType.ASSOCIATION_LOGICAL_NAME)) {
                GXDLMSAssociationLogicalName it =
                        (GXDLMSAssociationLogicalName) tmp;
                if (it.getAuthenticationMechanismName()
                        .getMechanismId() == authentication) {
                    target = it;
                    break;
                }
            }
            if (target != null) {
                if (authentication != Authentication.LOW) {
                    // Other authentication levels are check later.
                    return SourceDiagnostic.NONE;
                }
                if (java.util.Arrays.equals(target.getSecret(), password)) {
                    return SourceDiagnostic.NONE;
                }
            }
        }
        return SourceDiagnostic.AUTHENTICATION_FAILURE;
    }

    @Override
    protected AccessMode onGetAttributeAccess(final ValueEventArgs arg) {
        if (getUseLogicalNameReferencing()) {
            for (GXDLMSObject tmp : getItems()
                    .getObjects(ObjectType.ASSOCIATION_LOGICAL_NAME)) {
                GXDLMSAssociationLogicalName it =
                        (GXDLMSAssociationLogicalName) tmp;
                if (it.getAuthenticationMechanismName()
                        .getMechanismId() == getAuthentication()) {
                    return it.getObjectList()
                            .findByLN(arg.getTarget().getObjectType(),
                                    arg.getTarget().getLogicalName())
                            .getAccess(arg.getIndex());
                }
            }
        }
        return AccessMode.NO_ACCESS;
    }

    @Override
    protected MethodAccessMode onGetMethodAccess(final ValueEventArgs arg) {
        if (getUseLogicalNameReferencing()) {
            for (GXDLMSObject tmp : getItems()
                    .getObjects(ObjectType.ASSOCIATION_LOGICAL_NAME)) {
                GXDLMSAssociationLogicalName it =
                        (GXDLMSAssociationLogicalName) tmp;
                if (it.getAuthenticationMechanismName()
                        .getMechanismId() == getAuthentication()) {
                    return it.getObjectList()
                            .findByLN(arg.getTarget().getObjectType(),
                                    arg.getTarget().getLogicalName())
                            .getMethodAccess(arg.getIndex());
                }
            }
        }
        return MethodAccessMode.NO_ACCESS;
    }

    /**
     * DLMS client connection succeeded.
     */
    @Override
    protected void onConnected(GXDLMSConnectionEventArgs connectionInfo) {
        // Show trace only for one meter.
        if (Trace.ordinal() > TraceLevel.WARNING.ordinal()) {
            System.out.println("Client Connected");
        }
    }

    /**
     * DLMS client connection failed.
     */
    @Override
    protected void
            onInvalidConnection(GXDLMSConnectionEventArgs connectionInfo) {

    }

    /**
     * DLMS client connection closed.
     */

    @Override
    protected void onDisconnected(GXDLMSConnectionEventArgs connectionInfo) {
        // Show trace only for one meter.
        if (Trace.ordinal() > TraceLevel.WARNING.ordinal()
                && getConnectionState() != ConnectionState.NONE) {
            System.out.println("Client Disconnected");
        }
    }

    /**
     * Schedule or profile generic asks current value.
     * 
     * @throws IOException
     */
    @Override
    public void onPreGet(ValueEventArgs[] args) throws IOException {
    }

    /**
     * Schedule or profile generic asks current value.
     */
    @Override
    public void onPostGet(ValueEventArgs[] e) {

    }

}