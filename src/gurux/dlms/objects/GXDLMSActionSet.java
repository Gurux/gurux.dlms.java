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

public class GXDLMSActionSet
{
    private GXDLMSActionItem m_ActionUp;
    private GXDLMSActionItem m_ActionDown;
    
    /** 
     Constructor.
    */
    public GXDLMSActionSet()
    {
        setActionUp(new GXDLMSActionItem());
        setActionDown(new GXDLMSActionItem());
    }

    public final GXDLMSActionItem getActionUp()
    {
        return m_ActionUp;
    }
    public final void setActionUp(GXDLMSActionItem value)
    {
        m_ActionUp = value;
    }

    public final GXDLMSActionItem getActionDown()
    {
        return m_ActionDown;
    }
    public final void setActionDown(GXDLMSActionItem value)
    {
        m_ActionDown = value;
    }
}
