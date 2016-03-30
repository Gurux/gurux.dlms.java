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

package gurux.dlms.objects.enums;

/**
 * The internal states of the disconnect control object.
 */
public enum ControlState {
    /**
     * The output_state is set to false and the consumer is disconnected.
     */
    DISCONNECTED,

    /**
     * The output_state is set to true and the consumer is connected.
     */
    CONNECTED,

    /**
     * The output_state is set to false and the consumer is disconnected.
     */
    READY_FOR_RECONNECTION;

    @Override
    public String toString() {
        String str;
        switch (ordinal()) {
        case 0:// DISCONNECTED
            str = "Disconnected";
            break;
        case 1:// CONNECTED
            str = "Connected";
            break;
        case 2:// READY_FOR_RECONNECTION
            str = "Ready For reconnection";
            break;
        default:
            str = "Unknown";
        }
        return str;
    }
}
