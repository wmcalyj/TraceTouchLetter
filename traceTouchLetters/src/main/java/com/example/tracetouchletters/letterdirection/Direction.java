package com.example.tracetouchletters.letterdirection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengchaowang on 11/28/16.
 */

public class Direction implements Serializable {
    public List<Point> oneDirection;

    Direction() {
        oneDirection = new ArrayList<Point>();
    }

    Direction(float x, float y) {
        oneDirection = new ArrayList<Point>();
        recordingDirection(x, y);
    }

    void recordingDirection(float x, float y) {
        if (oneDirection == null) {
            oneDirection = new ArrayList<Point>();
        }
        oneDirection.add(new Point(x, y));
    }
}
