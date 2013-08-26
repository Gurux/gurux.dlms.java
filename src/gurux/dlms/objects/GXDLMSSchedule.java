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

import gurux.dlms.enums.ObjectType;

public class GXDLMSSchedule extends GXDLMSObject
{
    /**  
     Constructor.
    */
    public GXDLMSSchedule()
    {
        super(ObjectType.SCHEDULE);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
    */
    public GXDLMSSchedule(String ln)
    {
        super(ObjectType.SCHEDULE, ln, 0);
    }

    /**  
     Constructor.

     @param ln Logican Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSSchedule(String ln, int sn)
    {
        super(ObjectType.SCHEDULE, ln, sn);
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName()};
    }
}