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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.asn.GXAsn1Converter;
import gurux.dlms.asn.GXAsn1Integer;
import gurux.dlms.asn.GXAsn1Sequence;
import gurux.dlms.asn.enums.Ecc;
import gurux.dlms.ecdsa.GXEcdsa;
import gurux.dlms.enums.AccessMode3;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.ConfirmedServiceError;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.ConnectionState;
import gurux.dlms.enums.CryptoKeyType;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ExceptionServiceError;
import gurux.dlms.enums.ExceptionStateError;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.Signing;
import gurux.dlms.enums.Standard;
import gurux.dlms.enums.TranslatorOutputType;
import gurux.dlms.internal.CoAPClass;
import gurux.dlms.internal.CoAPClientError;
import gurux.dlms.internal.CoAPContentType;
import gurux.dlms.internal.CoAPMethod;
import gurux.dlms.internal.CoAPOptionType;
import gurux.dlms.internal.CoAPServerError;
import gurux.dlms.internal.CoAPSignaling;
import gurux.dlms.internal.CoAPSuccess;
import gurux.dlms.internal.CoAPType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.GXDLMSAccount;
import gurux.dlms.objects.GXDLMSActionSchedule;
import gurux.dlms.objects.GXDLMSActivityCalendar;
import gurux.dlms.objects.GXDLMSArbitrator;
import gurux.dlms.objects.GXDLMSArrayManager;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSAutoAnswer;
import gurux.dlms.objects.GXDLMSAutoConnect;
import gurux.dlms.objects.GXDLMSCharge;
import gurux.dlms.objects.GXDLMSClock;
import gurux.dlms.objects.GXDLMSCoAPDiagnostic;
import gurux.dlms.objects.GXDLMSCoAPSetup;
import gurux.dlms.objects.GXDLMSCommunicationPortProtection;
import gurux.dlms.objects.GXDLMSCompactData;
import gurux.dlms.objects.GXDLMSCredit;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSDemandRegister;
import gurux.dlms.objects.GXDLMSDisconnectControl;
import gurux.dlms.objects.GXDLMSExtendedRegister;
import gurux.dlms.objects.GXDLMSFunctionControl;
import gurux.dlms.objects.GXDLMSG3Plc6LoWPan;
import gurux.dlms.objects.GXDLMSG3PlcMacLayerCounters;
import gurux.dlms.objects.GXDLMSG3PlcMacSetup;
import gurux.dlms.objects.GXDLMSGSMDiagnostic;
import gurux.dlms.objects.GXDLMSGprsSetup;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSIECLocalPortSetup;
import gurux.dlms.objects.GXDLMSIec8802LlcType1Setup;
import gurux.dlms.objects.GXDLMSIec8802LlcType2Setup;
import gurux.dlms.objects.GXDLMSIec8802LlcType3Setup;
import gurux.dlms.objects.GXDLMSIecTwistedPairSetup;
import gurux.dlms.objects.GXDLMSImageTransfer;
import gurux.dlms.objects.GXDLMSIp4Setup;
import gurux.dlms.objects.GXDLMSIp6Setup;
import gurux.dlms.objects.GXDLMSLimiter;
import gurux.dlms.objects.GXDLMSLlcSscsSetup;
import gurux.dlms.objects.GXDLMSLteMonitoring;
import gurux.dlms.objects.GXDLMSMBusClient;
import gurux.dlms.objects.GXDLMSMBusDiagnostic;
import gurux.dlms.objects.GXDLMSMBusMasterPortSetup;
import gurux.dlms.objects.GXDLMSMBusPortSetup;
import gurux.dlms.objects.GXDLMSMBusSlavePortSetup;
import gurux.dlms.objects.GXDLMSMacAddressSetup;
import gurux.dlms.objects.GXDLMSModemConfiguration;
import gurux.dlms.objects.GXDLMSNtpSetup;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSParameterMonitor;
import gurux.dlms.objects.GXDLMSPppSetup;
import gurux.dlms.objects.GXDLMSPrimeNbOfdmPlcApplicationsIdentification;
import gurux.dlms.objects.GXDLMSPrimeNbOfdmPlcMacCounters;
import gurux.dlms.objects.GXDLMSPrimeNbOfdmPlcMacFunctionalParameters;
import gurux.dlms.objects.GXDLMSPrimeNbOfdmPlcMacNetworkAdministrationData;
import gurux.dlms.objects.GXDLMSPrimeNbOfdmPlcMacSetup;
import gurux.dlms.objects.GXDLMSPrimeNbOfdmPlcPhysicalLayerCounters;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXDLMSPushSetup;
import gurux.dlms.objects.GXDLMSRegister;
import gurux.dlms.objects.GXDLMSRegisterActivation;
import gurux.dlms.objects.GXDLMSRegisterMonitor;
import gurux.dlms.objects.GXDLMSSFSKActiveInitiator;
import gurux.dlms.objects.GXDLMSSFSKMacCounters;
import gurux.dlms.objects.GXDLMSSFSKMacSynchronizationTimeouts;
import gurux.dlms.objects.GXDLMSSFSKPhyMacSetUp;
import gurux.dlms.objects.GXDLMSSFSKReportingSystemList;
import gurux.dlms.objects.GXDLMSSapAssignment;
import gurux.dlms.objects.GXDLMSSchedule;
import gurux.dlms.objects.GXDLMSScriptTable;
import gurux.dlms.objects.GXDLMSSecuritySetup;
import gurux.dlms.objects.GXDLMSSpecialDaysTable;
import gurux.dlms.objects.GXDLMSTcpUdpSetup;
import gurux.dlms.objects.GXDLMSTokenGateway;
import gurux.dlms.objects.GXDLMSUtilityTables;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.dlms.plc.enums.PlcDataLinkData;
import gurux.dlms.plc.enums.PlcDestinationAddress;
import gurux.dlms.plc.enums.PlcHdlcSourceAddress;
import gurux.dlms.plc.enums.PlcMacSubframes;
import gurux.dlms.plc.enums.PlcSourceAddress;
import gurux.dlms.secure.AesGcmParameter;
import gurux.dlms.secure.CountType;
import gurux.dlms.secure.GXCiphering;
import gurux.dlms.secure.GXSecure;

/**
 * GXDLMS implements methods to communicate with DLMS/COSEM metering devices.
 */
abstract class GXDLMS {
    /*
     * HDLC frame start and end character.
     */
    static final byte HDLC_FRAME_START_END = 0x7E;
    static final byte CIPHERING_HEADER_SIZE = 7 + 12 + 3;
    static final int DATA_TYPE_OFFSET = 0xFF0000;

    static boolean useHdlc(InterfaceType type) {
        return type == InterfaceType.HDLC || type == InterfaceType.HDLC_WITH_MODE_E || type == InterfaceType.PLC_HDLC;
    }

    /**
     * Constructor.
     */
    private GXDLMS() {

    }

    static byte getInvokeIDPriority(final GXDLMSSettings settings, final boolean increase) {
        byte value = 0;
        if (settings.getPriority() == Priority.HIGH) {
            value |= 0x80;
        }
        if (settings.getServiceClass() == ServiceClass.CONFIRMED) {
            value |= 0x40;
        }
        if (increase) {
            settings.setInvokeID(((settings.getInvokeID() + 1) & 0xF));
        }
        value |= settings.getInvokeID();
        return value;
    }

    /**
     * Generates Invoke ID and priority.
     * 
     * @param settings
     *            DLMS settings.
     * @return Invoke ID and priority.
     */
    private static long getLongInvokeIDPriority(final GXDLMSSettings settings) {
        long value = 0;
        if (settings.getPriority() == Priority.HIGH) {
            value = 0x80000000;
        }
        if (settings.getServiceClass() == ServiceClass.CONFIRMED) {
            value |= 0x40000000;
        }
        value |= (int) (settings.getLongInvokeID() & 0xFFFFFF);
        settings.setLongInvokeID(settings.getLongInvokeID() + 1);
        return value;
    }

