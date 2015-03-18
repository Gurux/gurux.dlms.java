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

import gurux.dlms.enums.Authentication;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.objects.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import static javax.management.Query.in;

/** 
 GXDLMS implements methods to communicate with DLMS/COSEM metering devices.
*/
class GXDLMS
{
    private int ValueOfQualityOfService;
    private int NumberOfUnusedBits;
    private int packetIndex;
    private boolean IsLastMsgKeepAliveMsg;
    int expectedFrame, frameSequence;
    //Cached data.
    Object cacheData;
    //Cached data type.
    DataType cacheType;
    //Index where last item found.
    int cacheIndex;
    //Cache Size
    int cacheSize;
    int itemCount;
    int maxItemCount;
    private int privateMaxReceivePDUSize;
    private boolean privateUseCache;
    private Object privateServerID;
    private Object privateClientID;
    private boolean privateGenerateFrame;
    private byte privateDLMSVersion;
    private boolean privateUseLogicalNameReferencing;
    private InterfaceType privateInterfaceType;
    private boolean privateServer;
    private GXDLMSLimits privateLimits;
    private GXDLMSLNSettings privateLNSettings;
    private GXDLMSSNSettings privateSNSettings;
    byte[] CtoSChallenge;
    byte[] StoCChallenge;
    GXCiphering Ciphering;
    int m_InvokeID;
    private Priority m_Priority;
    private ServiceClass m_ServiceClass;

    Authentication m_Authentication;
    
    public Authentication getAuthentication()
    {
        return m_Authentication;
    }

    public void setAuthentication(Authentication value)
    {
        m_Authentication = value;
    }

    /*
     * Generates challenge.
     */
    static public byte[] generateChallenge()
    {
        Random r = new Random();
        // Random challenge is 8 to 64 bytes.
        int len = r.nextInt(57) + 8;
        byte[] result = new byte[len];
        for (int pos = 0; pos != len; ++pos)
        {
            // Allow printable characters only.
            do
            {
                result[pos] = (byte)r.nextInt(0x7A);
            }
            while (result[pos] < 0x21);
        }
        return result;
    }

    public final byte getInvokeIDPriority()
    {
        byte value = 0;
        if (getPriority() == getPriority().HIGH)
        {
            value |= 0x80;
        }
        if (getServiceClass() == ServiceClass.CONFIRMED)
        {
            value |= 0x40;
        }
        value |= m_InvokeID;
        return value;
    }

    /** 
     Used priority.
    */
    public final Priority getPriority()
    {
        return m_Priority;
    }
    public final void setPriority(Priority value)
    {
        m_Priority = value;
    }

    /** 
     Used service class.
    */    
    public final ServiceClass getServiceClass()
    {
        return m_ServiceClass;
    }
    public final void setServiceClass(ServiceClass value)
    {
        m_ServiceClass = value;
    }

    /** 
     Invoke ID.
    */
    public final int getInvokeID()
    {
        return m_InvokeID;
    }
    public final void setInvokeID(int value)
    {
        if (value > 0xF)
        {
            throw new IllegalArgumentException("Invalid InvokeID");
        }
        m_InvokeID = value;
    }

