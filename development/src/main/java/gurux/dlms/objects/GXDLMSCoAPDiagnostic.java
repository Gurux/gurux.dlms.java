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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXStructure;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCoAPDiagnostic
 */
public class GXDLMSCoAPDiagnostic extends GXDLMSObject implements IGXDLMSBase {

    /**
     * CoAP messages counter.
     */
    private GXCoapMessagesCounter messagesCounter;
    /**
     * CoAP request response counter.
     */
    private GXCoapRequestResponseCounter requestResponseCounter;
    /**
     * Bt counter.
     */
    private GXCoapBtCounter btCounter;
    /**
     * CoAP Capture time.
     */
    private GXCoapCaptureTime captureTime;

    /**
     * Constructor.
     */
    public GXDLMSCoAPDiagnostic() {
        this("0.0.25.17.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSCoAPDiagnostic(String ln) {
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
    public GXDLMSCoAPDiagnostic(String ln, int sn) {
        super(ObjectType.COAP_DIAGNOSTIC, ln, sn);
        messagesCounter = new GXCoapMessagesCounter();
        requestResponseCounter = new GXCoapRequestResponseCounter();
        btCounter = new GXCoapBtCounter();
        captureTime = new GXCoapCaptureTime();
    }

    /**
     * @return CoAP messages counter.
     */
    public final GXCoapMessagesCounter getMessagesCounter() {
        return messagesCounter;
    }

    /**
     * @return CoAP request response counter.
     */
    public final GXCoapRequestResponseCounter getRequestResponseCounter() {
        return requestResponseCounter;
    }

    /**
     * @return Bt counter.
     */
    public final GXCoapBtCounter getBtCounter() {
        return btCounter;
    }

    /**
     * @return CoAP Capture time.
     */
    public final GXCoapCaptureTime getCaptureTime() {
        return captureTime;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getMessagesCounter(), getRequestResponseCounter(), getBtCounter(),
                getCaptureTime() };
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // MessagesCounter
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // RequestResponseCounter
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // BtCounter
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // CaptureTime
        if (all || canRead(5)) {
            attributes.add(5);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "Messages counter", "Request response counter", "BT counter",
                "Capture time" };
    }

    /**
     * Reset diagnostic values.
     * 
     * @param client
     *            DLMS client.
     * @return Action bytes.
     * @throws NoSuchPaddingException
     *             No such padding exception.
     * @throws NoSuchAlgorithmException
     *             No such algorithm exception.
     * @throws InvalidAlgorithmParameterException
     *             Invalid algorithm parameter exception.
     * @throws InvalidKeyException
     *             Invalid key exception.
     * @throws BadPaddingException
     *             Bad padding exception.
     * @throws IllegalBlockSizeException
     *             Illegal block size exception.
     * @throws SignatureException
     *             Signature exception.
     */
    public final byte[][] reset(GXDLMSClient client)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        return client.method(this, 1, 0, DataType.INT8);
    }

    @Override
    public final String[] getMethodNames() {
        return new String[] { "Reset" };
    }

    @Override
    public final int getAttributeCount() {
        return 5;
    }

