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
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.manufacturersettings.GXObisCodeCollection;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;
import gurux.dlms.manufacturersettings.GXObisCode;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSRegister;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 GXDLMS implements methods to communicate with DLMS/COSEM metering devices.
*/
public class GXDLMSClient
{
    GXDLMSObjectCollection Objects;
    private GXObisCodeCollection privateObisCodes;
    private GXDLMS m_Base = new GXDLMS(false);            
    boolean m_IsAuthenticationRequired;
    private byte[] m_Password;
    
    /** 
     Constructor.
    */
    public GXDLMSClient()
    {        
        this.setAuthentication(gurux.dlms.enums.Authentication.NONE);
    }
    
    /** 
     Constructor.
     
     @param useLogicalNameReferencing Is Logical Name referencing used.
     @param clientID Server address.
     @param serverID Client address.
     @param authentication Authentication type.
     @param password Password if authentication is used.
     @param intefaceType
    */
    public GXDLMSClient(boolean useLogicalNameReferencing,             
            Object clientID,
            Object serverID,            
            gurux.dlms.enums.Authentication authentication,             
            String password,
            InterfaceType interfaceType)
    {
        this.setUseLogicalNameReferencing(useLogicalNameReferencing);
        this.setClientID(clientID);
        this.setServerID(serverID);
        setAuthentication(authentication);
        try
        {
            setPassword(password.getBytes("ASCII"));
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }    
        setInterfaceType(interfaceType);
    }
    
    /*
     * List of meter's objects.
     */
    public GXDLMSObjectCollection getObjects()
    {
        return Objects;
    }
    
    public void setObjects(GXDLMSObjectCollection value)
    {
        Objects = value;
    }

    /** 
     List of available OBIS codes.
     This list is used when Association view is read from the meter and description of the object is needed.
     If collection is not set description of object is empty.
    */
    public final GXObisCodeCollection getObisCodes()
    {
        return privateObisCodes;
    }
    public final void setObisCodes(GXObisCodeCollection value)
    {
        privateObisCodes = value;
    }

    /** 
     Checks, whether the received packet is a reply to the sent packet.

     @param sendData The sent data as a byte array. 
     @param receivedData The received data as a byte array.
     @return True, if the received packet is a reply to the sent packet. False, if not.
                    */
    public final boolean isReplyPacket(byte[] sendData, byte[] receivedData) throws Exception
    {
        return m_Base.isReplyPacket(sendData, receivedData);
    }

    public final GXCiphering getCiphering()
    {
        return m_Base.Ciphering;
    }
    /** 
     Client ID is the identification of the device that is used as a client.
     Client ID is aka HDLC Address.
    */
    public final Object getClientID()
    {
        return m_Base.getClientID();
    }
    public final void setClientID(Object value)
    {
        if (getInterfaceType() == InterfaceType.NET)
        {
            value = ((Number) value).shortValue();
        }
        m_Base.setClientID(value);
    }

    /** 
     Server ID is the indentification of the device that is used as a server.
     Server ID is aka HDLC Address.
    */
    public final Object getServerID()
    {
        return m_Base.getServerID();
    }
    public final void setServerID(Object value)
    {
        if (getInterfaceType() == InterfaceType.NET)
        {
            value = ((Number) value).shortValue();
        }
        m_Base.setServerID(value);
    }

    /** 
     Set server ID.

     This method is reserved for languages like Python where is no byte size.

     @param value Server ID.
     @param size Size of server ID as bytes.
    */
    public final void setServerID(Object value, int size)
    {
        if (size != 1 && size != 2 && size != 4)
        {
            throw new IllegalArgumentException("size");
        }
        if (size == 1)
        {
            m_Base.setServerID(((Number)value).byteValue());
        }
        else if (size == 2)
        {
            m_Base.setServerID(((Number)value).shortValue());
        }
        else if (size == 4)
        {
            m_Base.setServerID(((Number)value).intValue());
        }
    }

    /** 
     Set client ID.

     This method is reserved for languages like Python where is no byte size.
     
     @param value Client ID.
     @param size Size of server ID as bytes.
    */
    public final void setClientID(Object value, int size)
    {
        if (size != 1 && size != 2 && size != 4)
        {
            throw new IllegalArgumentException("size");
        }
        if (size == 1)
        {
            m_Base.setClientID(((Number)value).byteValue());
        }
        else if (size == 2)
        {
            m_Base.setClientID(((Number)value).shortValue());
        }
        else if (size == 4)
        {
            m_Base.setClientID(((Number)value).intValue());
        }
    }


    /** 
     Are BOP, EOP and checksum added to the packet.
    */
    public final boolean getGenerateFrame()
    {
        return m_Base.getGenerateFrame();
    }
    public final void setGenerateFrame(boolean value)
    {
        m_Base.setGenerateFrame(value);
    }

    /** 
     Is cache used. Default value is True;
    */
    public final boolean getUseCache()
    {
        return m_Base.getUseCache();
    }
    public final void setUseCache(boolean value)
    {
        m_Base.setUseCache(value);
    }

