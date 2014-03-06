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

package gurux.dlms;

import gurux.dlms.enums.DataType;
import gurux.dlms.objects.*;

public class ValueEventArgs
{
    private Object m_Value;
    private boolean m_Handled;
    private GXDLMSObject m_Target;
    private int m_Index;    
    private DataType m_DataType; 
    private int m_Selector;
    
    /** 
     Target DLMS object.
    */
    public final GXDLMSObject getTarget()
    {
        return m_Target;
    }
    private void setTarget(GXDLMSObject value)
    {
        m_Target = value;
    }

    /** 
     Attribute index of queried object.
    */
    public final int getIndex()
    {
        return m_Index;
    }
    private void setIndex(int value)
    {
        m_Index = value;
    }

    /** 
     object value
    */
    public final Object getValue()
    {
        return m_Value;
    }
    public final void setValue(Object value)
    {
        m_Value = value;
    }
    
     /** 
     Data type of the value.
    */
    public final DataType getDataType()
    {
        return this.m_DataType;
    }
    public final void setDataType(DataType value)
    {
        this.m_DataType = value;
    }

    /** 
     Is request handled.
    */
    public final boolean getHandled()
    {
        return m_Handled;
    }
    public final void setHandled(boolean value)
    {
        m_Handled = value;
    }

    /** 
     Is request handled.
    */
    public final int getSelector()
    {
        return m_Selector;
    }
    /** 
     Constructor.
    */
    public ValueEventArgs(GXDLMSObject target, int index, int selector)
    {
        this.m_DataType = DataType.NONE;
        setTarget(target);
        setIndex(index);
        m_Selector = selector;
    }
}