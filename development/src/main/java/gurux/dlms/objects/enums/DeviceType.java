package gurux.dlms.objects.enums;

/**
 * Defines the type of the device connected to the modem.
 */
public enum DeviceType {
    /**
     * PAN device.
     */
    PAN_DEVICE,
    /**
     * PAN coordinator.
     */
    PAN_COORDINATOR,
    /**
     * Not Defined.
     */
    NOT_DEFINED;

    /**
     * Get integer value from the enumerations.
     * 
     * @return Enumeration integer value.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Returns the constant enumeration value from an integer value.
     * 
     * @param value
     *            Integer value.
     * @return Enumeration value.
     */
    public static DeviceType forValue(final int value) {
        return values()[value];
    }
}