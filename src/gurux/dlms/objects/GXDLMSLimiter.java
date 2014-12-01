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
import gurux.dlms.GXDateTime;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;

public class GXDLMSLimiter extends GXDLMSObject implements IGXDLMSBase
{
    private GXDLMSObject MonitoredValue;
    private Object ThresholdActive;
    private Object ThresholdNormal;
    private Object ThresholdEmergency;
    private long MinOverThresholdDuration;
    private long MinUnderThresholdDuration;

    private GXDLMSEmergencyProfile EmergencyProfile;
    private int[] EmergencyProfileGroupIDs;
    private boolean EmergencyProfileActive;
    private GXDLMSActionItem ActionOverThreshold;
    private GXDLMSActionItem ActionUnderThreshold;


    /**  
     Constructor.
    */
    public GXDLMSLimiter()
    {
        super(ObjectType.LIMITER);
        EmergencyProfile = new GXDLMSEmergencyProfile();
        ActionOverThreshold = new GXDLMSActionItem();
        ActionUnderThreshold = new GXDLMSActionItem();                        
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSLimiter(String ln)
    {
        super(ObjectType.LIMITER, ln, 0);
        EmergencyProfile = new GXDLMSEmergencyProfile();
        ActionOverThreshold = new GXDLMSActionItem();
        ActionUnderThreshold = new GXDLMSActionItem();        
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSLimiter(String ln, int sn)
    {
        super(ObjectType.LIMITER, ln, sn);
        EmergencyProfile = new GXDLMSEmergencyProfile();
        ActionOverThreshold = new GXDLMSActionItem();
        ActionUnderThreshold = new GXDLMSActionItem();        
    }

/** 
 Defines an attribute of an object to be monitored.
*/
    public final GXDLMSObject getMonitoredValue()
    {
        return MonitoredValue;
    }
    public final void setMonitoredValue(GXDLMSObject value)
    {
        MonitoredValue = value;
    }

    /** 
     Provides the active threshold value to which the attribute monitored is compared.
    */
    public final Object getThresholdActive()
    {
        return ThresholdActive;
    }
    public final void setThresholdActive(Object value)
    {
        ThresholdActive = value;
    }

    /** 
     Provides the threshold value to which the attribute monitored 
     is compared when in normal operation.
    */
    public final Object getThresholdNormal()
    {
        return ThresholdNormal;
    }
    public final void setThresholdNormal(Object value)
    {
        ThresholdNormal = value;
    }

    /** 
     Provides the threshold value to which the attribute monitored
     is compared when an emergency profile is active.
    */
    public final Object getThresholdEmergency()
    {
        return ThresholdEmergency;
    }
    public final void setThresholdEmergency(Object value)
    {
        ThresholdEmergency = value;
    }

    /** 
     Defines minimal over threshold duration in seconds required
     to execute the over threshold action.
    */
    public final long getMinOverThresholdDuration()
    {
        return MinOverThresholdDuration;
    }
    public final void setMinOverThresholdDuration(long value)
    {
        MinOverThresholdDuration = value;
    }

    /** 
     Defines minimal under threshold duration in seconds required to
     execute the under threshold action.
    */
    public final long getMinUnderThresholdDuration()
    {
        return MinUnderThresholdDuration;
    }
    public final void setMinUnderThresholdDuration(long value)
    {
        MinUnderThresholdDuration = value;
    }
    public final GXDLMSEmergencyProfile getEmergencyProfile()
    {
        return EmergencyProfile;
    }   

    public final int[] getEmergencyProfileGroupIDs()
    {
        return EmergencyProfileGroupIDs;
    }
    public final void setEmergencyProfileGroupIDs(int[] value)
    {
        EmergencyProfileGroupIDs = value;
    }

    /** 
     Is Emergency Profile active.
    */
    public final boolean getEmergencyProfileActive()
    {
        return EmergencyProfileActive;
    }
    public final void setEmergencyProfileActive(boolean value)
    {
        EmergencyProfileActive = value;
    }

    /** 
     Defines the scripts to be executed when the monitored value
     crosses the threshold for minimal duration time.
    */
    public final GXDLMSActionItem getActionOverThreshold()
    {
        return ActionOverThreshold;
    }
    public final void setActionOverThreshold(GXDLMSActionItem value)
    {
        ActionOverThreshold = value;
    }

    /** 
     Defines the scripts to be executed when the monitored value
     crosses the threshold for minimal duration time.
    */
    public final GXDLMSActionItem getActionUnderThreshold()
    {
        return ActionUnderThreshold;
    }
    public final void setActionUnderThreshold(GXDLMSActionItem value)
    {
        ActionUnderThreshold = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), MonitoredValue, ThresholdActive, ThresholdNormal, ThresholdEmergency,
            MinOverThresholdDuration, MinUnderThresholdDuration, EmergencyProfile, EmergencyProfileGroupIDs, 
            EmergencyProfileActive, new Object[] {ActionOverThreshold, ActionUnderThreshold}};
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
        //MonitoredValue
        if (canRead(2))
        {
            attributes.add(2);
        }

        //ThresholdActive
        if (canRead(3))
        {
            attributes.add(3);
        }

        //ThresholdNormal
        if (canRead(4))
        {
            attributes.add(4);
        }

        //ThresholdEmergency
        if (canRead(5))
        {
            attributes.add(5);
        }

        //MinOverThresholdDuration
        if (canRead(6))
        {
            attributes.add(6);
        }

        //MinUnderThresholdDuration
        if (canRead(7))
        {
            attributes.add(7);
        }

        //EmergencyProfile
        if (canRead(8))
        {
            attributes.add(8);
        }
        //EmergencyProfileGroup
        if (canRead(9))
        {
            attributes.add(9);
        }

        //EmergencyProfileActive
        if (canRead(10))
        {
            attributes.add(10);
        }
        //Actions
        if (canRead(11))
        {
            attributes.add(11);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 11;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 0;
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
            return DataType.STRUCTURE;
        }
        if (index == 3)
        {
            return super.getDataType(index);
        }
        if (index == 4)
        {
            return super.getDataType(index);
        }
        if (index == 5)
        {
            return super.getDataType(index);
        }
        if (index == 6)
        {
            return DataType.UINT32;
        }
        if (index == 7)
        {
            return DataType.UINT32;
        }
        if (index == 8)
        {
            return DataType.STRUCTURE;
        }
        if (index == 9)
        {
            return DataType.ARRAY;
        }
        if (index == 10)
        {
            return DataType.BOOLEAN;
        }
        if (index == 11)
        {
            return DataType.STRUCTURE;
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
        else if (index == 2)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte) DataType.STRUCTURE.getValue());
            data.write(3);
            try
            {
            GXCommon.setData(data, DataType.INT16, MonitoredValue.getObjectType().getValue());
            GXCommon.setData(data, DataType.OCTET_STRING, MonitoredValue.getLogicalName());
            //TODO: GXCommon.setData(data, DataType.UINT8, MonitoredValue.getSelectedAttributeIndex());
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            } 
            return data.toByteArray();
        }
        else if (index == 3)
        {
            return ThresholdActive;
        }
        else if (index == 4)
        {
            return ThresholdNormal;
        }
        else if (index == 5)
        {
            return ThresholdEmergency;
        }
        else if (index == 6)
        {
            return MinOverThresholdDuration;
        }
        else if (index == 7)
        {
            return MinUnderThresholdDuration;
        }
        else if (index == 8)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte) DataType.STRUCTURE.getValue());
            data.write(3);  
            try
            {
                GXCommon.setData(data, DataType.UINT16, EmergencyProfile.getID());
                GXCommon.setData(data, DataType.DATETIME, EmergencyProfile.getActivationTime());
                GXCommon.setData(data, DataType.UINT32, EmergencyProfile.getDuration());
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            } 
            return data.toByteArray();
        }
        else if (index == 9)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte) DataType.ARRAY.getValue());
            data.write((byte)EmergencyProfileGroupIDs.length);
            try
            {
                for (Object it : EmergencyProfileGroupIDs)
                {
                    GXCommon.setData(data, DataType.UINT16, it);
                }
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            } 
            return data.toByteArray();
        }
        else if (index == 10)
        {
            return EmergencyProfileActive;
        }
        else if (index == 11)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write((byte) DataType.STRUCTURE.getValue());
            data.write(2);
            data.write((byte) DataType.STRUCTURE.getValue());
            data.write(2);
            try
            {
                GXCommon.setData(data, DataType.OCTET_STRING, ActionOverThreshold.getLogicalName());
                GXCommon.setData(data, DataType.UINT16, ActionOverThreshold.getScriptSelector());
                data.write((byte)DataType.STRUCTURE.getValue());
                data.write(2);
                GXCommon.setData(data, DataType.OCTET_STRING, ActionUnderThreshold.getLogicalName());
                GXCommon.setData(data, DataType.UINT16, ActionUnderThreshold.getScriptSelector());
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
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
	        ObjectType ot = ObjectType.forValue(((Number)(((Object[])value)[0])).intValue());
	        String ln = GXDLMSClient.changeType((byte[])((Object[])value)[1], DataType.OCTET_STRING).toString();
	        int attIndex = ((Number)(((Object[])value)[2])).intValue();
	        MonitoredValue = getParent().findByLN(ot, ln);
	        if (MonitoredValue != null)
	        {
	            //TODO: MonitoredValue.setSelectedAttributeIndex(attIndex);
	        }
        }
        else if (index == 3)
        {
            ThresholdActive = value;
        }
        else if (index == 4)
        {
            ThresholdNormal = value;
        }
        else if (index == 5)
        {
            ThresholdEmergency = value;
        }
        else if (index == 6)
        {
            MinOverThresholdDuration = ((Number)value).longValue();
        }
        else if (index == 7)
        {
            MinUnderThresholdDuration = ((Number)value).longValue();
        }
        else if (index == 8)
        {
            Object[] tmp = (Object[])value;
            EmergencyProfile.setID(((Number) tmp[0]).intValue());
            EmergencyProfile.setActivationTime((GXDateTime)GXDLMSClient.changeType((byte[])tmp[1], DataType.DATETIME));
            EmergencyProfile.setDuration((Long) tmp[2]);
        }
        else if (index == 9)
        {
            java.util.ArrayList<Integer> list = new java.util.ArrayList<Integer>();
            for (Object it : (Object[])value)
            {
                list.add(((Number)it).intValue());
            }
            EmergencyProfileGroupIDs = toIntArray(list);
        }
        else if (index == 10)
        {
            EmergencyProfileActive = (Boolean)value;
        }
        else if (index == 11)
        {
            Object[] tmp = (Object[])value;
            Object[] tmp1 = (Object[])tmp[0];
            Object[] tmp2 = (Object[])tmp[1];
            ActionOverThreshold.setLogicalName(GXDLMSClient.changeType((byte[])tmp1[0], DataType.OCTET_STRING).toString());
            ActionOverThreshold.setScriptSelector(((Number)(tmp1[1])).intValue());
            ActionUnderThreshold.setLogicalName(GXDLMSClient.changeType((byte[])tmp2[0], DataType.OCTET_STRING).toString());
            ActionUnderThreshold.setScriptSelector(((Number)(tmp2[1])).intValue());
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}