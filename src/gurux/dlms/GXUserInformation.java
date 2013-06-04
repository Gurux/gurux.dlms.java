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
class GXUserInformation {    
    
    /**
     * Reserved for internal use.
     */
    byte dlmsVersioNumber;
    
    /**
     * Reserved for internal use.
     */
    int maxReceivePDUSize;
    
    /**
     * Reserved for internal use.
     */
    byte[] conformanceBlock = new byte[3];
    
    /**
     * Reserved for internal use.
     */
    public GXUserInformation()
    {
        maxReceivePDUSize = 0xFFFF;
        dlmsVersioNumber = 6;
    }

    /**
     * Reserved for internal use.
     * @param data
     */
    void codeData(java.nio.ByteBuffer data)
    {
        data.put((byte)0xBE); //Tag
        data.put((byte)0x10); //Length for AARQ user field
        data.put((byte)0x04); //Coding the choice for user-information (Octet STRING, universal)
        data.put((byte)0x0E); //Length
        data.put((byte)GXCommon.InitialRequest); // Tag for xDLMS-Initiate request
        data.put((byte)0x00); // Usage field for dedicated-key component � not used
        data.put((byte)0x00); // Usage field for the response allowed component � not used
        data.put((byte)0x00); // Usage field of the proposed-quality-of-service component � not used
        data.put(dlmsVersioNumber); // Tag for conformance block
        data.put((byte)0x5F);
        data.put((byte)0x1F);
        data.put((byte)0x04);// length of the conformance block
        data.put((byte)0x00);// encoding the number of unused bits in the bit string
        data.put(conformanceBlock);
        data.putShort((short) maxReceivePDUSize);
    }

    /**
     * Reserved for internal use.
     * @param data
     * @throws Exception 
     */
    void encodeData(java.nio.ByteBuffer data)
    {
        int tag = GXCommon.unsignedByteToInt(data.get());
        if (tag != 0xBE)
        {
            throw new GXDLMSException("Invalid tag.");
        }
        int len = GXCommon.unsignedByteToInt(data.get());
        if (data.limit() - data.position() < len)
        {
            throw new GXDLMSException("Not enought data.");
        }
        //Excoding the choice for user information
        tag = GXCommon.unsignedByteToInt(data.get());
        if (tag != 0x4)
        {
            throw new GXDLMSException("Invalid tag.");
        }
        //Skip
        data.get();
        //Tag for xDLMS-Initate.response
        tag = GXCommon.unsignedByteToInt(data.get());
        boolean response = tag == GXCommon.InitialResponce;
        if (response)
        {
            //Optional usage field of the negotiated quality of service component
            tag = data.get();
            if (tag != 0) //Skip if used.
            {
                len = data.get();
                data.position(data.position() + len);
            }
        }
        else if (tag == GXCommon.InitialRequest)
        {
            //Optional usage field of the negotiated quality of service component
            tag = data.get();
            if (tag != 0) //Skip if used.
            {
                len = data.get();
                data.position(data.position() + len);
            }
            //Optional usage field of the negotiated quality of service component
            tag = data.get();
            if (tag != 0) //Skip if used.
            {
                len = data.get();
                data.position(data.position() + len);
            }
            //Optional usage field of the negotiated quality of service component
            tag = data.get();
            if (tag != 0) //Skip if used.
            {
                len = data.get();
                data.position(data.position() + len);
            }
         }
        else
        {
            throw new RuntimeException("Invalid tag.");
        }
        //Get DLMS version number.
        dlmsVersioNumber = data.get();
        //Tag for conformance block
        tag = GXCommon.unsignedByteToInt(data.get());
        if (tag != 0x5F)
        {
            throw new GXDLMSException("Invalid tag.");
        }
        //Old Way...
        if (GXCommon.unsignedByteToInt(data.get(data.position())) == 0x1F)
        {
            data.get();
        }
        //Get len
        GXCommon.unsignedByteToInt(data.get());
        //The number of unused bits in the bit string.
        //Get tag
        GXCommon.unsignedByteToInt(data.get());
        data.get(conformanceBlock, 0, 3);
        maxReceivePDUSize = data.getShort() & 0xFFFF;
        if (response)
        {
            //get VAA Name
            data.getShort();
        }
    }
}
