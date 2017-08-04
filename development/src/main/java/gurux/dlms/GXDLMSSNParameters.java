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

/**
 * SN Parameters
 */
class GXDLMSSNParameters {
    /**
     * DLMS settings.
     */
    private GXDLMSSettings settings;
    /**
     * DLMS Command.
     */
    private int command;
    /**
     * Request type.
     */
    private int requestType;
    /**
     * Attribute descriptor.
     */
    private GXByteBuffer attributeDescriptor;
    /**
     * Data.
     */
    private GXByteBuffer data;
    /**
     * Send date and time. This is used in Data notification messages.
     */
    private GXDateTime time;
    /**
     * Item Count.
     */
    private int count;

    /**
     * Are there more data to send or more data to receive.
     */
    private boolean multipleBlocks;

    /**
     * Block index.
     */
    private int blockIndex;

    /**
     * Constructor.
     * 
     * @param forSettings
     *            DLMS settings.
     * @param forCommand
     *            Command.
     * @param forCommandType
     *            Command type.
     * @param forAttributeDescriptor
     * @param forData
     *            Attribute descriptor
     * @return Generated messages.
     */
    GXDLMSSNParameters(final GXDLMSSettings forSettings, final int forCommand,
            final int forCount, final int forCommandType,
            final GXByteBuffer forAttributeDescriptor,
            final GXByteBuffer forData) {
        settings = forSettings;
        blockIndex = (short) settings.getBlockIndex();
        command = forCommand;
        count = forCount;
        requestType = forCommandType;
        attributeDescriptor = forAttributeDescriptor;
        data = forData;
        multipleBlocks = false;
    }

    /**
     * @return DLMS settings.
     */
    public GXDLMSSettings getSettings() {
        return settings;
    }

    /**
     * @return DLMS Command.
     */
    public int getCommand() {
        return command;
    }

    /**
     * @return Request type.
     */
    public int getRequestType() {
        return requestType;
    }

    /**
     * @param value
     *            Request type.
     */
    public void setRequestType(final int value) {
        requestType = value;
    }

    /**
     * @return the attributeDescriptor
     */
    public GXByteBuffer getAttributeDescriptor() {
        return attributeDescriptor;
    }

    /**
     * @return the data
     */
    public GXByteBuffer getData() {
        return data;
    }

    /**
     * @return the time
     */
    public GXDateTime getTime() {
        return time;
    }

    /**
     * @param value
     *            the time to set
     */
    public final void setTime(final GXDateTime value) {
        time = value;
    }

    /**
     * @return the multipleBlocks
     */
    public boolean isMultipleBlocks() {
        return multipleBlocks;
    }

    /**
     * @param value
     *            the multipleBlocks to set
     */
    void setMultipleBlocks(final boolean value) {
        multipleBlocks = value;
    }

    /**
     * @return Block index.
     */
    public int getBlockIndex() {
        return blockIndex;
    }

    /**
     * @param value
     *            Block index.
     */
    void setBlockIndex(final int value) {
        blockIndex = value;
    }

    /**
     * @return Item Count.
     */
    public int getCount() {
        return count;
    }
}
