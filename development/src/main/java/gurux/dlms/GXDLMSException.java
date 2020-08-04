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

import gurux.dlms.enums.AcseServiceProvider;
import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.SourceDiagnostic;

/**
 * DLMS specific exception class that has error description available from
 * GetDescription method.
 */
public class GXDLMSException extends RuntimeException {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    private AssociationResult result = AssociationResult.ACCEPTED;
    private int diagnostic = 0;
    private int errorCode;

    public GXDLMSException(final int errCode) {
        super(GXDLMS.getDescription(errCode));
        setErrorCode(errCode);
    }

    public GXDLMSException(final String message) {
        super(message);
    }

    /*
     * Constructor for AARE error.
     */
    GXDLMSException(final AssociationResult forResult,
            final SourceDiagnostic forDiagnostic) {
        super("Connection is " + getResult(forResult) + "\r\n"
                + getDiagnostic(forDiagnostic));
        result = forResult;
        diagnostic = forDiagnostic.getValue();
    }

    /*
     * Constructor for AARE error.
     */
    GXDLMSException(final AssociationResult forResult,
            final AcseServiceProvider forDiagnostic) {
        super("Connection is " + getResult(forResult) + "\r\n"
                + getDiagnostic(forDiagnostic));
        result = forResult;
        diagnostic = forDiagnostic.getValue();
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

    /*
     * Get diagnostic as a string.
     */
    private static String getDiagnostic(AcseServiceProvider diagnostic) {
        String str;
        switch (diagnostic) {
        case NONE:
            str = "None.";
            break;
        case NO_REASON_GIVEN:
            str = "No reason given.";
            break;
        case NO_COMMON_ACSE_VERSION:
            str = "No Common Acse version.";
            break;
        default:
            str = "Unknown diagnostic error.";
            break;
        }
        return str;
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
     * @return Diagnostic code in AARE message.
     */
    public final int getDiagnostic() {
        return diagnostic;
    }
}