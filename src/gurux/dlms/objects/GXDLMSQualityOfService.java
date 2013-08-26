//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

public class GXDLMSQualityOfService 
{
    int m_Precedence;
    int m_Delay;
    int m_Reliability;
    int m_PeakThroughput;
    int m_MeanThroughput;
    
    public final int getPrecedence()
    {
        return m_Precedence;
    }
    public final void setPrecedence(int value)
    {
        m_Precedence = value;
    }
    
    public final int getDelay()
    {
        return m_Delay;
    }
    public final void setDelay(int value)
    {
        m_Delay = value;
    }
    
    public final int getReliability()
    {
        return m_Reliability;
    }
    public final void setReliability(int value)
    {
        m_Reliability = value;
    }
    
    public final int getPeakThroughput()
    {
        return m_PeakThroughput;
    }
    public final void setPeakThroughput(int value)
    {
        m_PeakThroughput = value;
    }
    
    public final int getMeanThroughput()
    {
        return m_MeanThroughput;
    }
    public final void setMeanThroughput(int value)
    {
        m_MeanThroughput = value;
    }
}
