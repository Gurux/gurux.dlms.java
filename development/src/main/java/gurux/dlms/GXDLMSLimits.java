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
 * GXDLMSLimits contains commands for retrieving and setting the limits of field
 * length and window size, when communicating with the server.
 */
public class GXDLMSLimits {
    static final int DEFAULT_MAX_INFO_TX = 128;
    static final int DEFAULT_MAX_INFO_RX = 128;
    static final int DEFAULT_WINDOWS_SIZE_TX = 1;
    static final int DEFAULT_WINDOWS_SIZE_RX = 1;

    /**
     * The maximum information field length in transmit. DefaultValue is 128.
     */
    private int maxInfoTX;
    /**
     * The maximum information field length in receive. DefaultValue is 62.
     */
    private int maxInfoRX;
    /**
     * The window size in transmit. DefaultValue is 1.
     */
    private int windowSizeTX;
    /**
     * The window size in receive. DefaultValue is 1.
     */
    private int windowSizeRX;

    /**
     * Constructor.
     */
    GXDLMSLimits() {
        setMaxInfoTX(DEFAULT_MAX_INFO_TX);
        setMaxInfoRX(DEFAULT_MAX_INFO_RX);
        setWindowSizeTX(DEFAULT_WINDOWS_SIZE_TX);
        setWindowSizeRX(DEFAULT_WINDOWS_SIZE_RX);
    }

    /**
     * Gets maximum information field length in transmit. DefaultValue is 128.
     * 
     * @return Maximum information field length in transmit.
     */
    public final int getMaxInfoTX() {
        return maxInfoTX;
    }

    /**
     * Sets maximum information field length in transmit.
     * 
     * @param value
     *            Maximum information field length in transmit.
     */
    public final void setMaxInfoTX(final int value) {
        maxInfoTX = value;
    }

    /**
     * Get the maximum information field length in receive. DefaultValue is 62.
     * 
     * @return The maximum information field length in receive.
     */
    public final int getMaxInfoRX() {
        return maxInfoRX;
    }

    /**
     * Set the maximum information field length in receive.
     * 
     * @param value
     *            The maximum information field length in receive.
     */
    public final void setMaxInfoRX(final int value) {
        maxInfoRX = value;
    }

    /**
     * Get The window size in transmit. DefaultValue is 1.
     * 
     * @return The window size in transmit.
     */
    public final int getWindowSizeTX() {
        return windowSizeTX;
    }

    /**
     * Set the window size in transmit.
     * 
     * @param value
     *            The window size in transmit.
     */
    public final void setWindowSizeTX(final int value) {
        windowSizeTX = value;
    }

    /**
     * Get the window size in receive. DefaultValue is 1.
     * 
     * @return The window size in receive..
     */
    public final int getWindowSizeRX() {
        return windowSizeRX;
    }

    /**
     * Set the window size in receive.
     * 
     * @param value
     *            The window size in receive.
     */
    public final void setWindowSizeRX(final int value) {
        windowSizeRX = value;
    }
}