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

package gurux.dlms.asn.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * Extended key usage.
 */
public enum ExtendedKeyUsage {
	/**
	 * Certificate can be used as an TLS server certificate.
	 */
	SERVER_AUTH(1),
	/**
	 * Certificate can be used as an TLS client certificate.
	 */
	CLIENT_AUTH(2);

	private int value;

	/*
	 * Constructor.
	 */
	ExtendedKeyUsage(final int forValue) {
		value = forValue;
	}

	/*
	 * Get integer value for enumerator.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return Get enumeration constant values.
	 */
	private static ExtendedKeyUsage[] getEnumConstants() {
		return new ExtendedKeyUsage[] { SERVER_AUTH, CLIENT_AUTH };
	}

	/**
	 * Converts the integer value to enumerated value.
	 * 
	 * @param value The integer value, which is read from the device.
	 * @return The enumerated value, which represents the integer.
	 */
	public static java.util.Set<ExtendedKeyUsage> forValue(final int value) {
		Set<ExtendedKeyUsage> types = new HashSet<ExtendedKeyUsage>();
		ExtendedKeyUsage[] enums = getEnumConstants();
		for (int pos = 0; pos != enums.length; ++pos) {
			if ((enums[pos].value & value) == enums[pos].value) {
				types.add(enums[pos]);
			}
		}
		return types;
	}

	/**
	 * Converts the enumerated value to integer value.
	 * 
	 * @param value The enumerated value.
	 * @return The integer value.
	 */
	public static int toInteger(final Set<ExtendedKeyUsage> value) {
		int tmp = 0;
		for (ExtendedKeyUsage it : value) {
			tmp |= it.getValue();
		}
		return tmp;
	}
}
