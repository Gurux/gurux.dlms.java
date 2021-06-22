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
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.enums.CaptureMethod;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCompactData
 */
public class GXDLMSCompactData extends GXDLMSObject implements IGXDLMSBase {
	/**
	 * Compact buffer
	 */
	private byte[] buffer;

	/**
	 * Capture objects.
	 */
	private List<Entry<GXDLMSObject, GXDLMSCaptureObject>> captureObjects;

	/**
	 * Template ID.
	 */
	private short templateId;

	/**
	 * Template description.
	 */
	private byte[] templateDescription;

	/**
	 * Capture method.
	 */
	private CaptureMethod captureMethod;

	/**
	 * Constructor.
	 */
	public GXDLMSCompactData() {
		this("0.0.66.0.1.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSCompactData(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSCompactData(final String ln, final int sn) {
		super(ObjectType.COMPACT_DATA, ln, sn);
		captureMethod = CaptureMethod.INVOKE;
		captureObjects = new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
	}

	/**
	 * @return Compact buffer
	 */
	public final byte[] getBuffer() {
		return buffer;
	}

	/**
	 * @param value Compact buffer
	 */
	public final void setBuffer(final byte[] value) {
		buffer = value;
	}

	/**
	 * @return Capture objects.
	 */
	public final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> getCaptureObjects() {
		return captureObjects;
	}

	/**
	 * @return Template ID.
	 */
	public final short getTemplateId() {
		return templateId;
	}

	/**
	 * @param value Template ID.
	 */
	public final void setTemplateId(final short value) {
		templateId = value;
	}

	/**
	 * @return Template description.
	 */
	public final byte[] getTemplateDescription() {
		return templateDescription;
	}

	/**
	 * @param value Template description.
	 */
	public final void setTemplateDescription(final byte[] value) {
		templateDescription = value;
	}

	/**
	 * @return Capture method.
	 */
	public final CaptureMethod getCaptureMethod() {
		return captureMethod;
	}

	/**
	 * @param value Capture method.
	 */
	public final void setCaptureMethod(final CaptureMethod value) {
		captureMethod = value;
	}

	/**
	 * Clears the buffer.
	 */
	public void reset() {
		synchronized (this) {
			buffer = null;
		}
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), buffer, captureObjects, templateId, templateDescription,
				captureMethod };
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
		// Buffer
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// CaptureObjects
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// TemplateId
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// TemplateDescription
		if (all || canRead(5)) {
			attributes.add(5);
		}
		// CaptureMethod
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
		return 2;
	}

	@Override
	public final DataType getDataType(final int index) {
		switch (index) {
		case 1:
			return DataType.OCTET_STRING;
		case 2:
			return DataType.OCTET_STRING;
		case 3:
			return DataType.ARRAY;
		case 4:
			return DataType.UINT8;
		case 5:
			return DataType.OCTET_STRING;
		case 6:
			return DataType.ENUM;
		default:
			throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
		}
	}

	@Override
	public final byte[] invoke(final GXDLMSSettings settings, final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			// Reset.
			reset();
		} else if (e.getIndex() == 2) {
			// Capture.
			try {
				capture(e.getServer());
			} catch (Exception e1) {
				e.setError(ErrorCode.READ_WRITE_DENIED);
			}
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
		return null;
	}

	/**
	 * Returns captured objects.
	 * 
	 * @param settings DLMS settings.
	 * @return
	 */
	private byte[] getCaptureObjects(GXDLMSSettings settings) {
		GXByteBuffer data = new GXByteBuffer();
		data.setUInt8(DataType.ARRAY.getValue());
		// Add count
		GXCommon.setObjectCount(captureObjects.size(), data);
		for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
			data.setUInt8(DataType.STRUCTURE.getValue());
			// Count
			data.setUInt8(4);
			// ClassID
			GXCommon.setData(settings, data, DataType.UINT16, it.getKey().getObjectType().getValue());
			// LN
			GXCommon.setData(settings, data, DataType.OCTET_STRING,
					GXCommon.logicalNameToBytes(it.getKey().getLogicalName()));
			// Selected Attribute Index
			GXCommon.setData(settings, data, DataType.INT8, it.getValue().getAttributeIndex());
			// Selected Data Index
			GXCommon.setData(settings, data, DataType.UINT16, it.getValue().getDataIndex());
		}
		return data.array();
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
			return buffer;
		case 3:
			return getCaptureObjects(settings);
		case 4:
			return templateId;
		case 5:
			return templateDescription;
		case 6:
			return captureMethod.ordinal();
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
		return null;
	}

	private static void setCaptureObjects(GXDLMSSettings settings, List<Entry<GXDLMSObject, GXDLMSCaptureObject>> list,
			List<?> array) {
		GXDLMSConverter c = null;
		list.clear();
		try {
			if (array != null) {
				for (Object it : array) {
					List<?> tmp = (List<?>) it;
					if (tmp.size() != 4) {
						throw new GXDLMSException("Invalid structure format.");
					}
					int v = ((Number) tmp.get(0)).intValue();
					ObjectType type = ObjectType.forValue(v);
					String ln = GXCommon.toLogicalName((byte[]) tmp.get(1));
					int attributeIndex = ((Number) tmp.get(2)).intValue();
					int dataIndex = ((Number) tmp.get(3)).intValue();
					GXDLMSObject obj = null;
					if (settings != null && settings.getObjects() != null) {
						obj = settings.getObjects().findByLN(type, ln);
					}
					if (obj == null) {
						obj = gurux.dlms.GXDLMSClient.createObject(type);
						if (c == null) {
							c = new GXDLMSConverter();
						}
						c.updateOBISCodeInformation(obj);
					}
					list.add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj,
							new GXDLMSCaptureObject(attributeIndex, dataIndex)));
				}
			}
		} catch (RuntimeException e) {
			list.clear();
		}
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
			if (e.getValue() instanceof byte[]) {
				buffer = (byte[]) e.getValue();
			} else if (e.getValue() instanceof String) {
				buffer = GXCommon.hexToBytes((String) e.getValue());
			}
			break;
		case 3:
			setCaptureObjects(settings, captureObjects, (List<?>) e.getValue());
			if (settings.isServer()) {
				updateTemplateDescription();
			}
			break;
		case 4:
			templateId = ((Number) e.getValue()).shortValue();
			break;
		case 5:
			templateDescription = (byte[]) e.getValue();
			break;
		case 6:
			captureMethod = CaptureMethod.values()[((Number) e.getValue()).shortValue()];
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	private static void captureArray(final GXDLMSServerBase server, GXByteBuffer tmp, GXByteBuffer bb, int index) {
		// Skip type.
		tmp.getUInt8();
		int cnt = GXCommon.getObjectCount(tmp);
		for (int pos = 0; pos != cnt; ++pos) {
			if (index == -1 || index == pos) {
				DataType dt = DataType.forValue(tmp.getUInt8(tmp.position()));
				if (dt == DataType.STRUCTURE || dt == DataType.ARRAY) {
					captureArray(server, tmp, bb, -1);
				} else {
					captureValue(server, tmp, bb);
				}
				if (index == pos) {
					break;
				}
			}
		}
	}

	private static void captureValue(final GXDLMSServerBase server, final GXByteBuffer tmp, final GXByteBuffer bb) {
		GXByteBuffer tmp2 = new GXByteBuffer();
		GXDataInfo info = new GXDataInfo();
		Object value = GXCommon.getData(server.getSettings(), tmp, info);
		GXCommon.setData(server.getSettings(), tmp2, info.getType(), value);
		// If data is empty.
		if (tmp2.size() == 1) {
			bb.setUInt8(0);
		} else {
			tmp2.position(1);
			bb.set(tmp2);
		}
	}

	/*
	 * Copies the values of the objects to capture into the buffer by reading
	 * capture objects.
	 */
	public final void capture(final Object server) throws Exception {
		synchronized (this) {
			GXDLMSServerBase srv = (GXDLMSServerBase) server;
			GXByteBuffer bb = new GXByteBuffer();
			ValueEventArgs[] args = new ValueEventArgs[] { new ValueEventArgs(srv, this, 2, 0, null) };
			buffer = null;
			srv.notifyPreGet(args);
			if (!args[0].getHandled()) {
				for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
					ValueEventArgs e = new ValueEventArgs(srv, it.getKey(), it.getValue().getAttributeIndex(), 0, null);
					Object value = it.getKey().getValue(srv.getSettings(), e);
					DataType dt = it.getKey().getDataType(it.getValue().getAttributeIndex());
					if ((value instanceof byte[] || value instanceof GXByteBuffer[])
							&& (dt == DataType.STRUCTURE || dt == DataType.ARRAY)) {
						GXByteBuffer tmp;
						if (value instanceof byte[]) {
							tmp = new GXByteBuffer((byte[]) value);
						} else {
							tmp = (GXByteBuffer) value;
						}
						captureArray(srv, tmp, bb, it.getValue().getDataIndex() - 1);
					} else {
						GXByteBuffer tmp = new GXByteBuffer();
						GXCommon.setData(null, tmp, dt, value);
						// If data is empty.
						if (tmp.size() == 1) {
							bb.setUInt8(0);
						} else {
							tmp.position(1);
							bb.set(tmp);
						}
					}
				}
				buffer = bb.array();
			}
			srv.notifyPostGet(args);
			srv.notifyAction(args);
			srv.notifyPostAction(args);
		}
	}

	private static void updateTemplateDescription(final GXByteBuffer columns, final GXByteBuffer data,
			final int index) {
		DataType ch = DataType.forValue(data.getUInt8());
		int count = GXCommon.getObjectCount(data);
		if (index == -1) {
			columns.setUInt8(ch.getValue());
			if (ch == DataType.ARRAY) {
				columns.setUInt16(count);
			} else {
				columns.setUInt8(count);
			}
		}
		GXDataInfo info = new GXDataInfo();
		for (int pos = 0; pos < count; ++pos) {
			// If all data is captured.
			if (index == -1 || pos == index) {
				DataType dt = DataType.forValue(data.getUInt8(data.position()));
				if (dt == DataType.ARRAY || dt == DataType.STRUCTURE) {
					updateTemplateDescription(columns, data, -1);
					if (ch == DataType.ARRAY) {
						break;
					}
				} else {
					info.clear();
					columns.setUInt8(dt.getValue());
					// Get data.
					GXCommon.getData(null, data, info);
				}
				if (index == pos) {
					break;
				}
			}
		}
	}

	/**
	 * Update template description.
	 */
	public final void updateTemplateDescription() {
		synchronized (this) {
			GXByteBuffer bb = new GXByteBuffer();
			buffer = null;
			bb.setUInt8(DataType.STRUCTURE.getValue());
			GXCommon.setObjectCount(captureObjects.size(), bb);
			for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : captureObjects) {
				DataType dt = it.getKey().getDataType(it.getValue().getAttributeIndex());
				if (dt == DataType.ARRAY || dt == DataType.STRUCTURE) {
					ValueEventArgs e = new ValueEventArgs(null, it.getValue().getAttributeIndex(), 0, null);
					GXByteBuffer data = new GXByteBuffer();
					Object v = it.getKey().getValue(null, e);
					if (v instanceof byte[]) {
						data.set((byte[]) v);
					} else {
						data = (GXByteBuffer) v;
					}
					updateTemplateDescription(bb, data, it.getValue().getDataIndex() - 1);
				} else {
					bb.setUInt8(dt.getValue());
				}
			}
			templateDescription = bb.array();
		}
	}

	/**
	 * Convert compact data buffer to array of values.
	 * 
	 * @param templateDescription Template description byte array.
	 * @param buffer              Buffer byte array.
	 * @return Values from byte buffer.
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getValues(final byte[] templateDescription, final byte[] buffer) {
		// If templateDescription or buffer is not given.
		if (templateDescription == null || buffer == null || templateDescription.length == 0 || buffer.length == 0) {
			throw new IllegalArgumentException();
		}
		GXDataInfo info = new GXDataInfo();
		GXByteBuffer data = new GXByteBuffer();
		data.set(templateDescription);
		GXCommon.setObjectCount(buffer.length, data);
		data.set(buffer);
		info.setType(DataType.COMPACT_ARRAY);
		return (List<Object>) GXCommon.getData(null, data, info);
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "Buffer", "CaptureObjects", "TemplateId", "TemplateDescription",
				"CaptureMethod" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Reset", "Capture" };
	}
}