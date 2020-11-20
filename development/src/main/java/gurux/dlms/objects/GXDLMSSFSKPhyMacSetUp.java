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
import gurux.dlms.objects.enums.BaudRate;
import gurux.dlms.objects.enums.DeltaElectricalPhase;
import gurux.dlms.objects.enums.InitiatorElectricalPhase;
import gurux.dlms.objects.enums.Repeater;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSSFSKPhyMacSetUp
 */
public class GXDLMSSFSKPhyMacSetUp extends GXDLMSObject implements IGXDLMSBase {

    /**
     * Initiator electrical phase.
     */
    private InitiatorElectricalPhase initiatorElectricalPhase =
            InitiatorElectricalPhase.NOT_DEFINED;

    /**
     * Delta electrical phase.
     */
    private DeltaElectricalPhase deltaElectricalPhase =
            DeltaElectricalPhase.NOT_DEFINED;

    /**
     * Corresponds to the maximum allowed gain bound to be used by the server
     * system in the receiving mode. The default unit is dB.
     */
    private byte maxReceivingGain;
    /**
     * Corresponds to the maximum attenuation bound to be used by the server
     * system in the transmitting mode.The default unit is dB.
     */
    private byte maxTransmittingGain;
    /**
     * Intelligent search initiator process. If the value of the initiator
     * signal is above the value of this attribute, a fast synchronization
     * process is possible.
     */
    private byte searchInitiatorThreshold;
    /**
     * Mark frequency required for S-FSK modulation.
     */
    private long markFrequency;
    /**
     * Space frequency required for S-FSK modulation.
     */
    private long spaceFrequency;
    /**
     * Mac Address.
     */
    private int macAddress;

    /**
     * MAC group addresses.
     */
    private int[] macGroupAddresses;
    /**
     * Specifies are all frames repeated.
     */
    private Repeater repeater = Repeater.NEVER;
    /**
     * Repeater status.
     */
    private boolean repeaterStatus;
    /**
     * Min delta credit.
     */
    private byte minDeltaCredit;

    /**
     * Initiator MAC address.
     */
    private int initiatorMacAddress;

    /**
     * Synchronization locked/unlocked state.
     */
    private boolean synchronizationLocked;
    /**
     * Transmission speed supported by the physical device.
     */
    private BaudRate transmissionSpeed;

