//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms;

/**
 * Access selection item is used access requests with selection.
 */
public class GXDLMSAccessSelectionItem {

    /**
     * Executed command type.
     */
    private int command;

    /**
     * Target.
     */
    private ValueEventArgs target;

    /**
     * @return target.
     */
    public final ValueEventArgs getTarget() {
        return target;
    }

    /**
     * @param value
     *          Target.
     */
    public final void setTarget(final ValueEventArgs value) {
        target = value;
    }

    /**
     * @return Executed command type.
     */
    public final int getCommand() {
        return command;
    }

    /**
     * @param value
     *          Command type.
     */
    public final void setCommand(final int value) {
        command = value;
    }

    /**
     * Constructor.
     */
    public GXDLMSAccessSelectionItem() {
    }

    /**
     * Constructor.
     *
     * @param commandType
     *            Command to execute.
     * @param target
     *            Target.
     */
    public GXDLMSAccessSelectionItem(final int commandType,
                                     final ValueEventArgs target) {
        command = commandType;
        this.target = target;
    }
}
