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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSPrimeNbOfdmPlcMacNetworkAdministrationData
 */
public class GXDLMSPrimeNbOfdmPlcMacNetworkAdministrationData
        extends GXDLMSObject implements IGXDLMSBase {

    /**
     * List of entries in multicast switching table.
     */

    private GXMacMulticastEntry[] multicastEntries;
    /**
     * Switch table.
     */
    private short[] switchTable;

    /**
     * List of entries in multicast switching table.
     */
    private GXMacDirectTable[] directTable;
    /**
     * List of available switches.
     */
    private GXMacAvailableSwitch[] availableSwitches;
    /**
     * List of PHY communication parameters.
     */
    private GXMacPhyCommunication[] communications;

    /**
     * Constructor.
     */
    public GXDLMSPrimeNbOfdmPlcMacNetworkAdministrationData() {
        this("0.0.28.5.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSPrimeNbOfdmPlcMacNetworkAdministrationData(final String ln) {
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
    public GXDLMSPrimeNbOfdmPlcMacNetworkAdministrationData(final String ln,
            final int sn) {
        super(ObjectType.PRIME_NB_OFDM_PLC_MAC_NETWORK_ADMINISTRATION_DATA, ln,
                sn);
    }

    /**
     * @return List of entries in multicast switching table.
     */
    public final GXMacMulticastEntry[] getMulticastEntries() {
        return multicastEntries;
    }

    /**
     * @param value
     *            List of entries in multicast switching table.
     */
    public final void setMulticastEntries(final GXMacMulticastEntry[] value) {
        multicastEntries = value;
    }

    /**
     * @return Switch table.
     */
    public final short[] getSwitchTable() {
        return switchTable;
    }

    /**
     * @param value
     *            Switch table.
     */
    public final void setSwitchTable(final short[] value) {
        switchTable = value;
    }

    /**
     * @return List of entries in multicast switching table.
     */
    public final GXMacDirectTable[] getDirectTable() {
        return directTable;
    }

    /**
     * @param value
     *            List of entries in multicast switching table.
     */
    public final void setDirectTable(final GXMacDirectTable[] value) {
        directTable = value;
    }

    /**
     * @return List of available switches.
     */
    public final GXMacAvailableSwitch[] getAvailableSwitches() {
        return availableSwitches;
    }

    /**
     * @param value
     *            List of available switches.
     */
    public final void setAvailableSwitches(final GXMacAvailableSwitch[] value) {
        availableSwitches = value;
    }

    /**
     * @return List of PHY communication parameters.
     */
    public final GXMacPhyCommunication[] getCommunications() {
        return communications;
    }

    /**
     * @param value
     *            List of PHY communication parameters.
     */
    public final void setCommunications(final GXMacPhyCommunication[] value) {
        communications = value;
    }

    /**
     * Reset the values.
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
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), multicastEntries, switchTable,
                directTable, availableSwitches, communications };
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            multicastEntries = null;
            switchTable = null;
            directTable = null;
            availableSwitches = null;
            communications = null;
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
        // MulticastEntries
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // SwitchTable
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // DirectTable
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // AvailableSwitches
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // Communications
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
        case 6:
            return DataType.ARRAY;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    private byte[] getMulticastEntries(final GXDLMSSettings settings) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY.getValue());
        if (multicastEntries == null) {
            GXCommon.setObjectCount(0, bb);
        } else {
            GXCommon.setObjectCount(multicastEntries.length, bb);
            for (GXMacMulticastEntry it : multicastEntries) {
                bb.setUInt8((byte) DataType.STRUCTURE.getValue());
                bb.setUInt8(2);
                GXCommon.setData(settings, bb, DataType.INT8, it.getId());
                GXCommon.setData(settings, bb, DataType.INT16, it.getMembers());
            }
        }
        return bb.array();
    }

    private byte[] getSwitchTable(final GXDLMSSettings settings) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY.getValue());
        if (switchTable == null) {
            GXCommon.setObjectCount(0, bb);
        } else {
            GXCommon.setObjectCount(switchTable.length, bb);
            for (short it : switchTable) {
                GXCommon.setData(settings, bb, DataType.INT16, it);
            }
        }
        return bb.array();
    }

    private byte[] getDirectTable(final GXDLMSSettings settings) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY.getValue());
        if (directTable == null) {
            GXCommon.setObjectCount(0, bb);
        } else {
            GXCommon.setObjectCount(directTable.length, bb);
            for (GXMacDirectTable it : directTable) {
                bb.setUInt8(DataType.STRUCTURE.getValue());
                bb.setUInt8(7);
                GXCommon.setData(settings, bb, DataType.INT16,
                        it.getSourceSId());
                GXCommon.setData(settings, bb, DataType.INT16,
                        it.getSourceLnId());
                GXCommon.setData(settings, bb, DataType.INT16,
                        it.getSourceLcId());
                GXCommon.setData(settings, bb, DataType.INT16,
                        it.getDestinationSId());
                GXCommon.setData(settings, bb, DataType.INT16,
                        it.getDestinationLnId());
                GXCommon.setData(settings, bb, DataType.INT16,
                        it.getDestinationLcId());
                GXCommon.setData(settings, bb, DataType.OCTET_STRING,
                        it.getDid());
            }
        }
        return bb.array();
    }

    private byte[] getAvailableSwitches(final GXDLMSSettings settings) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY.getValue());
        if (availableSwitches == null) {
            GXCommon.setObjectCount(0, bb);
        } else {
            GXCommon.setObjectCount(availableSwitches.length, bb);
            for (GXMacAvailableSwitch it : availableSwitches) {
                bb.setUInt8(DataType.STRUCTURE.getValue());
                bb.setUInt8(5);
                GXCommon.setData(settings, bb, DataType.OCTET_STRING,
                        it.getSna());
                GXCommon.setData(settings, bb, DataType.INT16, it.getLsId());
                GXCommon.setData(settings, bb, DataType.INT8, it.getLevel());
                GXCommon.setData(settings, bb, DataType.INT8, it.getRxLevel());
                GXCommon.setData(settings, bb, DataType.INT8, it.getRxSnr());
            }
        }

        return bb.array();
    }

    private byte[] getCommunications(final GXDLMSSettings settings) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY.getValue());
        if (communications == null) {
            GXCommon.setObjectCount(0, bb);
        } else {
            GXCommon.setObjectCount(communications.length, bb);
            for (GXMacPhyCommunication it : communications) {
                bb.setUInt8(DataType.STRUCTURE.getValue());
                bb.setUInt8(9);
                GXCommon.setData(settings, bb, DataType.OCTET_STRING,
                        it.getEui());
                GXCommon.setData(settings, bb, DataType.INT8, it.getTxPower());
                GXCommon.setData(settings, bb, DataType.INT8, it.getTxCoding());
                GXCommon.setData(settings, bb, DataType.INT8, it.getRxCoding());
                GXCommon.setData(settings, bb, DataType.INT8, it.getRxLvl());
                GXCommon.setData(settings, bb, DataType.INT8, it.getSnr());
                GXCommon.setData(settings, bb, DataType.INT8,
                        it.getTxPowerModified());
                GXCommon.setData(settings, bb, DataType.INT8,
                        it.getTxCodingModified());
                GXCommon.setData(settings, bb, DataType.INT8,
                        it.getRxCodingModified());
            }
        }
        return bb.array();
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
            return getMulticastEntries(settings);
        case 3:
            return getSwitchTable(settings);
        case 4:
            return getDirectTable(settings);
        case 5:
            return getAvailableSwitches(settings);
        case 6:
            return getCommunications(settings);
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    private GXMacMulticastEntry[] setMulticastEntry(final List<Object> value) {
        List<GXMacMulticastEntry> data = new ArrayList<GXMacMulticastEntry>();
        if (value != null) {
            for (Object tmp : value) {
                List<Object> it = (List<Object>) tmp;
                GXMacMulticastEntry v = new GXMacMulticastEntry();
                v.setId((byte) (it.get(0)));
                v.setMembers((short) it.get(1));
                data.add(v);
            }
        }
        return data.toArray(new GXMacMulticastEntry[data.size()]);
    }

    private short[] setSwitchTable(final List<Object> value) {
        List<Short> data = new ArrayList<Short>();
        if (value != null) {
            for (Object it : value) {
                data.add((Short) it);
            }
        }
        return GXCommon.toShortArray(data);
    }

    private GXMacDirectTable[] setDirectTable(final List<Object> value) {
        List<GXMacDirectTable> data = new ArrayList<GXMacDirectTable>();
        if (value != null) {
            for (Object tmp : value) {
                List<Object> it = (List<Object>) tmp;
                GXMacDirectTable v = new GXMacDirectTable();
                v.setSourceSId((short) it.get(0));
                v.setSourceLnId((short) it.get(1));
                v.setSourceLcId((short) it.get(2));
                v.setDestinationSId((short) it.get(3));
                v.setDestinationLnId((short) it.get(4));
                v.setDestinationLcId((short) it.get(5));
                v.setDid((byte[]) it.get(6));
                data.add(v);
            }
        }
        return data.toArray(new GXMacDirectTable[data.size()]);
    }

    private GXMacAvailableSwitch[]
            setAvailableSwitches(final List<Object> value) {
        List<GXMacAvailableSwitch> data = new ArrayList<GXMacAvailableSwitch>();
        if (value != null) {
            for (Object tmp : value) {
                List<Object> it = (List<Object>) tmp;
                GXMacAvailableSwitch v = new GXMacAvailableSwitch();
                v.setSna((byte[]) it.get(0));
                v.setLsId((int) (it.get(1)));
                v.setLevel((short) it.get(2));
                v.setRxLevel((short) it.get(3));
                v.setRxSnr((short) it.get(4));
                data.add(v);
            }
        }
        return data.toArray(new GXMacAvailableSwitch[data.size()]);
    }

    private GXMacPhyCommunication[]
            setCommunications(final List<Object> value) {
        List<GXMacPhyCommunication> data =
                new ArrayList<GXMacPhyCommunication>();
        if (value != null) {
            for (Object tmp : value) {
                List<Object> it = (List<Object>) tmp;
                GXMacPhyCommunication v = new GXMacPhyCommunication();
                v.setEui((byte[]) it.get(0));
                v.setTxPower(((Number) it.get(1)).byteValue());
                v.setTxCoding(((Number) it.get(2)).byteValue());
                v.setRxCoding(((Number) it.get(3)).byteValue());
                v.setRxLvl(((Number) it.get(4)).byteValue());
                v.setSnr(((Number) it.get(5)).byteValue());
                v.setTxPowerModified(((Number) it.get(6)).byteValue());
                v.setTxCodingModified(((Number) it.get(7)).byteValue());
                v.setRxCodingModified(((Number) it.get(8)).byteValue());
                data.add(v);
            }
        }
        return data.toArray(new GXMacPhyCommunication[data.size()]);
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
            multicastEntries = setMulticastEntry((List<Object>) e.getValue());
            break;
        case 3:
            switchTable = setSwitchTable((List<Object>) e.getValue());
            break;
        case 4:
            directTable = setDirectTable((List<Object>) e.getValue());
            break;
        case 5:
            availableSwitches =
                    setAvailableSwitches((List<Object>) e.getValue());
            break;
        case 6:
            communications = setCommunications((List<Object>) e.getValue());
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    private GXMacMulticastEntry[] loadMulticastEntries(GXXmlReader reader)
            throws XMLStreamException {
        List<GXMacMulticastEntry> list = new ArrayList<GXMacMulticastEntry>();
        if (reader.isStartElement("MulticastEntries", true)) {
            while (reader.isStartElement("Item", true)) {
                GXMacMulticastEntry it = new GXMacMulticastEntry();
                list.add(it);
                it.setId((byte) reader.readElementContentAsInt("Id"));
                it.setMembers(
                        (short) reader.readElementContentAsInt("Members"));
            }
            reader.readEndElement("MulticastEntries");
        }
        return list.toArray(new GXMacMulticastEntry[list.size()]);
    }

    private short[] loadSwitchTable(GXXmlReader reader)
            throws XMLStreamException {
        List<Short> list = new ArrayList<Short>();
        if (reader.isStartElement("SwitchTable", true)) {
            while (reader.isStartElement("Item", false)) {
                list.add((short) reader.readElementContentAsInt("Item"));
            }
            reader.readEndElement("SwitchTable");
        }
        return GXCommon.toShortArray(list);
    }

    private GXMacDirectTable[] loadDirectTable(GXXmlReader reader)
            throws XMLStreamException {
        List<GXMacDirectTable> list = new ArrayList<GXMacDirectTable>();
        if (reader.isStartElement("DirectTable", true)) {
            while (reader.isStartElement("Item", true)) {
                GXMacDirectTable it = new GXMacDirectTable();
                list.add(it);
                it.setSourceSId(
                        (short) reader.readElementContentAsInt("SourceSId"));
                it.setSourceLnId(
                        (short) reader.readElementContentAsInt("SourceLnId"));
                it.setSourceLcId(
                        (short) reader.readElementContentAsInt("SourceLcId"));
                it.setDestinationSId((short) reader
                        .readElementContentAsInt("DestinationSId"));
                it.setDestinationLnId((short) reader
                        .readElementContentAsInt("DestinationLnId"));
                it.setDestinationLcId((short) reader
                        .readElementContentAsInt("DestinationLcId"));
                it.setDid(GXCommon
                        .hexToBytes(reader.readElementContentAsString("Did")));
            }
            reader.readEndElement("DirectTable");
        }
        return list.toArray(new GXMacDirectTable[list.size()]);
    }

    private GXMacAvailableSwitch[] loadAvailableSwitches(GXXmlReader reader)
            throws XMLStreamException {
        List<GXMacAvailableSwitch> list = new ArrayList<GXMacAvailableSwitch>();
        if (reader.isStartElement("AvailableSwitches", true)) {
            while (reader.isStartElement("Item", true)) {
                GXMacAvailableSwitch it = new GXMacAvailableSwitch();
                list.add(it);
                it.setSna(GXCommon
                        .hexToBytes(reader.readElementContentAsString("Sna")));
                it.setLsId(reader.readElementContentAsInt("LsId"));
                it.setLevel((short) reader.readElementContentAsInt("Level"));
                it.setRxLevel(
                        (short) reader.readElementContentAsInt("RxLevel"));
                it.setRxSnr((short) reader.readElementContentAsInt("RxSnr"));
            }
            reader.readEndElement("AvailableSwitches");
        }
        return list.toArray(new GXMacAvailableSwitch[list.size()]);
    }

    private GXMacPhyCommunication[] loadCommunications(GXXmlReader reader)
            throws XMLStreamException {
        List<GXMacPhyCommunication> list =
                new ArrayList<GXMacPhyCommunication>();
        if (reader.isStartElement("Communications", true)) {
            while (reader.isStartElement("Item", true)) {
                GXMacPhyCommunication it = new GXMacPhyCommunication();
                list.add(it);
                it.setEui(GXCommon
                        .hexToBytes(reader.readElementContentAsString("Eui")));
                it.setTxPower(
                        (short) reader.readElementContentAsInt("TxPower"));
                it.setTxCoding(
                        (short) reader.readElementContentAsInt("TxCoding"));
                it.setRxCoding(
                        (short) reader.readElementContentAsInt("RxCoding"));
                it.setRxLvl((short) reader.readElementContentAsInt("RxLvl"));
                it.setSnr((short) reader.readElementContentAsInt("Snr"));
                it.setTxPowerModified((short) reader
                        .readElementContentAsInt("TxPowerModified"));
                it.setTxCodingModified((short) reader
                        .readElementContentAsInt("TxCodingModified"));
                it.setRxCodingModified((short) reader
                        .readElementContentAsInt("RxCodingModified"));
            }
            reader.readEndElement("Communications");
        }
        return list.toArray(new GXMacPhyCommunication[list.size()]);
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        multicastEntries = loadMulticastEntries(reader);
        switchTable = loadSwitchTable(reader);
        directTable = loadDirectTable(reader);
        availableSwitches = loadAvailableSwitches(reader);
        communications = loadCommunications(reader);
    }

    private void saveMulticastEntries(GXXmlWriter writer)
            throws XMLStreamException {
        writer.writeStartElement("MulticastEntries");
        if (multicastEntries != null) {
            for (GXMacMulticastEntry it : multicastEntries) {
                writer.writeStartElement("Item");
                writer.writeElementString("Id", it.getId());
                writer.writeElementString("Members", it.getMembers());
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    private void saveSwitchTable(GXXmlWriter writer) throws XMLStreamException {
        writer.writeStartElement("SwitchTable");
        if (switchTable != null) {
            for (short it : switchTable) {
                writer.writeElementString("Item", it);
            }
        }
        writer.writeEndElement();
    }

    private void saveDirectTable(GXXmlWriter writer) throws XMLStreamException {
        writer.writeStartElement("DirectTable");
        if (directTable != null) {
            for (GXMacDirectTable it : directTable) {
                writer.writeStartElement("Item");
                writer.writeElementString("SourceSId", it.getSourceSId());
                writer.writeElementString("SourceLnId", it.getSourceLnId());
                writer.writeElementString("SourceLcId", it.getSourceLcId());
                writer.writeElementString("DestinationSId",
                        it.getDestinationSId());
                writer.writeElementString("DestinationLnId",
                        it.getDestinationLnId());
                writer.writeElementString("DestinationLcId",
                        it.getDestinationLcId());
                writer.writeElementString("Did",
                        GXCommon.toHex(it.getDid(), false));
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    private void saveAvailableSwitches(GXXmlWriter writer)
            throws XMLStreamException {
        writer.writeStartElement("AvailableSwitches");
        if (availableSwitches != null) {
            for (GXMacAvailableSwitch it : availableSwitches) {
                writer.writeStartElement("Item");
                writer.writeElementString("Sna",
                        GXCommon.toHex(it.getSna(), false));
                writer.writeElementString("LsId", it.getLsId());
                writer.writeElementString("Level", it.getLevel());
                writer.writeElementString("RxLevel", it.getRxLevel());
                writer.writeElementString("RxSnr", it.getRxSnr());
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    private void saveCommunications(GXXmlWriter writer)
            throws XMLStreamException {
        writer.writeStartElement("Communications");
        if (communications != null) {
            for (GXMacPhyCommunication it : communications) {
                writer.writeStartElement("Item");
                writer.writeElementString("Eui",
                        GXCommon.toHex(it.getEui(), false));
                writer.writeElementString("TxPower", it.getTxPower());
                writer.writeElementString("TxCoding", it.getTxCoding());
                writer.writeElementString("RxCoding", it.getRxCoding());
                writer.writeElementString("RxLvl", it.getRxLvl());
                writer.writeElementString("Snr", it.getSnr());
                writer.writeElementString("TxPowerModified",
                        it.getTxPowerModified());
                writer.writeElementString("TxCodingModified",
                        it.getTxCodingModified());
                writer.writeElementString("RxCodingModified",
                        it.getRxCodingModified());
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        saveMulticastEntries(writer);
        saveSwitchTable(writer);
        saveDirectTable(writer);
        saveAvailableSwitches(writer);
        saveCommunications(writer);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}