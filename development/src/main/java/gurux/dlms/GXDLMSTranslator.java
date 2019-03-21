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

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import gurux.dlms.enums.AccessServiceCommandType;
import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.BerType;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.secure.AesGcmParameter;
import gurux.dlms.secure.GXCiphering;

/**
 * This class is used to translate DLMS frame or PDU to xml.
 */
public class GXDLMSTranslator {
    HashMap<Integer, String> tags = new HashMap<Integer, String>();
    HashMap<String, Integer> tagsByName = new HashMap<String, Integer>();

    /**
     * Are numeric values shows as hex.
     */
    private boolean hex = true;

    /**
     * Is string serialized as hex. {@link messageToXml} {@link PduOnly}
     */
    private boolean showStringAsHex;

    /**
     * Sending data in multiple frames.
     */
    private boolean multipleFrames = false;
    /**
     * If only PDUs are shown and PDU is received on parts.
     */
    private GXByteBuffer pduFrames = new GXByteBuffer();

    /**
     * Is only PDU shown when data is parsed with messageToXml.
     * {@link messageToXml} {@link CompleatePdu}
     */
    private boolean pduOnly;

    private final TranslatorOutputType outputType;

    /*
     * Is XML declaration skipped.
     */
    private boolean omitXmlDeclaration = false;

    /*
     * Is XML name space skipped.
     */
    private boolean omitXmlNameSpace = false;

    /**
     * Add comments.
     */
    private boolean comments = false;

    /**
     * Used security.
     */
    private Security security;

    /**
     * System title.
     */
    private byte[] systemTitle;
    /**
     * Block cipher key.
     */
    private byte[] blockCipherKey;

    /**
     * Authentication key.
     */
    private byte[] authenticationKey;

    /**
     * Invocation Counter.
     */
    private int invocationCounter;

    /**
     * Constructor.
     * 
     * @param type
     *            Translator output type.
     */
    public GXDLMSTranslator(final TranslatorOutputType type) {
        outputType = type;
        getTags(outputType, tags, tagsByName);
    }

    /**
     * @return Output type.
     */
    public final TranslatorOutputType getOutputType() {
        return outputType;
    }

    /**
     * @return Is only PDU shown when data is parsed with messageToXml.
     */
    public final boolean getPduOnly() {
        return pduOnly;
    }

    /**
     * @param value
     *            Is only PDU shown when data is parsed with messageToXml.
     */
    public final void setPduOnly(final boolean value) {
        pduOnly = value;
    }

    /**
     * @return Used security.
     */
    public final Security getSecurity() {
        return security;
    }

    /**
     * @param value
     *            Used security.
     */
    public final void setSecurity(final Security value) {
        security = value;
    }

    /**
     * @return System title.
     */
    public final byte[] getSystemTitle() {
        return systemTitle;
    }

    /**
     * @param value
     *            System title.
     */
    public final void setSystemTitle(final byte[] value) {
        systemTitle = value;
    }

    /**
     * @return Block cipher key.
     */
    public final byte[] getBlockCipherKey() {
        return blockCipherKey;
    }

    /**
     * @param value
     *            Block cipher key.
     */
    public final void setBlockCipherKey(final byte[] value) {
        blockCipherKey = value;
    }

    /**
     * @return Authentication key.
     */
    public final byte[] getAuthenticationKey() {
        return authenticationKey;
    }

    /**
     * @param value
     *            Authentication key.
     */
    public final void setAuthenticationKey(final byte[] value) {
        authenticationKey = value;
    }

    /**
     * @return Invocation Counter.
     */
    public final int getInvocationCounter() {
        return invocationCounter;
    }

    /**
     * @param value
     *            Invocation Counter.
     */
    public final void setInvocationCounter(final int value) {
        invocationCounter = value;
    }

    /**
     * Convert string to byte array.
     * 
     * @param value
     *            Hex string.
     * @return Parsed byte array.
     */
    public static byte[] hexToBytes(final String value) {
        return GXCommon.hexToBytes(value);
    }

    /**
     * Convert byte array to hex string.
     * 
     * @param bytes
     *            Byte array.
     * @return Hex string.
     */
    public static String toHex(final byte[] bytes) {
        return GXCommon.toHex(bytes, true);
    }

    /**
     * Is only complete PDU parsed and shown. {@link messageToXml}
     * {@link PduOnly}
     */
    private boolean completePdu;

    public final boolean getCompletePdu() {
        return completePdu;
    }

    public final void setCompleatePdu(final boolean value) {
        completePdu = value;
    }

    /**
     * @return Is string serialized as hex.
     */
    public final boolean getShowStringAsHex() {
        return showStringAsHex;
    }

    /**
     * @param value
     *            Is string serialized as hex.
     */
    public final void setShowStringAsHex(final boolean value) {
        showStringAsHex = value;
    }

    /**
     * Find next frame from the string. Position of data is set to the begin of
     * new frame. If PDU is null it is not updated.
     * 
     * @param data
     *            Data where frame is search.
     * @param pdu
     *            PDU of received frame is set here.
     * @return Is new frame found.
     */
    public final boolean findNextFrame(final GXByteBuffer data,
            final GXByteBuffer pdu) {
        GXDLMSSettings settings = new GXDLMSSettings(true);
        GXReplyData reply = new GXReplyData();
        reply.setXml(new GXDLMSTranslatorStructure(outputType, omitXmlNameSpace,
                hex, getShowStringAsHex(), comments, tags));
        int pos;
        boolean found;
        while (data.position() < data.size()) {
            if (data.getUInt8(data.position()) == 0x7e) {
                pos = data.position();
                settings.setInterfaceType(InterfaceType.HDLC);
                found = GXDLMS.getData(settings, data, reply, null);
                data.position(pos);
                if (found) {
                    break;
                }
            } else if (data.getUInt16(data.position()) == 0x1) {
                pos = data.position();
                settings.setInterfaceType(InterfaceType.WRAPPER);
                found = GXDLMS.getData(settings, data, reply, null);
                data.position(pos);
                if (found) {
                    break;
                }
            }
            data.position(data.position() + 1);
        }
        if (pdu != null) {
            pdu.clear();
            pdu.set(reply.getData().getData(), 0, reply.getData().size());
        }
        return data.position() != data.size();
    }

    /**
     * Find next frame from the string. Position of data is set to the begin of
     * new frame. If PDU is null it is not updated.
     * 
     * @param data
     *            Data where frame is search.
     * @param pdu
     *            PDU of received frame is set here.
     * @param type
     *            Interface type.
     * @return Is new frame found.
     */
    public final boolean findNextFrame(final GXByteBuffer data,
            final GXByteBuffer pdu, final InterfaceType type) {
        GXDLMSSettings settings = new GXDLMSSettings(true);
        settings.setInterfaceType(type);
        GXReplyData reply = new GXReplyData();
        reply.setXml(new GXDLMSTranslatorStructure(outputType, omitXmlNameSpace,
                hex, getShowStringAsHex(), comments, tags));
        int pos;
        boolean found;
        try {
            while (data.position() < data.size()) {
                if (type == InterfaceType.HDLC
                        && data.getUInt8(data.position()) == 0x7e) {
                    pos = data.position();
                    found = GXDLMS.getData(settings, data, reply, null);
                    data.position(pos);
                    if (found) {
                        break;
                    }
                } else if (data.available() > 1 && type == InterfaceType.WRAPPER
                        && data.getUInt16(data.position()) == 0x1) {
                    pos = data.position();
                    found = GXDLMS.getData(settings, data, reply, null);
                    data.position(pos);
                    if (found) {
                        break;
                    }
                } else if (type == InterfaceType.WIRELESS_MBUS) {
                    pos = data.position();
                    settings.setInterfaceType(InterfaceType.WIRELESS_MBUS);
                    found = GXDLMS.getData(settings, data, reply, null);
                    data.position(pos);
                    if (found) {
                        break;
                    }
                }
                data.position(data.position() + 1);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Invalid DLMS frame.");
        }
        if (pdu != null) {
            pdu.clear();
            pdu.set(reply.getData().getData(), 0, reply.getData().size());
        }
        return data.position() != data.size();
    }

    static void addTag(final HashMap<Integer, String> list, final int value,
            final String text) {
        list.put(value, text);
    }

    /**
     * Get all tags.
     * 
     * @param type
     *            Output type.
     * @param list
     *            List of tags by ID.
     * @param tagsByName
     *            List of tags by name.
     */
    private static void getTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list,
            final HashMap<String, Integer> tagsByName) {
        if (type == TranslatorOutputType.SIMPLE_XML) {
            TranslatorSimpleTags.getGeneralTags(type, list);
            TranslatorSimpleTags.getSnTags(type, list);
            TranslatorSimpleTags.getLnTags(type, list);
            TranslatorSimpleTags.getGloTags(type, list);
            TranslatorSimpleTags.getDedTags(type, list);
            TranslatorSimpleTags.getTranslatorTags(type, list);
            TranslatorSimpleTags.getDataTypeTags(list);
        } else {
            TranslatorStandardTags.getGeneralTags(type, list);
            TranslatorStandardTags.getSnTags(type, list);
            TranslatorStandardTags.getLnTags(type, list);
            TranslatorStandardTags.getGloTags(type, list);
            TranslatorStandardTags.getDedTags(type, list);
            TranslatorStandardTags.getTranslatorTags(type, list);
            TranslatorStandardTags.getDataTypeTags(list);

        }
        // Simple is not case sensitive.
        boolean lowercase = type == TranslatorOutputType.SIMPLE_XML;
        for (Entry<Integer, String> it : list.entrySet()) {
            String str = it.getValue();
            if (lowercase) {
                str = str.toLowerCase();
            }
            if (!tagsByName.containsKey(str)) {
                tagsByName.put(str, it.getKey());
            }
        }
    }

    public final byte[] getPdu(final byte[] value) {
        return getPdu(new GXByteBuffer(value));
    }

    /**
     * Identify used DLMS framing type.
     * 
     * @param value
     *            Input data.
     * @return Interface type.
     */
    public static InterfaceType getDlmsFraming(final GXByteBuffer value) {
        for (int pos = value.position(); pos != value.size(); ++pos) {
            if (value.getUInt8(pos) == 0x7e) {
                return InterfaceType.HDLC;
            }
            if (value.available() > 1 && value.getUInt16(pos) == 1) {
                return InterfaceType.WRAPPER;
            }
            if (GXDLMS.isMBusData(value)) {
                return InterfaceType.WIRELESS_MBUS;
            }
        }
        throw new IllegalArgumentException("Invalid DLMS framing.");
    }

    public final byte[] getPdu(final GXByteBuffer value) {
        InterfaceType framing = getDlmsFraming(value);

        GXReplyData data = new GXReplyData();
        data.setXml(new GXDLMSTranslatorStructure(outputType, omitXmlNameSpace,
                hex, getShowStringAsHex(), comments, tags));
        GXDLMSSettings settings = new GXDLMSSettings(true);
        settings.setInterfaceType(framing);
        GXDLMS.getData(settings, value, data, null);
        return data.getData().array();
    }

    private GXCiphering getCiphering() {
        if (security != Security.NONE) {
            GXCiphering c = new GXCiphering(systemTitle);
            c.setSecurity(security);
            c.setSystemTitle(systemTitle);
            c.setBlockCipherKey(blockCipherKey);
            c.setAuthenticationKey(authenticationKey);
            c.setInvocationCounter(invocationCounter);
            return c;
        }
        return null;
    }

    /**
     * Clear {@link messageToXml} internal settings.
     */
    public void clear() {
        multipleFrames = false;
        pduFrames.clear();
    }

    /**
     * Convert message to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted xml. {@link setPduOnly} {@link setCompleatePdu}
     */
    public final String messageToXml(final byte[] value) {
        return messageToXml(new GXByteBuffer(value));
    }

