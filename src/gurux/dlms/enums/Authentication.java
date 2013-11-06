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
 Authentication enumerates the authentication levels.
*/
public enum Authentication
{
    /** 
     No authentication is used.
    */
    NONE,
    /** 
     Low authentication is used.
    */
    LOW,
    /** 
     High authentication is used.
    */
    HIGH,
    /*
     * High authentication is used. Password is hashed with MD5.
     */
    HIGH_MD5,
    /*
     * High authentication is used. Password is hashed with SHA1.
     */
    HIGH_SHA1,
    /*
     * High authentication is used. Password is hashed with GMAC.
     */
    HIGH_GMAC;

    /*
     * Get integer value for enum.
     */
    public int getValue()
    {
        return this.ordinal();
    }

    /*
     * Convert integer for enum value.
     */
    public static Authentication forValue(int value)
    {
        return values()[value];
    }
}