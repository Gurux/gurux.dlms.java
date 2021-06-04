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

package gurux.dlms.asn;

import java.util.HashSet;
import java.util.Set;

import gurux.dlms.asn.enums.ExtendedKeyUsage;
import gurux.dlms.objects.enums.CertificateType;

/**
 * Certificate request
 */
public class GXCertificateRequest {
	/**
	 * Certificate type.
	 */
	private CertificateType certificateType;
	/**
	 * Indicates the purpose for which the certified public key is used.
	 */
	private Set<ExtendedKeyUsage> extendedKeyUsage;
	/**
	 * Certificate Signing Request.
	 */
	private GXPkcs10 certificate;

	/**
	 * Constructor.
	 */
	public GXCertificateRequest() {
		extendedKeyUsage = new HashSet<ExtendedKeyUsage>();
	}

	/**
	 * 
	 * @param certificateType Certificate type.
	 * @param certificate     PKCS 10 certificate.
	 */
	public GXCertificateRequest(CertificateType certificateType, GXPkcs10 certificate) {
		this();
		setCertificate(certificate);
		setCertificateType(certificateType);
	}

	/**
	 * 
	 * @return Certificate type.
	 */
	public final CertificateType getCertificateType() {
		return certificateType;
	}

	/**
	 * 
	 * @param value Certificate type.
	 */
	public final void setCertificateType(CertificateType value) {
		certificateType = value;
	}

	/**
	 * 
	 * @return Indicates the purpose for which the certified public key is used.
	 */
	public final Set<ExtendedKeyUsage> getExtendedKeyUsage() {
		return extendedKeyUsage;
	}

	/**
	 * 
	 * @param value Indicates the purpose for which the certified public key is
	 *              used.
	 */
	public final void setExtendedKeyUsage(final Set<ExtendedKeyUsage> value) {
		extendedKeyUsage = value;
	}

	/**
	 * 
	 * @return Certificate Signing Request.
	 */
	public final GXPkcs10 getCertificate() {
		return certificate;
	}

	/**
	 * 
	 * @param value Certificate Signing Request.
	 */
	public final void setCertificate(final GXPkcs10 value) {
		certificate = value;
	}
}
