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
 GXDLMSLimits contains commands for retrieving and setting the limits of 
 field length and window size, when communicating with the server. 
*/
public class GXDLMSLimits
{
    private Object privateMaxInfoTX;
    private Object privateMaxInfoRX;        
    private Object privateWindowSizeTX;
    private Object privateWindowSizeRX;

    /*
     * Constructor.
     */
    GXDLMSLimits()
    {
        setMaxInfoTX((short)128);
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
            return privateMaxInfoTX;
    }
    public final void setMaxInfoTX(Object value)
    {
            privateMaxInfoTX = value;
    }

    /** 
     The maximum information field length in receive.


     DefaultValue is 62.

    */
    public final Object getMaxInfoRX()
    {
        return privateMaxInfoRX;
    }
    public final void setMaxInfoRX(Object value)
    {
        privateMaxInfoRX = value;
    }

    /** 
     The window size in transmit.


     DefaultValue is 1.

    */
    public final Object getWindowSizeTX()
    {
        return privateWindowSizeTX;
    }
    public final void setWindowSizeTX(Object value)
    {
        privateWindowSizeTX = value;
    }

    /** 
     The window size in receive.


     DefaultValue is 1.

    */
    public final Object getWindowSizeRX()
    {
        return privateWindowSizeRX;
    }
    public final void setWindowSizeRX(Object value)
    {
        privateWindowSizeRX = value;
    }
}