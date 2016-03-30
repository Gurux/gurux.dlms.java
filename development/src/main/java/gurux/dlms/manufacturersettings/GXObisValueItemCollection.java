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

package gurux.dlms.manufacturersettings;

import java.util.ArrayList;

public class GXObisValueItemCollection extends ArrayList<GXObisValueItem> {
    private static final long serialVersionUID = 1L;

    @Override
    public final void add(final int index, final GXObisValueItem item) {
        if (!this.contains(item)) {
            super.add(index, item);
        }
    }

    @Override
    public final boolean add(final GXObisValueItem item) {
        if (!this.contains(item)) {
            return super.add(item);
        }
        return false;
    }

    public final boolean contains(final GXObisValueItem item) {
        for (GXObisValueItem it : this) {
            if (it.getValue().equals(item.getValue())) {
                return true;
            }
        }
        return false;
    }
}