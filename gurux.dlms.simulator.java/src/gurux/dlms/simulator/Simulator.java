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
package gurux.dlms.simulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class Simulator {

    /*
     * Read simulated values from the meter.
     */
    static void readSimulatedValues(final Settings settings) throws Exception {
        GXDLMSReader reader = null;
        ////////////////////////////////////////
        // Initialise connection settings.
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
            throw new RuntimeException("Unknown media type.");
        }
        ////////////////////////////////////////
        reader = new GXDLMSReader(settings.client, settings.media,
                settings.trace, settings.iec, settings.invocationCounter);
        try {
            settings.media.open();
        } catch (Exception ex) {
            if (settings.media instanceof GXSerial) {
                System.out.println(
                        "----------------------------------------------------------");
                System.out.println(ex.getMessage());
                System.out.println("Available ports:");
                StringBuilder sb = new StringBuilder();
                for (String it : GXSerial.getPortNames()) {
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    sb.append(it);
                }
                System.out.println(sb.toString());
            }
            throw ex;
        }
        if (!settings.readObjects.isEmpty()) {
            reader.initializeConnection();
            if (settings.outputFile != null
                    && new File(settings.outputFile).exists()) {
                try {
                    GXDLMSObjectCollection c =
                            GXDLMSObjectCollection.load(settings.outputFile);
                    settings.client.getObjects().addAll(c);
                } catch (Exception ex) {
                    // It's OK if this fails.
                    System.out.print(ex.getMessage());
                }
            } else {
                reader.getAssociationView();
                if (settings.outputFile != null) {
                    settings.client.getObjects().save(settings.outputFile,
                            null);
                }
            }
            for (Map.Entry<String, Integer> it : settings.readObjects) {
                GXDLMSObject obj = settings.client.getObjects()
                        .findByLN(ObjectType.NONE, it.getKey());
                if ((obj.getAccess(it.getValue()).ordinal()
                        & AccessMode.READ.ordinal()) != 0) {
                    Object val = reader.read(obj, it.getValue());
                    reader.showValue(it.getValue(), val);
                }
            }
        } else {
            reader.readAll(settings.outputFile);
        }
    }

    /*
     * Start simulator.
     */
    static void startSimulator(final Settings settings) throws Exception {
        if (settings.media instanceof GXSerial) {
            GXDLMSMeter server = new GXDLMSMeter(
                    settings.client.getUseLogicalNameReferencing(),
                    InterfaceType.HDLC);
            if (settings.client.getUseLogicalNameReferencing()) {
                System.out.println(String.format(
                        "Logical Name DLMS Server in serial port %1$s.",
                        settings.media));
            } else {
                System.out.println(String.format(
                        "Short Name DLMS Server in serial port %1$s.",
                        settings.media));
            }
            server.initialize(settings.media, settings.trace,
                    settings.inputFile, 1, false);
            System.out.println(
                    "----------------------------------------------------------");
            while (System.in.read() != 13) {
                System.out.println("Press Enter to close.");
            }
            // Close servers.
            server.close();
        } else {
            // Create Network media component and start listen events.
            // 4059 is Official DLMS port.
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Short Name and start
            // listen events.
            List<GXDLMSMeter> servers = new ArrayList<GXDLMSMeter>();
            String str;
            if (settings.client.getInterfaceType() == InterfaceType.HDLC) {
                str = "DLMS HDLC";
            } else {
                str = "DLMS WRAPPER";
            }
            if (settings.client.getUseLogicalNameReferencing()) {
                str += " Logical Name ";
            } else {
                str += " Short Name ";
            }
            GXNet net = (GXNet) settings.media;
            net.setServer(true);
            if (settings.exclusive) {
                System.out.println(str + String.format(
                        "simulator start in port %1$d implementing %2$d meters.",
                        net.getPort(), settings.serverCount));
            } else {
                System.out.println(str + String.format(
                        "simulator start in ports %1$d-%2$d.", net.getPort(),
                        net.getPort() + settings.serverCount - 1));
            }
            for (int pos = 0; pos != settings.serverCount; ++pos) {
                GXDLMSMeter server = new GXDLMSMeter(
                        settings.client.getUseLogicalNameReferencing(),
                        settings.client.getInterfaceType());
                server.getConformance().clear();
                server.setMaxReceivePDUSize(0);
                servers.add(server);
                if (settings.exclusive) {
                    server.initialize(net, settings.trace, settings.inputFile,
                            pos + 1, settings.exclusive);
                } else {
                    server.initialize(
                            new GXNet(net.getProtocol(), net.getPort() + pos),
                            settings.trace, settings.inputFile, pos + 1,
                            settings.exclusive);
                }
                if (pos == 0
                        && settings.client.getUseLogicalNameReferencing()) {
                    System.out.println("Associations:");
                    for (GXDLMSObject tmp : server.getItems()
                            .getObjects(ObjectType.ASSOCIATION_LOGICAL_NAME)) {
                        GXDLMSAssociationLogicalName it =
                                (GXDLMSAssociationLogicalName) tmp;
                        if (it.getAuthenticationMechanismName()
                                .getMechanismId() == Authentication.NONE) {
                            System.out.println("Without authentication.");
                        } else {
                            System.out.println(String.format(
                                    "%1$s authentication, password %2$s",
                                    it.getAuthenticationMechanismName()
                                            .getMechanismId(),
                                    new String(it.getSecret())));
                        }
                    }
                }

            }
            while (System.in.read() != 13) {
                System.out.println("Press Enter to close.");
            }
            System.out.println("Closing servers.");
            // Close servers.
            for (GXDLMSMeter server : servers) {
                server.close();
            }
            System.out.println("Servers closed.");
        }
    }

    /**
     * @param args
     *            the command line arguments
     * @throws IOException
     * @throws XMLStreamException
     */
    public static void main(String[] args)
            throws XMLStreamException, IOException {
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
            if (settings.outputFile != null && !settings.outputFile.isEmpty()) {
                readSimulatedValues(settings);
                System.out.println(
                        "----------------------------------------------------------");
                System.out.println("Simulator template is created: "
                        + settings.outputFile);
            } else if (settings.inputFile != null
                    && !settings.inputFile.isEmpty()) {
                startSimulator(settings);
            } else {
                System.out.println("Device values file is not given.");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
            System.out.println("Ended. Press any key to continue.");
        }
    }

}
