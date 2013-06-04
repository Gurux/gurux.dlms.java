/*
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
*/

package gurux.dlms.manufacturersettings;

import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.AccessMode;

public class GXDLMSAttributeSettings
{
    private int privateMinimumVersion;
    private GXObisValueItemCollection privateValues = new GXObisValueItemCollection();
    private boolean privateStatic;
    private gurux.dlms.enums.DataType privateUIType = gurux.dlms.enums.DataType.NONE;
    private MethodAccessMode privateMethodAccess = MethodAccessMode.NO_ACCESS;
    private AccessMode privateAccess = AccessMode.READ_WRITE;
    private gurux.dlms.enums.DataType privateType = gurux.dlms.enums.DataType.NONE;
    private GXAttributeCollection privateParent;
    private String privateName;
    private int privateIndex;
    private int privateOrder;

    /** 
     Constructor.
    */
    public GXDLMSAttributeSettings()
    {
        
    }

    /** 
     Constructor.
    */
    public GXDLMSAttributeSettings(int index)
    {
        this();
        setIndex(index);
    }

    /*
     * Copy settings.
     */
    public final void copyTo(GXDLMSAttributeSettings target)
    {
        target.setName(this.getName());
        target.setIndex(getIndex());
        target.setType(getType());
        target.setUIType(getUIType());
        target.setAccess(getAccess());
        target.setStatic(getStatic());
        target.setValues(getValues());
        target.setOrder(getOrder());
        target.setMinimumVersion(getMinimumVersion());
    }

    /** 
     Attribute name.
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
     Attribute Index.
    */
    public final int getIndex()
    {
        return privateIndex;
    }
    public final void setIndex(int value)
    {
        privateIndex = value;
    }

    /*
     * Parent collection.
     */
    public final GXAttributeCollection getParent()
    {
        return privateParent;
    }
    public final void setParent(GXAttributeCollection value)
    {
        privateParent = value;
    }

    /** 
     Attribute data type.
    */
    public final gurux.dlms.enums.DataType getType()
    {
        return privateType;
    }
    public final void setType(gurux.dlms.enums.DataType value)
    {
        privateType = value;
    }

    /** 
     Data type that user ẃant's to see.
    */
    public final gurux.dlms.enums.DataType getUIType()
    {
        return privateUIType;
    }
    public final void setUIType(gurux.dlms.enums.DataType value)
    {
        privateUIType = value;
    }

    /*
     * Attribute access mode.
     */
    public final AccessMode getAccess()
    {
        return privateAccess;
    }
    public final void setAccess(AccessMode value)
    {
        privateAccess = value;
    }

    /*
     * Method access mode.
     */
    public final MethodAccessMode getMethodAccess()
    {
        return privateMethodAccess;
    }
    public final void setMethodAccess(MethodAccessMode value)
    {
        privateMethodAccess = value;
    }

    /*
     * Is attribute static. If it is static it is needed to read only once.
     */
    public final boolean getStatic()
    {
        return privateStatic;
    }
    public final void setStatic(boolean value)
    {
        privateStatic = value;
    }

    /** 
     Attribute values.
    */
    public final GXObisValueItemCollection getValues()
    {
        return privateValues;
    }
    public final void setValues(GXObisValueItemCollection value)
    {
        privateValues = value;
    }

    /** 
     Read order.
     * 
     * User read order to set read order of attributes.
    */
    public final int getOrder()
    {
        return privateOrder;
    }
    public final void setOrder(int value)
    {
        privateOrder = value;
    }

    /** 
     Minimum version where this attribute is implemented.
    */
    public final int getMinimumVersion()
    {
        return privateMinimumVersion;
    }
    public final void setMinimumVersion(int value)
    {
        privateMinimumVersion = value;
    }
}