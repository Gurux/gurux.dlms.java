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
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXArray;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXStructure;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.NtpAuthenticationMethod;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSNtpSetup
 */
public class GXDLMSNtpSetup extends GXDLMSObject implements IGXDLMSBase {

    /**
     * Is NTP time synchronisation active.
     */
    private boolean activated;
    /**
     * NTP server address.
     */
    private String serverAddress;
    /**
     * UDP port related to this protocol.
     */
    private int port;
    /**
     * Authentication method.
     */
    private NtpAuthenticationMethod authentication;
    /**
     * Symmetric keys for authentication.
     */
    private Map<Long, byte[]> keys;
    /**
     * Client key (NTP server public key).
     */
    private byte[] clientKey;

    /**
     * Constructor.
     */
    public GXDLMSNtpSetup() {
        this("0.0.25.10.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSNtpSetup(final String ln) {
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
    public GXDLMSNtpSetup(final String ln, final int sn) {
        super(ObjectType.NTP_SETUP, ln, sn);
        keys = new HashMap<Long, byte[]>();
        port = 123;
        authentication = NtpAuthenticationMethod.NO_SECURITY;
    }

    /**
     * @return Is NTP time synchronisation active.
     */
    public final boolean getActivated() {
        return activated;
    }

    /**
     * @param value
     *            Is NTP time synchronisation active.
     */
    public final void setActivated(final boolean value) {
        activated = value;
    }

    /**
     * @return NTP server address.
     */
    public final String getServerAddress() {
        return serverAddress;
    }

    /**
     * @param value
     *            NTP server address.
     */
    public final void setServerAddress(final String value) {
        serverAddress = value;
    }

    /**
     * @return UDP port related to this protocol.
     */
    public final int getPort() {
        return port;
    }

    /**
     * @param value
     *            UDP port related to this protocol.
     */
    public final void setPort(final int value) {
        port = value;
    }

    /**
     * @return Authentication method.
     */
    public final NtpAuthenticationMethod getAuthentication() {
        return authentication;
    }

    /**
     * @param value
     *            Authentication method.
     */
    public final void setAuthentication(final NtpAuthenticationMethod value) {
        authentication = value;
    }

    /**
     * @return Symmetric keys for authentication.
     */
    public final Map<Long, byte[]> getKeys() {
        return keys;
    }

    /**
     * @param value
     *            Symmetric keys for authentication.
     */
    public final void setKeys(final Map<Long, byte[]> value) {
        keys = value;
    }

    /**
     * @return Client key (NTP server public key).
     */
    public final byte[] getClientKey() {
        return clientKey;
    }

    /**
     * @param value
     *            Client key (NTP server public key).
     */
    public final void setClientKey(final byte[] value) {
        clientKey = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), activated, serverAddress, port,
                authentication, keys, clientKey };
    }

