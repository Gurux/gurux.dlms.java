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

package gurux.dlms.objects;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSSFSKMacSynchronizationTimeouts
 */
public class GXDLMSSFSKMacSynchronizationTimeouts extends GXDLMSObject implements IGXDLMSBase {

	/**
	 * Search initiator timeout.
	 */
	private int searchInitiatorTimeout;
	/**
	 * Synchronization confirmation timeout.
	 */
	private int synchronizationConfirmationTimeout;
	/**
	 * Time out not addressed.
	 */
	private int timeOutNotAddressed;
	/**
	 * Time out frame not OK.
	 */
	private int timeOutFrameNotOK;

	/**
	 * Constructor.
	 */
	public GXDLMSSFSKMacSynchronizationTimeouts() {
		this("0.0.26.2.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSSFSKMacSynchronizationTimeouts(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSSFSKMacSynchronizationTimeouts(final String ln, final int sn) {
		super(ObjectType.SFSK_MAC_SYNCHRONIZATION_TIMEOUTS, ln, sn);
	}

	public final int getSearchInitiatorTimeout() {
		return searchInitiatorTimeout;
	}

	public final void setSearchInitiatorTimeout(final int value) {
		searchInitiatorTimeout = value;
	}

	public final int getSynchronizationConfirmationTimeout() {
		return synchronizationConfirmationTimeout;
	}

	public final void setSynchronizationConfirmationTimeout(final int value) {
		synchronizationConfirmationTimeout = value;
	}

	public final int getTimeOutNotAddressed() {
		return timeOutNotAddressed;
	}

	public final void setTimeOutNotAddressed(final int value) {
		timeOutNotAddressed = value;
	}

	public final int getTimeOutFrameNotOK() {
		return timeOutFrameNotOK;
	}

	public final void setTimeOutFrameNotOK(final int value) {
		timeOutFrameNotOK = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), searchInitiatorTimeout, synchronizationConfirmationTimeout,
				timeOutNotAddressed, timeOutFrameNotOK };
	}

	/*
	 * Returns collection of attributes to read. If attribute is static and already
	 * read or device is returned HW error it is not returned.
	 */
	@Override
	public final int[] getAttributeIndexToRead(final boolean all) {
		java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
		// LN is static and read only once.
		if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
			attributes.add(1);
		}
		// SearchInitiatorTimeout
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// SynchronizationConfirmationTimeout
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// TimeOutNotAddressed
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// TimeOutFrameNotOK
		if (all || canRead(5)) {
			attributes.add(5);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 5;
	}

	/*
	 * Returns amount of methods.
	 */
	@Override
	public final int getMethodCount() {
		return 0;
	}

	@Override
	public final DataType getDataType(final int index) {
		DataType ret;
		switch (index) {
		case 1:
			ret = DataType.OCTET_STRING;
			break;
		case 2:
		case 3:
		case 4:
		case 5:
			ret = DataType.UINT16;
			break;
		default:
			throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
		}
		return ret;
	}

	/*
	 * Returns value of given attribute.
	 */
	@Override
	public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		Object ret;
		switch (e.getIndex()) {
		case 1:
			ret = GXCommon.logicalNameToBytes(getLogicalName());
			break;
		case 2:
			ret = searchInitiatorTimeout;
			break;
		case 3:
			ret = synchronizationConfirmationTimeout;
			break;
		case 4:
			ret = timeOutNotAddressed;
			break;
		case 5:
			ret = timeOutFrameNotOK;
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			ret = null;
			break;
		}
		return ret;
	}

	/*
	 * Set value of given attribute.
	 */
	@Override
	public final void setValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		switch (e.getIndex()) {
		case 1:
			setLogicalName(GXCommon.toLogicalName(e.getValue()));
			break;
		case 2:
			searchInitiatorTimeout = ((Number) e.getValue()).intValue();
			break;
		case 3:
			synchronizationConfirmationTimeout = ((Number) e.getValue()).intValue();
			break;
		case 4:
			timeOutNotAddressed = ((Number) e.getValue()).intValue();
			break;
		case 5:
			timeOutFrameNotOK = ((Number) e.getValue()).intValue();
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		searchInitiatorTimeout = reader.readElementContentAsInt("SearchInitiatorTimeout");
		synchronizationConfirmationTimeout = reader.readElementContentAsInt("SynchronizationConfirmationTimeout");
		timeOutNotAddressed = reader.readElementContentAsInt("TimeOutNotAddressed");
		timeOutFrameNotOK = reader.readElementContentAsInt("TimeOutFrameNotOK");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("SearchInitiatorTimeout", searchInitiatorTimeout);
		writer.writeElementString("SynchronizationConfirmationTimeout", synchronizationConfirmationTimeout);
		writer.writeElementString("TimeOutNotAddressed", timeOutNotAddressed);
		writer.writeElementString("TimeOutFrameNotOK", timeOutFrameNotOK);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "SearchInitiatorTimeout", "SynchronizationConfirmationTimeout",
				"TimeOutNotAddressed", "TimeOutFrameNotOK" };

	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}
}