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

import java.util.ArrayList;
import java.util.List;

import gurux.dlms.enums.Command;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.plc.GXDLMSPlcMeterInfo;
import gurux.dlms.plc.GXDLMSPlcRegister;
import gurux.dlms.plc.enums.PlcDestinationAddress;
import gurux.dlms.plc.enums.PlcHdlcSourceAddress;
import gurux.dlms.plc.enums.PlcSourceAddress;

/**
 * PLC communication settings.
 */
public class GXPlcSettings {
    private byte[] systemTitle;
    private GXDLMSSettings settings;
    /**
     * Initial credit (IC) tells how many times the frame must be repeated.
     * Maximum value is 7.
     */
    private int initialCredit;
    /**
     * The current credit (CC) initial value equal to IC and automatically
     * decremented by the MAC layer after each repetition. Maximum value is 7.
     */
    private int currentCredit;

    /**
     * Delta credit (DC) is used by the system management application entity
     * (SMAE) of the Client for credit management, while it has no meaning for a
     * Server or a REPEATER. It represents the difference(IC-CC) of the last
     * communication originated by the system identified by the DA address to
     * the system identified by the SA address. Maximum value is 3.
     */
    private byte deltaCredit;
    /**
     * Source MAC address.
     */
    private int macSourceAddress;
    /**
     * Destination MAC address.
     */
    private int macDestinationAddress;
    /**
     * Response probability.
     */
    private byte responseProbability;
    /**
     * Allowed time slots.
     */
    private int allowedTimeSlots;
    /**
     * Server saves client system title.
     */
    private byte[] clientSystemTitle;

    /**
     * @return Initial credit (IC) tells how many times the frame must be
     *         repeated. Maximum value is 7.
     */
    public final int getInitialCredit() {
        return initialCredit;
    }

    /**
     * @param value
     *            Initial credit (IC) tells how many times the frame must be
     *            repeated. Maximum value is 7.
     */
    public final void setInitialCredit(final int value) {
        initialCredit = value;
    }

    /**
     * @return The current credit (CC) initial value equal to IC and
     *         automatically decremented by the MAC layer after each repetition.
     *         Maximum value is 7.
     */
    public final int getCurrentCredit() {
        return currentCredit;
    }

    /**
     * @param value
     *            The current credit (CC) initial value equal to IC and
     *            automatically decremented by the MAC layer after each
     *            repetition. Maximum value is 7.
     */
    public final void setCurrentCredit(final int value) {
        currentCredit = value;
    }

    /**
     * @return Delta credit (DC) value.
     */
    public final byte getDeltaCredit() {
        return deltaCredit;
    }

    /**
     * @param value
     *            Delta credit (DC) value.
     */
    public final void setDeltaCredit(final byte value) {
        deltaCredit = value;
    }

    /**
     * @return IEC 61334-4-32 LLC uses 6 bytes long system title. IEC 61334-5-1
     *         uses 8 bytes long system title so we can use the default one.
     */
    public final byte[] getSystemTitle() {
        if (settings != null && settings.getInterfaceType() != InterfaceType.PLC
                && settings.getCipher() != null) {
            return settings.getCipher().getSystemTitle();
        }
        return systemTitle;
    }

    /**
     * @param value
     *            IEC 61334-4-32 LLC uses 6 bytes long system title. IEC
     *            61334-5-1 uses 8 bytes long system title so we can use the
     *            default one.
     */
    public final void setSystemTitle(final byte[] value) {
        if (settings != null && settings.getInterfaceType() != InterfaceType.PLC
                && settings.getCipher() != null) {
            settings.getCipher().setSystemTitle(value);
        }
        systemTitle = value;
    }

    /**
     * @return Source MAC address.
     */
    public final int getMacSourceAddress() {
        return macSourceAddress;
    }

    /**
     * @param value
     *            Source MAC address.
     */
    public final void setMacSourceAddress(final int value) {
        macSourceAddress = value;
    }

    /**
     * @return Destination MAC address.
     */
    public final int getMacDestinationAddress() {
        return macDestinationAddress;
    }

