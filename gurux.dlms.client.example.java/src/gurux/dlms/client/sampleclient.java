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
package gurux.dlms.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gurux.common.IGXMedia;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.secure.GXDLMSSecureClient;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class sampleclient {
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        GXCommunicate com = null;
        PrintWriter logFile = null;
        try {
            logFile = new PrintWriter(
                    new BufferedWriter(new FileWriter("logFile.txt")));
            // Objects to read.
            List<Map.Entry<String, Integer>> readObjects =
                    new ArrayList<Map.Entry<String, Integer>>();

            com = getManufactureSettings(args, readObjects);
            // If help is shown.
            if (com == null) {
                System.exit(1);
            }
            com.initializeConnection();
            com.readAllObjects(logFile, readObjects);
            System.out.println("Done!");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        } finally {
            if (logFile != null) {
                logFile.close();
            }
            try {
                ///////////////////////////////////////////////////////////////
                // Disconnect.
                if (com != null) {
                    com.close();
                }
            } catch (Exception Ex2) {
                System.out.println(Ex2.toString());
            }
        }
    }

    /**
     * Show help.
     */
    static void showHelp() {
        System.out.println(
                "GuruxDlmsSample reads data from the DLMS/COSEM device.");
        System.out.println(
                "GuruxDlmsSample /h=[Meter IP Address] /p=[Meter Port No] [/s=] /c=16 /s=1 /r=SN");
        System.out.println(" /h=\t host name or IP address.");
        System.out.println(" /p=\t port number or name (Example: 1000).");
        System.out.println(" /sp=\t serial port.");
        System.out.println(" /IEC use IEC as start protocol.");
        System.out.println(" /a=\t Authentication (None, Low, High).");
        System.out.println(" /pw=\t Password for authentication.");
        System.out.println(" /c=\t Client address. (Default: 16)");
        System.out.println(" /s=\t Server address. (Default: 1)");
        System.out.println(" /sn=\t Server address as serial number.");
        System.out.println(
                " /r=[SN, LN]\t Short name or Logican Name (default) referencing is used.");
        System.out.println(" /WRAPPER profile is used. HDLC is default.");
        System.out.println(" /\t Trace messages.");
        System.out.println(
                " /g=\"0.0.1.0.0.255:1; 0.0.1.0.0.255:2\" Get selected object(s) with given attribute index.");
        System.out.println("Example:");
        System.out.println("Read LG device using TCP/IP connection.");
        System.out.println(
                "GuruxDlmsSample /r=SN /c=16 /s=1 /h=[Meter IP Address] /p=[Meter Port No]");
        System.out.println("Read LG device using serial port connection.");
        System.out.println("GuruxDlmsSample /r=SN /c=16 /s=1 /sp=COM1 /IEC");
        System.out.println("Read Indian device using serial port connection.");
        System.out.println(
                "GuruxDlmsSample /sp=COM1 /c=16 /s=1 /a=Low /pw=[password]");
    }

    /**
     * Get manufacturer settings from Gurux web service if not installed yet.
     * This is something that you do not necessary seed. You can // hard code
     * the settings. This is only for demonstration. Use hard coded settings
     * like this:
     * <p/>
     * GXDLMSClient cl = new GXDLMSClient(true, 16, 1, Authentication.NONE,
     * null, InterfaceType.HDLC);
     * 
     * @param args
     *            Command line arguments.
     * @return
     * @throws Exception
     */
    static GXCommunicate getManufactureSettings(String[] args,
            List<Map.Entry<String, Integer>> readObjects) throws Exception {
        IGXMedia media = null;
        GXDLMSSecureClient dlms = new GXDLMSSecureClient(true, 16, 1,
                Authentication.NONE, null, InterfaceType.HDLC);
        GXCommunicate com;
        boolean trace = false, iec = true;
        String str = null;
        for (String it : args) {
            String item = it.trim().toLowerCase();
            if (item.startsWith("/sn=")) {
                // Serial number.
                dlms.setServerAddress(GXDLMSClient.getServerAddress(
                        Integer.parseInt(item.replaceFirst("/sn=", ""))));
            } else if ("/wrapper".compareToIgnoreCase(item) == 0) {
                // Wrapper is used.
                dlms.setInterfaceType(InterfaceType.WRAPPER);
            } else if (item.startsWith("/r=")) {
                // referencing
                str = item.replace("/r=", "");
                if ("sn".compareToIgnoreCase(str) == 0) {
                    dlms.setUseLogicalNameReferencing(false);
                } else if ("ln".compareToIgnoreCase(str) == 0) {
                    dlms.setUseLogicalNameReferencing(true);
                } else {
                    throw new IllegalArgumentException(
                            "Invalid reference. Set LN or SN.");
                }
            } else if (item.startsWith("/c="))// Client address
            {
                dlms.setClientAddress(
                        Integer.parseInt(item.replace("/c=", "")));
            } else if (item.startsWith("/s="))// Server address
            {
                dlms.setServerAddress(
                        Integer.parseInt(item.replace("/c=", "")));
            } else if (item.startsWith("/h=")) // Host
            {
                if (media == null) {
                    media = new GXNet();
                }
                GXNet net = (GXNet) media;
                net.setHostName(item.replace("/h=", ""));
            } else if (item.startsWith("/p="))// TCP/IP Port
            {
                if (media == null) {
                    media = new GXNet();
                }
                GXNet net = (GXNet) media;
                net.setPort(Integer.parseInt(item.replace("/p=", "")));
            } else if (item.startsWith("/sp="))// Serial Port
            {
                media = new GXSerial();
                GXSerial serial = (GXSerial) media;
                serial.setPortName(item.replace("/sp=", ""));
            } else if (item.startsWith("/t")) {
                // Are messages traced.
                trace = true;
            } else if (item.startsWith("/iec")) {
                // IEC is start protocol.
                iec = true;
            } else if (item.startsWith("/a=")) {
                // Authentication
                dlms.setAuthentication(Authentication
                        .valueOfString(it.trim().replace("/a=", "")));
            } else if (item.startsWith("/pw=")) {
                // Password
                dlms.setPassword(it.trim().replace("/pw=", "").getBytes());
            } else if (item.startsWith("/g=")) {
                // Get objects
                for (String o : item.replace("/g=", "").split("[;,]")) {
                    String[] tmp = o.split("[:]");
                    if (tmp.length != 2) {
                        throw new IllegalArgumentException(
                                "Invalid Logical name or attribute index.");
                    }
                    readObjects.add(new GXSimpleEntry<String, Integer>(
                            tmp[0].trim(), Integer.parseInt(tmp[1].trim())));
                }
            } else {
                showHelp();
                return null;
            }
        }
        if (media == null) {
            showHelp();
            return null;
        }
        ////////////////////////////////////////
        // Initialize connection settings.
        if (media instanceof GXSerial) {
            GXSerial serial = (GXSerial) media;
            if (iec) {
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
        } else if (media instanceof GXNet) {
            GXNet net = (GXNet) media;
        } else {
            throw new Exception("Unknown media type.");
        }
        com = new GXCommunicate(5000, dlms, iec, media);
        com.Trace = trace;
        return com;
    }
}
