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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import gurux.common.GXCommon;
import gurux.common.IGXMedia;
import gurux.common.IGXMediaListener;
import gurux.common.MediaStateEventArgs;
import gurux.common.PropertyChangedEventArgs;
import gurux.common.ReceiveEventArgs;
import gurux.common.TraceEventArgs;
import gurux.common.enums.TraceLevel;
import gurux.dlms.ConnectionState;
import gurux.dlms.GXCryptoKeyParameter;
import gurux.dlms.GXDLMSConnectionEventArgs;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXServerReply;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AccessMode3;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.MethodAccessMode3;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSSapAssignment;
import gurux.dlms.objects.GXDLMSSecuritySetup;
import gurux.dlms.objects.GXXmlWriterSettings;
import gurux.dlms.secure.GXDLMSSecureServer3;

/**
 * All example servers are using same objects.
 */
public class GXDLMSMeter extends GXDLMSSecureServer3 implements IGXMediaListener, gurux.net.IGXNetListener {

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
	 * @param logicalNameReferencing Is logical name referencing used.
	 * @param type                   Interface type.
	 */
	public GXDLMSMeter(boolean logicalNameReferencing, InterfaceType type) {
		super(logicalNameReferencing, type);
	}

	public void initialize(IGXMedia media, TraceLevel trace, String path, int sn, boolean exclusive) throws Exception {
		serialNumber = sn;
		objectsFile = path;
		Exclusive = exclusive;
		Media = media;
		Trace = trace;
		media.addListener(this);
		///////////////////////////////////////////////////////////////////////
		// Each association has own conformance.
		getConformance().clear();
		init();
	}

	/**
	 * Update simulated values for the meter instance.
	 * 
	 * @param items Simulated COSEM objects.
	 */
	void updateValues(GXDLMSObjectCollection items) {
		// Update COSEM Logical Device Name
		String LDN = String.format("%013d", serialNumber);
		GXDLMSData d = (GXDLMSData) items.findByLN(ObjectType.DATA, "0.0.42.0.0.255");
		if (d != null && d.getValue() instanceof String) {
			String v = (String) d.getValue();
			if (v != "") {
				d.setValue(v.substring(0, 3) + LDN.getBytes());
				getCiphering().setSystemTitle((byte[]) d.getValue());
			}
		}
		// Update SAP Assignments
		for (GXDLMSObject it : getItems().getObjects(ObjectType.SAP_ASSIGNMENT)) {
			GXDLMSSapAssignment sap = (GXDLMSSapAssignment) it;
			for (Entry<Integer, String> e : sap.getSapAssignmentList()) {
				e.setValue(LDN);
			}
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
			String format = "%0" + String.valueOf(v.length() - sb.length()) + "d";
			d.setValue(sb.toString() + String.format(format, serialNumber));
		}
	}

	/**
	 * Load saved COSEM objects from XML.
	 * 
	 * @param path  File path.
	 * @param items
	 * @return
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	boolean loadObjects(String path, GXDLMSObjectCollection items) throws XMLStreamException, IOException {
		synchronized (fileLock) {
			File f = new File(path);
			if (f.exists()) {
				GXDLMSObjectCollection objects = GXDLMSObjectCollection.load(path);
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
			throw new RuntimeException(String.format("Invalid device template file %s", objectsFile));
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
				System.out.println(String.format("PreRead %1$s:%2$s", e.getTarget().getLogicalName(), e.getIndex()));
			}
		}
	}

	@Override
	public void onPostRead(ValueEventArgs[] args) {

	}

	@Override
	public void onPreWrite(ValueEventArgs[] args) {
		for (ValueEventArgs e : args) {
			System.out.println(String.format("PreWrite %1$s:%2$s", e.getTarget().getLogicalName(), e.getIndex()));
		}
	}

	/**
	 * 
	 * @param title Update a new syste title when user updates new Logical Device
	 *              Name.
	 */
	private void updateSystemTitle(byte[] title) {
		getCiphering().setSystemTitle(title);
		for (GXDLMSObject it : getItems().getObjects(ObjectType.SECURITY_SETUP)) {
			GXDLMSSecuritySetup ss = (GXDLMSSecuritySetup) it;
			ss.setServerSystemTitle(title);
		}

		// Update SAP Assignments
		for (GXDLMSObject it : getItems().getObjects(ObjectType.SAP_ASSIGNMENT)) {
			GXDLMSSapAssignment sap = (GXDLMSSapAssignment) it;
			for (Entry<Integer, String> e : sap.getSapAssignmentList()) {
				e.setValue(new String(title));
			}
		}
	}

