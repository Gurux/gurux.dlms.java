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

package gurux.dlms.objects;

import gurux.dlms.internal.GXCommon;

public class GXxDLMSContextType {
    private String conformance;
    private int maxReceivePduSize;
    private int maxSendPpuSize;
    private int dlmsVersionNumber;
    private int qualityOfService;
    private byte[] cypheringInfo;

    public final String getConformance() {
        return conformance;
    }

    public final void setConformance(final String value) {
        conformance = value;
    }

    public final int getMaxReceivePduSize() {
        return maxReceivePduSize;
    }

    public final void setMaxReceivePduSize(final int value) {
        maxReceivePduSize = value;
    }

    public final int getMaxSendPpuSize() {
        return maxSendPpuSize;
    }

    public final void setMaxSendPpuSize(final int value) {
        maxSendPpuSize = value;
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
        return String.valueOf(conformance) + " "
                + String.valueOf(maxReceivePduSize) + " "
                + String.valueOf(maxSendPpuSize) + " "
                + String.valueOf(dlmsVersionNumber) + " "
                + String.valueOf(qualityOfService) + " "
                + GXCommon.toHex(cypheringInfo, true);
    }
}
