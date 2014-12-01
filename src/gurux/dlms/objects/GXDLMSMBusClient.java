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
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.List;

public class GXDLMSMBusClient extends GXDLMSObject implements IGXDLMSBase
{
    private long CapturePeriod;
    private int PrimaryAddress;    
    private String MBusPortReference;
    private List<java.util.Map.Entry<String, String>> CaptureDefinition;
    private long IdentificationNumber;
    private int ManufacturerID;
    private int DataHeaderVersion;
    private int DeviceType;
    private int AccessNumber;
    private int Status;
    private int Alarm;

    /**  
     Constructor.
    */
    public GXDLMSMBusClient()
    {
        super(ObjectType.MBUS_CLIENT);
        CaptureDefinition = new java.util.ArrayList<java.util.Map.Entry<String, String>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
    */
    public GXDLMSMBusClient(String ln)
    {
        super(ObjectType.MBUS_CLIENT, ln, 0);
        CaptureDefinition = new java.util.ArrayList<java.util.Map.Entry<String, String>>();
    }

    /**  
     Constructor.

     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSMBusClient(String ln, int sn)
    {
        super(ObjectType.MBUS_CLIENT, ln, sn);
        CaptureDefinition = new java.util.ArrayList<java.util.Map.Entry<String, String>>();
    }

   /** 
    Provides reference to an "M-Bus master port setup" object, used to configure
    an M-Bus port, each interface allowing to exchange data with one or more
    M-Bus slave devices
*/
    public final String getMBusPortReference()
    {
        return MBusPortReference;
    }
    public final void setMBusPortReference(String value)
    {
        MBusPortReference = value;
    }

    public final List<java.util.Map.Entry<String, String>> getCaptureDefinition()
    {
        return CaptureDefinition;
    }

    public final long getCapturePeriod()
    {
        return CapturePeriod;
    }
    public final void setCapturePeriod(long value)
    {
        CapturePeriod = value;
    }

    public final int getPrimaryAddress()
    {
        return PrimaryAddress;
    }
    public final void setPrimaryAddress(int value)
    {
        PrimaryAddress = value;
    }

    public final long getIdentificationNumber()
    {
        return IdentificationNumber;
    }
    public final void setIdentificationNumber(long value)
    {
        IdentificationNumber = value;
    }

    public final int getManufacturerID()
    {
        return ManufacturerID;
    }
    public final void setManufacturerID(int value)
    {
        ManufacturerID = value;
    }

    /*
     * Carries the Version element of the data header as specified in EN
     * 13757-3 sub-clause 5.6.
     */
    public final int getDataHeaderVersion()
    {
        return DataHeaderVersion;
    }
    public final void setDataHeaderVersion(int value)
    {
        DataHeaderVersion = value;
    }

    public final int getDeviceType()
    {
        return DeviceType;
    }
    public final void setDeviceType(int value)
    {
        DeviceType = value;
    }

    public final int getAccessNumber()
    {
        return AccessNumber;
    }
    public final void setAccessNumber(int value)
    {
        AccessNumber = value;
    }

    public final int getStatus()
    {
        return Status;
    }
    public final void setStatus(int value)
    {
        Status = value;
    }

    public final int getAlarm()
    {
        return Alarm;
    }
    public final void setAlarm(int value)
    {
        Alarm = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), MBusPortReference, CaptureDefinition, CapturePeriod, 
            PrimaryAddress, IdentificationNumber, ManufacturerID, DataHeaderVersion, DeviceType, AccessNumber, 
            Status, Alarm};
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
        //MBusPortReference
        if (canRead(2))
        {
            attributes.add(2);
        }
        //CaptureDefinition
        if (canRead(3))
        {
            attributes.add(3);
        }
        //CapturePeriod
        if (canRead(4))
        {
            attributes.add(4);
        }
        //PrimaryAddress
        if (canRead(5))
        {
            attributes.add(5);
        }
        //IdentificationNumber
        if (canRead(6))
        {
            attributes.add(6);
        }
        //ManufacturerID
        if (canRead(7))
        {
            attributes.add(7);
        }
        //Version
        if (canRead(8))
        {
            attributes.add(8);
        }
        //DeviceType
        if (canRead(9))
        {
            attributes.add(9);
        }
        //AccessNumber
        if (canRead(10))
        {
            attributes.add(10);
        }
        //Status
        if (canRead(11))
        {
            attributes.add(11);
        }
        //Alarm
        if (canRead(12))
        {
            attributes.add(12);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 12;
    }
    
    /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 8;
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
            return DataType.OCTET_STRING;
        }
        if (index == 3)
        {
            return DataType.ARRAY;
        }
        if (index == 4)
        {
            return DataType.UINT32;
        }
        if (index == 5)
        {
            return DataType.UINT8;
        }
        if (index == 6)
        {
            return DataType.UINT32;
        }
        if (index == 7)
        {
            return DataType.UINT16;
        }
        if (index == 8)
        {
            return DataType.UINT8;
        }
        if (index == 9)
        {
            return DataType.UINT8;
        }
        if (index == 10)
        {
            return DataType.UINT8;
        }
        if (index == 11)
        {
            return DataType.UINT8;
        }
        if (index == 12)
        {
            return DataType.UINT8;
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
            return MBusPortReference;
        }
        if (index == 3)
        {
            return CaptureDefinition;//TODO;
        }
        if (index == 4)
        {
            return CapturePeriod;
        }
        if (index == 5)
        {
            return PrimaryAddress;
        }
        if (index == 6)
        {
            return IdentificationNumber;
        }
        if (index == 7)
        {
            return ManufacturerID;
        }
        if (index == 8)
        {
            return DataHeaderVersion;
        }
        if (index == 9)
        {
            return DeviceType;
        }
        if (index == 10)
        {
            return AccessNumber;
        }
        if (index == 11)
        {
            return Status;
        }
        if (index == 12)
        {
            return Alarm;
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
            MBusPortReference = GXDLMSClient.changeType((byte[])value, DataType.OCTET_STRING).toString();
        }
        else if (index == 3)
        {
            CaptureDefinition.clear();
            for(Object it : (Object[]) value)
            {                    
                CaptureDefinition.add(new AbstractMap.SimpleEntry<String, String>(GXDLMSClient.changeType((byte[])Array.get(it, 0), DataType.OCTET_STRING).toString(),
                    GXDLMSClient.changeType((byte[])Array.get(it, 1), DataType.OCTET_STRING).toString()));
            }            
        }
        else if (index == 4)
        {
            CapturePeriod = ((Number)value).longValue();
        }
        else if (index == 5)
        {
            PrimaryAddress = ((Number)value).intValue();
        }
        else if (index == 6)
        {
            IdentificationNumber = ((Number)value).longValue();
        }
        else if (index == 7)
        {
            ManufacturerID = ((Number)value).intValue();
        }
        else if (index == 8)
        {
            DataHeaderVersion = ((Number)value).intValue();
        }
        else if (index == 9)
        {
            DeviceType = ((Number)value).intValue();
        }
        else if (index == 10)
        {
            AccessNumber = ((Number)value).intValue();
        }
        else if (index == 11)
        {
            Status = ((Number)value).intValue();
        }
        else if (index == 12)
        {
            Alarm = ((Number)value).intValue();
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
}