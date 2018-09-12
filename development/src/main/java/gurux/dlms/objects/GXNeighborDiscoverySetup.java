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

/**
 * Contains the configuration to be used for both routers and hosts to support the Neighbor Discovery protocol for IPv6.

 * Online help: <br>
 * http://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSIp6Setup
 */
/** 
*/
public class GXNeighborDiscoverySetup {
    /**
     * Gives the maximum number of router solicitation retries to be performed
     * by a node if the expected router advertisement has not been received.
     */
    private int maxRetry;
    /**
     * Gives the waiting time in milliseconds between two successive router
     * solicitation retries.
     */
    private int retryWaitTime;
    /**
     * Gives the router advertisement transmission period in seconds.
     */
    private long sendPeriod;

    /**
     * Constructor.
     */
    public GXNeighborDiscoverySetup() {
        setMaxRetry(3);
        setRetryWaitTime(10000);
    }

    public final int getMaxRetry() {
        return maxRetry;
    }

    public final void setMaxRetry(final int value) {
        maxRetry = value;
    }

    public final int getRetryWaitTime() {
        return retryWaitTime;
    }

    public final void setRetryWaitTime(final int value) {
        retryWaitTime = value;
    }

    /**
     * @return Gives the router advertisement transmission period in seconds.
     */
    public final long getSendPeriod() {
        return sendPeriod;
    }

    /**
     * @param value
     *            Router advertisement transmission period in seconds.
     */
    public final void setSendPeriod(final long value) {
        sendPeriod = value;
    }
}