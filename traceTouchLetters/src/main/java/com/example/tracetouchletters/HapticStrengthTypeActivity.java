package com.example.tracetouchletters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.views.MyImageView;

import nxr.tpad.lib.TPad;
import nxr.tpad.lib.TPadImpl;

public class HapticStrengthTypeActivity extends Activity {
    MyImageView myPreview;
    TPad mTpad;
    boolean isTPadConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_mode_haptic_type_strength);
        setButtons();
    }


    private void setButtons() {
        Button save = (Button) findViewById(R.id.hapticTypeAndStrengthOK);
        Button cancel = (Button) findViewById(R.id.hapticTypeAndStrengthCancel);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveHapticTypeAndStrengthChanges(true);
                Intent intent = new Intent(HapticStrengthTypeActivity.this,
                        SettingsSelectionActivity
                                .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAppCtxTmpValues();
                Intent intent = new Intent(HapticStrengthTypeActivity.this,
                        SettingsSelectionActivity
                                .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTpad == null || !isTPadConnected) {
            mTpad = new TPadImpl(this);
            myPreview = (MyImageView) findViewById(R.id.hapticTypeAndStrengthPreview);
            myPreview.setTpad(mTpad);
        }
        presetSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTpad != null && isTPadConnected) {
            mTpad.disconnectTPad();
            if (myPreview != null)
                myPreview.disconnectTPad();
        }
    }

    private void presetSettings() {
        SeekBar hapticStrength = (SeekBar) findViewById(R.id.hapticStrengthBar);
        SeekBar hapticRoughness = (SeekBar) findViewById(R.id.hapticRoughnessBar);
        hapticStrength.setProgress(AppCtx.hapticStrength);
        hapticRoughness.setProgress(AppCtx.hapticRoughness);
        hapticStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Do nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AppCtx.tmpHapticStrength = seekBar.getProgress();
            }
        });
        hapticRoughness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Do nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AppCtx.tmpHapticRoughness = seekBar.getProgress();
            }
        });
    }

    protected void resetAppCtxTmpValues() {
        AppCtx.tmpHapticStrength = AppCtx.hapticStrength;
        AppCtx.tmpHapticRoughness = AppCtx.hapticRoughness;
    }


    private void saveHapticTypeAndStrengthChanges(boolean tmpSave) {
        SeekBar hapticStrength = (SeekBar) findViewById(R.id.hapticStrengthBar);
        SeekBar hapticRoughness = (SeekBar) findViewById(R.id.hapticRoughnessBar);
        AppCtx.tmpHapticStrength = hapticStrength.getProgress();
        AppCtx.tmpHapticRoughness = hapticRoughness.getProgress();
        if (!tmpSave) {
            realSaveChanges();
        }
    }

    protected void realSaveChanges() {
        AppCtx.hapticStrength = AppCtx.tmpHapticStrength;
        AppCtx.hapticRoughness = AppCtx.tmpHapticRoughness;

        SharedPreferences settings = getSharedPreferences(AppCtx.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(AppCtx.HAPTICSTRENGTHNAME, AppCtx.hapticStrength);
        editor.putInt(AppCtx.HAPTICROUGHNESSNAME, AppCtx.hapticRoughness);
        // Commit the edits!
        editor.commit();
    }
}
