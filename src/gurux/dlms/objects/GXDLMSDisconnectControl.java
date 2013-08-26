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
import gurux.dlms.enums.ControlMode;
import gurux.dlms.enums.ControlState;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ObjectType;

public class GXDLMSDisconnectControl extends GXDLMSObject implements IGXDLMSBase
{
    private boolean OutputState;
    private ControlState ControlState;        
    private ControlMode ControlMode;

    /**  
     Constructor.
    */
    public GXDLMSDisconnectControl()
    {
        super(ObjectType.DISCONNECT_CONTROL);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSDisconnectControl(String ln)
    {
        super(ObjectType.DISCONNECT_CONTROL, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSDisconnectControl(String ln, int sn)
    {
        super(ObjectType.DISCONNECT_CONTROL, ln, sn);
    }

    /** 
     Output state of COSEM Disconnect Control object.
    */
    public final boolean getOutputState()
    {
        return OutputState;
    }
    public final void setOutputState(boolean value)
    {
        OutputState = value;
    }

    /** 
     Control state of COSEM Disconnect Control object.
    */
    public final ControlState getControlState()
    {
        return ControlState;
    }
    public final void setControlState(ControlState value)
    {
        ControlState = value;
    }
    
     /** 
     Control state of COSEM Disconnect Control object.
    */
    public final ControlMode getControlMode()
    {
        return ControlMode;
    }
    public final void setControlMode(ControlMode value)
    {
        ControlMode = value;
    }
    
    /*
        Forces the disconnect control object into 'disconnected' state if remote
        disconnection is enabled (control mode > 0).
    */
    public byte[][] remoteDisconnect(GXDLMSClient client)
    {
        byte[] ret = client.method(getName(), getObjectType(), 1, (int) 0);
        return new byte[][]{ret};
    }
    
    /*
     * Forces the disconnect control object into the 'ready_for_reconnection'
        state if a direct remote reconnection is disabled (control_mode = 1, 3, 5, 6).
        Forces the disconnect control object into the 'connected' state if a direct
        remote reconnection is enabled (control_mode = 2, 4).
     */
    public byte[][] remoteReconnect(GXDLMSClient client)
    {
        byte[] ret = client.method(getName(), getObjectType(), 2, (int) 0);
        return new byte[][]{ret};
    }        
    
    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getOutputState(), getControlState(), getControlMode()};
    }
    
    @Override
    public void invoke(int index, Object parameters)
    {
        
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
        //Mikko
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
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
        return 2;
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
            type[0] = DataType.BOOLEAN;
            return getOutputState();
        }    
        if (index == 3)
        {
            type[0] = DataType.ENUM;
            return getControlState().ordinal();
        }    
        if (index == 4)
        {
            type[0] = DataType.ENUM;
            return getControlMode().ordinal();
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
            if (value != null)
            {
                setLogicalName(GXDLMSObject.toLogicalName((byte[]) value));            
            }
        }
        else if (index == 2)
        {
            if (value == null)
            {
                setOutputState(false);
            }
            else
            {
                setOutputState((Boolean)value);            
            }            
        }
        else if (index == 3)
        {
            if (value == null)
            {
                setControlState(ControlState.DISCONNECTED);
            }
            else
            {
                setControlState(ControlState.values()[((Number)value).intValue()]);
            }           
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setControlMode(ControlMode.NONE);
            }
            else
            {
                setControlMode(ControlMode.values()[((Number)value).intValue()]);
            }
            
        }
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }    
}
