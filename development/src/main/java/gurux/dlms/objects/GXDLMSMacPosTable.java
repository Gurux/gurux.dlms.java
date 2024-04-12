package gurux.dlms.objects;

public class GXDLMSMacPosTable {
    /**
     * The 16-bit address the device is using to communicate through the PAN.
     * PIB attribute 0x53.
     */
    private int shortAddress;

    /**
     * Link Quality Indicator.
     */
    private short lqi;
    /**
     * Valid time.
     */
    private int validTime;

    /**
     * @return The 16-bit address the device is using to communicate through the
     *         PAN. PIB attribute 0x53.
     */
    public final int getShortAddress() {
        return shortAddress;
    }

    /**
     * @param value
     *            The 16-bit address the device is using to communicate through
     *            the PAN. PIB attribute 0x53.
     */
    public final void setShortAddress(final int value) {
        shortAddress = value;
    }

    /**
     * @return Link Quality Indicator.
     */
    public final short getLQI() {
        return lqi;
    }

    /**
     * @param value
     *            Link Quality Indicator.
     */
    public final void setLQI(final short value) {
        lqi = value;
    }

    /**
     * @return Valid time.
     */
    public final int getValidTime() {
        return validTime;
    }

    /**
     * @param value
     *            Valid time.
     */
    public final void setValidTime(final int value) {
        validTime = value;
    }
}