    /**
     * @param value
     *            Destination MAC address.
     */
    public final void setMacDestinationAddress(final int value) {
        macDestinationAddress = value;
    }

    /**
     * @return Response probability.
     */
    public final byte getResponseProbability() {
        return responseProbability;
    }

    /**
     * @param value
     *            Response probability.
     */
    public final void setResponseProbability(final byte value) {
        responseProbability = value;
    }

    /**
     * @return Allowed time slots.
     */
    public final int getAllowedTimeSlots() {
        return allowedTimeSlots;
    }

    /**
     * @param value
     *            Allowed time slots.
     */
    public final void setAllowedTimeSlots(final int value) {
        allowedTimeSlots = value;
    }

    /**
     * @return Server saves client system title.
     */
    public final byte[] getClientSystemTitle() {
        return clientSystemTitle;
    }

    /**
     * @param value
     *            Server saves client system title.
     */
    public final void setClientSystemTitle(final byte[] value) {
        clientSystemTitle = value;
    }

    /**
     * Set default values.
     */
    public final void reset() {
        initialCredit = 7;
        currentCredit = 7;
        deltaCredit = 0;
        // New device addresses are used.
        if (settings.getInterfaceType() == InterfaceType.PLC) {
            if (settings.isServer()) {
                macSourceAddress = PlcSourceAddress.NEW.getValue();
                macDestinationAddress = PlcSourceAddress.INITIATOR.getValue();
            } else {
                macSourceAddress = PlcSourceAddress.INITIATOR.getValue();
                macDestinationAddress =
                        PlcDestinationAddress.ALL_PHYSICAL.getValue();
            }
        } else {
            if (settings.isServer()) {
                macSourceAddress = PlcSourceAddress.NEW.getValue();
                macDestinationAddress =
                        PlcHdlcSourceAddress.INITIATOR.getValue();
            } else {
                macSourceAddress = PlcHdlcSourceAddress.INITIATOR.getValue();
                macDestinationAddress =
                        PlcDestinationAddress.ALL_PHYSICAL.getValue();
            }
        }
        responseProbability = 100;
        if (settings.getInterfaceType() == InterfaceType.PLC) {
            allowedTimeSlots = 10;
        } else {
            allowedTimeSlots = 0x14;
        }
    }

    public GXPlcSettings(GXDLMSSettings s) {
        settings = s;
        reset();
    }

