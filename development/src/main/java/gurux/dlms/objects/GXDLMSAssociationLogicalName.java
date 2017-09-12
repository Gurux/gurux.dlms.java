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
import java.security.Signature;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.AssociationStatus;
import gurux.dlms.secure.GXSecure;

public class GXDLMSAssociationLogicalName extends GXDLMSObject
        implements IGXDLMSBase {
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSAssociationLogicalName.class.getName());
    private GXDLMSObjectCollection objectList;
    private short clientSAP;
    private short serverSAP;
    private GXApplicationContextName applicationContextName;
    private GXxDLMSContextType xDLMSContextInfo;
    private GXAuthenticationMechanismName authenticationMechanismName;
    /**
     * Secret used in Low Level Authentication.
     */
    private byte[] secret;

    private AssociationStatus associationStatus =
            AssociationStatus.NonAssociated;
    private String securitySetupReference;

    /**
     * Constructor.
     */
    public GXDLMSAssociationLogicalName() {
        this("0.0.40.0.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSAssociationLogicalName(final String ln) {
        super(ObjectType.ASSOCIATION_LOGICAL_NAME, ln, 0);
        setLogicalName(ln);
        objectList = new GXDLMSObjectCollection(this);
        applicationContextName = new GXApplicationContextName();
        xDLMSContextInfo = new GXxDLMSContextType();
        authenticationMechanismName = new GXAuthenticationMechanismName();
        setVersion(1);
    }

    public final GXDLMSObjectCollection getObjectList() {
        return objectList;
    }

    public final void setObjectList(final GXDLMSObjectCollection value) {
        objectList = value;
    }

    /*
     * Contains the identifiers of the COSEM client APs within the physical
     * devices hosting these APs, which belong to the AA modelled by the
     * Association LN object.
     */
    public final short getClientSAP() {
        return clientSAP;
    }

    public final void setClientSAP(final short value) {
        clientSAP = value;
    }

    /*
     * Contains the identifiers of the COSEM server (logical device) APs within
     * the physical devices hosting these APs, which belong to the AA modelled
     * by the Association LN object.
     */
    public final short getServerSAP() {
        return serverSAP;
    }

    public final void setServerSAP(final short value) {
        serverSAP = value;
    }

    public final GXApplicationContextName getApplicationContextName() {
        return applicationContextName;
    }

    public final GXxDLMSContextType getXDLMSContextInfo() {
        return xDLMSContextInfo;
    }

    public final GXAuthenticationMechanismName
            getAuthenticationMechanismName() {
        return authenticationMechanismName;
    }

    /**
     * @return Secret used in Low Level Authentication.
     */
    public final byte[] getSecret() {
        return secret;
    }

    /**
     * @param value
     *            Secret used in Low Level Authentication.
     */
    public final void setSecret(final byte[] value) {
        secret = value;
    }

    public final AssociationStatus getAssociationStatus() {
        return associationStatus;
    }

    public final void setAssociationStatus(final AssociationStatus value) {
        associationStatus = value;
    }

    public final String getSecuritySetupReference() {
        return securitySetupReference;
    }

    public final void setSecuritySetupReference(final String value) {
        securitySetupReference = value;
    }

    /**
     * Updates secret.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     */
    public byte[][] updateSecret(final GXDLMSClient client) {
        if (getAuthenticationMechanismName()
                .getMechanismId() == Authentication.NONE) {
            throw new IllegalArgumentException(
                    "Invalid authentication level in MechanismId.");
        }
        if (getAuthenticationMechanismName()
                .getMechanismId() == Authentication.HIGH_GMAC) {
            throw new IllegalArgumentException(
                    "HighGMAC secret is updated using Security setup.");
        }
        if (getAuthenticationMechanismName()
                .getMechanismId() == Authentication.LOW) {
            return client.write(this, 7);
        }
        // Action is used to update High authentication password.
        return client.method(this, 2, secret, DataType.OCTET_STRING);
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getObjectList(),
                clientSAP + "/" + serverSAP, getApplicationContextName(),
                getXDLMSContextInfo(), getAuthenticationMechanismName(),
                getSecret(), getAssociationStatus(),
                getSecuritySetupReference() };
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // Check reply_to_HLS_authentication
        if (e.getIndex() == 1) {
            byte[] serverChallenge = null, clientChallenge = null;
            long ic = 0;
            byte[] readSecret;
            boolean accept;
            if (settings.getAuthentication() == Authentication.HIGH_ECDSA) {
                try {
                    GXByteBuffer signature =
                            new GXByteBuffer((byte[]) e.getParameters());
                    Signature ver = Signature.getInstance("SHA256withECDSA");
                    ver.initVerify(settings.getCipher().getCertificates().get(0)
                            .getPublicKey());
                    GXByteBuffer bb = new GXByteBuffer();
                    bb.set(settings.getSourceSystemTitle());
                    bb.set(settings.getCipher().getSystemTitle());
                    bb.set(settings.getStoCChallenge());
                    bb.set(settings.getCtoSChallenge());
                    ver.update(bb.array());
                    accept = ver.verify(signature.array());

                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            } else {
                if (settings.getAuthentication() == Authentication.HIGH_GMAC) {
                    readSecret = settings.getSourceSystemTitle();
                    GXByteBuffer bb =
                            new GXByteBuffer((byte[]) e.getParameters());
                    bb.getUInt8();
                    ic = bb.getUInt32();
                } else {
                    readSecret = secret;
                }
                serverChallenge =
                        GXSecure.secure(settings, settings.getCipher(), ic,
                                settings.getStoCChallenge(), readSecret);
                clientChallenge = (byte[]) e.getParameters();
                accept = GXCommon.compare(serverChallenge, clientChallenge);
            }
            if (accept) {
                if (settings.getAuthentication() == Authentication.HIGH_GMAC) {
                    readSecret = settings.getCipher().getSystemTitle();
                    ic = settings.getCipher().getInvocationCounter();
                } else {
                    readSecret = secret;
                }
                byte[] tmp = GXSecure.secure(settings, settings.getCipher(), ic,
                        settings.getCtoSChallenge(), readSecret);
                settings.setConnected(true);
                return tmp;
            } else {
                LOGGER.log(Level.INFO,
                        "Invalid CtoS:" + GXCommon.toHex(serverChallenge, false)
                                + "-" + GXCommon.toHex(clientChallenge, false));
                settings.setConnected(false);
                return null;
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
            return null;
        }
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
        // ObjectList is static and read only once.
        if (!isRead(2)) {
            attributes.add(new Integer(2));
        }
        // associated_partners_id is static and read only once.
        if (!isRead(3)) {
            attributes.add(new Integer(3));
        }
        // Application Context Name is static and read only once.
        if (!isRead(4)) {
            attributes.add(new Integer(4));
        }
        // xDLMS Context Info
        if (!isRead(5)) {
            attributes.add(new Integer(5));
        }
        // Authentication Mechanism Name
        if (!isRead(6)) {
            attributes.add(new Integer(6));
        }
        // Secret
        if (!isRead(7)) {
            attributes.add(new Integer(7));
        }
        // Association Status
        if (!isRead(8)) {
            attributes.add(new Integer(8));
        }
        // Security Setup Reference is from version 1.
        if (getVersion() > 0 && !isRead(9)) {
            attributes.add(new Integer(9));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final int getAttributeCount() {
        // Security Setup Reference is from version 1.
        if (getVersion() > 0) {
            return 9;
        }
        return 8;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 4;
    }

    /**
     * Returns Association View.
     */
    private byte[] getObjects(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        try {
            GXByteBuffer data = new GXByteBuffer();
            // Add count only for first time.
            if (settings.getIndex() == 0) {
                settings.setCount(objectList.size());
                data.setUInt8(DataType.ARRAY.getValue());
                GXCommon.setObjectCount(objectList.size(), data);
            }
            int pos = 0;
            for (GXDLMSObject it : objectList) {
                ++pos;
                if (!(pos <= settings.getIndex())) {
                    data.setUInt8(DataType.STRUCTURE.getValue());
                    // Count
                    data.setUInt8(4);
                    // ClassID
                    GXCommon.setData(data, DataType.UINT16,
                            it.getObjectType().getValue());
                    // Version
                    GXCommon.setData(data, DataType.UINT8, it.getVersion());
                    // LN
                    GXCommon.setData(data, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(it.getLogicalName()));
                    getAccessRights(it, e.getServer(), data); // Access rights.
                    settings.setIndex(settings.getIndex() + 1);
                    if (settings.isServer()) {
                        // If PDU is full.
                        if (!e.isSkipMaxPduSize()
                                && data.size() >= settings.getMaxPduSize()) {
                            break;
                        }
                    }
                }

            }
            return data.array();
        } catch (Exception ex) {
            e.setError(ErrorCode.HARDWARE_FAULT);
            return null;
        }

    }

    private void getAccessRights(final GXDLMSObject item,
            final GXDLMSServerBase server, final GXByteBuffer data)
            throws Exception {
        data.setUInt8(DataType.STRUCTURE.getValue());
        data.setUInt8(2);
        if (server == null) {
            data.setUInt8(DataType.ARRAY.getValue());
            data.setUInt8(0);
            data.setUInt8(DataType.ARRAY.getValue());
            data.setUInt8(0);
        } else {
            data.setUInt8(DataType.ARRAY.getValue());
            int cnt = item.getAttributeCount();
            data.setUInt8(cnt);
            ValueEventArgs e = new ValueEventArgs(server, item, 0, 0, null);
            for (int pos = 0; pos != cnt; ++pos) {
                e.setIndex(pos + 1);
                AccessMode m = server.notifyGetAttributeAccess(e);
                // attribute_access_item
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(3);
                GXCommon.setData(data, DataType.INT8, pos + 1);
                GXCommon.setData(data, DataType.ENUM, m.getValue());
                GXCommon.setData(data, DataType.NONE, null);
            }
            data.setUInt8(DataType.ARRAY.getValue());
            cnt = item.getMethodCount();
            data.setUInt8(cnt);
            for (int pos = 0; pos != cnt; ++pos) {
                e.setIndex(pos + 1);
                // attribute_access_item
                data.setUInt8(DataType.STRUCTURE.getValue());
                data.setUInt8(2);
                GXCommon.setData(data, DataType.INT8, pos + 1);
                MethodAccessMode m = server.notifyGetMethodAccess(e);
                // If version is 0.
                if (getVersion() == 0) {
                    GXCommon.setData(data, DataType.BOOLEAN, m.getValue() != 0);
                } else {
                    GXCommon.setData(data, DataType.ENUM, m.getValue());
                }
            }
        }
    }

    static final void updateAccessRights(final GXDLMSObject obj,
            final Object[] buff) {
        if (buff != null && buff.length != 0) {
            for (Object access : (Object[]) Array.get(buff, 0)) {
                Object[] attributeAccess = (Object[]) access;
                int id = ((Number) attributeAccess[0]).intValue();
                int tmp = ((Number) attributeAccess[1]).intValue();
                AccessMode aMode = AccessMode.forValue(tmp);
                obj.setAccess(id, aMode);
            }
            for (Object access : (Object[]) Array.get(buff, 1)) {
                Object[] methodAccess = (Object[]) access;
                int id = ((Number) methodAccess[0]).intValue();
                int tmp;
                // If version is 0.
                if (methodAccess[1] instanceof Boolean) {
                    if (((Boolean) methodAccess[1]).booleanValue()) {
                        tmp = 1;
                    } else {
                        tmp = 0;
                    }
                } else {
                    // If version is 1.
                    tmp = ((Number) methodAccess[1]).intValue();
                }
                MethodAccessMode mode = MethodAccessMode.forValue(tmp);
                obj.setMethodAccess(id, mode);
            }
        }
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
            return DataType.STRUCTURE;
        }
        if (index == 5) {
            return DataType.STRUCTURE;
        }
        if (index == 6) {
            return DataType.STRUCTURE;
        }
        if (index == 7) {
            return DataType.OCTET_STRING;
        }
        if (index == 8) {
            return DataType.ENUM;
        }
        if (index == 9) {
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
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            return getObjects(settings, e);
        }
        if (e.getIndex() == 3) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.ARRAY.getValue());
            // Add count
            data.setUInt8(2);
            data.setUInt8(DataType.UINT8.getValue());
            data.setUInt8(clientSAP);
            data.setUInt8(DataType.UINT16.getValue());
            data.setUInt16(serverSAP);
            return data.array();
        }
        if (e.getIndex() == 4) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            // Add count
            data.setUInt8(0x7);
            GXCommon.setData(data, DataType.UINT8,
                    applicationContextName.getJointIsoCtt());
            GXCommon.setData(data, DataType.UINT8,
                    applicationContextName.getCountry());
            GXCommon.setData(data, DataType.UINT16,
                    applicationContextName.getCountryName());
            GXCommon.setData(data, DataType.UINT8,
                    applicationContextName.getIdentifiedOrganization());
            GXCommon.setData(data, DataType.UINT8,
                    applicationContextName.getDlmsUA());
            GXCommon.setData(data, DataType.UINT8,
                    applicationContextName.getApplicationContext());
            GXCommon.setData(data, DataType.UINT8,
                    applicationContextName.getContextId());
            return data.array();
        }
        if (e.getIndex() == 5) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            data.setUInt8(6);
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt32(
                    Conformance.toInteger(xDLMSContextInfo.getConformance()));
            GXCommon.setData(data, DataType.BITSTRING, bb.subArray(1, 3));
            GXCommon.setData(data, DataType.UINT16,
                    xDLMSContextInfo.getMaxReceivePduSize());
            GXCommon.setData(data, DataType.UINT16,
                    xDLMSContextInfo.getMaxSendPduSize());
            GXCommon.setData(data, DataType.UINT8,
                    xDLMSContextInfo.getDlmsVersionNumber());
            GXCommon.setData(data, DataType.INT8,
                    xDLMSContextInfo.getQualityOfService());
            GXCommon.setData(data, DataType.OCTET_STRING,
                    xDLMSContextInfo.getCypheringInfo());
            return data.array();
        }
        if (e.getIndex() == 6) {
            GXByteBuffer data = new GXByteBuffer();
            data.setUInt8(DataType.STRUCTURE.getValue());
            // Add count
            data.setUInt8(0x7);
            GXCommon.setData(data, DataType.UINT8,
                    authenticationMechanismName.getJointIsoCtt());
            GXCommon.setData(data, DataType.UINT8,
                    authenticationMechanismName.getCountry());
            GXCommon.setData(data, DataType.UINT16,
                    authenticationMechanismName.getCountryName());
            GXCommon.setData(data, DataType.UINT8,
                    authenticationMechanismName.getIdentifiedOrganization());
            GXCommon.setData(data, DataType.UINT8,
                    authenticationMechanismName.getDlmsUA());
            GXCommon.setData(data, DataType.UINT8, authenticationMechanismName
                    .getAuthenticationMechanismName());
            GXCommon.setData(data, DataType.UINT8,
                    authenticationMechanismName.getMechanismId().getValue());
            return data.array();
        }
        if (e.getIndex() == 7) {
            return secret;
        }
        if (e.getIndex() == 8) {
            return getAssociationStatus().ordinal();
        }
        if (e.getIndex() == 9) {
            return GXCommon.logicalNameToBytes(getSecuritySetupReference());
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    static void updateObjectList(final GXDLMSSettings settings,
            final GXDLMSObjectCollection target, final Object value) {
        target.clear();
        if (value != null) {
            for (Object item : (Object[]) value) {
                ObjectType type = ObjectType
                        .forValue(((Number) Array.get(item, 0)).intValue());
                int version = ((Number) Array.get(item, 1)).intValue();
                String ln = GXCommon.toLogicalName((byte[]) Array.get(item, 2));
                GXDLMSObject obj = settings.getObjects().findByLN(type, ln);
                if (obj == null) {
                    obj = gurux.dlms.GXDLMSClient.createObject(type);
                    obj.setLogicalName(ln);
                    obj.setVersion(version);
                }
                // Add only known objects.
                if (obj instanceof IGXDLMSBase) {
                    updateAccessRights(obj, (Object[]) Array.get(item, 3));
                    target.add(obj);
                }
            }
        }
    }

    private void updateApplicationContextName(final Object value) {
        // Value of the object identifier encoded in BER
        if (value instanceof byte[]) {
            GXByteBuffer buff = new GXByteBuffer((byte[]) value);
            if (buff.getUInt8(0) == 0x60) {
                applicationContextName.setJointIsoCtt(0);
                applicationContextName.setCountry(0);
                applicationContextName.setCountryName(0);
                buff.position(buff.position() + 3);
                applicationContextName
                        .setIdentifiedOrganization(buff.getUInt8());
                applicationContextName.setDlmsUA(buff.getUInt8());
                applicationContextName.setApplicationContext(buff.getUInt8());
                applicationContextName.setContextId(buff.getUInt8());
            } else {
                // Get Tag and length.
                if (buff.getUInt8() != 2 && buff.getUInt8() != 7) {
                    throw new IllegalArgumentException();
                }
                // Get tag
                if (buff.getUInt8() != 0x11) {
                    throw new IllegalArgumentException();
                }
                applicationContextName.setJointIsoCtt(buff.getUInt8());
                // Get tag
                if (buff.getUInt8() != 0x11) {
                    throw new IllegalArgumentException();
                }
                applicationContextName.setCountry(buff.getUInt8());
                // Get tag
                if (buff.getUInt8() != 0x12) {
                    throw new IllegalArgumentException();
                }
                applicationContextName.setCountryName(buff.getUInt16());
                // Get tag
                if (buff.getUInt8() != 0x11) {
                    throw new IllegalArgumentException();
                }
                applicationContextName
                        .setIdentifiedOrganization(buff.getUInt8());
                // Get tag
                if (buff.getUInt8() != 0x11) {
                    throw new IllegalArgumentException();
                }
                applicationContextName.setDlmsUA(buff.getUInt8());
                // Get tag
                if (buff.getUInt8() != 0x11) {
                    throw new IllegalArgumentException();
                }
                applicationContextName.setApplicationContext(buff.getUInt8());
                // Get tag
                if (buff.getUInt8() != 0x11) {
                    throw new IllegalArgumentException();
                }
                applicationContextName.setContextId(buff.getUInt8());
            }
        } else {
            if (value != null) {
                applicationContextName.setJointIsoCtt(
                        ((Number) Array.get(value, 0)).intValue());
                applicationContextName
                        .setCountry(((Number) Array.get(value, 1)).intValue());
                applicationContextName.setCountryName(
                        ((Number) Array.get(value, 2)).intValue());
                applicationContextName.setIdentifiedOrganization(
                        ((Number) Array.get(value, 3)).intValue());
                applicationContextName
                        .setDlmsUA(((Number) Array.get(value, 4)).intValue());
                applicationContextName.setApplicationContext(
                        ((Number) Array.get(value, 5)).intValue());
                applicationContextName.setContextId(
                        ((Number) Array.get(value, 6)).intValue());
            }
        }
    }

    private void updateAuthenticationMechanismName(final Object value) {
        if (value != null) {
            // Value of the object identifier encoded in BER
            if (value instanceof byte[]) {
                GXByteBuffer buff = new GXByteBuffer((byte[]) value);
                if (buff.getUInt8(0) == 0x60) {
                    authenticationMechanismName.setJointIsoCtt(0);
                    authenticationMechanismName.setCountry(0);
                    authenticationMechanismName.setCountryName(0);
                    buff.position(buff.position() + 3);
                    authenticationMechanismName
                            .setIdentifiedOrganization(buff.getUInt8());
                    authenticationMechanismName.setDlmsUA(buff.getUInt8());
                    authenticationMechanismName
                            .setAuthenticationMechanismName(buff.getUInt8());
                    authenticationMechanismName.setMechanismId(
                            Authentication.forValue(buff.getUInt8()));
                } else {
                    // Get Tag and length.
                    if (buff.getUInt8() != 2 && buff.getUInt8() != 7) {
                        throw new IllegalArgumentException();
                    }
                    // Get tag
                    if (buff.getUInt8() != 0x11) {
                        throw new IllegalArgumentException();
                    }
                    authenticationMechanismName.setJointIsoCtt(buff.getUInt8());
                    // Get tag
                    if (buff.getUInt8() != 0x11) {
                        throw new IllegalArgumentException();
                    }
                    authenticationMechanismName.setCountry(buff.getUInt8());
                    // Get tag
                    if (buff.getUInt8() != 0x12) {
                        throw new IllegalArgumentException();
                    }
                    authenticationMechanismName
                            .setCountryName(buff.getUInt16());
                    // Get tag
                    if (buff.getUInt8() != 0x11) {
                        throw new IllegalArgumentException();
                    }
                    authenticationMechanismName
                            .setIdentifiedOrganization(buff.getUInt8());
                    // Get tag
                    if (buff.getUInt8() != 0x11) {
                        throw new IllegalArgumentException();
                    }
                    authenticationMechanismName.setDlmsUA(buff.getUInt8());
                    // Get tag
                    if (buff.getUInt8() != 0x11) {
                        throw new IllegalArgumentException();
                    }
                    authenticationMechanismName
                            .setAuthenticationMechanismName(buff.getUInt8());
                    // Get tag
                    if (buff.getUInt8() != 0x11) {
                        throw new IllegalArgumentException();
                    }
                    authenticationMechanismName.setMechanismId(
                            Authentication.forValue(buff.getUInt8()));
                }
            } else {
                if (value != null) {
                    authenticationMechanismName.setJointIsoCtt(
                            ((Number) Array.get(value, 0)).intValue());
                    authenticationMechanismName.setCountry(
                            ((Number) Array.get(value, 1)).intValue());
                    authenticationMechanismName.setCountryName(
                            ((Number) Array.get(value, 2)).intValue());
                    authenticationMechanismName.setIdentifiedOrganization(
                            ((Number) Array.get(value, 3)).intValue());
                    authenticationMechanismName.setDlmsUA(
                            ((Number) Array.get(value, 4)).intValue());
                    authenticationMechanismName.setAuthenticationMechanismName(
                            ((Number) Array.get(value, 5)).intValue());
                    authenticationMechanismName
                            .setMechanismId(Authentication.forValue(
                                    ((Number) Array.get(value, 6)).intValue()));
                }
            }
        }
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
            updateObjectList(settings, objectList, e.getValue());
            break;
        case 3:
            if (e.getValue() != null) {
                clientSAP = ((Number) Array.get(e.getValue(), 0)).shortValue();
                serverSAP = ((Number) Array.get(e.getValue(), 1)).shortValue();
            }
            break;
        case 4:
            updateApplicationContextName(e.getValue());
            break;
        case 5:
            if (e.getValue() != null) {
                xDLMSContextInfo.setConformance(Conformance.forValue(
                        ((Number) Array.get(e.getValue(), 1)).intValue()));
                xDLMSContextInfo.setMaxReceivePduSize(
                        ((Number) Array.get(e.getValue(), 1)).intValue());
                xDLMSContextInfo.setMaxSendPduSize(
                        ((Number) Array.get(e.getValue(), 2)).intValue());
                xDLMSContextInfo.setDlmsVersionNumber(
                        ((Number) Array.get(e.getValue(), 3)).byteValue());
                xDLMSContextInfo.setQualityOfService(
                        ((Number) Array.get(e.getValue(), 4)).intValue());
                xDLMSContextInfo
                        .setCypheringInfo((byte[]) Array.get(e.getValue(), 5));
            }
            break;
        case 6:
            updateAuthenticationMechanismName(e.getValue());
            break;
        case 7:
            secret = (byte[]) e.getValue();
            break;
        case 8:
            if (e.getValue() == null) {
                setAssociationStatus(AssociationStatus.NonAssociated);
            } else {
                setAssociationStatus(AssociationStatus
                        .values()[((Number) e.getValue()).intValue()]);
            }
            break;
        case 9:
            setSecuritySetupReference(
                    GXCommon.toLogicalName((byte[]) e.getValue()));
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        clientSAP = (byte) reader.readElementContentAsInt("ClientSAP");
        serverSAP = (byte) reader.readElementContentAsInt("ServerSAP");
        if (reader.isStartElement("ApplicationContextName", true)) {
            applicationContextName.setJointIsoCtt(
                    reader.readElementContentAsInt("JointIsoCtt"));
            applicationContextName
                    .setCountry(reader.readElementContentAsInt("Country"));
            applicationContextName.setCountryName(
                    reader.readElementContentAsInt("CountryName"));
            applicationContextName.setIdentifiedOrganization(
                    reader.readElementContentAsInt("IdentifiedOrganization"));
            applicationContextName
                    .setDlmsUA(reader.readElementContentAsInt("DlmsUA"));
            applicationContextName.setApplicationContext(
                    reader.readElementContentAsInt("ApplicationContext"));
            applicationContextName
                    .setContextId(reader.readElementContentAsInt("ContextId"));
            reader.readEndElement("ApplicationContextName");
        }

        if (reader.isStartElement("XDLMSContextInfo", true)) {
            xDLMSContextInfo.setConformance(Conformance
                    .forValue(reader.readElementContentAsInt("Conformance")));
            xDLMSContextInfo.setMaxReceivePduSize(
                    reader.readElementContentAsInt("MaxReceivePduSize"));
            xDLMSContextInfo.setMaxSendPduSize(
                    reader.readElementContentAsInt("MaxSendPduSize"));
            xDLMSContextInfo.setDlmsVersionNumber(
                    (byte) reader.readElementContentAsInt("DlmsVersionNumber"));
            xDLMSContextInfo.setQualityOfService(
                    reader.readElementContentAsInt("QualityOfService"));
            xDLMSContextInfo.setCypheringInfo(GXDLMSTranslator.hexToBytes(
                    reader.readElementContentAsString("CypheringInfo")));
            reader.readEndElement("XDLMSContextInfo");
        }
        if (reader.isStartElement("XDLMSContextInfo", true)) {
            authenticationMechanismName.setJointIsoCtt(
                    reader.readElementContentAsInt("JointIsoCtt"));
            authenticationMechanismName
                    .setCountry(reader.readElementContentAsInt("Country"));
            authenticationMechanismName.setCountryName(
                    reader.readElementContentAsInt("CountryName"));
            authenticationMechanismName.setIdentifiedOrganization(
                    reader.readElementContentAsInt("IdentifiedOrganization"));
            authenticationMechanismName
                    .setDlmsUA(reader.readElementContentAsInt("DlmsUA"));
            authenticationMechanismName.setAuthenticationMechanismName(reader
                    .readElementContentAsInt("AuthenticationMechanismName"));
            authenticationMechanismName.setMechanismId(Authentication
                    .forValue(reader.readElementContentAsInt("MechanismId")));
            reader.readEndElement("XDLMSContextInfo");
        }
        String str = reader.readElementContentAsString("Secret");
        if (str == null) {
            secret = null;
        } else {
            secret = GXDLMSTranslator.hexToBytes(str);
        }
        associationStatus = AssociationStatus.values()[reader
                .readElementContentAsInt("AssociationStatus")];
        securitySetupReference =
                reader.readElementContentAsString("SecuritySetupReference");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("ClientSAP", clientSAP);
        writer.writeElementString("ServerSAP", serverSAP);
        if (applicationContextName != null) {
            writer.writeStartElement("ApplicationContextName");
            writer.writeElementString("JointIsoCtt",
                    applicationContextName.getJointIsoCtt());
            writer.writeElementString("Country",
                    applicationContextName.getCountry());
            writer.writeElementString("CountryName",
                    applicationContextName.getCountryName());
            writer.writeElementString("IdentifiedOrganization",
                    applicationContextName.getIdentifiedOrganization());
            writer.writeElementString("DlmsUA",
                    applicationContextName.getDlmsUA());
            writer.writeElementString("ApplicationContext",
                    applicationContextName.getApplicationContext());
            writer.writeElementString("ContextId",
                    applicationContextName.getContextId());
            writer.writeEndElement();
        }
        if (xDLMSContextInfo != null) {
            writer.writeStartElement("XDLMSContextInfo");
            writer.writeElementString("Conformance",
                    Conformance.toInteger(xDLMSContextInfo.getConformance()));
            writer.writeElementString("MaxReceivePduSize",
                    xDLMSContextInfo.getMaxReceivePduSize());
            writer.writeElementString("MaxSendPduSize",
                    xDLMSContextInfo.getMaxSendPduSize());
            writer.writeElementString("DlmsVersionNumber",
                    xDLMSContextInfo.getDlmsVersionNumber());
            writer.writeElementString("QualityOfService",
                    xDLMSContextInfo.getQualityOfService());
            writer.writeElementString("CypheringInfo", GXDLMSTranslator
                    .toHex(xDLMSContextInfo.getCypheringInfo()));
            writer.writeEndElement();
        }
        if (authenticationMechanismName != null) {
            writer.writeStartElement("XDLMSContextInfo");
            writer.writeElementString("JointIsoCtt",
                    authenticationMechanismName.getJointIsoCtt());
            writer.writeElementString("Country",
                    authenticationMechanismName.getCountry());
            writer.writeElementString("CountryName",
                    authenticationMechanismName.getCountryName());
            writer.writeElementString("IdentifiedOrganization",
                    authenticationMechanismName.getIdentifiedOrganization());
            writer.writeElementString("DlmsUA",
                    authenticationMechanismName.getDlmsUA());
            writer.writeElementString("AuthenticationMechanismName",
                    authenticationMechanismName
                            .getAuthenticationMechanismName());
            writer.writeElementString("MechanismId",
                    authenticationMechanismName.getMechanismId().getValue());
            writer.writeEndElement();
        }
        writer.writeElementString("Secret", GXDLMSTranslator.toHex(secret));
        writer.writeElementString("AssociationStatus",
                associationStatus.ordinal());
        writer.writeElementString("SecuritySetupReference",
                securitySetupReference);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}