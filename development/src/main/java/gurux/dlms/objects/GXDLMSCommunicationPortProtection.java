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
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.ProtectionMode;
import gurux.dlms.objects.enums.ProtectionStatus;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCommunicationPortProtection
 */
public class GXDLMSCommunicationPortProtection extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Controls the protection mode.
     */
    private ProtectionMode protectionMode;
    /**
     * Number of allowed failed communication attempts before port is disabled.
     */
    private int allowedFailedAttempts;
    /**
     * The lockout time.
     */
    private long initialLockoutTime;
    /**
     * Holds a factor that controls how the lockout time is increased with each
     * failed attempt.
     */
    private byte steepnessFactor;
    /**
     * The lockout time.
     */
    private long maxLockoutTime;
    /**
     * The communication port being protected
     */
    private GXDLMSObject port;
    /**
     * Current protection status.
     */
    private ProtectionStatus protectionStatus;
    /**
     * Failed attempts.
     */
    private long failedAttempts;
    /**
     * Total failed attempts.
     */
    private long cumulativeFailedAttempts;

    /**
     * Constructor.
     */
    public GXDLMSCommunicationPortProtection() {
        this("0.0.44.2.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSCommunicationPortProtection(final String ln) {
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
    public GXDLMSCommunicationPortProtection(final String ln, final int sn) {
        super(ObjectType.COMMUNICATION_PORT_PROTECTION, ln, sn);
        protectionMode = ProtectionMode.LOCKED_ON_FAILED_ATTEMPTS;
        steepnessFactor = 1;
        protectionStatus = ProtectionStatus.UNLOCKED;

    }

    /**
     * @return Controls the protection mode.
     */
    public final ProtectionMode getProtectionMode() {
        return protectionMode;
    }

    /**
     * @param value
     *            Controls the protection mode.
     */
    public final void setProtectionMode(final ProtectionMode value) {
        protectionMode = value;
    }

    /**
     * @return Number of allowed failed communication attempts before port is
     *         disabled.
     */
    public final int getAllowedFailedAttempts() {
        return allowedFailedAttempts;
    }

    /**
     * @param value
     *            Number of allowed failed communication attempts before port is
     *            disabled.
     */
    public final void setAllowedFailedAttempts(final int value) {
        allowedFailedAttempts = value;
    }

    /**
     * @return The lockout time.
     */
    public final long getInitialLockoutTime() {
        return initialLockoutTime;
    }

    /**
     * @param value
     *            The lockout time.
     */
    public final void setInitialLockoutTime(final long value) {
        initialLockoutTime = value;
    }

    /**
     * @return Holds a factor that controls how the lockout time is increased
     *         with each failed attempt.
     */
    public final byte getSteepnessFactor() {
        return steepnessFactor;
    }

    /**
     * @param value
     *            Holds a factor that controls how the lockout time is increased
     *            with each failed attempt.
     */
    public final void setSteepnessFactor(final byte value) {
        steepnessFactor = value;
    }

    /**
     * @return The lockout time.
     */
    public final long getMaxLockoutTime() {
        return maxLockoutTime;
    }

    /**
     * @param value
     *            The lockout time.
     */
    public final void setMaxLockoutTime(final long value) {
        maxLockoutTime = value;
    }

    /**
     * @return The communication port being protected
     */
    public final GXDLMSObject getPort() {
        return port;
    }

    /**
     * @param value
     *            The communication port being protected
     */
    public final void setPort(final GXDLMSObject value) {
        port = value;
    }

    /**
     * @return Current protection status.
     */
    public final ProtectionStatus getProtectionStatus() {
        return protectionStatus;
    }

    /**
     * @param value
     *            Current protection status.
     */
    public final void setProtectionStatus(final ProtectionStatus value) {
        protectionStatus = value;
    }

    /**
     * @return Failed attempts.
     */
    public final long getFailedAttempts() {
        return failedAttempts;
    }

    /**
     * @param value
     *            Failed attempts.
     */
    public final void setFailedAttempts(final long value) {
        failedAttempts = value;
    }

    /**
     * @return Total failed attempts.
     */
    public final long getCumulativeFailedAttempts() {
        return cumulativeFailedAttempts;
    }

    /**
     * @param value
     *            Total failed attempts.
     */
    public final void setCumulativeFailedAttempts(final long value) {
        cumulativeFailedAttempts = value;
    }

    /**
     * Resets FailedAttempts and current lockout time to zero. Protection status
     * is set to unlocked.
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
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] reset(final GXDLMSClient client) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(getName(), getObjectType(), 1, 0, DataType.INT8);
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), protectionMode, allowedFailedAttempts,
                initialLockoutTime, steepnessFactor, maxLockoutTime, port, protectionStatus,
                failedAttempts, cumulativeFailedAttempts };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead(final boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // ProtectionMode
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // AllowedFailedAttempts
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // InitialLockoutTime
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // SteepnessFactor
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // MaxLockoutTime
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // Port
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // ProtectionStatus
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // FailedAttempts
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // CumulativeFailedAttempts
        if (all || canRead(10)) {
            attributes.add(10);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 10;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public final DataType getDataType(final int index) {
        DataType ret;
        switch (index) {
        case 1:
            ret = DataType.OCTET_STRING;
            break;
        case 2:
            ret = DataType.ENUM;
            break;
        case 3:
            ret = DataType.UINT16;
            break;
        case 4:
            ret = DataType.UINT32;
            break;
        case 5:
            ret = DataType.UINT8;
            break;
        case 6:
            ret = DataType.UINT32;
            break;
        case 7:
            ret = DataType.OCTET_STRING;
            break;
        case 8:
            ret = DataType.ENUM;
            break;
        case 9:
            ret = DataType.UINT32;
            break;
        case 10:
            ret = DataType.UINT32;
            break;
        default:
            throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
        }
        return ret;
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        Object ret;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            ret = getProtectionMode().ordinal();
            break;
        case 3:
            ret = getAllowedFailedAttempts();
            break;
        case 4:
            ret = getInitialLockoutTime();
            break;
        case 5:
            ret = getSteepnessFactor();
            break;
        case 6:
            ret = getMaxLockoutTime();
            break;
        case 7:
            if (getPort() == null) {
                ret = GXCommon.logicalNameToBytes("");
            } else {
                ret = GXCommon.logicalNameToBytes(port.getLogicalName());
            }
            break;
        case 8:
            ret = getProtectionStatus().ordinal();
            break;
        case 9:
            ret = getFailedAttempts();
            break;
        case 10:
            ret = getCumulativeFailedAttempts();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            ret = null;
            break;
        }
        return ret;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            protectionMode = ProtectionMode.values()[((Number) e.getValue()).byteValue()];
            break;
        case 3:
            allowedFailedAttempts = ((Number) e.getValue()).intValue();
            break;
        case 4:
            initialLockoutTime = ((Number) e.getValue()).longValue();
            break;
        case 5:
            steepnessFactor = ((Number) e.getValue()).byteValue();
            break;
        case 6:
            maxLockoutTime = ((Number) e.getValue()).longValue();
            break;
        case 7:
            port = settings.getObjects().findByLN(ObjectType.NONE,
                    GXCommon.toLogicalName(e.getValue()));
            break;
        case 8:
            protectionStatus = ProtectionStatus.values()[((Number) e.getValue()).intValue()];
            break;
        case 9:
            failedAttempts = ((Number) e.getValue()).longValue();
            break;
        case 10:
            cumulativeFailedAttempts = ((Number) e.getValue()).longValue();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        protectionMode = ProtectionMode.values()[reader.readElementContentAsInt("ProtectionMode")];
        allowedFailedAttempts = reader.readElementContentAsInt("AllowedFailedAttempts");
        initialLockoutTime = reader.readElementContentAsLong("InitialLockoutTime");
        steepnessFactor = (byte) reader.readElementContentAsInt("SteepnessFactor");
        maxLockoutTime = reader.readElementContentAsLong("MaxLockoutTime");
        String str = reader.readElementContentAsString("Port");
        if (str == null || str == "") {
            port = null;
        } else {
            port = reader.getObjects().findByLN(ObjectType.NONE, str);
            // Save port object for data object if it's not loaded yet.
            if (port == null) {
                port = GXDLMSClient.createObject(ObjectType.DATA);
                port.setLogicalName(str);
            }
        }
        protectionStatus =
                ProtectionStatus.values()[reader.readElementContentAsInt("ProtectionStatus")];
        failedAttempts = reader.readElementContentAsLong("FailedAttempts");
        cumulativeFailedAttempts = reader.readElementContentAsLong("CumulativeFailedAttempts");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("ProtectionMode", protectionMode.ordinal());
        writer.writeElementString("AllowedFailedAttempts", allowedFailedAttempts);
        writer.writeElementString("InitialLockoutTime", initialLockoutTime);
        writer.writeElementString("SteepnessFactor", steepnessFactor);
        writer.writeElementString("MaxLockoutTime", maxLockoutTime);
        if (port == null) {
            writer.writeElementString("Port", "");
        } else {
            writer.writeElementString("Port", port.getLogicalName());
        }
        writer.writeElementString("ProtectionStatus", protectionStatus.ordinal());
        writer.writeElementString("FailedAttempts", failedAttempts);
        writer.writeElementString("CumulativeFailedAttempts", cumulativeFailedAttempts);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }

    @Override
    public String[] getNames() {
        return new String[] { "Logical Name", "Protection mode", "Allowed failed attempts",
                "Initial lockout time", "Steepness factor", "Max lockout time", "Port",
                "Protection status", "Failed attempts", "Cumulative failed attempts" };
    }

    @Override
    public String[] getMethodNames() {
        return new String[] { "Reset" };
    }
}