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

package gurux.dlms.manufacturersettings;

import gurux.dlms.enums.Authentication;

public class GXManufacturer
{
    private InactivityMode privateInactivityMode = InactivityMode.KEEPALIVE;
    private boolean privateUseIEC47;
    private boolean privateForceInactivity;
    private boolean privateUseLogicalNameReferencing;
    private String privateIdentification;
    private GXObisCodeCollection m_ObisCodes;
    private String privateName;
    private java.util.ArrayList<GXAuthentication> privateSettings;
    private java.util.ArrayList<GXServerAddress> privateServerSettings;
    private int privateKeepAliveInterval;
    private StartProtocolType privateStartProtocol = StartProtocolType.IEC;    

    /** 
     Constructor.
    */
    public GXManufacturer()
    {
        setInactivityMode(InactivityMode.KEEPALIVE);
        setStartProtocol(StartProtocolType.IEC);
        m_ObisCodes = new GXObisCodeCollection();
        setSettings(new java.util.ArrayList<GXAuthentication>());
        setServerSettings(new java.util.ArrayList<GXServerAddress>());
        setKeepAliveInterval(40000);
    }

    /** 
     Manufacturer Identification.
     Device returns manufacturer ID when connection to the meter is made.
    */
    public final String getIdentification()
    {
        return privateIdentification;
    }
    public final void setIdentification(String value)
    {
        privateIdentification = value;
    }

    /** 
     Real name of the manufacturer.
    */
    public final String getName()
    {
        return privateName;
    }
    public final void setName(String value)
    {
        privateName = value;
    }

    /** 
     Is Logical name referencing used.
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
     Is Keep alive message used.
    */
    public final InactivityMode getInactivityMode()
    {
        return privateInactivityMode;
    }
    public final void setInactivityMode(InactivityMode value)
    {
        privateInactivityMode = value;
    }

    /** 
     Is Keep alive message forced for network connection as well.
    */
    public final boolean getForceInactivity()
    {
        return privateForceInactivity;
    }
    public final void setForceInactivity(boolean value)
    {
        privateForceInactivity = value;
    }

    public final GXObisCodeCollection getObisCodes()
    {
        return m_ObisCodes;
    }

    /** 
     Is IEC 62056-47 supported.
    */
    public final boolean getUseIEC47()
    {
        return privateUseIEC47;
    }
    public final void setUseIEC47(boolean value)
    {
        privateUseIEC47 = value;
    }

    /** 
     Is IEC 62056-21 skipped when using serial port connection.
    */
    public final StartProtocolType getStartProtocol()
    {
        return privateStartProtocol;
    }
    public final void setStartProtocol(StartProtocolType value)
    {
        privateStartProtocol = value;
    }

    /** 
     Keep Alive interval.
    */
    public final int getKeepAliveInterval()
    {
        return privateKeepAliveInterval;
    }
    public final void setKeepAliveInterval(int value)
    {
        privateKeepAliveInterval = value;
    }

    /** 
     Initialize settings.
    */
    public final java.util.ArrayList<GXAuthentication> getSettings()
    {
        return privateSettings;
    }
    public final void setSettings(java.util.ArrayList<GXAuthentication> value)
    {
        privateSettings = value;
    }

    /** 
     Server settings
    */
    public final java.util.ArrayList<GXServerAddress> getServerSettings()
    {
        return privateServerSettings;
    }
    public final void setServerSettings(java.util.ArrayList<GXServerAddress> value)
    {
        privateServerSettings = value;
    }

    public final GXAuthentication getAuthentication(Authentication authentication)
    {
        for (GXAuthentication it : getSettings())
        {
                if (it.getType() == authentication)
                {
                        return it;
                }
        }
        return null;
    }

    public final GXAuthentication getActiveAuthentication()
    {
        for (GXAuthentication it : getSettings())
        {
            if (it.getSelected())
            {
                    return it;
            }
        }
        if (!getSettings().isEmpty())
        {
            return getSettings().get(0);
        }
        return null;
    }

    public final GXServerAddress getServer(HDLCAddressType type)
    {
        for (GXServerAddress it : this.getServerSettings())
        {
            if (it.getHDLCAddress() == type)
            {
                return it;
            }
        }
        return null;
    }

    public final GXServerAddress getActiveServer()
    {
        for (GXServerAddress it : this.getServerSettings())
        {
            if (it.getSelected())
            {
                return it;
            }
        }
        if (!this.getServerSettings().isEmpty())
        {
            return this.getServerSettings().get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object convertTo(Object value, Class type)
    {
        //check if the id can be assigned for the field type, otherwise convert the id to the appropriate type
        if (value != null && !type.isAssignableFrom(value.getClass()))
        {
            if (type == short.class || type == Short.class)
            {
                return ((Number)value).shortValue();
            }
            else if (type == char.class || type == Character.class)
            {
                return Character.valueOf(value.toString().charAt(0));                
            }
            else if (type == int.class || type == Integer.class)
            {
                return ((Number)value).intValue();
            }
            else if (type == long.class || type == Long.class)
            {
                return ((Number)value).longValue();
            }
            else if (type == boolean.class || type == Boolean.class)
            {
                return Boolean.getBoolean(value.toString());
            }
            else if (type == byte.class || type == Byte.class)
            {
                return ((Number)value).byteValue();
            }
            else if (type == float.class || type == Float.class)
            {
                return ((Number)value).floatValue();
            }
            else if (type == double.class || type == Double.class)
            {
                return ((Number)value).doubleValue();
            }
            /*
            else if (type == BigDecimal.class)
            {
                return new BigDecimal(value.toString());
            }
            else if (type == BigInteger.class)
            {
                return new BigInteger(value.toString());
            }
             */
            else if (type == String.class)
            {
                return value.toString();
            }
            else
            {
                return null;
            }
        }
        return value;
    }
    
    static int evaluateSN(int sn)
    {        
        return (sn % 10000) + 1000;
    }
    
    /** 
     Count server address from physical and logical addresses.
    */
    public static Object countServerAddress(HDLCAddressType addressing, Object physicalAddress, int LogicalAddress)
    {
        Object value;
        if (addressing == HDLCAddressType.CUSTOM)
        {
            value = convertTo(physicalAddress, physicalAddress.getClass());
        }
        else
        {
            if (addressing == HDLCAddressType.SERIAL_NUMBER)
            {                                
               physicalAddress = convertTo(evaluateSN(((Number)physicalAddress).intValue()), physicalAddress.getClass());
            }
            if (physicalAddress.getClass() == Byte.class)
            {
                byte tmp = (Byte)physicalAddress;
                value = ((LogicalAddress & 0x7) << 5) | ((tmp & 0x7) << 1) | 0x1;
                value = convertTo(value, byte.class);
            }
            else if (physicalAddress.getClass() == Short.class)
            {
                int physicalID = Integer.parseInt(physicalAddress.toString());
                int logicalID = LogicalAddress;
                int total = (physicalID & 0x7F) << 1 | 1;
                value = (Integer) (total | (logicalID << 9));                    
                value = convertTo(value, short.class);
            }
            else if (physicalAddress.getClass() == Integer.class)
            {
                int physicalID = Integer.parseInt(physicalAddress.toString());
                int logicalID = LogicalAddress;
                int total = (((physicalID >> 7) & 0x7F) << 8) | (physicalID & 0x7F);
                value = (Integer)(((total << 1) | 1 | (logicalID << 17)));
            }
            else
            {
                throw new RuntimeException("Unknown physical address type.");
            }
        }
        return value;
    }    
}