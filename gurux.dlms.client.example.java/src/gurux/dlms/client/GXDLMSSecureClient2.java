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
package gurux.dlms.client;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import gurux.dlms.GXCryptoKeyParameter;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.IGXCryptoNotifier;
import gurux.dlms.asn.GXPkcs8;
import gurux.dlms.asn.GXx509Certificate;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.dlms.secure.GXDLMSSecureClient;

public class GXDLMSSecureClient2 extends GXDLMSSecureClient implements IGXCryptoNotifier {

	/**
	 * Constructor.
	 * 
	 * @param useLogicalNameReferencing Is Logical Name referencing used.
	 */
	public GXDLMSSecureClient2(final boolean useLogicalNameReferencing) {
		super(useLogicalNameReferencing);
	}

	/**
	 * Return correct path.
	 * 
	 * @param securitySuite Security Suite.
	 * @param type          Certificate type.
	 * @param path          Folder.
	 * @param systemTitle   System title.
	 * @return Path to the certificate file or folder if system title is not given.
	 */
	private static Path getPath(final SecuritySuite securitySuite, final CertificateType type, final String path,
			final byte[] systemTitle) {
		String pre;
		Path tmp;
		if (securitySuite == SecuritySuite.SUITE_2) {
			tmp = Paths.get(path, "384");
		} else {
			tmp = Paths.get(path);
		}
		if (systemTitle == null) {
			return tmp;
		}
		switch (type) {
		case DIGITAL_SIGNATURE:
			pre = "D";
			break;
		case KEY_AGREEMENT:
			pre = "A";
			break;
		default:
			throw new RuntimeException("Invalid type.");
		}
		return Paths.get(tmp.toString(), pre + GXDLMSTranslator.toHex(systemTitle, false) + ".pem");
	}

	/**
	 * Find ciphering keys.
	 * 
	 * @throws IOException
	 */
	@Override
	public void OnKey(Object sender, GXCryptoKeyParameter args) {
		try {
			if (args.getEncrypt()) {
				// Find private key.
				Path path = getPath(args.getSecuritySuite(), args.getCertificateType(), "Keys", args.getSystemTitle());
				args.setPrivateKey(GXPkcs8.load(path).getPrivateKey());
			} else {
				// Find public key.
				Path path = getPath(args.getSecuritySuite(), args.getCertificateType(), "Certificates",
						args.getSystemTitle());
				args.setPublicKey(GXx509Certificate.load(path).getPublicKey());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void OnCrypto(Object sender, GXCryptoKeyParameter args) {

	}

}