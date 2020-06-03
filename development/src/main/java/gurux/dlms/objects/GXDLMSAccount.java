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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXBitString;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.AccountCreditStatus;
import gurux.dlms.objects.enums.AccountStatus;
import gurux.dlms.objects.enums.CreditCollectionConfiguration;
import gurux.dlms.objects.enums.Currency;
import gurux.dlms.objects.enums.PaymentMode;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
 */
public class GXDLMSAccount extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Payment mode.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */

    private PaymentMode paymentMode;

    /**
     * Account status.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private AccountStatus accountStatus;

    /**
     * Credit In Use.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private byte currentCreditInUse;

    /**
     * Credit status.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private AccountCreditStatus currentCreditStatus;

    /**
     * The available_credit attribute is the sum of the positive current credit
     * amount values in the instances of the Credit class. <br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int availableCredit;

    /**
     * Amount to clear.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int amountToClear;

    /**
     * Conjunction with the amount to clear, and is included in the description
     * of that attribute.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int clearanceThreshold;

    /**
     * Aggregated debt.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int aggregatedDebt;

    /**
     * Credit references.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private List<String> creditReferences;

    /**
     * Charge references.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private List<String> chargeReferences;
    /**
     * Credit charge configurations.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private List<GXCreditChargeConfiguration> creditChargeConfigurations;

    /**
     * Token gateway configurations.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private List<GXTokenGatewayConfiguration> tokenGatewayConfigurations;
    /**
     * Account activation time.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private GXDateTime accountActivationTime;

    /**
     * Account closure time.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private GXDateTime accountClosureTime;

    /**
     * Currency.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private GXCurrency currency;
    /**
     * Low credit threshold.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int lowCreditThreshold;
    /**
     * Next credit available threshold.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int nextCreditAvailableThreshold;
    /**
     * Max Provision.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int maxProvision;

    /**
     * Max provision period.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     */
    private int maxProvisionPeriod;

    /**
     * Activate the value.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public final byte[][] activate(final GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return client.method(getName(), getObjectType(), 1, 0, DataType.INT8);
    }

    /**
     * Close the value.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public final byte[][] close(final GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return client.method(getName(), getObjectType(), 2, 0, DataType.INT8);
    }

    /**
     * Reset value.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     */
    public final byte[][] reset(final GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return client.method(getName(), getObjectType(), 3, 0, DataType.INT8);
    }

    /**
     * Constructor.
     */
    public GXDLMSAccount() {
        this("0.0.19.0.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSAccount(final String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSAccount(final String ln, final int sn) {
        super(ObjectType.ACCOUNT, ln, sn);
        paymentMode = PaymentMode.CREDIT;
        accountStatus = AccountStatus.NEW_INACTIVE_ACCOUNT;
        creditReferences = new ArrayList<String>();
        chargeReferences = new ArrayList<String>();
        creditChargeConfigurations =
                new ArrayList<GXCreditChargeConfiguration>();
        tokenGatewayConfigurations =
                new ArrayList<GXTokenGatewayConfiguration>();
        currency = new GXCurrency();
        currentCreditStatus = AccountCreditStatus.IN_CREDIT;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Payment mode.
     */
    public final PaymentMode getPaymentMode() {
        return paymentMode;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Payment mode.
     */
    public final void setPaymentMode(final PaymentMode value) {
        paymentMode = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Account status.
     */
    public final AccountStatus getAccountStatus() {
        return accountStatus;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Account status.
     */
    public final void setAccountStatus(final AccountStatus value) {
        accountStatus = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Credit In Use.
     */
    public final byte getCurrentCreditInUse() {
        return currentCreditInUse;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Credit In Use.
     */
    public final void setCurrentCreditInUse(final byte value) {
        currentCreditInUse = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Credit status.
     */
    public final AccountCreditStatus getCurrentCreditStatus() {
        return currentCreditStatus;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Credit status.
     */
    public final void setCurrentCreditStatus(final AccountCreditStatus value) {
        currentCreditStatus = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return AvailableCredit
     */
    public final int getAvailableCredit() {
        return availableCredit;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            AvailableCredit
     */
    public final void setAvailableCredit(final int value) {
        availableCredit = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Amount to clear.
     */
    public final int getAmountToClear() {
        return amountToClear;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Amount to clear.
     */
    public final void setAmountToClear(final int value) {
        amountToClear = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Clearance threshold.
     */
    public final int getClearanceThreshold() {
        return clearanceThreshold;
    }

    /**
     * @param value
     *            Clearance threshold.
     */
    public final void setClearanceThreshold(final int value) {
        clearanceThreshold = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Aggregated debt.
     */
    public final int getAggregatedDebt() {
        return aggregatedDebt;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Aggregated debt.
     */
    public final void setAggregatedDebt(final int value) {
        aggregatedDebt = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Credit references.
     */
    public final List<String> getCreditReferences() {
        return creditReferences;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Credit references.
     */
    public final void setCreditReferences(final List<String> value) {
        creditReferences = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Charge references.
     */
    public final List<String> getChargeReferences() {
        return chargeReferences;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Charge references.
     */
    public final void setChargeReferences(final List<String> value) {
        chargeReferences = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Credit charge configurations.
     */
    public final List<GXCreditChargeConfiguration>
            getCreditChargeConfigurations() {
        return creditChargeConfigurations;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Credit charge configurations.
     */
    public final void setCreditChargeConfigurations(
            final List<GXCreditChargeConfiguration> value) {
        creditChargeConfigurations = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Token gateway configurations.
     */
    public final List<GXTokenGatewayConfiguration>
            getTokenGatewayConfigurations() {
        return tokenGatewayConfigurations;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Token gateway configurations.
     */
    public final void setTokenGatewayConfigurations(
            final List<GXTokenGatewayConfiguration> value) {
        tokenGatewayConfigurations = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Account activation time.
     */
    public final GXDateTime getAccountActivationTime() {
        return accountActivationTime;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Account activation time.
     */
    public final void setAccountActivationTime(final GXDateTime value) {
        accountActivationTime = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Account closure time.
     */
    public final GXDateTime getAccountClosureTime() {
        return accountClosureTime;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Account closure time.
     */
    public final void setAccountClosureTime(final GXDateTime value) {
        accountClosureTime = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Currency.
     */
    public final GXCurrency getCurrency() {
        return currency;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Currency.
     */
    public final void setCurrency(final GXCurrency value) {
        currency = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Low credit threshold.
     */
    public final int getLowCreditThreshold() {
        return lowCreditThreshold;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Low credit threshold.
     */
    public final void setLowCreditThreshold(final int value) {
        lowCreditThreshold = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Next credit available threshold.
     */
    public final int getNextCreditAvailableThreshold() {
        return nextCreditAvailableThreshold;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Next credit available threshold.
     */
    public final void setNextCreditAvailableThreshold(final int value) {
        nextCreditAvailableThreshold = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Max Provision.
     */
    public final int getMaxProvision() {
        return maxProvision;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Max Provision.
     */
    public final void setMaxProvision(final int value) {
        maxProvision = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @return Max provision period.
     */
    public final int getMaxProvisionPeriod() {
        return maxProvisionPeriod;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSAccount
     * 
     * @param value
     *            Max provision period.
     */
    public final void setMaxProvisionPeriod(final int value) {
        maxProvisionPeriod = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(),
                new Object[] { paymentMode, accountStatus }, currentCreditInUse,
                currentCreditStatus, availableCredit, amountToClear,
                clearanceThreshold, aggregatedDebt, creditReferences,
                chargeReferences, creditChargeConfigurations,
                tokenGatewayConfigurations, accountActivationTime,
                accountClosureTime, currency, lowCreditThreshold,
                nextCreditAvailableThreshold, maxProvision,
                maxProvisionPeriod };
    }

    @Override
    public final byte[] invoke(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        // Resets the value to the default value.
        // The default value is an instance specific constant.
        if (e.getIndex() == 1) {
            accountStatus = AccountStatus.ACTIVE;
        } else if (e.getIndex() == 2) {
            accountStatus = AccountStatus.CLOSED;
        } else if (e.getIndex() == 3) {
            // Meter must handle this.
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
        return null;
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null
                || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // PaymentMode, AccountStatus
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // CurrentCreditInUse
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // CurrentCreditStatus
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // AvailableCredit
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // AmountToClear
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // ClearanceThreshold
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // AggregatedDebt
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // CreditReferences
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // ChargeReferences
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // CreditChargeConfigurations
        if (all || canRead(11)) {
            attributes.add(11);
        }
        // TokenGatewayConfigurations
        if (all || canRead(12)) {
            attributes.add(12);
        }
        // AccountActivationTime
        if (all || canRead(13)) {
            attributes.add(13);
        }
        // AccountClosureTime
        if (all || canRead(14)) {
            attributes.add(14);
        }
        // Currency
        if (all || canRead(15)) {
            attributes.add(15);
        }
        // LowCreditThreshold
        if (all || canRead(16)) {
            attributes.add(16);
        }
        // NextCreditAvailableThreshold
        if (all || canRead(17)) {
            attributes.add(17);
        }
        // MaxProvision
        if (all || canRead(18)) {
            attributes.add(18);
        }
        // MaxProvisionPeriod
        if (all || canRead(19)) {
            attributes.add(19);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 19;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 3;
    }

    @Override
    public final DataType getDataType(final int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.STRUCTURE;
        case 3:
            return DataType.UINT8;
        case 4:
            return DataType.BITSTRING;
        case 5:
            return DataType.INT32;
        case 6:
            return DataType.INT32;
        case 7:
            return DataType.INT32;
        case 8:
            return DataType.INT32;
        case 9:
            return DataType.ARRAY;
        case 10:
            return DataType.ARRAY;
        case 11:
            return DataType.ARRAY;
        case 12:
            return DataType.ARRAY;
        case 13:
            return DataType.OCTET_STRING;
        case 14:
            return DataType.OCTET_STRING;
        case 15:
            return DataType.STRUCTURE;
        case 16:
            return DataType.INT32;
        case 17:
            return DataType.INT32;
        case 18:
            return DataType.UINT16;
        case 19:
            return DataType.INT32;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        GXByteBuffer bb;
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.STRUCTURE.getValue());
            bb.setUInt8(2);
            bb.setUInt8(DataType.ENUM.getValue());
            bb.setUInt8(accountStatus.getValue());
            bb.setUInt8(DataType.ENUM.getValue());
            bb.setUInt8(paymentMode.getValue());
            return bb.array();
        case 3:
            return currentCreditInUse;
        case 4:
            return GXBitString.toBitString(currentCreditStatus.getValue(), 8);
        case 5:
            return availableCredit;
        case 6:
            return amountToClear;
        case 7:
            return clearanceThreshold;
        case 8:
            return aggregatedDebt;
        case 9:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (creditReferences == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(creditReferences.size(), bb);
                for (String it : creditReferences) {
                    bb.setUInt8(DataType.OCTET_STRING.getValue());
                    bb.setUInt8(6);
                    bb.set(GXCommon.logicalNameToBytes(it));
                }
            }
            return bb.array();
        case 10:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (chargeReferences == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(chargeReferences.size(), bb);
                for (String it : chargeReferences) {
                    bb.setUInt8(DataType.OCTET_STRING.getValue());
                    bb.setUInt8(6);
                    bb.set(GXCommon.logicalNameToBytes(it));
                }
            }
            return bb.array();
        case 11:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (creditChargeConfigurations == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(creditChargeConfigurations.size(), bb);
                for (GXCreditChargeConfiguration it : creditChargeConfigurations) {
                    bb.setUInt8(DataType.STRUCTURE.getValue());
                    bb.setUInt8(3);
                    bb.setUInt8(DataType.OCTET_STRING.getValue());
                    bb.setUInt8(6);
                    bb.set(GXCommon
                            .logicalNameToBytes(it.getCreditReference()));
                    bb.setUInt8(DataType.OCTET_STRING.getValue());
                    bb.setUInt8(6);
                    bb.set(GXCommon
                            .logicalNameToBytes(it.getChargeReference()));
                    GXCommon.setData(settings, bb, DataType.BITSTRING,
                            GXBitString.toBitString(
                                    CreditCollectionConfiguration.toInteger(
                                            it.getCollectionConfiguration()),
                                    3));
                }
            }
            return bb.array();
        case 12:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (tokenGatewayConfigurations == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(tokenGatewayConfigurations.size(), bb);
                for (GXTokenGatewayConfiguration it : tokenGatewayConfigurations) {
                    bb.setUInt8(DataType.STRUCTURE.getValue());
                    bb.setUInt8(2);
                    bb.setUInt8(DataType.OCTET_STRING.getValue());
                    bb.setUInt8(6);
                    bb.set(GXCommon
                            .logicalNameToBytes(it.getCreditReference()));
                    bb.setUInt8(DataType.UINT8.getValue());
                    bb.setUInt8(it.getTokenProportion());
                }
            }
            return bb.array();
        case 13:
            return accountActivationTime;
        case 14:
            return accountClosureTime;
        case 15:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.STRUCTURE.getValue());
            bb.setUInt8(3);
            GXCommon.setData(settings, bb, DataType.STRING_UTF8,
                    currency.getName());
            GXCommon.setData(settings, bb, DataType.INT8, currency.getScale());
            GXCommon.setData(settings, bb, DataType.ENUM,
                    currency.getUnit().getValue());
            return bb.array();
        case 16:
            return lowCreditThreshold;
        case 17:
            return nextCreditAvailableThreshold;
        case 18:
            return maxProvision;
        case 19:
            return maxProvisionPeriod;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            accountStatus = AccountStatus.forValue(
                    ((Number) ((List<?>) e.getValue()).get(0)).intValue());
            paymentMode = PaymentMode.forValue(
                    ((Number) ((List<?>) e.getValue()).get(1)).intValue());
            break;
        case 3:
            currentCreditInUse = ((Number) e.getValue()).byteValue();
            break;
        case 4:
            currentCreditStatus = AccountCreditStatus
                    .forValue(((GXBitString) e.getValue()).toInteger());
            break;
        case 5:
            availableCredit = ((Number) e.getValue()).intValue();
            break;
        case 6:
            amountToClear = ((Number) e.getValue()).intValue();
            break;
        case 7:
            clearanceThreshold = ((Number) e.getValue()).intValue();
            break;
        case 8:
            aggregatedDebt = ((Number) e.getValue()).intValue();
            break;
        case 9:
            creditReferences.clear();
            if (e.getValue() != null) {
                for (Object it : (List<?>) e.getValue()) {
                    creditReferences.add(GXCommon.toLogicalName(it));
                }
            }
            break;
        case 10:
            chargeReferences.clear();
            if (e.getValue() != null) {
                for (Object it : (List<?>) e.getValue()) {
                    chargeReferences.add(GXCommon.toLogicalName(it));
                }
            }
            break;
        case 11:
            creditChargeConfigurations.clear();
            if (e.getValue() != null) {
                for (Object it2 : (List<?>) e.getValue()) {
                    List<?> it = (List<?>) it2;
                    GXCreditChargeConfiguration item =
                            new GXCreditChargeConfiguration();
                    item.setCreditReference(GXCommon.toLogicalName(it.get(0)));
                    item.setChargeReference(GXCommon.toLogicalName(it.get(1)));
                    item.setCollectionConfiguration(
                            CreditCollectionConfiguration.forValue(
                                    ((GXBitString) it.get(2)).toInteger()));
                    creditChargeConfigurations.add(item);
                }
            }
            break;
        case 12:
            tokenGatewayConfigurations.clear();
            if (e.getValue() != null) {
                for (Object it2 : (List<?>) e.getValue()) {
                    List<?> it = (List<?>) it2;
                    GXTokenGatewayConfiguration item =
                            new GXTokenGatewayConfiguration();
                    item.setCreditReference(GXCommon.toLogicalName(it.get(0)));
                    item.setTokenProportion(((Number) it.get(1)).byteValue());
                    tokenGatewayConfigurations.add(item);
                }
            }
            break;
        case 13:
            if (e.getValue() == null) {
                accountActivationTime = new GXDateTime();
            } else {
                GXDateTime tmp;
                if (e.getValue() instanceof byte[]) {
                    boolean useUtc;
                    if (e.getSettings() != null) {
                        useUtc = e.getSettings().getUseUtc2NormalTime();
                    } else {
                        useUtc = false;
                    }
                    tmp = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) e.getValue(), DataType.DATETIME, useUtc);
                } else {
                    tmp = (GXDateTime) e.getValue();
                }
                accountActivationTime = tmp;
            }
            break;
        case 14:
            if (e.getValue() == null) {
                accountClosureTime = new GXDateTime();
            } else {
                GXDateTime tmp;
                if (e.getValue() instanceof byte[]) {
                    boolean useUtc;
                    if (e.getSettings() != null) {
                        useUtc = e.getSettings().getUseUtc2NormalTime();
                    } else {
                        useUtc = false;
                    }
                    tmp = (GXDateTime) GXDLMSClient.changeType(
                            (byte[]) e.getValue(), DataType.DATETIME, useUtc);
                } else {
                    tmp = (GXDateTime) e.getValue();
                }
                accountClosureTime = tmp;
            }
            break;
        case 15:
            List<?> tmp = (List<?>) e.getValue();
            currency.setName((String) tmp.get(0));
            currency.setScale(((Number) tmp.get(1)).byteValue());
            currency.setUnit(
                    Currency.forValue(((Number) tmp.get(2)).intValue()));
            break;
        case 16:
            lowCreditThreshold = ((Number) e.getValue()).intValue();
            break;
        case 17:
            nextCreditAvailableThreshold = ((Number) e.getValue()).intValue();
            break;
        case 18:
            maxProvision = ((Number) e.getValue()).intValue();
            break;
        case 19:
            maxProvisionPeriod = ((Number) e.getValue()).intValue();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    private static void loadReferences(GXXmlReader reader, String name,
            List<String> list) throws XMLStreamException {
        list.clear();
        if (reader.isStartElement(name, true)) {
            while (reader.isStartElement("Item", true)) {
                list.add(reader.readElementContentAsString("Name"));
            }
            reader.readEndElement(name);
        }
    }

    private static void loadCreditChargeConfigurations(GXXmlReader reader,
            List<GXCreditChargeConfiguration> list) throws XMLStreamException {
        list.clear();
        if (reader.isStartElement("CreditChargeConfigurations", true)) {
            while (reader.isStartElement("Item", true)) {
                GXCreditChargeConfiguration it =
                        new GXCreditChargeConfiguration();
                it.setCreditReference(
                        reader.readElementContentAsString("Credit"));
                it.setChargeReference(
                        reader.readElementContentAsString("Charge"));
                it.setCollectionConfiguration(
                        CreditCollectionConfiguration.forValue(reader
                                .readElementContentAsInt("Configuration")));
                list.add(it);
            }
            reader.readEndElement("CreditChargeConfigurations");
        }
    }

    private static void loadTokenGatewayConfigurations(GXXmlReader reader,
            List<GXTokenGatewayConfiguration> list) throws XMLStreamException {
        list.clear();
        if (reader.isStartElement("TokenGatewayConfigurations", true)) {
            while (reader.isStartElement("Item", true)) {
                GXTokenGatewayConfiguration it =
                        new GXTokenGatewayConfiguration();
                it.setCreditReference(
                        reader.readElementContentAsString("Credit"));
                it.setTokenProportion(
                        (byte) reader.readElementContentAsInt("Token"));
                list.add(it);
            }
            reader.readEndElement("TokenGatewayConfigurations");
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        paymentMode = PaymentMode
                .forValue(reader.readElementContentAsInt("PaymentMode"));
        accountStatus = AccountStatus
                .forValue(reader.readElementContentAsInt("AccountStatus"));
        currentCreditInUse =
                (byte) reader.readElementContentAsInt("CurrentCreditInUse");
        currentCreditStatus = AccountCreditStatus.forValue(
                reader.readElementContentAsInt("CurrentCreditStatus"));
        availableCredit = reader.readElementContentAsInt("AvailableCredit");
        amountToClear = reader.readElementContentAsInt("AmountToClear");
        clearanceThreshold =
                reader.readElementContentAsInt("ClearanceThreshold");
        aggregatedDebt = reader.readElementContentAsInt("AggregatedDebt");
        loadReferences(reader, "CreditReferences", creditReferences);
        loadReferences(reader, "ChargeReferences", chargeReferences);
        loadCreditChargeConfigurations(reader, creditChargeConfigurations);
        loadTokenGatewayConfigurations(reader, tokenGatewayConfigurations);
        accountActivationTime =
                reader.readElementContentAsDateTime("AccountActivationTime");
        accountClosureTime =
                reader.readElementContentAsDateTime("AccountClosureTime");
        currency.setName(reader.readElementContentAsString("CurrencyName"));
        currency.setScale(
                (byte) reader.readElementContentAsInt("CurrencyScale"));
        currency.setUnit(Currency
                .forValue(reader.readElementContentAsInt("CurrencyUnit")));
        lowCreditThreshold =
                reader.readElementContentAsInt("LowCreditThreshold");
        nextCreditAvailableThreshold =
                reader.readElementContentAsInt("NextCreditAvailableThreshold");
        maxProvision = reader.readElementContentAsInt("MaxProvision");
        maxProvisionPeriod =
                reader.readElementContentAsInt("MaxProvisionPeriod");
    }

    private void saveReferences(GXXmlWriter writer, List<String> list,
            String name) throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement(name);
            for (String it : list) {
                writer.writeStartElement("Item");
                writer.writeElementString("Name", it);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveCreditChargeConfigurations(GXXmlWriter writer,
            List<GXCreditChargeConfiguration> list) throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement("CreditChargeConfigurations");
            for (GXCreditChargeConfiguration it : list) {
                writer.writeStartElement("Item");
                writer.writeElementString("Credit", it.getCreditReference());
                writer.writeElementString("Charge", it.getChargeReference());
                writer.writeElementString("Configuration",
                        CreditCollectionConfiguration
                                .toInteger(it.getCollectionConfiguration()));
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveTokenGatewayConfigurations(GXXmlWriter writer,
            List<GXTokenGatewayConfiguration> list) throws XMLStreamException {
        if (list != null) {
            writer.writeStartElement("TokenGatewayConfigurations");
            for (GXTokenGatewayConfiguration it : list) {
                writer.writeStartElement("Item");
                writer.writeElementString("Credit", it.getCreditReference());
                writer.writeElementString("Token", it.getTokenProportion());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("PaymentMode", paymentMode.getValue());
        writer.writeElementString("AccountStatus", accountStatus.getValue());
        writer.writeElementString("CurrentCreditInUse", currentCreditInUse);
        if (currentCreditStatus != null) {
            writer.writeElementString("CurrentCreditStatus",
                    currentCreditStatus.getValue());
        }
        writer.writeElementString("AvailableCredit", availableCredit);
        writer.writeElementString("AmountToClear", amountToClear);
        writer.writeElementString("ClearanceThreshold", clearanceThreshold);
        writer.writeElementString("AggregatedDebt", aggregatedDebt);
        saveReferences(writer, creditReferences, "CreditReferences");
        saveReferences(writer, chargeReferences, "ChargeReferences");
        saveCreditChargeConfigurations(writer, creditChargeConfigurations);
        saveTokenGatewayConfigurations(writer, tokenGatewayConfigurations);
        writer.writeElementString("AccountActivationTime",
                accountActivationTime);
        writer.writeElementString("AccountClosureTime", accountClosureTime);
        writer.writeElementString("CurrencyName", currency.getName());
        writer.writeElementString("CurrencyScale", currency.getScale());
        writer.writeElementString("CurrencyUnit",
                currency.getUnit().getValue());

        writer.writeElementString("LowCreditThreshold", lowCreditThreshold);
        writer.writeElementString("NextCreditAvailableThreshold",
                nextCreditAvailableThreshold);

        writer.writeElementString("MaxProvision", maxProvision);
        writer.writeElementString("MaxProvisionPeriod", maxProvisionPeriod);

    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}