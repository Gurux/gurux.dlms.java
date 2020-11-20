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
package gurux.dlms.xmlClient;

import java.util.ArrayList;

import gurux.common.GXCmdParameter;
import gurux.common.GXCommon;
import gurux.common.IGXMedia;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXDLMSXmlClient;
import gurux.dlms.TranslatorOutputType;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class Settings {
    public IGXMedia media = null;
    public TraceLevel trace = TraceLevel.INFO;
    public GXDLMSXmlClient client =
            new GXDLMSXmlClient(TranslatorOutputType.SIMPLE_XML);
    // Objects to read.
    public String path;

    /**
     * Show help.
     */
    static void showHelp() {
        System.out.println(
                "GuruxDlmsSample reads data from the DLMS/COSEM device.");
        System.out.println(
                "GuruxDlmsSample -h [Meter IP Address] -p [Meter Port No] -c 16 -s 1 -r SN");
        System.out.println(" -h \t host name or IP address.");
        System.out.println(" -p \t port number or name (Example: 1000).");
        System.out.println(" -S \t serial port.");
        System.out.println(" -a \t Authentication (None, Low, High).");
        System.out.println(" -P \t Password for authentication.");
        System.out.println(" -c \t Client address. (Default: 16)");
        System.out.println(" -s \t Server address. (Default: 1)");
        System.out.println(" -n \t Server address as serial number.");
        System.out.println(
                " -r [sn, sn]\t Short name or Logican Name (default) referencing is used.");
        System.out
                .println(" -t [Error, Warning, Info, Verbose] Trace messages.");
        System.out.println("  -x input XML file.");
        System.out.println(
                " -i \t Used communication interface. Ex. -i WRAPPER.");
        System.out.println("Example:");
        System.out.println("Read LG device using TCP/IP connection.");
        System.out.println(
                "GuruxDlmsSample -r SN -c 16 -s 1 -h [Meter IP Address] -p [Meter Port No]");
        System.out.println("Read LG device using serial port connection.");
        System.out.println("GuruxDlmsSample -r SN -c 16 -s 1 -sp COM1 -i");
        System.out.println("Read Indian device using serial port connection.");
        System.out.println(
                "GuruxDlmsSample -S COM1 -c 16 -s 1 -a Low -P [password]");

    }

    static int getParameters(String[] args, Settings settings) {
        ArrayList<GXCmdParameter> parameters =
                GXCommon.getParameters(args, "h:p:c:s:r:i:t:a:p:wP:x:");
        GXNet net = null;
        for (GXCmdParameter it : parameters) {
            switch (it.getTag()) {
            case 'w':
                settings.client.setInterfaceType(InterfaceType.WRAPPER);
                break;
            case 'r':
                if ("sn".compareTo(it.getValue()) == 0) {
                    settings.client.setUseLogicalNameReferencing(false);
                } else if ("ln".compareTo(it.getValue()) == 0) {
                    settings.client.setUseLogicalNameReferencing(true);
                } else {
                    throw new IllegalArgumentException(
                            "Invalid reference option.");
                }
                break;
            case 'h':
                // Host address.
                if (settings.media == null) {
                    settings.media = new GXNet();
                }
                net = (GXNet) settings.media;
                net.setHostName(it.getValue());
                break;
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
                            "Invalid Authentication option. (Error, Warning, Info, Verbose, Off).");
                break;
            case 'p':
                // Port.
                if (settings.media == null) {
                    settings.media = new GXNet();
                }
                net = (GXNet) settings.media;
                net.setPort(Integer.parseInt(it.getValue()));
                break;
            case 'P':// Password
                settings.client.setPassword(it.getValue().getBytes());
                break;
            case 'i':// Interface type.
                if ("HDLC".equalsIgnoreCase(it.getValue()))
                    settings.client.setInterfaceType(InterfaceType.HDLC);
                else if ("WRAPPER".equalsIgnoreCase(it.getValue()))
                    settings.client.setInterfaceType(InterfaceType.WRAPPER);
                else if ("HdlcWithModeE".equalsIgnoreCase(it.getValue()))
                    settings.client
                            .setInterfaceType(InterfaceType.HDLC_WITH_MODE_E);
                else if ("Plc".equalsIgnoreCase(it.getValue()))
                    settings.client.setInterfaceType(InterfaceType.PLC);
                else if ("PlcHdlc".equalsIgnoreCase(it.getValue()))
                    settings.client.setInterfaceType(InterfaceType.PLC_HDLC);
                else
                    throw new IllegalArgumentException(
                            "Invalid interface type option." + it.getValue()
                                    + " (HDLC, WRAPPER, HdlcWithModeE, Plc, PlcHdlc)");
                break;
            case 'S':// Serial Port
                settings.media = new GXSerial();
                GXSerial serial = (GXSerial) settings.media;
                serial.setPortName(it.getValue());
                break;
            case 'a':
                try {
                    settings.client.setAuthentication(
                            Authentication.valueOfString(it.getValue()));
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Invalid Authentication option: '" + it.getValue()
                                    + "'. (None, Low, High, HighMd5, HighSha1, HighGmac, HighSha256)");
                }
                break;
            case 'o':
                break;
            case 'c':
                settings.client
                        .setClientAddress(Integer.parseInt(it.getValue()));
                break;
            case 's':
                settings.client
                        .setServerAddress(Integer.parseInt(it.getValue()));
                break;
            case 'x':
                settings.path = it.getValue();
                break;
            case '?':
                switch (it.getTag()) {
                case 'c':
                    throw new IllegalArgumentException(
                            "Missing mandatory client option.");
                case 's':
                    throw new IllegalArgumentException(
                            "Missing mandatory server option.");
                case 'h':
                    throw new IllegalArgumentException(
                            "Missing mandatory host name option.");
                case 'p':
                    throw new IllegalArgumentException(
                            "Missing mandatory port option.");
                case 'r':
                    throw new IllegalArgumentException(
                            "Missing mandatory reference option.");
                case 'a':
                    throw new IllegalArgumentException(
                            "Missing mandatory authentication option.");
                case 'S':
                    throw new IllegalArgumentException(
                            "Missing mandatory Serial port option.\n");
                case 't':
                    throw new IllegalArgumentException(
                            "Missing mandatory trace option.\n");
                default:
                    showHelp();
                    return 1;
                }
            default:
                showHelp();
                return 1;
            }
        }
        if (settings.media == null) {
            showHelp();
            return 1;
        }
        return 0;
    }
}