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

package gurux.dlms.enums;

import java.util.HashMap;

/**
 * Enumerated DLMS error codes.
 * 
 * @author Gurux Ltd.
 */
public enum ErrorCode {

    /**
     * Disconnect Mode.
     */
    DISCONNECT_MODE(-4),
    /**
     * Receive Not Ready.
     */
    RECEIVE_NOT_READY(-3),

    /**
     * Connection is rejected.
     */
    REJECTED(-2),

    /**
     * Unacceptable frame.
     */
    UNACCEPTABLE_FRAME(-1),

    /**
     * No error has occurred.
     */
    OK(0),

    /**
     * Access Error : Device reports a hardware fault.
     */
    HARDWARE_FAULT(1),

    /**
     * Access Error : Device reports a temporary failure.
     */
    TEMPORARY_FAILURE(2),

    /**
     * Access Error : Device reports Read-Write denied.
     */
    READ_WRITE_DENIED(3),

    /**
     * Access Error : Device reports a undefined object
     */
    UNDEFINED_OBJECT(4),
    /**
     * Access Error : Device reports a inconsistent Class or object
     */
    INCONSISTENT_CLASS(9),

    /**
     * Access Error : Device reports a unavailable object
     */
    UNAVAILABLE_OBJECT(11),

    /**
     * Access Error : Device reports a unmatched type
     */
    UNMATCHED_TYPE(12),

    /**
     * Access Error : Device reports scope of access violated
     */
    ACCESS_VIOLATED(13),

    /**
     * Access Error : Data Block Unavailable.
     */
    DATA_BLOCK_UNAVAILABLE(14),

    /**
     * Access Error : Long Get Or Read Aborted.
     */
    LONG_GET_OR_READ_ABORTED(15),

    /**
     * Access Error : No Long Get Or Read In Progress.
     */
    NO_LONG_GET_OR_READ_IN_PROGRESS(16),

    /**
     * Access Error : Long Set Or Write Aborted.
     */
    LONG_SET_OR_WRITE_ABORTED(17),

    /**
     * Access Error : No Long Set Or Write In Progress.
     */
    NO_LONG_SET_OR_WRITE_IN_PROGRESS(18),

    /**
     * Access Error : Data Block Number Invalid.
     */
    DATA_BLOCK_NUMBER_INVALID(19),

    /**
     * Access Error : Other Reason.
     */
    OTHER_REASON(250);

    /**
     * Integer value of enumeration.
     */
    private int intValue;

    /**
     * Collection of integer and enumeration values.
     */
    private static java.util.HashMap<Integer, ErrorCode> mappings;

    /**
     * Get mappings.
     * 
     * @return Hash map of enumeration and integer values.
     */
    private static HashMap<Integer, ErrorCode> getMappings() {
        synchronized (ErrorCode.class) {
            if (mappings == null) {
                mappings = new HashMap<Integer, ErrorCode>();
            }
        }
        return mappings;
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Integer value for enumerator.
     */
    ErrorCode(final int value) {
        intValue = value;
        synchronized (ErrorCode.class) {
            getMappings().put(new Integer(value), this);
        }
    }

    /**
     * Get enemerator's integer value.
     * 
     * @return Integer value of enumerator.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Get enumerator from integer value.
     * 
     * @param value
     *            integer value.
     * @return Enumerator value.
     */
    public static ErrorCode forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}