    /**
     * Generates an acknowledgment message, with which the server is informed to
     * send next packets.
     * 
     * @param settings
     *            DLMS settings.
     * @param type
     *            Frame type.
     * @return Acknowledgment message as byte array.
     * @deprecated
     */
    public static byte[] receiverReady(final GXDLMSSettings settings, final RequestTypes type) {
        java.util.Set<RequestTypes> tmp = new HashSet<RequestTypes>();
        tmp.add(type);
        try {
            return receiverReady(settings, tmp);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Generates an acknowledgment message, with which the server is informed to
     * send next packets.
     * 
     * @param settings
     *            DLMS settings.
     * @param type
     *            Frame type.
     * @return Acknowledgment message as byte array.
     * @deprecated
     */
    public static byte[] receiverReady(final GXDLMSSettings settings, final java.util.Set<RequestTypes> type) {
        GXReplyData reply = new GXReplyData();
        reply.getMoreData().addAll(type);
        reply.setWindowSize(settings.getGbtWindowSize());
        reply.setBlockNumberAck(settings.getBlockNumberAck());
        reply.setBlockNumber(settings.getBlockIndex());
        try {
            return receiverReady(settings, reply);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /*
     * Generates an acknowledgment message, with which the server is informed to
     * send next packets.
     * @param settings DLMS settings.
     * @param reply Received data.
     * @return Acknowledgment message as byte array.
     */
    public static byte[] receiverReady(final GXDLMSSettings settings, final GXReplyData reply)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {

        if (reply.getMoreData().isEmpty()) {
            // Generate RR.
            byte id = settings.getKeepAlive();
            if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
                return GXDLMS.getMacHdlcFrame(settings, id, 0, null);
            }
            return getHdlcFrame(settings, id, null);
        }
        // Get next frame.
        if (reply.getMoreData().contains(RequestTypes.FRAME)) {
            byte id = settings.getReceiverReady();
            if (settings.getInterfaceType() == InterfaceType.PLC_HDLC) {
                return GXDLMS.getMacHdlcFrame(settings, id, (byte) 0, null);
            }
            if (settings.getInterfaceType() == InterfaceType.COAP) {
                return getCoAPFrame(settings, Command.GET_REQUEST, null, 0);
            }
            return getHdlcFrame(settings, id, null);
        }
        int cmd = settings.getCommand();
        // Get next block.
        List<byte[]> data;
        if (reply.getMoreData().contains(RequestTypes.GBT)) {
            GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0, Command.GENERAL_BLOCK_TRANSFER, 0, null, null,
                    0xff, Command.NONE);
            p.setWindowSize(reply.getWindowSize());
            p.setBlockNumberAck(reply.getBlockNumber());
            p.setBlockIndex(settings.getBlockIndex());
            data = GXDLMS.getLnMessages(p);
        } else {
            GXByteBuffer bb = new GXByteBuffer(4);
            if (settings.getUseLogicalNameReferencing()) {
                bb.setUInt32(settings.getBlockIndex());
            } else {
                bb.setUInt16(settings.getBlockIndex());
            }
            settings.increaseBlockIndex();
            if (settings.getUseLogicalNameReferencing()) {
                GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0, cmd, GetCommandType.NEXT_DATA_BLOCK, bb,
                        null, 0xff, Command.NONE);
                data = getLnMessages(p);
            } else {
                GXDLMSSNParameters p = new GXDLMSSNParameters(settings, cmd, 1,
                        VariableAccessSpecification.BLOCK_NUMBER_ACCESS, bb, null);
                data = getSnMessages(p);
            }
        }
        return data.get(0);
    }

    static String getDescription(final int errCode) {
        String str;
        ErrorCode errorCode = ErrorCode.forValue(errCode);
        if (errorCode != null) {
            switch (errorCode) {
            case REJECTED:
                str = "Rejected";
                break;
            case OK:
                str = "";
                break;
            case HARDWARE_FAULT:
                str = "Access Error : Device reports a hardware fault.";
                break;
            case TEMPORARY_FAILURE:
                str = "Access Error : Device reports a temporary failure.";
                break;
            case READ_WRITE_DENIED:
                str = "Access Error : Device reports Read-Write denied.";
                break;
            case UNDEFINED_OBJECT:
                str = "Access Error : Device reports a undefined object.";
                break;
            case INCONSISTENT_CLASS:
                str = "Access Error : " + "Device reports a inconsistent Class or object.";
                break;
            case UNAVAILABLE_OBJECT:
                str = "Access Error : Device reports a unavailable object.";
                break;
            case UNMATCHED_TYPE:
                str = "Access Error : Device reports a unmatched type.";
                break;
            case ACCESS_VIOLATED:
                str = "Access Error : Device reports scope of access violated.";
                break;
            case DATA_BLOCK_UNAVAILABLE:
                str = "Access Error : Data Block Unavailable.";
                break;
            case LONG_GET_OR_READ_ABORTED:
                str = "Access Error : Long Get Or Read Aborted.";
                break;
            case NO_LONG_GET_OR_READ_IN_PROGRESS:
                str = "Access Error : No Long Get Or Read In Progress.";
                break;
            case LONG_SET_OR_WRITE_ABORTED:
                str = "Access Error : Long Set Or Write Aborted.";
                break;
            case NO_LONG_SET_OR_WRITE_IN_PROGRESS:
                str = "Access Error : No Long Set Or Write In Progress.";
                break;
            case DATA_BLOCK_NUMBER_INVALID:
                str = "Access Error : Data Block Number Invalid.";
                break;
            case OTHER_REASON:
                str = "Access Error : Other Reason.";
                break;
            default:
                str = "Access Error : Unknown error.";
                break;
            }
        } else {
            str = "Access Error : Invalid error code " + String.valueOf(errCode);
        }
        return str;
    }

    /*
     * Reserved for internal use.
     */
    static void checkInit(final GXDLMSSettings settings) {
        if (settings.getClientAddress() == 0) {
            throw new IllegalArgumentException("Invalid Client Address.");
        }
        if (settings.getServerAddress() == 0) {
            throw new IllegalArgumentException("Invalid Server Address.");
        }
    }

    static void appendData(final GXDLMSObject obj, final int index, final GXByteBuffer bb, final Object value) {

        DataType tp = obj.getDataType(index);
        if (tp == DataType.ARRAY) {
            if (value instanceof byte[] && tp != DataType.OCTET_STRING) {
                bb.set((byte[]) value);
                return;
            }
        } else {
            if (tp == DataType.NONE) {
                tp = GXDLMSConverter.getDLMSDataType(value);
                // If data type is not defined for Date Time it is write as
                // octet string.
                if (tp == DataType.DATETIME) {
                    tp = DataType.OCTET_STRING;
                }
            } else if (value instanceof String && tp == DataType.OCTET_STRING) {
                DataType ui = obj.getUIDataType(index);
                if (ui == DataType.STRING) {
                    GXCommon.setData(null, bb, tp, ((String) value).getBytes());
                    return;
                }
            }
        }
        GXCommon.setData(null, bb, tp, value);
    }

    /**
     * Get used glo message.
     * 
     * @param command
     *            Executed command.
     * @return Integer value of glo message.
     */
    private static int getGloMessage(final int command) {
        int cmd;
        switch (command) {
        case Command.READ_REQUEST:
            cmd = Command.GLO_READ_REQUEST;
            break;
        case Command.GET_REQUEST:
            cmd = Command.GLO_GET_REQUEST;
            break;
        case Command.WRITE_REQUEST:
            cmd = Command.GLO_WRITE_REQUEST;
            break;
        case Command.SET_REQUEST:
            cmd = Command.GLO_SET_REQUEST;
            break;
        case Command.METHOD_REQUEST:
            cmd = Command.GLO_METHOD_REQUEST;
            break;
        case Command.READ_RESPONSE:
            cmd = Command.GLO_READ_RESPONSE;
            break;
        case Command.GET_RESPONSE:
            cmd = Command.GLO_GET_RESPONSE;
            break;
        case Command.WRITE_RESPONSE:
            cmd = Command.GLO_WRITE_RESPONSE;
            break;
        case Command.SET_RESPONSE:
            cmd = Command.GLO_SET_RESPONSE;
            break;
        case Command.METHOD_RESPONSE:
            cmd = Command.GLO_METHOD_RESPONSE;
            break;
        case Command.DATA_NOTIFICATION:
            cmd = Command.GENERAL_GLO_CIPHERING;
            break;
        case Command.RELEASE_REQUEST:
            cmd = Command.RELEASE_REQUEST;
            break;
        case Command.RELEASE_RESPONSE:
            cmd = Command.RELEASE_RESPONSE;
            break;
        default:
            throw new GXDLMSException("Invalid GLO command.");
        }
        return cmd;
    }

    /**
     * Get used ded message.
     * 
     * @param cmd
     *            Executed command.
     * @return Integer value of ded message.
     */
    private static int getDedMessage(final int command) {
        int cmd;
        switch (command) {
        case Command.GET_REQUEST:
            cmd = Command.DED_GET_REQUEST;
            break;
        case Command.SET_REQUEST:
            cmd = Command.DED_SET_REQUEST;
            break;
        case Command.METHOD_REQUEST:
            cmd = Command.DED_METHOD_REQUEST;
            break;
        case Command.GET_RESPONSE:
            cmd = Command.DED_GET_RESPONSE;
            break;
        case Command.SET_RESPONSE:
            cmd = Command.DED_SET_RESPONSE;
            break;
        case Command.METHOD_RESPONSE:
            cmd = Command.DED_METHOD_RESPONSE;
            break;
        case Command.DATA_NOTIFICATION:
            cmd = Command.GENERAL_DED_CIPHERING;
            break;
        case Command.RELEASE_REQUEST:
            cmd = Command.RELEASE_REQUEST;
            break;
        case Command.RELEASE_RESPONSE:
            cmd = Command.RELEASE_RESPONSE;
            break;
        default:
            throw new GXDLMSException("Invalid DED command.");
        }
        return cmd;
    }

    /**
     * Add LLC bytes to generated message.
     * 
     * @param settings
     *            DLMS settings.
     * @param data
     *            Data where bytes are added.
     */
    static void addLLCBytes(final GXDLMSSettings settings, final GXByteBuffer data) {
        byte[] tmp = data.array();
        data.clear();
        if (settings.isServer()) {
            data.set(GXCommon.LLC_REPLY_BYTES);
        } else {
            data.set(GXCommon.LLC_SEND_BYTES);
        }
        data.set(tmp);
    }

    /*
     * Check is all data fit to one data block.
     * @param p LN parameters.
     * @param reply Generated reply.
     */
    @SuppressWarnings("squid:S1940")
    static void multipleBlocks(final GXDLMSLNParameters p, final GXByteBuffer reply, final boolean ciphering) {
        // Check is all data fit to one message if data is given.
        int len = 0;
        if (p.getData() != null) {
            len = p.getData().size() - p.getData().position();
        }
        if (p.getAttributeDescriptor() != null) {
            len += p.getAttributeDescriptor().size();
        }
        if (ciphering) {
            len += CIPHERING_HEADER_SIZE;
        }
        // If system title is sent.
        if ((Conformance.toInteger(p.getSettings().getNegotiatedConformance())
                & Conformance.GENERAL_PROTECTION.getValue()) != 0) {
            len += 9;
        }
        len += getSigningSize(p);
        if (!p.isMultipleBlocks()) {
            // Add command type and invoke and priority.
            p.setMultipleBlocks(2 + reply.size() + len > p.getSettings().getMaxPduSize());
        }
        if (p.isMultipleBlocks()) {
            // Add command type and invoke and priority.
            p.setLastBlock(!(8 + reply.size() + len > p.getSettings().getMaxPduSize()));
        }
        if (p.isLastBlock()) {
            // Add command type and invoke and priority.
            p.setLastBlock(!(8 + reply.size() + len > p.getSettings().getMaxPduSize()));
        }
    }

    /**
     * Returns the amount of the bytes that signing requires.
     * 
     * @param p
     *            LN Parameters.
     * @return need bytes.
     */
    static private byte getSigningSize(final GXDLMSLNParameters p) {
        byte size = 0;
        // If signing is used.
        if (p.getSettings().getCipher() != null
                && p.getSettings().getCipher().getSigning() == Signing.GENERAL_SIGNING) {
            if (p.getSettings().getCipher().getSecuritySuite() == SecuritySuite.SUITE_1) {
                size = 65;
            } else if (p.getSettings().getCipher().getSecuritySuite() == SecuritySuite.SUITE_2) {
                size = 99;
            }
        }
        return size;
    }

    /*
     * Get next logical name PDU.
     * @param p LN parameters.
     * @param reply Generated message.
     */
    public static void getLNPdu(final GXDLMSLNParameters p, final GXByteBuffer reply)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        boolean ciphering = p.getCommand() != Command.AARQ && p.getCommand() != Command.AARE
                && (p.getSettings().isCiphered(true) || p.cipheredCommand != Command.NONE);
        int len = 0;
        if (p.getCommand() == Command.AARQ) {
            if (p.getSettings().getGateway() != null
                    && p.getSettings().getGateway().getPhysicalDeviceAddress() != null) {
                reply.setUInt8(Command.GATEWAY_REQUEST);
                reply.setUInt8(p.getSettings().getGateway().getNetworkId());
                reply.setUInt8(p.getSettings().getGateway().getPhysicalDeviceAddress().length);
                reply.set(p.getSettings().getGateway().getPhysicalDeviceAddress());
            }
            reply.set(p.getAttributeDescriptor());
        } else {
            // Add command.
            if (p.getCommand() != Command.GENERAL_BLOCK_TRANSFER) {
                reply.setUInt8(p.getCommand());
            }
            if (p.getCommand() == Command.EVENT_NOTIFICATION || p.getCommand() == Command.DATA_NOTIFICATION
                    || p.getCommand() == Command.ACCESS_REQUEST || p.getCommand() == Command.ACCESS_RESPONSE) {
                // Add Long-Invoke-Id-And-Priority
                if (p.getCommand() != Command.EVENT_NOTIFICATION) {
                    if (p.getInvokeId() != 0) {
                        reply.setUInt32(p.getInvokeId());

                    } else {
                        reply.setUInt32(getLongInvokeIDPriority(p.getSettings()));
                    }
                }
                // Add date time.
                if (p.getTime() == null) {
                    reply.setUInt8(DataType.NONE.getValue());
                } else {
                    // Data is send in octet string.
                    // Remove data type except from event Notification.
                    int pos = reply.size();
                    GXCommon.setData(null, reply, DataType.OCTET_STRING, p.getTime());
                    if (p.getCommand() != Command.EVENT_NOTIFICATION) {
                        reply.move(pos + 1, pos, reply.size() - pos - 1);
                    }
                }
                multipleBlocks(p, reply, ciphering);
            } else if (p.getCommand() != Command.RELEASE_REQUEST) {
                // Get request size can be bigger than PDU size.
                if (p.getCommand() != Command.GET_REQUEST && p.getData() != null && p.getData().size() != 0) {
                    multipleBlocks(p, reply, ciphering);
                }
                // Change Request type if Set request and multiple blocks is
                // needed.
                if (p.getCommand() == Command.SET_REQUEST) {
                    if (p.isMultipleBlocks() && !p.getSettings().getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                        if (p.getRequestType() == SetRequestType.NORMAL) {
                            p.setRequestType(SetRequestType.FIRST_DATA_BLOCK);
                        } else if (p.getRequestType() == SetRequestType.FIRST_DATA_BLOCK) {
                            p.setRequestType(SetRequestType.WITH_DATA_BLOCK);
                        }
                    }
                }
                // Change Request type if action request and multiple blocks is
                // needed.
                else if (p.getCommand() == Command.METHOD_REQUEST) {
                    if (p.isMultipleBlocks() && !p.getSettings().getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                        if (p.getRequestType() == ActionRequestType.NORMAL) {
                            // Remove Method Invocation Parameters tag.
                            p.getAttributeDescriptor().size(p.getAttributeDescriptor().size() - 1);
                            p.setRequestType(ActionRequestType.WITH_FIRST_BLOCK);
                        } else if (p.getRequestType() == ActionRequestType.WITH_FIRST_BLOCK) {
                            p.setRequestType(ActionRequestType.WITH_BLOCK);
                        }
                    }
                }
                // Change Request type if action request and multiple blocks is
                // needed.
                else if (p.getCommand() == Command.METHOD_RESPONSE) {
                    if (p.isMultipleBlocks() && !p.getSettings().getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                        if (p.getRequestType() == ActionResponseType.NORMAL) {
                            // Remove Method Invocation Parameters tag.
                            p.getData().position(2 + p.getData().position());
                            p.setRequestType(ActionResponseType.WITH_BLOCK);
                            p.setStatus(0xFF);
                        } else if (p.getRequestType() == ActionResponseType.WITH_BLOCK) {
                            p.setRequestType(ActionResponseType.NEXT_BLOCK);
                        }
                    }
                }
                // Change request type If get response and multiple blocks is
                // needed.
                else if (p.getCommand() == Command.GET_RESPONSE) {
                    if (p.isMultipleBlocks() && !p.getSettings().getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                        if (p.getRequestType() == 1) {
                            p.setRequestType(2);
                        }
                    }
                }
                if (p.getCommand() != Command.GENERAL_BLOCK_TRANSFER) {
                    reply.setUInt8(p.getRequestType());
                    // Add Invoke Id And Priority.
                    if (p.getInvokeId() != 0) {
                        reply.setUInt8((byte) p.getInvokeId());
                    } else {
                        reply.setUInt8(getInvokeIDPriority(p.getSettings(), p.getSettings().getAutoIncreaseInvokeID()));
                    }
                }
            }
            // Add attribute descriptor.
            reply.set(p.getAttributeDescriptor());
            if (p.isMultipleBlocks()
                    && !p.getSettings().getNegotiatedConformance().contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                if (p.command != Command.SET_RESPONSE && p.command != Command.METHOD_RESPONSE) {
                    // Is last block.
                    if (p.isLastBlock()) {
                        reply.setUInt8(1);
                        p.getSettings().setCount(0);
                        p.getSettings().setIndex(0);
                    } else {
                        reply.setUInt8(0);
                    }
                }
                // Block index.
                reply.setUInt32(p.getBlockIndex());
                p.setBlockIndex(p.getBlockIndex() + 1);
                // Add status if reply.
                if (p.getStatus() != 0xFF) {
                    if (p.getStatus() != 0 && p.getCommand() == Command.GET_RESPONSE) {
                        reply.setUInt8(1);
                    }
                    reply.setUInt8(p.getStatus());
                }
                // Block size.
                if (p.getData() != null) {
                    len = p.getData().size() - p.getData().position();
                } else {
                    len = 0;
                }
                int totalLength = len + reply.size();
                if (ciphering) {
                    totalLength += CIPHERING_HEADER_SIZE;
                }

                if (totalLength > p.getSettings().getMaxPduSize()) {
                    len = p.getSettings().getMaxPduSize() - reply.size();
                    if (ciphering) {
                        len -= CIPHERING_HEADER_SIZE;
                    }
                    len -= GXCommon.getObjectCountSizeInBytes(len);
                }
                // If server is not asking the next block.
                if (!(len == 0 && p.command == Command.METHOD_RESPONSE
                        && p.getRequestType() == ActionResponseType.NEXT_BLOCK)) {
                    GXCommon.setObjectCount(len, reply);
                    reply.set(p.getData(), len);
                }
            }
            // Add data that fits to one block.
            if (len == 0) {
                // Add status if reply.
                if (p.getStatus() != 0xFF && p.getCommand() != Command.GENERAL_BLOCK_TRANSFER) {
                    if (p.getStatus() != 0 && p.getCommand() == Command.GET_RESPONSE) {
                        reply.setUInt8(1);
                    }
                    reply.setUInt8(p.getStatus());
                }
                if (p.getData() != null && p.getData().size() != 0) {
                    len = p.getData().size() - p.getData().position();
                    if (p.getSettings().getGateway() != null
                            && p.getSettings().getGateway().getPhysicalDeviceAddress() != null) {

                        if (3 + len + p.getSettings().getGateway().getPhysicalDeviceAddress().length > p.getSettings()
                                .getMaxPduSize()) {
                            len -= (3 + p.getSettings().getGateway().getPhysicalDeviceAddress().length);
                        }
                        GXByteBuffer tmp = new GXByteBuffer(reply);
                        reply.size(0);
                        reply.setUInt8(Command.GATEWAY_REQUEST);
                        reply.setUInt8(p.getSettings().getGateway().getNetworkId());
                        reply.setUInt8(p.getSettings().getGateway().getPhysicalDeviceAddress().length);
                        reply.set(p.getSettings().getGateway().getPhysicalDeviceAddress());
                        reply.set(tmp);
                    }
                    // Get request size can be bigger than PDU size.
                    if (p.getSettings().getNegotiatedConformance().contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                        if (7 + len + reply.size() > p.getSettings().getMaxPduSize()) {
                            len = p.getSettings().getMaxPduSize() - reply.size() - 7;
                        }
                        // Cipher data only once.
                        if (ciphering && p.command != Command.GENERAL_BLOCK_TRANSFER) {
                            reply.set(p.getData());
                            byte[] tmp;
                            boolean sign = shoudSign(p);
                            if ((p.getSettings().getConnected() & ConnectionState.DLMS) == 0 || !sign) {
                                tmp = cipher0(p, reply.array());
                            } else {
                                tmp = cipher1(p, reply.array(), sign);
                            }
                            p.getData().size(0);
                            p.getData().set(tmp);
                            reply.size(0);
                            len = p.getData().size();
                            if (7 + len > p.getSettings().getMaxPduSize()) {
                                len = p.getSettings().getMaxPduSize() - 7;
                            }
                            if (len + getSigningSize(p) > p.getSettings().getMaxPduSize()) {
                                len -= getSigningSize(p);
                            }
                            ciphering = false;
                        }
                    } else if (p.getCommand() != Command.GET_REQUEST
                            && len + reply.size() > p.getSettings().getMaxPduSize()) {
                        len = p.getSettings().getMaxPduSize() - reply.size();
                        len -= getSigningSize(p);
                    }
                    reply.set(p.getData(), len);
                } else if ((p.getSettings().getGateway() != null
                        && p.getSettings().getGateway().getPhysicalDeviceAddress() != null)
                        && !(p.getCommand() == Command.GENERAL_BLOCK_TRANSFER
                                || (p.isMultipleBlocks() && (p.getSettings().getNegotiatedConformance()
                                        .contains(Conformance.GENERAL_BLOCK_TRANSFER))))) {
                    if (3 + len + p.getSettings().getGateway().getPhysicalDeviceAddress().length > p.getSettings()
                            .getMaxPduSize()) {
                        len -= (3 + p.getSettings().getGateway().getPhysicalDeviceAddress().length);
                    }
                    GXByteBuffer tmp = new GXByteBuffer(reply);
                    reply.size(0);
                    reply.setUInt8(Command.GATEWAY_REQUEST);
                    reply.setUInt8(p.getSettings().getGateway().getNetworkId());
                    reply.setUInt8(p.getSettings().getGateway().getPhysicalDeviceAddress().length);
                    reply.set(p.getSettings().getGateway().getPhysicalDeviceAddress());
                    reply.set(tmp);
                }
            }
            if (ciphering && reply.size() != 0 && p.getCommand() != Command.RELEASE_REQUEST && (!p.isMultipleBlocks()
                    || !p.getSettings().getNegotiatedConformance().contains(Conformance.GENERAL_BLOCK_TRANSFER))) {
                // GBT ciphering is done for all the data, not just block.
                byte[] tmp;
                boolean sign = shoudSign(p);
                if ((p.getSettings().getConnected() & ConnectionState.DLMS) == 0 || !sign) {
                    tmp = cipher0(p, reply.array());
                } else {
                    tmp = cipher1(p, reply.array(), sign);
                }
                reply.size(0);
                reply.set(tmp);
            }
        }
        if (p.getCommand() == Command.GENERAL_BLOCK_TRANSFER || (p.isMultipleBlocks()
                && p.getSettings().getNegotiatedConformance().contains(Conformance.GENERAL_BLOCK_TRANSFER))) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.set(reply);
            reply.clear();
            reply.setUInt8(Command.GENERAL_BLOCK_TRANSFER);
            int value = 0;
            // Is last block
            if (p.isLastBlock()) {
                value = 0x80;
            } else if (p.streaming) {
                value |= 0x40;
            }
            value |= p.windowSize;
            reply.setUInt8(value);
            // Set block number sent.
            reply.setUInt16(p.getBlockIndex());
            ++p.blockIndex;
            if (p.getCommand() != Command.DATA_NOTIFICATION && p.blockNumberAck != 0) {
                // Set block number acknowledged
                reply.setUInt16(p.blockNumberAck);
                ++p.blockNumberAck;
            } else {
                p.blockNumberAck = -1;
                reply.setUInt16(0);
            }
            // Add data length.
            GXCommon.setObjectCount(bb.size(), reply);
            reply.set(bb);
            if (p.getCommand() != Command.GENERAL_BLOCK_TRANSFER) {
                p.command = Command.GENERAL_BLOCK_TRANSFER;
                ++p.blockNumberAck;
            }

            if (p.getSettings().getGateway() != null
                    && p.getSettings().getGateway().getPhysicalDeviceAddress() != null) {
                if (3 + len + p.getSettings().getGateway().getPhysicalDeviceAddress().length > p.getSettings()
                        .getMaxPduSize()) {
                    // len -= (3 + p.getSettings().getGateway()
                    // .getPhysicalDeviceAddress().length);
                }
                GXByteBuffer tmp = new GXByteBuffer(reply);
                reply.size(0);
                reply.setUInt8(Command.GATEWAY_REQUEST);
                reply.setUInt8(p.getSettings().getGateway().getNetworkId());
                reply.setUInt8(p.getSettings().getGateway().getPhysicalDeviceAddress().length);
                reply.set(p.getSettings().getGateway().getPhysicalDeviceAddress());
                reply.set(tmp);
            }
        }
        if (useHdlc(p.getSettings().getInterfaceType())) {
            addLLCBytes(p.getSettings(), reply);
        }
    }

    static private boolean shoudSign(GXDLMSLNParameters p) {
        boolean signing = p.getSettings().getCipher().getSigning() == Signing.GENERAL_SIGNING;
        if (!signing) {
            // Association LN V3 and signing is not needed.
            if (p.getSettings().isServer()) {
                signing = (p.accessMode & AccessMode3.DIGITALLY_SIGNED_RESPONSE.getValue()) != 0;
            } else {
                signing = (p.accessMode & AccessMode3.DIGITALLY_SIGNED_REQUEST.getValue()) != 0;
            }
        }
        return signing;
    }

    static AesGcmParameter getCipheringParameters(GXDLMSLNParameters p) {
        int cmd;
        byte[] key;
        GXICipher cipher = p.getSettings().getCipher();
        // If client.
        if (p.cipheredCommand == Command.NONE) {
            // General protection can be used with pre-established connections.
            if (((p.getSettings().getConnected() & ConnectionState.DLMS) == 0
                    || (Conformance.toInteger(p.getSettings().getNegotiatedConformance())
                            & Conformance.GENERAL_PROTECTION.getValue()) == 0)
                    && (p.getSettings().getPreEstablishedSystemTitle() == null
                            || p.getSettings().getPreEstablishedSystemTitle().length == 0
                            || (Conformance.toInteger(p.getSettings().getProposedConformance())
                                    & Conformance.GENERAL_PROTECTION.getValue()) == 0)) {
                if (cipher.getDedicatedKey() != null && (p.getSettings().getConnected() & ConnectionState.DLMS) != 0) {
                    cmd = getDedMessage(p.getCommand());
                    key = cipher.getDedicatedKey();
                } else {
                    cmd = getGloMessage(p.getCommand());
                    key = getBlockCipherKey(p.getSettings());
                }
            } else {
                if (p.getSettings().getCipher().getDedicatedKey() != null) {
                    cmd = Command.GENERAL_DED_CIPHERING;
                    key = cipher.getDedicatedKey();
                } else {
                    cmd = Command.GENERAL_DED_CIPHERING;
                    key = getBlockCipherKey(p.getSettings());
                }
            }
        } else // If server.
        {
            if (p.cipheredCommand == Command.GENERAL_DED_CIPHERING) {
                cmd = (byte) Command.GENERAL_DED_CIPHERING;
                key = cipher.getDedicatedKey();
            } else if (p.cipheredCommand == Command.GENERAL_GLO_CIPHERING) {
                cmd = Command.GENERAL_GLO_CIPHERING;
                key = getBlockCipherKey(p.getSettings());
            } else if (p.getSettings().getCipher().getDedicatedKey() == null || isGloMessage(p.cipheredCommand)) {
                cmd = getGloMessage(p.command);
                key = getBlockCipherKey(p.getSettings());
            } else {
                cmd = getDedMessage(p.command);
                key = cipher.getDedicatedKey();
            }
        }
        AesGcmParameter s = new AesGcmParameter(p.getSettings(), cmd, cipher.getSecurity(), cipher.getSecuritySuite(),
                cipher.getInvocationCounter(), cipher.getSystemTitle(), key, getAuthenticationKey(p.getSettings()));
        s.setIgnoreSystemTitle(p.getSettings().getStandard() == Standard.ITALY);
        s.setRecipientSystemTitle(p.getSettings().getSourceSystemTitle());
        return s;
    }

    /**
     * Cipher using security suite 1 or 2.
     * 
     * @param p
     *            LN settings.
     * @param data
     *            Data to encrypt.
     * @throws SignatureException
     */
    private static byte[] cipher1(final GXDLMSLNParameters p, final byte[] data, boolean sign)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        if (!sign && p.getSettings().getCipher().getSigning() == Signing.GENERAL_SIGNING) {
            sign = true;
        }
        byte keyid;
        switch (p.getSettings().getCipher().getSigning()) {
        case ONE_PASS_DIFFIE_HELLMAN:
            keyid = 1;
            break;
        case STATIC_UNIFIED_MODEL:
            keyid = 2;
            break;
        default:
            keyid = 0;
            break;
        }
        byte sc = 0;
        GXICipher c = p.getSettings().getCipher();
        if (p.getSettings().getSourceSystemTitle() == null && p.getSettings().getPreEstablishedSystemTitle() == null) {
            throw new IllegalArgumentException("Invalid Recipient System Title.");
        }
        if (c.getSystemTitle() == null) {
            throw new IllegalArgumentException("Invalid System Title.");
        }

        Security security = c.getSecurity();
        switch (security) {
        case AUTHENTICATION:
            sc = 0x10;
            break;
        case AUTHENTICATION_ENCRYPTION:
            sc = 0x30;
            break;
        case ENCRYPTION:
            sc = 0x20;
            break;
        default:
            if (c.getSigning() != Signing.GENERAL_SIGNING) {
                throw new IllegalArgumentException("Invalid security.");
            }
            break;
        }
        String alg, algID;
        int keyDataLen;
        switch (c.getSecuritySuite()) {
        case SUITE_1:
            sc |= 1;
            alg = "SHA-256";
            // AES-GCM-128
            algID = "60857405080300";
            keyDataLen = 256;
            break;
        case SUITE_2:
            sc |= 2;
            alg = "SHA-384";
            // AES-GCM-256
            algID = "60857405080301";
            keyDataLen = 384;
            break;
        default:
            throw new IllegalArgumentException("Invalid security suite.");
        }
        GXByteBuffer tmp2 = new GXByteBuffer();
        byte[] z = null;
        PrivateKey key = null;
        PublicKey pub = null;
        if (!sign) {
            // If external Hardware Security Module is used.
            byte[] ret = GXCommon.crypt(p.getSettings(), CertificateType.KEY_AGREEMENT, data, true, CryptoKeyType.ECDSA,
                    0, c.getSecurity(), c.getSecuritySuite(), c.getInvocationCounter());
            if (ret != null) {
                p.getSettings().getCipher()
                        .setInvocationCounter(1 + p.getSettings().getCipher().getInvocationCounter());
                return ret;
            }
            if (c.getKeyAgreementKeyPair() != null) {
                key = c.getKeyAgreementKeyPair().getPrivate();
                pub = c.getKeyAgreementKeyPair().getPublic();
            }
            if (key == null) {
                key = (PrivateKey) p.getSettings().getKey(CertificateType.KEY_AGREEMENT, c.getSystemTitle(), true);
                c.setKeyAgreementKeyPair(new KeyPair(pub, key));
            }
            if (pub == null) {
                pub = (PublicKey) p.getSettings().getKey(CertificateType.KEY_AGREEMENT,
                        p.getSettings().getSourceSystemTitle(), false);
                c.setKeyAgreementKeyPair(new KeyPair(pub, key));
            }
            if (keyid == 1) {
                // Generate ephemeral key pair for each transaction.
                KeyPair kp = GXEcdsa.generateKeyPair(Ecc.P256);
                c.setEphemeralKeyPair(kp);
                KeyAgreement ka = KeyAgreement.getInstance("ECDH");
                ka.init(kp.getPrivate());
                ka.doPhase(c.getKeyAgreementKeyPair().getPublic(), true);
                z = ka.generateSecret();
                Logger.getLogger(GXSecure.class.getName()).log(Level.FINEST, "Private ephemeral{0}",
                        GXCommon.toHex(GXAsn1Converter.rawValue(kp.getPrivate()), true));
                Logger.getLogger(GXSecure.class.getName()).log(Level.FINEST, "Public ephemeral: {0}",
                        GXCommon.toHex(GXAsn1Converter.rawValue(kp.getPublic()), true));
                Logger.getLogger(GXSecure.class.getName()).log(Level.FINEST, "Public agreement key: {0}",
                        GXCommon.toHex(GXAsn1Converter.rawValue(c.getKeyAgreementKeyPair().getPublic()), true));
            } else if (keyid == 2) {
                Logger.getLogger(GXSecure.class.getName()).log(Level.FINEST, "Private agreement key {0}",
                        GXCommon.toHex(GXAsn1Converter.rawValue(key), true));
                Logger.getLogger(GXSecure.class.getName()).log(Level.FINEST, "Public ephemeral key: {0}",
                        GXCommon.toHex(GXAsn1Converter.rawValue(pub), true));
                KeyAgreement ka = KeyAgreement.getInstance("ECDH");
                ka.init(c.getKeyAgreementKeyPair().getPrivate());
                ka.doPhase(c.getKeyAgreementKeyPair().getPublic(), true);
                z = ka.generateSecret();
                if (c.getTransactionId() == null) {
                    tmp2.setUInt8(0x0);
                } else {
                    tmp2.setUInt8(c.getTransactionId().length);
                    tmp2.set(c.getTransactionId());
                }
            }
        } else {
            if (c.getSigningKeyPair() != null) {
                key = c.getSigningKeyPair().getPrivate();
                pub = c.getSigningKeyPair().getPublic();
            }
            if (key == null) {
                key = (PrivateKey) p.getSettings().getKey(CertificateType.DIGITAL_SIGNATURE,
                        p.getSettings().getCipher().getSystemTitle(), true);
                c.setSigningKeyPair(new KeyPair(pub, key));
            }
            if (pub == null) {
                pub = (PublicKey) p.getSettings().getKey(CertificateType.DIGITAL_SIGNATURE,
                        p.getSettings().getSourceSystemTitle(), false);
                c.setSigningKeyPair(new KeyPair(pub, key));
            }
            Logger.getLogger(GXDLMS.class.getName()).log(Level.FINEST,
                    "Private signing key: {0}" + GXCommon.toHex(key.getEncoded()));
            Logger.getLogger(GXDLMS.class.getName()).log(Level.FINEST,
                    "Public signing key: {0}" + GXCommon.toHex(pub.getEncoded()));
        }

        tmp2.set(p.getSettings().getSourceSystemTitle());
        if (z != null) {
            Logger.getLogger(GXSecure.class.getName()).log(Level.FINEST, "Shared secret {0}", GXCommon.toHex(z));
        }
        AesGcmParameter s;
        if (sign) {
            s = getCipheringParameters(p);
        } else {
            GXByteBuffer kdf = new GXByteBuffer();
            kdf.set(GXSecure.generateKDF(alg, z, keyDataLen, GXCommon.hexToBytes(algID), c.getSystemTitle(),
                    tmp2.array(), null, null));
            Logger.getLogger(GXDLMS.class.getName()).log(Level.FINEST, "kdf: {0}", kdf);
            s = new AesGcmParameter(0x31, p.getSettings(), security, c.getSecuritySuite(), 0,
                    // KDF
                    kdf.subArray(0, 16),
                    // Authentication key.
                    c.getAuthenticationKey(),
                    // Originator system title.
                    c.getSystemTitle(),
                    // recipient system title.
                    p.getSettings().getSourceSystemTitle(),
                    // Date time
                    null,
                    // Other information.
                    null);
        }
        GXByteBuffer reply = new GXByteBuffer();
        if (sign) {
            reply.setUInt8(Command.GENERAL_SIGNING);
        } else {
            reply.setUInt8(Command.GENERAL_CIPHERING);
        }
        if (c.getTransactionId() == null) {
            GXCommon.setObjectCount(0, reply);
        } else {
            GXCommon.setObjectCount(c.getTransactionId().length, reply);
            reply.set(c.getTransactionId());
        }
        GXCommon.setObjectCount(s.getSystemTitle().length, reply);
        reply.set(s.getSystemTitle());
        GXCommon.setObjectCount(s.getRecipientSystemTitle().length, reply);
        reply.set(s.getRecipientSystemTitle());
        // date-time not present.
        reply.setUInt8(0);
        // other-information not present
        reply.setUInt8(0);
        if (!sign) {
            // optional flag
            reply.setUInt8(1);
            // agreed-key CHOICE
            reply.setUInt8(2);
            // key-parameters
            reply.setUInt8(1);
            reply.setUInt8(keyid);
        }
        if (keyid == 1) {
            try {
                if (c.getSigningKeyPair() != null) {
                    key = c.getSigningKeyPair().getPrivate();
                    pub = c.getSigningKeyPair().getPublic();
                } else {
                    key = null;
                    pub = null;
                }
                if (key == null) {
                    key = (PrivateKey) p.getSettings().getKey(CertificateType.DIGITAL_SIGNATURE,
                            p.getSettings().getCipher().getSystemTitle(), true);
                    c.setSigningKeyPair(new KeyPair(pub, key));
                }
                if (pub == null) {
                    pub = (PublicKey) p.getSettings().getKey(CertificateType.DIGITAL_SIGNATURE,
                            p.getSettings().getSourceSystemTitle(), false);
                    c.setSigningKeyPair(new KeyPair(pub, key));
                }
                // key-ciphered-data
                GXCommon.setObjectCount(0x80, reply);
                // Ephemeral public key client.
                byte[] tmp = GXAsn1Converter.rawValue(c.getEphemeralKeyPair().getPublic());
                reply.set(tmp);
                // Ephemeral Public Key Signature.
                reply.set(GXSecure.getEphemeralPublicKeySignature(keyid, c.getEphemeralKeyPair().getPublic(),
                        c.getSigningKeyPair().getPrivate()));
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } else if (!sign) {
            reply.setUInt8(0);
        }
        // ciphered-content
        s.setType(CountType.DATA | CountType.TAG);
        byte[] tmp = GXCiphering.encrypt(s, data);
        // Content length is not add for the signed data.
        GXByteBuffer signedData = new GXByteBuffer();
        signedData.set(reply.getData(), 1, reply.size() - 1);
        if (c.getSecurity() != Security.NONE) {
            if (sign) {
                // Content length is not add for the signed data.
                GXCommon.setObjectCount(6 + GXCommon.getObjectCountSizeInBytes(5 + tmp.length) + tmp.length, reply);
                // Add ciphered command.
                if (p.getSettings().getCipher().getDedicatedKey() == null) {
                    reply.setUInt8(getGloMessage(p.command));
                    signedData.setUInt8(getGloMessage(p.command));
                } else {
                    reply.setUInt8(getDedMessage(p.command));
                    signedData.setUInt8(getDedMessage(p.command));
                }
            }
            // Len
            GXCommon.setObjectCount(5 + tmp.length, reply);
            GXCommon.setObjectCount(5 + tmp.length, signedData);
            // Add SC
            reply.setUInt8(sc);
            signedData.setUInt8(sc);
            // Add IC.
            reply.setUInt32(p.getSettings().getCipher().getInvocationCounter());
            signedData.setUInt32(p.getSettings().getCipher().getInvocationCounter());
        } else if (!sign) {
            // Length.
            GXCommon.setObjectCount(tmp.length, reply);
        }
        p.getSettings().getCipher().setInvocationCounter(1 + p.getSettings().getCipher().getInvocationCounter());
        reply.set(tmp);
        signedData.set(tmp);
        if (sign) {
            // Signature
            Signature sig;
            if (p.getSettings().getCipher().getSecuritySuite() == SecuritySuite.SUITE_1) {
                sig = Signature.getInstance("SHA256withECDSA");
            } else if (p.getSettings().getCipher().getSecuritySuite() == SecuritySuite.SUITE_2) {
                sig = Signature.getInstance("SHA384withECDSA");
            } else {
                throw new IllegalArgumentException("Invalid security suite.");
            }
            if (c.getSigningKeyPair() == null) {
                throw new IllegalArgumentException("SigningKeyPair is empty.");
            }
            sig.initSign(key);
            sig.update(signedData.array());
            GXAsn1Sequence seq = (GXAsn1Sequence) GXAsn1Converter.fromByteArray(sig.sign());
            GXByteBuffer bb = new GXByteBuffer();
            bb.set(((GXAsn1Integer) seq.get(0)).getByteArray());
            if (bb.size() != 32) {
                bb.move(1, 0, 32);
            }
            bb.set(((GXAsn1Integer) seq.get(1)).getByteArray());
            if (bb.size() != 64) {
                bb.move(33, 32, 32);
            }
            byte[] signature = bb.array();
            GXCommon.setObjectCount(signature.length, reply);
            reply.set(signature);
        }
        return reply.array();
    }

    static boolean isGloMessage(int cmd) {
        return cmd == Command.GLO_GET_REQUEST || cmd == Command.GLO_SET_REQUEST || cmd == Command.GLO_METHOD_REQUEST;
    }

    private static byte[] getBlockCipherKey(final GXDLMSSettings settings) {
        if (settings.getEphemeralBlockCipherKey() != null) {
            return settings.getEphemeralBlockCipherKey();
        }
        return settings.getCipher().getBlockCipherKey();

    }

    private static byte[] getAuthenticationKey(final GXDLMSSettings settings) {
        if (settings.getEphemeralAuthenticationKey() != null) {
            return settings.getEphemeralAuthenticationKey();
        }
        return settings.getCipher().getAuthenticationKey();
    }

    /*
     * Cipher using security suite 0.
     * @param p LN settings.
     * @param data Data to encrypt.
     */
    static byte[] cipher0(final GXDLMSLNParameters p, final byte[] data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        int cmd;
        byte[] key;
        GXICipher cipher = p.getSettings().getCipher();
        // If client.
        if (p.getCipheredCommand() == Command.NONE) {
            if (((p.getSettings().getConnected() & ConnectionState.DLMS) == 0
                    || !p.getSettings().getNegotiatedConformance().contains(Conformance.GENERAL_PROTECTION))
                    // General protection can be used with pre-established
                    // connections.
                    && ((p.getSettings().getPreEstablishedSystemTitle() == null
                            || p.getSettings().getPreEstablishedSystemTitle().length == 0
                            || !p.getSettings().getProposedConformance().contains(Conformance.GENERAL_PROTECTION)))) {
                if (cipher.getDedicatedKey() != null && (p.getSettings().getConnected() & ConnectionState.DLMS) != 0) {
                    cmd = getDedMessage(p.command);
                    key = cipher.getDedicatedKey();
                } else {
                    cmd = getGloMessage(p.getCommand());
                    key = getBlockCipherKey(p.getSettings());
                }
            } else {
                if (cipher.getDedicatedKey() != null) {
                    cmd = Command.GENERAL_DED_CIPHERING;
                    key = cipher.getDedicatedKey();
                } else {
                    cmd = Command.GENERAL_GLO_CIPHERING;
                    key = getBlockCipherKey(p.getSettings());
                }
            }
        } else {// If server.
            if (p.getCipheredCommand() == Command.GENERAL_DED_CIPHERING) {
                cmd = Command.GENERAL_DED_CIPHERING;
                key = cipher.getDedicatedKey();
            } else if (p.getCipheredCommand() == Command.GENERAL_GLO_CIPHERING) {
                cmd = Command.GENERAL_GLO_CIPHERING;
                key = getBlockCipherKey(p.getSettings());
            } else if (isGloMessage(p.getCipheredCommand())) {
                cmd = getGloMessage(p.command);
                key = getBlockCipherKey(p.getSettings());
            } else {
                cmd = getDedMessage(p.command);
                key = cipher.getDedicatedKey();
            }
        }
        // If external Hardware Security Module is used.
        CryptoKeyType keyType;
        switch (cipher.getSecurity()) {
        case AUTHENTICATION:
            keyType = CryptoKeyType.AUTHENTICATION;
            break;
        case ENCRYPTION:
            keyType = CryptoKeyType.BLOCK_CIPHER;
            break;
        case AUTHENTICATION_ENCRYPTION:
            keyType = CryptoKeyType
                    .forValue(CryptoKeyType.AUTHENTICATION.getValue() | CryptoKeyType.BLOCK_CIPHER.getValue());
            break;
        default:
            throw new IllegalArgumentException("Security");
        }
        AesGcmParameter s = new AesGcmParameter(p.getSettings(), cmd, cipher.getSecurity(), cipher.getSecuritySuite(),
                cipher.getInvocationCounter(), cipher.getSystemTitle(), key, getAuthenticationKey(p.getSettings()));
        byte[] tmp = GXCiphering.encrypt(s, data);
        cipher.setInvocationCounter(1 + cipher.getInvocationCounter());
        return tmp;
    }

    /*
     * Get all Logical name messages. Client uses this to generate messages.
     * @param p LN settings.
     * @return Generated messages.
     */
    public static List<byte[]> getLnMessages(final GXDLMSLNParameters p)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        GXByteBuffer reply = new GXByteBuffer();
        java.util.ArrayList<byte[]> messages = new ArrayList<byte[]>();
        byte frame = 0;
        if (p.getCommand() == Command.DATA_NOTIFICATION || p.getCommand() == Command.EVENT_NOTIFICATION) {
            frame = 0x13;
        }
        do {
            getLNPdu(p, reply);
            p.setLastBlock(true);
            if (p.getAttributeDescriptor() == null) {
                p.getSettings().increaseBlockIndex();
            }
            while (reply.position() != reply.size()) {
                switch (p.getSettings().getInterfaceType()) {
                case WRAPPER:
                    messages.add(getWrapperFrame(p.getSettings(), p.getCommand(), reply));
                    break;
                case HDLC:
                case HDLC_WITH_MODE_E:
                    messages.add(GXDLMS.getHdlcFrame(p.getSettings(), frame, reply));
                    if (reply.position() != reply.size()) {
                        frame = p.getSettings().getNextSend(false);
                    }
                    break;
                case PDU:
                    messages.add(reply.array());
                    reply.position(reply.size());
                    break;
                case PLC:
                    messages.add(GXDLMS.getPlcFrame(p.getSettings(), (byte) 0x90, reply));
                    break;
                case PLC_HDLC:
                    messages.add(GXDLMS.getMacHdlcFrame(p.getSettings(), frame, (byte) 0, reply));
                    break;
                case SMS:
                    messages.add(getSMSFrame(p.getSettings(), p.command, reply));
                    break;
                case COAP:
                    messages.add(getCoAPFrame(p.getSettings(), p.command, reply, 0));
                    break;
                default:
                    throw new IllegalArgumentException("InterfaceType");
                }
            }
            reply.clear();
            frame = 0;
        } while (p.getData() != null && p.getData().position() != p.getData().size());
        return messages;
    }

    /*
     * Get all Short Name messages. Client uses this to generate messages.
     * @param p DLMS SN parameters.
     * @return Generated SN messages.
     */
    public static List<byte[]> getSnMessages(final GXDLMSSNParameters p)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        GXByteBuffer reply = new GXByteBuffer();
        java.util.ArrayList<byte[]> messages = new java.util.ArrayList<byte[]>();
        byte frame = 0x0;
        if (p.getCommand() == Command.INFORMATION_REPORT || p.getCommand() == Command.DATA_NOTIFICATION) {
            frame = 0x13;
        }
        do {
            getSNPdu(p, reply);
            // Command is not add to next PDUs.
            while (reply.position() != reply.size()) {
                if (p.getSettings().getInterfaceType() == InterfaceType.WRAPPER) {
                    messages.add(getWrapperFrame(p.getSettings(), p.getCommand(), reply));
                } else if (p.getSettings().getInterfaceType() == InterfaceType.HDLC) {
                    messages.add(getHdlcFrame(p.getSettings(), frame, reply));
                    if (reply.position() != reply.size()) {
                        frame = p.getSettings().getNextSend(false);
                    }
                } else if (p.getSettings().getInterfaceType() == InterfaceType.PDU) {
                    messages.add(reply.array());
                    break;
                } else {
                    throw new IllegalArgumentException("InterfaceType");
                }
            }
            reply.clear();
            frame = 0;
        } while (p.getData() != null && p.getData().position() != p.getData().size());
        return messages;

    }

    private static int appendMultipleSNBlocks(final GXDLMSSNParameters p, final GXByteBuffer reply) {
        boolean ciphering = p.getSettings().isCiphered(false);
        int hSize = reply.size() + 3;
        // Add LLC bytes.
        if (p.getCommand() == Command.WRITE_REQUEST || p.getCommand() == Command.READ_REQUEST) {
            hSize += 1 + GXCommon.getObjectCountSizeInBytes(p.getCount());
        }
        int maxSize = p.getSettings().getMaxPduSize() - hSize;
        if (ciphering) {
            maxSize -= CIPHERING_HEADER_SIZE;
            if (useHdlc(p.getSettings().getInterfaceType())) {
                maxSize -= 3;
            }
        }
        maxSize -= GXCommon.getObjectCountSizeInBytes(maxSize);
        if (p.getData().size() - p.getData().position() > maxSize) {
            // More blocks.
            reply.setUInt8(0);
        } else {
            // Last block.
            reply.setUInt8(1);
            maxSize = p.getData().size() - p.getData().position();
        }
        // Add block index.
        reply.setUInt16(p.getBlockIndex());
        if (p.getCommand() == Command.WRITE_REQUEST) {
            p.setBlockIndex(p.getBlockIndex() + 1);
            GXCommon.setObjectCount(p.getCount(), reply);
            reply.setUInt8(DataType.OCTET_STRING.getValue());
        } else if (p.getCommand() == Command.READ_REQUEST) {
            p.setBlockIndex(p.getBlockIndex() + 1);
        }

        GXCommon.setObjectCount(maxSize, reply);
        return maxSize;
    }

    /*
     * @param p
     * @param reply
     */
    public static void getSNPdu(final GXDLMSSNParameters p, final GXByteBuffer reply)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        boolean ciphering =
                p.getCommand() != Command.AARQ && p.getCommand() != Command.AARE && p.getSettings().isCiphered(false);
        if ((!ciphering || p.getCommand() == Command.AARQ || p.getCommand() == Command.AARE)
                && useHdlc(p.getSettings().getInterfaceType())) {
            if (p.getSettings().isServer()) {
                reply.set(GXCommon.LLC_REPLY_BYTES);
            } else if (reply.size() == 0) {
                reply.set(GXCommon.LLC_SEND_BYTES);
            }
        }
        int cnt = 0, cipherSize = 0;
        if (ciphering) {
            cipherSize = CIPHERING_HEADER_SIZE;
        }
        if (p.getData() != null) {
            cnt = p.getData().size() - p.getData().position();
        }
        // Add command.
        if (p.getCommand() == Command.INFORMATION_REPORT) {
            reply.setUInt8(p.getCommand());
            // Add date time.
            if (p.getTime() == null) {
                reply.setUInt8(DataType.NONE.getValue());
            } else {
                // Data is send in octet string. Remove data type.
                int pos = reply.size();
                GXCommon.setData(null, reply, DataType.OCTET_STRING, p.getTime());
                reply.move(pos + 1, pos, reply.size() - pos - 1);
            }
            GXCommon.setObjectCount(p.getCount(), reply);
            reply.set(p.getAttributeDescriptor());
        } else if (p.getCommand() != Command.AARQ && p.getCommand() != Command.AARE) {
            reply.setUInt8(p.getCommand());
            if (p.getCount() != 0xFF) {
                GXCommon.setObjectCount(p.getCount(), reply);
            }
            if (p.getRequestType() != 0xFF) {
                reply.setUInt8(p.getRequestType());
            }
            reply.set(p.getAttributeDescriptor());

            if (!p.isMultipleBlocks()) {
                p.setMultipleBlocks(reply.size() + cipherSize + cnt > p.getSettings().getMaxPduSize());
                // If reply data is not fit to one PDU.
                if (p.isMultipleBlocks()) {
                    reply.size(0);
                    if (!ciphering && useHdlc(p.getSettings().getInterfaceType())) {
                        if (p.getSettings().isServer()) {
                            reply.set(GXCommon.LLC_REPLY_BYTES);
                        } else if (reply.size() == 0) {
                            reply.set(GXCommon.LLC_SEND_BYTES);
                        }
                    }
                    // CHECKSTYLE:OFF
                    if (p.getCommand() == Command.WRITE_REQUEST) {
                        p.setRequestType(VariableAccessSpecification.WRITE_DATA_BLOCK_ACCESS);
                    } else if (p.getCommand() == Command.READ_REQUEST) {
                        p.setRequestType(VariableAccessSpecification.READ_DATA_BLOCK_ACCESS);
                    } else if (p.getCommand() == Command.READ_RESPONSE) {
                        p.setRequestType(SingleReadResponse.DATA_BLOCK_RESULT);
                    } else {
                        throw new IllegalArgumentException("Invalid command.");
                    }
                    // CHECKSTYLE:ON
                    reply.setUInt8(p.getCommand());
                    // Set object count.
                    reply.setUInt8(1);
                    if (p.getRequestType() != 0xFF) {
                        reply.setUInt8(p.getRequestType());
                    }
                    cnt = appendMultipleSNBlocks(p, reply);
                }
            } else {
                cnt = appendMultipleSNBlocks(p, reply);
            }
        }
        // Add data.
        reply.set(p.getData(), cnt);
        // If all data is transfered.
        if (p.getData() != null && p.getData().position() == p.getData().size()) {
            p.getSettings().setIndex(0);
            p.getSettings().setCount(0);
        }
        // If Ciphering is used.
        if (ciphering && p.getCommand() != Command.AARQ && p.getCommand() != Command.AARE) {
            GXICipher cipher = p.getSettings().getCipher();
            AesGcmParameter s = new AesGcmParameter(p.getSettings(), getGloMessage(p.getCommand()),
                    cipher.getSecurity(), cipher.getSecuritySuite(), cipher.getInvocationCounter(),
                    cipher.getSystemTitle(), getBlockCipherKey(p.getSettings()), getAuthenticationKey(p.getSettings()));
            byte[] tmp = GXCiphering.encrypt(s, reply.array());
            reply.size(0);
            if (useHdlc(p.getSettings().getInterfaceType())) {
                if (p.getSettings().isServer()) {
                    reply.set(GXCommon.LLC_REPLY_BYTES);
                } else if (reply.size() == 0) {
                    reply.set(GXCommon.LLC_SEND_BYTES);
                }
            }
            reply.set(tmp);
        }
    }

    static Object getAddress(final long value, final int size) {
        if (size < 2 && value < 0x80) {
            return (byte) (value << 1 | 1);
        }
        if (size < 4 && value < 0x4000) {
            return (short) ((value & 0x3F80) << 2 | (value & 0x7F) << 1 | 1);
        }
        if (value < 0x10000000) {
            return (int) ((value & 0xFE00000) << 4 | (value & 0x1FC000) << 3 | (value & 0x3F80) << 2
                    | (value & 0x7F) << 1 | 1);
        }
        throw new IllegalArgumentException("Invalid address.");
    }

    /**
     * @param value
     * @param size
     */
    private static byte[] getAddressBytes(final int value, final int size) {
        Object tmp = getAddress(value, size);
        GXByteBuffer bb = new GXByteBuffer();
        if (tmp instanceof Byte) {
            bb.setUInt8(((Byte) tmp).byteValue());
        } else if (tmp instanceof Short) {
            bb.setUInt16(((Short) tmp).intValue());
        } else if (tmp instanceof Integer) {
            bb.setUInt32(((Integer) tmp).intValue());
        } else if (tmp instanceof GXUInt8) {
            bb.setUInt8(((GXUInt8) tmp).byteValue());
        } else if (tmp instanceof GXUInt16) {
            bb.setUInt16(((GXUInt16) tmp).intValue());
        } else if (tmp instanceof GXUInt32) {
            bb.setUInt32(((GXUInt32) tmp).intValue());
        } else {
            throw new IllegalArgumentException("Invalid address type.");
        }
        return bb.array();
    }

    /**
     * Split DLMS PDU to wrapper frames.
     * 
     * @param settings
     *            DLMS settings.
     * @param command
     *            DLMS command.
     * @param data
     *            Wrapped data.
     * @return Wrapper frames.
     */
    static byte[] getWrapperFrame(final GXDLMSSettings settings, final int command, final GXByteBuffer data) {
        GXByteBuffer bb = new GXByteBuffer();
        // Add version.
        bb.setUInt16(1);
        if (settings.isServer()) {
            bb.setUInt16(settings.getServerAddress());
            if (settings.getPushClientAddress() != 0
                    && (command == Command.DATA_NOTIFICATION || command == Command.EVENT_NOTIFICATION)) {
                bb.setUInt16(settings.getPushClientAddress());
            } else {
                bb.setUInt16(settings.getClientAddress());
            }
        } else {
            bb.setUInt16(settings.getClientAddress());
            bb.setUInt16(settings.getServerAddress());
        }
        if (data == null) {
            // Data length.
            bb.setUInt16(0);
        } else {
            // Data length.
            bb.setUInt16(data.size());
            // Data
            bb.set(data);
        }
        // Remove sent data in server side.
        if (settings.isServer() && data != null) {
            if (data.size() == data.position()) {
                data.clear();
            } else {
                data.move(data.position(), 0, data.size() - data.position());
                data.position(0);
            }
        }
        return bb.array();
    }

    /**
     * Split DLMS PDU to SMS frames.
     * 
     * @param settings
     *            DLMS settings.
     * @param command
     *            DLMS command.
     * @param data
     *            Wrapped data.
     * @return SMS frame
     */
    public static byte[] getSMSFrame(final GXDLMSSettings settings, final int command, final GXByteBuffer data) {
        GXByteBuffer bb = new GXByteBuffer();
        if (settings.isServer()) {
            bb.setUInt8((byte) settings.getServerAddress());
            if (settings.getPushClientAddress() != 0
                    && (command == Command.DATA_NOTIFICATION || command == Command.EVENT_NOTIFICATION)) {
                bb.setUInt8(settings.getPushClientAddress());
            } else {
                bb.setUInt8(settings.getClientAddress());
            }
        } else {
            bb.setUInt8(settings.getClientAddress());
            bb.setUInt8(settings.getServerAddress());
        }
        // Data
        bb.set(data);
        // Remove sent data in server side.
        if (settings.isServer()) {
            data.clear();
        }
        return bb.array();
    }

    private static int addOpt(final GXByteBuffer bb, final int type, int last, final Object data) {
        int len;
        if (type < last) {
            last = 0;
        }
        GXByteBuffer value = new GXByteBuffer();
        if (data instanceof Byte) {
            len = 1;
            value.setUInt8((Byte) data);
        } else if (data instanceof Integer) {
            len = 2;
            value.setUInt16((Integer) data);
        } else if (data instanceof Long) {
            len = 4;
            value.setUInt32((Long) data);
        } else if (data instanceof byte[]) {
            value.set((byte[]) data);
            len = ((byte[]) data).length;
        } else if (data instanceof String) {
            value.set(((String) data).getBytes());
            len = ((String) data).length();
        } else {
            throw new IllegalArgumentException("Invalid CoAP option type.");
        }
        // Add opt delta to type.
        if (type - last < 13) {
            len |= ((type - last) << 4);
            bb.setUInt8(len);
        } else if (type - last < 269) {
            len |= (13 << 4);
            bb.setUInt8(len);
            // Opt delta extended.
            int delta = (type - last + 1);
            bb.setUInt8(delta);
        } else {
            len |= (byte) (14 << 4);
            bb.setUInt8(len);
            // Opt delta extended.
            int delta = type;
            delta -= (269 + last);
            bb.setUInt16(delta);
        }
        bb.set(value);
        return type;
    }

    /**
     * Split DLMS PDU to CoAP frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param command
     *            DLMS command.
     * @param data
     *            Wrapped data.
     * @param moreData
     *            moreData.
     * @return SMS frame
     */
    static byte[] getCoAPFrame(GXDLMSSettings settings, int command, GXByteBuffer data, int moreData) {
        GXByteBuffer bb = new GXByteBuffer();
        // Add version.
        byte version = 1;
        int type;
        byte tokenLen;
        int code;
        if (settings.getCoap().getToken().longValue() < 0x100) {
            tokenLen = 1;
        } else if (settings.getCoap().getToken().longValue() < 0x10000) {
            tokenLen = 2;
        } else if (settings.getCoap().getToken().longValue() < 0x100000000L) {
            tokenLen = 4;
        } else {
            tokenLen = 8;
        }
        if ((moreData & RequestTypes.FRAME.getValue()) != 0) {
            type = CoAPType.ACKNOWLEDGEMENT.getValue();
            code = (byte) CoAPClass.SUCCESS.getValue() << 5 | CoAPSuccess.CONTINUE.getValue();
        } else if (!settings.isServer() || command == Command.DATA_NOTIFICATION
                || command == Command.EVENT_NOTIFICATION) {
            code = (CoAPClass.METHOD.getValue() << 5) | CoAPMethod.POST.getValue();
            type = CoAPType.CONFIRMABLE.getValue();
        } else {
            type = CoAPType.ACKNOWLEDGEMENT.getValue();
            code = CoAPClass.SUCCESS.getValue() << 5 | CoAPSuccess.CREATED.getValue();
        }
        // Add confirmable.
        bb.setUInt8((version << 6 | (type << 4) | tokenLen));
        // Add code.
        bb.setUInt8(code);
        if ((moreData & RequestTypes.FRAME.getValue()) == 0) {
            settings.getCoap().setMessageId(1 + settings.getCoap().getMessageId());
        }
        bb.setUInt16(settings.getCoap().getMessageId());
        if (settings.getCoap().getToken().longValue() < 0x100) {
            bb.setUInt8((byte) settings.getCoap().getToken().longValue());
        } else if (settings.getCoap().getToken().longValue() < 0x10000) {
            bb.setUInt16((short) settings.getCoap().getToken().longValue());
        } else if (settings.getCoap().getToken().longValue() < 0x100000000L) {
            bb.setUInt32((int) settings.getCoap().getToken().longValue());
        } else {
            bb.setUInt64(settings.getCoap().getToken().longValue());
        }
        int lastOptionType = 14;
        if (type == CoAPType.CONFIRMABLE.getValue()) {
            // Add Uri-host.
            if (settings.getCoap().getHost() != null && !settings.getCoap().getHost().equals("")) {
                lastOptionType =
                        addOpt(bb, CoAPOptionType.URI_HOST.getValue(), lastOptionType, settings.getCoap().getHost());
            }
            if (settings.getCoap().getPort() != 0) {
                // Add Uri-port.
                lastOptionType =
                        addOpt(bb, CoAPOptionType.URI_PORT.getValue(), lastOptionType, settings.getCoap().getPort());
            }
            // Add Uri-port.
            if (settings.getCoap().getPath() != null && !settings.getCoap().getPath().equals("")) {
                lastOptionType =
                        addOpt(bb, CoAPOptionType.URI_PATH.getValue(), lastOptionType, settings.getCoap().getPath());
            }
        }
        if ((moreData & RequestTypes.FRAME.getValue()) == 0) {
            lastOptionType = addOpt(bb, CoAPOptionType.CONTENT_FORMAT.getValue(), lastOptionType,
                    (Integer) settings.getCoap().getContentFormat().getValue());
        }
        if (settings.getCoap().getMaxAge() != 0) {
            lastOptionType =
                    addOpt(bb, CoAPOptionType.MAX_AGE.getValue(), lastOptionType, settings.getCoap().getMaxAge());
        }

        if ((moreData & RequestTypes.FRAME.getValue()) != 0) {
            lastOptionType = addOpt(bb, CoAPOptionType.BLOCK1.getValue(), lastOptionType,
                    (byte) ((settings.getCoap().getBlockNumber() << 4) | 0xE));
        }
        if (settings.getCoap().getBlockNumber() != 0) {
            lastOptionType = addOpt(bb, CoAPOptionType.BLOCK2.getValue(), lastOptionType,
                    (byte) ((settings.getCoap().getBlockNumber() << 4) | 0xE));
        }

        for (int it : settings.getCoap().getOptions().keySet()) {
            lastOptionType = addOpt(bb, it, lastOptionType, settings.getCoap().getOptions().get(it));
        }
        // End of options marker.
        bb.setUInt8(0xFF);
        // Data
        bb.set(data);
        // Remove sent data in server side.
        if (settings.isServer()) {
            if (data.size() == data.position()) {
                data.clear();
            } else {
                data.move(data.position(), 0, data.size() - data.position());
                data.position(0);
            }
        }
        return bb.array();
    }

    /**
     * Get HDLC frame for data.
     * 
     * @param settings
     *            DLMS settings.
     * @param frame
     *            Frame ID. If zero new is generated.
     * @param data
     *            Data to add.
     * @return HDLC frame.
     */
    static byte[] getHdlcFrame(final GXDLMSSettings settings, final int frame, final GXByteBuffer data) {
        GXByteBuffer bb = new GXByteBuffer();
        int frameSize, len = 0;
        byte[] primaryAddress, secondaryAddress;
        if (settings.isServer()) {
            if (frame == 0x13 && settings.getPushClientAddress() != 0) {
                primaryAddress = getAddressBytes(settings.getPushClientAddress(), 0);
            } else {
                primaryAddress = getAddressBytes(settings.getClientAddress(), 0);
            }
            secondaryAddress = getAddressBytes(settings.getServerAddress(), settings.getServerAddressSize());
            len = secondaryAddress.length;
        } else {
            primaryAddress = getAddressBytes(settings.getServerAddress(), settings.getServerAddressSize());
            secondaryAddress = getAddressBytes(settings.getClientAddress(), 0);
            len = primaryAddress.length;
        }
        // Add BOP
        bb.setUInt8(HDLC_FRAME_START_END);
        frameSize = settings.getHdlcSettings().getMaxInfoTX();

        // Remove BOP, type, len, primaryAddress, secondaryAddress, frame,
        // header CRC, data CRC and EOP from data length.
        if (settings.getHdlcSettings().isUseFrameSize()) {
            frameSize -= (10 + len);
        } else {
            if (data != null && data.position() == 0) {
                frameSize -= 3;
            }
        }

        // If no data
        if (data == null || data.size() == 0) {
            len = 0;
            bb.setUInt8(0xA0);
        } else if (data.size() - data.position() <= frameSize) {
            len = data.size() - data.position();
            // Is last packet.
            bb.setUInt8(0xA0 | (((7 + primaryAddress.length + secondaryAddress.length + len) >> 8) & 0x7));
        } else {
            len = frameSize;
            // More data to left.
            bb.setUInt8(0xA8 | ((7 + primaryAddress.length + secondaryAddress.length + len >> 8) & 0x7));
        }
        // Frame len.
        if (len == 0) {
            bb.setUInt8((byte) (5 + primaryAddress.length + secondaryAddress.length + len));
        } else {
            bb.setUInt8((byte) (7 + primaryAddress.length + secondaryAddress.length + len));
        }
        // Add primary address.
        bb.set(primaryAddress);
        // Add secondary address.
        bb.set(secondaryAddress);

        // Add frame ID.
        if (frame == 0) {
            bb.setUInt8(settings.getNextSend(true));
        } else {
            bb.setUInt8(frame);
        }
        // Add header CRC.
        int crc = GXFCS16.countFCS16(bb.getData(), 1, bb.size() - 1);
        bb.setUInt16(crc);
        if (len != 0) {
            // Add data.
            bb.set(data, len);
            // Add data CRC.
            crc = GXFCS16.countFCS16(bb.getData(), 1, bb.size() - 1);
            bb.setUInt16(crc);
        }
        // Add EOP
        bb.setUInt8(HDLC_FRAME_START_END);
        // Remove sent data in server side.
        if (settings.isServer() && data != null) {
            if (data.size() == data.position()) {
                data.clear();
            } else {
                data.move(data.position(), 0, data.size() - data.position());
                data.position(0);
            }
        }
        return bb.array();
    }

    /**
     * Get MAC LLC frame for data.
     * 
     * @param settings
     *            DLMS settings.
     * @param frame
     *            HDLC frame sequence number.
     * @param creditFields
     *            Credit fields.
     * @param data
     *            Data to add.
     * @return MAC frame.
     */
    public static byte[] getMacFrame(final GXDLMSSettings settings, final byte frame, final byte creditFields,
            final GXByteBuffer data) {
        if (settings.getInterfaceType() == InterfaceType.PLC) {
            return getPlcFrame(settings, creditFields, data);
        }
        return getMacHdlcFrame(settings, frame, creditFields, data);
    }

    /**
     * Get MAC LLC frame for data.
     * 
     * @param settings
     *            DLMS settings.
     * @param data
     *            Data to add.
     * @return MAC frame.
     */
    private static byte[] getPlcFrame(final GXDLMSSettings settings, final byte creditFields, final GXByteBuffer data) {
        int frameSize = data.available();
        // Max frame size is 124 bytes.
        if (frameSize > 134) {
            frameSize = 134;
        }
        // PAD Length.
        int padLen = (36 - ((11 + frameSize) % 36)) % 36;
        GXByteBuffer bb = new GXByteBuffer();
        bb.capacity(15 + frameSize + padLen);
        // Add STX
        bb.setUInt8(2);
        // Length.
        bb.setUInt8((byte) (11 + frameSize));
        // Length.
        bb.setUInt8(0x50);
        // Add Credit fields.
        bb.setUInt8(creditFields);
        // Add source and target MAC addresses.
        bb.setUInt8((byte) (settings.getPlc().getMacSourceAddress() >> 4));
        int val = settings.getPlc().getMacSourceAddress() << 12;
        val |= settings.getPlc().getMacDestinationAddress() & 0xFFF;
        bb.setUInt16(val);
        bb.setUInt8((byte) padLen);
        // Control byte.
        bb.setUInt8(PlcDataLinkData.REQUEST.getValue());
        bb.setUInt8(settings.getServerAddress());
        bb.setUInt8(settings.getClientAddress());
        bb.set(data, frameSize);
        // Add padding.
        while (padLen != 0) {
            bb.setUInt8(0);
            --padLen;
        }
        // Checksum.
        int crc = GXFCS16.countFCS16(bb.getData(), 0, bb.size());
        bb.setUInt16(crc);
        // Remove sent data in server side.
        if (settings.isServer()) {
            if (data.size() == data.position()) {
                data.clear();
            } else {
                data.move(data.position(), 0, data.available());
                data.position(0);
            }
        }
        return bb.array();
    }

    /**
     * Get MAC HDLC frame for data.
     * 
     * @param settings
     *            DLMS settings.
     * @param frame
     *            HDLC frame.
     * @param creditFields
     *            Credit fields.
     * @param data
     *            Data to add.
     * @return MAC frame.
     */

    static byte[] getMacHdlcFrame(final GXDLMSSettings settings, final int frame, final int creditFields,
            final GXByteBuffer data) {
        if (settings.getHdlcSettings().getMaxInfoTX() > 126) {
            settings.getHdlcSettings().setMaxInfoTX(86);
        }
        GXByteBuffer bb = new GXByteBuffer();
        // Length is updated last.
        bb.setUInt16(0);
        // Add Credit fields.
        bb.setUInt8(creditFields);
        // Add source and target MAC addresses.
        bb.setUInt8(settings.getPlc().getMacSourceAddress() >> 4);
        int val = settings.getPlc().getMacSourceAddress() << 12;
        val |= settings.getPlc().getMacDestinationAddress() & 0xFFF;
        bb.setUInt16(val);
        byte[] tmp = GXDLMS.getHdlcFrame(settings, frame, data);
        int padLen = (36 - ((10 + tmp.length) % 36)) % 36;
        bb.setUInt8((byte) padLen);
        bb.set(tmp);
        // Add padding.
        while (padLen != 0) {
            bb.setUInt8(0);
            --padLen;
        }
        // Checksum.
        int crc = GXFCS16.countFCS24(bb.getData(), 2, bb.size() - 2 - padLen);
        bb.setUInt8((byte) (crc >> 16));
        bb.setUInt16(crc);
        // Add NC
        val = bb.size() / 36;
        if (bb.size() % 36 != 0) {
            ++val;
        }
        if (val == 1) {
            val = PlcMacSubframes.ONE;
        } else if (val == 2) {
            val = PlcMacSubframes.TWO;
        } else if (val == 3) {
            val = PlcMacSubframes.THREE;
        } else if (val == 4) {
            val = PlcMacSubframes.FOUR;
        } else if (val == 5) {
            val = PlcMacSubframes.FIVE;
        } else if (val == 6) {
            val = PlcMacSubframes.SIX;
        } else if (val == 7) {
            val = PlcMacSubframes.SEVEN;
        } else {
            throw new IllegalArgumentException("Data length is too high.");
        }
        bb.setUInt16(0, val);
        return bb.array();
    }

    /**
     * Check LLC bytes.
     * 
     * @param server
     *            Is server.
     * @param data
     *            Received data.
     * @return Are LLC bytes valid.
     */
    private static boolean getLLCBytes(final boolean server, final GXByteBuffer data) {
        if (server) {
            return data.compare(GXCommon.LLC_SEND_BYTES);
        } else {
            return data.compare(GXCommon.LLC_REPLY_BYTES);
        }
    }

    /**
     * Get HDLC sender and receiver address information.
     * 
     * @param reply
     *            Received data.
     * @param target
     *            target (primary) address
     * @param source
     *            Source (secondary) address.
     * @param type
     *            DLMS frame type.
     */
    public static void getHdlcAddressInfo(final GXByteBuffer reply, final int[] target, final int[] source,
            final short[] type) {
        int position = reply.position();
        target[0] = source[0] = 0;
        type[0] = 0;
        try {
            short ch;
            int pos, packetStartID = reply.position(), frameLen = 0;
            // If whole frame is not received yet.
            if (reply.size() - reply.position() < 9) {
                return;
            }
            // Find start of HDLC frame.
            for (pos = reply.position(); pos < reply.size(); ++pos) {
                ch = reply.getUInt8();
                if (ch == HDLC_FRAME_START_END) {
                    packetStartID = pos;
                    break;
                }
            }
            // Not a HDLC frame.
            // Sometimes meters can send some strange data between DLMS frames.
            if (reply.position() == reply.size()) {
                // Not enough data to parse;
                return;
            }
            short frame = reply.getUInt8();
            // Check frame length.
            if ((frame & 0x7) != 0) {
                frameLen = ((frame & 0x7) << 8);
            }
            ch = reply.getUInt8();
            // If not enough data.
            frameLen += ch;
            if (reply.size() - reply.position() + 1 < frameLen) {
                reply.position(packetStartID);
                // Not enough data to parse;
                return;
            }
            int eopPos = frameLen + packetStartID + 1;
            ch = reply.getUInt8(eopPos);
            if (ch != HDLC_FRAME_START_END) {
                throw new IllegalArgumentException("Invalid data format.");
            }
            // Get address.
            target[0] = GXCommon.getHDLCAddress(reply);
            source[0] = GXCommon.getHDLCAddress(reply);
            type[0] = reply.getUInt8();
        } finally {
            reply.position(position);
        }
    }

    static short getHdlcData(final boolean server, final GXDLMSSettings settings, final GXByteBuffer reply,
            final GXReplyData data, final GXReplyData notify) {
        short ch;
        int pos, packetStartID = reply.position(), frameLen = 0;
        int crc, crcRead;
        // If whole frame is not received yet.
        if (reply.size() - reply.position() < 9) {
            data.setComplete(false);
            return 0;
        }
        data.setComplete(true);
        if (notify != null) {
            notify.setComplete(true);
        }
        boolean isNotify = false;
        // Find start of HDLC frame.
        for (pos = reply.position(); pos < reply.size(); ++pos) {
            ch = reply.getUInt8();
            if (ch == HDLC_FRAME_START_END) {
                packetStartID = pos;
                break;
            }
        }
        // Not a HDLC frame.
        // Sometimes meters can send some strange data between DLMS frames.
        if (reply.position() == reply.size()) {
            data.setComplete(false);
            // Not enough data to parse;
            return 0;
        }
        short frame = reply.getUInt8();
        if ((frame & 0xF0) != 0xA0) {
            reply.position(reply.position() - 1);
            return getHdlcData(server, settings, reply, data, notify);
        }
        // Check frame length.
        if ((frame & 0x7) != 0) {
            frameLen = ((frame & 0x7) << 8);
        }
        ch = reply.getUInt8();
        // If not enough data.
        frameLen += ch;
        if (reply.size() - reply.position() + 1 < frameLen) {
            data.setComplete(false);
            reply.position(packetStartID);
            // Not enough data to parse;
            return 0;
        }
        int eopPos = frameLen + packetStartID + 1;
        ch = reply.getUInt8(eopPos);
        if (ch != HDLC_FRAME_START_END) {
            reply.position(reply.position() - 2);
            return getHdlcData(server, settings, reply, data, notify);
        }

        // Check addresses.
        int[] addresses = new int[2];
        boolean ret;
        try {
            ret = checkHdlcAddress(server, settings, reply, eopPos, addresses);
        } catch (Exception ex) {
            ret = false;
        }
        if (!ret) {
            // If not notify.
            if (!(reply.position() < reply.size() && reply.getUInt8(reply.position()) == 0x13)) {
                // If echo.
                reply.position(1 + eopPos);
                return getHdlcData(server, settings, reply, data, notify);
            } else if (notify != null) {
                isNotify = true;
                notify.setClientAddress(addresses[1]);
                notify.setServerAddress(addresses[0]);
            }
        }

        // Is there more data available.
        boolean moreData = (frame & 0x8) != 0;
        // Get frame type.
        frame = reply.getUInt8();
        // If server is using same client and server address for notifications.
        if (frame == 0x13 && !isNotify && notify != null) {
            isNotify = true;
            notify.setClientAddress(addresses[1]);
            notify.setServerAddress(addresses[0]);
        }
        // Is there more data available.
        if (moreData) {
            if (isNotify) {
                notify.getMoreData().add(RequestTypes.FRAME);
            } else {
                data.getMoreData().add(RequestTypes.FRAME);
            }
        } else {
            if (isNotify) {
                notify.getMoreData().remove(RequestTypes.FRAME);
            } else {
                data.getMoreData().remove(RequestTypes.FRAME);
            }
        }
        if (data.getXml() == null && !settings.checkFrame(frame)) {
            reply.position(eopPos + 1);
            return getHdlcData(server, settings, reply, data, notify);
        }
        // Check that header CRC is correct.
        crc = GXFCS16.countFCS16(reply.getData(), packetStartID + 1, reply.position() - packetStartID - 1);
        crcRead = reply.getUInt16();
        if (crc != crcRead) {
            if (reply.size() - reply.position() > 8) {
                return getHdlcData(server, settings, reply, data, notify);
            }
            throw new GXDLMSException("Wrong CRC.");
        }
        // Check that packet CRC match only if there is a data part.
        if (reply.position() != packetStartID + frameLen + 1) {
            crc = GXFCS16.countFCS16(reply.getData(), packetStartID + 1, frameLen - 2);
            crcRead = reply.getUInt16(packetStartID + frameLen - 1);
            if (crc != crcRead) {
                throw new GXDLMSException("Wrong CRC.");
            }
            // Remove CRC and EOP from packet length.
            if (isNotify) {
                notify.setPacketLength(eopPos - 2);
            } else {
                data.setPacketLength(eopPos - 2);
            }
        } else {
            if (isNotify) {
                notify.setPacketLength(reply.position() + 1);
            } else {
                data.setPacketLength(reply.position() + 1);
            }
        }

        if (frame != 0x13 && (frame & HdlcFrameType.U_FRAME.getValue()) == HdlcFrameType.U_FRAME.getValue()) {
            // Get Eop if there is no data.
            if (reply.position() == packetStartID + frameLen + 1) {
                // Get EOP.
                reply.getUInt8();
            }
            if (frame == 0x97) {
                data.setError((short) ErrorCode.UNACCEPTABLE_FRAME.getValue());
            } else if (frame == 0x1f) {
                data.setError((short) ErrorCode.DISCONNECT_MODE.getValue());
            }
            data.setCommand(frame);
        } else if (frame != 0x13 && (frame & HdlcFrameType.S_FRAME.getValue()) == HdlcFrameType.S_FRAME.getValue()) {
            // If S-frame
            int tmp = (frame >> 2) & 0x3;
            // If frame is rejected.
            if (tmp == HdlcControlFrame.REJECT.getValue()) {
                data.setError((short) ErrorCode.REJECTED.getValue());
            } else if (tmp == HdlcControlFrame.RECEIVE_NOT_READY.getValue()) {
                data.setError((short) ErrorCode.RECEIVE_NOT_READY.getValue());
            } else if (tmp == HdlcControlFrame.RECEIVE_READY.getValue()) {
                data.setError((short) ErrorCode.OK.getValue());
                // Get next frame.
            }
            // Get Eop if there is no data.
            if (reply.position() == packetStartID + frameLen + 1) {
                // Get EOP.
                reply.getUInt8();
            }
        } else {
            // I-frame
            // Get Eop if there is no data.
            if (reply.position() == packetStartID + frameLen + 1) {
                // Get EOP.
                reply.getUInt8();
                if ((frame & 0x1) == 0x1) {
                    data.getMoreData().add(RequestTypes.FRAME);
                }
            } else {
                if (!getLLCBytes(server, reply) && data.getXml() != null) {
                    getLLCBytes(!server, reply);
                }
            }
        }
        return frame;
    }

    /**
     * Get physical and logical address from server address.
     * 
     * @param address
     *            Server address.
     * @param logical
     *            Logical address.
     * @param physical
     *            Physical address.
     */
    private static void getServerAddress(final int address, final int[] logical, final int[] physical) {
        if (address < 0x4000) {
            logical[0] = address >>> 7;
            physical[0] = address & 0x7F;
        } else {
            logical[0] = address >>> 14;
            physical[0] = address & 0x3FFF;
        }
    }

    /**
     * Get HDLC address.
     * 
     * @param value
     *            HDLC address.
     * @param size
     *            HDLC address size. This is optional.
     * @return HDLC address.
     */
    @SuppressWarnings("squid:S1905")
    public static Object getHdlcAddress(int value, int size) {
        if (size < 2 && value < 0x80) {
            return (byte) (value << 1 | 1);
        }
        if (size < 4 && value < 0x4000) {
            return (short) ((value & 0x3F80) << 2 | (value & 0x7F) << 1 | 1);
        }
        if (value < 0x10000000) {
            return (int) ((value & 0xFE00000) << 4 | (value & 0x1FC000) << 3 | (value & 0x3F80) << 2
                    | (value & 0x7F) << 1 | 1);
        }
        throw new IllegalArgumentException("Invalid address.");
    }

    /*
     * Convert HDLC address to bytes.
     * @param value
     * @param size Address size in bytes.
     * @return
     */
    public static byte[] getHdlcAddressBytes(int value, int size) {
        Object tmp = getHdlcAddress(value, size);
        GXByteBuffer bb = new GXByteBuffer();
        if (tmp instanceof Byte && size < 2) {
            bb.setUInt8((Byte) tmp);
        } else if (tmp instanceof Short && size < 4) {
            bb.setUInt16((short) tmp);
        } else if (tmp instanceof Integer) {
            bb.setUInt32((int) tmp);
        } else {
            throw new IllegalArgumentException("Invalid address type.");
        }
        return bb.array();
    }

    /**
     * Check that client and server address match.
     * 
     * @param server
     *            Is server.
     * @param settings
     *            DLMS settings.
     * @param reply
     *            Received data.
     * @param index
     *            Position.
     * @return True, if client and server address match.
     */
    private static boolean checkHdlcAddress(final boolean server, final GXDLMSSettings settings,
            final GXByteBuffer reply, final int index, final int[] addresses) {
        int source, target;
        // Get destination and source addresses.
        target = GXCommon.getHDLCAddress(reply);
        source = GXCommon.getHDLCAddress(reply);
        addresses[0] = source;
        addresses[1] = target;
        if (server) {
            // Check that server addresses match.
            if (settings.getServerAddress() != 0 && settings.getServerAddress() != target) {
                if (reply.getUInt8(reply.position()) == Command.SNRM) {
                    settings.setServerAddress(target);
                } else {
                    throw new GXDLMSException("Server addresses do not match. It is " + String.valueOf(target)
                            + ". It should be " + String.valueOf(settings.getServerAddress()) + ".");
                }
            } else {
                settings.setServerAddress(target);
            }

            // Check that client addresses match.
            if (settings.getClientAddress() != 0 && settings.getClientAddress() != source) {
                if (reply.getUInt8(reply.position()) == Command.SNRM) {
                    settings.setClientAddress(source);
                } else {
                    throw new GXDLMSException("Client addresses do not match. It is " + String.valueOf(source)
                            + ". It should be " + String.valueOf(settings.getClientAddress()) + ".");
                }
            } else {
                settings.setClientAddress(source);
            }
        } else {
            // Check that client addresses match.
            if (settings.getClientAddress() != target) {
                // If echo.
                if (settings.getClientAddress() == source && settings.getServerAddress() == target) {
                    reply.position(index + 1);
                    return false;
                }
                throw new GXDLMSException("Destination addresses do not match. It is " + String.valueOf(target)
                        + ". It should be " + String.valueOf(settings.getClientAddress()) + ".");
            }
            // Check that server addresses match.
            if (settings.getServerAddress() != source &&
            // If All-station (Broadcast).
                    (settings.getServerAddress() & 0x7F) != 0x7F && (settings.getServerAddress() & 0x3FFF) != 0x3FFF) {
                // Check logical and physical address separately.
                // This is done because some meters might send four bytes
                // when only two bytes is needed.
                int[] readLogical = new int[1], readPhysical = new int[1], logical = new int[1], physical = new int[1];
                getServerAddress(source, readLogical, readPhysical);
                getServerAddress(settings.getServerAddress(), logical, physical);
                if (readLogical[0] != logical[0] || readPhysical[0] != physical[0]) {
                    throw new GXDLMSException("Source addresses do not match. It is " + String.valueOf(source)
                            + ". It should be " + String.valueOf(settings.getServerAddress()) + ".");
                }
            }
        }
        return true;
    }

    /*
     * Get data from TCP/IP frame.
     * @param settings DLMS settigns.
     * @param buff Received data.
     * @param target Reply information.
     */
    @SuppressWarnings({ "squid:S1940", "squid:S106" })
    static boolean getTcpData(final GXDLMSSettings settings, final GXByteBuffer buff, final GXReplyData data,
            final GXReplyData notify) {
        GXReplyData target = data;
        // If whole frame is not received yet.
        if (buff.size() - buff.position() < 8) {
            target.setComplete(false);
            return true;
        }
        boolean isData = true;
        int pos = buff.position();
        int value;
        target.setComplete(false);
        if (notify != null) {
            notify.setComplete(false);
        }
        while (buff.position() < buff.size() - 1) {
            // Get version
            value = buff.getUInt16();
            if (value == 1) {
                if (buff.available() < 6) {
                    isData = false;
                    break;
                }
                // Check TCP/IP addresses.
                if (!checkWrapperAddress(settings, buff, target)) {
                    if (notify != null) {
                        target = notify;
                    }
                    isData = false;
                }
                // Get length.
                value = buff.getUInt16();
                boolean complete = !((buff.size() - buff.position()) < value);
                if (complete && (buff.size() - buff.position()) != value && data.getXml() != null) {
                    data.getXml().appendComment("Data length is " + String.valueOf(value) + " and there are "
                            + String.valueOf(buff.size() - buff.position()) + " bytes.");
                }
                target.setComplete(complete);
                if (!complete) {
                    buff.position(pos);
                } else {
                    target.setPacketLength(buff.position() + value);
                }
                break;
            } else {
                buff.position(buff.position() - 1);
            }
        }
        return isData;
    }

    public static boolean checkSMSAddress(final GXDLMSSettings settings, final GXByteBuffer buff,
            final GXReplyData data, final GXReplyData notify) {
        boolean ret = true;
        int value;
        if (settings.isServer()) {
            value = buff.getUInt8();
            data.setSourceAddress(value);
            // Check that client addresses match.
            if (data.getXml() == null && settings.getClientAddress() != 0 && settings.getClientAddress() != value) {
                throw new RuntimeException("Source addresses do not match. It is " + String.valueOf(value)
                        + ". It should be " + String.valueOf(settings.getClientAddress()));
            }
            settings.setClientAddress(value);
            value = buff.getUInt8();
            data.setTargetAddress(value);
            // Check that server addresses match.
            if (data.getXml() == null && settings.getServerAddress() != 0 && settings.getServerAddress() != value) {
                throw new RuntimeException("Destination addresses do not match. It is " + String.valueOf(value)
                        + ". It should be " + String.valueOf(settings.getServerAddress()) + ".");
            }
            settings.setServerAddress(value);
        } else {
            value = buff.getUInt8();
            data.setTargetAddress(value);
            // Check that server addresses match.
            if (data.getXml() == null && settings.getServerAddress() != 0 && settings.getServerAddress() != value) {
                if (notify == null) {
                    throw new RuntimeException("Source addresses do not match. It is " + String.valueOf(value)
                            + ". It should be " + String.valueOf(settings.getServerAddress()) + ".");
                }
                notify.setSourceAddress(value);
                ret = false;
            } else {
                settings.setServerAddress(value);
            }
            value = buff.getUInt8();
            data.setSourceAddress(value);
            // Check that client addresses match.
            if (data.getXml() == null && settings.getClientAddress() != 0 && settings.getClientAddress() != value) {
                if (notify == null) {
                    throw new RuntimeException("Destination addresses do not match. It is "
                            + (new Integer(value)).toString() + ". It should be " + settings.getClientAddress() + ".");
                }
                ret = false;
                notify.setTargetAddress(value);
            } else {
                settings.setClientAddress(value);
            }
        }
        return ret;
    }

    /**
     * Get data from SMS frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     * @param data
     *            Reply information.
     * @param notify
     *            Notify information.
     */
    private static boolean getSmsData(final GXDLMSSettings settings, final GXByteBuffer buff, GXReplyData data,
            final GXReplyData notify) {
        // If whole frame is not received yet.
        if (buff.size() - buff.position() < 3) {
            data.setComplete(false);
            return true;
        }
        boolean isData = true;
        int pos = buff.position();
        data.setComplete(false);
        if (notify != null) {
            notify.setComplete(false);
            notify.setComplete(false);
        }
        // Check SMS addresses.
        if (!checkSMSAddress(settings, buff, data, notify)) {
            data = notify;
            isData = false;
        }
        // Get length.
        data.setComplete(buff.available() != 0);
        if (!data.isComplete()) {
            buff.position(pos);
        } else {
            data.setPacketLength(buff.size());
        }
        return isData;
    }

    private static BigInteger getCoAPValueAsInteger(GXByteBuffer buff, int len) {
        BigInteger token;
        switch (len) {
        case 1:
            token = BigInteger.valueOf(buff.getUInt8());
            break;
        case 2:
            token = BigInteger.valueOf(buff.getUInt16());
            break;
        case 4:
            token = BigInteger.valueOf(buff.getUInt32());
            break;
        case 8:
            token = buff.getUInt64();
            break;
        default:
            throw new RuntimeException("Invalid Coap data.");
        }
        return token;
    }

    private static void findNextToken(GXDLMSSettings settings, GXByteBuffer buff, int origPos, GXReplyData data,
            GXReplyData notify, boolean nextCoAPPacket)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        int orig = buff.position();
        try {
            // Find the next CoAP message.
            int pos;
            for (pos = buff.position(); pos < buff.size(); ++pos) {
                try {
                    buff.position(pos);
                    if (getCoAPData(settings, buff, null, notify)) {
                        data.setComplete(true);
                        if (nextCoAPPacket) {
                            data.setPacketLength(buff.position());
                        } else {
                            data.setPacketLength(pos);
                        }
                        break;
                    }
                } catch (RuntimeException e) {
                    // It's OK if this fails.
                }
            }
            if (pos == buff.size()) {
                data.setPacketLength(pos);
                data.setComplete(true);
            }
        } finally {
            buff.position(orig);
        }
    }

    /**
     * Get data from CoAP frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     * @param data
     *            Reply information.
     * @param notify
     *            Notify information.
     * @throws SignatureException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws Exception
     */
    private static boolean getCoAPData(GXDLMSSettings settings, GXByteBuffer buff, GXReplyData data, GXReplyData notify)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        if (data != null) {
            data.setComplete(false);
        }
        // If whole frame is not received yet.
        if (buff.size() - buff.position() < 8) {
            return false;
        }
        boolean isData = true;
        int origPos = buff.position();
        if (notify != null) {
            notify.setComplete(false);
        }
        int cmd = buff.getUInt8();
        byte version = (byte) (cmd >> 6 & 0x3);
        // Version must be 1.
        if (version != 1) {
            return false;
        }
        int coapType = (cmd >> 4) & 0x3;
        // Check type.
        if (coapType != CoAPType.CONFIRMABLE.getValue() && coapType != CoAPType.NON_CONFIRMABLE.getValue()
                && coapType != CoAPType.ACKNOWLEDGEMENT.getValue() && coapType != CoAPType.RESET.getValue()) {
            return false;
        }
        // Token Length.
        int len = cmd & 0xF;
        if (len == 0) {
            return false;
        }
        short ch = buff.getUInt8();
        int coapClass = (ch >>> 5);
        // Get msg ID.
        int msgId = buff.getUInt16();
        if (data != null && data.getXml() == null && settings.getCoap().getMessageId() != 0
                && settings.getCoap().getMessageId() != msgId) {
            return false;
        }
        BigInteger token = getCoAPValueAsInteger(buff, len);
        // Get options.
        short option;
        int moreData = 0;
        int type = 0;
        int frameSize = 0;
        if (coapClass != CoAPClass.CLIENT_ERROR.getValue()) {
            while (buff.available() != 0 && (option = buff.getUInt8()) != 0xFF) {
                if ((option == 0x61 || option == 0x41)) {
                    // All meters don't send End of options marker in ACK.
                    buff.position(buff.position() - 1);
                    break;
                }
                byte delta = (byte) (option >>> 4);
                if (delta == 13) {
                    // Extended delta value.
                    type += buff.getUInt8();
                    type += 13;
                } else if (delta == 14) {
                    // Extended delta value.
                    type += buff.getUInt16();
                    type += 269;
                } else {
                    type += delta;
                }
                byte optionLen = (byte) (option & 0xF);
                if (type == CoAPOptionType.URI_PORT.getValue()) {
                    int tmp = buff.getUInt16();
                    if (data != null) {
                        settings.getCoap().setPort(tmp);
                    }
                    continue;
                } else if (type == CoAPOptionType.IF_NONE_MATCH.getValue()) {
                    int tmp = buff.getUInt16();
                    if (data != null) {
                        settings.getCoap().setIfNoneMatch(CoAPContentType.forValue(tmp));
                    }
                    continue;
                } else if (type == CoAPOptionType.CONTENT_FORMAT.getValue()) {
                    int tmp = buff.getUInt16();
                    if (data != null) {
                        settings.getCoap().setContentFormat(CoAPContentType.forValue(tmp));
                    }
                    continue;
                } else if (type == CoAPOptionType.BLOCK1.getValue()) {
                    int tmp = getCoAPValueAsInteger(buff, optionLen).intValue();
                    if ((tmp & 0x8) != 0) {
                        moreData |= RequestTypes.DATABLOCK.getValue();
                    }
                    if (data != null) {
                        settings.getCoap().setBlockNumber((tmp >> 8));
                        data.getMoreData().addAll(RequestTypes.forValue(moreData));
                    }
                    frameSize = (int) Math.pow(2, (tmp & 0x7) + 4);
                    continue;
                } else if (type == CoAPOptionType.BLOCK2.getValue()) {
                    // More flag.
                    int tmp = getCoAPValueAsInteger(buff, optionLen).intValue();
                    byte blockNumber = (byte) (tmp >> 4);
                    if ((tmp & 0x8) != 0) {
                        moreData = RequestTypes.DATABLOCK.getValue();
                    }
                    frameSize = (int) Math.pow(2, (tmp & 0x7) + 4);
                    if (data != null) {
                        if (settings != null && blockNumber != settings.getCoap().getBlockNumber()
                                && settings.getCoap().getBlockNumber() != 0 && data.getXml() == null) {
                            // This block is already received.
                            if (blockNumber < settings.getCoap().getBlockNumber()) {
                                buff.position(buff.size());
                                return false;
                            }
                            throw new IllegalArgumentException(
                                    String.format("Invalid CoAP block number. It is %1$s and it should be %2$s.",
                                            blockNumber, settings.getCoap().getBlockNumber()));
                        }
                        if (settings != null) {
                            settings.getCoap().setBlockNumber((byte) (1 + blockNumber));
                        }
                        if (coapType == CoAPType.ACKNOWLEDGEMENT.getValue()) {
                            if (moreData == 0) {
                                if (data.getMoreData().contains(RequestTypes.FRAME)) {
                                    data.getMoreData().remove(RequestTypes.FRAME);
                                }
                                // Last CoAP block.
                                data.setPacketLength(buff.size());
                                data.setComplete(true);
                                settings.getCoap().setBlockNumber(0);
                            } else {
                                data.getMoreData().add(RequestTypes.FRAME);
                            }
                        }
                    }
                    continue;
                } else if (type == CoAPOptionType.MAX_AGE.getValue()) {
                    int tmp = buff.getUInt16();
                    if (data != null) {
                        settings.getCoap().setMaxAge(tmp);
                    }
                    continue;
                }
                byte[] value = new byte[optionLen];
                buff.get(value);
                if (type == CoAPOptionType.URI_HOST.getValue()) {
                    if (data != null) {
                        settings.getCoap().setHost(new String(value));
                    }
                } else if (type != CoAPOptionType.MAX_AGE.getValue()) {
                    if (data != null && (settings.isServer() || data.getXml() != null)) {
                        // Options are saved only for the server or when XML is
                        // used.
                        settings.getCoap().getOptions().put(type, value);
                    }
                }
            }
        }
        if (settings != null && data != null) {
            settings.getCoap().setVersion(version);
            settings.getCoap().setType(CoAPType.forValue(coapType));
            settings.getCoap().setClassCode(CoAPClass.forValue(coapClass));
            settings.getCoap().setMessageId(msgId);
            settings.getCoap().setToken(token);
            switch (CoAPClass.forValue(coapClass)) {
            case METHOD:
                settings.getCoap().setMethod(CoAPMethod.forValue(ch & 0x1F));
                switch (settings.getCoap().getMethod()) {
                case GET:
                case POST:
                case PUT:
                case DELETE:
                case FETCH:
                case PATCH:
                    break;
                default:
                    return false;
                }
                break;
            case SUCCESS:
                settings.getCoap().setSuccess(CoAPSuccess.forValue(ch & 0x1F));
                switch (settings.getCoap().getSuccess()) {
                case NONE:
                case CREATED:
                case DELETED:
                case VALID:
                case CONTINUE:
                    break;
                default:
                    return false;
                }
                break;
            case CLIENT_ERROR:
                settings.getCoap().setClientError(CoAPClientError.forValue(ch & 0x1F));
                switch (settings.getCoap().getClientError()) {
                case BAD_REQUEST:
                case UNAUTHORIZED:
                case BAD_OPTION:
                case FORBIDDEN:
                case NOT_FOUND:
                case METHOD_NOT_ALLOWED:
                case NOT_ACCEPTABLE:
                case REQUEST_ENTITY_INCOMPLETE:
                case CONFLICT:
                case PRECONDITION_FAILED:
                case REQUEST_ENTITY_TOO_LARGE:
                case UNSUPPORTED_CONTENT_FORMAT:
                    if (data == null || data.getXml() == null) {
                        throw new GXDLMSCoAPException(settings.getCoap().getClientError());
                    }
                    break;
                default:
                    return false;
                }
                break;
            case SERVER_ERROR:
                settings.getCoap().setServerError(CoAPServerError.forValue(ch & 0x1F));
                switch (settings.getCoap().getServerError()) {
                case INTERNAL:
                case NOT_IMPLEMENTED:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                case GATEWAY_TIMEOUT:
                case PROXYING_NOT_SUPPORTED:
                    if (data == null || data.getXml() == null) {
                        throw new GXDLMSCoAPException(settings.getCoap().getServerError());
                    }
                    break;
                default:
                    return false;
                }
                break;
            case SIGNALING:
                settings.getCoap().setSignaling(CoAPSignaling.forValue(ch & 0x1F));
                switch (settings.getCoap().getSignaling()) {
                case UNASSIGNED:
                case CSM:
                case PING:
                case PONG:
                case RELEASE:
                case ABORT:
                    break;
                default:
                    return false;
                }
                break;
            default:
                return false;
            }
            if (data.getXml() != null) {
                if (!data.isMoreData() && data.getPacketLength() == buff.size()) {
                    // If last block.
                    return true;
                }
                if (data.isMoreData()) {
                    findNextToken(settings, buff, origPos, data, notify, false);
                } else {
                    if (coapType == CoAPType.CONFIRMABLE.getValue() && moreData != 0) {
                        // Client sends ACK
                        data.setComplete(true);
                        data.setPacketLength(buff.position());
                    } else {
                        GXByteBuffer origData = data.getData();
                        int orig = buff.position();
                        int xmlLen = 0;
                        if (data.getXml() != null) {
                            xmlLen = data.getXml().getXmlLength();
                        }
                        data.setData(buff);
                        try {
                            getPdu(settings, data);
                            // Get length.
                            data.setComplete(true);
                            if (!data.isComplete()) {
                                buff.position(origPos);
                            } else {
                                if (data.getCommand() == Command.AARQ || data.getCommand() == Command.AARE) {
                                    // Find next token.
                                    findNextToken(settings, buff, origPos, data, notify, true);
                                    data.setPacketLength(data.getPacketLength() - orig);
                                } else {
                                    data.setPacketLength(buff.position());
                                }
                            }
                        } finally {
                            data.setData(origData);
                            buff.position(orig);
                            if (data.getXml() != null) {
                                data.getXml().setXmlLength(xmlLen);
                            }
                        }
                    }
                }
            } else {
                data.setComplete(true);
                if (!data.isComplete()) {
                    buff.position(origPos);
                } else {
                    data.setPacketLength(buff.size());
                }
            }
        }
        return isData;
    }

    /**
     * Validate M-Bus checksum
     */
    private static boolean validateCheckSum(final GXByteBuffer bb, final int count) {
        byte value = 0;
        for (int pos = 0; pos != count; ++pos) {
            value += bb.getUInt8(bb.position() + pos);
        }
        return value == bb.getUInt8(bb.position() + count);
    }

    /**
     * Get data from wired M-Bus frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     * @param data
     *            Reply information.
     */
    private static void getWiredMBusData(final GXDLMSSettings settings, final GXByteBuffer buff,
            final GXReplyData data) {
        int packetStartID = buff.position();
        if (buff.getUInt8() != 0x68 || buff.available() < 5) {
            data.setComplete(false);
            buff.position(buff.position() - 1);
        } else {
            // L-field.
            int len = buff.getUInt8();
            // L-field.
            if (buff.getUInt8() != len || buff.available() < 3 + len || buff.getUInt8() != 0x68) {
                data.setComplete(false);
                buff.position(packetStartID);
            } else {
                boolean crc = validateCheckSum(buff, len);
                if (!crc && data.getXml() == null) {
                    data.setComplete(false);
                    buff.position(packetStartID);
                } else {
                    if (!crc) {
                        data.getXml().appendComment("Invalid checksum.");
                    }
                    // Check EOP.
                    if (buff.getUInt8(buff.position() + len + 1) != 0x16) {
                        data.setComplete(false);
                        buff.position(packetStartID);
                        return;
                    }
                    data.setPacketLength(buff.position() + len);
                    data.setComplete(true);
                    int index = data.getData().position();
                    // Control field (C-Field)
                    short tmp = buff.getUInt8();
                    MBusCommand cmd = MBusCommand.forValue(tmp & 0xF);
                    // Address (A-field)
                    short id = buff.getUInt8();
                    // The Control Information Field (CI-field)
                    short ci = buff.getUInt8();
                    if (ci == 0x0) {
                        data.getMoreData().add(RequestTypes.FRAME);
                    } else if ((ci >>> 4) == (ci & 0xf)) {
                        // If this is the last telegram.
                        data.getMoreData().remove(RequestTypes.FRAME);
                    }
                    // If M-Bus data header is present
                    if (ci != 0) {

                    }
                    if ((tmp & 0x40) != 0) {
                        // Message from primary(initiating) station
                        // Destination Transport Service Access Point
                        settings.setClientAddress(buff.getUInt8());
                        // Source Transport Service Access Point
                        settings.setServerAddress(buff.getUInt8());
                    } else {
                        // Message from secondary (responding) station.
                        // Source Transport Service Access Point
                        settings.setServerAddress(buff.getUInt8());
                        // Destination Transport Service Access Point
                        settings.setClientAddress(buff.getUInt8());
                    }
                    if (data.getXml() != null && data.getXml().isComments()) {
                        data.getXml().appendComment("Command: " + cmd);
                        data.getXml().appendComment("A-Field: " + id);
                        data.getXml().appendComment("CI-Field: " + ci);
                        if ((tmp & 0x40) != 0) {
                            data.getXml().appendComment("Primary station: " + settings.getServerAddress());
                            data.getXml().appendComment("Secondary station: " + settings.getClientAddress());
                        } else {
                            data.getXml().appendComment("Primary station: " + settings.getClientAddress());
                            data.getXml().appendComment("Secondary station: " + settings.getServerAddress());
                        }
                    }
                }
            }
        }
    }

    /**
     * Get data from Wireless M-Bus frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     * @param data
     *            Reply information.
     */
    static void getWirelessMBusData(final GXDLMSSettings settings, final GXByteBuffer buff, final GXReplyData data) {
        // L-field.
        int len = buff.getUInt8();
        // Some meters are counting length to frame size.
        if (buff.size() < len - 1) {
            data.setComplete(false);
            buff.position(buff.position() - 1);
        } else {
            // Some meters are counting length to frame size.
            if (buff.size() < len) {
                --len;
            }
            data.setPacketLength(len);
            data.setComplete(true);
            // C-field.
            MBusCommand cmd = MBusCommand.forValue(buff.getUInt8());
            // M-Field.
            int manufacturerID = buff.getUInt16();
            String man = GXCommon.decryptManufacturer(manufacturerID);
            // A-Field.
            // long id =
            buff.getUInt32();
            short meterVersion = buff.getUInt8();
            MBusMeterType type = MBusMeterType.forValue(buff.getUInt8());
            // CI-Field
            MBusControlInfo ci = MBusControlInfo.forValue(buff.getUInt8());
            // Access number.
            // short frameId =
            buff.getUInt8();
            // State of the meter
            // short state =
            buff.getUInt8();
            // Configuration word.
            int configurationWord = buff.getUInt16();
            // byte encryptedBlocks = (byte) (configurationWord >> 12);
            MBusEncryptionMode encryption = MBusEncryptionMode.forValue(configurationWord & 7);
            settings.setClientAddress(buff.getUInt8());
            settings.setServerAddress(buff.getUInt8());
            if (data.getXml() != null && data.getXml().isComments()) {
                data.getXml().appendComment("Command: " + cmd);
                data.getXml().appendComment("Manufacturer: " + man);
                data.getXml().appendComment("Meter Version: " + meterVersion);
                data.getXml().appendComment("Meter Type: " + type);
                data.getXml().appendComment("Control Info: " + ci);
                data.getXml().appendComment("Encryption: " + encryption);
            }
        }
    }

    /**
     * Check is this PLC S-FSK message.
     * 
     * @param buff
     *            Received data.
     * @return S-FSK frame size in bytes.
     */
    static short getPlcSfskFrameSize(final GXByteBuffer buff) {
        short ret;
        if (buff.available() < 2) {
            ret = 0;
        } else {
            int len = buff.getUInt16(buff.position());
            switch (len) {
            case PlcMacSubframes.ONE:
                ret = 36;
                break;
            case PlcMacSubframes.TWO:
                ret = 2 * 36;
                break;
            case PlcMacSubframes.THREE:
                ret = 3 * 36;
                break;
            case PlcMacSubframes.FOUR:
                ret = 4 * 36;
                break;
            case PlcMacSubframes.FIVE:
                ret = 5 * 36;
                break;
            case PlcMacSubframes.SIX:
                ret = 6 * 36;
                break;
            case PlcMacSubframes.SEVEN:
                ret = 7 * 36;
                break;
            default:
                ret = 0;
                break;
            }
        }
        return ret;
    }

    /**
     * Get data from S-FSK PLC frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     * @param data
     *            Reply information.
     */
    private static void getPlcData(final GXDLMSSettings settings, final GXByteBuffer buff, final GXReplyData data) {
        if (buff.available() < 9) {
            data.setComplete(false);
            return;
        }
        int pos;
        int packetStartID = buff.position();
        // Find STX.
        short stx;
        for (pos = buff.position(); pos < buff.size(); ++pos) {
            stx = buff.getUInt8();
            if (stx == 2) {
                packetStartID = pos;
                break;
            }
        }
        // Not a PLC frame.
        if (buff.position() == buff.size()) {
            // Not enough data to parse;
            data.setComplete(false);
            buff.position(packetStartID);
            return;
        }
        int len = buff.getUInt8();
        // index =
        buff.position();
        if (buff.available() < len) {
            data.setComplete(false);
            buff.position(buff.position() - 2);
        } else {
            buff.getUInt8();
            // Get credit.
            buff.getUInt8();
            // MAC Addresses.
            int mac = buff.getUInt24();
            short macSa = (short) (mac >> 12);
            short macDa = (short) (mac & 0xFFF);
            short padLen = buff.getUInt8();
            if (buff.size() < len + padLen + 2) {
                data.setComplete(false);
                buff.position(buff.position() - 6);
            } else {
                // DL.Data.request
                short ch = buff.getUInt8();
                if (ch != PlcDataLinkData.REQUEST.getValue()) {
                    throw new RuntimeException("Parsing MAC LLC data failed. Invalid DataLink data request.");
                }
                // short da =
                buff.getUInt8();
                // short sa =
                buff.getUInt8();
                if (settings.isServer()) {
                    data.setComplete(
                            data.getXml() != null || ((macDa == (short) PlcDestinationAddress.ALL_PHYSICAL.getValue()
                                    || macDa == settings.getPlc().getMacSourceAddress())
                                    && (macSa == (short) PlcSourceAddress.INITIATOR.getValue()
                                            || macSa == settings.getPlc().getMacDestinationAddress())));
                    data.setServerAddress(macDa);
                    data.setClientAddress(macSa);
                } else {
                    data.setComplete(
                            data.getXml() != null || (macDa == (short) PlcDestinationAddress.ALL_PHYSICAL.getValue()
                                    || macDa == (short) PlcSourceAddress.INITIATOR.getValue()
                                    || macDa == settings.getPlc().getMacDestinationAddress()));
                    data.setClientAddress(macDa);
                    data.setServerAddress(macSa);
                }
                // Skip padding.
                if (data.isComplete()) {
                    int crcCount = GXFCS16.countFCS16(buff.getData(), 0, len + padLen);
                    int crc = buff.getUInt16(len + padLen);
                    // Check CRC.
                    if (crc != crcCount) {
                        if (data.getXml() == null) {
                            throw new RuntimeException("Invalid data checksum.");
                        }
                        data.getXml().appendComment("Invalid data checksum.");
                    }
                    data.setPacketLength(len);
                } else {
                    buff.position(packetStartID);
                }
            }
        }
    }

    /**
     * Get data from S-FSK PLC Hdlc frame.
     * 
     * @param settings
     *            DLMS settings.
     * @param buff
     *            Received data.
     * @param data
     *            Reply information.
     */
    private static short getPlcHdlcData(final GXDLMSSettings settings, final GXByteBuffer buff,
            final GXReplyData data) {
        if (buff.available() < 2) {
            data.setComplete(false);
            return 0;
        }
        short frame = 0;
        short frameLen = getPlcSfskFrameSize(buff);
        if (frameLen == 0) {
            throw new RuntimeException("Invalid PLC frame size.");
        }
        if (buff.available() < frameLen) {
            data.setComplete(false);
        } else {
            buff.position(2 + buff.position());
            int index = buff.position();
            // short credit =
            buff.getUInt8();
            // Credit fields. IC, CC, DC
            // MAC Addresses.
            int mac = buff.getUInt24();
            // SA.
            short sa = (short) (mac >> 12);
            // DA.
            short da = (short) (mac & 0xFFF);
            if (settings.isServer()) {
                data.setComplete(data.getXml() != null || ((da == PlcDestinationAddress.ALL_PHYSICAL.getValue()
                        || da == settings.getPlc().getMacSourceAddress())
                        && (sa == (short) PlcHdlcSourceAddress.INITIATOR.getValue()
                                || sa == settings.getPlc().getMacDestinationAddress())));
                data.setServerAddress(da);
                data.setClientAddress(sa);
            } else {
                data.setComplete(data.getXml() != null || (da == PlcHdlcSourceAddress.INITIATOR.getValue()
                        || da == settings.getPlc().getMacDestinationAddress()));
                data.setClientAddress(sa);
                data.setServerAddress(da);
            }
            if (data.isComplete()) {
                short padLen = buff.getUInt8();
                frame = getHdlcData(settings.isServer(), settings, buff, data, null);
                getDataFromFrame(buff, data, true);
                buff.position(buff.position() + padLen);
                int crcCount = GXFCS16.countFCS24(buff.getData(), index, buff.position() - index);
                int crc = buff.getUInt24(buff.position());
                // Check CRC.
                if (crc != crcCount) {
                    if (data.getXml() == null) {
                        throw new RuntimeException("Invalid data checksum.");
                    }
                    data.getXml().appendComment("Invalid data checksum.");
                }
                data.setPacketLength(2 + buff.position() - index);
            } else {
                buff.position(buff.position() + frameLen - index - 4);
            }
        }
        return frame;
    }

    /**
     * Check is this wireless M-Bus message.
     * 
     * @param buff
     *            Received data.
     * @return True, if this is wireless M-Bus message.
     */
    public static boolean isWirelessMBusData(final GXByteBuffer buff) {
        if (buff.available() < 2) {
            return false;
        }
        short cmd = buff.getUInt8(buff.position() + 1);
        return (cmd & MBusCommand.SND_NR.getValue()) != 0 || (cmd & MBusCommand.SND_UD.getValue()) != 0
                || (cmd & MBusCommand.RSP_UD.getValue()) != 0;
    }

    /**
     * Check is this wired M-Bus message.
     * 
     * @param buff
     *            Received data.
     * @return True, if this is wired M-Bus message.
     */
    public static boolean isWiredMBusData(final GXByteBuffer buff) {
        if (buff.available() < 1) {
            return false;
        }
        return buff.getUInt8(buff.position()) == 0x68;
    }

    /**
     * Check is this CoAP message.
     * 
     * @param buff
     *            Received data.
     * @return True, if this is CoAP message.
     * @throws SignatureException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws Exception
     */
    static boolean isCoAPData(final GXByteBuffer buff)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        if (buff.size() - buff.position() < 8) {
            return false;
        }
        int pos = buff.position();
        try {
            return getCoAPData(null, buff, null, null);
        } finally {
            buff.position(pos);
        }
    }

    /**
     * Check is this PLC S-FSK message.
     * 
     * @param buff
     *            Received data.
     * @return S-FSK frame size in bytes.
     */
    public static int GetPlcSfskFrameSize(final GXByteBuffer buff) {
        int ret;
        if (buff.available() < 2) {
            ret = 0;
        } else {
            int len = buff.getUInt16(buff.position());
            switch (len) {
            case (int) PlcMacSubframes.ONE:
                ret = 36;
                break;
            case (int) PlcMacSubframes.TWO:
                ret = 2 * 36;
                break;
            case (int) PlcMacSubframes.THREE:
                ret = 3 * 36;
                break;
            case (int) PlcMacSubframes.FOUR:
                ret = 4 * 36;
                break;
            case (int) PlcMacSubframes.FIVE:
                ret = 5 * 36;
                break;
            case (int) PlcMacSubframes.SIX:
                ret = 6 * 36;
                break;
            case (int) PlcMacSubframes.SEVEN:
                ret = 7 * 36;
                break;
            default:
                ret = 0;
                break;
            }
        }
        return ret;
    }

    private static boolean checkWrapperAddress(final GXDLMSSettings settings, final GXByteBuffer buff,
            final GXReplyData notify) {
        boolean ret = true;
        int value;
        if (settings.isServer()) {
            value = buff.getUInt16();
            // Check that client addresses match.
            if (settings.getClientAddress() != 0 && settings.getClientAddress() != value) {
                throw new GXDLMSException("Source addresses do not match. It is " + String.valueOf(value)
                        + ". It should be " + String.valueOf(settings.getClientAddress()) + ".");
            } else {
                settings.setClientAddress(value);
            }

            value = buff.getUInt16();
            // Check that server addresses match.
            if (settings.getServerAddress() != 0 && settings.getServerAddress() != value) {
                throw new GXDLMSException("Destination addresses do not match. It is " + String.valueOf(value)
                        + ". It should be " + String.valueOf(settings.getServerAddress()) + ".");
            } else {
                settings.setServerAddress(value);
            }
        } else {
            value = buff.getUInt16();
            // Check that server addresses match.
            if (settings.getServerAddress() != 0 && settings.getServerAddress() != value) {
                if (notify == null) {
                    throw new GXDLMSException("Source addresses do not match. It is " + String.valueOf(value)
                            + ". It should be " + String.valueOf(settings.getServerAddress()) + ".");
                }
                notify.setServerAddress(value);
                ret = false;
            } else {
                settings.setServerAddress(value);
            }

            value = buff.getUInt16();
            // Check that client addresses match.
            if (settings.getClientAddress() != 0 && settings.getClientAddress() != value) {
                if (notify == null) {
                    throw new GXDLMSException("Destination addresses do not match. It is " + String.valueOf(value)
                            + ". It should be " + String.valueOf(settings.getClientAddress()) + ".");
                }
                ret = false;
                notify.setClientAddress(value);
            } else {
                settings.setClientAddress(value);
            }
        }
        return ret;
    }

    /*
     * Handle read response data block result.
     * @param settings DLMS settings.
     * @param reply Received reply.
     * @param index Starting index.
     */
    @java.lang.SuppressWarnings("squid:S1192")
    static boolean readResponseDataBlockResult(final GXDLMSSettings settings, final GXReplyData reply,
            final int index) {
        reply.setError(0);
        short lastBlock = reply.getData().getUInt8();
        // Get Block number.
        int number = reply.getData().getUInt16();
        int blockLength = GXCommon.getObjectCount(reply.getData());
        // Is not Last block.
        if (lastBlock == 0) {
            reply.getMoreData().add(RequestTypes.DATABLOCK);
        } else {
            reply.getMoreData().remove(RequestTypes.DATABLOCK);
        }
        // If meter's block index is zero based.
        if (number != 1 && settings.getBlockIndex() == 1) {
            settings.setBlockIndex(number);
        }
        int expectedIndex = settings.getBlockIndex();
        if (number != expectedIndex) {
            // NOSONAR
            throw new GXDLMSException(
                    "Invalid Block number. It is " + number + " and it should be " + expectedIndex + ".");
        }
        // If whole block is not read.
        if (reply.getMoreData().contains(RequestTypes.FRAME)) {
            getDataFromBlock(reply.getData(), index);
            return false;
        }
        if (blockLength != reply.getData().size() - reply.getData().position()) {
            throw new IllegalArgumentException("Invalid block length.");
        }
        reply.setCommand(Command.NONE);
        if (reply.getXml() != null) {
            reply.getData().trim();
            reply.getXml().appendStartTag(Command.READ_RESPONSE, SingleReadResponse.DATA_BLOCK_RESULT);
            reply.getXml().appendLine(TranslatorTags.LAST_BLOCK, "Value", reply.getXml().integerToHex(lastBlock, 2));
            reply.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value", reply.getXml().integerToHex(number, 4));
            reply.getXml().appendLine(TranslatorTags.RAW_DATA, "Value",
                    GXCommon.toHex(reply.getData().getData(), false, 0, reply.getData().size()));
            reply.getXml().appendEndTag(Command.READ_RESPONSE, SingleReadResponse.DATA_BLOCK_RESULT);
            return false;
        }
        getDataFromBlock(reply.getData(), index);
        reply.setTotalCount(0);
        // If last packet and data is not try to peek.
        if (reply.getMoreData().isEmpty()) {
            settings.resetBlockIndex();
        }
        return true;
    }

    /*
     * Handle read response and get data from block and/or update error status.
     * @param settings DLMS settings.
     * @param data Received data from the client.
     */
    @SuppressWarnings("squid:S3034")
    static boolean handleReadResponse(final GXDLMSSettings settings, final GXReplyData reply, final int index) {
        int pos, cnt = reply.getTotalCount();
        // If we are reading value first time or block is handed.
        boolean first = cnt == 0 || reply.getCommandType() == SingleReadResponse.DATA_BLOCK_RESULT;
        if (first) {
            cnt = GXCommon.getObjectCount(reply.getData());
            reply.setTotalCount(cnt);
        }
        int type;
        List<Object> values = null;
        if (cnt != 1) {
            // Parse data after all data is received when readlist is used.
            if (reply.isMoreData()) {
                getDataFromBlock(reply.getData(), 0);
                // reply.setCommandType(SingleReadResponse.DATA_BLOCK_RESULT);
                return false;
            }
            if (!first) {
                reply.getData().position(0);
            }
            values = new ArrayList<Object>();
            if (reply.getValue() instanceof List<?>) {
                values.addAll((List<?>) reply.getValue());
            }
            reply.setValue(null);
        }

        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.READ_RESPONSE, "Qty", reply.getXml().integerToHex(cnt, 2));
        }
        for (pos = 0; pos != cnt; ++pos) {
            // Get response type code.
            if (first) {
                type = reply.getData().getUInt8();
                reply.setCommandType(type);
            } else {
                type = reply.getCommandType();
            }
            boolean standardXml =
                    reply.getXml() != null && reply.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML;
            switch (type) {
            case SingleReadResponse.DATA:
                reply.setError(0);
                if (reply.getXml() != null) {
                    if (standardXml) {
                        reply.getXml().appendStartTag(TranslatorTags.CHOICE);
                    }
                    reply.getXml().appendStartTag(Command.READ_RESPONSE, SingleReadResponse.DATA);
                    GXDataInfo di = new GXDataInfo();
                    di.setXml(reply.getXml());
                    GXCommon.getData(settings, reply.getData(), di);
                    reply.getXml().appendEndTag(Command.READ_RESPONSE, SingleReadResponse.DATA);
                    if (standardXml) {
                        reply.getXml().appendEndTag(TranslatorTags.CHOICE);
                    }
                } else if (cnt == 1) {
                    // Read one item.
                    getDataFromBlock(reply.getData(), 0);
                } else {
                    // If read multiple items.
                    reply.setReadPosition(reply.getData().position());
                    getValueFromData(settings, reply);
                    reply.getData().position(reply.getReadPosition());
                    values.add(reply.getValue());
                    reply.setValue(null);
                }
                break;
            case SingleReadResponse.DATA_ACCESS_ERROR:
                // Get error code.
                reply.setError(reply.getData().getUInt8());
                if (reply.getXml() != null) {
                    if (standardXml) {
                        reply.getXml().appendStartTag(TranslatorTags.CHOICE);
                    }
                    reply.getXml().appendLine(Command.READ_RESPONSE << 8 | SingleReadResponse.DATA_ACCESS_ERROR, null,
                            GXDLMSTranslator.errorCodeToString(reply.getXml().getOutputType(),
                                    ErrorCode.forValue(reply.getError())));
                    if (standardXml) {
                        reply.getXml().appendEndTag(TranslatorTags.CHOICE);
                    }
                }
                break;
            case SingleReadResponse.DATA_BLOCK_RESULT:
                if (!readResponseDataBlockResult(settings, reply, index)) {
                    // If XML only received bytes are shown. Data is not try to
                    // parse.
                    if (reply.getXml() != null) {
                        reply.getXml().appendEndTag(Command.READ_RESPONSE);
                    }
                    return false;
                }
                break;
            case SingleReadResponse.BLOCK_NUMBER:
                // Get Block number.
                int number = reply.getData().getUInt16();
                if (number != settings.getBlockIndex()) {
                    throw new GXDLMSException("Invalid Block number. It is " + number + " and it should be "
                            + settings.getBlockIndex() + ".");
                }
                settings.increaseBlockIndex();
                reply.getMoreData().add(RequestTypes.DATABLOCK);
                break;
            default:
                throw new GXDLMSException("HandleReadResponse failed. Invalid tag.");
            }
        }
        if (reply.getXml() != null) {
            reply.getXml().appendEndTag(Command.READ_RESPONSE);
            return true;
        }
        if (values != null) {
            reply.setValue(values);
        }
        return cnt == 1;
    }

    private static void handleActionResponseNormal(GXDLMSSettings settings, GXReplyData data) {
        boolean standardXml =
                data.getXml() != null && data.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML;
        // Get Action-Result
        short ret = data.getData().getUInt8();
        if (ret != 0) {
            data.setError(ret);
        }
        if (data.getXml() != null) {
            if (standardXml) {
                data.getXml().appendStartTag(TranslatorTags.SINGLE_RESPONSE);
            }
            data.getXml().appendLine(TranslatorTags.RESULT, null, GXDLMSTranslator
                    .errorCodeToString(data.getXml().getOutputType(), ErrorCode.forValue(data.getError())));
        }
        // Action-Response-Normal. Get data if exists. All meters do not
        // return data here.
        if (ret == 0 && data.getData().position() < data.getData().size()) {
            // Get-Data-Result
            ret = data.getData().getUInt8();
            if (ret == 0) {
                getDataFromBlock(data.getData(), 0);
            } else if (ret == 1) {
                // Get Data-Access-Result.
                ret = (byte) data.getData().getUInt8();
                if (ret != 0) {
                    data.setError(data.getData().getUInt8());
                    // Handle Texas Instrument missing byte here.
                    if (ret == 9 && data.getError() == 16) {
                        data.getData().position(data.getData().position() - 2);
                        getDataFromBlock(data.getData(), 0);
                        data.setError(0);
                        ret = 0;
                    }

                } else {
                    getDataFromBlock(data.getData(), 0);
                }
            } else {
                throw new GXDLMSException("HandleActionResponseNormal failed. " + "Invalid tag.");
            }
            if (data.getXml() != null && (ret != 0 || data.getData().position() < data.getData().size())) {
                data.getXml().appendStartTag(TranslatorTags.RETURN_PARAMETERS);
                if (ret != 0) {
                    data.getXml().appendLine(TranslatorTags.DATA_ACCESS_ERROR, null, GXDLMSTranslator
                            .errorCodeToString(data.getXml().getOutputType(), ErrorCode.forValue(data.getError())));

                } else {
                    data.getXml().appendStartTag(Command.READ_RESPONSE, SingleReadResponse.DATA);
                    GXDataInfo di = new GXDataInfo();
                    di.setXml(data.getXml());
                    GXCommon.getData(settings, data.getData(), di);
                    data.getXml().appendEndTag(Command.READ_RESPONSE, SingleReadResponse.DATA);
                }
                data.getXml().appendEndTag(TranslatorTags.RETURN_PARAMETERS);
                if (standardXml) {
                    data.getXml().appendEndTag(TranslatorTags.SINGLE_RESPONSE);
                }
            }
        }
    }

    private static boolean handleActionResponseFirstBlock(GXDLMSSettings settings, GXReplyData reply, int index) {
        boolean ret = true;
        short ch;
        long number;
        // Is Last block.
        ch = reply.getData().getUInt8();
        if (reply.getXml() != null) {
            // Result start tag.
            reply.getXml().appendStartTag(TranslatorTags.P_BLOCK);
            // LastBlock
            reply.getXml().appendLine(TranslatorTags.LAST_BLOCK, "Value", reply.getXml().integerToHex(ch, 2));
        }
        if (ch == 0) {
            reply.getMoreData().add(RequestTypes.DATABLOCK);
        } else {
            reply.getMoreData().remove(RequestTypes.DATABLOCK);
        }
        // Get Block number.
        number = reply.getData().getUInt32();
        if (reply.getXml() != null) {
            // BlockNumber
            reply.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value", reply.getXml().integerToHex(number, 8));
        } else {
            // Update initial block index. This is critical if message is send
            // and received in multiple blocks.
            if (number == 1) {
                settings.resetBlockIndex();
            }
            int expectedIndex = settings.getBlockIndex();
            if (number != expectedIndex) {
                throw new IllegalArgumentException(
                        "Invalid Block number. It is " + number + " and it should be " + expectedIndex + ".");
            }
        }
        // Note! There is no status!!
        if (reply.getXml() != null) {
            if (reply.getData().available() != 0) {
                // Get data size.
                int blockLength = GXCommon.getObjectCount(reply.getData());
                // if whole block is read.
                if (!reply.getMoreData().contains(RequestTypes.FRAME)) {
                    // Check Block length.
                    if (blockLength > reply.getData().available()) {
                        reply.getXml()
                                .appendComment("Block is not complete." + String.valueOf(reply.getData().available())
                                        + "/" + String.valueOf(blockLength) + ".");
                    }
                }
                reply.getXml().appendLine(TranslatorTags.RAW_DATA, "Value", GXCommon.toHex(reply.getData().getData(),
                        false, reply.getData().position(), reply.getData().available()));
            }
            reply.getXml().appendEndTag(TranslatorTags.P_BLOCK);
        } else if (reply.getData().available() != 0) {
            // Get data size.
            int blockLength = GXCommon.getObjectCount(reply.getData());
            // if whole block is read.
            if (!(reply.getMoreData().contains(RequestTypes.FRAME))) {
                // Check Block length.
                if (blockLength > reply.getData().size() - reply.getData().position()) {
                    throw new IllegalArgumentException("Invalid block length.");
                }
                // Keep command if this is last block for XML Client.
                if (reply.getMoreData().contains(RequestTypes.DATABLOCK)) {
                    reply.setCommand(Command.NONE);
                }
            }
            if (blockLength == 0) {
                // If meter sends empty data block.
                reply.getData().size(index);
            } else {
                getDataFromBlock(reply.getData(), index);
            }
            // If last packet and data is not try to peek.
            if (reply.getMoreData().isEmpty()) {
                reply.getData().position(0);
                settings.resetBlockIndex();
            }
        }
        if (reply.getMoreData().isEmpty() && settings != null && settings.getCommand() == Command.GET_REQUEST
                && settings.getCommandType() == GetCommandType.WITH_LIST) {
            handleGetResponseWithList(settings, reply);
            ret = false;
        }
        return ret;
    }

    /*
     * Handle method response and get data from block and/or update error
     * status.
     * @param data Received data from the client.
     */
    static void handleMethodResponse(final GXDLMSSettings settings, final GXReplyData data, int index) {

        // Get type.
        byte type = (byte) data.getData().getUInt8();
        // Get invoke ID and priority.
        data.setInvokeId(data.getData().getUInt8());
        verifyInvokeId(settings, data);
        addInvokeId(data.getXml(), Command.METHOD_RESPONSE, type, data.getInvokeId());
        if (type == ActionResponseType.NORMAL) {
            handleActionResponseNormal(settings, data);
        } else if (type == ActionResponseType.WITH_BLOCK) {
            handleActionResponseFirstBlock(settings, data, index);
        } else if (type == ActionResponseType.WITH_LIST) {
            throw new IllegalArgumentException("Invalid Command.");
        } else if (type == ActionResponseType.NEXT_BLOCK) {
            long number = data.getData().getUInt32();
            if (data.getXml() != null) {
                data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value", data.getXml().integerToHex(number, 8));
            }
            settings.increaseBlockIndex();
        } else {
            throw new IllegalArgumentException("Invalid Command.");
        }

        if (data.getXml() != null) {
            data.getXml().appendEndTag(Command.METHOD_RESPONSE, type);
            data.getXml().appendEndTag(Command.METHOD_RESPONSE);
        }
    }

    /**
     * Handle push and get data from block and/or update error status.
     * 
     * @param reply
     *            Received data from the client.
     */
    static void handlePush(final GXReplyData reply) {
        int index = reply.getData().position() - 1;
        // Is last block
        int last = reply.getData().getUInt8();
        if ((last & 0x80) == 0) {
            reply.getMoreData().add(RequestTypes.DATABLOCK);
        } else {
            reply.getMoreData().remove(RequestTypes.DATABLOCK);
        }
        // Get block number sent.
        reply.getData().getUInt8();
        // Get block number acknowledged
        reply.getData().getUInt8();
        // Get APU tag.
        reply.getData().getUInt8();
        // Addl fields
        reply.getData().getUInt8();
        // Data-Notification
        if ((reply.getData().getUInt8() & 0x0F) == 0) {
            throw new IllegalArgumentException("Invalid data.");
        }
        // Long-Invoke-Id-And-Priority
        reply.getData().getUInt32();
        // Get date time and skip it if used.
        int len = reply.getData().getUInt8();
        if (len != 0) {
            reply.getData().position(reply.getData().position() + len);
        }
        getDataFromBlock(reply.getData(), index);
    }

    private static void handleAccessResponse(final GXDLMSSettings settings, final GXReplyData reply) {
        // Get invoke id.
        long invokeId = reply.getData().getUInt32();
        int len = reply.getData().getUInt8();
        byte[] tmp = null;
        // If date time is given.
        if (len != 0) {
            tmp = new byte[len];
            reply.getData().get(tmp);
            reply.setTime(((GXDateTime) GXDLMSClient.changeType(tmp, DataType.DATETIME, settings)));
        }
        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.ACCESS_RESPONSE);
            reply.getXml().appendLine(TranslatorTags.LONG_INVOKE_ID, null, reply.getXml().integerToHex(invokeId, 8));
            if (reply.getTime() != null) {
                reply.getXml().appendComment(reply.getTime().toString());
            }
            reply.getXml().appendLine(TranslatorTags.DATE_TIME, "Value", GXCommon.toHex(tmp, false));

            // access-request-specification OPTIONAL
            reply.getData().getUInt8();
            len = GXCommon.getObjectCount(reply.getData());
            reply.getXml().appendStartTag(TranslatorTags.ACCESS_RESPONSE_BODY);
            reply.getXml().appendStartTag(TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA, "Qty",
                    reply.getXml().integerToHex(len, 2));
            for (int pos = 0; pos != len; ++pos) {
                if (reply.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    reply.getXml().appendStartTag(Command.WRITE_REQUEST, SingleReadResponse.DATA);
                }
                GXDataInfo di = new GXDataInfo();
                di.setXml(reply.getXml());
                GXCommon.getData(settings, reply.getData(), di);
                if (reply.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    reply.getXml().appendEndTag(Command.WRITE_REQUEST, SingleReadResponse.DATA);
                }
            }
            reply.getXml().appendEndTag(TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA);
            // access-response-specification
            int err;
            len = GXCommon.getObjectCount(reply.getData());
            reply.getXml().appendStartTag(TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION, "Qty",
                    reply.getXml().integerToHex(len, 2));
            for (int pos = 0; pos != len; ++pos) {
                byte type = (byte) reply.getData().getUInt8();
                err = reply.getData().getUInt8();
                if (err != 0) {
                    err = reply.getData().getUInt8();
                }
                reply.getXml().appendStartTag(TranslatorTags.ACCESS_RESPONSE_SPECIFICATION);

                reply.getXml().appendStartTag(Command.ACCESS_RESPONSE, type);
                reply.getXml().appendLine(TranslatorTags.RESULT, null,
                        GXDLMSTranslator.errorCodeToString(reply.getXml().getOutputType(), ErrorCode.forValue(err)));
                reply.getXml().appendEndTag(Command.ACCESS_RESPONSE, type);
                reply.getXml().appendEndTag(TranslatorTags.ACCESS_RESPONSE_SPECIFICATION);
                // <Result Value="Success" />
            }
            reply.getXml().appendEndTag(TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION);
            reply.getXml().appendEndTag(TranslatorTags.ACCESS_RESPONSE_BODY);
            reply.getXml().appendEndTag(Command.ACCESS_RESPONSE);
        } else {
            // Skip access-request-specification
            reply.getData().getUInt8();
        }
    }

    /**
     * Handle data notification get data from block and/or update error status.
     * 
     * @param settings
     *            DLMS settings.
     * @param reply
     *            Received data from the client.
     */
    private static void handleDataNotification(final GXDLMSSettings settings, final GXReplyData reply) {
        int start = reply.getData().position() - 1;
        // Get invoke id.
        long invokeId = reply.getData().getUInt32();
        // Get date time.
        reply.setTime(null);
        int len = reply.getData().getUInt8();
        byte[] tmp = null;
        // If date time is given.
        if (len != 0) {
            tmp = new byte[len];
            reply.getData().get(tmp);
            DataType dt = DataType.DATETIME;
            if (len == 4) {
                dt = DataType.TIME;
            } else if (len == 5) {
                dt = DataType.DATE;
            }
            GXDataInfo info = new GXDataInfo();
            info.setType(dt);
            Object ret = GXCommon.getData(settings, new GXByteBuffer(tmp), info);
            reply.setTime(((GXDateTime) ret));
        }
        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.DATA_NOTIFICATION);
            reply.getXml().appendLine(TranslatorTags.LONG_INVOKE_ID, null, reply.getXml().integerToHex(invokeId, 8));
            if (reply.getTime() != null) {
                reply.getXml().appendComment(String.valueOf(reply.getTime()));
            }
            reply.getXml().appendLine(TranslatorTags.DATE_TIME, null, GXCommon.toHex(tmp, false));
            reply.getXml().appendStartTag(TranslatorTags.NOTIFICATION_BODY);
            reply.getXml().appendStartTag(TranslatorTags.DATA_VALUE);
            GXDataInfo di = new GXDataInfo();
            di.setXml(reply.getXml());
            GXCommon.getData(settings, reply.getData(), di);
            reply.getXml().appendEndTag(TranslatorTags.DATA_VALUE);
            reply.getXml().appendEndTag(TranslatorTags.NOTIFICATION_BODY);
            reply.getXml().appendEndTag(Command.DATA_NOTIFICATION);
        } else {
            getDataFromBlock(reply.getData(), start);
            getValueFromData(settings, reply);
        }
    }

    /*
     * Handle set response and update error status.
     * @param settings DLMS settings.
     * @param reply Received data from the client.
     */
    static void handleSetResponse(final GXDLMSSettings settings, final GXReplyData data) {
        byte type = (byte) data.getData().getUInt8();
        // Invoke ID and priority.
        data.setInvokeId(data.getData().getUInt8());
        verifyInvokeId(settings, data);
        addInvokeId(data.getXml(), Command.SET_RESPONSE, type, data.getInvokeId());
        // SetResponseNormal
        if (type == SetResponseType.NORMAL) {
            data.setError(data.getData().getUInt8());
            if (data.getXml() != null) {
                // Result start tag.
                data.getXml().appendLine(TranslatorTags.RESULT, "Value", GXDLMSTranslator
                        .errorCodeToString(data.getXml().getOutputType(), ErrorCode.forValue(data.getError())));
            }
        } else if (type == SetResponseType.DATA_BLOCK) {
            long number = data.getData().getUInt32();
            if (data.getXml() != null) {
                data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value", data.getXml().integerToHex(number, 8));
            }
        } else if (type == SetResponseType.LAST_DATA_BLOCK) {
            data.setError(data.getData().getUInt8());
            long number = data.getData().getUInt32();
            if (data.getXml() != null) {
                // Result start tag.
                data.getXml().appendLine(TranslatorTags.RESULT, "Value", GXDLMSTranslator
                        .errorCodeToString(data.getXml().getOutputType(), ErrorCode.forValue(data.getError())));
                data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value", data.getXml().integerToHex(number, 8));
            }
        } else if (type == SetResponseType.WITH_LIST) {
            int cnt = GXCommon.getObjectCount(data.getData());
            if (data.getXml() != null) {
                data.getXml().appendStartTag(TranslatorTags.RESULT, "Qty", String.valueOf(cnt));
                for (int pos = 0; pos != cnt; ++pos) {
                    int err = data.getData().getUInt8();
                    data.getXml().appendLine(TranslatorTags.DATA_ACCESS_RESULT, "Value",
                            GXDLMSTranslator.errorCodeToString(data.getXml().getOutputType(), ErrorCode.forValue(err)));
                }
                data.getXml().appendEndTag(TranslatorTags.RESULT);
            } else {
                for (int pos = 0; pos != cnt; ++pos) {
                    short err = data.getData().getUInt8();
                    if (data.getError() == 0 && err != 0) {
                        data.setError(err);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid data type.");
        }
        if (data.getXml() != null) {
            data.getXml().appendEndTag(Command.SET_RESPONSE, type);
            data.getXml().appendEndTag(Command.SET_RESPONSE);
        }
    }

    /**
     * Handle write response and update error status.
     * 
     * @param data
     *            Received data from the client.
     */
    static void handleWriteResponse(final GXReplyData data) {
        int cnt = GXCommon.getObjectCount(data.getData());
        boolean standardXml =
                data.getXml() != null && data.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML;
        short ret;
        if (data.getXml() != null) {
            data.getXml().appendStartTag(Command.WRITE_RESPONSE, "Qty", data.getXml().integerToHex(cnt, 2));
        }
        for (int pos = 0; pos != cnt; ++pos) {
            ret = data.getData().getUInt8();
            if (standardXml) {
                data.getXml().appendStartTag(TranslatorTags.CHOICE);
            }
            if (ret == SingleWriteResponse.SUCCESS) {
                if (data.getXml() != null) {
                    data.getXml().appendLine("<"
                            + GXDLMSTranslator.errorCodeToString(data.getXml().getOutputType(), ErrorCode.forValue(ret))
                            + " />");
                }
            } else if (ret == SingleWriteResponse.DATA_ACCESS_ERROR) {
                ret = data.getData().getUInt8();
                if (data.getXml() != null) {
                    data.getXml().appendLine(TranslatorTags.DATA_ACCESS_ERROR, null,
                            GXDLMSTranslator.errorCodeToString(data.getXml().getOutputType(), ErrorCode.forValue(ret)));
                } else {
                    data.setError(ret);
                }
            } else if (ret == SingleWriteResponse.BLOCK_NUMBER) {
                data.setBlockNumber(data.getData().getUInt16());
                if (data.getXml() != null) {
                    data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, null,
                            data.getXml().integerToHex(data.getBlockNumber(), 4));
                }
            }
            if (standardXml) {
                data.getXml().appendEndTag(TranslatorTags.CHOICE);
            }
        }
        if (data.getXml() != null) {
            data.getXml().appendEndTag(Command.WRITE_RESPONSE);
        }
    }

    /*
     * Handle get response and get data from block and/or update error status.
     * @param settings DLMS settings.
     * @param reply Received data from the client.
     * @param index Block index number.
     */
    static void handleGetResponseWithList(final GXDLMSSettings settings, final GXReplyData reply) {
        short ch = 0;
        GXByteBuffer data = reply.getData();
        // Get object count.
        int cnt = GXCommon.getObjectCount(data);
        List<Object> values = new ArrayList<Object>(cnt);
        if (reply.getXml() != null) {
            // Result start tag.
            reply.getXml().appendStartTag(TranslatorTags.RESULT, "Qty", reply.getXml().integerToHex(cnt, 2));
        }
        for (int pos = 0; pos != cnt; ++pos) {
            // Result
            ch = data.getUInt8();
            if (ch != 0) {
                reply.setError(data.getUInt8());
            } else {
                if (reply.getXml() != null) {
                    GXDataInfo di = new GXDataInfo();
                    di.setXml(reply.getXml());
                    // Data.
                    reply.getXml().appendStartTag(Command.READ_RESPONSE, SingleReadResponse.DATA);
                    GXCommon.getData(settings, reply.getData(), di);
                    reply.getXml().appendEndTag(Command.READ_RESPONSE, SingleReadResponse.DATA);
                } else {
                    reply.setReadPosition(reply.getData().position());
                    getValueFromData(settings, reply);
                    if (reply.getValue() == null) {
                        // Increase read position if data is null. This is a
                        // special case.
                        reply.setReadPosition(1 + reply.getReadPosition());
                    }
                    reply.getData().position(reply.getReadPosition());
                    values.add(reply.getValue());
                    reply.setValue(null);
                }
            }
        }
        reply.setValue(values);
    }

    private static void verifyInvokeId(GXDLMSSettings settings, GXReplyData reply) {
        if (reply.getXml() == null && settings.getAutoIncreaseInvokeID()
                && (byte) reply.getInvokeId() != getInvokeIDPriority(settings, false)) {
            throw new RuntimeException(String.format("Invalid invoke ID. Expected: %s Actual: %s",
                    Integer.toHexString(getInvokeIDPriority(settings, false)), Long.toHexString(reply.getInvokeId())));
        }
    }

    private static boolean handleGetResponseNormal(GXDLMSSettings settings, GXReplyData reply, GXByteBuffer data) {
        boolean empty = false;
        if (data.available() == 0) {
            empty = true;
            getDataFromBlock(data, 0);
        } else {
            // Result
            short ch = data.getUInt8();
            if (ch != 0) {
                reply.setError(data.getUInt8());
            }
            if (reply.getXml() != null) {
                // Result start tag.
                reply.getXml().appendStartTag(TranslatorTags.RESULT);
                if (reply.getError() != 0) {
                    reply.getXml().appendLine(TranslatorTags.DATA_ACCESS_ERROR, "Value", GXDLMSTranslator
                            .errorCodeToString(reply.getXml().getOutputType(), ErrorCode.forValue(reply.getError())));
                } else {
                    reply.getXml().appendStartTag(TranslatorTags.DATA);
                    GXDataInfo di = new GXDataInfo();
                    di.setXml(reply.getXml());
                    GXCommon.getData(settings, reply.getData(), di);
                    reply.getXml().appendEndTag(TranslatorTags.DATA);
                }
            } else {
                getDataFromBlock(data, 0);
            }
        }
        return empty;
    }

    private static boolean handleGetResponseNextDataBlock(GXDLMSSettings settings, GXReplyData reply, int index,
            GXByteBuffer data) {
        boolean ret = true;
        short ch;
        long number;
        // Is Last block.
        ch = data.getUInt8();
        if (reply.getXml() != null) {
            // Result start tag.
            reply.getXml().appendStartTag(TranslatorTags.RESULT);
            // LastBlock
            reply.getXml().appendLine(TranslatorTags.LAST_BLOCK, "Value", reply.getXml().integerToHex(ch, 2));
        }
        if (ch == 0) {
            reply.getMoreData().add(RequestTypes.DATABLOCK);
        } else {
            reply.getMoreData().remove(RequestTypes.DATABLOCK);
        }
        // Get Block number.
        number = data.getUInt32();
        if (reply.getXml() != null) {
            // BlockNumber
            reply.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value", reply.getXml().integerToHex(number, 8));
        } else {
            // If meter's block index is zero based.
            if (number != 1 && settings.getBlockIndex() == 1) {
                settings.setBlockIndex((int) number);
            }
            int expectedIndex = settings.getBlockIndex();
            if (number != expectedIndex) {
                throw new IllegalArgumentException(
                        "Invalid Block number. It is " + number + " and it should be " + expectedIndex + ".");
            }
        }
        // Get status.
        ch = data.getUInt8();
        if (ch != 0) {
            reply.setError(data.getUInt8());
        }
        if (reply.getXml() != null) {
            // Result
            reply.getXml().appendStartTag(TranslatorTags.RESULT);
            if (reply.getError() != 0) {
                reply.getXml().appendLine(TranslatorTags.DATA_ACCESS_RESULT, "Value", GXDLMSTranslator
                        .errorCodeToString(reply.getXml().getOutputType(), ErrorCode.forValue(reply.getError())));
            } else if (reply.getData().available() != 0) {
                // Get data size.
                int blockLength = GXCommon.getObjectCount(data);
                // if whole block is read.
                if (!reply.getMoreData().contains(RequestTypes.FRAME)) {
                    // Check Block length.
                    if (blockLength > data.size() - data.position()) {
                        reply.getXml()
                                .appendComment("Block is not complete." + String.valueOf(data.size() - data.position())
                                        + "/" + String.valueOf(blockLength) + ".");
                    }
                }
                reply.getXml().appendLine(TranslatorTags.RAW_DATA, "Value",
                        GXCommon.toHex(reply.getData().getData(), false, data.position(), reply.getData().available()));
            }
            reply.getXml().appendEndTag(TranslatorTags.RESULT);
        } else if (data.position() != data.size()) {
            // Get data size.
            int blockLength = GXCommon.getObjectCount(data);
            // if whole block is read.
            if (!(reply.getMoreData().contains(RequestTypes.FRAME))) {
                // Check Block length.
                if (blockLength > data.size() - data.position()) {
                    throw new IllegalArgumentException("Invalid block length.");
                }
                reply.setCommand(Command.NONE);
            }
            if (blockLength == 0) {
                // If meter sends empty data block.
                data.size(index);
            } else {
                getDataFromBlock(data, index);
            }
            // If last packet and data is not try to peek.
            if (reply.getMoreData().isEmpty()) {
                if (!reply.getPeek()) {
                    data.position(0);
                    settings.resetBlockIndex();
                }
            }
        }
        if (reply.getMoreData().isEmpty() && settings != null && settings.getCommand() == Command.GET_REQUEST
                && settings.getCommandType() == GetCommandType.WITH_LIST) {
            handleGetResponseWithList(settings, reply);
            ret = false;
        }
        return ret;
    }

    /*
     * Handle get response and get data from block and/or update error status.
     * @param settings DLMS settings.
     * @param reply Received data from the client.
     * @param index Block index number.
     */
    @SuppressWarnings("squid:S1066")
    static boolean handleGetResponse(final GXDLMSSettings settings, final GXReplyData reply, final int index) {
        boolean ret = true;
        boolean empty = false;
        GXByteBuffer data = reply.getData();
        // Get type.
        byte type = (byte) data.getUInt8();
        // Get invoke ID and priority.
        reply.setInvokeId(data.getUInt8());
        verifyInvokeId(settings, reply);
        addInvokeId(reply.getXml(), Command.GET_RESPONSE, type, reply.getInvokeId());
        switch (type) {
        case GetCommandType.NORMAL:
            empty = handleGetResponseNormal(settings, reply, data);
            break;
        case GetCommandType.NEXT_DATA_BLOCK:
            // Is Last block.
            ret = handleGetResponseNextDataBlock(settings, reply, index, data);
            break;
        case GetCommandType.WITH_LIST:
            handleGetResponseWithList(settings, reply);
            ret = false;
            break;
        default:
            throw new IllegalArgumentException("Invalid Get response.");
        }
        if (reply.getXml() != null) {
            if (!empty) {
                reply.getXml().appendEndTag(TranslatorTags.RESULT);
            }
            reply.getXml().appendEndTag(Command.GET_RESPONSE, type);
            reply.getXml().appendEndTag(Command.GET_RESPONSE);
        }
        return ret;
    }

    /**
     * Handle General block transfer message.
     * 
     * @param settings
     *            DLMS settings.
     * @param data
     *            received data.
     */
    @SuppressWarnings("squid:S106")
    private static void handleGbt(final GXDLMSSettings settings, final GXReplyData data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        int index = data.getData().position() - 1;
        data.setWindowSize(settings.getGbtWindowSize());
        // BlockControl
        short bc = data.getData().getUInt8();
        // Is streaming active.
        data.setStreaming((bc & 0x40) != 0);
        // GBT Window size.
        byte windowSize = (byte) (bc & 0x3F);
        // Block number.
        int bn = data.getData().getUInt16();
        // Block number acknowledged.
        int bna = data.getData().getUInt16();
        if (data.getXml() == null) {
            // Remove existing data when first block is received.
            if (bn == 1) {
                index = 0;
            } else if (bna != settings.getBlockIndex() - 1) {
                // If this block is already received.
                data.getData().size(index);
                data.setCommand(Command.NONE);
                return;
            }
        }
        data.setBlockNumber(bn);
        // Block number acknowledged.
        data.setBlockNumberAck(bna);
        settings.setBlockNumberAck(data.getBlockNumber());
        data.setCommand(Command.NONE);
        int len = GXCommon.getObjectCount(data.getData());
        if (len > data.getData().size() - data.getData().position()) {
            data.setComplete(false);
            return;
        }

        if (data.getXml() != null) {
            if ((data.getData().size() - data.getData().position()) != len) {
                data.getXml().appendComment("Data length is " + String.valueOf(len) + " and there are "
                        + String.valueOf(data.getData().size() - data.getData().position()) + " bytes.");
            }
            data.getXml().appendStartTag(Command.GENERAL_BLOCK_TRANSFER);
            if (data.getXml().isComments()) {
                data.getXml().appendComment("Last block: " + ((bc & 0x80) != 0));
                data.getXml().appendComment("Streaming: " + data.getStreaming());
                data.getXml().appendComment("Window size: " + windowSize);
            }
            data.getXml().appendLine(TranslatorTags.BLOCK_CONTROL, null, data.getXml().integerToHex(bc, 2));
            data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, null,
                    data.getXml().integerToHex(data.getBlockNumber(), 4));
            data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER_ACK, null,
                    data.getXml().integerToHex(data.getBlockNumberAck(), 4));

            // If last block and comments.
            if ((bc & 0x80) != 0 && data.getXml().isComments() && data.getData().available() != 0) {
                int pos = data.getData().position();
                int len2 = data.getXml().getXmlLength();
                try {
                    GXReplyData reply = new GXReplyData();
                    reply.setData(data.getData());
                    reply.setXml(data.getXml());
                    reply.getXml().startComment("");
                    getPdu(settings, reply);
                    reply.getXml().endComment();
                } catch (Exception ex) {
                    data.getXml().setXmlLength(len2);
                    // It's ok if this fails.
                }
                data.getData().position(pos);
            }
            data.getXml().appendLine(TranslatorTags.BLOCK_DATA, null,
                    GXCommon.toHex(data.getData().getData(), false, data.getData().position(), len));
            data.getXml().appendEndTag(Command.GENERAL_BLOCK_TRANSFER);
            return;
        }
        getDataFromBlock(data.getData(), index);
        // Is Last block.
        if ((bc & 0x80) == 0) {
            data.getMoreData().add(RequestTypes.GBT);
        } else {
            data.getMoreData().remove(RequestTypes.GBT);
            if (data.getData().size() != 0) {
                data.getData().position(0);
                getPdu(settings, data);
            }
            // Get data if all data is read or we want to peek data.
            if (data.getData().position() != data.getData().size()
                    && (data.getCommand() == Command.READ_RESPONSE || data.getCommand() == Command.GET_RESPONSE)
                    && (data.getMoreData().isEmpty() || data.getPeek())) {
                data.getData().position(0);
                getValueFromData(settings, data);
            }
        }
    }

    /*
     * Get PDU from the packet.
     * @param settings DLMS settings.
     * @param data received data.
     */
    public static void getPdu(final GXDLMSSettings settings, final GXReplyData data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        int cmd = data.getCommand();
        // If header is not read yet or GBT message.
        if (data.getCommand() == Command.NONE) {
            // If PDU is missing.
            if (data.getData().size() - data.getData().position() == 0) {
                throw new IllegalArgumentException("Invalid PDU.");
            }
            int index = data.getData().position();
            // Get command.
            cmd = data.getData().getUInt8();
            data.setCommand(cmd);
            switch (cmd) {
            case Command.READ_RESPONSE:
                if (!handleReadResponse(settings, data, index)) {
                    return;
                }
                break;
            case Command.GET_RESPONSE:
                if (!handleGetResponse(settings, data, index)) {
                    return;
                }
                break;
            case Command.SET_RESPONSE:
                handleSetResponse(settings, data);
                break;
            case Command.WRITE_RESPONSE:
                handleWriteResponse(data);
                break;
            case Command.METHOD_RESPONSE:
                handleMethodResponse(settings, data, index);
                break;
            case Command.ACCESS_REQUEST:
                if (data.getXml() != null
                        || (!settings.isServer() && !data.getMoreData().contains(RequestTypes.FRAME))) {
                    GXDLMSLNCommandHandler.handleAccessRequest(settings, null, data.getData(), null, data.getXml(),
                            Command.NONE);
                }
                break;
            case Command.ACCESS_RESPONSE:
                handleAccessResponse(settings, data);
                break;
            case Command.GENERAL_BLOCK_TRANSFER:
                if (data.getXml() != null
                        || (!settings.isServer() && !data.getMoreData().contains(RequestTypes.FRAME))) {
                    handleGbt(settings, data);
                }
                break;
            case Command.AARQ:
            case Command.AARE:
                // This is parsed later.
                data.getData().position(data.getData().position() - 1);
                break;
            case Command.RELEASE_RESPONSE:
                break;
            case Command.CONFIRMED_SERVICE_ERROR:
                handleConfirmedServiceError(data);
                break;
            case Command.EXCEPTION_RESPONSE:
                handleExceptionResponse(data);
                break;
            case Command.GET_REQUEST:
                if (data.getXml() != null
                        || (!settings.isServer() && !data.getMoreData().contains(RequestTypes.FRAME))) {
                    GXDLMSLNCommandHandler.handleGetRequest(settings, null, data.getData(), null, data.getXml(),
                            Command.NONE);
                }
                break;
            case Command.READ_REQUEST:
            case Command.WRITE_REQUEST:
            case Command.SET_REQUEST:
            case Command.METHOD_REQUEST:
            case Command.RELEASE_REQUEST:
                // Server handles this.
                if (data.getMoreData().contains(RequestTypes.FRAME)) {
                    break;
                }
                break;
            case Command.GLO_READ_REQUEST:
            case Command.GLO_WRITE_REQUEST:
            case Command.GLO_GET_REQUEST:
            case Command.GLO_SET_REQUEST:
            case Command.GLO_METHOD_REQUEST:
            case Command.DED_GET_REQUEST:
            case Command.DED_SET_REQUEST:
            case Command.DED_METHOD_REQUEST:
                handleGloDedRequest(settings, data);
                // Server handles this.
                break;
            case Command.GLO_READ_RESPONSE:
            case Command.GLO_WRITE_RESPONSE:
            case Command.GLO_GET_RESPONSE:
            case Command.GLO_SET_RESPONSE:
            case Command.GLO_METHOD_RESPONSE:
            case Command.GLO_EVENT_NOTIFICATION_REQUEST:
            case Command.DED_GET_RESPONSE:
            case Command.DED_SET_RESPONSE:
            case Command.DED_METHOD_RESPONSE:
            case Command.DED_EVENT_NOTIFICATION:
                handleGloDedResponse(settings, data, index);
                break;
            case Command.GENERAL_GLO_CIPHERING:
            case Command.GENERAL_DED_CIPHERING:
                if (settings.isServer()) {
                    handleGloDedRequest(settings, data);
                } else {
                    handleGloDedResponse(settings, data, index);
                }
                break;
            case Command.GENERAL_SIGNING:
                if (settings.isServer()) {
                    handleGloDedRequest(settings, data);
                } else {
                    handleGloDedResponse(settings, data, index);
                }
                break;
            case Command.DATA_NOTIFICATION:
                handleDataNotification(settings, data);
                break;
            case Command.EVENT_NOTIFICATION:
                // Client handles this.
                break;
            case Command.INFORMATION_REPORT:
                // Client handles this.
                break;
            case Command.GENERAL_CIPHERING:
                handleGeneralCiphering(settings, data);
                break;
            case Command.GATEWAY_REQUEST:
            case Command.GATEWAY_RESPONSE:
                data.getData().getUInt8();
                int len = GXCommon.getObjectCount(data.getData());
                byte[] pda = new byte[len];
                data.getData().get(pda);
                getDataFromBlock(data.getData(), index);
                data.setCommand(Command.NONE);
                getPdu(settings, data);
                break;
            case Command.PING_RESPONSE:
            case Command.DISCOVER_REPORT:
            case Command.DISCOVER_REQUEST:
            case Command.REGISTER_REQUEST:
                break;
            default:
                throw new IllegalArgumentException("Invalid Command.");
            }
        } else if (!data.getMoreData().contains(RequestTypes.FRAME)) {
            // Is whole block is read and if last packet and data is not try to
            // peek.
            if (!data.getPeek() && data.getMoreData().isEmpty()) {
                if (data.getCommand() == Command.AARE || data.getCommand() == Command.AARQ) {
                    data.getData().position(0);
                } else {
                    data.getData().position(1);
                }
            }
            if (cmd == Command.GENERAL_BLOCK_TRANSFER) {
                data.getData().position(data.getCipherIndex() + 1);
                handleGbt(settings, data);
                data.setCipherIndex(data.getData().size());
                data.setCommand(Command.NONE);
            } else if (settings.isServer()) {
                // Get command if operating as a server.
                // Ciphered messages are handled after whole PDU is received.
                switch (cmd) {
                case Command.GLO_READ_REQUEST:
                case Command.GLO_WRITE_REQUEST:
                case Command.GLO_GET_REQUEST:
                case Command.GLO_SET_REQUEST:
                case Command.GLO_METHOD_REQUEST:
                case Command.GLO_EVENT_NOTIFICATION_REQUEST:
                case Command.DED_GET_REQUEST:
                case Command.DED_SET_REQUEST:
                case Command.DED_METHOD_REQUEST:
                case Command.DED_EVENT_NOTIFICATION:
                case Command.GENERAL_CIPHERING:
                case Command.GENERAL_SIGNING:
                    data.setCommand(Command.NONE);
                    data.getData().position(data.getCipherIndex());
                    getPdu(settings, data);
                    break;
                default:
                    break;
                }
            } else {
                if (data.isMoreData()) {
                    data.setCommand(Command.NONE);
                }
                // Ciphered messages are handled after whole PDU is received.
                switch (cmd) {
                case Command.GLO_READ_RESPONSE:
                case Command.GLO_WRITE_RESPONSE:
                case Command.GLO_GET_RESPONSE:
                case Command.GLO_SET_RESPONSE:
                case Command.GLO_METHOD_RESPONSE:
                case Command.DED_GET_RESPONSE:
                case Command.DED_SET_RESPONSE:
                case Command.DED_METHOD_RESPONSE:
                case Command.GENERAL_GLO_CIPHERING:
                case Command.GENERAL_DED_CIPHERING:
                case Command.GENERAL_CIPHERING:
                case Command.ACCESS_RESPONSE:
                case Command.GENERAL_SIGNING:
                    data.setCommand(Command.NONE);
                    data.getData().position(data.getCipherIndex());
                    getPdu(settings, data);
                    break;
                default:
                    break;
                }
                if (cmd == Command.READ_RESPONSE && data.getTotalCount() > 1) {
                    if (!handleReadResponse(settings, data, 0)) {
                        return;
                    }
                }
            }
        }

        // Get data only blocks if SN is used. This is faster.
        if (cmd == Command.READ_RESPONSE && data.getCommandType() == SingleReadResponse.DATA_BLOCK_RESULT
                && data.getMoreData().contains(RequestTypes.FRAME)) {
            return;
        }
        // Get data if all data is read or we want to peek data.
        if (data.getError() == 0 && data.getXml() == null && data.getData().position() != data.getData().size()
                && (cmd == Command.READ_RESPONSE || cmd == Command.GET_RESPONSE || cmd == Command.METHOD_RESPONSE
                        || cmd == Command.DATA_NOTIFICATION)
                && (data.getMoreData().isEmpty() || data.getPeek())) {
            getValueFromData(settings, data);
        }
    }

    static void handleConfirmedServiceError(final GXReplyData data) {
        if (data.getXml() != null) {
            data.getXml().appendStartTag(Command.CONFIRMED_SERVICE_ERROR);
            if (data.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML) {
                data.getData().getUInt8();
                data.getXml().appendStartTag(TranslatorTags.INITIATE_ERROR);
                ServiceError type = ServiceError.forValue(data.getData().getUInt8());

                String tag = TranslatorStandardTags.serviceErrorToString(type);
                String value = TranslatorStandardTags.getServiceErrorValue(type, (byte) data.getData().getUInt8());
                data.getXml().appendLine("x:" + tag, null, value);
                data.getXml().appendEndTag(TranslatorTags.INITIATE_ERROR);
            } else {
                data.getXml().appendLine(TranslatorTags.SERVICE, "Value",
                        data.getXml().integerToHex(data.getData().getUInt8(), 2));
                ServiceError type = ServiceError.forValue(data.getData().getUInt8());
                data.getXml().appendStartTag(TranslatorTags.SERVICE_ERROR);
                data.getXml().appendLine(TranslatorSimpleTags.serviceErrorToString(type), "Value",
                        TranslatorSimpleTags.getServiceErrorValue(type, (byte) data.getData().getUInt8()));
                data.getXml().appendEndTag(TranslatorTags.SERVICE_ERROR);
            }
            data.getXml().appendEndTag(Command.CONFIRMED_SERVICE_ERROR);
        } else {
            ConfirmedServiceError service = ConfirmedServiceError.forValue(data.getData().getUInt8());
            ServiceError type = ServiceError.forValue(data.getData().getUInt8());
            throw new GXDLMSConfirmedServiceError(service, type, data.getData().getUInt8());
        }
    }

    static void handleExceptionResponse(final GXReplyData data) {
        ExceptionStateError state = ExceptionStateError.forValue(data.getData().getUInt8());
        ExceptionServiceError error = ExceptionServiceError.forValue(data.getData().getUInt8());
        Object value = null;
        if (error == ExceptionServiceError.INVOCATION_COUNTER_ERROR && data.getData().available() > 3) {
            value = data.getData().getUInt32();
        }
        if (data.getXml() != null) {
            data.getXml().appendStartTag(Command.EXCEPTION_RESPONSE);
            if (data.getXml().getOutputType() == TranslatorOutputType.STANDARD_XML) {
                data.getXml().appendLine(TranslatorTags.STATE_ERROR, null,
                        TranslatorStandardTags.stateErrorToString(state));
                data.getXml().appendLine(TranslatorTags.SERVICE_ERROR, null,
                        TranslatorStandardTags.exceptionServiceErrorToString(error));
            } else {
                data.getXml().appendLine(TranslatorTags.STATE_ERROR, null,
                        TranslatorSimpleTags.stateErrorToString(state));
                data.getXml().appendLine(TranslatorTags.SERVICE_ERROR, null,
                        TranslatorSimpleTags.exceptionServiceErrorToString(error));
            }
            data.getXml().appendEndTag(Command.EXCEPTION_RESPONSE);
        } else {
            throw new GXDLMSExceptionResponse(state, error, value);
        }
    }

    private static void handleGloDedRequest(final GXDLMSSettings settings, final GXReplyData data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        if (data.getXml() != null) {
            data.getData().position(data.getData().position() - 1);
        } else {
            if (settings.getCipher() == null) {
                throw new IllegalArgumentException("Secure connection is not supported.");
            }
            // If all frames are read.
            if (!data.getMoreData().contains(RequestTypes.FRAME)) {
                data.getData().position(data.getData().position() - 1);
                AesGcmParameter p;
                GXICipher cipher = settings.getCipher();
                if (data.getCommand() == Command.GENERAL_DED_CIPHERING) {
                    p = new AesGcmParameter(settings, settings.getSourceSystemTitle(), cipher.getDedicatedKey(),
                            getAuthenticationKey(settings));
                } else if (data.getCommand() == Command.GENERAL_GLO_CIPHERING) {
                    p = new AesGcmParameter(settings, settings.getSourceSystemTitle(), getBlockCipherKey(settings),
                            getAuthenticationKey(settings));
                } else if (cipher.getDedicatedKey() == null || isGloMessage(data.getCommand())) {
                    p = new AesGcmParameter(settings, settings.getSourceSystemTitle(), getBlockCipherKey(settings),
                            getAuthenticationKey(settings));
                } else {
                    p = new AesGcmParameter(settings, settings.getSourceSystemTitle(), cipher.getDedicatedKey(),
                            getAuthenticationKey(settings));
                }
                byte[] tmp = GXCiphering.decrypt(settings.getCipher(), p, data.getData());
                cipher.setSecuritySuite(p.getSecuritySuite());
                cipher.setSecurity(p.getSecurity());
                data.getData().clear();
                data.getData().set(tmp);
                // Get command.
                data.setCipheredCommand(data.getCommand());
                data.setCommand(data.getData().getUInt8());
                if (data.getCommand() == Command.DATA_NOTIFICATION || data.getCommand() == Command.INFORMATION_REPORT) {
                    data.setCommand(Command.NONE);
                    data.getData().position(data.getData().position() - 1);
                    getPdu(settings, data);
                }
            } else {
                data.getData().position(data.getData().position() - 1);
            }
        }
    }

    private static void handleGloDedResponse(final GXDLMSSettings settings, final GXReplyData data, final int index)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        if (data.getXml() != null) {
            data.getData().position(data.getData().position() - 1);
        } else {
            if (settings.getCipher() == null) {
                throw new IllegalArgumentException("Secure connection is not supported.");
            }
            // If all frames are read.
            if (!data.getMoreData().contains(RequestTypes.FRAME)) {
                data.getData().position(data.getData().position() - 1);
                GXByteBuffer bb = new GXByteBuffer(data.getData());
                data.getData().position(index);
                data.getData().size(index);

                AesGcmParameter p;
                if (settings.getCipher().getDedicatedKey() != null && settings.getConnected() == ConnectionState.DLMS) {
                    p = new AesGcmParameter(settings, settings.getSourceSystemTitle(),
                            settings.getCipher().getDedicatedKey(), getAuthenticationKey(settings));
                } else {
                    if (settings.getPreEstablishedSystemTitle() != null
                            && (settings.getConnected() & ConnectionState.DLMS) == 0) {
                        p = new AesGcmParameter(settings, settings.getPreEstablishedSystemTitle(),
                                getBlockCipherKey(settings), getAuthenticationKey(settings));
                    } else {
                        p = new AesGcmParameter(settings, settings.getSourceSystemTitle(), getBlockCipherKey(settings),
                                getAuthenticationKey(settings));
                    }

                }
                data.getData().set(GXCiphering.decrypt(settings.getCipher(), p, bb));
                // Get command.
                data.setCipheredCommand(data.getCommand());
                data.setCommand(Command.NONE);
                if (!settings.isDecryptOnly()) {
                    getPdu(settings, data);
                    data.setCipherIndex(data.getData().size());
                } else {
                    data.setCipherIndex(data.getCipherIndex() + bb.position());
                }
            }
        }
    }

    private static void handleGeneralCiphering(final GXDLMSSettings settings, final GXReplyData data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        if (settings.getCipher() == null) {
            throw new IllegalArgumentException("Secure connection is not supported.");
        }
        // If all frames are read.
        if (!data.getMoreData().contains(RequestTypes.FRAME)) {
            int origPos = 0;
            if (data.getXml() != null) {
                origPos = data.getXml().getXmlLength();
            }
            data.getData().position(data.getData().position() - 1);
            AesGcmParameter p = new AesGcmParameter(settings, settings.getSourceSystemTitle(),
                    getBlockCipherKey(settings), getAuthenticationKey(settings));
            p.setXml(data.getXml());
            try {
                byte[] tmp = GXCiphering.decrypt(settings.getCipher(), p, data.getData());
                data.getData().clear();
                data.getData().set(tmp);
                data.setCipheredCommand(Command.GENERAL_CIPHERING);
                data.setCommand(Command.NONE);
                if (p.getSecurity() != null) {
                    getPdu(settings, data);
                }
            } catch (Exception ex) {
                if (data.getXml() == null) {
                    Logger.getLogger(GXSecure.class.getName()).log(Level.SEVERE, "handleGeneralCiphering failed. {0}",
                            ex.getMessage());
                    throw ex;
                }
                data.getXml().setXmlLength(origPos);
            }
            if (data.getXml() != null) {
                data.getXml().appendStartTag(Command.GENERAL_CIPHERING);
                data.getXml().appendLine(TranslatorTags.TRANSACTION_ID, null,
                        data.getXml().integerToHex(p.getInvocationCounter(), 16, true));
                data.getXml().appendLine(TranslatorTags.ORIGINATOR_SYSTEM_TITLE, null,
                        GXCommon.toHex(p.getSystemTitle(), false));
                data.getXml().appendLine(TranslatorTags.RECIPIENT_SYSTEM_TITLE, null,
                        GXCommon.toHex(p.getRecipientSystemTitle(), false));
                data.getXml().appendLine(TranslatorTags.DATE_TIME, null, GXCommon.toHex(p.getDateTime(), false));
                data.getXml().appendLine(TranslatorTags.OTHER_INFORMATION, null,
                        GXCommon.toHex(p.getOtherInformation(), false));

                data.getXml().appendStartTag(TranslatorTags.KEY_INFO);
                data.getXml().appendStartTag(TranslatorTags.AGREED_KEY);
                data.getXml().appendLine(TranslatorTags.KEY_PARAMETERS, null,
                        data.getXml().integerToHex(p.getKeyParameters(), 2, true));
                data.getXml().appendLine(TranslatorTags.KEY_CIPHERED_DATA, null,
                        GXCommon.toHex(p.getKeyCipheredData(), false));
                data.getXml().appendEndTag(TranslatorTags.AGREED_KEY);
                data.getXml().appendEndTag(TranslatorTags.KEY_INFO);

                data.getXml().appendLine(TranslatorTags.CIPHERED_CONTENT, null,
                        GXCommon.toHex(p.getCipheredContent(), false));

                data.getXml().appendEndTag(Command.GENERAL_CIPHERING);
            }
        }
    }

    /*
     * Get value from data.
     * @param settings DLMS settings.
     * @param reply Received data.
     */
    @SuppressWarnings("unchecked")
    static void getValueFromData(final GXDLMSSettings settings, final GXReplyData reply) {
        GXByteBuffer data = reply.getData();
        GXDataInfo info = new GXDataInfo();
        if (reply.getValue() instanceof List<?>) {
            info.setType(DataType.ARRAY);
            info.setCount(reply.getTotalCount());
            info.setIndex(reply.getCount());
        }
        int index = data.position();
        data.position(reply.getReadPosition());
        try {
            Object value = GXCommon.getData(settings, data, info);
            if (value != null) { // If new data.
                if (!(value instanceof List<?>)) {
                    reply.setValueType(info.getType());
                    reply.setValue(value);
                    reply.setTotalCount(0);
                    reply.setReadPosition(data.position());
                } else {
                    if (!((List<?>) value).isEmpty()) {
                        if (reply.getValue() == null) {
                            reply.setValue(value);
                        } else {
                            // Add items to collection.
                            ((List<Object>) reply.getValue()).addAll((List<?>) value);
                        }
                    }
                    reply.setReadPosition(data.position());
                    // Element count.
                    reply.setTotalCount(info.getCount());
                }
            } else if (info.isComplete() && reply.getCommand() == Command.DATA_NOTIFICATION) {
                // If last item is null. This is a special case.
                reply.setReadPosition(data.position());
            }
        } finally {
            data.position(index);
        }

        // If last data frame of the data block is read.
        if (reply.getCommand() != Command.DATA_NOTIFICATION && info.isComplete() && reply.getMoreData().isEmpty()) {
            // If all blocks are read.
            if (settings != null) {
                settings.resetBlockIndex();
            }
            data.position(0);
        }
    }

    public static boolean getData(final GXDLMSSettings settings, final GXByteBuffer reply, final GXReplyData data,
            final GXReplyData notify) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        short frame = 0;
        boolean isLast = true;
        boolean isNotify = false;
        GXReplyData target = data;
        // If DLMS frame is generated.
        switch (settings.getInterfaceType()) {
        case HDLC:
        case HDLC_WITH_MODE_E:
            frame = getHdlcData(settings.isServer(), settings, reply, target, notify);
            isLast = (frame & 0x10) != 0;
            if (notify != null && (frame == 0x13 || frame == 0x3)) {
                target = notify;
                isNotify = true;
            }
            target.setFrameId(frame);
            break;
        case WRAPPER:
        case PRIME_DC_WRAPPER:
            if (!getTcpData(settings, reply, target, notify)) {
                if (notify != null) {
                    target = notify;
                    isNotify = true;
                }
            }
            break;
        case WIRELESS_MBUS:
            getWirelessMBusData(settings, reply, target);
            break;
        case PDU:
            target.setPacketLength(reply.size());
            target.setComplete(reply.size() != 0);
            break;
        case PLC:
            getPlcData(settings, reply, data);
            break;
        case PLC_HDLC:
            frame = getPlcHdlcData(settings, reply, data);
            break;
        case WIRED_MBUS:
            getWiredMBusData(settings, reply, data);
            break;
        case SMS:
            if (!getSmsData(settings, reply, target, notify)) {
                if (notify != null) {
                    target = notify;
                }
                isNotify = true;
            }
            break;
        case COAP:
            if (!getCoAPData(settings, reply, target, notify)) {
                if (notify != null) {
                    target = notify;
                }
                isNotify = true;
            }
            break;
        default:
            throw new IllegalArgumentException("Invalid Interface type.");
        }
        // If all data is not read yet.
        if (!target.isComplete()) {
            return false;
        }

        if (settings.getInterfaceType() != InterfaceType.PLC_HDLC) {
            getDataFromFrame(reply, target, useHdlc(settings.getInterfaceType()));
        }

        // If keepalive or get next frame request.
        if (target.getXml() != null || ((frame != 0x13 && frame != 0x3 || data.isMoreData()) && (frame & 0x1) != 0)) {
            if (frame == 0x3 && data.isMoreData()) {
                boolean tmp = getData(settings, reply, data, notify);
                data.getData().position(0);
                return tmp;
            }
            return true;
        }
        if (frame == 0x13 && !data.isMoreData()) {
            data.getData().position(0);
        }
        try {
            if (settings.getInterfaceType() == InterfaceType.COAP) {
                if (target.getMoreData().isEmpty()) {
                    // Only full PDUs are handled with CoAP.
                    target.getData().position(0);
                    getPdu(settings, target);
                }
            } else {
                getPdu(settings, target);
            }
        } catch (Exception ex) {
            // Ignore received data if meter is sending invalid push message
            // while data is
            // read from the meter.
            if (!isNotify) {
                throw ex;
            }
            isLast = false;
            data.setCommand(Command.NONE);
        }
        if (notify != null && !isNotify) {
            // Check command to make sure it's not notify message.
            switch (target.getCommand()) {
            case Command.DATA_NOTIFICATION:
            case Command.GLO_EVENT_NOTIFICATION_REQUEST:
            case Command.INFORMATION_REPORT:
            case Command.EVENT_NOTIFICATION:
            case Command.DED_EVENT_NOTIFICATION:
                isNotify = true;
                notify.setComplete(data.isComplete());
                notify.getMoreData().addAll(data.getMoreData());
                data.getMoreData().clear();
                notify.setCommand(data.getCommand());
                data.setCommand(Command.NONE);
                notify.setTime(data.getTime());
                data.setTime(null);
                notify.getData().set(data.getData());
                data.getData().trim();
                notify.setValue(data.getValue());
                data.setValue(null);
                break;
            default:
                break;
            }
        }
        if (!isLast) {
            return getData(settings, reply, data, notify);
        }
        return !isNotify;
    }

    /**
     * Get data from HDLC or wrapper frame.
     * 
     * @param reply
     *            Received data that includes HDLC frame.
     * @param info
     *            Reply data.
     * @param hdlc
     *            Is HDLC framing used.
     */
    private static void getDataFromFrame(final GXByteBuffer reply, final GXReplyData info, final boolean hdlc) {
        GXByteBuffer data = info.getData();
        int offset = data.size();
        int cnt = info.getPacketLength() - reply.position();
        if (cnt != 0) {
            data.capacity(offset + cnt);
            data.set(reply.getData(), reply.position(), cnt);
            reply.position(reply.position() + cnt);
            if (hdlc) {
                reply.position(reply.position() + 3);
            }
        }
        // Set position to begin of new data.
        data.position(offset);
    }

    /**
     * Get data from Block.
     * 
     * @param data
     *            Stored data block.
     * @param index
     *            Position where data starts.
     * @return Amount of removed bytes.
     */
    private static int getDataFromBlock(final GXByteBuffer data, final int index) {
        if (data.size() == data.position()) {
            data.clear();
            return 0;
        }
        int len = data.position() - index;
        System.arraycopy(data.getData(), data.position(), data.getData(), data.position() - len,
                data.size() - data.position());
        data.position(data.position() - len);
        data.size(data.size() - len);
        return len;
    }

    /**
     * Returns action method information.
     * 
     * @param objectType
     *            Target object type.
     * @param value
     *            Starting address of action methods.
     * @param count
     *            Count of action methods.
     */
    @SuppressWarnings("squid:S1871")
    static void getActionInfo(final ObjectType objectType, final int[] value, final int[] count) {
        switch (objectType) {
        case DATA:
        case ACTION_SCHEDULE:
        case NONE:
        case AUTO_ANSWER:
        case AUTO_CONNECT:
        case MAC_ADDRESS_SETUP:
        case GPRS_SETUP:
        case IEC_HDLC_SETUP:
        case IEC_LOCAL_PORT_SETUP:
        case IEC_TWISTED_PAIR_SETUP:
        case MODEM_CONFIGURATION:
        case PPP_SETUP:
        case REGISTER_MONITOR:
        case ZIG_BEE_SAS_STARTUP:
        case ZIG_BEE_SAS_JOIN:
        case ZIG_BEE_SAS_APS_FRAGMENTATION:
        case SCHEDULE:
        case STATUS_MAPPING:
        case TCP_UDP_SETUP:
        case UTILITY_TABLES:
            value[0] = 0;
            count[0] = 0;
            break;
        case IMAGE_TRANSFER:
            value[0] = 0x40;
            count[0] = 4;
            break;
        case ACTIVITY_CALENDAR:
            value[0] = 0x50;
            count[0] = 1;
            break;
        case ASSOCIATION_LOGICAL_NAME:
            value[0] = 0x60;
            count[0] = 4;
            break;
        case ASSOCIATION_SHORT_NAME:
            value[0] = 0x20;
            count[0] = 8;
            break;
        case CLOCK:
            value[0] = 0x60;
            count[0] = 6;
            break;
        case DEMAND_REGISTER:
            value[0] = 0x48;
            count[0] = 2;
            break;
        case EXTENDED_REGISTER:
            value[0] = 0x38;
            count[0] = 1;
            break;
        case IP4_SETUP:
            value[0] = 0x60;
            count[0] = 3;
            break;
        case MBUS_SLAVE_PORT_SETUP:
            value[0] = 0x60;
            count[0] = 8;
            break;
        case PROFILE_GENERIC:
            value[0] = 0x58;
            count[0] = 4;
            break;
        case REGISTER:
            value[0] = 0x28;
            count[0] = 1;
            break;
        case REGISTER_ACTIVATION:
            value[0] = 0x30;
            count[0] = 3;
            break;
        case REGISTER_TABLE:
            value[0] = 0x28;
            count[0] = 2;
            break;
        case SAP_ASSIGNMENT:
        case SCRIPT_TABLE:
            value[0] = 0x20;
            count[0] = 1;
            break;
        case SPECIAL_DAYS_TABLE:
            value[0] = 0x10;
            count[0] = 2;
            break;
        case SECURITY_SETUP:
            value[0] = 0x30;
            count[0] = 8;
            break;
        case DISCONNECT_CONTROL:
            value[0] = 0x20;
            count[0] = 2;
            break;
        case PUSH_SETUP:
            value[0] = 0x38;
            count[0] = 1;
            break;
        case NTP_SETUP:
            value[0] = 0x38;
            count[0] = 3;
            break;
        default:
            count[0] = 0;
            value[0] = 0;
            break;
        }
    }

    /**
     * Parses UAResponse from byte array.
     * 
     * @param data
     *            Received message from the server.
     * @param settings
     *            DLMS settings.
     * @see GXDLMSClient#snrmRequest
     */
    static void parseSnrmUaResponse(final GXByteBuffer data, final GXDLMSSettings settings) {
        GXHdlcSettings limits = settings.getHdlcSettings();
        // If default settings are used.
        if (data.available() == 0) {
            limits.setMaxInfoTX(GXDLMSLimits.DEFAULT_MAX_INFO_TX);
            limits.setMaxInfoRX(GXDLMSLimits.DEFAULT_MAX_INFO_RX);
            limits.setWindowSizeTX(GXDLMSLimits.DEFAULT_WINDOWS_SIZE_TX);
            limits.setWindowSizeRX(GXDLMSLimits.DEFAULT_WINDOWS_SIZE_RX);
        } else {
            data.getUInt8(); // Skip FromatID
            data.getUInt8(); // Skip Group ID.
            data.getUInt8(); // Skip Group len
            int val;
            while (data.position() < data.size()) {
                short id = data.getUInt8();
                short len = data.getUInt8();
                switch (len) {
                case 1:
                    val = data.getUInt8();
                    break;
                case 2:
                    val = data.getUInt16();
                    break;
                case 4:
                    val = (int) data.getUInt32();
                    break;
                default:
                    throw new GXDLMSException("Invalid Exception.");
                }
                // RX / TX are delivered from the partner's point of view =>
                // reversed to ours
                switch (id) {
                case HDLCInfo.MAX_INFO_RX:
                    limits.setMaxInfoTX(val);
                    if (limits.isUseFrameSize()) {
                        byte[] secondaryAddress = GXDLMS.getHdlcAddressBytes(settings.getClientAddress(), 0);
                        limits.setMaxInfoTX(limits.getMaxInfoTX() + 10 + secondaryAddress.length);
                    }
                    break;
                case HDLCInfo.MAX_INFO_TX:
                    limits.setMaxInfoRX(val);
                    break;
                case HDLCInfo.WINDOW_SIZE_RX:
                    limits.setWindowSizeTX(val);
                    break;
                case HDLCInfo.WINDOW_SIZE_TX:
                    limits.setWindowSizeRX(val);
                    break;
                default:
                    throw new GXDLMSException("Invalid UA response.");
                }
            }
        }
    }

    static void appendHdlcParameter(final GXByteBuffer data, final int value) {
        if (value < 0x100) {
            data.setUInt8(1);
            data.setUInt8(value);
        } else {
            data.setUInt8(2);
            data.setUInt16(value);
        }
    }

    protected static void addInvokeId(GXDLMSTranslatorStructure xml, int command, byte type, long invokeID) {
        if (xml != null) {
            xml.appendStartTag(command);
            xml.appendStartTag(command, type);
            if (xml.isComments()) {
                StringBuilder sb = new StringBuilder();
                if ((invokeID & 0x80) != 0) {
                    sb.append("Priority: HIGH ");
                } else {
                    sb.append("Priority: NORMAL ");
                }
                if ((invokeID & 0x40) != 0) {
                    sb.append("ServiceClass: CONFIRMED ");
                } else {
                    sb.append("ServiceClass: UN_CONFIRMED ");
                }
                sb.append("InvokeID: " + String.valueOf(invokeID & 0xF));
                xml.appendComment(sb.toString());
            }
            xml.appendLine(TranslatorTags.INVOKE_ID, null, xml.integerToHex(invokeID, 2));
        }
    }
}
