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

import java.util.List;
import java.util.Map.Entry;

import gurux.common.GXCommon;
import gurux.common.IGXMediaListener;
import gurux.common.MediaStateEventArgs;
import gurux.common.PropertyChangedEventArgs;
import gurux.common.ReceiveEventArgs;
import gurux.common.TraceEventArgs;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXReplyData;
import gurux.dlms.TranslatorOutputType;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.secure.GXDLMSSecureClient;
import gurux.net.GXNet;
import gurux.net.enums.NetworkType;

/**
 * All example servers are using same objects.
 */
public class GXDLMSPushListener
        implements IGXMediaListener, gurux.net.IGXNetListener, AutoCloseable {

    /**
     * Are messages traced.
     */
    private boolean trace = false;
    /**
     * TCP/IP port to listen.
     */
    private GXNet media;
    /**
     * Received data is saved to reply buffer because whole message is not
     * always received in one packet.
     */
    private GXByteBuffer reply = new GXByteBuffer();
    /**
     * Received data. This is used if GBT is used and data is received on
     * several data blocks.
     */
    private GXReplyData data = new GXReplyData();

    /**
     * Client used to parse received data.
     */
    private GXDLMSSecureClient client = new GXDLMSSecureClient(true, 1, 1,
            Authentication.NONE, null, InterfaceType.WRAPPER);

    /**
     * Constructor.
     * 
     * @param port
     *            Listener port.
     */
    public GXDLMSPushListener(int port) throws Exception {
        media = new gurux.net.GXNet(NetworkType.TCP, port);
        media.setTrace(TraceLevel.VERBOSE);
        media.addListener(this);
        media.open();
        // TODO; Must set communication specific settings.
    }

    /**
     * Listener is closed.
     */
    public void close() {
        media.close();
    }

    @Override
    public void onError(Object sender, Exception ex) {
        System.out.println("Error has occurred:" + ex.getMessage());
    }

    private static void printData(final Object value) {
        if (value instanceof Object[]) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            // Print received data.
            for (Object it : (Object[]) value) {
                printData(it);
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        } else if (value instanceof byte[]) {
            // Print value.
            System.out.println(GXCommon.bytesToHex((byte[]) value));
        } else {
            // Print value.
            System.out.println(String.valueOf(value));
        }
    }

    private static void printValue(Entry<GXDLMSObject, Integer> it) {
        // Print value
        System.out.println(it.getKey().getObjectType() + " "
                + it.getKey().getLogicalName() + " " + it.getValue() + ":"
                + it.getKey().getValues()[it.getValue() - 1]);
    }

    /*
     * Client has send data.
     */
    @Override
    public void onReceived(Object sender, ReceiveEventArgs e) {
        try {
            synchronized (this) {
                if (trace) {
                    System.out.println("<- " + gurux.common.GXCommon
                            .bytesToHex((byte[]) e.getData()));
                }
                reply.set((byte[]) e.getData());
                client.getData(reply, data);
                // If all data is received.
                if (data.isComplete()) {
                    reply.clear();
                    if (!data.isMoreData()) {
                        try {
                            // Show data as XML.
                            GXDLMSTranslator t = new GXDLMSTranslator(
                                    TranslatorOutputType.SIMPLE_XML);
                            String xml = t.dataToXml(data.getData());
                            System.out.println(xml);
                            printData(data.getValue());
                            // Example is sending list of push messages in first
                            // parameter.
                            if (data.getValue() instanceof List<?>) {
                                List<Object> tmp =
                                        (List<Object>) data.getValue();
                                List<Entry<GXDLMSObject, Integer>> objects =
                                        client.parsePushObjects(
                                                (List<?>) tmp.get(0));
                                // Remove first item because it's not needed
                                // anymore.
                                objects.remove(0);
                                // Update clock.
                                int valueindex = 1;
                                for (Entry<GXDLMSObject, Integer> it : objects) {
                                    client.updateValue(it.getKey(),
                                            it.getValue(), tmp.get(valueindex));
                                    ++valueindex;
                                    printValue(it);
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        } finally {
                            data.clear();
                        }
                    }
                }
            }
        } catch (

        Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void onMediaStateChange(Object sender, MediaStateEventArgs e) {

    }

    /*
     * Client has made connection.
     */
    @Override
    public void onClientConnected(Object sender,
            gurux.net.ConnectionEventArgs e) {
        System.out.println("Client Connected.");
    }

    /*
     * Client has close connection.
     */
    @Override
    public void onClientDisconnected(Object sender,
            gurux.net.ConnectionEventArgs e) {
        // Reset server settings when connection closed.
        System.out.println("Client Disconnected.");
    }

    @Override
    public void onTrace(Object sender, TraceEventArgs e) {
        // System.out.println(e.toString());
    }

    @Override
    public void onPropertyChanged(Object sender, PropertyChangedEventArgs e) {

    }
}