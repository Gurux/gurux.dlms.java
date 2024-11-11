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
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXBitString;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXEnum;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.GXStructure;
import gurux.dlms.GXUInt16;
import gurux.dlms.GXUInt32;
import gurux.dlms.GXUInt8;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.enums.GainResolution;
import gurux.dlms.objects.enums.Modulation;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSG3PlcMacSetup
 */
public class GXDLMSG3PlcMacSetup extends GXDLMSObject implements IGXDLMSBase {
    /**
     * The 16-bit address the device is using to communicate through the PAN.
     * PIB attribute 0x53.
     */
    private int shortAddress;
    /**
     * Route cost to coordinator. PIB attribute 0x10F.
     */
    private int rcCoord;
    /**
     * The 16-bit identifier of the PAN through which the device is operating.
     * PIB attribute 0x50.
     */
    private int panId;

    /**
     * This attribute holds GMK keys required for MAC layer ciphering. PIB
     * attribute 0x71.
     */
    private ArrayList<Entry<Short, byte[]>> keyTable;
    /**
     * The outgoing frame counter for this device, used when ciphering frames at
     * MAC layer. PIB attribute 0x77.
     */
    private long frameCounter;

    /**
     * Defines the tone mask to use during symbol formation. PIB attribute
     * 0x110.
     */
    private String toneMask;

    /**
     * Maximum time to live of tone map parameters entry in the neighbour table
     * in minutes. PIB attribute 0x10D.
     */
    private short tmrTtl;
    /**
     * Maximum number of retransmissions. PIB attribute 0x59.
     */
    private short maxFrameRetries;
    /**
     * Maximum time to live for an entry in the neighbour table in minutes PIB
     * attribute 0x10E.
     */
    private short neighbourTableEntryTtl;

    /**
     * The neighbour table contains information about all the devices within the
     * POS of the device PIB attribute 0x010A.
     */
    private GXDLMSNeighbourTable[] neighbourTable;
    /**
     * The high priority contention window size in number of slots. PIB
     * attribute 0x0100.
     */
    private short highPriorityWindowSize;

    /**
     * Channel access fairness limit. PIB attribute 0x10C.
     */
    private short cscmFairnessLimit;

    /**
     * Duration time in seconds for the beacon randomization. PIB attribute
     * 0x111.
     */
    private short beaconRandomizationWindowLength;

    /**
     * This parameter controls the adaptive CW linear decrease. PIB attribute
     * 0x0112.
     */
    private short a;

    /**
     * Rate adaptation factor for channel access fairness limit. PIB attribute
     * 0x113.
     */
    private short k;

    /**
     * Number of consecutive attempts while using minimum CW. PIB attribute
     * 0x114.
     */
    private short minCwAttempts;
    /**
     * This read only attribute indicates the capability of the node. PIB
     * attribute 0x115.
     */
    private short cenelecLegacyMode;

    /**
     * This read only attribute indicates the capability of the node. PIB
     * attribute 0x116.
     */
    private short rccLegacyMode;
    /**
     * Maximum value of backoff exponent. PIB attribute 0x47.
     */
    private short maxBe;

    /**
     * Maximum number of backoff attempts. PIB attribute 0x4E.
     */
    private short maxCsmaBackoffs;
    /**
     * Minimum value of backoff exponent. PIB attribute 0x4F.
     */
    private short minBe;
    /**
     * If true, MAC uses maximum contention window.
     */
    private boolean macBroadcastMaxCwEnabled;

    /**
     * Attenuation of the output level in dB.
     */
    private short macTransmitAtten;

    /**
     * The neighbour table contains some information about all the devices
     * within the POS of the device.
     */
    private GXDLMSMacPosTable[] macPosTable;
    /**
     * Duplicate frame detection time in seconds.
     */
    private short macDuplicateDetectionTtl;