    /**
     * Discover available PLC meters.
     * 
     * @return Generated bytes.
     */
    public final byte[] discoverRequest() {
        GXByteBuffer bb = new GXByteBuffer();
        if (settings.getInterfaceType() != InterfaceType.PLC
                && settings.getInterfaceType() != InterfaceType.PLC_HDLC) {
            throw new IllegalArgumentException("Invalid interface type.");
        }
        if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
            bb.set(GXCommon.LLC_SEND_BYTES);
        }
        bb.setUInt8(Command.DISCOVER_REQUEST);
        bb.setUInt8(getResponseProbability());
        bb.setUInt16(getAllowedTimeSlots());
        // DiscoverReport initial credit
        bb.setUInt8(0);
        // IC Equal credit
        bb.setUInt8(0);
        int val = 0;
        int clientAddress = settings.getClientAddress();
        int serverAddress = settings.getServerAddress();
        int da = settings.getPlc().getMacDestinationAddress();
        int sa = settings.getPlc().getMacSourceAddress();
        try {
            // 10.4.6.4 Source and destination APs and addresses of CI-PDUs
            // Client address is No-station in discoverReport.
            if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
                settings.getPlc().setInitialCredit((byte) 0);
                settings.getPlc().setCurrentCredit((byte) 0);
                settings.getPlc().setMacSourceAddress(0xC01);
                settings.getPlc().setMacDestinationAddress(0xFFF);
                settings.setClientAddress(0x66);
                // All-station
                settings.setServerAddress(0x33FF);
            } else {
                val = settings.getPlc().getInitialCredit() << 5;
                val |= settings.getPlc().getCurrentCredit() << 2;
                val |= settings.getPlc().getDeltaCredit() & 0x3;
                settings.getPlc().setMacSourceAddress(0xC00);
                settings.setClientAddress(1);
                settings.setServerAddress(0);
            }
            return GXDLMS.getMacFrame(settings, (byte) 0x13, (byte) val, bb);
        } finally {
            settings.setClientAddress(clientAddress);
            settings.setServerAddress(serverAddress);
            settings.getPlc().setMacDestinationAddress(da);
            settings.getPlc().setMacSourceAddress(sa);
        }
    }

    /**
     * Generates discover report.
     * 
     * @param systemTitle
     *            System title
     * @param newMeter
     *            Is this a new meter.
     * @return Generated bytes.
     */
    public final byte[] discoverReport(final byte[] systemTitle,
            final boolean newMeter) {
        GXByteBuffer bb = new GXByteBuffer();
        if (settings.getInterfaceType() != InterfaceType.PLC
                && settings.getInterfaceType() != InterfaceType.PLC_HDLC) {
            throw new IllegalArgumentException("Invalid interface type.");
        }
        int alarmDescription;
        if (settings.getInterfaceType() == InterfaceType.PLC) {
            alarmDescription = (newMeter ? 1 : 0x82);
        } else {
            alarmDescription = 0;
        }
        if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
            bb.set(GXCommon.LLC_REPLY_BYTES);
        }
        bb.setUInt8(Command.DISCOVER_REPORT);
        bb.setUInt8(1);
        bb.set(systemTitle);
        if (alarmDescription != 0) {
            bb.setUInt8(1);
        }
        bb.setUInt8(alarmDescription);
        int clientAddress = settings.getClientAddress();
        int serverAddress = settings.getServerAddress();
        int macSourceAddress = settings.getPlc().getMacSourceAddress();
        int macTargetAddress = settings.getPlc().getMacDestinationAddress();
        try {
            // 10.4.6.4 Source and destination APs and addresses of CI-PDUs
            // Client address is No-station in discoverReport.
            if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
                settings.getPlc().setMacDestinationAddress(
                        PlcHdlcSourceAddress.INITIATOR.getValue());
            } else {
                settings.setClientAddress(0);
                settings.setServerAddress(0xFD);
            }
            return GXDLMS.getMacFrame(settings, (byte) 0x13, (byte) 0, bb);
        } finally {
            settings.setClientAddress(clientAddress);
            settings.setServerAddress(serverAddress);
            settings.getPlc().setMacSourceAddress(macSourceAddress);
            settings.getPlc().setMacDestinationAddress(macTargetAddress);
        }
    }

    /**
     * @param value
     */

    /**
     * Parse discover reply.
     * 
     * @param value
     *            Received data.
     * @param sa
     *            Source address.
     * @param da
     *            Destination address.
     * @return Array of system titles and alarm descriptor error code
     */
    public final List<GXDLMSPlcMeterInfo> parseDiscover(
            final GXByteBuffer value, final int sa, final int da) {
        List<GXDLMSPlcMeterInfo> list = new ArrayList<GXDLMSPlcMeterInfo>();
        short count = value.getUInt8();
        byte[] st;
        for (int pos = 0; pos != count; ++pos) {
            GXDLMSPlcMeterInfo info = new GXDLMSPlcMeterInfo();
            info.setSourceAddress(sa);
            info.setDestinationAddress(da);
            // Get System title.
            if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
                st = new byte[8];
            } else {
                st = new byte[6];
            }
            value.get(st);
            info.setSystemTitle(st);
            // Alarm descriptor of the reporting system.
            // Alarm-Descriptor presence flag
            if (value.getUInt8() != 0) {
                // Alarm-Descriptor
                info.setAlarmDescriptor(value.getUInt8());
            }
            list.add(info);
        }
        return list;
    }

    /**
     * Register PLC meters.
     * 
     * @param initiatorSystemTitle
     *            Active initiator system title
     * @param systemTitle
     *            System title.
     * @return Generated bytes.
     */
    public final byte[] registerRequest(final byte[] initiatorSystemTitle,
            final byte[] systemTitle) {
        GXByteBuffer bb = new GXByteBuffer();
        bb.setUInt8(Command.REGISTER_REQUEST);
        bb.set(initiatorSystemTitle);
        // LEN
        bb.setUInt8(0x1);
        bb.set(systemTitle);
        // MAC address.
        bb.setUInt16(getMacSourceAddress());
        int val = settings.getPlc().getInitialCredit() << 5;
        val |= settings.getPlc().getCurrentCredit() << 2;
        val |= settings.getPlc().getDeltaCredit() & 0x3;

        int clientAddress = settings.getClientAddress();
        int serverAddress = settings.getServerAddress();
        int macSourceAddress = settings.getPlc().getMacSourceAddress();
        int macTargetAddress = settings.getPlc().getMacDestinationAddress();
        try {
            // 10.4.6.4 Source and destination APs and addresses of CI-PDUs
            // Client address is No-station in discoverReport.
            if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
                settings.getPlc().setInitialCredit(0);
                settings.getPlc().setCurrentCredit(0);
                settings.getPlc().setMacSourceAddress(0xC01);
                settings.getPlc().setMacDestinationAddress(0xFFF);
                settings.setClientAddress(0x66);
                // All-station
                settings.setServerAddress(0x33FF);
            } else {
                settings.setClientAddress(1);
                settings.setServerAddress(0);
                settings.getPlc().setMacSourceAddress(0xC00);
                settings.getPlc().setMacDestinationAddress(0xFFF);
            }
            return GXDLMS.getMacFrame(settings, (byte) 0x13, (byte) val, bb);
        } finally {
            settings.setClientAddress(clientAddress);
            settings.setServerAddress(serverAddress);
            settings.getPlc().setMacSourceAddress(macSourceAddress);
            settings.getPlc().setMacDestinationAddress(macTargetAddress);
        }
    }

    /**
     * Parse register request.
     * 
     * @param value
     *            Received data.
     */
    public final void parseRegisterRequest(GXByteBuffer value) {
        // Get System title.
        byte[] st;
        if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
            st = new byte[8];
        } else {
            st = new byte[6];
        }
        value.get(st);
        short count = value.getUInt8();
        for (int pos = 0; pos != count; ++pos) {
            // Get System title.
            if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
                st = new byte[8];
            } else {
                st = new byte[6];
            }
            value.get(st);
            setSystemTitle(st);
            // MAC address.
            setMacSourceAddress(value.getUInt16());
        }
    }

    /**
     * Parse discover request.
     * 
     * @param value
     *            Received data.
     * @return Register information.
     */
    public final GXDLMSPlcRegister
            parseDiscoverRequest(final GXByteBuffer value) {
        GXDLMSPlcRegister ret = new GXDLMSPlcRegister();
        ret.setResponseProbability(value.getUInt8());
        ret.setAllowedTimeSlots(value.getUInt16());
        ret.setDiscoverReportInitialCredit(value.getUInt8());
        ret.setICEqualCredit(value.getUInt8());
        return ret;
    }

    /**
     * Ping PLC meter.
     * 
     * @param systemTitle
     *            System title.
     * @return Generated bytes.
     */
    public final byte[] pingRequest(final byte[] systemTitle) {
        GXByteBuffer bb = new GXByteBuffer();
        // Control byte.
        bb.setUInt8(Command.PING_REQUEST);
        bb.set(systemTitle);
        return GXDLMS.getMacFrame(settings, (byte) 0x13, (byte) 0, bb);
    }

    /**
     * Parse ping response.
     * 
     * @param value
     *            Received data.
     * @return System title.
     */
    public final byte[] parsePing(final GXByteBuffer value) {
        return value.subArray(1, 6);
    }

    /**
     * Repeater call request.
     * 
     * @return Generated bytes.
     */
    public final byte[] repeaterCallRequest() {
        GXByteBuffer bb = new GXByteBuffer();
        // Control byte.
        bb.setUInt8(Command.REPEAT_CALL_REQUEST);
        // MaxAdrMac.
        bb.setUInt16(0x63);
        // Nb_Tslot_For_New
        bb.setUInt8(0);
        // Reception-Threshold default value
        bb.setUInt8(0);
        return GXDLMS.getMacFrame(settings, (byte) 0x13, (byte) 0xFC, bb);
    }
}