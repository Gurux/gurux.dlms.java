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

import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.objects.GXDLMSObject;

public class ValueEventArgs {
    /**
     * Object value.
     */
    private Object eventValue;
    /**
     * Is request handled.
     */
    private boolean handled;
    /**
     * Target DLMS object
     */
    private GXDLMSObject target;
    /**
     * Attribute index.
     */
    private int index;
    /**
     * Data type of the value.
     */
    private DataType dataType;
    /**
     * Optional selector.
     */
    private int selector;
    /**
     * Optional parameters.
     */
    private Object parameters;

    /**
     * Occurred error.
     */
    private ErrorCode error = ErrorCode.OK;

    /**
     * Is action. This is reserved for internal use.
     */
    private boolean action;

    /**
     * Is action. This is reserved for internal use.
     */
    private GXDLMSSettings settings;

    /**
     * Is value max PDU size skipped when converting data to bytes.
     */
    private boolean skipMaxPduSize;

    /**
     * @return Target DLMS object.
     */
    public final GXDLMSObject getTarget() {
        return target;
    }

    private void setTarget(final GXDLMSObject value) {
        target = value;
    }

    /**
     * @return Attribute index of queried object.
     */
    public final int getIndex() {
        return index;
    }

    private void setIndex(final int value) {
        index = value;
    }

    /**
     * @return Object value.
     */
    public final Object getValue() {
        return eventValue;
    }

    /**
     * @param value
     *            Object value.
     */
    public final void setValue(final Object value) {
        eventValue = value;
    }

    /**
     * @return Data type of the value.
     */
    public final DataType getDataType() {
        return dataType;
    }

    /**
     * @param value
     *            Data type of the value.
     */
    public final void setDataType(final DataType value) {
        dataType = value;
    }

    /**
     * @return Is request handled.
     */
    public final boolean getHandled() {
        return handled;
    }

    /**
     * @param value
     *            Is request handled.
     */
    public final void setHandled(final boolean value) {
        handled = value;
    }

    /**
     * @return Optional selector.
     */
    public final int getSelector() {
        return selector;
    }

    /**
     * @param value
     *            Selector.
     */
    public final void setSelector(final int value) {
        selector = value;
    }

    /**
     * @return Optional parameters.
     */
    public final Object getParameters() {
        return parameters;
    }

    /**
     * @param value
     *            Optional parameters.
     */
    public final void setParameters(final Object value) {
        parameters = value;
    }

    /**
     * Constructor.
     * 
     * @param s
     *            DLMS settings.
     * @param eventTarget
     *            Event target.
     * @param eventIndex
     *            Event index.
     * @param readSelector
     *            Optional read event selector.
     * @param forParameters
     *            Optional parameters.
     */
    public ValueEventArgs(final GXDLMSSettings s,
            final GXDLMSObject eventTarget, final int eventIndex,
            final int readSelector, final Object forParameters) {
        settings = s;
        dataType = DataType.NONE;
        setTarget(eventTarget);
        setIndex(eventIndex);
        selector = readSelector;
        parameters = forParameters;
    }

    /**
     * Constructor.
     * 
     * @param eventTarget
     *            Event target.
     * @param eventIndex
     *            Event index.
     * @param readSelector
     *            Optional read event selector.
     * @param forParameters
     *            Optional parameters.
     */
    public ValueEventArgs(final GXDLMSObject eventTarget, final int eventIndex,
            final int readSelector, final Object forParameters) {
        dataType = DataType.NONE;
        setTarget(eventTarget);
        setIndex(eventIndex);
        selector = readSelector;
        parameters = forParameters;
    }

    /**
     * @return Occurred error.
     */
    public final ErrorCode getError() {
        return error;
    }

    /**
     * @param value
     *            Occurred error.
     */
    public final void setError(final ErrorCode value) {
        error = value;
    }

    /**
     * @return Is action.
     */
    public final boolean isAction() {
        return action;
    }

    /**
     * @param value
     *            Is action.
     */
    public final void setAction(final boolean value) {
        action = value;
    }

    /**
     * @return DLMS settings.
     */
    public final GXDLMSSettings getSettings() {
        return settings;
    }

    /**
     * @return Is value max PDU size skipped when converting data to bytes.
     */
    public final boolean isSkipMaxPduSize() {
        return skipMaxPduSize;
    }

    /**
     * @param value
     *            Is value max PDU size skipped when converting data to bytes.
     */
    public final void setSkipMaxPduSize(final boolean value) {
        skipMaxPduSize = value;
    }
}