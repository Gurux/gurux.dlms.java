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
// More information of Gurux DLMS/COSEM Director: https://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.server.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.internal.GXCommon;

/**
 * DLMS Server that uses Logical Name referencing with IEC 62056-46 Data link
 * layer using HDLC protocol. Example Iskraemeco and Actaris uses this.
 */
public class GXSecuritySettings {

	/**
	 * Constructor.
	 */
	private GXSecuritySettings() {

	}

	/*
	 * Load security settings.
	 */
	public static void load2(final GXDLMSSettings settings, final String path) throws ParserConfigurationException,
			SAXException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, DOMException {
		File file = new File(path);
		if (file.exists()) {
			XMLInputFactory xif = XMLInputFactory.newFactory();
			xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
			xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// an instance of builder to parse the specified xml file
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			for (int pos = 0; pos < doc.getChildNodes().item(0).getChildNodes().getLength(); ++pos) {
				Node elemNode = doc.getChildNodes().item(0).getChildNodes().item(pos);
				if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
					if (elemNode.getNodeName().equals("BlockCipherKey")) {
						settings.getCipher().setBlockCipherKey(GXCommon.hexToBytes(elemNode.getTextContent()));
					} else if (elemNode.getNodeName().equals("AuthenticationKey")) {
						settings.getCipher().setAuthenticationKey(GXCommon.hexToBytes(elemNode.getTextContent()));
					} else if (elemNode.getNodeName().equals("Kek")) {
						settings.setKek(GXCommon.hexToBytes(elemNode.getTextContent()));
					} else if (elemNode.getNodeName().equals("InvocationCounter")) {
						settings.getCipher().setInvocationCounter(Long.parseLong(elemNode.getTextContent()));
					} else if (elemNode.getNodeName().equals("MinimumInvocationCounter")) {
						settings.getCipher().setMinimumInvocationCounter(Long.parseLong(elemNode.getTextContent()));
					}
				}
			}
		}
	}

	/**
	 * Save security settings.
	 * 
	 * @param settings DLMS settings.
	 * @param path     File path.
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static void save2(final GXDLMSSettings settings, final String path)
			throws FileNotFoundException, XMLStreamException, NoSuchAlgorithmException, InvalidKeySpecException {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = outputFactory.createXMLStreamWriter(new FileOutputStream(path));

		writer.writeStartDocument();
		writer.writeCharacters(System.getProperty("line.separator"));
		writer.writeStartElement("SecuritySettings");
		writer.writeCharacters(System.getProperty("line.separator"));
		writer.writeStartElement("BlockCipherKey");
		writer.writeCharacters(GXCommon.toHex(settings.getCipher().getBlockCipherKey()));
		writer.writeEndElement();
		writer.writeCharacters(System.getProperty("line.separator"));
		writer.writeStartElement("AuthenticationKey");
		writer.writeCharacters(GXCommon.toHex(settings.getCipher().getAuthenticationKey()));
		writer.writeEndElement();
		writer.writeCharacters(System.getProperty("line.separator"));
		writer.writeStartElement("Kek");
		writer.writeCharacters(GXCommon.toHex(settings.getKek()));
		writer.writeEndElement();
		writer.writeCharacters(System.getProperty("line.separator"));
		writer.writeStartElement("InvocationCounter");
		writer.writeCharacters(String.valueOf(settings.getCipher().getInvocationCounter()));
		writer.writeEndElement();
		writer.writeCharacters(System.getProperty("line.separator"));
		writer.writeStartElement("MinimumInvocationCounter");
		writer.writeCharacters(String.valueOf(settings.getCipher().getMinimumInvocationCounter()));
		writer.writeEndElement();
		writer.writeEndDocument();
		writer.close();
	}
}