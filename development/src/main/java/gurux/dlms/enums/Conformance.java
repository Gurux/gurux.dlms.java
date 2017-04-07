package gurux.dlms.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * Enumerates all conformance bits.<br>
 * Client is using conformance to tell what kind of services it's interested and
 * server is using conformance to tell what kind of services it can offer. All
 * services enumerated here are not necessary supported by the client or the
 * server.
 */
public enum Conformance {
    /**
     * Reserved zero conformance bit. <br>
     * Not used at the moment.
     */
    RESERVED_ZERO(0x800000),
    /**
     * Is General Protection supported by the meter or is client interested from
     * General Protection.<br>
     * General Protection is used to secure connection between client and the
     * meter using Symmetric or ASymmetric ciphering or General Signing.
     */
    GENERAL_PROTECTION(0x400000),
    /**
     * General Block Transfer mechanism is used to transport data that size is
     * longer than PDU size.
     */
    GENERAL_BLOCK_TRANSFER(0x200000),
    /**
     * Is Read supported by the meter or is client interested from Read.<br>
     * Read is used with Short Name Referencing to read value from the meter.
     */
    READ(0x100000),
    /**
     * Is Write supported by the meter or is client interested from write data
     * to the meter.<br>
     * Write is used with Short Name Referencing to write value to the meter.
     */
    WRITE(0x80000),
    /**
     * Is Unconfirmed Write supported by the meter or is client interested from
     * write unconfirmed data to the meter.<br>
     * Unconfirmed Write is used with Short Name Referencing to write value
     * without confirmation from success to the meter.
     */
    UN_CONFIRMED_WRITE(0x40000),
    /**
     * Reserved six conformance bit.<br>
     * Not used at the moment.
     */
    RESERVED_SIX(0x20000),
    /**
     * Reserved seven conformance bit.<br>
     * Not used at the moment.
     */
    RESERVED_SEVEN(0x10000),
    /**
     * Is Attribute Set supported by the meter or is client interested from
     * write data with Attribute 0 to the meter.<br>
     * Attribute 0 Set means that all COSEM object's attributes can be write
     * with one message.
     */
    ATTRIBUTE_0_SUPPORTED_WITH_SET(0x8000),
    /**
     * Is Priority Management supported by the meter or is client interested
     * from Priority Management.<br>
     * Priority Management is used by Logical Name referencing to handle urgent
     * messages.
     */
    PRIORITY_MGMT_SUPPORTED(0x4000),
    /**
     * Is Attribute get supported by the meter or is client interested from read
     * data with Attribute 0 from the meter.<br>
     * Attribute 0 Get means that all COSEM object's attributes can be read with
     * one message.
     */
    ATTRIBUTE_0_SUPPORTED_WITH_GET(0x2000),
    /**
     * Is Block transfer supported by the meter or is client interested from
     * read data with blocks from the meter.<br>
     * Read Block transfer is used to read data from the meter that is not fit
     * to one PDU. Example reading historical data might take more than one PDU
     * and then block transfer is used.
     */
    BLOCK_TRANSFER_WITH_GET_OR_READ(0x1000),
    /**
     * Is Block transfer supported by the meter or is client interested from
     * writing data with blocks to the meter.<br>
     * Write Block transfer is used to write data from the meter that is not fit
     * to one PDU. Example updating image (Firmware) might take more than one
     * PDU and then block transfer is used.
     */
    BLOCK_TRANSFER_WITH_SET_OR_WRITE(0x800),
    /**
     * Is Block with action transfer supported by the meter or is client
     * interested from sending action data with blocks to the meter.<br>
     * Action Block transfer is used to send action data from the meter that is
     * not fit to one PDU. Example Public Key transfer might take more than one
     * PDU and then block transfer with action is used.
     */
    BLOCK_TRANSFER_WITH_ACTION(0x400),
    /**
     * Is multiple references supported by the meter or is client interested
     * from multiple references.<br>
     * Multiple references is used when reading or writing several object with
     * one message. Example ReadByList is using it.
     */
    MULTIPLE_REFERENCES(0x200),
    /**
     * Is Information reports supported by the meter.<br>
     * Information reports is used with Short Name Referencing
     * 
     * @see DATA_NOTIFICATION
     * @see EVENT_NOTIFICATION
     */
    INFORMATION_REPORT(0x100),
    /**
     * Is Data Notification supported by the meter.
     * 
     * @see INFORMATION_REPORT
     * @see EVENT_NOTIFICATION
     */
    DATA_NOTIFICATION(0x80),
    /**
     * Is Access service supported by the meter or is client interested from
     * Access service.<br>
     * Using Access service client can make several Read, Write or Action
     * requests with one command.
     */
    ACCESS(0x40),
    /**
     * Is parameterized access supported by the meter or is client interested
     * from parameterized access.<br>
     * Parameterized access is used with Short Name Referencing example if
     * Profile Generic is read by range or entry.
     * 
     * @see SELECTIVE_ACCESS
     */
    PARAMETERIZED_ACCESS(0x20),
    /**
     * Is Get supported by the meter or is client interested from Get.<br>
     * Get is used with Logical Name Referencing to read value from the meter.
     */
    GET(0x10),
    /**
     * Is Set supported by the meter or is client interested from Set.<br>
     * Set is used with Logical Name Referencing to write value to the meter.
     */
    SET(0x8),
    /**
     * Is selective access supported by the meter or is client interested from
     * selective access.<br>
     * Selective access is used with Logical Name Referencing example if Profile
     * Generic is read by range or entry.
     * 
     * @see PARAMETERIZED_ACCESS
     */
    SELECTIVE_ACCESS(0x4),
    /**
     * Is Event Notification supported by the meter.<br>
     * Meter is using Event Notifications with Logical Name referencing to send
     * events for given target. Example if power down occurred. Note! Client do
     * not need to have connection to the server when event notification is
     * send.
     * 
     * @see INFORMATION_REPORT
     * @see DATA_NOTIFICATION
     */
    EVENT_NOTIFICATION(0x2),
    /**
     * Is actions supported by the meter or is client interested from actions.
     * <br>
     * Actions are used example to reset historical data.
     */
    ACTION(0x1),
    /**
     * None conformace is reserved for internal use.
     */
    NONE(0);

