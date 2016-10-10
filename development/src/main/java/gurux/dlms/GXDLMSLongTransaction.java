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

package gurux.dlms;

/**
 * Long get or set information is saved here.
 */
public class GXDLMSLongTransaction {
    /**
     * Executed command.
     */
    private int command;

    /**
     * Targets.
     */
    private ValueEventArgs[] targets;

    /**
     * Extra data from PDU.
     */
    private GXByteBuffer data;

    /**
     * Constructor.
     * 
     * @param forTargets
     *            Targets.
     * @param forCommand
     *            Command.
     * @param forData
     *            Data.
     */
    public GXDLMSLongTransaction(final ValueEventArgs[] forTargets,
            final int forCommand, final GXByteBuffer forData) {
        targets = forTargets;
        command = forCommand;
        data = new GXByteBuffer();
        data.set(forData);
    }

    /**
     * @return Executed command.
     */
    public final int getCommand() {
        return command;
    }

    /**
     * @return Targets.
     */
    public final ValueEventArgs[] getTargets() {
        return targets;
    }

    /**
     * @return data.
     */
    public final GXByteBuffer getData() {
        return data;
    }

    /**
     * @param value
     *            New data.
     */
    public final void setData(final GXByteBuffer value) {
        data.clear();
        data.set(value);
    }
}
