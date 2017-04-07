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

import gurux.dlms.enums.Access;
import gurux.dlms.enums.ApplicationReference;
import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.Definition;
import gurux.dlms.enums.ExceptionServiceError;
import gurux.dlms.enums.HardwareResource;
import gurux.dlms.enums.Initiate;
import gurux.dlms.enums.LoadDataSet;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.enums.StateError;
import gurux.dlms.enums.Task;
import gurux.dlms.enums.VdeStateError;

/**
 * DLMS specific exception class that has error description available from
 * GetDescription method.
 */
public class GXDLMSException extends RuntimeException {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    private ConfirmedServiceError confirmedServiceError;
    private ServiceError serviceError;
    private byte serviceErrorValue;

    private AssociationResult result = AssociationResult.ACCEPTED;
    private SourceDiagnostic diagnostic = SourceDiagnostic.NONE;
    private int errorCode;
    private StateError stateError;
    private ExceptionServiceError exceptionServiceError;

    public GXDLMSException(final int errCode) {
        super(GXDLMS.getDescription(errCode));
        setErrorCode(errCode);
    }

    public GXDLMSException(final String message) {
        super(message);
    }

    /**
     * Constructor for AARE error.
     */
    GXDLMSException(final AssociationResult forResult,
            final SourceDiagnostic forDiagnostic) {
        super("Connection is " + getResult(forResult) + "\r\n"
                + getDiagnostic(forDiagnostic));
    }

    /**
     * Constructor for Confirmed ServiceError.
     * 
     * @param service
     * @param type
     * @param value
     */
    GXDLMSException(final ConfirmedServiceError service,
            final ServiceError type, final int value) {
        super("ServiceError " + getConfirmedServiceError(service)
                + " exception. " + getServiceError(type) + " "
                + getServiceErrorValue(type, (byte) value));
        confirmedServiceError = service;
        serviceError = type;
        serviceErrorValue = (byte) value;
    }

    /**
     * @param stateErr
     *            State error.
     * @param serviceErr
     *            Service error.
     */
    GXDLMSException(final StateError stateErr,
            final ExceptionServiceError serviceErr) {
        super("Meter returns " + getStateError(stateErr) + " exception. "
                + getServiceError(serviceErr));
        stateError = stateErr;
        exceptionServiceError = serviceErr;
    }

    /**
     * Gets state error description.
     * 
     * @param stateError
     *            State error enumerator value.
     * @return State error as an string.
     */
    private static String getStateError(final StateError stateError) {
        switch (stateError) {
        case SERVICE_NOT_ALLOWED:
            return "Service not allowed";
        case SERVICE_UNKNOWN:
            return "Service unknown";
        default:
        }
        return "";
    }

    private static String
            getConfirmedServiceError(final ConfirmedServiceError stateError) {
        switch (stateError) {
        case INITIATE_ERROR:
            return "Initiate Error";
        case READ:
            return "Read";
        case WRITE:
            return "Write";
        default:
            break;
        }
        return "";
    }

    private static String getServiceError(final ServiceError error) {
        switch (error) {
        case APPLICATION_REFERENCE:
            return "Application reference";
        case HARDWARE_RESOURCE:
            return "Hardware resource";
        case VDE_STATE_ERROR:
            return "Vde state error";
        case SERVICE:
            return "Service";
        case DEFINITION:
            return "Definition";
        case ACCESS:
            return "Access";
        case INITIATE:
            return "Initiate";
        case LOAD_DATASET:
            return "Load dataset";
        case TASK:
            return "Task";
        case OTHER_ERROR:
            return "Other Error";
        default:
            break;
        }
        return "";
    }

