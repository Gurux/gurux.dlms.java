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

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.ControlMode;
import gurux.dlms.objects.enums.ControlState;

public class GXDLMSDisconnectControl extends GXDLMSObject
        implements IGXDLMSBase {
    private boolean outputState;
    private ControlState controlState;
    private ControlMode controlMode;

    /**
     * Constructor.
     */
    public GXDLMSDisconnectControl() {
        super(ObjectType.DISCONNECT_CONTROL);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSDisconnectControl(final String ln) {
        super(ObjectType.DISCONNECT_CONTROL, ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSDisconnectControl(final String ln, final int sn) {
        super(ObjectType.DISCONNECT_CONTROL, ln, sn);
    }

    /**
     * @return Output state of COSEM Disconnect Control object.
     */
    public final boolean getOutputState() {
        return outputState;
    }

    /**
     * @param value
     *            Output state of COSEM Disconnect Control object.
     */
    public final void setOutputState(final boolean value) {
        outputState = value;
    }

    /**
     * @return Control state of COSEM Disconnect Control object.
     */
    public final ControlState getControlState() {
        return controlState;
    }

    /**
     * @param value
     *            Control state of COSEM Disconnect Control object.
     */
    public final void setControlState(final ControlState value) {
        controlState = value;
    }

    /**
     * @return Control mode of COSEM Disconnect Control object.
     */
    public final ControlMode getControlMode() {
        return controlMode;
    }

    /**
     * @param value
     *            Control mode of COSEM Disconnect Control object.
     */
    public final void setControlMode(final ControlMode value) {
        controlMode = value;
    }

    /*
     * Forces the disconnect control object into 'disconnected' state if remote
     * disconnection is enabled.
     */
    public final byte[][] remoteDisconnect(final GXDLMSClient client) {
        return client.method(this, 1, new Integer(0), DataType.INT8);
    }

    /*
     * Forces the disconnect control object into the 'ready_for_reconnection'
     * state if a direct remote reconnection is disabled.
     */
    public final byte[][] remoteReconnect(final GXDLMSClient client) {
        return client.method(this, 2, new Integer(0), DataType.INT8);
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), new Boolean(getOutputState()),
                getControlState(), getControlMode() };
    }

    @Override
    public final int[] getAttributeIndexToRead() {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(new Integer(1));
        }
        // OutputState
        if (canRead(2)) {
            attributes.add(new Integer(2));
        }
        // ControlState
        if (canRead(3)) {
            attributes.add(new Integer(3));
        }
        // ControlMode
        if (canRead(4)) {
            attributes.add(new Integer(4));
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final int getAttributeCount() {
        return 4;
    }

    @Override
    public final int getMethodCount() {
        return 2;
    }

    @Override
    public final DataType getDataType(final int index) {
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        if (index == 2) {
            return DataType.BOOLEAN;
        }
        if (index == 3) {
            return DataType.ENUM;
        }
        if (index == 4) {
            return DataType.ENUM;
        }
        throw new IllegalArgumentException(
                "getDataType failed. Invalid attribute index.");
    }

    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            return new Boolean(getOutputState());
        }
        if (e.getIndex() == 3) {
            return new Integer(getControlState().ordinal());
        }
        if (e.getIndex() == 4) {
            return new Integer(getControlMode().ordinal());
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            if (e.getValue() == null) {
                setOutputState(false);
            } else {
                setOutputState(((Boolean) e.getValue()).booleanValue());
            }
        } else if (e.getIndex() == 3) {
            if (e.getValue() == null) {
                setControlState(ControlState.DISCONNECTED);
            } else {
                setControlState(ControlState.values()[((Number) e.getValue())
                        .intValue()]);
            }
        } else if (e.getIndex() == 4) {
            if (e.getValue() == null) {
                setControlMode(ControlMode.NONE);
            } else {
                setControlMode(ControlMode.values()[((Number) e.getValue())
                        .intValue()]);
            }

        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        outputState = reader.readElementContentAsInt("OutputState") != 0;
        controlState = ControlState.values()[reader
                .readElementContentAsInt("ControlState")];
        controlMode = ControlMode.values()[reader
                .readElementContentAsInt("ControlMode")];
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("OutputState", outputState);
        writer.writeElementString("ControlState", controlState.ordinal());
        writer.writeElementString("ControlMode", controlMode.ordinal());
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}
