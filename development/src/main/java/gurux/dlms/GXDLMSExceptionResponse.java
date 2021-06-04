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

package gurux.dlms;

import gurux.dlms.enums.ExceptionServiceError;
import gurux.dlms.enums.ExceptionStateError;

/**
 * DLMS specific exception class that has error description available from
 * GetDescription method.
 */
public class GXDLMSExceptionResponse extends RuntimeException {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private final ExceptionStateError exceptionStateError;
	private final ExceptionServiceError exceptionServiceError;
	private final Object exceptionResponseValue;

	/*
	 * Constructor for Confirmed ServiceError.
	 */
	public GXDLMSExceptionResponse(final ExceptionStateError error, final ExceptionServiceError type,
			final Object value) {
		super(getStateError(error) + ". " + getServiceError(type));
		exceptionStateError = error;
		exceptionServiceError = type;
		exceptionResponseValue = value;
	}

	/**
	 * @return the state error.
	 */
	public final ExceptionStateError getStateError() {
		return exceptionStateError;
	}

	/**
	 * @return the service error.
	 */
	public final ExceptionServiceError getExceptionServiceError() {
		return exceptionServiceError;
	}

	/**
	 * @return the optional value field.
	 */
	public final Object getValue() {
		return exceptionResponseValue;
	}

	/**
	 * Gets state error description.
	 * 
	 * @param stateError State error enumerator value.
	 * @return State error as an string.
	 */
	private static String getStateError(final ExceptionStateError stateError) {
		switch (stateError) {
		case SERVICE_NOT_ALLOWED:
			return "Service not allowed";
		case SERVICE_UNKNOWN:
			return "Service unknown";
		default:
		}
		return "";
	}

	/**
	 * Gets service error description.
	 * 
	 * @param serviceError Service error enumerator value.
	 * @return Service error as an string.
	 */
	private static String getServiceError(final ExceptionServiceError serviceError) {
		switch (serviceError) {
		case OPERATION_NOT_POSSIBLE:
			return "Operation not possible";
		case SERVICE_NOT_SUPPORTED:
			return "Service not supported";
		case OTHER_REASON:
			return "Other reason";
		case PDU_TOO_LONG:
			return "PDU is too long";
		case DECIPHERING_ERROR:
			return "Ciphering failed";
		case INVOCATION_COUNTER_ERROR:
			return "Invocation counter is invalid.";
		default:
		}
		return "";
	}

}