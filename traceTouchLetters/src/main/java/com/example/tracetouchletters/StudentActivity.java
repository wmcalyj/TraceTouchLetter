package com.example.tracetouchletters;

import java.util.logging.Logger;

import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.views.MyImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import nxr.tpad.lib.TPad;
import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.views.FrictionMapView;

import static com.example.tracetouchletters.constants.HapticCharCase.LOWER;
import static com.example.tracetouchletters.constants.HapticCharCase.UPPER;

public class StudentActivity extends Activity {
    private static final Logger LOG = Logger.getLogger(StudentActivity.class.getName());
    protected TPad mTpad;
    FrictionMapView mFrictionMapView;
    Bitmap bitmap;
    MyImageView imageView;
    boolean tpadBound = false;
    Bitmap mBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_mode);
        setStudentActivityButtons();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTpad == null || !mTpad.getBound() || !tpadBound) {
            mTpad = new TPadImpl(this);
            tpadBound = true;
            imageView = (MyImageView) findViewById(R.id.studentText);
            imageView.setTpad(mTpad);
            setMyImageView();
        }
    }

    protected void setMyImageView() {
        switch (AppCtx.charCase) {
            case LOWER:
                imageView.changeToLowerCase();
                break;
            case UPPER:
                imageView.changeToUpperCase();
                break;
        }
    }

    private void setStudentActivityButtons() {
        ImageButton next = (ImageButton) findViewById(R.id.studentNextLetter);
        ImageButton prev = (ImageButton) findViewById(R.id.studentPrevLetter);

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.drawNextChar();
            }
        });
        prev.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageView.drawPrevChar();
                                    }
                                }

        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTpad != null && mTpad.getBound() && tpadBound) {
            mTpad.disconnectTPad();
            tpadBound = false;
        }
    }
}
