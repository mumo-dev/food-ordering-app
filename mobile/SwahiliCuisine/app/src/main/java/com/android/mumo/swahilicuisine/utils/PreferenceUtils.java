package com.android.mumo.swahilicuisine.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.mumo.swahilicuisine.model.Order;
import com.google.gson.Gson;

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

    public static void storeUserOrder(Context context, Order order) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(order);
        editor.putString("order", json);
        editor.apply();
    }

    public static Order getUserOrder(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
        String data = sharedPref.getString("order", "");
        Order order = new Gson().fromJson(data, Order.class);
        return order;
    }

}
