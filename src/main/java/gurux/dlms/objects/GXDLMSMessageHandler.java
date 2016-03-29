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
import java.util.Map.Entry;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSMessageHandler extends GXDLMSObject implements IGXDLMSBase {
    private List<Entry<GXDateTime, GXDateTime>> listeningWindow;
    private String[] allowedSenders;
    // CHECKSTYLE:OFF
    private final List<Entry<String, Entry<Integer, GXDLMSScriptAction>>> sendersAndActions;

    // CHECKSTYLE:ON
    /**
     * Constructor.
     */
    public GXDLMSMessageHandler() {
        super(ObjectType.MESSAGE_HANDLER);
        listeningWindow = new ArrayList<Entry<GXDateTime, GXDateTime>>();
        // CHECKSTYLE:OFF
        sendersAndActions =
                new ArrayList<Entry<String, Entry<Integer, GXDLMSScriptAction>>>();
        // CHECKSTYLE:ON
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSMessageHandler(final String ln) {
        super(ObjectType.MESSAGE_HANDLER, ln, 0);
        listeningWindow = new ArrayList<Entry<GXDateTime, GXDateTime>>();
        // CHECKSTYLE:OFF
        sendersAndActions =
                new ArrayList<Entry<String, Entry<Integer, GXDLMSScriptAction>>>();
        // CHECKSTYLE:ON
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSMessageHandler(final String ln, final int sn) {
        super(ObjectType.MESSAGE_HANDLER, ln, sn);
        listeningWindow = new ArrayList<Entry<GXDateTime, GXDateTime>>();
        // CHECKSTYLE:OFF
        sendersAndActions =
                new ArrayList<Entry<String, Entry<Integer, GXDLMSScriptAction>>>();
        // CHECKSTYLE:ON
    }

    /**
     * @return Listening Window.
     */
    public final List<Entry<GXDateTime, GXDateTime>> getListeningWindow() {
        return listeningWindow;
    }

    /**
     * @return List of allowed Senders.
     */
    public final String[] getAllowedSenders() {
        return allowedSenders;
    }

    /**
     * @param value
     *            List of allowed Senders.
     */
    public final void setAllowedSenders(final String[] value) {
        allowedSenders = value;
    }

    /**
     * @return Contains the logical name of a "Script table" object and the
     *         script selector of the script to be executed if an empty message
     *         is received from a matching sender.
     */
    public final List<Entry<String, Entry<Integer, GXDLMSScriptAction>>>
            getSendersAndActions() {
        return sendersAndActions;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), listeningWindow, allowedSenders,
                sendersAndActions };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead() {
        List<Integer> attributes = new ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // ListeningWindow
        if (canRead(2)) {
            attributes.add(2);
        }
        // AllowedSenders
        if (canRead(3)) {
            attributes.add(3);
        }
        // SendersAndActions
        if (canRead(4)) {
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
        // ListeningWindow
        if (index == 2) {
            return DataType.ARRAY;
        }
        // AllowedSenders
        if (index == 3) {
            return DataType.ARRAY;
        }
        // SendersAndActions
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
            GXByteBuffer buff = new GXByteBuffer();
            buff.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(listeningWindow.size(), buff);
            for (Entry<GXDateTime, GXDateTime> it : listeningWindow) {
                buff.setUInt8(DataType.STRUCTURE.getValue());
                buff.setUInt8(2);
                GXCommon.setData(buff, DataType.OCTET_STRING, it.getKey());
                GXCommon.setData(buff, DataType.OCTET_STRING, it.getValue());
            }
            return buff.array();
        }
        if (index == 3) {
            GXByteBuffer buff = new GXByteBuffer();
            buff.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(allowedSenders.length, buff);
            for (String it : allowedSenders) {
                GXCommon.setData(buff, DataType.OCTET_STRING, it.getBytes());
            }
            return buff.array();
        }
        if (index == 4) {
            GXByteBuffer buff = new GXByteBuffer();
            buff.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(sendersAndActions.size(), buff);
            for (Entry<String, Entry<Integer, GXDLMSScriptAction>> it : sendersAndActions) {
                buff.setUInt8(DataType.STRUCTURE.getValue());
                buff.setUInt8(2);
                GXCommon.setData(buff, DataType.OCTET_STRING,
                        it.getKey().getBytes());
                // TODO: GXCommon.SetData(buff, DataType.OCTET_STRING,
            }
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
            listeningWindow.clear();
            if (value instanceof Object[]) {
                for (Object it : (Object[]) value) {
                    Object[] tmp = (Object[]) it;
                    GXDateTime start = (GXDateTime) GXDLMSClient
                            .changeType((byte[]) tmp[0], DataType.DATETIME);
                    GXDateTime end = (GXDateTime) GXDLMSClient
                            .changeType((byte[]) tmp[1], DataType.DATETIME);
                    listeningWindow.add(new SimpleEntry<GXDateTime, GXDateTime>(
                            start, end));
                }
            }

        } else if (index == 3) {
            if (value instanceof Object[]) {
                List<String> tmp = new ArrayList<String>();
                for (Object it : (Object[]) value) {
                    tmp.add(new String((byte[]) it));
                }
                allowedSenders = tmp.toArray(new String[tmp.size()]);
            } else {
                allowedSenders = new String[0];
            }
        } else if (index == 4) {
            sendersAndActions.clear();
            // TODO:
            /*
             * if (value instanceof Object[]) { for (Object it : (Object[])
             * value) { Object[] tmp = (Object[]) it; String id = new
             * String((byte[]) tmp[0]); Object[] tmp2 = (Object[]) tmp[1];
             * KeyValuePair<int, GXDLMSScriptAction> executed_script = new
             * KeyValuePair<int, GXDLMSScriptAction>(Convert.ToInt32(tmp2[1],
             * tmp2[2])); SendersAndActions.Add(new KeyValuePair<string,
             * KeyValuePair<int, GXDLMSScriptAction>>(id, tmp[1] as
             * GXDateTime)); } }
             */
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}