    /** 
     DLMS version number. 
     Gurux DLMS component supports DLMS version number 6.

     @see GXDLMSClient#SNRMRequest
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

     @see GXDLMSClient#getClientID()
     @see GXDLMSClient#getServerID
     @see GXDLMSClient#getDLMSVersion
     @see GXDLMSClient#getUseLogicalNameReferencing
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
     Retrieves the password that is used in communication.

     If authentication is set to none, password is not used.

     @see GXDLMSClient#getAuthentication
    */
    public final byte[] getPassword()
    {
        return m_Password;
    }
    public final void setPassword(byte[] value)
    {
        m_Password = value;
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

    /** 
     Retrieves the amount of unused bits.    
    */
    public final int getNumberOfUnusedBits()
    {
        return m_Base.getNumberOfUnusedBits();
    }

    /** 
     Retrieves the data type. 

     @param data Data to parse.
     @return The current data type.
    */
    public final DataType getDLMSDataType(byte[] data) throws Exception
    {
        //Return cache size.
        if (getUseCache() && data.length == m_Base.cacheSize)
        {
            return m_Base.cacheType;
        }
        if (!getUseCache())
        {
            m_Base.clearProgress();
        }
        else if (m_Base.cacheIndex != 0)
        {
            return m_Base.cacheType;
        }
        DataType[] type = new DataType[1];
        Object[] value = new Object[1];
        m_Base.parseReplyData(getUseCache() ? ActionType.INDEX : ActionType.COUNT, data, value, type);
        return type[0];
    }

    /** 
     Retrieves the authentication used in communicating with the device.


     By default authentication is not used. If authentication is used,
     set the password with the Password property.

     @see GXDLMSClient#getPassword
     @see GXDLMSClient#getClientID
     @see GXDLMSClient#getServerID
     @see GXDLMSClient#getDLMSVersion
     @see GXDLMSClient#getUseLogicalNameReferencing
     @see GXDLMSClient#getMaxReceivePDUSize
    */
    public final Authentication getAuthentication()
    {
        return m_Base.getAuthentication();
    }
    public final void setAuthentication(Authentication value)
    {
        m_Base.setAuthentication(value);
    }

    public final Priority getPriority()
    {
        return m_Base.getPriority();
    }
    public final void setPriority(Priority value)
    {
        m_Base.setPriority(value);
    }

    /** 
     Used service class.
    */
    public final ServiceClass getServiceClass()
    {
       return m_Base.getServiceClass();
    }
    public final void setServiceClass(ServiceClass value)
    {
        m_Base.setServiceClass(value);
    }

    /** 
     Invoke ID.
    */
    public final int getInvokeID()
    {
        return m_Base.getInvokeID();
    }
    public final void setInvokeID(int value)
    {
        m_Base.setInvokeID(value);
    }
                
    /** 
     Determines the type of the connection

     All DLMS meters do not support the IEC 62056-47 standard.  
     If the device does not support the standard, and the connection is made 
     using TCP/IP, set the type to InterfaceType.General.
    */
    public final InterfaceType getInterfaceType()
    {
        return m_Base.getInterfaceType();
    }
    public final void setInterfaceType(InterfaceType value)
    {
        m_Base.setInterfaceType(value);
    }

    /** 
     Information from the connection size that server can handle.
    */
    public final GXDLMSLimits getLimits()
    {
        return m_Base.getLimits();
    }
    
    /*
     * Convert byte array to hex string.
     */
    public static String toHex(byte[] bytes) 
    {   
        return toHex(bytes, 0, bytes.length);        
    } 
    
    /*
     * Convert byte array to hex string.
     */
    public static String toHex(byte[] bytes, int index, int count) 
    {    
        return GXCommon.toHex(bytes, index, count);
    } 

    /** 
     Generates SNRM request.


     his method is used to generate send SNRMRequest. 
     Before the SNRM request can be generated, at least the following 
     properties must be set:
     <ul>
     <li>ClientID</li>
     <li>ServerID</li>    
     </ul>
     <b>Note! </b>According to IEC 62056-47: when communicating using 
     TCP/IP, the SNRM request is not send.

     @return SNRM request as byte array.
     @see GXDLMSClient#getClientID
     @see GXDLMSClient#getServerID
     @see GXDLMSClient#parseUAResponse
    */
    public final byte[] SNRMRequest()
    {
        m_IsAuthenticationRequired = false;
        m_Base.setMaxReceivePDUSize(0xFFFF);
        m_Base.clearProgress();
        //SNRM reguest is not used in network connections.
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            return new byte[0];
        }
        ByteBuffer tmp = ByteBuffer.allocate(50); 
        tmp.put((byte)0x81); //FromatID
        tmp.put((byte) 0x80); //GroupID
        tmp.put((byte) 0); //len

        //If custom HDLC parameters are used.
        if (!GXDLMSLimits.DefaultMaxInfoTX.equals(this.getLimits().getMaxInfoTX())){
            tmp.put((byte) HDLCInfo.MaxInfoTX);
            GXDLMSLimits.setValue(tmp, getLimits().getMaxInfoTX());
        }
        if (!GXDLMSLimits.DefaultMaxInfoRX.equals(this.getLimits().getMaxInfoRX())){
            tmp.put((byte) HDLCInfo.MaxInfoRX);
            GXDLMSLimits.setValue(tmp, getLimits().getMaxInfoRX());
        }
        if (!GXDLMSLimits.DefaultWindowSizeTX.equals(this.getLimits().getWindowSizeTX())){
            tmp.put((byte) HDLCInfo.WindowSizeTX);
            GXDLMSLimits.setValue(tmp, getLimits().getWindowSizeTX());
        }
        if (!GXDLMSLimits.DefaultWindowSizeTX.equals(this.getLimits().getWindowSizeRX())){   
            tmp.put((byte) HDLCInfo.WindowSizeRX);
            GXDLMSLimits.setValue(tmp, getLimits().getWindowSizeRX());                
        }
        //If default HDLC parameters are not used.
        int cnt = 0;
        byte[] data = null;
        if (tmp.position() != 3){
            byte len = (byte)tmp.position();            
            tmp.put(2, (byte)(len - 3)); //len
            cnt = tmp.position();
            data = tmp.array();
        }
        return m_Base.addFrame(FrameType.SNRM.getValue(), false, data, 0, cnt);
    }

    /** 
     Parses UAResponse from byte array.

     @param data        
     @see GXDLMSClient#SNRMRequest
    */
    public final void parseUAResponse(byte[] data)
    {
        int[] error = new int[1];
        byte[] frame = new byte[1];
        boolean[] packetFull = new boolean[1], wrongCrc = new boolean[1];
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();        
        int[] command = new int[1];
        m_Base.getDataFromFrame(java.nio.ByteBuffer.wrap(data), tmp, frame, true, 
                error, true, packetFull, wrongCrc, command);
        java.nio.ByteBuffer arr = java.nio.ByteBuffer.wrap(tmp.toByteArray());
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        if (this.getInterfaceType() != InterfaceType.NET && frame[0] != FrameType.UA.getValue())
        {
            throw new GXDLMSException("Not a UA response :" + frame[0]);
        }
        if (arr.limit() > 3){
            arr.get(); //Skip FromatID
            arr.get(); //Skip Group ID.
            arr.get(); //Skip Group len
            Object val;
            while (arr.position() < arr.limit()){
                int id = arr.get();
                byte len = arr.get();
                
                switch (len){
                    case 1:
                        val = (short) arr.get() & 0xFF;
                        break;
                    case 2:
                        val = (int) arr.getShort() & 0xFFFF;
                        break;
                    case 4:
                        val = (long) arr.getInt() & 0xFFFFFFFF;
                        break;
                    default:
                        throw new GXDLMSException("Invalid Exception.");
                }
                
                switch (id){
                    case HDLCInfo.MaxInfoTX:
                        getLimits().setMaxInfoTX(val);
                        break;
                    case HDLCInfo.MaxInfoRX:
                        getLimits().setMaxInfoRX(val);
                        break;
                    case HDLCInfo.WindowSizeTX:
                        getLimits().setWindowSizeTX(val);
                        break;
                    case HDLCInfo.WindowSizeRX:
                        getLimits().setWindowSizeRX(val);
                        break;
                    default:
                        throw new GXDLMSException("Invalid UA response.");
                }
            }
        }
    }

