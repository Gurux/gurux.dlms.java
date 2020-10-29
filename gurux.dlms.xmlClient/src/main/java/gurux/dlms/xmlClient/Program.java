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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gurux.common.GXCmdParameter;
import gurux.common.GXCommon;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXDLMSXmlPdu;
import gurux.dlms.GXReplyData;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.InterfaceType;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class Program {

    /**
     * Handle meter reply.
     * 
     * @param item
     *            Command to sent.
     * @param reply
     *            Received reply.
     */
    private static void handleReply(final GXDLMSXmlPdu item,
            final GXReplyData reply) {
        if (reply.getValue() instanceof byte[]) {
            System.out.println(GXCommon.bytesToHex((byte[]) reply.getValue()));
            System.out.println(reply.toString());
        } else {
            System.out.println(String.valueOf(reply));
        }
    }

    static File[] listFiles(final File folder) {
        List<File> list = new ArrayList<File>();
        if (folder.isDirectory()) {
            for (final File it : folder.listFiles()) {
                if (it.isDirectory()) {
                    listFiles(it);
                } else {
                    list.add(it);
                }
            }
        } else {
            list.add(folder);
        }
        return list.toArray(new File[list.size()]);
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Settings settings = new Settings();

        GXDLMSReader reader = null;
        try {
            ////////////////////////////////////////
            // Handle command line parameters.
            int ret = getParameters(args, settings);
            if (ret != 0) {
                System.exit(1);
                return;
            }

            ////////////////////////////////////////
            // Initialize connection settings.
            if (settings.media instanceof GXSerial) {
                GXSerial serial = (GXSerial) settings.media;
                if (settings.iec) {
                    serial.setBaudRate(BaudRate.BAUD_RATE_300);
                    serial.setDataBits(7);
                    serial.setParity(Parity.EVEN);
                    serial.setStopBits(StopBits.ONE);
                } else {
                    serial.setBaudRate(BaudRate.BAUD_RATE_9600);
                    serial.setDataBits(8);
                    serial.setParity(Parity.NONE);
                    serial.setStopBits(StopBits.ONE);
                }
            } else if (settings.media instanceof GXNet) {
            } else {
                throw new Exception("Unknown media type.");
            }

            if (settings.path == null || settings.path == "") {
                throw new Exception("Unknown xml path.");
            }
            // Execute messages.
            for (File file : listFiles(new File(settings.path))) {
                String name = file.getName();
                if (settings.trace.ordinal() > TraceLevel.WARNING.ordinal()) {
                    System.out.println(
                            "------------------------------------------------------------");
                    System.out.println(name);
                }
                List<GXDLMSXmlPdu> actions = settings.client.load(file);
                if (actions.isEmpty()) {
                    continue;
                }
                try {
                    settings.media.open();
                    reader = new GXDLMSReader(settings.client, settings.media,
                            settings.trace);

                    GXReplyData reply = new GXReplyData();
                    // Send SNRM if not in xml.
                    if (settings.client
                            .getInterfaceType() == InterfaceType.HDLC) {
                        if (!containsCommand(actions, Command.SNRM)) {
                            reader.snrmRequest();
                        }
                    }

                    // Send AARQ if not in xml.
                    if (!containsCommand(actions, Command.AARQ)) {
                        if (!containsCommand(actions, Command.SNRM)) {
                            reader.aarqRequest();
                        }
                    }

                    for (GXDLMSXmlPdu it : actions) {
                        if (it.getCommand() == Command.SNRM && settings.client
                                .getInterfaceType() == InterfaceType.WRAPPER) {
                            continue;
                        }
                        if (it.getCommand() == Command.DISCONNECT_REQUEST
                                && settings.client
                                        .getInterfaceType() == InterfaceType.WRAPPER) {
                            break;
                        }
                        // Send
                        reply.clear();
                        if (settings.trace.ordinal() > TraceLevel.WARNING
                                .ordinal()) {
                            System.out.println(
                                    "------------------------------------------------------------");
                            System.out.println(it.toString());
                        }
                        if (it.isRequest()) {
                            reader.readDataBlock(
                                    settings.client.pduToMessages(it), reply);
                            handleReply(it, reply);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(
                            "------------------------------------------------------------");
                    System.out.println(ex.getMessage());
                } finally {
                    // Send Disconnect if not in xml.
                    if (!containsCommand(actions, Command.DISCONNECT_REQUEST)) {
                        reader.disconnect();
                    } else {
                        settings.media.close();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(
                    "------------------------------------------------------------");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Is command in XML file.
     * 
     * @param actions
     * @param command
     * @return
     */
    private static boolean containsCommand(final List<GXDLMSXmlPdu> actions,
            final int command) {
        for (GXDLMSXmlPdu it : actions) {
            if (it.getCommand() == command) {
                return true;
            }
        }
        return false;
    }

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
        System.out.println(" -i IEC is a start protocol.");
        System.out.println(" -a \t Authentication (None, Low, High).");
        System.out.println(" -P \t Password for authentication.");
        System.out.println(" -c \t Client address. (Default: 16)");
        System.out.println(" -s \t Server address. (Default: 1)");
        System.out.println(" -n \t Server address as serial number.");
        System.out.println(
                " -r [sn, sn]\t Short name or Logican Name (default) referencing is used.");
        System.out.println(" -w WRAPPER profile is used. HDLC is default.");
        System.out
                .println(" -t [Error, Warning, Info, Verbose] Trace messages.");
        System.out.println("  -x input XML file.");
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
                GXCommon.getParameters(args, "h:p:c:s:r:it:a:p:wP:x:");
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
            case 'i':
                // IEC.
                settings.iec = true;
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
