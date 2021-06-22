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
import java.util.HashSet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXBitString;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.CreditConfiguration;
import gurux.dlms.objects.enums.CreditStatus;
import gurux.dlms.objects.enums.CreditType;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
 */
public class GXDLMSCredit extends GXDLMSObject implements IGXDLMSBase {

	/**
	 * Current credit amount. Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private int currentCreditAmount;
	/**
	 * Type. Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private CreditType type;
	/**
	 * Priority. Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private byte priority;
	/**
	 * Warning threshold. Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private int warningThreshold;

	/**
	 * Limit. Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private int limit;
	/**
	 * Credit configuration. Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private java.util.Set<CreditConfiguration> creditConfiguration;
	/**
	 * Status. <br>
	 * Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private CreditStatus status;
	/**
	 * Preset credit amount. <br>
	 * Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private int presetCreditAmount;
	/**
	 * Credit available threshold.<br>
	 * Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private int creditAvailableThreshold;
	/**
	 * Period.<br>
	 * Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 */
	private GXDateTime period;

	/**
	 * Constructor.
	 */
	public GXDLMSCredit() {
		this("0.0.19.10.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSCredit(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSCredit(final String ln, final int sn) {
		super(ObjectType.CREDIT, ln, sn);
		creditConfiguration = new HashSet<CreditConfiguration>();
		type = CreditType.TOKEN;
		status = CreditStatus.ENABLED;
	}

	/**
	 * @return Current credit amount.
	 */
	public final int getCurrentCreditAmount() {
		return currentCreditAmount;
	}

	/**
	 * @param value Current credit amount.
	 */
	public final void setCurrentCreditAmount(final int value) {
		currentCreditAmount = value;
	}

	/**
	 * @return Type.
	 */
	public final CreditType getType() {
		return type;
	}

	/**
	 * @param value Type.
	 */
	public final void setType(final CreditType value) {
		type = value;
	}

	/**
	 * @return Priority.
	 */
	public final byte getPriority() {
		return priority;
	}

	/**
	 * @param value Priority.
	 */
	public final void setPriority(final byte value) {
		priority = value;
	}

	/**
	 * @return Warning threshold.
	 */
	public final int getWarningThreshold() {
		return warningThreshold;
	}

	/**
	 * @param value Warning threshold.
	 */
	public final void setWarningThreshold(final int value) {
		warningThreshold = value;
	}

	/**
	 * @return Limit.
	 */
	public final int getLimit() {
		return limit;
	}

	/**
	 * @param value Limit.
	 */
	public final void setLimit(final int value) {
		limit = value;
	}

	/**
	 * @return Credit configuration.
	 */
	public final java.util.Set<CreditConfiguration> getCreditConfiguration() {
		return creditConfiguration;
	}

	/**
	 * @param value Credit configuration.
	 */
	public final void setCreditConfiguration(final java.util.Set<CreditConfiguration> value) {
		creditConfiguration = value;
	}

	/**
	 * @return Status.
	 */
	public final CreditStatus getStatus() {
		return status;
	}

	/**
	 * @param value Status.
	 */
	public final void setStatus(final CreditStatus value) {
		status = value;
	}

	/**
	 * @return Preset credit amount.
	 */
	public final int getPresetCreditAmount() {
		return presetCreditAmount;
	}

	/**
	 * @param value Preset credit amount.
	 */
	public final void setPresetCreditAmount(final int value) {
		presetCreditAmount = value;
	}

	/**
	 * @return Credit available threshold.
	 */
	public final int getCreditAvailableThreshold() {
		return creditAvailableThreshold;
	}

	/**
	 * @param value Credit available threshold.
	 */
	public final void setCreditAvailableThreshold(final int value) {
		creditAvailableThreshold = value;
	}

	/**
	 * Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 * 
	 * @return Period
	 */
	public final GXDateTime getPeriod() {
		return period;
	}

	/**
	 * Online help:<br>
	 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCredit
	 * 
	 * @param value Period
	 */
	public final void setPeriod(final GXDateTime value) {
		period = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), currentCreditAmount, type, priority, warningThreshold, limit,
				creditConfiguration, status, presetCreditAmount, creditAvailableThreshold, period };
	}

	/**
	 * Adjusts the value of the current credit amount attribute.
	 * 
	 * @param client DLMS client.
	 * @param value  Current credit amount
	 * @return Action bytes.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws BadPaddingException                Bad padding exception.
	 */
	public final byte[][] updateAmount(final GXDLMSClient client, final int value)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 1, value, DataType.INT32);
	}

	/**
	 * Sets the value of the current credit amount attribute.
	 * 
	 * @param client DLMS client.
	 * @param value  Current credit amount
	 * @return Action bytes.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws BadPaddingException                Bad padding exception.
	 */
	public final byte[][] setAmountToValue(final GXDLMSClient client, final int value)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 2, value, DataType.INT32);
	}

	/**
	 * Sets the value of the current credit amount attribute.
	 * 
	 * @param client DLMS client.
	 * @param value  Current credit amount
	 * @return Action bytes.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 * @throws BadPaddingException                Bad padding exception.
	 */
	public final byte[][] invokeCredit(final GXDLMSClient client, final CreditStatus value)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(this, 3, value.getValue(), DataType.UINT8);
	}

	@Override
	public byte[] invoke(final GXDLMSSettings settings, final ValueEventArgs e) {
		switch (e.getIndex()) {
		case 1:
			currentCreditAmount += ((Number) e.getValue()).intValue();
			break;
		case 2:
			currentCreditAmount = ((Number) e.getValue()).intValue();
			break;
		case 3:
			if (creditConfiguration.contains(CreditConfiguration.CONFIRMATION) && status == CreditStatus.SELECTABLE) {
				status = CreditStatus.INVOKED;
			}
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
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
		// CurrentCreditAmount
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// Type
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// Priority
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// WarningThreshold
		if (all || canRead(5)) {
			attributes.add(5);
		}
		// Limit
		if (all || canRead(6)) {
			attributes.add(6);
		}
		// creditConfiguration
		if (all || canRead(7)) {
			attributes.add(7);
		}
		// Status
		if (all || canRead(8)) {
			attributes.add(8);
		}
		// PresetCreditAmount
		if (all || canRead(9)) {
			attributes.add(9);
		}
		// CreditAvailableThreshold
		if (all || canRead(10)) {
			attributes.add(10);
		}
		// Period
		if (all || canRead(11)) {
			attributes.add(11);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 11;
	}

	/*
	 * Returns amount of methods.
	 */
	@Override
	public final int getMethodCount() {
		return 3;
	}

	@Override
	public final DataType getDataType(final int index) {
		switch (index) {
		case 1:
			return DataType.OCTET_STRING;
		case 2:
			return DataType.INT32;
		case 3:
			return DataType.ENUM;
		case 4:
			return DataType.UINT8;
		case 5:
			return DataType.INT32;
		case 6:
			return DataType.INT32;
		case 7:
			return DataType.BITSTRING;
		case 8:
			return DataType.ENUM;
		case 9:
			return DataType.INT32;
		case 10:
			return DataType.INT32;
		case 11:
			return DataType.OCTET_STRING;
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
			return currentCreditAmount;
		case 3:
			return type.getValue();
		case 4:
			return priority;
		case 5:
			return warningThreshold;
		case 6:
			return limit;
		case 7:
			return GXBitString.toBitString(CreditConfiguration.toInteger(creditConfiguration), 5);
		case 8:
			return status.getValue();
		case 9:
			return presetCreditAmount;
		case 10:
			return creditAvailableThreshold;
		case 11:
			return period;
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
			currentCreditAmount = ((Number) e.getValue()).intValue();
			break;
		case 3:
			type = CreditType.forValue(((Number) e.getValue()).intValue());
			break;
		case 4:
			priority = ((Number) e.getValue()).byteValue();
			break;
		case 5:
			warningThreshold = ((Number) e.getValue()).intValue();
			break;
		case 6:
			limit = ((Number) e.getValue()).intValue();
			break;
		case 7:
			creditConfiguration = CreditConfiguration.forValue(((GXBitString) e.getValue()).toInteger());
			break;
		case 8:
			status = CreditStatus.forValue(((Number) e.getValue()).intValue());
			break;
		case 9:
			presetCreditAmount = ((Number) e.getValue()).intValue();
			break;
		case 10:
			creditAvailableThreshold = ((Number) e.getValue()).intValue();
			break;
		case 11:
			if (e.getValue() == null) {
				period = new GXDateTime();
			} else {
				GXDateTime tmp;
				if (e.getValue() instanceof byte[]) {
					boolean useUtc;
					if (e.getSettings() != null) {
						useUtc = e.getSettings().getUseUtc2NormalTime();
					} else {
						useUtc = false;
					}
					tmp = (GXDateTime) GXDLMSClient.changeType((byte[]) e.getValue(), DataType.DATETIME, useUtc);
				} else {
					tmp = (GXDateTime) e.getValue();
				}
				period = tmp;
			}
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		currentCreditAmount = reader.readElementContentAsInt("CurrentCreditAmount");
		type = CreditType.forValue(reader.readElementContentAsInt("Type"));
		priority = (byte) reader.readElementContentAsInt("Priority");
		warningThreshold = reader.readElementContentAsInt("WarningThreshold");
		limit = reader.readElementContentAsInt("Limit");
		creditConfiguration = CreditConfiguration.forValue(reader.readElementContentAsInt("CreditConfiguration"));
		status = CreditStatus.forValue(reader.readElementContentAsInt("Status"));
		presetCreditAmount = reader.readElementContentAsInt("PresetCreditAmount");
		creditAvailableThreshold = reader.readElementContentAsInt("CreditAvailableThreshold");
		period = reader.readElementContentAsDateTime("Period");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("CurrentCreditAmount", currentCreditAmount);
		if (type != null) {
			writer.writeElementString("Type", type.getValue());
		}
		writer.writeElementString("Priority", priority);
		writer.writeElementString("WarningThreshold", warningThreshold);
		writer.writeElementString("Limit", limit);
		writer.writeElementString("CreditConfiguration", CreditConfiguration.toInteger(creditConfiguration));
		if (status != null) {
			writer.writeElementString("Status", status.getValue());
		}
		writer.writeElementString("PresetCreditAmount", presetCreditAmount);
		writer.writeElementString("CreditAvailableThreshold", creditAvailableThreshold);
		writer.writeElementString("Period", period);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "CurrentCreditAmount", "Type", "Priority", "WarningThreshold", "Limit",
				"CreditConfiguration", "Status", "PresetCreditAmount", "CreditAvailableThreshold", "Period" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Update amount", "Set amount to value", "Invoke credit" };
	}
}