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
        // Create servers and start listen events.
        try {
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Short Name
            // and start listen events.
            GXDLMSServerSN SNServer = new GXDLMSServerSN();
            SNServer.initialize(4060);
            System.out.println("Short Name DLMS Server in port 4060");
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Logical Name
            // and start listen events.
            GXDLMSServerLN LNServer = new GXDLMSServerLN();
            LNServer.initialize(4061);
            System.out.println("Logical Name DLMS Server in port 4061");
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Short Name
            // and start listen events.
            GXDLMSServerSN_47 SN_47Server = new GXDLMSServerSN_47();
            SN_47Server.initialize(4062);
            System.out.println(
                    "Short Name DLMS Server with IEC 62056-47 in port 4062");
            ///////////////////////////////////////////////////////////////////////
            // Create Gurux DLMS server component for Logical Name
            // and start listen events.
            GXDLMSServerLN_47 LN_47Server = new GXDLMSServerLN_47();
            LN_47Server.initialize(4063);
            System.out.println(
                    "Logical Name DLMS Server with IEC 62056-47 in port 4063");

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
        }
    }
}
