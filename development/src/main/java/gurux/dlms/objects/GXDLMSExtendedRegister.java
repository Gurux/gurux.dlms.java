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

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Unit;

public class GXDLMSExtendedRegister extends GXDLMSRegister {
    private GXDateTime captureTime = new GXDateTime();
    private Object status;

    /**
     * Constructor.
     */
    public GXDLMSExtendedRegister() {
        super(ObjectType.EXTENDED_REGISTER, null, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSExtendedRegister(final String ln) {
        super(ObjectType.EXTENDED_REGISTER, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSExtendedRegister(final String ln, final int sn) {
        super(ObjectType.EXTENDED_REGISTER, ln, sn);
    }

    /**
     * @return Status of COSEM Extended Register object.
     */
    public final Object getStatus() {
        return status;
    }

    /**
     * @param value
     *            Status of COSEM Extended Register object.
     */
    public final void setStatus(final Object value) {
        status = value;
    }

    /**
     * @return Capture time of COSEM Extended Register object.
     */
    public final GXDateTime getCaptureTime() {
        return captureTime;
    }

    /**
     * @param value
     *            Capture time of COSEM Extended Register object.
     */
    public final void setCaptureTime(final GXDateTime value) {
        captureTime = value;
    }

    @Override
    public final DataType getUIDataType(final int index) {
        if (index == 5) {
            return DataType.DATETIME;
        }
        return super.getUIDataType(index);
    }

    @Override
    public final Object[] getValues() {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        String str = "Scaler: " + formatter.format(getScaler());
        str += " Unit: ";
        str += getUnit().toString();
        return new Object[] { getLogicalName(), getValue(), str, getStatus(),
                getCaptureTime() };
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
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // Value
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        // Status
        if (canRead(4)) {
            attributes.add(new Integer(4));
        }
        // CaptureTime
        if (canRead(5)) {
            attributes.add(new Integer(5));
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

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return super.getDataType(index);
        }
        if (index == 3) {
            return DataType.ARRAY;
        }
        if (index == 4) {
            return super.getDataType(index);
        }
        if (index == 5) {
            return DataType.OCTET_STRING;
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
        if (e.getIndex() == 4) {
            return getStatus();
        }
        if (e.getIndex() == 5) {
            return getCaptureTime();
        }
        return super.getValue(settings, e);
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 4) {
            setStatus(e.getValue());
        } else if (e.getIndex() == 5) {
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
        } else {
            super.setValue(settings, e);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        setUnit(Unit.forValue(reader.readElementContentAsInt("Unit", 0)));
        setScaler(reader.readElementContentAsDouble("Scaler", 1));
        setValue(reader.readElementContentAsObject("Value", null));
        status = reader.readElementContentAsObject("Status", null);
        captureTime = (GXDateTime) reader
                .readElementContentAsObject("CaptureTime", null);
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Unit", getUnit().getValue());
        writer.writeElementString("Scaler", getScaler(), 1);
        writer.writeElementObject("Value", getValue());
        writer.writeElementObject("Status", status);
        writer.writeElementObject("CaptureTime", captureTime);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}