    private int value;

    /*
     * Constructor.
     */
    Conformance(final int forValue) {
        value = forValue;
    }

    /*
     * Get integer value for enumerator.
     */
    public int getValue() {
        return value;
    }

    public static Conformance[] getEnumConstants() {
        return new Conformance[] { ACTION, EVENT_NOTIFICATION, SELECTIVE_ACCESS,
                SET, GET, PARAMETERIZED_ACCESS, ACCESS, DATA_NOTIFICATION,
                INFORMATION_REPORT, MULTIPLE_REFERENCES,
                BLOCK_TRANSFER_WITH_ACTION, BLOCK_TRANSFER_WITH_SET_OR_WRITE,
                BLOCK_TRANSFER_WITH_GET_OR_READ, ATTRIBUTE_0_SUPPORTED_WITH_GET,
                PRIORITY_MGMT_SUPPORTED, ATTRIBUTE_0_SUPPORTED_WITH_SET,
                RESERVED_SEVEN, RESERVED_SIX, UN_CONFIRMED_WRITE, WRITE, READ,
                GENERAL_BLOCK_TRANSFER, GENERAL_PROTECTION, RESERVED_ZERO };
    }

    /**
     * Converts the integer value to enumerated value.
     * 
     * @param value
     *            The integer value, which is read from the device.
     * @return The enumerated value, which represents the integer.
     */
    public static Set<Conformance> forValue(final int value) {
        Set<Conformance> types = new HashSet<Conformance>();
        Conformance[] enums = getEnumConstants();
        for (int pos = 0; pos != enums.length; ++pos) {
            if ((enums[pos].value & value) == enums[pos].value) {
                types.add(enums[pos]);
            }
        }
        return types;
    }

    /**
     * Converts the enumerated value to integer value.
     * 
     * @param value
     *            The enumerated value.
     * @return The integer value.
     */
    public static int toInteger(final Set<Conformance> value) {
        if (value == null) {
            return 0;
        }
        int tmp = 0;
        for (Conformance it : value) {
            tmp |= it.getValue();
        }
        return tmp;
    }
}