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

import gurux.dlms.enums.AccessMode;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.Security;
import gurux.dlms.manufacturersettings.GXAuthentication;
import gurux.dlms.manufacturersettings.GXManufacturer;
import gurux.dlms.manufacturersettings.HDLCAddressType;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXProfileGenericUpdater;
import gurux.dlms.objects.IGXDLMSBase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** 
 GXDLMSServer implements methods to implement DLMS/COSEM meter/proxy.
*/
abstract public class GXDLMSServerBase
{
    int LastCommand;
    ByteArrayOutputStream ReceivedData = new ByteArrayOutputStream();
    private GXDLMSObjectCollection m_Items;
    GXDLMS m_Base = new GXDLMS(true);	
    ByteArrayOutputStream ReceivedFrame = new ByteArrayOutputStream();
    ArrayList<byte[]> SendData = new ArrayList<byte[]>();
    int FrameIndex = 0;
    boolean Initialized = false;
    private TreeMap<Integer, GXDLMSObject> SortedItems = new TreeMap<Integer, GXDLMSObject>();
    private ArrayList<Object> ServerIDs;
    private ArrayList<GXAuthentication> Authentications;

    static public byte[] chipher(Authentication auth, byte[] plainText)
    {     
        try
        {
            if (auth == Authentication.HIGH_MD5)
            {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return md.digest(plainText);
            }
            if (auth == Authentication.HIGH_SHA1)
            {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                return md.digest(plainText);
            }
            if (auth == Authentication.HIGH_GMAC)
            {

            }
        }
        catch(NoSuchAlgorithmException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        return plainText;
    }
    
    /*
     * Client to Server challenge.
     * Reserved internal use. Do not use.
     */
    public final byte[] getCtoSChallenge()
    {
        return m_Base.CtoSChallenge;
    }
    
    /*
     * Server to Client challenge.
     * Reserved internal use. Do not use.
     */
    public final byte[] getStoCChallenge()
    {
        return m_Base.StoCChallenge;
    }
     
    //Reserved for inner use. Do not use.
    public final Authentication getAuthentication()
    {
        return m_Base.getAuthentication();
    }
    
    /*
     * Read selected item.
     */
    abstract public void read(ValueEventArgs e);

    /*
     * Write selected item.
     */
    abstract public void write(ValueEventArgs e);
        
    /*
     * Client attempts to connect with the wrong server or client address.     
     */
    abstract public void invalidConnection(ConnectionEventArgs e);

    /*
     * Action is occurred.
     */
    abstract public void action(ValueEventArgs e);
    
    /** 
     Constructor.
    */
    public GXDLMSServerBase()
    {
        this(false);
    }

    /** 
     Constructor.
    */
    public GXDLMSServerBase(boolean logicalNameReferencing)
    {
        m_Base = new GXDLMS(true);
        m_Items = new GXDLMSObjectCollection(this);        
        m_Base.setUseLogicalNameReferencing(logicalNameReferencing);
        reset();        
        m_Base.setLNSettings(new GXDLMSLNSettings(new byte[] {0x00, 0x7E, 0x1F}));
        m_Base.setSNSettings(new GXDLMSSNSettings(new byte[] {0x1C, 0x03, 0x20}));
        ServerIDs = new java.util.ArrayList<Object>();
        this.setInterfaceType(InterfaceType.GENERAL);        
    }

    public GXCiphering getCiphering()
    {
        return m_Base.Ciphering;
    }
    /** 
     Count server ID from serial number.

     @param serialNumber
     @return Server ID.
    */
    public static int countServerIDFromSerialNumber(int serialNumber)
    {
        return ((Number)GXManufacturer.countServerAddress(HDLCAddressType.SERIAL_NUMBER, serialNumber, 0)).intValue();
    }

    /** 
     Count server ID from physical and logical addresses.

     @return Server ID.
    */
    public final Object countServerID(Object physicalAddress, int LogicalAddress)
    {
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            return ((Number)physicalAddress).shortValue();
        }
        return GXManufacturer.countServerAddress(HDLCAddressType.DEFAULT, physicalAddress, LogicalAddress);
    }

    /** 
     List of objects that meter supports.
    */
    public final GXDLMSObjectCollection getItems()
    {
        return m_Items;
    }
    public final void setItems(GXDLMSObjectCollection value)
    {
        m_Items = value;
    }

    /** 
     Information from the connection size that server can handle.
    */
    public final GXDLMSLimits getLimits()
    {
        return m_Base.getLimits();
    }    

    /** 
     Collection of server IDs.

     Server ID is the indentification of the device that is used as a server.
     Server ID is aka HDLC Address.	 
    */
    public final java.util.ArrayList<Object> getServerIDs()
    {
        return ServerIDs;
    }

    /** 
     DLMS version number. 

     Gurux DLMS component supports DLMS version number 6.	 
    */
    public final byte getDLMSVersion()
    {
        return m_Base.getDLMSVersion();
    }

    public final void setDLMSVersion(byte value)
    {
        m_Base.setDLMSVersion(value);
    }

    /** 
     Retrieves the maximum size of PDU receiver.

     PDU size tells maximum size of PDU packet.
     Value can be from 0 to 0xFFFF. By default the value is 0xFFFF.

     @see GXDLMSServerBase#getDLMSVersion
     @see GXDLMSServerBase#getUseLogicalNameReferencing
    */
    public final int getMaxReceivePDUSize()
    {
        return m_Base.getMaxReceivePDUSize();
    }

    public final void setMaxReceivePDUSize(int value)
    {
        m_Base.setMaxReceivePDUSize(value);
    }

