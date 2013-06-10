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

enum Command
{
    None(0),
    ReadRequest(0x5),
    ReadResponse(0xC),
    WriteRequest(0x6),
    WriteResponse(0xD),
    GetRequest(0xC0),
    GetResponse(0xC4),
    SetRequest(0xC1),
    SetResponse(0xC5),
    MethodRequest(0xC3),
    MethodResponse(0xC7),
    REJECTED(0x97),
    Snrm(0x93),
    Aarq(0x60),
    DisconnectRequest(0x62),
    DisconnectResponse(0x63);

    private int intValue;
    private static java.util.HashMap<Integer, Command> mappings;
    private static java.util.HashMap<Integer, Command> getMappings()
    {
        synchronized (Command.class)
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, Command>();
            }
        }
        return mappings;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private Command(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    /*
     * Get integer value for enum.
     */
    public int getValue()
    {
        return intValue;
    }

    /*
     * Convert integer for enum value.
     */
    public static Command forValue(int value)
    {
        return getMappings().get(value);
    }
}