    /** 
     Generate AARQ request. 

     Because all meters can't read all data in one packet, 
     the packet must be split first, by using SplitDataToPackets method.

     @param Tags
     @return AARQ request as byte array.
     @see GXDLMSClient#parseAAREResponse     
     @see GXDLMSClient#isDLMSPacketComplete
    */
    public final byte[][] AARQRequest(GXDLMSTagCollection Tags)
    {           
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(200);
        m_Base.checkInit();
        GXAPDU aarq = new GXAPDU(Tags);
        aarq.setUseLN(this.getUseLogicalNameReferencing());        
        if (this.getUseLogicalNameReferencing())
        {
            m_Base.setSNSettings(null);
            m_Base.setLNSettings(new GXDLMSLNSettings(new byte[] {0x00, 0x7E, 0x1F}));
            aarq.userInformation.conformanceBlock = getLNSettings().conformanceBlock;
        }
        else
        {
            m_Base.setLNSettings(null);
            m_Base.setSNSettings(new GXDLMSSNSettings(new byte[] {0x1C, 0x03, 0x20}));
            aarq.userInformation.conformanceBlock = getSNSettings().conformanceBlock;
        }
        aarq.setAuthentication(this.getAuthentication(), getPassword());
        aarq.userInformation.dlmsVersioNumber = getDLMSVersion();
        aarq.userInformation.maxReceivePDUSize = getMaxReceivePDUSize();        
        m_Base.StoCChallenge = null;
        if (getAuthentication().ordinal() > Authentication.HIGH.ordinal())
        {
            m_Base.CtoSChallenge = GXDLMS.generateChallenge();
        }
        else
        {
            m_Base.CtoSChallenge = null;
        }
        try
        {
            aarq.codeData(buff, getInterfaceType(), m_Base.CtoSChallenge);
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        m_Base.frameSequence = -1;
        m_Base.expectedFrame = -1;
        return m_Base.splitToBlocks(buff, Command.None, false);
    }

    /** 
     Parses the AARE response.

     @param reply

     Parse method will update the following data:
     <ul>
     <li>DLMSVersion</li>
     <li>MaxReceivePDUSize</li>
     <li>UseLogicalNameReferencing</li>
     <li>LNSettings or SNSettings</li>
     </ul>
     LNSettings or SNSettings will be updated, depending on the referencing, 
     Logical name or Short name.

     @return The AARE response
     @see GXDLMSClient#AARQRequest
     @see GXDLMSClient#getUseLogicalNameReferencing
     @see GXDLMSClient#getDLMSVersion
     @see GXDLMSClient#getMaxReceivePDUSize
     @see GXDLMSClient#getLNSettings
     @see GXDLMSClient#getSNSettings
    */
    public final GXDLMSTagCollection parseAAREResponse(byte[] reply)
    {
        byte[] frame = new byte[1];
        int[] error = new int[1];
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();        
        boolean[] packetFull = new boolean[1], wrongCrc = new boolean[1];
        int[] command = new int[1];
        m_Base.getDataFromFrame(java.nio.ByteBuffer.wrap(reply), tmp, frame, true, 
                error, false, packetFull, wrongCrc, command);
        java.nio.ByteBuffer arr = java.nio.ByteBuffer.wrap(tmp.toByteArray());
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        //Parse AARE data.
        GXDLMSTagCollection Tags = new GXDLMSTagCollection();
        GXAPDU pdu = new GXAPDU(Tags);
        pdu.encodeData(arr);
        m_Base.StoCChallenge = pdu.password;
        setUseLogicalNameReferencing(pdu.getUseLN());
        AssociationResult ret = pdu.getResultComponent();
        if (ret == AssociationResult.ACCEPTED)
        {
            if (getUseLogicalNameReferencing())
            {
                m_Base.setLNSettings(new GXDLMSLNSettings(pdu.userInformation.conformanceBlock));
            }
            else
            {
                m_Base.setSNSettings(new GXDLMSSNSettings(pdu.userInformation.conformanceBlock));
            }
            setMaxReceivePDUSize(pdu.userInformation.maxReceivePDUSize);
            setDLMSVersion(pdu.userInformation.dlmsVersioNumber);
        }
        else
        {
           m_Base.setLNSettings(null);
           m_Base.setSNSettings(null);
           throw new GXDLMSException(ret, pdu.getResultDiagnosticValue());
        }
        m_IsAuthenticationRequired = pdu.resultDiagnosticValue == SourceDiagnostic.AUTHENTICATION_REQUIRED.getValue();
        if (getDLMSVersion() != 6)
        {
            throw new GXDLMSException("Invalid DLMS version number.");
        }
        return Tags;
    }
    
    /*
     * Is authentication Required.
     */
    public boolean getIsAuthenticationRequired()
    {
        return m_IsAuthenticationRequired;
    }

    /// <summary>
    /// Get challenge request if HLS authentication is used.
    /// </summary>
    /// <returns></returns>
    public byte[][] getApplicationAssociationRequest()
    {
        if (m_Password == null || m_Password.length == 0)
        {
            throw new IllegalArgumentException("Password is invalid.");
        }
        ByteArrayOutputStream CtoS = new ByteArrayOutputStream();        
        try
        {
            CtoS.write(m_Password);
            CtoS.write(m_Base.StoCChallenge);
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        byte[] challenge = GXDLMSServerBase.chipher(getAuthentication(), CtoS.toByteArray());
        if (getUseLogicalNameReferencing())
        {
            return method("0.0.40.0.0.255", ObjectType.ASSOCIATION_LOGICAL_NAME, 1, challenge, 1, DataType.OCTET_STRING);
        }
        return method(0xFA00, ObjectType.ASSOCIATION_SHORT_NAME, 8, challenge, 1, DataType.OCTET_STRING);            
    }

    /// <summary>
    /// Parse server's challenge if HLS authentication is used.
    /// </summary>
    /// <param name="reply"></param>
    public void parseApplicationAssociationResponse(byte[] reply)
    {
        int[] error = new int[1];
        byte[] frame = new byte[1];
        boolean[] packetFull = new boolean[1], wrongCrc = new boolean[1];
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();        
        int[] command = new int[1];        
        try
        {
            m_Base.getDataFromFrame(java.nio.ByteBuffer.wrap(reply), tmp, 
                    frame, true, error, false, packetFull, wrongCrc, command);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getMessage());        
        }
        if (!packetFull[0])
        {
            throw new GXDLMSException("Not enought data to parse frame.");
        }
        if (wrongCrc[0])
        {
            throw new GXDLMSException("Wrong Checksum.");
        }
        int[] index = new int[1];
        //Skip item count
        ++index[0];
        //Skip item status
        ++index[0];
        int[] total = new int[1], count = new int[1];
        DataType[] type = new DataType[]{DataType.NONE};
        int[] cachePosition = new int[1];
        byte[] serverChallenge = (byte[]) GXCommon.getData(tmp.toByteArray(), index, 
                ActionType.NONE.getValue(), total, count, type, cachePosition);        
        ByteArrayOutputStream challenge = new ByteArrayOutputStream();        
        try
        {
            challenge.write(m_Password);        
            challenge.write(m_Base.CtoSChallenge);
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex.getMessage());        
        }
        byte[] clientChallenge = GXDLMSServerBase.chipher(getAuthentication(), challenge.toByteArray());
        int[] pos = new int[1];
        if (!GXCommon.compare(serverChallenge, pos, clientChallenge))
        {
            throw new GXDLMSException("Server returns invalid challenge.");
        }
    }

