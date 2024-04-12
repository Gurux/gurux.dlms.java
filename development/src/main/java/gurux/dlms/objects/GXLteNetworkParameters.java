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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

/**
 * Network parameters for the LTE network
 */
public class GXLteNetworkParameters {
    /**
     * T3402 timer in seconds.
     */
    private int t3402;

    /**
     * T3412 timer in seconds.
     */
    private int t3412;
    /**
     * T3412ext2 timer in seconds.
     */
    private long t3412ext2;
    /**
     * Power saving mode active timer timer in 0,01 seconds.
     */
    private int t3324;
    /**
     * Extended idle mode DRX cycle timer in 0,01 seconds.
     */
    private long teDRX;

    /**
     * DRX paging time window timer in seconds.
     */
    private int tPTW;
    /**
     * The minimum required Rx level in the cell in dBm.
     */
    private byte qRxlevMin;
    /**
     * The minimum required Rx level in enhanced coverage CE Mode A.
     */
    private byte qRxlevMinCE;

    /**
     * @return T3402 timer in seconds.
     */
    public final int getT3402() {
        return t3402;
    }

    /**
     * @param value
     *            T3402 timer in seconds.
     */
    public final void setT3402(final int value) {
        t3402 = value;
    }

    /**
     * @return T3412 timer in seconds.
     */
    public final int getT3412() {
        return t3412;
    }

    /**
     * @param value
     *            T3412 timer in seconds.
     */
    public final void setT3412(final int value) {
        t3412 = value;
    }

    /**
     * @return T3412ext2 timer in seconds.
     */
    public final long getT3412ext2() {
        return t3412ext2;
    }

    /**
     * @param value
     *            T3412ext2 timer in seconds.
     */
    public final void setT3412ext2(final long value) {
        t3412ext2 = value;
    }

    /**
     * @return Power saving mode active timer timer in 0,01 seconds.
     */
    public final int getT3324() {
        return t3324;
    }

    /**
     * @param value
     *            Power saving mode active timer timer in 0,01 seconds.
     */
    public final void setT3324(final int value) {
        t3324 = value;
    }

    /**
     * @return Extended idle mode DRX cycle timer in 0,01 seconds.
     */
    public final long getTeDRX() {
        return teDRX;
    }

    /**
     * @param value
     *            Extended idle mode DRX cycle timer in 0,01 seconds.
     */
    public final void setTeDRX(final long value) {
        teDRX = value;
    }

    /**
     * @return DRX paging time window timer in seconds.
     */
    public final int getTPTW() {
        return tPTW;
    }

    /**
     * @param value
     *            DRX paging time window timer in seconds.
     */
    public final void setTPTW(final int value) {
        tPTW = value;
    }

    /**
     * @return The minimum required Rx level in the cell in dBm.
     */
    public final byte getQRxlevMin() {
        return qRxlevMin;
    }

    /**
     * @param value
     *            The minimum required Rx level in the cell in dBm.
     */
    public final void setQRxlevMin(final byte value) {
        qRxlevMin = value;
    }

    /**
     * @return The minimum required Rx level in enhanced coverage CE Mode A.
     */
    public final byte getQRxlevMinCE() {
        return qRxlevMinCE;
    }

    /**
     * @param value
     *            The minimum required Rx level in enhanced coverage CE Mode A.
     */
    public final void setQRxlevMinCE(final byte value) {
        qRxlevMinCE = value;
    }

    /**
     * The minimum required Rx level in enhanced coverage CE Mode B.
     */
    private byte privateQRxLevMinCE1;

    public final byte getQRxLevMinCE1() {
        return privateQRxLevMinCE1;
    }

    public final void setQRxLevMinCE1(byte value) {
        privateQRxLevMinCE1 = value;
    }
}