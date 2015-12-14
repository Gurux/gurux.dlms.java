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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.MessageType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.ServiceType;

public class GXDLMSPushSetup extends GXDLMSObject implements IGXDLMSBase {
    private java.util.ArrayList<GXDLMSPushObject> pushObjectList;
    private GXSendDestinationAndMethod sendDestinationAndMethod;
    private List<Map.Entry<GXDateTime, GXDateTime>> communicationWindow;
    private int randomisationStartInterval;
    private int numberOfRetries;
    private int repetitionDelay;

    /**
     * Constructor.
     */
    public GXDLMSPushSetup() {
        super(ObjectType.PUSH_SETUP);
        pushObjectList = new java.util.ArrayList<GXDLMSPushObject>();
        sendDestinationAndMethod = new GXSendDestinationAndMethod();
        communicationWindow =
                new ArrayList<Map.Entry<GXDateTime, GXDateTime>>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSPushSetup(final String ln) {
        super(ObjectType.PUSH_SETUP, ln, 0);
        pushObjectList = new java.util.ArrayList<GXDLMSPushObject>();
        sendDestinationAndMethod = new GXSendDestinationAndMethod();
        communicationWindow =
                new ArrayList<Map.Entry<GXDateTime, GXDateTime>>();
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSPushSetup(final String ln, final int sn) {
        super(ObjectType.PUSH_SETUP, ln, sn);
        pushObjectList = new java.util.ArrayList<GXDLMSPushObject>();
        sendDestinationAndMethod = new GXSendDestinationAndMethod();
        communicationWindow =
                new ArrayList<Map.Entry<GXDateTime, GXDateTime>>();
    }

    /**
     * @return Defines the list of attributes or objects to be pushed. Upon a
     *         call of the push (data) method the selected attributes are sent
     *         to the destination defined in getSendDestinationAndMethod.
     */
    public final java.util.ArrayList<GXDLMSPushObject> getPushObjectList() {
        return pushObjectList;
    }

    public final GXSendDestinationAndMethod getSendDestinationAndMethod() {
        return sendDestinationAndMethod;
    }

    /**
     * @return Contains the start and end date/time stamp when the communication
     *         window(s) for the push become active (for the start instant), or
     *         inactive (for the end instant).
     */
    public final List<Map.Entry<GXDateTime, GXDateTime>>
            getCommunicationWindow() {
        return communicationWindow;
    }

    /**
     * @return To avoid simultaneous network connections of a lot of devices at
     *         ex-actly the same point in time, a randomisation interval in
     *         seconds can be defined. This means that the push operation is not
     *         started imme-diately at the beginning of the first communication
     *         window but started randomly delayed.
     */
    public final int getRandomisationStartInterval() {
        return randomisationStartInterval;
    }

    public final void setRandomisationStartInterval(final int value) {
        randomisationStartInterval = value;
    }

    /**
     * @return The maximum number of re-trials in case of unsuccessful push
     *         attempts. After a successful push no further push attempts are
     *         made until the push setup is triggered again. A value of 0 means
     *         no repetitions, i.e. only the initial connection attempt is made.
     */
    public final int getNumberOfRetries() {
        return numberOfRetries;
    }

    /**
     * @param value
     *            The maximum number of re-trials in case of unsuccessful push
     *            attempts. After a successful push no further push attempts are
     *            made until the push setup is triggered again. A value of 0
     *            means no repetitions, i.e. only the initial connection attempt
     *            is made.
     */
    public final void setNumberOfRetries(final byte value) {
        numberOfRetries = value;
    }

    public final int getRepetitionDelay() {
        return repetitionDelay;
    }

    public final void setRepetitionDelay(final int value) {
        repetitionDelay = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), pushObjectList,
                sendDestinationAndMethod, communicationWindow,
                randomisationStartInterval, numberOfRetries, repetitionDelay };
    }

    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // PushObjectList
        if (canRead(2)) {
            attributes.add(2);
        }
        // SendDestinationAndMethod
        if (canRead(3)) {
            attributes.add(3);
        }
        // CommunicationWindow
        if (canRead(4)) {
            attributes.add(4);
        }
        // RandomisationStartInterval
        if (canRead(5)) {
            attributes.add(5);
        }
        // NumberOfRetries
        if (canRead(6)) {
            attributes.add(6);
        }
        // RepetitionDelay
        if (canRead(7)) {
            attributes.add(7);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 7;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ARRAY;
        }
        if (index == 3) {
            return DataType.STRUCTURE;
        }
        if (index == 4) {
            return DataType.ARRAY;
        }
        if (index == 5) {
            return DataType.UINT16;
        }
        if (index == 6) {
            return DataType.UINT8;
        }
        if (index == 7) {
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
            return pushObjectList;
        }
        if (index == 3) {
            return sendDestinationAndMethod;
        }
        if (index == 4) {
            return communicationWindow;
        }
        if (index == 5) {
            return randomisationStartInterval;
        }
        if (index == 6) {
            return numberOfRetries;
        }
        if (index == 7) {
            return repetitionDelay;
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
            pushObjectList.clear();
            if (value instanceof Object[]) {
                for (Object it : (Object[]) value) {
                    Object[] tmp = (Object[]) it;
                    GXDLMSPushObject obj = new GXDLMSPushObject();
                    obj.setType(
                            ObjectType.forValue(((Number) tmp[0]).intValue()));
                    obj.setLogicalName(GXDLMSClient
                            .changeType((byte[]) tmp[1], DataType.BITSTRING)
                            .toString());
                    obj.setAttributeIndex(((Number) tmp[2]).intValue());
                    obj.setDataIndex(((Number) tmp[3]).intValue());
                    pushObjectList.add(obj);
                }
            }
        } else if (index == 3) {
            Object[] tmp = (Object[]) value;
            if (tmp != null) {
                sendDestinationAndMethod.setService(
                        ServiceType.forValue(((Number) tmp[0]).intValue()));
                sendDestinationAndMethod
                        .setDestination(new String((byte[]) tmp[1]));
                sendDestinationAndMethod.setMessage(
                        MessageType.forValue(((Number) tmp[2]).intValue()));
            }
        } else if (index == 4) {
            communicationWindow.clear();
            if (value instanceof Object[]) {
                for (Object it : (Object[]) value) {
                    Object[] tmp = (Object[]) it;
                    GXDateTime start = (GXDateTime) GXDLMSClient
                            .changeType((byte[]) tmp[0], DataType.DATETIME);
                    GXDateTime end = (GXDateTime) GXDLMSClient
                            .changeType((byte[]) tmp[1], DataType.DATETIME);
                    communicationWindow
                            .add(new SimpleEntry<GXDateTime, GXDateTime>(start,
                                    end));
                }
            }
        } else if (index == 5) {
            randomisationStartInterval = ((Number) value).intValue();
        } else if (index == 6) {
            numberOfRetries = ((Number) value).intValue();
        } else if (index == 7) {
            repetitionDelay = ((Number) value).intValue();
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}