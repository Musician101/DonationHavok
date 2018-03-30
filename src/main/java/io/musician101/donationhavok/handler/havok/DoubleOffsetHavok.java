package io.musician101.donationhavok.handler.havok;

abstract class DoubleOffsetHavok extends OffsetHavok<Double> {

    DoubleOffsetHavok(int delay, Double xOffset, Double yOffset, Double zOffset) {
        super(delay, xOffset, yOffset, zOffset);
    }
}
