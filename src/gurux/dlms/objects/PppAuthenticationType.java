/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

 /** 
Ppp Authentication Type
 */
public enum PppAuthenticationType
{
    /** 
     No authentication.
    */
    None(0),
    /** 
     PAP Login
    */
    PAP(1),
    /** 
     CHAP-algorithm
    */
    CHAP(2);

    private int intValue;
    private static java.util.HashMap<Integer, PppAuthenticationType> mappings;
    private static java.util.HashMap<Integer, PppAuthenticationType> getMappings()
    {
        if (mappings == null)
        {
            synchronized (PppAuthenticationType.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, PppAuthenticationType>();
                }
            }
        }
        return mappings;
    }

    private PppAuthenticationType(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return intValue;
    }

    public static PppAuthenticationType forValue(int value)
    {
        return getMappings().get(value);
    }
}