/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.enums;

public enum GXDLMSIp4SetupIpOptionType
{
    /** 
     If this option is present, the device shall be allowed to send security,
     compartmentation, handling restrictions and TCC (closed user group)
     parameters within its IP Datagrams. The value of the IP-Option-
     Length Field must be 11, and the IP-Option-Data shall contain the
     value of the Security, Compartments, Handling Restrictions and
     Transmission Control Code values, as specified in STD0005 / RFC791.
    */
    Security(0x82),
    /** 
     If this option is present, the device shall supply routing information to be
     used by the gateways in forwarding the datagram to the destination, and to
     record the route information.
     The IP-Option-length and IP-Option-Data values are specified in STD0005 / RFC 791.
    */
    LooseSourceAndRecordRoute(0x83),
    /** 
     If this option is present, the device shall supply routing information to be
     used by the gateways in forwarding the datagram to the destination, and to
     record the route information.
     The IP-Option-length and IP-Option-Data values are specified in STD0005 / RFC 791.
    */
    StrictSourceAndRecordRoute(0x89),
    /** 
     If this option is present, the device shall as well:
     send originated IP Datagrams with that option, providing means
     to record the route of these Datagrams;
     as a router, send routed IP Datagrams with the route option
     adjusted according to this option.
     The IP-Option-length and IP-Option-Data values are specified in
     STD0005 / RFC 791.
    */
    RecordRoute(0x07),
    /** 
     If this option is present, the device shall as well:
     send originated IP Datagrams with that option, providing means
     to time-stamp the datagram in the route to its destination;
     as a router, send routed IP Datagrams with the time-stamp option
     adjusted according to this option.
     The IP-Option-length and IP-Option-Data values are specified in STD0005 / RFC 791.
    */
    InternetTimestamp(0x44);

    private int intValue;
    private static java.util.HashMap<Integer, GXDLMSIp4SetupIpOptionType> mappings;
    private static java.util.HashMap<Integer, GXDLMSIp4SetupIpOptionType> getMappings()
    {
        if (mappings == null)
        {
            synchronized (GXDLMSIp4SetupIpOptionType.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, GXDLMSIp4SetupIpOptionType>();
                }
            }
        }
        return mappings;
    }

    private GXDLMSIp4SetupIpOptionType(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
            return intValue;
    }

    public static GXDLMSIp4SetupIpOptionType forValue(int value)
    {
            return getMappings().get(value);
    }
}