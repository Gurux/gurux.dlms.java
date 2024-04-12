package gurux.dlms.objects;

/**
 * The routing configuration element specifies all parameters linked to the
 * routing mechanism described in ITU-T G.9903:2014.
 */
public class GXDLMSRoutingConfiguration {
    /**
     * Maximum time that a packet is expected to take to reach any node from any
     * node in seconds. PIB attribute: 0x11.
     */
    private short netTraversalTime;

    /**
     * Maximum time-to-live of a routing table entry (in minutes). PIB
     * attribute: 0x12.
     */
    private int routingTableEntryTtl;

    /**
     * A weight factor for the Robust Mode to calculate link cost. PIB
     * attribute: 0x13.
     */
    private short kr;

    /**
     * A weight factor for modulation to calculate link cost. PIB attribute:
     * 0x14.
     */
    private short km;

    /**
     * A weight factor for number of active tones to calculate link cost. PIB
     * attribute: 0x15.
     */
    private short kc;

    /**
     * A weight factor for LQI to calculate route cost. PIB attribute: 0x16.
     */
    private short kq;

    /**
     * A weight factor for hop to calculate link cost. PIB attribute: 0x17.
     */
    private short kh;
    /**
     * A weight factor for the number of active routes in the routing table to
     * calculate link cost. PIB attribute: 0x1B.
     */
    private short krt;
    /**
     * The number of RREQ retransmission in case of RREP reception time out. PIB
     * attribute: 0x18.
     */
    private short rreqRetries;

    /**
     * The number of seconds to wait between two consecutive RREQ – RERR
     * generations. PIB attribute: 0x19.
     */
    private short rreqReqWait;

    /**
     * Maximum time-to-live of a blacklisted neighbour entry (in minutes). PIB
     * attribute: 0x1F.
     */
    private int blacklistTableEntryTtl;
    /**
     * If TRUE, the RREQ shall be generated with its 'unicast RREQ'. PIB
     * attribute: 0x0D.
     */
    private boolean unicastRreqGenEnable;
    /**
     * Enable the sending of RLCREQ frame by the device. PIB attribute: 0x09.
     */
    private boolean rlcEnabled;
    /**
     * It represents an additional cost to take into account a possible
     * asymmetry in the link. PIB attribute: 0x0A.
     */
    private short addRevLinkCost;

    /**
     * @return Maximum time that a packet is expected to take to reach any node
     *         from any node in seconds. PIB attribute: 0x11.
     */
    public final short getNetTraversalTime() {
        return netTraversalTime;
    }

    /**
     * @param value
     *            Maximum time that a packet is expected to take to reach any
     *            node from any node in seconds. PIB attribute: 0x11.
     */
    public final void setNetTraversalTime(final short value) {
        netTraversalTime = value;
    }

    /**
     * @return Maximum time-to-live of a routing table entry (in minutes). PIB
     *         attribute: 0x12.
     */
    public final int getRoutingTableEntryTtl() {
        return routingTableEntryTtl;
    }

    /**
     * @param value
     *            Maximum time-to-live of a routing table entry (in minutes).
     *            PIB attribute: 0x12.
     */
    public final void setRoutingTableEntryTtl(final int value) {
        routingTableEntryTtl = value;
    }

    /**
     * @return A weight factor for the Robust Mode to calculate link cost. PIB
     *         attribute: 0x13.
     */
    public final short getKr() {
        return kr;
    }

    /**
     * @param value
     *            A weight factor for the Robust Mode to calculate link cost.
     *            PIB attribute: 0x13.
     */
    public final void setKr(final short value) {
        kr = value;
    }

    /**
     * @return A weight factor for modulation to calculate link cost. PIB
     *         attribute: 0x14.
     */
    public final short getKm() {
        return km;
    }

    /**
     * @param value
     *            A weight factor for modulation to calculate link cost. PIB
     *            attribute: 0x14.
     */
    public final void setKm(final short value) {
        km = value;
    }

    /**
     * @return A weight factor for number of active tones to calculate link
     *         cost. PIB attribute: 0x15.
     */
    public final short getKc() {
        return kc;
    }

