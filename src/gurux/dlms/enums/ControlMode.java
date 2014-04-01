package gurux.dlms.enums;

/*
 * Configures the behaviour of the disconnect control object for all
triggers, i.e. the possible state transitions.
 */
public enum ControlMode 
{
    /*
     * The disconnect control object is always in 'connected' state,
     */
    NONE,
    /*
     * Disconnection: Remote (b, c), manual (f), local (g) 
     * Reconnection: Remote (d), manual (e).
     */
    MODE_1,
    /*
     * Disconnection: Remote (b, c), manual (f), local (g) 
     * Reconnection: Remote (a), manual (e).
     */
    MODE_2,
    /*
     * Disconnection: Remote (b, c), manual (-), local (g) 
     * Reconnection: Remote (d), manual (e).
     */
    MODE_3,
    /*
     * Disconnection: Remote (b, c), manual (-), local (g) 
     * Reconnection: Remote (a), manual (e)
     */
    MODE_4,
    /*
     * Disconnection: Remote (b, c), manual (f), local (g) 
     * Reconnection: Remote (d), manual (e), local (h),
     */
    MODE_5,
    /*
     * Disconnection: Remote (b, c), manual (-), local (g) 
     * Reconnection: Remote (d), manual (e), local (h)
     */
    MODE_6;
    
    @Override
    public String toString()
    {
        String str;        
        switch(ordinal())
        {
            case 0://NONE
                str = "None";
            break;
            case 1://MODE_1
                str = "Mode1";
            break;
            case 2://MODE_2
                str = "Mode2";
            break;
            case 3://MODE_3
                str = "Mode3";
            break;
            case 4://MODE_4
                str = "Mode4";
            break;
            case 5://MODE_5
                str = "Mode5";
            break;
            case 6://MODE_6
                str = "Mode6";
            break;
            default:
                str = "Unknown";
        }
        return str;
    }
}
