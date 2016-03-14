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

package gurux.dlms;

import gurux.dlms.objects.enums.BaudRate;

/**
 * APDU types.
 */
public enum PduType {
    /**
     * IMPLICIT BIT STRING {version1 (0)} DEFAULT {version1}
     */
    ProtocolVersion(0),

    /**
     * Application-context-name
     */
    ApplicationContextName(1),

    /**
     * AP-title OPTIONAL
     */
    CALLEDAPTITLE(2),

    /**
     * AE-qualifier OPTIONAL.
     */
    CALLEDAEQUALIFIER(3),

    /**
     * AP-invocation-identifier OPTIONAL.
     */
    CalledApInvocationId(4),

    /**
     * AE-invocation-identifier OPTIONAL
     */
    CalledAeInvocationId(5),

    /**
     * AP-title OPTIONAL
     */
    CallingApTitle(6),

    /**
     * AE-qualifier OPTIONAL
     */
    CallingAeQualifier(7),

    /**
     * AP-invocation-identifier OPTIONAL
     */
    CallingApInvocationId(8),

    /**
     * AE-invocation-identifier OPTIONAL
     */
    CALLINGAEINVOCATIONID(9),

    /**
     * The following field shall not be present if only the kernel is used.
     */
    SenderAcseRequirements(10),

    /**
     * The following field shall only be present if the authentication
     * functional unit is selected.
     */
    MECHANISMNAME(11),

    /**
     * The following field shall only be present if the authentication
     * functional unit is selected.
     */
    CallingAuthenticationValue(12),

    /**
     * Implementation-data.
     */
    ImplementationInformation(29),

    /**
     * Association-information OPTIONAL
     */
    UserInformation(30);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, PduType> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static java.util.HashMap<Integer, PduType> getMappings() {
        if (mappings == null) {
            synchronized (BaudRate.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, PduType>();
                }
            }
        }
        return mappings;
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Integer value of enumerator.
     */
    PduType(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /**
     * Get integer value for enumerator.
     * 
     * @return Enumerator integer value.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Returns enumerator value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static PduType forValue(final int value) {
        return getMappings().get(value);
    }
}
