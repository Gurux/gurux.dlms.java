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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

/**
 * Target object.
 */
public class GXDLMSTargetObject {
    /**
     * Target object.
     */
    private GXDLMSObject target;

    /**
     * Attribute Index of DLMS object.
     */
    private int attributeIndex;

    /**
     * @return Target object.
     */
    public final GXDLMSObject getTarget() {
        return target;
    }

    /**
     * @param value
     *            Target object.
     */
    public final void setTarget(final GXDLMSObject value) {
        target = value;
    }

    /**
     * @return Attribute Index of DLMS object.
     */
    public final int getAttributeIndex() {
        return attributeIndex;
    }

    /**
     * @param value
     *            Attribute Index of DLMS object.
     */
    public final void setAttributeIndex(final int value) {
        attributeIndex = value;
    }

    /**
     * Constructor
     */
    public GXDLMSTargetObject() {

    }

    /**
     * Constructor.
     * 
     * @param target
     *            Target object.
     * @param attributeIndex
     *            Attribute index.
     */
    public GXDLMSTargetObject(GXDLMSObject target, int attributeIndex) {
        setTarget(target);
        setAttributeIndex(attributeIndex);
    }
}