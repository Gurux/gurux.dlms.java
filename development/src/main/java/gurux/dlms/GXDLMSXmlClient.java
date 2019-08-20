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

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.Security;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.secure.GXDLMSSecureClient;

/**
 * GXDLMS Xml client implements methods to communicate with DLMS/COSEM metering
 * devices using XML.
 */
public class GXDLMSXmlClient extends GXDLMSSecureClient {

    /**
     * XML client don't throw exceptions. It serializes them as a default. Set
     * value to true, if exceptions are thrown.
     */
    private boolean throwExceptions;

    /**
     * Constructor
     * 
     * @param type
     *            XML type.
     */
    public GXDLMSXmlClient(TranslatorOutputType type) {
        translator = new GXDLMSTranslator(type);
        translator.setHex(false);
        setUseLogicalNameReferencing(true);
    }

    /**
     * @return XML client don't throw exceptions. It serializes them as a
     *         default. Set value to true, if exceptions are thrown.
     */
    public final boolean getThrowExceptions() {
        return throwExceptions;
    }

    /**
     * @param value
     *            XML client don't throw exceptions. It serializes them as a
     *            default. Set value to true, if exceptions are thrown.
     */
    @Override
    public final void setThrowExceptions(boolean value) {
        throwExceptions = value;
    }

