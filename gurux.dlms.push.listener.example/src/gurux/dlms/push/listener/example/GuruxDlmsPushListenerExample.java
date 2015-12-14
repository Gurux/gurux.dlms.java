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

package gurux.dlms.push.listener.example;

/**
 * @author Gurux Ltd
 */
public class GuruxDlmsPushListenerExample {
    /**
     * Server component that handles received DLMS Push messages.
     */
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {

        try (GXDLMSPushListener SNServer = new GXDLMSPushListener(4061)) {
            System.out.println("Starting to listen Push messages in port 4060");
            System.out.println("Press any key to close.");
            System.in.read();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