    /** 
     Generates a disconnect mode request.

     @return Disconnect mode request, as byte array.
    */
    public final byte[] disconnectedModeRequest()
    {
        m_Base.clearProgress();
        //If connection is not established, there is no need to send DisconnectRequest.
        if (getSNSettings() == null && getLNSettings() == null) //TODO:
        {
            return new byte[0];
        }
        //In current behavior, disconnect is not generated for network connection.
        if (this.getInterfaceType() != InterfaceType.NET)
        {
            return m_Base.addFrame(FrameType.DisconnectMode.getValue(), false, (byte[]) null, 0, 0);
        }
        return new byte[0];
    }

    /** 
     Generates a disconnect request.

     @return Disconnected request, as byte array.
    */
    public final byte[] disconnectRequest()
    {
        m_Base.clearProgress();
        if (this.getInterfaceType() != InterfaceType.NET)
        {
            return m_Base.addFrame(FrameType.Disconnect.getValue(), false, (byte[]) null, 0, 0);
        }
        return new byte[0];
    }

    /** 
     Reserved for internal use.
     @param ClassID
     @param Version
     @param BaseName
     @param LN
     @param AccessRights
     @param AttributeIndex
     @param dataIndex
     @return 
    */
    private GXDLMSObject createDLMSObject(int ClassID, Object Version, int BaseName, Object LN, Object AccessRights)
    {
        ObjectType type = ObjectType.forValue(ClassID);
        GXDLMSObject obj = createObject(type); 
        updateObjectData(obj, type, Version, BaseName, (byte[])LN, AccessRights);
        if (getObisCodes() != null)
        {
            GXObisCode code = getObisCodes().findByLN(obj.getObjectType(), obj.getLogicalName(), null);
            if (code != null)
            {
                obj.setDescription(code.getDescription());
                obj.getAttributes().addAll(code.getAttributes());
            }
        }
        return obj;        
    }

    /** 
     Reserved for internal use.
    */
    private GXDLMSObjectCollection parseSNObjects(byte[] buff, boolean onlyKnownObjects)
    {
        int[] index = new int[1];
        //Get array tag.
        byte size = buff[index[0]++];
        //Check that data is in the array
        if (size != 0x01)
        {
            throw new GXDLMSException("Invalid response.");
        }
        GXDLMSObjectCollection items = new GXDLMSObjectCollection(this);
        long cnt = GXCommon.getObjectCount(buff, index);
        int[] total = new int[1], count = new int[1];
        for (long objPos = 0; objPos != cnt; ++objPos)
        {
            DataType[] type = new DataType[]{DataType.NONE};
            int[] cachePosition = new int[1];
            Object[] objects = (Object[])GXCommon.getData(buff, index, 
                    ActionType.NONE.getValue(), total, count, type, cachePosition);
            if (index[0] == -1)
            {
                throw new OutOfMemoryError();
            }
            if (objects.length != 4)
            {
                throw new GXDLMSException("Invalid structure format.");
            }
            int classID = ((Number)(objects[1])).intValue() & 0xFFFF;                    
            int baseName = ((Number)(objects[0])).intValue() & 0xFFFF;
            if (baseName > 0)
            {
                GXDLMSObject comp = createDLMSObject(classID, objects[2], baseName, objects[3], null);
                if (!onlyKnownObjects || comp.getClass() != GXDLMSObject.class)
                {
                    items.add(comp);
                }
                else
                {
                    System.out.println(String.format("Unknown object : %d %d", classID, baseName));                    
                }
            }
        }
        return items;        
    }

    /** 
     Reserved for internal use.

     @param objectType
     @param version
     @param baseName
     @param logicalName
     @param accessRights
     @param attributeIndex
     @param dataIndex
    */
    final void updateObjectData(GXDLMSObject obj, ObjectType objectType, Object version, Object baseName, byte[] logicalName, Object accessRights)
    {
        obj.setObjectType(objectType);
        // Check access rights.
        if (accessRights != null && accessRights.getClass().isArray())
        {
            //access_rights: access_right
            Object[] access = (Object[])accessRights;
            for (Object attributeAccess : (Object[]) access[0])
            {
                int id = ((Number)((Object[]) attributeAccess)[0]).intValue();
                int tmp = ((Number)((Object[]) attributeAccess)[1]).intValue();
                AccessMode mode = AccessMode.forValue(tmp);
                obj.setAccess(id, mode);
            }
            for (Object methodAccess : (Object[])access[1])
            {
                int id = ((Number)((Object[]) methodAccess)[0]).intValue();
                int tmp;
                //If version is 0
                if (((Object[]) methodAccess)[1] instanceof Boolean)
                {
                    if ((Boolean) ((Object[]) methodAccess)[1])
                    {
                        tmp = 1;
                    }
                    else
                    {
                        tmp = 0;
                    }
                }
                else //If version is 1.
                {
                    tmp = ((Number)((Object[]) methodAccess)[1]).intValue();
                }
                MethodAccessMode mode = MethodAccessMode.forValue(tmp);
                obj.setMethodAccess(id, mode);
            }
        }
        if (baseName != null)
        {
            obj.setShortName(((Number)baseName).intValue());
        }
        if (version != null)
        {
            obj.setVersion(((Number)version).intValue());
        }
        obj.setLogicalName(GXDLMSObject.toLogicalName(logicalName));        
    }

    /** 
     Parses the COSEM objects of the received data.

     @param data Received data, from the device, as byte array. 
     @return Collection of COSEM objects.
    */
    public final GXDLMSObjectCollection parseObjects(ByteArrayOutputStream data, boolean onlyKnownObjects)
    {
        return parseObjects(data.toByteArray(), onlyKnownObjects);
    }
        