    /**
     * Synchronizes the time of the DLMS server with the NTP server.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     * @throws SignatureException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public final byte[][] synchronize(GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 1, 0, DataType.INT8);
    }

    /**
     * Adds a new symmetric authentication key to authentication key array.
     * 
     * @param client
     *            DLMS client.
     * @param id
     *            Authentication key Id.
     * @param key
     *            authentication Key.
     * @return Action bytes.
     * @throws SignatureException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public final byte[][] addAuthenticationKey(GXDLMSClient client, int id,
            byte[] key) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, SignatureException {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE);
        bb.setUInt8(2);
        bb.setUInt8(DataType.UINT32);
        bb.setUInt32(id);
        bb.setUInt8(DataType.OCTET_STRING);
        GXCommon.setObjectCount(key.length, bb);
        bb.set(key);
        return client.method(this, 2, bb.array(), DataType.STRUCTURE);
    }

    /**
     * Remove symmetric authentication key.
     * 
     * @param client
     *            DLMS client.
     * @param id
     *            Authentication key Id.
     * @return Action bytes.
     * @throws SignatureException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public final byte[][] deleteAuthenticationKey(GXDLMSClient client, int id)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 3, id, DataType.INT8);
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
        // Activated
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // ServerAddress
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // Port
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // Authentication
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // Keys
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // ClientKey
        if (all || canRead(7)) {
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
        return 3;
    }

    @Override
    public final DataType getUIDataType(final int index) {
        if (index == 3) {
            return DataType.STRING;
        }
        return super.getUIDataType(index);
    }

    @Override
    public final DataType getDataType(final int index) {
        DataType dt;
        switch (index) {
        case 1:
            dt = DataType.OCTET_STRING;
            break;
        case 2:
            dt = DataType.BOOLEAN;
            break;
        case 3:
            dt = DataType.OCTET_STRING;
            break;
        case 4:
            dt = DataType.UINT16;
            break;
        case 5:
            dt = DataType.ENUM;
            break;
        case 6:
            dt = DataType.ARRAY;
            break;
        case 7:
            dt = DataType.OCTET_STRING;
            break;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
        return dt;
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        Object ret;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            ret = activated;
            break;
        case 3:
            ret = serverAddress;
            break;
        case 4:
            ret = port;
            break;
        case 5:
            ret = authentication.ordinal();
            break;
        case 6:
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(keys.size(), bb);
            for (Object tmp : keys.entrySet()) {
                @SuppressWarnings("unchecked")
                Map.Entry<Long, byte[]> it = (Map.Entry<Long, byte[]>) tmp;
                bb.setUInt8(DataType.STRUCTURE);
                bb.setUInt8(2); // Count
                bb.setUInt8(DataType.UINT32.getValue());
                bb.setUInt32(it.getKey());
                bb.setUInt8(DataType.OCTET_STRING);
                GXCommon.setObjectCount(it.getValue().length, bb);
                bb.set(it.getValue());
            }
            ret = bb.array();
            break;
        case 7:
            ret = clientKey;
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            ret = null;
            break;
        }
        return ret;
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
        case 2:
            activated = (Boolean) e.getValue();
            break;
        case 3:
            if (e.getValue() instanceof byte[]) {
                serverAddress = new String((byte[]) e.getValue());
            } else if (e.getValue() instanceof String) {
                serverAddress = (String) e.getValue();
            } else {
                serverAddress = null;
            }
            break;
        case 4:
            port = ((Number) e.getValue()).intValue();
            break;
        case 5:
            authentication = NtpAuthenticationMethod
                    .values()[((Number) e.getValue()).intValue()];
            break;
        case 6: {
            keys.clear();
            if (e.getValue() != null) {
                for (Object tmp : (GXArray) e.getValue()) {
                    GXStructure it = (GXStructure) tmp;
                    keys.put((long) it.get(0), (byte[]) it.get(1));
                }
            }
        }
            break;
        case 7:
            clientKey = (byte[]) e.getValue();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        activated = reader.readElementContentAsInt("Activated", 1) != 0;
        serverAddress =
                reader.readElementContentAsString("ServerAddress", null);
        port = reader.readElementContentAsInt("Port", 0);
        authentication = NtpAuthenticationMethod.values()[reader
                .readElementContentAsInt("Authentication", 0)];
        keys.clear();
        if (reader.isStartElement("Keys", true)) {
            while (reader.isStartElement("Item", true)) {
                long id = reader.readElementContentAsLong("ID");
                byte[] key = GXCommon
                        .hexToBytes(reader.readElementContentAsString("Key"));
                keys.put(id, key);
            }
        }
        clientKey = GXCommon.hexToBytes(
                reader.readElementContentAsString("ServerAddress", null));
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Activated", activated);
        writer.writeElementString("ServerAddress", serverAddress);
        writer.writeElementString("Port", port);
        writer.writeElementString("Authentication", authentication.ordinal());
        writer.writeStartElement("Keys");
        for (Object tmp : keys.entrySet()) {
            @SuppressWarnings("unchecked")
            Map.Entry<Long, byte[]> it = (Map.Entry<Long, byte[]>) tmp;
            writer.writeStartElement("Item");
            writer.writeElementString("ID", it.getKey().toString());
            writer.writeElementString("Key",
                    GXCommon.toHex(it.getValue(), false));
            writer.writeEndElement();
        }
        writer.writeEndElement();// Keys
        writer.writeElementString("ClientKey",
                GXCommon.toHex(clientKey, false));
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }

    @Override
    public String[] getNames() {
        return new String[] { "Logical Name", "Activated", "ServerAddress",
                "Port", "Authentication", "Keys", "ClientKey" };
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "Synchronize", "Add authentication key",
                "Delete authentication key" };
    }
}