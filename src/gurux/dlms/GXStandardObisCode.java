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

package gurux.dlms;

 /** 
    OBIS Code class is used to find out default descrition to OBIS Code.
 */
class GXStandardObisCode
{
    private String[] OBIS;
    private String DataType;        
    private String Interfaces;        
    private String Description;

    /** 
     Constructor.
    */
    public GXStandardObisCode()
    {

    }

    /** 
     Constructor.
    */
    public GXStandardObisCode(String[] obis, String desc, 
            String interfaces, String dataType)
    {
        OBIS = new String[6];
        if (obis != null)
        {                
            System.arraycopy(obis, 0, OBIS, 0, 6);
        }
        this.setDescription(desc);
        this.setInterfaces(interfaces);
        setDataType(dataType);
    }

    /** 
     OBIS code.
    */        
    public final String[] getOBIS()
    {
        return OBIS;
    }
    public final void setOBIS(String[] value)
    {
        OBIS = value;
    }

    /** 
     OBIS code description.
    */
    public final String getDescription()
    {
        return Description;
    }
    public final void setDescription(String value)
    {
        Description = value;
    }

    /** 
     Interfaces that are using this OBIS code.
    */
    public final String getInterfaces()
    {
        return Interfaces;
    }
    public final void setInterfaces(String value)
    {
        Interfaces = value;
    }

    /** 
     Standard data types.
    */        
    public final String getDataType()
    {
        return DataType;
    }
    public final void setDataType(String value)
    {
        DataType = value;
    }

    /** 
     Convert to string.
     @return 
    */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(String s : OBIS)
        {
            if (builder.length() != 0)
            {
                builder.append(".");
            }
            builder.append(s);
        }
        return builder.toString() + " " + getDescription();
    }
}
