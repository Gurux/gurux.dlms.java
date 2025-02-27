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

import java.math.BigInteger;

public class GXDLMSMeterPrimaryAccountNumber {
    /**
     * The isuer identification number.
     */
    private BigInteger issuerId;

    /**
     * Decoder reference number.
     */
    private BigInteger decoderReferenceNumber;

    /**
     * Pan check digit.
     */
    private short panCheckDigit;

    /**
     * The isuer identification number.
     */
    public final BigInteger getIssuerId() {
        return issuerId;
    }

    /**
     * The isuer identification number.
     */
    public final void setIssuerId(final BigInteger value) {
        issuerId = value;
    }

    /**
     * Decoder reference number.
     */
    public final BigInteger getDecoderReferenceNumber() {
        return decoderReferenceNumber;
    }

    /**
     * Decoder reference number.
     */
    public final void setDecoderReferenceNumber(final BigInteger value) {
        decoderReferenceNumber = value;
    }

    /**
     * Pan check digit.
     */
    public final short getPanCheckDigit() {
        return panCheckDigit;
    }

    /**
     * Pan check digit.
     */
    public final void setPanCheckDigit(final short value) {
        panCheckDigit = value;
    }
}
