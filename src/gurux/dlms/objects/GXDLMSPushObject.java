/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

import gurux.dlms.enums.ObjectType;

public class GXDLMSPushObject
{
    private ObjectType Type;
    private String LogicalName;
    private int AttributeIndex;
    private int DataIndex;
    
    public final ObjectType getType()
    {
        return Type;
    }
    public final void setType(ObjectType value)
    {
        Type = value;
    }

    
    public final String getLogicalName()
    {
        return LogicalName;
    }
    public final void setLogicalName(String value)
    {
        LogicalName = value;
    }

    
    public final int getAttributeIndex()
    {
        return AttributeIndex;
    }
    public final void setAttributeIndex(int value)
    {
        AttributeIndex = value;
    }
    
    public final int getDataIndex()
    {
        return DataIndex;
    }
    public final void setDataIndex(int value)
    {
        DataIndex = value;
    }
}