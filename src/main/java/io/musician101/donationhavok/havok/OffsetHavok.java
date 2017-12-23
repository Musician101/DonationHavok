package io.musician101.donationhavok.havok;

public abstract class OffsetHavok<N extends Number> extends Havok {

    private final N xOffset;
    private final N yOffset;
    private final N zOffset;

    public OffsetHavok(int delay, N xOffset, N yOffset, N zOffset) {
        super(delay);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public N getXOffset() {
        return xOffset;
    }

    public N getYOffset() {
        return yOffset;
    }

    public N getZOffset() {
        return zOffset;
    }
}
