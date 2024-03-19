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
 * This class is used to count repetition delay for the next push message.
 */
public class GXRepetitionDelay {
    /**
     * The minimum delay until a next push attempt is started in seconds.
     */
    private int min;

    /**
     * Calculating the next delay.
     */
    private int exponent;

    /**
     * The maximum delay until a next push attempt is started in seconds.
     */
    private int max;

    /**
     * @return The minimum delay until a next push attempt is started in
     *         seconds.
     */
    public final int getMin() {
        return min;
    }

    /**
     * @param value
     *            The minimum delay until a next push attempt is started in
     *            seconds.
     */
    public final void setMin(final int value) {
        min = value;
    }

    /**
     * @return Calculating the next delay.
     */
    public final int getExponent() {
        return exponent;
    }

    /**
     * @param value
     *            Calculating the next delay.
     */
    public final void setExponent(final int value) {
        exponent = value;
    }

    /**
     * @return The maximum delay until a next push attempt is started in
     *         seconds.
     */
    public final int getMax() {
        return max;
    }

    /**
     * @param value
     *            The maximum delay until a next push attempt is started in
     *            seconds.
     */
    public final void setMax(final int value) {
        max = value;
    }
}