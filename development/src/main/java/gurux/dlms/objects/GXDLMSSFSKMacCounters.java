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

import java.util.List;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSSFSKMacCounters
 */
public class GXDLMSSFSKMacCounters extends GXDLMSObject implements IGXDLMSBase {

	/**
	 * List of synchronization registers.
	 */
	private java.util.ArrayList<java.util.Map.Entry<Integer, Long>> synchronizationRegister;

	private long physicalLayerDesynchronization;
	private long timeOutNotAddressedDesynchronization;

	private long timeOutFrameNotOkDesynchronization;

	private long writeRequestDesynchronization;
	private long wrongInitiatorDesynchronization;
	/**
	 * List of broadcast frames counter.
	 */
	private java.util.ArrayList<java.util.Map.Entry<Integer, Long>> broadcastFramesCounter;
	/**
	 * Repetitions counter.
	 */
	private long repetitionsCounter;
	/**
	 * Transmissions counter.
	 */
	private long transmissionsCounter;

	/**
	 * CRC OK frames counter.
	 */
	private long crcOkFramesCounter;
	/**
	 * CRC NOK frames counter.
	 */
	private long crcNOkFramesCounter;

	/**
	 * Constructor.
	 */
	public GXDLMSSFSKMacCounters() {
		this("0.0.26.3.0.255", 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 */
	public GXDLMSSFSKMacCounters(final String ln) {
		this(ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln Logical Name of the object.
	 * @param sn Short Name of the object.
	 */
	public GXDLMSSFSKMacCounters(final String ln, final int sn) {
		super(ObjectType.SFSK_MAC_COUNTERS, ln, sn);
		synchronizationRegister = new java.util.ArrayList<java.util.Map.Entry<Integer, Long>>();
		broadcastFramesCounter = new java.util.ArrayList<java.util.Map.Entry<Integer, Long>>();
	}

	/**
	 * @return List of synchronization registers.
	 */
	public final java.util.ArrayList<java.util.Map.Entry<Integer, Long>> getSynchronizationRegister() {
		return synchronizationRegister;
	}

	/**
	 * @param value List of synchronization registers.
	 */
	public final void setSynchronizationRegister(final java.util.ArrayList<java.util.Map.Entry<Integer, Long>> value) {
		synchronizationRegister = value;
	}

	public final long getPhysicalLayerDesynchronization() {
		return physicalLayerDesynchronization;
	}

	public final void setPhysicalLayerDesynchronization(long value) {
		physicalLayerDesynchronization = value;
	}

	public final long getTimeOutNotAddressedDesynchronization() {
		return timeOutNotAddressedDesynchronization;
	}

	public final void setTimeOutNotAddressedDesynchronization(long value) {
		timeOutNotAddressedDesynchronization = value;
	}

	public final long getTimeOutFrameNotOkDesynchronization() {
		return timeOutFrameNotOkDesynchronization;
	}

	public final void setTimeOutFrameNotOkDesynchronization(long value) {
		timeOutFrameNotOkDesynchronization = value;
	}

	public final long getWriteRequestDesynchronization() {
		return writeRequestDesynchronization;
	}

	public final void setWriteRequestDesynchronization(long value) {
		writeRequestDesynchronization = value;
	}

	public final long getWrongInitiatorDesynchronization() {
		return wrongInitiatorDesynchronization;
	}

	public final void setWrongInitiatorDesynchronization(long value) {
		wrongInitiatorDesynchronization = value;
	}

	/**
	 * @return List of broadcast frames counter.
	 */
	public final java.util.ArrayList<java.util.Map.Entry<Integer, Long>> getBroadcastFramesCounter() {
		return broadcastFramesCounter;
	}

	/**
	 * @param value List of broadcast frames counter.
	 */
	public final void setBroadcastFramesCounter(java.util.ArrayList<java.util.Map.Entry<Integer, Long>> value) {
		broadcastFramesCounter = value;
	}

	/**
	 * @return Repetitions counter.
	 */
	public final long getRepetitionsCounter() {
		return repetitionsCounter;
	}

	/**
	 * @param value Repetitions counter.
	 */
	public final void setRepetitionsCounter(long value) {
		repetitionsCounter = value;
	}

	/**
	 * @return Transmissions counter.
	 */
	public final long getTransmissionsCounter() {
		return transmissionsCounter;
	}

	/**
	 * @param value Transmissions counter.
	 */
	public final void setTransmissionsCounter(long value) {
		transmissionsCounter = value;
	}

	/**
	 * @return CRC OK frames counter.
	 */
	public final long getCrcOkFramesCounter() {
		return crcOkFramesCounter;
	}

	/**
	 * @param value CRC OK frames counter.
	 */
	public final void setCrcOkFramesCounter(long value) {
		crcOkFramesCounter = value;
	}

	/**
	 * @return CRC NOK frames counter.
	 */
	public final long getCrcNOkFramesCounter() {
		return crcNOkFramesCounter;
	}

	/**
	 * @param value CRC NOK frames counter.
	 */
	public final void setCrcNOkFramesCounter(long value) {
		crcNOkFramesCounter = value;
	}

	@Override
	public final Object[] getValues() {
		return new Object[] { getLogicalName(), synchronizationRegister,
				new Object[] { physicalLayerDesynchronization, timeOutNotAddressedDesynchronization,
						timeOutFrameNotOkDesynchronization, writeRequestDesynchronization,
						wrongInitiatorDesynchronization },
				broadcastFramesCounter, repetitionsCounter, transmissionsCounter, crcOkFramesCounter,
				crcNOkFramesCounter };
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
		// synchronizationRegister
		if (all || canRead(2)) {
			attributes.add(2);
		}
		if (all || canRead(3)) {
			attributes.add(3);
		}
		if (all || canRead(4)) {
			attributes.add(4);
		}
		if (all || canRead(5)) {
			attributes.add(5);
		}
		if (all || canRead(6)) {
			attributes.add(6);
		}
		if (all || canRead(7)) {
			attributes.add(7);
		}
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
		return 1;
	}

	@Override
	public final DataType getDataType(final int index) {
		DataType ret;
		switch (index) {
		case 1:
			ret = DataType.OCTET_STRING;
			break;
		case 2:
		case 4:
			ret = DataType.ARRAY;
			break;
		case 3:
			ret = DataType.STRUCTURE;
			break;
		case 5:
		case 6:
		case 7:
		case 8:
			ret = DataType.UINT32;
			break;
		default:
			throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
		}
		return ret;
	}

	/*
	 * Returns value of given attribute.
	 */
	@Override
	public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
		Object ret;
		switch (e.getIndex()) {
		case 1:
			ret = GXCommon.logicalNameToBytes(getLogicalName());
			break;
		case 2: {
			GXByteBuffer bb = new GXByteBuffer();
			bb.setUInt8(DataType.ARRAY);
			if (synchronizationRegister == null) {
				bb.setUInt8(0);
			} else {
				GXCommon.setObjectCount(synchronizationRegister.size(), bb);
				for (java.util.Map.Entry<Integer, Long> it : synchronizationRegister) {
					bb.setUInt8(DataType.STRUCTURE);
					bb.setUInt8(2);
					GXCommon.setData(settings, bb, DataType.UINT16, it.getKey());
					GXCommon.setData(settings, bb, DataType.UINT32, it.getValue());
				}
			}
			ret = bb.array();
		}
			break;
		case 3: {
			GXByteBuffer bb = new GXByteBuffer();
			bb.setUInt8(DataType.STRUCTURE);
			bb.setUInt8(5);
			GXCommon.setData(settings, bb, DataType.UINT32, physicalLayerDesynchronization);
			GXCommon.setData(settings, bb, DataType.UINT32, timeOutNotAddressedDesynchronization);
			GXCommon.setData(settings, bb, DataType.UINT32, timeOutFrameNotOkDesynchronization);
			GXCommon.setData(settings, bb, DataType.UINT32, writeRequestDesynchronization);
			GXCommon.setData(settings, bb, DataType.UINT32, wrongInitiatorDesynchronization);
			ret = bb.array();
		}
			break;
		case 4: {
			GXByteBuffer bb = new GXByteBuffer();
			bb.setUInt8(DataType.ARRAY);
			if (broadcastFramesCounter == null) {
				bb.setUInt8(0);
			} else {
				GXCommon.setObjectCount(broadcastFramesCounter.size(), bb);
				for (java.util.Map.Entry<Integer, Long> it : broadcastFramesCounter) {
					bb.setUInt8(DataType.STRUCTURE);
					bb.setUInt8(2);
					GXCommon.setData(settings, bb, DataType.UINT16, it.getKey());
					GXCommon.setData(settings, bb, DataType.UINT32, it.getValue());
				}
			}
			ret = bb.array();
		}
			break;
		case 5:
			ret = repetitionsCounter;
			break;
		case 6:
			ret = transmissionsCounter;
			break;
		case 7:
			ret = crcOkFramesCounter;
			break;
		case 8:
			ret = crcNOkFramesCounter;
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			ret = null;
			break;
		}
		return ret;
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
			synchronizationRegister.clear();
			if (e.getValue() != null) {
				for (Object tmp : (List<?>) e.getValue()) {
					List<?> it = (List<?>) tmp;
					synchronizationRegister.add(new GXSimpleEntry<Integer, Long>(((Number) it.get(0)).intValue(),
							((Number) it.get(1)).longValue()));
				}
			}
		}
			break;
		case 3: {
			if (e.getValue() != null) {
				List<?> arr = (List<?>) e.getValue();

				physicalLayerDesynchronization = ((Number) arr.get(0)).intValue();
				timeOutNotAddressedDesynchronization = ((Number) arr.get(1)).intValue();
				timeOutFrameNotOkDesynchronization = ((Number) arr.get(2)).intValue();
				writeRequestDesynchronization = ((Number) arr.get(3)).intValue();
				wrongInitiatorDesynchronization = ((Number) arr.get(4)).intValue();
			} else {
				physicalLayerDesynchronization = 0;
				timeOutNotAddressedDesynchronization = 0;
				timeOutFrameNotOkDesynchronization = 0;
				writeRequestDesynchronization = 0;
				wrongInitiatorDesynchronization = 0;
			}
		}
			break;
		case 4: {
			broadcastFramesCounter.clear();
			if (e.getValue() != null) {
				for (Object tmp : (List<?>) e.getValue()) {
					List<?> it = (List<?>) tmp;
					broadcastFramesCounter.add(new GXSimpleEntry<Integer, Long>(((Number) it.get(0)).intValue(),
							((Number) it.get(1)).longValue()));
				}
			}
		}
			break;
		case 5:
			repetitionsCounter = ((Number) e.getValue()).intValue();
			break;
		case 6:
			transmissionsCounter = ((Number) e.getValue()).intValue();
			break;
		case 7:
			crcOkFramesCounter = ((Number) e.getValue()).intValue();
			break;
		case 8:
			crcNOkFramesCounter = ((Number) e.getValue()).intValue();
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			break;
		}
	}

	@Override
	public final void load(final GXXmlReader reader) throws XMLStreamException {
		synchronizationRegister.clear();
		if (reader.isStartElement("SynchronizationRegisters", true)) {
			while (reader.isStartElement("Item", true)) {
				Integer k = reader.readElementContentAsInt("Key");
				Long v = reader.readElementContentAsLong("Value");
				synchronizationRegister.add(new GXSimpleEntry<Integer, Long>(k, v));
			}
			reader.readEndElement("SynchronizationRegisters");
		}
		physicalLayerDesynchronization = reader.readElementContentAsInt("PhysicalLayerDesynchronization");
		timeOutNotAddressedDesynchronization = reader.readElementContentAsInt("TimeOutNotAddressedDesynchronization");
		timeOutFrameNotOkDesynchronization = reader.readElementContentAsInt("TimeOutFrameNotOkDesynchronization");
		writeRequestDesynchronization = reader.readElementContentAsInt("WriteRequestDesynchronization");
		wrongInitiatorDesynchronization = reader.readElementContentAsInt("WrongInitiatorDesynchronization");
		broadcastFramesCounter.clear();
		if (reader.isStartElement("BroadcastFramesCounters", true)) {
			while (reader.isStartElement("Item", true)) {
				Integer k = reader.readElementContentAsInt("Key");
				Long v = reader.readElementContentAsLong("Value");
				broadcastFramesCounter.add(new GXSimpleEntry<Integer, Long>(k, v));
			}
			reader.readEndElement("BroadcastFramesCounters");
		}
		repetitionsCounter = reader.readElementContentAsInt("RepetitionsCounter");
		transmissionsCounter = reader.readElementContentAsInt("TransmissionsCounter");
		crcOkFramesCounter = reader.readElementContentAsInt("CrcOkFramesCounter");
		crcNOkFramesCounter = reader.readElementContentAsInt("CrcNOkFramesCounter");
	}

	@Override
	public final void save(final GXXmlWriter writer) throws XMLStreamException {
		if (synchronizationRegister != null) {
			writer.writeStartElement("SynchronizationRegisters");
			for (java.util.Map.Entry<Integer, Long> it : synchronizationRegister) {
				writer.writeStartElement("Item");
				writer.writeElementString("Key", it.getKey());
				writer.writeElementString("Value", it.getValue());
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
		writer.writeElementString("PhysicalLayerDesynchronization", physicalLayerDesynchronization);
		writer.writeElementString("TimeOutNotAddressedDesynchronization", timeOutNotAddressedDesynchronization);
		writer.writeElementString("TimeOutFrameNotOkDesynchronization", timeOutFrameNotOkDesynchronization);
		writer.writeElementString("WriteRequestDesynchronization", writeRequestDesynchronization);
		writer.writeElementString("WrongInitiatorDesynchronization", wrongInitiatorDesynchronization);
		if (broadcastFramesCounter != null) {
			writer.writeStartElement("BroadcastFramesCounters");
			for (java.util.Map.Entry<Integer, Long> it : broadcastFramesCounter) {
				writer.writeStartElement("Item");
				writer.writeElementString("Key", it.getKey());
				writer.writeElementString("Value", it.getValue());
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
		writer.writeElementString("RepetitionsCounter", repetitionsCounter);
		writer.writeElementString("TransmissionsCounter", transmissionsCounter);
		writer.writeElementString("CrcOkFramesCounter", crcOkFramesCounter);
		writer.writeElementString("CrcNOkFramesCounter", crcNOkFramesCounter);
	}

	@Override
	public final void postLoad(final GXXmlReader reader) {
	}

	@Override
	public String[] getNames() {
		return new String[] { "Logical Name", "SynchronizationRegister", "Desynchronization listing",
				"BroadcastFramesCounter", "RepetitionsCounter", "TransmissionsCounter", "CrcOkFramesCounter",
				"CrcNOkFramesCounter" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "Reset" };
	}
}