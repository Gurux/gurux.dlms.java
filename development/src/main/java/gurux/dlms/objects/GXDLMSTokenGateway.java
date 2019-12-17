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

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.TokenDelivery;
import gurux.dlms.objects.enums.TokenStatusCode;

/**
 * Online help:<br>
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
 */
public class GXDLMSTokenGateway extends GXDLMSObject implements IGXDLMSBase {

    /**
     * Token.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     */
    private byte[] token;

    /**
     * Time.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     */
    private GXDateTime time;

    /**
     * Descriptions.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     */
    private List<String> descriptions;

    /**
     * Token Delivery method.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     */
    private TokenDelivery deliveryMethod;

    /**
     * Token status code.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     */
    private TokenStatusCode statusCode;

    /**
     * Token data value.<br>
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     */
    private String dataValue;

    /**
     * Constructor.
     */
    public GXDLMSTokenGateway() {
        this("0.0.19.40.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSTokenGateway(final String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSTokenGateway(final String ln, final int sn) {
        super(ObjectType.TOKEN_GATEWAY, ln, sn);
        descriptions = new ArrayList<String>();
        deliveryMethod = TokenDelivery.LOCAL;
        statusCode = TokenStatusCode.FORMAT_OK;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @return Token.
     */
    public final byte[] getToken() {
        return token;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @param value
     *            Token.
     */
    public final void setToken(final byte[] value) {
        token = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @return Time.
     */
    public final GXDateTime getTime() {
        return time;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @param value
     *            Time.
     */
    public final void setTime(final GXDateTime value) {
        time = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @return Descriptions.
     */
    public final List<String> getDescriptions() {
        return descriptions;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @param value
     *            Descriptions.
     */
    public final void setDescriptions(final List<String> value) {
        descriptions = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @return Token Delivery method.
     */
    public final TokenDelivery getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @param value
     *            Token Delivery method.
     */
    public final void setDeliveryMethod(final TokenDelivery value) {
        deliveryMethod = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @return Token status code.
     */
    public final TokenStatusCode getStatusCode() {
        return statusCode;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @param value
     *            Token status code.
     */
    public final void setStatusCode(final TokenStatusCode value) {
        statusCode = value;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @return Token data value.
     */
    public final String getDataValue() {
        return dataValue;
    }

    /**
     * Online help:<br>
     * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSTokenGateway
     * 
     * @param value
     *            Token data value.
     */
    public final void setDataValue(final String value) {
        dataValue = value;
    }

    @Override
    public final Object[] getValues() {
        return new Object[] { getLogicalName(), token, time, descriptions,
                deliveryMethod, new Object[] { statusCode, dataValue } };
    }

    /*
     * Returns collection of attributes to read. If attribute is static and
     * already read or device is returned HW error it is not returned.
     */
    @Override
    public final int[] getAttributeIndexToRead(final boolean all) {
        java.util.ArrayList<Integer> attributes =
                new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null
                || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // Token
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // Time
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // Description
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // DeliveryMethod
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // Status
        if (all || canRead(6)) {
            attributes.add(6);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    /*
     * Returns amount of attributes.
     */
    @Override
    public final int getAttributeCount() {
        return 6;
    }

    /*
     * Returns amount of methods.
     */
    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public final DataType getDataType(final int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
            return DataType.OCTET_STRING;
        case 3:
            return DataType.OCTET_STRING;
        case 4:
            return DataType.ARRAY;
        case 5:
            return DataType.ENUM;
        case 6:
            return DataType.STRUCTURE;
        default:
            throw new IllegalArgumentException(
                    "getDataType failed. Invalid attribute index.");
        }
    }

    /*
     * Returns value of given attribute.
     */
    @Override
    public final Object getValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        GXByteBuffer bb;
        switch (e.getIndex()) {
        case 1:
            return GXCommon.logicalNameToBytes(getLogicalName());
        case 2:
            return token;
        case 3:
            return time;
        case 4:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY.getValue());
            if (descriptions == null) {
                bb.setUInt8(0);
            } else {
                bb.setUInt8((byte) descriptions.size());
                for (String it : descriptions) {
                    bb.setUInt8(DataType.OCTET_STRING.getValue());
                    bb.setUInt8((byte) it.length());
                    bb.set(it.getBytes());
                }
            }
            return bb.array();
        case 5:
            return deliveryMethod.getValue();
        case 6:
            bb = new GXByteBuffer();
            bb.setUInt8(DataType.STRUCTURE.getValue());
            bb.setUInt8(2);
            GXCommon.setData(null, bb, DataType.ENUM, statusCode.getValue());
            GXCommon.setData(null, bb, DataType.BITSTRING, dataValue);
            return bb.array();
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return null;
    }

    /*
     * Set value of given attribute.
     */
    @Override
    public final void setValue(final GXDLMSSettings settings,
            final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2:
            token = (byte[]) e.getValue();
            break;
        case 3:
            time = (GXDateTime) GXDLMSClient.changeType((byte[]) e.getValue(),
                    DataType.DATETIME);
            break;
        case 4:
            descriptions.clear();
            if (e.getValue() != null) {
                for (Object it : (List<?>) e.getValue()) {
                    descriptions.add(new String((byte[]) it));
                }
            }
            break;
        case 5:
            deliveryMethod =
                    TokenDelivery.forValue(((Number) e.getValue()).intValue());
            break;
        case 6:
            statusCode = TokenStatusCode.forValue(
                    ((Number) ((List<?>) e.getValue()).get(0)).intValue());
            dataValue = String.valueOf(((List<?>) e.getValue()).get(1));
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        token = GXCommon.hexToBytes(reader.readElementContentAsString("Token"));
        time = reader.readElementContentAsDateTime("Time");
        descriptions.clear();
        if (reader.isStartElement("Descriptions", true)) {
            while (reader.isStartElement("Item", true)) {
                descriptions.add(reader.readElementContentAsString("Name"));
            }
            reader.readEndElement("Descriptions");
        }
        deliveryMethod = TokenDelivery
                .forValue(reader.readElementContentAsInt("DeliveryMethod"));
        statusCode = TokenStatusCode
                .forValue(reader.readElementContentAsInt("Status"));
        dataValue = reader.readElementContentAsString("Data");
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Token", GXCommon.toHex(token, false));
        writer.writeElementString("Time", time);

        if (descriptions != null) {
            writer.writeStartElement("Descriptions");
            for (String it : descriptions) {
                writer.writeStartElement("Item");
                writer.writeElementString("Name", it);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeElementString("DeliveryMethod", deliveryMethod.getValue());
        writer.writeElementString("Status", statusCode.getValue());
        writer.writeElementString("Data", dataValue);
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
    }
}