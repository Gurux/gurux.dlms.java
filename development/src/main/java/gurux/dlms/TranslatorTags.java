//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms;

final class TranslatorTags {
    /*
     * Constructor.
     */
    private TranslatorTags() {

    }

    static final int WRAPPER = 0xFF01;
    static final int HDLC = 0xFF02;
    static final int PDU_DLMS = 0xFF03;
    static final int TARGET_ADDRESS = 0xFF04;
    static final int SOURCE_ADDRESS = 0xFF05;
    static final int LIST_OF_VARIABLE_ACCESS_SPECIFICATION = 0xFF06;
    static final int LIST_OF_DATA = 0xFF07;
    static final int SUCCESS = 0xFF08;
    static final int DATA_ACCESS_ERROR = 0xFF09;
    static final int ATTRIBUTE_DESCRIPTOR = 0xFF0A;
    static final int CLASS_ID = 0xFF0B;
    static final int INSTANCE_ID = 0xFF0C;
    static final int ATTRIBUTE_ID = 0xFF0D;
    static final int METHOD_INVOCATION_PARAMETERS = 0xFF0E;
    static final int SELECTOR = 0xFF0F;
    static final int PARAMETER = 0xFF10;
    static final int LAST_BLOCK = 0xFF11;
    static final int BLOCK_NUMBER = 0xFF12;
    static final int RAW_DATA = 0xFF13;
    static final int METHOD_DESCRIPTOR = 0xFF14;
    static final int METHOD_ID = 0xFF15;
    static final int RESULT = 0xFF16;
    static final int RETURN_PARAMETERS = 0xFF17;
    static final int ACCESS_SELECTION = 0xFF18;
    static final int VALUE = 0xFF19;
    static final int ACCESS_SELECTOR = 0xFF1A;
    static final int ACCESS_PARAMETERS = 0xFF1B;
    static final int ATTRIBUTE_DESCRIPTOR_LIST = 0xFF1C;
    static final int ATTRIBUTE_DESCRIPTOR_WITH_SELECTION = 0xFF1D;
    static final int READ_DATA_BLOCK_ACCESS = 0xFF1E;
    static final int WRITE_DATA_BLOCK_ACCESS = 0xFF1F;
    static final int DATA = 0xFF20;
    static final int INVOKE_ID = 0xFF21;
    static final int DATE_TIME = 0xFF22;
    static final int REASON = 0xFF23;
    static final int VARIABLE_ACCESS_SPECIFICATION = 0xFF24;
    static final int PDU_CSE = 0xFF26;
    static final int CHOICE = 0xFF27;
    static final int LONG_INVOKE_ID = 0xFF28;
    static final int NOTIFICATION_BODY = 0xFF29;
    static final int DATA_VALUE = 0xFF30;
    static final int ACCESS_REQUEST_BODY = 0xFF31;
    static final int LIST_OF_ACCESS_REQUEST_SPECIFICATION = 0xFF32;
    static final int ACCESS_REQUEST_SPECIFICATION = 0xFF33;
    static final int ACCESS_REQUEST_LIST_OF_DATA = 0xFF34;
    static final int ACCESS_RESPONSE_BODY = 0xFF35;
    static final int LIST_OF_ACCESS_RESPONSE_SPECIFICATION = 0xFF36;
    static final int ACCESS_RESPONSE_SPECIFICATION = 0xFF37;
    static final int ACCESS_RESPONSE_LIST_OF_DATA = 0xFF38;
    static final int SINGLE_RESPONSE = 0xFF39;
    static final int SERVICE = 0xFF40;
    static final int SERVICE_ERROR = 0xFF41;
    static final int INITIATE_ERROR = 0xFF42;
    static final int CIPHERED_SERVICE = 0xFF43;
    static final int SYSTEM_TITLE = 0xFF44;
    static final int DATA_BLOCK = 0xFF45;
    static final int TRANSACTION_ID = 0xFF46;
    static final int ORIGINATOR_SYSTEM_TITLE = 0xFF47;
    static final int RECIPIENT_SYSTEM_TITLE = 0xFF48;
    static final int OTHER_INFORMATION = 0xFF49;
    static final int KEY_INFO = 0xFF50;
    static final int AGREED_KEY = 0xFF51;
    static final int KEY_PARAMETERS = 0xFF52;
    static final int KEY_CIPHERED_DATA = 0xFF53;
    static final int CIPHERED_CONTENT = 0xFF54;
    static final int ATTRIBUTE_VALUE = 0xFF55;
    static final int CURRENT_TIME = 0xFF56;
    static final int TIME = 0xFF57;
}