    private void checkFrame(final short frame,
            final GXDLMSTranslatorStructure xml) {
        if (frame == 0x93) {
            xml.appendComment("SNRM frame.");
        } else if (frame == 0x73) {
            xml.appendComment("UA frame.");
        } else if ((frame
                & HdlcFrameType.S_FRAME.getValue()) == HdlcFrameType.S_FRAME
                        .getValue()) {
            // If S -frame.
            xml.appendComment("S frame.");
        } else if ((frame & 1) == HdlcFrameType.U_FRAME.getValue()) {
            // Handle U-frame.
            xml.appendComment("U frame.");
        } else {
            // I-frame.
            if (frame == 0x10) {
                xml.appendComment("AARQ frame.");
            } else if (frame == 0x30) {
                xml.appendComment("AARE frame.");
            } else {
                xml.appendComment("I frame.");
            }
        }
    }

    /**
     * Convert message to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted XML. {@link clear} {@link setPduOnly}
     *         {@link setCompleatePdu}
     */
    public final String messageToXml(final GXByteBuffer value) {
        if (value == null || value.size() == 0) {
            throw new IllegalArgumentException("value");
        }
        try {
            GXDLMSTranslatorStructure xml =
                    new GXDLMSTranslatorStructure(outputType, omitXmlNameSpace,
                            hex, getShowStringAsHex(), comments, tags);
            GXReplyData data = new GXReplyData();
            data.setXml(xml);
            int offset = value.position();
            GXDLMSSettings settings = new GXDLMSSettings(true);
            settings.setCipher(getCiphering());
            // If HDLC framing.
            if (value.getUInt8(value.position()) == 0x7e) {
                settings.setInterfaceType(InterfaceType.HDLC);
                if (GXDLMS.getData(settings, value, data, null)) {
                    if (!getPduOnly()) {
                        xml.appendLine("<HDLC len=\""
                                + xml.integerToHex(
                                        data.getPacketLength() - offset, 0)
                                + "\" >");
                        xml.appendLine(
                                "<TargetAddress Value=\""
                                        + xml.integerToHex(
                                                settings.getServerAddress(), 0)
                                        + "\" />");
                        xml.appendLine(
                                "<SourceAddress Value=\""
                                        + xml.integerToHex(
                                                settings.getClientAddress(), 0)
                                        + "\" />");
                        // Check frame.
                        if (comments) {
                            checkFrame(data.getFrameId(), xml);
                        }
                        xml.appendLine("<FrameType Value=\""
                                + xml.integerToHex(data.getFrameId(), 2, true)
                                + "\" />");
                    }
                    if (data.getData().size() == 0) {
                        if ((data.getFrameId() & 1) != 0
                                && data.getCommand() == Command.NONE) {
                            if (!getCompletePdu()) {
                                xml.appendLine(
                                        "<Command Value=\"NextFrame\" />");
                            }
                            multipleFrames = true;
                        } else {
                            xml.appendStartTag(data.getCommand());
                            xml.appendEndTag(data.getCommand());
                        }
                    } else {
                        if (multipleFrames || data.isMoreData()) {
                            if (getCompletePdu()) {
                                pduFrames.set(data.getData().getData());
                                if (data.getMoreData() == RequestTypes.NONE) {
                                    xml.appendLine(
                                            pduToXml(pduFrames, true, true));
                                    pduFrames.clear();
                                }
                            } else {
                                xml.appendLine("<NextFrame Value=\""
                                        + GXCommon.toHex(
                                                data.getData().getData(), false,
                                                data.getData().position(),
                                                data.getData().size() - data
                                                        .getData().position())
                                        + "\" />");
                            }
                            if (data.getMoreData() != RequestTypes.DATABLOCK) {
                                multipleFrames = false;
                            }
                        } else {
                            if (!getPduOnly()) {
                                xml.appendLine("<PDU>");
                            }
                            if (pduFrames.size() != 0) {
                                pduFrames.set(data.getData().getData());
                                xml.appendLine(pduToXml(pduFrames, true, true));
                                pduFrames.clear();
                            } else {
                                if (data.getCommand() == Command.SNRM
                                        || data.getCommand() == Command.UA) {
                                    xml.appendStartTag(data.getCommand());
                                    pduToXml(xml, data.getData(), true, true,
                                            true);
                                    xml.appendEndTag(data.getCommand());
                                    xml.setXmlLength(xml.getXmlLength() + 2);
                                } else {
                                    xml.appendLine(pduToXml(data.getData(),
                                            true, true));
                                }
                            }
                            // Remove \r\n.
                            xml.trim();
                            if (!getPduOnly()) {
                                xml.appendLine("</PDU>");
                            }
                        }
                    }
                    if (!getPduOnly()) {
                        xml.appendLine("</HDLC>");
                    }
                }
                return xml.toString();
            }
            // If wrapper.
            if (value.getUInt16(value.position()) == 1) {
                settings.setInterfaceType(InterfaceType.WRAPPER);
                GXDLMS.getData(settings, value, data, null);
                if (!getPduOnly()) {
                    xml.appendLine(
                            "<WRAPPER len=\""
                                    + xml.integerToHex(
                                            data.getPacketLength() - offset, 0)
                                    + "\" >");
                    xml.appendLine("<TargetAddress Value=\""
                            + xml.integerToHex(settings.getClientAddress(), 0)
                            + "\" />");
                    xml.appendLine("<SourceAddress Value=\""
                            + xml.integerToHex(settings.getServerAddress(), 0)
                            + "\" />");
                }
                if (data.getData().size() == 0) {
                    xml.appendLine("<Command Value=\""
                            + Command.toString(data.getCommand()) + "\" />");
                } else {
                    if (!getPduOnly()) {
                        xml.appendLine("<PDU>");
                    }
                    xml.appendLine(pduToXml(data.getData(), true, true));
                    // Remove \r\n.
                    xml.trim();
                    if (!getPduOnly()) {
                        xml.appendLine("</PDU>");
                    }
                }
                if (!getPduOnly()) {
                    xml.appendLine("</WRAPPER>");
                }
                return xml.toString();
            }
            // If Wireless M-Bus.
            else if (GXDLMS.isMBusData(value)) {
                settings.setInterfaceType(InterfaceType.WIRELESS_MBUS);
                int len = xml.getXmlLength();
                GXDLMS.getData(settings, value, data, null);
                String tmp = xml.toString().substring(len);
                xml.setXmlLength(len);
                if (!getPduOnly()) {
                    xml.appendLine(
                            "<WirelessMBus len=\""
                                    + xml.integerToHex(
                                            data.getPacketLength() - offset, 0)
                                    + "\" >");
                    xml.appendLine("<TargetAddress Value=\""
                            + xml.integerToHex(settings.getServerAddress(), 0)
                            + "\" />");
                    xml.appendLine("<SourceAddress Value=\""
                            + xml.integerToHex(settings.getClientAddress(), 0)
                            + "\" />");
                    xml.append(tmp);
                }
                if (data.getData().size() == 0) {
                    xml.appendLine("<Command Value=\""
                            + Command.toString(data.getCommand()) + "\" />");
                } else {
                    if (!getPduOnly()) {
                        xml.appendLine("<PDU>");
                    }
                    xml.appendLine(pduToXml(data.getData(), true, true));
                    // Remove \r\n.
                    xml.trim();
                    if (!getPduOnly()) {
                        xml.appendLine("</PDU>");
                    }
                }
                if (!getPduOnly()) {
                    xml.appendLine("</WRAPPER>");
                }
                return xml.toString();
            }

        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        throw new IllegalArgumentException("Invalid DLMS framing.");
    }

    /**
     * Convert PDU in hex string to XML.
     * 
     * @param pdu
     *            Converted hex string.
     * @return Converted XML.
     */
    public final String pduToXml(final String pdu) {
        byte[] tmp = GXCommon.hexToBytes(pdu);
        return pduToXml(tmp);
    }

    /**
     * Convert PDU bytes to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted XML.
     */
    public final String pduToXml(final byte[] value) {
        GXByteBuffer tmp = new GXByteBuffer(value);
        return pduToXml(tmp);
    }

    private void getUa(final GXByteBuffer data,
            final GXDLMSTranslatorStructure xml) {
        data.getUInt8(); // Skip FromatID
        data.getUInt8(); // Skip Group ID.
        data.getUInt8(); // Skip Group length.
        Object val;
        while (data.position() < data.size()) {
            short id = data.getUInt8();
            short len = data.getUInt8();
            switch (len) {
            case 1:
                val = data.getUInt8();
                break;
            case 2:
                val = data.getUInt16();
                break;
            case 4:
                val = data.getUInt32();
                break;
            default:
                throw new GXDLMSException("Invalid Exception.");
            }
            switch (id) {
            case HDLCInfo.MAX_INFO_TX:
                xml.appendLine(
                        "<MaxInfoTX Value=\"" + val.toString() + "\" />");
                break;
            case HDLCInfo.MAX_INFO_RX:
                xml.appendLine(
                        "<MaxInfoRX Value=\"" + val.toString() + "\" />");
                break;
            case HDLCInfo.WINDOW_SIZE_TX:
                xml.appendLine(
                        "<WindowSizeTX Value=\"" + val.toString() + "\" />");
                break;
            case HDLCInfo.WINDOW_SIZE_RX:
                xml.appendLine(
                        "<WindowSizeRX Value=\"" + val.toString() + "\" />");
                break;
            default:
                throw new GXDLMSException("Invalid UA response.");
            }
        }
    }

    public final String pduToXml(final GXByteBuffer value) {
        return pduToXml(value, omitXmlDeclaration, omitXmlNameSpace);
    }

    /**
     * @return Are comments added.
     */
    public boolean isComments() {
        return comments;
    }

    /**
     * @param value
     *            Are comments added.
     */
    public void setComments(final boolean value) {
        comments = value;
    }

    /**
     * Convert bytes to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted XML.
     */
    private String pduToXml(final GXByteBuffer value,
            final boolean omitDeclaration, final boolean omitNameSpace) {
        GXDLMSTranslatorStructure xml =
                new GXDLMSTranslatorStructure(outputType, omitXmlNameSpace, hex,
                        getShowStringAsHex(), comments, tags);
        return pduToXml(xml, value, omitDeclaration, omitNameSpace, true);
    }

    /**
     * Convert bytes to XML.
     * 
     * @param value
     *            Bytes to convert.
     * @return Converted XML.
     */
    String pduToXml(final GXDLMSTranslatorStructure xml,
            final GXByteBuffer value, final boolean omitDeclaration,
            final boolean omitNameSpace, final boolean allowUnknownCommand) {
        if (value == null || value.size() == 0) {
            throw new IllegalArgumentException("value");
        }
        try {
            GXDLMSSettings settings = new GXDLMSSettings(true);
            settings.setCipher(getCiphering());
            GXReplyData data = new GXReplyData();
            short cmd = value.getUInt8();
            String str;
            switch (cmd) {
            case Command.AARQ:
                value.position(0);
                GXAPDU.parsePDU(settings, settings.getCipher(), value, xml);
                break;
            case Command.INITIATE_REQUEST:
                value.position(0);
                settings = new GXDLMSSettings(true);
                GXAPDU.parseInitiate(true, settings, settings.getCipher(),
                        value, xml);
                break;
            case Command.INITIATE_RESPONSE:
                value.position(0);
                settings = new GXDLMSSettings(false);
                settings.setCipher(getCiphering());
                GXAPDU.parseInitiate(true, settings, settings.getCipher(),
                        value, xml);
                break;
            case 0x81: // Ua
                value.position(0);
                getUa(value, xml);
                break;
            case Command.AARE:
                value.position(0);
                settings = new GXDLMSSettings(false);
                settings.setCipher(getCiphering());
                GXAPDU.parsePDU(settings, settings.getCipher(), value, xml);
                break;
            case Command.GET_REQUEST:
                GXDLMSLNCommandHandler.handleGetRequest(settings, null, value,
                        null, xml, Command.NONE);
                break;
            case Command.SET_REQUEST:
                GXDLMSLNCommandHandler.handleSetRequest(settings, null, value,
                        null, xml, Command.NONE);
                break;
            case Command.READ_REQUEST:
                GXDLMSSNCommandHandler.handleReadRequest(settings, null, value,
                        null, xml, Command.NONE);
                break;
            case Command.METHOD_REQUEST:
                GXDLMSLNCommandHandler.handleMethodRequest(settings, null,
                        value, null, null, xml, Command.NONE);
                break;
            case Command.WRITE_REQUEST:
                GXDLMSSNCommandHandler.handleWriteRequest(settings, null, value,
                        null, xml, Command.NONE);
                break;
            case Command.ACCESS_REQUEST:
                GXDLMSLNCommandHandler.handleAccessRequest(settings, null,
                        value, null, xml, Command.NONE);
                break;
            case Command.DATA_NOTIFICATION:
                data.setXml(xml);
                data.setData(value);
                value.position(0);
                GXDLMS.getPdu(settings, data);
                break;
            case Command.INFORMATION_REPORT:
                data.setXml(xml);
                data.setData(value);
                GXDLMSSNCommandHandler.handleInformationReport(settings, data,
                        null);
                break;
            case Command.EVENT_NOTIFICATION:
                data.setXml(xml);
                data.setData(value);
                GXDLMSLNCommandHandler.handleEventNotification(settings, data,
                        null);
                break;
            case Command.READ_RESPONSE:
            case Command.WRITE_RESPONSE:
            case Command.GET_RESPONSE:
            case Command.SET_RESPONSE:
            case Command.METHOD_RESPONSE:
            case Command.ACCESS_RESPONSE:
            case Command.GENERAL_BLOCK_TRANSFER:
                data.setXml(xml);
                data.setData(value);
                value.position(0);
                GXDLMS.getPdu(settings, data);
                break;
            case Command.GENERAL_CIPHERING:
                settings.setCipher(new GXCiphering("ABCDEFGH".getBytes()));
                data.setXml(xml);
                data.setData(value);
                value.position(0);
                GXDLMS.getPdu(settings, data);
                break;
            case Command.RELEASE_REQUEST:
                xml.appendStartTag(cmd);
                value.getUInt8();
                // Len.
                if (value.available() != 0) {
                    // BerType
                    value.getUInt8();
                    // Len.
                    value.getUInt8();
                    if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                        str = TranslatorSimpleTags.releaseRequestReasonToString(
                                ReleaseRequestReason
                                        .forValue(value.getUInt8()));
                    } else {
                        str = TranslatorStandardTags
                                .releaseRequestReasonToString(
                                        ReleaseRequestReason
                                                .forValue(value.getUInt8()));
                    }
                    xml.appendLine(TranslatorTags.REASON, "Value", str);
                    if (value.available() != 0) {
                        GXAPDU.parsePDU2(settings, settings.getCipher(), value,
                                xml);
                    }
                }
                xml.appendEndTag(cmd);
                break;
            case Command.RELEASE_RESPONSE:
                xml.appendStartTag(cmd);
                value.getUInt8();
                // Len.
                if (value.available() != 0) {
                    // BerType
                    value.getUInt8();
                    // Len.
                    value.getUInt8();
                    if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                        str = TranslatorSimpleTags
                                .releaseResponseReasonToString(
                                        ReleaseResponseReason
                                                .forValue(value.getUInt8()));
                    } else {
                        str = TranslatorStandardTags
                                .releaseResponseReasonToString(
                                        ReleaseResponseReason
                                                .forValue(value.getUInt8()));
                    }
                    xml.appendLine(TranslatorTags.REASON, "Value", str);
                    if (value.available() != 0) {
                        GXAPDU.parsePDU2(settings, settings.getCipher(), value,
                                xml);
                    }
                }
                xml.appendEndTag(cmd);
                break;
            case Command.GLO_READ_REQUEST:
            case Command.GLO_WRITE_REQUEST:
            case Command.GLO_GET_REQUEST:
            case Command.GLO_SET_REQUEST:
            case Command.GLO_READ_RESPONSE:
            case Command.GLO_WRITE_RESPONSE:
            case Command.GLO_GET_RESPONSE:
            case Command.GLO_SET_RESPONSE:
            case Command.GLO_METHOD_REQUEST:
            case Command.GLO_METHOD_RESPONSE:
            case Command.DED_GET_REQUEST:
            case Command.DED_SET_REQUEST:
            case Command.DED_GET_RESPONSE:
            case Command.DED_SET_RESPONSE:
            case Command.DED_METHOD_REQUEST:
            case Command.DED_METHOD_RESPONSE:
                if (settings.getCipher() != null && comments) {
                    int originalPosition = value.position();
                    int len = xml.getXmlLength();
                    try {
                        value.position(value.position() - 1);
                        int c = cmd;
                        byte[] st;
                        if (c == Command.GLO_READ_REQUEST
                                || c == Command.GLO_WRITE_REQUEST
                                || c == Command.GLO_GET_REQUEST
                                || c == Command.GLO_SET_REQUEST
                                || c == Command.GLO_METHOD_REQUEST
                                || c == Command.DED_GET_REQUEST
                                || c == Command.DED_SET_REQUEST
                                || c == Command.DED_METHOD_REQUEST) {
                            st = settings.getCipher().getSystemTitle();
                        } else {
                            st = settings.getSourceSystemTitle();
                        }
                        if (st != null) {
                            AesGcmParameter p;
                            if (c == Command.DED_GET_REQUEST
                                    || c == Command.DED_SET_REQUEST
                                    || c == Command.DED_METHOD_REQUEST) {
                                p = new AesGcmParameter(st,
                                        settings.getCipher().getDedicatedKey(),
                                        settings.getCipher()
                                                .getAuthenticationKey());
                            } else {
                                p = new AesGcmParameter(st,
                                        settings.getCipher()
                                                .getBlockCipherKey(),
                                        settings.getCipher()
                                                .getAuthenticationKey());
                            }
                            if (p.getBlockCipherKey() != null) {
                                GXByteBuffer data2 =
                                        new GXByteBuffer(GXCiphering.decrypt(
                                                settings.getCipher(), p,
                                                value));
                                xml.startComment("Decrypt data:");
                                pduToXml(xml, data2, omitDeclaration,
                                        omitNameSpace, false);
                                xml.endComment();
                            }
                        }
                    } catch (Exception e) {
                        // It's OK if this fails. Ciphering settings are not
                        // correct.
                        xml.setXmlLength(len);
                    }
                    value.position(originalPosition);
                }
                int cnt = GXCommon.getObjectCount(value);
                if (cnt != value.size() - value.position()) {
                    xml.appendComment("Invalid length: " + String.valueOf(cnt)
                            + ". It should be: "
                            + String.valueOf(value.size() - value.position()));
                }
                xml.appendLine(cmd, "Value",
                        GXCommon.toHex(value.getData(), false, value.position(),
                                value.size() - value.position()));
                break;
            case Command.GENERAL_GLO_CIPHERING:
                if (settings.getCipher() != null && comments) {
                    int len = xml.getXmlLength();
                    try {
                        GXByteBuffer tmp = new GXByteBuffer();
                        tmp.set(value.getData(), value.position() - 1,
                                value.size() - value.position() + 1);
                        AesGcmParameter p;
                        p = new AesGcmParameter(
                                settings.getCipher().getSystemTitle(),
                                settings.getCipher().getBlockCipherKey(),
                                settings.getCipher().getAuthenticationKey());
                        tmp = new GXByteBuffer(GXCiphering
                                .decrypt(settings.getCipher(), p, tmp));
                        xml.startComment("Decrypt data:");
                        pduToXml(xml, tmp, omitDeclaration, omitNameSpace,
                                false);
                        xml.endComment();
                    } catch (Exception e) {
                        // It's OK if this fails. Ciphering settings are not
                        // correct.
                        xml.setXmlLength(len);
                    }
                }
                int len = GXCommon.getObjectCount(value);
                byte[] tmp = new byte[len];
                value.get(tmp);
                xml.appendStartTag(Command.GENERAL_GLO_CIPHERING);
                xml.appendLine(TranslatorTags.SYSTEM_TITLE, null,
                        GXCommon.toHex(tmp, false, 0, len));
                len = GXCommon.getObjectCount(value);
                tmp = new byte[len];
                value.get(tmp);
                xml.appendLine(TranslatorTags.CIPHERED_SERVICE, null,
                        GXCommon.toHex(tmp, false, 0, len));
                xml.appendEndTag(Command.GENERAL_GLO_CIPHERING);
                break;
            case Command.CONFIRMED_SERVICE_ERROR:
                data.setXml(xml);
                data.setData(value);
                GXDLMS.handleConfirmedServiceError(data);
                break;
            case Command.GATEWAY_REQUEST:
            case Command.GATEWAY_RESPONSE:
                data.setXml(xml);
                data.setData(value);
                // Get Network ID.
                short id = value.getUInt8();
                // Get Physical device address.
                len = GXCommon.getObjectCount(value);
                tmp = new byte[len];
                value.get(tmp);
                xml.appendStartTag(cmd);
                xml.appendLine(TranslatorTags.NETWORK_ID, null,
                        String.valueOf(id));
                xml.appendLine(TranslatorTags.PHYSICAL_DEVICE_ADDRESS, null,
                        GXCommon.toHex(tmp, false, 0, len));
                pduToXml(xml, new GXByteBuffer(value.remaining()),
                        omitDeclaration, omitNameSpace, allowUnknownCommand);
                xml.appendEndTag(cmd);
                break;
            default:
                if (!allowUnknownCommand) {
                    throw new Exception("Invalid command.");
                }
                xml.appendLine(
                        "<Data=\""
                                + GXCommon.toHex(value.getData(), false,
                                        value.position(),
                                        value.size() - value.position())
                                + "\" />");
                break;
            }
            if (outputType == TranslatorOutputType.STANDARD_XML) {
                StringBuilder sb = new StringBuilder();
                if (!omitDeclaration) {
                    sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
                }
                if (!omitNameSpace) {
                    // CHECKSTYLE:OFF
                    if (cmd != Command.AARE && cmd != Command.AARQ
                            && cmd != Command.RELEASE_REQUEST
                            && cmd != Command.RELEASE_RESPONSE) {
                        sb.append(
                                "<x:xDLMS-APDU xmlns:x=\"http://www.dlms.com/COSEMpdu\">\r\n");
                    } else {
                        sb.append(
                                "<x:aCSE-APDU xmlns:x=\"http://www.dlms.com/COSEMpdu\">\r\n");
                    }
                    // CHECKSTYLE:ON
                }
                sb.append(xml.toString());
                if (!omitNameSpace) {
                    if (cmd != Command.AARE && cmd != Command.AARQ
                            && cmd != Command.RELEASE_REQUEST
                            && cmd != Command.RELEASE_RESPONSE) {
                        sb.append("</x:xDLMS-APDU>\r\n");
                    } else {
                        sb.append("</x:aCSE-APDU>\r\n");
                    }
                }
                return sb.toString();
            }
            return xml.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Get command from XML.
     * 
     * @param node
     *            XML node.
     * @param s
     *            XML settings.
     * @param tag
     *            Tag.
     */
    private static void getCommand(final Node node, final GXDLMSXmlSettings s,
            final int tag) {
        s.setCommand(tag);
        byte[] tmp;
        switch (tag) {
        case Command.SNRM:
        case Command.AARQ:
        case Command.READ_REQUEST:
        case Command.WRITE_REQUEST:
        case Command.GET_REQUEST:
        case Command.SET_REQUEST:
        case Command.RELEASE_REQUEST:
        case Command.METHOD_REQUEST:
        case Command.ACCESS_REQUEST:
        case Command.INITIATE_REQUEST:
        case Command.CONFIRMED_SERVICE_ERROR:
            s.getSettings().setServer(false);
            break;
        case Command.GLO_INITIATE_REQUEST:
        case Command.GLO_GET_REQUEST:
        case Command.GLO_SET_REQUEST:
        case Command.GLO_METHOD_REQUEST:
        case Command.GLO_READ_REQUEST:
        case Command.GLO_WRITE_REQUEST:
        case Command.DED_GET_REQUEST:
        case Command.DED_SET_REQUEST:
        case Command.DED_METHOD_REQUEST:
            s.getSettings().setServer(false);
            s.setCommand(tag);
            tmp = GXCommon.hexToBytes(getValue(node, s));
            s.getSettings().getCipher()
                    .setSecurity(gurux.dlms.enums.Security.forValue(tmp[0]));
            s.getData().set(tmp);
            break;
        case Command.UA:
        case Command.AARE:
        case Command.GET_RESPONSE:
        case Command.SET_RESPONSE:
        case Command.READ_RESPONSE:
        case Command.WRITE_RESPONSE:
        case Command.METHOD_RESPONSE:
        case Command.RELEASE_RESPONSE:
        case Command.DATA_NOTIFICATION:
        case Command.ACCESS_RESPONSE:
        case Command.INITIATE_RESPONSE:
        case (int) Command.INFORMATION_REPORT:
        case (int) Command.EVENT_NOTIFICATION:
            break;
        case Command.GLO_INITIATE_RESPONSE:
        case Command.GLO_GET_RESPONSE:
        case Command.GLO_SET_RESPONSE:
        case Command.GLO_METHOD_RESPONSE:
        case Command.GLO_READ_RESPONSE:
        case Command.GLO_WRITE_RESPONSE:
        case Command.GLO_EVENT_NOTIFICATION_REQUEST:
        case Command.DED_GET_RESPONSE:
        case Command.DED_SET_RESPONSE:
        case Command.DED_METHOD_RESPONSE:
        case Command.DED_EVENT_NOTIFICATION:
            s.setCommand(tag);
            tmp = GXCommon.hexToBytes(getValue(node, s));
            s.getSettings().getCipher()
                    .setSecurity(gurux.dlms.enums.Security.forValue(tmp[0]));
            s.getData().set(tmp);
            break;
        case Command.GENERAL_GLO_CIPHERING:
            s.setCommand(tag);
            break;
        case Command.GENERAL_CIPHERING:
            s.setCommand(tag);
            break;
        case TranslatorTags.FRAME_TYPE:
            s.setCommand(0);
            break;
        case Command.GATEWAY_REQUEST:
            s.setGwCommand(Command.GATEWAY_REQUEST);
            s.getSettings().setServer(false);
            break;
        case Command.GATEWAY_RESPONSE:
            s.setGwCommand(Command.GATEWAY_RESPONSE);
            break;
        default:
            throw new IllegalArgumentException(
                    "Invalid Command: " + node.getNodeName());
        }
    }

    private static boolean getFrame(final Node node, final GXDLMSXmlSettings s,
            final int tag) {
        boolean found = true;
        switch (tag) {
        case TranslatorTags.WRAPPER:
            s.getSettings().setInterfaceType(InterfaceType.WRAPPER);
            break;
        case TranslatorTags.HDLC:
            s.getSettings().setInterfaceType(InterfaceType.HDLC);
            break;
        case TranslatorTags.TARGET_ADDRESS:
            s.getSettings().setServerAddress(s.parseInt(getValue(node, s)));
            break;
        case TranslatorTags.SOURCE_ADDRESS:
            s.getSettings().setClientAddress(s.parseInt(getValue(node, s)));
            break;
        default:
            // It's OK if frame is not found.
            found = false;
        }
        return found;
    }

    /**
     * Get amount of clild nodes.
     * 
     * @param node
     *            Target node.
     * @return Amount of child nodes.
     */
    private static int getNodeCount(final Node node) {
        int cnt = 0;
        for (int pos = 0; pos != node.getChildNodes().getLength(); ++pos) {
            Node node2 = node.getChildNodes().item(pos);
            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                ++cnt;
            }
        }
        return cnt;
    }

