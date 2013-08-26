/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

import gurux.dlms.enums.ObjectType;

public class GXDLMSMonitoredValue
{
    private ObjectType m_ObjectType;
    private String m_LogicalName;
    private int m_AttributeIndex;
  
    public final void update(GXDLMSObject value, int attributeIndex)
    {
        m_ObjectType = value.getObjectType();
        m_LogicalName = value.getLogicalName();
        m_AttributeIndex = attributeIndex;
    }
    
    public final ObjectType getObjectType()
    {
        return m_ObjectType;
    }
    public final void setObjectType(ObjectType value)
    {
        m_ObjectType = value;
    }

    public final String getLogicalName()
    {
        return m_LogicalName;
    }
    public final void setLogicalName(String value)
    {
        m_LogicalName = value;
    }

    public final int getAttributeIndex()
    {
        return m_AttributeIndex;
    }
    public final void setAttributeIndex(int value)
    {
        m_AttributeIndex = value;
    }
}