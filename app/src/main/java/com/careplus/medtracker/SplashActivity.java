package com.careplus.medtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {
    int dd, mm;
    String date = "";
    String month, month_date;

    SharedPreferences pref_date, pref_login;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref_date = getSharedPreferences("todays_date", Context.MODE_PRIVATE);
        pref_login = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = pref_date.edit();

        String[] months = getResources().getStringArray(R.array.months);

        // Getting today's date & setting it to the textView
        Calendar ca = Calendar.getInstance();
        mm = ca.get(Calendar.MONTH);
        dd = ca.get(Calendar.DATE);

        // Getting date & month as String
        if (dd < 10)
            date = "0";
        date += dd;
        month = months[mm].substring(0,3);
        month_date = month + " " + date;

        editor.putString("month_date", month_date);
        editor.commit();

        // Hiding Toolbar
        getSupportActionBar().hide();

        // Going to LoginActivity after 1500 ms
        new Handler().postDelayed(() -> {
            if (pref_login.getBoolean("login", false))
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 1500);
    }
}