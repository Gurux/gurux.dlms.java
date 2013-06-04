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
 GXDLMSTag is a key value pair to retrieve or set extra communication parameters, 
 when establishing a connection to the DLMS/COSEM metering device.
*/
public class GXDLMSTag
{
    private byte[] privateData;
    private int privateID;

    /** 
     Identification of the key valprivatedataue.
    */
    public final int getID()
    {
        return privateID;
    }
    public final void setID(int value)
    {
        privateID = value;
    }

    /** 
     The value of the key value.
    */
    public final byte[] getData()
    {
        return privateData;
    }
    public final void setData(byte[] value)
    {
        privateData = value;
    }
}