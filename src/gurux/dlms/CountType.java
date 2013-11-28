/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms;

enum CountType
{
    TAG(0x1),
    DATA(0x2),
    PACKET(0x3);

    private int Value;
    private static java.util.HashMap<Integer, CountType> mappings;
    private static java.util.HashMap<Integer, CountType> getMappings()
    {
        if (mappings == null)
        {
            synchronized (CountType.class)
            {
                if (mappings == null)
                {
                    mappings = new java.util.HashMap<Integer, CountType>();
                }
            }
        }
        return mappings;
    }

    private CountType(int value)
    {
        Value = value;
        getMappings().put(value, this);
    }

    public int getValue()
    {
        return Value;
    }

    public static CountType forValue(int value)
    {
        return getMappings().get(value);
    }
}