    static GXDLMSObject createObject(ObjectType type)
    {
        if (type == ObjectType.ACTION_SCHEDULE)
        {
            return new GXDLMSActionSchedule();
        }        
        if (type == ObjectType.ACTIVITY_CALENDAR)
        {
            return new GXDLMSActivityCalendar();
        }
        if (type == ObjectType.ASSOCIATION_LOGICAL_NAME)
        {
            return new GXDLMSAssociationLogicalName();
        }
        if (type == ObjectType.ASSOCIATION_SHORT_NAME)
        {
            return new GXDLMSAssociationShortName();
        }
        if (type == ObjectType.AUTO_ANSWER)
        {
            return new GXDLMSAutoAnswer();
        }
        if (type == ObjectType.AUTO_CONNECT)
        {
            return new GXDLMSAutoConnect();
        }
        if (type == ObjectType.CLOCK)
        {
            return new GXDLMSClock();
        }
        if (type == ObjectType.DATA)
        {
            return new GXDLMSData();
        }
        if (type == ObjectType.DEMAND_REGISTER)
        {
            return new GXDLMSDemandRegister();
        }
        if (type == ObjectType.MAC_ADDRESS_SETUP)
        {
            return new GXDLMSMacAddressSetup();
        }        
        if (type == ObjectType.EVENT)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.EXTENDED_REGISTER)
        {
            return new GXDLMSExtendedRegister();
        }
        if (type == ObjectType.GPRS_SETUP)
        {
            return new GXDLMSGprsSetup();
        }
        if (type == ObjectType.IEC_HDLC_SETUP)
        {
            return new GXDLMSHdlcSetup();
        }
        if (type == ObjectType.IEC_LOCAL_PORT_SETUP)
        {
            return new GXDLMSIECOpticalPortSetup();
        }
        if (type == ObjectType.IEC_TWISTED_PAIR_SETUP)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.IP4_SETUP)
        {
            return new GXDLMSIp4Setup();
        }
        if (type == ObjectType.MBUS_SLAVE_PORT_SETUP)
        {
            return new GXDLMSMBusSlavePortSetup();
        }
        if (type == ObjectType.IMAGE_TRANSFER)
        {
            return new GXDLMSImageTransfer();
        }
        if (type == ObjectType.SECURITY_SETUP)
        {
            return new GXDLMSSecuritySetup();
        }        
        if (type == ObjectType.DISCONNECT_CONTROL)
        {
            return new GXDLMSDisconnectControl();
        }        
        if (type == ObjectType.LIMITER)
        {
            return new GXDLMSLimiter();
        }        
        if (type == ObjectType.MBUS_CLIENT)
        {
            return new GXDLMSMBusClient();
        }
        if (type == ObjectType.MODEM_CONFIGURATION)
        {
            return new GXDLMSModemConfiguration();
        }
        if (type == ObjectType.PPP_SETUP)
        {
            return new GXDLMSPppSetup();
        }
        if (type == ObjectType.PROFILE_GENERIC)
        {
            return new GXDLMSProfileGeneric();
        }        
        if (type == ObjectType.REGISTER)
        {
            return new GXDLMSRegister();
        }
        if (type == ObjectType.REGISTER_ACTIVATION)
        {
            return new GXDLMSRegisterActivation();
        }
        if (type == ObjectType.REGISTER_MONITOR)
        {
            return new GXDLMSRegisterMonitor();
        }
        if (type == ObjectType.REGISTER_TABLE)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.REMOTE_ANALOGUE_CONTROL)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.REMOTE_DIGITAL_CONTROL)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.SAP_ASSIGNMENT)
        {
            return new GXDLMSSapAssignment();
        }
        if (type == ObjectType.SCHEDULE)
        {
            return new GXDLMSSchedule();
        }
        if (type == ObjectType.SCRIPT_TABLE)
        {
            return new GXDLMSScriptTable();
        }
        if (type == ObjectType.SMTP_SETUP)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.SPECIAL_DAYS_TABLE)
        {
            return new GXDLMSSpecialDaysTable();
        }
        if (type == ObjectType.STATUS_MAPPING)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.TCP_UDP_SETUP)
        {
            return new GXDLMSTcpUdpSetup();
        }
        if (type == ObjectType.TUNNEL)
        {
            return new GXDLMSObject();
        }
        if (type == ObjectType.UTILITY_TABLES)
        {
            return new GXDLMSObject();
        }        
        if (type == ObjectType.MBUS_MASTER_PORT_SETUP)
        {
            return new GXDLMSMBusMasterPortSetup();
        }                
        if (type == ObjectType.PUSH_SETUP)
        {
            return new GXDLMSPushSetup();
        }        
        if (type == ObjectType.MESSAGE_HANDLER)
        {
            return new GXDLMSMessageHandler();
        }        
        return new GXDLMSObject();
    }

    /** 
     Constructor.
    */
    public GXDLMS(boolean server)
    {
        m_Priority = Priority.HIGH;
        m_InvokeID = 1;        
        try 
        {
            Ciphering = new GXCiphering("ABCDEFGH".getBytes("ASCII"));
        }
        catch (UnsupportedEncodingException ex) 
        {
            throw new RuntimeException(ex.getMessage());
        }
        m_Authentication = Authentication.NONE;
        IsLastMsgKeepAliveMsg = false;
        setServer(server);
        setUseCache(true);
        this.setInterfaceType(InterfaceType.GENERAL);
        setDLMSVersion((byte) 6);
        setMaxReceivePDUSize(0xFFFF);
        clearProgress();
        setGenerateFrame(true);
        setLimits(new GXDLMSLimits());
    }

    /** 
     Reserved for internal use.
    */
    protected final void clearProgress()
    {
        cacheIndex = cacheSize = itemCount = maxItemCount = 0;
        cacheData = null;
        cacheType = DataType.NONE;
    }

    /** 
     Client ID is the identification of the device that is used as a client.
     Client ID is aka HDLC Address.
    */
    public final Object getClientID()
    {
        return privateClientID;
    }
    public final void setClientID(Object value)
    {
        privateClientID = value;
    }

    /** 
     Server ID is the indentification of the device that is used as a server.
     Server ID is aka HDLC Address.
    */
    public final Object getServerID()
    {
        return privateServerID;
    }
    public final void setServerID(Object value)
    {
        privateServerID = value;
    }

    public final boolean getGenerateFrame()
    {
        return privateGenerateFrame;
    }
    public final void setGenerateFrame(boolean value)
    {
        privateGenerateFrame = value;
    }

    /** 
     Is cache used. Default value is True;
    */
    public final boolean getUseCache()
    {
        return privateUseCache;
    }
    public final void setUseCache(boolean value)
    {
        privateUseCache = value;
    }

    public final byte getDLMSVersion()
    {
        return privateDLMSVersion;
    }
    public final void setDLMSVersion(byte value)
    {
        privateDLMSVersion = value;
    }

    /** 
     Retrieves the maximum size of PDU receiver.

     PDU size tells maximum size of PDU packet.
     Value can be from 0 to 0xFFFF. By default the value is 0xFFFF.
    */
    public final int getMaxReceivePDUSize()
    {
        return privateMaxReceivePDUSize;
    }

    public final void setMaxReceivePDUSize(int value)
    {
        privateMaxReceivePDUSize = value;
    }

    /** 
     Determines, whether Logical, or Short name, referencing is used.     

     Referencing depends on the device to communicate with.
     Normally, a device supports only either Logical or Short name referencing.
     The referencing is defined by the device manufacturer.
     If the referencing is wrong, the SNMR message will fail.
    */
    public final boolean getUseLogicalNameReferencing()
    {
        return privateUseLogicalNameReferencing;
    }
    public final void setUseLogicalNameReferencing(boolean value)
    {
        privateUseLogicalNameReferencing = value;
    }

    /** 
     Reserved for internal use.
    */
    final byte[][] generateMessage(Object name, byte[] data, ObjectType interfaceClass, int AttributeOrdinal, Command cmd)
    {
        if (getLimits().getMaxInfoRX() == null)
        {
            throw new GXDLMSException("Invalid arguement.");
        }
        boolean asList = false;
        java.nio.ByteBuffer buff;
        if (name instanceof byte[])
        {
            buff = java.nio.ByteBuffer.wrap((byte[])name);
        }
        else
        {
            if (name == null)
            {
                buff = java.nio.ByteBuffer.wrap(data);
            }
            else if (getUseLogicalNameReferencing())
            {
                //Interface class.
                int len = data == null ? 0 : data.length;                
                if (cmd == Command.GetRequest || cmd == Command.SetRequest || cmd == Command.MethodRequest)
                {
                    if (name instanceof List)
                    {                        
                        asList = true;
                        List<AbstractMap.SimpleEntry<GXDLMSObject, Integer>> tmp = 
                                (List<AbstractMap.SimpleEntry<GXDLMSObject, Integer>>)name;
                        buff = java.nio.ByteBuffer.allocate(20 + len + tmp.size() * 10);
                        //Item count
                        buff.put((byte)tmp.size());
                        for(AbstractMap.SimpleEntry<GXDLMSObject, Integer> it : tmp)
                        {
                            //Interface class.
                            GXCommon.setUInt16((short) it.getKey().getObjectType().getValue(), buff);
                            //Add LN
                            String[] items = it.getKey().getLogicalName().split("[.]");
                            if (items.length != 6)
                            {
                                throw new GXDLMSException("Invalid Logical Name.");
                            }
                            for(String it2 : items)
                            {
                                buff.put(Integer.valueOf(it2).byteValue());
                            }
                            buff.put(it.getValue().byteValue());
                            //Add Access type.
                            buff.put((byte) 0);
                        }
                    }
                    else
                    {
                        buff = java.nio.ByteBuffer.allocate(20 + len);
                        buff.putShort((short) interfaceClass.getValue());
                        //Add LN
                        String[] items = ((String)name).split("[.]", -1);
                        if (items.length != 6)
                        {
                            throw new GXDLMSException("Invalid Logical Name.");
                        }
                        for (String it : items)
                        {
                            buff.put((byte) (Short.parseShort(it) & 0xFF));
                        }
                        buff.put((byte) AttributeOrdinal);
                        //Items count
                        if (data == null || data.length == 0 || cmd == Command.SetRequest)
                        {
                            buff.put((byte)0);                     
                        }
                        else
                        {
                            buff.put((byte)1); //Items count                    
                        }
                    }
                }
                else
                {
                    buff = java.nio.ByteBuffer.allocate(20 + len);
                }
            }
            else
            {
                int len = data == null ? 0 : data.length;                
                if (name instanceof List)
                {                    
                    List<AbstractMap.SimpleEntry<GXDLMSObject, Integer>> tmp = 
                                (List<AbstractMap.SimpleEntry<GXDLMSObject, Integer>>)name;
                    buff = java.nio.ByteBuffer.allocate(10 + tmp.size() * 3);
                    //Item count
                    buff.put((byte) tmp.size());
                    for(AbstractMap.SimpleEntry<GXDLMSObject, Integer> it : tmp)
                    {
                        //Size
                        buff.put((byte)2);
                        int base_address = it.getKey().getShortName();
                        base_address += ((it.getValue() - 1) * 8);
                        GXCommon.setUInt16((short) base_address, buff);
                    }
                }
                else
                {
                    buff = java.nio.ByteBuffer.allocate(11 + len);
                    buff.put((byte) 1);                
                    if (cmd == Command.ReadResponse || cmd == Command.WriteResponse)
                    {
                        buff.put((byte) 0x0);
                    }
                    else
                    {
                        if (data == null || data.length == 0)
                        {
                            buff.put((byte) 2);
                        }
                        else //if Parameterised Access
                        {
                            buff.put((byte) 4);
                        }  
                        int base_address = GXCommon.intValue(name);
                        //AttributeOrdinal is count already for action.
                        if (AttributeOrdinal != 0)
                        {
                            base_address += ((AttributeOrdinal - 1) * 8);
                        }
                        buff.putShort((short) base_address);
                    }
                }
            }
            if (data != null && data.length != 0)
            {
                buff.put(data);
            }
        }
        return splitToBlocks(buff, cmd, asList);
    }

    /** 
     Is operated as server or client.
    */
    public final boolean getServer()
    {
        return privateServer;
    }
    public final void setServer(boolean value)
    {
        privateServer = value;
    }

    /** 
     Information from the connection size that server can handle.
    */
    public final GXDLMSLimits getLimits()
    {
        return privateLimits;
    }

    public final void setLimits(GXDLMSLimits value)
    {
        privateLimits = value;
    }

    /** 
     Removes the HDLC header from the packet, and returns COSEM data only.

     @param packet The received packet, from the device, as byte array.
     @param data The exported data.
     @return COSEM data.
    */
    public final java.util.Set<RequestTypes> getDataFromPacket(byte[] packet, 
            ByteArrayOutputStream data, byte[] frame, int[] command)
    {
        if (packet == null || packet.length == 0)
        {
            throw new IllegalArgumentException("Packet is invalid.");
        }
        int[] error = new int[1];         
        boolean[] packetFull = new boolean[1], wrongCrc = new boolean[1];
        java.util.Set<RequestTypes> moreData = getDataFromFrame(java.nio.ByteBuffer.wrap(packet), data, frame, true, error, false, packetFull, wrongCrc, command);
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        if (command[0] == Command.REJECTED.getValue())
        {
            throw new GXDLMSException("Packet rejected.");
        }
        if (moreData.isEmpty() && Ciphering.getSecurity() != Security.NONE &&
            (command[0] == Command.GloGetResponse.getValue() ||
            command[0] == Command.GloSetResponse.getValue() ||
            command[0] == Command.GloMethodResponse.getValue() || 
            command[0] == Command.None.getValue()) &&
            data.size() != 0)
        {
            Command cmd[] = new Command[1];
            byte[] tmp = decrypt(data.toByteArray(), cmd);
            data.reset();
            try 
            {
                data.write(tmp);
            }
            catch (IOException ex) 
            {
                throw new RuntimeException(ex.getMessage());
            }
            command[0] = (byte)cmd[0].getValue();
        }
        return moreData;
    }

    /** 
     Generates an acknowledgment message, with which the server is informed to 
     send next packets.

     @param type Frame type
     @return Acknowledgment message as byte array.
    */
    public final byte[] receiverReady(RequestTypes type)
    {
        if (!getUseLogicalNameReferencing() || type == RequestTypes.FRAME)
        {
            return addFrame(generateSupervisoryFrame((byte)0), false, (byte[])null, 0, 0);
        }
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(10);
        if (this.getInterfaceType() == InterfaceType.GENERAL)
        {
            if (getServer())
            {
                buff.put(GXCommon.LLCReplyBytes);
            }
            else
            {
                buff.put(GXCommon.LLCSendBytes);
            }
        }
        //Get request normal
        buff.put((byte) 0xC0);
        buff.put((byte) 0x02);
        //Invoke ID and priority.
        buff.put(getInvokeIDPriority());
        buff.putInt(packetIndex);
        return addFrame(generateIFrame(), false, buff, 0, buff.position());
    }

    /** 
     Determines, whether the DLMS packet is completed.

     @param data The data to be parsed, to complete the DLMS packet.
     @return True, when the DLMS packet is completed.
    */
    public final boolean isDLMSPacketComplete(byte[] data)
    {
        if (data == null)
        {
            return false;
        }
        byte[] frame = new byte[1];
        int[] error = new int[1];
        try
        {
            if (getInterfaceType() == InterfaceType.GENERAL)
            {
                if (data.length < 5)
                {
                    return false;
                }
                boolean compleate = false;
                //Find start of HDLC frame.
                for (int index = 0; index < data.length; ++index)
                {
                    if (data[index] == GXCommon.HDLCFrameStartEnd)
                    {
                        if (2 + data[index + 2] <= data.length)
                        {
                            compleate = true;                                
                        }
                        break;
                    }
                }
                if (!compleate)
                {
                    return false;
                }
            }
            else if (getInterfaceType() == InterfaceType.NET)
            {
                if (data.length < 6)
                {
                    return false;
                }
            }
            else
            {
                throw new Exception("Unknown interface type.");
            }
            boolean[] packetFull = new boolean[1], wrongCrc = new boolean[1];
            int[] command = new int[1];
            getDataFromFrame(java.nio.ByteBuffer.wrap(data), null, frame, true, 
                    error, false, packetFull, wrongCrc, command);
            return packetFull[0];
        }
        catch (java.lang.Exception ex)
        {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /** 
     Reserved for internal use.
    */
    public final void parseReplyData(ActionType action, byte[] buff, 
            Object[] value, DataType[] type)
    {
        type[0] = DataType.NONE;
        if (!getUseCache())
        {
            clearProgress();
        }
        int[] read = new int[1], total = new int[1], index = new int[1];
        int[] cache = new int[]{cacheIndex};
        value[0] = GXCommon.getData(buff, index, action.getValue(), total, read, type, cache);
        cacheIndex = cache[0];
        if (getUseCache())
        {
            cacheSize = buff.length;
            //If array.
            if (cacheData != null && cacheData.getClass().isArray())
            {
                if (value[0] != null)
                {
                    Object oldData = cacheData;
                    if (value.getClass().isArray())
                    {
                        Object newData = value[0];
                        Object[] data = new Object[Array.getLength(oldData) + Array.getLength(newData)];
                        System.arraycopy((Object[]) oldData, 0, data, 0, Array.getLength(oldData));
                        System.arraycopy((Object[]) newData, 0, data, Array.getLength(oldData), Array.getLength(newData));
                        cacheData = data;
                    }
                    else
                    {
                        Object[] data = new Object[Array.getLength(oldData) + 1];
                        System.arraycopy((Object[]) oldData, 0, data, 0, Array.getLength(oldData));
                        data[Array.getLength(oldData)] = value;
                        cacheData = data;
                    }
                }
            }
            else
            {
                cacheData = value[0];
                cacheType = type[0];
            }
        }
        itemCount += read[0];
        maxItemCount = total[0];
    }
    
    /*
     * Is cache used.
     */
    boolean useCache(byte[] data)
    {
        return getUseCache() && data.length == cacheSize;
    }

    /** 
     This method is used to solve current index of received DLMS packet, 
     by retrieving the current progress status.

     @param data DLMS data to parse.
     @return The current index of the packet.
    */
    public final int getCurrentProgressStatus(byte[] data)
    {
        try
        {
            //Return cached size.
            if (useCache(data))
            {
                return itemCount;
            }
            Object[] value = new Object[1];
            DataType type[] = new DataType[]{DataType.NONE};
            parseReplyData(getUseCache() ? ActionType.INDEX : ActionType.INDEX,
                    data, value, type);
            return itemCount;
        }        
        catch (RuntimeException ex)
        {
            System.out.println(ex.getMessage());
            return itemCount;
        }
        catch (java.lang.Exception ex)
        {
            System.out.println(ex.getMessage());
            return itemCount;
        }
    }

    /** 
     This method is used to solve the total amount of received items,
     by retrieving the maximum progress status.

     @param data DLMS data to parse.
     @return Total amount of received items.
    */
    public final int getMaxProgressStatus(byte[] data)
    {
        //Return cached size.
        if (useCache(data))
        {
            return maxItemCount;
        }
        if (!getUseCache())
        {
            clearProgress();
        }
        try
        {
            Object[] value = new Object[1];
            DataType type[] = new DataType[]{DataType.NONE};
            parseReplyData(getUseCache() ? ActionType.INDEX : ActionType.COUNT, data, value, type);
        }
        catch(RuntimeException ex)
        {
            System.out.println(ex.getMessage());
            return 1;
        }        
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return 1;
        }                
        return maxItemCount;
    }      

    /** 
     Checks, whether the received packet is a reply to the sent packet.

     @param sendData The sent data as a byte array. 
     @param receivedData The received data as a byte array.
     @return True, if the received packet is a reply to the sent packet. False, if not.
    */
    public final boolean isReplyPacket(byte[] sendData, byte[] receivedData) throws Exception
    {
        byte[] sendID = new byte[1];
        byte[] receiveID = new byte[1];
        int[] error = new int[1];
        boolean[] packetFull = new boolean[1];
        boolean[] wrongCrc = new boolean[1];
        int[] command = new int[1];
        getDataFromFrame(java.nio.ByteBuffer.wrap(sendData), null, sendID, 
                false, error, false, packetFull, wrongCrc, command);
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        getDataFromFrame(java.nio.ByteBuffer.wrap(receivedData), null, 
                receiveID, true, error, true, packetFull, wrongCrc, command);
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        if (command[0] == Command.REJECTED.getValue())
        {
            throw new GXDLMSException("Frame rejected.");
        }
        int sid = (sendID[0] & 0xFF);
        int rid = (receiveID[0] & 0xFF);
        boolean ret = rid == FrameType.Rejected.getValue() || 
                (sid == FrameType.Disconnect.getValue() && rid == FrameType.UA.getValue()) || 
                isExpectedFrame(sid, rid);
        return ret;
    }

    /** 
     Determines the type of the connection


     All DLMS meters do not support the IEC 62056-47 standard.  
     If the device does not support the standard, and the connection is made 
     using TCP/IP, set the type to InterfaceType.General.

    */
    public final InterfaceType getInterfaceType()
    {
        return privateInterfaceType;
    }
    public final void setInterfaceType(InterfaceType value)
    {
        privateInterfaceType = value;
    }   

    public byte[][] splitToFrames(java.nio.ByteBuffer packet, int blockIndex, 
                        int[] index, int count, Command cmd, int resultChoice,
                        boolean asList)
    {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        if (this.getInterfaceType() == InterfaceType.GENERAL  &&
           !(Ciphering.getSecurity() != Security.NONE && frameSequence != -1))
        {
            try
            {
                if (getServer())
                {
                    tmp.write(GXCommon.LLCReplyBytes);
                }
                else
                {
                    tmp.write(GXCommon.LLCSendBytes);
                }
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
        }    
        if (cmd != Command.None && this.getUseLogicalNameReferencing())
        {
            boolean moreBlocks = packet.limit() > getMaxReceivePDUSize() && packet.limit() > index[0] + count;
            //Command, multiple blocks and Invoke ID and priority.
            tmp.write(cmd.getValue());
            if (asList){
                tmp.write((byte)3);
            }
            else{
                tmp.write((byte)(moreBlocks ? 2 : 1));
            }
            tmp.write(getInvokeIDPriority());
            if (getServer())
            {
                tmp.write(resultChoice); // Get-Data-Result choice data
            }
            if (moreBlocks)
            {
                tmp.write((byte) blockIndex >> 8);
                tmp.write((byte) blockIndex & 0xFF);
                tmp.write((byte) 0);
                GXCommon.setObjectCount(count, tmp);
            }
        }
        else if (cmd != Command.None && !this.getUseLogicalNameReferencing())
        {
            tmp.write((byte) cmd.getValue());
        }
        //Crypt message in first run.
        if (Ciphering.getSecurity() != Security.NONE && frameSequence != -1)
        {
            try
            {
                int cnt = count;
                if (count + index[0] > packet.limit())
                {
                    cnt = packet.limit() - index[0];
                }
                tmp.write(packet.array(), index[0], cnt);
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }            
            packet.clear();
            Ciphering.setFrameCounter(Ciphering.getFrameCounter() + 1);
            Command gloCmd;
            if (cmd == Command.ReadRequest || cmd == Command.GetRequest)
            {
                gloCmd = Command.GloGetRequest;
            }
            else if (cmd == Command.WriteRequest || cmd == Command.SetRequest)
            {
                gloCmd = Command.GloSetRequest;
            }
            else if (cmd == Command.MethodRequest)
            {
                gloCmd = Command.GloMethodRequest;
            }
            else if (cmd == Command.ReadResponse || cmd == Command.GetResponse)
            {
                gloCmd = Command.GloGetResponse;
            }
            else if (cmd == Command.WriteResponse || cmd == Command.SetResponse)
            {
                gloCmd = Command.GloSetResponse;
            }
            else if (cmd == Command.MethodResponse)
            {
                gloCmd = Command.GloMethodResponse;
            }
            else
            {
                throw new GXDLMSException("Invalid GLO command.");
            }
            byte[] tmp2 = tmp.toByteArray();
            packet = java.nio.ByteBuffer.wrap(GXDLMSChippering.EncryptAesGcm(gloCmd, Ciphering.getSecurity(), 
                Ciphering.getFrameCounter(), Ciphering.getSystemTitle(), Ciphering.getBlockCipherKey(), 
                Ciphering.getAuthenticationKey(), tmp2));            
            packet.position(packet.limit());
            count = packet.position();
            tmp.reset();
            try
            {
                if (this.getInterfaceType() == InterfaceType.GENERAL)
                {
                    if (getServer())
                    {
                        tmp.write(GXCommon.LLCReplyBytes);
                    }
                    else
                    {
                        tmp.write(GXCommon.LLCSendBytes);
                    }
                }
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }           
        }                             
        int dataSize;
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            dataSize = getMaxReceivePDUSize();
        }
        else
        {
            if (cmd == Command.GetRequest || cmd == Command.MethodRequest || 
                cmd == Command.ReadRequest || cmd == Command.SetRequest || 
                cmd == Command.WriteRequest)
            {
                dataSize = GXCommon.intValue(getLimits().getMaxInfoTX());
            }
            else
            {
                dataSize = GXCommon.intValue(getLimits().getMaxInfoRX());
            }
        }
        if (count + index[0] > packet.limit())
        {
            count = packet.limit() - index[0];
        }
        packet.position(index[0]);
        byte[] tmp2 = new byte[count];
        packet.get(tmp2);
        try
        {
            tmp.write(tmp2);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }          
        index[0] += count;
        count = tmp.size();
        if (count < dataSize)
        {
            dataSize = count;
        }
        int cnt = (int)(count / dataSize);
        if (count % dataSize != 0)
        {
            ++cnt;
        }
        int start = 0;
        byte[][] buff = new byte[cnt][];
        for (int pos = 0; pos < cnt; ++pos)
        {
            byte id;
            if (pos == 0)
            {
                id = generateIFrame();
            }
            else
            {
                id = generateNextFrame();
            }
            if (start + dataSize > count)
            {
                dataSize = count - start;
            }            
            buff[pos] = addFrame(id, cnt != 1 && pos < cnt - 1, tmp.toByteArray(), start, dataSize);
            start += dataSize;
        }
        return buff;
    }

    /** 
     Split the send packet to a size that the device can handle.

     @param packet Packet to send.
     @return Array of byte arrays that are sent to device.
    */
    public final byte[][] splitToBlocks(java.nio.ByteBuffer packet, Command cmd, boolean asList)
    {
        int[] index = new int[1];
        int len = packet.position();
        packet.limit(len);
        packet.position(0);
        if (!getUseLogicalNameReferencing()) //SN
        {            
            return splitToFrames(packet, 0, index, len, cmd, 0, asList);
        }
        //If LN
        //Split to Blocks.
        java.util.ArrayList<byte[]> buff = new java.util.ArrayList<byte[]>();
        int blockIndex = 0;
        boolean multibleFrames = false;
        do
        {
            byte[][] frames = splitToFrames(packet, ++blockIndex, index, 
                    getMaxReceivePDUSize(), cmd, 0, asList);
            buff.addAll(Arrays.asList(frames));
            if (frames.length != 1)
            {
                multibleFrames = true;
                expectedFrame += 3;
            }
        }
        while (index[0] < len);
        if (multibleFrames)
        {
            expectedFrame -= 3;
        }
        byte[][] tmp = new byte[buff.size()][];
        int pos = -1;
        for(byte[] it : buff)
        {
            ++pos;
            tmp[pos] = new byte[it.length];
            System.arraycopy(it, 0, tmp[pos], 0, it.length);
        }
        return tmp;
    }

    static String getDescription(int errCode)
    {
        String str;
        switch (errCode)
        {
            case -1:
                str = "Not a reply";
                break;
            case 1:
                str = "Access Error : Device reports a hardware fault.";
                break;
            case 2:
                str = "Access Error : Device reports a temporary failure.";
                break;
            case 3:
                str = "Access Error : Device reports Read-Write denied.";
                break;
            case 4:
                str = "Access Error : Device reports a undefined object.";
                break;
            case 9: 
                str = "Access Error : Device reports a inconsistent Class or object.";
                break;
            case 11: 
                str = "Access Error : Device reports a unavailable object.";
                break;
            case 12: 
                str = "Access Error : Device reports a unmatched type.";
                break;
            case 13:
                str = "Access Error : Device reports scope of access violated.";
                break;
            case 14:   
                str = "Access Error : Data Block Unavailable.";
                break;
            case 15: 
                str = "Access Error : Long Get Or Read Aborted.";
                break;
            case 16: 
                str = "Access Error : No Long Get Or Read In Progress.";
                break;
            case 17: 
                str = "Access Error : Long Set Or Write Aborted.";
                break;
            case 18: 
                str = "Access Error : No Long Set Or Write In Progress.";
                break;
            case 19: 
                str = "Access Error : Data Block Number Invalid.";
                break;
            case 250: 
                str = "Access Error : Other Reason.";
                break;
            default:
                str = "Access Error : Unknown error.";
            break;
        }
        return str;
    }
    
    /** 
     Checks, whether there are any errors on received packet.

     @param sendData Sent data. 
     @param receivedData Received data. 
     @return True, if there are any errors on received data.
    */
    public final Object[][] checkReplyErrors(byte[] sendData, byte[] receivedData) throws Exception
    {
        boolean ret = true;
        if (sendData != null)
        {
            ret = isReplyPacket(sendData, receivedData);
        }
        //If packet is not reply for send packet...
        if (!ret)
        {
            Object[][] list = new Object[1][2];
            list[0][0] = -1;
            list[0][1] = "Not a reply.";
            return list;
        }

        //If we are checking UA or AARE messages.
        if (getLNSettings() == null && getSNSettings() == null) //TODO:
        {
            return null;
        }

        int[] err = new int[1];
        byte[] frame = new byte[1];
        boolean[] packetFull = new boolean[1], wrongCrc = new boolean[1];
        int[] command = new int[1];
        if (sendData != null)
        {            
            getDataFromFrame(java.nio.ByteBuffer.wrap(sendData), null, frame, 
                    false, err, false, packetFull, wrongCrc, command);
            if (!packetFull[0])
            {
                throw new GXDLMSException("Not enought data to parse frame.");
            }
            if (wrongCrc[0])
            {
                throw new GXDLMSException("Wrong Checksum.");
            }
            if (isReceiverReadyRequest(frame[0]) || frame[0] == FrameType.Disconnect.getValue())
            {
                return null;
            }
        }
        command[0] = 0;
        getDataFromFrame(java.nio.ByteBuffer.wrap(receivedData), null, frame, 
                true, err, false, packetFull, wrongCrc, command);
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        if (command[0] == Command.REJECTED.getValue())
        {
            throw new GXDLMSException("Frame rejected.");
        }
        if (err[0] != 0x00)
        {
            Object[][] list = new Object[1][2];
            list[0][0] = err[0] & 0xFF;
            list[0][1] = getDescription(err[0] & 0xFF);
            return list;
        }
        return null;
    }

    /** 
     Gets Logical Name settings, read from the device. 
    */
    public final GXDLMSLNSettings getLNSettings()
    {
        return privateLNSettings;
    }
    public final void setLNSettings(GXDLMSLNSettings value)
    {
        privateLNSettings = value;
    }

    /** 
     Gets Short Name settings, read from the device.
    */
    public final GXDLMSSNSettings getSNSettings()
    {
        return privateSNSettings;
    }
    public final void setSNSettings(GXDLMSSNSettings value)
    {
        privateSNSettings = value;
    }

    /** 
     Quality Of Service is an analysis of nonfunctional aspects of the software properties.

     @return 
    */    
    final int getValueOfQualityOfService()
    {
        return ValueOfQualityOfService;
    }
    void setValueOfQualityOfService(int value)
    {
        ValueOfQualityOfService = value;
    }

    /** 
     Retrieves the amount of unused bits.

     @return 
    */
    final int getNumberOfUnusedBits()
    {
        return NumberOfUnusedBits;
    }
    void setNumberOfUnusedBits(int value)
    {
        NumberOfUnusedBits = value;
    }

    /** 
     Generate I-frame: Information frame. Reserved for internal use.
    */
    byte generateIFrame()
    {
        //Expected frame number is increased only when first keep alive msg is send...
        if (!IsLastMsgKeepAliveMsg)
        {
            ++expectedFrame;
            expectedFrame = (expectedFrame & 0x7);
        }
        return generateNextFrame();
    }

    /** 
     Generate ACK message. Reserved for internal use.

     @return 
    */
    private byte generateNextFrame()
    {
        ++frameSequence;
        frameSequence = (frameSequence & 0x7);
        byte val = (byte)(((frameSequence & 0x7) << 1) & 0xF);
        val |= (byte)(((((expectedFrame & 0x7) << 1) | 0x1) & 0xF) << 4);
        IsLastMsgKeepAliveMsg = false;
        return val;
    }

    /** 
     Generates Keep Alive frame for keep alive message. Reserved for internal use.

     @return 
    */
    byte generateAliveFrame()
    {
        //Expected frame number is increased only when first keep alive msg is send...
        if (!IsLastMsgKeepAliveMsg && !getServer())
        {
            ++expectedFrame;
            expectedFrame = (expectedFrame & 0x7);
            IsLastMsgKeepAliveMsg = true;
        }
        byte val = 1;
        val |= (byte)((((((expectedFrame) & 0x7) << 1) | 0x1) & 0xF) << 4);
        return val;
    }

    /** 
     Return true if frame sequences are same. Reserved for internal use.

    */
    private boolean isExpectedFrame(int send, int received)
    {
        //In keep alive msg send ID might be same as receiver ID.
        //If echo.
        boolean ret = send == received || 
                ((send >> 5) & 0x7) == ((received >> 1) & 0x7) || 
                ((send & 0x1) == 0x1 || (received & 0x1) == 1);
        //If U-Frame...
        if (!ret)
        {
            return ret;
        }
        return ret;
    }

    /** 
     Generate I-frame: Information frame. Reserved for internal use. 

     0 Receive Ready (denoted RR(R)) is a positive acknowledge ACK of all frames
     up to and including frame number R-1.
     1 Reject (denoted RE.J(R)) is a negative acknowledge NAK
     of a Go-back-N mechanism. ie start retransmitting from frame number R.
     2 Receive Not Ready (denoted RNR(R)) is a positive acknowledge of all
     frames up to and including R-1 but the sender must pause until a
     Receive Ready arrives. This can be used to pause the sender because of
     temporary problems at the receiver.
     3 Selective Reject (denoted SREJ(R)) is a negative acknowledge in a
     Selective Repeat mechanism. ie resend only frame R. It is not
     supported in several implementations.

     @param type
     @return 
    */
    private byte generateSupervisoryFrame(byte type)
    {
        ++expectedFrame;
        expectedFrame = (expectedFrame & 0x7);
        byte val = (byte)((((type & 0x3) << 2) | 0x1) & 0xF);
        val |= (byte)(((((expectedFrame & 0x7) << 1) | 0x1) & 0xF) << 4);
        IsLastMsgKeepAliveMsg = false;
        return val;
    }

    /** 
     Reserved for internal use.

     @param val
     @return 
    */
    private boolean isReceiverReadyRequest(byte val)
    {
        boolean b = (val & 0xF) == 1 && (val >>> 4) == (((expectedFrame & 0x7) << 1) | 0x1);
        return b;
    }

    /** 
     Reserved for internal use.
    */
    public final void checkInit()
    {
        if (this.getClientID() == null)
        {
            throw new GXDLMSException("Invalid Client ID");
        }
        if (this.getServerID() == null)
        {
            throw new GXDLMSException("Invalid Server ID");
        }
    }

    /** 
     Reserved for internal use.

     @param address
     @return 
    */
    private byte[] getAddress(Object address)
    {
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            java.nio.ByteBuffer b = java.nio.ByteBuffer.allocate(2);
            b.putShort(((Number)address).shortValue());
            return b.array();
        }
        if (address instanceof Byte)
        {
            return new byte[] {((Number)address).byteValue()};
        }   
        java.nio.ByteBuffer b;
        if (address instanceof Short)
        {
            b = java.nio.ByteBuffer.allocate(2);
            b.putShort(((Number)address).shortValue());
            return b.array();
        }
        if (address instanceof Integer)
        {
            b = java.nio.ByteBuffer.allocate(4);
            b.putInt(((Number)address).intValue());
            return b.array();
        }
        if (getServer())
        {
            return new byte[0];
        }
        throw new GXDLMSException("Invalid Server Address.");
    }

    /** 
     Reserved for internal use.
    */    
    final byte[] addFrame(byte Type, boolean moreFrames, java.nio.ByteBuffer buff, int index, int count)
    {
        byte[] data = null;
        if (buff != null)
        {
            data = new byte[count - index];
            buff.position(index);
            buff.get(data);
        }         
        return addFrame(Type, moreFrames, data, index, count);
    }
    /** 
     Reserved for internal use.
    */
    final byte[] addFrame(byte Type, boolean moreFrames, byte[] data, int index, int count)
    {
        count = (count & 0xFFFF);
        checkInit();
        byte[] ServerBuff = getAddress(this.getServerID());
        byte[] ClientBuff = getAddress(this.getClientID());
        //Set packet size. BOP + Data size + dest and source size + EOP.
        int len = 7 + ServerBuff.length + ClientBuff.length;
        //If data is added. CRC is count for HDLC frame.
        if ((count) > 0)
        {
            len += count + 2;
        }
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(len);
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            //Add version 0x0001
            buff.put((byte) 0x00);
            buff.put((byte) 0x01);
            if (getServer())
            {
                //Add Destination (Server)
                buff.put(ServerBuff);
                //Add Source (Client)
                buff.put(ClientBuff);
            }
            else
            {
                //Add Source (Client)
                buff.put(ClientBuff);
                //Add Destination (Server)
                buff.put(ServerBuff);
            }
            //Add data length. 2 bytes.
            buff.putShort((short)count);
            if (data != null)
            {
                buff.put(data, 0, count);
            }
        }
        else
        {
            if (this.getGenerateFrame())
            {
                //HDLC frame opening flag.
                buff.put(GXCommon.HDLCFrameStartEnd);
            }
            //Frame type
            buff.put((byte)(moreFrames ? 0xA8 : 0xA0));
            //Length of msg.
            buff.put((byte)(len - 2));
            if (this.getServer())
            {
                //Client address
                buff.put(ClientBuff);
                //Server address
                buff.put(ServerBuff);
            }
            else
            {
                //Server address
                buff.put(ServerBuff);
                //Client address
                buff.put(ClientBuff);
            }
            //Add DLMS frame type
            buff.put(Type);
            //Count CRC for header.
            int crc;
            if (count > 0)
            {
                int start = 0;
                int cnt = buff.position();
                if (this.getGenerateFrame())
                {
                    --cnt;
                    start = 1;
                }
                crc = GXFCS16.countFCS16(buff, start, cnt);
                buff.putShort((short) crc);
                if (data != null)
                {
                    //If all data is added.
                    if (count == data.length)
                    {
                        buff.put(data);
                    }
                    else
                    {
                        byte[] tmp = new byte[count];
                        System.arraycopy(data, index, tmp, 0, count);
                        buff.put(tmp);
                    }
                }
            }
            //If framework is not generating CRC and EOP.
            if (this.getGenerateFrame())
            {
                //Count CRC for HDLC frame.
                crc = GXFCS16.countFCS16(buff, 1, buff.position() - 1);
                buff.putShort((short) crc);
                //EOP
                buff.put(GXCommon.HDLCFrameStartEnd);
            }
        }
        len = buff.position();
        byte tmp[] = new byte[len];
        buff.position(0);
        buff.get(tmp, 0, len);
        return tmp;
    }

    /** 
     Check LLC bytes. Reserved for internal use.
    */
    private boolean checkLLCBytes(java.nio.ByteBuffer buff)
    {
        if (getInterfaceType() == InterfaceType.GENERAL)
        {
            if (getServer())
            {
                return GXCommon.compare(buff, GXCommon.LLCSendBytes);
            }
            return GXCommon.compare(buff, GXCommon.LLCReplyBytes);
        }
        return false;
    }

    /** 
     Reserved for internal use.
    */
    private long getAddress(java.nio.ByteBuffer buff, int[] index, int size)
    {
        if (size == 1)
        {
            return buff.get(index[0]) & 0xFF;
        }
        if (size == 2)
        {
            return buff.getShort(index[0]);
        }
        if (size == 4)
        {
            return buff.getInt(index[0]);
        }
        throw new GXDLMSException("Invalid address size.");
    }

    /** 
     Reserved for internal use.
    */
    public final java.util.Set<RequestTypes> getDataFromFrame(java.nio.ByteBuffer buff, 
            ByteArrayOutputStream data, byte[] frame, boolean bReply, 
            int[] pError, boolean skipLLC, boolean[] packetFull, 
            boolean[] wrongCrc, int[] command)
    {
        command[0] = 0;
        wrongCrc[0] = false;
        packetFull[0] = true;
        frame[0] = 0;
        pError[0] = 0;
        int DataLen;
        java.util.Set<RequestTypes> MoreData = EnumSet.noneOf(RequestTypes.class);
        int PacketStartID = 0, len = buff.limit();
        int FrameLen = 0;
        //If DLMS frame is generated.
        if (getInterfaceType() != InterfaceType.NET)
        {
            if (len < 5)
            {
                packetFull[0] = false;
                return MoreData;
            }
            //Find start of HDLC frame.
            while(buff.position() < buff.limit())
            {
                if (buff.get() == GXCommon.HDLCFrameStartEnd)
                {
                    PacketStartID = buff.position() - 1;
                    break;
                }
            }
            if (buff.position() == len) //Not a HDLC frame.
            {
                throw new GXDLMSException("Invalid data format.");
            }
            frame[0] = buff.get();
            
            //Is there more data available.
            if ((frame[0] & 0x8) != 0)
            {
                MoreData = EnumSet.of(RequestTypes.FRAME);
            }
            //Check frame length.
            if ((frame[0] & 0x7) != 0)
            {
                FrameLen = (((frame[0] & 0x7) << 8) & 0xFF);
            }
            //If not enought data.
            FrameLen += (buff.get() & 0xFF);
            if (len < FrameLen + buff.position() - 1)
            {
                packetFull[0] = false;
                MoreData.clear();
                return MoreData;
            }
            if (MoreData.isEmpty() && 
                    buff.get(FrameLen + PacketStartID + 1) != GXCommon.HDLCFrameStartEnd)                
            {
                throw new GXDLMSException("Invalid data format.");
            }
        }
        else
        {
            //Get version
            int ver = (buff.getShort() & 0xFFFF);
            if (ver != 1)
            {
                throw new GXDLMSException("Unknown version.");
            }
        }
        byte[] ServerBuff = getAddress(this.getServerID());
        byte[] ClientBuff = getAddress(this.getClientID());
        if (((getServer() || !bReply) && getInterfaceType() == InterfaceType.GENERAL) || 
            (!getServer() && bReply && (getInterfaceType() == InterfaceType.NET)))
        {
            byte[] pTmp = ServerBuff;
            ServerBuff = ClientBuff;
            ClientBuff = pTmp;
        }
        if (!GXCommon.compare(buff, ClientBuff))
        {
            //If echo.
            if (getInterfaceType() != InterfaceType.NET && FrameLen != 0)
            {
                if (GXCommon.compare(buff, ServerBuff) && 
                    GXCommon.compare(buff, ClientBuff))
                    //Check that server addresses match.
                {
                    buff.position(2 + FrameLen);
                    DataLen = buff.limit() - buff.position() - 1;
                    if (DataLen > 5)
                    {
                        return getDataFromFrame(buff, data, frame, bReply, 
                                pError, skipLLC, packetFull, wrongCrc, command);
                    }
                    packetFull[0] = false;
                    MoreData.clear();
                    return MoreData;                    
                }
            }            
            int tmp[] = new int[]{buff.position()};
            throw new GXDLMSException("Source addresses do not match. It is " + 
                    getAddress(buff, tmp, ClientBuff.length) + ". It should be " + this.getClientID() + ".");
        }
        if (!GXCommon.compare(buff, ServerBuff))
        {
            int tmp[] = new int[]{buff.position()};
            throw new GXDLMSException("Destination addresses do not match. It is " + 
                    getAddress(buff, tmp, ServerBuff.length) + ". It should be " + this.getServerID() + ".");
        }
        if (getInterfaceType() != InterfaceType.NET)
        {
            //Get frame type.
            frame[0] = buff.get();
            //If server has left.
            if (frame[0] == FrameType.DisconnectMode.getValue() || frame[0] == FrameType.Rejected.getValue())
            {
                MoreData = EnumSet.noneOf(RequestTypes.class);                
                command[0] = Command.REJECTED.getValue();
                return MoreData; 
            }
            if (frame[0] == FrameType.SNRM.getValue() || frame[0] == FrameType.Disconnect.getValue())
            {
                //Check that CRC match.
                int crcRead = buff.getShort() & 0xFFFF;
                int crcCount = GXFCS16.countFCS16(buff, PacketStartID + 1, len - PacketStartID - 4);
                if (crcRead != crcCount)
                {
                    packetFull[0] = false;
                    wrongCrc[0] = true;
                    MoreData.clear();
                    return MoreData;                        
                }
                if (frame[0] == FrameType.SNRM.getValue())
                {
                    MoreData = EnumSet.noneOf(RequestTypes.class);                
                    command[0] = Command.Snrm.getValue();
                    return MoreData;
                }
                if (frame[0] == FrameType.Disconnect.getValue())
                {
                    MoreData = EnumSet.noneOf(RequestTypes.class);
                    command[0] = Command.DisconnectRequest.getValue();
                    return MoreData;
                }
                throw new RuntimeException("Invalid frame.");
            }
            else
            {
                //Check that header crc is corrent.
                int crcCount = GXFCS16.countFCS16(buff, PacketStartID + 1, buff.position() - PacketStartID - 1);
                int crcRead = buff.getShort() & 0xFFFF;
                if (crcRead != crcCount)
                {
                    //Do nothing because Actaris is counting wrong CRC to the header.
                    System.out.println("Wrong header CRC.");
                }
                //Check that CRC match.
                crcCount = GXFCS16.countFCS16(buff, PacketStartID + 1, len - PacketStartID - 4);
                crcRead = buff.getShort(len - 3) & 0xFFFF;
                if (crcRead != crcCount)
                {
                    System.out.println(GXCommon.toHex(buff.array()));
                    wrongCrc[0] = true;
                    MoreData.clear();
                    return MoreData;
                }
                //CheckLLCBytes returns false if LLC bytes are not used.
                if (!skipLLC && checkLLCBytes(buff))
                {
                    //TODO: LLC voi skipata SNRM ja Disconnect.
                    //Check response.
                    command[0] = buff.get(buff.position()) & 0xFF;                    
                    //If chiphering is used.
                    if (command[0] == Command.GloGetRequest.getValue() ||
                        command[0] == Command.GloGetResponse.getValue() ||
                        command[0] == Command.GloSetRequest.getValue() ||
                        command[0] == Command.GloSetResponse.getValue() ||
                        command[0] == Command.GloMethodRequest.getValue() ||
                        command[0] == Command.GloMethodResponse.getValue() ||
                        command[0] == Command.Aarq.getValue() ||
                        command[0] == Command.DisconnectRequest.getValue() ||
                        command[0] == 0x60 ||
                        command[0] == 0x61)
                    {
                    }
                    else if (bReply && (command[0] == Command.GetRequest.getValue() || 
                        command[0] == Command.GetResponse.getValue() ||
                        command[0] == Command.MethodRequest.getValue() || 
                        command[0] == Command.MethodResponse.getValue() ||
                        command[0] == Command.ReadRequest.getValue() || 
                        command[0] == Command.ReadResponse.getValue() ||
                        command[0] == Command.SetRequest.getValue() || 
                        command[0] == Command.SetResponse.getValue() ||
                        command[0] == Command.WriteRequest.getValue() || 
                        command[0] == Command.WriteResponse.getValue()))
                    {
                        /*
                    }
                    //if (command[0] == 0x60 || command[0] == 0x62)
                    {
                        MoreData = EnumSet.noneOf(RequestTypes.class);
                    }
                    else if (bReply && command[0] != 0x61 && command[0] != 0x60)
                    {
                    * */
                        //If LN is used, check is there more data available.
                        if (this.getUseLogicalNameReferencing())
                        {
                            if (!getLNData(buff, pError, MoreData, command[0]))
                            {
                                packetFull[0] = false;
                                MoreData.clear();
                                return MoreData;
                            }
                        }
                        else
                        {
                            getSNData(buff, pError, Command.forValue(command[0]));
                        }
                    }
                }
                //Skip data header and data CRC and EOP.
                if (buff.position() + 3 > buff.limit())
                {
                    if (getServer())
                    {
                        MoreData.add(RequestTypes.FRAME);
                    }                        
                }
                else if (data != null)
                {   
                    int index = buff.position();
                    data.write(buff.array(), index, buff.limit() - index - 3);                       
                }
            }
        }
        else
        {
            DataLen = buff.getShort();
            if (DataLen + buff.position() > len) //If frame is not read complete.
            {
                packetFull[0] = false;                
                MoreData.clear();
                return MoreData;
            }
            // IEC62056-53 Sections 8.3 and 8.6.1
            // If Get.Response.Normal.
            command[0] = buff.get(buff.position()) & 0xFF;
            //If chiphering is used.
            if (command[0] == Command.GloGetRequest.getValue() ||
                command[0] == Command.GloGetResponse.getValue() ||
                command[0] == Command.GloSetRequest.getValue() ||
                command[0] == Command.GloSetResponse.getValue() ||
                command[0] == Command.GloMethodRequest.getValue() ||
                command[0] == Command.GloMethodResponse.getValue() ||
                command[0] == Command.Aarq.getValue() ||
                command[0] == Command.DisconnectRequest.getValue() ||
                command[0] == Command.DisconnectResponse.getValue() ||
                command[0] == 0x61 ||
                command[0] == 0x60)
            {
            }
            else if (bReply && (command[0] == Command.GetRequest.getValue() || 
                    command[0] == Command.GetResponse.getValue() ||
                    command[0] == Command.MethodRequest.getValue() || 
                    command[0] == Command.MethodResponse.getValue() ||
                    command[0] == Command.ReadRequest.getValue() || 
                    command[0] == Command.ReadResponse.getValue() ||
                    command[0] == Command.SetRequest.getValue() || 
                    command[0] == Command.SetResponse.getValue() ||
                    command[0] == Command.WriteRequest.getValue() || 
                    command[0] == Command.WriteResponse.getValue()))
            {
                //If LN is used, check is there more data available.
                if (this.getUseLogicalNameReferencing())
                {
                    getLNData(buff, pError, MoreData, command[0]);                    
                }
                else
                {
                    getSNData(buff, pError, Command.forValue(command[0]));
                }
            }
            if (data != null)
            {
                int index = buff.position();
                data.write(buff.array(), index, buff.limit() - index);
            }
        }
        return MoreData;
    }

    public final byte[] GetCipheredData(byte[] data, GXCiphering ciphering)
    {
        return GXDLMSChippering.DecryptAesGcm(data, ciphering.getSystemTitle(), 
                ciphering.getBlockCipherKey(), ciphering.getAuthenticationKey());
    }
 
    public final byte[] decrypt(byte[] buff, Command[] command)
    {
        int index[] = new int[1];
        command[0] = Command.forValue(buff[index[0]++] & 0xFF);
        if (!(command[0] == Command.GloGetRequest || 
            command[0] == Command.GloSetRequest || 
            command[0] == Command.GloMethodRequest || 
            command[0] == Command.GloGetResponse || 
            command[0] == Command.GloSetResponse || 
            command[0] == Command.GloMethodResponse))
        {
            throw new GXDLMSException("Invalid data format.");
        }
        int len = GXCommon.getObjectCount(buff, index);
        if (buff.length - index[0] != len)
        {
            throw new GXDLMSException("Invalid data format.");
        }
        byte value = buff[index[0]++];
        if (value != 0x10 && value != 0x20 && value != 0x30)
        {
            throw new IllegalArgumentException("Invalid security value.");
        }
        if (Ciphering.getSecurity() == Security.NONE)
        {
            this.Ciphering.setSecurity(Security.forValue(value));
        }
        else if (this.Ciphering.getSecurity() != Security.forValue(value))
        {
            throw new GXDLMSException("Security method can't change after initialized.");
        }
        java.nio.ByteBuffer tmp = java.nio.ByteBuffer.wrap(GetCipheredData(buff, Ciphering));
        command[0] = Command.forValue(tmp.get(0) & 0xFF);
        if (this.getUseLogicalNameReferencing())
        {
            int[] error = new int[1];
            java.util.Set<RequestTypes> moreData = EnumSet.noneOf(RequestTypes.class);
            if (!getLNData(tmp, error, moreData, tmp.get(0) & 0xFF))
            {
                throw new GXDLMSException("Invalid data format.");
            }
        }
        else
        {
            int[] error = new int[1];
            getSNData(tmp, error, command[0]);
        }
        byte[] tmp2 = new byte[tmp.capacity() - tmp.position()];
        tmp.get(tmp2);
        return tmp2;
    }

    private static void getSNData(java.nio.ByteBuffer buff, int[] pError, Command command)
    {
        //Check that this is reply
        if (command != Command.ReadRequest && 
            command != Command.WriteRequest && 
            command != Command.SetRequest && 
            command != Command.SetResponse && 
            command != Command.ReadResponse && 
            command != Command.WriteResponse && 
            command != Command.GetRequest && 
            command != Command.GetResponse && 
            command != Command.MethodRequest && 
            command != Command.MethodResponse)
        {
            throw new GXDLMSException("Invalid command");
        }   
        //Ship invoke ID and priority.
        buff.get();
        if (command == Command.ReadResponse || command == Command.WriteResponse)
        {
            //Add reply status.
            buff.get();
            boolean bIsError = (buff.get() != 0);
            if (bIsError)
            {
                pError[0] = buff.get();
            }
        }
    }   
    
    private boolean getLNData(java.nio.ByteBuffer buff, int[] pError, java.util.Set<RequestTypes> MoreData, int command)
    {
        int res = command;
        //Get count.
        buff.get();
        //If meter returns exception.
        if (res == 0xD8)
        {
            StateError stateError = StateError.forValue(buff.get());
            ServiceError serviceError = ServiceError.forValue(buff.get());
            throw new GXDLMSException(stateError.toString() + " " + serviceError.toString());
        }
        boolean server = this.getServer();
        if (res != 0x60 && res != 0x63 && 
                res != Command.GetResponse.getValue() && 
                res != Command.SetResponse.getValue() && 
                res != Command.SetRequest.getValue() && 
                res != Command.GetRequest.getValue() && 
                res != Command.MethodRequest.getValue() &&
                res != Command.MethodResponse.getValue())
        {
            throw new GXDLMSException("Invalid response");
        }
        byte AttributeID = buff.get();
        //Skip Invoke ID and priority.
        buff.get();
        if (server && AttributeID == 0x2)
        {            
            MoreData.add(RequestTypes.DATABLOCK);
        }
        else if (res == Command.SetRequest.getValue())
        {
            MoreData.clear();
        }
        else if (res == Command.SetResponse.getValue() || 
                res == Command.MethodResponse.getValue())
        {
            pError[0] = buff.get();
        }
        else
        {
            if (server && AttributeID == 0x01)
            {
            }
            else
            {
                byte Priority = buff.get();
                if (AttributeID == 0x01 && Priority != 0)
                {
                    pError[0] = buff.get();
                }
                else
                {
                    if (AttributeID == 0x02)
                    {
                        if (Priority == 0)
                        {
                            MoreData.add(RequestTypes.DATABLOCK);
                        }
                        packetIndex = buff.getInt();
                        buff.get();
                        //Get items count.
                        int[] index = new int[]{buff.position()};
                        GXCommon.getObjectCount(buff, index);
                        buff.position(index[0]);
                    }
                }
            }
        }
        return true;
    }
    
    static void getActionInfo(ObjectType objectType, int[] value, int[] count) throws RuntimeException
    {        
        switch (objectType)
        {
            case DATA:
            case ACTION_SCHEDULE:
            case ALL:
            case AUTO_ANSWER:
            case AUTO_CONNECT:
            case MAC_ADDRESS_SETUP:
            case EVENT:
            case GPRS_SETUP:
            case IEC_HDLC_SETUP:
            case IEC_LOCAL_PORT_SETUP:
            case IEC_TWISTED_PAIR_SETUP:
            case MODEM_CONFIGURATION:
            case PPP_SETUP:
            case REGISTER_MONITOR:
            case REMOTE_ANALOGUE_CONTROL:
            case REMOTE_DIGITAL_CONTROL:
            case SCHEDULE:
            case SMTP_SETUP:
            case STATUS_MAPPING:
            case TCP_UDP_SETUP:
            case TUNNEL:
            case UTILITY_TABLES:
                throw new RuntimeException("Target do not support Action.");
            case IMAGE_TRANSFER:
                value[0] = 0x40;
                count[0] = 4;
                break;
            case ACTIVITY_CALENDAR:
                value[0] = 0x50;
                count[0] = 1;
                break;
            case ASSOCIATION_LOGICAL_NAME:
                value[0] = 0x60;
                count[0] = 4;
                break;
            case ASSOCIATION_SHORT_NAME:
                value[0] = 0x20;
                count[0] = 8;
                break;
            case CLOCK:
                value[0] = 0x60;
                count[0] = 6;
                break;
            case DEMAND_REGISTER:
                value[0] = 0x48;
                count[0] = 2;
                break;
            case EXTENDED_REGISTER:
                value[0] = 0x38;
                count[0] = 1;
                break;
            case IP4_SETUP:
                value[0] = 0x60;
                count[0] = 3;
                break;
            case MBUS_SLAVE_PORT_SETUP:
                value[0] = 0x60;
                count[0] = 8;
                break;
            case PROFILE_GENERIC:
                value[0] = 0x58;
                count[0] = 4;
                break;
            case REGISTER:
                value[0] = 0x28;
                count[0] = 1;
                break;
            case REGISTER_ACTIVATION:
                value[0] = 0x30;
                count[0] = 3;
                break;
            case REGISTER_TABLE:
                value[0] = 0x28;
                count[0] = 2;
                break;
            case SAP_ASSIGNMENT:
            case SCRIPT_TABLE:
                value[0] = 0x20;
                count[0] = 1;
                break;
            case SPECIAL_DAYS_TABLE:
                value[0] = 0x10;
                count[0] = 2;
                break;
            default:     
                count[0] = value[0] = 0;
                break;
        }
    }
}
