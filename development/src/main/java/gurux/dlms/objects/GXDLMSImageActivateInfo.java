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

public class GXDLMSImageActivateInfo {
    private long size;
    private String identification;
    private String signature;

    /**
     * @return Image_size is the size of the Image(s) to be activated. Expressed
     *         in octets;
     */
    public final long getSize() {
        return size;
    }

    /**
     * @param value
     *            Image_size is the size of the Image(s) to be activated.
     *            Expressed in octets;
     */
    public final void setSize(final long value) {
        size = value;
    }

    /**
     * @return Image identification is the identification of the Image(s) to be
     *         activated, and may contain information like manufacturer, device
     *         type, version information, etc.
     */
    public final String getIdentification() {
        return identification;
    }

    /**
     * @param value
     *            Image identification is the identification of the Image(s) to
     *            be activated, and may contain information like manufacturer,
     *            device type, version information, etc.
     */
    public final void setIdentification(final String value) {
        identification = value;
    }

    /**
     * @return Image signature is the signature of the Image(s) to be activated.
     */
    public final String getSignature() {
        return signature;
    }

    /**
     * @param value
     *            Image signature is the signature of the Image(s) to be
     *            activated.
     */
    public final void setSignature(final String value) {
        signature = value;
    }

    /**
     * Constructor.
     */
    public GXDLMSImageActivateInfo() {
        size = 0;
        identification = "";
        signature = "";
    }

    /**
     * Constructor.
     * 
     * @param forSize
     *            Size.
     * @param forIdentification
     *            Identification.
     * @param forSignature
     *            Signature.
     */
    public GXDLMSImageActivateInfo(final long forSize,
            final String forIdentification, final String forSignature) {
        size = forSize;
        identification = forIdentification;
        signature = forSignature;
    }

    @Override
    public final String toString() {
        return identification + " " + signature + " " + String.valueOf(size);
    }
}