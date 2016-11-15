package com.example.tracetouchletters.listeners;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tracetouchletters.StudentActivity;

import java.util.logging.Logger;

import nxr.tpad.lib.TPad;
import nxr.tpad.lib.TPadImpl;

/**
 * Created by mengchaowang on 11/12/16.
 */

public class MyImageViewTouchListener implements View.OnTouchListener {
    private static final Logger LOG = Logger.getLogger(MyImageViewTouchListener.class.getName());

    TPad mTpad;
    Bitmap mBmp;
    int colorToCompare;


    public MyImageViewTouchListener(TPad mTpad, int colorToCompare) {
        this.mTpad = mTpad;
        this.colorToCompare = colorToCompare;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float xPos = motionEvent.getX();
        float yPos = motionEvent.getY();
        if (mBmp == null) {
            mBmp = ((BitmapDrawable) ((ImageView) view).getDrawable()).getBitmap();
            LOG.info("mBmp instantiated");
        }

        int pixelColor = mBmp.getPixel((int) xPos, (int) yPos);
        if (sameColor(pixelColor)) {
            LOG.info("Same color at pos:" + xPos + ", " + yPos);
        } else {
            LOG.info("Different color (" + pixelColor + ") at pos: " + xPos + ", " + yPos);
        }
        return true;
    }

    public boolean sameColor(int pixelColor) {
        return Color.rgb(Color.red(colorToCompare), Color.green(colorToCompare), Color.blue
                (colorToCompare)) == Color.rgb(Color.red(pixelColor), Color.green(pixelColor),
                Color.blue(pixelColor));
    }
}
