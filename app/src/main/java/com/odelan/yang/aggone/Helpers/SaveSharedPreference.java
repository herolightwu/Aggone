package com.odelan.yang.aggone.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSharedPreference {
    //SharedPreferences sharedPreferences;

    public static final String PREFS_NAME = "Aggone";
    public static final String KEY_USER_TOKEN = "key_token";


    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String status = sharedPreferences.getString(key, null);
        return status;
    }

    public static void putString(Context context, String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void clear(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
