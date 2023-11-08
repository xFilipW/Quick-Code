package com.example.quickcode.introductory.onBoarding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.quickcode.R;
import com.example.quickcode.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {

    public static final float ROUND_SQUARE_HEIGHT = 1.07f;
    public static final int ANIMATION_START_DELAY = 4000;
    public static final int ANIMATION_DURATION = 1100;
    private ActivityOnBoardingBinding binding;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        super.onCreate(savedInstanceState);

        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int realMetrics = getRealMetrics();
        setBackgroundImageHeight(realMetrics);
        binding.imageBg.setImageResource(R.drawable.img_on_boarding_background);
        binding.pager.setAdapter(new OnBoardingAdapter(getSupportFragmentManager()));
        translateViews(realMetrics);
    }

    private void setBackgroundImageHeight(int realMetrics) {
        ViewGroup.LayoutParams layoutParams = binding.imageBg.getLayoutParams();
        layoutParams.height = realMetrics;
        binding.imageBg.setLayoutParams(layoutParams);
    }

    private int getRealMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        return (int) (displayMetrics.heightPixels * ROUND_SQUARE_HEIGHT);
    }

    private void translateViews(int translateYValue) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            translateView(binding.imageBg, translateYValue * -1);
            translateView(binding.imageLogo, translateYValue);
            translateView(binding.text, translateYValue);
            translateView(binding.lottie, translateYValue);

            animation = AnimationUtils.loadAnimation(this, R.anim.on_boarding_show_up);
            binding.pager.startAnimation(animation);
        });
    }

    @NonNull
    private void translateView(View view, int value) {
        view.animate().translationY(value).setDuration(ANIMATION_DURATION).setStartDelay(ANIMATION_START_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}