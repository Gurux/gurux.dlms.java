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

import gurux.common.GXCommon;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXDLMSXmlPdu;
import gurux.dlms.GXReplyData;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.InterfaceType;
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
            int ret = Settings.getParameters(args, settings);
            if (ret != 0) {
                System.exit(1);
                return;
            }

            ////////////////////////////////////////
            // Initialize connection settings.
            if (settings.media instanceof GXSerial) {
                System.out.println("Connect using serial port connection "
                        + settings.media.toString());

            } else if (settings.media instanceof GXNet) {
                System.out.println("Connect using network connection "
                        + settings.media.toString());
            } else {
                throw new RuntimeException("Unknown media type.");
            }

            if (settings.path == null || settings.path == "") {
                throw new RuntimeException("Unknown xml path.");
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
}
