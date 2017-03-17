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
    private int command;

    /**
     * Attribute index.
     */
    private int index;

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
    public final int getCommand() {
        return command;
    }

    /**
     * @return Attribute index.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            Attribute index.
     */
    public final void setIndex(final int value) {
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
    public GXDLMSAccessItem(final int commandType,
            final GXDLMSObject targetObject, final int attributeIndex) {
        if (commandType == AccessServiceCommandType.GET) {
            command = 1;
        } else if (commandType == AccessServiceCommandType.SET) {
            command = 2;
        } else if (commandType == AccessServiceCommandType.ACTION) {
            command = 3;
        }
        setTarget(targetObject);
        setIndex(attributeIndex);
    }
}
