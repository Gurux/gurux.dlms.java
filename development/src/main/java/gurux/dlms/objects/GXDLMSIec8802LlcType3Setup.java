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
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSIec8802LlcType3Setup
 */
public class GXDLMSIec8802LlcType3Setup extends GXDLMSObject implements IGXDLMSBase {

	/**
	 * Maximum number of octets in an ACn command PDU, N3.
	 */
	private int maximumOctetsACnPdu;
	/**
	 * Maximum number of times in transmissions N4.
	 */
	private byte maximumTransmissions;
	/**
	 * Acknowledgement time, T1.
	 */
	private int acknowledgementTime;
	/**
	 * Receive lifetime variable, T2.
	 */
	private int receiveLifetime;
	/**
	 * Transmit lifetime variable, T3.
	 */
	private int transmitLifetime;

	/**
	 * Constructor.
	 */
	public GXDLMSIec8802LlcType3Setup() {
		this("0.0.27.2.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSIec8802LlcType3Setup(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSIec8802LlcType3Setup(final String ln, final int sn) {
		super(ObjectType.IEC_8802_LLC_TYPE3_SETUP, ln, sn);
	}

	/**
	 * @return Maximum number of octets in an ACn command PDU, N3.
	 */
	public final int getMaximumOctetsACnPdu() {
		return maximumOctetsACnPdu;
	}

	/**
	 * @param value Maximum number of octets in an ACn command PDU, N3.
	 */
	public final void setMaximumOctetsACnPdu(int value) {
		maximumOctetsACnPdu = value;
	}

	/**
	 * @return Maximum number of times in transmissions N4.
	 */
	public final byte getMaximumTransmissions() {
		return maximumTransmissions;
	}

	/**
	 * @param value Maximum number of times in transmissions N4.
	 */
	public final void setMaximumTransmissions(final byte value) {
		maximumTransmissions = value;
	}

	/**
	 * @return Acknowledgement time, T1.
	 */
	public final int getAcknowledgementTime() {
		return acknowledgementTime;
	}

	/**
	 * @param value Acknowledgement time, T1.
	 */
	public final void setAcknowledgementTime(final int value) {
		acknowledgementTime = value;
	}

	/**
	 * @return Receive lifetime variable, T2.
	 */
	public final int getReceiveLifetime() {
		return receiveLifetime;
	}

	/**
	 * @param value Receive lifetime variable, T2.
	 */
	public final void setReceiveLifetime(final int value) {
		receiveLifetime = value;
	}

	/**
	 * @return Transmit lifetime variable, T3.
	 */
	public final int getTransmitLifetime() {
		return transmitLifetime;
	}

	/**
	 * @param value Transmit lifetime variable, T3.
	 */
	public final void setTransmitLifetime(final int value) {
		transmitLifetime = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), maximumOctetsACnPdu, maximumTransmissions, acknowledgementTime,
				receiveLifetime, transmitLifetime };
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
		// MaximumOctetsACnPdu
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// MaximumTransmissions
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// AcknowledgementTime
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// ReceiveLifetime
		if (all || canRead(5)) {
			attributes.add(5);
		}
		// TransmitLifetime
		if (all || canRead(6)) {
			attributes.add(6);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 6;
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
		switch (index) {
		case 1:
			return DataType.OCTET_STRING;
		case 2:
		case 3:
		case 5:
			return DataType.UINT8;
		case 4:
		case 6:
		case 7:
		case 8:
		case 9:
			return DataType.UINT16;
		default:
			throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
		}
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
			ret = maximumOctetsACnPdu;
			break;
		case 3:
			ret = maximumTransmissions;
			break;
		case 4:
			ret = acknowledgementTime;
			break;
		case 5:
			ret = receiveLifetime;
			break;
		case 6:
			ret = transmitLifetime;
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
			maximumOctetsACnPdu = ((Number) e.getValue()).intValue();
			break;
		case 3:
			maximumTransmissions = ((Number) e.getValue()).byteValue();
			break;
		case 4:
			acknowledgementTime = ((Number) e.getValue()).intValue();
			break;
		case 5:
			receiveLifetime = ((Number) e.getValue()).intValue();
			break;
		case 6:
			transmitLifetime = ((Number) e.getValue()).intValue();
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		maximumOctetsACnPdu = reader.readElementContentAsInt("MaximumOctetsACnPdu");
		maximumTransmissions = (byte) reader.readElementContentAsInt("MaximumTransmissions");
		acknowledgementTime = reader.readElementContentAsInt("AcknowledgementTime");
		receiveLifetime = reader.readElementContentAsInt("ReceiveLifetime");
		transmitLifetime = reader.readElementContentAsInt("TransmitLifetime");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("MaximumOctetsACnPdu", maximumOctetsACnPdu);
		writer.writeElementString("MaximumTransmissions", maximumTransmissions);
		writer.writeElementString("AcknowledgementTime", acknowledgementTime);
		writer.writeElementString("ReceiveLifetime", receiveLifetime);
		writer.writeElementString("TransmitLifetime", transmitLifetime);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "MaximumOctetsACnPdu", "MaximumTransmissions", "AcknowledgementTime",
				"ReceiveLifetime", "TransmitLifetime" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}
}