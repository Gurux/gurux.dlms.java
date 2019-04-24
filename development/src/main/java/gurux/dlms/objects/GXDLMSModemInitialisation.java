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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

public class GXDLMSModemInitialisation {
    private String request;
    private String response;
    private int delay;

    public final String getRequest() {
        return request;
    }

    public final void setRequest(final String value) {
        request = value;
    }

    public final String getResponse() {
        return response;
    }

    public final void setResponse(final String value) {
        response = value;
    }

    public final int getDelay() {
        return delay;
    }

    public final void setDelay(final int value) {
        delay = value;
    }

    @Override
    public final String toString() {
        return request + " " + response + " " + delay;
    }
}