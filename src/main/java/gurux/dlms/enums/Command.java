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

/**
 * DLMS commands.
 * 
 * @author Gurux Ltd.
 */
public enum Command {
    /**
     * No command to execute.
     */
    NONE(0),

    /**
     * Read request.
     */
    READ_REQUEST(0x5),

    /**
     * Read response.
     */
    READ_RESPONSE(0xC),

    /**
     * Write request.
     */
    WRITE_REQUEST(0x6),

    /**
     * Write response.
     */
    WRITE_RESPONSE(0xD),

    /**
     * Get request.
     */
    GET_REQUEST(0xC0),

    /**
     * Get response.
     */
    GET_RESPONSE(0xC4),

    /**
     * Set request.
     */
    SET_REQUEST(0xC1),

    /**
     * Set response.
     */
    SET_RESPONSE(0xC5),

    /**
     * Action request.
     */
    METHOD_REQUEST(0xC3),

    /**
     * Action response.
     */
    METHOD_RESPONSE(0xC7),

    /**
     * Command rejected.
     */
    REJECTED(0x97),

    /**
     * SNRM request.
     */
    SNRM(0x93),

    /**
     * UA request.
     */
    UA(0x73),

    /**
     * AARQ request.
     */
    AARQ(0x60),

    /**
     * AARE request.
     */
    AARE(0x61),

    /**
     * Disconnect request.
     */
    DISCONNECT_REQUEST(0x62),

    /**
     * Disconnect response.
     */
    DISCONNECT_RESPONSE(0x63),

    /**
     * Exception Response.
     */
    EXCEPTION_RESPONSE(0xD8),

    /**
     * Push request.
     */
    PUSH(0xE0),

    /**
     * Glo get request.
     */
    GLO_GET_REQUEST(0xC8),

    /**
     * Glo get response.
     */
    GLO_GET_RESPONSE(0xCC),

    /**
     * Glo set request.
     */
    GLO_SET_REQUEST(0xC9),

    /**
     * Glo set response.
     */
    GLO_SET_RESPONSE(0xCD),

    /**
     * Glo method request.
     */
    GLO_METHOD_REQUEST(0xCB),

    /**
     * Glo method response.
     */
    GLO_METHOD_RESPONSE(0xCF);

    /**
     * Integer value of enumerator.
     */
    private int intValue;
    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, Command> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static java.util.HashMap<Integer, Command> getMappings() {
        if (mappings == null) {
            synchronized (Command.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, Command>();
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
    Command(final int value) {
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
    public static Command forValue(final int value) {
        Command cmd = getMappings().get(value);
        if (cmd == null) {
            throw new IllegalArgumentException("Invalid Command.");
        }
        return cmd;
    }
}