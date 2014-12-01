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
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GXDLMSScriptTable extends GXDLMSObject implements IGXDLMSBase
{
    private List<AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction>> Scripts;
    /**  
     Constructor.
    */
    public GXDLMSScriptTable()
    {
        super(ObjectType.SCRIPT_TABLE);
        Scripts = new ArrayList<AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSScriptTable(String ln)
    {
        super(ObjectType.SCRIPT_TABLE, ln, 0);
        Scripts = new ArrayList<AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSScriptTable(String ln, int sn)
    {
        super(ObjectType.SCRIPT_TABLE, ln, sn);
        Scripts = new ArrayList<AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction>>();
    }

    public final List<AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction>> getScripts()
    {
        return Scripts;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getScripts()};
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
        //Scripts
        if (!isRead(2))
        {
            attributes.add(2);
        }    
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 2;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 1;
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
            int cnt = getScripts().size();
            ByteArrayOutputStream data = new ByteArrayOutputStream();   
            data.write(DataType.ARRAY.getValue());
            //Add count            
            GXCommon.setObjectCount(cnt, data);
            if (cnt != 0)
            {
                try
                {                    
                    for(AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction> it : getScripts())
                    {
                        data.write(DataType.STRUCTURE.getValue());
                        data.write(2); //Count
                        GXCommon.setData(data, DataType.UINT16, it.getKey()); //Script_identifier:
                        data.write(DataType.ARRAY.getValue());
                        data.write(5); //Count
                        GXDLMSScriptAction tmp = it.getValue();
                        GXCommon.setData(data, DataType.ENUM, tmp.getType().ordinal() + 1); //service_id
                        GXCommon.setData(data, DataType.UINT16, tmp.getObjectType().getValue()); //class_id
                        GXCommon.setData(data, DataType.OCTET_STRING, tmp.getLogicalName()); //logical_name
                        GXCommon.setData(data, DataType.INT8, tmp.getIndex()); //index
                        GXCommon.setData(data, tmp.getParameterType(), tmp.getObjectType().getValue()); //parameter
                    }
                }                
                catch (Exception ex) 
                {
                    Logger.getLogger(GXDLMSClock.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex.getMessage());
                }
            }
            return data.toByteArray();  
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
            Scripts.clear();
            //Fix Xemex bug here.
            //Xemex meters do not return array as they shoul be according standard.
            if (value instanceof Object[] && Array.getLength(value) != 0)
            {
                if (((Object[])value)[0] instanceof Object[])
                {
                    for(Object item : (Object[])value)
                    { 
                        int script_identifier = ((Number)Array.get(item, 0)).intValue();
                        for(Object arr : (Object[])Array.get(item, 1))
                        { 
                            GXDLMSScriptAction it = new GXDLMSScriptAction();
                            int val = ((Number)Array.get(arr, 0)).intValue();
                            GXDLMSScriptActionType type = GXDLMSScriptActionType.NONE;
                            //Some Iskra meters return -1 here.
                            //It is not standard value.
                            if (val > 0)
                            {
                                type = GXDLMSScriptActionType.values()[val];
                            }                            
                            it.setType(type);                
                            ObjectType ot = ObjectType.forValue(((Number)Array.get(arr, 1)).intValue());
                            it.setObjectType(ot);
                            String ln = GXDLMSObject.toLogicalName((byte[]) Array.get(arr, 2));
                            it.setLogicalName(ln);
                            it.setIndex(((Number)Array.get(arr, 3)).intValue());
                            it.setParameter(Array.get(arr, 4), DataType.NONE);
                            Scripts.add(new AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction>(script_identifier, it));
                        }                    
                    }               
                }
                else //Read Xemex meter here.
                {
                    int script_identifier = ((Number)Array.get(value, 0)).intValue();
                    Object[] arr = (Object[])((Object[])value)[1];
                    GXDLMSScriptAction it = new GXDLMSScriptAction();
                    GXDLMSScriptActionType type = GXDLMSScriptActionType.values()[((Number)Array.get(arr, 0)).intValue() - 1];
                    it.setType(type);
                    ObjectType ot = ObjectType.forValue(((Number)Array.get(arr, 1)).intValue());
                    it.setObjectType(ot);
                    String ln = GXDLMSObject.toLogicalName((byte[]) Array.get(arr, 2));
                    it.setLogicalName(ln);
                    it.setIndex(((Number)Array.get(arr, 3)).intValue());
                    it.setParameter(Array.get(arr, 4), DataType.NONE);
                    Scripts.add(new AbstractMap.SimpleEntry<Integer, GXDLMSScriptAction>(script_identifier, it));                
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
    
    @Override
    public byte[][] invoke(Object sender, int index, Object parameters)
    {
        //Execute selected method.
        if (index == 1)
        {
            
        }
        else
        {
            throw new IllegalArgumentException("Invoke failed. Invalid attribute index.");
        }
        return null;
    }
    
    /*
     * Executes the script specified in parameter data.
     */
    public byte[][] execute(GXDLMSClient client, Object data, DataType type)
    {
        return client.method(this, 1, data, type);
    }
}