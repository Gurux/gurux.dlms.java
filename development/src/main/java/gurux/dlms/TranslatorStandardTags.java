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

    /**
     * Get general tags.
     * 
     * @param type
     * @param list
     */
    static void getGeneralTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list) {
        GXDLMSTranslator.addTag(list, Command.SNRM, "Snrm");
        GXDLMSTranslator.addTag(list, Command.UNACCEPTABLE_FRAME,
                "UnacceptableFrame");
        GXDLMSTranslator.addTag(list, Command.DISCONNECT_MODE,
                "DisconnectMode");
        GXDLMSTranslator.addTag(list, Command.UA, "Ua");
        GXDLMSTranslator.addTag(list, Command.AARQ, "x:aarq");
        GXDLMSTranslator.addTag(list, Command.AARE, "x:aare");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.APPLICATION_CONTEXT_NAME,
                "x:application-context-name");
        GXDLMSTranslator.addTag(list, Command.INITIATE_RESPONSE,
                "x:initiateResponse");
        GXDLMSTranslator.addTag(list, Command.INITIATE_REQUEST,
                "x:initiateRequest");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_QUALITY_OF_SERVICE,
                "x:negotiated-quality-of-service");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_QUALITY_OF_SERVICE,
                "x:proposed-quality-of-service");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_DLMS_VERSION_NUMBER,
                "x:proposed-dlms-version-number");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_MAX_PDU_SIZE,
                "x:client-max-receive-pdu-size");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_CONFORMANCE,
                "x:proposed-conformance");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.VAA_NAME,
                "x:vaa-name");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_CONFORMANCE,
                "x:negotiated-conformance");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_DLMS_VERSION_NUMBER,
                "x:negotiated-dlms-version-number");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_MAX_PDU_SIZE,
                "x:server-max-receive-pdu-size");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CONFORMANCE_BIT,
                "ConformanceBit");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.SENDER_ACSE_REQUIREMENTS,
                "x:sender-acse-requirements");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESPONDER_ACSE_REQUIREMENT,
                "x:responder-acse-requirements");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESPONDING_MECHANISM_NAME,
                "x:mechanism-name");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.CALLING_MECHANISM_NAME,
                "x:mechanism-name");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.CALLING_AUTHENTICATION,
                "x:calling-authentication-value");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESPONDING_AUTHENTICATION,
                "x:responding-authentication-value");
        GXDLMSTranslator.addTag(list, Command.RELEASE_REQUEST, "x:rlrq");
        GXDLMSTranslator.addTag(list, Command.RELEASE_RESPONSE, "x:rlre");
        GXDLMSTranslator.addTag(list, Command.DISCONNECT_REQUEST, "Disc");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.ASSOCIATION_RESULT,
                "x:result");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESULT_SOURCE_DIAGNOSTIC,
                "x:result-source-diagnostic");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.ACSE_SERVICE_USER,
                "x:acse-service-user");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CALLING_AP_TITLE,
                "CallingAPTitle");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONDING_AP_TITLE,
                "RespondingAPTitle");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CHAR_STRING,
                "x:charstring");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.DEDICATED_KEY,
                "x:dedicated-key");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONSE_ALLOWED,
                "x:response-allowed");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.USER_INFORMATION,
                "x:user-information");
        GXDLMSTranslator.addTag(list, Command.CONFIRMED_SERVICE_ERROR,
                "x:confirmedServiceError");
        GXDLMSTranslator.addTag(list, Command.INFORMATION_REPORT,
                "x:informationReportRequest");
        GXDLMSTranslator.addTag(list, Command.EVENT_NOTIFICATION,
                "x:event-notification-request");
    }

    /**
     * Get SN tags.
     * 
     * @param type
     * @param list
     */
    static void getSnTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list) {
        list.put(Command.READ_REQUEST, "x:readRequest");
        list.put(Command.WRITE_REQUEST, "x:writeRequest");
        list.put(Command.WRITE_RESPONSE, "x:writeResponse");
        list.put(Command.WRITE_REQUEST << 8 | SingleReadResponse.DATA,
                "x:Data");
        list.put(
                Command.READ_REQUEST << 8
                        | VariableAccessSpecification.VARIABLE_NAME,
                "x:variable-name");
        list.put(
                Command.READ_REQUEST << 8
                        | VariableAccessSpecification.PARAMETERISED_ACCESS,
                "x:parameterized-access");
        list.put(
                Command.READ_REQUEST << 8
                        | VariableAccessSpecification.BLOCK_NUMBER_ACCESS,
                "BlockNumberAccess");
        list.put(
                Command.WRITE_REQUEST << 8
                        | VariableAccessSpecification.VARIABLE_NAME,
                "x:variable-name");

        list.put(Command.READ_RESPONSE, "x:readResponse");
        list.put(
                Command.READ_RESPONSE << 8
                        | SingleReadResponse.DATA_BLOCK_RESULT,
                "DataBlockResult");
        list.put(Command.READ_RESPONSE << 8 | SingleReadResponse.DATA,
                "x:data");
        list.put(Command.WRITE_RESPONSE << 8 | SingleReadResponse.DATA,
                "x:data");
        list.put(
                Command.READ_RESPONSE << 8
                        | SingleReadResponse.DATA_ACCESS_ERROR,
                "x:data-access-error");
    }

    /**
     * Get LN tags.
     * 
     * @param type
     * @param list
     */
    static void getLnTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list) {
        GXDLMSTranslator.addTag(list, Command.GET_REQUEST, "x:get-request");
        list.put(Command.GET_REQUEST << 8 | GetCommandType.NORMAL,
                "x:get-request-normal");
        list.put(Command.GET_REQUEST << 8 | GetCommandType.NEXT_DATA_BLOCK,
                "x:get-request-next");
        list.put(Command.GET_REQUEST << 8 | GetCommandType.WITH_LIST,
                "x:get-request-with-list");
        GXDLMSTranslator.addTag(list, Command.SET_REQUEST, "x:set-request");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.NORMAL,
                "x:set-request-normal");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.FIRST_DATA_BLOCK,
                "x:set-request-first-data-block");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.WITH_DATA_BLOCK,
                "x:set-request-with-datablock");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.WITH_LIST,
                "x:set-request-with-list");
        GXDLMSTranslator.addTag(list, Command.METHOD_REQUEST,
                "x:action-request");
        list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.NORMAL,
                "x:action-request-normal");
        list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.NEXT_BLOCK,
                "ActionRequestForNextDataBlock");
        list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.WITH_LIST,
                "x:action-request-with-list");
        GXDLMSTranslator.addTag(list, Command.METHOD_RESPONSE,
                "x:action-response");
        list.put(Command.METHOD_RESPONSE << 8 | ActionResponseType.NORMAL,
                "x:action-response-normal");
        list.put(
                Command.METHOD_RESPONSE << 8
                        | ActionResponseType.WITH_FIRST_BLOCK,
                "x:action-response-with-first-block");
        list.put(Command.METHOD_RESPONSE << 8 | ActionResponseType.WITH_LIST,
                "x:action-response-with-list");
        list.put(TranslatorTags.SINGLE_RESPONSE, "x:single-response");

        list.put((int) Command.DATA_NOTIFICATION, "x:data-notification");
        GXDLMSTranslator.addTag(list, Command.GET_RESPONSE, "x:get-response");
        list.put(Command.GET_RESPONSE << 8 | GetCommandType.NORMAL,
                "x:get-response-normal");
        list.put(Command.GET_RESPONSE << 8 | GetCommandType.NEXT_DATA_BLOCK,
                "x:get-response-with-data-block");
        list.put(Command.GET_RESPONSE << 8 | GetCommandType.WITH_LIST,
                "x:get-response-with-list");
        GXDLMSTranslator.addTag(list, Command.SET_RESPONSE, "x:set-response");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.NORMAL,
                "x:set-response-normal");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.DATA_BLOCK,
                "x:set-response-data-block");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.LAST_DATA_BLOCK,
                "x:set-response-with-last-data-block");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.WITH_LIST,
                "x:set-response-with-list");

        GXDLMSTranslator.addTag(list, Command.ACCESS_REQUEST,
                "x:access-request");
        list.put((Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.GET,
                "x:access-request-get");
        list.put((Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.SET,
                "x:access-request-set");
        list.put(
                (Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.ACTION,
                "x:access-request-action");
        GXDLMSTranslator.addTag(list, Command.ACCESS_RESPONSE,
                "x:access-response");
        list.put((Command.ACCESS_RESPONSE) << 8 | AccessServiceCommandType.GET,
                "x:access-response-get");
        list.put((Command.ACCESS_RESPONSE) << 8 | AccessServiceCommandType.SET,
                "x:access-response-set");
        list.put(
                (Command.ACCESS_RESPONSE) << 8
                        | AccessServiceCommandType.ACTION,
                "x:access-response-action");

        list.put(TranslatorTags.ACCESS_REQUEST_BODY, "x:access-request-body");
        list.put(TranslatorTags.LIST_OF_ACCESS_REQUEST_SPECIFICATION,
                "x:access-request-specification");
        list.put(TranslatorTags.ACCESS_REQUEST_SPECIFICATION,
                "x:Access-Request-Specification");
        list.put(TranslatorTags.ACCESS_REQUEST_LIST_OF_DATA,
                "x:access-request-list-of-data");

        list.put(TranslatorTags.ACCESS_RESPONSE_BODY, "x:access-response-body");
        list.put(TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION,
                "x:access-response-specification");
        list.put(TranslatorTags.ACCESS_RESPONSE_SPECIFICATION,
                "x:Access-Response-Specification");
        list.put(TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA,
                "x:access-response-list-of-data");
        list.put(TranslatorTags.SERVICE, "x:service");
        list.put(TranslatorTags.SERVICE_ERROR, "x:service-error");
    }

    /**
     * Get glo tags.
     * 
     * @param type
     * @param list
     */
    static void getGloTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list) {
        GXDLMSTranslator.addTag(list, Command.GLO_INITIATE_REQUEST,
                "x:glo-initiate-request");
        GXDLMSTranslator.addTag(list, Command.GLO_INITIATE_RESPONSE,
                "x:glo-initiate-response");
        GXDLMSTranslator.addTag(list, Command.GLO_GET_REQUEST,
                "x:glo-get-request");
        GXDLMSTranslator.addTag(list, Command.GLO_GET_RESPONSE,
                "x:glo-get-response");
        GXDLMSTranslator.addTag(list, Command.GLO_SET_REQUEST,
                "x:glo-set-request");
        GXDLMSTranslator.addTag(list, Command.GLO_SET_RESPONSE,
                "x:glo-set-response");
        GXDLMSTranslator.addTag(list, Command.GLO_METHOD_REQUEST,
                "x:glo-action-request");
        GXDLMSTranslator.addTag(list, Command.GLO_METHOD_RESPONSE,
                "x:glo-action-response");
        GXDLMSTranslator.addTag(list, Command.GLO_READ_REQUEST,
                "x:glo-read-request");
        GXDLMSTranslator.addTag(list, Command.GLO_READ_RESPONSE,
                "x:glo-read-response");
        GXDLMSTranslator.addTag(list, Command.GLO_WRITE_REQUEST,
                "x:glo-write-request");
        GXDLMSTranslator.addTag(list, Command.GLO_WRITE_RESPONSE,
                "x:glo-write-response");
        GXDLMSTranslator.addTag(list, Command.GENERAL_GLO_CIPHERING,
                "x:general-glo-ciphering");
        GXDLMSTranslator.addTag(list, Command.GENERAL_CIPHERING,
                "x:general-ciphering");
    }

    /**
     * Get translator tags.
     * 
     * @param type
     * @param list
     */
    static void getTranslatorTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list) {
        GXDLMSTranslator.addTag(list, TranslatorTags.WRAPPER, "Wrapper");
        GXDLMSTranslator.addTag(list, TranslatorTags.HDLC, "Hdlc");
        GXDLMSTranslator.addTag(list, TranslatorTags.PDU_DLMS, "x:xDLMS-APDU");
        GXDLMSTranslator.addTag(list, TranslatorTags.PDU_CSE, "x:aCSE-APDU");
        GXDLMSTranslator.addTag(list, TranslatorTags.TARGET_ADDRESS,
                "TargetAddress");
        GXDLMSTranslator.addTag(list, TranslatorTags.SOURCE_ADDRESS,
                "SourceAddress");
        GXDLMSTranslator.addTag(list,
                TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION,
                "x:variable-access-specification");
        GXDLMSTranslator.addTag(list, TranslatorTags.LIST_OF_DATA,
                "x:list-of-data");
        GXDLMSTranslator.addTag(list, TranslatorTags.SUCCESS, "Success");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA_ACCESS_ERROR,
                "x:data-access-result");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_DESCRIPTOR,
                "x:cosem-attribute-descriptor");
        GXDLMSTranslator.addTag(list, TranslatorTags.CLASS_ID, "x:class-id");
        GXDLMSTranslator.addTag(list, TranslatorTags.INSTANCE_ID,
                "x:instance-id");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_ID,
                "x:attribute-id");
        GXDLMSTranslator.addTag(list,
                TranslatorTags.METHOD_INVOCATION_PARAMETERS,
                "x:method-invocation-parameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.SELECTOR, "x:selector");
        GXDLMSTranslator.addTag(list, TranslatorTags.PARAMETER, "x:parameter");
        GXDLMSTranslator.addTag(list, TranslatorTags.LAST_BLOCK,
                "x:last-block");
        GXDLMSTranslator.addTag(list, TranslatorTags.BLOCK_NUMBER,
                "x:block-number");
        GXDLMSTranslator.addTag(list, TranslatorTags.RAW_DATA, "x:raw-data");
        GXDLMSTranslator.addTag(list, TranslatorTags.METHOD_DESCRIPTOR,
                "x:cosem-method-descriptor");
        GXDLMSTranslator.addTag(list, TranslatorTags.METHOD_ID, "x:method-id");
        GXDLMSTranslator.addTag(list, TranslatorTags.RESULT, "x:result");
        GXDLMSTranslator.addTag(list, TranslatorTags.RETURN_PARAMETERS,
                "x:return-parameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_SELECTION,
                "x:access-selection");
        GXDLMSTranslator.addTag(list, TranslatorTags.VALUE, "x:value");
        GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_SELECTOR,
                "x:access-selector");
        GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_PARAMETERS,
                "x:access-parameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST,
                "AttributeDescriptorList");
        GXDLMSTranslator.addTag(list,
                TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION,
                "AttributeDescriptorWithSelection");
        GXDLMSTranslator.addTag(list, TranslatorTags.READ_DATA_BLOCK_ACCESS,
                "ReadDataBlockAccess");
        GXDLMSTranslator.addTag(list, TranslatorTags.WRITE_DATA_BLOCK_ACCESS,
                "WriteDataBlockAccess");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA, "x:data");
        GXDLMSTranslator.addTag(list, TranslatorTags.INVOKE_ID,
                "x:invoke-id-and-priority");
        GXDLMSTranslator.addTag(list, TranslatorTags.LONG_INVOKE_ID,
                "x:long-invoke-id-and-priority");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATE_TIME, "x:date-time");
        GXDLMSTranslator.addTag(list, TranslatorTags.CURRENT_TIME,
                "x:current-time");
        GXDLMSTranslator.addTag(list, TranslatorTags.TIME, "x:time");
        GXDLMSTranslator.addTag(list, TranslatorTags.REASON, "x:reason");
        GXDLMSTranslator.addTag(list,
                TranslatorTags.VARIABLE_ACCESS_SPECIFICATION,
                "x:Variable-Access-Specification");
        GXDLMSTranslator.addTag(list, TranslatorTags.CHOICE, "x:CHOICE");
        GXDLMSTranslator.addTag(list, TranslatorTags.NOTIFICATION_BODY,
                "x:notification-body");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA_VALUE,
                "x:data-value");
        GXDLMSTranslator.addTag(list, TranslatorTags.INITIATE_ERROR,
                "x:initiateError");
        GXDLMSTranslator.addTag(list, TranslatorTags.CIPHERED_SERVICE,
                "x:ciphered-content");
        GXDLMSTranslator.addTag(list, TranslatorTags.SYSTEM_TITLE,
                "x:system-title");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA_BLOCK, "x:datablock");
        GXDLMSTranslator.addTag(list, TranslatorTags.TRANSACTION_ID,
                "x:transaction-id");
        GXDLMSTranslator.addTag(list, TranslatorTags.ORIGINATOR_SYSTEM_TITLE,
                "x:originator-system-title");
        GXDLMSTranslator.addTag(list, TranslatorTags.RECIPIENT_SYSTEM_TITLE,
                "x:recipient-system-title");
        GXDLMSTranslator.addTag(list, TranslatorTags.OTHER_INFORMATION,
                "x:other-information");
        GXDLMSTranslator.addTag(list, TranslatorTags.KEY_INFO, "x:key-info");
        GXDLMSTranslator.addTag(list, TranslatorTags.CIPHERED_CONTENT,
                "x:ciphered-content");
        GXDLMSTranslator.addTag(list, TranslatorTags.AGREED_KEY,
                "x:agreed-key");
        GXDLMSTranslator.addTag(list, TranslatorTags.KEY_PARAMETERS,
                "x:key-parameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.KEY_CIPHERED_DATA,
                "x:key-ciphered-data");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_VALUE,
                "x:attribute-value");
    }

    static void getDataTypeTags(final HashMap<Integer, String> list) {
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.NONE.getValue(),
                "x:null-data");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.ARRAY.getValue(),
                "x:array");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BCD.getValue(), "x:bcd");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BITSTRING.getValue(),
                "x:bit-string");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BOOLEAN.getValue(),
                "x:boolean");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.COMPACT_ARRAY.getValue(),
                "x:compact-array");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.DATE.getValue(), "x:date");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.DATETIME.getValue(),
                "x:date-time");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.ENUM.getValue(), "x:enum");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.FLOAT32.getValue(),
                "x:float32");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.FLOAT64.getValue(),
                "x:float64,");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT16.getValue(), "x:long");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT32.getValue(),
                "x:double-long");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT64.getValue(),
                "x:long64");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT8.getValue(),
                "x:integer");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.OCTET_STRING.getValue(),
                "x:octet-string");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRING.getValue(),
                "x:visible-string");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRING_UTF8.getValue(),
                "x:utf8-string");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue(),
                "x:structure");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.TIME.getValue(), "x:time");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT16.getValue(),
                "x:long-unsigned");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT32.getValue(),
                "x:double-long-unsigned");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT64.getValue(),
                "x:long64-unsigned");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT8.getValue(),
                "x:unsigned");
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
            throw new IllegalArgumentException(
                    "Error code: " + String.valueOf(value));
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
        Map<ApplicationReference, String> list =
                new HashMap<ApplicationReference, String>();
        list.put(ApplicationReference.APPLICATION_CONTEXT_UNSUPPORTED,
                "application-context-unsupported");
        list.put(ApplicationReference.APPLICATION_REFERENCE_INVALID,
                "application-reference-invalid");
        list.put(ApplicationReference.APPLICATION_UNREACHABLE,
                "application-unreachable");
        list.put(ApplicationReference.DECIPHERING_ERROR, "deciphering-error");
        list.put(ApplicationReference.OTHER, "other");
        list.put(ApplicationReference.PROVIDER_COMMUNICATION_ERROR,
                "provider-communication-error");
        list.put(ApplicationReference.TIME_ELAPSED, "time-elapsed");
        return list;
    }

    static Map<HardwareResource, String> getHardwareResource() {
        Map<HardwareResource, String> list =
                new HashMap<HardwareResource, String>();
        list.put(HardwareResource.MASS_STORAGE_UNAVAILABLE,
                "mass-storage-unavailable");
        list.put(HardwareResource.MEMORY_UNAVAILABLE, "memory-unavailable");
        list.put(HardwareResource.OTHER, "other");
        list.put(HardwareResource.OTHER_RESOURCE_UNAVAILABLE,
                "other-resource-unavailable");
        list.put(HardwareResource.PROCESSOR_RESOURCE_UNAVAILABLE,
                "processor-resource-unavailable");
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
        list.put(Definition.OBJECT_ATTRIBUTE_INCONSISTENT,
                "object-attribute-inconsistent");
        list.put(Definition.OBJECT_CLASS_INCONSISTENT,
                "object-class-inconsistent");
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
        list.put(Initiate.REFUSED_BY_THE_VDE_HANDLER,
                "refused-by-the-VDE-Handler");
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
        list.put(LoadDataSet.PRIMITIVE_OUT_OF_SEQUENCE,
                "primitive-out-of-sequence");
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

    static String getServiceErrorValue(final ServiceError error,
            final byte value) {
        switch (error) {
        case APPLICATION_REFERENCE:
            return getApplicationReference()
                    .get(ApplicationReference.forValue(value));
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
     * @param error
     *            Service error enumeration value.
     * @return Service error standard XML tag.
     */
    static String serviceErrorToString(final ServiceError error) {
        return getServiceErrors().get(error);
    }

    /**
     * @param value
     *            Service error standard XML tag.
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
        for (Entry<ApplicationReference, String> it : getApplicationReference()
                .entrySet()) {
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
        for (Entry<HardwareResource, String> it : getHardwareResource()
                .entrySet()) {
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
        case RESERVED_SIX:
            str = "reserved-six";
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
        } else if ("reserved-six".equalsIgnoreCase(value)) {
            ret = Conformance.RESERVED_SIX;
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

    static String
            releaseResponseReasonToString(final ReleaseResponseReason value) {
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

    static ReleaseResponseReason
            valueOfReleaseResponseReason(final String value) {
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

    static String
            releaseRequestReasonToString(final ReleaseRequestReason value) {
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

    static ReleaseRequestReason
            valueOfReleaseRequestReason(final String value) {
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
}
