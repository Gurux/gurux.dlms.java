//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: http://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------
package gurux.dlms.enums;

/** 
 Defines the minimum time between the reception of a request 
(end of request telegram) and the transmission of the response (begin of response telegram).
*/
public enum LocalPortResponseTime
{
    /** 
     Minimium time is 20 ms.
    */
    ms20(0),
    /** 
     Minimium time is 200 ms.
    */
    ms200(1);

    private int intValue;
    private static java.util.HashMap<Integer, LocalPortResponseTime> mappings;
    private static java.util.HashMap<Integer, LocalPortResponseTime> getMappings()
    {
        synchronized (LocalPortResponseTime.class)
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, LocalPortResponseTime>();
            }
        }
        return mappings;
    }

    private LocalPortResponseTime(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return intValue;
    }

    public static LocalPortResponseTime forValue(int value)
    {
        return getMappings().get(value);
    }
}