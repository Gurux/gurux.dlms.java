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

import java.math.BigInteger;
import java.util.SortedMap;
import java.util.TreeMap;

import gurux.dlms.internal.CoAPClass;
import gurux.dlms.internal.CoAPClientError;
import gurux.dlms.internal.CoAPContentType;
import gurux.dlms.internal.CoAPMethod;
import gurux.dlms.internal.CoAPServerError;
import gurux.dlms.internal.CoAPSignaling;
import gurux.dlms.internal.CoAPSuccess;
import gurux.dlms.internal.CoAPType;

/**
 * CoAP settings contains CoAP settings.
 */
public class GXCoAPSettings {
    /**
     * CoAP version.
     */
    private byte version;

    /**
     * CoAP type.
     */
    private CoAPType type;

    /**
     * CoAP class code.
     */
    private CoAPClass classCode;
    /**
     * CoAP Method.
     */
    private CoAPMethod method;
    /**
     * CoAP Success.
     */
    private CoAPSuccess success;
    /**
     * CoAP client error.
     */
    private CoAPClientError clientError;
    /**
     * CoAP signaling.
     */
    private CoAPSignaling signaling;
    /**
     * CoAP message Id.
     */
    private int messageId;
    /**
     * CoAP token.
     */
    private BigInteger token;
    /**
     * Uri host.
     */
    private String host;
    /**
     * Uri Path.
     */
    private String path;
    /**
     * Uri port.
     */
    private int port;

    /**
     * CoAP server error.
     */
    private CoAPServerError serverError;
    /**
     * If none match.
     */
    private CoAPContentType ifNoneMatch;
    /**
     * Content format.
     */
    private CoAPContentType contentFormat;

    /**
     * Max age.
     */
    private int maxAge;
    /**
     * CoAP block number.
     */
    private int blockNumber;

    /**
     * Unknown options.
     */
    private SortedMap<Integer, Object> options;

    /**
     * Constructor.
     */
    public GXCoAPSettings() {
        type = CoAPType.CONFIRMABLE;
        classCode = CoAPClass.METHOD;
        method = CoAPMethod.NONE;
        success = CoAPSuccess.NONE;
        clientError = CoAPClientError.BAD_REQUEST;
        serverError = CoAPServerError.INTERNAL;
        signaling = CoAPSignaling.UNASSIGNED;
        ifNoneMatch = CoAPContentType.NONE;
        contentFormat = CoAPContentType.APPLICATION_OSCORE;
        options = new TreeMap<>();
    }

    /**
     * @return CoAP version.
     */
    public final byte getVersion() {
        return version;
    }

    /**
     * @param value
     *            CoAP version.
     */
    public final void setVersion(final byte value) {
        version = value;
    }

    /**
     * @return CoAP type.
     */
    public final CoAPType getType() {
        return type;
    }

    /**
     * @param value
     *            CoAP type.
     */
    public final void setType(final CoAPType value) {
        type = value;
    }

    /**
     * @return CoAP class code.
     */
    public CoAPClass getClassCode() {
        return classCode;
    }

    /**
     * @param value
     *            CoAP class code.
     */
    public final void setClassCode(final CoAPClass value) {
        classCode = value;
    }

    public final CoAPMethod getMethod() {
        return method;
    }

    public final void setMethod(final CoAPMethod value) {
        method = value;
    }

    public final CoAPSuccess getSuccess() {
        return success;
    }

    public final void setSuccess(final CoAPSuccess value) {
        success = value;
    }

    public final CoAPClientError getClientError() {
        return clientError;
    }

    public final void setClientError(final CoAPClientError value) {
        clientError = value;
    }

    public final CoAPServerError getServerError() {
        return serverError;
    }

    public final void setServerError(final CoAPServerError value) {
        serverError = value;
    }

    public final CoAPSignaling getSignaling() {
        return signaling;
    }

    public final void setSignaling(final CoAPSignaling value) {
        signaling = value;
    }

    /**
     * @return CoAP message Id.
     */
    public final int getMessageId() {
        return messageId;
    }

    /**
     * @param value
     *            CoAP message Id.
     */
    public final void setMessageId(final int value) {
        messageId = value;
    }

    /**
     * @return CoAP token.
     */
    public final BigInteger getToken() {
        return token;
    }

    /**
     * @param value
     *            CoAP token.
     */
    public final void setToken(final BigInteger value) {
        token = value;
    }

    /**
     * @return Uri host.
     */
    public final String getHost() {
        return host;
    }

    /**
     * @param value
     *            Uri host.
     */
    public final void setHost(final String value) {
        host = value;
    }

    /**
     * @return Uri Path.
     */
    public final String getPath() {
        return path;
    }

    /**
     * @param value
     *            Uri Path.
     */
    public final void setPath(final String value) {
        path = value;
    }

    /**
     * @return Uri port.
     */
    public final int getPort() {
        return port;
    }

    /**
     * @param value
     *            Uri port.
     */
    public final void setPort(final int value) {
        port = value;
    }

    /**
     * @return If none match.
     */
    public final CoAPContentType getIfNoneMatch() {
        return ifNoneMatch;
    }

    /**
     * @param value
     *            If none match.
     */
    public final void setIfNoneMatch(final CoAPContentType value) {
        ifNoneMatch = value;
    }

    /**
     * @return Content format.
     */
    public final CoAPContentType getContentFormat() {
        return contentFormat;
    }

    /**
     * @param value
     *            Content format.
     */
    public final void setContentFormat(final CoAPContentType value) {
        contentFormat = value;
    }

    /**
     * @return Max age.
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * @param value
     *            Max age.
     */
    public final void setMaxAge(final int value) {
        maxAge = value;
    }

    /**
     * @return CoAP block number.
     */
    public final int getBlockNumber() {
        return blockNumber;
    }

    /**
     * @param value
     *            CoAP block number.
     */
    public final void setBlockNumber(int value) {
        blockNumber = value;
    }

    /**
     * @return Unknown options.
     */
    public final SortedMap<Integer, Object> getOptions() {
        return options;
    }

    /**
     * @param value
     *            Unknown options.
     */
    public final void setOptions(SortedMap<Integer, Object> value) {
        options = value;
    }

    /**
     * Reset all values.
     */
    public final void reset() {
        version = 0;
        type = CoAPType.CONFIRMABLE;
        classCode = CoAPClass.METHOD;
        method = CoAPMethod.NONE;
        success = CoAPSuccess.NONE;
        clientError = CoAPClientError.BAD_REQUEST;
        serverError = CoAPServerError.INTERNAL;
        signaling = CoAPSignaling.UNASSIGNED;
        messageId = 0;
        token = BigInteger.valueOf(0);
        host = null;
        path = null;
        port = 0;
        ifNoneMatch = CoAPContentType.NONE;
        contentFormat = CoAPContentType.NONE;
        maxAge = 0;
        blockNumber = 0;
        options.clear();
    }
}