    /** 
     Determines, whether Logical, or Short name, referencing is used.     	 

     Referencing depends on the device to communicate with.
     Normally, a device supports only either Logical or Short name referencing.
     The referencing is defined by the device manufacurer.
     If the referencing is wrong, the SNMR message will fail.

     * @see GXDLMSServerBase#getDLMSVersion
     * @see GXDLMSServerBase#getMaxReceivePDUSize
    */
    public final boolean getUseLogicalNameReferencing()
    {
        return m_Base.getUseLogicalNameReferencing();
    }
    public final void setUseLogicalNameReferencing(boolean value)
    {
        m_Base.setUseLogicalNameReferencing(value);
    }

    /** 
     Used Authentication password pairs.
    */
    public final java.util.ArrayList<GXAuthentication> getAuthentications()
    {
        return Authentications;
    }

    /** 
     Determines the type of the connection	 

     All DLMS meters do not support the IEC 62056-47 standard.  
     If the device does not support the standard, and the connection is made 
     using TCP/IP, set the type to InterfaceType.GENERAL.		 
    */
    public final InterfaceType getInterfaceType()
    {
        return m_Base.getInterfaceType();
    }
    public final void setInterfaceType(InterfaceType value)
    {
        boolean changed = m_Base.getInterfaceType() != value;        
        if (changed || Authentications == null)
        {
            m_Base.setInterfaceType(value);
            if (value == InterfaceType.GENERAL)
            {
                if (Authentications == null)
                {
                    Authentications = new ArrayList<GXAuthentication>();
                }
                else
                {
                    Authentications.clear();
                    ServerIDs.clear();
                }
                try
                {
                    Authentications.add(new GXAuthentication(Authentication.NONE, "".getBytes("ASCII"), (byte) 0x10));
                    Authentications.add(new GXAuthentication(Authentication.LOW, "GuruxLow".getBytes("ASCII"), (byte) 0x20));
                    Authentications.add(new GXAuthentication(Authentication.HIGH, "GuruxHigh".getBytes("ASCII"), (byte) 0x40));        
                    Authentications.add(new GXAuthentication(Authentication.HIGH_MD5, "GuruxHighMD5".getBytes("ASCII"), (byte) 0x40));        
                    Authentications.add(new GXAuthentication(Authentication.HIGH_SHA1, "GuruxHighSHA1".getBytes("ASCII"), (byte) 0x40));        
                    Authentications.add(new GXAuthentication(Authentication.HIGH_GMAC, "GuruxHighGMAC".getBytes("ASCII"), (byte) 0x40));        
                }
                catch(UnsupportedEncodingException ex)
                {
                    throw new RuntimeException(ex.getMessage());
                }
                ServerIDs.add(countServerID((byte)1, 0));
            }
            else
            {
                if (Authentications == null)
                {
                    Authentications = new ArrayList<GXAuthentication>();
                }
                else
                {
                    Authentications.clear();
                    ServerIDs.clear();
                }
                try
                {
                    Authentications.add(new GXAuthentication(Authentication.NONE, "".getBytes("ASCII"), (short) 0x10));
                    Authentications.add(new GXAuthentication(Authentication.LOW, "GuruxLow".getBytes("ASCII"), (short) 0x20));
                    Authentications.add(new GXAuthentication(Authentication.HIGH, "GuruxHigh".getBytes("ASCII"), (short) 0x40));        
                    Authentications.add(new GXAuthentication(Authentication.HIGH_MD5, "GuruxHighMD5".getBytes("ASCII"), (short) 0x40));        
                    Authentications.add(new GXAuthentication(Authentication.HIGH_SHA1, "GuruxHighSHA1".getBytes("ASCII"), (short) 0x40));        
                    Authentications.add(new GXAuthentication(Authentication.HIGH_GMAC, "GuruxHighGMAC".getBytes("ASCII"), (byte) 0x40));        
                }
                catch(UnsupportedEncodingException ex)
                {
                    throw new RuntimeException(ex.getMessage());
                }                
                ServerIDs.add((short) 1);
            }
        }
    }

    /** 
     Gets Logical Name settings, read from the device. 
    */
    public final GXDLMSLNSettings getLNSettings()
    {
        return m_Base.getLNSettings();
    }

    /** 
     Gets Short Name settings, read from the device.
    */
    public final GXDLMSSNSettings getSNSettings()
    {
        return m_Base.getSNSettings();
    }

    /** 
     Quality Of Service is an analysis of nonfunctional aspects of the software properties.	 
    */
    public final int getValueOfQualityOfService()
    {
        return m_Base.getValueOfQualityOfService();
    }
    public final void setValueOfQualityOfService(int value)
    {
        m_Base.setValueOfQualityOfService(value);
    }

    /** 
     Retrieves the amount of unused bits.
    */
    public final int getNumberOfUnusedBits()
    {
        return m_Base.getNumberOfUnusedBits();
    }
    public final void setNumberOfUnusedBits(int value)
    {
        m_Base.setNumberOfUnusedBits(value);
    }

