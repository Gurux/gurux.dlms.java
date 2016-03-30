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

public class GXAttributeCollection
        extends java.util.ArrayList<GXDLMSAttributeSettings> {
    private static final long serialVersionUID = 1L;
    private Object parent;

    /**
     * @return Parent object.
     */
    public final Object getParent() {
        return parent;
    }

    public final void setParent(final Object value) {
        parent = value;
    }

    public final GXDLMSAttributeSettings find(final int index) {
        if (index < 1) {
            throw new IllegalArgumentException("Invalid attribute Index.");
        }
        for (GXDLMSAttributeSettings it : this) {
            if (it.getIndex() == index) {
                return it;
            }
        }
        return null;
    }

    @Override
    public final boolean add(final GXDLMSAttributeSettings item) {
        item.setParent(this);
        return super.add(item);
    }
}