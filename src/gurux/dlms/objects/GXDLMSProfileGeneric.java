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
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.SortMethod;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GXDLMSProfileGeneric extends GXDLMSObject implements IGXDLMSBase
{
    GXDLMSServerBase Server;
    ArrayList<Object[]> m_Buffer = new ArrayList<Object[]>();
    Object m_From;
    AccessRange m_AccessSelector;
    Object m_To;    
    List<AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>> m_CaptureObjects;
    int m_CapturePeriod;
    SortMethod m_SortMethod;
    GXDLMSObject m_SortObject;
    int SortObjectAttributeIndex;
    int SortObjectDataIndex;
    
    int m_ProfileEntries;

    Object getOwner()
    {
        if (getParent() != null)
        {
            return getParent().getParent();
        }
        return null;
    }

    
    /**  
     Constructor.
    */
    public GXDLMSProfileGeneric()
    {
        this(null, (short) 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSProfileGeneric(String ln)
    {
        this(ln, (short) 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSProfileGeneric(String ln, int sn)
    {
        super(ObjectType.PROFILE_GENERIC, ln, sn);
        setAccessSelector(AccessRange.Last);
        m_CaptureObjects = new ArrayList<AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>>();
    }   

    /** 
     Client uses this to save how values are access.
    */
    public final GXDLMSServerBase getServer()
    {
        return Server;
    }
    public final void setServer(GXDLMSServerBase value)
    {
        Server = value;
    }

    
    /** 
     Client uses this to save how values are access.
    */
    public final AccessRange getAccessSelector()
    {
        return m_AccessSelector;
    }
    public final void setAccessSelector(AccessRange value)
    {
        m_AccessSelector = value;
    }

    /** 
     Client uses this to save from which date values are retrieved.
    */
    public final Object getFrom()
    {
        return m_From;
    }
    public final void setFrom(Object value)
    {
        m_From = value;
    }

    /** 
     Client uses this to save to which date values are retrieved.
    */
    public final Object getTo()
    {
        return m_To;
    }
    public final void setTo(Object value)
    {
        m_To = value;
    }

    /** 
     Data of profile generic.
    */
    public final Object[] getBuffer()
    {
        return m_Buffer.toArray();
    }
    public final void setBuffer(Object[][] value)
    {
        m_Buffer.addAll(Arrays.asList(value));
    }

    /*
     * Add new capture object (column) to the profile generic.
     */
    public void addCaptureObject(GXDLMSObject item, int attributeIndex, int dataIndex)
    {
        if (item == null)            
        {
            throw new RuntimeException("Invalid Object");
        }
        if (attributeIndex < 1)
        {
            throw new RuntimeException("Invalid attribute index");
        }
        if (dataIndex < 0)
        {
            throw new RuntimeException("Invalid data index");
        }
        GXDLMSCaptureObject co = new GXDLMSCaptureObject(attributeIndex, dataIndex);
        m_CaptureObjects.add(new AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(item, co));        
    }
    /** 
     Captured Objects.
    */
    public final GXDLMSObject[] getCaptureObjects()
    {
        List<GXDLMSObject> objects = new ArrayList<GXDLMSObject>();
        for(AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> it : m_CaptureObjects)
        {
            objects.add(it.getKey());
        }
        return objects.toArray(new GXDLMSObject[objects.size()]);
    }

    /** 
     How often values are captured.
    */
    public final int getCapturePeriod()
    {
        return m_CapturePeriod;
    }
    public final void setCapturePeriod(int value)
    {
        m_CapturePeriod = value;
    }

    /** 
     How columns are sorted.
    */
    public final SortMethod getSortMethod()
    {
        return m_SortMethod;
    }
    public final void setSortMethod(SortMethod value)
    {
        m_SortMethod = value;
    }

    /** 
     Column that is used for sorting.
    */
    public final GXDLMSObject getSortObject()
    {
        return m_SortObject;
    }
    public final void setSortObject(GXDLMSObject value)
    {
        m_SortObject = value;
    }

    /** 
     Attribute index of sort object.
    */
    public final int getSortObjectAttributeIndex()
    {
        return SortObjectAttributeIndex;
    }
    public final void setSortObjectAttributeIndex(int value)
    {
        SortObjectAttributeIndex = value;
    }
    
    /** 
     Data index of sort object.
    */
    public final int getSortObjectDataIndex()
    {
        return SortObjectDataIndex;
    }
    public final void setSortObject(int value)
    {
        SortObjectDataIndex = value;
    }    
    
    /** 
     Entries (rows) in Use.
    */
    public final int getEntriesInUse()
    {
        return m_Buffer.size();
    }

    /** 
     Maximum Entries (rows) count.
    */
    public final int getProfileEntries()
    {
        return m_ProfileEntries;
    }
    public final void setProfileEntries(int value)
    {
        m_ProfileEntries = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getBuffer(), getCaptureObjects(), 
            getCapturePeriod(), getSortMethod(), getSortObject(), getEntriesInUse(), getProfileEntries()};
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
        //Buffer
        if (!isRead(2))
        {
            attributes.add(2);
        }
        //CaptureObjects
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //CapturePeriod
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //SortMethod
        if (!isRead(5))
        {
            attributes.add(5);
        }
        //SortObject
        if (!isRead(6))
        {
            attributes.add(6);
        }
        //EntriesInUse
        if (!isRead(7))
        {
            attributes.add(7);
        }
        //ProfileEntries
        if (!isRead(8))
        {
            attributes.add(8);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */
    @Override
    public int getAttributeCount()
    {
        return 8;
    }       
    
     /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 1;
    }
    
    /** 
     Returns Association View.

     @param items
     @return 
    */
    private byte[] getColumns()
    {
        try
        {
            int cnt = m_CaptureObjects.size();        
            java.nio.ByteBuffer data = java.nio.ByteBuffer.allocate((18 * cnt) + 10);
            data.put((byte)DataType.ARRAY.getValue());
            //Add count
            GXCommon.setObjectCount(cnt, data);            
            for (AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> it : m_CaptureObjects)
            {
                data.put((byte) DataType.STRUCTURE.getValue());
                data.put((byte) 4); //Count
                GXCommon.setData(data, DataType.UINT16, it.getKey().getObjectType().getValue()); //ClassID
                GXCommon.setData(data, DataType.OCTET_STRING, it.getKey().getLogicalName()); //LN
                GXCommon.setData(data, DataType.INT8, it.getValue().getAttributeIndex()); //Attribute Index
                GXCommon.setData(data, DataType.UINT16, it.getValue().getDataIndex()); //Data Index
            }
            return data.array();
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    @SuppressWarnings("unused")
	private void getAccessSelector(byte[] data, int[] selector, Object[] start, Object[] to)
    {
        selector[0] = data[0];
        int[] pos = new int[1];
        DataType[] type = new DataType[]{DataType.NONE};
        //Start index
        int[] index = new int[1], count = new int[1], cachePos = new int[1];
        if (selector[0] == 1) //Read by range
        {
            if (data[1] != (int)DataType.STRUCTURE.getValue() || data[2] != 4 || data[3] != (int)DataType.STRUCTURE.getValue() || data[4] != 4)
            {
                throw new GXDLMSException("Invalid parameter");
            }
            pos[0] = 5;
            Object classId = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            type[0] = DataType.NONE;
            Object ln = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            type[0] = DataType.NONE;
            Object attributeIndex = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            type[0] = DataType.NONE;
            Object version = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            type[0] = DataType.NONE;
            Object tempVar = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            byte[] tmp = (byte[]) tempVar;
            start[0] = GXDLMSClient.changeType(tmp, DataType.DATETIME);
            type[0] = DataType.NONE;
            Object tempVar2 = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            tmp = (byte[]) tempVar2;
            to[0] = GXDLMSClient.changeType(tmp, DataType.DATETIME);
        }
        else if (selector[0] == 2) //Read by entry.
        {
            if (data[1] != (int)DataType.STRUCTURE.getValue() || data[2] != 4)
            {
                    throw new GXDLMSException("Invalid parameter");
            }
            pos[0] = 3;
            start[0] = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            type[0] = DataType.NONE;
            to[0] = GXCommon.getData(data, pos, 0, count, index, type, cachePos);
            if (((Number)start[0]).longValue() > ((Number)to[0]).longValue())
            {
                throw new GXDLMSException("Invalid parameter");
            }
        }
        else
        {
            throw new GXDLMSException("Invalid parameter");
        }
    }

    /** 
     Returns Association View.

     @param table
     @return 
    */
    private byte[] getData(Object[] table) 
            throws RuntimeException, ParseException, 
            UnsupportedEncodingException, IOException            
    {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write((byte)DataType.ARRAY.getValue());        
        GXCommon.setObjectCount(Array.getLength(table), data);
        DataType[] types = new DataType[m_CaptureObjects.size()];
        int pos = -1;
        for(AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> it : m_CaptureObjects)
        {
            types[++pos] = it.getKey().getDataType(it.getValue().getAttributeIndex());
        }
        for (Object row : table)
        {
            Object[] items = (Object[]) row;
            data.write(DataType.STRUCTURE.getValue());
            GXCommon.setObjectCount(Array.getLength(items), data);
            pos = -1;
            for (Object value : items)
            {
                DataType tp = types[++pos];
                if (tp == DataType.NONE)
                {
                    tp = GXCommon.getValueType(value);
                    types[pos] = tp;  
                }
                GXCommon.setData(data, tp, value);
            }
        }        
        return data.toByteArray();
        
    }   
    
    private byte[] getProfileGenericData(byte[] data)
    {        
        try
        {
            int[] selector = new int[1];
            Object[] from = new Object[1], to = new Object[1];
            //If all data is readed.
            if (data == null || data.length == 0)
            {
                return getData(getBuffer());            
            }
            getAccessSelector(data, selector, from, to);
            Object[] table = getBuffer();
            ArrayList<Object[]> items = new ArrayList<Object[]>();
            synchronized (this)
            {
                if (selector[0] == 1) //Read by range
                {
                    java.util.Date start = ((GXDateTime)from[0]).getValue();
                    java.util.Date end = ((GXDateTime)to[0]).getValue();
                    for (Object row : table)
                    {
                        java.util.Date tm = (java.util.Date)((Object[]) row)[0];
                        if (tm.compareTo(start) >= 0 && tm.compareTo(end) <= 0)
                        {
                            items.add((Object[])row);
                        }
                    }
                }
                else if (selector[0] == 2) //Read by entry.
                {
                    int start = ((Number)from[0]).intValue();
                    int count = ((Number)to[0]).intValue();
                    for (int pos = 0; pos < count; ++pos)
                    {
                        if (pos + start == Array.getLength(table))
                        {
                            break;
                        }
                        items.add((Object[]) table[start + pos]);
                    }
                }
            }
            return getData(items.toArray());       
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
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
            return getProfileGenericData(parameters);          
        }        
        if (index == 3)
        {
            type[0] = DataType.ARRAY;            
            return getColumns();
        }
        if (index == 4)
        {
            type[0] = DataType.INT8;
            return getCapturePeriod();
        }
        if (index == 5)
        {            
            type[0] = DataType.INT8;
            return getSortMethod().getValue();
        }        
        if (index == 6)
        {
            type[0] = DataType.ARRAY;
            ByteArrayOutputStream data = new ByteArrayOutputStream();            
            data.write((byte)DataType.STRUCTURE.getValue());
            data.write((byte) 4); //Count  
            try
            {
                if (m_SortObject == null)
                {
                    GXCommon.setData(data, DataType.UINT16, 0); //ClassID
                    GXCommon.setData(data, DataType.OCTET_STRING, new byte[6]); //LN
                    GXCommon.setData(data, DataType.INT8, 0); //Selected Attribute Index
                    GXCommon.setData(data, DataType.UINT16, 0); //Selected Data Index
                }
                else
                {
                    GXCommon.setData(data, DataType.UINT16, m_SortObject.getObjectType().getValue()); //ClassID
                    GXCommon.setData(data, DataType.OCTET_STRING, m_SortObject.getLogicalName()); //LN
                    GXCommon.setData(data, DataType.INT8, SortObjectAttributeIndex); //Attribute Index
                    GXCommon.setData(data, DataType.UINT16, SortObjectDataIndex); //Data Index
                }
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
            return data.toByteArray();
        }
        if (index == 7)
        {
            type[0] = DataType.UINT32;
            return getEntriesInUse();
        }
        if (index == 8)
        {
            type[0] = DataType.UINT32;
            return getProfileEntries();
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
            if (m_CaptureObjects == null || m_CaptureObjects.size() == 0)
            {
                throw new RuntimeException("Read capture objects first.");
            }
            m_Buffer.clear();
            if (value != null)
            {
                DataType[] types = new DataType[m_CaptureObjects.size()];
                int pos = -1;                
                for(AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> it : m_CaptureObjects)
                {
                    types[++pos] = it.getKey().getUIDataType(it.getValue().getAttributeIndex());
                }                
                for(Object row : (Object[]) value)
                {
                    if (Array.getLength(row) != m_CaptureObjects.size())
                    {
                        throw new RuntimeException("Number of columns do not match.");
                    }
                    for(int a = 0; a < Array.getLength(row); ++a)
                    {
                        Object data = Array.get(row, a);
                        DataType type = types[a];
                        if (type != DataType.NONE && type != null &&
                                data instanceof byte[])
                        {
                            data = GXDLMSClient.changeType((byte[]) data, type);
                            Array.set(row, a, data);
                        }
                        AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> item = m_CaptureObjects.get(pos);
                        if (item.getKey() instanceof GXDLMSRegister && item.getValue().AttributeIndex == 2)
                        {
                            double scaler = ((GXDLMSRegister) item.getKey()).getScaler();
                            if (scaler != 1)
                            {
                                try
                                {
                                    data = ((Number)data).doubleValue() * scaler;
                                    Array.set(row, a, data);
                                }
                                catch (Exception ex)
                                {
                                    System.out.println("Scalar failed for: " + item.getKey().getLogicalName());
                                    //Skip error
                                }
                            }
                        }   
                    }
                    m_Buffer.add((Object[]) row);
                }                
            }            
        }
        else if (index == 3)
        {
            m_CaptureObjects.clear();
            if (value != null)
            {
                for (Object it : (Object[]) value)
                {
                    Object[] tmp = (Object[])it;
                    if (tmp.length != 4)
                    {
                        throw new GXDLMSException("Invalid structure format.");
                    }
                    ObjectType type = ObjectType.forValue(((Number)tmp[0]).intValue());
                    String ln = GXDLMSObject.toLogicalName((byte[])tmp[1]);
                    GXDLMSObject obj = null;
                    Object owner = getOwner();
                    if (owner instanceof GXDLMSServerBase)
                    {
                        obj = ((GXDLMSServerBase) owner).getItems().findByLN(type, ln);
                    }
                    else if (owner instanceof GXDLMSClient)
                    {
                        obj = ((GXDLMSClient) owner).getObjects().findByLN(type, ln);
                    }
                    if(obj == null)
                    {                        
                        obj = gurux.dlms.GXDLMSClient.createObject(type);
                        obj.setLogicalName(ln);
                    }                    
                    addCaptureObject(obj, ((Number)tmp[2]).intValue(), ((Number)tmp[3]).intValue());
                }
            }
        }
        else if (index == 4)
        {
            if (value == null)
            {
                m_CapturePeriod = 0;
            }
            else
            {
                m_CapturePeriod = ((Number)value).intValue();                
            }
            
        }
        else if (index == 5)
        {
            if (value == null)
            {
                m_SortMethod = SortMethod.FIFO;
            }
            else
            {
                m_SortMethod = SortMethod.forValue(((Number)value).intValue());    
            }            
            
        }
        else if (index == 6)
        {
            if (value == null)
            {
                m_SortObject = null;
            }
            else
            {
                Object[] tmp = (Object[]) value;
                if (tmp.length != 4)
                {
                    throw new IllegalArgumentException("Invalid structure format.");
                }
                ObjectType type = ObjectType.forValue(((Number)tmp[0]).intValue());
                String ln = GXDLMSObject.toLogicalName((byte[])tmp[1]);
                int attributeIndex = ((Number)tmp[2]).intValue();
                int dataIndex = ((Number)tmp[3]).intValue();
                m_SortObject = Server.getItems().findByLN(type, ln);                   
                if(m_SortObject == null)
                {                        
                    m_SortObject = gurux.dlms.GXDLMSClient.createObject(type);
                    m_SortObject.setLogicalName(ln);                    
                }                                                    
                SortObjectAttributeIndex = attributeIndex;
                SortObjectDataIndex = dataIndex;
            }                        
        }
        else if (index == 7)
        {                
            //Client can't set row count.
            //TODO: throw new IllegalArgumentException("SetValue failed. Invalid attribute index.");
        }
        else if (index == 8)
        {
            if (value == null)
            {
                m_ProfileEntries = 0;
            }
            else
            {
                m_ProfileEntries = ((Number)value).intValue();                
            }                        
        }
        else
        {
            throw new IllegalArgumentException("SetValue failed. Invalid attribute index.");
        }
    }
    
    /** 
    Clears the buffer.
   */
   public void reset()
   {
        synchronized (this)
        {
            m_Buffer.clear();
        }
   }

   /** 
    Copies the values of the objects to capture 
    into the buffer by reading capture objects.
   */
   public void capture()
   {
       synchronized (this)
       {
           Object[] values = new Object[m_CaptureObjects.size()];
           int pos = -1;
           for (AbstractMap.SimpleEntry<GXDLMSObject, GXDLMSCaptureObject> obj : m_CaptureObjects)
            {
                ValueEventArgs e = new ValueEventArgs(obj.getKey(), obj.getValue().getAttributeIndex());
                Server.read(e);
                if (e.getHandled())
                {
                    values[++pos] = e.getValue();
                }
                else
                {
                    DataType[] type = new DataType[]{DataType.NONE};
                    values[++pos] = obj.getKey().getValue(obj.getValue().getAttributeIndex(), type, null, false);
                }
            }
            synchronized (this)
            {
                //Remove first items if buffer is full.
                if (getProfileEntries() == Array.getLength(getBuffer()))
                {
                    m_Buffer.remove(0);
                }
                m_Buffer.add(values);
            }           
       }         
   }
}