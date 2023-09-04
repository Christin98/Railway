package com.kha.train.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SharedPreference {
    public static final String PREFS_NAME = "app_pref";
    public static final String TIME_format = "time_format";
    private static Context context;
    Editor editor;

    public SharedPreference(Context context) {
        this.context = context;
        this.editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
    }

    public boolean getTimeFormate() {
        return context.getSharedPreferences(PREFS_NAME, 0).getBoolean(TIME_format, true);
    }

    public void setTimeFormate(boolean z) {
        this.editor.putBoolean(TIME_format, z);
        this.editor.apply();
    }
}
