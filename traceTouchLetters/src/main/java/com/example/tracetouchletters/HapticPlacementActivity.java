package com.example.tracetouchletters;

import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.constants.HapticCharCase;
import com.example.tracetouchletters.constants.HapticPlacement;
import com.example.tracetouchletters.views.MyImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import nxr.tpad.lib.TPad;
import nxr.tpad.lib.TPadImpl;

public class HapticPlacementActivity extends Activity {
    private static final String TAG = "HapticPlacementActivity";
    TPad mTpad;
    boolean isTpadConnected = false;
    MyImageView mFrictionLetter, mFrictionBackground, mFrictionDirection;
    RadioButton background, letter, direction, upper, lower;
    Button save, cancel;
    CheckBox noHaptic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_mode_haptic_placement);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mTpad == null || !isTpadConnected) {
            mTpad = new TPadImpl(this);
            isTpadConnected = true;
        }
        if (mFrictionLetter == null) {
            mFrictionLetter = (MyImageView) findViewById(R.id.hapticLetter);
            mFrictionLetter.setTpad(mTpad);
        }
        if (mFrictionBackground == null) {
            mFrictionBackground = (MyImageView) findViewById(R.id.hapticBackground);
            mFrictionBackground.setTpad(mTpad);
        }
        if (mFrictionDirection == null) {
            mFrictionDirection = (MyImageView) findViewById(R.id.hapticDirection);
            mFrictionDirection.setTpad(mTpad);
        }

        hapticPlacementSelectionButtonsInit();
        noHapticCheckboxInit();
        reloadAll();
        setSaveCancelButtons();

    }

    public void hapticPlacementSelectionButtonsInit() {
        if (background == null) {
            background = (RadioButton) findViewById(R.id.hapticBackgroundRadioButton);
        }
        if (letter == null) {
            letter = (RadioButton) findViewById(R.id.hapticLetterRadioButton);
        }
        if (direction == null) {
            direction = (RadioButton) findViewById(R.id.hapticDirectionRadioButton);
        }
        if (upper == null) {
            upper = (RadioButton) findViewById(R.id.upper);
            if (upper.isChecked()) {
                mFrictionBackground.changeToUpperCase();
                mFrictionDirection.changeToUpperCase();
                mFrictionLetter.changeToUpperCase();
            }
        }
        if (lower == null) {
            lower = (RadioButton) findViewById(R.id.lower);
            if (lower.isChecked()) {
                mFrictionBackground.changeToLowerCase();
                mFrictionDirection.changeToLowerCase();
                mFrictionLetter.changeToLowerCase();
            }
        }

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setChecked(true);
                letter.setChecked(false);
                direction.setChecked(false);
            }
        });
        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setChecked(false);
                letter.setChecked(true);
                direction.setChecked(false);
            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setChecked(false);
                letter.setChecked(false);
                direction.setChecked(true);
            }
        });
        upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lower.setChecked(false);
                upper.setChecked(true);
                mFrictionBackground.changeToUpperCase();
                mFrictionDirection.changeToUpperCase();
                mFrictionLetter.changeToUpperCase();

            }
        });
        lower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lower.setChecked(true);
                upper.setChecked(false);
                mFrictionBackground.changeToLowerCase();
                mFrictionDirection.changeToLowerCase();
                mFrictionLetter.changeToLowerCase();

            }
        });
    }


    protected void noHapticCheckboxInit() {
        noHaptic = (CheckBox) findViewById(R.id.noHaptic);
        noHaptic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    AppCtx.tmpNoHaptic = true;
                } else {
                    AppCtx.tmpNoHaptic = false;
                }
            }
        });

    }

    protected void reloadAll() {
        reloadHapticPlacement();
        reloadHapticCase();
    }

    protected void reloadHapticPlacement() {
        if (AppCtx.hapticPlacement == HapticPlacement.BACKGROUND) {
            background.setChecked(true);
            letter.setChecked(false);
            direction.setChecked(false);
        } else if (AppCtx.hapticPlacement == HapticPlacement.LETTER) {
            background.setChecked(false);
            letter.setChecked(true);
            direction.setChecked(false);
        } else if (AppCtx.hapticPlacement == HapticPlacement.DIRECTION) {
            background.setChecked(false);
            letter.setChecked(false);
            direction.setChecked(true);
        } else {
            Log.e(TAG, "Unknown haptic placement type: " + AppCtx.hapticPlacement + ", set to " +
                    "letter by default.");
            background.setChecked(false);
            letter.setChecked(true);
            direction.setChecked(false);
            AppCtx.hapticPlacement = HapticPlacement.LETTER;
        }

        noHaptic.setChecked(AppCtx.noHaptic);
    }

    protected void reloadHapticCase() {
        if (AppCtx.charCase == HapticCharCase.UPPER) {
            lower.setChecked(false);
            upper.setChecked(true);
        } else if (AppCtx.charCase == HapticCharCase.LOWER) {
            lower.setChecked(true);
            upper.setChecked(false);
        } else {
            Log.e(TAG, "Unkown haptic char case: " + AppCtx.charCase + ", set it to upper case by" +
                    " default.");
            lower.setChecked(false);
            upper.setChecked(true);
            AppCtx.charCase = HapticCharCase.UPPER;
        }
    }

    protected void setSaveCancelButtons() {
        if (save == null) {
            save = (Button) findViewById(R.id.hapticPlacementOK);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveAll(true);
                    Intent intent = new Intent(HapticPlacementActivity.this,
                            SettingsSelectionActivity.class);
                    startActivity(intent);

                }
            });
        }
        if (cancel == null) {
            cancel = (Button) findViewById(R.id.hapticPlacementCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelSettingsChange();
                    Intent intent = new Intent(HapticPlacementActivity.this,
                            SettingsSelectionActivity.class);
                    startActivity(intent);
                }
            });
        }


    }

    protected void saveAll(boolean tmpSave) {
        saveHapticPlacement();
        saveCase();
        if (!tmpSave) {
            saveAllChanges();
        }
    }

    protected void saveAllChanges() {
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

    protected void cancelSettingsChange() {
        AppCtx.tmpHapticStrength = AppCtx.hapticStrength;
        AppCtx.tmpHapticRoughness = AppCtx.hapticRoughness;
        AppCtx.tmpHapticPlacement = AppCtx.hapticPlacement;
        AppCtx.tmpCharCase = AppCtx.charCase;
        AppCtx.tmpNoHaptic = AppCtx.noHaptic;
    }

    protected void saveHapticPlacement() {
        if (letter != null && letter.isChecked()) {
            AppCtx.tmpHapticPlacement = HapticPlacement.LETTER;

        } else if (background != null && background.isChecked()) {
            AppCtx.tmpHapticPlacement = HapticPlacement.BACKGROUND;

        } else if (direction != null && direction.isChecked()) {
            AppCtx.tmpHapticPlacement = HapticPlacement.DIRECTION;
        } else {
            Log.e(TAG, "No Haptic Placement Selected, set to Letter");
            AppCtx.tmpHapticPlacement = HapticPlacement.LETTER;
        }

        if (noHaptic != null && noHaptic.isChecked()) {
            AppCtx.tmpNoHaptic = true;
        } else {
            AppCtx.tmpNoHaptic = false;
        }
    }


    protected void saveCase() {
        if (upper != null && upper.isChecked()) {
            AppCtx.tmpCharCase = HapticCharCase.UPPER;
        } else if (lower != null && lower.isChecked()) {
            AppCtx.tmpCharCase = HapticCharCase.LOWER;
        } else {
            Log.e(TAG, "No Case Selected, set to upper case");
            AppCtx.tmpCharCase = HapticCharCase.UPPER;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTpad != null && isTpadConnected) {
            mTpad.disconnectTPad();
            isTpadConnected = false;
        }
        if (mFrictionLetter != null) {
            mFrictionLetter.disconnectTPad();
        }
        if (mFrictionBackground != null) {
            mFrictionBackground.disconnectTPad();
        }
        if (mFrictionDirection != null) {
            mFrictionDirection.disconnectTPad();
        }
    }
}
