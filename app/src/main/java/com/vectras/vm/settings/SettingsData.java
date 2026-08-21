package com.vectras.vm.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsData {
    // System

    public static boolean alwaysShowLog(Context context) {
        return get(context, "alwaysShowLog");
    }

    public static void alwaysShowLog(Context context, boolean value) {
        set(context, "alwaysShowLog", value);
    }

    // X11

    public static boolean opengl(Context context) {
        return get(context, "opengl");
    }

    public static void opengl(Context context, boolean value) {
        set(context, "opengl", value);
    }

    public static void x11Bubble(Context context, boolean value) {
        set(context, "x11_bubble", value);
    }

    public static boolean x11Bubble(Context context) {
        return get(context, "x11_bubble");
    }

    // Core

    public static boolean get(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public static void set(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }
}
