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

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gurux.dlms.asn.GXAsn1Converter;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ExceptionServiceError;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.StateError;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.internal.GXDataInfo;
import gurux.dlms.objects.GXDLMSAccount;
import gurux.dlms.objects.GXDLMSActionSchedule;
import gurux.dlms.objects.GXDLMSActivityCalendar;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSAutoAnswer;
import gurux.dlms.objects.GXDLMSAutoConnect;
import gurux.dlms.objects.GXDLMSCharge;
import gurux.dlms.objects.GXDLMSClock;
import gurux.dlms.objects.GXDLMSCredit;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSDemandRegister;
import gurux.dlms.objects.GXDLMSDisconnectControl;
import gurux.dlms.objects.GXDLMSExtendedRegister;
import gurux.dlms.objects.GXDLMSGSMDiagnostic;
import gurux.dlms.objects.GXDLMSGprsSetup;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSIECOpticalPortSetup;
import gurux.dlms.objects.GXDLMSIecTwistedPairSetup;
import gurux.dlms.objects.GXDLMSImageTransfer;
import gurux.dlms.objects.GXDLMSIp4Setup;
import gurux.dlms.objects.GXDLMSIp6Setup;
import gurux.dlms.objects.GXDLMSLimiter;
import gurux.dlms.objects.GXDLMSMBusClient;
import gurux.dlms.objects.GXDLMSMBusMasterPortSetup;
import gurux.dlms.objects.GXDLMSMBusSlavePortSetup;
import gurux.dlms.objects.GXDLMSMacAddressSetup;
import gurux.dlms.objects.GXDLMSModemConfiguration;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSParameterMonitor;
import gurux.dlms.objects.GXDLMSPppSetup;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXDLMSPushSetup;
import gurux.dlms.objects.GXDLMSRegister;
import gurux.dlms.objects.GXDLMSRegisterActivation;
import gurux.dlms.objects.GXDLMSRegisterMonitor;
import gurux.dlms.objects.GXDLMSSapAssignment;
import gurux.dlms.objects.GXDLMSSchedule;
import gurux.dlms.objects.GXDLMSScriptTable;
import gurux.dlms.objects.GXDLMSSecuritySetup;
import gurux.dlms.objects.GXDLMSSpecialDaysTable;
import gurux.dlms.objects.GXDLMSTcpUdpSetup;
import gurux.dlms.objects.GXDLMSTokenGateway;
import gurux.dlms.objects.enums.CertificateType;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.dlms.secure.AesGcmParameter;
import gurux.dlms.secure.CountType;
import gurux.dlms.secure.GXASymmetric;
import gurux.dlms.secure.GXCiphering;

/**
 * GXDLMS implements methods to communicate with DLMS/COSEM metering devices.
 */
abstract class GXDLMS {
    static final byte CIPHERING_HEADER_SIZE = 7 + 12 + 3;
    static final int DATA_TYPE_OFFSET = 0xFF0000;

    /**
     * Constructor.
     */
    private GXDLMS() {

    }