    /**
     * Handle AARE and AARQ XML tags.
     * 
     * @param node
     *            XML node.
     * @param s
     *            XML Settings.
     * @param tag
     *            XML tag.
     */
    private static void handleAarqAare(final Node node,
            final GXDLMSXmlSettings s, final int tag) {
        byte[] tmp;
        Set<Conformance> list;
        int value;
        switch (tag) {
        case TranslatorGeneralTags.APPLICATION_CONTEXT_NAME:
            if (s.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                value = Integer.parseInt(node.getFirstChild().getNodeValue());
                switch (value) {
                case 1:
                    s.getSettings().setUseLogicalNameReferencing(true);
                    break;
                case 2:
                    s.getSettings().setUseLogicalNameReferencing(false);
                    break;
                case 3:
                    s.getSettings().setUseLogicalNameReferencing(true);
                    break;
                case 4:
                    s.getSettings().setUseLogicalNameReferencing(false);
                    break;
                default:
                    throw new RuntimeException(
                            "Invalid application context name.");
                }
            } else {
                if (node.getAttributes().item(0).getNodeValue()
                        .compareTo("SN") == 0
                        || node.getAttributes().item(0).getNodeValue()
                                .compareTo("SN_WITH_CIPHERING") == 0) {
                    s.getSettings().setUseLogicalNameReferencing(false);
                } else if (node.getAttributes().item(0).getNodeValue()
                        .compareTo("LN") == 0
                        || node.getAttributes().item(0).getNodeValue()
                                .compareTo("LN_WITH_CIPHERING") == 0) {
                    s.getSettings().setUseLogicalNameReferencing(true);
                } else {
                    throw new IllegalArgumentException(
                            "Invalid Reference type name.");
                }
            }
            break;
        case Command.GLO_INITIATE_REQUEST:
            s.getSettings().setServer(false);
            s.setCommand(tag);
            tmp = GXCommon.hexToBytes(getValue(node, s));
            s.getSettings().getCipher()
                    .setSecurity(gurux.dlms.enums.Security.forValue(tmp[0]));
            s.getData().set(tmp);
            break;
        case Command.GLO_INITIATE_RESPONSE:
            s.setCommand(tag);
            tmp = GXCommon.hexToBytes(getValue(node, s));
            s.getSettings().getCipher()
                    .setSecurity(gurux.dlms.enums.Security.forValue(tmp[0]));
            s.getData().set(tmp);
            break;
        case Command.INITIATE_REQUEST:
        case Command.INITIATE_RESPONSE:
            break;
        case TranslatorGeneralTags.USER_INFORMATION:
            if (s.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                GXByteBuffer bb = new GXByteBuffer();
                tmp = GXCommon.hexToBytes(getValue(node, s));
                bb.set(tmp);
                if (s.getSettings().isServer()) {
                    s.getSettings().setProposedConformance(
                            Conformance.forValue(0xFFFFFF));
                }
                GXAPDU.parseInitiate(false, s.getSettings(),
                        s.getSettings().getCipher(), bb, null);
                // Update proposed conformance or XML to PDU will fail.
                if (!s.getSettings().isServer()) {
                    s.getSettings().setProposedConformance(
                            s.getSettings().getNegotiatedConformance());
                }
            }
            break;
        case 0xBE00:
        case 0xBE06:
        case 0xBE01:
        case 0x8A:
            break;
        case 0xBE04:
            String str = getValue(node, s);
            if (Integer.parseInt(str, 16) == 07) {
                s.getSettings().setUseLogicalNameReferencing(true);
            } else {
                s.getSettings().setUseLogicalNameReferencing(false);
            }
            break;
        case 0x8B:
        case 0x89:
            // MechanismName.
            if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                s.getSettings().setAuthentication(
                        Authentication.valueOfString(getValue(node, s)));
            } else {
                s.getSettings().setAuthentication(Authentication
                        .forValue(Integer.parseInt(getValue(node, s))));
            }
            break;
        case 0xAC:
            // CallingAuthentication.
            if (s.getSettings().getAuthentication() == Authentication.LOW) {
                s.getSettings()
                        .setPassword(GXCommon.hexToBytes(getValue(node, s)));
            } else {
                s.getSettings().setCtoSChallenge(
                        GXCommon.hexToBytes(getValue(node, s)));
            }
            break;
        case TranslatorGeneralTags.DEDICATED_KEY:
            tmp = GXCommon.hexToBytes(getValue(node, s));
            s.getSettings().getCipher().setDedicatedKey(tmp);
            break;
        case TranslatorGeneralTags.CALLING_AP_TITLE:
            s.getSettings()
                    .setCtoSChallenge(GXCommon.hexToBytes(getValue(node, s)));
            break;
        case (int) TranslatorGeneralTags.CALLING_AE_INVOCATION_ID:
            s.getSettings().setUserId(s.parseInt(getValue(node, s)));
            break;
        case (int) TranslatorGeneralTags.CALLED_AE_INVOCATION_ID:
            s.getSettings().setUserId(s.parseInt(getValue(node, s)));
            break;
        case TranslatorGeneralTags.RESPONDING_AE_INVOCATION_ID:
            s.getSettings().setUserId(s.parseInt(getValue(node, s)));
            break;
        case 0xA4:
            // RespondingAPTitle.
            s.getSettings()
                    .setStoCChallenge(GXCommon.hexToBytes(getValue(node, s)));
            break;
        case 0xBE03:
        case 0xBE05:
            // ProposedConformance or NegotiatedConformance
            if (s.getSettings().isServer()) {
                list = s.getSettings().getNegotiatedConformance();
            } else {
                list = s.getSettings().getProposedConformance();
            }
            list.clear();
            if (s.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                String nodes = node.getFirstChild().getNodeValue();
                for (String it : nodes.split(" ")) {
                    if (!it.trim().isEmpty()) {
                        list.add(TranslatorStandardTags
                                .valueOfConformance(it.trim()));
                    }
                }
            }
            break;
        case 0xBE08:
            // ConformanceBit.
            // ProposedConformance or NegotiatedConformance
            if (s.getSettings().isServer()) {
                list = s.getSettings().getNegotiatedConformance();
            } else {
                list = s.getSettings().getProposedConformance();
            }
            list.add(TranslatorSimpleTags.valueOfConformance(
                    node.getAttributes().getNamedItem("Name").getNodeValue()));
            break;
        case 0xA2:
            // AssociationResult
            s.setResult(
                    AssociationResult.forValue(s.parseInt(getValue(node, s))));
            break;
        case 0xBE02:
        case 0xBE07:
            // NegotiatedMaxPduSize or ProposedMaxPduSize.
            s.getSettings().setMaxPduSize(s.parseInt(getValue(node, s)));
            break;
        case 0xA3:
            // ResultSourceDiagnostic
            s.setDiagnostic(SourceDiagnostic.NONE);
            break;
        case 0xA301:
            // ACSEServiceUser
            s.setDiagnostic(
                    SourceDiagnostic.forValue(s.parseInt(getValue(node, s))));
            break;
        case 0xBE09:
            // ProposedQualityOfService
            break;
        case TranslatorGeneralTags.CHAR_STRING:
            // Get PW
            if (s.getSettings().getAuthentication() == Authentication.LOW) {
                s.getSettings()
                        .setPassword(GXCommon.hexToBytes(getValue(node, s)));
            } else {
                if (s.getCommand() == Command.AARQ) {
                    s.getSettings().setCtoSChallenge(
                            GXCommon.hexToBytes(getValue(node, s)));
                } else {
                    s.getSettings().setStoCChallenge(
                            GXCommon.hexToBytes(getValue(node, s)));
                }
            }
            break;
        case TranslatorGeneralTags.RESPONDER_ACSE_REQUIREMENT:
            break;
        case TranslatorGeneralTags.RESPONDING_AUTHENTICATION:
            s.getSettings()
                    .setStoCChallenge(GXCommon.hexToBytes(getValue(node, s)));
            break;
        case TranslatorTags.RESULT:
            s.setResult(AssociationResult
                    .forValue(Integer.parseInt(getValue(node, s))));
            break;
        case Command.CONFIRMED_SERVICE_ERROR:
            if (s.getCommand() == Command.NONE) {
                s.getSettings().setServer(false);
                s.setCommand(tag);
            }
            break;
        case TranslatorTags.PROTOCOL_VERSION:
            str = getValue(node, s);
            GXByteBuffer pv = new GXByteBuffer();
            pv.setUInt8((byte) (8 - str.length()));
            GXCommon.setBitString(pv, str, false);
            s.getSettings().setProtocolVersion(str);
            break;
        case TranslatorTags.CALLED_AP_TITLE:
        case TranslatorTags.CALLED_AE_QUALIFIER:
            tmp = GXCommon.hexToBytes(getValue(node, s));
            s.getAttributeDescriptor().setUInt8(
                    (byte) (tag == (int) TranslatorTags.CALLED_AP_TITLE ? 0xA2
                            : 0xA3));
            s.getAttributeDescriptor().setUInt8(03);
            s.getAttributeDescriptor().setUInt8(BerType.OCTET_STRING);
            s.getAttributeDescriptor().setUInt8((byte) tmp.length);
            s.getAttributeDescriptor().set(tmp);
            break;
        case (int) TranslatorTags.CALLED_AP_INVOCATION_ID:
            s.getAttributeDescriptor().setUInt8(0xA6);
            s.getAttributeDescriptor().setUInt8(03);
            s.getAttributeDescriptor().setUInt8(BerType.INTEGER);
            s.getAttributeDescriptor().setUInt8(1);
            s.getAttributeDescriptor()
                    .setUInt8((byte) s.parseInt(getValue(node, s)));
            break;
        case (int) TranslatorTags.CALLED_AE_INVOCATION_ID:
            s.getAttributeDescriptor().setUInt8(0xA5);
            s.getAttributeDescriptor().setUInt8(03);
            s.getAttributeDescriptor().setUInt8(BerType.INTEGER);
            s.getAttributeDescriptor().setUInt8(1);
            s.getAttributeDescriptor()
                    .setUInt8((byte) s.parseInt(getValue(node, s)));
            break;
        case (int) TranslatorTags.CALLING_AP_INVOCATION_ID:
            s.getAttributeDescriptor().setUInt8(0xA4);
            s.getAttributeDescriptor().setUInt8(03);
            s.getAttributeDescriptor().setUInt8(BerType.INTEGER);
            s.getAttributeDescriptor().setUInt8(1);
            s.getAttributeDescriptor()
                    .setUInt8((byte) s.parseInt(getValue(node, s)));
            break;
        default:
            throw new IllegalArgumentException(
                    "Invalid AARQ node: " + node.getNodeName());
        }
    }

    private static String getValue(final Node node, final GXDLMSXmlSettings s) {
        Node tmp;
        if (s.getOutputType() == TranslatorOutputType.STANDARD_XML) {
            tmp = node.getFirstChild();
            // If content is empty.
            if (tmp == null) {
                return "";
            }
            String str = tmp.getNodeValue();
            if (str == null) {
                return "";
            }
            return str.trim();
        } else {
            tmp = node.getAttributes().getNamedItem("Value");
            // If content is empty.
            if (tmp == null) {
                return "";
            }
            return tmp.getNodeValue();
        }
    }

    static ErrorCode valueOfErrorCode(final TranslatorOutputType type,
            final String value) {
        if (type == TranslatorOutputType.STANDARD_XML) {
            return TranslatorStandardTags.valueOfErrorCode(value);
        } else {
            return TranslatorSimpleTags.valueOfErrorCode(value);
        }
    }

    static String errorCodeToString(final TranslatorOutputType type,
            final ErrorCode value) {
        if (type == TranslatorOutputType.STANDARD_XML) {
            return TranslatorStandardTags.errorCodeToString(value);
        } else {
            return TranslatorSimpleTags.errorCodeToString(value);
        }
    }

    /**
     * Parse XML.
     * 
     * @param node
     *            XML node.
     * @param s
     */
    private static void readNode(final Node node, final GXDLMSXmlSettings s) {
        long value;
        byte[] tmp;
        GXByteBuffer preData = null;
        String str;
        if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
            str = node.getNodeName().toLowerCase();
        } else {
            str = node.getNodeName();
        }
        int tag = 0;
        if (s.getCommand() != Command.CONFIRMED_SERVICE_ERROR
                || s.getTags().containsKey(str)) {
            tag = s.getTags().get(str);
        }
        if (s.getCommand() == Command.NONE) {
            if (!((s.getSettings().getClientAddress() == 0
                    || s.getSettings().getServerAddress() == 0)
                    && getFrame(node, s, tag)
                    || tag == (int) TranslatorTags.PDU_DLMS
                    || tag == (int) TranslatorTags.PDU_CSE)) {
                getCommand(node, s, tag);
            }
        } else if (s.getCommand() == Command.AARQ
                || s.getCommand() == Command.AARE
                || s.getCommand() == Command.INITIATE_REQUEST
                || s.getCommand() == Command.INITIATE_RESPONSE) {
            handleAarqAare(node, s, tag);
        } else if (tag >= GXDLMS.DATA_TYPE_OFFSET) {
            if (tag == DataType.DATETIME.getValue() + GXDLMS.DATA_TYPE_OFFSET
                    || (s.getCommand() == Command.EVENT_NOTIFICATION
                            && s.getAttributeDescriptor().size() == 0)) {
                preData = updateDateTime(node, s, preData);
                if (preData == null
                        && s.getCommand() == Command.GENERAL_CIPHERING) {
                    s.getData().setUInt8(0);
                }
            } else {
                preData = updateDataType(node, s, tag);
            }
        } else if (s.getCommand() == Command.CONFIRMED_SERVICE_ERROR) {
            if (s.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                if (tag == TranslatorTags.INITIATE_ERROR) {
                    s.getAttributeDescriptor().setUInt8(1);
                } else {
                    ServiceError se = TranslatorStandardTags
                            .getServiceError(str.substring(2));
                    s.getAttributeDescriptor().setUInt8(se.getValue());
                    s.getAttributeDescriptor().setUInt8(TranslatorStandardTags
                            .getError(se, getValue(node, s)));
                }
            } else {
                if (tag != TranslatorTags.SERVICE_ERROR) {
                    if (s.getAttributeDescriptor().size() == 0) {
                        s.getAttributeDescriptor()
                                .setUInt8(s.parseShort(getValue(node, s)));
                    } else {
                        ServiceError se =
                                TranslatorSimpleTags.getServiceError(str);
                        s.getAttributeDescriptor().setUInt8(se.getValue());
                        s.getAttributeDescriptor().setUInt8(TranslatorSimpleTags
                                .getError(se, getValue(node, s)));
                    }
                }
            }
        } else {
            switch (tag) {
            case Command.GET_REQUEST << 8 | GetCommandType.NORMAL:
            case Command.GET_REQUEST << 8 | GetCommandType.NEXT_DATA_BLOCK:
            case Command.GET_REQUEST << 8 | GetCommandType.WITH_LIST:
            case Command.SET_REQUEST << 8 | SetRequestType.NORMAL:
            case Command.SET_REQUEST << 8 | SetRequestType.FIRST_DATA_BLOCK:
            case Command.SET_REQUEST << 8 | SetRequestType.WITH_DATA_BLOCK:
            case Command.SET_REQUEST << 8 | SetRequestType.WITH_LIST:
                s.setRequestType((byte) (tag & 0xF));
                break;

            case Command.GET_RESPONSE << 8 | GetCommandType.NORMAL:
            case Command.GET_RESPONSE << 8 | GetCommandType.NEXT_DATA_BLOCK:
            case Command.GET_RESPONSE << 8 | GetCommandType.WITH_LIST:
            case Command.SET_RESPONSE << 8 | SetResponseType.NORMAL:
            case Command.SET_RESPONSE << 8 | SetResponseType.DATA_BLOCK:
            case Command.SET_RESPONSE << 8 | SetResponseType.LAST_DATA_BLOCK:
            case Command.SET_RESPONSE << 8 | SetResponseType.WITH_LIST:
            case Command.SET_RESPONSE << 8
                    | SetResponseType.LAST_DATA_BLOCK_WITH_LIST:
                s.setRequestType((byte) (tag & 0xF));
                break;
            case Command.READ_RESPONSE << 8
                    | SingleReadResponse.DATA_BLOCK_RESULT:
                s.setCount(s.getCount() + 1);
                s.setRequestType((byte) (tag & 0xF));
                break;
            case Command.READ_REQUEST << 8
                    | VariableAccessSpecification.PARAMETERISED_ACCESS:
                s.setRequestType(
                        VariableAccessSpecification.PARAMETERISED_ACCESS);
                break;
            case Command.READ_REQUEST << 8
                    | VariableAccessSpecification.BLOCK_NUMBER_ACCESS:
                s.setRequestType(
                        VariableAccessSpecification.BLOCK_NUMBER_ACCESS);
                s.setCount(s.getCount() + 1);
                break;
            case Command.METHOD_REQUEST << 8 | ActionRequestType.NORMAL:
                s.setRequestType((byte) (tag & 0xFF));
                break;
            case Command.METHOD_REQUEST << 8 | ActionRequestType.NEXT_BLOCK:
                s.setRequestType((byte) (tag & 0xFF));
                break;
            case Command.METHOD_REQUEST << 8 | ActionRequestType.WITH_LIST:
                s.setRequestType((byte) (tag & 0xFF));
                break;
            case Command.METHOD_RESPONSE << 8 | ActionResponseType.NORMAL:
                s.setRequestType((byte) (tag & 0xFF));
                break;
            case Command.READ_RESPONSE << 8 | SingleReadResponse.DATA:
            case TranslatorTags.DATA:
                // Data or DataBlockResult.
                if (s.getCommand() == Command.READ_REQUEST
                        || s.getCommand() == Command.READ_RESPONSE
                        || s.getCommand() == Command.GET_REQUEST) {
                    s.setCount(s.getCount() + 1);
                    s.setRequestType(0);
                } else if (s.getCommand() == Command.GET_RESPONSE
                        || s.getCommand() == Command.METHOD_RESPONSE) {
                    s.getData().setUInt8(0); // Add status.
                }
                break;
            case TranslatorTags.SUCCESS:
                s.setCount(s.getCount() + 1);
                s.getAttributeDescriptor().setUInt8(ErrorCode.OK.getValue());
                break;
            case TranslatorTags.DATA_ACCESS_ERROR:
                s.setCount(s.getCount() + 1);
                s.getAttributeDescriptor().setUInt8(1);
                s.getAttributeDescriptor().setUInt8(
                        valueOfErrorCode(s.getOutputType(), getValue(node, s))
                                .getValue());
                break;
            case TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION:
            case TranslatorTags.VARIABLE_ACCESS_SPECIFICATION:
                break;
            case TranslatorTags.LIST_OF_DATA:
                if (s.getCommand() == Command.ACCESS_RESPONSE
                        && s.getData().size() == 0) {
                    // If access-request-specification is not given.
                    s.getData().setUInt8(0);
                }
                if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML
                        || s.getCommand() != Command.WRITE_REQUEST) {
                    GXCommon.setObjectCount(getNodeCount(node), s.getData());
                }
                break;
            case (int) Command.ACCESS_RESPONSE << 8
                    | (byte) AccessServiceCommandType.GET:
            case (int) Command.ACCESS_RESPONSE << 8
                    | (byte) AccessServiceCommandType.SET:
            case (int) Command.ACCESS_RESPONSE << 8
                    | (byte) AccessServiceCommandType.ACTION:
                s.getData().setUInt8((0xFF & tag));
                break;
            case TranslatorTags.DATE_TIME:
                preData = updateDateTime(node, s, preData);
                if (preData == null
                        && s.getCommand() == Command.GENERAL_CIPHERING) {
                    s.getData().setUInt8(0);
                }
                break;
            case TranslatorTags.CURRENT_TIME:
                if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    updateDateTime(node, s, preData);
                } else {
                    str = getValue(node, s);
                    s.setTime(new GXDateTime(GXCommon.getGeneralizedTime(str)));
                }
                break;
            case TranslatorTags.TIME:
                preData = updateDateTime(node, s, preData);
                break;
            case TranslatorTags.INVOKE_ID:
                // InvokeIdAndPriority.
                value = s.parseShort(getValue(node, s));
                s.getSettings().updateInvokeId((short) value);
                break;
            case TranslatorTags.LONG_INVOKE_ID:
                value = s.parseLong(getValue(node, s));
                if ((value & 0x80000000) != 0) {
                    s.getSettings().setPriority(Priority.HIGH);
                } else {
                    s.getSettings().setPriority(Priority.NORMAL);
                }
                if ((value & 0x40000000) != 0) {
                    s.getSettings().setServiceClass(ServiceClass.CONFIRMED);
                } else {
                    s.getSettings().setServiceClass(ServiceClass.UN_CONFIRMED);
                }
                s.getSettings().setLongInvokeID(value & 0xFFFFFFF);
                break;
            case 0x88:
                break;
            case 0x80:
                // RespondingAuthentication
                s.getSettings().setStoCChallenge(
                        GXCommon.hexToBytes(getValue(node, s)));
                break;
            case TranslatorTags.ATTRIBUTE_DESCRIPTOR:
                break;
            case TranslatorTags.CLASS_ID:
                s.getAttributeDescriptor()
                        .setUInt16(s.parseInt(getValue(node, s)));
                break;
            case TranslatorTags.INSTANCE_ID:
                s.getAttributeDescriptor()
                        .add(GXCommon.hexToBytes(getValue(node, s)));
                break;
            case TranslatorTags.ATTRIBUTE_ID:
                s.getAttributeDescriptor()
                        .setUInt8(s.parseShort(getValue(node, s)));
                if (s.getCommand() != Command.ACCESS_REQUEST
                        && s.getCommand() != Command.EVENT_NOTIFICATION) {
                    // Add AccessSelection.
                    s.getAttributeDescriptor().setUInt8(0);
                }
                break;
            case TranslatorTags.METHOD_INVOCATION_PARAMETERS:
                s.getAttributeDescriptor()
                        .setUInt8(s.getAttributeDescriptor().size() - 1, 1);
                break;
            case TranslatorTags.SELECTOR:
                s.getAttributeDescriptor()
                        .set(GXCommon.hexToBytes(getValue(node, s)));
                break;
            case TranslatorTags.PARAMETER:
                break;
            case TranslatorTags.LAST_BLOCK:
                s.getData().setUInt8(s.parseShort(getValue(node, s)));
                break;
            case TranslatorTags.BLOCK_NUMBER:
                if (s.getCommand() == Command.GET_REQUEST
                        || s.getCommand() == Command.GET_RESPONSE
                        || s.getCommand() == Command.SET_REQUEST
                        || s.getCommand() == Command.SET_RESPONSE
                        || s.getCommand() == Command.METHOD_REQUEST
                        || s.getCommand() == Command.METHOD_RESPONSE) {
                    s.getData().setUInt32(s.parseLong(getValue(node, s)));
                } else {
                    s.getData().setUInt16(s.parseInt(getValue(node, s)));
                }
                break;
            case TranslatorTags.RAW_DATA:
                if (s.getCommand() == Command.GET_RESPONSE) {
                    s.getData().setUInt8(0);
                }
                tmp = GXCommon.hexToBytes(getValue(node, s));
                GXCommon.setObjectCount(tmp.length, s.getData());
                s.getData().set(tmp);
                break;
            case TranslatorTags.METHOD_DESCRIPTOR:
                break;
            case TranslatorTags.METHOD_ID:
                s.getAttributeDescriptor()
                        .setUInt8(s.parseShort(getValue(node, s)));
                // Add MethodInvocationParameters
                s.getAttributeDescriptor().setUInt8(0);
                break;
            case TranslatorTags.RESULT:
            case TranslatorGeneralTags.ASSOCIATION_RESULT:
                // Result.
                if (s.getCommand() == Command.GET_REQUEST
                        || s.getRequestType() == 3) {
                    GXCommon.setObjectCount(node.getChildNodes().getLength(),
                            s.getAttributeDescriptor());
                } else if (s.getCommand() == Command.METHOD_RESPONSE
                        || s.getCommand() == Command.SET_RESPONSE) {
                    str = getValue(node, s);
                    if (str != "") {
                        s.getAttributeDescriptor().setUInt8(
                                valueOfErrorCode(s.getOutputType(), str)
                                        .getValue());
                    }
                } else if (s.getCommand() == Command.ACCESS_RESPONSE) {
                    str = getValue(node, s);
                    if (str != "") {
                        s.getData().setUInt8(
                                valueOfErrorCode(s.getOutputType(), str)
                                        .getValue());
                    }
                }
                break;
            case TranslatorTags.REASON:
                if (s.getCommand() == Command.RELEASE_REQUEST) {
                    if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                        s.setReason((byte) TranslatorSimpleTags
                                .valueOfReleaseRequestReason(getValue(node, s))
                                .getValue());
                    } else {
                        s.setReason((byte) TranslatorStandardTags
                                .valueOfReleaseRequestReason(getValue(node, s))
                                .getValue());
                    }
                } else {
                    if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                        s.setReason((byte) TranslatorSimpleTags
                                .valueOfReleaseResponseReason(getValue(node, s))
                                .getValue());
                    } else {
                        s.setReason((byte) TranslatorStandardTags
                                .valueOfReleaseResponseReason(getValue(node, s))
                                .getValue());
                    }
                }
                break;
            case TranslatorTags.RETURN_PARAMETERS:
                s.getAttributeDescriptor().setUInt8(1);
                break;
            case TranslatorTags.ACCESS_SELECTION:
                s.getAttributeDescriptor()
                        .setUInt8(s.getAttributeDescriptor().size() - 1, 1);
                break;
            case TranslatorTags.VALUE:
                break;
            case TranslatorTags.SERVICE:
                if (s.getAttributeDescriptor().size() == 0) {
                    s.getAttributeDescriptor()
                            .setUInt8(s.parseShort(getValue(node, s)));
                } else {
                    s.getAttributeDescriptor()
                            .setUInt8(ServiceError.SERVICE.getValue());
                    s.getAttributeDescriptor().setUInt8(
                            Service.valueOf(getValue(node, s)).getValue());
                }
                break;
            case TranslatorTags.ACCESS_SELECTOR:
                s.getData().setUInt8(s.parseShort(getValue(node, s)));
                break;
            case TranslatorTags.ACCESS_PARAMETERS:
                break;
            case TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST:
                GXCommon.setObjectCount(getNodeCount(node),
                        s.getAttributeDescriptor());
                break;
            case TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION:
            case Command.ACCESS_REQUEST << 8 | AccessServiceCommandType.GET:
            case Command.ACCESS_REQUEST << 8 | AccessServiceCommandType.SET:
            case Command.ACCESS_REQUEST << 8 | AccessServiceCommandType.ACTION:
                if (s.getCommand() != Command.SET_REQUEST) {
                    s.getAttributeDescriptor().setUInt8(tag & 0xFF);
                }
                break;
            case Command.READ_REQUEST << 8
                    | VariableAccessSpecification.VARIABLE_NAME:
            case Command.WRITE_REQUEST << 8
                    | VariableAccessSpecification.VARIABLE_NAME:
            case Command.WRITE_REQUEST << 8 | SingleReadResponse.DATA:
                if (s.getCommand() != Command.ACCESS_REQUEST
                        && s.getCommand() != Command.ACCESS_RESPONSE) {
                    if (!(s.getOutputType() == TranslatorOutputType.STANDARD_XML
                            && tag == (Command.WRITE_REQUEST << 8
                                    | SingleReadResponse.DATA))) {
                        if (s.getRequestType() == 0xFF) {
                            s.getAttributeDescriptor().setUInt8(
                                    VariableAccessSpecification.VARIABLE_NAME);
                        } else {
                            s.getAttributeDescriptor()
                                    .setUInt8(s.getRequestType());
                            s.setRequestType(0xFF);
                        }
                        s.setCount(s.getCount() + 1);
                    } else if (s.getCommand() != Command.INFORMATION_REPORT) {
                        s.getAttributeDescriptor().setUInt8(s.getCount());
                    }
                    if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                        s.getAttributeDescriptor().setUInt16(
                                Integer.parseInt(getValue(node, s), 16));
                    } else {
                        str = getValue(node, s);
                        if (!str.isEmpty()) {
                            s.getAttributeDescriptor()
                                    .setUInt16(Short.parseShort(str));
                        }
                    }
                }
                break;
            case TranslatorTags.CHOICE:
                break;
            case Command.READ_RESPONSE << 8
                    | SingleReadResponse.DATA_ACCESS_ERROR:
                ErrorCode err =
                        valueOfErrorCode(s.getOutputType(), getValue(node, s));
                s.setCount(s.getCount() + 1);
                s.getData().setUInt8(1);
                s.getData().setUInt8(err.getValue());
                break;
            case TranslatorTags.NOTIFICATION_BODY:
                break;
            case TranslatorTags.DATA_VALUE:
                break;
            case TranslatorTags.ACCESS_REQUEST_BODY:
                break;
            case TranslatorTags.LIST_OF_ACCESS_REQUEST_SPECIFICATION:
                s.getAttributeDescriptor().setUInt8(getNodeCount(node));
                break;
            case TranslatorTags.ACCESS_REQUEST_SPECIFICATION:
                break;
            case TranslatorTags.ACCESS_REQUEST_LIST_OF_DATA:
                s.getAttributeDescriptor().setUInt8(getNodeCount(node));
                break;
            case TranslatorTags.ACCESS_RESPONSE_BODY:
                break;
            case TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION:
                s.getData().setUInt8(getNodeCount(node));
                break;
            case TranslatorTags.ACCESS_RESPONSE_SPECIFICATION:
                break;
            case TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA:
                // Add access-response-list-of-data. Optional
                s.getData().setUInt8(0);
                s.getData().setUInt8(getNodeCount(node));
                break;
            case TranslatorTags.SINGLE_RESPONSE:
                break;
            case TranslatorTags.SYSTEM_TITLE:
                tmp = GXCommon.hexToBytes(getValue(node, s));
                s.getSettings().setSourceSystemTitle(tmp);
                break;
            case TranslatorTags.CIPHERED_SERVICE:
            case Command.GLO_INITIATE_REQUEST:
                tmp = GXCommon.hexToBytes(getValue(node, s));
                if (s.getCommand() == Command.GENERAL_CIPHERING) {
                    GXCommon.setObjectCount(tmp.length, s.getData());
                } else if (s.getCommand() == Command.RELEASE_REQUEST) {
                    s.getData().setUInt8(0xBE);
                    GXCommon.setObjectCount(4 + tmp.length, s.getData());
                    s.getData().setUInt8(4);
                    GXCommon.setObjectCount(2 + tmp.length, s.getData());
                    s.getData().setUInt8(0x21);
                    GXCommon.setObjectCount(tmp.length, s.getData());
                }
                s.getData().set(tmp);
                break;
            case TranslatorTags.DATA_BLOCK:
                break;
            case TranslatorGeneralTags.USER_INFORMATION:
                tmp = GXCommon.hexToBytes(getValue(node, s));
                s.getData().setUInt8(0xBE);
                s.getData().setUInt8(2 + tmp.length);
                s.getData().setUInt8(0x4);
                s.getData().setUInt8(tmp.length);
                s.getData().set(tmp);
                break;
            case TranslatorTags.TRANSACTION_ID:
                tmp = GXCommon.hexToBytes(getValue(node, s));
                GXCommon.setObjectCount(tmp.length, s.getData());
                s.getData().set(tmp);
                break;
            case TranslatorTags.ORIGINATOR_SYSTEM_TITLE:
            case TranslatorTags.RECIPIENT_SYSTEM_TITLE:
            case TranslatorTags.OTHER_INFORMATION:
            case TranslatorTags.KEY_CIPHERED_DATA:
                tmp = GXCommon.hexToBytes(getValue(node, s));
                GXCommon.setObjectCount(tmp.length, s.getData());
                s.getData().set(tmp);
                break;
            case TranslatorTags.CIPHERED_CONTENT:
                tmp = GXCommon.hexToBytes(getValue(node, s));
                GXCommon.setObjectCount(tmp.length, s.getData());
                s.getData().set(tmp);
                break;
            case TranslatorTags.KEY_INFO:
                s.getData().setUInt8(1);
                break;
            case TranslatorTags.AGREED_KEY:
                s.getData().setUInt8(2);
                break;
            case TranslatorTags.KEY_PARAMETERS:
                s.getData().setUInt8(1);
                s.getData().setUInt8(Integer.parseInt(getValue(node, s)));
                break;
            case TranslatorTags.ATTRIBUTE_VALUE:
                break;
            case TranslatorTags.MAX_INFO_TX:
                value = Byte.parseByte(getValue(node, s));
                if ((s.getCommand() == Command.SNRM
                        && !s.getSettings().isServer())
                        || (s.getCommand() == Command.UA
                                && s.getSettings().isServer())) {
                    s.getSettings().getLimits().setMaxInfoRX((int) value);
                }
                s.getData().setUInt8(HDLCInfo.MAX_INFO_RX);
                s.getData().setUInt8(1);
                s.getData().setUInt8((byte) value);
                break;
            case TranslatorTags.MAX_INFO_RX:
                value = Byte.parseByte(getValue(node, s));
                if ((s.getCommand() == Command.SNRM
                        && !s.getSettings().isServer())
                        || (s.getCommand() == Command.UA
                                && s.getSettings().isServer())) {
                    s.getSettings().getLimits().setMaxInfoTX((int) value);
                }
                s.getData().setUInt8(HDLCInfo.MAX_INFO_TX);
                s.getData().setUInt8(1);
                s.getData().setUInt8((byte) value);
                break;
            case TranslatorTags.WINDOW_SIZE_TX:
                value = Byte.parseByte(getValue(node, s));
                if ((s.getCommand() == Command.SNRM
                        && !s.getSettings().isServer())
                        || (s.getCommand() == Command.UA
                                && s.getSettings().isServer())) {
                    s.getSettings().getLimits().setWindowSizeRX((byte) value);
                }
                s.getData().setUInt8((byte) HDLCInfo.WINDOW_SIZE_RX);
                s.getData().setUInt8(4);
                s.getData().setUInt32(value);
                break;
            case TranslatorTags.WINDOW_SIZE_RX:
                value = Byte.parseByte(getValue(node, s));
                if ((s.getCommand() == Command.SNRM
                        && !s.getSettings().isServer())
                        || (s.getCommand() == Command.UA
                                && s.getSettings().isServer())) {
                    s.getSettings().getLimits().setWindowSizeTX((byte) value);
                }
                s.getData().setUInt8(HDLCInfo.WINDOW_SIZE_TX);
                s.getData().setUInt8(4);
                s.getData().setUInt32(value);
                break;
            case Command.INITIATE_REQUEST:
                break;
            case TranslatorTags.VALUE_LIST:
                GXCommon.setObjectCount(getNodeCount(node), s.getData());
                break;
            case TranslatorTags.DATA_ACCESS_RESULT:
                s.getData().setUInt8(
                        valueOfErrorCode(s.getOutputType(), getValue(node, s))
                                .getValue());
                break;
            case TranslatorTags.WRITE_DATA_BLOCK_ACCESS:
                break;
            case TranslatorTags.FRAME_TYPE:
                break;
            case TranslatorTags.BLOCK_CONTROL:
                s.getAttributeDescriptor()
                        .setUInt8(s.parseShort(getValue(node, s)));
                break;
            case TranslatorTags.BLOCK_NUMBER_ACK:
                s.getAttributeDescriptor()
                        .setUInt16(s.parseShort(getValue(node, s)));
                break;
            case TranslatorTags.BLOCK_DATA:
                s.getData().set(GXCommon.hexToBytes(getValue(node, s)));
                break;
            case TranslatorTags.CONTENTS_DESCRIPTION:
                getNodeTypes(s, node);
                return;
            case TranslatorTags.ARRAY_CONTENTS:
                if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    getNodeValues(s, node);
                } else {
                    tmp = GXCommon.hexToBytes(getValue(node, s));
                    GXCommon.setObjectCount(tmp.length, s.getData());
                    s.getData().set(tmp);
                }
                break;
            case TranslatorTags.NETWORK_ID:
                s.setNetworkId(s.parseShort(getValue(node, s)));
                break;
            case TranslatorTags.PHYSICAL_DEVICE_ADDRESS:
                s.setPhysicalDeviceAddress(
                        GXCommon.hexToBytes(getValue(node, s)));
                s.setCommand(Command.NONE);
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid node: " + node.getNodeName());
            }
        }

        int cnt = 0;
        for (int pos = 0; pos != node.getChildNodes().getLength(); ++pos) {
            Node node2 = node.getChildNodes().item(pos);
            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                readNode(node2, s);
                ++cnt;
            }
        }
        if (preData != null) {
            GXCommon.setObjectCount(cnt, preData);
            preData.set(s.getData());
            s.getData().size(0);
            s.getData().set(preData);
        }
    }

    private static void getNodeValues(final GXDLMSXmlSettings s,
            final Node node) {
        int cnt = 1;
        int offset = 2;
        if (DataType.forValue(s.getData().getUInt8(2)) == DataType.STRUCTURE) {
            cnt = s.getData().getUInt8(3);
            offset = 4;
        }
        DataType types[] = new DataType[cnt];
        for (int pos = 0; pos != cnt; ++pos) {
            types[pos] = DataType.forValue(s.getData().getUInt8(offset + pos));
        }
        GXByteBuffer tmp = new GXByteBuffer();
        GXByteBuffer tmp2 = new GXByteBuffer();
        for (int row = 0; row != node.getChildNodes().getLength(); ++row) {
            String str = node.getChildNodes().item(row).getNodeValue();
            for (String r : str.split("[\n]")) {
                if (!"".equals(r.trim()) && !"\r".equals(r)) {
                    int col = 0;
                    for (String it : r.trim().split("[;]")) {
                        tmp.clear();
                        GXCommon.setData(tmp, types[col % types.length],
                                GXDLMSConverter.changeType(it,
                                        types[col % types.length]));
                        if (tmp.size() == 1) {
                            // If value is null.
                            s.getData().setUInt8(0);
                        } else {
                            tmp2.set(tmp.subArray(1, tmp.size() - 1));
                        }
                        ++col;
                    }
                }
            }
        }
        GXCommon.setObjectCount(tmp2.size(), s.getData());
        s.getData().set(tmp2);
    }

    private static void getNodeTypes(final GXDLMSXmlSettings s,
            final Node node) {
        int len = getNodeCount(node);
        if (len > 1) {
            s.getData().setUInt8(DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(len, s.getData());
        }
        for (int pos = 0; pos != node.getChildNodes().getLength(); ++pos) {
            Node node2 = node.getChildNodes().item(pos);
            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                String str;
                if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    str = node2.getNodeName().toLowerCase();
                } else {
                    str = node2.getNodeName();
                }
                int tag = s.getTags().get(str);
                s.getData().setUInt8(tag - GXDLMS.DATA_TYPE_OFFSET);
            }
        }
    }

    private static GXByteBuffer updateDateTime(final Node node,
            final GXDLMSXmlSettings s, final GXByteBuffer preData) {
        byte[] tmp;
        GXByteBuffer bb = preData;
        if (s.getRequestType() != 0xFF) {
            bb = updateDataType(node, s,
                    DataType.DATETIME.getValue() + GXDLMS.DATA_TYPE_OFFSET);
        } else {
            DataType dt = DataType.DATETIME;
            tmp = GXCommon.hexToBytes(getValue(node, s));
            if (tmp.length != 0) {
                if (tmp.length == 5) {
                    dt = DataType.DATE;
                } else if (tmp.length == 4) {
                    dt = DataType.TIME;
                }
                s.setTime((GXDateTime) GXDLMSClient.changeType(tmp, dt));
            }
        }
        return bb;
    }

    private static GXByteBuffer updateDataType(final Node node,
            final GXDLMSXmlSettings s, final int tag) {
        GXByteBuffer preData = null;
        String v = getValue(node, s);
        if (s.isTemplate() || v == "*") {
            s.setTemplate(true);
            return preData;
        }
        DataType dt = DataType.forValue(tag - GXDLMS.DATA_TYPE_OFFSET);
        switch (dt) {
        case ARRAY:
            s.getData().setUInt8(DataType.ARRAY.getValue());
            preData = new GXByteBuffer(s.getData());
            s.getData().size(0);
            break;
        case BCD:
            GXCommon.setData(s.getData(), DataType.BCD,
                    s.parseShort(getValue(node, s)));
            break;
        case BITSTRING:
            GXCommon.setData(s.getData(), DataType.BITSTRING,
                    getValue(node, s));
            break;
        case BOOLEAN:
            GXCommon.setData(s.getData(), DataType.BOOLEAN,
                    s.parseShort(getValue(node, s)));
            break;
        case DATE:
            GXCommon.setData(s.getData(), DataType.DATE,
                    GXDLMSClient.changeType(
                            GXCommon.hexToBytes(getValue(node, s)),
                            DataType.DATE));
            break;
        case DATETIME:
            byte[] tmp = GXCommon.hexToBytes(getValue(node, s));
            if (tmp.length == 5) {
                dt = DataType.DATE;
            } else if (tmp.length == 4) {
                dt = DataType.TIME;
            }
            GXCommon.setData(s.getData(), dt, GXDLMSClient.changeType(tmp, dt));
            break;
        case ENUM:
            GXCommon.setData(s.getData(), DataType.ENUM,
                    s.parseShort(getValue(node, s)));
            break;
        case FLOAT32:
            getFloat32(node, s);
            break;
        case FLOAT64:
            getFloat64(node, s);
            break;
        case INT16:
            GXCommon.setData(s.getData(), DataType.INT16,
                    s.parseShort(getValue(node, s)));
            break;
        case INT32:
            GXCommon.setData(s.getData(), DataType.INT32,
                    s.parseInt(getValue(node, s)));
            break;
        case INT64:
            GXCommon.setData(s.getData(), DataType.INT64,
                    s.parseLong(getValue(node, s)));
            break;
        case INT8:
            GXCommon.setData(s.getData(), DataType.INT8,
                    s.parseShort(getValue(node, s)));
            break;
        case NONE:
            GXCommon.setData(s.getData(), DataType.NONE, null);
            break;
        case OCTET_STRING:
            getOctetString(node, s);
            break;
        case STRING:
            if (s.isShowStringAsHex()) {
                GXCommon.setData(s.getData(), DataType.STRING,
                        GXCommon.hexToBytes(getValue(node, s)));
            } else {
                GXCommon.setData(s.getData(), DataType.STRING,
                        getValue(node, s));
            }
            break;
        case STRING_UTF8:
            if (s.isShowStringAsHex()) {
                GXCommon.setData(s.getData(), DataType.STRING_UTF8,
                        GXCommon.hexToBytes(getValue(node, s)));
            } else {
                GXCommon.setData(s.getData(), DataType.STRING_UTF8,
                        getValue(node, s));
            }
            break;
        case STRUCTURE:
            s.getData().setUInt8(DataType.STRUCTURE.getValue());
            preData = new GXByteBuffer(s.getData());
            s.getData().size(0);
            break;
        case TIME:
            GXCommon.setData(s.getData(), DataType.TIME,
                    GXDLMSClient.changeType(
                            GXCommon.hexToBytes(getValue(node, s)),
                            DataType.TIME));
            break;
        case UINT16:
            GXCommon.setData(s.getData(), DataType.UINT16,
                    s.parseShort(getValue(node, s)));
            break;
        case UINT32:
            GXCommon.setData(s.getData(), DataType.UINT32,
                    s.parseLong(getValue(node, s)));
            break;
        case UINT64:
            GXCommon.setData(s.getData(), DataType.UINT64,
                    s.parseLong(getValue(node, s)));
            break;
        case UINT8:
            GXCommon.setData(s.getData(), DataType.UINT8,
                    s.parseShort(getValue(node, s)));
            break;
        case COMPACT_ARRAY:
            s.getData().setUInt8(dt.getValue());
            break;
        default:
            throw new IllegalArgumentException(
                    "Invalid node: " + node.getNodeName());
        }
        return preData;
    }

    private static void getOctetString(final Node node,
            final GXDLMSXmlSettings s) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setHexString(getValue(node, s));
        GXCommon.setData(s.getData(), DataType.OCTET_STRING, bb.array());
    }

    private static void getFloat32(final Node node, final GXDLMSXmlSettings s) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setHexString(getValue(node, s));
        GXCommon.setData(s.getData(), DataType.FLOAT32, bb.getFloat());
    }

    private static void getFloat64(final Node node, final GXDLMSXmlSettings s) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setHexString(getValue(node, s));
        GXCommon.setData(s.getData(), DataType.FLOAT64, bb.getDouble());
    }

    /**
     * Convert XML to hex string.
     * 
     * @param xml
     *            Converted XML.
     * @return Converted PDU in hex string.
     */
    public final String xmlToHexPdu(final String xml) {
        return GXCommon.toHex(xmlToPdu(xml, null), false);
    }

    /**
     * Convert XML to hex string.
     * 
     * @param xml
     *            Converted XML.
     * @param addSpace
     *            Add spaces between bytes.
     * @return Converted PDU in hex string.
     */
    public final String xmlToHexPdu(final String xml, final boolean addSpace) {
        return GXCommon.toHex(xmlToPdu(xml, null), addSpace);
    }

    /**
     * Convert XML to byte array.
     * 
     * @param xml
     *            Converted XML.
     * @return Converted PDU in bytes.
     */
    public final byte[] xmlToPdu(final String xml) {
        return xmlToPdu(xml, null);
    }

    /**
     * Convert XML to byte array.
     * 
     * @param xml
     *            Converted XML.
     * @param settings
     *            XML settings.
     * @return Converted PDU in bytes.
     */
    public final byte[] xmlToPdu(final String xml,
            final GXDLMSXmlSettings settings) {
        DocumentBuilder docBuilder;
        Document doc;
        DocumentBuilderFactory docBuilderFactory =
                DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        GXDLMSXmlSettings s = settings;
        if (s == null) {
            s = new GXDLMSXmlSettings(outputType, hex, getShowStringAsHex(),
                    tagsByName);
        }
        readNode(doc.getDocumentElement(), s);
        GXByteBuffer bb = new GXByteBuffer();
        GXDLMSLNParameters ln;
        GXDLMSSNParameters sn;
        switch (s.getCommand()) {
        case Command.INITIATE_REQUEST:
            GXAPDU.getInitiateRequest(s.getSettings(),
                    s.getSettings().getCipher(), bb);
            break;
        case Command.INITIATE_RESPONSE:
            bb.set(GXAPDU.getUserInformation(s.getSettings(),
                    s.getSettings().getCipher()));
            break;
        case Command.READ_REQUEST:
        case Command.WRITE_REQUEST:
        case Command.READ_RESPONSE:
        case Command.WRITE_RESPONSE:
            sn = new GXDLMSSNParameters(s.getSettings(), s.getCommand(),
                    s.getCount(), s.getRequestType(),
                    s.getAttributeDescriptor(), s.getData());
            GXDLMS.getSNPdu(sn, bb);
            break;
        case Command.GET_REQUEST:
        case Command.GET_RESPONSE:
        case Command.SET_REQUEST:
        case Command.SET_RESPONSE:
        case Command.METHOD_REQUEST:
        case Command.METHOD_RESPONSE:
            ln = new GXDLMSLNParameters(s.getSettings(), 0, s.getCommand(),
                    s.getRequestType(), s.getAttributeDescriptor(), s.getData(),
                    0xff, Command.NONE);
            GXDLMS.getLNPdu(ln, bb);
            break;
        case Command.GLO_GET_REQUEST:
        case Command.GLO_GET_RESPONSE:
        case Command.GLO_SET_REQUEST:
        case Command.GLO_SET_RESPONSE:
        case Command.GLO_METHOD_REQUEST:
        case Command.GLO_METHOD_RESPONSE:
        case Command.GLO_READ_REQUEST:
        case Command.GLO_WRITE_REQUEST:
        case Command.GLO_READ_RESPONSE:
        case Command.GLO_WRITE_RESPONSE:
        case Command.DED_GET_REQUEST:
        case Command.DED_GET_RESPONSE:
        case Command.DED_SET_REQUEST:
        case Command.DED_SET_RESPONSE:
        case Command.DED_METHOD_REQUEST:
        case Command.DED_METHOD_RESPONSE:
            bb.setUInt8(s.getCommand());
            GXCommon.setObjectCount(s.getData().size(), bb);
            bb.set(s.getData());
            break;
        case Command.UNACCEPTABLE_FRAME:
            break;
        case Command.SNRM:
            s.getSettings().setServer(false);
            bb.set(GXDLMS.getHdlcFrame(s.getSettings(), (byte) Command.SNRM,
                    null));
            break;
        case Command.UA:
            break;
        case Command.AARQ:
        case Command.GLO_INITIATE_REQUEST:
            GXAPDU.generateAarq(s.getSettings(), s.getSettings().getCipher(),
                    s.getData(), bb);
            break;
        case Command.AARE:
        case Command.GLO_INITIATE_RESPONSE:
            GXAPDU.generateAARE(s.getSettings(), bb, s.getResult(),
                    s.getDiagnostic(), s.getSettings().getCipher(),
                    s.getAttributeDescriptor(), s.getData());
            break;
        case Command.DISCONNECT_REQUEST:
            break;
        case Command.RELEASE_REQUEST:
            bb.setUInt8(s.getCommand());
            // Len
            bb.setUInt8(3 + s.getData().size());
            // BerType
            bb.setUInt8(BerType.CONTEXT);
            // Len.
            bb.setUInt8(1);
            bb.setUInt8(s.getReason());
            if (s.getData().size() == 0) {
                bb.setUInt8(0);
            } else {
                bb.set(s.getData());
            }
            break;
        case Command.RELEASE_RESPONSE:
            bb.setUInt8(s.getCommand());
            // Len
            bb.setUInt8(3);
            // BerType
            bb.setUInt8(BerType.CONTEXT);
            // Len.
            bb.setUInt8(1);
            bb.setUInt8(s.getReason());
            break;
        case Command.CONFIRMED_SERVICE_ERROR:
            bb.setUInt8(s.getCommand());
            bb.set(s.getAttributeDescriptor());
            break;
        case Command.EXCEPTION_RESPONSE:
            break;
        case Command.GENERAL_BLOCK_TRANSFER:
            break;
        case Command.ACCESS_REQUEST:
            ln = new GXDLMSLNParameters(s.getSettings(), 0, s.getCommand(),
                    s.getRequestType(), s.getAttributeDescriptor(), s.getData(),
                    0xff, Command.NONE);
            GXDLMS.getLNPdu(ln, bb);
            break;
        case Command.ACCESS_RESPONSE:
            ln = new GXDLMSLNParameters(s.getSettings(), 0, s.getCommand(),
                    s.getRequestType(), s.getAttributeDescriptor(), s.getData(),
                    0xff, Command.NONE);
            GXDLMS.getLNPdu(ln, bb);
            break;
        case Command.DATA_NOTIFICATION:
            ln = new GXDLMSLNParameters(s.getSettings(), 0, s.getCommand(),
                    s.getRequestType(), s.getAttributeDescriptor(), s.getData(),
                    0xff, Command.NONE);
            ln.setTime(s.getTime());
            GXDLMS.getLNPdu(ln, bb);
            break;
        case Command.INFORMATION_REPORT:
            sn = new GXDLMSSNParameters(s.getSettings(), s.getCommand(),
                    s.getCount(), s.getRequestType(),
                    s.getAttributeDescriptor(), s.getData());
            sn.setTime(s.getTime());
            GXDLMS.getSNPdu(sn, bb);
            break;
        case Command.EVENT_NOTIFICATION:
            ln = new GXDLMSLNParameters(s.getSettings(), 0, s.getCommand(),
                    s.getRequestType(), s.getAttributeDescriptor(), s.getData(),
                    0xff, Command.NONE);
            ln.setTime(s.getTime());
            GXDLMS.getLNPdu(ln, bb);
            break;
        case Command.GENERAL_GLO_CIPHERING:
            bb.setUInt8(s.getCommand());
            GXCommon.setObjectCount(
                    s.getSettings().getSourceSystemTitle().length, bb);
            bb.set(s.getSettings().getSourceSystemTitle());
            GXCommon.setObjectCount(s.getData().size(), bb);
            bb.set(s.getData());
            break;
        case Command.GENERAL_CIPHERING:
            bb.setUInt8(s.getCommand());
            bb.set(s.getData());
            break;
        case Command.GLO_EVENT_NOTIFICATION_REQUEST:
            break;
        default:
        case Command.NONE:
            throw new IllegalArgumentException("Invalid command.");
        }
        if (s.getPhysicalDeviceAddress() != null) {
            GXByteBuffer bb2 = new GXByteBuffer();
            bb2.setUInt8(s.getGwCommand());
            bb2.setUInt8(s.getNetworkId());
            GXCommon.setObjectCount(s.getPhysicalDeviceAddress().length, bb2);
            bb2.set(s.getPhysicalDeviceAddress());
            bb2.set(bb);
            return bb2.array();
        }
        return bb.array();
    }

    /**
     * @return Are numeric values shown as hex.
     */
    public final boolean isHex() {
        return hex;
    }

    /**
     * @param value
     *            Are numeric values shows as hex.
     */
    public final void setHex(final boolean value) {
        hex = value;
    }

    /**
     * @return Is XML declaration skipped.
     */
    public final boolean isOmitXmlDeclaration() {
        return omitXmlDeclaration;
    }

    /**
     * @param value
     *            Is XML declaration skipped.
     */
    public final void setOmitXmlDeclaration(final boolean value) {
        omitXmlDeclaration = value;
    }

    /**
     * @return Is XML name space skipped.
     */
    public final boolean isOmitXmlNameSpace() {
        return omitXmlNameSpace;
    }

    /**
     * @param value
     *            Is XML name space skipped.
     */
    public final void setOmitXmlNameSpace(final boolean value) {
        omitXmlNameSpace = value;
    }

    /**
     * Convert data bytes to XML.
     * 
     * @param data
     *            Data to parse.
     * @return Generated xml
     */
    public final String dataToXml(final GXByteBuffer data) {
        GXDataInfo di = new GXDataInfo();
        GXDLMSTranslatorStructure xml =
                new GXDLMSTranslatorStructure(outputType, omitXmlNameSpace, hex,
                        getShowStringAsHex(), comments, tags);
        di.setXml(xml);
        GXCommon.getData(data, di);
        return di.getXml().toString();
    }

    private void getAllDataNodes(final NodeList nodes, GXDLMSXmlSettings s) {
        GXByteBuffer preData;
        int tag;
        for (int pos = 0; pos != nodes.getLength(); ++pos) {
            Node it = nodes.item(pos);
            if (it.getNodeType() == Node.ELEMENT_NODE) {
                if (s.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
                    tag = s.getTags().get(it.getNodeName().toLowerCase());
                } else {
                    tag = s.getTags().get(it.getNodeName());
                }
                if (tag == TranslatorTags.RAW_DATA) {
                    s.getData().setHexString(it.getNodeValue());
                } else {
                    preData = updateDataType(it, s, tag);
                    if (preData != null) {
                        GXCommon.setObjectCount(it.getChildNodes().getLength(),
                                preData);
                        preData.set(s.getData());
                        s.getData().size(0);
                        s.getData().set(preData);
                        getAllDataNodes(it.getChildNodes(), s);
                    }
                }
            }
        }
    }

    /**
     * Convert XML data to PDU bytes.
     * 
     * @param xml
     *            XML data.
     * @return Data in bytes.
     */
    public final byte[] XmlToData(final String xml) {
        DocumentBuilder docBuilder;
        Document doc;
        DocumentBuilderFactory docBuilderFactory =
                DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        GXDLMSXmlSettings s = new GXDLMSXmlSettings(outputType, hex,
                getShowStringAsHex(), tagsByName);

        getAllDataNodes(doc.getDocumentElement().getChildNodes(), s);
        return s.getData().array();
    }
}