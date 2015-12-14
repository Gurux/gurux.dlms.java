package gurux.dlms.objects;

import java.util.List;

final class GXDLMSObjectHelpers {

    /**
     * Constructor.
     */
    private GXDLMSObjectHelpers() {

    }

    public static int[] toIntArray(final List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = -1;
        for (Integer e : list) {
            ret[++i] = e.intValue();
        }
        return ret;
    }

    public static long[] toLongArray(final List<Long> list) {
        long[] ret = new long[list.size()];
        int i = -1;
        for (Long e : list) {
            ret[++i] = e.longValue();
        }
        return ret;
    }
}
