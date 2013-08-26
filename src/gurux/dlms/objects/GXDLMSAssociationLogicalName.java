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

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.manufacturersettings.GXAttributeCollection;
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
    AssociationStatus m_AssociationStatus = AssociationStatus.NonAssociated;
    GXDLMSObjectCollection m_ObjectList;
    Object m_AssociatedPartnersId;
    Object m_ApplicationContextName;
    Object m_XDLMSContextInfo;
    Object m_AuthenticationMechanismMame;
    Object m_Secret;
    String m_SecuritySetupReference;

    /**  
     Constructor.
    */
    public GXDLMSAssociationLogicalName()
    {
        this("0.0.40.0.0.255");
    }

     /**  
     Constructor.

     @param ln Logican Name of the object.
     */
    public GXDLMSAssociationLogicalName(String ln)
    {
        super(ObjectType.ASSOCIATION_LOGICAL_NAME, ln, 0);
        setLogicalName(ln);
        m_ObjectList = new GXDLMSObjectCollection(this);
    }

    public final GXDLMSObjectCollection getObjectList()
    {
        return m_ObjectList;
    }
    
    public final void setObjectList(GXDLMSObjectCollection value)
    {
        m_ObjectList = value;
    }

    public final Object getAssociatedPartnersId()
    {
        return m_AssociatedPartnersId;
    }
    public final void setAssociatedPartnersId(Object value)
    {
        m_AssociatedPartnersId = value;
    }

    public final Object getApplicationContextName()
    {
        return m_ApplicationContextName;
    }
    public final void setApplicationContextName(Object value)
    {
        m_ApplicationContextName = value;
    }

    public final Object getXDLMSContextInfo()
    {
        return m_XDLMSContextInfo;
    }
    public final void setXDLMSContextInfo(Object value)
    {
        m_XDLMSContextInfo = value;
    }

    public final Object getAuthenticationMechanismMame()
    {
        return m_AuthenticationMechanismMame;
    }
    public final void setAuthenticationMechanismMame(Object value)
    {
        m_AuthenticationMechanismMame = value;
    }

    public final Object getSecret()
    {
        return m_Secret;
    }
    public final void setSecret(Object value)
    {
        m_Secret = value;
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
        return m_SecuritySetupReference;
    }
    public final void setSecuritySetupReference(String value)
    {
        m_SecuritySetupReference = value;
    }
    
    

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getObjectList(), getAssociatedPartnersId(), getApplicationContextName(), getXDLMSContextInfo(), getAuthenticationMechanismMame(), getSecret(), getAssociationStatus()};
    }
    
    /*
     * Returns collection of attributes to read.
     * 
     * If attribute is static and already read or device is returned HW error it is not returned.
     */
    @Override
    public int[] GetAttributeIndexToRead()
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
        //AccessRightsList is static and read only once.
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //SecuritySetupReference is static and read only once.
        if (!isRead(4))
        {
            attributes.add(4);
        }
        return toIntArray(attributes);
    }
    
    @Override
    public int getAttributeCount()
    {
        return 9;
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
            int tmp = GXCommon.intValue(methodAccess[1]);
            MethodAccessMode mode = MethodAccessMode.forValue(tmp);
            obj.setMethodAccess(id, mode);
        }
    }
    
     /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, DataType[] type, byte[] parameters, boolean raw)
    {
        if (index == 1)
        {
            type[0] = DataType.OCTET_STRING;
            return getLogicalName();
        }        
        if (index == 2)
        {
            type[0] = DataType.ARRAY;
            return getObjects();
        }
        if (index == 3)
        {
            type[0] = DataType.NONE;
            return getAssociatedPartnersId();
        }
        if (index == 4)
        {
            type[0] = DataType.NONE;
            return getApplicationContextName();
        }
        if (index == 5)
        {
            type[0] = DataType.NONE;
            return getXDLMSContextInfo();
        }
        if (index == 6)
        {
            type[0] = DataType.NONE;
            return getAuthenticationMechanismMame();
        }
        if (index == 7)
        {
            type[0] = DataType.OCTET_STRING;
            return getSecret();
        }
        if (index == 8)
        {
            type[0] = DataType.UINT8;
            return getAssociationStatus().ordinal();
        }
        if (index == 9)
        {
            type[0] = DataType.OCTET_STRING;
            return getSecuritySetupReference();
        }
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
    
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value, boolean raw)
    {
        if (index == 1)
        {
            setLogicalName(GXDLMSObject.toLogicalName((byte[]) value));            
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
                    GXDLMSObject obj = gurux.dlms.GXDLMSClient.createObject(type);                    
                    obj.setLogicalName(ln);
                    updateAccessRights(obj, (Object[]) Array.get(item, 3));
                    obj.setLogicalName(ln);
                    obj.setVersion(version);
                    m_ObjectList.add(obj);
                }               
            }
        }
        else if (index == 3)
        {            
             setAssociatedPartnersId(value);            
        }
        else if (index == 4)
        {
            setApplicationContextName(value);
        }
        else if (index == 5)
        {
            setXDLMSContextInfo(value);
        }
        else if (index == 6)
        {
            setAuthenticationMechanismMame(value);                                    
        }
        else if (index == 7)
        {
            setSecret(value);
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
            setSecuritySetupReference(String.valueOf(value));
        }
        else
        {
            throw new IllegalArgumentException("SetValue failed. Invalid attribute index.");
        }
    }
}