    static byte getInvokeIDPriority(final GXDLMSSettings settings) {
        byte value = 0;
        if (settings.getPriority() == Priority.HIGH) {
            value |= 0x80;
        }
        if (settings.getServiceClass() == ServiceClass.CONFIRMED) {
            value |= 0x40;
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

    static GXDLMSObject createObject(final ObjectType type) {
        // If IC is manufacturer specific or unknown.
        if (type == null) {
            return new GXDLMSObject();
        }
        switch (type) {
        case ACTION_SCHEDULE:
            return new GXDLMSActionSchedule();
        case ACTIVITY_CALENDAR:
            return new GXDLMSActivityCalendar();
        case ASSOCIATION_LOGICAL_NAME:
            return new GXDLMSAssociationLogicalName();
        case ASSOCIATION_SHORT_NAME:
            return new GXDLMSAssociationShortName();
        case AUTO_ANSWER:
            return new GXDLMSAutoAnswer();
        case AUTO_CONNECT:
            return new GXDLMSAutoConnect();
        case CLOCK:
            return new GXDLMSClock();
        case DATA:
            return new GXDLMSData();
        case DEMAND_REGISTER:
            return new GXDLMSDemandRegister();
        case MAC_ADDRESS_SETUP:
            return new GXDLMSMacAddressSetup();
        case EXTENDED_REGISTER:
            return new GXDLMSExtendedRegister();
        case GPRS_SETUP:
            return new GXDLMSGprsSetup();
        case IEC_HDLC_SETUP:
            return new GXDLMSHdlcSetup();
        case IEC_LOCAL_PORT_SETUP:
            return new GXDLMSIECOpticalPortSetup();
        case IEC_TWISTED_PAIR_SETUP:
            return new GXDLMSIecTwistedPairSetup();
        case IP4_SETUP:
            return new GXDLMSIp4Setup();
        case IP6_SETUP:
            return new GXDLMSIp6Setup();
        case MBUS_SLAVE_PORT_SETUP:
            return new GXDLMSMBusSlavePortSetup();
        case IMAGE_TRANSFER:
            return new GXDLMSImageTransfer();
        case SECURITY_SETUP:
            return new GXDLMSSecuritySetup();
        case DISCONNECT_CONTROL:
            return new GXDLMSDisconnectControl();
        case LIMITER:
            return new GXDLMSLimiter();
        case MBUS_CLIENT:
            return new GXDLMSMBusClient();
        case MODEM_CONFIGURATION:
            return new GXDLMSModemConfiguration();
        case PPP_SETUP:
            return new GXDLMSPppSetup();
        case PROFILE_GENERIC:
            return new GXDLMSProfileGeneric();
        case REGISTER:
            return new GXDLMSRegister();
        case REGISTER_ACTIVATION:
            return new GXDLMSRegisterActivation();
        case REGISTER_MONITOR:
            return new GXDLMSRegisterMonitor();
        case REGISTER_TABLE:
            return new GXDLMSObject();
        case ZIG_BEE_SAS_STARTUP:
            return new GXDLMSObject();
        case ZIG_BEE_SAS_JOIN:
            return new GXDLMSObject();
        case SAP_ASSIGNMENT:
            return new GXDLMSSapAssignment();
        case SCHEDULE:
            return new GXDLMSSchedule();
        case SCRIPT_TABLE:
            return new GXDLMSScriptTable();
        case SPECIAL_DAYS_TABLE:
            return new GXDLMSSpecialDaysTable();
        case STATUS_MAPPING:
            return new GXDLMSObject();
        case TCP_UDP_SETUP:
            return new GXDLMSTcpUdpSetup();
        case ZIG_BEE_SAS_APS_FRAGMENTATION:
            return new GXDLMSObject();
        case UTILITY_TABLES:
            return new GXDLMSObject();
        case PUSH_SETUP:
            return new GXDLMSPushSetup();
        case MBUS_MASTER_PORT_SETUP:
            return new GXDLMSMBusMasterPortSetup();
        case GSM_DIAGNOSTIC:
            return new GXDLMSGSMDiagnostic();
        case ACCOUNT:
            return new GXDLMSAccount();
        case CREDIT:
            return new GXDLMSCredit();
        case CHARGE:
            return new GXDLMSCharge();
        case TOKEN_GATEWAY:
            return new GXDLMSTokenGateway();
        case PARAMETER_MONITOR:
            return new GXDLMSParameterMonitor();
        default:
            return new GXDLMSObject();
        }
    }

    /**
     * Generates an acknowledgment message, with which the server is informed to
     * send next packets.
     * 
     * @param type
     *            Frame type.
     * @return Acknowledgment message as byte array.
     */
    public static byte[] receiverReady(final GXDLMSSettings settings,
            final RequestTypes type) {
        if (type == RequestTypes.NONE) {
            throw new IllegalArgumentException(
                    "Invalid receiverReady RequestTypes parameter.");
        }
        // Get next frame.
        if ((type.getValue() & RequestTypes.FRAME.getValue()) != 0) {
            byte id = settings.getReceiverReady();
            return getHdlcFrame(settings, id, null);
        }
        short cmd;
        if (settings.getUseLogicalNameReferencing()) {
            if (settings.isServer()) {
                cmd = Command.GET_RESPONSE;
            } else {
                cmd = Command.GET_REQUEST;
            }
        } else {
            if (settings.isServer()) {
                cmd = Command.READ_RESPONSE;
            } else {
                cmd = Command.READ_REQUEST;
            }
        }
        // Get next block.
        List<byte[]> reply;
        GXByteBuffer bb = new GXByteBuffer(6);
        /*
         * if (settings.getNegotiatedConformance()
         * .contains(Conformance.GENERAL_BLOCK_TRANSFER)) { GXDLMSLNParameters p
         * = new GXDLMSLNParameters(settings, 0, Command.GENERAL_BLOCK_TRANSFER,
         * 0, bb, null, 0xff); p.windowSize = settings.getWindowSize();
         * p.blockNumberAck = settings.getBlockNumberAck();
         * p.setBlockIndex(settings.getBlockIndex()); p.streaming = false; reply
         * = GXDLMS.getLnMessages(p); settings.increaseBlockIndex(); } else
         */ {
            if (settings.getUseLogicalNameReferencing()) {
                bb.setUInt32(settings.getBlockIndex());
            } else {
                bb.setUInt16(settings.getBlockIndex());
            }
            settings.increaseBlockIndex();
            if (settings.getUseLogicalNameReferencing()) {
                GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0, cmd,
                        GetCommandType.NEXT_DATA_BLOCK, bb, null, 0xff);
                reply = getLnMessages(p);
            } else {
                GXDLMSSNParameters p = new GXDLMSSNParameters(settings, cmd, 1,
                        VariableAccessSpecification.BLOCK_NUMBER_ACCESS, bb,
                        null);
                reply = getSnMessages(p);
            }
        }
        return reply.get(0);
    }

    static String getDescription(final int errCode) {
        String str;
        switch (ErrorCode.forValue(errCode)) {
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
            str = "Access Error : "
                    + "Device reports a inconsistent Class or object.";
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
        return str;
    }

    /**
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

    static void appendData(final GXDLMSObject obj, final int index,
            final GXByteBuffer bb, final Object value) {

        DataType tp = obj.getDataType(index);
        if (tp == DataType.ARRAY) {
            if (value instanceof byte[]) {
                if (tp != DataType.OCTET_STRING) {
                    bb.set((byte[]) value);
                    return;
                }
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
                    GXCommon.setData(bb, tp, ((String) value).getBytes());
                    return;
                }
            }
        }
        GXCommon.setData(bb, tp, value);
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
    static void addLLCBytes(final GXDLMSSettings settings,
            final GXByteBuffer data) {
        byte[] tmp = data.array();
        data.clear();
        if (settings.isServer()) {
            data.set(GXCommon.LLC_REPLY_BYTES);
        } else {
            data.set(GXCommon.LLC_SEND_BYTES);
        }
        data.set(tmp);
    }

    /**
     * Check is all data fit to one data block.
     * 
     * @param p
     *            LN parameters.
     * @param reply
     *            Generated reply.
     */
    static void multipleBlocks(final GXDLMSLNParameters p,
            final GXByteBuffer reply, final boolean ciphering) {
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
        if (!p.isMultipleBlocks()) {
            // Add command type and invoke and priority.
            p.setMultipleBlocks(
                    2 + reply.size() + len > p.getSettings().getMaxPduSize());
        }
        if (p.isMultipleBlocks()) {
            // Add command type and invoke and priority.
            p.setLastBlock(!(8 + reply.size() + len > p.getSettings()
                    .getMaxPduSize()));
        }
        if (p.isLastBlock()) {
            // Add command type and invoke and priority.
            p.setLastBlock(!(8 + reply.size() + len > p.getSettings()
                    .getMaxPduSize()));
        }
    }

    /**
     * Get next logical name PDU.
     * 
     * @param p
     *            LN parameters.
     * @param reply
     *            Generated message.
     * @throws NoSuchAlgorithmExceptionp
     * @throws InvalidKeyException
     */
    public static void getLNPdu(final GXDLMSLNParameters p,
            final GXByteBuffer reply) {
        boolean ciphering = p.getCommand() != Command.AARQ
                && p.getCommand() != Command.AARE
                && p.getSettings().getCipher() != null
                && p.getSettings().getCipher().getSecurity() != null
                && p.getSettings().getCipher().getSecurity() != Security.NONE;
        int len = 0;
        if (p.getCommand() == Command.AARQ) {
            if (p.getSettings().getGateway() != null && p.getSettings()
                    .getGateway().getPhysicalDeviceAddress() != null) {
                reply.setUInt8(Command.GATEWAY_REQUEST);
                reply.setUInt8(p.getSettings().getGateway().getNetworkId());
                reply.setUInt8(p.getSettings().getGateway()
                        .getPhysicalDeviceAddress().length);
                reply.set(p.getSettings().getGateway()
                        .getPhysicalDeviceAddress());
            }
            reply.set(p.getAttributeDescriptor());
        } else {
            // Add command.
            if (p.getCommand() != Command.GENERAL_BLOCK_TRANSFER) {
                reply.setUInt8(p.getCommand());
            }
            if (p.getCommand() == Command.EVENT_NOTIFICATION
                    || p.getCommand() == Command.DATA_NOTIFICATION
                    || p.getCommand() == Command.ACCESS_REQUEST
                    || p.getCommand() == Command.ACCESS_RESPONSE) {
                // Add Long-Invoke-Id-And-Priority
                if (p.getCommand() != Command.EVENT_NOTIFICATION) {
                    if (p.getInvokeId() != 0) {
                        reply.setUInt32(p.getInvokeId());

                    } else {
                        reply.setUInt32(
                                getLongInvokeIDPriority(p.getSettings()));
                    }
                }
                // Add date time.
                if (p.getTime() == null) {
                    reply.setUInt8(DataType.NONE.getValue());
                } else {
                    // Data is send in octet string. Remove data type.
                    int pos = reply.size();
                    GXCommon.setData(reply, DataType.OCTET_STRING, p.getTime());
                    reply.move(pos + 1, pos, reply.size() - pos - 1);
                }
                multipleBlocks(p, reply, ciphering);
            } else if (p.getCommand() != Command.RELEASE_REQUEST) {
                // Get request size can be bigger than PDU size.
                if (p.getCommand() != Command.GET_REQUEST && p.getData() != null
                        && p.getData().size() != 0) {
                    multipleBlocks(p, reply, ciphering);
                }
                // Change Request type if Set request and multiple blocks is
                // needed.
                if (p.getCommand() == Command.SET_REQUEST) {
                    if (p.isMultipleBlocks() && !p.getSettings()
                            .getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                        if (p.getRequestType() == 1) {
                            p.setRequestType(2);
                        } else if (p.getRequestType() == 2) {
                            p.setRequestType(3);
                        }
                    }
                }
                // Change request type If get response and multiple blocks is
                // needed.
                if (p.getCommand() == Command.GET_RESPONSE) {
                    if (p.isMultipleBlocks() && !p.getSettings()
                            .getNegotiatedConformance()
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
                        reply.setUInt8(getInvokeIDPriority(p.getSettings()));
                    }
                }
            }
            // Add attribute descriptor.
            reply.set(p.getAttributeDescriptor());
            if (p.isMultipleBlocks()
                    && !p.getSettings().getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                // Is last block.
                if (p.isLastBlock()) {
                    reply.setUInt8(1);
                    p.getSettings().setCount(0);
                    p.getSettings().setIndex(0);
                } else {
                    reply.setUInt8(0);
                }
                // Block index.
                reply.setUInt32(p.getBlockIndex());
                p.setBlockIndex(p.getBlockIndex() + 1);
                // Add status if reply.
                if (p.getStatus() != 0xFF) {
                    if (p.getStatus() != 0
                            && p.getCommand() == Command.GET_RESPONSE) {
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
                GXCommon.setObjectCount(len, reply);
                reply.set(p.getData(), len);
            }
            // Add data that fits to one block.
            if (len == 0) {
                // Add status if reply.
                if (p.getStatus() != 0xFF
                        && p.getCommand() != Command.GENERAL_BLOCK_TRANSFER) {
                    if (p.getStatus() != 0
                            && p.getCommand() == Command.GET_RESPONSE) {
                        reply.setUInt8(1);
                    }
                    reply.setUInt8(p.getStatus());
                }
                if (p.getData() != null && p.getData().size() != 0) {
                    len = p.getData().size() - p.getData().position();
                    if (p.getSettings().getGateway() != null && p.getSettings()
                            .getGateway().getPhysicalDeviceAddress() != null) {

                        if (3 + len
                                + p.getSettings().getGateway()
                                        .getPhysicalDeviceAddress().length > p
                                                .getSettings()
                                                .getMaxPduSize()) {
                            len -= (3 + p.getSettings().getGateway()
                                    .getPhysicalDeviceAddress().length);
                        }
                        GXByteBuffer tmp = new GXByteBuffer(reply);
                        reply.size(0);
                        reply.setUInt8(Command.GATEWAY_REQUEST);
                        reply.setUInt8(
                                p.getSettings().getGateway().getNetworkId());
                        reply.setUInt8(p.getSettings().getGateway()
                                .getPhysicalDeviceAddress().length);
                        reply.set(p.getSettings().getGateway()
                                .getPhysicalDeviceAddress());
                        reply.set(tmp);
                    }
                    // Get request size can be bigger than PDU size.
                    if (p.getSettings().getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)) {
                        if (7 + len + reply.size() > p.getSettings()
                                .getMaxPduSize()) {
                            len = p.getSettings().getMaxPduSize() - reply.size()
                                    - 7;
                        }
                        // Cipher data only once.
                        if (ciphering
                                && p.command != Command.GENERAL_BLOCK_TRANSFER) {
                            reply.set(p.getData());
                            byte[] tmp;
                            if (p.getSettings().getCipher()
                                    .getSecuritySuite() == SecuritySuite.AES_GCM_128) {
                                tmp = cipher0(p, reply.array());
                            } else {
                                tmp = cipher1(p, reply.array());
                            }
                            p.getData().size(0);
                            p.getData().set(tmp);
                            reply.size(0);
                            len = p.getData().size();
                            if (7 + len > p.getSettings().getMaxPduSize()) {
                                len = p.getSettings().getMaxPduSize() - 7;
                            }
                        }
                    } else if (p.getCommand() != Command.GET_REQUEST && len
                            + reply.size() > p.getSettings().getMaxPduSize()) {
                        len = p.getSettings().getMaxPduSize() - reply.size();
                    }
                    reply.set(p.getData(), len);
                } else if ((p.getSettings().getGateway() != null
                        && p.getSettings().getGateway()
                                .getPhysicalDeviceAddress() != null)
                        && !(p.getCommand() == Command.GENERAL_BLOCK_TRANSFER
                                || (p.isMultipleBlocks() && (p.getSettings()
                                        .getNegotiatedConformance().contains(
                                                Conformance.GENERAL_BLOCK_TRANSFER))))) {
                    if (3 + len
                            + p.getSettings().getGateway()
                                    .getPhysicalDeviceAddress().length > p
                                            .getSettings().getMaxPduSize()) {
                        len -= (3 + p.getSettings().getGateway()
                                .getPhysicalDeviceAddress().length);
                    }
                    GXByteBuffer tmp = new GXByteBuffer(reply);
                    reply.size(0);
                    reply.setUInt8(Command.GATEWAY_REQUEST);
                    reply.setUInt8(p.getSettings().getGateway().getNetworkId());
                    reply.setUInt8(p.getSettings().getGateway()
                            .getPhysicalDeviceAddress().length);
                    reply.set(p.getSettings().getGateway()
                            .getPhysicalDeviceAddress());
                    reply.set(tmp);
                }
            }
            if (ciphering
                    && !p.getSettings().getNegotiatedConformance()
                            .contains(Conformance.GENERAL_BLOCK_TRANSFER)
                    && p.getCommand() != Command.RELEASE_REQUEST) {
                // GBT ciphering is done for all the data, not just block.
                byte[] tmp;
                if (p.getSettings().getCipher()
                        .getSecuritySuite() == SecuritySuite.AES_GCM_128) {
                    tmp = cipher0(p, reply.array());
                } else {
                    tmp = cipher1(p, reply.array());
                }
                reply.size(0);
                reply.set(tmp);
            }
        }
        if (p.getCommand() == Command.GENERAL_BLOCK_TRANSFER
                || (p.isMultipleBlocks()
                        && p.getSettings().getNegotiatedConformance().contains(
                                Conformance.GENERAL_BLOCK_TRANSFER))) {
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
            if (p.getCommand() != Command.DATA_NOTIFICATION
                    && p.blockNumberAck != 0) {
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

            if (p.getSettings().getGateway() != null && p.getSettings()
                    .getGateway().getPhysicalDeviceAddress() != null) {
                if (3 + len
                        + p.getSettings().getGateway()
                                .getPhysicalDeviceAddress().length > p
                                        .getSettings().getMaxPduSize()) {
                    len -= (3 + p.getSettings().getGateway()
                            .getPhysicalDeviceAddress().length);
                }
                GXByteBuffer tmp = new GXByteBuffer(reply);
                reply.size(0);
                reply.setUInt8(Command.GATEWAY_REQUEST);
                reply.setUInt8(p.getSettings().getGateway().getNetworkId());
                reply.setUInt8(p.getSettings().getGateway()
                        .getPhysicalDeviceAddress().length);
                reply.set(p.getSettings().getGateway()
                        .getPhysicalDeviceAddress());
                reply.set(tmp);
            }
        }
        if (p.getSettings().getInterfaceType() == InterfaceType.HDLC) {
            addLLCBytes(p.getSettings(), reply);
        }
    }

    /**
     * Cipher using security suite 1 or 2.
     * 
     * @param p
     *            LN settings.
     * @param reply
     *            Data to encrypt.
     */
    private static byte[] cipher1(final GXDLMSLNParameters p,
            final byte[] data) {
        byte keyid = 0;
        if (p.getSettings().getTargetEphemeralKey() != null) {
            keyid = 1;
        } else if (p.getSettings().getCipher()
                .getKeyAgreementKeyPair() != null) {
            keyid = 2;
        }
        // If connection is not establish yet.
        if (keyid == 0) {
            return data;
        }
        byte sc = 0;
        GXICipher c = p.getSettings().getCipher();
        if (c.getRecipientSystemTitle() == null) {
            throw new IllegalArgumentException(
                    "Invalid Recipient System Title.");
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
            throw new IllegalArgumentException("Invalid security.");
        }
        String alg, algID;
        int keyDataLen;
        switch (c.getSecuritySuite()) {
        case ECDH_ECDSA_AES_GCM_128_SHA_256:
            sc |= 1;
            alg = "SHA-256";
            // AES-GCM-128
            algID = "60857405080300";
            keyDataLen = 256;
            break;
        case ECDHE_CDSA_AES_GCM_256_SHA_384:
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
        GXByteBuffer transactionId = new GXByteBuffer();
        transactionId.setUInt64(c.getInvocationCounter());
        byte[] z = p.getSettings().getCipher().getSharedSecret();
        if (z == null) {
            if (keyid == 1) {
                z = GXCommon.getSharedSecret(c,
                        CertificateType.DIGITAL_SIGNATURE);
            } else {
                z = GXCommon.getSharedSecret(c, CertificateType.KEY_AGREEMENT);
                tmp2.setUInt8(0x8);
                tmp2.set(transactionId.getData(), 0, 8);
            }
        }
        tmp2.set(c.getRecipientSystemTitle());
        byte[] kdf = GXASymmetric.generateKDF(alg, z, keyDataLen,
                GXCommon.hexToBytes(algID), c.getSystemTitle(), tmp2.array(),
                null, null, c.getSecuritySuite());
        System.out.println("kdf: " + GXCommon.toHex(kdf));
        AesGcmParameter s = new AesGcmParameter(0xDD, security,
                c.getSecuritySuite(), c.getInvocationCounter(),
                // KDF
                kdf,
                // Authentication key.
                c.getAuthenticationKey(),
                // Originator system title.
                c.getSystemTitle(),
                // recipient system title.
                c.getRecipientSystemTitle(),
                // Date time
                null,
                // Other information.
                null);

        GXByteBuffer reply = new GXByteBuffer();
        reply.setUInt8(Command.GENERAL_CIPHERING);
        GXCommon.setObjectCount(transactionId.size(), reply);
        reply.set(transactionId);
        GXCommon.setObjectCount(s.getSystemTitle().length, reply);
        reply.set(s.getSystemTitle());
        GXCommon.setObjectCount(s.getRecipientSystemTitle().length, reply);
        reply.set(s.getRecipientSystemTitle());
        // date-time not present.
        reply.setUInt8(0);
        // other-information not present
        reply.setUInt8(0);
        // optional flag
        reply.setUInt8(1);
        // agreed-key CHOICE
        reply.setUInt8(2);
        // key-parameters
        reply.setUInt8(1);
        reply.setUInt8(keyid);
        if (keyid == 1) {
            try {
                // key-ciphered-data
                GXCommon.setObjectCount(0x80, reply);
                // Ephemeral public key client.
                reply.set(GXAsn1Converter
                        .toUIn64(c.getEphemeralKeyPair().getPublic()));

                // Ephemeral Public Key Signature.
                reply.set(GXASymmetric.getEphemeralPublicKeySignature(keyid,
                        c.getEphemeralKeyPair().getPublic(), p.getSettings()
                                .getCipher().getSigningKeyPair().getPrivate()));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            reply.setUInt8(0);
        }
        // ciphered-content
        s.setType(CountType.DATA | CountType.TAG);
        byte[] tmp = GXCiphering.encrypt(s, data);
        // Len
        GXCommon.setObjectCount(5 + tmp.length, reply);
        // Add SC
        reply.setUInt8(sc);
        // Add IC.
        reply.setUInt32(0);
        reply.set(tmp);
        return reply.array();
    }

    /**
     * Cipher using security suite 0.
     * 
     * @param p
     *            LN settings.
     * @param reply
     *            Data to encrypt.
     */
    static byte[] cipher0(final GXDLMSLNParameters p, final byte[] data) {
        int cmd;
        byte[] key;
        GXICipher cipher = p.getSettings().getCipher();
        if (!p.getSettings().getNegotiatedConformance()
                .contains(Conformance.GENERAL_PROTECTION)) {
            if ((p.getSettings().getConnected() & ConnectionState.DLMS) != 0
                    && cipher.getDedicatedKey() != null) {
                cmd = getDedMessage(p.command);
                key = cipher.getDedicatedKey();
            } else {
                cmd = getGloMessage(p.getCommand());
                key = cipher.getBlockCipherKey();
            }
        } else {
            if (cipher.getDedicatedKey() != null) {
                cmd = Command.GENERAL_DED_CIPHERING;
                key = cipher.getDedicatedKey();
            } else {
                cmd = Command.GENERAL_GLO_CIPHERING;
                key = cipher.getBlockCipherKey();
            }
        }
        AesGcmParameter s = new AesGcmParameter(cmd, cipher.getSecurity(),
                cipher.getInvocationCounter(), cipher.getSystemTitle(), key,
                cipher.getAuthenticationKey());
        byte[] tmp = GXCiphering.encrypt(s, data);
        if (p.getCommand() == Command.DATA_NOTIFICATION
                || p.getCommand() == Command.GENERAL_GLO_CIPHERING
                || p.getCommand() == Command.GENERAL_DED_CIPHERING) {
            GXByteBuffer reply = new GXByteBuffer();
            // Add command.
            reply.setUInt8(tmp[0]);
            // Add system title.
            GXCommon.setObjectCount(
                    p.getSettings().getCipher().getSystemTitle().length, reply);
            reply.set(p.getSettings().getCipher().getSystemTitle());
            // Add data.
            reply.set(tmp, 1, tmp.length - 1);
            return reply.array();
        }
        return tmp;
    }

    /**
     * Get all Logical name messages. Client uses this to generate messages.
     * 
     * @param p
     *            LN settings.
     * @return Generated messages.
     */
    public static List<byte[]> getLnMessages(final GXDLMSLNParameters p) {
        GXByteBuffer reply = new GXByteBuffer();
        java.util.ArrayList<byte[]> messages = new ArrayList<byte[]>();
        byte frame = 0;
        if (p.getCommand() == Command.AARQ) {
            frame = 0x10;
        } else if (p.getCommand() == Command.DATA_NOTIFICATION
                || p.getCommand() == Command.EVENT_NOTIFICATION) {
            frame = 0x13;
        }
        do {
            getLNPdu(p, reply);
            p.setLastBlock(true);
            if (p.getAttributeDescriptor() == null) {
                p.getSettings().increaseBlockIndex();
            }
            if (p.getCommand() == Command.AARQ
                    && p.getCommand() == Command.GET_REQUEST) {
                assert (!(p.getSettings().getMaxPduSize() < reply.size()));
            }
            while (reply.position() != reply.size()) {
                if (p.getSettings()
                        .getInterfaceType() == InterfaceType.WRAPPER) {
                    messages.add(getWrapperFrame(p.getSettings(), reply));
                } else if (p.getSettings()
                        .getInterfaceType() == InterfaceType.HDLC) {
                    messages.add(
                            GXDLMS.getHdlcFrame(p.getSettings(), frame, reply));
                    if (reply.position() != reply.size()) {
                        frame = p.getSettings().getNextSend(false);
                    }
                } else if (p.getSettings()
                        .getInterfaceType() == InterfaceType.PDU) {
                    messages.add(reply.array());
                    frame = 0;
                    break;
                } else {
                    throw new IllegalArgumentException("InterfaceType");
                }
            }
            reply.clear();
        } while (p.getData() != null
                && p.getData().position() != p.getData().size());
        return messages;
    }

    /**
     * Get all Short Name messages. Client uses this to generate messages.
     * 
     * @param p
     *            DLMS SN parameters.
     * @return Generated SN messages.
     */
    public static List<byte[]> getSnMessages(final GXDLMSSNParameters p) {
        GXByteBuffer reply = new GXByteBuffer();
        java.util.ArrayList<byte[]> messages =
                new java.util.ArrayList<byte[]>();
        byte frame = 0x0;
        if (p.getCommand() == Command.AARQ) {
            frame = 0x10;
        } else if (p.getCommand() == Command.INFORMATION_REPORT) {
            frame = 0x13;
        } else if (p.getCommand() == Command.NONE) {
            frame = p.getSettings().getNextSend(true);
        }
        do {
            getSNPdu(p, reply);
            if (p.getCommand() != Command.AARQ
                    && p.getCommand() != Command.AARE) {
                assert !(p.getSettings().getMaxPduSize() < reply.size());
            }
            // Command is not add to next PDUs.
            while (reply.position() != reply.size()) {
                if (p.getSettings()
                        .getInterfaceType() == InterfaceType.WRAPPER) {
                    messages.add(getWrapperFrame(p.getSettings(), reply));
                } else if (p.getSettings()
                        .getInterfaceType() == InterfaceType.HDLC) {
                    messages.add(getHdlcFrame(p.getSettings(), frame, reply));
                    if (reply.position() != reply.size()) {
                        if (p.getSettings().isServer()) {
                            frame = 0;
                        } else {
                            frame = p.getSettings().getNextSend(false);
                        }
                    }
                } else if (p.getSettings()
                        .getInterfaceType() == InterfaceType.PDU) {
                    messages.add(reply.array());
                    break;
                } else {
                    throw new IllegalArgumentException("InterfaceType");
                }
            }
            reply.clear();
        } while (p.getData() != null
                && p.getData().position() != p.getData().size());
        return messages;

    }

    private static int appendMultipleSNBlocks(final GXDLMSSNParameters p,
            final GXByteBuffer reply) {
        boolean ciphering = p.getSettings().getCipher() != null
                && p.getSettings().getCipher().getSecurity() != Security.NONE;
        int hSize = reply.size() + 3;
        // Add LLC bytes.
        if (p.getCommand() == Command.WRITE_REQUEST
                || p.getCommand() == Command.READ_REQUEST) {
            hSize += 1 + GXCommon.getObjectCountSizeInBytes(p.getCount());
        }
        int maxSize = p.getSettings().getMaxPduSize() - hSize;
        if (ciphering) {
            maxSize -= CIPHERING_HEADER_SIZE;
            if (p.getSettings().getInterfaceType() == InterfaceType.HDLC) {
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

    /**
     * @param p
     * @param reply
     */
    public static void getSNPdu(final GXDLMSSNParameters p,
            final GXByteBuffer reply) {
        boolean ciphering = p.getSettings().getCipher() != null
                && p.getSettings().getCipher().getSecurity() != Security.NONE;
        if ((!ciphering || p.getCommand() == Command.AARQ
                || p.getCommand() == Command.AARE)
                && p.getSettings().getInterfaceType() == InterfaceType.HDLC) {
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
                GXCommon.setData(reply, DataType.OCTET_STRING, p.getTime());
                reply.move(pos + 1, pos, reply.size() - pos - 1);
            }
            GXCommon.setObjectCount(p.getCount(), reply);
            reply.set(p.getAttributeDescriptor());
        } else if (p.getCommand() != Command.AARQ
                && p.getCommand() != Command.AARE) {
            reply.setUInt8(p.getCommand());
            if (p.getCount() != 0xFF) {
                GXCommon.setObjectCount(p.getCount(), reply);
            }
            if (p.getRequestType() != 0xFF) {
                reply.setUInt8(p.getRequestType());
            }
            reply.set(p.getAttributeDescriptor());

            if (!p.isMultipleBlocks()) {
                p.setMultipleBlocks(reply.size() + cipherSize + cnt > p
                        .getSettings().getMaxPduSize());
                // If reply data is not fit to one PDU.
                if (p.isMultipleBlocks()) {
                    reply.size(0);
                    if (!ciphering && p.getSettings()
                            .getInterfaceType() == InterfaceType.HDLC) {
                        if (p.getSettings().isServer()) {
                            reply.set(GXCommon.LLC_REPLY_BYTES);
                        } else if (reply.size() == 0) {
                            reply.set(GXCommon.LLC_SEND_BYTES);
                        }
                    }
                    // CHECKSTYLE:OFF
                    if (p.getCommand() == Command.WRITE_REQUEST) {
                        p.setRequestType(
                                VariableAccessSpecification.WRITE_DATA_BLOCK_ACCESS);
                    } else if (p.getCommand() == Command.READ_REQUEST) {
                        p.setRequestType(
                                VariableAccessSpecification.READ_DATA_BLOCK_ACCESS);
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
        if (p.getData() != null
                && p.getData().position() == p.getData().size()) {
            p.getSettings().setIndex(0);
            p.getSettings().setCount(0);
        }
        // If Ciphering is used.
        if (ciphering && p.getCommand() != Command.AARQ
                && p.getCommand() != Command.AARE) {
            GXICipher cipher = p.getSettings().getCipher();
            AesGcmParameter s = new AesGcmParameter(
                    getGloMessage(p.getCommand()), cipher.getSecurity(),
                    cipher.getInvocationCounter(), cipher.getSystemTitle(),
                    cipher.getBlockCipherKey(), cipher.getAuthenticationKey());
            byte[] tmp = GXCiphering.encrypt(s, reply.array());
            assert !(p.getSettings().getMaxPduSize() < tmp.length);
            reply.size(0);
            if (p.getSettings().getInterfaceType() == InterfaceType.HDLC) {
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
            return new Byte((byte) (value << 1 | 1));
        }
        if (size < 4 && value < 0x4000) {
            return new Short(
                    (short) ((value & 0x3F80) << 2 | (value & 0x7F) << 1 | 1));
        }
        if (value < 0x10000000) {
            return new Integer(
                    (int) ((value & 0xFE00000) << 4 | (value & 0x1FC000) << 3
                            | (value & 0x3F80) << 2 | (value & 0x7F) << 1 | 1));
        }
        throw new IllegalArgumentException("Invalid address.");
    }

    /**
     * @param value
     * @param bb
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
     * @param data
     *            Wrapped data.
     * @return Wrapper frames.
     */
    static byte[] getWrapperFrame(final GXDLMSSettings settings,
            final GXByteBuffer data) {
        GXByteBuffer bb = new GXByteBuffer();
        // Add version.
        bb.setUInt16(1);
        if (settings.isServer()) {
            bb.setUInt16(settings.getServerAddress());
            bb.setUInt16(settings.getClientAddress());
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
    static byte[] getHdlcFrame(final GXDLMSSettings settings, final int frame,
            final GXByteBuffer data) {
        GXByteBuffer bb = new GXByteBuffer();
        int frameSize, len = 0;
        byte[] primaryAddress, secondaryAddress;
        if (settings.isServer()) {
            primaryAddress = getAddressBytes(settings.getClientAddress(), 0);
            secondaryAddress = getAddressBytes(settings.getServerAddress(),
                    settings.getServerAddressSize());
        } else {
            primaryAddress = getAddressBytes(settings.getServerAddress(),
                    settings.getServerAddressSize());
            secondaryAddress = getAddressBytes(settings.getClientAddress(), 0);
        }
        // Add BOP
        bb.setUInt8(GXCommon.HDLC_FRAME_START_END);
        frameSize = settings.getLimits().getMaxInfoTX();
        if (data != null && data.position() == 0) {
            frameSize -= 3;
        }
        // If no data
        if (data == null || data.size() == 0) {
            len = 0;
            bb.setUInt8(0xA0);
        } else if (data.size() - data.position() <= frameSize) {
            len = data.size() - data.position();
            // Is last packet.
            bb.setUInt8(0xA0 | (((7 + primaryAddress.length
                    + secondaryAddress.length + len) >> 8) & 0x7));
        } else {
            len = frameSize;
            // More data to left.
            bb.setUInt8(0xA8 | ((len >> 8) & 0x7));
        }
        // Frame len.
        if (len == 0) {
            bb.setUInt8((byte) (5 + primaryAddress.length
                    + secondaryAddress.length + len));
        } else {
            bb.setUInt8((byte) (7 + primaryAddress.length
                    + secondaryAddress.length + len));
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
        bb.setUInt8(GXCommon.HDLC_FRAME_START_END);
        // Remove sent data in server side.
        if (settings.isServer()) {
            if (data != null) {
                if (data.size() == data.position()) {
                    data.clear();
                } else {
                    data.move(data.position(), 0,
                            data.size() - data.position());
                    data.position(0);
                }
            }
        }
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
    private static boolean getLLCBytes(final boolean server,
            final GXByteBuffer data) {
        if (server) {
            return data.compare(GXCommon.LLC_SEND_BYTES);
        } else {
            return data.compare(GXCommon.LLC_REPLY_BYTES);
        }
    }

    static short getHdlcData(final boolean server,
            final GXDLMSSettings settings, final GXByteBuffer reply,
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
            if (ch == GXCommon.HDLC_FRAME_START_END) {
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
        if (ch != GXCommon.HDLC_FRAME_START_END) {
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
            if (!(reply.position() < reply.size()
                    && reply.getUInt8(reply.position()) == 0x13)) {
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
        if ((frame & 0x8) != 0) {
            if (isNotify) {
                notify.setMoreData(
                        RequestTypes.forValue(notify.getMoreData().getValue()
                                | RequestTypes.FRAME.getValue()));
            } else {
                data.setMoreData(
                        RequestTypes.forValue(data.getMoreData().getValue()
                                | RequestTypes.FRAME.getValue()));
            }
        } else {
            if (isNotify) {
                notify.setMoreData(
                        RequestTypes.forValue(notify.getMoreData().getValue()
                                & ~RequestTypes.FRAME.getValue()));
            } else {
                data.setMoreData(
                        RequestTypes.forValue(data.getMoreData().getValue()
                                & ~RequestTypes.FRAME.getValue()));
            }
        }
        // Get frame type.
        frame = reply.getUInt8();
        if (data.getXml() == null && !settings.checkFrame(frame)) {
            reply.position(eopPos + 1);
            return getHdlcData(server, settings, reply, data, notify);
        }
        // Check that header CRC is correct.
        crc = GXFCS16.countFCS16(reply.getData(), packetStartID + 1,
                reply.position() - packetStartID - 1);
        crcRead = reply.getUInt16();
        if (crc != crcRead) {
            if (reply.size() - reply.position() > 8) {
                return getHdlcData(server, settings, reply, data, notify);
            }
            throw new GXDLMSException("Wrong CRC.");
        }
        // Check that packet CRC match only if there is a data part.
        if (reply.position() != packetStartID + frameLen + 1) {
            crc = GXFCS16.countFCS16(reply.getData(), packetStartID + 1,
                    frameLen - 2);
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

        if (frame != 0x13 && (frame
                & HdlcFrameType.U_FRAME.getValue()) == HdlcFrameType.U_FRAME
                        .getValue()) {
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
        } else if (frame != 0x13 && (frame
                & HdlcFrameType.S_FRAME.getValue()) == HdlcFrameType.S_FRAME
                        .getValue()) {
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
                    data.setMoreData(RequestTypes.FRAME);
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
    private static void getServerAddress(final int address, final int[] logical,
            final int[] physical) {
        if (address < 0x4000) {
            logical[0] = address >>> 7;
            physical[0] = address & 0x7F;
        } else {
            logical[0] = address >>> 14;
            physical[0] = address & 0x3FFF;
        }
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
    private static boolean checkHdlcAddress(final boolean server,
            final GXDLMSSettings settings, final GXByteBuffer reply,
            final int index, final int addresses[]) {
        int source, target;
        // Get destination and source addresses.
        target = GXCommon.getHDLCAddress(reply);
        source = GXCommon.getHDLCAddress(reply);
        addresses[0] = source;
        addresses[1] = target;
        if (server) {
            // Check that server addresses match.
            if (settings.getServerAddress() != 0
                    && settings.getServerAddress() != target) {
                if (reply.getUInt8(reply.position()) == Command.SNRM) {
                    settings.setServerAddress(target);
                } else {
                    throw new GXDLMSException(
                            "Server addresses do not match. It is "
                                    + String.valueOf(target) + ". It should be "
                                    + String.valueOf(
                                            settings.getServerAddress())
                                    + ".");
                }
            } else {
                settings.setServerAddress(target);
            }

            // Check that client addresses match.
            if (settings.getClientAddress() != 0
                    && settings.getClientAddress() != source) {
                if (reply.getUInt8(reply.position()) == Command.SNRM) {
                    settings.setClientAddress(source);
                } else {
                    throw new GXDLMSException(
                            "Client addresses do not match. It is "
                                    + String.valueOf(source) + ". It should be "
                                    + String.valueOf(
                                            settings.getClientAddress())
                                    + ".");
                }
            } else {
                settings.setClientAddress(source);
            }
        } else {
            // Check that client addresses match.
            if (settings.getClientAddress() != target) {
                // If echo.
                if (settings.getClientAddress() == source
                        && settings.getServerAddress() == target) {
                    reply.position(index + 1);
                    return false;
                }
                throw new GXDLMSException(
                        "Destination addresses do not match. It is "
                                + String.valueOf(target) + ". It should be "
                                + String.valueOf(settings.getClientAddress())
                                + ".");
            }
            // Check that server addresses match.
            if (settings.getServerAddress() != source) {
                // Check logical and physical address separately.
                // This is done because some meters might send four bytes
                // when only two bytes is needed.
                int[] readLogical = new int[1], readPhysical = new int[1],
                        logical = new int[1], physical = new int[1];
                getServerAddress(source, readLogical, readPhysical);
                getServerAddress(settings.getServerAddress(), logical,
                        physical);
                if (readLogical[0] != logical[0]
                        || readPhysical[0] != physical[0]) {
                    throw new GXDLMSException(
                            "Source addresses do not match. It is "
                                    + String.valueOf(source) + ". It should be "
                                    + String.valueOf(
                                            settings.getServerAddress())
                                    + ".");
                }
            }
        }
        return true;
    }

    /**
     * Get data from TCP/IP frame.
     * 
     * @param settings
     *            DLMS settigns.
     * @param buff
     *            Received data.
     * @param target
     *            Reply information.
     */
    static boolean getTcpData(final GXDLMSSettings settings,
            final GXByteBuffer buff, final GXReplyData data,
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
        while (buff.position() < buff.size() - 1) {
            // Get version
            value = buff.getUInt16();
            if (value == 1) {
                // Check TCP/IP addresses.
                if (!checkWrapperAddress(settings, buff, target)) {
                    target = notify;
                    isData = false;
                }
                // Get length.
                value = buff.getUInt16();
                boolean compleate = !((buff.size() - buff.position()) < value);
                if (compleate && (buff.size() - buff.position()) != value) {
                    System.out.println("Data length is " + String.valueOf(value)
                            + "and there are "
                            + String.valueOf(buff.size() - buff.position())
                            + " bytes.");
                }
                target.setComplete(compleate);
                if (!compleate) {
                    buff.position(pos);
                } else {
                    target.setPacketLength(buff.position() + value);
                }
            } else {
                buff.position(buff.position() - 1);
            }
            break;
        }
        return isData;
    }

    /**
     * Encrypt Flag name to two bytes.
     * 
     * @param flagName
     *            3 letter Flag name.
     * @return Encrypted Flag name.
     */
    private static int encryptManufacturer(final String flagName) {
        if (flagName.length() != 3) {
            throw new IllegalArgumentException("Invalid Flag name.");
        }
        int value = ((flagName.charAt(0) - 0x40) & 0x1f);
        value <<= 5;
        value += ((flagName.charAt(0) - 0x40) & 0x1f);
        value <<= 5;
        value += ((flagName.charAt(0) - 0x40) & 0x1f);
        return value;
    }

    /**
     * Decrypt two bytes to Flag name.
     * 
     * @param value
     *            Encrypted Flag value.
     * @return Flag name.
     */
    private static String decryptManufacturer(final int value) {
        int tmp = (value >> 8 | value << 8);
        char c = (char) ((tmp & 0x1f) + 0x40);
        tmp = (tmp >> 5);
        char c1 = (char) ((tmp & 0x1f) + 0x40);
        tmp = (tmp >> 5);
        char c2 = (char) ((tmp & 0x1f) + 0x40);
        return new String(new char[] { c2, c1, c });
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
    static void getMBusData(final GXDLMSSettings settings,
            final GXByteBuffer buff, final GXReplyData data) {
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
            String man = decryptManufacturer(manufacturerID);
            // A-Field.
            long id = buff.getUInt32();
            short meterVersion = buff.getUInt8();
            MBusMeterType type = MBusMeterType.forValue(buff.getUInt8());
            // CI-Field
            MBusControlInfo ci = MBusControlInfo.forValue(buff.getUInt8());
            // Access number.
            short frameId = buff.getUInt8();
            // State of the meter
            short state = buff.getUInt8();
            // Configuration word.
            int configurationWord = buff.getUInt16();
            // byte encryptedBlocks = (byte) (configurationWord >> 12);
            MBusEncryptionMode encryption =
                    MBusEncryptionMode.forValue(configurationWord & 7);
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
     * Check is this M-Bus message.
     * 
     * @param buff
     *            Received data.
     * @return True, if this is M-Bus message.
     */
    static boolean isMBusData(final GXByteBuffer buff) {
        if (buff.size() - buff.position() < 2) {
            return false;
        }
        MBusCommand cmd =
                MBusCommand.forValue(buff.getUInt8(buff.position() + 1));
        if (!(cmd == MBusCommand.SND_NR || cmd == MBusCommand.SND_UD2
                || cmd == MBusCommand.RSP_UD)) {
            return false;
        }
        return true;
    }

    private static boolean checkWrapperAddress(final GXDLMSSettings settings,
            final GXByteBuffer buff, final GXReplyData notify) {
        boolean ret = true;
        int value;
        if (settings.isServer()) {
            value = buff.getUInt16();
            // Check that client addresses match.
            if (settings.getClientAddress() != 0
                    && settings.getClientAddress() != value) {
                throw new GXDLMSException(
                        "Source addresses do not match. It is "
                                + String.valueOf(value) + ". It should be "
                                + String.valueOf(settings.getClientAddress())
                                + ".");
            } else {
                settings.setClientAddress(value);
            }

            value = buff.getUInt16();
            // Check that server addresses match.
            if (settings.getServerAddress() != 0
                    && settings.getServerAddress() != value) {
                throw new GXDLMSException(
                        "Destination addresses do not match. It is "
                                + String.valueOf(value) + ". It should be "
                                + String.valueOf(settings.getServerAddress())
                                + ".");
            } else {
                settings.setServerAddress(value);
            }
        } else {
            value = buff.getUInt16();
            // Check that server addresses match.
            if (settings.getClientAddress() != 0
                    && settings.getServerAddress() != value) {
                if (notify == null) {
                    throw new GXDLMSException(
                            "Source addresses do not match. It is "
                                    + String.valueOf(value) + ". It should be "
                                    + String.valueOf(
                                            settings.getServerAddress())
                                    + ".");
                }
                notify.setServerAddress(value);
                ret = false;
            } else {
                settings.setServerAddress(value);
            }

            value = buff.getUInt16();
            // Check that client addresses match.
            if (settings.getClientAddress() != 0
                    && settings.getClientAddress() != value) {
                if (notify == null) {
                    throw new GXDLMSException(
                            "Destination addresses do not match. It is "
                                    + String.valueOf(value) + ". It should be "
                                    + String.valueOf(
                                            settings.getClientAddress())
                                    + ".");
                }
                ret = false;
                notify.setClientAddress(value);
            } else {
                settings.setClientAddress(value);
            }
        }
        return ret;
    }

    /**
     * Handle read response data block result.
     * 
     * @param settings
     *            DLMS settings.
     * @param reply
     *            Received reply.
     * @param index
     *            Starting index.
     */
    static boolean readResponseDataBlockResult(final GXDLMSSettings settings,
            final GXReplyData reply, final int index) {
        reply.setError(0);
        short lastBlock = reply.getData().getUInt8();
        // Get Block number.
        int number = reply.getData().getUInt16();
        int blockLength = GXCommon.getObjectCount(reply.getData());
        // Is not Last block.
        if (lastBlock == 0) {
            reply.setMoreData(
                    RequestTypes.forValue(reply.getMoreData().getValue()
                            | RequestTypes.DATABLOCK.getValue()));
        } else {
            reply.setMoreData(
                    RequestTypes.forValue(reply.getMoreData().getValue()
                            & ~RequestTypes.DATABLOCK.getValue()));
        }
        // If meter's block index is zero based.
        if (number != 1 && settings.getBlockIndex() == 1) {
            settings.setBlockIndex(number);
        }
        int expectedIndex = settings.getBlockIndex();
        if (number != expectedIndex) {
            throw new GXDLMSException("Invalid Block number. It is " + number
                    + " and it should be " + expectedIndex + ".");
        }
        // If whole block is not read.
        if ((reply.getMoreData().getValue()
                & RequestTypes.FRAME.getValue()) != 0) {
            getDataFromBlock(reply.getData(), index);
            return false;
        }
        if (blockLength != reply.getData().size()
                - reply.getData().position()) {
            throw new IllegalArgumentException("Invalid block length.");
        }
        reply.setCommand(Command.NONE);
        if (reply.getXml() != null) {
            reply.getData().trim();
            reply.getXml().appendStartTag(Command.READ_RESPONSE,
                    SingleReadResponse.DATA_BLOCK_RESULT);
            reply.getXml().appendLine(TranslatorTags.LAST_BLOCK, "Value",
                    reply.getXml().integerToHex(lastBlock, 2));
            reply.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value",
                    reply.getXml().integerToHex(number, 4));
            reply.getXml().appendLine(TranslatorTags.RAW_DATA, "Value",
                    GXCommon.toHex(reply.getData().getData(), false, 0,
                            reply.getData().size()));
            reply.getXml().appendEndTag(Command.READ_RESPONSE,
                    SingleReadResponse.DATA_BLOCK_RESULT);
            return false;
        }
        getDataFromBlock(reply.getData(), index);
        reply.setTotalCount(0);
        // If last packet and data is not try to peek.
        if (reply.getMoreData() == RequestTypes.NONE) {
            settings.resetBlockIndex();
        }
        return true;
    }

    /**
     * Handle read response and get data from block and/or update error status.
     * 
     * @param data
     *            Received data from the client.
     */
    static boolean handleReadResponse(final GXDLMSSettings settings,
            final GXReplyData reply, final int index) {
        int pos, cnt = reply.getTotalCount();
        // If we are reading value first time or block is handed.
        boolean first = reply.getTotalCount() == 0 || reply
                .getCommandType() == SingleReadResponse.DATA_BLOCK_RESULT;
        if (first) {
            cnt = GXCommon.getObjectCount(reply.getData());
            reply.setTotalCount(cnt);
        }
        int type;
        List<Object> values = null;
        if (cnt != 1) {
            values = new ArrayList<Object>();
            if (reply.getValue() instanceof Object[]) {
                values.addAll(Arrays.asList((Object[]) reply.getValue()));
            }
            reply.setValue(null);
        }

        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.READ_RESPONSE, "Qty",
                    reply.getXml().integerToHex(cnt, 2));
        }
        for (pos = 0; pos != cnt; ++pos) {
            if (reply.getData().available() == 0) {
                if (cnt != 1) {
                    getDataFromBlock(reply.getData(), 0);
                    reply.setValue(values.toArray());
                    reply.setReadPosition(reply.getData().position());
                }
                return false;
            }
            // Get status code. Status code is begin of each PDU.
            if (first) {
                type = reply.getData().getUInt8();
                reply.setCommandType(type);
            } else {
                type = reply.getCommandType();
            }

            boolean standardXml = reply.getXml() != null && reply.getXml()
                    .getOutputType() == TranslatorOutputType.STANDARD_XML;
            switch (type) {
            case SingleReadResponse.DATA:
                reply.setError(0);
                if (reply.getXml() != null) {
                    if (standardXml) {
                        reply.getXml().appendStartTag(TranslatorTags.CHOICE);
                    }
                    reply.getXml().appendStartTag(Command.READ_RESPONSE,
                            SingleReadResponse.DATA);
                    GXDataInfo di = new GXDataInfo();
                    di.setXml(reply.getXml());
                    GXCommon.getData(reply.getData(), di);
                    reply.getXml().appendEndTag(Command.READ_RESPONSE,
                            SingleReadResponse.DATA);
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
                    if (reply.getData().position() == reply.getReadPosition()) {
                        int index2 = index;
                        // If multiple values remove command.
                        if (cnt != 1 && reply.getTotalCount() == 0) {
                            ++index2;
                        }
                        reply.setTotalCount(0);
                        reply.getData().position(index2);
                        getDataFromBlock(reply.getData(), 0);
                        reply.setValue(null);
                        // Ask that data is parsed after last block is received.
                        reply.setCommandType(
                                SingleReadResponse.DATA_BLOCK_RESULT);
                        return false;
                    }
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
                    reply.getXml().appendLine(
                            Command.READ_RESPONSE << 8
                                    | SingleReadResponse.DATA_ACCESS_ERROR,
                            null,
                            GXDLMSTranslator.errorCodeToString(
                                    reply.getXml().getOutputType(),
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
                    throw new GXDLMSException("Invalid Block number. It is "
                            + number + " and it should be "
                            + settings.getBlockIndex() + ".");
                }
                settings.increaseBlockIndex();
                reply.setMoreData(
                        RequestTypes.forValue(reply.getMoreData().getValue()
                                | RequestTypes.DATABLOCK.getValue()));
                break;
            default:
                throw new GXDLMSException(
                        "HandleReadResponse failed. Invalid tag.");
            }
        }
        if (reply.getXml() != null) {
            reply.getXml().appendEndTag(Command.READ_RESPONSE);
            return true;
        }
        if (values != null) {
            reply.setValue(values.toArray(new Object[values.size()]));
        }
        return cnt == 1;
    }

    /**
     * Handle method response and get data from block and/or update error
     * status.
     * 
     * @param data
     *            Received data from the client.
     */
    static void handleMethodResponse(final GXDLMSSettings settings,
            final GXReplyData data) {

        // Get type.
        byte type = (byte) data.getData().getUInt8();
        // Get invoke ID and priority.
        short invoke = data.getData().getUInt8();
        if (data.getXml() != null) {
            data.getXml().appendStartTag(Command.METHOD_RESPONSE);
            data.getXml().appendStartTag(Command.METHOD_RESPONSE, type);
            // InvokeIdAndPriority
            data.getXml().appendLine(TranslatorTags.INVOKE_ID, "Value",
                    data.getXml().integerToHex(invoke, 2));
        }
        boolean standardXml = data.getXml() != null && data.getXml()
                .getOutputType() == TranslatorOutputType.STANDARD_XML;
        if (type == 1) {
            // Get Action-Result
            short ret = data.getData().getUInt8();
            if (ret != 0) {
                data.setError(ret);
            }
            if (data.getXml() != null) {
                if (standardXml) {
                    data.getXml()
                            .appendStartTag(TranslatorTags.SINGLE_RESPONSE);
                }
                data.getXml().appendLine(TranslatorTags.RESULT, null,
                        GXDLMSTranslator.errorCodeToString(
                                data.getXml().getOutputType(),
                                ErrorCode.forValue(data.getError())));
            }
            // Action-Response-Normal. Get data if exists. All meters do not
            // return data here.
            if (data.getData().position() < data.getData().size()) {
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
                            data.getData()
                                    .position(data.getData().position() - 2);
                            getDataFromBlock(data.getData(), 0);
                            data.setError(0);
                            ret = 0;
                        }

                    } else {
                        getDataFromBlock(data.getData(), 0);
                    }
                } else {
                    throw new GXDLMSException(
                            "HandleActionResponseNormal failed. "
                                    + "Invalid tag.");
                }
                if (data.getXml() != null && (ret != 0
                        || data.getData().position() < data.getData().size())) {
                    data.getXml()
                            .appendStartTag(TranslatorTags.RETURN_PARAMETERS);
                    if (ret != 0) {
                        data.getXml().appendLine(
                                TranslatorTags.DATA_ACCESS_ERROR, null,
                                GXDLMSTranslator.errorCodeToString(
                                        data.getXml().getOutputType(),
                                        ErrorCode.forValue(data.getError())));

                    } else {
                        data.getXml().appendStartTag(Command.READ_RESPONSE,
                                SingleReadResponse.DATA);
                        GXDataInfo di = new GXDataInfo();
                        di.setXml(data.getXml());
                        GXCommon.getData(data.getData(), di);
                        data.getXml().appendEndTag(Command.READ_RESPONSE,
                                SingleReadResponse.DATA);
                    }
                    data.getXml()
                            .appendEndTag(TranslatorTags.RETURN_PARAMETERS);
                    if (standardXml) {
                        data.getXml()
                                .appendEndTag(TranslatorTags.SINGLE_RESPONSE);
                    }
                }
            }
        } else if (type == 2) {
            // Action-Response-With-Pblock
            throw new IllegalArgumentException("Invalid Command.");
        } else if (type == 3) {
            // Action-Response-With-List.
            throw new IllegalArgumentException("Invalid Command.");
        } else if (type == 4) {
            // Action-Response-Next-Pblock
            throw new IllegalArgumentException("Invalid Command.");
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
            reply.setMoreData(
                    RequestTypes.forValue(reply.getMoreData().getValue()
                            | RequestTypes.DATABLOCK.getValue()));
        } else {
            reply.setMoreData(
                    RequestTypes.forValue(reply.getMoreData().getValue()
                            & ~RequestTypes.DATABLOCK.getValue()));

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
            throw new RuntimeException("Invalid data.");
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

    private static void handleAccessResponse(final GXDLMSSettings settings,
            final GXReplyData reply) {
        // Get invoke id.
        long invokeId = reply.getData().getUInt32();
        int len = reply.getData().getUInt8();
        byte[] tmp = null;
        // If date time is given.
        if (len != 0) {
            tmp = new byte[len];
            reply.getData().get(tmp);
            reply.setTime(((GXDateTime) GXDLMSClient.changeType(tmp,
                    DataType.DATETIME)));
        }
        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.ACCESS_RESPONSE);
            reply.getXml().appendLine(TranslatorTags.LONG_INVOKE_ID, null,
                    reply.getXml().integerToHex(invokeId, 8));
            if (reply.getTime() != null) {
                reply.getXml().appendComment(reply.getTime().toString());
            }
            reply.getXml().appendLine(TranslatorTags.DATE_TIME, "Value",
                    GXCommon.toHex(tmp, false));

            // access-request-specification OPTIONAL
            reply.getData().getUInt8();
            len = GXCommon.getObjectCount(reply.getData());
            reply.getXml().appendStartTag(TranslatorTags.ACCESS_RESPONSE_BODY);
            reply.getXml().appendStartTag(
                    TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA, "Qty",
                    reply.getXml().integerToHex(len, 2));
            for (int pos = 0; pos != len; ++pos) {
                if (reply.getXml()
                        .getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    reply.getXml().appendStartTag(Command.WRITE_REQUEST,
                            SingleReadResponse.DATA);
                }
                GXDataInfo di = new GXDataInfo();
                di.setXml(reply.getXml());
                GXCommon.getData(reply.getData(), di);
                if (reply.getXml()
                        .getOutputType() == TranslatorOutputType.STANDARD_XML) {
                    reply.getXml().appendEndTag(Command.WRITE_REQUEST,
                            SingleReadResponse.DATA);
                }
            }
            reply.getXml()
                    .appendEndTag(TranslatorTags.ACCESS_RESPONSE_LIST_OF_DATA);
            // access-response-specification
            int err;
            len = GXCommon.getObjectCount(reply.getData());
            reply.getXml().appendStartTag(
                    TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION, "Qty",
                    reply.getXml().integerToHex(len, 2));
            for (int pos = 0; pos != len; ++pos) {
                byte type = (byte) reply.getData().getUInt8();
                err = reply.getData().getUInt8();
                if (err != 0) {
                    err = reply.getData().getUInt8();
                }
                reply.getXml().appendStartTag(
                        TranslatorTags.ACCESS_RESPONSE_SPECIFICATION);

                reply.getXml().appendStartTag(Command.ACCESS_RESPONSE, type);
                reply.getXml().appendLine(TranslatorTags.RESULT, null,
                        GXDLMSTranslator.errorCodeToString(
                                reply.getXml().getOutputType(),
                                ErrorCode.forValue(err)));
                reply.getXml().appendEndTag(Command.ACCESS_RESPONSE, type);
                reply.getXml().appendEndTag(
                        TranslatorTags.ACCESS_RESPONSE_SPECIFICATION);
                // <Result Value="Success" />
            }
            reply.getXml().appendEndTag(
                    TranslatorTags.LIST_OF_ACCESS_RESPONSE_SPECIFICATION);
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
    private static void handleDataNotification(final GXDLMSSettings settings,
            final GXReplyData reply) {
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
            Object ret = GXCommon.getData(new GXByteBuffer(tmp), info);
            reply.setTime(((GXDateTime) ret));
        }
        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.DATA_NOTIFICATION);
            reply.getXml().appendLine(TranslatorTags.LONG_INVOKE_ID, null,
                    reply.getXml().integerToHex(invokeId, 8));
            if (reply.getTime() != null) {
                reply.getXml().appendComment(String.valueOf(reply.getTime()));
            }
            reply.getXml().appendLine(TranslatorTags.DATE_TIME, null,
                    GXCommon.toHex(tmp, false));
            reply.getXml().appendStartTag(TranslatorTags.NOTIFICATION_BODY);
            reply.getXml().appendStartTag(TranslatorTags.DATA_VALUE);
            GXDataInfo di = new GXDataInfo();
            di.setXml(reply.getXml());
            GXCommon.getData(reply.getData(), di);
            reply.getXml().appendEndTag(TranslatorTags.DATA_VALUE);
            reply.getXml().appendEndTag(TranslatorTags.NOTIFICATION_BODY);
            reply.getXml().appendEndTag(Command.DATA_NOTIFICATION);
        } else {
            getDataFromBlock(reply.getData(), start);
            getValueFromData(settings, reply);
        }
    }

    /**
     * Handle set response and update error status.
     * 
     * @param reply
     *            Received data from the client.
     */
    static void handleSetResponse(final GXDLMSSettings settings,
            final GXReplyData data) {
        byte type = (byte) data.getData().getUInt8();
        // Invoke ID and priority.
        short invokeId = data.getData().getUInt8();
        if (data.getXml() != null) {
            data.getXml().appendStartTag(Command.SET_RESPONSE);
            data.getXml().appendStartTag(Command.SET_RESPONSE, type);
            // InvokeIdAndPriority
            data.getXml().appendLine(TranslatorTags.INVOKE_ID, "Value",
                    data.getXml().integerToHex(invokeId, 2));
        }

        // SetResponseNormal
        if (type == SetResponseType.NORMAL) {
            data.setError(data.getData().getUInt8());
            if (data.getXml() != null) {
                // Result start tag.
                data.getXml().appendLine(TranslatorTags.RESULT, "Value",
                        GXDLMSTranslator.errorCodeToString(
                                data.getXml().getOutputType(),
                                ErrorCode.forValue(data.getError())));
            }
        } else if (type == SetResponseType.DATA_BLOCK) {
            long number = data.getData().getUInt32();
            if (data.getXml() != null) {
                data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value",
                        data.getXml().integerToHex(number, 8));
            }
        } else if (type == SetResponseType.LAST_DATA_BLOCK) {
            data.setError(data.getData().getUInt8());
            long number = data.getData().getUInt32();
            if (data.getXml() != null) {
                // Result start tag.
                data.getXml().appendLine(TranslatorTags.RESULT, "Value",
                        GXDLMSTranslator.errorCodeToString(
                                data.getXml().getOutputType(),
                                ErrorCode.forValue(data.getError())));
                data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value",
                        data.getXml().integerToHex(number, 8));
            }
        } else if (type == SetResponseType.WITH_LIST) {
            int cnt = GXCommon.getObjectCount(data.getData());
            if (data.getXml() != null) {
                data.getXml().appendStartTag(TranslatorTags.RESULT, "Qty",
                        String.valueOf(cnt));
                for (int pos = 0; pos != cnt; ++pos) {
                    int err = data.getData().getUInt8();
                    data.getXml().appendLine(TranslatorTags.DATA_ACCESS_RESULT,
                            "Value",
                            GXDLMSTranslator.errorCodeToString(
                                    data.getXml().getOutputType(),
                                    ErrorCode.forValue(err)));
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
        short ret;
        if (data.getXml() != null) {
            data.getXml().appendStartTag(Command.WRITE_RESPONSE, "Qty",
                    data.getXml().integerToHex(cnt, 2));
        }
        for (int pos = 0; pos != cnt; ++pos) {
            ret = data.getData().getUInt8();
            if (ret != 0) {
                data.setError(data.getData().getUInt8());
            }
            if (data.getXml() != null) {
                if (ret == 0) {
                    data.getXml().appendLine("<" + GXDLMSTranslator
                            .errorCodeToString(data.getXml().getOutputType(),
                                    ErrorCode.forValue(ret))
                            + " />");
                } else {
                    data.getXml().appendLine(TranslatorTags.DATA_ACCESS_ERROR,
                            null,
                            GXDLMSTranslator.errorCodeToString(
                                    data.getXml().getOutputType(),
                                    ErrorCode.forValue(data.getError())));
                }
            }
        }
        if (data.getXml() != null) {
            data.getXml().appendEndTag(Command.WRITE_RESPONSE);
        }
    }

    /**
     * Handle get response and get data from block and/or update error status.
     * 
     * @param settings
     *            DLMS settings.
     * @param reply
     *            Received data from the client.
     * @param index
     *            Block index number.
     */
    static boolean handleGetResponse(final GXDLMSSettings settings,
            final GXReplyData reply, final int index) {
        long number;
        boolean ret = true;
        short ch = 0;
        GXByteBuffer data = reply.getData();

        // Get type.
        byte type = (byte) data.getUInt8();
        // Get invoke ID and priority.
        ch = data.getUInt8();

        if (reply.getXml() != null) {
            reply.getXml().appendStartTag(Command.GET_RESPONSE);
            reply.getXml().appendStartTag(Command.GET_RESPONSE, type);
            // InvokeIdAndPriority
            reply.getXml().appendLine(TranslatorTags.INVOKE_ID, "Value",
                    reply.getXml().integerToHex(ch, 2));
        }
        // Response normal
        if (type == GetCommandType.NORMAL) {
            // Result
            ch = data.getUInt8();
            if (ch != 0) {
                reply.setError((short) data.getUInt8());
            }
            if (reply.getXml() != null) {
                // Result start tag.
                reply.getXml().appendStartTag(TranslatorTags.RESULT);
                if (reply.getError() != 0) {
                    reply.getXml().appendLine(TranslatorTags.DATA_ACCESS_ERROR,
                            "Value",
                            GXDLMSTranslator.errorCodeToString(
                                    reply.getXml().getOutputType(),
                                    ErrorCode.forValue(reply.getError())));
                } else {
                    reply.getXml().appendStartTag(TranslatorTags.DATA);
                    GXDataInfo di = new GXDataInfo();
                    di.setXml(reply.getXml());
                    GXCommon.getData(reply.getData(), di);
                    reply.getXml().appendEndTag(TranslatorTags.DATA);
                }
            } else {
                getDataFromBlock(data, 0);
            }
        } else if (type == GetCommandType.NEXT_DATA_BLOCK) {
            // GetResponsewithDataBlock
            // Is Last block.
            ch = data.getUInt8();
            if (reply.getXml() != null) {
                // Result start tag.
                reply.getXml().appendStartTag(TranslatorTags.RESULT);
                // LastBlock
                reply.getXml().appendLine(TranslatorTags.LAST_BLOCK, "Value",
                        reply.getXml().integerToHex(ch, 2));
            }
            if (ch == 0) {
                reply.setMoreData(
                        RequestTypes.forValue(reply.getMoreData().getValue()
                                | RequestTypes.DATABLOCK.getValue()));
            } else {
                reply.setMoreData(
                        RequestTypes.forValue(reply.getMoreData().getValue()
                                & ~RequestTypes.DATABLOCK.getValue()));
            }
            // Get Block number.
            number = data.getUInt32();
            if (reply.getXml() != null) {
                // BlockNumber
                reply.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, "Value",
                        reply.getXml().integerToHex(number, 8));
            } else {
                // If meter's block index is zero based.
                if (number == 0 && settings.getBlockIndex() == 1) {
                    settings.setBlockIndex(0);
                }
                int expectedIndex = settings.getBlockIndex();
                if (number != expectedIndex) {
                    throw new IllegalArgumentException(
                            "Invalid Block number. It is " + number
                                    + " and it should be " + expectedIndex
                                    + ".");
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
                    reply.getXml().appendLine(TranslatorTags.DATA_ACCESS_RESULT,
                            "Value",
                            GXDLMSTranslator.errorCodeToString(
                                    reply.getXml().getOutputType(),
                                    ErrorCode.forValue(reply.getError())));
                } else if (reply.getData().available() != 0) {
                    // Get data size.
                    int blockLength = GXCommon.getObjectCount(data);
                    // if whole block is read.
                    if ((reply.getMoreData().getValue()
                            & RequestTypes.FRAME.getValue()) == 0) {
                        // Check Block length.
                        if (blockLength > data.size() - data.position()) {
                            reply.getXml()
                                    .appendComment("Block is not complete."
                                            + String.valueOf(data.size()
                                                    - data.position())
                                            + "/" + String.valueOf(blockLength)
                                            + ".");
                        }
                    }
                    reply.getXml().appendLine(TranslatorTags.RAW_DATA, "Value",
                            GXCommon.toHex(reply.getData().getData(), false,
                                    data.position(),
                                    reply.getData().available()));
                }
                reply.getXml().appendEndTag(TranslatorTags.RESULT);
            } else if (data.position() != data.size()) {
                // Get data size.
                int blockLength = GXCommon.getObjectCount(data);
                // if whole block is read.
                if ((reply.getMoreData().getValue()
                        & RequestTypes.FRAME.getValue()) == 0) {
                    // Check Block length.
                    if (blockLength > data.size() - data.position()) {
                        throw new IllegalArgumentException(
                                "Invalid block length.");
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
                if (reply.getMoreData() == RequestTypes.NONE) {
                    if (!reply.getPeek()) {
                        data.position(0);
                        settings.resetBlockIndex();
                    }
                }
            }
        } else if (type == GetCommandType.WITH_LIST) {
            // Get object count.
            int cnt = GXCommon.getObjectCount(data);
            Object[] values = new Object[cnt];
            if (reply.getXml() != null) {
                // Result start tag.
                reply.getXml().appendStartTag(TranslatorTags.RESULT, "Qty",
                        reply.getXml().integerToHex(cnt, 2));
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
                        reply.getXml().appendStartTag(Command.READ_RESPONSE,
                                SingleReadResponse.DATA);
                        GXCommon.getData(reply.getData(), di);
                        reply.getXml().appendEndTag(Command.READ_RESPONSE,
                                SingleReadResponse.DATA);
                    } else {
                        reply.setReadPosition(reply.getData().position());
                        getValueFromData(settings, reply);
                        reply.getData().position(reply.getReadPosition());
                        if (values != null) {
                            values[pos] = reply.getValue();
                        }
                        reply.setValue(null);
                    }
                }
            }
            reply.setValue(values);
            ret = false;
        } else {
            throw new IllegalArgumentException("Invalid Get response.");
        }
        if (reply.getXml() != null) {
            reply.getXml().appendEndTag(TranslatorTags.RESULT);
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
    private static void handleGbt(final GXDLMSSettings settings,
            final GXReplyData data) {
        int index = data.getData().position() - 1;
        data.setWindowSize(settings.getWindowSize());
        // BlockControl
        short bc = data.getData().getUInt8();
        // Is streaming active.
        data.setStreaming((bc & 0x40) != 0);
        // GBT Window size.
        byte windowSize = (byte) (bc & 0x3F);
        // Block number.
        data.setBlockNumber(data.getData().getUInt16());
        // Block number acknowledged.
        data.setBlockNumberAck(data.getData().getUInt16());
        settings.setBlockNumberAck(data.getBlockNumber());
        data.setCommand(Command.NONE);
        int len = GXCommon.getObjectCount(data.getData());
        if (len > data.getData().size() - data.getData().position()) {
            data.setComplete(false);
            return;
        }

        if (data.getXml() != null) {
            if ((data.getData().size() - data.getData().position()) != len) {
                data.getXml()
                        .appendComment("Data length is " + String.valueOf(len)
                                + "and there are "
                                + String.valueOf(data.getData().size()
                                        - data.getData().position())
                                + " bytes.");
            }
            data.getXml().appendStartTag(Command.GENERAL_BLOCK_TRANSFER);
            if (data.getXml().isComments()) {
                data.getXml()
                        .appendComment("Last block: " + ((bc & 0x80) != 0));
                data.getXml()
                        .appendComment("Streaming: " + data.getStreaming());
                data.getXml().appendComment("Window size: " + windowSize);
            }
            data.getXml().appendLine(TranslatorTags.BLOCK_CONTROL, null,
                    data.getXml().integerToHex(bc, 2));
            data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER, null,
                    data.getXml().integerToHex(data.getBlockNumber(), 4));
            data.getXml().appendLine(TranslatorTags.BLOCK_NUMBER_ACK, null,
                    data.getXml().integerToHex(data.getBlockNumberAck(), 4));

            // If last block and comments.
            if ((bc & 0x80) != 0 && data.getXml().isComments()) {
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
                    GXCommon.toHex(data.getData().getData(), false,
                            data.getData().position(), len));
            data.getXml().appendEndTag(Command.GENERAL_BLOCK_TRANSFER);
            return;
        }
        getDataFromBlock(data.getData(), index);
        // Is Last block.
        if ((bc & 0x80) == 0) {
            data.setMoreData(RequestTypes.forValue(data.getMoreData().getValue()
                    | RequestTypes.DATABLOCK.getValue()));
        } else {
            data.setMoreData(RequestTypes.forValue(data.getMoreData().getValue()
                    & ~RequestTypes.DATABLOCK.getValue()));
            if (data.getData().size() != 0) {
                data.getData().position(0);
                getPdu(settings, data);
            }
            // Get data if all data is read or we want to peek data.
            if (data.getData().position() != data.getData().size()
                    && (data.getCommand() == Command.READ_RESPONSE
                            || data.getCommand() == Command.GET_RESPONSE)
                    && (data.getMoreData() == RequestTypes.NONE
                            || data.getPeek())) {
                data.getData().position(0);
                getValueFromData(settings, data);
            }
        }
    }

    /**
     * Get PDU from the packet.
     * 
     * @param settings
     *            DLMS settings.
     * @param data
     *            received data.
     */
    public static void getPdu(final GXDLMSSettings settings,
            final GXReplyData data) {
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
                handleMethodResponse(settings, data);
                break;
            case Command.ACCESS_RESPONSE:
                handleAccessResponse(settings, data);
                break;
            case Command.GENERAL_BLOCK_TRANSFER:
                if (data.getXml() != null || (!settings.isServer()
                        && (data.getMoreData().getValue()
                                & RequestTypes.FRAME.getValue()) == 0)) {
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
            case Command.READ_REQUEST:
            case Command.WRITE_REQUEST:
            case Command.SET_REQUEST:
            case Command.METHOD_REQUEST:
            case Command.RELEASE_REQUEST:
                // Server handles this.
                if ((data.getMoreData().getValue()
                        & RequestTypes.FRAME.getValue()) != 0) {
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
                cmd = handleGloDedRequest(settings, data, cmd);
                // Server handles this.
                break;
            case Command.GLO_READ_RESPONSE:
            case Command.GLO_WRITE_RESPONSE:
            case Command.GLO_GET_RESPONSE:
            case Command.GLO_SET_RESPONSE:
            case Command.GLO_METHOD_RESPONSE:
            case Command.GENERAL_GLO_CIPHERING:
            case Command.GLO_EVENT_NOTIFICATION_REQUEST:
            case Command.DED_GET_RESPONSE:
            case Command.DED_SET_RESPONSE:
            case Command.DED_METHOD_RESPONSE:
            case Command.GENERAL_DED_CIPHERING:
            case Command.DED_EVENT_NOTIFICATION:
                handleGloDedResponse(settings, data, index);
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
            default:
                throw new IllegalArgumentException("Invalid Command.");
            }
        } else if ((data.getMoreData().getValue()
                & RequestTypes.FRAME.getValue()) == 0) {
            // Is whole block is read and if last packet and data is not try to
            // peek.
            if (!data.getPeek() && data.getMoreData() == RequestTypes.NONE) {
                if (data.getCommand() == Command.AARE
                        || data.getCommand() == Command.AARQ) {
                    data.getData().position(0);
                } else {
                    data.getData().position(1);
                }
            }
            if (cmd == Command.GENERAL_BLOCK_TRANSFER) {
                if (!data.isMoreData()) {
                    handleGbt(settings, data);
                }
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
                    data.setCommand(Command.NONE);
                    data.getData().position(data.getCipherIndex());
                    getPdu(settings, data);
                    break;
                default:
                    break;
                }
            } else {
                // Client do not need a command any more.
                data.setCommand(Command.NONE);
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
                    data.getData().position(data.getCipherIndex());
                    getPdu(settings, data);
                    break;
                default:
                    break;
                }
            }
        }

        // Get data only blocks if SN is used. This is faster.
        if (cmd == Command.READ_RESPONSE
                && data.getCommandType() == SingleReadResponse.DATA_BLOCK_RESULT
                && (data.getMoreData().getValue()
                        & RequestTypes.FRAME.getValue()) != 0) {
            return;
        }
        // Get data if all data is read or we want to peek data.
        if (data.getXml() == null
                && data.getData().position() != data.getData().size()
                && (cmd == Command.READ_RESPONSE || cmd == Command.GET_RESPONSE
                        || cmd == Command.METHOD_RESPONSE
                        || cmd == Command.DATA_NOTIFICATION)
                && (data.getMoreData() == RequestTypes.NONE
                        || data.getPeek())) {
            getValueFromData(settings, data);
        }
    }

    private static void handleExceptionResponse(final GXReplyData data) {
        throw new GXDLMSException(
                StateError.values()[data.getData().getUInt8() - 1],
                ExceptionServiceError.values()[data.getData().getUInt8() - 1]);
    }

    static void handleConfirmedServiceError(final GXReplyData data) {
        if (data.getXml() != null) {
            data.getXml().appendStartTag(Command.CONFIRMED_SERVICE_ERROR);
            if (data.getXml()
                    .getOutputType() == TranslatorOutputType.STANDARD_XML) {
                data.getData().getUInt8();
                data.getXml().appendStartTag(TranslatorTags.INITIATE_ERROR);
                ServiceError type =
                        ServiceError.forValue(data.getData().getUInt8());

                String tag = TranslatorStandardTags.serviceErrorToString(type);
                String value = TranslatorStandardTags.getServiceErrorValue(type,
                        (byte) data.getData().getUInt8());
                data.getXml().appendLine("x:" + tag, null, value);
                data.getXml().appendEndTag(TranslatorTags.INITIATE_ERROR);
            } else {
                data.getXml().appendLine(TranslatorTags.SERVICE, "Value", data
                        .getXml().integerToHex(data.getData().getUInt8(), 2));
                ServiceError type =
                        ServiceError.forValue(data.getData().getUInt8());
                data.getXml().appendStartTag(TranslatorTags.SERVICE_ERROR);
                data.getXml().appendLine(
                        TranslatorSimpleTags.serviceErrorToString(type),
                        "Value", TranslatorSimpleTags.getServiceErrorValue(type,
                                (byte) data.getData().getUInt8()));
                data.getXml().appendEndTag(TranslatorTags.SERVICE_ERROR);
            }
            data.getXml().appendEndTag(Command.CONFIRMED_SERVICE_ERROR);
        } else {
            ConfirmedServiceError service =
                    ConfirmedServiceError.forValue(data.getData().getUInt8());
            ServiceError type =
                    ServiceError.forValue(data.getData().getUInt8());
            throw new GXDLMSConfirmedServiceError(service, type,
                    data.getData().getUInt8());
        }
    }

    private static int handleGloDedRequest(final GXDLMSSettings settings,
            final GXReplyData data, final int cmd) {
        int cmd2 = cmd;
        if (settings.getCipher() == null) {
            throw new RuntimeException("Secure connection is not supported.");
        }
        // If all frames are read.
        if ((data.getMoreData().getValue()
                & RequestTypes.FRAME.getValue()) == 0) {
            data.getData().position(data.getData().position() - 1);
            AesGcmParameter p;
            if (settings.getCipher().getDedicatedKey() != null
                    && (settings.getConnected() & ConnectionState.DLMS) != 0) {
                p = new AesGcmParameter(settings.getSourceSystemTitle(),
                        settings.getCipher().getDedicatedKey(),
                        settings.getCipher().getAuthenticationKey());
            } else {
                p = new AesGcmParameter(settings.getSourceSystemTitle(),
                        settings.getCipher().getBlockCipherKey(),
                        settings.getCipher().getAuthenticationKey());
            }
            byte[] tmp = GXCiphering.decrypt(settings.getCipher(), p,
                    data.getData());
            data.getData().clear();
            data.getData().set(tmp);
            // Get command.
            cmd2 = data.getData().getUInt8();
            data.setCommand(cmd2);
        } else {
            data.getData().position(data.getData().position() - 1);
        }
        return cmd2;
    }

    private static void handleGloDedResponse(final GXDLMSSettings settings,
            final GXReplyData data, final int index) {
        if (settings.getCipher() == null) {
            throw new RuntimeException("Secure connection is not supported.");
        }
        // If all frames are read.
        if ((data.getMoreData().getValue()
                & RequestTypes.FRAME.getValue()) == 0) {
            data.getData().position(data.getData().position() - 1);
            GXByteBuffer bb = new GXByteBuffer(data.getData());
            data.getData().position(index);
            data.getData().size(index);

            AesGcmParameter p;
            if (settings.getCipher().getDedicatedKey() != null
                    && settings.getConnected() == ConnectionState.DLMS) {
                p = new AesGcmParameter(settings.getSourceSystemTitle(),
                        settings.getCipher().getDedicatedKey(),
                        settings.getCipher().getAuthenticationKey());
            } else {
                p = new AesGcmParameter(settings.getSourceSystemTitle(),
                        settings.getCipher().getBlockCipherKey(),
                        settings.getCipher().getAuthenticationKey());
            }
            data.getData()
                    .set(GXCiphering.decrypt(settings.getCipher(), p, bb));
            data.setCommand(Command.NONE);
            getPdu(settings, data);
            data.setCipherIndex(data.getData().size());
        }
    }

    private static void handleGeneralCiphering(final GXDLMSSettings settings,
            final GXReplyData data) {
        if (settings.getCipher() == null) {
            throw new RuntimeException("Secure connection is not supported.");
        }
        // If all frames are read.
        if ((data.getMoreData().getValue()
                & RequestTypes.FRAME.getValue()) == 0) {
            data.getData().position(data.getData().position() - 1);

            AesGcmParameter p =
                    new AesGcmParameter(settings.getSourceSystemTitle(),
                            settings.getCipher().getBlockCipherKey(),
                            settings.getCipher().getAuthenticationKey());
            byte[] tmp = GXCiphering.decrypt(settings.getCipher(), p,
                    data.getData());
            data.getData().clear();
            data.getData().set(tmp);
            data.setCommand(Command.NONE);
            if (p.getSecurity() != null) {
                try {
                    getPdu(settings, data);
                } catch (Exception ex) {
                    if (data.getXml() == null) {
                        throw ex;
                    }
                }
            }
            if (data.getXml() != null) {
                data.getXml().appendStartTag(Command.GENERAL_CIPHERING);
                data.getXml().appendLine(TranslatorTags.TRANSACTION_ID, null,
                        data.getXml().integerToHex(p.getInvocationCounter(), 16,
                                true));
                data.getXml().appendLine(TranslatorTags.ORIGINATOR_SYSTEM_TITLE,
                        null, GXCommon.toHex(p.getSystemTitle(), false));
                data.getXml().appendLine(TranslatorTags.RECIPIENT_SYSTEM_TITLE,
                        null,
                        GXCommon.toHex(p.getRecipientSystemTitle(), false));
                data.getXml().appendLine(TranslatorTags.DATE_TIME, null,
                        GXCommon.toHex(p.getDateTime(), false));
                data.getXml().appendLine(TranslatorTags.OTHER_INFORMATION, null,
                        GXCommon.toHex(p.getOtherInformation(), false));

                data.getXml().appendStartTag(TranslatorTags.KEY_INFO);
                data.getXml().appendStartTag(TranslatorTags.AGREED_KEY);
                data.getXml().appendLine(TranslatorTags.KEY_PARAMETERS, null,
                        data.getXml().integerToHex(p.getKeyParameters(), 2,
                                true));
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

    /**
     * Get value from data.
     * 
     * @param reply
     *            Received data.
     */
    static void getValueFromData(final GXDLMSSettings settings,
            final GXReplyData reply) {
        GXByteBuffer data = reply.getData();
        GXDataInfo info = new GXDataInfo();
        if (reply.getValue() instanceof Object[]) {
            info.setType(DataType.ARRAY);
            info.setCount(reply.getTotalCount());
            info.setIndex(reply.getCount());
        }
        int index = data.position();
        data.position(reply.getReadPosition());
        try {
            Object value = GXCommon.getData(data, info);
            if (value != null) { // If new data.
                if (!(value instanceof Object[])) {
                    reply.setValueType(info.getType());
                    reply.setValue(value);
                    reply.setTotalCount(0);
                    reply.setReadPosition(data.position());
                } else {
                    if (((Object[]) value).length != 0) {
                        if (reply.getValue() == null) {
                            reply.setValue(value);
                        } else {
                            // Add items to collection.
                            List<Object> list = new ArrayList<Object>();
                            GXCommon.addAll(list, (Object[]) reply.getValue());
                            GXCommon.addAll(list, (Object[]) value);
                            reply.setValue(list.toArray());
                        }
                    }
                    reply.setReadPosition(data.position());
                    // Element count.
                    reply.setTotalCount(info.getCount());
                }
            } else if (info.isComplete()
                    && reply.getCommand() == Command.DATA_NOTIFICATION) {
                // If last item is null. This is a special case.
                reply.setReadPosition(data.position());
            }
        } finally {
            data.position(index);
        }

        // If last data frame of the data block is read.
        if (reply.getCommand() != Command.DATA_NOTIFICATION && info.isComplete()
                && reply.getMoreData() == RequestTypes.NONE) {
            // If all blocks are read.
            if (settings != null) {
                settings.resetBlockIndex();
            }
            data.position(0);
        }
    }

    public static boolean getData(final GXDLMSSettings settings,
            final GXByteBuffer reply, final GXReplyData data,
            final GXReplyData notify) {
        short frame = 0;
        boolean isNotify = false;
        GXReplyData target = data;
        // If DLMS frame is generated.
        if (settings.getInterfaceType() == InterfaceType.HDLC) {
            frame = getHdlcData(settings.isServer(), settings, reply, target,
                    notify);
            if (notify != null && frame == 0x13) {
                target = notify;
                isNotify = true;
            }
            target.setFrameId(frame);
        } else if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
            if (!getTcpData(settings, reply, target, notify)) {
                target = notify;
                isNotify = true;
            }
        } else if (settings.getInterfaceType() == InterfaceType.WIRELESS_MBUS) {
            getMBusData(settings, reply, target);
        } else if (settings.getInterfaceType() == InterfaceType.PDU) {
            target.setPacketLength(reply.size());
            target.setComplete(true);
        } else {
            throw new IllegalArgumentException("Invalid Interface type.");
        }
        // If all data is not read yet.
        if (!target.isComplete()) {
            return false;
        }

        getDataFromFrame(reply, target);

        // If keepalive or get next frame request.
        if (target.getXml() != null || (frame != 0x13 && (frame & 0x1) != 0)) {
            if (settings.getInterfaceType() == InterfaceType.HDLC
                    && (target.getError() == ErrorCode.REJECTED.getValue()
                            || target.getData().size() != 0)) {
                if (reply.position() != reply.size()) {
                    reply.position(reply.position() + 3);
                }
            }
            return true;
        }
        getPdu(settings, target);
        if (isNotify) {
            return false;
        }
        return true;
    }

    /**
     * Get data from HDLC or wrapper frame.
     * 
     * @param reply
     *            Received data that includes HDLC frame.
     * @param info
     *            Reply data.
     */
    private static void getDataFromFrame(final GXByteBuffer reply,
            final GXReplyData info) {
        GXByteBuffer data = info.getData();
        int offset = data.size();
        int cnt = info.getPacketLength() - reply.position();
        if (cnt != 0) {
            data.capacity(offset + cnt);
            data.set(reply.getData(), reply.position(), cnt);
            reply.position(reply.position() + cnt);
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
    private static int getDataFromBlock(final GXByteBuffer data,
            final int index) {
        if (data.size() == data.position()) {
            data.clear();
            return 0;
        }
        int len = data.position() - index;
        System.arraycopy(data.getData(), data.position(), data.getData(),
                data.position() - len, data.size() - data.position());
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
    static void getActionInfo(final ObjectType objectType, final int[] value,
            final int[] count) {
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
     * @see GXDLMSClient#snrmRequest
     */
    static void parseSnrmUaResponse(final GXByteBuffer data,
            final GXDLMSLimits limits) {
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
}
