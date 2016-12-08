package com.example.tracetouchletters.letterdirection;

import java.io.Serializable;

/**
 * Created by mengchaowang on 11/28/16.
 */

public class Point implements Serializable {
    public float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

