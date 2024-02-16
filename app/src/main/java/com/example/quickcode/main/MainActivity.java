package com.example.quickcode.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcode.R;
import com.example.quickcode.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(
//                    R.id.fragment,
//                    MainFragment.newInstance(), MainFragment.TAG).commit();
//        }
    }
}
