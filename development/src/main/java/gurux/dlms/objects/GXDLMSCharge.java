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
import gurux.dlms.objects.enums.ChargeType;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
 */
public class GXDLMSCharge extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Total amount paid.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private int totalAmountPaid;

    /**
     * Charge type.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private ChargeType chargeType;
    /**
     * Priority.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private byte priority;
    /**
     * Unit charge active.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private GXUnitCharge unitChargeActive;
    /**
     * Unit charge passive.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private GXUnitCharge unitChargePassive;
    /**
     * Unit charge activation time.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private GXDateTime unitChargeActivationTime;
    /**
     * Period.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private int period;
    /**
     * Charge configuration.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private String chargeConfiguration;
    /**
     * Last collection time.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private GXDateTime lastCollectionTime;

    /**
     * Last collection amount.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private int lastCollectionAmount;
    /**
     * Total amount remaining.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private int totalAmountRemaining;
    /**
     * Proportion.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     */
    private int proportion;

    /**
     * Constructor.
     */
    public GXDLMSCharge() {
        this("0.0.19.20.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSCharge(final String ln) {
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
    public GXDLMSCharge(final String ln, final int sn) {
        super(ObjectType.CHARGE, ln, sn);
        unitChargeActive = new GXUnitCharge();
        unitChargePassive = new GXUnitCharge();
        chargeType = ChargeType.CONSUMPTION_BASED_COLLECTION;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Total amount paid.
     */
    public final int getTotalAmountPaid() {
        return totalAmountPaid;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Total amount paid.
     */
    public final void setTotalAmountPaid(final int value) {
        totalAmountPaid = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Charge type.
     */
    public final ChargeType getChargeType() {
        return chargeType;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Charge type.
     */
    public final void setChargeType(final ChargeType value) {
        chargeType = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Priority
     */
    public final byte getPriority() {
        return priority;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Priority
     */
    public final void setPriority(final byte value) {
        priority = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Unit charge active.
     */
    public final GXUnitCharge getUnitChargeActive() {
        return unitChargeActive;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Unit charge active.
     */
    public final void setUnitChargeActive(final GXUnitCharge value) {
        unitChargeActive = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Unit charge passive.
     */
    public final GXUnitCharge getUnitChargePassive() {
        return unitChargePassive;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Unit charge passive.
     */
    public final void setUnitChargePassive(final GXUnitCharge value) {
        unitChargePassive = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Unit charge activation time.
     */
    public final GXDateTime getUnitChargeActivationTime() {
        return unitChargeActivationTime;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Unit charge activation time.
     */
    public final void setUnitChargeActivationTime(final GXDateTime value) {
        unitChargeActivationTime = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Period.
     */
    public final int getPeriod() {
        return period;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Period.
     */
    public final void setPeriod(final int value) {
        period = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Charge configuration.
     */
    public final String getChargeConfiguration() {
        return chargeConfiguration;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Charge configuration.
     */
    public final void setChargeConfiguration(final String value) {
        chargeConfiguration = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Last collection time.
     */
    public final GXDateTime getLastCollectionTime() {
        return lastCollectionTime;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Last collection time.
     */
    public final void setLastCollectionTime(final GXDateTime value) {
        lastCollectionTime = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Last collection amount.
     */
    public final int getLastCollectionAmount() {
        return lastCollectionAmount;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Last collection amount.
     */
    public final void setLastCollectionAmount(final int value) {
        lastCollectionAmount = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Total amount remaining
     */
    public final int getTotalAmountRemaining() {
        return totalAmountRemaining;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Total amount remaining
     */
    public final void setTotalAmountRemaining(final int value) {
        totalAmountRemaining = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @return Proportion
     */
    public final int getProportion() {
        return proportion;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCharge
     * 
     * @param value
     *            Proportion
     */
    public final void setProportion(final int value) {
        proportion = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), totalAmountPaid, chargeType,
                priority, unitChargeActive, unitChargePassive,
                unitChargeActivationTime, period, chargeConfiguration,
                lastCollectionTime, lastCollectionAmount, totalAmountRemaining,
                proportion };
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
        // TotalAmountPaid
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // ChargeType
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // Priority
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // UnitChargeActive
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // UnitChargePassive
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // UnitChargeActivationTime
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // Period
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // ChargeConfiguration
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // LastCollectionTime
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // LastCollectionAmount
        if (all || canRead(11)) {
            attributes.add(11);
        }
        // TotalAmountRemaining
        if (all || canRead(12)) {
            attributes.add(12);
        }
        // Proportion
        if (all || canRead(13)) {
            attributes.add(13);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 13;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 5;
    }

    @Override
    public final DataType getDataType(final int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.INT32;
        case 3:
            return DataType.ENUM;
        case 4:
            return DataType.UINT8;
        case 5:
            return DataType.STRUCTURE;
        case 6:
            return DataType.STRUCTURE;
        case 7:
            return DataType.OCTET_STRING;
        case 8:
            return DataType.UINT32;
        case 9:
            return DataType.BITSTRING;
        case 10:
            return DataType.OCTET_STRING;
        case 11:
            return DataType.INT32;
        case 12:
            return DataType.INT32;
        case 13:
            return DataType.UINT16;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    private static byte[] getUnitCharge(GXUnitCharge charge) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(3);
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(2);
        GXCommon.setData(null, bb, DataType.INT8,
                charge.getChargePerUnitScaling().getCommodityScale());
        GXCommon.setData(null, bb, DataType.INT8,
                charge.getChargePerUnitScaling().getPriceScale());
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(3);
        if (charge.getCommodity().getTarget() == null) {
            GXCommon.setData(null, bb, DataType.UINT16, 0);
            bb.setUInt8(DataType.OCTET_STRING.getValue());
            bb.setUInt8(6);
            bb.setUInt8(0);
            bb.setUInt8(0);
            bb.setUInt8(0);
            bb.setUInt8(0);
            bb.setUInt8(0);
            bb.setUInt8(0);
            GXCommon.setData(null, bb, DataType.INT8, 0);
        } else {
            GXCommon.setData(null, bb, DataType.UINT16,
                    charge.getCommodity().getTarget().getObjectType());
            GXCommon.setData(null, bb, DataType.OCTET_STRING,
                    GXCommon.logicalNameToBytes(charge.getCommodity()
                            .getTarget().getLogicalName()));
            GXCommon.setData(null, bb, DataType.INT8,
                    charge.getCommodity().getIndex());
        }
        bb.setUInt8(DataType.ARRAY.getValue());
        if (charge.getChargeTables() == null) {
            bb.setUInt8(0);
        } else {
            GXCommon.setObjectCount(charge.getChargeTables().length, bb);
            for (GXChargeTable it : charge.getChargeTables()) {
                bb.setUInt8(DataType.STRUCTURE.getValue());
                bb.setUInt8(2);
                GXCommon.setData(null, bb, DataType.OCTET_STRING,
                        it.getIndex());
                GXCommon.setData(null, bb, DataType.INT16,
                        it.getChargePerUnit());
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
            return totalAmountPaid;
        case 3:
            return chargeType.getValue();
        case 4:
            return priority;
        case 5:
            return getUnitCharge(unitChargeActive);
        case 6:
            return getUnitCharge(unitChargePassive);
        case 7:
            return unitChargeActivationTime;
        case 8:
            return period;
        case 9:
            return chargeConfiguration;
        case 10:
            return lastCollectionTime;
        case 11:
            return lastCollectionAmount;
        case 12:
            return totalAmountRemaining;
        case 13:
            return proportion;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    private void setUnitCharge(final GXDLMSSettings settings,
            GXUnitCharge charge, Object value) {
        Object[] tmp = (Object[]) value;
        Object[] tmp2 = (Object[]) tmp[0];
        charge.getChargePerUnitScaling()
                .setCommodityScale(((Number) tmp2[0]).shortValue());
        charge.getChargePerUnitScaling()
                .setPriceScale(((Number) tmp2[1]).shortValue());
        tmp2 = (Object[]) tmp[1];
        ObjectType ot = ObjectType.forValue(((Number) tmp2[0]).intValue());
        String ln = GXCommon.toLogicalName(tmp2[1]);
        charge.getCommodity().setTarget(settings.getObjects().findByLN(ot, ln));
        charge.getCommodity().setIndex(((Number) tmp2[2]).intValue());
        List<GXChargeTable> list = new ArrayList<GXChargeTable>();
        tmp2 = (Object[]) tmp[2];
        for (Object tmp3 : tmp2) {
            Object[] it = (Object[]) tmp3;
            GXChargeTable item = new GXChargeTable();
            item.setIndex(new String((byte[]) it[0]));
            item.setChargePerUnit(((Number) it[1]).shortValue());
            list.add(item);
        }
        charge.setChargeTables(list.toArray(new GXChargeTable[0]));
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
            totalAmountPaid = ((Number) e.getValue()).intValue();
            break;
        case 3:
            chargeType =
                    ChargeType.forValue(((Number) e.getValue()).intValue());
            break;
        case 4:
            priority = ((Number) e.getValue()).byteValue();
            break;
        case 5:
            setUnitCharge(settings, unitChargeActive, e.getValue());
            break;
        case 6:
            setUnitCharge(settings, unitChargePassive, e.getValue());
            break;
        case 7:
            unitChargeActivationTime = (GXDateTime) GXDLMSClient
                    .changeType((byte[]) e.getValue(), DataType.DATETIME);
            break;
        case 8:
            period = ((Number) e.getValue()).intValue();
            break;
        case 9:
            chargeConfiguration = String.valueOf(e.getValue());
            break;
        case 10:
            lastCollectionTime = (GXDateTime) GXDLMSClient
                    .changeType((byte[]) e.getValue(), DataType.DATETIME);
            break;
        case 11:
            lastCollectionAmount = ((Number) e.getValue()).intValue();
            break;
        case 12:
            totalAmountRemaining = ((Number) e.getValue()).intValue();
            break;
        case 13:
            proportion = ((Number) e.getValue()).intValue();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    private static void loadUnitChargeActive(GXXmlReader reader, String name,
            GXUnitCharge charge) {

    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        totalAmountPaid = reader.readElementContentAsInt("TotalAmountPaid");
        chargeType = ChargeType
                .forValue(reader.readElementContentAsInt("ChargeType"));
        priority = (byte) reader.readElementContentAsInt("Priority");
        loadUnitChargeActive(reader, "UnitChargeActive", unitChargeActive);
        loadUnitChargeActive(reader, "UnitChargePassive", unitChargePassive);
        String tmp =
                reader.readElementContentAsString("UnitChargeActivationTime");
        if (tmp != null) {
            unitChargeActivationTime = new GXDateTime(tmp);
        }
        period = reader.readElementContentAsInt("Period");
        chargeConfiguration =
                reader.readElementContentAsString("ChargeConfiguration");
        tmp = reader.readElementContentAsString("LastCollectionTime");
        if (tmp != null) {
            lastCollectionTime = new GXDateTime(tmp);
        }
        lastCollectionAmount =
                reader.readElementContentAsInt("LastCollectionAmount");
        totalAmountRemaining =
                reader.readElementContentAsInt("TotalAmountRemaining");
        proportion = reader.readElementContentAsInt("Proportion");
    }

    private static void saveUnitChargeActive(GXXmlWriter writer, String name,
            GXUnitCharge charge) {

    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("TotalAmountPaid", totalAmountPaid);
        if (chargeType != null) {
            writer.writeElementString("ChargeType", chargeType.getValue());
        }
        writer.writeElementString("Priority", priority);
        saveUnitChargeActive(writer, "UnitChargeActive", unitChargeActive);
        saveUnitChargeActive(writer, "UnitChargePassive", unitChargePassive);
        writer.writeElementString("UnitChargeActivationTime",
                unitChargeActivationTime);
        writer.writeElementString("Period", period);
        writer.writeElementString("ChargeConfiguration", chargeConfiguration);
        writer.writeElementString("LastCollectionTime", lastCollectionTime);
        writer.writeElementString("LastCollectionAmount", lastCollectionAmount);
        writer.writeElementString("TotalAmountRemaining", totalAmountRemaining);
        writer.writeElementString("Proportion", proportion);

    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}