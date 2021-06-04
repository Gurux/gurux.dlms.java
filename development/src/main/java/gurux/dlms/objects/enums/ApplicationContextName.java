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

import java.util.HashMap;

/**
 * Enumerates application context name.<br>
 */
public enum ApplicationContextName {

	/**
	 * Invalid application context name.
	 */
	UNKNOWN(0),
	/**
	 * Logical name.
	 */
	LOGICAL_NAME(1),
	/**
	 * Short name.
	 */
	SHORT_NAME(2),
	/**
	 * Logical name with ciphering.
	 */
	LOGICAL_NAME_WITH_CIPHERING(3),
	/**
	 * Short name with ciphering.
	 */
	SHORT_NAME_WITH_CIPHERING(4);

	/**
	 * Integer value of enumerator.
	 */
	private int intValue;

	/**
	 * Collection of enumerator values.
	 */
	private static java.util.HashMap<Integer, ApplicationContextName> mappings;

	/**
	 * Returns collection of enumerator values.
	 * 
	 * @return Enumerator values.
	 */
	private static HashMap<Integer, ApplicationContextName> getMappings() {
		synchronized (ApplicationContextName.class) {
			if (mappings == null) {
				mappings = new HashMap<Integer, ApplicationContextName>();
			}
		}
		return mappings;
	}

	/**
	 * Constructor.
	 * 
	 * @param value Integer value of enumerator.
	 */
	ApplicationContextName(final int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	/**
	 * Get integer value for enumerator.
	 * 
	 * @return Enumerator integer value.
	 */
	public int getValue() {
		return intValue;
	}

	/**
	 * Returns enumerator value from an integer value.
	 * 
	 * @param value Integer value.
	 * @return Enumeration value.
	 */
	public static ApplicationContextName forValue(final int value) {
		ApplicationContextName ret = getMappings().get(value);
		if (ret == null) {
			throw new IllegalArgumentException("Invalid application context name enum value.");
		}
		return ret;
	}
}