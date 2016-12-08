package com.example.tracetouchletters.letterdirection;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengchaowang on 11/28/16.
 */

public class LetterDirection implements Serializable {
    private static final String TAG = "LetterDirection";
    public String letter;
    public List<Direction> directions;
    Direction currentDirection;

    public LetterDirection(String letter) {
        this.letter = letter;
        directions = new ArrayList<Direction>();
    }

    public void startRecording(float x, float y) {
        if (directions == null) {
            directions = new ArrayList<Direction>();
        }
        currentDirection = new Direction(x, y);
    }

    public void recordingDirection(float x, float y) {
        if (currentDirection == null) {
            Log.e(TAG, "current direction is null, cannot record direction");
        } else {
            currentDirection.recordingDirection(x, y);
        }
    }

    public void finishRecordingDirection(float x, float y) {
        if (currentDirection == null) {
            Log.e(TAG, "current direction is null, cannot finish recording direction");
        } else {
            currentDirection.recordingDirection(x, y);
            directions.add(currentDirection);
            currentDirection = null;
        }
    }

    public void deletePreviousDirection() {
        if (directions == null || directions.isEmpty()) {
            Log.i(TAG, "there is no previous direction, cannot delete");
        }
        directions.remove(directions.size() - 1);
    }
}
