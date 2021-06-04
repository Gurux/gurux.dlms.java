//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.asn.enums;
/** 
	 Public-Key Cryptography Standards (PKCS) type.
*/
public enum PkcsType
{
	/** 
	 Unknown certificate type.
	*/
	NONE,
	/** 
	 PKCS 8 is used with private key.
	*/
	PKCS_8,
	/** 
	 PKCS 10 is used with Certificate Signing Request.
	*/
	PKCS_10,
	/** 
	 x509Certificate is used with public key.
	*/
	x509_CERTIFICATE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PkcsType forValue(int value)
	{
		return values()[value];
	}
}
