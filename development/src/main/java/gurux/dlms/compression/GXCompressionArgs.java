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

import gurux.dlms.compression.enums.CompressionOperation;

/**
 * V.44 compression arguments.
 */
public class GXCompressionArgs {
    /**
     * Compression operation to perform.
     */
    private CompressionOperation operation;

    /**
     * Compression options for the V.44 compression.
     */
    private GXCompressionOptions options;

    /**
     * Input data that is being compressed or decompressed.
     */
    private byte[] inputData;

    /**
     * Output data produced by the compression or decompression operation.
     */
    private byte[] outputData;

    /**
     * Constructor.
     *
     * @param forOperation
     *            Compression operation to perform.
     * @param forOptions
     *            Compression options for the V.44 compression.
     * @param forInputData
     *            Input data that is being compressed or decompressed.
     */
    public GXCompressionArgs(final CompressionOperation forOperation,
            final GXCompressionOptions forOptions, final byte[] forInputData) {
        operation = forOperation;
        options = forOptions;
        inputData = forInputData;
    }

    /**
     * @return Compression operation to perform.
     */
    public final CompressionOperation getOperation() {
        return operation;
    }

    /**
     * @param value
     *            Compression operation to perform.
     */
    final void setOperation(final CompressionOperation value) {
        operation = value;
    }

    /**
     * @return Compression options for the V.44 compression.
     */
    public final GXCompressionOptions getOptions() {
        return options;
    }

    /**
     * @param value
     *            Compression options for the V.44 compression.
     */
    final void setOptions(final GXCompressionOptions value) {
        options = value;
    }

    /**
     * @return Input data that is being compressed or decompressed.
     */
    public final byte[] getInputData() {
        return inputData;
    }

    /**
     * @param value
     *            Input data that is being compressed or decompressed.
     */
    final void setInputData(final byte[] value) {
        inputData = value;
    }

    /**
     * @return Output data produced by the compression or decompression
     *         operation.
     */
    public final byte[] getOutputData() {
        return outputData;
    }

    /**
     * @param value
     *            Output data produced by the compression or decompression
     *            operation.
     */
    public final void setOutputData(final byte[] value) {
        outputData = value;
    }
}
