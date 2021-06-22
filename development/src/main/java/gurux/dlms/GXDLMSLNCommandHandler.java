package gurux.dlms;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AccessServiceCommandType;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXDLMSSecuritySetup;
import gurux.dlms.objects.enums.AssociationStatus;

final class GXDLMSLNCommandHandler {
	private static final Logger LOGGER = Logger.getLogger(GXDLMSServerBase.class.getName());

	/**
	 * Constructor.
	 */
	private GXDLMSLNCommandHandler() {

	}

	static void handleGetRequest(final GXDLMSSettings settings, final GXDLMSServerBase server, final GXByteBuffer data,
			final GXByteBuffer replyData, final GXDLMSTranslatorStructure xml, final int cipheredCommand)
			throws Exception {
		// Return error if connection is not established.
		if (xml == null && (settings.getConnected() & ConnectionState.DLMS) == 0 && cipheredCommand == Command.NONE) {
			replyData.set(GXDLMSServerBase.generateConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR,
					ServiceError.SERVICE, Service.UNSUPPORTED.getValue()));
			return;
		}
		// Get type.
		byte type = 1;
		short invokeID = 0;
		try {
			// If GBT is used data is empty.
			if (data.size() != 0) {
				// Get type.
				type = (byte) data.getUInt8();
				// Get invoke ID and priority.
				invokeID = data.getUInt8();
				settings.updateInvokeId(invokeID);
				GXDLMS.addInvokeId(xml, Command.GET_REQUEST, type, invokeID);
			}
			// GetRequest normal
			if (type == GetCommandType.NORMAL) {
				getRequestNormal(settings, invokeID, server, data, replyData, xml, cipheredCommand);
			} else if (type == GetCommandType.NEXT_DATA_BLOCK) {
				// Get request for next data block
				getRequestNextDataBlock(settings, invokeID, server, data, replyData, xml, false, cipheredCommand);
			} else if (type == GetCommandType.WITH_LIST) {
				// Get request with a list.
				getRequestWithList(settings, invokeID, server, data, replyData, xml, cipheredCommand);
			} else {
				LOGGER.log(Level.INFO, "HandleGetRequest failed. Invalid command type.");
				GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invokeID, Command.GET_RESPONSE, 1, null, null,
						ErrorCode.READ_WRITE_DENIED.getValue(), cipheredCommand), replyData);
				settings.resetBlockIndex();
			}
			if (xml != null) {
				xml.appendEndTag(Command.GET_REQUEST, type);
				xml.appendEndTag(Command.GET_REQUEST);
			}
		} catch (Exception ex) {
			replyData.clear();
			LOGGER.log(Level.SEVERE, ex.getMessage());
			GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invokeID, Command.GET_RESPONSE, type, null, null,
					ErrorCode.READ_WRITE_DENIED.getValue(), cipheredCommand), replyData);
			settings.resetBlockIndex();
		}
	}

	/*
	 * Handle set request.
	 */
	public static void handleSetRequest(final GXDLMSSettings settings, final GXDLMSServerBase server,
			final GXByteBuffer data, final GXByteBuffer replyData, final GXDLMSTranslatorStructure xml,
			final int cipheredCommand) throws Exception {
		// Return error if connection is not established.
		if (xml == null && (settings.getConnected() & ConnectionState.DLMS) == 0 && cipheredCommand == Command.NONE) {
			replyData.set(GXDLMSServerBase.generateConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR,
					ServiceError.SERVICE, Service.UNSUPPORTED.getValue()));
			return;
		}
		// Get type.
		byte type = (byte) data.getUInt8();
		// Get invoke ID and priority.
		short invoke = data.getUInt8();
		settings.updateInvokeId(invoke);
		// SetRequest normal or Set Request With First Data Block
		GXDLMSLNParameters p = new GXDLMSLNParameters(settings, invoke, Command.SET_RESPONSE, type, null, null, 0,
				cipheredCommand);
		GXDLMS.addInvokeId(xml, Command.SET_REQUEST, type, invoke);
		try {
			switch (type) {
			case SetRequestType.NORMAL:
			case SetRequestType.FIRST_DATA_BLOCK:
				handleSetRequestNormal(settings, server, data, type, p, replyData, xml);
				break;
			case SetRequestType.WITH_DATA_BLOCK:
				hanleSetRequestWithDataBlock(settings, server, data, p, replyData, xml);
				break;
			case SetRequestType.WITH_LIST:
				hanleSetRequestWithList(settings, invoke, server, data, p, replyData, xml);
				break;
			default:
				LOGGER.log(Level.INFO, "HandleSetRequest failed. Unknown command.");
				settings.resetBlockIndex();
				GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invoke, Command.SET_RESPONSE, 1, null, null,
						ErrorCode.READ_WRITE_DENIED.getValue(), cipheredCommand), replyData);
				settings.resetBlockIndex();
				return;
			}
			if (xml != null) {
				xml.appendEndTag(Command.SET_REQUEST, type);
				xml.appendEndTag(Command.SET_REQUEST);
				return;
			}
			GXDLMS.getLNPdu(p, replyData);
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invoke, Command.SET_RESPONSE, type, null, null,
					ErrorCode.READ_WRITE_DENIED.getValue(), cipheredCommand), replyData);
			settings.resetBlockIndex();
		}
	}

	private static void appendAttributeDescriptor(final GXDLMSTranslatorStructure xml, final int ci, final byte[] ln,
			final short attributeIndex) {
		xml.appendStartTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR);
		if (xml.isComments()) {
			ObjectType ot = ObjectType.forValue(ci);
			if (ot != null) {
				xml.appendComment(ot.toString());
			}
		}
		xml.appendLine(TranslatorTags.CLASS_ID, "Value", xml.integerToHex(ci, 4));
		xml.appendComment(GXCommon.toLogicalName(ln));
		xml.appendLine(TranslatorTags.INSTANCE_ID, "Value", GXCommon.toHex(ln, false));
		xml.appendLine(TranslatorTags.ATTRIBUTE_ID, "Value", xml.integerToHex(attributeIndex, 2));
		xml.appendEndTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR);
	}

	private static void appendMethodDescriptor(final GXDLMSTranslatorStructure xml, final int ci, final byte[] ln,
			final short attributeIndex) {
		xml.appendStartTag(TranslatorTags.METHOD_DESCRIPTOR);
		if (xml.isComments()) {
			ObjectType ot = ObjectType.forValue(ci);
			if (ot != null) {
				xml.appendComment(ot.toString());
			}
		}
		xml.appendLine(TranslatorTags.CLASS_ID, "Value", xml.integerToHex(ci, 4));
		xml.appendComment(GXCommon.toLogicalName(ln));
		xml.appendLine(TranslatorTags.INSTANCE_ID, "Value", GXCommon.toHex(ln, false));
		xml.appendLine(TranslatorTags.METHOD_ID, "Value", xml.integerToHex(attributeIndex, 2));
		xml.appendEndTag(TranslatorTags.METHOD_DESCRIPTOR);
	}

	/**
	 * Handle get request normal command.
	 *
	 * @param data Received data.
	 */
	private static void getRequestNormal(final GXDLMSSettings settings, final short invokeID,
			final GXDLMSServerBase server, final GXByteBuffer data, final GXByteBuffer replyData,
			final GXDLMSTranslatorStructure xml, final int cipheredCommand) throws Exception {
		GXByteBuffer bb = new GXByteBuffer();
		ValueEventArgs e = null;
		ErrorCode status = ErrorCode.OK;
		settings.setCount(0);
		settings.setIndex(0);
		settings.resetBlockIndex();
		try {
			// CI
			int ci = data.getUInt16();
			byte[] ln = new byte[6];
			data.get(ln);
			// Attribute Id
			short attributeIndex = data.getUInt8();

			// AccessSelection
			short selection = data.getUInt8();
			short selector = 0;
			Object parameters = null;
			GXDataInfo info = new GXDataInfo();
			if (selection != 0) {
				selector = data.getUInt8();
			}
			if (xml != null) {
				appendAttributeDescriptor(xml, ci, ln, attributeIndex);
				if (selection != 0) {
					info.setXml(xml);
					xml.appendStartTag(TranslatorTags.ACCESS_SELECTION);
					xml.appendLine(TranslatorTags.ACCESS_SELECTOR, "Value", xml.integerToHex(selector, 2));
					xml.appendStartTag(TranslatorTags.ACCESS_PARAMETERS);
					GXCommon.getData(settings, data, info);
					xml.appendEndTag(TranslatorTags.ACCESS_PARAMETERS);
					xml.appendEndTag(TranslatorTags.ACCESS_SELECTION);
				}
				return;
			}
			if (selection != 0) {
				parameters = GXCommon.getData(settings, data, info);
			}
			ObjectType ot = ObjectType.forValue(ci);
			GXDLMSObject obj = settings.getObjects().findByLN(ot, GXCommon.toLogicalName(ln));
			if (obj == null) {
				obj = server.notifyFindObject(ot, 0, GXCommon.toLogicalName(ln));
			}
			e = new ValueEventArgs(server, obj, attributeIndex, selector, parameters);
			if (obj == null) {
				// "Access Error : Device reports a undefined object."
				status = ErrorCode.UNDEFINED_OBJECT;
			} else {
				e.setInvokeId(invokeID);
				if (server.notifyGetAttributeAccess(e) == AccessMode.NO_ACCESS.getValue()) {
					// Read Write denied.
					status = ErrorCode.READ_WRITE_DENIED;
				} else {
					// Handle default Association LN read as a special case.
					if ((obj instanceof GXDLMSAssociationLogicalName || obj instanceof GXDLMSAssociationShortName)
							&& attributeIndex == 1) {
						GXDLMS.appendData(obj, attributeIndex, bb, new byte[] { 0, 0, 40, 0, 0, (byte) 255 });
					} else {
						if (obj instanceof GXDLMSProfileGeneric && attributeIndex == 2) {
							DataType dt;
							int rowsize = 0;
							GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) e.getTarget();
							// Count how many rows we can fit to one PDU.
							for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pg.getCaptureObjects()) {
								dt = it.getKey().getDataType(it.getValue().getAttributeIndex());
								if (dt == DataType.OCTET_STRING) {
									dt = it.getKey().getUIDataType(it.getValue().getAttributeIndex());
									if (dt == DataType.DATETIME) {
										rowsize += GXCommon.getDataTypeSize(DataType.DATETIME);
									} else if (dt == DataType.DATE) {
										rowsize += GXCommon.getDataTypeSize(DataType.DATE);
									} else if (dt == DataType.TIME) {
										rowsize += GXCommon.getDataTypeSize(DataType.TIME);
									}
								} else if (dt == DataType.NONE) {
									rowsize += 2;
								} else {
									rowsize += GXCommon.getDataTypeSize(dt);
								}
							}
							if (rowsize != 0) {
								e.setRowToPdu(settings.getMaxPduSize() / rowsize);
							}
						}
						server.notifyRead(new ValueEventArgs[] { e });
						Object value;
						if (e.getHandled()) {
							value = e.getValue();
						} else {
							settings.setCount(e.getRowEndIndex() - e.getRowBeginIndex());
							value = obj.getValue(settings, e);
						}
						server.notifyPostRead(new ValueEventArgs[] { e });
						if (e.isByteArray()) {
							bb.set((byte[]) value);
						} else {
							GXDLMS.appendData(obj, attributeIndex, bb, value);
						}
						status = e.getError();
					}
				}
			}
		} catch (Exception ex) {
			status = ErrorCode.READ_WRITE_DENIED;
		}
		GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invokeID, Command.GET_RESPONSE, 1, null, bb, status.getValue(),
				cipheredCommand), replyData);
		if (settings.getCount() != settings.getIndex() || bb.size() != bb.position()) {
			server.setTransaction(new GXDLMSLongTransaction(new ValueEventArgs[] { e }, Command.GET_REQUEST, bb));
		}
	}

	/*
	 * Handle get request next data block command.
	 */
	static void getRequestNextDataBlock(final GXDLMSSettings settings, final int invokeID,
			final GXDLMSServerBase server, final GXByteBuffer data, final GXByteBuffer replyData,
			final GXDLMSTranslatorStructure xml, final boolean streaming, final int cipheredCommand) throws Exception {
		GXByteBuffer bb = new GXByteBuffer();
		if (!streaming) {
			int index = (int) data.getUInt32();
			// Get block index.
			if (xml != null) {
				xml.appendLine(TranslatorTags.BLOCK_NUMBER, null, xml.integerToHex(index, 8));
				return;
			}
			if (index != settings.getBlockIndex()) {
				LOGGER.log(Level.INFO, "getRequestNextDataBlock failed. Invalid block number. "
						+ settings.getBlockIndex() + "/" + index);
				GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invokeID, Command.GET_RESPONSE, 2, null, bb,
						ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue(), cipheredCommand), replyData);
				return;
			}
		}
		settings.increaseBlockIndex();
		GXDLMSLNParameters p = new GXDLMSLNParameters(settings, invokeID,
				streaming ? Command.GENERAL_BLOCK_TRANSFER : Command.GET_RESPONSE, 2, null, bb, ErrorCode.OK.getValue(),
				cipheredCommand);
		p.streaming = streaming;
		p.windowSize = settings.getWindowSize();
		// If transaction is not in progress.
		if (server.getTransaction() == null) {
			p.setStatus(ErrorCode.NO_LONG_GET_OR_READ_IN_PROGRESS.getValue());
		} else {
			bb.set(server.getTransaction().getData());
			boolean moreData = settings.getIndex() != settings.getCount();
			if (moreData) {
				// If there is multiple blocks on the buffer.
				// This might happen when Max PDU size is very small.
				if (bb.size() < settings.getMaxPduSize()) {
					Object value;
					for (ValueEventArgs arg : server.getTransaction().getTargets()) {
						arg.setInvokeId(p.getInvokeId());
						server.notifyRead(new ValueEventArgs[] { arg });
						if (arg.getHandled()) {
							value = arg.getValue();
						} else {
							value = arg.getTarget().getValue(settings, arg);
						}
						p.setInvokeId(arg.getInvokeId());
						// Add data.
						if (arg.isByteArray()) {
							bb.set((byte[]) value);
						} else {
							GXDLMS.appendData(arg.getTarget(), arg.getIndex(), bb, value);
						}
					}
					moreData = settings.getIndex() != settings.getCount();
				}
			}
			p.setMultipleBlocks(true);
			GXDLMS.getLNPdu(p, replyData);
			if (moreData || bb.size() - bb.position() != 0) {
				server.getTransaction().setData(bb);
			} else {
				server.setTransaction(null);
				settings.resetBlockIndex();
			}
		}
	}

	/**
	 * Handle get request with list command.
	 *
	 * @param data Received data.
	 */
	private static void getRequestWithList(final GXDLMSSettings settings, final short invokeID,
			final GXDLMSServerBase server, final GXByteBuffer data, final GXByteBuffer replyData,
			final GXDLMSTranslatorStructure xml, final int cipheredCommand) throws Exception {
		GXByteBuffer bb = new GXByteBuffer();
		int pos;
		int cnt = GXCommon.getObjectCount(data);
		GXCommon.setObjectCount(cnt, bb);
		List<ValueEventArgs> list = new ArrayList<ValueEventArgs>();
		if (xml != null) {
			xml.appendStartTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST, "Qty", xml.integerToHex(cnt, 2));
		}
		for (pos = 0; pos != cnt; ++pos) {
			ObjectType ci = ObjectType.forValue(data.getUInt16());
			byte[] ln = new byte[6];
			data.get(ln);
			short attributeIndex = data.getUInt8();

			// AccessSelection
			int selection = data.getUInt8();
			int selector = 0;
			Object parameters = null;
			if (selection != 0) {
				selector = data.getUInt8();
				GXDataInfo i = new GXDataInfo();
				parameters = GXCommon.getData(settings, data, i);
			}
			if (xml != null) {
				xml.appendStartTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION);
				xml.appendStartTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR);
				if (xml.isComments()) {
					xml.appendComment(ci.toString());
				}
				xml.appendLine(TranslatorTags.CLASS_ID, "Value", xml.integerToHex(ci.getValue(), 4));
				xml.appendComment(GXCommon.toLogicalName(ln));
				xml.appendLine(TranslatorTags.INSTANCE_ID, "Value", GXCommon.toHex(ln, false));
				xml.appendLine(TranslatorTags.ATTRIBUTE_ID, "Value", xml.integerToHex(attributeIndex, 2));
				xml.appendEndTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR);
				xml.appendEndTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION);
			} else {
				GXDLMSObject obj = settings.getObjects().findByLN(ci, GXCommon.toLogicalName(ln));
				if (obj == null) {
					obj = server.notifyFindObject(ci, 0, GXCommon.toLogicalName(ln));
				}
				ValueEventArgs arg = new ValueEventArgs(server, obj, attributeIndex, selector, parameters);
				arg.setInvokeId(invokeID);
				if (obj == null) {
					arg.setError(ErrorCode.UNDEFINED_OBJECT);
					list.add(arg);
				} else {
					if (server.notifyGetAttributeAccess(arg) == AccessMode.NO_ACCESS.getValue()) {
						// Read Write denied.
						arg.setError(ErrorCode.READ_WRITE_DENIED);
						list.add(arg);
					} else {
						list.add(arg);
					}
				}
			}
		}
		if (xml != null) {
			xml.appendEndTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST);
			return;
		}
		server.notifyRead(list.toArray(new ValueEventArgs[list.size()]));
		Object value;
		pos = 0;
		GXDLMSLNParameters p = new GXDLMSLNParameters(settings, invokeID, Command.GET_RESPONSE, 3, null, bb, 0xFF,
				cipheredCommand);
		for (ValueEventArgs it : list) {
			try {
				if (it.getHandled()) {
					value = it.getValue();
				} else {
					value = it.getTarget().getValue(settings, it);
				}
				bb.setUInt8(it.getError().getValue());
				if (it.isByteArray()) {
					bb.set((byte[]) value);
				} else {
					GXDLMS.appendData(it.getTarget(), it.getIndex(), bb, value);
				}
				p.setInvokeId(it.getInvokeId());
			} catch (Exception ex) {
				bb.setUInt8(ErrorCode.HARDWARE_FAULT.getValue());
			}
			if (settings.getIndex() != settings.getCount()) {
				server.setTransaction(new GXDLMSLongTransaction(list.toArray(new ValueEventArgs[list.size()]),
						Command.GET_REQUEST, null));
			}
			++pos;
		}
		server.notifyPostRead(list.toArray(new ValueEventArgs[list.size()]));
		GXDLMS.getLNPdu(p, replyData);
	}

	private static void handleSetRequestNormal(final GXDLMSSettings settings, final GXDLMSServerBase server,
			final GXByteBuffer data, final int type, final GXDLMSLNParameters p, final GXByteBuffer replyData,
			final GXDLMSTranslatorStructure xml) throws Exception {
		Object value = null;
		GXDataInfo reply = new GXDataInfo();
		// CI
		short ci = data.getInt16();
		ObjectType ot = ObjectType.forValue(ci & 0xFFFF);
		byte[] ln = new byte[6];
		data.get(ln);
		// Attribute index.
		short index = data.getUInt8();
		// Get Access Selection.
		data.getUInt8();
		if (type == 2) {
			short lastBlock = data.getUInt8();
			p.setMultipleBlocks(lastBlock == 0);
			long blockNumber = data.getUInt32();
			if (blockNumber != settings.getBlockIndex()) {
				LOGGER.log(Level.INFO, "handleSetRequest failed. Invalid block number. " + settings.getBlockIndex()
						+ "/" + blockNumber);
				p.setStatus(ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue());
				return;
			}
			settings.increaseBlockIndex();
			int size = GXCommon.getObjectCount(data);
			int realSize = data.size() - data.position();
			if (size != realSize) {
				LOGGER.log(Level.INFO, "handleSetRequest failed. Invalid block size.");
				p.setStatus(ErrorCode.DATA_BLOCK_UNAVAILABLE.getValue());
				return;
			}
			if (xml != null) {
				appendAttributeDescriptor(xml, ci, ln, index);
				xml.appendStartTag(TranslatorTags.DATA_BLOCK);
				xml.appendLine(TranslatorTags.LAST_BLOCK, "Value", xml.integerToHex(lastBlock, 2));
				xml.appendLine(TranslatorTags.BLOCK_NUMBER, "Value", xml.integerToHex(blockNumber, 8));
				xml.appendLine(TranslatorTags.RAW_DATA, "Value", data.remainingHexString(false));
				xml.appendEndTag(TranslatorTags.DATA_BLOCK);
			}
			return;
		}
		if (xml != null) {
			appendAttributeDescriptor(xml, ci, ln, index);
			xml.appendStartTag(TranslatorTags.VALUE);
			GXDataInfo di = new GXDataInfo();
			di.setXml(xml);
			value = GXCommon.getData(settings, data, di);
			if (!di.isComplete()) {
				value = GXCommon.toHex(data.getData(), false, data.position(), data.size() - data.position());
			} else if (value instanceof byte[]) {
				value = GXCommon.toHex((byte[]) value, false);
			}
			xml.appendEndTag(TranslatorTags.VALUE);
			return;
		}
		if (!p.isMultipleBlocks()) {
			settings.resetBlockIndex();
			value = GXCommon.getData(settings, data, reply);
		}

		GXDLMSObject obj = settings.getObjects().findByLN(ot, GXCommon.toLogicalName(ln));
		if (obj == null) {
			obj = server.notifyFindObject(ot, 0, GXCommon.toLogicalName(ln));
		}
		// If target is unknown.
		if (obj == null) {
			// Device reports a undefined object.
			p.setStatus(ErrorCode.UNDEFINED_OBJECT.getValue());
		} else {
			ValueEventArgs e = new ValueEventArgs(server, obj, index, 0, null);
			e.setInvokeId(p.getInvokeId());
			int am = server.notifyGetAttributeAccess(e);
			// If write is denied.
			if ((am & AccessMode.WRITE.getValue()) == 0) {
				// Read Write denied.
				p.setStatus(ErrorCode.READ_WRITE_DENIED.getValue());
			} else {
				try {
					if (value instanceof byte[]) {
						DataType dt = obj.getDataType(index);
						boolean useUtc = false;
						if (settings != null) {
							useUtc = settings.getUseUtc2NormalTime();
						}
						if (dt != DataType.NONE && dt != DataType.OCTET_STRING) {
							value = GXDLMSClient.changeType((byte[]) value, dt, useUtc);
						}
					}
					e.setValue(value);
					ValueEventArgs[] list = new ValueEventArgs[] { e };
					if (p.isMultipleBlocks()) {
						server.setTransaction(new GXDLMSLongTransaction(list, Command.GET_REQUEST, data));
					}
					server.notifyWrite(list);
					if (e.getError() != ErrorCode.OK) {
						p.setStatus(e.getError().getValue());
					} else if (!e.getHandled() && !p.isMultipleBlocks()) {
						obj.setValue(settings, e);
					}
					server.notifyPostWrite(list);
					p.setInvokeId(e.getInvokeId());
					p.setStatus(e.getError().getValue());
				} catch (Exception ex) {
					p.setStatus(ErrorCode.HARDWARE_FAULT.getValue());
				}
			}
		}
	}

	private static void hanleSetRequestWithDataBlock(final GXDLMSSettings settings, final GXDLMSServerBase server,
			final GXByteBuffer data, final GXDLMSLNParameters p, final GXByteBuffer replyData,
			final GXDLMSTranslatorStructure xml) throws Exception {
		GXDataInfo reply = new GXDataInfo();
		short lastBlock = data.getUInt8();
		p.setMultipleBlocks(lastBlock == 0);
		long blockNumber = data.getUInt32();
		if (xml == null && blockNumber != settings.getBlockIndex()) {
			LOGGER.log(Level.INFO,
					"handleSetRequest failed. Invalid block number. " + settings.getBlockIndex() + "/" + blockNumber);
			p.setStatus(ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue());
		} else {
			settings.increaseBlockIndex();
			int size = GXCommon.getObjectCount(data);
			int realSize = data.size() - data.position();
			if (size != realSize) {
				LOGGER.log(Level.INFO, "handleSetRequest failed. Invalid block size.");
				p.setStatus(ErrorCode.DATA_BLOCK_UNAVAILABLE.getValue());
			}
			if (xml != null) {
				xml.appendStartTag(TranslatorTags.DATA_BLOCK);
				if (xml.getOutputType() == TranslatorOutputType.SIMPLE_XML) {
					xml.appendLine(TranslatorTags.LAST_BLOCK, "Value", xml.integerToHex(lastBlock, 2));

				} else {
					xml.appendLine(TranslatorTags.LAST_BLOCK, "Value", lastBlock != 0 ? "true" : "false");
				}
				xml.appendLine(TranslatorTags.BLOCK_NUMBER, "Value", xml.integerToHex(blockNumber, 8));
				xml.appendLine(TranslatorTags.RAW_DATA, "Value", data.remainingHexString(false));
				xml.appendEndTag(TranslatorTags.DATA_BLOCK);
				return;
			}
			server.getTransaction().getData().set(data);
			// If all data is received.
			if (!p.isMultipleBlocks()) {
				try {
					Object value = GXCommon.getData(settings, server.getTransaction().getData(), reply);
					if (value instanceof byte[]) {
						DataType dt = server.getTransaction().getTargets()[0].getTarget()
								.getDataType(server.getTransaction().getTargets()[0].getIndex());
						if (dt != DataType.NONE && dt != DataType.OCTET_STRING) {
							boolean useUtc;
							if (settings != null) {
								useUtc = settings.getUseUtc2NormalTime();
							} else {
								useUtc = false;
							}
							value = GXDLMSClient.changeType((byte[]) value, dt, useUtc);
						}
					}
					server.getTransaction().getTargets()[0].setValue(value);
					server.notifyWrite(server.getTransaction().getTargets());
					if (!server.getTransaction().getTargets()[0].getHandled() && !p.isMultipleBlocks()) {
						server.getTransaction().getTargets()[0].getTarget().setValue(settings,
								server.getTransaction().getTargets()[0]);
					}
					server.notifyPostWrite(server.getTransaction().getTargets());
				} catch (RuntimeException e) {
					p.setStatus(ErrorCode.HARDWARE_FAULT.getValue());
				} finally {
					server.setTransaction(null);
				}
				settings.resetBlockIndex();
			}
		}
		p.setMultipleBlocks(true);
	}

	private static void hanleSetRequestWithList(final GXDLMSSettings settings, final int invokeID,
			final GXDLMSServerBase server, final GXByteBuffer data, final GXDLMSLNParameters p,
			final GXByteBuffer replyData, final GXDLMSTranslatorStructure xml) throws Exception {
		ValueEventArgs e;
		int cnt = GXCommon.getObjectCount(data);
		List<ValueEventArgs> list = new ArrayList<ValueEventArgs>();
		if (xml != null) {
			xml.appendStartTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST, "Qty", xml.integerToHex(cnt, 2));
		}
		try {
			for (int pos = 0; pos != cnt; ++pos) {
				ObjectType ci = ObjectType.forValue(data.getUInt16());
				byte[] ln = new byte[6];
				data.get(ln);
				short attributeIndex = data.getUInt8();
				// AccessSelection
				int selection = data.getUInt8();
				int selector = 0;
				Object parameters = null;
				if (selection != 0) {
					selector = data.getUInt8();
					GXDataInfo info = new GXDataInfo();
					parameters = GXCommon.getData(settings, data, info);
				}
				if (xml != null) {
					xml.appendStartTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION);
					xml.appendStartTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR);
					xml.appendComment(ci.toString());
					xml.appendLine(TranslatorTags.CLASS_ID, "Value", xml.integerToHex(ci.getValue(), 4));
					xml.appendComment(GXCommon.toLogicalName(ln));
					xml.appendLine(TranslatorTags.INSTANCE_ID, "Value", GXCommon.toHex(ln, false));
					xml.appendLine(TranslatorTags.ATTRIBUTE_ID, "Value", xml.integerToHex(attributeIndex, 2));
					xml.appendEndTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR);
					xml.appendEndTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION);
				} else {
					GXDLMSObject obj = settings.getObjects().findByLN(ci, GXCommon.toLogicalName(ln));
					if (obj == null) {
						obj = server.notifyFindObject(ci, 0, GXCommon.toLogicalName(ln));
					}
					if (obj == null) {
						// "Access Error : Device reports a undefined object."
						e = new ValueEventArgs(server, obj, attributeIndex, 0, 0);
						e.setError(ErrorCode.UNDEFINED_OBJECT);
						list.add(e);
					} else {
						ValueEventArgs arg = new ValueEventArgs(server, obj, attributeIndex, selector, parameters);
						arg.setInvokeId(invokeID);
						if (server.notifyGetAttributeAccess(arg) == 0) {
							// Read Write denied.
							arg.setError(ErrorCode.READ_WRITE_DENIED);
							list.add(arg);
						} else {
							list.add(arg);
						}
					}
				}
			}
			cnt = GXCommon.getObjectCount(data);
			if (xml != null) {
				xml.appendEndTag(TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST);
				xml.appendStartTag(TranslatorTags.VALUE_LIST, "Qty", xml.integerToHex(cnt, 2));
			}
			for (int pos = 0; pos != cnt; ++pos) {
				GXDataInfo di = new GXDataInfo();
				di.setXml(xml);
				if (xml != null && xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
					xml.appendStartTag(Command.WRITE_REQUEST, SingleReadResponse.DATA);
				}
				Object value = GXCommon.getData(settings, data, di);
				if (!di.isComplete()) {
					value = GXCommon.toHex(data.getData(), false, data.position(), data.size() - data.position());
				} else if (value instanceof byte[]) {
					value = GXCommon.toHex((byte[]) value, false);
				}
				if (xml != null && xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
					xml.appendEndTag(Command.WRITE_REQUEST, SingleReadResponse.DATA);
				}
			}
			if (xml != null) {
				xml.appendEndTag(TranslatorTags.VALUE_LIST);
			}
		} catch (Exception ex) {
			if (xml == null) {
				throw ex;
			}
		}
	}

	public static void methodRequestNormal(GXDLMSSettings settings, short invokeId, GXDLMSServerBase server,
			GXByteBuffer data, GXDLMSConnectionEventArgs connectionInfo, GXByteBuffer replyData,
			GXDLMSTranslatorStructure xml, int cipheredCommand) throws Exception {
		ErrorCode error = ErrorCode.OK;
		ValueEventArgs e = null;
		GXByteBuffer bb = new GXByteBuffer();
		// CI
		int ci = data.getUInt16();
		ObjectType ot = ObjectType.forValue(ci);
		byte[] ln = new byte[6];
		data.get(ln);
		// Attribute Id
		short id = data.getUInt8();
		// Get parameters.
		Object parameters = null;
		byte selection = (byte) data.getUInt8();
		if (xml != null) {
			xml.appendStartTag(Command.METHOD_REQUEST);
			appendMethodDescriptor(xml, ci, ln, id);
			if (selection != 0) {
				// MethodInvocationParameters
				xml.appendStartTag(TranslatorTags.METHOD_INVOCATION_PARAMETERS);
				GXDataInfo di = new GXDataInfo();
				di.setXml(xml);
				GXCommon.getData(settings, data, di);
				xml.appendEndTag(TranslatorTags.METHOD_INVOCATION_PARAMETERS);
			}
			xml.appendEndTag(Command.METHOD_REQUEST, ActionRequestType.NORMAL);
			xml.appendEndTag(Command.METHOD_REQUEST);
			return;
		}
		if (selection != 0) {
			GXDataInfo info = new GXDataInfo();
			parameters = GXCommon.getData(settings, data, info);
		}
		GXDLMSObject obj = settings.getObjects().findByLN(ot, GXCommon.toLogicalName(ln));
		if ((settings.getConnected() & ConnectionState.DLMS) == 0 && cipheredCommand == Command.NONE
				&& (ci != ObjectType.ASSOCIATION_LOGICAL_NAME.getValue() || id != 1)) {
			replyData.set(GXDLMSServerBase.generateConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR,
					ServiceError.SERVICE, Service.UNSUPPORTED.getValue()));
			return;
		}
		if (obj == null) {
			obj = server.notifyFindObject(ot, 0, GXCommon.toLogicalName(ln));
		}
		if (obj == null) {
			// Device reports a undefined object.
			error = ErrorCode.UNDEFINED_OBJECT;
		} else {
			e = new ValueEventArgs(server, obj, id, 0, parameters);
			e.setInvokeId(invokeId);
			if (server.notifyGetMethodAccess(e) == 0) {
				error = ErrorCode.READ_WRITE_DENIED;
			} else {
				try {
					server.notifyAction(new ValueEventArgs[] { e });
					byte[] actionReply;
					if (e.getHandled()) {
						actionReply = (byte[]) e.getValue();
					} else {
						actionReply = obj.invoke(settings, e);
					}
					server.notifyPostAction(new ValueEventArgs[] { e });
					// Set default action reply if not given.
					if (actionReply != null && e.getError() == ErrorCode.OK) {
						// Add return parameters
						bb.setUInt8(1);
						// Add parameters error code.
						bb.setUInt8(0);
						if (e.isByteArray()) {
							bb.set(actionReply);
						} else {
							GXCommon.setData(settings, bb, GXDLMSConverter.getDLMSDataType(actionReply), actionReply);
						}
					} else {
						// Add parameters error code.
						error = e.getError();
						// Add return parameters
						bb.setUInt8(0);
					}
				} catch (Exception ex) {
					// Add parameters error code.
					error = ErrorCode.INCONSISTENT_CLASS;
					// Add return parameters
					bb.setUInt8(0);
				}
				invokeId = (short) e.getInvokeId();
			}
		}
		GXDLMSLNParameters p = new GXDLMSLNParameters(settings, invokeId, Command.METHOD_RESPONSE, 1, null, bb,
				error.getValue(), cipheredCommand);
		GXDLMS.getLNPdu(p, replyData);
		// If High level authentication fails.
		if (obj instanceof GXDLMSAssociationLogicalName && id == 1) {
			if (((GXDLMSAssociationLogicalName) obj).getAssociationStatus() == AssociationStatus.ASSOCIATED) {
				server.notifyConnected(connectionInfo);
				settings.setConnected(settings.getConnected() | ConnectionState.DLMS);
			} else {
				server.notifyInvalidConnection(connectionInfo);
				settings.setConnected(settings.getConnected() & ~ConnectionState.DLMS);
			}
		}
		// Start to use new keys.
		if (e != null && error == ErrorCode.OK && obj instanceof GXDLMSSecuritySetup && (id == 2 || id == 3)) {
			((GXDLMSSecuritySetup) obj).applyKeys(settings, e);
		}
	}

	public static void methodRequestNextBlock(GXDLMSSettings settings, GXDLMSServerBase server, GXByteBuffer data,
			GXDLMSConnectionEventArgs connectionInfo, GXByteBuffer replyData, GXDLMSTranslatorStructure xml,
			boolean streaming, int cipheredCommand) throws Exception {
		GXByteBuffer bb = new GXByteBuffer();
		if (!streaming) {
			// Get block index.
			long index = data.getUInt32();
			if (xml != null) {
				xml.appendLine(TranslatorTags.BLOCK_NUMBER, null, xml.integerToHex(index, 8));
				return;
			}
			if (index != settings.getBlockIndex()) {
				LOGGER.log(Level.INFO, "methodRequestNextBlock failed. Invalid block number. "
						+ settings.getBlockIndex() + "/" + index);
				GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, 0, Command.GET_RESPONSE, 2, null, bb,
						ErrorCode.DATA_BLOCK_NUMBER_INVALID.getValue(), cipheredCommand), replyData);
				return;
			}
		}
		settings.increaseBlockIndex();
		GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0,
				streaming ? Command.GENERAL_BLOCK_TRANSFER : Command.GET_RESPONSE, 2, null, bb, ErrorCode.OK.getValue(),
				cipheredCommand);
		p.setStreaming(streaming);
		p.setWindowSize(settings.getWindowSize());
		// If transaction is not in progress.
		if (server.getTransaction() == null) {
			p.setStatus(ErrorCode.NO_LONG_GET_OR_READ_IN_PROGRESS.getValue());
		} else {
			bb.set(server.getTransaction().getData());
			boolean moreData = settings.getIndex() != settings.getCount();
			if (moreData) {
				// If there is multiple blocks on the buffer.
				// This might happen when Max PDU size is very small.
				if (bb.size() < settings.getMaxPduSize()) {
					Object value;
					for (ValueEventArgs arg : server.getTransaction().getTargets()) {
						arg.setInvokeId(p.getInvokeId());
						server.notifyAction(new ValueEventArgs[] { arg });
						if (arg.getHandled()) {
							value = arg.getValue();
						} else {
							value = arg.getTarget().invoke(settings, arg);
						}
						p.setInvokeId(arg.getInvokeId());
						// Add data.
						if (arg.isByteArray()) {
							bb.set((byte[]) value);
						} else {
							GXDLMS.appendData(arg.getTarget(), arg.getIndex(), bb, value);
						}
					}
					moreData = settings.getIndex() != settings.getCount();
				}
			}
			p.setMultipleBlocks(true);
			GXDLMS.getLNPdu(p, replyData);
			if (moreData || bb.available() != 0) {
				server.getTransaction().setData(bb);
			} else {
				server.setTransaction(null);
				settings.resetBlockIndex();
			}
		}
	}

	/*
	 * Handle action request.
	 */
	static void handleMethodRequest(final GXDLMSSettings settings, final GXDLMSServerBase server,
			final GXByteBuffer data, final GXDLMSConnectionEventArgs connectionInfo, final GXByteBuffer replyData,
			final GXDLMSTranslatorStructure xml, final int cipheredCommand) throws Exception {
		// Get type.
		byte type = (byte) data.getUInt8();
		// Get invoke ID and priority.
		short invokeId = data.getUInt8();
		settings.updateInvokeId(invokeId);
		GXDLMS.addInvokeId(xml, Command.METHOD_REQUEST, type, invokeId);
		switch (type) {
		case ActionResponseType.NORMAL:
			methodRequestNormal(settings, invokeId, server, data, connectionInfo, replyData, xml, cipheredCommand);
			break;
		case ActionResponseType.WITH_FIRST_BLOCK:
			methodRequestNextBlock(settings, server, data, connectionInfo, replyData, xml, false, cipheredCommand);
			break;
		case ActionResponseType.WITH_LIST:
			throw new IllegalArgumentException("Invalid Command.");
		case ActionResponseType.WITH_BLOCK:
			throw new IllegalArgumentException("Invalid Command.");
		default:
			if (xml == null) {
				Logger.getLogger(GXDLMS.class.getName()).log(Level.WARNING,
						"HandleMethodRequest failed. Invalid command type :{0} ", type);
				settings.resetBlockIndex();
				type = ActionRequestType.NORMAL;
				data.clear();
				GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invokeId, Command.METHOD_RESPONSE, (byte) type, null,
						null, ErrorCode.READ_WRITE_DENIED.getValue(), cipheredCommand), replyData);
			}
		}
		if (xml != null) {
			if (type <= ActionRequestType.WITH_LIST) {
				xml.appendEndTag(Command.METHOD_REQUEST, type);
			}
			xml.appendEndTag(Command.METHOD_REQUEST);
		}
	}

	/*
	 * Handle Access request.
	 * 
	 * @param settings DLMS settings.
	 * 
	 * @param server server.
	 * 
	 * @param data Received data from the client.
	 * 
	 * @param reply reply data-
	 * 
	 * @param xml XML settings.
	 */
	public static void handleAccessRequest(final GXDLMSSettings settings, final GXDLMSServerBase server,
			final GXByteBuffer data, final GXByteBuffer reply, final GXDLMSTranslatorStructure xml,
			final int cipheredCommand) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// Return error if connection is not established.
		if (xml == null && (settings.getConnected() & ConnectionState.DLMS) == 0 && cipheredCommand == Command.NONE) {
			reply.set(GXDLMSServerBase.generateConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR,
					ServiceError.SERVICE, Service.UNSUPPORTED.getValue()));
			return;
		}
		// Get long invoke id and priority.
		long invokeId = data.getUInt32();
		settings.setLongInvokeID(invokeId);
		int len = GXCommon.getObjectCount(data);
		byte[] tmp = null;
		// If date time is given.
		if (len != 0) {
			tmp = new byte[len];
			data.get(tmp);
			if (xml == null) {

				DataType dt = DataType.DATETIME;
				if (len == 4) {
					dt = DataType.TIME;
				} else if (len == 5) {
					dt = DataType.DATE;
				}
				GXDataInfo info = new GXDataInfo();
				info.setType(dt);
				GXCommon.getData(settings, new GXByteBuffer(tmp), info);
			}
		}
		// Get object count.
		int cnt = GXCommon.getObjectCount(data);
		if (xml != null) {
			xml.appendStartTag(Command.ACCESS_REQUEST);
			xml.appendLine(TranslatorTags.LONG_INVOKE_ID, "Value", xml.integerToHex(invokeId, 8));
			xml.appendLine(TranslatorTags.DATE_TIME, "Value", GXCommon.toHex(tmp, false));
			xml.appendStartTag(TranslatorTags.ACCESS_REQUEST_BODY);
			xml.appendStartTag(TranslatorTags.LIST_OF_ACCESS_REQUEST_SPECIFICATION, "Qty", xml.integerToHex(cnt, 2));
		}
		List<GXDLMSAccessItem> list = new ArrayList<GXDLMSAccessItem>();
		byte type;
		for (int pos = 0; pos != cnt; ++pos) {
			type = (byte) data.getUInt8();
			if (!(type == AccessServiceCommandType.GET || type == AccessServiceCommandType.SET
					|| type == AccessServiceCommandType.ACTION)) {
				throw new IllegalArgumentException("Invalid access service command type.");
			}
			// CI
			int ci = data.getUInt16();
			byte[] ln = new byte[6];
			data.get(ln);
			// Attribute Id
			short attributeIndex = data.getUInt8();
			if (xml != null) {
				xml.appendStartTag(TranslatorTags.ACCESS_REQUEST_SPECIFICATION);
				xml.appendStartTag(Command.ACCESS_REQUEST, type);
				appendAttributeDescriptor(xml, ci, ln, attributeIndex);
				xml.appendEndTag(Command.ACCESS_REQUEST, type);
				xml.appendEndTag(TranslatorTags.ACCESS_REQUEST_SPECIFICATION);
			} else {
				ObjectType ot = ObjectType.forValue(ci);
				GXDLMSObject obj = settings.getObjects().findByLN(ot, GXCommon.toLogicalName(ln));
				if (obj == null) {
					try {
						obj = server.notifyFindObject(ot, 0, GXCommon.toLogicalName(ln));
					} catch (Exception e) {
						obj = null;
					}
				}
				list.add(new GXDLMSAccessItem(type, obj, attributeIndex));
			}
		}
		if (xml != null) {
			xml.appendEndTag(TranslatorTags.LIST_OF_ACCESS_REQUEST_SPECIFICATION);
			xml.appendStartTag(TranslatorTags.ACCESS_REQUEST_LIST_OF_DATA, "Qty", xml.integerToHex(cnt, 2));
		}
		// Get data count.
		cnt = GXCommon.getObjectCount(data);
		GXByteBuffer bb = new GXByteBuffer();
		// access-request-specification.
		bb.setUInt8(0);
		GXCommon.setObjectCount(cnt, bb);
		GXByteBuffer results = new GXByteBuffer();
		GXCommon.setObjectCount(cnt, results);
		try {
			for (int pos = 0; pos != cnt; ++pos) {
				GXDataInfo di = new GXDataInfo();
				di.setXml(xml);
				if (xml != null && xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
					xml.appendStartTag(Command.WRITE_REQUEST, SingleReadResponse.DATA);
				}
				Object value = GXCommon.getData(settings, data, di);
				if (!di.isComplete()) {
					value = GXCommon.toHex(data.getData(), false, data.position(), data.size() - data.position());
				} else if (value instanceof byte[]) {
					value = GXCommon.toHex((byte[]) value, false);
				}
				if (xml == null) {
					GXDLMSAccessItem it = list.get(pos);
					results.setUInt8(it.getCommand());
					if (it.getTarget() == null) {
						// If target is unknown.
						bb.setUInt8(0);
						results.setUInt8(ErrorCode.UNAVAILABLE_OBJECT.getValue());
					} else {
						ValueEventArgs e = new ValueEventArgs(settings, it.getTarget(), it.getIndex(), 0, value);
						settings.setIndex(0);
						if (it.getCommand() == AccessServiceCommandType.GET) {
							if ((server.notifyGetAttributeAccess(e) & AccessMode.READ.getValue()) == 0) {
								// Read Write denied.
								results.setUInt8(ErrorCode.READ_WRITE_DENIED.getValue());
							} else {
								server.notifyRead(new ValueEventArgs[] { e });
								if (e.getHandled()) {
									value = e.getValue();
								} else {
									value = it.getTarget().getValue(settings, e);
								}
								if (e.isByteArray()) {
									bb.set((byte[]) value);
								} else {
									GXDLMS.appendData(it.getTarget(), it.getIndex(), bb, value);
								}
								server.notifyPostRead(new ValueEventArgs[] { e });
								results.setUInt8(ErrorCode.OK.getValue());
							}
						} else if (it.getCommand() == AccessServiceCommandType.SET) {
							results.setUInt8(ErrorCode.OK.getValue());
						} else {
							results.setUInt8(ErrorCode.OK.getValue());
						}
					}
				}
				if (xml != null && xml.getOutputType() == TranslatorOutputType.STANDARD_XML) {
					xml.appendEndTag(Command.WRITE_REQUEST, SingleReadResponse.DATA);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		if (xml != null) {
			xml.appendEndTag(TranslatorTags.ACCESS_REQUEST_LIST_OF_DATA);
			xml.appendEndTag(TranslatorTags.ACCESS_REQUEST_BODY);
			xml.appendEndTag(Command.ACCESS_REQUEST);
		} else {
			// Append status codes.
			bb.set(results);
			GXDLMS.getLNPdu(new GXDLMSLNParameters(settings, invokeId, Command.ACCESS_RESPONSE, 0xff, null, bb, 0xFF,
					cipheredCommand), reply);
		}
	}

	/**
	 * @param settings DLMS settings.
	 * @param reply    Received data.
	 * @param list     Received event notification objects.
	 */
	static void handleEventNotification(final GXDLMSSettings settings, final GXReplyData reply,
			final List<Entry<GXDLMSObject, Integer>> list) {
		reply.setTime(null);
		int len = reply.getData().getUInt8();
		byte[] tmp = null;
		// If date time is given.
		if (len != 0) {
			boolean useUtc;
			if (settings != null) {
				useUtc = settings.getUseUtc2NormalTime();
			} else {
				useUtc = false;
			}
			len = reply.getData().getUInt8();
			tmp = new byte[len];
			reply.getData().get(tmp);
			reply.setTime((GXDateTime) GXDLMSClient.changeType(tmp, DataType.DATETIME, useUtc));
		}
		if (reply.getXml() != null) {
			reply.getXml().appendStartTag(Command.EVENT_NOTIFICATION);
			if (reply.getTime() != null) {
				reply.getXml().appendComment(String.valueOf(reply.getTime()));
				reply.getXml().appendLine(TranslatorTags.TIME, null, GXCommon.toHex(tmp, false));
			}
		}
		int ci = reply.getData().getUInt16();
		byte[] ln = new byte[6];
		reply.getData().get(ln);
		short index = reply.getData().getUInt8();
		if (reply.getXml() != null) {
			appendAttributeDescriptor(reply.getXml(), ci, ln, index);
			reply.getXml().appendStartTag(TranslatorTags.ATTRIBUTE_VALUE);
		}
		GXDataInfo di = new GXDataInfo();
		di.setXml(reply.getXml());
		Object value = GXCommon.getData(settings, reply.getData(), di);

		if (reply.getXml() != null) {
			reply.getXml().appendEndTag(TranslatorTags.ATTRIBUTE_VALUE);
			reply.getXml().appendEndTag(Command.EVENT_NOTIFICATION);
		} else {
			GXDLMSObject obj = settings.getObjects().findByLN(ObjectType.forValue(ci), GXCommon.toLogicalName(ln));
			if (obj != null) {
				ValueEventArgs v = new ValueEventArgs(obj, index, 0, null);
				v.setValue(value);
				obj.setValue(settings, v);
				list.add(new GXSimpleEntry<GXDLMSObject, Integer>(obj, (int) index));
			} else {
				Logger.getLogger(GXDLMS.class.getName()).log(Level.INFO,
						"InformationReport message. Unknown object :{0} ",
						String.valueOf(ObjectType.forValue(ci)) + " " + GXCommon.toLogicalName(ln));
			}
		}
	}
}
