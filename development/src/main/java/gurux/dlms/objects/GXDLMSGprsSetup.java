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

import java.util.List;

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
 * Online help: <br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSGprsSetup
 */
public class GXDLMSGprsSetup extends GXDLMSObject implements IGXDLMSBase {
    private String apn;
    private long pinCode;
    private GXDLMSQualityOfService defaultQualityOfService;
    private GXDLMSQualityOfService requestedQualityOfService;

    /**
     * Constructor.
     */
    public GXDLMSGprsSetup() {
        super(ObjectType.GPRS_SETUP);
        defaultQualityOfService = new GXDLMSQualityOfService();
        requestedQualityOfService = new GXDLMSQualityOfService();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSGprsSetup(final String ln) {
        super(ObjectType.GPRS_SETUP, ln, 0);
        defaultQualityOfService = new GXDLMSQualityOfService();
        requestedQualityOfService = new GXDLMSQualityOfService();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSGprsSetup(final String ln, final int sn) {
        super(ObjectType.GPRS_SETUP, ln, sn);
        defaultQualityOfService = new GXDLMSQualityOfService();
        requestedQualityOfService = new GXDLMSQualityOfService();
    }

    public final String getAPN() {
        return apn;
    }

    public final void setAPN(final String value) {
        apn = value;
    }

    public final long getPINCode() {
        return pinCode;
    }

    public final void setPINCode(final long value) {
        pinCode = value;
    }

    public final GXDLMSQualityOfService getDefaultQualityOfService() {
        return defaultQualityOfService;
    }

    public final GXDLMSQualityOfService getRequestedQualityOfService() {
        return requestedQualityOfService;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getAPN(), getPINCode(),
                getDefaultQualityOfService(), getRequestedQualityOfService() };
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
        // APN
        if (all || !isRead(2)) {
            attributes.add(2);
        }
        // PINCode
        if (all || !isRead(3)) {
            attributes.add(3);
        }
        // DefaultQualityOfService + RequestedQualityOfService
        if (all || !isRead(4)) {
            attributes.add(4);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 4;
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
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.OCTET_STRING;
        }
        if (index == 3) {
            return DataType.UINT16;
        }
        if (index == 4) {
            return DataType.ARRAY;
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
            if (apn == null) {
                return null;
            }
            return apn.getBytes();
        }
        if (e.getIndex() == 3) {
            return getPINCode();
        }
        if (e.getIndex() == 4) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(5);
            GXCommon.setData(settings, data, DataType.UINT8,
                    defaultQualityOfService.getPrecedence());
            GXCommon.setData(settings, data, DataType.UINT8,
                    defaultQualityOfService.getDelay());
            GXCommon.setData(settings, data, DataType.UINT8,
                    defaultQualityOfService.getReliability());
            GXCommon.setData(settings, data, DataType.UINT8,
                    defaultQualityOfService.getPeakThroughput());
            GXCommon.setData(settings, data, DataType.UINT8,
                    defaultQualityOfService.getMeanThroughput());
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(5);
            GXCommon.setData(settings, data, DataType.UINT8,
                    requestedQualityOfService.getPrecedence());
            GXCommon.setData(settings, data, DataType.UINT8,
                    requestedQualityOfService.getDelay());
            GXCommon.setData(settings, data, DataType.UINT8,
                    requestedQualityOfService.getReliability());
            GXCommon.setData(settings, data, DataType.UINT8,
                    requestedQualityOfService.getPeakThroughput());
            GXCommon.setData(settings, data, DataType.UINT8,
                    requestedQualityOfService.getMeanThroughput());
            return data.array();
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
            if (e.getValue() instanceof String) {
                setAPN(e.getValue().toString());
            } else {
                setAPN(GXDLMSClient.changeType((byte[]) e.getValue(),
                        DataType.STRING, false).toString());
            }
        } else if (e.getIndex() == 3) {
            setPINCode(((Number) e.getValue()).intValue());
        } else if (e.getIndex() == 4) {
            List<?> arr = (List<?>) ((List<?>) e.getValue()).get(0);
            defaultQualityOfService
                    .setPrecedence(((Number) arr.get(0)).intValue());
            defaultQualityOfService.setDelay(((Number) arr.get(1)).intValue());
            defaultQualityOfService
                    .setReliability(((Number) arr.get(2)).intValue());
            defaultQualityOfService
                    .setPeakThroughput(((Number) arr.get(3)).intValue());
            defaultQualityOfService
                    .setMeanThroughput(((Number) arr.get(4)).intValue());
            arr = (List<?>) ((List<?>) e.getValue()).get(1);
            requestedQualityOfService
                    .setPrecedence(((Number) arr.get(0)).intValue());
            requestedQualityOfService
                    .setDelay(((Number) arr.get(1)).intValue());
            requestedQualityOfService
                    .setReliability(((Number) arr.get(2)).intValue());
            requestedQualityOfService
                    .setPeakThroughput(((Number) arr.get(3)).intValue());
            requestedQualityOfService
                    .setMeanThroughput(((Number) arr.get(4)).intValue());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        apn = reader.readElementContentAsString("APN");
        pinCode = reader.readElementContentAsInt("PINCode");
        if (reader.isStartElement("DefaultQualityOfService", true)) {
            defaultQualityOfService.setPrecedence(
                    reader.readElementContentAsInt("Precedence"));
            defaultQualityOfService
                    .setDelay(reader.readElementContentAsInt("Delay"));
            defaultQualityOfService.setReliability(
                    reader.readElementContentAsInt("Reliability"));
            defaultQualityOfService.setPeakThroughput(
                    reader.readElementContentAsInt("PeakThroughput"));
            defaultQualityOfService.setMeanThroughput(
                    reader.readElementContentAsInt("MeanThroughput"));
            reader.readEndElement("DefaultQualityOfService");
        }
        if (reader.isStartElement("RequestedQualityOfService", true)) {
            requestedQualityOfService.setPrecedence(
                    reader.readElementContentAsInt("Precedence"));
            requestedQualityOfService
                    .setDelay(reader.readElementContentAsInt("Delay"));
            requestedQualityOfService.setReliability(
                    reader.readElementContentAsInt("Reliability"));
            requestedQualityOfService.setPeakThroughput(
                    reader.readElementContentAsInt("PeakThroughput"));
            requestedQualityOfService.setMeanThroughput(
                    reader.readElementContentAsInt("MeanThroughput"));
            reader.readEndElement("DefaultQualityOfService");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("APN", apn);
        writer.writeElementString("PINCode", pinCode);
        if (defaultQualityOfService != null) {
            writer.writeStartElement("DefaultQualityOfService");
            writer.writeElementString("Precedence",
                    defaultQualityOfService.getPrecedence());
            writer.writeElementString("Delay",
                    defaultQualityOfService.getDelay());
            writer.writeElementString("Reliability",
                    defaultQualityOfService.getReliability());
            writer.writeElementString("PeakThroughput",
                    defaultQualityOfService.getPeakThroughput());
            writer.writeElementString("MeanThroughput",
                    defaultQualityOfService.getMeanThroughput());
            writer.writeEndElement();
        }
        if (requestedQualityOfService != null) {
            writer.writeStartElement("RequestedQualityOfService");
            writer.writeElementString("Precedence",
                    requestedQualityOfService.getPrecedence());
            writer.writeElementString("Delay",
                    requestedQualityOfService.getDelay());
            writer.writeElementString("Reliability",
                    requestedQualityOfService.getReliability());
            writer.writeElementString("PeakThroughput",
                    requestedQualityOfService.getPeakThroughput());
            writer.writeElementString("MeanThroughput",
                    requestedQualityOfService.getMeanThroughput());
            writer.writeEndElement();
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
        // Not needed for this object.
    }
}