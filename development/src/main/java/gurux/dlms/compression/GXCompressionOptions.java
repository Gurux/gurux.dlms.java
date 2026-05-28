//
// --------------------------------------------------------------------------
//  Gurux Ltd
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

package gurux.dlms.compression;

import gurux.dlms.compression.enums.MaximumStringLength;

/**
 * V.44 compression options.
 */
public class GXCompressionOptions {
    /**
     * Default codeword size.
     */
    private int defaultCodewordSize = 6;

    /**
     * Default ordinal size.
     */
    private int defaultOrdinalSize = 7;

    /**
     * Is V.44 compression enabled.
     */
    private boolean enableCompression;

    /**
     * Maximum number of codewords that can be processed. N2.
     */
    private int maxCodewords = 1024;

    /**
     * The maximum string length. N7T is counted from this.
     */
    private MaximumStringLength maximumStringLength =
            MaximumStringLength.VALUE_255;

    /**
     * Max dictionary size.
     */
    private long maxDictionarySize = 3072;

    /**
     * @return Is V.44 compression enabled.
     */
    public final boolean getEnableCompression() {
        return enableCompression;
    }

    /**
     * @param value
     *            Is V.44 compression enabled.
     */
    public final void setEnableCompression(final boolean value) {
        enableCompression = value;
    }

    /**
     * @return Default codeword size.
     */
    public final int getDefaultCodewordSize() {
        return defaultCodewordSize;
    }

    /**
     * @param value
     *            Default codeword size.
     */
    public final void setDefaultCodewordSize(final int value) {
        if (value < 6 || value > 120) {
            throw new IllegalArgumentException(
                    "The minimum allowed codeword size is 6 bytes.");
        }
        defaultCodewordSize = value;
    }

    /**
     * @return Default ordinal size.
     */
    public final int getDefaultOrdinalSize() {
        return defaultOrdinalSize;
    }

    /**
     * @param value
     *            Default ordinal size.
     */
    public final void setDefaultOrdinalSize(final int value) {
        if (value < 7 || value > 8) {
            throw new IllegalArgumentException(
                    "The orginal size ranges from 7 to 8 bytes.");
        }
        defaultOrdinalSize = value;
    }

    /**
     * @return Maximum number of codewords that can be processed.
     */
    public final int getMaxCodewords() {
        return maxCodewords;
    }

    /**
     * @param value
     *            Maximum number of codewords that can be processed.
     */
    public final void setMaxCodewords(final int value) {
        if (value < 0 || value > 0xFFFF) {
            throw new IllegalArgumentException(
                    "The maximum codewords value must be between 0 and 65535.");
        }
        maxCodewords = value;
    }

    /**
     * @return The maximum string length.
     */
    public final MaximumStringLength getMaximumStringLength() {
        return maximumStringLength;
    }

    /**
     * @param value
     *            The maximum string length.
     */
    public final void setMaximumStringLength(
            final MaximumStringLength value) {
        maximumStringLength = value;
    }

    /**
     * @return Max dictionary size.
     */
    public final long getMaxDictionarySize() {
        return maxDictionarySize;
    }

    /**
     * @param value
     *            Max dictionary size.
     */
    public final void setMaxDictionarySize(final long value) {
        if (value < 0 || value > 0xFFFFFFFFL) {
            throw new IllegalArgumentException(
                    "The maximum dictionary size must be between 0 and 4294967295.");
        }
        maxDictionarySize = value;
    }
}
