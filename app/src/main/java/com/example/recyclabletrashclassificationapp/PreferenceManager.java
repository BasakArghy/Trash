package com.example.recyclabletrashclassificationapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static final String PREF_NAME = "settings_prefs";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isDarkModeEnabled(Context context) {
        return getPrefs(context).getBoolean("dark_theme", false);
    }
}
