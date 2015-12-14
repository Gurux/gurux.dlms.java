package gurux.dlms;

import gurux.dlms.objects.GXDLMSObject;

/**
 * Server uses this class to find Short Name object and attribute index. This
 * class is reserved for internal use.
 * 
 * @author Gurux Ltd.
 */
class GXSNInfo {

    /**
     * Is attribute index or action index
     */
    private boolean action = false;

    /**
     * Attribute index.
     */
    private int index = 0;

    /**
     * COSEM object.
     */
    private GXDLMSObject item = null;

    /**
     * @return The index
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            The index to set
     */
    public final void setIndex(final int value) {
        index = value;
    }

    /**
     * @return Is action.
     */
    public final boolean isAction() {
        return action;
    }

    /**
     * @param value
     *            Is action.
     */
    public final void setAction(final boolean value) {
        action = value;
    }

    /**
     * @return The item
     */
    public final GXDLMSObject getItem() {
        return item;
    }

    /**
     * @param value
     *            The item to set
     */
    public final void setItem(final GXDLMSObject value) {
        item = value;
    }
}