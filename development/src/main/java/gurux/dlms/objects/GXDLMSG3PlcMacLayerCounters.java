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
import gurux.dlms.GXUInt32;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSG3PlcMacLayerCounters
 */
public class GXDLMSG3PlcMacLayerCounters extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Statistic counter of successfully transmitted data packets. PIB
     * attribute: 0x0101.
     */
    private long txDataPacketCount;
    /**
     * Statistic counter of successfully received data packets. PIB attribute:
     * 0x0102.
     */
    private long rxDataPacketCount;

    /**
     * Statistic counter of successfully transmitted command packets. PIB
     * attribute: 0x0103.
     */

    private long txCmdPacketCount;
    /**
     * Statistic counter of successfully received command packets. PIB
     * attribute: 0x0104.
     */
    private long rxCmdPacketCount;

    /**
     * Counts the number of times when CSMA backoffs reach macMaxCSMABackoffs.
     * PIB attribute: 0x0105.
     */
    private long cSMAFailCount;
    /**
     * Counts the number of times when an ACK is not received while transmitting
     * a unicast data frame. PIB attribute: 0x0106.
     */
    private long cSMANoAckCount;

    /**
     * Statistic counter of the number of frames received with bad CRC. PIB
     * attribute: 0x0109.
     */
    private long badCrcCount;
    /**
     * Statistic counter of the number of broadcast frames sent. PIB attribute:
     * 0x0108.
     */
    private long txDataBroadcastCount;
    /**
     * Statistic counter of successfully received broadcast packets PIB
     * attribute: 0x0107.
     */
    private long rxDataBroadcastCount;

    /**
     * Constructor.
     */
    public GXDLMSG3PlcMacLayerCounters() {
        this("0.0.29.0.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSG3PlcMacLayerCounters(String ln) {
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
    public GXDLMSG3PlcMacLayerCounters(String ln, int sn) {
        super(ObjectType.G3_PLC_MAC_LAYER_COUNTERS, ln, sn);
        setVersion(1);
    }

    /**
     * @return Statistic counter of successfully transmitted data packets. PIB
     *         attribute: 0x0101.
     */
    public final long getTxDataPacketCount() {
        return txDataPacketCount;
    }

    /**
     * @param value
     *            Statistic counter of successfully transmitted data packets.
     *            PIB attribute: 0x0101.
     */
    public final void setTxDataPacketCount(final long value) {
        txDataPacketCount = value;
    }

    /**
     * @return Statistic counter of successfully received data packets. PIB
     *         attribute: 0x0102.
     */
    public final long getRxDataPacketCount() {
        return rxDataPacketCount;
    }

    /**
     * @param value
     *            Statistic counter of successfully received data packets. PIB
     *            attribute: 0x0102.
     */
    public final void setRxDataPacketCount(final long value) {
        rxDataPacketCount = value;
    }

    /**
     * @return Statistic counter of successfully transmitted command packets.
     *         PIB attribute: 0x0103.
     */
    public final long getTxCmdPacketCount() {
        return txCmdPacketCount;
    }

    /**
     * @param value
     *            Statistic counter of successfully transmitted command packets.
     *            PIB attribute: 0x0103.
     */
    public final void setTxCmdPacketCount(final long value) {
        txCmdPacketCount = value;
    }

    /**
     * @return Statistic counter of successfully received command packets. PIB
     *         attribute: 0x0104.
     */
    public final long getRxCmdPacketCount() {
        return rxCmdPacketCount;
    }

    /**
     * @param value
     *            Statistic counter of successfully received command packets.
     *            PIB attribute: 0x0104.
     */
    public final void setRxCmdPacketCount(final long value) {
        rxCmdPacketCount = value;
    }

    /**
     * @return Counts the number of times when CSMA backoffs reach
     *         macMaxCSMABackoffs. PIB attribute: 0x0105.
     */
    public final long getCSMAFailCount() {
        return cSMAFailCount;
    }

    /**
     * @param value
     *            Counts the number of times when CSMA backoffs reach
     *            macMaxCSMABackoffs. PIB attribute: 0x0105.
     */
    public final void setCSMAFailCount(final long value) {
        cSMAFailCount = value;
    }

    /**
     * @return Counts the number of times when an ACK is not received while
     *         transmitting a unicast data frame. PIB attribute: 0x0106.
     */
    public final long getCSMANoAckCount() {
        return cSMANoAckCount;
    }

    /**
     * @param value
     *            Counts the number of times when an ACK is not received while
     *            transmitting a unicast data frame. PIB attribute: 0x0106.
     */
    public final void setCSMANoAckCount(long value) {
        cSMANoAckCount = value;
    }

    /**
     * @return Statistic counter of the number of frames received with bad CRC.
     *         PIB attribute: 0x0109.
     */
    public final long getBadCrcCount() {
        return badCrcCount;
    }

    /**
     * @param value
     *            Statistic counter of the number of frames received with bad
     *            CRC. PIB attribute: 0x0109.
     */
    public final void setBadCrcCount(long value) {
        badCrcCount = value;
    }

    /**
     * @return Statistic counter of the number of broadcast frames sent. PIB
     *         attribute: 0x0108.
     */
    public final long getTxDataBroadcastCount() {
        return txDataBroadcastCount;
    }

    /**
     * @param value
     *            Statistic counter of the number of broadcast frames sent. PIB
     *            attribute: 0x0108.
     */
    public final void setTxDataBroadcastCount(long value) {
        txDataBroadcastCount = value;
    }

    /**
     * @return Statistic counter of successfully received broadcast packets PIB
     *         attribute: 0x0107.
     */
    public final long getRxDataBroadcastCount() {
        return rxDataBroadcastCount;
    }

    /**
     * @param value
     *            Statistic counter of successfully received broadcast packets
     *            PIB attribute: 0x0107.
     */
    public final void setRxDataBroadcastCount(long value) {
        rxDataBroadcastCount = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getTxDataPacketCount(), getRxDataPacketCount(), getTxCmdPacketCount(),
                getRxCmdPacketCount(), getCSMAFailCount(), getCSMANoAckCount(), getBadCrcCount(),
                getTxDataBroadcastCount(), getRxDataBroadcastCount() };
    }

    @Override
    public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            setRxDataBroadcastCount(0);
            setTxDataBroadcastCount(getRxDataBroadcastCount());
            setBadCrcCount(getTxDataBroadcastCount());
            setCSMANoAckCount(getBadCrcCount());
            setCSMAFailCount(getCSMANoAckCount());
            setRxCmdPacketCount(getCSMAFailCount());
            setTxCmdPacketCount(getRxCmdPacketCount());
            setRxDataPacketCount(getTxCmdPacketCount());
            setTxDataPacketCount(getRxDataPacketCount());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // TxDataPacketCount
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // RxDataPacketCount
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // TxCmdPacketCount
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // RxCmdPacketCount
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // CSMAFailCount
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // CSMANoAckCount
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // BadCrcCount
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // TxDataBroadcastCount
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // RxDataBroadcastCount
        if (all || canRead(10)) {
            attributes.add(10);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "TxDataPacketCount", "RxDataPacketCount", "TxCmdPacketCount",
                "RxCmdPacketCount", " CSMAFailCount", "CSMANoAckCount", "BadCrcCount", "TxDataBroadcastCount",
                " RxDataBroadcastCount" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[] { "Reset" };
    }

    @Override
    public final int getAttributeCount() {
        return 10;
    }

    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public DataType getDataType(int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.UINT32;
        }
        if (index == 3) {
            return DataType.UINT32;
        }
        if (index == 4) {
            return DataType.UINT32;
        }
        if (index == 5) {
            return DataType.UINT32;
        }
        if (index == 6) {
            return DataType.UINT32;
        }
        if (index == 7) {
            return DataType.UINT32;
        }
        if (index == 8) {
            return DataType.UINT32;
        }
        if (index == 9) {
            return DataType.UINT32;
        }
        if (index == 10) {
            return DataType.UINT32;
        }
        throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
    }

    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            return getTxDataPacketCount();
        }
        if (e.getIndex() == 3) {
            return getRxDataPacketCount();
        }
        if (e.getIndex() == 4) {
            return getTxCmdPacketCount();
        }
        if (e.getIndex() == 5) {
            return getRxCmdPacketCount();
        }
        if (e.getIndex() == 6) {
            return getCSMAFailCount();
        }
        if (e.getIndex() == 7) {
            return getCSMANoAckCount();
        }
        if (e.getIndex() == 8) {
            return getBadCrcCount();
        }
        if (e.getIndex() == 9) {
            return getTxDataBroadcastCount();
        }
        if (e.getIndex() == 10) {
            return getRxDataBroadcastCount();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    @Override
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            setTxDataPacketCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 3) {
            setRxDataPacketCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 4) {
            setTxCmdPacketCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 5) {
            setRxCmdPacketCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 6) {
            setCSMAFailCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 7) {
            setCSMANoAckCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 8) {
            setBadCrcCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 9) {
            setTxDataBroadcastCount(((GXUInt32) e.getValue()).longValue());
        } else if (e.getIndex() == 10) {
            setRxDataBroadcastCount(((GXUInt32) e.getValue()).longValue());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(GXXmlReader reader) throws XMLStreamException {
        setTxDataPacketCount(reader.readElementContentAsInt("TxDataPacketCount"));
        setRxDataPacketCount(reader.readElementContentAsInt("RxDataPacketCount"));
        setTxCmdPacketCount(reader.readElementContentAsInt("TxCmdPacketCount"));
        setRxCmdPacketCount(reader.readElementContentAsInt("RxCmdPacketCount"));
        setCSMAFailCount(reader.readElementContentAsInt("CSMAFailCount"));
        setCSMANoAckCount(reader.readElementContentAsInt("CSMANoAckCount"));
        setBadCrcCount(reader.readElementContentAsInt("BadCrcCount"));
        setTxDataBroadcastCount(reader.readElementContentAsInt("TxDataBroadcastCount"));
        setRxDataBroadcastCount(reader.readElementContentAsInt("RxDataBroadcastCount"));
    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("TxDataPacketCount", getTxDataPacketCount());
        writer.writeElementString("RxDataPacketCount", getRxDataPacketCount());
        writer.writeElementString("TxCmdPacketCount", getTxCmdPacketCount());
        writer.writeElementString("RxCmdPacketCount", getRxCmdPacketCount());
        writer.writeElementString("CSMAFailCount", getCSMAFailCount());
        writer.writeElementString("CSMANoAckCount", getCSMANoAckCount());
        writer.writeElementString("BadCrcCount", getBadCrcCount());
        writer.writeElementString("TxDataBroadcastCount", getTxDataBroadcastCount());
        writer.writeElementString("RxDataBroadcastCount", getRxDataBroadcastCount());
    }

    @Override
    public final void postLoad(GXXmlReader reader) {
    }
}