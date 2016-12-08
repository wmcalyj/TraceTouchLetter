package com.example.tracetouchletters.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.constants.HapticType;

import java.nio.FloatBuffer;
import java.util.Random;

import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.consts.TPadVibration;

/**
 * Created by mengchaowang on 11/14/16.
 */

public class MyTPadService extends TPadImpl {

    public final static long OUTPUT_SAMPLE_RATE = 6250; // 6.250kHz output rate
    public final static int BUFFER_SIZE = 6250; // enough buffer for a 1 hz
    private static final String TAG = "MyTPadService";
    private static FloatBuffer buffer = FloatBuffer.allocate(BUFFER_SIZE);

    public MyTPadService(Context context) {
        super(context);
    }

    public float[] calculateFrictionBuffer(boolean isPreview) {

        float amp = isPreview ? (float) (AppCtx.tmpHapticStrength / 100.00) : (float) (AppCtx
                .hapticStrength / 100.00);
        float freq = isPreview ? (float) (AppCtx.tmpHapticRoughness / 100.00) * 230 + 20 :
                (float) (AppCtx
                        .hapticRoughness / 100.00) * 230 + 20;
        return getSinusoidFrictionBuffer(freq, amp);
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

    public float[] getSinusoidFrictionBuffer(float freq, float amp) {
        return getFrictionBuffer(HapticType.SINUSOID, freq, amp);
    }

    public float[] getFrictionBuffer(int type, float freq, float amp) {
        int periodSamps = (int) ((1 / freq) * OUTPUT_SAMPLE_RATE);

        synchronized (buffer) {
            buffer.clear();
            buffer.limit(periodSamps);

            float tp = 0;

            switch (type) {

                case HapticType.SINUSOID:

                    for (float i = 0; i < periodSamps; i++) {

                        tp = (float) ((1 + Math.sin(2 * Math.PI * freq * i / OUTPUT_SAMPLE_RATE))
                                / 2f);

                        buffer.put(amp * tp);

                    }

                    break;
                case HapticType.SAWTOOTH:
                    for (float i = 0; i < periodSamps; i++) {

                        buffer.put(amp * (i / periodSamps));

                    }
                    break;

                case HapticType.TRIANGLE:
                    for (float i = 0; i < periodSamps / 2; i++) {

                        buffer.put(amp * tp++ * 2 / periodSamps);

                    }
                    for (float i = periodSamps / 2; i < periodSamps; i++) {

                        buffer.put(amp * tp-- * 2 / periodSamps);

                    }

                    break;
                default:
                    break;
            }

            buffer.flip();

        }
        return buffer.array();
    }
}
