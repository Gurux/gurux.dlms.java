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

import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.MacCapabilities;
import gurux.dlms.objects.enums.MacState;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPrimeNbOfdmPlcMacFunctionalParameters
 */
public class GXDLMSPrimeNbOfdmPlcMacFunctionalParameters extends GXDLMSObject
        implements IGXDLMSBase {

    /**
     * LNID allocated to this node at time of its registration.
     */
    private short lnId;
    /**
     * LSID allocated to this node at the time of its promotion.
     */
    private short lsId;

    /**
     * SID of the switch node through which this node is connected to the
     * subnetwork.
     */
    private short sId;
    /**
     * Subnetwork address to which this node is registered.
     */
    private byte[] sna;
    /**
     * Present functional state of the node.
     */
    private MacState state;

    /**
     * The SCP length, in symbols, in present frame.
     */
    private int scpLength;
    /**
     * Level of this node in subnetwork hierarchy.
     */
    private short nodeHierarchyLevel;

    /**
     * Number of beacon slots provisioned in present frame structure.
     */
    private short beaconSlotCount;
    /**
     * Beacon slot in which this device’s switch node transmits its beacon.
     */
    private short beaconRxSlot;
    /**
     * Beacon slot in which this device transmits its beacon.
     */
    private short beaconTxSlot;
    /**
     * Number of frames between receptions of two successive beacons.
     */
    private short beaconRxFrequency;

    /**
     * Number of frames between transmissions of two successive beacons.
     */
    private short beaconTxFrequency;
    /**
     * This attribute defines the capabilities of the node.
     */
    private Set<MacCapabilities> capabilities;

    /**
     * Constructor.
     */
    public GXDLMSPrimeNbOfdmPlcMacFunctionalParameters() {
        this("0.0.28.3.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSPrimeNbOfdmPlcMacFunctionalParameters(final String ln) {
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
    public GXDLMSPrimeNbOfdmPlcMacFunctionalParameters(final String ln,
            final int sn) {
        super(ObjectType.PRIME_NB_OFDM_PLC_MAC_FUNCTIONAL_PARAMETERS, ln, sn);
        capabilities = new HashSet<MacCapabilities>();
        state = MacState.DISCONNECTED;
    }

    /**
     * @return LNID allocated to this node at time of its registration.
     */
    public final short getLnId() {
        return lnId;
    }

    /**
     * @param value
     *            LNID allocated to this node at time of its registration.
     */
    public final void setLnId(final short value) {
        lnId = value;
    }

    /**
     * @return LSID allocated to this node at the time of its promotion.
     */
    public final short getLsId() {
        return lsId;
    }

    /**
     * @param value
     *            LSID allocated to this node at the time of its promotion.
     */
    public final void setLsId(final short value) {
        lsId = value;
    }

    /**
     * @return SID of the switch node through which this node is connected to
     *         the subnetwork.
     */
    public final short getSId() {
        return sId;
    }

    /**
     * @param value
     *            SID of the switch node through which this node is connected to
     *            the subnetwork.
     */
    public final void setSId(final short value) {
        sId = value;
    }

    /**
     * @return Subnetwork address to which this node is registered.
     */
    public final byte[] getSna() {
        return sna;
    }

    /**
     * @param value
     *            Subnetwork address to which this node is registered.
     */
    public final void setSna(final byte[] value) {
        sna = value;
    }

    /**
     * @return Present functional state of the node.
     */
    public final MacState getState() {
        return state;
    }

    /**
     * @param value
     *            Present functional state of the node.
     */
    public final void setState(final MacState value) {
        state = value;
    }

    /**
     * @return The SCP length, in symbols, in present frame.
     */
    public final int getScpLength() {
        return scpLength;
    }

    /**
     * @param value
     *            The SCP length, in symbols, in present frame.
     */
    public final void setScpLength(final int value) {
        scpLength = value;
    }

    /**
     * @return Level of this node in subnetwork hierarchy.
     */
    public final short getNodeHierarchyLevel() {
        return nodeHierarchyLevel;
    }

    /**
     * @param value
     *            Level of this node in subnetwork hierarchy.
     */
    public final void setNodeHierarchyLevel(short value) {
        nodeHierarchyLevel = value;
    }

    /**
     * @return Number of beacon slots provisioned in present frame structure.
     */
    public final short getBeaconSlotCount() {
        return beaconSlotCount;
    }

    /**
     * @param value
     *            Number of beacon slots provisioned in present frame structure.
     */
    public final void setBeaconSlotCount(final short value) {
        beaconSlotCount = value;
    }

    /**
     * @return Beacon slot in which this device’s switch node transmits its
     *         beacon.
     */
    public final short getBeaconRxSlot() {
        return beaconRxSlot;
    }

    /**
     * @param value
     *            Beacon slot in which this device’s switch node transmits its
     *            beacon.
     */
    public final void setBeaconRxSlot(short value) {
        beaconRxSlot = value;
    }

    /**
     * @return Beacon slot in which this device transmits its beacon.
     */
    public final short getBeaconTxSlot() {
        return beaconTxSlot;
    }

    /**
     * @param value
     *            Beacon slot in which this device transmits its beacon.
     */
    public final void setBeaconTxSlot(final short value) {
        beaconTxSlot = value;
    }

    /**
     * @return Number of frames between receptions of two successive beacons.
     */
    public final short getBeaconRxFrequency() {
        return beaconRxFrequency;
    }

    /**
     * @param value
     *            Number of frames between receptions of two successive beacons.
     */
    public final void setBeaconRxFrequency(final short value) {
        beaconRxFrequency = value;
    }

    /**
     * @return Number of frames between transmissions of two successive beacons.
     */
    public final short getBeaconTxFrequency() {
        return beaconTxFrequency;
    }

    /**
     * @param value
     *            Number of frames between transmissions of two successive
     *            beacons.
     */
    public final void setBeaconTxFrequency(final short value) {
        beaconTxFrequency = value;
    }

    /**
     * @return This attribute defines the capabilities of the node.
     */
    public final Set<MacCapabilities> getCapabilities() {
        return capabilities;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), lnId, lsId, sId, sna, state,
                scpLength, nodeHierarchyLevel, beaconSlotCount, beaconRxSlot,
                beaconTxSlot, beaconRxFrequency, beaconTxFrequency,
                capabilities };
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
        // LnId
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // LsId
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // SId
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // SNa
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // State
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // ScpLength
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // NodeHierarchyLevel
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // BeaconSlotCount
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // BeaconRxSlot
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // BeaconTxSlot
        if (all || canRead(11)) {
            attributes.add(11);
        }
        // BeaconRxFrequency
        if (all || canRead(12)) {
            attributes.add(12);
        }
        // BeaconTxFrequency
        if (all || canRead(13)) {
            attributes.add(13);
        }
        // Capabilities
        if (all || canRead(14)) {
            attributes.add(14);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 14;
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
            return DataType.OCTET_STRING;
        case 2:
            return DataType.INT16;
        case 3:
        case 4:
            return DataType.UINT8;
        case 5:
            return DataType.OCTET_STRING;
        case 6:
            return DataType.ENUM;
        case 7:
            return DataType.INT16;
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
            return DataType.UINT8;
        case 14:
            return DataType.UINT16;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            return lnId;
        case 3:
            return lsId;
        case 4:
            return sId;
        case 5:
            return sna;
        case 6:
            return state.ordinal();
        case 7:
            return scpLength;
        case 8:
            return nodeHierarchyLevel;
        case 9:
            return beaconSlotCount;
        case 10:
            return beaconRxSlot;
        case 11:
            return beaconTxSlot;
        case 12:
            return beaconRxFrequency;
        case 13:
            return beaconTxFrequency;
        case 14:
            return MacCapabilities.toInteger(capabilities);
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
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            lnId = ((Number) e.getValue()).shortValue();
            break;
        case 3:
            lsId = ((Number) e.getValue()).shortValue();
            break;
        case 4:
            sId = ((Number) e.getValue()).shortValue();
            break;
        case 5:
            sna = (byte[]) e.getValue();
            break;
        case 6:
            state = MacState.values()[((Number) e.getValue()).shortValue()];
            break;
        case 7:
            scpLength = ((Number) e.getValue()).shortValue();
            break;
        case 8:
            nodeHierarchyLevel = ((Number) e.getValue()).shortValue();
            break;
        case 9:
            beaconSlotCount = ((Number) e.getValue()).shortValue();
            break;
        case 10:
            beaconRxSlot = ((Number) e.getValue()).shortValue();
            break;
        case 11:
            beaconTxSlot = ((Number) e.getValue()).shortValue();
            break;
        case 12:
            beaconRxFrequency = ((Number) e.getValue()).shortValue();
            break;
        case 13:
            beaconTxFrequency = ((Number) e.getValue()).shortValue();
            break;
        case 14:
            capabilities = MacCapabilities
                    .forValue(((Number) e.getValue()).shortValue());
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        lnId = (short) reader.readElementContentAsInt("LnId");
        lsId = (byte) reader.readElementContentAsInt("LsId");
        sId = (byte) reader.readElementContentAsInt("SId");
        sna = GXCommon.hexToBytes(reader.readElementContentAsString("SNa"));
        state = MacState.values()[reader.readElementContentAsInt("State")];
        scpLength = (byte) reader.readElementContentAsInt("ScpLength");
        nodeHierarchyLevel =
                (byte) reader.readElementContentAsInt("NodeHierarchyLevel");
        beaconSlotCount =
                (byte) reader.readElementContentAsInt("BeaconSlotCount");
        beaconRxSlot = (byte) reader.readElementContentAsInt("BeaconRxSlot");
        beaconTxSlot = (byte) reader.readElementContentAsInt("BeaconTxSlot");
        beaconRxFrequency =
                (byte) reader.readElementContentAsInt("BeaconRxFrequency");
        beaconTxFrequency =
                (byte) reader.readElementContentAsInt("BeaconTxFrequency");
        capabilities = MacCapabilities
                .forValue(reader.readElementContentAsInt("Capabilities"));
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("LnId", lnId);
        writer.writeElementString("LsId", lsId);
        writer.writeElementString("SId", sId);
        writer.writeElementString("SNa", GXCommon.toHex(sna, false));
        writer.writeElementString("State", state.ordinal());
        writer.writeElementString("ScpLength", scpLength);
        writer.writeElementString("NodeHierarchyLevel", nodeHierarchyLevel);
        writer.writeElementString("BeaconSlotCount", beaconSlotCount);
        writer.writeElementString("BeaconRxSlot", beaconRxSlot);
        writer.writeElementString("BeaconTxSlot", beaconTxSlot);
        writer.writeElementString("BeaconRxFrequency", beaconRxFrequency);
        writer.writeElementString("BeaconTxFrequency", beaconTxFrequency);
        writer.writeElementString("Capabilities",
                MacCapabilities.toInteger(capabilities));
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}