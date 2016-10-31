package gurux.dlms;

import java.util.HashMap;

import gurux.dlms.enums.AccessServiceCommandType;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;

final class TranslatorSimpleTags {
    /**
     * Constructor.
     */
    private TranslatorSimpleTags() {

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
        GXDLMSTranslator.addTag(list, Command.UA, "Ua");
        GXDLMSTranslator.addTag(list, Command.AARQ, "AssociationRequest");
        GXDLMSTranslator.addTag(list, Command.AARE, "AssociationResponse");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.APPLICATION_CONTEXT_NAME,
                "ApplicationContextName");
        GXDLMSTranslator.addTag(list, Command.INITIATE_RESPONSE,
                "InitiateResponse");
        GXDLMSTranslator.addTag(list, Command.INITIATE_REQUEST,
                "InitiateRequest");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_QUALITY_OF_SERVICE,
                "NegotiatedQualityOfService");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_QUALITY_OF_SERVICE,
                "ProposedQualityOfService");

        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_DLMS_VERSION_NUMBER,
                "ProposedDlmsVersionNumber");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_MAX_PDU_SIZE,
                "ProposedMaxPduSize");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.PROPOSED_CONFORMANCE,
                "ProposedConformance");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.VAA_NAME,
                "VaaName");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_CONFORMANCE,
                "NegotiatedConformance");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_DLMS_VERSION_NUMBER,
                "NegotiatedDlmsVersionNumber");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.NEGOTIATED_MAX_PDU_SIZE,
                "NegotiatedMaxPduSize");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CONFORMANCE_BIT,
                "ConformanceBit");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.SENDER_ACSE_REQUIREMENTS,
                "SenderACSERequirements");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESPONDER_ACSE_REQUIREMENT,
                "ResponderACSERequirement");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESPONDING_MECHANISM_NAME,
                "MechanismName");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.CALLING_MECHANISM_NAME, "MechanismName");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.CALLING_AUTHENTICATION,
                "CallingAuthentication");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESPONDING_AUTHENTICATION,
                "RespondingAuthentication");
        GXDLMSTranslator.addTag(list, Command.DISCONNECT_REQUEST,
                "ReleaseRequest");
        GXDLMSTranslator.addTag(list, Command.DISCONNECT_RESPONSE,
                "ReleaseResponse");
        GXDLMSTranslator.addTag(list, Command.DISC, "Disc");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.ASSOCIATION_RESULT,
                "AssociationResult");
        GXDLMSTranslator.addTag(list,
                TranslatorGeneralTags.RESULT_SOURCE_DIAGNOSTIC,
                "ResultSourceDiagnostic");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.ACSE_SERVICE_USER,
                "ACSEServiceUser");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.CALLING_AP_TITLE,
                "CallingAPTitle");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.RESPONDING_AP_TITLE,
                "RespondingAPTitle");
        GXDLMSTranslator.addTag(list, TranslatorGeneralTags.DEDICATED_KEY,
                "DedicatedKey");
    }

    /**
     * Get SN tags.
     * 
     * @param type
     * @param list
     */
    static void getSnTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list) {
        list.put(Command.READ_REQUEST, "ReadRequest");
        list.put(Command.WRITE_REQUEST, "WriteRequest");
        list.put(Command.WRITE_REQUEST << 8 | SingleReadResponse.DATA,
                "VariableName");
        list.put(Command.WRITE_RESPONSE, "WriteResponse");
        list.put(
                Command.READ_REQUEST << 8
                        | VariableAccessSpecification.VARIABLE_NAME,
                "VariableName");
        list.put(
                Command.READ_REQUEST << 8
                        | VariableAccessSpecification.PARAMETERISED_ACCESS,
                "ParameterisedAccess");
        list.put(
                Command.READ_REQUEST << 8
                        | VariableAccessSpecification.BLOCK_NUMBER_ACCESS,
                "BlockNumberAccess");
        list.put(
                Command.WRITE_REQUEST << 8
                        | VariableAccessSpecification.VARIABLE_NAME,
                "VariableName");
        list.put(Command.READ_RESPONSE, "ReadResponse");
        list.put(
                Command.READ_RESPONSE << 8
                        | SingleReadResponse.DATA_BLOCK_RESULT,
                "DataBlockResult");
        list.put(Command.READ_RESPONSE << 8 | SingleReadResponse.DATA, "Data");
        list.put(
                Command.READ_RESPONSE << 8
                        | SingleReadResponse.DATA_ACCESS_ERROR,
                "DataAccessError");
    }

    /**
     * Get LN tags.
     * 
     * @param type
     * @param list
     */
    static void getLnTags(final TranslatorOutputType type,
            final HashMap<Integer, String> list) {
        GXDLMSTranslator.addTag(list, Command.GET_REQUEST, "GetRequest");
        list.put(Command.GET_REQUEST << 8 | GetCommandType.NORMAL,
                "GetRequestNormal");
        list.put(Command.GET_REQUEST << 8 | GetCommandType.NEXT_DATA_BLOCK,
                "GetRequestForNextDataBlock");
        list.put(Command.GET_REQUEST << 8 | GetCommandType.WITH_LIST,
                "GetRequestWithList");
        GXDLMSTranslator.addTag(list, Command.SET_REQUEST, "SetRequest");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.NORMAL,
                "SetRequestNormal");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.FIRST_DATA_BLOCK,
                "SetRequestFirstDataBlock");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.WITH_DATA_BLOCK,
                "SetRequestWithDataBlock");
        list.put(Command.SET_REQUEST << 8 | SetRequestType.WITH_LIST,
                "SetRequestWithList");
        GXDLMSTranslator.addTag(list, Command.METHOD_REQUEST, "ActionRequest");
        list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.NORMAL,
                "ActionRequestNormal");
        list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.NEXT_BLOCK,
                "ActionRequestForNextDataBlock");
        list.put(Command.METHOD_REQUEST << 8 | ActionRequestType.WITH_LIST,
                "ActionRequestWithList");
        GXDLMSTranslator.addTag(list, Command.METHOD_RESPONSE,
                "ActionResponse");
        list.put(Command.METHOD_RESPONSE << 8 | ActionResponseType.NORMAL,
                "ActionResponseNormal");
        list.put(
                Command.METHOD_RESPONSE << 8
                        | ActionResponseType.WITH_FIRST_BLOCK,
                "ActionResponseWithFirstBlock");
        list.put(Command.METHOD_RESPONSE << 8 | ActionResponseType.WITH_LIST,
                "ActionResponseWithList");
        list.put((int) Command.DATA_NOTIFICATION, "DataNotification");
        GXDLMSTranslator.addTag(list, Command.GET_RESPONSE, "GetResponse");
        list.put(Command.GET_RESPONSE << 8 | GetCommandType.NORMAL,
                "GetResponseNormal");
        list.put(Command.GET_RESPONSE << 8 | GetCommandType.NEXT_DATA_BLOCK,
                "GetResponsewithDataBlock");
        list.put(Command.GET_RESPONSE << 8 | GetCommandType.WITH_LIST,
                "GetResponseWithList");
        GXDLMSTranslator.addTag(list, Command.SET_RESPONSE, "SetResponse");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.NORMAL,
                "SetResponseNormal");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.DATA_BLOCK,
                "SetResponseDataBlock");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.LAST_DATA_BLOCK,
                "SetResponseWithLastDataBlock");
        list.put(Command.SET_RESPONSE << 8 | SetResponseType.WITH_LIST,
                "SetResponseWithList");

        GXDLMSTranslator.addTag(list, Command.ACCESS_REQUEST, "AccessRequest");
        list.put((Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.GET,
                "AccessRequestGet");
        list.put((Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.SET,
                "AccessRequestSet");
        list.put(
                (Command.ACCESS_REQUEST) << 8 | AccessServiceCommandType.ACTION,
                "AccessRequestAction");
        GXDLMSTranslator.addTag(list, Command.ACCESS_RESPONSE,
                "AccessResponse");
        list.put((Command.ACCESS_RESPONSE) << 8 | AccessServiceCommandType.GET,
                "AccessResponseGet");
        list.put((Command.ACCESS_RESPONSE) << 8 | AccessServiceCommandType.SET,
                "AccessResponseSet");
        list.put(
                (Command.ACCESS_RESPONSE) << 8
                        | AccessServiceCommandType.ACTION,
                "AccessResponseAction");
        list.put(TranslatorTags.ACCESS_REQUEST_BODY, "AccessRequestBody");
        list.put(TranslatorTags.LIST_OF_ACCESS_REQUEST_SPECIFICATION,
                "AccessRequestSpecification");
        list.put(TranslatorTags.ACCESS_REQUEST_SPECIFICATION,
                "_AccessRequestSpecification");
        list.put(TranslatorTags.ACCESS_REQUEST_LIST_OF_DATA,
                "AccessRequestListOfData");

        list.put(TranslatorTags.ACCESS_RESPONSE_BODY, "AccessResponseBody");
        list.put(TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION,
                "AccessResponseSpecification");
        list.put(TranslatorTags.ACCESS_RESPONSE_SPECIFICATION,
                "_AccessResponseSpecification");
        list.put(TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA,
                "AccessResponseListOfData");
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
                "glo_InitiateRequest");
        GXDLMSTranslator.addTag(list, Command.GLO_INITIATE_RESPONSE,
                "glo_InitiateResponse");
        GXDLMSTranslator.addTag(list, Command.GLO_GET_REQUEST,
                "glo_GetRequest");
        GXDLMSTranslator.addTag(list, Command.GLO_GET_RESPONSE,
                "glo_GetResponse");
        GXDLMSTranslator.addTag(list, Command.GLO_SET_REQUEST,
                "glo_SetRequest");
        GXDLMSTranslator.addTag(list, Command.GLO_SET_RESPONSE,
                "glo_SetResponse");
        GXDLMSTranslator.addTag(list, Command.GLO_METHOD_REQUEST,
                "glo_ActionRequest");
        GXDLMSTranslator.addTag(list, Command.GLO_METHOD_RESPONSE,
                "glo_ActionResponse");
        GXDLMSTranslator.addTag(list, Command.GLO_READ_REQUEST,
                "glo_ReadRequest");
        GXDLMSTranslator.addTag(list, Command.GLO_READ_RESPONSE,
                "glo_ReadResponse");
        GXDLMSTranslator.addTag(list, Command.GLO_WRITE_REQUEST,
                "glo_WriteRequest");
        GXDLMSTranslator.addTag(list, Command.GLO_WRITE_RESPONSE,
                "glo_WriteResponse");
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
        GXDLMSTranslator.addTag(list, TranslatorTags.PDU_DLMS, "Pdu");
        GXDLMSTranslator.addTag(list, TranslatorTags.TARGET_ADDRESS,
                "TargetAddress");
        GXDLMSTranslator.addTag(list, TranslatorTags.SOURCE_ADDRESS,
                "SourceAddress");
        GXDLMSTranslator.addTag(list,
                TranslatorTags.LIST_OF_VARIABLE_ACCESS_SPECIFICATION,
                "ListOfVariableAccessSpecification");
        GXDLMSTranslator.addTag(list, TranslatorTags.LIST_OF_DATA,
                "ListOfData");
        GXDLMSTranslator.addTag(list, TranslatorTags.SUCCESS, "Success");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA_ACCESS_ERROR,
                "DataAccessError");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_DESCRIPTOR,
                "AttributeDescriptor");
        GXDLMSTranslator.addTag(list, TranslatorTags.CLASS_ID, "ClassId");
        GXDLMSTranslator.addTag(list, TranslatorTags.INSTANCE_ID, "InstanceId");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_ID,
                "AttributeId");
        GXDLMSTranslator.addTag(list,
                TranslatorTags.METHOD_INVOCATION_PARAMETERS,
                "MethodInvocationParameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.SELECTOR, "Selector");
        GXDLMSTranslator.addTag(list, TranslatorTags.PARAMETER, "Parameter");
        GXDLMSTranslator.addTag(list, TranslatorTags.LAST_BLOCK, "LastBlock");
        GXDLMSTranslator.addTag(list, TranslatorTags.BLOCK_NUMBER,
                "BlockNumber");
        GXDLMSTranslator.addTag(list, TranslatorTags.RAW_DATA, "RawData");
        GXDLMSTranslator.addTag(list, TranslatorTags.METHOD_DESCRIPTOR,
                "MethodDescriptor");
        GXDLMSTranslator.addTag(list, TranslatorTags.METHOD_ID, "MethodId");
        GXDLMSTranslator.addTag(list, TranslatorTags.RESULT, "Result");
        GXDLMSTranslator.addTag(list, TranslatorTags.RETURN_PARAMETERS,
                "ReturnParameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_SELECTION,
                "AccessSelection");
        GXDLMSTranslator.addTag(list, TranslatorTags.VALUE, "Value");
        GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_SELECTOR,
                "AccessSelector");
        GXDLMSTranslator.addTag(list, TranslatorTags.ACCESS_PARAMETERS,
                "AccessParameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_DESCRIPTOR_LIST,
                "AttributeDescriptorList");
        GXDLMSTranslator.addTag(list,
                TranslatorTags.ATTRIBUTE_DESCRIPTOR_WITH_SELECTION,
                "AttributeDescriptorWithSelection");
        GXDLMSTranslator.addTag(list, TranslatorTags.READ_DATA_BLOCK_ACCESS,
                "ReadDataBlockAccess");
        GXDLMSTranslator.addTag(list, TranslatorTags.WRITE_DATA_BLOCK_ACCESS,
                "WriteDataBlockAccess");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA, "Data");
        GXDLMSTranslator.addTag(list, TranslatorTags.INVOKE_ID,
                "InvokeIdAndPriority");
        GXDLMSTranslator.addTag(list, TranslatorTags.LONG_INVOKE_ID,
                "LongInvokeIdAndPriority");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATE_TIME, "DateTime");
        GXDLMSTranslator.addTag(list, TranslatorTags.REASON, "Reason");
        GXDLMSTranslator.addTag(list, TranslatorTags.NOTIFICATION_BODY,
                "NotificationBody");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA_VALUE, "DataValue");
    }

    static void getDataTypeTags(final HashMap<Integer, String> list) {
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.NONE.getValue(), "None");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.ARRAY.getValue(), "Array");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BCD.getValue(), "BCD");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BITSTRING.getValue(),
                "BitString");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.BOOLEAN.getValue(),
                "Boolean");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.COMPACT_ARRAY.getValue(),
                "CompactArray");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.DATE.getValue(), "Date");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.DATETIME.getValue(),
                "DateTime");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.ENUM.getValue(), "Enum");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.FLOAT32.getValue(),
                "Float32");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.FLOAT64.getValue(),
                "Float64");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT16.getValue(), "Int16");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT32.getValue(), "Int32");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT64.getValue(), "Int64");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.INT8.getValue(), "Int8");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.OCTET_STRING.getValue(),
                "OctetString");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRING.getValue(),
                "String");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRING_UTF8.getValue(),
                "StringUTF8");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.STRUCTURE.getValue(),
                "Structure");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.TIME.getValue(), "Time");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT16.getValue(),
                "UInt16");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT32.getValue(),
                "UInt32");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT64.getValue(),
                "UInt64");
        list.put(GXDLMS.DATA_TYPE_OFFSET + DataType.UINT8.getValue(), "UInt8");
    }

    static String errorCodeToString(final ErrorCode value) {
        String str;
        switch (value) {
        case ACCESS_VIOLATED:
            str = "AccessViolated";
            break;
        case DATA_BLOCK_NUMBER_INVALID:
            str = "DataBlockNumberInvalid";
            break;
        case DATA_BLOCK_UNAVAILABLE:
            str = "DataBlockUnavailable";
            break;
        case HARDWARE_FAULT:
            str = "HardwareFault";
            break;
        case INCONSISTENT_CLASS:
            str = "InconsistentClass";
            break;
        case LONG_GET_OR_READ_ABORTED:
            str = "LongGetOrReadAborted";
            break;
        case LONG_SET_OR_WRITE_ABORTED:
            str = "LongSetOrWriteAborted";
            break;
        case NO_LONG_GET_OR_READ_IN_PROGRESS:
            str = "NoLongGetOrReadInProgress";
            break;
        case NO_LONG_SET_OR_WRITE_IN_PROGRESS:
            str = "NoLongSetOrWriteInProgress";
            break;
        case OK:
            str = "Success";
            break;
        case OTHER_REASON:
            str = "OtherReason";
            break;
        case READ_WRITE_DENIED:
            str = "ReadWriteDenied";
            break;
        case TEMPORARY_FAILURE:
            str = "TemporaryFailure";
            break;
        case UNAVAILABLE_OBJECT:
            str = "UnavailableObject";
            break;
        case UNDEFINED_OBJECT:
            str = "UndefinedObject";
            break;
        case UNMATCHED_TYPE:
            str = "UnmatchedType";
            break;
        default:
            throw new IllegalArgumentException(
                    "Error code: " + String.valueOf(value));
        }
        return str;
    }

    static ErrorCode valueOfErrorCode(final String value) {
        ErrorCode v;
        if ("AccessViolated".equalsIgnoreCase(value)) {
            v = ErrorCode.ACCESS_VIOLATED;
        } else if ("DataBlockNumberInvalid".equalsIgnoreCase(value)) {
            v = ErrorCode.DATA_BLOCK_NUMBER_INVALID;
        } else if ("DataBlockUnavailable".equalsIgnoreCase(value)) {
            v = ErrorCode.DATA_BLOCK_UNAVAILABLE;
        } else if ("HardwareFault".equalsIgnoreCase(value)) {
            v = ErrorCode.HARDWARE_FAULT;
        } else if ("InconsistentClass".equalsIgnoreCase(value)) {
            v = ErrorCode.INCONSISTENT_CLASS;
        } else if ("LongGetOrReadAborted".equalsIgnoreCase(value)) {
            v = ErrorCode.LONG_GET_OR_READ_ABORTED;
        } else if ("LongSetOrWriteAborted".equalsIgnoreCase(value)) {
            v = ErrorCode.LONG_SET_OR_WRITE_ABORTED;
        } else if ("NoLongGetOrReadInProgress".equalsIgnoreCase(value)) {
            v = ErrorCode.NO_LONG_GET_OR_READ_IN_PROGRESS;
        } else if ("NoLongSetOrWriteInProgress".equalsIgnoreCase(value)) {
            v = ErrorCode.NO_LONG_SET_OR_WRITE_IN_PROGRESS;
        } else if ("Success".equalsIgnoreCase(value)) {
            v = ErrorCode.OK;
        } else if ("OtherReason".equalsIgnoreCase(value)) {
            v = ErrorCode.OTHER_REASON;
        } else if ("ReadWriteDenied".equalsIgnoreCase(value)) {
            v = ErrorCode.READ_WRITE_DENIED;
        } else if ("TemporaryFailure".equalsIgnoreCase(value)) {
            v = ErrorCode.TEMPORARY_FAILURE;
        } else if ("UnavailableObject".equalsIgnoreCase(value)) {
            v = ErrorCode.UNAVAILABLE_OBJECT;
        } else if ("UndefinedObject".equalsIgnoreCase(value)) {
            v = ErrorCode.UNDEFINED_OBJECT;
        } else if ("UnmatchedType".equalsIgnoreCase(value)) {
            v = ErrorCode.UNMATCHED_TYPE;
        } else {
            throw new IllegalArgumentException("Error code: " + value);
        }
        return v;
    }

}
