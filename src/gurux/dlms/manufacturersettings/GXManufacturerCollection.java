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

package gurux.dlms.manufacturersettings;

import gurux.dlms.enums.ObjectType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.jar.Attributes;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

public class GXManufacturerCollection extends java.util.ArrayList<GXManufacturer>
{
    private static final long serialVersionUID = 1L;

    /** 
     Find manufacturer settings by manufacturer id.
     @return found manufacturer or null.
    */
    public final GXManufacturer findByIdentification(String id)
    {
        for (GXManufacturer it : this)
        {
            if (it.getIdentification().compareToIgnoreCase(id) == 0)
            {
                return it;
            }
        }
        return null;
    }

    /** 
     Is this first run.	 
    */
    public static boolean isFirstRun(String path) throws Exception
    {        
        if (!(new java.io.File(path)).isDirectory())
        {
            if (!(new java.io.File(path)).mkdir())
            {
                return true;
            }
        }
        java.io.File file = new java.io.File(path, "files.xml");
        if (!file.exists())
        {
            return true;
        }
        return false;
    }

    /** 
     Check if there are any updates available in Gurux www server.	 
     @return Returns true if there are any updates available.
    */
    public static boolean isUpdatesAvailable(String path)
    {        
        File file = new File(path, "files.xml");
        if (!file.exists())
        {
            return true;
        }
        Map<String, Date> installed = new HashMap<String, Date>();
        Map<String, Date> available = new HashMap<String, Date>();
        DateFormat tmFormater = new SimpleDateFormat("MM-dd-yyyy");        
        try
        {
            java.io.FileInputStream tmp = new java.io.FileInputStream(file);
            XMLInputFactory inputFactory= XMLInputFactory.newInstance();
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(tmp);                
            String data = "", target = "";
            while(xmlStreamReader.hasNext())
            {
                int event = xmlStreamReader.next();
                if(event == XMLStreamConstants.START_ELEMENT)
                {
                    data = xmlStreamReader.getAttributeValue(0);
                }
                else if(event == XMLStreamConstants.CHARACTERS)
                {
                    target = xmlStreamReader.getText();
                }
                else if(event == XMLStreamConstants.END_ELEMENT)
                {                    
                    if (!data.equals(""))
                    {
                        installed.put(target, tmFormater.parse(data));
                        data = target = "";
                    }
                }
            }
            URL gurux = new URL("http://www.gurux.org/obis/files.xml");
            URLConnection gx = gurux.openConnection();
            InputStream io = gx.getInputStream();
            xmlStreamReader = inputFactory.createXMLStreamReader(io);    
            while(xmlStreamReader.hasNext())
            {
                int event=xmlStreamReader.next();
                if(event == XMLStreamConstants.START_ELEMENT)
                {
                    data = xmlStreamReader.getAttributeValue(0);                              
                }
                else if(event == XMLStreamConstants.CHARACTERS)
                {
                    target = xmlStreamReader.getText();
                }
                else if(event == XMLStreamConstants.END_ELEMENT)
                {
                    if (!data.equals(""))
                    {
                        available.put(target, tmFormater.parse(data));
                        data = target = "";
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return true;
        }
        for (Map.Entry<String, Date> it : available.entrySet()) 
        {
            //If new item is added.
            if (!installed.containsKey(it.getKey()))
            {
                return true;
            }
            //If item is updated.
            if (it.getValue().compareTo(installed.get(it.getKey())) != 0)
            {
                return true;
            }
        }
        return false;
    }
    
    /** 
     Update manufacturer settings from the Gurux www server.

    */
    public static void updateManufactureSettings(String directory) throws Exception
    {
        File dir = new File(directory);
        if (!dir.exists())
        {
            if (!dir.mkdirs())
            {
                return;
            }
        }
        InputStream io = null;        
        PrintWriter writer = null;
        XMLStreamReader xmlStreamReader = null;
        InputStreamReader r = null;
        try
        {
            String path = directory + File.separator + "files.xml";
            URL gurux = new URL("http://www.gurux.org/obis/files.xml");
            URLConnection gx = gurux.openConnection();
            io = gx.getInputStream();
            String target, data, line;
            String newline;
            r = new InputStreamReader(io, "utf-8");
            BufferedReader reader = new BufferedReader(r);
            writer = new PrintWriter(path, "utf-8");
            target = "";
            data = "";
            newline = System.getProperty("line.separator");
            while((line = reader.readLine()) != null)
            {
                writer.write(line);
                writer.write(newline);
            }
            r.close();
            writer.close();
            writer = null;
            io.close();
            io = null;
            XMLInputFactory inputFactory=XMLInputFactory.newInstance();
            java.io.FileInputStream tmp = new java.io.FileInputStream(path);
            xmlStreamReader = inputFactory.createXMLStreamReader(tmp);        
            while(xmlStreamReader.hasNext())
            {
                int event = xmlStreamReader.next();
                if(event == XMLStreamConstants.START_ELEMENT)
                {
                    data = "";
                    target = xmlStreamReader.getLocalName();                
                }
                else if(event == XMLStreamConstants.CHARACTERS)
                {
                    data = xmlStreamReader.getText();
                }
                else if(event == XMLStreamConstants.END_ELEMENT)
                {
                    if (target.equalsIgnoreCase("file"))
                    {
                        URL f = new URL("http://www.gurux.org/obis/" + data);
                        URLConnection fc = f.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(fc.getInputStream()));
                        try
                        {
                            writer = new PrintWriter(directory + File.separator + data, "utf-8");
                            while((line = in.readLine()) != null)
                            {
                                writer.write(line);
                                writer.write(newline);
                            }
                        }
                        finally                            
                        {
                            if (writer != null)
                            {
                                writer.close();
                                writer = null;
                            }
                            if (in != null)
                            {
                                in.close();
                            }
                        }
                    }
                    target = "";
                }
            }
        }
        finally
        {
            if (r != null)
            {
                r.close();
            }
            if (io != null)
            {
                io.close();
            }
            if (writer != null)
            {
                writer.close();
            }        
            if (xmlStreamReader != null)
            {
                xmlStreamReader.close();
            }            
        }
    }
    
    public static void readManufacturerSettings(GXManufacturerCollection Manufacturers, String path)
    {
        Manufacturers.clear();
        File di = new File(path);
        String[] files = di.list();
        // Either directory does not exist or is not a directory
        if (files != null)
        {            
            for (String it : files)
            {
                if (it.endsWith(".obx"))
                {
                    try
                    {
                    java.io.FileInputStream in = new java.io.FileInputStream(path + File.separator + it);                    
                    Manufacturers.add(parse(in));
                    }
                    catch(Exception ex)
                    {
                        System.out.println(ex.getMessage());
                        continue;
                    }
                }
            }
        }        
    }
    
    /**
     * Reads value of an xml element with xsi:byte, xsi:short, xsi:int,
     * xsi:long, xsi:unsignedByte, xsd:unsignedShort, xsd:unsignedInt,
     * xsd:unsignedLong attributes.
     * <br><b>Note</b>: If no such parameter is found, returns "Object type is String."
     * @return Object, containing byte, short,int or long number.
     * @return Object, containing byte,short,int or long number
     */
    private static Object getNumberValue(String value, String type)
    {
        Object retval = value;
        if (type.equals("xsd:byte")) 
        {
            retval = Byte.parseByte(value);
        }
        else if (type.equals("xsd:short")) 
        {
            retval = Short.parseShort(value);
        }
        else if (type.equals("xsd:int")) 
        {
            retval = Integer.parseInt(value);
        }
        else if (type.equals("xsd:long")) 
        {
            retval = Long.parseLong(value);
        }
        else if (type.equals("xsd:unsignedByte")) 
        {
            retval = Byte.parseByte(value);
        }
        else if (type.equals("xsd:unsignedShort")) 
        {
            retval = Short.parseShort(value);
        }
        else if (type.equals("xsd:unsignedInt")) 
        {
            retval = Integer.parseInt(value);
        }
        else if (type.equals("xsd:unsignedLong")) 
        {
            retval = Long.parseLong(value);
        }
        return retval;
    }

    /**
     * Reserved for internal use.
     * @param in
     * @return
     * @throws Exception
     */
    private static GXManufacturer parse(InputStream in) throws Exception
    {
        GXManufacturer man = null;
        //get a factory
        XMLInputFactory inputFactory=XMLInputFactory.newInstance();
        XMLStreamReader  xmlStreamReader =inputFactory.createXMLStreamReader(in);
        String target = null, data = null;
        Attributes attributes = new Attributes();
        GXAuthentication authentication = null;
        GXServerAddress serveraddress = null;
        GXObisCode obisCode = null;
        GXDLMSAttribute att = null;
        while(xmlStreamReader.hasNext())
        {
            int event=xmlStreamReader.next();
            if(event==XMLStreamConstants.START_ELEMENT)
            {
                attributes.clear();
                data = null;
                target = xmlStreamReader.getLocalName();
                if (target.equalsIgnoreCase("GXManufacturer"))
                {
                    man = new GXManufacturer();
                }
                else if(target.equalsIgnoreCase("GXObisCode"))
                {
                    obisCode = new GXObisCode();
                    man.getObisCodes().add(obisCode);
                }
                else if(target.equalsIgnoreCase("GXAuthentication"))
                {
                    authentication = new GXAuthentication();
                    man.getSettings().add(authentication);
                }
                else if(target.equalsIgnoreCase("GXServerAddress"))
                {
                    serveraddress = new GXServerAddress();
                    man.getServerSettings().add(serveraddress);
                }
                else if(target.equalsIgnoreCase("GXDLMSAttributeSettings"))
                {
                    att = new GXDLMSAttribute();
                    obisCode.getAttributes().add(att);
                }                
                for(int i=0; i<xmlStreamReader.getAttributeCount();i++)
                {
                    attributes.putValue(xmlStreamReader.getAttributeLocalName(i), xmlStreamReader.getAttributeValue(i));
                }
            }
            else if(event==XMLStreamConstants.CHARACTERS)
            {
                data = xmlStreamReader.getText();
            }
            else if(event==XMLStreamConstants.END_ELEMENT)
            {
                if(target.equalsIgnoreCase("Identification"))
                {
                    man.setIdentification(data);
                }
                else if(target.equalsIgnoreCase("Name"))
                {
                    man.setName(data);
                }
                else if(target.equalsIgnoreCase("UseLN"))
                {
                    man.setUseLogicalNameReferencing(Boolean.parseBoolean(data));
                }
                else if(target.equalsIgnoreCase("UseIEC47"))
                {
                    man.setUseIEC47(Boolean.parseBoolean(data));
                }
                else if(target.equalsIgnoreCase("ClientID"))
                {
                    String type = attributes.getValue("type");
                    if (authentication != null)
                    {
                        authentication.setClientID(getNumberValue(data, type));
                    } 
                }
                else if(target.equalsIgnoreCase("PhysicalAddress"))
                {
                    String type = attributes.getValue("type");
                    if (serveraddress != null)
                    {
                        serveraddress.setPhysicalAddress(getNumberValue(data, type));
                    }
                }
                else if(target.equalsIgnoreCase("LogicalAddress"))
                {                    
                    if (serveraddress != null)
                    {                        
                        serveraddress.setLogicalAddress(Integer.parseInt(data));
                    }
                }
                else if(target.equalsIgnoreCase("Formula"))
                {                    
                    if (serveraddress != null)
                    {
                        serveraddress.setFormula(data);
                    }
                }   
                else if(target.equalsIgnoreCase("HDLCAddress"))
                {                    
                    if (serveraddress != null)
                    {
                        serveraddress.setHDLCAddress(HDLCAddressType.forValue(Integer.parseInt(data)));
                    }
                }                
                else if(target.equalsIgnoreCase("Selected"))
                {
                    authentication.setSelected(Boolean.parseBoolean(data));
                }
                else if(target.equalsIgnoreCase("InactivityMode"))
                {
                    man.setInactivityMode(gurux.dlms.manufacturersettings.InactivityMode.valueOf(data.toUpperCase()));
                }
                else if(target.equalsIgnoreCase("Type"))
                {
                    if (authentication != null)
                    {
                        authentication.setType(gurux.dlms.enums.Authentication.valueOf(data.toUpperCase()));
                    }
                    else
                    {
                        if (data.equals("BinaryCodedDesimal"))
                        {
                            att.setType(gurux.dlms.enums.DataType.BCD);
                        }
                        else if (data.equals("OctetString"))
                        {
                            att.setType(gurux.dlms.enums.DataType.OCTET_STRING);
                        }
                        else
                        {
                            att.setType(gurux.dlms.enums.DataType.valueOf(data.toUpperCase()));
                        }
                    }
                }
                 else if(target.equalsIgnoreCase("UIType"))
                {
                    if (data.equals("BinaryCodedDesimal"))
                    {
                        att.setType(gurux.dlms.enums.DataType.BCD);
                    }
                    else if (data.equals("OctetString"))
                    {
                        att.setType(gurux.dlms.enums.DataType.OCTET_STRING);
                    }
                    else
                    {
                        att.setUIType(gurux.dlms.enums.DataType.valueOf(data.toUpperCase()));
                    }
                }
                else if(target.equalsIgnoreCase("GXAuthentication"))
                {
                    authentication = null;
                }
                else if(target.equalsIgnoreCase("LogicalName"))
                {
                    obisCode.setLogicalName(data);
                }
                else if(target.equalsIgnoreCase("Description"))
                {
                    obisCode.setDescription(data);
                }
                else if(target.equalsIgnoreCase("ObjectType"))
                {
                    obisCode.setObjectType(ObjectType.forValue(Integer.parseInt(data)));
                }
                else if(target.equalsIgnoreCase("Interface"))
                {
                    //Old way. ObjectType is used now.
                }
                else if(target.equalsIgnoreCase("Index"))
                {
                    att.setIndex(Integer.parseInt(data));
                }
                else if(target.equalsIgnoreCase("StartProtocol"))
                {
                    man.setStartProtocol(StartProtocolType.valueOf(data.toUpperCase()));
                }                
                else
                {
                    target = "";
                }
                target = "";
            }
        }
        return man;
    }
}