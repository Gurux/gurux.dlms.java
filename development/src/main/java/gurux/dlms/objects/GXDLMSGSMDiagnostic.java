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
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.GsmCircuitSwitchStatus;
import gurux.dlms.objects.enums.GsmPacketSwitchStatus;
import gurux.dlms.objects.enums.GsmStatus;

/**
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSGSMDiagnostic
 */
public class GXDLMSGSMDiagnostic extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Name of network operator.
     */
    private String operator;

    /**
     * Registration status of the modem.
     */
    private GsmStatus status;

    /**
     * Registration status of the modem.
     */
    private GsmCircuitSwitchStatus circuitSwitchStatus;

    /**
     * Registration status of the modem.
     */
    private GsmPacketSwitchStatus packetSwitchStatus;

    /**
     * Registration status of the modem.
     */
    private GXDLMSGSMCellInfo cellInfo;

    /**
     * Adjacent cells.
     */
    private List<GXAdjacentCell> adjacentCells;

    /**
     * Date and time when the data have been last captured.
     */
    private GXDateTime captureTime;

    /**
     * Constructor.
     */
    public GXDLMSGSMDiagnostic() {
        this("0.0.25.6.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSGSMDiagnostic(final String ln) {
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
    public GXDLMSGSMDiagnostic(final String ln, final int sn) {
        super(ObjectType.GSM_DIAGNOSTIC, ln, sn);
        setVersion(1);
        cellInfo = new GXDLMSGSMCellInfo();
        adjacentCells = new ArrayList<GXAdjacentCell>();
        status = GsmStatus.NONE;
        circuitSwitchStatus = GsmCircuitSwitchStatus.INACTIVE;
        packetSwitchStatus = GsmPacketSwitchStatus.INACTIVE;
    }

    /**
     * @return Name of network operator.
     */
    public final String getOperator() {
        return operator;
    }

    /**
     * @param value
     *            Name of network operator.
     */
    public final void setOperator(final String value) {
        operator = value;
    }

    /**
     * @return Registration status of the modem.
     */
    public final GsmStatus getStatus() {
        return status;
    }

    /**
     * @param value
     *            Registration status of the modem.
     */
    public final void setStatus(final GsmStatus value) {
        status = value;
    }

    /**
     * @return Registration status of the modem.
     */
    public final GsmCircuitSwitchStatus getCircuitSwitchStatus() {
        return circuitSwitchStatus;
    }

    /**
     * @param value
     *            Registration status of the modem.
     */
    public final void
            setCircuitSwitchStatus(final GsmCircuitSwitchStatus value) {
        circuitSwitchStatus = value;
    }

    /**
     * @return Registration status of the modem.
     */
    public final GsmPacketSwitchStatus getPacketSwitchStatus() {
        return packetSwitchStatus;
    }

    /**
     * @param value
     *            Registration status of the modem.
     */
    public final void setPacketSwitchStatus(final GsmPacketSwitchStatus value) {
        packetSwitchStatus = value;
    }

    public final GXDLMSGSMCellInfo getCellInfo() {
        return cellInfo;
    }

    public final void setCellInfo(final GXDLMSGSMCellInfo value) {
        cellInfo = value;
    }

    /**
     * @return Adjacent cells.
     */
    public final List<GXAdjacentCell> getAdjacentCells() {
        return adjacentCells;
    }

    /**
     * @return Date and time when the data have been last captured.
     */
    public final GXDateTime getCaptureTime() {
        return captureTime;
    }

    /**
     * @param value
     *            Date and time when the data have been last captured.
     */
    public final void setCaptureTime(final GXDateTime value) {
        captureTime = value;
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
        // Operator
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // Status
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // CircuitSwitchStatus
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // PacketSwitchStatus
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // CellInfo
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // AdjacentCells
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // CaptureTime
        if (all || canRead(8)) {
            attributes.add(8);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), operator, status,
                circuitSwitchStatus, packetSwitchStatus, cellInfo,
                adjacentCells, captureTime };
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
        return 0;
    }

    @Override
    public final DataType getDataType(final int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.STRING;
        case 3:
            return DataType.ENUM;
        case 4:
            return DataType.ENUM;
        case 5:
            return DataType.ENUM;
        case 6:
            return DataType.STRUCTURE;
        case 7:
            return DataType.ARRAY;
        case 8:
            return DataType.DATETIME;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        GXByteBuffer bb;
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            if (operator == null) {
                return null;
            }
            return operator.getBytes();
        case 3:
            if (status == null) {
                return 0;
            }
            return status.ordinal();
        case 4:
            return circuitSwitchStatus.ordinal();
        case 5:
            return packetSwitchStatus.ordinal();
        case 6:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.STRUCTURE.getValue());
            if (getVersion() == 0) {
                bb.setUInt8(4);
                GXCommon.setData(settings, bb, DataType.UINT16,
                        cellInfo.getCellId());
            } else {
                bb.setUInt8(7);
                GXCommon.setData(settings, bb, DataType.UINT32,
                        cellInfo.getCellId());
            }
            GXCommon.setData(settings, bb, DataType.UINT16,
                    cellInfo.getLocationId());
            GXCommon.setData(settings, bb, DataType.UINT8,
                    cellInfo.getSignalQuality());
            GXCommon.setData(settings, bb, DataType.UINT8, cellInfo.getBer());
            if (getVersion() > 0) {
                GXCommon.setData(settings, bb, DataType.UINT16,
                        cellInfo.getMobileCountryCode());
                GXCommon.setData(settings, bb, DataType.UINT16,
                        cellInfo.getMobileNetworkCode());
                GXCommon.setData(settings, bb, DataType.UINT32,
                        cellInfo.getChannelNumber());
            }
            return bb.array();
        case 7:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (adjacentCells == null) {
                bb.setUInt8(0);
            } else {
                bb.setUInt8((byte) adjacentCells.size());
            }
            for (GXAdjacentCell it : adjacentCells) {
                bb.setUInt8(DataType.STRUCTURE.getValue());
                bb.setUInt8(2);
                GXCommon.setData(settings, bb,
                        getVersion() == 0 ? DataType.UINT16 : DataType.UINT32,
                        it.getCellId());
                GXCommon.setData(settings, bb, DataType.UINT8,
                        it.getSignalQuality());
            }
            return bb.array();
        case 8:
            return captureTime;
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
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
            if (e.getValue() instanceof byte[]) {
                operator = new String((byte[]) e.getValue());
            } else if (operator instanceof String) {
                operator = (String) e.getValue();
            } else if (operator == null) {
                operator = null;
            } else {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            }
            break;
        case 3:
            status = GsmStatus.values()[((Number) e.getValue()).intValue()];
            break;
        case 4:
            circuitSwitchStatus = GsmCircuitSwitchStatus
                    .values()[((Number) e.getValue()).intValue()];
            break;
        case 5:
            packetSwitchStatus = GsmPacketSwitchStatus
                    .values()[((Number) e.getValue()).intValue()];
            break;
        case 6:
            if (e.getValue() != null) {
                Object[] tmp = (Object[]) e.getValue();
                cellInfo.setCellId(((Number) tmp[0]).longValue());
                cellInfo.setLocationId(((Number) tmp[1]).intValue());
                cellInfo.setSignalQuality(((Number) tmp[2]).intValue());
                cellInfo.setBer(((Number) tmp[3]).intValue());
                if (getVersion() > 0) {
                    cellInfo.setMobileCountryCode(((Number) tmp[4]).intValue());
                    cellInfo.setMobileNetworkCode(((Number) tmp[5]).intValue());
                    cellInfo.setChannelNumber(((Number) tmp[6]).intValue());
                }
            }
            break;
        case 7:
            adjacentCells.clear();
            if (e.getValue() != null) {
                for (Object it : (Object[]) e.getValue()) {
                    Object[] tmp = (Object[]) it;
                    GXAdjacentCell ac = new GXAdjacentCell();
                    ac.setCellId(((Number) tmp[0]).longValue());
                    ac.setSignalQuality(((Number) tmp[1]).shortValue());
                    adjacentCells.add(ac);
                }
            }
            break;
        case 8:
            if (e.getValue() instanceof byte[]) {
                captureTime = (GXDateTime) GXDLMSClient
                        .changeType((byte[]) e.getValue(), DataType.DATETIME);
            } else {
                captureTime = (GXDateTime) e.getValue();
            }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        operator = reader.readElementContentAsString("Operator");
        status = GsmStatus.values()[reader.readElementContentAsInt("Status")];
        circuitSwitchStatus = GsmCircuitSwitchStatus.values()[reader
                .readElementContentAsInt("CircuitSwitchStatus")];
        packetSwitchStatus = GsmPacketSwitchStatus.values()[reader
                .readElementContentAsInt("PacketSwitchStatus")];
        if (reader.isStartElement("CellInfo", true)) {
            cellInfo.setCellId(reader.readElementContentAsLong("CellId"));
            cellInfo.setLocationId(
                    reader.readElementContentAsInt("LocationId"));
            cellInfo.setSignalQuality(
                    reader.readElementContentAsInt("SignalQuality"));
            cellInfo.setBer(reader.readElementContentAsInt("Ber"));
            reader.readEndElement("CellInfo");
        }
        adjacentCells.clear();
        if (reader.isStartElement("AdjacentCells", true)) {
            while (reader.isStartElement("Item", true)) {
                GXAdjacentCell it = new GXAdjacentCell();
                it.setCellId(reader.readElementContentAsLong("CellId"));
                it.setSignalQuality((short) reader
                        .readElementContentAsInt("SignalQuality"));
                adjacentCells.add(it);
            }
            reader.readEndElement("AdjacentCells");
        }
        captureTime = new GXDateTime(
                reader.readElementContentAsString("CaptureTime"));
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementObject("Operator", operator);
        writer.writeElementString("Status", status.ordinal());
        writer.writeElementString("CircuitSwitchStatus",
                circuitSwitchStatus.ordinal());
        writer.writeElementString("PacketSwitchStatus",
                packetSwitchStatus.ordinal());
        if (cellInfo != null) {
            writer.writeStartElement("CellInfo");
            writer.writeElementString("CellId", cellInfo.getCellId());
            writer.writeElementString("LocationId", cellInfo.getLocationId());
            writer.writeElementString("SignalQuality",
                    cellInfo.getSignalQuality());
            writer.writeElementString("Ber", cellInfo.getBer());
            writer.writeEndElement();
        }

        if (adjacentCells != null) {
            writer.writeStartElement("AdjacentCells");
            for (GXAdjacentCell it : adjacentCells) {
                writer.writeStartElement("Item");
                writer.writeElementString("CellId", it.getCellId());
                writer.writeElementString("SignalQuality",
                        it.getSignalQuality());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeElementObject("CaptureTime", captureTime);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}