    /** 
     Get command, OBIS Code and attribute index.
     @param data        
     @param name
     @param attributeIndex
    */
    private void getCommand(int cmd, byte[] data, ObjectType[] type, List<Object> names, int[] attributeIndex, int selector[], Object[] parameters)
    {
        selector[0] = 0;
        type[0] = ObjectType.NONE;
        int[] index = new int[1];
        names.clear();
        if (this.getUseLogicalNameReferencing())
        {
            type[0] = ObjectType.forValue(GXCommon.getUInt16(data, index));
            java.nio.ByteBuffer tmp = java.nio.ByteBuffer.wrap(GXCommon.rawData(data, index, 6));
            StringBuilder bcd = new StringBuilder(tmp.limit() * 4);
            for (int it : tmp.array())
            {
                if (bcd.length() != 0)
                {
                    bcd.append(".");
                }
                bcd.append(String.format("%d", it & 0xFF));
            }
            names.add(bcd.toString());
            attributeIndex[0] = data[index[0]++];
            //if Value
            if (data.length - index[0] != 0)
            {
                //If access selector is used.
                if (data[index[0]++] != 0)
                {
                    if (cmd != Command.MethodRequest.getValue())
                    {
                        selector[0] = data[index[0]++];
                    }
                }
                int[] a = new int[1], b = new int[1], c = new int[1];
                DataType[] dt = new DataType[]{DataType.NONE};
                parameters[0] = GXCommon.getData(data, index, 0, a, b, dt, c);
            } 
        }
        else
        {
            attributeIndex[0] = 0;
            int cnt = data[index[0]++];
            if (cmd == Command.ReadRequest.getValue())
            {
                for (int pos = 0; pos != cnt; ++pos)
                {
                    int tp = data[index[0]++];
                    if (tp == 2)
                    {
                        names.add(GXCommon.getUInt16(data, index));
                    }
                    else if (tp == 4)
                    {
                        names.add(GXCommon.getUInt16(data, index));
                        selector[0] = data[index[0]++];
                        int[] a = new int[1], b = new int[1], c = new int[1];
                        DataType[] dt = new DataType[]{DataType.NONE};
                        parameters[0] = GXCommon.getData(data, index, 0, a, b, dt, c);
                    }
                    else
                    {
                        throw new RuntimeException("Invalid parameter.");
                    }
                }
            }
            else if (cmd == Command.WriteRequest.getValue())
            {
                List<Byte> accessTypes = new ArrayList<Byte>();
                for (int pos = 0; pos != cnt; ++pos)
                {
                    accessTypes.add(data[index[0]++]);
                    names.add(GXCommon.getUInt16(data, index));
                }
                //Get data count
                cnt = GXCommon.getObjectCount(data, index);
                for (int pos = 0; pos != cnt; ++pos)
                {
                    if (accessTypes.get(pos) == 4)
                    {
                        selector[0] = data[index[0]++];
                    }
                    int[] a = new int[1], b = new int[1], c = new int[1];
                    DataType[] dt = new DataType[]{DataType.NONE};
                    parameters[0] = GXCommon.getData(data, index, 0, a, b, dt, c);
                }
            }               
        }
    }
       
    /** 
     Generates an acknowledgment message, with which the server is informed to 
     send next packets.

     @param type Frame type
     @return Acknowledgment message as byte array.
    */
    private byte[] receiverReady(RequestTypes type)
    {
        return m_Base.receiverReady(type);
    }

    /** 
     This method is used to solve current index of received DLMS packet, 
     by retrieving the current progress status.

     @param data DLMS data to parse.
     @return The current index of the packet.
    */
    public final int getCurrentProgressStatus(byte[] data)
    {
        return m_Base.getCurrentProgressStatus(data);
    }

    /** 
     This method is used to solve the total amount of received items,
     by retrieving the maximum progress status.

     @param data DLMS data to parse.
     @return Total amount of received items.
    */
    public final int getMaxProgressStatus(byte[] data)
    {
        return m_Base.getMaxProgressStatus(data);
    }