    public static void removeRecursively(final Node node, final short nodeType,
            final String name) {
        if (node.getNodeType() == nodeType
                && (name == null || node.getNodeName().equals(name))) {
            node.getParentNode().removeChild(node);
        } else {
            // check the children recursively
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                removeRecursively(list.item(i), nodeType, name);
            }
        }
    }

    /**
     * Load XML commands from the file.
     * 
     * @param file
     *            XML file
     * @return Loaded XML objects.
     */
    public List<GXDLMSXmlPdu> load(final File file) {
        return load(file, null);
    }

    /**
     * Load XML commands from the file.
     * 
     * @param file
     *            XML file
     * @param s
     *            Load settings.
     * @return Loaded XML objects.
     */
    @SuppressWarnings("squid:S00112")
    public List<GXDLMSXmlPdu> load(final File file, final GXXmlLoadSettings s) {
        DocumentBuilder docBuilder;
        Document doc;
        DocumentBuilderFactory docBuilderFactory =
                DocumentBuilderFactory.newInstance();
        try {
            docBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,
                    true);
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return load(doc, s);
    }

    /**
     * Load XML commands from the string.
     * 
     * @param xml
     *            XML string
     * @param s
     *            Load settings.
     * @return Loaded XML objects.
     */
    @SuppressWarnings("squid:S00112")
    public List<GXDLMSXmlPdu> load(final String xml,
            final GXXmlLoadSettings s) {
        DocumentBuilder docBuilder;
        Document doc;
        DocumentBuilderFactory docBuilderFactory =
                DocumentBuilderFactory.newInstance();
        try {
            docBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,
                    true);
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return load(doc, s);
    }

    /**
     * Load XML commands from the string.
     * 
     * @param xml
     *            XML string
     * @param settings
     *            Load settings.
     * @return Loaded XML objects.
     */
    @SuppressWarnings({ "squid:S00112", "squid:S1066", "squid:S135" })
    private List<GXDLMSXmlPdu> load(final Document doc,
            final GXXmlLoadSettings loadSettings) {
        // Remove comments.
        removeRecursively(doc, Node.COMMENT_NODE, null);

        List<GXDLMSXmlPdu> actions = new ArrayList<GXDLMSXmlPdu>();
        String description = null, error = null, errorUrl = null, sleep = null;
        for (int pos = 0; pos != doc.getChildNodes().getLength(); ++pos) {
            Node m1 = doc.getChildNodes().item(pos);
            if (m1.getNodeType() == Node.ELEMENT_NODE) {
                for (int pos2 = 0; pos2 != m1.getChildNodes()
                        .getLength(); ++pos2) {
                    Node node = m1.getChildNodes().item(pos2);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        if (node.getNodeName().equals("Description")) {
                            description = node.getNodeValue();
                            continue;
                        }
                        if (node.getNodeName().equals("Error")) {
                            error = node.getNodeValue();
                            continue;
                        }
                        if (node.getNodeName().equals("ErrorUrl")) {
                            errorUrl = node.getNodeValue();
                            continue;
                        }
                        if (node.getNodeName().equals("Sleep")) {
                            sleep = node.getNodeValue();
                            continue;
                        }
                        if (loadSettings != null
                                && node.getNodeName().equals("GetRequest")) {
                            if (loadSettings.getStart() != new java.util.Date(0)
                                    && loadSettings
                                            .getEnd() != new java.util.Date(
                                                    0)) {
                                for (int pos3 = 0; pos3 != node.getChildNodes()
                                        .getLength(); ++pos3) {
                                    Node n1 = node.getChildNodes().item(pos3);
                                    if (n1.getNodeName()
                                            .equals("GetRequestNormal")) {
                                        for (int pos4 = 0; pos4 != n1
                                                .getChildNodes()
                                                .getLength(); ++pos4) {
                                            Node n2 = n1.getChildNodes()
                                                    .item(pos4);
                                            if (n2.getNodeName().equals(
                                                    "AccessSelection")) {
                                                for (int pos5 = 0; pos5 != n2
                                                        .getChildNodes()
                                                        .getLength(); ++pos5) {
                                                    Node n3 = n2.getChildNodes()
                                                            .item(pos5);
                                                    if (n3.getNodeName().equals(
                                                            "AccessSelector")) {
                                                        if (!n3.getAttributes()
                                                                .getNamedItem(
                                                                        "Value")
                                                                .getNodeValue()
                                                                .equals("1")) {
                                                            break;
                                                        }
                                                    } else if (n3.getNodeName()
                                                            .equals("AccessParameters")) {
                                                        for (int pos6 =
                                                                0; pos6 != n3
                                                                        .getChildNodes()
                                                                        .getLength(); ++pos6) {
                                                            Node n4 = n3
                                                                    .getChildNodes()
                                                                    .item(pos6);
                                                            if (n4.getNodeName()
                                                                    .equals("Structure")) {
                                                                boolean start =
                                                                        true;
                                                                for (int pos7 =
                                                                        0; pos7 != n4
                                                                                .getChildNodes()
                                                                                .getLength(); ++pos7) {
                                                                    Node n5 = n4
                                                                            .getChildNodes()
                                                                            .item(pos7);
                                                                    if (n5.getNodeName()
                                                                            .equals("OctetString")) {
                                                                        GXByteBuffer bb =
                                                                                new GXByteBuffer();
                                                                        if (start) {
                                                                            GXCommon.setData(
                                                                                    settings,
                                                                                    bb,
                                                                                    DataType.OCTET_STRING,
                                                                                    loadSettings
                                                                                            .getStart());
                                                                            n5.getAttributes()
                                                                                    .getNamedItem(
                                                                                            "Value")
                                                                                    .setNodeValue(
                                                                                            bb.toHex(
                                                                                                    false,
                                                                                                    2));
                                                                            start = false;
                                                                        } else {
                                                                            GXCommon.setData(
                                                                                    settings,
                                                                                    bb,
                                                                                    DataType.OCTET_STRING,
                                                                                    loadSettings
                                                                                            .getEnd());
                                                                            n5.getAttributes()
                                                                                    .getNamedItem(
                                                                                            "Value")
                                                                                    .setNodeValue(
                                                                                            bb.toHex(
                                                                                                    false,
                                                                                                    2));
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        break;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        GXDLMSXmlSettings s = new GXDLMSXmlSettings(
                                translator.getOutputType(), translator.isHex(),
                                translator.getShowStringAsHex(),
                                translator.tagsByName);
                        s.getSettings()
                                .setClientAddress(settings.getClientAddress());
                        s.getSettings()
                                .setServerAddress(settings.getServerAddress());
                        byte[] reply;
                        try {
                            reply = translator.xmlToPdu(
                                    GXDLMSXmlPdu.getOuterXml(node), s);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex.getMessage());
                        }
                        if ((s.getCommand() == Command.SNRM
                                && !s.getSettings().isServer())
                                || (s.getCommand() == Command.UA
                                        && s.getSettings().isServer())) {
                            settings.getLimits().setMaxInfoTX(
                                    s.getSettings().getLimits().getMaxInfoTX());
                            settings.getLimits().setMaxInfoRX(
                                    s.getSettings().getLimits().getMaxInfoRX());
                            settings.getLimits().setWindowSizeRX(s.getSettings()
                                    .getLimits().getWindowSizeRX());
                            settings.getLimits().setWindowSizeTX(s.getSettings()
                                    .getLimits().getWindowSizeTX());
                        }
                        if (s.isTemplate()) {
                            reply = null;
                        }
                        GXDLMSXmlPdu p =
                                new GXDLMSXmlPdu(s.getCommand(), node, reply);
                        if (description != null && !description.equals("")) {
                            p.setDescription(description);
                        }
                        if (error != null && !error.equals("")) {
                            p.setError(error);
                        }
                        if (errorUrl != null && !errorUrl.equals("")) {
                            p.setErrorUrl(errorUrl);
                        }
                        if (sleep != null && !sleep.equals("")) {
                            p.setSleep(Integer.parseInt(sleep));
                        }
                        actions.add(p);
                    }
                }
            }
        }
        return actions;
    }

    /**
     * Load XML commands from the file.
     * 
     * @param pdu
     *            Parsed PDU.
     * @return Generated messages (frames).
     */
    public final byte[][] pduToMessages(final GXDLMSXmlPdu pdu) {
        List<byte[]> messages = new ArrayList<byte[]>();
        if (pdu.getCommand() == Command.SNRM) {
            messages.add(pdu.getData());
        } else if (pdu.getCommand() == Command.UA) {
            messages.add(pdu.getData());
        } else if (pdu.getCommand() == Command.DISCONNECT_REQUEST) {
            messages.add(GXDLMS.getHdlcFrame(settings,
                    (byte) Command.DISCONNECT_REQUEST,
                    new GXByteBuffer(pdu.getData())));
        } else {
            GXByteBuffer reply;
            if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
                if (getCiphering().getSecurity() != Security.NONE) {
                    GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                            pdu.getCommand(), 0x0, null, null, 0xff,
                            Command.NONE);
                    reply = new GXByteBuffer(GXDLMS.cipher0(p, pdu.getData()));
                } else {
                    reply = new GXByteBuffer(pdu.getData());
                }
            } else {
                if (getCiphering().getSecurity() != Security.NONE) {
                    GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
                            pdu.getCommand(), 0x0, null, null, 0xff,
                            Command.NONE);
                    byte[] tmp = GXDLMS.cipher0(p, pdu.getData());
                    reply = new GXByteBuffer((short) (3 + tmp.length));
                    reply.set(GXCommon.LLC_SEND_BYTES);
                    reply.set(tmp);
                } else {
                    reply = new GXByteBuffer(
                            (short) (3 + pdu.getData().length));
                    reply.set(GXCommon.LLC_SEND_BYTES);
                    reply.set(pdu.getData());
                }
            }
            byte frame = 0;
            while (reply.position() != reply.size()) {
                if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
                    messages.add(GXDLMS.getWrapperFrame(settings, reply));
                } else if (settings.getInterfaceType() == InterfaceType.HDLC) {
                    if (pdu.getCommand() == Command.AARQ) {
                        frame = 0x10;
                    } else if (pdu.getCommand() == Command.AARE) {
                        frame = 0x30;
                    } else if (pdu.getCommand() == Command.EVENT_NOTIFICATION) {
                        frame = 0x13;
                    }
                    messages.add(GXDLMS.getHdlcFrame(settings, frame, reply));
                    if (reply.position() != reply.size()) {
                        frame = settings.getNextSend(false);
                    }
                } else if (settings.getInterfaceType() == InterfaceType.PDU) {
                    messages.add(reply.array());
                    break;
                } else {
                    throw new IllegalArgumentException("InterfaceType");
                }
            }
        }
        return messages.toArray(new byte[0][0]);
    }
}
