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
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSIec8802LlcType2Setup
 */
public class GXDLMSIec8802LlcType2Setup extends GXDLMSObject
        implements IGXDLMSBase {

    /**
     * Transmit Window Size K.
     */
    private byte transmitWindowSizeK;
    /**
     * Transmit Window Size RW.
     */
    private byte transmitWindowSizeRW;
    /**
     * Maximum octets in I Pdu N1.
     */
    private int maximumOctetsPdu;
    /**
     * Maximum number of transmissions, N2.
     */
    private byte maximumNumberTransmissions;
    /**
     * Acknowledgement timer in seconds.
     */
    private int acknowledgementTimer;
    /**
     * P-bit timer in seconds.
     */
    private int bitTimer;
    /**
     * Reject timer.
     */
    private int rejectTimer;
    /**
     * Busy state timer.
     */
    private int busyStateTimer;

    /**
     * Constructor.
     */
    public GXDLMSIec8802LlcType2Setup() {
        this("0.0.27.1.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSIec8802LlcType2Setup(final String ln) {
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
    public GXDLMSIec8802LlcType2Setup(final String ln, final int sn) {
        super(ObjectType.IEC_8802_LLC_TYPE2_SETUP, ln, sn);
        transmitWindowSizeK = 1;
        transmitWindowSizeRW = 1;
        maximumOctetsPdu = 128;
    }

    /**
     * @return Transmit Window Size K.
     */
    public final byte getTransmitWindowSizeK() {
        return transmitWindowSizeK;
    }

    /**
     * @param value
     *            Transmit Window Size K.
     */
    public final void setTransmitWindowSizeK(byte value) {
        transmitWindowSizeK = value;
    }

    /**
     * @return Transmit Window Size RW.
     */
    public final byte getTransmitWindowSizeRW() {
        return transmitWindowSizeRW;
    }

    /**
     * @param value
     *            Transmit Window Size RW.
     */
    public final void setTransmitWindowSizeRW(final byte value) {
        transmitWindowSizeRW = value;
    }

    /**
     * @return Maximum octets in I Pdu N1.
     */
    public final int getMaximumOctetsPdu() {
        return maximumOctetsPdu;
    }

    /**
     * @param value
     *            Maximum octets in I Pdu N1.
     */
    public final void setMaximumOctetsPdu(final int value) {
        maximumOctetsPdu = value;
    }

    /**
     * @return Maximum number of transmissions, N2.
     */
    public final byte getMaximumNumberTransmissions() {
        return maximumNumberTransmissions;
    }

    /**
     * @param value
     *            Maximum number of transmissions, N2.
     */
    public final void setMaximumNumberTransmissions(final byte value) {
        maximumNumberTransmissions = value;
    }

    /**
     * @return Acknowledgement timer in seconds.
     */
    public final int getAcknowledgementTimer() {
        return acknowledgementTimer;
    }

    /**
     * @param value
     *            Acknowledgement timer in seconds.
     */
    public final void setAcknowledgementTimer(final int value) {
        acknowledgementTimer = value;
    }

    /**
     * @return P-bit timer in seconds.
     */
    public final int getBitTimer() {
        return bitTimer;
    }

    /**
     * @param value
     *            P-bit timer in seconds.
     */
    public final void setBitTimer(final int value) {
        bitTimer = value;
    }

    /**
     * @return Reject timer.
     */
    public final int getRejectTimer() {
        return rejectTimer;
    }

    /**
     * @param value
     *            Reject timer.
     */
    public final void setRejectTimer(final int value) {
        rejectTimer = value;
    }

    /**
     * @return Busy state timer.
     */
    public final int getBusyStateTimer() {
        return busyStateTimer;
    }

    /**
     * @param value
     *            Busy state timer.
     */
    public final void setBusyStateTimer(final int value) {
        busyStateTimer = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), transmitWindowSizeK,
                transmitWindowSizeRW, maximumOctetsPdu,
                maximumNumberTransmissions, acknowledgementTimer, bitTimer,
                rejectTimer, busyStateTimer };
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
        // TransmitWindowSizeK
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // TransmitWindowSizeRW
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // MaximumOctetsPdu
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // MaximumNumberTransmissions
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // AcknowledgementTimer
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // BitTimer
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // RejectTimer
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // BusyStateTimer
        if (all || canRead(9)) {
            attributes.add(9);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 9;
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
        case 3:
        case 5:
            return DataType.UINT8;
        case 4:
        case 6:
        case 7:
        case 8:
        case 9:
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
        Object ret;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            ret = transmitWindowSizeK;
            break;
        case 3:
            ret = transmitWindowSizeRW;
            break;
        case 4:
            ret = maximumOctetsPdu;
            break;
        case 5:
            ret = maximumNumberTransmissions;
            break;
        case 6:
            ret = acknowledgementTimer;
            break;
        case 7:
            ret = bitTimer;
            break;
        case 8:
            ret = rejectTimer;
            break;
        case 9:
            ret = busyStateTimer;
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
            transmitWindowSizeK = ((Number) e.getValue()).byteValue();
            break;
        case 3:
            transmitWindowSizeRW = ((Number) e.getValue()).byteValue();
            break;
        case 4:
            maximumOctetsPdu = ((Number) e.getValue()).intValue();
            break;
        case 5:
            maximumNumberTransmissions = ((Number) e.getValue()).byteValue();
            break;
        case 6:
            acknowledgementTimer = ((Number) e.getValue()).intValue();
            break;
        case 7:
            bitTimer = ((Number) e.getValue()).intValue();
            break;
        case 8:
            rejectTimer = ((Number) e.getValue()).intValue();
            break;
        case 9:
            busyStateTimer = ((Number) e.getValue()).intValue();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }

    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        transmitWindowSizeK =
                (byte) reader.readElementContentAsInt("TransmitWindowSizeK");
        transmitWindowSizeRW =
                (byte) reader.readElementContentAsInt("TransmitWindowSizeRW");
        maximumOctetsPdu = reader.readElementContentAsInt("MaximumOctetsPdu");
        maximumNumberTransmissions = (byte) reader
                .readElementContentAsInt("MaximumNumberTransmissions");
        acknowledgementTimer =
                reader.readElementContentAsInt("AcknowledgementTimer");
        bitTimer = reader.readElementContentAsInt("BitTimer");
        rejectTimer = reader.readElementContentAsInt("RejectTimer");
        busyStateTimer = reader.readElementContentAsInt("BusyStateTimer");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("TransmitWindowSizeK", transmitWindowSizeK);
        writer.writeElementString("TransmitWindowSizeRW", transmitWindowSizeRW);
        writer.writeElementString("MaximumOctetsPdu", maximumOctetsPdu);
        writer.writeElementString("MaximumNumberTransmissions",
                maximumNumberTransmissions);
        writer.writeElementString("AcknowledgementTimer", acknowledgementTimer);
        writer.writeElementString("BitTimer", bitTimer);
        writer.writeElementString("RejectTimer", rejectTimer);
        writer.writeElementString("BusyStateTimer", busyStateTimer);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}