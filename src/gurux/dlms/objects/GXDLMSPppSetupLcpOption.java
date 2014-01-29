/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

public class GXDLMSPppSetupLcpOption
{
    private GXDLMSPppSetupLcpOptionType m_Type;
    private Object m_Data;
    private int m_Length;   

    public final GXDLMSPppSetupLcpOptionType getType()
    {
        return m_Type;
    }
    public final void setType(GXDLMSPppSetupLcpOptionType value)
    {
        m_Type = value;
    }

    public final int getLength()
    {
        return m_Length;
    }

    public final void setLength(int value)
    {
        m_Length = value;
    }

    public final Object getData()
    {
        return m_Data;
    }
    public final void setData(Object value)
    {
        m_Data = value;
    }

    @Override
    public String toString()
    {
        return getType().toString() + " " + String.valueOf(getData());
    }
}