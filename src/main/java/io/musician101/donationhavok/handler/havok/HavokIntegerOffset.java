package io.musician101.donationhavok.handler.havok;

abstract class HavokIntegerOffset extends HavokOffset<Integer> {

    HavokIntegerOffset(int delay, Integer xOffset, Integer yOffset, Integer zOffset) {
        super(delay, xOffset, yOffset, zOffset);
    }
}
