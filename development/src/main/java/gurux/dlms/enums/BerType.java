//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms.enums;

/**
 * BER encoding enumeration values.
 */
public final class BerType {
    /**
     * Constructor.
     */
    private BerType() {
    }

    /**
     * End of Content.
     */
    public static final short EOC = 0x00;

    /**
     * Boolean.
     */
    public static final short BOOLEAN = 0x1;

    /**
     * Integer.
     */
    public static final short INTEGER = 0x2;

    /**
     * Bit String.
     */
    public static final short BIT_STRING = 0x3;

    /**
     * Octet string.
     */
    public static final short OCTET_STRING = 0x4;

    /**
     * Null value.
     */
    public static final short NULL = 0x5;

    /**
     * Object identifier.
     */
    public static final short OBJECT_IDENTIFIER = 0x6;

    /**
     * Object Descriptor.
     */
    public static final short OBJECT_DESCRIPTOR = 7;

    /**
     * External
     */
    public static final short EXTERNAL = 8;

    /**
     * Real = float).
     */
    public static final short REAL = 9;

    /**
     * Enumerated.
     */
    public static final short ENUMERATED = 10;

    /**
     * Sequence.
     */
    public static final short SEQUENCE = 0x10;

    public static final short SET = 0x11;
    /**
     * Utf8 String.
     */
    public static final short UTF8STRING = 12;

    /**
     * Numeric string.
     */
    public static final short NUMERIC_STRING = 18;

    /**
     * Printable string.
     */
    public static final short PRINTABLE_STRING = 19;

    /**
     * Teletex string.
     */
    public static final short TELETEX_STRING = 20;

    /**
     * Videotex string.
     */
    public static final short VIDEOTEX_STRING = 21;

    /**
     * Ia5 string
     */
    public static final short IA5_STRING = 22;

    /**
     * Utc time.
     */
    public static final short UTC_TIME = 23;

    /**
     * Generalized time.
     */
    public static final short GENERALIZED_TIME = 24;

    /**
     * Graphic string.
     */
    public static final short GRAPHIC_STRING = 25;

    /**
     * Visible string.
     */
    public static final short VISIBLE_STRING = 26;

    /**
     * General string.
     */
    public static final short GENERAL_STRING = 27;

    /**
     * Universal string.
     */
    public static final short UNIVERSAL_STRING = 28;

    /**
     * Bmp string.
     */
    public static final short BMP_STRING = 30;

    /**
     * Application class.
     */
    public static final short APPLICATION = 0x40;

    /**
     * Context class.
     */
    public static final short CONTEXT = 0x80;

    /**
     * Private class.
     */
    public static final short PRIVATE = 0xc0;

    /**
     * Constructed.
     */
    public static final short CONSTRUCTED = 0x20;
}
