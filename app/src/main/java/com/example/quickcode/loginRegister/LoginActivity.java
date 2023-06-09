package com.example.quickcode.loginRegister;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quickcode.R;
import com.example.quickcode.common.cleaningEditTexts.CleanUpFragment;
import com.example.quickcode.common.simples.SimpleOnTabSelectedListener;
import com.example.quickcode.databinding.ActivityLoginBinding;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int navigationBarColor = Color.parseColor("#F1F1F1");
        getWindow().setNavigationBarColor(navigationBarColor);
        WindowInsetsControllerCompat windowInsetsControllerCompat = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsControllerCompat.setAppearanceLightNavigationBars(true);

        TabLayout.Tab first = binding.tabLayout.newTab().setText(R.string.log_in_page_tab_layout_label_tab1_title);
        TabLayout.Tab second = binding.tabLayout.newTab().setText(R.string.log_in_page_tab_layout_label_tab2_title);

        binding.tabLayout.addTab(first);
        binding.tabLayout.addTab(second);
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        LoginAdapter adapter = new LoginAdapter(this);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(position == 0 ? first : second);
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition(), true);
                removeToolTipForTabs();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                String itemTag = ((LoginAdapter) binding.viewPager.getAdapter()).getItemTag(tab.getPosition());
                CleanUpFragment fragmentByTag = (CleanUpFragment) getSupportFragmentManager().findFragmentByTag(itemTag);

                if (fragmentByTag != null) {
                    fragmentByTag.removeTextWatchers();
                    fragmentByTag.cleanViews();
                    fragmentByTag.restorePasswordTransformationMethods();
                    fragmentByTag.addTextWatchers();
                }
            }
        });

        binding.viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        removeToolTipForTabs();
    }

    private void removeToolTipForTabs() {
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = binding.tabLayout.getTabAt(i);

            if(tabAt != null) {
                TooltipCompat.setTooltipText(tabAt.view, null);
            }

        }
    }
}