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
 * OBIS Code class is used to find out default description to OBIS Code.
 */
class GXStandardObisCode {
    private String[] obis;
    private String dataType;
    private String interfaces;
    private String description;

    /**
     * Constructor.
     */
    GXStandardObisCode() {

    }

    /**
     * Constructor.
     */
    GXStandardObisCode(final String[] forObis, final String desc,
            final String forInterfaces, final String forDataType) {
        obis = new String[6];
        if (forObis != null) {
            System.arraycopy(forObis, 0, obis, 0, 6);
        }
        this.setDescription(desc);
        this.setInterfaces(forInterfaces);
        setDataType(forDataType);
    }

    /**
     * OBIS code.
     */
    public final String[] getOBIS() {
        return obis;
    }

    public final void setOBIS(final String[] value) {
        obis = value;
    }

    /**
     * OBIS code description.
     */
    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String value) {
        description = value;
    }

    /**
     * Interfaces that are using this OBIS code.
     */
    public final String getInterfaces() {
        return interfaces;
    }

    public final void setInterfaces(final String value) {
        interfaces = value;
    }

    /**
     * Standard data types.
     */
    public final String getDataType() {
        return dataType;
    }

    public final void setDataType(final String value) {
        dataType = value;
    }

    /**
     * Convert to string.
     * 
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String s : obis) {
            if (builder.length() != 0) {
                builder.append(".");
            }
            builder.append(s);
        }
        return builder.toString() + " " + getDescription();
    }
}