    /**
     * @param value
     *            A weight factor for number of active tones to calculate link
     *            cost. PIB attribute: 0x15.
     */
    public final void setKc(final short value) {
        kc = value;
    }

    /**
     * @return A weight factor for LQI to calculate route cost. PIB attribute:
     *         0x16.
     */
    public final short getKq() {
        return kq;
    }

    /**
     * @param value
     *            A weight factor for LQI to calculate route cost. PIB
     *            attribute: 0x16.
     */
    public final void setKq(final short value) {
        kq = value;
    }

    /**
     * @return A weight factor for hop to calculate link cost. PIB attribute:
     *         0x17.
     */
    public final short getKh() {
        return kh;
    }

    /**
     * @param value
     *            A weight factor for hop to calculate link cost. PIB attribute:
     *            0x17.
     */
    public final void setKh(final short value) {
        kh = value;
    }

    /**
     * @return A weight factor for the number of active routes in the routing
     *         table to calculate link cost. PIB attribute: 0x1B.
     */
    public final short getKrt() {
        return krt;
    }

    /**
     * @param value
     *            A weight factor for the number of active routes in the routing
     *            table to calculate link cost. PIB attribute: 0x1B.
     */
    public final void setKrt(final short value) {
        krt = value;
    }

    /**
     * @return The number of RREQ retransmission in case of RREP reception time
     *         out. PIB attribute: 0x18.
     */
    public final short getRreqRetries() {
        return rreqRetries;
    }

    /**
     * @param value
     *            The number of RREQ retransmission in case of RREP reception
     *            time out. PIB attribute: 0x18.
     */
    public final void setRreqRetries(final short value) {
        rreqRetries = value;
    }

    /**
     * @return The number of seconds to wait between two consecutive RREQ – RERR
     *         generations. PIB attribute: 0x19.
     */
    public final short getRreqReqWait() {
        return rreqReqWait;
    }

    /**
     * @param value
     *            The number of seconds to wait between two consecutive RREQ –
     *            RERR generations. PIB attribute: 0x19.
     */
    public final void setRreqReqWait(final short value) {
        rreqReqWait = value;
    }

    /**
     * @return Maximum time-to-live of a blacklisted neighbour entry (in
     *         minutes). PIB attribute: 0x1F.
     */
    public final int getBlacklistTableEntryTtl() {
        return blacklistTableEntryTtl;
    }

    /**
     * @param value
     *            Maximum time-to-live of a blacklisted neighbour entry (in
     *            minutes). PIB attribute: 0x1F.
     */
    public final void setBlacklistTableEntryTtl(int value) {
        blacklistTableEntryTtl = value;
    }

    /**
     * @return If TRUE, the RREQ shall be generated with its 'unicast RREQ'. PIB
     *         attribute: 0x0D.
     */
    public final boolean getUnicastRreqGenEnable() {
        return unicastRreqGenEnable;
    }

    /**
     * @param value
     *            If TRUE, the RREQ shall be generated with its 'unicast RREQ'.
     *            PIB attribute: 0x0D.
     */
    public final void setUnicastRreqGenEnable(boolean value) {
        unicastRreqGenEnable = value;
    }

    /**
     * @return Enable the sending of RLCREQ frame by the device. PIB attribute:
     *         0x09.
     */
    public final boolean getRlcEnabled() {
        return rlcEnabled;
    }

    /**
     * @param value
     *            Enable the sending of RLCREQ frame by the device. PIB
     *            attribute: 0x09.
     */
    public final void setRlcEnabled(boolean value) {
        rlcEnabled = value;
    }

    /**
     * @return It represents an additional cost to take into account a possible
     *         asymmetry in the link. PIB attribute: 0x0A.
     */
    public final short getAddRevLinkCost() {
        return addRevLinkCost;
    }

    /**
     * @param value
     *            It represents an additional cost to take into account a
     *            possible asymmetry in the link. PIB attribute: 0x0A.
     */
    public final void setAddRevLinkCost(final short value) {
        addRevLinkCost = value;
    }
}