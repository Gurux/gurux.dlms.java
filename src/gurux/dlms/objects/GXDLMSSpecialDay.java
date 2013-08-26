/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gurux.dlms.objects;

import gurux.dlms.GXDateTime;

public class GXDLMSSpecialDay
{
    private int privateIndex;
    private GXDateTime privateDate;
    private int privateDayId;

    public final int getIndex()
    {
        return privateIndex;
    }
    public final void setIndex(int value)
    {
        privateIndex = value;
    }

    public final GXDateTime getDate()
    {
        return privateDate;
    }
    public final void setDate(GXDateTime value)
    {
        privateDate = value;
    }

    public final int getDayId()
    {
        return privateDayId;
    }
    public final void setDayId(int value)
    {
        privateDayId = value;
    }
}