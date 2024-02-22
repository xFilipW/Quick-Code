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

import com.example.quickcode.R;
import com.example.quickcode.databinding.FragmentMainBinding;
import com.example.quickcode.main.adapters.RecyclerViewLanguagesAdapter;
import com.example.quickcode.main.adapters.RecyclerViewTutorialsAdapter;
import com.example.quickcode.main.data.ProgrammingLanguageData;
import com.example.quickcode.main.data.ProgrammingTutorialData;

import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    public static final String TAG = "MainFragment";
    RecyclerViewLanguagesAdapter languagesAdapter;
    RecyclerViewTutorialsAdapter tutorialsAdapter;
    List<ProgrammingLanguageData> programmingLanguageDataList;
    List<ProgrammingTutorialData> programmingTutorialDataList;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        programmingLanguageDataList = Arrays.asList(
                new ProgrammingLanguageData(R.drawable.ic_java, "Java"),
                new ProgrammingLanguageData(R.drawable.ic_python, "Python"),
                new ProgrammingLanguageData(R.drawable.ic_cs, "C#"),
                new ProgrammingLanguageData(R.drawable.ic_cpp, "C++"),
                new ProgrammingLanguageData(R.drawable.ic_android, "Android Apps"),
                new ProgrammingLanguageData(R.drawable.ic_html, "Html"),
                new ProgrammingLanguageData(R.drawable.ic_css, "Css"),
                new ProgrammingLanguageData(R.drawable.ic_js, "Java Script"),
                new ProgrammingLanguageData(R.drawable.ic_php, "Php"),
                new ProgrammingLanguageData(R.drawable.ic_mysql, "Mysql")
        );

        binding.recyclerLanguages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true));
        languagesAdapter = new RecyclerViewLanguagesAdapter();
        languagesAdapter.setLanguageData(programmingLanguageDataList);
        binding.recyclerLanguages.setAdapter(languagesAdapter);

        programmingTutorialDataList = Arrays.asList(
                new ProgrammingTutorialData(R.drawable.ic_cpp, "Python", "How to make a simple calculator"),
                new ProgrammingTutorialData(R.drawable.ic_cpp, "C#", "How to make a simple calculator"),
                new ProgrammingTutorialData(R.drawable.ic_cpp, "C++", "How to make a simple calculator"),
                new ProgrammingTutorialData(R.drawable.ic_cpp, "Html", "How to make a simple calculator"),
                new ProgrammingTutorialData(R.drawable.ic_cpp, "Css", "How to make a simple calculator"),
                new ProgrammingTutorialData(R.drawable.ic_cpp, "Java script", "How to make a simple calculator"),
                new ProgrammingTutorialData(R.drawable.ic_cpp, "PHP", "How to make a simple calculator"),
                new ProgrammingTutorialData(R.drawable.ic_cpp, "MySql", "How to make a simple calculator")
        );

        binding.recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true));
        tutorialsAdapter = new RecyclerViewTutorialsAdapter();
        tutorialsAdapter.setTutorialData(programmingTutorialDataList);
        binding.recyclerViewTutorials.setAdapter(tutorialsAdapter);
    }

}
