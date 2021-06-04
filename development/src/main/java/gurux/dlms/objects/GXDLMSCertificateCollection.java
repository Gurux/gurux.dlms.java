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

import java.util.ArrayList;

import gurux.dlms.asn.GXAsn1Converter;
import gurux.dlms.objects.enums.CertificateEntity;
import gurux.dlms.objects.enums.CertificateType;

public class GXDLMSCertificateCollection extends ArrayList<GXDLMSCertificateInfo> {
	private static final long serialVersionUID = 1L;

	/**
	 * Find certificate with given parameters.
	 * 
	 * @param entity      Certificate entity.
	 * @param type        Certificate type.
	 * @param systemtitle System title.
	 * @return
	 */
	public GXDLMSCertificateInfo find(final CertificateEntity entity, final CertificateType type,
			final byte[] systemtitle) {
		String subject = GXAsn1Converter.systemTitleToSubject(systemtitle);
		for (GXDLMSCertificateInfo it : this) {
			if ((it.getEntity() == CertificateEntity.SERVER && entity == CertificateEntity.SERVER)
					|| (it.getEntity() == CertificateEntity.CLIENT && entity == CertificateEntity.CLIENT)
							&& it.getSubject().equals(subject)) {
				return it;
			}
		}
		return null;
	}
}