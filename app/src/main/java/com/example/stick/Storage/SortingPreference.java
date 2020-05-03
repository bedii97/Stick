package com.example.stick.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SortingPreference {

    private static final String SORTING_PREF_NAME = "SortingPreference";
    private static SortingPreference mInstance;
    private Context mContext;

    //Const
    public static final int ALPHABETIC = 0;
    public static final int BY_DATE = 1;

    private SortingPreference(Context ctx){
        mContext = ctx;
    }

    public static synchronized SortingPreference getInstance(Context ctx){
        if(mInstance == null) mInstance = new SortingPreference(ctx.getApplicationContext());
        return mInstance;
    }

    public void saveNotePreference(int preference){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SORTING_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("notePreference", preference);
        editor.apply();
    }

    public int getNotePreference(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SORTING_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("notePreference", 1);
    }

    public void saveTaskPreference(int preference){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SORTING_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("taskPreference", preference);
        editor.apply();
    }

    public int getTaskPreference(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SORTING_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("taskPreference", 1);
    }
}
