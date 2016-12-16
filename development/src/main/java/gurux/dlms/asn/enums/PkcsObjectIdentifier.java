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

package gurux.dlms.asn.enums;

public enum PkcsObjectIdentifier implements GXOid {
    /**
     * pkcs-1 OBJECT IDENTIFIER ::= { iso(1) member-body(2) us(840)
     * rsadsi(113549) pkcs(1) 1 }
     */
    /**
     * Rsa encryption.
     */
    RsaEncryption("1.2.840.113549.1.1.1"),
    /**
     * 
     */
    MD2WithRsaEncryption("1.2.840.113549.1.1.2"),
    /**
     * 
     */
    MD4WithRsaEncryption("1.2.840.113549.1.1.3"),
    /**
     * 
     */
    MD5WithRsaEncryption("1.2.840.113549.1.1.4"),
    /**
     * 
     */
    Sha1WithRsaEncryption("1.2.840.113549.1.1.5"),
    /**
     * 
     */
    SrsaOaepEncryptionSet("1.2.840.113549.1.1.6"),
    /**
     * 
     */
    IdRsaesOaep("1.2.840.113549.1.1.7"),
    /**
     * 
     */
    IdMgf1("1.2.840.113549.1.1.8"),
    /**
     * 
     */
    IdPSpecified("1.2.840.113549.1.1.9"),
    /**
     * 
     */
    IdRsassaPss("1.2.840.113549.1.1.10"),
    /**
     * 
     */
    Sha256WithRsaEncryption("1.2.840.113549.1.1.11"),
    /**
     * 
     */
    Sha384WithRsaEncryption("1.2.840.113549.1.1.12"),
    /**
     * 
     */
    Sha512WithRsaEncryption("1.2.840.113549.1.1.13"),
    /**
     * 
     */
    Sha224WithRsaEncryption("1.2.840.113549.1.1.14"),
    //
    // pkcs-3 OBJECT IDENTIFIER ::= {
    // iso(1) member-body(2) us(840) rsadsi(113549) pkcs(1) 3 }
    //

    /**
     * 
     */
    DhKeyAgree1ment("1.2.840.113549.1.3.1"),
    //
    // pkcs-5 OBJECT IDENTIFIER ::= {
    // iso(1) member-body(2) us(840) rsadsi(113549) pkcs(1) 5 }
    //

    /**
     * 
     */
    PbeWithMD2AndDesCbc("1.2.840.113549.1.5.1"),
    /**
     * 
     */
    PbeWithMD2AndRC2Cbc("1.2.840.113549.1.5.4"),
    /**
     * 
     */
    PbeWithMD5AndDesCbc("1.2.840.113549.1.5.3"),
    /**
     * 
     */
    PbeWithMD5AndRC2Cbc("1.2.840.113549.1.5.6"),
    /**
     * 
     */
    PbeWithSha1AndDesCbc("1.2.840.113549.1.5.10"),
    /**
     * 
     */
    PbeWithSha1AndRC2Cbc("1.2.840.113549.1.5.11"),
    /**
     * 
     */
    IdPbeS2("1.2.840.113549.1.5.13"),
    /**
     * 
     */
    IdPbkdf2("1.2.840.113549.1.5.12"),

    //
    // encryptionAlgorithm OBJECT IDENTIFIER ::= {
    // iso(1) member-body(2) us(840) rsadsi(113549) 3 }
    //
    DesEde3Cbc("1.2.840.113549.3.7"), RC2Cbc("1.2.840.113549.3.2"),

    // md2 OBJECT IDENTIFIER ::=
    // {iso(1) member-body(2) US(840) rsadsi(113549) DigestAlgorithm(2) 2}
    //
    MD2("1.2.840.113549.2.2"),

    //
    // md4 OBJECT IDENTIFIER ::=
    // {iso(1) member-body(2) US(840) rsadsi(113549) DigestAlgorithm(2) 4}
    //
    MD4("1.2.840.113549.2.4"),

    //
    // md5 OBJECT IDENTIFIER ::=
    // {iso(1) member-body(2) US(840) rsadsi(113549) DigestAlgorithm(2) 5}
    //
    MD5("1.2.840.113549.2.5"),

    IdHmacWithSha1("1.2.840.113549.2.7"),
    IdHmacWithSha224("1.2.840.113549.2.8"),
    IdHmacWithSha256("1.2.840.113549.2.9"),
    IdHmacWithSha384("1.2.840.113549.2.10"),
    IdHmacWithSha512("1.2.840.113549.2.11"),

    //
    // pkcs-7 OBJECT IDENTIFIER ::= {
    // iso(1) member-body(2) us(840) rsadsi(113549) pkcs(1) 7 }
    //

