//
// --------------------------------------------------------------------------
//  Gurux Ltd
package gurux.dlms;

import gurux.dlms.enums.AccessServiceCommandType;
import gurux.dlms.objects.GXDLMSObject;

/**
 * Access item is used to generate Access Service message.
 */
public class GXDLMSAccessItem {
    /**
     * COSEM target object.
     */
    private GXDLMSObject target;
    /**
     * Executed command type.
     */
    private AccessServiceCommandType command;

    /**
     * Attribute index.
     */
    private byte index;

    /**
     * @return COSEM target object.
     */
    public final GXDLMSObject getTarget() {
        return target;
    }

    /**
     * @param value
     *            COSEM target object.
     */
    public final void setTarget(final GXDLMSObject value) {
        target = value;
    }

    /**
     * @return Executed command type.
     */
    public final AccessServiceCommandType getCommand() {
        return command;
    }

    /**
     * @param value
     *            Executed command type.
     */
    public final void setCommand(final AccessServiceCommandType value) {
        command = value;
    }

    /**
     * @return Attribute index.
     */
    public final byte getIndex() {
        return index;
    }

    /**
     * @param value
     *            Attribute index.
     */
    public final void setIndex(final byte value) {
        index = value;
    }

    /**
     * Constructor.
     */
    public GXDLMSAccessItem() {
    }

    /**
     * Constructor.
     * 
     * @param commandType
     *            Command to execute.
     * @param targetObject
     *            COSEM target object.
     * @param attributeIndex
     *            Attribute index.
     */
    public GXDLMSAccessItem(final AccessServiceCommandType commandType,
            final GXDLMSObject targetObject, final byte attributeIndex) {
        setCommand(commandType);
        setTarget(targetObject);
        setIndex(attributeIndex);
    }
}
