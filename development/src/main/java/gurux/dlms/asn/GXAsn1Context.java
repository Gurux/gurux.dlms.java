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

package gurux.dlms.asn;

public class GXAsn1Context extends java.util.ArrayList<Object> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public GXAsn1Context() {
        constructed = true;
    }

    /**
     * Context index.
     */
    private int index;

    /**
     * Is constructed.
     */
    private boolean constructed;

    /**
     * @return Context index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param value
     *            Context index.
     */
    public void setIndex(final int value) {
        index = value;
    }

    /**
     * @return Is constructed.
     */
    public boolean isConstructed() {
        return constructed;
    }

    /**
     * @param value
     *            Is constructed.
     */
    public void setConstructed(final boolean value) {
        constructed = value;
    }

}
