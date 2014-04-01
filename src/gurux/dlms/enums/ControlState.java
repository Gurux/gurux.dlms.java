package gurux.dlms.enums;

/*
 * The internal states of the disconnect control object.
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
    READY_FOR_RECONNECTION;
    
    @Override
    public String toString()
    {
        String str;        
        switch(ordinal())
        {
            case 0://DISCONNECTED
                str = "Disconnected";
            break;
            case 1://CONNECTED
                str = "Connected";
            break;
            case 2://READY_FOR_RECONNECTION
                str = "Ready For reconnection";
            break;
            default:
                str = "Unknown";
        }
        return str;
    }
}
