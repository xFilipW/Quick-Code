package com.example.quickcode.introductory.onBoarding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.quickcode.R;
import com.example.quickcode.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {

    public static final float ROUND_SQUARE_HEIGHT = 1.07f;
    private ActivityOnBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageBg.setImageResource(R.drawable.img_on_boarding_background);

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightInPixels = binding.getRoot().getHeight();

                ViewGroup.LayoutParams layoutParams = binding.imageBg.getLayoutParams();
                int translateYValue = (int) ((heightInPixels * ROUND_SQUARE_HEIGHT));
                layoutParams.height = translateYValue;
                binding.imageBg.setLayoutParams(layoutParams);

                binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);

                onBackgroundImageHeightSet(translateYValue);
            }
        });

        binding.pager.setAdapter(new OnBoardingAdapter(getSupportFragmentManager()));
    }

    private void onBackgroundImageHeightSet(int translateYValue) {
        int expectedHeightToTranslate = translateYValue;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            binding.imageBg.animate().translationY(expectedHeightToTranslate * -1).setDuration(1100).setStartDelay(4000);
            binding.imageLogo.animate().translationY(expectedHeightToTranslate).setDuration(1100).setStartDelay(4000);
            binding.text.animate().translationY(expectedHeightToTranslate).setDuration(1100).setStartDelay(4000);
            binding.lottie.animate().translationY(expectedHeightToTranslate).setDuration(1100).setStartDelay(4000);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}