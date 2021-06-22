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
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPrimeNbOfdmPlcApplicationsIdentification
 */
public class GXDLMSPrimeNbOfdmPlcApplicationsIdentification extends GXDLMSObject implements IGXDLMSBase {
	/**
	 * Textual description of the firmware version running on the device.
	 */
	private String firmwareVersion;

	/**
	 * Unique vendor identifier assigned by PRIME Alliance.
	 */
	private int vendorId;
	/**
	 * Vendor assigned unique identifier for specific product.
	 */
	private int productId;

	/**
	 * Constructor.
	 */
	public GXDLMSPrimeNbOfdmPlcApplicationsIdentification() {
		this("0.0.28.7.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSPrimeNbOfdmPlcApplicationsIdentification(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSPrimeNbOfdmPlcApplicationsIdentification(final String ln, final int sn) {
		super(ObjectType.PRIME_NB_OFDM_PLC_APPLICATIONS_IDENTIFICATION, ln, sn);
	}

	/**
	 * @return Textual description of the firmware version running on the device.
	 */
	public final String getFirmwareVersion() {
		return firmwareVersion;
	}

	/**
	 * @param value Textual description of the firmware version running on the
	 *              device.
	 */
	public final void setFirmwareVersion(final String value) {
		firmwareVersion = value;
	}

	/**
	 * @return Unique vendor identifier assigned by PRIME Alliance.
	 */
	public final int getVendorId() {
		return vendorId;
	}

	/**
	 * @param value Unique vendor identifier assigned by PRIME Alliance.
	 */
	public final void setVendorId(final int value) {
		vendorId = value;
	}

	/**
	 * @return Vendor assigned unique identifier for specific product.
	 */
	public final int getProductId() {
		return productId;
	}

	/**
	 * @param value Vendor assigned unique identifier for specific product.
	 */
	public final void setProductId(final int value) {
		productId = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), firmwareVersion, vendorId, productId };
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
		// FirmwareVersion
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// VendorId
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// ProductId
		if (all || canRead(4)) {
			attributes.add(4);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 4;
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
		case 2:
			return DataType.OCTET_STRING;
		case 3:
		case 4:
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
		switch (e.getIndex()) {
		case 1:
			return GXCommon.logicalNameToBytes(getLogicalName());
		case 2:
			if (firmwareVersion != null) {
				return firmwareVersion.getBytes();
			}
			break;
		case 3:
			return vendorId;
		case 4:
			return productId;
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
			firmwareVersion = new String((byte[]) e.getValue());
			break;
		case 3:
			vendorId = ((Number) e.getValue()).intValue();
			break;
		case 4:
			productId = ((Number) e.getValue()).intValue();
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		firmwareVersion = reader.readElementContentAsString("FirmwareVersion");
		vendorId = reader.readElementContentAsInt("VendorId");
		productId = reader.readElementContentAsInt("ProductId");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeElementString("FirmwareVersion", firmwareVersion);
		writer.writeElementString("VendorId", vendorId);
		writer.writeElementString("ProductId", productId);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "FirmwareVersion", "VendorId", "ProductId" };

	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}
}