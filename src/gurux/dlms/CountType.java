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
package gurux.dlms;

enum CountType
{
    TAG(0x1),
    DATA(0x2),
    PACKET(0x3);

    private int Value;
    private static java.util.HashMap<Integer, CountType> mappings;
    private static java.util.HashMap<Integer, CountType> getMappings()
    {
        if (mappings == null)
        {
            synchronized (CountType.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, CountType>();
                }
            }
        }
        return mappings;
    }

    private CountType(int value)
    {
        Value = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return Value;
    }

    public static CountType forValue(int value)
    {
        return getMappings().get(value);
    }
}