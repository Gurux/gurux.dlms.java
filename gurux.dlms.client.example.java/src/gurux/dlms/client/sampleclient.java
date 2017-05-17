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

import gurux.common.IGXMedia;
import gurux.dlms.enums.Authentication;
import gurux.dlms.manufacturersettings.GXManufacturer;
import gurux.dlms.manufacturersettings.GXManufacturerCollection;
import gurux.dlms.secure.GXDLMSSecureClient;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.net.enums.NetworkType;
import gurux.serial.GXSerial;
import gurux.terminal.GXTerminal;

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
            // Gurux example server parameters.
            // /m=lgz /h=localhost /p=4060
            // /m=grx /h=localhost /p=4061
            com = getManufactureSettings(args);
            // If help is shown.
            if (com == null) {
                return;
            }
            com.initializeConnection();
            com.readAllObjects(logFile);
        } catch (Exception e) {
            System.out.println(e.toString());
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
        System.out.println("Done!");
    }

    /**
     * Show help.
     */
    static void ShowHelp() {
        System.out.println(
                "GuruxDlmsSample reads data from the DLMS/COSEM device.");
        System.out.println("");
        System.out.println(
                "GuruxDlmsSample /m=lgz /h=www.gurux.org /p=1000 [/s=] [/u]");
        System.out.println(" /m=\t manufacturer identifier.");
        System.out.println(" /sp=\t serial port. (Example: COM1)");
        System.out.println(" /n=\t Phone number.");
        System.out.println(" /b=\t Serial port baud rate. 9600 is default.");
        System.out.println(" /h=\t host name.");
        System.out.println(" /p=\t port number or name (Example: 1000).");
        System.out.println(" /s=\t start protocol (IEC or DLMS).");
        System.out.println(" /a=\t Authentication (None, Low, High).");
        System.out.println(" /pw=\t Password for authentication.");
        System.out.println(" /t\t Trace messages.");
        System.out
                .println(" /u\t Update meter settings from Gurux web portal.");
        System.out.println("Example:");
        System.out.println("Read LG device using TCP/IP connection.");
        System.out.println("GuruxDlmsSample /m=lgz /h=www.gurux.org /p=1000");
        System.out.println("Read LG device using serial port connection.");
        System.out.println("GuruxDlmsSample /m=lgz /sp=COM1 /s=DLMS");
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
    static GXCommunicate getManufactureSettings(String[] args)
            throws Exception {
        IGXMedia media = null;
        GXCommunicate com;
        String path = "ManufacturerSettings";
        try {
            if (GXManufacturerCollection.isFirstRun(path)) {
                GXManufacturerCollection.updateManufactureSettings(path);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        // 4059 is Official DLMS port.
        String id = "", host = "", port = "4059", pw = "";
        boolean trace = false, iec = true;
        Authentication auth = Authentication.NONE;
        int startBaudRate = 9600;
        String number = null;
        for (String it : args) {
            String item = it.trim();
            if (item.compareToIgnoreCase("/u") == 0) {
                // Update
                // Get latest manufacturer settings from Gurux web server.
                GXManufacturerCollection.updateManufactureSettings(path);
            } else if (item.startsWith("/m=")) {
                // Manufacturer
                id = item.replaceFirst("/m=", "");
            } else if (item.startsWith("/h=")) {
                // Host
                host = item.replaceFirst("/h=", "");
            } else if (item.startsWith("/p=")) {
                // TCP/IP Port
                media = new gurux.net.GXNet();
                port = item.replaceFirst("/p=", "");
            } else if (item.startsWith("/sp=")) {
                // Serial Port
                if (media == null) {
                    media = new gurux.serial.GXSerial();
                }
                port = item.replaceFirst("/sp=", "");
            } else if (item.startsWith("/n=")) {
                // Phone number for terminal.
                media = new GXTerminal();
                number = item.replaceFirst("/n=", "");
            } else if (item.startsWith("/b=")) {
                // Baud rate
                startBaudRate = Integer.parseInt(item.replaceFirst("/b=", ""));
            } else if (item.startsWith("/t")) {
                // Are messages traced.
                trace = true;
            } else if (item.startsWith("/s=")) {
                // Start
                String tmp = item.replaceFirst("/s=", "");
                iec = !tmp.toLowerCase().equals("dlms");
            } else if (item.startsWith("/a=")) {
                // Authentication
                auth = Authentication.valueOf(
                        it.trim().replaceFirst("/a=", "").toUpperCase());
            } else if (item.startsWith("/pw=")) {
                // Password
                pw = it.trim().replaceFirst("/pw=", "");
            } else {
                ShowHelp();
                return null;
            }
        }
        if (id.isEmpty() || port.isEmpty()
                || (media instanceof gurux.net.GXNet && host.isEmpty())) {
            ShowHelp();
            return null;
        }
        ////////////////////////////////////////
        // Initialize connection settings.
        if (media instanceof GXSerial) {
            GXSerial serial = (GXSerial) media;
            serial.setPortName(port);
            if (iec) {
                serial.setBaudRate(BaudRate.BAUD_RATE_300);
                serial.setDataBits(7);
                serial.setParity(Parity.EVEN);
                serial.setStopBits(StopBits.ONE);
            } else {
                serial.setBaudRate(BaudRate.forValue(startBaudRate));
                serial.setDataBits(8);
                serial.setParity(Parity.NONE);
                serial.setStopBits(StopBits.ONE);
            }
        } else if (media instanceof GXNet) {
            GXNet net = (GXNet) media;
            net.setPort(Integer.parseInt(port));
            net.setHostName(host);
            net.setProtocol(NetworkType.TCP);
        } else if (media instanceof GXTerminal) {
            GXTerminal terminal = (GXTerminal) media;
            terminal.setPortName(port);
            terminal.setBaudRate(BaudRate.forValue(startBaudRate));
            terminal.setDataBits(8);
            terminal.setParity(gurux.io.Parity.NONE);
            terminal.setStopBits(gurux.io.StopBits.ONE);
            terminal.setPhoneNumber(number);
        } else {
            throw new Exception("Unknown media type.");
        }
        GXDLMSSecureClient dlms = new GXDLMSSecureClient();
        GXManufacturerCollection items = new GXManufacturerCollection();
        GXManufacturerCollection.readManufacturerSettings(items, path);
        GXManufacturer man = items.findByIdentification(id);
        if (man == null) {
            throw new RuntimeException("Invalid manufacturer.");
        }
        dlms.setObisCodes(man.getObisCodes());
        com = new GXCommunicate(50000, dlms, man, iec, auth, pw, media);
        com.Trace = trace;
        return com;
    }
}