    private static String getServiceErrorValue(final ServiceError error,
            final byte value) {
        switch (error) {
        case APPLICATION_REFERENCE:
            return ApplicationReference.forValue(value).toString();
        case HARDWARE_RESOURCE:
            return HardwareResource.forValue(value).toString();
        case VDE_STATE_ERROR:
            return VdeStateError.forValue(value).toString();
        case SERVICE:
            return Service.forValue(value).toString();
        case DEFINITION:
            return Definition.forValue(value).toString();
        case ACCESS:
            return Access.forValue(value).toString();
        case INITIATE:
            return Initiate.forValue(value).toString();
        case LOAD_DATASET:
            return LoadDataSet.forValue(value).toString();
        case TASK:
            return Task.forValue(value).toString();
        case OTHER_ERROR:
            return String.valueOf(value);
        default:
            break;
        }
        return "";
    }

    /**
     * Gets service error description.
     * 
     * @param serviceError
     *            Service error enumerator value.
     * @return Service error as an string.
     */
    private static String
            getServiceError(final ExceptionServiceError serviceError) {
        switch (serviceError) {
        case OPERATION_NOT_POSSIBLE:
            return "Operation not possible";
        case SERVICE_NOT_SUPPORTED:
            return "Service not supported";
        case OTHER_REASON:
            return "Other reason";
        default:
        }
        return "";
    }

    /**
     * Get result as a string.
     * 
     * @param result
     *            Enumeration value of AssociationResult.
     * @return String description of AssociationResult.
     */
    private static String getResult(final AssociationResult result) {
        if (result == AssociationResult.PERMANENT_REJECTED) {
            return "permanently rejected";
        }
        if (result == AssociationResult.TRANSIENT_REJECTED) {
            return "transient rejected";
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Get diagnostic as a string.
     * 
     * @param value
     *            Enumeration value of SourceDiagnostic.
     * @return String description of SourceDiagnostic.
     */
    private static String getDiagnostic(final SourceDiagnostic value) {
        if (value == SourceDiagnostic.NO_REASON_GIVEN) {
            return "No reason is given.";
        }
        if (value == SourceDiagnostic.NOT_SUPPORTED) {
            return "The application context name is not supported.";
        }
        if (value == SourceDiagnostic.NOT_RECOGNISED) {
            return "The authentication mechanism name is not recognized.";
        }
        if (value == SourceDiagnostic.MECHANISM_NAME_REGUIRED) {
            return "Authentication mechanism name is required.";
        }
        if (value == SourceDiagnostic.AUTHENTICATION_FAILURE) {
            return "Authentication failure.";
        }
        if (value == SourceDiagnostic.AUTHENTICATION_REQUIRED) {
            return "Authentication is required.";
        }
        throw new UnsupportedOperationException();
    }

    /**
     * @return Error code.
     */
    public final int getErrorCode() {
        return errorCode;
    }

    /**
     * @param value
     *            Error code.
     */
    public final void setErrorCode(final int value) {
        errorCode = value;
    }

    /**
     * @return Association Result in AARE message.
     */
    public final AssociationResult getResult() {
        return result;
    }

    /**
     * @param value
     *            Association Result in AARE message.
     */
    final void setResult(final AssociationResult value) {
        result = value;
    }

    /**
     * @return Diagnostic code in AARE message.
     */
    public final SourceDiagnostic getDiagnostic() {
        return diagnostic;
    }

    /**
     * @param value
     *            Diagnostic code in AARE message.
     */
    final void setDiagnostic(final SourceDiagnostic value) {
        diagnostic = value;
    }

    /**
     * @return State error.
     */
    public final StateError getStateError() {
        return stateError;
    }

    /**
     * @return Service error.
     */
    public final ExceptionServiceError getExceptionServiceError() {
        return exceptionServiceError;
    }

    /**
     * @return the Confirmed service error.
     */
    public final ConfirmedServiceError getConfirmedServiceError() {
        return confirmedServiceError;
    }

    /**
     * @return the serviceErrorValue
     */
    public final byte getServiceErrorValue() {
        return serviceErrorValue;
    }

    /**
     * @return the serviceError
     */
    public final ServiceError getServiceError() {
        return serviceError;
    }

}