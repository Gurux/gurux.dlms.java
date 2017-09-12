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

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.KeyAgreement;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.asn.GXAsn1BitString;
import gurux.dlms.asn.GXAsn1Converter;
import gurux.dlms.asn.GXAsn1Sequence;
import gurux.dlms.asn.GXPkcs10;
import gurux.dlms.asn.GXx509Certificate;
import gurux.dlms.asn.enums.KeyUsage;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.CertificateEntity;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.GlobalKeyType;
import gurux.dlms.objects.enums.SecurityPolicy;
import gurux.dlms.objects.enums.SecurityPolicy0;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.dlms.secure.GXASymmetric;
import gurux.dlms.secure.GXDLMSSecureClient;

public class GXDLMSSecuritySetup extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Security policy version 1.
     */
    private Set<SecurityPolicy> securityPolicy = new HashSet<SecurityPolicy>();
    /**
     * Security policy version 0.
     */
    private SecurityPolicy0 securityPolicy0 = SecurityPolicy0.NOTHING;

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
        securitySuite = SecuritySuite.AES_GCM_128;
        certificates = new ArrayList<GXDLMSCertificateInfo>();
    }

    /**
     * @return Security policy for version 1.
     */
    public final Set<SecurityPolicy> getSecurityPolicy() {
        return securityPolicy;
    }

    /**
     * @param value
     *            Security policy for version 1.
     */
    public final void setSecurityPolicy(final Set<SecurityPolicy> value) {
        securityPolicy = value;
    }

    /**
     * @return Security policy for version 0.
     */
    public final SecurityPolicy0 getSecurityPolicy0() {
        return securityPolicy0;
    }

    /**
     * @param value
     *            Security policy for version 0.
     */
    public final void setSecurityPolicy0(final SecurityPolicy0 value) {
        switch (value) {
        case NOTHING:
        case AUTHENTICATED:
        case ENCRYPTED:
        case AUTHENTICATED_ENCRYPTED:
            securityPolicy0 = value;
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
            final SecurityPolicy0 security) {
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
            final Set<SecurityPolicy> security) {
        return client.method(this, 1, SecurityPolicy.toInteger(security),
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
     * Agree on one or more symmetric keys using the key agreement algorithm.
     * 
     * @param client
     *            DLMS client that is used to generate action.
     * @param type
     *            Global key type.
     * @return Generated action.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidKeyException
     *             Invalid key.
     * @throws SignatureException
     *             Invalid signature.
     */
    public final byte[][] keyAgreement(final GXDLMSSecureClient client,
            final GlobalKeyType type) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        GXByteBuffer bb = new GXByteBuffer();
        byte[] data = getEphemeralPublicKeyData(type.ordinal(),
                client.getCiphering().getEphemeralKeyPair().getPublic());
        bb.set(data, 1, 64);
        System.out.println("Signin public key: " + client.getCiphering()
                .getSigningKeyPair().getPublic().toString());

        // Add signature.
        byte[] sign =
                GXASymmetric.getEphemeralPublicKeySignature(type.ordinal(),
                        client.getCiphering().getEphemeralKeyPair().getPublic(),
                        client.getCiphering().getSigningKeyPair().getPrivate());
        bb.set(sign);
        System.out.println("Data: " + GXCommon.toHex(data));
        System.out.println("Sign: " + GXCommon.toHex(sign));

        List<GXSimpleEntry<GlobalKeyType, byte[]>> list =
                new ArrayList<GXSimpleEntry<GlobalKeyType, byte[]>>();
        list.add(new GXSimpleEntry<GlobalKeyType, byte[]>(type, bb.array()));
        return keyAgreement(client, list);
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
            final GXx509Certificate certificate) {
        return importCertificate(client, certificate.getEncoded());
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
    public final byte[][] exportCertificateByEntity(
            final GXDLMSSecureClient client, final CertificateEntity entity,
            final CertificateType type, final byte[] systemTitle) {
        if (systemTitle == null || systemTitle.length == 0) {
            throw new IllegalArgumentException("Invalid system title.");
        }
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
        return client.method(this, 7, bb.array(), DataType.STRUCTURE);
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
    public final byte[][] exportCertificateBySerial(
            final GXDLMSSecureClient client, final String serialNumber,
            final String issuer) {
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
        return client.method(this, 7, bb.array(), DataType.STRUCTURE);
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
    public final byte[][] removeCertificateByEntity(
            final GXDLMSSecureClient client, final CertificateEntity entity,
            final CertificateType type, final byte[] systemTitle) {
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
        return client.method(this, 8, bb.array(), DataType.STRUCTURE);
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
    public final byte[][] removeCertificateBySerial(
            final GXDLMSSecureClient client, final String serialNumber,
            final String issuer) {
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
        return client.method(this, 8, bb.array(), DataType.STRUCTURE);
    }

    /**
     * Convert system title to subject.
     * 
     * @param systemTitle
     *            System title.
     * @return Subject.
     */
    public static String systemTitleToSubject(final byte[] systemTitle) {
        GXByteBuffer bb = new GXByteBuffer(systemTitle);
        bb.getData()[0] = 0;
        bb.getData()[1] = 0;
        bb.getData()[2] = 0;
        String subject = "CN=" + new String(systemTitle, 0, 3);
        subject += String.valueOf(bb.getUInt64());
        return subject;
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // Increase security level.
        if (e.getIndex() == 1) {
            Security security = settings.getCipher().getSecurity();
            if (getVersion() == 0) {
                SecurityPolicy0 policy = SecurityPolicy0
                        .values()[((Number) e.getParameters()).byteValue()];
                setSecurityPolicy0(policy);
                if (policy == SecurityPolicy0.AUTHENTICATED) {
                    settings.getCipher().setSecurity(Security.AUTHENTICATION);
                } else if (policy == SecurityPolicy0.ENCRYPTED) {
                    settings.getCipher().setSecurity(Security.ENCRYPTION);
                } else if (policy == SecurityPolicy0.AUTHENTICATED_ENCRYPTED) {
                    settings.getCipher()
                            .setSecurity(Security.AUTHENTICATION_ENCRYPTION);
                }
            } else if (getVersion() == 1) {
                java.util.Set<SecurityPolicy> policy = SecurityPolicy
                        .forValue(((Number) e.getParameters()).byteValue());
                setSecurityPolicy(policy);
                if (policy.contains(SecurityPolicy.AUTHENTICATED_RESPONSE)) {
                    security = Security.forValue(security.getValue()
                            | Security.AUTHENTICATION.getValue());
                    settings.getCipher().setSecurity(security);
                }
                if (policy.contains(SecurityPolicy.ENCRYPTED_RESPONSE)) {
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
            try {
                Object[] tmp = (Object[]) ((Object[]) e.getParameters())[0];
                short keyId = (short) tmp[0];
                if (keyId != 0) {
                    e.setError(ErrorCode.READ_WRITE_DENIED);
                } else {
                    byte[] data = (byte[]) tmp[1];
                    // ephemeral public key
                    GXByteBuffer data2 = new GXByteBuffer(65);
                    data2.setUInt8(keyId);
                    data2.set(data, 0, 64);
                    GXByteBuffer sign = new GXByteBuffer();
                    sign.set(data, 64, 64);
                    PublicKey pk = null;
                    String subject = systemTitleToSubject(
                            settings.getSourceSystemTitle());
                    for (GXx509Certificate it : settings.getCipher()
                            .getCertificates()) {
                        if (it.getKeyUsage()
                                .contains(KeyUsage.DIGITAL_SIGNATURE)
                                && it.getSubject().equals(subject)) {
                            pk = it.getPublicKey();
                            break;
                        }
                    }
                    if (pk == null || !GXASymmetric
                            .validateEphemeralPublicKeySignature(data2.array(),
                                    sign.array(), pk)) {
                        e.setError(ErrorCode.READ_WRITE_DENIED);
                        settings.setTargetEphemeralKey(null);
                    } else {
                        settings.setTargetEphemeralKey(GXAsn1Converter
                                .getPublicKey(data2.subArray(1, 64)));
                        // Generate ephemeral keys.
                        KeyPair eKpS =
                                settings.getCipher().getEphemeralKeyPair();
                        if (eKpS == null) {
                            eKpS = GXAsn1Converter.generateKeyPair();
                            settings.getCipher().setEphemeralKeyPair(eKpS);
                        }
                        // Generate shared secret.
                        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
                        ka.init(eKpS.getPrivate());
                        ka.doPhase(settings.getTargetEphemeralKey(), true);
                        byte[] sharedSecret = ka.generateSecret();
                        settings.getCipher().setSharedSecret(sharedSecret);
                        System.out.println("Server's shared secret: "
                                + GXCommon.toHex(sharedSecret));
                        GXByteBuffer bb = new GXByteBuffer();
                        data = getEphemeralPublicKeyData(keyId,
                                eKpS.getPublic());
                        bb.set(data, 1, 64);
                        // Add signature.
                        byte[] tmp2 =
                                GXASymmetric.getEphemeralPublicKeySignature(
                                        keyId, eKpS.getPublic(),
                                        settings.getCipher().getSigningKeyPair()
                                                .getPrivate());
                        bb.set(tmp2);
                        System.out.println("Data: " + GXCommon.toHex(data));
                        System.out.println("Sign: " + GXCommon.toHex(tmp2));
                        return bb.array();
                    }
                }
            } catch (Exception ex) {
                e.setError(ErrorCode.HARDWARE_FAULT);
            }
        } else if (e.getIndex() == 4) {
            // generate_key_pair
            CertificateType key = CertificateType
                    .forValue(((Number) e.getParameters()).intValue());
            try {
                KeyPair value = GXAsn1Converter.generateKeyPair();
                switch (key) {
                case DIGITAL_SIGNATURE:
                    settings.getCipher().setSigningKeyPair(value);
                    break;
                case KEY_AGREEMENT:
                    settings.getCipher().setKeyAgreementKeyPair(value);
                    break;
                default:
                    e.setError(ErrorCode.READ_WRITE_DENIED);
                }
            } catch (Exception e1) {
                e.setError(ErrorCode.HARDWARE_FAULT);
            }
        } else if (e.getIndex() == 5) {
            // generate_certificate_request
            CertificateType key = CertificateType
                    .forValue(((Number) e.getParameters()).intValue());
            try {
                KeyPair kp = null;
                switch (key) {
                case DIGITAL_SIGNATURE:
                    kp = settings.getCipher().getSigningKeyPair();
                    break;
                case KEY_AGREEMENT:
                    kp = settings.getCipher().getKeyAgreementKeyPair();
                    break;
                default:
                    break;
                }
                if (kp == null) {
                    e.setError(ErrorCode.READ_WRITE_DENIED);
                } else {
                    GXPkcs10 pkc10 = GXPkcs10.createCertificateSigningRequest(
                            kp, systemTitleToSubject(
                                    settings.getCipher().getSystemTitle()));
                    return pkc10.getEncoded();
                }
            } catch (Exception e1) {
                e.setError(ErrorCode.HARDWARE_FAULT);
            }
        } else if (e.getIndex() == 6) {
            // import_certificate
            GXx509Certificate cert =
                    new GXx509Certificate((byte[]) e.getParameters());
            if (cert.getKeyUsage().isEmpty()) {
                // At least one bit must be used.
                e.setError(ErrorCode.READ_WRITE_DENIED);
            } else {
                settings.getCipher().getCertificates().add(cert);
            }
        } else if (e.getIndex() == 7) {
            // export_certificate
            Object[] tmp = ((Object[]) e.getParameters());
            short type = (short) tmp[0];
            GXx509Certificate cert = null;
            synchronized (settings.getCipher().getCertificates()) {
                if (type == 0) {
                    tmp = (Object[]) tmp[1];
                    cert = findCertificateByEntity(settings,
                            CertificateEntity.forValue((short) tmp[0]),
                            CertificateType.forValue((short) tmp[1]),
                            (byte[]) tmp[2]);
                } else if (type == 1) {
                    tmp = (Object[]) tmp[1];
                    cert = findCertificateBySerial(settings, (byte[]) tmp[1],
                            new String((byte[]) tmp[2]));
                }
                if (cert == null) {
                    e.setError(ErrorCode.HARDWARE_FAULT);
                } else {
                    return cert.getEncoded();
                }
            }
        } else if (e.getIndex() == 8) {
            // remove_certificate
            Object[] tmp = (Object[]) ((Object[]) e.getParameters())[0];
            short type = (short) tmp[0];
            GXx509Certificate cert = null;
            synchronized (settings.getCipher().getCertificates()) {
                if (type == 0) {
                    cert = findCertificateByEntity(settings,
                            CertificateEntity.forValue((int) tmp[1]),
                            CertificateType.forValue((int) tmp[2]),
                            (byte[]) tmp[3]);
                } else if (type == 1) {
                    cert = findCertificateBySerial(settings, (byte[]) tmp[1],
                            new String((byte[]) tmp[2]));
                }
                if (cert == null) {
                    e.setError(ErrorCode.HARDWARE_FAULT);
                } else {
                    settings.getCipher().getCertificates().remove(cert);
                }
            }
        } else {
            // Invalid type
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        // Return standard reply.
        return null;
    }

    private static KeyUsage
            certificateTypeToKeyUsage(final CertificateType type) {
        KeyUsage k = KeyUsage.NONE;
        switch (type) {
        case DIGITAL_SIGNATURE:
            k = KeyUsage.DIGITAL_SIGNATURE;
            break;
        case KEY_AGREEMENT:
            k = KeyUsage.KEY_AGREEMENT;
            break;
        case TLS:
            k = KeyUsage.KEY_CERT_SIGN;
            break;
        case OTHER:
            k = KeyUsage.CRL_SIGN;
            break;
        default:
            // At least one bit must be used.
            return null;
        }
        return k;
    }

    /**
     * Find certificate using entity information.
     * 
     * @param settings
     *            DLMS Settings.
     * @param entity
     *            Certificate entity type.
     * @param type
     *            Certificate type.
     * @param systemtitle
     *            System title.
     * @return
     */
    private static GXx509Certificate findCertificateByEntity(
            final GXDLMSSettings settings, final CertificateEntity entity,
            final CertificateType type, final byte[] systemtitle) {
        String subject = systemTitleToSubject(systemtitle);
        KeyUsage k = certificateTypeToKeyUsage(type);
        for (GXx509Certificate it : settings.getCipher().getCertificates()) {
            if (it.getKeyUsage().contains(k)
                    && it.getSubject().equalsIgnoreCase(subject)) {
                return it;
            }
        }
        return null;
    }

    /**
     * Find certificate using serial information.
     * 
     * @param settings
     *            DLMS Settings.
     * @param serialNumber
     *            Serial number.
     * @param issuer
     *            Issuer.
     * @return
     */
    private static GXx509Certificate findCertificateBySerial(
            final GXDLMSSettings settings, final byte[] serialNumber,
            final String issuer) {
        for (GXx509Certificate it : settings.getCipher().getCertificates()) {
            if (Arrays.equals(it.getSerialNumber().getByteArray(), serialNumber)
                    && it.getIssuer().equalsIgnoreCase(issuer)) {
                return it;
            }
        }
        return null;
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
        GXCommon.setObjectCount(settings.getCipher().getCertificates().size(),
                bb);
        for (GXx509Certificate it : settings.getCipher().getCertificates()) {
            bb.setUInt8((byte) DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(6, bb);
            bb.setUInt8((byte) DataType.ENUM.getValue());
            // TODO: certificate_entity: enum:
            bb.setUInt8((byte) CertificateEntity.SERVER.getValue());
            bb.setUInt8((byte) DataType.ENUM.getValue());
            // TODO: digital signature
            bb.setUInt8((byte) CertificateType.DIGITAL_SIGNATURE.getValue());
            GXCommon.addString(it.getSerialNumber().toString(), bb);
            GXCommon.addString(it.getIssuer(), bb);
            GXCommon.addString(it.getSubject(), bb);
            GXCommon.addString("", bb);
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
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            if (getVersion() == 0) {
                return new Integer(securityPolicy0.ordinal());
            }
            return new Integer(SecurityPolicy.toInteger(securityPolicy));
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
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            // Security level is set with action.
            if (settings.isServer()) {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            } else {
                if (getVersion() == 0) {
                    securityPolicy0 =
                            SecurityPolicy0.values()[(Short) e.getValue()];
                } else {
                    securityPolicy =
                            SecurityPolicy.forValue((Short) e.getValue());
                }
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

    /**
     * Get Ephemeral Public Key Signature.
     * 
     * @param keyId
     *            Key ID.
     * @param ephemeralKey
     *            Ephemeral key.
     * @return Ephemeral Public Key Signature.
     */
    public static byte[] getEphemeralPublicKeyData(final int keyId,
            final PublicKey ephemeralKey) {
        GXAsn1BitString tmp =
                (GXAsn1BitString) ((GXAsn1Sequence) GXAsn1Converter
                        .fromByteArray(ephemeralKey.getEncoded())).get(1);

        // Ephemeral public key client
        GXByteBuffer epk = new GXByteBuffer(tmp.getValue());
        // First byte is 4 in Java and that is not used. We can override it.
        epk.getData()[0] = (byte) keyId;
        return epk.array();
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        securityPolicy = SecurityPolicy
                .forValue(reader.readElementContentAsInt("SecurityPolicy"));
        securityPolicy0 = SecurityPolicy0.values()[reader
                .readElementContentAsInt("SecurityPolicy0")];
        securitySuite = SecuritySuite.values()[reader
                .readElementContentAsInt("SecuritySuite")];
        String str = reader.readElementContentAsString("ClientSystemTitle");
        if (str == null) {
            clientSystemTitle = null;
        } else {
            clientSystemTitle = GXDLMSTranslator.hexToBytes(str);
        }
        str = reader.readElementContentAsString("ServerSystemTitle");
        if (str == null) {
            serverSystemTitle = null;
        } else {
            serverSystemTitle = GXDLMSTranslator.hexToBytes(str);
        }
        certificates.clear();
        if (reader.isStartElement("Certificates", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSCertificateInfo it = new GXDLMSCertificateInfo();
                certificates.add(it);
                it.setEntity(CertificateEntity
                        .forValue(reader.readElementContentAsInt("Entity")));
                it.setType(CertificateType
                        .forValue(reader.readElementContentAsInt("Type")));
                it.setSerialNumber(
                        reader.readElementContentAsString("SerialNumber"));
                it.setIssuer(reader.readElementContentAsString("Issuer"));
                it.setSubject(reader.readElementContentAsString("Subject"));
                it.setSubjectAltName(
                        reader.readElementContentAsString("SubjectAltName"));
            }
            reader.readEndElement("Certificates");
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("SecurityPolicy",
                SecurityPolicy.toInteger(securityPolicy));
        writer.writeElementString("SecurityPolicy0", securityPolicy0.ordinal());
        writer.writeElementString("SecuritySuite", securitySuite.ordinal());
        writer.writeElementString("ClientSystemTitle",
                GXDLMSTranslator.toHex(clientSystemTitle));
        writer.writeElementString("ServerSystemTitle",
                GXDLMSTranslator.toHex(serverSystemTitle));
        if (certificates != null) {
            writer.writeStartElement("Certificates");
            for (GXDLMSCertificateInfo it : certificates) {
                writer.writeStartElement("Item");
                writer.writeElementString("Entity", it.getEntity().getValue());
                writer.writeElementString("Type", it.getType().getValue());
                writer.writeElementString("SerialNumber", it.getSerialNumber());
                writer.writeElementString("Issuer", it.getIssuer());
                writer.writeElementString("Subject", it.getSubject());
                writer.writeElementString("SubjectAltName",
                        it.getSubjectAltName());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}