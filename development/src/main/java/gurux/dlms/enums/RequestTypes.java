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

/**
 * RequestTypes enumerates the replies of the server to a client's request,
 * indicating the request type.
 */
public enum RequestTypes {
    /**
     * No more data is available for the request.
     */
    NONE(0),

    /**
     * More data blocks are available for the request.
     */
    DATABLOCK(1),

    /**
     * More data frames are available for the request.
     */
    FRAME(2),

    /**
     * More data frames and data blocks are available for the request.
     */
    BOTH(3);

    /**
     * Integer value of enumerator.
     */
    private int intValue;

    /**
     * Collection of enumerator values.
     */
    private static java.util.HashMap<Integer, RequestTypes> mappings;

    /**
     * Returns collection of enumerator values.
     * 
     * @return Enumerator values.
     */
    private static java.util.HashMap<Integer, RequestTypes> getMappings() {
        if (mappings == null) {
            synchronized (RequestTypes.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, RequestTypes>();
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
    RequestTypes(final int value) {
        intValue = value;
        getMappings().put(new Integer(value), this);
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
    public static RequestTypes forValue(final int value) {
        return getMappings().get(new Integer(value));
    }
}