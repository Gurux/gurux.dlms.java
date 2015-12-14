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

public class GXDLMSAutoConnect extends GXDLMSObject implements IGXDLMSBase {
    private AutoConnectMode mode;
    private List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> callingWindow;
    private String[] destinations;
    private int repetitionDelay;
    private int repetitions;

    /**
     * Constructor.
     */
    public GXDLMSAutoConnect() {
        this("0.0.2.1.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSAutoConnect(final String ln) {
        this(ln, (short) 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSAutoConnect(final String ln, final int sn) {
        super(ObjectType.AUTO_CONNECT, ln, sn);
        callingWindow = new ArrayList<SimpleEntry<GXDateTime, GXDateTime>>();
    }

    public final AutoConnectMode getMode() {
        return mode;
    }

    public final void setMode(final AutoConnectMode value) {
        mode = value;
    }

    public final int getRepetitions() {
        return repetitions;
    }

    public final void setRepetitions(final int value) {
        repetitions = value;
    }

    public final int getRepetitionDelay() {
        return repetitionDelay;
    }

    public final void setRepetitionDelay(final int value) {
        repetitionDelay = value;
    }

    public final List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>>
            getCallingWindow() {
        return callingWindow;
    }

    public final void setCallingWindow(
            final List<AbstractMap.SimpleEntry<GXDateTime, GXDateTime>> value) {
        callingWindow = value;
    }

    public final String[] getDestinations() {
        return destinations;
    }

    public final void setDestinations(final String[] value) {
        destinations = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getMode(), getRepetitions(),
                getRepetitionDelay(), getCallingWindow(), getDestinations() };
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
        // Mode
        if (canRead(2)) {
            attributes.add(2);
        }
        // Repetitions
        if (canRead(3)) {
            attributes.add(3);
        }
        // RepetitionDelay
        if (canRead(4)) {
            attributes.add(4);
        }
        // CallingWindow
        if (canRead(5)) {
            attributes.add(5);
        }
        // Destinations
        if (canRead(6)) {
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
            return DataType.UINT8;
        }
        if (index == 4) {
            return DataType.UINT16;
        }
        if (index == 5) {
            return DataType.ARRAY;
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
            return (byte) getMode().getValue();
        }
        if (index == 3) {
            return getRepetitions();
        }
        if (index == 4) {
            return getRepetitionDelay();
        }
        if (index == 5) {
            int cnt = getCallingWindow().size();
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8((byte) DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0) {
                for (SimpleEntry<GXDateTime, GXDateTime> it : callingWindow) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    // Count
                    data.setUInt8(2);
                    // Start time
                    GXCommon.setData(data, DataType.OCTET_STRING, it.getKey());
                    // End time
                    GXCommon.setData(data, DataType.OCTET_STRING,
                            it.getValue());
                }
            }
            return data.array();
        }
        if (index == 6) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            if (getDestinations() == null) {
                // Add count
                GXCommon.setObjectCount(0, data);
            } else {
                int cnt = getDestinations().length;
                // Add count
                GXCommon.setObjectCount(cnt, data);
                for (String it : getDestinations()) {
                    GXCommon.setData(data, DataType.OCTET_STRING,
                            GXCommon.getBytes(it)); // destination
                }
            }
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
            setMode(AutoConnectMode.forValue(((Number) value).intValue()));
        } else if (index == 3) {
            setRepetitions(((Number) value).intValue());
        } else if (index == 4) {
            setRepetitionDelay(((Number) value).intValue());
        } else if (index == 5) {
            getCallingWindow().clear();
            if (value != null) {
                for (Object item : (Object[]) value) {
                    GXDateTime start = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) Array.get(item, 0), DataType.DATETIME);
                    GXDateTime end = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) Array.get(item, 1), DataType.DATETIME);
                    getCallingWindow()
                            .add(new SimpleEntry<GXDateTime, GXDateTime>(start,
                                    end));
                }
            }
        } else if (index == 6) {
            setDestinations(null);
            if (value != null) {
                List<String> items = new ArrayList<String>();
                for (Object item : (Object[]) value) {
                    String it = GXDLMSClient
                            .changeType((byte[]) item, DataType.STRING)
                            .toString();
                    items.add(it);
                }
                setDestinations(items.toArray(new String[items.size()]));
            }
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}