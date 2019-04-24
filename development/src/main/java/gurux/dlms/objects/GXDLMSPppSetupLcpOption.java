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
package gurux.dlms.objects;

import gurux.dlms.objects.enums.PppSetupLcpOptionType;

public class GXDLMSPppSetupLcpOption {
    private PppSetupLcpOptionType type;
    private Object data;
    private int length;

    public final PppSetupLcpOptionType getType() {
        return type;
    }

    public final void setType(final PppSetupLcpOptionType value) {
        type = value;
    }

    public final int getLength() {
        return length;
    }

    public final void setLength(final int value) {
        length = value;
    }

    public final Object getData() {
        return data;
    }

    public final void setData(final Object value) {
        data = value;
    }

    @Override
    public final String toString() {
        return getType().toString() + " " + String.valueOf(getData());
    }
}