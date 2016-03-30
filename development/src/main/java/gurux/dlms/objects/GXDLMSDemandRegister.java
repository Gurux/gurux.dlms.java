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

import java.lang.reflect.Array;
import java.math.BigInteger;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Unit;
import gurux.dlms.internal.GXCommon;

public class GXDLMSDemandRegister extends GXDLMSObject implements IGXDLMSBase {
    private int scaler;
    private int unit;
    private Object currentAvarageValue;
    private Object lastAvarageValue;
    private Object status;
    private GXDateTime captureTime = new GXDateTime();
    private GXDateTime startTimeCurrent = new GXDateTime();
    private int numberOfPeriods;
    private BigInteger period;

    /**
     * Constructor.
     */
    public GXDLMSDemandRegister() {
        this(null);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSDemandRegister(final String ln) {
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
    public GXDLMSDemandRegister(final String ln, final int sn) {
        super(ObjectType.DEMAND_REGISTER, ln, sn);
        setScaler(1);
    }

    /**
     * @return Current average value of COSEM Data object.
     */
    public final Object getCurrentAvarageValue() {
        return currentAvarageValue;
    }

    /**
     * @param value
     *            Current average value of COSEM Data object.
     */
    public final void setCurrentAvarageValue(final Object value) {
        currentAvarageValue = value;
    }

    /**
     * @return Last average value of COSEM Data object.
     */
    public final Object getLastAvarageValue() {
        return lastAvarageValue;
    }

    /**
     * @param value
     *            Last average value of COSEM Data object.
     */
    public final void setLastAvarageValue(final Object value) {
        lastAvarageValue = value;
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
     * @return Status of COSEM Register object.
     */
    public final Object getStatus() {
        return status;
    }

    /**
     * @param value
     *            Status of COSEM Register object.
     */
    public final void setStatus(final Object value) {
        status = value;
    }

    /**
     * @return Capture time of COSEM Register object.
     */
    public final GXDateTime getCaptureTime() {
        return captureTime;
    }

    /**
     * @param value
     *            Capture time of COSEM Register object.
     */
    public final void setCaptureTime(final GXDateTime value) {
        captureTime = value;
    }

    /**
     * @return Current start time of COSEM Register object.
     */
    public final GXDateTime getStartTimeCurrent() {
        return startTimeCurrent;
    }

    /**
     * @param value
     *            Current start time of COSEM Register object.
     */
    public final void setStartTimeCurrent(final GXDateTime value) {
        startTimeCurrent = value;
    }

    public final BigInteger getPeriod() {
        return period;
    }

    public final void setPeriod(final BigInteger value) {
        period = value;
    }

    public final int getNumberOfPeriods() {
        return numberOfPeriods;
    }

    public final void setNumberOfPeriods(final int value) {
        numberOfPeriods = value;
    }

    /**
     * Reset value.
     */
    void reset() {

    }

    /**
     * Next period.
     */
    void nextPeriod() {

    }

    @Override
    public final Object[] getValues() {
        String str = String.format("Scaler: %1$,.2f Unit: ", getScaler());
        str += getUnit().toString();
        return new Object[] { getLogicalName(), getCurrentAvarageValue(),
                getLastAvarageValue(), str, getStatus(), getCaptureTime(),
                getStartTimeCurrent(), getPeriod(), getNumberOfPeriods() };
    }

    @Override
    public final boolean isRead(final int index) {
        if (index == 4) {
            return unit != 0;
        }
        return super.isRead(index);
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // ScalerUnit
        if (!isRead(4)) {
            attributes.add(4);
        }
        // CurrentAvarageValue
        if (canRead(2)) {
            attributes.add(2);
        }
        // LastAvarageValue
        if (canRead(3)) {
            attributes.add(3);
        }
        // Status
        if (canRead(5)) {
            attributes.add(5);
        }
        // CaptureTime
        if (canRead(6)) {
            attributes.add(6);
        }
        // StartTimeCurrent
        if (canRead(7)) {
            attributes.add(7);
        }
        // Period
        if (canRead(8)) {
            attributes.add(8);
        }
        // NumberOfPeriods
        if (canRead(9)) {
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
        return 2;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return super.getDataType(index);
        }
        if (index == 3) {
            return super.getDataType(index);
        }
        if (index == 4) {
            return DataType.ARRAY;
        }
        if (index == 5) {
            return super.getDataType(index);
        }
        if (index == 6) {
            return DataType.OCTET_STRING;
        }
        if (index == 7) {
            return DataType.OCTET_STRING;
        }
        if (index == 8) {
            return DataType.UINT32;
        }
        if (index == 9) {
            return DataType.UINT16;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings, final int index,
            final int selector, final Object parameters) {
        if (index == 1) {
            return getLogicalName();
        }
        if (index == 2) {
            return getCurrentAvarageValue();
        }
        if (index == 3) {
            return getLastAvarageValue();
        }
        if (index == 4) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            GXCommon.setData(data, DataType.INT8, scaler);
            GXCommon.setData(data, DataType.ENUM, unit);
            return data.array();
        }
        if (index == 5) {
            return getStatus();
        }
        if (index == 6) {
            return getCaptureTime();
        }
        if (index == 7) {
            return getStartTimeCurrent();
        }
        if (index == 8) {
            return getPeriod();
        }
        if (index == 9) {
            return getNumberOfPeriods();
        }
        throw new IllegalArgumentException(
                "GetValue failed. Invalid attribute index.");
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final int index,
            final Object value) {
        if (index == 1) {
            super.setValue(settings, index, value);
        } else if (index == 2) {
            setCurrentAvarageValue(value);
        } else if (index == 3) {
            setLastAvarageValue(value);
        } else if (index == 4) {
            // Set default values.
            if (value == null) {
                scaler = 0;
                unit = 0;
            } else {
                if (Array.getLength(value) != 2) {
                    throw new IllegalArgumentException(
                            "setValue failed. Invalid scaler unit value.");
                }
                scaler = ((Number) Array.get(value, 0)).intValue();
                unit = (((Number) Array.get(value, 1)).intValue() & 0xFF);
            }
        } else if (index == 5) {
            if (value == null) {
                setStatus(null);
            } else {
                setStatus(value);
            }
        } else if (index == 6) {
            if (value == null) {
                setCaptureTime(new GXDateTime());
            } else {
                GXDateTime tmp;
                if (value instanceof byte[]) {
                    tmp = (GXDateTime) GXDLMSClient.changeType((byte[]) value,
                            DataType.DATETIME);
                } else {
                    tmp = (GXDateTime) value;
                }
                setCaptureTime(tmp);
            }
        } else if (index == 7) {
            if (value == null) {
                setStartTimeCurrent(new GXDateTime());
            } else {
                GXDateTime tmp;
                if (value instanceof byte[]) {
                    tmp = (GXDateTime) GXDLMSClient.changeType((byte[]) value,
                            DataType.DATETIME);
                } else {
                    tmp = (GXDateTime) value;
                }
                setStartTimeCurrent(tmp);
            }
        } else if (index == 8) {
            if (value == null) {
                setPeriod(BigInteger.valueOf(0));
            } else {
                setPeriod(BigInteger.valueOf(((Number) value).longValue()));
            }
        } else if (index == 9) {
            if (value == null) {
                setNumberOfPeriods(1);
            } else {
                setNumberOfPeriods(((Number) value).intValue());
            }
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}