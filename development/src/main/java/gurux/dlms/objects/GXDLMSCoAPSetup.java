package gurux.dlms.objects;

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXEnum;
import gurux.dlms.GXUInt16;
import gurux.dlms.GXUInt8;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.TransportMode;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSCoAPSetup
 */
public class GXDLMSCoAPSetup extends GXDLMSObject implements IGXDLMSBase {
    /**
     * TCP-UDP setup object.
     */
    private GXDLMSTcpUdpSetup udpReference;

    /**
     * The minimum initial ACK timeout in milliseconds.
     */
    private int ackTimeout;

    /**
     * The random factor to apply for randomness of the initial ACK timeout.
     */
    private int ackRandomFactor;
    /**
     * The maximum number of retransmissions for a confirmable message.
     */
    private int maxRetransmit;
    /**
     * The amount of simultaneous outstanding CoAP request messages.
     */
    private int nStart;
    /**
     * Delay acknowledge timeout in milliseconds.
     */
    private int delayAckTimeout;
    /**
     * Exponential back off.
     */
    private int exponentialBackOff;
    /**
     * Probing rate.
     */
    private int probingRate;
    /**
     * CoAP Uri path.
     */
    private String coAPUriPath;

    /**
     * CoAP transport mode.
     */
    private TransportMode transportMode;

    /**
     * The version of the DLMS/COSEM CoAP wrapper.
     */
    private Object wrapperVersion;
    /**
     * The length of the Token.
     */
    private short tokenLength;

