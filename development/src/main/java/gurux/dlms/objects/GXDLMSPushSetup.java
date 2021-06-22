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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.MessageType;
import gurux.dlms.objects.enums.ServiceType;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPushSetup
 */
public class GXDLMSPushSetup extends GXDLMSObject implements IGXDLMSBase {
	private ServiceType service;
	private String destination;
	private MessageType message;

	private List<Entry<GXDLMSObject, GXDLMSCaptureObject>> pushObjectList;
	private List<Map.Entry<GXDateTime, GXDateTime>> communicationWindow;
	private int randomisationStartInterval;
	private int numberOfRetries;
	private int repetitionDelay;

	/**
	 * Constructor.
	 */
	public GXDLMSPushSetup() {
		this("0.7.25.9.0.255");
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSPushSetup(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSPushSetup(final String ln, final int sn) {
		super(ObjectType.PUSH_SETUP, ln, sn);
		pushObjectList = new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
		communicationWindow = new ArrayList<Map.Entry<GXDateTime, GXDateTime>>();
		service = ServiceType.TCP;
		message = MessageType.COSEM_APDU;
	}

	public final ServiceType getService() {
		return service;
	}

	public final void setService(final ServiceType value) {
		service = value;
	}

	public final String getDestination() {
		return destination;
	}

	public final void setDestination(final String value) {
		destination = null;
	}

	public final MessageType getMessage() {
		return message;
	}

	public final void setMessage(final MessageType value) {
		message = value;
	}

	/**
	 * @return Defines the list of attributes or objects to be pushed. Upon a call
	 *         of the push (data) method the selected attributes are sent to the
	 *         destination defined in getSendDestinationAndMethod.
	 */
	public final List<Entry<GXDLMSObject, GXDLMSCaptureObject>> getPushObjectList() {
		return pushObjectList;
	}

	/**
	 * @return Contains the start and end date/time stamp when the communication
	 *         window(s) for the push become active (for the start instant), or
	 *         inactive (for the end instant).
	 */
	public final List<Map.Entry<GXDateTime, GXDateTime>> getCommunicationWindow() {
		return communicationWindow;
	}

	/**
	 * @return To avoid simultaneous network connections of a lot of devices at
	 *         ex-actly the same point in time, a randomisation interval in seconds
	 *         can be defined. This means that the push operation is not started
	 *         imme-diately at the beginning of the first communication window but
	 *         started randomly delayed.
	 */
	public final int getRandomisationStartInterval() {
		return randomisationStartInterval;
	}

	public final void setRandomisationStartInterval(final int value) {
		randomisationStartInterval = value;
	}

	/**
	 * @return The maximum number of re-trials in case of unsuccessful push
	 *         attempts. After a successful push no further push attempts are made
	 *         until the push setup is triggered again. A value of 0 means no
	 *         repetitions, i.e. only the initial connection attempt is made.
	 */
	public final int getNumberOfRetries() {
		return numberOfRetries;
	}

	/**
	 * @param value The maximum number of re-trials in case of unsuccessful push
	 *              attempts. After a successful push no further push attempts are
	 *              made until the push setup is triggered again. A value of 0 means
	 *              no repetitions, i.e. only the initial connection attempt is
	 *              made.
	 */
	public final void setNumberOfRetries(final byte value) {
		numberOfRetries = value;
	}

	public final int getRepetitionDelay() {
		return repetitionDelay;
	}

	public final void setRepetitionDelay(final int value) {
		repetitionDelay = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), pushObjectList, service + " " + destination + " " + message,
				communicationWindow, randomisationStartInterval, numberOfRetries, repetitionDelay };
	}

