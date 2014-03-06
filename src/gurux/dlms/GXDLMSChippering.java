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

import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class GXDLMSChippering
{       
    /** 
     Get Nonse from frame counter and system title.

     @param FrameCounter Frame counter.
     @param systemTitle System title.
     @return 
    */
    private static byte[] GetNonse(long FrameCounter, byte[] systemTitle)
    {
        byte[] nonce = new byte[12];
        System.arraycopy(systemTitle, 0, nonce, 0, systemTitle.length);
        nonce[8] = (byte) ((FrameCounter >> 24) & 0xFF);
        nonce[9] = (byte) ((FrameCounter >> 16) & 0xFF);
        nonce[10] = (byte) ((FrameCounter >> 8) & 0xFF);
        nonce[11] = (byte) (FrameCounter & 0xFF);                
        return nonce;
    }

    static byte[] EncryptAesGcm(Command command, Security security, long FrameCounter, byte[] systemTitle, byte[] BlockCipherKey, byte[] AuthenticationKey, byte[] plainText)
    {
        Object[] tag = new Object[1];
        byte[] tempVar = EncryptAesGcm(command, security, FrameCounter, systemTitle, BlockCipherKey, AuthenticationKey, plainText, CountType.PACKET, tag);
        return tempVar;
    }
       
    private static byte[] EncryptAesGcm(Command command, Security security, long FrameCounter, byte[] systemTitle, byte[] BlockCipherKey, byte[] AuthenticationKey, byte[] plainText, CountType type, Object[] countTag)
    {
        try
        {
            countTag[0] = null;
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            if (type == CountType.PACKET)
            {
                data.write(security.getValue());
            }        
            byte[] tmp = new byte[4];
            tmp[0] = (byte) ((FrameCounter >> 24) & 0xFF);
            tmp[1] = (byte) ((FrameCounter >> 16) & 0xFF);
            tmp[2] = (byte) ((FrameCounter >> 8) & 0xFF);
            tmp[3] = (byte) (FrameCounter & 0xFF);                                    
            byte[] aad = GetAuthenticatedData(security, AuthenticationKey, plainText);
            GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(security, true, BlockCipherKey, aad, GetNonse(FrameCounter, systemTitle), null);
            // Encrypt the secret message
            if (security != Security.AUTHENTICATION)
            {
                gcm.Write(plainText);
            }
            byte[] ciphertext = gcm.FlushFinalBlock();
            if (security == Security.AUTHENTICATION)
            {
                if (type == CountType.PACKET)
                {
                    data.write(tmp);
                }
                if ((type.getValue() & CountType.DATA.getValue()) != 0)
                {
                    data.write(plainText);
                }
                if ((type.getValue() & CountType.TAG.getValue()) != 0)
                {
                    countTag[0] = gcm.GetTag();
                    data.write((byte[])countTag[0]);
                }
            }
            else if (security == Security.ENCRYPTION)
            {
                data.write(tmp);
                data.write(ciphertext);
            }
            else if (security == Security.AUTHENTICATION_ENCRYPTION)
            {
                if (type == CountType.PACKET)
                {
                    data.write(tmp);
                }
                if ((type.getValue() & CountType.DATA.getValue()) != 0)
                {
                    data.write(ciphertext);
                }
                if ((type.getValue() & CountType.TAG.getValue()) != 0)
                {
                    countTag[0] = gcm.GetTag();
                    data.write((byte[]) countTag[0]);
                }
            }
            else
            {
                throw new IllegalArgumentException("security");
            }
            if (type == CountType.PACKET)
            {
                ByteArrayOutputStream tmp2 = new ByteArrayOutputStream(10 + data.size());                
                tmp2.write(command.getValue());
                gurux.dlms.internal.GXCommon.setObjectCount(data.size(), tmp2);
                data.writeTo(tmp2);
                return tmp2.toByteArray();
            }
            return data.toByteArray();
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private static byte[] GetAuthenticatedData(Security security, byte[] AuthenticationKey, byte[] plainText)
    {
        try
        {
            if (security == Security.AUTHENTICATION)
            {
                ByteArrayOutputStream tmp2 = new ByteArrayOutputStream();
                tmp2.write((byte)security.getValue());
                tmp2.write(AuthenticationKey);
                tmp2.write(plainText);
                return tmp2.toByteArray();
            }
            else if (security == Security.ENCRYPTION)
            {
                return AuthenticationKey;
            }
            else if (security == Security.AUTHENTICATION_ENCRYPTION)
            {
                ByteArrayOutputStream tmp2 = new ByteArrayOutputStream();
                tmp2.write((byte)security.getValue());
                tmp2.write(AuthenticationKey);
                return tmp2.toByteArray();
            }
            return null;
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /** 
     Decrypt data.

     @param cryptedText Crypted data.
     @param systemTitle
     @param BlockCipherKey
     @param AuthenticationKey
     @return 
    */
    static byte[] DecryptAesGcm(byte[] cryptedText, byte[] systemTitle, byte[] blockCipherKey, byte[] authenticationKey)
    {
        if (cryptedText == null || cryptedText.length < 2)
        {
            throw new IllegalArgumentException("cryptedData");
        }
        int[] pos = new int[1];
        Command cmd = Command.forValue(cryptedText[pos[0]++] & 0xFF);
        if (!(cmd == Command.GloGetRequest || cmd == Command.GloGetResponse || 
                cmd == Command.GloSetRequest || cmd == Command.GloSetResponse || 
                cmd == Command.GloMethodRequest || cmd == Command.GloMethodResponse))
        {
            throw new IllegalArgumentException("cryptedData");
        }
        int len = GXCommon.getObjectCount(cryptedText, pos);
        Security security = Security.forValue(cryptedText[pos[0]++]);
        byte[] FrameCounterData = new byte[4];
        FrameCounterData[0] = cryptedText[pos[0]++];
        FrameCounterData[1] = cryptedText[pos[0]++];
        FrameCounterData[2] = cryptedText[pos[0]++];
        FrameCounterData[3] = cryptedText[pos[0]++];
        int[] index = new int[1];
        long frameCounter = (long) gurux.dlms.internal.GXCommon.getUInt32(FrameCounterData, index);
        byte[] tag = new byte[12];
        byte[] encryptedData;
        int length;
        if (security == Security.AUTHENTICATION)
        {
            length = cryptedText.length - pos[0] - 12;
            encryptedData = new byte[length];
            System.arraycopy(cryptedText, pos[0], encryptedData, 0, length);
            pos[0] += length;
            System.arraycopy(cryptedText, pos[0], tag, 0, 12);
            //Check tag.
            byte[] countTag;
            Object[] tmp = new Object[1];
            EncryptAesGcm(Command.None, security, frameCounter, systemTitle, blockCipherKey, authenticationKey, encryptedData, CountType.PACKET, tmp);
            countTag = (byte[]) tmp[0];
            if (!GXDLMSChipperingStream.TagsEquals(tag, countTag))
            {
                throw new GXDLMSException("Decrypt failed. Invalid tag.");
            }
            return encryptedData;
        }
        byte[] ciphertext = null;
        if (security == Security.ENCRYPTION)
        {
            length = cryptedText.length - pos[0];
            ciphertext = new byte[length];
            System.arraycopy(cryptedText, pos[0], ciphertext, 0, ciphertext.length);
            pos[0] += ciphertext.length;
        }
        else if (security == Security.AUTHENTICATION_ENCRYPTION)
        {
            length = cryptedText.length - pos[0] - 12;
            ciphertext = new byte[length];
            System.arraycopy(cryptedText, pos[0], ciphertext, 0, ciphertext.length);
            pos[0] += ciphertext.length;
            System.arraycopy(cryptedText, pos[0], tag, 0, 12);
            pos[0] += tag.length;
        }
        byte[] aad = GetAuthenticatedData(security, authenticationKey, cryptedText);
        GXDLMSChipperingStream gcm = new GXDLMSChipperingStream(security, false, blockCipherKey, aad, GetNonse(frameCounter, systemTitle), tag);
        gcm.Write(ciphertext);
        return gcm.FlushFinalBlock();
    }
}
