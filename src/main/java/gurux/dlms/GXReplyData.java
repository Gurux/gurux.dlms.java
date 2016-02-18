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

import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.RequestTypes;

public class GXReplyData {

    /**
     * Is more data available.
     */
    private RequestTypes moreData;
    /**
     * Received command.
     */
    private Command command;
    /**
     * Received data.
     */
    private GXByteBuffer data = new GXByteBuffer();
    /**
     * Is frame complete.
     */
    private boolean complete;
    /**
     * Received error.
     */
    private short error;

    /**
     * Read value.
     */
    private Object dataValue = null;

    /**
     * Expected count of element in the array.
     */
    private int totalCount = 0;

    /**
     * Last read position. This is used in peek to solve how far data is read.
     */
    private int readPosition;

    /**
     * Block length.
     */
    private int blockLength = 0;

    /**
     * Try get value.
     */
    private boolean peek;

    /**
     * Client address.
     */
    private int clientAddress;

    /**
     * Server address.
     */
    private int serverAddress;

    private DataType dataType = DataType.NONE;

    /**
     * Constructor.
     * 
     * @param more
     *            Is more data available.
     * @param cmd
     *            Received command.
     * @param buff
     *            Received data.
     * @param forComplete
     *            Is frame complete.
     * @param err
     *            Received error ID.
     */
    GXReplyData(final RequestTypes more, final Command cmd,
            final GXByteBuffer buff, final boolean forComplete,
            final byte err) {
        clear();
        moreData = more;
        command = cmd;
        data = buff;
        complete = forComplete;
        error = err;
    }

    /**
     * Constructor.
     */
    public GXReplyData() {
        clear();
    }

    public final DataType getValueType() {
        return dataType;
    }

    public final void setValueType(final DataType value) {
        dataType = value;
    }

    public final Object getValue() {
        return dataValue;
    }

    public final void setValue(final Object value) {
        dataValue = value;
    }

    public final int getReadPosition() {
        return readPosition;
    }

    public final void setReadPosition(final int value) {
        readPosition = value;
    }

    public final int getBlockLength() {
        return blockLength;
    }

    public final void setBlockLength(final int value) {
        blockLength = value;
    }

    public final int getClientAddress() {
        return clientAddress;
    }

    public final void setClientAddress(final int value) {
        clientAddress = value;
    }

    public final int getServerAddress() {
        return serverAddress;
    }

    public final void setServerAddress(final int value) {
        serverAddress = value;
    }

    public final void setCommand(final Command value) {
        command = value;
    }

    public final void setData(final GXByteBuffer value) {
        data = value;
    }

    public final void setComplete(final boolean value) {
        complete = value;
    }

    public final void setError(final short value) {
        error = value;
    }

    public final void setTotalCount(final int value) {
        totalCount = value;
    }

    /**
     * Reset data values to default.
     */
    public final void clear() {
        moreData = RequestTypes.NONE;
        command = Command.NONE;
        data.capacity(0);
        complete = false;
        error = 0;
        totalCount = 0;
        dataValue = null;
        readPosition = 0;
        blockLength = 0;
        dataType = DataType.NONE;
    }

    /**
     * @return Is more data available.
     */
    public final boolean isMoreData() {
        return moreData != RequestTypes.NONE && error == 0;
    }

    /**
     * Is more data available.
     * 
     * @return Return None if more data is not available or Frame or Block type.
     */
    public final RequestTypes getMoreData() {
        return moreData;
    }

    public final void setMoreData(final RequestTypes forValue) {
        moreData = forValue;
    }

    /**
     * Get received command.
     * 
     * @return Received command.
     */
    public final Command getCommand() {
        return command;
    }

    /**
     * Get received data.
     * 
     * @return Received data.
     */
    public final GXByteBuffer getData() {
        return data;
    }

    /**
     * Is frame complete.
     * 
     * @return Returns true if frame is complete or false if bytes is missing.
     */
    public final boolean isComplete() {
        return complete;
    }

    /**
     * Get Received error. Value is zero if no error has occurred.
     * 
     * @return Received error.
     */
    public final short getError() {
        return error;
    }

    public final String getErrorMessage() {
        return GXDLMS.getDescription(error);
    }

    /**
     * Get total count of element in the array. If this method is used peek must
     * be set true.
     * 
     * @return Count of element in the array.
     * @see peek
     * @see getCount
     */
    public final int getTotalCount() {
        return totalCount;
    }

    /**
     * Get count of read elements. If this method is used peek must be set true.
     * 
     * @return Count of read elements.
     * @see peek
     * @see getTotalCount
     */
    public final int getCount() {
        if (dataValue instanceof Object[]) {
            return ((Object[]) dataValue).length;
        }
        return 0;
    }

    /**
     * Get is value try to peek.
     * 
     * @return Is value try to peek.
     * @see getCount
     * @see getTotalCount
     */
    public final boolean getPeek() {
        return peek;
    }

    /**
     * Set is value try to peek.
     * 
     * @param forValue
     *            Is value try to peek.
     */
    public final void setPeek(final boolean forValue) {
        peek = forValue;
    }

}
