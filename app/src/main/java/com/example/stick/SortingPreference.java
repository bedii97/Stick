package com.example.stick;

import android.content.Context;

public class SortingPreference {

    private static final String USER_PREF_NAME = "token_shared_pref";
    private static SortingPreference mInstance;
    private Context mContext;

    private SortingPreference(Context ctx){
        mContext = ctx;
    }

    public static synchronized SortingPreference getInstance(Context ctx){
        if(mInstance == null) mInstance = new SortingPreference(ctx.getApplicationContext());
        return mInstance;
    }

    public void saveNotePreference(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(USER_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("UserToken", token);
        editor.apply();
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(USER_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserToken", "empty");
    }
}
