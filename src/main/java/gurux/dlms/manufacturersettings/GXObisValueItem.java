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

public class GXObisValueItem {
    private String uiValue;
    private Object value;

    /**
     * Constructor.
     */
    public GXObisValueItem() {
    }

    /**
     * Constructor.
     * 
     * @param devValue
     *            Device value.
     * @param userValue
     *            Value that is shown to the user.
     */
    public GXObisValueItem(final Object devValue, final String userValue) {
        setValue(devValue);
        setUIValue(userValue);
    }

    /**
     * @return Value that is read from or written to the Device.
     */
    public final Object getValue() {
        return value;
    }

    /**
     * @param val
     *            Value that is read from or written to the Device.
     */
    public final void setValue(final Object val) {
        value = val;
    }

    /**
     * @return Value that is shown to the user.
     */
    public final String getUIValue() {
        return uiValue;
    }

    /**
     * @param val
     *            Value that is shown to the user.
     */
    public final void setUIValue(final String val) {
        uiValue = val;
    }
}