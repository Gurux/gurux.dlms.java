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

/**
 * M-Bus data definition element.
 */
public class GXMBusClientData {
    /**
     * Data information block.
     */
    private byte[] dataInformation;
    /**
     * Value information block.
     */
    private byte[] valueInformation;

    /**
     * Data.
     */
    private Object data;

    /**
     * @return Data information block.
     */
    public final byte[] getDataInformation() {
        return dataInformation;
    }

    /**
     * @param value
     *            Data information block.
     */
    public final void setDataInformation(final byte[] value) {
        dataInformation = value;
    }

    /**
     * @return Value information block.
     */
    public final byte[] getValueInformation() {
        return valueInformation;
    }

    /**
     * @param value
     *            Value information block.
     */
    public final void setValueInformation(final byte[] value) {
        valueInformation = value;
    }

    /**
     * @return Data.
     */
    public final Object getData() {
        return data;
    }

    /**
     * @param value
     *            Data.
     */
    public final void setData(final Object value) {
        data = value;
    }
}