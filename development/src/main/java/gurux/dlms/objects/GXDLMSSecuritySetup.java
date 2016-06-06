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

import java.util.ArrayList;
import java.util.List;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.CertificateEntity;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.GlobalKeyType;
import gurux.dlms.objects.enums.SecurityPolicy;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.dlms.secure.GXDLMSSecureClient;

public class GXDLMSSecuritySetup extends GXDLMSObject implements IGXDLMSBase {

    /**
     * Security policy.
     */
    private SecurityPolicy securityPolicy;

    /**
     * Security suite.
     */
    private SecuritySuite securitySuite;

    /**
     * Server system title.
     */
    private byte[] serverSystemTitle;

    /**
     * Client system title.
     */
    private byte[] clientSystemTitle;

    /**
     * Available certificates.
     */
    private List<GXDLMSCertificateInfo> certificates;

    /**
     * Constructor.
     */
    public GXDLMSSecuritySetup() {
        this("0.0.43.0.0.255");
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSecuritySetup(final String ln) {
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
    public GXDLMSSecuritySetup(final String ln, final int sn) {
        super(ObjectType.SECURITY_SETUP, ln, sn);
        securityPolicy = SecurityPolicy.NOTHING;
        securitySuite = SecuritySuite.AES_GCM_128;
        certificates = new ArrayList<GXDLMSCertificateInfo>();
    }

    /**
     * @return Security policy.
     */
    public final SecurityPolicy getSecurityPolicy() {
        return securityPolicy;
    }

    /**
     * @param value
     *            Security policy.
     */
    public final void setSecurityPolicy(final SecurityPolicy value) {
        securityPolicy = value;
    }

    /**
     * @return Security suite.
     */
    public final SecuritySuite getSecuritySuite() {
        return securitySuite;
    }

    /**
     * @param value
     *            Security suite.
     */
    public final void setSecuritySuite(final SecuritySuite value) {
        securitySuite = value;
    }

    /**
     * @return Client system title.
     */
    public final byte[] getClientSystemTitle() {
        return clientSystemTitle;
    }

    /**
     * @param value
     *            Client system title.
     */
    public final void setClientSystemTitle(final byte[] value) {
        clientSystemTitle = value;
    }

    /**
     * @return Server system title.
     */
    public final byte[] getServerSystemTitle() {
        return serverSystemTitle;
    }

    /**
     * @param value
     *            Server system title.
     */
    public final void setServerSystemTitle(final byte[] value) {
        serverSystemTitle = value;
    }

    /**
     * @return Available certificates.
     */
    public final List<GXDLMSCertificateInfo> getCertificates() {
        return certificates;
    }

    /**
     * @param value
     *            Available certificates.
     */
    public final void setCertificates(final List<GXDLMSCertificateInfo> value) {
        certificates = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), securityPolicy, securitySuite,
                clientSystemTitle, serverSystemTitle, certificates };
    }

    /**
     * Activates and strengthens the security policy.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param security
     *            New security level.
     * @return Generated action.
     */
    public final byte[][] activate(final GXDLMSClient client,
            final Security security) {
        int value = 0;
        switch (security) {
        case NONE:
            value = 0;
            break;
        case AUTHENTICATION:
            value = 1;
            break;
        case ENCRYPTION:
            value = 2;
            break;
        case AUTHENTICATION_ENCRYPTION:
            value = 3;
            break;
        default:
            throw new IllegalArgumentException();
        }
        return client.method(this, 1, value, DataType.ENUM);
    }

