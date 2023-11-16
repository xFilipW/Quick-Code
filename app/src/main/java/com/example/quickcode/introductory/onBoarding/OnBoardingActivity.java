package com.example.quickcode.introductory.onBoarding;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.quickcode.R;
import com.example.quickcode.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity implements StatusBarListener {

    public static final float ROUND_SQUARE_HEIGHT = 1.07f;
    public static final int ANIMATION_START_DELAY = 4000;
    public static final int ANIMATION_DURATION = 1100;
    private ActivityOnBoardingBinding binding;

    private boolean inProgress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int realMetrics = getRealMetrics();
        setBackgroundImageHeight(realMetrics);
        binding.imageBg.setImageResource(R.drawable.png_on_boarding_background);
        binding.pager.setAdapter(new OnBoardingAdapter(getSupportFragmentManager()));
        binding.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (inProgress)
                    return;

                if (position == 0) {
                    setStatusBarTextColorDark();
                } else {
                    setStatusBarTextColorLight();
                }
            }
        });
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

            translateView(binding.lottie, translateYValue).setListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animator) {
                    setStatusBarTextColorLight();
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    inProgress = false;
                    setStatusBarTextColorDark();
                }
            });
        });
    }

    @NonNull
    private ViewPropertyAnimator translateView(View view, int value) {
        return view.animate()
                .translationY(value)
                .alpha(0)
                .setDuration(ANIMATION_DURATION)
                .setStartDelay(ANIMATION_START_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void setStatusBarTextColorLight() {
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(false);
    }

    @Override
    public void setStatusBarTextColorDark() {
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(true);
    }
}