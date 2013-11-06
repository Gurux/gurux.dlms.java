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
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.manufacturersettings.GXAuthentication;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSAssociationShortName extends GXDLMSObject implements IGXDLMSBase
{
    Object m_AccessRightsList;
    GXDLMSObjectCollection m_ObjectList;
    Object m_SecuritySetupReference;

    /**  
     Constructor.
    */
    public GXDLMSAssociationShortName()
    {
        this("0.0.40.0.0.255", 0xFA00);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSAssociationShortName(String ln, int sn)
    {
        super(ObjectType.ASSOCIATION_SHORT_NAME, ln, sn);
        m_ObjectList = new GXDLMSObjectCollection(this);        
    }

    public final GXDLMSObjectCollection getObjectList()
    {
        return m_ObjectList;
    }    

    public final Object getAccessRightsList()
    {
        return m_AccessRightsList;
    }
    public final void setAccessRightsList(Object value)
    {
        m_AccessRightsList = value;
    }

    public final Object getSecuritySetupReference()
    {
        return m_SecuritySetupReference;
    }
    public final void setSecuritySetupReference(Object value)
    {
        m_SecuritySetupReference = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getObjectList(), getAccessRightsList(), getSecuritySetupReference()};
    }
        
    /*
    * Invokes method.
    * 
     @param index Method index.
    */
    @Override
    public byte[] invoke(Object sender, int index, Object parameters)
    {
        //Check reply_to_HLS_authentication
        if (index == 8)
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
                return s.serverReportError(1, 5, 3);
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
        return 4;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 8;
    }  
    
    private void getAccessRights(GXDLMSObject item, ByteArrayOutputStream data) 
            throws RuntimeException, UnsupportedEncodingException, ParseException, 
            IOException            
    {        
        data.write((byte)DataType.STRUCTURE.getValue());
        data.write((byte) 3);
        GXCommon.setData(data, DataType.UINT16, item.getShortName());
        data.write((byte) DataType.ARRAY.getValue());
        data.write((byte) item.getAttributes().size());
        for (GXDLMSAttributeSettings att : item.getAttributes())
        {
            data.write((byte)DataType.STRUCTURE.getValue()); //attribute_access_item
            data.write((byte)3);
            GXCommon.setData(data, DataType.INT8, att.getIndex());
            GXCommon.setData(data, DataType.ENUM, att.getAccess().getValue());
            GXCommon.setData(data, DataType.NONE, null);
        }
        data.write((byte)DataType.ARRAY.getValue());
        data.write((byte)item.getMethodAttributes().size());
        for (GXDLMSAttributeSettings it : item.getMethodAttributes())
        {
            data.write((byte)DataType.STRUCTURE.getValue()); //attribute_access_item
            data.write((byte)2);
            GXCommon.setData(data, DataType.INT8, it.getIndex());
            GXCommon.setData(data, DataType.ENUM, it.getMethodAccess().getValue());
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
        else if (index == 2)
        {
            type[0] = DataType.ARRAY;
            int cnt = m_ObjectList.size();
            try
            {
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                data.write((byte)DataType.ARRAY.getValue());
                //Add count            
                GXCommon.setObjectCount(cnt, data);
                if (cnt != 0)
                {
                    for (GXDLMSObject it : m_ObjectList)
                    {
                        data.write((byte) DataType.STRUCTURE.getValue());
                        data.write((byte) 4); //Count
                        GXCommon.setData(data, DataType.UINT16, it.getShortName()); //base address.
                        GXCommon.setData(data, DataType.UINT16, it.getObjectType().getValue()); //ClassID
                        GXCommon.setData(data, DataType.UINT8, 0); //Version
                        GXCommon.setData(data, DataType.OCTET_STRING, it.getLogicalName()); //LN
                    }
                    if (m_ObjectList.findBySN(this.getShortName()) == null)
                    {
                        data.write((byte) DataType.STRUCTURE.getValue());
                        data.write((byte) 4); //Count
                        GXCommon.setData(data, DataType.UINT16, this.getShortName()); //base address.
                        GXCommon.setData(data, DataType.UINT16, this.getObjectType().getValue()); //ClassID
                        GXCommon.setData(data, DataType.UINT8, 0); //Version
                        GXCommon.setData(data, DataType.OCTET_STRING, this.getLogicalName()); //LN
                    }
                }
                return data.toByteArray();
            }
            catch(Exception ex)
            {
                Logger.getLogger(GXDLMSAssociationShortName.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }                        
        }  
        else if (index == 3)
        {
            type[0] = DataType.ARRAY;
            boolean lnExists = m_ObjectList.findBySN(this.getShortName()) != null;
            //Add count        
            int cnt = m_ObjectList.size();
            if (!lnExists)
            {
                ++cnt;
            }            
            ByteArrayOutputStream data = new ByteArrayOutputStream();            
            data.write((byte)DataType.ARRAY.getValue());                        
            GXCommon.setObjectCount(cnt, data);            
            try
            {
                for(GXDLMSObject it : m_ObjectList)
                {
                    getAccessRights(it, data);
                }
                if (!lnExists)
                {
                    getAccessRights(this, data);
                }
            }
            catch(Exception ex)
            {
                Logger.getLogger(GXDLMSAssociationShortName.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }                       
            return data.toByteArray();
        }  
        else if (index == 4)
        {
            
        }                
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
    
    void updateAccessRights(Object[] buff)
    {        
        for (Object access : buff)
        {            
            int sn = GXCommon.intValue(Array.get(access, 0));
            GXDLMSObject obj = m_ObjectList.findBySN(sn);    
            if (obj != null)
            {            
                for (Object attributeAccess : (Object[]) Array.get(access, 1))
                {                          
                    int id = GXCommon.intValue(Array.get(attributeAccess, 0));
                    int tmp = GXCommon.intValue(Array.get(attributeAccess, 1));
                    AccessMode mode = AccessMode.forValue(tmp);
                    obj.setAccess(id, mode);
                }                
                for (Object methodAccess : (Object[]) Array.get(access, 2))
                {
                    int id = ((Number)((Object[]) methodAccess)[0]).intValue();
                    int tmp = ((Number)((Object[]) methodAccess)[1]).intValue();
                    MethodAccessMode mode = MethodAccessMode.forValue(tmp);
                    obj.setMethodAccess(id, mode);
                }
            }
        }
    }
    
    @Override
    public DataType getDataType(int index)
    {
        if (index == 2 || index == 3)
        {
            return DataType.ARRAY;
        }
        return super.getDataType(index);
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
                    int sn = ((Number)Array.get(item, 0)).intValue() & 0xFFFF;
                    ObjectType type = ObjectType.forValue(((Number)Array.get(item, 1)).intValue());
                    int version = ((Number)Array.get(item, 2)).intValue();
                    String ln = GXDLMSObject.toLogicalName((byte[]) Array.get(item, 3));
                    GXDLMSObject obj = gurux.dlms.GXDLMSClient.createObject(type);        
                    obj.setLogicalName(ln);
                    obj.setShortName(sn);
                    obj.setVersion(version);
                    m_ObjectList.add(obj);
                }               
            }
        }  
        else if (index == 3)
        {
            if (value == null)
            {
                for(GXDLMSObject it : m_ObjectList)
                {
                    for(int pos = 1; pos != it.getAttributeCount(); ++pos)
                    {
                        it.setAccess(pos, AccessMode.NO_ACCESS);
                    }
                }
            }
            else
            {
                updateAccessRights((Object[]) value);
            }
        }  
        else if (index == 4)
        {  
            if (value != null)
            {
                throw new UnsupportedOperationException();
            }
        }  
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}