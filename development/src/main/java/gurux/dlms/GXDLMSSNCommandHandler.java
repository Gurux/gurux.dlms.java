package gurux.dlms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.GXDLMSObject;

final class GXDLMSSNCommandHandler {
    private static final Logger LOGGER =
            Logger.getLogger(GXDLMSServerBase.class.getName());

    /**
     * Constructor.
     */
    private GXDLMSSNCommandHandler() {

    }

    // CHECKSTYLE:OFF
    private static void handleRead(final GXDLMSSettings settings,
            final GXDLMSServerBase server, final byte type,
            final GXByteBuffer data, final List<ValueEventArgs> list,
            final List<ValueEventArgs> reads,
            final List<ValueEventArgs> actions, final GXByteBuffer replyData,
            final GXDLMSTranslatorStructure xml) throws Exception {
        // CHECKSTYLE:ON
        // GetRequest normal
        int sn = data.getInt16();
        if (xml != null) {
            if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                xml.appendStartTag(
                        TranslatorTags.VARIABLE_ACCESS_SPECIFICATION);
            } else {
                sn &= 0xFFFF;
            }
            if (type == VariableAccessSpecification.PARAMETERISED_ACCESS) {
                xml.appendStartTag(Command.READ_REQUEST,
                        VariableAccessSpecification.PARAMETERISED_ACCESS);
                xml.appendLine(
                        Command.READ_REQUEST << 8
                                | VariableAccessSpecification.VARIABLE_NAME,
                        "Value", xml.integerToHex(sn, 4));
                xml.appendLine(TranslatorTags.SELECTOR, "Value",
                        xml.integerToHex(data.getUInt8(), 2));
                GXDataInfo di = new GXDataInfo();
                di.setXml(xml);
                xml.appendStartTag(TranslatorTags.PARAMETER);
                GXCommon.getData(data, di);
                xml.appendEndTag(TranslatorTags.PARAMETER);
                xml.appendEndTag(Command.READ_REQUEST,
                        VariableAccessSpecification.PARAMETERISED_ACCESS);
            } else {
                xml.appendLine(
                        Command.READ_REQUEST << 8
                                | VariableAccessSpecification.VARIABLE_NAME,
                        "Value", xml.integerToHex(sn, 4));
            }
            if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                xml.appendEndTag(TranslatorTags.VARIABLE_ACCESS_SPECIFICATION);
            }
            return;
        }
        sn = sn & 0xFFFF;
        GXSNInfo i = findSNObject(server, server.getSettings(), sn);
        ValueEventArgs e =
                new ValueEventArgs(server, i.getItem(), i.getIndex(), 0, null);
        e.setAction(i.isAction());
        if (type == VariableAccessSpecification.PARAMETERISED_ACCESS) {
            e.setSelector(data.getUInt8());
            GXDataInfo di = new GXDataInfo();
            e.setParameters(GXCommon.getData(data, di));
        }
        // Return error if connection is not established.
        if (!settings.acceptConnection()
                && (!e.isAction() || e.getTarget().getShortName() != 0xFA00
                        || e.getIndex() != 8)) {
            replyData.set(GXDLMSServerBase.generateConfirmedServiceError(
                    ConfirmedServiceError.INITIATE_ERROR, ServiceError.SERVICE,
                    Service.UNSUPPORTED.getValue()));
            return;
        }

        list.add(e);
        if (!e.isAction()
                && server.notifyGetAttributeAccess(e) == AccessMode.NO_ACCESS) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        } else if (e.isAction() && server
                .notifyGetMethodAccess(e) == MethodAccessMode.NO_ACCESS) {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        } else {
            if (e.isAction()) {
                actions.add(e);
            } else {
                reads.add(e);
            }
        }
    }

    /**
     * Handle read Block in blocks.
     * 
     * @param data
     *            Received data.
     */
    private static void handleReadBlockNumberAccess(
            final GXDLMSSettings settings, final GXDLMSServerBase server,
            final GXByteBuffer data, final GXByteBuffer replyData,
            final GXDLMSTranslatorStructure xml) throws Exception {
        int blockNumber = data.getUInt16();
        if (xml != null) {
            xml.appendStartTag(Command.READ_REQUEST,
                    VariableAccessSpecification.BLOCK_NUMBER_ACCESS);
            xml.appendLine(TranslatorTags.BLOCK_NUMBER, "Value",
                    xml.integerToHex(blockNumber, 4));
            xml.appendEndTag(Command.READ_REQUEST,
                    VariableAccessSpecification.BLOCK_NUMBER_ACCESS);
            return;
        }

        GXByteBuffer bb = new GXByteBuffer();
        if (blockNumber != settings.getBlockIndex()) {
            LOGGER.log(Level.INFO,
                    "handleReadBlockNumberAccess failed. Invalid block number. "
                            + settings.getBlockIndex() + "/" + blockNumber);
            bb.setUInt8(ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue());
            GXDLMS.getSNPdu(
                    new GXDLMSSNParameters(settings, Command.READ_RESPONSE, 1,
                            SingleReadResponse.DATA_ACCESS_ERROR, bb, null),
                    replyData);
            settings.resetBlockIndex();
            return;
        }
        if (settings.getIndex() != settings.getCount() && server
                .getTransaction().getData().size() < settings.getMaxPduSize()) {
            List<ValueEventArgs> reads = new ArrayList<ValueEventArgs>();
            List<ValueEventArgs> actions = new ArrayList<ValueEventArgs>();
            for (ValueEventArgs it : server.getTransaction().getTargets()) {
                if (it.isAction()) {
                    actions.add(it);
                } else {
                    reads.add(it);
                }
            }
            if (reads.size() != 0) {
                server.notifyRead(
                        reads.toArray(new ValueEventArgs[reads.size()]));
            }

            if (actions.size() != 0) {
                server.notifyAction(
                        actions.toArray(new ValueEventArgs[actions.size()]));
            }
            getReadData(settings, server.getTransaction().getTargets(),
                    server.getTransaction().getData());
            if (reads.size() != 0) {
                server.notifyPostRead(
                        reads.toArray(new ValueEventArgs[reads.size()]));
            }
            if (actions.size() != 0) {
                server.notifyPostAction(
                        actions.toArray(new ValueEventArgs[actions.size()]));
            }
        }
        settings.increaseBlockIndex();
        GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                Command.READ_RESPONSE, 1, SingleReadResponse.DATA_BLOCK_RESULT,
                bb, server.getTransaction().getData());
        p.setMultipleBlocks(true);
        GXDLMS.getSNPdu(p, replyData);
        // If all data is sent.
        if (server.getTransaction().getData().size() == server.getTransaction()
                .getData().position()) {
            server.setTransaction(null);
            settings.resetBlockIndex();
        } else {
            server.getTransaction().getData().trim();
        }
    }

    /**
     * Get data for Read command.
     * 
     * @param list
     *            received objects.
     * @param data
     *            Data as byte array.
     * @return Response type.
     */
    private static byte getReadData(final GXDLMSSettings settings,
            final ValueEventArgs[] list, final GXByteBuffer data) {
        Object value;
        boolean first = true;
        byte type = SingleReadResponse.DATA;
        for (ValueEventArgs e : list) {
            if (e.getHandled()) {
                value = e.getValue();
            } else {
                // If action.
                if (e.isAction()) {
                    value = e.getTarget().invoke(settings, e);
                } else {
                    value = e.getTarget().getValue(settings, e);
                }
            }
            if (e.getError() == ErrorCode.OK) {
                if (!first && list.length != 1) {
                    data.setUInt8(SingleReadResponse.DATA);
                }
                // If action.
                if (e.isAction()) {
                    GXCommon.setData(data,
                            GXDLMSConverter.getDLMSDataType(value), value);
                } else {
                    GXDLMS.appendData(e.getTarget(), e.getIndex(), data, value);
                }
            } else {
                if (!first && list.length != 1) {
                    data.setUInt8(SingleReadResponse.DATA_ACCESS_ERROR);
                }
                data.setUInt8(e.getError().getValue());
                type = SingleReadResponse.DATA_ACCESS_ERROR;
            }
            first = false;
        }
        return type;
    }

    private static void handleReadDataBlockAccess(final GXDLMSSettings settings,
            final GXDLMSServerBase server, final int command,
            final GXByteBuffer data, final int cnt,
            final GXByteBuffer replyData, final GXDLMSTranslatorStructure xml)
            throws Exception {
        GXByteBuffer bb = new GXByteBuffer();
        short lastBlock = data.getUInt8();
        int blockNumber = data.getUInt16();
        if (xml != null) {
            if (command == Command.WRITE_RESPONSE) {
                xml.appendStartTag(TranslatorTags.WRITE_DATA_BLOCK_ACCESS);
            } else {
                xml.appendStartTag(TranslatorTags.READ_DATA_BLOCK_ACCESS);
            }
            xml.appendLine("<LastBlock Value=\""
                    + xml.integerToHex(lastBlock, 2) + "\" />");
            xml.appendLine("<BlockNumber Value=\""
                    + xml.integerToHex(blockNumber, 4) + "\" />");
            if (command == Command.WRITE_RESPONSE) {
                xml.appendEndTag(TranslatorTags.WRITE_DATA_BLOCK_ACCESS);
            } else {
                xml.appendEndTag(TranslatorTags.READ_DATA_BLOCK_ACCESS);
            }
            return;
        }
        if (blockNumber != settings.getBlockIndex()) {
            LOGGER.log(Level.INFO,
                    "handleReadDataBlockAccess failed. Invalid block number. "
                            + settings.getBlockIndex() + "/" + blockNumber);
            bb.setUInt8(ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue());
            GXDLMS.getSNPdu(
                    new GXDLMSSNParameters(settings, command, 1,
                            SingleReadResponse.DATA_ACCESS_ERROR, bb, null),
                    replyData);
            settings.resetBlockIndex();
            return;
        }
        int count = 1, type = DataType.OCTET_STRING.getValue();
        if (command == Command.WRITE_RESPONSE) {
            count = data.getUInt8();
            type = data.getUInt8();
        }
        int size = GXCommon.getObjectCount(data);
        int realSize = data.size() - data.position();
        if (count != 1 || type != DataType.OCTET_STRING.getValue()
                || size != realSize) {
            LOGGER.log(Level.INFO,
                    "handleGetRequest failed. Invalid block size.");
            bb.setUInt8(ErrorCode.DATA_BLOCK_UNAVAILABLE.getValue());
            GXDLMS.getSNPdu(
                    new GXDLMSSNParameters(settings, command, cnt,
                            SingleReadResponse.DATA_ACCESS_ERROR, bb, null),
                    replyData);
            settings.resetBlockIndex();
            return;
        }
        if (server.getTransaction() == null) {
            server.setTransaction(
                    new GXDLMSLongTransaction(null, command, data));
        } else {
            server.getTransaction().getData().set(data);
        }
        if (lastBlock == 0) {
            bb.setUInt16(blockNumber);
            settings.increaseBlockIndex();
            if (command == Command.READ_RESPONSE) {
                type = SingleReadResponse.BLOCK_NUMBER;
            } else {
                type = SingleWriteResponse.BLOCK_NUMBER;
            }
            GXDLMS.getSNPdu(new GXDLMSSNParameters(settings, command, cnt, type,
                    null, bb), replyData);
            return;
        } else {
            if (server.getTransaction() != null) {
                data.size(0);
                data.set(server.getTransaction().getData());
                server.setTransaction(null);
            }
            if (command == Command.READ_RESPONSE) {
                handleReadRequest(settings, server, data, replyData, xml);
            } else {
                handleWriteRequest(settings, server, data, replyData, xml);
            }
            settings.resetBlockIndex();
        }
    }

    /**
     * Handle read request.
     * 
     * @param data
     *            Received data.
     */
    static void handleReadRequest(final GXDLMSSettings settings,
            final GXDLMSServerBase server, final GXByteBuffer data,
            final GXByteBuffer replyData, final GXDLMSTranslatorStructure xml)
            throws Exception {
        GXByteBuffer bb = new GXByteBuffer();
        int cnt = 0xFF;
        byte type;
        List<ValueEventArgs> list = new ArrayList<ValueEventArgs>();
        // If get next frame.
        if (xml == null && data.size() == 0) {
            if (server.getTransaction() != null) {
                return;
            }
            bb.set(replyData);
            replyData.clear();
            for (ValueEventArgs it : server.getTransaction().getTargets()) {
                list.add(it);
            }
        } else {
            cnt = GXCommon.getObjectCount(data);
            List<ValueEventArgs> reads = new ArrayList<ValueEventArgs>();
            List<ValueEventArgs> actions = new ArrayList<ValueEventArgs>();
            if (xml != null) {
                xml.appendStartTag(Command.READ_REQUEST, "Qty",
                        xml.integerToHex(cnt, 2));
            }
            for (int pos = 0; pos != cnt; ++pos) {
                type = (byte) data.getUInt8();
                switch (type) {
                case VariableAccessSpecification.VARIABLE_NAME:
                case VariableAccessSpecification.PARAMETERISED_ACCESS:
                    handleRead(settings, server, type, data, list, reads,
                            actions, replyData, xml);
                    break;
                case VariableAccessSpecification.BLOCK_NUMBER_ACCESS:
                    handleReadBlockNumberAccess(settings, server, data,
                            replyData, xml);
                    if (xml != null) {
                        xml.appendEndTag(Command.READ_REQUEST);
                    }
                    return;
                case VariableAccessSpecification.READ_DATA_BLOCK_ACCESS:
                    handleReadDataBlockAccess(settings, server,
                            Command.READ_RESPONSE, data, cnt, replyData, xml);
                    if (xml != null) {
                        xml.appendEndTag(Command.READ_REQUEST);
                    }
                    return;
                default:
                    returnSNError(settings, Command.READ_RESPONSE,
                            ErrorCode.READ_WRITE_DENIED, replyData);
                    return;
                }
            }
            if (reads.size() != 0) {
                server.notifyRead(
                        reads.toArray(new ValueEventArgs[reads.size()]));
            }
            if (actions.size() != 0) {
                server.notifyAction(
                        actions.toArray(new ValueEventArgs[actions.size()]));
            }
        }
        if (xml != null) {
            xml.appendEndTag(Command.READ_REQUEST);
            return;
        }
        int requestType = getReadData(settings,
                list.toArray(new ValueEventArgs[list.size()]), bb);
        GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                Command.READ_RESPONSE, cnt, requestType, null, bb);
        GXDLMS.getSNPdu(p, replyData);
        if (server.getTransaction() == null && (bb.size() != bb.position()
                || settings.getCount() != settings.getIndex())) {
            List<ValueEventArgs> reads = new ArrayList<ValueEventArgs>();
            for (ValueEventArgs it : list) {
                reads.add(it);
            }
            if (reads.size() != 0) {
                server.notifyPostRead(
                        reads.toArray(new ValueEventArgs[reads.size()]));
            }
            server.setTransaction(new GXDLMSLongTransaction(
                    reads.toArray(new ValueEventArgs[reads.size()]),
                    Command.READ_REQUEST, bb));
        } else if (server.getTransaction() != null) {
            replyData.set(bb);
            return;
        }
    }

    private static void returnSNError(final GXDLMSSettings settings,
            final int cmd, final ErrorCode error,
            final GXByteBuffer replyData) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(error.getValue());
        GXDLMS.getSNPdu(
                new GXDLMSSNParameters(settings, cmd, 1,
                        SingleReadResponse.DATA_ACCESS_ERROR, bb, null),
                replyData);
        settings.resetBlockIndex();
    }

    /**
     * Find Short Name object.
     * 
     * @param sn
     */
    private static GXSNInfo findSNObject(final GXDLMSServerBase server,
            final GXDLMSSettings settings, final int sn) throws Exception {
        GXSNInfo i = new GXSNInfo();
        int[] offset = new int[1], count = new int[1];
        for (GXDLMSObject it : settings.getObjects()) {
            if (sn >= it.getShortName()) {
                // If attribute is accessed.
                if (sn < it.getShortName() + it.getAttributeCount() * 8) {
                    i.setAction(false);
                    i.setItem(it);
                    i.setIndex(((sn - it.getShortName()) / 8) + 1);
                    break;
                } else {
                    // If method is accessed.
                    GXDLMS.getActionInfo(it.getObjectType(), offset, count);
                    if (sn < it.getShortName() + offset[0] + (8 * count[0])) {
                        i.setItem(it);
                        i.setAction(true);
                        i.setIndex(
                                (sn - it.getShortName() - offset[0]) / 8 + 1);
                        break;
                    }
                }
            }
        }
        if (i.getItem() == null && server != null) {
            i.setItem(server.notifyFindObject(ObjectType.NONE, sn, null));
        }
        return i;
    }

    /**
     * Handle write request.
     * 
     * @param Reply
     *            Received data from the client.
     * @return Reply.
     */
    static void handleWriteRequest(final GXDLMSSettings settings,
            final GXDLMSServerBase server, final GXByteBuffer data,
            final GXByteBuffer replyData, final GXDLMSTranslatorStructure xml)
            throws Exception {
        // Return error if connection is not established.
        if (xml == null && !settings.acceptConnection()) {
            replyData.set(GXDLMSServerBase.generateConfirmedServiceError(
                    ConfirmedServiceError.INITIATE_ERROR, ServiceError.SERVICE,
                    Service.UNSUPPORTED.getValue()));
            return;
        }
        short type;
        Object value;
        // Get object count.
        List<GXSNInfo> targets = new ArrayList<GXSNInfo>();
        int cnt = GXCommon.getObjectCount(data);
        if (xml != null) {
            xml.appendStartTag(Command.WRITE_REQUEST);
            xml.appendStartTag(
                    TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION, "Qty",
                    xml.integerToHex(cnt, 2));
            if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                xml.appendStartTag(
                        TranslatorTags.VARIABLE_ACCESS_SPECIFICATION);
            }
        }
        GXByteBuffer results = new GXByteBuffer(cnt);
        for (int pos = 0; pos != cnt; ++pos) {
            type = data.getUInt8();
            switch (type) {
            case VariableAccessSpecification.VARIABLE_NAME:
                int sn = data.getUInt16();
                if (xml != null) {
                    xml.appendLine(
                            Command.WRITE_REQUEST << 8
                                    | VariableAccessSpecification.VARIABLE_NAME,
                            "Value", xml.integerToHex(sn, 4));
                } else {
                    GXSNInfo i = findSNObject(server, server.getSettings(), sn);
                    targets.add(i);
                    // If target is unknown.
                    if (i == null) {
                        // Device reports a undefined object.
                        results.setUInt8(ErrorCode.UNDEFINED_OBJECT.getValue());
                    } else {
                        results.setUInt8(ErrorCode.OK.getValue());
                    }
                }
                break;
            case VariableAccessSpecification.WRITE_DATA_BLOCK_ACCESS:
                handleReadDataBlockAccess(settings, server,
                        Command.WRITE_RESPONSE, data, cnt, replyData, xml);
                if (xml == null) {
                    return;
                }
            default:
                // Device reports a HW error.
                results.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
            }
        }
        if (xml != null) {
            if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                xml.appendEndTag(TranslatorTags.VARIABLE_ACCESS_SPECIFICATION);
            }
            xml.appendEndTag(
                    TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION);
        }
        // Get data count.
        cnt = GXCommon.getObjectCount(data);
        GXDataInfo di = new GXDataInfo();
        if (xml != null) {
            di.setXml(xml);
            xml.appendStartTag(TranslatorTags.LIST_OF_DATA, "Qty",
                    xml.integerToHex(cnt, 2));
        }
        for (int pos = 0; pos != cnt; ++pos) {
            di.clear();
            if (xml != null) {
                if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    xml.appendStartTag(Command.WRITE_REQUEST << 8
                            | SingleReadResponse.DATA);
                }
                value = GXCommon.getData(data, di);
                if (!di.isComplete()) {
                    value = GXCommon.toHex(data.getData(), false,
                            data.position(), data.size() - data.position());
                    xml.appendLine(
                            GXDLMS.DATA_TYPE_OFFSET + di.getType().getValue(),
                            "Value", value.toString());
                }
                if (xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    xml.appendEndTag(Command.WRITE_REQUEST << 8
                            | SingleReadResponse.DATA);
                }
            } else if (results.getUInt8(pos) == 0) {
                // If object has found.
                GXSNInfo target = targets.get(pos);
                value = GXCommon.getData(data, di);
                if (value instanceof byte[]) {
                    DataType dt =
                            target.getItem().getDataType(target.getIndex());
                    if (dt != DataType.NONE && dt != DataType.OCTET_STRING) {
                        value = GXDLMSClient.changeType((byte[]) value, dt);
                    }
                }
                ValueEventArgs e = new ValueEventArgs(server, target.getItem(),
                        target.getIndex(), 0, null);
                AccessMode am = server.notifyGetAttributeAccess(e);
                // If write is denied.
                if (am != AccessMode.WRITE && am != AccessMode.READ_WRITE) {
                    results.setUInt8(pos,
                            ErrorCode.READ_WRITE_DENIED.getValue());
                } else {
                    e.setValue(value);
                    server.notifyWrite(new ValueEventArgs[] { e });
                    if (e.getError() != ErrorCode.OK) {
                        results.setUInt8(pos, e.getError().getValue());
                    } else if (!e.getHandled()) {
                        target.getItem().setValue(settings, e);
                    }
                    server.notifyPostWrite(new ValueEventArgs[] { e });
                }
            }
        }
        if (xml != null) {
            xml.appendEndTag(TranslatorTags.LIST_OF_DATA);
            xml.appendEndTag(Command.WRITE_REQUEST);
            return;
        }
        GXByteBuffer bb = new GXByteBuffer((2 * cnt));
        int ret;
        for (int pos = 0; pos != cnt; ++pos) {
            ret = results.getUInt8(pos);
            // If meter returns error.
            if (ret != 0) {
                bb.setUInt8(1);
            }
            bb.setUInt8(ret);
        }
        GXDLMSSNParameters p = new GXDLMSSNParameters(settings,
                Command.WRITE_RESPONSE, cnt, 0xFF, null, bb);
        GXDLMS.getSNPdu(p, replyData);
    }

    /**
     * Handle information report.
     * 
     * @param settings
     *            DLMS settings.
     * @param reply
     *            Received data.
     * @param list
     *            Received information report objects.
     */
    static void handleInformationReport(final GXDLMSSettings settings,
            final GXReplyData reply,
            final List<Entry<GXDLMSObject, Integer>> list) throws Exception {
        reply.setTime(null);
        int len = reply.getData().getUInt8();
        byte[] tmp = null;
        // If date time is given.
        if (len != 0) {
            tmp = new byte[len];
            reply.getData().get(tmp);
            reply.setTime((GXDateTime) GXDLMSClient.changeType(tmp,
                    DataType.DATETIME));
        }
        short type;
        TranslatorOutputType ot = TranslatorOutputType.SIMPLE_XML;
        if (reply.getXml() != null) {
            ot = reply.getXml().getOutputType();
        }
        int count = GXCommon.getObjectCount(reply.getData());
        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.INFORMATION_REPORT);
            if (reply.getTime() != null) {
                reply.getXml().appendComment(String.valueOf(reply.getTime()));
                if (ot == TranslatorOutputType.SIMPLE_XML) {
                    reply.getXml().appendLine(TranslatorTags.CURRENT_TIME, null,
                            GXCommon.toHex(tmp, false));
                } else {
                    reply.getXml().appendLine(TranslatorTags.CURRENT_TIME, null,
                            GXCommon.generalizedTime(reply.getTime()));
                }
            }
            reply.getXml().appendStartTag(
                    TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION, "Qty",
                    reply.getXml().integerToHex(count, 2));
        }
        for (int pos = 0; pos != count; ++pos) {
            type = reply.getData().getUInt8();
            if (type == VariableAccessSpecification.VARIABLE_NAME) {
                int sn = reply.getData().getUInt16();
                if (reply.getXml() != null) {
                    if (ot == TranslatorOutputType.STANDARD_XML) {
                        reply.getXml().appendStartTag(
                                TranslatorTags.VARIABLE_ACCESS_SPECIFICATION);
                    }
                    reply.getXml().appendLine(
                            Command.WRITE_REQUEST << 8
                                    | VariableAccessSpecification.VARIABLE_NAME,
                            "Value", reply.getXml().integerToHex(sn, 4));
                    if (ot == TranslatorOutputType.STANDARD_XML) {
                        reply.getXml().appendEndTag(
                                TranslatorTags.VARIABLE_ACCESS_SPECIFICATION);
                    }
                } else {
                    GXSNInfo info = findSNObject(null, settings, sn);
                    if (info.getItem() != null) {
                        list.add(new GXSimpleEntry<GXDLMSObject, Integer>(
                                info.getItem(), info.getIndex()));
                    } else {
                        System.out.println(
                                "InformationReport message. Unknown object : "
                                        + String.valueOf(sn));
                    }
                }
            }
        }
        if (reply.getXml() != null) {
            reply.getXml().appendEndTag(
                    TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION);
            reply.getXml().appendStartTag(TranslatorTags.LIST_OF_DATA, "Qty",
                    reply.getXml().integerToHex(count, 2));
        }
        // Get values.
        count = GXCommon.getObjectCount(reply.getData());
        GXDataInfo di = new GXDataInfo();
        di.setXml(reply.getXml());
        for (int pos = 0; pos != count; ++pos) {
            di.clear();
            if (reply.getXml() != null) {
                if (ot == TranslatorOutputType.STANDARD_XML) {
                    reply.getXml().appendStartTag(Command.WRITE_REQUEST << 8
                            | SingleReadResponse.DATA);
                }
                GXCommon.getData(reply.getData(), di);
                if (ot == TranslatorOutputType.STANDARD_XML) {
                    reply.getXml().appendEndTag(Command.WRITE_REQUEST << 8
                            | SingleReadResponse.DATA);
                }
            } else {
                ValueEventArgs v = new ValueEventArgs(list.get(pos).getKey(),
                        list.get(pos).getValue(), 0, null);
                v.setValue(GXCommon.getData(reply.getData(), di));
                list.get(pos).getKey().setValue(settings, v);
            }
        }
        if (reply.getXml() != null) {
            reply.getXml().appendEndTag(TranslatorTags.LIST_OF_DATA);
            reply.getXml().appendEndTag(Command.INFORMATION_REPORT);
        }
    }
}
