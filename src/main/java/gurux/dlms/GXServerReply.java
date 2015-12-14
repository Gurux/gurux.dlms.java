package gurux.dlms;

import java.util.List;

public class GXServerReply {
    /**
     * Server received data.
     */
    private GXByteBuffer data = new GXByteBuffer();

    /**
     * Server reply messages.
     */
    private List<byte[][]> replyMessages;

    /**
     * Reply message index.
     */
    private int index = 0;

    /**
     * @return the index
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param value
     *            The index to set.
     */
    public final void setIndex(final int value) {
        index = value;
    }

    /**
     * @return the data
     */
    public final GXByteBuffer getData() {
        return data;
    }

    /**
     * @param value
     *            The data to set.
     */
    public final void setData(final GXByteBuffer value) {
        data = value;
    }

    /**
     * @return the replyMessages
     */
    public final List<byte[][]> getReplyMessages() {
        return replyMessages;
    }

    /**
     * @param value
     *            the replyMessages to set
     */
    public final void setReplyMessages(final List<byte[][]> value) {
        replyMessages = value;
    }
}
