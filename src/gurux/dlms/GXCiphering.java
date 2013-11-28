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

/** 
    Gurux DLMS/COSEM Transport security (Ciphering) settings.
*/
public class GXCiphering
{
    private Security m_Security;
    private byte[] m_AuthenticationKey;
    private byte[] m_SystemTitle;
    private byte[] m_BlockCipherKey;
    long m_FrameCounter = 0;

    /** 
     Constructor.
     Default values are from the Green Book.
     @param frameCounter Default frame counter value. Set to Zero.
     @param systemTitle
     @param blockCipherKey
     @param authenticationKey
    */
    public GXCiphering(byte[] systemTitle)
    {
        setSecurity(Security.NONE);
        setSystemTitle(systemTitle);
        setBlockCipherKey(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 
            0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F});
        setAuthenticationKey(new byte[] {(byte) 0xD0, (byte)0xD1, (byte)0xD2, 
            (byte)0xD3, (byte)0xD4, (byte)0xD5, (byte)0xD6, (byte)0xD7, 
            (byte)0xD8, (byte)0xD9, (byte)0xDA, (byte)0xDB, (byte)0xDC, 
            (byte)0xDD, (byte)0xDE, (byte)0xDF});
    }

    /** 
     Constructor.

     Default values are from the Green Book.

     @param frameCounter Default frame counter value. Set to Zero.
     @param systemTitle
     @param blockCipherKey
     @param authenticationKey
    */
    public GXCiphering(long frameCounter, byte[] systemTitle, 
            byte[] blockCipherKey, byte[] authenticationKey)
    {
        setSecurity(Security.NONE);
        m_FrameCounter = frameCounter;
        setSystemTitle(systemTitle);
        setBlockCipherKey(blockCipherKey);
        setAuthenticationKey(authenticationKey);
    }

    /** 
     Used security.
    */        
    public final long getFrameCounter()
    {
        return m_FrameCounter;
    }
    public final void setFrameCounter(long value)
    {
        m_FrameCounter = value;
    }
    
    /** 
     Used security.
    */        
    public final Security getSecurity()
    {
        return m_Security;
    }
    public final void setSecurity(Security value)
    {
        m_Security = value;
    }

    /** 
     The SystemTitle is a 8 bytes (64 bit) value that identifies a partner of the communication. 
     First 3 bytes contains the three letters manufacturer ID.
     The remainder of the system title holds for example a serial number. 
     <seealso href="http://www.dlms.com/organization/flagmanufacturesids">List of manufacturer ID.
    */
    public final byte[] getSystemTitle()
    {
        return m_SystemTitle;
    }

    public final void setSystemTitle(byte[] value)
    {
        if (value != null && value.length != 8)
        {
            throw new IllegalArgumentException("Invalid System Title.");
        }
        m_SystemTitle = value;
    }

    /** 
     Each block is ciphered with this key.
    */
    public final byte[] getBlockCipherKey()
    {
        return m_BlockCipherKey;
    }
    public final void setBlockCipherKey(byte[] value)
    {
        if (value != null && value.length != 16)
        {
            throw new IllegalArgumentException("Invalid Block Cipher Key.");
        }
        m_BlockCipherKey = value;
    }

    /** 
     Authentication Key is 16 bytes value.
    */
    public final byte[] getAuthenticationKey()
    {
        return m_AuthenticationKey;
    }
    public final void setAuthenticationKey(byte[] value)
    {
        if (value != null && value.length != 16)
        {
            throw new IllegalArgumentException("Invalid Authentication Key.");
        }
        m_AuthenticationKey = value;
    }
}