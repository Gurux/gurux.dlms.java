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

package gurux.dlms.objects;

import gurux.dlms.Command;
import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.manufacturersettings.GXAttributeCollection;
import gurux.dlms.manufacturersettings.GXAuthentication;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSAssociationLogicalName extends GXDLMSObject implements IGXDLMSBase
{    
    GXDLMSObjectCollection m_ObjectList;
    short ClientSAP;
    short ServerSAP;                
    GXApplicationContextName ApplicationContextName;
    GXxDLMSContextType XDLMSContextInfo;
    GXAuthenticationMechanismName AuthenticationMechanismMame;
    byte[] Secret;    
    AssociationStatus m_AssociationStatus = AssociationStatus.NonAssociated;
    String SecuritySetupReference;

    /**  
     Constructor.
    */
    public GXDLMSAssociationLogicalName()
    {
        this("0.0.40.0.0.255");
        ApplicationContextName = new GXApplicationContextName();
        XDLMSContextInfo = new GXxDLMSContextType();
        AuthenticationMechanismMame = new GXAuthenticationMechanismName();
    }

     /**  
     Constructor.

     @param ln Logical Name of the object.
     */
    public GXDLMSAssociationLogicalName(String ln)
    {
        super(ObjectType.ASSOCIATION_LOGICAL_NAME, ln, 0);
        setLogicalName(ln);
        m_ObjectList = new GXDLMSObjectCollection(this);
        ApplicationContextName = new GXApplicationContextName();
        XDLMSContextInfo = new GXxDLMSContextType();
        AuthenticationMechanismMame = new GXAuthenticationMechanismName();
    }

    public final GXDLMSObjectCollection getObjectList()
    {
        return m_ObjectList;
    }
    
    public final void setObjectList(GXDLMSObjectCollection value)
    {
        m_ObjectList = value;
    }
    
    /*
     * Contains the identifiers of the COSEM client APs within the physical devices hosting these APs, 
     * which belong to the AA modelled by the “Association LN” object.
     */
    public final short getClientSAP()
    {
        return ClientSAP;
    }
    public final void setClientSAP(short value)
    {
        ClientSAP = value;
    }
    
    /*
     * Contains the identifiers of the COSEM server (logical device) APs within the physical 
     * devices hosting these APs, which belong to the AA modelled by the “Association LN” object.
     */
    public final short getServerSAP()
    {
        return ServerSAP;
    }
    public final void setServerSAP(short value)
    {
        ServerSAP = value;
    }
    
    public final GXApplicationContextName getApplicationContextName()
    {
        return ApplicationContextName;
    }

    public final GXxDLMSContextType getXDLMSContextInfo()
    {
        return XDLMSContextInfo;
    }

    public final GXAuthenticationMechanismName getAuthenticationMechanismMame()
    {
        return AuthenticationMechanismMame;
    }

    public final byte[] getSecret()
    {
        return Secret;
    }
    public final void setSecret(byte[] value)
    {
        Secret = value;
    }

    public final AssociationStatus getAssociationStatus()
    {
        return m_AssociationStatus;
    }
    public final void setAssociationStatus(AssociationStatus value)
    {
        m_AssociationStatus = value;
    }
    
    public final String getSecuritySetupReference()
    {
        return SecuritySetupReference;
    }
    public final void setSecuritySetupReference(String value)
    {
        SecuritySetupReference = value;
    }
       
    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getObjectList(), 
            ClientSAP + "/" + ServerSAP, getApplicationContextName(), 
            getXDLMSContextInfo(), getAuthenticationMechanismMame(), 
            getSecret(), getAssociationStatus(), getSecuritySetupReference()};
    }
    
    /*
    * Invokes method.
    * 
     @param index Method index.
    */
    @Override
    public byte[][] invoke(Object sender, int index, Object parameters)
    {
        //Check reply_to_HLS_authentication
        if (index == 1)
        {
            GXDLMSServerBase s = (GXDLMSServerBase) sender;
            if (s == null)
            {
                throw new IllegalArgumentException("sender");
            }
            //Get server Challenge.
            ByteArrayOutputStream challenge = new ByteArrayOutputStream();
            ByteArrayOutputStream CtoS = new ByteArrayOutputStream();
            //Find shared secret
            for(GXAuthentication it : s.getAuthentications())
            {
                if (it.getType() == s.getAuthentication())
                {
                    try 
                    {
                        CtoS.write(it.getPassword());
                        challenge.write(it.getPassword());
                        challenge.write(s.getStoCChallenge());
                    }
                    catch (IOException ex) 
                    {
                        throw new RuntimeException(ex.getMessage());
                    }
                    break;
                }
            }            
            byte[] serverChallenge = GXDLMSServerBase.chipher(s.getAuthentication(), 
                    challenge.toByteArray());
            byte[] clientChallenge = (byte[])parameters;
            int[] pos = new int[1];
            if (GXCommon.compare(serverChallenge, pos, clientChallenge))
            {
                try 
                {
                    CtoS.write(s.getCtoSChallenge());                    
                } 
                catch (IOException ex) 
                {
                    throw new RuntimeException(ex.getMessage());
                }
                return s.acknowledge(Command.MethodResponse, 0, 
                        GXDLMSServerBase.chipher(s.getAuthentication(), CtoS.toByteArray()), 
                        DataType.OCTET_STRING);
            }
            else
            {
                //Return error.
                return s.serverReportError(Command.ReadRequest, 5);
            }            
        }
        else
        {
            throw new IllegalArgumentException("Invoke failed. Invalid attribute index.");
        }
    }
    
    /*
     * Returns collection of attributes to read.
     * 
     * If attribute is static and already read or device is returned HW error it is not returned.
     */
    @Override
    public int[] getAttributeIndexToRead()
    {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        //LN is static and read only once.
        if (LogicalName == null || LogicalName.compareTo("") == 0)
        {
            attributes.add(1);
        }        
        //ObjectList is static and read only once.
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //associated_partners_id is static and read only once.
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //Application Context Name is static and read only once.
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //xDLMS Context Info
        if (!isRead(5))
        {
            attributes.add(5);
        }
        // Authentication Mechanism Name
        if (!isRead(6))
        {
            attributes.add(6);
        }
        // Secret
        if (!isRead(7))
        {
            attributes.add(7);
        }
        // Association Status
        if (!isRead(8))
        {
            attributes.add(8);
        }
        //Security Setup Reference is from version 1.
        if (getVersion() > 0 && !isRead(9))
        {
            attributes.add(9);
        }
        return toIntArray(attributes);
    }
    
    @Override
    public int getAttributeCount()
    {
        //Security Setup Reference is from version 1.
        if (getVersion() > 0)
        {
            return 9;
        }
        return 8;
    }
     
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 4;    
    }
    
     /** 
     Returns Association View.    
    */
    private byte[] getObjects()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write((byte)DataType.ARRAY.getValue());
        boolean lnExists = m_ObjectList.findByLN(ObjectType.ASSOCIATION_LOGICAL_NAME, this.getLogicalName()) != null;
        try
        {
            //Add count        
            int cnt = m_ObjectList.size();
            if (!lnExists)
            {
                ++cnt;
            }
            GXCommon.setObjectCount(cnt, stream);
            for (GXDLMSObject it : m_ObjectList)
            {
                stream.write((byte) DataType.STRUCTURE.getValue());
                stream.write((byte) 4); //Count
                GXCommon.setData(stream, DataType.UINT16, it.getObjectType().getValue()); //ClassID
                GXCommon.setData(stream, DataType.UINT8, it.getVersion()); //Version
                GXCommon.setData(stream, DataType.OCTET_STRING, it.getLogicalName()); //LN
                getAccessRights(it, stream); //Access rights.
            }
            if (!lnExists)
            {
                stream.write((byte) DataType.STRUCTURE.getValue());
                stream.write((byte) 4); //Count
                GXCommon.setData(stream, DataType.UINT16, this.getObjectType().getValue()); //ClassID
                GXCommon.setData(stream, DataType.UINT8, this.getVersion()); //Version
                GXCommon.setData(stream, DataType.OCTET_STRING, this.getLogicalName()); //LN
                getAccessRights(this, stream); //Access rights.
            }
            return stream.toByteArray();
        }
        catch(Exception ex)
        {
            Logger.getLogger(GXDLMSRegisterMonitor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }        
    }
    
    private void getAccessRights(GXDLMSObject item, ByteArrayOutputStream data) 
            throws RuntimeException, UnsupportedEncodingException, 
            ParseException, IOException
    {        
        data.write((byte)DataType.STRUCTURE.getValue());
        data.write((byte) 2);
        data.write((byte) DataType.ARRAY.getValue());
        GXAttributeCollection attributes = item.getAttributes();        
        int cnt = item.getAttributeCount();
        data.write((byte) cnt);        
        for (int pos = 0; pos != cnt; ++pos)            
        {
            GXDLMSAttributeSettings att = attributes.find(pos + 1);
            data.write((byte)DataType.STRUCTURE.getValue()); //attribute_access_item
            data.write((byte)3);
            GXCommon.setData(data, DataType.INT8, pos + 1);
            ///If attribute is not set return read only.
            if (att == null)
            {
                GXCommon.setData(data, DataType.ENUM, AccessMode.READ.getValue());
            }
            else
            {
                GXCommon.setData(data, DataType.ENUM, att.getAccess().getValue());
            }
            GXCommon.setData(data, DataType.NONE, null);
        }
        data.write((byte)DataType.ARRAY.getValue());
        attributes = item.getMethodAttributes();
        cnt = item.getMethodCount();
        data.write((byte)cnt); 
        for (int pos = 0; pos != cnt; ++pos)            
        {
            GXDLMSAttributeSettings att = attributes.find(pos + 1);
            data.write((byte)DataType.STRUCTURE.getValue()); //attribute_access_item
            data.write((byte)2);
            GXCommon.setData(data, DataType.INT8, pos + 1);
             ///If method attribute is not set return no access.
            if (att == null)
            {
                GXCommon.setData(data, DataType.ENUM, MethodAccessMode.NO_ACCESS.getValue());
            }
            else
            {
                GXCommon.setData(data, DataType.ENUM, att.getMethodAccess().getValue());                
            }
        }        
    }         

    void updateAccessRights(GXDLMSObject obj, Object[] buff)
    {        
        for(Object access : (Object[]) Array.get(buff, 0))
        {
            Object[] attributeAccess = (Object[]) access;
            int id = GXCommon.intValue(attributeAccess[0]);
            int tmp = GXCommon.intValue(attributeAccess[1]);
            AccessMode aMode = AccessMode.forValue(tmp);        
            obj.setAccess(id, aMode);
        }        
        for(Object access : (Object[]) Array.get(buff, 1))
        {
            Object[] methodAccess = (Object[]) access;
            int id = GXCommon.intValue(methodAccess[0]);
            int tmp;
            //If version is 0.
            if (methodAccess[1] instanceof Boolean)
            {   
                if (((Boolean) methodAccess[1]).booleanValue())
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
                tmp = GXCommon.intValue(methodAccess[1]);
            }
            MethodAccessMode mode = MethodAccessMode.forValue(tmp);
            obj.setMethodAccess(id, mode);
        }
    }
    
    @Override
    public DataType getDataType(int index)
    {
        if (index == 1)
        {
            return DataType.OCTET_STRING;
        }        
        if (index == 2)
        {
            return DataType.ARRAY;
        }
        if (index == 3)
        {
            return DataType.STRUCTURE;
        }
        if (index == 4)
        {
            return DataType.STRUCTURE;
        }
        if (index == 5)
        {
            return DataType.STRUCTURE;
        }
        if (index == 6)
        {
            return DataType.STRUCTURE;
        }
        if (index == 7)
        {
            return DataType.OCTET_STRING;
        }
        if (index == 8)
        {
            return DataType.ENUM;
        }
        if (index == 9)
        {
            return DataType.OCTET_STRING;
        } 
        throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
    }
    
     /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, int selector, Object parameters)
    {
        if (index == 1)
        {
            return getLogicalName();
        }        
        if (index == 2)
        {
            return getObjects();
        }
        if (index == 3)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.ARRAY.getValue());       
            //Add count            
            data.write(2);
            data.write((byte)DataType.UINT8.getValue());
            data.write(ClientSAP);
            data.write((byte)DataType.UINT16.getValue());           
            data.write(ServerSAP >> 8);
            data.write(ServerSAP & 0xFF);
            return data.toByteArray();                            
        }
        if (index == 4)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.STRUCTURE.getValue());       
            //Add count            
            data.write(0x7);
            try
            {
                GXCommon.setData(data, DataType.UINT8, ApplicationContextName.getJointIsoCtt());
                GXCommon.setData(data, DataType.UINT8, ApplicationContextName.getCountry());
                GXCommon.setData(data, DataType.UINT16, ApplicationContextName.getCountryName());
                GXCommon.setData(data, DataType.UINT8, ApplicationContextName.getIdentifiedOrganization());
                GXCommon.setData(data, DataType.UINT8, ApplicationContextName.getDlmsUA());
                GXCommon.setData(data, DataType.UINT8, ApplicationContextName.getApplicationContext());
                GXCommon.setData(data, DataType.UINT8, ApplicationContextName.getContextId());
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
            return data.toByteArray();
        }
        if (index == 5)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.STRUCTURE.getValue());       
            data.write(6);
            try
            {
                GXCommon.setData(data, DataType.BITSTRING, XDLMSContextInfo.getConformance());
                GXCommon.setData(data, DataType.UINT16, XDLMSContextInfo.getMaxReceivePduSize());
                GXCommon.setData(data, DataType.UINT16, XDLMSContextInfo.getMaxSendPpuSize());
                GXCommon.setData(data, DataType.UINT8, XDLMSContextInfo.getDlmsVersionNumber());
                GXCommon.setData(data, DataType.INT8, XDLMSContextInfo.getQualityOfService());
                GXCommon.setData(data, DataType.OCTET_STRING, XDLMSContextInfo.getCypheringInfo());
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
            return data.toByteArray();
        }
        if (index == 6)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte)DataType.STRUCTURE.getValue());       
            //Add count            
            data.write(0x7);
            try
            {
                GXCommon.setData(data, DataType.UINT8, AuthenticationMechanismMame.getJointIsoCtt());
                GXCommon.setData(data, DataType.UINT8, AuthenticationMechanismMame.getCountry());
                GXCommon.setData(data, DataType.UINT16, AuthenticationMechanismMame.getCountryName());
                GXCommon.setData(data, DataType.UINT8, AuthenticationMechanismMame.getIdentifiedOrganization());
                GXCommon.setData(data, DataType.UINT8, AuthenticationMechanismMame.getDlmsUA());
                GXCommon.setData(data, DataType.UINT8, AuthenticationMechanismMame.getAuthenticationMechanismName());
                GXCommon.setData(data, DataType.UINT8, AuthenticationMechanismMame.getMechanismId().getValue());
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
            return data.toByteArray();
        }
        if (index == 7)
        {
            return Secret;
        }
        if (index == 8)
        {
            return getAssociationStatus().ordinal();
        }
        if (index == 9)
        {
            return getSecuritySetupReference();
        }
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
    
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value)
    {
        if (index == 1)
        {
            super.setValue(index, value);            
        }          
        else if (index == 2)
        {
            m_ObjectList.clear();
            if (value != null)
            {
                for(Object item : (Object[])value)
                {                    
                    ObjectType type = ObjectType.forValue(((Number)Array.get(item, 0)).intValue());
                    int version = ((Number)Array.get(item, 1)).intValue();
                    String ln = GXDLMSObject.toLogicalName((byte[]) Array.get(item, 2));
                    GXDLMSObject obj = getParent().findByLN(type, ln);
                    if (obj == null)
                    {
                        obj = gurux.dlms.GXDLMSClient.createObject(type);
                        obj.setLogicalName(ln);
                        obj.setVersion(version);                        
                    }
                    //Add only known objects.
                    if (obj instanceof IGXDLMSBase)
                    {
                        updateAccessRights(obj, (Object[]) Array.get(item, 3));
                        m_ObjectList.add(obj);
                    }
                }               
            }
        }
        else if (index == 3)
        {            
            if (value != null)
            {
                ClientSAP = (short) GXCommon.intValue(Array.get(value, 0));
                ServerSAP = (short) GXCommon.intValue(Array.get(value, 1));
            }
        }
        else if (index == 4)
        {
            //Value of the object identifier encoded in BER
            if (value instanceof byte[])
            {                    
                int pos = -1;
                byte[] arr = (byte[]) value;
                if (arr[0] == 0x60)
                {
                    ApplicationContextName.setJointIsoCtt(0);
                    ++pos;                        
                    ApplicationContextName.setCountry(0);
                    ++pos;
                    ApplicationContextName.setCountryName(0);
                    ++pos;
                    ApplicationContextName.setIdentifiedOrganization(arr[++pos]);
                    ApplicationContextName.setDlmsUA(arr[++pos]);
                    ApplicationContextName.setApplicationContext(arr[++pos]);
                    ApplicationContextName.setContextId(arr[++pos]);
                }
                else
                {
                    //Get Tag and Len.
                    if (arr[++pos] != 2 && arr[++pos] != 7)
                    {
                        throw new IllegalArgumentException();
                    }
                    //Get tag
                    if (arr[++pos] != 0x11)
                    {
                        throw new IllegalArgumentException();
                    }
                    ApplicationContextName.setJointIsoCtt(arr[++pos]);
                    //Get tag
                    if (arr[++pos] != 0x11)
                    {
                        throw new IllegalArgumentException();
                    }
                    ApplicationContextName.setCountry(arr[++pos]);
                    //Get tag
                    if (arr[++pos] != 0x12)
                    {
                        throw new IllegalArgumentException();
                    }
                    int tmp[] = new int[]{pos};
                    ApplicationContextName.setCountryName(GXCommon.getUInt16(arr, tmp));
                    pos = tmp[1];
                    //Get tag
                    if (arr[++pos] != 0x11)
                    {
                        throw new IllegalArgumentException();
                    }
                    ApplicationContextName.setIdentifiedOrganization(arr[++pos]);
                    //Get tag
                    if (arr[++pos] != 0x11)
                    {
                        throw new IllegalArgumentException();
                    }
                    ApplicationContextName.setDlmsUA(arr[++pos]);
                    //Get tag
                    if (arr[++pos] != 0x11)
                    {
                        throw new IllegalArgumentException();
                    }
                    ApplicationContextName.setApplicationContext(arr[++pos]);
                    //Get tag
                    if (arr[++pos] != 0x11)
                    {
                        throw new IllegalArgumentException();
                    }
                    ApplicationContextName.setContextId(arr[++pos]);
                }
            }
            else
            {
                if (value != null)
                {
                    ApplicationContextName.setJointIsoCtt(((Number)Array.get(value, 0)).intValue());
                    ApplicationContextName.setCountry(((Number)Array.get(value, 1)).intValue());
                    ApplicationContextName.setCountryName(((Number)Array.get(value, 2)).intValue());
                    ApplicationContextName.setIdentifiedOrganization(((Number)Array.get(value, 3)).intValue());
                    ApplicationContextName.setDlmsUA(((Number)Array.get(value, 4)).intValue());
                    ApplicationContextName.setApplicationContext(((Number)Array.get(value, 5)).intValue());
                    ApplicationContextName.setContextId(((Number)Array.get(value, 6)).intValue());
                }
            }
        }
        else if (index == 5)
        {
            if (value != null)
            {
                XDLMSContextInfo.setConformance(Array.get(value, 0).toString());
                XDLMSContextInfo.setMaxReceivePduSize(((Number)Array.get(value, 1)).intValue());
                XDLMSContextInfo.setMaxSendPpuSize(((Number)Array.get(value, 2)).intValue());
                XDLMSContextInfo.setDlmsVersionNumber(((Number)Array.get(value, 3)).intValue());
                XDLMSContextInfo.setQualityOfService(((Number)Array.get(value, 4)).intValue());
                XDLMSContextInfo.setCypheringInfo((byte[])Array.get(value, 5));
            }
        }
        else if (index == 6)
        {      
            if (value != null)
            {
                //Value of the object identifier encoded in BER
                if (value instanceof byte[])
                {
                    int pos = -1;
                    byte[] arr = (byte[]) value;
                    if (arr[0] == 0x60)
                    {
                        AuthenticationMechanismMame.setJointIsoCtt(0);
                        ++pos;
                        AuthenticationMechanismMame.setCountry(0);
                        ++pos;
                        AuthenticationMechanismMame.setCountryName(0);
                        ++pos;
                        AuthenticationMechanismMame.setIdentifiedOrganization(arr[++pos]);
                        AuthenticationMechanismMame.setDlmsUA(arr[++pos]);
                        AuthenticationMechanismMame.setAuthenticationMechanismName(arr[++pos]);
                        AuthenticationMechanismMame.setMechanismId(Authentication.forValue(arr[++pos]));
                    }
                    else
                    {
                        //Get Tag and Len.
                        if (arr[++pos] != 2 && arr[++pos] != 7)
                        {
                            throw new IllegalArgumentException();
                        }
                        //Get tag
                        if (arr[++pos] != 0x11)
                        {
                            throw new IllegalArgumentException();
                        }
                        AuthenticationMechanismMame.setJointIsoCtt(arr[++pos]);
                        //Get tag
                        if (arr[++pos] != 0x11)
                        {
                            throw new IllegalArgumentException();
                        }
                        AuthenticationMechanismMame.setCountry(arr[++pos]);
                        //Get tag
                        if (arr[++pos] != 0x12)
                        {
                            throw new IllegalArgumentException();
                        }
                        int[] tmp = new int[]{pos};
                        AuthenticationMechanismMame.setCountryName(GXCommon.getUInt16(arr, tmp));
                        pos = tmp[0];
                        //Get tag
                        if (arr[++pos] != 0x11)
                        {
                            throw new IllegalArgumentException();
                        }
                        AuthenticationMechanismMame.setIdentifiedOrganization(arr[++pos]);
                        //Get tag
                        if (arr[++pos] != 0x11)
                        {
                            throw new IllegalArgumentException();
                        }
                        AuthenticationMechanismMame.setDlmsUA(arr[++pos]);
                        //Get tag
                        if (arr[++pos] != 0x11)
                        {
                            throw new IllegalArgumentException();
                        }
                        AuthenticationMechanismMame.setAuthenticationMechanismName(arr[++pos]);
                        //Get tag
                        if (arr[++pos] != 0x11)
                        {
                            throw new IllegalArgumentException();
                        }
                        AuthenticationMechanismMame.setMechanismId(Authentication.forValue(arr[++pos]));
                    }
                }
                else
                {           
                    if (value != null)
                    {
                        AuthenticationMechanismMame.setJointIsoCtt(((Number)Array.get(value, 0)).intValue());
                        AuthenticationMechanismMame.setCountry(((Number)Array.get(value, 1)).intValue());
                        AuthenticationMechanismMame.setCountryName(((Number)Array.get(value, 2)).intValue());
                        AuthenticationMechanismMame.setIdentifiedOrganization(((Number)Array.get(value, 3)).intValue());
                        AuthenticationMechanismMame.setDlmsUA(((Number)Array.get(value, 4)).intValue());
                        AuthenticationMechanismMame.setAuthenticationMechanismName(((Number)Array.get(value, 5)).intValue());
                        AuthenticationMechanismMame.setMechanismId(Authentication.forValue(((Number)Array.get(value, 6)).intValue()));
                    }
                }                                           
            }
        }
        else if (index == 7)
        {
            Secret = (byte[]) value;
        }
        else if (index == 8)
        {
            if (value == null)
            {
                setAssociationStatus(AssociationStatus.NonAssociated);
            }
            else
            {
                setAssociationStatus(AssociationStatus.values()[((Number)value).intValue()]);
            }                        
        }
        else if (index == 9)
        {            
            setSecuritySetupReference(GXDLMSObject.toLogicalName((byte[]) value));
        }
        else
        {
            throw new IllegalArgumentException("SetValue failed. Invalid attribute index.");
        }
    }
}