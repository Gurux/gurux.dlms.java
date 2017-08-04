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
// Copyright =c) Gurux Ltd
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

package gurux.dlms.enums;

/**
 * DLMS commands.
 */
public final class Command {
    /**
     * Constructor.
     */
    private Command() {

    }

    /**
     * No command to execute.
     */
    public static final int NONE = 0;

    /**
     * Initiate request.
     */
    public static final int INITIATE_REQUEST = 0x1;

    /**
     * Initiate response.
     */
    public static final int INITIATE_RESPONSE = 0x8;

    /**
     * Read request.
     */
    public static final int READ_REQUEST = 0x5;

    /**
     * Read response.
     */
    public static final int READ_RESPONSE = 0xC;

    /**
     * Write request.
     */
    public static final int WRITE_REQUEST = 0x6;

    /**
     * Write response.
     */
    public static final int WRITE_RESPONSE = 0xD;

    /**
     * Get request.
     */
    public static final int GET_REQUEST = 0xC0;

    /**
     * Get response.
     */
    public static final int GET_RESPONSE = 0xC4;

    /**
     * Set request.
     */
    public static final int SET_REQUEST = 0xC1;

    /**
     * Set response.
     */
    public static final int SET_RESPONSE = 0xC5;

    /**
     * Action request.
     */
    public static final int METHOD_REQUEST = 0xC3;

    /**
     * Action response.
     */
    public static final int METHOD_RESPONSE = 0xC7;

    /**
     * HDLC Disconnect Mode.
     */
    public static final int DISCONNECT_MODE = 0x1F;
    /**
     * HDLC Unacceptable frame.
     */
    public static final int UNACCEPTABLE_FRAME = 0x97;

    /**
     * HDLC SNRM request.
     */
    public static final int SNRM = 0x93;

    /**
     * HDLC UA request.
     */
    public static final int UA = 0x73;

    /**
     * AARQ request.
     */
    public static final int AARQ = 0x60;

    /**
     * AARE request.
     */
    public static final int AARE = 0x61;

    /**
     * Disconnect request for HDLC framing.
     */
    public static final int DISCONNECT_REQUEST = 0x53;

    /**
     * Release request.
     */
    public static final int RELEASE_REQUEST = 0x62;

    /**
     * Disconnect response.
     */
    public static final int RELEASE_RESPONSE = 0x63;

    /**
     * Confirmed Service Error.
     */
    public static final int CONFIRMED_SERVICE_ERROR = 0x0E;

    /**
     * Exception Response.
     */
    public static final int EXCEPTION_RESPONSE = 0xD8;

    /**
     * General Block Transfer.
     */
    public static final int GENERAL_BLOCK_TRANSFER = 0xE0;

    /**
     * Access Request.
     */
    public static final int ACCESS_REQUEST = 0xD9;

    /**
     * Access Response.
     */
    public static final int ACCESS_RESPONSE = 0xDA;

    /**
     * Data Notification request.
     */
    public static final int DATA_NOTIFICATION = 0x0F;

    /**
     * Glo get request.
     */
    public static final int GLO_GET_REQUEST = 0xC8;

    /**
     * Glo get response.
     */
    public static final int GLO_GET_RESPONSE = 0xCC;

    /**
     * Glo set request.
     */
    public static final int GLO_SET_REQUEST = 0xC9;

    /**
     * Glo set response.
     */
    public static final int GLO_SET_RESPONSE = 0xCD;

    /**
     * Glo event notification request.
     */
    public static final int GLO_EVENT_NOTIFICATION_REQUEST = 0xCA;

    /**
     * Glo method request.
     */
    public static final int GLO_METHOD_REQUEST = 0xCB;

    /**
     * Glo method response.
     */
    public static final int GLO_METHOD_RESPONSE = 0xCF;

    /**
     * Glo Initiate request.
     */
    public static final int GLO_INITIATE_REQUEST = 0x21;
    /**
     * Glo read request.
     */
    public static final int GLO_READ_REQUEST = 37;

    /**
     * Glo write request.
     */
    public static final int GLO_WRITE_REQUEST = 38;
    /**
     * Glo Initiate response.
     */
    public static final int GLO_INITIATE_RESPONSE = 40;
    /**
     * Glo read response.
     */
    public static final int GLO_READ_RESPONSE = 44;
    /**
     * Glo write response.
     */
    public static final int GLO_WRITE_RESPONSE = 45;

    /**
     * General GLO ciphering.
     */
    public static final int GENERAL_GLO_CIPHERING = 0xDB;

    /**
     * General DED ciphering.
     */
    public static final int GENERAL_DED_CIPHERING = 0xDC;

    /**
     * General ciphering.
     */
    public static final int GENERAL_CIPHERING = 0xDD;

    /**
     * Information Report request.
     */
    public static final int INFORMATION_REPORT = 0x18;

    /**
     * Event Notification request.
     */
    public static final int EVENT_NOTIFICATION = 0xC2;

