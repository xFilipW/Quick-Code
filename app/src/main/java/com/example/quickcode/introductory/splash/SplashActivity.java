package com.example.quickcode.introductory.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.example.quickcode.R;
import com.example.quickcode.introductory.onBoarding.OnBoardingActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView imgBg, imgLogo;
    TextView text;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        imgBg = findViewById(R.id.imageBg);
        imgLogo = findViewById(R.id.imageLogo);
        text = findViewById(R.id.text);
        lottie = findViewById(R.id.lottie);

        imgBg.animate().translationY(-2600).setDuration(1000).setStartDelay(4000);
        imgLogo.animate().translationY(2200).setDuration(1000).setStartDelay(4000);
        text.animate().translationY(2200).setDuration(1000).setStartDelay(4000);
        lottie.animate().translationY(2200).setDuration(1000).setStartDelay(4000);

        startActivity(new Intent(this, OnBoardingActivity.class));

    }

}
