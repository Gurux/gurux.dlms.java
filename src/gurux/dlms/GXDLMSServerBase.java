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

import gurux.dlms.internal.GXCommon;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.RequestTypes;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/** 
 GXDLMSServer implements methods to implement DLMS/COSEM meter/proxy.
*/
abstract public class GXDLMSServerBase
{
    private GXDLMSObjectCollection privateItems;
    private GXDLMS m_Base = new GXDLMS(true);	
    ByteArrayOutputStream ReceivedData = new ByteArrayOutputStream();
    ArrayList<byte[]> SendData = new ArrayList<>();
    int FrameIndex = 0;
    boolean Initialized = false;
    private TreeMap<Integer, GXDLMSObject> SortedItems = new TreeMap<>();
    private ArrayList<Object> privateServerIDs;
    private ArrayList<GXAuthentication> privateAuthentications;

    /*
     * Read selected item.
     */
    abstract public void read(ValueEventArgs e);

    /*
     * Write selected item.
     */
    abstract public void write(ValueEventArgs e);
    
    /*
     * Returns collection of available items.
     */
    abstract public void updateItems();
    
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
        privateItems = new GXDLMSObjectCollection(this);        
        m_Base.setUseLogicalNameReferencing(logicalNameReferencing);
        //TODO: StartProtocol = StartProtocolType.DLMS;
        reset();        
        m_Base.setLNSettings(new GXDLMSLNSettings(new byte[] {0x00, 0x7E, 0x1F}));
        m_Base.setSNSettings(new GXDLMSSNSettings(new byte[] {0x1C, 0x03, 0x20}));
        privateServerIDs = new java.util.ArrayList<>();
        this.setInterfaceType(InterfaceType.GENERAL);        
    }

    /** 
     Count server ID from serial number.

     @param serialNumber
     @return Server ID.
    */
    public static int countServerIDFromSerialNumber(int serialNumber)
    {
        return (int) GXManufacturer.countServerAddress(HDLCAddressType.SERIAL_NUMBER, serialNumber, 0);
    }

    /** 
     Count server ID from physical and logical addresses.

     @return Server ID.
    */
    public final Object countServerID(Object physicalAddress, int LogicalAddress)
    {
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            return (short)physicalAddress;
        }
        return GXManufacturer.countServerAddress(HDLCAddressType.DEFAULT, physicalAddress, LogicalAddress);
    }

    /** 
     List of objects that meter supports.
    */
    public final GXDLMSObjectCollection getItems()
    {
        return privateItems;
    }
    public final void setItems(GXDLMSObjectCollection value)
    {
        privateItems = value;
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
        return privateServerIDs;
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
        return privateAuthentications;
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
        if (changed || privateAuthentications == null)
        {
            m_Base.setInterfaceType(value);
            if (value == InterfaceType.GENERAL)
            {
                if (privateAuthentications == null)
                {
                    privateAuthentications = new ArrayList<>();
                }
                else
                {
                    privateAuthentications.clear();
                    privateServerIDs.clear();
                }
                privateAuthentications.add(new GXAuthentication(Authentication.NONE, "", (byte) 0x10));
                privateAuthentications.add(new GXAuthentication(Authentication.LOW, "GuruxLow", (byte) 0x20));
                privateAuthentications.add(new GXAuthentication(Authentication.HIGH, "GuruxHigh", (byte) 0x40));        
                privateServerIDs.add(countServerID((byte)1, 0));
            }
            else
            {
                if (privateAuthentications == null)
                {
                    privateAuthentications = new ArrayList<>();
                }
                else
                {
                    privateAuthentications.clear();
                    privateServerIDs.clear();
                }
                privateAuthentications.add(new GXAuthentication(Authentication.NONE, "", (short) 0x10));
                privateAuthentications.add(new GXAuthentication(Authentication.LOW, "GuruxLow", (short) 0x20));
                privateAuthentications.add(new GXAuthentication(Authentication.HIGH, "GuruxHigh", (short) 0x40));        
                privateServerIDs.add((short) 1);
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
    private void getCommand(byte[] data, ObjectType[] type, Object[] name, int[] attributeIndex, Object[] parameters)
    {
        type[0] = ObjectType.NONE;
        int[] index = new int[1];
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
            name[0] = bcd.toString();
            attributeIndex[0] = data[index[0]++];
            //Skip data index
            ++index[0];
            int cnt = data.length - index[0];                        
            if (cnt != 0)
            {
                parameters[0] = new byte[cnt];
                System.arraycopy(data, index[0], (byte[]) parameters[0], 0, cnt);
            }
        }
        else
        {
            attributeIndex[0] = 0;
            ++index[0]; //Cnt.
            ++index[0]; //Len.
            name[0] = GXCommon.getUInt16(data, index);
            int cnt = data.length - index[0];                        
            if (cnt != 0)
            {
                parameters[0] = new byte[cnt];
                System.arraycopy(data, index[0], (byte[]) parameters[0], 0, cnt);
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
            for (int pos = 0; pos != privateItems.size(); ++pos)
            {
                GXDLMSObject it = privateItems.get(pos);
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
                    for (GXDLMSObject obj : pg.getCaptureObjects())
                    {
                        if (obj.getSelectedAttributeIndex() < 1)
                        {
                            throw new RuntimeException("Invalid attribute index. SelectedAttributeIndex is not set for " + obj.getName());
                        }
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
                    privateItems.remove(pos);
                    --pos;
                }
            }
            if (!association)
            {
                if (getUseLogicalNameReferencing())
                {
                    GXDLMSAssociationLogicalName ln = new GXDLMSAssociationLogicalName();
                    for(GXDLMSObject it : privateItems)
                    {
                        ln.getObjectList().add(it);
                    }
                    privateItems.add(ln);
                }
                else
                {
                    GXDLMSAssociationShortName sn = new GXDLMSAssociationShortName();
                    for(GXDLMSObject it : privateItems)
                    {
                        sn.getObjectList().add(it);
                    }
                    privateItems.add(sn);
                }
            }
            //Arrange items by Short Name.
            short sn = 0xA0;
            if (!this.getUseLogicalNameReferencing())
            {
                SortedItems.clear();
                for (GXDLMSObject it : privateItems)
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
        int[] pos = new int[]{0};
        aarq.encodeData(arr);
        AssociationResult result = AssociationResult.ACCEPTED;
        SourceDiagnostic diagnostic = SourceDiagnostic.NONE;
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
            else if (aarq.authentication != Authentication.NONE && 
                    !auth.getPassword().equals(aarq.password) && 
                    auth.getPassword().length() != aarq.password.length())
            {
                result = AssociationResult.PERMANENT_REJECTED;
                diagnostic = SourceDiagnostic.AUTHENTICATION_FAILURE;
            }
        }
        //Generate AARE packet.
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(100);
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
        aarq.generateAARE(buff, getMaxReceivePDUSize(), conformanceBlock, result, diagnostic);
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
        buff.put((byte)HDLCInfo.MaxInfoTX);
        setValue(buff, getLimits().getMaxInfoTX());
        buff.put((byte)HDLCInfo.MaxInfoRX);
        setValue(buff, getLimits().getMaxInfoRX());
        buff.put((byte)HDLCInfo.WindowSizeTX);
        setValue(buff, getLimits().getWindowSizeTX());
        buff.put((byte)HDLCInfo.WindowSizeRX);
        setValue(buff, getLimits().getWindowSizeRX());
        byte len = (byte) buff.position();
        buff.put(0, (byte) 0x81); //FromatID
        buff.put(1, (byte) 0x80); //GroupID
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
            RequestTypes MoreData = RequestTypes.NONE;
            //Is there more data available.
            if (frame == GXCommon.HDLCFrameTypeMoreData)
            {
                MoreData = RequestTypes.FRAME;
            }
            //If not enought data.
            FrameLen = buff[index[0]++];
            if (len < FrameLen + index[0] - 1)
            {
                return false;
            }
            if ((frame != GXCommon.HDLCFrameType && frame != GXCommon.HDLCFrameTypeMoreData) || 
                    (MoreData == RequestTypes.NONE && 
                    buff[FrameLen + PacketStartID + 1] != GXCommon.HDLCFrameStartEnd)) //Check EOP - Check BOP
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
            ReceivedData.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        //TODO: Protocol = StartProtocol;
        m_Base.setServerID(null);
        m_Base.setClientID(null);
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
        try
        {
            byte[] data;
            if (ReceivedData.size() != 0)
            {
                ReceivedData.write(buff);
                data = ReceivedData.toByteArray();
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
                if (ReceivedData.size() == 0)
                {
                    ReceivedData.write(buff);
                }
                return null; //Wait more data.
            }
            Object[] name = new Object[1];
            GXDLMSObject item = null;
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            byte[] frame = new byte[1];
            int[] command = new int[1];
            java.util.Set<RequestTypes> ret = m_Base.getDataFromPacket(data, tmp, frame, command);
            byte[] allData = tmp.toByteArray();
            ReceivedData.reset();
            //Ask next frame.
            if (ret.contains(RequestTypes.FRAME))
            {
                ++FrameIndex;
                //Keep alive...
                if (FrameIndex >= SendData.size())
                {
                    FrameIndex = 0;
                    SendData.clear();
                    SendData.add(m_Base.addFrame(m_Base.generateAliveFrame(), false, null, 0, 0));
                    return SendData.get(FrameIndex);
                }
                return SendData.get(FrameIndex);
            }            
            //Ask next data block.
            else if (ret.contains(RequestTypes.DATABLOCK))
            {
                ++FrameIndex;
                int[] index = new int[]{0};
                GXCommon.getUInt32(allData, index);                    
                return SendData.get(FrameIndex);
            }                      
            FrameIndex = 0;
            SendData.clear();
            if (command[0] == Command.Aarq.getValue())
            {
                SendData.add(handleAARQRequest(data));
                return SendData.get(FrameIndex);
            }
            else if (command[0] == Command.Snrm.getValue())
            {
                SendData.add(handleSnrmRequest());
                return SendData.get(FrameIndex);
            }
            else if (command[0] == Command.DisconnectRequest.getValue())            
            {
                SendData.add(generateDisconnectRequest());
                return SendData.get(FrameIndex);
            }
            else if (command[0] == Command.WriteRequest.getValue() ||
                    (command[0] == Command.SetRequest.getValue()))
            {
                int[] index = new int[1];
                Object value;
                if (!getUseLogicalNameReferencing())
                {
                    index[0]++;//Get item count.
                    index[0]++;//Get item len.
                    int sn = GXCommon.getUInt16(allData, index);
                    //Convert DLMS data to object type.
                    int[] count = new int[1], index2 = new int[1], pos = new int[1];
                    for (Map.Entry<Integer, GXDLMSObject> it : SortedItems.entrySet())                    
                    {
                        if (it.getKey() > sn)
                        {
                            break;
                        }
                        item = it.getValue();
                    }                    
                    int attributeIndex = ((sn - item.getShortName()) / 8) + 1;
                    System.out.println(String.format("Writing %s, attribute index %d", item.getName(), attributeIndex));
                    DataType type = item.getDataType(attributeIndex);
                    DataType[] type2 = new DataType[]{DataType.NONE};
                    value = GXCommon.getData(allData, index, ActionType.NONE.getValue(), 
                            count, index2, type2, pos);
                    if (value instanceof byte[] && type != DataType.NONE)
                    {
                        value = GXDLMSClient.changeType((byte[])value, type);
                    }
                    index[0] = attributeIndex;
                }
                else
                {
                    ObjectType[] type = new ObjectType[1];
                    Object[] parameter = new Object[1];
                    getCommand(allData, type, name, index, parameter);
                    DataType[] type2 = new DataType[]{DataType.NONE};
                    int[] pos = new int[1], index2 = new int[1], index3 = new int[1], count = new int[1];
                    value = GXCommon.getData((byte[]) parameter[0], index3, 
                            ActionType.NONE.getValue(), count, index2, type2, pos);
                    item = privateItems.findByLN(type[0], name[0].toString());
                }
                if (item != null)
                {
                    if (value instanceof byte[])
                    {
                        DataType tp = item.getUIDataType(index[0]);
                        if (tp != DataType.NONE)
                        {
                            value = GXDLMSClient.changeType((byte[])value, tp);
                        }
                    }
                    if (item != null)
                    {
                        ValueEventArgs e = new ValueEventArgs(item, index[0]);
                        e.setValue(value);
                        write(e);
                        if (e.getHandled())
                        {
                            SendData.add(acknowledge(getUseLogicalNameReferencing() ? Command.SetResponse : Command.WriteResponse, 0));
                            return SendData.get(FrameIndex);
                        }
                    }                                        
                    item.setValue(index[0], value);
                    SendData.add(acknowledge(getUseLogicalNameReferencing() ? Command.SetResponse : Command.WriteResponse, 0));
                    return SendData.get(FrameIndex);
                }
            }
            else if (command[0] == Command.ReadRequest.getValue() && !getUseLogicalNameReferencing())
            {
                ObjectType[] type = new ObjectType[]{ObjectType.NONE};
                int[] index = new int[1];
                Object[] parameter = new Object[1];
                getCommand(allData, type, name, index, parameter);
                int sn = ((Number)name[0]).shortValue() & 0xFFFF;
                for (Map.Entry<Integer, GXDLMSObject> it : SortedItems.entrySet())
                {
                    if (it.getKey() > sn)
                    {
                        break;
                    }
                    item = it.getValue();
                }                
                index[0] = ((sn - item.getShortName()) / 8) + 1;
                System.out.println(String.format("Reading %s, attribute index %d", item.getName(), index[0]));
                ValueEventArgs e = new ValueEventArgs(item, index[0]);
                read(e);
                if (e.getHandled())
                {                      
                    SendData.addAll(Arrays.asList(readReply(name[0], type[0], index[0], e.getValue(), e.getDataType())));
                    return SendData.get(FrameIndex);
                }                
                return getValue(name[0], item, index[0], parameter);
            }
            else if (command[0] == Command.GetRequest.getValue() && getUseLogicalNameReferencing())
            {
                ObjectType[] type = new ObjectType[]{ObjectType.NONE};
                int[] index = new int[1];
                Object[] parameter = new Object[1];
                getCommand(allData, type, name, index, parameter);
                System.out.println(String.format("Reading %s, attribute index %d", name, index[0]));
                item = privateItems.findByLN(type[0], name[0].toString());
                if (item != null)
                {
                    ValueEventArgs e = new ValueEventArgs(item, index[0]);
                    read(e);
                    if (e.getHandled())
                    {                        
                        SendData.addAll(Arrays.asList(readReply(name[0], type[0], index[0], e.getValue(), e.getDataType())));
                        return SendData.get(FrameIndex);
                    }
                    return getValue(name[0], item, index[0], parameter);
                }
            }           
            else if (command[0] == Command.MethodRequest.getValue())
            {
                ObjectType[] type = new ObjectType[1];
                int[] index = new int[1];
                Object[] parameter = new Object[1];
                getCommand(allData, type, name, index, parameter);
                if (parameter[0] != null)
                {         
                    DataType[] dtype = new DataType[]{DataType.NONE};
                    int[] read = new int[1], total = new int[1], index2 = new int[1];
                    int[] cache = new int[1];
                    parameter[0] = GXCommon.getData((byte[])parameter[0], index2, 
                            ActionType.NONE.getValue(), total, read, dtype, cache);
                }
                if (getUseLogicalNameReferencing())
                {                    
                    item = privateItems.findByLN(type[0], name[0].toString());
                }
                else
                {
                    int sn = (int)name[0];
                    for (Map.Entry<Integer, GXDLMSObject> it : SortedItems.entrySet())
                    {
                        if (it.getKey() > sn)
                        {
                            break;
                        }
                        item = it.getValue();
                    }
                    //If item is last item.
                    if (item == null)
                    {
                        item = (GXDLMSObject) SortedItems.values().toArray()[privateItems.size() - 1];
                    }
                    int[] value = new int[1], count = new int[1];
                    GXDLMS.getActionInfo(item.getObjectType(), value, count);
                    index[0] = ((sn - item.getShortName()) / value[0]);                    
                }
                if (item != null)
                {
                    System.out.println(String.format("Action on %s, attribute index %d", name[0], index[0]));
                    ValueEventArgs e = new ValueEventArgs(item, index[0]);
                    e.setValue(parameter[0]);
                    action(e);
                    if (!e.getHandled())
                    {
                        item.invoke(index[0], e.getValue());
                    }
                    SendData.add(acknowledge(Command.MethodResponse, 0));
                    return SendData.get(FrameIndex);                    
                }
            }
            //Return HW error.
            SendData.add(serverReportError(1, 5, 3));
            return SendData.get(FrameIndex);
        }
        catch (java.lang.Exception e)
        {
            //Return HW error.
            ReceivedData.reset();
            SendData.addAll(Arrays.asList(serverReportError(1, 5, 3)));
            return SendData.get(FrameIndex);
        }
    }
 
    private byte[] getValue(Object name, GXDLMSObject item, int index, Object[] parameters)
            throws RuntimeException, UnsupportedEncodingException, 
            ParseException, IOException
    {
        Object value;        
        IGXDLMSBase base = (IGXDLMSBase) item;
        if (base != null)
        {
            DataType[] tp = new DataType[]{DataType.NONE};
            byte[] params = null;
            if (parameters != null)
            {
                params = (byte[]) parameters[0];            
            }
            value = item.getValue(index, tp, params);
            if (tp[0] == DataType.NONE)
            {
                tp[0] = GXCommon.getValueType(value);
            }            
            if (tp[0] != DataType.NONE || (value == null && tp[0] == DataType.NONE))
            {
                SendData.addAll(Arrays.asList(readReply(name, item.getObjectType(), index, value, tp[0])));
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
        SendData.add(serverReportError(1, 5, 3));
        return SendData.get(FrameIndex);
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
        GXCommon.setData(data, type, value);        
        return m_Base.generateMessage(name, 0, data.toByteArray(), objectType, 
                attributeOrdinal, 
                this.getUseLogicalNameReferencing() ? Command.GetResponse : Command.ReadResponse);
    }

    /** 
     Generates a acknowledge message.
    */
    final byte[] acknowledge(Command cmd, int status)
    {
        java.nio.ByteBuffer data = java.nio.ByteBuffer.allocate(7);
        if (this.getInterfaceType() == InterfaceType.GENERAL)
        {
            data.put(GXCommon.LLCReplyBytes);
        }           
        //Get request normal
        data.put((byte) cmd.getValue());
        data.put((byte) 0x01);
        //Invoke ID and priority.
        data.put((byte) 0x81);
        data.put((byte) status);
        return m_Base.addFrame(m_Base.generateIFrame(), false, data, 0, data.position());
    }

    /** 
     Generates a acknowledge message.
    */
    public final byte[] serverReportError(int serviceErrorCode, int type, int code)
    {
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(10);
        if (this.getInterfaceType() == InterfaceType.GENERAL)
        {
            if (m_Base.getServer())
            {
                buff.put(GXCommon.LLCReplyBytes);
            }
            else
            {
                buff.put(GXCommon.LLCSendBytes);
            }
        }
        byte cmd;
        if (m_Base.getServer())
        {
            if (this.getUseLogicalNameReferencing())
            {
                cmd = (byte)Command.GetResponse.getValue();
            }
            else
            {
                cmd = (byte)Command.ReadResponse.getValue();
            }
        }
        else
        {
            if (this.getUseLogicalNameReferencing())
            {
                cmd = (byte)Command.GetRequest.getValue();
            }
            else
            {
                cmd = (byte)Command.ReadRequest.getValue();
            }
        }
        //Get request normal
        buff.put(cmd);
        if (this.getUseLogicalNameReferencing())
        {
            buff.put((byte) 0x01);                
        }
        buff.put((byte) serviceErrorCode);
        //Invoke ID and priority.
        buff.put((byte)type);
        buff.put((byte)code);
        return m_Base.addFrame(m_Base.generateIFrame(), false, buff, 0, buff.position());
    }
}