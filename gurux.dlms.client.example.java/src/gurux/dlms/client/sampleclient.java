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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.enums.ObjectType;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class sampleclient {

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

            ////////////////////////////////////////
            // Initialize connection settings.
            if (settings.media instanceof GXSerial) {
                System.out.println("Connect using serial port connection "
                        + settings.media.toString());
            } else if (settings.media instanceof GXNet) {
                System.out.println("Connect using network connection "
                        + settings.media.toString());
            } else {
                throw new Exception("Unknown media type.");
            }
            ////////////////////////////////////////
            reader = new GXDLMSReader(settings.client, settings.media,
                    settings.trace, settings.invocationCounter);
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
                        GXDLMSObjectCollection c = GXDLMSObjectCollection
                                .load(settings.outputFile);
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
                    Object val = reader.read(
                            settings.client.getObjects()
                                    .findByLN(ObjectType.NONE, it.getKey()),
                            it.getValue());
                    reader.showValue(it.getValue(), val);
                }
            } else {
                reader.readAll(settings.outputFile);
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