    /** 
      Initialize server.

     This must call after server objects are set.
    */
    public final void initialize()
    {
        if (getAuthentications().isEmpty())
        {
            throw new RuntimeException("Authentications is not set.");
        }
        boolean association = false;
        Initialized = true;
        if (SortedItems.size() != getItems().size())
        {
            for (int pos = 0; pos != m_Items.size(); ++pos)
            {
                GXDLMSObject it = m_Items.get(pos);
                if (it.getLogicalName() == null)
                {
                    throw new RuntimeException("Invalid Logical Name.");
                }
                if (it instanceof GXDLMSProfileGeneric)
                {
                    GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric)((it instanceof GXDLMSProfileGeneric) ? it : null);
                    pg.setServer(this);
                    if (pg.getProfileEntries() < 1)
                    {
                        throw new RuntimeException("Invalid Profile Entries. Profile entries tells amount of rows in the table.");
                    }
                    if (pg.getProfileEntries() < 1)
                    {
                        throw new RuntimeException("Invalid Profile Entries. Profile entries tells amount of rows in the table.");
                    }
                    if (pg.getCapturePeriod() > 0)
                    {
                        new GXProfileGenericUpdater(this, pg).start();
                    }
                }
                else if ((it instanceof GXDLMSAssociationShortName && 
                        !this.getUseLogicalNameReferencing()) || 
                        (it instanceof GXDLMSAssociationLogicalName && 
                        this.getUseLogicalNameReferencing()))
                {
                    association = true;
                }
                else if (!(it instanceof IGXDLMSBase)) //Remove unsupported items.
                {
                    System.out.println(it.getObjectType().toString() + " not supported.");
                    m_Items.remove(pos);
                    --pos;
                }
            }
            if (!association)
            {
                if (getUseLogicalNameReferencing())
                {
                    GXDLMSAssociationLogicalName ln = new GXDLMSAssociationLogicalName();
                    for(GXDLMSObject it : m_Items)
                    {
                        ln.getObjectList().add(it);
                    }
                    m_Items.add(ln);
                }
                else
                {
                    GXDLMSAssociationShortName sn = new GXDLMSAssociationShortName();
                    for(GXDLMSObject it : m_Items)
                    {
                        sn.getObjectList().add(it);
                    }
                    m_Items.add(sn);
                }
            }
            //Arrange items by Short Name.
            short sn = 0xA0;
            if (!this.getUseLogicalNameReferencing())
            {
                SortedItems.clear();
                for (GXDLMSObject it : m_Items)
                {                    
                    //Generate Short Name if not given.
                    if (it.getShortName() == 0)
                    {
                        do
                        {
                            it.setShortName(sn);
                            sn += 0xA0;
                        }
                        while (SortedItems.containsKey(it.getShortName() & 0xFFFF));
                    }
                    SortedItems.put(it.getShortName() & 0xFFFF, it);
                }
            }
        }
    }

    /** 
     Parse AARQ request that cliend send and returns AARE request.

     @param data
     @return 
    */
    private byte[] handleAARQRequest(byte[] data) throws Exception
    {
        int[] error = new int[]{0};
        byte[] frame = new byte[]{0};
        java.nio.ByteBuffer arr = java.nio.ByteBuffer.wrap(data);
        boolean[] packetFull = new boolean[]{false}, wrongCrc = new boolean[]{false};
        int[] command = new int[]{0};
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        m_Base.getDataFromFrame(arr, tmp, frame, true, error, false, packetFull, wrongCrc, command);
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        GXAPDU aarq = new GXAPDU(null);
        aarq.setUseLN(this.getUseLogicalNameReferencing());
        arr = java.nio.ByteBuffer.wrap(tmp.toByteArray());
        aarq.encodeData(arr);
        AssociationResult result = AssociationResult.ACCEPTED;
        SourceDiagnostic diagnostic = SourceDiagnostic.NONE;
        m_Base.setAuthentication(aarq.authentication);
        m_Base.CtoSChallenge = null;
        m_Base.StoCChallenge = null;
        if (aarq.authentication.getValue() >= Authentication.HIGH.getValue())
        {
            m_Base.CtoSChallenge = aarq.password;
        }
        if (this.getUseLogicalNameReferencing() != aarq.getUseLN())
        {
            result = AssociationResult.PERMANENT_REJECTED;
            diagnostic = SourceDiagnostic.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED;
        }
        else
        {
            GXAuthentication auth = null;
            for (GXAuthentication it : getAuthentications())
            {
                if (it.getType() == aarq.authentication)
                {
                    auth = it;
                    break;
                }
            }
            if (auth == null)
            {
                result = AssociationResult.PERMANENT_REJECTED;
                //If authentication is required.
                if (aarq.authentication == Authentication.NONE)
                {
                    diagnostic = SourceDiagnostic.AUTHENTICATION_REQUIRED;
                }
                else
                {
                    diagnostic = SourceDiagnostic.AUTHENTICATION_MECHANISM_NAME_NOT_RECOGNISED;
                }
            }
            //If authentication is used check pw.
            else if (aarq.authentication != Authentication.NONE)
            {
                //If Low authentication is used and pw don't match.                    
                if (aarq.authentication == Authentication.LOW)
                {
                    if (!java.util.Arrays.equals(auth.getPassword(), aarq.password))
                    {
                        result = AssociationResult.PERMANENT_REJECTED;
                        diagnostic = SourceDiagnostic.AUTHENTICATION_FAILURE;
                    }
                }
                else //If High authentication is used.
                {
                    m_Base.StoCChallenge = GXDLMS.generateChallenge();
                    result = AssociationResult.ACCEPTED;
                    diagnostic = SourceDiagnostic.AUTHENTICATION_REQUIRED;
                }
            }
        }
        //Generate AARE packet.
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(150);
        byte[] conformanceBlock;
        if (getUseLogicalNameReferencing())
        {
            conformanceBlock = getLNSettings().conformanceBlock;
        }
        else
        {
            conformanceBlock = getSNSettings().conformanceBlock;
        }
        if (this.getInterfaceType() == InterfaceType.GENERAL)
        {
            buff.put((byte) 0xE6);
            buff.put((byte) 0xE7);
            buff.put((byte) 0x00);
        }        
        aarq.generateAARE(buff, aarq.authentication, m_Base.StoCChallenge, getMaxReceivePDUSize(), conformanceBlock, result, diagnostic);
        m_Base.expectedFrame = 0;
        m_Base.frameSequence = -1;
        return m_Base.addFrame(m_Base.generateIFrame(), false, buff, 0, buff.position());
    }

    private void setValue(java.nio.ByteBuffer buff, Object data) throws Exception
    {
        byte[] tmp = GXCommon.getAsByteArray(data);
        buff.put((byte)tmp.length);
        buff.put(tmp);
    }

    /** 
     Parse SNRM Request.


     If server do not accept client empty byte array is returned.

     @return Returns returned UA packet.
    */
    private byte[] handleSnrmRequest() throws Exception
    {        
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(25);    
        buff.put((byte) 0x81); //FromatID
        buff.put((byte) 0x80); //GroupID
        buff.put((byte) 0); //len
        buff.put(HDLCInfo.MaxInfoTX);
        setValue(buff, getLimits().getMaxInfoTX());
        buff.put(HDLCInfo.MaxInfoRX);
        setValue(buff, getLimits().getMaxInfoRX());
        buff.put(HDLCInfo.WindowSizeTX);
        setValue(buff, getLimits().getWindowSizeTX());
        buff.put(HDLCInfo.WindowSizeRX);
        setValue(buff, getLimits().getWindowSizeRX());
        int len = (byte)buff.position() - 3;
        buff.put(2, (byte) len); //len
        return m_Base.addFrame((byte)FrameType.UA.getValue(), false, buff, 0, buff.position());
    }

    /** 
     Generate disconnect request.
    */
    private byte[] generateDisconnectRequest() throws Exception
    {       
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(2);
            buff.put((byte) 0x63);
            buff.put((byte) 0x0);
            return m_Base.addFrame((byte) 0, false, buff, 0, buff.position());
        }
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(20);
        buff.put((byte) 0x81); //FromatID
        buff.put((byte) 0x80); //GroupID
        buff.put((byte) 0); //len        
        buff.put((byte)HDLCInfo.MaxInfoTX);
        setValue(buff, getLimits().getMaxInfoTX());
        buff.put((byte)HDLCInfo.MaxInfoRX);
        setValue(buff, getLimits().getMaxInfoRX());
        buff.put((byte)HDLCInfo.WindowSizeTX);
        setValue(buff, getLimits().getWindowSizeTX());
        buff.put((byte)HDLCInfo.WindowSizeRX);
        setValue(buff, getLimits().getWindowSizeRX());
        byte len = (byte) (buff.position() - 3);
        buff.put(2, len); //len
        return m_Base.addFrame(FrameType.UA.getValue(), false, buff, 0, buff.position());
    }

    /** 
     Reserved for internal use.
    */
    private Object getAdd(byte[] buff, int index[])
    {
        int size = 0;
        for (int pos = index[0]; pos != buff.length; ++pos)
        {
            ++size;
            if ((buff[pos] & 0x1) == 1)
            {
                break;
            }
        }
        if (size == 1)
        {
            return buff[index[0]++];
        }
        else if (size == 2)
        {
            return GXCommon.getUInt16(buff, index);
        }
        else if (size == 4)
        {
            return GXCommon.getUInt32(buff, index);
        }
        throw new InvalidParameterException("Wrong size.");
    }

    /** 
     Reserved for internal use.
    */
    public final boolean getAddress(byte[] buff, Object[] clientId, Object[] serverId)
            throws UnsupportedEncodingException
    {
        int[] index = new int[]{0};
        int PacketStartID = 0, len = buff.length;
        int FrameLen = 0;
        //If DLMS frame is generated.
        if (getInterfaceType() != InterfaceType.NET)
        {
            //Find start of HDLC frame.
            for (; index[0] < len; ++index[0])
            {
                if (buff[index[0]] == GXCommon.HDLCFrameStartEnd)
                {
                    PacketStartID = index[0];
                    ++index[0];
                    break;
                }
            }
            if (index[0] == len) //Not a HDLC frame.
            {
                throw new GXDLMSException("Invalid data format.");
            }
            byte frame = buff[index[0]++];
            java.util.Set<RequestTypes> MoreData = EnumSet.noneOf(RequestTypes.class);           
            //Is there more data available.
            if ((frame & 0x8) != 0)
            {
                MoreData = EnumSet.of(RequestTypes.FRAME);
            }
            //Check frame length.
            if ((frame & 0x7) != 0)
            {
                FrameLen = ((frame & 0x7) << 8);
            }            
            //If not enought data.
            FrameLen += buff[index[0]++];
            if (len < FrameLen + index[0] - 1)
            {
                return false;
            }
            if (MoreData.isEmpty() && 
                buff[FrameLen + PacketStartID + 1] != GXCommon.HDLCFrameStartEnd)
            {
                throw new GXDLMSException("Invalid data format.");
            }
            serverId[0] = getAdd(buff, index);            
            //Client address is always one byte.
            clientId[0] = getAdd(buff, index); 
        }
        else
        {
            //Get version
            int ver = (buff[index[0]++] & 0xFF) << 8;
            ver |= buff[index[0]++] & 0xFF;
            if (ver != 1)
            {
                throw new GXDLMSException("Unknown version.");
            }
            clientId[0] = (short) GXCommon.getUInt16(buff, index);
            serverId[0] = (short) GXCommon.getUInt16(buff, index);
        }
        return true;
    }

    /** 
     Reset after connection is closed.
    */
    public final void reset()
    {
        try
        {
            ReceivedFrame.close();
            ReceivedData.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        LastCommand = 0;
        m_Base.setServerID(null);
        m_Base.setClientID(null);
        m_Base.setAuthentication(Authentication.NONE);
        m_Base.Ciphering.setSecurity(Security.NONE);
        m_Base.Ciphering.setFrameCounter(0);
    }

    /** 
     Mandles client request.

     @param buff Received data from the client.
     @return Response to the request.        

     Response is null if request packet is not compleate.

    */
    public final byte[] handleRequest(byte[] buff)
    {
        if (buff == null)
        {
            return null;
        }
        if (!Initialized)
        {
            throw new RuntimeException("Server not Initialized.");
        }
        int[] command = new int[1];
        try
        {
            byte[] data;
            if (ReceivedFrame.size() != 0)
            {
                ReceivedFrame.write(buff);
                data = ReceivedFrame.toByteArray();
            }
            else
            {
                data = buff;
            }            
            if (m_Base.getServerID() == null)
            {
                Object[] sid = new Object[1], cid = new Object[1];
                getAddress(data, cid, sid);
                for (Object it : this.getServerIDs())
                {
                    if (((Number)sid[0]).intValue() == ((Number)it).intValue())
                    {
                        m_Base.setServerID(sid[0]);
                        m_Base.setClientID(cid[0]);
                        break;
                    }                                            
                }
                //We do not communicate if server ID not found.
                if (m_Base.getServerID() == null)
                {
                    invalidConnection(new ConnectionEventArgs(sid));
                    return null;
                }
            }
            if (!m_Base.isDLMSPacketComplete(data))
            {
                if (ReceivedFrame.size() == 0)
                {
                    ReceivedFrame.write(buff);
                }
                return null; //Wait more data.
            }
            List<Object> names = new ArrayList<Object>();
            GXDLMSObject item = null;
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            byte[] frame = new byte[1];                       
            java.util.Set<RequestTypes> ret = m_Base.getDataFromPacket(data, tmp, frame, command);
            byte[] allData = tmp.toByteArray();
            ReceivedFrame.reset();            
            //Ask next frame.
            if (ret.contains(RequestTypes.FRAME))
            {
                ++FrameIndex;
                //Add new data.
                if ((frame[0] & 0x1) == 0)
                {
                    if (command[0] != 0)
                    {
                        LastCommand = command[0];
                    }
                    ReceivedData.write(allData);
                    SendData.clear();
                    FrameIndex = 0;
                    --m_Base.expectedFrame;
                    return m_Base.receiverReady(RequestTypes.FRAME);                                
                }
                //Keep alive...
                else if (FrameIndex >= SendData.size() && (frame[0] & 0x1) == 1)                            
                {
                    SendData.clear();
                    FrameIndex = 0;
                    return m_Base.addFrame(m_Base.generateAliveFrame(), false, (byte[]) null, 0, 0);
                }
                return SendData.get(FrameIndex);
            }
            //Ask next data block.
            else if (ret.contains(RequestTypes.DATABLOCK))
            {
                //Add new data.
                if ((frame[0] & 0x1) == 0)
                {
                    if (command[0] != 0)
                    {
                        LastCommand = command[0];
                    }
                    ReceivedData.write(allData);
                    SendData.clear();
                    FrameIndex = 0;                                
                    return m_Base.receiverReady(RequestTypes.DATABLOCK);
                }
                ++FrameIndex;
                int[] index = new int[1];
                int BlockIndex = (int)GXCommon.getUInt32(allData, index);
                return SendData.get(FrameIndex);
            }
            if (ReceivedData.size() != 0)
            {
                ReceivedData.write(allData);
                allData = ReceivedData.toByteArray();
                ReceivedData.reset();
                command[0] = LastCommand;
            }
            FrameIndex = 0;
            SendData.clear();
            if (command[0] == Command.GloGetRequest.getValue() ||
                command[0] == Command.GloSetRequest.getValue() ||
                command[0] == Command.GloMethodRequest.getValue())
            {
                Command[] cmd = new Command[1];
                allData = m_Base.decrypt(allData, cmd);
                command[0] = cmd[0].getValue();
            }
            if (command[0] == Command.Snrm.getValue())
            {
                SendData.add(handleSnrmRequest());
                return SendData.get(FrameIndex);
            }
            else if (command[0] == Command.Aarq.getValue())
            {
                SendData.add(handleAARQRequest(data));
                return SendData.get(FrameIndex);
            }
            
            else if (command[0] == Command.DisconnectRequest.getValue())            
            {
                System.out.println("Disonnecting");
                SendData.add(generateDisconnectRequest());
                return SendData.get(FrameIndex);
            }
            else if (command[0] == Command.WriteRequest.getValue())
            {
                int attributeIndex;
                int tmp2[] = new int[1];
                Object[] value = new Object[1];                
                ObjectType[] type = new ObjectType[1];
                int[] selector = new int[1];
                getCommand(command[0], allData, type, names, tmp2, selector, value);
                int sn = ((Number)names.get(0)).intValue();
                for (Map.Entry<Integer, GXDLMSObject> it : SortedItems.entrySet())                    
                {
                    int aCnt = ((IGXDLMSBase) it.getValue()).getAttributeCount();
                    if (sn >= it.getKey() && sn <= (it.getKey() + (8 * aCnt)))
                    {
                        item = it.getValue();
                        attributeIndex = ((sn - item.getShortName()) / 8) + 1;                        
                        //If write is denied.
                        AccessMode acc = item.getAccess(attributeIndex);
                        if (acc == AccessMode.NO_ACCESS || acc == AccessMode.READ ||
                            acc == AccessMode.AUTHENTICATED_READ)
                        {
                            SendData.addAll(Arrays.asList(serverReportError(Command.forValue(command[0]), 3)));
                            return SendData.get(FrameIndex);
                        }
                        if (value instanceof Byte[])
                        {
                            DataType tp = item.getUIDataType(attributeIndex);
                            if (tp != DataType.NONE)
                            {
                                value[0] = GXDLMSClient.changeType((byte[])value[0], tp);
                            }
                        }
                        ValueEventArgs e = new ValueEventArgs(item, attributeIndex, selector[0]);
                        e.setValue(value[0]);
                        write(e);
                        if (!e.getHandled())
                        {
                            ((IGXDLMSBase) item).setValue(attributeIndex, value[0]);
                        }
                        //Return OK.
                        SendData.addAll(Arrays.asList(acknowledge(getUseLogicalNameReferencing() ? Command.SetResponse : Command.WriteResponse, 0)));
                        return SendData.get(FrameIndex);
                    }
                }     
                if (item == null)
                {
                    throw new IllegalArgumentException("Unknown object.");
                }               
            }
            else if (command[0] == Command.SetRequest.getValue())
            {
                int attributeIndex[] = new int[1];
                Object[] value = new Object[1];                
                ObjectType[] type = new ObjectType[1];
                int[] selector = new int[1];
                getCommand(command[0], allData, type, names, attributeIndex, selector, value);                
                item = getItems().findByLN(type[0], names.get(0).toString());
                if (item != null)
                {
                    //If write is denied.
                    AccessMode acc = item.getAccess(attributeIndex[0]);
                    if (acc == AccessMode.NO_ACCESS || acc == AccessMode.READ ||
                        acc == AccessMode.AUTHENTICATED_READ)
                    {
                        SendData.addAll(Arrays.asList(serverReportError(Command.forValue(command[0]), 3)));
                        return SendData.get(FrameIndex);
                    }
                    if (value instanceof Byte[])
                    {
                        DataType tp = item.getUIDataType(attributeIndex[0]);
                        if (tp != DataType.NONE)
                        {
                            value[0] = GXDLMSClient.changeType((byte[])value[0], tp);
                        }
                    }
                    ValueEventArgs e = new ValueEventArgs(item, attributeIndex[0], selector[0]);
                    e.setValue(value[0]);
                    write(e);
                    if (!e.getHandled())
                    {
                        ((IGXDLMSBase) item).setValue(attributeIndex[0], value[0]);
                    }
                    //Return OK.
                    SendData.addAll(Arrays.asList(acknowledge(getUseLogicalNameReferencing() ? Command.SetResponse : Command.WriteResponse, 0)));
                    return SendData.get(FrameIndex);
                }
                throw new IllegalArgumentException("Unknown object.");
            }
            else if (command[0] == Command.ReadRequest.getValue() && !getUseLogicalNameReferencing())
            {
                int attributeIndex;
                int tmp2[] = new int[1];
                Object[] value = new Object[1];                
                ObjectType[] type = new ObjectType[1];
                int[] selector = new int[1];
                getCommand(command[0], allData, type, names, tmp2, selector, value);
                int sn = ((Number)names.get(0)).intValue();
                for (Map.Entry<Integer, GXDLMSObject> it : SortedItems.entrySet())
                {
                    int aCnt = ((IGXDLMSBase) it.getValue()).getAttributeCount();
                    if (sn >= it.getKey() && sn <= (it.getKey() + (8 * aCnt)))
                    {
                        item = it.getValue();
                        attributeIndex = ((sn - item.getShortName()) / 8) + 1;                        
                        System.out.println(String.format("Reading %d, attribute index %d", item.getName(), attributeIndex));
                        ValueEventArgs e = new ValueEventArgs(item, attributeIndex, selector[0]);
                        e.setValue(value[0]);
                        read(e);
                        if (e.getHandled())
                        {
                            DataType tp = item.getDataType(attributeIndex);                            
                            SendData.addAll(Arrays.asList(readReply(names.get(0), type[0], attributeIndex, e.getValue(), tp)));
                            return SendData.get(FrameIndex);
                        }
                        if (item != null)
                        {
                            return getValue(names.get(0), item, attributeIndex, selector[0], value[0]);
                        }
                    }
                    //If action.
                    else if (sn >= it.getKey() + aCnt && ((IGXDLMSBase) it.getValue()).getMethodCount() != 0)
                    {
                        //Convert DLMS data to object type.
                        int[] value2 = new int[1], count = new int[1];
                        GXDLMS.getActionInfo(it.getValue().getObjectType(), value2, count);
                        if (sn <= it.getKey() + value2[0] + (8 * count[0]))//If action
                        {
                            item = it.getValue();
                            attributeIndex = ((sn - item.getShortName() - value2[0]) / 8) + 1;
                            System.out.println(String.format("Reading %d, attribute index %d", item.getName(), attributeIndex));
                            ValueEventArgs e = new ValueEventArgs(item, attributeIndex, selector[0]);                            
                            e.setValue(value[0]);
                            action(e);
                            if (!e.getHandled())
                            {
                                byte[][] reply = ((IGXDLMSBase) item).invoke(this, attributeIndex, e.getValue());
                                if (reply != null)
                                {
                                    SendData.addAll(Arrays.asList(reply));
                                    return SendData.get(FrameIndex);
                                }
                            }
                            SendData.addAll(Arrays.asList(acknowledge(Command.MethodResponse, 0)));
                            return SendData.get(FrameIndex);
                        }
                    }
                }                           
                throw new IllegalArgumentException("Unknown object.");                
            }
            else if (command[0] == Command.GetRequest.getValue() && getUseLogicalNameReferencing())
            {
                ObjectType[] type = new ObjectType[]{ObjectType.NONE};
                int[] index = new int[1];
                Object[] parameter = new Object[1];
                int[] selector = new int[1];
                getCommand(command[0], allData, type, names, index, selector, parameter);
                System.out.println(String.format("Reading %s, attribute index %d", names, index[0]));
                item = m_Items.findByLN(type[0], names.get(0).toString());
                if (item != null)
                {
                    ValueEventArgs e = new ValueEventArgs(item, index[0], selector[0]);
                    read(e);
                    if (e.getHandled())
                    {                        
                        SendData.addAll(Arrays.asList(readReply(names.get(0), type[0], index[0], e.getValue(), e.getDataType())));
                        return SendData.get(FrameIndex);
                    }
                    return getValue(names.get(0), item, index[0], selector[0], parameter[0]);
                }
            }           
            else if (command[0] == Command.MethodRequest.getValue())
            {
                ObjectType[] type = new ObjectType[1];
                int[] index = new int[1];
                Object[] parameter = new Object[1];
                int[] selector = new int[1];
                getCommand(command[0], allData, type, names, index, selector, parameter);
                item = m_Items.findByLN(type[0], names.get(0).toString());
                if (item != null)
                {
                    System.out.println(String.format("Action on %s, attribute index %d", names.get(0), index[0]));
                    ValueEventArgs e = new ValueEventArgs(item, index[0], selector[0]);
                    e.setValue(parameter[0]);
                    action(e);
                    if (!e.getHandled())
                    {
                        byte[][] reply = item.invoke(this, index[0], e.getValue());
                        if (reply != null)
                        {
                            SendData.addAll(Arrays.asList(reply));
                            return SendData.get(FrameIndex);
                        }
                    }
                    SendData.addAll(Arrays.asList(acknowledge(Command.MethodResponse, 0)));
                    return SendData.get(FrameIndex);                    
                }
            }
            //Return HW error.
            SendData.addAll(Arrays.asList(serverReportError(Command.forValue(command[0]), 3)));
            return SendData.get(FrameIndex);
        }
        catch (java.lang.Exception e)
        {
            //Return HW error.
            ReceivedFrame.reset();
            SendData.addAll(Arrays.asList(serverReportError(Command.forValue(command[0]), 3)));
            return SendData.get(FrameIndex);
        }
    }
 
    private byte[] getValue(Object name, GXDLMSObject item, int index, int selector, Object parameters)
            throws RuntimeException, UnsupportedEncodingException, 
            ParseException, IOException
    {
        Object value;        
        IGXDLMSBase base = (IGXDLMSBase) item;
        if (base != null)
        {            
            value = item.getValue(index, selector, parameters);
            DataType tp = item.getDataType(index);
            if (tp == DataType.NONE)
            {
                tp = GXCommon.getValueType(value);
            } 
            if (tp == DataType.OCTET_STRING && value instanceof String && 
                item.getUIDataType(index) == DataType.STRING)
            {
                value = ((String)value).getBytes("ASCII");
            } 
            if (tp != DataType.NONE || (value == null && tp == DataType.NONE))
            {
                SendData.addAll(Arrays.asList(readReply(name, item.getObjectType(), index, value, tp)));
                return SendData.get(FrameIndex);
            }
        }
        else
        {
            Object[] values = item.getValues();
            if (index <= values.length)
            {
                value = values[index - 1];
                DataType tp = item.getDataType(index);
                if (tp == DataType.NONE)
                {
                    throw new IllegalArgumentException("Invalid data type.");                    
                }
                if (tp != DataType.NONE || (value == null && tp == DataType.NONE))
                {
                    SendData.addAll(Arrays.asList(readReply(name, item.getObjectType(), index, value, tp)));
                    return SendData.get(FrameIndex);
                }
            }
        }
        //Return HW error.
        throw new RuntimeException("");
    }

    /** 
     Generates a read message.

     @param name Short or Logical Name.
     @param objectType Read Interface.
     @param attributeOrdinal Read attribute index.
     @return Read request as byte array.
    */
    public final byte[][] readReply(Object name, ObjectType objectType, 
            int attributeOrdinal, Object value, DataType type)
            throws RuntimeException, UnsupportedEncodingException, 
            ParseException, IOException
    {
        if ((objectType != ObjectType.NONE && attributeOrdinal < 0))
        {
            throw new GXDLMSException("Invalid parameter");
        }
        ByteArrayOutputStream data = new ByteArrayOutputStream();        
        if (type == DataType.NONE)
        {
            type = GXCommon.getValueType(value);
        }
        GXCommon.setData(data, type, value);        
        return m_Base.generateMessage(name, data.toByteArray(), objectType, 
                attributeOrdinal, 
                this.getUseLogicalNameReferencing() ? Command.GetResponse : Command.ReadResponse);
    }

    public byte[][] acknowledge(Command cmd, int status)
    {
        return acknowledge(cmd, status, null, DataType.NONE);
    }
    
    /** 
     Generates a acknowledge message.
    */
    public byte[][] acknowledge(Command cmd, int status, Object data, DataType type)
    {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        if (!this.getUseLogicalNameReferencing())
        {
            buff.write(0x01);
            buff.write(status);
        }
        if (type != DataType.NONE)
        {
            buff.write(0x01);
            buff.write(0x00);
            GXCommon.setData(buff, type, data);
        }
        int index[] = new int[1];
        return m_Base.splitToFrames(java.nio.ByteBuffer.wrap(buff.toByteArray()), 1, index, buff.size(), cmd, 0);
    }

    /** 
     Generates a acknowledge message.
    */
    public final byte[][] serverReportError(Command cmd, int serviceErrorCode)
    {
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(4);
        switch (cmd)
        {
            case ReadRequest:
                cmd = Command.ReadResponse;
            break;
            case WriteRequest:        
                cmd = Command.WriteResponse;
            break;
            case GetRequest:
                cmd = Command.GetResponse;
            break;
            case SetRequest:
                cmd = Command.SetResponse;
            break;
            case MethodRequest:
                cmd = Command.MethodResponse;
            break;
            default:
                throw new RuntimeException("Invalid Command.");
        }
        if (!this.getUseLogicalNameReferencing())
        {
            buff.put((byte) 0x01);
            buff.put((byte) 0x01);
            buff.put((byte) serviceErrorCode);
        }        
        int index[] = new int[1];
        return m_Base.splitToFrames(buff, 1, index, buff.position(), cmd, serviceErrorCode);
    }
}