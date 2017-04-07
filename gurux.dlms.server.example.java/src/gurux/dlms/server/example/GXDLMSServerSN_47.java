//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: http://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.server.example;

import gurux.dlms.enums.InterfaceType;
import gurux.dlms.objects.GXDLMSAssociationShortName;

/**
 * DLMS Server that uses Short Same referencing with IEC 62056-47 COSEM
 * transport layers for IPv4 networks. Example Iskraemeco uses this. Note! For
 * serial port communication is used GXDLMSServerSN.
 */
public class GXDLMSServerSN_47 extends GXDLMSBase {
    public GXDLMSServerSN_47() {
        super(new GXDLMSAssociationShortName(), InterfaceType.WRAPPER);
    }
}