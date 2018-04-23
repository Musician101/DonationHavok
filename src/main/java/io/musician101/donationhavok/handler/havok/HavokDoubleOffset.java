package io.musician101.donationhavok.handler.havok;

abstract class HavokDoubleOffset extends HavokOffset<Double> {

    HavokDoubleOffset(int delay, Double xOffset, Double yOffset, Double zOffset) {
        super(delay, xOffset, yOffset, zOffset);
    }
}
