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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPrimeNbOfdmPlcMacCounters
 */
public class GXDLMSPrimeNbOfdmPlcMacCounters extends GXDLMSObject implements IGXDLMSBase {
	/**
	 * Count of successfully transmitted MSDUs.
	 */
	private long txDataPktCount;

	/**
	 * Count of successfully received MSDUs whose destination address was this node.
	 */
	private long rxDataPktCount;
	/**
	 * Count of successfully transmitted MAC control packets.
	 */
	private long txCtrlPktCount;
	/**
	 * Count of successfully received MAC control packets whose destination was this
	 * node.
	 */
	private long rxCtrlPktCount;
	/**
	 * Count of failed CSMA transmit attempts.
	 */
	private long csmaFailCount;
	/**
	 * Count of number of times this node has to back off SCP transmission due to
	 * channel busy state.
	 */
	private long csmaChBusyCount;

	/**
	 * Constructor.
	 */
	public GXDLMSPrimeNbOfdmPlcMacCounters() {
		this("0.0.28.4.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSPrimeNbOfdmPlcMacCounters(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSPrimeNbOfdmPlcMacCounters(final String ln, final int sn) {
		super(ObjectType.PRIME_NB_OFDM_PLC_MAC_COUNTERS, ln, sn);
	}

	/**
	 * @return Count of successfully transmitted MSDUs.
	 */
	public final long getTxDataPktCount() {
		return txDataPktCount;
	}

	/**
	 * @param value Count of successfully transmitted MSDUs.
	 */
	public final void setTxDataPktCount(final long value) {
		txDataPktCount = value;
	}

	/**
	 * @return Count of successfully received MSDUs whose destination address was
	 *         this node.
	 */
	public final long getRxDataPktCount() {
		return rxDataPktCount;
	}

	/**
	 * @param value Count of successfully received MSDUs whose destination address
	 *              was this node.
	 */
	public final void setRxDataPktCount(final long value) {
		rxDataPktCount = value;
	}

	/**
	 * @return Count of successfully transmitted MAC control packets.
	 */
	public final long getTxCtrlPktCount() {
		return txCtrlPktCount;
	}

	/**
	 * @param value Count of successfully transmitted MAC control packets.
	 */
	public final void setTxCtrlPktCount(final long value) {
		txCtrlPktCount = value;
	}

	/**
	 * @return Count of successfully received MAC control packets whose destination
	 *         was this node.
	 */
	public final long getRxCtrlPktCount() {
		return rxCtrlPktCount;
	}

	/**
	 * @param value Count of successfully received MAC control packets whose
	 *              destination was this node.
	 */
	public final void setRxCtrlPktCount(final long value) {
		rxCtrlPktCount = value;
	}

	/**
	 * @return Count of failed CSMA transmit attempts.
	 */
	public final long getCsmaFailCount() {
		return csmaFailCount;
	}

	/**
	 * @param value Count of failed CSMA transmit attempts.
	 */
	public final void setCsmaFailCount(final long value) {
		csmaFailCount = value;
	}

	/**
	 * @return Count of number of times this node has to back off SCP transmission
	 *         due to channel busy state.
	 */
	public final long getCsmaChBusyCount() {
		return csmaChBusyCount;
	}

	/**
	 * @param value Count of number of times this node has to back off SCP
	 *              transmission due to channel busy state.
	 */
	public final void setCsmaChBusyCount(final long value) {
		csmaChBusyCount = value;
	}

	/**
	 * Reset all counters.
	 * 
	 * @param client DLMS client.
	 * @return Action bytes.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 */
	public final byte[][] reset(final GXDLMSClient client) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 1, 0, DataType.INT8);
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), txDataPktCount, rxDataPktCount, txCtrlPktCount, rxCtrlPktCount,
				csmaFailCount, csmaChBusyCount };
	}

	@Override
	public final byte[] invoke(final GXDLMSSettings settings, final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			txDataPktCount = rxDataPktCount = txCtrlPktCount = rxCtrlPktCount = csmaFailCount = csmaChBusyCount = 0;
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
		return null;
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
		// TxDataPktCount
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// RxDataPktCount
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// TxCtrlPktCount
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// RxCtrlPktCount
		if (all || canRead(5)) {
			attributes.add(5);
		}
		// CsmaFailCount
		if (all || canRead(6)) {
			attributes.add(6);
		}
		// CsmaChBusyCount
		if (all || canRead(7)) {
			attributes.add(7);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 7;
	}

	/*
	 * Returns amount of methods.
	 */
	@Override
	public final int getMethodCount() {
		return 1;
	}

	@Override
	public final DataType getDataType(final int index) {
		switch (index) {
		case 1:
			return DataType.OCTET_STRING;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			return DataType.UINT32;
		default:
			throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
		}
	}

	/*
	 * Returns value of given attribute.
	 */
	@Override
	public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		switch (e.getIndex()) {
		case 1:
			return GXCommon.logicalNameToBytes(getLogicalName());
		case 2:
			return txDataPktCount;
		case 3:
			return rxDataPktCount;
		case 4:
			return txCtrlPktCount;
		case 5:
			return rxCtrlPktCount;
		case 6:
			return csmaFailCount;
		case 7:
			return csmaChBusyCount;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
		return null;
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
			txDataPktCount = ((Number) e.getValue()).longValue();
			break;
		case 3:
			rxDataPktCount = ((Number) e.getValue()).longValue();
			break;
		case 4:
			txCtrlPktCount = ((Number) e.getValue()).longValue();
			break;
		case 5:
			rxCtrlPktCount = ((Number) e.getValue()).longValue();
			break;
		case 6:
			csmaFailCount = ((Number) e.getValue()).longValue();
			break;
		case 7:
			csmaChBusyCount = ((Number) e.getValue()).longValue();
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		txDataPktCount = reader.readElementContentAsLong("TxDataPktCount");
		rxDataPktCount = reader.readElementContentAsLong("RxDataPktCount");
		txCtrlPktCount = reader.readElementContentAsLong("TxCtrlPktCount");
		rxCtrlPktCount = reader.readElementContentAsLong("RxCtrlPktCount");
		csmaFailCount = reader.readElementContentAsLong("CsmaFailCount");
		csmaChBusyCount = reader.readElementContentAsLong("CsmaChBusyCount");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("TxDataPktCount", txDataPktCount);
		writer.writeElementString("RxDataPktCount", rxDataPktCount);
		writer.writeElementString("TxCtrlPktCount", txCtrlPktCount);
		writer.writeElementString("RxCtrlPktCount", rxCtrlPktCount);
		writer.writeElementString("CsmaFailCount", csmaFailCount);
		writer.writeElementString("CsmaChBusyCount", csmaChBusyCount);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "TxDataPktCount", "RxDataPktCount", "TxCtrlPktCount", "RxCtrlPktCount",
				"CsmaFailCount", "CsmaChBusyCount" };

	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Reset" };
	}
}