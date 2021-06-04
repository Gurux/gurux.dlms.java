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
package gurux.dlms.objects.enums;

/**
 * Security suite Specifies authentication, encryption and key wrapping
 * algorithm.
 */
public enum SecuritySuite {
	/**
	 * GMAC ciphering is used.<br>
	 * AES-GCM-128 for authenticated encryption and AES-128 for key wrapping. <br>
	 * A.K.A Security Suite 0.
	 */
	SUITE_0,
	/**
	 * ECDSA P-256 ciphering is used. <br>
	 * ECDH-ECDSAAES-GCM-128SHA-256. <br>
	 * A.K.A Security Suite 1.
	 */
	SUITE_1,
	/**
	 * ECDSA P-384 ciphering is used.<br>
	 * ECDH-ECDSAAES-GCM-256SHA-384. <br>
	 * A.K.A Security Suite 2.
	 */
	SUITE_2;

	public int getValue() {
		return this.ordinal();
	}

	public static SecuritySuite forValue(final int value) {
		SecuritySuite ret = values()[value];
		if (ret == null) {
			throw new IllegalArgumentException("Invalid security suite enum value.");
		}
		return ret;

	}
}