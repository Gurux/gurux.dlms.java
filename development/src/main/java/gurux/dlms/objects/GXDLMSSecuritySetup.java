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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

// import javax.security.auth.x500.X500Principal;

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
import gurux.dlms.objects.enums.SecurityPolicy1;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.dlms.secure.GXDLMSSecureClient;
//CHECKSTYLE:OFF
//import sun.security.pkcs10.PKCS10;
//import sun.security.x509.X500Name;
//CHECKSTYLE:ON

@SuppressWarnings("restriction")
public class GXDLMSSecuritySetup extends GXDLMSObject implements IGXDLMSBase {
    public static final String DN_NAME = "CN=Test O=Gurux, L=Tampere, C=FI";
    private static final String SHA1WITHECDSA = "SHA1withECDSA";
    /**
     * Asymmetric key pair as required by the security suite.
     */
    private Hashtable<CertificateType, KeyPair> keys =
            new Hashtable<CertificateType, KeyPair>();
    /**
     * Security policy.
     */
    private int securityPolicy = 0;

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
        securityPolicy = 0;
        securitySuite = SecuritySuite.AES_GCM_128;
        certificates = new ArrayList<GXDLMSCertificateInfo>();
    }

    /**
     * @return Security policy for version 1.
     */
    public final Set<SecurityPolicy1> getSecurityPolicy1() {
        return SecurityPolicy1.forValue(securityPolicy);
    }

    /**
     * @param value
     *            Security policy for version 1.
     */
    public final void setSecurityPolicy1(final Set<SecurityPolicy1> value) {
        securityPolicy = SecurityPolicy1.toInteger(value);
    }

    /**
     * @return Security policy for version 0.
     */
    public final SecurityPolicy getSecurityPolicy() {
        return SecurityPolicy.values()[securityPolicy];
    }

    /**
     * @param value
     *            Security policy for version 0.
     */
    public final void setSecurityPolicy(final SecurityPolicy value) {
        switch (value) {
        case NOTHING:
        case AUTHENTICATED:
        case ENCRYPTED:
        case AUTHENTICATED_ENCRYPTED:
            securityPolicy = value.ordinal();
            break;
        default:
            throw new IllegalArgumentException();
        }
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

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), securityPolicy, securitySuite,
                clientSystemTitle, serverSystemTitle, certificates };
    }

    /**
     * Activates and strengthens the security policy for version 0 Security
     * Setup Object.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param security
     *            New security level.
     * @return Generated action.
     */
    public final byte[][] activate(final GXDLMSClient client,
            final SecurityPolicy security) {
        return client.method(this, 1, security.ordinal(), DataType.ENUM);
    }

    /**
     * Activates and strengthens the security policy for version 1 Security
     * Setup Object.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param security
     *            New security level.
     * @return Generated action.
     */
    public final byte[][] activate(final GXDLMSClient client,
            final Set<SecurityPolicy1> security) {
        return client.method(this, 1, SecurityPolicy1.toInteger(security),
                DataType.ENUM);
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
     * @param type
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
     * @param certificate
     *            X.509 v3 certificate.
     * @return Generated action.
     */
    public final byte[][] importCertificate(final GXDLMSClient client,
            final X509Certificate certificate) {
        try {
            return importCertificate(client, certificate.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
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
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(DataType.OCTET_STRING.getValue());
        GXCommon.setObjectCount(key.length, bb);
        bb.set(key);
        return client.method(this, 6, bb.array(), DataType.OCTET_STRING);
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
            final String serialNumber, final String issuer) {
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
        GXCommon.setData(bb, DataType.OCTET_STRING, serialNumber.getBytes());
        // issuer
        GXCommon.setData(bb, DataType.OCTET_STRING, issuer.getBytes());
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
            final String serialNumber, final String issuer) {
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
        GXCommon.setData(bb, DataType.OCTET_STRING, serialNumber.getBytes());
        // issuer
        GXCommon.setData(bb, DataType.OCTET_STRING, issuer.getBytes());
        return client.method(this, 8, bb.array(), DataType.OCTET_STRING);
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // Increase security level.
        if (e.getIndex() == 1) {
            Security security = settings.getCipher().getSecurity();
            if (getVersion() == 0) {
                SecurityPolicy policy = SecurityPolicy
                        .values()[((Number) e.getParameters()).byteValue()];
                setSecurityPolicy(policy);
                if (policy == SecurityPolicy.AUTHENTICATED) {
                    settings.getCipher().setSecurity(Security.AUTHENTICATION);
                } else if (policy == SecurityPolicy.ENCRYPTED) {
                    settings.getCipher().setSecurity(Security.ENCRYPTION);
                } else if (policy == SecurityPolicy.AUTHENTICATED_ENCRYPTED) {
                    settings.getCipher()
                            .setSecurity(Security.AUTHENTICATION_ENCRYPTION);
                }
            } else if (getVersion() == 1) {
                java.util.Set<SecurityPolicy1> policy = SecurityPolicy1
                        .forValue(((Number) e.getParameters()).byteValue());
                setSecurityPolicy1(policy);
                if (policy.contains(SecurityPolicy1.AUTHENTICATED_RESPONSE)) {
                    security = Security.forValue(security.getValue()
                            | Security.AUTHENTICATION.getValue());
                    settings.getCipher().setSecurity(security);
                }
                if (policy.contains(SecurityPolicy1.ENCRYPTED_RESPONSE)) {
                    security = Security.forValue(security.getValue()
                            | Security.ENCRYPTION.getValue());
                    settings.getCipher().setSecurity(security);
                }
            }
        } else if (e.getIndex() == 2) {
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
        } else if (e.getIndex() == 3) {
            // key_agreement
        } else if (e.getIndex() == 4) {
            // generate_key_pair
            CertificateType key = CertificateType
                    .forValue(((Number) e.getParameters()).intValue());
            try {
                KeyPairGenerator kpg =
                        KeyPairGenerator.getInstance("EC", "SunEC");
                KeyPair value = kpg.genKeyPair();
                keys.put(key, value);
            } catch (Exception e1) {
                e.setError(ErrorCode.HARDWARE_FAULT);
            }
        } else if (e.getIndex() == 5) {
            // generate_certificate_request
            try {
                // CertificateType key = CertificateType
                // .forValue(((Number) e.getParameters()).intValue());
                // return generatePKCS10(keys.get(key), "Test", "dev-Gurux",
                // "Gurux", "Tampere", "", "FI");
            } catch (Exception e1) {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            }
        } else if (e.getIndex() == 6) {
            // import_certificate
            try {
                CertificateFactory certFactory =
                        CertificateFactory.getInstance("X.509");
                InputStream in =
                        new ByteArrayInputStream((byte[]) e.getParameters());
                X509Certificate cert =
                        (X509Certificate) certFactory.generateCertificate(in);
                settings.getCertificates().add(cert);
            } catch (CertificateException e1) {
                // Invalid type
                e.setError(ErrorCode.READ_WRITE_DENIED);
            }
        } else if (e.getIndex() == 7) {
            // TODO: export_certificate
        } else if (e.getIndex() == 8) {
            // TODO: remove_certificate
        } else {
            // Invalid type
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        // Return standard reply.
        return null;
    }

    /*
     * @param cn Common Name, is X.509 speak for the name that distinguishes the
     * Certificate best, and ties it to your Organization
     * @param ou Organizational unit
     * @param o Organization NAME
     * @param l Location
     * @param S State
     * @param C Country
     * @return
     * @throws Exception
     */
    // private static byte[] generatePKCS10(final KeyPair kp, final String cn,
    // final String ou, final String o, final String l, final String s,
    // final String c) throws Exception {
    // // generate PKCS10 certificate request
    // PKCS10 pkcs10 = new PKCS10(kp.getPublic());
    // Signature signature = Signature.getInstance(SHA1WITHECDSA);
    // signature.initSign(kp.getPrivate());
    // // common, orgUnit, org, locality, state, country
    // X500Principal principal = new X500Principal("CN=" + cn + ", OU=" + ou
    // + ", O=" + o + ", L=" + l + ", C=" + c);
    // X500Name x500name = null;
    // x500name = new X500Name(principal.getEncoded());
    // pkcs10.encodeAndSign(x500name, signature);
    // byte[] ret = pkcs10.getEncoded();
    // return ret;
    // }

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
                return DataType.ARRAY;
            }
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        } else {
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Get certificates as byte buffer.
     */
    private byte[] getCertificatesByteArray(final GXDLMSSettings settings) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8((byte) DataType.ARRAY.getValue());
        GXCommon.setObjectCount(settings.getCertificates().size(), bb);
        for (X509Certificate it : settings.getCertificates()) {
            bb.setUInt8((byte) DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(6, bb);
            bb.setUInt8((byte) DataType.ENUM.getValue());
            // TODO: certificate_entity: enum:
            bb.setUInt8((byte) CertificateEntity.SERVER.getValue());
            bb.setUInt8((byte) DataType.ENUM.getValue());
            // TODO: digital signature
            bb.setUInt8((byte) CertificateType.DIGITAL_SIGNATURE.getValue());
            GXCommon.addString(it.getSerialNumber().toString(), bb);
            GXCommon.addString(it.getIssuerDN().getName(), bb);
            GXCommon.addString(it.getSubjectDN().getName(), bb);
            try {
                if (it.getSubjectAlternativeNames() != null
                        && !it.getSubjectAlternativeNames().isEmpty()) {
                    GXCommon.addString(it.getSubjectAlternativeNames()
                            .toArray(null).toString(), bb);
                } else {
                    GXCommon.addString("", bb);
                }
            } catch (CertificateParsingException e) {
                GXCommon.addString("", bb);
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
        if (e.getIndex() == 1) {
            return getLogicalName();
        }
        if (e.getIndex() == 2) {
            return new Integer(securityPolicy);
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
            return getCertificatesByteArray(settings);
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
            // Security level is set with action.
            if (settings.isServer()) {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            } else {
                this.securityPolicy = (Short) e.getValue();
            }
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