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

import gurux.common.IGXMediaListener;
import gurux.common.MediaStateEventArgs;
import gurux.common.PropertyChangedEventArgs;
import gurux.common.ReceiveEventArgs;
import gurux.common.TraceEventArgs;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXReplyData;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.secure.GXDLMSSecureNotify;
import gurux.net.GXNet;
import gurux.net.enums.NetworkType;

/**
 * All example servers are using same objects.
 */
public class GXDLMSPushListener extends GXDLMSSecureNotify
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
     * Constructor.
     * 
     * @param port
     *            Listener port.
     */
    public GXDLMSPushListener(int port) throws Exception {
        super(true, 1, 1, InterfaceType.WRAPPER);
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
                getData(reply, data);
                // If all data is received.
                if (data.isComplete() && !data.isMoreData()) {
                    try {
                        List<Entry<GXDLMSObject, Integer>> list;
                        list = parsePush((Object[]) data.getValue());
                        // Print received data.
                        for (Entry<GXDLMSObject, Integer> it : list) {
                            // Print LN.
                            System.out.println(it.getKey().toString());
                            // Print Value.
                            System.out.println(String.valueOf(it.getKey()
                                    .getValues()[it.getValue() - 1]));
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    } finally {
                        data.clear();
                    }
                }
            }
        } catch (Exception ex) {
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