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

import gurux.dlms.objects.enums.CertificateEntity;
import gurux.dlms.objects.enums.CertificateType;

public class GXDLMSCertificateInfo {
    /**
     * Used certificate entity.
     */
    private CertificateEntity entity = CertificateEntity.SERVER;

    /**
     * Used certificate type.
     */
    private CertificateType type = CertificateType.DIGITAL_SIGNATURE;

    /**
     * Certificate serial number.
     */
    private String serialNumber;

    /**
     * Certificate issuer.
     */
    private String issuer;
    /**
     * Certificate subject.
     */
    private String subject;

    /**
     * Certificate subject alt name.
     */
    private String subjectAltName;

    /**
     * @return Used certificate entity.
     */
    public final CertificateEntity getEntity() {
        return entity;
    }

    /**
     * @param value
     *            Used certificate entity.
     */
    public final void setEntity(final CertificateEntity value) {
        entity = value;
    }

    /**
     * @return Used certificate type.
     */
    public final CertificateType getType() {
        return type;
    }

    /**
     * @param value
     *            Used certificate type.
     */
    public final void setType(final CertificateType value) {
        type = value;
    }

    /**
     * @return Certificate serial number.
     */
    public final String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param value
     *            Certificate serial number.
     */
    public final void setSerialNumber(final String value) {
        serialNumber = value;
    }

    /**
     * @return Certificate issuer.
     */
    public final String getIssuer() {
        return issuer;
    }

    /**
     * @param value
     *            Certificate issuer.
     */
    public final void setIssuer(final String value) {
        issuer = value;
    }

    /**
     * @return Certificate subject.
     */
    public final String getSubject() {
        return subject;
    }

    /**
     * @param value
     *            Certificate subject.
     */
    public final void setSubject(final String value) {
        subject = value;
    }

    /**
     * @return Certificate subject alt name.
     */
    public final String getSubjectAltName() {
        return subjectAltName;
    }

    /**
     * @param value
     *            Certificate subject alt name.
     */
    public final void setSubjectAltName(final String value) {
        subjectAltName = value;
    }

}