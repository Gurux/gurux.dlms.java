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
import java.util.Calendar;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Unit;
import gurux.dlms.internal.GXCommon;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSDemandRegister
 */
public class GXDLMSDemandRegister extends GXDLMSObject implements IGXDLMSBase {
	private int scaler;
	private int unit;
	private Object currentAverageValue;
	private Object lastAverageValue;
	private Object status;
	private GXDateTime captureTime = new GXDateTime();
	private GXDateTime startTimeCurrent = new GXDateTime();
	private long numberOfPeriods;
	private long period;

	/**
	 * Constructor.
	 */
	public GXDLMSDemandRegister() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSDemandRegister(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSDemandRegister(final String ln, final int sn) {
		super(ObjectType.DEMAND_REGISTER, ln, sn);
		setScaler(1);
	}

	/**
	 * @return Current average value of COSEM Data object.
	 */
	public final Object getCurrentAverageValue() {
		return currentAverageValue;
	}

	/**
	 * @param value Current average value of COSEM Data object.
	 */
	public final void setCurrentAverageValue(final Object value) {
		currentAverageValue = value;
	}

	/**
	 * @return Last average value of COSEM Data object.
	 */
	public final Object getLastAverageValue() {
		return lastAverageValue;
	}

	/**
	 * @param value Last average value of COSEM Data object.
	 */
	public final void setLastAverageValue(final Object value) {
		lastAverageValue = value;
	}

	/**
	 * @return Scaler of COSEM Register object.
	 */
	public final double getScaler() {
		return Math.pow(10, scaler);
	}

	/**
	 * @param value Scaler of COSEM Register object.
	 */
	public final void setScaler(final double value) {
		scaler = (int) Math.log10(value);
	}

	/**
	 * @return Unit of COSEM Register object.
	 */
	public final Unit getUnit() {
		return Unit.forValue(unit);
	}

	/**
	 * @param value Unit of COSEM Register object.
	 */
	public final void setUnit(final Unit value) {
		unit = value.getValue();
	}

	/**
	 * @return Status of COSEM Register object.
	 */
	public final Object getStatus() {
		return status;
	}

	/**
	 * @param value Status of COSEM Register object.
	 */
	public final void setStatus(final Object value) {
		status = value;
	}

	/**
	 * @return Capture time of COSEM Register object.
	 */
	public final GXDateTime getCaptureTime() {
		return captureTime;
	}

	/**
	 * @param value Capture time of COSEM Register object.
	 */
	public final void setCaptureTime(final GXDateTime value) {
		captureTime = value;
	}

	/**
	 * @return Current start time of COSEM Register object.
	 */
	public final GXDateTime getStartTimeCurrent() {
		return startTimeCurrent;
	}

	/**
	 * @param value Current start time of COSEM Register object.
	 */
	public final void setStartTimeCurrent(final GXDateTime value) {
		startTimeCurrent = value;
	}

	public final long getPeriod() {
		return period;
	}

	public final void setPeriod(final long value) {
		period = value;
	}

	public final long getNumberOfPeriods() {
		return numberOfPeriods;
	}

	public final void setNumberOfPeriods(final long value) {
		numberOfPeriods = value;
	}

	/**
	 * Reset value.
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
		return client.method(getName(), getObjectType(), 1, 0, DataType.INT8);
	}

	/**
	 * Next period.
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
	public final byte[][] nextPeriod(final GXDLMSClient client) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(getName(), getObjectType(), 2, 0, DataType.INT8);
	}

	@Override
	public final Object[] getValues() {
		String str = "Scaler: " + String.valueOf(getScaler()) + " Unit: " + getUnit().toString();
		return new Object[] { getLogicalName(), getCurrentAverageValue(), getLastAverageValue(), str, getStatus(),
				getCaptureTime(), getStartTimeCurrent(), getPeriod(), getNumberOfPeriods() };
	}

	@Override
	public final boolean isRead(final int index) {
		if (index == 4) {
			return unit != 0;
		}
		return super.isRead(index);
	}

	@Override
	public final byte[] invoke(final GXDLMSSettings settings, final ValueEventArgs e) {
		// Resets the value to the default value.
		// The default value is an instance specific constant.
		if (e.getIndex() == 1) {
			currentAverageValue = lastAverageValue = null;
			captureTime = new GXDateTime(Calendar.getInstance());
			startTimeCurrent = new GXDateTime(Calendar.getInstance());
		} else if (e.getIndex() == 2) {
			lastAverageValue = currentAverageValue;
			currentAverageValue = null;
			captureTime = new GXDateTime(Calendar.getInstance());
			startTimeCurrent = new GXDateTime(Calendar.getInstance());
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
		// ScalerUnit
		if (all || !isRead(4)) {
			attributes.add(4);
		}
		// CurrentAvarageValue
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// LastAvarageValue
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// Status
		if (all || canRead(5)) {
			attributes.add(5);
		}
		// CaptureTime
		if (all || canRead(6)) {
			attributes.add(6);
		}
		// StartTimeCurrent
		if (all || canRead(7)) {
			attributes.add(7);
		}
		// Period
		if (all || canRead(8)) {
			attributes.add(8);
		}
		// NumberOfPeriods
		if (all || canRead(9)) {
			attributes.add(9);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 9;
	}

	/*
	 * Returns amount of methods.
	 */
	@Override
	public final int getMethodCount() {
		return 2;
	}

	@Override
	public final DataType getDataType(final int index) {
		if (index == 1) {
			return DataType.OCTET_STRING;
		}
		if (index == 2) {
			return super.getDataType(index);
		}
		if (index == 3) {
			return super.getDataType(index);
		}
		if (index == 4) {
			return DataType.ARRAY;
		}
		if (index == 5) {
			return super.getDataType(index);
		}
		if (index == 6) {
			return DataType.OCTET_STRING;
		}
		if (index == 7) {
			return DataType.OCTET_STRING;
		}
		if (index == 8) {
			return DataType.UINT32;
		}
		if (index == 9) {
			return DataType.UINT16;
		}
		throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
	}

	/*
	 * Returns value of given attribute.
	 */
	@Override
	public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			return GXCommon.logicalNameToBytes(getLogicalName());
		}
		if (e.getIndex() == 2) {
			if (settings != null && !settings.isServer() && getScaler() != 1 && currentAverageValue != null) {
				DataType type = GXDLMSConverter.getDLMSDataType(currentAverageValue);
				Object tmp = ((Number) currentAverageValue).doubleValue() / getScaler();
				if (type != DataType.NONE) {
					tmp = GXDLMSConverter.changeType(tmp, type);
				}
				return tmp;
			}
			return currentAverageValue;
		}
		if (e.getIndex() == 3) {
			if (settings != null && !settings.isServer() && getScaler() != 1 && lastAverageValue != null) {
				DataType type = GXDLMSConverter.getDLMSDataType(lastAverageValue);
				Object tmp = ((Number) lastAverageValue).doubleValue() / getScaler();
				if (type != DataType.NONE) {
					tmp = GXDLMSConverter.changeType(tmp, type);
				}
				return tmp;
			}
			return lastAverageValue;
		}
		if (e.getIndex() == 4) {
			GXByteBuffer data = new GXByteBuffer();
			data.setUInt8(DataType.STRUCTURE.getValue());
			data.setUInt8(2);
			GXCommon.setData(settings, data, DataType.INT8, scaler);
			GXCommon.setData(settings, data, DataType.ENUM, unit);
			return data.array();
		}
		if (e.getIndex() == 5) {
			return getStatus();
		}
		if (e.getIndex() == 6) {
			return getCaptureTime();
		}
		if (e.getIndex() == 7) {
			return getStartTimeCurrent();
		}
		if (e.getIndex() == 8) {
			return getPeriod();
		}
		if (e.getIndex() == 9) {
			return getNumberOfPeriods();
		}
		e.setError(ErrorCode.READ_WRITE_DENIED);
		return null;
	}

	/*
	 * Set value of given attribute.
	 */
	@Override
	public final void setValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			setLogicalName(GXCommon.toLogicalName(e.getValue()));
		} else if (e.getIndex() == 2) {
			if (settings != null && !settings.isServer() && getScaler() != 1 && e.getValue() != null) {
				try {
					setCurrentAverageValue(((Number) e.getValue()).doubleValue() * getScaler());
				} catch (Exception e1) {
					// Sometimes scaler is set for wrong Object type.
					setCurrentAverageValue(e.getValue());
				}
			} else {
				setCurrentAverageValue(e.getValue());
			}
		} else if (e.getIndex() == 3) {
			if (settings != null && !settings.isServer() && getScaler() != 1 && e.getValue() != null) {
				try {
					setLastAverageValue(((Number) e.getValue()).doubleValue() * getScaler());
				} catch (Exception e1) {
					// Sometimes scaler is set for wrong Object type.
					setLastAverageValue(e.getValue());
				}
			} else {
				setLastAverageValue(e.getValue());
			}
		} else if (e.getIndex() == 4) {
			// Set default values.
			if (e.getValue() == null) {
				scaler = 0;
				unit = 0;
			} else {
				List<?> arr = (List<?>) e.getValue();
				if (arr.size() != 2) {
					throw new IllegalArgumentException("setValue failed. Invalid scaler unit value.");
				}
				scaler = ((Number) arr.get(0)).intValue();
				unit = (((Number) arr.get(1)).intValue() & 0xFF);
			}
		} else if (e.getIndex() == 5) {
			if (e.getValue() == null) {
				setStatus(null);
			} else {
				setStatus(e.getValue());
			}
		} else if (e.getIndex() == 6) {
			if (e.getValue() == null) {
				setCaptureTime(new GXDateTime());
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
				setCaptureTime(tmp);
			}
		} else if (e.getIndex() == 7) {
			if (e.getValue() == null) {
				setStartTimeCurrent(new GXDateTime());
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
				setStartTimeCurrent(tmp);
			}
		} else if (e.getIndex() == 8) {
			if (e.getValue() == null) {
				setPeriod(0);
			} else {
				setPeriod(((Number) e.getValue()).longValue());
			}
		} else if (e.getIndex() == 9) {
			if (e.getValue() == null) {
				setNumberOfPeriods(0);
			} else {
				setNumberOfPeriods(((Number) e.getValue()).longValue());
			}
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		currentAverageValue = reader.readElementContentAsObject("CurrentAverageValue", null, this, 2);
		lastAverageValue = reader.readElementContentAsObject("LastAverageValue", null, this, 3);
		setScaler(reader.readElementContentAsDouble("Scaler", 1));
		unit = reader.readElementContentAsInt("Unit");
		status = reader.readElementContentAsObject("Status", null, this, 5);
		captureTime = reader.readElementContentAsDateTime("CaptureTime");
		startTimeCurrent = reader.readElementContentAsDateTime("StartTimeCurrent");
		period = reader.readElementContentAsInt("Period");
		numberOfPeriods = reader.readElementContentAsInt("NumberOfPeriods");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementObject("CurrentAverageValue", currentAverageValue);
		writer.writeElementObject("LastAverageValue", lastAverageValue);
		writer.writeElementString("Scaler", getScaler(), 1);
		writer.writeElementString("Unit", unit);
		writer.writeElementObject("Status", status);
		writer.writeElementString("CaptureTime", captureTime);
		writer.writeElementString("StartTimeCurrent", startTimeCurrent);
		writer.writeElementString("Period", period);
		writer.writeElementString("NumberOfPeriods", numberOfPeriods);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
		// Not needed for this object.
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "Current Average Value", "Last Average Value", "Scaler and Unit",
				"Status", "Capture Time", "Start Time Current", "Period", "Number Of Periods" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Reset", "Next period" };
	}
}