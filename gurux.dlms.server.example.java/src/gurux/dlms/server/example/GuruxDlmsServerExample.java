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

package gurux.dlms.server.example;

import gurux.common.GXCmdParameter;
import gurux.common.GXCommon;
import gurux.common.enums.TraceLevel;

class Settings {
    public TraceLevel trace = TraceLevel.INFO;
    public int port = 4060;
}

/**
 * @author Gurux Ltd
 */
public class GuruxDlmsServerExample {

    /**
     * Server component that handles received DLMS messages.
     */
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Settings settings = new Settings();
        // Create servers and start listen events.
        try {
            getParameters(args, settings);
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Short Name
            // and start listen events.
            GXDLMSServerSN SNServer = new GXDLMSServerSN();
            SNServer.initialize(settings.port, settings.trace);
            System.out.println("Short Name DLMS Server in port "
                    + String.valueOf(settings.port));
            System.out.println("Example connection settings:");
            System.out.println(
                    "Gurux.DLMS.Client.Example.Net -r sn -h localhost -p "
                            + String.valueOf(settings.port));
            System.out.println(
                    "----------------------------------------------------------");
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Logical Name
            // and start listen events.
            GXDLMSServerLN LNServer = new GXDLMSServerLN();
            LNServer.initialize(settings.port + 1, settings.trace);
            System.out.println("Logical Name DLMS Server in port "
                    + String.valueOf(settings.port + 1));
            System.out.println("Example connection settings:");
            System.out.println("Gurux.DLMS.Client.Example.Net -h=localhost -p "
                    + String.valueOf(settings.port + 1));
            System.out.println(
                    "----------------------------------------------------------");
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Short Name
            // and start listen events.
            GXDLMSServerSN_47 SN_47Server = new GXDLMSServerSN_47();
            SN_47Server.initialize(settings.port + 2, settings.trace);
            System.out
                    .println("Short Name DLMS Server with IEC 62056-47 in port "
                            + String.valueOf(settings.port + 2));
            System.out.println("Example connection settings:");
            System.out.println(
                    "Gurux.DLMS.Client.Example.Net -r sn -h localhost -w -p "
                            + String.valueOf(settings.port + 2));
            System.out.println(
                    "----------------------------------------------------------");
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Logical Name
            // and start listen events.
            GXDLMSServerLN_47 LN_47Server = new GXDLMSServerLN_47();
            LN_47Server.initialize(settings.port + 3, settings.trace);
            System.out.println(
                    "Logical Name DLMS Server with IEC 62056-47 in port "
                            + String.valueOf(settings.port + 3));
            System.out.println("Example connection settings:");
            System.out
                    .println("Gurux.DLMS.Client.Example.Net -h localhost -w -p "
                            + String.valueOf(settings.port + 3));

            System.out.println("Press Enter to close.");
            while (System.in.read() != 13) {
                System.out.println("Press Enter to close.");
            }
            /// Close servers.
            System.out.println("Closing servers.");
            SNServer.close();
            LNServer.close();
            SN_47Server.close();
            LN_47Server.close();
            System.out.println("Servers closed.");
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    private static int getParameters(String[] args, Settings settings) {
        java.util.ArrayList<GXCmdParameter> parameters =
                GXCommon.getParameters(args, "t:p:");
        for (GXCmdParameter it : parameters) {
            switch (it.getTag()) {
            case 't':
                // Trace.
                try {
                    settings.trace =
                            TraceLevel.valueOf(it.getValue().toUpperCase());
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException(
                            "Invalid Authentication option. (Error, Warning, Info, Verbose, Off)");
                }
                break;
            case 'p':
                // Port.
                settings.port = Integer.parseInt(it.getValue());
                break;
            case '?':
                switch (it.getTag()) {
                case 'p':
                    throw new IllegalArgumentException(
                            "Missing mandatory port option.");
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
        return 0;
    }

    private static void showHelp() {
        System.out.println(
                "Gurux DLMS example Server implements four DLMS/COSEM devices.\r\n");
        System.out.println(
                " -t [Error, Warning, Info, Verbose] Trace messages.\r\n");
        System.out.println(" -p Start port number. Default is 4060.\r\n");
    }
}
