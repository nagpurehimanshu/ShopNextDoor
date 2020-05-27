package com.example.shopnextdoor.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ManageSharedPreferences {

    public static void saveUsername(Context context, String username){
        SharedPreferences preferences = context.getSharedPreferences("Login", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public static String getUsername(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Login", context.MODE_PRIVATE);
        return preferences.getString("username", "");
    }

    public static void saveName(Context context, String name){
        SharedPreferences preferences = context.getSharedPreferences("Login", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.apply();
    }

    public static String getName(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Login", context.MODE_PRIVATE);
        return preferences.getString("name", "");
    }

    //type: 0 for customer, 1 for shop
    public static void saveType(Context context, boolean type){
        SharedPreferences preferences = context.getSharedPreferences("Login", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("type", type);
        editor.apply();
    }

    public static Boolean getType(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Login", context.MODE_PRIVATE);
        return preferences.getBoolean("type", false);
    }
}
