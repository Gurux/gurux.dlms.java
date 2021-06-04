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

import java.util.HashSet;
import java.util.Set;

import gurux.dlms.enums.Conformance;
import gurux.dlms.internal.GXCommon;

public class GXxDLMSContextType {

	/**
	 * Conformance.
	 */
	private Set<Conformance> conformance;
	/**
	 * Maximum receive PDU size.
	 */
	private int maxReceivePduSize;
	/**
	 * Maximum Send PDU size.
	 */
	private int maxSendPduSize;
	/**
	 * DLMS Version Number.
	 */
	private int dlmsVersionNumber;
	/**
	 * Quality Of Service.
	 */
	private int qualityOfService;
	/**
	 * CypheringInfo.
	 */
	private byte[] cypheringInfo;

	/**
	 * Constructor.
	 */
	public GXxDLMSContextType() {
		conformance = new HashSet<Conformance>();
		dlmsVersionNumber = 6;
	}

	/**
	 * @return Conformance
	 */
	public final Set<Conformance> getConformance() {
		return conformance;
	}

	/**
	 * @param value Conformance
	 */
	public final void setConformance(final Set<Conformance> value) {
		conformance = value;
	}

	public final int getMaxReceivePduSize() {
		return maxReceivePduSize;
	}

	public final void setMaxReceivePduSize(final int value) {
		maxReceivePduSize = value;
	}

	public final int getMaxSendPduSize() {
		return maxSendPduSize;
	}

	public final void setMaxSendPduSize(final int value) {
		maxSendPduSize = value;
	}

	public final int getDlmsVersionNumber() {
		return dlmsVersionNumber;
	}

	public final void setDlmsVersionNumber(final int value) {
		dlmsVersionNumber = value;
	}

	public final int getQualityOfService() {
		return qualityOfService;
	}

	public final void setQualityOfService(final int value) {
		qualityOfService = value;
	}

	public final byte[] getCypheringInfo() {
		return cypheringInfo;
	}

	public final void setCypheringInfo(final byte[] value) {
		cypheringInfo = value;
	}

	@Override
	public final String toString() {
		return String.valueOf(conformance) + " " + String.valueOf(maxReceivePduSize) + " "
				+ String.valueOf(maxSendPduSize) + " " + String.valueOf(dlmsVersionNumber) + " "
				+ String.valueOf(qualityOfService) + " " + GXCommon.toHex(cypheringInfo, true);
	}
}
