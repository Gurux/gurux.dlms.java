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

package gurux.dlms.objects;

import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.internal.AutoResetEvent;

/*
 * This class is reserved for internal use. Do not use.
 */
final class GXProfileGenericUpdater extends Thread {
    private GXDLMSServerBase server;
    private GXDLMSProfileGeneric target;

    private AutoResetEvent receivedEvent;

    public AutoResetEvent getReceivedEvent() {
        return receivedEvent;
    }

    /*
     * Constructor.
     */
    GXProfileGenericUpdater(final GXDLMSServerBase svr,
            final GXDLMSProfileGeneric pg) {
        receivedEvent = new AutoResetEvent(false);
        target = pg;
        server = svr;
    }

    public void run() {
        do {
            try {
                target.capture(server);
            } catch (Exception ex) {
                System.out.printf(ex.getMessage());
            }
        } while (!receivedEvent.waitOne(target.getCapturePeriod() * 1000));
    }
}