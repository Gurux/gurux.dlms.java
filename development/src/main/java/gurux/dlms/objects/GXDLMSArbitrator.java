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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSArbitrator
 */
public class GXDLMSArbitrator extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Requested actions.
     */
    private GXDLMSActionItem[] actions;

    /**
     * Permissions for each actor to request actions.
     */
    private String[] permissionsTable;
    /**
     * Weight allocated for each actor and to each possible action of that
     * actor.
     */
    private int[][] weightingsTable;
    /**
     * The most recent requests of each actor.
     */
    private String[] mostRecentRequestsTable;
    /**
     * The number identifies a bit in the Actions.
     */
    private byte lastOutcome;

    /**
     * Constructor.
     */
    public GXDLMSArbitrator() {
        this("0.0.96.3.20.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSArbitrator(final String ln) {
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
    public GXDLMSArbitrator(final String ln, final int sn) {
        super(ObjectType.ARBITRATOR, ln, sn);
    }

    /**
     * @return Requested actions.
     */
    public final GXDLMSActionItem[] getActions() {
        return actions;
    }

    /**
     * @param value
     *            Requested actions.
     */
    public final void setActions(final GXDLMSActionItem[] value) {
        actions = value;
    }

    /**
     * @return Permissions for each actor to request actions.
     */
    public final String[] getPermissionsTable() {
        return permissionsTable;
    }

    /**
     * @param value
     *            Permissions for each actor to request actions.
     */
    public final void setPermissionsTable(final String[] value) {
        permissionsTable = value;
    }

    /**
     * @return Weight allocated for each actor and to each possible action of
     *         that actor.
     */
    public final int[][] getWeightingsTable() {
        return weightingsTable;
    }

    /**
     * @param value
     *            Weight allocated for each actor and to each possible action of
     *            that actor.
     */
    public final void setWeightingsTable(final int[][] value) {
        weightingsTable = value;
    }

    /**
     * @return The most recent requests of each actor.
     */
    public final String[] getMostRecentRequestsTable() {
        return mostRecentRequestsTable;
    }

    /**
     * @param value
     *            The most recent requests of each actor.
     */
    public final void setMostRecentRequestsTable(final String[] value) {
        mostRecentRequestsTable = value;
    }

    /**
     * @return The number identifies a bit in the Actions.
     */
    public final byte getLastOutcome() {
        return lastOutcome;
    }

    /**
     * @param value
     *            The number identifies a bit in the Actions.
     */
    public final void setLastOutcome(final byte value) {
        lastOutcome = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), actions, permissionsTable,
                weightingsTable, mostRecentRequestsTable, lastOutcome };
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
        // Actions
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // Permissions table
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // Weightings table
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // Most recent requests table
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // Last outcome
        if (all || canRead(6)) {
            attributes.add(6);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /// <summary>
    /// Request Action.
    /// </summary>
    /// <param name="client">DLMS client.</param>
    /// <returns>Action bytes.</returns>

    /**
     * Request Action.
     * 
     * @param client
     *            DLMS client.
     * @param actor
     *            Actor.
     * @param actions
     *            Actions in bit-string.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public byte[][] requestAction(GXDLMSClient client, byte actor,
            String actions)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE);
        bb.setUInt8(2);
        bb.setUInt8(DataType.UINT8);
        bb.setUInt8(actor);
        GXCommon.setData(null, bb, DataType.BITSTRING, actions);
        return client.method(this, 1, bb.array(), DataType.STRUCTURE);
    }

    /**
     * Reset value.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public byte[][] reset(GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return client.method(this, 2, 0, DataType.INT8);
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
        return 2;
    }

    @Override
    public final DataType getDataType(final int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
        case 3:
        case 4:
        case 5:
            return DataType.ARRAY;
        case 6:
            return DataType.UINT8;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        Object ret;
        GXByteBuffer data;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2: {
            data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            if (actions == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(actions.length, data);
                for (GXDLMSActionItem it : actions) {
                    data.setUInt8(DataType.STRUCTURE);
                    // Count
                    data.setUInt8(2);
                    GXCommon.setData(settings, data, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(it.getLogicalName()));
                    GXCommon.setData(settings, data, DataType.UINT16,
                            it.getScriptSelector());
                }
            }
            ret = data.array();
        }
            break;
        case 3: {
            data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            if (permissionsTable == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(permissionsTable.length, data);
                for (String it : permissionsTable) {
                    GXCommon.setData(settings, data, DataType.BITSTRING, it);
                }
            }
            ret = data.array();
        }
            break;
        case 4: {
            data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            if (weightingsTable == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(weightingsTable.length, data);
                for (int[] it : weightingsTable) {
                    data.setUInt8(DataType.ARRAY);
                    GXCommon.setObjectCount(it.length, data);
                    for (int it2 : it) {
                        GXCommon.setData(settings, data, DataType.UINT16, it2);
                    }
                }
            }
            ret = data.array();
        }
            break;
        case 5: {
            data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY);
            if (mostRecentRequestsTable == null) {
                GXCommon.setObjectCount(0, data);
            } else {
                GXCommon.setObjectCount(mostRecentRequestsTable.length, data);
                for (String it : mostRecentRequestsTable) {
                    GXCommon.setData(settings, data, DataType.BITSTRING, it);
                }
            }
            ret = data.array();
        }
            break;
        case 6:
            ret = lastOutcome;
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            ret = null;
            break;
        }
        return ret;
    }

    /**
     * Create a new action.
     * 
     * @param settings
     *            DLMS settings.
     * @param it
     *            object array.
     * @return Action item.
     */
    private static GXDLMSActionItem createAction(final GXDLMSSettings settings,
            final List<?> it) {
        GXDLMSActionItem item = new GXDLMSActionItem();
        item.setLogicalName(GXCommon.toLogicalName(it.get(0)));
        item.setScriptSelector(((Number) it.get(1)).intValue());
        return item;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2: {
            List<GXDLMSActionItem> list = new ArrayList<GXDLMSActionItem>();
            if (e.getValue() != null) {
                List<?> arr = (List<?>) e.getValue();
                for (Object it : arr) {
                    list.add(createAction(settings, (List<?>) it));
                }
            }
            actions = list.toArray(new GXDLMSActionItem[0]);
        }
            break;
        case 3: {
            List<String> list = new ArrayList<String>();
            if (e.getValue() != null) {
                List<?> arr = (List<?>) e.getValue();
                for (Object it : arr) {
                    list.add(it.toString());
                }
            }
            permissionsTable = list.toArray(new String[0]);
        }
            break;
        case 4: {
            List<int[]> list = new ArrayList<int[]>();
            if (e.getValue() != null) {
                List<?> arr = (List<?>) e.getValue();
                for (Object it : arr) {
                    List<Integer> list2 = new ArrayList<Integer>();
                    for (Object it2 : (List<?>) it) {
                        list2.add(((Number) it2).intValue());
                    }
                    list.add(GXCommon.toIntArray(list2));
                }
            }
            weightingsTable = list.toArray(new int[0][0]);
        }
            break;
        case 5: {
            List<String> list = new ArrayList<String>();
            if (e.getValue() != null) {
                List<?> arr = (List<?>) e.getValue();
                for (Object it : arr) {
                    list.add((String) it);
                }
            }
            mostRecentRequestsTable = list.toArray(new String[0]);
        }
            break;
        case 6:
            lastOutcome = ((Number) e.getValue()).byteValue();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }

    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        List<GXDLMSActionItem> actionsList = new ArrayList<GXDLMSActionItem>();
        if (reader.isStartElement("Actions", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSActionItem it = new GXDLMSActionItem();
                it.setLogicalName(reader.readElementContentAsString("LN"));
                it.setScriptSelector(
                        reader.readElementContentAsInt("ScriptSelector"));
                actionsList.add(it);
            }
            reader.readEndElement("Actions");
        }
        actions = actionsList.toArray(new GXDLMSActionItem[0]);

        List<String> permissions = new ArrayList<String>();
        if (reader.isStartElement("PermissionTable", true)) {
            while (reader.isStartElement("Item", false)) {
                permissions.add(reader.readElementContentAsString("Item"));
            }
            reader.readEndElement("PermissionTable");
        }
        permissionsTable = permissions.toArray(new String[0]);

        List<int[]> weightings = new ArrayList<int[]>();
        if (reader.isStartElement("WeightingTable", true)) {
            while (reader.isStartElement("Weightings", true)) {
                List<Integer> list = new ArrayList<Integer>();
                while (reader.isStartElement("Item", false)) {
                    list.add(reader.readElementContentAsInt("Item"));
                }
                weightings.add(GXCommon.toIntArray(list));
            }
            reader.readEndElement("WeightingTable");
        }
        weightingsTable = weightings.toArray(new int[0][0]);
        List<String> mostRecentRequests = new ArrayList<String>();
        if (reader.isStartElement("MostRecentRequestsTable", true)) {
            while (reader.isStartElement("Item", false)) {
                permissions.add(reader.readElementContentAsString("Item"));
            }
            reader.readEndElement("MostRecentRequestsTable");
        }
        mostRecentRequestsTable = mostRecentRequests.toArray(new String[0]);
        lastOutcome = (byte) reader.readElementContentAsInt("LastOutcome");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (actions != null) {
            writer.writeStartElement("Actions");
            for (GXDLMSActionItem it : actions) {
                writer.writeStartElement("Item");
                writer.writeElementString("LN", it.getLogicalName());
                writer.writeElementString("ScriptSelector",
                        it.getScriptSelector());
                writer.writeEndElement();
            }
            writer.writeEndElement();// Actions
        }

        if (permissionsTable != null) {
            writer.writeStartElement("PermissionTable");
            for (Object it : permissionsTable) {
                writer.writeElementString("Item", String.valueOf(it));
            }
            writer.writeEndElement();// PermissionTable
        }
        if (weightingsTable != null) {
            writer.writeStartElement("WeightingTable");
            for (Object it : weightingsTable) {
                writer.writeStartElement("Weightings");
                for (Object it2 : (List<?>) it) {
                    writer.writeElementString("Item", (int) it2);
                }
                writer.writeEndElement();// Weightings
            }
            writer.writeEndElement();// WeightingTable
        }

        if (mostRecentRequestsTable != null) {
            writer.writeStartElement("MostRecentRequestsTable");
            for (String it : mostRecentRequestsTable) {
                writer.writeElementString("Item", it);
            }
            writer.writeEndElement();// MostRecentRequestsTable
        }
        writer.writeElementString("LastOutcome", lastOutcome);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}