    public static String toString(final int value) {
        String str;
        switch (value) {
        case Command.NONE:
            str = "None";
            break;
        case Command.INITIATE_REQUEST:
            str = "InitiateRequest";
            break;
        case Command.INITIATE_RESPONSE:
            str = "InitiateResponse";
            break;
        case Command.READ_REQUEST:
            str = "ReadRequest";
            break;
        case Command.READ_RESPONSE:
            str = "ReadResponse";
            break;
        case Command.WRITE_REQUEST:
            str = "WriteRequest";
            break;
        case Command.WRITE_RESPONSE:
            str = "WriteResponse";
            break;
        case Command.GET_REQUEST:
            str = "GetRequest";
            break;
        case Command.GET_RESPONSE:
            str = "GetResponse";
            break;
        case Command.SET_REQUEST:
            str = "SetRequest";
            break;
        case Command.SET_RESPONSE:
            str = "SetResponse";
            break;
        case Command.METHOD_REQUEST:
            str = "MethodRequest";
            break;
        case Command.METHOD_RESPONSE:
            str = "MethodResponse";
            break;
        case Command.UNACCEPTABLE_FRAME:
            str = "UnacceptableFrame";
            break;
        case Command.SNRM:
            str = "Snrm";
            break;
        case Command.UA:
            str = "Ua";
            break;
        case Command.AARQ:
            str = "Aarq";
            break;
        case Command.AARE:
            str = "Aare";
            break;
        case Command.DISCONNECT_REQUEST:
            str = "Disc";
            break;
        case Command.RELEASE_REQUEST:
            str = "DisconnectRequest";
            break;
        case Command.RELEASE_RESPONSE:
            str = "DisconnectResponse";
            break;
        case Command.CONFIRMED_SERVICE_ERROR:
            str = "ConfirmedServiceError";
            break;
        case Command.EXCEPTION_RESPONSE:
            str = "ExceptionResponse";
            break;
        case Command.GENERAL_BLOCK_TRANSFER:
            str = "GeneralBlockTransfer";
            break;
        case Command.ACCESS_REQUEST:
            str = "AccessRequest";
            break;
        case Command.ACCESS_RESPONSE:
            str = "AccessResponse";
            break;
        case Command.DATA_NOTIFICATION:
            str = "DataNotification";
            break;
        case Command.GLO_GET_REQUEST:
            str = "GloGetRequest";
            break;
        case Command.GLO_GET_RESPONSE:
            str = "GloGetResponse";
            break;
        case Command.GLO_SET_REQUEST:
            str = "GloSetRequest";
            break;
        case Command.GLO_SET_RESPONSE:
            str = "GloSetResponse";
            break;
        case Command.GLO_EVENT_NOTIFICATION_REQUEST:
            str = "GloEventNotificationRequest";
            break;
        case Command.GLO_METHOD_REQUEST:
            str = "GloMethodRequest";
            break;
        case Command.GLO_METHOD_RESPONSE:
            str = "GloMethodResponse";
            break;
        case Command.GLO_INITIATE_REQUEST:
            str = "GloInitiateRequest";
            break;
        case Command.GLO_READ_REQUEST:
            str = "GloReadRequest";
            break;
        case Command.GLO_WRITE_REQUEST:
            str = "GloWriteRequest";
            break;
        case Command.GLO_INITIATE_RESPONSE:
            str = "GloInitiateResponse";
            break;
        case Command.GLO_READ_RESPONSE:
            str = "GloReadResponse";
            break;
        case Command.GLO_WRITE_RESPONSE:
            str = "GloWriteResponse";
            break;
        case Command.GENERAL_GLO_CIPHERING:
            str = "GeneralGloCiphering";
            break;
        case Command.GENERAL_DED_CIPHERING:
            str = "GeneralDedCiphering";
            break;
        case Command.GENERAL_CIPHERING:
            str = "GeneralCiphering";
            break;
        case Command.INFORMATION_REPORT:
            str = "InformationReport";
            break;
        case Command.EVENT_NOTIFICATION:
            str = "EventNotification";
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return str;
    }

    public static int valueOf(final String value) {
        int ret;
        if ("None".equalsIgnoreCase(value)) {
            ret = Command.NONE;
        } else if ("InitiateRequest".equalsIgnoreCase(value)) {
            ret = Command.INITIATE_REQUEST;
        } else if ("InitiateResponse".equalsIgnoreCase(value)) {
            ret = Command.INITIATE_RESPONSE;
        } else if ("ReadRequest".equalsIgnoreCase(value)) {
            ret = Command.READ_REQUEST;
        } else if ("ReadResponse".equalsIgnoreCase(value)) {
            ret = Command.READ_RESPONSE;
        } else if ("WriteRequest".equalsIgnoreCase(value)) {
            ret = Command.WRITE_REQUEST;
        } else if ("WriteRequest".equalsIgnoreCase(value)) {
            ret = Command.WRITE_RESPONSE;
        } else if ("WriteResponse".equalsIgnoreCase(value)) {
            ret = Command.WRITE_RESPONSE;
        } else if ("GetRequest".equalsIgnoreCase(value)) {
            ret = Command.GET_REQUEST;
        } else if ("GetResponse".equalsIgnoreCase(value)) {
            ret = Command.GET_RESPONSE;
        } else if ("SetRequest".equalsIgnoreCase(value)) {
            ret = Command.SET_REQUEST;
        } else if ("SetResponse".equalsIgnoreCase(value)) {
            ret = Command.SET_RESPONSE;
        } else if ("MethodRequest".equalsIgnoreCase(value)) {
            ret = Command.METHOD_REQUEST;
        } else if ("MethodResponse".equalsIgnoreCase(value)) {
            ret = Command.METHOD_RESPONSE;
        } else if ("UnacceptableFrame".equalsIgnoreCase(value)) {
            ret = Command.UNACCEPTABLE_FRAME;
        } else if ("Snrm".equalsIgnoreCase(value)) {
            ret = Command.SNRM;
        } else if ("Ua".equalsIgnoreCase(value)) {
            ret = Command.UA;
        } else if ("Aarq".equalsIgnoreCase(value)) {
            ret = Command.AARQ;
        } else if ("Aare".equalsIgnoreCase(value)) {
            ret = Command.AARE;
        } else if ("Disc".equalsIgnoreCase(value)) {
            ret = Command.DISCONNECT_REQUEST;
        } else if ("DisconnectRequest".equalsIgnoreCase(value)) {
            ret = Command.RELEASE_REQUEST;
        } else if ("DisconnectResponse".equalsIgnoreCase(value)) {
            ret = Command.RELEASE_RESPONSE;
        } else if ("ConfirmedServiceError".equalsIgnoreCase(value)) {
            ret = Command.CONFIRMED_SERVICE_ERROR;
        } else if ("ExceptionResponse".equalsIgnoreCase(value)) {
            ret = Command.EXCEPTION_RESPONSE;
        } else if ("GeneralBlockTransfer".equalsIgnoreCase(value)) {
            ret = Command.GENERAL_BLOCK_TRANSFER;
        } else if ("AccessRequest".equalsIgnoreCase(value)) {
            ret = Command.ACCESS_REQUEST;
        } else if ("AccessResponse".equalsIgnoreCase(value)) {
            ret = Command.ACCESS_RESPONSE;
        } else if ("DataNotification".equalsIgnoreCase(value)) {
            ret = Command.DATA_NOTIFICATION;
        } else if ("GloGetRequest".equalsIgnoreCase(value)) {
            ret = Command.GLO_GET_REQUEST;
        } else if ("GloGetResponse".equalsIgnoreCase(value)) {
            ret = Command.GLO_GET_RESPONSE;
        } else if ("GloSetRequest".equalsIgnoreCase(value)) {
            ret = Command.GLO_SET_REQUEST;
        } else if ("GloSetResponse".equalsIgnoreCase(value)) {
            ret = Command.GLO_SET_RESPONSE;
        } else if ("GloEventNotificationRequest".equalsIgnoreCase(value)) {
            ret = Command.GLO_EVENT_NOTIFICATION_REQUEST;
        } else if ("GloMethodRequest".equalsIgnoreCase(value)) {
            ret = Command.GLO_METHOD_REQUEST;
        } else if ("GloMethodResponse".equalsIgnoreCase(value)) {
            ret = Command.GLO_METHOD_RESPONSE;
        } else if ("GloInitiateRequest".equalsIgnoreCase(value)) {
            ret = Command.GLO_INITIATE_REQUEST;
        } else if ("GloReadRequest".equalsIgnoreCase(value)) {
            ret = Command.GLO_READ_REQUEST;
        } else if ("GloWriteRequest".equalsIgnoreCase(value)) {
            ret = Command.GLO_WRITE_REQUEST;
        } else if ("GloInitiateResponse".equalsIgnoreCase(value)) {
            ret = Command.GLO_INITIATE_RESPONSE;
        } else if ("GloReadResponse".equalsIgnoreCase(value)) {
            ret = Command.GLO_READ_RESPONSE;
        } else if ("GloWriteResponse".equalsIgnoreCase(value)) {
            ret = Command.GLO_WRITE_RESPONSE;
        } else if ("GeneralGloCiphering".equalsIgnoreCase(value)) {
            ret = Command.GENERAL_GLO_CIPHERING;
        } else if ("GeneralDedCiphering".equalsIgnoreCase(value)) {
            ret = Command.GENERAL_DED_CIPHERING;
        } else if ("GeneralCiphering".equalsIgnoreCase(value)) {
            ret = Command.GENERAL_CIPHERING;
        } else if ("InformationReport".equalsIgnoreCase(value)) {
            ret = Command.INFORMATION_REPORT;
        } else if ("EventNotification".equalsIgnoreCase(value)) {
            ret = Command.EVENT_NOTIFICATION;
        } else {
            throw new IllegalArgumentException(value);
        }
        return ret;
    }
}