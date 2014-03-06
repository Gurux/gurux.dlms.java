//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: http://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------
package gurux.dlms.objects;

public class GXDLMSModemInitialisation
{
    private String Request;
    private String Response;
    private int Delay;
    
    public final String getRequest()
    {
        return Request;
    }
    public final void setRequest(String value)
    {
        Request = value;
    }

    public final String getResponse()
    {
        return Response;
    }
    public final void setResponse(String value)
    {
        Response = value;
    }

    public final int getDelay()
    {
        return Delay;
    }

    public final void setDelay(int value)
    {
        Delay = value;
    }
    
    @Override 
    public String toString()
    {
        return Request + " " + Response + " " + Delay;
    }
}