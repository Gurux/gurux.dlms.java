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

/**
 * Reserved for internal use.
 */
package gurux.dlms.enums;

import java.util.EnumSet;

/**
 * <b>RequestTypes</b> enumerates the replies of the server to a client's request,
 * indicating the request type.
 */
public enum RequestTypes
{
    /**
     * No more data is available for the request.
     * <p />Integer value=0
     */
    NONE(0),
    
    /**
     * More data blocks are available for the request.
     * <p />Integer value=1
     */
    DATABLOCK(1),
    
    /**
     * More data frames are available for the request.
     * <p />Integer value=2
     */
    FRAME(2);    
    
    /**
     * Reserved for internal use.
     */
    int value;
    /*
     Constructor.
     */
    RequestTypes(int value)
    {
        this.value = value;
    }

    /**
     * Retrieves the value that indicates the request type.
     * @return Value of request type.
     */
    public int getValue()
    {
        return this.value;
    }
    
    public static boolean contains(Object o)
    {
        return false;
    }

    /**
     * Converts the integer value to enumerated value.  
     * @param value The integer value, which is read from the device.
     * @return The enumerated value, which represents the integer.
     * @throws Exception If the interger has no representative in enumerated
     * values, an exception is thrown.
     */
    public static java.util.Set<RequestTypes> forValue(int value) throws Exception
    {
        EnumSet<RequestTypes> types = EnumSet.of(NONE);
        RequestTypes[] enums = RequestTypes.class.getEnumConstants();
        for(int pos = 0; pos != enums.length; ++pos)
        {
            if ((enums[pos].value & value) == enums[pos].value)
            {
                types.add(enums[pos]);
            }
        }
        return types;
    }
}