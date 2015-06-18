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
import java.lang.reflect.Array;
import java.util.List;

/** 
 GXDLMSLimits contains commands for retrieving and setting the limits of 
 field length and window size, when communicating with the server. 
*/
public class GXDLMSLimits
{
    static final Object DefaultMaxInfoRX = (byte)128;        
    static final Object DefaultMaxInfoTX = (byte)128;
    static final Object DefaultWindowSizeRX = (byte)1;
    static final Object DefaultWindowSizeTX = (byte)1;

    static void setValue(java.nio.ByteBuffer buff, Object data)
    {
        try{
            byte[] tmp = GXCommon.getAsByteArray(data);
            buff.put((byte)tmp.length);
            buff.put(tmp);    
        }
        catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }        
    }
         
    private Object MaxInfoTX;
    private Object MaxInfoRX;        
    private Object WindowSizeTX;
    private Object WindowSizeRX;

    /*
     * Constructor.
     */
    GXDLMSLimits()
    {
        setMaxInfoTX((byte)128);
        setMaxInfoRX((byte)128);
        setWindowSizeTX((byte) 1);
        setWindowSizeRX(getWindowSizeTX());
    }

    /** 
     The maximum information field length in transmit. 	 
     DefaultValue is 128.	 
    */
    public final Object getMaxInfoTX()
    {
        return MaxInfoTX;
    }
    public final void setMaxInfoTX(Object value)
    {
        MaxInfoTX = value;
    }

    /** 
     The maximum information field length in receive.


     DefaultValue is 62.

    */
    public final Object getMaxInfoRX()
    {
        return MaxInfoRX;
    }
    public final void setMaxInfoRX(Object value)
    {
        MaxInfoRX = value;
    }

    /** 
     The window size in transmit.


     DefaultValue is 1.

    */
    public final Object getWindowSizeTX()
    {
        return WindowSizeTX;
    }
    public final void setWindowSizeTX(Object value)
    {
        WindowSizeTX = value;
    }

    /** 
     The window size in receive.


     DefaultValue is 1.

    */
    public final Object getWindowSizeRX()
    {
        return WindowSizeRX;
    }
    public final void setWindowSizeRX(Object value)
    {
        WindowSizeRX = value;
    }
}