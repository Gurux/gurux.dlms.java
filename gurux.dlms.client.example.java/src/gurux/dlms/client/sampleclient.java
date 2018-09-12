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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import gurux.common.GXCmdParameter;
import gurux.common.GXCommon;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class sampleclient {

    static ObjectType[] getObjects() {
        List<ObjectType> list = new ArrayList<ObjectType>();
        list.add(ObjectType.ACTION_SCHEDULE);
        list.add(ObjectType.ACTIVITY_CALENDAR);
        list.add(ObjectType.ASSOCIATION_LOGICAL_NAME);
        list.add(ObjectType.ASSOCIATION_SHORT_NAME);
        list.add(ObjectType.AUTO_ANSWER);
        list.add(ObjectType.AUTO_CONNECT);
        list.add(ObjectType.CLOCK);
        list.add(ObjectType.DATA);
        list.add(ObjectType.DEMAND_REGISTER);
        list.add(ObjectType.MAC_ADDRESS_SETUP);
        list.add(ObjectType.EXTENDED_REGISTER);
        list.add(ObjectType.GPRS_SETUP);
        list.add(ObjectType.IEC_HDLC_SETUP);
        list.add(ObjectType.IEC_LOCAL_PORT_SETUP);
        list.add(ObjectType.IEC_TWISTED_PAIR_SETUP);
        list.add(ObjectType.IP4_SETUP);
        list.add(ObjectType.MBUS_SLAVE_PORT_SETUP);
        list.add(ObjectType.IMAGE_TRANSFER);
        list.add(ObjectType.SECURITY_SETUP);
        list.add(ObjectType.DISCONNECT_CONTROL);
        list.add(ObjectType.LIMITER);
        list.add(ObjectType.MBUS_CLIENT);
        list.add(ObjectType.MODEM_CONFIGURATION);
        list.add(ObjectType.PPP_SETUP);
        list.add(ObjectType.PROFILE_GENERIC);
        list.add(ObjectType.REGISTER);
        list.add(ObjectType.REGISTER_ACTIVATION);
        list.add(ObjectType.REGISTER_MONITOR);
        // list.add(ObjectType.REGISTER_TABLE);
        // list.add(ObjectType.ZIG_BEE_SAS_STARTUP);
        // list.add(ObjectType.ZIG_BEE_SAS_JOIN);
        list.add(ObjectType.SAP_ASSIGNMENT);
        list.add(ObjectType.SCHEDULE);
        list.add(ObjectType.SCRIPT_TABLE);
        list.add(ObjectType.SPECIAL_DAYS_TABLE);
        // list.add(ObjectType.STATUS_MAPPING);
        list.add(ObjectType.TCP_UDP_SETUP);
        // list.add(ObjectType.ZIG_BEE_SAS_APS_FRAGMENTATION);
        // list.add(ObjectType.UTILITY_TABLES);
        list.add(ObjectType.PUSH_SETUP);
        list.add(ObjectType.MBUS_MASTER_PORT_SETUP);
        list.add(ObjectType.GSM_DIAGNOSTIC);
        list.add(ObjectType.ACCOUNT);
        list.add(ObjectType.CREDIT);
        list.add(ObjectType.CHARGE);
        // list.add(ObjectType.TOKEN_GATEWAY);
        list.add(ObjectType.PARAMETER_MONITOR);
        list.add(ObjectType.IP6_SETUP);
        return list.toArray(new ObjectType[0]);
    }

    /**
     * @param args
     *            the command line arguments
     * @throws IOException
     * @throws XMLStreamException
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
            ////////////////////////////////////////
            reader = new GXDLMSReader(settings.client, settings.media,
                    settings.trace);
            settings.media.open();
            if (settings.readObjects.size() != 0) {
                reader.initializeConnection();
                reader.getAssociationView();
                for (Map.Entry<String, Integer> it : settings.readObjects) {
                    Object val = reader.read(
                            settings.client.getObjects()
                                    .findByLN(ObjectType.NONE, it.getKey()),
                            it.getValue());
                    reader.showValue(it.getValue(), val);
                }
            } else {
                reader.readAll();
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
        System.out.println(
                " -g \"0.0.1.0.0.255:1; 0.0.1.0.0.255:2\" Get selected object(s) with given attribute index.");
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
                GXCommon.getParameters(args, "h:p:c:s:r:it:a:p:wP:g:");
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
            case 'g':
                // Get (read) selected objects.
                for (String o : it.getValue().split("[;,]")) {
                    String[] tmp = o.split("[:]");
                    if (tmp.length != 2) {
                        throw new IllegalArgumentException(
                                "Invalid Logical name or attribute index.");
                    }
                    settings.readObjects.add(new GXSimpleEntry<String, Integer>(
                            tmp[0].trim(), Integer.parseInt(tmp[1].trim())));
                }
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
