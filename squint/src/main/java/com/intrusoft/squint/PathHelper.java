package com.intrusoft.squint;

import android.graphics.Path;

import com.intrusoft.squint.Squint.Direction;
import com.intrusoft.squint.Squint.Gravity;

import static com.intrusoft.squint.Squint.Gravity.BOTTOM;
import static com.intrusoft.squint.Squint.Gravity.LEFT;
import static com.intrusoft.squint.Squint.Gravity.RIGHT;
import static com.intrusoft.squint.Squint.Gravity.TOP;

final class PathHelper {
    private PathHelper() {
        // no instances
    }

    static void calculatePath(Path path, float width, float height, double angle, Direction direction, Gravity gravity) {
        path.reset();
        switch (direction) {
            case LEFT_TO_RIGHT:
                leftToRight(path, width, height, angle, gravity);
            case RIGHT_TO_LEFT:
                rightToLeft(path, width, height, angle, gravity);
            case TOP_TO_BOTTOM:
                topToBottom(path, width, height, angle, gravity);
            case BOTTOM_TO_TOP:
                bottomToTop(path, width, height, angle, gravity);
            default:
                throw new IllegalArgumentException("Unexpected direction: " + direction);
        }
    }


    private static void leftToRight(Path path, float width, float height, double angle, Gravity gravity) {
        if (gravity == LEFT || gravity == RIGHT)
            gravity = BOTTOM;
        float attitude = (float) Math.abs(Math.tan(Math.toRadians(angle)) * width);
        if (gravity == BOTTOM) {
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height - attitude);
            path.lineTo(0, height);
            path.close();
        } else {
            path.moveTo(0, 0);
            path.lineTo(width, attitude);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.close();
        }
    }

    private static void rightToLeft(Path path, float width, float height, double angle, Gravity gravity) {
        if (gravity == LEFT || gravity == RIGHT)
            gravity = BOTTOM;
        float attitude = (float) Math.abs(Math.tan(Math.toRadians(angle)) * width);
        if (gravity == BOTTOM) {
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height - attitude);
            path.close();
        } else {
            path.moveTo(0, attitude);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.close();
        }
    }

    private static void topToBottom(Path path, float width, float height, double angle, Gravity gravity) {
        if (gravity == TOP || gravity == BOTTOM)
            gravity = LEFT;
        float attitude = (float) Math.abs(Math.tan(Math.toRadians(angle)) * height);
        if (gravity == LEFT) {
            path.moveTo(0, 0);
            path.lineTo(0, height);
            path.lineTo(width - attitude, height);
            path.lineTo(width, 0);
            path.close();
        } else {
            path.moveTo(0, 0);
            path.lineTo(attitude, height);
            path.lineTo(width, height);
            path.lineTo(width, 0);
            path.close();
        }
    }

    private static void bottomToTop(Path path, float width, float height, double angle, Gravity gravity) {
        if (gravity == TOP || gravity == BOTTOM)
            gravity = LEFT;

        float attitude = (float) Math.abs(Math.tan(Math.toRadians(angle)) * height);
        if (gravity == LEFT) {
            path.moveTo(0, 0);
            path.lineTo(0, height);
            path.lineTo(width, height);
            path.lineTo(width - attitude, 0);
            path.close();
        } else {
            path.moveTo(attitude, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.close();
        }
    }
}
