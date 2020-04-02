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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPrimeNbOfdmPlcPhysicalLayerCounters
 */
public class GXDLMSPrimeNbOfdmPlcPhysicalLayerCounters extends GXDLMSObject
        implements IGXDLMSBase {
    /**
     * Number of bursts received on the physical layer for which the CRC was
     * incorrect.
     */
    private int crcIncorrectCount;
    /**
     * Number of bursts received on the physical layer for which the CRC was
     * correct, but the Protocol field of PHY header had invalid value.
     */
    private int crcFailedCount;
    /**
     * Number of times when PHY layer received new data to transmit.
     */
    private int txDropCount;
    /**
     * Number of times when the PHY layer received new data on the channel.
     */
    private int rxDropCount;

    /**
     * Constructor.
     */
    public GXDLMSPrimeNbOfdmPlcPhysicalLayerCounters() {
        this("0.0.28.1.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSPrimeNbOfdmPlcPhysicalLayerCounters(final String ln) {
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
    public GXDLMSPrimeNbOfdmPlcPhysicalLayerCounters(final String ln,
            final int sn) {
        super(ObjectType.PRIME_NB_OFDM_PLC_PHYSICAL_LAYER_COUNTERS, ln, sn);
    }

    /**
     * @return Number of bursts received on the physical layer for which the CRC
     *         was incorrect.
     */
    public final int crcIncorrectCount() {
        return crcIncorrectCount;
    }

    /**
     * @param value
     *            Number of bursts received on the physical layer for which the
     *            CRC was incorrect.
     */
    public final void setCrcIncorrectCount(final int value) {
        crcIncorrectCount = value;
    }

    /**
     * @return Number of bursts received on the physical layer for which the CRC
     *         was correct, but the Protocol field of PHY header had invalid
     *         value.
     */
    public final int getCrcFailedCount() {
        return crcFailedCount;
    }

    /**
     * @param value
     *            Number of bursts received on the physical layer for which the
     *            CRC was correct, but the Protocol field of PHY header had
     *            invalid value.
     */
    public final void setCrcFailedCount(final int value) {
        crcFailedCount = value;
    }

    /**
     * @return Number of times when PHY layer received new data to transmit.
     */
    public final int getTxDropCount() {
        return txDropCount;
    }

    /**
     * @param value
     *            Number of times when PHY layer received new data to transmit.
     */
    public final void setTxDropCount(final int value) {
        txDropCount = value;
    }

    /**
     * @return Number of times when the PHY layer received new data on the
     *         channel.
     */
    public final int getRxDropCount() {
        return rxDropCount;
    }

    /**
     * @param value
     *            Number of times when the PHY layer received new data on the
     *            channel.
     */
    public final void setRxDropCount(final int value) {
        rxDropCount = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), crcIncorrectCount,
                crcFailedCount, txDropCount, rxDropCount };
    }

    /**
     * Deallocating the service node address. The value of the
     * ServiceNodeAddress becomes NEW and the value of the BaseNodeAddress
     * becomes 0.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public final byte[][] reset(final GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return client.method(this, 1, 0, DataType.INT8);
    }

    @Override
    public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            crcIncorrectCount = crcFailedCount = txDropCount = rxDropCount = 0;
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
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
        // CrcIncorrectCount
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // CrcFailedCount
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // TxDropCount
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // RxDropCount
        if (all || canRead(5)) {
            attributes.add(5);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 5;
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
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
        case 3:
        case 4:
        case 5:
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
            return crcIncorrectCount;
        case 3:
            return crcFailedCount;
        case 4:
            return txDropCount;
        case 5:
            return rxDropCount;
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
            crcIncorrectCount = ((Number) e.getValue()).intValue();
            break;
        case 3:
            crcFailedCount = ((Number) e.getValue()).intValue();
            break;
        case 4:
            txDropCount = ((Number) e.getValue()).intValue();
            break;
        case 5:
            rxDropCount = ((Number) e.getValue()).intValue();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        crcIncorrectCount = reader.readElementContentAsInt("CrcIncorrectCount");
        crcFailedCount = reader.readElementContentAsInt("CrcFailedCount");
        txDropCount = reader.readElementContentAsInt("TxDropCount");
        rxDropCount = reader.readElementContentAsInt("RxDropCount");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("CrcIncorrectCount", crcIncorrectCount);
        writer.writeElementString("CrcFailedCount", crcFailedCount);
        writer.writeElementString("TxDropCount", txDropCount);
        writer.writeElementString("RxDropCount", rxDropCount);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}