    static void updateOBISCodes(GXDLMSObjectCollection objects)
    {        
        InputStream stream = GXDLMSClient.class.getResourceAsStream("/resources/OBISCodes.txt");
        if (stream == null)
        {
            return;
        }
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1000];
        try 
        {
            while ((nRead = stream.read(data, 0, data.length)) != -1) 
            {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(GXDLMSClient.class.getName()).log(Level.SEVERE, null, ex);
        }        
        String str = buffer.toString();        
        GXStandardObisCodeCollection codes = new GXStandardObisCodeCollection();
        String[] rows = str.split("\r\n");
        for (String it : rows)
        {
            String[] items = it.split("[;]", -1);
            String[] obis = items[0].split("[.]", -1);
            GXStandardObisCode code = new GXStandardObisCode(obis, items[3] + "; " + items[4] + "; " + items[5] + "; " + items[6] + "; " + items[7], items[1], items[2]);
            codes.add(code);
        }
        for (GXDLMSObject it : objects)
        {
            if (!(it.getDescription() == null || it.getDescription().equals("")))
            {
                continue;
            }
            String ln = it.getLogicalName();           
            GXStandardObisCode code = codes.find(ln, it.getObjectType());
            if (code != null)
            {
                it.setDescription(code.getDescription());                
                //If string is used
                if (code.getDataType().contains("10"))
                {
                    code.setDataType("10");
                }
                //If date time is used.
                else if (code.getDataType().contains("25") || code.getDataType().contains("26"))
                {
                    code.setDataType("25");
                }                
                else if (code.getDataType().contains("9"))
                {
                    //Time stamps of the billing periods objects (first scheme if there are two)
                    if ((GXStandardObisCodeCollection.equalsMask("0.0-64.96.7.10-14.255", ln) ||
                        //Time stamps of the billing periods objects (second scheme)
                        GXStandardObisCodeCollection.equalsMask("0.0-64.0.1.5.0-99,255", ln) ||
                        //Time of power failure
                        GXStandardObisCodeCollection.equalsMask("0.0-64.0.1.2.0-99,255", ln) ||
                        //Time stamps of the billing periods objects (first scheme if there are two)                        
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.1.2.0-99,255", ln) ||
                        //Time stamps of the billing periods objects (second scheme)
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.1.5.0-99,255", ln) ||
                        //Time expired since last end of billing period
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.0.255", ln) ||                        
                        //Time of last reset
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.6.255", ln) ||
                        //Date of last reset
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.7.255", ln) ||
                        //Time expired since last end of billing period (Second billing period scheme)
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.13.255", ln) ||                        
                        //Time of last reset (Second billing period scheme)
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.14.255", ln) ||
                        //Date of last reset (Second billing period scheme)
                        GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.15.255", ln)))
                    {
                        code.setDataType("25");
                    }
                    //Local time
                    else if (GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.1.255", ln))
                    {
                        code.setDataType("27");
                    }
                    //Local date
                    else if (GXStandardObisCodeCollection.equalsMask("1.0-64.0.9.2.255", ln))
                    {
                        code.setDataType("26");
                    }
                }
                if (!code.getDataType().equals("*") && 
                        !code.getDataType().equals("") && 
                        !code.getDataType().contains(","))
                {
                    DataType type = DataType.forValue(Integer.parseInt(code.getDataType()));
                    switch (it.getObjectType())
                    {
                        case DATA:
                        case REGISTER:
                        case REGISTER_ACTIVATION:                            
                        case EXTENDED_REGISTER:                            
                            it.setUIDataType(2, type);
                            break;
                        default:
                        break;
                    }                    
                }
            }
            else
            {
                System.out.println("Unknown OBIS Code: " + it.getLogicalName() + 
                        " Type: " + it.getObjectType());                
            }
        }
    }
    
    /** 
     Parses the COSEM objects of the received data.

     @param data Received data, from the device, as byte array. 
     @return Collection of COSEM objects.
    */
    public final GXDLMSObjectCollection parseObjects(byte[] data, boolean onlyKnownObjects)
    {
        if (data == null)
        {
            throw new GXDLMSException("Invalid parameter.");
        }
        GXDLMSObjectCollection objects;
        if (getUseLogicalNameReferencing())
        {
            objects = parseLNObjects(data, onlyKnownObjects);
        }
        else
        {
            objects = parseSNObjects(data, onlyKnownObjects);
        }
        updateOBISCodes(objects);
        Objects = objects;
        return objects;
    }
        
        /** 
     Parses the COSEM objects of the received data.

     @param data Received data, from the device, as byte array. 
     @return Collection of COSEM objects.
    */
    public final GXDLMSObjectCollection parseObjects(java.nio.ByteBuffer data, boolean onlyKnownObjects)
    {
        return parseObjects(data.array(), onlyKnownObjects);
    }

    /** 
     Reserved for internal use.
    */
    private GXDLMSObjectCollection parseLNObjects(byte[] buff, boolean onlyKnownObjects)
    {
        int[] index = new int[1];
        //Get array tag.
        byte size = buff[index[0]++];
        //Check that data is in the array
        if (size != 0x01)
        {
            throw new GXDLMSException("Invalid response.");
        }
        GXDLMSObjectCollection items = new GXDLMSObjectCollection(this);        
        long cnt = GXCommon.getObjectCount(buff, index);
        int[] total = new int[1], count = new int[1];
        for (long objPos = 0; objPos != cnt; ++objPos)
        {
            //Some meters give wrong item count.
            if (index[0] == buff.length)
            {
                break;
            }
            DataType[] type = new DataType[]{DataType.NONE};
            int[] cachePosition = new int[1];
            Object[] objects = (Object[])GXCommon.getData(buff, index, 
                    ActionType.NONE.getValue(), total, count, type, cachePosition);
            if (index[0] == -1)
            {
                throw new OutOfMemoryError();
            }
            if (objects.length != 4)
            {
                throw new GXDLMSException("Invalid structure format.");
            }
            int classID = ((Number)(objects[0])).intValue() & 0xFFFF;
            if (classID > 0)
            {
                GXDLMSObject comp = createDLMSObject(classID, objects[1], 0, 
                        objects[2], objects[3]);
                if (!onlyKnownObjects || comp.getClass() != GXDLMSObject.class)
                {
                    items.add(comp);
                }
                else
                {
                    System.out.println(String.format("Unknown object : %d %s", 
                            classID, GXDLMSObject.toLogicalName((byte[]) objects[2])));                    
                }
            }
        }
        return items;
    }

    public final List<AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>> parseColumns(ByteArrayOutputStream data)
    {        
        return parseColumns(data.toByteArray());
    }

    /** 
     Parse data columns fro the byte stream.
    */
    public final List<AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>> parseColumns(byte[] data)
    {
        if (data == null)
        {
            throw new GXDLMSException("Invalid parameter.");
        }
        int[] index = new int[1];
        byte size = data[index[0]++];
        //Check that data is in the array.
        if (size != 0x01)
        {
            throw new GXDLMSException("Invalid response.");
        }
        //get object count
        int cnt = GXCommon.getObjectCount(data, index);
        int objectCnt = 0;
        List<AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>> items = 
                new ArrayList<AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>>();        
        int[] total = new int[1], count = new int[1];
        GXDLMSObjectCollection objects2 = new GXDLMSObjectCollection();
        while (index[0] != data.length && cnt != objectCnt)
        {
            DataType[] type = new DataType[]{DataType.NONE};
            int[] cachePosition = new int[1];
            Object[] objects = (Object[])GXCommon.getData(data, index, 
                    ActionType.NONE.getValue(), total, count, type, cachePosition);
            if (index[0] == -1)
            {
                throw new OutOfMemoryError();
            }
            if (objects.length != 4)
            {
                throw new GXDLMSException("Invalid structure format.");
            }
            ++objectCnt;
            GXDLMSObject comp = createDLMSObject(((Number)objects[0]).shortValue(), 
                    null, 0, objects[1], 0);
            if (comp != null)
            {
                GXDLMSCaptureObject co = new GXDLMSCaptureObject(((Number)objects[2]).shortValue(), ((Number)objects[3]).shortValue());                
                items.add(new AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(comp, co));
                objects2.add(comp);
                //Update data type and scaler unit if register.
                if (Objects != null)
                {
                    GXDLMSObject tmp = Objects.findByLN(comp.getObjectType(), comp.getLogicalName());
                    if (tmp != null)
                    {
                        if (comp instanceof GXDLMSRegister)
                        {
                            int index2 = co.getAttributeIndex();
                            //Some meters return zero.
                            if (index2 == 0)
                            {
                                index2 = 2;
                            }
                            comp.setUIDataType(index2, tmp.getUIDataType(index2));
                            ((GXDLMSRegister) comp).setScaler(((GXDLMSRegister) tmp).getScaler());
                            ((GXDLMSRegister) comp).setUnit(((GXDLMSRegister) tmp).getUnit());
                        }
                    }
                }
            }            
        }
        updateOBISCodes(objects2);
        return items;
    }

    /*
     * Get Value from byte array received from the meter.
     */
    public final Object updateValue(byte[] data, GXDLMSObject target, int attributeIndex) throws Exception
    {
        Object value = getValue(data, target, attributeIndex);
        target.setValue(attributeIndex, value);        
        return target.getValues()[attributeIndex - 1];
    }

    
    /*
     * Get Value from byte array received from the meter.
     */
    public final Object getValue(byte[] data, GXDLMSObject target, int attributeIndex) throws Exception
    {        
        Object value = getValue(data);
        if (value instanceof byte[])
        {
            DataType type = target.getUIDataType(attributeIndex);
            if (type == DataType.NONE)
            {
                return value;
            }
            return GXDLMSClient.changeType((byte[]) value, type);
        }            
        return value;
    }
    
    /*
     * Get Value from byte array received from the meter.
     */
    public final Object getValue(ByteArrayOutputStream data, GXDLMSObject target, int attributeIndex) throws Exception
    {
        return getValue(data.toByteArray(), target.getObjectType(), target.getLogicalName(), attributeIndex);
    }
    
    /** 
     Get Value from byte array received from the meter.
    */
    public final Object getValue(byte[] data, ObjectType type, String ln, int attributeIndex) throws Exception
    {
        Object value = getValue(data);
        if (value instanceof byte[])
        {
            //Get Logican name.
            if (attributeIndex == 1)
            {
                return GXDLMSClient.changeType((byte[]) value, DataType.OCTET_STRING);
            }
            else if((type == ObjectType.CLOCK && attributeIndex == 2) ||
                        (type == ObjectType.EXTENDED_REGISTER && attributeIndex == 5))
            {
                return changeType((byte[])value, DataType.DATETIME);
            }       
            if (getObisCodes() != null)
            {
                GXObisCode code = getObisCodes().findByLN(type, ln, null);
                if (code != null)
                {
                    GXDLMSAttributeSettings att = code.getAttributes().find(attributeIndex);
                    if (att != null && ((byte[])value).length != 0)
                    {
                        return changeType((byte[])value, att.getUIType());
                    }
                }
            }            
        }
        return value;
    }

        /** 
     Get Value from byte array received from the meter.

     @param data Byte array received from the meter.
     @return Received data.
    */
    public final Object getValue(ByteArrayOutputStream data)
    {
        return getValue(data.toByteArray());
    }    
            
    /** 
     Get Value from byte array received from the meter.

     @param data Byte array received from the meter.
     @return Received data.
    */
    public final Object getValue(byte[] data)
    {
        if (!getUseCache() || data.length < m_Base.cacheIndex)
        {
            m_Base.clearProgress();
        }
        //Return cached items.
        if (m_Base.useCache(data))
        {
            return m_Base.cacheData;
        }
        DataType[] type = new DataType[1];
        Object[] value = new Object[1];
        m_Base.parseReplyData(getUseCache() ? ActionType.INDEX : ActionType.NONE, data, value, type);
        return m_Base.cacheData;
    }

    /*
     Update list values.
    */
    public void updateValues(List<AbstractMap.SimpleEntry<GXDLMSObject, Integer>> list, byte[] data)
    {
        Object value;    
        DataType[] type = new DataType[]{DataType.NONE};
        int[] read = new int[1], total = new int[1], 
                index = new int[1], lastIndex = new int[1];
        for(AbstractMap.SimpleEntry<GXDLMSObject, Integer> it : list)
        {
            type[0] = DataType.NONE;
            if (index[0] != 0)
            {
                //Check status.
                if (data[index[0]] != 0)
                {
                    throw new GXDLMSException(data[index[0]]);
                }
                //Skip status code.
                ++index[0];
            }
            lastIndex[0] = 0;
            value = GXCommon.getData(data, index, ActionType.NONE.getValue(), 
                    total, read, type, lastIndex);
            it.getKey().setValue(it.getValue(), value);
        }            
    }

    /** 
     TryGetValue try parse multirow value from byte array to variant.

     This method can be used when Profile Generic is read and if 
     data is need to update at collection time.
     Cached data is cleared after read.

     @param data Byte array received from the meter.        
     @return Received data.
     @see GXDLMSClient#getUseCache
    */
    public final Object tryGetValue(byte[] data)
    {
        if (!getUseCache() || data.length < m_Base.cacheIndex)
        {
            m_Base.clearProgress();
        }
        DataType[] type = new DataType[]{DataType.NONE};
        int[] read = new int[1], total = new int[1], index = new int[1];
        try
        {
            //Return cached items.
            if (getUseCache())
            {
                if (m_Base.cacheSize == data.length)
                {
                    //Clear cached data after read.
                    Object tmp = m_Base.cacheData;
                    m_Base.cacheData = null;
                    return tmp;
                }
                if (m_Base.cacheData != null)
                {
                    throw new RuntimeException("Cache data is not empty.");
                }
            }
            int[] cachePosition = new int[] {m_Base.cacheIndex};
            Object value = GXCommon.getData(data, index, ActionType.NONE.getValue(), 
                    total, read, type, cachePosition);
            m_Base.cacheIndex = cachePosition[0];
            if (getUseCache())
            {
                m_Base.cacheData = null;
                m_Base.itemCount += read[0];
                m_Base.cacheSize = data.length;
                m_Base.maxItemCount += total[0];
            }
            return value;
        }
        catch (RuntimeException ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /** 
     Changes byte array received from the meter to given type.

     @param value Byte array received from the meter.
     @param type Wanted type.
     @return Value changed by type.
    */
    public static Object changeType(byte[] value, DataType type)
    {
        if (value == null)
        {
            return null;
        }
        if (type == DataType.NONE)
        {
            return GXCommon.toHex(value);
        }
        if (value.length == 0 && (type == DataType.STRING || type == DataType.OCTET_STRING))
        {
            return "";
        }
        int[] total = new int[1], count = new int[1], index = new int[]{0};
        DataType[] tp = new DataType[]{type};
        int[] cachePosition = new int[1];
        Object ret = GXCommon.getData(value, index, ActionType.NONE.getValue(), 
                total, count, tp, cachePosition);
        if (index[0] == -1)
        {
            System.out.println(GXCommon.toHex(value));
            throw new OutOfMemoryError();
        }
        if (type == DataType.OCTET_STRING && ret instanceof byte[])
        {
            String str;
            byte[] arr = (byte[])ret;
            if (arr.length == 0)
            {
                str = "";
            }
            else
            {
                StringBuilder bcd = new StringBuilder(arr.length * 4);
                for (int it : arr)
                {
                    if (bcd.length() != 0)
                    {
                        bcd.append(".");
                    }
                    bcd.append(String.format("%d", it & 0xFF));
                }
                str = bcd.toString();
            }
            return str;
        }
        return ret;
    }

     /** 
     Reads the Association view from the device.

     This method is used to get all objects in the device.

     @return Read request, as byte array.
     */
    public final byte[] getObjectsRequest()
    {
        Object name;
        if (getUseLogicalNameReferencing())
        {
            name = "0.0.40.0.0.255";
        }
        else
        {
            name = (short)0xFA00;
        }
        return read(name, ObjectType.ASSOCIATION_LOGICAL_NAME, 2)[0];
    }

    /** 
     Generate Method (Action) request.

     @param name Method object short name or Logical Name.
     @param objectType Object type.
     @param data Methdod data.
     * @param index Methdod index.
     @return DLMS action message.
    */
     public final byte[][] method(GXDLMSObject item, int index, Object data, DataType type)
    {
        return method(item.getName(), item.getObjectType(), index, data, 1, type);         
    }
     
      /** 
     Generate Method (Action) request..

     @param name Method object short name or Logical Name.
     @param objectType Object type.
     @param data Method data.
     @param index Method index.
     @return DLMS action message.
    */    
    public final byte[][] method(Object name, ObjectType objectType, int index, Object data, int parameterCount, DataType type)
    {
        if (name == null || index < 1)
        {
            throw new IllegalArgumentException("Invalid parameter");
        }
        m_Base.clearProgress();
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        if (!getUseLogicalNameReferencing())
        {
            int[] value = new int[1], count = new int[1];
            GXDLMS.getActionInfo(objectType, value, count);
            if (index > count[0])
            {
                throw new IllegalArgumentException("methodIndex");
            }
            index = (value[0] + (index - 1) * 0x8);           
            name = ((Number)name).intValue() + index;
            index = 0;
            if (data != null)
            {
                //Add parameter count.
                buff.write((byte) parameterCount);
            }
        }
        GXCommon.setData(buff, type, data);        
        Command cmd;
        if (getUseLogicalNameReferencing())
        {
            cmd = Command.MethodRequest;
        }
        else
        {
            cmd = Command.ReadRequest;
        } 
        return m_Base.generateMessage(name, buff.toByteArray(), objectType, index, cmd);
    }
   
    /*
     * Generates a write message.
     */
    public final byte[][] write(GXDLMSObject item, 
            int index)
            throws RuntimeException, UnsupportedEncodingException, ParseException
    {        
        Object value = item.getValue(index, 0, null);
        DataType type = item.getDataType(index);
        return write(item.getName(), value, type, item.getObjectType(), index);
    }
    
    /** 
     Generates a write message.

     @param name Short or Logical Name.
     @param value Data to Write.
     @param type Data type of write object.
     @param objectType
     @param index Attribute index where data is write.
     @return DLMS write message.
    */
    public final byte[][] write(Object name, Object value, DataType type, 
            ObjectType objectType, int index)
            throws RuntimeException, UnsupportedEncodingException, ParseException
    {
        if (index < 1)
        {
            throw new GXDLMSException("Invalid parameter");
        }
        if (type == DataType.NONE && value != null)
        {
            type = GXCommon.getValueType(value);
            if (type == DataType.NONE)
            {
                throw new GXDLMSException("Invalid parameter. In java value type must give.");
            }
        }
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        GXCommon.setData(buff, type, value);
        m_Base.clearProgress();
        return m_Base.generateMessage(name, buff.toByteArray(), objectType, index, 
                this.getUseLogicalNameReferencing() ? Command.SetRequest : 
                Command.WriteRequest);
    }

    /** 
     Generates a read message.

     @param name Short or Logical Name.
     @param objectType Read Interface.
     @param attributeOrdinal Read attribute index.
     @return Read request as byte array.
    */
    public final byte[][] read(Object name, ObjectType objectType, int attributeOrdinal)
    {
        if ((attributeOrdinal < 0))
        {
            throw new GXDLMSException("Invalid parameter");
        }
        //Clear cache
        m_Base.clearProgress();
        return m_Base.generateMessage(name, new byte[0], objectType, attributeOrdinal, this.getUseLogicalNameReferencing() ? Command.GetRequest : Command.ReadRequest);
    }

    /** 
     Generates a read message.

     @param item DLMS object to read.
     @param attributeOrdinal Read attribute index.
     @return Read request as byte array.
    */
    public final byte[][] read(GXDLMSObject item, int attributeOrdinal)
    {
        if ((attributeOrdinal < 1))
        {
            throw new GXDLMSException("Invalid parameter");
        }
        //Clear cache
        m_Base.clearProgress();
        return m_Base.generateMessage(item.getName(), new byte[0], item.getObjectType(), attributeOrdinal, 
            this.getUseLogicalNameReferencing() ? Command.GetRequest : Command.ReadRequest);
    }

    /** 
     Read list of COSEM objects.

     @param list DLMS objects to read.
     @return Read request as byte array.
    */
    public final byte[][] readList(List<AbstractMap.SimpleEntry<GXDLMSObject, Integer>> list)
    {
        if (list == null || list.isEmpty())
        {
            throw new RuntimeException("Invalid parameter.");
        }
        if (getUseLogicalNameReferencing() && list.size() > 10)
        {
            throw new RuntimeException("Max 10 items can be read at the time.");
        }
        //Clear cache
        m_Base.clearProgress();
        return m_Base.generateMessage(list, new byte[0], ObjectType.NONE, 0, 
            this.getUseLogicalNameReferencing() ? Command.GetRequest : Command.ReadRequest);
    }
    /** 
     Generates the keep alive message. 

     Keep alive message is sent to keep the connection to the device alive.

     @return Returns Keep alive message, as byte array.
    */
    public final byte[] keepAlive()
    {
        m_Base.clearProgress();
        //There is no keepalive in IEC 62056-47.
        if (this.getInterfaceType() == InterfaceType.NET)
        {
            return new byte[0];
        }
        return m_Base.addFrame(m_Base.generateAliveFrame(), false, (byte[]) null, 0, 0);
    }

    /** 
     Read rows by entry.

     @param name object name.
     @param index Zero bases start index.
     @param count Rows count to read.
     @return Read message as byte array.
    */
    public final byte[] readRowsByEntry(Object name, int index, int count)
    {
        m_Base.clearProgress();
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(19);
        buff.put((byte) 0x02); //Add AccessSelector
        buff.put((byte)DataType.STRUCTURE.getValue()); //Add enum tag.
        buff.put((byte) 0x04); //Add item count
        GXCommon.setData(buff, DataType.UINT32, index); //Add start index
        GXCommon.setData(buff, DataType.UINT32, count); //Add Count
        GXCommon.setData(buff, DataType.UINT16, 1); //Read all columns.
        GXCommon.setData(buff, DataType.UINT16, 0);
        return m_Base.generateMessage(name, buff.array(), 
                ObjectType.PROFILE_GENERIC, 2, 
                this.getUseLogicalNameReferencing() ? 
                Command.GetRequest : Command.ReadRequest)[0];
    }
    
    /** 
     Read rows by range.
      
     Use this method to read Profile Generic table between dates.
     @param name object name.
     @param sortedLn The logical name of the sorted object.
     @param sortedObjectType The ObjectType of the sorted object.
     @param sortedVersion The version of the sorted object.
     @param start Start time.
     @param end End time.
    */
    public final byte[] readRowsByRange(Object name, String sortedLn, 
            ObjectType sortedObjectType, int sortedVersion, 
            java.util.Date start, java.util.Date end)
            throws RuntimeException, UnsupportedEncodingException, ParseException
    {                
        m_Base.clearProgress();
        java.nio.ByteBuffer buff = java.nio.ByteBuffer.allocate(100);
        buff.put((byte) 0x01); //Add AccessSelector
        buff.put((byte)DataType.STRUCTURE.getValue()); //Add enum tag.
        buff.put((byte) 0x04); //Add item count
        buff.put((byte) 0x02); //Add enum tag.
        buff.put((byte) 0x04); //Add item count
        GXCommon.setData(buff, DataType.UINT16, (short)8); // Add class_id
        GXCommon.setData(buff, DataType.OCTET_STRING, sortedLn); // Add parameter Logical name
        GXCommon.setData(buff, DataType.INT8, 2);// Add attribute index.
        GXCommon.setData(buff, DataType.UINT16, sortedVersion); // Add version
        GXCommon.setData(buff, DataType.DATETIME, start); // Add start time
        GXCommon.setData(buff, DataType.DATETIME, end); // Add start time
        //Add array of read columns. Read All...
        buff.put((byte) 0x01); //Add item count
        buff.put((byte) 0x00); //Add item count
        byte[] tmp = new byte[buff.position()];        
        buff.position(0);
        buff.get(tmp);
        return m_Base.generateMessage(name, tmp, 
                ObjectType.PROFILE_GENERIC, 2, 
                this.getUseLogicalNameReferencing() ? 
                Command.GetRequest : Command.ReadRequest)[0];
    }

    /*
     * Create object by interface type.
     */
    static public GXDLMSObject createObject(ObjectType type)
    {
        return GXDLMS.createObject(type);
    }

    /** 
     Determines, whether the DLMS packet is completed.

     @param data The data to be parsed, to complete the DLMS packet.
     @return True, when the DLMS packet is completed.
    */
    public final boolean isDLMSPacketComplete(byte[] data)
    {
        return m_Base.isDLMSPacketComplete(data);
    }

    public final Object[][] checkReplyErrors(byte[] sendData, 
            byte[] receivedData) throws Exception
    {
        return m_Base.checkReplyErrors(sendData, receivedData);
    }

    /** 
     Generates an acknowledgment message, with which the server is informed to 
     send next packets.

     @param type Frame type
     @return Acknowledgment message as byte array.
    */
    public final byte[] receiverReady(RequestTypes type)
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
     This method is used to solve current index of received DLMS packet, 
     by retrieving the current progress status.

     @param data DLMS data to parse.
     @return The current index of the packet.
    */
    public final int getCurrentProgressStatus(ByteArrayOutputStream data)
    {
        return m_Base.getCurrentProgressStatus(data.toByteArray());
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
     This method is used to solve the total amount of received items,
     by retrieving the maximum progress status.

     @param data DLMS data to parse.
     @return Total amount of received items.
    */
    public final int getMaxProgressStatus(ByteArrayOutputStream data)
    {
        return m_Base.getMaxProgressStatus(data.toByteArray());
    }

     /** 
     Removes the HDLC header from the packet, and returns COSEM data only.

     @param packet The received packet, from the device, as byte array.
     @param data The exported data.
     @return COSEM data.
    */
    public final java.util.Set<RequestTypes> getDataFromPacket(byte[] packet, 
            ByteArrayOutputStream data)
    {
        byte[] frame = new byte[1];
        int[] command = new int[1];
        return m_Base.getDataFromPacket(packet, data, frame, command);
    }      
}