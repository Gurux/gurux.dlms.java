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

import java.util.Set;

import gurux.dlms.enums.AccessMode3;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode3;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSTcpUdpSetup;

/**
 * GXDLMSServer implements methods to implement DLMS/COSEM meter/proxy.
 */
public abstract class GXDLMSServer3 extends GXDLMSServer2 {
	/**
	 * Constructor for logical name referencing.
	 * 
	 * @param logicalNameReferencing Is logical name referencing used.
	 * @param type                   Interface type.
	 */
	public GXDLMSServer3(final boolean logicalNameReferencing, final InterfaceType type) {
		super(logicalNameReferencing, type);
	}

	/**
	 * Constructor for logical name referencing.
	 * 
	 * @param ln   Association logical name.
	 * @param type Interface type.
	 */
	public GXDLMSServer3(final GXDLMSAssociationLogicalName ln, final InterfaceType type) {
		super(ln, type);
	}

	/**
	 * Constructor for short name referencing.
	 * 
	 * @param sn   Association short name.
	 * @param type Interface type.
	 */
	public GXDLMSServer3(final GXDLMSAssociationShortName sn, final InterfaceType type) {
		super(sn, type);
	}

	/**
	 * Constructor for logical name referencing.
	 * 
	 * @param ln   Association logical name.
	 * @param hdlc HDLC settings.
	 */
	public GXDLMSServer3(final GXDLMSAssociationLogicalName ln, final GXDLMSHdlcSetup hdlc) {
		super(ln, hdlc);
	}

	/**
	 * Constructor for short name referencing.
	 * 
	 * @param sn   Association short name.
	 * @param hdlc HDLC settings.
	 */
	public GXDLMSServer3(final GXDLMSAssociationShortName sn, final GXDLMSHdlcSetup hdlc) {
		super(sn, hdlc);
	}

	/**
	 * Constructor for logical name referencing.
	 * 
	 * @param ln      Association logical name.
	 * @param wrapper WRAPPER settings.
	 */
	public GXDLMSServer3(final GXDLMSAssociationLogicalName ln, final GXDLMSTcpUdpSetup wrapper) {
		super(ln, wrapper);
	}

	/**
	 * Constructor for short name referencing.
	 * 
	 * @param sn      Association short name.
	 * @param wrapper WRAPPER settings.
	 */
	public GXDLMSServer3(final GXDLMSAssociationShortName sn, final GXDLMSTcpUdpSetup wrapper) {
		super(sn, wrapper);
	}

	/**
	 * Get attribute access mode.
	 * 
	 * @param arg Value event argument.
	 * @return Access mode.
	 * @throws Exception Server handler occurred exceptions.
	 */
	protected abstract Set<AccessMode3> onGetAttributeAccess3(ValueEventArgs arg) throws Exception;

	/**
	 * Get method access mode.
	 * 
	 * @param arg Value event argument.
	 * @return Method access mode.
	 * @throws Exception Server handler occurred exceptions.
	 */
	protected abstract Set<MethodAccessMode3> onGetMethodAccess3(ValueEventArgs arg) throws Exception;
}
