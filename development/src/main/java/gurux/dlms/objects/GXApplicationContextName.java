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

public class GXApplicationContextName {
    private String logicalName;
    private int jointIsoCtt;
    private int country;
    private int countryName;
    private int identifiedOrganization;
    private int dlmsUA;
    private int applicationContext;
    private int contextId;

    public final String getLogicalName() {
        return logicalName;
    }

    public final void setLogicalName(final String value) {
        logicalName = value;
    }

    public final int getJointIsoCtt() {
        return jointIsoCtt;
    }

    public final void setJointIsoCtt(final int value) {
        jointIsoCtt = value;
    }

    public final int getCountry() {
        return country;
    }

    public final void setCountry(final int value) {
        country = value;
    }

    public final int getCountryName() {
        return countryName;
    }

    public final void setCountryName(final int value) {
        countryName = value;
    }

    public final int getIdentifiedOrganization() {
        return identifiedOrganization;
    }

    public final void setIdentifiedOrganization(final int value) {
        identifiedOrganization = value;
    }

    public final int getDlmsUA() {
        return dlmsUA;
    }

    public final void setDlmsUA(final int value) {
        dlmsUA = value;
    }

    public final int getApplicationContext() {
        return applicationContext;
    }

    public final void setApplicationContext(final int value) {
        applicationContext = value;
    }

    public final int getContextId() {
        return contextId;
    }

    public final void setContextId(final int value) {
        contextId = value;
    }

    @Override
    public final String toString() {
        String name = "";
        if (logicalName != null) {
            name = logicalName;
        }
        return name + " " + String.valueOf(jointIsoCtt) + " "
                + String.valueOf(country) + " " + String.valueOf(countryName)
                + " " + String.valueOf(identifiedOrganization) + " "
                + String.valueOf(dlmsUA) + " "
                + String.valueOf(applicationContext) + " "
                + String.valueOf(contextId);
    }
}