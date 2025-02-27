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

import java.math.BigInteger;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXArray;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXStructure;
import gurux.dlms.GXUInt32;
import gurux.dlms.GXUInt64;
import gurux.dlms.GXUInt8;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help:
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSIEC6205541Attributes
 */
public class GXDLMSIec6205541Attributes extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Meter primary account number.
     */
    private GXDLMSMeterPrimaryAccountNumber meterPan;

    /**
     * Meter commodity. This can be ELECTRICITY, WATER, GAS, or TIME.
     */
    private String commodity;

    /**
     * Token carrier types.
     */
    private byte[] tokenCarrierTypes;

    /**
     * Encryption algorithm.
     */
    private short encryptionAlgorithm;
    /**
     * Supply group code.
     */
    private long supplyGroupCode;

    /**
     * Tariff index.
     */
    private short tariffIndex;
    /**
     * Key revision number.
     */
    private short keyRevisionNumber;

    /**
     * Key type.
     */
    private short keyType;
    /**
     * Key expiry number.
     */
    private short keyExpiryNumber;
    /**
     * Number of key change tokens supported by the serve.
     */
    private short kctSupported;
    /**
     * Conformance certificate number.
     */
    private String stsCertificate;

    /**
     * @return Encryption algorithm.
     */
    public final short getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    /**
     * @param value
     *            Encryption algorithm.
     */
    public final void setEncryptionAlgorithm(final short value) {
        encryptionAlgorithm = value;
    }

    /**
     * @return Supply group code.
     */
    public final long getSupplyGroupCode() {
        return supplyGroupCode;
    }

    /**
     * @param value
     *            Supply group code.
     */
    public final void setSupplyGroupCode(final long value) {
        supplyGroupCode = value;
    }

    /**
     * @return Meter primary account number.
     */
    public final GXDLMSMeterPrimaryAccountNumber getMeterPan() {
        return meterPan;
    }

    /**
     * @param value
     *            Meter primary account number.
     */
    public final void setMeterPan(final GXDLMSMeterPrimaryAccountNumber value) {
        meterPan = value;
    }

    /**
     * @return Meter commodity. This can be ELECTRICITY, WATER, GAS, or TIME.
     */
    public final String getCommodity() {
        return commodity;
    }

    /**
     * @param value
     *            Meter commodity. This can be ELECTRICITY, WATER, GAS, or TIME.
     */
    public final void setCommodity(String value) {
        commodity = value;
    }

    /**
     * @return Token carrier types.
     */
    public final byte[] getTokenCarrierTypes() {
        return tokenCarrierTypes;
    }

    /**
     * @param value
     *            Token carrier types.
     */
    public final void setTokenCarrierTypes(final byte[] value) {
        tokenCarrierTypes = value;
    }

    /**
     * @return Tariff index.
     */
    public final short getTariffIndex() {
        return tariffIndex;
    }

    /**
     * @param value
     *            Tariff index.
     */
    public final void setTariffIndex(final short value) {
        tariffIndex = value;
    }

    /**
     * @return Key revision number.
     */
    public final short getKeyRevisionNumber() {
        return keyRevisionNumber;
    }

    /**
     * @param value
     *            Key revision number.
     */
    public final void setKeyRevisionNumber(final short value) {
        keyRevisionNumber = value;
    }

    /**
     * @return Key type.
     */
    public final short getKeyType() {
        return keyType;
    }

    /**
     * @param value
     *            Key type.
     */
    public final void setKeyType(final short value) {
        keyType = value;
    }

    /**
     * @return Key expiry number.
     */
    public final short getKeyExpiryNumber() {
        return keyExpiryNumber;
    }

    /**
     * @param value
     *            Key expiry number.
     */
    public final void setKeyExpiryNumber(final short value) {
        keyExpiryNumber = value;
    }

    /**
     * @return Number of key change tokens supported by the serve.
     */
    public final short getKctSupported() {
        return kctSupported;
    }

    /**
     * @param value
     *            Number of key change tokens supported by the serve.
     */
    public final void setKctSupported(final short value) {
        kctSupported = value;
    }

    /**
     * @return Conformance certificate number.
     */
    public final String getStsCertificate() {
        return stsCertificate;
    }

    /**
     * @param value
     *            Conformance certificate number.
     */
    public final void setStsCertificate(String value) {
        stsCertificate = value;
    }

    /**
     * Constructor.
     */
    public GXDLMSIec6205541Attributes() {
        this("0.0.19.60.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSIec6205541Attributes(String ln) {
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
    public GXDLMSIec6205541Attributes(String ln, int sn) {
        super(ObjectType.IEC_6205541_ATTRIBUTES, ln, sn);
        meterPan = new GXDLMSMeterPrimaryAccountNumber();
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getMeterPan(), getCommodity(), getTokenCarrierTypes(),
                getEncryptionAlgorithm(), getSupplyGroupCode(), getTariffIndex(), getKeyRevisionNumber(), getKeyType(),
                getKeyExpiryNumber(), getKctSupported(), getStsCertificate() };
    }

    @Override
    public final byte[] invoke(GXDLMSSettings settings, ValueEventArgs e) {
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // MeterPan
        if (all || !isRead(2)) {
            attributes.add(2);
        }
        // Commodity
        if (all || !isRead(3)) {
            attributes.add(3);
        }
        // Token carrier types.
        if (all || !isRead(4)) {
            attributes.add(4);
        }
        // Encryption algorithm.
        if (all || !isRead(5)) {
            attributes.add(5);
        }
        // Supply group code.
        if (all || !canRead(6)) {
            attributes.add(6);
        }
        // Tariff index.
        if (all || !canRead(7)) {
            attributes.add(7);
        }
        // Key revision number.
        if (all || !canRead(8)) {
            attributes.add(8);
        }
        // Key type.
        if (all || !canRead(9)) {
            attributes.add(9);
        }
        // Key expiry number.
        if (all || !canRead(10)) {
            attributes.add(10);
        }
        // Kct supported.
        if (all || !super.isRead(11)) {
            attributes.add(11);
        }
        // Sts sertificate.
        if (all || !super.isRead(12)) {
            attributes.add(12);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 12;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 0;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.STRUCTURE;
        case 3:
        case 12:
            return DataType.STRING;
        case 4:
            return DataType.ARRAY;
        case 5:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
            return DataType.UINT8;
        case 6:
            return DataType.UINT32;
        default:
            throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2: {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE);
            data.setUInt8(3);
            GXCommon.setData(settings, data, DataType.UINT32, meterPan.getIssuerId());
            GXCommon.setData(settings, data, DataType.UINT64, meterPan.getDecoderReferenceNumber());
            GXCommon.setData(settings, data, DataType.UINT8, meterPan.getPanCheckDigit());
            return data.array();
        }
        case 3:
            return getCommodity();
        case 4: {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            if (getTokenCarrierTypes() == null) {
                // Object count is zero.
                data.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getTokenCarrierTypes().length, data);
                for (byte it : getTokenCarrierTypes()) {
                    GXCommon.setData(settings, data, DataType.UINT8, it);
                }
            }
            return data.array();
        }
        case 5:
            return getEncryptionAlgorithm();
        case 6:
            return getSupplyGroupCode();
        case 7:
            return getTariffIndex();
        case 8:
            return getKeyRevisionNumber();
        case 9:
            return getKeyType();
        case 10:
            return getKeyExpiryNumber();
        case 11:
            return getKctSupported();
        case 12:
            return getStsCertificate();
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
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            if (e.getValue() instanceof GXStructure) {
                GXStructure s = (GXStructure) e.getValue();
                meterPan.setIssuerId(BigInteger.valueOf(((GXUInt32) s.get(0)).longValue()));
                meterPan.setDecoderReferenceNumber(((GXUInt64) s.get(1)).bigIntegerValue());
                meterPan.setPanCheckDigit(((Number) s.get(2)).shortValue());
            }
            break;
        case 3:
            setCommodity(String.valueOf(e.getValue()));
            break;
        case 4: {
            GXByteBuffer data = new GXByteBuffer();
            if (e.getValue() instanceof GXArray) {
                for (Object it : (GXArray) e.getValue()) {
                    data.setUInt8(((GXUInt8) it).shortValue());
                }
            }
            setTokenCarrierTypes(data.getData());
        }
            break;
        case 5:
            setEncryptionAlgorithm(((Number) e.getValue()).shortValue());
            break;
        case 6:
            setSupplyGroupCode(((Number) e.getValue()).longValue());
            break;
        case 7:
            setTariffIndex(((Number) e.getValue()).shortValue());
            break;
        case 8:
            setKeyRevisionNumber(((Number) e.getValue()).shortValue());
            break;
        case 9:
            setKeyType(((Number) e.getValue()).shortValue());
            break;
        case 10:
            setKeyExpiryNumber(((Number) e.getValue()).shortValue());
            break;
        case 11:
            setKctSupported(((Number) e.getValue()).shortValue());
            break;
        case 12:
            setStsCertificate((String) e.getValue());
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }

    }

    @Override
    public final void load(GXXmlReader reader) throws XMLStreamException {
        String str = reader.readElementContentAsString("IssuerId");
        if (str != null && str != "") {
            meterPan.setIssuerId(new BigInteger(str));
        }
        str = reader.readElementContentAsString("DecoderReferenceNumber");
        if (str != null && str != "") {
            meterPan.setDecoderReferenceNumber(new BigInteger(str));
        }
        meterPan.setPanCheckDigit((short) reader.readElementContentAsInt("PanCheckDigit"));
        commodity = reader.readElementContentAsString("Commodity");
        GXByteBuffer list = new GXByteBuffer();
        if (reader.isStartElement("TokenCarrierTypes", true)) {
            while (reader.isStartElement("Value", false)) {
                list.setUInt8(reader.readElementContentAsInt("Value"));
            }
            reader.readEndElement("TokenCarrierTypes");
        }
        tokenCarrierTypes = list.array();
        setEncryptionAlgorithm((short) reader.readElementContentAsInt("EncryptionAlgorithm"));
        setSupplyGroupCode((long) reader.readElementContentAsULong("SupplyGroupCode"));
        setTariffIndex((byte) reader.readElementContentAsULong("TariffIndex"));
        setKeyRevisionNumber((short) reader.readElementContentAsULong("KeyRevisionNumber"));
        setKeyType((short) reader.readElementContentAsULong("KeyType"));
        setKeyExpiryNumber((byte) reader.readElementContentAsULong("KeyExpiryNumber"));
        setKctSupported((short) reader.readElementContentAsULong("KctSupported"));
        setStsCertificate(reader.readElementContentAsString("StsCertificate"));
    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("IssuerId", String.valueOf(meterPan.getIssuerId()));
        writer.writeElementString("DecoderReferenceNumber", String.valueOf(meterPan.getDecoderReferenceNumber()));
        writer.writeElementString("PanCheckDigit", meterPan.getPanCheckDigit());
        writer.writeElementString("Commodity", commodity);
        writer.writeStartElement("TokenCarrierTypes");
        if (tokenCarrierTypes != null) {
            for (byte it : tokenCarrierTypes) {
                writer.writeElementString("Value", String.valueOf(it));
            }
        }
        writer.writeEndElement();
        writer.writeElementString("EncryptionAlgorithm", getEncryptionAlgorithm());
        writer.writeElementString("SupplyGroupCode", getSupplyGroupCode());
        writer.writeElementString("TariffIndex", getTariffIndex());
        writer.writeElementString("KeyRevisionNumber", getKeyRevisionNumber());
        writer.writeElementString("KeyType", getKeyType());
        writer.writeElementString("KeyExpiryNumber", getKeyExpiryNumber());
        writer.writeElementString("KctSupported", getKctSupported());
        writer.writeElementString("StsCertificate", getStsCertificate());
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }

    @Override
    public String[] getNames() {
        return new String[] { "Logical Name", "MeterPan", "Commodity", "TokenCarrierTypes", "EncryptionAlgorithm",
                "SupplyGroupCode", "TariffIndex", "KeyRevisionNumber", "KeyType", "KeyExpiryNumber", "KctSupported",
                "StsCertificate" };

    }

    @Override
    public String[] getMethodNames() {
        return new String[0];
    }
}
