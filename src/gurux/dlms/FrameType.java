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

/**
 * Reserved for internal use.
 */
enum FrameType
{
    //////////////////////////////////////////////////////////
    // This command is used to set the secondary station in connected mode and reset
    // its sequence number variables.
    SNRM(0x93), // Set Normal Response Mode (SNRM)
    //////////////////////////////////////////////////////////
    // This response is used to confirm that the secondary received and acted on an SNRM or DISC command.
    UA(0x73), // Unnumbered Acknowledge (UA)
    //////////////////////////////////////////////////////////
    // This command and response is used to transfer a block of data together with its sequence number.
    // The command also includes the sequence number of the next frame the transmitting station expects to see.
    // This way, it works as an RR. Like RR, it enables transmission of I frames from the opposite side.
    Information(0x10), // Information (I)
    //////////////////////////////////////////////////////////
    // This response is used to indicate an error condition. The two most likely error conditions are:
    // Invalid command or Sequence number problem.
    Rejected(0x97),  // Frame Reject
    //////////////////////////////////////////////////////////
    // This command is used to terminate the connection.
    Disconnect(0x53),
    //////////////////////////////////////////////////////////
    // This response is used to inform the primary station that the secondary is disconnected.
    DisconnectMode(0x1F); // Disconnected Mode

    /**
     * Reserved for internal use.
     */
    byte value;
    
    /*
     Constructor.
     */
    FrameType(int value)
    {
        this.value = (byte) value;
    }

    /**
     * Retrieves the frame type.
     * @return The frame type.
     */
    public byte getValue()
    {
        return this.value;
    }
}