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

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

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
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // APN
        if (!isRead(2)) {
            attributes.add(2);
        }
        // PINCode
        if (!isRead(3)) {
            attributes.add(3);
        }
        // DefaultQualityOfService + RequestedQualityOfService
        if (!isRead(4)) {
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
    public final Object getValue(final GXDLMSSettings settings, final int index,
            final int selector, final Object parameters) {
        if (index == 1) {
            return getLogicalName();
        }
        if (index == 2) {
            return getAPN();
        }
        if (index == 3) {
            return getPINCode();
        }
        if (index == 4) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(2);
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(5);
            GXCommon.setData(data, DataType.UINT8,
                    defaultQualityOfService.getPrecedence());
            GXCommon.setData(data, DataType.UINT8,
                    defaultQualityOfService.getDelay());
            GXCommon.setData(data, DataType.UINT8,
                    defaultQualityOfService.getReliability());
            GXCommon.setData(data, DataType.UINT8,
                    defaultQualityOfService.getPeakThroughput());
            GXCommon.setData(data, DataType.UINT8,
                    defaultQualityOfService.getMeanThroughput());
            data.setUInt8(5);
            GXCommon.setData(data, DataType.UINT8,
                    requestedQualityOfService.getPrecedence());
            GXCommon.setData(data, DataType.UINT8,
                    requestedQualityOfService.getDelay());
            GXCommon.setData(data, DataType.UINT8,
                    requestedQualityOfService.getReliability());
            GXCommon.setData(data, DataType.UINT8,
                    requestedQualityOfService.getPeakThroughput());
            GXCommon.setData(data, DataType.UINT8,
                    requestedQualityOfService.getMeanThroughput());
            return data.array();
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
            if (value instanceof String) {
                setAPN(value.toString());
            } else {
                setAPN(GXDLMSClient.changeType((byte[]) value, DataType.STRING)
                        .toString());
            }
        } else if (index == 3) {
            setPINCode(((Number) value).intValue());
        } else if (index == 4) {
            Object[] arr = (Object[]) Array.get(value, 0);
            defaultQualityOfService
                    .setPrecedence(((Number) Array.get(arr, 0)).intValue());
            defaultQualityOfService
                    .setDelay(((Number) Array.get(arr, 1)).intValue());
            defaultQualityOfService
                    .setReliability(((Number) Array.get(arr, 2)).intValue());
            defaultQualityOfService
                    .setPeakThroughput(((Number) Array.get(arr, 3)).intValue());
            defaultQualityOfService
                    .setMeanThroughput(((Number) Array.get(arr, 4)).intValue());
            arr = (Object[]) Array.get(value, 1);
            requestedQualityOfService
                    .setPrecedence(((Number) Array.get(arr, 0)).intValue());
            requestedQualityOfService
                    .setDelay(((Number) Array.get(arr, 1)).intValue());
            requestedQualityOfService
                    .setReliability(((Number) Array.get(arr, 2)).intValue());
            requestedQualityOfService
                    .setPeakThroughput(((Number) Array.get(arr, 3)).intValue());
            requestedQualityOfService
                    .setMeanThroughput(((Number) Array.get(arr, 4)).intValue());
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}