    @Override
    public final int getMethodCount() {
        return 1;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
        case 3:
        case 4:
        case 5:
            return DataType.STRUCTURE;
        default:
            throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
        }
    }

    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        GXByteBuffer buff = new GXByteBuffer();
        Object ret = null;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            buff.setUInt8(DataType.STRUCTURE);
            GXCommon.setObjectCount(10, buff);
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getTx());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getRx());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getTxResend());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getTxReset());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getRxReset());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getTxAck());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getRxAck());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getRxDrop());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getTxNonPiggybacked());
            GXCommon.setData(settings, buff, DataType.UINT32, messagesCounter.getMaxRtxExceeded());
            ret = buff.array();
            break;
        case 3:
            buff.setUInt8(DataType.STRUCTURE);
            GXCommon.setObjectCount(8, buff);
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getRxRequests());
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getTxRequests());
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getRxResponse());
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getTxResponse());
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getTxClientError());
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getRxClientError());
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getTxServerError());
            GXCommon.setData(settings, buff, DataType.UINT32, requestResponseCounter.getRxServerError());
            ret = buff.array();
            break;
        case 4:
            buff.setUInt8(DataType.STRUCTURE);
            GXCommon.setObjectCount(3, buff);
            GXCommon.setData(settings, buff, DataType.UINT32, btCounter.getBlockWiseTransferStarted());
            GXCommon.setData(settings, buff, DataType.UINT32, btCounter.getBlockWiseTransferCompleted());
            GXCommon.setData(settings, buff, DataType.UINT32, btCounter.getBlockWiseTransferTimeout());
            ret = buff.array();
            break;
        case 5:
            buff.setUInt8(DataType.STRUCTURE);
            GXCommon.setObjectCount(2, buff);
            GXCommon.setData(settings, buff, DataType.UINT8, captureTime.getAttributeId());
            GXCommon.setData(settings, buff, DataType.DATETIME, captureTime.getTimeStamp());
            ret = buff.array();
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return ret;
    }

    @Override
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2: {
            GXStructure s = (GXStructure) ((e.getValue() instanceof GXStructure) ? e.getValue() : null);
            messagesCounter.setTx(((Number) s.get(0)).longValue());
            messagesCounter.setRx(((Number) s.get(1)).longValue());
            messagesCounter.setTxResend(((Number) s.get(2)).longValue());
            messagesCounter.setTxReset(((Number) s.get(3)).longValue());
            messagesCounter.setRxReset(((Number) s.get(4)).longValue());
            messagesCounter.setTxAck(((Number) s.get(5)).longValue());
            messagesCounter.setRxAck(((Number) s.get(6)).longValue());
            messagesCounter.setRxDrop(((Number) s.get(7)).longValue());
            messagesCounter.setTxNonPiggybacked(((Number) s.get(8)).longValue());
            messagesCounter.setMaxRtxExceeded(((Number) s.get(9)).longValue());
            break;
        }
        case 3: {
            GXStructure s = (GXStructure) ((e.getValue() instanceof GXStructure) ? e.getValue() : null);
            requestResponseCounter.setRxRequests(((Number) s.get(0)).longValue());
            requestResponseCounter.setTxRequests(((Number) s.get(1)).longValue());
            requestResponseCounter.setRxResponse(((Number) s.get(2)).longValue());
            requestResponseCounter.setTxResponse(((Number) s.get(3)).longValue());
            requestResponseCounter.setTxClientError(((Number) s.get(4)).longValue());
            requestResponseCounter.setRxClientError(((Number) s.get(5)).longValue());
            requestResponseCounter.setTxServerError(((Number) s.get(6)).longValue());
            requestResponseCounter.setRxServerError(((Number) s.get(7)).longValue());
            break;
        }
        case 4: {
            GXStructure s = (GXStructure) ((e.getValue() instanceof GXStructure) ? e.getValue() : null);
            btCounter.setBlockWiseTransferStarted(((Number) s.get(0)).longValue());
            btCounter.setBlockWiseTransferCompleted(((Number) s.get(1)).longValue());
            btCounter.setBlockWiseTransferTimeout(((Number) s.get(2)).longValue());
            break;
        }
        case 5: {
            GXStructure s = (GXStructure) ((e.getValue() instanceof GXStructure) ? e.getValue() : null);
            captureTime.setAttributeId(((Number) s.get(0)).byteValue());
            captureTime.setTimeStamp((GXDateTime) s.get(1));
            break;
        }
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(GXXmlReader reader) throws XMLStreamException {
        messagesCounter.setTx(reader.readElementContentAsLong("Tx"));
        messagesCounter.setRx(reader.readElementContentAsLong("Rx"));
        messagesCounter.setTxResend(reader.readElementContentAsLong("TxResend"));
        messagesCounter.setTxReset(reader.readElementContentAsLong("TxReset"));
        messagesCounter.setRxReset(reader.readElementContentAsLong("RxReset"));
        messagesCounter.setTxAck(reader.readElementContentAsLong("TxAck"));
        messagesCounter.setRxAck(reader.readElementContentAsLong("RxAck"));
        messagesCounter.setRxDrop(reader.readElementContentAsLong("RxDrop"));
        messagesCounter.setTxNonPiggybacked(reader.readElementContentAsLong("TxNonPiggybacked"));
        messagesCounter.setMaxRtxExceeded(reader.readElementContentAsLong("MaxRtxExceeded"));
        requestResponseCounter.setRxRequests(reader.readElementContentAsLong("RxRequests"));
        requestResponseCounter.setTxRequests(reader.readElementContentAsLong("TxRequests"));
        requestResponseCounter.setRxResponse(reader.readElementContentAsLong("RxResponse"));
        requestResponseCounter.setTxResponse(reader.readElementContentAsLong("TxResponse"));
        requestResponseCounter.setTxClientError(reader.readElementContentAsLong("TxClientError"));
        requestResponseCounter.setRxClientError(reader.readElementContentAsLong("RxClientError"));
        requestResponseCounter.setTxServerError(reader.readElementContentAsLong("TxServerError"));
        requestResponseCounter.setRxServerError(reader.readElementContentAsLong("RxServerError"));
        btCounter.setBlockWiseTransferStarted(reader.readElementContentAsLong("TransferStarted"));
        btCounter.setBlockWiseTransferCompleted(reader.readElementContentAsLong("TransferCompleted"));
        btCounter.setBlockWiseTransferTimeout(reader.readElementContentAsLong("TransferTimeout"));
        captureTime.setAttributeId((byte) reader.readElementContentAsInt("AttributeId"));
        captureTime.setTimeStamp(reader.readElementContentAsDateTime("TimeStamp"));

    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("Tx", messagesCounter.getTx());
        writer.writeElementString("Rx", messagesCounter.getRx());
        writer.writeElementString("TxResend", messagesCounter.getTxResend());
        writer.writeElementString("TxReset", messagesCounter.getTxReset());
        writer.writeElementString("RxReset", messagesCounter.getRxReset());
        writer.writeElementString("TxAck", messagesCounter.getTxAck());
        writer.writeElementString("RxAck", messagesCounter.getRxAck());
        writer.writeElementString("RxDrop", messagesCounter.getRxDrop());
        writer.writeElementString("TxNonPiggybacked", messagesCounter.getTxNonPiggybacked());
        writer.writeElementString("MaxRtxExceeded", messagesCounter.getMaxRtxExceeded());
        writer.writeElementString("RxRequests", requestResponseCounter.getRxRequests());
        writer.writeElementString("TxRequests", requestResponseCounter.getTxRequests());
        writer.writeElementString("RxResponse", requestResponseCounter.getRxResponse());
        writer.writeElementString("TxResponse", requestResponseCounter.getTxResponse());
        writer.writeElementString("TxClientError", requestResponseCounter.getTxClientError());
        writer.writeElementString("RxClientError", requestResponseCounter.getRxClientError());
        writer.writeElementString("TxServerError", requestResponseCounter.getTxServerError());
        writer.writeElementString("RxServerError", requestResponseCounter.getRxServerError());
        writer.writeElementString("TransferStarted", btCounter.getBlockWiseTransferStarted());
        writer.writeElementString("TransferCompleted", btCounter.getBlockWiseTransferCompleted());
        writer.writeElementString("TransferTimeout", btCounter.getBlockWiseTransferTimeout());
        writer.writeElementString("AttributeId", captureTime.getAttributeId());
        writer.writeElementString("TimeStamp", captureTime.getTimeStamp());
    }

    @Override
    public final void postLoad(GXXmlReader reader) {
    }
}
