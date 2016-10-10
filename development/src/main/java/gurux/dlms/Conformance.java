package gurux.dlms;

/**
 * Enumerates all conformance bits.
 */
final class Conformance {
    /**
     * Constructor.
     */
    private Conformance() {

    }

    /**
     * Reserved zero conformance bit.
     */
    static final int RESERVED_ZERO = 0x800000;

    /*
     * /** General protection conformance bit.
     */
    static final int GENERAL_PROTECTION = 0x400000;

    /**
     * General block transfer conformance bit.
     */
    static final int GENERAL_BLOCK_TRANSFER = 0x200000;
    /**
     * Read conformance bit.
     */
    static final int READ = 0x100000;
    /**
     * Write conformance bit.
     */
    static final int WRITE = 0x80000;
    /**
     * Un confirmed write conformance bit.
     */
    static final int UN_CONFIRMED_WRITE = 0x40000;
    /**
     * Reserved six conformance bit.
     */
    static final int RESERVED_SIX = 0x20000;
    /**
     * Reserved seven conformance bit.
     */
    static final int RESERVED_SEVEN = 0x10000;
    /**
     * Attribute 0 supported with set conformance bit.
     */
    static final int ATTRIBUTE_0_SUPPORTED_WITH_SET = 0x8000;
    /**
     * Priority mgmt supported conformance bit.
     */
    static final int PRIORITY_MGMT_SUPPORTED = 0x4000;
    /**
     * Attribute 0 supported with get conformance bit.
     */
    static final int ATTRIBUTE_0_SUPPORTED_WITH_GET = 0x2000;
    /**
     * Block transfer with get or read conformance bit.
     */
    static final int BLOCK_TRANSFER_WITH_GET_OR_READ = 0x1000;
    /**
     * Block transfer with set or write conformance bit.
     */
    static final int BLOCK_TRANSFER_WITH_SET_OR_WRITE = 0x800;
    /**
     * Block transfer with action conformance bit.
     */
    static final int BLOCK_TRANSFER_WITH_ACTION = 0x400;
    /**
     * multiple references conformance bit.
     */
    static final int MULTIPLE_REFERENCES = 0x200;
    /**
     * Information report conformance bit.
     */
    static final int INFORMATION_REPORT = 0x100;
    /**
     * Data notification conformance bit.
     */
    static final int DATA_NOTIFICATION = 0x80;
    /**
     * Access conformance bit.
     */
    static final int ACCESS = 0x40;
    /**
     * Parameterized access conformance bit.
     */
    static final int PARAMETERIZED_ACCESS = 0x20;
    /**
     * Get conformance bit.
     */
    static final int GET = 0x10;
    /**
     * Set conformance bit.
     */
    static final int SET = 0x8;
    /**
     * Selective access conformance bit.
     */
    static final int SELECTIVE_ACCESS = 0x4;
    /**
     * Event notification conformance bit.
     */
    static final int EVENT_NOTIFICATION = 0x2;
    /**
     * Action conformance bit.
     */
    static final int ACTION = 0x1;

    static int[] getEnumConstants() {
        return new int[] { ACTION, EVENT_NOTIFICATION, SELECTIVE_ACCESS, SET,
                GET, PARAMETERIZED_ACCESS, ACCESS, DATA_NOTIFICATION,
                INFORMATION_REPORT, MULTIPLE_REFERENCES,
                BLOCK_TRANSFER_WITH_ACTION, BLOCK_TRANSFER_WITH_SET_OR_WRITE,
                BLOCK_TRANSFER_WITH_GET_OR_READ, ATTRIBUTE_0_SUPPORTED_WITH_GET,
                PRIORITY_MGMT_SUPPORTED, ATTRIBUTE_0_SUPPORTED_WITH_SET,
                RESERVED_SEVEN, RESERVED_SIX, UN_CONFIRMED_WRITE, WRITE, READ,
                GENERAL_BLOCK_TRANSFER, GENERAL_PROTECTION, RESERVED_ZERO };
    }

