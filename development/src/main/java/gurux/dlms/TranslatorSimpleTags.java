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
        GXDLMSTranslator.addTag(list, Command.UNACCEPTABLE_FRAME,
                "UnacceptableFrame");
        GXDLMSTranslator.addTag(list, Command.DISCONNECT_MODE,
                "DisconnectMode");
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
        GXDLMSTranslator.addTag(list, Command.RELEASE_REQUEST,
                "ReleaseRequest");
        GXDLMSTranslator.addTag(list, Command.RELEASE_RESPONSE,
                "ReleaseResponse");
        GXDLMSTranslator.addTag(list, Command.DISCONNECT_REQUEST,
                "DisconnectRequest");
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
        GXDLMSTranslator.addTag(list, Command.CONFIRMED_SERVICE_ERROR,
                "ConfirmedServiceError");
        GXDLMSTranslator.addTag(list, Command.INFORMATION_REPORT,
                "InformationReportRequest");
        GXDLMSTranslator.addTag(list, Command.EVENT_NOTIFICATION,
                "EventNotificationRequest");
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
        list.put(TranslatorTags.SERVICE, "Service");
        list.put(TranslatorTags.SERVICE_ERROR, "ServiceError");
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
        GXDLMSTranslator.addTag(list, Command.GENERAL_GLO_CIPHERING,
                "GeneralGloCiphering");

        GXDLMSTranslator.addTag(list, Command.GENERAL_CIPHERING,
                "GeneralCiphering");
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
        GXDLMSTranslator.addTag(list, TranslatorTags.CURRENT_TIME,
                "CurrentTime");
        GXDLMSTranslator.addTag(list, TranslatorTags.TIME, "Time");

        GXDLMSTranslator.addTag(list, TranslatorTags.REASON, "Reason");
        GXDLMSTranslator.addTag(list, TranslatorTags.NOTIFICATION_BODY,
                "NotificationBody");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA_VALUE, "DataValue");
        GXDLMSTranslator.addTag(list, TranslatorTags.CIPHERED_SERVICE,
                "CipheredService");
        GXDLMSTranslator.addTag(list, TranslatorTags.SYSTEM_TITLE,
                "SystemTitle");
        GXDLMSTranslator.addTag(list, TranslatorTags.DATA_BLOCK, "DataBlock");

        GXDLMSTranslator.addTag(list, TranslatorTags.TRANSACTION_ID,
                "TransactionId");
        GXDLMSTranslator.addTag(list, TranslatorTags.ORIGINATOR_SYSTEM_TITLE,
                "OriginatorSystemTitle");
        GXDLMSTranslator.addTag(list, TranslatorTags.RECIPIENT_SYSTEM_TITLE,
                "RecipientSystemTitle");
        GXDLMSTranslator.addTag(list, TranslatorTags.OTHER_INFORMATION,
                "OtherInformation");
        GXDLMSTranslator.addTag(list, TranslatorTags.KEY_INFO, "KeyInfo");
        GXDLMSTranslator.addTag(list, TranslatorTags.CIPHERED_CONTENT,
                "CipheredContent");
        GXDLMSTranslator.addTag(list, TranslatorTags.AGREED_KEY, "AgreedKey");
        GXDLMSTranslator.addTag(list, TranslatorTags.KEY_PARAMETERS,
                "KeyParameters");
        GXDLMSTranslator.addTag(list, TranslatorTags.KEY_CIPHERED_DATA,
                "KeyCipheredData");
        GXDLMSTranslator.addTag(list, TranslatorTags.ATTRIBUTE_VALUE,
                "AttributeValue");
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

    private static Map<ServiceError, String> getServiceErrors() {
        Map<ServiceError, String> list = new HashMap<ServiceError, String>();
        list.put(ServiceError.APPLICATION_REFERENCE, "ApplicationReference");
        list.put(ServiceError.HARDWARE_RESOURCE, "HardwareResource");
        list.put(ServiceError.VDE_STATE_ERROR, "VdeStateError");
        list.put(ServiceError.SERVICE, "Service");
        list.put(ServiceError.DEFINITION, "Definition");
        list.put(ServiceError.ACCESS, "Access");
        list.put(ServiceError.INITIATE, "Initiate");
        list.put(ServiceError.LOAD_DATASET, "LoadDataSet");
        list.put(ServiceError.TASK, "Task");
        return list;
    }

    static Map<ApplicationReference, String> getApplicationReference() {
        Map<ApplicationReference, String> list =
                new HashMap<ApplicationReference, String>();
        list.put(ApplicationReference.APPLICATION_CONTEXT_UNSUPPORTED,
                "ApplicationContextUnsupported");
        list.put(ApplicationReference.APPLICATION_REFERENCE_INVALID,
                "ApplicationReferenceInvalid");
        list.put(ApplicationReference.APPLICATION_UNREACHABLE,
                "ApplicationUnreachable");
        list.put(ApplicationReference.DECIPHERING_ERROR, "DecipheringError");
        list.put(ApplicationReference.OTHER, "Other");
        list.put(ApplicationReference.PROVIDER_COMMUNICATION_ERROR,
                "ProviderCommunicationError");
        list.put(ApplicationReference.TIME_ELAPSED, "TimeElapsed");
        return list;
    }

    static Map<HardwareResource, String> getHardwareResource() {
        Map<HardwareResource, String> list =
                new HashMap<HardwareResource, String>();
        list.put(HardwareResource.MASS_STORAGE_UNAVAILABLE,
                "MassStorageUnavailable");
        list.put(HardwareResource.MEMORY_UNAVAILABLE, "MemoryUnavailable");
        list.put(HardwareResource.OTHER, "Other");
        list.put(HardwareResource.OTHER_RESOURCE_UNAVAILABLE,
                "OtherResourceUnavailable");
        list.put(HardwareResource.PROCESSOR_RESOURCE_UNAVAILABLE,
                "ProcessorResourceUnavailable");
        return list;
    }

    static Map<VdeStateError, String> getVdeStateError() {
        Map<VdeStateError, String> list = new HashMap<VdeStateError, String>();
        list.put(VdeStateError.LOADING_DATASET, "LoadingDataSet");
        list.put(VdeStateError.NO_DLMS_CONTEXT, "NoDlmsContext");
        list.put(VdeStateError.OTHER, "Other");
        list.put(VdeStateError.STATUS_INOPERABLE, "StatusInoperable");
        list.put(VdeStateError.STATUS_NO_CHANGE, "StatusNochange");
        return list;
    }

    static Map<Service, String> getService() {
        Map<Service, String> list = new HashMap<Service, String>();
        list.put(Service.OTHER, "Other");
        list.put(Service.PDU_SIZE, "PduSize");
        list.put(Service.UNSUPPORTED, "ServiceUnsupported");
        return list;
    }

    static Map<Definition, String> getDefinition() {
        Map<Definition, String> list = new HashMap<Definition, String>();
        list.put(Definition.OBJECT_ATTRIBUTE_INCONSISTENT,
                "ObjectAttributeInconsistent");
        list.put(Definition.OBJECT_CLASS_INCONSISTENT,
                "ObjectClassInconsistent");
        list.put(Definition.OBJECT_UNDEFINED, "ObjectUndefined");
        list.put(Definition.OTHER, "Other");
        return list;
    }

    static Map<Access, String> getAccess() {
        Map<Access, String> list = new HashMap<Access, String>();
        list.put(Access.HARDWARE_FAULT, "HardwareFault");
        list.put(Access.OBJECT_ACCESS_INVALID, "ObjectAccessInvalid");
        list.put(Access.OBJECT_UNAVAILABLE, "ObjectUnavailable");
        list.put(Access.OTHER, "Other");
        list.put(Access.SCOPE_OF_ACCESS_VIOLATED, "ScopeOfAccessViolated");
        return list;
    }

    static Map<Initiate, String> getInitiate() {
        Map<Initiate, String> list = new HashMap<Initiate, String>();
        list.put(Initiate.DLMS_VERSION_TOO_LOW, "DlmsVersionTooLow");
        list.put(Initiate.INCOMPATIBLE_CONFORMANCE, "IncompatibleConformance");
        list.put(Initiate.OTHER, "Other");
        list.put(Initiate.PDU_SIZE_TOO_SHORT, "PduSizeTooShort");
        list.put(Initiate.REFUSED_BY_THE_VDE_HANDLER, "RefusedByTheVDEHandler");
        return list;
    }

    static Map<LoadDataSet, String> getLoadDataSet() {
        Map<LoadDataSet, String> list = new HashMap<LoadDataSet, String>();
        list.put(LoadDataSet.DATASET_NOT_READY, "DataSetNotReady");
        list.put(LoadDataSet.DATASET_SIZE_TOO_LARGE, "DatasetSizeTooLarge");
        list.put(LoadDataSet.INTERPRETATION_FAILURE, "InterpretationFailure");
        list.put(LoadDataSet.NOT_AWAITED_SEGMENT, "NotAwaitedSegment");
        list.put(LoadDataSet.NOT_LOADABLE, "NotLoadable");
        list.put(LoadDataSet.OTHER, "Other");
        list.put(LoadDataSet.PRIMITIVE_OUT_OF_SEQUENCE,
                "PrimitiveOutOfSequence");
        list.put(LoadDataSet.STORAGE_FAILURE, "StorageFailure");
        return list;
    }

    static Map<Task, String> getTask() {
        Map<Task, String> list = new HashMap<Task, String>();
        list.put(Task.NO_REMOTE_CONTROL, "NoRemoteControl");
        list.put(Task.OTHER, "Other");
        list.put(Task.TI_RUNNING, "tiRunning");
        list.put(Task.TI_STOPPED, "tiStopped");
        list.put(Task.TI_UNUSABLE, "tiUnusable");
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
     * @return Service error simple XML tag.
     */
    static String serviceErrorToString(final ServiceError error) {
        return getServiceErrors().get(error);
    }

    /**
     * @param value
     *            Service error simple XML tag.
     * @return Service error enumeration value.
     */
    static ServiceError getServiceError(final String value) {
        ServiceError error = null;
        for (Entry<ServiceError, String> it : getServiceErrors().entrySet()) {
            if (value.compareTo(it.getValue().toLowerCase()) == 0) {
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
            str = "Access";
            break;
        case ACTION:
            str = "Action";
            break;
        case ATTRIBUTE_0_SUPPORTED_WITH_GET:
            str = "Attribute0SupportedWithGet";
            break;
        case ATTRIBUTE_0_SUPPORTED_WITH_SET:
            str = "Attribute0SupportedWithSet";
            break;
        case BLOCK_TRANSFER_WITH_ACTION:
            str = "BlockTransferWithAction";
            break;
        case BLOCK_TRANSFER_WITH_GET_OR_READ:
            str = "BlockTransferWithGetOrRead";
            break;
        case BLOCK_TRANSFER_WITH_SET_OR_WRITE:
            str = "BlockTransferWithSetOrWrite";
            break;
        case DATA_NOTIFICATION:
            str = "DataNotification";
            break;
        case EVENT_NOTIFICATION:
            str = "EventNotification";
            break;
        case GENERAL_BLOCK_TRANSFER:
            str = "GeneralBlockTransfer";
            break;
        case GENERAL_PROTECTION:
            str = "GeneralProtection";
            break;
        case GET:
            str = "Get";
            break;
        case INFORMATION_REPORT:
            str = "InformationReport";
            break;
        case MULTIPLE_REFERENCES:
            str = "MultipleReferences";
            break;
        case PARAMETERIZED_ACCESS:
            str = "ParameterizedAccess";
            break;
        case PRIORITY_MGMT_SUPPORTED:
            str = "PriorityMgmtSupported";
            break;
        case READ:
            str = "Read";
            break;
        case RESERVED_SEVEN:
            str = "ReservedSeven";
            break;
        case RESERVED_SIX:
            str = "ReservedSix";
            break;
        case RESERVED_ZERO:
            str = "ReservedZero";
            break;
        case SELECTIVE_ACCESS:
            str = "SelectiveAccess";
            break;
        case SET:
            str = "Set";
            break;
        case UN_CONFIRMED_WRITE:
            str = "UnconfirmedWrite";
            break;
        case WRITE:
            str = "Write";
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return str;
    }

    public static Conformance valueOfConformance(final String value) {
        Conformance ret;
        if ("Access".equalsIgnoreCase(value)) {
            ret = Conformance.ACCESS;
        } else if ("Action".equalsIgnoreCase(value)) {
            ret = Conformance.ACTION;
        } else if ("Attribute0SupportedWithGet".equalsIgnoreCase(value)) {
            ret = Conformance.ATTRIBUTE_0_SUPPORTED_WITH_GET;
        } else if ("Attribute0SupportedWithSet".equalsIgnoreCase(value)) {
            ret = Conformance.ATTRIBUTE_0_SUPPORTED_WITH_SET;
        } else if ("BlockTransferWithAction".equalsIgnoreCase(value)) {
            ret = Conformance.BLOCK_TRANSFER_WITH_ACTION;
        } else if ("BlockTransferWithGetOrRead".equalsIgnoreCase(value)) {
            ret = Conformance.BLOCK_TRANSFER_WITH_GET_OR_READ;
        } else if ("BlockTransferWithSetOrWrite".equalsIgnoreCase(value)) {
            ret = Conformance.BLOCK_TRANSFER_WITH_SET_OR_WRITE;
        } else if ("DataNotification".equalsIgnoreCase(value)) {
            ret = Conformance.DATA_NOTIFICATION;
        } else if ("EventNotification".equalsIgnoreCase(value)) {
            ret = Conformance.EVENT_NOTIFICATION;
        } else if ("GeneralBlockTransfer".equalsIgnoreCase(value)) {
            ret = Conformance.GENERAL_BLOCK_TRANSFER;
        } else if ("GeneralProtection".equalsIgnoreCase(value)) {
            ret = Conformance.GENERAL_PROTECTION;
        } else if ("Get".equalsIgnoreCase(value)) {
            ret = Conformance.GET;
        } else if ("InformationReport".equalsIgnoreCase(value)) {
            ret = Conformance.INFORMATION_REPORT;
        } else if ("MultipleReferences".equalsIgnoreCase(value)) {
            ret = Conformance.MULTIPLE_REFERENCES;
        } else if ("ParameterizedAccess".equalsIgnoreCase(value)) {
            ret = Conformance.PARAMETERIZED_ACCESS;
        } else if ("PriorityMgmtSupported".equalsIgnoreCase(value)) {
            ret = Conformance.PRIORITY_MGMT_SUPPORTED;
        } else if ("Read".equalsIgnoreCase(value)) {
            ret = Conformance.READ;
        } else if ("ReservedSeven".equalsIgnoreCase(value)) {
            ret = Conformance.RESERVED_SEVEN;
        } else if ("ReservedSix".equalsIgnoreCase(value)) {
            ret = Conformance.RESERVED_SIX;
        } else if ("ReservedZero".equalsIgnoreCase(value)) {
            ret = Conformance.RESERVED_ZERO;
        } else if ("SelectiveAccess".equalsIgnoreCase(value)) {
            ret = Conformance.SELECTIVE_ACCESS;
        } else if ("Set".equalsIgnoreCase(value)) {
            ret = Conformance.SET;
        } else if ("UnconfirmedWrite".equalsIgnoreCase(value)) {
            ret = Conformance.UN_CONFIRMED_WRITE;
        } else if ("Write".equalsIgnoreCase(value)) {
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
            str = "Normal";
            break;
        case NOT_FINISHED:
            str = "NotFinished";
            break;
        case USER_DEFINED:
            str = "UserDefined";
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return str;
    }

    static ReleaseResponseReason
            valueOfReleaseResponseReason(final String value) {
        ReleaseResponseReason ret;
        if ("Normal".equalsIgnoreCase(value)) {
            ret = ReleaseResponseReason.NORMAL;
        } else if ("NotFinished".equalsIgnoreCase(value)) {
            ret = ReleaseResponseReason.NOT_FINISHED;
        } else if ("UserDefined".equalsIgnoreCase(value)) {
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
            str = "Normal";
            break;
        case URGENT:
            str = "Urgent";
            break;
        case USER_DEFINED:
            str = "UserDefined";
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return str;
    }

    static ReleaseRequestReason
            valueOfReleaseRequestReason(final String value) {
        ReleaseRequestReason ret;
        if ("Normal".equalsIgnoreCase(value)) {
            ret = ReleaseRequestReason.NORMAL;
        } else if ("Urgent".equalsIgnoreCase(value)) {
            ret = ReleaseRequestReason.URGENT;
        } else if ("UserDefined".equalsIgnoreCase(value)) {
            ret = ReleaseRequestReason.USER_DEFINED;
        } else {
            throw new IllegalArgumentException(value);
        }
        return ret;
    }

}
