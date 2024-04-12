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

import gurux.dlms.objects.enums.GainResolution;
import gurux.dlms.objects.enums.Modulation;

/**
 * The neighbour table contains information about all the devices within the POS
 * of the device
 */
public class GXDLMSNeighbourTable {
    /**
     * MAC Short Address of the node.
     */
    private int shortAddress;

    /**
     * Is Payload Modulation scheme used.
     */
    private boolean enabled;

    /**
     * Frequency sub-band can be used for communication with the device.
     */
    private String toneMap;

    /**
     * Modulation type.
     */
    private Modulation modulation = Modulation.ROBUST_MODE;
    /**
     * Tx Gain.
     */
    private byte txGain;

    /**
     * Tx Gain resolution.
     */
    private GainResolution txRes = GainResolution.DB6;

    /**
     * Transmitter gain for each group of tones represented by one valid bit of
     * the tone map.
     */
    private String txCoeff;

    /**
     * Link Quality Indicator.
     */
    private short lqi;
    /**
     * Phase difference in multiples of 60 degrees between the mains phase of
     * the local node and the neighbour node.
     */
    private byte phaseDifferential;
    /**
     * Remaining time in minutes until which the tone map response parameters in
     * the neighbour table are considered valid.
     */
    private short tMRValidTime;
    /**
     * Remaining time in minutes until which this entry in the neighbour table
     * is considered valid.
     */
    private short neighbourValidTime;

    /**
     * @return MAC Short Address of the node.
     */
    public final int getShortAddress() {
        return shortAddress;
    }

    /**
     * @param value
     *            MAC Short Address of the node.
     */
    public final void setShortAddress(final int value) {
        shortAddress = value;
    }

    /**
     * @return Is Payload Modulation scheme used.
     */
    public final boolean getEnabled() {
        return enabled;
    }

    /**
     * @param value
     *            Is Payload Modulation scheme used.
     */
    public final void setEnabled(boolean value) {
        enabled = value;
    }

    /**
     * @return Frequency sub-band can be used for communication with the device.
     */
    public final String getToneMap() {
        return toneMap;
    }

    /**
     * @param value
     *            Frequency sub-band can be used for communication with the
     *            device.
     */
    public final void setToneMap(String value) {
        toneMap = value;
    }

    /**
     * @return Modulation type.
     */
    public final Modulation getModulation() {
        return modulation;
    }

    /**
     * @param value
     *            Modulation type.
     */
    public final void setModulation(Modulation value) {
        modulation = value;
    }

    /**
     * @return Tx Gain.
     */
    public final byte getTxGain() {
        return txGain;
    }

    /**
     * Tx Gain.
     * 
     * @param value
     */
    public final void setTxGain(byte value) {
        txGain = value;
    }

    /**
     * @return Tx Gain resolution.
     */
    public final GainResolution getTxRes() {
        return txRes;
    }

    /**
     * @param value
     *            Tx Gain resolution.
     */
    public final void setTxRes(GainResolution value) {
        txRes = value;
    }

    /**
     * @return Transmitter gain for each group of tones represented by one valid
     *         bit of the tone map.
     */
    public final String getTxCoeff() {
        return txCoeff;
    }

    /**
     * @param value
     *            Transmitter gain for each group of tones represented by one
     *            valid bit of the tone map.
     */
    public final void setTxCoeff(String value) {
        txCoeff = value;
    }

    /**
     * @return Link Quality Indicator.
     */
    public final short getLqi() {
        return lqi;
    }

    /**
     * @param value
     *            Link Quality Indicator.
     */
    public final void setLqi(final short value) {
        lqi = value;
    }

    /**
     * @return Phase difference in multiples of 60 degrees between the mains
     *         phase of the local node and the neighbour node.
     */
    public final byte getPhaseDifferential() {
        return phaseDifferential;
    }

    /**
     * @param value
     *            Phase difference in multiples of 60 degrees between the mains
     *            phase of the local node and the neighbour node.
     */
    public final void setPhaseDifferential(byte value) {
        phaseDifferential = value;
    }

    /**
     * @return Remaining time in minutes until which the tone map response
     *         parameters in the neighbour table are considered valid.
     */
    public final short getTMRValidTime() {
        return tMRValidTime;
    }

    /**
     * @param value
     *            Remaining time in minutes until which the tone map response
     *            parameters in the neighbour table are considered valid.
     */
    public final void setTMRValidTime(short value) {
        tMRValidTime = value;
    }

    /**
     * @return Remaining time in minutes until which this entry in the neighbour
     *         table is considered valid.
     */
    public final short getNeighbourValidTime() {
        return neighbourValidTime;
    }

    /**
     * @param value
     *            Remaining time in minutes until which this entry in the
     *            neighbour table is considered valid.
     */
    public final void setNeighbourValidTime(final short value) {
        neighbourValidTime = value;
    }
}