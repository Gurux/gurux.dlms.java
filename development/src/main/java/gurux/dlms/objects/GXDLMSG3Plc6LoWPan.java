package gurux.dlms.objects;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXBitString;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXEnum;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.GXStructure;
import gurux.dlms.GXUInt16;
import gurux.dlms.GXUInt8;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.DeviceType;

/**
 * G3-PLC 6LoWPAN adaptation layer setup. Online help:
 * https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSG3Plc6LoWPan
 */
public class GXDLMSG3Plc6LoWPan extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Defines the maximum number of hops to be used by the routing algorithm.
     * PIB attribute 0x0F.
     */
    private short maxHops;
    /**
     * The weak link value defines the LQI value below which a link to a
     * neighbour is considered as a weak link. PIB attribute 0x1A.
     */
    private short weakLqiValue;

    /**
     * The minimum security level to be used for incoming and outgoing
     * adaptation frames. PIB attribute 0x00.
     */
    private short securityLevel;
    /**
     * Contains the list of prefixes defined on this PAN PIB attribute 0x01.
     */
    private short[] prefixTable;

    /**
     * The routing configuration element specifies all parameters linked to the
     * routing mechanism described in ITU-T G.9903:2014.
     */
    private ArrayList<GXDLMSRoutingConfiguration> routingConfiguration;
    /**
     * Maximum time to live of an adpBroadcastLogTable entry (in minutes). PIB
     * attribute 0x02.
     */
    private int broadcastLogTableEntryTtl;

    /**
     * Routing table PIB attribute 0x03.
     */
    private ArrayList<GXDLMSRoutingTable> routingTable;

    /**
     * @return Routing table PIB attribute 0x03.
     */
    public final ArrayList<GXDLMSRoutingTable> getRoutingTable() {
        return routingTable;
    }

    /**
     * @param value
     *            Routing table PIB attribute 0x03.
     */
    public final void setRoutingTable(ArrayList<GXDLMSRoutingTable> value) {
        routingTable = value;
    }

    /**
     * Contains the context information associated to each CID extension field.
     * PIB attribute 0x07.
     */
    private ArrayList<GXDLMSContextInformationTable> contextInformationTable;
    /**
     * Contains the list of the blacklisted neighbours.Key is 16-bit address of
     * the blacklisted neighbour. Value is Remaining time in minutes until which
     * this entry in the blacklisted neighbour table is considered valid. PIB
     * attribute 0x1E.
     */
    private ArrayList<Entry<Integer, Integer>> blacklistTable;
    /**
     * Broadcast log table PIB attribute 0x0B.
     */
    private ArrayList<GXDLMSBroadcastLogTable> broadcastLogTable;

    /**
     * Contains the group addresses to which the device belongs. array PIB
     * attribute 0x0E.
     */
    private int[] groupTable;
    /**
     * Network join timeout in seconds for LBD PIB attribute 0x20.
     */
    private int maxJoinWaitTime;
    /**
     * Timeout for path discovery in seconds. PIB attribute 0x21.
     */
    private short pathDiscoveryTime;

    /**
     * Index of the active GMK to be used for data transmission. PIB attribute
     * 0x22.
     */
    private short activeKeyIndex;
    /**
     * Metric Type to be used for routing purposes. PIB attribute 0x3.
     */
    private short metricType;

    /**
     * Defines the short address of the coordinator. PIB attribute 0x8.
     */
    private int coordShortAddress;
    /**
     * Is default routing (LOADng) disabled. PIB attribute 0xF0.
     */
    private boolean disableDefaultRouting;
    /**
     * Defines the type of the device connected to the modem PIB attribute 0x10.
     */
    private DeviceType deviceType = DeviceType.PAN_DEVICE;

    /**
     * If true, the default route will be created. PIB attribute 0x24.
     */
    private boolean defaultCoordRouteEnabled;

    /**
     * Constructor.
     */
    public GXDLMSG3Plc6LoWPan() {
        this("0.0.29.2.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSG3Plc6LoWPan(String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSG3Plc6LoWPan(String ln, int sn) {
        super(ObjectType.G3_PLC6_LO_WPAN, ln, sn);
        setVersion(3);
        blacklistTable = new ArrayList<Entry<Integer, Integer>>();
        contextInformationTable = new ArrayList<GXDLMSContextInformationTable>();
        routingConfiguration = new ArrayList<GXDLMSRoutingConfiguration>();
        routingTable = new ArrayList<GXDLMSRoutingTable>();
        broadcastLogTable = new ArrayList<GXDLMSBroadcastLogTable>();
        maxHops = 8;
        weakLqiValue = 52;
        securityLevel = 5;
        broadcastLogTableEntryTtl = 2;
        maxJoinWaitTime = 20;
        pathDiscoveryTime = 40;
        activeKeyIndex = 0;
        metricType = 0xF;
        coordShortAddress = 0;
        disableDefaultRouting = false;
        deviceType = DeviceType.NOT_DEFINED;
    }

    /**
     * @return Defines the maximum number of hops to be used by the routing
     *         algorithm. PIB attribute 0x0F.
     */
    public final short getMaxHops() {
        return maxHops;
    }

    /**
     * @param value
     *            Defines the maximum number of hops to be used by the routing
     *            algorithm. PIB attribute 0x0F.
     */
    public final void setMaxHops(final short value) {
        maxHops = value;
    }

    /**
     * @return The weak link value defines the LQI value below which a link to a
     *         neighbour is considered as a weak link. PIB attribute 0x1A.
     */
    public final short getWeakLqiValue() {
        return weakLqiValue;
    }

    /**
     * @param value
     *            The weak link value defines the LQI value below which a link
     *            to a neighbour is considered as a weak link. PIB attribute
     *            0x1A.
     */
    public final void setWeakLqiValue(final short value) {
        weakLqiValue = value;
    }

    /**
     * @return The minimum security level to be used for incoming and outgoing
     *         adaptation frames. PIB attribute 0x00.
     */
    public final short getSecurityLevel() {
        return securityLevel;
    }

    /**
     * @param value
     *            The minimum security level to be used for incoming and
     *            outgoing adaptation frames. PIB attribute 0x00.
     */
    public final void setSecurityLevel(final short value) {
        securityLevel = value;
    }

    /**
     * @return Contains the list of prefixes defined on this PAN PIB attribute
     *         0x01.
     */
    public final short[] getPrefixTable() {
        return prefixTable;
    }

    /**
     * @param value
     *            Contains the list of prefixes defined on this PAN PIB
     *            attribute 0x01.
     */
    public final void setPrefixTable(final short[] value) {
        prefixTable = value;
    }

    /**
     * @return The routing configuration element specifies all parameters linked
     *         to the routing mechanism described in ITU-T G.9903:2014.
     */
    public final ArrayList<GXDLMSRoutingConfiguration> getRoutingConfiguration() {
        return routingConfiguration;
    }

    /**
     * @param value
     *            The routing configuration element specifies all parameters
     *            linked to the routing mechanism described in ITU-T
     *            G.9903:2014.
     */
    public final void setRoutingConfiguration(final ArrayList<GXDLMSRoutingConfiguration> value) {
        routingConfiguration = value;
    }

    /**
     * @return Maximum time to live of an adpBroadcastLogTable entry (in
     *         minutes). PIB attribute 0x02.
     */
    public final int getBroadcastLogTableEntryTtl() {
        return broadcastLogTableEntryTtl;
    }

    /**
     * @param value
     *            Maximum time to live of an adpBroadcastLogTable entry (in
     *            minutes). PIB attribute 0x02.
     */
    public final void setBroadcastLogTableEntryTtl(final int value) {
        broadcastLogTableEntryTtl = value;
    }

    /**
     * @return Contains the context information associated to each CID extension
     *         field. PIB attribute 0x07.
     */
    public final ArrayList<GXDLMSContextInformationTable> getContextInformationTable() {
        return contextInformationTable;
    }

    /**
     * @param value
     *            Contains the context information associated to each CID
     *            extension field. PIB attribute 0x07.
     */
    public final void setContextInformationTable(final ArrayList<GXDLMSContextInformationTable> value) {
        contextInformationTable = value;
    }

    /**
     * @return Contains the list of the blacklisted neighbours.Key is 16-bit
     *         address of the blacklisted neighbour. Value is Remaining time in
     *         minutes until which this entry in the blacklisted neighbour table
     *         is considered valid. PIB attribute 0x1E.
     */
    public final ArrayList<Entry<Integer, Integer>> getBlacklistTable() {
        return blacklistTable;
    }

    /**
     * @param value
     *            Contains the list of the blacklisted neighbours.Key is 16-bit
     *            address of the blacklisted neighbour. Value is Remaining time
     *            in minutes until which this entry in the blacklisted neighbour
     *            table is considered valid. PIB attribute 0x1E.
     */
    public final void setBlacklistTable(ArrayList<Entry<Integer, Integer>> value) {
        blacklistTable = value;
    }

    /**
     * @return Broadcast log table PIB attribute 0x0B.
     */
    public final ArrayList<GXDLMSBroadcastLogTable> getBroadcastLogTable() {
        return broadcastLogTable;
    }

    /**
     * @param value
     *            Broadcast log table PIB attribute 0x0B.
     */
    public final void setBroadcastLogTable(final ArrayList<GXDLMSBroadcastLogTable> value) {
        broadcastLogTable = value;
    }

    /**
     * @return Contains the group addresses to which the device belongs. array
     *         PIB attribute 0x0E.
     */
    public final int[] getGroupTable() {
        return groupTable;
    }

    /**
     * @param value
     *            Contains the group addresses to which the device belongs.
     *            array PIB attribute 0x0E.
     */
    public final void setGroupTable(final int[] value) {
        groupTable = value;
    }

    /**
     * @return Network join timeout in seconds for LBD PIB attribute 0x20.
     */
    public final int getMaxJoinWaitTime() {
        return maxJoinWaitTime;
    }

    /**
     * @param value
     *            Network join timeout in seconds for LBD PIB attribute 0x20.
     */
    public final void setMaxJoinWaitTime(int value) {
        maxJoinWaitTime = value;
    }

    /**
     * @return Timeout for path discovery in seconds. PIB attribute 0x21.
     */
    public final short getPathDiscoveryTime() {
        return pathDiscoveryTime;
    }

    /**
     * @param value
     *            Timeout for path discovery in seconds. PIB attribute 0x21.
     */
    public final void setPathDiscoveryTime(final short value) {
        pathDiscoveryTime = value;
    }

    /**
     * @return Index of the active GMK to be used for data transmission. PIB
     *         attribute 0x22.
     */
    public final short getActiveKeyIndex() {
        return activeKeyIndex;
    }

    /**
     * @param value
     *            Index of the active GMK to be used for data transmission. PIB
     *            attribute 0x22.
     */
    public final void setActiveKeyIndex(final short value) {
        activeKeyIndex = value;
    }

    /**
     * @return Metric Type to be used for routing purposes. PIB attribute 0x3.
     */
    public final short getMetricType() {
        return metricType;
    }

    /**
     * @param value
     *            Metric Type to be used for routing purposes. PIB attribute
     *            0x3.
     */
    public final void setMetricType(final short value) {
        metricType = value;
    }

    /**
     * @return Defines the short address of the coordinator. PIB attribute 0x8.
     */
    public final int getCoordShortAddress() {
        return coordShortAddress;
    }

    /**
     * @param value
     *            Defines the short address of the coordinator. PIB attribute
     *            0x8.
     */
    public final void setCoordShortAddress(final int value) {
        coordShortAddress = value;
    }

    /**
     * @return Is default routing (LOADng) disabled. PIB attribute 0xF0.
     */
    public final boolean getDisableDefaultRouting() {
        return disableDefaultRouting;
    }

    /**
     * @param value
     *            Is default routing (LOADng) disabled. PIB attribute 0xF0.
     */
    public final void setDisableDefaultRouting(boolean value) {
        disableDefaultRouting = value;
    }

    /**
     * @return Defines the type of the device connected to the modem PIB
     *         attribute 0x10.
     */
    public final DeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * @param value
     *            Defines the type of the device connected to the modem PIB
     *            attribute 0x10.
     */
    public final void setDeviceType(DeviceType value) {
        deviceType = value;
    }

    /**
     * @return If true, the default route will be created. PIB attribute 0x24.
     */
    public final boolean getDefaultCoordRouteEnabled() {
        return defaultCoordRouteEnabled;
    }

    /**
     * @param value
     *            If true, the default route will be created. PIB attribute
     *            0x24.
     */
    public final void setDefaultCoordRouteEnabled(boolean value) {
        defaultCoordRouteEnabled = value;
    }

    /**
     * List of the addresses of the devices for which this LOADng router is
     * providing connectivity. PIB attribute 0x23.
     */
    private int[] destinationAddress;

    /**
     * @return List of the addresses of the devices for which this LOADng router
     *         is providing connectivity. PIB attribute 0x23.
     */
    public final int[] getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param value
     *            List of the addresses of the devices for which this LOADng
     *            router is providing connectivity. PIB attribute 0x23.
     */
    public final void setDestinationAddress(int[] value) {
        destinationAddress = value;
    }

    /**
     * PIB attribute 0x04.
     */
    private short lowLQI;

    /**
     * @return PIB attribute 0x04.
     */
    public final short getLowLQI() {
        return lowLQI;
    }

    /**
     * @param value
     *            PIB attribute 0x04.
     */
    public final void setLowLQI(final short value) {
        lowLQI = value;
    }

    /**
     * PIB attribute 0x05.
     */
    private short highLQI;

    /**
     * @return PIB attribute 0x05.
     */
    public final short getHighLQI() {
        return highLQI;
    }

    /**
     * @param value
     *            PIB attribute 0x05.
     */
    public final void setHighLQI(final short value) {
        highLQI = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getMaxHops(), getWeakLqiValue(), getSecurityLevel(), getPrefixTable(),
                getRoutingConfiguration(), getBroadcastLogTableEntryTtl(), getRoutingTable(),
                getContextInformationTable(), getBlacklistTable(), getBroadcastLogTable(), getGroupTable(),
                getMaxJoinWaitTime(), getPathDiscoveryTime(), getActiveKeyIndex(), getMetricType(),
                getCoordShortAddress(), getDisableDefaultRouting(), getDeviceType(), getDefaultCoordRouteEnabled(),
                getDestinationAddress(), getLowLQI(), getHighLQI() };
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        ArrayList<Integer> attributes = new ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // MaxHops
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // WeakLqiValue
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // SecurityLevel
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // PrefixTable
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // RoutingConfiguration
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // BroadcastLogTableEntryTtl
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // RoutingTable
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // ContextInformationTable
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // BlacklistTable
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // BroadcastLogTable
        if (all || canRead(11)) {
            attributes.add(11);
        }
        // GroupTable
        if (all || canRead(12)) {
            attributes.add(12);
        }
        // MaxJoinWaitTime
        if (all || canRead(13)) {
            attributes.add(13);
        }
        // PathDiscoveryTime
        if (all || canRead(14)) {
            attributes.add(14);
        }
        // ActiveKeyIndex
        if (all || canRead(15)) {
            attributes.add(15);
        }
        // MetricType
        if (all || canRead(16)) {
            attributes.add(16);
        }
        if (getVersion() > 0) {
            // CoordShortAddress
            if (all || canRead(17)) {
                attributes.add(17);
            }
            // DisableDefaultRouting
            if (all || canRead(18)) {
                attributes.add(18);
            }
            // DeviceType
            if (all || canRead(19)) {
                attributes.add(19);
            }
            if (getVersion() > 1) {
                // DefaultCoordRouteEnabled
                if (all || canRead(20)) {
                    attributes.add(20);
                }
                // DestinationAddress
                if (all || canRead(21)) {
                    attributes.add(21);
                }
                if (getVersion() > 2) {
                    // LowLQI
                    if (all || canRead(22)) {
                        attributes.add(22);
                    }
                    // HighLQI
                    if (all || canRead(23)) {
                        attributes.add(23);
                    }
                }
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "MaxHops", "WeakLqiValue", "SecurityLevel", "PrefixTable",
                "RoutingConfiguration", "BroadcastLogTableEntryTtl", "RoutingTable", "ContextInformationTable",
                "BlacklistTable", "BroadcastLogTable", "GroupTable", "MaxJoinWaitTime", " PathDiscoveryTime",
                "ActiveKeyIndex", "MetricType", "CoordShortAddress", "DisableDefaultRouting", "DeviceType",
                "Default coord route enabled", "Destination address", "Low LQI", "High LQI" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[0];
    }

    @Override
    public final int getAttributeCount() {
        if (getVersion() == 0) {
            return 16;
        }
        if (getVersion() == 1) {
            return 19;
        }
        if (getVersion() == 2) {
            return 21;
        }
        // Version is 3.
        return 23;
    }

    @Override
    public final int getMethodCount() {
        return 0;
    }

    @Override
    public DataType getDataType(int index) {
        // LN.
        if (index == 1) {
            return DataType.OCTET_STRING;
        }
        // MaxHops
        if (index == 2) {
            return DataType.UINT8;
        }
        // WeakLqiValue
        if (index == 3) {
            return DataType.UINT8;
        }
        // SecurityLevel
        if (index == 4) {
            return DataType.UINT8;
        }
        // PrefixTable
        if (index == 5) {
            return DataType.ARRAY;
        }
        // RoutingConfiguration
        if (index == 6) {
            return DataType.ARRAY;
        }
        // BroadcastLogTableEntryTtl
        if (index == 7) {
            return DataType.UINT16;
        }
        // RoutingTable
        if (index == 8) {
            return DataType.ARRAY;
        }
        // ContextInformationTable
        if (index == 9) {
            return DataType.ARRAY;
        }
        // BlacklistTable
        if (index == 10) {
            return DataType.ARRAY;
        }
        // BroadcastLogTable
        if (index == 11) {
            return DataType.ARRAY;
        }
        // GroupTable
        if (index == 12) {
            return DataType.ARRAY;
        }
        // MaxJoinWaitTime
        if (index == 13) {
            return DataType.UINT16;
        }
        // PathDiscoveryTime
        if (index == 14) {
            return DataType.UINT8;
        }
        // ActiveKeyIndex
        if (index == 15) {
            return DataType.UINT8;
        }
        // MetricType
        if (index == 16) {
            return DataType.UINT8;
        }
        if (getVersion() > 0) {
            // CoordShortAddress
            if (index == 17) {
                return DataType.UINT16;
            }
            // DisableDefaultRouting
            if (index == 18) {
                return DataType.BOOLEAN;
            }
            // DeviceType
            if (index == 19) {
                return DataType.ENUM;
            }
            if (getVersion() > 1) {
                // DefaultCoordRouteEnabled
                if (index == 20) {
                    return DataType.BOOLEAN;
                }
                // DestinationAddress
                if (index == 21) {
                    return DataType.ARRAY;
                }
                // LowLQI
                if (index == 22) {
                    return DataType.UINT8;
                }
                // HighLQI
                if (index == 23) {
                    return DataType.UINT8;
                }

            }
        }
        throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
    }

    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            return GXCommon.logicalNameToBytes(getLogicalName());
        }
        if (e.getIndex() == 2) {
            return getMaxHops();
        }
        if (e.getIndex() == 3) {
            return getWeakLqiValue();
        }
        if (e.getIndex() == 4) {
            return getSecurityLevel();
        }
        if (e.getIndex() == 5) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getPrefixTable() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getPrefixTable().length, bb);
                for (Object it : prefixTable) {
                    GXCommon.setData(settings, bb, DataType.UINT8, it);
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 6) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getRoutingConfiguration() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getRoutingConfiguration().size(), bb);
                for (GXDLMSRoutingConfiguration it : getRoutingConfiguration()) {
                    bb.setUInt8(DataType.STRUCTURE);
                    bb.setUInt8(14);
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getNetTraversalTime());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getRoutingTableEntryTtl());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getKr());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getKm());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getKc());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getKq());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getKh());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getKrt());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getRreqRetries());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getRreqReqWait());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getBlacklistTableEntryTtl());
                    GXCommon.setData(settings, bb, DataType.BOOLEAN, it.getUnicastRreqGenEnable());
                    GXCommon.setData(settings, bb, DataType.BOOLEAN, it.getRlcEnabled());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getAddRevLinkCost());
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 7) {
            return getBroadcastLogTableEntryTtl();
        }
        if (e.getIndex() == 8) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getRoutingTable() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getRoutingTable().size(), bb);
                for (GXDLMSRoutingTable it : getRoutingTable()) {
                    bb.setUInt8(DataType.STRUCTURE);
                    bb.setUInt8(6);
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getDestinationAddress());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getNextHopAddress());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getRouteCost());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getHopCount());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getWeakLinkCount());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getValidTime());
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 9) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getContextInformationTable() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getContextInformationTable().size(), bb);
                for (GXDLMSContextInformationTable it : getContextInformationTable()) {
                    bb.setUInt8(DataType.STRUCTURE);
                    bb.setUInt8(5);
                    GXCommon.setData(settings, bb, DataType.BITSTRING, it.getCID());
                    if (it.getContext() == null) {
                        GXCommon.setData(settings, bb, DataType.UINT8, 0);
                    } else {
                        GXCommon.setData(settings, bb, DataType.UINT8, it.getContext().length);
                    }
                    GXCommon.setData(settings, bb, DataType.OCTET_STRING, it.getContext());
                    GXCommon.setData(settings, bb, DataType.BOOLEAN, it.getCompression());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getValidLifetime());
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 10) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getBlacklistTable() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getBlacklistTable().size(), bb);
                for (Entry<Integer, Integer> it : getBlacklistTable()) {
                    bb.setUInt8(DataType.STRUCTURE);
                    bb.setUInt8(2);
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getKey());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getValue());
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 11) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getBroadcastLogTable() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getBroadcastLogTable().size(), bb);
                for (GXDLMSBroadcastLogTable it : getBroadcastLogTable()) {
                    bb.setUInt8(DataType.STRUCTURE);
                    bb.setUInt8(3);
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getSourceAddress());
                    GXCommon.setData(settings, bb, DataType.UINT8, it.getSequenceNumber());
                    GXCommon.setData(settings, bb, DataType.UINT16, it.getValidTime());
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 12) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getGroupTable() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getGroupTable().length, bb);
                for (int it : getGroupTable()) {
                    GXCommon.setData(settings, bb, DataType.UINT16, it);
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 13) {
            return getMaxJoinWaitTime();
        }
        if (e.getIndex() == 14) {
            return getPathDiscoveryTime();
        }
        if (e.getIndex() == 15) {
            return getActiveKeyIndex();
        }
        if (e.getIndex() == 16) {
            return getMetricType();
        }
        if (e.getIndex() == 17) {
            return getCoordShortAddress();
        }
        if (e.getIndex() == 18) {
            return getDisableDefaultRouting();
        }
        if (e.getIndex() == 19) {
            return deviceType.getValue();
        }
        if (e.getIndex() == 20) {
            return getDefaultCoordRouteEnabled();
        }
        if (e.getIndex() == 21) {
            GXByteBuffer bb = new GXByteBuffer();
            bb.setUInt8(DataType.ARRAY);
            if (getDestinationAddress() == null) {
                bb.setUInt8(0);
            } else {
                GXCommon.setObjectCount(getDestinationAddress().length, bb);
                for (int it : getDestinationAddress()) {
                    GXCommon.setData(settings, bb, DataType.UINT16, it);
                }
            }
            return bb.array();
        }
        if (e.getIndex() == 22) {
            return getLowLQI();
        }
        if (e.getIndex() == 23) {
            return getHighLQI();
        }
        e.setError(ErrorCode.READ_WRITE_DENIED);
        return null;
    }

    @Override
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        if (e.getIndex() == 1) {
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
        } else if (e.getIndex() == 2) {
            setMaxHops(((Number) e.getValue()).shortValue());
        } else if (e.getIndex() == 3) {
            setWeakLqiValue(((Number) e.getValue()).shortValue());
        } else if (e.getIndex() == 4) {
            setSecurityLevel(((Number) e.getValue()).shortValue());
        } else if (e.getIndex() == 5) {
            ArrayList<Short> list = new ArrayList<Short>();
            if (e.getValue() != null) {
                for (Object it : (Iterable<?>) e.getValue()) {
                    list.add(((GXUInt8) it).shortValue());
                }
            }
            prefixTable = GXDLMSObjectHelpers.toShortArray(list);
        } else if (e.getIndex() == 6) {
            getRoutingConfiguration().clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure arr = (GXStructure) tmp;
                    GXDLMSRoutingConfiguration it = new GXDLMSRoutingConfiguration();
                    it.setNetTraversalTime(((Number) arr.get(0)).shortValue());
                    it.setRoutingTableEntryTtl(((Number) arr.get(1)).intValue());
                    it.setKr(((Number) arr.get(2)).shortValue());
                    it.setKm(((Number) arr.get(3)).shortValue());
                    it.setKc(((Number) arr.get(4)).shortValue());
                    it.setKq(((Number) arr.get(5)).shortValue());
                    it.setKh(((Number) arr.get(6)).shortValue());
                    it.setKrt(((Number) arr.get(7)).shortValue());
                    it.setRreqRetries(((Number) arr.get(8)).shortValue());
                    it.setRreqReqWait(((Number) arr.get(9)).shortValue());
                    it.setBlacklistTableEntryTtl(((Number) arr.get(10)).shortValue());
                    it.setUnicastRreqGenEnable((boolean) (arr.get(11)));
                    it.setRlcEnabled((boolean) (arr.get(12)));
                    it.setAddRevLinkCost(((Number) arr.get(13)).shortValue());
                    getRoutingConfiguration().add(it);
                }
            }
        } else if (e.getIndex() == 7) {
            broadcastLogTableEntryTtl = ((Number) e.getValue()).intValue();
        } else if (e.getIndex() == 8) {
            routingTable.clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure arr = (GXStructure) tmp;
                    GXDLMSRoutingTable it = new GXDLMSRoutingTable();
                    it.setDestinationAddress(((Number) arr.get(0)).intValue());
                    it.setNextHopAddress(((Number) arr.get(1)).intValue());
                    it.setRouteCost(((Number) arr.get(2)).intValue());
                    it.setHopCount(((Number) arr.get(3)).shortValue());
                    it.setWeakLinkCount(((Number) arr.get(4)).shortValue());
                    it.setValidTime(((Number) arr.get(5)).intValue());
                    routingTable.add(it);
                }
            }
        } else if (e.getIndex() == 9) {
            contextInformationTable.clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure arr = (GXStructure) tmp;
                    GXDLMSContextInformationTable it = new GXDLMSContextInformationTable();
                    it.setCID(((GXBitString) arr.get(0)).toString());
                    it.setContext((byte[]) arr.get(2));
                    it.setCompression((boolean) arr.get(3));
                    it.setValidLifetime(((Number) arr.get(4)).intValue());
                    contextInformationTable.add(it);
                }
            }
        } else if (e.getIndex() == 10) {
            blacklistTable.clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure arr = (GXStructure) tmp;
                    blacklistTable.add(new GXSimpleEntry<Integer, Integer>(((GXUInt16) arr.get(0)).intValue(),
                            ((GXUInt16) arr.get(1)).intValue()));
                }
            }
        } else if (e.getIndex() == 11) {
            broadcastLogTable.clear();
            if (e.getValue() != null) {
                for (Object tmp : (Iterable<?>) e.getValue()) {
                    GXStructure arr = (GXStructure) tmp;
                    GXDLMSBroadcastLogTable it = new GXDLMSBroadcastLogTable();
                    it.setSourceAddress(((GXUInt16) (arr.get(0))).intValue());
                    it.setSequenceNumber(((GXUInt8) (arr.get(1))).shortValue());
                    it.setValidTime(((GXUInt16) (arr.get(2))).intValue());
                    broadcastLogTable.add(it);
                }
            }
        } else if (e.getIndex() == 12) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            if (e.getValue() != null) {
                for (Object it : (Iterable<?>) e.getValue()) {
                    list.add(((GXUInt16) it).intValue());
                }
            }
            groupTable = GXDLMSObjectHelpers.toIntArray(list);
        } else if (e.getIndex() == 13) {
            setMaxJoinWaitTime(((GXUInt16) e.getValue()).intValue());
        } else if (e.getIndex() == 14) {
            setPathDiscoveryTime(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 15) {
            setActiveKeyIndex(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 16) {
            setMetricType(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 17) {
            setCoordShortAddress(((GXUInt16) e.getValue()).intValue());
        } else if (e.getIndex() == 18) {
            setDisableDefaultRouting((boolean) e.getValue());
        } else if (e.getIndex() == 19) {
            setDeviceType(DeviceType.forValue(((GXEnum) e.getValue()).shortValue()));
        } else if (e.getIndex() == 20) {
            setDefaultCoordRouteEnabled((boolean) e.getValue());
        } else if (e.getIndex() == 21) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            if (e.getValue() != null) {
                for (Object it : (Iterable<?>) e.getValue()) {
                    list.add(((GXUInt16) it).intValue());
                }
            }
            destinationAddress = GXDLMSObjectHelpers.toIntArray(list);
        } else if (e.getIndex() == 22) {
            setLowLQI(((GXUInt8) e.getValue()).shortValue());
        } else if (e.getIndex() == 23) {
            setHighLQI(((GXUInt8) e.getValue()).shortValue());
        } else {
            e.setError(ErrorCode.READ_WRITE_DENIED);
        }
    }

    private void loadPrefixTable(GXXmlReader reader) throws XMLStreamException {
        ArrayList<Short> list = new ArrayList<Short>();
        if (reader.isStartElement("PrefixTable", true)) {
            while (reader.isStartElement("Value", false)) {
                list.add((short) reader.readElementContentAsInt("Value", 0));
            }
            reader.readEndElement("PrefixTable");
        }
        prefixTable = GXDLMSObjectHelpers.toShortArray(list);
    }

    private void loadRoutingConfiguration(GXXmlReader reader) throws XMLStreamException {
        getRoutingConfiguration().clear();
        if (reader.isStartElement("RoutingConfiguration", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSRoutingConfiguration it = new GXDLMSRoutingConfiguration();
                getRoutingConfiguration().add(it);
                it.setNetTraversalTime((short) reader.readElementContentAsInt("NetTraversalTime"));
                it.setRoutingTableEntryTtl((int) reader.readElementContentAsInt("RoutingTableEntryTtl"));
                it.setKr((short) reader.readElementContentAsInt("Kr"));
                it.setKm((short) reader.readElementContentAsInt("Km"));
                it.setKc((short) reader.readElementContentAsInt("Kc"));
                it.setKq((short) reader.readElementContentAsInt("Kq"));
                it.setKh((short) reader.readElementContentAsInt("Kh"));
                it.setKrt((short) reader.readElementContentAsInt("Krt"));
                it.setRreqRetries((short) reader.readElementContentAsInt("RreqRetries"));
                it.setRreqReqWait((short) reader.readElementContentAsInt("RreqReqWait"));
                it.setBlacklistTableEntryTtl((int) reader.readElementContentAsInt("BlacklistTableEntryTtl"));
                it.setUnicastRreqGenEnable(reader.readElementContentAsInt("UnicastRreqGenEnable") != 0);
                it.setRlcEnabled(reader.readElementContentAsInt("RlcEnabled") != 0);
                it.setAddRevLinkCost((short) reader.readElementContentAsInt("AddRevLinkCost"));
            }
            reader.readEndElement("RoutingConfiguration");
        }
    }

    private void loadRoutingTable(GXXmlReader reader) throws XMLStreamException {
        getRoutingTable().clear();
        if (reader.isStartElement("RoutingTable", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSRoutingTable it = new GXDLMSRoutingTable();
                getRoutingTable().add(it);
                it.setDestinationAddress((int) reader.readElementContentAsInt("DestinationAddress"));
                it.setNextHopAddress((int) reader.readElementContentAsInt("NextHopAddress"));
                it.setRouteCost((int) reader.readElementContentAsInt("RouteCost"));
                it.setHopCount((short) reader.readElementContentAsInt("HopCount"));
                it.setWeakLinkCount((short) reader.readElementContentAsInt("WeakLinkCount"));
                it.setValidTime((int) reader.readElementContentAsInt("ValidTime"));
            }
            reader.readEndElement("RoutingTable");
        }
    }

    private void loadContextInformationTable(GXXmlReader reader) throws XMLStreamException {
        getContextInformationTable().clear();
        if (reader.isStartElement("ContextInformationTable", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSContextInformationTable it = new GXDLMSContextInformationTable();
                getContextInformationTable().add(it);
                it.setCID(reader.readElementContentAsString("CID"));
                it.setContext(GXDLMSTranslator.hexToBytes(reader.readElementContentAsString("Context")));
                it.setCompression(reader.readElementContentAsInt("Compression") != 0);
                it.setValidLifetime((int) reader.readElementContentAsInt("ValidLifetime"));
            }
            reader.readEndElement("ContextInformationTable");
        }
    }

    private void loadBlacklistTable(GXXmlReader reader) throws XMLStreamException {
        blacklistTable.clear();
        if (reader.isStartElement("BlacklistTable", true)) {
            while (reader.isStartElement("Item", true)) {
                int k = (int) reader.readElementContentAsInt("Key");
                int v = (int) reader.readElementContentAsInt("Value");
                blacklistTable.add(new GXSimpleEntry<Integer, Integer>(k, v));
            }
            reader.readEndElement("BlacklistTable");
        }
    }

    private void loadBroadcastLogTable(GXXmlReader reader) throws XMLStreamException {
        broadcastLogTable.clear();
        if (reader.isStartElement("BroadcastLogTable", true)) {
            while (reader.isStartElement("Item", true)) {
                GXDLMSBroadcastLogTable it = new GXDLMSBroadcastLogTable();
                getBroadcastLogTable().add(it);
                it.setSourceAddress((int) reader.readElementContentAsInt("SourceAddress"));
                it.setSequenceNumber((short) reader.readElementContentAsInt("SequenceNumber"));
                it.setValidTime((int) reader.readElementContentAsInt("ValidTime"));
            }
            reader.readEndElement("BroadcastLogTable");
        }
    }

    private void loadGroupTable(GXXmlReader reader) throws XMLStreamException {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (reader.isStartElement("GroupTable", true)) {
            while (reader.isStartElement("Value", false)) {
                list.add(reader.readElementContentAsInt("Value"));
            }
            reader.readEndElement("GroupTable");
        }
        groupTable = GXDLMSObjectHelpers.toIntArray(list);
    }

    private void loadDestinationAddress(GXXmlReader reader) throws XMLStreamException {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (reader.isStartElement("DestinationAddress", true)) {
            while (reader.isStartElement("Value", false)) {
                list.add(reader.readElementContentAsInt("Value"));
            }
            reader.readEndElement("DestinationAddress");
        }
        destinationAddress = GXDLMSObjectHelpers.toIntArray(list);
    }

    public final void load(GXXmlReader reader) throws XMLStreamException {
        maxHops = (short) reader.readElementContentAsInt("MaxHops");
        weakLqiValue = (short) reader.readElementContentAsInt("WeakLqiValue");
        securityLevel = (short) reader.readElementContentAsInt("SecurityLevel");
        loadPrefixTable(reader);
        loadRoutingConfiguration(reader);
        broadcastLogTableEntryTtl = reader.readElementContentAsInt("BroadcastLogTableEntryTtl");
        loadRoutingTable(reader);
        loadContextInformationTable(reader);
        loadBlacklistTable(reader);
        loadBroadcastLogTable(reader);
        loadGroupTable(reader);
        maxJoinWaitTime = (int) reader.readElementContentAsInt("MaxJoinWaitTime");
        pathDiscoveryTime = (short) reader.readElementContentAsInt("PathDiscoveryTime");
        activeKeyIndex = (short) reader.readElementContentAsInt("ActiveKeyIndex");
        metricType = (short) reader.readElementContentAsInt("MetricType");
        coordShortAddress = reader.readElementContentAsInt("CoordShortAddress");
        disableDefaultRouting = reader.readElementContentAsInt("DisableDefaultRouting") != 0;
        deviceType = DeviceType.forValue(reader.readElementContentAsInt("DeviceType"));
        defaultCoordRouteEnabled = reader.readElementContentAsInt("DefaultCoordRouteEnabled") != 0;
        loadDestinationAddress(reader);
        lowLQI = (short) reader.readElementContentAsInt("LowLQI");
        highLQI = (short) reader.readElementContentAsInt("HighLQI");
    }

    private void savePrefixTable(GXXmlWriter writer) throws XMLStreamException {
        if (getPrefixTable() != null) {
            writer.writeStartElement("PrefixTable");
            for (Object it : getPrefixTable()) {
                writer.writeElementObject("Value", it);
            }
            writer.writeEndElement();
        }
    }

    private void saveRoutingConfiguration(GXXmlWriter writer) throws XMLStreamException {
        if (getRoutingConfiguration() != null) {
            writer.writeStartElement("RoutingConfiguration");
            for (GXDLMSRoutingConfiguration it : getRoutingConfiguration()) {
                writer.writeStartElement("Item");
                writer.writeElementString("NetTraversalTime", it.getNetTraversalTime());
                writer.writeElementString("RoutingTableEntryTtl", it.getRoutingTableEntryTtl());
                writer.writeElementString("Kr", it.getKr());
                writer.writeElementString("Km", it.getKm());
                writer.writeElementString("Kc", it.getKc());
                writer.writeElementString("Kq", it.getKq());
                writer.writeElementString("Kh", it.getKh());
                writer.writeElementString("Krt", it.getKrt());
                writer.writeElementString("RreqRetries", it.getRreqRetries());
                writer.writeElementString("RreqReqWait", it.getRreqReqWait());
                writer.writeElementString("BlacklistTableEntryTtl", it.getBlacklistTableEntryTtl());
                writer.writeElementString("UnicastRreqGenEnable", it.getUnicastRreqGenEnable());
                writer.writeElementString("RlcEnabled", it.getRlcEnabled());
                writer.writeElementString("AddRevLinkCost", it.getAddRevLinkCost());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveRoutingTable(GXXmlWriter writer) throws XMLStreamException {
        if (getRoutingTable() != null) {
            writer.writeStartElement("RoutingTable");
            for (GXDLMSRoutingTable it : getRoutingTable()) {
                writer.writeStartElement("Item");
                writer.writeElementString("DestinationAddress", it.getDestinationAddress());
                writer.writeElementString("NextHopAddress", it.getNextHopAddress());
                writer.writeElementString("RouteCost", it.getRouteCost());
                writer.writeElementString("HopCount", it.getHopCount());
                writer.writeElementString("WeakLinkCount", it.getWeakLinkCount());
                writer.writeElementString("ValidTime", it.getValidTime());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveContextInformationTable(GXXmlWriter writer) throws XMLStreamException {
        if (getContextInformationTable() != null) {
            writer.writeStartElement("ContextInformationTable");
            for (GXDLMSContextInformationTable it : getContextInformationTable()) {
                writer.writeStartElement("Item");
                writer.writeElementString("CID", it.getCID());
                writer.writeElementString("Context", GXDLMSTranslator.toHex(it.getContext()));
                writer.writeElementString("Compression", it.getCompression());
                writer.writeElementString("ValidLifetime", it.getValidLifetime());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveBlacklistTable(GXXmlWriter writer) throws XMLStreamException {
        if (getBlacklistTable() != null) {
            writer.writeStartElement("BlacklistTable");
            for (Entry<Integer, Integer> it : blacklistTable) {
                writer.writeStartElement("Item");
                writer.writeElementObject("Key", it.getKey());
                writer.writeElementObject("Value", it.getValue());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveBroadcastLogTable(GXXmlWriter writer) throws XMLStreamException {
        if (getBroadcastLogTable() != null) {
            writer.writeStartElement("BroadcastLogTable");
            for (GXDLMSBroadcastLogTable it : getBroadcastLogTable()) {
                writer.writeStartElement("Item");
                writer.writeElementObject("SourceAddress", it.getSourceAddress());
                writer.writeElementObject("SequenceNumber", it.getSequenceNumber());
                writer.writeElementObject("ValidTime", it.getValidTime());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private void saveGroupTable(GXXmlWriter writer) throws XMLStreamException {
        if (getGroupTable() != null) {
            writer.writeStartElement("GroupTable");
            for (int it : getGroupTable()) {
                writer.writeElementObject("Value", it);
            }
            writer.writeEndElement();
        }
    }

    private void saveDestinationAddress(GXXmlWriter writer) throws XMLStreamException {
        if (getDestinationAddress() != null) {
            writer.writeStartElement("DestinationAddress");
            for (int it : getDestinationAddress()) {
                writer.writeElementObject("Value", it);
            }
            writer.writeEndElement();
        }
    }

    @Override
    public final void save(GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("MaxHops", getMaxHops());
        writer.writeElementString("WeakLqiValue", getWeakLqiValue());
        writer.writeElementString("SecurityLevel", getSecurityLevel());
        savePrefixTable(writer);
        saveRoutingConfiguration(writer);
        writer.writeElementString("BroadcastLogTableEntryTtl", getBroadcastLogTableEntryTtl());
        saveRoutingTable(writer);
        saveContextInformationTable(writer);
        saveBlacklistTable(writer);
        saveBroadcastLogTable(writer);
        saveGroupTable(writer);
        writer.writeElementString("MaxJoinWaitTime", getMaxJoinWaitTime());
        writer.writeElementString("PathDiscoveryTime", getPathDiscoveryTime());
        writer.writeElementString("ActiveKeyIndex", getActiveKeyIndex());
        writer.writeElementString("MetricType", getMetricType());
        writer.writeElementString("CoordShortAddress", getCoordShortAddress());
        writer.writeElementString("DisableDefaultRouting", getDisableDefaultRouting());
        writer.writeElementString("DeviceType", getDeviceType().getValue());
        writer.writeElementString("DefaultCoordRouteEnabled", getDefaultCoordRouteEnabled());
        saveDestinationAddress(writer);
        writer.writeElementString("LowLQI", getLowLQI());
        writer.writeElementString("HighLQI", getHighLQI());
    }

    @Override
    public final void postLoad(GXXmlReader reader) {
    }
}
