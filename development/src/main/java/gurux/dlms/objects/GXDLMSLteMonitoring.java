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

import javax.xml.stream.XMLStreamException;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.GXEnum;
import gurux.dlms.GXStructure;
import gurux.dlms.GXUInt16;
import gurux.dlms.GXUInt32;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.LteCoverageEnhancement;

/**
 * Online help: https://www.gurux.fi/Gurux.DLMS.Objects.GXDLMSLteMonitoring
 */
public class GXDLMSLteMonitoring extends GXDLMSObject implements IGXDLMSBase {
    /**
     * Network parameters for the LTE network.
     */
    private GXLteNetworkParameters networkParameters;

    /**
     * Quality of service of the LTE network.
     */
    private GXLteQualityOfService qualityOfService;

    /**
     * Constructor.
     */
    public GXDLMSLteMonitoring() {
        this("0.0.25.11.0.255", 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     */
    public GXDLMSLteMonitoring(String ln) {
        this(ln, 0);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Logical Name of the object.
     * @param sn
     *            Short Name of the object.
     */
    public GXDLMSLteMonitoring(String ln, int sn) {
        super(ObjectType.LTE_MONITORING, ln, sn);
        setVersion(1);
        networkParameters = new GXLteNetworkParameters();
        qualityOfService = new GXLteQualityOfService();
    }

    /**
     * @return Network parameters for the LTE network.
     */
    public final GXLteNetworkParameters getNetworkParameters() {
        return networkParameters;
    }

    /**
     * @param value
     *            Network parameters for the LTE network.
     */
    public final void setNetworkParameters(GXLteNetworkParameters value) {
        networkParameters = value;
    }

    /**
     * @return Quality of service of the LTE network.
     */
    public final GXLteQualityOfService getQualityOfService() {
        return qualityOfService;
    }

    /**
     * @param value
     *            Quality of service of the LTE network.
     */
    public final void setQualityOfService(GXLteQualityOfService value) {
        qualityOfService = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] { getLogicalName(), networkParameters, qualityOfService };
    }

    @Override
    public final int[] getAttributeIndexToRead(boolean all) {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        // LN is static and read only once.
        if (all || getLogicalName() == null || getLogicalName().compareTo("") == 0) {
            attributes.add(1);
        }
        // NetworkParameters
        if (all || canRead(2)) {
            attributes.add(2);
        }
        if (getVersion() > 0) {
            // QualityOfService
            if (all || canRead(3)) {
                attributes.add(3);
            }
        }
        return GXDLMSObjectHelpers.toIntArray(attributes);
    }

    @Override
    public final String[] getNames() {
        return new String[] { "Logical Name", "Network parameters", "Quality of service" };
    }

    @Override
    public final String[] getMethodNames() {
        return new String[0];
    }

    @Override
    public final int getAttributeCount() {
        if (getVersion() == 0) {
            return 2;
        }
        return 3;
    }

    @Override
    public final int getMethodCount() {
        return 0;
    }

    @Override
    public DataType getDataType(int index) {
        switch (index) {
        case 1:
            return DataType.OCTET_STRING;
        case 2:
        case 3:
            return DataType.STRUCTURE;
        default:
            throw new IllegalArgumentException("GetDataType failed. Invalid attribute index.");
        }
    }

    @Override
    public final Object getValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        Object ret = null;
        GXByteBuffer buff = new GXByteBuffer();
        switch (e.getIndex()) {
        case 1:
            ret = GXCommon.logicalNameToBytes(getLogicalName());
            break;
        case 2:
            buff.setUInt8(DataType.STRUCTURE);
            GXCommon.setObjectCount(9, buff);
            GXCommon.setData(settings, buff, DataType.UINT16, getNetworkParameters().getT3402());
            GXCommon.setData(settings, buff, DataType.UINT16, getNetworkParameters().getT3412());
            GXCommon.setData(settings, buff, DataType.UINT32, getNetworkParameters().getT3412ext2());
            GXCommon.setData(settings, buff, DataType.UINT16, getNetworkParameters().getT3324());
            GXCommon.setData(settings, buff, DataType.UINT32, getNetworkParameters().getTeDRX());
            GXCommon.setData(settings, buff, DataType.UINT16, getNetworkParameters().getTPTW());
            GXCommon.setData(settings, buff, DataType.INT8, getNetworkParameters().getQRxlevMin());
            GXCommon.setData(settings, buff, DataType.INT8, getNetworkParameters().getQRxlevMinCE());
            GXCommon.setData(settings, buff, DataType.INT8, getNetworkParameters().getQRxLevMinCE1());
            ret = buff.array();
            break;
        case 3:
            if (getVersion() == 0) {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            } else {
                buff.setUInt8(DataType.STRUCTURE);
                GXCommon.setObjectCount(4, buff);
                GXCommon.setData(settings, buff, DataType.INT8, getQualityOfService().getSignalQuality());
                GXCommon.setData(settings, buff, DataType.INT8, getQualityOfService().getSignalLevel());
                GXCommon.setData(settings, buff, DataType.INT8, getQualityOfService().getSignalToNoiseRatio());
                GXCommon.setData(settings, buff, DataType.ENUM,
                        getQualityOfService().getCoverageEnhancement().getValue());
                ret = buff.array();
            }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
        return ret;
    }

    @Override
    public final void setValue(final GXDLMSSettings settings, final ValueEventArgs e) {
        switch (e.getIndex()) {
        case 1:
            setLogicalName(GXCommon.toLogicalName(e.getValue()));
            break;
        case 2: {
            GXStructure s = (GXStructure) ((e.getValue() instanceof GXStructure) ? e.getValue() : null);
            if (s != null) {
                getNetworkParameters().setT3402(((GXUInt16) s.get(0)).intValue());
                getNetworkParameters().setT3412(((GXUInt16) s.get(1)).intValue());
                getNetworkParameters().setT3412ext2(((GXUInt32) s.get(2)).longValue());
                getNetworkParameters().setT3324(((GXUInt16) s.get(3)).intValue());
                getNetworkParameters().setTeDRX(((GXUInt32) s.get(4)).longValue());
                getNetworkParameters().setTPTW(((GXUInt16) s.get(5)).intValue());
                getNetworkParameters().setQRxlevMin((byte) s.get(6));
                getNetworkParameters().setQRxlevMinCE((byte) s.get(7));
                getNetworkParameters().setQRxLevMinCE1((byte) s.get(8));
            }
        }
            break;
        case 3:
            if (getVersion() == 0) {
                e.setError(ErrorCode.READ_WRITE_DENIED);
            } else {
                GXStructure s = (GXStructure) ((e.getValue() instanceof GXStructure) ? e.getValue() : null);
                if (s != null) {
                    getQualityOfService().setSignalQuality((byte) s.get(0));
                    getQualityOfService().setSignalLevel((byte) s.get(1));
                    getQualityOfService().setSignalToNoiseRatio((byte) s.get(2));
                    getQualityOfService()
                            .setCoverageEnhancement(LteCoverageEnhancement.forValue(((GXEnum) s.get(3)).intValue()));
                }
            }
            break;
        default:
            e.setError(ErrorCode.READ_WRITE_DENIED);
            break;
        }
    }

    @Override
    public final void load(final GXXmlReader reader) throws XMLStreamException {
        networkParameters.setT3402((short) reader.readElementContentAsInt("T3402"));
        networkParameters.setT3412((short) reader.readElementContentAsInt("T3412"));
        networkParameters.setT3412ext2((int) reader.readElementContentAsInt("T3412ext2"));
        networkParameters.setT3324((short) reader.readElementContentAsInt("T3324"));
        networkParameters.setTeDRX((int) reader.readElementContentAsInt("TeDRX"));
        networkParameters.setTPTW((short) reader.readElementContentAsInt("TPTW"));
        networkParameters.setQRxlevMin((byte) reader.readElementContentAsInt("QRxlevMin"));
        networkParameters.setQRxlevMinCE((byte) reader.readElementContentAsInt("QRxlevMinCE"));
        networkParameters.setQRxLevMinCE1((byte) reader.readElementContentAsInt("QRxLevMinCE1"));
        qualityOfService.setSignalQuality((byte) reader.readElementContentAsInt("SignalQuality"));
        qualityOfService.setSignalLevel((byte) reader.readElementContentAsInt("SignalLevel"));
        qualityOfService.setSignalToNoiseRatio((byte) reader.readElementContentAsInt("SignalToNoiseRatio"));
        qualityOfService.setCoverageEnhancement(
                LteCoverageEnhancement.values()[reader.readElementContentAsInt("CoverageEnhancement")]);
    }

    @Override
    public final void save(final GXXmlWriter writer) throws XMLStreamException {
        writer.writeElementString("T3402", getNetworkParameters().getT3402());
        writer.writeElementString("T3412", getNetworkParameters().getT3412());
        writer.writeElementString("T3412ext2", getNetworkParameters().getT3412ext2());
        writer.writeElementString("T3324", getNetworkParameters().getT3324());
        writer.writeElementString("TeDRX", getNetworkParameters().getTeDRX());
        writer.writeElementString("TPTW", getNetworkParameters().getTPTW());
        writer.writeElementString("QRxlevMin", getNetworkParameters().getQRxlevMin());
        writer.writeElementString("QRxlevMinCE", getNetworkParameters().getQRxlevMinCE());
        writer.writeElementString("QRxLevMinCE1", getNetworkParameters().getQRxLevMinCE1());
        writer.writeElementString("SignalQuality", getQualityOfService().getSignalQuality());
        writer.writeElementString("SignalLevel", getQualityOfService().getSignalLevel());
        writer.writeElementString("SignalToNoiseRatio", getQualityOfService().getSignalToNoiseRatio());
        writer.writeElementString("CoverageEnhancement",
                (int) getQualityOfService().getCoverageEnhancement().ordinal());
    }

    @Override
    public void postLoad(final GXXmlReader reader) {
        // Not needed for this object.
    }

}
