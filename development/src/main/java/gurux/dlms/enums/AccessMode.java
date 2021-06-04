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
package gurux.dlms.enums;

/**
 * 
 * The AccessMode enumerates the access modes for Logical Name Association
 * version 3.
 *
 */
public enum AccessMode {
	/**
	 * No access.
	 */
	NO_ACCESS(0),
	/*
	 * The client is allowed only reading from the server. <p> This is used in
	 * version 1. </p>
	 */
	READ(1),
	/*
	 * The client is allowed only writing to the server.
	 */
	WRITE(2),
	/*
	 * The client is allowed both reading from the server and writing to it. <p>
	 * This is used in version 1. </p>
	 */
	READ_WRITE(3),

	/**
	 * Authenticated read is used.
	 * <p>
	 * This is used in version 2.
	 * </p>
	 */
	AUTHENTICATED_READ(4),

	/**
	 * Authenticated write is used
	 * <p>
	 * This is used in version 2.
	 * </p>
	 */
	AUTHENTICATED_WRITE(5),

	/**
	 * Authenticated Read Write is used.
	 * <p>
	 * This is used in version 2.
	 * </p>
	 */
	AUTHENTICATED_READ_WRITE(6);

	private int value;
	private static java.util.HashMap<Integer, AccessMode> mappings;

	private static java.util.HashMap<Integer, AccessMode> getMappings() {
		synchronized (AccessMode3.class) {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, AccessMode>();
			}
		}
		return mappings;
	}

	AccessMode(final int mode) {
		this.value = mode;
		getMappings().put(mode, this);
	}

	/*
	 * Get integer value for enum.
	 */
	public final int getValue() {
		return value;
	}

	/*
	 * Convert integer for enum value.
	 */
	public static AccessMode forValue(final int value) {
		return getMappings().get(value);
	}
}