package com.example.tracetouchletters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.tracetouchletters.constants.AppCtx;

public class SettingsSelectionActivity extends Activity {

    ImageButton save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_setting_selection);
        setModeButtons();
        setSaveCancelButtons();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private void setModeButtons() {
        Button strengthAndType = (Button) findViewById(R.id.teacherHapticStrengthType);
        strengthAndType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SettingsSelectionActivity.this, HapticStrengthTypeActivity
                        .class);
                startActivity(intent);
            }
        });
        Button placement = (Button) findViewById(R.id.teacherHapticPlacement);
        placement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SettingsSelectionActivity.this, HapticPlacementActivity.class);
                startActivity(intent);
            }
        });
        Button drawDirection = (Button) findViewById(R.id.teacherDrawDirection);
        drawDirection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsSelectionActivity.this,
                        TeacherDrawDirectionActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void setSaveCancelButtons() {
        if (save == null) {
            save = (ImageButton) findViewById(R.id.settingsSave);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveAllSettingChanges();
                    Intent intent = new Intent(SettingsSelectionActivity.this,
                            ModeSelectionActivity.class);
                    startActivity(intent);

                }
            });
        }
        if (cancel == null) {
            cancel = (ImageButton) findViewById(R.id.settingsCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelAllSettingChanges();
                    Intent intent = new Intent(SettingsSelectionActivity.this,
                            ModeSelectionActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void saveAllSettingChanges() {

        AppCtx.hapticStrength = AppCtx.tmpHapticStrength;
        AppCtx.hapticRoughness = AppCtx.tmpHapticRoughness;
        AppCtx.hapticPlacement = AppCtx.tmpHapticPlacement;
        AppCtx.charCase = AppCtx.tmpCharCase;
        AppCtx.noHaptic = AppCtx.tmpNoHaptic;

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

    protected void cancelAllSettingChanges() {
        AppCtx.tmpHapticStrength = AppCtx.hapticStrength;
        AppCtx.tmpHapticRoughness = AppCtx.hapticRoughness;
        AppCtx.tmpHapticPlacement = AppCtx.hapticPlacement;
        AppCtx.tmpCharCase = AppCtx.charCase;
        AppCtx.tmpNoHaptic = AppCtx.noHaptic;
    }
}
