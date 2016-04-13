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

import gurux.dlms.enums.AssociationResult;

/**
 * Reserved for internal use.
 */
enum FrameType {
    /**
     * This command is used to set the secondary station in connected mode and
     * reset its sequence number variables.
     */
    SNRM(0x93), // Set Normal Response Mode (SNRM)

    /**
     * This response is used to confirm that the secondary received and acted on
     * an SNRM or DISC command.
     */
    UA(0x73), // Unnumbered Acknowledge (UA)

    /**
     * This command and response is used to transfer a block of data together
     * with its sequence number.The command also includes the sequence number of
     * the next frame the transmitting station expects to see. This way, it
     * works as an RR. Like RR, it enables transmission of I frames from the
     * opposite side.
     */
    AARQ(0x10),

    /**
     * AARE frame.
     */
    AARE(0x30),

    /**
     * This response is used to indicate an error condition. The two most likely
     * error conditions are: Invalid command or Sequence number problem.
     */
    REJECTED(0x97),

    /**
     * This command is used to terminate the connection.
     */
    DISCONNECT_REQUEST(0x53),

    /**
     * This command is used to terminate the connection.
     */
    DISCONNECT_RESPONSE(0x52),

    /**
     * This response is used to inform the primary station that the secondary is
     * disconnected.
     */
    DISCONNECT_MODE(0x1F); // Disconnected Mode

    private final int intValue;
    private static java.util.HashMap<Integer, FrameType> mappings;

    private static java.util.HashMap<Integer, FrameType> getMappings() {
        synchronized (AssociationResult.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, FrameType>();
            }
        }
        return mappings;
    }

    FrameType(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
    }

    /*
     * Get integer value for enum.
     */
    public int getValue() {
        return intValue;
    }

    /*
     * Convert integer for enum value.
     */
    public static FrameType forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}