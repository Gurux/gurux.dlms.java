package gurux.dlms.secure;

import gurux.dlms.enums.Command;
import gurux.dlms.enums.Security;

public class AesGcmParameter {
    private Command command;
    private Security security;
    private long frameCounter;

    private byte[] systemTitle;
    private byte[] blockCipherKey;
    private byte[] authenticationKey;
    private byte[] plainText;
    private CountType type;
    private byte[] countTag;

    AesGcmParameter(final Command forCommand, final Security forSecurity,
            final long forFrameCounter, final byte[] forSystemTitle,
            final byte[] forBlockCipherKey, final byte[] forAuthenticationKey,
            final byte[] forPlainText) {
        command = forCommand;
        security = forSecurity;
        frameCounter = forFrameCounter;

        systemTitle = forSystemTitle;
        blockCipherKey = forBlockCipherKey;
        authenticationKey = forAuthenticationKey;
        plainText = forPlainText;
        type = CountType.PACKET;
    }

    public final Command getCommand() {
        return command;
    }

    public final void setCommand(final Command value) {
        command = value;
    }

    public final Security getSecurity() {
        return security;
    }

    public final void setSecurity(final Security value) {
        security = value;
    }

    public final long getFrameCounter() {
        return frameCounter;
    }

    public final void setFrameCounter(final long value) {
        frameCounter = value;
    }

    public final byte[] getSystemTitle() {
        return systemTitle;
    }

    public final void setSystemTitle(final byte[] value) {
        systemTitle = value;
    }

    public final byte[] getBlockCipherKey() {
        return blockCipherKey;
    }

    public final void setBlockCipherKey(final byte[] value) {
        blockCipherKey = value;
    }

    public final byte[] getAuthenticationKey() {
        return authenticationKey;
    }

    public final void setAuthenticationKey(final byte[] value) {
        authenticationKey = value;
    }

    public final byte[] getPlainText() {
        return plainText;
    }

    public final void setPlainText(final byte[] value) {
        plainText = value;
    }

    public final CountType getType() {
        return type;
    }

    public final void setType(final CountType value) {
        type = value;
    }

    public final byte[] getCountTag() {
        return countTag;
    }

    public final void setCountTag(final byte[] value) {
        countTag = value;
    }
}
