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
 * MAC PHY communication parameters.
 */
public class GXMacPhyCommunication {
    /**
     * EUI is the EUI-48 of the other device.
     */
    private byte[] eui;

    /**
     * The tx power of GPDU packets sent to the device.
     */
    private byte txPower;

    /**
     * The Tx coding of GPDU packets sent to the device.
     */
    private byte txCoding;

    /**
     * The Rx coding of GPDU packets received from the device.
     */
    private byte rxCoding;
    /**
     * The Rx power level of GPDU packets received from the device.
     */
    private byte rxLvl;
    /**
     * SNR of GPDU packets received from the device.
     */
    private byte snr;
    /**
     * The number of times the Tx power was modified.
     */
    private byte txPowerModified;
    /**
     * The number of times the Tx coding was modified.
     */
    private byte txCodingModified;
    /**
     * The number of times the Rx coding was modified.
     */
    private byte rxCodingModified;

    /**
     * @return EUI is the EUI-48 of the other device.
     */
    public final byte[] getEui() {
        return eui;
    }

    /**
     * @param value
     *            EUI is the EUI-48 of the other device.
     */
    public final void setEui(final byte[] value) {
        eui = value;
    }

    /**
     * @return The tx power of GPDU packets sent to the device.
     */
    public final byte getTxPower() {
        return txPower;
    }

    /**
     * @param value
     *            The tx power of GPDU packets sent to the device.
     */
    public final void setTxPower(final byte value) {
        txPower = value;
    }

    /**
     * @return The Tx coding of GPDU packets sent to the device.
     */
    public final byte getTxCoding() {
        return txCoding;
    }

    /**
     * @param value
     *            The Tx coding of GPDU packets sent to the device.
     */
    public final void setTxCoding(final byte value) {
        txCoding = value;
    }

    /**
     * @return The Rx coding of GPDU packets received from the device.
     */
    public final byte getRxCoding() {
        return rxCoding;
    }

    /**
     * @param value
     *            The Rx coding of GPDU packets received from the device.
     */
    public final void setRxCoding(final byte value) {
        rxCoding = value;
    }

    /**
     * @return The Rx power level of GPDU packets received from the device.
     */
    public final byte getRxLvl() {
        return rxLvl;
    }

    /**
     * @param value
     *            The Rx power level of GPDU packets received from the device.
     */
    public final void setRxLvl(final byte value) {
        rxLvl = value;
    }

    /**
     * @return SNR of GPDU packets received from the device.
     */
    public final byte getSnr() {
        return snr;
    }

    /**
     * @param value
     *            SNR of GPDU packets received from the device.
     */
    public final void setSnr(final byte value) {
        snr = value;
    }

    /**
     * @return The number of times the Tx power was modified.
     */
    public final byte getTxPowerModified() {
        return txPowerModified;
    }

    /**
     * @param value
     *            The number of times the Tx power was modified.
     */
    public final void setTxPowerModified(final byte value) {
        txPowerModified = value;
    }

    /**
     * @return The number of times the Tx coding was modified.
     */
    public final byte getTxCodingModified() {
        return txCodingModified;
    }

    /**
     * @param value
     *            The number of times the Tx coding was modified.
     */
    public final void setTxCodingModified(final byte value) {
        txCodingModified = value;
    }

    /**
     * @return The number of times the Rx coding was modified.
     */
    public final byte getRxCodingModified() {
        return rxCodingModified;
    }

    /**
     * @param value
     *            The number of times the Rx coding was modified.
     */
    public final void setRxCodingModified(final byte value) {
        rxCodingModified = value;
    }

}