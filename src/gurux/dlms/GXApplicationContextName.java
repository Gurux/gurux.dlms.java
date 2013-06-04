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

import gurux.dlms.internal.GXCommon;

/**
 * Reserved for internal use.
 */
class GXApplicationContextName
{
    /**
     * Reserved for internal use.
     */    
    public boolean useLN = false;
    
    /**
     * Reserved for internal use.
     * @param data 
     */
    void codeData(java.nio.ByteBuffer data)
    {        
        //Application context name tag
        data.put((byte) 0xA1);
        //Len
        data.put((byte) 0x09);
        data.put((byte) 0x06);
        data.put((byte) 0x07);
        if (useLN)
        {
            data.put(GXCommon.LogicalNameObjectID);
        }
        else
        {
            data.put(GXCommon.ShortNameObjectID);
        }        
    }

    /**
     * Reserved for internal use.
     * @param buff
     * @throws Exception 
     */
    void encodeData(java.nio.ByteBuffer buff)
    {        
        int tag = GXCommon.unsignedByteToInt(buff.get());
        if (tag != 0xA1)
        {
            throw new GXDLMSException("Invalid tag.");
        }
        //Get length.
        int len = GXCommon.unsignedByteToInt(buff.get());
        if (buff.limit() - buff.position() < len)
        {
            throw new GXDLMSException("Encoding failed. Not enought data.");
        }
        buff.get();
        //Get length.
        GXCommon.unsignedByteToInt(buff.get());
        useLN = GXCommon.compare(buff, GXCommon.LogicalNameObjectID);
        if (!useLN)
        {
            buff.position(buff.position() + GXCommon.LogicalNameObjectID.length);
        }
    }
};