    /**
     * Constructor.
     */
    public GXDLMSCoAPSetup() {
        this("0.0.25.16.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSCoAPSetup(String ln) {
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
    public GXDLMSCoAPSetup(String ln, int sn) {
        super(ObjectType.COAP_SETUP, ln, sn);
    }

    /**
     * @return TCP-UDP setup object.
     */
    public final GXDLMSTcpUdpSetup getUdpReference() {
        return udpReference;
    }

    /**
     * @param value
     *            TCP-UDP setup object.
     */
    public final void setUdpReference(GXDLMSTcpUdpSetup value) {
        udpReference = value;
    }

    /**
     * @return The minimum initial ACK timeout in milliseconds.
     */
    public final int getAckTimeout() {
        return ackTimeout;
    }

    /**
     * @param value
     *            The minimum initial ACK timeout in milliseconds.
     */
    public final void setAckTimeout(final int value) {
        ackTimeout = value;
    }

    /**
     * @return The random factor to apply for randomness of the initial ACK
     *         timeout.
     */
    public final int getAckRandomFactor() {
        return ackRandomFactor;
    }

    /**
     * @param value
     *            The random factor to apply for randomness of the initial ACK
     *            timeout.
     */
    public final void setAckRandomFactor(final int value) {
        ackRandomFactor = value;
    }

    /**
     * @return The maximum number of retransmissions for a confirmable message.
     */
    public final int getMaxRetransmit() {
        return maxRetransmit;
    }

    /**
     * @param value
     *            The maximum number of retransmissions for a confirmable
     *            message.
     */
    public final void setMaxRetransmit(final int value) {
        maxRetransmit = value;
    }

    /**
     * @return The amount of simultaneous outstanding CoAP request messages.
     */
    public final int getNStart() {
        return nStart;
    }

    /**
     * @param value
     *            The amount of simultaneous outstanding CoAP request messages.
     */
    public final void setNStart(final int value) {
        nStart = value;
    }

    /**
     * @return Delay acknowledge timeout in milliseconds.
     */
    public final int getDelayAckTimeout() {
        return delayAckTimeout;
    }

    /**
     * @param value
     *            Delay acknowledge timeout in milliseconds.
     */
    public final void setDelayAckTimeout(int value) {
        delayAckTimeout = value;
    }

    /**
     * @return Exponential back off.
     */
    public final int getExponentialBackOff() {
        return exponentialBackOff;
    }

    /**
     * @param value
     *            Exponential back off.
     */
    public final void setExponentialBackOff(final int value) {
        exponentialBackOff = value;
    }

    /**
     * @return Probing rate.
     */
    public final int getProbingRate() {
        return probingRate;
    }

    /**
     * @param value
     *            Probing rate.
     */
    public final void setProbingRate(final int value) {
        probingRate = value;
    }

    /**
     * @return CoAP Uri path.
     */
    public final String getCoAPUriPath() {
        return coAPUriPath;
    }

    /**
     * @param value
     *            CoAP Uri path.
     */
    public final void setCoAPUriPath(final String value) {
        coAPUriPath = value;
    }

    /**
     * @return CoAP transport mode.
     */
    public final TransportMode getTransportMode() {
        return transportMode;
    }

    /**
     * @param value
     *            CoAP transport mode.
     */
    public final void setTransportMode(final TransportMode value) {
        transportMode = value;
    }

    /**
     * @return The version of the DLMS/COSEM CoAP wrapper.
     */
    public final Object getWrapperVersion() {
        return wrapperVersion;
    }

    /**
     * @param value
     *            The version of the DLMS/COSEM CoAP wrapper.
     */
    public final void setWrapperVersion(final Object value) {
        wrapperVersion = value;
    }

    /**
     * @return The length of the Token.
     */
    public final short getTokenLength() {
        return tokenLength;
    }

    /**
     * @param value
     *            The length of the Token.
     */
    public final void setTokenLength(short value) {
        tokenLength = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), getUdpReference(), getAckTimeout(), getAckRandomFactor(),
                getMaxRetransmit(), getNStart(), getDelayAckTimeout(), getExponentialBackOff(), getProbingRate(),
                getCoAPUriPath(), getTransportMode(), getWrapperVersion(), getTokenLength() };
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // UdpReference,
        if (all || canRead(2)) {
            attributes.add(2);
        }
        // AckTimeout
        if (all || canRead(3)) {
            attributes.add(3);
        }
        // AckRandomFactor
        if (all || canRead(4)) {
            attributes.add(4);
        }
        // MaxRetransmit
        if (all || canRead(5)) {
            attributes.add(5);
        }
        // NStart
        if (all || canRead(6)) {
            attributes.add(6);
        }
        // DelayAckTimeout
        if (all || canRead(7)) {
            attributes.add(7);
        }
        // ExponentialBackOff
        if (all || canRead(8)) {
            attributes.add(8);
        }
        // ProbingRate
        if (all || canRead(9)) {
            attributes.add(9);
        }
        // CoAPUriPath
        if (all || canRead(10)) {
            attributes.add(10);
        }
        // TransportMode
        if (all || canRead(11)) {
            attributes.add(11);
        }
        // WrapperVersion
        if (all || canRead(12)) {
            attributes.add(12);
        }
        // TokenLength
        if (all || canRead(13)) {
            attributes.add(13);
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "Value", "UdpReference", "AckTimeout", "AckRandomFactor", "MaxRetransmit",
                "NStart", "DelayAckTimeout", "ExponentialBackOff", "ProbingRate", "CoAPUriPath", "TransportMode",
                "WrapperVersion", "TokenLength" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[0];
    }

    @Override
    public final int getAttributeCount() {
        return 13;
    }

    @Override
    public final int getMethodCount() {
        return 0;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
        case 2:
        case 10:
            return DataType.OCTET_STRING;
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
            return DataType.UINT16;
        case 11:
            return DataType.ENUM;
        case 12:
            return super.getDataType(index);
        case 13:
            return DataType.UINT8;
        default:
            throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
        }
    }

    @Override
    public final Object getValue(GXDLMSSettings settings, ValueEventArgs e) {
        Object ret;
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            if (getUdpReference() != null) {
                ret = GXCommon.logicalNameToBytes(getUdpReference().getLogicalName());
            } else {
                ret = null;
            }
            break;
        case 3:
            ret = getAckTimeout();
            break;
        case 4:
            ret = getAckRandomFactor();
            break;
        case 5:
            ret = getMaxRetransmit();
            break;
        case 6:
            ret = getNStart();
            break;
        case 7:
            ret = getDelayAckTimeout();
            break;
        case 8:
            ret = getExponentialBackOff();
            break;
        case 9:
            ret = getProbingRate();
            break;
        case 10:
            ret = getCoAPUriPath().getBytes();
            break;
        case 11:
            ret = transportMode.getValue();
            break;
        case 12:
            ret = getWrapperVersion();
            break;
        case 13:
            ret = getTokenLength();
            break;
        default:
            ret = null;
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return ret;
    }

