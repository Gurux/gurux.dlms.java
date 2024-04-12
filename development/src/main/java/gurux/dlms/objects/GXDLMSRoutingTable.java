package gurux.dlms.objects;

/**
 * G3-PLC 6LoWPAN routing table.
 */
public class GXDLMSRoutingTable {
    /**
     * Address of the destination.
     */
    private int destinationAddress;

    /**
     * Address of the next hop on the route towards the destination.
     */
    private int nextHopAddress;

    /**
     * Cumulative link cost along the route towards the destination.
     */
    private int routeCost;

    /**
     * Number of hops of the selected route to the destination.
     */
    private short hopCount;

    /**
     * Number of weak links to destination.
     */
    private short weakLinkCount;

    /**
     * Remaining time in minutes until when this entry in the routing table is
     * considered valid.
     */
    private int validTime;

    /**
     * @return Address of the destination.
     */
    public final int getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param value
     *            Address of the destination.
     */
    public final void setDestinationAddress(final int value) {
        destinationAddress = value;
    }

    /**
     * @return Address of the next hop on the route towards the destination.
     */
    public final int getNextHopAddress() {
        return nextHopAddress;
    }

    /**
     * @param value
     *            Address of the next hop on the route towards the destination.
     */
    public final void setNextHopAddress(final int value) {
        nextHopAddress = value;
    }

    /**
     * @return Cumulative link cost along the route towards the destination.
     */
    public final int getRouteCost() {
        return routeCost;
    }

    /**
     * @param value
     *            Cumulative link cost along the route towards the destination.
     */
    public final void setRouteCost(final int value) {
        routeCost = value;
    }

    /**
     * @return Number of hops of the selected route to the destination.
     */
    public final short getHopCount() {
        return hopCount;
    }

    /**
     * @param value
     *            Number of hops of the selected route to the destination.
     */
    public final void setHopCount(final short value) {
        hopCount = value;
    }

    /**
     * @return Number of weak links to destination.
     */
    public final short getWeakLinkCount() {
        return weakLinkCount;
    }

    /**
     * @param value
     *            Number of weak links to destination.
     */
    public final void setWeakLinkCount(final short value) {
        weakLinkCount = value;
    }

    /**
     * @return Remaining time in minutes until when this entry in the routing
     *         table is considered valid.
     */
    public final int getValidTime() {
        return validTime;
    }

    /**
     * @param value
     *            Remaining time in minutes until when this entry in the routing
     *            table is considered valid.
     */
    public final void setValidTime(final int value) {
        validTime = value;
    }
}