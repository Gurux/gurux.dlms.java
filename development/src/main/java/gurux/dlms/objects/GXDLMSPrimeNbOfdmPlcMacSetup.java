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
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPrimeNbOfdmPlcMacSetup
 */
public class GXDLMSPrimeNbOfdmPlcMacSetup extends GXDLMSObject implements IGXDLMSBase {
	/**
	 * PIB attribute 0x0010.
	 */
	private short macMinSwitchSearchTime;

	/**
	 * PIB attribute 0x0011.
	 */
	private short macMaxPromotionPdu;

	/**
	 * PIB attribute 0x0012.
	 */
	private short macPromotionPduTxPeriod;
	/**
	 * PIB attribute 0x0013.
	 */
	private short macBeaconsPerFrame;
	/**
	 * PIB attribute 0x0014.
	 */
	private short macScpMaxTxAttempts;
	/**
	 * PIB attribute 0x0015.
	 */
	private short macCtlReTxTimer;
	/**
	 * PIB attribute 0x0018.
	 */
	private short macMaxCtlReTx;

	/**
	 * Constructor.
	 */
	public GXDLMSPrimeNbOfdmPlcMacSetup() {
		this("0.0.28.2.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSPrimeNbOfdmPlcMacSetup(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSPrimeNbOfdmPlcMacSetup(final String ln, final int sn) {
		super(ObjectType.PRIME_NB_OFDM_PLC_MAC_SETUP, ln, sn);
	}

	/**
	 * @return PIB attribute 0x0010.
	 */
	public final short getMacMinSwitchSearchTime() {
		return macMinSwitchSearchTime;
	}

	/**
	 * @param value PIB attribute 0x0010.
	 */
	public final void setMacMinSwitchSearchTime(final short value) {
		macMinSwitchSearchTime = value;
	}

	/**
	 * @return PIB attribute 0x0011.
	 */
	public final short getMacMaxPromotionPdu() {
		return macMaxPromotionPdu;
	}

	/**
	 * @param value PIB attribute 0x0011.
	 */
	public final void setMacMaxPromotionPdu(final short value) {
		macMaxPromotionPdu = value;
	}

	/**
	 * @return PIB attribute 0x0012.
	 */
	public final short getMacPromotionPduTxPeriod() {
		return macPromotionPduTxPeriod;
	}

	/**
	 * @param value PIB attribute 0x0012.
	 */
	public final void setMacPromotionPduTxPeriod(final short value) {
		macPromotionPduTxPeriod = value;
	}

	/**
	 * @return PIB attribute 0x0013.
	 */
	public final short getMacBeaconsPerFrame() {
		return macBeaconsPerFrame;
	}

	/**
	 * @param value PIB attribute 0x0013.
	 */
	public final void setMacBeaconsPerFrame(final short value) {
		macBeaconsPerFrame = value;
	}

	/**
	 * @return PIB attribute 0x0014.
	 */
	public final short getMacScpMaxTxAttempts() {
		return macScpMaxTxAttempts;
	}

	/**
	 * @param value PIB attribute 0x0014.
	 */
	public final void setMacScpMaxTxAttempts(final short value) {
		macScpMaxTxAttempts = value;
	}

	/**
	 * @return PIB attribute 0x0015.
	 */
	public final short getMacCtlReTxTimer() {
		return macCtlReTxTimer;
	}

	/**
	 * @param value PIB attribute 0x0015.
	 */
	public final void setMacCtlReTxTimer(final short value) {
		macCtlReTxTimer = value;
	}

	/**
	 * @return PIB attribute 0x0018.
	 */
	public final short getMacMaxCtlReTx() {
		return macMaxCtlReTx;
	}

	/**
	 * @param value PIB attribute 0x0018.
	 */
	public final void setMacMaxCtlReTx(final short value) {
		macMaxCtlReTx = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), macMinSwitchSearchTime, macMaxPromotionPdu, macPromotionPduTxPeriod,
				macBeaconsPerFrame, macScpMaxTxAttempts, macCtlReTxTimer, macMaxCtlReTx };
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
		// MacMinSwitchSearchTime
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// MacMaxPromotionPdu
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// MacPromotionPduTxPeriod
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// MacBeaconsPerFrame
		if (all || canRead(5)) {
			attributes.add(5);
		}
		// MacScpMaxTxAttempts
		if (all || canRead(6)) {
			attributes.add(6);
		}
		// MacCtlReTxTimer
		if (all || canRead(7)) {
			attributes.add(7);
		}
		// MacMaxCtlReTx
		if (all || canRead(8)) {
			attributes.add(8);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 8;
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
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			return DataType.UINT8;
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
			return macMinSwitchSearchTime;
		case 3:
			return macMaxPromotionPdu;
		case 4:
			return macPromotionPduTxPeriod;
		case 5:
			return macBeaconsPerFrame;
		case 6:
			return macScpMaxTxAttempts;
		case 7:
			return macCtlReTxTimer;
		case 8:
			return macMaxCtlReTx;
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
			macMinSwitchSearchTime = ((Number) e.getValue()).shortValue();
			break;
		case 3:
			macMaxPromotionPdu = ((Number) e.getValue()).shortValue();
			break;
		case 4:
			macPromotionPduTxPeriod = ((Number) e.getValue()).shortValue();
			break;
		case 5:
			macBeaconsPerFrame = ((Number) e.getValue()).shortValue();
			break;
		case 6:
			macScpMaxTxAttempts = ((Number) e.getValue()).shortValue();
			break;
		case 7:
			macCtlReTxTimer = ((Number) e.getValue()).shortValue();
			break;
		case 8:
			macMaxCtlReTx = ((Number) e.getValue()).shortValue();
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		macMinSwitchSearchTime = (short) reader.readElementContentAsInt("MacMinSwitchSearchTime");
		macMaxPromotionPdu = (short) reader.readElementContentAsInt("MacMaxPromotionPdu");
		macPromotionPduTxPeriod = (short) reader.readElementContentAsInt("MacPromotionPduTxPeriod");
		macBeaconsPerFrame = (short) reader.readElementContentAsInt("MacBeaconsPerFrame");
		macScpMaxTxAttempts = (short) reader.readElementContentAsInt("MacScpMaxTxAttempts");
		macCtlReTxTimer = (short) reader.readElementContentAsInt("MacCtlReTxTimer");
		macMaxCtlReTx = (short) reader.readElementContentAsInt("MacMaxCtlReTx");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("MacMinSwitchSearchTime", macMinSwitchSearchTime);
		writer.writeElementString("MacMaxPromotionPdu", macMaxPromotionPdu);
		writer.writeElementString("MacPromotionPduTxPeriod", macPromotionPduTxPeriod);
		writer.writeElementString("MacBeaconsPerFrame", macBeaconsPerFrame);
		writer.writeElementString("MacScpMaxTxAttempts", macScpMaxTxAttempts);
		writer.writeElementString("MacCtlReTxTimer", macCtlReTxTimer);
		writer.writeElementString("MacMaxCtlReTx", macMaxCtlReTx);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "MacMinSwitchSearchTime", "MacMaxPromotionPdu", "MacPromotionPduTxPeriod",
				"MacBeaconsPerFrame", "MacScpMaxTxAttempts", "MacCtlReTxTimer", "MacMaxCtlReTx" };

	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}
}