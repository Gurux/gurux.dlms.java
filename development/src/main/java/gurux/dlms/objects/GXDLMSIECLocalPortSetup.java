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

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.BaudRate;
import gurux.dlms.objects.enums.LocalPortResponseTime;
import gurux.dlms.objects.enums.OpticalProtocolMode;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSIECLocalPortSetup
 */
public class GXDLMSIECLocalPortSetup extends GXDLMSObject implements IGXDLMSBase {
	private String password1;
	private String password2;
	private String password5;
	private OpticalProtocolMode defaultMode;
	private BaudRate defaultBaudrate;
	private BaudRate proposedBaudrate;
	private LocalPortResponseTime responseTime;
	private String deviceAddress;

	/**
	 * Constructor.
	 */
	public GXDLMSIECLocalPortSetup() {
		this("0.0.20.0.0.255");
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSIECLocalPortSetup(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSIECLocalPortSetup(final String ln, final int sn) {
		super(ObjectType.IEC_LOCAL_PORT_SETUP, ln, sn);
		defaultMode = OpticalProtocolMode.DEFAULT;
		defaultBaudrate = BaudRate.BAUDRATE_300;
		proposedBaudrate = BaudRate.BAUDRATE_300;
		responseTime = LocalPortResponseTime.ms20;
		setVersion(1);
	}

	public final OpticalProtocolMode getDefaultMode() {
		return defaultMode;
	}

	public final void setDefaultMode(final OpticalProtocolMode value) {
		defaultMode = value;
	}

	public final BaudRate getDefaultBaudrate() {
		return defaultBaudrate;
	}

	public final void setDefaultBaudrate(final BaudRate value) {
		defaultBaudrate = value;
	}

	public final BaudRate getProposedBaudrate() {
		return proposedBaudrate;
	}

	public final void setProposedBaudrate(final BaudRate value) {
		proposedBaudrate = value;
	}

	public final LocalPortResponseTime getResponseTime() {
		return responseTime;
	}

	public final void setResponseTime(final LocalPortResponseTime value) {
		responseTime = value;
	}

	public final String getDeviceAddress() {
		return deviceAddress;
	}

	public final void setDeviceAddress(final String value) {
		deviceAddress = value;
	}

	public final String getPassword1() {
		return password1;
	}

	public final void setPassword1(final String value) {
		password1 = value;
	}

	public final String getPassword2() {
		return password2;
	}

	public final void setPassword2(final String value) {
		password2 = value;
	}

	public final String getPassword5() {
		return password5;
	}

	public final void setPassword5(final String value) {
		password5 = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), getDefaultMode(), getDefaultBaudrate(), getProposedBaudrate(),
				getResponseTime(), getDeviceAddress(), getPassword1(), getPassword2(), getPassword5() };
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
		// DefaultMode
		if (all || !isRead(2)) {
			attributes.add(2);
		}
		// DefaultBaudrate
		if (all || !isRead(3)) {
			attributes.add(3);
		}
		// ProposedBaudrate
		if (all || !isRead(4)) {
			attributes.add(4);
		}
		// ResponseTime
		if (all || !isRead(5)) {
			attributes.add(5);
		}
		// DeviceAddress
		if (all || !isRead(6)) {
			attributes.add(6);
		}
		// Password1
		if (all || !isRead(7)) {
			attributes.add(7);
		}
		// Password2
		if (all || !isRead(8)) {
			attributes.add(8);
		}
		// Password5
		if (all || !isRead(9)) {
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
		return 0;
	}

	@Override
	public final DataType getDataType(final int index) {
		if (index == 1) {
			return DataType.OCTET_STRING;
		}
		if (index == 2) {
			return DataType.ENUM;
		}
		if (index == 3) {
			return DataType.ENUM;
		}
		if (index == 4) {
			return DataType.ENUM;
		}
		if (index == 5) {
			return DataType.ENUM;
		}
		if (index == 6) {
			return DataType.OCTET_STRING;
		}
		if (index == 7) {
			return DataType.OCTET_STRING;
		}
		if (index == 8) {
			return DataType.OCTET_STRING;
		}
		if (index == 9) {
			return DataType.OCTET_STRING;
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
			return this.getDefaultMode().getValue();
		}
		if (e.getIndex() == 3) {
			return getDefaultBaudrate().ordinal();
		}
		if (e.getIndex() == 4) {
			return getProposedBaudrate().ordinal();
		}
		if (e.getIndex() == 5) {
			return getResponseTime().ordinal();
		}
		if (e.getIndex() == 6) {
			return GXCommon.getBytes(deviceAddress);
		}
		if (e.getIndex() == 7) {
			return GXCommon.getBytes(password1);
		}
		if (e.getIndex() == 8) {
			return GXCommon.getBytes(password2);
		}
		if (e.getIndex() == 9) {
			return GXCommon.getBytes(password5);
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
			setDefaultMode(OpticalProtocolMode.forValue(((Number) e.getValue()).intValue()));
		} else if (e.getIndex() == 3) {
			setDefaultBaudrate(BaudRate.values()[((Number) e.getValue()).intValue()]);
		} else if (e.getIndex() == 4) {
			setProposedBaudrate(BaudRate.values()[((Number) e.getValue()).intValue()]);
		} else if (e.getIndex() == 5) {
			setResponseTime(LocalPortResponseTime.values()[((Number) e.getValue()).intValue()]);
		} else if (e.getIndex() == 6) {
			setDeviceAddress(GXDLMSClient.changeType((byte[]) e.getValue(), DataType.STRING, false).toString());
		} else if (e.getIndex() == 7) {
			setPassword1(GXDLMSClient.changeType((byte[]) e.getValue(), DataType.STRING, false).toString());
		} else if (e.getIndex() == 8) {
			setPassword2(GXDLMSClient.changeType((byte[]) e.getValue(), DataType.STRING, false).toString());
		} else if (e.getIndex() == 9) {
			setPassword5(GXDLMSClient.changeType((byte[]) e.getValue(), DataType.STRING, false).toString());
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		defaultMode = OpticalProtocolMode.forValue(reader.readElementContentAsInt("DefaultMode"));
		defaultBaudrate = BaudRate.values()[reader.readElementContentAsInt("DefaultBaudrate")];
		proposedBaudrate = BaudRate.values()[reader.readElementContentAsInt("ProposedBaudrate")];
		responseTime = LocalPortResponseTime.forValue(reader.readElementContentAsInt("ResponseTime"));
		deviceAddress = reader.readElementContentAsString("DeviceAddress");
		password1 = reader.readElementContentAsString("Password1");
		password2 = reader.readElementContentAsString("Password2");
		password5 = reader.readElementContentAsString("Password5");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("DefaultMode", defaultMode.getValue());
		writer.writeElementString("DefaultBaudrate", defaultBaudrate.ordinal());
		writer.writeElementString("ProposedBaudrate", proposedBaudrate.ordinal());
		writer.writeElementString("ResponseTime", responseTime.getValue());
		writer.writeElementString("DeviceAddress", deviceAddress);
		writer.writeElementString("Password1", password1);
		writer.writeElementString("Password2", password2);
		writer.writeElementString("Password5", password5);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
		// Not needed for this object.
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "Default Mode", "Default Baud rate", "Proposed Baud rate",
				"Response Time", "Device Address", "Password 1", "Password 2", "Password 5" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}
}