package com.noiprocs.gnik.randomreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RandomReminderUtil {
    private static SharedPreferences PREFERENCE;
    private static SharedPreferences.Editor EDITOR;

    public static void initializedPerference(Context context) {
        RandomReminderUtil.PREFERENCE = PreferenceManager.getDefaultSharedPreferences(context);
        EDITOR = PREFERENCE.edit();
    }

    public static boolean getBoolean(String key) {
        return RandomReminderUtil.PREFERENCE.getBoolean(key, false);
    }

    public static void setBoolean(String key, boolean value) {
        EDITOR.putBoolean(key, value);
        EDITOR.commit();
    }
}
