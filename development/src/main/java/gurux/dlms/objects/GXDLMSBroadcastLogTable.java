package gurux.dlms.objects;

/**
 * Broadcast log table
 */
public class GXDLMSBroadcastLogTable {
    /**
     * Source address of a broadcast packet.
     */
    private int sourceAddress;

    /**
     * The sequence number contained in the BC0 header
     */
    private short sequenceNumber;
    /**
     * Remaining time in minutes until when this entry in the broadcast log
     * table is considered valid.
     */
    private int validTime;

    /**
     * @return Source address of a broadcast packet.
     */
    public final int getSourceAddress() {
        return sourceAddress;
    }

    /**
     * @param value
     *            Source address of a broadcast packet.
     */
    public final void setSourceAddress(final int value) {
        sourceAddress = value;
    }

    /**
     * @return The sequence number contained in the BC0 header
     */
    public final short getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param value
     *            The sequence number contained in the BC0 header
     */
    public final void setSequenceNumber(final short value) {
        sequenceNumber = value;
    }

    /**
     * @return Remaining time in minutes until when this entry in the broadcast
     *         log table is considered valid.
     */
    public final int getValidTime() {
        return validTime;
    }

    /**
     * @param value
     *            Remaining time in minutes until when this entry in the
     *            broadcast log table is considered valid.
     */
    public final void setValidTime(final int value) {
        validTime = value;
    }
}