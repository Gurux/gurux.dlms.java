package gurux.dlms.enums;

/*
 * Yhe internal states of the disconnect control object.
 */
public enum ControlState 
{
    /*
     * The output_state is set to false and the consumer is disconnected.
     */
    DISCONNECTED,
    /*
     * The output_state is set to true and the consumer is connected.
     */
    CONNECTED,
    /*
     * The output_state is set to false and the consumer is disconnected.
     */
    READY_FOR_RECONNECTION
}