    @Override
    public final void setValue(GXDLMSSettings settings, ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2: {
            setUdpReference(null);
            String ln = GXCommon.toLogicalName(e.getValue());
            setUdpReference((GXDLMSTcpUdpSetup) settings.getObjects().findByLN(ObjectType.TCP_UDP_SETUP, ln));
            if (getUdpReference() == null) {
                setUdpReference(new GXDLMSTcpUdpSetup(ln));
            }
        }
            break;
        case 3:
            setAckTimeout(((GXUInt16) e.getValue()).intValue());
            break;
        case 4:
            setAckRandomFactor(((GXUInt16) e.getValue()).intValue());
            break;
        case 5:
            setMaxRetransmit(((GXUInt16) e.getValue()).intValue());
            break;
        case 6:
            setNStart(((GXUInt16) e.getValue()).intValue());
            break;
        case 7:
            setDelayAckTimeout(((GXUInt16) e.getValue()).intValue());
            break;
        case 8:
            setExponentialBackOff(((GXUInt16) e.getValue()).intValue());
            break;
        case 9:
            setProbingRate(((GXUInt16) e.getValue()).intValue());
            break;
        case 10: {
            byte[] tmp = (byte[]) e.getValue();
            if (tmp != null) {
                setCoAPUriPath(new String(tmp));
            } else {
                setCoAPUriPath(null);
            }
            break;
        }
        case 11:
            setTransportMode(TransportMode.forValue(((GXEnum) e.getValue()).shortValue()));
            break;
        case 12:
            setWrapperVersion(e.getValue());
            break;
        case 13:
            setTokenLength(((GXUInt8) e.getValue()).shortValue());
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(GXXmlReader reader) throws XMLStreamException {
        setUdpReference(null);
        String ln = reader.readElementContentAsString("UdpReference");
        if (ln != null && ln != "") {
            setUdpReference((GXDLMSTcpUdpSetup) reader.getObjects().findByLN(ObjectType.TCP_UDP_SETUP, ln));
            if (getUdpReference() == null) {
                setUdpReference(new GXDLMSTcpUdpSetup(ln));
            }
        }
        setAckTimeout((short) reader.readElementContentAsInt("AckTimeout"));
        setAckRandomFactor((short) reader.readElementContentAsInt("AckRandomFactor"));
        setMaxRetransmit((short) reader.readElementContentAsInt("MaxRetransmit"));
        setNStart((short) reader.readElementContentAsInt("NStart"));
        setDelayAckTimeout((short) reader.readElementContentAsInt("DelayAckTimeout"));
        setExponentialBackOff((short) reader.readElementContentAsInt("ExponentialBackOff"));
        setProbingRate((short) reader.readElementContentAsInt("ProbingRate"));
        setCoAPUriPath(reader.readElementContentAsString("CoAPUriPath"));
        setTransportMode(TransportMode.forValue(reader.readElementContentAsInt("TransportMode")));
        setWrapperVersion(reader.readElementContentAsObject("WrapperVersion", null, this, 12));
        setTokenLength((byte) reader.readElementContentAsInt("TokenLength"));
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        if (getUdpReference() != null) {
            writer.writeElementString("UdpReference", getUdpReference().getLogicalName());
        }
        writer.writeElementString("AckTimeout", getAckTimeout());
        writer.writeElementString("AckRandomFactor", getAckRandomFactor());
        writer.writeElementString("MaxRetransmit", getMaxRetransmit());
        writer.writeElementString("NStart", getNStart());
        writer.writeElementString("DelayAckTimeout", getDelayAckTimeout());
        writer.writeElementString("ExponentialBackOff", getExponentialBackOff());
        writer.writeElementString("ProbingRate", getProbingRate());
        writer.writeElementString("CoAPUriPath", getCoAPUriPath());
        writer.writeElementString("TransportMode", getTransportMode().getValue());
        DataType dt = getDataType(2);
        if (getWrapperVersion() != null && dt == DataType.NONE) {
            dt = GXDLMSConverter.getDLMSDataType(getWrapperVersion());
        }
        writer.writeElementObject("WrapperVersion", getWrapperVersion(), dt, getUIDataType(2));
        writer.writeElementString("TokenLength", getTokenLength());
    }

    @Override
    public final void postLoad(final GXXmlReader reader) {
        if (getUdpReference() != null) {
            GXDLMSTcpUdpSetup target = (GXDLMSTcpUdpSetup) reader.getObjects().findByLN(ObjectType.TCP_UDP_SETUP,
                    getUdpReference().getLogicalName());
            if (target != null && target != getUdpReference()) {
                setUdpReference(target);
            }
        }
    }
}
