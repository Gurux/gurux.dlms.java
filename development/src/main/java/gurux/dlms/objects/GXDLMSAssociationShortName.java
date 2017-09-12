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
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;
import gurux.dlms.secure.GXSecure;

public class GXDLMSAssociationShortName extends GXDLMSObject
        implements IGXDLMSBase {
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSAssociationShortName.class.getName());
    private GXDLMSObjectCollection objectList;
    private String securitySetupReference;

    /**
     * Secret used in Authentication.
     */
    private byte[] secret;

    /**
     * Constructor.
     */
    public GXDLMSAssociationShortName() {
        this("0.0.40.0.0.255", 0xFA00);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSAssociationShortName(final String ln, final int sn) {
        super(ObjectType.ASSOCIATION_SHORT_NAME, ln, sn);
        objectList = new GXDLMSObjectCollection(this);
        setVersion(2);
    }

    /**
     * @return Secret used in LLS Authentication.
     */
    public final byte[] getSecret() {
        return secret;
    }

    /**
     * @param value
     *            Secret used in LLS Authentication.
     */
    public final void setSecret(final byte[] value) {
        secret = value;
    }

    public final GXDLMSObjectCollection getObjectList() {
        return objectList;
    }

    public final String getSecuritySetupReference() {
        return securitySetupReference;
    }

    public final void setSecuritySetupReference(final String value) {
        securitySetupReference = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), getObjectList(), null,
                getSecuritySetupReference() };
    }

    /*
     * Invokes method.
     * @param index Method index.
     */
    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // Check reply_to_HLS_authentication
        if (e.getIndex() == 8) {
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
                settings.setConnected(true);
                return GXSecure.secure(settings, settings.getCipher(), ic,
                        settings.getCtoSChallenge(), readSecret);
            } else {
                LOGGER.log(Level.INFO,
                        "Invalid CtoS:" + GXCommon.toHex(serverChallenge, false)
                                + "-" + GXCommon.toHex(clientChallenge, false));
                return null;
            }
        } else {
            settings.setConnected(false);
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
        if (getVersion() > 1) {
            // AccessRightsList is static and read only once.
            if (!isRead(3)) {
                attributes.add(new Integer(3));
            }
            // SecuritySetupReference is static and read only once.
            if (!isRead(4)) {
                attributes.add(new Integer(4));
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final int getAttributeCount() {
        if (getVersion() < 2) {
            return 2;
        }
        return 4;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 8;
    }

    private void getAccessRights(final GXDLMSObject item,
            final GXByteBuffer data) {
        data.setUInt8((byte) DataType.STRUCTURE.getValue());
        data.setUInt8((byte) 3);
        GXCommon.setData(data, DataType.UINT16, item.getShortName());
        data.setUInt8((byte) DataType.ARRAY.getValue());
        data.setUInt8((byte) item.getAttributes().size());
        for (GXDLMSAttributeSettings att : item.getAttributes()) {
            // attribute_access_item
            data.setUInt8((byte) DataType.STRUCTURE.getValue());
            data.setUInt8((byte) 3);
            GXCommon.setData(data, DataType.INT8, att.getIndex());
            GXCommon.setData(data, DataType.ENUM, att.getAccess().getValue());
            GXCommon.setData(data, DataType.NONE, null);
        }
        data.setUInt8((byte) DataType.ARRAY.getValue());
        data.setUInt8((byte) item.getMethodAttributes().size());
        for (GXDLMSAttributeSettings it : item.getMethodAttributes()) {
            // attribute_access_item
            data.setUInt8((byte) DataType.STRUCTURE.getValue());
            data.setUInt8((byte) 2);
            GXCommon.setData(data, DataType.INT8, it.getIndex());
            GXCommon.setData(data, DataType.ENUM,
                    it.getMethodAccess().getValue());
        }
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        } else if (index == 2) {
            return DataType.ARRAY;
        } else if (index == 3) {
            return DataType.ARRAY;
        } else if (index == 4) {
            return DataType.OCTET_STRING;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    /**
     * Returns Association View.
     */
    private byte[] getObjects(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        GXByteBuffer bb = new GXByteBuffer();
        int cnt = objectList.size();

        // Add count only for first time.
        if (settings.getIndex() == 0) {
            settings.setCount(cnt);
            bb.setUInt8((byte) DataType.ARRAY.getValue());
            // Add count
            GXCommon.setObjectCount(cnt, bb);
        }
        int pos = 0;
        if (cnt != 0) {
            for (GXDLMSObject it : objectList) {
                ++pos;
                if (!(pos <= settings.getIndex())) {
                    bb.setUInt8((byte) DataType.STRUCTURE.getValue());
                    // Count
                    bb.setUInt8((byte) 4);
                    // base address.
                    GXCommon.setData(bb, DataType.INT16, it.getShortName());
                    // ClassID
                    GXCommon.setData(bb, DataType.UINT16,
                            it.getObjectType().getValue());
                    // Version
                    GXCommon.setData(bb, DataType.UINT8, 0);
                    // LN
                    GXCommon.setData(bb, DataType.OCTET_STRING,
                            GXCommon.logicalNameToBytes(it.getLogicalName()));
                    settings.setIndex(settings.getIndex() + 1);
                    if (settings.isServer()) {
                        // If PDU is full.
                        if (!e.isSkipMaxPduSize()
                                && bb.size() >= settings.getMaxPduSize()) {
                            break;
                        }
                    }
                }
            }
        }
        return bb.array();
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        GXByteBuffer bb = new GXByteBuffer();
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        } else if (e.getIndex() == 2) {
            return getObjects(settings, e);
        } else if (e.getIndex() == 3) {
            boolean lnExists = objectList.findBySN(this.getShortName()) != null;
            // Add count
            int cnt = objectList.size();
            if (!lnExists) {
                ++cnt;
            }
            bb.setUInt8(DataType.ARRAY.getValue());
            GXCommon.setObjectCount(cnt, bb);
            for (GXDLMSObject it : objectList) {
                getAccessRights(it, bb);
            }
            if (!lnExists) {
                getAccessRights(this, bb);
            }
            return bb.array();
        } else if (e.getIndex() == 4) {
            return GXCommon.getBytes(securitySetupReference);
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    final void updateAccessRights(final Object[] buff) {
        for (Object access : buff) {
            int sn = ((Number) Array.get(access, 0)).intValue();
            GXDLMSObject obj = objectList.findBySN(sn);
            if (obj != null) {
                for (Object attributeAccess : (Object[]) Array.get(access, 1)) {
                    int id = ((Number) Array.get(attributeAccess, 0))
                            .intValue();
                    int tmp =
                            ((Number) Array.get(attributeAccess, 1)).intValue();
                    AccessMode mode = AccessMode.forValue(tmp);
                    obj.setAccess(id, mode);
                }
                for (Object methodAccess : (Object[]) Array.get(access, 2)) {
                    int id = ((Number) ((Object[]) methodAccess)[0]).intValue();
                    int tmp =
                            ((Number) ((Object[]) methodAccess)[1]).intValue();
                    MethodAccessMode mode = MethodAccessMode.forValue(tmp);
                    obj.setMethodAccess(id, mode);
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
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            objectList.clear();
            if (e.getValue() != null) {
                for (Object item : (Object[]) e.getValue()) {
                    int sn = ((Number) Array.get(item, 0)).intValue() & 0xFFFF;
                    ObjectType type = ObjectType
                            .forValue(((Number) Array.get(item, 1)).intValue());
                    int version = ((Number) Array.get(item, 2)).intValue();
                    String ln =
                            GXCommon.toLogicalName((byte[]) Array.get(item, 3));
                    GXDLMSObject obj =
                            gurux.dlms.GXDLMSClient.createObject(type);
                    obj.setLogicalName(ln);
                    obj.setShortName(sn);
                    obj.setVersion(version);
                    objectList.add(obj);
                }
            }
        } else if (e.getIndex() == 3) {
            if (e.getValue() == null) {
                for (GXDLMSObject it : objectList) {
                    for (int pos = 1; pos != it.getAttributeCount(); ++pos) {
                        it.setAccess(pos, AccessMode.NO_ACCESS);
                    }
                }
            } else {
                updateAccessRights((Object[]) e.getValue());
            }
        } else if (e.getIndex() == 4) {
            if (e.getValue() instanceof String) {
                securitySetupReference = e.getValue().toString();
            } else if (e.getValue() != null) {
                securitySetupReference = new String((byte[]) e.getValue());
            }
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        String str = reader.readElementContentAsString("Secret");
        if (str == null) {
            secret = null;
        } else {
            secret = GXDLMSTranslator.hexToBytes(str);
        }
        securitySetupReference =
                reader.readElementContentAsString("SecuritySetupReference");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Secret", GXDLMSTranslator.toHex(secret));
        writer.writeElementString("SecuritySetupReference",
                securitySetupReference);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}