package gurux.dlms.objects;

import gurux.dlms.objects.enums.LteCoverageEnhancement;

/**
 * Quality of service of the LTE network.
 */
public class GXLteQualityOfService {
    /**
     * Signal quality.
     */
    private byte signalQuality;
    /**
     * Signal level.
     */
    private byte signalLevel;
    /**
     * Signal to noise ratio.
     */
    private byte signalToNoiseRatio;
    /**
     * Coverage enhancement.
     */
    private LteCoverageEnhancement coverageEnhancement = LteCoverageEnhancement.LEVEL0;

    /**
     * @return Signal quality.
     */
    public final byte getSignalQuality() {
        return signalQuality;
    }

    /**
     * @param value
     *            Signal quality.
     */
    public final void setSignalQuality(final byte value) {
        signalQuality = value;
    }

    /**
     * @return Signal level.
     */
    public final byte getSignalLevel() {
        return signalLevel;
    }

    /**
     * @param value
     *            Signal level.
     */
    public final void setSignalLevel(final byte value) {
        signalLevel = value;
    }

    /**
     * @return Signal to noise ratio.
     */
    public final byte getSignalToNoiseRatio() {
        return signalToNoiseRatio;
    }

    /**
     * @param value
     *            Signal to noise ratio.
     */
    public final void setSignalToNoiseRatio(final byte value) {
        signalToNoiseRatio = value;
    }

    /**
     * @return Coverage enhancement.
     */
    public final LteCoverageEnhancement getCoverageEnhancement() {
        return coverageEnhancement;
    }

    /**
     * @param value
     *            Coverage enhancement.
     */
    public final void setCoverageEnhancement(LteCoverageEnhancement value) {
        coverageEnhancement = value;
    }
}