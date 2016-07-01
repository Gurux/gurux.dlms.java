//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: http://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.push.listener.example;

import java.util.Calendar;

import gurux.dlms.GXDLMSNotify;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSClock;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSPushSetup;
import gurux.net.GXNet;
import gurux.net.enums.NetworkType;

/**
 * @author Gurux Ltd
 */
public class GuruxDlmsPushListenerExample {
    /**
     * Server component that handles received DLMS Push messages.
     */
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        int port = 4061;
        GXNet media = new GXNet(NetworkType.TCP, "localhost", port);
        GXDLMSNotify cl = new GXDLMSNotify(true, 1, 1, InterfaceType.WRAPPER);
        GXDLMSPushSetup p = new GXDLMSPushSetup();
        GXDLMSClock clock = new GXDLMSClock();
        p.getPushObjectList()
                .add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(p,
                        new GXDLMSCaptureObject(2, 0)));
        p.getPushObjectList()
                .add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(clock,
                        new GXDLMSCaptureObject(2, 0)));
        try (GXDLMSPushListener SNServer = new GXDLMSPushListener(port)) {
            System.out.println(
                    "Starting to listen Push messages in port " + port);
            System.out.println(
                    "Press X to close and Enter to send a Push message.");
            int ret = 10;
            while ((ret = System.in.read()) != -1) {
                // Send push.
                if (ret == 10) {
                    System.out.println("Sending Push message.");
                    media.open();
                    clock.setTime(
                            new GXDateTime(Calendar.getInstance().getTime()));
                    for (byte[] it : cl.generatePushSetupMessages(null, p)) {
                        media.send(it, null);
                    }
                    media.close();
                }
                // Close app.
                if (ret == 'x' || ret == 'X') {
                    break;
                }
            }
            media.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
