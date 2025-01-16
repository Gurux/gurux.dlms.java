package gurux.dlms.objects;

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.enums.ObjectType;

/**
 * Class for better object creation, in case of using custom objects.
 */
public class GXCreateObject {

    public static GXDLMSObject createObject(final GXDLMSSettings settings, final ObjectType type, final int classID,
                                     final int version) {
        // If IC is manufacturer specific or unknown.
        if (type == null || type == ObjectType.NONE) {
            GXDLMSObject obj = null;
            if (settings != null && settings.getCustomObjectNotifier() != null) {
                obj = settings.getCustomObjectNotifier().onObjectCreate(classID, version);
            }
            if (obj == null) {
                obj = new GXDLMSObject();
            }
            return obj;
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
                return new GXDLMSIECLocalPortSetup();
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
                return new GXDLMSUtilityTables();
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
            case COMPACT_DATA:
                return new GXDLMSCompactData();
            case LLC_SSCS_SETUP:
                return new GXDLMSLlcSscsSetup();
            case PRIME_NB_OFDM_PLC_PHYSICAL_LAYER_COUNTERS:
                return new GXDLMSPrimeNbOfdmPlcPhysicalLayerCounters();
            case PRIME_NB_OFDM_PLC_MAC_SETUP:
                return new GXDLMSPrimeNbOfdmPlcMacSetup();
            case PRIME_NB_OFDM_PLC_MAC_FUNCTIONAL_PARAMETERS:
                return new GXDLMSPrimeNbOfdmPlcMacFunctionalParameters();
            case PRIME_NB_OFDM_PLC_MAC_COUNTERS:
                return new GXDLMSPrimeNbOfdmPlcMacCounters();
            case PRIME_NB_OFDM_PLC_MAC_NETWORK_ADMINISTRATION_DATA:
                return new GXDLMSPrimeNbOfdmPlcMacNetworkAdministrationData();
            case PRIME_NB_OFDM_PLC_APPLICATIONS_IDENTIFICATION:
                return new GXDLMSPrimeNbOfdmPlcApplicationsIdentification();
            case IEC_8802_LLC_TYPE1_SETUP:
                return new GXDLMSIec8802LlcType1Setup();
            case IEC_8802_LLC_TYPE2_SETUP:
                return new GXDLMSIec8802LlcType2Setup();
            case IEC_8802_LLC_TYPE3_SETUP:
                return new GXDLMSIec8802LlcType3Setup();
            case SFSK_REPORTING_SYSTEM_LIST:
                return new GXDLMSSFSKReportingSystemList();
            case ARBITRATOR:
                return new GXDLMSArbitrator();
            case SFSK_MAC_COUNTERS:
                return new GXDLMSSFSKMacCounters();
            case SFSK_MAC_SYNCHRONIZATION_TIMEOUTS:
                return new GXDLMSSFSKMacSynchronizationTimeouts();
            case SFSK_ACTIVE_INITIATOR:
                return new GXDLMSSFSKActiveInitiator();
            case SFSK_PHY_MAC_SETUP:
                return new GXDLMSSFSKPhyMacSetUp();
            case NTP_SETUP:
                return new GXDLMSNtpSetup();
            case COMMUNICATION_PORT_PROTECTION:
                return new GXDLMSCommunicationPortProtection();
            case G3_PLC_MAC_LAYER_COUNTERS:
                return new GXDLMSG3PlcMacLayerCounters();
            case G3_PLC6_LO_WPAN:
                return new GXDLMSG3Plc6LoWPan();
            case G3_PLC_MAC_SETUP:
                return new GXDLMSG3PlcMacSetup();
            case ARRAY_MANAGER:
                return new GXDLMSArrayManager();
            case LTE_MONITORING:
                return new GXDLMSLteMonitoring();
            case FUNCTION_CONTROL:
                return new GXDLMSFunctionControl();
            case COAP_SETUP:
                return new GXDLMSCoAPSetup();
            case COAP_DIAGNOSTIC:
                return new GXDLMSCoAPDiagnostic();
            case MBUS_PORT_SETUP:
                return new GXDLMSMBusPortSetup();
            case MBUS_DIAGNOSTIC:
                return new GXDLMSMBusDiagnostic();
            default:
                return new GXDLMSObject();
        }
    }

}
