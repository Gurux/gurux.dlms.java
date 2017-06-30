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

import java.util.HashMap;

import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.secure.GXCiphering;

class GXDLMSXmlSettings {
    private AssociationResult result = AssociationResult.ACCEPTED;
    private SourceDiagnostic diagnostic = SourceDiagnostic.NONE;
    private byte reason = 0;
    private int command;
    private int count = 0;
    private int requestType = 0xFF;
    private GXByteBuffer attributeDescriptor = new GXByteBuffer();
    private GXByteBuffer data = new GXByteBuffer();
    private GXDLMSSettings settings = new GXDLMSSettings(true);
    private HashMap<String, Integer> tags = new HashMap<String, Integer>();
    private GXDateTime time = null;
    /**
     * Are numeric values shows as hex.
     */
    private boolean showNumericsAsHex;
    private boolean showStringAsHex = false;

    private TranslatorOutputType outputType;

    /**
     * Constructor.
     * 
     * @param list
     */
    GXDLMSXmlSettings(final TranslatorOutputType type,
            final boolean numericsAsHex, final boolean hex,
            final HashMap<String, Integer> list) {
        outputType = type;
        showNumericsAsHex = outputType != TranslatorOutputType.STANDARD_XML
                && numericsAsHex;
        showStringAsHex = hex;
        settings.setInterfaceType(InterfaceType.PDU);
        settings.setCipher(new GXCiphering("ABCDEFGH".getBytes()));
        tags = list;
    }

    /**
     * @return DLMS settings.
     */
    public final GXDLMSSettings getSettings() {
        return settings;
    }

    /**
     * @return the result
     */
    public final AssociationResult getResult() {
        return result;
    }

    /**
     * @param value
     *            the result to set
     */
    public final void setResult(final AssociationResult value) {
        result = value;
    }

    /**
     * @return the diagnostic
     */
    public final SourceDiagnostic getDiagnostic() {
        return diagnostic;
    }

    /**
     * @param value
     *            the diagnostic to set
     */
    public final void setDiagnostic(final SourceDiagnostic value) {
        diagnostic = value;
    }

    /**
     * @return the reason
     */
    public final byte getReason() {
        return reason;
    }

    /**
     * @param value
     *            the reason to set
     */
    public final void setReason(final byte value) {
        reason = value;
    }

    /**
     * @return the command
     */
    public final int getCommand() {
        return command;
    }

    /**
     * @param value
     *            the command to set
     */
    public final void setCommand(final int value) {
        command = value;
    }

    /**
     * @return the count
     */
    public final int getCount() {
        return count;
    }

    /**
     * @param value
     *            the count to set
     */
    public void setCount(final int value) {
        count = value;
    }

    /**
     * @return the requestType
     */
    public int getRequestType() {
        return requestType;
    }

    /**
     * @param value
     *            the requestType to set
     */
    public final void setRequestType(final int value) {
        requestType = value;
    }

    /**
     * @return the attributeDescriptor
     */
    public final GXByteBuffer getAttributeDescriptor() {
        return attributeDescriptor;
    }

    /**
     * @return the data
     */
    public final GXByteBuffer getData() {
        return data;
    }

    /**
     * @return the tags
     */
    public final HashMap<String, Integer> getTags() {
        return tags;
    }

    /**
     * @return the time
     */
    public final GXDateTime getTime() {
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
     * @return the showStringAsHex
     */
    public final boolean isShowStringAsHex() {
        return showStringAsHex;
    }

    public final int parseInt(final String value) {
        if (showNumericsAsHex) {
            return Integer.parseInt(value, 16);
        }
        return Integer.parseInt(value);
    }

    public final short parseShort(final String value) {
        if (showNumericsAsHex) {
            return Short.parseShort(value, 16);
        }
        return Short.parseShort(value);
    }

    public final long parseLong(final String value) {
        if (showNumericsAsHex) {
            return Long.parseLong(value, 16);
        }
        return Long.parseLong(value);
    }

    /**
     * @return the outputType
     */
    public TranslatorOutputType getOutputType() {
        return outputType;
    }
}