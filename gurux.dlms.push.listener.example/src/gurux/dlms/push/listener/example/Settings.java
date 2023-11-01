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
package gurux.dlms.push.listener.example;

import java.util.List;

import gurux.common.GXCmdParameter;
import gurux.common.GXCommon;
import gurux.common.enums.TraceLevel;
import gurux.dlms.enums.InterfaceType;

public class Settings {
    public TraceLevel trace = TraceLevel.INFO;
    public int port = 4060;
    public InterfaceType interfaceType = InterfaceType.HDLC;

    /**
     * Show help.
     */
    static void showHelp() {
        System.out.println(
                "Gurux DLMS push listener waits push messages from DLMS devices.");
        System.out.println(
                " -t\t[Error, Warning, Info, Verbose] Trace messages.");
        System.out.println(" -p\tUser push port. Default is 4060.");
        System.out
                .println(" -i\tUsed communication interface. Ex. -i WRAPPER.");
    }

    static int getParameters(String[] args, Settings settings) {
        List<GXCmdParameter> parameters =
                GXCommon.getParameters(args, "t:p:i:");
        for (GXCmdParameter it : parameters) {
            switch (it.getTag()) {
            case 't':
                // Trace.
                if ("Error".compareTo(it.getValue()) == 0)
                    settings.trace = TraceLevel.ERROR;
                else if ("Warning".compareTo(it.getValue()) == 0)
                    settings.trace = TraceLevel.WARNING;
                else if ("Info".compareTo(it.getValue()) == 0)
                    settings.trace = TraceLevel.INFO;
                else if ("Verbose".compareTo(it.getValue()) == 0)
                    settings.trace = TraceLevel.VERBOSE;
                else if ("Off".compareTo(it.getValue()) == 0)
                    settings.trace = TraceLevel.OFF;
                else
                    throw new IllegalArgumentException(
                            "Invalid Authentication option '" + it.getValue()
                                    + "'. (Error, Warning, Info, Verbose, Off).");
                break;
            case 'p':
                // Port.
                settings.port = Integer.parseInt(it.getValue());
                break;
            case 'i':// Interface type.
                if ("HDLC".equalsIgnoreCase(it.getValue()))
                    settings.interfaceType = InterfaceType.HDLC;
                else if ("WRAPPER".equalsIgnoreCase(it.getValue()))
                    settings.interfaceType = InterfaceType.WRAPPER;
                else if ("HdlcWithModeE".equalsIgnoreCase(it.getValue()))
                    settings.interfaceType = InterfaceType.HDLC_WITH_MODE_E;
                else if ("Plc".equalsIgnoreCase(it.getValue()))
                    settings.interfaceType = InterfaceType.PLC;
                else if ("PlcHdlc".equalsIgnoreCase(it.getValue()))
                    settings.interfaceType = InterfaceType.PLC_HDLC;
                else if ("CoAP".equalsIgnoreCase(it.getValue()))
                    settings.interfaceType = InterfaceType.COAP;
                else
                    throw new IllegalArgumentException(
                            "Invalid interface type option." + it.getValue()
                                    + " (HDLC, WRAPPER, HdlcWithModeE, Plc, PlcHdlc, CoAP)");
                break;
            case '?':
                switch (it.getTag()) {
                case 'p':
                    throw new IllegalArgumentException(
                            "Missing mandatory port option.");
                case 't':
                    throw new IllegalArgumentException(
                            "Missing mandatory trace option.\n");
                case 'i':
                    throw new IllegalArgumentException(
                            "Missing mandatory interface type option.");
                default:
                    showHelp();
                    return 1;
                }
            default:
                showHelp();
                return 1;
            }
        }
        return 0;
    }
}