package com.intrusoft.squint;

public class Squint {

   public enum Gravity {
        LEFT(0),
        RIGHT(1),
        TOP(2),
        BOTTOM(3);
        final int value;

        Gravity(int value) {
            this.value = value;
        }
    }

    static final Gravity[] GRAVITIES = {Gravity.LEFT, Gravity.RIGHT, Gravity.TOP, Gravity.BOTTOM};

    public enum Direction {
        LEFT_TO_RIGHT(0),
        RIGHT_TO_LEFT(1),
        TOP_TO_BOTTOM(2),
        BOTTOM_TO_TOP(3);
        final int value;

        Direction(int value) {
            this.value = value;
        }
    }

    static final Direction[] DIRECTIONS = {Direction.LEFT_TO_RIGHT, Direction.RIGHT_TO_LEFT, Direction.TOP_TO_BOTTOM, Direction.BOTTOM_TO_TOP};
}