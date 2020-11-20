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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms;

import gurux.dlms.enums.InterfaceType;

/**
 * XML translator message detailed data.
 */
public class GXDLMSTranslatorMessage {
    /**
     * Message to convert to XML.
     */
    private GXByteBuffer message;

    /**
     * Converted XML.
     */
    private String xml;
    /**
     * Executed Command.
     */
    private int command;
    /**
     * System title from AARQ or AARE messages.
     */
    private byte[] systemTitle;

    /**
     * Dedicated key from AARQ messages.
     */
    private byte[] dedicatedKey;
    /**
     * Interface type.
     */
    private InterfaceType interfaceType;

    public final GXByteBuffer getMessage() {
        return message;
    }

    public final void setMessage(GXByteBuffer value) {
        message = value;
    }

    /**
     * @return Converted XML.
     */
    public final String getXml() {
        return xml;
    }

    /**
     * @param value
     *            Converted XML.
     */
    public final void setXml(String value) {
        xml = value;
    }

    /**
     * @return Executed Command.
     */
    public final int getCommand() {
        return command;
    }

    /**
     * @param value
     *            Executed Command.
     */
    public final void setCommand(int value) {
        command = value;
    }

    /**
     * @return System title from AARQ or AARE messages.
     */
    public final byte[] getSystemTitle() {
        return systemTitle;
    }

    /**
     * @param value
     *            System title from AARQ or AARE messages.
     */
    public final void setSystemTitle(byte[] value) {
        systemTitle = value;
    }

    /**
     * @return Dedicated key from AARQ messages.
     */
    public final byte[] getDedicatedKey() {
        return dedicatedKey;
    }

    /**
     * @param value
     *            Dedicated key from AARQ messages.
     */
    public final void setDedicatedKey(byte[] value) {
        dedicatedKey = value;
    }

    /**
     * @return Interface type.
     */
    public final InterfaceType getInterfaceType() {
        return interfaceType;
    }

    /**
     * @param value
     *            Interface type.
     */
    public final void setInterfaceType(InterfaceType value) {
        interfaceType = value;
    }

}