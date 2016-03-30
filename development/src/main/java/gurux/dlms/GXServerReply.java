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

import java.util.List;

public class GXServerReply {
    /**
     * Server received data.
     */
    private GXByteBuffer data = new GXByteBuffer();

    /**
     * Server reply messages.
     */
    private List<byte[][]> replyMessages;

    /**
     * Reply message index.
     */
    private int index = 0;

    /**
     * @return the index
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            The index to set.
     */
    public final void setIndex(final int value) {
        index = value;
    }

    /**
     * @return the data
     */
    public final GXByteBuffer getData() {
        return data;
    }

    /**
     * @param value
     *            The data to set.
     */
    public final void setData(final GXByteBuffer value) {
        data = value;
    }

    /**
     * @return the replyMessages
     */
    public final List<byte[][]> getReplyMessages() {
        return replyMessages;
    }

    /**
     * @param value
     *            the replyMessages to set
     */
    public final void setReplyMessages(final List<byte[][]> value) {
        replyMessages = value;
    }
}
