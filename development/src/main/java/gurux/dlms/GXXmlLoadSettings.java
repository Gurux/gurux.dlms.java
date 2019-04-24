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
package gurux.dlms;

public class GXXmlLoadSettings {
    /**
     * Start date of profile Generic.
     */
    private java.util.Date start = new java.util.Date(0);
    /**
     * End date of profile Generic.
     */
    private java.util.Date end = new java.util.Date(0);

    /**
     * @return Start date of profile Generic.
     */
    public final java.util.Date getStart() {
        return start;
    }

    /**
     * @param value
     *            Start date of profile Generic.
     */
    public final void setStart(final java.util.Date value) {
        start = value;
    }

    /**
     * @return End date of profile Generic.
     */
    public final java.util.Date getEnd() {
        return end;
    }

    /**
     * @param value
     *            End date of profile Generic.
     */
    public final void setEnd(final java.util.Date value) {
        end = value;
    }
}