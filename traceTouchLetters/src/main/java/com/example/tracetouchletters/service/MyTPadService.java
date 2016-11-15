package com.example.tracetouchletters.service;

import android.util.Log;

import com.example.tracetouchletters.constants.AppCtx;

import java.util.Random;

/**
 * Created by mengchaowang on 11/14/16.
 */

public class MyTPadService {

    private static final String TAG = "MyTPadService";

    public float[] calculateFrictionBuffer(boolean isPreview) {

        float base = isPreview ? (float) (AppCtx.tmpHapticStrength / 100.00) : (float) (AppCtx
                .hapticStrength / 100.00);
        float random = isPreview ? (float) (AppCtx.tmpHapticRoughness / 100.00) : (float) (AppCtx
                .hapticRoughness / 100.00);
        return calculateFrictionBuffer(base, random);
    }

    public float[] calculateFrictionBuffer(float base, float noise) {
        Log.v(TAG, "Base: " + base + ", noise: " + noise);
        float[] frictionBuffer = new float[(int) (nxr.tpad.lib.TPadService
                .OUTPUT_SAMPLE_RATE * (.020f))];// 125 samples, 20ms @ sample rate output
        Random ran = new Random(System.currentTimeMillis());
        float value;
        for (int i = 0; i < frictionBuffer.length; i++) {
            value = ran.nextFloat() * 2 * noise - 1 * noise + base;
            while (value >= 1 || value <= 0) {
                value = ran.nextFloat() * 2 * noise - 1 * noise + base;
            }
            Log.v(TAG, "FrictionBuffer value: " + value);
            frictionBuffer[i] = value;
        }
        return frictionBuffer;
    }
}
