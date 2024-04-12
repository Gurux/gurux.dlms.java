package gurux.dlms.objects;

/**
 * Contains the context information associated to each CID extension field.
 */
public class GXDLMSContextInformationTable {
    /**
     * Corresponds to the 4-bit context information used for source and
     * destination addresses (SCI, DCI).
     */
    private String cid;

    /**
     * Context.
     */
    private byte[] context;

    /**
     * Indicates if the context is valid for use in compression.
     */
    private boolean compression;

    /**
     * Remaining time in minutes during which the context information table is
     * considered valid. It is updated upon reception of the advertised context.
     */
    private int validLifetime;

    /**
     * @return Corresponds to the 4-bit context information used for source and
     *         destination addresses (SCI, DCI).
     */
    public final String getCID() {
        return cid;
    }

    /**
     * @param value
     *            Corresponds to the 4-bit context information used for source
     *            and destination addresses (SCI, DCI).
     */
    public final void setCID(final String value) {
        cid = value;
    }

    /**
     * @return Context.
     */
    public final byte[] getContext() {
        return context;
    }

    /**
     * @param value
     *            Context.
     */
    public final void setContext(final byte[] value) {
        context = value;
    }

    /**
     * @return Indicates if the context is valid for use in compression.
     */
    public final boolean getCompression() {
        return compression;
    }

    /**
     * @param value
     *            Indicates if the context is valid for use in compression.
     */
    public final void setCompression(final boolean value) {
        compression = value;
    }

    /**
     * @return Remaining time in minutes during which the context information
     *         table is considered valid. It is updated upon reception of the
     *         advertised context.
     */
    public final int getValidLifetime() {
        return validLifetime;
    }

    /**
     * @param value
     *            Remaining time in minutes during which the context information
     *            table is considered valid. It is updated upon reception of the
     *            advertised context.
     */
    public final void setValidLifetime(final int value) {
        validLifetime = value;
    }
}