	@Override
	public void onPostWrite(ValueEventArgs[] args) {
		GXXmlWriterSettings settings = new GXXmlWriterSettings();
		try {
			for (ValueEventArgs it : args) {
				if (it.getError() != ErrorCode.OK) {
					// Load default values if user has try to save invalid
					// data.
					getItems().clear();
					getItems().addAll(GXDLMSObjectCollection.load(objectsFile));
					return;
				}
				// If user writes a new Logical Device Name.
				if (it.getTarget().getObjectType() == ObjectType.DATA
						&& it.getTarget().getLogicalName().compareTo("0.0.42.0.0.255") == 0) {
					GXDLMSData d = (GXDLMSData) it.getTarget();
					if (d.getValue() instanceof byte[] && ((byte[]) d.getValue()).length == 8) {
						updateSystemTitle((byte[]) d.getValue());
					} else {
						// If content of the system title is invalid.
						it.setError(ErrorCode.INCONSISTENT_CLASS);
						getItems().clear();
						getItems().addAll(GXDLMSObjectCollection.load(objectsFile));
						return;
					}
				}
			}
			getItems().save(objectsFile, settings);
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPreAction(ValueEventArgs[] args) throws Exception {
		for (ValueEventArgs it : args) {
			System.out.println(String.format("onPreAction %1$s:%2$s", it.getTarget().getLogicalName(), it.getIndex()));
		}
	}

	@Override
	public void onPostAction(ValueEventArgs[] args) throws Exception {
		for (ValueEventArgs it : args) {
			if (it.getError() != ErrorCode.OK) {
				// Load default values if user has try to save invalid
				// data.
				getItems().clear();
				getItems().addAll(GXDLMSObjectCollection.load(objectsFile));
				return;
			}
			// Save value if it's updated with action.
			if (isChangedWithAction(it.getTarget().getObjectType(), it.getIndex())) {
				GXXmlWriterSettings settings = new GXXmlWriterSettings();
				getItems().save(objectsFile, settings);
			}
			if (it.getTarget() instanceof GXDLMSSecuritySetup && it.getIndex() == 2) {
				System.out.println("----------------------------------------------------------");
				System.out.println("Updated keys:");
				System.out.println("Server System title: " + GXCommon.bytesToHex(getCiphering().getSystemTitle()));
				System.out.println("Authentication key: " + GXCommon.bytesToHex(getCiphering().getAuthenticationKey()));
				System.out.println("Block cipher key: " + GXCommon.bytesToHex(getCiphering().getBlockCipherKey()));
				System.out.println("Client System title: " + GXDLMSTranslator.toHex(getClientSystemTitle()));
				System.out.println("Master key (KEK) title: " + GXDLMSTranslator.toHex(getKek()));
			}
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
				if (Trace == TraceLevel.VERBOSE && this.getConnectionState() != ConnectionState.NONE) {
					System.out.println("RX:\t" + gurux.common.GXCommon.bytesToHex((byte[]) e.getData()));
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
							System.out.println("TX:\t" + gurux.common.GXCommon.bytesToHex(sr.getReply()));
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
	public void onClientConnected(Object sender, gurux.net.ConnectionEventArgs e) {
		if (Trace.ordinal() > TraceLevel.OFF.ordinal() && (!Exclusive || serialNumber == 1)) {
			System.out.println("TCP/IP connection established.");
		}
	}

	/*
	 * Client has close connection.
	 */
	@Override
	public void onClientDisconnected(Object sender, gurux.net.ConnectionEventArgs e) {
		if (Trace.ordinal() > TraceLevel.OFF.ordinal() && (!Exclusive || serialNumber == 1)) {
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
			for (GXDLMSObject it : getItems()) {
				if (it.getObjectType() == ObjectType.ASSOCIATION_LOGICAL_NAME) {
					GXDLMSAssociationLogicalName a = (GXDLMSAssociationLogicalName) it;
					if (a.getClientSAP() == getSettings().getClientAddress()
							&& a.getAuthenticationMechanismName().getMechanismId() == getSettings().getAuthentication()
							&& (ln.compareTo(a.getLogicalName()) == 0 || ln.compareTo("0.0.40.0.0.255") == 0))
						return it;
				}
			}
		}
		// Find object from the active association view.
		else if (getAssignedAssociation() != null) {
			return getAssignedAssociation().getObjectList().findByLN(objectType, ln);
		}
		return null;
	}

	/**
	 * Example server accepts all connections.
	 * 
	 * @param serverAddress Server address.
	 * @param clientAddress Client address.
	 * @return True.
	 */
	@Override
	public final boolean isTarget(final int serverAddress, final int clientAddress) {
		// Find client address from the association views.
		setAssignedAssociation(null);
		for (GXDLMSObject it : this.getItems().getObjects(ObjectType.ASSOCIATION_LOGICAL_NAME)) {
			{
				GXDLMSAssociationLogicalName a = (GXDLMSAssociationLogicalName) it;
				if (a.getClientSAP() == clientAddress) {
					setAssignedAssociation(a);
					break;
				}
			}
		}
		if (getAssignedAssociation() != null) {
			// If address is not broadcast or serial number.
			// Remove logical address from the server address.
			boolean broadcast = (serverAddress & 0x3FFF) == 0x3FFF || (serverAddress & 0x7F) == 0x7F;
			if (!(broadcast || (serverAddress & 0x3FFF) == serialNumber % 10000 + 1000)) {
				// Find address from the SAP table.
				GXDLMSObjectCollection objs = getItems().getObjects(ObjectType.SAP_ASSIGNMENT);
				if (objs.size() == 0) {
					return true;
				}
				for (GXDLMSObject s : objs) {
					GXDLMSSapAssignment sap = (GXDLMSSapAssignment) s;
					if (sap.getSapAssignmentList().isEmpty()) {
						return true;
					}
					for (Map.Entry<Integer, String> e : sap.getSapAssignmentList()) {
						// Check server address with two bytes.
						if ((serverAddress & 0xFFFF0000) == 0 && (serverAddress & 0x7FFF) == e.getKey()) {
							return true;
						}
						// Check server address with one byte.
						if ((serverAddress & 0xFFFFFF00) == 0 && (serverAddress & 0x7F) == e.getKey()) {
							return true;
						}
					}
				}
			}
		} else {
			System.out.println(String.format("Invalid client address: %1$s ", clientAddress));
		}
		return false;
	}

	@Override
	public final SourceDiagnostic onValidateAuthentication(final Authentication authentication, final byte[] password) {
		if (getUseLogicalNameReferencing()) {
			if (getAssignedAssociation() != null) {
				if (getAssignedAssociation().getAuthenticationMechanismName().getMechanismId() != authentication) {
					if (authentication == Authentication.NONE) {
						return SourceDiagnostic.AUTHENTICATION_REQUIRED;
					}
					return SourceDiagnostic.AUTHENTICATION_FAILURE;
				}
				if (authentication != Authentication.LOW) {
					// Other authentication levels are check later.
					return SourceDiagnostic.NONE;
				}
				if (java.util.Arrays.equals(getAssignedAssociation().getSecret(), password)) {
					return SourceDiagnostic.NONE;
				}
			}
		}
		return SourceDiagnostic.AUTHENTICATION_FAILURE;
	}

	@Override
	protected AccessMode onGetAttributeAccess(final ValueEventArgs arg) {
		return getAssignedAssociation().getAccess(arg.getTarget(), arg.getIndex());
	}

	@Override
	protected Set<AccessMode3> onGetAttributeAccess3(ValueEventArgs arg) throws Exception {
		return getAssignedAssociation().getAccess3(arg.getTarget(), arg.getIndex());
	}

	@Override
	protected MethodAccessMode onGetMethodAccess(final ValueEventArgs arg) {
		return getAssignedAssociation().getMethodAccess(arg.getTarget(), arg.getIndex());
	}

	@Override
	protected Set<MethodAccessMode3> onGetMethodAccess3(ValueEventArgs arg) throws Exception {
		return getAssignedAssociation().getMethodAccess3(arg.getTarget(), arg.getIndex());
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
	protected void onInvalidConnection(GXDLMSConnectionEventArgs connectionInfo) {

	}

	/**
	 * DLMS client connection closed.
	 */

	@Override
	protected void onDisconnected(GXDLMSConnectionEventArgs connectionInfo) {
		// Show trace only for one meter.
		if (Trace.ordinal() > TraceLevel.WARNING.ordinal() && getConnectionState() != ConnectionState.NONE) {
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

	@Override
	public void OnKey(Object sender, GXCryptoKeyParameter args) {
	}

	@Override
	public void OnCrypto(Object sender, GXCryptoKeyParameter args) {

	}
}