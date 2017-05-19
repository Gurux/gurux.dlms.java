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

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Unit;
import gurux.dlms.internal.GXCommon;

public class GXDLMSDemandRegister extends GXDLMSObject implements IGXDLMSBase {
    private int scaler;
    private int unit;
    private Object currentAverageValue;
    private Object lastAverageValue;
    private Object status;
    private GXDateTime captureTime = new GXDateTime();
    private GXDateTime startTimeCurrent = new GXDateTime();
    private long numberOfPeriods;
    private long period;

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
    public final Object getCurrentAverageValue() {
        return currentAverageValue;
    }

    /**
     * @param value
     *            Current average value of COSEM Data object.
     */
    public final void setCurrentAverageValue(final Object value) {
        currentAverageValue = value;
    }

    /**
     * @return Last average value of COSEM Data object.
     */
    public final Object getLastAverageValue() {
        return lastAverageValue;
    }

    /**
     * @param value
     *            Last average value of COSEM Data object.
     */
    public final void setLastAverageValue(final Object value) {
        lastAverageValue = value;
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

    public final long getPeriod() {
        return period;
    }

    public final void setPeriod(final long value) {
        period = value;
    }

    public final long getNumberOfPeriods() {
        return numberOfPeriods;
    }

    public final void setNumberOfPeriods(final long value) {
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
        String str = "Scaler: " + String.valueOf(getScaler()) + " Unit: "
                + getUnit().toString();
        return new Object[] { getLogicalName(), getCurrentAverageValue(),
                getLastAverageValue(), str, getStatus(), getCaptureTime(),
                getStartTimeCurrent(), new Long(getPeriod()),
                new Long(getNumberOfPeriods()) };
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
            attributes.add(new Integer(1));
        }
        // ScalerUnit
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        // CurrentAvarageValue
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        // LastAvarageValue
        if (canRead(3)) {
            attributes.add(new Integer(3));
        }
        // Status
        if (canRead(5)) {
            attributes.add(new Integer(5));
        }
        // CaptureTime
        if (canRead(6)) {
            attributes.add(new Integer(6));
        }
        // StartTimeCurrent
        if (canRead(7)) {
            attributes.add(new Integer(7));
        }
        // Period
        if (canRead(8)) {
            attributes.add(new Integer(8));
        }
        // NumberOfPeriods
        if (canRead(9)) {
            attributes.add(new Integer(9));
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
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            return getCurrentAverageValue();
        }
        if (e.getIndex() == 3) {
            return getLastAverageValue();
        }
        if (e.getIndex() == 4) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            GXCommon.setData(data, DataType.INT8, new Integer(scaler));
            GXCommon.setData(data, DataType.ENUM, new Integer(unit));
            return data.array();
        }
        if (e.getIndex() == 5) {
            return getStatus();
        }
        if (e.getIndex() == 6) {
            return getCaptureTime();
        }
        if (e.getIndex() == 7) {
            return getStartTimeCurrent();
        }
        if (e.getIndex() == 8) {
            return new Long(getPeriod());
        }
        if (e.getIndex() == 9) {
            return new Long(getNumberOfPeriods());
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
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            setCurrentAverageValue(e.getValue());
        } else if (e.getIndex() == 3) {
            setLastAverageValue(e.getValue());
        } else if (e.getIndex() == 4) {
            // Set default values.
            if (e.getValue() == null) {
                scaler = 0;
                unit = 0;
            } else {
                Object[] arr = (Object[]) e.getValue();
                if (arr.length != 2) {
                    throw new IllegalArgumentException(
                            "setValue failed. Invalid scaler unit value.");
                }
                scaler = ((Number) arr[0]).intValue();
                unit = (((Number) arr[1]).intValue() & 0xFF);
            }
        } else if (e.getIndex() == 5) {
            if (e.getValue() == null) {
                setStatus(null);
            } else {
                setStatus(e.getValue());
            }
        } else if (e.getIndex() == 6) {
            if (e.getValue() == null) {
                setCaptureTime(new GXDateTime());
            } else {
                GXDateTime tmp;
                if (e.getValue() instanceof byte[]) {
                    tmp = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) e.getValue(), DataType.DATETIME);
                } else {
                    tmp = (GXDateTime) e.getValue();
                }
                setCaptureTime(tmp);
            }
        } else if (e.getIndex() == 7) {
            if (e.getValue() == null) {
                setStartTimeCurrent(new GXDateTime());
            } else {
                GXDateTime tmp;
                if (e.getValue() instanceof byte[]) {
                    tmp = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) e.getValue(), DataType.DATETIME);
                } else {
                    tmp = (GXDateTime) e.getValue();
                }
                setStartTimeCurrent(tmp);
            }
        } else if (e.getIndex() == 8) {
            if (e.getValue() == null) {
                setPeriod(0);
            } else {
                setPeriod(((Number) e.getValue()).longValue());
            }
        } else if (e.getIndex() == 9) {
            if (e.getValue() == null) {
                setNumberOfPeriods(0);
            } else {
                setNumberOfPeriods(((Number) e.getValue()).longValue());
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        currentAverageValue =
                reader.readElementContentAsObject("CurrentAverageValue", null);
        lastAverageValue =
                reader.readElementContentAsObject("LastAverageValue", null);
        setScaler(reader.readElementContentAsDouble("Scaler", 1));
        unit = reader.readElementContentAsInt("Unit");
        status = reader.readElementContentAsObject("Status", null);
        String str = reader.readElementContentAsString("CaptureTime");
        if (str == null) {
            captureTime = null;
        } else {
            captureTime = new GXDateTime(str);
        }
        str = reader.readElementContentAsString("StartTimeCurrent");
        if (str == null) {
            startTimeCurrent = null;
        } else {
            startTimeCurrent = new GXDateTime(str);
        }
        period = reader.readElementContentAsInt("Period");
        numberOfPeriods = reader.readElementContentAsInt("NumberOfPeriods");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementObject("CurrentAverageValue", currentAverageValue);
        writer.writeElementObject("LastAverageValue", lastAverageValue);
        writer.writeElementString("Scaler", getScaler(), 1);
        writer.writeElementString("Unit", unit);
        writer.writeElementObject("Status", status);
        writer.writeElementString("CaptureTime", captureTime);
        writer.writeElementString("StartTimeCurrent", startTimeCurrent);
        writer.writeElementString("Period", period);
        writer.writeElementString("NumberOfPeriods", numberOfPeriods);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}