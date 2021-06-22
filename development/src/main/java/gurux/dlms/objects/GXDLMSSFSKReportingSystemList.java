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

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSSFSKReportingSystemList
 */
public class GXDLMSSFSKReportingSystemList extends GXDLMSObject implements IGXDLMSBase {

	/**
	 * Contains the system titles of the server systems which have made a
	 * DiscoverReport request and which have not already been registered.
	 */
	private ArrayList<byte[]> reportingSystemList;

	/**
	 * Constructor.
	 */
	public GXDLMSSFSKReportingSystemList() {
		this("0.0.26.6.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSSFSKReportingSystemList(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSSFSKReportingSystemList(final String ln, final int sn) {
		super(ObjectType.SFSK_REPORTING_SYSTEM_LIST, ln, sn);
		reportingSystemList = new ArrayList<byte[]>();
	}

	/**
	 * @return Contains the system titles of the server systems which have made a
	 *         DiscoverReport request and which have not already been registered.
	 */
	public final ArrayList<byte[]> getReportingSystemList() {
		return reportingSystemList;
	}

	/**
	 * @param value Contains the system titles of the server systems which have made
	 *              a DiscoverReport request and which have not already been
	 *              registered.
	 */
	public final void setReportingSystemList(final java.util.ArrayList<byte[]> value) {
		reportingSystemList = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), reportingSystemList };
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
		// ReportingSystemList
		if (all || canRead(2)) {
			attributes.add(2);
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */
	@Override
	public final int getAttributeCount() {
		return 2;
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
			return DataType.ARRAY;
		}
		throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
	}

	/*
	 * Returns value of given attribute.
	 */
	@Override
	public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		switch (e.getIndex()) {
		case 1:
			return GXCommon.logicalNameToBytes(getLogicalName());
		case 2: {
			int cnt = reportingSystemList.size();
			GXByteBuffer data = new GXByteBuffer();
			data.setUInt8(DataType.ARRAY);
			// Add count
			GXCommon.setObjectCount(cnt, data);
			for (byte[] it : reportingSystemList) {
				GXCommon.setData(settings, data, DataType.OCTET_STRING, it);
			}
			return data.array();
		}
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
		case 2: {
			reportingSystemList.clear();
			if (e.getValue() != null) {
				for (Object tmp : (List<?>) e.getValue()) {
					reportingSystemList.add((byte[]) tmp);
				}
			}
		}
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		reportingSystemList.clear();
		if (reader.isStartElement("ReportingSystems", true)) {
			while (reader.isStartElement("Item", false)) {
				reportingSystemList.add(GXCommon.hexToBytes(reader.readElementContentAsString("Item")));
			}
		}
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		writer.writeStartElement("ReportingSystems");
		for (byte[] it : reportingSystemList) {
			writer.writeElementString("Item", GXCommon.toHex(it, false));
		}
		writer.writeEndElement();
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "ReportingSystemList" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}
}