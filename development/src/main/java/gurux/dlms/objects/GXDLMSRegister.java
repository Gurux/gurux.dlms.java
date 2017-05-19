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
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

import java.text.NumberFormat;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Unit;
import gurux.dlms.internal.GXCommon;

public class GXDLMSRegister extends GXDLMSObject implements IGXDLMSBase {
    private int scaler;
    private int unit;
    private Object objectValue;

    /**
     * Constructor.
     */
    public GXDLMSRegister() {
        super(ObjectType.REGISTER);
        setScaler(1);
        setUnit(Unit.NONE);
    }

    public GXDLMSRegister(final ObjectType type, final String ln,
            final int sn) {
        super(type, ln, sn);
        setScaler(1);
        setUnit(Unit.NONE);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSRegister(final String ln) {
        this(ObjectType.REGISTER, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSRegister(final String ln, final int sn) {
        this(ObjectType.REGISTER, ln, sn);
    }

    /**
     * @return Scaler of COSEM Register object.
     */
    public final double getScaler() {
        return Math.pow(10, scaler);
    }

    /**
     * @param value
     *            Scaler of COSEM Register object.
     */
    public final void setScaler(final double value) {
        scaler = (int) Math.log10(value);
    }

    /**
     * @return Unit of COSEM Register object.
     */
    public final Unit getUnit() {
        return Unit.forValue(unit);
    }

    /**
     * @param value
     *            Unit of COSEM Register object.
     */
    public final void setUnit(final Unit value) {
        unit = value.getValue();
    }

    /**
     * Get value of COSEM Register object.
     * 
     * @return Value of COSEM Register object.
     */
    public final Object getValue() {
        return objectValue;
    }

    /**
     * Set value of COSEM Register object.
     * 
     * @param value
     *            Value of COSEM Register object.
     */
    public final void setValue(final Object value) {
        objectValue = value;
    }

    /*
     * Reset value.
     */
    public final byte[][] reset(final GXDLMSClient client) {
        return client.method(getName(), getObjectType(), 1, new Integer(0),
                DataType.INT8);
    }

    // CHECKSTYLE:OFF
    @Override
    public Object[] getValues() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        String str = "Scaler: " + formatter.format(getScaler());
        str += " Unit: ";
        str += getUnit().toString();
        return new Object[] { getLogicalName(), getValue(), str };
    }
    // CHECKSTYLE:ON

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // Resets the value to the default value.
        // The default value is an instance specific constant.
        if (e.getIndex() == 1) {
            setValue(null);
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
    }

    @Override
    public final boolean isRead(final int index) {
        if (index == 3) {
            return unit != 0;
        }
        return super.isRead(index);
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    // CHECKSTYLE:OFF
    @Override
    public int[] getAttributeIndexToRead() {
        // CHECKSTYLE:ON
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // ScalerUnit
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // Value
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    // CHECKSTYLE:OFF
    @Override
    public int getAttributeCount() {
        return 3;
    }
    // CHECKSTYLE:ON

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 1;
    }

    // CHECKSTYLE:OFF
    @Override
    public DataType getDataType(final int index) {
        // CHECKSTYLE:ON

        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return super.getDataType(index);
        }
        if (index == 3) {
            return DataType.STRUCTURE;
        }
        if (index == 4 && this instanceof GXDLMSExtendedRegister) {
            return super.getDataType(index);
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    // CHECKSTYLE:OFF
    public Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // CHECKSTYLE:ON
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            return getValue();
        }
        if (e.getIndex() == 3) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            GXCommon.setData(data, DataType.INT8, new Integer(scaler));
            GXCommon.setData(data, DataType.ENUM, new Integer(unit));
            return data.array();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    /*
     * Set value of given attribute.
     */
    // CHECKSTYLE:OFF
    @Override
    public void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // CHECKSTYLE:ON

        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            if (scaler != 0) {
                try {
                    objectValue =
                            new Double(((Number) e.getValue()).doubleValue()
                                    * getScaler());
                } catch (Exception e1) {
                    // Sometimes scaler is set for wrong Object type.
                    setValue(e.getValue());
                }
            } else {
                setValue(e.getValue());
            }
        } else if (e.getIndex() == 3) {
            // Set default values.
            if (e.getValue() == null) {
                scaler = 0;
                unit = 0;
            } else {
                Object[] arr = (Object[]) e.getValue();
                if (arr == null || arr.length != 2) {
                    scaler = 0;
                    unit = 0;
                } else {
                    scaler = ((Number) arr[0]).intValue();
                    unit = (((Number) arr[1]).intValue() & 0xFF);
                }
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public void load(final GXXmlReader reader) throws XMLStreamException {
        unit = reader.readElementContentAsInt("Unit", 0);
        setScaler(reader.readElementContentAsDouble("Scaler", 1));
        setValue(reader.readElementContentAsObject("Value", null));
    }

    @Override
    public void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Unit", unit);
        writer.writeElementString("Scaler", getScaler(), 1);
        writer.writeElementObject("Value", getValue());
    }

    @Override
    public void postLoad(final GXXmlReader reader) {
    }

}