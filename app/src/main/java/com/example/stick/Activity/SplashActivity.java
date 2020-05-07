package com.example.stick.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stick.R;
import com.example.stick.Storage.ThemePreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setAppTheme() {
        ThemePreference preference = ThemePreference.getInstance(this);
        if (preference.isDarkTheme()) setTheme(R.style.AppThemeDark);
        else setTheme(R.style.AppTheme);
    }
}