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
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import gurux.dlms.enums.Command;

public class GXDLMSXmlPdu {
    /**
     * XML Node.
     */
    private Node xmlNode;

    /**
     * Command.
     */
    private int command;

    /**
     * Description of the test.
     */
    private String description;

    /**
     * Shown error if this test fails.
     */
    private String error;
    /**
     * Error url if test fails.
     */
    private String errorUrl;

    /**
     * @return XML Node.
     */
    public final Node getXmlNode() {
        return xmlNode;
    }

    /**
     * @return Command.
     */
    public final int getCommand() {
        return command;
    }

    /**
     * @param value
     *            Command.
     */
    public final void setCommand(final int value) {
        command = value;
    }

    /**
     * Generated Pdu.
     */
    private byte[] data;

    /**
     * @return Generated Pdu.
     */
    public final byte[] getData() {
        return data;
    }

    /**
     * @param value
     *            Generated Pdu.
     */
    public final void setData(final byte[] value) {
        data = value;
    }

    /**
     * @return Description of the test.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param value
     *            Description of the test.
     */
    public final void setDescription(final String value) {
        description = value;
    }

    /**
     * @return Shown error if this test fails.
     */
    public final String getError() {
        return error;
    }

    /**
     * @param value
     *            Shown error if this test fails.
     */
    public final void setError(final String value) {
        error = value;
    }

    /**
     * @return Error url if test fails.
     */
    public final String getErrorUrl() {
        return errorUrl;
    }

    /**
     * @param value
     *            Error url if test fails.
     */
    public final void setErrorUrl(final String value) {
        errorUrl = value;
    }

    /**
     * Sleep time in milliseconds.
     */
    private int privateSleep;

    public final int getSleep() {
        return privateSleep;
    }

    public final void setSleep(int value) {
        privateSleep = value;
    }

    static String getOuterXml(Node node)
            throws TransformerConfigurationException, TransformerException {
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty("omit-xml-declaration", "yes");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }

    /**
     * Return PDU as XML string.
     */
    public final String getPduAsXml() {
        if (getXmlNode() == null) {
            return "";
        }
        try {
            return getOuterXml(xmlNode);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private static void compare(final Node expectedNode, final Node actualNode,
            final java.util.ArrayList<String> list) {
        int cnt = expectedNode.getChildNodes().getLength();
        if (expectedNode.getNodeName()
                .compareTo(actualNode.getNodeName()) != 0) {
            Node a = expectedNode.getAttributes().getNamedItem("Value");
            if (expectedNode.getNodeName().compareTo("None") == 0 && a != null
                    && a.getNodeValue().compareTo("*") == 0) {
                return;
            }
            list.add(expectedNode.getNodeName() + "-"
                    + actualNode.getNodeName());
            return;
        } else if (cnt != actualNode.getChildNodes().getLength()) {
            // If we are reading array items count might vary.
            if (expectedNode.getNodeName().equals("Array")
                    || expectedNode.getNodeName().equals("Structure")) {
                if (cnt < actualNode.getChildNodes().getLength()) {
                    // Check only first If meter is returning more nodes what we
                    // have in template.
                } else {
                    cnt = actualNode.getChildNodes().getLength();
                }
            } else {
                list.add("Different amount: " + expectedNode.getNodeName() + "-"
                        + actualNode.getNodeName());
                return;
            }
        }
        for (int pos = 0; pos != cnt; ++pos) {
            if (actualNode.getChildNodes().item(pos) == null) {
                list.add("Different values. Expected: '"
                        + expectedNode.getChildNodes().item(pos).getNodeValue()
                        + "'. Actual: 'null'.");
            } else if (actualNode.getChildNodes().item(pos).getChildNodes()
                    .getLength() != 0) {
                compare(expectedNode.getChildNodes().item(pos),
                        actualNode.getChildNodes().item(pos), list);
            } else if (expectedNode.getChildNodes().item(pos).getNodeValue()
                    .compareTo(actualNode.getChildNodes().item(pos)
                            .getNodeValue()) != 0) {
                Node a = expectedNode.getChildNodes().item(pos).getAttributes()
                        .getNamedItem("Value");
                if (a == null
                        || (expectedNode.getChildNodes().item(pos).getNodeName()
                                .compareTo("None") != 0
                                && expectedNode.getChildNodes().item(pos)
                                        .getNodeName()
                                        .compareTo(actualNode.getChildNodes()
                                                .item(pos).getNodeName()) != 0)
                        || a.getNodeValue().compareTo("*") != 0)
                // If value type is not defined.
                {
                    if (!expectedNode.getFirstChild().getNodeName()
                            .equals("Structure")
                            && !expectedNode.getFirstChild().getNodeName()
                                    .equals("Array")
                            && !expectedNode.getParentNode().getNodeName()
                                    .equals("Array")) {
                        list.add("Different values. Expected: '"
                                + expectedNode.getChildNodes().item(pos)
                                        .getNodeValue()
                                + "'. Actual: '" + actualNode.getChildNodes()
                                        .item(pos).getNodeValue()
                                + "'.");
                    }
                }
            }
        }
    }

    /**
     * Compare load XML.
     * 
     * @param xml
     *            XML string to compare.
     * @return True, if content is same.
     */
    public final java.util.ArrayList<String> compare(String xml) {
        java.util.ArrayList<String> list = new java.util.ArrayList<String>();
        // XmlDocument doc2 = new XmlDocument();
        // doc2.LoadXml(xml);
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
        compare(getXmlNode(), doc.getDocumentElement(), list);
        return list;
    }

    /**
     * Is command request. This information is used to tell is PDU send.
     * 
     * @return True, if command is request.
     */
    public final boolean isRequest() {
        boolean ret;
        switch (command) {
        case Command.SNRM:
        case Command.AARQ:
        case Command.READ_REQUEST:
        case Command.GLO_READ_REQUEST:
        case Command.WRITE_REQUEST:
        case Command.GLO_WRITE_REQUEST:
        case Command.GET_REQUEST:
        case Command.GLO_GET_REQUEST:
        case Command.SET_REQUEST:
        case Command.GLO_SET_REQUEST:
        case Command.METHOD_REQUEST:
        case Command.GLO_METHOD_REQUEST:
        case Command.DISCONNECT_REQUEST:
        case Command.RELEASE_REQUEST:
            ret = true;
            break;
        default:
            ret = false;
            break;
        }
        return ret;
    }

    /**
     * Constructor.
     */
    public GXDLMSXmlPdu() {

    }

    /**
     * Constructor.
     * 
     * @param command
     *            Generated command.
     * @param xml
     *            Generated PDU as XML.
     * @param pdu
     *            Generated PDU.
     */
    public GXDLMSXmlPdu(final int command, final Node xml, final byte[] pdu) {
        setCommand(command);
        xmlNode = xml;
        setData(pdu);
    }

    @Override
    public String toString() {
        return getPduAsXml();
    }
}