    Data("1.2.840.113549.1.7.1"), SignedData("1.2.840.113549.1.7.2"),
    EnvelopedData("1.2.840.113549.1.7.3"),
    SignedAndEnvelopedData("1.2.840.113549.1.7.4"),
    DigestedData("1.2.840.113549.1.7.5"), EncryptedData("1.2.840.113549.1.7.6"),

    //
    // pkcs-9 OBJECT IDENTIFIER ::= {
    // iso(1) member-body(2) us(840) rsadsi(113549) pkcs(1) 9 }
    //

    Pkcs9AtEmailAddress("1.2.840.113549.1.9.1"),
    Pkcs9AtUnstructuredName("1.2.840.113549.1.9.2"),
    Pkcs9AtContentType("1.2.840.113549.1.9.3"),
    Pkcs9AtMessageDigest("1.2.840.113549.1.9.4"),
    Pkcs9AtSigningTime("1.2.840.113549.1.9.5"),
    Pkcs9AtCounterSignature("1.2.840.113549.1.9.6"),
    Pkcs9AtChallengePassword("1.2.840.113549.1.9.7"),
    Pkcs9AtUnstructuredAddress("1.2.840.113549.1.9.8"),
    Pkcs9AtExtendedCertificateAttributes("1.2.840.113549.1.9.9"),
    Pkcs9AtSigningDescription("1.2.840.113549.1.9.13"),
    Pkcs9AtExtensionRequest("1.2.840.113549.1.9.14"),
    Pkcs9AtSmimeCapabilities("1.2.840.113549.1.9.15"),
    IdSmime("1.2.840.113549.1.9.16"),

    Pkcs9AtFriendlyName("1.2.840.113549.1.9.20"),
    Pkcs9AtLocalKeyID("1.2.840.113549.1.9.21"),

    X509CertType("1.2.840.113549.1.9.22.1"),

    X509Certificate("1.2.840.113549.1.9.22.1"),
    SdsiCertificate("1.2.840.113549.1.9.22.2"),

    X509Crl("1.2.840.113549.1.9.23.1"),

    IdAlg(IdSmime + ".3"),

    IdAlgEsdh(IdAlg + "5"), IdAlgCms3DesWrap(IdAlg + ".6"),
    IdAlgCmsRC2Wrap(IdAlg + "7"), IdAlgPwriKek(IdAlg + ".9"),
    IdAlgSsdh(IdAlg + ".10"),

    /*
     * <pre> -- RSA-KEM Key Transport Algorithm id-rsa-kem OID ::= { iso(1)
     * member-body(2) us(840) rsadsi(113549) pkcs(1) pkcs-9(9) smime(16) alg(3)
     * 14 } </pre>
     */
    IdRsaKem(IdAlg + ".14"),

    //
    // SMIME capability sub oids.
    //
    PreferSignedData(Pkcs9AtSmimeCapabilities + ".1"),
    CannotDecryptAny(Pkcs9AtSmimeCapabilities + ".2"),
    SmimeCapabilitiesVersions(Pkcs9AtSmimeCapabilities + ".3"),

    //
    // other SMIME attributes
    //
    IdAAReceiptRequest(IdSmime + ".2.1"),

    //
    // id-ct OBJECT IDENTIFIER ::= {iso(1) member-body(2) usa(840)
    // rsadsi(113549) pkcs(1) pkcs-9(9) smime(16) ct(1)}
    //
    IdCTAuthData("1.2.840.113549.1.9.16.1.2"),
    IdCTTstInfo("1.2.840.113549.1.9.16.1.4"),
    IdCTCompressedData("1.2.840.113549.1.9.16.1.9"),
    IdCTAuthEnvelopedData("1.2.840.113549.1.9.16.1.23"),
    IdCTTimestampedData("1.2.840.113549.1.9.16.1.31"),

    //
    // id-cti OBJECT IDENTIFIER ::= {iso(1) member-body(2) usa(840)
    // rsadsi(113549) pkcs(1) pkcs-9(9) smime(16) cti(6)}
    //
    IdCtiEtsProofOfOrigin("1.2.840.113549.1.9.16.6.1"),
    IdCtiEtsProofOfReceipt("1.2.840.113549.1.9.16.6.2"),
    IdCtiEtsProofOfDelivery("1.2.840.113549.1.9.16.6.3"),
    IdCtiEtsProofOfSender("1.2.840.113549.1.9.16.6.4"),
    IdCtiEtsProofOfApproval("1.2.840.113549.1.9.16.6.5"),
    IdCtiEtsProofOfCreation("1.2.840.113549.1.9.16.6.6"),

