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

package gurux.dlms.manufacturersettings;

import java.util.ArrayList;
import java.util.List;

import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.Security;

public class GXManufacturer {
    private InactivityMode inactivityMode = InactivityMode.KEEPALIVE;
    private boolean useIEC47;
    private boolean forceInactivity;
    private boolean useLogicalNameReferencing;
    private String identification;
    private GXObisCodeCollection obisCodes;
    private String name;
    private java.util.ArrayList<GXAuthentication> settings;
    private java.util.ArrayList<GXServerAddress> serverSettings;
    private int keepAliveInterval;
    private StartProtocolType startProtocol = StartProtocolType.IEC;

    private String webAddress;
    private String info;
    private Security security = Security.NONE;
    private byte[] systemTitle;
    private byte[] serverSystemTitle;
    private byte[] blockCipherKey;
    private byte[] authenticationKey;
    private List<InterfaceType> supporterdInterfaces;

    /**
     * Constructor.
     */
    public GXManufacturer() {
        setInactivityMode(InactivityMode.KEEPALIVE);
        setStartProtocol(StartProtocolType.IEC);
        obisCodes = new GXObisCodeCollection();
        setSettings(new java.util.ArrayList<GXAuthentication>());
        serverSettings = new java.util.ArrayList<GXServerAddress>();
        setKeepAliveInterval(40000);
        supporterdInterfaces = new ArrayList<InterfaceType>();
    }

    /**
     * Manufacturer Identification. Device returns manufacturer ID when
     * connection to the meter is made.
     * 
     * @return Manufacturer Identification.
     */
    public final String getIdentification() {
        return identification;
    }

    /**
     * @param value
     *            Manufacturer Identification.
     */
    public final void setIdentification(final String value) {
        identification = value;
    }

    /**
     * @return Real name of the manufacturer.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param value
     *            Real name of the manufacturer.
     */
    public final void setName(final String value) {
        name = value;
    }

    /**
     * @return Is Logical name referencing used.
     */
    public final boolean getUseLogicalNameReferencing() {
        return useLogicalNameReferencing;
    }

    /**
     * @param value
     *            Is Logical name referencing used.
     */
    public final void setUseLogicalNameReferencing(final boolean value) {
        useLogicalNameReferencing = value;
    }

    /**
     * @return Is Keep alive message used.
     */
    public final InactivityMode getInactivityMode() {
        return inactivityMode;
    }

    /**
     * @param value
     *            Is Keep alive message used.
     */
    public final void setInactivityMode(final InactivityMode value) {
        inactivityMode = value;
    }

    /**
     * @return Is Keep alive message forced for network connection as well.
     */
    public final boolean getForceInactivity() {
        return forceInactivity;
    }

    /**
     * @param value
     *            Is Keep alive message forced for network connection as well.
     */
    public final void setForceInactivity(final boolean value) {
        forceInactivity = value;
    }

    /**
     * @return Collection of custom OBIS codes.
     */
    public final GXObisCodeCollection getObisCodes() {
        return obisCodes;
    }

    /**
     * @return Is IEC 62056-47 supported.
     */
    public final boolean getUseIEC47() {
        return useIEC47;
    }

    /**
     * @param value
     *            Is IEC 62056-47 supported.
     */
    public final void setUseIEC47(final boolean value) {
        useIEC47 = value;
    }

    /**
     * @return Start protocol.
     */
    public final StartProtocolType getStartProtocol() {
        return startProtocol;
    }

    /**
     * @param value
     *            Start protocol.
     */
    public final void setStartProtocol(final StartProtocolType value) {
        startProtocol = value;
    }

    /**
     * @return Keep Alive interval.
     */
    public final int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    /**
     * @param value
     *            Keep Alive interval.
     */
    public final void setKeepAliveInterval(final int value) {
        keepAliveInterval = value;
    }

    /**
     * @return Initialize settings.
     */
    public final java.util.ArrayList<GXAuthentication> getSettings() {
        return settings;
    }

    /**
     * @param value
     *            Initialize settings.
     */
    public final void setSettings(final java.util.ArrayList<GXAuthentication> value) {
        settings = value;
    }

    /**
     * @return Server settings
     */
    public final java.util.ArrayList<GXServerAddress> getServerSettings() {
        return serverSettings;
    }

    public final GXServerAddress getServer(final HDLCAddressType type) {
        for (GXServerAddress it : this.getServerSettings()) {
            if (it.getHDLCAddress() == type) {
                return it;
            }
        }
        return null;
    }

    /**
     * Get authentication settings.
     * 
     * @param authentication
     *            Authentication type.
     * @return Authentication settings.
     */
    public final GXAuthentication getAuthentication(final Authentication authentication) {
        for (GXAuthentication it : getSettings()) {
            if (it.getType() == authentication) {
                return it;
            }
        }
        return null;
    }

    /**
     * @return Web address where is more information.
     */
    public String getWebAddress() {
        return webAddress;
    }

    /**
     * @param value
     *            Web address where is more information.
     */
    public void setWebAddress(final String value) {
        this.webAddress = value;
    }

    /**
     * @return Additional info.
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param value
     *            Additional info.
     */
    public void setInfo(final String value) {
        info = value;
    }

    /**
     * @return Used GMAC Security type.
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * @param value
     *            Used GMAC Security type.
     */
    public void setSecurity(final Security value) {
        security = value;
    }

    /**
     * @return System Title.
     */
    public byte[] getSystemTitle() {
        return systemTitle;
    }

    /**
     * @param value
     *            System Title.
     */
    public void setSystemTitle(final byte[] value) {
        systemTitle = value;
    }

    /**
     * @return Server System Title.
     */
    public byte[] getServerSystemTitle() {
        return serverSystemTitle;
    }

    /**
     * @param value
     *            Server System Title.
     */
    public void setServerSystemTitle(final byte[] value) {
        this.serverSystemTitle = value;
    }

    /**
     * @return GMAC block cipher key.
     */
    public byte[] getBlockCipherKey() {
        return blockCipherKey;
    }

    /**
     * @param value
     *            GMAC block cipher key.
     */
    public void setBlockCipherKey(final byte[] value) {
        this.blockCipherKey = value;
    }

    /**
     * @return GMAC authentication key.
     */
    public byte[] getAuthenticationKey() {
        return authenticationKey;
    }

    /**
     * @param value
     *            GMAC authentication key.
     */
    public void setAuthenticationKey(final byte[] value) {
        this.authenticationKey = value;
    }

    /**
     * @return Supported interface types.
     */
    public List<InterfaceType> getSupporterdInterfaces() {
        return supporterdInterfaces;
    }
}