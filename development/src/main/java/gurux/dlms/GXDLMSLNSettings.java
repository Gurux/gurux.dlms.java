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

import java.security.InvalidParameterException;

import gurux.dlms.internal.GXCommon;

/**
 * GXDLMSLNSettings contains commands for retrieving, and setting, the Logical
 * Name settings of the server, shortly said LN referencing support.
 */
public class GXDLMSLNSettings {
    /**
     * Settings.
     */
    private byte[] conformanceBlock;

    /**
     * Constructor.
     */
    GXDLMSLNSettings() {
        setConformanceBlock(new byte[3]);
    }

    /**
     * Constructor.
     * 
     * @param value
     *            Conformance block.
     */
    GXDLMSLNSettings(final byte[] value) {
        setConformanceBlock(value);
    }

    /**
     * Clear all bits.
     */
    public final void clear() {
        conformanceBlock[0] = 0;
        conformanceBlock[1] = 0;
        conformanceBlock[2] = 0;
    }

    final void copyTo(final GXDLMSLNSettings target) {
        target.setConformanceBlock(getConformanceBlock());
    }

    /**
     * @return Can COSEM object set with one message.
     */
    public final boolean getAttribute0SetReferencing() {
        return GXCommon.getBits(getConformanceBlock()[1], 0x80);
    }

    /**
     * @param value
     *            Can COSEM object set with one message.
     */
    public final void setAttribute0SetReferencing(final boolean value) {
        getConformanceBlock()[1] =
                GXCommon.setBits(getConformanceBlock()[1], 0x80, value);
    }

    public final boolean getPriorityManagement() {
        return GXCommon.getBits(getConformanceBlock()[1], 0x40);
    }

    public final void setPriorityManagement(final boolean value) {
        getConformanceBlock()[1] =
                GXCommon.setBits(getConformanceBlock()[1], 0x40, value);
    }

    /**
     * @return Can COSEM object get with one message.
     */
    public final boolean getAttribute0GetReferencing() {
        return GXCommon.getBits(getConformanceBlock()[1], 0x20);
    }

    /**
     * @param value
     *            Can COSEM object get with one message.
     */
    public final void setAttribute0GetReferencing(final boolean value) {
        getConformanceBlock()[1] =
                GXCommon.setBits(getConformanceBlock()[1], 0x20, value);
    }

    /**
     * @return Can data read in blocks.
     */
    public final boolean getGetBlockTransfer() {
        return GXCommon.getBits(getConformanceBlock()[1], 0x10);
    }

    /**
     * @param value
     *            Can data read in blocks.
     */
    public final void setGetBlockTransfer(final boolean value) {
        getConformanceBlock()[1] =
                GXCommon.setBits(getConformanceBlock()[1], 0x10, value);
    }

    /**
     * @return Can data write in blocks.
     */
    public final boolean getSetBlockTransfer() {
        return GXCommon.getBits(getConformanceBlock()[1], 0x8);
    }

    /**
     * @param value
     *            Can data write in blocks.
     */
    public final void setSetBlockTransfer(final boolean value) {
        getConformanceBlock()[1] =
                GXCommon.setBits(getConformanceBlock()[1], 0x8, value);
    }

    public final boolean getActionBlockTransfer() {
        return GXCommon.getBits(getConformanceBlock()[1], 0x4);
    }

    public final void setActionBlockTransfer(final boolean value) {
        getConformanceBlock()[1] =
                GXCommon.setBits(getConformanceBlock()[1], 0x4, value);
    }

    public final boolean getMultipleReferences() {
        return GXCommon.getBits(getConformanceBlock()[1], 0x2);
    }

    public final void setMultipleReferences(final boolean value) {
        getConformanceBlock()[1] =
                GXCommon.setBits(getConformanceBlock()[1], 0x2, value);
    }

    /**
     * @return Can data get from the server.
     */
    public final boolean getGet() {
        return GXCommon.getBits(getConformanceBlock()[2], 0x10);
    }

    /**
     * @param value
     *            Can data get from the server.
     */
    public final void setGet(final boolean value) {
        getConformanceBlock()[2] =
                GXCommon.setBits(getConformanceBlock()[2], 0x10, value);
    }

    /**
     * @return Can data set to the server.
     */
    public final boolean getSet() {
        return GXCommon.getBits(getConformanceBlock()[2], 0x8);
    }

    /**
     * @param value
     *            Can data set to the server.
     */
    public final void setSet(final boolean value) {
        getConformanceBlock()[2] =
                GXCommon.setBits(getConformanceBlock()[2], 0x8, value);
    }

    /**
     * @return Can client call actions (methods).
     */
    public final boolean getAction() {
        return GXCommon.getBits(getConformanceBlock()[2], 0x1);
    }

    /**
     * @param value
     *            Can client call actions (methods).
     */
    public final void setAction(final boolean value) {
        getConformanceBlock()[2] =
                GXCommon.setBits(getConformanceBlock()[2], 0x1, value);
    }

    /**
     * @return Is server supporting event notifications.
     */
    public final boolean getEventNotification() {
        return GXCommon.getBits(getConformanceBlock()[2], 0x2);
    }

    /**
     * @param value
     *            Is server supporting event notifications.
     */
    public final void setEventNotification(final boolean value) {
        getConformanceBlock()[2] =
                GXCommon.setBits(getConformanceBlock()[2], 0x2, value);
    }

    /**
     * @return Is selective access supported.
     */
    public final boolean getSelectiveAccess() {
        return GXCommon.getBits(getConformanceBlock()[2], 0x4);
    }

    /**
     * @param value
     *            Is selective access supported.
     */
    public final void setSelectiveAccess(final boolean value) {
        getConformanceBlock()[2] =
                GXCommon.setBits(getConformanceBlock()[2], 0x4, value);
    }

    /**
     * @return Conformance block.
     */
    public final byte[] getConformanceBlock() {
        return conformanceBlock;
    }

    /**
     * @param value
     *            Conformance block.
     */
    public final void setConformanceBlock(final byte[] value) {
        if (value == null || value.length != 3) {
            throw new InvalidParameterException("Invalid conformance block.");
        }
        conformanceBlock = value;
    }
}