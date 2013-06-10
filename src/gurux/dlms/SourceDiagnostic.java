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
 SourceDiagnostic enumerates the error codes for reasons that can cause the server to reject the client.
*/
enum SourceDiagnostic
{
    /** 
     An unknown error has occurred.
    */
    NONE(0),
    /** 
     No reason is given.
    */
    NO_REASON_GIVEN(1),
    /** 
     The application context name is not supported. 
    */
    APPLICATION_CONTEXT_NAME_NOT_SUPPORTED(2),
    /** 
     The authentication mechanism name is not recognized.
    */
    AUTHENTICATION_MECHANISM_NAME_NOT_RECOGNISED(11),
    /** 
     Authentication mechanism name is required.
    */
    AUTHENTICATION_MECHANISM_NAME_REGUIRED(12),
    /** 
     Authentication failure.
    */
    AUTHENTICATION_FAILURE(13),
    /** 
     Authentication is required.
    */
    AUTHENTICATION_REQUIRED(14);

    private int intValue;
    private static java.util.HashMap<Integer, SourceDiagnostic> mappings;
    private static java.util.HashMap<Integer, SourceDiagnostic> getMappings()
    {
        synchronized (SourceDiagnostic.class)
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, SourceDiagnostic>();
            }
        }
        return mappings;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private SourceDiagnostic(int value)
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
    public static SourceDiagnostic forValue(int value)
    {
        return getMappings().get(value);
    }
}