/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

 public enum GXDLMSPppSetupIPCPOptionType
 {
    IPCompressionProtocol(2),
    PrefLocalIP(3),
    PrefPeerIP(20),
    GAO(21),
    USIP(22);

    private int intValue;
    private static java.util.HashMap<Integer, GXDLMSPppSetupIPCPOptionType> mappings;
    private static java.util.HashMap<Integer, GXDLMSPppSetupIPCPOptionType> getMappings()
    {
        if (mappings == null)
        {
            synchronized (GXDLMSPppSetupIPCPOptionType.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, GXDLMSPppSetupIPCPOptionType>();
                }
            }
        }
        return mappings;
    }

    private GXDLMSPppSetupIPCPOptionType(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return intValue;
    }

    public static GXDLMSPppSetupIPCPOptionType forValue(int value)
    {
        return getMappings().get(value);
    }
}