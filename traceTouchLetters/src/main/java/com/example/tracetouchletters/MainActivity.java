package com.example.tracetouchletters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.constants.HapticCharCase;
import com.example.tracetouchletters.constants.HapticPlacement;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restoreSettings();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        setStartButton();
    }

    private void setStartButton() {
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ModeSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void restoreSettings() {
        if (!AppCtx.settingsLoaded) {
            SharedPreferences settings = getSharedPreferences(AppCtx.PREFS_NAME, 0);
            AppCtx.hapticStrength = settings.getInt(AppCtx.HAPTICSTRENGTHNAME, 50);
            AppCtx.hapticRoughness = settings.getInt(AppCtx.HAPTICROUGHNESSNAME, 50);
            AppCtx.hapticPlacement = settings.getInt(AppCtx.HAPTICPLACEMENTNAME, HapticPlacement
                    .LETTER);
            AppCtx.charCase = settings.getInt(AppCtx.HAPTICCHARCASENAME, HapticCharCase.UPPER);
            AppCtx.noHaptic = settings.getBoolean(AppCtx.NOHAPTICNAME, false);

            AppCtx.settingsLoaded = true;

            AppCtx.tmpHapticStrength = AppCtx.hapticStrength;
            AppCtx.tmpHapticRoughness = AppCtx.hapticRoughness;
            AppCtx.tmpNoHaptic = AppCtx.noHaptic;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        saveCurrentSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCurrentSettings();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentSettings();
    }

    private void saveCurrentSettings() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(AppCtx.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(AppCtx.HAPTICSTRENGTHNAME, AppCtx.hapticStrength);
        editor.putInt(AppCtx.HAPTICROUGHNESSNAME, AppCtx.hapticRoughness);
        editor.putInt(AppCtx.HAPTICPLACEMENTNAME, AppCtx.hapticPlacement);
        editor.putInt(AppCtx.HAPTICCHARCASENAME, AppCtx.charCase);

        editor.putBoolean(AppCtx.NOHAPTICNAME, AppCtx.noHaptic);
        // Commit the edits!
        editor.commit();
        editor.clear();
    }
}
