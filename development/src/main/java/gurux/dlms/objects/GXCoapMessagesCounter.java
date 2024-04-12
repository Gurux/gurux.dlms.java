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
 * CoAP messages counter.
 */
public class GXCoapMessagesCounter {
    /**
     * Transmit messages.
     */
    private long tx;

    /**
     * Received messages.
     */
    private long rx;
    /**
     * CoAP messages that have been re-sent.
     */
    private long rxResend;

    /**
     * Received CoAP reset messages.
     */
    private long rxReset;
    /**
     * Transmit CoAP reset messages.
     */
    private long txReset;

    /**
     * Received CoAP acknowledgement messages.
     */
    private long rxAck;

    /**
     * Transmit CoAP acknowledgement messages.
     */
    private long txAck;
    /**
     * The number of CoAP messages received but silently dropped due to message
     * format error or other reason.
     */
    private long rxDrop;

    /**
     * The number of CoAP responses that were returned non-piggybacked.
     */
    private long txNonPiggybacked;
    /**
     * The number of times transmission of a CoAP message was abandoned due to
     * exceed of the max retransmission counter of the CoAP messaging layer.
     */
    private long maxRtxExceeded;

    /**
     * @return Transmit messages.
     */
    public final long getTx() {
        return tx;
    }

    /**
     * @param value
     *            Transmit messages.
     */
    public final void setTx(final long value) {
        tx = value;
    }

    /**
     * @return Received messages.
     */
    public final long getRx() {
        return rx;
    }

    /**
     * @param value
     *            Received messages.
     */
    public final void setRx(final long value) {
        rx = value;
    }

    /**
     * @return CoAP messages that have been re-sent.
     */
    public final long getTxResend() {
        return rxResend;
    }

    /**
     * @param value
     *            CoAP messages that have been re-sent.
     */
    public final void setTxResend(final long value) {
        rxResend = value;
    }

    /**
     * @return Received CoAP reset messages.
     */
    public final long getRxReset() {
        return rxReset;
    }

    /**
     * @param value
     *            Received CoAP reset messages.
     */
    public final void setRxReset(final long value) {
        rxReset = value;
    }

    /**
     * @return Transmit CoAP reset messages.
     */
    public final long getTxReset() {
        return txReset;
    }

    /**
     * @param value
     *            Transmit CoAP reset messages.
     */
    public final void setTxReset(final long value) {
        txReset = value;
    }

    /**
     * @return Received CoAP acknowledgement messages.
     */
    public final long getRxAck() {
        return rxAck;
    }

    /**
     * @param value
     *            Received CoAP acknowledgement messages.
     */
    public final void setRxAck(final long value) {
        rxAck = value;
    }

    /**
     * @return Transmit CoAP acknowledgement messages.
     */
    public final long getTxAck() {
        return txAck;
    }

    /**
     * @param value
     *            Transmit CoAP acknowledgement messages.
     */
    public final void setTxAck(final long value) {
        txAck = value;
    }

    /**
     * @return The number of CoAP messages received but silently dropped due to
     *         message format error or other reason.
     */
    public final long getRxDrop() {
        return rxDrop;
    }

    /**
     * @param value
     *            The number of CoAP messages received but silently dropped due
     *            to message format error or other reason.
     */
    public final void setRxDrop(final long value) {
        rxDrop = value;
    }

    /**
     * @return The number of CoAP responses that were returned non-piggybacked.
     */
    public final long getTxNonPiggybacked() {
        return txNonPiggybacked;
    }

    /**
     * @param value
     *            The number of CoAP responses that were returned
     *            non-piggybacked.
     */
    public final void setTxNonPiggybacked(final long value) {
        txNonPiggybacked = value;
    }

    /**
     * @return The number of times transmission of a CoAP message was abandoned
     *         due to exceed of the max retransmission counter of the CoAP
     *         messaging layer.
     */
    public final long getMaxRtxExceeded() {
        return maxRtxExceeded;
    }

    /**
     * @param value
     *            The number of times transmission of a CoAP message was
     *            abandoned due to exceed of the max retransmission counter of
     *            the CoAP messaging layer.
     */
    public final void setMaxRtxExceeded(final long value) {
        maxRtxExceeded = value;
    }
}