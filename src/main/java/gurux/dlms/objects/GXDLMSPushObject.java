/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

import gurux.dlms.enums.ObjectType;

public class GXDLMSPushObject {
    private ObjectType type;
    private String logicalName;
    private int attributeIndex;
    private int dataIndex;

    public final ObjectType getType() {
        return type;
    }

    public final void setType(final ObjectType value) {
        type = value;
    }

    public final String getLogicalName() {
        return logicalName;
    }

    public final void setLogicalName(final String value) {
        logicalName = value;
    }

    public final int getAttributeIndex() {
        return attributeIndex;
    }

    public final void setAttributeIndex(final int value) {
        attributeIndex = value;
    }

    public final int getDataIndex() {
        return dataIndex;
    }

    public final void setDataIndex(final int value) {
        dataIndex = value;
    }
}