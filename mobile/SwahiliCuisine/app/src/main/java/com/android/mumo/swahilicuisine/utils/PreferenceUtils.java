package com.android.mumo.swahilicuisine.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static final String PREFERENCE_KEY_FILE = "com.android.mumo.swahilicuisine.utils.PREFERENCE_KEY_FILE";


    public static void storeLocationName(Context context, String name) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("location_name", name);
        editor.apply();
    }

    public static void storeLocationId(Context context, int id) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("location_id", id);
        editor.apply();
    }

    public static void storeLocationAreaName(Context context, String name) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("location_area_name", name);
        editor.apply();
    }

    public static void storeLocationAreaId(Context context, int id) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("location_area_id", id);
        editor.apply();
    }

    public static String getLocationName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString("location_name", null);
    }

    public static int getLocationId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getInt("location_id", 0);
    }

    public static String getLocationAreaName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString("location_area_name", null);
    }

    public static int getLocationAreaId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getInt("location_area_id", 0);
    }

}
