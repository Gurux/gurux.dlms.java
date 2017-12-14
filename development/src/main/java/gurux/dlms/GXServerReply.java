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

package gurux.dlms;

public class GXServerReply {

    /**
     * Connection info.
     */
    private GXDLMSConnectionEventArgs connectionInfo;

    /**
     * Server received data.
     */
    private byte[] data;

    /**
     * Server reply message.
     */
    private byte[] reply;

    /**
     * Message count to send.
     */
    private int count;

    /**
     * Constructor.
     * 
     * @param value
     *            Received data.
     */
    public GXServerReply(byte[] value) {
        data = value;
    }

    /**
     * @return the data
     */
    public final byte[] getData() {
        return data;
    }

    /**
     * @param value
     *            The data to set.
     */
    public final void setData(final byte[] value) {
        data = value;
    }

    /**
     * @return The reply message.
     */
    public final byte[] getReply() {
        return reply;
    }

    /**
     * @param value
     *            the replyMessages to set
     */
    public final void setReply(final byte[] value) {
        reply = value;
    }

    /**
     * @return Connection info.
     */
    public final GXDLMSConnectionEventArgs getConnectionInfo() {
        return connectionInfo;
    }

    /**
     * @param value
     *            Connection info.
     */
    public final void setConnectionInfo(final GXDLMSConnectionEventArgs value) {
        connectionInfo = value;
    }

    /**
     * @return Is GBT streaming in progress.
     */
    public final boolean isStreaming() {
        return getCount() != 0;
    }

    /**
     * @return Message count to send.
     */
    public final int getCount() {
        return count;
    }

    /**
     * @param value
     *            Message count to send.
     */
    public final void setCount(final int value) {
        count = value;
    }
}
