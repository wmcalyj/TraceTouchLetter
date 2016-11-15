package com.example.tracetouchletters.constants;


import android.content.SharedPreferences;

public class AppCtx {
    public static final String HAPTICPLACEMENTNAME = "HAPTICPLACEMENTNAME";
    public static final String PREFS_NAME = "TraceTouchLettersPrefsFile";
    public static final String HAPTICSTRENGTHNAME = "HAPTICSTRENGTH";
    public static final String HAPTICROUGHNESSNAME = "HAPTICROUGHNESS";
    public static final String HAPTICCHARCASENAME = "HAPTICCHARCASENAME";
    public static final String NOHAPTICNAME = "NOHAPTICNAME";
    public static boolean settingsLoaded;

    public static boolean noHaptic;
    public static boolean tmpNoHaptic;
    
    public static int hapticStrength;
    public static int tmpHapticStrength;

    public static int hapticRoughness;
    public static int tmpHapticRoughness;

    public static int charCase;
    public static int tmpCharCase;

    public static int hapticPlacement;
    public static int tmpHapticPlacement;

    private AppCtx() {
        // Do nothing;
    }
}