    /**
     * Constructor.
     */
    public GXDLMSG3PlcMacSetup() {
        this("0.0.29.1.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSG3PlcMacSetup(String ln) {
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
    public GXDLMSG3PlcMacSetup(String ln, int sn) {
        super(ObjectType.G3_PLC_MAC_SETUP, ln, sn);
        setVersion(3);
        keyTable = new ArrayList<Entry<Short, byte[]>>();
        shortAddress = 0xFFFF;
        rcCoord = 0xFFFF;
        panId = 0xFFFF;
        frameCounter = 0;
        // ToneMask
        tmrTtl = 2;
        maxFrameRetries = 5;
        neighbourTableEntryTtl = 255;
        highPriorityWindowSize = 7;
        cscmFairnessLimit = 25;
        beaconRandomizationWindowLength = 12;
        a = 8;
        k = 5;
        minCwAttempts = 10;
        cenelecLegacyMode = 1;
        rccLegacyMode = 1;
        maxBe = 8;
        maxCsmaBackoffs = 50;
        minBe = 3;
    }

    /**
     * @return The 16-bit address the device is using to communicate through the
     *         PAN. PIB attribute 0x53.
     */
    public final int getShortAddress() {
        return shortAddress;
    }

    /**
     * @param value
     *            The 16-bit address the device is using to communicate through
     *            the PAN. PIB attribute 0x53.
     */
    public final void setShortAddress(final int value) {
        shortAddress = value;
    }

    /**
     * @return Route cost to coordinator. PIB attribute 0x10F.
     */
    public final int getRcCoord() {
        return rcCoord;
    }

    /**
     * @param value
     *            Route cost to coordinator. PIB attribute 0x10F.
     */
    public final void setRcCoord(final int value) {
        rcCoord = value;
    }

    /**
     * @return The 16-bit identifier of the PAN through which the device is
     *         operating. PIB attribute 0x50.
     */
    public final int getPANId() {
        return panId;
    }

    /**
     * @param value
     *            The 16-bit identifier of the PAN through which the device is
     *            operating. PIB attribute 0x50.
     */
    public final void setPANId(final int value) {
        panId = value;
    }

    /**
     * @return This attribute holds GMK keys required for MAC layer ciphering.
     *         PIB attribute 0x71.
     */
    public final ArrayList<Entry<Short, byte[]>> getKeyTable() {
        return keyTable;
    }

    /**
     * @param value
     *            This attribute holds GMK keys required for MAC layer
     *            ciphering. PIB attribute 0x71.
     */
    public final void setKeyTable(ArrayList<Entry<Short, byte[]>> value) {
        keyTable = value;
    }

    /**
     * @return The outgoing frame counter for this device, used when ciphering
     *         frames at MAC layer. PIB attribute 0x77.
     */
    public final long getFrameCounter() {
        return frameCounter;
    }

    /**
     * @param value
     *            The outgoing frame counter for this device, used when
     *            ciphering frames at MAC layer. PIB attribute 0x77.
     */
    public final void setFrameCounter(final long value) {
        frameCounter = value;
    }

    /**
     * @return Defines the tone mask to use during symbol formation. PIB
     *         attribute 0x110.
     */
    public final String getToneMask() {
        return toneMask;
    }

    /**
     * @param value
     *            Defines the tone mask to use during symbol formation. PIB
     *            attribute 0x110.
     */
    public final void setToneMask(String value) {
        toneMask = value;
    }

    /**
     * @return Maximum time to live of tone map parameters entry in the
     *         neighbour table in minutes. PIB attribute 0x10D.
     */
    public final short getTmrTtl() {
        return tmrTtl;
    }

    /**
     * @param value
     *            Maximum time to live of tone map parameters entry in the
     *            neighbour table in minutes. PIB attribute 0x10D.
     */
    public final void setTmrTtl(short value) {
        tmrTtl = value;
    }

    /**
     * @return Maximum number of retransmissions. PIB attribute 0x59.
     */
    public final short getMaxFrameRetries() {
        return maxFrameRetries;
    }

    /**
     * @param value
     *            Maximum number of retransmissions. PIB attribute 0x59.
     */
    public final void setMaxFrameRetries(final short value) {
        maxFrameRetries = value;
    }

    /**
     * @return Maximum time to live for an entry in the neighbour table in
     *         minutes PIB attribute 0x10E.
     */
    public final short getNeighbourTableEntryTtl() {
        return neighbourTableEntryTtl;
    }

    /**
     * @param value
     *            Maximum time to live for an entry in the neighbour table in
     *            minutes PIB attribute 0x10E.
     */
    public final void setNeighbourTableEntryTtl(short value) {
        neighbourTableEntryTtl = value;
    }

    /**
     * @return The neighbour table contains information about all the devices
     *         within the POS of the device PIB attribute 0x010A.
     */
    public final GXDLMSNeighbourTable[] getNeighbourTable() {
        return neighbourTable;
    }

    /**
     * @param value
     *            The neighbour table contains information about all the devices
     *            within the POS of the device PIB attribute 0x010A.
     */
    public final void setNeighbourTable(GXDLMSNeighbourTable[] value) {
        neighbourTable = value;
    }

    /**
     * @return The high priority contention window size in number of slots. PIB
     *         attribute 0x0100.
     */
    public final short getHighPriorityWindowSize() {
        return highPriorityWindowSize;
    }

    /**
     * @param value
     *            The high priority contention window size in number of slots.
     *            PIB attribute 0x0100.
     */
    public final void setHighPriorityWindowSize(final short value) {
        highPriorityWindowSize = value;
    }

    /**
     * @return Channel access fairness limit. PIB attribute 0x10C.
     */
    public final short getCscmFairnessLimit() {
        return cscmFairnessLimit;
    }

    /**
     * @param value
     *            Channel access fairness limit. PIB attribute 0x10C.
     */
    public final void setCscmFairnessLimit(final short value) {
        cscmFairnessLimit = value;
    }

    /**
     * @return Duration time in seconds for the beacon randomization. PIB
     *         attribute 0x111.
     */
    public final short getBeaconRandomizationWindowLength() {
        return beaconRandomizationWindowLength;
    }

    /**
     * @param value
     *            Duration time in seconds for the beacon randomization. PIB
     *            attribute 0x111.
     */
    public final void setBeaconRandomizationWindowLength(final short value) {
        beaconRandomizationWindowLength = value;
    }

    /**
     * @return This parameter controls the adaptive CW linear decrease. PIB
     *         attribute 0x0112.
     */
    public final short getA() {
        return a;
    }

    /**
     * @param value
     *            This parameter controls the adaptive CW linear decrease. PIB
     *            attribute 0x0112.
     */
    public final void setA(final short value) {
        a = value;
    }

    /**
     * @return Rate adaptation factor for channel access fairness limit. PIB
     *         attribute 0x113.
     */
    public final short getK() {
        return k;
    }

    /**
     * @param value
     *            Rate adaptation factor for channel access fairness limit. PIB
     *            attribute 0x113.
     */
    public final void setK(final short value) {
        k = value;
    }

    /**
     * @return Number of consecutive attempts while using minimum CW. PIB
     *         attribute 0x114.
     */
    public final short getMinCwAttempts() {
        return minCwAttempts;
    }

    /**
     * @param value
     *            Number of consecutive attempts while using minimum CW. PIB
     *            attribute 0x114.
     */
    public final void setMinCwAttempts(final short value) {
        minCwAttempts = value;
    }

    /**
     * @return This read only attribute indicates the capability of the node.
     *         PIB attribute 0x115.
     */
    public final short getCenelecLegacyMode() {
        return cenelecLegacyMode;
    }

    /**
     * @param value
     *            This read only attribute indicates the capability of the node.
     *            PIB attribute 0x115.
     */
    public final void setCenelecLegacyMode(final short value) {
        cenelecLegacyMode = value;
    }

    /**
     * @return This read only attribute indicates the capability of the node.
     *         PIB attribute 0x116.
     */
    public final short getFccLegacyMode() {
        return rccLegacyMode;
    }

    /**
     * @param value
     *            This read only attribute indicates the capability of the node.
     *            PIB attribute 0x116.
     */
    public final void setFccLegacyMode(final short value) {
        rccLegacyMode = value;
    }

    /**
     * @return Maximum value of backoff exponent. PIB attribute 0x47.
     */
    public final short getMaxBe() {
        return maxBe;
    }

    /**
     * @param value
     *            Maximum value of backoff exponent. PIB attribute 0x47.
     */
    public final void setMaxBe(final short value) {
        maxBe = value;
    }

    /**
     * @return Maximum number of backoff attempts. PIB attribute 0x4E.
     */
    public final short getMaxCsmaBackoffs() {
        return maxCsmaBackoffs;
    }

    /**
     * @param value
     *            Maximum number of backoff attempts. PIB attribute 0x4E.
     */
    public final void setMaxCsmaBackoffs(final short value) {
        maxCsmaBackoffs = value;
    }

    /**
     * @return Minimum value of backoff exponent. PIB attribute 0x4F.
     */
    public final short getMinBe() {
        return minBe;
    }

    /**
     * @param value
     *            Minimum value of backoff exponent. PIB attribute 0x4F.
     */
    public final void setMinBe(final short value) {
        minBe = value;
    }

    /**
     * @return If true, MAC uses maximum contention window.
     */
    public final boolean getMacBroadcastMaxCwEnabled() {
        return macBroadcastMaxCwEnabled;
    }

    /**
     * @param value
     *            If true, MAC uses maximum contention window.
     */
    public final void setMacBroadcastMaxCwEnabled(final boolean value) {
        macBroadcastMaxCwEnabled = value;
    }

    /**
     * @return Attenuation of the output level in dB.
     */
    public final short getMacTransmitAtten() {
        return macTransmitAtten;
    }

    /**
     * @param value
     *            Attenuation of the output level in dB.
     */
    public final void setMacTransmitAtten(final short value) {
        macTransmitAtten = value;
    }

    /**
     * @return The neighbour table contains some information about all the
     *         devices within the POS of the device.
     */
    public final GXDLMSMacPosTable[] getMacPosTable() {
        return macPosTable;
    }

    /**
     * @param value
     *            The neighbour table contains some information about all the
     *            devices within the POS of the device.
     */
    public final void setMacPosTable(final GXDLMSMacPosTable[] value) {
        macPosTable = value;
    }

    /**
     * @return Duplicate frame detection time in seconds.
     */
    public final short getMacDuplicateDetectionTtl() {
        return macDuplicateDetectionTtl;
    }

    /**
     * @param value
     *            Duplicate frame detection time in seconds.
     */
    public final void setMacDuplicateDetectionTtl(short value) {
        macDuplicateDetectionTtl = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getShortAddress(), getRcCoord(), getPANId(), getKeyTable(),
                getFrameCounter(), getToneMask(), getTmrTtl(), getMaxFrameRetries(), getNeighbourTableEntryTtl(),
                getNeighbourTable(), getHighPriorityWindowSize(), getCscmFairnessLimit(),
                getBeaconRandomizationWindowLength(), getA(), getK(), getMinCwAttempts(), getCenelecLegacyMode(),
                getFccLegacyMode(), getMaxBe(), getMaxCsmaBackoffs(), getMinBe(), getMacBroadcastMaxCwEnabled(),
                getMacTransmitAtten(), getMacPosTable(), getMacDuplicateDetectionTtl() };
    }

    private static byte[] getNeighbourTables(GXDLMSNeighbourTable[] tables) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY);
        if (tables == null) {
            bb.setUInt8(0);
        } else {
            GXCommon.setObjectCount(tables.length, bb);
            for (GXDLMSNeighbourTable it : tables) {
                bb.setUInt8(DataType.STRUCTURE);
                bb.setUInt8(11);
                GXCommon.setData(null, bb, DataType.UINT16, it.getShortAddress());
                GXCommon.setData(null, bb, DataType.BOOLEAN, it.getEnabled());
                GXCommon.setData(null, bb, DataType.BITSTRING, it.getToneMap());
                GXCommon.setData(null, bb, DataType.ENUM, it.getModulation());
                GXCommon.setData(null, bb, DataType.INT8, it.getTxGain());
                GXCommon.setData(null, bb, DataType.ENUM, it.getTxRes());
                GXCommon.setData(null, bb, DataType.BITSTRING, it.getTxCoeff());
                GXCommon.setData(null, bb, DataType.UINT8, it.getLqi());
                GXCommon.setData(null, bb, DataType.INT8, it.getPhaseDifferential());
                GXCommon.setData(null, bb, DataType.UINT8, it.getTMRValidTime());
                GXCommon.setData(null, bb, DataType.UINT8, it.getNeighbourValidTime());
            }
        }
        return bb.array();
    }

    private static byte[] getPosTables(GXDLMSMacPosTable[] tables) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY);
        if (tables == null) {
            bb.setUInt8(0);
        } else {
            GXCommon.setObjectCount(tables.length, bb);
            for (GXDLMSMacPosTable it : tables) {
                bb.setUInt8(DataType.STRUCTURE);
                bb.setUInt8(3);
                GXCommon.setData(null, bb, DataType.UINT16, it.getShortAddress());
                GXCommon.setData(null, bb, DataType.UINT8, it.getLQI());
                GXCommon.setData(null, bb, DataType.UINT8, it.getValidTime());
            }
        }
        return bb.array();
    }

    @Override
    public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            ArrayList<GXDLMSNeighbourTable> list = new ArrayList<GXDLMSNeighbourTable>();
            int index = (int) e.getValue();
            for (GXDLMSNeighbourTable it : neighbourTable) {
                if (it.getShortAddress() == index) {
                    list.add(it);
                }
            }
            return getNeighbourTables(list.toArray(new GXDLMSNeighbourTable[0]));
        } else if (e.getIndex() == 2) {
            ArrayList<GXDLMSMacPosTable> list = new ArrayList<GXDLMSMacPosTable>();
            int index = (int) e.getValue();
            for (GXDLMSMacPosTable it : getMacPosTable()) {
                if (it.getShortAddress() == index) {
                    list.add(it);
                }
            }
            return getPosTables(list.toArray(new GXDLMSMacPosTable[0]));

        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        ArrayList<Integer> attributes = new ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // MacShortAddress
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // MacRcCoord
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // MacPANId
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // MackeyTable
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // MacFrameCounter
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // MacToneMask
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // MacTmrTtl
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // MacMaxFrameRetries
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // MacneighbourTableEntryTtl
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // MacNeighbourTable
        if (all || canRead(11)) {
            attributes.add(11);
        }
        // MachighPriorityWindowSize
        if (all || canRead(12)) {
            attributes.add(12);
        }
        // MacCscmFairnessLimit
        if (all || canRead(13)) {
            attributes.add(13);
        }
        // MacBeaconRandomizationWindowLength
        if (all || canRead(14)) {
            attributes.add(14);
        }
        // MacA
        if (all || canRead(15)) {
            attributes.add(15);
        }
        // MacK
        if (all || canRead(16)) {
            attributes.add(16);
        }
        // MacMinCwAttempts
        if (all || canRead(17)) {
            attributes.add(17);
        }
        // MacCenelecLegacyMode
        if (all || canRead(18)) {
            attributes.add(18);
        }
        // MacFCCLegacyMode
        if (all || canRead(19)) {
            attributes.add(19);
        }
        // MacMaxBe
        if (all || canRead(20)) {
            attributes.add(20);
        }
        // MacMaxCsmaBackoffs,
        if (all || canRead(21)) {
            attributes.add(21);
        }
        // MacMinBe
        if (all || canRead(22)) {
            attributes.add(22);
        }
        // MacBroadcastMaxCwEnabled
        if (all || canRead(23)) {
            attributes.add(23);
        }
        // MacTransmitAtten
        if (all || canRead(24)) {
            attributes.add(24);
        }
        // MacPosTable
        if (all || canRead(25)) {
            attributes.add(25);
        }
        // MacDuplicateDetectionTtl
        if (getVersion() > 2) {
            if (all || canRead(26)) {
                attributes.add(26);
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /**
     * Retrieves the MAC neighbour table.
     * 
     * @param client
     *            DLMS client.
     * @param address
     *            MAC short address
     * @return Generated bytes.
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
     * @throws SignatureException
     */
    public final byte[][] getNeighbourTableEntry(GXDLMSClient client, short address)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 1, address, DataType.UINT16);
    }

    private static GXDLMSNeighbourTable[] parseNeighbourTableEntry(Object value) {
        ArrayList<GXDLMSNeighbourTable> list = new ArrayList<GXDLMSNeighbourTable>();
        if (value != null) {
            for (Object tmp : (Iterable<Object>) value) {
                GXStructure arr = (GXStructure) tmp;
                GXDLMSNeighbourTable it = new GXDLMSNeighbourTable();
                it.setShortAddress(((GXUInt16) arr.get(0)).intValue());
                it.setEnabled((boolean) arr.get(1));
                it.setToneMap(String.valueOf(arr.get(2)));
                it.setModulation(Modulation.forValue(((GXEnum) arr.get(3)).shortValue()));
                it.setTxGain((byte) arr.get(4));
                it.setTxRes(GainResolution.forValue(((GXEnum) arr.get(5)).shortValue()));
                it.setTxCoeff(String.valueOf(arr.get(6)));
                it.setLqi(((GXUInt8) arr.get(7)).shortValue());
                it.setPhaseDifferential((byte) arr.get(8));
                it.setTMRValidTime(((GXUInt8) arr.get(9)).shortValue());
                it.setNeighbourValidTime(((GXUInt8) arr.get(10)).shortValue());
                list.add(it);
            }
        }
        return list.toArray(new GXDLMSNeighbourTable[0]);
    }

    /**
     * Parse neighbour table entry.
     * 
     * @param reply
     *            Received reply
     * @return {@link getNeighbourTableEntry}
     */
    public final GXDLMSNeighbourTable[] parseNeighbourTableEntry(GXByteBuffer reply) {
        GXDataInfo info = new GXDataInfo();
        Object value = GXCommon.getData(null, reply, info);
        return parseNeighbourTableEntry(value);
    }

    /**
     * Retrieves the mac POS table.
     * 
     * @param client
     *            DLMS client.
     * @param address
     *            MAC short address
     * @return Generated bytes. {@link parsePosTableEntry}
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
     * @throws SignatureException
     */
    public final byte[][] getPosTableEntry(GXDLMSClient client, int address)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 2, address, DataType.UINT16);
    }

    private static GXDLMSMacPosTable[] parsePosTableEntry(Object value) {
        ArrayList<GXDLMSMacPosTable> list = new ArrayList<GXDLMSMacPosTable>();
        if (value != null) {
            for (Object tmp : (Iterable<Object>) value) {
                GXStructure arr = (GXStructure) tmp;
                GXDLMSMacPosTable it = new GXDLMSMacPosTable();
                it.setShortAddress(((GXUInt16) arr.get(0)).intValue());
                it.setLQI(((GXUInt8) arr.get(1)).shortValue());
                it.setValidTime(((GXUInt8) arr.get(2)).shortValue());
                list.add(it);
            }
        }
        return list.toArray(new GXDLMSMacPosTable[0]);
    }

    /**
     * Parse MAC POS tables.
     * 
     * @param reply
     *            Received reply
     * @return {@link getPosTableEntry}
     */
    public final GXDLMSMacPosTable[] parsePosTableEntry(GXByteBuffer reply) {
        GXDataInfo info = new GXDataInfo();
        Object value = GXCommon.getData(null, reply, info);
        return parsePosTableEntry(value);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "MacShortAddress", "MacRcCoord", "MacPANId", "MackeyTable ",
                "MacFrameCounter", "MacToneMask", "MacTmrTtl", "MacMaxFrameRetries", "MacneighbourTableEntryTtl",
                "MacNeighbourTable", "MachighPriorityWindowSize", "MacCscmFairnessLimit",
                "MacBeaconRandomizationWindowLength", "MacA", "MacK", "MacMinCwAttempts", "MacCenelecLegacyMode",
                "MacFCCLegacyMode", "MacMaxBe", "MacMaxCsmaBackoffs", "MacMinBe", "MacBroadcastMaxCwEnabled",
                "MacTransmitAtten", "MacPosTable", "MacDuplicateDetectionTtl" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[] { "MAC get neighbour table entry", "MAC get POS tableentry" };
    }

    @Override
    public final int getAttributeCount() {
        if (getVersion() == 3) {
            return 26;
        }
        if (getVersion() == 2) {
            return 25;
        }
        return 22;
    }

    @Override
    public final int getMethodCount() {
        if (getVersion() == 3) {
            return 2;
        }
        return 1;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.UINT16;
        // MacRcCoord
        case 3:
            return DataType.UINT16;
        // MacPANId
        case 4:
            return DataType.UINT16;
        // MackeyTable
        case 5:
            return DataType.ARRAY;
        // MacFrameCounter
        case 6:
            return DataType.UINT32;
        // MacToneMask
        case 7:
            return DataType.BITSTRING;
        // MacTmrTtl
        case 8:
            return DataType.UINT8;
        // MacMaxFrameRetries
        case 9:
            return DataType.UINT8;
        // MacneighbourTableEntryTtl
        case 10:
            return DataType.UINT8;
        // MacNeighbourTable
        case 11:
            return DataType.ARRAY;
        // MachighPriorityWindowSize
        case 12:
            return DataType.UINT8;
        // MacCscmFairnessLimit
        case 13:
            return DataType.UINT8;
        // MacBeaconRandomizationWindowLength
        case 14:
            return DataType.UINT8;
        // MacA
        case 15:
            return DataType.UINT8;
        // MacK
        case 16:
            return DataType.UINT8;
        // MacMinCwAttempts
        case 17:
            return DataType.UINT8;
        // MacCenelecLegacyMode
        case 18:
            return DataType.UINT8;
        // MacFCCLegacyMode
        case 19:
            return DataType.UINT8;
        // MacMaxBe
        case 20:
            return DataType.UINT8;
        // MacMaxCsmaBackoffs
        case 21:
            return DataType.UINT8;
        // MacMinBe
        case 22:
            return DataType.UINT8;
        // MacBroadcastMaxCwEnabled
        case 23:
            return DataType.BOOLEAN;
        // MacTransmitAtten
        case 24:
            return DataType.UINT8;
        // MacPosTable
        case 25:
            return DataType.ARRAY;
        case 26:
            // MacDuplicateDetectionTtl
            return DataType.UINT8;
        default:
            throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
        }
    }

    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            return shortAddress;
        }
        if (e.getIndex() == 3) {
            return rcCoord;
        }
        if (e.getIndex() == 4) {
            return panId;
        }
        if (e.getIndex() == 5) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getKeyTable() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getKeyTable().size(), bb);
                for (Entry<Short, byte[]> it : getKeyTable()) {
                    bb.setUInt8(DataType.STRUCTURE);
                    bb.setUInt8(2);
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getKey());
                    GXCommon.setData(settings, bb, DataType.OCTET_STRING, it.getValue());
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 6) {
            return getFrameCounter();
        }
        if (e.getIndex() == 7) {
            return getToneMask();
        }
        if (e.getIndex() == 8) {
            return getTmrTtl();
        }
        if (e.getIndex() == 9) {
            return getMaxFrameRetries();
        }
        if (e.getIndex() == 10) {
            return getNeighbourTableEntryTtl();
        }
        if (e.getIndex() == 11) {
            return getNeighbourTables(getNeighbourTable());
        }
        if (e.getIndex() == 12) {
            return getHighPriorityWindowSize();
        }
        if (e.getIndex() == 13) {
            return getCscmFairnessLimit();
        }
        if (e.getIndex() == 14) {
            return getBeaconRandomizationWindowLength();
        }
        if (e.getIndex() == 15) {
            return getA();
        }
        if (e.getIndex() == 16) {
            return getK();
        }
        if (e.getIndex() == 17) {
            return getMinCwAttempts();
        }
        if (e.getIndex() == 18) {
            return getCenelecLegacyMode();
        }
        if (e.getIndex() == 19) {
            return getFccLegacyMode();
        }
        if (e.getIndex() == 20) {
            return getMaxBe();
        }
        if (e.getIndex() == 21) {
            return getMaxCsmaBackoffs();
        }
        if (e.getIndex() == 22) {
            return getMinBe();
        }
        if (e.getIndex() == 23) {
            return getMacBroadcastMaxCwEnabled();
        }
        if (e.getIndex() == 24) {
            return getMacTransmitAtten();
        }
        if (e.getIndex() == 25) {
            return getPosTables(getMacPosTable());
        }
        if (e.getIndex() == 26) {
            return getMacDuplicateDetectionTtl();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    @Override
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            setShortAddress(((GXUInt16) e.getValue()).intValue());
        } else if (e.getIndex() == 3) {
            setRcCoord(((GXUInt16) e.getValue()).intValue());
        } else if (e.getIndex() == 4) {
            setPANId(((GXUInt16) e.getValue()).intValue());
        } else if (e.getIndex() == 5) {
            getKeyTable().clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure arr = (GXStructure) tmp;
                    keyTable.add(
                            new GXSimpleEntry<Short, byte[]>(((GXUInt8) arr.get(0)).shortValue(), (byte[]) arr.get(1)));
                }
            }
        } else if (e.getIndex() == 6) {
            setFrameCounter(((GXUInt32) e.getValue()).shortValue());
        } else if (e.getIndex() == 7) {
            setToneMask(((GXBitString) e.getValue()).toString());
        } else if (e.getIndex() == 8) {
            setTmrTtl(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 9) {
            setMaxFrameRetries(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 10) {
            setNeighbourTableEntryTtl(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 11) {
            setNeighbourTable(parseNeighbourTableEntry(e.getValue()));
        } else if (e.getIndex() == 12) {
            setHighPriorityWindowSize(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 13) {
            setCscmFairnessLimit(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 14) {
            setBeaconRandomizationWindowLength(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 15) {
            setA(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 16) {
            setK(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 17) {
            setMinCwAttempts(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 18) {
            setCenelecLegacyMode(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 19) {
            setFccLegacyMode(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 20) {
            setMaxBe(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 21) {
            setMaxCsmaBackoffs(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 22) {
            setMinBe(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 23) {
            setMacBroadcastMaxCwEnabled((boolean) e.getValue());
        } else if (e.getIndex() == 24) {
            setMacTransmitAtten(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 25) {
            setMacPosTable(parsePosTableEntry(e.getValue()));
        } else if (e.getIndex() == 26) {
            setMacDuplicateDetectionTtl(((GXUInt8) e.getValue()).shortValue());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    private void loadKeyTable(GXXmlReader reader) throws XMLStreamException {
        getKeyTable().clear();
        if (reader.isStartElement("KeyTable", true)) {
            while (reader.isStartElement("Item", true)) {
                short k = (short) reader.readElementContentAsInt("Key");
                byte[] d = GXDLMSTranslator.hexToBytes(reader.readElementContentAsString("Data"));
                getKeyTable().add(new GXSimpleEntry<Short, byte[]>(k, d));
            }
            reader.readEndElement("KeyTable");
        }
    }

    private void loadNeighbourTable(GXXmlReader reader) throws XMLStreamException {
        ArrayList<GXDLMSNeighbourTable> list = new ArrayList<GXDLMSNeighbourTable>();
        if (reader.isStartElement("NeighbourTable", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSNeighbourTable it = new GXDLMSNeighbourTable();
                it.setShortAddress(reader.readElementContentAsInt("ShortAddress"));
                it.setEnabled(reader.readElementContentAsInt("Enabled") != 0);
                it.setToneMap(reader.readElementContentAsString("ToneMap"));
                it.setModulation(Modulation.forValue(reader.readElementContentAsInt("Modulation")));
                it.setTxGain((byte) reader.readElementContentAsInt("TxGain"));
                it.setTxRes(GainResolution.forValue(reader.readElementContentAsInt("TxRes")));
                it.setTxCoeff(reader.readElementContentAsString("TxCoeff"));
                it.setLqi((short) reader.readElementContentAsInt("Lqi"));
                it.setPhaseDifferential((byte) reader.readElementContentAsInt("PhaseDifferential"));
                it.setTMRValidTime((short) reader.readElementContentAsInt("TMRValidTime"));
                it.setNeighbourValidTime((short) reader.readElementContentAsInt("NeighbourValidTime"));
                list.add(it);
            }
            reader.readEndElement("NeighbourTable");
        }
        setNeighbourTable(list.toArray(new GXDLMSNeighbourTable[0]));
    }

    private void loadMacPosTable(GXXmlReader reader) throws XMLStreamException {
        ArrayList<GXDLMSMacPosTable> list = new ArrayList<GXDLMSMacPosTable>();
        if (reader.isStartElement("MacPosTable", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSMacPosTable it = new GXDLMSMacPosTable();
                it.setShortAddress(reader.readElementContentAsInt("ShortAddress"));
                it.setLQI((byte) reader.readElementContentAsInt("LQI"));
                it.setValidTime(reader.readElementContentAsInt("ValidTime"));
                list.add(it);
            }
            reader.readEndElement("MacPosTable");
        }
        setMacPosTable(list.toArray(new GXDLMSMacPosTable[0]));
    }

    @Override
    public final void load(GXXmlReader reader) throws XMLStreamException {
        setShortAddress(reader.readElementContentAsInt("ShortAddress"));
        setRcCoord(reader.readElementContentAsInt("RcCoord"));
        setPANId((short) reader.readElementContentAsInt("PANId"));
        loadKeyTable(reader);
        setFrameCounter((short) reader.readElementContentAsInt("FrameCounter"));
        setToneMask(reader.readElementContentAsString("ToneMask"));
        setTmrTtl((byte) reader.readElementContentAsInt("TmrTtl"));
        setMaxFrameRetries((byte) reader.readElementContentAsInt("MaxFrameRetries"));
        setNeighbourTableEntryTtl((byte) reader.readElementContentAsInt("NeighbourTableEntryTtl"));
        loadNeighbourTable(reader);
        setHighPriorityWindowSize((byte) reader.readElementContentAsInt("HighPriorityWindowSize"));
        setCscmFairnessLimit((byte) reader.readElementContentAsInt("CscmFairnessLimit"));
        setBeaconRandomizationWindowLength((byte) reader.readElementContentAsInt("BeaconRandomizationWindowLength"));
        setA((byte) reader.readElementContentAsInt("A"));
        setK((byte) reader.readElementContentAsInt("K"));
        setMinCwAttempts((byte) reader.readElementContentAsInt("MinCwAttempts"));
        setCenelecLegacyMode((byte) reader.readElementContentAsInt("CenelecLegacyMode"));
        setFccLegacyMode((byte) reader.readElementContentAsInt("FccLegacyMode"));
        setMaxBe((byte) reader.readElementContentAsInt("MaxBe"));
        setMaxCsmaBackoffs((byte) reader.readElementContentAsInt("MaxCsmaBackoffs"));
        setMinBe((byte) reader.readElementContentAsInt("MinBe"));
        setMacBroadcastMaxCwEnabled(reader.readElementContentAsInt("MacBroadcastMaxCwEnabled") != 0);
        setMacTransmitAtten((byte) reader.readElementContentAsInt("MacTransmitAtten"));
        loadMacPosTable(reader);
        setMacDuplicateDetectionTtl((byte) reader.readElementContentAsInt("MacDuplicateDetectionTtl"));
    }

    private void saveKeyTable(GXXmlWriter writer) throws XMLStreamException {
        writer.writeStartElement("KeyTable");
        if (keyTable != null) {
            for (Entry<Short, byte[]> it : keyTable) {
                writer.writeStartElement("Item");
                writer.writeElementString("Key", it.getKey());
                writer.writeElementString("Data", GXDLMSTranslator.toHex(it.getValue()));
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    private void saveNeighbourTable(GXXmlWriter writer) throws XMLStreamException {
        writer.writeStartElement("NeighbourTable");
        if (neighbourTable != null) {
            for (GXDLMSNeighbourTable it : neighbourTable) {
                writer.writeStartElement("Item");
                writer.writeElementString("ShortAddress", it.getShortAddress());
                writer.writeElementString("Enabled", it.getEnabled());
                writer.writeElementString("ToneMap", it.getToneMap());
                writer.writeElementString("Modulation", it.getModulation().getValue());
                writer.writeElementString("TxGain", it.getTxGain());
                writer.writeElementString("TxRes", it.getTxRes().getValue());
                writer.writeElementString("TxCoeff", it.getTxCoeff());
                writer.writeElementString("Lqi", it.getLqi());
                writer.writeElementString("PhaseDifferential", it.getPhaseDifferential());
                writer.writeElementString("TMRValidTime", it.getTMRValidTime());
                writer.writeElementString("NeighbourValidTime", it.getNeighbourValidTime());
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    private void saveMacPosTable(GXXmlWriter writer) throws XMLStreamException {
        writer.writeStartElement("MacPosTable");
        if (getMacPosTable() != null) {
            for (GXDLMSMacPosTable it : getMacPosTable()) {
                writer.writeStartElement("Item");
                writer.writeElementString("ShortAddress", it.getShortAddress());
                writer.writeElementString("LQI", it.getLQI());
                writer.writeElementString("ValidTime", it.getValidTime());
                writer.writeEndElement();
            }
        }
        writer.writeEndElement(); // MacPosTable
    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("ShortAddress", getShortAddress());
        writer.writeElementString("RcCoord", getRcCoord());
        writer.writeElementString("PANId", getPANId());
        saveKeyTable(writer);
        writer.writeElementString("FrameCounter", getFrameCounter());
        writer.writeElementString("ToneMask", getToneMask());
        writer.writeElementString("TmrTtl", getTmrTtl());
        writer.writeElementString("MaxFrameRetries", getMaxFrameRetries());
        writer.writeElementString("NeighbourTableEntryTtl", getNeighbourTableEntryTtl());
        saveNeighbourTable(writer);
        writer.writeElementString("HighPriorityWindowSize", getHighPriorityWindowSize());
        writer.writeElementString("CscmFairnessLimit", getCscmFairnessLimit());
        writer.writeElementString("BeaconRandomizationWindowLength", getBeaconRandomizationWindowLength());
        writer.writeElementString("A", getA());
        writer.writeElementString("K", getK());
        writer.writeElementString("MinCwAttempts", getMinCwAttempts());
        writer.writeElementString("CenelecLegacyMode", getCenelecLegacyMode());
        writer.writeElementString("FccLegacyMode", getFccLegacyMode());
        writer.writeElementString("MaxBe", getMaxBe());
        writer.writeElementString("MaxCsmaBackoffs", getMaxCsmaBackoffs());
        writer.writeElementString("MinBe", getMinBe());
        writer.writeElementString("MacBroadcastMaxCwEnabled", getMacBroadcastMaxCwEnabled());
        writer.writeElementString("MacTransmitAtten", getMacTransmitAtten());
        saveMacPosTable(writer);
        writer.writeElementString("MacDuplicateDetectionTtl", getMacDuplicateDetectionTtl());
    }

    @Override
    public final void postLoad(GXXmlReader reader) {
    }
}
