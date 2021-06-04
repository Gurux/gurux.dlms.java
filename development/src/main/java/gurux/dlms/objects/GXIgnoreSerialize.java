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

package gurux.dlms.objects;

import gurux.dlms.enums.ObjectType;

/**
 * COSEM objects that are not serialized.
 */
public class GXIgnoreSerialize {

	/**
	 * Object types that are not serialized.
	 */
	private ObjectType objectType;

	/**
	 * Attributes that are not serialized.
	 */
	private int[] attributes;

	/**
	 * 
	 * @return Object types that are not serialized.
	 */
	public ObjectType getObjectType() {
		return objectType;
	}

	/**
	 * 
	 * @param value Object types that are not serialized.
	 */
	public void setObjectType(final ObjectType value) {
		this.objectType = value;
	}

	/**
	 * 
	 * @return Attributes that are not serialized.
	 */
	public int[] getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @param value Attributes that are not serialized.
	 */
	public void setAttributes(final int[] value) {
		attributes = value;
	}

	public GXIgnoreSerialize() {

	}

	public GXIgnoreSerialize(ObjectType ot, int index) {
		objectType = ot;
		attributes = new int[] { index };
	}

}
