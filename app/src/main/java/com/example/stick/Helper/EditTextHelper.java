package com.example.stick.Helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class EditTextHelper {



    public void openKeyboard(final Context context, final View view){
        if(context == null){
            return;
        }
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);
            }
        }, 100);
    }

    public void closeKeyboard(final Context context, final View view){
        if(context == null){
            return;
        }
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);
            }
        }, 100);
    }
}
