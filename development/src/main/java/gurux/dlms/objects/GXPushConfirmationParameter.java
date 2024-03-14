package gurux.dlms.objects;

import gurux.dlms.GXDateTime;

/**
 * Push confirmation parameters.
 */
public class GXPushConfirmationParameter {
    /**
     * Confirmation start date. Fields of date-time not specified are not used.
     */
    private GXDateTime startDate;
    /**
     * Confirmation time interval in seconds. Disabled, if zero.
     */
    private long interval;

    /**
     * @return Confirmation start date. Fields of date-time not specified are
     *         not used.
     */
    public final GXDateTime getStartDate() {
        return startDate;
    }

    /**
     * @param value
     *            Confirmation start date. Fields of date-time not specified are
     *            not used.
     */
    public final void setStartDate(final GXDateTime value) {
        startDate = value;
    }

    /**
     * @return Confirmation time interval in seconds. Disabled, if zero.
     */
    public final long getInterval() {
        return interval;
    }

    /**
     * @param value
     *            Confirmation time interval in seconds. Disabled, if zero.
     */
    public final void setInterval(final long value) {
        interval = value;
    }
}