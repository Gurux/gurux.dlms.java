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

package gurux.dlms.objects;

public class GXDLMSImageActivateInfo 
{
    long Size;
    String Identification;
    String Signature;
        
     /** 
      *  Image_size is the size of the Image(s) to be activated. 
      *  Expressed in octets;
    */
    public final long getSize()
    {
        return Size;
    }
    public final void setSize(long value)
    {
        Size = value;
    }
    
     /** 
      * Image identification is the identification of the Image(s)
      * to be activated, and may contain information like
      * manufacturer, device type, version information, etc.
    */
    public final String getIdentification()
    {
        return Identification;
    }
    public final void setIdentification(String value)
    {
        Identification = value;
    }
    
     /** 
      * Image signature is the signature of the Image(s) to be activated.
    */
    public final String getSignature()
    {
        return Signature;
    }
    public final void setSignature(String value)
    {
        Signature = value;
    }
    
    /**
     * Constructor.
     */
    public GXDLMSImageActivateInfo()
    {
        
    }
    
    /**
     * Constructor.
     */
    public GXDLMSImageActivateInfo(long size, String identification, String signature)
    {
        Size = size;
        Identification = identification;
        Signature = signature;
    }
    
    
    @Override
    public String toString()
    {
        return Identification + " " + Signature + " " + String.valueOf(Size);        
    }
}