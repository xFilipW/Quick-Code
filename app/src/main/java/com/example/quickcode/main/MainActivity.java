package com.example.quickcode.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.quickcode.R;
import com.example.quickcode.databinding.ActivityMainBinding;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        binding.bottomNavBar.add(new MeowBottomNavigation.Model(1, R.drawable.ic_quiz));
        binding.bottomNavBar.add(new MeowBottomNavigation.Model(2, R.drawable.ic_tutorials));
        binding.bottomNavBar.add(new MeowBottomNavigation.Model(3, R.drawable.ic_saved));

        binding.bottomNavBar.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(binding.fragment.getId(), QuizzesFragment.newInstance(), QuizzesFragment.TAG)
                                .commit();
                        break;
                    case 2:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(binding.fragment.getId(), MainFragment.newInstance(), MainFragment.TAG)
                                .commit();
                        break;
                    case 3:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(binding.fragment.getId(), new Fragment(), "grafasf 2")
                                .commit();
                }
                return null;
            }
        });

        binding.navigation.setCheckedItem(R.id.tutorials);
        binding.navigation.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.tutorials) {
                // TODO: 14.03.2024
            } else if (id == R.id.quizzes) {
                // TODO: 14.03.2024
            } else {
                // TODO: 14.03.2024
            }

            return true;
        });

    }
}