    /**
     * Updates one or more global keys.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param kek
     *            Master key, also known as Key Encrypting Key.
     * @param list
     *            List of Global key types and keys.
     * @return Generated action.
     */
    public final byte[][] globalKeyTransfer(final GXDLMSClient client,
            final byte[] kek,
            final List<GXSimpleEntry<GlobalKeyType, byte[]>> list) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("Invalid list. It is empty.");
        }
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY.getValue());
        bb.setUInt8((byte) list.size());
        byte[] tmp;
        for (GXSimpleEntry<GlobalKeyType, byte[]> it : list) {
            bb.setUInt8(DataType.STRUCTURE.getValue());
            bb.setUInt8(2);
            GXCommon.setData(bb, DataType.ENUM, it.getKey().ordinal());
            tmp = GXDLMSSecureClient.encrypt(kek, it.getValue());
            GXCommon.setData(bb, DataType.OCTET_STRING, tmp);
        }
        return client.method(this, 2, bb.array(), DataType.ARRAY);
    }

    /**
     * Agree on one or more symmetric keys using the key agreement algorithm.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param list
     *            List of keys.
     * @return Generated action.
     */
    public final byte[][] keyAgreement(final GXDLMSClient client,
            final List<GXSimpleEntry<GlobalKeyType, byte[]>> list) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("Invalid list. It is empty.");
        }
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.ARRAY.getValue());
        bb.setUInt8((byte) list.size());
        for (GXSimpleEntry<GlobalKeyType, byte[]> it : list) {
            bb.setUInt8(DataType.STRUCTURE.getValue());
            bb.setUInt8(2);
            GXCommon.setData(bb, DataType.ENUM, it.getKey().ordinal());
            GXCommon.setData(bb, DataType.OCTET_STRING, it.getValue());
        }
        return client.method(this, 3, bb.array(), DataType.ARRAY);
    }

    /**
     * Generates an asymmetric key pair as required by the security suite.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param security
     *            New certificate type.
     * @return Generated action.
     */
    public final byte[][] generateKeyPair(final GXDLMSClient client,
            final CertificateType type) {
        return client.method(this, 4, type.getValue(), DataType.ENUM);
    }

    /**
     * Ask Server sends the Certificate Signing Request (CSR) data.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param type
     *            identifies the key pair for which the certificate will be
     *            requested.
     * @return Generated action.
     */
    public final byte[][] generateCertificate(final GXDLMSClient client,
            final CertificateType type) {
        return client.method(this, 5, type.getValue(), DataType.ENUM);
    }

    /**
     * Imports an X.509 v3 certificate of a public key.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param key
     *            Public key.
     * @return Generated action.
     */
    public final byte[][] importCertificate(final GXDLMSClient client,
            final byte[] key) {
        return client.method(this, 6, key, DataType.OCTET_STRING);
    }

    /**
     * Exports an X.509 v3 certificate from the server using entity information.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param entity
     *            Certificate entity.
     * @param type
     *            Certificate type.
     * @param systemTitle
     *            System title.
     * @return Generated action.
     */
    public final byte[][] exportCertificateByEntity(final GXDLMSClient client,
            final CertificateEntity entity, final CertificateType type,
            final byte[] systemTitle) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(2);
        // Add enum
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(0);
        // Add certificate_identification_by_entity
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(3);
        // Add certificate_entity
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(entity.getValue());
        // Add certificate_type
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(type.getValue());
        // system_title
        GXCommon.setData(bb, DataType.OCTET_STRING, systemTitle);
        return client.method(this, 7, bb.array(), DataType.OCTET_STRING);
    }

    /**
     * Exports an X.509 v3 certificate from the server using serial information.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param serialNumber
     *            Serial number.
     * @param issuer
     *            Issuer
     * @return Generated action.
     */
    public final byte[][] exportCertificateBySerial(final GXDLMSClient client,
            final byte[] serialNumber, final byte[] issuer) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(2);
        // Add enum
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(1);
        // Add certificate_identification_by_entity
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(2);
        // serialNumber
        GXCommon.setData(bb, DataType.OCTET_STRING, serialNumber);
        // issuer
        GXCommon.setData(bb, DataType.OCTET_STRING, issuer);
        return client.method(this, 7, bb.array(), DataType.OCTET_STRING);
    }

    /**
     * Removes X.509 v3 certificate from the server using entity.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param entity
     *            Certificate entity type.
     * @param type
     *            Certificate type.
     * @param systemTitle
     *            System title.
     * @return Generated action.
     */
    public final byte[][] removeCertificateByEntity(final GXDLMSClient client,
            final CertificateEntity entity, final CertificateType type,
            final byte[] systemTitle) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(2);
        // Add enum
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(0);
        // Add certificate_identification_by_entity
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(3);
        // Add certificate_entity
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(entity.getValue());
        // Add certificate_type
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(type.getValue());
        // system_title
        GXCommon.setData(bb, DataType.OCTET_STRING, systemTitle);
        return client.method(this, 8, bb.array(), DataType.OCTET_STRING);
    }

    /**
     * Removes X.509 v3 certificate from the server using serial number.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param serialNumber
     *            Serial number.
     * @param issuer
     *            Issuer.
     * @return Generated action.
     */
    public final byte[][] removeCertificateBySerial(final GXDLMSClient client,
            final byte[] serialNumber, final byte[] issuer) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(2);
        // Add enum
        bb.setUInt8(DataType.ENUM.getValue());
        bb.setUInt8(1);
        // Add certificate_identification_by_entity
        bb.setUInt8(DataType.STRUCTURE.getValue());
        bb.setUInt8(2);
        // serialNumber
        GXCommon.setData(bb, DataType.OCTET_STRING, serialNumber);
        // issuer
        GXCommon.setData(bb, DataType.OCTET_STRING, issuer);
        return client.method(this, 8, bb.array(), DataType.OCTET_STRING);
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 2) {
            for (Object tmp : (Object[]) e.getParameters()) {
                Object[] item = (Object[]) tmp;
                GlobalKeyType type =
                        GlobalKeyType.values()[((Number) item[0]).intValue()];
                byte[] data = (byte[]) item[1];
                switch (type) {
                case UNICAST_ENCRYPTION:
                case BROADCAST_ENCRYPTION:
                    // Invalid type
                    e.setError(ErrorCode.READ_WRITE_DENIED);
                case AUTHENTICATION:
                    // if settings.Cipher is null non secure server is used.
                    settings.getCipher().setAuthenticationKey(GXDLMSSecureClient
                            .decrypt(settings.getKek(), data));
                    break;
                case KEK:
                    settings.setKek(GXDLMSSecureClient
                            .decrypt(settings.getKek(), data));
                    break;
                default:
                    e.setError(ErrorCode.READ_WRITE_DENIED);
                }
            }
            // Return standard reply.
            return null;
        } else {
            return new byte[] { (byte) ErrorCode.READ_WRITE_DENIED.getValue() };
        }
    }

    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // SecurityPolicy
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        // SecuritySuite
        if (canRead(3)) {
            attributes.add(new Integer(3));
        }
        if (getVersion() > 0) {
            // ClientSystemTitle
            if (canRead(4)) {
                attributes.add(new Integer(4));
            }
            // ServerSystemTitle
            if (canRead(5)) {
                attributes.add(new Integer(5));
            }
            // Certificates
            if (canRead(6)) {
                attributes.add(new Integer(6));
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        if (getVersion() == 0) {
            return 5;
        }
        return 6;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        if (getVersion() == 0) {
            return 2;
        }
        return 8;
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
            return DataType.ENUM;
        }
        if (index == 4) {
            return DataType.OCTET_STRING;
        }
        if (index == 5) {
            return DataType.OCTET_STRING;
        }
        if (getVersion() > 0) {
            if (index == 6) {
                return DataType.OCTET_STRING;
            }
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        } else {
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /**
     * Get certificates as byte buffer.
     * 
     * @return
     */
    private byte[] getCertificatesByteArray() {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8((byte) DataType.ARRAY.getValue());
        GXCommon.setObjectCount(certificates.size(), bb);
        for (GXDLMSCertificateInfo it : certificates) {
            bb.setUInt8((byte) DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(6, bb);
            bb.setUInt8((byte) DataType.ENUM.getValue());
            bb.setUInt8((byte) it.getEntity().getValue());
            bb.setUInt8((byte) DataType.ENUM.getValue());
            bb.setUInt8((byte) it.getType().getValue());
            GXCommon.addString(it.getSerialNumber(), bb);
            GXCommon.addString(it.getIssuer(), bb);
            GXCommon.addString(it.getSubject(), bb);
            GXCommon.addString(it.getSubjectAltName(), bb);
        }
        return bb.array();
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return getLogicalName();
        }
        if (e.getIndex() == 2) {
            return new Integer(getSecurityPolicy().getValue());
        }
        if (e.getIndex() == 3) {
            return new Integer(getSecuritySuite().getValue());
        }
        if (e.getIndex() == 4) {
            return getClientSystemTitle();
        }
        if (e.getIndex() == 5) {
            return getServerSystemTitle();
        }
        if (e.getIndex() == 6) {
            return getCertificatesByteArray();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    private void updateSertificates(final Object[] list) {
        certificates.clear();
        for (Object tmp : list) {
            Object[] it = (Object[]) tmp;
            GXDLMSCertificateInfo info = new GXDLMSCertificateInfo();
            info.setEntity(
                    CertificateEntity.forValue(((Number) it[0]).intValue()));
            info.setType(CertificateType.forValue(((Number) it[1]).intValue()));
            info.setSerialNumber(new String((byte[]) it[2]));
            info.setIssuer(new String((byte[]) it[3]));
            info.setSubject(new String((byte[]) it[4]));
            info.setSubjectAltName(new String((byte[]) it[5]));
            certificates.add(info);
        }
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            super.setValue(settings, e);
        } else if (e.getIndex() == 2) {
            setSecurityPolicy(SecurityPolicy
                    .forValue(((Number) e.getValue()).byteValue()));
        } else if (e.getIndex() == 3) {
            setSecuritySuite(SecuritySuite
                    .forValue(((Number) e.getValue()).byteValue()));
        } else if (e.getIndex() == 4) {
            setClientSystemTitle((byte[]) e.getValue());
        } else if (e.getIndex() == 5) {
            setServerSystemTitle((byte[]) e.getValue());
        } else if (e.getIndex() == 6) {
            updateSertificates((Object[]) e.getValue());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }
}