package com.example.quickcode.introductory.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.quickcode.R;
import com.example.quickcode.introductory.onBoarding.OnBoardingActivity;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final Runnable runnable = () -> {
        runIntroductory();
        finish();
    };
    private final int DELAY_MILLIS = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    public void runIntroductory() {
        startActivity(new Intent(this, OnBoardingActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, DELAY_MILLIS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

}
