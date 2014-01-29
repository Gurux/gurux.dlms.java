/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

public enum GXDLMSPppSetupLcpOptionType
{
    MAX_REC_UNIT(1),
    ASYNC_CONTROL_CHAR_MAP(2),
    AUTH_PROTOCOL(3),
    MAGIC_NUMBER(5),
    PROTOCOL_FIELD_COMPRESSION(7),
    ADDRESS_AND_CTRL_COMPRESSION(8),
    FCS_ALTERNATIVES(9),
    CALLBACK(13);

    private int intValue;
    private static java.util.HashMap<Integer, GXDLMSPppSetupLcpOptionType> mappings;
    private static java.util.HashMap<Integer, GXDLMSPppSetupLcpOptionType> getMappings()
    {
        if (mappings == null)
        {
            synchronized (GXDLMSPppSetupLcpOptionType.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, GXDLMSPppSetupLcpOptionType>();
                }
            }
        }
        return mappings;
    }

    private GXDLMSPppSetupLcpOptionType(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return intValue;
    }

    public static GXDLMSPppSetupLcpOptionType forValue(int value)
    {
        return getMappings().get(value);
    }
}