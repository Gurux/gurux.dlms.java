package gurux.dlms;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import gurux.dlms.enums.Access;
import gurux.dlms.enums.AccessServiceCommandType;
import gurux.dlms.enums.ApplicationReference;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.Definition;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ExceptionServiceError;
import gurux.dlms.enums.ExceptionStateError;
import gurux.dlms.enums.HardwareResource;
import gurux.dlms.enums.Initiate;
import gurux.dlms.enums.LoadDataSet;
import gurux.dlms.enums.Task;
import gurux.dlms.enums.VdeStateError;

final class TranslatorStandardTags {
	/**
	 * Constructor.
	 */
	private TranslatorStandardTags() {

	}

	/*
	 * Get general tags.
	 */
	static void getGeneralTags(final TranslatorOutputType type, final HashMap<Integer, String> list) {
		GXDLMSTranslator.addTag(list, Command.SNRM, "Snrm");
		GXDLMSTranslator.addTag(list, Command.UNACCEPTABLE_FRAME, "UnacceptableFrame");
		GXDLMSTranslator.addTag(list, Command.DISCONNECT_MODE, "DisconnectMode");
		GXDLMSTranslator.addTag(list, Command.UA, "Ua");
		GXDLMSTranslator.addTag(list, Command.AARQ, "aarq");
		GXDLMSTranslator.addTag(list, Command.AARE, "aare");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.APPLICATION_CONTEXT_NAME, "application-context-name");
		GXDLMSTranslator.addTag(list, Command.INITIATE_RESPONSE, "initiateResponse");
		GXDLMSTranslator.addTag(list, Command.INITIATE_REQUEST, "initiateRequest");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.NEGOTIATED_QUALITY_OF_SERVICE,
				"negotiated-quality-of-service");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.PROPOSED_QUALITY_OF_SERVICE, "proposed-quality-of-service");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.PROPOSED_DLMS_VERSION_NUMBER,
				"proposed-dlms-version-number");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.PROPOSED_MAX_PDU_SIZE, "client-max-receive-pdu-size");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.PROPOSED_CONFORMANCE, "proposed-conformance");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.VAA_NAME, "vaa-name");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.NEGOTIATED_CONFORMANCE, "negotiated-conformance");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.NEGOTIATED_DLMS_VERSION_NUMBER,
				"negotiated-dlms-version-number");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.NEGOTIATED_MAX_PDU_SIZE, "server-max-receive-pdu-size");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CONFORMANCE_BIT, "ConformanceBit");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.SENDER_ACSE_REQUIREMENTS, "sender-acse-requirements");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONDER_ACSE_REQUIREMENT, "responder-acse-requirements");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONDING_MECHANISM_NAME, "mechanism-name");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CALLING_MECHANISM_NAME, "mechanism-name");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CALLING_AUTHENTICATION, "calling-authentication-value");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONDING_AUTHENTICATION,
				"responding-authentication-value");
		GXDLMSTranslator.addTag(list, Command.RELEASE_REQUEST, "rlrq");
		GXDLMSTranslator.addTag(list, Command.RELEASE_RESPONSE, "rlre");
		GXDLMSTranslator.addTag(list, Command.DISCONNECT_REQUEST, "Disc");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.ASSOCIATION_RESULT, "result");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESULT_SOURCE_DIAGNOSTIC, "result-source-diagnostic");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.ACSE_SERVICE_USER, "acse-service-user");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.ACSE_SERVICE_PROVIDER, "acse-service-provider");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CALLING_AP_TITLE, "CallingAPTitle");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONDING_AP_TITLE, "RespondingAPTitle");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CHAR_STRING, "charstring");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.DEDICATED_KEY, "dedicated-key");
		GXDLMSTranslator.addTag(list, TranslatorTags.RESPONSE_ALLOWED, "response-allowed");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.USER_INFORMATION, "user-information");
		GXDLMSTranslator.addTag(list, Command.CONFIRMED_SERVICE_ERROR, "confirmedServiceError");
		GXDLMSTranslator.addTag(list, Command.INFORMATION_REPORT, "informationReportRequest");
		GXDLMSTranslator.addTag(list, Command.EVENT_NOTIFICATION, "event-notification-request");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CALLING_AE_INVOCATION_ID, "calling-AE-invocation-id");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CALLED_AE_INVOCATION_ID, "called-AE-invocation-id");
		GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONDING_AE_INVOCATION_ID, "responding-AE-invocation-id");
		GXDLMSTranslator.addTag(list, Command.EXCEPTION_RESPONSE, "exception-response");
		GXDLMSTranslator.addTag(list, TranslatorTags.STATE_ERROR, "state-error");
		GXDLMSTranslator.addTag(list, TranslatorTags.SERVICE_ERROR, "service-error");
	}

	/*
	 * Get SN tags.
	 */
	static void getSnTags(final TranslatorOutputType type, final HashMap<Integer, String> list) {
		list.put(Command.READ_REQUEST, "readRequest");
		list.put(Command.WRITE_REQUEST, "writeRequest");
		list.put(Command.WRITE_RESPONSE, "writeResponse");
		list.put(Command.WRITE_REQUEST << 8 | SingleReadResponse.DATA, "Data");
		list.put(Command.READ_REQUEST << 8 | VariableAccessSpecification.VARIABLE_NAME, "variable-name");
		list.put(Command.READ_REQUEST << 8 | VariableAccessSpecification.PARAMETERISED_ACCESS, "parameterized-access");
		list.put(Command.READ_REQUEST << 8 | VariableAccessSpecification.BLOCK_NUMBER_ACCESS, "BlockNumberAccess");
		list.put(Command.WRITE_REQUEST << 8 | VariableAccessSpecification.VARIABLE_NAME, "variable-name");

		list.put(Command.READ_RESPONSE, "readResponse");
		list.put(Command.READ_RESPONSE << 8 | SingleReadResponse.DATA_BLOCK_RESULT, "DataBlockResult");
		list.put(Command.READ_RESPONSE << 8 | SingleReadResponse.DATA, "data");
		list.put(Command.WRITE_RESPONSE << 8 | SingleReadResponse.DATA, "data");
		list.put(Command.READ_RESPONSE << 8 | SingleReadResponse.DATA_ACCESS_ERROR, "data-access-error");
	}

	/*
	 * Get LN tags.
	 */
	static void getLnTags(final TranslatorOutputType type, final HashMap<Integer, String> list) {
		GXDLMSTranslator.addTag(list, Command.GET_REQUEST, "get-request");
		list.put(Command.GET_REQUEST << 8 | GetCommandType.NORMAL, "get-request-normal");
		list.put(Command.GET_REQUEST << 8 | GetCommandType.NEXT_DATA_BLOCK, "get-request-next");
		list.put(Command.GET_REQUEST << 8 | GetCommandType.WITH_LIST, "get-request-with-list");
		GXDLMSTranslator.addTag(list, Command.SET_REQUEST, "set-request");
		list.put(Command.SET_REQUEST << 8 | SetRequestType.NORMAL, "set-request-normal");
		list.put(Command.SET_REQUEST << 8 | SetRequestType.FIRST_DATA_BLOCK, "set-request-first-data-block");
		list.put(Command.SET_REQUEST << 8 | SetRequestType.WITH_DATA_BLOCK, "set-request-with-datablock");
		list.put(Command.SET_REQUEST << 8 | SetRequestType.WITH_LIST, "set-request-with-list");
		GXDLMSTranslator.addTag(list, Command.METHOD_REQUEST, "action-request");
		list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.NORMAL, "action-request-normal");
		list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.NEXT_BLOCK, "action-request-next-pblock");
		list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.WITH_LIST, "action-request-with-list");
		GXDLMSTranslator.addTag(list, Command.METHOD_RESPONSE, "action-response");
		list.put(Command.METHOD_RESPONSE << 8 | ActionResponseType.NORMAL, "action-response-normal");
		list.put(Command.METHOD_RESPONSE << 8 | ActionResponseType.WITH_FIRST_BLOCK, "action-response-with-pblock");
		list.put(Command.METHOD_RESPONSE << 8 | ActionResponseType.WITH_LIST, "action-response-with-list");
		list.put(TranslatorTags.SINGLE_RESPONSE, "single-response");

		list.put((int) Command.DATA_NOTIFICATION, "data-notification");
		GXDLMSTranslator.addTag(list, Command.GET_RESPONSE, "get-response");
		list.put(Command.GET_RESPONSE << 8 | GetCommandType.NORMAL, "get-response-normal");
		list.put(Command.GET_RESPONSE << 8 | GetCommandType.NEXT_DATA_BLOCK, "get-response-with-data-block");
		list.put(Command.GET_RESPONSE << 8 | GetCommandType.WITH_LIST, "get-response-with-list");
		GXDLMSTranslator.addTag(list, Command.SET_RESPONSE, "set-response");
		list.put(Command.SET_RESPONSE << 8 | SetResponseType.NORMAL, "set-response-normal");
		list.put(Command.SET_RESPONSE << 8 | SetResponseType.DATA_BLOCK, "set-response-datablock");
		list.put(Command.SET_RESPONSE << 8 | SetResponseType.LAST_DATA_BLOCK, "set-response-with-last-data-block");
		list.put(Command.SET_RESPONSE << 8 | SetResponseType.WITH_LIST, "set-response-with-list");

		GXDLMSTranslator.addTag(list, Command.ACCESS_REQUEST, "access-request");
		list.put((Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.GET, "access-request-get");
		list.put((Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.SET, "access-request-set");
		list.put((Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.ACTION, "access-request-action");
		GXDLMSTranslator.addTag(list, Command.ACCESS_RESPONSE, "access-response");
		list.put((Command.ACCESS_RESPONSE) << 8 | AccessServiceCommandType.GET, "access-response-get");
		list.put((Command.ACCESS_RESPONSE) << 8 | AccessServiceCommandType.SET, "access-response-set");
		list.put((Command.ACCESS_RESPONSE) << 8 | AccessServiceCommandType.ACTION, "access-response-action");

		list.put(TranslatorTags.ACCESS_REQUEST_BODY, "access-request-body");
		list.put(TranslatorTags.LIST_OF_ACCESS_REQUEST_SPECIFICATION, "access-request-specification");
		list.put(TranslatorTags.ACCESS_REQUEST_SPECIFICATION, "Access-Request-Specification");
		list.put(TranslatorTags.ACCESS_REQUEST_LIST_OF_DATA, "access-request-list-of-data");

		list.put(TranslatorTags.ACCESS_RESPONSE_BODY, "access-response-body");
		list.put(TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION, "access-response-specification");
		list.put(TranslatorTags.ACCESS_RESPONSE_SPECIFICATION, "Access-Response-Specification");
		list.put(TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA, "access-response-list-of-data");
		list.put(TranslatorTags.SERVICE, "service");
		list.put(TranslatorTags.SERVICE_ERROR, "service-error");
		list.put(Command.GENERAL_BLOCK_TRANSFER, "general-block-transfer");
		GXDLMSTranslator.addTag(list, Command.GATEWAY_REQUEST, "gateway-request");
		GXDLMSTranslator.addTag(list, Command.GATEWAY_RESPONSE, "gateway-response");
	}

	/*
	 * Get PLC tags.
	 */
	static void getPlcTags(final HashMap<Integer, String> list) {
		GXDLMSTranslator.addTag(list, Command.DISCOVER_REQUEST, "discover-request");
		GXDLMSTranslator.addTag(list, Command.DISCOVER_REPORT, "discover-report");
		GXDLMSTranslator.addTag(list, Command.REGISTER_REQUEST, "register-request");
		GXDLMSTranslator.addTag(list, Command.PING_REQUEST, "ping-request");
		GXDLMSTranslator.addTag(list, Command.PING_RESPONSE, "ping-response");
	}

	/*
	 * Get glo tags.
	 */
	static void getGloTags(final TranslatorOutputType type, final HashMap<Integer, String> list) {
		GXDLMSTranslator.addTag(list, Command.GLO_INITIATE_REQUEST, "glo-initiate-request");
		GXDLMSTranslator.addTag(list, Command.GLO_INITIATE_RESPONSE, "glo-initiate-response");
		GXDLMSTranslator.addTag(list, Command.GLO_GET_REQUEST, "glo-get-request");
		GXDLMSTranslator.addTag(list, Command.GLO_GET_RESPONSE, "glo-get-response");
		GXDLMSTranslator.addTag(list, Command.GLO_SET_REQUEST, "glo-set-request");
		GXDLMSTranslator.addTag(list, Command.GLO_SET_RESPONSE, "glo-set-response");
		GXDLMSTranslator.addTag(list, Command.GLO_METHOD_REQUEST, "glo-action-request");
		GXDLMSTranslator.addTag(list, Command.GLO_METHOD_RESPONSE, "glo-action-response");
		GXDLMSTranslator.addTag(list, Command.GLO_READ_REQUEST, "glo-read-request");
		GXDLMSTranslator.addTag(list, Command.GLO_READ_RESPONSE, "glo-read-response");
		GXDLMSTranslator.addTag(list, Command.GLO_WRITE_REQUEST, "glo-write-request");
		GXDLMSTranslator.addTag(list, Command.GLO_WRITE_RESPONSE, "glo-write-response");
		GXDLMSTranslator.addTag(list, Command.GENERAL_GLO_CIPHERING, "general-glo-ciphering");
		GXDLMSTranslator.addTag(list, Command.GENERAL_CIPHERING, "general-ciphering");
	}

	/*
	 * Get ded tags.
	 */
	static void getDedTags(final TranslatorOutputType type, final HashMap<Integer, String> list) {
		GXDLMSTranslator.addTag(list, Command.DED_INITIATE_REQUEST, "ded-initiate-request");
		GXDLMSTranslator.addTag(list, Command.DED_INITIATE_RESPONSE, "ded-initiate-response");
		GXDLMSTranslator.addTag(list, Command.DED_GET_REQUEST, "ded-get-request");
		GXDLMSTranslator.addTag(list, Command.DED_GET_RESPONSE, "ded-get-response");
		GXDLMSTranslator.addTag(list, Command.DED_SET_REQUEST, "ded-set-request");
		GXDLMSTranslator.addTag(list, Command.DED_SET_RESPONSE, "ded-set-response");
		GXDLMSTranslator.addTag(list, Command.DED_METHOD_REQUEST, "ded-action-request");
		GXDLMSTranslator.addTag(list, Command.DED_METHOD_RESPONSE, "ded-action-response");
		GXDLMSTranslator.addTag(list, Command.GENERAL_DED_CIPHERING, "general-ded-ciphering");
	}

	/*
	 * Get translator tags.
	 */
	static void getTranslatorTags(final TranslatorOutputType type, final HashMap<Integer, String> list) {
		GXDLMSTranslator.addTag(list, TranslatorTags.WRAPPER, "Wrapper");
		GXDLMSTranslator.addTag(list, TranslatorTags.HDLC, "Hdlc");
		GXDLMSTranslator.addTag(list, TranslatorTags.PDU_DLMS, "xDLMS-APDU");
		GXDLMSTranslator.addTag(list, TranslatorTags.PDU_CSE, "aCSE-APDU");
		GXDLMSTranslator.addTag(list, TranslatorTags.TARGET_ADDRESS, "TargetAddress");
		GXDLMSTranslator.addTag(list, TranslatorTags.SOURCE_ADDRESS, "SourceAddress");
		GXDLMSTranslator.addTag(list, TranslatorTags.FRAME_TYPE, "FrameType");
		GXDLMSTranslator.addTag(list, TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION,
				"variable-access-specification");
		GXDLMSTranslator.addTag(list, TranslatorTags.LIST_OF_DATA, "list-of-data");
		GXDLMSTranslator.addTag(list, TranslatorTags.SUCCESS, "Success");
		GXDLMSTranslator.addTag(list, TranslatorTags.DATA_ACCESS_ERROR, "data-access-result");
		GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_DESCRIPTOR, "cosem-attribute-descriptor");
		GXDLMSTranslator.addTag(list, TranslatorTags.CLASS_ID, "class-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.INSTANCE_ID, "instance-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_ID, "attribute-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.METHOD_INVOCATION_PARAMETERS, "method-invocation-parameters");
		GXDLMSTranslator.addTag(list, TranslatorTags.SELECTOR, "selector");
		GXDLMSTranslator.addTag(list, TranslatorTags.PARAMETER, "parameter");
		GXDLMSTranslator.addTag(list, TranslatorTags.LAST_BLOCK, "last-block");
		GXDLMSTranslator.addTag(list, TranslatorTags.BLOCK_NUMBER, "block-number");
		GXDLMSTranslator.addTag(list, TranslatorTags.RAW_DATA, "raw-data");
		GXDLMSTranslator.addTag(list, TranslatorTags.METHOD_DESCRIPTOR, "cosem-method-descriptor");
		GXDLMSTranslator.addTag(list, TranslatorTags.METHOD_ID, "method-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.RESULT, "result");
		GXDLMSTranslator.addTag(list, TranslatorTags.P_BLOCK, "pblock");
		GXDLMSTranslator.addTag(list, TranslatorTags.RETURN_PARAMETERS, "return-parameters");
		GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_SELECTION, "access-selection");
		GXDLMSTranslator.addTag(list, TranslatorTags.VALUE, "value");
		GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_SELECTOR, "access-selector");
		GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_PARAMETERS, "access-parameters");
		GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST, "attribute-descriptor-list");
		GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION,
				"Cosem-Attribute-Descriptor-With-Selection");
		GXDLMSTranslator.addTag(list, TranslatorTags.READ_DATA_BLOCK_ACCESS, "ReadDataBlockAccess");
		GXDLMSTranslator.addTag(list, TranslatorTags.WRITE_DATA_BLOCK_ACCESS, "write-data-block-access");
		GXDLMSTranslator.addTag(list, TranslatorTags.DATA, "data");
		GXDLMSTranslator.addTag(list, TranslatorTags.INVOKE_ID, "invoke-id-and-priority");
		GXDLMSTranslator.addTag(list, TranslatorTags.LONG_INVOKE_ID, "long-invoke-id-and-priority");
		GXDLMSTranslator.addTag(list, TranslatorTags.DATE_TIME, "date-time");
		GXDLMSTranslator.addTag(list, TranslatorTags.CURRENT_TIME, "current-time");
		GXDLMSTranslator.addTag(list, TranslatorTags.TIME, "time");
		GXDLMSTranslator.addTag(list, TranslatorTags.REASON, "reason");
		GXDLMSTranslator.addTag(list, TranslatorTags.VARIABLE_ACCESS_SPECIFICATION, "Variable-Access-Specification");
		GXDLMSTranslator.addTag(list, TranslatorTags.CHOICE, "CHOICE");
		GXDLMSTranslator.addTag(list, TranslatorTags.NOTIFICATION_BODY, "notification-body");
		GXDLMSTranslator.addTag(list, TranslatorTags.DATA_VALUE, "data-value");
		GXDLMSTranslator.addTag(list, TranslatorTags.INITIATE_ERROR, "initiateError");
		GXDLMSTranslator.addTag(list, TranslatorTags.CIPHERED_SERVICE, "ciphered-content");
		GXDLMSTranslator.addTag(list, TranslatorTags.SYSTEM_TITLE, "system-title");
		GXDLMSTranslator.addTag(list, TranslatorTags.DATA_BLOCK, "datablock");
		GXDLMSTranslator.addTag(list, TranslatorTags.TRANSACTION_ID, "transaction-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.ORIGINATOR_SYSTEM_TITLE, "originator-system-title");
		GXDLMSTranslator.addTag(list, TranslatorTags.RECIPIENT_SYSTEM_TITLE, "recipient-system-title");
		GXDLMSTranslator.addTag(list, TranslatorTags.OTHER_INFORMATION, "other-information");
		GXDLMSTranslator.addTag(list, TranslatorTags.KEY_INFO, "key-info");
		GXDLMSTranslator.addTag(list, TranslatorTags.CIPHERED_CONTENT, "ciphered-content");
		GXDLMSTranslator.addTag(list, TranslatorTags.AGREED_KEY, "agreed-key");
		GXDLMSTranslator.addTag(list, TranslatorTags.KEY_PARAMETERS, "key-parameters");
		GXDLMSTranslator.addTag(list, TranslatorTags.KEY_CIPHERED_DATA, "key-ciphered-data");
		GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_VALUE, "attribute-value");
		GXDLMSTranslator.addTag(list, TranslatorTags.MAX_INFO_RX, "MaxInfoRX");
		GXDLMSTranslator.addTag(list, TranslatorTags.MAX_INFO_TX, "MaxInfoTX");
		GXDLMSTranslator.addTag(list, TranslatorTags.WINDOW_SIZE_RX, "WindowSizeRX");
		GXDLMSTranslator.addTag(list, TranslatorTags.WINDOW_SIZE_TX, "WindowSizeTX");
		GXDLMSTranslator.addTag(list, TranslatorTags.VALUE_LIST, "value-list");
		GXDLMSTranslator.addTag(list, TranslatorTags.DATA_ACCESS_RESULT, "data-access-result");
		GXDLMSTranslator.addTag(list, TranslatorTags.BLOCK_CONTROL, "block-control");
		GXDLMSTranslator.addTag(list, TranslatorTags.BLOCK_NUMBER_ACK, "block-number-ack");
		GXDLMSTranslator.addTag(list, TranslatorTags.BLOCK_DATA, "block-data");
		GXDLMSTranslator.addTag(list, TranslatorTags.CONTENTS_DESCRIPTION, "contents-description");
		GXDLMSTranslator.addTag(list, TranslatorTags.ARRAY_CONTENTS, "array-contents");
		GXDLMSTranslator.addTag(list, TranslatorTags.NETWORK_ID, "network-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.PHYSICAL_DEVICE_ADDRESS, "physical-device-address");
		GXDLMSTranslator.addTag(list, TranslatorTags.PROTOCOL_VERSION, "protocol-version");
		GXDLMSTranslator.addTag(list, TranslatorTags.CALLED_AP_TITLE, "called-ap-title");
		GXDLMSTranslator.addTag(list, TranslatorTags.CALLED_AP_INVOCATION_ID, "called-ap-invocation-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.CALLED_AE_INVOCATION_ID, "called-ae-invocation-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.CALLING_AP_INVOCATION_ID, "calling-ap-invocation-id");
		GXDLMSTranslator.addTag(list, TranslatorTags.CALLED_AE_QUALIFIER, "called-ae-qualifier");
	}

	static void getDataTypeTags(final HashMap<Integer, String> list) {
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.NONE.getValue(), "null-data");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.ARRAY.getValue(), "array");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BCD.getValue(), "bcd");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BITSTRING.getValue(), "bit-string");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BOOLEAN.getValue(), "boolean");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.COMPACT_ARRAY.getValue(), "compact-array");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.DATE.getValue(), "date");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.DATETIME.getValue(), "date-time");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.ENUM.getValue(), "enum");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.FLOAT32.getValue(), "float32");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.FLOAT64.getValue(), "float64,");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT16.getValue(), "long");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT32.getValue(), "double-long");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT64.getValue(), "long64");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT8.getValue(), "integer");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.OCTET_STRING.getValue(), "octet-string");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRING.getValue(), "visible-string");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRING_UTF8.getValue(), "utf8-string");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue(), "structure");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.TIME.getValue(), "time");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT16.getValue(), "long-unsigned");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT32.getValue(), "double-long-unsigned");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT64.getValue(), "long64-unsigned");
		list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT8.getValue(), "unsigned");
	}

	static String errorCodeToString(final ErrorCode value) {
		String str;
		switch (value) {
		case ACCESS_VIOLATED:
			str = "scope-of-access-violated";
			break;
		case DATA_BLOCK_NUMBER_INVALID:
			str = "data-block-number-invalid";
			break;
		case DATA_BLOCK_UNAVAILABLE:
			str = "data-block-unavailable";
			break;
		case HARDWARE_FAULT:
			str = "hardware-fault";
			break;
		case INCONSISTENT_CLASS:
			str = "object-class-inconsistent";
			break;
		case LONG_GET_OR_READ_ABORTED:
			str = "long-get-aborted";
			break;
		case LONG_SET_OR_WRITE_ABORTED:
			str = "long-set-aborted";
			break;
		case NO_LONG_GET_OR_READ_IN_PROGRESS:
			str = "no-long-get-in-progress";
			break;
		case NO_LONG_SET_OR_WRITE_IN_PROGRESS:
			str = "no-long-set-in-progress";
			break;
		case OK:
			str = "success";
			break;
		case OTHER_REASON:
			str = "other-reason";
			break;
		case READ_WRITE_DENIED:
			str = "read-write-denied";
			break;
		case TEMPORARY_FAILURE:
			str = "temporary-failure";
			break;
		case UNAVAILABLE_OBJECT:
			str = "object-unavailable";
			break;
		case UNDEFINED_OBJECT:
			str = "object-undefined";
			break;
		case UNMATCHED_TYPE:
			str = "type-unmatched";
			break;
		default:
			throw new IllegalArgumentException("Error code: " + String.valueOf(value));
		}
		return str;
	}

	static ErrorCode valueOfErrorCode(final String value) {
		ErrorCode v;
		if ("scope-of-access-violated".equalsIgnoreCase(value)) {
			v = ErrorCode.ACCESS_VIOLATED;
		} else if ("data-block-number-invalid".equalsIgnoreCase(value)) {
			v = ErrorCode.DATA_BLOCK_NUMBER_INVALID;
		} else if ("data-block-unavailable".equalsIgnoreCase(value)) {
			v = ErrorCode.DATA_BLOCK_UNAVAILABLE;
		} else if ("hardware-fault".equalsIgnoreCase(value)) {
			v = ErrorCode.HARDWARE_FAULT;
		} else if ("object-class-inconsistent".equalsIgnoreCase(value)) {
			v = ErrorCode.INCONSISTENT_CLASS;
		} else if ("long-get-aborted".equalsIgnoreCase(value)) {
			v = ErrorCode.LONG_GET_OR_READ_ABORTED;
		} else if ("long-set-aborted".equalsIgnoreCase(value)) {
			v = ErrorCode.LONG_SET_OR_WRITE_ABORTED;
		} else if ("no-long-get-in-progress".equalsIgnoreCase(value)) {
			v = ErrorCode.NO_LONG_GET_OR_READ_IN_PROGRESS;
		} else if ("no-long-set-in-progress".equalsIgnoreCase(value)) {
			v = ErrorCode.NO_LONG_SET_OR_WRITE_IN_PROGRESS;
		} else if ("success".equalsIgnoreCase(value)) {
			v = ErrorCode.OK;
		} else if ("other-reason".equalsIgnoreCase(value)) {
			v = ErrorCode.OTHER_REASON;
		} else if ("read-write-denied".equalsIgnoreCase(value)) {
			v = ErrorCode.READ_WRITE_DENIED;
		} else if ("temporary-failure".equalsIgnoreCase(value)) {
			v = ErrorCode.TEMPORARY_FAILURE;
		} else if ("object-unavailable".equalsIgnoreCase(value)) {
			v = ErrorCode.UNAVAILABLE_OBJECT;
		} else if ("object-undefined".equalsIgnoreCase(value)) {
			v = ErrorCode.UNDEFINED_OBJECT;
		} else if ("type-unmatched".equalsIgnoreCase(value)) {
			v = ErrorCode.UNMATCHED_TYPE;
		} else {
			throw new IllegalArgumentException("Error code: " + value);
		}
		return v;
	}

	private static Map<ServiceError, String> getServiceErrors() {
		Map<ServiceError, String> list = new HashMap<ServiceError, String>();
		list.put(ServiceError.APPLICATION_REFERENCE, "application-reference");
		list.put(ServiceError.HARDWARE_RESOURCE, "hardware-resource");
		list.put(ServiceError.VDE_STATE_ERROR, "vde-state-error");
		list.put(ServiceError.SERVICE, "service");
		list.put(ServiceError.DEFINITION, "definition");
		list.put(ServiceError.ACCESS, "access");
		list.put(ServiceError.INITIATE, "initiate");
		list.put(ServiceError.LOAD_DATASET, "load-data-set");
		list.put(ServiceError.TASK, "task");
		return list;
	}

	static Map<ApplicationReference, String> getApplicationReference() {
		Map<ApplicationReference, String> list = new HashMap<ApplicationReference, String>();
		list.put(ApplicationReference.APPLICATION_CONTEXT_UNSUPPORTED, "application-context-unsupported");
		list.put(ApplicationReference.APPLICATION_REFERENCE_INVALID, "application-reference-invalid");
		list.put(ApplicationReference.APPLICATION_UNREACHABLE, "application-unreachable");
		list.put(ApplicationReference.DECIPHERING_ERROR, "deciphering-error");
		list.put(ApplicationReference.OTHER, "other");
		list.put(ApplicationReference.PROVIDER_COMMUNICATION_ERROR, "provider-communication-error");
		list.put(ApplicationReference.TIME_ELAPSED, "time-elapsed");
		return list;
	}

	static Map<HardwareResource, String> getHardwareResource() {
		Map<HardwareResource, String> list = new HashMap<HardwareResource, String>();
		list.put(HardwareResource.MASS_STORAGE_UNAVAILABLE, "mass-storage-unavailable");
		list.put(HardwareResource.MEMORY_UNAVAILABLE, "memory-unavailable");
		list.put(HardwareResource.OTHER, "other");
		list.put(HardwareResource.OTHER_RESOURCE_UNAVAILABLE, "other-resource-unavailable");
		list.put(HardwareResource.PROCESSOR_RESOURCE_UNAVAILABLE, "processor-resource-unavailable");
		return list;
	}

	static Map<VdeStateError, String> getVdeStateError() {
		Map<VdeStateError, String> list = new HashMap<VdeStateError, String>();
		list.put(VdeStateError.LOADING_DATASET, "loading-data-set");
		list.put(VdeStateError.NO_DLMS_CONTEXT, "no-dlms-context");
		list.put(VdeStateError.OTHER, "other");
		list.put(VdeStateError.STATUS_INOPERABLE, "status-inoperable");
		list.put(VdeStateError.STATUS_NO_CHANGE, "status-nochange");
		return list;
	}

	static Map<Service, String> getService() {
		Map<Service, String> list = new HashMap<Service, String>();
		list.put(Service.OTHER, "other");
		list.put(Service.PDU_SIZE, "pdu-size");
		list.put(Service.UNSUPPORTED, "service-unsupported");
		return list;
	}

	static Map<Definition, String> getDefinition() {
		Map<Definition, String> list = new HashMap<Definition, String>();
		list.put(Definition.OBJECT_ATTRIBUTE_INCONSISTENT, "object-attribute-inconsistent");
		list.put(Definition.OBJECT_CLASS_INCONSISTENT, "object-class-inconsistent");
		list.put(Definition.OBJECT_UNDEFINED, "object-undefined");
		list.put(Definition.OTHER, "other");
		return list;
	}

	static Map<Access, String> getAccess() {
		Map<Access, String> list = new HashMap<Access, String>();
		list.put(Access.HARDWARE_FAULT, "hardware-fault");
		list.put(Access.OBJECT_ACCESS_INVALID, "object-access-violated");
		list.put(Access.OBJECT_UNAVAILABLE, "object-unavailable");
		list.put(Access.OTHER, "other");
		list.put(Access.SCOPE_OF_ACCESS_VIOLATED, "scope-of-access-violated");
		return list;
	}

	static Map<Initiate, String> getInitiate() {
		Map<Initiate, String> list = new HashMap<Initiate, String>();
		list.put(Initiate.DLMS_VERSION_TOO_LOW, "dlms-version-too-low");
		list.put(Initiate.INCOMPATIBLE_CONFORMANCE, "incompatible-conformance");
		list.put(Initiate.OTHER, "other");
		list.put(Initiate.PDU_SIZE_TOO_SHORT, "pdu-size-too-short");
		list.put(Initiate.REFUSED_BY_THE_VDE_HANDLER, "refused-by-the-VDE-Handler");
		return list;
	}

	static Map<LoadDataSet, String> getLoadDataSet() {
		Map<LoadDataSet, String> list = new HashMap<LoadDataSet, String>();
		list.put(LoadDataSet.DATASET_NOT_READY, "data-set-not-ready");
		list.put(LoadDataSet.DATASET_SIZE_TOO_LARGE, "dataset-size-too-large");
		list.put(LoadDataSet.INTERPRETATION_FAILURE, "interpretation-failure");
		list.put(LoadDataSet.NOT_AWAITED_SEGMENT, "not-awaited-segment");
		list.put(LoadDataSet.NOT_LOADABLE, "not-loadable");
		list.put(LoadDataSet.OTHER, "other");
		list.put(LoadDataSet.PRIMITIVE_OUT_OF_SEQUENCE, "primitive-out-of-sequence");
		list.put(LoadDataSet.STORAGE_FAILURE, "storage-failure");
		return list;
	}

	static Map<Task, String> getTask() {
		Map<Task, String> list = new HashMap<Task, String>();
		list.put(Task.NO_REMOTE_CONTROL, "no-remote-control");
		list.put(Task.OTHER, "other");
		list.put(Task.TI_RUNNING, "ti-running");
		list.put(Task.TI_STOPPED, "ti-stopped");
		list.put(Task.TI_UNUSABLE, "ti-unusable");
		return list;
	}

	static String getServiceErrorValue(final ServiceError error, final byte value) {
		switch (error) {
		case APPLICATION_REFERENCE:
			return getApplicationReference().get(ApplicationReference.forValue(value));
		case HARDWARE_RESOURCE:
			return getHardwareResource().get(HardwareResource.forValue(value));
		case VDE_STATE_ERROR:
			return getVdeStateError().get(VdeStateError.forValue(value));
		case SERVICE:
			return getService().get(Service.forValue(value));
		case DEFINITION:
			return getDefinition().get(Definition.forValue(value));
		case ACCESS:
			return getAccess().get(Access.forValue(value));
		case INITIATE:
			return getInitiate().get(Initiate.forValue(value));
		case LOAD_DATASET:
			return getLoadDataSet().get(LoadDataSet.forValue(value));
		case TASK:
			return getTask().get(Task.forValue(value));
		case OTHER_ERROR:
			return String.valueOf(value);
		default:
			break;
		}
		return "";
	}

	/**
	 * @param error Service error enumeration value.
	 * @return Service error standard XML tag.
	 */
	static String serviceErrorToString(final ServiceError error) {
		return getServiceErrors().get(error);
	}

	/**
	 * @param value Service error standard XML tag.
	 * @return Service error enumeration value.
	 */
	static ServiceError getServiceError(final String value) {
		ServiceError error = null;
		for (Entry<ServiceError, String> it : getServiceErrors().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				error = it.getKey();
				break;
			}
		}
		if (error == null) {
			throw new IllegalArgumentException();
		}
		return error;
	}

	private static int getApplicationReference(final String value) {
		int ret = -1;
		for (Entry<ApplicationReference, String> it : getApplicationReference().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getHardwareResource(final String value) {
		int ret = -1;
		for (Entry<HardwareResource, String> it : getHardwareResource().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getVdeStateError(final String value) {
		int ret = -1;
		for (Entry<VdeStateError, String> it : getVdeStateError().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getService(final String value) {
		int ret = -1;
		for (Entry<Service, String> it : getService().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getDefinition(final String value) {
		int ret = -1;
		for (Entry<Definition, String> it : getDefinition().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getAccess(final String value) {
		int ret = -1;
		for (Entry<Access, String> it : getAccess().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getInitiate(final String value) {
		int ret = -1;
		for (Entry<Initiate, String> it : getInitiate().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getLoadDataSet(final String value) {
		int ret = -1;
		for (Entry<LoadDataSet, String> it : getLoadDataSet().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	private static int getTask(final String value) {
		int ret = -1;
		for (Entry<Task, String> it : getTask().entrySet()) {
			if (value.compareTo(it.getValue()) == 0) {
				ret = it.getKey().getValue();
				break;
			}
		}
		if (ret == -1) {
			throw new IllegalArgumentException();
		}
		return ret;
	}

	static byte getError(final ServiceError serviceError, final String value) {
		int ret = 0;
		switch (serviceError) {
		case APPLICATION_REFERENCE:
			ret = getApplicationReference(value);
			break;
		case HARDWARE_RESOURCE:
			ret = getHardwareResource(value);
			break;
		case VDE_STATE_ERROR:
			ret = getVdeStateError(value);
			break;
		case SERVICE:
			ret = getService(value);
			break;
		case DEFINITION:
			ret = getDefinition(value);
			break;
		case ACCESS:
			ret = getAccess(value);
			break;
		case INITIATE:
			ret = getInitiate(value);
			break;
		case LOAD_DATASET:
			ret = getLoadDataSet(value);
			break;
		case TASK:
			ret = getTask(value);
			break;
		case OTHER_ERROR:
			ret = Integer.parseInt(value);
			break;
		default:
			break;
		}
		return (byte) ret;
	}

	static String conformancetoString(final Conformance value) {
		String str;
		switch (value) {
		case ACCESS:
			str = "access";
			break;
		case ACTION:
			str = "action";
			break;
		case ATTRIBUTE_0_SUPPORTED_WITH_GET:
			str = "attribute0-supported-with-get";
			break;
		case ATTRIBUTE_0_SUPPORTED_WITH_SET:
			str = "attribute0-supported-with-set";
			break;
		case BLOCK_TRANSFER_WITH_ACTION:
			str = "block-transfer-with-action";
			break;
		case BLOCK_TRANSFER_WITH_GET_OR_READ:
			str = "block-transfer-with-get-or-read";
			break;
		case BLOCK_TRANSFER_WITH_SET_OR_WRITE:
			str = "block-transfer-with-set-or-write";
			break;
		case DATA_NOTIFICATION:
			str = "data-notification";
			break;
		case EVENT_NOTIFICATION:
			str = "event-notification";
			break;
		case GENERAL_BLOCK_TRANSFER:
			str = "general-block-transfer";
			break;
		case GENERAL_PROTECTION:
			str = "general-protection";
			break;
		case GET:
			str = "get";
			break;
		case INFORMATION_REPORT:
			str = "information-report";
			break;
		case MULTIPLE_REFERENCES:
			str = "multiple-references";
			break;
		case PARAMETERIZED_ACCESS:
			str = "parameterized-access";
			break;
		case PRIORITY_MGMT_SUPPORTED:
			str = "priority-mgmt-supported";
			break;
		case READ:
			str = "read";
			break;
		case RESERVED_SEVEN:
			str = "reserved-seven";
			break;
		case DELTA_VALUE_ENCODING:
			str = "delta-value-encoding";
			break;
		case RESERVED_ZERO:
			str = "reserved-zero";
			break;
		case SELECTIVE_ACCESS:
			str = "selective-access";
			break;
		case SET:
			str = "set";
			break;
		case UN_CONFIRMED_WRITE:
			str = "unconfirmed-write";
			break;
		case WRITE:
			str = "write";
			break;
		default:
			throw new IllegalArgumentException(String.valueOf(value));
		}
		return str;
	}

	static Conformance valueOfConformance(final String value) {
		Conformance ret;
		if ("access".equalsIgnoreCase(value)) {
			ret = Conformance.ACCESS;
		} else if ("action".equalsIgnoreCase(value)) {
			ret = Conformance.ACTION;
		} else if ("attribute0-supported-with-get".equalsIgnoreCase(value)) {
			ret = Conformance.ATTRIBUTE_0_SUPPORTED_WITH_GET;
		} else if ("attribute0-supported-with-set".equalsIgnoreCase(value)) {
			ret = Conformance.ATTRIBUTE_0_SUPPORTED_WITH_SET;
		} else if ("block-transfer-with-action".equalsIgnoreCase(value)) {
			ret = Conformance.BLOCK_TRANSFER_WITH_ACTION;
		} else if ("block-transfer-with-get-or-read".equalsIgnoreCase(value)) {
			ret = Conformance.BLOCK_TRANSFER_WITH_GET_OR_READ;
		} else if ("block-transfer-with-set-or-write".equalsIgnoreCase(value)) {
			ret = Conformance.BLOCK_TRANSFER_WITH_SET_OR_WRITE;
		} else if ("data-notification".equalsIgnoreCase(value)) {
			ret = Conformance.DATA_NOTIFICATION;
		} else if ("event-notification".equalsIgnoreCase(value)) {
			ret = Conformance.EVENT_NOTIFICATION;
		} else if ("general-block-transfer".equalsIgnoreCase(value)) {
			ret = Conformance.GENERAL_BLOCK_TRANSFER;
		} else if ("general-protection".equalsIgnoreCase(value)) {
			ret = Conformance.GENERAL_PROTECTION;
		} else if ("get".equalsIgnoreCase(value)) {
			ret = Conformance.GET;
		} else if ("information-report".equalsIgnoreCase(value)) {
			ret = Conformance.INFORMATION_REPORT;
		} else if ("multiple-references".equalsIgnoreCase(value)) {
			ret = Conformance.MULTIPLE_REFERENCES;
		} else if ("parameterized-access".equalsIgnoreCase(value)) {
			ret = Conformance.PARAMETERIZED_ACCESS;
		} else if ("priority-mgmt-supported".equalsIgnoreCase(value)) {
			ret = Conformance.PRIORITY_MGMT_SUPPORTED;
		} else if ("read".equalsIgnoreCase(value)) {
			ret = Conformance.READ;
		} else if ("reserved-seven".equalsIgnoreCase(value)) {
			ret = Conformance.RESERVED_SEVEN;
		} else if ("delta-value-encoding".equalsIgnoreCase(value)) {
			ret = Conformance.DELTA_VALUE_ENCODING;
		} else if ("reserved-zero".equalsIgnoreCase(value)) {
			ret = Conformance.RESERVED_ZERO;
		} else if ("selective-access".equalsIgnoreCase(value)) {
			ret = Conformance.SELECTIVE_ACCESS;
		} else if ("set".equalsIgnoreCase(value)) {
			ret = Conformance.SET;
		} else if ("unconfirmed-write".equalsIgnoreCase(value)) {
			ret = Conformance.UN_CONFIRMED_WRITE;
		} else if ("write".equalsIgnoreCase(value)) {
			ret = Conformance.WRITE;
		} else {
			throw new IllegalArgumentException(value);
		}
		return ret;
	}

	static String releaseResponseReasonToString(final ReleaseResponseReason value) {
		String str;
		switch (value) {
		case NORMAL:
			str = "normal";
			break;
		case NOT_FINISHED:
			str = "not-finished";
			break;
		case USER_DEFINED:
			str = "user-defined";
			break;
		default:
			throw new IllegalArgumentException(String.valueOf(value));
		}
		return str;
	}

	static ReleaseResponseReason valueOfReleaseResponseReason(final String value) {
		ReleaseResponseReason ret;
		if ("normal".equalsIgnoreCase(value)) {
			ret = ReleaseResponseReason.NORMAL;
		} else if ("not-finished".equalsIgnoreCase(value)) {
			ret = ReleaseResponseReason.NOT_FINISHED;
		} else if ("user-defined".equalsIgnoreCase(value)) {
			ret = ReleaseResponseReason.USER_DEFINED;
		} else {
			throw new IllegalArgumentException(value);
		}
		return ret;
	}

	static String releaseRequestReasonToString(final ReleaseRequestReason value) {
		String str;
		switch (value) {
		case NORMAL:
			str = "normal";
			break;
		case URGENT:
			str = "not-finished";
			break;
		case USER_DEFINED:
			str = "user-defined";
			break;
		default:
			throw new IllegalArgumentException(String.valueOf(value));
		}
		return str;
	}

	static ReleaseRequestReason valueOfReleaseRequestReason(final String value) {
		ReleaseRequestReason ret;
		if ("normal".equalsIgnoreCase(value)) {
			ret = ReleaseRequestReason.NORMAL;
		} else if ("not-finished".equalsIgnoreCase(value)) {
			ret = ReleaseRequestReason.URGENT;
		} else if ("user-defined".equalsIgnoreCase(value)) {
			ret = ReleaseRequestReason.USER_DEFINED;
		} else {
			throw new IllegalArgumentException(value);
		}
		return ret;
	}

	/**
	 * Gets state error description.
	 * 
	 * @param error State error enumerator value.
	 * @return State error as an string.
	 */
	static String stateErrorToString(final ExceptionStateError error) {
		switch (error) {
		case SERVICE_NOT_ALLOWED:
			return "service-not-allowed";
		case SERVICE_UNKNOWN:
			return "service-unknown";
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Gets service error description.
	 * 
	 * @param error Service error enumerator value.
	 * @return Service error as an string.
	 */
	static String exceptionServiceErrorToString(final ExceptionServiceError error) {
		switch (error) {
		case OPERATION_NOT_POSSIBLE:
			return "operation-not-possible";
		case SERVICE_NOT_SUPPORTED:
			return "service-not-supported";
		case OTHER_REASON:
			return "other-reason";
		case PDU_TOO_LONG:
			return "pdu-too-long";
		case DECIPHERING_ERROR:
			return "deciphering-error";
		case INVOCATION_COUNTER_ERROR:
			return "invocation-counter-error";
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @param value State error string value.
	 * @return State error enum value.
	 */
	static ExceptionStateError valueofStateError(final String value) {
		if ("service-not-allowed".equalsIgnoreCase(value)) {
			return ExceptionStateError.SERVICE_NOT_ALLOWED;
		}
		if ("service-unknown".equalsIgnoreCase(value)) {
			return ExceptionStateError.SERVICE_UNKNOWN;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @param value Service error string value.
	 * @return Service error enum value.
	 */
	static ExceptionServiceError valueOfExceptionServiceError(final String value) {
		if ("operation-not-possible".equalsIgnoreCase(value)) {
			return ExceptionServiceError.OPERATION_NOT_POSSIBLE;
		}
		if ("service-not-supported".equalsIgnoreCase(value)) {
			return ExceptionServiceError.SERVICE_NOT_SUPPORTED;
		}
		if ("other-reason".equalsIgnoreCase(value)) {
			return ExceptionServiceError.OTHER_REASON;
		}
		if ("pdu-too-long".equalsIgnoreCase(value)) {
			return ExceptionServiceError.PDU_TOO_LONG;
		}
		if ("deciphering-error".equalsIgnoreCase(value)) {
			return ExceptionServiceError.DECIPHERING_ERROR;
		}
		if ("invocation-counter-error".equalsIgnoreCase(value)) {
			return ExceptionServiceError.INVOCATION_COUNTER_ERROR;
		}
		throw new IllegalArgumentException();
	}
}
