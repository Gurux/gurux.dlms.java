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

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.MethodAccessMode;

public class GXDLMSAttributeSettings {
    private int minimumVersion;
    private GXObisValueItemCollection values = new GXObisValueItemCollection();
    private boolean staticValue;
    private DataType uiType = DataType.NONE;
    private MethodAccessMode methodAccess = MethodAccessMode.NO_ACCESS;
    private AccessMode access = AccessMode.READ_WRITE;
    private DataType type = DataType.NONE;
    private GXAttributeCollection parent;
    private String name;
    private int index;
    private int order;

    /**
     * Constructor.
     */
    public GXDLMSAttributeSettings() {

    }

    /**
     * Constructor.
     * 
     * @param forIndex
     *            Attribute index.
     */
    public GXDLMSAttributeSettings(final int forIndex) {
        this();
        setIndex(forIndex);
    }

    /*
     * Copy settings.
     */
    public final void copyTo(final GXDLMSAttributeSettings target) {
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
     * @return Attribute name.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param value
     *            Attribute name.
     */
    public final void setName(final String value) {
        name = value;
    }

    /**
     * @return Attribute Index.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            Attribute Index.
     */
    public final void setIndex(final int value) {
        index = value;
    }

    /*
     * Parent collection.
     */
    public final GXAttributeCollection getParent() {
        return parent;
    }

    public final void setParent(final GXAttributeCollection value) {
        parent = value;
    }

    /**
     * @return Attribute data type.
     */
    public final gurux.dlms.enums.DataType getType() {
        return type;
    }

    /**
     * @param value
     *            Attribute data type.
     */
    public final void setType(final DataType value) {
        type = value;
    }

    /**
     * @return Data type that user ẃant's to see.
     */
    public final DataType getUIType() {
        return uiType;
    }

    /**
     * @param value
     *            Data type that user ẃant's to see.
     */
    public final void setUIType(final DataType value) {
        uiType = value;
    }

    /*
     * Attribute access mode.
     */
    public final AccessMode getAccess() {
        return access;
    }

    public final void setAccess(final AccessMode value) {
        access = value;
    }

    /*
     * Method access mode.
     */
    public final MethodAccessMode getMethodAccess() {
        return methodAccess;
    }

    public final void setMethodAccess(final MethodAccessMode value) {
        methodAccess = value;
    }

    /*
     * Is attribute static. If it is static it is needed to read only once.
     */
    public final boolean getStatic() {
        return staticValue;
    }

    public final void setStatic(final boolean value) {
        staticValue = value;
    }

    /**
     * @return Attribute values.
     */
    public final GXObisValueItemCollection getValues() {
        return values;
    }

    /**
     * @param value
     *            Attribute values.
     */
    public final void setValues(final GXObisValueItemCollection value) {
        values = value;
    }

    /**
     * @return Read order. User read order to set read order of attributes.
     */
    public final int getOrder() {
        return order;
    }

    /**
     * @param value
     *            Read order. User read order to set read order of attributes.
     */
    public final void setOrder(final int value) {
        order = value;
    }

    /**
     * @return Minimum version where this attribute is implemented.
     */
    public final int getMinimumVersion() {
        return minimumVersion;
    }

    /**
     * @param value
     *            Minimum version where this attribute is implemented.
     */
    public final void setMinimumVersion(final int value) {
        minimumVersion = value;
    }
}