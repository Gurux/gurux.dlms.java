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

package gurux.dlms.secure;

import gurux.dlms.GXDLMSServer2;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSTcpUdpSetup;

/**
 * Implements secured DLMS server.
 * 
 * @author Gurux Ltd.
 */
public abstract class GXDLMSSecureServer2 extends GXDLMSServer2 {
    /**
     * Ciphering settings.
     */
    private GXCiphering ciphering;

    /**
     * Constructor.
     * 
     * @param ln
     *            Association logical name.
     * @param type
     *            Interface type.
     */
    public GXDLMSSecureServer2(final GXDLMSAssociationLogicalName ln,
            final InterfaceType type) {
        super(ln, type);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    /**
     * Constructor.
     * 
     * @param sn
     *            Association short name.
     * @param type
     *            Interface type.
     */
    public GXDLMSSecureServer2(final GXDLMSAssociationShortName sn,
            final InterfaceType type) {
        super(sn, type);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Association logical name.
     * @param hdlc
     *            HDLC settings.
     */
    public GXDLMSSecureServer2(final GXDLMSAssociationLogicalName ln,
            final GXDLMSHdlcSetup hdlc) {
        super(ln, hdlc);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    /**
     * Constructor.
     * 
     * @param sn
     *            Association short name.
     * @param hdlc
     *            HDLC settings.
     */
    public GXDLMSSecureServer2(final GXDLMSAssociationShortName sn,
            final GXDLMSHdlcSetup hdlc) {
        super(sn, hdlc);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Association logical name.
     * @param wrapper
     *            Wrapper settings.
     */
    public GXDLMSSecureServer2(final GXDLMSAssociationLogicalName ln,
            final GXDLMSTcpUdpSetup wrapper) {
        super(ln, wrapper);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    /**
     * Constructor.
     * 
     * @param sn
     *            Association short name.
     * @param wrapper
     *            Wrapper settings.
     */
    public GXDLMSSecureServer2(final GXDLMSAssociationShortName sn,
            final GXDLMSTcpUdpSetup wrapper) {
        super(sn, wrapper);
        ciphering = new GXCiphering("ABCDEFGH".getBytes());
        setCipher(ciphering);
    }

    public final GXCiphering getCiphering() {
        return ciphering;
    }

    /**
     * @return Key Encrypting Key, also known as Master key.
     */
    public final byte[] getKek() {
        return getSettings().getKek();
    }

    /**
     * @param value
     *            Key Encrypting Key, also known as Master key.
     */
    public final void setKek(final byte[] value) {
        getSettings().setKek(value);
    }
}
