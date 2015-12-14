package gurux.dlms.internal;

import gurux.dlms.enums.DataType;

//Mikko poista
public class GXDataInfo {
    private int index;
    private int count;
    private DataType type = DataType.NONE;
    private boolean compleate = true;

    public final int getIndex() {
        return index;
    }

    public final void setIndex(final int value) {
        this.index = value;
    }

    public final int getCount() {
        return count;
    }

    public final void setCount(final int value) {
        this.count = value;
    }

    public final DataType getType() {
        return type;
    }

    public final void setType(final DataType value) {
        this.type = value;
    }

    public final boolean isCompleate() {
        return compleate;
    }

    public final void setCompleate(final boolean value) {
        this.compleate = value;
    }

    public final void clear() {
        index = 0;
        count = 0;
        type = DataType.NONE;
        compleate = true;
    }
}
