/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

public class GXDLMSModemInitialisation
{
    private String Request;
    private String Response;
    private int Delay;
    
    public final String getRequest()
    {
        return Request;
    }
    public final void setRequest(String value)
    {
        Request = value;
    }

    public final String getResponse()
    {
        return Response;
    }
    public final void setResponse(String value)
    {
        Response = value;
    }

    public final int getDelay()
    {
        return Delay;
    }

    public final void setDelay(int value)
    {
        Delay = value;
    }
    
    @Override 
    public String toString()
    {
        return Request + " " + Response + " " + Delay;
    }
}