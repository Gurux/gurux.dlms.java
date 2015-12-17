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

public class GXServerAddress {
    private int physicalAddress;
    private HDLCAddressType hdlcAddress = HDLCAddressType.DEFAULT;
    private String formula;
    private int logicalAddress;
    private boolean selected;

    /**
     * Constructor.
     */
    public GXServerAddress() {
    }

    /**
     * Constructor.
     * 
     * @param address
     *            HDLC address type.
     * @param value
     *            Physical address.
     */
    public GXServerAddress(final HDLCAddressType address, final int value) {
        setHDLCAddress(address);
        setPhysicalAddress(value);
    }

    /**
     * @return Is server address enabled.
     */
    public final boolean getSelected() {
        return selected;
    }

    /**
     * @param value
     *            Is server address enabled.
     */
    public final void setSelected(final boolean value) {
        selected = value;
    }

    public final HDLCAddressType getHDLCAddress() {
        return hdlcAddress;
    }

    public final void setHDLCAddress(final HDLCAddressType value) {
        hdlcAddress = value;
    }

    public final String getFormula() {
        return formula;
    }

    public final void setFormula(final String value) {
        formula = value;
    }

    public final int getPhysicalAddress() {
        return physicalAddress;
    }

    public final void setPhysicalAddress(final int value) {
        physicalAddress = value;
    }

    public final int getLogicalAddress() {
        return logicalAddress;
    }

    public final void setLogicalAddress(final int value) {
        logicalAddress = value;
    }
}