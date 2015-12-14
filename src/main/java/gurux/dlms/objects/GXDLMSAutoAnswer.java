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
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.AutoConnectMode;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.AutoAnswerStatus;

public class GXDLMSAutoAnswer extends GXDLMSObject implements IGXDLMSBase {
    private AutoConnectMode mode;
    private List<SimpleEntry<GXDateTime, GXDateTime>> listeningWindow;
    private AutoAnswerStatus status = AutoAnswerStatus.INACTIVE;
    private int numberOfCalls;
    private int numberOfRingsInListeningWindow, numberOfRingsOutListeningWindow;

    /**
     * Constructor.
     */
    public GXDLMSAutoAnswer() {
        super(ObjectType.AUTO_ANSWER, "0.0.2.2.0.255", 0);
        listeningWindow = new ArrayList<SimpleEntry<GXDateTime, GXDateTime>>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSAutoAnswer(final String ln) {
        super(ObjectType.AUTO_ANSWER, ln, 0);
        listeningWindow = new ArrayList<SimpleEntry<GXDateTime, GXDateTime>>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSAutoAnswer(final String ln, final int sn) {
        super(ObjectType.AUTO_ANSWER, ln, sn);
        listeningWindow = new ArrayList<SimpleEntry<GXDateTime, GXDateTime>>();
    }

    public final AutoConnectMode getMode() {
        return mode;
    }

    public final void setMode(final AutoConnectMode value) {
        mode = value;
    }

    public final List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>>
            getListeningWindow() {
        return listeningWindow;
    }

    public final void setListeningWindow(
            final List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> value) {
        listeningWindow = value;
    }

    public final AutoAnswerStatus getStatus() {
        return status;
    }

    public final void setStatus(final AutoAnswerStatus value) {
        status = value;
    }

    public final int getNumberOfCalls() {
        return numberOfCalls;
    }

    public final void setNumberOfCalls(final int value) {
        numberOfCalls = value;
    }

    /*
     * Number of rings within the window defined by ListeningWindow.
     */
    public final int getNumberOfRingsInListeningWindow() {
        return numberOfRingsInListeningWindow;
    }

    public final void setNumberOfRingsInListeningWindow(final int value) {
        numberOfRingsInListeningWindow = value;
    }

    /*
     * Number of rings outside the window defined by ListeningWindow.
     */
    public final int getNumberOfRingsOutListeningWindow() {
        return numberOfRingsOutListeningWindow;
    }

    public final void setNumberOfRingsOutListeningWindow(final int value) {
        numberOfRingsOutListeningWindow = value;
    }

    @Override
    public final Object[] getValues() {
        String str = String.format("%d/%d", numberOfRingsInListeningWindow,
                numberOfRingsOutListeningWindow);
        return new Object[] { getLogicalName(), getMode(), getListeningWindow(),
                getStatus(), getNumberOfCalls(), str };
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
        // Mode is static and read only once.
        if (!isRead(2)) {
            attributes.add(2);
        }
        // ListeningWindow is static and read only once.
        if (!isRead(3)) {
            attributes.add(3);
        }
        // Status is not static.
        if (canRead(4)) {
            attributes.add(4);
        }

        // NumberOfCalls is static and read only once.
        if (!isRead(5)) {
            attributes.add(5);
        }
        // NumberOfRingsInListeningWindow is static and read only once.
        if (!isRead(6)) {
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
        return 0;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ENUM;
        }
        if (index == 3) {
            return DataType.ARRAY;
        }
        if (index == 4) {
            return DataType.ENUM;
        }
        if (index == 5) {
            return DataType.UINT8;
        }
        if (index == 6) {
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
            return getMode().ordinal();
        }
        if (index == 3) {
            int cnt = getListeningWindow().size();
            GXByteBuffer buff = new GXByteBuffer();
            buff.setUInt8(DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(cnt, buff);
            if (cnt != 0) {
                for (SimpleEntry<GXDateTime, GXDateTime> it : listeningWindow) {
                    buff.setUInt8(DataType.STRUCTURE.getValue());
                    // Count
                    buff.setUInt8(2);
                    // Start time
                    GXCommon.setData(buff, DataType.OCTET_STRING, it.getKey());
                    // End time
                    GXCommon.setData(buff, DataType.OCTET_STRING,
                            it.getValue());
                }
            }
            return buff.array();
        }
        if (index == 4) {
            return getStatus().getValue();
        }
        if (index == 5) {
            return getNumberOfCalls();
        }
        if (index == 6) {
            GXByteBuffer buff = new GXByteBuffer();
            buff.setUInt8(DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(2, buff);
            GXCommon.setData(buff, DataType.UINT8,
                    numberOfRingsInListeningWindow);
            GXCommon.setData(buff, DataType.UINT8,
                    numberOfRingsOutListeningWindow);
            return buff.array();
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
            setMode(AutoConnectMode
                    .forValue(((Number) value).byteValue() & 0xFF));
        } else if (index == 3) {
            getListeningWindow().clear();
            if (value != null) {
                for (Object item : (Object[]) value) {
                    GXDateTime start = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) Array.get(item, 0), DataType.DATETIME);
                    GXDateTime end = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) Array.get(item, 1), DataType.DATETIME);
                    getListeningWindow()
                            .add(new SimpleEntry<GXDateTime, GXDateTime>(start,
                                    end));
                }
            }
        } else if (index == 4) {
            setStatus(AutoAnswerStatus.forValue(((Number) value).intValue()));
        } else if (index == 5) {
            setNumberOfCalls(((Number) value).intValue());
        } else if (index == 6) {
            numberOfRingsInListeningWindow = 0;
            numberOfRingsOutListeningWindow = 0;
            if (value != null) {
                numberOfRingsInListeningWindow =
                        ((Number) Array.get(value, 0)).intValue();
                numberOfRingsOutListeningWindow =
                        ((Number) Array.get(value, 1)).intValue();
            }
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}