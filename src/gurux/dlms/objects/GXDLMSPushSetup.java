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
import gurux.dlms.enums.MessageType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.ServiceType;
import java.util.AbstractMap.SimpleEntry;

public class GXDLMSPushSetup extends GXDLMSObject implements IGXDLMSBase
{
    private java.util.ArrayList<GXDLMSPushObject> PushObjectList;
    private GXSendDestinationAndMethod SendDestinationAndMethod;
    private java.util.ArrayList<java.util.Map.Entry<GXDateTime, GXDateTime>> CommunicationWindow;
    private int RandomisationStartInterval;
    private int NumberOfRetries;
    private int RepetitionDelay;


    /**  
     Constructor.
    */
    public GXDLMSPushSetup()
    {
        super(ObjectType.PUSH_SETUP);        
        PushObjectList = new java.util.ArrayList<GXDLMSPushObject>();
        SendDestinationAndMethod = new GXSendDestinationAndMethod();
        CommunicationWindow = new java.util.ArrayList<java.util.Map.Entry<GXDateTime, GXDateTime>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSPushSetup(String ln)
    {
        super(ObjectType.PUSH_SETUP, ln, 0);
        PushObjectList = new java.util.ArrayList<GXDLMSPushObject>();
        SendDestinationAndMethod = new GXSendDestinationAndMethod();
        CommunicationWindow = new java.util.ArrayList<java.util.Map.Entry<GXDateTime, GXDateTime>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSPushSetup(String ln, int sn)
    {
        super(ObjectType.PUSH_SETUP, ln, sn);
        PushObjectList = new java.util.ArrayList<GXDLMSPushObject>();
        SendDestinationAndMethod = new GXSendDestinationAndMethod();
        CommunicationWindow = new java.util.ArrayList<java.util.Map.Entry<GXDateTime, GXDateTime>>();
    }

   /** 
    Defines the list of attributes or objects to be pushed. 
    Upon a call of the push (data) method the selected attributes are sent to the desti-nation 
    defined in send_destination_and_method.
*/
    public final java.util.ArrayList<GXDLMSPushObject> getPushObjectList()
    {
        return PushObjectList;
    }

    public final GXSendDestinationAndMethod getSendDestinationAndMethod()
    {
        return SendDestinationAndMethod;
    }

    /** 
     Contains the start and end date/time 
     stamp when the communication window(s) for the push become active 
     (for the start instant), or inac-tive (for the end instant).
    */
    public final java.util.ArrayList<java.util.Map.Entry<GXDateTime, GXDateTime>> getCommunicationWindow()
    {
        return CommunicationWindow;
    }

    /** 
     To avoid simultaneous network connections of a lot of devices at ex-actly 
     the same point in time, a randomisation interval in seconds can be defined.
     This means that the push operation is not started imme-diately at the
     beginning of the first communication window but started randomly delayed.
    */
    public final int getRandomisationStartInterval()
    {
        return RandomisationStartInterval;
    }
    public final void setRandomisationStartInterval(int value)
    {
        RandomisationStartInterval = value;
    }
    /** 
     The maximum number of retrials in case of unsuccessful push at-tempts. After a successful push no further push attempts are made until the push setup is triggered again. 
     A value of 0 means no repetitions, i.e. only the initial connection at-tempt is made.
    */

    public final int getNumberOfRetries()
    {
        return NumberOfRetries;
    }
    public final void setNumberOfRetries(byte value)
    {
        NumberOfRetries = value;
    }

    public final int getRepetitionDelay()
    {
        return RepetitionDelay;
    }

    public final void setRepetitionDelay(int value)
    {
        RepetitionDelay = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), PushObjectList, 
            SendDestinationAndMethod, CommunicationWindow, 
            RandomisationStartInterval, NumberOfRetries, RepetitionDelay};
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
        //PushObjectList
        if (canRead(2))
        {
            attributes.add(2);
        }
        //SendDestinationAndMethod
        if (canRead(3))
        {
            attributes.add(3);
        }
        //CommunicationWindow
        if (canRead(4))
        {
            attributes.add(4);
        }
        //RandomisationStartInterval
        if (canRead(5))
        {
            attributes.add(5);
        }
        //NumberOfRetries
        if (canRead(6))
        {
            attributes.add(6);
        }
        //RepetitionDelay
        if (canRead(7))
        {
            attributes.add(7);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 7;
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
        if (index == 3)
        {
            return DataType.STRUCTURE;
        }
        if (index == 4)
        {
            return DataType.ARRAY;
        }
        if (index == 5)
        {
            return DataType.UINT16;
        }
        if (index == 6)
        {
            return DataType.UINT8;
        }
        if (index == 7)
        {
            return DataType.UINT16;
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
            return PushObjectList;
        }
        if (index == 3)
        {
            return SendDestinationAndMethod;
        }
        if (index == 4)
        {
            return CommunicationWindow;
        }
        if (index == 5)
        {
            return RandomisationStartInterval;
        }
        if (index == 6)
        {
            return NumberOfRetries;
        }
        if (index == 7)
        {
            return RepetitionDelay;
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
            PushObjectList.clear();
            if (value instanceof Object[])
            {
                for (Object it : (Object[])value)
                {
                    Object[] tmp = (Object[]) it;
                    GXDLMSPushObject obj = new GXDLMSPushObject();
                    obj.setType(ObjectType.forValue(((Number)tmp[0]).intValue()));
                    obj.setLogicalName(GXDLMSClient.changeType((byte[])tmp[1], DataType.BITSTRING).toString());
                    obj.setAttributeIndex(((Number)tmp[2]).intValue());
                    obj.setDataIndex(((Number)tmp[3]).intValue());
                    PushObjectList.add(obj);
                }
            }
        }
        else if (index == 3)
        {
            Object[] tmp = (Object[]) value;
            if (tmp != null)
            {
                SendDestinationAndMethod.setService(ServiceType.forValue(((Number)tmp[0]).intValue()));
                SendDestinationAndMethod.setDestination(new String((byte[]) tmp[1]));
                SendDestinationAndMethod.setMessage(MessageType.forValue(((Number)tmp[2]).intValue()));
            }
        }
        else if (index == 4)
        {
            CommunicationWindow.clear();
            if (value instanceof Object[])
            {
                for (Object it : (Object[])value)
                {
                    Object[] tmp = (Object[]) it;
                    GXDateTime start = (GXDateTime) GXDLMSClient.changeType((byte[]) tmp[0], DataType.DATETIME);
                    GXDateTime end = (GXDateTime) GXDLMSClient.changeType((byte[]) tmp[1], DataType.DATETIME);
                    CommunicationWindow.add(new SimpleEntry<GXDateTime, GXDateTime>(start, end));
                }
            }
        }
        else if (index == 5)
        {
            RandomisationStartInterval = ((Number)value).intValue();
        }
        else if (index == 6)
        {
            NumberOfRetries = ((Number)value).intValue();
        }
        else if (index == 7)
        {
            RepetitionDelay = ((Number)value).intValue();
        }                       
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}