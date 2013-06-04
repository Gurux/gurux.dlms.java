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
    MODE_6,
}
