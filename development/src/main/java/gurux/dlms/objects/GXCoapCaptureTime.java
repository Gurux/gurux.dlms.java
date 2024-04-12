package gurux.dlms.objects;

import gurux.dlms.GXDateTime;

/**
 * CoAP Capture time.
 */
public class GXCoapCaptureTime {
    /**
     * The most recent change attribute Id (3, 4 or 5).
     */
    private byte attributeId;
    /**
     * Time stamp.
     */
    private GXDateTime timeStamp;

    /**
     * @return The most recent change attribute Id (3, 4 or 5).
     */
    public final byte getAttributeId() {
        return attributeId;
    }

    /**
     * @param value
     *            The most recent change attribute Id (3, 4 or 5).
     */
    public final void setAttributeId(final byte value) {
        attributeId = value;
    }

    /**
     * @return Time stamp.
     */
    public final GXDateTime getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param value
     *            Time stamp.
     */
    public final void setTimeStamp(final GXDateTime value) {
        timeStamp = value;
    }
}