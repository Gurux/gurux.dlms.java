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

import gurux.dlms.internal.GXCommon;

/** 
 GXDLMSLNSettings contains commands for retrieving, and setting, the
 Logical Name settings of the server, shortly said LN referencing support.
*/
public class GXDLMSLNSettings
{
    /** 
     Settings.
    */
    byte[] conformanceBlock = new byte[3];

    /** 
     Constructor.
    */
    GXDLMSLNSettings(byte[] conformanceBlock)
    {
        //Set default values.
        this.conformanceBlock = conformanceBlock;
    }

    final void copyTo(GXDLMSLNSettings target)
    {
        target.conformanceBlock = this.conformanceBlock;
    }

    public final boolean getAttribute0SetReferencing()
    {
        return GXCommon.getBits(conformanceBlock[1], 0x80);
    }
    public final void setAttribute0SetReferencing(boolean value)
    {
        conformanceBlock[1] = GXCommon.setBits(conformanceBlock[1], 0x80, value);
    }

    public final boolean getPriorityManagement()
    {
        return GXCommon.getBits(conformanceBlock[1], 0x40);
    }
    public final void setPriorityManagement(boolean value)
    {
        conformanceBlock[1] = GXCommon.setBits(conformanceBlock[1], 0x40, value);
    }

    public final boolean getAttribute0GetReferencing()
    {
        return GXCommon.getBits(conformanceBlock[1], 0x20);
    }
    public final void setAttribute0GetReferencing(boolean value)
    {
        conformanceBlock[1] = GXCommon.setBits(conformanceBlock[1], 0x20, value);
    }

    /** 
     Checks, if data from the server can be read in blocks.
    */
    public final boolean getGetBlockTransfer()
    {
        return GXCommon.getBits(conformanceBlock[1], 0x10);
    }
    public final void setGetBlockTransfer(boolean value)
    {
        conformanceBlock[1] = GXCommon.setBits(conformanceBlock[1], 0x10, value);
    }

    /** 
     Checks, if data to the server can be written in blocks.
    */
    public final boolean getSetBlockTransfer()
    {
        return GXCommon.getBits(conformanceBlock[1], 0x8);
    }
    public final void setSetBlockTransfer(boolean value)
    {
        conformanceBlock[1] = GXCommon.setBits(conformanceBlock[1], 0x8, value);
    }


    public final boolean getActionBlockTransfer()
    {
        return GXCommon.getBits(conformanceBlock[1], 0x4);
    }
    public final void setActionBlockTransfer(boolean value)
    {
        conformanceBlock[1] = GXCommon.setBits(conformanceBlock[1], 0x4, value);
    }


    public final boolean getMultipleReferences()
    {
        return GXCommon.getBits(conformanceBlock[1], 0x2);
    }
    public final void setMultipleReferences(boolean value)
    {
        conformanceBlock[1] = GXCommon.setBits(conformanceBlock[1], 0x2, value);
    }


    /** 
     Checks, if data can be read from the server.
    */
    public final boolean getGet()
    {
        return GXCommon.getBits(conformanceBlock[2], 0x8);
    }
    public final void setGet(boolean value)
    {
        conformanceBlock[2] = GXCommon.setBits(conformanceBlock[2], 0x8, value);
    }


    /** 
     Checks, if data can be written to the server.
    */
    public final boolean getSet()
    {
        return GXCommon.getBits(conformanceBlock[2], 0x4);
    }
    public final void setSet(boolean value)
    {
        conformanceBlock[2] = GXCommon.setBits(conformanceBlock[2], 0x4, value);
    }

    public final boolean getAction()
    {
        return GXCommon.getBits(conformanceBlock[2], 0x2);
    }
    public final void setAction(boolean value)
    {
        conformanceBlock[2] = GXCommon.setBits(conformanceBlock[2], 0x2, value);
    }

    public final boolean getEventNotification()
    {
        return GXCommon.getBits(conformanceBlock[2], 0x1);
    }
    public final void setEventNotification(boolean value)
    {
        conformanceBlock[2] = GXCommon.setBits(conformanceBlock[2], 0x1, value);
    }

    /** 
     Is selective access used.
    */
    public final boolean getSelectiveAccess()
    {
        return GXCommon.getBits(conformanceBlock[2], 0x80);
    }
    public final void setSelectiveAccess(boolean value)
    {
        conformanceBlock[2] = GXCommon.setBits(conformanceBlock[2], 0x80, value);
    }
}