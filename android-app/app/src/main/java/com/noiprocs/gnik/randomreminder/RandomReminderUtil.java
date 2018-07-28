package com.noiprocs.gnik.randomreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RandomReminderUtil {
    private static SharedPreferences PREFERENCE;
    private static SharedPreferences.Editor EDITOR;

    public static void initializedPreference(Context context) {
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

    public static int getInt(String key) {
        return RandomReminderUtil.PREFERENCE.getInt(key, 0);
    }

    public static void setInt(String key, int value) {
        EDITOR.putInt(key, value);
        EDITOR.commit();
    }
}
