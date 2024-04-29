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

import gurux.dlms.objects.enums.ProtectionType;

/**
 * Push protection parameters.
 */
public class GXPushProtectionParameters {
    /**
     * Protection type.
     */
    private ProtectionType protectionType = ProtectionType.AUTHENTICATION;

    /**
     * Transaction Id.
     */
    private byte[] transactionId;

    /**
     * Originator system title.
     */
    private byte[] originatorSystemTitle;

    /**
     * Recipient system title.
     */
    private byte[] recipientSystemTitle;

    /**
     * Other information.
     */
    private byte[] otherInformation;

    /**
     * Key info.
     */
    private GXDLMSDataProtectionKey keyInfo = new GXDLMSDataProtectionKey();

    /**
     * @return Protection type.
     */
    public final ProtectionType getProtectionType() {
        return protectionType;
    }

    /**
     * @param value
     *            Protection type.
     */
    public final void setProtectionType(final ProtectionType value) {
        protectionType = value;
    }

    /**
     * @return Transaction Id.
     */
    public final byte[] getTransactionId() {
        return transactionId;
    }

    /**
     * @param value
     *            Transaction Id.
     */
    public final void setTransactionId(final byte[] value) {
        transactionId = value;
    }

    /**
     * @return Originator system title.
     */
    public final byte[] getOriginatorSystemTitle() {
        return originatorSystemTitle;
    }

    /**
     * @param value
     *            Originator system title.
     */
    public final void setOriginatorSystemTitle(final byte[] value) {
        originatorSystemTitle = value;
    }

    /**
     * @return Recipient system title.
     */
    public final byte[] getRecipientSystemTitle() {
        return recipientSystemTitle;
    }

    /**
     * @param value
     *            Recipient system title.
     */
    public final void setRecipientSystemTitle(final byte[] value) {
        recipientSystemTitle = value;
    }

    /**
     * @return Other information.
     */
    public final byte[] getOtherInformation() {
        return otherInformation;
    }

    /**
     * @param value
     *            Other information.
     */
    public final void setOtherInformation(final byte[] value) {
        otherInformation = value;
    }

    /**
     * @return Key info.
     */
    public final GXDLMSDataProtectionKey getKeyInfo() {
        return keyInfo;
    }

    /**
     * @param value
     *            Key info.
     */
    public final void setKeyInfo(final GXDLMSDataProtectionKey value) {
        keyInfo = value;
    }
}