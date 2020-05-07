package com.example.stick.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemePreference {
    private static final String THEME_PREF_NAME = "ThemePreference";
    private static ThemePreference mInstance;
    private Context mContext;

    //Const
    public static final int LIGHT = 0;
    public static final int DARK = 1;

    private ThemePreference(Context ctx){
        mContext = ctx;
    }

    public static synchronized ThemePreference getInstance(Context ctx){
        if(mInstance == null) mInstance = new ThemePreference(ctx.getApplicationContext());
        return mInstance;
    }

    public void saveThemePreference(int preference){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(THEME_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("themePreference", preference);
        editor.apply();
    }

    public void quickSaveThemePreference(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(THEME_PREF_NAME, Context.MODE_PRIVATE);
        int preferenceInt = sharedPreferences.getInt("themePreference", 0);
        preferenceInt = preferenceInt == 0 ? 1 : 0;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("themePreference", preferenceInt);
        editor.apply();
    }


    public int getThemePreference(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(THEME_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("themePreference", 0);
    }

    public boolean isDarkTheme(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(THEME_PREF_NAME, Context.MODE_PRIVATE);
        int preference = sharedPreferences.getInt("themePreference", 0);
        return preference == 1;
    }
}
