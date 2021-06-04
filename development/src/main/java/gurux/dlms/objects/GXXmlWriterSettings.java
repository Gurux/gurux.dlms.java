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

import java.util.ArrayList;
import java.util.List;

/**
 * XML write settings.
 */
public class GXXmlWriterSettings {

	/**
	 * Are attribute values also serialized.
	 */
	private boolean values;

	/**
	 * Are values saved in old way.
	 */
	private boolean old;

	/**
	 * Are default values serialized.
	 */
	private boolean ignoreDefaultValues;

	/**
	 * GXDateTime values are serialised using meter time, not local time.
	 */
	private boolean useMeterTime;

	/**
	 * List of attributes that are not serialized.
	 */
	private List<GXIgnoreSerialize> ignored;

	/**
	 * Constructor.
	 */
	public GXXmlWriterSettings() {
		values = true;
		ignoreDefaultValues = true;
		ignored = new ArrayList<GXIgnoreSerialize>();
	}

	/**
	 * @return Are attribute values also serialized.
	 */
	public final boolean getValues() {
		return values;
	}

	/**
	 * @param value Are attribute values also serialized.
	 */
	public final void setValues(final boolean value) {
		values = value;
	}

	/**
	 * @return Are values saved in old way.
	 */
	public final boolean getOld() {
		return old;
	}

	/**
	 * @param value Are Are values saved in old way.
	 */
	public final void setOld(final boolean value) {
		old = value;
	}

	/**
	 * @return Are default values serialized.
	 */
	public boolean isIgnoreDefaultValues() {
		return ignoreDefaultValues;
	}

	/**
	 * @param value Are default values serialized.
	 */
	public void setIgnoreDefaultValues(final boolean value) {
		ignoreDefaultValues = value;
	}

	/**
	 * @return GXDateTime values are serialised using meter time, not local time.
	 */
	public boolean isUseMeterTime() {
		return useMeterTime;
	}

	/**
	 * @param value GXDateTime values are serialised using meter time, not local
	 *              time.
	 */
	public void setUseMeterTime(final boolean value) {
		useMeterTime = value;
	}

	/**
	 * 
	 * @return List of attributes that are not serialized.
	 */
	public List<GXIgnoreSerialize> getIgnored() {
		return ignored;
	}

	/**
	 * 
	 * @param value List of attributes that are not serialized.
	 */
	public void setIgnored(List<GXIgnoreSerialize> value) {
		ignored = value;
	}
}
