/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.enums;

/** 
 Defines the minimum time between the reception of a request 
(end of request telegram) and the transmission of the response (begin of response telegram).
*/
public enum LocalPortResponseTime
{
    /** 
     Minimium time is 20 ms.
    */
    ms20(0),
    /** 
     Minimium time is 200 ms.
    */
    ms200(1);

    private int intValue;
    private static java.util.HashMap<Integer, LocalPortResponseTime> mappings;
    private static java.util.HashMap<Integer, LocalPortResponseTime> getMappings()
    {
        synchronized (LocalPortResponseTime.class)
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, LocalPortResponseTime>();
            }
        }
        return mappings;
    }

    private LocalPortResponseTime(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return intValue;
    }

    public static LocalPortResponseTime forValue(int value)
    {
        return getMappings().get(value);
    }
}