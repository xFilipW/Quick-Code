package com.example.quickcode.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.quickcode.R;
import com.example.quickcode.databinding.FragmentMainBinding;
import com.example.quickcode.main.adapters.RecyclerViewLanguagesAdapter;
import com.example.quickcode.main.adapters.RecyclerViewTutorialsAdapter;
import com.example.quickcode.main.viewHolders.ItemDividerDecoration;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    private FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.toolbar.getLayoutParams();
            layoutParams.topMargin = systemBarsInsets.top;
            binding.toolbar.setLayoutParams(layoutParams);

            return insets;
        });

        binding.recyclerLanguages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewLanguagesAdapter languagesAdapter = new RecyclerViewLanguagesAdapter();
        languagesAdapter.setData(Repository.PROGRAMMING_LANGUAGE_DATA_LIST);
        binding.recyclerLanguages.setAdapter(languagesAdapter);
        binding.recyclerLanguages.addItemDecoration(new ItemDividerDecoration((int) getResources().getDimension(R.dimen.divider_width)));

        binding.recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        RecyclerViewTutorialsAdapter tutorialsAdapter = new RecyclerViewTutorialsAdapter();
        tutorialsAdapter.setData(Repository.PROGRAMMING_TUTORIAL_DATA_LIST);
        binding.recyclerViewTutorials.setAdapter(tutorialsAdapter);

        binding.bottomNavBar.add(new MeowBottomNavigation.Model(1, R.drawable.ic_quiz));
        binding.bottomNavBar.add(new MeowBottomNavigation.Model(2, R.drawable.ic_tutorials));
        binding.bottomNavBar.add(new MeowBottomNavigation.Model(4, R.drawable.ic_saved));

        binding.bottomNavBar.show(2, true);
    }
}