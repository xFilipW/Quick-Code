package com.example.quickcode.verify;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.quickcode.R;
import com.example.quickcode.databinding.ActivityVerifyEmailBinding;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private long futureTime = Integer.MIN_VALUE;
    private ActivityVerifyEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int navigationBarColor = Color.parseColor("#F1F1F1");
        getWindow().setNavigationBarColor(navigationBarColor);
        WindowInsetsControllerCompat windowInsetsControllerCompat = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsControllerCompat.setAppearanceLightNavigationBars(true);

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (futureTime == Integer.MIN_VALUE)
                    futureTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);

                long currentTime = System.currentTimeMillis();
                long l = futureTime - currentTime;
                long seconds = l / 1000;

                if (seconds < -1) {
                    handler.removeCallbacksAndMessages(null);
                }

                binding.timer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));

                handler.postDelayed(this, 100);
            }
        };

        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
