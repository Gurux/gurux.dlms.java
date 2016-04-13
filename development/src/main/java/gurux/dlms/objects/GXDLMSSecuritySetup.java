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

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.objects.enums.SecurityPolicy;
import gurux.dlms.objects.enums.SecuritySuite;

public class GXDLMSSecuritySetup extends GXDLMSObject implements IGXDLMSBase {
    private SecurityPolicy securityPolicy;
    private SecuritySuite securitySuite;
    private byte[] serverSystemTitle;
    private byte[] clientSystemTitle;

    /**
     * Constructor.
     */
    public GXDLMSSecuritySetup() {
        super(ObjectType.SECURITY_SETUP);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSSecuritySetup(final String ln) {
        super(ObjectType.SECURITY_SETUP, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSSecuritySetup(final String ln, final int sn) {
        super(ObjectType.SECURITY_SETUP, ln, sn);
    }

    public final SecurityPolicy getSecurityPolicy() {
        return securityPolicy;
    }

    public final void setSecurityPolicy(final SecurityPolicy value) {
        securityPolicy = value;
    }

    public final SecuritySuite getSecuritySuite() {
        return securitySuite;
    }

    public final void setSecuritySuite(final SecuritySuite value) {
        securitySuite = value;
    }

    public final byte[] getClientSystemTitle() {
        return clientSystemTitle;
    }

    public final void setClientSystemTitle(final byte[] value) {
        clientSystemTitle = value;
    }

    public final byte[] getServerSystemTitle() {
        return serverSystemTitle;
    }

    public final void setServerSystemTitle(final byte[] value) {
        serverSystemTitle = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), securityPolicy, securitySuite,
                clientSystemTitle, serverSystemTitle };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // SecurityPolicy
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        // SecuritySuite
        if (canRead(3)) {
            attributes.add(new Integer(3));
        }
        if (getVersion() > 0) {
            // ClientSystemTitle
            if (canRead(4)) {
                attributes.add(new Integer(4));
            }
            // ServerSystemTitle
            if (canRead(5)) {
                attributes.add(new Integer(5));
            }
            // Certificates
            if (canRead(6)) {
                attributes.add(new Integer(6));
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        if (getVersion() == 0) {
            return 3;
        }
        return 6;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        if (getVersion() == 0) {
            return 3;
        }
        return 8;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.ENUM;
        }
        if (index == 3) {
            return DataType.ENUM;
        }
        if (getVersion() > 0) {
            if (index == 4) {
                return DataType.OCTET_STRING;
            }
            if (index == 5) {
                return DataType.OCTET_STRING;
            }
            if (index == 6) {
                return DataType.OCTET_STRING;
            }
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        } else {
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings, final int index,
            final int selector, final Object parameters) {
        if (index == 1) {
            return getLogicalName();
        }
        if (index == 2) {
            return new Integer(getSecurityPolicy().getValue());
        }
        if (index == 3) {
            return new Integer(getSecuritySuite().getValue());
        }
        if (index == 4) {
            return getClientSystemTitle();
        }
        if (index == 5) {
            return getServerSystemTitle();
        }
        throw new IllegalArgumentException(
                "GetValue failed. Invalid attribute index.");
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings, final int index,
            final Object value) {
        if (index == 1) {
            super.setValue(settings, index, value);
        } else if (index == 2) {
            setSecurityPolicy(
                    SecurityPolicy.forValue(((Number) value).byteValue()));
        } else if (index == 3) {
            setSecuritySuite(
                    SecuritySuite.forValue(((Number) value).byteValue()));
        } else if (index == 4) {
            setClientSystemTitle((byte[]) value);
        } else if (index == 5) {
            setServerSystemTitle((byte[]) value);
        } else {
            throw new IllegalArgumentException(
                    "GetValue failed. Invalid attribute index.");
        }
    }
}