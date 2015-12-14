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

import gurux.dlms.GXDLMSSettings;

public interface IGXDLMSBase {

    /**
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     * 
     * @return Collection of attributes to read.
     */
    int[] getAttributeIndexToRead();

    /**
     * @return Amount of attributes.
     */
    int getAttributeCount();

    /**
     * @return Amount of methods.
     */
    int getMethodCount();

    /**
     * Returns value of given attribute.
     * 
     * @param settings
     *            DLMS settings.
     * @param index
     *            Attribute index.
     * @param selector
     *            Optional selector.
     * @param parameters
     *            Optional parameters.
     * @return Returned value.
     */
    Object getValue(GXDLMSSettings settings, int index, int selector,
            Object parameters);

    /**
     * Set value of given attribute.
     * 
     * @param settings
     *            DLMS settings.
     * @param index
     *            Attribute index.
     * @param value
     *            New value.
     */
    void setValue(GXDLMSSettings settings, int index, Object value);

    /**
     * Server calls invoke method.
     * 
     * @param settings
     *            Server settings.
     * @param index
     *            Method index.
     * @param parameters
     *            Optional method parameters.
     * @return Reply for the client,.
     */
    byte[] invoke(GXDLMSSettings settings, int index, Object parameters);
}
