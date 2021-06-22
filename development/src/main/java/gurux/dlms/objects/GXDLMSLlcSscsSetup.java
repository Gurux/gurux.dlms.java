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
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSLlcSscsSetup
 */
public class GXDLMSLlcSscsSetup extends GXDLMSObject implements IGXDLMSBase {
	private int serviceNodeAddress;
	private int baseNodeAddress;

	/**
	 * Constructor.
	 */
	public GXDLMSLlcSscsSetup() {
		this("0.0.28.0.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSLlcSscsSetup(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSLlcSscsSetup(final String ln, final int sn) {
		super(ObjectType.LLC_SSCS_SETUP, ln, sn);
	}

	/**
	 * @return Address assigned to the service node during its registration by the
	 *         base node.
	 */
	public final int getServiceNodeAddress() {
		return serviceNodeAddress;
	}

	/**
	 * @param value Address assigned to the service node during its registration by
	 *              the base node.
	 */
	public final void setServiceNodeAddress(final int value) {
		serviceNodeAddress = value;
	}

	/**
	 * @return Base node address to which the service node is registered.
	 */
	public final int getBaseNodeAddress() {
		return baseNodeAddress;
	}

	/**
	 * @param value Base node address to which the service node is registered.
	 */
	public final void setBaseNodeAddress(final int value) {
		baseNodeAddress = value;
	}

	/**
	 * Deallocating the service node address. The value of the ServiceNodeAddress
	 * becomes NEW and the value of the BaseNodeAddress becomes 0.
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
	public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
		if (e.getIndex() == 1) {
			serviceNodeAddress = 0xFFE;
			baseNodeAddress = 0;
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
		return null;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), serviceNodeAddress, baseNodeAddress };
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
		// ServiceNodeAddress
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// BaseNodeAddress
		if (all || canRead(3)) {
			attributes.add(3);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 3;
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
			return DataType.UINT16;
		default:
			throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
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
			return serviceNodeAddress;
		case 3:
			return baseNodeAddress;
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
		if (e.getIndex() == 1) {
			setLogicalName(GXCommon.toLogicalName(e.getValue()));
		} else if (e.getIndex() == 2) {
			serviceNodeAddress = ((Number) e.getValue()).intValue();
		} else if (e.getIndex() == 3) {
			baseNodeAddress = ((Number) e.getValue()).intValue();
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		serviceNodeAddress = reader.readElementContentAsInt("ServiceNodeAddress");
		baseNodeAddress = reader.readElementContentAsInt("BaseNodeAddress");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("ServiceNodeAddress", serviceNodeAddress);
		writer.writeElementString("BaseNodeAddress", baseNodeAddress);

	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "ServiceNodeAddress", "BaseNodeAddress" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Reset" };
	}
}