    /**
     * Constructor.
     */
    public GXDLMSSFSKPhyMacSetUp() {
        this("0.0.26.0.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSFSKPhyMacSetUp(final String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSSFSKPhyMacSetUp(final String ln, final int sn) {
        super(ObjectType.SFSK_PHY_MAC_SETUP, ln, sn);
    }

    /**
     * @return Initiator electrical phase.
     */
    public final InitiatorElectricalPhase getInitiatorElectricalPhase() {
        return initiatorElectricalPhase;
    }

    /**
     * @param value
     *            Initiator electrical phase.
     */
    public final void
            setInitiatorElectricalPhase(final InitiatorElectricalPhase value) {
        initiatorElectricalPhase = value;
    }

    /**
     * @return Delta electrical phase.
     */
    public final DeltaElectricalPhase getDeltaElectricalPhase() {
        return deltaElectricalPhase;
    }

    /**
     * @param value
     *            Delta electrical phase.
     */
    public final void
            setDeltaElectricalPhase(final DeltaElectricalPhase value) {
        deltaElectricalPhase = value;
    }

    /**
     * @return Corresponds to the maximum allowed gain bound to be used by the
     *         server system in the receiving mode. The default unit is dB.
     */
    public final byte getMaxReceivingGain() {
        return maxReceivingGain;
    }

    /**
     * @param value
     *            Corresponds to the maximum allowed gain bound to be used by
     *            the server system in the receiving mode. The default unit is
     *            dB.
     */
    public final void setMaxReceivingGain(final byte value) {
        maxReceivingGain = value;
    }

    /**
     * @return Corresponds to the maximum attenuation bound to be used by the
     *         server system in the transmitting mode.The default unit is dB.
     */
    public final byte getMaxTransmittingGain() {
        return maxTransmittingGain;
    }

    /**
     * @param value
     *            Corresponds to the maximum attenuation bound to be used by the
     *            server system in the transmitting mode.The default unit is dB.
     */
    public final void setMaxTransmittingGain(final byte value) {
        maxTransmittingGain = value;
    }

    /**
     * @return Intelligent search initiator process. If the value of the
     *         initiator signal is above the value of this attribute, a fast
     *         synchronization process is possible.
     */
    public final byte getSearchInitiatorThreshold() {
        return searchInitiatorThreshold;
    }

    /**
     * @param value
     *            Intelligent search initiator process. If the value of the
     *            initiator signal is above the value of this attribute, a fast
     *            synchronization process is possible.
     */
    public final void setSearchInitiatorThreshold(final byte value) {
        searchInitiatorThreshold = value;
    }

    /**
     * @return Mark frequency required for S-FSK modulation.
     */
    public final long getMarkFrequency() {
        return markFrequency;
    }

    /**
     * @param value
     *            Mark frequency required for S-FSK modulation.
     */
    public final void setMarkFrequency(final long value) {
        markFrequency = value;
    }

    /**
     * @return Space frequency required for S-FSK modulation.
     */
    public final long getSpaceFrequency() {
        return spaceFrequency;
    }

    /**
     * @param value
     *            Space frequency required for S-FSK modulation.
     */
    public final void setSpaceFrequency(final long value) {
        spaceFrequency = value;
    }

    /**
     * @return Mac Address.
     */
    public final int getMacAddress() {
        return macAddress;
    }

    /**
     * @param value
     *            Mac Address.
     */
    public final void setMacAddress(final int value) {
        macAddress = value;
    }

    /**
     * @return MAC group addresses.
     */
    public final int[] getMacGroupAddresses() {
        return macGroupAddresses;
    }

    /**
     * @param value
     *            MAC group addresses.
     */
    public final void setMacGroupAddresses(final int[] value) {
        macGroupAddresses = value;
    }

    /**
     * @return Specifies are all frames repeated.
     */
    public final Repeater getRepeater() {
        return repeater;
    }

    /**
     * @param value
     *            Specifies are all frames repeated.
     */
    public final void setRepeater(final Repeater value) {
        repeater = value;
    }

    /**
     * @return Repeater status.
     */
    public final boolean getRepeaterStatus() {
        return repeaterStatus;
    }

    /**
     * @param value
     *            Repeater status.
     */
    public final void setRepeaterStatus(final boolean value) {
        repeaterStatus = value;
    }

    /**
     * @return Min delta credit.
     */
    public final byte getMinDeltaCredit() {
        return minDeltaCredit;
    }

    /**
     * @param value
     *            Min delta credit.
     */
    public final void setMinDeltaCredit(final byte value) {
        minDeltaCredit = value;
    }

    /**
     * @return Initiator MAC address.
     */
    public final int getInitiatorMacAddress() {
        return initiatorMacAddress;
    }

    /**
     * @param value
     *            Initiator MAC address.
     */
    public final void setInitiatorMacAddress(final int value) {
        initiatorMacAddress = value;
    }

    /**
     * @return Synchronization locked/unlocked state.
     */
    public final boolean getSynchronizationLocked() {
        return synchronizationLocked;
    }

    /**
     * @param value
     *            Synchronization locked/unlocked state.
     */
    public final void setSynchronizationLocked(boolean value) {
        synchronizationLocked = value;
    }

    /**
     * @return Transmission speed supported by the physical device.
     */
    public final BaudRate getTransmissionSpeed() {
        return transmissionSpeed;
    }

    /**
     * @param value
     *            Transmission speed supported by the physical device.
     */
    public final void setTransmissionSpeed(BaudRate value) {
        transmissionSpeed = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), initiatorElectricalPhase,
                deltaElectricalPhase, maxReceivingGain, maxTransmittingGain,
                searchInitiatorThreshold,
                new Object[] { markFrequency, spaceFrequency }, macAddress,
                macGroupAddresses, repeater, repeaterStatus, minDeltaCredit,
                initiatorMacAddress, synchronizationLocked, transmissionSpeed };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead(final boolean all) {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null
                || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // InitiatorElectricalPhase
        if (all || canRead(2)) {
            attributes.add(2);
        }

        // DeltaElectricalPhase
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // MaxReceivingGain,
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // MaxTransmittingGain
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // SearchInitiatorThreshold
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // MarkFrequency, SpaceFrequency
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // MacAddress
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // MacGroupAddresses
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // Repeater
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // RepeaterStatus
        attributes.add(11);

        // MinDeltaCredit
        attributes.add(12);

        // InitiatorMacAddress,
        attributes.add(13);

        // SynchronizationLocked
        attributes.add(14);

        // TransmissionSpeed
        if (all || canRead(15)) {
            attributes.add(15);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 15;
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
        DataType ret;
        // LogicalName
        switch (index) {
        case 1:
            ret = DataType.OCTET_STRING;
            break;
        case 4:
        case 5:
        case 6:
        case 12:
            ret = DataType.UINT8;
            break;
        case 7:
            ret = DataType.STRUCTURE;
            break;
        case 8:
        case 13:
            ret = DataType.UINT16;
            break;
        case 9:
            ret = DataType.ARRAY;
            break;
        case 2:
        case 3:
        case 10:
        case 15:
            ret = DataType.ENUM;
            break;
        case 11:
        case 14:
            ret = DataType.BOOLEAN;
            break;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
        return ret;
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        Object ret;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            ret = initiatorElectricalPhase;
            break;
        case 3:
            ret = deltaElectricalPhase;
            break;
        case 4:
            ret = maxReceivingGain;
            break;
        case 5:
            ret = maxTransmittingGain;
            break;
        case 6:
            ret = searchInitiatorThreshold;
            break;
        case 7: {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.STRUCTURE.getValue());
            bb.setUInt8(2);
            GXCommon.setData(settings, bb, DataType.UINT32, markFrequency);
            GXCommon.setData(settings, bb, DataType.UINT32, spaceFrequency);
            ret = bb.array();
            break;
        }
        case 8:
            ret = macAddress;
            break;
        case 9: {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (macGroupAddresses == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(macGroupAddresses.length, bb);
                for (int it : macGroupAddresses) {
                    GXCommon.setData(settings, bb, DataType.UINT16, it);
                }
            }
            ret = bb.array();
            break;
        }
        case 10:
            ret = repeater;
            break;
        case 11:
            ret = repeaterStatus;
            break;
        case 12:
            ret = minDeltaCredit;
            break;
        case 13:
            ret = initiatorMacAddress;
            break;
        case 14:
            ret = synchronizationLocked;
            break;
        case 15:
            ret = transmissionSpeed;
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
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            initiatorElectricalPhase = InitiatorElectricalPhase
                    .values()[((Number) e.getValue()).byteValue()];
            break;
        case 3:
            deltaElectricalPhase = DeltaElectricalPhase
                    .values()[((Number) e.getValue()).byteValue()];
            break;
        case 4:
            maxReceivingGain = ((Number) e.getValue()).byteValue();
            break;
        case 5:
            maxTransmittingGain = ((Number) e.getValue()).byteValue();
            break;
        case 6:
            searchInitiatorThreshold = ((Number) e.getValue()).byteValue();
            break;
        case 7: {
            if (e.getValue() != null) {
                List<?> arr = (List<?>) e.getValue();
                markFrequency = ((Number) arr.get(0)).intValue();
                spaceFrequency = ((Number) arr.get(1)).intValue();
            } else {
                markFrequency = 0;
                spaceFrequency = 0;
            }

            break;
        }
        case 8:
            macAddress = ((Number) e.getValue()).intValue();
            break;
        case 9: {
            List<Integer> list = new ArrayList<Integer>();
            if (e.getValue() != null) {
                for (Object it : (List<?>) e.getValue()) {
                    list.add((int) it);
                }
            }
            macGroupAddresses = GXCommon.toIntArray(list);
            break;
        }
        case 10:
            repeater = Repeater.values()[((Number) e.getValue()).byteValue()];
            break;
        case 11:
            repeaterStatus = (Boolean) e.getValue();
            break;
        case 12:
            minDeltaCredit = ((Number) e.getValue()).byteValue();
            break;
        case 13:
            initiatorMacAddress = ((Number) e.getValue()).intValue();
            break;
        case 14:
            synchronizationLocked = (Boolean) e.getValue();
            break;
        case 15:
            transmissionSpeed =
                    BaudRate.values()[((Number) e.getValue()).byteValue()];
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        initiatorElectricalPhase = InitiatorElectricalPhase.values()[reader
                .readElementContentAsInt("InitiatorElectricalPhase")];
        deltaElectricalPhase = DeltaElectricalPhase.values()[reader
                .readElementContentAsInt("DeltaElectricalPhase")];
        maxReceivingGain =
                (byte) reader.readElementContentAsInt("MaxReceivingGain");
        maxTransmittingGain =
                (byte) reader.readElementContentAsInt("MaxTransmittingGain");
        searchInitiatorThreshold = (byte) reader
                .readElementContentAsInt("SearchInitiatorThreshold");
        markFrequency = reader.readElementContentAsInt("MarkFrequency");
        spaceFrequency = reader.readElementContentAsInt("SpaceFrequency");
        macAddress = reader.readElementContentAsInt("MacAddress");
        java.util.ArrayList<Integer> list = new java.util.ArrayList<Integer>();
        if (reader.isStartElement("MacGroupAddresses", true)) {
            while (reader.isStartElement("Value", false)) {
                list.add(reader.readElementContentAsInt("Value"));
            }
            reader.readEndElement("MacGroupAddresses");
        }
        macGroupAddresses = GXCommon.toIntArray(list);
        repeater =
                Repeater.values()[reader.readElementContentAsInt("Repeater")];
        repeaterStatus = reader.readElementContentAsInt("RepeaterStatus") != 0;
        minDeltaCredit =
                (byte) reader.readElementContentAsInt("MinDeltaCredit");
        initiatorMacAddress =
                reader.readElementContentAsInt("InitiatorMacAddress");
        synchronizationLocked =
                reader.readElementContentAsInt("SynchronizationLocked") != 0;
        transmissionSpeed = BaudRate.values()[reader
                .readElementContentAsInt("TransmissionSpeed")];
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("InitiatorElectricalPhase",
                initiatorElectricalPhase.ordinal());
        writer.writeElementString("DeltaElectricalPhase",
                deltaElectricalPhase.ordinal());
        writer.writeElementString("MaxReceivingGain", maxReceivingGain);
        writer.writeElementString("MaxTransmittingGain", maxTransmittingGain);
        writer.writeElementString("SearchInitiatorThreshold",
                searchInitiatorThreshold);
        writer.writeElementString("MarkFrequency", markFrequency);
        writer.writeElementString("SpaceFrequency", spaceFrequency);
        writer.writeElementString("MacAddress", macAddress);
        writer.writeStartElement("MacGroupAddresses");
        if (macGroupAddresses != null) {
            for (int it : macGroupAddresses) {
                writer.writeElementString("Value", it);
            }
        }
        writer.writeEndElement();
        writer.writeElementString("Repeater", repeater.ordinal());
        writer.writeElementString("RepeaterStatus", repeaterStatus);
        writer.writeElementString("MinDeltaCredit", minDeltaCredit);
        writer.writeElementString("InitiatorMacAddress", initiatorMacAddress);
        writer.writeElementString("SynchronizationLocked",
                synchronizationLocked);
        writer.writeElementString("TransmissionSpeed",
                transmissionSpeed.ordinal());
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}