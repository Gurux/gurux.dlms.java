/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

public class GXDLMSPppSetupIPCPOption
{
    private GXDLMSPppSetupIPCPOptionType m_Type;
    private int m_Length;
    private Object m_Data;
    
    public final GXDLMSPppSetupIPCPOptionType getType()
    {
        return m_Type;
    }
    public final void setType(GXDLMSPppSetupIPCPOptionType value)
    {
        m_Type = value;
    }

    //C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    //ORIGINAL LINE: public byte getLength()
    public final int getLength()
    {
        return m_Length;
    }
    //C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    //ORIGINAL LINE: public void setLength(byte value)
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
}