	/**
	 * Get received objects from push message.
	 * 
	 * @param client GXDLMSClient used to update the values.
	 * @param values Received values.
	 */
	public void getPushValues(final GXDLMSClient client, final List<?> values) {
		if (values.size() != pushObjectList.size()) {
			throw new IllegalArgumentException("Size of the push object list is different than values.");
		}
		int pos = 0;
		List<Entry<GXDLMSObject, GXDLMSCaptureObject>> objects = new ArrayList<Entry<GXDLMSObject, GXDLMSCaptureObject>>();
		for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pushObjectList) {
			GXDLMSObject obj = (GXDLMSObject) it.getKey();
			objects.add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj,
					new GXDLMSCaptureObject(it.getValue().getAttributeIndex(), it.getValue().getDataIndex())));
			client.updateValue(obj, it.getValue().getAttributeIndex(), values.get(pos));
			++pos;
		}
	}

	@Override
	public final byte[] invoke(final GXDLMSSettings settings, final ValueEventArgs e) {
		if (e.getIndex() != 1) {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
		return null;
	}

	/*
	 * Activates the push process.
	 */
	public final byte[][] activate(final GXDLMSClient client) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return client.method(getName(), getObjectType(), 1, 0, DataType.INT8);
	}

	@Override
	public final int[] getAttributeIndexToRead(final boolean all) {
		java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
		// LN is static and read only once.
		if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
			attributes.add(1);
		}
		// PushObjectList
		if (all || canRead(2)) {
			attributes.add(2);
		}
		// SendDestinationAndMethod
		if (all || canRead(3)) {
			attributes.add(3);
		}
		// CommunicationWindow
		if (all || canRead(4)) {
			attributes.add(4);
		}
		// RandomisationStartInterval
		if (all || canRead(5)) {
			attributes.add(5);
		}
		// NumberOfRetries
		if (all || canRead(6)) {
			attributes.add(6);
		}
		// RepetitionDelay
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
		if (index == 1) {
			return DataType.OCTET_STRING;
		}
		if (index == 2) {
			return DataType.ARRAY;
		}
		if (index == 3) {
			return DataType.STRUCTURE;
		}
		if (index == 4) {
			return DataType.ARRAY;
		}
		if (index == 5) {
			return DataType.UINT16;
		}
		if (index == 6) {
			return DataType.UINT8;
		}
		if (index == 7) {
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
		GXByteBuffer buff = new GXByteBuffer();
		if (e.getIndex() == 2) {
			buff.setUInt8(DataType.ARRAY.getValue());
			GXCommon.setObjectCount(pushObjectList.size(), buff);
			for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pushObjectList) {
				buff.setUInt8(DataType.STRUCTURE.getValue());
				buff.setUInt8(4);
				GXCommon.setData(settings, buff, DataType.UINT16, it.getKey().getObjectType().getValue());
				GXCommon.setData(settings, buff, DataType.OCTET_STRING,
						GXCommon.logicalNameToBytes(it.getKey().getLogicalName()));
				GXCommon.setData(settings, buff, DataType.INT8, it.getValue().getAttributeIndex());
				GXCommon.setData(settings, buff, DataType.UINT16, it.getValue().getDataIndex());
			}
			return buff.array();
		}
		if (e.getIndex() == 3) {
			buff.setUInt8(DataType.STRUCTURE.getValue());
			buff.setUInt8(3);
			GXCommon.setData(settings, buff, DataType.ENUM, getService().getValue());
			byte[] tmp = null;
			if (GXCommon.isHexString(destination)) {
				tmp = GXCommon.hexToBytes(destination);
			} else if (destination != null) {
				tmp = getDestination().getBytes();
			}
			GXCommon.setData(settings, buff, DataType.OCTET_STRING, tmp);
			GXCommon.setData(settings, buff, DataType.ENUM, getMessage().getValue());
			return buff.array();
		}
		if (e.getIndex() == 4) {
			buff.setUInt8(DataType.ARRAY.getValue());
			GXCommon.setObjectCount(communicationWindow.size(), buff);
			for (Entry<GXDateTime, GXDateTime> it : communicationWindow) {
				buff.setUInt8(DataType.STRUCTURE.getValue());
				buff.setUInt8(2);
				GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getKey());
				GXCommon.setData(settings, buff, DataType.OCTET_STRING, it.getValue());
			}
			return buff.array();
		}
		if (e.getIndex() == 5) {
			return randomisationStartInterval;
		}
		if (e.getIndex() == 6) {
			return numberOfRetries;
		}
		if (e.getIndex() == 7) {
			return repetitionDelay;
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
			pushObjectList.clear();
			Entry<GXDLMSObject, GXDLMSCaptureObject> ent;
			if (e.getValue() instanceof List<?>) {
				for (Object it : (List<?>) e.getValue()) {
					List<?> tmp = (List<?>) it;
					ObjectType type = ObjectType.forValue(((Number) tmp.get(0)).intValue());
					String ln = GXCommon.toLogicalName(tmp.get(1));
					GXDLMSObject obj = settings.getObjects().findByLN(type, ln);
					if (obj == null) {
						obj = gurux.dlms.GXDLMSClient.createObject(type);
						obj.setLogicalName(ln);
					}
					GXDLMSCaptureObject co = new GXDLMSCaptureObject();
					co.setAttributeIndex(((Number) tmp.get(2)).intValue());
					co.setDataIndex(((Number) tmp.get(3)).intValue());
					ent = new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj, co);
					pushObjectList.add(ent);
				}
			}
		} else if (e.getIndex() == 3) {
			List<?> tmp = (List<?>) e.getValue();
			if (tmp != null) {
				setService(ServiceType.forValue(((Number) tmp.get(0)).intValue()));
				byte[] tmp2 = (byte[]) tmp.get(1);
				if (GXByteBuffer.isAsciiString(tmp2)) {
					setDestination(new String(tmp2));
				} else {
					setDestination(GXCommon.toHex(tmp2, false));
				}
				setMessage(MessageType.forValue(((Number) tmp.get(2)).intValue()));
			}
		} else if (e.getIndex() == 4) {
			communicationWindow.clear();
			if (e.getValue() instanceof List<?>) {
				boolean useUtc;
				if (e.getSettings() != null) {
					useUtc = e.getSettings().getUseUtc2NormalTime();
				} else {
					useUtc = false;
				}
				for (Object it : (List<?>) e.getValue()) {
					List<?> tmp = (List<?>) it;
					GXDateTime start = (GXDateTime) GXDLMSClient.changeType((byte[]) tmp.get(0), DataType.DATETIME,
							useUtc);
					GXDateTime end = (GXDateTime) GXDLMSClient.changeType((byte[]) tmp.get(1), DataType.DATETIME,
							useUtc);
					communicationWindow.add(new GXSimpleEntry<GXDateTime, GXDateTime>(start, end));
				}
			}
		} else if (e.getIndex() == 5) {
			randomisationStartInterval = ((Number) e.getValue()).intValue();
		} else if (e.getIndex() == 6) {
			numberOfRetries = ((Number) e.getValue()).intValue();
		} else if (e.getIndex() == 7) {
			repetitionDelay = ((Number) e.getValue()).intValue();
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		pushObjectList.clear();
		if (reader.isStartElement("ObjectList", true)) {
			while (reader.isStartElement("Item", true)) {
				ObjectType ot = ObjectType.forValue(reader.readElementContentAsInt("ObjectType"));
				String ln = reader.readElementContentAsString("LN");
				int ai = reader.readElementContentAsInt("AI");
				int di = reader.readElementContentAsInt("DI");
				reader.readEndElement("ObjectList");
				GXDLMSCaptureObject co = new GXDLMSCaptureObject(ai, di);
				GXDLMSObject obj = reader.getObjects().findByLN(ot, ln);
				if (obj == null) {
					obj = GXDLMSClient.createObject(ot);
					obj.setLogicalName(ln);
				}
				pushObjectList.add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(obj, co));
			}
			reader.readEndElement("ObjectList");
		}

		service = ServiceType.forValue(reader.readElementContentAsInt("Service"));
		destination = reader.readElementContentAsString("Destination");
		message = MessageType.forValue(reader.readElementContentAsInt("Message"));
		communicationWindow.clear();
		if (reader.isStartElement("CommunicationWindow", true)) {
			while (reader.isStartElement("Item", true)) {
				GXDateTime start = reader.readElementContentAsDateTime("Start");
				GXDateTime end = reader.readElementContentAsDateTime("End");
				communicationWindow.add(new GXSimpleEntry<GXDateTime, GXDateTime>(start, end));
			}
			reader.readEndElement("CommunicationWindow");
		}
		randomisationStartInterval = reader.readElementContentAsInt("RandomisationStartInterval");
		numberOfRetries = reader.readElementContentAsInt("NumberOfRetries");
		repetitionDelay = reader.readElementContentAsInt("RepetitionDelay");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		if (pushObjectList != null) {
			writer.writeStartElement("ObjectList");
			for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pushObjectList) {
				writer.writeStartElement("Item");
				writer.writeElementString("ObjectType", it.getKey().getObjectType().getValue());
				writer.writeElementString("LN", it.getKey().getLogicalName());
				writer.writeElementString("AI", it.getValue().getAttributeIndex());
				writer.writeElementString("DI", it.getValue().getDataIndex());
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
		if (service != null) {
			writer.writeElementString("Service", service.getValue());
		}
		writer.writeElementString("Destination", destination);
		if (message != null) {
			writer.writeElementString("Message", message.getValue());
		}
		if (communicationWindow != null) {
			writer.writeStartElement("CommunicationWindow");
			for (Entry<GXDateTime, GXDateTime> it : communicationWindow) {
				writer.writeStartElement("Item");
				writer.writeElementString("Start", it.getKey());
				writer.writeElementString("End", it.getValue());
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
		writer.writeElementString("RandomisationStartInterval", randomisationStartInterval);
		writer.writeElementString("NumberOfRetries", numberOfRetries);
		writer.writeElementString("RepetitionDelay", repetitionDelay);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
		// Not needed for this object.
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "Object List", "Send Destination And Method", "Communication Window",
				"Randomisation Start Interval", "Number Of Retries", "Repetition Delay" };

	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Push" };
	}
}