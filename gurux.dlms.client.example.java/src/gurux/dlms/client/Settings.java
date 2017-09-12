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
package gurux.dlms.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gurux.common.IGXMedia;
import gurux.common.enums.TraceLevel;
import gurux.dlms.secure.GXDLMSSecureClient;

public class Settings {
    public IGXMedia media = null;
    public TraceLevel trace = TraceLevel.INFO;
    public boolean iec = false;
    public GXDLMSSecureClient client = new GXDLMSSecureClient(true);
    // Objects to read.
    public List<Map.Entry<String, Integer>> readObjects =
            new ArrayList<Map.Entry<String, Integer>>();
}