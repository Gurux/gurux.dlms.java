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

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import gurux.dlms.asn.enums.KeyUsage;

/**
 * Enumerate values that are add to counted GMAC.
 */
public final class GXx509CertificateCollection extends ArrayList<GXx509Certificate>
		implements java.util.List<GXx509Certificate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Find certificate using serial information.
	 * 
	 * @param serialNumber Serial number.
	 * @param issuer       Issuer.
	 * @return x509Certificate
	 */
	public GXx509Certificate findBySerial(final BigInteger serialNumber, final String issuer) {
		for (GXx509Certificate it : this) {
			if (it.getSerialNumber().equals(serialNumber) && it.getIssuer().equalsIgnoreCase(issuer)) {
				return it;
			}
		}
		return null;
	}

	/**
	 * Find certificate using system title.
	 * 
	 * @param systemTitle System title.
	 * @param usage       Key usage.
	 * @return x509Certificate
	 */
	public GXx509Certificate findBySystemTitle(final byte[] systemTitle, final KeyUsage usage) {
		String cn = GXAsn1Converter.systemTitleToSubject(systemTitle);
		int u = usage.getValue();
		for (GXx509Certificate it : this) {
			if (KeyUsage.toInteger(it.getKeyUsage()) == u && (systemTitle == null || it.getSubject().contains(cn))) {
				return it;
			}
		}
		return null;
	}

	/**
	 * Find certificate using key usage.
	 * 
	 * @param usage Key usage.
	 * @return x509Certificate
	 */
	public List<GXx509Certificate> getCertificates(final Set<KeyUsage> usage) {
		List<GXx509Certificate> list = new ArrayList<GXx509Certificate>();
		int u = KeyUsage.toInteger(usage);
		for (GXx509Certificate it : this) {
			if (KeyUsage.toInteger(it.getKeyUsage()) == u) {
				list.add(it);
			}
		}
		return list;
	}

	/**
	 * Find public key certificate by common name (CN).
	 * 
	 * @param commonName Common name.
	 * @param usage      Key usage.
	 * @return x509Certificate
	 */
	public GXx509Certificate findByCommonName(final String commonName, final Set<KeyUsage> usage) {
		int value = KeyUsage.toInteger(usage);
		for (GXx509Certificate it : this) {
			if (KeyUsage.toInteger(it.getKeyUsage()) == value
					&& (commonName == null || it.getSubject().contains(commonName))) {
				return it;
			}
		}
		return null;
	}

	/**
	 * Check if certificate exists in the array using Common Name and key usage of
	 * the certificate.
	 * 
	 * @param cert Certificate to search.
	 * @return Found certificate.
	 */
	public GXx509Certificate find(final GXx509Certificate cert) {
		for (GXx509Certificate it : this) {
			if (it.getSerialNumber() == cert.getSerialNumber()) {
				return it;
			}
		}
		return null;
	}

	/**
	 * Import certificates from the given folder.
	 * 
	 * @param directory Directory where certificates are imported.
	 */
	public void importFromDirectory(final String directory) {
		File dir = new File(directory);
		if (dir.exists()) {
			String[] files = dir.list();
			for (String it : files) {
				try {
					if (it.endsWith(".pem")) {
						File f = new File(it);
						GXx509Certificate cert = GXx509Certificate.load(f.toPath());
						add(cert);
					}
				} catch (Exception ex) {
					Logger.getLogger(GXx509CertificateCollection.class.getName()).log(Level.SEVERE,
							"Failed to load x509 certificate: {0}", it);
				}
			}
		}
	}
}