    static String toString(final int value) {
        String str;
        switch (value) {
        case ACCESS:
            str = "Access";
            break;
        case ACTION:
            str = "Action";
            break;
        case ATTRIBUTE_0_SUPPORTED_WITH_GET:
            str = "Attribute0SupportedWithGet";
            break;
        case ATTRIBUTE_0_SUPPORTED_WITH_SET:
            str = "Attribute0SupportedWithSet";
            break;
        case BLOCK_TRANSFER_WITH_ACTION:
            str = "BlockTransferWithAction";
            break;
        case BLOCK_TRANSFER_WITH_GET_OR_READ:
            str = "BlockTransferWithGetOrRead";
            break;
        case BLOCK_TRANSFER_WITH_SET_OR_WRITE:
            str = "BlockTransferWithSetOrWrite";
            break;
        case DATA_NOTIFICATION:
            str = "DataNotification";
            break;
        case EVENT_NOTIFICATION:
            str = "EventNotification";
            break;
        case GENERAL_BLOCK_TRANSFER:
            str = "GeneralBlockTransfer";
            break;
        case GENERAL_PROTECTION:
            str = "GeneralProtection";
            break;
        case GET:
            str = "Get";
            break;
        case INFORMATION_REPORT:
            str = "InformationReport";
            break;
        case MULTIPLE_REFERENCES:
            str = "MultipleReferences";
            break;
        case PARAMETERIZED_ACCESS:
            str = "ParameterizedAccess";
            break;
        case PRIORITY_MGMT_SUPPORTED:
            str = "PriorityMgmtSupported";
            break;
        case READ:
            str = "Read";
            break;
        case RESERVED_SEVEN:
            str = "ReservedSeven";
            break;
        case RESERVED_SIX:
            str = "ReservedSix";
            break;
        case RESERVED_ZERO:
            str = "ReservedZero";
            break;
        case SELECTIVE_ACCESS:
            str = "SelectiveAccess";
            break;
        case SET:
            str = "Set";
            break;
        case UN_CONFIRMED_WRITE:
            str = "UnconfirmedWrite";
            break;
        case WRITE:
            str = "Write";
            break;
        default:
            throw new IllegalArgumentException(String.valueOf(value));
        }
        return str;
    }

    public static int valueOf(final String value) {
        int ret;
        if ("Access".equalsIgnoreCase(value)) {
            ret = Conformance.ACCESS;
        } else if ("Action".equalsIgnoreCase(value)) {
            ret = Conformance.ACTION;
        } else if ("Attribute0SupportedWithGet".equalsIgnoreCase(value)) {
            ret = Conformance.ATTRIBUTE_0_SUPPORTED_WITH_GET;
        } else if ("Attribute0SupportedWithSet".equalsIgnoreCase(value)) {
            ret = Conformance.ATTRIBUTE_0_SUPPORTED_WITH_SET;
        } else if ("BlockTransferWithAction".equalsIgnoreCase(value)) {
            ret = Conformance.BLOCK_TRANSFER_WITH_ACTION;
        } else if ("BlockTransferWithGetOrRead".equalsIgnoreCase(value)) {
            ret = Conformance.BLOCK_TRANSFER_WITH_GET_OR_READ;
        } else if ("BlockTransferWithSetOrWrite".equalsIgnoreCase(value)) {
            ret = Conformance.BLOCK_TRANSFER_WITH_SET_OR_WRITE;
        } else if ("DataNotification".equalsIgnoreCase(value)) {
            ret = Conformance.DATA_NOTIFICATION;
        } else if ("EventNotification".equalsIgnoreCase(value)) {
            ret = Conformance.EVENT_NOTIFICATION;
        } else if ("GeneralBlockTransfer".equalsIgnoreCase(value)) {
            ret = Conformance.GENERAL_BLOCK_TRANSFER;
        } else if ("GeneralProtection".equalsIgnoreCase(value)) {
            ret = Conformance.GENERAL_PROTECTION;
        } else if ("Get".equalsIgnoreCase(value)) {
            ret = Conformance.GET;
        } else if ("InformationReport".equalsIgnoreCase(value)) {
            ret = Conformance.INFORMATION_REPORT;
        } else if ("MultipleReferences".equalsIgnoreCase(value)) {
            ret = Conformance.MULTIPLE_REFERENCES;
        } else if ("ParameterizedAccess".equalsIgnoreCase(value)) {
            ret = Conformance.PARAMETERIZED_ACCESS;
        } else if ("PriorityMgmtSupported".equalsIgnoreCase(value)) {
            ret = Conformance.PRIORITY_MGMT_SUPPORTED;
        } else if ("Read".equalsIgnoreCase(value)) {
            ret = Conformance.READ;
        } else if ("ReservedSeven".equalsIgnoreCase(value)) {
            ret = Conformance.RESERVED_SEVEN;
        } else if ("ReservedSix".equalsIgnoreCase(value)) {
            ret = Conformance.RESERVED_SIX;
        } else if ("ReservedZero".equalsIgnoreCase(value)) {
            ret = Conformance.RESERVED_ZERO;
        } else if ("SelectiveAccess".equalsIgnoreCase(value)) {
            ret = Conformance.SELECTIVE_ACCESS;
        } else if ("Set".equalsIgnoreCase(value)) {
            ret = Conformance.SET;
        } else if ("UnconfirmedWrite".equalsIgnoreCase(value)) {
            ret = Conformance.UN_CONFIRMED_WRITE;
        } else if ("Write".equalsIgnoreCase(value)) {
            ret = Conformance.WRITE;
        } else {
            throw new IllegalArgumentException(value);
        }
        return ret;
    }
}