    //
    // id-aa OBJECT IDENTIFIER ::= {iso(1) member-body(2) usa(840)
    // rsadsi(113549) pkcs(1) pkcs-9(9) smime(16) attributes(2)}
    //
    IdAAContentHint("1.2.840.113549.1.9.16.2.4"), // See RFC 2634
    IdAAMsgSigDigest("1.2.840.113549.1.9.16.2.5"),
    IdAAContentReference("1.2.840.113549.1.9.16.2.10"),

    /*
     * id-aa-encrypKeyPref OBJECT IDENTIFIER ::= {id-aa 11}
     */
    IdAAEncrypKeyPref("1.2.840.113549.1.9.16.2.11"),
    IdAASigningCertificate("1.2.840.113549.1.9.16.2.12"),
    IdAASigningCertificateV2("1.2.840.113549.1.9.16.2.47"),

    IdAAContentIdentifier("1.2.840.113549.1.9.16.2.7"), // See RFC 2634

    /*
     * RFC 3126
     */
    IdAASignatureTimeStampToken("1.2.840.113549.1.9.16.2.14"),

    IdAAEtsSigPolicyID("1.2.840.113549.1.9.16.2.15"),
    IdAAEtsCommitmentType("1.2.840.113549.1.9.16.2.16"),
    IdAAEtsSignerLocation("1.2.840.113549.1.9.16.2.17"),
    IdAAEtsSignerAttr("1.2.840.113549.1.9.16.2.18"),
    IdAAEtsOtherSigCert("1.2.840.113549.1.9.16.2.19"),
    IdAAEtsContentTimestamp("1.2.840.113549.1.9.16.2.20"),
    IdAAEtsCertificateRefs("1.2.840.113549.1.9.16.2.21"),
    IdAAEtsRevocationRefs("1.2.840.113549.1.9.16.2.22"),
    IdAAEtsCertValues("1.2.840.113549.1.9.16.2.23"),
    IdAAEtsRevocationValues("1.2.840.113549.1.9.16.2.24"),
    IdAAEtsEscTimeStamp("1.2.840.113549.1.9.16.2.25"),
    IdAAEtsCertCrlTimestamp("1.2.840.113549.1.9.16.2.26"),
    IdAAEtsArchiveTimestamp("1.2.840.113549.1.9.16.2.27"),
    //
    // id-spq OBJECT IDENTIFIER ::= {iso(1) member-body(2) usa(840)
    // rsadsi(113549) pkcs(1) pkcs-9(9) smime(16) id-spq(5)}
    //

    IdSpqEtsUri("1.2.840.113549.1.9.16.5.1"),
    IdSpqEtsUNotice("1.2.840.113549.1.9.16.5.2"),

    //
    // pkcs-12 OBJECT IDENTIFIER ::= {
    // iso(1) member-body(2) us(840) rsadsi(113549) pkcs(1) 12 }
    //

    KeyBag("1.2.840.113549.1.12.10.1.1"),
    Pkcs8ShroudedKeyBag("1.2.840.113549.1.12.10.1.2"),
    CertBag("1.2.840.113549.1.12.10.1.3"), CrlBag("1.2.840.113549.1.12.10.1.4"),
    SecretBag("1.2.840.113549.1.12.10.1.5"),
    SafeContentsBag("1.2.840.113549.1.12.10.1.6"),

    PbeWithShaAnd128BitRC4("1.2.840.113549.1.12.1.1"),
    PbeWithShaAnd40BitRC4("1.2.840.113549.1.12.1.2"),
    PbeWithShaAnd3KeyTripleDesCbc("1.2.840.113549.1.12.1.3"),
    PbeWithShaAnd2KeyTripleDesCbc("1.2.840.113549.1.12.1.4"),
    PbeWithShaAnd128BitRC2Cbc("1.2.840.113549.1.12.1.5"),
    PbewithShaAnd40BitRC2Cbc("1.2.840.113549.1.12.1.6");

    private String value;
    private static java.util.HashMap<String, PkcsObjectIdentifier> mappings;

    private static java.util.HashMap<String, PkcsObjectIdentifier>
            getMappings() {
        synchronized (PkcsObjectIdentifier.class) {
            if (mappings == null) {
                mappings =
                        new java.util.HashMap<String, PkcsObjectIdentifier>();
            }
        }
        return mappings;
    }

    PkcsObjectIdentifier(final String mode) {
        value = mode;
        getMappings().put(new String(mode), this);
    }

    /*
     * Get string value for enum.
     */
    public final String getValue() {
        return value;
    }

    /*
     * Convert string for enum value.
     */
    public static PkcsObjectIdentifier forValue(final String value) {
        return getMappings().get(value);
    }
}
